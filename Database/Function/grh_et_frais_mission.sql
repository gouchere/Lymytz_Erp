-- Function: grh_et_frais_mission(bigint, bigint, date, date, character varying, character varying)

-- DROP FUNCTION grh_et_frais_mission(bigint, bigint, date, date, character varying, character varying);

CREATE OR REPLACE FUNCTION grh_et_frais_mission(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN type_ character varying, IN periode_ character varying)
  RETURNS TABLE(id bigint, element character varying, entete character varying, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	element_ RECORD;
	dates_ RECORD;
	
	valeur_ DOUBLE PRECISION DEFAULT 0;
	somme_ DOUBLE PRECISION DEFAULT 0;
	
	entete_ CHARACTER VARYING;
	
	date_ DATE;
	date_save_ DATE DEFAULT date_debut_;
	
	i INTEGER DEFAULT 0;
BEGIN
-- 	DROP TABLE IF EXISTS table_frais_mission;
	CREATE TEMP TABLE IF NOT EXISTS table_frais_mission(_id bigint, _element character varying, _entete character varying, _valeur double precision, _rang integer); 
	DELETE FROM table_frais_mission;
	IF(societe_ > 0 or agence_ > 0)THEN
		IF(type_ = 'O')THEN
			FOR element_ IN SELECT DISTINCT(y.id) AS id, y.titre FROM yvs_grh_objets_mission y INNER JOIN yvs_grh_missions m ON m.objet_mission = y	.id WHERE m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R')
			LOOP
				date_debut_ = date_save_;
				i = 0;
				somme_ = 0;
				WHILE(date_debut_ <= date_fin_)
				LOOP
					i = i + 1;
					SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, periode_);
					IF(dates_.date_sortie IS NOT NULL)THEN
						date_ = dates_.date_sortie;
						entete_ = dates_.intitule;
					END IF;
					
					IF(agence_ > 0)THEN
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.objet_mission = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND e.agence = agence_ AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					ELSE
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.objet_mission = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					END IF;
					valeur_ = COALESCE(valeur_, 0);
					somme_ = somme_ + valeur_;

					INSERT INTO table_frais_mission VALUES(element_.id, element_.titre, entete_, valeur_, i);
					
					
					IF(periode_ = 'A')THEN
						date_debut_ = date_debut_ + INTERVAL '1 year';
					ELSIF(periode_ = 'T')THEN
						date_debut_ = date_debut_ + INTERVAL '3 month';
					ELSIF(periode_ = 'M')THEN
						date_debut_ = date_debut_ + INTERVAL '1 month';
					ELSIF(periode_ = 'S')THEN
						date_debut_ = date_debut_ + INTERVAL '1 week';
					ELSE
						date_debut_ = date_debut_ + INTERVAL '1 day';
					END IF;
				END LOOP;
				IF(somme_ = 0)THEN
					DELETE FROM table_frais_mission WHERE _id = element_.id;
				END IF;
			END LOOP;
		ELSIF(type_ = 'L')THEN
			FOR element_ IN SELECT DISTINCT(y.id) AS id, y.libele AS titre FROM yvs_dictionnaire y INNER JOIN yvs_grh_missions m ON m.lieu = y.id WHERE m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R')
			LOOP
				date_debut_ = date_save_;
				i = 0;
				somme_ = 0;
				WHILE(date_debut_ <= date_fin_)
				LOOP
					i = i + 1;
					SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, periode_);
					IF(dates_.date_sortie IS NOT NULL)THEN
						date_ = dates_.date_sortie;
						entete_ = dates_.intitule;
					END IF;
					
					IF(agence_ > 0)THEN
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.lieu = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND e.agence = agence_ AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					ELSE
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.lieu = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					END IF;
					valeur_ = COALESCE(valeur_, 0);
					somme_ = somme_ + valeur_;

					INSERT INTO table_frais_mission VALUES(element_.id, element_.titre, entete_, valeur_, i);
					
					
					IF(periode_ = 'A')THEN
						date_debut_ = date_debut_ + INTERVAL '1 year';
					ELSIF(periode_ = 'T')THEN
						date_debut_ = date_debut_ + INTERVAL '3 month';
					ELSIF(periode_ = 'M')THEN
						date_debut_ = date_debut_ + INTERVAL '1 month';
					ELSIF(periode_ = 'S')THEN
						date_debut_ = date_debut_ + INTERVAL '1 week';
					ELSE
						date_debut_ = date_debut_ + INTERVAL '1 day';
					END IF;
				END LOOP;
				IF(somme_ = 0)THEN
					DELETE FROM table_frais_mission WHERE _id = element_.id;
				END IF;
			END LOOP;
		ELSE
			FOR element_ IN SELECT DISTINCT(y.id) AS id, CONCAT(y.nom, ' ', y.prenom) AS titre FROM yvs_grh_employes y INNER JOIN yvs_grh_missions m ON m.employe = y.id WHERE m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R')
			LOOP
				date_debut_ = date_save_;
				i = 0;
				somme_ = 0;
				WHILE(date_debut_ <= date_fin_)
				LOOP
					i = i + 1;
					SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, periode_);
					IF(dates_.date_sortie IS NOT NULL)THEN
						date_ = dates_.date_sortie;
						entete_ = dates_.intitule;
					END IF;
					
					IF(agence_ > 0)THEN
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.employe = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND e.agence = agence_ AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					ELSE
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.employe = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					END IF;
					valeur_ = COALESCE(valeur_, 0);
					somme_ = somme_ + valeur_;

					INSERT INTO table_frais_mission VALUES(element_.id, element_.titre, entete_, valeur_, i);
					
					
					IF(periode_ = 'A')THEN
						date_debut_ = date_debut_ + INTERVAL '1 year';
					ELSIF(periode_ = 'T')THEN
						date_debut_ = date_debut_ + INTERVAL '3 month';
					ELSIF(periode_ = 'M')THEN
						date_debut_ = date_debut_ + INTERVAL '1 month';
					ELSIF(periode_ = 'S')THEN
						date_debut_ = date_debut_ + INTERVAL '1 week';
					ELSE
						date_debut_ = date_debut_ + INTERVAL '1 day';
					END IF;
				END LOOP;
				IF(somme_ = 0)THEN
					DELETE FROM table_frais_mission WHERE _id = element_.id;
				END IF;
			END LOOP;
		END IF;
	END IF;
	return QUERY SELECT * FROM table_frais_mission ORDER BY _element, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_frais_mission(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;
