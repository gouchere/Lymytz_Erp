CREATE OR REPLACE FUNCTION pgagent.create_refresh_mv_job(
    db_name TEXT,
    view_name TEXT
) RETURNS INTEGER AS $$
DECLARE
    job_class_id INTEGER;
    job_id INTEGER;
BEGIN
    -- 1. Récupérer la classe de job 'Routine Maintenance'
    SELECT jclid INTO job_class_id FROM pgagent.pga_jobclass WHERE jclname = 'Routine Maintenance' LIMIT 1;

    -- 2. Créer le job
    INSERT INTO pgagent.pga_job (jobjclid, jobname, jobdesc, jobenabled)
    VALUES (
        job_class_id,
        'Refresh ' || view_name,
        'Rafraîchit la vue matérialisée ' || view_name || ' chaque jour à 2h',
        true
    )
    RETURNING jobid INTO job_id;

    -- 3. Créer l'étape SQL du job
    INSERT INTO pgagent.pga_jobstep (jstjobid, jstname, jstdesc, jstenabled, jstkind, jstcode, jstdbname)
    VALUES (
        job_id,
        'refresh_view',
        'Rafraîchit la vue',
        true,
        's',
        'REFRESH MATERIALIZED VIEW CONCURRENTLY ' || view_name || ';',
        db_name
    );

    -- 4. Créer la planification (tous les jours à 2h)
    INSERT INTO pgagent.pga_schedule (
        jscjobid, jscname, jscdesc, jscenabled,
        jscminutes, jschours, jscweekdays, jscmonthdays, jscmonths
    )
    VALUES (
        job_id,
        'Tous les jours à 2h',
        'Planification quotidienne à 2h',
        true,
        ARRAY[
            false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
            false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false
        ], -- minutes (toutes à false, 60 éléments)
        ARRAY[
            false, false, true, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false, false, false
        ], -- heures (2h à true, 24 éléments)
        ARRAY[true,true,true,true,true,true,true], -- tous les jours de la semaine (7 éléments)
        ARRAY[
            false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false
        ], -- tous les jours du mois (32 éléments)
        ARRAY[
            true,true,true,true,true,true,true,true,true,true,true,true
        ] -- tous les mois (12 éléments)
    );

    RETURN job_id;
END;
$$ LANGUAGE plpgsql;

SELECT pgagent.create_refresh_mv_job('lymytz_demo_0', 'view_compta_debit_credit');
SELECT pgagent.create_refresh_mv_job('lymytz_demo_0', 'view_compta_analytique_debit_credit');