-- Function: com_calcul_commission(character varying, character varying, character varying, bigint)

-- DROP FUNCTION com_calcul_commission(character varying, character varying, character varying, bigint);

CREATE OR REPLACE FUNCTION com_calcul_commission(ids_ character varying, commerciaux_ character varying, points_ character varying, periode_ bigint)
  RETURNS SETOF bigint AS
$BODY$
DECLARE 	
	commission_ RECORD;
	facture_ RECORD;
	facteur_ RECORD;
	ligne_ RECORD;

	compteur_ BIGINT DEFAULT 1;
	max_ BIGINT DEFAULT 1;
	vente_ BIGINT DEFAULT 0;
	last_ BIGINT DEFAULT 0;
	data_ BIGINT DEFAULT 0;
	
	date_ DATE DEFAULT CURRENT_DATE;
	
	somme_ DOUBLE PRECISION DEFAULT 0;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	prec_ DOUBLE PRECISION DEFAULT 0;

	colonne_ CHARACTER VARYING DEFAULT 'DISTINCT y.id, y.statut, y.statut_regle, e.date_entete, co.id AS commercial, co.commission';

	query_ CHARACTER VARYING DEFAULT 'SELECT '||colonne_||' FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id
					LEFT JOIN yvs_compta_caisse_piece_vente c ON c.vente = y.id INNER JOIN yvs_com_commercial_vente o ON o.facture = y.id INNER JOIN yvs_com_comerciale co ON co.id = o.commercial ';
	where_ CHARACTER VARYING DEFAULT 'WHERE o.commercial IS NOT NULL AND y.type_doc = ''FV'' AND co.commission IS NOT NULL AND y.statut = ''V'' AND o.responsable AND co.commission IS NOT NULL AND ((y.statut NOT IN (''E'',''A'') ';
