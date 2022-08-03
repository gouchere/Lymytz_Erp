-- Function: compta_et_balance(bigint, bigint, character varying, date, date, bigint, character varying, character varying)
-- DROP FUNCTION compta_et_balance(bigint, bigint, character varying, date, date, bigint, character varying, character varying);
CREATE OR REPLACE FUNCTION compta_et_balance(IN agence_ bigint, IN societe_ bigint, IN comptes_ character varying, IN date_debut_ date, IN date_fin_ date, IN journal_ bigint, IN type_ character varying, IN nature_ character varying)
  RETURNS TABLE(numero bigint, code character varying, general character varying, debit_initial double precision, credit_initial double precision, debit_periode double precision, credit_periode double precision, debit_solde_periode double precision, credit_solde_periode double precision, debit_solde_cumul double precision, credit_solde_cumul double precision, is_general boolean, table_tiers character varying, id_general bigint) AS
$BODY$
declare 
	data_ record;
	valeurs_ record;
	
	query_ character varying default '';
	sous_ bigint default 0;
	general_ boolean default false;
	
	gen_ record;
	
	di_ double precision default 0;
	ci_ double precision default 0;
	si_ double precision default 0;
	dp_ double precision default 0;
	cp_ double precision default 0;
	sp_ double precision default 0;
	dsp_ double precision default 0;
	csp_ double precision default 0;
	dsc_ double precision default 0;
	csc_ double precision default 0;
	ssc_ double precision default 0;
	
	ids_ character varying default '''0''';
	valeur_ character varying default '0';
	comptes_save_ character varying default comptes_;
	
	date_initial_ DATE DEFAULT date_fin_;
	
