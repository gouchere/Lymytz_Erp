-- SELECT DISTINCT a.id as article, a.ref_art as reference, a.designation, a.categorie, c.id as unite, u.reference as numero, SUM(x.quantite) As quantite, f.sens
-- FROM yvs_prod_of_suivi_flux x LEFT JOIN yvs_prod_flux_composant f ON x.composant = f.id INNER JOIN yvs_prod_composant_of y ON f.composant = y.id
-- INNER JOIN yvs_base_conditionnement c ON y.unite = c.id LEFT JOIN yvs_base_unite_mesure u ON c.unite = u.id INNER JOIN yvs_base_articles a ON y.article = a.id
-- LEFT JOIN yvs_prod_suivi_operations p ON x.id_operation = p.id LEFT JOIN yvs_prod_session_of s ON p.session_of = s.id LEFT JOIN yvs_prod_session_prod e ON s.session_prod = e.id 
-- INNER JOIN yvs_prod_ordre_fabrication o ON y.ordre_fabrication = o.id INNER JOIN yvs_prod_session_of so ON o.id = so.ordre INNER JOIN yvs_prod_session_prod sp ON sp.id = so.session_prod
-- WHERE sp.date_session BETWEEN '01-04-2020' AND current_date AND a.id = 7301 AND sp.date_session != e.date_session GROUP BY a.id, c.id, u.id, f.sens ORDER BY a.designation, a.ref_art

-- SELECT sum(n_live_tup) FROM pg_stat_user_tables WHERE schemaname = 'public'

-- select proname,prosrc, proargnames from pg_proc where prosrc like '%search_text%';

--com_calcul_commission('', '', '', 21)

-- UPDATE table_name SET author = ? FROM table_name t1 INNER JOIN other_table t0 ON ... WHERE table_name.id = t1.id

-- UPDATE yvs_com_doc_ventes y SET depot_livrer = d.depot_livrer FROM yvs_com_doc_ventes d  WHERE d.depot_livrer != y.depot_livrer AND y.id = d.document_lie AND y.depot_livrer = 2077

-- INSERT INTO yvs_synchro_tables(table_name) SELECT tablename FROM pg_tables WHERE schemaname = 'public' AND tablename NOT LIKE 'pg_%' AND tablename NOT LIKE 'yvs_synchro%' ORDER BY tablename;

-- LISTE DES ENTETES AVEC UNE MAUVAISE AGENCE POUR UN USER
SELECT DISTINCT e.id FROM yvs_users y INNER JOIN yvs_com_creneau_horaire_users c ON c.users = y.id 
INNER JOIN yvs_com_entete_doc_vente e ON e.creneau = c.id INNER JOIN yvs_agences a ON e.agence = a.id 
WHERE e.agence = 2315 AND y.id = 3068

-- Verifie si il y'a des entete cree avec une mauvaise agence
SELECT DISTINCT e.id, e.date_entete FROM yvs_users y INNER JOIN yvs_com_creneau_horaire_users c ON c.users = y.id 
INNER JOIN yvs_com_entete_doc_vente e ON e.creneau = c.id
WHERE e.agence = 2315 AND e.date_entete BETWEEN '01-08-2021' AND '31-08-2021' AND y.agence != e.agence

-- Recherche des ligne de vente avec un prix_total different
SELECT c.doc_vente, d.num_doc, c.id, c.quantite, c.prix, c.remise, c.ristourne, c.prix_total, (prix_total - ((quantite * prix) - remise)), e.date_entete 
FROM yvs_com_contenu_doc_vente c LEFT JOIN yvs_com_taxe_contenu_vente t ON c.id = t.contenu INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id
 INNER JOIN yvs_com_entete_doc_vente e ON e.id = d.entete_doc
WHERE (prix_total - ((quantite * prix) - remise))> 2 AND (prix_total - (quantite * prix)) > 2 
AND e.date_entete BETWEEN '01-08-2021' AND '31-08-2021' AND type_doc = 'FV'
GROUP BY c.id, d.id, e.id HAVING COUNT(t.id) < 1 ORDER BY e.date_entete DESC

