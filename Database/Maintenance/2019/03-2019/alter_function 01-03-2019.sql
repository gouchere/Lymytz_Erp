-- Function: com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
-- DROP FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying);
CREATE OR REPLACE FUNCTION com_inventaire_preparatoire(IN agence_ bigint, IN depot_ bigint, IN date_ date, IN print_all_ boolean, IN option_print_ character varying, IN type_ character varying)
  RETURNS TABLE(depot bigint, article bigint, code character varying, designation character varying, numero character varying, famille character varying, unite bigint, reference character varying, prix double precision, puv double precision, pua double precision, pr double precision, stock double precision) AS
$BODY$
DECLARE 
	articles_ RECORD;
	unites_ RECORD;

	insert_ BOOLEAN DEFAULT false;

	unite_ BIGINT DEFAULT 0;
	
	prix_ DOUBLE PRECISION DEFAULT 0;
	stock_ DOUBLE PRECISION DEFAULT 0;

	query_ CHARACTER VARYING DEFAULT 'SELECT y.id, y.article, y.depot, a.ref_art, a.designation, f.reference_famille, f.designation as famille, a.pua, a.puv, d.agence, y.actif FROM yvs_base_article_depot y 
	INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON f.id = a.famille INNER JOIN yvs_base_depots d ON y.depot = d.id WHERE y.article IS NOT NULL';
	
BEGIN 	
	DROP TABLE IF EXISTS table_inventaire_preparatoire;
	CREATE TEMP TABLE IF NOT EXISTS table_inventaire_preparatoire(_depot bigint, _article bigint, _code character varying, _designation character varying, numero_ character varying, _famille character varying, _unite bigint, _reference character varying, prix_ double precision, _puv double precision, _pua double precision, _pr double precision, _stock double precision); 
	DELETE FROM table_inventaire_preparatoire;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND d.agence = '||agence_;
	END IF;
	IF(depot_ IS NOT NULL AND depot_ > 0)THEN
		query_ = query_ || ' AND d.id = '||depot_;
	END IF;
	option_print_ = COALESCE(option_print_, '');
-- 	RAISE NOTICE '%',query_;
	FOR articles_ IN EXECUTE query_
	LOOP
		FOR unites_ IN SELECT y.id, y.unite, u.reference, COALESCE(y.prix, articles_.puv) AS puv, COALESCE(y.prix_achat, articles_.pua) AS pua FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE y.article = articles_.article
		LOOP
			insert_ = false;
			stock_ = (SELECT get_stock(articles_.article, 0, articles_.depot, articles_.agence, 0, date_, unites_.id, 0));
			IF(print_all_)THEN
				IF(stock_ != 0)THEN
					insert_ = true;
				ELSE
					insert_ = articles_.actif;					
				END IF;
			ELSE
				IF(option_print_ = 'P')THEN
					IF(stock_ > 0)THEN
						insert_ = true;
					END IF;
				ELSIF(option_print_ = 'N')THEN
					IF(stock_ < 0)THEN
						insert_ = true;
					END IF;
				ELSE
					IF(stock_ != 0)THEN
						insert_ = true;
					END IF;
				END IF;
			END IF;
			IF(insert_)THEN
				IF(type_ = 'A')THEN
					prix_ = COALESCE((SELECT get_pua(articles_.article, 0, depot_, unites_.id)), 0);
				ELSIF(type_ = 'V')THEN
					prix_ = unites_.puv;
				ELSE
					prix_ = COALESCE((SELECT get_pr(articles_.article, depot_, 0, date_, unites_.id)), 0);
				END IF;
				INSERT INTO table_inventaire_preparatoire VALUES(articles_.depot, articles_.article, articles_.ref_art, articles_.designation, articles_.reference_famille, articles_.famille, unites_.unite, unites_.reference, prix_, unites_.puv, unites_.pua, 0, stock_);
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_inventaire_preparatoire ORDER BY _depot, _famille, _code;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_inventaire_preparatoire(bigint, bigint, date, boolean, character varying, character varying)
  OWNER TO postgres;

  
  -- Function: get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer)
-- DROP FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer);
CREATE OR REPLACE FUNCTION get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ integer)
  RETURNS double precision AS
$BODY$
DECLARE
	data_ record;
	tarif_ record;
	
	valeur_ double precision default 0;
	remise_ double precision;
	garde_ double precision;

	famille_ bigint;

	control_ boolean default false;
	planifier_ boolean default false;
BEGIN
	valeur_ = qte_ * prix_;	
	if(client_ is not null and client_ > 0)then
		select into famille_ y.famille from yvs_base_articles y where y.id = article_;
		for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
		loop
			control_ = false;
			planifier_ = false;
-- 			RAISE NOTICE 'categorie : %',data_.categorie;
			if(data_.permanent is false)then
				if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
					control_ = true;
					planifier_ = true;
				end if;
			else
				control_ = true;
			end if;
-- 			RAISE NOTICE 'control_ : %',control_;
			if(control_)then
				if(unite_ is not null and unite_ > 0)then
					select into tarif_ y.* from yvs_base_plan_tarifaire y where y.categorie = data_.categorie and y.actif is true and ((y.article is not null and (y.article = article_ and y.conditionnement = (select c.id from yvs_base_conditionnement c where c.article = y.article and c.unite = unite_))) or (y.article is null and y.famille = famille_)) order by y.article limit 1;
					if(tarif_.id is not null)then
						if(tarif_.nature_remise = 'TAUX')then
							garde_ = valeur_ * (tarif_.remise /100);
						else
							garde_ = tarif_.remise;
						end if;
