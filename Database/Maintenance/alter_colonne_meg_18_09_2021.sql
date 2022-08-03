-- Table: yvs_com_proformat_vente_cout_sup
-- DROP TABLE yvs_com_proformat_vente_cout_sup;
CREATE TABLE yvs_com_proformat_vente_cout_sup
(
  id bigserial NOT NULL,
  montant double precision,
  proformat bigint,
  supp boolean DEFAULT false,
  actif boolean DEFAULT true,
  author bigint,
  type_cout bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  service boolean DEFAULT false,
  execute_trigger character varying,
  CONSTRAINT yvs_cout_sup_proformat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_proformat_vente_cout_sup_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_proformat_vente_cout_sup_proformat_fkey FOREIGN KEY (proformat)
      REFERENCES yvs_com_proformat_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_com_proformat_vente_cout_sup_type_cout_fkey FOREIGN KEY (type_cout)
      REFERENCES yvs_grh_type_cout (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_proformat_vente_cout_sup
  OWNER TO postgres;

CREATE INDEX yvs_com_proformat_vente_cout_sup_type_cout_idx
  ON yvs_com_proformat_vente_cout_sup
  USING btree
  (type_cout);

CREATE INDEX yvs_com_proformat_vente_cout_sup_proformat_idx
  ON yvs_com_proformat_vente_cout_sup
  USING btree
  (proformat);  
  
ALTER TABLE yvs_grh_contrat_emps ADD COLUMN statut character varying;
ALTER TABLE yvs_grh_contrat_emps ALTER COLUMN statut SET DEFAULT 'E'::character varying;