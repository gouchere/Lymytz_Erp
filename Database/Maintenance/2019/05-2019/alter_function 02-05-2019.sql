-- Function: fusion_data_for_table(character varying, bigint, character varying)
-- DROP FUNCTION fusion_data_for_table(character varying, bigint, character varying);
CREATE OR REPLACE FUNCTION fusion_data_for_table(table_ character varying, new_value bigint, old_value character varying)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	result_ boolean default false;
BEGIN	
	if(table_ = 'yvs_base_conditionnement')then
		ALTER TABLE yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey;
	elsif(table_ = 'yvs_grh_tranche_horaire')then
		ALTER TABLE yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_tranche_fkey;
	elsif(table_ = 'yvs_niveau_acces')then
		DELETE FROM yvs_autorisation_module WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val);
		DELETE FROM yvs_autorisation_page_module WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val);
		DELETE FROM yvs_autorisation_ressources_page WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val);
	elsif(table_ = 'yvs_base_articles')then
		DELETE FROM yvs_base_article_categorie_comptable WHERE COALESCE(article, 0) > 0 AND article::character varying in (select val from regexp_split_to_table(old_value,',') val);
		ALTER TABLE yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_article_fkey;
	end if;
	
	result_ =  fusion_data_for_table_all(table_, new_value, old_value);
	
	if(table_ = 'yvs_base_conditionnement')then
		UPDATE yvs_base_mouvement_stock SET conditionnement = new_value WHERE conditionnement::character varying in (select val from regexp_split_to_table(old_value,',') val);
		ALTER TABLE yvs_base_mouvement_stock
		  ADD CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey FOREIGN KEY (conditionnement)
		      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
		      ON UPDATE CASCADE ON DELETE NO ACTION;
	elsif(table_ = 'yvs_base_articles')then
		UPDATE yvs_base_mouvement_stock SET article = new_value WHERE article::character varying in (select val from regexp_split_to_table(old_value,',') val);
		ALTER TABLE yvs_base_mouvement_stock
		  ADD CONSTRAINT yvs_base_mouvement_stock_article_fkey FOREIGN KEY (article)
		      REFERENCES yvs_base_articles (id) MATCH SIMPLE
		      ON UPDATE CASCADE ON DELETE NO ACTION;
	elsif(table_ = 'yvs_grh_tranche_horaire')then
		UPDATE yvs_base_mouvement_stock SET tranche = new_value WHERE tranche::character varying in (select val from regexp_split_to_table(old_value,',') val);
		ALTER TABLE yvs_base_mouvement_stock
		  ADD CONSTRAINT yvs_base_mouvement_stock_tranche_fkey FOREIGN KEY (tranche)
		      REFERENCES yvs_grh_tranche_horaire (id) MATCH SIMPLE
		      ON UPDATE CASCADE ON DELETE NO ACTION;
	elsif(table_ = 'yvs_base_tiers')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'TIERS';
	elsif(table_ = 'yvs_base_fournisseur')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'FOURNISSEUR';
	elsif(table_ = 'yvs_com_client')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'CLIENT';
	elsif(table_ = 'yvs_grh_employes')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'EMPLOYE';
	end if;
	return result_;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fusion_data_for_table(character varying, bigint, character varying)
  OWNER TO postgres;

  
