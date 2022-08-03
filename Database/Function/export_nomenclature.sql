-- Function: export_nomenclature(bigint, character varying, integer, character varying, character varying, character varying)
-- DROP FUNCTION export_nomenclature(bigint, character varying, integer, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION export_nomenclature(societe_ bigint, serveur character varying, port integer, database character varying, users character varying, password character varying)
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
	query_ = 'SELECT a.id, a.ref_art FROM yvs_base_articles a INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE a.categorie IN (''PF'', ''PSF'') AND f.societe = '||QUOTE_LITERAL(societe_);
	FOR articles_ IN SELECT id, ref_art FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint, ref_art character varying)
	LOOP
		FOR nomenclatures_ IN SELECT n.*, u.reference AS unite FROM yvs_prod_nomenclature n INNER JOIN yvs_base_articles a ON n.article = a.id LEFT JOIN yvs_base_conditionnement c ON n.unite_mesure = c.id LEFT JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE a.ref_art = articles_.ref_art
		LOOP
			query_ = 'SELECT y.id FROM yvs_prod_nomenclature y WHERE y.reference = '||QUOTE_LITERAL(nomenclatures_.reference)||' AND y.article = '||QUOTE_LITERAL(articles_.id);
			SELECT INTO nomenclature_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
			
			IF(COALESCE(nomenclature_, 0) < 1)THEN
				conditionnement_ = NULL;
				IF(nomenclatures_.unite_mesure IS NOT NULL AND nomenclatures_.unite IS NOT NULL)THEN
					query_ = 'SELECT y.id FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = '||QUOTE_LITERAL(articles_.id) ||' AND u.reference = '||QUOTE_LITERAL(nomenclatures_.unite);
					SELECT INTO conditionnement_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
					IF(COALESCE(conditionnement_, 0) < 1)THEN
						query_ = 'SELECT y.id FROM yvs_base_unite_mesure y WHERE y.reference = '||QUOTE_LITERAL(nomenclatures_.unite)||' AND y.societe = '||QUOTE_LITERAL(societe_);
						SELECT INTO unite_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
						IF(COALESCE(unite_ , 0) < 1)THEN
							query_ = 'INSERT INTO yvs_base_unite_mesure(reference, libelle, societe, type) VALUES ('||QUOTE_LITERAL(nomenclatures_.unite)||', '||QUOTE_LITERAL(nomenclatures_.unite)||', '||societe_||', ''Q'')';
							PERFORM  dblink_exec('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_);
							query_ = 'SELECT (nextval(''yvs_prod_unite_masse_id_seq''::regclass) - 1)';
							SELECT INTO unite_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
						END IF;
						IF(COALESCE(unite_, 0) > 1)THEN
							query_ = 'INSERT INTO yvs_base_conditionnement(article, unite, by_prod, actif) VALUES ('||QUOTE_LITERAL(articles_.id)||', '||QUOTE_LITERAL(unite_)||', true, true)';
							PERFORM  dblink_exec('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_);
							query_ = 'SELECT (nextval(''yvs_base_conditionnement_id_seq''::regclass) - 1)';
							SELECT INTO conditionnement_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
						END IF;
					END IF;
					IF(COALESCE(conditionnement_, 0) < 1)THEN
						continue ;
					END IF;
				END IF;
				query_ = 'INSERT INTO yvs_prod_nomenclature(reference, niveau, debut_validite, fin_validite, quantite, article, actif, quantite_lie_aux_composants, 
					principal, alway_valide, unite_preference, date_update, date_save, unite_mesure, for_conditionnement, marge_qte, acces, masquer, type_nomenclature) 
				VALUES ('||QUOTE_LITERAL(nomenclatures_.reference)||', '||QUOTE_LITERAL(nomenclatures_.niveau)||', '||QUOTE_LITERAL(nomenclatures_.debut_validite)||', '||QUOTE_LITERAL(nomenclatures_.fin_validite)||', '||QUOTE_LITERAL(nomenclatures_.quantite)||', '||QUOTE_LITERAL(articles_.id)||',
					'||QUOTE_LITERAL(nomenclatures_.actif)||', '||QUOTE_LITERAL(nomenclatures_.quantite_lie_aux_composants)||', '||QUOTE_LITERAL(nomenclatures_.principal)||', '||QUOTE_LITERAL(nomenclatures_.alway_valide)||', 
					'||QUOTE_LITERAL(nomenclatures_.unite_preference)||', '||QUOTE_LITERAL(nomenclatures_.date_update)||', '||QUOTE_LITERAL(nomenclatures_.date_save)||', '||QUOTE_LITERAL(conditionnement_)||', '||QUOTE_LITERAL(nomenclatures_.for_conditionnement)||', 
					'||QUOTE_LITERAL(nomenclatures_.marge_qte)||', '||QUOTE_LITERAL(nomenclatures_.acces)||', '||QUOTE_LITERAL(nomenclatures_.masquer)||', '||QUOTE_LITERAL(nomenclatures_.type_nomenclature)||')';
				PERFORM  dblink_exec('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_);
				query_ = 'SELECT (nextval(''yvs_prod_nomenclature_id_seq''::regclass) - 1)';
				SELECT INTO nomenclature_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
				RAISE NOTICE 'nomenclature_ : %',nomenclature_;
			ELSE
				query_ = 'DELETE FROM yvs_prod_composant_nomenclature WHERE nomenclature = '||QUOTE_LITERAL(nomenclature_);
				PERFORM  dblink_exec('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_);
			END IF;
			FOR composants_ IN SELECT y.*, a.ref_art, u.reference AS reference FROM yvs_prod_composant_nomenclature y INNER JOIN yvs_base_articles a ON y.article = a.id LEFT JOIN yvs_base_conditionnement c ON y.unite = c.id LEFT JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE y.nomenclature = nomenclatures_.id
			LOOP
				query_ = 'SELECT y.id FROM yvs_base_articles y WHERE y.ref_art = '||QUOTE_LITERAL(composants_.ref_art);
				SELECT INTO article_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
				IF(COALESCE(article_, 0) > 1)THEN
					query_ = 'SELECT y.id FROM yvs_prod_composant_nomenclature y WHERE y.nomenclature = '||QUOTE_LITERAL(nomenclature_)||' AND y.article ='||QUOTE_LITERAL(article_);
					SELECT INTO composant_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
					IF(COALESCE(composant_, 0) < 1)THEN
						conditionnement_ = NULL;
						IF(composants_.unite IS NOT NULL AND composants_.reference IS NOT NULL)THEN
							query_ = 'SELECT y.id FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = '||QUOTE_LITERAL(article_)||' AND u.reference ='||QUOTE_LITERAL(composants_.reference);
							SELECT INTO conditionnement_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
							IF(COALESCE(conditionnement_, 0) < 1)THEN
								query_ = 'SELECT y.id FROM yvs_base_unite_mesure y WHERE y.reference = '||QUOTE_LITERAL(composants_.reference)||' AND y.societe = '||QUOTE_LITERAL(societe_);
								SELECT INTO unite_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
								IF(COALESCE(unite_ , 0) < 1)THEN
									query_ = 'INSERT INTO yvs_base_unite_mesure(reference, libelle, societe, type) VALUES ('||QUOTE_LITERAL(composants_.reference)||', '||QUOTE_LITERAL(composants_.reference)||', '||societe_||', ''Q'')';
									PERFORM  dblink_exec('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_);
									query_ = 'SELECT (nextval(''yvs_prod_unite_masse_id_seq''::regclass) - 1)';
									SELECT INTO unite_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
								END IF;
								IF(COALESCE(unite_, 0) > 1)THEN
									query_ = 'INSERT INTO yvs_base_conditionnement(article, unite, by_prod, actif) VALUES ('||QUOTE_LITERAL(article_)||', '||QUOTE_LITERAL(unite_)||', true, true)';
									PERFORM  dblink_exec('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_);
									query_ = 'SELECT (nextval(''yvs_base_conditionnement_id_seq''::regclass) - 1)';
									SELECT INTO conditionnement_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
								END IF;
							END IF;
							IF(COALESCE(conditionnement_, 0) < 1)THEN
								continue ;
							END IF;
						END IF;
						IF(composants_.composant_lie IS NOT NULL)THEN
							SELECT INTO datas_ y.*, a.ref_art, u.reference AS reference FROM yvs_prod_composant_nomenclature y INNER JOIN yvs_base_articles a ON y.article = a.id LEFT JOIN yvs_base_conditionnement c ON y.unite = c.id LEFT JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE y.id = composants_.composant_lie;
							query_ = 'SELECT y.id FROM yvs_base_articles y WHERE y.ref_art = '||QUOTE_LITERAL(datas_.ref_art);
							SELECT INTO data_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
							IF(COALESCE(data_, 0) > 1)THEN
								query_ = 'SELECT y.id FROM yvs_prod_composant_nomenclature y WHERE y.nomenclature = '||QUOTE_LITERAL(nomenclature_)||' AND y.article ='||QUOTE_LITERAL(data_);
								SELECT INTO composant_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
								IF(COALESCE(composant_, 0) < 1)THEN
									other_ = NULL;
									IF(datas_.unite IS NOT NULL AND datas_.reference IS NOT NULL)THEN
										query_ = 'SELECT y.id FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = '||QUOTE_LITERAL(data_)||' AND u.reference ='||QUOTE_LITERAL(datas_.reference);
										SELECT INTO other_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
										IF(COALESCE(other_, 0) < 1)THEN
											query_ = 'SELECT y.id FROM yvs_base_unite_mesure y WHERE y.reference = '||QUOTE_LITERAL(datas_.reference)||' AND y.societe = '||QUOTE_LITERAL(societe_);
											SELECT INTO unite_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
											IF(COALESCE(unite_ , 0) < 1)THEN
												query_ = 'INSERT INTO yvs_base_unite_mesure(reference, libelle, societe, type) VALUES ('||QUOTE_LITERAL(datas_.reference)||', '||QUOTE_LITERAL(datas_.reference)||', '||societe_||', ''Q'')';
												PERFORM  dblink_exec('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_);
												query_ = 'SELECT (nextval(''yvs_prod_unite_masse_id_seq''::regclass) - 1)';
												SELECT INTO unite_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
											END IF;
											IF(COALESCE(unite_, 0) > 1)THEN
												query_ = 'INSERT INTO yvs_base_conditionnement(article, unite, by_prod, actif) VALUES ('||QUOTE_LITERAL(datas_.id)||', '||QUOTE_LITERAL(unite_)||', true, true)';
												PERFORM  dblink_exec('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_);
												query_ = 'SELECT (nextval(''yvs_base_conditionnement_id_seq''::regclass) - 1)';
												SELECT INTO conditionnement_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
											END IF;
										END IF;
										IF(COALESCE(other_, 0) < 1)THEN
											continue ;
										END IF;
									END IF;
									query_ = 'INSERT INTO yvs_prod_composant_nomenclature(quantite, coefficient, type, mode_arrondi, actif, article, nomenclature, 
										unite, date_update, date_save, stockable, ordre, composant_lie, inside_cout, alternatif, free_use)
									VALUES ('||QUOTE_LITERAL(composants_.quantite)||', '||QUOTE_LITERAL(composants_.coefficient)||', '||QUOTE_LITERAL(composants_.type)||', '||QUOTE_LITERAL(composants_.mode_arrondi)||', '||QUOTE_LITERAL(composants_.actif)||', '||QUOTE_LITERAL(data_, nomenclature_)||', '||QUOTE_LITERAL(other_)||', 
										'||QUOTE_LITERAL(composants_.date_update)||', '||QUOTE_LITERAL(composants_.date_save)||', '||QUOTE_LITERAL(composants_.stockable)||', '||QUOTE_LITERAL(composants_.ordre)||', null, '||QUOTE_LITERAL(composants_.inside_cout)||', '||QUOTE_LITERAL(composants_.alternatif)||', '||QUOTE_LITERAL(composants_.free_use)||')';
									PERFORM  dblink_exec('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_);
									query_ = 'SELECT (nextval(''yvs_prod_composant_nomenclature_id_seq''::regclass) - 1)';
									SELECT INTO composant_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
								END IF;
							END IF;
						END IF;
						query_ = 'INSERT INTO yvs_prod_composant_nomenclature(quantite, coefficient, type, mode_arrondi, actif, article, nomenclature, 
							unite, date_update, date_save, stockable, ordre, composant_lie, inside_cout, alternatif, free_use)
						VALUES ('||QUOTE_LITERAL(composants_.quantite)||', '||QUOTE_LITERAL(composants_.coefficient)||', '||QUOTE_LITERAL(composants_.type)||', '||QUOTE_LITERAL(composants_.mode_arrondi)||', '||QUOTE_LITERAL(composants_.actif)||', '||QUOTE_LITERAL(article_)||', '||QUOTE_LITERAL(nomenclature_)||', '||QUOTE_LITERAL(conditionnement_)||', 
							'||QUOTE_LITERAL(composants_.date_update)||', '||QUOTE_LITERAL(composants_.date_save)||', '||QUOTE_LITERAL(composants_.stockable)||', '||QUOTE_LITERAL(composants_.ordre)||', '||QUOTE_LITERAL(composant_)||', '||QUOTE_LITERAL(composants_.inside_cout)||', '||QUOTE_LITERAL(composants_.alternatif)||', '||QUOTE_LITERAL(composants_.free_use)||')';
						PERFORM  dblink_exec('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_);
						query_ = 'SELECT (nextval(''yvs_prod_composant_nomenclature_id_seq''::regclass) - 1)';
						SELECT INTO composant_ id FROM dblink('host='||serveur||' port='||port||' dbname='||database||' user='||users||' password='||password, query_) AS table_(id bigint);
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
ALTER FUNCTION export_nomenclature(bigint, character varying, integer, character varying, character varying, character varying)
  OWNER TO postgres;
