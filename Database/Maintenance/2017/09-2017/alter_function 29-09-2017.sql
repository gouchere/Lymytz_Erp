INSERT INTO yvs_base_element_reference(designation, module, author) VALUES ('Piece Credit Vente', 'COFI', 16);
INSERT INTO yvs_base_element_reference(designation, module, author) VALUES ('Piece Credit Achat', 'COFI', 16);

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
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;	
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, y.num_refrence, dp.users, y.fournisseur, cl.nom, cl.prenom , em.code_users AS caissier FROM yvs_compta_acompte_fournisseur y 
			INNER JOIN yvs_base_fournisseur cl ON y.fournisseur = cl.id INNER JOIN yvs_base_caisse ca ON y.caisse = ca.id LEFT JOIN yvs_grh_employes em ON ca.responsable = em.id
			LEFT JOIN yvs_users_agence dp ON dp.id = y.author INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE y.id= NEW.id; 
			
		IF(NEW.fournisseur IS NOT NULL) THEN
			id_tiers_=NEW.fournisseur;
		ELSE
			id_tiers_=line_.fournisseur;
		END IF;
		--récupère le code tièrs de ce fournisseurs
		SELECT INTO id_tiers_ t.tiers FROM yvs_base_fournisseur t WHERE t.id= NEW.fournisseur;
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
		VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_ACHAT', NEW.montant, null, NEW.date_acompte, null, NEW.date_acompte,  NEW.statut, 
				line_.societe, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'D', (nom_ ||' '||prenom_), NEW.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_ACHAT' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_refrence, note=NEW.commentaire, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=null, date_mvt=NEW.date_acompte, date_paiment_prevu=null, date_paye=NEW.date_acompte, 
				statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='D', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='ACOMPTE_ACHAT' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_ACHAT', NEW.montant, null, NEW.date_acompte, null, NEW.date_acompte,  NEW.statut, line_.societe,
				NEW.author,NEW.caisse, line_.caissier,null, id_tiers_,'D', (nom_ ||' '||prenom_), NEW.model);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_ACHAT' AND id_externe=OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_acompte_fournisseur()
  OWNER TO postgres;
-- Trigger: compta_action_on_acompte_fournisseur on yvs_compta_acompte_fournisseur

-- DROP TRIGGER compta_action_on_acompte_fournisseur ON yvs_compta_acompte_fournisseur;

