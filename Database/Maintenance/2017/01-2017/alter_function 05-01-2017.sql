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
	select into doc_ id, type_doc, statut, entete_doc, mouv_stock, depot_livrer, tranche_livrer from yvs_com_doc_ventes where id = NEW.doc_vente;
	select into dep_ h.depot as depot, h.tranche as tranche, e.date_entete as date_doc from yvs_com_entete_doc_vente e inner join yvs_com_creneau_horaire_users c
		on e.creneau = c.id inner join yvs_com_creneau_depot h on c.creneau_depot = h.id
		where e.id = doc_.entete_doc;
	if(doc_.type_doc = 'FRV' or doc_.type_doc = 'BLV') then
		--Insertion mouvement stock
		if(doc_.statut = 'V')then
			if(doc_.depot_livrer is not null)then
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot_livrer and article = NEW.article and mouvement = 'S';
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id = mouv_;
						if(doc_.type_doc = 'BLV')then
							result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
						else
							result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', dep_.date_doc));
						end if;
					else
						update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.prix where id = mouv_;
					end if;
				else
					if(doc_.type_doc = 'BLV')then
						result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'S', dep_.date_doc));
					else
						result = (select valorisation_stock(NEW.article, doc_.depot_livrer, doc_.tranche_livrer, NEW.quantite, 0, ''||TG_TABLE_NAME||'', NEW.id, 'E', dep_.date_doc));
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

  
  
  -- Function: delete_contenu_doc_stock()

-- DROP FUNCTION delete_contenu_doc_stock();

