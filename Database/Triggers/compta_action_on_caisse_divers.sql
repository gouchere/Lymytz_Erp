-- Function: compta_action_on_caisse_divers()
-- DROP FUNCTION compta_action_on_caisse_divers();
CREATE OR REPLACE FUNCTION compta_action_on_caisse_divers()
  RETURNS trigger AS
$BODY$    
DECLARE	
	delai_ integer;
	duree_ integer;
	agence_ integer;
	mouv_ bigint;
	titre_  CHARACTER VARYING;
	
	action_ character varying;
	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF (action_='UPDATE' and NEW.mouvement != OLD.mouvement) THEN 
		UPDATE yvs_compta_caisse_piece_divers SET date_update = current_timestamp WHERE doc_divers = NEW.id;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_caisse_divers()
  OWNER TO postgres;
