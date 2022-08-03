-- Function: com_recalcule_pr_periode(date, date, bigint)
-- DROP FUNCTION com_recalcule_pr_periode(date, date, bigint);
CREATE OR REPLACE FUNCTION com_recalcule_pr_periode(debut_ date, fin_ date, unite_ bigint)
  RETURNS boolean AS
$BODY$   
DECLARE	
	ligne_ RECORD;
	pr_ double precision;
	i integer default 0;
	total bigint default 0;
BEGIN
	--yvs_com_contenu_doc_vente
	select into total  count(c.id) FROM yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d ON d.id=c.doc_vente inner join yvs_com_entete_doc_vente e ON e.id=d.entete_doc 
		WHERE c.doc_vente=d.id AND e.date_entete BETWEEN debut_ AND fin_ and d.type_doc='FV' AND c.conditionnement = unite_;
	FOR ligne_  IN select c.*, e.date_entete FROM yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d ON d.id=c.doc_vente inner join yvs_com_entete_doc_vente e ON e.id=d.entete_doc 
			WHERE c.doc_vente=d.id AND e.date_entete BETWEEN debut_ AND fin_ and d.type_doc='FV' AND c.conditionnement = unite_
	LOOP
		raise notice ' % / %',i, total;
		select into pr_ get_pr(ligne_.article, ligne_.depot_livraison_prevu, 0, ligne_.date_entete, ligne_.conditionnement);
		update yvs_com_contenu_doc_vente set pr=pr_ WHERE id=ligne_.id OR (parent=ligne_.id AND parent is not null);
		i=i+1;
	END LOOP;

	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_recalcule_pr_periode(date, date, bigint)
  OWNER TO postgres;
