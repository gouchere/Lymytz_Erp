-- Function: compta_action_on_caisse_piece_ecart_vente()

-- DROP FUNCTION compta_action_on_caisse_piece_ecart_vente();

CREATE OR REPLACE FUNCTION compta_action_on_caisse_piece_ecart_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	echeance_ RECORD;
	ecart_ RECORD;
	
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	
	mouvement_ character varying default 'R';
	statut_ character varying default 'W';
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
	
BEGIN	
	IF(EXEC_) THEN
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		IF(action_='INSERT' OR action_='UPDATE') THEN
			id_el_ = NEW.id;
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
			id_el_ = OLD.id;
		END IF;
			
		--find societe concerné
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  ag.societe, pv.numero as num_doc, pv.users, cr.nom_users as nom FROM yvs_com_ecart_entete_vente pv
				LEFT JOIN yvs_com_entete_doc_vente en ON en.id=pv.entete_doc INNER JOIN yvs_users cr ON cr.id=pv.users
				INNER JOIN yvs_agences ag ON ag.id=cr.agence WHERE pv.id=NEW.piece; 									    
			IF(line_.societe IS NULL) THEN
				SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
			END IF;
			SELECT INTO ecart_ * FROM yvs_com_ecart_entete_vente WHERE id = NEW.piece;
			id_tiers_=line_.users;
			IF(line_.nom IS NOT NULL) THEN
				nom_ = line_.nom;
			END IF;
			IF(ecart_.montant > 0)THEN
				mouvement_ = 'R';
			ELSE
				mouvement_ = 'D';
			END IF;
			IF(ecart_.statut = 'V')THEN
				statut_ = 'P';
			ELSE
				statut_ = 'W';
			END IF;
		END IF;         
		IF(action_='INSERT') THEN
			-- Récupère le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total,numero_externe)
				VALUES (ecart_.numero, NEW.id, '', 'ECART_VENTE', ABS(ecart_.montant), line_.num_doc, ecart_.date_ecart, ecart_.date_ecart, ecart_.date_ecart, statut_, 
					line_.societe,NEW.author,NEW.caisse, ecart_.users, id_tiers_, null, COALESCE(mouvement_, 'R'), nom_, NEW.model, 0, 0, line_.num_doc);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='ECART_VENTE' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=ecart_.numero, note='', montant=ABS(ecart_.montant), tiers_interne=id_tiers_, model = NEW.model,
					reference_externe=line_.num_doc, date_mvt=ecart_.date_ecart, date_paiment_prevu=ecart_.date_ecart, date_paye=ecart_.date_ecart, 
					statut_piece=statut_, caisse=NEW.caisse, caissier=ecart_.users, mouvement=COALESCE(mouvement_, 'R'), societe=line_.societe, name_tiers=nom_,
					etape_total=0, etape_valide=0, numero_externe=line_.num_doc
				WHERE table_externe='ECART_VENTE' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total,numero_externe)
				VALUES (ecart_.numero, NEW.id, '', 'ECART_VENTE', ABS(ecart_.montant), line_.num_doc, ecart_.date_ecart, ecart_.date_ecart, ecart_.date_ecart, statut_, line_.societe,NEW.author,
					NEW.caisse, ecart_.users, id_tiers_, null, COALESCE(mouvement_, 'R'), nom_, NEW.model, 0, 0,line_.num_doc);
			END IF;	
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='ECART_VENTE' AND id_externe=OLD.id;	
			RETURN OLD;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_caisse_piece_ecart_vente()
  OWNER TO postgres;
