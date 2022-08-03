-- Function: compta_action_on_acompte_fournisseur()

-- DROP FUNCTION compta_action_on_acompte_fournisseur();

CREATE OR REPLACE FUNCTION mut_action_on_contribution_evenement()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	caisse_ RECORD;
	action_ CHARACTER VARYING;
	table_name_ CHARACTER VARYING DEFAULT 'CONTRIBUTION_EVENEMENT';
	existe_ BIGINT DEFAULT 0;
	
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_ t.designation FROM yvs_mut_evenement e INNER JOIN yvs_mut_type_evenement t ON e.type = t.id WHERE e.id = NEW.evenement;
		SELECT INTO caisse_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
		SELECT INTO tiers_ m.id, CONCAT(e.nom, ' ', e.prenom) AS nom FROM yvs_mut_compte c INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.compte;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
	    -- Récupère le document
		IF(NEW.caisse IS NOT NULL)THEN
			INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
				author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
			VALUES (null, NEW.id, 'Contribution '||line_.designation, table_name_, NEW.montant, null, NEW.date_contribution, caisse_.caissier, 'P',  NEW.author, 
				NEW.caisse, 'R', tiers_.id, null, tiers_.nom, NEW.date_update, NEW.date_save);
		END IF;
	ELSIF (action_='UPDATE') THEN 
		IF(NEW.caisse IS NOT NULL)THEN
			SELECT INTO existe_ id FROM yvs_mut_mouvement_caisse WHERE table_externe=table_name_ AND id_externe=NEW.id;
			IF(existe_ IS NOT NULL OR existe_ > 0) THEN 	
				UPDATE yvs_mut_mouvement_caisse SET montant = NEW.montant, date_mvt = NEW.date_contribution, caisse = NEW.caisse, caissier = caisse_.caissier, 
				tiers_interne = tiers_.id, name_tiers = tiers_.nom WHERE id = existe_;
			ELSE
				INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
					author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
				VALUES (null, NEW.id, 'Contribution '||line_.designation, table_name_, NEW.montant, null, NEW.date_contribution, caisse_.caissier, 'P',  NEW.author, 
					NEW.caisse, 'R', tiers_.id, null, tiers_.nom, NEW.date_update, NEW.date_save);
			END IF;	
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_contribution_evenement()
  OWNER TO postgres;
  

CREATE OR REPLACE FUNCTION mut_action_on_financement_activite()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	caisse_ RECORD;
	action_ CHARACTER VARYING;
	table_name_ CHARACTER VARYING DEFAULT 'FINANCEMENT_ACTIVITE';
	existe_ BIGINT DEFAULT 0;
	
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_ t.designation FROM yvs_mut_activite a INNER JOIN yvs_mut_evenement e ON a.evenement =e.id INNER JOIN yvs_mut_type_evenement t ON e.type = t.id WHERE a.id = NEW.activite;
		SELECT INTO caisse_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
	    -- Récupère le document
		INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
			author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
		VALUES (null, NEW.id, 'Contribution '||line_.designation, table_name_, NEW.montant_recu, null, NEW.date_financement, caisse_.caissier, 'P',  NEW.author, 
			NEW.caisse, 'D', null, null, null, NEW.date_update, NEW.date_save);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO existe_ id FROM yvs_mut_mouvement_caisse WHERE table_externe=table_name_ AND id_externe=NEW.id;
		IF(existe_ IS NOT NULL OR existe_ > 0) THEN 	
			UPDATE yvs_mut_mouvement_caisse SET montant = NEW.montant_recu, date_mvt = NEW.date_financement, caisse = NEW.caisse, caissier = caisse_.caissier, 
			tiers_interne = null, name_tiers = null WHERE id = existe_;
		ELSE
			INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
				author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
			VALUES (null, NEW.id, 'Contribution '||line_.designation, table_name_, NEW.montant_recu, null, NEW.date_financement, caisse_.caissier, 'P',  NEW.author, 
				NEW.caisse, 'D', null, null, null, NEW.date_update, NEW.date_save);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_financement_activite()
  OWNER TO postgres;
  

CREATE OR REPLACE FUNCTION mut_action_on_operation_caisse()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	caisse_ RECORD;
	action_ CHARACTER VARYING;
	mouvement_ CHARACTER VARYING DEFAULT 'D';
	table_name_ CHARACTER VARYING DEFAULT 'OPERATION_CAISSE';
	existe_ BIGINT DEFAULT 0;
	
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO caisse_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
		IF(NEW.nature = 'Depot')THEN
			mouvement_ = 'R';
		END IF;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
	    -- Récupère le document
		INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
			author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
		VALUES (null, NEW.id, NEW.commentaire, table_name_, NEW.montant, null, NEW.date_operation, caisse_.caissier, 'P', NEW.author, 
			NEW.caisse, mouvement_, null, NEW.source, null, NEW.date_update, NEW.date_save);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO existe_ id FROM yvs_mut_mouvement_caisse WHERE table_externe=table_name_ AND id_externe=NEW.id;
		IF(existe_ IS NOT NULL OR existe_ > 0) THEN 	
			UPDATE yvs_mut_mouvement_caisse SET montant = NEW.montant, date_mvt = NEW.date_operation, caisse = NEW.caisse, caissier = caisse_.caissier, 
			tiers_interne = null, tiers_externe = NEW.source, name_tiers = null, mouvement = mouvement_ WHERE id = existe_;
		ELSE
			INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
				author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
			VALUES (null, NEW.id, NEW.commentaire, table_name_, NEW.montant, null, NEW.date_operation, caisse_.caissier, 'P',  NEW.author, 
				NEW.caisse, mouvement_, null, NEW.source, null, NEW.date_update, NEW.date_save);
		END IF;	
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_operation_caisse()
  OWNER TO postgres;
  

