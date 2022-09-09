--SELECT  import_data_article_from_glp('localhost', 5432, 'care_paix_plus', 'postgres', 'yves1910/', 2290, 2315, null)
CREATE OR REPLACE FUNCTION import_nomenclature_from_glp(_server character varying, _port integer, _dbname character varying, _user_database character varying, _password character varying, _societe bigint, _agence bigint, _author bigint)
	RETURNS BOOLEAN AS
	$BODY$
		DECLARE
			articles_ RECORD;
			nomenclatures_ RECORD;
			link CHARACTER VARYING default 'host='||_server|| ' dbname='||_dbname||' port='||_port|| ' user='||_user_database|| ' password='||_password;
			query_article CHARACTER VARYING;
			query_nomenclature CHARACTER VARYING;
			article_ RECORD;
			composant_ RECORD;
			current_composant bigint;
			unite_ RECORD;
			current_unite bigint;
			current_nomenclature bigint;
			nb_nomenclature integer;
			compteur integer;
			type_composant CHARACTER VARYING;
			current_gamme bigint;
			current_operation bigint;
		BEGIN
			-- **Créer une nomenclature**
			-- **Créer les composants de la nomenclature**
			-- **Créer les Gammes de fabrications**
			-- **Créer les Opérations de game**
			
			query_article= 'SELECT a.refart, a.categorie, a.changeprix, a.classe, a.commentaire, a.designation ,
					a.modeconso, a.norme,a.pua,a.puv, a.remise, a.suivienstock, a.unite, a.unitepoids, 
					a.reffamille AS famille, a.defnorme, a.visibleensynthese,a.classe2, f.designation 
					FROM articles a INNER JOIN famillearticles f ON a.reffamille=f.reffamille 
					WHERE a.sommeil IS FALSE AND a.categorie!=''REVENTE'' AND a.categorie!=''MATIERE PREMIERE''';						
			FOR articles_ IN SELECT * FROM dblink(link, query_article) AS 
								table_(refart character varying, categorie character varying, changeprix BOOLEAN , classe character varying, commentaire character varying, 
								       designation character varying, modeconso character varying, norme character varying, pua double precision, 
								       puv double precision, remise double precision, suivienstock boolean , unite character varying, unitepoids character varying, 
								       famille character varying, defnorme boolean , visibleensynthese boolean,classe2 character varying, designation_f character varying)
			LOOP
				--Récupère les informations de l'article sur le serveur locale				
				SELECT INTO article_ a.id, c.unite, a.ref_art, c.id AS conditionnement FROM yvs_base_conditionnement c INNER JOIN yvs_base_articles a ON a.id=c.article 
																													   INNER JOIN yvs_base_famille_article f ON f.id=a.famille
																					   WHERE a.ref_art=articles_.refart AND f.societe=_societe;
				IF(COALESCE(article_.id,0)>0 AND (articles_.categorie='PRODUIT FINI' OR articles_.categorie='PRODUIT SEMI FINI')) THEN
					-- Vérifie si l'article a déjà une nomenclature
					SELECT INTO nb_nomenclature COUNT(*) FROM yvs_prod_nomenclature WHERE article=article_.id;
					IF(COALESCE(nb_nomenclature,0)<1) THEN
						--récupère la nomenclature sur le serveur distant
						query_nomenclature= 'SELECT s.id, s.compose AS article, s.composant, s.ordre, s.qtecomposant as quantite_c, s.qtecompose as quantite,s.umasse
											 FROM structurations s WHERE compose= '''||articles_.refart||'''';
						compteur=0;
						FOR nomenclatures_ IN SELECT * FROM dblink(link, query_nomenclature) AS 
										table_n(id bigint, article character varying, composant character varying, ordre integer,
											quantite_c double precision, quantite double precision, umasse character varying)
						LOOP
								IF(compteur=0) THEN
									-- insère la nomenclature
									SELECT INTO current_nomenclature nextval('yvs_prod_nomenclature_id_seq'::regclass);
									INSERT INTO yvs_prod_nomenclature(id, reference, niveau, debut_validite, fin_validite, quantite, 
																		article, actif, quantite_lie_aux_composants, principal, alway_valide, 
																		author, unite_preference, date_update, date_save, unite_mesure, 
																		for_conditionnement, marge_qte, acces, masquer, type_nomenclature)
																VALUES (current_nomenclature, 'NOM:'||article_.ref_art, 1, current_date, current_date,nomenclatures_.quantite, 
																		article_.id, true, false, true, true, 
																		_author, null, current_timestamp, current_timestamp, article_.conditionnement, 
																		false, 0, null, false, 'P');

									-- insère une gamme de fabrication
									SELECT INTO current_gamme nextval('yvs_prod_gamme_article_id_seq'::regclass);
									INSERT INTO yvs_prod_gamme_article(id, code_ref, designation, description, actif, article, principal, 
																		author, date_update, date_save, debut_validite, fin_validite, 
																		permanant, unite_temps, acces, masquer)
																VALUES (current_gamme, 'GAMME:'||article_.ref_art, 'GAMME:'||article_.ref_art, null, true, article_.id, true, 
																		_author, current_timestamp, current_timestamp, current_date, current_date, 
																		true, null, null, false);

									-- insère une opération de gamme
									SELECT INTO current_operation nextval('yvs_prod_phase_gamme_id_seq'::regclass);
									INSERT INTO yvs_prod_operations_gamme(id, code_ref, description, numero, gamme_article, operation, 
																			author, date_update, date_save, temps_reglage, temps_operation, 
																			type_temps, taux_efficience, taux_perte, quantite_base, cadence,quantite_min, type_cout, actif)
																	VALUES (current_operation, 'PREPARATION & CUISSON', '', 10, current_gamme, false, 
																			_author, current_timestamp, current_timestamp, 0, 3, 
																			'Proportionnel', 0, 0, 0, 0,0, 'T', true);

								END IF;
							-- gérer l'unité des composants
							SELECT INTO current_unite id FROM yvs_base_unite_mesure u WHERE u.reference=nomenclatures_.umasse AND u.societe=_societe;
							IF(COALESCE(current_unite,0)<1) THEN
								SELECT INTO current_unite nextval('yvs_prod_unite_masse_id_seq'::regclass);
								INSERT INTO yvs_base_unite_mesure(id, reference, libelle, societe, description, type, author, date_update, date_save, defaut)
										VALUES(current_unite,nomenclatures_.umasse, nomenclatures_.umasse,_societe, 'Unité de stockage par defaut', 'Q', _author, current_timestamp, current_timestamp, false);
							END IF;
							SELECT INTO composant_ a.id, c.id AS unite, a.categorie FROM yvs_base_conditionnement c INNER JOIN yvs_base_articles a on a.id=c.article 
																													INNER JOIN yvs_base_unite_mesure u ON u.id=c.unite 
																													INNER JOIN yvs_base_famille_article f ON f.id=a.famille
																													WHERE a.ref_art=nomenclatures_.composant AND f.societe=_societe; 
							-- Modifier l'unité du composant
							RAISE NOTICE 'article :% C= % ',nomenclatures_.article, nomenclatures_.composant;
							UPDATE yvs_base_conditionnement SET unite=current_unite WHERE id=(SELECT c.id FROM yvs_base_conditionnement c inner join yvs_base_articles a on a.id=c.article 
																				      INNER JOIN yvs_base_unite_mesure u ON u.id=c.unite
																				      INNER JOIN yvs_base_famille_article f ON f.id=a.famille 
																      WHERE a.ref_art=nomenclatures_.composant AND u.reference='UNITE' AND f.societe=_societe);							
							IF(composant_.categorie='MP') THEN
								type_composant='N';
							ELSIF(composant_.categorie='PSF') THEN
								type_composant='SP';
							END IF;
							--Insère le composant
							SELECT INTO current_composant nextval('yvs_prod_composant_nomenclature_id_seq'::regclass);
							INSERT INTO yvs_prod_composant_nomenclature(id, quantite, coefficient, type, mode_arrondi, actif, article, 
																		nomenclature, author, unite, date_update, date_save, stockable, 
																		ordre, composant_lie, inside_cout, alternatif,free_use)
																VALUES (current_composant, nomenclatures_.quantite_c, 1, type_composant, 'E', true, composant_.id, 
																		current_nomenclature, _author, composant_.unite, current_timestamp, current_timestamp, false, 
																		nomenclatures_.ordre, null, true, false, false);

							
							-- insère le composant opération 
							INSERT INTO yvs_prod_composant_op(sens, composant, operation, author, date_save, date_update, 
															quantite, marge_qte, coeficient_valeur, unite, taux_perte, type_cout, free_use)
													VALUES ('S', current_composant, current_operation, _author, current_timestamp, current_timestamp, 
															100, 20, null, composant_.unite, 0, null, false);

							compteur=compteur+1;
						END LOOP;
					END IF;
				END IF;
			END LOOP;

			RETURN TRUE;
		END;
	$BODY$
	LANGUAGE plpgsql VOLATILE
	COST 100;
	ALTER FUNCTION import_nomenclature_from_glp(character varying, integer, character varying,character varying, character varying,bigint, bigint, bigint)
	OWNER TO postgres;