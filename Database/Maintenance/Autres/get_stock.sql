
CREATE OR REPLACE FUNCTION get_stock(art_ bigint, depot_ bigint, date_ date, tranche_ integer)
  RETURNS double precision AS
$BODY$
	DECLARE entree_ double precision; 
		sortie_ double precision; 
BEGIN
	entree_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='E' AND m.tranche=tranche_));
	sortie_ := (SELECT SUM(m.quantite) FROM yvs_base_mouvement_stock m WHERE (m.article=art_ AND m.depot=depot_ AND m.date_doc<=date_ AND m.mouvement='S' AND m.tranche=tranche_));

	IF entree_ IS null THEN
	 entree_:=0;	
	END IF;
	IF sortie_ IS null THEN
	  sortie_:=0;	
	END IF;
	return (entree_ - sortie_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock(bigint, bigint, date, integer)
  OWNER TO postgres;