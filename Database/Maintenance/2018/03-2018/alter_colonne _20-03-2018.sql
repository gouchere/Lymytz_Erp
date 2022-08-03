
 ALTER TABLE yvs_compta_caisse_piece_divers ADD COLUMN beneficiaire CHARACTER VARYING; 
 
CREATE TABLE yvs_com_objectifs_agence
(
  id bigserial NOT NULL,
  agence bigint,
  objectif bigint,
  periode bigint,
  valeur double precision,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  CONSTRAINT yvs_com_objectifs_agence_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_objectifs_agence_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_objectifs_agence_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_objectifs_agence_objectif_fkey FOREIGN KEY (objectif)
      REFERENCES yvs_com_modele_objectif (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_objectifs_agence_periode_fkey FOREIGN KEY (periode)
      REFERENCES yvs_com_periode_objectif (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_objectifs_agence
  OWNER TO postgres;
  
  
CREATE TABLE yvs_com_objectifs_point_vente
(
  id bigserial NOT NULL,
  point_vente bigint,
  objectif bigint,
  periode bigint,
  valeur double precision,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  CONSTRAINT yvs_com_objectifs_point_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_objectifs_point_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_objectifs_point_vente_point_vente_fkey FOREIGN KEY (point_vente)
      REFERENCES yvs_base_point_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_objectifs_point_vente_objectif_fkey FOREIGN KEY (objectif)
      REFERENCES yvs_com_modele_objectif (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_objectifs_point_vente_periode_fkey FOREIGN KEY (periode)
      REFERENCES yvs_com_periode_objectif (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_objectifs_point_vente
  OWNER TO postgres;
  
  
CREATE TABLE yvs_com_commission_vente
(
  id bigserial NOT NULL,
  facture bigint,
  periode bigint,
  montant double precision,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_com_commission_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_commission_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_commission_vente_facture_fkey FOREIGN KEY (facture)
      REFERENCES yvs_com_doc_ventes (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_com_commission_vente_periode_fkey FOREIGN KEY (periode)
      REFERENCES yvs_com_periode_objectif (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_commission_vente
  OWNER TO postgres;