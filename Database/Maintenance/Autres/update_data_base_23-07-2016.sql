ALTER TABLE yvs_grh_majoration_conge DROP COLUMN supp;
ALTER TABLE yvs_grh_interval_majoration DROP COLUMN supp;

ALTER TABLE yvs_interval_majoration RENAME TO yvs_grh_interval_majoration


ALTER TABLE yvs_grh_majoration_conge ADD COLUMN unite_intervale character varying;

ALTER TABLE yvs_articles_template RENAME TO yvs_base_articles_template
ALTER TABLE yvs_tiers RENAME TO yvs_base_tiers

ALTER TABLE yvs_base_articles_template DROP COLUMN code_barre;
ALTER TABLE yvs_base_articles_template DROP COLUMN photo_1;
ALTER TABLE yvs_base_articles_template DROP COLUMN ref_art;
ALTER TABLE yvs_base_articles_template DROP COLUMN supp;
ALTER TABLE yvs_base_articles_template DROP COLUMN agence;
ALTER TABLE yvs_base_articles_template DROP COLUMN photo_2;
ALTER TABLE yvs_base_articles_template DROP COLUMN photo_3;
ALTER TABLE yvs_base_articles_template ADD COLUMN ref_template character varying;


ALTER TABLE yvs_base_famille_article RENAME COLUMN reference TO reference_famille ;

ALTER TABLE yvs_base_tiers DROP COLUMN supp;
ALTER TABLE yvs_base_tiers DROP COLUMN sup;
ALTER TABLE yvs_base_tiers DROP CONSTRAINT yvs_tiers_agence_fkey;
ALTER TABLE yvs_base_tiers DROP COLUMN agence to agence;
ALTER TABLE yvs_base_tiers ADD COLUMN societe bigint;
ALTER TABLE yvs_base_tiers
  ADD CONSTRAINT yvs_base_tiers_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  
ALTER TABLE yvs_base_groupes_article DROP COLUMN last_author;
 ALTER TABLE yvs_base_groupes_article DROP COLUMN last_date_save;
ALTER TABLE yvs_base_groupes_article DROP COLUMN date_save;
ALTER TABLE yvs_base_groupes_article DROP COLUMN supp;
ALTER TABLE yvs_articles DROP COLUMN societe;
ALTER TABLE yvs_articles DROP COLUMN supp;


ALTER TABLE yvs_plan_tarif_groupe DROP CONSTRAINT yvs_plan_tarif_groupe_categorie_fkey;
DROP TABLE yvs_prixcategorie;


DROP TABLE yvs_cat_tarif

ALTER TABLE yvs_com_categorie_client RENAME TO yvs_base_categorie_client

ALTER TABLE yvs_base_categorie_client ADD COLUMN actif boolean;

ALTER TABLE yvs_com_categorie_tarifaire_client RENAME TO yvs_base_plan_tarifaire



ALTER TABLE yvs_grh_missions RENAME COLUMN colturer TO cloturer

-- Table: yvs_grh_header_bulletin

-- DROP TABLE yvs_grh_header_bulletin;