CREATE OR REPLACE FUNCTION mut_action_on_operation_compte()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	caisse_ RECORD;
	action_ CHARACTER VARYING;
	mouvement_ CHARACTER VARYING DEFAULT 'D';
	table_name_ CHARACTER VARYING DEFAULT 'OPERATION_COMPTE';
	existe_ BIGINT DEFAULT 0;
	
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO caisse_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
		SELECT INTO tiers_ m.id, CONCAT(e.nom, ' ', e.prenom) AS nom FROM yvs_mut_compte c INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.compte;
		IF(NEW.nature = 'Depot')THEN
			mouvement_ = 'R';
		END IF;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
	    -- Récupère le document
		IF(NEW.caisse IS NOT NULL)THEN
			INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
				author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
			VALUES (NEW.reference_operation, NEW.id, NEW.commentaire, table_name_, NEW.montant, NEW.reference_operation, NEW.date_operation, caisse_.caissier, 'P', NEW.author, 
				NEW.caisse, mouvement_, tiers_.id, null, tiers_.nom, NEW.date_update, NEW.date_save);
		END IF;
	ELSIF (action_='UPDATE') THEN 
		IF(NEW.caisse IS NOT NULL)THEN
			SELECT INTO existe_ id FROM yvs_mut_mouvement_caisse WHERE table_externe=table_name_ AND id_externe=NEW.id;
			IF(existe_ IS NOT NULL OR existe_ > 0) THEN 	
				UPDATE yvs_mut_mouvement_caisse SET montant = NEW.montant, date_mvt = NEW.date_operation, caisse = NEW.caisse, caissier = caisse_.caissier,
				numero = NEW.reference_operation, reference_externe = NEW.reference_operation, 
				tiers_interne = tiers_.id, tiers_externe = null, name_tiers = tiers_.nom, mouvement = mouvement_ WHERE id = existe_;
			ELSE
				INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
					author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
				VALUES (NEW.reference_operation, NEW.id, NEW.commentaire, table_name_, NEW.montant, NEW.reference_operation, NEW.date_operation, caisse_.caissier, 'P',  NEW.author, 
					NEW.caisse, mouvement_, tiers_.id, null, tiers_.nom, NEW.date_update, NEW.date_save);
			END IF;	
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_operation_compte()
  OWNER TO postgres;  
  
  
CREATE OR REPLACE FUNCTION mut_action_on_reglement_credit()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	caisse_ RECORD;
	action_ CHARACTER VARYING;
	mouvement_ CHARACTER VARYING DEFAULT 'D';
	table_name_ CHARACTER VARYING DEFAULT 'REGLEMENT_CREDIT';
	existe_ BIGINT DEFAULT 0;
	
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO caisse_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
		SELECT INTO tiers_ m.id, CONCAT(e.nom, ' ', e.prenom) AS nom, r.reference FROM yvs_mut_credit r INNER JOIN yvs_mut_compte c ON r.compte = c.id INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE r.id = NEW.credit;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
	    -- Récupère le document
		IF(NEW.caisse IS NOT NULL)THEN
			INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
				author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
			VALUES (tiers_.reference, NEW.id, 'Reglement '||tiers_.reference, table_name_, NEW.montant, tiers_.reference, NEW.date_reglement, caisse_.caissier, NEW.statut_piece, NEW.author, 
				NEW.caisse, mouvement_, tiers_.id, null, tiers_.nom, NEW.date_update, NEW.date_save);
		END IF;
	ELSIF (action_='UPDATE') THEN 
		IF(NEW.caisse IS NOT NULL)THEN
			SELECT INTO existe_ id FROM yvs_mut_mouvement_caisse WHERE table_externe=table_name_ AND id_externe=NEW.id;
			IF(existe_ IS NOT NULL OR existe_ > 0) THEN 	
				UPDATE yvs_mut_mouvement_caisse SET montant = NEW.montant, date_mvt = NEW.date_reglement, caisse = NEW.caisse, caissier = caisse_.caissier,
				numero = tiers_.reference, reference_externe = tiers_.reference, statut_piece = NEW.statut_piece,
				tiers_interne = tiers_.id, tiers_externe = null, name_tiers = tiers_.nom, mouvement = mouvement_ WHERE id = existe_;
			ELSE
				INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
					author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
				VALUES (tiers_.reference, NEW.id, 'Reglement '||tiers_.reference, table_name_, NEW.montant, tiers_.reference, NEW.date_reglement, caisse_.caissier, NEW.statut_piece,  NEW.author, 
					NEW.caisse, mouvement_, tiers_.id, null, tiers_.nom, NEW.date_update, NEW.date_save);
			END IF;	
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_reglement_credit()
  OWNER TO postgres; 
  
  
