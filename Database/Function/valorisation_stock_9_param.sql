-- Function: valorisation_stock(bigint, bigint, bigint, double precision, double precision, character varying, bigint, character varying, date)

-- DROP FUNCTION valorisation_stock(bigint, bigint, bigint, double precision, double precision, character varying, bigint, character varying, date);

CREATE OR REPLACE FUNCTION valorisation_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, cout_ double precision, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE 
	meth_ character varying;
	new_cout_ double precision default 0;
BEGIN
	select into meth_ methode_val from yvs_base_articles where id = article_;
	if(cout_ is null)then
		cout_ = 0;
	end if;
	if(mouvement_ = 'E')then
		if(meth_ = 'FIFO')then
			return (select insert_mouvement_stock(article_, depot_, tranche_, quantite_, cout_, cout_, null, tableexterne_, idexterne_, 'E', date_));	
		elsif (meth_ = 'CMPI')then
			return (select valorisation_stock_by_cmp1(article_, depot_, tranche_, quantite_, cout_, tableexterne_, idexterne_, date_));
		elsif (meth_ = 'CMPII')then
		
		end if;
	else 
		if(meth_ = 'FIFO')then
			if(select valorisation_stock_by_peps(article_, depot_, tranche_, quantite_, tableexterne_, idexterne_, date_) > 0)then
				return true;
			end if;		
		elsif (meth_ = 'CMPI')then
		--récupère le dernier cout de revient (Cout de stock)
			select into new_cout_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ order by date_doc DESC, id desc limit 1;
			if(new_cout_ is null)then
				new_cout_ = 0;
			end if;
			return (select insert_mouvement_stock(article_ , depot_ , tranche_, quantite_ , new_cout_ , new_cout_ , null, tableexterne_, idexterne_, 'S', date_));
		elsif (meth_ = 'CMPII')then
		
		end if;
	end if;
	return false;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock(bigint, bigint, bigint, double precision, double precision, character varying, bigint, character varying, date)
  OWNER TO postgres;
