-- Function: cr_valide_fiche(bigint, bigint, bigint, date, date)

-- DROP FUNCTION cr_valide_fiche(bigint, bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION cr_valide_fiche(societe_ bigint, employe_ bigint, type_ bigint, date_debut_ date, date_fin_ date)
  RETURNS boolean AS
$BODY$   
DECLARE 
	em_ record;
	pe_ record;
	pa_ record;
BEGIN
	if(employe_ is not null and employe_ > 0)then
		update yvs_grh_presence set valider = true, type_validation = type_ where employe = employe_ and total_presence > 0 and date_debut >= date_debut_ and date_fin <= date_fin_;
	else
		for em_ in select e.id as id from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id where a.societe = societe_
		loop
			PERFORM cr_valide_fiche(societe_, em_.id, type_, date_debut_, date_fin_);
		end loop;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cr_valide_fiche(bigint, bigint, bigint, date, date)
  OWNER TO postgres;