CREATE OR REPLACE FUNCTION mut_action_on_reglement_mensualite()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	caisse_ RECORD;
	action_ CHARACTER VARYING;
	mouvement_ CHARACTER VARYING DEFAULT 'R';
	table_name_ CHARACTER VARYING DEFAULT 'REGLEMENT_MENSUALITE';
	existe_ BIGINT DEFAULT 0;
	
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO caisse_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
		SELECT INTO tiers_ m.id, CONCAT(e.nom, ' ', e.prenom) AS nom, r.reference FROM yvs_mut_mensualite s INNER JOIN yvs_mut_echellonage h ON s.echellonage = h.id
			INNER JOIN yvs_mut_credit r ON h.credit = r.id INNER JOIN yvs_mut_compte c ON r.compte = c.id INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id 
			INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE s.id = NEW.mensualite;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
	    -- Récupère le document
		IF(NEW.caisse IS NOT NULL)THEN
			INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
				author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
			VALUES (tiers_.reference, NEW.id, 'Mensualite '||tiers_.reference, table_name_, NEW.montant, tiers_.reference, NEW.date_reglement, caisse_.caissier, NEW.statut_piece, NEW.author, 
				NEW.caisse, mouvement_, tiers_.id, null, tiers_.nom, NEW.date_update, NEW.date_save);
		END IF;
	ELSIF (action_='UPDATE') THEN 
		IF(NEW.caisse IS NOT NULL)THEN
			SELECT INTO existe_ id FROM yvs_mut_mouvement_caisse WHERE table_externe=table_name_ AND id_externe=NEW.id;
			IF(existe_ IS NOT NULL OR existe_ > 0) THEN 	
				UPDATE yvs_mut_mouvement_caisse SET montant = NEW.montant, date_mvt = NEW.date_reglement, caisse = NEW.caisse, caissier = caisse_.caissier,
				numero = tiers_.reference, reference_externe = tiers_.reference, statut_piece = NEW.statut_piece,
				tiers_interne = tiers_.id, tiers_externe = null, name_tiers = tiers_.nom, mouvement = mouvement_ WHERE id = existe_;
			ELSE
				INSERT INTO yvs_mut_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, caissier, statut_piece, 
					author, caisse, mouvement, tiers_interne, tiers_externe, name_tiers, date_update, date_save)
				VALUES (tiers_.reference, NEW.id, 'Mensualite '||tiers_.reference, table_name_, NEW.montant, tiers_.reference, NEW.date_reglement, caisse_.caissier, NEW.statut_piece,  NEW.author, 
					NEW.caisse, mouvement_, tiers_.id, null, tiers_.nom, NEW.date_update, NEW.date_save);
			END IF;	
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_reglement_mensualite()
  OWNER TO postgres;
  
  
CREATE TRIGGER mut_action_on_operation_compte
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_mut_operation_compte
  FOR EACH ROW
  EXECUTE PROCEDURE mut_action_on_operation_compte();  
  
CREATE TRIGGER mut_action_on_reglement_mensualite
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_mut_reglement_mensualite
  FOR EACH ROW
  EXECUTE PROCEDURE mut_action_on_reglement_mensualite();
  
  
CREATE TRIGGER mut_action_on_reglement_credit
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_mut_reglement_credit
  FOR EACH ROW
  EXECUTE PROCEDURE mut_action_on_reglement_credit(); 

  
CREATE TRIGGER mut_action_on_operation_caisse
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_mut_operation_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE mut_action_on_operation_caisse();

  
CREATE TRIGGER mut_action_on_financement_activite
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_mut_financement_activite
  FOR EACH ROW
  EXECUTE PROCEDURE mut_action_on_financement_activite();

  
CREATE TRIGGER mut_action_on_contribution_evenement
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_mut_contribution_evenement
  FOR EACH ROW
  EXECUTE PROCEDURE mut_action_on_contribution_evenement();
  
-- Function: mut_total_caisse(bigint, character varying, character varying, character)
-- DROP FUNCTION mut_total_caisse(bigint, character varying, character varying, character);

CREATE OR REPLACE FUNCTION mut_total_caisse(caisse_ bigint, table_ character varying, mouvement_ character varying, statut_ character)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	somme_mouv double precision default 0;

BEGIN
	if(caisse_ is not null and caisse_ > 0)then
		if(mouvement_ is not null)then
			if(table_ is null or table_ = '')then
				if(mouvement_ = 'D')then
					select into somme_mouv SUM(montant) from yvs_mut_mouvement_caisse where mouvement = 'D' and caisse = caisse_ AND statut_piece=statut_;
				elsif(mouvement_ = 'R')then
					select into somme_mouv SUM(montant) from yvs_mut_mouvement_caisse where mouvement = 'R' and caisse = caisse_ AND statut_piece=statut_;
				end if;
			else
				if(mouvement_ = 'D')then
					select into somme_mouv SUM(montant) from yvs_mut_mouvement_caisse where mouvement = 'D' and caisse = caisse_ and table_externe = table_ AND statut_piece=statut_;
				elsif(mouvement_ = 'R')then
					select into somme_mouv SUM(montant) from yvs_mut_mouvement_caisse where mouvement = 'R' and caisse = caisse_ and table_externe = table_ AND statut_piece=statut_;
				end if;
			end if;
			if(somme_mouv is null)then
				somme_mouv = 0;
			end if;
			total_ = somme_mouv;
		end if;
	end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_total_caisse(bigint, character varying, character varying, character)
  OWNER TO postgres;
