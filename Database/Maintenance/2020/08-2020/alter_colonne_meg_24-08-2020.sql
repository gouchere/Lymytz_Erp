INSERT INTO yvs_base_element_reference(designation, module, default_prefix) VALUES ('Commission', 'COM', 'COM');
INSERT INTO yvs_base_element_reference(designation, module, default_prefix) VALUES ('Piece Caisse Commission', 'COFI', 'PO');

-- Table: yvs_com_commission_commerciaux
-- DROP TABLE yvs_com_commission_commerciaux;
CREATE TABLE yvs_com_commission_commerciaux
(
  id bigserial NOT NULL,
  numero character varying,
  commerciaux bigint,
  periode bigint,
  montant double precision DEFAULT 0,
  statut character varying,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  execute_trigger character varying,
  CONSTRAINT yvs_com_commission_commerciaux_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_commission_commerciaux_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_commission_commerciaux_commerciaux_fkey FOREIGN KEY (commerciaux)
      REFERENCES yvs_com_comerciale (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_commission_commerciaux_periode_fkey FOREIGN KEY (periode)
      REFERENCES yvs_com_periode_objectif (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_commission_commerciaux
  OWNER TO postgres;
  

-- Table: yvs_compta_caisse_piece_commission
-- DROP TABLE yvs_compta_caisse_piece_commission;
CREATE TABLE yvs_compta_caisse_piece_commission
(
  id bigserial NOT NULL,
  numero_piece character varying,
  montant double precision,
  statut_piece character(1),
  commission bigint,
  caisse bigint,
  author bigint,
  date_piece date,
  date_paiement timestamp without time zone,
  caissier bigint,
  note character varying,
  date_paiment_prevu date,
  model bigint,
  comptabilise boolean DEFAULT false,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  reference_externe character varying,
  execute_trigger character varying,
  CONSTRAINT yvs_compta_caisse_piece_commission_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_caisse_piece_commission_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_commission_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_commission_caissier_fkey FOREIGN KEY (caissier)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_commission_commission_fkey FOREIGN KEY (commission)
      REFERENCES yvs_com_commission_commerciaux (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_caisse_piece_commission_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_caisse_piece_commission
  OWNER TO postgres;

