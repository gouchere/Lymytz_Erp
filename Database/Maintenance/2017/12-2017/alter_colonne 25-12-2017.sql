ALTER TABLE yvs_mut_parametre ADD COLUMN quotite_cessible double precision;
ALTER TABLE yvs_mut_parametre ALTER COLUMN quotite_cessible SET DEFAULT 0;

ALTER TABLE yvs_mut_credit DROP COLUMN montant_verse;
ALTER TABLE yvs_mut_credit ADD COLUMN statut_paiement character varying(1);
ALTER TABLE yvs_mut_credit ALTER COLUMN statut_paiement SET DEFAULT 'W'::character varying;

update yvs_mut_credit set statut_paiement = 'W' where statut_paiement is null;
update yvs_mut_credit set statut_paiement = 'P', etat = 'V' where etat = 'P';

ALTER TABLE yvs_mut_avance_salaire ADD COLUMN statut_paiement character varying(1);
ALTER TABLE yvs_mut_avance_salaire ALTER COLUMN statut_paiement SET DEFAULT 'W'::character varying;

ALTER TABLE yvs_mut_echellonage DROP COLUMN montant_verse;

ALTER TABLE yvs_mut_mensualite DROP COLUMN montant_verse;
ALTER TABLE yvs_mut_mensualite ADD COLUMN commentaire character varying;
DROP TRIGGER update_ ON yvs_mut_mensualite;

ALTER TABLE yvs_mut_type_credit ADD COLUMN nature_penalite_retard character varying(1) DEFAULT 'T'::character varying;
ALTER TABLE yvs_mut_type_credit ADD COLUMN nature_penalite_anticipation character varying(1) DEFAULT 'T'::character varying;
ALTER TABLE yvs_mut_type_credit ADD COLUMN nature_penalite_suspension character varying(1) DEFAULT 'T'::character varying;


CREATE TABLE yvs_compta_phase_acompte_vente
(
  id bigserial NOT NULL,
  piece_vente bigint,
  phase_reg bigint,
  phase_ok boolean,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_phase_acompte_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_phase_acompte_vente_piece_vente_fkey FOREIGN KEY (piece_vente)
      REFERENCES yvs_compta_acompte_client (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_phase_piece_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_phase_piece_phase_reg_fkey FOREIGN KEY (phase_reg)
      REFERENCES yvs_compta_phase_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_phase_acompte_vente
  OWNER TO postgres;
  
  
CREATE TABLE yvs_compta_phase_acompte_achat
(
  id bigserial NOT NULL,
  piece_achat bigint,
  phase_reg bigint,
  phase_ok boolean,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_phase_acompte_achat_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_phase_acompte_achat_piece_achat_fkey FOREIGN KEY (piece_achat)
      REFERENCES yvs_compta_acompte_fournisseur (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_phase_piece_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_phase_piece_phase_reg_fkey FOREIGN KEY (phase_reg)
      REFERENCES yvs_compta_phase_reglement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_phase_acompte_achat
  OWNER TO postgres;