CREATE TRIGGER compta_action_on_acompte_fournisseur
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_acompte_fournisseur
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_acompte_fournisseur();

  
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
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;	
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_ ag.societe, dv.num_doc, dp.users, dv.fournisseur, cl.nom, cl.prenom, pv.numero_piece, pv.note, pv.montant, pv.date_piece, pv.date_paiment_prevu, pv.date_paiement, pv.statut_piece,
				ac.caisse, pv.caissier, pv.model, pv.fournisseur
			FROM yvs_compta_notif_reglement_achat y INNER JOIN yvs_compta_acompte_fournisseur ac ON y.acompte = ac.id
			INNER JOIN yvs_compta_caisse_piece_achat pv ON y.piece_achat = pv.id INNER JOIN yvs_com_doc_achats dv ON dv.id = pv.achat
			INNER JOIN yvs_base_fournisseur cl ON cl.id=dv.fournisseur 
			LEFT JOIN yvs_users_agence dp ON dp.id= y.author INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE y.id = NEW.id; 
		
		--récupère le code tièrs de ce fournisseurs
		SELECT INTO id_tiers_ t.tiers FROM yvs_base_fournisseur t WHERE t.id= line_.fournisseur;
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
			VALUES (line_.numero_piece, NEW.id, line_.note, 'NOTIF_ACHAT', -line_.montant, line_.num_doc, line_.date_piece, line_.date_paiment_prevu, line_.date_paiement,  line_.statut_piece, 
				line_.societe, NEW.author, line_.caisse, line_.caissier, null, id_tiers_,'D', (nom_ ||' '||prenom_), line_.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='NOTIF_ACHAT' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=line_.numero_piece, note=line_.note, montant= -line_.montant, tiers_externe=id_tiers_, model = line_.model,
				reference_externe=line_.num_doc, date_mvt= line_.date_piece, date_paiment_prevu= line_.date_paiment_prevu, date_paye= line_.date_paiement, 
				statut_piece= line_.statut_piece, caisse= line_.caisse, caissier= line_.caissier, mouvement='D', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='NOTIF_ACHAT' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (line_.numero_piece, NEW.id, line_.note, 'NOTIF_ACHAT', -line_.montant, line_.num_doc, line_.date_piece, line_.date_paiment_prevu, line_.date_paiement,  line_.statut_piece, line_.societe,NEW.author,
				line_.caisse, line_.caissier,null, id_tiers_,'D', (nom_ ||' '||prenom_), line_.model);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='NOTIF_ACHAT' AND id_externe=OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_notif_reglement_achat()
  OWNER TO postgres;
  
  
CREATE TRIGGER compta_action_on_notif_reglement_achat
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_notif_reglement_achat
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_notif_reglement_achat();
  
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
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;	
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_ ag.societe, dv.num_doc,cu.users, dv.client, cl.nom, cl.prenom, pv.numero_piece, pv.note, pv.montant, pv.date_piece, pv.date_paiment_prevu, pv.date_paiement, pv.statut_piece,
				ac.caisse, pv.caissier, pv.model, pv.client
			FROM yvs_compta_notif_reglement_vente y INNER JOIN yvs_compta_acompte_client ac ON y.acompte = ac.id
			INNER JOIN yvs_compta_caisse_piece_vente pv ON y.piece_vente = pv.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = pv.vente
			INNER JOIN yvs_com_client cl ON cl.id=dv.client INNER JOIN yvs_com_entete_doc_vente en ON en.id=dv.entete_doc
			INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=en.creneau INNER JOIN yvs_com_creneau_point cr ON cr.id=cu.creneau_point
			INNER JOIN yvs_base_point_vente dp ON dp.id=cr.point INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE y.id = NEW.id; 
		
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
			VALUES (line_.numero_piece, NEW.id, line_.note, 'NOTIF_VENTE', -line_.montant, line_.num_doc, line_.date_piece, line_.date_paiment_prevu, line_.date_paiement,  line_.statut_piece, 
				line_.societe, NEW.author, line_.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), line_.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='NOTIF_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=line_.numero_piece, note=line_.note, montant= -line_.montant, tiers_externe=id_tiers_, model = line_.model,
				reference_externe=line_.num_doc, date_mvt= line_.date_piece, date_paiment_prevu= line_.date_paiment_prevu, date_paye= line_.date_paiement, 
				statut_piece= line_.statut_piece, caisse= line_.caisse, caissier= line_.caissier, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='NOTIF_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (line_.numero_piece, NEW.id, line_.note, 'NOTIF_VENTE', -line_.montant, line_.num_doc, line_.date_piece, line_.date_paiment_prevu, line_.date_paiement,  line_.statut_piece, line_.societe,NEW.author,
				line_.caisse, line_.caissier,null, id_tiers_,'R', (nom_ ||' '||prenom_), line_.model);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='NOTIF_VENTE' AND id_externe=OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_notif_reglement_vente()
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
	
