-- Function: heure_travail_requis(bigint, date, date)

-- DROP FUNCTION heure_travail_requis(bigint, date, date);

CREATE OR REPLACE FUNCTION heure_travail_requis(id_ bigint, date_prec_ date, date_suiv_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	employe_ RECORD;
	jour_ DATE;
	total_ DOUBLE PRECISION default 0;
	
BEGIN
	-- recherche des informations sur un employe
	--select into employe_ e.id, c.regle_tache, c.calendrier, a.societe, c.id from yvs_employes e inner join yvs_agences a on e.agence = a.id 
	--	inner join yvs_contrat_emps c on c.employe = e.id where e.id = id_ limit 1;
	--if(employe_.id is null) then
	--	total_ = 0;
	--else
		jour_ = date_prec_;
		while (jour_ <= date_suiv_) loop
			total_ = total_ + (select heure_travail_requis_jour(id_, jour_));
			jour_ = jour_ + interval '1day';
		end loop;
		if (total_ is null) then
			total_ = 0;
		end if;
	--end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION heure_travail_requis(bigint, date, date)
  OWNER TO postgres;
