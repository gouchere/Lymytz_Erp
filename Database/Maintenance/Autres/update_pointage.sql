-- Function: update_pointage()

-- DROP FUNCTION update_pointage();

CREATE OR REPLACE FUNCTION update_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	NEW_somme_ DOUBLE PRECISION default 0;
	OLD_somme_ DOUBLE PRECISION default 0;
    
BEGIN
	-- controle sur l'action et les valeurs inserees
	if (NEW.valider = true and NEW.heure_sortie is not null and NEW.heure_entree is not null) then
		HS_ = ((select extract(hour from NEW.heure_sortie)) + ((select extract(minute from NEW.heure_sortie))/60));
		HE_ = ((select extract(hour from NEW.heure_entree)) + ((select extract(minute from NEW.heure_entree))/60));
		NEW_somme_ = HS_ - HE_;
		if(NEW_somme_ < 0)then
			NEW_somme_ = NEW_somme_ + 24;
		end if;
		if(OLD.heure_sortie is not null and OLD.valider = true and OLD.actif = true) then
			HS_ = ((select extract(hour from OLD.heure_sortie)) + ((select extract(minute from OLD.heure_sortie))/60));
			HE_ = ((select extract(hour from OLD.heure_entree)) + ((select extract(minute from OLD.heure_entree))/60));
			OLD_somme_ = HS_ - HE_;
			if(OLD_somme_ < 0)then
				OLD_somme_ = OLD_somme_ + 24;
			end if;
		else
			OLD_somme_ = 0;
		end if;
	elsif (OLD.valider = true and OLD.heure_sortie is not null and OLD.heure_entree is not null) then			
		NEW_somme_ = 0;
		HS_ = ((select extract(hour from OLD.heure_sortie)) + ((select extract(minute from OLD.heure_sortie))/60));
		HE_ = ((select extract(hour from OLD.heure_entree)) + ((select extract(minute from OLD.heure_entree))/60));
		OLD_somme_ = HS_ - HE_;	
		if(OLD_somme_ < 0)then
			OLD_somme_ = OLD_somme_ + 24;
		end if;
	else
		NEW_somme_ = 0;
		OLD_somme_ = 0;
	end if;
	update yvs_grh_presence set total_presence = total_presence + NEW_somme_ - OLD_somme_ where id = NEW.presence; 
	if (NEW.valider = true and NEW.heure_sortie is not null and NEW.heure_entree is not null and NEW.heure_supplementaire IS true) THEN
		--traitement des heures supplémentaires
		HS_ = ((select extract(hour from NEW.heure_sortie)) + ((select extract(minute from NEW.heure_sortie))/60));
		HE_ = ((select extract(hour from NEW.heure_entree)) + ((select extract(minute from NEW.heure_entree))/60));
		NEW_somme_ = HS_ - HE_;
		if(NEW_somme_ < 0)then
			NEW_somme_ = NEW_somme_ + 24;
		end if;
		if(OLD.heure_sortie is not null and OLD.valider = true and OLD.actif = true AND OLD.heure_supplementaire IS true) then
			HS_ = ((select extract(hour from OLD.heure_sortie)) + ((select extract(minute from OLD.heure_sortie))/60));
			HE_ = ((select extract(hour from OLD.heure_entree)) + ((select extract(minute from OLD.heure_entree))/60));
			OLD_somme_ = HS_ - HE_;
			if(OLD_somme_ < 0)then
				OLD_somme_ = OLD_somme_ + 24;
			end if;
		else
			OLD_somme_ = 0;
		end if;
		--insère dans les heures suppléméntaire
		update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = NEW.presence; 
	END IF;
	if (NEW.valider = true and NEW.heure_sortie is not null and NEW.heure_entree is not null and NEW.compensation_heure IS true) THEN
		-- traitement des heures de compensations
		HS_ = ((select extract(hour from NEW.heure_sortie)) + ((select extract(minute from NEW.heure_sortie))/60));
		HE_ = ((select extract(hour from NEW.heure_entree)) + ((select extract(minute from NEW.heure_entree))/60));
		NEW_somme_ = HS_ - HE_;
		if(NEW_somme_ < 0)then
			NEW_somme_ = NEW_somme_ + 24;
		end if;
		if(OLD.heure_sortie is not null and OLD.valider = true and OLD.actif = true AND OLD.compensation_heure IS true) then
			HS_ = ((select extract(hour from OLD.heure_sortie)) + ((select extract(minute from OLD.heure_sortie))/60));
			HE_ = ((select extract(hour from OLD.heure_entree)) + ((select extract(minute from OLD.heure_entree))/60));
			OLD_somme_ = HS_ - HE_;
			if(OLD_somme_ < 0)then
				OLD_somme_ = OLD_somme_ + 24;
			end if;
		else
			OLD_somme_ = 0;
		end if;
		--insère dans les heures de compensation
		update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = NEW.presence; 
	END IF;
	--recuperation de l'heure de pointage
	if (NEW.heure_sortie is null) then
		NEW.heure_pointage = NEW.heure_entree;
	else
		NEW.heure_pointage = NEW.heure_sortie;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_pointage()
  OWNER TO postgres;
