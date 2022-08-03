ALTER TABLE yvs_base_point_vente ADD COLUMN activer_notification boolean;
ALTER TABLE yvs_base_point_vente ALTER COLUMN activer_notification SET DEFAULT false;

ALTER TABLE yvs_workflow_alertes ADD COLUMN description character varying;
-- Table: yvs_societes_avis
-- DROP TABLE yvs_societes_avis;
CREATE TABLE yvs_societes_avis
(
  id bigserial NOT NULL,
  note integer,
  commentaire character varying,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  auteur character varying,
  telephone character varying,
  email character varying,
  societe bigint,
  execute_trigger character varying,
  CONSTRAINT yvs_societes_avis_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_societes_avis_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_societes_avis
  OWNER TO postgres;

-- Table: yvs_base_publicites
-- DROP TABLE yvs_base_publicites;
CREATE TABLE yvs_base_publicites
(
  id bigserial NOT NULL,
  priorite integer default 1,
  description character varying,
  image character varying,
  article bigint,
  url character varying,
  permanent boolean default true,
  date_debut date DEFAULT current_date,
  date_fin date DEFAULT current_date,
  societe bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  execute_trigger character varying,
  CONSTRAINT yvs_base_publicites_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_publicites_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_publicites_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_publicites_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_publicites
  OWNER TO postgres;
