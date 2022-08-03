
select alter_action_colonne_key('yvs_com_fiche_approvisionnement', 'yvs_com_article_approvisionnement', true, true);

-- Function: insert_contenu_doc_achat()

-- DROP FUNCTION insert_contenu_doc_achat();

CREATE OR REPLACE FUNCTION insert_contenu_doc_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	doc_ record;
	arts_ record;
	result boolean default false;
BEGIN
	select into arts_ a.id from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = NEW.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, mouv_stock, livrer from yvs_com_doc_achats where id = NEW.doc_achat;
	if(doc_.type_doc = 'FA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu, (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_contenu_doc_achat()
  OWNER TO postgres;
  
  
  -- Function: get_taxe_total_achat(bigint)

-- DROP FUNCTION get_taxe_total_achat(bigint);

CREATE OR REPLACE FUNCTION get_taxe_total_achat(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	contenu_ record;
	taxes_ record;
	remise_ double precision default 0;
	somme_ double precision default 0;
	taux_ double precision default 0;

BEGIN
	select into contenu_ a.article, c.doc_achat, c.quantite_attendu as qte, c.pua_attendu as pua, c.remise_attendu as rem , d.categorie_comptable
		from yvs_com_contenu_doc_achat c inner join yvs_com_article a on c.article = a.id inner join yvs_com_doc_achats d
		on d.id = c.doc_achat where c.id = id_;

	somme_ =  contenu_.qte * contenu_.pua;
	if(somme_ is null)then
		somme_ = 0;
	end if;
	remise_ = somme_ * (contenu_.rem / 100);
	if(remise_ is null)then
		remise_ = 0;
	end if;
	
	for taxes_ in select t.taux, ta.app_remise from yvs_base_article_categorie_comptable_taxe ta 
		inner join yvs_base_taxes t on ta.taxe = t.id inner join yvs_base_article_categorie_comptable c on ta.article_categorie = c.id
		where ta.actif = true and c.categorie = contenu_.categorie_comptable and c.article = contenu_.article
	loop
		taux_ = taux_ + (somme_ * (taxes_.taux / 100));
		if(taxes_.app_remise = true)then
			taux_ = taux_ + (remise_ * (taxes_.taux / 100));
		end if;
	end loop;
	
	if(taux_ is null)then
		taux_ = 0;
	end if;
    return taux_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_taxe_total_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_taxe_total_achat(bigint) IS 'retourne le montant total des taxes d'' article';

  
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
	select into arts_ a.id, a.methode_val from yvs_com_article c inner join yvs_articles a on c.article = a.id where c.id = OLD.article;
	select into doc_ id, type_doc, date_doc, depot_reception as depot, tranche, statut, mouv_stock , livrer from yvs_com_doc_achats where id = OLD.doc_achat;
	if(doc_.type_doc = 'FA' or doc_.type_doc = 'BLA') then
		--Insertion mouvement stock
		if(doc_.statut = 'VALIDE' and doc_.mouv_stock = true and doc_.livrer = true)then
			select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME and depot = doc_.depot and article = arts_.id and mouvement = 'E';
			if(mouv_ is not null)then
				if(arts_.methode_val = 'FIFO')then
					delete from yvs_base_mouvement_stock where id = mouv_;
					result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
				else
					update yvs_base_mouvement_stock set quantite = NEW.quantite_attendu, cout_entree = NEW.pua_attendu where id = mouv_;
				end if;
			else
				result = (select valorisation_stock(arts_.id, doc_.depot, doc_.tranche, NEW.quantite_attendu,  (select get_cout_achat_contenu(NEW.id)), ''||TG_TABLE_NAME||'', NEW.id, 'E', doc_.date_doc));
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
  

  
  -- Function: delete_mensualite_facture_achat()

-- DROP FUNCTION delete_mensualite_facture_achat();

CREATE OR REPLACE FUNCTION delete_mensualite_facture_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	montant_ double precision default 0;
	total_ double precision default 0;
	somme_ double precision default 0;
	etat_ varchar default 'EDITABLE';
	solde_ boolean default false;
	id_reg bigint;
BEGIN
	montant_ = (select get_montant_ttc_doc_achat(OLD.facture));
	if(montant_ is null)then
		montant_ = 0;
	end if;
	for id_reg in select id from yvs_com_mensualite_facture_achat where facture = OLD.facture
	loop
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = id_reg and table_externe = 'yvs_com_mensualite_facture_achat' and (statut = 'VALIDE' or statut = 'REGLE');
		if(total_ is null)then
			total_ = 0;
		end if;
		somme_ = somme_ + total_;
	end loop;	
	if(somme_ is null)then
		somme_ = 0;
	end if;
	if(somme_ >= montant_)then
		etat_ = 'REGLE';
		solde_  = true;
	elsif (somme_ > 0)then
		etat_ = 'ENCOURS';
	end if;
	update yvs_com_doc_achats set statut = etat_, solde = solde_ where id = OLD.facture AND statut != 'EDITABLE' AND statut != 'REFUSE' and cloturer = false;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_mensualite_facture_achat()
  OWNER TO postgres;

  
  
  -- Function: insert_mensualite_facture_achat()

-- DROP FUNCTION insert_mensualite_facture_achat();

CREATE OR REPLACE FUNCTION insert_mensualite_facture_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	montant_ double precision default 0;
	total_ double precision default 0;
	somme_ double precision default 0;
	etat_ varchar default 'EDITABLE';
	solde_ boolean default false;
	id_reg bigint;
BEGIN
	montant_ = (select get_montant_ttc_doc_achat(NEW.facture));
	if(montant_ is null)then
		montant_ = 0;
	end if;
	for id_reg in select id from yvs_com_mensualite_facture_achat where facture = NEW.facture
	loop
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = id_reg and table_externe = 'yvs_com_mensualite_facture_achat' and (statut = 'VALIDE' or statut = 'REGLE');
		if(total_ is null)then
			total_ = 0;
		end if;
		somme_ = somme_ + total_;
	end loop;	
	if(somme_ is null)then
		somme_ = 0;
	end if;
	if(somme_ >= montant_)then
		etat_ = 'REGLE';
		solde_  = true;
	elsif (somme_ > 0)then
		etat_ = 'ENCOURS';
	end if;
	update yvs_com_doc_achats set statut = etat_, solde = solde_ where id = NEW.facture AND statut != 'EDITABLE' AND statut != 'REFUSE' and cloturer = false;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mensualite_facture_achat()
  OWNER TO postgres;

  
  
  -- Function: update_mensualite_facture_achat()

-- DROP FUNCTION update_mensualite_facture_achat();

CREATE OR REPLACE FUNCTION update_mensualite_facture_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	montant_ double precision default 0;
	total_ double precision default 0;
	somme_ double precision default 0;
	etat_ varchar default 'EDITABLE';
	solde_ boolean default false;
	id_reg bigint;
BEGIN
	montant_ = (select get_montant_ttc_doc_achat(OLD.facture));
	if(montant_ is null)then
		montant_ = 0;
	end if;
	for id_reg in select id from yvs_com_mensualite_facture_achat where facture = OLD.facture
	loop
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = id_reg and table_externe = 'yvs_com_mensualite_facture_achat' and (statut = 'VALIDE' or statut = 'REGLE');
		if(total_ is null)then
			total_ = 0;
			NEW.etat = 'EDITABLE';
		end if;
		if(total_ > 0 and NEW.montant > total_)then
			NEW.etat = 'ENCOURS';
		end if;
		somme_ = somme_ + total_;
	end loop;	
	if(somme_ is null)then
		somme_ = 0;
	end if;
	if(somme_ >= montant_)then
		etat_ = 'REGLE';
		solde_  = true;
	elsif (somme_ > 0)then
		etat_ = 'ENCOURS';
	end if;
	update yvs_com_doc_achats set statut = etat_, solde = solde_ where id = OLD.facture AND statut != 'EDITABLE' AND statut != 'REFUSE' and cloturer = false;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_mensualite_facture_achat()
  OWNER TO postgres;



-- Function: delete_mensualite_facture_vente()

-- DROP FUNCTION delete_mensualite_facture_vente();

CREATE OR REPLACE FUNCTION delete_mensualite_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	montant_ double precision default 0;
	total_ double precision default 0;
	somme_ double precision default 0;
	etat_ varchar default 'EDITABLE';
	solde_ boolean default false;
	id_reg bigint;
BEGIN
	montant_ = (select get_montant_ttc_doc_vente(OLD.facture));
	if(montant_ is null)then
		montant_ = 0;
	end if;
	for id_reg in select id from yvs_com_mensualite_facture_vente where facture = OLD.facture
	loop
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = id_reg and table_externe = 'yvs_com_mensualite_facture_vente' and (statut = 'VALIDE' or statut = 'REGLE');
		if(total_ is null)then
			total_ = 0;
		end if;
		somme_ = somme_ + total_;
	end loop;	
	if(somme_ is null)then
		somme_ = 0;
	end if;
	if(somme_ >= montant_)then
		etat_ = 'REGLE';
		solde_  = true;
	elsif (somme_ > 0)then
		etat_ = 'ENCOURS';
	end if;
	update yvs_com_doc_ventes set statut = etat_, solde = solde_ where id = OLD.facture AND statut != 'EDITABLE' AND statut != 'REFUSE' and cloturer = false;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_mensualite_facture_vente()
  OWNER TO postgres;

  
  -- Function: insert_mensualite_facture_vente()

-- DROP FUNCTION insert_mensualite_facture_vente();

CREATE OR REPLACE FUNCTION insert_mensualite_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	montant_ double precision default 0;
	total_ double precision default 0;
	somme_ double precision default 0;
	etat_ varchar default 'EDITABLE';
	solde_ boolean default false;
	id_reg bigint;
BEGIN
	montant_ = (select get_montant_ttc_doc_vente(NEW.facture));
	if(montant_ is null)then
		montant_ = 0;
	end if;
	for id_reg in select id from yvs_com_mensualite_facture_vente where facture = NEW.facture
	loop
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = id_reg and table_externe = 'yvs_com_mensualite_facture_vente' and (statut = 'VALIDE' or statut = 'REGLE');
		if(total_ is null)then
			total_ = 0;
		end if;
		somme_ = somme_ + total_;
	end loop;	
	if(somme_ is null)then
		somme_ = 0;
	end if;
	if(somme_ >= montant_)then
		etat_ = 'REGLE';
		solde_  = true;
	elsif (somme_ > 0)then
		etat_ = 'ENCOURS';
	end if;
	update yvs_com_doc_ventes set statut = etat_, solde = solde_ where id = NEW.facture AND statut != 'EDITABLE' AND statut != 'REFUSE' and cloturer = false;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mensualite_facture_vente()
  OWNER TO postgres;

  
  
  -- Function: update_mensualite_facture_vente()

-- DROP FUNCTION update_mensualite_facture_vente();

CREATE OR REPLACE FUNCTION update_mensualite_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	montant_ double precision default 0;
	total_ double precision default 0;
	somme_ double precision default 0;
	etat_ varchar default 'EDITABLE';
	solde_ boolean default false;
	id_reg bigint;
BEGIN
	montant_ = (select get_montant_ttc_doc_vente(OLD.facture));
	if(montant_ is null)then
		montant_ = 0;
	end if;
	for id_reg in select id from yvs_com_mensualite_facture_vente where facture = OLD.facture
	loop
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = id_reg and table_externe = 'yvs_com_mensualite_facture_vente' and (statut = 'VALIDE' or statut = 'REGLE');
		if(total_ is null)then
			total_ = 0;
			NEW.etat = 'EDITABLE';
		end if;
		if(total_ > 0 and NEW.montant > total_)then
			NEW.etat = 'ENCOURS';
		end if;
		somme_ = somme_ + total_;
	end loop;	
	if(somme_ is null)then
		somme_ = 0;
	end if;
	if(somme_ >= montant_)then
		etat_ = 'REGLE';
		solde_  = true;
	elsif (somme_ > 0)then
		etat_ = 'ENCOURS';
	end if;
	update yvs_com_doc_ventes set statut = etat_, solde = solde_ where id = OLD.facture AND statut != 'EDITABLE' AND statut != 'REFUSE' and cloturer = false;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_mensualite_facture_vente()
  OWNER TO postgres;
  
  
  -- Function: piece_tresorerie_insert()

-- DROP FUNCTION piece_tresorerie_insert();

CREATE OR REPLACE FUNCTION piece_tresorerie_insert()
  RETURNS trigger AS
$BODY$    
DECLARE
	mensualite_ record;
	montant_ double precision default 0;
	total_ double precision default 0;
	etat_ varchar default 'EDITABLE';
BEGIN
	if(NEW.table_externe = 'yvs_com_mensualite_facture_achat')then
		select into montant_ montant from yvs_com_mensualite_facture_achat where id = NEW.id_externe;
		
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = NEW.id_externe and table_externe = NEW.table_externe and (statut = 'VALIDE' or statut = 'REGLE');
		if(montant_ is null)then
			montant_ = 0;
		end if;
		if(total_ is null)then
			total_ = 0;
		end if;
		if(total_ >= montant_)then
			etat_ = 'REGLE';
		elsif (total_ > 0)then
			etat_ = 'ENCOURS';
		end if;
		update yvs_com_mensualite_facture_achat set etat = etat_ where id = NEW.id_externe;
	elsif(NEW.table_externe = 'yvs_com_mensualite_facture_vente')then
		select into montant_ montant from yvs_com_mensualite_facture_vente where id = NEW.id_externe;
		
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = NEW.id_externe and table_externe = NEW.table_externe and (statut = 'VALIDE' or statut = 'REGLE');
		if(montant_ is null)then
			montant_ = 0;
		end if;
		if(total_ is null)then
			total_ = 0;
		end if;
		if(total_ >= montant_)then
			etat_ = 'REGLE';
		elsif (total_ > 0)then
			etat_ = 'ENCOURS';
		end if;
		update yvs_com_mensualite_facture_vente set etat = etat_ where id = NEW.id_externe;
	elsif(NEW.table_externe = 'yvs_base_mensualite_doc_divers')then
		select into montant_ montant from yvs_base_mensualite_doc_divers where id = NEW.id_externe;
		
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = NEW.id_externe and table_externe = NEW.table_externe and (statut = 'VALIDE' or statut = 'REGLE');
		if(montant_ is null)then
			montant_ = 0;
		end if;
		if(total_ is null)then
			total_ = 0;
		end if;
		if(total_ >= montant_)then
			etat_ = 'REGLE';
		elsif (total_ > 0)then
			etat_ = 'ENCOURS';
		end if;
		update yvs_base_mensualite_doc_divers set etat = etat_ where id = NEW.id_externe;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION piece_tresorerie_insert()
  OWNER TO postgres;

  
  
  -- Function: piece_tresorerie_delete()

-- DROP FUNCTION piece_tresorerie_delete();

CREATE OR REPLACE FUNCTION piece_tresorerie_delete()
  RETURNS trigger AS
$BODY$    
DECLARE
	mensualite_ record;
	montant_ double precision default 0;
	total_ double precision default 0;
	etat_ varchar default 'EDITABLE';
BEGIN
	if(OLD.table_externe = 'yvs_com_mensualite_facture_achat')then
		select into montant_ montant from yvs_com_mensualite_facture_achat where id = OLD.id_externe;
		
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = OLD.id_externe and table_externe = OLD.table_externe and (statut = 'VALIDE' or statut = 'REGLE');
		if(montant_ is null)then
			montant_ = 0;
		end if;
		if(total_ is null)then
			total_ = 0;
		end if;
		if(total_ >= montant_)then
			etat_ = 'REGLE';
		elsif (total_ > 0)then
			etat_ = 'ENCOURS';
		end if;
		update yvs_com_mensualite_facture_achat set etat = etat_ where id = OLD.id_externe;
	elsif(OLD.table_externe = 'yvs_com_mensualite_facture_vente')then
		select into montant_ montant from yvs_com_mensualite_facture_vente where id = OLD.id_externe;
		
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = OLD.id_externe and table_externe = OLD.table_externe and (statut = 'VALIDE' or statut = 'REGLE');
		if(montant_ is null)then
			montant_ = 0;
		end if;
		if(total_ is null)then
			total_ = 0;
		end if;
		if(total_ >= montant_)then
			etat_ = 'REGLE';
		elsif (total_ > 0)then
			etat_ = 'ENCOURS';
		end if;
		update yvs_com_mensualite_facture_vente set etat = etat_ where id = OLD.id_externe;
	elsif(OLD.table_externe = 'yvs_base_mensualite_doc_divers')then
		select into montant_ montant from yvs_base_mensualite_doc_divers where id = OLD.id_externe;
		
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = OLD.id_externe and table_externe = OLD.table_externe and (statut = 'VALIDE' or statut = 'REGLE');
		if(montant_ is null)then
			montant_ = 0;
		end if;
		if(total_ is null)then
			total_ = 0;
		end if;
		if(total_ >= montant_)then
			etat_ = 'REGLE';
		elsif (total_ > 0)then
			etat_ = 'ENCOURS';
		end if;
		update yvs_base_mensualite_doc_divers set etat = etat_ where id = OLD.id_externe;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION piece_tresorerie_delete()
  OWNER TO postgres;


 -- Function: piece_tresorerie_update()

-- DROP FUNCTION piece_tresorerie_update();

CREATE OR REPLACE FUNCTION piece_tresorerie_update()
  RETURNS trigger AS
$BODY$    
DECLARE
	mensualite_ record;
	montant_ double precision default 0;
	total_ double precision default 0;
	etat_ varchar default 'EDITABLE';
BEGIN
	-- RAISE NOTICE ' i %', OLD.table_externe;
	if(OLD.table_externe = 'yvs_com_mensualite_facture_achat')then
		select into montant_ montant from yvs_com_mensualite_facture_achat where id = OLD.id_externe;
		
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = OLD.id_externe and table_externe = 'yvs_com_mensualite_facture_achat' and (statut = 'VALIDE' or statut = 'REGLE');
		if(montant_ is null)then
			montant_ = 0;
		end if;
		if(total_ is null)then
			total_ = 0;
		end if;
		if(total_ >= montant_)then
			etat_ = 'REGLE';
		elsif (total_ > 0)then
			etat_ = 'ENCOURS';
		end if;
		update yvs_com_mensualite_facture_achat set etat = etat_ where id = OLD.id_externe;
	elsif(OLD.table_externe = 'yvs_com_mensualite_facture_vente')then
		select into montant_ montant from yvs_com_mensualite_facture_vente where id = OLD.id_externe;
		
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = OLD.id_externe and table_externe = 'yvs_com_mensualite_facture_vente' and (statut = 'VALIDE' or statut = 'REGLE');
		if(montant_ is null)then
			montant_ = 0;
		end if;
		if(total_ is null)then
			total_ = 0;
		end if;
		if(total_ >= montant_)then
			etat_ = 'REGLE';
		elsif (total_ > 0)then
			etat_ = 'ENCOURS';
		end if;
		update yvs_com_mensualite_facture_vente set etat = etat_ where id = OLD.id_externe;
	elsif(OLD.table_externe = 'yvs_base_mensualite_doc_divers')then
		select into montant_ montant from yvs_base_mensualite_doc_divers where id = OLD.id_externe;
		
		select into total_ sum(montant) from yvs_base_piece_tresorerie where id_externe = OLD.id_externe and table_externe = 'yvs_base_mensualite_doc_divers' and (statut = 'VALIDE' or statut = 'REGLE');
		if(montant_ is null)then
			montant_ = 0;
		end if;
		if(total_ is null)then
			total_ = 0;
		end if;
		if(total_ >= montant_)then
			etat_ = 'REGLE';
		elsif (total_ > 0)then
			etat_ = 'ENCOURS';
		end if;
		update yvs_base_mensualite_doc_divers set etat = etat_ where id = OLD.id_externe;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION piece_tresorerie_update()
  OWNER TO postgres;

  
  
  -- Function: delete_pointage()

-- DROP FUNCTION delete_pointage();

CREATE OR REPLACE FUNCTION delete_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE 
	HS_ DOUBLE PRECISION default 0;
	HE_ DOUBLE PRECISION default 0;
	OLD_somme_ DOUBLE PRECISION default 0;
    
BEGIN
	if (OLD.valider = true and OLD.actif = true and OLD.heure_sortie is not null) then
		HS_ = ((select extract(hour from OLD.heure_sortie)) + ((select extract(minute from OLD.heure_sortie))/60));
		if(HS_ is null)then
			HS_ = 0;
		end if;
		if(OLD.heure_entree is not null)then
			HE_ = ((select extract(hour from OLD.heure_entree)) + ((select extract(minute from OLD.heure_entree))/60));
		end if;
		if(HE_ is null)then
			HE_ = 0;
		end if;
		OLD_somme_ = HS_ - HE_;
		if(OLD_somme_<0)then
			OLD_somme_ = OLD_somme_ + 24;
		end if;
		if((OLD.compensation_heure is null OR OLD.compensation_heure = false) and (OLD.heure_supplementaire is null OR OLD.heure_supplementaire = false))then
			update yvs_grh_presence set total_presence = total_presence - OLD_somme_ where id = OLD.presence;
		else
			IF(OLD.heure_supplementaire = true) THEN
				update yvs_grh_presence set total_heure_sup = total_heure_sup - OLD_somme_ where id = OLD.presence;
			END IF;
			IF(OLD.compensation_heure = true) THEN
				update yvs_grh_presence set total_heure_compensation = total_heure_compensation - OLD_somme_ where id = OLD.presence;
			END IF;
		end if;
	end if;
	return OLD ;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_pointage()
  OWNER TO postgres;

  
  
  -- Function: update_pointage()

-- DROP FUNCTION update_pointage();

CREATE OR REPLACE FUNCTION update_pointage()
  RETURNS trigger AS
$BODY$   
    DECLARE
	HS_ DOUBLE PRECISION default 0; --heure sortie
	HE_ DOUBLE PRECISION default 0; --heure entrée
	NEW_somme_ DOUBLE PRECISION default 0;
	OLD_somme_ DOUBLE PRECISION default 0;
    
BEGIN
	-- recuperation de l'ancien interval heure fin - heure debut
	if(OLD.heure_sortie is not null and OLD.heure_entree is not null)then
		HS_ = ((select extract(hour from OLD.heure_sortie)) + ((select extract(minute from OLD.heure_sortie))/60));
		HE_ = ((select extract(hour from OLD.heure_entree)) + ((select extract(minute from OLD.heure_entree))/60));
		OLD_somme_ = HS_ - HE_;
		if(OLD_somme_ < 0)then
			OLD_somme_ = OLD_somme_ + 24;
		end if;
	end if;
	if(OLD_somme_ is null)then
		OLD_somme_ = 0;
	end if;
	
	-- recuperation du nouveau interval heure fin - heure debut
	if(NEW.heure_sortie is not null and NEW.heure_entree is not null)then
		HS_ = ((select extract(hour from NEW.heure_sortie)) + ((select extract(minute from NEW.heure_sortie))/60));
		HE_ = ((select extract(hour from NEW.heure_entree)) + ((select extract(minute from NEW.heure_entree))/60));
		NEW_somme_ = HS_ - HE_;
		if(NEW_somme_ < 0)then
			NEW_somme_ = NEW_somme_ + 24;
		end if;
	end if;
	if(NEW_somme_ is null)then
		NEW_somme_ = 0;
	end if;

	-- Verification si l'on modifie 'heure_sortie' ou 'heure_entree'
	if(NEW.heure_sortie != OLD.heure_sortie or NEW.heure_entree != OLD.heure_entree)then -- Si c'est la cas
		-- Verification si l'on modifie 'valider' ou 'actif'
		if((OLD.valider != NEW.valider or OLD.actif != NEW.actif))then -- Si c'est le cas
			-- Verification si les nouvelles valeurs de valider et actif sont a 'true'
			if(NEW.valider = true and NEW.actif = true)then -- Si c'est le cas on ajout l'heure
				if((NEW.compensation_heure is null OR NEW.compensation_heure = false) and (NEW.heure_supplementaire is null OR NEW.heure_supplementaire = false)) then
					update yvs_grh_presence set total_presence = total_presence + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				else
					if(NEW.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence;
					end if;
					if(NEW.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence;
					end if;
				end if;
			else -- Si ce n'est pas le cas on retirer l'heure
				if(NEW.compensation_heure = false and NEW.heure_supplementaire = false) then
					update yvs_grh_presence set total_presence = total_presence - NEW_somme_ where id = OLD.presence; 
				else
					if(NEW.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup - NEW_somme_ where id = OLD.presence;
					end if;
					if(NEW.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation - NEW_somme_ where id = OLD.presence;
					end if;
				end if;
			end if;
		else -- Si ce n'est pas le cas
			-- Verification si les anciennes valeurs de valider et actif sont a 'true'
			if(OLD.valider = true and OLD.actif)then -- si c'est le cas on ajout l'heure
				if((NEW.compensation_heure is null OR NEW.compensation_heure = false) and (NEW.heure_supplementaire is null OR NEW.heure_supplementaire = false)) then
					update yvs_grh_presence set total_presence = total_presence + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				else
					if(OLD.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence;
					end if;
					if(OLD.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence;
					end if;
				end if;
			end if;
		end if;
	else -- si ce n'est pas le cas
		-- Verification si l'on modifie 'valider' ou 'actif'
		if((OLD.valider != NEW.valider or OLD.actif != NEW.actif))then -- Si c'est le cas
			-- Verification si les nouvelles valeurs de valider et actif sont a 'true'
			if(NEW.valider = true and NEW.actif = true)then -- Si c'est le cas on ajout l'heure
				if((NEW.compensation_heure is null OR NEW.compensation_heure = false) and (NEW.heure_supplementaire is null OR NEW.heure_supplementaire = false)) then
					update yvs_grh_presence set total_presence = total_presence + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				else
					if(NEW.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence;
					end if;
					if(NEW.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence;
					end if;
				end if;
			else -- Si ce n'est pas le cas on retirer l'heure
				if(NEW.compensation_heure = false and NEW.heure_supplementaire = false) then
					update yvs_grh_presence set total_presence = total_presence - NEW_somme_ where id = OLD.presence; 
				else
					if(NEW.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup - NEW_somme_ where id = OLD.presence;
					end if;
					if(NEW.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation - NEW_somme_ where id = OLD.presence;
					end if;
				end if;
			end if;
		else -- Si ce n'est pas le cas
			-- Verification si les anciennes valeurs de valider et actif sont a 'true'
			if(OLD.valider = true and OLD.actif)then
				if((NEW.compensation_heure is null OR NEW.compensation_heure = false) and (NEW.heure_supplementaire is null OR NEW.heure_supplementaire = false)) then
					update yvs_grh_presence set total_presence = total_presence + NEW_somme_ - OLD_somme_ where id = OLD.presence;
				else
					if(NEW.heure_supplementaire = true) then
						update yvs_grh_presence set total_heure_sup = total_heure_sup + NEW_somme_ - OLD_somme_ where id = OLD.presence;
					end if;
					if(NEW.compensation_heure = true) then
						update yvs_grh_presence set total_heure_compensation = total_heure_compensation + NEW_somme_ - OLD_somme_ where id = OLD.presence;
					end if;
				end if;
			end if;
		end if;
	end if;
	
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

