-- Function: com_recalcule_remise_periode(bigint, bigint, bigint, date, date, bigint)

-- DROP FUNCTION com_recalcule_remise_periode(bigint, bigint, bigint, date, date, bigint);

CREATE OR REPLACE FUNCTION com_recalcule_remise_periode(
    societe_ bigint,
    agence_ bigint,
    client_ bigint,
    debut_ date,
    fin_ date,
    point_ bigint)
  RETURNS double precision AS
$BODY$
declare 
	line_ record;
	query_ character varying default 'SELECT c.id, c.article, c.conditionnement, c.quantite, c.prix, c.rabais, d.client, e.date_entete, cp.point::bigint, co.unite, a.puv_ttc, d.categorie_comptable as categorie 
						FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_base_articles a ON c.article = a.id 
						INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc
						INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point
						INNER JOIN yvs_base_point_vente pv ON cp.point = pv.id INNER JOIN yvs_agences ag ON pv.agence = ag.id
						INNER JOIN yvs_base_conditionnement co ON c.conditionnement = co.id INNER JOIN yvs_base_unite_mesure un ON co.unite = un.id
						WHERE e.date_entete BETWEEN '''||debut_||''' AND '''|| fin_||'''';
	total_ double precision default 0;
	prix_total_ double precision;
	taxe_ double precision default 0;
	prix_ double precision;
	remise_ double precision;
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
		prix_ = line_.prix - COALESCE(line_.rabais, 0);
		remise_ = COALESCE(get_remise_vente(line_.article, line_.quantite, prix_, line_.client, 0, line_.date_entete, line_.unite), 0);		
		IF(line_.puv_ttc IS FALSE AND COALESCE(line_.categorie, 0) > 0)THEN	
			taxe_ = (SELECT get_taxe(line_.article, line_.categorie, 0, remise_, line_.quantite, prix_, true, 0));
		END IF;
		prix_total_ = (line_.quantite * prix_) + taxe_ - remise_; 
		UPDATE yvs_com_contenu_doc_vente SET remise = COALESCE(remise_, 0), prix_total = prix_total_ WHERE id = line_.id;
		total_ = total_ + COALESCE(remise_, 0);
	END LOOP;
	RETURN total_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_recalcule_remise_periode(bigint, bigint, bigint, date, date, bigint)
  OWNER TO postgres;
