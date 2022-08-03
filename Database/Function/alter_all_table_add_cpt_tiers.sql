-- Function: alter_all_table_add_cpt_tiers()

-- DROP FUNCTION alter_all_table_add_cpt_tiers();

CREATE OR REPLACE FUNCTION alter_all_table_add_cpt_tiers()
  RETURNS boolean AS
$BODY$
DECLARE
    table_ RECORD;
    line_ bigint;
    deja_ BOOLEAN default false;   
    trouve_ BOOLEAN default false;

BEGIN
    for table_ in select * from yvs_grh_employes 
    loop
	select into line_ id from yvs_base_tiers t where t.nom=table_.nom and t.prenom=table_.prenom limit 1;
	IF(line_ IS NOT NULL) THEN
		UPDATE yvs_grh_employes SET compte_tiers=line_ WHERE id=table_.id;
		RAISE NOTICE 'update %',table_.nom;
	END IF;
        
    end loop;
    return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_all_table_add_cpt_tiers()
  OWNER TO postgres;
