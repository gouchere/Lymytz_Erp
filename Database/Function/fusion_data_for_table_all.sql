-- Function: fusion_data_for_table_all(character varying, bigint, character varying)
-- DROP FUNCTION fusion_data_for_table_all(character varying, bigint, character varying);
CREATE OR REPLACE FUNCTION fusion_data_for_table_all(table_ character varying, new_value bigint, old_value character varying)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	table_name_ character varying;
	ids_ character varying default '0';
	constraint_ record;	
	query_ character varying;
	value_ bigint;
	verify_ bigint;
	
BEGIN	
	-- Construction de la chaine des old_value
	if(old_value is not null and old_value not in ('', ' '))then
		for query_ in select val from regexp_split_to_table(old_value,',') val
		loop
			ids_ = ids_ || ','||query_;
		end loop;
	end if;
	-- Recherche de toutes les tables rattachées a la table actuell
	for table_name_ in select tablename from pg_tables where tablename not like 'pg_%' and schemaname = 'public' order by tablename
	loop	
		-- Recherche de la clé secondaire liée a la clé primaire donnée
		FOR constraint_ IN SELECT k.CONSTRAINT_NAME, k.TABLE_NAME, k.COLUMN_NAME, f.TABLE_NAME AS TABLE_NAME_, f.COLUMN_NAME AS COLUMN_NAME_ 
			FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
			INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			WHERE k.table_schema = 'public' AND k.TABLE_NAME = table_name_ AND c.CONSTRAINT_TYPE = 'FOREIGN KEY' AND f.TABLE_NAME = table_
		LOOP
			-- Modification de l'ancienne valeur par la nouvelle
			FOR value_ IN EXECUTE 'SELECT id FROM public.'||table_name_||' WHERE '||constraint_.COLUMN_NAME||' in ('||ids_||')'
			LOOP
				SELECT INTO verify_ id FROM yvs_synchro_listen_table WHERE name_table = table_name_ AND id_source = value_::bigint AND action_name = 'UPDATE';
				IF(COALESCE(verify_, 0) > 0)THEN
					UPDATE yvs_synchro_listen_table SET to_listen = TRUE WHERE id = verify_;
				ELSE
					INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
						(table_name_, value_::bigint, current_date, true, 'UPDATE');
				END IF;
			END LOOP;
			query_ = 'UPDATE public.'||table_name_||' SET '||constraint_.COLUMN_NAME||' = '||new_value||' WHERE '||constraint_.COLUMN_NAME||' in ('||ids_||')';
			EXECUTE query_;
			RAISE NOTICE 'query_ %',query_;
		END LOOP;
	end loop;
	FOR value_ IN EXECUTE 'SELECT id FROM public.'||table_||' WHERE id in ('||ids_||')'
	LOOP
		INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
				(table_, value_::bigint, current_date, true, 'DELETE');
	END LOOP;
	EXECUTE 'DELETE FROM '||table_||' WHERE id in ('||ids_||')';
	RAISE NOTICE '%','';
	return true;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fusion_data_for_table_all(character varying, bigint, character varying)
  OWNER TO postgres;
