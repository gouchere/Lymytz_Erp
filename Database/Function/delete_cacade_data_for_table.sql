-- Function: delete_cacade_data_for_table(character varying, bigint)

-- DROP FUNCTION delete_cacade_data_for_table(character varying, bigint);

CREATE OR REPLACE FUNCTION delete_cacade_data_for_table(table_ character varying, id_ bigint)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	line_table_ record;
	constraint_ record;	
	query_ character varying;
	
BEGIN	
	-- Recherche de toutes les tables rattachées a la table actuell
	for line_table_ in SELECT k.CONSTRAINT_NAME, k.TABLE_NAME, k.COLUMN_NAME, f.TABLE_NAME AS TABLE_NAME_, f.COLUMN_NAME AS COLUMN_NAME_ 
			FROM information_schema.KEY_COLUMN_USAGE AS k 
			INNER JOIN information_schema.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			INNER JOIN information_schema.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			WHERE k.table_schema = 'public'  AND f.TABLE_NAME =table_   AND c.CONSTRAINT_TYPE = 'FOREIGN KEY' 
	loop	
		-- Supprimer la ligne de la table 
		EXECUTE 'DELETE FROM '||line_table_.table_name ||' WHERE '||line_table_.column_name ||' = '||id_;
	end loop;
EXECUTE 'DELETE FROM '||table_ ||' WHERE id = '||id_;
	return true;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_cacade_data_for_table(character varying, bigint)
  OWNER TO postgres;
