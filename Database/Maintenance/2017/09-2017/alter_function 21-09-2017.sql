-- Function: com_et_objectif_by_periode(bigint, bigint)

-- DROP FUNCTION com_et_objectif_by_periode(bigint, bigint);

CREATE OR REPLACE FUNCTION com_et_objectif_by_periode(IN periode_ bigint, IN objectif_ bigint, IN type_ CHARACTER VARYING)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	ligne_ RECORD;
	agence_ RECORD;
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
		FOR ligne_ IN SELECT c.id, c.code_ref AS code, c.nom, c.prenom, o.valeur AS attente FROM yvs_com_comerciale c INNER JOIN yvs_com_objectifs_comercial o ON o.commercial = c.id WHERE o.periode = periode_ AND o.objectif = objectif_
		LOOP
			i = i + 1;
			valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_));
			INSERT INTO table_objectif_by_periode VALUES(ligne_.id, ligne_.code, ligne_.nom ||' '||ligne_.prenom, periode_, entete_, ligne_.attente, valeur_, i);
		END LOOP;
	ELSIF(type_ = 'A')THEN
		FOR agence_ IN SELECT y.id, y.codeagence AS code, y.designation AS nom FROM yvs_agences y WHERE y.actif = TRUE
		LOOP
			i = i + 1;
			somme_ = 0;
			attente_ = 0;
			FOR ligne_ IN SELECT c.id, o.valeur AS attente FROM yvs_com_comerciale c INNER JOIN yvs_com_objectifs_comercial o ON o.commercial = c.id WHERE o.periode = periode_ AND o.objectif = objectif_ AND c.agence = agence_.id
			LOOP
				valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_));
				somme_ = somme_ + valeur_;
				attente_ = attente_ + ligne_.attente;
			END LOOP;
			IF(somme_ > 0)THEN
				INSERT INTO table_objectif_by_periode VALUES(agence_.id, agence_.code, agence_.nom , periode_, entete_, attente_, somme_, i);
			END IF;
		END LOOP;
	ELSE
		FOR agence_ IN SELECT y.id, y.code, y.libelle AS nom FROM yvs_base_point_vente y WHERE y.actif = TRUE
		LOOP
			i = i + 1;
			somme_ = 0;
			attente_ = 0;
			FOR ligne_ IN SELECT c.id, o.valeur AS attente FROM yvs_com_comerciale c INNER JOIN yvs_com_objectifs_comercial o ON o.commercial = c.id INNER JOIN yvs_com_creneau_horaire_users ch ON c.utilisateur = ch.users INNER JOIN yvs_com_creneau_point cp ON ch.creneau_point = cp.id WHERE o.periode = periode_ AND o.objectif = objectif_ AND cp.point = agence_.id
			LOOP
				valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_, objectif_));
				somme_ = somme_ + valeur_;
				attente_ = attente_ + ligne_.attente;
			END LOOP;
			IF(somme_ > 0)THEN
				INSERT INTO table_objectif_by_periode VALUES(agence_.id, agence_.code, agence_.nom , periode_, entete_, attente_, somme_, i);
			END IF;
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_objectif_by_periode ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif_by_periode(bigint, bigint, CHARACTER VARYING)
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION com_et_objectif_by_periode(IN periode_ bigint, IN objectif_ bigint)
  RETURNS TABLE(commercial bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
    
BEGIN
	return QUERY SELECT * FROM com_et_objectif_by_periode(periode_, objectif_, null) ORDER BY nom, rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif_by_periode(bigint, bigint)
  OWNER TO postgres;
  
  -- Function: com_et_objectif(bigint, bigint)

-- DROP FUNCTION com_et_objectif(bigint, bigint);

CREATE OR REPLACE FUNCTION com_et_objectif(IN societe_ bigint, IN objectif_ bigint, IN type_ CHARACTER VARYING)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	periode_ RECORD;
	ligne_ RECORD;
	agence_ RECORD;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	attente_ DOUBLE PRECISION DEFAULT 0;
	_attente_ DOUBLE PRECISION DEFAULT 0;
	somme_ DOUBLE PRECISION DEFAULT 0;
    
	i INTEGER DEFAULT 0;
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif(_element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif;
	IF(type_ IS NULL OR type_ = '')THEN
		FOR ligne_ IN SELECT y.id, y.code_ref AS code, y.nom, y.prenom FROM yvs_com_comerciale y INNER JOIN yvs_agences a ON y.agence = a.id WHERE a.societe = societe_ ORDER BY y.code_ref
		LOOP
			somme_ = 0;
			i = 0;
			
			FOR periode_ IN SELECT p.id, p.code_ref AS entete FROM yvs_com_periode_objectif p WHERE p.societe = societe_ ORDER BY p.date_debut
			LOOP
				i = i + i;
				SELECT INTO attente_ COALESCE(o.valeur) FROM yvs_com_objectifs_comercial o WHERE o.periode = periode_.id AND o.objectif = objectif_ AND o.commercial = ligne_.id;
				attente_ = COALESCE(attente_ , 0);
				valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_.id, objectif_));
				valeur_ = COALESCE(valeur_ , 0);
				somme_ = somme_ + valeur_;
				INSERT INTO table_objectif VALUES(ligne_.id, ligne_.code, ligne_.nom ||' '||ligne_.prenom, periode_.id, periode_.entete, attente_, valeur_, i);
			END LOOP;
			IF(somme_ = 0) THEN
				DELETE FROM table_objectif WHERE _element = ligne_.id;
			END IF;
		END LOOP;
	ELSIF(type_ = 'A')THEN
		FOR agence_ IN SELECT y.id, y.codeagence AS code, y.designation AS nom FROM yvs_agences y WHERE y.actif = TRUE AND y.societe = societe_
		LOOP
			i = 0;			
			FOR periode_ IN SELECT p.id, p.code_ref AS entete FROM yvs_com_periode_objectif p WHERE p.societe = societe_ ORDER BY p.date_debut
			LOOP
				somme_ = 0;
				i = i + i;
				FOR ligne_ IN SELECT y.id, y.code_ref AS code, y.nom, y.prenom FROM yvs_com_comerciale y WHERE y.agence = agence_.id  ORDER BY y.code_ref
				LOOP	
					SELECT INTO _attente_ COALESCE(o.valeur) FROM yvs_com_objectifs_comercial o WHERE o.periode = periode_.id AND o.objectif = objectif_ AND o.commercial = ligne_.id;
					attente_ = attente_ + COALESCE(_attente_ , 0);
					valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_.id, objectif_));
					somme_ = somme_ + COALESCE(valeur_ , 0);
				END LOOP;
				IF(somme_ != 0) THEN
					INSERT INTO table_objectif VALUES(agence_.id, agence_.code, agence_.nom, periode_.id, periode_.entete, attente_, somme_, i);
				END IF;
			END LOOP;
		END LOOP;
	ELSE
		FOR agence_ IN SELECT y.id, y.code, y.libelle AS nom FROM yvs_base_point_vente y WHERE y.actif = TRUE
		LOOP
		i = 0;			
			FOR periode_ IN SELECT p.id, p.code_ref AS entete FROM yvs_com_periode_objectif p WHERE p.societe = societe_ ORDER BY p.date_debut
			LOOP
				somme_ = 0;
				i = i + i;
				FOR ligne_ IN SELECT y.id, y.code_ref AS code, y.nom, y.prenom FROM yvs_com_comerciale y INNER JOIN yvs_com_creneau_horaire_users ch ON y.utilisateur = ch.users INNER JOIN yvs_com_creneau_point cp ON ch.creneau_point = cp.id WHERE cp.point = agence_.id  ORDER BY y.code_ref
				LOOP	
					SELECT INTO _attente_ COALESCE(o.valeur) FROM yvs_com_objectifs_comercial o WHERE o.periode = periode_.id AND o.objectif = objectif_ AND o.commercial = ligne_.id;
					attente_ = attente_ + COALESCE(_attente_ , 0);
					valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_.id, objectif_));
					somme_ = somme_ + COALESCE(valeur_ , 0);
				END LOOP;
				IF(somme_ != 0) THEN
					INSERT INTO table_objectif VALUES(agence_.id, agence_.code, agence_.nom, periode_.id, periode_.entete, attente_, somme_, i);
				END IF;
			END LOOP;
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_objectif ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif(bigint, bigint, CHARACTER VARYING)
  OWNER TO postgres;
