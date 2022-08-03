
ALTER TABLE yvs_parametre_grh ADD COLUMN time_marge_autorise time without time zone;
ALTER TABLE yvs_parametre_grh ALTER COLUMN time_marge_autorise SET DEFAULT '00:15:00'::time without time zone;
ALTER TABLE yvs_parametre_grh ADD COLUMN time_marge_retard time without time zone;
ALTER TABLE yvs_parametre_grh ALTER COLUMN time_marge_retard SET DEFAULT '02:00:00'::time without time zone;

ALTER TABLE yvs_com_doc_achats ADD COLUMN tranche bigint;
ALTER TABLE yvs_com_doc_achats
  ADD CONSTRAINT yvs_com_doc_achats_tranche_fkey FOREIGN KEY (tranche)
      REFERENCES yvs_grh_tranche_horaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
	  

ALTER TABLE yvs_com_contenu_doc_achat ADD COLUMN parent bigint;
ALTER TABLE yvs_com_contenu_doc_achat
  ADD CONSTRAINT yvs_com_contenu_doc_achat_parent_fkey FOREIGN KEY (parent)
      REFERENCES yvs_com_contenu_doc_achat (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_com_contenu_doc_vente ADD COLUMN parent bigint;
ALTER TABLE yvs_com_contenu_doc_vente
  ADD CONSTRAINT yvs_com_contenu_doc_vente_parent_fkey FOREIGN KEY (parent)
      REFERENCES yvs_com_contenu_doc_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN parent bigint;
ALTER TABLE yvs_com_contenu_doc_stock
  ADD CONSTRAINT yvs_com_contenu_doc_stock_parent_fkey FOREIGN KEY (parent)
      REFERENCES yvs_com_contenu_doc_stock (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_com_contenu_doc_stock ADD COLUMN qte_attendu double precision;
ALTER TABLE yvs_com_contenu_doc_stock ALTER COLUMN qte_attendu SET DEFAULT 0;
	  

ALTER TABLE yvs_com_doc_ventes ADD COLUMN date_solder date;
ALTER TABLE yvs_com_doc_ventes ALTER COLUMN date_solder SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN consigner boolean;
ALTER TABLE yvs_com_doc_ventes ALTER COLUMN consigner SET DEFAULT false;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN date_consigner date;
ALTER TABLE yvs_com_doc_ventes ALTER COLUMN date_consigner SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN livrer boolean;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN date_livraison date;
ALTER TABLE yvs_com_doc_ventes ALTER COLUMN date_livraison SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN cloturer boolean;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN date_cloturer date;
ALTER TABLE yvs_com_doc_ventes ALTER COLUMN date_cloturer SET DEFAULT ('now'::text)::date;

ALTER TABLE yvs_com_doc_ventes ADD COLUMN valider_by bigint;
ALTER TABLE yvs_com_doc_ventes
  ADD CONSTRAINT yvs_com_doc_ventes_valider_by_fkey FOREIGN KEY (valider_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN date_valider date;
ALTER TABLE yvs_com_doc_ventes ALTER COLUMN date_valider SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN annuler_by bigint;
ALTER TABLE yvs_com_doc_ventes
  ADD CONSTRAINT yvs_com_doc_ventes_annuler_by_fkey FOREIGN KEY (annuler_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN date_annuler date;
ALTER TABLE yvs_com_doc_ventes ALTER COLUMN date_annuler SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN depot_livrer bigint;
ALTER TABLE yvs_com_doc_ventes
  ADD CONSTRAINT yvs_com_doc_ventes_depot_livrer_fkey FOREIGN KEY (depot_livrer)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;


ALTER TABLE yvs_com_doc_achats ADD COLUMN date_solder date;
ALTER TABLE yvs_com_doc_achats ADD COLUMN livrer boolean;
ALTER TABLE yvs_com_doc_achats ADD COLUMN date_livraison date;
ALTER TABLE yvs_com_doc_achats ALTER COLUMN date_livraison SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_com_doc_achats ADD COLUMN cloturer boolean;
ALTER TABLE yvs_com_doc_achats ADD COLUMN date_cloturer date;
ALTER TABLE yvs_com_doc_achats ALTER COLUMN date_cloturer SET DEFAULT ('now'::text)::date;

ALTER TABLE yvs_com_doc_achats ADD COLUMN valider_by bigint;
ALTER TABLE yvs_com_doc_achats
  ADD CONSTRAINT yvs_com_doc_achats_valider_by_fkey FOREIGN KEY (valider_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_com_doc_achats ADD COLUMN date_valider date;
ALTER TABLE yvs_com_doc_achats ALTER COLUMN date_valider SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_com_doc_achats ADD COLUMN annuler_by bigint;
ALTER TABLE yvs_com_doc_achats
  ADD CONSTRAINT yvs_com_doc_achats_annuler_by_fkey FOREIGN KEY (annuler_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_com_doc_achats ADD COLUMN date_annuler date;
ALTER TABLE yvs_com_doc_achats ALTER COLUMN date_annuler SET DEFAULT ('now'::text)::date;

ALTER TABLE yvs_com_doc_stocks ADD COLUMN cloturer boolean;
ALTER TABLE yvs_com_doc_stocks ADD COLUMN date_cloturer date;
ALTER TABLE yvs_com_doc_stocks ALTER COLUMN date_cloturer SET DEFAULT ('now'::text)::date;

ALTER TABLE yvs_com_doc_stocks ADD COLUMN valider_by bigint;
ALTER TABLE yvs_com_doc_stocks
  ADD CONSTRAINT yvs_com_doc_stocks_valider_by_fkey FOREIGN KEY (valider_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_com_doc_stocks ADD COLUMN date_valider date;
ALTER TABLE yvs_com_doc_stocks ALTER COLUMN date_valider SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_com_doc_stocks ADD COLUMN annuler_by bigint;
ALTER TABLE yvs_com_doc_stocks
  ADD CONSTRAINT yvs_com_doc_stocks_annuler_by_fkey FOREIGN KEY (annuler_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_com_doc_stocks ADD COLUMN date_annuler date;
ALTER TABLE yvs_com_doc_stocks ALTER COLUMN date_annuler SET DEFAULT ('now'::text)::date;

-- DROP TABLE journaux;

CREATE TABLE yvs_compta_journaux
(
  id bigserial NOT NULL,
  codejournal character varying(255),
  actif boolean,
  centralisationparligne boolean,
  comptetresorerie character varying(255),
  libele character varying(255),
  type_journal character varying(255),
  agence bigint,
  author bigint,
  CONSTRAINT yvs_compta_journaux_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_journaux_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_compta_journaux_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_journaux
  OWNER TO postgres;
  
  
ALTER TABLE yvs_com_parametre_achat ADD COLUMN journal bigint;
ALTER TABLE yvs_com_parametre_achat
  ADD CONSTRAINT yvs_com_parametre_achat_journal_fkey FOREIGN KEY (journal)
      REFERENCES yvs_compta_journaux (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_com_parametre_vente ADD COLUMN journal bigint;
ALTER TABLE yvs_com_parametre_vente
  ADD CONSTRAINT yvs_com_parametre_vente_journal_fkey FOREIGN KEY (journal)
      REFERENCES yvs_compta_journaux (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_com_parametre_stock ADD COLUMN journal bigint;
ALTER TABLE yvs_com_parametre_stock
  ADD CONSTRAINT yvs_com_parametre_stock_journal_fkey FOREIGN KEY (journal)
      REFERENCES yvs_compta_journaux (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