-- 						RAISE NOTICE 'garde_ : %',garde_;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL) then
-- 							RAISE NOTICE 'tarif_.id : %',tarif_.id;
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
							else
								remise_ = tarif_.remise;
							end if;
						end if;
						if(remise_ is null or remise_ <= 0)then
							remise_ = garde_;
						end if;
						exit;
					end if;
				else
					select into tarif_ y.* from yvs_base_plan_tarifaire y where y.categorie = data_.categorie and y.actif is true and ((y.article is not null and y.article = article_) or (y.article is null and y.famille = famille_)) order by y.article limit 1;
					if(tarif_.id is not null)then
						if(tarif_.nature_remise = 'TAUX')then
							garde_ = valeur_ * (tarif_.remise /100);
						else
							garde_ = tarif_.remise;
						end if;
						select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
						if(tarif_.id IS NOT NULL) then
							if(tarif_.nature_remise = 'TAUX')then
								remise_ = valeur_ * (tarif_.remise /100);
							else
								remise_ = tarif_.remise;
							end if;
						end if;
						if(remise_ is null or remise_ <= 0)then
							remise_ = garde_;
						end if;
						exit;
					end if;
				end if;
				if(planifier_)then
					exit;
				end if;
			end if;
		end loop;
	end if;
	if(remise_ is null or remise_ <= 0)then
		if(unite_ is not null and unite_ > 0)then
			select into tarif_ y.* from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.unite = unite_ and actif is true limit 1;
			if(tarif_.id IS NOT NULL) then
					if(tarif_.nature_remise = 'TAUX')then
						remise_ = valeur_ * (tarif_.remise /100);
					else
						remise_ = tarif_.remise;
					end if;
			end if;
		else
			select into tarif_ * from yvs_base_article_point where point = point_ and article = article_ and actif is true limit 1;
			if(tarif_.id IS NOT NULL) then
				if(tarif_.nature_remise = 'TAUX')then
					remise_ = valeur_ * (tarif_.remise /100);
				else
					remise_ = tarif_.remise;
				end if;
			end if;
		end if;
		if(remise_ is null or remise_ <= 0)then
			if(unite_ is not null and unite_ > 0)then
				select into remise_ remise from yvs_base_conditionnement where article = article_ and unite = unite_;
				if(remise_ is not null) then
					remise_ = valeur_ * (remise_/100);
				end if;
			else
				select into remise_ remise from yvs_base_articles where id = article_; 
				if(remise_ is not null) then
					remise_ = valeur_ * (remise_/100);
				end if;
			end if;
		end if;	
	end if;	
	if(remise_ is null or remise_ <=0)then
		remise_ = 0;
	end if;	
	return remise_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer)
  OWNER TO postgres;
COMMENT ON FUNCTION get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer) IS 'retourne la remise sur vente d'' article';


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
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		--trouve le nom du caissier
		SELECT INTO user_ u.* FROM yvs_users u WHERE u.id=NEW.caissier_source;
		SELECT INTO line_  n.societe FROM yvs_base_caisse c INNER JOIN  yvs_base_plan_comptable cp ON c.compte=cp.id INNER JOIN yvs_base_nature_compte n ON n.id=cp.nature_compte WHERE c.id=NEW.source;
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
		-- Insère la sortie
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
			societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
		VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', (NEW.montant + frais_), NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
			line_.societe,NEW.author,NEW.source, NEW.caissier_source, NEW.caissier_source, null,'D', user_.nom_users, NEW.model);
		-- Insère l'entré
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
			societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement,name_tiers, model)
		VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', NEW.montant, NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
			line_.societe,NEW.author,NEW.cible, NEW.caissier_cible, NEW.caissier_source, null,'R', user_.nom_users, NEW.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=(NEW.montant + frais_), tiers_interne=NEW.caissier_source, model = NEW.model,
				reference_externe=NEW.numero_piece, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
				statut_piece=NEW.statut_piece, caisse=NEW.source, caissier=NEW.caissier_source, mouvement='D', societe=line_.societe, name_tiers=user_.nom_users
			WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id AND mouvement='D';
					  ---
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_interne=NEW.caissier_source, model = NEW.model,
				reference_externe=NEW.numero_piece, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
				statut_piece=NEW.statut_piece, caisse=NEW.cible, caissier=NEW.caissier_cible, mouvement='R', societe=line_.societe, name_tiers=user_.nom_users
			WHERE table_externe='DOC_VIREMENT' AND id_externe=NEW.id AND mouvement='R';
		ELSE
			-- Insère la sortie
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement,name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', (NEW.montant + frais_), NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				line_.societe,NEW.author,NEW.source, NEW.caissier_source, NEW.caissier_source, null,'D',user_.nom_users, NEW.model);
			-- Insère l'entré
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VIREMENT', NEW.montant, NEW.numero_piece, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				line_.societe,NEW.author,NEW.cible, NEW.caissier_cible, NEW.caissier_source, null,'R',user_.nom_users, NEW.model);
		END IF;	           
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VIREMENT' AND id_externe=OLD.id;
	END IF;
	RETURN NULL;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_virement()
  OWNER TO postgres;

  
