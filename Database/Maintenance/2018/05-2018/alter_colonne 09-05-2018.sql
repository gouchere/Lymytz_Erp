
CREATE TABLE yvs_com_commercial_point
(
  id bigserial NOT NULL,
  commercial bigint,
  point bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  CONSTRAINT yvs_com_commercial_point_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_commercial_point_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_commercial_point_commercial_fkey FOREIGN KEY (commercial)
      REFERENCES yvs_com_comerciale (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_commercial_point_point_fkey FOREIGN KEY (point)
      REFERENCES yvs_base_point_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_commercial_point
  OWNER TO postgres;

ALTER TABLE yvs_base_point_vente ADD COLUMN commission_for character(1);
ALTER TABLE yvs_base_point_vente ALTER COLUMN commission_for SET DEFAULT 'C'::bpchar;
COMMENT ON COLUMN yvs_base_point_vente.commission_for IS '''C'' pour commercial
''P'' pout point de vente';