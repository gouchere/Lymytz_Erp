-- Function: update_(date, date)

-- DROP FUNCTION update_(date, date);

CREATE OR REPLACE FUNCTION update_insert_commerciaux(point_ bigint, comercial_ bigint, debut_ date , fin_ date)
  RETURNS boolean AS
$BODY$   
DECLARE	
	ligne_ RECORD;
	i integer default 0;
	total bigint default 0;
	_nb bigint default 0;
	id_ bigint default 0;
BEGIN
	-- ALTER TABLE yvs_com_contenu_doc_vente DISABLE TRIGGER update_;
	select into total  COUNT(d.*) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc 
															INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau
															INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point
								  WHERE cp.point=point_ AND d.type_doc='FV' AND d.statut='V'
														AND e.date_entete BETWEEN debut_ AND fin_;
	FOR ligne_  IN select d.* FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc 
														INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau
														INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point
							  WHERE cp.point=point_ AND d.type_doc='FV' AND d.statut='V'
													AND e.date_entete BETWEEN debut_ AND fin_
	LOOP
		raise notice ' % / %',i, total;
		--Vérifie si la relation existe déjà
		SELECT INTO _nb COUNT(*) FROM yvs_com_commercial_vente WHERE facture=ligne_.id AND commercial=comercial_;
		IF(COALESCE(_nb,0)>1) THEN
			SELECT INTO id_ MIN(id) FROM yvs_com_commercial_vente WHERE facture=ligne_.id AND commercial=comercial_;
			--Supprimer les doublons
			DELETE FROM yvs_com_commercial_vente WHERE facture=ligne_.id AND commercial=comercial_ AND id!=id_;
			-- INSERT INTO yvs_com_commercial_vente(commercial, facture, taux, date_save, date_update, author,responsable, diminue_ca)
				   -- VALUES (comercial_, ligne_.id, 100, current_timestamp, current_timestamp, 93, true, false);
		ELSIF (_nb=0) THEN
			-- Insert 
			INSERT INTO yvs_com_commercial_vente(commercial, facture, taux, date_save, date_update, author,responsable, diminue_ca)
				   VALUES (comercial_, ligne_.id, 100, current_timestamp, current_timestamp, ligne_.author, true, false);
		END IF;
		i=i+1;
	END LOOP;
	-- ALTER TABLE yvs_com_contenu_doc_vente ENABLE TRIGGER update_;
	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_(date, date)
  OWNER TO postgres;
