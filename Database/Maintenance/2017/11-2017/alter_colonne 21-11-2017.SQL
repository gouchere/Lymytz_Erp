ALTER TABLE yvs_compta_caisse_doc_divers ADD COLUMN is_montant_ttc boolean;
ALTER TABLE yvs_compta_caisse_doc_divers ALTER COLUMN is_montant_ttc SET DEFAULT false;
ALTER TABLE yvs_compta_caisse_doc_divers ADD COLUMN statut_regle character(1);
ALTER TABLE yvs_compta_caisse_doc_divers ALTER COLUMN statut_regle SET DEFAULT 'W'::bpchar;