begin 	
	DROP TABLE IF EXISTS table_balance;
	CREATE TEMP TABLE IF NOT EXISTS table_balance(num bigint, cod character varying, gen character varying, di double precision, ci double precision, dp double precision, cp double precision,
							dsp double precision, csp double precision, dsc double precision, csc double precision, is_gen boolean, tt character varying, id_gen bigint); 
	DELETE FROM table_balance;
	if(type_ = 'T')then
		query_ = 'select DISTINCT(y.compte_tiers) as id, name_tiers(y.compte_tiers, y.table_tiers, ''R'') as code, y.table_tiers, SUM(COALESCE(y.debit, 0)) AS debit, SUM(COALESCE(y.credit, 0)) AS credit from yvs_compta_content_journal y inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where coalesce(y.compte_tiers, 0) > 0 ';			
		if (nature_ = 'T' or nature_ = 'R' or nature_ = 'S') then
			query_ = query_ || ' AND table_tiers = ''TIERS''';
		elsif (nature_ = 'E') then
			query_ = query_ || ' AND table_tiers = ''EMPLOYE''';
		elsif (nature_ = 'C') then
			query_ = query_ || ' AND table_tiers = ''CLIENT''';
		elsif (nature_ = 'F')then
			query_ = query_ || ' AND table_tiers = ''FOURNISSEUR''';
		elsif(nature_ = 'CF') then
			query_ = query_ || ' AND table_tiers IN (''CLIENT'', ''FOURNISSEUR'')';
		end if;
	elsif(type_ = 'C') then
		query_ = 'select DISTINCT(c.id) as id, c.num_compte as code, null::character varying as table_tiers, SUM(y.debit) AS debit, SUM(y.credit) AS credit from yvs_compta_content_journal y inner join yvs_base_plan_comptable c on y.compte_general = c.id inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where c.num_compte is not null';			
	else
		query_ = 'select DISTINCT(c.id) as id, c.code_ref as code, null::character varying as table_tiers, SUM(o.debit) AS debit, SUM(o.credit) AS credit from yvs_compta_content_analytique o INNER JOIN yvs_compta_content_journal y ON o.contenu = y.id inner join yvs_compta_centre_analytique c on o.centre = c.id inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where c.code_ref is not null';
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ || ' and a.id = '||agence_||'';
	end if;
	if(societe_ is not null and societe_ > 0)then
		query_ = query_ || ' and a.societe = '||societe_||'';
	end if;
	query_ = query_ || ' and p.date_piece BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(comptes_, '') NOT IN ('', ' ', '0'))THEN
		FOR valeur_ IN SELECT TRIM(head) FROM regexp_split_to_table(comptes_,',') head WHERE CHAR_LENGTH(TRIM(head)) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(TRIM(valeur_));
		END LOOP;
		IF(type_ = 'T')THEN
			query_ = query_ || ' AND name_tiers(y.compte_tiers, y.table_tiers, ''R'') IN ('||ids_||')';		
		ELSIF(type_ = 'C') THEN
			query_ = query_ || ' AND c.num_compte IN ('||ids_||')';	
		ELSE
			query_ = query_ || ' AND c.code_ref IN ('||ids_||')';	
		END IF;
	END IF;
	if(type_ = 'T')then
		query_ = query_ || ' GROUP BY y.compte_tiers, y.table_tiers';
	elsif(type_ = 'C') then
		query_ = query_ || ' GROUP BY c.id';
	else
		query_ = query_ || ' GROUP BY c.id';
	end if;
	if(query_ is not null and query_ != '')then
		SELECT INTO date_initial_ date_debut FROM yvs_base_exercice WHERE date_debut_ BETWEEN date_debut AND date_fin;
		FOR data_ IN EXECUTE query_
		loop
			dsp_ = 0;
			dsc_ = 0;		
			csp_ = 0;
			csc_ = 0;

			IF(date_initial_ < date_debut_)THEN 
				SELECT INTO valeurs_ * FROM compta_et_debit_credit_all(agence_, societe_, data_.id, date_initial_, (date_debut_ - interval '1 day')::date, journal_, type_::character varying, false, data_.table_tiers::character varying, true);
			ELSE
				SELECT INTO valeurs_ * FROM compta_et_debit_credit_initial(agence_, societe_, data_.id, date_fin_, journal_, type_, false, data_.table_tiers, true);
			END IF;
			di_ = COALESCE(valeurs_.debit, 0);
			ci_ = COALESCE(valeurs_.credit, 0);
			si_ = di_ - ci_ ;
			
			IF(date_initial_ < date_debut_)THEN 
				dp_ = COALESCE(data_.debit, 0);
				cp_ = COALESCE(data_.credit, 0);
			ELSE
				dp_ = COALESCE(data_.debit, 0) - di_;
				cp_ = COALESCE(data_.credit, 0) - ci_;
			END IF;
			sp_ = dp_ - cp_ ;	
				
			if(si_ > 0)then
				di_ = si_;
				ci_ = 0;			
			elsif(si_ < 0) then
				ci_ = -(si_);
				di_ = 0;
			else
				ci_ = 0;
				di_ = 0;
			end if;		
				
			if(sp_ > 0)then
				dsp_ = sp_;
				csp_ = 0;			
			elsif(sp_ < 0)then
				csp_ = -(sp_);
				dsp_ = 0;
			else
				csp_ = 0;
				dsp_ = 0;
			end if;

			ssc_ = si_ + sp_;
			if(ssc_ > 0)then
				dsc_ = ssc_;
				csc_ = 0;			
			elsif(ssc_ < 0)then
				csc_ = -(ssc_);
				dsc_ = 0;
			else
				csc_ = 0;
				dsc_ = 0;
			end if;		
			
			if(di_ != 0 or ci_ != 0 or dp_ !=0 or cp_ != 0 or dsp_ != 0 or csp_ != 0 or dsc_ != 0 or csc_ !=0)then
				general_ = false;
				if(type_ = 'C')then
					select into gen_ coalesce(g.id, c.id) as id, coalesce(g.num_compte, c.num_compte) as numero from yvs_base_plan_comptable g right join yvs_base_plan_comptable c on g.id = c.compte_general where c.id = data_.id;
					select into sous_ count(c.id) from yvs_base_plan_comptable c where c.compte_general = data_.id;
					if(sous_ is not null and sous_ > 0)then
						general_ = true;
					end if;
					insert into table_balance values(data_.id, data_.code, gen_.numero, COALESCE(di_, 0), COALESCE(ci_, 0), COALESCE(dp_, 0), COALESCE(cp_, 0), COALESCE(dsp_, 0), COALESCE(csp_, 0), COALESCE(dsc_, 0), COALESCE(csc_, 0), general_, data_.table_tiers, gen_.id);
				else
					insert into table_balance values(data_.id, data_.code, '', COALESCE(di_, 0), COALESCE(ci_, 0), COALESCE(dp_, 0), COALESCE(cp_, 0), COALESCE(dsp_, 0), COALESCE(csp_, 0), COALESCE(dsc_, 0), COALESCE(csc_, 0), general_, data_.table_tiers, 0);
				end if;
			end if;
			comptes_save_ = REPLACE(comptes_save_, data_.code,'');
		end loop;
		ids_ = '''0''';
		RAISE NOTICE 'comptes_save_ : %',comptes_save_;
		IF(COALESCE(comptes_save_, '') NOT IN ('', ' ', '0'))THEN
			FOR valeur_ IN SELECT TRIM(head) FROM regexp_split_to_table(comptes_save_,',') head WHERE CHAR_LENGTH(TRIM(head)) > 0
			LOOP
				ids_ = ids_ ||','||QUOTE_LITERAL(TRIM(valeur_));
			END LOOP;
		END IF;
		RAISE NOTICE 'ids_ : %',ids_;
		IF(ids_ != '''0''')THEN
			if(type_ = 'T')then
				query_ = 'select DISTINCT(y.compte_tiers) as id, name_tiers(y.compte_tiers, y.table_tiers, ''R'') as code, y.table_tiers, SUM(COALESCE(y.debit, 0)) AS debit, SUM(COALESCE(y.credit, 0)) AS credit from yvs_compta_content_journal y inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where coalesce(y.compte_tiers, 0) > 0 ';			
				if (nature_ = 'T' or nature_ = 'R' or nature_ = 'S') then
					query_ = 'select p.id, p.code_tiers as code, ''TIERS''::character varying as table_tiers from yvs_base_tiers p WHERE p.code_tiers IN ('||ids_||')';
				elsif (nature_ = 'E') then
					query_ = 'select p.id, p.code_tiers as code, ''EMPLOYE''::character varying as table_tiers from yvs_grh_employes c inner join yvs_base_tiers p on c.compte_tiers = p.id WHERE p.code_tiers IN ('||ids_||')';
				elsif (nature_ = 'C') then
					query_ = 'select c.id, c.code_client as code, ''CLIENT''::character varying as table_tiers from yvs_com_client c inner join yvs_base_tiers p on c.tiers = p.id WHERE c.code_client IN ('||ids_||')';
				elsif (nature_ = 'F')then
					query_ = 'select c.id, c.code_fsseur as code, ''FOURNISSEUR''::character varying as table_tiers from yvs_base_fournisseur c inner join yvs_base_tiers p on c.tiers = p.id WHERE c.code_fsseur IN ('||ids_||')';
				end if;
			elsif(type_ = 'C') then
				query_ = 'select c.id, c.num_compte as code, null::character varying as table_tiers from yvs_base_plan_comptable c inner join yvs_base_nature_compte p on c.nature_compte = p.id WHERE c.num_compte IN ('||ids_||')';			
			else
				query_ = 'select c.id, c.code_ref as code, null::character varying as table_tiers from yvs_compta_centre_analytique c inner join yvs_compta_plan_analytique p on c.plan = p.id WHERE c.code_ref IN ('||ids_||')';
			end if;
			if(societe_ is not null and societe_ > 0)then
				query_ = query_ || ' and p.societe = '||societe_||'';
			elsif(agence_ is not null and agence_ > 0)then
				query_ = query_ || ' and p.societe = (SELECT a.societe FROM yvs_agences a WHERE a.id = '||agence_||')';
			end if;
