CREATE OR REPLACE FUNCTION mut_calcul_solde_caisse_a_distribuer(id_periode_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	depot_ double precision default 0;
	retrait_ double precision default 0;
	interet_ double precision default 0;
	interet1_ double precision default 0;
	solde_ double precision default 0;
	depot_abonne_ double precision default 0;
	retrait_abonne_ double precision default 0;
	val_ double precision default 0;
	periode_ record;
	line_ record;
	

BEGIN	
	SELECT INTO periode_ * FROM yvs_mut_periode_exercice WHERE id=id_periode_;
	SELECT INTO depot_ SUM(m.montant) FROM yvs_mut_mouvement_caisse m LEFT JOIN yvs_mut_abonnement_depense d ON d.mouvement_caisse=m.id
			   WHERE d.id IS NULL AND m.statut_piece='P' AND m.date_mvt BETWEEN periode_.date_debut AND periode_.date_fin AND m.in_solde_caisse IS TRUE AND m.mouvement='D';
	SELECT INTO retrait_ SUM(m.montant) FROM yvs_mut_mouvement_caisse m LEFT JOIN yvs_mut_abonnement_depense d ON d.mouvement_caisse=m.id
			   WHERE d.id IS NULL AND m.statut_piece='P' AND m.date_mvt BETWEEN periode_.date_debut AND periode_.date_fin AND m.in_solde_caisse IS TRUE AND m.mouvement='R';	
	FOR line_ IN SELECT m.montant, m.mouvement,d.valeur, d.type_val FROM  yvs_mut_mouvement_caisse m INNER  join yvs_mut_abonnement_depense d on d.mouvement_caisse=m.id 
									WHERE m.statut_piece='P' AND d.date_retrait BETWEEN periode_.date_debut AND periode_.date_fin AND m.in_solde_caisse IS TRUE
		LOOP
			IF(line_.type_val!='T') THEN
			   val_=COALESCE(line_.valeur,0);
			ELSE
			   val_=COALESCE(line_.montant*line_.valeur/100,0);
			END IF;
			IF(line_.mouvement='D') THEN
			   depot_abonne_=depot_abonne_+val_;
			ELSE
			   retrait_abonne_=retrait_abonne_+val_;
			END IF;
		END LOOP;

	-- Calcul des intérêt récolté
	SELECT INTO interet_ SUM(m.interet) FROM yvs_mut_mensualite m INNER JOIN yvs_mut_echellonage e ON e.id=m.echellonage INNER JOIN yvs_mut_credit c ON c.id=e.credit
					    WHERE e.credit_retains_interet IS TRUE AND e.etat NOT IN ('S','A') AND c.etat NOT IN ('E','S','A') AND e.date_echellonage BETWEEN periode_.date_debut AND periode_.date_fin;
	SELECT INTO interet1_ SUM(m.interet) FROM yvs_mut_mensualite m INNER JOIN yvs_mut_echellonage e ON e.id=m.echellonage INNER JOIN yvs_mut_credit c ON c.id=e.credit
					     WHERE e.credit_retains_interet IS FALSE AND e.etat NOT IN ('S','A') AND c.etat NOT IN ('E','S','A') AND m.etat='P'  AND m.date_reglement BETWEEN periode_.date_debut AND periode_.date_fin;
	return (COALESCE(depot_,0)+ COALESCE(interet_,0)+COALESCE(interet1_,0) + COALESCE(depot_abonne_,0)- retrait_abonne_-COALESCE(retrait_,0));
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_calcul_solde_caisse_a_distribuer(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION mut_calcul_solde_caisse_a_distribuer(bigint) IS 'retourne le montant disponible à répartir entre les mutualistes';