COMMENT ON FUNCTION mut_total_caisse(bigint, character varying, character varying, character) IS 'retourne le total (dépense ou recette) d''une caisse ';


-- Function: update_credit()

-- DROP FUNCTION update_credit();

CREATE OR REPLACE FUNCTION update_credit()
  RETURNS trigger AS
$BODY$    
DECLARE
	caisse_ record;
	montant_ double precision;
id_ bigint;
BEGIN
	if(OLD.etat = 'Accordé' or NEW.etat = 'Accordé')then
		update yvs_mut_compte set solde = solde - NEW.montant_verse where id = OLD.compte;
	end if;
	if(NEW.etat != OLD.etat and NEW.etat = 'Reglé')then
		-- Recuperation de la caisse interet
		-- select into caisse_ ca.id from yvs_mut_caisse ca inner join yvs_mut_mutualiste mu 
-- 			on mu.mutuelle = ca.mutuelle inner join yvs_mut_compte co on co.mutualiste = mu.id
-- 			inner join yvs_mut_type_compte tc on ca.type = tc.id
-- 			where tc.type_compte = 'Interet' and co.id = OLD.compte limit 1;
-- 		update yvs_mut_caisse set solde = solde + (NEW.montant_verse - OLD.montant) where 
-- 			id = caisse_.id;
	end if;
	IF(NEW.etat='Payé') THEN
	--trouve le montant de remboursement total dans la table echéancier
	SELECT INTO montant_ ech.montant FROM  yvs_mut_echellonage ech INNER JOIN yvs_mut_credit cr ON cr.id=ech.credit
				 WHERE ech.etat='EnCours' AND ech.credit=NEW.id limit 1;
		IF montant_ IS NOT NULL THEN
		--verifie si le crédit a déjà été payé
		SELECT INTO id_ op.id FROM yvs_mut_operation_compte op WHERE op.souce_reglement=NEW.id AND op.table_source='yvs_mut_credit' ;
		  IF id_ IS NULL THEN 
		        INSERT INTO yvs_mut_operation_compte(date_operation, montant, nature, compte, author, heure_operation, 
						      commentaire, automatique, epargne_mensuel, souce_reglement,table_source)
		        VALUES (NEW.date_credit, -montant_, 'Retrait', NEW.compte, NEW.author, current_time, 
			'', NEW.automatique, false, NEW.id,'yvs_mut_credit');
		 END IF;
		END IF;
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_credit()
  OWNER TO postgres;

-- Function: get_total_caisse(bigint, character varying, character varying, character)

-- DROP FUNCTION get_total_caisse(bigint, character varying, character varying, character);

