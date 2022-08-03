-- Function: compta_action_on_piece_caisse_achat()
-- DROP FUNCTION compta_action_on_piece_caisse_achat();
CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_achat()
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
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
	
BEGIN	
	IF(EXEC_) THEN
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		--find societe concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  dv.agence, dv.num_doc, dv.fournisseur, f.nom, f.prenom FROM yvs_compta_caisse_piece_achat pv 
				INNER JOIN yvs_com_doc_achats dv ON dv.id=pv.achat INNER JOIN yvs_base_fournisseur f ON f.id=dv.fournisseur WHERE pv.id=NEW.id; 
										
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
			END IF;
			IF(NEW.fournisseur IS NOT NULL) THEN
				id_tiers_=NEW.fournisseur;
			ELSE
				id_tiers_=line_.fournisseur;
			END IF;
			IF(line_.nom IS NOT NULL) THEN
				nom_=line_.nom;
			END IF;
			IF(line_.prenom IS NOT NULL) THEN
				prenom_=line_.prenom;
			END IF;
			--rÃ©cupÃ¨re le code tiÃ¨rs de ce fournisseurs
			SELECT INTO id_tiers_ t.tiers FROM yvs_base_fournisseur t WHERE t.id=id_tiers_;		
			-- recherche les Ã©tapes de validation
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece_achat WHERE piece_achat= NEW.id;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece_achat WHERE piece_achat= NEW.id AND phase_ok IS TRUE;
		END IF;         
		IF(action_='INSERT') THEN
			-- RÃ©cupÃ¨re le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, 
			model, etape_valide, etape_total,numero_externe)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.agence,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, 'D',(nom_||' '||prenom_),
				NEW.model,etape_valide_, etape_total_,NEW.reference_externe);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, model = NEW.model, 
					date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', agence=line_.agence, name_tiers=(nom_||' '||prenom_)
					,etape_total=etape_total_, etape_valide=etape_valide_, numero_externe=NEW.reference_externe
					WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, agence, author, caisse,caissier, tiers_interne, tiers_externe, mouvement, name_tiers,
				model,etape_valide_, etape_total_,numero_externe)
					VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.agence,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'D',(nom_||' '||prenom_), 
					NEW.model,etape_valide_, etape_total_,NEW.reference_externe);
			END IF;
			UPDATE yvs_compta_notif_reglement_achat SET date_update = current_date WHERE piece_achat = NEW.id;
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_ACHAT' AND id_externe=OLD.id;
			RETURN OLD;			
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_achat()
  OWNER TO postgres;


-- Function: compta_action_on_piece_caisse_divers()
-- DROP FUNCTION compta_action_on_piece_caisse_divers();
CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	action_ character varying;
	id_ bigint;
	id_el_ bigint;
	id_tiers_ bigint;
	montant_ double precision default 0 ;
	nom_ character varying default '';
	prenom_ character varying default '';

	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
BEGIN	
	IF(EXEC_) THEN
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  dv.agence, dv.num_piece as num_doc, dv.id_tiers, dv.table_tiers , dv.mouvement, dv.description FROM yvs_compta_caisse_piece_divers pv 
				INNER JOIN yvs_compta_caisse_doc_divers dv ON dv.id=pv.doc_divers WHERE pv.id=NEW.id; 
										
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
			END IF;
			--rÃ©cupÃ©rer nom du tiers
			IF(line_.id_tiers IS NOT NULL) THEN
			  --SELECT INTO tiers_ t.* FROM yvs_base_tiers t WHERE t.id=line_.tiers;
			   SELECT INTO nom_ name_tiers(line_.id_tiers, line_.table_tiers,'N');
			   SELECT INTO id_tiers_ yvs_give_id_tiers(line_.id_tiers, line_.table_tiers,'N');
			END IF;
			
		END IF;         
		IF(action_='INSERT') THEN
			id_ = NEW.doc_divers;
			-- RÃ©cupÃ¨re le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, 
													statut_piece, agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,numero_externe)
				VALUES (NEW.num_piece, NEW.id, line_.description, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_valider,  
						NEW.statut_piece, line_.agence,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, line_.mouvement, NEW.beneficiaire, NEW.mode_paiement,NEW.reference_externe);
		ELSIF (action_='UPDATE') THEN 
			id_ = NEW.doc_divers;
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_piece, note=line_.description, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, model = NEW.mode_paiement,
					date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_valider, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement=line_.mouvement, agence=line_.agence, name_tiers=NEW.beneficiaire,
					numero_externe=NEW.reference_externe
					WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, 
				statut_piece, agence, author, caisse,caissier, tiers_interne, tiers_externe, mouvement,name_tiers, model,numero_externe)
					VALUES (NEW.num_piece, NEW.id, line_.description, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_valider,  
					NEW.statut_piece, line_.agence,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,line_.mouvement, NEW.beneficiaire, NEW.mode_paiement,NEW.reference_externe);
			END IF;
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			id_ = OLD.doc_divers;
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=OLD.id;	
		END IF;
		SELECT INTO montant_ SUM(y.montant) FROM yvs_compta_caisse_piece_divers y WHERE y.statut_piece = 'P' AND y.doc_divers = id_;
		SELECT INTO id_ y.bon FROM yvs_compta_justificatif_bon y WHERE piece = id_;
		if(id_ IS NOT NULL AND id_ > 0)THEN
			UPDATE yvs_compta_mouvement_caisse SET montant = montant - COALESCE(montant_, 0) WHERE table_externe='BON_PROVISOIRE' AND id_externe=id_;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_divers()
  OWNER TO postgres;



-- Function: compta_action_on_caisse_piece_ecart_vente()
-- DROP FUNCTION compta_action_on_caisse_piece_ecart_vente();
CREATE OR REPLACE FUNCTION compta_action_on_caisse_piece_ecart_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	echeance_ RECORD;
	ecart_ RECORD;
	
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	
	mouvement_ character varying default 'R';
	statut_ character varying default 'W';
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
	
BEGIN	
	IF(EXEC_) THEN
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		IF(action_='INSERT' OR action_='UPDATE') THEN
			id_el_ = NEW.id;
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
			id_el_ = OLD.id;
		END IF;
			
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  cr.agence, pv.numero as num_doc, pv.users, cr.nom_users as nom FROM yvs_com_ecart_entete_vente pv
				LEFT JOIN yvs_com_entete_doc_vente en ON en.id=pv.entete_doc INNER JOIN yvs_users cr ON cr.id=pv.users WHERE pv.id=NEW.piece; 									    
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
			END IF;
			SELECT INTO ecart_ * FROM yvs_com_ecart_entete_vente WHERE id = NEW.piece;
			id_tiers_=line_.users;
			IF(line_.nom IS NOT NULL) THEN
				nom_ = line_.nom;
			END IF;
			IF(ecart_.montant > 0)THEN
				mouvement_ = 'R';
			ELSE
				mouvement_ = 'D';
			END IF;
			IF(ecart_.statut = 'V')THEN
				statut_ = 'P';
			ELSE
				statut_ = 'W';
			END IF;
		END IF;         
		IF(action_='INSERT') THEN
			-- RÃ©cupÃ¨re le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total,numero_externe)
				VALUES (ecart_.numero, NEW.id, '', 'ECART_VENTE', ABS(ecart_.montant), line_.num_doc, ecart_.date_ecart, ecart_.date_ecart, ecart_.date_ecart, statut_, 
					line_.agence,NEW.author,NEW.caisse, ecart_.users, id_tiers_, null, COALESCE(mouvement_, 'R'), nom_, NEW.model, 0, 0, line_.num_doc);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='ECART_VENTE' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=ecart_.numero, note='', montant=ABS(ecart_.montant), tiers_interne=id_tiers_, model = NEW.model,
					reference_externe=line_.num_doc, date_mvt=ecart_.date_ecart, date_paiment_prevu=ecart_.date_ecart, date_paye=ecart_.date_ecart, 
					statut_piece=statut_, caisse=NEW.caisse, caissier=ecart_.users, mouvement=COALESCE(mouvement_, 'R'), agence=line_.agence, name_tiers=nom_,
					etape_total=0, etape_valide=0, numero_externe=line_.num_doc
				WHERE table_externe='ECART_VENTE' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total,numero_externe)
				VALUES (ecart_.numero, NEW.id, '', 'ECART_VENTE', ABS(ecart_.montant), line_.num_doc, ecart_.date_ecart, ecart_.date_ecart, ecart_.date_ecart, statut_, line_.agence,NEW.author,
					NEW.caisse, ecart_.users, id_tiers_, null, COALESCE(mouvement_, 'R'), nom_, NEW.model, 0, 0,line_.num_doc);
			END IF;	
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='ECART_VENTE' AND id_externe=OLD.id;	
			RETURN OLD;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_caisse_piece_ecart_vente()
  OWNER TO postgres;


-- Function: compta_insert_piece_mission()
-- DROP FUNCTION compta_insert_piece_mission();
CREATE OR REPLACE FUNCTION compta_insert_piece_mission()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	mission_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';

	avance_ double precision default 0;
	frais_ double precision default 0;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
	
BEGIN	
	IF(EXEC_) THEN	
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  e.agence, m.numero_mission, m.employe, e.nom, e.prenom FROM yvs_compta_caisse_piece_mission pm 
				INNER JOIN yvs_grh_missions m ON m.id=pm.mission INNER JOIN yvs_grh_employes e ON e.id = m.employe WHERE m.id=NEW.mission LIMIT 1; 
										
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
			END IF;
			IF(line_.nom IS NOT NULL) THEN
				nom_=line_.nom;
			END IF;
			IF(line_.prenom IS NOT NULL) THEN
				prenom_=line_.prenom;
			END IF;
			mission_ = NEW.mission;
		ELSE
			mission_ = OLD.mission;
		END IF;         
		IF(action_='INSERT') THEN
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, caissier, statut_piece, 
													agence, author, caisse, mouvement, tiers_employe, tiers_externe, name_tiers, model)
				   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'MISSION', NEW.montant,line_.numero_mission, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement, NEW.caissier, NEW.statut_piece, 
						   line_.agence,NEW.author, NEW.caisse, 'D',line_.employe,NULL, (nom_ ||' '||prenom_), NEW.model);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='MISSION' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, reference_externe=line_.numero_mission, date_mvt=NEW.date_piece, 
					date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', agence=line_.agence,
					tiers_employe=line_.employe, name_tiers=(nom_ ||' '||prenom_), model = NEW.model
					WHERE table_externe='MISSION' AND id_externe= NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, caissier, statut_piece, 
													agence, author, caisse, mouvement, tiers_employe, tiers_externe, name_tiers, model)
				   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'MISSION', NEW.montant,line_.numero_mission, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement, NEW.caissier, NEW.statut_piece, 
						   line_.agence,NEW.author, NEW.caisse, 'D',line_.employe,NULL,(nom_ ||' '||prenom_), NEW.model);
			END IF;
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='MISSION' AND id_externe=OLD.id;
		END IF;
		SELECT INTO frais_ SUM(y.montant) FROM yvs_grh_frais_mission y WHERE y.mission = mission_;
		SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_mission y WHERE y.statut_piece = 'P' AND y.mission = mission_;
		IF(COALESCE(avance_, 0) > 0)THEN
			IF(COALESCE(avance_, 0) >= COALESCE(frais_, 0))THEN
				UPDATE yvs_grh_missions SET statut_paiement = 'P' WHERE id = mission_;
			ELSE
				UPDATE yvs_grh_missions SET statut_paiement = 'R' WHERE id = mission_;
			END IF;
		ELSE
			UPDATE yvs_grh_missions SET statut_paiement = 'W' WHERE id = mission_;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_insert_piece_mission()
  OWNER TO postgres;


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
	agence_ BIGINT;
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
		
	--find agence concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO bulletin_ COUNT(y.id) FROM yvs_compta_notif_reglement_bulletin y WHERE y.piece = NEW.id;
		IF(COALESCE(bulletin_, 0) = 1 )THEN
			SELECT INTO line_  cu.agence, cl.ref_bulletin as num_doc, cu.compte_tiers as tiers, cu.nom, cu.prenom  FROM yvs_compta_caisse_piece_salaire pv INNER JOIN yvs_compta_notif_reglement_bulletin dv ON dv.piece= pv.id 
				INNER JOIN yvs_grh_bulletins cl ON cl.id=dv.bulletin INNER JOIN yvs_grh_contrat_emps en ON en.id=cl.contrat
				INNER JOIN yvs_grh_employes cu ON cu.id=en.employe WHERE pv.id=NEW.id;
			id_tiers_= line_.tiers;
			--récupère le code tièrs de ce clients
			IF(line_.nom IS NOT NULL) THEN
				nom_ = line_.nom;
			END IF;
			IF(line_.prenom IS NOT NULL) THEN
				prenom_ = line_.prenom;
			END IF;
			agence_ = line_.agence;
			numero_ = line_.num_doc;
		ELSE	
			SELECT INTO agence_ ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
		END IF;										    
		IF(agence_ IS NULL) THEN
			SELECT INTO agence_  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
		END IF;
		-- recherche les étapes de validation
		SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE piece = NEW.id;
		SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece_salaire WHERE piece = NEW.id AND phase_ok IS TRUE;
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_SALAIRE', NEW.montant, numero_, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				agence_,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, 'D', nom_ ||' '||prenom_, NEW.model, etape_valide_, etape_total_);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_SALAIRE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=numero_, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
				statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', agence=agence_, name_tiers=nom_ ||' '||prenom_,
				etape_total=etape_total_, etape_valide=etape_valide_
			WHERE table_externe='DOC_SALAIRE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_SALAIRE', NEW.montant, numero_, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, agence_,NEW.author,
				NEW.caisse, NEW.caissier,null, id_tiers_, 'D', nom_ ||' '||prenom_, NEW.model, etape_valide_, etape_total_);
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
	IF(EXEC_) THEN		
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
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
					etape_total=etape_total_, etape_valide=etape_valide_, numero_externe=NEW.reference_externe
					WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total,numero_externe)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.agence,NEW.author,
					NEW.caisse, NEW.caissier,null, id_tiers_, COALESCE(NEW.mouvement, 'R'), line_.nom_client, NEW.model, etape_valide_, etape_total_,NEW.reference_externe);
			END IF;	
			UPDATE yvs_compta_notif_reglement_vente SET date_update = current_date WHERE piece_vente = NEW.id;
			vente_ = NEW.vente;	           
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=OLD.id;		
			vente_ = OLD.vente;		    	 
		END IF;
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
		PERFORM action_in_header_vente_or_piece_virement(line_.entete_doc);
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


