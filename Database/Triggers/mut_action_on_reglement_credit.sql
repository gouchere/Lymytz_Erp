-- Function: mut_action_on_reglement_credit()

-- DROP FUNCTION mut_action_on_reglement_credit();

CREATE OR REPLACE FUNCTION mut_action_on_reglement_credit()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	caisse_ RECORD;
	compte_ RECORD;
	action_ CHARACTER VARYING;
	mouvement_ CHARACTER VARYING DEFAULT 'D';
	table_name_ CHARACTER VARYING DEFAULT 'REGLEMENT_CREDIT';
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(EXEC_) THEN	
		IF(action_='INSERT' OR action_='UPDATE') THEN
			--SELECT INTO caisse_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
			--SELECT INTO compte_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
			SELECT INTO tiers_ m.id, CONCAT(e.nom, ' ', e.prenom) AS nom, r.reference FROM yvs_mut_credit r INNER JOIN yvs_mut_compte c ON r.compte = c.id INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE r.id = NEW.credit;
		END IF;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
		IF(EXEC_) THEN	
			-- Récupère le document
			IF(NEW.statut_piece='P') THEN 
				IF(NEW.mode_paiement='ESPECE') THEN
					PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);
				ELSE
					--Insert l'opération de dépôt dans la table opération_compte
					PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);

				END IF;
			END IF;
		END IF;
	ELSIF (action_='UPDATE') THEN 
		IF(EXEC_) THEN	
			IF(NEW.statut_piece='P')THEN
					IF(NEW.mode_paiement='ESPECE') THEN 
						PERFORM   mut_insert_into_mouv_caisse(NEW.id, table_name_);
					ELSE
						PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);	
					END iF;	
			ELSE
				DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = NEW.id;
				DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = NEW.id;	
			END IF;
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		IF(OLD.mode_paiement='ESPECE') THEN 	
			DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;	
		ELSE		
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = OLD.id;	
		END IF;
		RETURN OLD;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_reglement_credit()
  OWNER TO postgres;
