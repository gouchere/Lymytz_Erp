-- FUNCTION: public.delete_content_journal()

-- DROP FUNCTION IF EXISTS public.delete_content_journal();

CREATE OR REPLACE FUNCTION public.delete_piece_comptable()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
    
DECLARE
	contenu_ record;
    EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
	FOR contenu_ IN SELECT compte_general from yvs_compta_content_journal where piece = OLD.id AND report IS TRUE
	LOOP
		DELETE FROM yvs_compta_report_compte r USING yvs_compta_pieces_comptable p INNER JOIN yvs_compta_journaux j ON p.journal = j.id
		INNER JOIN yvs_agences a ON j.agence = a.id
		WHERE j.agence = r.agence AND r.compte = contenu_.compte_general AND p.id = OLD.id AND
		r.exercice = (SELECT e.id FROM yvs_base_exercice e INNER JOIN yvs_base_exercice n ON (e.date_fin < n.date_debut AND n.societe = e.societe) WHERE n.id = p.exercice AND n.societe = a.societe ORDER BY e.date_fin DESC LIMIT 1);
	END LOOP;
	return old;
END;
$BODY$;

ALTER FUNCTION public.delete_piece_comptable()
    OWNER TO postgres;
    
-- Trigger: delete_
-- DROP TRIGGER IF EXISTS delete_ ON public.yvs_compta_pieces_comptable;
CREATE TRIGGER delete_
    BEFORE DELETE
    ON public.yvs_compta_pieces_comptable
    FOR EACH ROW
    EXECUTE PROCEDURE public.delete_piece_comptable();
