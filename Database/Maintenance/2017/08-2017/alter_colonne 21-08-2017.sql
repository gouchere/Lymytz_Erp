ALTER TABLE yvs_param_formulaire RENAME intitule_form TO intitule;

CREATE TABLE yvs_param_champ_formulaire
(
  id bigserial NOT NULL,
  code character varying,
  champ character varying,
  formulaire bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  author bigint,
  CONSTRAINT yvs_param_champ_formulaire_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_param_champ_formulaire_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_param_champ_formulaire_formulaire_fkey FOREIGN KEY (formulaire)
      REFERENCES yvs_param_formulaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_param_champ_formulaire
  OWNER TO postgres;

CREATE TABLE yvs_param_model_formulaire
(
  id bigserial NOT NULL,
  intitule character varying,
  formulaire bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_param_model_formulaire_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_param_model_formulaire_formulaire_fkey FOREIGN KEY (formulaire)
      REFERENCES yvs_param_formulaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_param_model_formulaire_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_param_model_formulaire
  OWNER TO postgres;
  
DROP TABLE yvs_param_modele_form;

CREATE TABLE yvs_param_propriete_champ
(
  id bigserial NOT NULL,
  visible boolean DEFAULT true,
  obligatoire boolean,
  editable boolean DEFAULT true,
  champ bigint,
  modele bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  author bigint,
  CONSTRAINT yvs_param_propriete_champ_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_param_propriete_champ_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_param_propriete_champ_champ_fkey FOREIGN KEY (champ)
      REFERENCES yvs_param_champ_formulaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_param_propriete_champ_modele_fkey FOREIGN KEY (modele)
      REFERENCES yvs_param_model_formulaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_param_propriete_champ
  OWNER TO postgres;
  
ALTER TABLE yvs_param_user_formulaire DROP CONSTRAINT yvs_param_user_formulaire_formulaire_fkey;

ALTER TABLE yvs_param_user_formulaire
  ADD CONSTRAINT yvs_param_user_formulaire_formulaire_fkey FOREIGN KEY (formulaire)
      REFERENCES yvs_param_model_formulaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;