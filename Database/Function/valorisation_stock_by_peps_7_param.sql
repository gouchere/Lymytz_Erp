-- Function: valorisation_stock_by_peps(bigint, bigint, bigint, double precision, character varying, bigint, date)
-- DROP FUNCTION valorisation_stock_by_peps(bigint, bigint, bigint, double precision, character varying, bigint, date);
CREATE OR REPLACE FUNCTION valorisation_stock_by_peps(
    article_ bigint,
    depot_ bigint,
    tranche_ bigint,
    quantite_ double precision,
    tableexterne_ character varying,
    idexterne_ bigint,
    date_ date)
  RETURNS integer AS
$BODY$
DECLARE 
	i integer default 0;  
	entree_ record;  
	qte double precision;
	stock double precision default 0;
	reste double precision default 0;
BEGIN
	return valorisation_stock_by_peps(article_ , 0,depot_ , tranche_, quantite_ ,tableexterne_ ,idexterne_ , date_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_peps(bigint, bigint, bigint, double precision, character varying, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_peps(bigint, bigint, bigint, double precision, character varying, bigint, date) IS 'Valorisation de stock pas la methode PEPS';
