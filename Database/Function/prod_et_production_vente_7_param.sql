-- Function: prod_et_production_vente(bigint, character varying, date, date, character varying, character varying, character varying)
DROP FUNCTION prod_et_production_vente(bigint, character varying, date, date, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION prod_et_production_vente(IN societe_ bigint, IN articles_ character varying, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying, IN type_ character varying, IN valorise_by_ character varying)
  RETURNS TABLE(id bigint, reference character varying, designation character varying, unite bigint, intitule character varying, entete character varying, date_debut date, date_fin date, production double precision, vente double precision, ecart double precision, production_val double precision, vente_val double precision, ecart_val double precision) AS
$BODY$
DECLARE 
	data_ RECORD;
	ligne_ RECORD;	
	dates_ RECORD;

	id_ BIGINT DEFAULT 0;

	prix_revient_ DOUBLE PRECISION;
	valeur_ DOUBLE PRECISION;
	ecart_ DOUBLE PRECISION;
   
BEGIN 	
	-- Creation de la table temporaire des resumés de cotisation mensuel
	DROP TABLE IF EXISTS table_production_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_production_vente(_id bigint, _reference character varying, _designation character varying, _unite bigint, _intitule character varying, _entete character varying, _date_debut date, _date_fin date, _production double precision, _vente double precision, _ecart double precision, _production_val double precision, _vente_val double precision, _ecart_val double precision);
	DELETE FROM table_production_vente;
	
	FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_,  periode_, TRUE)
	LOOP
		IF(COALESCE(type_, '') = '' OR COALESCE(type_, '') = 'P')THEN
			FOR data_ IN SELECT c.id AS unite, u.reference, a.id, a.ref_art, a.designation, SUM(COALESCE(d.quantite, 0)) AS quantite, c.prix AS prix_vente, c.prix_achat, c.prix_prod FROM yvs_prod_declaration_production d INNER JOIN yvs_prod_session_of s ON d.session_of = s.id INNER JOIN yvs_prod_session_prod o ON s.session_prod = o.id
				INNER JOIN yvs_base_conditionnement c ON d.conditionnement = c.id INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id
				WHERE o.date_session BETWEEN dates_.date_debut AND dates_.date_fin AND f.societe = societe_ 
				AND ((COALESCE(articles_, '') IN ('', ' ', '0', '0-') AND a.ref_art IS NOT NULL) OR (COALESCE(articles_, '') NOT IN ('', ' ', '0', '0-') AND a.id::character varying in (select val from regexp_split_to_table(articles_,'-') val)))
				GROUP BY c.id, u.id, a.id
			LOOP
				IF(COALESCE(valorise_by_ , 'R') = 'V')THEN
					valeur_ = data_.prix_vente * data_.quantite;
				ELSIF(COALESCE(valorise_by_ , 'R') = 'A')THEN
					valeur_ = data_.prix_achat * data_.quantite;
				ELSIF(COALESCE(valorise_by_ , 'R') = 'P')THEN
					valeur_ = data_.prix_prod * data_.quantite;
				ELSE
					prix_revient_ := (SELECT get_pr(data_.id, 0, 0, dates_.date_fin, data_.unite, 0));
					valeur_ = prix_revient_ * data_.quantite;
				END IF;
				INSERT INTO table_production_vente VALUES(data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, dates_.intitule, dates_.date_debut, dates_.date_fin, COALESCE(data_.quantite, 0), 0, COALESCE(data_.quantite, 0), COALESCE(valeur_, 0), 0, COALESCE(valeur_, 0));
			END LOOP;
		END IF;
		IF(COALESCE(type_, '') = '' OR COALESCE(type_, '') = 'V')THEN
			FOR data_ IN SELECT c.id AS unite, u.reference, a.id, a.ref_art, a.designation, SUM(COALESCE(y.quantite, 0)) AS quantite, SUM(COALESCE(y.prix_total, 0)) AS valeur, c.prix AS prix_vente, c.prix_achat, c.prix_prod FROM yvs_com_contenu_doc_vente y INNER JOIN yvs_com_doc_ventes d ON y.doc_vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
				INNER JOIN yvs_base_conditionnement c ON y.conditionnement = c.id INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id INNER JOIN yvs_base_unite_mesure u ON c.unite = u.id
				WHERE e.date_entete BETWEEN dates_.date_debut AND dates_.date_fin AND f.societe = societe_ AND d.type_doc = 'FV'
				AND a.categorie IN ('PSF', 'PF') AND ((COALESCE(articles_, '') IN ('', ' ', '0', '0-') AND a.ref_art IS NOT NULL) OR (COALESCE(articles_, '') NOT IN ('', ' ', '0', '0-') AND a.id::character varying in (select val from regexp_split_to_table(articles_,'-') val)))
				GROUP BY c.id, u.id, a.id
			LOOP
				valeur_ = data_.valeur;
				SELECT INTO id_ y._unite FROM table_production_vente y WHERE y._id = data_.id AND y._unite = data_.unite AND y._entete = dates_.intitule;
				IF(COALESCE(id_, 0) > 0)THEN
					SELECT INTO ligne_ y.* FROM table_production_vente y WHERE y._id = data_.id AND y._unite = data_.unite AND y._entete = dates_.intitule;
					ecart_ = (ligne_._production - data_.quantite);
					IF(COALESCE(valorise_by_ , 'R') = 'V')THEN
						valeur_ = data_.prix_vente * ecart_;
					ELSIF(COALESCE(valorise_by_ , 'R') = 'A')THEN
						valeur_ = data_.prix_achat * ecart_;
					ELSIF(COALESCE(valorise_by_ , 'R') = 'P')THEN
						valeur_ = data_.prix_prod * ecart_;
					ELSE
						prix_revient_ := (SELECT get_pr(data_.id, 0, 0, dates_.date_fin, data_.unite, 0));
						valeur_ = prix_revient_ * ecart_;
					END IF;
					UPDATE table_production_vente y SET _vente = data_.quantite, _vente_val = COALESCE(data_.valeur, 0), _ecart = ecart_, _ecart_val = COALESCE(valeur_, 0) WHERE y._id = data_.id AND y._unite = data_.unite AND y._entete = dates_.intitule;
				ELSE
					INSERT INTO table_production_vente VALUES(data_.id, data_.ref_art, data_.designation, data_.unite, data_.reference, dates_.intitule, dates_.date_debut, dates_.date_fin, 0, COALESCE(data_.quantite, 0), (-COALESCE(data_.quantite, 0)), 0, COALESCE(valeur_, 0), (-COALESCE(valeur_, 0)));
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
ALTER FUNCTION prod_et_production_vente(bigint, character varying, date, date, character varying, character varying, character varying)
  OWNER TO postgres;
