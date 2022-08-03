-- Table: yvs_prod_session_prod

-- DROP TABLE yvs_prod_session_prod;
-- Table: yvs_prod_session_prod

-- DROP TABLE yvs_prod_session_prod;

CREATE TABLE yvs_prod_session_prod
(
  id bigserial NOT NULL,
  date_session date,
  equipe bigint,
  tranche bigint,
  producteur bigint,
  author bigint,
  date_save timestamp without time zone,
  date_update timestamp without time zone,
  depot bigint,
  CONSTRAINT yvs_prod_session_prod_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_session_prod_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_session_prod_depot_fkey FOREIGN KEY (depot)
      REFERENCES yvs_base_depots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_session_prod_equipe_fkey FOREIGN KEY (equipe)
      REFERENCES yvs_prod_equipe_production (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_session_prod_producteur_fkey FOREIGN KEY (producteur)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_session_prod_tranche_fkey FOREIGN KEY (tranche)
      REFERENCES yvs_grh_tranche_horaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_session_prod
  OWNER TO postgres;

  
-- Table: yvs_prod_session_of

-- DROP TABLE yvs_prod_session_of;

CREATE TABLE yvs_prod_session_of
(
  id bigserial NOT NULL,
  ordre bigint,
  session_prod bigint,
  author bigint,
  date_save date,
  date_update timestamp without time zone,
  CONSTRAINT yvs_prod_session_of_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_session_of_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_session_of_ordre_fkey FOREIGN KEY (ordre)
      REFERENCES yvs_prod_ordre_fabrication (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_session_of_session_prod_fkey FOREIGN KEY (session_prod)
      REFERENCES yvs_prod_session_prod (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_session_of
  OWNER TO postgres;

  
-- Table: yvs_prod_suivi_operations

-- DROP TABLE yvs_prod_suivi_operations;

CREATE TABLE yvs_prod_suivi_operations
(
  id bigserial NOT NULL,
  session_of bigint,
  operation_of bigint,
  date_debut date,
  date_fin date,
  heure_debut time without time zone,
  heure_fin time without time zone,
  cout double precision,
  ref_operation character varying,
  statut_op character varying,
  author bigint,
  date_save timestamp without time zone,
  date_upadate timestamp without time zone,
  CONSTRAINT yvs_prod_suivi_operations_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_suivi_operations_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_suivi_operations_operation_of_fkey FOREIGN KEY (operation_of)
      REFERENCES yvs_prod_operations_of (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_suivi_operations_session_of_fkey FOREIGN KEY (session_of)
      REFERENCES yvs_prod_session_of (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_suivi_operations
  OWNER TO postgres;


-- ALTER TABLE yvs_prod_of_suivi_flux DROP COLUMN operation;

ALTER TABLE yvs_prod_of_suivi_flux ADD COLUMN id_operation bigint;
-- ALTER TABLE yvs_prod_of_suivi_flux DROP CONSTRAINT yvs_prod_of_suivi_flux_operation_fkey;

ALTER TABLE yvs_prod_of_suivi_flux
  ADD CONSTRAINT yvs_prod_of_suivi_flux_id_operation_fkey FOREIGN KEY (id_operation)
      REFERENCES yvs_prod_suivi_operations (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_prod_declaration_production ADD COLUMN session_of bigint;

-- ALTER TABLE yvs_prod_declaration_production DROP CONSTRAINT yvs_prod_declaration_production_session_prod_fkey;
ALTER TABLE yvs_prod_declaration_production
  ADD CONSTRAINT yvs_prod_declaration_production_session_of_fkey FOREIGN KEY (session_of)
      REFERENCES yvs_prod_session_of (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

DROP TABLE yvs_prod_employe_equipe;

CREATE TABLE yvs_prod_membres_equipe
(
  id bigserial NOT NULL,
  equipe_production bigint,
  producteur bigint,
  actif boolean DEFAULT true,
  author bigint,
  date_update timestamp without time zone DEFAULT ('now'::text)::date,
  date_save timestamp without time zone DEFAULT ('now'::text)::date,
  CONSTRAINT yvs_prod_membres_equipe_pkey PRIMARY KEY (id),
  CONSTRAINT yvs_prod_membres_equipe_author_fkey FOREIGN KEY (author)
      REFERENCES yvs_users_agence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_membres_equipe_producteur_fkey FOREIGN KEY (producteur)
      REFERENCES yvs_users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yvs_prod_membres_equipe_equipe_production_fkey FOREIGN KEY (equipe_production)
      REFERENCES yvs_prod_equipe_production (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yvs_prod_membres_equipe
  OWNER TO postgres;
  
  
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
		--Insertion mouvement stock
		IF(flux_.sens = 'E')then
			result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, (NEW.quantite - NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_flux));
		ELSIF(flux_.sens = 'S') THEN
			result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
		ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
			result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
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
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME LIMIT 1;
		if(mouv_ is not null)then
			select into arts_ * from yvs_base_articles a where a.id = composant_.article;
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				IF(flux_.sens = 'E')then
					result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, (NEW.quantite-NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_flux));
				ELSIF(flux_.sens='S') THEN
					result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
				ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
					result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
				END IF;
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = NEW.cout, conditionnement = composant_.unite, calcul_pr = NEW.calcul_pr, tranche = NEW.tranche where id = mouv_;
			end if;
		else
			if(flux_.sens = 'E')then
				result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, (NEW.quantite-NEW.quantite_perdue), NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_flux));
			ELSIF(flux_.sens='S') THEN
				result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));			
			ELSIF(flux_.sens = 'N' AND NEW.quantite_perdue>0) THEN 
				result = (select valorisation_stock(composant_.article,composant_.unite, composant_.depot_conso, NEW.tranche, NEW.quantite_perdue, NEW.cout, ''||TG_TABLE_NAME||'', NEW.id, 'S', NEW.date_flux));
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

  
CREATE OR REPLACE FUNCTION prod_maj_cout_of()
  RETURNS trigger AS
$BODY$    
DECLARE
	cout_ double precision;
	ordre_ bigint;
	
BEGIN	
	SELECT INTO ordre_ o.ordre_fabrication FROM yvs_prod_operations_of o WHERE o.id=NEW.operation_of;
	SELECT INTO cout_ SUM(op.cout) FROM yvs_prod_suivi_operations op INNER JOIN yvs_prod_operations_of o ON o.id=op.operation_of
	WHERE o.ordre_fabrication=ordre_;
	UPDATE yvs_prod_ordre_fabrication SET cout_of=cout_ WHERE id=ordre_;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION prod_maj_cout_of()
  OWNER TO postgres;


CREATE TRIGGER prod_maj_cout_op_on_of
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_prod_suivi_operations
  FOR EACH ROW
  EXECUTE PROCEDURE prod_maj_cout_of();


-- ALTER TABLE yvs_prod_ordre_fabrication DROP COLUMN cout_of;

ALTER TABLE yvs_prod_ordre_fabrication ADD COLUMN cout_of double precision;

ALTER TABLE yvs_prod_parametre ADD COLUMN declaration_proportionnel boolean;
ALTER TABLE yvs_prod_parametre ALTER COLUMN declaration_proportionnel SET DEFAULT false;

ALTER TABLE yvs_prod_composant_of ADD COLUMN marge double precision;
