ALTER TABLE yvs_com_lot_reception DROP COLUMN critere;
ALTER TABLE yvs_com_lot_reception DROP COLUMN valeur_critere;
ALTER TABLE yvs_com_lot_reception DROP COLUMN description;
ALTER TABLE yvs_com_lot_reception DROP COLUMN parent;

ALTER TABLE yvs_com_lot_reception RENAME date_lot TO date_fabrication;
ALTER TABLE yvs_com_lot_reception ADD COLUMN date_expiration DATE;
ALTER TABLE yvs_com_lot_reception ADD COLUMN statut character varying;
ALTER TABLE yvs_com_lot_reception ADD COLUMN agence bigint;
ALTER TABLE yvs_com_lot_reception
  ADD CONSTRAINT yvs_com_lot_reception_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_com_parametre ADD COLUMN use_lot_reception boolean default false;

-- Table: yvs_base_mouvement_stock_lot
-- DROP TABLE yvs_base_mouvement_stock_lot;
CREATE TABLE yvs_base_mouvement_stock_lot
(
  id bigserial NOT NULL,
  mouvement bigint,
  lot bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_base_mouvement_stock_lot_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_mouvement_stock_lot_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_mouvement_stock_lot_lot_fkey FOREIGN KEY (lot)
      REFERENCES yvs_com_lot_reception (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_mouvement_stock_lot_mouvement_fkey FOREIGN KEY (mouvement)
      REFERENCES yvs_base_mouvement_stock (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_mouvement_stock_lot
  OWNER TO postgres;


-- Index: yvs_base_mouvement_stock_lot_article_idx
-- DROP INDEX yvs_base_mouvement_stock_lot_article_idx;
CREATE INDEX yvs_base_mouvement_stock_lot_lot_idx
  ON yvs_base_mouvement_stock_lot
  USING btree
  (lot);
  
-- Index: yvs_base_mouvement_stock_lot_article_idx
-- DROP INDEX yvs_base_mouvement_stock_lot_article_idx;
CREATE INDEX yvs_base_mouvement_stock_lot_mouvement_idx
  ON yvs_base_mouvement_stock_lot
  USING btree
  (mouvement);
  
  
INSERT INTO yvs_base_mouvement_stock_lot(lot, mouvement, author, date_save, date_update) 
SELECT m.lot, m.id, m.author, m.date_save, m.date_update FROM yvs_base_mouvement_stock m WHERE m.lot IS NOT NULL;

ALTER TABLE yvs_base_mouvement_stock DROP COLUMN lot;
ALTER TABLE yvs_base_mouvement_stock DROP COLUMN qualite;


-- Table: yvs_base_mouvement_stock_qualite
-- DROP TABLE yvs_base_mouvement_stock_qualite;
CREATE TABLE yvs_base_mouvement_stock_qualite
(
  id bigserial NOT NULL,
  mouvement bigint,
  qualite bigint,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  execute_trigger character varying,
  CONSTRAINT yvs_base_mouvement_stock_qualite_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_base_mouvement_stock_qualite_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_mouvement_stock_qualite_qualite_fkey FOREIGN KEY (qualite)
      REFERENCES yvs_com_qualite (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_base_mouvement_stock_qualite_mouvement_fkey FOREIGN KEY (mouvement)
      REFERENCES yvs_base_mouvement_stock (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_base_mouvement_stock_qualite
  OWNER TO postgres;


-- Index: yvs_base_mouvement_stock_qualite_article_idx
-- DROP INDEX yvs_base_mouvement_stock_qualite_article_idx;
CREATE INDEX yvs_base_mouvement_stock_qualite_qualite_idx
  ON yvs_base_mouvement_stock_qualite
  USING btree
  (qualite);
  
-- Index: yvs_base_mouvement_stock_qualite_article_idx
-- DROP INDEX yvs_base_mouvement_stock_qualite_article_idx;
CREATE INDEX yvs_base_mouvement_stock_qualite_mouvement_idx
  ON yvs_base_mouvement_stock_qualite
  USING btree
  (mouvement);
  
SELECT alter_action_colonne_key('yvs_ressources_page', true, true);
DELETE FROM yvs_ressources_page WHERE reference_ressource = 'stock_view_all_depot';