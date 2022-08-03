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
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, 'D',(nom_||' '||prenom_), NEW.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, model = NEW.model, 
				date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', societe=line_.societe, name_tiers=(nom_||' '||prenom_)
				WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe, mouvement, name_tiers, model)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'D',(nom_||' '||prenom_), NEW.model);
		END IF;
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

  
  -- Function: compta_action_on_piece_caisse_divers()

-- DROP FUNCTION compta_action_on_piece_caisse_divers();

CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  dv.societe, dv.num_piece as num_doc, dv.tiers, dv.mouvement FROM yvs_compta_caisse_piece_divers pv 
			INNER JOIN yvs_compta_caisse_doc_divers dv ON dv.id=pv.doc_divers WHERE pv.id=NEW.id; 
								    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		--récupérer nom du tiers
		IF(line_.tiers IS NOT NULL) THEN
		  SELECT INTO tiers_ t.* FROM yvs_base_tiers t WHERE t.id=line_.tiers;
		END IF;
		IF(tiers_.nom IS NOT NULL) THEN
			nom_=tiers_.nom;
		END IF;
		IF(tiers_.prenom IS NOT NULL) THEN
			prenom_=tiers_.prenom;
		END IF;
		
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.num_piece, NEW.id, NEW.note, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_piece,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, line_.mouvement, (nom_ ||' '||prenom_), NEW.mode_paiement);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, model = NEW.mode_paiement,
				date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_piece, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement=line_.mouvement, societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
				WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe, mouvement,name_tiers, model)
				VALUES (NEW.num_piece, NEW.id, NEW.note, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_piece,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,line_.mouvement, (nom_ ||' '||prenom_), NEW.mode_paiement);
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=OLD.id;	
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_divers()
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
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;
	-- Verification si la piece n'est pas générée par un acompte
	SELECT INTO line_ y.id FROM yvs_compta_notif_reglement y WHERE y.piece_vente = id_el_;
	IF(line_.id IS NULL OR line_.id < 1)THEN	
		--find societe concerné
		IF(action_='INSERT' OR action_='UPDATE') THEN
			  SELECT INTO line_  ag.societe, dv.num_doc,cu.users, dv.client, cl.nom, cl.prenom  FROM yvs_compta_caisse_piece_vente pv INNER JOIN yvs_com_doc_ventes dv ON dv.id=pv.vente INNER JOIN yvs_com_client cl ON cl.id=dv.client INNER JOIN yvs_com_entete_doc_vente en ON en.id=dv.entete_doc
										    INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=en.creneau INNER JOIN yvs_com_creneau_point cr ON cr.id=cu.creneau_point
										    INNER JOIN yvs_base_point_vente dp ON dp.id=cr.point INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE pv.id=NEW.id; 
										    
			  IF(line_.societe IS NULL) THEN
				SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
			  END IF;
			  IF(NEW.client IS NOT NULL) THEN
				id_tiers_=NEW.client;
			  ELSE
				id_tiers_=line_.client;
			  END IF;
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
		END IF;         
		IF(action_='INSERT') THEN
		    -- Récupère le document
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
					societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
					line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
			vente_ = NEW.vente;
		ELSIF (action_='UPDATE') THEN 
		      SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		      IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse
			  SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
			      reference_externe=line_.num_doc, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
					  statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			  WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		       ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
							   societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
					   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier,null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);

		       END IF;	
			vente_ = NEW.vente;	           
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=OLD.id;		
			vente_ = OLD.vente;		    	 
		END IF;
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
id_tiers_ bigint;
	
