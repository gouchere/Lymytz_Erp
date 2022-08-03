DROP TABLE yvs_com_categorie_tarifaire;
CREATE TABLE yvs_com_categorie_tarifaire
(
  id bigserial NOT NULL,
  client bigint,
  categorie bigint,
  date_debut date,
  date_fin date,
  priorite integer,
  actif boolean DEFAULT true,
  permanent boolean DEFAULT true,
  author bigint,
  CONSTRAINT yvs_com_categorie_tarifaire_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_categorie_tarifaire_categorie_fkey FOREIGN KEY (categorie)
      REFERENCES yvs_base_categorie_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_categorie_tarifaire_client_fkey FOREIGN KEY (client)
      REFERENCES yvs_com_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_categorie_tarifaire_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_categorie_tarifaire
  OWNER TO postgres;