-- Verifie que les factures sont comptabilisées a la bonne date
SELECT DISTINCT d.id, d.num_doc, p.date_piece, e.date_entete FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
INNER JOIN yvs_compta_content_journal_facture_vente c ON c.facture = d.id INNER JOIN yvs_compta_pieces_comptable p On c.piece = p.id
WHERE e.date_entete BETWEEN '01-08-2021' AND '31-08-2021' AND e.date_entete != p.date_piece

--  Recherches des comptes de produits avec une valeur au debit
select p.num_compte, c.* FROM yvs_compta_content_journal c INNER JOIN yvs_base_plan_comptable p ON c.compte_general=p.id INNER JOIN yvs_base_nature_compte n ON n.id=p.nature_compte
INNER JOIN yvs_compta_pieces_comptable pc ON pc.id=c.piece 
WHERE pc.date_piece BETWEEN '01-08-2021' AND '31-08-2021'
AND n.societe=2290 AND (p.num_compte LIKE '701%' OR p.num_compte LIKE '702%') AND COALESCE(c.debit, 0) > 0

-- Détermine les factures qui sont comptabilisé dans les comptes autres que 70
select p.num_compte, c.* FROM yvs_compta_content_journal c INNER JOIN yvs_base_plan_comptable p ON c.compte_general=p.id INNER JOIN yvs_base_nature_compte n ON n.id=p.nature_compte
INNER JOIN yvs_compta_pieces_comptable pc ON pc.id=c.piece 
WHERE pc.date_piece BETWEEN '01-08-2021' AND '31-08-2021' AND libelle NOT LIKE 'RIST%' AND libelle NOT LIKE 'ARRO%'
AND n.societe=2290 AND p.num_compte NOT LIKE '701%' AND p.num_compte NOT LIKE '702%' 
AND p.num_compte NOT LIKE '411%' AND c.table_externe IN ('DOC_VENTE', 'JOURNAL_VENTE')

-- Détermine les contenu qui sont comptabilisé dans les comptes 70 qui ne sont pas des factures
select p.num_compte, c.* FROM yvs_compta_content_journal c INNER JOIN yvs_base_plan_comptable p ON c.compte_general=p.id INNER JOIN yvs_base_nature_compte n ON n.id=p.nature_compte
INNER JOIN yvs_compta_pieces_comptable pc ON pc.id=c.piece 
WHERE pc.date_piece BETWEEN '01-08-2021' AND '31-08-2021'
AND n.societe=2290 AND (p.num_compte LIKE '701%' OR p.num_compte LIKE '702%') AND c.table_externe NOT IN ('DOC_VENTE', 'JOURNAL_VENTE')

-- Factures Non présente dans la table content_journal_facture_vente
SELECT c.id, d.num_doc from yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc
left JOIN yvs_compta_content_journal_facture_vente c ON c.facture=d.id
WHERE e.date_entete BETWEEN '01-08-2021' AND '31-08-2021' AND d.type_doc='FV' AND d.statut='V' 
and (d.comptabilise IS true or d.comptabilise IS NULL) and c.id is null 
order by e.date_entete 

-- Ca par compte a partir de la gestion commercial
SELECT co.num_compte, SUM(c.prix_total - c.ristourne) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d On c.doc_vente = d.id
INNER JOIN yvs_base_article_categorie_comptable ac On (ac.article = c.article AND ac.categorie = d.categorie_comptable)
INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_base_plan_comptable co ON ac.compte = co.id
WHERE e.agence = 2315 AND d.type_doc = 'FV' AND d.statut = 'V' AND e.date_entete BETWEEN '01-08-2021' AND '31-08-2021'
GROUP BY co.id ORDER BY co.num_compte

-- Ca par compte a partir de la gestion commercial par facture
SELECT * FROM compare_ca_com_ca_compta(2290, 2315, '01-08-2021', '31-08-2021');

