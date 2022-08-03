
ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN rabais double precision;
ALTER TABLE yvs_com_contenu_doc_vente ALTER COLUMN rabais SET DEFAULT 0;


CREATE TABLE yvs_base_plan_tarifaire_tranche
(
  id bigserial NOT NULL,
  base character varying DEFAULT 'QTE'::character varying,
  valeur_min double precision DEFAULT 0,
  valeur_max double precision DEFAULT 0,
  remise double precision DEFAULT 0,
  nature_remise character varying DEFAULT 'TAUX'::character varying,
  plan bigint,
  author bigint,
  CONSTRAINT yvs_base_plan_tarifaire_tranche_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_plan_tarifaire_tranche_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_plan_tarifaire_tranche_plan_fkey FOREIGN KEY (plan)
      REFERENCES yvs_base_plan_tarifaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_plan_tarifaire_tranche
  OWNER TO postgres;
  
  -- DROP TABLE yvs_com_commission;

CREATE TABLE yvs_com_grille_commission
(
  id bigserial NOT NULL ,
  montant_maximal double precision DEFAULT 0,
  montant_minimal double precision DEFAULT 0,
  montant_commission double precision DEFAULT 0,
  nature_montant character varying,
  commission bigint,
  base character varying DEFAULT 'QTE'::character varying,
  author bigint,
  CONSTRAINT yvs_com_grille_commission_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_grille_commission_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_grille_commission_commission_fkey FOREIGN KEY (commission)
      REFERENCES yvs_com_commission (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_commission
  OWNER TO postgres;

  
ALTER TABLE yvs_com_commission DROP COLUMN montant_maximal;
ALTER TABLE yvs_com_commission DROP COLUMN montant_minimal;
ALTER TABLE yvs_com_commission DROP COLUMN montant_commission;
ALTER TABLE yvs_com_commission DROP COLUMN nature_montant;

ALTER TABLE yvs_com_grille_ristourne ADD COLUMN base character varying;
ALTER TABLE yvs_com_grille_ristourne ALTER COLUMN base SET DEFAULT 'QTE'::character varying;

ALTER TABLE yvs_com_ristourne DROP COLUMN reference;
ALTER TABLE yvs_com_ristourne DROP COLUMN base_ristourne;
ALTER TABLE yvs_com_ristourne DROP COLUMN societe;
ALTER TABLE yvs_com_ristourne ADD COLUMN article bigint;
ALTER TABLE yvs_com_ristourne
  ADD CONSTRAINT yvs_com_ristourne_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_com_ristourne ADD COLUMN plan bigint;
ALTER TABLE yvs_com_ristourne
  ADD CONSTRAINT yvs_com_ristourne_plan_fkey FOREIGN KEY (plan)
      REFERENCES yvs_com_plan_ristourne (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_plan_ristourne DROP COLUMN date_debut;
ALTER TABLE yvs_com_plan_ristourne DROP COLUMN date_fin;
ALTER TABLE yvs_com_plan_ristourne DROP COLUMN ristourne;
ALTER TABLE yvs_com_plan_ristourne DROP COLUMN article;
ALTER TABLE yvs_com_plan_ristourne DROP COLUMN categorie;
ALTER TABLE yvs_com_plan_ristourne DROP COLUMN permanent;
ALTER TABLE yvs_com_plan_ristourne ADD COLUMN reference character varying;
ALTER TABLE yvs_com_plan_ristourne ADD COLUMN societe bigint;
ALTER TABLE yvs_com_plan_ristourne
  ADD CONSTRAINT yvs_com_plan_ristourne_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_com_plan_commission DROP COLUMN nature_commission;

ALTER TABLE yvs_com_grille_remise ADD COLUMN base character varying;
ALTER TABLE yvs_com_grille_remise ALTER COLUMN base SET DEFAULT 'QTE'::character varying;
ALTER TABLE yvs_com_remise DROP COLUMN base_remise;


ALTER TABLE yvs_com_client ADD COLUMN plan_ristourne bigint;
ALTER TABLE yvs_com_client
  ADD CONSTRAINT yvs_com_client_plan_ristourne_fkey FOREIGN KEY (plan_ristourne)
      REFERENCES yvs_com_plan_ristourne (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
