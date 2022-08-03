-- Function: travail_jour_dimanche(bigint, date, date)

-- DROP FUNCTION travail_jour_dimanche(bigint, date, date);

CREATE OR REPLACE FUNCTION travail_jour_dimanche(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0; 
	infos_ RECORD;
	employe_ RECORD;
	presence_ RECORD;
	jour_ INTEGER;
	date_ DATE;
	
BEGIN
    -- recherche des informations de l'employe
	select into employe_ e.id, a.societe, e.horaire_dynamique from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id 
                where e.id = id_;
        date_ = date_debut_;
	while (date_ <= date_fin_)
	loop	
		jour_ = (select extract(DOW from date_));		
		-- test si le jour present est un dimanche
		if (jour_ = 0) then
			select into presence_ id from yvs_grh_presence where employe = id_ and
				date_debut <= date_ and date_fin >= date_ and valider = true;
			if(presence_.id is not null) then
				total_ = total_ + (select heure_travail_effectif_jour_unique(presence_.id, date_, true));
			end if;
		end if;
		date_ = date_ + interval '1 day';
	end loop;
	return total_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION travail_jour_dimanche(bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION travail_jour_dimanche(bigint, date, date) IS 'fonction qui retourne la duree d''heure de travail des dimanche d''un employe au mois precedent
elle prend en parametre : l''id de l''employe, la date du mois de la recherche, un boolean qui precise si l''on prend en compte les heures de compensation';