-- Verifie si une facture est comptabilisée 2 fois
select p.num_compte, c.ref_externe, c.num_piece FROM yvs_compta_content_journal c INNER JOIN yvs_base_plan_comptable p ON c.compte_general=p.id 
INNER JOIN yvs_base_nature_compte n ON n.id=p.nature_compte INNER JOIN yvs_compta_pieces_comptable pc ON pc.id=c.piece 
WHERE pc.date_piece BETWEEN '01-08-2021' AND '31-08-2021'
AND n.societe=2290 AND p.num_compte LIKE '411%' AND c.table_externe IN ('DOC_VENTE', 'JOURNAL_VENTE')
GROUP BY p.num_compte, c.ref_externe, c.num_piece HAVING COUNt(c.id) > 1 

-- Verifie si facture non valide et comptabilisée
SELECT DISTINCT p.id, p.num_piece, d.id, d.num_doc, e.date_entete FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_journaux j On p.journal = j.id
INNER JOIN yvs_compta_content_journal c ON c.piece = p.id INNER JOIN yvs_com_doc_ventes d ON (c.ref_externe = d.id AND c.table_externe = 'DOC_VENTE')
INNER JOIN yvs_com_entete_doc_vente e On d.entete_doc = e.id
WHERE e.agence = 2315 AND d.type_doc = 'FV' AND d.statut != 'V' AND e.date_entete BETWEEN '01-08-2021' AND '31-08-2021'
ORDER BY e.date_entete

-- Verifie si existe des documents de vente qui ne sont pas des factures et comptabilisée
SELECT DISTINCT p.id, p.num_piece, d.id, d.num_doc, e.date_entete FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_journaux j On p.journal = j.id
INNER JOIN yvs_compta_content_journal c ON c.piece = p.id INNER JOIN yvs_com_doc_ventes d ON (c.ref_externe = d.id AND c.table_externe = 'DOC_VENTE')
INNER JOIN yvs_com_entete_doc_vente e On d.entete_doc = e.id
WHERE e.agence = 2315 AND d.type_doc NOT IN ('FV', 'FAV') AND e.date_entete BETWEEN '01-08-2021' AND '31-08-2021'
ORDER BY e.date_entete

-- Verifie si les factures sont comptabilisé dans le bon journal
SELECT DISTINCT p.id, p.num_piece, e.date_entete, j.agence, e.agence FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_journaux j On p.journal = j.id
INNER JOIN yvs_compta_content_journal c ON c.piece = p.id INNER JOIN yvs_com_doc_ventes d ON (c.ref_externe = d.id AND c.table_externe = 'DOC_VENTE')
INNER JOIN yvs_com_entete_doc_vente e On d.entete_doc = e.id
WHERE e.agence = 2315 AND d.type_doc = 'FV' AND d.statut = 'V' AND e.date_entete BETWEEN '01-08-2021' AND '31-08-2021' AND j.agence != e.agence
ORDER BY e.date_entete

-- Count le nombre de facture et le nombre de piece comptable vente
SELECT COUNT(DISTINCT d.id), COUNT(DISTINCT p.id) FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_journaux j On p.journal = j.id
INNER JOIN yvs_compta_content_journal c ON c.piece = p.id INNER JOIN yvs_com_doc_ventes d ON (c.ref_externe = d.id AND c.table_externe = 'DOC_VENTE') 
INNER JOIN yvs_com_entete_doc_vente e On d.entete_doc = e.id
WHERE e.agence = 2315 AND d.type_doc = 'FV' AND d.statut = 'V' AND e.date_entete BETWEEN '01-08-2021' AND '31-08-2021'


-- Recherche des pieces comptable rattaché a des factures inexistantes
SELECT DISTINCT p.id, p.num_piece, p.date_piece, c.ref_externe, c.table_externe, d.id FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_journaux j On p.journal = j.id
INNER JOIN yvs_compta_content_journal c ON c.piece = p.id LEFT JOIN yvs_com_doc_ventes d ON (c.ref_externe = d.id AND c.table_externe = 'DOC_VENTE') 
WHERE j.agence = 2315 AND (c.ref_externe IS NOT NULL AND c.ref_externe > 0) AND d.id IS NULL 
AND p.date_piece BETWEEN '01-08-2021' AND '31-08-2021' AND c.table_externe = 'DOC_VENTE'

