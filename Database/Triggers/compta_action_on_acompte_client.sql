-- Function: compta_action_on_acompte_client()

-- DROP FUNCTION compta_action_on_acompte_client();

CREATE OR REPLACE FUNCTION compta_action_on_acompte_client()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';
	etape_total_ integer default 0;
	etape_valide_ integer default 0;
	date_ date;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
	
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
			SELECT INTO line_  ag.societe, y.num_refrence, dp.users, y.client, cl.nom, cl.prenom , em.code_users AS caissier FROM yvs_compta_acompte_client y 
				INNER JOIN yvs_com_client cl ON y.client = cl.id INNER JOIN yvs_base_caisse ca ON y.caisse = ca.id LEFT JOIN yvs_grh_employes em ON ca.responsable = em.id
				LEFT JOIN yvs_users_agence dp ON dp.id = y.author INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE y.id= NEW.id; 
				
			IF(NEW.client IS NOT NULL) THEN
				id_tiers_=NEW.client;
			ELSE
				id_tiers_=line_.client;
			END IF;
			IF(NEW.statut='P') THEN 
				date_=NEW.date_acompte;
			ELSE
				date_=null;
			END IF;
			--récupère le code tièrs de ce clients
			SELECT INTO id_tiers_ t.tiers FROM yvs_com_client t WHERE t.id= NEW.client;
			IF(line_.societe IS NULL) THEN
				SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
			END IF;
			IF(line_.nom IS NOT NULL) THEN
				nom_=line_.nom;
			END IF;
			IF(line_.prenom IS NOT NULL) THEN
				prenom_=line_.prenom;
			END IF;
			-- recherche les étapes de validation
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_acompte_vente WHERE piece_vente= NEW.id;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_acompte_vente WHERE piece_vente= NEW.id AND phase_ok IS TRUE;
		END IF;         
		IF(action_='INSERT') THEN
			-- Récupère le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,etape_total, etape_valide,numero_externe)
			VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_VENTE', NEW.montant, NEW.num_refrence, NEW.date_acompte,NEW.date_acompte, NEW.date_paiement,  NEW.statut, 
					line_.societe, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model,etape_total_,etape_valide_,NEW.reference_externe);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_VENTE' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_refrence, note=NEW.commentaire, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
					reference_externe=NEW.num_refrence, date_mvt=NEW.date_acompte, date_paiment_prevu=NEW.date_acompte, date_paye=NEW.date_paiement, 
					statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_),
					etape_total=etape_total_, etape_valide=etape_valide_, numero_externe=NEW.reference_externe
				WHERE table_externe='ACOMPTE_VENTE' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,etape_total,etape_valide)
				VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_VENTE', NEW.montant, NEW.num_refrence, NEW.date_acompte, NEW.date_paiement, NEW.date_paiement,  NEW.statut, line_.societe,
					NEW.author,NEW.caisse, line_.caissier,null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model,etape_total_,etape_valide_);
			END IF;	
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_VENTE' AND id_externe=OLD.id;	
			RETURN OLD;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_acompte_client()
  OWNER TO postgres;
