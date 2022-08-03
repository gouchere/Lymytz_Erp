-- Function: prod_et_journal_production(bigint, bigint, bigint, character varying, date, date, character varying, integer, character varying)
-- DROP FUNCTION prod_et_journal_production(bigint, bigint, bigint, character varying, date, date, character varying, integer, character varying);
CREATE OR REPLACE FUNCTION prod_et_journal_production(IN societe_ bigint, IN agence_ bigint, IN depot_ bigint, IN article_ character varying, IN date_debut_ date, IN date_fin_ date, IN categorie_ character varying, IN cumule_by_ integer, IN valorise_by_ character varying)
  RETURNS TABLE(id bigint, code character varying, designation character varying, unite bigint, reference character varying, production double precision, mp bigint, code_mp character varying, designation_mp character varying, unite_mp bigint, reference_mp character varying, equipe bigint, code_equipe character varying, nom_equipe character varying, prix_vente double precision, prix_achat double precision, prix_revient double precision, prix_prod double precision, quantite double precision, valeur double precision) AS
$BODY$
DECLARE 
   
BEGIN 	
	RETURN QUERY SELECT * FROM prod_et_journal_production(societe_, agence_, depot_, article_, date_debut_, date_fin_, categorie_, cumule_by_, valorise_by_, 'A');
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION prod_et_journal_production(bigint, bigint, bigint, character varying, date, date, character varying, integer, character varying)
  OWNER TO postgres;
