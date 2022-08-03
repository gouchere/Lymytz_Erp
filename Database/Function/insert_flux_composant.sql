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