-- Function: action_in_header_vente_or_piece_virement(bigint)
-- DROP FUNCTION action_in_header_vente_or_piece_virement(bigint);
CREATE OR REPLACE FUNCTION action_in_header_vente_or_piece_virement(header_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE 
	entete_ RECORD;
	
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	ecart_ DOUBLE PRECISION DEFAULT 0;

	id_ BIGINT DEFAULT 0;

	numero_ CHARACTER VARYING DEFAULT '';
	reference_ CHARACTER VARYING DEFAULT '';
	numeric_ INTEGER DEFAULT 0;
   
BEGIN 	
	IF(COALESCE(header_, 0) > 0)THEN
		SELECT INTO entete_ y.date_entete, h.users, y.author, a.societe FROM yvs_com_entete_doc_vente y INNER JOIN yvs_com_creneau_horaire_users h ON y.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id WHERE y.id = header_;	
		ca_ = (SELECT get_ca_entete_vente(header_));
		SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_virement y INNER JOIN yvs_compta_notif_versement_vente n ON n.piece = y.id WHERE y.statut_piece = 'P' AND n.entete_doc = header_;
		avance_ = COALESCE(avance_, 0);
		ecart_ =  avance_ - ca_;
		SELECT INTO id_ y.id FROM yvs_com_ecart_entete_vente y WHERE y.entete_doc = header_;
		IF(COALESCE(id_, 0) > 0)THEN
			IF(ecart_ != 0)THEN
				UPDATE yvs_com_ecart_entete_vente SET montant = ecart_ WHERE id = id_;
			ELSE
				DELETE FROM yvs_com_ecart_entete_vente WHERE id = id_;
			END IF;
		ELSIF(ecart_ != 0)THEN
			numero_ = 'ECR/';
			numero_ = numero_ || to_char(entete_.date_entete ,'ddMMyy') || '/';
			SELECT INTO reference_ y.numero FROM yvs_com_ecart_entete_vente y INNER JOIN yvs_users u ON y.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id WHERE numero LIKE numero_ ||'%' AND a.societe = entete_.societe;
			IF(COALESCE(reference_, '') != '')THEN
				numeric_ = TRIM(REPLACE(reference_, numero_, ' '))::INTEGER;
				numeric_ = numeric_ + 1;
				numero_ = numero_ || REPLACE(to_char(numeric_,'999'), ' ', '0');
			ELSE
				numero_ = numero_ || REPLACE(to_char(1,'999'), ' ', '0');
			END IF;
			INSERT INTO yvs_com_ecart_entete_vente(date_ecart, numero, montant, entete_doc, users, statut, statut_regle, author) 
				VALUES(entete_.date_entete, numero_, ecart_, header_, entete_.users, 'E', 'W', entete_.author);
		END IF;
		RETURN TRUE;
	END IF;
	RETURN FALSE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_in_header_vente_or_piece_virement(bigint)
  OWNER TO postgres;

  
-- Function: get_ristourne(bigint, double precision, double precision, bigint, date)
-- DROP FUNCTION get_ristourne(bigint, double precision, double precision, bigint, date);
CREATE OR REPLACE FUNCTION get_ristourne(cond_ bigint, qte_ double precision, prix_ double precision, client_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	ristourne_ double precision;
	data_ record;
	plan_ bigint;
	tarif_ record;
	valeur_ double precision default 0;
	
	famille_ bigint;

	control_ boolean default false;
	planifier_ boolean default false;
BEGIN
	valeur_ = qte_ * prix_;
	if(client_ is not null and client_ > 0)then
		select into data_ * from yvs_com_client where id = client_;
		if(data_.plan_ristourne is not null)then
			select into plan_ id from yvs_com_plan_ristourne WHERE actif = true and id = data_.plan_ristourne;
			if(plan_ is not null)then				
				select into famille_ y.famille from yvs_base_articles y inner join yvs_base_conditionnement c on c.article = y.id where c.id = cond_;
				for data_ in select * from yvs_com_ristourne WHERE actif = true and plan = plan_ AND ((conditionnement is not null and conditionnement = cond_) or (conditionnement is null and famille = famille_)) AND nature = 'R'
				loop
					control_ = false;
					planifier_ = false;
					if(data_.permanent is false)then
						if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
							control_ = true;
							planifier_ = true;
						end if;
					else
						control_ = true;
					end if;	
					if(control_)then
						select into tarif_ * from yvs_com_grille_ristourne where ristourne = data_.id and ((base = 'QTE' and qte_ between montant_minimal and montant_maximal)or(base = 'CATTC' and valeur_ between montant_minimal and montant_maximal));
						if(tarif_.id is not null)then
							if(tarif_.nature_montant = 'TAUX')then
								ristourne_ = valeur_ * (tarif_.montant_ristourne /100);
							else
								ristourne_ = tarif_.montant_ristourne;
							end if;
						end if;
						exit;
					end if;
				end loop;
			end if;	
		end if;		
	end if;
	
	if(ristourne_ is null or ristourne_ <1)then
		ristourne_ = 0;
	end if;
	return ristourne_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ristourne(bigint, double precision, double precision, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ristourne(bigint, double precision, double precision, bigint, date) IS 'retourne la ristourne d'' article';


-- Function: com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean)
-- DROP FUNCTION com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION com_et_ristourne_vente(IN societe_ bigint, IN users_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN cumul_ boolean)
  RETURNS TABLE(client bigint, code character varying, nom character varying, users bigint, code_users character varying, nom_users character varying, unite bigint, entete character varying, quantite double precision, prix_total double precision, ristourne double precision, rang integer) AS
$BODY$
declare 

   clients_ RECORD;
   data_ RECORD;
   dates_ RECORD;
   
   jour_ character varying;
   
   ristourne_ CHARACTER VARYING DEFAULT 'SELECT u.id, u.code_users, u.nom_users, c.ristourne, c.quantite, c.prix_total, c.conditionnement';
   colonne_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT y.id, y.code_client, CONCAT(y.nom, '' '', y.prenom) AS nom';
   save_ CHARACTER VARYING DEFAULT ' FROM yvs_com_client y INNER JOIN yvs_com_doc_ventes d ON d.client = y.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
						INNER JOIN yvs_com_contenu_doc_vente c ON c.doc_vente = d.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id
						WHERE e.date_entete BETWEEN'||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND COALESCE(c.ristourne, 0) > 0 AND a.societe = '||societe_;
   where_ CHARACTER VARYING DEFAULT '';
   query_ CHARACTER VARYING DEFAULT '';

   i integer default 0;
   
begin 	
	DROP TABLE IF EXISTS table_et_ristourne_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ristourne_vente(_client bigint, _code character varying, _nom character varying, _users bigint, _code_users character varying, _nom_users character varying, _unite bigint, _entete character varying, _quantite double precision, _prix_total double precision, _ristourne double precision, _rang integer);
	DELETE FROM table_et_ristourne_vente;
	IF(users_ IS NOT NULL AND users_ > 0)THEN
		save_ = save_ || ' AND u.id = '||users_;
	END IF;
	IF(client_ IS NOT NULL AND client_ > 0)THEN
		where_ = ' AND y.id = '||client_;
	END IF;
-- 	RAISE NOTICE 'query_ %',query_;
	IF(cumul_)THEN
		ristourne_ = 'SELECT u.id, u.code_users, u.nom_users, SUM(COALESCE(c.ristourne, 0)) as ristourne, SUM(COALESCE(c.quantite, 0)) as quantite, SUM(COALESCE(c.prix_total, 0)) as prix_total, c.conditionnement';
	END IF;
	FOR clients_ IN EXECUTE colonne_ || save_ || where_
	LOOP
		i = 0;
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_, true)
		LOOP
			i = i + 1;
			jour_ = dates_.intitule;
			query_ = ristourne_ || save_ || ' AND e.date_entete BETWEEN'||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND y.id = '||clients_.id;
			IF(cumul_)THEN
				query_ = query_ || ' GROUP BY u.id, c.conditionnement';
			END IF;
			FOR data_ IN EXECUTE query_
			LOOP
				IF(COALESCE(data_.ristourne, 0) != 0)THEN
					INSERT INTO table_et_ristourne_vente VALUES(clients_.id, clients_.code_client, clients_.nom, data_.id, data_.code_users, data_.nom_users, data_.conditionnement, jour_, data_.quantite, data_.prix_total, data_.ristourne, i);
				END IF;
			END LOOP;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_ristourne_vente ORDER BY _client, _code, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean)
  OWNER TO postgres;
  
  
