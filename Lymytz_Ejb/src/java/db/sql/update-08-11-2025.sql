UPDATE public.yvs_com_parametre_stock SET duree_save_ration = 0 WHERE agence IN 
(SELECT a.id FROM public.yvs_agences a INNER JOIN public.yvs_societes s ON a.societe = s.id 
    WHERE UPPER(s.code_abreviation) LIKE '%GLP%' OR UPPER(a.designation) LIKE '%GLP%');