CREATE OR REPLACE FUNCTION compta_total_caisse(societe_ bigint, caisse_ bigint, mode_ bigint, table_ character varying, mouvement_ character varying, type_ character varying , statut_ character, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	depense_ double precision default 0;
	recette_ double precision default 0;
	query_ CHARACTER VARYING DEFAULT 'SELECT COALESCE(SUM(COALESCE(y.montant, 0)), 0) FROM yvs_compta_mouvement_caisse y LEFT JOIN yvs_base_mode_reglement m ON y.model = m.id WHERE montant IS NOT NULL';
	query_0 CHARACTER VARYING DEFAULT '';

BEGIN
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND y.societe = '||societe_;
	END IF;
	IF(caisse_ IS NOT NULL AND caisse_ > 0)THEN
		query_ = query_ || ' AND y.caisse = '||caisse_;
	END IF;
	IF(mode_ IS NOT NULL AND mode_ > 0)THEN
		query_ = query_ || ' AND y.model = '||mode_;
	END IF;
	IF(date_ IS NOT NULL)THEN
		query_ = query_ || ' AND y.date_mvt <= '''||date_||'''';
	END IF;
	IF(table_ IS NOT NULL AND table_ != '')THEN
		query_ = query_ || ' AND y.table_externe = '''||table_||'''';
	END IF;
	IF(statut_ IS NOT NULL AND statut_ != '')THEN
		query_ = query_ || ' AND y.statut_piece = '''||statut_||'''';
	END IF;
	IF(type_ IS NOT NULL AND type_ != '')THEN
		query_ = query_ || ' AND m.type_reglement IN (SELECT TRIM(val) FROM regexp_split_to_table('''||type_||''','','') val)';
	END IF;
	IF(mouvement_ IS NOT NULL AND mouvement_ != '')THEN
		IF(mouvement_ = 'D')THEN
			query_ = query_ || ' AND y.mouvement = ''D''';
		ELSIF(mouvement_ = 'R')THEN
			query_ = query_ || ' AND y.mouvement = ''R''';
		ELSE
			query_0 = query_ || ' AND y.mouvement = ''D''';
			query_ = query_ || ' AND y.mouvement = ''R''';
		END IF;
	ELSE
		query_0 = query_ || ' AND y.mouvement = ''D''';
		query_ = query_ || ' AND y.mouvement = ''R''';		
	END IF;
	IF(query_0 IS NOT NULL AND query_0 != '')THEN
		EXECUTE query_0 INTO depense_;
	END IF;	
	EXECUTE query_ INTO recette_;
	RETURN COALESCE(recette_, 0) - COALESCE(depense_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_total_caisse(bigint, bigint, bigint, character varying, character varying, character varying, character, date)
  OWNER TO postgres;
COMMENT ON FUNCTION compta_total_caisse(bigint, bigint, bigint, character varying, character varying, character varying, character, date) IS 'retourne le total (dépense ou recette) d''une caisse ';


-- Function: compta_action_on_piece_caisse_vente()

-- DROP FUNCTION compta_action_on_piece_caisse_vente();

CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';
	echeance_ RECORD;
	vente_ BIGINT;
	solde_ double precision default 0;
	payer_ double precision default 0;
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;
		
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, dv.num_doc,cu.users, dv.client, cl.nom, cl.prenom  FROM yvs_compta_caisse_piece_vente pv INNER JOIN yvs_com_doc_ventes dv ON dv.id=pv.vente 
			INNER JOIN yvs_com_client cl ON cl.id=dv.client INNER JOIN yvs_com_entete_doc_vente en ON en.id=dv.entete_doc
			INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=en.creneau INNER JOIN yvs_com_creneau_point cr ON cr.id=cu.creneau_point
			INNER JOIN yvs_base_point_vente dp ON dp.id=cr.point INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE pv.id=NEW.id; 
									    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		id_tiers_=line_.client;
		--récupère le code tièrs de ce clients
		SELECT INTO id_tiers_ t.tiers FROM yvs_com_client t WHERE t.id=id_tiers_;
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		IF(line_.nom IS NOT NULL) THEN
			nom_=line_.nom;
		END IF;
		IF(line_.prenom IS NOT NULL) THEN
			prenom_=line_.prenom;
		END IF;
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
		vente_ = NEW.vente;
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=line_.num_doc, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
				statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='R', societe=line_.societe, name_tiers=(nom_ ||' '||prenom_)
			WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,
				NEW.caisse, NEW.caissier,null, id_tiers_,'R', (nom_ ||' '||prenom_), NEW.model);
		END IF;	
		UPDATE yvs_compta_notif_reglement_vente SET date_update = current_date WHERE piece_vente = NEW.id;
		vente_ = NEW.vente;	           
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=OLD.id;		
		vente_ = OLD.vente;		    	 
	END IF;

	-- Mise à jour de l'échéancier
	SELECT INTO solde_ coalesce(SUM(montant), 0) FROM yvs_com_mensualite_facture_vente WHERE facture = vente_;
	SELECT INTO payer_ coalesce(SUM(montant), 0) FROM yvs_compta_caisse_piece_vente WHERE vente = vente_ AND statut_piece = 'P';
	IF(payer_ <= 0 OR solde_ <= 0)THEN
		UPDATE yvs_com_mensualite_facture_vente SET etat = 'W', avance = 0 WHERE facture = vente_ AND etat != 'W';
	ELSIF(solde_ > payer_)THEN
		WHILE(payer_ > 0)
		LOOP
			FOR echeance_ IN SELECT * FROM yvs_com_mensualite_facture_vente WHERE facture = vente_ ORDER BY date_reglement ASC
			LOOP
				IF(payer_ >= echeance_.montant)THEN
					UPDATE yvs_com_mensualite_facture_vente SET etat = 'P', avance = montant WHERE id = echeance_.id;
				ELSE
					UPDATE yvs_com_mensualite_facture_vente SET etat = 'R', avance = (montant - payer_) WHERE id = echeance_.id;
				END IF;
				payer_ = payer_ - echeance_.montant;
			END LOOP;
		END LOOP;
	ELSE
		UPDATE yvs_com_mensualite_facture_vente SET etat = 'P', avance = montant WHERE facture = vente_ AND etat != 'P';
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_vente()
  OWNER TO postgres;

  
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
	nom_ character varying default '';
	prenom_ character varying default '';
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, dv.num_doc, dv.fournisseur, f.nom, f.prenom FROM yvs_compta_caisse_piece_achat pv 
			INNER JOIN yvs_com_doc_achats dv ON dv.id=pv.achat INNER JOIN yvs_base_fournisseur f ON f.id=dv.fournisseur INNER JOIN yvs_agences ag ON ag.id=dv.agence WHERE pv.id=NEW.id; 
								    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		IF(NEW.fournisseur IS NOT NULL) THEN
			id_tiers_=NEW.fournisseur;
		ELSE
			id_tiers_=line_.fournisseur;
		END IF;
		IF(line_.nom IS NOT NULL) THEN
			nom_=line_.nom;
		END IF;
		IF(line_.prenom IS NOT NULL) THEN
			prenom_=line_.prenom;
		END IF;
		--récupère le code tièrs de ce fournisseurs
		SELECT INTO id_tiers_ t.tiers FROM yvs_base_fournisseur t WHERE t.id=id_tiers_;
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, 'D',(nom_||' '||prenom_), NEW.model);
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, reference_externe=line_.num_doc, date_mvt=NEW.date_piece, model = NEW.model, 
				date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', societe=line_.societe, name_tiers=(nom_||' '||prenom_)
				WHERE table_externe='DOC_ACHAT' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, societe, author, caisse,caissier, tiers_interne, tiers_externe, mouvement, name_tiers, model)
				VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_ACHAT', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_,'D',(nom_||' '||prenom_), NEW.model);
		END IF;
		UPDATE yvs_compta_notif_reglement_achat SET date_update = current_date WHERE piece_achat = NEW.id;
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
  
  DROP TRIGGER mut_action_reglement_credit ON yvs_mut_reglement_credit;
    
  -- Function: update_echeancier()

