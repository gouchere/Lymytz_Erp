-- Function: synchronise_employe_mutuelle()

-- DROP FUNCTION synchronise_employe_mutuelle();

CREATE OR REPLACE FUNCTION synchronise_employe_mutuelle()
  RETURNS integer AS
$BODY$
DECLARE 
	id_ RECORD;
	emp_ bigint default 0;
	i int default 0;
    
BEGIN
	for id_ in select id from yvs_grh_employes
	loop
		select into emp_ id from yvs_mut_employe where employe = id_.id;
		if(emp_ is null or emp_ < 1)then
			insert into yvs_mut_employe("employe") values (id_.id);
			i = i+1;
		end if;
	end loop;
	return i;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION synchronise_employe_mutuelle()
  OWNER TO postgres;
