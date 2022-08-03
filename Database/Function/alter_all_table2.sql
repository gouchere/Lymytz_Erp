-- Function: alter_all_table2(character varying, character varying, character varying, boolean)

-- DROP FUNCTION alter_all_table2(character varying, character varying, character varying, boolean);

CREATE OR REPLACE FUNCTION alter_all_table2(schema_ character varying, champs_ character varying, type_ character varying, contraint_ boolean)
  RETURNS boolean AS
$BODY$
DECLARE
    table_ RECORD;
    colonne_ RECORD;
    deja_ BOOLEAN default false;   
    trouve_ BOOLEAN default false;

BEGIN
    for table_ in select tablename from pg_tables where tablename not like 'pg_%' and schemaname = schema_
    loop
        for colonne_ in SELECT column_name, data_type FROM information_schema.columns WHERE table_name = table_.tablename
        loop
            if (colonne_.column_name = champs_ and (colonne_.data_type = 'bigserial' or colonne_.data_type = 'character varying')) then
                deja_ = true;
                exit;
            else
                deja_ = false;
            end if;

            if (colonne_.column_name = 'societe') then
                trouve_ = true;
            else
                trouve_ = false;
            end if;
        end loop;

        if (deja_ = true and trouve_ = false) then
            EXECUTE 'alter table '|| table_.tablename ||' drop column '|| champs_ ||'';
            EXECUTE 'alter table '|| table_.tablename ||' add column '|| champs_ ||' '|| type_ ||'';
            
            if (contraint_ = true) then
                EXECUTE 'alter table '|| table_.tablename ||' add CONSTRAINT '|| table_.tablename||'_'|| champs_ ||'_fkey FOREIGN KEY ('|| champs_ ||') 
                REFERENCES yvs_agences (id) MATCH SIMPLE
                ON UPDATE NO ACTION ON DELETE NO ACTION';
            end if;

            EXECUTE 'update '|| table_.tablename ||' set '|| champs_ ||' = 4';
        end if;
    end loop;
    return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_all_table2(character varying, character varying, character varying, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION alter_all_table2(character varying, character varying, character varying, boolean) IS 'fonction qui modifie toutes les tables en ajoutant un champs specifique
elle prend en parametre le schema de la base de donnee, le champs a ajouter et le type du champs';
