-- Function: compta_insert_piece_commission()

-- DROP FUNCTION compta_insert_piece_commission();

CREATE OR REPLACE FUNCTION compta_insert_piece_commission()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	commission_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';

	avance_ double precision default 0;
	frais_ double precision default 0;
	
	EXEC_ BOOLEAN DEFAULT get_config('EXECUTE_MVT_CAISSE');
	
BEGIN	
	IF(EXEC_) THEN	
		action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
		--find agence concernÃ©
		IF(action_='INSERT' OR action_='UPDATE') THEN
			commission_ = NEW.commission;
		ELSE
			commission_ = OLD.commission;
		END IF;      
		SELECT INTO line_  e.agence, m.numero, e.tiers, e.nom, e.prenom, m.montant FROM yvs_compta_caisse_piece_commission pm 
			INNER JOIN yvs_com_commission_commerciaux m ON m.id = pm.commission INNER JOIN yvs_com_comerciale e ON e.id = m.commerciaux WHERE m.id = commission_ LIMIT 1; 
									
		IF(line_.agence IS NULL) THEN
			SELECT INTO line_.agence  ua.agence FROM yvs_users_agence ua WHERE ua.id=NEW.author;
		END IF;
		IF(line_.nom IS NOT NULL) THEN
			nom_=line_.nom;
		END IF;
		IF(line_.prenom IS NOT NULL) THEN
			prenom_=line_.prenom;
		END IF;   
		
		IF(action_='INSERT') THEN
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, caissier, statut_piece, 
													agence, author, caisse, mouvement, tiers_employe, tiers_externe, name_tiers, model)
				   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'COMMISSION', NEW.montant,line_.numero, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement, NEW.caissier, NEW.statut_piece, 
						   line_.agence,NEW.author, NEW.caisse, 'D',NULL,line_.tiers, (nom_ ||' '||prenom_), NEW.model);
		ELSIF (action_='UPDATE') THEN 
			SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='COMMISSION' AND id_externe=NEW.id;
			IF(id_el_ IS NOT NULL) THEN 	
				UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, reference_externe=line_.numero, date_mvt=NEW.date_piece, 
					date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement='D', agence=line_.agence,
					tiers_externe=line_.tiers, name_tiers=(nom_ ||' '||prenom_), model = NEW.model
					WHERE table_externe='COMMISSION' AND id_externe= NEW.id;
			ELSE
				INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, caissier, statut_piece, 
													agence, author, caisse, mouvement, tiers_employe, tiers_externe, name_tiers, model)
				   VALUES (NEW.numero_piece, NEW.id, NEW.note, 'COMMISSION', NEW.montant,line_.numero, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement, NEW.caissier, NEW.statut_piece, 
						   line_.agence,NEW.author, NEW.caisse, 'D',NULL,line_.tiers,(nom_ ||' '||prenom_), NEW.model);
			END IF;
		ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 
			DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='COMMISSION' AND id_externe=OLD.id;
		END IF;
		frais_ = COALESCE(line_.montant, 0);
		SELECT INTO avance_ SUM(y.montant) FROM yvs_compta_caisse_piece_commission y WHERE y.statut_piece = 'P' AND y.commission = commission_;
		IF(COALESCE(avance_, 0) > 0)THEN
			IF(COALESCE(avance_, 0) >= COALESCE(frais_, 0))THEN
				UPDATE yvs_com_commission_commerciaux SET statut_regle = 'P' WHERE id = commission_;
			ELSE
				UPDATE yvs_com_commission_commerciaux SET statut_regle = 'R' WHERE id = commission_;
			END IF;
		ELSE
			UPDATE yvs_com_commission_commerciaux SET statut_regle = 'W' WHERE id = commission_;
		END IF;
	END IF;
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_insert_piece_commission()
  OWNER TO postgres;
