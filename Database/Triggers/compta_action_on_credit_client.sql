-- Function: compta_action_on_credit_client()

-- DROP FUNCTION compta_action_on_credit_client();

CREATE OR REPLACE FUNCTION compta_action_on_credit_client()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
BEGIN	
IF(EXEC_) THEN 		
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;	
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, y.num_reference, dp.users, y.client, cl.nom, cl.prenom , em.code_users AS caissier, y.motif
			FROM yvs_compta_credit_client y INNER JOIN yvs_compta_reglement_credit_client cc ON cc.credit = y.id
			INNER JOIN yvs_com_client cl ON y.client = cl.id INNER JOIN yvs_base_caisse ca ON cc.caisse = ca.id LEFT JOIN yvs_grh_employes em ON ca.responsable = em.id
			LEFT JOIN yvs_users_agence dp ON dp.id = y.author INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE cc.id= NEW.id; 
			
		--récupère le code tièrs de ce clients
		SELECT INTO id_tiers_ t.tiers FROM yvs_com_client t WHERE t.id= line_.client;
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		IF(line_.nom IS NOT NULL) THEN
			nom_=line_.nom;
		END IF;
		IF(line_.prenom IS NOT NULL) THEN
			prenom_=line_.prenom;
		END IF;
	END IF;         
	IF(action_='INSERT') THEN
	    -- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
		VALUES (NEW.numero, NEW.id, line_.motif, 'CREDIT_VENTE', NEW.valeur, line_.num_reference, NEW.date_reg, NEW.date_reg, NEW.date_paiement,  NEW.statut, 
				line_.societe, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='CREDIT_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero, note=line_.motif, montant=NEW.valeur, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=line_.num_reference, date_mvt=NEW.date_reg, date_paiment_prevu=NEW.date_reg, date_paye=NEW.date_paiement, 
				statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='CREDIT_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero, NEW.id, line_.motif, 'CREDIT_VENTE', NEW.valeur, line_.num_reference, NEW.date_reg, NEW.date_reg, NEW.date_paiement,  NEW.statut, 
					line_.societe, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='CREDIT_VENTE' AND id_externe=OLD.id;		
	END IF;
END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_credit_client()
  OWNER TO postgres;
