-- DROP FUNCTION public.compta_et_dashboard(int8, int8, varchar, varchar, date, date, varchar, varchar);

CREATE OR REPLACE FUNCTION public.compta_et_dashboard(
	agence_ bigint, 
	societe_ bigint, 
	comptedebut_ character varying, 
	comptefin_ character varying, 
	date_debut_ date, 
	date_fin_ date, 
	type_ character varying, 
	periode_ character varying
)
RETURNS TABLE(
	numero character varying, 
	entete character varying, 
	valeur character varying, 
	montant double precision, 
	num_classe character varying, 
	is_head boolean, 
	is_produit boolean, 
	is_charge boolean, 
	is_total boolean, 
	rang integer
)
LANGUAGE plpgsql
AS $function$
DECLARE 
	comptes_ record;
	dates_ record;
	soldes_ record;
	
	jour_ character varying;
	valeur_ character varying;
	
	solde_ double precision := 0;
	debit_ double precision := 0;
	credit_ double precision := 0;
	
	societe_id_ bigint;
	hide_compte_ boolean := true;
	pdt_ boolean;
	chg_ boolean;
	compteur_ integer := 0;
BEGIN 	
	-- Suppression et création de la table temporaire
	DROP TABLE IF EXISTS table_dashboard;
	CREATE TEMP TABLE table_dashboard (
		num character varying, 
		titre character varying, 
		element character varying, 
		mtant double precision, 
		classe character varying, 
		head boolean, 
		produit boolean, 
		charge boolean, 
		total boolean, 
		_rang integer
	);

	-- Déterminer la société une seule fois
	IF societe_ IS NOT NULL AND societe_ > 0 THEN
		societe_id_ := societe_;
	ELSIF agence_ IS NOT NULL AND agence_ > 0 THEN
		SELECT a.societe INTO societe_id_ FROM yvs_agences a WHERE a.id = agence_;
	END IF;

	-- Récupération des comptes selon le type
	FOR comptes_ IN 
		SELECT DISTINCT 
			c.id, 
			c.num_compte as code, 
			c.intitule, 
			substring(c.num_compte, 1, 1) as classe,
			CASE 
				WHEN substring(c.num_compte, 1, 1) = '6' THEN true
				WHEN substring(c.num_compte, 1, 1) = '8' AND c.num_compte SIMILAR TO '8[13579]%' THEN true
				ELSE false
			END as is_charge,
			CASE 
				WHEN substring(c.num_compte, 1, 1) = '7' THEN true
				WHEN substring(c.num_compte, 1, 1) = '8' AND c.num_compte SIMILAR TO '8[02468]%' THEN true
				ELSE false
			END as is_produit
		FROM yvs_base_plan_comptable c 
		INNER JOIN yvs_base_nature_compte n ON n.id = c.nature_compte 
		INNER JOIN yvs_base_plan_comptable y ON y.compte_general = c.id 
		WHERE c.id IS NOT NULL
		  AND (societe_id_ IS NULL OR n.societe = societe_id_)
		  AND c.num_compte BETWEEN comptedebut_ AND comptefin_
		  AND (
			(type_ = 'R' AND c.num_compte SIMILAR TO '[678]%')
			OR (type_ = 'P' AND (c.num_compte LIKE '7%' OR c.num_compte SIMILAR TO '8[02468]%'))
			OR (type_ = 'C' AND (c.num_compte LIKE '6%' OR c.num_compte SIMILAR TO '8[13579]%'))
			OR (type_ = 'B' AND substring(c.num_compte, 1, 1) < '6')
			OR (type_ NOT IN ('R', 'P', 'C', 'B'))
		  )
		ORDER BY c.num_compte
	LOOP
		hide_compte_ := true;
		pdt_ := comptes_.is_produit;
		chg_ := comptes_.is_charge;
		
		IF periode_ = 'A' THEN 
			-- Opération sur l'année	
			SELECT debit, credit INTO soldes_ 
			FROM compta_et_debit_credit(agence_, societe_, comptes_.id, date_debut_, date_fin_, 0, 'C', true, '', true);
			
			debit_ := COALESCE(soldes_.debit, 0);
			credit_ := COALESCE(soldes_.credit, 0);
			
			-- Insertion débit du compte
			INSERT INTO table_dashboard VALUES (comptes_.code, 'DEBIT', comptes_.intitule, debit_, comptes_.classe, false, pdt_, chg_, false);

			-- Mise à jour ou insertion du total débit de la classe
			UPDATE table_dashboard 
			SET mtant = mtant + debit_ 
			WHERE classe = comptes_.classe AND titre = 'DEBIT' AND head = true AND num = 'TOTAL';
			
			IF NOT FOUND THEN
				INSERT INTO table_dashboard VALUES ('TOTAL', 'DEBIT', 'CLASSE ' || comptes_.classe, debit_, comptes_.classe, true, pdt_, chg_, false);
			END IF;
				
			-- Insertion crédit du compte
			INSERT INTO table_dashboard VALUES (comptes_.code, 'CREDIT', comptes_.intitule, -credit_, comptes_.classe, false, pdt_, chg_, false);

			-- Mise à jour ou insertion du total crédit de la classe
			UPDATE table_dashboard 
			SET mtant = mtant - credit_ 
			WHERE classe = comptes_.classe AND titre = 'CREDIT' AND head = true AND num = 'TOTAL';
			
			IF NOT FOUND THEN
				INSERT INTO table_dashboard VALUES ('TOTAL', 'CREDIT', 'CLASSE ' || comptes_.classe, -credit_, comptes_.classe, true, pdt_, chg_, false);
			END IF;
			
			IF debit_ <> 0 OR credit_ <> 0 THEN
				hide_compte_ := false;
			END IF;
		ELSE 
			-- Opération autre que sur l'année	
			compteur_ := 0;
			
			FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, periode_)
			LOOP
				jour_ := dates_.intitule;
				compteur_ := compteur_ + 1;

				-- Récupération du solde du compte
				solde_ := COALESCE(compta_et_solde(agence_, societe_, comptes_.id, dates_.date_debut, dates_.date_fin, 0, 'C', true, ''), 0);
				
				INSERT INTO table_dashboard VALUES (comptes_.code, jour_, comptes_.intitule, solde_, comptes_.classe, false, pdt_, chg_, false, compteur_);

				-- Mise à jour ou insertion du total de la classe pour cette période
				UPDATE table_dashboard 
				SET mtant = mtant + solde_ 
				WHERE classe = comptes_.classe AND titre = jour_ AND head = true AND num = 'TOTAL';
				
				IF NOT FOUND THEN
					INSERT INTO table_dashboard VALUES ('TOTAL', jour_, 'CLASSE ' || comptes_.classe, solde_, comptes_.classe, true, pdt_, chg_, false, compteur_);
				END IF;
				
				IF solde_ <> 0 THEN
					hide_compte_ := false;
				END IF;
			END LOOP;
		END IF;
		
		-- Suppression des comptes sans mouvement
		IF hide_compte_ THEN
			DELETE FROM table_dashboard WHERE num = comptes_.code;
		END IF;
	END LOOP;

	-- Récupération du total global des colonnes
	IF periode_ = 'A' THEN 
		-- Insertion des totaux globaux
		INSERT INTO table_dashboard (num, titre, element, mtant, classe, head, produit, charge, total, _rang)
		SELECT '', titre, '', SUM(mtant), 'Z', false, false, false, true, null
		FROM table_dashboard 
		WHERE head = false AND titre IN ('DEBIT', 'CREDIT')
		GROUP BY titre;
	ELSE 
		-- Insertion des totaux par période
		INSERT INTO table_dashboard (num, titre, element, mtant, classe, head, produit, charge, total, _rang)
		SELECT '', p.intitule, '', COALESCE(SUM(td.mtant), 0), 'Z', false, false, false, true, (row_number() OVER (ORDER BY p.intitule))::integer
		FROM decoupage_interval_date(date_debut_, date_fin_, periode_) p
		LEFT JOIN table_dashboard td ON td.titre = p.intitule AND td.head = false
		GROUP BY p.intitule;
	END IF;
	
	-- Suppression des classes sans montant
	DELETE FROM table_dashboard 
	WHERE classe IN (
		SELECT classe 
		FROM table_dashboard 
		GROUP BY classe 
		HAVING SUM(mtant) = 0
	);
	
	RETURN QUERY 
	SELECT * FROM table_dashboard 
	ORDER BY classe, num, _rang;
END;
$function$;