-- Function: compta_action_on_piece_caisse_virement()
-- DROP FUNCTION compta_action_on_piece_caisse_virement();
CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_virement()
  RETURNS trigger AS
$BODY$    
DECLARE
line_ RECORD;
user_ RECORD;
action_ character varying;
id_el_ bigint;
id_ bigint;
id_tiers_ bigint;
frais_ double precision default 0;
	
EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
	
BEGIN	
	IF(EXEC_) THEN	
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			--trouve le nom du caissier
			SELECT INTO user_ u.* FROM yvs_users u WHERE u.id=NEW.caissier_source;
			SELECT INTO line_  n.agence FROM yvs_base_caisse c INNER JOIN  yvs_compta_journaux n ON c.journal=n.id WHERE c.id=NEW.source;
			SELECT INTO frais_ SUM(y.montant) FROM yvs_compta_cout_sup_piece_virement y WHERE y.virement = NEW.id;
			id_ = NEW.id;
		ELSE
			id_ = OLD.id;
		END IF;
		FOR id_el_ IN SELECT y.entete_doc FROM yvs_compta_notif_versement_vente y WHERE piece = id_
		LOOP
			PERFORM action_in_header_vente_or_piece_virement(id_);
		END LOOP;
		frais_ = COALESCE(frais_, 0);
		IF(action_='INSERT') THEN
			-- InsÃ¨re la sortie
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', (NEW.montant + frais_), NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				line_.agence,NEW.author,NEW.source, NEW.caissier_source, NEW.caissier_source, null,'D', user_.nom_users, NEW.model);
			-- InsÃ¨re l'entrÃ©
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement,name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', NEW.montant, NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				line_.agence,NEW.author,NEW.cible, NEW.caissier_cible, NEW.caissier_source, null,'R', user_.nom_users, NEW.model);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=(NEW.montant + frais_), tiers_interne=NEW.caissier_source, model = NEW.model,
					reference_externe=NEW.numero_piece, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
					statut_piece=NEW.statut_piece, caisse=NEW.source, caissier=NEW.caissier_source, mouvement='D', agence=line_.agence, name_tiers=user_.nom_users
				WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id AND mouvement='D';
						  ---
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_interne=NEW.caissier_source, model = NEW.model,
					reference_externe=NEW.numero_piece, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
					statut_piece=NEW.statut_piece, caisse=NEW.cible, caissier=NEW.caissier_cible, mouvement='R', agence=line_.agence, name_tiers=user_.nom_users
				WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id AND mouvement='R';
			ELSE
				-- InsÃ¨re la sortie
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement,name_tiers, model)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', (NEW.montant + frais_), NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
					line_.agence,NEW.author,NEW.source, NEW.caissier_source, NEW.caissier_source, null,'D',user_.nom_users, NEW.model);
				-- InsÃ¨re l'entrÃ©
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', NEW.montant, NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
					line_.agence,NEW.author,NEW.cible, NEW.caissier_cible, NEW.caissier_source, null,'R',user_.nom_users, NEW.model);
			END IF;	           
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VIREMENT' AND id_externe=OLD.id;
			RETURN OLD;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_virement()
  OWNER TO postgres;


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
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  NEW.agence, NEW.numero, t.id as tiers, t.nom, t.prenom FROM yvs_base_tiers t INNER JOIN yvs_users_agence f ON f.id=t.author WHERE t.id=NEW.tiers; 
										
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
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
			SELECT INTO id_model_ m.id FROM yvs_base_mode_reglement m INNER JOIN yvs_agences a ON a.societe = m.societe WHERE a.id = line_.agence AND type_reglement = 'ESPECE' AND default_mode IS TRUE LIMIT 1;
			IF(id_model_ IS NULL OR id_model_ < 1)THEN
				SELECT INTO id_model_ m.id FROM yvs_base_mode_reglement m INNER JOIN yvs_agences a ON a.societe = m.societe WHERE a.id = line_.agence AND type_reglement = 'ESPECE' LIMIT 1;
			END IF;
		END IF;         
		IF(action_='INSERT' AND NEW.statut_justify != 'J') THEN
			-- RÃ©cupÃ¨re le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
				VALUES (NEW.numero, NEW.id, NEW.description, 'BON_PROVISOIRE', NEW.montant, line_.numero, NEW.date_bon, NEW.date_valider, NEW.date_payer,  NEW.statut_paiement, line_.agence, NEW.author, NEW.caisse, NEW.caissier, null, id_tiers_, 'D', nom_prenom_, id_model_);
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
					date_paiment_prevu=NEW.date_valider, date_paye=NEW.date_payer, statut_piece=NEW.statut_paiement, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', agence=line_.agence, name_tiers=nom_prenom_
					WHERE table_externe='BON_PROVISOIRE' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
					VALUES (NEW.numero, NEW.id, NEW.description, 'BON_PROVISOIRE', (NEW.montant - total_justif_), line_.numero, NEW.date_bon, NEW.date_bon, NEW.date_bon,  NEW.statut_paiement, line_.agence, NEW.author, NEW.caisse, NEW.caissier, null, id_tiers_, 'D', nom_prenom_, id_model_);
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
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  dp.agence, y.num_refrence, ua.users, y.client, cl.nom, cl.prenom , em.code_users AS caissier FROM yvs_compta_acompte_client y 
				INNER JOIN yvs_com_client cl ON y.client = cl.id INNER JOIN yvs_base_caisse ca ON y.caisse = ca.id LEFT JOIN yvs_grh_employes em ON ca.responsable = em.id
				INNER JOIN yvs_compta_journaux dp ON dp.id = ca.journal INNER JOIN yvs_users_agence ua ON ua.id = y.author WHERE y.id= NEW.id; 
				
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
			--rÃ©cupÃ¨re le code tiÃ¨rs de ce clients
			SELECT INTO id_tiers_ t.tiers FROM yvs_com_client t WHERE t.id= NEW.client;
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
			END IF;
			IF(line_.nom IS NOT NULL) THEN
				nom_=line_.nom;
			END IF;
			IF(line_.prenom IS NOT NULL) THEN
				prenom_=line_.prenom;
			END IF;
			-- recherche les Ã©tapes de validation
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_acompte_vente WHERE piece_vente= NEW.id;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_acompte_vente WHERE piece_vente= NEW.id AND phase_ok IS TRUE;
		END IF;         
		IF(action_='INSERT') THEN
			-- RÃ©cupÃ¨re le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,etape_total, etape_valide,numero_externe)
			VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_VENTE', NEW.montant, NEW.num_refrence, NEW.date_acompte,NEW.date_acompte, NEW.date_paiement,  NEW.statut, 
					line_.agence, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model,etape_total_,etape_valide_,NEW.reference_externe);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_VENTE' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_refrence, note=NEW.commentaire, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
					reference_externe=NEW.num_refrence, date_mvt=NEW.date_acompte, date_paiment_prevu=NEW.date_acompte, date_paye=NEW.date_paiement, 
					statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='R', agence=line_.agence, name_tiers=(nom_ ||' '||prenom_),
					etape_total=etape_total_, etape_valide=etape_valide_, numero_externe=NEW.reference_externe
				WHERE table_externe='ACOMPTE_VENTE' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,etape_total,etape_valide)
				VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_VENTE', NEW.montant, NEW.num_refrence, NEW.date_acompte, NEW.date_paiement, NEW.date_paiement,  NEW.statut, line_.agence,
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


-- Function: compta_action_on_acompte_fournisseur()
-- DROP FUNCTION compta_action_on_acompte_fournisseur();
CREATE OR REPLACE FUNCTION compta_action_on_acompte_fournisseur()
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
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  ag.agence, y.num_refrence, dp.users, y.fournisseur, cl.nom, cl.prenom , em.code_users AS caissier FROM yvs_compta_acompte_fournisseur y 
				INNER JOIN yvs_base_fournisseur cl ON y.fournisseur = cl.id INNER JOIN yvs_base_caisse ca ON y.caisse = ca.id LEFT JOIN yvs_grh_employes em ON ca.responsable = em.id
				LEFT JOIN yvs_users_agence dp ON dp.id = y.author INNER JOIN yvs_compta_journaux ag ON ag.id= ca.journal WHERE y.id= NEW.id; 
				
			IF(NEW.fournisseur IS NOT NULL) THEN
				id_tiers_=NEW.fournisseur;
			ELSE
				id_tiers_=line_.fournisseur;
			END IF;
			IF(NEW.statut='P') THEN 
				date_=NEW.date_acompte;
			ELSE
				date_=null;
			END IF;
			--rÃ©cupÃ¨re le code tiÃ¨rs de ce fournisseurs
			SELECT INTO id_tiers_ t.tiers FROM yvs_base_fournisseur t WHERE t.id= NEW.fournisseur;
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
			END IF;
			IF(line_.nom IS NOT NULL) THEN
				nom_=line_.nom;
			END IF;
			IF(line_.prenom IS NOT NULL) THEN
				prenom_=line_.prenom;
			END IF;
			-- recherche les Ã©tapes de validation
			SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_acompte_vente WHERE piece_vente= NEW.id;
			SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_acompte_vente WHERE piece_vente= NEW.id AND phase_ok IS TRUE;
		END IF;         
		IF(action_='INSERT') THEN
			-- RÃ©cupÃ¨re le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,etape_total, etape_valide,  numero_externe)
			VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_ACHAT', NEW.montant, NEW.num_refrence, NEW.date_acompte, NEW.date_acompte, NEW.date_paiement, NEW.statut, 
					line_.agence, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'D', (nom_ ||' '||prenom_), NEW.model,etape_total_,etape_valide_, NEW.reference_externe);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_ACHAT' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_refrence, note=NEW.commentaire, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
					reference_externe=NEW.num_refrence, date_mvt=NEW.date_acompte, date_paiment_prevu=NEW.date_acompte, date_paye=NEW.date_paiement, 
					statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='D', agence=line_.agence, name_tiers=(nom_ ||' '||prenom_),
					etape_total=etape_total_, etape_valide=etape_valide_, numero_externe=NEW.reference_externe
				WHERE table_externe='ACOMPTE_ACHAT' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,etape_total, etape_valide)
				VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_ACHAT', NEW.montant, NEW.num_refrence, NEW.date_acompte, NEW.date_paiement, NEW.date_paiement,  NEW.statut, line_.agence,
					NEW.author,NEW.caisse, line_.caissier,null, id_tiers_,'D', (nom_ ||' '||prenom_), NEW.model,etape_total_,etape_valide_);
			END IF;	
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_ACHAT' AND id_externe=OLD.id;		
			RETURN OLD;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_acompte_fournisseur()
  OWNER TO postgres;


