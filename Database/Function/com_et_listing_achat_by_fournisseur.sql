-- Function: com_et_listing_achat_by_fournisseur(bigint, bigint, bigint, date, date, character varying, integer)

-- DROP FUNCTION com_et_listing_achat_by_fournisseur(bigint, bigint, bigint, date, date, character varying, integer);

CREATE OR REPLACE FUNCTION com_et_listing_achat_by_fournisseur(IN societe_ bigint, IN agence_ bigint, IN fournisseur_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN cumul_ integer)
  RETURNS TABLE(fournisseur bigint, code character varying, nom character varying, depot bigint, abbreviation character varying, designation character varying, unite bigint, entete character varying, quantite double precision, prix_total double precision, rang integer) AS
$BODY$
declare 

   fournisseurs_ RECORD;
   data_ RECORD;
   dates_ RECORD;
   
   jour_ character varying;
   
   ristourne_ CHARACTER VARYING DEFAULT 'SELECT e.id, e.code as abbreviation, e.designation, c.quantite_recu AS quantite, c.prix_total, c.conditionnement';
   colonne_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT y.id, y.code_fsseur, CONCAT(y.nom, '' '', y.prenom) AS nom';
   save_ CHARACTER VARYING DEFAULT ' FROM yvs_base_fournisseur y INNER JOIN yvs_com_doc_achats d ON d.fournisseur = y.id INNER JOIN yvs_agences a ON d.agence = a.id
						INNER JOIN yvs_com_contenu_doc_achat c ON c.doc_achat = d.id LEFT JOIN yvs_base_depots e ON d.depot_reception = e.id
						WHERE d.statut = ''V'' AND d.type_doc = ''FA'' AND d.date_doc BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND a.societe = '||societe_;
   where_ CHARACTER VARYING DEFAULT '';
   query_ CHARACTER VARYING DEFAULT '';
   code_depot_ CHARACTER VARYING DEFAULT '';
   name_depot_ CHARACTER VARYING DEFAULT '';
   
   cond_ BIGINT DEFAULT 0;

   i integer default 0;
   
begin 	
	-- Si cumul=0 On ne cumule pas (détaillé)
	-- Si cumul=1 On ne cumule par fournisseur et par articles
	-- Si cumul=2 On ne cumule par fournisseur (récapitulatif)
	DROP TABLE IF EXISTS table_listing_achat_by_fournisseur;
	CREATE TEMP TABLE IF NOT EXISTS table_listing_achat_by_fournisseur(_fournisseur bigint, _code character varying, _nom character varying, _depot bigint, _abbreviation character varying, _designation character varying, _unite bigint, _entete character varying, _quantite double precision, _prix_total double precision, _rang integer);
	DELETE FROM table_listing_achat_by_fournisseur;
	IF(fournisseur_ IS NOT NULL AND fournisseur_ > 0)THEN
		where_ = where_ || ' AND y.id = '||fournisseur_;
	END IF;
	IF(COALESCE(agence_,0)> 0)THEN
		save_ = save_ || ' AND a.id = '||agence_;
	END IF;
-- 	RAISE NOTICE 'query_ %',query_;
	IF(cumul_=0)THEN
		
	ELSIF(cumul_=1) THEN --Par fournisseur et par article
		ristourne_ = 'SELECT c.article as id, SUM(COALESCE(c.quantite_recu, 0)) as quantite, SUM(COALESCE(c.prix_total, 0)) as prix_total, c.conditionnement';
	ELSIF(cumul_=2) THEN --Par fournisseur
		ristourne_ = 'SELECT y.id, y.code_fsseur, CONCAT(y.nom, '' '', y.prenom) AS nom, SUM(COALESCE(c.quantite_recu, 0)) as quantite, SUM(COALESCE(c.prix_total, 0)) as prix_total';
	END IF;
	FOR fournisseurs_ IN EXECUTE colonne_ || save_ || where_
	LOOP
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_, true)
		LOOP
			jour_ = dates_.intitule;
			query_ = ristourne_ || save_ || ' AND d.date_doc BETWEEN'||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND y.id = '||fournisseurs_.id;
			IF(cumul_=0)THEN
				--rien	
			ELSIF(cumul_=1) THEN --Par fournisseur et par article
					query_ = query_ || ' GROUP BY y.id, c.article, c.conditionnement';
			ELSIF(cumul_=2) THEN --Par fournisseur
					query_ = query_ || ' GROUP BY y.id, y.id';	
			END IF;
			RAISE NOTICE 'query_ : %',query_;
			FOR data_ IN EXECUTE query_
			LOOP
				IF(cumul_=0)THEN
					code_depot_=data_.abbreviation;
					name_depot_=data_.designation;
					cond_=data_.conditionnement;
				END IF;
				IF(cumul_=1)THEN
					cond_=data_.conditionnement;
				END IF;
				INSERT INTO table_listing_achat_by_fournisseur VALUES(fournisseurs_.id, fournisseurs_.code_fsseur, fournisseurs_.nom, data_.id, code_depot_, name_depot_, cond_, jour_, data_.quantite, data_.prix_total, dates_.position);
			END LOOP;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_listing_achat_by_fournisseur ORDER BY _code, _rang, _unite;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_listing_achat_by_fournisseur(bigint, bigint, bigint, date, date, character varying, integer)
  OWNER TO postgres;