-- Function: get_ca_entete_vente(bigint)
-- DROP FUNCTION get_ca_entete_vente(bigint);
CREATE OR REPLACE FUNCTION get_ca_entete_vente(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;

	cs_m double precision default 0;
	cs_p double precision default 0;
	cs_ double precision default 0;
	
	remise_ double precision default 0;
	data_ record;
	
	header_ record;
	qte_ double precision;

BEGIN
	-- Recupere le montant TTC du contenu de la facture
	select into total_ sum(c.prix_total) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id where d.entete_doc = id_ and d.type_doc = 'FV';
	if(total_ is null)then
		total_ = 0;
	end if;
	-- Recupere le total des couts de service supplementaire d'une facture
	select into cs_p sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id where d.entete_doc = id_ and d.type_doc = 'FV' and t.augmentation is true and o.service = true;
	if(cs_p is null)then
		cs_p = 0;
	end if;
	select into cs_m sum(o.montant) from yvs_com_cout_sup_doc_vente o inner join yvs_grh_type_cout t on o.type_cout = t.id inner join yvs_com_doc_ventes d on o.doc_vente = d.id where d.entete_doc = id_ and d.type_doc = 'FV' and t.augmentation is false and o.service = true;
	if(cs_m is null)then
		cs_m = 0;
	end if;
	cs_  = cs_p - cs_m;
	total_ = total_ + cs_;
	
	-- Recupere le total des remises sur la facture
	select into header_ ed.* from yvs_com_entete_doc_vente ed where ed.id = id_;
	if(header_ is not null and header_.id is not null)then	
		-- Recupere le total des quantitées d'une facture
		select into qte_ sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id where d.type_doc = 'FV' and d.entete_doc = id_;
		if(qte_ is null)then
			qte_ = 0;
		end if;

		for data_ in select gr.* from yvs_com_grille_remise gr inner join yvs_com_remise re on gr.remise = re.id inner join yvs_com_remise_doc_vente rd on rd.remise = re.id inner join yvs_com_doc_ventes dv on rd.doc_vente = dv.id
				where dv.entete_doc = id_ and dv.type_doc = 'FV' and (re.permanent is true or (header_.date_entete between re.date_debut and re.date_fin)) 
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
	if(remise_ is null)then
		remise_= 0;
	end if;
	total_ = total_ - remise_;
	
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_ca_entete_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_ca_entete_vente(bigint) IS 'retourne le chiffre d''affaire d''un entete vente';


-- Function: com_get_versement_attendu(character varying)
-- DROP FUNCTION com_get_versement_attendu(character varying);
CREATE OR REPLACE FUNCTION com_get_versement_attendu(IN headers_ character varying)
  RETURNS DOUBLE PRECISION AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;

	head_ RECORD;
BEGIN    
	SELECT INTO ca_ SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d WHERE d.type_doc = 'FV' AND d.document_lie IS NULL AND d.entete_doc::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head);
	FOR head_ IN SELECT h.users, e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id WHERE e.id::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head)
	LOOP
		SELECT INTO result_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) AND p.caissier = head_.users AND p.date_paiement = head_.date_entete;
		RAISE NOTICE 'result_ : %',result_;
		avance_ = avance_ + COALESCE(result_, 0);
	END LOOP;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avances : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu(character varying)
  OWNER TO postgres;
  
  
-- Function: com_get_versement_attendu(character varying)
-- DROP FUNCTION com_get_versement_attendu(character varying);
CREATE OR REPLACE FUNCTION com_get_versement_attendu(IN headers_ character varying)
  RETURNS DOUBLE PRECISION AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;
BEGIN    
	SELECT INTO ca_ SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d WHERE d.type_doc = 'FV' AND d.document_lie IS NULL AND d.entete_doc::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head);
	SELECT INTO avance_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE d.type_doc = 'BCV' AND d.entete_doc::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head);
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avance : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu(character varying)
  OWNER TO postgres;
  
  
-- Function: com_get_versement_attendu(bigint, date, date)
-- DROP FUNCTION com_get_versement_attendu(bigint, date, date);
CREATE OR REPLACE FUNCTION com_get_versement_attendu(IN users_ bigint, IN date_debut_ date, IN date_fin_ date)
  RETURNS DOUBLE PRECISION AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;
BEGIN    
	SELECT INTO ca_ SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id WHERE d.type_doc = 'FV' AND d.document_lie IS NULL AND h.users = users_ AND e.date_entete BETWEEN date_debut_ AND date_fin_;
	SELECT INTO avance_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE d.type_doc = 'BCV' AND p.caissier = users_ AND p.date_paiement BETWEEN date_debut_ AND date_fin_;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avance : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu(bigint, date, date)
  OWNER TO postgres;
  
  
  
