-- Function: get_ca_vente(bigint)
-- DROP FUNCTION get_ca_vente(bigint);
CREATE OR REPLACE FUNCTION get_ca_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	cs_ double precision default 0;
	remise_ double precision default 0;
	avoir_ double precision default 0;
	avance_ double precision default 0;
	data_ record;

BEGIN
	-- Recupere le montant TTC du contenu de la facture
	select into total_ sum(prix_total) from yvs_com_contenu_doc_vente where doc_vente = id_;
	if(total_ is null)then
		total_ = 0;
	end if;
	-- Recupere le total des couts de service supplementaire d'une facture
	cs_ = (select get_cout_sup_vente(id_, true));
	total_ = total_ + cs_;
	
	-- Recupere le total des remises sur la facture
	remise_ = (select get_remise_vente(id_));
	if(remise_ is null)then
		remise_= 0;
	end if;
	total_ = total_ - remise_;
	
	-- Recupere le total des avoirs sur la facture
	for data_ in select id from yvs_com_doc_ventes where document_lie = id_ and type_doc = 'FAV' and statut = 'V'
	loop
		avoir_ = avoir_ + coalesce((select get_ca_vente(data_.id)), 0);
		avance_ = avance_ + coalesce((select sum(montant) from yvs_compta_caisse_piece_vente where vente = data_.id and statut_piece = 'P'), 0);
	end loop;
-- 	RAISE NOTICE 'avoir_ : %',avoir_;
-- 	RAISE NOTICE 'avance_ : %',avance_;
	total_ = total_ - coalesce(avoir_, 0) + coalesce(avance_, 0);
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ca_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ca_vente(bigint) IS 'retourne le chiffre d''affaire d''un doc vente';


-- Function: equilibre_acompte_client(bigint)
-- DROP FUNCTION equilibre_acompte_client(bigint);
CREATE OR REPLACE FUNCTION equilibre_acompte_client(id_ bigint)
  RETURNS character varying AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	cout_ double precision default 0;
	paye_ bigint default 0;
	statut_ character varying ='W';

