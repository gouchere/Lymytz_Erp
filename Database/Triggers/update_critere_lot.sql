-- Function: update_critere_lot()

-- DROP FUNCTION update_critere_lot();

CREATE OR REPLACE FUNCTION update_critere_lot()
  RETURNS trigger AS
$BODY$   
DECLARE
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN
	IF(EXEC_) THEN
		if(NEW.actif != OLD.actif)then
			if(NEW.actif = false)then
				update yvs_com_lot_reception set actif = false where critere = OLD.id;
			end if;
		end if;
	END IF;
	RETURN NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_critere_lot()
  OWNER TO postgres;
