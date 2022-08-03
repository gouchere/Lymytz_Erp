-- Function: grh_get_datedebut_ct(bigint)

-- DROP FUNCTION grh_get_datedebut_ct(bigint);

CREATE OR REPLACE FUNCTION grh_get_datedebut_ct(conge_ bigint)
  RETURNS date AS
$BODY$
DECLARE 
	conges_ record;
	datedebut date;
BEGIN	
	SELECT INTO conges_ employe, date_debut FROM yvs_grh_conge_emps WHERE id = conge_;
	datedebut = conges_.date_debut;
	FOR conges_  IN SELECT c.id FROM yvs_grh_conge_emps c WHERE c.employe = conges_.employe AND c.id != conge_ AND c.statut IN('V','T','C') AND c.type_conge='CT' AND (c.date_fin between (conges_.date_debut - integer '1')::date and conges_.date_debut)
	LOOP	
		datedebut = (select grh_get_datedebut_ct(conges_.id));
	END LOOP;
	RETURN datedebut;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION grh_get_datedebut_ct(bigint)
  OWNER TO postgres;
