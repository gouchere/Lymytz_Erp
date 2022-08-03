-- Function: action_in_header_vente_or_piece_virement(bigint)

-- DROP FUNCTION action_in_header_vente_or_piece_virement(bigint);

CREATE OR REPLACE FUNCTION action_in_header_vente_or_piece_virement(header_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE 
	entete_ RECORD;
	
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	ecart_ DOUBLE PRECISION DEFAULT 0;

	id_ BIGINT DEFAULT 0;

	numero_ CHARACTER VARYING DEFAULT '';
	reference_ CHARACTER VARYING DEFAULT '';
	numeric_ INTEGER DEFAULT 0;
   
BEGIN 	
	IF(COALESCE(header_, 0) > 0)THEN
		SELECT INTO entete_ y.date_entete, h.users, y.author, a.societe FROM yvs_com_entete_doc_vente y INNER JOIN yvs_com_creneau_horaire_users h ON y.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id WHERE y.id = header_;	
		ca_ = (SELECT com_get_versement_attendu(entete_.users, entete_.date_entete, entete_.date_entete));
		SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_virement y INNER JOIN yvs_compta_notif_versement_vente n ON n.piece = y.id WHERE y.statut_piece = 'P' AND n.entete_doc = header_;
		avance_ = COALESCE(avance_, 0);
		ecart_ =  avance_ - ca_;
		SELECT INTO id_ y.id FROM yvs_com_ecart_entete_vente y WHERE y.entete_doc = header_;
		IF(COALESCE(id_, 0) > 0)THEN
			IF(ecart_ != 0)THEN
				UPDATE yvs_com_ecart_entete_vente SET montant = ecart_ WHERE id = id_;
			ELSE
				DELETE FROM yvs_com_ecart_entete_vente WHERE id = id_;
			END IF;
		ELSIF(ecart_ != 0)THEN
			numero_ = 'ECR/';
			numero_ = numero_ || to_char(entete_.date_entete ,'ddMMyy') || '/';
			SELECT INTO reference_ y.numero FROM yvs_com_ecart_entete_vente y INNER JOIN yvs_users u ON y.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id WHERE numero LIKE numero_ ||'%' AND a.societe = entete_.societe;
			IF(COALESCE(reference_, '') != '')THEN
				numeric_ = TRIM(REPLACE(reference_, numero_, ' '))::INTEGER;
				numeric_ = numeric_ + 1;
				numero_ = numero_ || REPLACE(to_char(numeric_,'999'), ' ', '0');
			ELSE
				numero_ = numero_ || REPLACE(to_char(1,'999'), ' ', '0');
			END IF;
			INSERT INTO yvs_com_ecart_entete_vente(date_ecart, numero, montant, entete_doc, users, statut, statut_regle, author) 
				VALUES(entete_.date_entete, numero_, ecart_, header_, entete_.users, 'E', 'W', entete_.author);
		END IF;
		RETURN TRUE;
	END IF;
	RETURN FALSE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_in_header_vente_or_piece_virement(bigint)
  OWNER TO postgres;
