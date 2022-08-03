-- Function: com_et_solde_fournisseur(bigint, bigint, character varying, date, date, character varying);
-- DROP FUNCTION com_et_solde_fournisseur(bigint, bigint, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION com_et_solde_fournisseur(societe_ bigint, agence_ bigint, reference_ character varying, date_debut_ date, date_fin_ date, periode_ character varying)
RETURNS TABLE(id bigint, code character varying, nom character varying, entete character varying, solde_initial double precision, coup_sup double precision, ttc double precision, avance double precision, acompte double precision, credit double precision, solde double precision, rang integer) AS
$BODY$
declare 
   record_ record;
   dates_ record;
   
   solde_ double precision default 0;
   solde_initial_ double precision default 0;
   coup_sup_m double precision default 0;
   coup_sup_p double precision default 0;
   coup_sup_ double precision default 0;
   ttc_ double precision default 0;
   avance_ double precision default 0;
   acompte_e double precision default 0;
   acompte_r double precision default 0;
   acompte_ double precision default 0;
   credit_e double precision default 0;
   credit_r double precision default 0;
   credit_ double precision default 0;
   
   query_ CHARACTER VARYING DEFAULT 'SELECT y.id, y.code_fsseur AS code, CONCAT(y.nom, '' '', y.prenom) AS nom FROM yvs_base_fournisseur y INNER JOIN yvs_base_tiers t ON y.tiers = t.id WHERE t.societe = ' ||COALESCE(societe_ , 0);
   
begin 	
	DROP TABLE IF EXISTS table_et_solde_fournisseur;
	CREATE TEMP TABLE IF NOT EXISTS table_et_solde_fournisseur(_id_ bigint, _code_ character varying, _nom_ character varying, _entete_ character varying, _solde_initial_ double precision, _coup_sup_ double precision, _ttc_ double precision, _avance_ double precision, _acompte_ double precision, _credit_ double precision, _solde_ double precision, _rang_ integer);
	IF(reference_ IS NOT NULL AND reference_ != '')THEN
		query_ = query_ || ' AND y.id::character varying IN (SELECT TRIM(val) FROM regexp_split_to_table('''||reference_||''', '','') val)';
	END IF;
	FOR record_ IN EXECUTE query_
	LOOP
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_, TRUE)
		LOOP
			-- COUP SUP
			query_ = 'SELECT SUM(y.montant) FROM yvs_com_cout_sup_doc_achat y INNER JOIN yvs_com_doc_achats d ON y.doc_achat = d.id INNER JOIN yvs_grh_type_cout t ON y.type_cout = t.id 
				 WHERE d.statut = ''V'' AND d.type_doc = ''FA'' AND d.fournisseur = '||QUOTE_LITERAL(record_.id)||' AND d.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
				query_ = query_ || ' AND d.agence = '||agence_;
			END IF;
			EXECUTE query_ || ' AND t.augmentation IS TRUE' INTO coup_sup_p;
			EXECUTE query_ || ' AND t.augmentation IS FALSE' INTO coup_sup_m;
			coup_sup = COALESCE(coup_sup_p, 0) - COALESCE(coup_sup_m, 0);
			-- TTC
			query_ = 'SELECT SUM(y.prix_total) FROM yvs_com_contenu_doc_achat y INNER JOIN yvs_com_doc_achats d ON y.doc_achat = d.id 
				 WHERE d.statut = ''V'' AND d.type_doc = ''FA'' AND d.fournisseur = '||QUOTE_LITERAL(record_.id)||' AND d.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
				query_ = query_ || ' AND d.agence = '||agence_;
			END IF;
			EXECUTE query_ INTO ttc_;
			-- TTC
			query_ = 'SELECT SUM(y.prix_total) FROM yvs_com_contenu_doc_achat y INNER JOIN yvs_com_doc_achats d ON y.doc_achat = d.id 
				 WHERE d.statut = ''V'' AND d.type_doc = ''FA'' AND d.fournisseur = '||QUOTE_LITERAL(record_.id)||' AND d.date_doc BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
				query_ = query_ || ' AND d.agence = '||agence_;
			END IF;
			EXECUTE query_ INTO ttc_;
			-- CREDIT
			query_ = 'SELECT SUM(y.montant) FROM yvs_compta_credit_fournisseur y LEFT JOIN yvs_compta_journaux j ON y.journal = j.id 
				 WHERE y.statut = ''V'' AND y.fournisseur = '||QUOTE_LITERAL(record_.id)||' AND y.date_credit BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
				query_ = query_ || ' AND j.agence = '||agence_;
			END IF;
			EXECUTE query_ INTO credit_e;
			query_ = 'SELECT SUM(y.valeur) FROM yvs_compta_reglement_credit_fournisseur y INNER JOIN yvs_compta_credit_fournisseur c ON y.credit = c.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id 
				 WHERE y.statut = ''P'' AND c.fournisseur = '||QUOTE_LITERAL(record_.id)||' AND y.date_paiement BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
				query_ = query_ || ' AND j.agence = '||agence_;
			END IF;
			EXECUTE query_ INTO credit_r;
			credit_ = COALESCE(credit_e, 0) - COALESCE(credit_r, 0);
			-- ACOMPTE
			query_ = 'SELECT SUM(y.montant) FROM yvs_compta_acompte_fournisseur y LEFT JOIN yvs_base_caisse c ON y.caisse = c.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id 
				 WHERE y.statut = ''P'' AND y.fournisseur = '||QUOTE_LITERAL(record_.id)||' AND y.date_acompte BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
				query_ = query_ || ' AND j.agence = '||agence_;
			END IF;
			EXECUTE query_ INTO acompte_e;
			query_ = 'SELECT SUM(p.montant) FROM yvs_compta_notif_reglement_achat y INNER JOIN yvs_compta_caisse_piece_achat p ON y.piece_achat = p.id INNER JOIN yvs_base_caisse c ON p.caisse = c.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id 
				 WHERE p.statut_piece = ''P'' AND p.fournisseur = '||QUOTE_LITERAL(record_.id)||' AND p.date_paiement BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
				query_ = query_ || ' AND j.agence = '||agence_;
			END IF;
			EXECUTE query_ INTO acompte_r;
			acompte_ = COALESCE(acompte_e, 0) - COALESCE(acompte_r, 0);
			EXECUTE query_ INTO acompte_e;
			-- AVANCE
			query_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_achat y INNER JOIN yvs_base_caisse c ON y.caisse = c.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id 
				 WHERE y.statut_piece = ''P'' AND y.fournisseur = '||QUOTE_LITERAL(record_.id)||' AND y.date_paiement BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			IF(agence_ IS NOT NULL AND agence_ > 0)THEN		
				query_ = query_ || ' AND j.agence = '||agence_;
			END IF;
			EXECUTE query_ INTO avance_;
			solde_ = COALESCE(solde_initial_, 0) + (COALESCE(avance_, 0) + COALESCE(acompte_, 0)) -  (COALESCE(ttc_, 0) + COALESCE(coup_sup_, 0) + COALESCE(credit_, 0));
			IF(COALESCE(solde_, 0) != 0)THEN
				INSERT INTO table_et_solde_fournisseur VALUES(record_.id, record_.code, record_.nom, dates_.intitule, COALESCE(solde_initial_, 0), COALESCE(coup_sup_, 0), COALESCE(ttc_, 0), COALESCE(avance_, 0), COALESCE(acompte_, 0), COALESCE(credit_, 0), COALESCE(solde_, 0), dates_.position);
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_solde_fournisseur ORDER BY _nom_, _rang_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_et_solde_fournisseur(bigint, bigint, character varying, date, date, character varying)
  OWNER TO postgres;
