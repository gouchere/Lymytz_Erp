-- Function: com_et_mouvement_stock_desc(bigint, character varying)

-- DROP FUNCTION com_et_mouvement_stock_desc(bigint, character varying);

CREATE OR REPLACE FUNCTION com_et_mouvement_stock_desc(id_externe_ bigint, table_externe_ character varying)
  RETURNS character varying AS
$BODY$
declare 
   name_ character varying;
   result_ character varying;   
   line_ record;   
begin 	
	IF(table_externe_ = 'yvs_com_ration') THEN
			SELECT INTO name_ concat(COALESCE(t.nom,''),' ', COALESCE(t.prenom,'')) FROM yvs_com_ration y INNER JOIN yvs_base_tiers t ON t.id=y.personnel WHERE y.id=id_externe_ LIMIT 1;
			result_='RATION DE '||name_;
	ELSIF(table_externe_ = 'yvs_com_contenu_doc_achat') THEN
			SELECT INTO name_ concat(COALESCE(t.nom,''),' ', COALESCE(t.prenom,'')) FROM yvs_com_contenu_doc_achat y INNER JOIN yvs_com_doc_achats d ON d.id=y.doc_achat INNER JOIN yvs_base_fournisseur f ON f.id=d.fournisseur INNER JOIN yvs_base_tiers t ON t.id=f.tiers WHERE y.id=id_externe_ limit 1;
			result_='ACHAT CHEZ '||name_;
	ELSIF(table_externe_ = 'yvs_com_contenu_doc_stock_reception') THEN
			SELECT INTO line_ e.designation, u.nom_users FROM yvs_com_contenu_doc_stock_reception r INNER JOIN yvs_com_contenu_doc_stock c ON r.contenu = c.id INNER JOIN yvs_com_doc_stocks d ON d.id=c.doc_stock INNER JOIN yvs_base_depots e ON d.source = e.id INNER JOIN yvs_users_agence ua ON ua.id=r.author INNER JOIN yvs_users u ON u.id=ua.users WHERE r.id=id_externe_;
			result_='TRANSFERT VENANT DE '||line_.designation||' RECU PAR '||COALESCE(line_.nom_users, '');
	ELSIF(table_externe_ = 'yvs_prod_declaration_production') THEN
			SELECT INTO name_  u.nom_users FROM yvs_prod_declaration_production d INNER JOIN yvs_prod_session_of so ON so.id=d.session_of INNER JOIN yvs_prod_session_prod s ON so.session_prod=s.id INNER JOIN yvs_users u ON u.id=s.producteur WHERE d.id=id_externe_ limit 1;
			result_='PRODUCTION PAR '||name_;
	ELSIF(table_externe_ = 'yvs_prod_of_suivi_flux') THEN
		SELECT INTO name_ u.nom_users FROM yvs_prod_of_suivi_flux  sf INNER JOIN yvs_prod_suivi_operations sp ON sp.id=sf.id_operation						   
						     INNER JOIN yvs_prod_session_of so ON so.id=sp.session_of INNER JOIN yvs_prod_session_prod s ON so.session_prod=s.id INNER JOIN yvs_users u ON u.id=s.producteur  where sf.id=id_externe_ limit 1;
			result_='CONSOMMATION MP PAR '||name_;
	ELSIF(table_externe_ = 'yvs_com_contenu_doc_stock') THEN
			SELECT INTO line_ u.nom_users, type_doc, destination FROM yvs_com_contenu_doc_stock c INNER JOIN yvs_com_doc_stocks d ON d.id=c.doc_stock INNER JOIN yvs_users u ON u.id=d.valider_by WHERE c.id=id_externe_ limit 1;
			IF(line_.type_doc = 'TR') THEN
					SELECT INTO name_ d.designation FROM yvs_base_depots d WHERE d.id=line_.destination;
					result_='TRANSFERT VERS '||name_||' PAR '||COALESCE(line_.nom_users, '');
			ELSIF(line_.type_doc = 'ES') THEN
					result_='ENTREE PAR '||line_.nom_users;
			ELSIF(line_.type_doc = 'SS') THEN		
					result_='SORTIE PAR '||line_.nom_users;
			ELSIF(line_.type_doc = 'IN') THEN	
					result_='AJUSTEMENT INVENTAIRE PAR '||line_.nom_users;				
			ELSIF(line_.type_doc = 'FT') THEN	
					result_='RECONDITIONNEMENT PAR '||line_.nom_users;
			END IF;
	ELSIF(table_externe_ = 'yvs_com_contenu_doc_vente') THEN
			SELECT INTO name_ u.nom_users FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON d.id=c.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau INNER JOIN yvs_users u ON u.id=cu.users WHERE c.id=id_externe_ limit 1;
			result_='VENTE PAR '||name_;
	ELSE 
		result_='';
	END IF;
	RETURN result_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_et_mouvement_stock_desc(bigint, character varying)
  OWNER TO postgres;
