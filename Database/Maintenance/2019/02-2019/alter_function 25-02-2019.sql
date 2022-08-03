-- Function: insert_droit(character varying, character varying, bigint, bigint, character varying, character varying)
-- DROP FUNCTION insert_droit(character varying, character varying, bigint, bigint, character varying, character varying);
CREATE OR REPLACE FUNCTION insert_droit(reference_ character varying, designation_ character varying, parent_ bigint, author_ bigint, grade_ character varying, type_ character varying)
  RETURNS boolean AS
$BODY$
DECLARE 
	droit_ BIGINT;
	niveaux_ BIGINT;
	acces_ BIGINT;
BEGIN
	IF(type_ = 'M')THEN
		SELECT INTO droit_ id FROM yvs_module WHERE reference = reference_;
		IF(COALESCE(droit_, 0) < 1)THEN
			INSERT INTO yvs_module(libelle, description, reference, author) VALUES (designation_, designation_, reference_, author_);
		END IF;
		IF(COALESCE(grade_, '') NOT IN ('', ' '))THEN
			SELECT INTO droit_ id FROM yvs_module WHERE reference = reference_;
			IF(COALESCE(droit_, 0) > 0)THEN
				FOR niveaux_ IN SELECT id FROM yvs_niveau_acces WHERE grade::character varying in (select val from regexp_split_to_table(grade_, ',') val)
				LOOP
					SELECT INTO acces_ id FROM yvs_autorisation_module WHERE module = droit_ AND niveau_acces = niveaux_;
					IF(COALESCE(acces_, 0) < 1)THEN
						INSERT INTO yvs_autorisation_module(niveau_acces, module, acces, author) VALUES (niveaux_, droit_, true, author_);
					ELSE
						UPDATE yvs_autorisation_module SET acces = TRUE WHERE id = acces_;
					END IF;
				END LOOP;
			END IF;
		END IF;
		RETURN TRUE;
	ELSIF(type_ = 'P' AND COALESCE(parent_, 0) > 0)THEN
		SELECT INTO droit_ id FROM yvs_page_module WHERE reference = reference_;
		IF(COALESCE(droit_, 0) < 1)THEN
			INSERT INTO yvs_page_module(libelle, reference, description, module, author)VALUES (designation_, reference_, designation_, parent_, author_);
		END IF;
		IF(COALESCE(grade_, '') NOT IN ('', ' '))THEN
			SELECT INTO droit_ id FROM yvs_page_module WHERE reference = reference_;
			IF(COALESCE(droit_, 0) > 0)THEN
				FOR niveaux_ IN SELECT id FROM yvs_niveau_acces WHERE grade::character varying in (select val from regexp_split_to_table(grade_, ',') val)
				LOOP
					SELECT INTO acces_ id FROM yvs_autorisation_page_module WHERE page_module = droit_ AND niveau_acces = niveaux_;
					IF(COALESCE(acces_, 0) < 1)THEN
						INSERT INTO yvs_autorisation_page_module(niveau_acces, page_module, acces, author) VALUES (niveaux_, droit_, true, author_);
					ELSE
						UPDATE yvs_autorisation_page_module SET acces = TRUE WHERE id = acces_;
					END IF;
				END LOOP;
			END IF;
		END IF;
		RETURN TRUE;
	ELSIF(COALESCE(parent_, 0) > 0)THEN
		SELECT INTO droit_ id FROM yvs_ressources_page WHERE reference_ressource = reference_;
		IF(COALESCE(droit_, 0) < 1)THEN
			INSERT INTO yvs_ressources_page(reference_ressource, libelle, description, page_module, author) VALUES (reference_, designation_, designation_, parent_, author_);
		END IF;
		IF(COALESCE(grade_, '') NOT IN ('', ' '))THEN
			SELECT INTO droit_ id FROM yvs_ressources_page WHERE reference_ressource = reference_;
			IF(COALESCE(droit_, 0) > 0)THEN
				FOR niveaux_ IN SELECT id FROM yvs_niveau_acces WHERE grade::character varying in (select val from regexp_split_to_table(grade_, ',') val)
				LOOP
					SELECT INTO acces_ id FROM yvs_autorisation_ressources_page WHERE ressource_page = droit_ AND niveau_acces = niveaux_;
					IF(COALESCE(acces_, 0) < 1)THEN
						INSERT INTO yvs_autorisation_ressources_page(niveau_acces, ressource_page, acces, author) VALUES (niveaux_, droit_, true, author_);
					ELSE
						UPDATE yvs_autorisation_ressources_page SET acces = TRUE WHERE id = acces_;
					END IF;
				END LOOP;
			END IF;
		END IF;
		RETURN TRUE;
	END IF;
	RETURN FALSE;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_droit(character varying, character varying, bigint, bigint, character varying, character varying)
  OWNER TO postgres;
