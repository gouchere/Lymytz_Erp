DROP TABLE yvs_com_cout_additionel;
CREATE TABLE yvs_com_cout_sup_doc_achat
(
  id bigserial NOT NULL,
  montant double precision,
  doc_achat bigint,
  supp boolean DEFAULT false,
  actif boolean DEFAULT true,
  author bigint,
  type_cout bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_com_cout_sup_doc_achat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_cout_sup_doc_achat_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_cout_sup_doc_achat_doc_achat_fkey FOREIGN KEY (doc_achat)
      REFERENCES yvs_com_doc_achats (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_com_cout_sup_doc_achat_type_cout_fkey FOREIGN KEY (type_cout)
      REFERENCES yvs_grh_type_cout (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_cout_sup_doc_achat
  OWNER TO postgres;
  
  
CREATE TABLE yvs_base_code_acces
(
  id bigserial NOT NULL,
  code character varying,
  description character varying,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  societe bigint,
  CONSTRAINT yvs_base_code_acces_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_code_acces_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_code_acces_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_code_acces
  OWNER TO postgres;
  
CREATE TABLE yvs_base_users_acces
(
  id bigserial NOT NULL,
  code bigint,
  users bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  CONSTRAINT yvs_base_users_acces_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_users_acces_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_users_acces_code_fkey FOREIGN KEY (code)
      REFERENCES yvs_base_code_acces (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_users_acces_users_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_users_acces
  OWNER TO postgres;
  
CREATE TABLE yvs_compta_caisse_piece_compensation
(
  id bigserial NOT NULL,
  vente bigint,
  achat bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  CONSTRAINT yvs_compta_caisse_piece_compensation_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_caisse_piece_compensation_achat_fkey FOREIGN KEY (achat)
      REFERENCES yvs_compta_caisse_piece_achat (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_compensation_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_compensation_vente_fkey FOREIGN KEY (vente)
      REFERENCES yvs_compta_caisse_piece_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_caisse_piece_compensation
  OWNER TO postgres;
  
CREATE TABLE yvs_com_objectifs_comercial
(
  id bigserial NOT NULL,
  commercial bigint,
  objectif bigint,
  periode bigint,
  valeur double precision,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  CONSTRAINT yvs_com_objectifs_comercial_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_objectifs_comercial_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_objectifs_comercial_commercial_fkey FOREIGN KEY (commercial)
      REFERENCES yvs_com_comerciale (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_objectifs_comercial_objectif_fkey FOREIGN KEY (objectif)
      REFERENCES yvs_com_modele_objectif (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_objectifs_comercial_periode_fkey FOREIGN KEY (periode)
      REFERENCES yvs_com_periode_objectif (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_objectifs_comercial
  OWNER TO postgres;
  
  
ALTER TABLE yvs_compta_mouvement_caisse ADD COLUMN model bigint;
ALTER TABLE yvs_compta_mouvement_caisse
  ADD CONSTRAINT yvs_compta_mouvement_caisse_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;    
	  
ALTER TABLE yvs_com_ristourne ADD COLUMN conditionnement bigint;
ALTER TABLE yvs_com_ristourne
  ADD CONSTRAINT yvs_com_ristourne_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_grille_ristourne ADD COLUMN article bigint;
ALTER TABLE yvs_com_grille_ristourne
  ADD CONSTRAINT yvs_com_grille_ristourne_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;  
	  
ALTER TABLE yvs_com_grille_ristourne ADD COLUMN conditionnement bigint;
ALTER TABLE yvs_com_grille_ristourne
  ADD CONSTRAINT yvs_com_grille_ristourne_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN article_bonus bigint;
ALTER TABLE yvs_com_contenu_doc_vente
  ADD CONSTRAINT yvs_com_contenu_doc_vente_article_bonus_fkey FOREIGN KEY (article_bonus)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  	  
ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN conditionnement_bonus bigint;
ALTER TABLE yvs_com_contenu_doc_vente
  ADD CONSTRAINT yvs_com_contenu_doc_vente_conditionnement_bonus_fkey FOREIGN KEY (conditionnement_bonus)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_remise ADD COLUMN code_acces bigint;
ALTER TABLE yvs_com_remise
  ADD CONSTRAINT yvs_com_remise_code_acces_fkey FOREIGN KEY (code_acces)
      REFERENCES yvs_base_code_acces (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_compta_phase_reglement ADD COLUMN for_emission boolean;
ALTER TABLE yvs_compta_phase_reglement ALTER COLUMN for_emission SET DEFAULT false;

ALTER TABLE yvs_compta_caisse_piece_compensation ADD COLUMN sens character(1);
ALTER TABLE yvs_compta_caisse_piece_compensation ALTER COLUMN sens SET DEFAULT 'C'::bpchar;

ALTER TABLE yvs_com_modele_objectif ALTER COLUMN indicateur TYPE character varying;

ALTER TABLE yvs_base_conditionnement_fournisseur ADD COLUMN principal boolean;
ALTER TABLE yvs_base_conditionnement_fournisseur ALTER COLUMN principal SET DEFAULT false;

ALTER TABLE yvs_com_reservation_stock ADD COLUMN description character varying;
	  
