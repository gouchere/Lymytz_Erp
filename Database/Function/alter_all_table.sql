-- Function: alter_all_table(character varying, character varying, character varying, boolean)

-- DROP FUNCTION alter_all_table(character varying, character varying, character varying, boolean);

CREATE OR REPLACE FUNCTION alter_all_table(schema_ character varying, champs_ character varying, type_ character varying, defaut_ boolean)
  RETURNS boolean AS
$BODY$
DECLARE
    table_ RECORD;
    colonne_ RECORD;
    deja_ BOOLEAN default false;   

BEGIN
    for table_ in select tablename from pg_tables where tablename not like 'pg_%' and schemaname = schema_
    loop
        for colonne_ in SELECT column_name, data_type FROM information_schema.columns WHERE table_name = table_.tablename
        loop
            if (colonne_.column_name = champs_ and colonne_.data_type = type_) then
                deja_ = true;
                exit;
            else
                deja_ = false;
            end if;
        end loop;

        if (deja_ = false) then
            EXECUTE 'alter table '|| table_.tablename ||' add column '|| champs_ ||' '|| type_ ||' default '|| defaut_ ||'';
        else
            EXECUTE 'alter table '|| table_.tablename ||' drop column '|| champs_ ||'';
            EXECUTE 'alter table '|| table_.tablename ||' add column '|| champs_ ||' '|| type_ ||' default '|| defaut_ ||'';
        end if;
    end loop;
    return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_all_table(character varying, character varying, character varying, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION alter_all_table(character varying, character varying, character varying, boolean) IS 'fonction qui modifie toutes les tables en ajoutant un champs specifique
elle prend en parametre le schema de la base de donnee, le champs a ajouter et le type du champs';
