-- Function: compta_warning_caisse_divers()
-- DROP FUNCTION compta_warning_caisse_divers();
CREATE OR REPLACE FUNCTION compta_warning_caisse_divers()
  RETURNS trigger AS
$BODY$    
DECLARE	
	divers_ RECORD;
	
	id_ bigint;
	author_ bigint;
	count_ bigint;

	action_ character varying;
	titre_ character varying default 'OD_NON_PLANNIFIE';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN 
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			id_ = NEW.doc_divers;
			author_ = NEW.author; 
		ELSE
			id_ = OLD.doc_divers;
			author_ = OLD.author; 
		END IF;
		SELECT INTO divers_ date_doc, agence FROM yvs_compta_caisse_doc_divers WHERE id = id_;
		SELECT INTO count_ COUNT(p.id) FROM yvs_compta_caisse_piece_divers p WHERE doc_divers = id_;
		PERFORM workflow_add_warning(id_, titre_, 'REGLEMENT', divers_.date_doc::date, 'UPDATE', COALESCE(count_, 0) > 0, divers_.agence, author_);
	END IF;
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		RETURN NEW;
	ELSE
		RETURN OLD;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_warning_caisse_divers()
  OWNER TO postgres;
