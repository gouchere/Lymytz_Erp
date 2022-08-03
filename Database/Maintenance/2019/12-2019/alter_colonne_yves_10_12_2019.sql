-- Table: yvs_base_etats

-- DROP TABLE yvs_base_etats;

CREATE TABLE yvs_base_etats
(
  id serial NOT NULL,
  titre character varying,
  description character varying,
  CONSTRAINT yvs_base_etats_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_etats
  OWNER TO postgres;



-- Table: yvs_base_etat_societes

-- DROP TABLE yvs_base_etat_societes;

CREATE TABLE yvs_base_etat_societes
(
  id serial NOT NULL,
  societe bigint,
  etat integer,
  visible boolean,
  CONSTRAINT yvs_base_etat_societes_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_etat_societes_etat_fkey FOREIGN KEY (etat)
      REFERENCES yvs_base_etats (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_etat_societes_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_etat_societes
  OWNER TO postgres;



ALTER TABLE yvs_compta_content_journal_piece_virement ADD COLUMN sens_compta character(1);
ALTER TABLE yvs_compta_content_journal_piece_virement ALTER COLUMN sens_compta SET DEFAULT 'B'::bpchar;