BEGIN	
   action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
    --find societe concerné
		IF(action_='INSERT' OR action_='UPDATE') THEN
	  --trouve le nom du caissier
	  SELECT INTO user_ u.* FROM yvs_users u WHERE u.id=NEW.caissier_source;
          SELECT INTO line_  n.societe FROM yvs_base_caisse c INNER JOIN  yvs_base_plan_comptable cp ON c.compte=cp.id INNER JOIN yvs_base_nature_compte n ON n.id=cp.nature_compte
							      WHERE c.id=NEW.source;
	
	  END IF;
	IF(action_='INSERT') THEN
	    -- Insère la sortie
	   INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
											   societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
									   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', NEW.montant, NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
											  line_.societe,NEW.author,NEW.source, NEW.caissier_source, NEW.caissier_source, null,'D', user_.nom_users, NEW.model);
	   -- Insère l'entré
	   INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
											   societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement,name_tiers, model)
									   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', NEW.montant, NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
											  line_.societe,NEW.author,NEW.cible, NEW.caissier_cible, NEW.caissier_source, null,'R', user_.nom_users, NEW.model);
	ELSIF (action_='UPDATE') THEN 
	      SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse
				  SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_interne=NEW.caissier_source, model = NEW.model,
					  reference_externe=NEW.numero_piece, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
						  statut_piece=NEW.statut_piece, caisse=NEW.source, caissier=NEW.caissier_source, mouvement='D', societe=line_.societe, name_tiers=user_.nom_users
						  WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id AND mouvement='D';
						  ---
				  UPDATE yvs_compta_mouvement_caisse
				  SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_interne=NEW.caissier_source, model = NEW.model,
					  reference_externe=NEW.numero_piece, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
						  statut_piece=NEW.statut_piece, caisse=NEW.cible, caissier=NEW.caissier_cible, mouvement='R', societe=line_.societe, name_tiers=user_.nom_users
						  WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id AND mouvement='R';
			ELSE
					 -- Insère la sortie
						INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
											   societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement,name_tiers, model)
									   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', NEW.montant, NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
											  line_.societe,NEW.author,NEW.source, NEW.caissier_source, NEW.caissier_source, null,'D',user_.nom_users, NEW.model);
					 -- Insère l'entré
						INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
															   societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
									   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', NEW.montant, NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
											  line_.societe,NEW.author,NEW.cible, NEW.caissier_cible, NEW.caissier_source, null,'R',user_.nom_users, NEW.model);
			END IF;
	           
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse
		WHERE table_externe='DOC_VIREMENT' AND id_externe=OLD.id;	
		    	 
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_virement()
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
	nom_ character varying default '';
	prenom_ character varying default '';
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, m.numero_mission, m.employe, e.nom, e.prenom FROM yvs_compta_piece_caisse_mission pm 
			INNER JOIN yvs_grh_missions m ON m.id=pm.mission INNER JOIN yvs_grh_employes e ON e.id = m.employe
			INNER JOIN yvs_agences ag ON ag.id=e.agence WHERE m.id=NEW.mission LIMIT 1; 
								    
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
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, caissier, statut_piece, 
												societe, author, caisse, mouvement, tiers_employe, tiers_externe, name_tiers, model)
			   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'MISSION', NEW.montant,line_.numero_mission, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement, NEW.caissier, NEW.statut_piece, 
					   line_.societe,NEW.author, NEW.caisse, 'D',line_.employe,NULL, (nom_ ||' '||prenom_), NEW.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='MISSION' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, reference_externe=line_.numero_mission, date_mvt=NEW.date_piece, 
				date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', societe=line_.societe,
				tiers_employe=line_.employe, name_tiers=(nom_ ||' '||prenom_), model = NEW.model
				WHERE table_externe='MISSION' AND id_externe= NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, caissier, statut_piece, 
												societe, author, caisse, mouvement, tiers_employe, tiers_externe, name_tiers, model)
			   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'MISSION', NEW.montant,line_.numero_mission, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement, NEW.caissier, NEW.statut_piece, 
					   line_.societe,NEW.author, NEW.caisse, 'D',line_.employe,NULL,(nom_ ||' '||prenom_), NEW.model);
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='MISSION' AND id_externe=OLD.id;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_insert_piece_mission()
  OWNER TO postgres;
  
  
  -- Function: get_ttc_achat(bigint)

