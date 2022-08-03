-- Function: delete_tiers_no_action(bigint)

-- DROP FUNCTION delete_tiers_no_action(bigint);

CREATE OR REPLACE FUNCTION delete_tiers_no_action(societe_ bigint)
  RETURNS integer AS
$BODY$
DECLARE 
	tiers_ BIGINT;
	total_ INTEGER DEFAULT 0;
BEGIN
	FOR tiers_ IN SELECT tiers FROM audit_tiers(societe_)
	LOOP
		DELETE FROM yvs_base_tiers WHERE id = tiers_;
		total_ = total_ + 1;
	END LOOP;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_tiers_no_action(bigint)
  OWNER TO postgres;
