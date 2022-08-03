-- Function: compta_et_balance(bigint, bigint, character varying, date, date, character varying)
DROP FUNCTION compta_et_balance(bigint, bigint, character varying, character varying, date, date, character varying);
CREATE OR REPLACE FUNCTION compta_et_balance(IN agence_ bigint, IN societe_ bigint, IN comptes_ character varying, IN date_debut_ date, IN date_fin_ date, IN type_ character varying)
  RETURNS TABLE(numero bigint, code character varying, general character varying, debit_initial double precision, credit_initial double precision, debit_periode double precision, credit_periode double precision, debit_solde_periode double precision, credit_solde_periode double precision, debit_solde_cumul double precision, credit_solde_cumul double precision, is_general boolean, table_tiers character varying, id_general bigint) AS
$BODY$
declare 
	query_ character varying default '';
	connexion character varying default 'dbname=lymytz_demo_0 user=postgres password=yves1910/';
	data_ record;
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
	
begin 	
	DROP TABLE IF EXISTS table_balance;
	CREATE TEMP TABLE IF NOT EXISTS table_balance(num bigint, cod character varying, gen character varying, di double precision, ci double precision, dp double precision, cp double precision,
							dsp double precision, csp double precision, dsc double precision, csc double precision, is_gen boolean, tt character varying, id_gen bigint); 
	DELETE FROM table_balance;
	if(type_ = 'T')then
		query_ = 'select DISTINCT(y.compte_tiers) as id, name_tiers(y.compte_tiers, y.table_tiers, ''R'') as code, y.table_tiers from yvs_compta_content_journal y inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where coalesce(y.compte_tiers, 0) > 0';			
	elsif(type_ = 'C') then
		query_ = 'select DISTINCT(c.id) as id, c.num_compte as code, null as table_tiers from yvs_compta_content_journal y inner join yvs_base_plan_comptable c on y.compte_general = c.id inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where c.num_compte is not null';			
	else
		query_ = 'select DISTINCT(c.id) as id, c.code_ref as code, null as table_tiers from yvs_compta_content_analytique o INNER JOIN yvs_compta_content_journal y ON o.contenu = y.id inner join yvs_compta_centre_analytique c on o.centre = c.id inner join yvs_compta_pieces_comptable p on y.piece = p.id inner join yvs_compta_journaux j on p.journal = j.id inner join yvs_agences a on a.id = j.agence where c.code_ref is not null';
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ || ' and a.id = '||agence_||'';
	end if;
	if(societe_ is not null and societe_ > 0)then
		query_ = query_ || ' and a.societe = '||societe_||'';
	end if;
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
			query_ = query_ || ' AND c.code_ref IN ('||ids_||')';	
		END IF;
	END IF;
	if(query_ is not null and query_ != '')then
		FOR data_ IN EXECUTE query_
		loop
			dsp_ = 0;
			dsc_ = 0;		
			csp_ = 0;
			csc_ = 0;

			di_ = (select compta_et_debit_initial(agence_, societe_, data_.id, date_debut_, type_, false));
			ci_ = (select compta_et_credit_initial(agence_, societe_, data_.id, date_debut_, type_, false));
			si_ = di_ - ci_ ;
			
			dp_ = (select compta_et_debit(agence_, societe_, data_.id, date_debut_, date_fin_, type_, false));
			cp_ = (select compta_et_credit(agence_, societe_, data_.id, date_debut_, date_fin_, type_, false));	
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
					insert into table_balance values(data_.id, data_.code, gen_.numero, di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_, general_, data_.table_tiers, gen_.id);
				else
					insert into table_balance values(data_.id, data_.code, '', di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_, general_, data_.table_tiers, 0);
				end if;
			end if;
		end loop;
	end if;
	return QUERY SELECT * from table_balance order by num;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_balance(bigint, bigint, character varying, date, date, character varying)
  OWNER TO postgres;