CREATE OR REPLACE FUNCTION delete_contenu_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	mouv_ bigint;
BEGIN
	select into doc_ id, type_doc, statut, source, destination from yvs_com_doc_stocks where id = OLD.doc_stock;
	if(doc_.statut = 'V')then
		if(doc_.type_doc = 'FT') then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = OLD.article and mouvement = 'E';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = OLD.article and mouvement = 'S';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
		elsif(doc_.type_doc = 'SS') then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = OLD.article and mouvement = 'S';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
		elsif(doc_.type_doc = 'ES') then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.destination and article = OLD.article and mouvement = 'E';
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
		elsif(doc_.type_doc = 'INV') then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.source and article = OLD.article;
			if(mouv_ is not null)then
				delete from yvs_base_mouvement_stock where id = mouv_;
			end if;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_contenu_doc_stock()
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
			if(doc_.destination is not null)then
				result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			end if;
			if(doc_.source is not null)then
				result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'SS') then
			if(doc_.source is not null)then
				result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'ES') then
			if(doc_.destination is not null)then
				result = (select valorisation_stock(NEW.article, doc_.destination, trancheD_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
			end if;
		elsif(doc_.type_doc = 'INV') then
			if(doc_.source is not null)then
				if(NEW.quantite>0)then
					result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'S', doc_.date_doc));
				elsif(NEW.quantite<0)then
					result = (select valorisation_stock(NEW.article, doc_.source, trancheS_, NEW.quantite, NEW.prix, ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				end if;
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
  
  
  -- Function: add_presence(bigint, bigint, date, date)

-- DROP FUNCTION add_presence(bigint, bigint, date, date);

CREATE OR REPLACE FUNCTION insert_droit_com()
  RETURNS boolean AS
$BODY$   
DECLARE 
	module_ bigint default 0;
	page_ bigint default 0;
	ressource_  bigint default 0;
BEGIN
	select into module_ id from yvs_module where reference = 'com_';
	if(module_ is not null)then
		select into page_ id from yvs_page_module where reference = 'gescom_fv';
		if(page_ is not null)then
			select into ressource_ id from yvs_ressources_page where reference = 'fv_save_doc';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_save_doc', 'Enregistrer une facture', 'Enregistrer une facture', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_valide_doc';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_valide_doc', 'Valider une facture', 'Valider une facture', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_cancel_doc_valid';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_cancel_doc_valid', 'Annuler une facture', 'Annuler une facture (quand la facture est sous etat ''validé'')', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_delete_doc';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_delete_doc', 'Supprimer une facture', 'Supprimer une facture', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_view_all_doc';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_view_all_doc', 'Voir toutes les factures de la societe', 'Voir toutes les factures de la societe', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_view_only_doc_agence';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_view_only_doc_agence', 'Voir toutes les factures de l''agence', 'Voir toutes les factures de l''agence', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_view_only_doc_pv';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_view_only_doc_pv', 'Voir toutes les factures d''un point de vente', 'Voir toutes les factures d''un point de vente', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_update_header';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_update_header', 'Modifier la date du header', 'Modifier la date du header', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_save_in_past';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_save_in_past', 'Enregistrer une facture au dela de la marge de retard de a date autorisée', 'Enregistrer une facture au dela de la marge de retard de a date autorisée', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_apply_rem_all';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_apply_rem_all', 'Appliquer une remise sur la facture', 'Appliquer une remise sur la facture', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_apply_rabais';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_apply_rabais', 'Appliquer un rabais sur une ligne de vente', 'Appliquer un rabais sur une ligne de vente', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_apply_remise';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_apply_remise', 'Appliquer une remise sur une ligne de vente', 'Appliquer une remise sur une ligne de vente', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_create_client';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_create_client', 'Créer un client', 'Créer un client', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_create_reglement';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_create_reglement', 'Créer des pieces reglements', 'Créer des pieces reglements', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_clean';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_clean', 'Nettoyer les facture sans contenus', 'Nettoyer les facture sans contenus', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_livrer';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_livrer', 'Livrer une facture', 'Livrer une facture', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_clean_header';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_clean_header', 'Nettoyer Les header sans facture', 'Nettoyer Les header sans facture', page_);
			end if;
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_droit_com()
  OWNER TO postgres;  
  
  
CREATE OR REPLACE FUNCTION reevaluer_total_presence(id_ bigint)
  RETURNS boolean AS
$BODY$   
DECLARE 
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	somme_ DOUBLE PRECISION default 0;
	pause_ DOUBLE PRECISION default 0;
	marge_ DOUBLE PRECISION default 0;
	requis_ DOUBLE PRECISION default 0;
	pointages_ RECORD;
	record_ RECORD;
BEGIN
	select into record_ * from yvs_grh_presence where id = id_;
	if(record_.valider = false)then
		if(record_.marge_approuve is not null)then
			marge_ = ((select extract(hour from record_.marge_approuve)) + ((select extract(minute from record_.marge_approuve))/60));
		end if;
		if(marge_ is null)then
			marge_ = 0;
		end if;
		
		if(record_.duree_pause is not null)then
			pause_ = ((select extract(hour from record_.duree_pause)) + ((select extract(minute from record_.duree_pause))/60));
		end if;
		if(pause_ is null)then
			pause_ = 0;
		end if;
		
		HS_ = ((select extract(hour from record_.heure_debut)) + ((select extract(minute from record_.heure_debut))/60));
		HE_ = ((select extract(hour from record_.heure_fin)) + ((select extract(minute from record_.heure_fin))/60));
		requis_ = HS_ - HE_;
		if(requis_ is null)then
			requis_ = 0;
		end if;
		if(requis_ < 0)then
			requis_ = requis_ + 24;
		end if;
		requis_ = requis_ - pause_;
		
		update yvs_grh_presence set total_presence = 0, total_heure_sup = 0, total_heure_compensation = 0 where id = record_.id;
		for pointages_ in select * from yvs_grh_pointage where presence = record_.id and valider = true and actif = true and heure_entree is not null and heure_sortie is not null
		loop
			-- recuperation de l'interval heure fin - heure debut
			HS_ = ((select extract(hour from pointages_.heure_sortie)) + ((select extract(minute from pointages_.heure_sortie))/60));
			HE_ = ((select extract(hour from pointages_.heure_entree)) + ((select extract(minute from pointages_.heure_entree))/60));
			somme_ = HS_ - HE_;
			if(somme_ is null)then
				somme_ = 0;
			end if;
			if(somme_ < 0)then
				somme_ = somme_ + 24;
			end if;
			
			somme_ = somme_ + marge_;
			if(somme_ > requis_)then
				somme_ = requis_;
			end if;
			
			if((pointages_.compensation_heure is null or pointages_.compensation_heure = false) and (pointages_.heure_supplementaire is null or pointages_.heure_supplementaire = false)) then			
				update yvs_grh_presence set total_presence = total_presence + somme_ where id = record_.id;
			else
				if(pointages_.heure_supplementaire = true) then
					update yvs_grh_presence set total_heure_sup = total_heure_sup + somme_ where id = record_.id;
				end if;
				if(pointages_.compensation_heure = true) then
					update yvs_grh_presence set total_heure_compensation = total_heure_compensation + somme_ where id = record_.id;
				end if;	
			end if;		
		end loop;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION reevaluer_total_presence(bigint)
  OWNER TO postgres;
  
  
CREATE OR REPLACE FUNCTION update_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE    
    
BEGIN
	PERFORM reevaluer_total_presence(OLD.presence);
	--recuperation de l'heure de pointage
	if (NEW.heure_sortie is null) then
		NEW.heure_pointage = NEW.heure_entree;
	else
		NEW.heure_pointage = NEW.heure_sortie;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_pointage()
  OWNER TO postgres;
  
  
  CREATE OR REPLACE FUNCTION insert_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE     
    
BEGIN
	PERFORM reevaluer_total_presence(NEW.presence);
	if (NEW.heure_sortie is null) then
		NEW.heure_pointage = NEW.heure_entree;
	else
		NEW.heure_pointage = NEW.heure_sortie;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_pointage()
  OWNER TO postgres;
  
DROP TRIGGER insert_pointage ON yvs_grh_pointage;
CREATE TRIGGER insert_pointage
  AFTER INSERT
  ON yvs_grh_pointage
  FOR EACH ROW
  EXECUTE PROCEDURE insert_pointage();
  
DROP TRIGGER delete_pointage ON yvs_grh_pointage;
CREATE TRIGGER delete_pointage
  AFTER DELETE
  ON yvs_grh_pointage
  FOR EACH ROW
  EXECUTE PROCEDURE delete_pointage();  
  
  
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
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, dv.num_doc, dv.fournisseur FROM yvs_compta_caisse_piece_achat pv 
			INNER JOIN yvs_com_doc_achats dv ON dv.id=pv.achat INNER JOIN yvs_agences ag ON ag.id=dv.agence WHERE pv.id=NEW.id; 
								    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		IF(NEW.fournisseur IS NOT NULL) THEN
			id_tiers_=NEW.fournisseur;
		ELSE
			id_tiers_=line_.fournisseur;
		END IF;
		--récupère le code tièrs de ce fournisseurs
		SELECT INTO id_tiers_ t.tiers FROM yvs_base_fournisseur t WHERE t.id=id_tiers_;
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, 'D');
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, 
				date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', societe=line_.societe
				WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe, mouvement)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'D');
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
  
  
  
CREATE TRIGGER compta_action_on_piece_achat
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_caisse_piece_achat
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_achat();
  
  -- Function: compta_action_on_piece_caisse_divers()

-- DROP FUNCTION compta_action_on_piece_caisse_divers();

CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  dv.societe, dv.num_piece as num_doc, dv.tiers, dv.mouvement FROM yvs_compta_caisse_piece_divers pv 
			INNER JOIN yvs_compta_caisse_doc_divers dv ON dv.id=pv.doc_divers WHERE pv.id=NEW.id; 
								    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement)
			VALUES (NEW.num_piece, NEW.id, NEW.note, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_piece,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, line_.mouvement);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.num_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, 
				date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_piece, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement=line_.mouvement, societe=line_.societe
				WHERE table_externe='DOC_DIVERS' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe, mouvement)
				VALUES (NEW.num_piece, NEW.id, NEW.note, 'DOC_DIVERS', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_piece,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,line_.mouvement);
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

  
CREATE TRIGGER compta_action_on_piece_divers
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_compta_caisse_piece_divers
  FOR EACH ROW
  EXECUTE PROCEDURE compta_action_on_piece_caisse_divers();
