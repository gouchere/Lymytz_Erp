-- Function: insert_tiers_telephone()

-- DROP FUNCTION insert_tiers_telephone();

CREATE OR REPLACE FUNCTION insert_tiers_telephone()
  RETURNS trigger AS
$BODY$
DECLARE
    qte REAL;
    
BEGIN
	if(NEW.principal = true)then
		update yvs_base_tiers_telephone set principal = false where tiers = NEW.tiers and numero != NEW.numero;
	end if;
	return new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_tiers_telephone()
  OWNER TO postgres;

  
  -- Function: update_tiers_telephone()

-- DROP FUNCTION update_tiers_telephone();

CREATE OR REPLACE FUNCTION update_tiers_telephone()
  RETURNS trigger AS
$BODY$
DECLARE
    qte REAL;
    
BEGIN
	if(NEW.principal = true)then
		update yvs_base_tiers_telephone set principal = false where tiers = OLD.tiers and numero != OLD.numero;
	end if;
	return new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_tiers_telephone()
  OWNER TO postgres;
