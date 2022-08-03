-- Function: insertion_avancement(bigint)

-- DROP FUNCTION insertion_avancement(bigint);

CREATE OR REPLACE FUNCTION insertion_avancement(id_ bigint)
  RETURNS bigint AS
$BODY$
DECLARE 
    result BIGINT default 0;
    max_e INTEGER; 
	max_c INTEGER;
	employe_ RECORD;
	infos_ RECORD;
	code_c INTEGER default 0;
	code_e INTEGER default 0;
	code_ RECORD;
	total_horaire_min_ double precision default 0;
	total_min_ double precision default 0;
	date_ date default '01-01-1990';
	periode_ double precision default 0;
    
BEGIN
	select into max_c degre from yvs_grh_categorie_professionelle order by degre desc limit 1;
	select into max_e degre from yvs_grh_echelons order by degre desc limit 1;
	
	select into employe_ e.id, a.societe, a.secteur_activite as secteur from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id inner join yvs_societes s on a.societe = s.id  where e.id = id_;
	
	select into infos_ ce.id, cc.salaire_horaire_min, cc.salaire_min, ce.date_change, ce.convention, ca.degre as degre_c, e.degre as degre_e, cc.categorie, cc.echellon from yvs_grh_convention_employe ce 
		inner join yvs_grh_convention_collective cc on ce.convention = cc.id inner join yvs_grh_categorie_professionelle ca on cc.categorie = ca.id 
		inner join yvs_grh_echelons e on cc.echellon = e.id where employe = employe_.id  and ce.actif = true;
	
	if (infos_.degre_e < max_e) then
		code_c = infos_.categorie;
		select into code_e id from yvs_grh_echelons where degre = infos_.degre_e + 1 and societe = employe_.societe order by degre asc limit 1;
	end if;
	
	select into code_ id, salaire_horaire_min, salaire_min from yvs_grh_convention_collective where categorie = code_c and echellon = code_e and secteur = employe_.secteur;
	total_horaire_min_ = (code_.salaire_horaire_min - infos_.salaire_horaire_min);
	total_min_ = code_.salaire_min - infos_.salaire_min;
	
	if (code_.id is not null) then
		select into infos_ p.periode_davancement, p.periode_premier_avancement from yvs_parametre_grh p inner join yvs_agences a on p.societe = a.societe
			inner join yvs_grh_employes e on e.agence = a.id where e.id = employe_.id;
		periode_ = infos_.periode_davancement;
		date_ = to_date((((select extract(day from current_date)) || '-' || (select extract(month from current_date)) ||'-'|| (select extract(year from current_date)) + periode_)),'DD MM YYYY');
	
		update yvs_grh_convention_employe set actif = false where employe = employe_.id;
		insert into yvs_grh_convention_employe(employe, convention, date_change, actif) values (employe_.id, code_.id, current_date, true);
		update yvs_grh_contrat_emps set salaire_horaire = (salaire_horaire + total_horaire_min_), salaire_mensuel = (salaire_mensuel + total_min_) where employe = employe_.id;
		update yvs_grh_employes set date_prochain_avancement = date_ where id = employe_.id;

		select into result id from yvs_grh_convention_employe where employe = employe_.id and convention = code_.id and actif = true;
	end if;
    return result;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insertion_avancement(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION insertion_avancement(bigint) IS 'fonction modifie la convention collective d''un employe et retourne l''id de la nouvelle convention de l''employe
elle prend en parametre l''id de l''employe
';
