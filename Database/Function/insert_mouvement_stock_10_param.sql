-- Function: insert_mouvement_stock(bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying)
-- DROP FUNCTION insert_mouvement_stock(bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying);
CREATE OR REPLACE FUNCTION insert_mouvement_stock(
    article_ bigint,
    depot_ bigint,
    quantite_ double precision,
    coutentree_ double precision,
    cout_ double precision,
    parent_ bigint,
    tableexterne_ character varying,
    idexterne_ bigint,
    mouvement_ character varying,
    last_pr_ double precision,
	id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE 
	
    
BEGIN
	return insert_mouvement_stock(article_, depot_ , 0, quantite_ ,coutentree_ ,cout_ ,parent_ ,tableexterne_ ,idexterne_ ,mouvement_ ,current_date, last_pr_, id_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, double precision, bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, double precision, bigint) IS 'Insert une ligne de sortie de mouvement de stock';
