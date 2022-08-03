CREATE TABLE yvs_warning_model_doc
(
  id serial NOT NULL,
  model integer,
  societe integer,
  ecart integer DEFAULT 30,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_warning_model_doc_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_warning_model_doc_model_fkey FOREIGN KEY (model)
      REFERENCES yvs_workflow_model_doc (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_warning_model_doc_societe_fkey FOREIGN KEY (societe)
      REFERENCES yvs_societes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_warning_model_doc_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_warning_model_doc
  OWNER TO postgres;
  
CREATE TABLE yvs_workflow_valid_bon_provisoire
(
  id bigserial NOT NULL,
  doc_caisse bigint,
  etape bigint,
  author bigint,
  etape_valid boolean,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_workflow_valid_bon_provisoire_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_workflow_valid_bon_provisoire_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_workflow_valid_bon_provisoire_doc_caisse_fkey FOREIGN KEY (doc_caisse)
      REFERENCES yvs_compta_bon_provisoire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_workflow_valid_bon_provisoire_etape_fkey FOREIGN KEY (etape)
      REFERENCES yvs_workflow_etape_validation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_workflow_valid_bon_provisoire
  OWNER TO postgres;
  
CREATE TABLE yvs_users_favoris
(
  id bigserial NOT NULL,
  titre character varying,
  module character varying,
  page character varying,
  users integer,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT now(),
  author bigint,
  CONSTRAINT yvs_users_favoris_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_users_favoris_users_fkey FOREIGN KEY (users)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_users_favoris_users_fkey1 FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_users_favoris
  OWNER TO postgres;
  
ALTER TABLE yvs_societes ADD COLUMN ecart_document integer DEFAULT 30;

ALTER TABLE yvs_base_depots ADD COLUMN principal boolean DEFAULT true;

ALTER TABLE yvs_compta_bon_provisoire DROP CONSTRAINT yvs_compta_bon_provisoire_piece_fkey;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN numero character varying;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN date_bon DATE DEFAULT current_date;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN date_valider DATE DEFAULT current_date;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN statut character varying(1) default 'E';
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN statut_paiement character varying(1) default 'W';
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN statut_justify character varying(1) default 'W';
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN montant double precision default 0;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN description character varying;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN etape_total integer default 0;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN etape_valide integer default 0;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN agence bigint;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN tiers bigint;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN caisse bigint;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN caissier bigint;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN valider_by bigint;
ALTER TABLE yvs_compta_bon_provisoire ADD COLUMN doc_caisse bigint;
ALTER TABLE yvs_compta_bon_provisoire
  ADD CONSTRAINT yvs_compta_bon_provisoire_caisse_fkey FOREIGN KEY (caisse)
      REFERENCES yvs_base_caisse (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_compta_bon_provisoire
  ADD CONSTRAINT yvs_compta_bon_provisoire_valider_by_fkey FOREIGN KEY (valider_by)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_compta_bon_provisoire
  ADD CONSTRAINT yvs_compta_bon_provisoire_caissier_fkey FOREIGN KEY (caissier)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE yvs_compta_bon_provisoire
  ADD CONSTRAINT yvs_compta_bon_provisoire_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE yvs_compta_bon_provisoire
  ADD CONSTRAINT yvs_compta_bon_provisoire_tiers_fkey FOREIGN KEY (tiers)
      REFERENCES yvs_base_tiers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
	  
DELETE FROM yvs_compta_bon_provisoire WHERE piece IS NULL;
UPDATE yvs_compta_bon_provisoire SET doc_caisse = (SELECT y.doc_divers FROM yvs_compta_caisse_piece_divers y WHERE y.id = piece);
UPDATE yvs_compta_bon_provisoire SET piece = doc_caisse;
ALTER TABLE yvs_compta_bon_provisoire DROP COLUMN doc_caisse;

ALTER TABLE yvs_compta_justificatif_bon DROP COLUMN date_justifier;
ALTER TABLE yvs_compta_justificatif_bon DROP CONSTRAINT yvs_compta_justificatif_bon_piece_fkey;
ALTER TABLE yvs_compta_justificatif_bon DROP COLUMN montant;
ALTER TABLE yvs_compta_justificatif_bon RENAME piece TO bon;
ALTER TABLE yvs_compta_justificatif_bon ADD COLUMN piece bigint;
ALTER TABLE yvs_compta_justificatif_bon
  ADD CONSTRAINT yvs_compta_justificatif_bon_bon_fkey FOREIGN KEY (bon)
      REFERENCES yvs_compta_bon_provisoire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE yvs_compta_justificatif_bon
  ADD CONSTRAINT yvs_compta_justificatif_bon_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_caisse_doc_divers (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE yvs_workflow_model_doc ADD COLUMN description character varying;

UPDATE yvs_compta_bon_provisoire SET beneficiaire = (SELECT p.beneficiaire FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = piece AND p.beneficiaire IS NOT NULL LIMIT 1),
				statut_paiement = COALESCE((SELECT p.statut_piece FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = piece LIMIT 1), 'W'),
				caisse = (SELECT p.caisse FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = piece AND caisse IS NOT NULL LIMIT 1),
				caissier = (SELECT p.caissier FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = piece AND caissier IS NOT NULL LIMIT 1),
				valider_by = (SELECT y.valider_by FROM yvs_compta_caisse_doc_divers y WHERE y.id = piece),
				date_valider = (SELECT y.date_valider FROM yvs_compta_caisse_doc_divers y WHERE y.id = piece),
				numero = (SELECT y.num_piece FROM yvs_compta_caisse_doc_divers y WHERE y.id = piece),
				montant = (SELECT y.montant FROM yvs_compta_caisse_doc_divers y WHERE y.id = piece),
				date_bon = (SELECT y.date_doc FROM yvs_compta_caisse_doc_divers y WHERE y.id = piece),
				description = (SELECT y.description FROM yvs_compta_caisse_doc_divers y WHERE y.id = piece),
				statut = (SELECT y.statut_doc FROM yvs_compta_caisse_doc_divers y WHERE y.id = piece),
				etape_valide = (SELECT y.etape_valide FROM yvs_compta_caisse_doc_divers y WHERE y.id = piece),
				etape_total = (SELECT y.etape_total FROM yvs_compta_caisse_doc_divers y WHERE y.id = piece),
				agence = (SELECT p.agence FROM yvs_users_agence p WHERE p.id = author LIMIT 1),
				tiers = (SELECT y.tiers FROM yvs_compta_caisse_doc_divers y WHERE y.id = piece);
				
INSERT INTO yvs_compta_bon_provisoire(beneficiaire, caisse, caissier, statut_paiement, numero, date_save, author, date_update, date_bon, tiers, statut, statut_justify, montant, description, etape_total, etape_valide, piece, valider_by, date_valider, agence)
	SELECT 	(SELECT p.beneficiaire FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = y.id AND p.beneficiaire IS NOT NULL LIMIT 1),
			(SELECT p.caisse FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = y.id AND p.caisse IS NOT NULL LIMIT 1),
			(SELECT p.caissier FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = y.id AND p.caissier IS NOT NULL LIMIT 1),
			COALESCE((SELECT p.statut_piece FROM yvs_compta_caisse_piece_divers p WHERE p.doc_divers = y.id LIMIT 1), 'W'), y.num_piece,
		y.date_save, y.author, y.date_update, y.date_doc, y.tiers, y.statut_doc, 'W' , y.montant, y.description, y.etape_total, y.etape_valide, y.id, y.valider_by, y.date_valider, 
		(SELECT p.agence FROM yvs_users_agence p WHERE p.id = y.author LIMIT 1) FROM yvs_compta_caisse_doc_divers y 
		WHERE y.bon_provisoire = TRUE AND y.id NOT IN (SELECT p.piece FROM yvs_compta_bon_provisoire p);
		
INSERT INTO yvs_workflow_valid_bon_provisoire (etape, author, etape_valid, date_update, date_save, doc_caisse)
 SELECT	etape, author, etape_valid, date_update, date_save, (SELECT y.id FROM yvs_compta_bon_provisoire y WHERE y.piece = w.doc_caisse) 
	FROM yvs_workflow_valid_doc_caisse w WHERE w.doc_caisse IN (SELECT DISTINCT piece FROM yvs_compta_bon_provisoire);
		
ALTER TABLE yvs_compta_piece_caisse_mission DROP CONSTRAINT yvs_compta_piece_caisse_mission_piece_fkey;
ALTER TABLE yvs_compta_piece_caisse_mission ADD COLUMN bon bigint;
UPDATE yvs_compta_piece_caisse_mission m SET bon = (SELECT y.id FROM yvs_compta_bon_provisoire y WHERE y.piece = m.piece);
UPDATE yvs_compta_piece_caisse_mission m SET piece = bon;
ALTER TABLE yvs_compta_piece_caisse_mission DROP COLUMN bon;
ALTER TABLE yvs_compta_piece_caisse_mission
  ADD CONSTRAINT yvs_compta_piece_caisse_mission_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_bon_provisoire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
			
INSERT INTO yvs_compta_justificatif_bon (bon, piece, date_save, date_update, author) SELECT y.id, y.piece, y.date_save, y.date_update, y.author FROM yvs_compta_bon_provisoire y;
ALTER TABLE yvs_compta_bon_provisoire DROP COLUMN piece;
ALTER TABLE yvs_compta_caisse_doc_divers DROP COLUMN bon_provisoire;
  
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, description) VALUES ('RETOUR_VENTE', 'yvs_com_doc_ventes', 16, 'Bon retour vente en attente');
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, description) VALUES ('AVOIR_VENTE', 'yvs_com_doc_ventes', 16, 'Facture avoir vente en attente');
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, description) VALUES ('RETOUR_ACHAT', 'yvs_com_doc_achats', 16, 'Bon retour achat en attente');
INSERT INTO yvs_workflow_model_doc(titre_doc, name_table, author, description) VALUES ('AVOIR_ACHAT', 'yvs_com_doc_achats', 16, 'Facture avoir achat en attente');

UPDATE yvs_workflow_model_doc SET description = 'Missions en attente' WHERE titre_doc = 'MISSIONS';
UPDATE yvs_workflow_model_doc SET description = 'Formation en attente' WHERE titre_doc = 'FORMATIONS';
UPDATE yvs_workflow_model_doc SET description = 'Conges en attente' WHERE titre_doc = 'CONGES';
UPDATE yvs_workflow_model_doc SET description = 'Facture achat en attente' WHERE titre_doc = 'FACTURE_ACHAT';
UPDATE yvs_workflow_model_doc SET description = 'Facture vente en attente' WHERE titre_doc = 'FACTURE_VENTE';
UPDATE yvs_workflow_model_doc SET description = 'Permission en attente' WHERE titre_doc = 'PERMISSION_CD';
UPDATE yvs_workflow_model_doc SET description = 'Sortie en attente' WHERE titre_doc = 'SORTIE_STOCK';
UPDATE yvs_workflow_model_doc SET description = 'Opération diverse en attente' WHERE titre_doc = 'OPERATION_DIVERS';
UPDATE yvs_workflow_model_doc SET description = 'Bon provisoire en attente' WHERE titre_doc = 'BON_OPERATION_DIVERS';
UPDATE yvs_workflow_model_doc SET description = 'Approvisionnment en attente' WHERE titre_doc = 'APPROVISIONNEMENT';

UPDATE yvs_workflow_model_doc SET name_table = 'yvs_compta_bon_provisoire' WHERE titre_doc = 'BON_OPERATION_DIVERS';

INSERT INTO yvs_base_element_reference(designation, module) VALUES ('Bon Provisoire', 'COFI');