-- Function: equilibre_doc_divers(bigint)
-- DROP FUNCTION equilibre_doc_divers(bigint);
CREATE OR REPLACE FUNCTION equilibre_doc_divers(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	doc_ RECORD;
	ttc_ double precision default 0;
	cout_ double precision default 0;
	paye_ bigint default 0;
	statut_regle_ character varying ='W';
	statut_ character varying ='W';

BEGIN
	-- montant total du document: c'est le montant du doc + le montant des charges supplémentaire
	SELECT INTO doc_ y.* FROM yvs_compta_caisse_doc_divers y WHERE y.id = id_;
	SELECT INTO cout_ COALESCE((SELECT SUM(y.montant) FROM yvs_compta_cout_sup_doc_divers y WHERE y.doc_divers = id_), 0);
	ttc_= cout_ + (COALESCE(doc_.montant, 0));
	-- Montant totale des pièces payé
	SELECT INTO paye_ COALESCE((SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_divers y WHERE y.statut_piece='P' AND y.doc_divers = id_), 0);
	IF(paye_ < ttc_) THEN
		statut_regle_ = 'W';
	ELSE
		statut_regle_ = 'P';
	END IF;
	IF(COALESCE(doc_.etape_total, 0) > 0)THEN
		IF(COALESCE(doc_.etape_total, 0) = COALESCE(doc_.etape_valide, 0))THEN
			statut_ = 'V';
		ELSIF(COALESCE(doc_.etape_valide, 0) > 0)THEN
			statut_ = 'R';
		ELSE
			statut_ = 'E';
		END IF;
	END IF;
	UPDATE yvs_compta_caisse_doc_divers SET statut_regle = statut_regle_, statut_doc = statut_ WHERE  id=id_;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_doc_divers(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_doc_divers(bigint) IS 'equilibre l''etat reglé et des documents divers';


-- Function: com_recalcule_pr_periode(bigint, bigint, bigint, bigint, date, date)
-- DROP FUNCTION com_recalcule_pr_periode(bigint, bigint, bigint, date, date);
CREATE OR REPLACE FUNCTION com_recalcule_pr_periode(societe_ bigint, agence_ bigint, depot_ bigint, article_ bigint, debut_ date, fin_ date)
  RETURNS double precision AS
$BODY$
declare 
	line_ record;
	entree_ record; 

	query_ character varying default 'SELECT m.* FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE m.date_doc BETWEEN '||QUOTE_LITERAL(debut_)||' AND '|| QUOTE_LITERAL(fin_);

	total_ double precision default 0;
	pr_ double precision default 0;
	new_cout double precision default 0;
	stock_ double precision default 0;
	prix_arr_ numeric;
begin 
	IF(COALESCE(societe_, 0) > 0) THEN 
		query_= query_ || ' AND a.societe = '||societe_;
	END IF;
	IF(COALESCE(agence_, 0) > 0) THEN 
		query_= query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(depot_, 0) > 0) THEN 
		query_= query_ || ' AND d.id = '||depot_;
	END IF;
	IF(COALESCE(article_, 0) > 0) THEN 
		query_= query_ || ' AND m.article = '||article_;
	END IF;
	ALTER TABLE yvs_base_mouvement_stock DISABLE TRIGGER stock_maj_prix_mvt;
	FOR line_ IN EXECUTE query_
	LOOP
		SELECT INTO entree_ COALESCE(quantite, 0), COALESCE(cout_stock, 0) as cout FROM yvs_base_mouvement_stock WHERE calcul_pr IS TRUE AND mouvement = 'E' AND article = line_.article AND conditionnement = line_.conditionnement AND depot = line_.depot AND id != line_.id ORDER BY date_doc DESC, id desc limit 1;
		IF(line_.mouvement = 'E')THEN
			stock_ = get_stock_reel(line_.article, 0, line_.depot, 0, 0, line_.date_doc, line_.conditionnement, 0);
			-- Retourne le nouveau cout moyen calculé
			new_cout = COALESCE((((stock_ * COALESCE(entree_.cout, 0)) + (line_.quantite * line_.cout_entree)) / (line_.quantite + stock_)), 0);
			-- Arrondi les chiffres
			pr_ = round(new_cout::numeric, 3);
		ELSE
			pr_ = COALESCE(entree_.cout, 0);
		END IF;
		total_ = total_ + pr_;
		UPDATE yvs_base_mouvement_stock SET cout_stock = pr_ WHERE id = line_.id;
	END LOOP;
	ALTER TABLE yvs_base_mouvement_stock ENABLE TRIGGER stock_maj_prix_mvt;
	RETURN total_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_recalcule_pr_periode(bigint, bigint, bigint, bigint, date, date)
  OWNER TO postgres;


-- Function: grh_et_progression_salariale(bigint, bigint, bigint, date, date)
-- DROP FUNCTION grh_et_progression_salariale(bigint, bigint, bigint, date, date);
CREATE OR REPLACE FUNCTION grh_et_progression_salariale(IN societe_ bigint, IN agence_ bigint, IN employe_ bigint, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(element bigint, designation character varying, retenue boolean, "header" bigint, "reference" character varying, date_debut date, date_fin date, montant double precision, is_total boolean) AS
$BODY$
DECLARE 
	data_ RECORD;	
	dates_ RECORD;
	header_ RECORD;

	montant_ DOUBLE PRECISION DEFAULT 0;
	gain_ DOUBLE PRECISION DEFAULT 0;
	retenue_ DOUBLE PRECISION DEFAULT 0;
   
BEGIN 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_progression_salariale;
	CREATE TEMP TABLE IF NOT EXISTS table_progression_salariale(_element bigint, _designation character varying, _retenue boolean, _header bigint, _reference_ character varying, _date_debut date, _date_fin date, _montant double precision, _is_total boolean);
	DELETE FROM table_progression_salariale;
	FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, 'M')
	LOOP	
		SELECT INTO header_ * FROM yvs_grh_ordre_calcul_salaire o WHERE o.societe = societe_ AND o.debut_mois BETWEEN dates_.date_debut AND dates_.date_fin;
		IF(COALESCE(header_.id, 0) > 0)THEN
			FOR data_ IN SELECT * FROM yvs_grh_element_salaire e INNER JOIN yvs_grh_rubrique_bulletin r ON e.rubrique = r.id WHERE e.visible_bulletin IS TRUE AND r.societe = societe_
			LOOP
				IF(COALESCE(data_.retenue, false))THEN
					SELECT INTO montant_ COALESCE(d.retenu_salariale, 0) FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id 
						INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id WHERE d.element_salaire = data_.id AND b.entete = header_.id AND c.employe = employe_;
				ELSE
					SELECT INTO montant_ COALESCE(d.montant_payer, 0) FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id 
						INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id WHERE d.element_salaire = data_.id AND b.entete = header_.id AND c.employe = employe_;
				END IF;
				IF(COALESCE(montant_, 0) != 0)THEN
					INSERT INTO table_progression_salariale VALUES(data_.id, UPPER(data_.nom), COALESCE(data_.retenue, false), header_.id, header_.reference, header_.debut_mois, header_.fin_mois, COALESCE(montant_, 0), false);
				END IF;
			END LOOP;
			SELECT INTO retenue_ SUM(COALESCE(d.retenu_salariale, 0)) FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id 
				INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id INNER JOIN yvs_grh_element_salaire e ON d.element_salaire = e.id 
				WHERE COALESCE(e.retenue, FALSE) IS TRUE AND e.visible_bulletin IS TRUE AND b.entete = header_.id AND c.employe = employe_;
			SELECT INTO gain_ SUM(COALESCE(d.montant_payer, 0)) FROM yvs_grh_detail_bulletin d INNER JOIN yvs_grh_bulletins b ON d.bulletin = b.id 
				INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id INNER JOIN yvs_grh_element_salaire e ON d.element_salaire = e.id 
				WHERE COALESCE(e.retenue, FALSE) IS FALSE AND e.visible_bulletin IS TRUE AND b.entete = header_.id AND c.employe = employe_;
			montant_ = COALESCE(gain_, 0) - COALESCE(retenue_, 0);
			IF(COALESCE(montant_, 0) != 0)THEN
				INSERT INTO table_progression_salariale VALUES(0, 'NET A PAYER', FALSE, header_.id, header_.reference, header_.debut_mois, header_.fin_mois, COALESCE(montant_, 0), true);
			END IF;
		END IF;
	END LOOP;
	RETURN QUERY SELECT * FROM table_progression_salariale ORDER BY _is_total, _date_debut, _date_fin, _retenue DESC, _designation;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_progression_salariale(bigint, bigint, bigint, date, date)
  OWNER TO postgres;


-- Function: prod_et_production_vente(bigint, character varying, date, date, character varying, character varying)
-- DROP FUNCTION prod_et_production_vente(bigint, character varying, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION prod_et_production_vente(IN societe_ bigint, IN articles_ character varying, IN date_debut_ date, IN date_fin_ date, periode_ character varying, type_ character varying)
  RETURNS TABLE(id bigint, "reference" character varying, designation character varying, unite bigint, intitule character varying, entete character varying, date_debut date, date_fin date, production double precision, vente double precision) AS
$BODY$
DECLARE 
	data_ RECORD;	
	dates_ RECORD;

	id_ BIGINT DEFAULT 0;
   
BEGIN 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_production_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_production_vente(_id bigint, _reference character varying, _designation character varying, _unite bigint, _intitule character varying, _entete character varying, _date_debut date, _date_fin date, _production double precision, _vente double precision);
	DELETE FROM table_production_vente;
	
	FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_,  periode_, TRUE)
	LOOP
		IF(COALESCE(type_, '') = '' OR COALESCE(type_, '') = 'P')THEN
			FOR data_ IN SELECT c.id AS unite, u.reference, a.id, a.ref_art, a.designation, SUM(COALESCE(d.quantite, 0)) AS quantite FROM yvs_prod_declaration_production d INNER JOIN yvs_prod_session_of s ON d.session_of = s.id INNER JOIN yvs_prod_session_prod o ON s.session_prod = o.id
				INNER JOIN yvs_base_conditionnement c ON d.conditionnement = c.id INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id
				WHERE o.date_session BETWEEN dates_.date_debut AND dates_.date_fin AND f.societe = societe_ 
				AND ((COALESCE(articles_, '') IN ('', ' ', '0', '0-') AND a.ref_art IS NOT NULL) OR (COALESCE(articles_, '') NOT IN ('', ' ', '0', '0-') AND a.id::character varying in (select val from regexp_split_to_table(articles_,'-') val)))
				GROUP BY c.id, u.id, a.id
			LOOP
				INSERT INTO table_production_vente VALUES(data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, dates_.intitule, dates_.date_debut, dates_.date_fin, COALESCE(data_.quantite, 0), 0);
			END LOOP;
		END IF;
		IF(COALESCE(type_, '') = '' OR COALESCE(type_, '') = 'V')THEN
			FOR data_ IN SELECT c.id AS unite, u.reference, a.id, a.ref_art, a.designation, SUM(COALESCE(y.quantite, 0)) AS quantite FROM yvs_com_contenu_doc_vente y INNER JOIN yvs_com_doc_ventes d ON y.doc_vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
				INNER JOIN yvs_base_conditionnement c ON y.conditionnement = c.id INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id
				WHERE e.date_entete BETWEEN dates_.date_debut AND dates_.date_fin AND f.societe = societe_ AND d.type_doc = 'FV'
				AND a.categorie IN ('PSF', 'PF') AND ((COALESCE(articles_, '') IN ('', ' ', '0', '0-') AND a.ref_art IS NOT NULL) OR (COALESCE(articles_, '') NOT IN ('', ' ', '0', '0-') AND a.id::character varying in (select val from regexp_split_to_table(articles_,'-') val)))
				GROUP BY c.id, u.id, a.id
			LOOP
				RAISE NOTICE 'data_.id : % - data_.unite : %',data_.id,data_.unite;
				SELECT INTO id_ y._unite FROM table_production_vente y WHERE y._id = data_.id AND y._unite = data_.unite AND y._entete = dates_.intitule;
				RAISE NOTICE 'id_ : %',id_;
				IF(COALESCE(id_, 0) > 0)THEN
					UPDATE table_production_vente y SET _vente = data_.quantite WHERE y._id = data_.id AND y._unite = data_.unite AND y._entete = dates_.intitule;
				ELSE
					INSERT INTO table_production_vente VALUES(data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, dates_.intitule, dates_.date_debut, dates_.date_fin, 0, COALESCE(data_.quantite, 0));
				END IF;
			END LOOP;
		END IF;
	END LOOP;
	RETURN QUERY SELECT * FROM table_production_vente ORDER BY _date_debut, _date_fin, _designation, _reference;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION prod_et_production_vente(bigint, character varying, date, date, character varying, character varying)
  OWNER TO postgres;

  
  
-- Function: et_total_articles(bigint, bigint, date, date, character varying, character varying)
-- DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    _date_save DATE DEFAULT date_debut_;
    date_ DATE;
    
    taux_ DOUBLE PRECISION DEFAULT 0;
    _total DOUBLE PRECISION DEFAULT 0;
    _quantite DOUBLE PRECISION DEFAULT 0;
    _taux DOUBLE PRECISION DEFAULT 0;
    
    i INTEGER DEFAULT 0;
    count_ INTEGER DEFAULT 0;

    
    jour_ CHARACTER VARYING;
    execute_ CHARACTER VARYING;   
    facture_avoir_ CHARACTER VARYING;   
    query_ CHARACTER VARYING default 'select c.id as unite, a.id, a.ref_art, a.designation, m.reference, sum(coalesce(y.prix_total, 0)) as total, sum(coalesce(y.quantite, 0)) as qte from yvs_com_contenu_doc_vente y 
					inner join yvs_base_articles a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id inner join yvs_base_unite_mesure m on c.unite = m.id 
					inner join yvs_base_famille_article f ON a.famille = f.id inner join yvs_com_doc_ventes d on y.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc 
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id  inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.statut = ''V''';
    
    data_ RECORD;
    avoir_ RECORD;
    prec_ RECORD;
    _article RECORD;
    dates_ RECORD;

    deja_ BOOLEAN DEFAULT FALSE;
    
BEGIN    
	CREATE TEMP TABLE IF NOT EXISTS table_total_articles(_code_ CHARACTER VARYING, _nom_ CHARACTER VARYING, _article_ BIGINT, _unite bigint, _reference character varying, _periode_ CHARACTER VARYING, _total_ DOUBLE PRECISION, _quantite_ DOUBLE PRECISION, _taux_ DOUBLE PRECISION, _rang_ INTEGER, _is_total_ BOOLEAN, _is_footer_ BOOLEAN);
	DELETE FROM table_total_articles;
	deja_ = false;
	query_ = query_ ||' AND f.societe = '||societe_;
	if(article_ is not null and article_ NOT IN ('', ' '))then
		query_ = query_ ||' AND a.ref_art LIKE ANY (ARRAY[';
		for jour_ IN select val from regexp_split_to_table(article_,',') val
		loop
			if(deja_)then
				query_ = query_ || ',';
			end if;
			query_ = query_ || ''''||TRIM(jour_)||'%''';
			deja_ = true;
		end loop;
		query_ = query_ || '])';
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ ||' and p.agence = '||agence_;
	end if;
	
	date_debut_ = _date_save;
	i = 0;
	for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, periode_)
	loop
		jour_ = dates_.intitule;
		count_ = 1;
		_total = 0;
		_quantite = 0;
		_taux = 0;
		execute_ = query_ ||' and ((d.type_doc = ''FV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))' || ' GROUP BY c.id, m.id, a.id';
		FOR _article IN EXECUTE execute_
		LOOP			
			facture_avoir_ = query_ ||' and y.conditionnement = '||_article.unite||' and y.article = '||_article.id||'
					and ((d.type_doc = ''FAV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''') or (d.type_doc = ''BRV'' and d.date_livraison between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''')) GROUP BY c.id, m.id, a.id';
			execute facture_avoir_ into avoir_;	
			if(avoir_.total IS NULL)then
				avoir_.total = 0;					
			end if;
			_article.total = _article.total - avoir_.total;
			
			SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id and _unite = _article.unite ORDER BY _rang_ DESC LIMIT 1;
			IF(prec_.total = 0)THEN
				SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id and _unite = _article.unite AND _total_ > 0 ORDER BY _rang_ DESC LIMIT 1;
			END IF;
			taux_ = _article.total - prec_.total;
			IF(taux_ IS NULL)THEN
				taux_ = 0;
			END IF;
			IF(taux_ != 0 and coalesce(prec_.total, 0) != 0)THEN
				taux_ = (taux_ / prec_.total) * 100;
			END IF;

			INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, _article.unite, _article.reference, jour_, _article.total, _article.qte, taux_, i, FALSE, FALSE);

			_total = _total + _article.total;
			_quantite = _quantite + _article.qte;
			_taux = _taux + taux_;
			count_ = count_ + 1;
		END LOOP;
		i = i + 1;
		IF(_total > 0)THEN
			INSERT INTO table_total_articles VALUES('TOTAUX', 'TOTAUX', 0, 0, '', jour_, _total, _quantite, (_taux / count_), i, TRUE, TRUE);
		END IF;
	END LOOP;
	FOR _article IN SELECT _article_ AS id, _nom_ AS designation, _code_ AS ref_art, _unite AS unite, _reference AS reference, COALESCE(sum(_total_), 0) AS total, COALESCE(sum(_quantite_), 0) AS qte, COALESCE(sum(_taux_), 0) AS taux 
		FROM table_total_articles y GROUP BY _article_, _code_, _nom_, _unite, _reference
	LOOP		
		SELECT INTO i count(_rang_) FROM table_total_articles WHERE _article_ = _article.id AND _unite = _article.unite;
		IF(i IS NULL OR i = 0)THEN
			i = 1;
		END IF;
		INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, _article.unite, _article.reference, 'TOTAUX', _article.total, _article.qte, (_article.taux / i), i+1, TRUE, FALSE);
	END LOOP;
	
	RETURN QUERY SELECT * FROM table_total_articles ORDER BY _is_total_, _is_footer_, _nom_, _code_, _rang_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;

  
  
-- Function: com_et_synthese_approvision_distribution(bigint, bigint, bigint, date, date, boolean)
-- DROP FUNCTION com_et_synthese_approvision_distribution(bigint, bigint, bigint, date, date, boolean);
CREATE OR REPLACE FUNCTION com_et_synthese_approvision_distribution(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN date_debut_ date, IN date_fin_ date, IN for_prod_ boolean)
  RETURNS TABLE(id bigint, groupe character varying, libelle character varying, type bigint, classe bigint, code character varying, intitule character varying, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	data_ RECORD;
	classe_ RECORD;

	query_ CHARACTER VARYING DEFAULT 'SELECT SUM(y.quantite * COALESCE(c.prix, 0)) FROM yvs_base_mouvement_stock y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_depots e ON y.depot = e.id
						INNER JOIN yvs_base_famille_article f ON a.famille = f.id INNER JOIN yvs_base_conditionnement c ON y.conditionnement = c.id WHERE y.quantite IS NOT NULL';
	inner_st CHARACTER VARYING DEFAULT 'INNER JOIN yvs_com_contenu_doc_stock s ON y.id_externe = s.id INNER JOIN yvs_com_doc_stocks d ON s.doc_stock = d.id WHERE';
	inner_fv CHARACTER VARYING DEFAULT 'INNER JOIN yvs_com_contenu_doc_vente s ON y.id_externe = s.id INNER JOIN yvs_com_doc_ventes d ON s.doc_vente = d.id WHERE';
	inner_fa CHARACTER VARYING DEFAULT 'INNER JOIN yvs_com_contenu_doc_achat s ON y.id_externe = s.id INNER JOIN yvs_com_doc_achats d ON s.doc_achat = d.id WHERE';
	quantite_ CHARACTER VARYING DEFAULT '';

	entree_ DOUBLE PRECISION DEFAULT 0;
	sortie_ DOUBLE PRECISION DEFAULT 0;
	stock_ DOUBLE PRECISION DEFAULT 0;
	
	valeur_si_ DOUBLE PRECISION DEFAULT 0;
	valeur_fa_ DOUBLE PRECISION DEFAULT 0;
	valeur_pd_ DOUBLE PRECISION DEFAULT 0;
	valeur_en_ DOUBLE PRECISION DEFAULT 0;
	valeur_ex_ DOUBLE PRECISION DEFAULT 0;
	valeur_in_ DOUBLE PRECISION DEFAULT 0;
	
	valeur_fv_ DOUBLE PRECISION DEFAULT 0;
	valeur_dn_ DOUBLE PRECISION DEFAULT 0;
	valeur_ra_ DOUBLE PRECISION DEFAULT 0;
	valeur_mq_ DOUBLE PRECISION DEFAULT 0;
	valeur_dp_ DOUBLE PRECISION DEFAULT 0;
	valeur_om_ DOUBLE PRECISION DEFAULT 0;
	valeur_ss_ DOUBLE PRECISION DEFAULT 0;
	valeur_sss_ DOUBLE PRECISION DEFAULT 0;
	valeur_sf_ DOUBLE PRECISION DEFAULT 0;
   
BEGIN 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_et_synthese_approvision_distribution;
	CREATE TEMP TABLE IF NOT EXISTS table_et_synthese_approvision_distribution(_id bigint, _groupe character varying, _libelle character varying, _type bigint, _classe bigint, _code character varying, _intitule character varying, _valeur double precision, _rang integer);
	DELETE FROM table_et_synthese_approvision_distribution;
	IF(COALESCE(for_prod_, false))THEN
		query_ = query_ || ' AND a.categorie IN (''PSF'', ''PF'')';
	END IF;
	IF(societe_ IS NOT NULL AND societe_ != 0)THEN
		query_ = query_ || ' AND f.societe = '||societe_;
	END IF;
	IF(agence_ IS NOT NULL AND agence_ != 0)THEN
		query_ = query_ || ' AND e.agence = '||agence_;
	END IF;
	IF(depot_ IS NOT NULL AND depot_ != 0)THEN
		query_ = query_ || ' AND e.id = '||depot_;
	END IF;
	-- VALEUR SUR LES ARTICLES QUI N'ONT PAS DE CLASSE
	quantite_ = query_ || ' AND (a.classe1 IS NULL AND a.classe2 IS NULL)';
	-- STOCK INITIAL
	EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.date_doc < '||QUOTE_LITERAL(date_debut_) INTO entree_;
	EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.date_doc < '||QUOTE_LITERAL(date_debut_) INTO sortie_;
	valeur_si_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	IF(valeur_si_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'STOCK INITIAL', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_si_, 1);
	END IF;
	
	-- STOCK FINAL
	EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.date_doc <= '||QUOTE_LITERAL(date_fin_) INTO entree_;
	EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.date_doc <= '||QUOTE_LITERAL(date_fin_) INTO sortie_;
	valeur_sf_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	IF(valeur_sf_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'STOCK FINAL', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_sf_, 8);
	END IF;
	
	quantite_ = quantite_ || ' AND y.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||'';
	-- PRODUCTION
	EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_prod_of_suivi_flux'', ''yvs_prod_declaration_production'')' INTO entree_;
	sortie_ = 0;
	valeur_pd_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	IF(valeur_pd_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'PRODUCTION', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_pd_, 2);
	END IF;
	-- ACHAT
	EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_achat'')' INTO entree_;
	sortie_ = 0;
	valeur_fa_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	IF(valeur_fa_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'ACHAT', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_fa_, 3);
	END IF;
	-- ENTREE
	IF(COALESCE(depot_, 0) > 0 OR TRUE)THEN
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''ES'', ''FT'') AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
	ELSE
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''ES'') AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
	END IF;
	sortie_ = 0;
	valeur_en_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''INV'' AND d.nature = ''EXCEDENT'' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
	valeur_ex_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
	valeur_in_ = COALESCE(valeur_en_, 0) + COALESCE(valeur_ex_, 0);
	IF(valeur_in_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'AUTRE MVT(E)', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_in_, 4);
	END IF;
	
	-- VENTE
	EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_vente'')' INTO entree_;
	EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_vente'')' INTO sortie_;
	valeur_fv_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	IF(valeur_fv_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'VENTE', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_fv_, 1);
	END IF;
	
	-- DON ET RECEPTION
	entree_ = 0;
	EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''SS'' AND d.nature = ''DONS'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
	valeur_dn_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	IF(valeur_dn_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'DONS', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_dn_, 3);
	END IF;
	
	-- RATION
	entree_ = 0;
	EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_ration'')' INTO sortie_;
	valeur_ra_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	IF(valeur_ra_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'RATION', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_ra_, 4);
	END IF;
	
	-- MANQUANT
	entree_ = 0;
	EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''INV'' AND d.nature = ''MANQUANT'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
	valeur_mq_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	IF(valeur_mq_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'MANQUANT', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_mq_, 5);
	END IF;
	
	-- REJET ET CONSTAT
	entree_ = 0;
	EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''SS'' AND d.nature = ''DEPRECIATION'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
	valeur_dp_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	IF(valeur_dp_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'REJETS', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_dp_, 6);
	END IF;
	
	-- AUTRE MVT
	SELECT INTO entree_ 0;
	EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''SS'') AND d.nature NOT IN (''DONS'', ''DEPRECIATION'') AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
	valeur_sss_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	sortie_ = 0;
	IF(COALESCE(depot_, 0) > 0 OR TRUE)THEN
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''FT'') AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
	END IF;
	valeur_ss_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
	EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_prod_of_suivi_flux'', ''yvs_prod_declaration_production'')' INTO sortie_;
	valeur_om_ = COALESCE(valeur_ss_, 0) + COALESCE(valeur_sss_, 0) + COALESCE(sortie_, 0);
	IF(valeur_om_ != 0)THEN
		INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'AUTRE MVT(S)', 0, 0, 'NO CLASSE', 'NO CLASSE', valeur_om_, 7);
	END IF;
	
	
	-- VALEUR SUR LES ARTICLES QUI ONT UNE CLASSE
	FOR classe_ IN SELECT c.id, c.code_ref, c.designation FROM yvs_base_classes_stat c WHERE c.societe = societe_
	LOOP
		quantite_ = query_ || ' AND (a.classe1 = '||classe_.id||' OR a.classe2  = '||classe_.id||')';
		-- STOCK INITIAL
		EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.date_doc < '||QUOTE_LITERAL(date_debut_) INTO entree_;
		EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.date_doc < '||QUOTE_LITERAL(date_debut_) INTO sortie_;
		valeur_si_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
		IF(valeur_si_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'STOCK INITIAL', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_si_, 1);
		END IF;
		
		-- STOCK FINAL
		EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.date_doc <= '||QUOTE_LITERAL(date_fin_) INTO entree_;
		EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.date_doc <= '||QUOTE_LITERAL(date_fin_) INTO sortie_;
		valeur_sf_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);	
		IF(valeur_sf_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'STOCK FINAL', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_sf_, 8);
		END IF;
		
		quantite_ = quantite_ || ' AND y.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||'';
		-- PRODUCTION
		EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_prod_of_suivi_flux'', ''yvs_prod_declaration_production'')' INTO entree_;
		sortie_ = 0;
		valeur_pd_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
		IF(valeur_pd_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'PRODUCTION', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_pd_, 2);
		END IF;
		-- ACHAT
		EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_achat'')' INTO entree_;
		sortie_ = 0;
		valeur_fa_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
		IF(valeur_fa_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'ACHAT', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_fa_, 3);
		END IF;
		-- ENTREE
		IF(COALESCE(depot_, 0) > 0 OR TRUE)THEN
			EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''ES'', ''FT'') AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
		ELSE
			EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''ES'') AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
		END IF;
		sortie_ = 0;
		valeur_en_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''INV'' AND d.nature = ''EXCEDENT'' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO entree_;
		valeur_ex_ = COALESCE(entree_, 0) - COALESCE(sortie_, 0);
		valeur_in_ = COALESCE(valeur_en_, 0) + COALESCE(valeur_ex_, 0);
		IF(valeur_in_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (1, 'APPROVISIONNEMENT', 'AUTRE MVT(E)', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_in_, 4);
		END IF;
		
		-- VENTE
		EXECUTE quantite_ || ' AND y.mouvement = ''E'' AND y.table_externe IN (''yvs_com_contenu_doc_vente'')' INTO entree_;
		EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_vente'')' INTO sortie_;
		valeur_fv_ =  COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		IF(valeur_fv_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'VENTE', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_fv_, 1);
		END IF;
		
		-- DON ET RECEPTION
		entree_ = 0;
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''SS'' AND d.nature = ''DONS'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
		valeur_dn_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		IF(valeur_dn_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'DONS', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_dn_, 3);
		END IF;
		
		-- RATION
		entree_ = 0;
		EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_ration'')' INTO sortie_;
		valeur_ra_ =  COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		IF(valeur_ra_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'RATION', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_ra_, 4);
		END IF;
		
		-- MANQUANT
		entree_ = 0;
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''INV'' AND d.nature = ''MANQUANT'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
		valeur_mq_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		IF(valeur_mq_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'MANQUANT', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_mq_, 5);
		END IF;
		
		-- REJET ET CONSTAT
		entree_ = 0;
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc = ''SS'' AND d.nature = ''DEPRECIATION'' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
		valeur_dp_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		IF(valeur_dp_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'REJETS', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_dp_, 6);
		END IF;
		
		-- AUTRE MVT
		SELECT INTO entree_ 0;
		EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''SS'') AND d.nature NOT IN (''DONS'', ''DEPRECIATION'') AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
		valeur_sss_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		sortie_ = 0;
		IF(COALESCE(depot_, 0) > 0 OR TRUE)THEN
			EXECUTE REPLACE(quantite_, 'WHERE', inner_st) || ' AND d.type_doc IN (''FT'') AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_com_contenu_doc_stock'')' INTO sortie_;
		END IF;
		valeur_ss_ = COALESCE(sortie_, 0) - COALESCE(entree_, 0);
		EXECUTE quantite_ || ' AND y.mouvement = ''S'' AND y.table_externe IN (''yvs_prod_of_suivi_flux'', ''yvs_prod_declaration_production'')' INTO sortie_;
		valeur_om_ = COALESCE(valeur_ss_, 0) + COALESCE(valeur_sss_, 0) + COALESCE(sortie_, 0);
		IF(valeur_om_ != 0)THEN
			INSERT INTO table_et_synthese_approvision_distribution VALUES (2, 'DISTRIBUTION', 'AUTRE MVT(S)', 1, classe_.id, classe_.code_ref, classe_.designation, valeur_om_, 7);
		END IF;	
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_synthese_approvision_distribution ORDER BY _id, _rang, _type, _intitule, _code;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_synthese_approvision_distribution(bigint, bigint, bigint, date, date, boolean)
  OWNER TO postgres;

  
  
-- Function: prod_et_synthese_consommation_mp(bigint, bigint, bigint, character varying, date, date, boolean, character varying)
DROP FUNCTION prod_et_synthese_consommation_mp(bigint, bigint, bigint, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION prod_et_synthese_consommation_mp(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN article_ character varying, IN date_debut_ date, IN date_fin_ date, IN by_journee boolean, IN type_ character varying)
  RETURNS TABLE(id bigint, article bigint, reference character varying, designation character varying, unite bigint, numero character varying, classe bigint, code character varying, intitule character varying, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	data_ RECORD;
	classe_ RECORD;
	unite_ RECORD;
	dates_ RECORD;
	
	query_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT a.id, a.ref_art, a.designation, c.id as unite, u.reference FROM yvs_base_articles a INNER JOIN yvs_base_conditionnement c ON a.id = c.article INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id 
						INNER JOIN yvs_base_famille_article o ON a.famille = o.id WHERE a.categorie = ''MP''';
	mouvement_ CHARACTER VARYING DEFAULT 'SELECT SUM(y.quantite) FROM yvs_base_mouvement_stock y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_depots d ON y.depot = d.id INNER JOIN yvs_agences f ON d.agence = f.id WHERE y.quantite IS NOT NULL';
	inner_st CHARACTER VARYING DEFAULT 'INNER JOIN yvs_com_contenu_doc_stock s ON y.id_externe = s.id INNER JOIN yvs_com_doc_stocks o ON s.doc_stock = o.id WHERE';
	stock_ CHARACTER VARYING;
	ids_ CHARACTER VARYING DEFAULT '''0''';
	id_ CHARACTER VARYING DEFAULT '0';
	
	valeur_ DOUBLE PRECISION DEFAULT 0;
	taux_ DOUBLE PRECISION DEFAULT 0;

	exist_ BIGINT DEFAULT 0;
	serial_ BIGINT DEFAULT 1;
   
BEGIN 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_et_synthese_consommation_mp;
	CREATE TEMP TABLE IF NOT EXISTS table_et_synthese_consommation_mp(_id bigint, _article bigint, _reference character varying, _designation character varying, _unite bigint, _numero character varying, _classe bigint, _code character varying, _intitule character varying, _valeur double precision, _rang integer);
	DELETE FROM table_et_synthese_consommation_mp;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND o.societe = '||societe_;
		mouvement_ = mouvement_ || ' AND f.societe = '||societe_;
	END IF;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		mouvement_ = mouvement_ || ' AND f.id = '||agence_;
	END IF;
	IF(depot_ IS NOT NULL AND  depot_ > 0)THEN
		mouvement_ = mouvement_ || ' AND d.id = '|| depot_;
	ELSE
		mouvement_ = REPLACE(mouvement_, 'WHERE', inner_st) || ' AND o.type_doc NOT IN (''FT'', ''TR'')';
	END IF;
	IF(date_debut_ IS NOT NULL AND date_fin_ IS NOT NULL)THEN
		mouvement_ = mouvement_ || ' AND y.date_doc BETWEEN '''||date_debut_||''' AND '''||date_fin_||'''';
	END IF;
	IF(COALESCE(article_, '') NOT IN ('', ' ', '0'))THEN
		FOR id_ IN SELECT head FROM regexp_split_to_table(article_,',') head WHERE CHAR_LENGTH(head) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(id_);
		END LOOP;
		query_ = query_ || ' AND a.id IN ('||ids_||')';
	END IF;
	FOR data_ IN EXECUTE query_
	LOOP
		stock_ = mouvement_ || ' AND y.article = '||data_.id||' AND y.conditionnement = '||data_.unite;
		valeur_ = (SELECT get_stock(data_.id, 0, 0, 0, societe_, (date_debut_ - interval '1 day')::date, data_.unite));
		IF(COALESCE(valeur_, 0) != 0)THEN
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'S.I', 'STOCK INITIAL', valeur_, 1);
		END IF;
		serial_ = serial_ + 1;
		EXECUTE stock_ || ' AND y.mouvement = ''E''' INTO valeur_;
		IF(COALESCE(valeur_, 0) != 0)THEN
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'APPROV', 'APPROVISSIONNEMENT', valeur_, 2);
		END IF;
		serial_ = serial_ + 1;
		IF(COALESCE(by_journee, FALSE) IS FALSE)THEN			
			SELECT INTO valeur_ SUM(u.quantite) FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id  
				INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id 
				WHERE u.date_flux BETWEEN date_debut_ AND date_fin_ AND a.classe1 IS NULL AND a.classe2 IS NULL AND y.article = data_.id AND y.unite = data_.unite;
			IF(valeur_ != 0)THEN
				INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'NO CLASSE', 'NO CLASSE', valeur_, 3);
			END IF;
			serial_ = serial_ + 1;
			
			FOR classe_ IN SELECT c.id, c.code_ref, c.designation FROM yvs_base_classes_stat c WHERE c.societe = societe_
			LOOP
				SELECT INTO valeur_ SUM(u.quantite) FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id 
					INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id 
					WHERE u.date_flux BETWEEN date_debut_ AND date_fin_ AND (a.classe1 = classe_.id OR a.classe2 = classe_.id) AND y.article = data_.id AND y.unite = data_.unite;
				IF(valeur_ != 0)THEN
					INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, classe_.id, classe_.code_ref, classe_.designation, valeur_, 4);
				END IF;
				serial_ = serial_ + 1;
			END LOOP;
		ELSE
			FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, 'J', true)
			LOOP
				SELECT INTO valeur_ SUM(u.quantite) FROM yvs_prod_of_suivi_flux u INNER JOIN yvs_prod_flux_composant c ON u.composant = c.id INNER JOIN yvs_prod_composant_of y ON c.composant = y.id 
					INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_base_articles a ON o.article = a.id 
					WHERE u.date_flux BETWEEN dates_.date_debut AND dates_.date_fin AND y.article = data_.id AND y.unite = data_.unite;
				IF(valeur_ != 0)THEN
					INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, dates_.intitule, dates_.intitule, valeur_, 4);
				END IF;
				serial_ = serial_ + 1;
			END LOOP;
		END IF;

		SELECT INTO valeur_ SUM(y._valeur) FROM table_et_synthese_consommation_mp y WHERE y._article = data_.id AND y._unite = data_.unite AND y._rang IN (3, 4);
		IF(valeur_ != 0)THEN
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'TOTAL', 'TOTAL CONSO', valeur_, 5);
		END IF;
		serial_ = serial_ + 1;
		
		EXECUTE stock_|| ' AND y.mouvement = ''S'' AND y.table_externe NOT IN (''yvs_prod_composant_of'', ''yvs_prod_flux_composant'', ''yvs_prod_of_suivi_flux'')' INTO valeur_;
		IF(COALESCE(valeur_, 0) != 0)THEN
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'SORTIE', 'AUTRES SORTIE', valeur_, 6);
		END IF;
		serial_ = serial_ + 1;
		
		valeur_ = (SELECT get_stock(data_.id, 0, 0, 0, societe_, date_fin_, data_.unite));
		IF(COALESCE(valeur_, 0) != 0)THEN
			INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, 0, 'S.F', 'STOCK FINAL', valeur_, 7);
		END IF;
		serial_ = serial_ + 1;
	END LOOP;
	IF(COALESCE(type_, '') NOT IN ('', ' '))THEN
		IF(COALESCE(type_, '') = 'D')THEN
			FOR classe_ IN SELECT DISTINCT y._article AS article FROM table_et_synthese_consommation_mp y
			LOOP
				SELECT INTO unite_ c.id as unite, u.id, u.reference FROM yvs_base_conditionnement c INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id WHERE c.article = classe_.article AND c.by_prod IS TRUE AND c.defaut IS TRUE LIMIT 1;
				IF(COALESCE(unite_.id, 0) > 0)THEN
					FOR data_ IN SELECT y.*, c.unite FROM table_et_synthese_consommation_mp y INNER JOIN yvs_base_conditionnement c ON y._unite = c.id WHERE y._article = classe_.article AND y._unite != COALESCE(unite_.unite, 0)
					LOOP
						SELECT INTO taux_ t.taux_change FROM yvs_base_table_conversion t WHERE t.unite = COALESCE(data_.unite, 0) AND t.unite_equivalent = COALESCE(unite_.id, 0);
						IF(COALESCE(taux_, 0) = 0)THEN
							SELECT INTO taux_ t.taux_change FROM yvs_base_table_conversion t WHERE t.unite = COALESCE(unite_.id, 0) AND t.unite_equivalent = COALESCE(data_.unite, 0);
							IF(COALESCE(taux_, 0) = 0)THEN
								valeur_ = 0;
							ELSE
								valeur_ = data_._valeur / COALESCE(taux_, 0);
							END IF;
						ELSE
							valeur_ = data_._valeur * COALESCE(taux_, 0);							
						END IF;
						DELETE FROM table_et_synthese_consommation_mp WHERE _id = data_._id;
						SELECT INTO exist_ _id FROM table_et_synthese_consommation_mp y WHERE y._article = classe_.article AND y._code = data_._code AND y._unite = COALESCE(unite_.unite, 0);
						IF(COALESCE(exist_, 0) = 0)THEN
							INSERT INTO table_et_synthese_consommation_mp VALUES (serial_, data_._article, data_._reference, data_._designation, unite_.unite, unite_.reference, data_._classe, data_._code, data_._intitule, valeur_, data_._rang);
						ELSE
							UPDATE table_et_synthese_consommation_mp y SET _valeur = y._valeur + valeur_ WHERE y._id = exist_;
						END IF;
						serial_ = serial_ + 1;
					END LOOP;
				END IF;
			END LOOP;
		END IF;
	END IF;
	RETURN QUERY SELECT * FROM table_et_synthese_consommation_mp ORDER BY _rang, _code, _designation, _reference;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION prod_et_synthese_consommation_mp(bigint, bigint, bigint, character varying, date, date, boolean, character varying)
  OWNER TO postgres;

