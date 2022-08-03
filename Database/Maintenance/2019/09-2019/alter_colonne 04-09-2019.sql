ALTER TABLE yvs_compta_pieces_comptable ADD COLUMN piece_extourne bigint;
-- Function: com_et_mouvement_stock_desc(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying)

-- DROP FUNCTION com_et_mouvement_stock(bigint, bigint, bigint, bigint, bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION com_et_mouvement_stock_num(id_externe_ bigint, table_externe_ character varying)
  RETURNS character varying AS
$BODY$
declare 
   name_ character varying;
   result_ character varying;   
   line_ record;   
begin 	
	CASE table_externe_ 
		WHEN 'yvs_com_ration' THEN
			SELECT INTO name_ d.num_doc FROM yvs_com_ration y INNER JOIN yvs_com_doc_ration d ON d.id=y.doc_ration WHERE y.id=id_externe_ LIMIT 1;
		WHEN 'yvs_com_contenu_doc_achat' THEN
			SELECT INTO name_ d.num_doc FROM yvs_com_contenu_doc_achat y INNER JOIN yvs_com_doc_achats d ON d.id=y.doc_achat ;
		WHEN 'yvs_com_contenu_doc_stock_reception' THEN
			SELECT INTO name_  d.num_doc FROM yvs_com_contenu_doc_stock_reception r INNER JOIN yvs_com_contenu_doc_stock c ON c.id=r.contenu INNER JOIN yvs_com_contenu_doc_stock d ON d.id=c.doc_stock WHERE r.id=id_externe_;
		WHEN 'yvs_prod_declaration_production' THEN
			SELECT INTO name_  o.code_ref FROM yvs_prod_declaration_production d INNER JOIN yvs_prod_session_of so ON so.id=d.session_of INNER JOIN yvs_prod_ordre_fabrication o ON so.ordre=o.id WHERE d.id=id_externe_ limit 1;
		WHEN 'yvs_prod_of_suivi_flux' THEN
		SELECT INTO name_  o.code_ref FROM yvs_prod_of_suivi_flux  sf INNER JOIN yvs_prod_suivi_operations sp ON sp.id=sf.id_operation						   
						     INNER JOIN yvs_prod_session_of so ON so.id=sp.session_of INNER JOIN  yvs_prod_ordre_fabrication o ON so.ordre=o.id WHERE sf.id=id_externe_ limit 1;
		WHEN 'yvs_com_contenu_doc_stock' THEN
			SELECT INTO name_ d.num_doc FROM yvs_com_contenu_doc_stock c INNER JOIN yvs_com_doc_stocks d ON d.id=c.doc_stock INNER JOIN yvs_users u ON u.id=d.valider_by WHERE c.id=id_externe_ limit 1;			
		WHEN 'yvs_com_contenu_doc_vente' THEN
			SELECT INTO name_ d.num_doc FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_vente d ON d.id=c.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau INNER JOIN yvs_users u ON u.id=cu.users WHERE c.id=id_externe_ limit 1;
	END CASE;
	RETURN name_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_et_mouvement_stock_num(bigint,character varying)
  OWNER TO postgres;

