
ALTER TABLE yvs_com_doc_stocks ADD COLUMN ignore_pr boolean;
ALTER TABLE yvs_com_doc_stocks ALTER COLUMN ignore_pr SET DEFAULT false;
COMMENT ON COLUMN yvs_com_doc_stocks.ignore_pr IS 'Propriété qui se propage au contenu du document';

-- Column: suspendu

-- ALTER TABLE yvs_prod_ordre_fabrication DROP COLUMN suspendu;

ALTER TABLE yvs_prod_ordre_fabrication ADD COLUMN suspendu boolean;
ALTER TABLE yvs_prod_ordre_fabrication ALTER COLUMN suspendu SET DEFAULT false;
COMMENT ON COLUMN yvs_prod_ordre_fabrication.suspendu IS 'Equivalent au statut suspendu de l''oF';
