INSERT INTO yvs_workflow_model_doc(titre_doc, name_table) VALUES ('BON_OPERATION_DIVERS', 'yvs_compta_caisse_doc_divers');

UPDATE yvs_prod_etat_ressource SET ressource_production = null;
ALTER TABLE yvs_prod_etat_ressource DROP CONSTRAINT yvs_prod_etat_ressource_ressource_production_fkey;
ALTER TABLE yvs_prod_etat_ressource
  ADD CONSTRAINT yvs_prod_etat_ressource_ressource_production_fkey FOREIGN KEY (ressource_production)
      REFERENCES yvs_prod_poste_charge (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_prod_poste_charge ADD COLUMN type_valeur character(1);
ALTER TABLE yvs_prod_poste_charge ALTER COLUMN type_valeur SET DEFAULT 'D'::bpchar;
	  
DROP TABLE yvs_prod_ressource_production_tiers;
CREATE TABLE yvs_prod_poste_charge_tiers
(
  id serial NOT NULL,
  tiers bigint,
  ressource bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_prod_poste_charge_tiers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_poste_charge_tiers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_poste_charge_tiers_ressource_fkey FOREIGN KEY (ressource)
      REFERENCES yvs_prod_poste_charge (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_prod_poste_charge_tiers_tiers_fkey FOREIGN KEY (tiers)
      REFERENCES yvs_base_tiers (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_poste_charge_tiers
  OWNER TO postgres;
  
DROP TABLE yvs_prod_ressource_production_employe;
CREATE TABLE yvs_prod_poste_charge_employe
(
  id serial NOT NULL,
  employe bigint,
  ressource bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_prod_poste_charge_employe_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_poste_charge_employe_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_poste_charge_employe_employe_fkey FOREIGN KEY (employe)
      REFERENCES yvs_grh_employes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_poste_charge_employe_ressource_fkey FOREIGN KEY (ressource)
      REFERENCES yvs_prod_poste_charge (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_poste_charge_employe
  OWNER TO postgres;  
  
DROP TABLE yvs_prod_ressource_production;
CREATE TABLE yvs_prod_poste_charge_materiel
(
  id serial NOT NULL,
  capacite double precision DEFAULT 0,
  ressource bigint,
  materiel bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_prod_poste_charge_materiel_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_poste_charge_materiel_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_poste_charge_materiel_ressource_fkey FOREIGN KEY (ressource)
      REFERENCES yvs_prod_poste_charge (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_prod_poste_charge_materiel_materiel_fkey FOREIGN KEY (materiel)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_poste_charge_materiel
  OWNER TO postgres;
  
  
ALTER TABLE yvs_compta_caisse_doc_divers ADD COLUMN bon_provisoire boolean;
ALTER TABLE yvs_compta_caisse_doc_divers ALTER COLUMN bon_provisoire SET DEFAULT false;

ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN quantite_resultante double precision;
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN conditionnement_resultante bigint;
ALTER TABLE yvs_com_contenu_doc_stock
  ADD CONSTRAINT yvs_com_contenu_doc_stock_cconditionnement_resultante_fkey FOREIGN KEY (conditionnement_resultante)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN ordonnateur bigint;
ALTER TABLE yvs_compta_bon_provisoire
  ADD CONSTRAINT yvs_compta_bon_provisoire_ordonnateur_fkey FOREIGN KEY (ordonnateur)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;