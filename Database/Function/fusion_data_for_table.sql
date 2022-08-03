-- Function: fusion_data_for_table(character varying, bigint, character varying)
-- DROP FUNCTION fusion_data_for_table(character varying, bigint, character varying);
CREATE OR REPLACE FUNCTION fusion_data_for_table(table_ character varying, new_value bigint, old_value character varying)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	result_ boolean default false;
	value_ bigint;
	verify_ bigint;
BEGIN	
	if(table_ = 'yvs_base_conditionnement')then
		ALTER TABLE yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey;
	elsif(table_ = 'yvs_grh_tranche_horaire')then
		ALTER TABLE yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_tranche_fkey;
	elsif(table_ = 'yvs_niveau_acces')then
		FOR value_ IN SELECT id FROM yvs_autorisation_module WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val)
		LOOP
			INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
				('yvs_autorisation_module', value_::bigint, current_date, true, 'DELETE');
		END LOOP;
		FOR value_ IN SELECT id FROM yvs_autorisation_page_module WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val)
		LOOP
			INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
				('yvs_autorisation_page_module', value_::bigint, current_date, true, 'DELETE');
		END LOOP;
		FOR value_ IN SELECT id FROM yvs_autorisation_ressources_page WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val)
		LOOP
			INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
				('yvs_autorisation_ressources_page', value_::bigint, current_date, true, 'DELETE');
		END LOOP;
		DELETE FROM yvs_autorisation_module WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val);
		DELETE FROM yvs_autorisation_page_module WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val);
		DELETE FROM yvs_autorisation_ressources_page WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val);
	elsif(table_ = 'yvs_base_articles')then
		FOR value_ IN SELECT id FROM yvs_base_article_categorie_comptable WHERE article::character varying in (select val from regexp_split_to_table(old_value,',') val)
		LOOP
			INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
				('yvs_base_article_categorie_comptable', value_::bigint, current_date, true, 'DELETE');
		END LOOP;
		DELETE FROM yvs_base_article_categorie_comptable WHERE COALESCE(article, 0) > 0 AND article::character varying in (select val from regexp_split_to_table(old_value,',') val);
		ALTER TABLE yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_article_fkey;
	end if;
	
	result_ =  fusion_data_for_table_all(table_, new_value, old_value);
	
	if(table_ = 'yvs_base_conditionnement')then
		FOR value_ IN SELECT id FROM yvs_base_mouvement_stock WHERE conditionnement::character varying in (select val from regexp_split_to_table(old_value,',') val)
		LOOP
			SELECT INTO verify_ id FROM yvs_synchro_listen_table WHERE name_table = 'yvs_base_mouvement_stock' AND id_source = value_::bigint AND action_name = 'UPDATE';
			IF(COALESCE(verify_, 0) > 0)THEN
				UPDATE yvs_synchro_listen_table SET to_listen = TRUE WHERE id = verify_;
			ELSE
				INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
					('yvs_base_mouvement_stock', value_::bigint, current_date, true, 'UPDATE');
			END IF;
		END LOOP;
		UPDATE yvs_base_mouvement_stock SET conditionnement = new_value WHERE conditionnement::character varying in (select val from regexp_split_to_table(old_value,',') val);
		ALTER TABLE yvs_base_mouvement_stock
		  ADD CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey FOREIGN KEY (conditionnement)
		      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
		      ON UPDATE CASCADE ON DELETE NO ACTION;
	elsif(table_ = 'yvs_base_articles')then
		FOR value_ IN SELECT id FROM yvs_base_mouvement_stock WHERE article::character varying in (select val from regexp_split_to_table(old_value,',') val)
		LOOP
			SELECT INTO verify_ id FROM yvs_synchro_listen_table WHERE name_table = 'yvs_base_mouvement_stock' AND id_source = value_::bigint AND action_name = 'UPDATE';
			IF(COALESCE(verify_, 0) > 0)THEN
				UPDATE yvs_synchro_listen_table SET to_listen = TRUE WHERE id = verify_;
			ELSE
				INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
					('yvs_base_mouvement_stock', value_::bigint, current_date, true, 'UPDATE');
			END IF;
		END LOOP;
		UPDATE yvs_base_mouvement_stock SET article = new_value WHERE article::character varying in (select val from regexp_split_to_table(old_value,',') val);
		ALTER TABLE yvs_base_mouvement_stock
		  ADD CONSTRAINT yvs_base_mouvement_stock_article_fkey FOREIGN KEY (article)
		      REFERENCES yvs_base_articles (id) MATCH SIMPLE
		      ON UPDATE CASCADE ON DELETE NO ACTION;
	elsif(table_ = 'yvs_grh_tranche_horaire')then
		FOR value_ IN SELECT id FROM yvs_base_mouvement_stock WHERE tranche::character varying in (select val from regexp_split_to_table(old_value,',') val)
		LOOP
			SELECT INTO verify_ id FROM yvs_synchro_listen_table WHERE name_table = 'yvs_base_mouvement_stock' AND id_source = value_::bigint AND action_name = 'UPDATE';
			IF(COALESCE(verify_, 0) > 0)THEN
				UPDATE yvs_synchro_listen_table SET to_listen = TRUE WHERE id = verify_;
			ELSE
				INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
					('yvs_base_mouvement_stock', value_::bigint, current_date, true, 'UPDATE');
			END IF;
		END LOOP;
		UPDATE yvs_base_mouvement_stock SET tranche = new_value WHERE tranche::character varying in (select val from regexp_split_to_table(old_value,',') val);
		ALTER TABLE yvs_base_mouvement_stock
		  ADD CONSTRAINT yvs_base_mouvement_stock_tranche_fkey FOREIGN KEY (tranche)
		      REFERENCES yvs_grh_tranche_horaire (id) MATCH SIMPLE
		      ON UPDATE CASCADE ON DELETE NO ACTION;
	elsif(table_ = 'yvs_base_tiers')then
		FOR value_ IN SELECT id FROM yvs_compta_content_journal WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'TIERS'
		LOOP
			SELECT INTO verify_ id FROM yvs_synchro_listen_table WHERE name_table = 'yvs_compta_content_journal' AND id_source = value_::bigint AND action_name = 'UPDATE';
			IF(COALESCE(verify_, 0) > 0)THEN
				UPDATE yvs_synchro_listen_table SET to_listen = TRUE WHERE id = verify_;
			ELSE
				INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
					('yvs_compta_content_journal', value_::bigint, current_date, true, 'UPDATE');
			END IF;
		END LOOP;
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'TIERS';
	elsif(table_ = 'yvs_base_fournisseur')then
		FOR value_ IN SELECT id FROM yvs_compta_content_journal WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'FOURNISSEUR'
		LOOP
			SELECT INTO verify_ id FROM yvs_synchro_listen_table WHERE name_table = 'yvs_compta_content_journal' AND id_source = value_::bigint AND action_name = 'UPDATE';
			IF(COALESCE(verify_, 0) > 0)THEN
				UPDATE yvs_synchro_listen_table SET to_listen = TRUE WHERE id = verify_;
			ELSE
				INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
					('yvs_compta_content_journal', value_::bigint, current_date, true, 'UPDATE');
			END IF;
		END LOOP;
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'FOURNISSEUR';
	elsif(table_ = 'yvs_com_client')then
		FOR value_ IN SELECT id FROM yvs_compta_content_journal WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'CLIENT'
		LOOP
			SELECT INTO verify_ id FROM yvs_synchro_listen_table WHERE name_table = 'yvs_compta_content_journal' AND id_source = value_::bigint AND action_name = 'UPDATE';
			IF(COALESCE(verify_, 0) > 0)THEN
				UPDATE yvs_synchro_listen_table SET to_listen = TRUE WHERE id = verify_;
			ELSE
				INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
					('yvs_compta_content_journal', value_::bigint, current_date, true, 'UPDATE');
			END IF;
		END LOOP;
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'CLIENT';
	elsif(table_ = 'yvs_grh_employes')then
		FOR value_ IN SELECT id FROM yvs_compta_content_journal WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'EMPLOYE'
		LOOP
			SELECT INTO verify_ id FROM yvs_synchro_listen_table WHERE name_table = 'yvs_compta_content_journal' AND id_source = value_::bigint AND action_name = 'UPDATE';
			IF(COALESCE(verify_, 0) > 0)THEN
				UPDATE yvs_synchro_listen_table SET to_listen = TRUE WHERE id = verify_;
			ELSE
				INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name) VALUES
					('yvs_compta_content_journal', value_::bigint, current_date, true, 'UPDATE');
			END IF;
		END LOOP;
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'EMPLOYE';
	end if;
	return result_;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fusion_data_for_table(character varying, bigint, character varying)
  OWNER TO postgres;
