-- FUNCTION: public.update_presence()

-- DROP FUNCTION IF EXISTS public.update_presence();

CREATE OR REPLACE FUNCTION public.update_presence()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
   
    DECLARE
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrÃ©e
	pause_ DOUBLE PRECISION default 0;
	somme_ DOUBLE PRECISION default 0;
	marge_ DOUBLE PRECISION default 0;
	requis_ DOUBLE PRECISION default 0;
	total_presence_ DOUBLE PRECISION default 0;
	total_supplementaire_ DOUBLE PRECISION default 0;
	total_complementaire_ DOUBLE PRECISION default 0;
	suppl_ DOUBLE PRECISION default 0;
	total_retard_ DOUBLE PRECISION default 0;
	retardD_ DOUBLE PRECISION default 0;
	retardF_ DOUBLE PRECISION default 0;
	taux_validation_ DOUBLE PRECISION default 0;
	p_ RECORD;
	first_ RECORD;
	last_ RECORD;
    
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN
	IF(EXEC_) THEN
		select into suppl_ pa.limit_heure_sup from yvs_parametre_grh pa inner join yvs_agences ag on pa.societe = ag.societe inner join yvs_grh_employes e on e.agence = ag.id
			inner join yvs_grh_presence pe on pe.employe = e.id where pa.limit_heure_sup is not null and pe.id = OLD.id limit 1;
		if(suppl_ is null)then
			suppl_ = 0;
		end if;
		if(NEW.valider != OLD.valider)then	
			if(NEW.valider = true)then
				if(NEW.marge_approuve is not null)then
					marge_ = ((select extract(hour from NEW.marge_approuve)) + ((select extract(minute from NEW.marge_approuve))/60));
				end if;
				if(marge_ is null)then
					marge_ = 0;
				end if;
				
				if(NEW.duree_pause is not null)then
					pause_ = ((select extract(hour from NEW.duree_pause)) + ((select extract(minute from NEW.duree_pause))/60));
				end if;
				if(pause_ is null)then
					pause_ = 0;
				end if;
				
				HS_ = ((select extract(hour from OLD.heure_fin)) + ((select extract(minute from OLD.heure_fin))/60));
				HE_ = ((select extract(hour from OLD.heure_debut)) + ((select extract(minute from OLD.heure_debut))/60));
				requis_ = HS_ - HE_;
				if(requis_ is null)then
					requis_ = 0;
				end if;
				if(requis_ < 0)then
					requis_ = requis_ + 24;
				end if;
				if(requis_ >= pause_)then
					requis_ = requis_ - pause_;
				end if;

				total_presence_ = 0;
				total_supplementaire_ = 0;
				total_complementaire_ = 0;
				total_retard_ = 0;
				retardD_ = 0;
				retardF_ = 0;
				-- trouve le taux de validation de la fiche
				SELECT INTO taux_validation_ COALESCE(taux_journee,0) FROM yvs_grh_type_validation WHERE id=NEW.type_validation;
				IF(taux_validation_>=100 AND COALESCE(NEW.total_presence,0)>0) THEN
					SELECT INTO first_ p.* FROM yvs_grh_pointage p where presence = OLD.id and valider = true and actif = true and heure_entree is not null ORDER BY p.heure_entree ASC LIMIT 1;
					SELECT INTO last_ p.* FROM yvs_grh_pointage p where presence = OLD.id and valider = true and actif = true and heure_sortie is not null ORDER BY p.heure_entree DESC LIMIT 1;
					IF(first_.heure_entree IS NOT NULL) THEN
						retardD_ = ((select extract(hour from first_.heure_entree)) + ((select extract(minute from first_.heure_entree))/60))-
								  ((select extract(hour from NEW.heure_debut)) + ((select extract(minute from NEW.heure_debut))/60));
						RAISE NOTICE 'RETARD DEBUT %',retardD_;
                        if(retardD_ < 0)then
                            retardD_ = 0;
                        end if;
					END IF;
					IF(last_.heure_sortie IS NOT null) THEN
						retardF_ = ((select extract(hour from NEW.heure_fin)) + ((select extract(minute from NEW.heure_fin))/60))-
									((select extract(hour from last_.heure_sortie)) + ((select extract(minute from last_.heure_sortie))/60));
						RAISE NOTICE 'RETARD FIN % %',retardF_, (select extract(minute from last_.heure_sortie));
                        if(retardF_ < 0)then
                            retardF_ = 0;
                        end if;
					END IF;
					total_retard_= retardD_+retardF_;
				END IF;
-- 				update yvs_grh_presence set total_presence = 0, total_heure_sup = 0, total_heure_compensation = 0 where id = OLD.id;			
				FOR p_ in select * from yvs_grh_pointage p where presence = OLD.id and valider = true and actif = true and heure_entree is not null and heure_sortie is not null
														 ORDER BY p.heure_entree
				LOOP
					-- recuperation de l'interval heure fin - heure debut					
					HS_ = ((select extract(hour from p_.heure_sortie)) + ((select extract(minute from p_.heure_sortie))/60));
					HE_ = ((select extract(hour from p_.heure_entree)) + ((select extract(minute from p_.heure_entree))/60));
					somme_ = HS_ - HE_;
					if(somme_ is null)then
						somme_ = 0;
					end if;
					if(somme_ < 0)then
						somme_ = somme_ + 24;
					end if;
					
					if((p_.compensation_heure is null OR p_.compensation_heure = false) and (p_.heure_supplementaire is null OR p_.heure_supplementaire = false)) then
						total_presence_ = total_presence_ + somme_;
					else
						if(p_.heure_supplementaire = true) then
							total_supplementaire_ = total_supplementaire_ + somme_;
						end if;
						if(p_.compensation_heure = true) then
							total_complementaire_ = total_complementaire_ + somme_;
						end if;	
					end if;		
				END LOOP; 

				total_presence_ = total_presence_ + marge_;
				if(total_presence_ > requis_)then
					total_presence_ = requis_;
				end if;
				if(total_supplementaire_ > suppl_)then
					total_supplementaire_ = suppl_;
				end if;
				if(total_retard_<0)then
					total_retard_ = 0;
				end if;
				
				update yvs_grh_presence set total_presence = total_presence_, total_heure_sup = total_supplementaire_, total_heure_compensation = total_complementaire_ ,
											total_retard=total_retard_
										where id = OLD.id;
			end if;
		end if;
	END IF;
	return NEW; 
END;
$BODY$;

ALTER FUNCTION public.update_presence()
    OWNER TO postgres;