-- Function: compta_et_grand_livre(bigint, bigint, character varying, character varying, date, date, character varying, boolean)
DROP FUNCTION compta_et_grand_livre(bigint, bigint, character varying, character varying, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_grand_livre(IN societe_ bigint, IN agence_ bigint, IN comptes_ character varying, IN date_debut_ date, IN date_fin_ date, IN type_ character varying, IN lettrer_ boolean)
  RETURNS TABLE(id bigint, code character varying, intitule character varying, compte character varying, designation character varying, numero character varying, date_piece date, jour integer, libelle character varying, lettrage character varying, journal character varying, debit double precision, credit double precision, compte_tiers bigint, table_tiers character varying, solde boolean) AS
$BODY$
declare 
	data_ record;
	
	credit_si_ double precision default 0;
	debit_si_ double precision default 0;
	
	query_ character varying default '';
	last_code_ character varying default '';
	
	ids_ character varying default '''0''';
	valeur_ character varying default '0';
begin 	
	DROP TABLE IF EXISTS table_et_grand_livre;
	CREATE TEMP TABLE IF NOT EXISTS table_et_grand_livre(_id bigint, _code character varying, _intitule character varying, _compte character varying, _designation character varying, _numero character varying, _date_piece date, _jour integer, _libelle character varying, _lettrage character varying, _journal character varying, _debit double precision, _credit double precision, _compte_tiers bigint, _table_tiers character varying, _solde boolean); 
	DELETE FROM table_et_grand_livre;
	IF(type_ = 'T')THEN
		query_ = 'SELECT p.num_piece, y.jour, y.num_piece, y.num_ref, y.libelle, y.lettrage, y.debit, y.credit, y.compte_tiers as id, c.num_compte as compte, c.intitule as designation, p.date_piece, j.code_journal as journal, name_tiers(y.compte_tiers, y.table_tiers, ''R'') as code, name_tiers(y.compte_tiers, y.table_tiers, ''N'') as intitule, y.compte_tiers, y.table_tiers
				FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id';
	ELSIF(type_ = 'C') THEN
		query_ = 'SELECT p.num_piece, y.jour, y.num_piece, y.num_ref, y.libelle, y.lettrage, y.debit, y.credit, c.id, c.num_compte as code, c.intitule, p.date_piece, j.code_journal as journal, null as compte, null as designation, y.compte_tiers, y.table_tiers
				FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id';
			
	ELSE
		query_ = 'SELECT p.num_piece, y.jour, y.num_piece, y.num_ref, y.libelle, y.lettrage, l.debit, l.credit, n.id, c.num_compte as compte, c.intitule as designation, p.date_piece, j.code_journal as journal, n.code_ref as code, n.designation as intitule, y.compte_tiers, y.table_tiers
				FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id
				INNER JOIN yvs_compta_content_analytique l ON y.id = l.contenu INNER JOIN yvs_compta_centre_analytique n ON n.id = l.centre';
	END IF;
	query_ = query_ || ' WHERE p.date_piece BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
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
		query_ = query_ || ' AND COALESCE(LENGTH(y.lettrage), 0)';
		IF(lettrer_)THEN
			query_ = query_ || '>0';
		ELSE
			query_ = query_ || '<=0';
		END IF;
	END IF;
	IF(type_ = 'T')THEN
		query_ = query_ || ' ORDER BY name_tiers(y.compte_tiers, y.table_tiers, ''R''), p.date_piece, p.num_piece';
	ELSIF(type_ = 'C')THEN
		query_ = query_ || ' ORDER BY c.num_compte, p.date_piece, p.num_piece';
	ELSE
		query_ = query_ || ' ORDER BY n.code_ref, p.date_piece, p.num_piece';
	END IF;
	RAISE NOTICE 'query_ : %',query_;
	IF(query_ IS NOT NULL AND query_ != '')THEN
		FOR data_ IN EXECUTE query_
		LOOP
			IF(COALESCE(data_.id, 0) > 0)THEN
				IF(last_code_ != data_.code)THEN
					debit_si_ = (select compta_et_debit_initial(agence_, societe_, data_.id, data_.date_piece, type_, false));
					credit_si_ = (select compta_et_credit_initial(agence_, societe_, data_.id, data_.date_piece, type_, false));			
					last_code_ = data_.code;
					INSERT INTO table_et_grand_livre VALUES(data_.id, data_.code, data_.intitule, data_.compte, data_.designation, 'S.I', (data_.date_piece - interval '1 day'), to_char((data_.date_piece - interval '1 day'), 'dd')::integer, 'SOLDE INITIAL', '', data_.journal, debit_si_, credit_si_, data_.compte_tiers, data_.table_tiers, false);
				END IF;
				INSERT INTO table_et_grand_livre VALUES(data_.id, data_.code, data_.intitule, data_.compte, data_.designation, data_.num_piece, data_.date_piece, data_.jour, UPPER(data_.libelle), data_.lettrage, data_.journal, data_.debit, data_.credit, data_.compte_tiers, data_.table_tiers, false);
			END IF;
		END LOOP;			
	END IF;
	FOR data_ IN SELECT DISTINCT _id, _code, _intitule, _compte, _designation, _table_tiers, SUM(_debit) AS _debit, SUM(_credit) AS _credit FROM table_et_grand_livre GROUP BY _id, _code, _intitule, _compte, _designation, _table_tiers
	LOOP
		debit_si_ = data_._debit - data_._credit;
		IF(debit_si_ > 0)THEN
			credit_si_ = 0;
		ELSE
			credit_si_ = - debit_si_;
			debit_si_ = 0;
		END IF;
		-- INSERT INTO table_et_grand_livre VALUES(data_._id, data_._code, data_._intitule, data_._compte, data_._designation, 'S.F', date_fin_, to_char((date_fin_ - interval '1 day'), 'dd')::integer, 'Solde Final', '', '', debit_si_, credit_si_, data_._table_tiers, true);
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_grand_livre ORDER BY _solde, _code, _date_piece, _numero;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_grand_livre(bigint, bigint, character varying, date, date, character varying, boolean)
  OWNER TO postgres;
  
  
-- Function: fusion_data_for_table(character varying, bigint, bigint)
-- DROP FUNCTION fusion_data_for_table(character varying, bigint, bigint);
CREATE OR REPLACE FUNCTION fusion_data_for_table(table_ character varying, new_value bigint, old_value bigint)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	
BEGIN	
	return fusion_data_for_table(table_, new_value, old_value::character varying);
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fusion_data_for_table(character varying, bigint, bigint)
  OWNER TO postgres;

  
-- Function: fusion_data_for_table_all(character varying, bigint, character varying)
-- DROP FUNCTION fusion_data_for_table_all(character varying, bigint, character varying);
CREATE OR REPLACE FUNCTION fusion_data_for_table_all(table_ character varying, new_value bigint, old_value character varying)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	table_name_ character varying;
	ids_ character varying default '0';
	constraint_ record;	
	query_ character varying;
	
BEGIN	
	-- Construction de la chaine des old_value
	if(old_value is not null and old_value not in ('', ' '))then
		for query_ in select val from regexp_split_to_table(old_value,',') val
		loop
			ids_ = ids_ || ','||query_;
		end loop;
	end if;
	-- Recherche de toutes les tables rattachées a la table actuell
	for table_name_ in select tablename from pg_tables where tablename not like 'pg_%' and schemaname = 'public' order by tablename
	loop	
		-- Recherche de la clé secondaire liée a la clé primaire donnée
		FOR constraint_ IN SELECT k.CONSTRAINT_NAME, k.TABLE_NAME, k.COLUMN_NAME, f.TABLE_NAME AS TABLE_NAME_, f.COLUMN_NAME AS COLUMN_NAME_ 
			FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k 
			INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
			WHERE k.table_schema = 'public' AND k.TABLE_NAME = table_name_ AND c.CONSTRAINT_TYPE = 'FOREIGN KEY' AND f.TABLE_NAME = table_
		LOOP
			-- Modification de l'ancienne valeur par la nouvelle
			query_ = 'UPDATE public.'||table_name_||' SET '||constraint_.COLUMN_NAME||' = '||new_value||' WHERE '||constraint_.COLUMN_NAME||' in ('||ids_||')';
			EXECUTE query_;
			RAISE NOTICE 'query_ %',query_;
		END LOOP;
	end loop;
	EXECUTE 'DELETE FROM '||table_||' WHERE id  in ('||ids_||')';
	RAISE NOTICE '%','';
	return true;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fusion_data_for_table_all(character varying, bigint, character varying)
  OWNER TO postgres;

  
-- Function: fusion_data_for_table(character varying, bigint, character varying)
-- DROP FUNCTION fusion_data_for_table(character varying, bigint, character varying);
CREATE OR REPLACE FUNCTION fusion_data_for_table(table_ character varying, new_value bigint, old_value character varying)
  RETURNS boolean AS
$BODY$
  DECLARE 	
	result_ boolean default false;
BEGIN	
	if(table_ = 'yvs_base_conditionnement')then
		ALTER TABLE yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey;
	end if;
	result_ =  fusion_data_for_table_all(table_, new_value, old_value);
	if(table_ = 'yvs_base_conditionnement')then
		UPDATE yvs_base_mouvement_stock SET conditionnement = new_value WHERE conditionnement::character varying in (select val from regexp_split_to_table(old_value,',') val);
		ALTER TABLE yvs_base_mouvement_stock
		  ADD CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey FOREIGN KEY (conditionnement)
		      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
		      ON UPDATE CASCADE ON DELETE NO ACTION;
	elsif(table_ = 'yvs_base_tiers')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND table_tiers = 'TIERS';
	elsif(table_ = 'yvs_base_fournisseur')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND table_tiers = 'FOURNISSEUR';
	elsif(table_ = 'yvs_com_client')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND table_tiers = 'CLIENT';
	elsif(table_ = 'yvs_grh_employes')then
		UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND table_tiers = 'EMPLOYE';
	end if;
	return result_;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fusion_data_for_table(character varying, bigint, character varying)
  OWNER TO postgres;
