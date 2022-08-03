
CREATE TABLE yvs_prod_conditionnement_declaration
(
  id bigserial NOT NULL,
  declaration bigint,
  conditionnement bigint,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  CONSTRAINT yvs_prod_conditionnement_declaration_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_conditionnement_declaration_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_conditionnement_declaration_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_prod_fiche_conditionnement (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_prod_conditionnement_declaration_declaration_fkey FOREIGN KEY (declaration)
      REFERENCES yvs_prod_declaration_production (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_conditionnement_declaration
  OWNER TO postgres;