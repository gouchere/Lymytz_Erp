DROP TABLE yvs_mut_operation_caisse;
-Enlever la contrainte not null sur les colonnes de la table mouvement_caisse

COMMENT ON COLUMN yvs_mut_mouvement_caisse.mouvement IS 'sens de la dépense (D=Dépôt, R=Retrait)';


ALTER TABLE yvs_mut_parametre ADD COLUMN paiement_par_compte_strict boolean;
ALTER TABLE yvs_mut_parametre ALTER COLUMN paiement_par_compte_strict SET DEFAULT true;


ALTER TABLE yvs_mut_parametre ADD COLUMN accept_retrait_epargne boolean;
ALTER TABLE yvs_mut_parametre ALTER COLUMN accept_retrait_epargne SET DEFAULT false;


ALTER TABLE yvs_mut_caisse ADD COLUMN principal boolean;
ALTER TABLE yvs_mut_caisse ALTER COLUMN principal SET DEFAULT false;


ALTER TABLE yvs_mut_parametre ADD COLUMN debut_epargne integer;
ALTER TABLE yvs_mut_parametre ALTER COLUMN debut_epargne SET DEFAULT 1;

ALTER TABLE yvs_mut_parametre ADD COLUMN fin_epargne integer;
ALTER TABLE yvs_mut_parametre ALTER COLUMN fin_epargne SET DEFAULT 5;

--Observer les données des tables user_agences
-- yvs_mut_comptes
-- yvs_mut_caisses


ALTER TABLE yvs_mut_parametre ADD COLUMN retard_saisie_epargne integer;
ALTER TABLE yvs_mut_parametre ALTER COLUMN retard_saisie_epargne SET DEFAULT 5;

INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('mut_antidate_epargne_out_periode', 'Antidater l''Epargne au delà de la limite de date prévu', 'Antidater l''Epargne au delà de la limite de date prévu', (SELECT id FROM yvs_page_module WHERE reference = 'mutuel_eparg_assuranc'));
	
	INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module)
	VALUES ('mut_atidate_epargne', 'Antidater l''Epargne dans la limite de date prévu', 'Antidater l''Epargne dans la limite de date prévu', (SELECT id FROM yvs_page_module WHERE reference = 'mutuel_eparg_assuranc'));
	
ALTER TABLE yvs_mut_parametre ADD COLUMN credit_retains_interet boolean;
ALTER TABLE yvs_mut_parametre ALTER COLUMN credit_retains_interet SET DEFAULT true;

ALTER TABLE yvs_mut_echellonage ADD COLUMN credit_retains_interet boolean;
ALTER TABLE yvs_mut_echellonage ALTER COLUMN credit_retains_interet SET DEFAULT false;

ALTER TABLE yvs_mut_parametre DROP COLUMN taux_global_avance_salaire;
 ALTER TABLE yvs_mut_parametre DROP COLUMN jour_debut_mois;
 ALTER TABLE yvs_mut_parametre DROP COLUMN jour_fin_mois;
 ALTER TABLE yvs_mut_parametre RENAME COLUMN fraction_salaire_minimal TO capacite_endettement;
ALTER TABLE yvs_mut_parametre DROP COLUMN taux_forcage;
ALTER TABLE yvs_mut_parametre DROP COLUMN base_forcage;
ALTER TABLE yvs_mut_parametre DROP COLUMN action_deficit;
ALTER TABLE yvs_mut_parametre DROP COLUMN nbre_avalise_obligatoir;
ALTER TABLE yvs_mut_parametre ADD COLUMN base_capacite_endettement character varying;
ALTER TABLE yvs_mut_parametre ALTER COLUMN base_capacite_endettement SET DEFAULT 'EPARGNE'::character varying;

ALTER TABLE yvs_mut_poste_employe DROP CONSTRAINT yvs_mut_poste_employe_poste_fkey;

ALTER TABLE yvs_mut_poste_employe
  ADD CONSTRAINT yvs_mut_poste_employe_poste_fkey FOREIGN KEY (poste)
      REFERENCES yvs_mut_poste (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE yvs_mut_prime_poste DROP CONSTRAINT yvs_mut_prime_poste_poste_fkey;

ALTER TABLE yvs_mut_prime_poste
  ADD CONSTRAINT yvs_mut_prime_poste_poste_fkey FOREIGN KEY (poste)
      REFERENCES yvs_mut_poste (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE yvs_mut_compte DROP CONSTRAINT yvs_mut_compte_type_fkey;

ALTER TABLE yvs_mut_compte
  ADD CONSTRAINT yvs_mut_compte_type_fkey FOREIGN KEY (type_compte)
      REFERENCES yvs_mut_type_compte (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE cascade;

