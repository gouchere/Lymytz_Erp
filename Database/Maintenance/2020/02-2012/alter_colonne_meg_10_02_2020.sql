		
SELECT insert_droit('bcv_view_all_doc', 'Voir toutes les commandes de la socièté', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bcv'), 16, 'A,B,C','R');		
	
SELECT insert_droit('bcv_view_only_doc_agence', 'Voir uniquement les commandes de l''agence', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bcv'), 16, 'A,B,C','R');		
	
SELECT insert_droit('bcv_view_only_doc_pv', 'Voir uniquement les commandes de mes points de vente', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bcv'), 16, 'A,B,C','R');		
	
SELECT insert_droit('bcv_view_only_doc_depot', 'Voir uniquement les commandes de mes dépôts', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_bcv'), 16, 'A,B,C','R');
	
SELECT insert_droit('compta_annule_lettrage_not_equilib', 'Annuler le lettrage meme si l''ecriture n''est pas equilibré', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_saisie_libre'), 16, 'A,B,C','R');
	
SELECT alter_action_colonne_key('yvs_ressources_page', true, true);
DELETE FROM yvs_ressources_page WHERE reference_ressource = 'fv_view_all_type_doc';	
SELECT alter_action_colonne_key('yvs_ressources_page', true, false);

ALTER TABLE yvs_com_liaison_depot ADD COLUMN transit boolean;
ALTER TABLE yvs_com_liaison_depot ALTER COLUMN transit SET DEFAULT false;

ALTER TABLE yvs_com_doc_ventes ALTER COLUMN date_livraison_prevu TYPE timestamp without time zone;

ALTER TABLE yvs_com_parametre_vente ADD COLUMN sell_lower_pr boolean;
ALTER TABLE yvs_com_parametre_vente ALTER COLUMN sell_lower_pr SET DEFAULT true;
	
SELECT insert_droit('stat_general', 'Page statistique général', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_ca', 'Page chiffre d''affaire globale', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_classement', 'Page des classements', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');				
	
SELECT insert_droit('stat_listing_vente', 'Page des listings vente', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');				
	
SELECT insert_droit('stat_journal_vente_vendeur', 'Page des journaux vente par vendeur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_ecart_vendeur', 'Page des ecarts vendeur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_classement_vendeur', 'Page des classements vendeur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_creance_vendeur', 'Page des créances vendeur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_ca_client', 'Page chiffre d''affaire par client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_ristourne_client', 'Page des ristournes client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');				
	
SELECT insert_droit('stat_listing_vente_client', 'Page des listings vente par client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_classement_client', 'Page des classements client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');					
	
SELECT insert_droit('stat_journal_vente_client', 'Page des journaux vente par client', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_progression_client', 'Page progressions des clients', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_valorisation_stock', 'Page de valorisation des stocks', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_distribution_stock', 'Page de distribution des stocks', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_resume', 'Page des résumés statistique', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_journal_production', 'Page des journaux de production', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_synthese_production', 'Page des synthèses de production', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_ecart_production', 'Page des écarts de production', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_production_vente', 'Page des productions / ventes', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_bon_provisoire_encours', 'Page des bon provisoires encours', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_solde_caisse', 'Page des soldes des caisses', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_brouillard_caisse', 'Page des brouillards de caisse', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_facture_achat_impaye', 'Page des factures d''achats impayées', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');		
	
SELECT insert_droit('stat_ca_fournisseur', 'Page chiffre d''affaire par fournisseur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_classement_fournisseur', 'Page des classements fournisseur', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_ca_article', 'Page chiffre d''affaire par article', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');			
	
SELECT insert_droit('stat_classement_article', 'Page des classements article', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');				
	
SELECT insert_droit('stat_marge_article', 'Page des marges article', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');					
	
SELECT insert_droit('stat_listing_vente_article', 'Page des listings vente par article', 
	(SELECT id FROM yvs_module WHERE reference = 'stat_'), 16, 'A,B,C','P');
	
-- Table: yvs_stat_dashboard
-- DROP TABLE yvs_stat_dashboard;
CREATE TABLE yvs_stat_dashboard
(
  id bigserial NOT NULL,
  code character varying,
  libelle character varying,
  groupe character varying,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  CONSTRAINT yvs_stat_dashboard_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_stat_dashboard_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_stat_dashboard
  OWNER TO postgres;

-- Table: yvs_stat_dashboard_users
-- DROP TABLE yvs_stat_dashboard_users;
CREATE TABLE yvs_stat_dashboard_users
(
  id bigserial NOT NULL,
  dashboard bigint,
  users bigint,
  active boolean default true,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  CONSTRAINT yvs_stat_dashboard_users_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_stat_dashboard_users_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_stat_dashboard_users_dashboard_fkey FOREIGN KEY (dashboard)
      REFERENCES yvs_stat_dashboard (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_stat_dashboard_users_users_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_stat_dashboard_users
  OWNER TO postgres;
  
SELECT insert_droit('gescom_tbg_stock', 'Tableau de bord des stocks', 
	(SELECT id FROM yvs_module WHERE reference = 'com_'), 16, 'A,B,C','P');
	
SELECT insert_droit('gescom_tbg_vente', 'Tableau de bord des ventes', 
	(SELECT id FROM yvs_module WHERE reference = 'com_'), 16, 'A,B,C','P');
	
SELECT insert_droit('gescom_dashboard_fournisseur', 'Tableau de bord des fournisseurs', 
	(SELECT id FROM yvs_module WHERE reference = 'com_'), 16, 'A,B,C','P');
	
SELECT insert_droit('gescom_dashboard_vendeur', 'Tableau de bord des vendeurs', 
	(SELECT id FROM yvs_module WHERE reference = 'com_'), 16, 'A,B,C','P');

INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('MARGE_MS_ARTICLE', 'Marge marchandise par article', 'MARGES');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('MARGE_PF_ARTICLE', 'Marge produit fini par classe', 'MARGES');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('MARGE_CLASSE', 'Marges par classe', 'MARGES');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('MARGE_FAMILLE', 'Marges par famille', 'MARGES');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('MARGE_POINT', 'Marges par point de vente', 'MARGES');

INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('CA_ARTICLE_AGENCE', 'Chriffre d''affaire des articles par agence', 'CA');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('CA_ARTICLE_POINT', 'Chriffre d''affaire des articles par point', 'CA');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('CA_ARTICLE_VENDEUR', 'Chriffre d''affaire des articles par vendeur', 'CA');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('CA_POINT_AGENCE', 'Chriffre d''affaire des points de vente', 'CA');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('CA_VENDEUR_AGENCE', 'Chriffre d''affaire des vendeurs', 'CA');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('CA_GENERAL', 'Chriffre d''affaire généraux', 'CA');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('CA_RESUME', 'Chriffre d''affaire résumé', 'CA');

INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('LISTING_ARTICLE', 'Listing article', 'LISTING');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('LISTING_CLIENT', 'Listing client', 'LISTING');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('LISTING_FACTURE', 'Listing par ventes', 'LISTING');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('LISTING_GROUPE', 'Listing groupé', 'LISTING');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('LISTING_RISTOURNE', 'Ristourne', 'LISTING');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('LISTING_IMPAYE_ACHAT', 'Facture achat impayé', 'LISTING');

INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('CLASSEMENT_CLIENT', 'Classement client', 'CLASSEMENT');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('CLASSEMENT_VENDEUR', 'Classement vendeur', 'CLASSEMENT');

INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('SYNTHESE_DISTRIBUTION', 'Synthèse de distribution', 'STOCK');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('VALORISATION_STOCK', 'Valorisation stock', 'STOCK');

INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('CREANCE_VENDEUR', 'Créances vendeur', 'JOURNAL');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('JOURNAL_VENTE', 'Journal de vente', 'JOURNAL');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('ECART_VENDEUR', 'Ecart vendeur', 'JOURNAL');

INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('BON_ENCOURS', 'Bon provisoire en cours', 'CAISSE');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('SOLDE_CAISSE', 'Solde des caisses', 'CAISSE');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('SOLDE_BANQUE', 'Solde des banques', 'CAISSE');

INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('JOURNAL_PRODUCTION_PF', 'Production PF', 'PRODUCTION');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('JOURNAL_PRODUCTION_PFS', 'Production PSF', 'PRODUCTION');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('JOURNAL_PRODUCTION_PF_EQUIPE', 'Production PF par équipe', 'PRODUCTION');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('JOURNAL_PRODUCTION_PSF_EQUIPE', 'Production PSF par équipe', 'PRODUCTION');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('JOURNAL_PRODUCTION_PF_TRANCHE', 'Production PF par tranche', 'PRODUCTION');
INSERT INTO yvs_stat_dashboard(code, libelle, groupe) VALUES ('JOURNAL_PRODUCTION_PSF_TRANCHE', 'Production PSF par tranche', 'PRODUCTION');

