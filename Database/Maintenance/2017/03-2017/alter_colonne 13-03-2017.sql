CREATE TABLE yvs_connection_historique
(
  id bigint NOT NULL,
  adresse_ip character varying,
  adresse_mac character varying,
  users integer,
  date_connexion timestamp without time zone,
  agence bigint,
  author bigint,
  id_session character varying,
  debut_navigation timestamp without time zone,
  CONSTRAINT yvs_connection_historique_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_connection_historique_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_connection_historique_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_connection_historique_user_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_connection_historique
  OWNER TO postgres;
  
  
CREATE TABLE yvs_connection_pages_historique
(
  id bigint NOT NULL,
  titre_page character varying,
  auteur_page bigint,
  date_ouverture timestamp without time zone,
  author bigint,
  CONSTRAINT yvs_connection_pages_historique_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_connection_pages_historique_auteur_page_fkey FOREIGN KEY (auteur_page)
      REFERENCES yvs_connection_historique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_connection_pages
  OWNER TO postgres;
