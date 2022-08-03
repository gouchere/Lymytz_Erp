ALTER TABLE yvs_prod_equipe_production DROP CONSTRAINT yvs_prod_equipe_production_chef_equipe_fkey;
ALTER TABLE yvs_prod_equipe_production
  ADD CONSTRAINT yvs_prod_equipe_production_chef_equipe_fkey FOREIGN KEY (chef_equipe)
      REFERENCES yvs_grh_employes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_prod_employe_equipe DROP CONSTRAINT yvs_prod_employe_equipe_employe_fkey;
ALTER TABLE yvs_prod_employe_equipe
  ADD CONSTRAINT yvs_prod_employe_equipe_employe_fkey FOREIGN KEY (employe)
      REFERENCES yvs_grh_employes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

DROP TABLE yvs_prod_employe;

ALTER TABLE yvs_com_fiche_approvisionnement ADD COLUMN etape_total integer;
ALTER TABLE yvs_com_fiche_approvisionnement ADD COLUMN etape_valide integer;

ALTER TABLE yvs_workflow_valid_divers RENAME TO yvs_workflow_valid_approvissionnement;
ALTER SEQUENCE yvs_workflow_valid_divers_id_seq RENAME TO yvs_workflow_valid_approvissionnement_id_seq;
ALTER TABLE yvs_workflow_valid_approvissionnement DROP CONSTRAINT yvs_workflow_valid_divers_divers_fkey;
ALTER TABLE yvs_workflow_valid_approvissionnement RENAME divers TO "document";

ALTER TABLE yvs_workflow_valid_approvissionnement
  ADD CONSTRAINT yvs_workflow_valid_approvissionnement_document_fkey FOREIGN KEY ("document")
      REFERENCES yvs_com_fiche_approvisionnement (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
      
DROP TRIGGER action_on_workflow_divers ON yvs_workflow_valid_approvissionnement;
DROP FUNCTION action_on_workflow_divers();

INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author) VALUES ('APPROVISIONNEMENT', 'yvs_com_fiche_approvisionnement', 16);
update yvs_prod_nomenclature set quantite_lie_aux_composants = false;

CREATE TABLE yvs_prod_declaration_production
(
  id bigserial NOT NULL,
  ordre bigint,
  conditionnement bigint,
  date_declaration date DEFAULT ('now'::text)::date,
  depot bigint,
  quantite double precision DEFAULT 0,
  statut character(1) DEFAULT 'W'::bpchar,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  CONSTRAINT yvs_prod_declaration_production_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_declaration_production_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_declaration_production_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_declaration_production_depot_fkey FOREIGN KEY (depot)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_declaration_production_ordre_fkey FOREIGN KEY (ordre)
      REFERENCES yvs_prod_ordre_fabrication (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_declaration_production
  OWNER TO postgres;
