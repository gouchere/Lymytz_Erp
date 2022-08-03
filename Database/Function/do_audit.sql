-- Function: do_audit(bigint, bigint, bigint, date, date, character varying)

-- DROP FUNCTION do_audit(bigint, bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION do_audit(IN depot_ bigint, IN agence_ bigint, IN societe_ bigint, IN date_debut_ date, IN date_fin_ date, IN type_ character varying)
  RETURNS TABLE(depot bigint, element character varying, valeur_exacte character varying, valeur_prevu character varying, valeur_autre character varying, type_test character varying, nature character varying) AS
$BODY$   
DECLARE 
	data_ record;
	connexion character varying default 'dbname=lymytz_demo_0 user=postgres password=yves1910/';
	valeur_exacte_ double precision default 0;
	valeur_prevu_ double precision default 0;
	query_not_in character varying default '';
	query_article_depot character varying default '';
	query_out_moyenne character varying default '';	
	query_great_net character varying default '';	
	
BEGIN
	DROP TABLE IF EXISTS table_audit;
	CREATE TEMP TABLE IF NOT EXISTS table_audit(dep bigint, elt character varying, exacte character varying, prevu character varying, autre character varying, test character varying, nat character varying);
	if(type_ = 'S' or (type_ is not null or type_ = ''))then
		if(depot_ is null or depot_ < 1)then
			if(agence_ is null or agence_ < 1)then
				if(societe_ is null or societe_ < 1)then
					query_not_in = '';
					query_article_depot = '';
				else
					query_not_in = 'select distinct(y.article), y.depot from yvs_base_mouvement_stock y inner join yvs_base_depots d on y.depot = d.id inner join yvs_agences a on d.agence = a.id where a.societe = '||societe_||' and (y.date_doc between '''||date_debut_||''' and '''||date_fin_||''') and y.article not in 
					(select distinct(d.article) from yvs_base_article_depot d where d.depot = y.depot)';
					query_article_depot = 'select y.article, y.depot, COALESCE(y.stock_min,0) as stock_min, COALESCE(y.stock_max,0) as stock_max, COALESCE(y.marg_stock_moyen,0) as marg_stock_moyen, COALESCE(y.stock_net,0) as stock_net from yvs_base_article_depot y inner join yvs_base_depots d on y.depot = d.id 
						inner join yvs_agences a on d.agence = a.id where a.societe = '||societe_;
				end if;
			else
				query_not_in = 'select distinct(y.article), y.depot from yvs_base_mouvement_stock y inner join yvs_base_depots d on y.depot = d.id where d.agence = '||agence_||' and (y.date_doc between '''||date_debut_||''' and '''||date_fin_||''') and y.article not in 
				(select distinct(d.article) from yvs_base_article_depot d where d.depot = y.depot)';					
				query_article_depot = 'select y.article, y.depot, COALESCE(y.stock_min,0) as stock_min, COALESCE(y.stock_max,0) as stock_max, COALESCE(y.marg_stock_moyen,0) as marg_stock_moyen, COALESCE(y.stock_net,0) as stock_net from yvs_base_article_depot y inner join yvs_base_depots d on y.depot = d.id where d.agence = '||agence_;
			end if;				
		else
			query_not_in = 'select distinct(y.article), y.depot from yvs_base_mouvement_stock y where y.depot = '||depot_||' and (y.date_doc between '''||date_debut_||''' and '''||date_fin_||''') and y.article not in (select distinct(d.article) from yvs_base_article_depot d where d.depot = y.depot)';
			query_article_depot = 'select y.article, y.depot, COALESCE(y.stock_min,0) as stock_min, COALESCE(y.stock_max,0) as stock_max, COALESCE(y.marg_stock_moyen,0) as marg_stock_moyen, COALESCE(y.stock_net,0) as stock_net from yvs_base_article_depot y where y.depot = '||depot_||'';
		end if;
		--Articles qui ont du stock mais qui ne sont pas rattachés au dépôt en question
		RAISE NOTICE 'query_not_in %',query_not_in;
		if(query_not_in is not null and query_not_in != '')then
			FOR data_  IN SELECT * FROM dblink(connexion, query_not_in) AS t(article bigint, depot bigint)
			loop
				valeur_exacte_ = (select get_stock_reel(data_.article, data_.depot, date_fin_));
				INSERT INTO table_audit values(data_.depot, data_.article||'', ''||valeur_exacte_, 'Stock prevu : '||valeur_prevu_, 'true',  'Article qui ont du stock mais qui n''existe pas dans le dépôt', 'STOCK');
			end loop;
		end if;
		RAISE NOTICE 'query_article_depot %',query_article_depot;
		if(query_article_depot is not null and query_article_depot != '')then
			FOR data_  IN SELECT * FROM dblink(connexion, query_article_depot) AS t(article bigint, depot bigint, stock_min double precision, stock_max double precision, marg_stock_moyen double precision, stock_net double precision)
			loop
				valeur_exacte_ = (select get_stock_reel(data_.article, data_.depot, date_fin_));
				valeur_prevu_ = (select get_stock_reel(data_.article, data_.depot, date_debut_, date_fin_, 'M'));	
				
				--Articles dont le stock est hors des marges prévu dans le depot
				if(valeur_exacte_ not between data_.stock_min and data_.stock_max)then
					INSERT INTO table_audit values(data_.depot, data_.article||'', ''||valeur_exacte_, 'Stock min : '||data_.stock_min||' - Stock max : '||data_.stock_max, valeur_prevu_||'',  'Article dont le stock est hors des marges prévu', 'STOCK');
				end if;

				--Articles dont le stock est hors des marges de stock moyen prévu dans le depot
				if(valeur_exacte_ not between (valeur_prevu_ - data_.marg_stock_moyen) and (valeur_prevu_ + data_.marg_stock_moyen))then
					INSERT INTO table_audit values(data_.depot, data_.article||'', ''||valeur_exacte_, 'Stock moyen  : '||valeur_prevu_, valeur_prevu_||'' ,'Article dont le stock est hors des marges du stock moyen', 'STOCK');
				end if;

				--Articles dont le stock est superieur au stock net prévu dans le depot
				if(data_.stock_net is not null and data_.stock_net > -1)then
					if(valeur_exacte_ > data_.stock_net)then
						INSERT INTO table_audit values(data_.depot, data_.article||'', ''||valeur_exacte_, 'Stock net  : '||data_.stock_net, valeur_prevu_||'', 'Article dont le stock est superieur au stock net', 'STOCK');
					end if;
				end if;
			end loop;
		end if;
	end if;
	return QUERY select * from table_audit order by depot, nat, test,  elt;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION do_audit(bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
COMMENT ON FUNCTION do_audit(bigint, bigint, bigint, date, date, character varying) IS 'Audit sur les types specifiés S pour stocks -- ';
