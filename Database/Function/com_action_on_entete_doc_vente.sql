-- Function: com_action_on_entete_doc_vente()
-- DROP FUNCTION com_action_on_entete_doc_vente();
CREATE OR REPLACE FUNCTION com_action_on_entete_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE	
	action_ character varying;
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF (action_='UPDATE' AND NEW.cloturer != OLD.cloturer) THEN 
		IF(COALESCE(NEW.cloturer, false))THEN
-- 			PERFORM action_in_header_vente_or_piece_virement(NEW.id);
		ELSE
			DELETE FROM yvs_com_ecart_entete_vente WHERE entete_doc = NEW.id;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_action_on_entete_doc_vente()
  OWNER TO postgres;
