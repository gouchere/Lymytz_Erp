ALTER TABLE yvs_mut_interet DROP COLUMN exercice;
ALTER TABLE yvs_mut_mensualite ADD COLUMN date_reglement date;


ALTER TABLE yvs_mut_mouvement_caisse ADD COLUMN in_solde_caisse boolean;
ALTER TABLE yvs_mut_mouvement_caisse ALTER COLUMN in_solde_caisse SET DEFAULT false;

ALTER TABLE yvs_mut_interet ADD COLUMN statut_paiement character varying;
ALTER TABLE yvs_mut_interet ALTER COLUMN statut_paiement SET DEFAULT 'W'::character varying;

ALTER TABLE yvs_mut_reglement_prime DROP COLUMN exercice;

ALTER TABLE yvs_mut_reglement_prime ADD COLUMN statut_paiement character varying;
ALTER TABLE yvs_mut_reglement_prime ALTER COLUMN statut_paiement SET DEFAULT 'W'::character varying;

ALTER TABLE yvs_mut_interet ADD COLUMN compte bigint;
ALTER TABLE yvs_mut_interet
  ADD CONSTRAINT yvs_mut_interet_compte_fkey FOREIGN KEY (compte)
      REFERENCES yvs_mut_compte (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE yvs_mut_reglement_prime ADD COLUMN compte bigint;
ALTER TABLE yvs_mut_reglement_prime
  ADD CONSTRAINT yvs_mut_reglement_prime_compte_fkey FOREIGN KEY (compte)
      REFERENCES yvs_mut_compte (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  
CREATE OR REPLACE FUNCTION mut_action_on_paiement_interet()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ CHARACTER VARYING;
	table_name_ CHARACTER VARYING DEFAULT 'REGLEMENT_INTERET';
	
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		--SELECT INTO tiers_ m.id, CONCAT(e.nom, ' ', e.prenom) AS nom, r.reference FROM yvs_mut_credit r INNER JOIN yvs_mut_compte c ON r.compte = c.id INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE r.id = NEW.credit;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
	    -- Récupère le document
		IF(NEW.statut_paiement='P' AND NEW.compte IS NOT NULL) THEN 			
				--Insert l'opération de dépôt dans la table opération_compte
				PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);
		END IF;
	ELSIF (action_='UPDATE') THEN 
		IF(NEW.statut_paiement='P')THEN
				PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);	
		ELSE
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = NEW.id;	
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN		
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = OLD.id;	
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_paiement_interet()
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION mut_action_on_paiement_prime()
  RETURNS trigger AS
$BODY$    
DECLARE
	action_ CHARACTER VARYING;
	table_name_ CHARACTER VARYING DEFAULT 'REGLEMENT_PRIME';
	
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		--SELECT INTO tiers_ m.id, CONCAT(e.nom, ' ', e.prenom) AS nom, r.reference FROM yvs_mut_credit r INNER JOIN yvs_mut_compte c ON r.compte = c.id INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE r.id = NEW.credit;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
	    -- Récupère le document
		IF(NEW.statut_paiement='P' AND NEW.compte IS NOT NULL) THEN 			
				--Insert l'opération de dépôt dans la table opération_compte
				PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);
		END IF;
	ELSIF (action_='UPDATE') THEN 
		IF(NEW.statut_piece='P')THEN
				PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);	
		ELSE
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = NEW.id;	
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN		
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = OLD.id;	
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_paiement_prime()
  OWNER TO postgres;

-- Function: mut_action_on_reglement_credit()

-- DROP FUNCTION mut_action_on_reglement_credit();

CREATE OR REPLACE FUNCTION mut_insert_into_operation_compte(IN id_ bigint, table_ character varying)
  RETURNS boolean AS
$BODY$    
DECLARE	
	infos_cr_ record;
	libelle_ character varying;
	mouvement_ character varying;
	nature_ character varying;
	reference_ character varying;
	line_ record;
	old_id_ bigint;
	periode_ bigint;
	caisse_ bigint;