-- 			RAISE NOTICE 'query_ : %',query_;
			FOR data_ IN EXECUTE query_
			LOOP
				sp_ = 0;
				dp_ = 0;
				cp_ = 0;		
				dsp_ = 0;
				csp_ = 0;
				dsc_ = 0;
				csc_ = 0;

				IF(date_initial_ < date_debut_)THEN 
					SELECT INTO valeurs_ * FROM compta_et_debit_credit_all(agence_, societe_, data_.id, date_initial_, (date_debut_ - interval '1 day')::date, journal_, type_::character varying, false, data_.table_tiers::character varying, true);
				ELSE
					SELECT INTO valeurs_ * FROM compta_et_debit_credit_initial(agence_, societe_, data_.id, date_fin_, journal_, type_, false, data_.table_tiers, true);
				END IF;
				di_ = COALESCE(valeurs_.debit, 0);
				ci_ = COALESCE(valeurs_.credit, 0);
				si_ = di_ - ci_ ;	
				
				if(sp_ > 0)then
					dsp_ = sp_;
					csp_ = 0;			
				elsif(sp_ < 0)then
					csp_ = -(sp_);
					dsp_ = 0;
				else
					csp_ = 0;
					dsp_ = 0;
				end if;

				ssc_ = si_ + sp_;
				if(ssc_ > 0)then
					dsc_ = ssc_;
					csc_ = 0;			
				elsif(ssc_ < 0)then
					csc_ = -(ssc_);
					dsc_ = 0;
				else
					csc_ = 0;
					dsc_ = 0;
				end if;		
				
				if(di_ != 0 or ci_ != 0 or dp_ !=0 or cp_ != 0 or dsp_ != 0 or csp_ != 0 or dsc_ != 0 or csc_ !=0)then
					general_ = false;
					if(type_ = 'C')then
						select into gen_ coalesce(g.id, c.id) as id, coalesce(g.num_compte, c.num_compte) as numero from yvs_base_plan_comptable g right join yvs_base_plan_comptable c on g.id = c.compte_general where c.id = data_.id;
						select into sous_ count(c.id) from yvs_base_plan_comptable c where c.compte_general = data_.id;
						if(sous_ is not null and sous_ > 0)then
							general_ = true;
						end if;
						insert into table_balance values(data_.id, data_.code, gen_.numero, COALESCE(di_, 0), COALESCE(ci_, 0), COALESCE(dp_, 0), COALESCE(cp_, 0), COALESCE(dsp_, 0), COALESCE(csp_, 0), COALESCE(dsc_, 0), COALESCE(csc_, 0), general_, COALESCE(data_.table_tiers, '')::character varying, gen_.id);
					else
						insert into table_balance values(data_.id, data_.code, '', COALESCE(di_, 0), COALESCE(ci_, 0), COALESCE(dp_, 0), COALESCE(cp_, 0), COALESCE(dsp_, 0), COALESCE(csp_, 0), COALESCE(dsc_, 0), COALESCE(csc_, 0), general_, COALESCE(data_.table_tiers, '')::character varying, 0);
					end if;
				end if;
			END LOOP;
		END IF;
	end if;
	return QUERY SELECT * from table_balance order by num;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_balance(bigint, bigint, character varying, date, date, bigint, character varying, character varying)
  OWNER TO postgres;
