-- FUNCTION: public.delete_content_journal()

-- DROP FUNCTION IF EXISTS public.delete_content_journal();

CREATE OR REPLACE FUNCTION public.delete_content_journal()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
    
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
IF(OLD.report IS TRUE)THEN
    RAISE NOTICE '%, %, %',OLD.report, OLD.compte_general, OLD.piece;
    DELETE FROM yvs_compta_report_compte r USING yvs_compta_pieces_comptable p INNER JOIN yvs_compta_journaux j ON p.journal = j.id
		INNER JOIN yvs_agences a ON j.agence = a.id
    WHERE j.agence = r.agence AND r.compte = OLD.compte_general AND p.id = OLD.piece AND
    r.exercice = (SELECT e.id FROM yvs_base_exercice e INNER JOIN yvs_base_exercice n ON (e.date_fin < n.date_debut AND n.societe = e.societe) WHERE n.id = p.exercice AND n.societe = a.societe ORDER BY e.date_fin DESC LIMIT 1);
END IF;
return old;
END;
$BODY$;

ALTER FUNCTION public.delete_content_journal()
    OWNER TO postgres;
