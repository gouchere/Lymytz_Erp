CREATE OR REPLACE FUNCTION insert_emplacement_depot()
  RETURNS trigger AS
$BODY$
DECLARE
    qte REAL;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');   
BEGIN
	IF(EXEC_) THEN
		IF(NEW.defaut = true)THEN
			update yvs_base_emplacement_depot set defaut = false where depot = NEW.depot and id != NEW.id;
		END IF;
	END IF;
	return new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_emplacement_depot()
  OWNER TO postgres;
COMMENT  ON FUNCTION insert_emplacement_depot() IS 'Permet de garantir qu''on ait qu''un unique emplacement par défaut dans un dépôt' ;
