-- Function: grh_et_progression_all(bigint, bigint, bigint, bigint, date, date, character varying)

-- DROP FUNCTION grh_et_progression_all(bigint, bigint, bigint, bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION grh_et_progression_all(IN societe_ bigint, IN agence_ bigint, IN service_ bigint, IN employe_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(agence bigint, employe bigint, code character varying, nom character varying, periode character varying, rang integer, salaire double precision, presence double precision, conge double precision, permission double precision, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE 
	_connexion_ CHARACTER VARYING DEFAULT 'dbname=lymytz_demo_0 user=postgres password=yves1910/';
	_query_ CHARACTER VARYING DEFAULT '';
	
	_employe_ RECORD;
	_data_ RECORD;
	
	_salaire_ DOUBLE PRECISION DEFAULT 0;
	_presence_ DOUBLE PRECISION DEFAULT 0;
	_conge_ DOUBLE PRECISION DEFAULT 0;
	_permission_ DOUBLE PRECISION DEFAULT 0;
	_sum_salaire_ DOUBLE PRECISION DEFAULT 0;
	_sum_presence_ DOUBLE PRECISION DEFAULT 0;
	_sum_conge_ DOUBLE PRECISION DEFAULT 0;
	_sum_permission_ DOUBLE PRECISION DEFAULT 0;
	
	_date_save_ DATE DEFAULT date_debut_;
	_date_fin_ DATE;
	_periode_ CHARACTER VARYING;
	_jour_ character varying;
	_mois_ character varying;
	_mois_0_ character varying;
	_annee_ character varying;
	_annee_0_ character varying;
	
	i INTEGER DEFAULT 0;

    dates_ RECORD;
BEGIN 	
	--DROP TABLE IF EXISTS grh_recap_presence;
	CREATE TEMP TABLE IF NOT EXISTS grh_table_progression_all(_agence BIGINT, _employe BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode CHARACTER VARYING, _rang INTEGER, _salaire DOUBLE PRECISION, _presence DOUBLE PRECISION, _conge DOUBLE PRECISION, _permission DOUBLE PRECISION, _is_total BOOLEAN, _is_footer BOOLEAN); 
	DELETE FROM grh_table_progression_all;
	IF(employe_ IS NOT NULL AND employe_ > 0)THEN
		_query_ = 'SELECT e.id, e.nom, e.prenom, e.matricule, e.agence FROM yvs_grh_employes e INNER JOIN yvs_agences a ON a.id = e.agence WHERE e.id = '||employe_||' ORDER BY e.nom';
	ELSIF(service_ IS NOT NULL AND service_ > 0)THEN
		_query_ = 'SELECT e.id, e.nom, e.prenom, e.matricule, e.agence FROM yvs_grh_employes e INNER JOIN yvs_grh_poste_de_travail p ON p.id = e.poste_actif WHERE p.departement = '||service_||' ORDER BY e.nom';
	ELSIF(agence_ IS NOT NULL AND agence_ > 0)THEN
		_query_ = 'SELECT e.id, e.nom, e.prenom, e.matricule, e.agence FROM yvs_grh_employes e INNER JOIN yvs_agences a ON a.id = e.agence WHERE e.agence = '||agence_||' ORDER BY e.nom';
	ELSE
		_query_ = 'SELECT e.id, e.nom, e.prenom, e.matricule, e.agence FROM yvs_grh_employes e INNER JOIN yvs_agences a ON a.id = e.agence WHERE a.societe = '||societe_||' ORDER BY a.designation, e.nom';
	END IF;
	
	RAISE NOTICE 'query %',_query_;
	IF(_query_ IS NOT NULL AND _query_ != '')THEN
		FOR _employe_ IN SELECT * FROM dblink(_connexion_, _query_) AS t(id BIGINT, nom CHARACTER VARYING, prenom CHARACTER VARYING, matricule CHARACTER VARYING, agence BIGINT)
		LOOP
			date_debut_ = _date_save_; i = 0;
			_sum_salaire_ = 0; _sum_presence_ = 0; _sum_conge_ = 0; _sum_permission_ = 0;
			WHILE(date_debut_ <= date_fin_)
			LOOP
				_salaire_ = 0; _presence_ = 0; _conge_ = 0; _permission_ = 0;
				SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, periode_);
				IF(dates_.date_sortie IS NOT NULL)THEN
					_date_fin_ = dates_.date_sortie;
					_periode_ = dates_.intitule;
				END IF;
				RAISE NOTICE 'date_ % %', _periode_,_date_fin_;
				

				-- Recuperation des informations de presence de l'employé dans l'interval de dates
				FOR _data_ IN SELECT * FROM grh_presence_durees(_employe_.id, _employe_.agence, societe_, date_debut_, _date_fin_)
				LOOP
					IF(_data_.element = 'Je')THEN
						_presence_ = _data_.valeur;
					ELSIF(_data_.element = 'Ca')THEN
						_conge_ = _data_.valeur;
					ELSIF(_data_.element = 'Pl')THEN
						_permission_ = _data_.valeur;
					ELSE
						
					END IF;
				END LOOP;
				--Calcul de la masse salarial de l'employe dans l'interval de dates
				SELECT INTO _salaire_ COALESCE(SUM(y.montant_payer - y.retenu_salariale), 0) FROM Yvs_grh_detail_bulletin y INNER JOIN yvs_grh_element_salaire s ON y.element_salaire = s.id 
				INNER JOIN yvs_grh_bulletins b ON y.bulletin = b.id INNER JOIN yvs_grh_ordre_calcul_salaire o ON b.entete = o.id
				INNER JOIN yvs_grh_contrat_emps c ON b.contrat = c.id INNER JOIN yvs_grh_employes e ON c.employe = e.id
				WHERE c.actif AND s.visible_bulletin = true AND y.now_visible = true AND c.employe = _employe_.id AND 
					(y.montant_employeur != 0 OR y.montant_payer != 0 OR y.retenu_salariale != 0) 
					AND ((o.debut_mois BETWEEN date_debut_ AND _date_fin_) AND (o.fin_mois BETWEEN date_debut_ AND _date_fin_));
					
				_sum_salaire_ = _sum_salaire_ + _salaire_; 
				_sum_presence_ = _sum_presence_ + _presence_; 
				_sum_conge_ = _sum_conge_ + _conge_; 
				_sum_permission_ = _sum_permission_ + _permission_;
					
				INSERT INTO grh_table_progression_all VALUES(_employe_.agence, _employe_.id, _employe_.matricule, TRIM(CONCAT(_employe_.prenom, ' ', _employe_.nom)), _periode_, i, COALESCE(_salaire_, 0), COALESCE(_presence_, 0), COALESCE(_conge_, 0), COALESCE(_permission_, 0), FALSE, FALSE);
				i = i + 1;
				
				IF(periode_ = 'A')THEN
					date_debut_ = date_debut_ + INTERVAL '1 YEAR';
				ELSIF(periode_ = 'T')THEN
					date_debut_ = date_debut_ + INTERVAL '3 MONTH';
				ELSIF(periode_ = 'M')THEN
					date_debut_ = date_debut_ + INTERVAL '1 MONTH';
				ELSIF(periode_ = 'S')THEN
					date_debut_ = date_debut_ + INTERVAL '1 week';
				ELSE
					date_debut_ = date_debut_ + INTERVAL '1 DAY';
				END IF;
			END LOOP;	
			INSERT INTO grh_table_progression_all VALUES(_employe_.agence, _employe_.id, _employe_.matricule, TRIM(CONCAT(_employe_.prenom, ' ', _employe_.nom)), 'TOTAUX', i, COALESCE(_sum_salaire_, 0), COALESCE(_sum_presence_, 0), COALESCE(_conge_, 0), COALESCE(_sum_permission_, 0), TRUE, FALSE);
		END LOOP;
		
		FOR _data_ IN SELECT _agence, a.codeagence, a.designation, _periode, _rang, COALESCE(SUM(_salaire), 0) AS _salaire, COALESCE(SUM(_presence), 0) AS _presence, COALESCE(SUM(_conge), 0) AS _conge, COALESCE(SUM(_permission), 0) AS _permission, _is_total FROM grh_table_progression_all 
			INNER JOIN yvs_agences a ON _agence = a.id GROUP BY _agence, a.codeagence, a.designation, _periode, _rang, _is_total
		LOOP
			INSERT INTO grh_table_progression_all VALUES(_data_._agence, 0, _data_.codeagence, _data_.designation, _data_._periode, _data_._rang, COALESCE(_data_._salaire, 0), COALESCE(_data_._presence, 0), COALESCE(_data_._conge, 0), COALESCE(_data_._permission, 0), _data_._is_total, FALSE);
		END LOOP;
		FOR _data_ IN SELECT _periode, _rang, COALESCE(SUM(_salaire), 0) AS _salaire, COALESCE(SUM(_presence), 0) AS _presence, COALESCE(SUM(_conge), 0) AS _conge, COALESCE(SUM(_permission), 0) AS _permission FROM grh_table_progression_all WHERE _employe = 0 GROUP BY _periode, _rang
		LOOP
			INSERT INTO grh_table_progression_all VALUES(0, 0, 'TOTAUX', 'TOTAUX', _data_._periode, _data_._rang, COALESCE(_data_._salaire, 0), COALESCE(_data_._presence, 0), COALESCE(_data_._conge, 0), COALESCE(_data_._permission, 0), TRUE, TRUE);
		END LOOP;
	END IF;
	return QUERY SELECT * FROM grh_table_progression_all order by _is_footer, _code, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_progression_all(bigint, bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;