-- Function: et_total_agence(bigint, date, date, character varying)
-- DROP FUNCTION et_total_agence(bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_agence(IN societe_ bigint, IN date_debut_ date, IN date_fin_ date, IN reference_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, agence bigint, jour character varying, total double precision, quantite double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
	data_ RECORD;
	agence_ RECORD;

	max_ BIGINT DEFAULT 0;
	
	valeur_ character varying;
	query_ character varying default 'select a.id, a.codeagence AS code, a.designation FROM yvs_agences a WHERE a.societe = '||societe_;
	
	deja_ BOOLEAN DEFAULT FALSE;
BEGIN    
	DROP TABLE IF EXISTS table_total_agence;
	CREATE TEMP TABLE IF NOT EXISTS table_total_agence(_code character varying, _nom character varying, _agence bigint, _jour character varying, _total double precision, _quantite double precision, _taux double precision, _rang integer, _is_total boolean, _is_footer boolean);
	DELETE FROM table_total_agence;
	if(reference_ is not null and reference_ NOT IN ('', ' '))then
		query_ = query_ ||' AND a.codeagence LIKE ANY (ARRAY[';
		for valeur_ IN select val from regexp_split_to_table(reference_,',') val
		loop
			if(deja_)then
				query_ = query_ || ',';
			end if;
			query_ = query_ || ''''||TRIM(valeur_)||'%''';
			deja_ = true;
		end loop;
		query_ = query_ || '])';
	end if;
	FOR agence_ IN EXECUTE query_
	LOOP
		FOR data_ IN SELECT y.jour, y.rang, SUM(y.total) AS total, SUM(y.quantite) AS quantite, AVG(y.taux) AS taux, y.is_total FROM et_total_pt_vente(societe_, agence_.id, date_debut_, date_fin_, '', periode_) y WHERE y.is_footer IS FALSE GROUP BY y.jour, y.rang, y.is_total
		LOOP
			INSERT INTO table_total_agence VALUES(agence_.code, agence_.designation, agence_.id, data_.jour, data_.total, data_.quantite, data_.taux, data_.rang, data_.is_total, false);
		END LOOP;
	END LOOP;
	FOR data_ IN SELECT y._jour AS jour, SUM(COALESCE(y._total, 0)) AS total, SUM(COALESCE(y._quantite, 0)) AS quantite, AVG(COALESCE(y._taux, 0)) AS taux FROM table_total_agence y GROUP BY y._jour
	LOOP		
		SELECT INTO max_ MAX(y._rang) FROM table_total_agence y WHERE y._jour = data_.jour;
		insert into table_total_agence values('TOTAUX', 'TOTAUX', 0, data_.jour, data_.total, data_.quantite, data_.taux, max_ + 1, true, true);
	END LOOP;
	RETURN QUERY SELECT * FROM table_total_agence ORDER BY _is_footer, _agence, _rang;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_agence(bigint, date, date, character varying, character varying)
  OWNER TO postgres;



  
-- Function: com_et_dashboard(bigint, bigint, bigint, date, date)
-- DROP FUNCTION com_et_dashboard(bigint, bigint, bigint, date, date);
CREATE OR REPLACE FUNCTION com_et_dashboard(IN societe_ bigint, IN agence_ bigint, IN users_ bigint, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(nombre bigint, valeur double precision, code character varying, libelle character varying, rang integer) AS
$BODY$
declare 

   query_ character varying;
   code_ character varying;
   libelle_ character varying;
   
   valeur_ double precision default 0;
   autres_ double precision default 0;
   
   nombre_ bigint default 0;
   nombre_2 bigint default 0;
   
begin 	
	DROP TABLE IF EXISTS table_et_dashboard;
	CREATE TEMP TABLE IF NOT EXISTS table_et_dashboard(_nombre bigint, _valeur double precision, _code character varying, _libelle character varying, _rang integer);
	DELETE FROM table_et_dashboard;
	-- chiffre d'affaire reel ((ca valide + ca service) - ca avoir)
	-- chiffre d'affaire valider
	code_ = 'caVenteValide';
	libelle_ = 'Facture completement validée';
	query_ = 'SELECT SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(c.prix_total)', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 5);
	-- chiffre d'affaire sur services supplementaire
	code_ = 'caVenteValideSS';
	libelle_ = 'Service supplementaire validé';
	query_ = 'SELECT SUM(c.montant) FROM yvs_com_cout_sup_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id INNER JOIN yvs_grh_type_cout t ON c.type_cout = t.id
		WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND a.societe = '||societe_||' AND c.service IS TRUE AND t.augmentation IS FALSE AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO autres_;
	EXECUTE REPLACE(query_, 't.augmentation IS FALSE', 't.augmentation IS TRUE') INTO valeur_;
	valeur_ = COALESCE(valeur_, 0) - COALESCE(autres_, 0);
	INSERT INTO table_et_dashboard VALUES (COALESCE(0, 0), COALESCE(valeur_, 0), code_, libelle_, -1);
	-- chiffre d'affaire sur couts supplementaire
	code_ = 'caVenteValideCS';
	libelle_ = 'Cout supplementaire validé';
	query_ = REPLACE(query_, 'c.service IS TRUE', 'c.service IS FALSE');
	EXECUTE query_ INTO autres_;
	EXECUTE REPLACE(query_, 't.augmentation IS FALSE', 't.augmentation IS TRUE') INTO valeur_;
	valeur_ = COALESCE(valeur_, 0) - COALESCE(autres_, 0);
	INSERT INTO table_et_dashboard VALUES (COALESCE(0, 0), COALESCE(valeur_, 0), code_, libelle_, -2);
	-- chiffre d'affaire avoir
	code_ = 'caVenteAvoir';
	libelle_ = 'Facture d''avoir validée';
	query_ = 'SELECT SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''BRV'' AND d.statut = ''V'' AND a.societe = '||societe_||' AND d.date_livraison BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO autres_;
	EXECUTE REPLACE(query_, 'SUM(c.prix_total)', 'COUNT(DISTINCT d.id)') INTO nombre_2;
	query_ = REPLACE(REPLACE(query_, 'd.date_livraison', 'e.date_entete'), 'BRV', 'FAV');
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(c.prix_total)', 'COUNT(DISTINCT d.id)') INTO nombre_;
	valeur_ = COALESCE(valeur_, 0) + COALESCE(autres_, 0);
	nombre_ = COALESCE(nombre_, 0) + COALESCE(nombre_2, 0);
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 6);
	autres_ = valeur_;
	nombre_ = nombre_2;
	-- chiffre d'affaire reel
	code_ = 'caVente';
	libelle_ = 'Chiffre d''affaire réel';
	SELECT INTO valeur_ SUM(_valeur) FROM table_et_dashboard WHERE _code IN ('caVenteValide', 'caVenteValideSS');
	SELECT INTO nombre_ _nombre FROM table_et_dashboard WHERE _code = 'caVenteValide';
	valeur_ = COALESCE(valeur_, 0) - COALESCE(autres_, 0);
	nombre_ = COALESCE(nombre_, 0) - COALESCE(nombre_2, 0);
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 0);
	
	-- chiffre d'affaire en attente
	code_ = 'caVenteAttence';
	libelle_ = 'Facture en attente de validation';
	query_ = 'SELECT sum(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut = ''E'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(c.prix_total)', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 2);
	
	-- chiffre d'affaire en cours
	code_ = 'caVenteEnCours';
	libelle_ = 'Facture en cours de validation';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut = ''R'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 3);
	
	-- chiffre d'affaire annulé
	code_ = 'caVenteAnnule';
	libelle_ = 'Facture annulée';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut = ''A'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 4);
	
	-- chiffre d'affaire livré
	code_ = 'caVenteLivre';
	libelle_ = 'Facture completement livrée';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_livre = ''L'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 7);
	
	-- chiffre d'affaire livraison en cours
	code_ = 'caVenteEnCoursLivre';
	libelle_ = 'Facture en cours de livraison';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_livre = ''R'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 8);
	
	-- chiffre d'affaire livraison en attence
	code_ = 'caVenteNotLivre';
	libelle_ = 'Facture en attente de livraison';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_livre = ''W'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 9);
	
	-- chiffre d'affaire livraison en retard
	code_ = 'caVenteRetardLivr';
	libelle_ = 'Facture en retard de livraison';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_livre != ''L'' AND a.societe = '||societe_||' AND d.date_livraison_prevu < '||QUOTE_LITERAL(date_fin_)||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 10);
	
	-- chiffre d'affaire réglé
	code_ = 'caVenteRegle';
	libelle_ = 'Facture completement réglée';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_regle = ''P'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 11);
	
	-- chiffre d'affaire reglement en cours
	code_ = 'caVenteEnCoursRegle';
	libelle_ = 'Facture en cours de reglement';
	query_ = 'SELECT SUM(c.montant) FROM yvs_compta_caisse_piece_vente c INNER JOIN yvs_com_doc_ventes d ON c.vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_regle = ''R'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(c.montant)', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 12);
	
	-- chiffre d'affaire réglé
	code_ = 'caVenteNotRegle';
	libelle_ = 'Facture en attente de reglement';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut_regle = ''W'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 14);
	
	-- total facture
	code_ = 'ca';
	libelle_ = 'Chiffre d''affaire Provisoire';
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
	SELECT INTO autres_ SUM(_valeur) FROM table_et_dashboard WHERE _code = 'caVenteAvoir';
	valeur_ = COALESCE(valeur_, 0) - COALESCE(autres_, 0);
	INSERT INTO table_et_dashboard VALUES (COALESCE(nombre_, 0), COALESCE(valeur_, 0), code_, libelle_, 1);
	
	RETURN QUERY SELECT * FROM table_et_dashboard ORDER BY _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_dashboard(bigint, bigint, bigint, date, date)
  OWNER TO postgres;

