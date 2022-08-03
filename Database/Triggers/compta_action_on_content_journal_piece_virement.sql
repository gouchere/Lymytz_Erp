DROP TRIGGER compta_action_on_content_journal_piece_virement ON yvs_compta_content_journal_piece_virement;

CREATE TRIGGER compta_action_on_content_journal_piece_virement
  BEFORE INSERT OR UPDATE OR DELETE
  ON yvs_compta_content_journal_piece_virement
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_content_journal_piece_virement();

-- Function: compta_action_on_content_journal_piece_virement()

-- DROP FUNCTION compta_action_on_content_journal_piece_virement();

CREATE OR REPLACE FUNCTION compta_action_on_content_journal_piece_virement()
  RETURNS trigger AS
$BODY$    
DECLARE
c_ BIGINT;	
run_ BOOLEAN ;
action_ character varying ;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE  
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		IF(NEW.execute_trigger='OK') THEN
			run_=false;
		ELSE
			run_=true;
		END IF;
	ELSE
		IF(OLD.execute_trigger='OK') THEN
			run_=false;
		ELSE
			run_=true;
		END IF;
	END IF;
	IF(run_) THEN
	RAISE NOTICE 'Begin...';
		IF(TG_OP='INSERT' OR TG_OP='UPDATE') THEN
			SELECT INTO c_ COUNT(*) FROM yvs_compta_content_journal WHERE ref_externe=NEW.reglement AND table_externe='DOC_VIREMENT';
			IF(c_=4 OR NEW.sens_compta='B') THEN
				RAISE NOTICE 'here equibre...';
				UPDATE yvs_compta_caisse_piece_virement SET comptabilise = TRUE WHERE id = NEW.reglement;
				IF(NEW.sens_compta IS NULL) THEN
					NEW.sens_compta='B';
				END IF;
			ELSE
				UPDATE yvs_compta_caisse_piece_virement SET comptabilise = FALSE WHERE id = NEW.reglement;
			END IF;
			RETURN NEW;
		ELSIF(TG_OP='DELETE' OR TG_OP='TRONCATE') THEN 	
			UPDATE yvs_compta_caisse_piece_virement SET comptabilise = FALSE WHERE id = OLD.reglement;
			RETURN OLD;
		END IF;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_content_journal_piece_virement()
  OWNER TO postgres;
