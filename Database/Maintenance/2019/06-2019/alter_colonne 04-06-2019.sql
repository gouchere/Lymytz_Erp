SELECT insert_droit('gescom_stock_generer_entree', 'Générer une fiche d''entrée pour ajuster les restes à livrer', 
	(SELECT id FROM yvs_page_module WHERE reference = 'gescom_stock_dep'), 16, 'A','R');
	
SELECT insert_droit('stock_clean_all_doc_societe', 'Faire le nettoyage des documents sans contenus', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_user_'), 16, 'A','R');
	
SELECT insert_droit('base_user_fusion', 'Fusionner les données', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_user_'), 16, 'A','R');
	
-- Table: yvs_com_contenu_doc_stock_reception
-- DROP TABLE yvs_com_contenu_doc_stock_reception;
CREATE TABLE yvs_com_contenu_doc_stock_reception
(
  id bigserial NOT NULL,
  quantite double precision,
  date_reception date,
  contenu bigint,
  date_save timestamp without time zone DEFAULT now(),
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  calcul_pr boolean default true,
  author bigint,
  CONSTRAINT yvs_contenu_doc_stock_reception_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_com_contenu_doc_stock_reception_contenu_fkey FOREIGN KEY (contenu)
      REFERENCES yvs_com_contenu_doc_stock (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT yvs_com_contenu_doc_stock_reception_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_com_contenu_doc_stock_reception
  OWNER TO postgres;
  
  
INSERT INTO yvs_com_contenu_doc_stock_reception(quantite, date_reception, contenu, calcul_pr, date_save, date_update, author) 
	SELECT c.quantite_entree, c.date_reception, c.id, c.calcul_pr, c.date_save, c.date_update, c.author 
	FROM yvs_com_contenu_doc_stock c INNER JOIN yvs_com_doc_stocks d ON c.doc_stock = d.id
	WHERE d.type_doc = 'FT' AND c.statut = 'V';
	