BEGIN 	
-- 	DROP TABLE IF EXISTS table_calcul_commission;
	CREATE TEMP TABLE IF NOT EXISTS table_calcul_commission(_vente BIGINT); 
	DELETE FROM table_calcul_commission;

	-- Recuperation des informations de la periode
	SELECT INTO ligne_ y.* FROM yvs_com_periode_objectif y WHERE y.id = periode_;
	IF(ligne_.id IS NOT NULL AND ligne_.id > 0)THEN
		-- Recherche des factures enregistrées dans la période
		where_ = where_ || 'AND e.date_entete BETWEEN '''||ligne_.date_debut||''' AND '''||ligne_.date_fin||''') OR (c.statut_piece = ''P'' AND c.date_paiement BETWEEN '''||ligne_.date_debut||''' AND '''||ligne_.date_fin||'''))';
		IF(ids_ IS NOT NULL AND ids_ NOT IN ('', ' '))THEN
			where_ = where_ || 'AND y.id::character varying in (select val from regexp_split_to_table('''||ids_||''','','') val)';
		END IF;
		IF(commerciaux_ IS NOT NULL AND commerciaux_ NOT IN ('', ' '))THEN
			where_ = where_ || 'AND o.commercial::character varying in (select val from regexp_split_to_table('''||commerciaux_||''','','') val)';
		END IF;
		IF(points_ IS NOT NULL AND points_ NOT IN ('', ' '))THEN
			where_ = where_ || 'AND cp.point::character varying in (select val from regexp_split_to_table('''||points_||''','','') val)';
		END IF;
		query_ = query_ || where_;
		EXECUTE REPLACE(query_, colonne_, 'COUNT(DISTINCT y.id)') INTO max_;
		RAISE NOTICE '%',query_;
		FOR facture_ IN EXECUTE query_
		LOOP
			RAISE NOTICE '%/%',compteur_, max_;
			valeur_ = 0;
			last_ = 0;
			somme_ = 0;
			SELECT INTO data_ id FROM yvs_com_commission_vente WHERE facture = facture_.id AND periode = periode_;
			IF(data_ IS NOT NULL AND data_ > 0)THEN
				DELETE FROM yvs_com_commission_vente WHERE id = data_;
			END IF;
			-- Recuperation des taux de commission de la facture calculées pour le plan de commission du responsable
			-- RAISE NOTICE 'facture_.id : %',facture_.id;	
			-- RAISE NOTICE 'commercial_.id : %',commercial_.id;	
-- 			RAISE NOTICE 'facture_.commission : %',facture_.commission;
			FOR commission_ IN SELECT * FROM com_commission(facture_.commission, facture_.id)
			LOOP
				vente_ = 0;		
				-- Recuperation des informations du facteur de taux
				SELECT INTO facteur_ y.* FROM yvs_com_facteur_taux y WHERE y.id = commission_.facteur;
				IF(facteur_.champ_application = 'R')THEN -- Facteur de taux appliqué sur les factures reglées
					SELECT INTO date_ COALESCE(y.date_paiement, CURRENT_DATE) FROM yvs_compta_caisse_piece_vente y WHERE y.statut_piece = 'P' AND y.vente = facture_.id ORDER BY date_paiement DESC LIMIT 1;
					IF(facture_.statut_regle = 'P' and date_ BETWEEN ligne_.date_debut AND ligne_.date_fin)THEN
						vente_ = facture_.id;
					END IF;
				ELSIF(facteur_.champ_application = 'E')THEN -- Facteur de taux appliqué sur les factures encours de reglement
					SELECT INTO date_ COALESCE(y.date_paiement, CURRENT_DATE) FROM yvs_compta_caisse_piece_vente y WHERE y.statut_piece = 'P' AND y.vente = facture_.id ORDER BY date_paiement DESC LIMIT 1;
					IF(facture_.statut_regle IN ('R', 'P') and date_ BETWEEN ligne_.date_debut AND ligne_.date_fin)THEN
						vente_ = facture_.id;
					END IF;
				ELSE -- Facteur de taux appliqué sur les factures validées		
					IF(facture_.date_entete BETWEEN ligne_.date_debut AND ligne_.date_fin)THEN
						vente_ = facture_.id;
					END IF;
				END IF;
-- 				RAISE NOTICE 'vente_ : %',vente_;
				IF(vente_ > 0)THEN
					IF(facteur_.champ_application = 'E')THEN
						SELECT INTO somme_ SUM(COALESCE(y.montant, 0)) FROM yvs_compta_caisse_piece_vente y WHERE y.statut_piece = 'P' AND y.date_paiement BETWEEN ligne_.date_debut AND ligne_.date_fin AND y.vente = facture_.id ;
						IF(commission_.nature = 'T')THEN
							somme_ = (somme_ * commission_.taux) / 100;
						END IF;
						valeur_ = valeur_ + COALESCE(somme_, 0);
					ELSE
						IF(commission_.nature = 'T')THEN
							IF(commission_.contenu IS NOT NULL AND commission_.contenu > 0)THEN
								CASE facteur_.base
									WHEN 'H' THEN -- Facteur de taux basé sur le valeur HT
										SELECT INTO somme_ ((COALESCE(y.quantite, 0) * (COALESCE(y.prix, 0) - COALESCE(y.rabais, 0)) - COALESCE(y.remise, 0) - COALESCE(y.ristourne, 0))) FROM yvs_com_contenu_doc_vente y WHERE id = commission_.contenu;
									WHEN 'T' THEN -- Facteur de taux basé sur le valeur TTC
										SELECT INTO somme_ (COALESCE(y.prix_total, 0) - COALESCE(y.ristourne, 0)) FROM yvs_com_contenu_doc_vente y WHERE id = commission_.contenu;
									WHEN 'M' THEN -- Facteur de taux basé sur le valeur Marge
										SELECT INTO somme_ (COALESCE(y.prix_total, 0) - ((COALESCE(y.prix, 0) - COALESCE(y.pr, 0))* COALESCE(y.quantite, 0))) FROM yvs_com_contenu_doc_vente y WHERE id = commission_.contenu;
								END CASE;
								somme_ = (somme_ * commission_.taux) / 100;
								UPDATE yvs_com_contenu_doc_vente SET comission = somme_ WHERE id = commission_.contenu;
							ELSE
								CASE facteur_.base
									WHEN 'H' THEN -- Facteur de taux basé sur le valeur HT
										SELECT INTO somme_ (SUM(COALESCE(y.quantite, 0) * (COALESCE(y.prix, 0) - COALESCE(y.rabais, 0)) - COALESCE(y.remise, 0) - COALESCE(y.ristourne, 0))) FROM yvs_com_contenu_doc_vente y WHERE doc_vente = vente_;
									WHEN 'T' THEN -- Facteur de taux basé sur le valeur TTC
										SELECT INTO somme_ (SUM(COALESCE(y.prix_total, 0) - COALESCE(y.ristourne, 0))) FROM yvs_com_contenu_doc_vente y WHERE doc_vente = vente_;
									WHEN 'M' THEN -- Facteur de taux basé sur le valeur Marge
										SELECT INTO somme_ (SUM(COALESCE(y.prix_total, 0) - ((COALESCE(y.prix, 0) - COALESCE(y.pr, 0))* COALESCE(y.quantite, 0)))) FROM yvs_com_contenu_doc_vente y WHERE doc_vente = vente_;
								END CASE;
								somme_ = (somme_ * commission_.taux) / 100;
							END IF;
							valeur_ = valeur_ + somme_;
						ELSE
							IF(commission_.contenu IS NOT NULL AND commission_.contenu > 0)THEN
								UPDATE yvs_com_contenu_doc_vente SET comission = commission_.taux WHERE id = commission_.contenu;
							END IF;
							valeur_ = valeur_ + commission_.taux;
						END IF;
					END IF;
					last_ = vente_;	
				END IF;
			END LOOP;	
			UPDATE yvs_com_doc_ventes SET commision = COALESCE(valeur_, 0) WHERE id = facture_.id AND commision != COALESCE(valeur_, 0);
			IF(COALESCE(valeur_, 0) > 0)THEN
				SELECT INTO prec_ SUM(montant) FROM yvs_com_commission_vente WHERE facture = facture_.id AND periode != periode_;
				valeur_ = valeur_ - COALESCE(prec_, 0);
				SELECT INTO data_ id FROM yvs_com_commission_vente WHERE facture = facture_.id AND periode = periode_;
				IF(data_ IS NOT NULL AND data_ > 0)THEN
					UPDATE yvs_com_commission_vente SET montant = valeur_ WHERE id = data_;
				ELSE
					INSERT INTO yvs_com_commission_vente(facture, periode, montant) VALUES(facture_.id, periode_, valeur_);
				END IF;
				INSERT INTO table_calcul_commission VALUES(vente_);
			END IF;
			compteur_ = compteur_ + 1;
		END LOOP; 
	END IF;	
	RETURN QUERY SELECT * FROM table_calcul_commission ORDER BY _vente;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_calcul_commission(character varying, character varying, character varying, bigint)
  OWNER TO postgres;
