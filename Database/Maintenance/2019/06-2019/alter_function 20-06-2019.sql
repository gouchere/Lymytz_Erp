-- Function: update_contenu_doc_stock()
-- DROP FUNCTION update_contenu_doc_stock();
CREATE OR REPLACE FUNCTION update_contenu_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	entree_ record;
	sortie_ record;
	ligne_ record;
	
	mouv_ bigint;
	trancheD_ bigint;
	trancheS_ bigint;
	
	result boolean default false;
BEGIN
	select into doc_ * from yvs_com_doc_stocks where id = NEW.doc_stock;
	select into trancheD_ tranche from yvs_com_creneau_depot where id = doc_.creneau_destinataire;
	select into trancheS_ tranche from yvs_com_creneau_depot where id = doc_.creneau_source;
	select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = NEW.article;
	--Insertion mouvement stock
	if(doc_.statut = 'V' or doc_.statut = 'U' or doc_.statut = 'R') then
		if(doc_.type_doc = 'FT') then	
			if(doc_.source is not null and (doc_.statut = 'U' or doc_.statut = 'V' or doc_.statut = 'R'))then
				select into mouv_ id from yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME and depot = doc_.source and mouvement = 'S' ORDER BY id;				
				if(coalesce(mouv_, 0) > 0)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME and mouvement = 'S';
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, article = NEW.article, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr, date_doc=doc_.date_doc WHERE id = mouv_;
						FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME and mouvement = 'S' AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
					end if;
				else
					result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;	
				FOR ligne_ IN SELECT y.id, y.quantite, y.date_reception FROM yvs_com_contenu_doc_stock_reception y WHERE contenu = NEW.id
				LOOP
					for mouv_ in select id from yvs_base_mouvement_stock where id_externe = ligne_.id and table_externe = 'yvs_com_contenu_doc_stock_reception'
					loop
						delete from yvs_base_mouvement_stock where id = mouv_;
					end loop;
					result = (select valorisation_stock(NEW.article, NEW.conditionnement_entree, doc_.destination, trancheD_, ligne_.quantite, NEW.prix_entree, 'yvs_com_contenu_doc_stock_reception', ligne_.id, 'E', ligne_.date_reception));
				END LOOP;
			end if;	
		elsif(doc_.type_doc = 'SS') then
			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = NEW.id and table_externe = TG_TABLE_NAME;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
						result = (select valorisation_stock(NEW.article, NEW.conditionnement,doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, article = NEW.article, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr , date_doc=doc_.date_doc WHERE id = mouv_;
						FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
					end if;
				else
					result = (select valorisation_stock(NEW.article, NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				end if;	
			end if;
		elsif(doc_.type_doc = 'ES') then
			IF(doc_.destination is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination AND mouvement = 'E';
				IF(mouv_ is not null)THEN
						IF(arts_.methode_val = 'FIFO')then
							delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = NEW.article and mouvement = 'E';
							result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
						ELSE
							UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, article = NEW.article, conditionnement = NEW.conditionnement_entree, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr , date_doc=doc_.date_doc WHERE id = mouv_;
							FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
						END IF;
				ELSE
					result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				END IF;	
			END IF;
		elsif(doc_.type_doc = 'IN') then
			if(doc_.source is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
						if(NEW.quantite>0)then
							result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
						elsif(NEW.quantite<0)then
							result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
						end if;
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, article = NEW.article, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr , date_doc=doc_.date_doc WHERE id = mouv_;
						FOR ligne_ IN SELECT id FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id and table_externe = TG_TABLE_NAME AND parent IS NULL ORDER BY id
						LOOP
							IF(ligne_.id != mouv_)THEN
								DELETE FROM yvs_base_mouvement_stock WHERE id = ligne_.id;
							END IF;
						END LOOP;
					end if;
				else
					if(NEW.quantite>0)then
						result = (select valorisation_stock(NEW.article,NEW.conditionnement, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					elsif(NEW.quantite<0)then
						result = (select valorisation_stock(NEW.article, NEW.conditionnement_entree, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					end if;
				end if;	
			end if;
		elsif(doc_.type_doc = 'TR') then
			if(doc_.source is not null)then
				select into entree_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement;
				select into sortie_ a.id, a.methode_val, c.prix from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where a.id = NEW.article and c.id = NEW.conditionnement_entree;

				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article;
						result = (select valorisation_stock(NEW.article,NEW.conditionnement,  doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
						result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree,  doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
					else
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite, cout_entree = NEW.prix, article = NEW.article, conditionnement = NEW.conditionnement, lot = NEW.lot_sortie, calcul_pr = NEW.calcul_pr, date_doc=doc_.date_doc
													    WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'S';
						UPDATE yvs_base_mouvement_stock SET quantite = NEW.quantite_entree, cout_entree = NEW.prix_entree, article = NEW.article, conditionnement = NEW.conditionnement_entree, lot = NEW.lot_entree, calcul_pr = NEW.calcul_pr, date_doc=doc_.date_doc 
														WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = NEW.article and mouvement = 'E';
					end if;
				else
					result = (select valorisation_stock(NEW.article,NEW.conditionnement,  doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
					result = (select valorisation_stock(NEW.article,NEW.conditionnement_entree,  doc_.source, trancheS_, NEW.quantite_entree, NEW.prix_entree, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;	
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_contenu_doc_stock()
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
	query_ = 'SELECT SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id 
		INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id 
		WHERE d.type_doc = ''FV'' AND d.statut = ''E'' AND a.societe = '||societe_||' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	IF(COALESCE(agence_, 0) > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(users_, 0) > 0)THEN
		query_ = query_ || ' AND u.id = '||users_;
	END IF;
	EXECUTE query_ INTO valeur_;
	EXECUTE REPLACE(query_, 'SUM(get_ca_vente(d.id))', 'COUNT(DISTINCT d.id)') INTO nombre_;
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


-- Function: com_et_dashboard_vendeur(bigint, bigint, bigint, bigint, date, date, boolean)
-- DROP FUNCTION com_et_dashboard_vendeur(bigint, bigint, bigint, bigint, date, date, boolean);
CREATE OR REPLACE FUNCTION com_et_dashboard_vendeur(IN societe_ bigint, IN agence_ bigint, IN client_ bigint, IN vendeur_ bigint, IN date_debut_ date, IN date_fin_ date, IN group_ boolean)
  RETURNS TABLE(client bigint, nom_client character varying, numero character varying, date date, montant double precision, avance double precision, acompte double precision, credit double precision, reste double precision, solde_initial double precision, rang integer, vendeur bigint, nom_vendeur character varying) AS
$BODY$
declare 
   ligne_ RECORD;
   dates_ RECORD;
   
   montant_ DOUBLE PRECISION DEFAULT 0;
   avance_ DOUBLE PRECISION DEFAULT 0;
   acompte_ DOUBLE PRECISION DEFAULT 0;
   credit_ DOUBLE PRECISION DEFAULT 0;
   reste_ DOUBLE PRECISION DEFAULT 0;
   solde_initial_ DOUBLE PRECISION DEFAULT 0;

   rang_ INTEGER DEFAULT 0;

   date_initial DATE DEFAULT '01-01-2000';

   query_ CHARACTER VARYING DEFAULT '';
   
begin 	
	DROP TABLE IF EXISTS table_et_dashboard_vendeur;
	CREATE TEMP TABLE IF NOT EXISTS table_et_dashboard_vendeur(_client bigint, _nom_client character varying, _numero character varying, _date date, _montant double precision, _avance double precision, _acompte double precision, _credit double precision, _reste double precision, _solde_initial double precision, _rang integer, _vendeur bigint, _nom_vendeur character varying);
	DELETE FROM table_et_dashboard_vendeur;
	IF(group_)THEN -- SOLDE CUMULES PAR PERIODE
		query_ = 'SELECT DISTINCT cl.id AS client, CONCAT(cl.nom, '' '', cl.prenom) AS nom, u.id AS users, u.nom_users FROM  yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id 
			INNER JOIN yvs_com_client cl ON y.client = cl.id INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id INNER JOIN yvs_users u ON c.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id
			WHERE y.type_doc = ''FV'' AND y.statut = ''V'' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
		IF(societe_ IS NOT NULL AND societe_ > 0)THEN
			query_ = query_ || ' AND a.societe = '||societe_;
		END IF;
		IF(agence_ IS NOT NULL AND agence_ > 0)THEN
			query_ = query_ || ' AND a.id = '||agence_;
		END IF;
		IF(client_ IS NOT NULL AND client_ > 0)THEN
			query_ = query_ || ' AND y.client = '||client_;
		END IF;
		IF(vendeur_ IS NOT NULL AND vendeur_ > 0)THEN
			query_ = query_ || ' AND c.users = '||vendeur_;
		END IF;
		RAISE NOTICE 'query_ : %', query_;
		FOR ligne_ IN EXECUTE query_ || ' ORDER BY u.nom_users, nom'
		LOOP
			-- CHIFFRE AFFAIRE 
			montant_ = (SELECT get_ca_client(ligne_.client, ligne_.users, date_debut_, date_fin_));
			-- REGLEMENT 
			SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON y.vente = d.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id WHERE y.statut_piece = 'P' AND d.client = ligne_.client AND y.caissier = ligne_.users AND e.date_entete BETWEEN date_debut_ AND date_fin_;
			reste_ = COALESCE(solde_initial_, 0) + ((COALESCE(montant_, 0) + COALESCE(credit_, 0)) - (COALESCE(avance_, 0) + COALESCE(acompte_, 0)));
			SELECT INTO rang_ MAX(rang_) FROM table_et_dashboard_vendeur;
			rang_ = COALESCE(rang_, 0) + 1;
			INSERT INTO table_et_dashboard_vendeur VALUES(ligne_.client, 'CREANCE', ligne_.nom, date_debut_, COALESCE(montant_, 0), COALESCE(avance_, 0), COALESCE(acompte_, 0), COALESCE(credit_, 0), COALESCE(reste_, 0), COALESCE(solde_initial_, 0), rang_, ligne_.users, ligne_.nom_users);
		END LOOP;	
	ELSE	-- SOLDE DETAILLE PAR FACTURE
		query_ = 'SELECT y.id, y.num_doc, e.date_entete, cl.id AS client, CONCAT(cl.nom, '' '', cl.prenom) AS nom, u.id AS users, u.nom_users FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id 
			INNER JOIN yvs_com_client cl ON y.client = cl.id INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id INNER JOIN yvs_users u ON c.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id
			WHERE y.type_doc = ''FV'' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
		IF(societe_ IS NOT NULL AND societe_ > 0)THEN
			query_ = query_ || ' AND a.societe = '||societe_;
		END IF;
		IF(agence_ IS NOT NULL AND agence_ > 0)THEN
			query_ = query_ || ' AND a.id = '||agence_;
		END IF;
		IF(client_ IS NOT NULL AND client_ > 0)THEN
			query_ = query_ || ' AND y.client = '||client_;
		END IF;
		IF(vendeur_ IS NOT NULL AND vendeur_ > 0)THEN
			query_ = query_ || ' AND c.users = '||vendeur_;
		END IF;
		RAISE NOTICE 'query_ : %', query_;
		FOR ligne_ IN EXECUTE query_ || ' ORDER BY e.date_entete'
		LOOP
			-- CHIFFRE AFFAIRE 
			montant_ = (SELECT get_ca_vente(ligne_.id));
			-- REGLEMENT 
			SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_vente y WHERE y.statut_piece = 'P' AND y.vente = ligne_.id;
			reste_ = COALESCE(solde_initial_, 0) + ((COALESCE(montant_, 0) + COALESCE(credit_, 0)) - (COALESCE(avance_, 0) + COALESCE(acompte_, 0)));
			SELECT INTO rang_ MAX(rang_) FROM table_et_dashboard_vendeur;
			rang_ = COALESCE(rang_, 0) + 1;
			INSERT INTO table_et_dashboard_vendeur VALUES(ligne_.client, ligne_.nom, ligne_.num_doc, ligne_.date_entete, COALESCE(montant_, 0), COALESCE(avance_, 0), COALESCE(acompte_, 0), COALESCE(credit_, 0), COALESCE(reste_, 0), COALESCE(solde_initial_, 0), rang_, ligne_.users, ligne_.nom_users);
		END LOOP;
	END IF;
	RETURN QUERY SELECT * FROM table_et_dashboard_vendeur ORDER BY _rang, _date DESC, numero DESC;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_dashboard_vendeur(bigint, bigint, bigint, bigint, date, date, boolean)
  OWNER TO postgres;



-- Function: et_total_articles(bigint, bigint, date, date, character varying, character varying)
DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, pr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE

    
BEGIN    
	RETURN QUERY SELECT * FROM et_total_articles(societe_, agence_, date_debut_, date_fin_, '', '', periode_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying)
  OWNER TO postgres;


-- Function: et_total_articles(bigint, bigint, date, date, character varying)
DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, pr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    
BEGIN    
	RETURN QUERY SELECT * FROM et_total_articles(societe_, agence_, date_debut_, date_fin_, '', periode_);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying)
  OWNER TO postgres;


-- Function: et_total_articles(bigint, bigint, date, date, character varying, character varying, character varying)
-- DROP FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying, character varying);
CREATE OR REPLACE FUNCTION et_total_articles(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date, IN article_ character varying, IN categorie_ character varying, IN periode_ character varying)
  RETURNS TABLE(code character varying, nom character varying, article bigint, unite bigint, reference character varying, periode character varying, total double precision, quantite double precision, pr double precision, taux double precision, rang integer, is_total boolean, is_footer boolean) AS
$BODY$
DECLARE
    _date_save DATE DEFAULT date_debut_;
    date_ DATE;
    
    taux_ DOUBLE PRECISION DEFAULT 0;
    pr_ DOUBLE PRECISION DEFAULT 0;
    _total DOUBLE PRECISION DEFAULT 0;
    _quantite DOUBLE PRECISION DEFAULT 0;
    _pr DOUBLE PRECISION DEFAULT 0;
    _taux DOUBLE PRECISION DEFAULT 0;
    
    i INTEGER DEFAULT 0;
    count_ INTEGER DEFAULT 0;

    
    jour_ CHARACTER VARYING;
    execute_ CHARACTER VARYING;   
    facture_avoir_ CHARACTER VARYING;   
    query_ CHARACTER VARYING default 'select c.id as unite, a.id, a.ref_art, a.designation, m.reference, sum(coalesce(y.prix_total, 0)) as total, sum(coalesce(y.quantite, 0)) as qte from yvs_com_contenu_doc_vente y 
					inner join yvs_base_articles a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id inner join yvs_base_unite_mesure m on c.unite = m.id 
					inner join yvs_base_famille_article f ON a.famille = f.id inner join yvs_com_doc_ventes d on y.doc_vente = d.id inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc 
					inner join yvs_com_creneau_horaire_users u on e.creneau = u.id  inner join yvs_com_creneau_point o on u.creneau_point = o.id inner join yvs_base_point_vente p on o.point = p.id
					where d.statut = ''V''';
    
    data_ RECORD;
    avoir_ RECORD;
    prec_ RECORD;
    _article RECORD;
    dates_ RECORD;

    deja_ BOOLEAN DEFAULT FALSE;
    
BEGIN    
	CREATE TEMP TABLE IF NOT EXISTS table_total_articles(_code_ CHARACTER VARYING, _nom_ CHARACTER VARYING, _article_ BIGINT, _unite bigint, _reference character varying, _periode_ CHARACTER VARYING, _total_ DOUBLE PRECISION, _quantite_ DOUBLE PRECISION, _pr_ DOUBLE PRECISION, _taux_ DOUBLE PRECISION, _rang_ INTEGER, _is_total_ BOOLEAN, _is_footer_ BOOLEAN);
	DELETE FROM table_total_articles;
	deja_ = false;
	query_ = query_ ||' AND f.societe = '||societe_;
	if(article_ is not null and article_ NOT IN ('', ' '))then
		query_ = query_ ||' AND a.ref_art LIKE ANY (ARRAY[';
		for jour_ IN select val from regexp_split_to_table(article_,',') val
		loop
			if(deja_)then
				query_ = query_ || ',';
			end if;
			query_ = query_ || ''''||TRIM(jour_)||'%''';
			deja_ = true;
		end loop;
		query_ = query_ || '])';
	end if;
	if(categorie_ is not null and categorie_ NOT IN ('', ' '))then
		query_ = query_ ||' and a.categorie = '||QUOTE_LITERAL(categorie_);
	end if;
	if(agence_ is not null and agence_ > 0)then
		query_ = query_ ||' and p.agence = '||agence_;
	end if;
	
	date_debut_ = _date_save;
	i = 0;
	for dates_ in select * from decoupage_interval_date(date_debut_, date_fin_, periode_)
	loop
		jour_ = dates_.intitule;
		count_ = 1;
		_total = 0;
		_quantite = 0;
		_taux = 0;
		_pr = 0;
		execute_ = query_ ||' and ((d.type_doc = ''FV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||'''))' || ' GROUP BY c.id, m.id, a.id';
		FOR _article IN EXECUTE execute_
		LOOP			
			facture_avoir_ = query_ ||' and y.conditionnement = '||_article.unite||' and y.article = '||_article.id||'
					and ((d.type_doc = ''FAV'' and e.date_entete between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''') or (d.type_doc = ''BRV'' and d.date_livraison between '''||dates_.date_debut||''' AND '''||dates_.date_fin||''')) GROUP BY c.id, m.id, a.id';
			execute facture_avoir_ into avoir_;	
			if(avoir_.total IS NULL)then
				avoir_.total = 0;					
			end if;
			_article.total = _article.total - avoir_.total;
			
			SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id and _unite = _article.unite ORDER BY _rang_ DESC LIMIT 1;
			IF(prec_.total = 0)THEN
				SELECT INTO prec_ _total_ AS total FROM table_total_articles WHERE _article_ = _article.id and _unite = _article.unite AND _total_ > 0 ORDER BY _rang_ DESC LIMIT 1;
			END IF;
			taux_ = _article.total - prec_.total;
			IF(taux_ IS NULL)THEN
				taux_ = 0;
			END IF;
			IF(taux_ != 0 and coalesce(prec_.total, 0) != 0)THEN
				taux_ = (taux_ / prec_.total) * 100;
			END IF;
			SELECT INTO pr_ AVG(y.cout_stock) FROM yvs_base_mouvement_stock y WHERE y.conditionnement = _article.unite AND y.date_doc BETWEEN dates_.date_debut AND dates_.date_fin;
			IF(pr_ IS NULL)THEN
				pr_ = 0;
			END IF;

			INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, _article.unite, _article.reference, jour_, _article.total, _article.qte, pr_, taux_, i, FALSE, FALSE);

			_total = _total + _article.total;
			_quantite = _quantite + _article.qte;
			_taux = _taux + taux_;
			_pr = _pr + pr_;
			count_ = count_ + 1;
		END LOOP;
		i = i + 1;
		IF(_total > 0)THEN
			INSERT INTO table_total_articles VALUES('TOTAUX', 'TOTAUX', 0, 0, '', jour_, _total, _quantite, (_pr / count_), (_taux / count_), i, TRUE, TRUE);
		END IF;
	END LOOP;
	FOR _article IN SELECT _article_ AS id, _nom_ AS designation, _code_ AS ref_art, _unite AS unite, _reference AS reference, COALESCE(sum(_total_), 0) AS total, COALESCE(sum(_quantite_), 0) AS qte, COALESCE(avg(_pr_), 0) AS pr, COALESCE(sum(_taux_), 0) AS taux 
		FROM table_total_articles y GROUP BY _article_, _code_, _nom_, _unite, _reference
	LOOP		
		SELECT INTO i count(_rang_) FROM table_total_articles WHERE _article_ = _article.id AND _unite = _article.unite;
		IF(i IS NULL OR i = 0)THEN
			i = 1;
		END IF;
		INSERT INTO table_total_articles VALUES(_article.ref_art, _article.designation, _article.id, _article.unite, _article.reference, 'TOTAUX', _article.total, _article.qte, _article.pr, (_article.taux / i), i+1, TRUE, FALSE);
	END LOOP;
	
	RETURN QUERY SELECT * FROM table_total_articles ORDER BY _is_total_, _is_footer_, _nom_, _code_, _rang_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION et_total_articles(bigint, bigint, date, date, character varying, character varying, character varying)
  OWNER TO postgres;


-- Function: com_recalcule_pr_periode(bigint, bigint, bigint, character varying, date, date)
-- DROP FUNCTION com_recalcule_pr_periode(bigint, bigint, bigint, character varying, date, date);
CREATE OR REPLACE FUNCTION com_recalcule_pr_periode(societe_ bigint, agence_ bigint, depot_ bigint, article_ character varying, debut_ date, fin_ date)
  RETURNS double precision AS
$BODY$
declare 
	line_ record;

	query_ character varying default 'SELECT m.* FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id INNER JOIN yvs_agences a ON d.agence = a.id WHERE m.date_doc BETWEEN '||QUOTE_LITERAL(debut_)||' AND '|| QUOTE_LITERAL(fin_);
	ids_ character varying default '''0''';
	id_ character varying default '0';
	type_ character varying default '';
	
	total_ double precision default 0;
	pr_ double precision default 0;
	last_pr_ double precision default 0;
	new_cout double precision default 0;
	stock_ double precision default 0;
	prix_arr_ numeric;

	last_article_ BIGINT DEFAULT 0;
begin 
	IF(COALESCE(societe_, 0) > 0) THEN 
		query_= query_ || ' AND a.societe = '||societe_;
	END IF;
	IF(COALESCE(agence_, 0) > 0) THEN 
		query_= query_ || ' AND a.id = '||agence_;
	END IF;
	IF(COALESCE(depot_, 0) > 0) THEN 
		query_= query_ || ' AND d.id = '||depot_;
	END IF;
	IF(COALESCE(article_, '') NOT IN ('', ' ', '0'))THEN
		FOR id_ IN SELECT art FROM regexp_split_to_table(article_,',') art WHERE CHAR_LENGTH(art) > 0
		LOOP
			ids_ = ids_ ||','||QUOTE_LITERAL(id_);
		END LOOP;
		query_= query_ || ' AND m.article::text IN ('||ids_||')';
	END IF;
	ALTER TABLE yvs_base_mouvement_stock DISABLE TRIGGER stock_maj_prix_mvt;
	FOR line_ IN EXECUTE query_ || ' ORDER BY m.conditionnement, m.date_doc ASC, m.mouvement'
	LOOP
		IF(last_article_ != line_.conditionnement)THEN
			stock_ = get_stock_reel(line_.article, 0, line_.depot, 0, 0, line_.date_doc - 1, line_.conditionnement, 0);
			last_pr_ = get_pr(line_.article, line_.depot, 0, line_.date_doc - 1, line_.conditionnement, line_.id);
			last_article_ = line_.conditionnement;
			pr_ = last_pr_;
		ELSE
			last_pr_ = pr_;
		END IF;
		
		IF(line_.table_externe = 'yvs_com_contenu_doc_stock' OR line_.table_externe = 'yvs_com_contenu_doc_stock_reception')THEN
			IF(line_.table_externe = 'yvs_com_contenu_doc_stock')THEN
				SELECT INTO type_ d.type_doc FROM yvs_com_doc_stocks d INNER JOIN yvs_com_contenu_doc_stock c ON c.doc_stock = d.id WHERE c.id = line_.id_externe;
			ELSE
				SELECT INTO type_ d.type_doc FROM yvs_com_doc_stocks d INNER JOIN yvs_com_contenu_doc_stock c ON c.doc_stock = d.id INNER JOIN yvs_com_contenu_doc_stock_reception r ON r.contenu = c.id WHERE r.id = line_.id_externe;
			END IF;
			ALTER TABLE yvs_com_contenu_doc_stock DISABLE TRIGGER update_;
			IF(type_ = 'FT' OR type_ = 'TR')THEN
				IF(line_.mouvement = 'E')THEN
					UPDATE yvs_com_contenu_doc_stock SET prix_entree = pr_ WHERE id = line_.id_externe;
				ELSE
					UPDATE yvs_com_contenu_doc_stock SET prix = pr_ WHERE id = line_.id_externe;
				END IF;
				UPDATE yvs_base_mouvement_stock SET cout_entree = pr_ WHERE id = line_.id;
				line_.cout_entree = pr_;
			ELSIF(line_.mouvement = 'S')THEN
				UPDATE yvs_com_contenu_doc_stock SET prix_entree = pr_, prix = pr_ WHERE id = line_.id_externe;
				UPDATE yvs_base_mouvement_stock SET cout_entree = pr_ WHERE id = line_.id;
				line_.cout_entree = pr_;
			END IF;
			ALTER TABLE yvs_com_contenu_doc_stock ENABLE TRIGGER update_;
		ELSIF(line_.table_externe = 'yvs_prod_of_suivi_flux')THEN
			UPDATE yvs_prod_of_suivi_flux SET cout = pr_ WHERE id = line_.id_externe;
			UPDATE yvs_base_mouvement_stock SET cout_entree = pr_ WHERE id = line_.id;
			line_.cout_entree = pr_;
		END IF;
		
		IF(line_.mouvement = 'E')THEN
			-- Retourne le nouveau cout moyen calculé
			IF(COALESCE(line_.quantite, 0) + stock_ != 0)THEN
				new_cout = COALESCE((((stock_ * COALESCE(last_pr_, 0)) + ((COALESCE(line_.quantite, 0) * COALESCE(line_.cout_entree, 0)))) / (COALESCE(line_.quantite, 0) + stock_)), 0);
				-- Arrondi les chiffres
				pr_ = round(new_cout::numeric, 3);
			ELSE
				pr_ = COALESCE(last_pr_, 0);
			END IF;
		END IF;
		IF(line_.mouvement = 'E')THEN
			stock_ = stock_ + COALESCE(line_.quantite, 0);
		ELSE
			stock_ = stock_ - COALESCE(line_.quantite, 0);
		END IF;
		UPDATE yvs_base_mouvement_stock SET cout_stock = pr_ WHERE id = line_.id;
	END LOOP;
	ALTER TABLE yvs_base_mouvement_stock ENABLE TRIGGER stock_maj_prix_mvt;
	RETURN pr_;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_recalcule_pr_periode(bigint, bigint, bigint, character varying, date, date)
  OWNER TO postgres;


-- Function: decoupage_interval_date(date, date, character varying, boolean)
-- DROP FUNCTION decoupage_interval_date(date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION decoupage_interval_date(IN date_debut_ date, IN date_fin_ date, IN periode_ character varying, IN view_date_ boolean)
  RETURNS TABLE(intitule character varying, date_debut date, date_fin date) AS
$BODY$
DECLARE

    date_ date;
    date_action_ date default date_debut_;

    intitule_ character varying;
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;

    pattern_ character varying default 'dd'; 

BEGIN    
-- 	DROP TABLE IF EXISTS table_decoupage_interval_date;
	CREATE TEMP TABLE IF NOT EXISTS table_decoupage_interval_date(_intitule character varying, _date_debut date, _date_fin date);
	DELETE FROM table_decoupage_interval_date;
	if(view_date_)then
		pattern_ = 'dd-MM-yyyy';
	else
		if(to_char(date_debut_ , 'MM') != to_char(date_fin_ , 'MM'))then
			pattern_ =  pattern_ || '-MM';
		end if;
		if(to_char(date_debut_ , 'yyyy') != to_char(date_fin_ , 'yyyy'))then
			pattern_ =  pattern_ || '-yyyy';
		end if;
	end if;
	if(coalesce(periode_, '') not in ('', ' '))then
		while(date_action_ <= date_fin_)
		loop
			if(periode_ = 'A')then
				date_ = (date_action_ + interval '1 year' - interval '1 day');
				if(date_ > date_fin_)then
					date_ = date_ - (date_ - date_fin_);
				end if;		
				intitule_ = (select extract(year from date_action_));	
			elsif(periode_ = 'T')then
				date_ = (date_action_ + interval '3 month' - interval '1 day');
				if(date_ > date_fin_)then
					date_ = date_ - (date_ - date_fin_);
				end if;		
				jour_0 = (select extract(month from date_action_));
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
				date_ = (date_action_ + interval '1 month' - interval '1 day');
				if(date_ > date_fin_)then
					date_ = date_ - (date_ - date_fin_);
				end if;		
				jour_ = (select extract(month from date_action_));
				if(char_length(jour_)<2)then
					jour_ = '0'||jour_;
				end if;			
				annee_ = (select extract(year from date_action_));	
				intitule_ = jour_ ||'-'|| annee_;
			elsif(periode_ = 'S')then
				date_ = (date_action_ + interval '1 week' - interval '1 day');	
				if(date_ > date_fin_)then
					date_ = date_ - (date_ - date_fin_);
				end if;
				
				jour_0 = (select extract(day from date_action_));
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
				mois_0 = (select extract(month from date_action_));
				if(char_length(mois_0)<2)then
					mois_0 = '0'||mois_0;
				end if;
				if(mois_ != mois_0)then
					mois_ = '('|| mois_0 || '/'|| mois_ ||')';
				end if;
				intitule_ = jour_ || mois_;
			else
				date_ = (date_action_ + interval '0 day');
				if(date_ > date_fin_)then
					date_ = date_ - (date_ - date_fin_);
				end if;	
				intitule_ = to_char(date_action_ , pattern_);
			end if;
			
			INSERT INTO table_decoupage_interval_date VALUES(intitule_, date_action_, date_);
		
			if(periode_ = 'A')then
				date_action_ = date_action_ + interval '1 year';
			elsif(periode_ = 'T')then
				date_action_ = date_action_ + interval '3 month';
			elsif(periode_ = 'M')then
				date_action_ = date_action_ + interval '1 month';
			elsif(periode_ = 'S')then
				date_action_ = date_action_ + interval '1 week';
			else
				date_action_ = date_action_ + interval '1 day';
			end if;
		end loop;
	else
		intitule_ = to_char(date_action_ , 'dd-MM-yyyy');
		INSERT INTO table_decoupage_interval_date VALUES(intitule_, date_debut_, date_fin_);
	end if;
	return QUERY select * from table_decoupage_interval_date;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION decoupage_interval_date(date, date, character varying, boolean)
  OWNER TO postgres;
