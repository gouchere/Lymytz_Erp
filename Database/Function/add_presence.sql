-- Function: add_presence(bigint, bigint, date, date)

-- DROP FUNCTION add_presence(bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION add_presence(societe_ bigint, employe_ bigint, date_debut_ date, date_fin_ date)
  RETURNS boolean AS
$BODY$   
DECLARE 
	em_ record;
	pe_ record;
	pa_ record;
	date_ date;
BEGIN
	select into pa_ duree_retard_autorise from yvs_parametre_grh where societe = societe_;
	if(employe_ is not null and employe_ > 0)then
		date_ = date_debut_;
		while (date_ <= date_fin_) loop
			select into pe_ id from yvs_grh_presence where employe = employe_ and date_ between date_debut and date_fin;
			if(pe_.id is null)then
				insert into yvs_grh_presence(employe, marge_approuve, valider, date_debut, date_fin, heure_debut, heure_fin, duree_pause)
					values (employe_, pa_.duree_retard_autorise, false, date_, date_, '07:30:00', '17:30:00', '01:30:00');
					
				select into pe_ id from yvs_grh_presence order by id desc limit 1;

				insert into yvs_grh_pointage(valider, actif, horaire_normale, presence, pointage_automatique, heure_entree, heure_sortie, heure_pointage,  date_save_entree, date_save_sortie)
					    values (true, true, true, pe_.id, true, (select ((date_ || ' ' || '07:30:00')::timestamp)), (select ((date_ || ' ' || '17:30:00')::timestamp)), 
					    (select ((date_ || ' ' || '17:30:00')::timestamp)), (select ((date_ || ' ' || '07:30:00')::timestamp)), (select ((date_ || ' ' || '17:30:00')::timestamp)));
			end if;
			date_ = date_ + interval '1 day';
		end loop;
	else
		for em_ in select e.id as id from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id where a.societe = societe_
		loop
			PERFORM add_presence(societe_, em_.id, date_debut_, date_fin_);
		end loop;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION add_presence(bigint, bigint, date, date)
  OWNER TO postgres;