-- DROP FUNCTION update_echeancier();

CREATE OR REPLACE FUNCTION update_echeancier()
  RETURNS trigger AS
$BODY$      
DECLARE
	etat varchar default null;
	credit_ record;
	montant_ double precision default 0;
	verse_ double precision default 0;
BEGIN
	select into credit_ montant from yvs_mut_credit where id = OLD.credit;
	SELECT INTO verse_ COALESCE(SUM(r.montant), 0) FROM yvs_mut_reglement_mensualite r INNER JOIN yvs_mut_mensualite m ON r.mensualite = m.id INNER JOIN yvs_mut_echellonage e ON m.echellonage = e.id WHERE r.montant IS NOT NULL AND e.credit = OLD.credit AND r.statut_piece = 'P';
	IF (credit_.montant = verse_) THEN
		NEW.etat = 'P';
		update yvs_mut_credit set statut_credit = 'P' where id = OLD.credit;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_echeancier()
  OWNER TO postgres;

  
  -- Function: delete_credit()

-- DROP FUNCTION delete_credit();

CREATE OR REPLACE FUNCTION delete_credit()
  RETURNS trigger AS
$BODY$    
DECLARE
	verse double precision;
BEGIN
	if(OLD.etat = 'V')then
		SELECT INTO verse SUM(y.montant) FROM yvs_mut_reglement_credit y WHERE y.montant IS NOT NULL AND y.statut_piece = 'P' AND y.credit = OLD.id;
		update yvs_mut_compte set solde = solde - OLD.montant + verse where id = OLD.compte;
	end if;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_credit()
  OWNER TO postgres;

  
  -- Function: insert_credit()

-- DROP FUNCTION insert_credit();

CREATE OR REPLACE FUNCTION insert_credit()
  RETURNS trigger AS
$BODY$    
DECLARE
	montant_ double precision;
	id_ bigint;
BEGIN
	if(NEW.etat = 'V')then
		update yvs_mut_compte set solde = solde + NEW.montant where id = NEW.compte;
	end if;
	--si le crédit est dans l'état accorder, on insère une ligne de retrait dans la table opérations
	--si le crédit est dasn l'état accorder, on insère une ligne de retrait dans la table opérations
	IF(NEW.statut_credit ='P') THEN
	--trouve le montant de remboursement total dans la table echéancier
		SELECT INTO montant_ ech.montant FROM  yvs_mut_echellonage ech INNER JOIN yvs_mut_credit cr ON cr.id=ech.credit
				 WHERE ech.etat='R' AND ech.credit=NEW.id limit 1;
		IF montant_ IS NOT NULL THEN
		--verifie si le crédit a déjà été payé
		SELECT INTO id_ op.id FROM yvs_mut_operation_compte op WHERE op.souce_reglement=NEW.id AND op.table_source='yvs_mut_credit' ;
		  IF id_ IS NULL THEN 
		        INSERT INTO yvs_mut_operation_compte(date_operation, montant, nature, compte, author, heure_operation, 
						      commentaire, automatique, epargne_mensuel, souce_reglement,table_source)
		        VALUES (NEW.date_credit, -montant_, 'Retrait', NEW.compte, NEW.author, current_time, 
			'', NEW.automatique, false, NEW.id,'yvs_mut_credit');
		 END IF;
		END IF;
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_credit()
  OWNER TO postgres;

  
  -- Function: update_credit()

-- DROP FUNCTION update_credit();

CREATE OR REPLACE FUNCTION update_credit()
  RETURNS trigger AS
$BODY$    
DECLARE
	caisse_ record;
	montant_ double precision;
	verse double precision;
