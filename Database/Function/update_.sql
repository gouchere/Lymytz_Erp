-- Function: update_(date, date)

-- DROP FUNCTION update_(date, date);

CREATE OR REPLACE FUNCTION update_(debut_ date, fin_ date)
  RETURNS boolean AS
$BODY$   
DECLARE	
	ligne_ RECORD;
	pr_ double precision;
	i integer default 0;
	total bigint default 0;
BEGIN
	-- ALTER TABLE yvs_com_contenu_doc_vente DISABLE TRIGGER update_;
	select into total  count(c.id) FROM yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d ON d.id=c.doc_vente
							inner join yvs_com_entete_doc_vente e ON e.id=d.entete_doc inner join yvs_com_creneau_horaire_users cu ON cu.id=e.creneau
							INNER JOIN yvs_com_creneau_point cp on cp.id=cu.creneau_point
							WHERE c.doc_vente=d.id and e.agence=2315 AND e.date_entete BETWEEN debut_ AND fin_ and d.type_doc='FV'
							AND cp.point=35;
	FOR ligne_  IN select c.*,e.date_entete FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_base_article a ON a.id=c.article inner join yvs_com_doc_ventes d ON d.id=c.doc_vente
							inner join yvs_com_entete_doc_vente e ON e.id=d.entete_doc inner join yvs_com_creneau_horaire_users cu ON cu.id=e.creneau
							INNER JOIN yvs_com_creneau_point cp on cp.id=cu.creneau_point
							WHERE c.doc_vente=d.id and e.agence=2315 AND e.date_entete BETWEEN debut_ AND fin_ and d.type_doc='FV'
							AND cp.point=35
	LOOP
	raise notice ' % / %',i, total;
	IF(a.categorie='PF') THEN
		select into pr_ get_prix_nomenclature(ligne_.article, ligne_.conditionnement, ligne_.depot_livraison_prevu,ligne_.date_entete);
	ELSE
		select into pr_ get_pr(ligne_.article, ligne_.depot_livraison_prevu, 0, ligne_.date_entete, ligne_.conditionnement);
	END IF;
	update yvs_com_contenu_doc_vente set pr=pr_, execute_trigger='OK' WHERE id=ligne_.id OR (parent=ligne_.id AND parent is not null);
		i=i+1;
	END LOOP;
	-- ALTER TABLE yvs_com_contenu_doc_vente ENABLE TRIGGER update_;
	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_(date, date)
  OWNER TO postgres;
