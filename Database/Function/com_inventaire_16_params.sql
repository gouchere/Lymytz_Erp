-- Function: com_inventaire(bigint, bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint, boolean, boolean)
-- DROP FUNCTION com_inventaire(bigint, bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint, boolean, boolean);
CREATE OR REPLACE FUNCTION com_inventaire(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN emplacement_ bigint, IN famille_ bigint, IN categorie_ character varying, IN groupe_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying, IN article_ character varying, IN offset_ bigint, IN limit_ bigint, IN preparatoire_ boolean, IN with_child_ boolean)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, emplacement bigint, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision, "position" double precision, count double precision) AS
$BODY$
DECLARE 
	articles_ RECORD;
	unites_ RECORD;

	insert_ BOOLEAN DEFAULT false;

	unite_ BIGINT DEFAULT 0;
	count_ BIGINT DEFAULT 0;
	i_count_ BIGINT DEFAULT 0;
	position_ BIGINT DEFAULT 0;
	sous_ BIGINT DEFAULT 0;
	
	prix_ DOUBLE PRECISION DEFAULT 0;
	stock_ DOUBLE PRECISION DEFAULT 0;
	reservation_ DOUBLE PRECISION DEFAULT 0;
	reste_a_livre_ DOUBLE PRECISION DEFAULT 0;

	colonne_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT y.id, y.article, y.depot, a.ref_art, a.designation, f.reference_famille, f.designation as famille, d.agence, y.actif, 
		c.id AS unite, u.id AS mesure, u.reference, COALESCE(c.prix, a.puv) AS puv, COALESCE(c.prix_achat, a.pua) AS pua, e.emplacement AS emplacement';
	query_ CHARACTER VARYING DEFAULT ' FROM yvs_base_article_depot y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON f.id = a.famille 
		INNER JOIN yvs_base_depots d ON y.depot = d.id INNER JOIN yvs_base_conditionnement c ON c.article = a.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id
		LEFT JOIN yvs_base_article_emplacement e ON y.id = e.article
		 WHERE y.article IS NOT NULL AND f.societe = '||societe_;
	ids_ character varying default '0';

	debut_ timestamp default current_timestamp;
	fin_ timestamp default current_timestamp;
	entree_ double precision; 
	sortie_ double precision; 
	
