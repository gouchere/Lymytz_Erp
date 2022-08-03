CREATE OR REPLACE FUNCTION insert_tiers_telephone()
  RETURNS trigger AS
$BODY$
DECLARE
    qte REAL;
    EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN
	IF(EXEC_) THEN
		IF(NEW.principal = true) THEN
			update yvs_base_tiers_telephone set principal = false where tiers = NEW.tiers and numero != NEW.numero;
		END IF;
	END IF;
	RETURN NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_tiers_telephone()
  OWNER TO postgres;
