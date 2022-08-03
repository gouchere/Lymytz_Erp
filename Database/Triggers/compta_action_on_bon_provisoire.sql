-- Function: compta_action_on_bon_provisoire()

-- DROP FUNCTION compta_action_on_bon_provisoire();

CREATE OR REPLACE FUNCTION compta_action_on_bon_provisoire()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	id_model_ bigint;
	nom_prenom_ character varying default '';

	total_justif_ double precision default 0;
	montant_justif_ double precision default 0;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
BEGIN	
	IF(EXEC_) THEN
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		--find societe concerné
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  ag.societe, NEW.numero, t.id as tiers, t.nom, t.prenom FROM yvs_base_tiers t INNER JOIN yvs_users_agence f ON f.id=t.author INNER JOIN yvs_agences ag ON ag.id=f.agence WHERE t.id=NEW.tiers; 
										
			IF(line_.societe IS NULL) THEN
				SELECT INTO line_.societe  a.societe FROM yvs_agences a WHERE a.id=NEW.agence;
			END IF;
			IF(NEW.tiers IS NOT NULL) THEN
				id_tiers_=NEW.tiers;
			ELSE
				id_tiers_=line_.tiers;
			END IF;
			nom_prenom_ = NEW.beneficiaire;
			IF(nom_prenom_ IS NULL OR nom_prenom_ IN ('', ' '))THEN
				IF(line_.nom IS NOT NULL) THEN
					nom_prenom_ = line_.nom;
				END IF;
				IF(line_.prenom IS NOT NULL) THEN
					nom_prenom_ = nom_prenom_ || ' '||line_.prenom;
				END IF;
			END IF;
			SELECT INTO id_model_ id FROM yvs_base_mode_reglement WHERE societe = line_.societe AND type_reglement = 'ESPECE' AND default_mode IS TRUE LIMIT 1;
			IF(id_model_ IS NULL OR id_model_ < 1)THEN
				SELECT INTO id_model_ id FROM yvs_base_mode_reglement WHERE societe = line_.societe AND type_reglement = 'ESPECE' LIMIT 1;
			END IF;
		END IF;         
		IF(action_='INSERT' AND NEW.statut_justify != 'J') THEN
			-- Récupère le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
				VALUES (NEW.numero, NEW.id, NEW.description, 'BON_PROVISOIRE', NEW.montant, line_.numero, NEW.date_bon, NEW.date_valider, NEW.date_payer,  NEW.statut_paiement, line_.societe, NEW.author, NEW.caisse, NEW.caissier, null, id_tiers_, 'D', nom_prenom_, id_model_);
		ELSIF (action_='UPDATE' AND NEW.statut_justify != 'J') THEN 
			SELECT INTO montant_justif_ SUM(p.montant) FROM yvs_compta_caisse_piece_divers p INNER JOIN yvs_compta_justificatif_bon j ON j.piece = p.id WHERE p.statut_piece = 'P' AND j.bon = NEW.id;
			total_justif_ = total_justif_ + COALESCE(montant_justif_, 0);
			SELECT INTO montant_justif_ SUM(p.montant) FROM yvs_compta_caisse_piece_achat p INNER JOIN yvs_compta_justif_bon_achat j ON j.piece = p.id WHERE p.statut_piece = 'P' AND j.bon = NEW.id;
			total_justif_ = total_justif_ + COALESCE(montant_justif_, 0);
			SELECT INTO montant_justif_ SUM(p.montant) FROM yvs_compta_caisse_piece_mission p INNER JOIN yvs_compta_justif_bon_mission j ON j.mission = p.mission WHERE p.statut_piece = 'P' AND j.piece = NEW.id;
			total_justif_ = total_justif_ + COALESCE(montant_justif_, 0);
	-- 		RAISE NOTICE 'total_justif_ : %',total_justif_;
			IF(total_justif_ > NEW.montant)THEN
				total_justif_ = NEW.montant;
			END IF;
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='BON_PROVISOIRE' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 			
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero, note=NEW.description, montant=(NEW.montant - total_justif_), tiers_externe=id_tiers_, reference_externe=line_.numero, date_mvt=NEW.date_bon, model = id_model_, 
					date_paiment_prevu=NEW.date_valider, date_paye=NEW.date_payer, statut_piece=NEW.statut_paiement, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', societe=line_.societe, name_tiers=nom_prenom_
					WHERE table_externe='BON_PROVISOIRE' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
					VALUES (NEW.numero, NEW.id, NEW.description, 'BON_PROVISOIRE', (NEW.montant - total_justif_), line_.numero, NEW.date_bon, NEW.date_bon, NEW.date_bon,  NEW.statut_paiement, line_.societe, NEW.author, NEW.caisse, NEW.caissier, null, id_tiers_, 'D', nom_prenom_, id_model_);
			END IF;
			UPDATE yvs_compta_notif_reglement_achat SET date_update = current_date WHERE piece_achat = NEW.id;
		ELSIF(action_='DELETE' OR action_='TRONCATE' OR NEW.statut_justify = 'J') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='BON_PROVISOIRE' AND id_externe=OLD.id;	
			RETURN OLD;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_bon_provisoire()
  OWNER TO postgres;
