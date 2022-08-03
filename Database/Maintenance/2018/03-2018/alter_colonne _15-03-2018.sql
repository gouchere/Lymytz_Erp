INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module) VALUES ('d_div_update_doc_valid', 'Modifier un document déja validé', 'Modifier un document déja validé', 75);
SELECT alter_action_colonne_key('yvs_compta_content_journal', 'yvs_compta_content_analytique', true, true);
CREATE TABLE yvs_compta_centre_doc_divers
(
  id bigserial NOT NULL,
  doc_divers bigint,
  centre bigint,
  montant double precision,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_centre_doc_divers_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_centre_doc_divers_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_centre_doc_divers_contenu_fkey FOREIGN KEY (doc_divers)
      REFERENCES yvs_compta_caisse_doc_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_compta_centre_doc_divers_centre_fkey FOREIGN KEY (centre)
      REFERENCES yvs_compta_centre_analytique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_centre_doc_divers
  OWNER TO postgres;
  
ALTER TABLE yvs_compta_parametre ADD COLUMN maj_compta_auto_divers boolean;
ALTER TABLE yvs_compta_parametre ALTER COLUMN maj_compta_auto_divers SET DEFAULT false;
ALTER TABLE yvs_compta_parametre ADD COLUMN maj_compta_statut_divers character(1);
ALTER TABLE yvs_compta_parametre ALTER COLUMN maj_compta_statut_divers SET DEFAULT 'P'::bpchar;

ALTER TABLE yvs_compta_content_journal ADD COLUMN statut character(1);
ALTER TABLE yvs_compta_content_journal ALTER COLUMN statut SET DEFAULT 'V'::bpchar;

ALTER TABLE yvs_compta_content_analytique ADD COLUMN statut character(1);
ALTER TABLE yvs_compta_content_analytique ALTER COLUMN statut SET DEFAULT 'V'::bpchar;