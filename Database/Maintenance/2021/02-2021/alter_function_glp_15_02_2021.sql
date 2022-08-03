-- Function: credit_initial(character varying, character varying, date, character varying)
-- DROP FUNCTION credit_initial(character varying, character varying, date, character varying);
CREATE OR REPLACE FUNCTION credit_initial(
    agence_ character varying,
    valeur_ character varying,
    datedebut_ date,
    type_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   date_ date;
   exo_ record;
   credit_ double precision default 0;
   
begin 	
	select into exo_ datedebut , datefin from exercices where datedebut_ between datedebut and datefin;

	if(exo_.datedebut = datedebut_)then
		if(agence_  is null or agence_ = '')then
			if(type_ = 'A')then
				select into credit_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where c.reportanouveau is true and c.centreanal = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			elsif(type_ = 'C') then
				select into credit_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece  where c.reportanouveau is true and c.numcompte = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			else
				select into credit_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece  where c.reportanouveau is true and c.comptetiers = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			end if;
		else
			if(type_ = 'A')then
				select into credit_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece  inner join journaux a on a.codejournal = p.journal where c.reportanouveau is true and a.agence = agence_ and c.centreanal = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			elsif(type_ = 'C') then
				select into credit_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece  inner join journaux a on a.codejournal = p.journal where c.reportanouveau is true and a.agence = agence_ and c.numcompte = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			else
				select into credit_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece  inner join journaux a on a.codejournal = p.journal where c.reportanouveau is true and a.agence = agence_ and c.comptetiers = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			end if;
		end if;			
		
	else
		date_ = datedebut_ - interval '1 day';
		if(agence_  is null or agence_ = '')then
			if(type_ = 'A')then
				select into credit_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece  where c.centreanal = valeur_ and p.date_piece between exo_.datedebut and date_;
			elsif(type_ = 'C') then
				select into credit_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece  where c.numcompte = valeur_ and p.date_piece between exo_.datedebut and date_;
			else
				select into credit_ sum(c.credit) from contenujournal c  INNER JOIN piece_comptable p ON p.id=c.piece where c.comptetiers = valeur_ and p.date_piece between exo_.datedebut and date_;
			end if;
		else
			if(type_ = 'A')then
				select into credit_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece  inner join journaux a on a.codejournal = p.journal where a.agence = agence_ and c.centreanal = valeur_ and p.date_piece between exo_.datedebut and date_;
			elsif(type_ = 'C') then
				select into credit_ sum(c.credit) from contenujournal c  INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where a.agence = agence_ and c.numcompte = valeur_ and p.date_piece between exo_.datedebut and date_;
			else
				select into credit_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece  inner join journaux a on a.codejournal = p.journal where a.agence = agence_ and c.comptetiers = valeur_ and p.date_piece between exo_.datedebut and date_;
			end if;
		end if;	
	end if;
	if(credit_ is null)then
		credit_ = 0;
	end if;
	return credit_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION credit_initial(character varying, character varying, date, character varying)
  OWNER TO postgres;



-- Function: debit_initial(character varying, character varying, date, character varying)
-- DROP FUNCTION debit_initial(character varying, character varying, date, character varying);
CREATE OR REPLACE FUNCTION debit_initial(
    agence_ character varying,
    valeur_ character varying,
    datedebut_ date,
    type_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   date_ date;
   exo_ record;
   debit_ double precision default 0;
   
begin 	
	select into exo_ datedebut , datefin from exercices where datedebut_ between datedebut and datefin;
	if(exo_.datedebut = datedebut_)then
		if(agence_  is null or agence_ = '')then
			if(type_ = 'A')then
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where c.reportanouveau is true and c.centreanal = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			elsif(type_ = 'C') then
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where c.reportanouveau is true and c.numcompte = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			else
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where c.reportanouveau is true and c.comptetiers = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			end if;
		else
			if(type_ = 'A')then
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where c.reportanouveau is true and a.agence = agence_ and c.centreanal = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			elsif(type_ = 'C') then
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where c.reportanouveau is true and a.agence = agence_ and c.numcompte = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			else
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where c.reportanouveau is true and a.agence = agence_ and c.comptetiers = valeur_ and p.date_piece between exo_.datedebut and exo_.datefin;
			end if;
		end if;			
		
	else
		date_ = datedebut_ - interval '1 day';
		if(agence_  is null or agence_ = '')then
			if(type_ = 'A')then
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where c.centreanal = valeur_ and p.date_piece between exo_.datedebut and date_;
			elsif(type_ = 'C') then
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where c.numcompte = valeur_ and p.date_piece between exo_.datedebut and date_;
			else
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where c.comptetiers = valeur_ and p.date_piece between exo_.datedebut and date_;
			end if;
		else
			if(type_ = 'A')then
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where a.agence = agence_ and c.centreanal = valeur_ and p.date_piece between exo_.datedebut and date_;
			elsif(type_ = 'C') then
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where a.agence = agence_ and c.numcompte = valeur_ and p.date_piece between exo_.datedebut and date_;
			else
				select into debit_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where a.agence = agence_ and c.comptetiers = valeur_ and p.date_piece between exo_.datedebut and date_;
			end if;
		end if;	
	end if;
	if(debit_ is null)then
		debit_ = 0;
	end if;
	return debit_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION debit_initial(character varying, character varying, date, character varying)
  OWNER TO postgres;



-- Function: et_balance_analytique(character varying, character varying, character varying, date, date)
-- DROP FUNCTION et_balance_analytique(character varying, character varying, character varying, date, date);
CREATE OR REPLACE FUNCTION et_balance_analytique(
    IN agence_ character varying,
    IN comptedebut_ character varying,
    IN comptefin_ character varying,
    IN datedebut_ date,
    IN datefin_ date)
  RETURNS TABLE(numero character varying, libelle character varying, general character varying, debit_initial double precision, credit_initial double precision, debit_periode double precision, credit_periode double precision, debit_solde_periode double precision, credit_solde_periode double precision, debit_solde_cumul double precision, credit_solde_cumul double precision) AS
$BODY$
declare 
	comptes_ record;
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
	
begin 	
	CREATE TEMP TABLE IF NOT EXISTS balance(num character varying, lib character varying, gen character varying,
		di double precision, ci double precision,
		dp double precision, cp double precision,
		dsp double precision, csp double precision,
		dsc double precision, csc double precision); 
	delete from balance;

	for comptes_ in select codeplan, libele from plananals where codeplan between comptedebut_ and comptefin_
	loop
		dsp_ = 0;
		dsc_ = 0;		
		csp_ = 0;
		csc_ = 0;

		di_ = (select debit_initial(agence_, comptes_.codeplan, datedebut_, 'A'));
		ci_ = (select credit_initial(agence_, comptes_.codeplan, datedebut_, 'A'));
		si_ = di_ - ci_ ;
		if(agence_ is null or agence_ = '')then
			select into dp_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where (c.reportanouveau is false or c.reportanouveau is null) and c.centreanal = comptes_.codeplan and p.date_piece between datedebut_ and datefin_;
			if(dp_ is null)then
				dp_ = 0;
			end if;	
			select into cp_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where (c.reportanouveau is false or c.reportanouveau is null) and c.centreanal = comptes_.codeplan and p.date_piece between datedebut_ and datefin_;
			if(cp_ is null)then
				cp_ = 0;
			end if;	
			sp_ = dp_ - cp_ ;
		else
			select into dp_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where (c.reportanouveau is false or c.reportanouveau is null) and a.agence = agence_ and c.centreanal = comptes_.codeplan and p.date_piece between datedebut_ and datefin_;
			if(dp_ is null)then
				dp_ = 0;
			end if;	
			select into cp_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where (c.reportanouveau is false or c.reportanouveau is null) and a.agence = agence_ and c.centreanal = comptes_.codeplan and p.date_piece between datedebut_ and datefin_;
			if(cp_ is null)then
				cp_ = 0;
			end if;	
			sp_ = dp_ - cp_ ;
		end if;			
			
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
			insert into balance(num, lib, gen, di, ci, dp, cp, dsp, csp, dsc, csc) values(comptes_.codeplan, comptes_.libele, '', di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_);
		end if;
	end loop;

	return QUERY SELECT num, lib, gen, di, ci, dp, cp, dsp, csp, dsc, csc from balance order by num;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_balance_analytique(character varying, character varying, character varying, date, date)
  OWNER TO postgres;



-- Function: et_balance_generale(character varying, character varying, character varying, date, date)
-- DROP FUNCTION et_balance_generale(character varying, character varying, character varying, date, date);
CREATE OR REPLACE FUNCTION et_balance_generale(
    IN agence_ character varying,
    IN comptedebut_ character varying,
    IN comptefin_ character varying,
    IN datedebut_ date,
    IN datefin_ date)
  RETURNS TABLE(numero character varying, libelle character varying, general character varying, debit_initial double precision, credit_initial double precision, debit_periode double precision, credit_periode double precision, debit_solde_periode double precision, credit_solde_periode double precision, debit_solde_cumul double precision, credit_solde_cumul double precision) AS
$BODY$
declare 
	comptes_ record;
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
	
begin 	
	CREATE TEMP TABLE IF NOT EXISTS balance(num character varying, lib character varying, gen character varying,
		di double precision, ci double precision,
		dp double precision, cp double precision,
		dsp double precision, csp double precision,
		dsc double precision, csc double precision); 
	delete from balance;

	for comptes_ in select numcompte, intitule, comptegeneral as general from comptes where numcompte between comptedebut_ and comptefin_ and (comptegeneral is not null and comptegeneral != '')
	loop
		dsp_ = 0;
		dsc_ = 0;		
		csp_ = 0;
		csc_ = 0;
		
		RAISE NOTICE ' comptes_.general : %',comptes_.general;		
		di_ = (select debit_initial(agence_, comptes_.numcompte, datedebut_, 'C'));
		ci_ = (select credit_initial(agence_, comptes_.numcompte, datedebut_, 'C'));
		si_ = di_ - ci_ ;
		if(agence_ is null or agence_ = '')then
			select into dp_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece  where (c.reportanouveau is false or c.reportanouveau is null) and c.numcompte = comptes_.numcompte and p.date_piece between datedebut_ and datefin_;
			if(dp_ is null)then
				dp_ = 0;
			end if;	
			select into cp_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where (c.reportanouveau is false or c.reportanouveau is null) and c.numcompte = comptes_.numcompte and p.date_piece between datedebut_ and datefin_;
			if(cp_ is null)then
				cp_ = 0;
			end if;	
			sp_ = dp_ - cp_ ;
		else
			select into dp_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where (c.reportanouveau is false or c.reportanouveau is null) and a.agence = agence_ and c.numcompte = comptes_.numcompte and p.date_piece between datedebut_ and datefin_;
			if(dp_ is null)then
				dp_ = 0;
			end if;	
			select into cp_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where (c.reportanouveau is false or c.reportanouveau is null) and a.agence = agence_ and c.numcompte = comptes_.numcompte and p.date_piece between datedebut_ and datefin_;
			if(cp_ is null)then
				cp_ = 0;
			end if;	
			sp_ = dp_ - cp_ ;
		end if;				
			
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
			insert into balance(num, lib, gen, di, ci, dp, cp, dsp, csp, dsc, csc) values(comptes_.numcompte, comptes_.intitule, comptes_.general, di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_);
		end if;
	end loop;

	return QUERY SELECT num, lib, gen, di, ci, dp, cp, dsp, csp, dsc, csc from balance order by num;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_balance_generale(character varying, character varying, character varying, date, date)
  OWNER TO postgres;



-- Function: et_balance_generale_tiers(character varying, character varying, character varying, date, date)
-- DROP FUNCTION et_balance_generale_tiers(character varying, character varying, character varying, date, date);
CREATE OR REPLACE FUNCTION et_balance_generale_tiers(
    IN agence_ character varying,
    IN comptedebut_ character varying,
    IN comptefin_ character varying,
    IN datedebut_ date,
    IN datefin_ date)
  RETURNS TABLE(numero character varying, libelle character varying, general character varying, debit_initial double precision, credit_initial double precision, debit_periode double precision, credit_periode double precision, debit_solde_periode double precision, credit_solde_periode double precision, debit_solde_cumul double precision, credit_solde_cumul double precision) AS
$BODY$
declare 
	tiers_ record;
	data_ record;
	comptes_ record;
	compte_ character varying;
	name_ character varying default '';
	
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
	
begin 	
	CREATE TEMP TABLE IF NOT EXISTS balance(num character varying, lib character varying, gen character varying,
		di double precision, ci double precision,
		dp double precision, cp double precision,
		dsp double precision, csp double precision,
		dsc double precision, csc double precision); 
	delete from balance;

	-- Tiers
	for tiers_ in select codeclient as codetiers, name as nom, prenom, compte from clients where codeclient between comptedebut_ and comptefin_
	loop
		--Construction du nom du tiers
		if(tiers_.nom is not null)then
			name_ = tiers_.nom;
		end if;
		if(tiers_.prenom is not null)then
			if(name_ is not null)then
				name_ = name_ ||' '||tiers_.prenom;
			else
				name_ = tiers_.prenom;
			end if;
		end if;
		compte_ = tiers_.compte;

		--Recherche du code general du compte tiers
		select into comptes_ numcompte, intitule, comptegeneral as general from comptes where numcompte = compte_;
		
		dsp_ = 0;
		dsc_ = 0;		
		csp_ = 0;
		csc_ = 0;
		
		RAISE NOTICE ' comptes_.general : %',comptes_.general;		
		di_ = (select debit_initial(agence_, tiers_.codetiers, datedebut_, 'T'));
		ci_ = (select credit_initial(agence_, tiers_.codetiers, datedebut_, 'T'));
		si_ = di_ - ci_ ;
		if(agence_ is null or agence_ = '')then
			select into dp_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(dp_ is null)then
				dp_ = 0;
			end if;	
			select into cp_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(cp_ is null)then
				cp_ = 0;
			end if;	
			sp_ = dp_ - cp_ ;
		else
			select into dp_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where a.agence = agence_ and c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(dp_ is null)then
				dp_ = 0;
			end if;	
			select into cp_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where a.agence = agence_ and c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(cp_ is null)then
				cp_ = 0;
			end if;	
			sp_ = dp_ - cp_ ;
		end if;			
			
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
			insert into balance(num, lib, gen, di, ci, dp, cp, dsp, csp, dsc, csc) values(tiers_.codetiers, name_, comptes_.general, di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_);
		end if;
	end loop;

	
	-- Fournisseurs
	for tiers_ in select codefseur as codetiers, name as nom, prenom, compte from fournisseurs where codefseur between comptedebut_ and comptefin_
	loop
		--Construction du nom du tiers
		if(tiers_.nom is not null)then
			name_ = tiers_.nom;
		end if;
		if(tiers_.prenom is not null)then
			if(name_ is not null)then
				name_ = name_ ||' '||tiers_.prenom;
			else
				name_ = tiers_.prenom;
			end if;
		end if;
		compte_ = tiers_.compte;

		--Recherche du code general du compte tiers
		select into comptes_ numcompte, intitule, comptegeneral as general from comptes where numcompte = compte_;
		
		dsp_ = 0;
		dsc_ = 0;		
		csp_ = 0;
		csc_ = 0;
		
		RAISE NOTICE ' comptes_.general : %',comptes_.general;		
		di_ = (select debit_initial(agence_, tiers_.codetiers, datedebut_, 'T'));
		ci_ = (select credit_initial(agence_, tiers_.codetiers, datedebut_, 'T'));
		si_ = di_ - ci_ ;
		if(agence_ is null or agence_ = '')then
			select into dp_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(dp_ is null)then
				dp_ = 0;
			end if;	
			select into cp_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(cp_ is null)then
				cp_ = 0;
			end if;	
			sp_ = dp_ - cp_ ;
		else
			select into dp_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where a.agence = agence_ and c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(dp_ is null)then
				dp_ = 0;
			end if;	
			select into cp_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where a.agence = agence_ and c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(cp_ is null)then
				cp_ = 0;
			end if;	
			sp_ = dp_ - cp_ ;
		end if;			
			
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
			insert into balance(num, lib, gen, di, ci, dp, cp, dsp, csp, dsc, csc) values(tiers_.codetiers, name_, comptes_.general, di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_);
		end if;
	end loop;


	-- Utilisateurs
	for tiers_ in select coderep as codetiers, nom, prenom, compte from users where coderep between comptedebut_ and comptefin_
	loop
		--Construction du nom du tiers
		if(tiers_.nom is not null)then
			name_ = tiers_.nom;
		end if;
		if(tiers_.prenom is not null)then
			if(name_ is not null)then
				name_ = name_ ||' '||tiers_.prenom;
			else
				name_ = tiers_.prenom;
			end if;
		end if;
		compte_ = tiers_.compte;

		--Recherche du code general du compte tiers
		select into comptes_ numcompte, intitule, numcompte as general from comptes where numcompte = compte_;
		
		dsp_ = 0;
		dsc_ = 0;		
		csp_ = 0;
		csc_ = 0;
		
		RAISE NOTICE ' comptes_.general : %',comptes_.general;		
		di_ = (select debit_initial(agence_, tiers_.codetiers, datedebut_, 'T'));
		ci_ = (select credit_initial(agence_, tiers_.codetiers, datedebut_, 'T'));
		si_ = di_ - ci_ ;
		if(agence_ is null or agence_ = '')then
			select into dp_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where (c.reportanouveau is false or c.reportanouveau is null) and c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(dp_ is null)then
				dp_ = 0;
			end if;	
			select into cp_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece where (c.reportanouveau is false or c.reportanouveau is null) and c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(cp_ is null)then
				cp_ = 0;
			end if;	
			sp_ = dp_ - cp_ ;
		else
			select into dp_ sum(c.debit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where (c.reportanouveau is false or c.reportanouveau is null) and a.agence = agence_ and c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(dp_ is null)then
				dp_ = 0;
			end if;	
			select into cp_ sum(c.credit) from contenujournal c INNER JOIN piece_comptable p ON p.id=c.piece inner join journaux a on a.codejournal = p.journal where (c.reportanouveau is false or c.reportanouveau is null) and a.agence = agence_ and c.comptetiers = tiers_.codetiers and p.date_piece between datedebut_ and datefin_;
			if(cp_ is null)then
				cp_ = 0;
			end if;	
			sp_ = dp_ - cp_ ;
		end if;				
			
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
			insert into balance(num, lib, gen, di, ci, dp, cp, dsp, csp, dsc, csc) values(tiers_.codetiers, name_, comptes_.general, di_, ci_, dp_, cp_, dsp_, csp_, dsc_, csc_);
		end if;
	end loop;

	return QUERY SELECT num, lib, gen, di, ci, dp, cp, dsp, csp, dsc, csc from balance order by num;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_balance_generale_tiers(character varying, character varying, character varying, date, date)
  OWNER TO postgres;



-- Index: piece_comptable_date_piece_idx
-- DROP INDEX piece_comptable_date_piece_idx;
CREATE INDEX piece_comptable_date_piece_idx
  ON piece_comptable
  USING btree
  (date_piece);


-- Index: contenujournal_centreanal_idx
-- DROP INDEX contenujournal_centreanal_idx;
CREATE INDEX contenujournal_centreanal_idx
  ON contenujournal
  USING btree
  (centreanal COLLATE pg_catalog."default");


-- Index: contenujournal_comptetiers_idx
-- DROP INDEX contenujournal_comptetiers_idx;
CREATE INDEX contenujournal_comptetiers_idx
  ON contenujournal
  USING btree
  (comptetiers COLLATE pg_catalog."default");


-- Index: contenujournal_numcompte_idx
-- DROP INDEX contenujournal_numcompte_idx;
CREATE INDEX contenujournal_numcompte_idx
  ON contenujournal
  USING btree
  (numcompte COLLATE pg_catalog."default");


-- Index: contenujournal_piece_idx
-- DROP INDEX contenujournal_piece_idx;
CREATE INDEX contenujournal_piece_idx
  ON contenujournal
  USING btree
  (piece);


-- Index: contenujournal_reportanouveau_centreanal_idx
-- DROP INDEX contenujournal_reportanouveau_centreanal_idx;
CREATE INDEX contenujournal_reportanouveau_centreanal_idx
  ON contenujournal
  USING btree
  (reportanouveau, centreanal COLLATE pg_catalog."default");


-- Index: contenujournal_reportanouveau_comptetiers_idx
-- DROP INDEX contenujournal_reportanouveau_comptetiers_idx;
CREATE INDEX contenujournal_reportanouveau_comptetiers_idx
  ON contenujournal
  USING btree
  (reportanouveau, comptetiers COLLATE pg_catalog."default");


-- Index: contenujournal_reportanouveau_numcompte_idx
-- DROP INDEX contenujournal_reportanouveau_numcompte_idx;
CREATE INDEX contenujournal_reportanouveau_numcompte_idx
  ON contenujournal
  USING btree
  (reportanouveau, numcompte COLLATE pg_catalog."default");
