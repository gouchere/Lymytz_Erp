-- Function: compta_et_grand_livre(bigint, bigint, character varying, date, date, bigint, character varying, boolean, boolean, character varying)
-- DROP FUNCTION compta_et_grand_livre(bigint, bigint, character varying, date, date, bigint, character varying, boolean, boolean, character varying);
CREATE OR REPLACE FUNCTION compta_et_grand_livre(IN societe_ bigint, IN agence_ bigint, IN comptes_ character varying, IN date_debut_ date, IN date_fin_ date, IN journal_ bigint, IN type_ character varying, IN lettrer_ boolean, IN cumule_ boolean, IN nature_ character varying)
  RETURNS TABLE(id bigint, code character varying, intitule character varying, compte character varying, designation character varying, numero character varying, date_piece date, jour integer, libelle character varying, lettrage character varying, journal character varying, debit double precision, credit double precision, compte_tiers bigint, table_tiers character varying, solde boolean) AS
$BODY$
declare 
	data_ record;
	valeurs_ record;
	
	credit_si_ double precision default 0;
	debit_si_ double precision default 0;
	solde_ double precision default 0;
	
	query_ character varying default '';
	last_code_ character varying default '';
	
	ids_ character varying default '''0''';
	valeur_ character varying default '0';
	
	date_initial_ DATE DEFAULT date_fin_;
