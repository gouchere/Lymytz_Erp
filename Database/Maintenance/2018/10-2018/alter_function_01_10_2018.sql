 -- pièce caisse vente
 -- pièce caisse achat
 pièce caisse acompte client
 pièce caisse acompte fournisseur
 
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
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;
		
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, dv.num_doc,cu.users, dv.client, dv.nom_client, cl.nom, cl.prenom  FROM yvs_compta_caisse_piece_vente pv INNER JOIN yvs_com_doc_ventes dv ON dv.id=pv.vente 
			INNER JOIN yvs_com_client cl ON cl.id=dv.client INNER JOIN yvs_com_entete_doc_vente en ON en.id=dv.entete_doc
			INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=en.creneau INNER JOIN yvs_com_creneau_point cr ON cr.id=cu.creneau_point
			INNER JOIN yvs_base_point_vente dp ON dp.id=cr.point INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE pv.id=NEW.id; 
									    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		id_tiers_=line_.client;
		--récupère le code tièrs de ce clients
		SELECT INTO id_tiers_ t.tiers FROM yvs_com_client t WHERE t.id=id_tiers_;
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
		SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece WHERE piece_vente= NEW.id;
		SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece WHERE piece_vente= NEW.id AND phase_ok IS TRUE;
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'R', line_.nom_client, NEW.model, etape_valide_, etape_total_);
		vente_ = NEW.vente;
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=line_.num_doc, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
				statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='R', societe=line_.societe, name_tiers=line_.nom_client,
				etape_total=etape_total_, etape_valide=etape_valide_
			WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,
				NEW.caisse, NEW.caissier,null, id_tiers_,'R', line_.nom_client, NEW.model, etape_valide_, etape_total_);
		END IF;	
		UPDATE yvs_compta_notif_reglement_vente SET date_update = current_date WHERE piece_vente = NEW.id;
		vente_ = NEW.vente;	           
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=OLD.id;		
		vente_ = OLD.vente;		    	 
	END IF;

	-- Mise à jour de l'échéancier
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
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_vente()
  OWNER TO postgres;

  
  
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
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, dv.num_doc, dv.fournisseur, f.nom, f.prenom FROM yvs_compta_caisse_piece_achat pv 
			INNER JOIN yvs_com_doc_achats dv ON dv.id=pv.achat INNER JOIN yvs_base_fournisseur f ON f.id=dv.fournisseur INNER JOIN yvs_agences ag ON ag.id=dv.agence WHERE pv.id=NEW.id; 
								    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
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
		--récupère le code tièrs de ce fournisseurs
		SELECT INTO id_tiers_ t.tiers FROM yvs_base_fournisseur t WHERE t.id=id_tiers_;		
		-- recherche les étapes de validation
		SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece_achat WHERE piece_achat= NEW.id;
		SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece_achat WHERE piece_achat= NEW.id AND phase_ok IS TRUE;
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, 
		model, etape_valide, etape_total)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, 'D',(nom_||' '||prenom_),
			NEW.model,etape_valide_, etape_total_);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, model = NEW.model, 
				date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', societe=line_.societe, name_tiers=(nom_||' '||prenom_)
				,etape_total=etape_total_, etape_valide=etape_valide_
				WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe, mouvement, name_tiers,
			model,etape_valide_, etape_total_)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'D',(nom_||' '||prenom_), 
				NEW.model,etape_valide_, etape_total_);
		END IF;
		UPDATE yvs_compta_notif_reglement_achat SET date_update = current_date WHERE piece_achat = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_ACHAT' AND id_externe=OLD.id;	
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_achat()
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
	
