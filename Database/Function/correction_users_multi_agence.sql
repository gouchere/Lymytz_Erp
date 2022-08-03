-- Function: insertion_avancement(bigint)
-- DROP FUNCTION insertion_avancement(bigint);
CREATE OR REPLACE FUNCTION correction_users_multi_agence()
  RETURNS boolean AS
$BODY$
DECLARE 
    user_ RECORD;
BEGIN
	FOR user_ IN SELECT u.id, a.societe FROM yvs_users u INNER JOIN yvs_agences a ON u.agence = a.id WHERE u.acces_multi_agence IS TRUE
	LOOP
		INSERT INTO yvs_users_agence (users, agence, connecte, actif, can_action, author, date_update, date_save) 
			SELECT user_.id, a.id, false, true, true, a.author, current_date, current_date FROM
			yvs_agences a LEFT JOIN yvs_users_agence ua ON ua.agence = a.id WHERE ua.id IS NULL AND a.societe = user_.societe;
	END LOOP;
	
	-- FOR user_ IN SELECT DISTINCT u.id, n.societe FROM yvs_users u INNER JOIN yvs_niveau_users y ON y.id_user INNER JOIN yvs_niveau_acces n ON n.id = y.id_niveau INNER JOIN yvs_autorisation_ressources_page ur ON n.id = ur.niveau_acces
		-- INNER JOIN yvs_ressources_page r ON ur.ressource_page = r.id WHERE r.yvs_ressources_page = 'param_agenc_vu_' AND ur.acces IS TRUE
	-- LOOP
		-- INSERT INTO yvs_users_agence (users, agence, connecte, actif, author, date_update, date_save) SELECT user_.id, a.id, false, true, a.author, current_date, current_date FROM
			-- yvs_agences a LEFT JOIN yvs_users_agence ua ON ua.agence = a.id WHERE ua.id IS NULL AND a.societe = user_.societe;
	-- END LOOP;
	
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION correction_users_multi_agence()
  OWNER TO postgres;