CREATE TABLE yvs_grh_header_bulletin
(
  id bigserial NOT NULL,
  matricule character varying,
  cni character varying,
  poste character varying,
  departement character varying,
  date_embauche date,
  telephone character varying,
  email character varying,
  categorie_pro character varying,
  echelon_pro character varying,
  matricule_cnps character varying,
  bulletin bigint,
  author bigint,
  code_agence character varying,
  designation_agence character varying,
  code_societe character varying,
  adresse_agence character varying,
  adresse_societe character varying,
  nom_employe character varying,
  anciennete character varying,
  civilite character varying,
  adresse_employe character varying,
  CONSTRAINT yvs_grh_header_bulletin_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_header_bulletin_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_header_bulletin_bulletin_fkey FOREIGN KEY (bulletin)
      REFERENCES yvs_grh_bulletins (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_header_bulletin
  OWNER TO postgres;
COMMENT ON TABLE yvs_grh_header_bulletin
  IS 'cette Table garde l''etat de l''en tête d''un bulletin. le besoin de garder ces infos statique plutot que de les retrouver par liaison est lié au fait que si on  reimprime un bulletin après un certains temps, sont en-tete peux ne plus correspondre à l''entête au moment  de l''edition du bulletin';

  
  
 -- Function: find_disponibilite_employe(bigint, date)

-- DROP FUNCTION find_disponibilite_employe(bigint, date);

CREATE OR REPLACE FUNCTION find_disponibilite_employe(employe_ bigint, date_jour_ date)
  RETURNS character varying AS
$BODY$
DECLARE result_ character varying; 
	id_ bigint;
    
BEGIN
 SELECT INTO id_ y.id FROM yvs_grh_missions y  WHERE y.employe=employe_ AND (date_jour_ BETWEEN y.date_debut AND y.date_fin) AND y.actif is true AND y.statut_mission='V';
 IF id_ IS NOT NULL THEN 
	result_='En Mission';
 END IF;
SELECT INTO id_ y.id FROM yvs_grh_formation_emps y WHERE y.employe=employe_ AND (date_jour_ BETWEEN y.date_debut AND y.date_fin) AND y.actif is true;
 IF id_ IS NOT NULL THEN 
	result_='En Formation';
 END IF;
 SELECT INTO id_ y.id FROM yvs_grh_conge_emps y WHERE y.employe=employe_ AND (date_jour_ BETWEEN y.date_debut AND y.date_fin) AND y.actif is true AND y.statut='V';
 IF id_ IS NOT NULL THEN 
	result_='En Congé';
 END IF;
 --cherche la journée férié
 SELECT INTO id_ a.societe FROM yvs_grh_employes y INNER JOIN yvs_agences a ON a.id=y.agence WHERE y.id=employe_ LIMIT 1;
 SELECT INTO result_ y.titre FROM yvs_jours_feries y WHERE date_jour_ =y.jour AND y.actif is true AND y.societe=id_;
	RETURN result_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION find_disponibilite_employe(bigint, date)
  OWNER TO postgres;

  
----------------------
-----------------------
ALTER TABLE yvs_jours_ouvres ADD COLUMN ouvrable boolean;
ALTER TABLE yvs_jours_ouvres ALTER COLUMN ouvrable SET DEFAULT true;
COMMENT ON COLUMN yvs_jours_ouvres.ouvrable IS 'précise les jour ouvrable pour l''employé';


ALTER TABLE yvs_com_creneau_horaire ADD COLUMN type_creno integer;
ALTER TABLE yvs_com_creneau_horaire
  ADD CONSTRAINT yvs_com_creneau_horaire_type_creno_fkey FOREIGN KEY (type_creno)
      REFERENCES yvs_com_type_creneau_horaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

	  
	  
DROP TABLE yvs_grh_detail_prelevement_emps;
DROP TABLE yvs_grh_prelevement_emps;
ALTER TABLE yvs_grh_plan_prelevement DROP COLUMN base_interval;
ALTER TABLE yvs_grh_plan_prelevement DROP CONSTRAINT yvs_plan_prelevement_societe_fkey;
ALTER TABLE yvs_grh_plan_prelevement DROP COLUMN societe;

ALTER TABLE yvs_grh_plan_prelevement ADD COLUMN base_retenue character(1);
ALTER TABLE yvs_grh_plan_prelevement ADD COLUMN valeur double precision;

ALTER TABLE yvs_grh_plan_prelevement ADD COLUMN taux boolean;
COMMENT ON COLUMN yvs_grh_plan_prelevement.taux IS 'indique si la valeur est évalué en pourcentage';
ALTER TABLE yvs_grh_plan_prelevement ADD COLUMN actif boolean;
ALTER TABLE yvs_grh_plan_prelevement ADD COLUMN nombre_mois integer;
ALTER TABLE yvs_grh_plan_prelevement ADD COLUMN defaut boolean;
COMMENT ON COLUMN yvs_grh_plan_prelevement.defaut IS 'Précise le plan par défaut: on ne peut avoir qu\'un plan par défaut';

ALTER TABLE yvs_grh_element_additionel ADD COLUMN statut character(1);
COMMENT ON COLUMN yvs_grh_element_additionel.statut IS 'R=en cours
R=réglé
S=Suspendu';
ALTER TABLE yvs_grh_element_additionel ADD COLUMN plan_prelevement bigint;

ALTER TABLE yvs_grh_element_additionel
  ADD CONSTRAINT yvs_grh_element_additionel_plan_prelevement_fkey FOREIGN KEY (plan_prelevement)
      REFERENCES yvs_grh_plan_prelevement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

	  -- Table: yvs_grh_detail_prelevement_emps
	  
	  
ALTER TABLE yvs_grh_realisation_tache ADD COLUMN debut_realisation date;
ALTER TABLE yvs_grh_realisation_tache ADD COLUMN fin_realisation date;
ALTER TABLE yvs_grh_realisation_tache ADD COLUMN author bigint;

-- DROP TABLE yvs_grh_detail_prelevement_emps;

CREATE TABLE yvs_grh_detail_prelevement_emps
(
  id bigserial NOT NULL,
  date_prelevement date,
  statut_reglement character(1), -- R=réglé...
  reference character varying,
  retenue bigint,
  plan_prelevement bigint,
  valeur double precision,
  author bigint,
  CONSTRAINT yvs_grh_detail_prelevement_emps_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_detail_prelevement_emps_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_detail_prelevement_emps_plan_prelevement_fkey FOREIGN KEY (plan_prelevement)
      REFERENCES yvs_grh_plan_prelevement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_detail_prelevement_emps_retenue_fkey FOREIGN KEY (retenue)
      REFERENCES yvs_grh_element_additionel (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_detail_prelevement_emps
  OWNER TO postgres;
COMMENT ON COLUMN yvs_grh_detail_prelevement_emps.statut_reglement IS 'R=réglé
E=encours
S=suspendu';

ALTER TABLE yvs_grh_plan_prelevement DROP COLUMN nombre_mois;
ALTER TABLE yvs_grh_plan_prelevement DROP COLUMN taux;



ALTER TABLE yvs_grh_plan_prelevement ADD COLUMN base_plan character(1);
COMMENT ON COLUMN yvs_grh_plan_prelevement.base_plan IS 'F=fixe
T=taux
D=Durée';


-- Function: conge_jour_avoir(bigint, date)

 --DROP FUNCTION calcul_periode_reference(id_ bigint, date_jour_ date);

CREATE OR REPLACE FUNCTION calcul_periode_reference(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 
	employe_ RECORD; 
	code_ RECORD;
	date_d DATE ;
	date_new_ DATE ;
	duree_ INTEGER DEFAULT 0;
	interval_ interval;
    
BEGIN
	-- recherche des informations sur un employe
	select into employe_ e.id, co.conge_acquis, e.date_embauche, e.civilite, a.societe
		from yvs_grh_employes e inner join yvs_grh_contrat_emps co on co.employe = e.id inner join yvs_agences a on e.agence = a.id 
		where e.id = id_ limit 1;
	--recherche la date du dernier conge annuelle
	SELECT INTO date_d co.date_fin FROM yvs_grh_conge_emps co WHERE  co.employe=id_ AND co.type_conge='Annuelle' AND co.statut='C' ORDER BY co.date_fin DESC offset 0 limit 1;
	select into code_ date_debut_exercice, duree_cumule_conge from yvs_parametre_grh 
                where societe = employe_.societe limit 1; 
                --recherche la date de départ du nouveau congé enregisté
	SELECT INTO date_new_ co.date_debut FROM yvs_grh_conge_emps co WHERE  co.employe=id_ AND co.type_conge='Annuelle' AND co.statut='V' ORDER BY co.date_fin DESC offset 0 limit 1;
	IF date_d IS NULL THEN		
		date_d =code_.date_debut_exercice;
	END IF;
	-- recherche des informations sur la date de debut d'exercice de la societe		
		IF date_d IS NOT NULL THEN
		  --nombre de mois entre les deux dates
		  interval_=age(date_new_,date_d);
		  duree_ = (SELECT (12* extract(year FROM interval_)::integer) + (extract(month FROM interval_)::integer));			
		END IF;
				
	-- calcul du nombre de jours de conge a prendre a la date du jour
	IF(duree_ IS NULL) THEN 
	   duree_=0;
	END IF;
	return duree_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION calcul_periode_reference(bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION calcul_periode_reference(bigint, date) IS 'retourne le nombre de mois de référence. qui correspond au nombre de mois entre 
le retours du précédent congé et le départ pour le nouveau congé';



-- Table: yvs_grh_details_retenue

-- DROP TABLE yvs_grh_details_retenue;

CREATE TABLE yvs_grh_composants_retenue
(
  id bigserial NOT NULL,
  motif character varying,
  montant double precision,
  actif boolean,
  date_ajout date,
  author bigint,
  retenue bigint,
  CONSTRAINT yvs_grh_composants_retenue_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_composants_retenue_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_composants_retenue_retenue_fkey FOREIGN KEY (retenue)
      REFERENCES yvs_grh_element_additionel (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_composants_retenue
  OWNER TO postgres;


  -- Function: grh_salaire_insert_new_retenue()

-- DROP FUNCTION grh_salaire_insert_new_retenue();

CREATE OR REPLACE FUNCTION grh_salaire_insert_new_retenue()
  RETURNS trigger AS
$BODY$    
DECLARE
	type_ret_ RECORD;
	id_ bigint;
BEGIN
	--cherche la nature du mouvements
	SELECT INTO type_ret_ t.* FROM yvs_grh_element_additionel e INNER JOIN yvs_grh_type_element_additionel t ON t.id=e.type_element WHERE e.id=NEW.id;
	IF(type_ret_.retenu) THEN 
		-- insert la ligne des composants
		INSERT INTO yvs_grh_composants_retenue (motif,montant,actif,date_ajout,author,retenue)
			                      VALUES(NEW.description, NEW.montant_element, true, NEW.date_element, NEW.author, NEW.id);
	        UPDATE yvs_grh_element_additionel SET montant_element=NEW.montant_element WHERE id=NEW.id;
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_salaire_insert_new_retenue()
  OWNER TO postgres;
COMMENT ON FUNCTION grh_salaire_insert_new_retenue() IS 'cette fonction permet de créer une ligne composant de retenu. en effet, ce choix d''implémentation est parcequ''on voudrai rendre uniforme la source du montant de la retenu. qui dans ce cas doit dépendre 
entièrement des éléments de composant retenu';




-- Function: grh_salaire_insert_new_composant_retenue()

-- DROP FUNCTION grh_salaire_insert_new_composant_retenue();

CREATE OR REPLACE FUNCTION grh_salaire_insert_new_composant_retenue()
  RETURNS trigger AS
$BODY$    
DECLARE
	type_ret_ RECORD;
	action_ character varying;
	montant_ double precision default 0;
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT') THEN 
		IF(NEW.actif) THEN 		
			UPDATE yvs_grh_element_additionel SET montant_element=montant_element + NEW.montant WHERE id=NEW.retenue;		
		END IF;
	END IF;
	IF(action_='UPDATE') THEN 
		IF(NEW.actif) THEN 
			-- insert la ligne des composants		
			UPDATE yvs_grh_element_additionel SET montant_element=(montant_element-OLD.montant )+NEW.montant WHERE id=NEW.retenue;
		ELSE
			UPDATE yvs_grh_element_additionel SET montant_element= (montant_element-OLD.montant )-NEW.montant WHERE id=NEW.retenue;
		END IF;
	END IF;
	IF(action_='DELETE' OR action_='TRONCATE') THEN 		
			UPDATE yvs_grh_element_additionel SET montant_element= montant_element-OLD.montant WHERE id=OLD.retenue;
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_salaire_insert_new_composant_retenue()
  OWNER TO postgres;

  
  CREATE TRIGGER insert_update_composant_ret
  AFTER INSERT OR UPDATE
  ON yvs_grh_composants_retenue
  FOR EACH ROW
  EXECUTE PROCEDURE grh_salaire_insert_new_composant_retenue();

  
CREATE TRIGGER "insert_newRetenue"
  AFTER INSERT
  ON yvs_grh_element_additionel
  FOR EACH ROW
  EXECUTE PROCEDURE grh_salaire_insert_new_retenue();

  
  
  CREATE OR REPLACE FUNCTION grh_salaire_change_statut_retenue()
  RETURNS trigger AS
$BODY$    
DECLARE
	all_regle_ boolean default true;
	line_ RECORD;
BEGIN	
	 FOR line_ IN SELECT de.* FROM yvs_grh_detail_prelevement_emps de WHERE de.retenue=NEW.retenue
	 LOOP
		IF(line_.statut_reglement!='R') THEN
		  RETURN NEW;
		END IF;
	 END LOOP;
	 UPDATE yvs_grh_element_additionel SET statut='R', author=NEW.author WHERE id=NEW.retenue;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_salaire_change_statut_retenue()
  OWNER TO postgres;
  
  
CREATE TRIGGER grh_update_etat_element_retenue
  AFTER UPDATE
  ON yvs_grh_detail_prelevement_emps
  FOR EACH ROW
  EXECUTE PROCEDURE grh_salaire_change_statut_retenue();

  
  
  
 

CREATE OR REPLACE FUNCTION grh_employe_insert_date_prochain_avanacement()
  RETURNS trigger AS
$BODY$
DECLARE
	infos_ RECORD;
	periode_ INTEGER;
	date_ VARCHAR default '01/01/1990';
  
BEGIN	
	--positionne la date du prochain avancement
	select into infos_ p.periode_davancement, p.periode_premier_avancement from yvs_parametre_grh p inner join yvs_agences a on p.societe = a.societe
		where a.id = NEW.agence ;
	periode_ = infos_.periode_davancement + infos_.periode_premier_avancement;
	date_ = (((select extract(day from current_date)) || '-' || (select extract(month from current_date)) ||'-'|| (select extract(year from current_date)) + periode_));
	NEW.date_prochain_avancement = to_date(date_,'DD MM YYYY') ;
	IF (TG_OP='UPDATE') THEN
	--écoute la désactivation d'un employé
		IF(NEW.actif IS false) THEN
		   -- désactive ses contrats
		   UPDATE yvs_grh_contrat_emps SET actif=false WHERE employe=NEW.id;
		END IF;
	END IF;
	return NEW;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_employe_insert_date_prochain_avanacement()
  OWNER TO postgres;
  
  -----------------
  --forme contraintes
  -----------------
  
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
  
 
--------------------------
--NEW MODIF
--------------------------
ALTER TABLE yvs_com_remise RENAME COLUMN reference TO ref_remise ;

ALTER TABLE yvs_com_plan_remise DROP COLUMN remise;
ALTER TABLE yvs_com_plan_remise ADD COLUMN remise double precision;
ALTER TABLE yvs_com_plan_remise ADD COLUMN prix_vente double precision;

CREATE OR REPLACE FUNCTION grh_salaire_change_statut_retenue()
  RETURNS trigger AS
$BODY$    
DECLARE
	all_regle_ boolean default true;
	line_ RECORD;
BEGIN	
	 FOR line_ IN SELECT de.* FROM yvs_grh_detail_prelevement_emps de WHERE de.retenue=NEW.retenue
	 LOOP
		IF(line_.statut_reglement!='R') THEN
		  RETURN NEW;
		END IF;
	 END LOOP;
	 UPDATE yvs_grh_element_additionel SET statut='R', author=NEW.author WHERE id=NEW.retenue;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_salaire_change_statut_retenue()
  OWNER TO postgres;

  CREATE TRIGGER grh_update_etat_element_retenue
  AFTER UPDATE
  ON yvs_grh_detail_prelevement_emps
  FOR EACH ROW
  EXECUTE PROCEDURE grh_salaire_change_statut_retenue();

  
  CREATE OR REPLACE FUNCTION grh_salaire_insert_new_composant_retenue()
  RETURNS trigger AS
$BODY$    
DECLARE
	type_ret_ RECORD;
	action_ character varying;
	montant_ double precision default 0;
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT') THEN 
		IF(NEW.actif) THEN 		
			UPDATE yvs_grh_element_additionel SET montant_element=montant_element + NEW.montant WHERE id=NEW.retenue;
		ELSE
			--montant_=(montant_element-OLD.montant )-NEW.montant;
			--UPDATE yvs_grh_element_additionel SET montant_element= (montant_element)-NEW.montant WHERE id=NEW.retenue;
		END IF;
	END IF;
	IF(action_='UPDATE') THEN 
		IF(NEW.actif) THEN 
			-- insert la ligne des composants		
			UPDATE yvs_grh_element_additionel SET montant_element=(montant_element-OLD.montant )+NEW.montant WHERE id=NEW.retenue;
		ELSE
			UPDATE yvs_grh_element_additionel SET montant_element= (montant_element-OLD.montant )-NEW.montant WHERE id=NEW.retenue;
		END IF;
	END IF;
	IF(action_='DELETE' OR action_='TRONCATE') THEN 		
			UPDATE yvs_grh_element_additionel SET montant_element= montant_element-OLD.montant WHERE id=OLD.retenue;
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_salaire_insert_new_composant_retenue()
  OWNER TO postgres;
  
  CREATE TRIGGER insert_update_composant_ret
  AFTER INSERT OR UPDATE
  ON yvs_grh_composants_retenue
  FOR EACH ROW
  EXECUTE PROCEDURE grh_salaire_insert_new_composant_retenue();
  
  
  
  
  
  
  
  ALTER TABLE yvs_parametre_grh ADD COLUMN delais_validation_pointage integer;
COMMENT ON COLUMN yvs_parametre_grh.delais_validation_pointage IS 'Marge en jour de validation d''une fiche de présence';
ALTER TABLE yvs_parametre_grh ADD COLUMN delais_sasie_pointage integer;
COMMENT ON COLUMN yvs_parametre_grh.delais_sasie_pointage IS 'marge en jour de saisie manuel d''un pointage';

-- Column: quotite_cessible

-- ALTER TABLE yvs_parametre_grh DROP COLUMN quotite_cessible;

ALTER TABLE yvs_parametre_grh ADD COLUMN quotite_cessible double precision;
ALTER TABLE yvs_parametre_grh ALTER COLUMN quotite_cessible SET DEFAULT 100;


-- Table: yvs_grh_contrat_restreint

-- DROP TABLE yvs_grh_contrat_restreint;

CREATE TABLE yvs_grh_contrat_restreint
(
  id bigserial NOT NULL,
  contrat bigint,
  niveaux bigint,
  author bigint,
  CONSTRAINT yvs_grh_contrat_restreint_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_contrat_restreint_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_contrat_restreint_contrat_fkey FOREIGN KEY (contrat)
      REFERENCES yvs_grh_contrat_emps (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_contrat_restreint_niveaux_fkey FOREIGN KEY (niveaux)
      REFERENCES yvs_niveau_acces (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_contrat_restreint
  OWNER TO postgres;
  
  
  
 ALTER TABLE yvs_grh_contrat_emps ADD COLUMN source_first_conge character varying;
COMMENT ON COLUMN yvs_grh_contrat_emps.source_first_conge IS 'indique la source du paramètre date lors de l''evaluation du premier congé annuel DE=Date d''enbauche DEX=date début exercice DF=date fixe indiqué dans le contrat';

ALTER TABLE yvs_grh_contrat_emps ADD COLUMN date_first_conge date;
ALTER TABLE yvs_grh_employes DROP COLUMN nb_jour_conge;
DROP FUNCTION conge_jour_pris_annuel(bigint, date, date);

