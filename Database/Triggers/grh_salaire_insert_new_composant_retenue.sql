-- Function: grh_salaire_insert_new_composant_retenue()

-- DROP FUNCTION grh_salaire_insert_new_composant_retenue();

CREATE OR REPLACE FUNCTION grh_salaire_insert_new_composant_retenue()
  RETURNS trigger AS
$BODY$    
DECLARE
	type_ret_ RECORD;
	action_ character varying;
	montant_ double precision default 0;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN
	IF(EXEC_) THEN		
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		IF(action_='INSERT') THEN 
			IF(NEW.actif) THEN 		
				UPDATE yvs_grh_element_additionel SET montant_element=montant_element + NEW.montant WHERE id=NEW.retenue;		
			END IF;
		END IF;
		IF(action_='UPDATE') THEN 
			IF(NEW.actif) THEN 
				-- insert la ligne des composants		
				UPDATE yvs_grh_element_additionel SET montant_element=(montant_element-OLD.montant )+NEW.montant WHERE id=NEW.retenue;
			ELSE
				UPDATE yvs_grh_element_additionel SET montant_element= (montant_element-OLD.montant )-NEW.montant WHERE id=NEW.retenue;
			END IF;
		END IF;
		IF(action_='DELETE' OR action_='TRONCATE') THEN 		
				UPDATE yvs_grh_element_additionel SET montant_element= montant_element-OLD.montant WHERE id=OLD.retenue;
		END IF;
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_salaire_insert_new_composant_retenue()
  OWNER TO postgres;