BEGIN
	-- montant total du document
	SELECT INTO ttc_ y.montant FROM yvs_compta_acompte_client y WHERE y.id = id_;
	-- Montant totale des pièces payé
	SELECT INTO paye_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_compta_notif_reglement_vente n ON n.piece_vente = y.id WHERE y.statut_piece='P' AND n.acompte = id_;
	IF(COALESCE(paye_, 0) > 0)THEN
		statut_ = 'R';
		IF(paye_ >= ttc_) THEN
			statut_ = 'P';
		END IF;
	END IF;
	UPDATE yvs_compta_acompte_client SET statut_notif = statut_ WHERE id=id_;
	return statut_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_acompte_client(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_acompte_client(bigint) IS 'equilibre l''etat reglé et des documents acomptes client';



-- Function: equilibre_acompte_fournisseur(bigint)
-- DROP FUNCTION equilibre_acompte_fournisseur(bigint);
CREATE OR REPLACE FUNCTION equilibre_acompte_fournisseur(id_ bigint)
  RETURNS character varying AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	cout_ double precision default 0;
	paye_ bigint default 0;
	statut_ character varying ='W';

BEGIN
	-- montant total du document
	SELECT INTO ttc_ y.montant FROM yvs_compta_acompte_fournisseur y WHERE y.id = id_;
	-- Montant totale des pièces payé
	SELECT INTO paye_ SUM(y.montant) FROM yvs_compta_caisse_piece_achat y INNER JOIN yvs_compta_notif_reglement_achat n ON n.piece_achat = y.id WHERE y.statut_piece='P' AND n.acompte = id_;
	IF(COALESCE(paye_, 0) > 0)THEN
		statut_ = 'R';
		IF(paye_ >= ttc_) THEN
			statut_ = 'P';
		END IF;
	END IF;
	UPDATE yvs_compta_acompte_fournisseur SET statut_notif = statut_ WHERE id=id_;
	return statut_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_acompte_fournisseur(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_acompte_fournisseur(bigint) IS 'equilibre l''etat reglé et des documents acomptes fournisseur';


-- Function: com_et_ecart_vente(bigint, bigint, character varying, date, date, character varying, boolean)
-- DROP FUNCTION com_et_ecart_vente(bigint, bigint, character varying, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION com_et_ecart_vente(IN societe_ bigint, IN agence_ bigint, IN element_ character varying, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN by_point_ boolean)
  RETURNS TABLE(agence bigint, element bigint, code character varying, nom character varying, entete character varying, versement_attendu double precision, versement_reel double precision, ecart double precision, solde double precision, rang integer) AS
$BODY$
declare 

   vendeur_ RECORD;
   dates_ RECORD;
   
   jour_ character varying;
   ids_ character varying default '0';
   
   versement_attendu_ double precision default 0;
   versement_reel_ double precision default 0;
   ecart_ double precision default 0;
   solde_ double precision default 0;
   
   query_ CHARACTER VARYING DEFAULT '';
   query_montant_ CHARACTER VARYING DEFAULT '';

   i integer default 0;
   
begin 	
	--DROP TABLE IF EXISTS table_et_ecart_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ecart_vente(_agence bigint, _element bigint, _code character varying, _nom character varying, _entete character varying, _versement_attendu double precision, _versement_reel double precision, _ecart double precision, _solde double precision, _rang integer);
	DELETE FROM table_et_ecart_vente;
	IF(by_point_)THEN
		query_ = 'SELECT DISTINCT y.id::bigint as id, y.code as code, y.libelle as nom';
	ELSE
		query_ = 'SELECT DISTINCT y.id::bigint as id, y.code_users as code, y.nom_users as nom';
	END IF;
	query_ = query_ || ', y.agence::bigint as agence FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id ';
	IF(by_point_)THEN
		query_ = query_ || ' INNER JOIN yvs_base_point_vente y ON c.point = y.id';
	ELSE
		query_ = query_ || ' INNER JOIN yvs_users y ON h.users = y.id';
	END IF;
	query_ = query_ || ' INNER JOIN yvs_agences a ON y.agence = a.id WHERE e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND y.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	IF(COALESCE(element_, '') NOT IN ('', ' ', '0'))THEN
		FOR jour_ IN SELECT head FROM regexp_split_to_table(element_,',') head
		LOOP
			ids_ = ids_ ||','||jour_;
		END LOOP;
		query_ = query_ || ' AND y.id IN ('||ids_||')';
	END IF;
	RAISE NOTICE 'query_ %',query_;
	FOR vendeur_ IN EXECUTE query_
	LOOP
		i = 0;
		solde_ = 0;
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_, true)
		LOOP
			i = i + 1;
			jour_ = dates_.intitule;
			IF(by_point_)THEN
				query_montant_ = 'SELECT SUM(com_get_versement_attendu(y.id::character varying)) FROM yvs_com_entete_doc_vente y INNER JOIN yvs_com_creneau_horaire_users h ON y.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE c.point = '||vendeur_.id;
			ELSE
				query_montant_ = 'SELECT SUM(com_get_versement_attendu(y.id::character varying)) FROM yvs_com_entete_doc_vente y INNER JOIN yvs_com_creneau_horaire_users h ON y.creneau = h.id WHERE h.users = '||vendeur_.id;
			END IF;
			query_montant_ = query_montant_ || ' AND y.date_entete BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			EXECUTE query_montant_ INTO versement_attendu_;
			versement_attendu_ = COALESCE(versement_attendu_, 0);
			
			IF(by_point_)THEN
				query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source IN (SELECT DISTINCT h.users FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE c.point = '||vendeur_.id||')';
			ELSE
				query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source = '||vendeur_.id;
			END IF;
			query_montant_ = query_montant_ || ' AND y.statut_piece = ''P'' AND y.date_paiement BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			EXECUTE query_montant_ INTO versement_reel_;
			versement_reel_ = COALESCE(versement_reel_, 0);
			
			ecart_ = versement_attendu_ - versement_reel_;
			solde_ = solde_ + ecart_;
			IF(ecart_ != 0)THEN
				INSERT INTO table_et_ecart_vente VALUES(vendeur_.agence, vendeur_.id::bigint, vendeur_.code, vendeur_.nom, jour_, versement_attendu_, versement_reel_, ecart_, solde_, i);
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_ecart_vente ORDER BY _agence, _code, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ecart_vente(bigint, bigint, character varying, date, date, character varying, boolean)
  OWNER TO postgres;

  
-- Function: update_contenu_doc_achat()
-- DROP FUNCTION update_contenu_doc_achat();
CREATE OR REPLACE FUNCTION update_contenu_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, date_doc from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FRA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'E';
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = NEW.article and mouvement = 'E';
					if(doc_.type_doc = 'BLA')then
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					end if;
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_recu, cout_entree = NEW.pua_recu, conditionnement = NEW.conditionnement, lot = NEW.lot, date_doc = doc_.date_doc where id = mouv_;
				end if;
			else
				if(doc_.type_doc = 'BLA')then			
					result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				else
					result = (select valorisation_stock(NEW.article, NEW.conditionnement, doc_.depot, doc_.tranche, NEW.quantite_recu,  NEW.pua_recu, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;
			end if;	
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_achat()
  OWNER TO postgres;

  
-- Function: update_contenu_doc_vente()
-- DROP FUNCTION update_contenu_doc_vente();
CREATE OR REPLACE FUNCTION update_contenu_doc_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	dep_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer, date_livraison from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'BRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			if(doc_.depot_livrer is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article and mouvement = 'S';
						if(doc_.type_doc = 'BLV')then
							result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
						else
							result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = (NEW.quantite), cout_entree = NEW.prix, conditionnement = NEW.conditionnement, lot = NEW.lot, date_doc = doc_.date_livraison where id = mouv_;
					end if;
				else
					if(doc_.type_doc = 'BLV')then
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_livraison));
					else
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.depot_livrer, doc_.tranche_livrer, (NEW.quantite), 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_livraison));
					end if;
				end if;	
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_vente()
  OWNER TO postgres;

  
