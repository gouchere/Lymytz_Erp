UPDATE yvs_compta_caisse_doc_divers SET reference_externe=num_piece WHERE (reference_externe IS NULL OR COALESCE(reference_externe,'')='');