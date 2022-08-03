
  
  -- Table: yvs_workflow_model_doc

-- DROP TABLE yvs_workflow_model_doc;

CREATE TABLE yvs_workflow_model_doc
(
  id serial NOT NULL,
  titre_doc character varying,
  name_table character varying,
  author bigint,
  CONSTRAINT yvs_workflow_model_doc_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_model_doc_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_model_doc
  OWNER TO postgres;

 
 -- Table: yvs_workflow_etape_validation

-- DROP TABLE yvs_workflow_etape_validation;

CREATE TABLE yvs_workflow_etape_validation
(
  id bigserial NOT NULL,
  label_statut character varying,
  document_model integer,
  author bigint,
  etape_suivante bigint,
  first_etape boolean,
  titre_etape character varying,
  actif boolean,
  CONSTRAINT yvs_workflow_etape_validation_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_etape_validation_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_etape_validation_document_model_fkey FOREIGN KEY (document_model)
      REFERENCES yvs_workflow_model_doc (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_etape_validation_etape_suivante_fkey FOREIGN KEY (etape_suivante)
      REFERENCES yvs_workflow_etape_validation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_etape_validation
  OWNER TO postgres;


  

ALTER TABLE yvs_grh_missions DROP COLUMN valider;

ALTER TABLE yvs_grh_missions ADD COLUMN valider boolean;


-- Table: yvs_workflow_autorisation_valid_doc

-- DROP TABLE yvs_workflow_autorisation_valid_doc;

CREATE TABLE yvs_workflow_autorisation_valid_doc
(
  id bigserial NOT NULL,
  etape_valide integer,
  can_valide boolean,
  niveau_acces bigint,
  author bigint,
  CONSTRAINT yvs_workflow_autorisation_valid_doc_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_autorisation_valid_doc_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_autorisation_valid_doc_etape_valide_fkey FOREIGN KEY (etape_valide)
      REFERENCES yvs_workflow_etape_validation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_autorisation_valid_doc_niveau_acces_fkey FOREIGN KEY (niveau_acces)
      REFERENCES yvs_niveau_acces (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_autorisation_valid_doc
  OWNER TO postgres;

 
  
-- Table: yvs_workflow_valid_mission

-- DROP TABLE yvs_workflow_valid_mission;

CREATE TABLE yvs_workflow_valid_mission
(
  mission bigint,
  etape bigint,
  author bigint,
  etape_valid boolean,
  id bigserial NOT NULL,
  CONSTRAINT yvs_workflow_valid_mission_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_valid_mission_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_valid_mission_etape_fkey FOREIGN KEY (etape)
      REFERENCES yvs_workflow_etape_validation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_valid_mission_mission_fkey FOREIGN KEY (mission)
      REFERENCES yvs_grh_missions (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_valid_mission
  OWNER TO postgres;




--Modifier la table yvs_grh_grille_mission
	-- +renommer la colone agence en societe
--Modifier la nature de la clé primaire de la table yvs_grh_detail_grille_frai_mission
-- Ajouter la colonne frais de mission à la table yvs_grh_missions
ALTER TABLE yvs_grh_missions ADD COLUMN frais_mission bigint;
ALTER TABLE yvs_grh_missions
  ADD CONSTRAINT yvs_grh_missions_frais_mission_fkey FOREIGN KEY (frais_mission)
      REFERENCES yvs_grh_grille_mission (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	
ALTER TABLE yvs_grh_detail_grille_frai_mission DROP CONSTRAINT yvs_grh_detail_grille_frai_mission_grille_fkey;

ALTER TABLE yvs_grh_detail_grille_frai_mission
  ADD CONSTRAINT yvs_grh_detail_grille_frai_mission_grille_fkey FOREIGN KEY (grille)
      REFERENCES yvs_grh_grille_mission (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
	  
 DROP TABLE yvs_grh_cout_mission;
 DROP TABLE yvs_grh_mission_emps;
 ALTER TABLE yvs_grh_missions ADD COLUMN colturer boolean;
 ALTER TABLE yvs_grh_formation ADD COLUMN statut_formation character(1);
 
 
 
 ALTER TABLE yvs_grh_missions ADD COLUMN date_retour date;
 ALTER TABLE yvs_grh_missions ADD COLUMN heure_depart time without time zone;
 ALTER TABLE yvs_grh_missions ADD COLUMN heure_arrive time without time zone;
 ALTER TABLE yvs_grh_missions ADD COLUMN reference_mission character varying;
 ALTER TABLE yvs_grh_missions ADD COLUMN transport character varying;
 ALTER TABLE yvs_grh_missions ADD COLUMN role_mission character varying;
 ALTER TABLE yvs_grh_missions ADD COLUMN date_save timestamp without time zone default current_timestamp;
 
 ALTER TABLE yvs_grh_departement ADD COLUMN abreviation character varying;
 
 
 
 
 
