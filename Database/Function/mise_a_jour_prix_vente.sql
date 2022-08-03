-- Function: mise_a_jour_prix_vente(bigint, bigint, bigint, bigint, bigint)

-- DROP FUNCTION mise_a_jour_prix_vente(bigint, bigint, bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION mise_a_jour_prix_vente(article_ bigint, unite_ bigint, client_ bigint, point_ bigint, categorie_ bigint)
  RETURNS integer AS
$BODY$   
DECLARE 	
	compteur_ INTEGER DEFAULT 0;
	
	prix_vente_ DOUBLE PRECISION DEFAULT 0;
	prix_min_ DOUBLE PRECISION DEFAULT 0;
	remise_ DOUBLE PRECISION DEFAULT 0;

	ligne_ BIGINT;
BEGIN
	IF(article_ IS NOT NULL AND article_ > 0)THEN
		IF((unite_ IS NOT NULL AND unite_ > 0) OR unite_ = -1)THEN
			IF(client_ IS NOT NULL AND client_ > 0)THEN
				IF(point_ IS NOT NULL AND point_ > 0)THEN
					IF(categorie_ IS NOT NULL AND categorie_ > 0)THEN
						prix_vente_ = (SELECT get_puv(article_, 0, 0, client_, 0, point_, current_date, unite_, false));
						prix_min_ = (SELECT get_puv(article_, 0, 0, client_, 0, point_, current_date, unite_, true));
						remise_ = (SELECT get_remise_vente(article_, 0, 0, client_, point_, current_date, unite_));
						IF(unite_ = -1)THEN
							SELECT INTO ligne_ y.id FROM yvs_com_prix_vente y WHERE y.article = article_ AND y.conditionnement IS NULL AND y.client = client_
								AND y.categorie = categorie_ AND y.point_vente = point_;
							IF(ligne_ IS NULL OR ligne_ < 1)THEN
								INSERT INTO yvs_com_prix_vente(article, conditionnement, client, categorie, point_vente, prix, remise, minimal)
											VALUES(article_, NULL, client_, categorie_, point_, prix_vente_, remise_, prix_min_);
							ELSE
								UPDATE yvs_com_prix_vente SET prix = prix_vente_, remise = remise_, minimal = prix_min_ WHERE id = ligne_;
							END IF;

						ELSE
							SELECT INTO ligne_ y.id FROM yvs_com_prix_vente y WHERE y.article = article_ AND y.conditionnement = unite_ AND y.client = client_
								AND y.categorie = categorie_ AND y.point_vente = point_;
							IF(ligne_ IS NULL OR ligne_ < 1)THEN
								INSERT INTO yvs_com_prix_vente(article, conditionnement, client, categorie, point_vente, prix, remise, minimal)
											VALUES(article_, unite_, client_, categorie_, point_, prix_vente_, remise_, prix_min_);
							ELSE
								UPDATE yvs_com_prix_vente SET prix = prix_vente_, remise = remise_, minimal = prix_min_ WHERE id = ligne_;
							END IF;
						END IF;
						compteur_ = 1;
					ELSE
						FOR categorie_ IN SELECT y.id FROM yvs_base_categorie_comptable y WHERE y.actif IS TRUE AND y.nature = 'VENTE'
						LOOP
							compteur_ = compteur_ + (SELECT mise_a_jour_prix_vente(article_, unite_, client_, point_, categorie_));
						END LOOP;
					END IF;
				ELSE
					FOR point_ IN SELECT y.id FROM yvs_base_point_vente y WHERE y.actif IS TRUE
					LOOP
						compteur_ = compteur_ + (SELECT mise_a_jour_prix_vente(article_, unite_, client_, point_, categorie_));
					END LOOP;
				END IF;
			ELSE
				FOR client_ IN SELECT y.id FROM yvs_com_client y WHERE y.actif IS TRUE
				LOOP
					compteur_ = compteur_ + (SELECT mise_a_jour_prix_vente(article_, unite_, client_, point_, categorie_));
				END LOOP;				
			END IF;
		ELSE
			SELECT INTO ligne_ COUNT(y.id) FROM yvs_base_conditionnement y WHERE y.article = article_;
			IF(ligne_ IS NOT NULL AND ligne_ > 0)THEN
				FOR unite_ IN SELECT y.id FROM yvs_base_conditionnement y WHERE y.article = article_
				LOOP
					compteur_ = compteur_ + (SELECT mise_a_jour_prix_vente(article_, unite_, client_, point_, categorie_));
				END LOOP;
			ELSE
				compteur_ = compteur_ + (SELECT mise_a_jour_prix_vente(article_, -1, client_, point_, categorie_));
			END IF;
		END IF;
	ELSE
		FOR article_ IN SELECT y.id FROM yvs_base_articles y WHERE y.actif IS TRUE
		LOOP
			compteur_ = compteur_ + (SELECT mise_a_jour_prix_vente(article_, unite_, client_, point_, categorie_));
		END LOOP;
	END IF;
	RETURN compteur_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION mise_a_jour_prix_vente(bigint, bigint, bigint, bigint, bigint)
  OWNER TO postgres;
