-- Function: delete_doc_stocks()

-- DROP FUNCTION delete_doc_stocks();

CREATE OR REPLACE FUNCTION delete_doc_stocks()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
BEGIN
	if(OLD.statut = 'V') then
		for cont_ in select id from yvs_com_contenu_doc_stock where doc_stock = OLD.id
		loop
			
			--Recherche mouvement stock
			for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_stock'
			loop
				delete from yvs_base_mouvement_stock where id = mouv_;
			end loop;
		end loop;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_doc_stocks()
  OWNER TO postgres;
