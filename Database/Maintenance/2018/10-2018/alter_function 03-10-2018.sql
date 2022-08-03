-- Function: alter_update_reference(bigint, date, bigint, character varying, character varying)

-- DROP FUNCTION alter_update_reference(bigint, date, bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION alter_update_reference(societe_ bigint, date_ date, parent_ bigint, element_ character varying, type_ character varying)
  RETURNS boolean AS
$BODY$   
DECLARE 
	model_ RECORD;
	document_ RECORD;

	compteur_ INTEGER DEFAULT 1;
	i_ INTEGER DEFAULT 0;

	query_ CHARACTER VARYING;
	modele_ CHARACTER VARYING;
	reference_ CHARACTER VARYING;

	add_date_ BOOLEAN DEFAULT FALSE;

	date_save_ DATE;
BEGIN
	SELECT INTO model_ y.* FROM yvs_base_modele_reference y INNER JOIN yvs_base_element_reference e ON y.element = e.id WHERE y.societe = societe_ AND e.designation = element_ ;
	IF(model_.id IS NOT NULL AND model_.id > 0)THEN
		modele_ = model_.prefix || model_.separateur;
		IF((COALESCE(parent_, 0) > 0) AND type_ IS NOT NULL)THEN
			IF(type_ = 'AGENCE')THEN
				SELECT INTO reference_ abbreviation FROM yvs_agences WHERE id = parent_;
			ELSIF(type_ = 'DEPOT')THEN
				SELECT INTO reference_ abbreviation FROM yvs_base_depots WHERE id = parent_;
			ELSIF(type_ = 'JOURNAL')THEN
				SELECT INTO reference_ code_journal FROM yvs_compta_journaux WHERE id = parent_;
			ELSIF(type_ = 'POINTVENTE')THEN
				SELECT INTO reference_ code FROM yvs_base_point_vente WHERE id = parent_;
			ELSIF(type_ = 'CAISSE')THEN
				SELECT INTO reference_ reference_caisse FROM yvs_mut_caisse WHERE id = parent_;
			ELSIF(type_ = 'TYPECREDIT')THEN
				SELECT INTO reference_ code FROM yvs_mut_type_credit WHERE id = parent_;
			ELSE
				
			END IF;
			IF(reference_ IS NOT NULL)THEN
				modele_ = modele_ || reference_ || model_.separateur;
			END IF;
		END IF;
		IF(model_.jour OR model_.mois OR model_.annee)THEN
			add_date_ = TRUE;
		END IF;
		IF(element_ = 'Operation Divers')THEN
			query_ = 'SELECT y.id, y.date_doc FROM yvs_compta_caisse_doc_divers y WHERE y.societe = '||COALESCE(societe_, 0);
			IF(date_ IS NOT NULL)THEN
				query_ = query_ || ' AND y.date_doc = '||QUOTE_LITERAL(date_);
			END IF;
			query_ = query_ || ' ORDER BY y.date_doc, y.id';	
			FOR document_ IN EXECUTE query_
			LOOP
				reference_ = modele_;
				IF(date_save_ != document_.date_doc)THEN
					compteur_ = 1;
				END IF;
				IF(model_.jour)THEN
					reference_ = reference_ || to_char(document_.date_doc ,'dd');
				END IF;
				IF(model_.mois)THEN
					reference_ = reference_ || to_char(document_.date_doc ,'MM');
				END IF;
				IF(model_.annee)THEN
					reference_ = reference_ || to_char(document_.date_doc ,'yy');
				END IF;
				IF(add_date_)THEN
					reference_ = reference_ || model_.separateur;
				END IF;

				FOR i_ IN 0..(model_.taille - (char_length(compteur_::TEXT)+1)) LOOP
					reference_ = reference_ || '0';					
				END LOOP;
				reference_ = reference_ || compteur_;				
				RAISE NOTICE 'MODELE : %', reference_;	
				UPDATE yvs_compta_caisse_doc_divers SET num_piece = reference_ WHERE id = document_.id;		
				compteur_ = compteur_ + 1;
				date_save_ = document_.date_doc;
			END LOOP;
		ELSIF(element_ = 'Piece Caisse')THEN
			query_ = 'SELECT y.id, y.date_mvt as date_doc, id_externe, table_externe FROM yvs_compta_mouvement_caisse y WHERE y.societe = '||COALESCE(societe_, 0);
			IF(date_ IS NOT NULL)THEN
				query_ = query_ || ' AND y.date_mvt = '||QUOTE_LITERAL(date_);
			END IF;
			query_ = query_ || ' ORDER BY y.date_mvt, y.id';
			FOR document_ IN EXECUTE query_
			LOOP
				reference_ = modele_;
				IF(date_save_ != document_.date_doc)THEN
					compteur_ = 1;
				END IF;
				IF(model_.jour)THEN
					reference_ = reference_ || to_char(document_.date_doc ,'dd');
				END IF;
				IF(model_.mois)THEN
					reference_ = reference_ || to_char(document_.date_doc ,'MM');
				END IF;
				IF(model_.annee)THEN
					reference_ = reference_ || to_char(document_.date_doc ,'yy');
				END IF;
				IF(add_date_)THEN
					reference_ = reference_ || model_.separateur;
				END IF;

				FOR i_ IN 0..(model_.taille - (char_length(compteur_::TEXT)+1)) LOOP
					reference_ = reference_ || '0';					
				END LOOP;
				reference_ = reference_ || compteur_;				
				RAISE NOTICE 'MODELE : %', reference_;	
				UPDATE yvs_compta_mouvement_caisse SET numero = reference_ WHERE id = document_.id;	
				IF(document_.table_externe = 'DOC_VIREMENT')THEN
					UPDATE yvs_compta_caisse_piece_virement SET numero_piece = reference_ WHERE id = document_.id_externe;	
				ELSIF(document_.table_externe = 'MISSION')THEN
					UPDATE yvs_compta_caisse_piece_mission SET numero_piece = reference_ WHERE id = document_.id_externe;	
				ELSIF(document_.table_externe = 'DOC_VENTE')THEN
					UPDATE yvs_compta_caisse_piece_vente SET numero_piece = reference_ WHERE id = document_.id_externe;	
				ELSIF(document_.table_externe = 'DOC_ACHAT')THEN
					UPDATE yvs_compta_caisse_piece_achat SET numero_piece = reference_ WHERE id = document_.id_externe;	
				ELSIF(document_.table_externe = 'DOC_DIVERS')THEN
					UPDATE yvs_compta_caisse_piece_divers SET num_piece = reference_ WHERE id = document_.id_externe;	
				END IF;
				compteur_ = compteur_ + 1;
				date_save_ = document_.date_doc;
			END LOOP;
		END IF;
		RETURN TRUE;
	END IF;
	RETURN FALSE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION alter_update_reference(bigint, date, bigint, character varying, character varying)
  OWNER TO postgres;
