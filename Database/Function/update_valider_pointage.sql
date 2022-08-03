-- Function: update_valider_pointage()

-- DROP FUNCTION update_valider_pointage();

CREATE OR REPLACE FUNCTION update_valider_pointage()
  RETURNS boolean AS
$BODY$   
DECLARE 
	p_ record;
	o_ record;  
	date_debut_ timestamp;
	date_fin_ timestamp;
BEGIN
	for p_ in select id, date_debut, heure_debut, date_fin, heure_fin from yvs_grh_presence 
	loop
		date_debut_ =  (select ((p_.date_debut || ' ' || p_.heure_debut)::timestamp));
		date_fin_ =  (select ((p_.date_fin || ' ' || p_.heure_fin)::timestamp));
		if(p_.heure_debut<p_.heure_fin)then
			date_fin_ = date_fin_ + interval '1 day';
		end if;
		RAISE NOTICE 'date_debut_ : %',date_debut_ || ' -- date_fin_ : '||date_fin_; 
		
		for o_ in select id, heure_entree, heure_sortie from yvs_grh_pointage where presence = p_.id
		loop
			if(((date_debut_ <= o_.heure_entree) and (o_.heure_entree <= date_fin_)) and ((date_debut_ <= o_.heure_sortie) and (o_.heure_sortie <= date_fin_)))then
				update yvs_grh_pointage set valider = true where id = o_.id;
			else
				update yvs_grh_pointage set valider = false where id = o_.id;
			end if;
		end loop;
	end loop;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_valider_pointage()
  OWNER TO postgres;
