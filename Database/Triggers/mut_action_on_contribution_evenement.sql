-- Function: mut_action_on_contribution_evenement()

-- DROP FUNCTION mut_action_on_contribution_evenement();

CREATE OR REPLACE FUNCTION mut_action_on_contribution_evenement()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	caisse_ RECORD;
	action_ CHARACTER VARYING;
	table_name_ CHARACTER VARYING DEFAULT 'CONTRIBUTION_EVENEMENT';
	existe_ BIGINT DEFAULT 0;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		IF(EXEC_) THEN
			SELECT INTO line_ e.etat, c.compte FROM yvs_mut_contribution_evenement c INNER JOIN yvs_mut_evenement e ON e.id=c.evenement INNER JOIN yvs_mut_type_evenement t ON e.type = t.id WHERE c.id = NEW.id;
		END IF;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
		IF(EXEC_) THEN
			-- Récupère le document
			IF(line_.etat IN ('V','R','C'))THEN	 -- Validé, en cours, ou clôturé
				IF(line_.compte IS NULL) THEN 
					-- Insère directement dans la caisse
					PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);
				ELSE
					-- Insère un mouvement de compte
					PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);
				END IF;
			END IF;
		END IF;
	ELSIF (action_='UPDATE') THEN 
		IF(EXEC_) THEN
			IF(line_.etat IN ('V','R','C'))THEN	 -- Validé, en cours, ou clôturé
				IF(line_.compte IS NULL) THEN 
					-- Insère directement dans la caisse
					PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);
				ELSE
					-- Insère un mouvement de compte
					PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);
				END IF;
			ELSE	
				-- supprime les mouvements rattaché
				DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = NEW.id;
				DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = NEW.id;
			END IF;
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = NEW.id;
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = NEW.id;	
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_contribution_evenement()
  OWNER TO postgres;
