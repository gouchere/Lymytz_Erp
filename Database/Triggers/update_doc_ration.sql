-- Function: update_doc_ration()
-- DROP FUNCTION update_doc_ration();
CREATE OR REPLACE FUNCTION update_doc_ration()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	mouv_ record;	
	ligne_ record;
	
	result_ boolean default false;
	prix_ double precision;
	
	ACTION_  CHARACTER VARYING;
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
BEGIN
	ACTION_= TG_OP;	--INSERT DELETE TRONCATE UPDATE   
	CASE ACTION_
	WHEN 'DELETE' THEN
		for ligne_ in select id from yvs_com_ration where doc_ration = OLD.id
		loop			
			delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_ration';
		end loop;
		RETURN OLD;
	WHEN 'UPDATE' THEN	
		IF(EXEC_) THEN
			if(NEW.statut = 'V') then
				SELECT INTO doc_ cd.id, cd.tranche FROM yvs_com_doc_ration d INNER JOIN yvs_com_creneau_depot cd ON cd.id=d.creneau_horaire WHERE d.id=NEW.id;
				for ligne_ in select id, article , quantite, conditionnement, date_ration, calcul_pr FROM yvs_com_ration r  WHERE r.doc_ration= NEW.id
				loop
					SELECT INTO arts_ a.id, a.methode_val, u.prix FROM yvs_base_articles a INNER JOIN yvs_base_conditionnement u on u.article = a.id WHERE a.id = ligne_.article and u.id = ligne_.conditionnement;
					prix_ = get_pr(ligne_.article, NEW.depot, 0, ligne_.date_ration, ligne_.conditionnement);
					--Insertion mouvement stock
					SELECT INTO mouv_ id, cout_stock FROM yvs_base_mouvement_stock WHERE id_externe = ligne_.id and table_externe = 'yvs_com_ration';
					if(mouv_.id is not null)then
						if(arts_.methode_val = 'FIFO') THEN
							DELETE FROM yvs_base_mouvement_stock WHERE id_externe = ligne_.id and table_externe = 'yvs_com_ration';
							result_ = (select valorisation_stock(ligne_.article, ligne_.conditionnement, NEW.depot, doc_.tranche, ligne_.quantite, prix_, 'yvs_com_ration', ligne_.id, 'S', ligne_.date_ration));
						else
							PERFORM insert_mouvement_stock(ligne_.article, NEW.depot, doc_.tranche, ligne_.quantite, prix_, mouv_.cout_stock, null, 'yvs_com_ration', ligne_.id, 's', ligne_.date_ration, prix_, mouv_.id);
						end if;
					else
						result_= (select valorisation_stock(ligne_.article,ligne_.conditionnement, NEW.depot, doc_.tranche, ligne_.quantite,prix_, 'yvs_com_ration', ligne_.id, 'S', ligne_.date_ration));
					end if;	
				end loop;
			elsif(NEW.statut != 'V')then
				for ligne_ in select id from yvs_com_ration where doc_ration = OLD.id
				loop				
					--Recherche mouvement stock
					delete from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_ration';
				end loop;
			end if;	
		END IF;
		RETURN NEW;
	END CASE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_ration()
  OWNER TO postgres;
