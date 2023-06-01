ALTER TABLE public.yvs_prod_declaration_production DROP CONSTRAINT yvs_prod_declaration_production_doc_stock_fkey;

ALTER TABLE public.yvs_prod_declaration_production
    ADD CONSTRAINT yvs_prod_declaration_production_doc_stock_fkey FOREIGN KEY (doc_stock)
    REFERENCES public.yvs_com_contenu_doc_stock (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE SET NULL;