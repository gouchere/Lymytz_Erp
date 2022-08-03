ALTER TABLE yvs_grh_element_salaire ADD COLUMN mode_repartition character(1);
ALTER TABLE yvs_grh_element_salaire ALTER COLUMN mode_repartition SET DEFAULT 'D'::bpchar;

ALTER TABLE yvs_com_doc_achats ADD COLUMN etape_total integer;
ALTER TABLE yvs_com_doc_achats ADD COLUMN etape_valide integer;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN etape_total integer;
ALTER TABLE yvs_com_doc_ventes ADD COLUMN etape_valide integer;
ALTER TABLE yvs_com_doc_stocks ADD COLUMN etape_total integer;
ALTER TABLE yvs_com_doc_stocks ADD COLUMN etape_valide integer;
ALTER TABLE yvs_compta_caisse_doc_divers ADD COLUMN etape_total integer;
ALTER TABLE yvs_compta_caisse_doc_divers ADD COLUMN etape_valide integer;

ALTER TABLE yvs_com_fiche_approvisionnement ADD COLUMN statut_terminer character varying DEFAULT 'W';

update yvs_com_fiche_approvisionnement set etat = 'V' where etat = 'U';
select equilibre_approvision(id) from yvs_com_fiche_approvisionnement;

update yvs_workflow_valid_facture_achat set date_update = date_update;
update yvs_workflow_valid_facture_vente set date_update = date_update;
update yvs_workflow_valid_doc_stock set date_update = date_update;
update yvs_workflow_valid_divers set date_update = date_update;
update yvs_workflow_valid_doc_caisse set date_update = date_update;