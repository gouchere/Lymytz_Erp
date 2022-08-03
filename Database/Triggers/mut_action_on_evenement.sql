-- Function: mut_action_on_evenement()

-- DROP FUNCTION mut_action_on_evenement();

CREATE OR REPLACE FUNCTION mut_action_on_evenement()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ CHARACTER VARYING;
	table_name_ CHARACTER VARYING DEFAULT 'EVENEMENT';
	existe_ BIGINT DEFAULT 0;
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN
	IF(EXEC_) THEN	
		action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_ * FROM yvs_mut_evenement e INNER JOIN yvs_mut_type_evenement t ON e.type = t.id WHERE e.id = NEW.id;
		END IF;
		IF(action_='INSERT') THEN
			-- Récupère le document
			IF(line_.etat IN ('V','R','C'))THEN	 -- Validé, en cours, ou clôturé
				IF(line_.caisse_event IS NOT NULL) THEN 
					-- Insère directement dans la caisse
					PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);
					UPDATE yvs_mut_contribution_evenement SET id=id WHERE evenement=NEW.id;
				END IF;
			END IF;
		ELSIF (action_='UPDATE') THEN 
			IF(line_.etat IN ('V','R','C'))THEN	 -- Validé, en cours, ou clôturé
				IF(line_.caisse_event IS NOT NULL) THEN 
					-- Insère directement dans la caisse
					PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);
					UPDATE yvs_mut_contribution_evenement SET id=id WHERE evenement=NEW.id;
				END IF;
			ELSE	
				-- supprime les mouvements rattaché
				DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = NEW.id;
				UPDATE yvs_mut_contribution_evenement SET id=id WHERE evenement=NEW.id;
			END IF;		
		END IF;
	END IF;
	IF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;
		UPDATE yvs_mut_contribution_evenement SET id=id WHERE evenement=OLD.id;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_evenement()
  OWNER TO postgres;
