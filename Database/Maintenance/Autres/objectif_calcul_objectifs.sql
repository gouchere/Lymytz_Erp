-- Function: conge_jour_supplementaire(bigint, date)

-- DROP FUNCTION conge_jour_supplementaire(bigint, date);

CREATE OR REPLACE FUNCTION objectif_calcul_objectifs(periode_ bigint, id_model_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 
	requete_  character varying;
	model_ record;
	cible_article bigint[];
	cible_client bigint[];
	cible_zone bigint[];
    
BEGIN
SELECT INTO model_  * FROM yvs_com_modele_objectif WHERE id=id_model_;
SELECT INTO cible_article c.id_externe FROM yvs_com_cible_objectif c INNER JOIN yvs_com_modele_objectif m ON m.id=c.objectif 
									   WHERE c.table_externe='BASE_ARTICLE' AND m.id=id_model_;
		
	CASE model_.indicateur
		WHEN 'CA' THEN 		
			requete_='SELECT * FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d';
		WHEN 'QUANTITE' THEN 
		WHEN 'MARGE' THEN 
		ELSE
	END CASE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION objectif_calcul_objectifs(bigint,bigint)
  OWNER TO postgres;