ALTER TABLE yvs_com_doc_stocks ADD COLUMN date_transmis date DEFAULT ('now'::text)::date;
ALTER TABLE yvs_com_doc_stocks ADD COLUMN author_transmis bigint;
ALTER TABLE yvs_com_doc_stocks
  ADD CONSTRAINT yvs_com_doc_stocks_author_transmis_fkey FOREIGN KEY (author_transmis)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
