-- Function: equilibre_achat(bigint, date, date)

-- DROP FUNCTION equilibre_achat(bigint, date, date);

CREATE OR REPLACE FUNCTION maj_migration_data_declaration()
  RETURNS boolean AS
$BODY$
DECLARE
	headers_ record;
	ordres_ record;
	operations_ record;
	line_ record;
	dates_ record;
	id_session_ bigint;
	id_session_of_ bigint;
	id_op_of_ bigint;

BEGIN
	FOR headers_ IN SELECT * from yvs_prod_declaration_production d
					LOOP
						-- Insère la ligne de session prod pour chaque instance
						-- SELECT INTO dates_ s.date_save, s.date_update FROM yvs_prod_of_suivi_flux s WHERE s.date_flux=headers_.date_flux AND s.equipe=headers_.equipe AND s.tranche=headers_.tranche;
							  SELECT INTO id_session_ s.id FROM yvs_prod_session_of s INNER JOIN yvs_prod_session_prod sp ON sp.id=s.session_prod 
														 WHERE sp.date_session=headers_.date_declaration AND sp.depot=headers_.depot AND sp.tranche=headers_.tranche
															AND sp.equipe=headers_.equipe AND s.ordre=headers_.ordre;
								IF(COALESCE(id_session_,0)>0) THEN
									UPDATE yvs_prod_declaration_production SET session_of=id_session_ WHERE id=headers_.id;
								END IF;
					END LOOP;
					return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION maj_migration_data_declaration()
  OWNER TO postgres;
