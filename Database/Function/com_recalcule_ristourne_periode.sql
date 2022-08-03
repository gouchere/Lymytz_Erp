-- Function: com_recalcule_ristourne_periode(bigint, bigint, bigint, date, date, bigint)
-- DROP FUNCTION com_recalcule_ristourne_periode(bigint, bigint, bigint, date, date, bigint);
CREATE OR REPLACE FUNCTION com_recalcule_ristourne_periode(societe_ bigint, agence_ bigint, client_ bigint, debut_ date, fin_ date, point_ bigint)
  RETURNS double precision AS
$BODY$
declare 
	line_ record;
	query_ character varying default 'SELECT c.id, c.conditionnement, c.quantite, c.prix, d.client, e.date_entete FROM yvs_com_contenu_doc_vente c 
						INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc
						INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point
						INNER JOIN yvs_base_point_vente pv ON cp.point = pv.id INNER JOIN yvs_agences ag ON pv.agence = ag.id
						WHERE COALESCE(d.comptabilise, FALSE) IS FALSE AND e.date_entete BETWEEN '''||debut_||''' AND '''|| fin_||'''';
	total_ double precision default 0;
	rist_ double precision;
begin 
	IF(COALESCE(societe_, 0) > 0) THEN 
		query_= query_ || 'AND ag.societe = '||societe_;
	END IF;
	IF(COALESCE(agence_, 0) > 0) THEN 
		query_= query_ || 'AND ag.id = '||agence_;
	END IF;
	IF(COALESCE(point_, 0) > 0) THEN 
		query_= query_ || 'AND pv.id = '||point_;
	END IF;
	IF(COALESCE(client_, 0) > 0) THEN 
		query_= query_ || 'AND d.client = '||client_;
	END IF;
	FOR line_ IN execute query_
	LOOP
		rist_ = COALESCE(get_ristourne(line_.conditionnement, line_.quantite, line_.prix, line_.client, line_.date_entete), 0);
		UPDATE yvs_com_contenu_doc_vente SET ristourne = COALESCE(rist_, 0) WHERE id=line_.id;
		total_ = total_ + COALESCE(rist_, 0);
	END LOOP;
	RETURN total_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_recalcule_ristourne_periode(bigint, bigint, bigint, date, date, bigint)
  OWNER TO postgres;
