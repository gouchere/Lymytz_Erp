-- Function: compta_et_balance(bigint, bigint, character varying, character varying, date, date, character varying)

-- DROP FUNCTION compta_et_balance(bigint, bigint, character varying, character varying, date, date, character varying);

CREATE OR REPLACE FUNCTION connexion()
  RETURNS character varying AS
$BODY$
declare 
	
begin 	
	return 'dbname=lymytz_demo_0 user=postgres password=yves1910/';
end;
$BODY$
  LANGUAGE plpgsql VOLATILE;
ALTER FUNCTION connexion()
  OWNER TO postgres;
  
CREATE EXTENSION hstore;


-- Function: compta_et_balance(bigint, bigint, character varying, character varying, date, date, character varying)

-- DROP FUNCTION stat_et_exportation(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION stat_et_exportation(IN societe_ bigint, IN report_ character varying, IN ids_ character varying)
  RETURNS TABLE(numero bigint, entete character varying, valeur character varying, "type" character varying) AS
$BODY$
declare 
	etat_ RECORD;
	colonnes_ RECORD;
	data_ RECORD;

	column_ INTEGER DEFAULT 0;
	i INTEGER DEFAULT 0;
	numero_ INTEGER DEFAULT 0;
	
	id_ character varying default '';
	select_ character varying default '';
	from_ character varying default '';
	order_ character varying default '';
	where_ character varying default '';
	jointure_ character varying default '';
	query_ character varying default '';
	type_ character varying default '';

	value_ text;
	hstoredata hstore;
	reqfield text := 'my_num';   -- assigning at declaration time for convenience

	with_orderby BOOLEAN DEFAULT FALSE;
	first_ BOOLEAN DEFAULT TRUE;

	tab_table_ character varying[];
	tab_column_ character varying[];
	tab_header_ character varying[];

begin 	
-- 	DROP TABLE IF EXISTS table_et_exportation;
	CREATE TEMP TABLE IF NOT EXISTS table_et_exportation(_numero bigint, _entete character varying, _valeur character varying, _type character varying); 
	DELETE FROM table_et_exportation;
	SELECT INTO etat_ e.* FROM yvs_stat_export_etat e WHERE e.reference = report_ AND e.societe = societe_;
	IF(etat_.id IS NOT NULL AND etat_.id > 0)THEN
		from_ = etat_.table_principal ||' '|| etat_.table_principal;
                IF(COALESCE(etat_.order_by, '') NOT IN ('', ' '))THEN
			with_orderby = TRUE;
		END IF;
		IF (with_orderby) THEN
			order_ = ' ORDER BY ' || etat_.order_by;
		END IF;
		IF(etat_.type_formule = 'S')THEN
			FOR colonnes_ IN SELECT c.* FROM yvs_stat_export_colonne c WHERE c.etat = etat_.id ORDER BY c.ordre
			LOOP
				IF(colonnes_.visible)THEN
					IF(column_ = 0)THEN
						IF(colonnes_.colonne_date)THEN
							select_ = 'to_char(' || colonnes_.table_name || '.' || colonnes_.colonne || ', ''' || colonnes_.format_date || ''')';
						ELSE
							select_ = colonnes_.table_name || '.' || colonnes_.colonne;
						END IF;
					ELSE
						IF(colonnes_.colonne_date)THEN
							select_ = select_ ||', '|| 'to_char(' || colonnes_.table_name || '.' || colonnes_.colonne || ', ''' || colonnes_.format_date || ''') as '||colonnes_.colonne;
						ELSE
							select_ = select_ ||', '|| colonnes_.table_name || '.' || colonnes_.colonne;
						END IF;
					END IF;
					column_ = column_ + 1;
					tab_table_[column_] = colonnes_.table_name;
					tab_column_[column_] = colonnes_.colonne;
					tab_header_[column_] = colonnes_.libelle;
				END IF;
				IF(colonnes_.contrainte)THEN
					IF(colonnes_.sens_contrainte = 'N')THEN
						jointure_ = colonnes_.table_name_liee;
					ELSE
						jointure_ = colonnes_.table_name;
					END IF;
					jointure_ = jointure_ || ' ON ' || colonnes_.table_name || '.' ||colonnes_.colonne || '=' || colonnes_.table_name_liee || '.' || colonnes_.colonne_liee;
					IF(from_ !~ jointure_)THEN
						from_ = from_ || ' LEFT JOIN ' ||jointure_;
					END IF;
				END IF;
				IF (with_orderby IS FALSE AND COALESCE(colonnes_.order_by, '') NOT IN ('', ' '))THEN
					IF(COALESCE(order_, '') NOT IN ('', ' '))THEN
						order_ = ' ORDER BY ' || colonnes_.table_name || '.' || colonnes_.colonne;
					ELSE
						order_ = order_ || ', ' || colonnes_.table_name || '.' || colonnes_.colonne;
					END IF;
					IF(colonnes_.order_by = 'D')THEN
						order_ = order_ || ' DESC';
					ELSE
						order_ = order_ || ' ASC';
					END IF;
				END IF;
			END LOOP;

			IF(COALESCE(ids_, '') NOT IN ('', ' '))THEN
				where_ = ' WHERE ' || etat_.table_principal || '.' || etat_.colonne_principal ||' IN (-1';
				FOR id_ IN SELECT id FROM regexp_split_to_table(ids_, ',') id
				LOOP
					where_ = where_ || ', ' || id_;
				END LOOP;
				where_ = where_ || ')';
			END IF;
			query_ = 'SELECT ' || select_ || ' FROM ' || from_ || where_;
			IF(COALESCE(order_, '') NOT IN ('', ' '))THEN
				query_ = query_ || ' ' || order_;
			END IF;
		ELSE
			IF(COALESCE(ids_, '') NOT IN ('', ' '))THEN
				where_ = ' ' || etat_.table_principal || '.' || etat_.colonne_principal ||' IN (-1';
				FOR id_ IN SELECT id FROM regexp_split_to_table(ids_, ',') id
				LOOP
					where_ = where_ || ', ' || id_;
				END LOOP;
				where_ = where_ || ')';
			END IF;
			IF(etat_.formule ~ '?')THEN
				query_ = (SELECT REPLACE(etat_.formule, '?', where_));
			END IF;
		END IF;
		RAISE NOTICE 'query_ %',query_;
		IF(COALESCE(query_, '') NOT IN ('', ' '))THEN
			numero_ = 0;
			FOR data_ IN EXECUTE query_
			LOOP
				numero_ = numero_ + 1;
				hstoredata = (SELECT hstore(data_));
				FOR i IN 1..column_ LOOP
					reqfield := tab_column_[i];   -- assigning at declaration time for convenience
					EXECUTE 'SELECT data_type FROM information_schema.columns WHERE table_name = '''||tab_table_[i]||''' AND column_name = '''||tab_column_[i]||'''' INTO type_;
					value_ := hstoredata -> reqfield::text;
					INSERT INTO table_et_exportation VALUES(numero_, tab_header_[i], value_, type_);
				END LOOP;
			END LOOP;
		END IF;
	END IF;
	return QUERY SELECT * from table_et_exportation order by numero;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION stat_et_exportation(bigint, character varying, character varying)
  OWNER TO postgres;
  
  -- Function: et_total_pt_vente(bigint, date, date, character varying)

-- DROP FUNCTION et_total_pt_vente(bigint, date, date, character varying);

CREATE OR REPLACE FUNCTION et_total_pt_vente(IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, point bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    points_ bigint;
    total_ record;
    nbre integer default 0;

BEGIN    
	DROP TABLE IF EXISTS point_vente_by_jour_;
	CREATE TEMP TABLE IF NOT EXISTS point_vente_by_jour_(_code character varying, _nom character varying, pt bigint, jr character varying, ttc double precision, qte double precision, ttx double precision, pos integer, total boolean, footer boolean);
	if(agence_ is null or agence_ < 1)then
		for points_ in select distinct y.id from yvs_base_point_vente y inner join yvs_com_creneau_point o on o.point = y.id inner join yvs_com_creneau_horaire_users u on u.creneau_point = o.id inner join yvs_com_entete_doc_vente e on u.id = e.creneau
		loop
			insert into point_vente_by_jour_ select *, false from et_total_one_pt_vente(null, points_, date_debut_, date_fin_, periode_);
		end loop;

	else
		for points_ in select distinct y.id from yvs_base_point_vente y inner join yvs_com_creneau_point o on o.point = y.id inner join yvs_com_creneau_horaire_users u on u.creneau_point = o.id inner join yvs_com_entete_doc_vente e on u.id = e.creneau where y.agence = agence_
		loop
			insert into point_vente_by_jour_ select *, false from et_total_one_pt_vente(agence_, points_, date_debut_, date_fin_, periode_);
		end loop;
	end if;
	for total_ in select jr as jour, coalesce(sum(ttc), 0) as ttc, coalesce(sum(qte), 0) as qte, coalesce(sum(ttx), 0) as ttx, coalesce(sum(pos), 0) as pos from point_vente_by_jour_ y group by jr
	loop		
		select into nbre count(pos) from point_vente_by_jour_ where jr = total_.jour;
		if(nbre is null or nbre = 0)then
			nbre = 1;
		end if;
		insert into point_vente_by_jour_ values('TOTAUX', 'TOTAUX', 0, total_.jour, total_.ttc, total_.qte, (total_.ttx / nbre), total_.pos + 1, true, true);
	end loop;
	
    return QUERY select * from point_vente_by_jour_ order by footer, pt, pos;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_pt_vente(bigint, date, date, character varying)
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
			IF(tiers_.nom IS NOT NULL) THEN
				nom_=tiers_.nom;
			END IF;
			IF(tiers_.prenom IS NOT NULL) THEN
				prenom_=tiers_.prenom;
			END IF;
			id_tiers_=tiers_.id;
		END IF;
		
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.num_piece, NEW.id, NEW.note, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_piece,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, line_.mouvement, NEW.beneficiaire, NEW.mode_paiement);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, model = NEW.mode_paiement,
				date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_piece, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement=line_.mouvement, societe=line_.societe, name_tiers=NEW.beneficiaire
				WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe, mouvement,name_tiers, model)
				VALUES (NEW.num_piece, NEW.id, NEW.note, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_piece,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,line_.mouvement, NEW.beneficiaire, NEW.mode_paiement);
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
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'R', line_.nom_client, NEW.model);
		vente_ = NEW.vente;
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=line_.num_doc, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
				statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='R', societe=line_.societe, name_tiers=line_.nom_client
			WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,
				NEW.caisse, NEW.caissier,null, id_tiers_,'R', line_.nom_client, NEW.model);
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
  
  
CREATE OR REPLACE FUNCTION update_()
  RETURNS boolean AS
$BODY$   
DECLARE 
	contenu_ RECORD;
	id_ RECORD;
BEGIN
	FOR id_ IN select id, doc_divers from yvs_compta_caisse_piece_divers where beneficiaire is null or beneficiaire in('',' ')
	LOOP
		select into contenu_ * from yvs_compta_bon_provisoire where piece = id_.id;
		if(contenu_.id is not null and contenu_.id > 0)then
			update yvs_compta_caisse_piece_divers set beneficiaire = contenu_.beneficiaire where id = id_.id;
		else
			select into contenu_ * from yvs_compta_caisse_doc_divers where id = id_.doc_divers;
			if(contenu_.tiers is not null and contenu_.tiers > 0)then
				update yvs_compta_caisse_piece_divers set beneficiaire = (select CONCAT(nom, ' ', prenom) from yvs_base_tiers t where t.id = contenu_.tiers ) where id = id_.id;
			end if;
		end if;
	END LOOP;
	RETURN TRUE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_()
  OWNER TO postgres;
  
 select update_();
 
 -- Function: get_pua(bigint, bigint, bigint)

-- DROP FUNCTION get_pua(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION get_pua(article_ bigint, fsseur_ bigint, unite_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	pua_ double precision;

BEGIN
	if(unite_ is not null and unite_ > 0)then
		select into pua_ c.pua_attendu from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat where c.article = article_ and c.conditionnement = unite_ and d.statut = 'V' order by d.date_doc desc limit 1;
		if(pua_ is null or pua_ < 1)then	
			select into pua_ y.pua from yvs_base_conditionnement_fournisseur y inner join yvs_base_article_fournisseur a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.fournisseur = fsseur_ and a.article = article_ and c.id = unite_ limit 1;
			if(pua_ is null or pua_ < 1)then
				select into pua_ prix_achat from yvs_base_conditionnement c where c.id = unite_;
				if(pua_ is null or pua_ < 1)then		
					select into pua_ pua from yvs_base_articles where id = article_;
					if(pua_ is null)then
						pua_ = 0;
					end if;
				end if;
			end if;
		end if;
	else
		select into pua_ c.pua_attendu from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat where c.article = article_ and d.statut = 'V' order by d.date_doc desc limit 1;
		if(pua_ is null or pua_ < 1)then
			select into pua_ puv from yvs_base_article_fournisseur where fournisseur = fsseur_ and article = article_;
			if(pua_ is null or pua_ < 1)then		
				select into pua_ pua from yvs_base_articles where id = article_;
				if(pua_ is null)then
					pua_ = 0;
				end if;
			end if;
		end if;
	end if;
	return pua_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_pua(bigint, bigint, bigint)
  OWNER TO postgres;

UPDATE yvs_compta_caisse_piece_virement SET caissier_source = null WHERE caissier_source IN (SELECT id FROM yvs_users WHERE code_users IS NULL OR code_users IN('', ' '));
UPDATE yvs_compta_caisse_piece_virement SET caissier_cible = null WHERE caissier_cible IN (SELECT id FROM yvs_users WHERE code_users IS NULL OR code_users IN('', ' '));
DELETE FROM yvs_users WHERE code_users IS NULL OR code_users IN('', ' ');
UPDATE yvs_compta_caisse_piece_virement SET caissier_cible = (SELECT e.code_users FROM yvs_grh_employes e INNER JOIN yvs_base_caisse c ON e.id = c.responsable WHERE c.id = cible) WHERE caissier_cible IS NULL;
UPDATE yvs_compta_caisse_piece_virement SET caissier_source = (SELECT e.code_users FROM yvs_grh_employes e INNER JOIN yvs_base_caisse c ON e.id = c.responsable WHERE c.id = source) WHERE caissier_source IS NULL;

DROP FUNCTION com_calcul_commission(bigint);
-- Function: com_calcul_commission(character varying, bigint, bigint)

-- DROP FUNCTION com_calcul_commission(character varying, bigint, bigint);

CREATE OR REPLACE FUNCTION com_calcul_commission(IN ids_ character varying, IN one_ bigint, IN periode_ bigint)
  RETURNS TABLE(vente BIGINT) AS
$BODY$
DECLARE 	
	commercial_ RECORD;
	commission_ RECORD;
	facture_ RECORD;
	facteur_ RECORD;
	ligne_ RECORD;

	vente_ BIGINT DEFAULT 0;
	data_ BIGINT DEFAULT 0;
	
	date_ DATE DEFAULT CURRENT_DATE;

	somme_ DOUBLE PRECISION DEFAULT 0;
	valeur_ DOUBLE PRECISION DEFAULT 0;

	query_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT y.* FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id LEFT JOIN yvs_compta_caisse_piece_vente c ON c.vente = y.id LEFT JOIN yvs_com_commercial_vente o ON o.facture = y.id ';
	where_ CHARACTER VARYING DEFAULT 'WHERE (y.statut NOT IN (''E'',''A'') ';
BEGIN 	
-- 	DROP TABLE IF EXISTS table_calcul_commission;
	CREATE TEMP TABLE IF NOT EXISTS table_calcul_commission(_vente BIGINT); 
	DELETE FROM table_calcul_commission;

	-- Recuperation des informations de la periode
	SELECT INTO ligne_ y.* FROM yvs_com_periode_objectif y WHERE y.id = periode_;
	IF(ligne_.id IS NOT NULL AND ligne_.id > 0)THEN
		-- Recherche des factures enregistrées dans la période
		where_ = where_ || 'AND e.date_entete BETWEEN '''||ligne_.date_debut||''' AND '''||ligne_.date_fin||''') OR (c.statut_piece = ''P'' AND c.date_paiement BETWEEN '''||ligne_.date_debut||''' AND '''||ligne_.date_fin||''')';
		IF(ids_ IS NOT NULL AND ids_ NOT IN ('', ' '))THEN
			where_ = where_ || 'AND y.id = ::character varying in (select val from regexp_split_to_table('''||ids_||''','','') val)';
		END IF;
		IF(one_ IS NOT NULL AND one_ > 0)THEN
			where_ = where_ || 'AND o.commercial = '||one_||'';
		END IF;
		FOR facture_ IN EXECUTE query_
		LOOP
			valeur_ = 0;
			vente_ = 0;
			somme_ = 0;
			IF(facture_.statut = 'V')THEN
				SELECT INTO data_ id FROM yvs_com_commission_vente WHERE facture = facture_.id AND periode = periode_;
				IF(data_ IS NOT NULL AND data_ > 0)THEN
					DELETE FROM yvs_com_commission_vente WHERE id = data_;
				END IF;
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
							IF(facture_.statut_regle = 'P' and date_ BETWEEN ligne_.date_debut AND ligne_.date_fin)THEN
								vente_ = facture_.id;
							END IF;
						ELSIF(facteur_.champ_application = 'E')THEN -- Facteur de taux appliqué sur les factures reglées
							SELECT INTO date_ COALESCE(y.date_paiement, CURRENT_DATE) FROM yvs_compta_caisse_piece_vente y WHERE y.statut_piece = 'P' AND y.vente = facture_.id ORDER BY date_paiement DESC LIMIT 1;
							IF(facture_.statut_regle IN ('R', 'P') and date_ BETWEEN ligne_.date_debut AND ligne_.date_fin)THEN
								vente_ = facture_.id;
							END IF;
						ELSE -- Facteur de taux appliqué sur les factures validées
							IF(facture_.date_valider BETWEEN ligne_.date_debut AND ligne_.date_fin)THEN
								vente_ = facture_.id;
							END IF;
						END IF;
						IF(vente_ > 0)THEN
							IF(facteur_.champ_application = 'E')THEN
								SELECT INTO somme_ SUM(COALESCE(y.montant, 0)) FROM yvs_compta_caisse_piece_vente y WHERE y.statut_piece = 'P' AND y.date_paiement BETWEEN ligne_.date_debut AND ligne_.date_fin AND y.vente = facture_.id ;
								IF(commission_.nature = 'T')THEN
									somme_ = (somme_ * commission_.taux) / 100;
								END IF;
								valeur_ = COALESCE(somme_, 0);
							ELSE
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
							END IF;
							IF(valeur_ != 0)THEN
								SELECT INTO data_ id FROM yvs_com_commission_vente WHERE facture = vente_ AND periode = periode_;
								IF(data_ IS NOT NULL AND data_ > 0)THEN
									UPDATE yvs_com_commission_vente SET montant = montant + valeur_ WHERE id = data_;
								ELSE
									INSERT INTO yvs_com_commission_vente(facture, periode, montant) VALUES(vente_, periode_, valeur_);
								END IF;
								UPDATE yvs_com_doc_ventes SET commision = valeur_ WHERE id = vente_;
							END IF;
						END IF;
					END LOOP;
					IF(valeur_ > 0)THEN
						INSERT INTO table_calcul_commission VALUES(vente_);
					END IF;
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
ALTER FUNCTION com_calcul_commission(character varying, bigint, bigint)
  OWNER TO postgres;


  -- Function: com_get_valeur_objectif(bigint, bigint, bigint)

-- DROP FUNCTION com_get_valeur_objectif(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION com_get_valeur_objectif(commercial_ bigint, periode_ bigint, objectif_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE 
    
BEGIN
	RETURN (SELECT com_get_valeur_objectif(commercial_, periode_, objectif_, ''));
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_valeur_objectif(bigint, bigint, bigint)
  OWNER TO postgres;

  
  -- Function: com_get_valeur_objectif(bigint, bigint, bigint)

-- DROP FUNCTION com_get_valeur_objectif(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION com_get_valeur_objectif(id_ bigint, periode_ bigint, objectif_ bigint, type_ character varying)
  RETURNS double precision AS
$BODY$
DECLARE 
	objectif RECORD;
	periode RECORD;

	colonne CHARACTER VARYING DEFAULT '*';
	condition CHARACTER VARYING DEFAULT '';
	query CHARACTER VARYING DEFAULT '';
	
	valeur DOUBLE PRECISION DEFAULT 0;
	
	count_article BIGINT DEFAULT 0;
	count_client BIGINT DEFAULT 0;
	count_zone BIGINT DEFAULT 0;
    
BEGIN
	-- Recuperation des informations de la periode
	SELECT INTO periode * FROM yvs_com_periode_objectif WHERE id = periode_;
	-- Recuperation des informations de l'objectif
	SELECT INTO objectif * FROM yvs_com_modele_objectif WHERE id = objectif_;

	-- Verification si l'objectif porte sur les articles
	SELECT INTO count_article COALESCE(count(e.id), 0) FROM yvs_com_cible_objectif e WHERE e.objectif = objectif_ AND table_externe = 'BASE_ARTICLE';
	IF(count_article > 0)THEN
		condition = ' AND (c.article in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''BASE_ARTICLE'' AND o.id = '||objectif_||'))';
	END IF;
	-- Verification si l'objectif porte sur les clients
	SELECT INTO count_client COALESCE(count(e.id), 0) FROM yvs_com_cible_objectif e WHERE e.objectif = objectif_ AND table_externe = 'BASE_CLIENT';
	IF(count_client > 0)THEN
		condition = condition ||' AND (d.client in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''BASE_CLIENT'' AND o.id = '||objectif_||'))';
	END IF;
	-- Verification si l'objectif porte sur les zones
	SELECT INTO count_zone COALESCE(count(e.id), 0) FROM yvs_com_cible_objectif e WHERE e.objectif = objectif_ AND table_externe = 'DOC_ZONE';
	IF(count_zone > 0)THEN
		condition = condition ||' AND ((d.adresse in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''DOC_ZONE'' AND o.id = '||objectif_||')) 
					OR (d.adresse in (SELECT f.id FROM yvs_dictionnaire f WHERE f.parent in (SELECT e.id_externe FROM yvs_com_cible_objectif e INNER JOIN yvs_com_modele_objectif o ON o.id = e.objectif WHERE e.table_externe = ''DOC_ZONE'' AND o.id = '||objectif_||'))))';
	END IF;

	query = 'SELECT * FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON d.id = c.doc_vente INNER JOIN yvs_com_entete_doc_vente t ON d.entete_doc = t.id
			INNER JOIN yvs_com_creneau_horaire_users u ON t.creneau = u.id INNER JOIN yvs_com_creneau_point s ON u.creneau_point = s.id
			INNER JOIN yvs_base_point_vente p ON s.point = p.id INNER JOIN yvs_com_commercial_vente y ON y.facture = d.id
		WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND t.date_entete BETWEEN '''||periode.date_debut||''' AND '''||periode.date_fin||''' ';	
	IF(type_ IS NULL OR type_ IN ('', ' '))THEN
		query = query || 'AND y.commercial = '||id_;
	ELSIF(type_ = 'A')THEN
		query = query || 'AND p.agence = '||id_;
	ELSE
		query = query || 'AND p.id = '||id_;
	END IF;
	query = query || condition;
	CASE objectif.indicateur
		WHEN 'CA' THEN 	
			colonne = 'COALESCE(sum(c.prix_total), 0)';
		WHEN 'QUANTITE' THEN 
			colonne = 'COALESCE(sum(c.quantite), 0)';
		WHEN 'MARGE' THEN 
			colonne = 'sum(c.id)';
		ELSE
			colonne = 'sum(c.id)';
	END CASE;
	query = (select replace(query, '*', colonne));
	RAISE NOTICE 'query % ',query;
	execute query INTO valeur;

	return COALESCE(valeur, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_valeur_objectif(bigint, bigint, bigint)
  OWNER TO postgres;

  
  -- Function: com_et_objectif(bigint, bigint, character varying)

-- DROP FUNCTION com_et_objectif(bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION com_et_objectif(IN societe_ bigint, IN objectif_ bigint, IN type_ character varying)
  RETURNS TABLE(element bigint, code character varying, nom character varying, periode bigint, entete character varying, attente double precision, valeur double precision, rang integer) AS
$BODY$
DECLARE 
	periode_ RECORD;
	ligne_ RECORD;
	agence_ RECORD;
	valeur_ DOUBLE PRECISION DEFAULT 0;
	attente_ DOUBLE PRECISION DEFAULT 0;
	_attente_ DOUBLE PRECISION DEFAULT 0;
	somme_ DOUBLE PRECISION DEFAULT 0;
    
	i INTEGER DEFAULT 0;
BEGIN
-- 	DROP TABLE IF EXISTS table_objectif;
	CREATE TEMP TABLE IF NOT EXISTS table_objectif(_element BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _periode BIGINT, _entete CHARACTER VARYING, _attente DOUBLE PRECISION, _valeur DOUBLE PRECISION, _rang INTEGER); 
	DELETE FROM table_objectif;
	IF(type_ IS NULL OR type_ = '')THEN
		FOR ligne_ IN SELECT y.id, y.code_ref AS code, y.nom, y.prenom FROM yvs_com_comerciale y INNER JOIN yvs_agences a ON y.agence = a.id WHERE a.societe = societe_ ORDER BY y.code_ref
		LOOP
			i = 0;			
			FOR periode_ IN SELECT p.id, p.code_ref AS entete FROM yvs_com_periode_objectif p WHERE p.societe = societe_ ORDER BY p.date_debut
			LOOP
				i = i + i;
				SELECT INTO attente_ COALESCE(o.valeur) FROM yvs_com_objectifs_comercial o WHERE o.periode = periode_.id AND o.objectif = objectif_ AND o.commercial = ligne_.id;
				attente_ = COALESCE(attente_ , 0);
				valeur_ = (SELECT com_get_valeur_objectif(ligne_.id, periode_.id, objectif_));
				valeur_ = COALESCE(valeur_ , 0);
				IF(valeur_ != 0 OR attente_ != 0) THEN
					INSERT INTO table_objectif VALUES(ligne_.id, ligne_.code, ligne_.nom ||' '||ligne_.prenom, periode_.id, periode_.entete, attente_, valeur_, i);
				END IF;
			END LOOP;
		END LOOP;
	ELSIF(type_ = 'A')THEN
		FOR agence_ IN SELECT y.id, y.codeagence AS code, y.designation AS nom FROM yvs_agences y WHERE y.actif = TRUE AND y.societe = societe_
		LOOP
			i = 0;			
			FOR periode_ IN SELECT p.id, p.code_ref AS entete FROM yvs_com_periode_objectif p WHERE p.societe = societe_ ORDER BY p.date_debut
			LOOP
				somme_ = 0;
				i = i + i;
				SELECT INTO attente_ COALESCE(o.valeur) FROM yvs_com_objectifs_agence o WHERE o.periode = periode_.id AND o.objectif = objectif_ AND o.agence = agence_.id;
				attente_ = COALESCE(attente_ , 0);
				IF(attente_ IS NULL OR attente_ < 0)THEN
					FOR ligne_ IN SELECT y.id, y.code_ref AS code, y.nom, y.prenom FROM yvs_com_comerciale y WHERE y.agence = agence_.id  ORDER BY y.code_ref
					LOOP	
						SELECT INTO _attente_ COALESCE(o.valeur) FROM yvs_com_objectifs_comercial o WHERE o.periode = periode_.id AND o.objectif = objectif_ AND o.commercial = ligne_.id;
						attente_ = attente_ + COALESCE(_attente_ , 0);
					END LOOP;
				END IF;
				valeur_ = (SELECT com_get_valeur_objectif(agence_.id, periode_.id, objectif_, type_));
				valeur_ = COALESCE(valeur_ , 0);
				IF(valeur_ != 0 OR attente_ != 0) THEN
					INSERT INTO table_objectif VALUES(agence_.id, agence_.code, agence_.nom, periode_.id, periode_.entete, attente_, valeur_, i);
				END IF;
			END LOOP;
		END LOOP;
	ELSE
		FOR agence_ IN SELECT y.id, y.code, y.libelle AS nom FROM yvs_base_point_vente y WHERE y.actif = TRUE
		LOOP
		i = 0;			
			FOR periode_ IN SELECT p.id, p.code_ref AS entete FROM yvs_com_periode_objectif p WHERE p.societe = societe_ ORDER BY p.date_debut
			LOOP
				somme_ = 0;
				i = i + i;
				SELECT INTO attente_ COALESCE(o.valeur) FROM yvs_com_objectifs_point_vente o WHERE o.periode = periode_.id AND o.objectif = objectif_ AND o.point_vente = agence_.id;
				attente_ = COALESCE(attente_ , 0);
				IF(attente_ IS NULL OR attente_ < 0)THEN
					FOR ligne_ IN SELECT y.id, y.code_ref AS code, y.nom, y.prenom FROM yvs_com_comerciale y INNER JOIN yvs_com_creneau_horaire_users ch ON y.utilisateur = ch.users INNER JOIN yvs_com_creneau_point cp ON ch.creneau_point = cp.id WHERE cp.point = agence_.id  ORDER BY y.code_ref
					LOOP	
						SELECT INTO _attente_ COALESCE(o.valeur) FROM yvs_com_objectifs_comercial o WHERE o.periode = periode_.id AND o.objectif = objectif_ AND o.commercial = ligne_.id;
						attente_ = attente_ + COALESCE(_attente_ , 0);
					END LOOP;
				END IF;
				valeur_ = (SELECT com_get_valeur_objectif(agence_.id, periode_.id, objectif_, type_));
				IF(valeur_ != 0 OR attente_ != 0) THEN
					INSERT INTO table_objectif VALUES(agence_.id, agence_.code, agence_.nom, periode_.id, periode_.entete, attente_, valeur_, i);
				END IF;
			END LOOP;
		END LOOP;
	END IF;
	return QUERY SELECT * FROM table_objectif ORDER BY _nom, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_objectif(bigint, bigint, character varying)
  OWNER TO postgres;

