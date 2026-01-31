-- DROP FUNCTION public.compta_et_grand_livre(int8, int8, varchar, date, date, int8, varchar, bool, bool, varchar);

CREATE OR REPLACE FUNCTION public.compta_et_grand_livre(
	societe_ bigint, 
	agence_ bigint, 
	comptes_ character varying, 
	date_debut_ date, 
	date_fin_ date, 
	journal_ bigint, 
	type_ character varying, 
	lettrer_ boolean, 
	cumule_ boolean, 
	nature_ character varying
)
RETURNS TABLE(
	id bigint, 
	code character varying, 
	intitule character varying, 
	compte character varying, 
	designation character varying, 
	numero character varying, 
	date_piece date, 
	jour integer, 
	libelle character varying, 
	lettrage character varying, 
	journal character varying, 
	debit double precision, 
	credit double precision, 
	compte_tiers bigint, 
	table_tiers character varying, 
	solde boolean
)
LANGUAGE plpgsql
AS $function$
DECLARE 
	data_ record;
	valeurs_ record;
	
	credit_si_ double precision := 0;
	debit_si_ double precision := 0;
	solde_ double precision := 0;
	
	last_code_ character varying := '';
	
	comptes_array_ text[];
	
	date_initial_ DATE;
BEGIN 	
	-- Suppression de la table temporaire si elle existe
	DROP TABLE IF EXISTS table_et_grand_livre;
	
	-- Création de la table temporaire
	CREATE TEMP TABLE table_et_grand_livre (
		_id bigint, 
		_code character varying, 
		_intitule character varying, 
		_compte character varying, 
		_designation character varying, 
		_numero character varying, 
		_date_piece date, 
		_jour integer, 
		_libelle character varying, 
		_lettrage character varying, 
		_journal character varying, 
		_debit double precision, 
		_credit double precision, 
		_compte_tiers bigint, 
		_table_tiers character varying, 
		_solde boolean
	);
	
	-- Récupérer la date de début d'exercice une seule fois
	SELECT e.date_debut INTO date_initial_ 
	FROM yvs_base_exercice e
	WHERE date_debut_ BETWEEN e.date_debut AND e.date_fin
	LIMIT 1;
	
	date_initial_ := COALESCE(date_initial_, date_fin_);

	-- Convertir la liste de comptes en tableau
	IF COALESCE(comptes_, '') NOT IN ('', ' ', '0') THEN
		SELECT array_agg(TRIM(head)) INTO comptes_array_
		FROM regexp_split_to_table(comptes_, ',') head 
		WHERE CHAR_LENGTH(TRIM(head)) > 0;
	END IF;

	-- ========== TYPE TIERS ==========
	IF type_ = 'T' THEN
		FOR data_ IN 
			SELECT 
				y.compte_tiers AS id, 
				name_tiers(y.compte_tiers, y.table_tiers, 'R') AS code,
				name_tiers(y.compte_tiers, y.table_tiers, 'N') AS intitule,
				c.num_compte AS compte, 
				c.intitule AS designation,
				y.num_piece, 
				p.date_piece, 
				y.jour, 
				UPPER(y.libelle) AS libelle, 
				y.lettrage, 
				j.code_journal AS journal, 
				y.debit, 
				y.credit, 
				y.compte_tiers, 
				y.table_tiers, 
				y.report
			FROM yvs_compta_pieces_comptable p 
			INNER JOIN yvs_compta_content_journal y ON p.id = y.piece 
			INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
			INNER JOIN yvs_compta_journaux j ON p.journal = j.id 
			INNER JOIN yvs_agences a ON j.agence = a.id
			WHERE p.date_piece BETWEEN date_debut_ AND date_fin_
			  AND COALESCE(y.compte_tiers, 0) > 0
			  AND (journal_ IS NULL OR journal_ <= 0 OR j.id = journal_)
			  AND (agence_ IS NULL OR agence_ <= 0 OR a.id = agence_)
			  AND (societe_ IS NULL OR societe_ <= 0 OR a.societe = societe_)
			  AND (lettrer_ IS NULL 
				   OR (lettrer_ AND y.lettrage IS NOT NULL AND TRIM(y.lettrage) <> '')
				   OR (NOT lettrer_ AND (y.lettrage IS NULL OR TRIM(y.lettrage) = '')))
			  AND (nature_ IS NULL OR nature_ NOT IN ('T','R','S','E','C','F','CF')
				   OR (nature_ IN ('T','R','S') AND y.table_tiers = 'TIERS')
				   OR (nature_ = 'E' AND y.table_tiers = 'EMPLOYE')
				   OR (nature_ = 'C' AND y.table_tiers = 'CLIENT')
				   OR (nature_ = 'F' AND y.table_tiers = 'FOURNISSEUR')
				   OR (nature_ = 'CF' AND y.table_tiers IN ('CLIENT','FOURNISSEUR')))
			  AND (comptes_array_ IS NULL OR name_tiers(y.compte_tiers, y.table_tiers, 'R') = ANY(comptes_array_))
			ORDER BY name_tiers(y.compte_tiers, y.table_tiers, 'R'), p.date_piece, y.report DESC, p.num_piece
		LOOP
			IF COALESCE(data_.id, 0) > 0 THEN
				IF last_code_ <> data_.code AND date_initial_ < date_debut_ THEN 
					SELECT * INTO valeurs_ FROM compta_et_debit_credit_all(
						agence_, societe_, data_.id, date_initial_, 
						(date_debut_ - INTERVAL '1 day')::date, journal_, 
						type_, false, data_.table_tiers, true
					);
					debit_si_ := COALESCE(valeurs_.debit, 0);
					credit_si_ := COALESCE(valeurs_.credit, 0);
					solde_ := debit_si_ - credit_si_;
					IF solde_ >= 0 THEN debit_si_ := solde_; credit_si_ := 0;
					ELSE debit_si_ := 0; credit_si_ := -solde_; END IF;
					last_code_ := data_.code;
					IF solde_ <> 0 THEN
						INSERT INTO table_et_grand_livre VALUES(
							data_.id, data_.code, data_.intitule, 'S.I', data_.designation, 'S.I', 
							(date_debut_ - INTERVAL '1 day')::date, 
							EXTRACT(DAY FROM (date_debut_ - INTERVAL '1 day'))::integer, 
							'SOLDE INITIAL', '', data_.journal, debit_si_, credit_si_, 
							data_.compte_tiers, data_.table_tiers, true
						);
					END IF;				
				END IF;
				INSERT INTO table_et_grand_livre VALUES(
					data_.id, data_.code, data_.intitule, data_.compte, data_.designation, 
					data_.num_piece, data_.date_piece, data_.jour, data_.libelle, 
					data_.lettrage, data_.journal, data_.debit, data_.credit, 
					data_.compte_tiers, data_.table_tiers, COALESCE(data_.report, false)
				);
			END IF;
		END LOOP;

	-- ========== TYPE COMPTE ==========
	ELSIF type_ = 'C' THEN
		FOR data_ IN 
			SELECT 
				c.id, 
				c.num_compte AS code, 
				c.intitule,
				null::varchar AS compte, 
				null::varchar AS designation,
				y.num_piece, 
				p.date_piece, 
				y.jour, 
				UPPER(y.libelle) AS libelle,
				y.lettrage, 
				j.code_journal AS journal,
				y.debit, 
				y.credit, 
				y.compte_tiers, 
				y.table_tiers, 
				y.report
			FROM yvs_compta_pieces_comptable p 
			INNER JOIN yvs_compta_content_journal y ON p.id = y.piece 
			INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
			INNER JOIN yvs_compta_journaux j ON p.journal = j.id 
			INNER JOIN yvs_agences a ON j.agence = a.id
			WHERE p.date_piece BETWEEN date_debut_ AND date_fin_
			  AND (journal_ IS NULL OR journal_ <= 0 OR j.id = journal_)
			  AND (agence_ IS NULL OR agence_ <= 0 OR a.id = agence_)
			  AND (societe_ IS NULL OR societe_ <= 0 OR a.societe = societe_)
			  AND (comptes_array_ IS NULL OR c.num_compte = ANY(comptes_array_))
			  AND (lettrer_ IS NULL 
				   OR (lettrer_ AND y.lettrage IS NOT NULL AND TRIM(y.lettrage) <> '')
				   OR (NOT lettrer_ AND (y.lettrage IS NULL OR TRIM(y.lettrage) = '')))
			ORDER BY c.num_compte, p.date_piece, y.report DESC, p.num_piece
		LOOP
			IF COALESCE(data_.id, 0) > 0 THEN
				IF last_code_ <> data_.code AND date_initial_ < date_debut_ THEN 
					SELECT * INTO valeurs_ FROM compta_et_debit_credit_all(
						agence_, societe_, data_.id, date_initial_, 
						(date_debut_ - INTERVAL '1 day')::date, journal_, 
						type_, false, data_.table_tiers, true
					);
					debit_si_ := COALESCE(valeurs_.debit, 0);
					credit_si_ := COALESCE(valeurs_.credit, 0);
					solde_ := debit_si_ - credit_si_;
					IF solde_ >= 0 THEN debit_si_ := solde_; credit_si_ := 0;
					ELSE debit_si_ := 0; credit_si_ := -solde_; END IF;
					last_code_ := data_.code;
					IF solde_ <> 0 THEN
						INSERT INTO table_et_grand_livre VALUES(
							data_.id, data_.code, data_.intitule, 'S.I', data_.designation, 'S.I', 
							(date_debut_ - INTERVAL '1 day')::date, 
							EXTRACT(DAY FROM (date_debut_ - INTERVAL '1 day'))::integer, 
							'SOLDE INITIAL', '', data_.journal, debit_si_, credit_si_, 
							data_.compte_tiers, data_.table_tiers, true
						);
					END IF;				
				END IF;
				INSERT INTO table_et_grand_livre VALUES(
					data_.id, data_.code, data_.intitule, data_.compte, data_.designation, 
					data_.num_piece, data_.date_piece, data_.jour, data_.libelle, 
					data_.lettrage, data_.journal, data_.debit, data_.credit, 
					data_.compte_tiers, data_.table_tiers, COALESCE(data_.report, false)
				);
			END IF;
		END LOOP;

	-- ========== TYPE ANALYTIQUE CUMULE ==========
	ELSIF cumule_ THEN
		FOR data_ IN 
			SELECT 
				n.id, 
				n.code_ref AS code, 
				n.designation AS intitule,
				c.num_compte AS compte, 
				c.intitule AS designation,
				null::varchar AS num_piece, 
				null::date AS date_piece, 
				null::integer AS jour, 
				null::varchar AS libelle,
				null::varchar AS lettrage, 
				''::varchar AS journal,
				SUM(l.debit) AS debit, 
				SUM(l.credit) AS credit,
				null::bigint AS compte_tiers, 
				'T'::varchar AS table_tiers, 
				false AS report
			FROM yvs_compta_pieces_comptable p 
			INNER JOIN yvs_compta_content_journal y ON p.id = y.piece 
			INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
			INNER JOIN yvs_compta_journaux j ON p.journal = j.id 
			INNER JOIN yvs_agences a ON j.agence = a.id
			INNER JOIN yvs_compta_content_analytique l ON y.id = l.contenu 
			INNER JOIN yvs_compta_centre_analytique n ON n.id = l.centre
			WHERE p.date_piece BETWEEN date_debut_ AND date_fin_
			  AND (journal_ IS NULL OR journal_ <= 0 OR j.id = journal_)
			  AND (agence_ IS NULL OR agence_ <= 0 OR a.id = agence_)
			  AND (societe_ IS NULL OR societe_ <= 0 OR a.societe = societe_)
			  AND (comptes_array_ IS NULL OR n.code_ref = ANY(comptes_array_))
			  AND (lettrer_ IS NULL 
				   OR (lettrer_ AND y.lettrage IS NOT NULL AND TRIM(y.lettrage) <> '')
				   OR (NOT lettrer_ AND (y.lettrage IS NULL OR TRIM(y.lettrage) = '')))
			GROUP BY n.id, n.code_ref, n.designation, c.num_compte, c.intitule
			ORDER BY n.code_ref, c.num_compte
		LOOP
			IF COALESCE(data_.id, 0) > 0 THEN
				IF last_code_ <> data_.code AND date_initial_ < date_debut_ THEN 
					SELECT * INTO valeurs_ FROM compta_et_debit_credit_all(
						agence_, societe_, data_.id, date_initial_, 
						(date_debut_ - INTERVAL '1 day')::date, journal_, 
						type_, false, data_.table_tiers, true
					);
					debit_si_ := COALESCE(valeurs_.debit, 0);
					credit_si_ := COALESCE(valeurs_.credit, 0);
					solde_ := debit_si_ - credit_si_;
					IF solde_ >= 0 THEN debit_si_ := solde_; credit_si_ := 0;
					ELSE debit_si_ := 0; credit_si_ := -solde_; END IF;
					last_code_ := data_.code;
					IF solde_ <> 0 THEN
						INSERT INTO table_et_grand_livre VALUES(
							data_.id, data_.code, data_.intitule, 'S.I', data_.designation, 'S.I', 
							(date_debut_ - INTERVAL '1 day')::date, 
							EXTRACT(DAY FROM (date_debut_ - INTERVAL '1 day'))::integer, 
							'SOLDE INITIAL', '', '', debit_si_, credit_si_, NULL, NULL, true
						);
					END IF;				
				END IF;
				INSERT INTO table_et_grand_livre VALUES(
					data_.id, data_.code, data_.intitule, data_.compte, data_.designation, 
					NULL, NULL, NULL, NULL, NULL, '', 
					data_.debit, data_.credit, NULL, NULL, false
				);
			END IF;
		END LOOP;

	-- ========== TYPE ANALYTIQUE DETAILLE ==========
	ELSE
		FOR data_ IN 
			SELECT 
				n.id, 
				n.code_ref AS code, 
				n.designation AS intitule,
				c.num_compte AS compte, 
				c.intitule AS designation,
				y.num_piece, 
				p.date_piece, 
				y.jour, 
				UPPER(y.libelle) AS libelle,
				y.lettrage, 
				j.code_journal AS journal,
				l.debit, 
				l.credit, 
				y.compte_tiers, 
				y.table_tiers, 
				y.report
			FROM yvs_compta_pieces_comptable p 
			INNER JOIN yvs_compta_content_journal y ON p.id = y.piece 
			INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
			INNER JOIN yvs_compta_journaux j ON p.journal = j.id 
			INNER JOIN yvs_agences a ON j.agence = a.id
			INNER JOIN yvs_compta_content_analytique l ON y.id = l.contenu 
			INNER JOIN yvs_compta_centre_analytique n ON n.id = l.centre
			WHERE p.date_piece BETWEEN date_debut_ AND date_fin_
			  AND (journal_ IS NULL OR journal_ <= 0 OR j.id = journal_)
			  AND (agence_ IS NULL OR agence_ <= 0 OR a.id = agence_)
			  AND (societe_ IS NULL OR societe_ <= 0 OR a.societe = societe_)
			  AND (comptes_array_ IS NULL OR n.code_ref = ANY(comptes_array_))
			  AND (lettrer_ IS NULL 
				   OR (lettrer_ AND y.lettrage IS NOT NULL AND TRIM(y.lettrage) <> '')
				   OR (NOT lettrer_ AND (y.lettrage IS NULL OR TRIM(y.lettrage) = '')))
			ORDER BY n.code_ref, p.date_piece, y.report DESC, p.num_piece
		LOOP
			IF COALESCE(data_.id, 0) > 0 THEN
				IF last_code_ <> data_.code AND date_initial_ < date_debut_ THEN 
					SELECT * INTO valeurs_ FROM compta_et_debit_credit_all(
						agence_, societe_, data_.id, date_initial_, 
						(date_debut_ - INTERVAL '1 day')::date, journal_, 
						type_, false, data_.table_tiers, true
					);
					debit_si_ := COALESCE(valeurs_.debit, 0);
					credit_si_ := COALESCE(valeurs_.credit, 0);
					solde_ := debit_si_ - credit_si_;
					IF solde_ >= 0 THEN debit_si_ := solde_; credit_si_ := 0;
					ELSE debit_si_ := 0; credit_si_ := -solde_; END IF;
					last_code_ := data_.code;
					IF solde_ <> 0 THEN
						INSERT INTO table_et_grand_livre VALUES(
							data_.id, data_.code, data_.intitule, 'S.I', data_.designation, 'S.I', 
							(date_debut_ - INTERVAL '1 day')::date, 
							EXTRACT(DAY FROM (date_debut_ - INTERVAL '1 day'))::integer, 
							'SOLDE INITIAL', '', data_.journal, debit_si_, credit_si_, 
							data_.compte_tiers, data_.table_tiers, true
						);
					END IF;				
				END IF;
				INSERT INTO table_et_grand_livre VALUES(
					data_.id, data_.code, data_.intitule, data_.compte, data_.designation, 
					data_.num_piece, data_.date_piece, data_.jour, data_.libelle, 
					data_.lettrage, data_.journal, data_.debit, data_.credit, 
					data_.compte_tiers, data_.table_tiers, COALESCE(data_.report, false)
				);
			END IF;
		END LOOP;
	END IF;
	
	RETURN QUERY 
	SELECT * FROM table_et_grand_livre 
	ORDER BY _code, _solde DESC, _date_piece, _numero;
END;
$function$;
