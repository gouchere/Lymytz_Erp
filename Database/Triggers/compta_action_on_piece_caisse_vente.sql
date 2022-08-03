-- Function: compta_action_on_piece_caisse_vente()
-- DROP FUNCTION compta_action_on_piece_caisse_vente();
CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';
	echeance_ RECORD;
	vente_ BIGINT;
	solde_ double precision default 0;
	payer_ double precision default 0;
	etape_total_ integer default 0;
	etape_valide_ integer default 0;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
	EXEC_T_ BOOLEAN DEFAULT get_config('EXECUTE_TRIGGER');
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(EXEC_) THEN				
		IF(action_='INSERT' OR action_='UPDATE') THEN
			id_el_ = NEW.id;
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
			id_el_ = OLD.id;
		END IF;
			
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  dp.agence, dv.num_doc,cu.users, cl.tiers, dv.nom_client, cl.nom, cl.prenom, dv.entete_doc FROM yvs_compta_caisse_piece_vente pv INNER JOIN yvs_com_doc_ventes dv ON dv.id=pv.vente 
				INNER JOIN yvs_com_client cl ON cl.id=dv.client INNER JOIN yvs_com_entete_doc_vente en ON en.id=dv.entete_doc
				INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=en.creneau INNER JOIN yvs_com_creneau_point cr ON cr.id=cu.creneau_point
				INNER JOIN yvs_base_point_vente dp ON dp.id=cr.point WHERE pv.id=NEW.id; 
											
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
			END IF;
			id_tiers_=line_.tiers;
			-- recherche les Ã©tapes de validation
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece WHERE piece_vente= NEW.id;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece WHERE piece_vente= NEW.id AND phase_ok IS TRUE;
		ELSE
			SELECT INTO line_  dp.agence, dv.num_doc,cu.users, dv.client, dv.nom_client, cl.nom, cl.prenom, dv.entete_doc FROM yvs_compta_caisse_piece_vente pv INNER JOIN yvs_com_doc_ventes dv ON dv.id=pv.vente 
				INNER JOIN yvs_com_client cl ON cl.id=dv.client INNER JOIN yvs_com_entete_doc_vente en ON en.id=dv.entete_doc
				INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=en.creneau INNER JOIN yvs_com_creneau_point cr ON cr.id=cu.creneau_point
				INNER JOIN yvs_base_point_vente dp ON dp.id=cr.point WHERE pv.id=OLD.id; 
		END IF;         
		IF(action_='INSERT') THEN
			-- RÃ©cupÃ¨re le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total,numero_externe)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
					line_.agence,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, COALESCE(NEW.mouvement, 'R'), line_.nom_client, NEW.model, etape_valide_, etape_total_,NEW.reference_externe);
			vente_ = NEW.vente;
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
					reference_externe=line_.num_doc, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
					statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement=COALESCE(NEW.mouvement, 'R'), agence=line_.agence, name_tiers=line_.nom_client,
					etape_total=etape_total_, etape_valide=etape_valide_, numero_externe=NEW.reference_externe, author=NEW.author, date_update=NEW.date_update
					WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total,numero_externe)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.agence,NEW.author,
					NEW.caisse, NEW.caissier,null, id_tiers_, COALESCE(NEW.mouvement, 'R'), line_.nom_client, NEW.model, etape_valide_, etape_total_,NEW.reference_externe);
			END IF;	
			UPDATE yvs_compta_notif_reglement_vente SET date_update = current_date WHERE piece_vente = NEW.id;
			vente_ = NEW.vente;	           		
		END IF;
	END IF;
		IF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=OLD.id;		
			vente_ = OLD.vente;		    	 
		END IF;
	IF(EXEC_T_) THEN		
		-- Mise Ã  jour de l'Ã©chÃ©ancier
		SELECT INTO solde_ coalesce(SUM(montant), 0) FROM yvs_com_mensualite_facture_vente WHERE facture = vente_;
		SELECT INTO payer_ coalesce(SUM(montant), 0) FROM yvs_compta_caisse_piece_vente WHERE vente = vente_ AND statut_piece = 'P';
		IF(payer_ <= 0 OR solde_ <= 0)THEN
			UPDATE yvs_com_mensualite_facture_vente SET etat = 'W', avance = 0 WHERE facture = vente_ AND etat != 'W';
		ELSIF(solde_ > payer_)THEN
			WHILE(payer_ > 0)
			LOOP
				FOR echeance_ IN SELECT * FROM yvs_com_mensualite_facture_vente WHERE facture = vente_ ORDER BY date_reglement ASC
				LOOP
					IF(payer_ >= echeance_.montant)THEN
						UPDATE yvs_com_mensualite_facture_vente SET etat = 'P', avance = montant WHERE id = echeance_.id;
					ELSE
						UPDATE yvs_com_mensualite_facture_vente SET etat = 'R', avance = (montant - payer_) WHERE id = echeance_.id;
					END IF;
					payer_ = payer_ - echeance_.montant;
				END LOOP;
			END LOOP;
		ELSE
			UPDATE yvs_com_mensualite_facture_vente SET etat = 'P', avance = montant WHERE facture = vente_ AND etat != 'P';
		END IF;
-- 		PERFORM action_in_header_vente_or_piece_virement(line_.entete_doc);
		IF(action_='INSERT' OR action_='UPDATE') THEN
			PERFORM equilibre_vente(NEW.vente);
		ELSE
			PERFORM equilibre_vente(OLD.vente);	    	 
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_vente()
  OWNER TO postgres;