-- Function: equilibre_achat(bigint, boolean)
-- DROP FUNCTION equilibre_achat(bigint, boolean);
CREATE OR REPLACE FUNCTION equilibre_achat(id_ bigint, by_parent_ boolean)
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

	query_control character varying;
	query_content character varying;

BEGIN
	-- Equilibre de l'etat reglé
	SELECT INTO ch_ a.societe FROM yvs_com_doc_achats d INNER JOIN yvs_agences a ON d.agence = a.id WHERE d.id = id_;
	ttc_ = (select get_ttc_achat(id_));
	ttc_ = arrondi(ch_, ttc_);
	select into av_ sum(montant) from yvs_compta_caisse_piece_achat where achat = id_ and statut_piece = 'P';
	if(av_ is null)then
		av_ = 0;
	end if;

	-- Equilibre de l'etat livré
	query_control = 'select sum(c.quantite_recu) from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on c.doc_achat = d.id 
		where d.type_doc = ''BLA'' and d.statut = ''V'' and d.document_lie = '||id_;
	IF(by_parent_)THEN
		query_content = 'select id, conditionnement as unite, quantite_recu::decimal as qte from yvs_com_contenu_doc_achat where doc_achat = '||id_;
	ELSE
		query_content = 'select article as id, conditionnement as unite, sum(quantite_recu)::decimal as qte from yvs_com_contenu_doc_achat where doc_achat = '||id_||' group by article, conditionnement';	
	END IF;
	for contenu_ in execute query_content
	loop
		in_ = true;
		IF(by_parent_)THEN
			execute query_control || ' and c.parent = '||COALESCE(contenu_.id, 0) into qte_;
		ELSE
			execute query_control || ' and c.conditionnement = '||COALESCE(contenu_.unite, 0) into qte_;
		END IF;
		qte_ = coalesce(qte_, 0);
		if(qte_ < contenu_.qte)then
			correct = false;
			if(qte_ > 0)then
				encours = true;
				exit;
			end if;
		end if;
	end loop;
	-- Bonus
	IF(by_parent_)THEN
		query_content = 'select id, conditionnement_bonus as unite, coalesce(quantite_bonus, 0)::decimal as qte from yvs_com_contenu_doc_achat where doc_achat = '||id_||' and coalesce(quantite_bonus, 0) > 0';
	ELSE
		query_content = 'select article_bonus as id, conditionnement_bonus as unite, sum(quantite_bonus)::decimal as qte from yvs_com_contenu_doc_achat where doc_achat = '||id_||' and coalesce(quantite_bonus, 0) > 0 group by article_bonus, conditionnement_bonus';
	END IF;
	for contenu_ in execute query_content
	loop
		in_ = true;
		IF(by_parent_)THEN
			execute query_control || ' and c.parent = '||COALESCE(contenu_.id, 0) into qte_;
		ELSE
			execute query_control || ' and c.conditionnement = '||COALESCE(contenu_.unite, 0) into qte_;
		END IF;
		qte_ = coalesce(qte_, 0);
		if(qte_ < contenu_.qte)then
			correct = false;
			if(qte_ > 0)then
				encours = true;
				exit;
			end if;
		end if;
	end loop;
	RAISE NOTICE 'in_ %',in_;
	if(in_)then
		select into ch_ count(y.id) from yvs_compta_caisse_piece_achat y inner join yvs_base_mode_reglement m on y.model = m.id where y.achat = id_ and m.type_reglement = 'BANQUE';
		if(av_ > 0 and av_>=ttc_)then
			update yvs_com_doc_achats set statut_regle = 'P' where id = id_;
		elsif (av_ > 0 or ch_ > 0) then
			update yvs_com_doc_achats set statut_regle = 'R' where id = id_;
		else
			update yvs_com_doc_achats set statut_regle = 'W' where id = id_;
		end if;
		
		RAISE NOTICE 'correct % ',correct;
		if(correct)then
			update yvs_com_doc_achats set statut_livre = 'L' where id = id_;
		else
			RAISE NOTICE 'encours % ',encours;
			if(encours)then
				update yvs_com_doc_achats set statut_livre = 'R' where id = id_;
			elsif(by_parent_)then
				update yvs_com_doc_achats set statut_livre = 'W' where id = id_;
			end if;
			IF(by_parent_)then
				PERFORM equilibre_achat(id_, false);
			end if;
		end if;	
	else
		update yvs_com_doc_achats set statut_regle = 'W', statut_livre = 'W' where id = id_;
	end if;	
	update yvs_workflow_valid_facture_achat set date_update = date_update where facture_achat = id_;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_achat(bigint, boolean)
  OWNER TO postgres;

  
