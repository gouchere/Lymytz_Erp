-- Function: grh_get_sous_service(bigint, boolean)

-- DROP FUNCTION grh_get_sous_service(bigint, boolean);

CREATE OR REPLACE FUNCTION grh_get_sous_service(IN departement_ bigint, IN clear_table_ boolean)
  RETURNS SETOF bigint AS
$BODY$
declare 
	verify_ bigint;
	service_ bigint;
begin 	
-- 	DROP TABLE table_sous_service;
	CREATE TEMP TABLE IF NOT EXISTS table_sous_service(_id bigint);
	if(clear_table_)then
		DELETE FROM table_sous_service;
	end if;
	for service_ in select y.id from yvs_grh_departement y where y.departement_parent = departement_ order by y.id
	loop
		select into verify_ coalesce(_id, 0) from table_sous_service where _id = service_;
		if(verify_ is null or verify_ < 1)then
			insert into table_sous_service values(service_);
			select into verify_ coalesce(count(y.id), 0) from yvs_grh_departement y where y.departement_parent = service_;
			if(verify_ > 0)then
				PERFORM grh_get_sous_service(service_, false);
			end if;
		end if;
	end loop;
	RETURN QUERY select * from table_sous_service order by _id;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_get_sous_service(bigint, boolean)
  OWNER TO postgres;