BEGIN 	
	DROP TABLE IF EXISTS table_inventaire_preparatoire;
	CREATE TEMP TABLE IF NOT EXISTS table_inventaire_preparatoire(_depot bigint, _article bigint, _code character varying, _designation character varying, numero_ character varying, _famille character varying, _unite bigint, _reference character varying, _emplacement bigint, prix_ double precision, _puv double precision, _pua double precision, _pr double precision, _stock double precision, _reservation double precision, _reste_a_livre double precision, _position double precision, _count double precision); 
	DELETE FROM table_inventaire_preparatoire;
	IF(depot_ IS NOT NULL AND depot_ > 0)THEN
		query_ = query_ || ' AND d.id = '||depot_;
	ELSIF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND d.agence = '||agence_;
	END IF;
	IF(famille_ IS NOT NULL AND famille_ > 0)THEN
		IF(with_child_)THEN
			query_ = query_ || ' AND a.famille IN ('||famille_;
			FOR sous_ IN SELECT base_get_sous_famille(famille_, true)
			LOOP
				query_ = query_ || ', '||sous_;
			END LOOP;
			query_ = query_ || ')';
		ELSE
			query_ = query_ || ' AND a.famille = '||famille_;
		END IF;
	END IF;
	IF(groupe_ IS NOT NULL AND groupe_ > 0)THEN
		query_ = query_ || ' AND a.groupe = '||groupe_;
	END IF;
	IF(TRIM(COALESCE(categorie_, '')) NOT IN ('', ' '))THEN
		query_ = query_ || ' AND a.categorie = '||QUOTE_LITERAL(categorie_);
	END IF;
	ids_ = '0';
	IF(article_ IS NOT NULL AND article_ NOT IN ('', ' '))THEN
		FOR articles_ IN select val from regexp_split_to_table(article_,',') val
		LOOP
			ids_ = ids_ ||','||articles_.val;
		END LOOP;
		query_ = query_ || ' AND a.id in ('||ids_||')';
	END IF;
	IF(emplacement_ IS NOT NULL AND emplacement_ > 0)THEN
		query_ = query_ || ' AND e.emplacement = '||emplacement_;
	END IF;
	EXECUTE 'SELECT COUNT(DISTINCT(c.id)) ' || query_ INTO count_;
	query_ = query_ || ' ORDER BY a.designation';
	IF(limit_ IS NOT NULL AND limit_ > 0)THEN
		query_ = query_ || ' offset '||offset_;
	END IF;
	RAISE NOTICE '%',query_;
	option_print_ = COALESCE(option_print_, '');
	FOR articles_ IN EXECUTE colonne_ || query_
	LOOP
		debut_ = current_timestamp;
		position_ = position_ + 1;
		insert_ = false;
		
		stock_ = (SELECT get_stock(articles_.article, 0, articles_.depot, articles_.agence, 0, date_, articles_.unite, 0));
		IF(COALESCE(preparatoire_, FALSE) IS FALSE)THEN
			SELECT INTO reservation_ SUM(c.quantite) FROM yvs_com_reservation_stock c WHERE c.depot = articles_.depot AND c.article = articles_.article AND c.conditionnement = articles_.unite AND c.statut = 'V' AND c.date_echeance <= date_;
			SELECT INTO reste_a_livre_ ((COALESCE((select sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on a.id=c.article inner join yvs_com_doc_ventes d on d.id=c.doc_vente  inner join yvs_com_entete_doc_vente en on en.id=d.entete_doc
					where d.type_doc = 'FV' and d.statut = 'V' and en.date_entete <= date_ AND c.article = articles_.article AND d.depot_livrer = articles_.depot AND c.conditionnement = articles_.unite limit 1), 0))
				- (COALESCE((select sum(c1.quantite) from yvs_com_contenu_doc_vente c1 inner join yvs_base_articles a1 on a1.id=c1.article inner join yvs_com_doc_ventes d1 on d1.id=c1.doc_vente 
					where d1.type_doc = 'BLV' and d1.statut = 'V' and d1.date_livraison <= date_ AND c1.article = articles_.article AND d1.depot_livrer = articles_.depot AND c1.conditionnement = articles_.unite limit 1), 0)));
		END IF;
		IF(reste_a_livre_ < 0)THEN
			reste_a_livre_ = 0;
		END IF;
		IF(print_all_)THEN
			IF(stock_ != 0)THEN
				insert_ = true;
			ELSE
				insert_ = articles_.actif;					
			END IF;
		ELSE
			IF(option_print_ = 'P')THEN
				IF(stock_ > 0)THEN
					insert_ = true;
				END IF;
			ELSIF(option_print_ = 'N')THEN
				IF(stock_ < 0)THEN
					insert_ = true;
				END IF;
			ELSIF(option_print_ = 'E')THEN
				IF(stock_ = 0)THEN
					insert_ = true;
				END IF;
			ELSE
				IF(stock_ != 0)THEN
					insert_ = true;
				END IF;
			END IF;
		END IF;
		IF(insert_)THEN
			IF(type_ = 'A')THEN
				prix_ = COALESCE((SELECT get_pua(articles_.article, 0, depot_, articles_.unite)), 0);
			ELSIF(type_ = 'V')THEN
				prix_ = articles_.puv;
			ELSE
				prix_ = COALESCE((SELECT get_pr(articles_.article, depot_, 0, date_, articles_.unite)), 0);
			END IF;
			i_count_ = i_count_ + 1;
			INSERT INTO table_inventaire_preparatoire VALUES(articles_.depot, articles_.article, articles_.ref_art, articles_.designation, articles_.reference_famille, articles_.famille, articles_.mesure, articles_.reference, articles_.emplacement, prix_, articles_.puv, articles_.pua, 0, stock_, COALESCE(reservation_, 0), COALESCE(reste_a_livre_, 0), position_, COALESCE(count_, 0));
		END IF;
		IF(limit_ IS NOT NULL AND limit_ > 0)THEN
			if(i_count_ >= limit_)THEN	
				EXIT;
			END IF;
		END IF;
		fin_ = current_timestamp;
		RAISE NOTICE '% : %ms',articles_.ref_art, (fin_ - debut_);
	END LOOP;
	RETURN QUERY SELECT * FROM table_inventaire_preparatoire ORDER BY _designation, _code, _depot, _famille;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire(bigint, bigint, bigint, bigint, bigint, character varying, bigint, date, boolean, character varying, character varying, character varying, bigint, bigint, boolean, boolean)
  OWNER TO postgres;
