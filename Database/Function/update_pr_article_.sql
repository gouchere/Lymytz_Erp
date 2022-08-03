-- Function: update_pr_article_(bigint, date, date, bigint)
-- DROP FUNCTION update_pr_article_(bigint, date, date, bigint);
CREATE OR REPLACE FUNCTION update_pr_article_(agence_ bigint, debut_ date, fin_ date, cond_ bigint)
  RETURNS boolean AS
$BODY$   
DECLARE	
	ligne_ RECORD;
	dates_ RECORD;
	article_ RECORD;
	
	pr_ double precision;
	i integer default 0;
	total bigint default 0;
BEGIN
	SELECT INTO article_ a.* FROM yvs_base_articles a INNER JOIN yvs_base_conditionnement c ON c.article = a.id WHERE c.id = cond_;
	RAISE NOTICE 'article_ : %',article_.id;
	FOR dates_ IN SELECT * FROM decoupage_interval_date(debut_, fin_, 'J', true)
	LOOP
		RAISE NOTICE 'dates_.date_debut : %',dates_.date_debut;
		FOR ligne_  IN SELECT DISTINCT COALESCE(c.depot_livraison_prevu, 0) AS depot_livraison_prevu FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_base_articles a ON a.id=c.article 
				inner join yvs_com_doc_ventes d ON d.id=c.doc_vente inner join yvs_com_entete_doc_vente e ON e.id=d.entete_doc 
				WHERE c.doc_vente = d.id and e.agence = agence_ AND e.date_entete BETWEEN dates_.date_debut AND dates_.date_fin and d.type_doc='FV' AND c.conditionnement = cond_
		LOOP
			select into pr_ get_pr(agence_, article_.id, ligne_.depot_livraison_prevu, 0, dates_.date_debut, cond_, 0);
			RAISE NOTICE 'ligne_.depot_livraison_prevu : % = %',ligne_.depot_livraison_prevu, pr_;
			update yvs_com_contenu_doc_vente y set pr = pr_, execute_trigger='OK' FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id WHERE d.id = y.doc_vente AND y.conditionnement = cond_ AND e.date_entete BETWEEN dates_.date_debut AND dates_.date_fin AND COALESCE(y.depot_livraison_prevu, 0) = ligne_.depot_livraison_prevu;
		END LOOP;
		
	END LOOP;
	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_pr_article_(bigint, date, date, bigint)
  OWNER TO postgres;
