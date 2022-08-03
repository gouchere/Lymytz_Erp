-- Function: mut_action_on_paiement_interet()

-- DROP FUNCTION mut_action_on_paiement_interet();

CREATE OR REPLACE FUNCTION mut_action_on_paiement_interet()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	caisse_ RECORD;
	compte_ RECORD;
	action_ CHARACTER VARYING;
	mouvement_ CHARACTER VARYING DEFAULT 'D';
	table_name_ CHARACTER VARYING DEFAULT 'REGLEMENT_INTERET';
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	
	--find societe concerné        
	IF(action_='INSERT') THEN
		IF(EXEC_) THEN	
			-- Récupère le document
			IF(NEW.statut_paiement='P') THEN 
				IF(NEW.caisse!='' AND NEW.caisse !=null)  THEN
					PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);
				ELSE
					--Insert l'opération de dépôt dans la table opération_compte
					PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);
				END IF;
			END IF;
		END IF;
	ELSIF (action_='UPDATE') THEN 
		IF(EXEC_) THEN	
			IF(NEW.statut_paiement='P')THEN
					IF(NEW.caisse !=null) THEN 
						PERFORM   mut_insert_into_mouv_caisse(NEW.id, table_name_);
					ELSE
						PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);	
					END iF;	
			ELSE
				DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = NEW.id;
				DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = NEW.id;	
			END IF;
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
			DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;	
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = OLD.id;	
			RETURN OLD;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_paiement_interet()
  OWNER TO postgres;
