-- Function: get_remise(bigint, double precision, double precision, bigint, date)

-- DROP FUNCTION get_remise(bigint, double precision, double precision, bigint, date);

CREATE OR REPLACE FUNCTION get_remise(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE

BEGIN
	return (select get_remise_vente(article_, qte_, prix_, client_, 0, date_));
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise(bigint, double precision, double precision, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise(bigint, double precision, double precision, bigint, date) IS 'retourne la remise d'' article';


-- Function: get_remise_vente(bigint)

-- DROP FUNCTION get_remise_vente(bigint);

CREATE OR REPLACE FUNCTION get_remise_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	remise_ double precision;
	data_ record;
	header_ record;
	total_ double precision;
	qte_ double precision;

BEGIN
	if(id_ is not null and id_ > 0)then	
		select into header_ ed.* from yvs_com_entete_doc_vente ed inner join yvs_com_doc_ventes dv on dv.entete_doc = ed.id where dv.id = id_;
		if(header_ is not null and header_.id is not null)then			
			-- Recupere le montant TTC du contenu de la facture
			select into total_ sum(prix_total) from yvs_com_contenu_doc_vente where doc_vente = id_;
			if(total_ is null)then
				total_ = 0;
			end if;
			-- Recupere le total des couts supplementaire d'une facture
			select into qte_ sum(montant) from yvs_com_cout_sup_doc_vente where doc_vente = id_ and actif = true;
			if(qte_ is null)then
				qte_ = 0;
			end if;
			total_ = total_ + qte_;
			
			-- Recupere le total des quantitées d'une facture
			select into qte_ sum(quantite) from yvs_com_contenu_doc_vente where doc_vente = id_;
			if(qte_ is null)then
				qte_ = 0;
			end if;

			for data_ in select gr.* from yvs_com_grille_remise gr inner join yvs_com_remise re on gr.remise = re.id inner join yvs_com_remise_doc_vente rd on rd.remise = re.id
					where rd.doc_vente = id_ and (re.permanent is true or (header_.date_entete between re.date_debut and re.date_fin)) 
					and ((gr.base = 'QTE' and qte_ between gr.montant_minimal and gr.montant_maximal) or (gr.base = 'CA' and total_ between gr.montant_minimal and gr.montant_maximal))
			loop
				if(data_ is not null and data_.id is not null) then
					if(data_.nature_montant = 'TAUX')then
						remise_ = remise_ + (total_ * (data_.montant_remise /100));
					else
						remise_ = remise_ + data_.montant_remise;
					end if;
				end if;
			end loop;
		end if;
	end if;
	if(remise_ is null or remise_ <=0)then
		remise_ = 0;
	end if;	
	return remise_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise_vente(bigint) IS 'retourne la remise sur une vente';


-- Function: compta_et_balance(bigint, bigint, character varying, character varying, date, date, character varying)

-- DROP FUNCTION com_commission(BIGINT, BIGINT);

CREATE OR REPLACE FUNCTION com_commission(IN plan_ BIGINT, IN vente_ BIGINT)
  RETURNS TABLE(vente BIGINT, contenu BIGINT, facteur BIGINT, taux DOUBLE PRECISION, cible CHARACTER VARYING, nature CHARACTER VARYING) AS
$BODY$
DECLARE 
	taux_ DOUBLE PRECISION DEFAULT 0;
	total_ DOUBLE PRECISION DEFAULT 0;
	remise_ DOUBLE PRECISION DEFAULT 0;
	marge_ DOUBLE PRECISION DEFAULT 0;
	
	facteur_ RECORD;
	type_ RECORD;
	client_ RECORD;
	article_ RECORD;
	tranche_ RECORD;
	
	contenu_ BIGINT DEFAULT 0;

	evalue_ BOOLEAN DEFAULT FALSE;
	verifier_ BOOLEAN DEFAULT FALSE;
	lier_ BOOLEAN DEFAULT FALSE;

	cible_ CHARACTER VARYING DEFAULT '';
	nature_ CHARACTER VARYING DEFAULT 'T';
	
BEGIN 	
-- 	DROP TABLE IF EXISTS table_com_commission;
	CREATE TEMP TABLE IF NOT EXISTS table_com_commission(_vente BIGINT, _contenu BIGINT, _facteur BIGINT, _taux DOUBLE PRECISION, _cible CHARACTER VARYING, _nature CHARACTER VARYING); 
	DELETE FROM table_com_commission;
	FOR facteur_ IN SELECT * FROM yvs_com_facteur_taux f WHERE f.plan = plan_
	LOOP
		taux_ = 0;
		cible_ = '';
		nature_ = 'T';
		contenu_ = 0;
		lier_ = FALSE;
		evalue_ = FALSE;
		verifier_ = FALSE;
		
		
		SELECT INTO type_ * FROM yvs_com_type_grille t WHERE t.id = facteur_.type_grille;
		IF(facteur_.general)THEN -- Verification si le facteur est général donc n'est rattaché à aucun element
			total_ = (SELECT get_ttc_vente(vente_));
			remise_ = (SELECT get_remise_vente(vente_));
			evalue_ = TRUE;
		ELSE
			IF(facteur_.nouveau_client)THEN -- Verification si le facteur est lié aux nouveaux clients
				SELECT INTO client_ c.id, COALESCE(c.date_entete, CURRENT_DATE) date_creation FROM yvs_com_entete_doc_vente c INNER JOIN yvs_com_doc_ventes d ON d.entete_doc = c.id 
					INNER JOIN  yvs_com_doc_ventes y ON y.client = d.client
				WHERE y.id = vente_ ORDER BY c.date_entete LIMIT 1;
				IF(current_date - 30 <= client_.date_creation)THEN   -- Verification si le client est un nouveau client
					verifier_ = TRUE;
				END IF;
			ELSE
				verifier_ = TRUE;
			END IF;
			IF(verifier_)THEN
				IF(facteur_.article IS NOT NULL AND facteur_.article > 0)THEN 	-- Verification si le facteur est liée a un article
					lier_ = TRUE;
					IF(facteur_.categorie IS NOT NULL AND facteur_.categorie > 0)THEN   -- Verification si le facteur est lié à une catégorie de client
						SELECT INTO article_ c.id, COALESCE(c.prix_total, 0) AS prix_total FROM yvs_com_contenu_doc_vente c 
							INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id INNER JOIN yvs_com_categorie_tarifaire t ON t.client = d.client
						WHERE c.doc_vente = vente_ AND c.article = facteur_.article AND t.categorie = facteur_.categorie;
					ELSE
						SELECT INTO article_ c.id, COALESCE(c.prix_total, 0) AS prix_total FROM yvs_com_contenu_doc_vente c WHERE c.doc_vente = vente_ AND c.article = facteur_.article;
					END IF;
					IF(article_.id IS NOT NULL AND article_.id > 0)THEN	-- Verification si la facture est liée à l'article du facteur	
						total_ = article_.prix_total;
						remise_ = article_.remise;
						evalue_ = TRUE;	
					END IF;	
				ELSIF(facteur_.categorie IS NULL OR facteur_.categorie < 1)THEN -- Verification si le facteur est lié à une catégorie de client
					SELECT INTO client_ c.client FROM yvs_com_categorie_tarifaire c WHERE c.categorie = facteur_.categorie AND c.client = client_.id;
					IF(client_.client IS NOT NULL AND client_.client > 0)THEN -- Verification si le client est de la catégorie du facteur
						total_ = (SELECT get_ttc_vente(vente_));
						remise_ = (SELECT get_remise_vente(vente_));
						evalue_ = TRUE;	
					END IF;
				ELSE
					total_ = (SELECT get_ttc_vente(vente_));
					remise_ = (SELECT get_remise_vente(vente_));
					evalue_ = TRUE;						
				END IF;
			END IF;
		END IF;
		IF(evalue_)THEN
			IF(type_.id IS NULL OR type_.id < 1)THEN
				taux_ = COALESCE(facteur_.taux, 0);
			ELSE
				cible_ = type_.cible;
				CASE cible_
					WHEN 'C' THEN
						SELECT INTO tranche_ t.id, COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type_grille AND (t.tranche_minimal <= total_ AND t.tranche_maximal >= total_);
					WHEN 'R' THEN
						IF(remise_ > 0)THEN
							remise_ = (remise_ / (total_ + remise_));
						END IF;
						SELECT INTO tranche_ t.id,COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type_grille AND (t.tranche_minimal <= remise_ AND t.tranche_maximal >= remise_);
					WHEN 'M' THEN
						SELECT INTO tranche_ t.id,COALESCE(t.taux, 0) AS taux, COALESCE(t.nature, 'T') AS nature FROM yvs_com_tranche_taux t WHERE t.type_grille = facteur_.type_grille AND (t.tranche_minimal <= marge_ AND t.tranche_maximal >= marge_);
				END CASE;
				IF(tranche_.id IS NOT NULL AND tranche_.id >0)THEN
					taux_ = tranche_.taux;
					nature_ = tranche_.nature;
				END IF;
			END IF;
			IF(lier_)THEN
				FOR contenu_ IN SELECT c.id FROM yvs_com_contenu_doc_vente c WHERE c.doc_vente = vente_ AND c.article = facteur_.article
				LOOP
					INSERT INTO table_com_commission VALUES(vente_, contenu_, facteur_.id, taux_, cible_, nature_);
				END LOOP;
			ELSE
				INSERT INTO table_com_commission VALUES(vente_, contenu_, facteur_.id, taux_, cible_, nature_);
			END IF;
		END IF;
	END LOOP;
	RETURN QUERY SELECT * FROM table_com_commission ORDER BY _vente;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_commission(BIGINT, BIGINT)
  OWNER TO postgres;


  
  -- Function: equilibre_vente(bigint)

-- DROP FUNCTION equilibre_vente(bigint);

CREATE OR REPLACE FUNCTION equilibre_vente(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	av_ double precision default 0;
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
		if(av_>=ttc_)then
			update yvs_com_doc_ventes set statut_regle = 'P' where statut = 'V' and id = id_;
		elsif (av_ > 0) then
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


-- Function: action_on_contenu_facture_vente()

-- DROP FUNCTION action_on_contenu_facture_vente();

CREATE OR REPLACE FUNCTION action_on_contenu_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	vente_ BIGINT DEFAULT 0;
	id_ BIGINT DEFAULT 0;
	parent_ BIGINT DEFAULT 0;
	action_ character varying;
	total_ double precision default 0;
	encours_ double precision default 0;
	valide_ double precision default 0;
	record_ RECORD;
	execute_ BOOLEAN DEFAULT TRUE;
	
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT') THEN
		vente_ = NEW.doc_vente;
		id_ = NEW.id;
		parent_ = NEW.parent;
		encours_= (COALESCE(NEW.quantite, 0) + COALESCE(NEW.quantite_bonus,0));
	ELSIF (action_='UPDATE') THEN 
		vente_ = NEW.doc_vente;	  
		id_ = NEW.id;       
		parent_ = NEW.parent;  
		encours_= (COALESCE(NEW.quantite, 0) + COALESCE(NEW.quantite_bonus, 0));
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		vente_ = OLD.doc_vente;	
		id_ = OLD.id;
		parent_ = OLD.parent;
		encours_= (COALESCE(OLD.quantite, 0) + COALESCE(OLD.quantite_bonus, 0));
		execute_ = FALSE;	    	 
	END IF;

	SELECT INTO record_ * FROM yvs_com_doc_ventes WHERE id = vente_;
	IF(record_.statut = 'V')THEN
		if(execute_ = TRUE)THEN
			IF(record_.type_doc = 'FV')THEN
				SELECT INTO total_ COALESCE(SUM(c.quantite), 0) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id WHERE d.statut = 'V' AND parent = id_;
				IF(encours_ <= total_)THEN
					NEW.statut_livree = 'L';
				ELSIF(total_ > 0)THEN
					NEW.statut_livree = 'R';
				ELSE
					NEW.statut_livree = 'W';
				END IF;
			ELSIF(record_.type_doc = 'BLV')THEN
				SELECT INTO total_ COALESCE(SUM(c.quantite), 0) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id WHERE d.statut = 'V' AND c.parent = parent_;
				IF(encours_ <= total_)THEN
					UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'L' WHERE id = parent_;
				ELSIF(total_ > 0)THEN
					UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'R' WHERE id = parent_;
				ELSE
					UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'W' WHERE id = parent_;
				END IF;
			END IF;
		ELSIF(record_.type_doc = 'BLV')THEN
			SELECT INTO total_ COALESCE(SUM(c.quantite), 0) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id WHERE d.statut = 'V' AND c.parent = parent_;
			IF(encours_ <= total_)THEN
				UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'L' WHERE id = parent_;
			ELSIF(total_ > 0)THEN
				UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'R' WHERE id = parent_;
			ELSE
				UPDATE yvs_com_contenu_doc_vente SET statut_livree = 'W' WHERE id = parent_;
			END IF;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_contenu_facture_vente()
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
					societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
					line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_));
			vente_ = NEW.vente;
		ELSIF (action_='UPDATE') THEN 
		      SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		      IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse
			  SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_,
			      reference_externe=line_.num_doc, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
					  statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			  WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		       ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
							   societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers)
					   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier,null, id_tiers_,'R', (nom_ ||' '||prenom_));

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

  
  -- Function: compta_action_on_piece_caisse_vente()

