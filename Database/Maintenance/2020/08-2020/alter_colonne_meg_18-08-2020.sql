ALTER TABLE yvs_com_parametre ADD COLUMN document_generer_from_ecart character varying DEFAULT 'RE';
COMMENT ON COLUMN yvs_com_parametre.document_generer_from_ecart IS 'RE pour retenue, CR pour crédit';

ALTER TABLE yvs_com_doc_stocks ADD COLUMN taux_ecart_inventaire double precision default 1;