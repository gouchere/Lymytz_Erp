ALTER TABLE yvs_users ADD COLUMN connect_online_planning boolean;
ALTER TABLE yvs_users ALTER COLUMN connect_online_planning SET DEFAULT false;

-- Table: yvs_users_grade
-- DROP TABLE yvs_users_grade;
CREATE TABLE yvs_users_grade
(
  id bigserial NOT NULL,
  reference character varying,
  libelle character varying,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  CONSTRAINT yvs_users_grade_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_users_grade_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_users_grade
  OWNER TO postgres;
  
INSERT INTO yvs_users_grade(reference, libelle, author) VALUES ('ADMIN', 'ADMINISTRATEUR', 16);
  
  
ALTER TABLE yvs_niveau_acces DROP COLUMN grade;
ALTER TABLE yvs_niveau_acces ADD COLUMN grade bigint;
ALTER TABLE yvs_niveau_acces
  ADD CONSTRAINT yvs_niveau_acces_grade_fkey FOREIGN KEY (grade)
      REFERENCES yvs_users_grade (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  

ALTER TABLE yvs_com_doc_ration ADD COLUMN date_fiche DATE DEFAULT CURRENT_DATE;