-- Table: yvs_com_ecart_entete_vente
-- DROP TABLE yvs_com_ecart_entete_vente;
CREATE TABLE yvs_com_ecart_entete_vente
(
  id bigserial NOT NULL,
  date_ecart date,
  montant double precision,
  entete_doc bigint,
  users bigint,
  caisse bigint,
  statut character(1) DEFAULT 'W'::bpchar,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_com_ecart_entete_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_ecart_entete_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_ecart_entete_vente_entete_doc_fkey FOREIGN KEY (entete_doc)
      REFERENCES yvs_com_entete_doc_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_com_ecart_entete_vente_users_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_com_ecart_entete_vente_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_ecart_entete_vente
  OWNER TO postgres;

  
-- Table: yvs_com_reglement_ecart_entete_vente
-- DROP TABLE yvs_com_reglement_ecart_entete_vente;
CREATE TABLE yvs_com_reglement_ecart_entete_vente
(
  id bigserial NOT NULL,
  date_reglement date,
  montant double precision,
  piece bigint,
  caisse bigint,
  statut character(1) DEFAULT 'W'::bpchar,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_com_reglement_ecart_entete_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_reglement_ecart_entete_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_reglement_ecart_entete_vente_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_com_ecart_entete_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_com_reglement_ecart_entete_vente_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_reglement_ecart_entete_vente
  OWNER TO postgres;
  
  
-- Table: yvs_com_compensation_ecart_entete_vente
-- DROP TABLE yvs_com_compensation_ecart_entete_vente;
CREATE TABLE yvs_com_compensation_ecart_entete_vente
(
  id bigserial NOT NULL,
  date_compensation date,
  montant double precision default 0,
  manquant bigint,
  excedent bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_com_compensation_ecart_entete_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_compensation_ecart_entete_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_compensation_ecart_entete_vente_manquant_fkey FOREIGN KEY (manquant)
      REFERENCES yvs_com_reglement_ecart_entete_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_com_compensation_ecart_entete_vente_excedent_fkey FOREIGN KEY (excedent)
      REFERENCES yvs_com_reglement_ecart_entete_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_compensation_ecart_entete_vente
  OWNER TO postgres;

  
-- Table: yvs_compta_notif_versement_vente
-- DROP TABLE yvs_compta_notif_versement_vente;
CREATE TABLE yvs_compta_notif_versement_vente
(
  id bigserial NOT NULL,
  piece bigint,
  entete_doc bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_notif_versement_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_notif_versement_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_notif_versement_vente_entete_doc_fkey FOREIGN KEY (entete_doc)
      REFERENCES yvs_com_entete_doc_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_notif_versement_vente_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_caisse_piece_virement (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_notif_versement_vente
  OWNER TO postgres;