-- Function: compta_action_on_notif_reglement_achat()
-- DROP FUNCTION compta_action_on_notif_reglement_achat();
CREATE OR REPLACE FUNCTION compta_action_on_notif_reglement_achat()
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
	--find agence concernÃ©
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_ ag.agence, dv.num_doc, dp.users, dv.fournisseur, cl.nom, cl.prenom, pv.numero_piece, pv.note, pv.montant, pv.date_piece, pv.date_paiment_prevu, pv.date_paiement, pv.statut_piece,
				ac.caisse, pv.caissier, pv.model, ac.fournisseur
			FROM yvs_compta_notif_reglement_achat y INNER JOIN yvs_compta_acompte_fournisseur ac ON y.acompte = ac.id
			INNER JOIN yvs_compta_caisse_piece_achat pv ON y.piece_achat = pv.id INNER JOIN yvs_com_doc_achats dv ON dv.id = pv.achat
			INNER JOIN yvs_base_fournisseur cl ON cl.id=dv.fournisseur INNER JOIN yvs_base_caisse ca ON ac.caisse = ca.id
			LEFT JOIN yvs_users_agence dp ON dp.id= y.author INNER JOIN yvs_compta_journaux ag ON ag.id=ca.journal WHERE y.id = NEW.id; 
		
		--rÃ©cupÃ¨re le code tiÃ¨rs de ce fournisseurs
		SELECT INTO id_tiers_ t.tiers FROM yvs_base_fournisseur t WHERE t.id= line_.fournisseur;
		IF(line_.agence IS NULL) THEN
			SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
		END IF;
		IF(line_.nom IS NOT NULL) THEN
			nom_=line_.nom;
		END IF;
		IF(line_.prenom IS NOT NULL) THEN
			prenom_=line_.prenom;
		END IF;
	END IF;         
	IF(action_='INSERT') THEN
		-- RÃ©cupÃ¨re le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (line_.numero_piece, NEW.id, line_.note, 'NOTIF_ACHAT', -line_.montant, line_.num_doc, line_.date_piece, line_.date_paiment_prevu, line_.date_paiement,  line_.statut_piece, 
				line_.agence, NEW.author, line_.caisse, line_.caissier, null, id_tiers_,'D', (nom_ ||' '||prenom_), line_.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='NOTIF_ACHAT' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=line_.numero_piece, note=line_.note, montant= -line_.montant, tiers_externe=id_tiers_, model = line_.model,
				reference_externe=line_.num_doc, date_mvt= line_.date_piece, date_paiment_prevu= line_.date_paiment_prevu, date_paye= line_.date_paiement, 
				statut_piece= line_.statut_piece, caisse= line_.caisse, caissier= line_.caissier, mouvement='D', agence=line_.agence, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='NOTIF_ACHAT' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (line_.numero_piece, NEW.id, line_.note, 'NOTIF_ACHAT', -line_.montant, line_.num_doc, line_.date_piece, line_.date_paiment_prevu, line_.date_paiement,  line_.statut_piece, line_.agence,NEW.author,
				line_.caisse, line_.caissier,null, id_tiers_,'D', (nom_ ||' '||prenom_), line_.model);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='NOTIF_ACHAT' AND id_externe=OLD.id;
		RETURN OLD;		
	END IF;
END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_notif_reglement_achat()
  OWNER TO postgres;


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
	--find agence concernÃ©
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


-- Function: compta_action_on_credit_fournisseur()
-- DROP FUNCTION compta_action_on_credit_fournisseur();
CREATE OR REPLACE FUNCTION compta_action_on_credit_fournisseur()
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
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			SELECT INTO line_  ag.agence, y.num_reference, dp.users, y.fournisseur, cl.nom, cl.prenom , em.code_users AS caissier, y.motif
				FROM yvs_compta_credit_fournisseur y INNER JOIN yvs_compta_reglement_credit_fournisseur cc ON cc.credit = y.id
				INNER JOIN yvs_base_fournisseur cl ON y.fournisseur = cl.id INNER JOIN yvs_base_caisse ca ON cc.caisse = ca.id LEFT JOIN yvs_grh_employes em ON ca.responsable = em.id
				LEFT JOIN yvs_users_agence dp ON dp.id = y.author INNER JOIN yvs_compta_journaux ag ON ag.id=ca.journal WHERE cc.id= NEW.id; 
				
			--rÃ©cupÃ¨re le code tiÃ¨rs de ce fournisseurs
			SELECT INTO id_tiers_ t.tiers FROM yvs_base_fournisseur t WHERE t.id= line_.fournisseur;
			IF(line_.agence IS NULL) THEN
				SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
			END IF;
			IF(line_.nom IS NOT NULL) THEN
				nom_=line_.nom;
			END IF;
			IF(line_.prenom IS NOT NULL) THEN
				prenom_=line_.prenom;
			END IF;
		END IF;         
		IF(action_='INSERT') THEN
			-- RÃ©cupÃ¨re le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero, NEW.id, line_.motif, 'CREDIT_ACHAT', NEW.valeur, line_.num_reference, NEW.date_reg, NEW.date_reg, NEW.date_paiement,  NEW.statut, 
					line_.agence, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'D', (nom_ ||' '||prenom_), NEW.model);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='CREDIT_ACHAT' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero, note=line_.motif, montant=NEW.valeur, tiers_externe=id_tiers_, model = NEW.model,
					reference_externe=line_.num_reference, date_mvt=NEW.date_reg, date_paiment_prevu=NEW.date_reg, date_paye=NEW.date_paiement, 
					statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='D', agence=line_.agence, name_tiers=(nom_ ||' '||prenom_)
				WHERE table_externe='CREDIT_ACHAT' AND id_externe=NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
						agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
				VALUES (NEW.numero, NEW.id, line_.motif, 'CREDIT_ACHAT', NEW.valeur, line_.num_reference, NEW.date_reg, NEW.date_reg, NEW.date_paiement,  NEW.statut, 
						line_.agence, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'D', (nom_ ||' '||prenom_), NEW.model);
			END IF;	
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='CREDIT_ACHAT' AND id_externe=OLD.id;	
			RETURN OLD;			
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_credit_fournisseur()
  OWNER TO postgres;


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
	--find agence concernÃ©
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.agence, y.num_reference, dp.users, y.client, cl.nom, cl.prenom , em.code_users AS caissier, y.motif
			FROM yvs_compta_credit_client y INNER JOIN yvs_compta_reglement_credit_client cc ON cc.credit = y.id
			INNER JOIN yvs_com_client cl ON y.client = cl.id INNER JOIN yvs_base_caisse ca ON cc.caisse = ca.id LEFT JOIN yvs_grh_employes em ON ca.responsable = em.id
			LEFT JOIN yvs_users_agence dp ON dp.id = y.author INNER JOIN yvs_compta_journaux ag ON ag.id=ca.journal WHERE cc.id= NEW.id; 
			
		--rÃ©cupÃ¨re le code tiÃ¨rs de ce clients
		SELECT INTO id_tiers_ t.tiers FROM yvs_com_client t WHERE t.id= line_.client;
		IF(line_.agence IS NULL) THEN
			SELECT INTO line_.agence  a.agence FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		IF(line_.nom IS NOT NULL) THEN
			nom_=line_.nom;
		END IF;
		IF(line_.prenom IS NOT NULL) THEN
			prenom_=line_.prenom;
		END IF;
	END IF;         
	IF(action_='INSERT') THEN
	    -- RÃ©cupÃ¨re le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
		VALUES (NEW.numero, NEW.id, line_.motif, 'CREDIT_VENTE', NEW.valeur, line_.num_reference, NEW.date_reg, NEW.date_reg, NEW.date_paiement,  NEW.statut, 
				line_.agence, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='CREDIT_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero, note=line_.motif, montant=NEW.valeur, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=line_.num_reference, date_mvt=NEW.date_reg, date_paiment_prevu=NEW.date_reg, date_paye=NEW.date_paiement, 
				statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='R', agence=line_.agence, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='CREDIT_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					agence, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero, NEW.id, line_.motif, 'CREDIT_VENTE', NEW.valeur, line_.num_reference, NEW.date_reg, NEW.date_reg, NEW.date_paiement,  NEW.statut, 
					line_.agence, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
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


-- Function: com_et_mouvement_stock_desc(bigint, character varying)
-- DROP FUNCTION com_et_mouvement_stock_desc(bigint, character varying);
CREATE OR REPLACE FUNCTION com_et_mouvement_stock_desc(id_externe_ bigint, table_externe_ character varying)
  RETURNS character varying AS
$BODY$
declare 
   name_ character varying;
   result_ character varying;   
   line_ record;   
begin 	
	CASE table_externe_ 
		WHEN 'yvs_com_ration' THEN
			SELECT INTO name_ concat(COALESCE(t.nom,''),' ', COALESCE(t.prenom,'')) FROM yvs_com_ration y INNER JOIN yvs_base_tiers t ON t.id=y.personnel WHERE y.id=id_externe_ LIMIT 1;
			result_='RATION DE '||name_;
		WHEN 'yvs_com_contenu_doc_achat' THEN
			SELECT INTO name_ concat(COALESCE(t.nom,''),' ', COALESCE(t.prenom,'')) FROM yvs_com_contenu_doc_achat y INNER JOIN yvs_com_doc_achats d ON d.id=y.doc_achat INNER JOIN yvs_base_fournisseur f ON f.id=d.fournisseur INNER JOIN yvs_base_tiers t ON t.id=f.tiers WHERE y.id=id_externe_ limit 1;
			result_='ACHAT CHEZ '||name_;
		WHEN 'yvs_com_contenu_doc_stock_reception' THEN
			SELECT INTO name_  u.nom_users FROM yvs_com_contenu_doc_stock_reception r INNER JOIN yvs_users_agence ua ON ua.id=r.author INNER JOIN yvs_users u ON u.id=ua.users WHERE r.id=id_externe_;
			result_='RECU PAR '||name_;
		WHEN 'yvs_prod_declaration_production' THEN
			SELECT INTO name_  u.nom_users FROM yvs_prod_declaration_production d INNER JOIN yvs_prod_session_of so ON so.id=d.session_of INNER JOIN yvs_prod_session_prod s ON so.session_prod=s.id INNER JOIN yvs_users u ON u.id=s.producteur WHERE d.id=id_externe_ limit 1;
			result_='PRODUCTION PAR '||name_;
		WHEN 'yvs_prod_of_suivi_flux' THEN
			SELECT INTO name_ u.nom_users FROM yvs_prod_of_suivi_flux  sf INNER JOIN yvs_prod_suivi_operations sp ON sp.id=sf.id_operation						   
						     INNER JOIN yvs_prod_session_of so ON so.id=sp.session_of INNER JOIN yvs_prod_session_prod s ON so.session_prod=s.id INNER JOIN yvs_users u ON u.id=s.producteur  where sf.id=id_externe_ limit 1;
			result_='CONSOMMATION MP PAR '||name_;
		WHEN 'yvs_com_contenu_doc_stock' THEN
			SELECT INTO line_ u.nom_users, type_doc, destination FROM yvs_com_contenu_doc_stock c INNER JOIN yvs_com_doc_stocks d ON d.id=c.doc_stock INNER JOIN yvs_users u ON u.id=d.valider_by WHERE c.id=id_externe_ limit 1;
			CASE line_.type_doc
				WHEN 'TR' THEN
					SELECT INTO name_ d.designation FROM yvs_base_depots d WHERE d.id=line_.destination;
					result_='TRANSFERT VERS'||name_||' PAR '||line_.nom_users;
				WHEN 'ES' THEN
					result_='ENTREE PAR '||line_.nom_users;
				WHEN 'SS' THEN		
					result_='SORTIE PAR '||line_.nom_users;
				WHEN 'IN' THEN	
					result_='AJUSTEMENT INVENTAIRE PAR '||line_.nom_users;				
				WHEN 'FT' THEN	
					result_='RECONDITIONNEMENT PAR '||line_.nom_users;
				ELSE
					result_ = '';
			END CASE;
		WHEN 'yvs_com_contenu_doc_vente' THEN
			SELECT INTO name_ u.nom_users FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_vente d ON d.id=c.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau INNER JOIN yvs_users u ON u.id=cu.users WHERE c.id=id_externe_ limit 1;
			result_='VENTE PAR '||name_;
		ELSE
			result_ = '';
	END CASE;
	RETURN result_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_et_mouvement_stock_desc(bigint, character varying)
  OWNER TO postgres;


-- Function: com_et_mouvement_stock_num(bigint, character varying)
-- DROP FUNCTION com_et_mouvement_stock_num(bigint, character varying);
CREATE OR REPLACE FUNCTION com_et_mouvement_stock_num(id_externe_ bigint, table_externe_ character varying)
  RETURNS character varying AS
$BODY$
declare 
   name_ character varying;
   result_ character varying;   
   line_ record;   
begin 	
	CASE table_externe_ 
		WHEN 'yvs_com_ration' THEN
			SELECT INTO name_ d.num_doc FROM yvs_com_ration y INNER JOIN yvs_com_doc_ration d ON d.id=y.doc_ration WHERE y.id=id_externe_ LIMIT 1;
		WHEN 'yvs_com_contenu_doc_achat' THEN
			SELECT INTO name_ d.num_doc FROM yvs_com_contenu_doc_achat y INNER JOIN yvs_com_doc_achats d ON d.id=y.doc_achat ;
		WHEN 'yvs_com_contenu_doc_stock_reception' THEN
			SELECT INTO name_  d.num_doc FROM yvs_com_contenu_doc_stock_reception r INNER JOIN yvs_com_contenu_doc_stock c ON c.id=r.contenu INNER JOIN yvs_com_doc_stocks d ON d.id=c.doc_stock WHERE r.id=id_externe_;
		WHEN 'yvs_prod_declaration_production' THEN
			SELECT INTO name_  o.code_ref FROM yvs_prod_declaration_production d INNER JOIN yvs_prod_session_of so ON so.id=d.session_of INNER JOIN yvs_prod_ordre_fabrication o ON so.ordre=o.id WHERE d.id=id_externe_ limit 1;
		WHEN 'yvs_prod_of_suivi_flux' THEN
		SELECT INTO name_  o.code_ref FROM yvs_prod_of_suivi_flux  sf INNER JOIN yvs_prod_suivi_operations sp ON sp.id=sf.id_operation						   
						     INNER JOIN yvs_prod_session_of so ON so.id=sp.session_of INNER JOIN  yvs_prod_ordre_fabrication o ON so.ordre=o.id WHERE sf.id=id_externe_ limit 1;
		WHEN 'yvs_com_contenu_doc_stock' THEN
			SELECT INTO name_ d.num_doc FROM yvs_com_contenu_doc_stock c INNER JOIN yvs_com_doc_stocks d ON d.id=c.doc_stock INNER JOIN yvs_users u ON u.id=d.valider_by WHERE c.id=id_externe_ limit 1;			
		WHEN 'yvs_com_contenu_doc_vente' THEN
			SELECT INTO name_ d.num_doc FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_vente d ON d.id=c.doc_vente INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau INNER JOIN yvs_users u ON u.id=cu.users WHERE c.id=id_externe_ limit 1;
		ELSE
			name_ = '';
	END CASE;
	RETURN name_;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_et_mouvement_stock_num(bigint, character varying)
  OWNER TO postgres;



-- Function: yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean)
-- DROP FUNCTION yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean);
CREATE OR REPLACE FUNCTION yvs_compta_content_journal(IN societe_ bigint, IN agences_ character varying, IN element_ bigint, IN table_ character varying, IN clear_ boolean)
  RETURNS TABLE(id bigint, jour integer, num_piece character varying, num_ref character varying, compte_general bigint, compte_tiers bigint, libelle character varying, debit double precision, credit double precision, echeance date, ref_externe bigint, table_externe character varying, statut character varying, error character varying, contenu bigint, centre bigint, coefficient double precision, numero integer, agence bigint, warning character varying, table_tiers character varying) AS
