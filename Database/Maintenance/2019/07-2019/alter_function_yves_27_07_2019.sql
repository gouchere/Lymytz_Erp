
ALTER TABLE yvs_compta_acompte_fournisseur ADD COLUMN parent bigint;
ALTER TABLE yvs_compta_acompte_fournisseur
  ADD CONSTRAINT yvs_compta_acompte_fournisseur_parent_fkey FOREIGN KEY (parent)
      REFERENCES yvs_compta_acompte_fournisseur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_compta_acompte_client ADD COLUMN parent bigint;
ALTER TABLE yvs_compta_acompte_client
  ADD CONSTRAINT yvs_compta_acompte_client_parent_fkey FOREIGN KEY (parent)
      REFERENCES yvs_compta_acompte_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

SELECT insert_droit('compta_extourne_cheque', 'Valider/Annuler l''extourne sur un chèque', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_cheq_reg_vente'), 16, 'A,B,E','R');
	

ALTER TABLE yvs_compta_pieces_comptable ADD COLUMN extourne boolean;
ALTER TABLE yvs_compta_pieces_comptable ALTER COLUMN extourne SET DEFAULT false;

ALTER TABLE yvs_compta_caisse_piece_vente ADD COLUMN verouille boolean;
ALTER TABLE yvs_compta_caisse_piece_vente ALTER COLUMN verouille SET DEFAULT false;
