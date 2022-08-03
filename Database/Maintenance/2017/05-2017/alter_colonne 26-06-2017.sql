DROP TABLE yvs_com_grille_commission;
DROP TABLE yvs_com_commission;

CREATE TABLE yvs_com_type_grille
(
  id serial NOT NULL,
  reference character varying,
  cible character(1) default 'C',
  societe bigint,
  author bigint,
  CONSTRAINT yvs_com_type_grille_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_type_grille_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_type_grille_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_type_grille
  OWNER TO postgres;
  
  
CREATE TABLE yvs_com_tranche_taux
(
  id serial NOT NULL,
  tranche_maximal double precision DEFAULT 0,
  tranche_minimal double precision DEFAULT 0,
  taux double precision DEFAULT 0,
  nature character(1) default 'T',
  type_grille bigint,
  author bigint,
  CONSTRAINT yvs_com_tranche_taux_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_tranche_taux_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_tranche_taux_type_grille_fkey FOREIGN KEY (type_grille)
      REFERENCES yvs_com_type_grille (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_tranche_taux
  OWNER TO postgres;
  
  
CREATE TABLE yvs_com_periode_validite_commision
(
  id serial NOT NULL,
  date_debut date default current_date,
  date_fin date default current_date,
  periodicite character(1) default 'P',
  plan bigint,
  author bigint,
  CONSTRAINT yvs_com_periode_validite_commision_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_periode_validite_commision_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_periode_validite_commision_plan_fkey FOREIGN KEY (plan)
      REFERENCES yvs_com_plan_commission (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_periode_validite_commision
  OWNER TO postgres;
  
  
CREATE TABLE yvs_com_facteur_taux
(
  id bigserial NOT NULL,
  general boolean default true,
  nouveau_client boolean default true,
  taux double precision default 0,
  base character(1) default 'A',
  champ_application character(1) default 'A',
  type_grille bigint,
  plan bigint,
  article bigint,
  categorie bigint,
  actif boolean default true,
  author bigint,
  CONSTRAINT yvs_com_facteur_taux_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_facteur_taux_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_facteur_taux_plan_fkey FOREIGN KEY (plan)
      REFERENCES yvs_com_plan_commission (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_facteur_taux_type_grille_fkey FOREIGN KEY (type_grille)
      REFERENCES yvs_com_type_grille (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_facteur_taux_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_facteur_taux_categorie_fkey FOREIGN KEY (categorie)
      REFERENCES yvs_base_categorie_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_facteur_taux
  OWNER TO postgres;
  
ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN taux_remise double precision default 0;

ALTER TABLE yvs_com_doc_ventes ADD COLUMN livraison_auto boolean DEFAULT false;

ALTER TABLE yvs_base_point_vente ADD COLUMN livraison_on character(1) DEFAULT 'A';

ALTER TABLE yvs_compta_reglement_credit_client ADD COLUMN statut character(1) DEFAULT 'W';

ALTER TABLE yvs_compta_reglement_credit_client ADD COLUMN model bigint;
ALTER TABLE yvs_compta_reglement_credit_client
  ADD CONSTRAINT yvs_compta_reglement_credit_client_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
  
ALTER TABLE yvs_com_plan_commission ADD COLUMN intitule character varying;
ALTER TABLE yvs_com_plan_commission ADD COLUMN permanent boolean default true;