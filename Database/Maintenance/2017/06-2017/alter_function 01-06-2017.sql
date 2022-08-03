DROP TRIGGER insert_ ON yvs_mut_reglement_mensualite;
DROP FUNCTION insert_reglement_mensualite();
DROP TRIGGER delete_ ON yvs_mut_reglement_mensualite;
DROP FUNCTION delete_reglement_mensualite();
DROP TRIGGER update_ ON yvs_mut_reglement_mensualite;
DROP FUNCTION update_reglement_mensualite();

-- Function: mut_action_reglement()

-- DROP FUNCTION mut_action_reglement();

CREATE OR REPLACE FUNCTION mut_action_reglement()
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
	
	SELECT INTO id_echeance echellonage FROM yvs_mut_mensualite WHERE id = mensualite_;
	SELECT INTO id_credit credit FROM yvs_mut_echellonage WHERE id = id_echeance;
	
	-- Mise à jour de l'échéancier
	SELECT INTO solde_ coalesce(SUM(montant), 0) FROM yvs_mut_mensualite WHERE echellonage = id_echeance;
	SELECT INTO payer_ coalesce(SUM(y.montant), 0) FROM yvs_mut_reglement_mensualite y INNER JOIN yvs_mut_mensualite m ON y.mensualite = m.id WHERE m.echellonage = id_echeance AND statut_piece = 'P';
	IF(payer_ <= 0 OR solde_ <= 0)THEN
		UPDATE yvs_mut_mensualite SET etat = 'W', montant_verse = 0 WHERE echellonage = id_echeance AND etat != 'W';
	ELSIF(solde_ > payer_)THEN
		UPDATE yvs_mut_echellonage SET etat = 'R', montant_verse = payer_ WHERE id = id_echeance;
		WHILE(payer_ > 0)
		LOOP	
			FOR echeance_ IN SELECT * FROM yvs_mut_mensualite WHERE echellonage = id_echeance ORDER BY date_mensualite ASC
			LOOP
				IF(payer_ >= echeance_.montant)THEN
					UPDATE yvs_mut_mensualite SET etat = 'P', montant_verse = montant WHERE id = echeance_.id;
				ELSE
					UPDATE yvs_mut_mensualite SET etat = 'R', montant_verse = payer_ WHERE id = echeance_.id;
				END IF;
				payer_ = payer_ - echeance_.montant;
				IF(payer_ < 0)THEN
					exit;
				END IF;
			END LOOP;
		END LOOP;
		UPDATE yvs_mut_credit SET statut_credit = 'R' WHERE id = id_credit;	
	ELSE
		UPDATE yvs_mut_mensualite SET etat = 'P', montant_verse = montant WHERE echellonage = id_echeance AND etat != 'P';
		UPDATE yvs_mut_echellonage SET etat = 'P', montant_verse = montant WHERE id = id_echeance;		
		UPDATE yvs_mut_credit SET statut_credit = 'P' WHERE id = id_credit;		
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mut_action_reglement()
  OWNER TO postgres;
  
CREATE TRIGGER mut_action_reglement
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_mut_reglement_mensualite
  FOR EACH ROW
  EXECUTE PROCEDURE mut_action_reglement();