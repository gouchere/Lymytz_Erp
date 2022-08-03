ALTER TABLE yvs_grh_profil_emps DROP CONSTRAINT yvs_grh_profil_emps_profil_fkey;
ALTER TABLE yvs_grh_profil_emps
  ADD CONSTRAINT yvs_grh_profil_emps_profil_fkey FOREIGN KEY (profil)
      REFERENCES yvs_grh_profil (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
	  

CREATE TABLE yvs_grh_statut_employe
(
  id bigserial NOT NULL,
  statut character(1),
  libelle_statut character varying,
  author bigint,
  CONSTRAINT yvs_grh_statut_employe_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_statut_employe_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_statut_employe
  OWNER TO postgres;

	  
CREATE TABLE yvs_grh_historique_statut_employe
(
  id bigserial NOT NULL,
  statut bigint,
  date_change date,
  author bigint,
  employe bigint,
  CONSTRAINT yvs_grh_historique_statut_employe_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_grh_historique_statut_employe_statut_fkey FOREIGN KEY (statut)
      REFERENCES yvs_grh_statut_employe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_statut_employe_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_grh_statut_employe_employe_fkey FOREIGN KEY (employe)
      REFERENCES yvs_grh_employes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_grh_historique_statut_employe
  OWNER TO postgres;

  
ALTER TABLE yvs_grh_employes ADD COLUMN statut_emps bigint;
ALTER TABLE yvs_grh_employes
  ADD CONSTRAINT yvs_grh_employes_statut_emps_fkey FOREIGN KEY (statut_emps)
      REFERENCES yvs_grh_statut_employe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;