$BODY$
DECLARE
	_id_ BIGINT DEFAULT 0;
	_jour_ INTEGER DEFAULT 0; 
	_num_piece_ CHARACTER VARYING DEFAULT '';
	_num_ref_ CHARACTER VARYING DEFAULT ''; 
	_compte_general_ BIGINT; 
	_compte_tiers_ BIGINT; 
	_libelle_ CHARACTER VARYING DEFAULT ''; 
	_debit_ DOUBLE PRECISION DEFAULT 0; 
	_credit_ DOUBLE PRECISION DEFAULT 0; 
	_echeance_ DATE DEFAULT CURRENT_DATE; 
	_ref_externe_ BIGINT DEFAULT element_; 
	_table_externe_ CHARACTER VARYING DEFAULT table_; 
	_statut_ CHARACTER VARYING DEFAULT 'V';
	_error_ CHARACTER VARYING DEFAULT '';
	_warning_ CHARACTER VARYING DEFAULT '';
	_contenu_ BIGINT;
	_centre_ BIGINT;
	_coefficient_ DOUBLE PRECISION DEFAULT 0;
	_numero_ INTEGER DEFAULT 0;
	_agence_ BIGINT DEFAULT 0;
	_table_tiers_ CHARACTER VARYING DEFAULT '';

	ligne_ RECORD;
	sous_ RECORD;
	data_ RECORD;
	
	count_ BIGINT; 
	_compte_secondaire_ BIGINT; 
	
	taux_ DOUBLE PRECISION DEFAULT 100;
	total_ DOUBLE PRECISION DEFAULT 0; 
	somme_ DOUBLE PRECISION DEFAULT 0; 
	valeur_ DOUBLE PRECISION DEFAULT 0; 
	valeur_limite_arrondi_ DOUBLE PRECISION DEFAULT 0;
	
	i_ INTEGER DEFAULT 1;

	titre_ CHARACTER VARYING;
	ids_ CHARACTER VARYING DEFAULT '0';