-- Recherche des factures de vente comptabilisé sur un compte
select p.num_compte, c.ref_externe, c.num_piece FROM yvs_compta_content_journal c INNER JOIN yvs_base_plan_comptable p ON c.compte_general=p.id 
INNER JOIN yvs_base_nature_compte n ON n.id=p.nature_compte INNER JOIN yvs_compta_pieces_comptable pc ON pc.id=c.piece 
WHERE pc.date_piece BETWEEN '07-08-2021' AND '07-08-2021'
AND n.societe=2290 AND p.num_compte LIKE '70211100' AND c.table_externe IN ('DOC_VENTE', 'JOURNAL_VENTE')

-- verifie si une commande livrée possède encore les reglements
SELECT y.id, e.date_entete FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id 
LEFT JOIN yvs_com_doc_ventes d ON (d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie = y.id) LEFT JOIN yvs_compta_caisse_piece_vente p ON (p.statut_piece = 'P' AND p.vente = y.id) 
INNER JOIN yvs_users u ON c.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
WHERE a.id = 2315 AND e.date_entete BETWEEN '01-06-2021' AND '31-06-2021' AND y.type_doc = 'BCV' AND y.statut = 'V' 
GROUP BY y.id, e.id HAVING COUNT(d.id) > 0 AND COUNT(p.id) > 0 ORDER BY e.date_entete DESC

-- verifie si une facture possède le meme contenu depuis l'application de caisse
SELECT y.id, y.num_doc FROM yvs_com_doc_ventes y INNER JOIN yvs_com_contenu_doc_vente c ON c.doc_vente = y.id
GROUP BY y.id, c.conditionnement HAVING COUNT(c.id) > 1 

-- Balance vente
SELECT code, credit_periode FROM compta_et_balance(2315, 2290, '70110100, 70111100, 70110100, 70110200, 70111200, 70110200, 70110310, 70110300, 70211100, 70210100, 70210300, 70211300, 70210300, 70210500, 70211500, 70210500, 70211700, 70210700, 70211900, 70210900, 70211601, 70211600', '01-08-2021', '07-08-2021', 0, 'C', '') ORDER BY code
-- SELECT m.* FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_conditionnement c ON m.conditionnement = c.id WHERE m.article != c.article
-- UPDATE yvs_com_entete_doc_vente SET agence = (SELECT u.agence FROM yvs_users u INNER JOIN yvs_com_creneau_horaire_users h ON u.id = h.users WHERE h.id = creneau) WHERE agence IS NULL

-- SELECT fusion_data_for_table('yvs_societes', 2290, 1)
-- SELECT fusion_data_for_table('yvs_agences', 2315, 1)

-- SELECT y.caisse, y.mouvement, SUM(y.montant) FROM yvs_compta_mouvement_caisse y INNER JOIN yvs_base_mode_reglement m ON y.model = m.id INNER JOIN yvs_agences a ON y.agence = a.id 
-- WHERE y.date_paye <= '03-11-2020' AND a.societe = 2300 AND y.statut_piece = 'P' GROUP BY y.caisse, y.mouvement ORDER BY y.caisse, y.mouvement DESC

pg_dump -h localhost -U postgres --format custom --blobs --verbose lymytz_demo_0> /home/yves/files/lymytz_demo_30_11_2020.backup

-- SET session_replication_role = REPLICA;
-- UPDATE yvs_com_ecart_entete_vente SET statut = 'E' WHERE id = 28;
-- SET session_replication_role = DEFAULT;

