-- Function: base_maj_seuil_stocks(character varying, bigint, date,date,character varying,character varying )

-- DROP FUNCTION base_maj_seuil_stocks(character varying, bigint, date,date,character varying,character varying )

CREATE OR REPLACE FUNCTION base_maj_seuil_stocks(IN articles_ character varying, IN depot_ bigint, IN debut_ date, IN fin_ date, IN ids_exclude character varying, methode_ character varying)
  RETURNS TABLE (id bigint, ref_art character varying, designation character varying, categorie character varying, besoin_moy double precision, delai_moy double precision, besoin_max double precision, delai_max double precision, stock_sec double precision, point_cmde double precision, coefficient double precision, ecart_type double precision, conditionnement bigint, unite character varying) AS 
$BODY$
declare 	
	_delai_max Integer default 0;
	_delai_moy double precision default 0;
	_total_delai Integer default 0;
	_duree Integer default 0;
	_compteur Integer default 0;
	
	_qte_max double precision default 0;
	_qte_moy double precision default 0;
	_qte_total double precision default 0;	
   _ss double precision default 0;
   _pt_cmde double precision default 0;
   _coef double precision default 1.28;
   _coef_convert double precision default 0;   
   _et double precision default 0;
   _variance double precision default 0;
   
   line_ record;
   _article record;
   
   query_ character varying;
   next_ character varying;
   ref_unite_ character varying;
   id_cond_ bigint;
   article_ bigint;
   unite_ record;
   unite_cible record;
   pre_ date;
   first_date_ date;
   last_date_ date;
