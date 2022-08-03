-- Function: compta_insert_piece_mission()

-- DROP FUNCTION compta_insert_piece_mission();

CREATE OR REPLACE FUNCTION compta_insert_piece_mission()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	mission_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';

	avance_ double precision default 0;
	frais_ double precision default 0;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
	
BEGIN	
	IF(EXEC_) THEN	
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  e.agence, m.numero_mission, m.employe, e.nom, e.prenom FROM yvs_compta_caisse_piece_mission pm 
				INNER JOIN yvs_grh_missions m ON m.id=pm.mission INNER JOIN yvs_grh_employes e ON e.id = m.employe WHERE m.id=NEW.mission LIMIT 1; 
										
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
			END IF;
			IF(line_.nom IS NOT NULL) THEN
				nom_=line_.nom;
			END IF;
			IF(line_.prenom IS NOT NULL) THEN
				prenom_=line_.prenom;
			END IF;
			mission_ = NEW.mission;
		ELSE
			mission_ = OLD.mission;
		END IF;         
		IF(action_='INSERT') THEN
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, caissier, statut_piece, 
													agence, author, caisse, mouvement, tiers_employe, tiers_externe, name_tiers, model)
				   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'MISSION', NEW.montant,line_.numero_mission, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement, NEW.caissier, NEW.statut_piece, 
						   line_.agence,NEW.author, NEW.caisse, 'D',line_.employe,NULL, (nom_ ||' '||prenom_), NEW.model);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='MISSION' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, reference_externe=line_.numero_mission, date_mvt=NEW.date_piece, 
					date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', agence=line_.agence,
					tiers_employe=line_.employe, name_tiers=(nom_ ||' '||prenom_), model = NEW.model, author=NEW.author, date_update=NEW.date_update
					WHERE table_externe='MISSION' AND id_externe= NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, caissier, statut_piece, 
													agence, author, caisse, mouvement, tiers_employe, tiers_externe, name_tiers, model)
				   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'MISSION', NEW.montant,line_.numero_mission, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement, NEW.caissier, NEW.statut_piece, 
						   line_.agence,NEW.author, NEW.caisse, 'D',line_.employe,NULL,(nom_ ||' '||prenom_), NEW.model);
			END IF;
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='MISSION' AND id_externe=OLD.id;
		END IF;
		SELECT INTO frais_ SUM(y.montant) FROM yvs_grh_frais_mission y WHERE y.mission = mission_;
		SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_mission y WHERE y.statut_piece = 'P' AND y.mission = mission_;
		IF(COALESCE(avance_, 0) > 0)THEN
			IF(COALESCE(avance_, 0) >= COALESCE(frais_, 0))THEN
				UPDATE yvs_grh_missions SET statut_paiement = 'P' WHERE id = mission_;
			ELSE
				UPDATE yvs_grh_missions SET statut_paiement = 'R' WHERE id = mission_;
			END IF;
		ELSE
			UPDATE yvs_grh_missions SET statut_paiement = 'W' WHERE id = mission_;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_insert_piece_mission()
  OWNER TO postgres;
