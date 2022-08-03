CREATE TABLE yvs_stat_export_etat
(
  id serial NOT NULL,
  code character varying,
  libelle character varying,
  file_name character varying,
  format character varying,
  societe bigint,
  CONSTRAINT yvs_stat_export_etat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_stat_export_etat_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_stat_export_etat
  OWNER TO postgres;
  
  CREATE TABLE yvs_stat_export_colonne
(
  id serial NOT NULL,
  colonne character varying,
  libelle character varying,
  integrer boolean DEFAULT true,
  visible boolean default false,
  table_name character varying,
  contrainte boolean default false,
  colonne_liee character varying,
  table_name_liee character varying,
  etat integer,
  CONSTRAINT yvs_stat_export_colonne_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_stat_export_colonne_etat_fkey FOREIGN KEY (etat)
      REFERENCES yvs_stat_export_etat (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_stat_export_colonne
  OWNER TO postgres;