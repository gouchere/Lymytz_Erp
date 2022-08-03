-- Function: grh_salaire_change_statut_retenue()

-- DROP FUNCTION grh_salaire_change_statut_retenue();

CREATE OR REPLACE FUNCTION grh_salaire_change_statut_retenue()
  RETURNS trigger AS
$BODY$    
DECLARE
	all_regle_ boolean default true;
	line_ RECORD;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN
	IF(EXEC_) THEN	
		 FOR line_ IN SELECT de.* FROM yvs_grh_detail_prelevement_emps de WHERE de.retenue=NEW.retenue
		 LOOP
			IF(line_.statut_reglement!='P') THEN
			  RETURN NEW;
			END IF;
		 END LOOP;
		 UPDATE yvs_grh_element_additionel SET statut='P', author=NEW.author WHERE id=NEW.retenue;
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_salaire_change_statut_retenue()
  OWNER TO postgres;
