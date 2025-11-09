-- Date: 08 Nov 2025 10:15:00
-- Description: Initialise la durée de retrogradation des dates de rations à 0 pour les agences GLP
UPDATE public.yvs_com_parametre_stock SET duree_save_ration = 0 WHERE agence IN
(SELECT a.id FROM public.yvs_agences a INNER JOIN public.yvs_societes s ON a.societe = s.id 
    WHERE UPPER(s.code_abreviation) LIKE '%GLP%' OR UPPER(a.designation) LIKE '%GLP%');

ALTER TABLE public.yvs_compta_justificatif_bon ADD date_justification timestamp without time zone NULL;
ALTER TABLE public.yvs_compta_justif_bon_achat ADD date_justification timestamp without time zone NULL;

SELECT insert_droit('compta_injustifier_bp', 'Injustifier un bon provisoire déjà justifié',
                    (SELECT id FROM yvs_page_module WHERE reference = 'compta_view_bon_prov'), null, 'A,B','R');


