ALTER TABLE yvs_com_doc_stocks ADD COLUMN date_reception date;
ALTER TABLE yvs_com_doc_stocks DISABLE TRIGGER update_;
UPDATE yvs_com_doc_stocks SET date_reception = date_doc;
ALTER TABLE yvs_com_doc_stocks ENABLE TRIGGER update_;

ALTER TABLE yvs_base_parametre ADD COLUMN duree_inactiv_article INTEGER DEFAULT 0;