
CREATE TABLE yvs_com_qualite
(
  id bigserial NOT NULL,
  code character varying,
  libelle character varying,
  supp boolean DEFAULT false,
  actif boolean DEFAULT true,
  societe integer,
  author bigint,
  CONSTRAINT yvs_com_qualite_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_qualite_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_qualite_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_qualite
  OWNER TO postgres;
  
ALTER TABLE yvs_base_mouvement_stock ADD COLUMN qualite bigint;
ALTER TABLE yvs_base_mouvement_stock
  ADD CONSTRAINT yvs_base_mouvement_stock_qualite_fkey FOREIGN KEY (qualite)
      REFERENCES yvs_com_qualite (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN qualite bigint;
ALTER TABLE yvs_com_contenu_doc_achat
  ADD CONSTRAINT yvs_com_contenu_doc_achat_qualite_fkey FOREIGN KEY (qualite)
      REFERENCES yvs_com_qualite (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN qualite bigint;
ALTER TABLE yvs_com_contenu_doc_stock
  ADD CONSTRAINT yvs_com_contenu_doc_stock_qualite_fkey FOREIGN KEY (qualite)
      REFERENCES yvs_com_qualite (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN qualite bigint;
ALTER TABLE yvs_com_contenu_doc_vente
  ADD CONSTRAINT yvs_com_contenu_doc_vente_qualite_fkey FOREIGN KEY (qualite)
      REFERENCES yvs_com_qualite (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;