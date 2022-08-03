-- Function: compta_action_on_piece_caisse_virement()

-- DROP FUNCTION compta_action_on_piece_caisse_virement();

CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_virement()
  RETURNS trigger AS
$BODY$    
DECLARE
line_source_ RECORD;
line_cible_ RECORD;
user_ RECORD;
action_ character varying;
id_el_ bigint;
id_ bigint;
id_tiers_ bigint;
frais_ double precision default 0;
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
	
BEGIN	
	IF(EXEC_) THEN	
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			--trouve le nom du caissier
			SELECT INTO user_ u.* FROM yvs_users u WHERE u.id=NEW.caissier_source;
			SELECT INTO line_source_  n.agence FROM yvs_base_caisse c INNER JOIN  yvs_compta_journaux n ON c.journal=n.id WHERE c.id=NEW.source;
			SELECT INTO line_cible_  n.agence FROM yvs_base_caisse c INNER JOIN  yvs_compta_journaux n ON c.journal=n.id WHERE c.id=NEW.cible;
			SELECT INTO frais_ SUM(y.montant) FROM yvs_compta_cout_sup_piece_virement y WHERE y.virement = NEW.id;
			id_ = NEW.id;
		ELSE
			id_ = OLD.id;
		END IF;
		frais_ = COALESCE(frais_, 0);
		IF(action_='INSERT') THEN
			-- InsÃ¨re la sortie
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', (NEW.montant + frais_), NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				line_source_.agence,NEW.author,NEW.source, NEW.caissier_source, NEW.caissier_source, null,'D', user_.nom_users, NEW.model);
			-- InsÃ¨re l'entrÃ©
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement,name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', NEW.montant, NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				line_cible_.agence,NEW.author,NEW.cible, NEW.caissier_cible, NEW.caissier_source, null,'R', user_.nom_users, NEW.model);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=(NEW.montant + frais_), tiers_interne=NEW.caissier_source, model = NEW.model,
					reference_externe=NEW.numero_piece, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
					statut_piece=NEW.statut_piece, caisse=NEW.source, caissier=NEW.caissier_source, mouvement='D', agence=line_source_.agence, name_tiers=user_.nom_users
				WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id AND mouvement='D';
						  ---
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_interne=NEW.caissier_source, model = NEW.model,
					reference_externe=NEW.numero_piece, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
					statut_piece=NEW.statut_piece, caisse=NEW.cible, caissier=NEW.caissier_cible, mouvement='R', agence=line_cible_.agence, name_tiers=user_.nom_users
				WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id AND mouvement='R';
			ELSE
				-- InsÃ¨re la sortie
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement,name_tiers, model)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', (NEW.montant + frais_), NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
					line_source_.agence,NEW.author,NEW.source, NEW.caissier_source, NEW.caissier_source, null,'D',user_.nom_users, NEW.model);
				-- InsÃ¨re l'entrÃ©
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', NEW.montant, NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
					line_cible_.agence,NEW.author,NEW.cible, NEW.caissier_cible, NEW.caissier_source, null,'R',user_.nom_users, NEW.model);
			END IF;	           
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VIREMENT' AND id_externe=OLD.id;
			RETURN OLD;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_virement()
  OWNER TO postgres;
