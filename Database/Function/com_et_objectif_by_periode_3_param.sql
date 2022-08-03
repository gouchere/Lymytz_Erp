-- Function: com_et_objectif_by_periode(bigint, bigint, character varying)

-- DROP FUNCTION com_et_objectif_by_periode(bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif_by_periode(IN periode_ bigint, IN objectif_ bigint, IN type_ character varying)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	ligne_ RECORD;
	valeur_ DOUBLE PRECISION DEFAULT 0;

	i INTEGER DEFAULT 0;
	somme_ DOUBLE PRECISION DEFAULT 0;
	attente_ DOUBLE PRECISION DEFAULT 0;
	entete_ CHARACTER VARYING;
    
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif_by_periode;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif_by_periode(_element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif_by_periode;
	SELECT INTO entete_ p.code_ref FROM yvs_com_periode_objectif p WHERE p.id = periode_;
	i = 0;
	IF(type_ IS NULL OR type_ = '')THEN
		FOR ligne_ IN SELECT c.id, c.code_ref AS code, c.nom, c.prenom, o.valeur AS attente 
					  FROM yvs_com_comerciale c INNER JOIN yvs_com_objectifs_comercial o ON o.commercial = c.id 
					  WHERE o.periode = periode_ AND o.objectif = objectif_
		LOOP
			i = i + 1;
			attente_ = COALESCE(ligne_.attente , 0);
			valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_, type_));
			valeur_ = COALESCE(valeur_ , 0);
			IF(valeur_ != 0 OR attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom ||' '||ligne_.prenom, periode_, entete_, attente_, valeur_, i);
			END IF;	
		END LOOP;
	ELSIF(type_ = 'A')THEN
		FOR ligne_ IN SELECT y.id, y.codeagence AS code, y.designation AS nom, o.valeur AS attente FROM yvs_agences y INNER JOIN yvs_com_objectifs_agence o ON o.agence = y.id WHERE o.periode = periode_ AND o.objectif = objectif_
		LOOP
			i = i + 1;
			attente_ = COALESCE(ligne_.attente , 0);
			IF(attente_ IS NULL OR attente_ < 0)THEN
				SELECT INTO attente_ SUM(COALESCE(o.valeur, 0)) FROM yvs_com_objectifs_comercial o INNER JOIN yvs_com_comerciale y ON o.commercial = y.id WHERE o.periode = periode_ AND o.objectif = objectif_ AND y.agence = ligne_.id;
			END IF;
			attente_ = COALESCE(ligne_.attente , 0);
			valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_, type_));
			valeur_ = COALESCE(valeur_ , 0);
			IF(valeur_ != 0 OR attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom, periode_, entete_, attente_, valeur_, i);
			END IF;
		END LOOP;
	ELSE
		FOR ligne_ IN SELECT y.id, y.code, y.libelle AS nom, o.valeur AS attente FROM yvs_base_point_vente y INNER JOIN yvs_com_objectifs_point_vente o ON o.point_vente = y.id WHERE o.periode = periode_ AND o.objectif = objectif_
		LOOP
			i = i + 1;
			attente_ = COALESCE(ligne_.attente , 0);
			IF(attente_ IS NULL OR attente_ < 0)THEN
				SELECT INTO attente_ SUM(COALESCE(o.valeur, 0)) FROM yvs_com_objectifs_comercial o INNER JOIN yvs_com_comerciale c ON o.commercial = c.id INNER JOIN yvs_com_creneau_horaire_users ch ON c.utilisateur = ch.users INNER JOIN yvs_com_creneau_point cp ON ch.creneau_point = cp.id
				WHERE o.periode = periode_ AND o.objectif = objectif_ AND cp.point = ligne_.id;
			END IF;
			attente_ = COALESCE(ligne_.attente , 0);
			valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_, type_));
			valeur_ = COALESCE(valeur_ , 0);
			IF(valeur_ != 0 OR attente_ != 0) THEN
				INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom, periode_, entete_, attente_, valeur_, i);
			END IF;
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_objectif_by_periode ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif_by_periode(bigint, bigint, character varying)
  OWNER TO postgres;
