-- Function: mut_action_on_operation_compte()

-- DROP FUNCTION mut_action_on_operation_compte();

CREATE OR REPLACE FUNCTION mut_action_on_operation_compte()
  RETURNS trigger AS
$BODY$    
DECLARE	
	tiers_ RECORD;
	caisse_ RECORD;
	action_ CHARACTER VARYING;
	mouvement_ CHARACTER VARYING DEFAULT 'D';
	table_name_ CHARACTER VARYING DEFAULT 'OPERATION_COMPTE';
	oldId_ BIGINT DEFAULT 0;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(EXEC_) THEN
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO caisse_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
			SELECT INTO tiers_ m.id, CONCAT(e.nom, ' ', e.prenom) AS nom FROM yvs_mut_compte c INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.compte;
			IF(NEW.nature = 'EPARGNE' OR NEW.nature = 'ASSURANCE' OR NEW.nature = 'SALAIRE' OR NEW.nature = 'INTERET' OR NEW.nature = 'DEPOT')THEN
				mouvement_ = 'R';
				IF(NEW.table_source='REGLEMENT_CREDIT') THEN
					mouvement_='D';	--Cas d'un retrait en caisse pour le paiment du crédit accordé à un mutualiste
				END IF;
			END IF;
		END IF;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
		IF(EXEC_) THEN
			-- Récupère le document
			IF(NEW.caisse IS NOT NULL)THEN
				PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);			
			END IF;
		END IF;
	ELSIF (action_='UPDATE') THEN 
		IF(EXEC_) THEN
			IF(NEW.caisse IS NOT NULL)THEN			
					PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);	
			END IF;
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;	
		RETURN OLD;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_operation_compte()
  OWNER TO postgres;