-- Function: action_in_header_vente_or_piece_virement(bigint)

-- DROP FUNCTION action_in_header_vente_or_piece_virement(bigint);

CREATE OR REPLACE FUNCTION action_in_header_vente_or_piece_virement(header_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE 
	entete_ RECORD;
	
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	ecart_ DOUBLE PRECISION DEFAULT 0;

	id_ BIGINT DEFAULT 0;

	numero_ CHARACTER VARYING DEFAULT '';
	reference_ CHARACTER VARYING DEFAULT '';
	numeric_ INTEGER DEFAULT 0;
   
BEGIN 	
	IF(COALESCE(header_, 0) > 0)THEN
		SELECT INTO entete_ y.date_entete, h.users, y.author, a.societe FROM yvs_com_entete_doc_vente y INNER JOIN yvs_com_creneau_horaire_users h ON y.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id WHERE y.id = header_;	
		ca_ = (SELECT com_get_versement_attendu(header_::character varying));
		SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_virement y INNER JOIN yvs_compta_notif_versement_vente n ON n.piece = y.id WHERE y.statut_piece = 'P' AND n.entete_doc = header_;
		avance_ = COALESCE(avance_, 0);
		ecart_ =  avance_ - ca_;
		SELECT INTO id_ y.id FROM yvs_com_ecart_entete_vente y WHERE y.entete_doc = header_;
		IF(COALESCE(id_, 0) > 0)THEN
			IF(ecart_ != 0)THEN
				UPDATE yvs_com_ecart_entete_vente SET montant = ecart_ WHERE id = id_;
			ELSE
				DELETE FROM yvs_com_ecart_entete_vente WHERE id = id_;
			END IF;
		ELSIF(ecart_ != 0)THEN
			numero_ = 'ECR/';
			numero_ = numero_ || to_char(entete_.date_entete ,'ddMMyy') || '/';
			SELECT INTO reference_ y.numero FROM yvs_com_ecart_entete_vente y INNER JOIN yvs_users u ON y.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id WHERE numero LIKE numero_ ||'%' AND a.societe = entete_.societe;
			IF(COALESCE(reference_, '') != '')THEN
				numeric_ = TRIM(REPLACE(reference_, numero_, ' '))::INTEGER;
				numeric_ = numeric_ + 1;
				numero_ = numero_ || REPLACE(to_char(numeric_,'999'), ' ', '0');
			ELSE
				numero_ = numero_ || REPLACE(to_char(1,'999'), ' ', '0');
			END IF;
			INSERT INTO yvs_com_ecart_entete_vente(date_ecart, numero, montant, entete_doc, users, statut, statut_regle, author) 
				VALUES(entete_.date_entete, numero_, ecart_, header_, entete_.users, 'E', 'W', entete_.author);
		END IF;
		RETURN TRUE;
	END IF;
	RETURN FALSE;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_in_header_vente_or_piece_virement(bigint)
  OWNER TO postgres;

  
  
-- Function: com_et_ecart_vente(bigint, bigint, bigint, date, date, character varying)
-- DROP FUNCTION com_et_ecart_vente(bigint, bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION com_et_ecart_vente(IN societe_ bigint, IN agence_ bigint, IN users_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying)
  RETURNS TABLE(agence bigint, users bigint, code character varying, nom character varying, entete character varying, versement_attendu double precision, versement_reel double precision, ecart double precision, solde double precision, rang integer) AS
$BODY$
declare 

   vendeur_ RECORD;
   dates_ RECORD;
   
   jour_ character varying;
   
   versement_attendu_ double precision default 0;
   versement_reel_ double precision default 0;
   ecart_ double precision default 0;
   solde_ double precision default 0;
   
   query_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT y.id, y.code_users as code, y.nom_users as nom, y.agence FROM yvs_com_ecart_entete_vente e INNER JOIN yvs_users y ON e.users = y.id INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.code_users IS NOT NULL';

   i integer default 0;
   
begin 	
	--DROP TABLE IF EXISTS table_et_ecart_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ecart_vente(_agence bigint, _users bigint, _code character varying, _nom character varying, _entete character varying, _versement_attendu double precision, _versement_reel double precision, _ecart double precision, _solde double precision, _rang integer);
	DELETE FROM table_et_ecart_vente;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND y.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	IF(users_ IS NOT NULL AND users_ > 0)THEN
		query_ = query_ || ' AND y.id = '||users_;
	END IF;
	RAISE NOTICE 'query_ %',query_;
	FOR vendeur_ IN EXECUTE query_
	LOOP
		i = 0;
		solde_ = 0;
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_)
		LOOP
			i = i + 1;
			jour_ = dates_.intitule;
			SELECT INTO versement_attendu_ SUM(com_get_versement_attendu(y.id::character varying)) FROM yvs_com_entete_doc_vente y INNER JOIN yvs_com_creneau_horaire_users h ON y.creneau = h.id WHERE h.users = vendeur_.id AND y.date_entete BETWEEN dates_.date_debut AND dates_.date_fin;
			versement_attendu_ = COALESCE(versement_attendu_, 0);
			SELECT INTO versement_reel_ SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source = vendeur_.id AND y.statut_piece = 'P' AND y.date_paiement BETWEEN dates_.date_debut AND dates_.date_fin;
			versement_reel_ = COALESCE(versement_reel_, 0);
			ecart_ = versement_attendu_ - versement_reel_;
			solde_ = solde_ + ecart_;
			IF(ecart_ != 0)THEN
				INSERT INTO table_et_ecart_vente VALUES(vendeur_.agence, vendeur_.id, vendeur_.code, vendeur_.nom, jour_, versement_attendu_, versement_reel_, ecart_, solde_, i);
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_ecart_vente ORDER BY _agence, _code, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ecart_vente(bigint, bigint, bigint, date, date, character varying)
  OWNER TO postgres;

  
-- Function: insert_contenu_doc_stock()
-- DROP FUNCTION insert_contenu_doc_stock();
CREATE OR REPLACE FUNCTION insert_contenu_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	trancheD_ bigint;
	trancheS_ bigint;
	result boolean default false;
BEGIN
	select into doc_ id, type_doc, date_doc, statut, source, destination, creneau_destinataire, creneau_source from yvs_com_doc_stocks where id = NEW.doc_stock;
	select into trancheD_ tranche from yvs_com_creneau_depot where id = doc_.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = doc_.creneau_source;
	
	if(doc_.statut = 'V') then
		if(doc_.type_doc = 'FT') then
		--Insertion mouvement stock		
			if(doc_.source is not null)then
				result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
			if(doc_.destination is not null)then
				result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'SS') then
			if(doc_.source is not null)then
				result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'ES') then
			IF(doc_.destination is not null) THEN
				result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			END IF;
		elsif(doc_.type_doc = 'IN') then
			if(doc_.source is not null)then
				if(NEW.quantite>0)then
					result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				elsif(NEW.quantite<0)then
					result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;
			end if;
		elsif(doc_.type_doc = 'TR') then
			if(doc_.source is not null)then
				result = (select valorisation_stock(NEW.article, NEW.conditionnement,doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				result = (select valorisation_stock(NEW.article, NEW.conditionnement_entree,doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_stock()
  OWNER TO postgres;


-- Function: com_et_journal_vente(bigint, bigint, date, date)
-- DROP FUNCTION com_et_journal_vente(bigint, bigint, date, date);
CREATE OR REPLACE FUNCTION com_et_journal_vente(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(agence bigint, users bigint, code character varying, nom character varying, classe bigint, reference character varying, designation character varying, montant double precision, is_classe boolean, is_vendeur boolean, rang integer) AS
$BODY$
declare 
   vendeur_ RECORD;
   classe_ record;
   
   journal_ character varying;
   
   valeur_ double precision default 0;
   totaux_ double precision;
   
   query_ CHARACTER VARYING DEFAULT 'SELECT y.id, y.code_users, y.nom_users as nom, y.agence FROM yvs_users y INNER JOIN yvs_agences a ON y.agence = a.id WHERE code_users IS NOT NULL';
   
begin 	
	--DROP TABLE table_et_journal_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_journal_vente(_agence BIGINT, _users BIGINT, _code CHARACTER VARYING, _nom CHARACTER VARYING, _classe BIGINT, _reference CHARACTER VARYING, _designation CHARACTER VARYING, _montant DOUBLE PRECISION, _is_classe BOOLEAN, _is_vendeur BOOLEAN, _rang INTEGER);
	DELETE FROM table_et_journal_vente;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND y.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	FOR vendeur_ IN EXECUTE query_
	LOOP
		-- Vente directe par classe statistique
		FOR classe_ IN SELECT c.id, UPPER(c.code_ref) as code, UPPER(c.designation) as intitule FROM yvs_base_classes_stat c WHERE c.societe = societe_
		LOOP
			SELECT INTO valeur_ SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN (SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
				INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
				WHERE hu.users = vendeur_.id AND a.classe1 = classe_.id AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), TRUE, TRUE, 0);	
			END IF;
		END LOOP;
		
		-- CA Des article non classé
		SELECT INTO valeur_ SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
			WHERE hu.users = vendeur_.id AND a.classe1 IS NULL AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), TRUE, TRUE, 0);
		END IF;
		
		-- CA Par vendeur
		SELECT INTO valeur_ SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
			WHERE hu.users = vendeur_.id AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);	
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, TRUE, 1);
		
		-- Ristournes vente directe
		SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
			WHERE hu.users = vendeur_.id AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL AND e.date_entete BETWEEN date_debut_ AND date_fin_);
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, TRUE, 2);
		
		-- Commandes Annulées
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'A' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'CMDE.A', 'CMDE.A', COALESCE(valeur_, 0), FALSE, TRUE, 3);
					
		-- Commandes Recu
		SELECT INTO valeur_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id WHERE d.type_doc = 'BCV' AND d.statut = 'V' AND y.statut_piece = 'P' AND y.caissier = vendeur_.id AND y.date_paiement BETWEEN date_debut_ AND date_fin_;
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'CMDE.R', 'CMDE.R', COALESCE(valeur_, 0), FALSE, TRUE, 4);		
		
		-- Versement attendu	
		SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = vendeur_.id AND _rang > 0; 
		INSERT INTO table_et_journal_vente values (vendeur_.agence, vendeur_.id, vendeur_.code_users, vendeur_.nom, 0, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, TRUE, 5);	

		SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE y._users = vendeur_.id;
		IF(COALESCE(valeur_, 0) = 0)THEN
			DELETE FROM table_et_journal_vente WHERE _users = vendeur_.id;
		END IF;
	END LOOP;
	
	-- LIGNE DES COMMANDE SERVIES PAR CLASSE STAT
	query_ = 'SELECT SUM(y.prix_total - y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
			INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
			WHERE d.type_doc = ''BCV'' AND d.statut = ''V'' AND d.statut_livre = ''L'' AND d.date_livraison BETWEEN '''||date_debut_||''' AND '''||date_fin_||'''';
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND u.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND g.societe = '||societe_;
	END IF;
	FOR classe_ IN SELECT c.id, UPPER(c.code_ref) as code, UPPER(c.designation) as intitule FROM yvs_base_classes_stat c WHERE c.societe = societe_
	LOOP			
		EXECUTE query_ || ' AND a.classe1 = '||classe_.id||')' INTO valeur_;
		IF(COALESCE(valeur_, 0) > 0)THEN
			INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', classe_.id, classe_.code, classe_.intitule, COALESCE(valeur_, 0), FALSE, FALSE, 0);	
		END IF;
	END LOOP;
	
	-- Autres aticles sans classe
	EXECUTE query_ || ' AND a.classe1 IS NULL) ' INTO valeur_;
	IF(COALESCE(valeur_, 0) > 0)THEN
		INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'AUTRES', 'AUTRES', COALESCE(valeur_, 0), FALSE, FALSE, 0);
	END IF;			
	
	-- CA sur commande reçu
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = 0; 
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'C.A', 'C.A', COALESCE(valeur_, 0), FALSE, FALSE, 1);
	
	-- Ristourne sur commande reçu
	SELECT INTO valeur_ SUM(y.ristourne) FROM yvs_com_contenu_doc_vente y WHERE y.id IN(SELECT DISTINCT c.id FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e on e.id = d.entete_doc INNER JOIN yvs_com_contenu_doc_vente c ON d.id = c.doc_vente 
		INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_com_creneau_horaire_users hu ON e.creneau = hu.id
		INNER JOIN yvs_users u ON hu.users = u.id INNER JOIN yvs_agences g ON u.agence = g.id
		WHERE g.id = agence_ AND d.type_doc = 'BCV' AND d.statut = 'V' AND d.statut_livre = 'L' AND d.date_livraison BETWEEN date_debut_ AND date_fin_);
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'RIST.', 'RISTOURNE', COALESCE(valeur_, 0), FALSE, FALSE, 2);
	
	-- CMDE.A sur commande reçu
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'CMDE.A', 'CMDE.A', 0, FALSE, FALSE, 3);
	
	-- CMDE.R sur commande reçu
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'CMDE.R', 'CMDE.R', 0, FALSE, FALSE, 4);
	
	-- VERS.ATT sur commande reçu
	SELECT INTO valeur_ SUM(y._montant) FROM table_et_journal_vente y WHERE _users = 0 AND _rang > 0; 
	INSERT INTO table_et_journal_vente values (agence_, 0, 'CMDE.SERVI','CMDE.SERVI', 0, 'VERS.ATT', 'VERS.ATT', COALESCE(valeur_, 0), FALSE, FALSE, 5);

	FOR classe_ IN SELECT _classe, _reference, _designation, _rang, SUM(_montant) AS _valeur FROM table_et_journal_vente GROUP BY _classe, _reference, _designation, _rang
	LOOP 
		INSERT INTO table_et_journal_vente values (agence_, -1, 'TOTAUX','TOTAUX', classe_._classe, classe_._reference, classe_._designation, classe_._valeur, FALSE, FALSE, classe_._rang);
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_journal_vente ORDER BY _agence, _is_vendeur DESC, _code, _rang, _is_classe DESC, _classe;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_journal_vente(bigint, bigint, date, date)
  OWNER TO postgres;