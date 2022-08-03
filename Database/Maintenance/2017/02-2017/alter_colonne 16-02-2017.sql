CREATE TABLE yvs_com_reservation_stock
(
  id bigserial NOT NULL,
  num_reference character varying,
  depot bigint,
  article bigint,
  date_reservation date,
  date_echeance date,
  quantite double precision,
  statut character varying,
  actif boolean,
  num_externe character varying,
  author bigint,
  CONSTRAINT yvs_com_reservation_stock_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_reservation_stock_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_reservation_stock_depot_fkey FOREIGN KEY (depot)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_com_reservation_stock_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_reservation_stock
  OWNER TO postgres;
  
ALTER TABLE yvs_com_doc_ventes DROP COLUMN consigner;
ALTER TABLE yvs_com_doc_ventes DROP COLUMN date_consigner;

INSERT INTO yvs_base_element_reference(designation, module, author) VALUES ('Reservation Stock', 'COM', 16);
