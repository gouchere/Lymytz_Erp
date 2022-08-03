
ALTER TABLE yvs_base_article_depot ADD COLUMN depot_pr bigint;
ALTER TABLE yvs_base_article_depot
  ADD CONSTRAINT yvs_base_article_depot_depot_pr_fkey FOREIGN KEY (depot_pr)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
UPDATE yvs_base_article_depot SET depot_pr = depot;
	  
SELECT insert_droit('base_article_change_reference', 'Modifier la reference de l''article', 
	(SELECT id FROM yvs_page_module WHERE reference = 'base_view_article'), 16, 'A','R');