BEGIN 
	DROP TABLE IF EXISTS table_seuils_stock;
	CREATE TEMP TABLE IF NOT EXISTS table_seuils_stock(_id bigint, _ref_art character varying, _designation character varying, _categorie character varying, _besoin_moy double precision, _delai_moy double precision,_besoin_max double precision, _delai_max double precision, _stock_sec double precision, _point_cmde double precision, _coefficient double precision, _ecart_type double precision, _conditionnement bigint, _unite character varying);
	IF(COALESCE(articles_,'')!='') THEN		
		FOR next_ IN select val from regexp_split_to_table(articles_,',') val
			LOOP					
				article_=next_::bigint;	
				SELECT INTO _article * FROM yvs_base_articles a WHERE a.id=article_;	
				IF(COALESCE(ids_exclude,'')='')	THEN
					ids_exclude='0';
				END IF;
				IF(_article.categorie='MP') THEN
					query_='SELECT SUM(quantite) AS quantite, m.date_doc, m.conditionnement  FROM yvs_base_mouvement_stock m WHERE m.article='||article_||' AND m.date_doc BETWEEN '''||debut_ ||''' AND '''||fin_||
							'''AND m.mouvement=''S'' AND m.table_externe=''yvs_prod_of_suivi_flux'' AND m.id_externe::character varying NOT IN (SELECT val FROM regexp_split_to_table('''||ids_exclude||''','','') val)
							GROUP BY date_doc, m.conditionnement
							ORDER BY m.date_doc, m.conditionnement';
				ELSIF(_article.categorie='MARCHANDISE') THEN
					query_='SELECT SUM(quantite) AS quantite, m.date_doc, m.conditionnement  FROM yvs_base_mouvement_stock m WHERE m.article='||article_||' AND m.date_doc BETWEEN '''||debut_ ||''' AND '''||fin_||
							'''AND m.mouvement=''S'' AND m.table_externe=''yvs_com_contenu_doc_vente'' AND m.id_externe::character varying NOT IN (SELECT val FROM regexp_split_to_table('''||ids_exclude||''','','') val)
							GROUP BY date_doc, m.conditionnement
							ORDER BY m.date_doc, m.conditionnement';
				ELSE
					query_='';
				END IF;
				--Initialisation des variable à chaque boucles sur l'articles
				_qte_max=0;_qte_moy=0;_qte_total=0;
				_ss=0; _pt_cmde=0;
				_compteur=0;_delai_max=0;_delai_moy=0;_total_delai=0;
				pre_=null;
				IF(COALESCE(next_,'')='') THEN 
					CONTINUE;
				END IF;
				-- Conditionnement par défaut dans le dépôt
				SELECT INTO first_date_ m.date_doc FROM yvs_base_mouvement_stock m WHERE m.date_doc>=debut_ AND m.article=article_ AND m.depot=depot_ AND m.mouvement='E' AND m.table_externe='yvs_com_contenu_doc_achat' ORDER BY m.date_doc ASC LIMIT 1;
				SELECT INTO last_date_ m.date_doc FROM yvs_base_mouvement_stock m WHERE m.date_doc<=fin_ AND m.article=article_ AND m.depot=depot_ AND m.mouvement='E' AND m.table_externe='yvs_com_contenu_doc_achat' ORDER BY m.date_doc DESC LIMIT 1;
				SELECT INTO unite_ c.id, u.id id_unite, u.libelle FROM yvs_base_conditionnement c INNER JOIN yvs_base_article_depot ad ON ad.default_cond=c.id INNER JOIN yvs_base_unite_mesure u ON u.id=c.unite WHERE c.article=article_ LIMIT 1; 				
				-- Détermination des périodes 
				FOR line_ IN SELECT DISTINCT m.date_doc  FROM yvs_base_mouvement_stock m WHERE m.article=article_ AND m.depot=depot_ AND m.date_doc BETWEEN debut_ AND fin_
																	  AND m.mouvement='E' AND m.table_externe='yvs_com_contenu_doc_achat' 
																	ORDER BY m.date_doc 
					LOOP 
						IF(pre_ IS NULL) THEN
							pre_=line_.date_doc;
							CONTINUE;
						END IF;
						_duree=(line_.date_doc-pre_);
						IF(_duree>_delai_max) THEN
						   _delai_max=_duree;
						END IF;
						_total_delai=_total_delai+_duree;
						pre_=line_.date_doc;
						_compteur=_compteur+1;
					END LOOP;
				_duree=last_date_-first_date_;
				IF(_compteur>0 AND _duree>0) THEN
					_delai_moy=(_total_delai/_compteur)::double precision;
				END IF;	
				--
				-- Détermination de la consommation moyenne
				--
				_compteur=0;
				_coef_convert=0;
				IF(COALESCE(ids_exclude,'')='')THEN
					ids_exclude='0';
				END IF;
				IF(query_!='') THEN					
					FOR line_ IN EXECUTE query_ 
						LOOP	
							--Convertir en unité de stockage si les unités sont différentes
							SELECT INTO unite_cible u.id, u.libelle,c.id id_cond FROM yvs_base_conditionnement c INNER JOIN yvs_base_unite_mesure u ON u.id=c.unite WHERE c.id=line_.conditionnement;
							IF(unite_cible.id!=unite_.id_unite) THEN
								SELECT INTO _coef_convert taux_change FROM yvs_base_table_conversion t WHERE t.unite=unite_cible.id AND t.unite_equivalent=unite_.id_unite;
								IF(COALESCE(_coef_convert,0)=0) THEN
									_coef_convert=0;
								END IF;
							END IF;
							IF(_coef_convert>0) THEN 
								line_.quantite=line_.quantite*_coef_convert;
							END IF;
							IF(line_.quantite>_qte_max) THEN
							   _qte_max=line_.quantite;
							END IF;
							_qte_total=_qte_total+line_.quantite;
							_compteur=_compteur+1;
						END LOOP;
				ELSE 
					RETURN QUERY SELECT * FROM table_seuils_stock;
				END IF;
				IF(_compteur>0 AND _duree>0) THEN
					_qte_moy=(_qte_total/_duree)::double precision;
				ELSE
					CONTINUE;
				END IF;				
				CASE methode_
					WHEN '1' THEN
						_ss=(_delai_max*_qte_max)-(_delai_moy*_qte_moy);
						_pt_cmde=_ss+(_delai_moy*_qte_moy);				
					WHEN '2' THEN
						--Cherche la variance de la demande
						IF(_qte_moy>0) THEN
							_compteur=1;
							FOR line_ IN EXECUTE query_ 
								LOOP
									_variance=_variance+(_compteur-_qte_moy)^2;
									_compteur=_compteur+1;
								END LOOP;
							_variance=_variance/_compteur;
							_et=(|/_variance);
							_ss=_coef *_et* (|/_delai_moy);
							_pt_cmde=_ss+(_delai_moy*_qte_moy);	
						END IF;
				END CASE;
				IF(_coef_convert<=0) THEN
					ref_unite_=unite_cible.libelle;
					id_cond_=unite_cible.id_cond;
				ELSE
					ref_unite_=unite_.libelle;
					id_cond_=unite_.id;
				END IF;
				INSERT INTO table_seuils_stock VALUES (article_,_article.ref_art,_article.designation,_article.categorie,round(_qte_moy::decimal,2),round(_delai_moy::decimal,2), round(_qte_max::decimal,2), round(_delai_max::decimal,2),round(_ss::decimal,2), round(_pt_cmde::decimal,2),_coef,_et,id_cond_,ref_unite_);
			END LOOP;
	END IF;	
	RETURN QUERY SELECT * FROM table_seuils_stock;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION base_maj_seuil_stocks(character varying, bigint, date,date,character varying,character varying )
  OWNER TO postgres;
  COMMENT ON FUNCTION base_maj_seuil_stocks(character varying, bigint, date,date,character varying,character varying) IS 'FOnction de calcul et mise à jour du stock de sécurité et du point de commande d''un article'