BEGIN	
	CASE table_
		WHEN 'REGLEMENT_CREDIT' THEN
			SELECT INTO line_ * FROM yvs_mut_reglement_credit WHERE id=id_;
			SELECT INTO infos_cr_ m.id AS employe, CONCAT(e.nom, ' ', e.prenom) AS nom, cr.reference FROM yvs_mut_credit cr INNER JOIN yvs_mut_compte c ON cr.compte = c.id 
																											 INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id 
																											 INNER JOIN yvs_grh_employes e ON m.employe = e.id 
																											 WHERE cr.id = line_.credit;
			libelle_='Paiement du crédit '||infos_cr_.reference;
			mouvement_='D';
			nature_='DEPOT';
			caisse_=line_.caisse;
			reference_ =infos_cr_.reference;
			SELECT INTO periode_ p.id FROM yvs_mut_periode_exercice p WHERE line_.date_reglement BETWEEN p.date_debut AND p.date_fin;
		WHEN 'REGLEMENT_MENSUALITE' THEN
			SELECT INTO line_ rm.*, me.date_mensualite, cr.reference AS reference_credit FROM yvs_mut_reglement_mensualite rm INNER JOIN yvs_mut_mensualite me ON me.id=rm.mensualite 
																							INNER JOIN yvs_mut_echellonage ech ON ech.id=me.echellonage
																							INNER JOIN yvs_mut_credit cr ON cr.id=ech.credit
																							WHERE rm.id=id_;
			SELECT INTO infos_cr_ m.id AS employe, CONCAT(e.nom, ' ', e.prenom) AS nom FROM yvs_mut_compte c INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id 
																											 INNER JOIN yvs_grh_employes e ON m.employe = e.id 
																											 WHERE c.id = line_.compte;
			libelle_='Remboursement de la mensualité du '||line_.date_mensualite::character varying || ' CR: '||line_.reference_credit;
			mouvement_='R';
			nature_='RETRAIT';
			caisse_=line_.caisse;
			reference_='';		
			SELECT INTO periode_ p.id FROM yvs_mut_periode_exercice p WHERE line_.date_reglement BETWEEN p.date_debut AND p.date_fin;
		WHEN 'REGLEMENT_INTERET' THEN
			SELECT INTO line_  ri.id,ri.date_interet date_reglement, ri.montant, ri.compte,ri.author, ri.periode FROM yvs_mut_interet ri WHERE ri.id=id_;
			libelle_='PAIEMENT DE L''INTERET DU '||line_.date_reglement::character varying;
			mouvement_='D';
			nature_='DEPOT';
			reference_='';		
			periode_ =line_.periode;
			caisse_=null;
		WHEN 'REGLEMENT_PRIME' THEN
			SELECT INTO line_ ri.id, ri.date_prime date_reglement, ri.montant, ri.compte,ri.author, ri.periode FROM yvs_mut_reglement_prime ri WHERE ri.id=id_;
			libelle_='PAIEMENT DE LA PRIME DE GESTION '||line_.date_reglement::character varying;
			mouvement_='D';
			nature_='DEPOT';
			reference_='';		
			periode_ =line_.periode;
			caisse_=null;
	END CASE;
		SELECT INTO old_id_ id FROM yvs_mut_operation_compte WHERE table_source=table_ AND souce_reglement=id_;
		IF(old_id_ IS NULL OR old_id_<=0) THEN
			INSERT INTO yvs_mut_operation_compte(date_operation, montant, nature, compte, heure_operation, commentaire, automatique, epargne_mensuel, souce_reglement, table_source, 
											author, periode,caisse, reference_operation, date_update, date_save,sens_operation)
										VALUES (line_.date_reglement, line_.montant, nature_, line_.compte, current_time,libelle_, true,false,id_,table_, 
											line_.author,periode_,caisse_,reference_,current_date,current_date,mouvement_);
		ELSE
			UPDATE yvs_mut_operation_compte
					   SET date_operation=line_.date_reglement, montant=line_.montant, nature=nature_, compte=line_.compte, 
						    epargne_mensuel=false, souce_reglement=line_.id, table_source=table_, caisse=caisse_, periode=periode_,
						   date_update=current_date, sens_operation=mouvement_, commentaire=libelle_
						WHERE id = old_id_;
		END IF;
		RETURN true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_insert_into_operation_compte(bigint, character varying)
  OWNER TO postgres;

  
-- Function: mut_action_on_reglement_mensualite()

-- DROP FUNCTION mut_action_on_reglement_mensualite();

