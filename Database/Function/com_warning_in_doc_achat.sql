-- Function: com_warning_in_doc_achat()
-- DROP FUNCTION com_warning_in_doc_achat();
CREATE OR REPLACE FUNCTION com_warning_in_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	id_ bigint;
	agence_ bigint;
	author_ bigint;
	date_doc_ date;
	date_livraison_ date;
	date_reglement_ date;
	statut_ character varying;
	statut_regle_ character varying;
	statut_livre_ character varying;
	type_doc_ character varying;

	action_ character varying;
	titre_ character varying default 'FACTURE_ACHAT';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			id_ = NEW.id; 
			author_ = NEW.author; 
			date_doc_ = NEW.date_doc; 
			date_livraison_ = NEW.date_livraison; 
			statut_ = NEW.statut;
			statut_regle_ = NEW.statut_regle;
			statut_livre_ = NEW.statut_livre;
			type_doc_ = NEW.type_doc;
			agence_= NEW.agence;
		ELSE
			id_ = OLD.id;
			author_ = OLD.author; 
			date_doc_ = OLD.date_doc; 
			date_livraison_ = OLD.date_livraison; 
			statut_ = OLD.statut;
			statut_regle_ = OLD.statut_regle;
			statut_livre_ = OLD.statut_livre;
			type_doc_ = OLD.type_doc;
			agence_= OLD.agence;
		END IF;
		IF(agence_ IS NULL) THEN
			SELECT INTO agence_ d.agence FROM yvs_com_doc_achats a INNER JOIN yvs_base_depots d ON d.id=a.depot_reception WHERE a.id=id_;
		END IF;
		SELECT INTO date_reglement_ m.date_reglement FROM yvs_com_mensualite_facture_achat m WHERE m.facture = id_ ORDER BY m.date_reglement DESC LIMIT 1;
		IF(type_doc_='FA') THEN
			PERFORM workflow_add_warning(id_, 'FACTURE_ACHAT', 'VALIDATION', date_doc_, action_, statut_ IN ('V', 'T', 'C', 'A'), agence_, author_);
			IF(action_ = 'DELETE' OR action_ = 'TRUNCATE' OR statut_ = 'V')THEN
				PERFORM workflow_add_warning(id_, 'FACTURE_ACHAT_LIVRE', 'LIVRAISON', COALESCE(date_livraison_, date_doc_), action_, statut_livre_ IN ('L', 'C', 'A'), agence_, author_);
				PERFORM workflow_add_warning(id_, 'FACTURE_ACHAT_REGLE', 'REGLEMENT', COALESCE(date_reglement_, date_doc_), action_, statut_regle_ IN ('P', 'C', 'A'), agence_, author_);
			ELSIF(statut_ != 'V')THEN
				PERFORM workflow_add_warning(id_, 'FACTURE_ACHAT_LIVRE', 'LIVRAISON', COALESCE(date_livraison_, date_doc_), action_, true, agence_, author_);
				PERFORM workflow_add_warning(id_, 'FACTURE_ACHAT_REGLE', 'REGLEMENT', COALESCE(date_reglement_, date_doc_), action_, true, agence_, author_);
			END IF;
		ELSIF(type_doc_='BLA')THEN
			PERFORM workflow_add_warning(id_, 'BON_LIVRAISON_ACHAT', 'VALIDATION', COALESCE(date_livraison_, date_doc_), action_, statut_ IN ('V', 'T', 'C', 'A'), agence_, author_);
		ELSE
			IF(type_doc_ = 'FAA') THEN
				titre_ = 'AVOIR_ACHAT';
			ELSIF(type_doc_ = 'BRA') THEN	
				titre_ = 'RETOUR_ACHAT';
			END IF;
			PERFORM workflow_add_warning(id_, titre_, 'VALIDATION', date_doc_, action_, statut_ IN ('V', 'T', 'C', 'A'), agence_, author_);
		END IF;
	END IF;
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		RETURN NEW;
	ELSE
		RETURN OLD;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_warning_in_doc_achat()
  OWNER TO postgres;
