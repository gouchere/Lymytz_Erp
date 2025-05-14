ALTER TABLE public.yvs_base_articles ALTER COLUMN methode_val SET DEFAULT 'CMPI'::character varying;

ALTER TABLE public.yvs_compta_parametre ADD compta_partiel_virement bool DEFAULT true NULL;

