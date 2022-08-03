-- Function: mut_action_on_financement_activite()

-- DROP FUNCTION mut_action_on_financement_activite();

CREATE OR REPLACE FUNCTION mut_action_on_financement_activite()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	caisse_ RECORD;
	action_ CHARACTER VARYING;
	table_name_ CHARACTER VARYING DEFAULT 'FINANCEMENT_ACTIVITE';
	existe_ BIGINT DEFAULT 0;
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN
	IF(EXEC_) THEN		
		action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_ t.designation FROM yvs_mut_activite a INNER JOIN yvs_mut_evenement e ON a.evenement =e.id INNER JOIN yvs_mut_type_evenement t ON e.type = t.id WHERE a.id = NEW.activite;
			SELECT INTO caisse_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
		END IF;
		--find societe concerné        
		IF(action_='INSERT') THEN
			-- Récupère le document
			INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
				author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
			VALUES (null, NEW.id, 'Contribution '||line_.designation, table_name_, NEW.montant_recu, null, NEW.date_financement, caisse_.caissier, 'P',  NEW.author, 
				NEW.caisse, 'D', null, null, null, NEW.date_update, NEW.date_save);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO existe_ id FROM yvs_mut_mouvement_caisse WHERE table_externe=table_name_ AND id_externe=NEW.id;
			IF(existe_ IS NOT NULL OR existe_ > 0) THEN 	
				UPDATE yvs_mut_mouvement_caisse SET montant = NEW.montant_recu, date_mvt = NEW.date_financement, caisse = NEW.caisse, caissier = caisse_.caissier, 
				tiers_interne = null, name_tiers = null WHERE id = existe_;
			ELSE
				INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
					author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
				VALUES (null, NEW.id, 'Contribution '||line_.designation, table_name_, NEW.montant_recu, null, NEW.date_financement, caisse_.caissier, 'P',  NEW.author, 
					NEW.caisse, 'D', null, null, null, NEW.date_update, NEW.date_save);
			END IF;	
		END IF;
	END IF;
	IF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;		
		RETURN OLD;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_financement_activite()
  OWNER TO postgres;
