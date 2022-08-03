-- Function: calcul_montant_heure(bigint, double precision)

-- DROP FUNCTION calcul_montant_heure(bigint, double precision);

CREATE OR REPLACE FUNCTION calcul_montant_heure(id_ bigint, somme_ double precision)
  RETURNS double precision AS
$BODY$   DECLARE 
		employe_ RECORD;
		montant_ double precision default 0;    
BEGIN
	-- recherche des informations de l'employe
	select into employe_ e.id, a.societe, c.salaire_horaire from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id
				inner join yvs_grh_contrat_emps c on e.id = c.employe where c.id = id_;
	montant_ = somme_ * employe_.salaire_horaire;
	return montant_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION calcul_montant_heure(bigint, double precision)
  OWNER TO postgres;
COMMENT ON FUNCTION calcul_montant_heure(bigint, double precision) IS 'fonction qui retourne le montant des heures totales
elle prend en parametre l''id de l''employe en question et l''equivalent du total de ses heures de travail';
