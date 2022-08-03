-- Function: decoupage_interval_date(date, date, character varying)

-- DROP FUNCTION decoupage_interval_date(date, date, character varying);

CREATE OR REPLACE FUNCTION decoupage_interval_date(IN date_entree_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(intitule character varying, date_sortie date) AS
$BODY$
DECLARE

    date_ date;

    intitule_ character varying;
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;

BEGIN    
-- 	DROP TABLE IF EXISTS table_decoupage_interval_date;
	CREATE TEMP TABLE IF NOT EXISTS table_decoupage_interval_date(_intitule character varying, _date_sortie date);
	DELETE FROM table_decoupage_interval_date;
	if(periode_ = 'A')then
		date_ = (date_entree_ + interval '1 year' - interval '1 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		intitule_ = (select extract(year from date_entree_));	
	elsif(periode_ = 'T')then
		date_ = (date_entree_ + interval '3 month' - interval '1 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		jour_0 = (select extract(month from date_entree_));
		if(char_length(jour_0)<2)then
			jour_0 = '0'||jour_0;
		end if;
		jour_ = '('||jour_0||'/';
		
		jour_0 = (select extract(month from date_));
		if(char_length(jour_0)<2)then
			jour_0 = '0'||jour_0;
		end if;
		intitule_ = jour_||jour_0||')';		
	elsif(periode_ = 'M')then
		date_ = (date_entree_ + interval '1 month' - interval '1 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		jour_ = (select extract(month from date_entree_));
		if(char_length(jour_)<2)then
			jour_ = '0'||jour_;
		end if;			
		annee_ = (select extract(year from date_entree_));	
		intitule_ = jour_ ||'-'|| annee_;
	elsif(periode_ = 'S')then
		date_ = (date_entree_ + interval '1 week' - interval '1 day');	
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;
		
		jour_0 = (select extract(day from date_entree_));
		if(char_length(jour_0)<2)then
			jour_0 = '0'||jour_0;
		end if;
		jour_ = '('||jour_0||'/';
		jour_0 = (select extract(day from date_));
		if(char_length(jour_0)<2)then
			jour_0 = '0'||jour_0;
		end if;
		jour_ = jour_||jour_0||')-';
		
		mois_ = (select extract(month from date_));
		if(char_length(mois_)<2)then
			mois_ = '0'||mois_;
		end if;
		mois_0 = (select extract(month from date_entree_));
		if(char_length(mois_0)<2)then
			mois_0 = '0'||mois_0;
		end if;
		if(mois_ != mois_0)then
			mois_ = '('|| mois_0 || '/'|| mois_ ||')';
		end if;
		intitule_ = jour_ || mois_;
	else
		date_ = (date_entree_ + interval '0 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		intitule_ = to_char(date_entree_ ,'dd');
	end if;
	INSERT INTO table_decoupage_interval_date VALUES(intitule_, date_);
	
	return QUERY select * from table_decoupage_interval_date;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION decoupage_interval_date(date, date, character varying)
  OWNER TO postgres;
