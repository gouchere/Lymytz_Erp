
SELECT insert_droit('compta_od_valide_recette', 'Valider les recettes en OD', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'), 16, 'A,B,C','R');
	
SELECT insert_droit('compta_od_valide_depense', 'Valider les dépenses en OD', 
	(SELECT id FROM yvs_page_module WHERE reference = 'compta_view_op_div'), 16, 'A,B,C','R');
	
ALTER TABLE yvs_compta_parametre ADD COLUMN montant_seuil_recette_od double precision;
ALTER TABLE yvs_compta_parametre ALTER COLUMN montant_seuil_recette_od SET DEFAULT 0;

ALTER TABLE yvs_compta_parametre RENAME COLUMN montant_seuil_od TO montant_seuil_depense_od;

ALTER TABLE yvs_synchro_listen_table ADD COLUMN author bigint;
ALTER TABLE yvs_synchro_listen_table
  ADD CONSTRAINT yvs_synchro_listen_table_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
-- Table: yvs_synchro_tables
-- DROP TABLE yvs_synchro_tables;
CREATE TABLE yvs_synchro_tables
(
  id serial NOT NULL,
  table_name character varying,
  use_societe boolean DEFAULT true,
  CONSTRAINT yvs_synchro_tables_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_synchro_tables
  OWNER TO postgres;
  
INSERT INTO yvs_synchro_tables (table_name, use_societe) SELECT y, true FROM return_field(null, null, null) y WHERE y NOT LIKE 'yvs_synchro%';	 	  
	  
UPDATE yvs_base_articles SET author = (SELECT y.id FROM yvs_users_agence y INNER JOIN yvs_agences a On y.agence = a.id INNER JOIN yvs_base_famille_article f ON a.societe = f.societe WHERE f.id = famille LIMIT 1) 
	WHERE author IS NULL;
	