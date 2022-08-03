-- Function: import_gamme(bigint, character varying, integer, character varying, character varying, character varying)
-- DROP FUNCTION import_gamme(bigint, character varying, integer, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION import_gamme(societe_ bigint, serveur character varying, port integer, database character varying, users character varying, password character varying)
  RETURNS boolean AS
$BODY$
DECLARE
    articles_ RECORD;
    gammes_ RECORD;
    operations_ RECORD;
    composants_ RECORD;
    datas_ RECORD;

    article_ BIGINT;
    conditionnement_ BIGINT;
    unite_ BIGINT;
    gamme_ BIGINT;
    composant_ BIGINT;
    operation_ BIGINT;
    data_ BIGINT;
    other_ BIGINT;

    query_ CHARACTER VARYING;

BEGIN
	FOR articles_ IN SELECT a.id, a.ref_art FROM yvs_base_articles a INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE a.categorie IN ('PF', 'PSF') AND f.societe = societe_
	LOOP
		query_ = 'SELECT g.*, u.reference AS unite FROM yvs_prod_gamme_article g INNER JOIN yvs_base_articles a ON g.article = a.id LEFT JOIN yvs_base_unite_mesure u ON g.unite_temps = u.id WHERE a.ref_art = '||QUOTE_LITERAL(articles_.ref_art);
		FOR gammes_ IN SELECT * FROM dblink('host='||serveur||' dbname='||database||' user='||users||' password='||password, query_)
			AS table_(id bigint, code_ref character varying,  designation character varying, description character varying, actif boolean, article integer, principal boolean, author bigint, date_update timestamp,
				date_save timestamp, debut_validite date, fin_validite date, permanant boolean, unite_temps bigint, acces bigint, masquer boolean , execute_trigger character varying, unite character varying)
		LOOP
			SELECT INTO gamme_ y.id FROM yvs_prod_gamme_article y WHERE y.code_ref = gammes_.code_ref AND y.article = articles_.id;
			IF(COALESCE(gamme_, 0) < 1)THEN
				unite_ = NULL;
				IF(gammes_.unite_temps IS NOT NULL AND gammes_.unite IS NOT NULL)THEN
					SELECT INTO unite_ y.id FROM yvs_base_unite_mesure y WHERE y.reference = gammes_.unite AND y.societe = societe_;
					IF(COALESCE(unite_ , 0) < 1)THEN
						INSERT INTO yvs_base_unite_mesure(reference, libelle, societe, type) VALUES (gammes_.unite, gammes_.unite, societe_, 'Q');
						unite_ = currval('yvs_prod_unite_masse_id_seq');
					END IF;
				END IF;
				INSERT INTO yvs_prod_gamme_article(code_ref, designation, description, actif, article, principal, date_update, date_save, 
						debut_validite, fin_validite, permanant, unite_temps, acces, masquer) 
				VALUES (gammes_.code_ref, gammes_.designation, gammes_.description, gammes_.actif, articles_.id, gammes_.principal, gammes_.date_update, gammes_.date_save, 
						gammes_.debut_validite, gammes_.fin_validite, gammes_.permanant, unite_, gammes_.acces, gammes_.masquer);
				gamme_ = currval('yvs_prod_gamme_article_id_seq');
			ELSE
				UPDATE yvs_prod_gamme_article SET code_ref=gammes_.code_ref, designation=gammes_.designation, description=gammes_.description, actif=gammes_.actif, principal=gammes_.principal, 
					date_update=gammes_.date_update, date_save=gammes_.date_save, debut_validite=gammes_.debut_validite,  fin_validite=gammes_.fin_validite, permanant=gammes_.permanant, acces=gammes_.acces, masquer=gammes_.masquer 
				WHERE id = gamme_;
				DELETE FROM yvs_prod_operations_gamme WHERE gamme_article = gamme_;
			END IF;

			query_ = 'SELECT y.* FROM yvs_prod_operations_gamme y WHERE y.gamme_article = '||gammes_.id;
			FOR operations_ IN SELECT * FROM dblink('host='||serveur||' dbname='||database||' user='||users||' password='||password, query_)
				AS table_(id integer, code_ref character varying, description character varying, numero integer, gamme_article integer, operation boolean, author bigint,
					date_update timestamp, date_save timestamp, temps_reglage double precision, temps_operation double precision, type_temps character varying, taux_efficience double precision,
					taux_perte double precision, quantite_base double precision, cadence double precision, quantite_min double precision, type_cout character(1), actif boolean, execute_trigger character varying)
			LOOP
				INSERT INTO yvs_prod_operations_gamme(code_ref, description, numero, gamme_article, operation, date_update, date_save, temps_reglage, temps_operation, 
					type_temps, taux_efficience, taux_perte, quantite_base, cadence,  quantite_min, type_cout, actif)
				VALUES (operations_.code_ref, operations_.description, operations_.numero, gamme_, operations_.operation, operations_.date_update, operations_.date_save, operations_.temps_reglage, operations_.temps_operation, 
					operations_.type_temps, operations_.taux_efficience, operations_.taux_perte, operations_.quantite_base, operations_.cadence, operations_.quantite_min, operations_.type_cout, operations_.actif);
				operation_ = currval('yvs_prod_phase_gamme_id_seq');
				
				query_ = 'SELECT y.*, ca.ref_art AS composant_article, cu.reference AS composant_unite, na.ref_art AS nomenclature_article, n.reference AS nomenclature FROM yvs_prod_composant_op y 
					LEFT JOIN yvs_prod_composant_nomenclature cn ON y.composant = cn.id LEFT JOIN yvs_prod_nomenclature n ON cn.nomenclature = n.id LEFT JOIN yvs_base_articles na ON cn.article = na.id
					LEFT JOIN yvs_base_conditionnement c ON y.unite = c.id LEFT JOIN yvs_base_articles ca ON c.article = ca.id LEFT JOIN yvs_base_unite_mesure cu ON c.unite = cu.id WHERE y.operation = '||operations_.id;
				FOR composants_ IN SELECT * FROM dblink('host='||serveur||' dbname='||database||' user='||users||' password='||password, query_)
					AS table_(id integer, sens character(1), composant bigint, operation bigint, author bigint, date_save timestamp, date_update timestamp, quantite double precision, marge_qte double precision, 
						coeficient_valeur double precision, unite bigint, taux_perte double precision,  type_cout character(1), execute_trigger character varying, free_use boolean,
						composant_article character varying, composant_unite character varying, nomenclature_article character varying, nomenclature character varying)
				LOOP
					conditionnement_ = NULL;
					IF(composants_.unite IS NOT NULL AND composants_.composant_article IS NOT NULL AND composants_.composant_unite IS NOT NULL)THEN
						SELECT INTO article_ y.id FROM yvs_base_articles y INNER JOIN yvs_base_famille_article f ON y.famille = f.id WHERE f.societe = societe_ AND y.ref_art = composants_.composant_article;
						IF(COALESCE(article_, 0) < 1)THEN
							continue ;
						END IF;
						SELECT INTO conditionnement_ y.id FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = article_ AND u.reference = composants_.composant_unite;
						IF(COALESCE(conditionnement_, 0) < 1)THEN
							SELECT INTO unite_ y.id FROM yvs_base_unite_mesure y WHERE y.reference = composants_.composant_unite AND y.societe = societe_;
							IF(COALESCE(unite_ , 0) < 1)THEN
								INSERT INTO yvs_base_unite_mesure(reference, libelle, societe, type) VALUES (composants_.composant_unite, composants_.composant_unite, societe_, 'Q');
								unite_ = currval('yvs_prod_unite_masse_id_seq');
							END IF;
							IF(COALESCE(unite_, 0) > 1)THEN
								INSERT INTO yvs_base_conditionnement(article, unite, by_prod, actif) VALUES (article_, unite_, true, true);
								conditionnement_ = currval('yvs_base_conditionnement_id_seq');
							END IF;
						END IF;
						IF(COALESCE(conditionnement_, 0) < 1)THEN
							continue ;
						END IF;
					END IF;
					SELECT INTO composant_ y.id FROM yvs_prod_composant_nomenclature y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_prod_nomenclature n ON y.nomenclature = n.id
						WHERE a.ref_art = composants_.nomenclature_article AND n.reference = composants_.nomenclature;
					IF(COALESCE(composant_, 0) < 1)THEN
						continue ;
					END IF;
					INSERT INTO yvs_prod_composant_op(sens, composant, operation, date_save, date_update, quantite, marge_qte, coeficient_valeur, unite, taux_perte, type_cout, free_use)
					VALUES (composants_.sens, composant_, operation_, composants_.date_save, composants_.date_update, 
						composants_.quantite, composants_.marge_qte, composants_.coeficient_valeur, conditionnement_, composants_.taux_perte, composants_.type_cout, composants_.free_use);
				END LOOP;
			END LOOP;
		END LOOP;
	END LOOP;
	return true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION import_gamme(bigint, character varying, integer, character varying, character varying, character varying)
  OWNER TO postgres;
