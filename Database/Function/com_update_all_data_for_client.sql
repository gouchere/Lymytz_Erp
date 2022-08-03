-- Function: com_update_all_data_for_client(bigint)

-- DROP FUNCTION com_update_all_data_for_client(bigint);

CREATE OR REPLACE FUNCTION com_update_all_data_for_client(client_ bigint)
  RETURNS boolean AS
$BODY$   
DECLARE 
	vente_ RECORD;
	commercial_ RECORD;
BEGIN
	FOR commercial_ IN SELECT y.id, y.tiers, e.compte_tiers FROM yvs_com_comerciale y INNER JOIN yvs_grh_employes e ON e.code_users = y.utilisateur
	LOOP
		IF(commercial_.tiers IS NULL OR commercial_.tiers < 1)THEN
			UPDATE yvs_com_comerciale SET tiers = commercial_.compte_tiers WHERE id = commercial_.id;
		END IF;
	END LOOP;
	FOR vente_ IN SELECT y.id, c.users, l.tiers, l.suivi_comptable FROM yvs_com_doc_ventes y INNER JOIN yvs_com_client l ON y.client = l.id INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id 
	WHERE y.client = client_ AND y.type_doc in ('FV','FAV') AND y.id NOT IN (SELECT w.ref_externe FROM yvs_compta_content_journal w WHERE w.table_externe = 'DOC_VENTE')
	LOOP
		SELECT INTO commercial_ y.id, c.tiers FROM yvs_com_commercial_vente y INNER JOIN yvs_com_comerciale c ON y.commercial = c.id WHERE y.facture = vente_.id AND y.responsable IS TRUE;
		IF(commercial_.id IS NULL OR commercial_.id < 1)THEN
			SELECT INTO commercial_ y.id, c.tiers FROM yvs_com_commercial_vente y INNER JOIN yvs_com_comerciale c ON y.commercial = c.id WHERE y.facture = vente_.id AND c.utilisateur = vente_.users;
			IF(commercial_.id IS NULL OR commercial_.id < 1)THEN
				SELECT INTO commercial_ y.id, y.tiers FROM yvs_com_comerciale y WHERE y.utilisateur = vente_.users;
				IF(commercial_.id IS NOT NULL OR commercial_.id > 0)THEN
					INSERT INTO yvs_com_commercial_vente(commercial, facture, taux, responsable) VALUES(commercial_.id, vente_.id, 100, true);
				END IF;
			END IF;
		END IF;
		IF(vente_.suivi_comptable IS TRUE)THEN
			UPDATE yvs_com_doc_ventes SET tiers = vente_.tiers WHERE id = vente_.id;
		ELSIF(commercial_.tiers IS NOT NULL OR commercial_.tiers > 0)THEN
			UPDATE yvs_com_doc_ventes SET tiers = commercial_.tiers WHERE id = vente_.id;
		ELSE
			SELECT INTO commercial_ y.id, e.compte_tiers AS tiers FROM yvs_users y INNER JOIN yvs_grh_employes e ON y.id = e.code_users WHERE y.id = vente_.users;
-- 			RAISE NOTICE 'commercial_.tiers %',commercial_.tiers;
			IF(commercial_.tiers IS NOT NULL OR commercial_.tiers > 0)THEN
				UPDATE yvs_com_doc_ventes SET tiers = commercial_.tiers WHERE id = vente_.id;
			END IF;
		END IF;
	END LOOP;
	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_update_all_data_for_client(bigint)
  OWNER TO postgres;
