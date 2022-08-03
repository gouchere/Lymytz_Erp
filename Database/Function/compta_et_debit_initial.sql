-- Function: compta_et_debit_initial(bigint, bigint, bigint, date, character varying)

-- DROP FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying);

CREATE OR REPLACE FUNCTION compta_et_debit_initial(agence_ bigint, societe_ bigint, valeur_ bigint, date_debut_ date, type_ character varying)
  RETURNS double precision AS
$BODY$
declare 
   date_fin_ date;
   exo_ record;
   
begin 	
	select into exo_ date_debut , date_fin from yvs_base_exercice where date_debut_ between date_debut and date_fin;

	if(exo_.date_debut = date_debut_)then
		date_fin_ = date_debut_ - interval '1 month';		
		select into exo_ date_debut , date_fin from yvs_base_exercice where date_fin_ between date_debut and date_fin;
		date_fin_ = exo_.date_fin;			
	else
		date_fin_ = date_debut_ - interval '1 day';
	end if;
	
	return (select compta_et_debit(agence_, societe_, valeur_, date_debut_, date_fin_, type_));
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_et_debit_initial(bigint, bigint, bigint, date, character varying)
  OWNER TO postgres;
