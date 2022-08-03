-- Table: yvs_compta_centre_cout_virement
-- DROP TABLE yvs_compta_centre_cout_virement;
CREATE TABLE yvs_compta_centre_cout_virement
(
  id bigserial NOT NULL,
  cout bigint,
  centre bigint,
  coefficient double precision,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_centre_cout_virement_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_centre_cout_virement_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_centre_cout_virement_centre_fkey FOREIGN KEY (centre)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_centre_cout_virement_cout_fkey FOREIGN KEY (cout)
      REFERENCES yvs_compta_cout_sup_piece_virement (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_centre_cout_virement
  OWNER TO postgres;