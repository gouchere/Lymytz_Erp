--SELECT  import_data_article_from_glp('localhost', 5432, 'care_paix_plus', 'postgres', 'yves1910/', 2290, 2315, null)
CREATE OR REPLACE FUNCTION import_data_article_from_glp(_server character varying, _port integer, _dbname character varying, _user_database character varying, _password character varying, _societe bigint, _agence bigint, _author bigint)
	RETURNS BOOLEAN AS
	$BODY$
		DECLARE
			articles_ RECORD;
			unite_ RECORD;
			current_id_U BIGINT;
			link CHARACTER VARYING default 'host='||_server|| ' dbname='||_dbname||' port='||_port|| ' user='||_user_database|| ' password='||_password;
			query_article CHARACTER VARYING;
			query_prod CHARACTER VARYING;
			categorie_ CHARACTER VARYING;
			nb_prod bigint;
			familles_ RECORD;
			current_id_F BIGINT;
			current_id_A BIGINT;
			current_ RECORD; -- permet de selectionner un article
			classes_ RECORD; -- permet de selectionner la classe statistique
			current_id_C BIGINT; --classe statistique
			current_id_G BIGINT; --id groupe par defaut
			current_id_cond BIGINT; --id groupe par defaut
		BEGIN
			-- **Créer une unité par défaut**
			-- **Créer les familles**
			-- **Créer les catégories comptables**
			-- **Créer les classes stats**
			
			query_article= 'SELECT a.refart, a.categorie, a.changeprix, a.classe, a.commentaire, a.designation ,
					a.modeconso, a.norme,a.pua,a.puv, a.remise, a.suivienstock, a.unite, a.unitepoids, 
					a.reffamille AS famille, a.defnorme, a.visibleensynthese,a.classe2, f.designation 
					FROM articles a INNER JOIN famillearticles f ON a.reffamille=f.reffamille 
					WHERE a.sommeil IS FALSE AND a.categorie!=''REVENTE''';			
			-- Récupération de l'unité par défaut
			SELECT INTO unite_ * FROM yvs_base_unite_mesure u WHERE u.societe=_societe AND (u.reference='UNITE' OR u.reference='unite' OR u.reference='Unite' OR u.defaut=true) LIMIT 1;
			IF(COALESCE(unite_.id,0)<1) THEN
				SELECT INTO current_id_U nextval('yvs_prod_unite_masse_id_seq'::regclass);
				INSERT INTO yvs_base_unite_mesure(id, reference, libelle, societe, description, type, author, date_update, date_save, defaut)
							VALUES(current_id_U,'UNITE', 'UNITE',_societe, 'Unité de stockage par defaut', 'Q', _author, current_timestamp, current_timestamp, true);
			ELSE
				current_id_U=unite_.id;
			END IF;
			-- insère un groupe par defaut
			SELECT INTO current_id_G id FROM yvs_base_groupes_article WHERE societe=_societe AND refgroupe='DEFAULT';
			IF(COALESCE(current_id_G, 0)<1) THEN
				SELECT INTO current_id_G nextval('yvs_groupesproduits_id_seq'::regclass);
				INSERT INTO yvs_base_groupes_article( id, description, refgroupe, societe, code_appel, actif, groupe_parent, 
													designation, author, date_update, date_save, photo, execute_trigger, 
													ordre)
											VALUES (current_id_G, null,'DEFAULT', _societe, 'DEFAULT', true, null, 'DEFAULT', 
													_author, current_timestamp, current_timestamp, null, null, 0);

			END IF;
			FOR articles_ IN SELECT * FROM dblink(link, query_article) AS 
								table_(refart character varying, categorie character varying, changeprix BOOLEAN , classe character varying, commentaire character varying, 
								       designation character varying, modeconso character varying, norme character varying, pua double precision, 
								       puv double precision, remise double precision, suivienstock boolean , unite character varying, unitepoids character varying, 
								       famille character varying, defnorme boolean , visibleensynthese boolean,classe2 character varying, designation_f character varying)
			LOOP
				--Si c'est un produit fini ou semi fini, on vérifie qu'on ait récement produit
				IF(articles_.categorie='PRODUIT FINI' OR articles_.categorie='PRODUIT SEMI FINI') THEN
					query_prod='SELECT COUNT(*) AS nb FROM productions WHERE article='||articles_.refart|| 'AND datep>''01-01-2021''';
					--SELECT 	INTO nb_prod nb FROM dblink(link, query_prod) AS nb;	
				END IF;
				-- vérifie l'existence de la famille d'article
				SELECT INTO familles_ * FROM yvs_base_famille_article WHERE societe=_societe AND reference_famille=articles_.famille;
				IF(COALESCE(familles_.id,0)<1) THEN
					SELECT INTO current_id_F nextval('yvs_prod_famille_article_id_seq'::regclass);
					INSERT INTO yvs_base_famille_article(id, reference_famille, designation, description, famille_parent, 
															societe, author, actif, date_update, date_save, prefixe, execute_trigger)
								VALUES (current_id_F, articles_.famille, articles_.designation_f, '', null, 
									_societe, _author, true, current_timestamp, current_timestamp, null, null);
				ELSE
					current_id_F=familles_.id;
				END IF;
				--insert l'article
					-- vérifier qu'un article de même référence n'existe pas déjà dans la société
					SELECT INTO current_ * FROM yvs_base_articles a INNER JOIN yvs_base_famille_article f ON f.id=a.famille 
											WHERE f.societe=_societe AND UPPER(a.ref_art)=UPPER(articles_.refart);
					IF(COALESCE(current_.id,0)<1) THEN						
						-- Vérifions la classe statistique
						IF(articles_.classe!='null' AND articles_.classe IS NOT NULL) THEN 
							SELECT INTO current_id_C id FROM yvs_base_classes_stat c WHERE c.code_ref=articles_.classe AND c.societe=_societe;
							IF(COALESCE(current_id_C,0)<1) THEN
								SELECT INTO current_id_C nextval('yvs_base_classes_stat_id_seq'::regclass);
								INSERT INTO yvs_base_classes_stat(id, code_ref, designation, actif, visible_synthese, visible_journal, 
																author, date_save, date_update, societe, parent, execute_trigger)
															VALUES (current_id_C, articles_.classe, articles_.classe, true, true, true, 
																	_author, current_timestamp, current_timestamp, _societe, null, null);

							END IF;
						END IF;
						SELECT INTO current_id_A nextval('yvs_articles_id_seq'::regclass);
						case articles_.cateorie
							WHEN 'PRODUIT FINI' THEN
								categorie_='PF';
							WHEN 'MATIERE PREMIERE' THEN
								categorie_='MP';
							WHEN 'PRODUIT SEMI FINI' THEN
							categorie_='PSF';
							END CASE;
						INSERT INTO yvs_base_articles(id, change_prix, description, def_norme, designation, mode_conso, 
															norme,ref_art, suivi_en_stock, visible_en_synthese, groupe, coefficient, service, 
															methode_val, actif, fabriquant, photo_2, photo_3, categorie, 
															famille, duree_vie, duree_garantie, fichier, template, unite_de_masse, 
															unite_volume, lot_fabrication, author, nature_prix_min, puv_ttc, 
															pua_ttc, unite_stockage, unite_vente, date_update, date_save, 														
															classe1, classe2, type_service, date_last_mvt, taux_ecart_pr, 														
															tags, ordre, controle_fournisseur, photo_1, 
															
															photo_1_extension, photo_2_extension, photo_3_extension, requiere_lot)
										VALUES (current_id_A, articles_.changeprix, articles_.commentaire, articles_.defnorme, articles_.designation, articles_.modeconso, 
												true, articles_.refart, articles_.suivienstock,articles_.visibleensynthese, current_id_G, 0, false, 
											'CMPI', true, null, null, null, categorie_,
											current_id_F,0, 0, null, null, null, 
											null, 1, _author, null, true, 
											true, null, null, current_timestamp, current_timestamp, 
											current_id_C,current_id_C, null, null, 0, 
											null, null, false, null, 
											null, null, null, false);

					ELSE
						current_id_A=current_.id;
					END IF;
				-- insère le conditionnement   
				SELECT INTO current_id_cond id FROM yvs_base_conditionnement c WHERE c.article=current_id_A AND c.unite=current_id_U;
				IF(COALESCE(current_id_cond,0)<1) THEN
					SELECT INTO current_id_cond nextval('yvs_base_conditionnement_id_seq'::regclass);
					INSERT INTO yvs_base_conditionnement(id, article, unite, author, prix, prix_min, nature_prix_min, 
															remise, cond_vente, date_update, date_save, prix_achat, photo, 
															code_barre, by_achat, by_prod, defaut, prix_prod, marge_min, 
															execute_trigger, actif, proportion_pua, taux_pua, photo_extension)
												 VALUES (current_id_cond, current_id_A, current_id_U, _author, articles_.puv, articles_.puv, null, 
															0, true, current_timestamp, current_timestamp, articles_.pua, null, 
															null, true, true, true, 0, 0, 
															null, true, false, 0, null);

				END IF;
			END LOOP;

			RETURN TRUE;
		END;
	$BODY$
	LANGUAGE plpgsql VOLATILE
	COST 100;
	ALTER FUNCTION import_data_article_from_glp(character varying, integer, character varying,character varying, character varying,bigint, bigint, bigint)
	OWNER TO postgres;