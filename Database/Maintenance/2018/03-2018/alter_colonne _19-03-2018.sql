ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN beneficiaire CHARACTER VARYING;
UPDATE yvs_compta_bon_provisoire SET beneficiaire = (SELECT CONCAT(t.nom, ' ', t.prenom) FROM yvs_base_tiers t WHERE t.id = tiers);
ALTER TABLE yvs_compta_bon_provisoire DROP COLUMN tiers;

INSERT INTO yvs_base_element_reference(designation, module) VALUES ('Piece Caisse', 'COFI');
UPDATE yvs_workflow_valid_doc_caisse SET date_update = date_update;