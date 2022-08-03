-- Function: alter_table_add_index()
-- DROP FUNCTION alter_table_add_index);
CREATE OR REPLACE FUNCTION alter_table_add_index()
  RETURNS boolean AS
$BODY$
  DECLARE 	
	constraint_ record;	
	
	table_name_ character varying;
	query_ character varying;
	index_ character varying;
	
	compteur_ bigint default 0;
	value_ bigint default 0;
	
BEGIN	 
	for table_name_ in select tablename from pg_tables where tablename not like 'pg_%' and schemaname = 'public' order by tablename
	loop	
		RAISE NOTICE 'Table° : %',table_name_;
		-- Recherche de la clé secondaire liée a la clé primaire donnée
		FOR constraint_ IN SELECT k.CONSTRAINT_NAME, k.TABLE_NAME, k.COLUMN_NAME, f.TABLE_NAME AS TABLE_NAME_, f.COLUMN_NAME AS COLUMN_NAME_ 
			FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
			INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			WHERE k.table_schema = 'public' AND c.CONSTRAINT_TYPE = 'FOREIGN KEY' AND k.TABLE_NAME = table_name_
		LOOP
			BEGIN
				compteur_ = 1;
				index_ =  table_name_||'_'||constraint_.COLUMN_NAME||'_idx';
				IF(CHAR_LENGTH(index_) > 63)THEN
					index_ = SUBSTRING(index_, 0, 59)||compteur_||'_idx';
					query_ = 'SELECT COUNT(indexname) FROM pg_indexes WHERE schemaname = ''public'' and indexname = '||QUOTE_LITERAL(index_);
					EXECUTE query_ INTO value_;
					WHILE(COALESCE(value_, 0) > 0)LOOP
						compteur_ = compteur_ + 1;
						index_ = SUBSTRING(index_, 0, 59)||compteur_||'_idx';
						query_ = 'SELECT COUNT(indexname) FROM pg_indexes WHERE schemaname = ''public'' and indexname = '||QUOTE_LITERAL(index_);
						EXECUTE query_ INTO value_;
					END LOOP;
				END IF;
				query_ = 'SELECT COUNT(indexname) FROM pg_indexes WHERE schemaname = ''public'' and indexdef LIKE ''%('||constraint_.COLUMN_NAME||')%'' and tablename = '||QUOTE_LITERAL(table_name_)||'';
				EXECUTE query_ INTO value_;
				RAISE NOTICE 'value_ : %',value_;
				IF(COALESCE(value_, 0) < 1)THEN
					query_ = 'CREATE INDEX '||index_||' ON '||table_name_||' USING btree ('||constraint_.COLUMN_NAME||')';
					EXECUTE query_;
				END IF;
				RAISE NOTICE ' index  : %',' -- table : '||constraint_.TABLE_NAME_||' /colonne : '||constraint_.COLUMN_NAME||' :: '||index_;
			COMMIT;
		END LOOP;
	END LOOP;
	return true;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_table_add_index()
  OWNER TO postgres;
