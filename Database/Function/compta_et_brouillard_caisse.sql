-- Function: compta_et_brouillard_caisse(bigint, date, date)

-- DROP FUNCTION compta_et_brouillard_caisse(bigint, date, date);

CREATE OR REPLACE FUNCTION compta_et_brouillard_caisse(IN caisse_ bigint, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(id bigint, numero character varying, date_mvt date, note character varying, tiers character varying, mode character varying, montant double precision, type character varying, solde double precision, solde_periode double precision, id_externe bigint, table_externe character varying) AS
$BODY$
declare 
   ligne_ RECORD;
   solde_initial DOUBLE PRECISION DEFAULT 0;
   solde_periode DOUBLE PRECISION DEFAULT 0;
   solde_ DOUBLE PRECISION DEFAULT 0;

   mouvement_ CHARACTER VARYING DEFAULT 'DEPENSE'; 
   description_ CHARACTER VARYING; 
   query_ CHARACTER VARYING DEFAULT 'SELECT y.*, m.designation FROM yvs_compta_mouvement_caisse y LEFT JOIN yvs_base_mode_reglement m ON y.model = m.id WHERE y.statut_piece = ''P'' AND y.table_externe NOT IN (''NOTIF_ACHAT'', ''NOTIF_VENTE'', ''BON_PROVISOIRE'') AND 
					(y.table_externe NOT IN (''DOC_VENTE'',''DOC_ACHAT'') OR (y.table_externe = ''DOC_VENTE'' AND y.id_externe NOT IN (SELECT DISTINCT(a.piece_vente) FROM yvs_compta_notif_reglement_vente a)) 
							OR (y.table_externe = ''DOC_ACHAT'' AND y.id_externe NOT IN (SELECT DISTINCT(a.piece_achat) FROM yvs_compta_notif_reglement_achat a)))';
   
begin 	
	--DROP TABLE table_et_brouillard_caisse;
	CREATE TEMP TABLE IF NOT EXISTS table_et_brouillard_caisse(_id bigint, _numero character varying, _date_mvt date, _note character varying, _tiers character varying, _mode character varying, _montant double precision, _type character varying, _solde double precision, _solde_periode double precision, _id_externe bigint, _table_externe character varying);
	DELETE FROM table_et_brouillard_caisse;
	IF(caisse_ IS NOT NULL AND caisse_ > 0)THEN
		query_ = query_ || ' AND y.caisse = '||caisse_;
		query_ = query_ || ' AND y.date_paye BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
		solde_initial = (SELECT compta_total_caisse(0, caisse_, 0, '', '', 'ESPECE,BANQUE', 'P', (date_debut_ - interval '1 day')::date));
		solde_ = solde_initial;
		INSERT INTO table_et_brouillard_caisse VALUES(0, 'S.I', (date_debut_ - interval '1 day'), 'SOLDE INITIAL', '', 'ESPECE', 0, '', solde_, 0, 0);
		FOR ligne_ IN EXECUTE query_ || ' ORDER BY y.date_paye, y.mouvement DESC, y.numero, y.id'
		LOOP
			description_ = ligne_.note;
			IF(description_ IS NULL OR description_ IN ('', ' '))THEN
				IF(ligne_.table_externe = 'DOC_ACHAT')THEN
					description_ = 'Reglement Achat N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'DOC_VENTE')THEN
					description_ = 'Reglement Vente N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'MISSION')THEN
					description_ = 'Reglement Mission N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'DOC_VIREMENT')THEN
					description_ = 'Virement N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'ACOMPTE_VENTE')THEN
					description_ = 'Acompte Client N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'ACOMPTE_ACHAT')THEN
					description_ = 'Acompte Fournisseur N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'CREDIT_VENTE')THEN
					description_ = 'Crédit Client N° '||ligne_.reference_externe;
				ELSIF(ligne_.table_externe = 'CREDIT_ACHAT')THEN
					description_ = 'Crédit Fournisseur N° '||ligne_.reference_externe;
				END IF;
			END IF;
			IF(ligne_.mouvement = 'R')THEN
				mouvement_ = 'RECETTE';
				solde_ = solde_ + ligne_.montant;
				solde_periode = solde_periode + ligne_.montant;
			ELSE
				mouvement_ = 'DEPENSE';
				solde_ = solde_ - ligne_.montant;
				solde_periode = solde_periode - ligne_.montant;
			END IF;
			INSERT INTO table_et_brouillard_caisse VALUES(ligne_.id, ligne_.numero, ligne_.date_paye, description_, ligne_.name_tiers, ligne_.designation, ligne_.montant, mouvement_, solde_, solde_periode, ligne_.id_externe, ligne_.table_externe);
		END LOOP;
	END IF;
	RETURN QUERY SELECT * FROM table_et_brouillard_caisse ORDER BY _date_mvt, _type DESC, _numero;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_brouillard_caisse(bigint, date, date)
  OWNER TO postgres;
