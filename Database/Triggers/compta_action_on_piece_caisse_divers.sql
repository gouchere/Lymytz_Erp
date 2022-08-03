-- Function: compta_action_on_piece_caisse_divers()

-- DROP FUNCTION compta_action_on_piece_caisse_divers();

CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	action_ character varying;
	id_ bigint;
	id_el_ bigint;
	id_tiers_ bigint;
	montant_ double precision default 0 ;
	nom_ character varying default '';
	prenom_ character varying default '';

	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
BEGIN	
	IF(EXEC_) THEN
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  dv.agence, dv.num_piece as num_doc, dv.id_tiers, dv.table_tiers , dv.mouvement, dv.description FROM yvs_compta_caisse_piece_divers pv 
				INNER JOIN yvs_compta_caisse_doc_divers dv ON dv.id=pv.doc_divers WHERE pv.id=NEW.id; 
										
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
			END IF;
			--rÃ©cupÃ©rer nom du tiers
			IF(line_.id_tiers IS NOT NULL) THEN
			  --SELECT INTO tiers_ t.* FROM yvs_base_tiers t WHERE t.id=line_.tiers;
			   SELECT INTO nom_ name_tiers(line_.id_tiers, line_.table_tiers,'N');
			   SELECT INTO id_tiers_ yvs_give_id_tiers(line_.id_tiers, line_.table_tiers,'N');
			END IF;
			
		END IF;         
		IF(action_='INSERT') THEN
			id_ = NEW.doc_divers;
			-- RÃ©cupÃ¨re le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, 
													statut_piece, agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,numero_externe)
				VALUES (NEW.num_piece, NEW.id, line_.description, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_valider,  
						NEW.statut_piece, line_.agence,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, line_.mouvement, NEW.beneficiaire, NEW.mode_paiement,NEW.reference_externe);
		ELSIF (action_='UPDATE') THEN 
			id_ = NEW.doc_divers;
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_piece, note=line_.description, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, model = NEW.mode_paiement,
					date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_valider, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement=line_.mouvement, agence=line_.agence, name_tiers=NEW.beneficiaire,
					numero_externe=NEW.reference_externe, author=NEW.author, date_update=NEW.date_update
					WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, 
				statut_piece, agence, author, caisse,caissier, tiers_interne, tiers_externe, mouvement,name_tiers, model,numero_externe)
					VALUES (NEW.num_piece, NEW.id, line_.description, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_valider,  
					NEW.statut_piece, line_.agence,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,line_.mouvement, NEW.beneficiaire, NEW.mode_paiement,NEW.reference_externe);
			END IF;
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			id_ = OLD.doc_divers;
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=OLD.id;	
		END IF;
		SELECT INTO montant_ SUM(y.montant) FROM yvs_compta_caisse_piece_divers y WHERE y.statut_piece = 'P' AND y.doc_divers = id_;
		SELECT INTO id_ y.bon FROM yvs_compta_justificatif_bon y WHERE piece = id_;
		if(id_ IS NOT NULL AND id_ > 0)THEN
			UPDATE yvs_compta_mouvement_caisse SET montant = montant - COALESCE(montant_, 0) WHERE table_externe='BON_PROVISOIRE' AND id_externe=id_;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_divers()
  OWNER TO postgres;
