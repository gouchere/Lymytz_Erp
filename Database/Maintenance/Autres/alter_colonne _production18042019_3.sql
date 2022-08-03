-- Function: insert_flux_composant()
-- DROP FUNCTION insert_flux_composant();
CREATE OR REPLACE FUNCTION insert_flux_composant()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	operation_ record;
	composant_ record;
	flux_ record;
	session_ record;
	cout_ double precision;
	conditionnement_ bigint;
	result boolean default false;
BEGIN
	select into operation_ y.* from yvs_prod_operations_of y inner join yvs_prod_flux_composant c on y.id = c.operation where c.id = NEW.composant;
	if(operation_.statut_op = 'R' OR operation_.statut_op = 'T') then
		select into flux_ * from yvs_prod_flux_composant where id = NEW.composant;
		select into composant_ * from yvs_prod_composant_of where id = flux_.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		SELECT INTO cout_ op.cout FROM yvs_prod_suivi_operations op WHERE  op.id=NEW.id_operation;
		SELECT INTO session_ sp.* FROM yvs_prod_suivi_operations op INNER JOIN yvs_prod_session_of so ON op.session_of = so.id INNER JOIN yvs_prod_session_prod sp ON so.session_prod = sp.id WHERE op.id=NEW.id_operation;
		--Insertion mouvement stock
		IF(flux_.sens = 'E')then
			result = (select valorisation_stock(composant_.article,composant_.unite, session_.depot, session_.tranche, (NEW.quantite - NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session));
		ELSIF(flux_.sens = 'S') THEN
			result = (select valorisation_stock(composant_.article,composant_.unite, session_.depot, session_.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session));
		ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
			result = (select valorisation_stock(composant_.article,composant_.unite, session_.depot, session_.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session));
		END IF;
		-- Met à jour le prix de l'opération
		IF(cout_ IS NULL) THEN 
			cout_=0; 
		END IF;
		cout_=cout_ + COALESCE(NEW.cout,0);
		UPDATE yvs_prod_suivi_operations SET cout=cout_ WHERE id=NEW.id_operation;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_flux_composant()
  OWNER TO postgres;



-- Function: update_flux_composant()
-- DROP FUNCTION update_flux_composant();
CREATE OR REPLACE FUNCTION update_flux_composant()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	operation_ record;
	composant_ record;
	flux_ record;
	mouv_ bigint;
	arts_ record;
	session_ record;
	cout_ double precision;
    conditionnement_ bigint;
	result boolean default false;
BEGIN
	select into operation_ y.* from yvs_prod_operations_of y inner join yvs_prod_flux_composant c on y.id = c.operation where c.id = NEW.composant;
	IF(operation_.statut_op = 'R' OR operation_.statut_op = 'T') then
		select into flux_ * from yvs_prod_flux_composant where id = NEW.composant;
		select into composant_ * from yvs_prod_composant_of where id = flux_.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		SELECT INTO cout_ op.cout FROM yvs_prod_suivi_operations op WHERE  op.id=NEW.id_operation;
		SELECT INTO session_ sp.* FROM yvs_prod_suivi_operations op INNER JOIN yvs_prod_session_of so ON op.session_of = so.id INNER JOIN yvs_prod_session_prod sp ON so.session_prod = sp.id WHERE op.id=NEW.id_operation;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME LIMIT 1;
		if(mouv_ is not null)then
			select into arts_ * from yvs_base_articles a where a.id = composant_.article;
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				IF(flux_.sens = 'E')then
					result = (select valorisation_stock(composant_.article,composant_.unite, session_.depot, session_.tranche, (NEW.quantite-NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session));
				ELSIF(flux_.sens='S') THEN
					result = (select valorisation_stock(composant_.article,composant_.unite, session_.depot, session_.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session));
				ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
					result = (select valorisation_stock(composant_.article,composant_.unite, session_.depot, session_.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session));
				END IF;
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.cout, conditionnement = composant_.unite, calcul_pr = NEW.calcul_pr, tranche = session_.tranche where id = mouv_;
			end if;
		else
			if(flux_.sens = 'E')then
				result = (select valorisation_stock(composant_.article,composant_.unite, session_.depot, session_.tranche, (NEW.quantite-NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session));
			ELSIF(flux_.sens='S') THEN
				result = (select valorisation_stock(composant_.article,composant_.unite, session_.depot, session_.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session));			
			ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
				result = (select valorisation_stock(composant_.article,composant_.unite, session_.depot, session_.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session));
			END IF;
		end if;
		-- Mettre à jour le cout de l'opération
		SELECT INTO cout_ op.cout FROM yvs_prod_suivi_operations op WHERE  op.id=NEW.id_operation;
		IF(cout_ IS NULL) THEN 
			cout_=0; 
		END IF;
		cout_=cout_ + COALESCE(NEW.cout,0)-COALESCE(OLD.cout,0);
		UPDATE yvs_prod_suivi_operations SET cout=cout_ WHERE id=NEW.id_operation;
			
	ELSE
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		UPDATE yvs_prod_suivi_operations SET cout=(cout-(NEW.cout,0)) WHERE id=NEW.id_operation;
	END IF;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_flux_composant()
  OWNER TO postgres;
  
  
  
-- Function: insert_declaration_production()
-- DROP FUNCTION insert_declaration_production();
CREATE OR REPLACE FUNCTION insert_declaration_production()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	session_ record;
 	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre;
		SELECT INTO session_ sp.* FROM yvs_prod_session_of so INNER JOIN yvs_prod_session_prod sp ON so.session_prod = sp.id WHERE so.id=NEW.session_of;
		--Insertion mouvement stock
		result = (select valorisation_stock(ordre_.article,NEW.conditionnement, session_.depot, session_.tranche, NEW.quantite, NEW.cout_production, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session));
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_declaration_production()
  OWNER TO postgres;


  
-- Function: update_declaration_production()
-- DROP FUNCTION update_declaration_production();
CREATE OR REPLACE FUNCTION update_declaration_production()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	session_ record;
 	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre;
		SELECT INTO session_ sp.* FROM yvs_prod_session_of so INNER JOIN yvs_prod_session_prod sp ON so.session_prod = sp.id WHERE so.id=NEW.session_of;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		if(mouv_ is not null)then
			select into arts_ * from yvs_base_articles a inner join yvs_base_conditionnement c on c.article = a.id where c.id = NEW.conditionnement;
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				result = (select valorisation_stock(ordre_.article,NEW.conditionnement, session_.depot, session_.tranche, NEW.quantite, NEW.cout_production, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session));
			else
				RAISE NOTICE 'session_.tranche : %',session_.tranche;
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.cout_production, conditionnement = NEW.conditionnement, calcul_pr = NEW.calcul_pr, tranche = session_.tranche where id = mouv_;
			end if;
		else
			result = (select valorisation_stock(ordre_.article,NEW.conditionnement, session_.depot, session_.tranche, NEW.quantite, NEW.cout_production, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session));
		end if;	
	elsif(NEW.statut != 'V')then
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_declaration_production()
  OWNER TO postgres;
  
  

-- Function: com_et_bouclage_journalier(bigint, bigint, date, boolean)
-- DROP FUNCTION com_et_bouclage_journalier(bigint, bigint, date, boolean);
CREATE OR REPLACE FUNCTION com_et_bouclage_journalier(IN societe_ bigint, IN agence_ bigint, IN date_ date, IN by_point_ boolean)
  RETURNS TABLE(id bigint, code character varying, nom character varying, entete character varying, valeur double precision, is_agence boolean, rang integer) AS
$BODY$
declare 

	ligne_ RECORD;
	
	query_ character varying;
	libelle_ character varying;
   
	valeur_ double precision default 0;
   
begin 	
	DROP TABLE IF EXISTS table_et_bouclage_journalier;
	CREATE TEMP TABLE IF NOT EXISTS table_et_bouclage_journalier(_id bigint, _code character varying, _nom character varying, _entete character varying, _valeur double precision, _is_agence boolean, _rang integer);
	DELETE FROM table_et_bouclage_journalier;
	IF(COALESCE(by_point_, false) IS FALSE)THEN
		query_ = 'SELECT y.id, y.code_users AS code, y.nom_users AS nom FROM yvs_users y INNER JOIN yvs_agences a ON y.agence = a.id';
	ELSE
		query_ = 'SELECT y.id, y.code, y.libelle AS nom FROM yvs_base_point_vente y INNER JOIN yvs_agences a ON y.agence = a.id';
	END IF;
	IF(COALESCE(societe_, 0) > 0)THEN
		query_ = query_ || ' WHERE a.societe = '||societe_;
		IF(COALESCE(agence_, 0) > 0)THEN
			query_ = query_ || ' AND a.id = '||agence_;
		END IF;
		FOR ligne_ IN EXECUTE query_
		LOOP
			IF(COALESCE(by_point_, false) IS FALSE)THEN
				valeur_ = (SELECT get_ca_vendeur(ligne_.id, date_, date_));
			ELSE
				valeur_ = (SELECT get_ca_point_vente(ligne_.id, date_, date_));
			END IF;
			IF(COALESCE(valeur_, 0) > 0)THEN
				INSERT INTO table_et_bouclage_journalier VALUES(ligne_.id, ligne_.code, ligne_.nom, 'CA REALISE', valeur_, false, 1);
			END IF;
			
		END LOOP;
	END IF;
	RETURN QUERY SELECT * FROM table_et_bouclage_journalier ORDER BY _is_agence, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_bouclage_journalier(bigint, bigint, date, boolean)
  OWNER TO postgres;

