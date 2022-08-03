CREATE TABLE yvs_compta_acompte_client
(
  id bigserial NOT NULL,
  montant double precision,
  date_acompte date,
  num_refrence character varying,
  commentaire character varying,
  author bigint,
  client bigint,
  caisse bigint,
  statut character(1) DEFAULT 'W'::bpchar,
  CONSTRAINT yvs_compta_acompte_client_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_acompte_client_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_acompte_client_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_acompte_client_client_fkey FOREIGN KEY (client)
      REFERENCES yvs_com_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_acompte_client
  OWNER TO postgres;
  
CREATE TABLE yvs_compta_credit_client
(
  id bigserial NOT NULL,
  num_reference character varying,
  motif character varying,
  date_credit date,
  montant double precision,
  client bigint,
  author bigint,
  CONSTRAINT yvs_compta_credit_client_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_credit_client_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_credit_client_client_fkey FOREIGN KEY (client)
      REFERENCES yvs_com_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_credit_client
  OWNER TO postgres;
  
CREATE TABLE yvs_compta_notif_reglement
(
  id bigserial NOT NULL,
  piece_vente bigint,
  acompte bigint,
  author bigint,
  CONSTRAINT yvs_compta_notif_reglement_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_notif_reglement_acompte_fkey FOREIGN KEY (acompte)
      REFERENCES yvs_compta_acompte_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_notif_reglement_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_notif_reglement_piece_vente_fkey FOREIGN KEY (piece_vente)
      REFERENCES yvs_compta_caisse_piece_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_notif_reglement
  OWNER TO postgres;
  
  
CREATE TABLE yvs_compta_reglement_credit_client
(
  id bigserial NOT NULL,
  credit bigint,
  caisse bigint,
  valeur double precision,
  author bigint,
  date_reg date,
  heure_reg time without time zone,
  statut character(1) DEFAULT 'W'::bpchar,
  CONSTRAINT yvs_compta_reglement_credit_client_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_reglement_credit_client_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_reglement_credit_client_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_reglement_credit_client_credit_fkey FOREIGN KEY (credit)
      REFERENCES yvs_compta_credit_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_reglement_credit_client
  OWNER TO postgres;
