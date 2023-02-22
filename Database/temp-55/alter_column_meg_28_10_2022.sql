
ALTER TABLE IF EXISTS public.yvs_com_parametre_vente ADD COLUMN livraison_auto boolean DEFAULT false;

ALTER TABLE IF EXISTS public.yvs_com_parametre_achat ADD COLUMN print_document_when_valide boolean DEFAULT false;
ALTER TABLE IF EXISTS public.yvs_com_parametre_stock ADD COLUMN print_document_when_valide boolean DEFAULT false;
ALTER TABLE IF EXISTS public.yvs_com_parametre_vente ADD COLUMN print_document_when_valide boolean DEFAULT false;