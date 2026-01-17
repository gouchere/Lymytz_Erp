DROP TABLE IF EXISTS report.view_compta_debit_credit;
CREATE TABLE report.view_compta_debit_credit AS
SELECT
    p.date_piece,
    j.agence,
    a.societe,
    j.id AS journal_id,
    c.compte_general,
    c.compte_tiers,
    c.table_tiers,

    SUM(c.debit)  AS debit,
    SUM(c.credit) AS credit

FROM public.yvs_compta_content_journal c
JOIN public.yvs_compta_pieces_comptable p ON c.piece = p.id
JOIN public.yvs_compta_journaux j ON j.id = p.journal
JOIN public.yvs_agences a ON j.agence = a.id

GROUP BY
    p.date_piece,
    j.agence,
    a.societe,
    j.id,
    c.compte_general,
    c.compte_tiers,
    c.table_tiers;