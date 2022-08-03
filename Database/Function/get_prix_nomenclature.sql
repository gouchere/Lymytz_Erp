-- Function: get_prix_nomenclature(bigint, bigint, bigint, date)

-- DROP FUNCTION get_prix_nomenclature(bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_prix_nomenclature(article_ bigint, unite_ bigint, depot_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	pr_ double precision;
	prix_ double precision default 0;
	prix_N_ double precision default 0;
	quantite_ double precision default 0;
	quantite_N double precision default 0;
	composant_ record ;	
	idNom_ Bigint ;	

BEGIN
	FOR composant_ IN SELECT c.*,n.quantite qteN FROM yvs_prod_composant_nomenclature c INNER JOIN yvs_prod_nomenclature n ON n.id=c.nomenclature
								 WHERE n.article=article_ AND n.unite_mesure=unite_ AND c.inside_cout=true AND c.actif IS TRUE AND c.alternatif IS FALSE
														  AND n.actif IS true
		LOOP
			quantite_N=composant_.qteN;
			-- vérifie si le composant est un produit intermédiare  (ayant lui aussi une nomenclature)
			SELECT INTO idNom_ COUNT(id) FROM yvs_prod_nomenclature n WHERE n.article=composant_.article AND n.actif IS TRUE;
			IF(COALESCE(idNom_,0)>0) THEN
				prix_N_=prix_N_+ (get_prix_nomenclature(composant_.article, composant_.unite, depot_, date_) * composant_.quantite);
				RAISE NOTICE ' PRIX % =%',(SELECT ref_art FROM yvs_base_articles WHERE id=composant_.article),prix_N_;
			ELSE 			
				pr_=get_pr(composant_.article,depot_,0,date_,composant_.unite,0);
				IF(composant_.composant_lie IS NOT NULL) THEN
					-- la quantité est défini comme pourcentage  d'un autre composant
					SELECT INTO quantite_ composant_.quantite*COALESCE((SELECT c1.quantite FROM yvs_prod_composant_nomenclature c1 WHERE c1.id=composant_.composant_lie),0);
					prix_=prix_+COALESCE(pr_* COALESCE(quantite_,0));
				ELSE
					prix_=prix_+COALESCE(pr_* composant_.quantite);
				END IF;	
			END IF;			
		END LOOP;
	IF(COALESCE(quantite_N,0)>0) THEN
	   RETURN (prix_/quantite_N) + (prix_N_ /quantite_N);		
	ELSE
	   RETURN prix_;		
	END IF;	
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_prix_nomenclature(bigint, bigint, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_prix_nomenclature(bigint, bigint, bigint, date) IS 'retourne le prix de la nomenclature d'' article';
