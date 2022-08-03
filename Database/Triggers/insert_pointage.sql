-- Function: insert_pointage()
-- DROP FUNCTION insert_pointage();
CREATE OR REPLACE FUNCTION insert_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE     
    
BEGIN
	IF(TG_OP = 'DELETE' OR TG_OP = 'TRUNCAT')THEN
		RETURN OLD;
	END IF;
	PERFORM reevaluer_total_presence(NEW.presence);
	if (NEW.heure_sortie is null) then
		NEW.heure_pointage = NEW.heure_entree;
	else
		NEW.heure_pointage = NEW.heure_sortie;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_pointage()
  OWNER TO postgres;
