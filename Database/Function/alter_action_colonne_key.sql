-- Function: alter_action_colonne_key(character varying, boolean, boolean)

-- DROP FUNCTION alter_action_colonne_key(character varying, boolean, boolean);

CREATE OR REPLACE FUNCTION alter_action_colonne_key(table_ character varying, update_ boolean, delete_ boolean)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	table_name_ character varying;
	constraint_ record;	
	query_ character varying;
	compteur_ integer default 0;
	
BEGIN	
	-- Debut de la modification des action cles primaires 	
	compteur_ = 0;
	RAISE NOTICE 'Table° : %',table_;

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
			compteur_ = compteur_ + 1;
			--Suppression de la contrainte
			execute 'ALTER TABLE public.'||table_name_||' DROP CONSTRAINT '||constraint_.CONSTRAINT_NAME;
			
			query_ = 'ALTER TABLE public.'||table_name_||'
					ADD CONSTRAINT '||table_name_||'_'||constraint_.COLUMN_NAME||'_fkey FOREIGN KEY ('||constraint_.COLUMN_NAME||')
					REFERENCES public.'||table_||' ('||constraint_.COLUMN_NAME_||') MATCH SIMPLE';
			if(update_ = true and delete_ = true)then
				query_ = query_ || ' ON UPDATE CASCADE ON DELETE SET NULL';
			elsif(update_ = true and delete_ = false)then
				query_ = query_ || ' ON UPDATE CASCADE ON DELETE NO ACTION';
			elsif(update_ = false and delete_ = true)then
				query_ = query_ || ' ON UPDATE NO ACTION ON DELETE SET NULL';
			elsif(update_ = false and delete_ = false)then
				query_ = query_ || ' ON UPDATE NO ACTION ON DELETE NO ACTION';
			end if;
			execute query_;
			
			RAISE NOTICE ' constraint N° : %',compteur_ ||' -- table : '||table_name_||' /colonne : '||constraint_.COLUMN_NAME||' /contrainte : '||constraint_.CONSTRAINT_NAME;
		END LOOP;
	end loop;
	RAISE NOTICE '%','';
	return true;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_action_colonne_key(character varying, boolean, boolean)
  OWNER TO postgres;
