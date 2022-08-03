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
	pause_ DOUBLE PRECISION default 0;
	total_ DOUBLE PRECISION default 0;
	requis_ DOUBLE PRECISION default 0;
	p_ RECORD;
    
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
	
	-- Traitement de la fiche de presence
	select into p_ * from yvs_grh_presence where id = OLD.presence;	
	if(p_.duree_pause is not null)then
		pause_ = ((select extract(hour from p_.duree_pause)) + ((select extract(minute from p_.duree_pause))/60));
	end if;
	if(pause_ is null)then
		pause_ = 0;
	end if;	
	HS_ = ((select extract(hour from p_.heure_debut)) + ((select extract(minute from p_.heure_debut))/60));
	HE_ = ((select extract(hour from p_.heure_fin)) + ((select extract(minute from p_.heure_fin))/60));
	requis_ = HS_ - HE_;
	if(requis_ is null)then
		requis_ = 0;
	end if;
	if(requis_ < 0)then
		requis_ = requis_ + 24;
	end if;
	if(requis_ >= pause_)then
		requis_ = requis_ - pause_;
	end if;

	-- Verification si l'on modifie 'heure_sortie' ou 'heure_entree'
	if(NEW.heure_sortie != OLD.heure_sortie or NEW.heure_entree != OLD.heure_entree)then -- Si c'est la cas
		-- Verification si l'on modifie 'valider' ou 'actif'
		if((OLD.valider != NEW.valider or OLD.actif != NEW.actif))then -- Si c'est le cas
			-- Verification si les nouvelles valeurs de valider et actif sont a 'true'
			if(NEW.valider = true and NEW.actif = true)then -- Si c'est le cas on ajout l'heure
				if((NEW.compensation_heure is null OR NEW.compensation_heure = false) and (NEW.heure_supplementaire is null OR NEW.heure_supplementaire = false)) then
					total_ = p_.total_presence + NEW_somme_ - OLD_somme_;
					if(total_ > requis_)then
						total_ = requis_;
					end if;	
					update yvs_grh_presence set total_presence = total_ where id = OLD.presence and valider = false;
				else
					if(NEW.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence and valider = false;
					end if;
					if(NEW.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence and valider = false;
					end if;
				end if;
			else -- Si ce n'est pas le cas on retirer l'heure
				if(NEW.compensation_heure = false and NEW.heure_supplementaire = false) then
					total_ = p_.total_presence - OLD_somme_;
					if(total_ > requis_)then
						total_ = requis_;
					end if;	
					update yvs_grh_presence set total_presence = total_ where id = OLD.presence and valider = false; 
				else
					if(NEW.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup - NEW_somme_ where id = OLD.presence and valider = false;
					end if;
					if(NEW.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation - NEW_somme_ where id = OLD.presence and valider = false;
					end if;
				end if;
			end if;
		else -- Si ce n'est pas le cas
			-- Verification si les anciennes valeurs de valider et actif sont a 'true'
			if(OLD.valider = true and OLD.actif)then -- si c'est le cas on ajout l'heure
				if((NEW.compensation_heure is null OR NEW.compensation_heure = false) and (NEW.heure_supplementaire is null OR NEW.heure_supplementaire = false)) then
					total_ = p_.total_presence + NEW_somme_ - OLD_somme_;
					if(total_ > requis_)then
						total_ = requis_;
					end if;	
					update yvs_grh_presence set total_presence = total_ where id = OLD.presence and valider = false;
				else
					if(OLD.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence and valider = false;
					end if;
					if(OLD.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence and valider = false;
					end if;
				end if;
			end if;
		end if;
	else -- si ce n'est pas le cas
		-- Verification si l'on modifie 'valider' ou 'actif'
		if((OLD.valider != NEW.valider or OLD.actif != NEW.actif))then -- Si c'est le cas
			-- Verification si les nouvelles valeurs de valider et actif sont a 'true'
			if(NEW.valider = true and NEW.actif = true)then -- Si c'est le cas on ajout l'heure
				if((NEW.compensation_heure is null OR NEW.compensation_heure = false) and (NEW.heure_supplementaire is null OR NEW.heure_supplementaire = false)) then
					total_ = p_.total_presence + NEW_somme_ - OLD_somme_;
					if(total_ > requis_)then
						total_ = requis_;
					end if;	
					update yvs_grh_presence set total_presence = total_ where id = OLD.presence and valider = false;
				else
					if(NEW.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence and valider = false;
					end if;
					if(NEW.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence and valider = false;
					end if;
				end if;
			else -- Si ce n'est pas le cas on retirer l'heure
				if(NEW.compensation_heure = false and NEW.heure_supplementaire = false) then
					total_ = p_.total_presence - OLD_somme_;
					if(total_ > requis_)then
						total_ = requis_;
					end if;	
					update yvs_grh_presence set total_presence = total_ where id = OLD.presence and valider = false; 
				else
					if(NEW.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup - NEW_somme_ where id = OLD.presence and valider = false;
					end if;
					if(NEW.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation - NEW_somme_ where id = OLD.presence and valider = false;
					end if;
				end if;
			end if;
		else -- Si ce n'est pas le cas
			-- Verification si les anciennes valeurs de valider et actif sont a 'true'
			if(OLD.valider = true and OLD.actif)then
				if((NEW.compensation_heure is null OR NEW.compensation_heure = false) and (NEW.heure_supplementaire is null OR NEW.heure_supplementaire = false)) then
					total_ = p_.total_presence + NEW_somme_ - OLD_somme_;
					if(total_ > requis_)then
						total_ = requis_;
					end if;	
					update yvs_grh_presence set total_presence = total_ where id = OLD.presence and valider = false;
				else
					if(NEW.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence and valider = false;
					end if;
					if(NEW.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence and valider = false;
					end if;
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

  
  
  -- Function: insert_pointage()

-- DROP FUNCTION insert_pointage();

CREATE OR REPLACE FUNCTION insert_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE 
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	somme_ DOUBLE PRECISION default 0;
	pause_ DOUBLE PRECISION default 0;
	total_ DOUBLE PRECISION default 0;
	requis_ DOUBLE PRECISION default 0;
	p_ RECORD;
    
BEGIN
	if(NEW.valider = true and NEW.actif = true and NEW.heure_entree is not null and NEW.heure_sortie is not null)then
	
		-- Traitement de la fiche de presence
		select into p_ * from yvs_grh_presence where id = NEW.presence;	
		if(p_.duree_pause is not null)then
			pause_ = ((select extract(hour from p_.duree_pause)) + ((select extract(minute from p_.duree_pause))/60));
		end if;
		if(pause_ is null)then
			pause_ = 0;
		end if;	
		HS_ = ((select extract(hour from p_.heure_debut)) + ((select extract(minute from p_.heure_debut))/60));
		HE_ = ((select extract(hour from p_.heure_fin)) + ((select extract(minute from p_.heure_fin))/60));
		requis_ = HS_ - HE_;
		if(requis_ is null)then
			requis_ = 0;
		end if;
		if(requis_ < 0)then
			requis_ = requis_ + 24;
		end if;
		if(requis_ >= pause_)then
			requis_ = requis_ - pause_;
		end if;
	
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
		if((NEW.compensation_heure is null OR NEW.compensation_heure = false) and (NEW.heure_supplementaire is null OR NEW.heure_supplementaire = false)) then
			total_ = p_.total_presence + somme_;
			if(total_ > requis_)then
				total_ = requis_;
			end if;	
			update yvs_grh_presence set total_presence = total_ where id = NEW.presence;
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
