CREATE TABLE public.yvs_historique_pr (
	id bigserial NOT NULL,
	conditionnement int8 NOT NULL,
	date_evaluation date NULL,
	depot int8 NULL,
	pr float8 NULL,
	CONSTRAINT yvs_historique_pr_pk PRIMARY KEY (id)
);

ALTER TABLE public.yvs_base_mouvement_stock DROP CONSTRAINT yvs_com_mouvement_stock_depot_fkey;

ALTER TABLE public.yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_tranche_fkey;

ALTER TABLE public.yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_parent_fkey;

ALTER TABLE public.yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey ;

ALTER TABLE public.yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_article_fkey ;

ALTER TABLE public.yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_author_fkey ;