-- DROP FUNCTION get_ttc_achat(bigint);

CREATE OR REPLACE FUNCTION get_ttc_achat(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	qte_ double precision default 0;
	cs_ double precision default 0;
	data_ record;

BEGIN
	-- Recupere le montant TTC du contenu de la facture
	select into total_ sum((quantite_attendu * (pua_attendu)) - remise_attendu + taxe) from yvs_com_contenu_doc_achat where doc_achat = id_;
	if(total_ is null)then
		total_ = 0;
	end if;
	
	-- Recupere le total des couts supplementaire d'une facture
	select into cs_ sum(o.montant) from yvs_com_cout_sup_doc_achat o where o.doc_achat = id_ and o.actif is true;
	if(cs_ is null)then
		cs_ = 0;
	end if;
	total_ = total_ + cs_;
		
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ttc_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ttc_achat(bigint) IS 'retourne le montant TTC d''un doc achat';

-- Function: delete_contenu_doc_stock()

-- DROP FUNCTION delete_contenu_doc_stock();

CREATE OR REPLACE FUNCTION delete_contenu_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE

BEGIN
	delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_contenu_doc_stock()
  OWNER TO postgres;

  -- Function: delete_contenu_doc_achat()

-- DROP FUNCTION delete_contenu_doc_achat();

CREATE OR REPLACE FUNCTION delete_contenu_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE

BEGIN
	delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_contenu_doc_achat()
  OWNER TO postgres;

  -- Function: delete_contenu_doc_vente()

-- DROP FUNCTION delete_contenu_doc_vente();

CREATE OR REPLACE FUNCTION delete_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE

BEGIN
	delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_contenu_doc_vente()
  OWNER TO postgres;

  -- Function: equilibre_vente(bigint)

-- DROP FUNCTION equilibre_vente(bigint);

CREATE OR REPLACE FUNCTION equilibre_vente(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	av_ double precision default 0;
	ch_ bigint default 0;
	contenu_ record;
	qte_ double precision default 0;
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

BEGIN
	-- Equilibre de l'etat reglé
	ttc_ = (select get_ttc_vente(id_));
	select into av_ sum(montant) from yvs_compta_caisse_piece_vente where vente = id_ and statut_piece = 'P';
	if(av_ is null)then
		av_ = 0;
	end if;

	-- Equilibre de l'etat livré
	for contenu_ in select article, sum(quantite + coalesce(quantite_bonus,0)) as qte from yvs_com_contenu_doc_vente where doc_vente = id_ group by article
	loop
		in_ = true;
		select into qte_ sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id where d.document_lie = id_ and c.article = contenu_.article and d.statut = 'V';
		if(qte_ is null)then
			qte_ = 0;
		end if;
		if(qte_ < contenu_.qte)then
			correct = false;
			if(qte_ > 0)then
				encours = true;
				exit;
			end if;
		end if;
	end loop;
	if(in_)then
		select into ch_ count(y.id) from yvs_compta_caisse_piece_vente y inner join yvs_base_mode_reglement m on y.model = m.id where y.vente = id_ and m.type_reglement = 'BANQUE';
		if(av_>=ttc_)then
			update yvs_com_doc_ventes set statut_regle = 'P' where statut = 'V' and id = id_;
		elsif (av_ > 0 or ch_ > 0) then
			update yvs_com_doc_ventes set statut_regle = 'R' where id = id_;
		else
			update yvs_com_doc_ventes set statut_regle = 'W' where id = id_;
		end if;
		
		if(correct)then
			update yvs_com_doc_ventes set statut_livre = 'L' where id = id_;
		else
			if(encours)then
				update yvs_com_doc_ventes set statut_livre = 'R' where id = id_;
			else
				update yvs_com_doc_ventes set statut_livre = 'W' where id = id_;
			end if;
		end if;	
	else
		update yvs_com_doc_ventes set statut_regle = 'W', statut_livre = 'W' where id = id_;
	end if;	
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de vente';

-- Function: com_calcul_commission(bigint)

-- DROP FUNCTION com_calcul_commission(bigint);

CREATE OR REPLACE FUNCTION com_calcul_commission(IN periode_ bigint)
  RETURNS TABLE(vente BIGINT) AS
$BODY$
DECLARE 	
	commercial_ RECORD;
	commission_ RECORD;
	facture_ RECORD;
	facteur_ RECORD;
	ligne_ RECORD;

	vente_ BIGINT DEFAULT 0;
	
	date_ DATE DEFAULT CURRENT_DATE;

	somme_ DOUBLE PRECISION DEFAULT 0;
	valeur_ DOUBLE PRECISION DEFAULT 0;
BEGIN 	
-- 	DROP TABLE IF EXISTS table_calcul_commission;
	CREATE TEMP TABLE IF NOT EXISTS table_calcul_commission(_vente BIGINT); 
	DELETE FROM table_calcul_commission;

	-- Recuperation des informations de la periode
	SELECT INTO ligne_ y.* FROM yvs_com_periode_objectif y WHERE y.id = periode_;
	IF(ligne_.id IS NOT NULL AND ligne_.id > 0)THEN
		-- Recherche des factures enregistrées dans la période
		FOR facture_ IN SELECT y.* FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id LEFT JOIN yvs_compta_caisse_piece_vente c ON c.vente = y.id 
		WHERE (y.statut NOT IN ('E','A') AND e.date_entete BETWEEN ligne_.date_debut AND ligne_.date_fin) OR (c.statut_piece = 'P' AND c.date_paiement BETWEEN ligne_.date_debut AND ligne_.date_fin)
		LOOP
			valeur_ = 0;
			vente_ = 0;
			somme_ = 0;
			-- Vérification si la facture est rattachée à un commercial responsable
			SELECT INTO commercial_ y.* FROM yvs_com_comerciale y INNER JOIN yvs_com_commercial_vente c ON c.commercial = y.id WHERE c.facture = facture_.id AND c.responsable;
			IF(commercial_.id IS NOT NULL AND commercial_.id > 0)THEN
				-- Recuperation des taux de commission de la facture calculées pour le plan de commission du responsable
				FOR commission_ IN SELECT * FROM com_commission(COALESCE(commercial_.commission, 0), facture_.id)
				LOOP			
					vente_ = 0;		
					-- Recuperation des informations du facteur de taux
					SELECT INTO facteur_ y.* FROM yvs_com_facteur_taux y WHERE y.id = commission_.facteur;
					IF(facteur_.champ_application = 'R')THEN -- Facteur de taux appliqué sur les factures reglées
						SELECT INTO date_ COALESCE(y.date_paiement, CURRENT_DATE) FROM yvs_compta_caisse_piece_vente y WHERE y.statut_piece = 'P' AND y.vente = facture_.id ORDER BY date_paiement DESC LIMIT 1;
						IF(date_ BETWEEN ligne_.date_debut AND ligne_.date_fin)THEN
							vente_ = facture_.id;
						END IF;
					ELSE -- Facteur de taux appliqué sur les factures validées
						IF(facture_.date_valider BETWEEN ligne_.date_debut AND ligne_.date_fin)THEN
							vente_ = facture_.id;
						END IF;
					END IF;
					IF(vente_ > 0)THEN
						IF(commission_.nature = 'T')THEN
							IF(commission_.contenu IS NOT NULL AND commission_.contenu > 0)THEN
								CASE facteur_.base
									WHEN 'H' THEN -- Facteur de taux basé sur le valeur HT
										SELECT INTO somme_ ((COALESCE(y.quantite, 0) * (COALESCE(y.prix, 0) - COALESCE(y.rabais, 0))- COALESCE(y.remise, 0))) FROM yvs_com_contenu_doc_vente y WHERE id = commission_.contenu;
									WHEN 'T' THEN -- Facteur de taux basé sur le valeur TTC
										SELECT INTO somme_ (COALESCE(y.prix_total, 0)) FROM yvs_com_contenu_doc_vente y WHERE id = commission_.contenu;
									WHEN 'M' THEN -- Facteur de taux basé sur le valeur Marge
										SELECT INTO somme_ (COALESCE(y.prix_total, 0) - ((COALESCE(y.prix, 0) - COALESCE(y.pr, 0))* COALESCE(y.quantite, 0))) FROM yvs_com_contenu_doc_vente y WHERE id = commission_.contenu;
								END CASE;
								somme_ = (somme_ * commission_.taux) / 100;
								UPDATE yvs_com_contenu_doc_vente SET comission = somme_ WHERE id = commission_.contenu;
							ELSE
								CASE facteur_.base
									WHEN 'H' THEN -- Facteur de taux basé sur le valeur HT
										SELECT INTO somme_ (SUM(COALESCE(y.quantite, 0) * (COALESCE(y.prix, 0) - COALESCE(y.rabais, 0))- COALESCE(y.remise, 0))) FROM yvs_com_contenu_doc_vente y WHERE doc_vente = vente_;
									WHEN 'T' THEN -- Facteur de taux basé sur le valeur TTC
										SELECT INTO somme_ (SUM(COALESCE(y.prix_total, 0))) FROM yvs_com_contenu_doc_vente y WHERE doc_vente = vente_;
									WHEN 'M' THEN -- Facteur de taux basé sur le valeur Marge
										SELECT INTO somme_ (SUM(COALESCE(y.prix_total, 0) - ((COALESCE(y.prix, 0) - COALESCE(y.pr, 0))* COALESCE(y.quantite, 0)))) FROM yvs_com_contenu_doc_vente y WHERE doc_vente = vente_;
								END CASE;
								somme_ = (somme_ * commission_.taux) / 100;
							END IF;
							valeur_ = valeur_ + somme_;
						ELSE
							IF(commission_.contenu IS NOT NULL AND commission_.contenu > 0)THEN
								UPDATE yvs_com_contenu_doc_vente SET comission = commission_.taux WHERE id = commission_.contenu;
							END IF;
							valeur_ = valeur_ + commission_.taux;
						END IF;
						UPDATE yvs_com_doc_ventes SET commision = valeur_ WHERE id = vente_;
					END IF;
				END LOOP;
				IF(valeur_ > 0)THEN
					INSERT INTO table_calcul_commission VALUES(vente_);
				END IF;
			END IF;
		END LOOP; 
	END IF;	
	RETURN QUERY SELECT * FROM table_calcul_commission ORDER BY _vente;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_calcul_commission(bigint)
  OWNER TO postgres;



  
update yvs_grh_type_cout set module_cout = 'L' where module_cout is null;
update yvs_grh_type_cout set module_cout = 'M' where module_cout = 'GRH';
update yvs_grh_type_cout set module_cout = 'V' where module_cout = 'COM';
  
update yvs_compta_mouvement_caisse set model = (select y.model from yvs_compta_piece_caisse_mission y where y.id = id_externe) where model is null and table_externe = 'MISSION';
update yvs_compta_mouvement_caisse set model = (select y.model from yvs_compta_caisse_piece_virement y where y.id = id_externe) where model is null and table_externe = 'DOC_VIREMENT';
update yvs_compta_mouvement_caisse set model = (select y.model from yvs_compta_caisse_piece_vente y where y.id = id_externe) where model is null and table_externe = 'DOC_VENTE';
update yvs_compta_mouvement_caisse set model = (select y.mode_paiement from yvs_compta_caisse_piece_divers y where y.id = id_externe) where model is null and table_externe = 'DOC_DIVERS';
update yvs_compta_mouvement_caisse set model = (select y.model from yvs_compta_caisse_piece_achat y where y.id = id_externe) where model is null and table_externe = 'DOC_ACHAT';

update yvs_compta_piece_caisse_mission set model = 2297 where model is null and statut_piece = 'P';
update yvs_com_grille_ristourne set article = (select y.article from yvs_com_ristourne y where y.id = ristourne) where article is null;