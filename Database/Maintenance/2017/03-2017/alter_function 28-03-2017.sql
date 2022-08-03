-- Function: alter_action_colonne_key(character varying, boolean, boolean)

-- DROP FUNCTION alter_action_colonne_key(character varying, boolean, boolean);

CREATE OR REPLACE FUNCTION fusion_data_for_table(table_ character varying, new_value bigint, old_value bigint)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	table_name_ character varying;
	constraint_ record;	
	query_ character varying;
	
BEGIN	
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
			query_ = 'UPDATE public.'||table_name_||' SET '||constraint_.COLUMN_NAME||' = '||new_value||' WHERE '||constraint_.COLUMN_NAME||' = '||old_value;
			EXECUTE query_;
			RAISE NOTICE 'query_ %',query_;
		END LOOP;
	end loop;
	EXECUTE 'DELETE FROM '||table_||' WHERE id = '||old_value;
	RAISE NOTICE '%','';
	return true;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fusion_data_for_table(character varying, bigint, bigint)
  OWNER TO postgres;