-- Function: equilibre_vente(bigint, boolean)
-- DROP FUNCTION equilibre_vente(bigint, boolean);
CREATE OR REPLACE FUNCTION equilibre_vente(id_ bigint, by_parent_ boolean)
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

	query_control character varying;
	query_content character varying;

BEGIN
	-- Equilibre de l'etat reglé
	SELECT INTO ch_ a.societe FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users u ON e.creneau = u.id INNER JOIN yvs_users s on s.id = u.users INNER JOIN yvs_agences a ON s.agence = a.id WHERE d.id = id_;
	ttc_ = (select get_ttc_vente(id_));
	ttc_ = arrondi(ch_, ttc_);
	select into av_ sum(coalesce(montant,0)) from yvs_compta_caisse_piece_vente where vente = id_ and statut_piece = 'P';
	if(av_ is null)then
		av_ = 0;
	end if;
	av_ = arrondi(ch_, av_);

	-- Equilibre de l'etat livré
	query_control = 'select sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id 
		where d.type_doc = ''BLV'' and d.statut = ''V'' and d.document_lie = '||id_;
	IF(by_parent_)THEN
		query_content = 'select id, conditionnement as unite, quantite::decimal as qte from yvs_com_contenu_doc_vente where doc_vente = '||id_;
	ELSE
		query_content = 'select article as id, conditionnement as unite, sum(quantite)::decimal as qte from yvs_com_contenu_doc_vente where doc_vente = '||id_||' group by article, conditionnement';
	END IF;
	for contenu_ in execute query_content
	loop
		in_ = true;
		IF(by_parent_)THEN
			execute query_control || ' and c.parent = '||COALESCE(contenu_.id, 0) into qte_;
		ELSE
			execute query_control || ' and c.conditionnement = '||COALESCE(contenu_.unite, 0) into qte_;
		END IF;
		qte_ = coalesce(qte_, 0);
		if(qte_ < contenu_.qte)then
			correct = false;
			if(qte_ > 0)then
				encours = true;
				exit;
			end if;
		end if;
	end loop;
	-- Bonus
	IF(by_parent_)THEN
		query_content = 'select id, conditionnement_bonus as unite, coalesce(quantite_bonus, 0)::decimal as qte from yvs_com_contenu_doc_vente where doc_vente = '||id_||' and coalesce(quantite_bonus, 0) > 0';
	ELSE
		query_content = 'select article_bonus as id, conditionnement_bonus as unite, sum(quantite_bonus)::decimal as qte from yvs_com_contenu_doc_vente where doc_vente = '||id_||' and coalesce(quantite_bonus, 0) > 0 group by article_bonus, conditionnement_bonus';
	END IF;
	for contenu_ in execute query_content
	loop
		in_ = true;
		IF(by_parent_)THEN
			execute query_control || ' and c.parent = '||COALESCE(contenu_.id, 0) into qte_;
		ELSE
			execute query_control || ' and c.conditionnement = '||COALESCE(contenu_.unite, 0) into qte_;
		END IF;
		qte_ = coalesce(qte_, 0);
		if(qte_ < contenu_.qte)then
			correct = false;
			if(qte_ > 0)then
				encours = true;
				exit;
			end if;
		end if;
	end loop;
	RAISE NOTICE 'in_ : %',in_;
	if(in_)then
		select into ch_ count(y.id) from yvs_compta_caisse_piece_vente y inner join yvs_base_mode_reglement m on y.model = m.id where y.vente = id_ and m.type_reglement = 'BANQUE';
		if(av_>=ttc_)then
			update yvs_com_doc_ventes set statut_regle = 'P' where id = id_;
		elsif (av_ > 0 or ch_ > 0) then
			update yvs_com_doc_ventes set statut_regle = 'R' where id = id_;
		else
			update yvs_com_doc_ventes set statut_regle = 'W' where id = id_;
		end if;
		
		RAISE NOTICE 'correct : %',correct;
		if(correct)then
			update yvs_com_doc_ventes set statut_livre = 'L' where id = id_;
		else
			RAISE NOTICE 'encours : %',encours;
			if(encours)then
				update yvs_com_doc_ventes set statut_livre = 'R' where id = id_;
			elsif(by_parent_)then
				update yvs_com_doc_ventes set statut_livre = 'W' where id = id_;
			end if;
			IF(by_parent_)then
				PERFORM equilibre_vente(id_, false);
			end if;
		end if;	
	else
		update yvs_com_doc_ventes set statut_regle = 'W', statut_livre = 'W' where id = id_;
	end if;	
	update yvs_workflow_valid_facture_vente set date_update = date_update where facture_vente = id_;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente(bigint, boolean)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente(bigint, boolean) IS 'equilibre l''etat reglé et l''etat livré des documents de vente';
