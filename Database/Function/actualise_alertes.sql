-- Function: update_()

-- DROP FUNCTION update_();

CREATE OR REPLACE FUNCTION actualise_alertes()
  RETURNS boolean AS
$BODY$   
	DECLARE	
		
	BEGIN
		SELECT set_config('mayapp.EXECUTE_MVT_STOCK','false',true);
		SELECT set_config('mayapp.EXECUTE_TRIGGER','false',true);
		
		UPDATE yvs_com_doc_ventes SET id=id WHERE statut='V' AND (statut_livre!='L' OR statut_regle!='R') AND type_doc='FV';
		UPDATE yvs_com_doc_ventes SET id=id WHERE statut!='V';
		UPDATE yvs_com_doc_achats SET id=id WHERE statut='V' AND (statut_livre!='L' OR statut_regle!='R') AND type_doc='FV';
		UPDATE yvs_com_doc_achats SET id=id WHERE statut!='V';
		UPDATE yvs_com_doc_stocks SET id=id WHERE statut!='V';
		
		SELECT set_config('mayapp.EXECUTE_MVT_STOCK','true',true);
		SELECT set_config('mayapp.EXECUTE_TRIGGER','true',true);
		RETURN TRUE;
	END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION actualise_alertes()
  OWNER TO postgres;
