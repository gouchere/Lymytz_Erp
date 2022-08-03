-- Function: stock_maj_prix_mvt()

-- DROP FUNCTION stock_maj_prix_mvt();

CREATE OR REPLACE FUNCTION stock_maj_prix_mvt()
  RETURNS trigger AS
$BODY$    
DECLARE
	prix_arr_ double precision;
	stock_ double precision default 0;
	coef_ double precision default 0;
	last_pr_ double precision;
	new_cout double precision;
	new_quantite double precision;
	val_ecart double precision;
	
	unite_c BIGINT;
	mouv_ BIGINT;
	
	line_ RECORD;
	cond_ RECORD;

	duree_ INTEGER DEFAULT 1 ;
	delai_ INTEGER DEFAULT 1 ;
	
	action_ character varying;
	run_ BOOLEAN ;
	--EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_STOCK');
BEGIN
	-- fonction de calcul du prix de revient dans la table mvt de stock
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE  
	IF(action_ = 'INSERT' OR action_ = 'UPDATE') THEN  
		IF(NEW.execute_trigger='OK') THEN
			run_=false;
		ELSE
			run_=true;
		END IF;
	ELSE
		IF(OLD.execute_trigger='OK') THEN
			run_=false;
		ELSE
			run_=true;
		END IF;
	END IF;
	IF(run_) THEN		 
		IF(action_='INSERT') THEN  
			new_quantite = 0;
		ELSIF(action_='UPDATE') THEN  
			new_quantite = NEW.quantite - OLD.quantite;
		END IF;
		last_pr_= COALESCE(get_pr(NEW.article, NEW.depot, 0, NEW.date_doc, NEW.conditionnement, NEW.id), 0);		
		IF(NEW.mouvement = 'E' AND NEW.calcul_pr IS TRUE) THEN 
			--calcule le stock
			stock_= get_stock_reel(NEW.article,0,NEW.depot, 0, 0, NEW.date_doc, NEW.conditionnement, 0) + new_quantite;
			-- Recherche des valeurs null
			if(stock_ is null) THEN
				stock_=0;
			END IF;
			if(NEW.quantite is null)then
				NEW.quantite = 0;
			end if;
			if(NEW.cout_entree is null)then
				NEW.cout_entree = 0;
			end if;
			-- Calcul du cout de stockage
			prix_arr_ = stock_ + NEW.quantite;
			if(prix_arr_ <= 0)then
				prix_arr_ = 1;
			end if;
			new_cout = (((stock_ * last_pr_) + (NEW.quantite * NEW.cout_entree)) / (prix_arr_));
			-- Retourne le nouveau cout moyen calculÃƒÂ©
			if(new_cout is null)then
				new_cout = 0;
			end if;
		ELSE 
			new_cout = last_pr_;
		END IF;
		NEW.cout_stock = ROUND(new_cout::decimal, 3);
		val_ecart := (select taux_ecart_pr from yvs_base_articles where id = NEW.article) ;
		if(COALESCE (val_ecart,0)>0)then
			if(abs(new_cout - last_pr_) > abs(val_ecart))then
				PERFORM set_config ('myapp.EXECUTE_MVT_STOCK','false',false);
				execute 'UPDATE '|| NEW.table_externe||' set calcul_pr = false where id = '|| NEW.id_externe;
				NEW.calcul_pr=false;
				PERFORM set_config ('myapp.EXECUTE_MVT_STOCK','true',false);
			 end if;

		end if;
		UPDATE yvs_base_articles SET date_last_mvt = NEW.date_doc WHERE id = NEW.article;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION stock_maj_prix_mvt()
  OWNER TO postgres;
COMMENT ON FUNCTION stock_maj_prix_mvt() IS 'CETTE FONCTION PERMET DE CALCULER LE COUT DU STOCK EN PRENANT EN COMPTE LA MODIFICATION D''UNE LIGNE DE STOCK';
