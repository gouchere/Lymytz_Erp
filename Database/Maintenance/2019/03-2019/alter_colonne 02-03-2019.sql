DELETE FROM yvs_page_module WHERE reference = 'gescom_recond';
INSERT INTO yvs_base_element_reference(designation, module, author)VALUES ('Recette OD', 'COFI', 16);
INSERT INTO yvs_base_element_reference(designation, module, author)VALUES ('Depense OD', 'COFI', 16);

SELECT insert_droit('compta_reg_fv_change_caissier', 'Changer le caissier lors de la création d''un reglement', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_reg_vente'), 16, 'A,B,C,E,F', 'R');

-- Table: yvs_base_article_analytique
-- DROP TABLE yvs_base_article_analytique;
CREATE TABLE yvs_base_article_analytique
(
  id bigserial NOT NULL,
  coefficient double precision DEFAULT 0,
  article bigint,
  centre bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  author bigint,
  CONSTRAINT yvs_base_article_analytique_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_article_analytique_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_base_article_analytique_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_article_analytique_centre_fkey FOREIGN KEY (centre)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_article_analytique
  OWNER TO postgres;
  
  
-- Table: yvs_compta_centre_contenu_achat
-- DROP TABLE yvs_compta_centre_contenu_achat;
CREATE TABLE yvs_compta_centre_contenu_achat
(
  id bigserial NOT NULL,
  coefficient double precision DEFAULT 0,
  contenu bigint,
  centre bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  author bigint,
  CONSTRAINT yvs_compta_centre_contenu_achat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_centre_contenu_achat_contenu_fkey FOREIGN KEY (contenu)
      REFERENCES yvs_com_contenu_doc_achat (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_centre_contenu_achat_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_centre_contenu_achat_centre_fkey FOREIGN KEY (centre)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_centre_contenu_achat
  OWNER TO postgres;