CREATE OR REPLACE FUNCTION mut_action_on_reglement_mensualite()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	credit_ RECORD;
	action_ CHARACTER VARYING;
	mouvement_ CHARACTER VARYING DEFAULT 'R';
	table_name_ CHARACTER VARYING DEFAULT 'REGLEMENT_MENSUALITE';
	existe_ BIGINT DEFAULT 0;
	sum_avalise double precision ;
	montant_ double precision ;
	avalise_ record;
	
BEGIN	
		
	action_ = TG_OP; --INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO tiers_ m.id, CONCAT(e.nom, ' ', e.prenom) AS nom, r.reference FROM yvs_mut_mensualite s INNER JOIN yvs_mut_echellonage h ON s.echellonage = h.id
			INNER JOIN yvs_mut_credit r ON h.credit = r.id INNER JOIN yvs_mut_compte c ON r.compte = c.id INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id 
			INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE s.id = NEW.mensualite;
	END IF;
	--find societe concerné  
	CASE action_      
	WHEN 'INSERT' THEN
	    -- Récupère le document
		IF(NEW.statut_piece='P')THEN
			IF(NEW.mode_paiement='ESPECE') THEN
				PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);
			ELSE
				--Insert l'opération de dépôt dans la table opération_compte
				PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);
			END IF;
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
		END IF;
	WHEN 'UPDATE' THEN 
		IF(NEW.statut_piece='P')THEN
			IF(NEW.mode_paiement='ESPECE') THEN
					PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);
			ELSE
					--Insert l'opération de dépôt dans la table opération_compte
					PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);
			END IF;
		ELSE
			DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = NEW.id;	
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = NEW.id;
		END IF;
	WHEN 'DELETE' THEN 
			DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;	
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = OLD.id;
	WHEN 'TRONCATE'THEN 	
			DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;	
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = OLD.id;	
	END CASE;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_reglement_mensualite()
  OWNER TO postgres;

  
-- Function: mut_action_on_reglement_credit()

-- DROP FUNCTION mut_action_on_reglement_credit();

CREATE OR REPLACE FUNCTION mut_action_on_reglement_credit()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	tiers_ RECORD;
	caisse_ RECORD;
	compte_ RECORD;
	action_ CHARACTER VARYING;
	mouvement_ CHARACTER VARYING DEFAULT 'D';
	table_name_ CHARACTER VARYING DEFAULT 'REGLEMENT_CREDIT';
	
BEGIN	
	action_ = TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		--SELECT INTO caisse_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
		--SELECT INTO compte_ e.code_users AS caissier FROM yvs_mut_caisse c INNER JOIN yvs_mut_mutualiste m ON c.responsable = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE c.id = NEW.caisse;
		SELECT INTO tiers_ m.id, CONCAT(e.nom, ' ', e.prenom) AS nom, r.reference FROM yvs_mut_credit r INNER JOIN yvs_mut_compte c ON r.compte = c.id INNER JOIN yvs_mut_mutualiste m ON c.mutualiste = m.id INNER JOIN yvs_grh_employes e ON m.employe = e.id WHERE r.id = NEW.credit;
	END IF;
	--find societe concerné        
	IF(action_='INSERT') THEN
	    -- Récupère le document
		IF(NEW.statut_piece='P') THEN 
			IF(NEW.mode_paiement='ESPECE') THEN
				PERFORM  mut_insert_into_mouv_caisse(NEW.id, table_name_);
			ELSE
				--Insert l'opération de dépôt dans la table opération_compte
				PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);

			END IF;
		END IF;
	ELSIF (action_='UPDATE') THEN 
		IF(NEW.statut_piece='P')THEN
				IF(NEW.mode_paiement='ESPECE') THEN 
					PERFORM   mut_insert_into_mouv_caisse(NEW.id, table_name_);
				ELSE
					PERFORM  mut_insert_into_operation_compte(NEW.id, table_name_);	
				END iF;	
		ELSE
			DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = NEW.id;
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = NEW.id;	
		END IF;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		IF(OLD.mode_paiement='ESPECE') THEN 	
			DELETE FROM yvs_mut_mouvement_caisse WHERE table_externe = table_name_ AND id_externe = OLD.id;	
		ELSE		
			DELETE FROM yvs_mut_operation_compte WHERE table_source = table_name_ AND souce_reglement = OLD.id;	
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_on_reglement_credit()
  OWNER TO postgres;

  