BEGIN	
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
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,etape_total, etape_valide)
		VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_VENTE', NEW.montant, NEW.num_refrence, NEW.date_acompte, null, NEW.date_acompte,  NEW.statut, 
				line_.societe, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model,etape_total_,etape_valide_);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_refrence, note=NEW.commentaire, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=NEW.num_refrence, date_mvt=NEW.date_acompte, date_paiment_prevu=null, date_paye=NEW.date_acompte, 
				statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_),
				etape_total=etape_total_, etape_valide=etape_valide_
			WHERE table_externe='ACOMPTE_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,etape_total,etape_valide)
			VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_VENTE', NEW.montant, NEW.num_refrence, NEW.date_acompte, null, NEW.date_acompte,  NEW.statut, line_.societe,
				NEW.author,NEW.caisse, line_.caissier,null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model,etape_total_,etape_valide_);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_VENTE' AND id_externe=OLD.id;		
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
		-- recherche les étapes de validation
		SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_acompte_vente WHERE piece_vente= NEW.id;
		SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_acompte_vente WHERE piece_vente= NEW.id AND phase_ok IS TRUE;
	END IF;         
	IF(action_='INSERT') THEN
	    -- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,etape_total, etape_valide)
		VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_ACHAT', NEW.montant, NEW.num_refrence, NEW.date_acompte, null, NEW.date_acompte,  NEW.statut, 
				line_.societe, NEW.author, NEW.caisse, line_.caissier, null, id_tiers_,'D', (nom_ ||' '||prenom_), NEW.model,etape_total_,etape_valide_);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='ACOMPTE_ACHAT' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_refrence, note=NEW.commentaire, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=NEW.num_refrence, date_mvt=NEW.date_acompte, date_paiment_prevu=null, date_paye=NEW.date_acompte, 
				statut_piece=NEW.statut, caisse=NEW.caisse, caissier=line_.caissier, mouvement='D', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_),
				etape_total=etape_total_, etape_valide=etape_valide_
			WHERE table_externe='ACOMPTE_ACHAT' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model,etape_total, etape_valide)
			VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'ACOMPTE_ACHAT', NEW.montant, NEW.num_refrence, NEW.date_acompte, null, NEW.date_acompte,  NEW.statut, line_.societe,
				NEW.author,NEW.caisse, line_.caissier,null, id_tiers_,'D', (nom_ ||' '||prenom_), NEW.model,etape_total_,etape_valide_);
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

  
 --------------
 ALTER TABLE yvs_compta_piece_caisse_mission RENAME TO yvs_compta_justif_bon_mission

ALTER TABLE yvs_compta_justif_bon_mission DROP CONSTRAINT yvs_compta_piece_caisse_mission_fkey;

ALTER TABLE yvs_compta_justif_bon_mission
  ADD CONSTRAINT yvs_compta_justif_bon_mission_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE yvs_compta_justif_bon_mission DROP CONSTRAINT yvs_compta_piece_caisse_mission_mission_fkey;

ALTER TABLE yvs_compta_justif_bon_mission
  ADD CONSTRAINT yvs_compta_justif_bon_mission_mission_fkey FOREIGN KEY (mission)
      REFERENCES yvs_grh_missions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_compta_justif_bon_mission DROP CONSTRAINT yvs_compta_piece_caisse_mission_piece_fkey;

ALTER TABLE yvs_compta_justif_bon_mission
  ADD CONSTRAINT yvs_compta_justif_bon_mission_piece_fkey FOREIGN KEY (piece)
      REFERENCES yvs_compta_bon_provisoire (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE; 
	  
ALTER TABLE yvs_compta_justif_bon_mission DROP CONSTRAINT yvs_compta_piece_caisse_mission_pkey;

ALTER TABLE yvs_compta_justif_bon_mission
  ADD CONSTRAINT yvs_compta_justif_bon_mission_pkey PRIMARY KEY(id);

CREATE SEQUENCE yvs_compta_justif_bon_mission_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 3
  CACHE 1;
ALTER TABLE yvs_compta_justif_bon_mission_id_seq
  OWNER TO postgres;

ALTER TABLE yvs_compta_justif_bon_mission ALTER COLUMN id SET DEFAULT nextval('yvs_compta_justif_bon_mission_id_seq'::regclass);
DROP SEQUENCE yvs_compta_piece_caisse_mission_id_seq;

--

ALTER TABLE yvs_prod_composant_nomenclature ADD COLUMN composant_lie integer;
ALTER TABLE yvs_prod_composant_nomenclature
  ADD CONSTRAINT yvs_prod_composant_nomenclature_composant_lie_fkey FOREIGN KEY (composant_lie)
      REFERENCES yvs_prod_composant_nomenclature (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

--
ALTER TABLE yvs_base_article_code_barre DROP COLUMN article;
ALTER TABLE yvs_base_article_code_barre ADD COLUMN conditionnement bigint;
ALTER TABLE yvs_base_article_code_barre
  ADD CONSTRAINT yvs_base_article_code_barre_conditionnement_fkey FOREIGN KEY (conditionnement)
      REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

