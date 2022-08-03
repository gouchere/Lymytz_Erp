-- Table: yvs_compta_caisse_piece_ecart_vente
-- DROP TABLE yvs_compta_caisse_piece_ecart_vente;
CREATE TABLE yvs_compta_caisse_piece_ecart_vente
(
  id bigserial NOT NULL,
  piece bigint,
  caisse bigint,
  model bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_compta_caisse_piece_ecart_vente_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_compta_caisse_piece_ecart_vente_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_compta_caisse_piece_ecart_vente_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_caisse_piece_ecart_vente_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_base_mode_reglement (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_compta_caisse_piece_ecart_vente_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_com_ecart_entete_vente (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_compta_caisse_piece_ecart_vente
  OWNER TO postgres;
  
ALTER TABLE yvs_com_ecart_entete_vente DROP COLUMN caisse;
ALTER TABLE yvs_com_ecart_entete_vente ADD COLUMN numero character varying;

ALTER TABLE yvs_com_reglement_ecart_entete_vente ADD COLUMN numero character varying;

ALTER TABLE yvs_base_point_vente ADD COLUMN accept_client_no_name boolean;
ALTER TABLE yvs_base_point_vente ALTER COLUMN accept_client_no_name SET DEFAULT false;

ALTER TABLE yvs_com_ecart_entete_vente ADD COLUMN statut_regle character(1);
DROP FUNCTION yvs_compta_content_journal(bigint, bigint, character varying);

--
ALTER TABLE yvs_compta_reglement_credit_fournisseur ADD COLUMN reference_externe character varying;
ALTER TABLE yvs_compta_reglement_credit_client ADD COLUMN reference_externe character varying;