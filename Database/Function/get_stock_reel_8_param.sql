-- Function: get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, date, character varying)

-- DROP FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_debut_ date, date_fin_ date, type_ character varying)
  RETURNS double precision AS
$BODY$
DECLARE 
	valeur_ double precision default 0; 
BEGIN

	if(type_ is not null)then
		if(type_ = 'M')then
			select into valeur_ AVG(get_stock_reel(art_, tranche_, depot_, agence_, societe_, (jour - interval '1 day')::date, jour::date)) as stock from generate_series(date_debut_, date_fin_, interval '1 day') as jour;
		elsif(type_ = 'S')then
			select into valeur_ SUM(get_stock_reel(art_, tranche_, depot_, agence_, societe_, (jour - interval '1 day')::date, jour::date)) as stock from generate_series(date_debut_, date_fin_, interval '1 day') as jour;
		else
			select into valeur_ get_stock_reel(art_, tranche_, depot_ , agence_ , societe_, date_debut_, date_fin_);
		end if;
	end if;
	return valeur_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_reel(bigint, bigint, bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
