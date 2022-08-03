CREATE TABLE yvs_mut_activite
(
  id bigserial NOT NULL,
  montant_requis double precision default 0,
  evenement bigint,
  type_activite bigint,
  author bigint,
  CONSTRAINT yvs_mut_activite_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_mut_activiteauthor_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_mut_activite_evement_fkey FOREIGN KEY (evenement)
      REFERENCES yvs_mut_evenement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_mut_activite_type_activite_fkey FOREIGN KEY (type_activite)
      REFERENCES yvs_mut_activite_type (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_mut_activite
  OWNER TO postgres;
  
CREATE TABLE yvs_mut_financement_activite
(
  id bigserial NOT NULL,
  montant_recu double precision default 0,
  date_financement date default current_date,
  activite bigint,
  caisse bigint,
  author bigint,
  CONSTRAINT yvs_mut_financement_activite_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_mut_financement_activiteauthor_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_mut_financement_activite_evement_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_mut_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_mut_financement_activite_type_activite_fkey FOREIGN KEY (activite)
      REFERENCES yvs_mut_activite (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_mut_financement_activite
  OWNER TO postgres;
  
  
ALTER TABLE yvs_mut_type_credit ADD COLUMN code character varying;

ALTER TABLE yvs_stat_grh_element_dipe ADD COLUMN by_formulaire boolean default false;

ALTER TABLE yvs_stat_export_colonne ADD COLUMN colonne_date boolean default false;
ALTER TABLE yvs_stat_export_colonne ADD COLUMN format_date character varying default 'dd-MM-yy';

ALTER TABLE yvs_stat_export_etat ADD COLUMN separateur character varying default ';';
ALTER TABLE yvs_stat_export_etat ADD COLUMN reference character varying;

ALTER TABLE yvs_mut_evenement ADD COLUMN montant_obligatoire double precision default 0;

ALTER TABLE yvs_mut_participant_evenement ADD COLUMN role_membre character varying;
ALTER TABLE yvs_mut_participant_evenement ADD COLUMN organisateur boolean default false;
ALTER TABLE yvs_mut_participant_evenement DROP COLUMN evenement;
ALTER TABLE yvs_mut_participant_evenement ADD COLUMN activite bigint;
ALTER TABLE yvs_mut_participant_evenement
  ADD CONSTRAINT yvs_mut_participant_evenement_activite_fkey FOREIGN KEY (activite)
      REFERENCES yvs_mut_activite (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_mut_contribution_evenement DROP COLUMN mutualiste;
ALTER TABLE yvs_mut_contribution_evenement DROP COLUMN contribution;
ALTER TABLE yvs_mut_contribution_evenement ADD COLUMN caisse bigint;
ALTER TABLE yvs_mut_contribution_evenement
  ADD CONSTRAINT yvs_mut_contribution_evenement_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_mut_caisse (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE yvs_mut_contribution_evenement ADD COLUMN compte bigint;
ALTER TABLE yvs_mut_contribution_evenement
  ADD CONSTRAINT yvs_mut_contribution_evenement_compte_fkey FOREIGN KEY (compte)
      REFERENCES yvs_mut_compte (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;