begin 	
	DROP TABLE IF EXISTS table_et_grand_livre;
	CREATE TEMP TABLE IF NOT EXISTS table_et_grand_livre(_id bigint, _code character varying, _intitule character varying, _compte character varying, _designation character varying, _numero character varying, _date_piece date, _jour integer, _libelle character varying, _lettrage character varying, _journal character varying, _debit double precision, _credit double precision, _compte_tiers bigint, _table_tiers character varying, _solde boolean); 
	DELETE FROM table_et_grand_livre;
	IF(type_ = 'T')THEN
		query_ = 'SELECT p.num_piece, y.jour, y.num_piece, y.num_ref, y.libelle, y.lettrage, y.debit, y.credit, y.compte_tiers as id, c.num_compte as compte, c.intitule as designation, p.date_piece, j.code_journal as journal, name_tiers(y.compte_tiers, y.table_tiers, ''R'') as code, name_tiers(y.compte_tiers, y.table_tiers, ''N'') as intitule, y.compte_tiers, y.table_tiers, y.report
				FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id';
	ELSIF(type_ = 'C') THEN
		query_ = 'SELECT c.id, c.num_compte as code, c.intitule, null as compte, null as designation, y.num_piece, p.date_piece, y.jour, UPPER(y.libelle) AS libelle, y.lettrage, j.code_journal as journal, y.debit, y.credit, y.compte_tiers, y.table_tiers, y.report
				FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id';
			
	ELSE
		IF(cumule_) THEN
			query_ = 'SELECT SUM(l.debit) debit, SUM(l.credit) credit, n.id as id, c.num_compte as compte, c.intitule as designation, n.code_ref as code, n.designation as intitule, ''T''::character varying AS table_tiers, null AS type_report
					FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
					INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id
					INNER JOIN yvs_compta_content_analytique l ON y.id = l.contenu INNER JOIN yvs_compta_centre_analytique n ON n.id = l.centre';
		ELSE
			query_ = 'SELECT p.num_piece, y.jour, y.num_piece, y.num_ref, y.libelle, y.lettrage, l.debit, l.credit, n.id, c.num_compte as compte, c.intitule as designation, p.date_piece, j.code_journal as journal, n.code_ref as code, n.designation as intitule, y.compte_tiers, y.table_tiers, y.report
					FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
					INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id
					INNER JOIN yvs_compta_content_analytique l ON y.id = l.contenu INNER JOIN yvs_compta_centre_analytique n ON n.id = l.centre';
		END IF;
	END IF;
	query_ = query_ || ' WHERE p.date_piece BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	if(type_ = 'T')then
		query_ = query_ || ' AND COALESCE(y.compte_tiers, 0) > 0';
		if (nature_ = 'T' or nature_ = 'R' or nature_ = 'S') then
			query_ = query_ || ' AND y.table_tiers = ''TIERS''';
		elsif (nature_ = 'E') then
			query_ = query_ || ' AND y.table_tiers = ''EMPLOYE''';
		elsif (nature_ = 'C') then
			query_ = query_ || ' AND y.table_tiers = ''CLIENT''';
		elsif (nature_ = 'F')then
			query_ = query_ || ' AND y.table_tiers = ''FOURNISSEUR''';
		elsif(nature_ = 'CF') then
			query_ = query_ || ' AND y.table_tiers IN (''CLIENT'', ''FOURNISSEUR'')';
		end if;
	end if;
	IF(journal_ IS NOT NULL AND journal_ > 0)THEN
		query_ = query_ || ' AND j.id = '||journal_||'';
	END IF;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_||'';
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_||'';
	END IF;
	IF(COALESCE(comptes_, '') NOT IN ('', ' ', '0'))THEN
		FOR valeur_ IN SELECT head FROM regexp_split_to_table(comptes_,',') head WHERE CHAR_LENGTH(head) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(valeur_);
		END LOOP;
		IF(type_ = 'T')THEN
			query_ = query_ || ' AND name_tiers(y.compte_tiers, y.table_tiers, ''R'') IN ('||ids_||')';		
		ELSIF(type_ = 'C') THEN
			query_ = query_ || ' AND c.num_compte IN ('||ids_||')';	
		ELSE
			query_ = query_ || ' AND n.code_ref IN ('||ids_||')';	
		END IF;
	END IF;
	IF(lettrer_ IS NOT NULL)THEN
		IF(lettrer_)THEN
			query_ = query_ || ' AND (y.lettrage IS NOT NULL AND CHAR_LENGTH(TRIM(y.lettrage)) > 0)';
		ELSE
			query_ = query_ || ' AND (y.lettrage IS NULL OR CHAR_LENGTH(TRIM(y.lettrage)) < 1)';
		END IF;
	END IF;
	IF(type_ = 'T')THEN
		query_ = query_ || ' ORDER BY name_tiers(y.compte_tiers, y.table_tiers, ''R''), p.date_piece, y.report DESC, p.num_piece';
	ELSIF(type_ = 'C')THEN
		query_ = query_ || ' ORDER BY c.num_compte, p.date_piece, y.report DESC, p.num_piece';
	ELSE
		IF(cumule_) THEN			
			query_ = query_ || ' GROUP BY n.id, n.code_ref, n.designation, c.num_compte, c.intitule';	
			query_ = query_ || ' ORDER BY n.code_ref,  c.num_compte, c.intitule';
		ELSE
			query_ = query_ || ' ORDER BY n.code_ref, p.date_piece, y.report DESC, p.num_piece';
		END IF;			
	END IF;

	SELECT INTO date_initial_ date_debut FROM yvs_base_exercice WHERE date_debut_ BETWEEN date_debut AND date_fin;
	IF(query_ IS NOT NULL AND query_ != '')THEN
		FOR data_ IN EXECUTE query_
		LOOP
			IF(COALESCE(data_.id, 0) > 0)THEN
				IF(last_code_ != data_.code AND date_initial_ < date_debut_)THEN 
					SELECT INTO valeurs_ * FROM compta_et_debit_credit_all(agence_, societe_, data_.id, date_initial_, (date_debut_ - interval '1 day')::date, journal_, type_::character varying, false, data_.table_tiers::character varying, true);
					debit_si_ = COALESCE(valeurs_.debit, 0);
					credit_si_ = COALESCE(valeurs_.credit, 0);
					solde_ = debit_si_ - credit_si_;
					IF(solde_ >= 0)THEN
						debit_si_ = solde_;
						credit_si_ = 0;
					ELSE
						debit_si_ = 0;
						credit_si_ = -(solde_);
					END IF;
					last_code_ = data_.code;
					IF(solde_!=0)THEN
						IF(cumule_) THEN 
							INSERT INTO table_et_grand_livre VALUES(data_.id, data_.code, data_.intitule, 'S.I', data_.designation, 'S.I', (date_debut_ - interval '1 day'), to_char((date_debut_ - interval '1 day'), 'dd')::integer, 'SOLDE INITIAL', '', '', debit_si_, credit_si_, null, null, true);
						ELSE
							INSERT INTO table_et_grand_livre VALUES(data_.id, data_.code, data_.intitule, 'S.I', data_.designation, 'S.I', (date_debut_ - interval '1 day'), to_char((date_debut_ - interval '1 day'), 'dd')::integer, 'SOLDE INITIAL', '', data_.journal, debit_si_, credit_si_, data_.compte_tiers, data_.table_tiers, true);
						END IF;				
					END IF;				
				END IF;
				IF(cumule_) THEN 
					INSERT INTO table_et_grand_livre VALUES(data_.id, data_.code, data_.intitule, data_.compte, data_.designation, null, null, null, null, null, '', data_.debit, data_.credit, null, null, COALESCE(data_.report, FALSE));
				ELSE
					INSERT INTO table_et_grand_livre VALUES(data_.id, data_.code, data_.intitule, data_.compte, data_.designation, data_.num_piece, data_.date_piece, data_.jour, UPPER(data_.libelle), data_.lettrage, data_.journal, data_.debit, data_.credit, data_.compte_tiers, data_.table_tiers, COALESCE(data_.report, FALSE));
				END IF;
			END IF;
		END LOOP;			
	END IF;
	RETURN QUERY SELECT * FROM table_et_grand_livre ORDER BY _code, _solde DESC, _date_piece, _numero;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_grand_livre(bigint, bigint, character varying, date, date, bigint, character varying, boolean, boolean, character varying)
  OWNER TO postgres;
