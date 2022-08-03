-- Function: com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
-- DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN agence_ bigint, IN depot_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision) AS
$BODY$
DECLARE 
BEGIN 	
	RETURN QUERY SELECT * FROM com_inventaire_preparatoire(agence_, depot_, '', date_, print_all_, option_print_, type_);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
  OWNER TO postgres;


-- Function: com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
-- DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN agence_ bigint, IN depot_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision, reservation double precision, reste_a_livre double precision) AS
$BODY$
DECLARE 
	articles_ RECORD;
	unites_ RECORD;

	insert_ BOOLEAN DEFAULT false;

	unite_ BIGINT DEFAULT 0;
	
	prix_ DOUBLE PRECISION DEFAULT 0;
	stock_ DOUBLE PRECISION DEFAULT 0;
	reservation_ DOUBLE PRECISION DEFAULT 0;
	reste_a_livre_ DOUBLE PRECISION DEFAULT 0;

	query_ CHARACTER VARYING DEFAULT 'SELECT y.id, y.article, y.depot, a.ref_art, a.designation, f.reference_famille, f.designation as famille, a.pua, a.puv, d.agence, y.actif FROM yvs_base_article_depot y 
		INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON f.id = a.famille INNER JOIN yvs_base_depots d ON y.depot = d.id WHERE y.article IS NOT NULL';
	
BEGIN 	
	DROP TABLE IF EXISTS table_inventaire_preparatoire;
	CREATE TEMP TABLE IF NOT EXISTS table_inventaire_preparatoire(_depot bigint, _article bigint, _code character varying, _designation character varying, numero_ character varying, _famille character varying, _unite bigint, _reference character varying, prix_ double precision, _puv double precision, _pua double precision, _pr double precision, _stock double precision, _reservation double precision, _reste_a_livre double precision); 
	DELETE FROM table_inventaire_preparatoire;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND d.agence = '||agence_;
	END IF;
	IF(depot_ IS NOT NULL AND depot_ > 0)THEN
		query_ = query_ || ' AND d.id = '||depot_;
	END IF;
	option_print_ = COALESCE(option_print_, '');
-- 	RAISE NOTICE '%',query_;
	FOR articles_ IN EXECUTE query_
	LOOP
		FOR unites_ IN SELECT y.id, y.unite, u.reference, COALESCE(y.prix, articles_.puv) AS puv, COALESCE(y.prix_achat, articles_.pua) AS pua FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = articles_.article
		LOOP
			insert_ = false;
			stock_ = (SELECT get_stock(articles_.article, 0, articles_.depot, articles_.agence, 0, date_, unites_.id, 0));
			SELECT INTO reservation_ SUM(c.quantite) FROM yvs_com_reservation_stock c WHERE c.depot = articles_.depot AND c.article = articles_.article AND c.conditionnement = unites_.id AND c.statut = 'V' AND c.date_echeance <= date_;
			SELECT INTO reste_a_livre_ ((COALESCE((select sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_base_articles a on a.id=c.article inner join yvs_com_doc_ventes d on d.id=c.doc_vente  inner join yvs_com_entete_doc_vente en on en.id=d.entete_doc
					where d.type_doc = 'FV' and d.statut = 'V' and en.date_entete <= date_ AND c.article = articles_.article AND d.depot_livrer = articles_.depot AND c.conditionnement = unites_.id limit 1), 0))
				- (COALESCE((select sum(c1.quantite) from yvs_com_contenu_doc_vente c1 inner join yvs_base_articles a1 on a1.id=c1.article inner join yvs_com_doc_ventes d1 on d1.id=c1.doc_vente 
					where d1.type_doc = 'BLV' and d1.statut = 'V' and d1.date_livraison <= date_ AND c1.article = articles_.article AND d1.depot_livrer = articles_.depot AND c1.conditionnement = unites_.id limit 1), 0)));
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
				ELSE
					IF(stock_ != 0)THEN
						insert_ = true;
					END IF;
				END IF;
			END IF;
			IF(insert_)THEN
				IF(type_ = 'A')THEN
					prix_ = COALESCE((SELECT get_pua(articles_.article, 0, depot_, unites_.id)), 0);
				ELSIF(type_ = 'V')THEN
					prix_ = unites_.puv;
				ELSE
					prix_ = COALESCE((SELECT get_pr(articles_.article, depot_, 0, date_, unites_.id)), 0);
				END IF;
				INSERT INTO table_inventaire_preparatoire VALUES(articles_.depot, articles_.article, articles_.ref_art, articles_.designation, articles_.reference_famille, articles_.famille, unites_.unite, unites_.reference, prix_, unites_.puv, unites_.pua, 0, stock_, COALESCE(reservation_, 0), COALESCE(reste_a_livre_, 0));
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_inventaire_preparatoire ORDER BY _depot, _famille, _code;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
  OWNER TO postgres;


