-- Function: compta_action_on_piece_caisse_divers()

-- DROP FUNCTION compta_action_on_piece_caisse_divers();

CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  dv.societe, dv.num_piece as num_doc, dv.tiers, dv.mouvement FROM yvs_compta_caisse_piece_divers pv 
			INNER JOIN yvs_compta_caisse_doc_divers dv ON dv.id=pv.doc_divers WHERE pv.id=NEW.id; 
								    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		--récupérer nom du tiers
		IF(line_.tiers IS NOT NULL) THEN
		  SELECT INTO tiers_ t.* FROM yvs_base_tiers t WHERE t.id=line_.tiers;
			IF(tiers_.nom IS NOT NULL) THEN
				nom_=tiers_.nom;
			END IF;
			IF(tiers_.prenom IS NOT NULL) THEN
				prenom_=tiers_.prenom;
			END IF;
		END IF;
		
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.num_piece, NEW.id, NEW.note, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_piece,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, line_.mouvement, (nom_ ||' '||prenom_), NEW.mode_paiement);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, model = NEW.mode_paiement,
				date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_piece, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement=line_.mouvement, societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
				WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe, mouvement,name_tiers, model)
				VALUES (NEW.num_piece, NEW.id, NEW.note, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_piece,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,line_.mouvement, (nom_ ||' '||prenom_), NEW.mode_paiement);
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=OLD.id;	
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_divers()
  OWNER TO postgres;