-- SELECT d.num_doc FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_com_contenu_doc_vente v ON v.doc_vente = d.id 
-- INNER JOIN yvs_com_commercial_vente co ON co.facture = d.id INNER JOIN yvs_com_comerciale c ON co.commercial = c.id
-- WHERE e.date_entete BETWEEN '01-06-2020' AND '30-06-2020' AND d.type_doc = 'FV' AND d.statut = 'V' AND c.commission IS NOT NULL
-- AND v.article NOT IN (SELECT y.id_externe FROM yvs_com_cible_facteur_taux y WHERE y.table_externe = 'BASE_ARTICLE')

-- cd C:\Program Files (x86)\PostgreSQL\9.2\bin
-- psql -U postgres
-- SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'lymytz_demo_0' AND pid <> pg_backend_pid();
-- DROP DATABASE lymytz_demo_0;
-- ALTER DATABASE lymytz_demo_0 RENAME TO lymytz_demo_00;
-- CREATE DATABASE lymytz_demo_0;

-- pg_restore -U postgres -d lymytz_demo_glp -v --format custom D:\_20211116_235959.backup
-- pg_dump -U posgtres -p 5432 --format custom --blobs --verbose lymytz_demo_glp> /home/ubuntu/files/backup_03_01_2022.backup

-- UPDATE yvs_workflow_alertes y SET silence = FALSE WHERE y.id = (SELECT a.id FROM yvs_workflow_alertes a WHERE a.agence = 8 AND a.model_doc = 7 AND a.silence IS TRUE LIMIT 1)

-- UPDATE yvs_workflow_alertes y SET silence = FALSE WHERE y.id = (SELECT a.id FROM yvs_workflow_alertes a WHERE a.agence = 8 AND a.model_doc = 36 AND a.silence IS TRUE LIMIT 1)
-- SELECT COUNT(y.id) FROM yvs_base_articles y LEFT JOIN yvs_base_famille_article f ON y.famille = f.id WHERE y.famille IS NULL OR f.societe IS NULL

-- ALTER TABLE yvs_compta_caisse_doc_divers DISABLE TRIGGER compta_action_on_divers;
-- INSERT INTO yvs_compta_content_journal_doc_divers(divers, piece, author)
-- SELECT y.id, c.piece, c.author FROM yvs_compta_caisse_doc_divers y INNER JOIN yvs_compta_content_journal c ON (c.ref_externe = y.id AND c.table_externe = 'DOC_DIVERS') 
-- WHERE y.comptabilise IS FALSE;
-- ALTER TABLE yvs_compta_caisse_doc_divers ENABLE TRIGGER compta_action_on_divers;

-- Liste des documents d'entree qui ont plusieurs lignes
SELECT m.num_doc, m.id_externe, COUNT(m.id) FROM yvs_base_mouvement_stock m WHERE m.mouvement = 'E' GROUP BY m.num_doc, m.id_externe HAVING COUNT(m.id) > 1

SELECT DISTINCT y.num_piece FROM yvs_compta_content_journal y INNER JOIN yvs_compta_pieces_comptable p ON y.piece = p.id
INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id INNER JOIN yvs_compta_journaux j ON p.journal = j.id
WHERE p.date_piece BETWEEN '01-01-2020' AND '31-12-2020' AND y.table_externe = 'DOC_VIREMENT'
AND (y.lettrage IS NULL OR CHAR_LENGTH(TRIM(y.lettrage)) < 1) AND c.num_compte = '58810000' AND j.agence = 2315
ORDER BY y.num_piece LIMIT 10
 y.id, c.conditionnement HAVING COUNT(c.id) > 1 

-- Code barre rattaché a plusieurs articles
SELECT b.code_barre FROM yvs_base_article_code_barre b GROUP BY b.code_barre HAVING COUNT(b.conditionnement) > 1

-- Insert code barre not listen
INSERT INTO yvs_synchro_listen_table(name_table, id_source, date_save, to_listen, action_name, author, serveur)
SELECT 'yvs_base_article_code_barre',  b.id, current_timestamp, TRUE, 'INSERT', 117, 1
 FROM yvs_base_article_code_barre b LEFT JOIN yvs_synchro_listen_table l ON (l.name_table = 'yvs_base_article_code_barre' AND l.id_source = b.id) WHERE l.id IS NULL