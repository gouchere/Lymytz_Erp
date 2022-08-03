-- Function: compta_action_on_notif_reglement_vente()

-- DROP FUNCTION compta_action_on_notif_reglement_vente();

CREATE OR REPLACE FUNCTION compta_action_on_notif_reglement_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
BEGIN	
IF(EXEC_) THEN 		
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;	
	--find agence concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_ ag.agence, dv.num_doc,cu.users, dv.client, cl.nom, cl.prenom, pv.numero_piece, pv.note, pv.montant, pv.date_piece, pv.date_paiment_prevu, pv.date_paiement, pv.statut_piece,
				ac.caisse, pv.caissier, pv.model, ac.client
			FROM yvs_compta_notif_reglement_vente y INNER JOIN yvs_compta_acompte_client ac ON y.acompte = ac.id
			INNER JOIN yvs_compta_caisse_piece_vente pv ON y.piece_vente = pv.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = pv.vente
			INNER JOIN yvs_com_client cl ON cl.id=dv.client INNER JOIN yvs_com_entete_doc_vente en ON en.id=dv.entete_doc INNER JOIN yvs_base_caisse ca ON ac.caisse = ca.id
			INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=en.creneau INNER JOIN yvs_com_creneau_point cr ON cr.id=cu.creneau_point
			INNER JOIN yvs_base_point_vente dp ON dp.id=cr.point INNER JOIN yvs_compta_journaux ag ON ag.id=ca.journal WHERE y.id = NEW.id; 
		
		--rÃ©cupÃ¨re le code tiÃ¨rs de ce clients
		SELECT INTO id_tiers_ t.tiers FROM yvs_com_client t WHERE t.id= line_.client;
		IF(line_.agence IS NULL) THEN
			SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
		END IF;
		IF(line_.nom IS NOT NULL) THEN
			nom_=line_.nom;
		END IF;
		IF(line_.prenom IS NOT NULL) THEN
			prenom_=line_.prenom;
		END IF;
		PERFORM equilibre_acompte_client(NEW.acompte);
	END IF;         
	IF(action_='INSERT') THEN
		-- RÃ©cupÃ¨re le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (line_.numero_piece, NEW.id, line_.note, 'NOTIF_VENTE', -line_.montant, line_.num_doc, line_.date_piece, line_.date_paiment_prevu, line_.date_paiement,  line_.statut_piece, 
				line_.agence, NEW.author, line_.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), line_.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='NOTIF_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=line_.numero_piece, note=line_.note, montant= -line_.montant, tiers_externe=id_tiers_, model = line_.model,
				reference_externe=line_.num_doc, date_mvt= line_.date_piece, date_paiment_prevu= line_.date_paiment_prevu, date_paye= line_.date_paiement, 
				statut_piece= line_.statut_piece, caisse= line_.caisse, caissier= line_.caissier, mouvement='R', agence=line_.agence, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='NOTIF_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (line_.numero_piece, NEW.id, line_.note, 'NOTIF_VENTE', -line_.montant, line_.num_doc, line_.date_piece, line_.date_paiment_prevu, line_.date_paiement,  line_.statut_piece, line_.agence,NEW.author,
				line_.caisse, line_.caissier,null, id_tiers_,'R', (nom_ ||' '||prenom_), line_.model);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='NOTIF_VENTE' AND id_externe=OLD.id;
		UPDATE yvs_compta_acompte_client SET statut_notif='W' WHERE id=acompte;
		RETURN OLD;		
	END IF;
END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_notif_reglement_vente()
  OWNER TO postgres;
