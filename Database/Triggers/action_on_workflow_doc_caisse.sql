-- Function: action_on_workflow_doc_caisse()

-- DROP FUNCTION action_on_workflow_doc_caisse();

CREATE OR REPLACE FUNCTION action_on_workflow_doc_caisse()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
	id_ BIGINT DEFAULT 0;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT' OR action_='UPDATE' ) THEN
		id_ = NEW.doc_caisse;
	ELSIF (action_='TRUNCATE' OR action_='DELETE') THEN 
		id_ = OLD.doc_caisse;
	END IF;
	IF(EXEC_) THEN
		SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_doc_caisse WHERE doc_caisse=id_;
		SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_doc_caisse WHERE doc_caisse=id_ AND etape_valid IS TRUE;
		UPDATE yvs_compta_caisse_doc_divers SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=id_;
	END IF;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_doc_caisse()
  OWNER TO postgres;
