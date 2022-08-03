-- Function: grh_conges_employes(bigint, bigint, bigint, bigint, date, date)

-- DROP FUNCTION grh_conges_employes(bigint, bigint, bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION grh_conges_employes(IN societe_ bigint, IN agence_ bigint, IN service_ bigint, IN employe_ bigint, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(agence bigint, employe bigint, code character varying, nom character varying, valeur double precision, element character varying) AS
$BODY$
DECLARE 
	_employe RECORD;  
	_autres RECORD;   
	_date_debut DATE DEFAULT date_debut_;
	_query CHARACTER VARYING DEFAULT '';
	_interval INTERVAL;
	_nbre_annee INTEGER DEFAULT 0;
	_anciennete INTEGER DEFAULT 0;
	_cumul_conge_permis INTEGER DEFAULT 0;
	_sommes INTEGER DEFAULT 0;
	_age INTEGER DEFAULT 0;
	_garde INTEGER DEFAULT 0;
	_valeur DOUBLE PRECISION DEFAULT 0;
BEGIN
	CREATE TEMP TABLE IF NOT EXISTS table_conges_employes(_agence_ BIGINT, _employe_ BIGINT, _code_ CHARACTER VARYING, _nom_ CHARACTER VARYING, _valeur_ DOUBLE PRECISION, _element_ CHARACTER VARYING);
	DELETE FROM table_conges_employes;
	IF(employe_ IS NOT NULL AND employe_ > 0)THEN
		_query = 'SELECT e.id, e.matricule, e.civilite, e.nom, e.prenom, e.date_embauche, e.agence, c.conge_acquis, c.source_first_conge, c.date_first_conge 
			FROM yvs_grh_employes e INNER JOIN yvs_grh_contrat_emps c ON c.employe = e.id WHERE e.id = '||employe_;
	ELSIF(service_ IS NOT NULL AND service_ > 0)THEN
		_query = 'SELECT e.id, e.matricule, e.civilite, e.nom, e.prenom, e.date_embauche, e.agence, c.conge_acquis, c.source_first_conge, c.date_first_conge 
			FROM yvs_grh_employes e INNER JOIN yvs_grh_contrat_emps c ON c.employe = e.id INNER JOIN yvs_grh_poste_de_travail p ON p.id=e.poste_actif WHERE p.departement = '||service_||' ORDER BY e.nom';
	ELSIF(agence_ IS NOT NULL AND agence_ > 0)THEN
		_query = 'SELECT e.id, e.matricule, e.civilite, e.nom, e.prenom, e.date_embauche, e.agence, c.conge_acquis, c.source_first_conge, c.date_first_conge 
			FROM yvs_grh_employes e INNER JOIN yvs_grh_contrat_emps c ON c.employe = e.id WHERE e.agence = '||agence_||' ORDER BY e.nom';
	ELSE
		_query = 'SELECT e.id, e.matricule, e.civilite, e.nom, e.prenom, e.date_embauche, e.agence, c.conge_acquis, c.source_first_conge, c.date_first_conge 
			FROM yvs_grh_employes e INNER JOIN yvs_grh_contrat_emps c ON c.employe = e.id INNER JOIN yvs_agence a ON a.id = e.agence WHERE a.societe = '||societe_||' ORDER BY e.nom';
	END IF;
	IF(_query IS NOT NULL AND _query != '')THEN
		-- cumul de congé permis
		SELECT INTO _cumul_conge_permis  (duree_cumule_conge * 12) FROM yvs_parametre_grh  WHERE societe = societe_ limit 1; 
		_cumul_conge_permis=COALESCE(_cumul_conge_permis,0);
		-- nombre de mois entre les deux dates
		_interval = age(date_fin_, date_debut_);
		-- determination de la durée
		_nbre_annee = (SELECT (12* EXTRACT(YEAR FROM _interval)::INTEGER) + (EXTRACT(MONTH FROM _interval)::INTEGER));
		IF (_nbre_annee IS NOT NULL AND _cumul_conge_permis IS NOT NULL) THEN
			IF(_nbre_annee > _cumul_conge_permis) THEN
				_nbre_annee = _cumul_conge_permis;
			END IF;
		END IF;
		FOR _employe IN EXECUTE _query 
			LOOP
			_sommes = 0;
			-- DETERMINATION DU CONGE PRINCiPAL DÛ
			_valeur = (_employe.conge_acquis * _nbre_annee);
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'CPD');
	
			-- DETERMINATION DU CONGE SUPPLEMENTAIRE DÛ
			-- nombre de mois entre les deux dates
			_interval = age(date_fin_, _employe.date_embauche);
			-- determination de l'anciennete
			_anciennete = (SELECT (12* EXTRACT(YEAR FROM _interval)::INTEGER) + (EXTRACT(MONTH FROM _interval)::INTEGER)) /12;
			-- recuperation des parametre de conge
			SELECT INTO _autres id, anciennete, nb_jour_supp FROM yvs_grh_majoration_conge m WHERE m.nature = 'Anciennete' AND m.societe = societe_;
			IF(_autres.id IS NOT NULL)THEN
				_sommes = (SELECT cast((_anciennete / _autres.anciennete) AS integer)) * _autres.nb_jour_supp;  --gagne 2 jour par periode de 5ans
			END IF;
			IF(_sommes IS NULL)THEN
				_sommes = 0;
			END IF;
			IF(_employe.civilite IS NOT NULL AND _employe.civilite != 'M')THEN
				-- recherche des personnes en charge d'un employe et calcul du total de jour
				FOR _autres IN SELECT p.id, p.date_naissance FROM yvs_grh_personne_en_charge p WHERE p.employe = _employe.id  AND p.epouse = false order by p.id asc
				LOOP	
					-- age de l'enfant
					_age = (SELECT EXTRACT(year FROM current_date)) - (SELECT EXTRACT(year FROM _autres.date_naissance));	
					-- determination du nombre de jours de conge a prendre si on est dans le cas d'une femme mere
					SELECT INTO _garde COALESCE(i.nb_jour, 0) FROM yvs_grh_interval_majoration i inner join yvs_grh_majoration_conge m on i.majoration_conge = m.id 
						WHERE m.nature = 'Dame mere' AND m.societe = employe_.societe AND (_age between i.valeur_maximal AND i.valeur_minimal);
					_sommes = _sommes + _garde;
				END LOOP;
			END IF;
			_interval = age(date_fin_, date_debut_);
			_nbre_annee = (((SELECT (12* EXTRACT(YEAR FROM _interval)::INTEGER) + (EXTRACT(MONTH FROM _interval)::INTEGER)) /12)::INTEGER)+1;
			_valeur = abs(_sommes * _nbre_annee);
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'CSD');
			
			-- DETERMINATION DU CONGE PRIS
			SELECT INTO _valeur COALESCE(sum((c.date_fin - c.date_debut)+1), 0) FROM yvs_grh_conge_emps c WHERE c.employe = _employe.id AND c.date_debut >= date_debut_
				AND c.date_fin <= date_fin_ AND (((c.nature = 'C') OR (c.nature = 'P' AND duree_permission = 'L')) AND c.effet = 'CONGE ANNUEL') 
				AND (c.statut = 'C' OR c.statut = 'V') group by c.employe;
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'CNP');
			
			-- DETERMINATION DE PERMISSION DU
			SELECT INTO _valeur COALESCE(y.total_conge_permis, 0) FROM yvs_parametre_grh y WHERE y.societe = societe_;
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'PLD');

			IF(_employe.source_first_conge='DEX') THEN
				date_debut_ = _date_debut;
			ELSIF (_employe.source_first_conge='DE') THEN
				date_debut_ = _employe.date_embauche;
			ELSIF(_employe.source_first_conge='DF') THEN
				date_debut_ = _employe.date_first_conge;
			END IF;
			
			-- DETERMINATION DE PERMISSION LONG PRIS SPECIAL
			SELECT INTO _valeur COALESCE(sum((c.date_fin - c.date_debut)+1), 0) FROM yvs_grh_conge_emps c WHERE c.employe = _employe.id AND c.date_debut >= date_debut_
				AND c.date_fin <= date_fin_ AND (c.nature = 'P' AND duree_permission = 'L' AND c.effet = 'SPECIALE') 
				AND (c.statut = 'C' OR c.statut = 'V') group by c.employe;
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'PLS');
			
			-- DETERMINATION DE PERMISSION LONG PRIS ANNUEL
			SELECT INTO _valeur COALESCE(sum((c.date_fin - c.date_debut)+1), 0) FROM yvs_grh_conge_emps c WHERE c.employe = _employe.id AND c.date_debut >= date_debut_
				AND c.date_fin <= date_fin_ AND (c.nature = 'P' AND duree_permission = 'L' AND c.effet = 'CONGE ANNUEL') 
				AND (c.statut = 'C' OR c.statut = 'V') group by c.employe;
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'PLN');
			
			-- DETERMINATION DE PERMISSION LONG PRIS AUTORISE
			SELECT INTO _valeur COALESCE(sum((c.date_fin - c.date_debut)+1), 0) FROM yvs_grh_conge_emps c WHERE c.employe = _employe.id AND c.date_debut >= date_debut_
				AND c.date_fin <= date_fin_ AND (c.nature = 'P' AND duree_permission = 'L' AND c.effet = 'AUTORISE') 
				AND (c.statut = 'C' OR c.statut = 'V') group by c.employe;
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'PLA');
			
			-- DETERMINATION DE PERMISSION LONG PRIS SALAIRE
			SELECT INTO _valeur COALESCE(sum((c.date_fin - c.date_debut)+1), 0) FROM yvs_grh_conge_emps c WHERE c.employe = _employe.id AND c.date_debut >= date_debut_
				AND c.date_fin <= date_fin_ AND (c.nature = 'P' AND duree_permission = 'L' AND c.effet = 'SALAIRE') 
				AND (c.statut = 'C' OR c.statut = 'V') group by c.employe;
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'PLSL');
			
			-- DETERMINATION DE PERMISSION COURT PRIS SALAIRE
			SELECT INTO _valeur COALESCE(sum((EXTRACT (HOUR FROM (c.heure_fin - c.heure_debut))) + (EXTRACT (MINUTE FROM (c.heure_fin - c.heure_debut)) / 60)+1), 0) FROM yvs_grh_conge_emps c WHERE c.employe = _employe.id AND c.date_debut >= date_debut_
				AND c.date_fin <= date_fin_ AND (c.nature = 'P' AND duree_permission = 'C' AND c.effet = 'SALAIRE') 
				AND (c.statut = 'C' OR c.statut = 'V') group by c.employe;
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'PCSL');
			
			-- DETERMINATION DE PERMISSION COURT PRIS SPECIAL
			SELECT INTO _valeur COALESCE(sum((EXTRACT (HOUR FROM (c.heure_fin - c.heure_debut))) + (EXTRACT (MINUTE FROM (c.heure_fin - c.heure_debut)) / 60)+1), 0) FROM yvs_grh_conge_emps c WHERE c.employe = _employe.id AND c.date_debut >= date_debut_
				AND c.date_fin <= date_fin_ AND (c.nature = 'P' AND duree_permission = 'C' AND c.effet = 'SPECIALE') 
				AND (c.statut = 'C' OR c.statut = 'V') group by c.employe;
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'PCS');
			
			-- DETERMINATION DE PERMISSION COURT PRIS ANNUEL
			SELECT INTO _valeur COALESCE(sum((EXTRACT (HOUR FROM (c.heure_fin - c.heure_debut))) + (EXTRACT (MINUTE FROM (c.heure_fin - c.heure_debut)) / 60)+1), 0) FROM yvs_grh_conge_emps c WHERE c.employe = _employe.id AND c.date_debut >= date_debut_
				AND c.date_fin <= date_fin_ AND (c.nature = 'P' AND duree_permission = 'C' AND c.effet = 'CONGE ANNUEL') 
				AND (c.statut = 'C' OR c.statut = 'V') group by c.employe;
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'PCN');
			
			-- DETERMINATION DE PERMISSION COURT PRIS AUTORISE
			SELECT INTO _valeur COALESCE(sum((EXTRACT (HOUR FROM (c.heure_fin - c.heure_debut))) + (EXTRACT (MINUTE FROM (c.heure_fin - c.heure_debut)) / 60)+1), 0) FROM yvs_grh_conge_emps c WHERE c.employe = _employe.id AND c.date_debut >= date_debut_
				AND c.date_fin <= date_fin_ AND (c.nature = 'P' AND duree_permission = 'C' AND c.effet = 'AUTORISE') 
				AND (c.statut = 'C' OR c.statut = 'V') group by c.employe;
			INSERT INTO table_conges_employes VALUES (_employe.agence, _employe.id, _employe.matricule, TRIM(CONCAT(_employe.prenom, ' ',_employe.nom)), COALESCE(_valeur, 0), 'PCA');
		END LOOP;
	END IF;
	RETURN QUERY SELECT * FROM table_conges_employes ORDER BY _nom_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_conges_employes(bigint, bigint, bigint, bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION grh_conges_employes(bigint, bigint, bigint, bigint, date, date) IS 'retourne les details de congé d''un employé sur un interval de date';
