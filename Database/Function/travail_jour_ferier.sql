-- Function: travail_jour_ferier(bigint, date, date)

-- DROP FUNCTION travail_jour_ferier(bigint, date, date);

CREATE OR REPLACE FUNCTION travail_jour_ferier(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0; 
	employe_ RECORD;
	presence_ RECORD;
	ferier_ RECORD;
	
BEGIN
	-- recherche des informations sur un employe
	select into employe_ e.id, a.societe, c.salaire_horaire, p.date_paiement_salaire from yvs_grh_employes e 
		inner join yvs_agences a on e.agence = a.id inner join yvs_societes s on a.societe = s.id 
		inner join yvs_parametre_grh p on p.societe = s.id inner join yvs_grh_contrat_emps c on c.employe = e.id
		where e.id = id_ limit 1;

	for ferier_ in select jour from yvs_jours_feries where (jour >= date_debut_ and jour < date_fin_) and societe = employe_.societe
	loop
		select into presence_ id from yvs_grh_presence where employe = id_ and
			date_debut <= ferier_.jour and date_fin >= ferier_.jour and valider = true;
		if(presence_.id is not null) then
			total_ = total_ + (select heure_travail_effectif_jour_unique(presence_.id, ferier_.jour, true));
		end if;
	end loop;
	if(total_ is null) then
		total_ = 0;
	end if;
	return total_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION travail_jour_ferier(bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION travail_jour_ferier(bigint, date, date) IS 'fonction qui compte le nombre d''heure travaillé durant les jours férié compris entre deux dates';
