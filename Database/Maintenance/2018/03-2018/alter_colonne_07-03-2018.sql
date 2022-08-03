
ALTER TABLE yvs_grh_conge_emps ADD COLUMN etape_total integer;
ALTER TABLE yvs_grh_conge_emps ADD COLUMN etape_valide integer;


-- DROP FUNCTION action_on_workflow_conges_emps();

CREATE OR REPLACE FUNCTION action_on_workflow_conges_emps()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT') THEN
		--compte les étapes				
	ELSIF (action_='UPDATE' OR action_='DELETE') THEN 
		SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_conge WHERE conge=OLD.conge;
		SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_conge WHERE conge=OLD.conge AND etape_valid IS TRUE;
		UPDATE yvs_grh_conge_emps SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=OLD.conge;
	END IF;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_conges_emps()
  OWNER TO postgres;

  
CREATE TRIGGER action_on_workflow_conge
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_workflow_valid_conge
  FOR EACH ROW
  EXECUTE PROCEDURE action_on_workflow_conges_emps();

  

-- DROP FUNCTION init_etape_conge( );

CREATE OR REPLACE FUNCTION init_etape_conge()
  RETURNS boolean  AS
$BODY$
DECLARE 
	conge_ record;
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
BEGIN
-- 	DROP TABLE IF EXISTS table_audit_tiers;	
	FOR conge_ IN SELECT * FROM yvs_grh_conge_emps 
	LOOP
		SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_conge WHERE conge=conge_.id;
		SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_conge WHERE conge=conge_.id AND etape_valid IS TRUE;
		UPDATE yvs_grh_conge_emps SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=conge_.id;
	END LOOP;
return true;	
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION init_etape_conge()
  OWNER TO postgres;