id_ bigint;
BEGIN
	SELECT INTO verse SUM(y.montant) FROM yvs_mut_reglement_credit y WHERE y.montant IS NOT NULL AND y.statut_piece = 'P' AND y.credit = OLD.id;
	if(OLD.etat = 'V' or NEW.etat = 'V')then
		update yvs_mut_compte set solde = solde - verse where id = OLD.compte;
	end if;
	IF(NEW.statut_credit='P') THEN
	--trouve le montant de remboursement total dans la table echéancier
	SELECT INTO montant_ ech.montant FROM  yvs_mut_echellonage ech INNER JOIN yvs_mut_credit cr ON cr.id=ech.credit
				 WHERE ech.etat='R' AND ech.credit=NEW.id limit 1;
		IF montant_ IS NOT NULL THEN
		--verifie si le crédit a déjà été payé
		SELECT INTO id_ op.id FROM yvs_mut_operation_compte op WHERE op.souce_reglement=NEW.id AND op.table_source='yvs_mut_credit' ;
		  IF id_ IS NULL THEN 
		        INSERT INTO yvs_mut_operation_compte(date_operation, montant, nature, compte, author, heure_operation, 
						      commentaire, automatique, epargne_mensuel, souce_reglement,table_source)
		        VALUES (NEW.date_credit, -montant_, 'Retrait', NEW.compte, NEW.author, current_time, 
			'', NEW.automatique, false, NEW.id,'yvs_mut_credit');
		 END IF;
		END IF;
	END IF;
	return NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_credit()
  OWNER TO postgres;
  
  -- Function: mut_action_reglement_mensualite()

-- DROP FUNCTION mut_action_reglement_mensualite();

CREATE OR REPLACE FUNCTION mut_action_reglement_mensualite()
  RETURNS trigger AS
$BODY$    
DECLARE
	id_credit BIGINT;
	id_echeance BIGINT;
	echeance_ RECORD;
	mensualite_ BIGINT;
	solde_ double precision default 0;
	payer_ double precision default 0;
	
	avalise_ RECORD;
	credit_ RECORD;
	sum_avalise double precision;
	montant_ double precision;
	mens_ RECORD;
	echeancier_ RECORD;
	
	action_ character varying default '';
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		 
	END IF;         
	IF(action_='INSERT') THEN	
		SELECT INTO mens_ m.* FROM yvs_mut_mensualite m WHERE m.id=NEW.mensualite;
		--Calcul les montant d'avalise libéré par ce remboursement si éventuellement il y en a
			--recherche le crédit concerné par le règlement
		SELECT INTO credit_ cr.* FROM  yvs_mut_credit cr INNER JOIN yvs_mut_echellonage ech ON ech.credit=cr.id
								  INNER JOIN yvs_mut_mensualite me ON me.echellonage=ech.id
						 WHERE me.id=NEW.mensualite;
			SELECT INTO sum_avalise SUM(a.montant) FROM yvs_mut_avalise_credit a WHERE a.credit=credit_.id;
		IF(sum_avalise IS NOT NULL) THEN
			FOR avalise_ IN SELECT * FROM  yvs_mut_avalise_credit  WHERE credit=credit_.id
			LOOP
			    IF(avalise_.montant>=avalise_.montant_libere) THEN   -- si l'avalise n'est pas encore complètement couverte
			       montant_=(avalise_.montant/sum_avalise)*NEW.montant; --proportion libéré par le règlement en cours
			       IF(montant_ IS NOT NULL) THEN
				     IF ((montant_ + avalise_.montant_libere)>avalise_.montant) THEN
					UPDATE yvs_mut_avalise_credit SET montant_libere=avalise_.montant WHERE id=avalise_.id;
				     ELSE
					UPDATE yvs_mut_avalise_credit SET montant_libere=montant_libere + montant_ WHERE id=avalise_.id;
				     END IF;			     
				END IF;
			    END IF;
			END LOOP;		
		END IF;
		mensualite_ = NEW.mensualite;
	ELSIF (action_='UPDATE') THEN 	      
		mensualite_ = NEW.mensualite;	           
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 		
		mensualite_ = OLD.mensualite;		    	 
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_reglement_mensualite()
  OWNER TO postgres;

-- Function: mut_equilibre_credit(bigint)

-- DROP FUNCTION mut_equilibre_credit(bigint);

