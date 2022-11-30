DROP TRIGGER IF EXISTS update ON public.yvs_prod_declaration_production;

CREATE TRIGGER update
    AFTER UPDATE 
    ON public.yvs_prod_declaration_production
    FOR EACH ROW
    WHEN (new.quantite <> old.quantite OR new.statut <> old.statut OR new.conditionnement <> old.conditionnement OR new.cout_production <> old.cout_production OR new.execute_trigger::text = 'OUI'::text)
    EXECUTE PROCEDURE public.insert_declaration_production();