BEGIN	
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
			LEFT JOIN yvs_users_agence dp ON dp.id = y.author INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE y.id= NEW.id; 
			
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
		VALUES (line_.num_reference, NEW.id, line_.motif, 'CREDIT_VENTE', NEW.valeur, null, NEW.date_reg, null, NEW.date_reg,  NEW.statut, 
				line_.societe, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='CREDIT_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_reference, note=NEW.motif, montant=NEW.valeur, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=null, date_mvt=NEW.date_reg, date_paiment_prevu=null, date_paye=NEW.date_reg, 
				statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='CREDIT_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.num_reference, NEW.id, NEW.motif, 'CREDIT_VENTE', NEW.valeur, null, NEW.date_reg, null, NEW.date_reg,  NEW.statut, line_.societe,
				NEW.author,NEW.caisse, line_.caissier,null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='CREDIT_VENTE' AND id_externe=OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_credit_client()
  OWNER TO postgres;
  -- Trigger: compta_action_on_acompte_client on yvs_compta_acompte_client

-- DROP TRIGGER compta_action_on_acompte_client ON yvs_compta_acompte_client;

CREATE TRIGGER compta_action_on_credit_client
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_reglement_credit_client
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_credit_client();
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
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;	
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, y.num_reference, dp.users, y.fournisseur, cl.nom, cl.prenom , em.code_users AS caissier, y.motif
			FROM yvs_compta_credit_fournisseur y INNER JOIN yvs_compta_reglement_credit_fournisseur cc ON cc.credit = y.id
			INNER JOIN yvs_base_fournisseur cl ON y.fournisseur = cl.id INNER JOIN yvs_base_caisse ca ON cc.caisse = ca.id LEFT JOIN yvs_grh_employes em ON ca.responsable = em.id
			LEFT JOIN yvs_users_agence dp ON dp.id = y.author INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE y.id= NEW.id; 
			
		--récupère le code tièrs de ce fournisseurs
		SELECT INTO id_tiers_ t.tiers FROM yvs_base_fournisseur t WHERE t.id= line_.fournisseur;
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
		VALUES (line_.num_reference, NEW.id, line_.motif, 'CREDIT_ACHAT', NEW.valeur, null, NEW.date_reg, null, NEW.date_reg,  NEW.statut, 
				line_.societe, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'D', (nom_ ||' '||prenom_), NEW.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='CREDIT_ACHAT' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_reference, note=NEW.motif, montant=NEW.valeur, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=null, date_mvt=NEW.date_reg, date_paiment_prevu=null, date_paye=NEW.date_reg, 
				statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='D', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='CREDIT_ACHAT' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.num_reference, NEW.id, NEW.motif, 'CREDIT_ACHAT', NEW.valeur, null, NEW.date_reg, null, NEW.date_reg,  NEW.statut, line_.societe,
				NEW.author,NEW.caisse, line_.caissier,null, id_tiers_,'D', (nom_ ||' '||prenom_), NEW.model);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='CREDIT_ACHAT' AND id_externe=OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_credit_fournisseur()
  OWNER TO postgres;
-- Trigger: compta_action_on_acompte_fournisseur on yvs_compta_acompte_fournisseur

-- DROP TRIGGER compta_action_on_acompte_fournisseur ON yvs_compta_acompte_fournisseur;

CREATE TRIGGER compta_action_on_credit_fournisseur
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_reglement_credit_fournisseur
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_credit_fournisseur();

-- Function: decoupage_interval_date(date, date, character varying)

-- DROP FUNCTION decoupage_interval_date(date, date, character varying);

CREATE OR REPLACE FUNCTION decoupage_interval_date(IN date_entree_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(intitule character varying, date_sortie date) AS
$BODY$
DECLARE

    date_ date;

    intitule_ character varying;
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;

BEGIN    
-- 	DROP TABLE IF EXISTS table_decoupage_interval_date;
	CREATE TEMP TABLE IF NOT EXISTS table_decoupage_interval_date(_intitule character varying, _date_sortie date);
	DELETE FROM table_decoupage_interval_date;
	if(periode_ = 'A')then
		date_ = (date_entree_ + interval '1 year' - interval '1 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		intitule_ = (select extract(year from date_entree_));	
	elsif(periode_ = 'T')then
		date_ = (date_entree_ + interval '3 month' - interval '1 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		jour_0 = (select extract(month from date_entree_));
		if(char_length(jour_0)<2)then
			jour_0 = '0'||jour_0;
		end if;
		jour_ = '('||jour_0||'/';
		
		jour_0 = (select extract(month from date_));
		if(char_length(jour_0)<2)then
			jour_0 = '0'||jour_0;
		end if;
		intitule_ = jour_||jour_0||')';		
	elsif(periode_ = 'M')then
		date_ = (date_entree_ + interval '1 month' - interval '1 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		jour_ = (select extract(month from date_entree_));
		if(char_length(jour_)<2)then
			jour_ = '0'||jour_;
		end if;			
		annee_ = (select extract(year from date_entree_));	
		intitule_ = jour_ ||'-'|| annee_;
	elsif(periode_ = 'S')then
		date_ = (date_entree_ + interval '1 week' - interval '1 day');	
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;
		
		jour_0 = (select extract(day from date_entree_));
		if(char_length(jour_0)<2)then
			jour_0 = '0'||jour_0;
		end if;
		jour_ = '('||jour_0||'/';
		jour_0 = (select extract(day from date_));
		if(char_length(jour_0)<2)then
			jour_0 = '0'||jour_0;
		end if;
		jour_ = jour_||jour_0||')-';
		
		mois_ = (select extract(month from date_));
		if(char_length(mois_)<2)then
			mois_ = '0'||mois_;
		end if;
		mois_0 = (select extract(month from date_entree_));
		if(char_length(mois_0)<2)then
			mois_0 = '0'||mois_0;
		end if;
		if(mois_ != mois_0)then
			mois_ = '('|| mois_0 || '/'|| mois_ ||')';
		end if;
		intitule_ = jour_ || mois_;
	else
		date_ = (date_entree_ + interval '0 day');
		if(date_ > date_fin_)then
			date_ = date_ - (date_ - date_fin_);
		end if;		
		intitule_ = to_char(date_entree_ ,'dd');
	end if;
	INSERT INTO table_decoupage_interval_date VALUES(intitule_, date_);
	
	return QUERY select * from table_decoupage_interval_date;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION decoupage_interval_date(date, date, character varying)
  OWNER TO postgres;

-- Function: grh_et_masse_salariale(bigint, character varying, character varying)

-- DROP FUNCTION grh_et_masse_salariale(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION grh_et_frais_mission(IN societe_ bigint, IN agence_ bigint, In date_debut_ DATE, In date_fin_ DATE, IN type_ character varying, IN periode_ character varying)
  RETURNS TABLE(id bigint, element character varying, entete character varying, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	element_ RECORD;
	dates_ RECORD;
	
	valeur_ DOUBLE PRECISION DEFAULT 0;
	somme_ DOUBLE PRECISION DEFAULT 0;
	
	entete_ CHARACTER VARYING;
	
	date_ DATE;
	date_save_ DATE DEFAULT date_debut_;
	
	i INTEGER DEFAULT 0;
BEGIN
-- 	DROP TABLE IF EXISTS table_frais_mission;
	CREATE TEMP TABLE IF NOT EXISTS table_frais_mission(_id bigint, _element character varying, _entete character varying, _valeur double precision, _rang integer); 
	DELETE FROM table_frais_mission;
	IF(societe_ > 0 or agence_ > 0)THEN
		IF(type_ = 'O')THEN
			FOR element_ IN SELECT DISTINCT(y.id) AS id, y.titre FROM yvs_grh_objets_mission y INNER JOIN yvs_grh_missions m ON m.objet_mission = y	.id WHERE m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R')
			LOOP
				date_debut_ = date_save_;
				i = 0;
				somme_ = 0;
				WHILE(date_debut_ <= date_fin_)
				LOOP
					i = i + 1;
					SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, periode_);
					IF(dates_.date_sortie IS NOT NULL)THEN
						date_ = dates_.date_sortie;
						entete_ = dates_.intitule;
					END IF;
					
					IF(agence_ > 0)THEN
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.objet_mission = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND e.agence = agence_ AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					ELSE
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.objet_mission = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					END IF;
					valeur_ = COALESCE(valeur_, 0);
					somme_ = somme_ + valeur_;

					INSERT INTO table_frais_mission VALUES(element_.id, element_.titre, entete_, valeur_, i);
					
					
					IF(periode_ = 'A')THEN
						date_debut_ = date_debut_ + INTERVAL '1 year';
					ELSIF(periode_ = 'T')THEN
						date_debut_ = date_debut_ + INTERVAL '3 month';
					ELSIF(periode_ = 'M')THEN
						date_debut_ = date_debut_ + INTERVAL '1 month';
					ELSIF(periode_ = 'S')THEN
						date_debut_ = date_debut_ + INTERVAL '1 week';
					ELSE
						date_debut_ = date_debut_ + INTERVAL '1 day';
					END IF;
				END LOOP;
				IF(somme_ = 0)THEN
					DELETE FROM table_frais_mission WHERE _id = element_.id;
				END IF;
			END LOOP;
		ELSIF(type_ = 'L')THEN
			FOR element_ IN SELECT DISTINCT(y.id) AS id, y.libele AS titre FROM yvs_dictionnaire y INNER JOIN yvs_grh_missions m ON m.lieu = y.id WHERE m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R')
			LOOP
				date_debut_ = date_save_;
				i = 0;
				somme_ = 0;
				WHILE(date_debut_ <= date_fin_)
				LOOP
					i = i + 1;
					SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, periode_);
					IF(dates_.date_sortie IS NOT NULL)THEN
						date_ = dates_.date_sortie;
						entete_ = dates_.intitule;
					END IF;
					
					IF(agence_ > 0)THEN
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.lieu = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND e.agence = agence_ AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					ELSE
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.lieu = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					END IF;
					valeur_ = COALESCE(valeur_, 0);
					somme_ = somme_ + valeur_;

					INSERT INTO table_frais_mission VALUES(element_.id, element_.titre, entete_, valeur_, i);
					
					
					IF(periode_ = 'A')THEN
						date_debut_ = date_debut_ + INTERVAL '1 year';
					ELSIF(periode_ = 'T')THEN
						date_debut_ = date_debut_ + INTERVAL '3 month';
					ELSIF(periode_ = 'M')THEN
						date_debut_ = date_debut_ + INTERVAL '1 month';
					ELSIF(periode_ = 'S')THEN
						date_debut_ = date_debut_ + INTERVAL '1 week';
					ELSE
						date_debut_ = date_debut_ + INTERVAL '1 day';
					END IF;
				END LOOP;
				IF(somme_ = 0)THEN
					DELETE FROM table_frais_mission WHERE _id = element_.id;
				END IF;
			END LOOP;
		ELSE
			FOR element_ IN SELECT DISTINCT(y.id) AS id, CONCAT(y.nom, ' ', y.prenom) AS titre FROM yvs_grh_employes y INNER JOIN yvs_grh_missions m ON m.employe = y.id WHERE m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R')
			LOOP
				date_debut_ = date_save_;
				i = 0;
				somme_ = 0;
				WHILE(date_debut_ <= date_fin_)
				LOOP
					i = i + 1;
					SELECT INTO dates_ * FROM decoupage_interval_date(date_debut_, date_fin_, periode_);
					IF(dates_.date_sortie IS NOT NULL)THEN
						date_ = dates_.date_sortie;
						entete_ = dates_.intitule;
					END IF;
					
					IF(agence_ > 0)THEN
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.employe = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND e.agence = agence_ AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					ELSE
						SELECT INTO valeur_ SUM(COALESCE(y.montant, 0)) FROM yvs_grh_frais_mission y INNER JOIN yvs_grh_missions m ON y.mission = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id
						WHERE m.employe = element_.id AND m.actif IS TRUE AND m.statut_mission IN ('V', 'C', 'R') AND m.date_debut >= date_debut_ AND m.date_fin <= date_;
					END IF;
					valeur_ = COALESCE(valeur_, 0);
					somme_ = somme_ + valeur_;

					INSERT INTO table_frais_mission VALUES(element_.id, element_.titre, entete_, valeur_, i);
					
					
					IF(periode_ = 'A')THEN
						date_debut_ = date_debut_ + INTERVAL '1 year';
					ELSIF(periode_ = 'T')THEN
						date_debut_ = date_debut_ + INTERVAL '3 month';
					ELSIF(periode_ = 'M')THEN
						date_debut_ = date_debut_ + INTERVAL '1 month';
					ELSIF(periode_ = 'S')THEN
						date_debut_ = date_debut_ + INTERVAL '1 week';
					ELSE
						date_debut_ = date_debut_ + INTERVAL '1 day';
					END IF;
				END LOOP;
				IF(somme_ = 0)THEN
					DELETE FROM table_frais_mission WHERE _id = element_.id;
				END IF;
			END LOOP;
		END IF;
	END IF;
	return QUERY SELECT * FROM table_frais_mission ORDER BY _element, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION grh_et_frais_mission(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;
