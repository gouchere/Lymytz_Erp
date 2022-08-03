
CREATE TABLE yvs_com_commercial_vente
(
  id bigserial NOT NULL,
  commercial bigint,
  facture bigint,
  taux double precision DEFAULT 0,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  responsable boolean DEFAULT false,
  CONSTRAINT yvs_com_commercial_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_commercial_vente_commercial_fkey FOREIGN KEY (commercial)
      REFERENCES yvs_com_comerciale (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_commercial_vente_facture_fkey FOREIGN KEY (facture)
      REFERENCES yvs_com_doc_ventes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_commercial_vente
  OWNER TO postgres;