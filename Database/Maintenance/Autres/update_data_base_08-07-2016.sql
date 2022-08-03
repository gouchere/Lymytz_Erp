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
