-- Function: import_nomenclature(bigint, character varying, integer, character varying, character varying, character varying)

-- DROP FUNCTION import_nomenclature(bigint, character varying, integer, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION import_nomenclature(societe_ bigint, serveur character varying, port integer, database character varying, users character varying, password character varying)
  RETURNS boolean AS
$BODY$
DECLARE
    articles_ RECORD;
    nomenclatures_ RECORD;
    composants_ RECORD;
    datas_ RECORD;

    article_ BIGINT;
    conditionnement_ BIGINT;
    unite_ BIGINT;
    nomenclature_ BIGINT;
    composant_ BIGINT;
    data_ BIGINT;
    other_ BIGINT;

    query_ CHARACTER VARYING;

BEGIN
	FOR articles_ IN SELECT a.id, a.ref_art FROM yvs_base_articles a INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE a.categorie IN ('PF', 'PSF') AND f.societe = societe_
	LOOP
		query_ = 'SELECT n.*, u.reference AS unite FROM yvs_prod_nomenclature n INNER JOIN yvs_base_articles a ON n.article = a.id LEFT JOIN yvs_base_conditionnement c ON n.unite_mesure = c.id LEFT JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE a.ref_art = '||QUOTE_LITERAL(articles_.ref_art);
		FOR nomenclatures_ IN SELECT * FROM dblink('host='||serveur||' dbname='||database||' user='||users||' password='||password, query_)
			AS table_(id bigint, reference character varying, niveau integer, debut_validite date, fin_validite date, quantite double precision, article integer, actif boolean, quantite_lie_aux_composants boolean, 
				principal boolean, alway_valide boolean, author bigint, unite_preference character varying, date_update timestamp, date_save timestamp, unite_mesure bigint, for_conditionnement boolean, 
				marge_qte double precision, acces bigint, masquer boolean, type_nomenclature character varying, execute_trigger character varying, unite character varying)
		LOOP
			SELECT INTO nomenclature_ y.id FROM yvs_prod_nomenclature y WHERE y.reference = nomenclatures_.reference AND y.article = articles_.id;
			IF(COALESCE(nomenclature_, 0) < 1)THEN
				conditionnement_ = NULL;
				IF(nomenclatures_.unite_mesure IS NOT NULL AND nomenclatures_.unite IS NOT NULL)THEN
					SELECT INTO conditionnement_ y.id FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = articles_.id AND u.reference = nomenclatures_.unite;
					IF(COALESCE(conditionnement_, 0) < 1)THEN
						SELECT INTO unite_ y.id FROM yvs_base_unite_mesure y WHERE y.reference = nomenclatures_.unite AND y.societe = societe_;
						IF(COALESCE(unite_ , 0) < 1)THEN
							INSERT INTO yvs_base_unite_mesure(reference, libelle, societe, type) VALUES (nomenclatures_.unite, nomenclatures_.unite, societe_, 'Q');
							unite_ = currval('yvs_prod_unite_masse_id_seq');
						END IF;
						IF(COALESCE(unite_, 0) > 1)THEN
							INSERT INTO yvs_base_conditionnement(article, unite, by_prod, actif) VALUES (articles_.id, unite_, true, true);
							conditionnement_ = currval('yvs_base_conditionnement_id_seq');
						END IF;
					END IF;
					IF(COALESCE(conditionnement_, 0) < 1)THEN
						continue ;
					END IF;
				END IF;
				INSERT INTO yvs_prod_nomenclature(reference, niveau, debut_validite, fin_validite, quantite, article, actif, quantite_lie_aux_composants, 
					principal, alway_valide, unite_preference, date_update, date_save, unite_mesure, for_conditionnement, marge_qte, acces, masquer, type_nomenclature) 
				VALUES (nomenclatures_.reference, nomenclatures_.niveau, nomenclatures_.debut_validite, nomenclatures_.fin_validite, nomenclatures_.quantite, articles_.id, nomenclatures_.actif, nomenclatures_.quantite_lie_aux_composants, 
					nomenclatures_.principal, nomenclatures_.alway_valide, nomenclatures_.unite_preference, nomenclatures_.date_update, nomenclatures_.date_save, conditionnement_, nomenclatures_.for_conditionnement, nomenclatures_.marge_qte, nomenclatures_.acces, nomenclatures_.masquer, nomenclatures_.type_nomenclature);
				nomenclature_ = currval('yvs_prod_nomenclature_id_seq');
			ELSE
				DELETE FROM yvs_prod_composant_nomenclature WHERE nomenclature = nomenclature_;
			END IF;
			RAISE NOTICE 'nomenclature : % ', nomenclatures_.id;
			query_ = 'SELECT y.*, a.ref_art, u.reference AS reference FROM yvs_prod_composant_nomenclature y INNER JOIN yvs_base_articles a ON y.article = a.id LEFT JOIN yvs_base_conditionnement c ON y.unite = c.id LEFT JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE y.nomenclature = '||nomenclatures_.id;
			FOR composants_ IN SELECT * FROM dblink('host='||serveur||' dbname='||database||' user='||users||' password='||password, query_)
				AS table_(id integer, quantite double precision, coefficient double precision, type character varying, mode_arrondi character varying, actif boolean, article integer, nomenclature integer, author bigint, 
					unite bigint, date_update timestamp, date_save timestamp, stockable boolean, ordre integer, composant_lie integer, inside_cout boolean, alternatif boolean, execute_trigger character varying, 
					free_use boolean, ref_art character varying, reference character varying)
			LOOP
				SELECT INTO article_ y.id FROM yvs_base_articles y WHERE y.ref_art = composants_.ref_art;
				RAISE NOTICE '	article_ : %, %', composants_.ref_art, article_;
				IF(COALESCE(article_, 0) > 1)THEN
					SELECT INTO composant_ y.id FROM yvs_prod_composant_nomenclature y WHERE y.nomenclature = nomenclature_ AND y.article = article_;
					IF(COALESCE(composant_, 0) < 1)THEN
						conditionnement_ = NULL;
						IF(composants_.unite IS NOT NULL AND composants_.reference IS NOT NULL)THEN
							SELECT INTO conditionnement_ y.id FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = article_ AND u.reference = composants_.reference;
							IF(COALESCE(conditionnement_, 0) < 1)THEN
								SELECT INTO unite_ y.id FROM yvs_base_unite_mesure y WHERE y.reference = composants_.reference AND y.societe = societe_;
								IF(COALESCE(unite_ , 0) < 1)THEN
									INSERT INTO yvs_base_unite_mesure(reference, libelle, societe, type) VALUES (composants_.reference, composants_.reference, societe_, 'Q');
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
						IF(composants_.composant_lie IS NOT NULL)THEN
							query_ = 'SELECT y.*, a.ref_art, u.reference AS reference FROM yvs_prod_composant_nomenclature y INNER JOIN yvs_base_articles a ON y.article = a.id LEFT JOIN yvs_base_conditionnement c ON y.unite = c.id LEFT JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE y.id = '||composants_.composant_lie;
							SELECT INTO datas_ *  FROM dblink('host='||serveur||' dbname='||database||' user='||users||' password='||password, query_)
								AS table_(id integer, quantite double precision, coefficient double precision, type character varying, mode_arrondi character varying, actif boolean, article integer, nomenclature integer, author bigint, 
									unite bigint, date_update timestamp, date_save timestamp, stockable boolean, ordre integer, composant_lie integer, inside_cout boolean, alternatif boolean, execute_trigger character varying, 
									free_use boolean, ref_art character varying, reference character varying);
							SELECT INTO data_ y.id FROM yvs_base_articles y WHERE y.ref_art = datas_.ref_art;
							IF(COALESCE(data_, 0) > 1)THEN
								SELECT INTO composant_ y.id FROM yvs_prod_composant_nomenclature y WHERE y.nomenclature = nomenclature_ AND y.article = data_;
								IF(COALESCE(composant_, 0) < 1)THEN
									other_ = NULL;
									IF(datas_.unite IS NOT NULL AND datas_.reference IS NOT NULL)THEN
										SELECT INTO other_ y.id FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = data_ AND u.reference = datas_.reference;
										IF(COALESCE(other_, 0) < 1)THEN
											SELECT INTO unite_ y.id FROM yvs_base_unite_mesure y WHERE y.reference = datas_.reference AND y.societe = societe_;
											IF(COALESCE(unite_ , 0) < 1)THEN
												INSERT INTO yvs_base_unite_mesure(reference, libelle, societe, type) VALUES (datas_.reference, datas_.reference, societe_, 'Q');
												unite_ = currval('yvs_prod_unite_masse_id_seq');
											END IF;
											IF(COALESCE(unite_, 0) > 1)THEN
												INSERT INTO yvs_base_conditionnement(article, unite, by_prod, actif) VALUES (datas_, unite_, true, true);
												other_ = currval('yvs_base_conditionnement_id_seq');
											END IF;
										END IF;
										IF(COALESCE(other_, 0) < 1)THEN
											continue ;
										END IF;
									END IF;
									INSERT INTO yvs_prod_composant_nomenclature(quantite, coefficient, type, mode_arrondi, actif, article, nomenclature, 
										unite, date_update, date_save, stockable, ordre, composant_lie, inside_cout, alternatif, free_use)
									VALUES (composants_.quantite, composants_.coefficient, composants_.type, composants_.mode_arrondi, composants_.actif, data_, nomenclature_, other_, 
										composants_.date_update, composants_.date_save, composants_.stockable, composants_.ordre, null, composants_.inside_cout, composants_.alternatif, composants_.free_use);
									composant_ = currval('yvs_prod_composant_nomenclature_id_seq');
								END IF;
							END IF;
						END IF;
						INSERT INTO yvs_prod_composant_nomenclature(quantite, coefficient, type, mode_arrondi, actif, article, nomenclature, 
							unite, date_update, date_save, stockable, ordre, composant_lie, inside_cout, alternatif, free_use)
						VALUES (composants_.quantite, composants_.coefficient, composants_.type, composants_.mode_arrondi, composants_.actif, article_, nomenclature_, conditionnement_, 
							composants_.date_update, composants_.date_save, composants_.stockable, composants_.ordre, composant_, composants_.inside_cout, composants_.alternatif, composants_.free_use);
						composant_ = currval('yvs_prod_composant_nomenclature_id_seq');
					END IF;
				END IF;
			END LOOP;
		END LOOP;
	END LOOP;
	return true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION import_nomenclature(bigint, character varying, integer, character varying, character varying, character varying)
  OWNER TO postgres;
