-- Function: compta_action_on_piece_caisse_salaire()
-- DROP FUNCTION compta_action_on_piece_caisse_salaire();
CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_salaire()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	numero_ character varying default '';
	nom_ character varying default '';
	prenom_ character varying default '';
	echeance_ RECORD;
	bulletin_ BIGINT;
	societe_ BIGINT;
	solde_ double precision default 0;
	payer_ double precision default 0;
	etape_total_ integer default 0;
	etape_valide_ integer default 0;
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;
		
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO bulletin_ COUNT(y.id) FROM yvs_compta_caisse_piece_bulletin y WHERE y.piece = NEW.id;
		IF(COALESCE(bulletin_, 0) = 1 )THEN
			SELECT INTO line_  ag.societe, cl.ref_bulletin as num_doc, cu.compte_tiers as tiers, cu.nom, cu.prenom  FROM yvs_compta_caisse_piece_salaire pv INNER JOIN yvs_compta_caisse_piece_bulletin dv ON dv.piece= pv.id 
				INNER JOIN yvs_grh_bulletins cl ON cl.id=dv.bulletin INNER JOIN yvs_grh_contrat_emps en ON en.id=cl.contrat
				INNER JOIN yvs_grh_employes cu ON cu.id=en.employe INNER JOIN yvs_agences ag ON cu.agence = ag.id  WHERE pv.id=NEW.id;
			id_tiers_= line_.tiers;
			--récupère le code tièrs de ce clients
			IF(line_.nom IS NOT NULL) THEN
				nom_ = line_.nom;
			END IF;
			IF(line_.prenom IS NOT NULL) THEN
				prenom_ = line_.prenom;
			END IF;
			societe_ = line_.societe;
			numero_ = line_.num_doc;
		ELSE	
			SELECT INTO societe_ a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;										    
		IF(societe_ IS NULL) THEN
			SELECT INTO societe_  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		-- recherche les étapes de validation
		SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE piece = NEW.id;
		SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE piece = NEW.id AND phase_ok IS TRUE;
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_SALAIRE', NEW.montant, numero_, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				societe_,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'R', nom_ ||' '||prenom_, NEW.model, etape_valide_, etape_total_);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_SALAIRE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=numero_, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
				statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='R', societe=societe_, name_tiers=nom_ ||' '||prenom_,
				etape_total=etape_total_, etape_valide=etape_valide_
			WHERE table_externe='DOC_SALAIRE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_SALAIRE', NEW.montant, numero_, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, societe_,NEW.author,
				NEW.caisse, NEW.caissier,null, id_tiers_,'R', nom_ ||' '||prenom_, NEW.model, etape_valide_, etape_total_);
		END IF;	
		UPDATE yvs_compta_notif_reglement_vente SET date_update = current_date WHERE piece_vente = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_SALAIRE' AND id_externe=OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_salaire()
  OWNER TO postgres;  
  
 
-- Trigger: compta_action_on_piece_salaire on yvs_compta_caisse_piece_salaire
-- DROP TRIGGER compta_action_on_piece_salaire ON yvs_compta_caisse_piece_salaire;
CREATE TRIGGER compta_action_on_piece_salaire
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_caisse_piece_salaire
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_salaire();
  
  
-- Function: compta_action_on_phase_reg_piece_salaire()
-- DROP FUNCTION compta_action_on_phase_reg_piece_salaire();
CREATE OR REPLACE FUNCTION compta_action_on_phase_reg_piece_salaire()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ integer default 0;
	etape_valide_ integer default 0;
	table_ character varying;
	action_ character varying;
	
BEGIN		
		action_=TG_OP;
		table_='DOC_SALAIRE';		
	IF(action_='INSERT' OR action_='UPDATE') THEN	
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE salaire= NEW.salaire;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE salaire= NEW.salaire AND phase_ok IS TRUE;
				UPDATE yvs_compta_mouvement_caisse SET 
					etape_total=etape_total_, etape_valide=etape_valide_
				WHERE table_externe=table_ AND id_externe=NEW.salaire;		
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE salaire= OLD.salaire;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE salaire= OLD.salaire AND phase_ok IS TRUE;
			UPDATE yvs_compta_mouvement_caisse SET 
					etape_total=etape_total_, etape_valide=etape_valide_
				WHERE table_externe=table_ AND id_externe=OLD.salaire;	    	 
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_phase_reg_piece_salaire()
  OWNER TO postgres;

  
-- Trigger: compta_action_on_phase_reg_piece_salaire on yvs_compta_phase_piece_salaire
-- DROP TRIGGER compta_action_on_phase_reg_piece_salaire ON yvs_compta_phase_piece_salaire;
CREATE TRIGGER compta_action_on_phase_reg_piece_salaire
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_phase_piece_salaire
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_phase_reg_piece_salaire();
