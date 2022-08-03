-- FUNCTION: public.update_delete_report_not_lie(bigint)
-- DROP FUNCTION IF EXISTS public.update_delete_report_not_lie(bigint);
CREATE OR REPLACE FUNCTION public.update_delete_report_not_lie(societe_ bigint)
RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
AS $BODY$
   
DECLARE	
	ligne_ RECORD;
	next_ BIGINT;
	count_ BIGINT;
BEGIN
	FOR ligne_ IN SELECT y.id, y.compte, y.exercice, y.agence FROM yvs_compta_report_compte y INNER JOIN yvs_agences a ON y.agence = a.id WHERE a.societe = societe_
	LOOP
		SELECT INTO next_ e.id FROM yvs_base_exercice e INNER JOIN yvs_base_exercice n ON (e.date_debut > n.date_fin AND e.societe = n.societe) 
			WHERE n.id = ligne_.exercice AND n.societe = societe_ ORDER BY e.date_debut ASC LIMIT 1;
		RAISE NOTICE 'exericice : % , next_ : %', ligne_.exercice, next_;
		IF(COALESCE(next_, 0) > 0)THEN
			SELECT INTO count_ COUNT(y.id) FROM yvs_compta_content_journal y INNER JOIN yvs_compta_pieces_comptable p ON y.piece = p.id 
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id 
				WHERE j.agence = ligne_.agence AND p.exercice = next_ AND y.compte_general = ligne_.compte;
			RAISE NOTICE 'count_ : %', count_;
            IF(COALESCE(count_, 0) < 1)THEN
                DELETE FROM yvs_compta_report_compte WHERE id = ligne_.id;
            END IF;
		END IF;
	END LOOP;
	RETURN TRUE;
END;
$BODY$;

ALTER FUNCTION public.update_delete_report_not_lie(bigint)
    OWNER TO postgres;
