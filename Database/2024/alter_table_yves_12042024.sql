ALTER TABLE public.yvs_prod_composant_op ADD marge_sup double precision NULL;
COMMENT ON COLUMN public.yvs_prod_composant_op.marge_sup IS 'Fixe marge maximale de consommation accept� au moment de la d�claration';

COMMENT ON COLUMN public.yvs_prod_composant_op.marge_sup IS 'Fixe la marge minimale de consommation accept� au moment de la d�claration';

ALTER TABLE public.yvs_prod_flux_composant ADD marge_superieure double precision NULL;

