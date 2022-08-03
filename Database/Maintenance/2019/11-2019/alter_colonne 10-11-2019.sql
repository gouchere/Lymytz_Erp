ALTER TABLE yvs_com_doc_ventes ADD COLUMN nature character varying default 'VENTE';
ALTER TABLE yvs_grh_employes ALTER COLUMN agence SET DEFAULT NULL;

-- Table: yvs_com_doc_ventes_informations
-- DROP TABLE yvs_com_doc_ventes_informations;
CREATE TABLE yvs_com_doc_ventes_informations
(
  id bigserial NOT NULL,
  facture bigint,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  heure_debut time without time zone DEFAULT ('now'::text)::time with time zone,
  date_fin date DEFAULT ('now'::text)::date,
  heure_fin time without time zone DEFAULT ('now'::text)::time with time zone,
  adresse_livraison bigint,
  num_cni character varying,
  nombre_personne integer,
  nom_personne_supplementaire character varying,
  modele_vehicule character varying,
  num_immatriculation character varying,
  CONSTRAINT yvs_com_doc_ventes_informations_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_doc_ventes_informations_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_doc_ventes_informations_facture_fkey FOREIGN KEY (facture)
      REFERENCES yvs_com_doc_ventes (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_doc_ventes_informations
  OWNER TO postgres;


-- Table: yvs_com_contenu_doc_vente_etat
-- DROP TABLE yvs_com_contenu_doc_vente_etat;
CREATE TABLE yvs_com_contenu_doc_vente_etat
(
  id bigserial NOT NULL,
  libelle character varying,
  valeur character varying,
  contenu bigint,
  author bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  CONSTRAINT yvs_com_contenu_doc_vente_etat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_contenu_doc_vente_etat_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_contenu_doc_vente_etat_contenu_fkey FOREIGN KEY (contenu)
      REFERENCES yvs_com_contenu_doc_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_contenu_doc_vente_etat
  OWNER TO postgres;