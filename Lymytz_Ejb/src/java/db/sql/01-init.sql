UPDATE public.yvs_agences SET supp = true WHERE abbreviation IS NULL AND codeagence IS NULL AND designation IS NULL;
