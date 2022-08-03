ALTER TABLE yvs_grh_element_salaire DROP CONSTRAINT yvs_grh_element_salaire_quantite_fkey;
ALTER TABLE yvs_grh_element_salaire
  ADD CONSTRAINT yvs_grh_element_salaire_quantite_fkey FOREIGN KEY (quantite)
      REFERENCES yvs_grh_element_salaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
	  
ALTER TABLE yvs_grh_element_salaire DROP CONSTRAINT yvs_grh_element_salaire_base_pourcentage_fkey;
ALTER TABLE yvs_grh_element_salaire
  ADD CONSTRAINT yvs_grh_element_salaire_base_pourcentage_fkey FOREIGN KEY (base_pourcentage)
      REFERENCES yvs_grh_element_salaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE yvs_grh_element_structure DROP CONSTRAINT yvs_element_structure_element_salaire_fkey;	  
ALTER TABLE yvs_grh_element_structure
  ADD CONSTRAINT yvs_element_structure_element_salaire_fkey FOREIGN KEY (element_salaire)
      REFERENCES yvs_grh_element_salaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE yvs_grh_detail_bulletin DROP CONSTRAINT yvs_grh_montan_salaire_element_salaire_fkey;	  
ALTER TABLE yvs_grh_detail_bulletin
  ADD CONSTRAINT yvs_grh_montan_salaire_element_salaire_fkey FOREIGN KEY (element_salaire)
      REFERENCES yvs_grh_element_salaire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
	  
	  
DELETE FROM yvs_page_module
 WHERE reference='grh_assuranc_'
DELETE FROM yvs_page_module
 WHERE reference='grh_assureur_'
 DELETE FROM yvs_page_module
 WHERE reference='grh_tache_'

 
 
 ALTER TABLE yvs_regle_tache RENAME TO yvs_grh_regle_tache;
 ALTER TABLE yvs_taches RENAME TO yvs_grh_taches;
 ALTER TABLE yvs_tache_emps RENAME TO yvs_grh_tache_emps;
 ALTER TABLE yvs_cout_mission RENAME TO yvs_grh_cout_mission;
 
 ALTER TABLE yvs_grh_tache_emps DROP COLUMN etat; 
 
ALTER TABLE yvs_grh_tache_emps ADD COLUMN statut_tache character(1);
COMMENT ON COLUMN yvs_grh_tache_emps.statut_tache IS 'C=Create
V=valider ou confirmé
R=En cours de réalisation (Running)
T=terminé';

ALTER TABLE yvs_grh_tache_emps DROP COLUMN tache;

ALTER TABLE yvs_grh_tache_emps ADD COLUMN tache bigint;


ALTER TABLE yvs_grh_tache_emps
  ADD CONSTRAINT yvs_grh_tache_emps_tache_fkey FOREIGN KEY (tache)
      REFERENCES yvs_grh_montant_tache (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  

-- Table: yvs_grh_realisation_tache

-- DROP TABLE yvs_grh_realisation_tache;

CREATE TABLE yvs_grh_realisation_tache
(
  id bigint NOT NULL DEFAULT nextval('yvs_grh_tache_realise_id_seq'::regclass),
  date_realisation date,
  tache bigint,
  quantite_realise double precision,
  statut character(1),
  pourcentage_validation double precision,
  CONSTRAINT yvs_grh_tache_realise_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_realisation_tache_tache_fkey FOREIGN KEY (tache)
      REFERENCES yvs_grh_tache_emps (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_realisation_tache
  OWNER TO postgres;


DROP FUNCTION tache_montant_total(bigint, integer, date, date);
DROP FUNCTION tache_montant_all_total(bigint, integer, date);

-- Function: tache_montant_total(bigint, integer, date, date)

-- DROP FUNCTION tache_montant_total(bigint, integer, date, date);

CREATE OR REPLACE FUNCTION tache_montant_total(id_ bigint, date_debut date, date_fin date)
  RETURNS double precision AS
$BODY$DECLARE
    vide_ VARCHAR default null; 
	employe_ RECORD;
	tache_ RECORD;
	montant_ double precision default 0;
	rem_tache_ RECORD;
	prime_ RECORD;
	interval_ RECORD ;
	quantite_ double precision default 0;
	
BEGIN
    -- recherche des informations sur un employe
	select into employe_ e.id, e.regle_tache, a.societe FROM yvs_grh_employes e inner join yvs_agences a on e.agence = a.id  where e.id = id_ limit 1;
	
	--trouve toutes les tâche réalisé par l'employé entre deux date
	for tache_ IN SELECT rt.*, te.id, te.tache AS id_montant_tache FROM yvs_grh_realisation_tache rt INNER JOIN yvs_grh_tache_emps te ON te.id=rt.tache WHERE te.employe=employe_.id AND rt.statut='T' 
		LOOP	
			--pour chacune de ces tâche calcul le montant
			SELECT INTO rem_tache_ m.* FROM yvs_grh_montant_tache m WHERE m.id=tache_.id_montant_tache AND m.actif is true;
			IF rem_tache_.montant IS NOT NULL THEN
				montant_ = (montant_ + (rem_tache_.montant * tache_.pourcentage_validation/100));
			END IF;
			IF (rem_tache_.prime_tache IS NOT NULL  AND tache_.quantite_realise IS NOT NULL )THEN
			
				--calcul la prime correspondant à la quantité réalisé de cette tache
					--récupération ordonné des primes par quantité
					FOR prime_ IN SELECT  i.* FROM yvs_grh_interval_prime_tache i INNER JOIN yvs_grh_prime_tache p ON p.id=i.prime_tache WHERE p.id=rem_tache_.prime_tache AND p.actif IS TRUE ORDER BY i.quantite DESC
					LOOP						
						IF tache_.quantite_realise >= prime_.quantite THEN
						  interval_ :=prime_;
						  EXIT; 
						END IF;
					END LOOP;					
				--IF interval_ IS NOT NULL THEN 					
				RAISE NOTICE 'HERE %',interval_.quantite;
					IF interval_.taux IS FALSE THEN
					  montant_=montant_+ (interval_.montant * tache_.quantite_realise);
					ELSE
					  montant_=montant_+ ((interval_.montant*rem_tache_.montant/100)*tache_.quantite_realise); 	
					END IF;
				--END IF;
			END IF;			
		END LOOP;
	return montant_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tache_montant_total(bigint, date, date)
  OWNER TO postgres;

  
ALTER TABLE yvs_parametre_grh DROP COLUMN pointage_auto;
ALTER TABLE yvs_parametre_grh DROP COLUMN heure_supplementaire;

ALTER TABLE yvs_grh_contrat_emps DROP CONSTRAINT yvs_contrat_emps_regle_tache_fkey;
ALTER TABLE yvs_grh_contrat_emps DROP COLUMN regle_tache;

ALTER TABLE yvs_grh_contrat_emps DROP COLUMN salaire_tache;

ALTER TABLE yvs_grh_contrat_emps DROP COLUMN contrat_principal;
 ALTER TABLE yvs_grh_tache_emps DROP COLUMN quantite;
ALTER TABLE yvs_grh_tache_emps RENAME COLUMN pontuelle TO planification_permanente 
 ALTER TABLE yvs_grh_tache_emps DROP COLUMN statut_tache;
 ALTER TABLE yvs_grh_tache_emps DROP COLUMN date_debut;
  
 
