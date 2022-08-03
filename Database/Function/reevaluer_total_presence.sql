-- Function: reevaluer_total_presence(bigint)

-- DROP FUNCTION reevaluer_total_presence(bigint);

CREATE OR REPLACE FUNCTION reevaluer_total_presence(id_ bigint)
  RETURNS boolean AS
$BODY$   
DECLARE 
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	somme_ DOUBLE PRECISION default 0;
	pause_ DOUBLE PRECISION default 0;
	marge_ DOUBLE PRECISION default 0;
	requis_ DOUBLE PRECISION default 0;
	pointages_ RECORD;
	record_ RECORD;
BEGIN
	select into record_ * from yvs_grh_presence where id = id_;
	if(record_.valider = false)then
		if(record_.marge_approuve is not null)then
			marge_ = ((select extract(hour from record_.marge_approuve)) + ((select extract(minute from record_.marge_approuve))/60));
		end if;
		if(marge_ is null)then
			marge_ = 0;
		end if;
		
		if(record_.duree_pause is not null)then
			pause_ = ((select extract(hour from record_.duree_pause)) + ((select extract(minute from record_.duree_pause))/60));
		end if;
		if(pause_ is null)then
			pause_ = 0;
		end if;
		
		HS_ = ((select extract(hour from record_.heure_debut)) + ((select extract(minute from record_.heure_debut))/60));
		HE_ = ((select extract(hour from record_.heure_fin)) + ((select extract(minute from record_.heure_fin))/60));
		requis_ = HS_ - HE_;
		if(requis_ is null)then
			requis_ = 0;
		end if;
		if(requis_ < 0)then
			requis_ = requis_ + 24;
		end if;
		requis_ = requis_ - pause_;
		
		update yvs_grh_presence set total_presence = 0, total_heure_sup = 0, total_heure_compensation = 0 where id = record_.id;
		for pointages_ in select * from yvs_grh_pointage where presence = record_.id and valider = true and actif = true and heure_entree is not null and heure_sortie is not null
		loop
			-- recuperation de l'interval heure fin - heure debut
			HS_ = ((select extract(hour from pointages_.heure_sortie)) + ((select extract(minute from pointages_.heure_sortie))/60));
			HE_ = ((select extract(hour from pointages_.heure_entree)) + ((select extract(minute from pointages_.heure_entree))/60));
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
			
			if((pointages_.compensation_heure is null or pointages_.compensation_heure = false) and (pointages_.heure_supplementaire is null or pointages_.heure_supplementaire = false)) then			
				update yvs_grh_presence set total_presence = total_presence + somme_ where id = record_.id;
			else
				if(pointages_.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup + somme_ where id = record_.id;
				end if;
				if(pointages_.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation + somme_ where id = record_.id;
				end if;	
			end if;		
		end loop;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION reevaluer_total_presence(bigint)
  OWNER TO postgres;
