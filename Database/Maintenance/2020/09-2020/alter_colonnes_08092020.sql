ALTER TABLE yvs_com_commercial_vente
  ADD CONSTRAINT yvs_com_commercial_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
