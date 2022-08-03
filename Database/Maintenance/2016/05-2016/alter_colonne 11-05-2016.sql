ALTER TABLE yvs_agences ADD COLUMN adresse_ip character varying;

ALTER TABLE yvs_base_point_vente_depot ADD COLUMN principal boolean;
ALTER TABLE yvs_base_point_vente_depot ALTER COLUMN principal SET DEFAULT false;

ALTER TABLE yvs_societes ADD COLUMN adresse_ip character varying;

ALTER TABLE yvs_com_parametre ADD COLUMN seuil_fsseur double precision;
ALTER TABLE yvs_com_parametre ALTER COLUMN seuil_fsseur SET DEFAULT 0;
ALTER TABLE yvs_com_parametre ADD COLUMN seuil_client double precision;
ALTER TABLE yvs_com_parametre ALTER COLUMN seuil_client SET DEFAULT 0;

CREATE TABLE yvs_com_parametre_achat
(
  id serial NOT NULL,
  livre_auto boolean DEFAULT false,
  etat_mouv_stock character varying DEFAULT 'VALIDE'::character varying,
  jour_anterieur integer DEFAULT 0,
  author bigint,
  agence bigint,
  CONSTRAINT yvs_com_parametre_achat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_parametre_achat_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_parametre_achat_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_parametre_achat
  OWNER TO postgres;
  
  -- Table: parametre_stock

-- DROP TABLE parametre_stock;

CREATE TABLE yvs_com_parametre_stock
(
  id serial NOT NULL,
  valid_auto boolean DEFAULT false,
  etat_mouv_stock character varying DEFAULT 'VALIDE'::character varying,
  jour_anterieur integer DEFAULT 0,
  author bigint,
  agence bigint,
  CONSTRAINT yvs_com_parametre_stock_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_parametre_stock_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_parametre_stock_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_parametre_stock
  OWNER TO postgres;
  
  
  -- Table: parametre_vente

-- DROP TABLE parametre_vente;

CREATE TABLE yvs_com_parametre_vente
(
  id serial NOT NULL,
  livre_auto boolean DEFAULT false,
  etat_mouv_stock character varying DEFAULT 'VALIDE'::character varying,
  jour_anterieur integer DEFAULT 0,
  author bigint,
  agence bigint,
  CONSTRAINT yvs_com_parametre_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_parametre_vente_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_parametre_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_parametre_vente
  OWNER TO postgres;

  
  -- Table: yvs_com_parametre

-- DROP TABLE yvs_com_parametre;

CREATE TABLE yvs_compta_parametre
(
  id serial NOT NULL,
  taille_compte integer default 8,
  societe bigint,
  author bigint,
  CONSTRAINT yvs_compta_parametre_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_parametre_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_parametre_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_parametre
  OWNER TO postgres;


