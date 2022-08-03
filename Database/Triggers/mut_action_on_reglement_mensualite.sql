-- Function: mut_action_on_reglement_mensualite()

-- DROP FUNCTION mut_action_on_reglement_mensualite();

CREATE OR REPLACE FUNCTION mut_action_on_reglement_mensualite()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	credit_ RECORD;
	action_ CHARACTER VARYING;
	mouvement_ CHARACTER VARYING DEFAULT 'R';
	table_name_ CHARACTER VARYING DEFAULT 'REGLEMENT_MENSUALITE';
	existe_ BIGINT DEFAULT 0;
	sum_avalise double precision ;
	montant_ double precision ;
	avalise_ record;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');	
BEGIN	
	action_ = TG_OP; --INSERT DELETE TRONCATE UPDATE
	IF(EXEC_) THEN			
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO tiers_ m.id, CONCAT(e.nom, ' ', e.prenom) AS nom, r.reference FROM yvs_mut_mensualite s INNER JOIN yvs_mut_echellonage h ON s.echellonage = h.id
				INNER JOIN yvs_mut_credit r ON h.credit = r.id INNER JOIN yvs_mut_compte c ON r.compte = c.id INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id 
				INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE s.id = NEW.mensualite;
		END IF;
	END IF;
	--find societe concerné  
	CASE action_      
	WHEN 'INSERT' THEN
		IF(EXEC_) THEN
			-- Récupère le document
			IF(NEW.statut_piece='P')THEN
				IF(NEW.mode_paiement='ESPECE') THEN
					PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);
				ELSE
					--Insert l'opération de dépôt dans la table opération_compte
					PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);
				END IF;
			--Calcul les montant d'avalise libéré par ce remboursement si éventuellement il y en a
				--recherche le crédit concerné par le règlement
				SELECT INTO credit_ cr.* FROM  yvs_mut_credit cr INNER JOIN yvs_mut_echellonage ech ON ech.credit=cr.id
									  INNER JOIN yvs_mut_mensualite me ON me.echellonage=ech.id
							 WHERE me.id=NEW.mensualite;
				SELECT INTO sum_avalise SUM(a.montant) FROM yvs_mut_avalise_credit a WHERE a.credit=credit_.id;
				IF(sum_avalise IS NOT NULL) THEN
					FOR avalise_ IN SELECT * FROM  yvs_mut_avalise_credit  WHERE credit=credit_.id
					LOOP
						IF(avalise_.montant>=avalise_.montant_libere) THEN   -- si l'avalise n'est pas encore complètement couverte
						   montant_=(avalise_.montant/sum_avalise)*NEW.montant; --proportion libéré par le règlement en cours
						   IF(montant_ IS NOT NULL) THEN
								 IF ((montant_ + avalise_.montant_libere)>avalise_.montant) THEN
									UPDATE yvs_mut_avalise_credit SET montant_libere=avalise_.montant WHERE id=avalise_.id;
								 ELSE
									UPDATE yvs_mut_avalise_credit SET montant_libere=montant_libere + montant_ WHERE id=avalise_.id;
								 END IF;			     
							END IF;
						END IF;
					END LOOP;	
				END IF;
			END IF;
		END IF;
	WHEN 'UPDATE' THEN 
		IF(EXEC_) THEN
			IF(NEW.statut_piece='P')THEN
				IF(NEW.mode_paiement='ESPECE') THEN
						PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);
				ELSE
						--Insert l'opération de dépôt dans la table opération_compte
						PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);
				END IF;
			ELSE
				DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = NEW.id;	
				DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = NEW.id;
			END IF;
		END IF;
	WHEN 'DELETE' THEN 
			DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;	
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = OLD.id;
			RETURN OLD;
	WHEN 'TRONCATE'THEN 	
			DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;	
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = OLD.id;	
			RETURN OLD;
	END CASE;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_reglement_mensualite()
  OWNER TO postgres;
