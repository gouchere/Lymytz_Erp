-- Function: com_action_on_creneau_horaire()
-- DROP FUNCTION com_action_on_creneau_horaire();
CREATE OR REPLACE FUNCTION com_action_on_creneau_horaire()
  RETURNS trigger AS
$BODY$    
DECLARE
	data_ record;
	
	id_ bigint;
	author_ bigint;
	employe_ bigint;
	tranche_ bigint;
	supp_ boolean default false;
	actif_ boolean default true;
	date_debut_ date;
	date_fin_ date;
	duree_pause_ time without time zone;
	date_update_ timestamp without time zone default current_timestamp;
	date_save_ timestamp without time zone default current_timestamp;
	
	action_ character varying;	
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
	IF((action_ = 'INSERT' OR action_ = 'UPDATE') AND NEW.permanent IS FALSE) THEN		
		author_ = NEW.author;		
		date_update_ = NEW.date_update;		
		date_save_ = NEW.date_save;		
		actif_ = NEW.actif;	
		date_debut_ = NEW.date_travail;
		date_fin_ = NEW.date_travail;
		SELECT INTO employe_ y.id FROM yvs_grh_employes y WHERE y.code_users = NEW.users LIMIT 1;
		IF(COALESCE(employe_, 0) > 0)THEN
			SELECT INTO tranche_ y.tranche FROM yvs_com_creneau_depot y WHERE y.id = NEW.creneau_depot; 
			IF(COALESCE(tranche_, 0) < 1)THEN
				SELECT INTO tranche_ y.tranche FROM yvs_com_creneau_point y WHERE y.id = NEW.creneau_point; 
			END IF;
			IF(COALESCE(tranche_, 0) > 0)THEN
				SELECT INTO data_ (y.heure_fin < y.heure_debut) as incremente, y.duree_pause FROM yvs_grh_tranche_horaire y WHERE y.id = tranche_;
				duree_pause_ = data_.duree_pause;
				IF(data_.incremente)THEN
					date_fin_ = date_fin_ + interval '1 day';
				END IF;
				SELECT INTO id_ y.id FROM yvs_grh_planning_employe y WHERE y.employe = employe_ AND y.tranche = tranche_ AND y.date_debut = date_debut_;
				IF(COALESCE(id_, 0)<1)THEN
					INSERT INTO yvs_grh_planning_employe(employe, supp, actif, date_debut, date_fin, author, duree_pause, tranche, date_update, date_save)
						VALUES (employe_, supp_, actif_, date_debut_, date_fin_, author_, duree_pause_, tranche_, date_update_, date_save_);
				ELSE
					UPDATE yvs_grh_planning_employe SET employe = employe_, supp = supp_, actif = actif_, date_debut = date_debut_, date_fin = date_fin_
						, author = author_, duree_pause = duree_pause_, tranche = tranche_, date_update = date_update_ WHERE id = id_;
				END IF;
			END IF;
		END IF;
	END IF;
	
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		RETURN NEW;
	ELSE
		RETURN OLD;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_action_on_creneau_horaire()
  OWNER TO postgres;
