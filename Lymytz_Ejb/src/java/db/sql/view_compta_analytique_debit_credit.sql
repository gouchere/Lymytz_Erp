DROP MATERIALIZED VIEW IF EXISTS view_compta_analytique_debit_credit;
CREATE MATERIALIZED VIEW view_compta_analytique_debit_credit AS
SELECT
    p.date_piece,
    j.agence,
    a.societe,
    j.id AS journal_id,
    o.centre,
    c.report,
    SUM(o.debit)  AS debit,
    SUM(o.credit) AS credit
FROM public.yvs_compta_content_analytique o
JOIN public.yvs_compta_content_journal c ON o.contenu = c.id
JOIN public.yvs_compta_pieces_comptable p ON c.piece = p.id
JOIN public.yvs_compta_journaux j ON j.id = p.journal
JOIN public.yvs_agences a ON j.agence = a.id
GROUP BY
    p.date_piece,
    j.agence,
    a.societe,
    j.id,
    o.centre,   
    c.report
;



--# ...existing code...
--0 2 * * * postgres psql -d lymytz_demo_0 -c "REFRESH MATERIALIZED VIEW CONCURRENTLY view_compta_analytique_debit_credit;"
--# ...existing code...