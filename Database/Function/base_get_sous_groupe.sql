-- Function: base_get_sous_groupe(bigint, boolean)
-- DROP FUNCTION base_get_sous_groupe(bigint, boolean);
CREATE OR REPLACE FUNCTION base_get_sous_groupe(classe_ bigint, clear_table_ boolean)
  RETURNS SETOF bigint AS
$BODY$
declare 
	verify_ bigint;
	sous_ bigint;
begin 	
-- 	DROP TABLE table_sous_groupe;
	CREATE TEMP TABLE IF NOT EXISTS table_sous_groupe(_id bigint);
	if(clear_table_)then
		DELETE FROM table_sous_groupe;
	end if;
	for sous_ in select y.id from yvs_base_groupes_article y where y.groupe_parent = classe_ order by y.id
	loop
		select into verify_ coalesce(_id, 0) from table_sous_groupe where _id = sous_;
		if(coalesce(verify_, 0) < 1)then
			insert into table_sous_groupe values(sous_);
			select into verify_ coalesce(count(y.id), 0) from yvs_base_groupes_article y where y.groupe_parent = sous_;
			if(verify_ > 0)then
				PERFORM base_get_sous_groupe(sous_, false);
			end if;
		end if;
	end loop;
	RETURN QUERY select * from table_sous_groupe order by _id;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION base_get_sous_groupe(bigint, boolean)
  OWNER TO postgres;
