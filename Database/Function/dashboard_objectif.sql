-- Function: dashboard_objectif(bigint, bigint, integer, date, date, character varying, character varying)

-- DROP FUNCTION dashboard_objectif(bigint, bigint, integer, date, date, character varying, character varying);

CREATE OR REPLACE FUNCTION dashboard_objectif(IN employe_ bigint, IN societe_ bigint, IN nombre_ integer, IN date_debut_ date, IN date_fin_ date, IN header_ character varying, IN element_ character varying)
  RETURNS TABLE(element character varying, entete character varying, valeur double precision) AS
$BODY$
DECLARE
	ligne_ CHARACTER VARYING DEFAULT ''; -- element_
	colonne_ CHARACTER VARYING DEFAULT ''; -- header_
	valeur_ DOUBLE PRECISION DEFAULT 0;

	objectif_ RECORD;
	donnee_ RECORD;
BEGIN
-- 	DROP TABLE IF EXISTS table_dashboard_objectif;
	CREATE TEMP TABLE IF NOT EXISTS table_dashboard_objectif(_element CHARACTER VARYING, _entete CHARACTER VARYING, _valeur DOUBLE PRECISION);
	DELETE FROM table_dashboard_objectif;

	IF(header_ IS NULL OR header_ = '' OR header_ = ' ')THEN
		header_ = 'Z';
	END IF;
	IF(element_ IS NULL OR element_ = '' OR element_ = ' ')THEN
		element_ = 'A';
	END IF;
	FOR objectif_ IN SELECT y.* FROM yvs_base_objectif y INNER JOIN yvs_base_objectif_employe e ON e.objectif = y.id WHERE y.nombre_element = nombre_ AND e.employe = employe_ AND y.societe = societe_
	LOOP
		FOR donnee_ IN SELECT y.* FROM yvs_base_element_objectif y WHERE y.objectif = objectif_.id
		LOOP
			CASE donnee_.table_externe
				WHEN 'yvs_dictionnaire' THEN
					IF(header_ = 'Z')THEN
						SELECT INTO colonne_ libele FROM yvs_dictionnaire WHERE id = donnee_.id_externe;
					ELSIF(element_ = 'Z')THEN
						SELECT INTO ligne_ libele FROM yvs_dictionnaire WHERE id = donnee_.id_externe;
					END IF;
				WHEN 'yvs_base_articles' THEN
					IF(header_ = 'A')THEN
						SELECT INTO colonne_ designation FROM yvs_base_articles WHERE id = donnee_.id_externe;
					ELSIF(element_ = 'A')THEN
						SELECT INTO ligne_ designation FROM yvs_base_articles WHERE id = donnee_.id_externe;
					END IF;
				WHEN 'yvs_base_categorie_client' THEN
					IF(header_ = 'C')THEN
						SELECT INTO colonne_ libelle FROM yvs_base_categorie_client WHERE id = donnee_.id_externe;
					ELSIF(element_ = 'C')THEN
						SELECT INTO ligne_ libelle FROM yvs_base_categorie_client WHERE id = donnee_.id_externe;
					END IF;
				WHEN 'yvs_base_categorie_comptable' THEN
					IF(header_ = 'O')THEN
						SELECT INTO colonne_ designation FROM yvs_base_categorie_comptable WHERE id = donnee_.id_externe;
					ELSIF(element_ = 'O')THEN
						SELECT INTO ligne_ designation FROM yvs_base_categorie_comptable WHERE id = donnee_.id_externe;
					END IF;
				WHEN 'yvs_com_client' THEN
					IF(header_ = 'L')THEN
						SELECT INTO colonne_ CONCAT(nom, ' ', prenom) FROM yvs_com_client WHERE id = donnee_.id_externe;
					ELSIF(element_ = 'L')THEN
						SELECT INTO ligne_ CONCAT(nom, ' ', prenom) FROM yvs_com_client WHERE id = donnee_.id_externe;
					END IF;
				ELSE
					colonne_ = '';
					ligne_ = '';
			END CASE;
		END LOOP;
		INSERT INTO table_dashboard_objectif VALUES(ligne_, colonne_, valeur_);
	END LOOP;
	RETURN QUERY SELECT * FROM table_dashboard_objectif ORDER BY _element, _entete;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION dashboard_objectif(bigint, bigint, integer, date, date, character varying, character varying)
  OWNER TO postgres;
COMMENT ON FUNCTION dashboard_objectif(bigint, bigint, integer, date, date, character varying, character varying) IS 'Les valeurs de "header ou element" A-Article  C-Categorie client  L-Client  O-Categorie comptable  Z-Zone';
