-- Function: update_type_creneau_horaire()

-- DROP FUNCTION update_type_creneau_horaire();

CREATE OR REPLACE FUNCTION update_type_creneau_horaire()
  RETURNS trigger AS
$BODY$    
DECLARE 
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN
	IF(EXEC_) THEN 
		if(NEW.actif != OLD.actif)then
			if(NEW.actif = false)then
				update yvs_com_creneau_horaire set actif = false where type = OLD.id;
			end if;
		end if;
	END IF;
	RETURN NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_type_creneau_horaire()
  OWNER TO postgres;
COMMENT ON FUNCTION update_type_creneau_horaire() IS 'Désactive les plannings si l''on désactive une tranche rattachée '