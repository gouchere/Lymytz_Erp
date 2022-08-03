-- Table: yvs_users_validite
-- DROP TABLE yvs_users_validite;
CREATE TABLE yvs_users_validite
(
  id bigserial NOT NULL,
  users bigint NOT NULL,
  date_expiration date default current_date,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_users_validite_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_users_validite_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_users_validite_users_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_users_validite
  OWNER TO postgres; 
  
  
-- Table: yvs_com_proformat_vente
-- DROP TABLE yvs_com_proformat_vente;
CREATE TABLE public.yvs_com_proformat_vente
(
   id bigserial, 
   numero character varying, 
   statut character varying default 'E', 
   client character varying, 
   telephone character varying, 
   adresse character varying, 
   description character varying, 
   date_doc date, 
   date_expiration date, 
   agence bigint, 
   date_save timestamp without time zone DEFAULT current_timestamp, 
   date_update timestamp without time zone DEFAULT current_timestamp, 
   author bigint, 
  CONSTRAINT yvs_com_proformat_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_proformat_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_proformat_vente_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
) 
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_proformat_vente
  OWNER TO postgres;
  
  
-- Table: yvs_com_proformat_vente_contenu
-- DROP TABLE yvs_com_proformat_vente_contenu;
CREATE TABLE public.yvs_com_proformat_vente_contenu
(
   id bigserial, 
   conditionnement bigint, 
   quantite double precision default 0, 
   prix double precision default 0, 
   proformat bigint, 
   date_save timestamp without time zone DEFAULT current_timestamp, 
   date_update timestamp without time zone DEFAULT current_timestamp, 
   author bigint, 
  CONSTRAINT yvs_com_proformat_vente_contenu_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_proformat_vente_contenu_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_proformat_vente_contenu_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_com_proformat_vente_contenu_proformat_fkey FOREIGN KEY (proformat)
      REFERENCES yvs_com_proformat_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
) 
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_proformat_vente_contenu
  OWNER TO postgres;

  
ALTER TABLE yvs_users_agence ADD COLUMN can_action BOOLEAN DEFAULT FALSE;

ALTER TABLE yvs_base_articles DISABLE TRIGGER base_warning_article;
ALTER TABLE yvs_base_articles ADD COLUMN controle_fournisseur boolean DEFAULT false;
ALTER TABLE yvs_base_articles DROP COLUMN photo_1;
ALTER TABLE yvs_base_articles ADD COLUMN photo_1 character varying;
ALTER TABLE yvs_base_articles ADD COLUMN photo_1_extension character varying;
ALTER TABLE yvs_base_articles ADD COLUMN photo_2_extension character varying;
ALTER TABLE yvs_base_articles ADD COLUMN photo_3_extension character varying;
ALTER TABLE yvs_base_articles ENABLE TRIGGER base_warning_article;