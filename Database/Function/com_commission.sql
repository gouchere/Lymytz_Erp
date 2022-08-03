-- Function: com_commission(bigint, bigint)
-- DROP FUNCTION com_commission(bigint, bigint);
CREATE OR REPLACE FUNCTION com_commission(IN plan_ bigint, IN vente_ bigint)
  RETURNS TABLE(vente bigint, contenu bigint, facteur bigint, taux double precision, cible character varying, nature character varying) AS
$BODY$
DECLARE 
	taux_ DOUBLE PRECISION DEFAULT 0;
	total_ DOUBLE PRECISION DEFAULT 0;
	remise_ DOUBLE PRECISION DEFAULT 0;
	marge_ DOUBLE PRECISION DEFAULT 0;
	
	facteur_ RECORD;
	type_ RECORD;
	client_ RECORD;
	element_ RECORD;
	tranche_ RECORD;
	ligne_ RECORD;
	
	contenu_ BIGINT DEFAULT 0;

	evalue_ BOOLEAN DEFAULT FALSE;
	verifier_ BOOLEAN DEFAULT FALSE;
	lier_ BOOLEAN DEFAULT FALSE;

	cible_ CHARACTER VARYING DEFAULT '';
	nature_ CHARACTER VARYING DEFAULT 'T';
	
BEGIN 	
-- 	DROP TABLE IF EXISTS table_com_commission;
	CREATE TEMP TABLE IF NOT EXISTS table_com_commission(_vente BIGINT, _contenu BIGINT, _facteur BIGINT, _taux DOUBLE PRECISION, _cible CHARACTER VARYING, _nature CHARACTER VARYING); 
	DELETE FROM table_com_commission;
	FOR facteur_ IN SELECT f.id, f.general, f.nouveau_client, f.taux, t.id AS type, t.cible FROM yvs_com_facteur_taux f LEFT JOIN yvs_com_type_grille t ON t.id = f.type_grille LEFT JOIN yvs_com_periode_validite_commision p ON f.id = p.facteur WHERE f.plan = plan_ 
		AND (f.permanent IS TRUE OR (f.permanent IS FALSE AND ((p.periodicite = 'J' AND current_date <= p.date_fin ) OR (p.periodicite = 'A' AND p.date_debut <= current_date) OR (p.periodicite = 'D' AND current_date BETWEEN p.date_debut AND p.date_fin))))
	LOOP
		taux_ = 0;
		cible_ = '';
		nature_ = 'T';
		contenu_ = 0;
		lier_ = FALSE;
		evalue_ = FALSE;
		verifier_ = FALSE;
		
		SELECT INTO ligne_ y.client FROM yvs_com_doc_ventes y WHERE y.id = vente_;
		SELECT INTO client_ d.client AS id, MIN(c.date_entete) AS date_creation FROM yvs_com_entete_doc_vente c INNER JOIN yvs_com_doc_ventes d ON d.entete_doc = c.id 
				 WHERE d.client = ligne_.client GROUP BY d.client;
		IF(facteur_.nouveau_client)THEN -- Verification si le facteur est lié aux nouveaux clients
			IF(current_date - 30 <= client_.date_creation)THEN   -- Verification si le client est un nouveau client
				verifier_ = TRUE;
			END IF;
		ELSE
			verifier_ = TRUE;
		END IF;
		IF(verifier_)THEN	
			IF(facteur_.general)THEN -- Verification si le facteur est général donc n'est rattaché à aucun element
				total_ = (SELECT get_ca_vente(vente_));		
				IF(facteur_.type IS NULL OR facteur_.type < 1)THEN
					taux_ = COALESCE(facteur_.taux, 0);
				ELSE
					cible_ = facteur_.cible;
					CASE cible_
						WHEN 'C' THEN
							SELECT INTO tranche_ t.id, COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type AND (t.tranche_minimal <= total_ AND t.tranche_maximal >= total_);
						WHEN 'R' THEN
							
						WHEN 'M' THEN
							SELECT INTO tranche_ t.id,COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type AND (t.tranche_minimal <= marge_ AND t.tranche_maximal >= marge_);
					END CASE;
					IF(tranche_.id IS NOT NULL AND tranche_.id >0)THEN
						taux_ = tranche_.taux;
						nature_ = tranche_.nature;
					END IF;
				END IF;
				INSERT INTO table_com_commission VALUES(vente_, contenu_, facteur_.id, taux_, cible_, nature_);
			ELSE
 				FOR ligne_ IN SELECT ARRAY_AGG(y.id_externe) AS id_externe, table_externe FROM yvs_com_cible_facteur_taux y WHERE y.facteur = facteur_.id GROUP BY y.table_externe
				LOOP
					lier_ = FALSE;
					evalue_ = FALSE;
					evalue_ = FALSE;
					CASE ligne_.table_externe
						WHEN 'BASE_ARTICLE' THEN
							SELECT INTO element_ c.id, COALESCE(c.prix_total, 0) AS prix_total, COALESCE(c.ristourne, 0) AS ristourne, COALESCE(c.remise, 0) AS remise FROM yvs_com_contenu_doc_vente c WHERE c.doc_vente = vente_ AND c.article = ANY(ligne_.id_externe);
							IF(element_.id IS NOT NULL AND element_.id > 0)THEN	-- Verification si la facture est liée à l'article du facteur	
								total_ = element_.prix_total - element_.ristourne;
