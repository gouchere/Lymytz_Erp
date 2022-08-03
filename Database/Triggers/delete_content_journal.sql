-- Function: delete_content_journal()

-- DROP FUNCTION delete_content_journal();

CREATE OR REPLACE FUNCTION delete_content_journal()
  RETURNS trigger AS
$BODY$    
DECLARE
	societe_ BIGINT;
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
IF(EXEC_) THEN
	IF(COALESCE(OLD.lettrage, '') != '')THEN
		SELECT INTO societe_ j.societe FROM yvs_base_plan_comptable p INNER JOIN yvs_base_nature_compte j ON p.nature_compte = j.id WHERE p.id = OLD.compte_general;
		UPDATE yvs_compta_content_journal SET lettrage = null FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id
			WHERE p.id = yvs_compta_content_journal.piece AND a.societe = societe_ AND yvs_compta_content_journal.lettrage = OLD.lettrage;
	END IF;
END IF;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_content_journal()
  OWNER TO postgres;
