-- Function: compta_action_on_piece_caisse_achat()

-- DROP FUNCTION compta_action_on_piece_caisse_achat();

CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';
	etape_total_ integer default 0;
	etape_valide_ integer default 0;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
	
BEGIN	
	IF(EXEC_) THEN
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		--find societe concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  dv.agence, dv.num_doc, dv.fournisseur, f.nom, f.prenom FROM yvs_compta_caisse_piece_achat pv 
				INNER JOIN yvs_com_doc_achats dv ON dv.id=pv.achat INNER JOIN yvs_base_fournisseur f ON f.id=dv.fournisseur WHERE pv.id=NEW.id; 
										
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
			END IF;
			IF(NEW.fournisseur IS NOT NULL) THEN
				id_tiers_=NEW.fournisseur;
			ELSE
				id_tiers_=line_.fournisseur;
			END IF;
			IF(line_.nom IS NOT NULL) THEN
				nom_=line_.nom;
			END IF;
			IF(line_.prenom IS NOT NULL) THEN
				prenom_=line_.prenom;
			END IF;
			--rÃ©cupÃ¨re le code tiÃ¨rs de ce fournisseurs
			SELECT INTO id_tiers_ t.tiers FROM yvs_base_fournisseur t WHERE t.id=id_tiers_;		
			-- recherche les Ã©tapes de validation
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece_achat WHERE piece_achat= NEW.id;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece_achat WHERE piece_achat= NEW.id AND phase_ok IS TRUE;
		END IF;         
		IF(action_='INSERT') THEN
			-- RÃ©cupÃ¨re le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, 
			model, etape_valide, etape_total,numero_externe)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.agence,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, 'D',(nom_||' '||prenom_),
				NEW.model,etape_valide_, etape_total_,NEW.reference_externe);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, model = NEW.model, 
					date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', agence=line_.agence, name_tiers=(nom_||' '||prenom_)
					,etape_total=etape_total_, etape_valide=etape_valide_, numero_externe=NEW.reference_externe, author=NEW.author, date_update=NEW.date_update
					WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, agence, author, caisse,caissier, tiers_interne, tiers_externe, mouvement, name_tiers,
				model,etape_valide_, etape_total_,numero_externe)
					VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.agence,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'D',(nom_||' '||prenom_), 
					NEW.model,etape_valide_, etape_total_,NEW.reference_externe);
			END IF;
			UPDATE yvs_compta_notif_reglement_achat SET date_update = current_date WHERE piece_achat = NEW.id;
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_ACHAT' AND id_externe=OLD.id;
			RETURN OLD;			
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_achat()
  OWNER TO postgres;
