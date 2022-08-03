-- Table: yvs_com_doc_ventes

-- DROP TABLE yvs_com_doc_ventes;

CREATE TABLE yvs_com_doc_ventes
(
  id bigint NOT NULL DEFAULT nextval('yvs_doc_ventes_id_seq'::regclass),
  num_piece character varying,
  type_doc character varying,
  statut character varying,
  client bigint,
  categorie_tarifaire bigint,
  categorie_comptable bigint,
  prefixe character varying,
  document_lie bigint,
  supp boolean DEFAULT false,
  actif boolean DEFAULT true,
  livreur bigint,
  num_doc character varying,
  entete_doc bigint,
  heure_doc time without time zone,
  montant_avance double precision DEFAULT 0,
  date_save timestamp without time zone DEFAULT now(),
  mouv_stock boolean DEFAULT false,
  impression integer DEFAULT 0,
  author bigint,
  date_solder date DEFAULT ('now'::text)::date,
  consigner boolean DEFAULT false,
  date_consigner date DEFAULT ('now'::text)::date,
  date_livraison date DEFAULT ('now'::text)::date,
  cloturer boolean,
  date_cloturer date DEFAULT ('now'::text)::date,
  valider_by bigint,
  date_valider date DEFAULT ('now'::text)::date,
  annuler_by bigint,
  date_annuler date DEFAULT ('now'::text)::date,
  depot_livrer bigint,
  tranche_livrer bigint,
  description character varying(255),
  model_reglement integer,
  statut_regle character varying DEFAULT 'W'::character varying,
  statut_livre character varying DEFAULT 'W'::character varying,
  date_livraison_prevu date,
  CONSTRAINT yvs_doc_ventes_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_doc_ventes_annuler_by_fkey FOREIGN KEY (annuler_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_ventes_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_ventes_categorie_comptable_fkey FOREIGN KEY (categorie_comptable)
      REFERENCES yvs_base_categorie_comptable (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_ventes_client_fkey FOREIGN KEY (client)
      REFERENCES yvs_com_client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_ventes_depot_livrer_fkey FOREIGN KEY (depot_livrer)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_ventes_document_lie_fkey FOREIGN KEY (document_lie)
      REFERENCES yvs_com_doc_ventes (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT yvs_com_doc_ventes_entete_doc_fkey FOREIGN KEY (entete_doc)
      REFERENCES yvs_com_entete_doc_vente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_ventes_livreur_fkey FOREIGN KEY (livreur)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_ventes_model_reglement_fkey FOREIGN KEY (model_reglement)
      REFERENCES yvs_base_model_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_ventes_tranche_livrer_fkey FOREIGN KEY (tranche_livrer)
      REFERENCES yvs_grh_tranche_horaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_ventes_valider_by_fkey FOREIGN KEY (valider_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_doc_ventes
  OWNER TO postgres;

-- Trigger: delete_ on yvs_com_doc_ventes

-- DROP TRIGGER delete_ ON yvs_com_doc_ventes;

CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_com_doc_ventes
  FOR EACH ROW
  EXECUTE PROCEDURE delete_doc_ventes();

-- Trigger: update_ on yvs_com_doc_ventes

-- DROP TRIGGER update_ ON yvs_com_doc_ventes;

CREATE TRIGGER update_
  BEFORE UPDATE
  ON yvs_com_doc_ventes
  FOR EACH ROW
  EXECUTE PROCEDURE update_doc_ventes();

