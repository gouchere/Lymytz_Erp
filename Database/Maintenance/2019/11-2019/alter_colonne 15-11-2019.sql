ALTER TABLE yvs_base_articles_template ADD COLUMN libelle character varying;
UPDATE yvs_base_articles_template SET libelle = description;

INSERT INTO yvs_base_element_reference(designation, module, default_prefix) VALUES('Bon Retour Location', 'COM', 'BRL');