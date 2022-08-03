-- Function: action_on_workflow_conges_emps()

-- DROP FUNCTION action_on_workflow_conges_emps();

CREATE OR REPLACE FUNCTION action_on_workflow_conges_emps()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT') THEN
		--compte les étapes				
	ELSIF (action_='UPDATE' OR action_='DELETE') THEN
		IF(EXEC_) THEN	
			SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_conge WHERE conge=OLD.conge;
			SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_conge WHERE conge=OLD.conge AND etape_valid IS TRUE;
			UPDATE yvs_grh_conge_emps SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=OLD.conge;
		END IF;
	END IF;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_conges_emps()
  OWNER TO postgres;
