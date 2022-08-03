-- Function: update_()

-- DROP FUNCTION update_();

CREATE OR REPLACE FUNCTION update_()
  RETURNS boolean AS
$BODY$   
DECLARE 
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	somme_ DOUBLE PRECISION default 0;
	pause_ DOUBLE PRECISION default 0;
	marge_ DOUBLE PRECISION default 0;
	requis_ DOUBLE PRECISION default 0;
	p_ RECORD;
	o_ RECORD;
BEGIN
	for o_ in select * from yvs_grh_presence
	loop
		if(o_.marge_approuve is not null)then
			marge_ = ((select extract(hour from o_.marge_approuve)) + ((select extract(minute from o_.marge_approuve))/60));
		end if;
		if(marge_ is null)then
			marge_ = 0;
		end if;
		
		if(o_.duree_pause is not null)then
			pause_ = ((select extract(hour from o_.duree_pause)) + ((select extract(minute from o_.duree_pause))/60));
		end if;
		if(pause_ is null)then
			pause_ = 0;
		end if;
		
		HS_ = ((select extract(hour from o_.heure_debut)) + ((select extract(minute from o_.heure_debut))/60));
		HE_ = ((select extract(hour from o_.heure_fin)) + ((select extract(minute from o_.heure_fin))/60));
		requis_ = HS_ - HE_;
		if(requis_ is null)then
			requis_ = 0;
		end if;
		if(requis_ < 0)then
			requis_ = requis_ + 24;
		end if;
		requis_ = requis_ - pause_;
		
		update yvs_grh_presence set total_presence = 0 where id = o_.id;
		for p_ in select * from yvs_grh_pointage where presence = o_.id and valider = true and actif = true and heure_entree is not null and heure_sortie is not null
		loop
			-- recuperation de l'interval heure fin - heure debut
			HS_ = ((select extract(hour from p_.heure_sortie)) + ((select extract(minute from p_.heure_sortie))/60));
			HE_ = ((select extract(hour from p_.heure_entree)) + ((select extract(minute from p_.heure_entree))/60));
			somme_ = HS_ - HE_;
			if(somme_ is null)then
				somme_ = 0;
			end if;
			if(somme_ < 0)then
				somme_ = somme_ + 24;
			end if;
			
			somme_ = somme_ + marge_;
			if(somme_ > requis_)then
				somme_ = requis_;
			end if;
			
			if((p_.compensation_heure is null OR p_.compensation_heure = false) and (p_.heure_supplementaire is null OR p_.heure_supplementaire = false)) then	
				update yvs_grh_presence set total_presence = total_presence + somme_ where id = o_.id;
			else
				if(p_.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup + somme_ where id = o_.id;
				end if;
				if(p_.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation + somme_ where id = o_.id;
				end if;	
			end if;		
		end loop;
	end loop;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_()
  OWNER TO postgres;

  
  -- Function: delete_pointage()

-- DROP FUNCTION delete_pointage();

CREATE OR REPLACE FUNCTION delete_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE 
	HS_ DOUBLE PRECISION default 0;
	HE_ DOUBLE PRECISION default 0;
	OLD_somme_ DOUBLE PRECISION default 0;
    
BEGIN
	if (OLD.valider = true and OLD.actif = true and OLD.heure_sortie is not null) then
		HS_ = ((select extract(hour from OLD.heure_sortie)) + ((select extract(minute from OLD.heure_sortie))/60));
		if(HS_ is null)then
			HS_ = 0;
		end if;
		if(OLD.heure_entree is not null)then
			HE_ = ((select extract(hour from OLD.heure_entree)) + ((select extract(minute from OLD.heure_entree))/60));
		end if;
		if(HE_ is null)then
			HE_ = 0;
		end if;
		OLD_somme_ = HS_ - HE_;
		if(OLD_somme_<0)then
			OLD_somme_ = OLD_somme_ + 24;
		end if;
		if((NEW.compensation_heure is null OR NEW.compensation_heure = false) and (NEW.heure_supplementaire is null OR NEW.heure_supplementaire = false))then
			update yvs_grh_presence set total_presence = total_presence - OLD_somme_ where id = OLD.presence;
		else
			IF(OLD.heure_supplementaire = true) THEN
				update yvs_grh_presence set total_heure_sup = total_heure_sup - OLD_somme_ where id = OLD.presence;
			END IF;
			IF(OLD.compensation_heure = true) THEN
				update yvs_grh_presence set total_heure_compensation = total_heure_compensation - OLD_somme_ where id = OLD.presence;
			END IF;
		end if;
	end if;
	return OLD ;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_pointage()
  OWNER TO postgres;

  
  -- Function: insert_pointage()

-- DROP FUNCTION insert_pointage();

CREATE OR REPLACE FUNCTION insert_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE 
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	somme_ DOUBLE PRECISION default 0;
    
BEGIN
	if(NEW.valider = true and NEW.actif = true and NEW.heure_entree is not null and NEW.heure_sortie is not null)then
		-- recuperation de l'interval heure fin - heure debut
		HS_ = ((select extract(hour from NEW.heure_sortie)) + ((select extract(minute from NEW.heure_sortie))/60));
		HE_ = ((select extract(hour from NEW.heure_entree)) + ((select extract(minute from NEW.heure_entree))/60));
		somme_ = HS_ - HE_;
		if(somme_ is null)then
			somme_ = 0;
		end if;
		if(somme_ < 0)then
			somme_ = somme_ + 24;
		end if;
		if((NEW.compensation_heure is null OR NEW.compensation_heure = false) and (NEW.heure_supplementaire is null OR NEW.heure_supplementaire = false))then
			update yvs_grh_presence set total_presence = total_presence + somme_ where id = NEW.presence;
		else
			if(NEW.heure_supplementaire = true) then
				update yvs_grh_presence set total_heure_sup = total_heure_sup + somme_ where id = NEW.presence;
			end if;
			if(NEW.compensation_heure = true) then
				update yvs_grh_presence set total_heure_compensation = total_heure_compensation + somme_ where id = NEW.presence;
			end if;	
		end if;		
	end if;
	if (NEW.heure_sortie is null) then
		NEW.heure_pointage = NEW.heure_entree;
	else
		NEW.heure_pointage = NEW.heure_sortie;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_pointage()
  OWNER TO postgres;

  
  
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
	-- recuperation de l'ancien interval heure fin - heure debut
	if(OLD.heure_sortie is not null and OLD.heure_entree is not null)then
		HS_ = ((select extract(hour from OLD.heure_sortie)) + ((select extract(minute from OLD.heure_sortie))/60));
		HE_ = ((select extract(hour from OLD.heure_entree)) + ((select extract(minute from OLD.heure_entree))/60));
		OLD_somme_ = HS_ - HE_;
		if(OLD_somme_ < 0)then
			OLD_somme_ = OLD_somme_ + 24;
		end if;
	end if;
	if(OLD_somme_ is null)then
		OLD_somme_ = 0;
	end if;
	
	-- recuperation du nouveau interval heure fin - heure debut
	if(NEW.heure_sortie is not null and NEW.heure_entree is not null)then
		HS_ = ((select extract(hour from NEW.heure_sortie)) + ((select extract(minute from NEW.heure_sortie))/60));
		HE_ = ((select extract(hour from NEW.heure_entree)) + ((select extract(minute from NEW.heure_entree))/60));
		NEW_somme_ = HS_ - HE_;
		if(NEW_somme_ < 0)then
			NEW_somme_ = NEW_somme_ + 24;
		end if;
	end if;
	if(NEW_somme_ is null)then
		NEW_somme_ = 0;
	end if;

	-- Verification si l'on modifie 'valider' ou 'actif'
	if(OLD.valider != NEW.valider or OLD.actif != NEW.actif)then -- Si c'est le cas
		-- Verification si valider and actif sont a 'true'
		if(NEW.valider = true and NEW.actif = true)then -- Si c'est le cas on ajout l'heure
			if((OLD.compensation_heure is null OR OLD.compensation_heure = false) and (OLD.heure_supplementaire is null OR OLD.heure_supplementaire = false)) then
				update yvs_grh_presence set total_presence = total_presence + NEW_somme_ - OLD_somme_ where id = OLD.presence;
			else
				if(OLD.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				end if;
				if(OLD.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				end if;
			end if;
		else -- Si ce n'est pas le cas on retirer l'heure
			if(OLD.compensation_heure = false and OLD.heure_supplementaire = false) then
				update yvs_grh_presence set total_presence = total_presence - NEW_somme_ where id = OLD.presence; 
			else
				if(OLD.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup - NEW_somme_ where id = OLD.presence;
				end if;
				if(OLD.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation - NEW_somme_ where id = OLD.presence;
				end if;
			end if;
		end if;
	else -- S ce n'est pas le cas
		-- Verification si l'on modifie 'heure_sortie' ou 'heure_entree'
		if((OLD.heure_sortie != NEW.heure_sortie or OLD.heure_entree != NEW.heure_entree) and (NEW.valider = true and NEW.actif = true))then -- Si c'est le cas
			if((OLD.compensation_heure is null OR OLD.compensation_heure = false) and (OLD.heure_supplementaire is null OR OLD.heure_supplementaire = false)) then
				update yvs_grh_presence set total_presence = total_presence + NEW_somme_ - OLD_somme_ where id = OLD.presence;
			else
				if(OLD.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				end if;
				if(OLD.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				end if;
			end if;
		end if;
	end if;
	
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

  
  
  -- Function: update_presence()

-- DROP FUNCTION update_presence();

CREATE OR REPLACE FUNCTION update_presence()
  RETURNS trigger AS
$BODY$   
    DECLARE
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	pause_ DOUBLE PRECISION default 0;
	somme_ DOUBLE PRECISION default 0;
	marge_ DOUBLE PRECISION default 0;
	requis_ DOUBLE PRECISION default 0;
	p_ RECORD;
    
BEGIN
	if(NEW.valider != OLD.valider and NEW.valider = true)then	
		if(NEW.marge_approuve is not null)then
			marge_ = ((select extract(hour from NEW.marge_approuve)) + ((select extract(minute from NEW.marge_approuve))/60));
		end if;
		if(marge_ is null)then
			marge_ = 0;
		end if;
		
		if(NEW.duree_pause is not null)then
			pause_ = ((select extract(hour from NEW.duree_pause)) + ((select extract(minute from NEW.duree_pause))/60));
		end if;
		if(pause_ is null)then
			pause_ = 0;
		end if;
		
		HS_ = ((select extract(hour from OLD.heure_debut)) + ((select extract(minute from OLD.heure_debut))/60));
		HE_ = ((select extract(hour from OLD.heure_fin)) + ((select extract(minute from OLD.heure_fin))/60));
		requis_ = HS_ - HE_;
		if(requis_ is null)then
			requis_ = 0;
		end if;
		if(requis_ < 0)then
			requis_ = requis_ + 24;
		end if;
		
		update yvs_grh_presence set total_presence = 0 where id = OLD.id;
		for p_ in select * from yvs_grh_pointage where presence = OLD.id and valider = true and actif = true and heure_entree is not null and heure_sortie is not null
		loop
			-- recuperation de l'interval heure fin - heure debut
			HS_ = ((select extract(hour from p_.heure_sortie)) + ((select extract(minute from p_.heure_sortie))/60));
			HE_ = ((select extract(hour from p_.heure_entree)) + ((select extract(minute from p_.heure_entree))/60));
			somme_ = HS_ - HE_;
			if(somme_ is null)then
				somme_ = 0;
			end if;
			if(somme_ < 0)then
				somme_ = somme_ + 24;
			end if;

			somme_ = somme_ + marge_;
			if(somme_ > requis_)then
				somme_ = requis_;
			end if;
			
			if((p_.compensation_heure is null OR p_.compensation_heure = false) and (p_.heure_supplementaire is null OR p_.heure_supplementaire = false)) then
				update yvs_grh_presence set total_presence = total_presence + somme_ where id = OLD.id;
			else
				if(p_.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup + somme_ where id = OLD.id;
				end if;
				if(p_.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation + somme_ where id = OLD.id;
				end if;	
			end if;		
		end loop;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_presence()
  OWNER TO postgres;
