ALTER TABLE yvs_base_conditionnement_depot RENAME CONSTRAINT yvs_com_contenu_doc_stock_author_fkey  TO yvs_base_conditionnement_depot_author_fkey;
ALTER TABLE yvs_base_conditionnement_fournisseur RENAME CONSTRAINT yvs_com_contenu_doc_stock_author_fkey TO yvs_base_conditionnement_fournisseur_author_fkey;
ALTER TABLE yvs_base_model_reglement RENAME CONSTRAINT yvs_model_de_reglement_societe_fkey TO yvs_base_model_reglement_societe_fkey ;
ALTER TABLE yvs_compta_caisse_piece_achat RENAME CONSTRAINT yvs_compta_piece_caisse_mission_caissier_fkey TO yvs_compta_caisse_piece_achat_caissier_fkey ;
ALTER TABLE yvs_compta_phase_acompte_achat RENAME CONSTRAINT yvs_compta_phase_piece_author_fkey TO yvs_compta_phase_acompte_achat_author_fkey ;
ALTER TABLE yvs_compta_phase_acompte_achat RENAME CONSTRAINT yvs_compta_phase_piece_phase_reg_fkey TO yvs_compta_phase_acompte_achat_phase_reg_fkey ;
ALTER TABLE yvs_compta_phase_acompte_vente RENAME CONSTRAINT yvs_compta_phase_piece_author_fkey TO yvs_compta_phase_acompte_vente_author_fkey ;
ALTER TABLE yvs_compta_phase_acompte_vente RENAME CONSTRAINT yvs_compta_phase_piece_phase_reg_fkey TO yvs_compta_phase_acompte_vente_phase_reg_fkey ;


-- Function: alter_delete_doublon_contrainte(boolean)
-- DROP FUNCTION alter_delete_doublon_contrainte(boolean);
CREATE OR REPLACE FUNCTION alter_delete_doublon_contrainte(alter_contraint boolean)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	table_name_ character varying;
	constraint_ record;	
	colonne_ character varying;
	compteur_ integer default 0;
	
BEGIN

	for table_name_ in select tablename from pg_tables where tablename not like 'pg_%' and schemaname = 'public'
	loop	
		-- Recherche des clé secondaires
		FOR colonne_ IN SELECT DISTINCT(k.COLUMN_NAME) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
			INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			WHERE k.table_schema = 'public' AND k.TABLE_NAME = table_name_ AND c.CONSTRAINT_TYPE = 'FOREIGN KEY'
		LOOP
			compteur_ = 0;
			-- Recherche des occurences de la clé secondaire
			for constraint_ in SELECT k.CONSTRAINT_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
				INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				WHERE k.table_schema = 'public' AND k.TABLE_NAME = table_name_ AND c.TABLE_NAME = table_name_ AND c.CONSTRAINT_TYPE = 'FOREIGN KEY' AND k.COLUMN_NAME = colonne_
			loop
				if(compteur_ > 0)then
					RAISE NOTICE 'table_name_ %', table_name_;
					RAISE NOTICE 'colonne_ %', colonne_;
					RAISE NOTICE 'constraint_ %', constraint_.CONSTRAINT_NAME;
					--Suppression de la contrainte
					execute 'ALTER TABLE public.'||table_name_||' DROP CONSTRAINT '||constraint_.CONSTRAINT_NAME;	
				end if;
				compteur_ = compteur_ + 1;
			end loop;	
		END LOOP;
	end loop;	

	if(alter_contraint)then
		for table_name_ in select tablename from pg_tables where tablename not like 'pg_%' and schemaname = 'public'
		loop	
			-- Recherche de la clé secondaire liée a la clé primaire donnée
			FOR colonne_ IN SELECT DISTINCT(k.COLUMN_NAME) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
				INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				WHERE k.table_schema = 'public' AND k.TABLE_NAME = table_name_ AND c.CONSTRAINT_TYPE = 'FOREIGN KEY'
			LOOP
				SELECT INTO constraint_ f.TABLE_NAME  
				FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
				INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
				WHERE k.table_schema = 'public' AND k.TABLE_NAME = table_name_ AND c.CONSTRAINT_TYPE = 'FOREIGN KEY' AND k.COLUMN_NAME = colonne_;
				
				PERFORM alter_action_colonne_key(constraint_.TABLE_NAME, table_name_, true, true);	
			END LOOP;
		end loop;	
	end if;	
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_delete_doublon_contrainte(boolean)
  OWNER TO postgres;

  
-- Function: alter_action_colonne_key(character varying, boolean, boolean)
DROP FUNCTION alter_action_colonne_key(character varying, boolean, boolean);
CREATE OR REPLACE FUNCTION alter_action_colonne_key(table_ character varying, update_ boolean, delete_ boolean)
  RETURNS CHARACTER VARYING AS
$BODY$
  DECLARE 	
	table_name_ character varying;
	constraint_ record;	
	query_ character varying;
	compteur_ integer default 0;
	key_ CHARACTER VARYING DEFAULT ''; 
	
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
			key_ = constraint_.COLUMN_NAME_;
			--Suppression de la contrainte
			execute 'ALTER TABLE public.'||table_name_||' DROP CONSTRAINT '||constraint_.CONSTRAINT_NAME;
			
			query_ = 'ALTER TABLE public.'||table_name_||'
					ADD CONSTRAINT '||table_name_||'_'||constraint_.COLUMN_NAME||'_fkey FOREIGN KEY ('||constraint_.COLUMN_NAME||')
					REFERENCES public.'||table_||' ('||constraint_.COLUMN_NAME_||') MATCH SIMPLE';
			if(update_ = true and delete_ = true)then
				query_ = query_ || ' ON UPDATE CASCADE ON DELETE CASCADE';
			elsif(update_ = true and delete_ = false)then
				query_ = query_ || ' ON UPDATE CASCADE ON DELETE NO ACTION';
			elsif(update_ = false and delete_ = true)then
				query_ = query_ || ' ON UPDATE NO ACTION ON DELETE CASCADE';
			elsif(update_ = false and delete_ = false)then
				query_ = query_ || ' ON UPDATE NO ACTION ON DELETE NO ACTION';
			end if;
			execute query_;
			
			RAISE NOTICE ' constraint N° : %',compteur_ ||' -- table : '||table_name_||' /colonne : '||constraint_.COLUMN_NAME||' /contrainte : '||constraint_.CONSTRAINT_NAME;
		END LOOP;
	end loop;
	return key_;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_action_colonne_key(character varying, boolean, boolean)
  OWNER TO postgres;

  
-- Function: action_on_all_tables_maj()
-- DROP FUNCTION action_on_all_tables_maj();
CREATE OR REPLACE FUNCTION action_on_all_tables_maj()
  RETURNS trigger AS
$BODY$    
DECLARE
	id_ bigint;
	oldId bigint;
	action_ character varying;
	
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF(action_='INSERT' OR action_='UPDATE') THEN  
		id_ = NEW.id;
	ELSE
		id_ = OLD.id;
	END IF;
	INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, groupe_table, to_listen, action_name) VALUES (TG_TABLE_NAME, id_, current_timestamp, '', true, action_);
	IF(action_='INSERT' OR action_='UPDATE') THEN  
		RETURN NEW;
	ELSE
		RETURN OLD;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_all_tables_maj()
  OWNER TO postgres;
