-- Function: com_et_dashboard(bigint, bigint, bigint, date, date)

-- DROP FUNCTION com_et_dashboard(bigint, bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION com_et_dashboard_generale(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN debut_anne_p date, IN fin_anne_p date, IN jour_anne_p date)
  RETURNS TABLE(valeur double precision, code character varying, libelle character varying, rang integer, lien character varying) AS
$BODY$
declare 

   query_ character varying;
   code_ character varying;
   libelle_ character varying;
   
   valeur_ double precision default 0;
   autres_ double precision default 0;
   
   jourN_ date ;
   moisN_ date;
   
BEGIN	
	DROP TABLE IF EXISTS table_et_dashboard;
	CREATE TEMP TABLE IF NOT EXISTS table_et_dashboard(_valeur double precision, _code character varying, _libelle character varying, _rang integer, _lien character varying);
	DELETE FROM table_et_dashboard;
	-- chiffre
		-- CA de la période passé en paramètre
	code_ = 'caMois';
	libelle_ = 'Chiffre d''affaire du '||date_fin_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V',null, null, 0, 0, 0,0, 0, date_debut_, date_fin_,'CA');
	INSERT INTO table_et_dashboard VALUES (COALESCE(valeur_, 0), code_, libelle_, 0,'');
		-- CA du jour
	code_ = 'caJour';
	libelle_ = 'Chiffre d''affaire du '||date_fin_;
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V',null, null, 0, 0, 0,0, 0, date_fin_, date_fin_,'CA');
	INSERT INTO table_et_dashboard VALUES (COALESCE(valeur_, 0), code_, libelle_, 0,'');
		-- CA du jour N-1
	code_ = 'caJourP';
	libelle_ = 'Chiffre d''affaire du '||jour_anne_p;
	moisN_= date_fin_- interval '1 year';
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V',null, null, 0, 0, 0,0, 0, jour_anne_p, jour_anne_p,'CA');
	INSERT INTO table_et_dashboard VALUES (COALESCE(valeur_, 0), code_, libelle_, 0,'');
		-- CA du mois N-1
	code_ = 'caMoisP';
	libelle_ = 'Chiffre d''affaire du ';
	SELECT INTO valeur_ get_ca_vente(societe_, agence_, 'V',null, null, 0, 0, 0,0, 0, debut_anne_p, fin_anne_p,'CA');
	INSERT INTO table_et_dashboard VALUES (COALESCE(valeur_, 0), code_, libelle_, 0,'');
	-- Statistique produits
		-- Nombre de produits actifs
		code_ = 'nbArticle';
		libelle_ = 'Nombre de produits';
		SELECT INTO valeur_ COUNT(a.id) FROM yvs_base_articles a INNER JOIN yvs_base_famille_article f ON f.id=a.famille WHERE f.societe=societe_ AND a.actif is true;
		INSERT INTO table_et_dashboard VALUES (COALESCE(valeur_, 0), code_, libelle_, 1,'');
		-- Nombre de clients actifs
		code_ = 'nbClient';
		libelle_ = 'Nombre de clients actifs';
		SELECT INTO valeur_ COUNT(c.id) FROM yvs_com_client c INNER JOIN yvs_base_tiers t ON t.id=c.tiers WHERE t.societe=societe_ AND c.actif is true;
		INSERT INTO table_et_dashboard VALUES (COALESCE(valeur_, 0), code_, libelle_, 1,'');
		-- Nombre de fournisseurs actifs
		code_ = 'nbFournisseur';
		libelle_ = 'Nombre de fournisseurs actifs';
		SELECT INTO valeur_ COUNT(f.id) FROM yvs_base_fournisseur f INNER JOIN yvs_base_tiers t ON t.id=f.tiers WHERE t.societe=societe_ AND f.actif is true;
		INSERT INTO table_et_dashboard VALUES (COALESCE(valeur_, 0), code_, libelle_,1,'');
		-- Nombre d'employés actifs
		code_ = 'nbEmploye';
		libelle_ = 'Nombre d''employé actifs';
		SELECT INTO valeur_ COUNT(e.id) FROM yvs_grh_employes e INNER JOIN yvs_agences a ON a.id=e.agence WHERE a.societe=societe_ AND a.actif is true;
		INSERT INTO table_et_dashboard VALUES (COALESCE(valeur_, 0), code_, libelle_, 1,'');
		
	RETURN QUERY SELECT * FROM table_et_dashboard ORDER BY _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_dashboard_generale(bigint,bigint,date, date,date, date, date)
  OWNER TO postgres;
