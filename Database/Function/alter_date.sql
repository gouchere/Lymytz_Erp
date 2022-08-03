-- Function: alter_date(timestamp without time zone, character varying, timestamp without time zone)

-- DROP FUNCTION alter_date(timestamp without time zone, character varying, timestamp without time zone);

CREATE OR REPLACE FUNCTION alter_date(date_ timestamp without time zone, field_ character varying, value_ timestamp without time zone)
  RETURNS timestamp without time zone AS
$BODY$
DECLARE
    
     val_ integer default 0;
     year_ integer default 1970;
     month_ integer default 0;
     day_ integer default 0;
     hour_ integer default 0;
     minute_ integer default 0;
     second_ integer default 0;
     --milisecond_  default 0;

BEGIN
	IF(date_ IS null or value_ IS null or field_ is null) THEN
		return current_timestamp;
	END IF;
	year_ =(select extract(year from date_));
	month_ =(select extract(month from date_));
	day_ =(select extract(day from date_));
	hour_ =(select extract(hour from date_));
	minute_ =(select extract(minute from date_));
	--second_ =(select extract(second from date_));
	--milisecond_ =(select extract(milliseconds from date_));
   CASE field_ 
	WHEN 'year' THEN
	    val_= (select extract (year from value_));
	    return (val_||'-'||month_||'-'||day_||' '||hour_||':'||minute_||':'||second_)::timestamp;
	    
	WHEN 'month' THEN
	   val_= (select extract (month from value_));
	   return (year_||'-'||val_||'-'||day_||' '||hour_||':'||minute_||':'||second_)::timestamp;	  
	WHEN 'day' THEN
	   val_= (select extract (day from value_));
	   return (year_||'-'||month_||'-'||val_||' '||hour_||':'||minute_||':'||second_)::timestamp;	   
	WHEN 'hour' THEN
	   val_= (select extract (hour from value_));
	   return (year_||'-'||month_||'-'||day_||' '||val_||':'||minute_||':'||second_)::timestamp;
	WHEN 'minute' THEN
	  val_= (select extract (minute from value_));
	   return (year_||'-'||month_||'-'||day_||' '||hour_||':'||val_||':'||second_)::timestamp;
	--WHEN 'second' THEN
	  --val_= (select extract (second from value_));
	   
	--WHEN 'millisecond' THEN
	--val_= (select extract (milliseconds from value_));

	END CASE;
	return current_timestamp;
   
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_date(timestamp without time zone, character varying, timestamp without time zone)
  OWNER TO postgres;
