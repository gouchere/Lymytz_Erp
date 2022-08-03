ALTER TABLE yvs_base_taxes ADD COLUMN "module" CHARACTER VARYING DEFAULT 'M';

DROP INDEX yvs_base_articles_ref_art;
  
DROP INDEX yvs_compta_content_journal_compte_tiers_idx;
DROP INDEX yvs_compta_content_journal_table_externe_idx;
DROP INDEX yvs_compta_content_journal_num_piece_idx;
DROP INDEX yvs_compta_content_journal_num_ref_idx;
DROP INDEX yvs_compta_content_journal_table_tiers_idx;


CREATE INDEX yvs_compta_content_journal_num_piece_num_ref_ref_externe_ta_idx
  ON yvs_compta_content_journal
  USING btree
  (num_piece COLLATE pg_catalog."default", num_ref COLLATE pg_catalog."default", ref_externe, table_externe COLLATE pg_catalog."default", table_tiers COLLATE pg_catalog."default", compte_tiers);


CREATE INDEX yvs_base_articles_ref_art_designation_categorie_type_servic_idx
  ON yvs_base_articles
  USING btree
  (ref_art COLLATE pg_catalog."default", designation COLLATE pg_catalog."default", categorie COLLATE pg_catalog."default", type_service COLLATE pg_catalog."default", actif);
  