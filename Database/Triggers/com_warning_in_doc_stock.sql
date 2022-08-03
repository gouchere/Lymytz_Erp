-- Function: com_warning_in_doc_stock()
-- DROP FUNCTION com_warning_in_doc_stock();
CREATE OR REPLACE FUNCTION com_warning_in_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	agence_ bigint;

	action_ character varying;
	titre_ character varying default 'ENTREE_STOCK';

	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER_ALERTE');
BEGIN	
	IF(EXEC_T_) THEN
		action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE    
		IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
			CASE NEW.type_doc
				WHEN 'SS' THEN
					titre_='SORTIE_STOCK';
				WHEN 'TR' THEN
					titre_='RECONDITIONNEMENT_STOCK';
				WHEN 'IN' THEN
					titre_='INVENTAIRE_STOCK';
				WHEN 'FT' THEN
					titre_='TRANSFERT_STOCK';
				ELSE
					titre_='ENTREE_STOCK';				
			END CASE;
			IF(NEW.type_doc = 'ES')THEN
				SELECT INTO agence_ de.agence FROM yvs_com_doc_stocks d INNER JOIN yvs_base_depots de ON de.id=d.destination WHERE d.id = NEW.id;
			ELSE
				SELECT INTO agence_ de.agence FROM yvs_com_doc_stocks d INNER JOIN yvs_base_depots de ON de.id=d.source WHERE d.id = NEW.id;
			END IF;
			PERFORM workflow_add_warning(NEW.id, titre_, 'VALIDATION', NEW.date_doc, action_, NEW.statut IN ('V', 'T', 'C', 'A'), agence_, NEW.author);
		ELSE 
			CASE OLD.type_doc
				WHEN 'SS' THEN
					titre_='SORTIE_STOCK';
				WHEN 'TR' THEN
					titre_='RECONDITIONNEMENT_STOCK';
				WHEN 'IN' THEN
					titre_='INVENTAIRE_STOCK';
				WHEN 'FT' THEN
					titre_='TRANSFERT_STOCK';
				ELSE
					titre_='ENTREE_STOCK';				
			END CASE;
			DELETE FROM yvs_workflow_alertes a USING yvs_workflow_model_doc w WHERE a.model_doc = w.id AND a.id_element = OLD.id AND w.titre_doc = titre_;
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
ALTER FUNCTION com_warning_in_doc_stock()
  OWNER TO postgres;
