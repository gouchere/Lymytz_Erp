ALTER TABLE yvs_mut_parametre ADD COLUMN nbre_avalise_obligatoir integer;
ALTER TABLE yvs_mut_parametre ALTER COLUMN nbre_avalise_obligatoir SET DEFAULT 0;
ALTER TABLE yvs_mut_parametre ADD COLUMN valid_credit_by_vote boolean;
ALTER TABLE yvs_mut_parametre ALTER COLUMN valid_credit_by_vote SET DEFAULT false;
ALTER TABLE yvs_mut_parametre ADD COLUMN duree_etude_credit integer;
ALTER TABLE yvs_mut_parametre ALTER COLUMN duree_etude_credit SET DEFAULT 0;
ALTER TABLE yvs_mut_parametre ADD COLUMN taux_vote_valid_credit_correct double precision;
ALTER TABLE yvs_mut_parametre ALTER COLUMN taux_vote_valid_credit_correct SET DEFAULT 0;
ALTER TABLE yvs_mut_parametre ADD COLUMN taux_vote_valid_credit_incorrect double precision;
ALTER TABLE yvs_mut_parametre ALTER COLUMN taux_vote_valid_credit_incorrect SET DEFAULT 0;

ALTER TABLE yvs_mut_poste ADD COLUMN can_vote_credit boolean;
ALTER TABLE yvs_mut_poste ALTER COLUMN can_vote_credit SET DEFAULT false;

ALTER TABLE yvs_mut_credit ADD COLUMN statut_credit character varying(1);
ALTER TABLE yvs_mut_credit ALTER COLUMN statut_credit SET DEFAULT 'W'::character varying;
ALTER TABLE yvs_mut_credit ADD COLUMN date_soumission date;
ALTER TABLE yvs_mut_credit ALTER COLUMN date_soumission SET DEFAULT ('now'::text)::date;
ALTER TABLE yvs_mut_credit ADD COLUMN frais_additionnel double precision;
ALTER TABLE yvs_mut_credit ALTER COLUMN frais_additionnel SET DEFAULT 0;

ALTER TABLE yvs_mut_reglement_mensualite ADD COLUMN statut_piece character(1);
ALTER TABLE yvs_mut_reglement_mensualite ALTER COLUMN statut_piece SET DEFAULT 'W'::bpchar;
ALTER TABLE yvs_mut_reglement_mensualite ADD COLUMN caisse bigint;
ALTER TABLE yvs_mut_reglement_mensualite
  ADD CONSTRAINT yvs_mut_reglement_mensualite_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_mut_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


CREATE TABLE yvs_mut_vote_validation_credit
(
  id bigserial NOT NULL,
  credit bigint,
  date_validation timestamp without time zone DEFAULT now(),
  mutualiste bigint,
  accepte boolean DEFAULT false,
  author bigint,
  CONSTRAINT yvs_mut_vote_validation_credit_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_mut_vote_validation_credit_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_mut_vote_validation_credit_credit_fkey FOREIGN KEY (credit)
      REFERENCES yvs_mut_credit (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_mut_vote_validation_credit_mutualiste_fkey FOREIGN KEY (mutualiste)
      REFERENCES yvs_mut_mutualiste (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_mut_vote_validation_credit
  OWNER TO postgres;
  
  
CREATE TABLE yvs_mut_reglement_credit
(
  id bigserial NOT NULL,
  date_reglement date,
  montant double precision DEFAULT 0,
  credit bigint,
  caisse bigint,
  regle_par character varying, -- Précise qui à réglé la mensualité....
  author bigint,
  statut_piece character(1) DEFAULT 'W'::bpchar,
  CONSTRAINT yvs_mut_reglement_credit_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_mut_reglement_credit_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_mut_reglement_credit_credit_fkey FOREIGN KEY (credit)
      REFERENCES yvs_mut_credit (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_mut_reglement_credit_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_mut_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_mut_reglement_credit
  OWNER TO postgres;
  
select alter_action_colonne_key('yvs_mut_credit', 'yvs_mut_echellonage', true, true);
select alter_action_colonne_key('yvs_mut_credit', 'yvs_mut_vote_validation_credit', true, true);
select alter_action_colonne_key('yvs_mut_echellonage', 'yvs_mut_mensualite', true, true);
select alter_action_colonne_key('yvs_mut_mensualite', 'yvs_mut_reglement_mensualite', true, true);