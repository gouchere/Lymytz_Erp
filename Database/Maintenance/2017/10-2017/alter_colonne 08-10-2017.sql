ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN quantite_bonus double precision;
ALTER TABLE yvs_com_contenu_doc_achat ALTER COLUMN quantite_bonus SET DEFAULT 0;
ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN article_bonus bigint;
ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN conditionnement_bonus bigint;
ALTER TABLE yvs_com_contenu_doc_achat
  ADD CONSTRAINT yvs_com_contenu_doc_achat_article_bonus_fkey FOREIGN KEY (article_bonus)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_com_contenu_doc_achat
  ADD CONSTRAINT yvs_com_contenu_doc_achat_conditionnement_bonus_fkey FOREIGN KEY (conditionnement_bonus)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;