CREATE OR REPLACE FUNCTION mut_equilibre_credit(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	credit_ double precision default 0;
	reglement_ double precision default 0;

	line_ RECORD;

BEGIN
	SELECT INTO credit_ COALESCE(montant, 0) FROM yvs_mut_credit WHERE id = id_;
	IF(credit_ IS NOT NULL AND credit_ >0)THEN
		-- Equilibrer le statut de paiement du credit
		SELECT INTO reglement_ COALESCE(SUM(montant), 0) FROM yvs_mut_reglement_credit WHERE montant IS NOT NULL AND credit = id_ AND statut_piece = 'P';
		IF(credit_ <= reglement_)THEN
			UPDATE yvs_mut_credit SET statut_paiement = 'P' WHERE id = id_;
		ELSIF(reglement_ > 0)THEN
			UPDATE yvs_mut_credit SET statut_paiement = 'R' WHERE id = id_;
		ELSE
			UPDATE yvs_mut_credit SET statut_paiement = 'W' WHERE id = id_;
		END IF;
		
		-- Equilibrer le statut de remboursement du credit
		SELECT INTO reglement_ COALESCE(SUM(r.montant), 0) FROM yvs_mut_reglement_mensualite r INNER JOIN yvs_mut_mensualite m ON r.mensualite = m.id INNER JOIN yvs_mut_echellonage e ON m.echellonage = e.id WHERE r.montant IS NOT NULL AND e.credit = id_ AND r.statut_piece = 'P';
		IF(credit_ <= reglement_)THEN
			UPDATE yvs_mut_credit SET statut_credit = 'P' WHERE id = id_;
		ELSIF(reglement_ > 0)THEN
			UPDATE yvs_mut_credit SET statut_credit = 'R' WHERE id = id_;
		ELSE
			UPDATE yvs_mut_credit SET statut_credit = 'W' WHERE id = id_;
		END IF;

		-- Equilibrer le statut de reglement des mensualites
		FOR line_ IN SELECT m.id, (m.montant + COALESCE(m.montant_penalite, 0)) AS montant FROM yvs_mut_mensualite m INNER JOIN yvs_mut_echellonage e ON m.echellonage = e.id WHERE m.montant IS NOT NULL AND e.credit = id_
		LOOP
			SELECT INTO reglement_ COALESCE(SUM(montant), 0) FROM yvs_mut_reglement_mensualite WHERE montant IS NOT NULL AND mensualite = line_.id AND statut_piece = 'P';
			IF(line_.montant = reglement_)THEN
				UPDATE yvs_mut_mensualite SET etat = 'P' WHERE id = line_.id;
			ELSIF(reglement_ > 0)THEN
				UPDATE yvs_mut_mensualite SET etat = 'R' WHERE id = line_.id;
			ELSE
				UPDATE yvs_mut_mensualite SET etat = 'W' WHERE id = line_.id;
			END IF;
		END LOOP;

		-- Equilibrer le statut de reglement des écheanciers
		FOR line_ IN SELECT e.id, e.montant FROM yvs_mut_echellonage e WHERE e.montant IS NOT NULL AND e.credit = id_
		LOOP
			SELECT INTO reglement_ COALESCE(SUM(r.montant), 0) FROM yvs_mut_reglement_mensualite r INNER JOIN yvs_mut_mensualite m ON r.mensualite = m.id WHERE r.montant IS NOT NULL AND m.echellonage = line_.id AND r.statut_piece = 'P';
			IF(line_.montant = reglement_)THEN
				UPDATE yvs_mut_echellonage SET etat = 'P' WHERE id = line_.id;
			ELSIF(reglement_ > 0)THEN
				UPDATE yvs_mut_echellonage SET etat = 'R' WHERE id = line_.id;
			ELSE
				UPDATE yvs_mut_echellonage SET etat = 'W' WHERE id = line_.id;
			END IF;
		END LOOP;
	END IF;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_equilibre_credit(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION mut_equilibre_credit(bigint) IS 'equilibre les statuts du crédits';

-- Function: com_et_objectif_by_periode(bigint, bigint)

-- DROP FUNCTION com_et_objectif_by_periode(bigint, bigint);

CREATE OR REPLACE FUNCTION mut_mensualite_echeance(IN echeance_ bigint)
  RETURNS TABLE(id bigint, date_echeance date, mensualite double precision, amortissement double precision, interet double precision, reste_payer double precision, capital_restant double precision, dette_restant double precision) AS
$BODY$
DECLARE 
	capital_ DOUBLE PRECISION DEFAULT 0;
	dette_ DOUBLE PRECISION DEFAULT 0;
	reglement_ DOUBLE PRECISION DEFAULT 0;
	ligne_ RECORD;
BEGIN
-- 	DROP TABLE IF EXISTS table_mensualite_echeance;
	CREATE TEMP TABLE IF NOT EXISTS table_mensualite_echeance(_id bigint, _date_echeance date, _mensualite double precision, _amortissement double precision, _interet double precision, _reste_payer double precision, _capital_restant double precision, _dette_restant double precision); 
	DELETE FROM table_mensualite_echeance;
	
	SELECT INTO capital_ COALESCE(c.montant, 0) FROM yvs_mut_credit c INNER JOIN yvs_mut_echellonage e ON e.credit = c.id WHERE e.id = echeance_;
	SELECT INTO dette_ COALESCE(e.montant, 0) FROM yvs_mut_echellonage e WHERE e.id = echeance_;
	FOR ligne_ IN SELECT * FROM yvs_mut_mensualite WHERE echellonage = echeance_ ORDER BY date_mensualite
	LOOP
		SELECT INTO reglement_ COALESCE(SUM(r.montant),0) FROM yvs_mut_reglement_mensualite r WHERE r.mensualite = ligne_.id AND r.montant IS NOT NULL AND r.statut_piece = 'P';
		IF(capital_ > 0)THEN
			capital_ = capital_ - COALESCE(ligne_.amortissement, 0);
		END IF;
		IF(dette_ > 0)THEN
			dette_ = dette_ - COALESCE(ligne_.montant, 0);
		END IF;
		reglement_ = ligne_.montant - reglement_;
		IF(reglement_ < 0)THEN
			reglement_ = 0;
		END IF;
		INSERT INTO table_mensualite_echeance VALUES(ligne_.id, ligne_.date_mensualite, ligne_.montant, ligne_.amortissement, ligne_.interet, reglement_, capital_, dette_);
	END LOOP;
	return QUERY SELECT * FROM table_mensualite_echeance ORDER BY _date_echeance;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION mut_mensualite_echeance(bigint)
  OWNER TO postgres;
