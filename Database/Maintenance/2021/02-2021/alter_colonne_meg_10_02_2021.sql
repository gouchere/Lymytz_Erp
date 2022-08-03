ALTER TABLE yvs_com_ecart_entete_vente ADD COLUMN date_debut date;

-- Table: yvs_compta_journaux_periode
-- DROP TABLE yvs_compta_journaux_periode;
CREATE TABLE yvs_compta_journaux_periode
(
  id bigserial NOT NULL,
  cloture boolean default true,
  journal bigint,
  periode bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_compta_journaux_periode_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_journaux_periode_journal_fkey FOREIGN KEY (journal)
      REFERENCES yvs_compta_journaux (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_compta_journaux_periode_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_compta_journaux_periode_periode_fkey FOREIGN KEY (periode)
      REFERENCES yvs_mut_periode_exercice (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_journaux_periode
  OWNER TO postgres;

-- Index: yvs_compta_journaux_periode_journal_idx
-- DROP INDEX yvs_compta_journaux_periode_journal_idx;

CREATE INDEX yvs_compta_journaux_periode_journal_idx
  ON yvs_compta_journaux_periode
  USING btree
  (journal);

-- Index: yvs_compta_journaux_periode_journal_idx
-- DROP INDEX yvs_compta_journaux_periode_journal_idx;

CREATE INDEX yvs_compta_journaux_periode_periode_idx
  ON yvs_compta_journaux_periode
  USING btree
  (periode);