BEGIN    
	IF(clear_)THEN
		DROP TABLE IF EXISTS table_compta_content_journal;
		CREATE TEMP TABLE IF NOT EXISTS table_compta_content_journal(_id BIGINT, _jour INTEGER, _num_piece CHARACTER VARYING, _num_ref CHARACTER VARYING, _compte_general BIGINT, _compte_tiers BIGINT, _libelle CHARACTER VARYING, _debit DOUBLE PRECISION, _credit DOUBLE PRECISION, _echeance DATE, _ref_externe BIGINT, _table_externe CHARACTER VARYING, _statut CHARACTER VARYING, _error character varying, _contenu bigint, _centre bigint, _coefficient double precision, _numero integer, _agence bigint, _warning character varying, _table_tiers character varying);
		DELETE FROM table_compta_content_journal;
	END IF;
	IF(table_ IS NOT NULL AND table_ NOT IN ('', ' '))THEN
		SELECT INTO valeur_limite_arrondi_ valeur_limite_arrondi FROM yvs_compta_parametre WHERE societe = societe_;
		valeur_limite_arrondi_ = COALESCE(valeur_limite_arrondi_, 0);
		IF(table_ = 'DOC_VENTE' OR table_ = 'AVOIR_VENTE')THEN	
			SELECT INTO data_ d.num_doc, d.nom_client, d.tiers, d.client, e.date_entete, c.compte, p.saisie_compte_tiers, u.agence FROM yvs_com_doc_ventes d LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_client c ON d.tiers = c.id 
				 LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_base_plan_comptable p ON c.compte = p.id WHERE d.id = element_;
			IF(data_.num_doc IS NOT NULL AND data_.num_doc NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				total_ = (SELECT get_ttc_vente(element_)); -- RECUPERATION DU TTC DE LA FACTURE
				IF(total_ IS NOT NULL AND total_ > 0)THEN	
					-- RECHERCHE DES ARTICLES SANS COMPTE POUR LA CATEGORIE DE LA FACTURE
					SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id
					WHERE d.id = element_ AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) < 1;
					IF(COALESCE(count_, 0) < 1)THEN
						-- RECHERCHE DES ARTICLES AVEC PLUS D'UN COMPTE POUR LA CATEGORIE DE LA FACTURE
						SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id
						WHERE d.id = element_ AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) > 1;
						IF(COALESCE(count_, 0) > 0)THEN
							_error_ = 'certains article sont rattaché a plusieurs comptes pour la catégorie de la facture';
						END IF;
					ELSE
						_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
					END IF;
					-- COMPTABILISATION DU TTC DE LA FACTURE
					IF(data_.client IS NULL OR data_.client < 1)THEN
						_error_ = 'cette facture de vente n''est pas rattachée à un client';
					ELSIF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette facture de vente n''est pas rattachée à un compte tiers';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette facture de vente est rattachée à un client qui n''a pas de compte collectif';
					END IF;
					_jour_ = to_char(data_.date_entete ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_entete;
					_compte_general_ = data_.compte;
					IF(data_.saisie_compte_tiers)THEN
						_compte_tiers_ = data_.tiers;
					ELSE
						_compte_tiers_ = null;
					END IF;
					_numero_ = 1;
					FOR ligne_ IN SELECT t.* FROM yvs_base_tranche_reglement t INNER JOIN yvs_base_model_reglement m ON t.model = m.id INNER JOIN yvs_com_doc_ventes d ON d.model_reglement = m.id WHERE d.id = element_ ORDER BY t.numero
					LOOP
						_debit_ = 0;
						_credit_ = 0;
						IF(table_ = 'DOC_VENTE')THEN
							_debit_ = (total_ * ligne_.taux / 100);
							somme_ = somme_ + _debit_;
							_libelle_ = UPPER('Echéancier '||ligne_.taux||'% pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);
						ELSE	
							_credit_ = (total_ * ligne_.taux / 100);
							somme_ = somme_ + _credit_;
							_libelle_ = UPPER('Echéancier '||ligne_.taux||'% pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_echeance_ = _echeance_ + ligne_.interval_jour;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;
					IF(total_ > somme_)THEN
						_debit_ = 0;
						_credit_ = 0;
						IF(somme_ > 0)THEN
							taux_ = (((total_ - somme_) / total_) * 100);
						END IF;
						IF(table_ = 'DOC_VENTE')THEN
							_debit_ = (total_ - somme_);
							_libelle_ = UPPER('Echéancier '||taux_||'% pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);
						ELSE	
							_credit_ = (total_ - somme_);
							_libelle_ = UPPER('Echéancier '||taux_||'% pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
					
					_error_ = null;
					_numero_ = 2;
					_echeance_ = data_.date_entete;
					 -- COMPTE DE RISTOURNE SUR VENTE
					SELECT INTO _compte_secondaire_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = 'RISTOURNE_VENTE';
					-- COMPTABILISATION DES ARTICLES
					FOR ligne_ IN SELECT  SUM(co.prix_total) AS total, SUM(co.ristourne) AS ristourne, pc.id AS compte, pc.intitule, pc.saisie_compte_tiers FROM yvs_com_contenu_doc_vente co INNER JOIN yvs_com_doc_ventes dv ON co.doc_vente = dv.id
						INNER JOIN yvs_base_article_categorie_comptable ca ON ca.article = co.article AND ca.categorie = dv.categorie_comptable LEFT JOIN yvs_base_plan_comptable pc ON ca.compte = pc.id 
						WHERE dv.id = element_ GROUP BY pc.id
					LOOP
						_debit_ = 0;					
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _credit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_vente tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_vente tc INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente 
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE ca.compte = ligne_.compte AND dv.id = element_);
						IF(table_ = 'DOC_VENTE')THEN
							_credit_ = ligne_.total - COALESCE(_credit_, 0);
							_libelle_ = UPPER(ligne_.intitule||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);	
							IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
								_credit_ = _credit_ - ligne_.ristourne;
							END IF;
						ELSE	
							_debit_ = ligne_.total - COALESCE(_credit_, 0);
							_libelle_ = UPPER(ligne_.intitule||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);	
							_credit_ = 0;		
							IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
								_debit_ = _debit_ - ligne_.ristourne;
							END IF;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						IF(ligne_.saisie_compte_tiers)THEN
							_compte_tiers_ = data_.tiers;
						ELSE
							_compte_tiers_ = null;
						END IF;					
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;

						IF(COALESCE(ligne_.ristourne, 0) > 0)THEN
							IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
								_credit_ = 0;
								_debit_ = 0;
								IF(table_ = 'DOC_VENTE')THEN
									_credit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								ELSE	
									_debit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne restituée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								END IF;
								IF(i_ < 10)THEN
									_num_ref_ = data_.num_doc ||'-0'||i_;
								ELSE
									_num_ref_ = data_.num_doc ||'-'||i_;
								END IF;
								_compte_general_ = _compte_secondaire_;
								_compte_tiers_ = null;		
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
							ELSE
								_credit_ = 0;
								_debit_ = 0;
								IF(table_ = 'DOC_VENTE')THEN
									_debit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								ELSE	
									_credit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne restituée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								END IF;
								IF(i_ < 10)THEN
									_num_ref_ = data_.num_doc ||'-0'||i_;
								ELSE
									_num_ref_ = data_.num_doc ||'-'||i_;
								END IF;
								_compte_tiers_ = null;		
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
								
								_credit_ = 0;
								_debit_ = 0;
								IF(table_ = 'DOC_VENTE')THEN
									_credit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								ELSE	
									_debit_ = ligne_.ristourne;
									_libelle_ = UPPER('Ristourne restituée sur marchandises N° '||data_.num_doc||' de '||data_.nom_client);
								END IF;
								IF(i_ < 10)THEN
									_num_ref_ = data_.num_doc ||'-0'||i_;
								ELSE
									_num_ref_ = data_.num_doc ||'-'||i_;
								END IF;
								_compte_general_ = data_.compte;
								_compte_tiers_ = data_.tiers;	
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
							END IF;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT sum(tc.montant) AS total, tx.compte, tx.designation, pc.saisie_compte_tiers FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_vente tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						LEFT JOIN yvs_base_plan_comptable pc ON tx.compte = pc.id WHERE dv.id = element_ GROUP BY tx.compte, tx.designation, pc.id 
					LOOP
						IF(ligne_.total != 0)THEN
							_debit_ = 0;
							_credit_ = 0;
							IF(table_ = 'DOC_VENTE')THEN
								_credit_ = ligne_.total;
								_libelle_ = ligne_.designation||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;	
							ELSE	
								_credit_ = ligne_.total;
								_libelle_ = ligne_.designation||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client;	
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;					
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation, pc.saisie_compte_tiers FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_vente co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						LEFT JOIN yvs_base_plan_comptable pc ON tx.compte = pc.id WHERE dv.id = element_ GROUP BY tx.id, tx.libelle, pc.id 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(table_ = 'DOC_VENTE')THEN
								IF(ligne_.augmentation)THEN
									_credit_ = ligne_.total;
								ELSE
									_debit_ = ligne_.total;
								END IF;
								_libelle_ = ligne_.libelle||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;
							ELSE	
								IF(ligne_.augmentation)THEN
									_debit_ = ligne_.total;
								ELSE
									_credit_ = ligne_.total;
								END IF;
								_libelle_ = ligne_.libelle||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							IF(ligne_.saisie_compte_tiers)THEN
								_compte_tiers_ = data_.tiers;
							ELSE
								_compte_tiers_ = null;
							END IF;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'JOURNAL_VENTE')THEN
			SELECT INTO data_ u.code_users, e.date_entete, p.code AS point, u.agence FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id 
				LEFT JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id LEFT JOIN yvs_base_point_vente p ON cp.point = p.id 
				WHERE e.id = element_;
			IF(data_.code_users IS NOT NULL AND data_.code_users NOT IN ('', ' '))THEN		
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;
				SELECT INTO count_ (d.id) FROM yvs_com_doc_ventes d WHERE d.entete_doc = element_ AND d.type_doc = 'FV' AND d.statut = 'V';	
				IF(count_ IS NOT NULL AND count_ > 0)THEN
					-- RECHERCHE DES ARTICLES SANS COMPTE POUR LA CATEGORIE DES FACTURES
					SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id
					WHERE d.entete_doc = element_ AND d.type_doc = 'FV' AND d.statut = 'V' AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) < 1;
					IF(COALESCE(count_, 0) < 1)THEN
						-- RECHERCHE DES ARTICLES AVEC PLUS D'UN COMPTE POUR LA CATEGORIE DES FACTURES
						SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id
						WHERE d.entete_doc = element_ AND d.type_doc = 'FV' AND d.statut = 'V' AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) > 1;
						IF(COALESCE(count_, 0) > 0)THEN
							_error_ = 'ce journal de vente est rattaché à des articles qui ont plusieurs comptes';
						END IF;
					ELSE
						_error_ = 'ce journal de vente est rattaché à des articles qui n''ont pas de compte';
					END IF;
					-- COMPTABILISATION DU TTC DE LA JOURNEE DE VENTE
					_jour_ = to_char(data_.date_entete ,'dd')::integer;
					_num_piece_ = data_.code_users || '/';
					IF(data_.point IS NOT NULL AND data_.point NOT IN ('', ' '))THEN
						_num_piece_ = _num_piece_ || data_.point || '/';
					ELSE
						_error_ = 'ce journal de vente n'' pas rattaché à un point de vente';
					END IF;
					_numero_ = 1;
					_num_piece_ = _num_piece_ || to_char(data_.date_entete ,'ddMMyy');	
					_echeance_ = data_.date_entete;
					 -- COMPTE DE RISTOURNE SUR VENTE
					SELECT INTO _compte_secondaire_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = 'RISTOURNE_VENTE';
					FOR ligne_ IN SELECT SUM(get_ttc_vente(dv.id)) as total , ts.compte, dv.tiers, CONCAT(ts.nom, ' ', ts.prenom) AS nom, pc.saisie_compte_tiers, SUM((SELECT SUM(co.ristourne) FROM yvs_com_contenu_doc_vente co WHERE co.doc_vente = dv.id)) AS ristourne 
						FROM yvs_com_doc_ventes dv INNER JOIN yvs_com_client ts ON dv.tiers = ts.id LEFT JOIN yvs_base_plan_comptable pc ON ts.compte = pc.id LEFT JOIN yvs_compta_content_journal_facture_vente fc ON dv.id = fc.facture
						LEFT JOIN yvs_compta_content_journal cj ON dv.id = cj.ref_externe AND cj.table_externe = 'DOC_VENTE'
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_ AND fc.id IS NULL AND cj.id IS NULL 
						GROUP BY dv.entete_doc, dv.tiers, ts.id, pc.id
					LOOP
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains client n''ont pas de compte collectif';
						END IF;
						_debit_ = ligne_.total;
						_credit_ = 0;
						somme_ = somme_ + _debit_;
						_libelle_ = 'Echéancier 100% pour le journal de vente N° '||_num_piece_;
						IF(i_ < 10)THEN
							_num_ref_ = _num_piece_ ||'-0'||i_;
						ELSE
							_num_ref_ = _num_piece_ ||'-'||i_;
						END IF;
						_compte_general_ = ligne_.compte;
						IF(ligne_.saisie_compte_tiers)THEN
							_compte_tiers_ = ligne_.tiers;
						ELSE
							_compte_tiers_ = null;
						END IF;
						_id_ = _id_ - 1;	
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
						
						IF(COALESCE(ligne_.ristourne, 0) > 0)THEN
							IF(COALESCE(_compte_secondaire_, 0) < 1)THEN	
								_debit_ = 0;
								_credit_ = ligne_.ristourne;
								_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||_num_piece_);
								IF(i_ < 10)THEN
									_num_ref_ = _num_piece_ ||'-0'||i_;
								ELSE
									_num_ref_ = _num_piece_ ||'-'||i_;
								END IF;
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
							END IF;
						END IF;
					END LOOP;

					_error_ = null;
					_numero_ = 2;
					-- COMPTABILISATION DES ARTICLES
					FOR ligne_ IN SELECT SUM(co.prix_total) AS total, SUM(co.ristourne) AS ristourne, pc.id AS compte, pc.intitule, pc.saisie_compte_tiers FROM yvs_com_contenu_doc_vente co INNER JOIN yvs_com_doc_ventes dv ON co.doc_vente = dv.id
						INNER JOIN yvs_base_article_categorie_comptable ca ON ca.article = co.article AND ca.categorie = dv.categorie_comptable
						LEFT JOIN yvs_base_plan_comptable pc ON ca.compte = pc.id LEFT JOIN yvs_compta_content_journal_facture_vente fc ON dv.id = fc.facture
						LEFT JOIN yvs_compta_content_journal cj ON dv.id = cj.ref_externe AND cj.table_externe = 'DOC_VENTE'
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_ AND fc.id IS NULL AND cj.id IS NULL 
						GROUP BY pc.id
					LOOP
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _credit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_vente tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_vente tc INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente 
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article LEFT JOIN yvs_compta_content_journal_facture_vente fc ON dv.id = fc.facture
								LEFT JOIN yvs_compta_content_journal cj ON dv.id = cj.ref_externe AND cj.table_externe = 'DOC_VENTE'
								WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND ca.compte = ligne_.compte AND dv.entete_doc = element_ AND fc.id IS NULL AND cj.id IS NULL);
						_credit_ = ligne_.total - COALESCE(_credit_, 0);
						IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
							_credit_ = _credit_ - ligne_.ristourne;
						END IF;
						_debit_ = 0;
						IF(i_ < 10)THEN
							_num_ref_ = _num_piece_ ||'-0'||i_;
						ELSE
							_num_ref_ = _num_piece_ ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = null;
						_libelle_ = ligne_.intitule||' pour le journal de vente N° '||_num_piece_;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;

						IF(COALESCE(ligne_.ristourne, 0) > 0)THEN
							IF(COALESCE(_compte_secondaire_, 0) > 0)THEN
								_credit_ = ligne_.ristourne;
								_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||_num_piece_);
								IF(i_ < 10)THEN
									_num_ref_ = _num_piece_ ||'-0'||i_;
								ELSE
									_num_ref_ = _num_piece_ ||'-'||i_;
								END IF;
								_compte_general_ = _compte_secondaire_;
								_compte_tiers_ = null;		
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
							ELSE
								_credit_ = 0;
								_debit_ = ligne_.ristourne;
								_libelle_ = UPPER('Ristourne accordée sur marchandises N° '||_num_piece_);
								IF(i_ < 10)THEN
									_num_ref_ = _num_piece_ ||'-0'||i_;
								ELSE
									_num_ref_ = _num_piece_ ||'-'||i_;
								END IF;		
								_id_ = _id_ - 1;
								
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
								i_ = i_ + 1;
							END IF;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT SUM(tc.montant) AS total, tx.compte, tx.designation FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_vente tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id 
						INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente LEFT JOIN yvs_compta_content_journal_facture_vente fc ON dv.id = fc.facture
						LEFT JOIN yvs_compta_content_journal cj ON (dv.id = cj.ref_externe AND cj.table_externe = 'DOC_VENTE')
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_ AND fc.id IS NULL AND cj.id IS NULL 
						GROUP BY tx.compte, tx.designation 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = ligne_.total;
							_debit_ = 0;
							IF(i_ < 10)THEN
								_num_ref_ = _num_piece_ ||'-0'||i_;
							ELSE
								_num_ref_ = _num_piece_ ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.designation||' pour le journal de vente N° '||_num_piece_;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT SUM(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_vente co ON co.type_cout = tx.id 
						INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente LEFT JOIN yvs_compta_content_journal_facture_vente fc ON dv.id = fc.facture
						LEFT JOIN yvs_compta_content_journal cj ON dv.id = cj.ref_externe AND cj.table_externe = 'DOC_VENTE'
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_ AND fc.id IS NULL AND cj.id IS NULL 
						GROUP BY tx.id, tx.libelle 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(ligne_.augmentation)THEN
								_credit_ = ligne_.total;
								_numero_ = 2;
							ELSE
								_debit_ = ligne_.total;
								_numero_ = 1;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = _num_piece_ ||'-0'||i_;
							ELSE
								_num_ref_ = _num_piece_ ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.libelle||' pour le journal de vente N° '||_num_piece_;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'DOC_ACHAT' OR table_ = 'AVOIR_ACHAT')THEN
			SELECT INTO data_ d.num_doc, d.date_doc, d.fournisseur, CONCAT(c.nom, ' ', c.prenom) AS nom, c.compte, d.fournisseur as tiers, pc.saisie_compte_tiers, d.agence, COALESCE(d.description, '') AS description FROM yvs_com_doc_achats d LEFT JOIN yvs_base_fournisseur c ON d.fournisseur = c.id LEFT JOIN yvs_base_plan_comptable pc ON c.compte = pc.id WHERE d.id = element_;
			IF(data_.num_doc IS NOT NULL AND data_.num_doc NOT IN ('', ' '))THEN		
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;			
				total_ = (SELECT get_ttc_achat(element_)); -- RECUPERATION DU TTC DE LA FACTURE
				IF(total_ IS NOT NULL AND total_ > 0)THEN
					-- RECHERCHE DES ARTICLES SANS COMPTE POUR LA CATEGORIE DE LA FACTURE
					SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_achat c INNER JOIN yvs_com_doc_achats d ON c.doc_achat = d.id
						WHERE d.id = element_ AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) < 1;
					IF(COALESCE(count_, 0) < 1)THEN
						-- RECHERCHE DES ARTICLES AVEC PLUS D'UN COMPTE POUR LA CATEGORIE DE LA FACTURE
						SELECT INTO count_ COUNT(DISTINCT c.article) FROM yvs_com_contenu_doc_achat c INNER JOIN yvs_com_doc_achats d ON c.doc_achat = d.id
							WHERE d.id = element_ AND (SELECT COUNT(ca.id) FROM yvs_base_article_categorie_comptable ca 
									WHERE ca.article = c.article AND ca.categorie = d.categorie_comptable) > 1;
						IF(COALESCE(count_, 0) > 0)THEN
							_error_ = 'certains article sont rattaché a plusieurs comptes pour la catégorie de la facture';
						END IF;
					ELSE
						_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
					END IF;	
					-- COMPTABILISATION DU TTC DE LA FACTURE
					IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
						_error_ = 'cette facture d''achat n''est pas rattachée à un fournisseur';
					ELSIF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette facture d''achat est rattachée à un fournisseur qui n''a pas de compte tiers';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette facture d''achat est rattachée à un fournisseur qui n''a pas de compte collectif';
					END IF;
					_jour_ = to_char(data_.date_doc ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_doc;
					_compte_general_ = data_.compte;
					_libelle_ = data_.description;
					IF(data_.saisie_compte_tiers)THEN
						_compte_tiers_ = data_.tiers;
					ELSE
						_compte_tiers_ = null;
					END IF;
					_numero_ = 2;
					FOR ligne_ IN SELECT t.* FROM yvs_base_tranche_reglement t INNER JOIN yvs_base_model_reglement m ON t.model = m.id INNER JOIN yvs_com_doc_achats d ON d.model_reglement = m.id WHERE d.id = element_ ORDER BY t.numero
					LOOP
						_debit_ = 0;
						_credit_ = 0;
						IF(table_ = 'DOC_ACHAT')THEN
							_credit_ = (SELECT arrondi(societe_, (total_ * ligne_.taux / 100)));
							somme_ = somme_ + _credit_;
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||ligne_.taux||'% pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE	
							_debit_ = (SELECT arrondi(societe_, (total_ * ligne_.taux / 100)));
							somme_ = somme_ + _debit_;
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||ligne_.taux||'% pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_echeance_ = _echeance_ + ligne_.interval_jour;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;
					IF(total_ > somme_)THEN
						_debit_ = 0;
						_credit_ = 0;
						IF(somme_ > 0)THEN
							taux_ = (((total_ - somme_) / total_) * 100);
						END IF;
						IF(table_ = 'DOC_ACHAT')THEN
							_credit_ = (SELECT arrondi(societe_, (total_ - somme_)));
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||taux_||'% pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE	
							_debit_ = (SELECT arrondi(societe_, (total_ - somme_)));
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||taux_||'% pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;

					_error_ = null;
					_numero_ = 1;
					_echeance_ = data_.date_doc;
					-- COMPTABILISATION DES ARTICLES
					FOR ligne_ IN SELECT  SUM(co.prix_total) AS total, pc.id AS compte, pc.intitule, pc.saisie_compte_tiers, pc.saisie_analytique FROM yvs_com_contenu_doc_achat co INNER JOIN yvs_com_doc_achats dv ON co.doc_achat = dv.id
						INNER JOIN yvs_base_article_categorie_comptable ca ON ca.article = co.article AND ca.categorie = dv.categorie_comptable LEFT JOIN yvs_base_plan_comptable pc ON ca.compte = pc.id 
						WHERE dv.id = element_ GROUP BY pc.id
					LOOP
						_credit_ = 0;
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _debit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_achat tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_achat tc INNER JOIN yvs_com_contenu_doc_achat co ON tc.contenu = co.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE ca.compte = ligne_.compte AND dv.id = element_);
						IF(table_ = 'DOC_ACHAT')THEN
							_debit_ = ligne_.total - COALESCE(_debit_, 0);
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = ligne_.intitule||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE
							_credit_ = ligne_.total - COALESCE(_debit_, 0);
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = ligne_.intitule||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
							_debit_ = 0;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = null;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
						IF(table_ = 'DOC_ACHAT')THEN
							valeur_ = _debit_;
						ELSE
							valeur_ = _credit_;
						END IF;
						IF(ligne_.saisie_analytique IS TRUE AND (valeur_ IS NOT NULL AND valeur_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
							_contenu_ = _id_;
							RAISE NOTICE 'valeur_ : %',valeur_;
							RAISE NOTICE '_compte_general_ : %',_compte_general_;
							FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, 0, 0, _compte_general_, null, valeur_, '', table_, FALSE, FALSE)
							LOOP
								_error_ = sous_.error;
								_centre_ = sous_.centre;
								_coefficient_ = sous_.coefficient;
								IF(table_ = 'DOC_ACHAT')THEN
									_debit_ = sous_.valeur;
								ELSE
									_credit_ = sous_.valeur;
								END IF;
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
							END LOOP;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT sum(tc.montant) AS total, tx.compte, tx.designation FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_achat tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_achat co ON tc.contenu = co.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
						WHERE dv.id = element_ GROUP BY tx.compte, tx.designation 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(table_ = 'DOC_ACHAT')THEN
								_debit_ = ligne_.total;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.designation||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							ELSE
								_credit_ = ligne_.total;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.designation||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_achat co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
						WHERE dv.id = element_ GROUP BY tx.id, tx.libelle 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(table_ = 'DOC_ACHAT')THEN
								IF(ligne_.augmentation IS TRUE)THEN
									_debit_ = ligne_.total;
									_numero_ = 1;
								ELSE
									_credit_ = ligne_.total;
									_numero_ = 2;
								END IF;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.libelle||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							ELSE
								IF(ligne_.augmentation IS TRUE)THEN
									_credit_ = ligne_.total;
									_numero_ = 2;
								ELSE
									_debit_ = ligne_.total;
									_numero_ = 1;
								END IF;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.libelle||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'FRAIS_MISSION')THEN
			SELECT INTO data_ f.id, f.numero_piece, f.date_paiement, f.montant, c.journal, c.compte, o.compte_charge, m.ordre, m.objet_mission, m.numero_mission, e.agence, p.saisie_analytique, f.caisse FROM yvs_compta_caisse_piece_mission f LEFT JOIN yvs_grh_missions m ON f.mission = m.id 
				LEFT JOIN yvs_grh_employes e ON m.employe = e.id LEFT JOIN yvs_grh_objets_mission o ON m.objet_mission = o.id LEFT JOIN yvs_base_plan_comptable p ON o.compte_charge = p.id LEFT JOIN yvs_base_caisse c ON f.caisse = c.id WHERE f.id = element_;	
			_table_tiers_ = 'EMPLOYE';	
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero_mission;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.ordre;
				_compte_tiers_ = null;
				
				IF(data_.objet_mission IS NULL OR data_.objet_mission < 1)THEN
					_error_ = 'ce reglement est lié à une mission qui n''a pas d''objet de mission';
				ELSIF(data_.compte_charge IS NULL OR data_.compte_charge < 1)THEN
					_error_ = 'ce reglement est lié à une mission dont l''objet de mission n''a pas de compte de charge';
				END IF;
				_credit_ = 0;
				_debit_ = data_.montant;
				_numero_ = 1;
				_compte_general_ = data_.compte_charge;	
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
				valeur_ = _debit_;
				IF(data_.saisie_analytique IS TRUE AND (valeur_ IS NOT NULL AND valeur_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
					_contenu_ = _id_;
					FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, 0, 0, _compte_general_, null, valeur_, '', table_, FALSE, FALSE)
					LOOP
						_error_ = sous_.error;
						_centre_ = sous_.centre;
						_coefficient_ = sous_.coefficient;
						_debit_ = sous_.valeur;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
					END LOOP;
				END IF;

				_error_ = null;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est pas lié à une caisse';
				ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'ce reglement est lié à une caisse qui n''a pas de compte';
				END IF;
				_credit_ = data_.montant;
				_numero_ = 2;
				_debit_ = 0;
				_compte_general_ = data_.compte;	
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, y.caisse, p.vente, d.num_doc, d.client, t.compte as compte_tiers, t.suivi_comptable, d.tiers, m.compte_tiers AS tiers_interne, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, u.agence,
				r.libelle, r.mode_reglement, r.code_comptable, c.intitule, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.numero_piece) AS reference_externe FROM yvs_compta_phase_piece y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_vente p ON y.piece_vente = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_doc_ventes d ON p.vente = d.id LEFT JOIN yvs_com_client t ON d.client = t.id LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
				LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_grh_employes m ON u.id = m.code_users WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.vente IS NULL OR data_.vente < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune facture';
				ELSE
					_libelle_ = _libelle_|| ' du client '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece p ON p.phase_reg = r.id WHERE p.piece_vente = data_.id;
				RAISE NOTICE '%',data_.numero_phase;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune caisse';
				ELSE
					IF(data_.numero_phase = ligne_.max)THEN
						IF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;					
					ELSE
						IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
							_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
						ELSE
							_compte_general_ = data_.compte_caisse;
						END IF;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.vente IS NOT NULL AND data_.vente > 0)THEN
						IF(data_.client IS NULL OR data_.client < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de client';
						ELSE
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_vente v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_vente p ON y.piece_vente = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece _y INNER JOIN yvs_compta_caisse_piece_vente _p ON _y.piece_vente = _p.id INNER JOIN yvs_compta_phase_piece _y0 ON _y0.piece_vente = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
					IF(data_.suivi_comptable)THEN
						IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
							_error_ = 'car ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte tiers';
						ELSE
							_compte_tiers_ = data_.tiers;
						END IF;
					ELSE
						IF(data_.tiers_interne IS NULL OR data_.tiers_interne < 1)THEN
							_error_ = 'car ce reglement est rattaché à une facture qui est liée à un vendeur qui n''a pas de compte tiers';
						ELSE
							_compte_tiers_ = data_.tiers_interne;
						END IF;
					END IF;		
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, y.caisse, p.achat, d.num_doc, d.fournisseur, t.compte as compte_tiers, d.fournisseur as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, d.agence,
				r.libelle, r.mode_reglement, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.numero_piece) AS reference_externe FROM yvs_compta_phase_piece_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_achat p ON y.piece_achat = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_doc_achats d ON p.achat = d.id LEFT JOIN yvs_base_fournisseur t ON d.fournisseur = t.id WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.achat IS NULL OR data_.achat < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune facture';
				ELSE
					_libelle_ = _libelle_||' du fournisseur '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece_achat p ON p.phase_reg = r.id WHERE p.piece_achat = data_.id;
				RAISE NOTICE 'data_.numero_phase %',data_.numero_phase;
				RAISE NOTICE 'ligne_.max %',ligne_.max;
				RAISE NOTICE 'ligne_.min %',ligne_.min;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune caisse';
				ELSE
					IF(data_.numero_phase = ligne_.min)THEN
						IF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;					
					ELSE
						IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
							_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
						ELSE
							_compte_general_ = data_.compte_caisse;
						END IF;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 2;
					_credit_ = data_.montant;	
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.achat IS NOT NULL AND data_.achat > 0)THEN
						IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de fournisseur';
						ELSE
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_achat v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_achat p ON y.piece_achat = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece_achat _y INNER JOIN yvs_compta_caisse_piece_achat _p ON _y.piece_achat = _p.id INNER JOIN yvs_compta_phase_piece_achat _y0 ON _y0.piece_achat = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'car ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;	
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE			
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN
			SELECT INTO data_ p.id, p.num_piece as numero_piece, p.date_valider as date_paiement, p.montant, y.caisse, p.doc_divers, d.num_piece as num_doc, d.id_tiers as tiers, name_tiers(d.id_tiers, d.table_tiers, 'C')::integer as compte_tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, d.table_tiers, d.agence,
				r.libelle, r.mode_reglement, y.statut, d.mouvement, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, name_tiers(d.id_tiers, d.table_tiers, 'N') AS nom_prenom,
				COALESCE(p.reference_externe, p.num_piece) AS reference_externe FROM yvs_compta_phase_piece_divers y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_divers p ON y.piece_divers = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_compta_caisse_doc_divers d ON p.doc_divers = d.id WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = data_.table_tiers;		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.doc_divers IS NULL OR data_.doc_divers < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune operation diverse';
				ELSIF(data_.nom_prenom NOT IN ('', ' '))THEN
					_libelle_ = _libelle_||' du tiers '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min, 0 as tmp FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece_divers p ON p.phase_reg = r.id WHERE p.piece_divers = data_.id;
				IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
					ligne_.tmp = ligne_.max;
					ligne_.max = ligne_.min;
					ligne_.min = ligne_.tmp;
				END IF;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune caisse';
					ELSIF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 2;
						_credit_ = data_.montant;
					ELSE
						_numero_ = 1;
						_debit_ = data_.montant;
					END IF;
				ELSE		
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 1;
						_debit_ = data_.montant;
					ELSE
						_numero_ = 2;
						_credit_ = data_.montant;
					END IF;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.doc_divers IS NOT NULL AND data_.doc_divers > 0)THEN
						IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
							_error_ = 'ce reglement est rattaché à une operation diverse d''un tiers qui n''a pas de compte collectif';
						ELSE
							_compte_general_ = data_.compte_tiers;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece_divers y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_divers v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_divers p ON y.piece_divers = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece_divers _y INNER JOIN yvs_compta_caisse_piece_divers _p ON _y.piece_divers = _p.id INNER JOIN yvs_compta_phase_piece_divers _y0 ON _y0.piece_divers = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'ce reglement est rattaché à une operation diverse qui n''a pas de tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;	
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 1;
						_debit_ = data_.montant;
					ELSE
						_numero_ = 2;
						_credit_ = data_.montant;
					END IF;
				ELSE		
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 2;
						_credit_ = data_.montant;
					ELSE
						_numero_ = 1;
						_debit_ = data_.montant;
					END IF;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN
			SELECT INTO data_ p.id, p.num_refrence, p.date_paiement, p.montant, y.caisse, t.compte as compte_tiers, p.fournisseur as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, y.piece_achat, m.societe, j.agence, p.nature, 
				r.libelle, r.mode_reglement, r.code_comptable, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.num_refrence) AS reference_externe FROM yvs_compta_phase_acompte_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_acompte_fournisseur p ON y.piece_achat = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_base_fournisseur t ON p.fournisseur = t.id LEFT JOIN yvs_base_mode_reglement m ON p.model = m.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE y.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_refrence;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.piece_achat IS NULL OR data_.piece_achat < 1)THEN
					_error_ = 'cette phase n''est rattachée a aucun acompte';
				ELSE
					_libelle_ = _libelle_||' du fournisseur '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_acompte_achat p ON p.phase_reg = r.id WHERE p.piece_achat = data_.id;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE			
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.nature = 'A')THEN
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des acomptes fournisseur n''est pas paramètré';
						END IF;
					ELSE
						IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
							_error_ = 'le fournisseur n''a pas de compte collectif';
						END IF;
						_compte_general_ = data_.compte_tiers;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_acompte_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_acompte_achat v ON v.etape = y.id INNER JOIN yvs_compta_acompte_fournisseur p ON y.piece_achat = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_acompte_achat _y INNER JOIN yvs_compta_acompte_fournisseur _p ON _y.piece_achat = _p.id INNER JOIN yvs_compta_phase_acompte_achat _y0 ON _y0.piece_achat = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;			
				END IF;	
				IF(data_.reglement_ok)THEN
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette phase est liée a un acompte fournisseur qui n'' pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;			
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN
			SELECT INTO data_ p.id, p.num_refrence, p.date_paiement, p.montant, y.caisse, t.compte as compte_tiers, p.client as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, y.piece_vente, m.societe, j.agence, p.nature,
				r.libelle, r.mode_reglement, r.code_comptable, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.num_refrence) AS reference_externe FROM yvs_compta_phase_acompte_vente y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_acompte_client p ON y.piece_vente = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_client t ON p.client = t.id LEFT JOIN yvs_base_mode_reglement m ON p.model = m.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE y.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_refrence;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.piece_vente IS NULL OR data_.piece_vente < 1)THEN
					_error_ = 'cette phase n''est rattachée a aucun acompte';
				ELSE
					_libelle_ = _libelle_||' du client '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_acompte_vente p ON p.phase_reg = r.id WHERE p.piece_vente = data_.id;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.nature = 'A')THEN
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des acomptes client n''est pas paramètré';
						END IF;
					ELSE
						IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
							_error_ = 'le client n''a pas de compte collectif';
						END IF;
						_compte_general_ = data_.compte_tiers;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_acompte_vente y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_acompte_vente v ON v.etape = y.id INNER JOIN yvs_compta_acompte_client p ON y.piece_vente = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_acompte_vente _y INNER JOIN yvs_compta_acompte_client _p ON _y.piece_vente = _p.id INNER JOIN yvs_compta_phase_acompte_vente _y0 ON _y0.piece_vente = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;			
				END IF;	
				IF(data_.reglement_ok)THEN
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette phase est liée a un acompte client qui n'' pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;			
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_VENTE')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, p.caisse, p.vente, d.num_doc, d.client, t.compte as compte_tiers, t.suivi_comptable, d.tiers, m.compte_tiers AS tiers_interne, c.compte, mo.type_reglement, mo.societe, u.agence
				FROM yvs_compta_caisse_piece_vente p LEFT JOIN yvs_base_caisse c ON p.caisse = c.id LEFT JOIN yvs_base_mode_reglement mo ON p.model = mo.id
				LEFT JOIN yvs_com_doc_ventes d ON p.vente = d.id LEFT JOIN yvs_com_client t ON d.client = t.id LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
				LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_grh_employes m ON u.id = m.code_users WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece p WHERE p.piece_vente = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_vente = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_VENTE', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_paiement ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_paiement;
					_libelle_ = 'Reglement Facture vente N° ';		
					_compte_tiers_ = null;	
					IF(data_.vente IS NULL OR data_.vente < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune facture';
					ELSE
						_libelle_ = _libelle_||data_.num_doc;
					END IF;
					IF(data_.type_reglement = 'COMPENSATION')THEN					
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'COMPENSATION';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des attentes pour compensation n''est pas paramètré';
						END IF;
					ELSE				
						SELECT INTO ligne_ COALESCE(a.nature, 'A') AS nature, CHAR_LENGTH(COALESCE(a.nature, 'A'))::bigint as count FROM yvs_compta_notif_reglement_vente n INNER JOIN yvs_compta_acompte_client a ON n.acompte = a.id WHERE n.piece_vente = element_ LIMIT 1;
						IF(ligne_.count IS NOT NULL AND ligne_.count > 0)THEN	
							IF(ligne_.nature = 'A')THEN
								SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
								IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
									_error_ = 'Le compte des acomptes client n''est paramètré';
								END IF;	
							ELSE
								IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
									_error_ = 'ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte général';
								ELSE
									_compte_general_ = data_.compte_tiers;
								END IF;
							END IF;
							_libelle_ = _libelle_|| ' par acompte';	
						ELSE
							IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
								_error_ = 'ce reglement n''est associé à aucune caisse';
							ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
								_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte;
							END IF;
						END IF;
					END IF;
					_numero_ = 1;			
					_credit_ = 0;
					_debit_ = data_.montant;	
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
						
					_error_ = '';
					IF(data_.vente IS NOT NULL AND data_.vente > 0)THEN
						IF(data_.client IS NULL OR data_.client < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de client';
						ELSE
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
							IF(COALESCE(ligne_.count, 0) < 1)THEN
								IF(data_.suivi_comptable)THEN
									IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
										_error_ = 'car ce reglement est rattaché à une facture qui n''a pas de compte tiers';
									ELSE
										_compte_tiers_ = data_.tiers;
									END IF;
								ELSE
									IF(data_.tiers_interne IS NULL OR data_.tiers_interne < 1)THEN
										_error_ = 'car ce reglement est rattaché à une facture qui est liée à un vendeur qui n''a pas de compte tiers';
									ELSE
										_compte_tiers_ = data_.tiers_interne;
									END IF;
								END IF;
							END IF;
						END IF;
					ELSE
						_error_ = 'ce reglement n''est associé à aucune facture';
					END IF;
					_ref_externe_ = data_.id;
					_table_externe_ = 'CAISSE_VENTE';
					_numero_ = 2;
					_credit_ = data_.montant;
					_debit_ = 0;	
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CAISSE_ACHAT')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, p.caisse, p.achat, d.num_doc, d.fournisseur, t.compte AS compte_tiers, d.fournisseur as tiers, c.compte, mo.type_reglement, mo.societe, d.agence FROM yvs_compta_caisse_piece_achat p 
				LEFT JOIN yvs_base_caisse c ON p.caisse = c.id  LEFT JOIN yvs_base_mode_reglement mo ON p.model = mo.id
				LEFT JOIN yvs_com_doc_achats d ON p.achat = d.id LEFT JOIN yvs_base_fournisseur t ON d.fournisseur = t.id WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece_achat p WHERE p.piece_achat = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece_achat p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_achat = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_ACHAT', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_paiement ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_paiement;
					_libelle_ = 'Reglement Facture achat N° ';
					
					IF(data_.achat IS NULL OR data_.achat < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune facture';
					ELSE
						SELECT INTO ligne_ COALESCE(a.nature, 'A') AS nature, CHAR_LENGTH(COALESCE(a.nature, 'A'))::bigint as count FROM yvs_compta_notif_reglement_achat n INNER JOIN yvs_compta_acompte_fournisseur a ON n.acompte = a.id WHERE n.piece_achat = element_ LIMIT 1;
						_libelle_ = _libelle_||data_.num_doc;
						IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de fournisseur';
						ELSE
							
							IF(COALESCE(ligne_.count, 0) < 1)THEN
								IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
									_error_ = 'car ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte tiers';
								ELSE
									_compte_tiers_ = data_.tiers;
								END IF;
							END IF;
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
						END IF;
					END IF;
					_numero_ = 1;
					_credit_ = 0;
					_debit_ = data_.montant;
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
					
					_error_ = '';			
					_compte_tiers_ = null;	
					IF(data_.type_reglement = 'COMPENSATION')THEN					
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'COMPENSATION';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des attentes pour compensation n''est pas paramètré';
						END IF;
					ELSE		
						IF(ligne_.count IS NOT NULL AND ligne_.count > 0)THEN	
							IF(ligne_.nature = 'A')THEN
								SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
								IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
									_error_ = 'Le compte des acomptes fournisseur n''est paramètré';
								END IF;		
							ELSE
								IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
									_error_ = 'ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte général';
								ELSE
									_compte_general_ = data_.compte_tiers;
								END IF;
							END IF;
						ELSE
							IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
								_error_ = 'ce reglement n''est associé à aucune caisse';
							ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
								_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
							ELSE	
								_compte_general_ = data_.compte;
							END IF;
						END IF;
					END IF;
					_numero_ = 2;			
					_credit_ = data_.montant;
					_debit_ = 0;		
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CAISSE_DIVERS')THEN
			SELECT INTO data_ p.id, p.num_piece, p.date_piece, p.montant, p.caisse, p.doc_divers, d.num_piece as num_doc, CONCAT(d.type_doc, '') AS type_doc, CONCAT(d.mouvement, '') AS mouvement, d.id_tiers as tiers, d.compte_general, name_tiers(d.id_tiers, d.table_tiers, 'C')::integer as compte_collectif, 
				c.compte, mo.type_reglement, COALESCE(d.libelle_comptable, d.num_piece) AS description, d.table_tiers, d.agence 
				FROM yvs_compta_caisse_piece_divers p LEFT JOIN yvs_base_caisse c ON p.caisse = c.id LEFT JOIN yvs_base_mode_reglement mo ON p.mode_paiement = mo.id 
				LEFT JOIN yvs_compta_caisse_doc_divers d ON p.doc_divers = d.id WHERE p.id = element_;
			IF(data_.num_piece IS NOT NULL AND data_.num_piece NOT IN ('', ' '))THEN		
				_agence_ = data_.agence;
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece_divers p WHERE p.piece_divers = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece_divers p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_divers = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_DIVERS', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					SELECT INTO ligne_ COUNT(p.id) FROM yvs_compta_caisse_piece_cout_divers p WHERE p.piece = data_.id;
					IF(COALESCE(ligne_.count, 0) > 0)THEN
						_error_ = 'ce reglement est rattaché à un cout';
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					ELSE
						_table_tiers_ = data_.table_tiers;	
						_jour_ = to_char(data_.date_piece ,'dd')::integer;
						_num_piece_ = data_.num_doc;	
						_echeance_ = data_.date_piece;
						_libelle_ = 'Reglement N° '||data_.num_piece||' sur ';	
						
						IF(data_.doc_divers IS NULL OR data_.doc_divers < 1)THEN
							_error_ = 'ce reglement n''est associé à aucun document';
						ELSE
							_libelle_ = _libelle_||data_.description;
						END IF;
						
						_error_ = '';
						_compte_general_ = null;
						_compte_tiers_ = null;
						_debit_ = 0;
						_credit_ = 0;		
						IF(data_.mouvement = 'D')THEN
							_debit_ = data_.montant;
							_numero_ = 1;
						ELSE	
							_credit_ = data_.montant;	
							_numero_ = 2;			
						END IF;
						IF(data_.tiers IS NULL OR data_.tiers < 1)THEN							
							_error_ = 'devez comptabiliser le documents en question';
						ELSE
							_compte_tiers_ = data_.tiers;
							IF(data_.compte_collectif IS NULL OR data_.compte_collectif < 1)THEN							
								_error_ = 'car ce reglement est rattaché à un document d''un tiers qui n''a pas de compte collectif';
							ELSE
								_compte_general_ = data_.compte_collectif;
							END IF;	
						END IF;	
						_num_ref_ = data_.num_piece;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
						
						_error_ = '';
						_compte_general_ = null;
						_compte_tiers_ = null;
						_credit_ = 0;
						_debit_ = 0;
						IF(data_.mouvement = 'D')THEN
							_credit_ = data_.montant;
							_numero_ = 2;
						ELSE
							_debit_ = data_.montant;
							_numero_ = 1;		
						END IF;	
						IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
							_error_ = 'ce reglement n''est associé à aucune caisse';
						ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;	
						_num_ref_ = data_.num_piece;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
				END IF;
			END IF;
		ELSIF(table_ = 'ACOMPTE_VENTE')THEN
			SELECT INTO data_ a.id, a.date_acompte, a.num_refrence, a.montant, m.societe, c.compte, f.compte AS compte_collectif, a.client as tiers, m.type_reglement, j.agence, a.nature FROM yvs_compta_acompte_client a  LEFT JOIN yvs_base_caisse c ON a.caisse = c.id
				INNER JOIN yvs_base_mode_reglement m ON a.model = m.id LEFT JOIN yvs_com_client f ON a.client = f.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE a.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_acompte_vente p WHERE p.piece_vente = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_acompte_vente p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_vente = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_ACOMPTE_VENTE', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_acompte ,'dd')::integer;
					_num_piece_ = data_.num_refrence;	
					_echeance_ = data_.date_acompte;
					_libelle_ = 'Acompte N° '||data_.num_refrence;	
					IF(data_.nature = 'A')THEN			
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'le compte des acomptes client n''est pas paramètré';
						END IF;
					ELSE
						IF(data_.compte_collectif IS NULL OR data_.compte_collectif < 1)THEN
							_error_ = 'le client n''a pas de compte collectif';
						END IF;
						_compte_general_ = data_.compte_collectif;
					END IF;
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cet acompte est rattaché à un fournisseur qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;				
					END IF;				
					_credit_ = data_.montant;
					_debit_ = 0;	
					_numero_ = 2;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;

					_error_ = null;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cet acompte est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;
					_credit_ = 0;
					_debit_ = data_.montant;	
					_numero_ = 1;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'ACOMPTE_ACHAT')THEN
			SELECT INTO data_ a.id, a.date_acompte, a.num_refrence, a.montant, m.societe, c.compte, f.compte AS compte_collectif, a.fournisseur as tiers, m.type_reglement, j.agence, a.nature FROM yvs_compta_acompte_fournisseur a LEFT JOIN yvs_base_caisse c ON a.caisse = c.id 
				INNER JOIN yvs_base_mode_reglement m ON a.model = m.id INNER JOIN yvs_base_fournisseur f ON a.fournisseur = f.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE a.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_acompte_achat p WHERE p.piece_achat = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_acompte_achat p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_achat = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_ACOMPTE_ACHAT', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_acompte ,'dd')::integer;
					_num_piece_ = data_.num_refrence;	
					_echeance_ = data_.date_acompte;
					_libelle_ = 'Acompte N° '||data_.num_refrence;	
					IF(data_.nature = 'A')THEN				
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des acomptes fournisseur n''est pas paramètré';
						END IF;
					ELSE
						IF(data_.compte_collectif IS NULL OR data_.compte_collectif < 1)THEN
							_error_ = 'le fournisseur n''a pas de compte collectif';
						END IF;
						_compte_general_ = data_.compte_collectif;
					END IF;
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cet acompte est rattaché à un fournisseur qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;				
					END IF;			
					_credit_ = 0;
					_debit_ = data_.montant;	
					_numero_ = 1;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;

					_error_ = null;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cet acompte est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;
					_credit_ = data_.montant;
					_debit_ = 0;	
					_numero_ = 2;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CREDIT_VENTE')THEN
			SELECT INTO data_ a.date_credit, a.num_reference, a.montant, m.societe, c.compte, a.client as tiers, t.compte as compte_general, j.agence FROM yvs_compta_credit_client a 
				INNER JOIN yvs_com_client c ON a.client = c.id LEFT JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				LEFT JOIN yvs_grh_type_cout t ON a.type_credit = t.id WHERE a.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_credit ,'dd')::integer;
				_num_piece_ = data_.num_reference;	
				_echeance_ = data_.date_credit;
				_libelle_ = 'Credit N° '||data_.num_reference;				
				IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
					_error_ = 'Le credit est rattaché a un type qui n''a pas de compte';
				ELSE
					_compte_general_ = data_.compte_general;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				_compte_tiers_ = data_.tiers;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le client n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 1;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CREDIT_ACHAT')THEN
			SELECT INTO data_ a.date_credit, a.num_reference, a.montant, m.societe, c.compte, a.fournisseur as tiers, t.compte as compte_general, j.agence FROM yvs_compta_credit_fournisseur a 
				INNER JOIN yvs_base_fournisseur c ON a.fournisseur = c.id INNER JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				LEFT JOIN yvs_grh_type_cout t ON a.type_credit = t.id WHERE a.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_credit ,'dd')::integer;
				_num_piece_ = data_.num_reference;	
				_echeance_ = data_.date_credit;
				_libelle_ = 'Credit N° '||data_.num_reference;					
				IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
					_error_ = 'Le credit est rattaché a un type qui n''a pas de compte';
				ELSE
					_compte_general_ = data_.compte_general;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 1;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				_compte_tiers_ = data_.tiers;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le fournisseur n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_CREDIT_VENTE')THEN
			SELECT INTO data_ r.date_paiement, r.numero, a.num_reference, r.valeur as montant, m.societe, c.compte, a.client as tiers, t.compte as compte_general, j.agence ,r.caisse
				FROM yvs_compta_reglement_credit_client r LEFT JOIN yvs_compta_credit_client a ON r.credit = a.id LEFT JOIN yvs_base_caisse t ON r.caisse = t.id
				LEFT JOIN yvs_com_client c ON a.client = c.id INNER JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				WHERE r.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero;	
				_echeance_ = data_.date_paiement;
				_libelle_ = 'REGLEMENT N°'||data_.numero||' DU Credit N° '||data_.num_reference;					
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'Le reglement n''est pas rattaché à une caisse';
				ELSE
					IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
						_error_ = 'Le reglement est rattaché a une caisse qui n''a pas de compte';
					ELSE
						_compte_general_ = data_.compte_general;
					END IF;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 2;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le client n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_compte_tiers_ = data_.tiers;	
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_CREDIT_ACHAT')THEN
			SELECT INTO data_ r.date_paiement, r.numero, a.num_reference, r.valeur as montant, m.societe, c.compte, a.fournisseur as tiers, t.compte as compte_general, j.agence ,r.caisse
				FROM yvs_compta_reglement_credit_fournisseur r LEFT JOIN yvs_compta_credit_fournisseur a ON r.credit = a.id LEFT JOIN yvs_base_caisse t ON r.caisse = t.id
				LEFT JOIN yvs_base_fournisseur c ON a.fournisseur = c.id INNER JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				WHERE r.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero;	
				_echeance_ = data_.date_paiement;
				_libelle_ = 'REGLEMENT N°'||data_.numero||' DU Credit N° '||data_.num_reference;					
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'Le reglement n''est pas rattaché à une caisse';
				ELSE
					IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
						_error_ = 'Le reglement est rattaché a une caisse qui n''a pas de compte';
					ELSE
						_compte_general_ = data_.compte_general;
					END IF;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le fournisseur n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_compte_tiers_ = data_.tiers;	
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 1;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'DOC_VIREMENT')THEN
			SELECT INTO data_ p.numero_piece, p.date_paiement, p.montant, p.cible, p.source, COALESCE(p.note, 'Virement N° '||p.numero_piece) AS note, c.compte AS compte_cible, s.compte AS compte_source, (SELECT l.compte FROM yvs_base_liaison_caisse l WHERE l.caisse_cible = p.cible AND l.caisse_source = p.source LIMIT 1) AS compte_interne, j.agence
				FROM yvs_compta_caisse_piece_virement p LEFT JOIN yvs_base_caisse s ON p.source = s.id LEFT JOIN yvs_base_caisse c ON p.cible = c.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'EMPLOYE';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero_piece;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.note;

				_error_ = '';
				_debit_ = 0;
				_compte_general_ = null;
				IF(data_.source IS NULL OR data_.source < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune emetteur';
				ELSIF(data_.compte_source IS NULL OR data_.compte_source < 1)THEN
					_error_ = 'ce reglement est rattaché à un emetteur qui n''a pas de compte général';
				ELSE
					_compte_general_ = data_.compte_source;
				END IF;
				_credit_ = data_.montant;
				_numero_ = 2;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				_credit_ = 0;
				IF(data_.compte_interne IS NULL OR data_.compte_interne < 1)THEN
					_error_ = 'ces comptes n''ont pas de compte de liaison';
				ELSE
					_compte_general_ = data_.compte_interne;
				END IF;
				_debit_ = data_.montant;
				_numero_ = 2;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				
				_debit_ = 0;
				_credit_ = data_.montant;
				_numero_ = 1;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				_credit_ = 0;
				IF(data_.cible IS NULL OR data_.cible < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune recepteur';
				ELSIF(data_.compte_cible IS NULL OR data_.compte_cible < 1)THEN
					_error_ = 'ce reglement est rattaché à un recepteur qui n''a pas de compte général';
				ELSE
					_compte_general_ = data_.compte_cible;
				END IF;
				_debit_ = data_.montant;
				_numero_ = 1;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_numero_ = 2;
				FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_compta_cout_sup_piece_virement co ON co.type_cout = tx.id INNER JOIN yvs_compta_caisse_piece_virement dv ON dv.id = co.virement
					WHERE dv.id = element_ GROUP BY tx.id, tx.libelle 
				LOOP
					_compte_tiers_ = null;	
					IF(ligne_.total != 0)THEN
						_libelle_ = ligne_.libelle||' pour le virement N° '||data_.numero_piece;
						
						_credit_ = 0;
						_debit_ = ligne_.total;
						IF(i_ < 10)THEN
							_num_ref_ = data_.numero_piece ||'-0'||i_;
						ELSE
							_num_ref_ = data_.numero_piece ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
						END IF;
						_compte_general_ = ligne_.compte;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;

						
						_credit_ = ligne_.total;
						_debit_ = 0;
						IF(i_ < 10)THEN
							_num_ref_ = data_.numero_piece ||'-0'||i_;
						ELSE
							_num_ref_ = data_.numero_piece ||'-'||i_;
						END IF;
						IF(data_.source IS NULL OR data_.source < 1)THEN
							_error_ = 'ce reglement n''est associé à aucune emetteur';
						ELSIF(data_.compte_source IS NULL OR data_.compte_source < 1)THEN
							_error_ = 'ce reglement est rattaché à un emetteur qui n''a pas de compte général';
						END IF;
						_compte_general_ = data_.compte_source;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
				END LOOP;
			END IF;
		ELSIF(table_ = 'ORDRE_SALAIRE')THEN
			SELECT INTO data_ o.* FROM yvs_grh_ordre_calcul_salaire o WHERE o.id = element_;
			IF(data_.reference IS NOT NULL AND data_.reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'TIERS';	
				_jour_ = to_char(data_.date_execution ,'dd')::integer;
				_num_piece_ = data_.reference;	
				_echeance_ = data_.date_execution;
				-- Recupere gains
				-- récupère les éléments de gains en groupant par compte charge du gains
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, COALESCE(SUM(db.montant_payer),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition FROM yvs_grh_detail_bulletin db 
					LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat 
					LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id 
					WHERE el.visible_bulletin IS TRUE AND NOT (COALESCE(db.montant_payer,0)=0) AND el.actif IS TRUE AND el.retenue IS FALSE AND b.entete = element_ 
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, co.id ORDER BY ag.designation, el.nom
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;
					
					_error_ = '';
					_credit_ = 0;
					_numero_ = 1;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.id IS NULL OR ligne_.id < 1)THEN
						_error_ = 'certains bulletins ne sont pas rattachés aux elements de salaire';
					ELSIF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'le gain '||ligne_.nom||' n''est rattaché à aucun compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
-- 					RAISE NOTICE '- %',_compte_general_;
					_debit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- récupère du net a payer en groupant par compte collectif employé
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, COALESCE(co.intitule, '') AS intitule, e.compte_collectif, (COALESCE(SUM(db.montant_payer),0) - COALESCE(SUM(db.retenu_salariale),0)) AS montant, co.saisie_analytique 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON e.compte_collectif = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.montant_payer,0)!=0 OR COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND b.entete = element_
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, co.id, e.compte_collectif ORDER BY ag.designation, e.compte_collectif, co.intitule
				LOOP
					_libelle_ = ligne_.intitule ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 1;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_collectif IS NULL OR ligne_.compte_collectif < 1)THEN
						_error_ = 'certains employes n''ont pas de compte collectif';
					ELSE
						_compte_general_ = ligne_.compte_collectif;
					END IF;
					_credit_ = ligne_.montant;
					IF(ligne_.montant = 177871.63)THEN
						RAISE NOTICE '_libelle_ : %', _libelle_;
						RAISE NOTICE '_compte_general_ : %', _compte_general_;
					END IF;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, 0, _compte_general_, _compte_tiers_, _credit_, '', table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- Recupere retenues
				-- récupère les retenues patronales
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, el.compte_cotisation, COALESCE(SUM(db.montant_employeur),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.montant_employeur,0)!=0) AND el.actif IS TRUE AND el.retenue IS TRUE AND b.entete = element_
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, el.compte_charge, el.compte_cotisation, co.id ORDER BY ag.designation , el.compte_charge
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 5;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_cotisation IS NULL OR ligne_.compte_cotisation < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de cotisation';
					ELSE
						_compte_general_ = ligne_.compte_cotisation;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _credit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;	

					_error_ = '';
					_credit_ = 0;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
					_debit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- récupère les retenues salariales sans saisie tiers en groupant par compte de cotisation des retenues
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_cotisation, COALESCE(SUM(db.retenu_salariale),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON el.compte_cotisation = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND (COALESCE(el.saisi_compte_tiers, false) IS FALSE) 
					AND el.retenue IS TRUE AND b.entete = element_ AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, el.compte_cotisation, co.id ORDER BY ag.designation , el.nom
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 2;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_cotisation IS NULL OR ligne_.compte_cotisation < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de cotisation';
					ELSE
						_compte_general_ = ligne_.compte_cotisation;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _credit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;	
				-- récupère les retenues salariales avec saisie tiers en groupant par compte de charge des retenues
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, COALESCE(SUM(db.retenu_salariale),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition, t.id AS tiers, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom
                                        FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
                                        LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
                                        LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id LEFT JOIN yvs_base_tiers t ON e.compte_tiers = t.id 
                                        WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND COALESCE(el.saisi_compte_tiers, false) IS TRUE 
                                        AND el.retenue IS TRUE AND b.entete = element_ AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val))))
                                        GROUP BY ag.id, el.id, el.compte_charge, co.id, t.id ORDER BY ag.designation, el.nom
				LOOP
					_libelle_ = ligne_.nom ||' liée '|| ligne_.nom_prenom;
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 3;
					_compte_general_ = null;
					IF(ligne_.tiers IS NULL OR ligne_.tiers < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' est rattachée à des employés qui n''ont pas de compte tiers';
					ELSE
						_compte_tiers_ = ligne_.tiers;
					END IF;
					IF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;	
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;	
			END IF;
		END IF;
	END IF;
	-- DETERMINATION DE L'ARRONDI
	SELECT INTO _debit_ SUM(_debit) FROM table_compta_content_journal;
	SELECT INTO _credit_ SUM(_credit) FROM table_compta_content_journal;
	somme_ = COALESCE(_debit_, 0) - COALESCE(_credit_, 0);
	IF(somme_ != 0 AND abs(somme_) <= valeur_limite_arrondi_)THEN
		_compte_tiers_ = null;
		IF(somme_ > 0)THEN
			SELECT INTO data_ * FROM table_compta_content_journal WHERE _credit > 0 ORDER BY _numero LIMIT 1;
			_libelle_ = 'arrondis gain';
			titre_ = 'ARRONDI_GAIN';
			IF(table_ = 'DOC_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'JOURNAL_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'DOC_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _credit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'FRAIS_MISSION')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _credit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CREDIT_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'CREDIT_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'DOC_VIREMENT')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'ORDRE_SALAIRE')THEN	
				_table_tiers_ = 'EMPLOYE';	
			END IF;				
			SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = titre_;
			IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
				_error_ = 'Le compte des '||_libelle_||' n''est pas paramètré';
			END IF;
			_credit_ = somme_;
			_debit_ = 0;
		ELSE
			SELECT INTO data_ * FROM table_compta_content_journal WHERE _debit > 0 ORDER BY _numero LIMIT 1;
			_libelle_ = 'arrondis perte';
			titre_ = 'ARRONDI_PERTE';
			IF(table_ = 'DOC_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _debit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'JOURNAL_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'DOC_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'FRAIS_MISSION')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _debit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CREDIT_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'CREDIT_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'DOC_VIREMENT')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'ORDRE_SALAIRE')THEN	
				_table_tiers_ = 'EMPLOYE';	
			END IF;
			_libelle_ = 'Arrondi Perte';				
			SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE c.actif IS TRUE AND n.societe = societe_ AND n.nature = titre_;
			IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
				_error_ = 'Le compte des '||_libelle_||' n''est pas paramètré';
			END IF;
			_credit_ = 0;
			_debit_ = -somme_;
		END IF;	
		_jour_ = data_._jour;
		_num_piece_ = data_._num_piece;
		_echeance_ = data_._echeance;
		_numero_ = data_._numero;	
		IF(i_ < 10)THEN
			_num_ref_ = _num_piece_ ||'-0'||i_;
		ELSE
			_num_ref_ = _num_piece_ ||'-'||i_;
		END IF;		
		_numero_ = COALESCE(_numero_, 0) + 1;
		_id_ = _id_ - 1;
		INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
	END IF;
	RETURN QUERY SELECT * FROM table_compta_content_journal ORDER BY _numero, _debit DESC, _credit DESC, _id DESC, _jour, _num_piece;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean)
  OWNER TO postgres;

  
ALTER TABLE yvs_prod_session_prod ADD COLUMN actif boolean;
ALTER TABLE yvs_prod_session_prod ALTER COLUMN actif SET DEFAULT true;

