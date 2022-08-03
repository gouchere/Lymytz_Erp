ALTER TABLE yvs_com_lot_reception ADD COLUMN article bigint;
ALTER TABLE yvs_com_lot_reception
  ADD CONSTRAINT yvs_com_lot_reception_article_fkey FOREIGN KEY (article)
      REFERENCES yvs_base_articles (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;
	  
	  
ALTER TABLE yvs_com_parametre_vente ADD COLUMN give_bonus_in_status character varying default 'R';