-- DROP FUNCTION compta_action_on_piece_caisse_vente();

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
	echeance_ RECORD;
	vente_ BIGINT;
	solde_ double precision default 0;
	payer_ double precision default 0;
	
BEGIN	
   action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
    --find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  cu.societe, pv.num_refrence as num_doc, cl.tiers as client, cl.nom, cl.prenom  FROM yvs_compta_acompte_client pv INNER JOIN yvs_com_client cl ON cl.id=pv.client
			INNER JOIN yvs_base_tiers cu ON cu.id=cl.tiers WHERE pv.id = NEW.id; 
									    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		id_tiers_= line_.client;
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
				societe, author, caisse, tiers_externe, mouvement, name_tiers)
		VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'yvs_compta_acompte_client', NEW.montant, line_.num_doc, NEW.date_acompte, current_date, current_date,  NEW.statut, 
				line_.societe, NEW.author, NEW.caisse, id_tiers_,'R', (nom_ ||' '||prenom_));
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='yvs_compta_acompte_client' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_refrence, note=NEW.commentaire, montant=NEW.montant, tiers_externe=id_tiers_,
				reference_externe=line_.num_doc, date_mvt=NEW.date_acompte, date_paiment_prevu=current_date, date_paye=current_date, 
				statut_piece=NEW.statut, caisse=NEW.caisse, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='yvs_compta_acompte_client' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				   societe, author, caisse,caissier, tiers_externe, mouvement, name_tiers)
			VALUES (NEW.num_refrence, NEW.id, NEW.commentaire, 'yvs_compta_acompte_client', NEW.montant, line_.num_doc, NEW.date_acompte, current_date, current_date,  NEW.statut, 
				line_.societe,NEW.author,NEW.caisse, NEW.caissier, id_tiers_,'R', (nom_ ||' '||prenom_));
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='yvs_compta_acompte_client' AND id_externe=OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_acompte_client()
  OWNER TO postgres;
