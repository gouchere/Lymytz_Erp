-- Function: periode_fr_en(character varying)

-- DROP FUNCTION periode_fr_en(character varying);

CREATE OR REPLACE FUNCTION periode_fr_en(mois character varying)
  RETURNS character varying AS
$BODY$
	 
BEGIN
	if(mois is not null)then
		if(mois = 'J' or mois = 'j' or mois = 'J(s)' or mois = 'j(s)' or mois = 'Js' or mois = 'js' or mois = 'Jr' or mois = 'jr' or mois = 'Jr(s)' or mois = 'jr(s)'  or mois = 'Jrs' or mois = 'jrs' or mois = 'Jour' or mois = 'jour' or mois = 'Jour(s)' or mois = 'jour(s)' or mois = 'Jours' or mois = 'jours')then
			return 'day';
		elsif(mois = 'W' or mois = 'w' or mois = 'W(s)' or mois = 'w(s)' or mois = 'Ws' or mois = 'ws' or mois = 'Sem' or mois = 'sem' or mois = 'Sem(s)' or mois = 'sem(s)' or mois = 'Sems' or mois = 'sems' or mois = 'Semaine' or mois = 'semaine' or mois = 'Semaine(s)' or mois = 'semaine(s)' or mois = 'Semaines' or mois = 'semaines') then
			return 'week';
		elsif(mois = 'M' or mois = 'm' or mois = 'M(s)' or mois = 'm(s)' or mois = 'Ms' or mois = 'ms' or mois = 'Mois' or mois = 'mois' or mois = 'Men(s)' or mois = 'men(s)' or mois = 'Mens' or mois = 'mens' or mois = 'Mensuel' or mois = 'mensuel' or mois = 'Mensuel(s)' or mois = 'mensuel(s)' or mois = 'Mensuels' or mois = 'mensuels') then
			return 'month';
		elsif(mois = 'A' or mois = 'a' or mois = 'A(s)' or mois = 'a(s)' or mois = 'As' or mois = 'as' or mois = 'An' or mois = 'an' or mois = 'Ans' or mois = 'ans' or mois = 'An(s)' or mois = 'an(s)' or mois = 'Annee' or mois = 'annee' or mois = 'Annee(s)' or mois = 'annee(s)' or mois = 'Annees' or mois = 'annees' or mois = 'Année' or mois = 'année' or mois = 'Année(s)' or mois = 'année(s)' or mois = 'Années' or mois = 'années') then
			return 'year';
		end if;
		return mois;
	end if;
	return 'day';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION periode_fr_en(character varying)
  OWNER TO postgres;
