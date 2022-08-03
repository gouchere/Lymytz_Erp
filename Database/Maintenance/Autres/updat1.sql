Renommer la table yvs_qualification_formation en yvs_grh_qualification_formation

ALTER TABLE yvs_parametre_grh ADD COLUMN calcul_planing_dynamique boolean;
ALTER TABLE yvs_parametre_grh ADD COLUMN time_marge time without time zone;

renommer la colone agence de la table yvs_grh_grille_mission en societe
Ajouter le champ id à la table yvs_grh_detail_grille_frai_mission (clé primaire)

-- Table: yvs_workflow_valid_mission

DROP TABLE yvs_workflow_valid_conge;

CREATE TABLE yvs_workflow_valid_conge
(
  conge bigint,
  etape bigint,
  author bigint,
  etape_valid boolean,
  id bigserial NOT NULL,
  CONSTRAINT yvs_workflow_valid_conge_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_valid_conge_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_valid_conge_etape_fkey FOREIGN KEY (etape)
      REFERENCES yvs_workflow_etape_validation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_valid_conge_conge_fkey FOREIGN KEY (conge)
      REFERENCES yvs_grh_conge_emps (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_valid_conge
  OWNER TO postgres;

  
  --Les éléments de salaires
  -- Enlever la contrainte sur les colonnes quantite et base_pourcentage de la table yvs_element_salaire
  --renommer la table yvs_element_salaire en yvs_grh_element_salaire
  --remettre les contrainte qu'on vient de supprimer
  
  --Renommer la table yvs_categorie_element en yvs_grh_catecorie_element
  
  --enlever la colonne societe dans yvs_grh_element_salaire
  --renommer la table yvs_element_structure en yvs_grh_element_structure
  --renommer la table yvs_structure_salaire en yvs_grh_structure_salaire
  
  --Supprimer la clé primaire de la table yvs_grh_element_structure et remplacer par une cle sequence (id)