-- Function: com_et_objectif(bigint, bigint)

-- DROP FUNCTION com_et_objectif(bigint, bigint);

CREATE OR REPLACE FUNCTION com_et_objectif(IN societe_ bigint, IN objectif_ bigint)
  RETURNS TABLE(commercial bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 

BEGIN
	return QUERY SELECT * FROM com_et_objectif(societe_, objectif_, null) ORDER BY nom, rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif(bigint, bigint)
  OWNER TO postgres;

  -- Function: com_et_objectif(bigint, bigint)

-- DROP FUNCTION com_et_objectif(bigint, bigint);

CREATE OR REPLACE FUNCTION grh_et_masse_salariale(IN societe_ BIGINT, IN agence_ CHARACTER VARYING)
  RETURNS TABLE(element bigint, code character varying, periode bigint, entete character varying, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	element_ RECORD;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	somme_ DOUBLE PRECISION DEFAULT 0;
	i INTEGER DEFAULT 0;
	ordre_ RECORD;
BEGIN
-- 	DROP TABLE IF EXISTS table_masse_salariale;
	CREATE TEMP TABLE IF NOT EXISTS table_masse_salariale(_element bigint, _code character varying, _periode bigint, _entete character varying, _valeur double precision, _rang integer); 
	DELETE FROM table_masse_salariale;
	FOR element_ IN SELECT y.id, y.nom, y.retenue FROM yvs_grh_element_salaire y INNER JOIN yvs_grh_categorie_element c ON y.categorie = c.id WHERE y.visible_on_livre_paie IS TRUE AND c.societe = societe_ ORDER BY y.nom
	LOOP
		i = 0;
		FOR ordre_ IN SELECT o.id, o.reference AS code FROM yvs_grh_ordre_calcul_salaire o WHERE o.societe = societe_
		LOOP
			i = i + 1;
			valeur_ = 0;
			if(element_.retenue is true)then
				IF(agence_ IS NOT NULL AND agence_ != '')THEN
					SELECT INTO valeur_ SUM(coalesce(d.retenu_salariale, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id and y.agence::character varying in (SELECT val from regexp_split_to_table(agence_,',') val);
				ELSE
					SELECT INTO valeur_ SUM(coalesce(d.retenu_salariale, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id;
				END IF;

				somme_ = somme_ + coalesce(valeur_, 0);
				valeur_ = -coalesce(valeur_, 0);
			elsif(element_.retenue is false)then
				IF(agence_ IS NOT NULL AND agence_ != '')THEN
					SELECT INTO valeur_ SUM(coalesce(d.montant_payer, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id and y.agence::character varying in (SELECT val from regexp_split_to_table(agence_,',') val);
				ELSE
					SELECT INTO valeur_ SUM(coalesce(d.montant_payer, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id 
					inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id
					where b.entete = ordre_.id and d.element_salaire = element_.id;
				END IF;
				
				somme_ = somme_ + coalesce(valeur_, 0);
			end if;
			insert INTO table_masse_salariale values(element_.id, element_.nom, ordre_.id, ordre_.code, valeur_, i);
		END LOOP;
		IF(somme_ = 0)THEN
			delete from table_masse_salariale where element = element_.id;
		END IF;
	end loop;
	return QUERY SELECT * FROM table_masse_salariale ORDER BY _code, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_masse_salariale(BIGINT, CHARACTER VARYING)
  OWNER TO postgres;