--  								RAISE NOTICE 'element_.ristourne : %',element_.ristourne;
-- 								remise_ = element_.remise;
								evalue_ = TRUE;	
							END IF;	
							lier_ = TRUE;
						WHEN 'DOC_ZONE'THEN
							SELECT INTO element_ c.client FROM yvs_com_doc_ventes c WHERE c.adresse = ligne_.id_externe OR (c.adresse in (SELECT y.id FROM yvs_dictionnaire y WHERE y.parent = ANY(ligne_.id_externe)));
							IF(element_.client IS NOT NULL AND element_.client > 0)THEN -- Verification si le client est de la catégorie du facteur
								total_ = (SELECT get_ca_vente(vente_));
								evalue_ = TRUE;	
							END IF;
						WHEN 'BASE_CLIENT' THEN
							IF(client_.id = ligne_.id_externe)THEN -- Verification si le client est de la catégorie du facteur
								total_ = (SELECT get_ca_vente(vente_));
								evalue_ = TRUE;	
							END IF;
						ELSE
							SELECT INTO element_ c.client FROM yvs_com_categorie_tarifaire c WHERE c.categorie = ligne_.id_externe AND c.client = ANY(client_.id);
							IF(element_.client IS NOT NULL AND element_.client > 0)THEN -- Verification si le client est de la catégorie du facteur
								total_ = (SELECT get_ca_vente(vente_));
								evalue_ = TRUE;	
							END IF;
					END CASE;
					IF(evalue_)THEN
						IF(facteur_.type IS NULL OR facteur_.type < 1)THEN
							taux_ = COALESCE(facteur_.taux, 0);
						ELSE
							cible_ = facteur_.cible;
							CASE cible_
								WHEN 'C' THEN
									SELECT INTO tranche_ t.id, COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type AND (t.tranche_minimal <= total_ AND t.tranche_maximal >= total_);
								WHEN 'R' THEN
									IF(remise_ > 0)THEN
										remise_ = (remise_ / (total_ + remise_));
									END IF;
									SELECT INTO tranche_ t.id,COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type AND (t.tranche_minimal <= remise_ AND t.tranche_maximal >= remise_);
								WHEN 'M' THEN
									SELECT INTO tranche_ t.id,COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type AND (t.tranche_minimal <= marge_ AND t.tranche_maximal >= marge_);
							END CASE;
							IF(tranche_.id IS NOT NULL AND tranche_.id >0)THEN
								taux_ = tranche_.taux;
								nature_ = tranche_.nature;
							END IF;
						END IF;
						IF(lier_ AND FALSE)THEN
							INSERT INTO table_com_commission SELECT vente_, c.id, facteur_.id, taux_, cible_, nature_ FROM yvs_com_contenu_doc_vente c WHERE c.doc_vente = vente_ AND c.article = ANY(ligne_.id_externe);
						ELSE
							INSERT INTO table_com_commission VALUES(vente_, contenu_, facteur_.id, taux_, cible_, nature_);
						END IF;
					END IF;
				END LOOP;
			END IF;
		END IF;
	END LOOP;
	RETURN QUERY SELECT * FROM table_com_commission ORDER BY _vente;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_commission(bigint, bigint)
  OWNER TO postgres;