-- Function: com_et_valorise_stock(date, character varying, character varying, character varying, bigint)
-- DROP FUNCTION com_et_valorise_stock(date, character varying, character varying, character varying, bigint);
CREATE OR REPLACE FUNCTION com_et_valorise_stock(IN date_ date, IN categorie_ character varying, IN depots_ character varying, IN groupe_by_ character varying, IN societe_ bigint)
  RETURNS TABLE(id bigint, reference character varying, designation character varying, unite character varying, code character varying, depot character varying, quantite double precision, prix_revient double precision, prix_vente double precision, prix_achat double precision) AS
$BODY$
DECLARE

BEGIN
	return QUERY select * from com_et_valorise_stock(date_, categorie_, depots_, '', groupe_by_, societe_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_valorise_stock(date, character varying, character varying, character varying, bigint)
  OWNER TO postgres;



-- Function: com_et_valorise_stock(date, character varying, character varying, character varying, character varying, bigint)
-- DROP FUNCTION com_et_valorise_stock(date, character varying, character varying, character varying, character varying, bigint);
CREATE OR REPLACE FUNCTION com_et_valorise_stock(IN date_ date, IN categorie_ character varying, IN depots_ character varying, IN article_ character varying, IN groupe_by_ character varying, IN societe_ bigint)
  RETURNS TABLE(id bigint, reference character varying, designation character varying, unite character varying, code character varying, depot character varying, quantite double precision, prix_revient double precision, prix_vente double precision, prix_achat double precision) AS
$BODY$
DECLARE
	depot_ record;
	articles_ record;
	familles_ record;
	unite_ record;
	
	query_ character varying default 'SELECT a.id, a.ref_art as code, a.designation FROM yvs_base_articles a INNER JOIN yvs_base_famille_article f On a.famille = f.id WHERE f.societe = '||societe_;
	query_depot_ character varying default 'SELECT id, code, designation FROM yvs_base_depots';
	ids_ character varying default '0';

	ct_ double precision default 0;
	st_ double precision default 0;
	pr_ double precision default 0;
	pv_ double precision default 0;
	pa_ double precision default 0;
	
	i_ integer default 0;

BEGIN
	DROP TABLE IF EXISTS table_valorise_stock;
	CREATE TEMP TABLE IF NOT EXISTS table_valorise_stock(_id bigint, _reference character varying, _designation character varying, unite_ character varying, _code character varying, _depot character varying, _quantite double precision, _prix_revient double precision, _prix_vente double precision, _prix_achat double precision);
	if(societe_ is not null and societe_ > 0)then
		IF(depots_ IS NOT NULL AND depots_ NOT IN ('', ' '))THEN
			FOR depot_ IN select val from regexp_split_to_table(depots_,',') val
			LOOP
				ids_ = ids_ ||','||depot_.val;
			END LOOP;
			query_depot_ = query_depot_ || ' WHERE id in ('||ids_||')';
		END IF;
		query_depot_ = query_depot_ || ' ORDER BY code';
		IF(categorie_ IS NOT NULL AND categorie_ NOT IN ('', ' '))THEN
			query_ = query_ || ' AND categorie = '''||categorie_||'''';
		END IF;
		ids_ = '0';
		IF(article_ IS NOT NULL AND article_ NOT IN ('', ' '))THEN
			FOR articles_ IN select val from regexp_split_to_table(article_,',') val
			LOOP
				ids_ = ids_ ||','||articles_.val;
			END LOOP;
			query_ = query_ || ' AND a.id in ('||ids_||')';
		END IF;
		query_ = query_ || ' ORDER BY ref_art';
		
		IF(groupe_by_ = 'F')THEN
			FOR familles_ IN SELECT f.id, f.reference_famille as code, f.designation FROM yvs_base_famille_article f WHERE f.societe = societe_
			LOOP			
				FOR depot_ IN EXECUTE query_depot_
				LOOP
					st_ = 0;
					pr_ = 0;
					pv_ = 0;
					pa_ = 0;
					i_ = 0;
					
					FOR articles_ IN SELECT a.id, a.ref_art as code, a.designation FROM yvs_base_articles a WHERE famille = familles_.id
					LOOP
						FOR unite_ IN SELECT c.id, u.reference as code FROM yvs_base_conditionnement c INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE c.article = articles_.id
						LOOP
							ct_ = COALESCE((SELECT get_stock_reel(articles_.id, 0, depot_.id, 0, 0, date_, unite_.id)), 0);
							IF(ct_ != 0)THEN
								st_ = st_ + ct_;
								pr_ = pr_ + COALESCE((SELECT get_pr(articles_.id, depot_.id, 0, date_, unite_.id)), 0);
								pv_ = pv_ + COALESCE((SELECT get_puv(articles_.id, 0, 0, 0, depot_.id, 0, date_, unite_.id, false)), 0);
								pa_ = pa_ + COALESCE((SELECT get_pua(articles_.id, 0, depot_.id, unite_.id)), 0);
								i_ = i_ + 1;
							END IF;
						END LOOP;
					END LOOP;
					IF(st_ != 0)THEN
						IF(pr_ != 0)THEN
							pr_ = pr_ / i_;
						END IF;
						IF(pv_ != 0)THEN
							pv_ = pv_ / i_;
						END IF;
						IF(pa_ != 0)THEN
							pa_ = pr_ / i_;
						END IF;
						INSERT INTO table_valorise_stock VALUES(familles_.id, familles_.code, familles_.designation, '', depot_.code, depot_.designation, st_, pr_, pv_, pa_);
					END IF;
				END LOOP;
			END LOOP;
		ELSE		
			FOR articles_ IN EXECUTE query_
			LOOP				
				FOR depot_ IN EXECUTE query_depot_
				LOOP
					FOR unite_ IN SELECT c.id, u.reference as code FROM yvs_base_conditionnement c INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE c.article = articles_.id
					LOOP
						st_ = COALESCE((SELECT get_stock_reel(articles_.id, 0, depot_.id, 0, 0, date_, unite_.id)), 0);
						IF(st_ != 0)THEN
							pr_ = COALESCE((SELECT get_pr(articles_.id, depot_.id, 0, date_, unite_.id)), 0);
							pv_ = COALESCE((SELECT get_puv(articles_.id, 0, 0, 0, depot_.id, 0, date_, unite_.id, false)), 0);
							pa_ = COALESCE((SELECT get_pua(articles_.id, 0, depot_.id, unite_.id)), 0);

							INSERT INTO table_valorise_stock VALUES(articles_.id, articles_.code, articles_.designation, unite_.code, depot_.code, depot_.designation, st_, pr_, pv_, pa_);
						END IF;
					END LOOP;
				END LOOP;			
			END LOOP;
		END IF;
	end if;
	return QUERY select * from table_valorise_stock order by _code, _reference;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_valorise_stock(date, character varying, character varying, character varying, character varying, bigint)
  OWNER TO postgres;


-- Function: com_get_versement_attendu(character varying)
-- DROP FUNCTION com_get_versement_attendu(character varying);
CREATE OR REPLACE FUNCTION com_get_versement_attendu(headers_ character varying)
  RETURNS double precision AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;

	cs_m double precision default 0;
	cs_p double precision default 0;
	cs_ double precision default 0;

	head_ RECORD;
BEGIN    
	-- Recupere le montant TTC du contenu de la facture
	select into ca_ sum(c.prix_total) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
	inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id 
	where d.entete_doc::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head) and d.type_doc = 'FV' and d.statut = 'V'
	and d.document_lie is null;
	
	-- Recupere le total des couts de service supplementaire d'une facture
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id 
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where d.entete_doc::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head) 
	and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is true and o.service = true and (d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where d.entete_doc::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head) 
	and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is false and o.service = true and (d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	cs_  = COALESCE(cs_p, 0) - COALESCE(cs_m, 0);
	ca_ = COALESCE(ca_, 0) + COALESCE(cs_, 0);
	
	FOR head_ IN SELECT h.users, e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id WHERE e.id::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head)
	LOOP
		SELECT INTO result_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) AND p.caissier = head_.users AND p.date_paiement = head_.date_entete;
		RAISE NOTICE 'result_ : %',result_;
		avance_ = avance_ + COALESCE(result_, 0);
	END LOOP;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avances : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu(character varying)
  OWNER TO postgres;


-- Function: com_get_versement_attendu_vendeur(bigint, date, date)
-- DROP FUNCTION com_get_versement_attendu_vendeur(bigint, date, date);
CREATE OR REPLACE FUNCTION com_get_versement_attendu_vendeur(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;

	cs_m double precision default 0;
	cs_p double precision default 0;
	cs_ double precision default 0;
	
	head_ RECORD;
BEGIN    
	-- Recupere le montant TTC du contenu de la facture
	select into ca_ sum(c.prix_total) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
	inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id where h.users = id_ and d.type_doc = 'FV' and d.statut = 'V'
	and e.date_entete between date_debut_ and date_fin_ and d.document_lie is null;
	
	-- Recupere le total des couts de service supplementaire d'une facture
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id 
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where h.users = id_ and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is true and o.service = true and e.date_entete between date_debut_ and date_fin_ and
		(d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where h.users = id_ and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is false and o.service = true and e.date_entete between date_debut_ and date_fin_ and
		(d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	cs_  = COALESCE(cs_p, 0) - COALESCE(cs_m, 0);
	ca_ = COALESCE(ca_, 0) + COALESCE(cs_, 0);
	
	FOR head_ IN SELECT h.users, e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point p ON h.creneau_point= p.id 
		WHERE h.users = id_ AND e.date_entete BETWEEN date_debut_ AND date_fin_
	LOOP
		SELECT INTO result_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) AND p.caissier = head_.users AND p.date_paiement = head_.date_entete;
		RAISE NOTICE 'result_ : %',result_;
		avance_ = avance_ + COALESCE(result_, 0);
	END LOOP;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avances : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu_vendeur(bigint, date, date)
  OWNER TO postgres;


-- Function: com_get_versement_attendu_point(bigint, date, date)
-- DROP FUNCTION com_get_versement_attendu_point(bigint, date, date);
CREATE OR REPLACE FUNCTION com_get_versement_attendu_point(id_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;

	cs_m double precision default 0;
	cs_p double precision default 0;
	cs_ double precision default 0;

	head_ RECORD;
BEGIN    
	-- Recupere le montant TTC du contenu de la facture
	select into ca_ sum(c.prix_total) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id
	inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id where p.point = id_ and d.type_doc = 'FV' and d.statut = 'V'
	and e.date_entete between date_debut_ and date_fin_ and d.document_lie is null;
	
	-- Recupere le total des couts de service supplementaire d'une facture
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id 
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where p.point = id_ and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is true and o.service = true and e.date_entete between date_debut_ and date_fin_ and
		(d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id
	inner join yvs_com_entete_doc_vente e on d.entete_doc = e.id inner join yvs_com_creneau_horaire_users h on e.creneau = h.id inner join yvs_com_creneau_point p on h.creneau_point = p.id
	where p.point = id_ and d.type_doc = 'FV' and d.statut = 'V' and t.augmentation is false and o.service = true and e.date_entete between date_debut_ and date_fin_ and
		(d.document_lie is null or (d.document_lie is not null and d.statut_regle in ('R', 'P')));
	
	cs_  = COALESCE(cs_p, 0) - COALESCE(cs_m, 0);
	ca_ = COALESCE(ca_, 0) + COALESCE(cs_, 0);
	
	FOR head_ IN SELECT h.users, e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point p ON h.creneau_point= p.id 
		WHERE p.point = id_ AND e.date_entete BETWEEN date_debut_ AND date_fin_
	LOOP
		SELECT INTO result_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) AND p.caissier = head_.users AND p.date_paiement = head_.date_entete;
		RAISE NOTICE 'result_ : %',result_;
		avance_ = avance_ + COALESCE(result_, 0);
	END LOOP;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avances : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu_point(bigint, date, date)
  OWNER TO postgres;


-- Function: com_et_journal_vente(bigint, bigint, date, date, boolean)
-- DROP FUNCTION com_et_journal_vente(bigint, bigint, date, date, boolean);
CREATE OR REPLACE FUNCTION com_et_journal_vente(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN by_famille_ boolean)
  RETURNS TABLE(agence bigint, users bigint, code character varying, nom character varying, classe bigint, reference character varying, designation character varying, montant double precision, is_classe boolean, is_vendeur boolean, rang integer) AS
$BODY$
declare 
   vendeur_ RECORD;
   classe_ record;
   
   journal_ character varying;
   
   valeur_ double precision default 0;
   totaux_ double precision;
   
   query_ CHARACTER VARYING DEFAULT 'SELECT y.id, y.code_users, y.nom_users as nom, y.agence FROM yvs_users y INNER JOIN yvs_agences a ON y.agence = a.id WHERE code_users IS NOT NULL';
   
begin 	
	--DROP TABLE table_et_journal_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_journal_vente(_agence BIGINT, _users BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _classe BIGINT, _reference CHARACTER VARYING, _designation CHARACTER VARYING, _montant DOUBLE PRECISION, _is_classe BOOLEAN, _is_vendeur BOOLEAN, _rang INTEGER);
	DELETE FROM table_et_journal_vente;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND y.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	FOR vendeur_ IN EXECUTE query_
	LOOP
		IF(COALESCE(by_famille_, FALSE))THEN
			-- Vente directe par famille d'article
			FOR classe_ IN SELECT s.id, UPPER(s.reference_famille) AS code, UPPER(s.designation) AS intitule, SUM(c.prix_total - c.ristourne) AS valeur FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc 
				INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article s ON a.famille = s.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND s.societe = societe_ AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_ GROUP BY s.id
			LOOP
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(classe_.valeur, 0), TRUE, TRUE, 0);
			END LOOP;
		ELSE
			-- Vente directe par classe statistique
			FOR classe_ IN SELECT s.id, UPPER(s.code_ref) AS code, UPPER(s.designation) AS intitule, SUM(c.prix_total - c.ristourne) AS valeur FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc 
				INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_classes_stat s ON a.classe1 = s.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND s.societe = societe_ AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_ GROUP BY s.id
			LOOP
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(classe_.valeur, 0), TRUE, TRUE, 0);	
			END LOOP;
			
			-- CA Des article non classé
			SELECT INTO valeur_ SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
				INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND a.classe1 IS NULL AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), TRUE, TRUE, 0);
			END IF;
		END IF;
		
		-- CA Par vendeur
		SELECT INTO valeur_ SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN (SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
			WHERE hu.users = vendeur_.id AND e.date_entete BETWEEN date_debut_ AND date_fin_ AND d.type_doc = 'FV' AND d.statut = 'V' AND (d.document_lie IS NULL OR (d.document_lie IS NOT NULL AND d.statut_regle IN ('R', 'P'))));	
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -1, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, TRUE, 1);
		
		-- Ristournes vente directe
		SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
			WHERE hu.users = vendeur_.id AND e.date_entete BETWEEN date_debut_ AND date_fin_ AND d.type_doc = 'FV' AND d.statut = 'V' AND (d.document_lie IS NULL OR (d.document_lie IS NOT NULL AND d.statut_regle IN ('R', 'P'))));
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -2, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, TRUE, 2);
		
		-- Commandes Annulées
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'A' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -3, 'CMDE.A', 'CMDE.A', COALESCE(valeur_, 0), FALSE, TRUE, 3);
					
		-- Commandes Recu
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'V' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -4, 'CMDE.R', 'CMDE.R', COALESCE(valeur_, 0), FALSE, TRUE, 4);		
		
		-- Versement attendu	
		SELECT INTO valeur_ com_get_versement_attendu_vendeur(vendeur_.id, date_debut_, date_fin_); 
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, -5, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, TRUE, 5);	

		SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE y._users = vendeur_.id;
		IF(COALESCE(valeur_, 0) = 0)THEN
			DELETE FROM table_et_journal_vente WHERE _users = vendeur_.id;
		END IF;
	END LOOP;
	
	-- LIGNE DES COMMANDE SERVIES PAR CLASSE STAT
	query_ = 'SELECT SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
			WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND d.document_lie IS NOT NULL AND e.date_entete BETWEEN '''||date_debut_||''' AND '''||date_fin_||'''';
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND u.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND g.societe = '||societe_;
	END IF;
	IF(COALESCE(by_famille_, FALSE))THEN
		FOR classe_ IN SELECT c.id, UPPER(c.reference_famille) as code, UPPER(c.designation) as intitule FROM yvs_base_famille_article c WHERE c.societe = societe_
		LOOP			
			EXECUTE query_ || ' AND a.famille = '||classe_.id||')' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			END IF;
		END LOOP;
	ELSE
		FOR classe_ IN SELECT c.id, UPPER(c.code_ref) as code, UPPER(c.designation) as intitule FROM yvs_base_classes_stat c WHERE c.societe = societe_
		LOOP			
			EXECUTE query_ || ' AND a.classe1 = '||classe_.id||')' INTO valeur_;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
			END IF;
		END LOOP;
		
		-- Autres aticles sans classe
		EXECUTE query_ || ' AND a.classe1 IS NULL) ' INTO valeur_;
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), FALSE, FALSE, 0);
		END IF;	
	END IF;
	
	-- CA sur commande reçu
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = 0; 
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -1, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, FALSE, 1);
	
	-- Ristourne sur commande reçu
	SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
		INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
		INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
		WHERE g.id = agence_ AND d.type_doc = 'BCV' AND d.statut = 'V' AND d.statut_livre = 'L' AND d.date_livraison BETWEEN date_debut_ AND date_fin_);
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -2, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, FALSE, 2);
	
	-- CMDE.A sur commande reçu
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -3, 'CMDE.A', 'CMDE.A', 0, FALSE, FALSE, 3);
	
	-- CMDE.R sur commande reçu
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -4, 'CMDE.R', 'CMDE.R', 0, FALSE, FALSE, 4);
	
	-- VERS.ATT sur commande reçu
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = 0 AND _rang > 0; 
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', -5, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, FALSE, 5);

	FOR classe_ IN SELECT _classe, _reference, _designation, _rang, SUM(_montant) AS _valeur FROM table_et_journal_vente GROUP BY _classe, _reference, _designation, _rang
	LOOP 
		INSERT INTO table_et_journal_vente values (agence_, -1, 'TOTAUX','TOTAUX', classe_._classe, classe_._reference, classe_._designation, classe_._valeur, FALSE, FALSE, classe_._rang);
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_journal_vente ORDER BY _agence, _is_vendeur DESC, _code, _rang, _is_classe DESC, _classe;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_journal_vente(bigint, bigint, date, date, boolean)
  OWNER TO postgres;


-- Function: com_et_ecart_vente(bigint, bigint, character varying, date, date, character varying, boolean)
DROP FUNCTION com_et_ecart_vente(bigint, bigint, character varying, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION com_et_ecart_vente(IN societe_ bigint, IN agence_ bigint, IN element_ character varying, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN by_point_ boolean)
  RETURNS TABLE(agence bigint, element bigint, code character varying, nom character varying, entete character varying, versement_attendu double precision, versement_reel double precision, ecart double precision, solde double precision, rang integer, solde_initial boolean) AS
$BODY$
declare 

   vendeur_ RECORD;
   dates_ RECORD;
   
   jour_ character varying;
   ids_ character varying default '0';
   
   versement_attendu_ double precision default 0;
   versement_reel_ double precision default 0;
   ecart_ double precision default 0;
   solde_ double precision default 0;
   
   query_ CHARACTER VARYING DEFAULT '';
   query_montant_ CHARACTER VARYING DEFAULT '';

   i integer default 0;

   date_initial_ DATE DEFAULT '01-01-2000';
   date_debut_initial_ DATE DEFAULT date_debut_ - interval '1 day';
   
begin 	
	DROP TABLE IF EXISTS table_et_ecart_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ecart_vente(_agence bigint, _element bigint, _code character varying, _nom character varying, _entete character varying, _versement_attendu double precision, _versement_reel double precision, _ecart double precision, _solde double precision, _rang integer, _solde_initial boolean);
	DELETE FROM table_et_ecart_vente;
	IF(by_point_)THEN
		query_ = 'SELECT DISTINCT y.id::bigint as id, y.code as code, y.libelle as nom';
	ELSE
		query_ = 'SELECT DISTINCT y.id::bigint as id, y.code_users as code, y.nom_users as nom';
	END IF;
	query_ = query_ || ', y.agence::bigint as agence FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id ';
	IF(by_point_)THEN
		query_ = query_ || ' INNER JOIN yvs_base_point_vente y ON c.point = y.id';
	ELSE
		query_ = query_ || ' INNER JOIN yvs_users y ON h.users = y.id';
	END IF;
	query_ = query_ || ' INNER JOIN yvs_agences a ON y.agence = a.id WHERE e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND y.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	IF(COALESCE(element_, '') NOT IN ('', ' ', '0'))THEN
		FOR jour_ IN SELECT head FROM regexp_split_to_table(element_,',') head
		LOOP
			ids_ = ids_ ||','||jour_;
		END LOOP;
		query_ = query_ || ' AND y.id IN ('||ids_||')';
	END IF;
	RAISE NOTICE 'query_ %',query_;
	FOR vendeur_ IN EXECUTE query_
	LOOP
		i = 0;
		-- SOLDE INITIAL		
		IF(by_point_)THEN
			versement_attendu_ = (SELECT com_get_versement_attendu_point(vendeur_.id, date_initial_, date_debut_initial_));
		ELSE
			versement_attendu_ = (SELECT com_get_versement_attendu_vendeur(vendeur_.id, date_initial_, date_debut_initial_));
		END IF;
		versement_attendu_ = COALESCE(versement_attendu_, 0);
		
		IF(by_point_)THEN
			query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source IN (SELECT DISTINCT h.users FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE c.point = '||vendeur_.id||')';
		ELSE
			query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source = '||vendeur_.id;
		END IF;
		query_montant_ = query_montant_ || ' AND y.statut_piece = ''P'' AND y.date_paiement BETWEEN '||QUOTE_LITERAL(date_initial_)||' AND '||QUOTE_LITERAL(date_debut_initial_);
		EXECUTE query_montant_ INTO versement_reel_;
		versement_reel_ = COALESCE(versement_reel_, 0);
		ecart_ = versement_attendu_ - versement_reel_;
		solde_ = ecart_;
		IF(ecart_ != 0)THEN
			INSERT INTO table_et_ecart_vente VALUES(vendeur_.agence, vendeur_.id::bigint, vendeur_.code, vendeur_.nom, 'ECART INITIAL', versement_attendu_, versement_reel_, ecart_, solde_, i, true);
		END IF;
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_, true)
		LOOP
			i = i + 1;
			jour_ = dates_.intitule;		
			IF(by_point_)THEN
				versement_attendu_ = (SELECT com_get_versement_attendu_point(vendeur_.id, dates_.date_debut, dates_.date_fin));
			ELSE
				versement_attendu_ = (SELECT com_get_versement_attendu_vendeur(vendeur_.id, dates_.date_debut, dates_.date_fin));
			END IF;
			versement_attendu_ = COALESCE(versement_attendu_, 0);
			
			IF(by_point_)THEN
				query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source IN (SELECT DISTINCT h.users FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE c.point = '||vendeur_.id||')';
			ELSE
				query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source = '||vendeur_.id;
			END IF;
			query_montant_ = query_montant_ || ' AND y.statut_piece = ''P'' AND y.date_paiement BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			EXECUTE query_montant_ INTO versement_reel_;
			versement_reel_ = COALESCE(versement_reel_, 0);
			
			ecart_ = versement_attendu_ - versement_reel_;
			solde_ = solde_ + ecart_;
			IF(ecart_ != 0)THEN
				INSERT INTO table_et_ecart_vente VALUES(vendeur_.agence, vendeur_.id::bigint, vendeur_.code, vendeur_.nom, jour_, versement_attendu_, versement_reel_, ecart_, solde_, i, false);
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_ecart_vente ORDER BY _agence, _code, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ecart_vente(bigint, bigint, character varying, date, date, character varying, boolean)
  OWNER TO postgres;
