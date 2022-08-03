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
	mouv_ record;
	depot_ bigint;
	result_ boolean default false;
	last_pr_ double precision default 0;
BEGIN
	IF(TG_OP = 'DELETE' OR TG_OP = 'TRUNCATE')THEN
	DELETE FROM yvs_base_mouvement_stock WHERE id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		RETURN OLD;
	END IF;
	select into operation_ y.* from yvs_prod_operations_of y inner join yvs_prod_flux_composant c on y.id = c.operation where c.id = NEW.composant;
	if(operation_.statut_op = 'R' OR operation_.statut_op = 'T') then
		select into flux_ * from yvs_prod_flux_composant where id = NEW.composant;
		select into composant_ * from yvs_prod_composant_of where id = flux_.composant;
		select into ordre_ * from yvs_prod_ordre_fabrication where id = operation_.ordre_fabrication;
		SELECT INTO cout_ op.cout FROM yvs_prod_suivi_operations op WHERE  op.id=NEW.id_operation;
		SELECT INTO session_ sp.* FROM yvs_prod_suivi_operations op INNER JOIN yvs_prod_session_of so ON op.session_of = so.id 
																	INNER JOIN yvs_prod_session_prod sp ON so.session_prod = sp.id 
																	WHERE op.id=NEW.id_operation;
		--Insertion mouvement stock
		SELECT INTO mouv_ id, cout_stock FROM yvs_base_mouvement_stock WHERE id_externe = NEW.id AND table_externe = TG_TABLE_NAME;
		depot_=COALESCE(composant_.depot_conso,session_.depot);
		IF(flux_.sens = 'E')then
			--vérifie si le mouvement existe déjà			
			IF(COALESCE(mouv_.id,0)<=0) THEN
				result_ = (select valorisation_stock(composant_.article,composant_.unite, depot_, session_.tranche, (NEW.quantite - NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session));
			ELSE
				last_pr_= COALESCE(get_pr(composant_.article, depot_, 0, session_.date_session, composant_.unite, mouv_.id), 0);
				PERFORM insert_mouvement_stock(composant_.article, depot_, session_.tranche, (NEW.quantite - NEW.quantite_perdue), NEW.cout, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'E', session_.date_session, last_pr_, mouv_.id);
			END IF;
		ELSIF(flux_.sens = 'S') THEN
			IF(COALESCE(mouv_.id,0)<=0) THEN
				result_ = (select valorisation_stock(composant_.article,composant_.unite, depot_, session_.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session));
			ELSE
				last_pr_= COALESCE(get_pr(composant_.article, depot_, 0, session_.date_session, composant_.unite, mouv_.id), 0);
				PERFORM insert_mouvement_stock(composant_.article, depot_, session_.tranche, NEW.quantite, NEW.cout, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session, last_pr_, mouv_.id);
			END IF;
		ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
			IF(COALESCE(mouv_.id,0)<=0) THEN
				result_ = (select valorisation_stock(composant_.article,composant_.unite, depot_, session_.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session));
			ELSE
				last_pr_= COALESCE(get_pr(composant_.article, depot_, 0, session_.date_session, composant_.unite, mouv_.id), 0);
				PERFORM insert_mouvement_stock(composant_.article, depot_, session_.tranche, NEW.quantite_perdue, NEW.cout, mouv_.cout_stock, null, ''||TG_TABLE_NAME||'', NEW.id, 'S', session_.date_session, last_pr_, mouv_.id);
			END IF;
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
