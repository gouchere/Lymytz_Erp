-- Function: yvs_compta_content_journal(bigint, bigint, character varying)

-- DROP FUNCTION yvs_compta_content_journal(bigint, bigint, character varying);

CREATE OR REPLACE FUNCTION yvs_compta_content_journal(IN agence_ bigint, IN element_ bigint, IN table_ character varying)
  RETURNS TABLE(id bigint, jour integer, num_piece character varying, num_ref character varying, compte_general bigint, compte_tiers bigint, libelle character varying, debit double precision, credit double precision, echeance date, ref_externe bigint, table_externe character varying, statut character varying, error character varying, contenu bigint, centre bigint, coefficient double precision) AS
$BODY$
DECLARE
	_id_ BIGINT DEFAULT 0;
	_jour_ INTEGER DEFAULT 0; 
	_num_piece_ CHARACTER VARYING DEFAULT '';
	_num_ref_ CHARACTER VARYING DEFAULT ''; 
	_compte_general_ BIGINT; 
	_compte_tiers_ BIGINT; 
	_libelle_ CHARACTER VARYING DEFAULT ''; 
	_debit_ DOUBLE PRECISION DEFAULT 0; 
	_credit_ DOUBLE PRECISION DEFAULT 0; 
	_echeance_ DATE DEFAULT CURRENT_DATE; 
	_ref_externe_ BIGINT DEFAULT element_; 
	_table_externe_ CHARACTER VARYING DEFAULT table_; 
	_statut_ CHARACTER VARYING DEFAULT 'V';
	_error_ CHARACTER VARYING DEFAULT '';
	_contenu_ BIGINT;
	_centre_ BIGINT;
	_coefficient_ DOUBLE PRECISION DEFAULT 0;

	ligne_ RECORD;
	sous_ RECORD;
	data_ RECORD;

	societe_ BIGINT DEFAULT 0;
	
	total_ DOUBLE PRECISION DEFAULT 0; 
	somme_ DOUBLE PRECISION DEFAULT 0; 
	
	i_ INTEGER DEFAULT 1;
	taux_ DOUBLE PRECISION DEFAULT 100;
BEGIN    
	DROP TABLE IF EXISTS table_compta_content_journal;
	CREATE TEMP TABLE IF NOT EXISTS table_compta_content_journal(_id BIGINT, _jour INTEGER, _num_piece CHARACTER VARYING, _num_ref CHARACTER VARYING, _compte_general BIGINT, _compte_tiers BIGINT, _libelle CHARACTER VARYING, _debit DOUBLE PRECISION, _credit DOUBLE PRECISION, _echeance DATE, _ref_externe BIGINT, _table_externe CHARACTER VARYING, _statut CHARACTER VARYING, _error character varying, _contenu bigint, _centre bigint, _coefficient double precision);
	DELETE FROM table_compta_content_journal;
	IF(table_ IS NOT NULL AND table_ NOT IN ('', ' '))THEN
		SELECT INTO societe_ a.societe FROM yvs_agences a WHERE a.id = agence_;
		IF(table_ = 'DOC_VENTE')THEN
			SELECT INTO data_ d.num_doc, d.nom_client, d.tiers, d.client, e.date_entete, c.compte FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_com_client c ON d.client = c.id WHERE d.id = element_;
			IF(data_.num_doc IS NOT NULL AND data_.num_doc NOT IN ('', ' '))THEN				
				total_ = (SELECT get_ttc_vente(element_)); -- RECUPERATION DU TTC DE LA FACTURE
				IF(total_ IS NOT NULL AND total_ > 0)THEN	
					-- COMPTABILISATION DU TTC DE LA FACTURE
					IF(data_.client IS NULL OR data_.client < 1)THEN
						_error_ = 'cette facture de vente n''est pas rattachée à un client';
					ELSIF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette facture de vente est rattachée à un client qui n''a pas de compte tiers';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette facture de vente est rattachée à un client qui n''a pas de compte collectif';
					END IF;
					_jour_ = to_char(data_.date_entete ,'MM')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_entete;
					_compte_general_ = data_.compte;
					_compte_tiers_ = data_.tiers;
					FOR ligne_ IN SELECT t.* FROM yvs_base_tranche_reglement t INNER JOIN yvs_base_model_reglement m ON t.model = m.id INNER JOIN yvs_com_doc_ventes d ON d.model_reglement = m.id WHERE d.id = element_
					LOOP
						_debit_ = (SELECT arrondi(societe_, (total_ * ligne_.taux / 100)));
						_credit_ = 0;
						somme_ = somme_ + _debit_;
						_libelle_ = 'Echéancier '||ligne_.taux||'% pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
						i_ = i_ + 1;
					END LOOP;
					IF(total_ > somme_)THEN
						_debit_ = (SELECT arrondi(societe_, (total_ - somme_)));
						_credit_ = 0;
						IF(somme_ > 0)THEN
							taux_ = (((total_ - somme_) / total_) * 100);
						END IF;
						_libelle_ = 'Echéancier '||taux_||'% pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
						i_ = i_ + 1;
					END IF;

					-- COMPTABILISATION DES ARTICLES
					DROP TABLE IF EXISTS table_montant_compte_contenu; -- REGROUPEMENT DES MONTANT PAR COMPTES
					CREATE TEMP TABLE IF NOT EXISTS table_montant_compte_contenu(_compte BIGINT, _montant DOUBLE PRECISION);
					FOR ligne_ IN SELECT co.prix_total, (SELECT ca.compte FROM yvs_base_article_categorie_comptable ca WHERE ca.article = co.article AND ca.categorie = dv.categorie_comptable LIMIT 1) AS compte
						FROM yvs_com_contenu_doc_vente co INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente WHERE dv.id = element_
					LOOP
						SELECT INTO sous_ y.* FROM table_montant_compte_contenu y WHERE y._compte = ligne_.compte;
						IF(sous_._compte IS NOT NULL AND sous_._compte > 0)THEN
							UPDATE table_montant_compte_contenu SET _montant = _montant + ligne_.prix_total WHERE _compte = ligne_.compte;
						ELSE
							INSERT INTO table_montant_compte_contenu VALUES (ligne_.compte, ligne_.prix_total);
						END IF;
					END LOOP;
					FOR ligne_ IN SELECT SUM(_montant) AS total, ca._compte AS compte, pc.intitule FROM table_montant_compte_contenu ca INNER JOIN yvs_base_plan_comptable pc ON ca._compte = pc.id GROUP BY ca._compte, pc.id
					LOOP
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _credit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_vente tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_vente tc INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente 
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE ca.compte = ligne_.compte AND dv.id = element_);
						_credit_ = ligne_.total - COALESCE(_credit_, 0);
						_debit_ = 0;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = null;
						_libelle_ = ligne_.intitule||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
						i_ = i_ + 1;
					END LOOP;

					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT sum(tc.montant) AS total, tx.compte, tx.designation FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_vente tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						WHERE dv.id = element_ GROUP BY tx.compte, tx.designation 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = ligne_.total;
							_debit_ = 0;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.designation||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_vente co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						WHERE dv.id = element_ GROUP BY tx.id, tx.libelle 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(ligne_.augmentation)THEN
								_credit_ = ligne_.total;
							ELSE
								_debit_ = ligne_.total;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.libelle||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'JOURNAL_VENTE')THEN
			SELECT INTO data_ u.code_users, d.code AS depot, e.date_entete, p.code AS point FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id 
				LEFT JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id LEFT JOIN yvs_base_point_vente p ON cp.point = p.id LEFT JOIN yvs_com_creneau_depot cd ON h.creneau_point = cd.id LEFT JOIN yvs_base_depots d ON cd.depot = d.id 
				WHERE e.id = element_;
			IF(data_.code_users IS NOT NULL AND data_.code_users NOT IN ('', ' '))THEN				
				total_ = (SELECT get_ttc_entete_vente(element_)); -- RECUPERATION DU TTC DE LA JOURNEE DE VENTE
				IF(total_ IS NOT NULL AND total_ > 0)THEN	
					-- COMPTABILISATION DU TTC DE LA JOURNEE DE VENTE
					_jour_ = to_char(data_.date_entete ,'MM')::integer;
					_num_piece_ = data_.code_users || '/';
					IF(data_.point IS NOT NULL AND data_.point NOT IN ('', ' '))THEN
						_num_piece_ = _num_piece_ || data_.point || '/';
					ELSE
						_num_piece_ = _num_piece_ || data_.depot || '/';
					END IF;
					_num_piece_ = _num_piece_ || to_char(data_.date_entete ,'ddMMyy');	
					_echeance_ = data_.date_entete;
					FOR ligne_ IN SELECT sum(get_ttc_vente(dv.id)) as total , cl.compte, dv.tiers, CONCAT(ts.nom, ' ', ts.prenom) AS nom FROM yvs_com_doc_ventes dv INNER JOIN yvs_com_client cl ON dv.client = cl.id 
						INNER JOIN yvs_base_tiers ts ON dv.tiers = ts.id WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_
						GROUP BY dv.entete_doc, cl.compte, dv.tiers, ts.id
					LOOP
						_debit_ = (SELECT arrondi(societe_, ligne_.total));
						_credit_ = 0;
						somme_ = somme_ + _debit_;
						_libelle_ = 'Echéancier 100% pour le journal de vente N° '||_num_piece_;
						IF(i_ < 10)THEN
							_num_ref_ = _num_piece_ ||'-0'||i_;
						ELSE
							_num_ref_ = _num_piece_ ||'-'||i_;
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = ligne_.tiers;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
						i_ = i_ + 1;
					END LOOP;

					-- COMPTABILISATION DES ARTICLES
					DROP TABLE IF EXISTS table_montant_compte_contenu; -- REGROUPEMENT DES MONTANT PAR COMPTES
					CREATE TEMP TABLE IF NOT EXISTS table_montant_compte_contenu(_compte BIGINT, _montant DOUBLE PRECISION);
					FOR ligne_ IN SELECT co.prix_total, (SELECT ca.compte FROM yvs_base_article_categorie_comptable ca WHERE ca.article = co.article AND ca.categorie = dv.categorie_comptable LIMIT 1) AS compte
						FROM yvs_com_contenu_doc_vente co INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_
					LOOP
						SELECT INTO sous_ y.* FROM table_montant_compte_contenu y WHERE y._compte = ligne_.compte;
						IF(sous_._compte IS NOT NULL AND sous_._compte > 0)THEN
							UPDATE table_montant_compte_contenu SET _montant = _montant + ligne_.prix_total WHERE _compte = ligne_.compte;
						ELSE
							INSERT INTO table_montant_compte_contenu VALUES (ligne_.compte, ligne_.prix_total);
						END IF;
					END LOOP;
					FOR ligne_ IN SELECT SUM(_montant) AS total, ca._compte AS compte, pc.intitule FROM table_montant_compte_contenu ca INNER JOIN yvs_base_plan_comptable pc ON ca._compte = pc.id GROUP BY ca._compte, pc.id
					LOOP
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _credit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_vente tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_vente tc INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente 
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND ca.compte = ligne_.compte AND dv.entete_doc = element_);
						_credit_ = ligne_.total - COALESCE(_credit_, 0);
						_debit_ = 0;
						IF(i_ < 10)THEN
							_num_ref_ = _num_piece_ ||'-0'||i_;
						ELSE
							_num_ref_ = _num_piece_ ||'-'||i_;
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = null;
						_libelle_ = ligne_.intitule||' pour le journal de vente N° '||_num_piece_;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
						i_ = i_ + 1;
					END LOOP;

					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT sum(tc.montant) AS total, tx.compte, tx.designation FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_vente tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id 
						INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_ GROUP BY tx.compte, tx.designation 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = ligne_.total;
							_debit_ = 0;
							IF(i_ < 10)THEN
								_num_ref_ = _num_piece_ ||'-0'||i_;
							ELSE
								_num_ref_ = _num_piece_ ||'-'||i_;
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.designation||' pour le journal de vente N° '||_num_piece_;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_vente co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_ GROUP BY tx.id, tx.libelle 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(ligne_.augmentation)THEN
								_credit_ = ligne_.total;
							ELSE
								_debit_ = ligne_.total;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = _num_piece_ ||'-0'||i_;
							ELSE
								_num_ref_ = _num_piece_ ||'-'||i_;
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.libelle||' pour le journal de vente N° '||_num_piece_;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'DOC_ACHAT')THEN
			SELECT INTO data_ d.num_doc, d.date_doc, d.fournisseur, CONCAT(c.nom, ' ', c.prenom) AS nom, c.compte, c.tiers FROM yvs_com_doc_achats d LEFT JOIN yvs_base_fournisseur c ON d.fournisseur = c.id WHERE d.id = element_;
			IF(data_.num_doc IS NOT NULL AND data_.num_doc NOT IN ('', ' '))THEN				
				total_ = (SELECT get_ttc_achat(element_)); -- RECUPERATION DU TTC DE LA FACTURE
				IF(total_ IS NOT NULL AND total_ > 0)THEN	
					-- COMPTABILISATION DU TTC DE LA FACTURE
					IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
						_error_ = 'cette facture d''achat n''est pas rattachée à un fournisseur';
					ELSIF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette facture d''achat est rattachée à un fournisseur qui n''a pas de compte tiers';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette facture d''achat est rattachée à un fournisseur qui n''a pas de compte collectif';
					END IF;
					_jour_ = to_char(data_.date_doc ,'MM')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_doc;
					_compte_general_ = data_.compte;
					_compte_tiers_ = data_.tiers;
					FOR ligne_ IN SELECT t.* FROM yvs_base_tranche_reglement t INNER JOIN yvs_base_model_reglement m ON t.model = m.id INNER JOIN yvs_com_doc_achats d ON d.model_reglement = m.id WHERE d.id = element_
					LOOP
						_debit_ = 0;
						_credit_ = (SELECT arrondi(societe_, (total_ * ligne_.taux / 100)));
						somme_ = somme_ + _debit_;
						_libelle_ = 'Echéancier '||ligne_.taux||'% pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
						i_ = i_ + 1;
					END LOOP;
					IF(total_ > somme_)THEN
						_debit_ = 0;
						_credit_ = (SELECT arrondi(societe_, (total_ - somme_)));
						IF(somme_ > 0)THEN
							taux_ = (((total_ - somme_) / total_) * 100);
						END IF;
						_libelle_ = 'Echéancier '||taux_||'% pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
						i_ = i_ + 1;
					END IF;

					-- COMPTABILISATION DES ARTICLES
					DROP TABLE IF EXISTS table_montant_compte_contenu; -- REGROUPEMENT DES MONTANT PAR COMPTES
					CREATE TEMP TABLE IF NOT EXISTS table_montant_compte_contenu(_compte BIGINT, _montant DOUBLE PRECISION);
					FOR ligne_ IN SELECT co.prix_total, (SELECT ca.compte FROM yvs_base_article_categorie_comptable ca WHERE ca.article = co.article AND ca.categorie = dv.categorie_comptable LIMIT 1) AS compte
						FROM yvs_com_contenu_doc_achat co INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat WHERE dv.id = element_
					LOOP
						SELECT INTO sous_ y.* FROM table_montant_compte_contenu y WHERE y._compte = ligne_.compte;
						IF(sous_._compte IS NOT NULL AND sous_._compte > 0)THEN
							UPDATE table_montant_compte_contenu SET _montant = _montant + ligne_.prix_total WHERE _compte = ligne_.compte;
						ELSE
							INSERT INTO table_montant_compte_contenu VALUES (ligne_.compte, ligne_.prix_total);
						END IF;
					END LOOP;
					FOR ligne_ IN SELECT SUM(_montant) AS total, ca._compte AS compte, pc.intitule FROM table_montant_compte_contenu ca INNER JOIN yvs_base_plan_comptable pc ON ca._compte = pc.id GROUP BY ca._compte, pc.id
					LOOP
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _debit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_achat tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_achat tc INNER JOIN yvs_com_contenu_doc_achat co ON tc.contenu = co.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE ca.compte = ligne_.compte AND dv.id = element_);
						_credit_ = 0;
						_debit_ = ligne_.total - COALESCE(_debit_, 0);
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = null;
						_libelle_ = ligne_.intitule||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
						i_ = i_ + 1;
					END LOOP;

					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT sum(tc.montant) AS total, tx.compte, tx.designation FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_achat tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_achat co ON tc.contenu = co.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
						WHERE dv.id = element_ GROUP BY tx.compte, tx.designation 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = ligne_.total;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.designation||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_achat co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
						WHERE dv.id = element_ GROUP BY tx.id, tx.libelle 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(ligne_.augmentation IS TRUE)THEN
								_debit_ = ligne_.total;
							ELSE
								_credit_ = ligne_.total;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.libelle||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'FRAIS_MISSION')THEN
			SELECT INTO data_ f.id, f.date_paiement, f.montant, c.journal, c.compte, o.compte_charge, m.ordre, m.numero_mission FROM yvs_compta_caisse_piece_mission f LEFT JOIN yvs_grh_missions m ON f.mission = m.id 
				LEFT JOIN yvs_grh_objets_mission o ON m.objet_mission = o.id LEFT JOIN yvs_base_caisse c ON f.caisse = c.id WHERE f.id = element_;
			IF(data_.numero_mission IS NOT NULL AND data_.numero_mission NOT IN ('', ' '))THEN
				_jour_ = to_char(data_.date_paiement ,'MM')::integer;
				_num_piece_ = data_.numero_mission;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.ordre;
				_compte_tiers_ = null;
				
				_credit_ = 0;
				_debit_ = data_.montant;
				_compte_general_ = data_.compte_charge;	
				_num_ref_ = _num_piece_ ||'-01';						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
				i_ = i_ + 1;
				
				_credit_ = data_.montant;
				_debit_ = 0;
				_compte_general_ = data_.compte;	
				_num_ref_ = _num_piece_ ||'-02';						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_VENTE')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, p.caisse, p.vente, d.num_doc, d.client, t.compte as compte_tiers, t.suivi_comptable, t.tiers, m.compte_tiers AS tiers_interne, c.compte FROM yvs_compta_caisse_piece_vente p LEFT JOIN yvs_base_caisse c ON p.caisse = c.id 
				LEFT JOIN yvs_com_doc_ventes d ON p.vente = d.id LEFT JOIN yvs_com_client t ON d.client = t.id LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
				LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_grh_employes m ON u.id = m.code_users WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN
				_jour_ = to_char(data_.date_paiement ,'MM')::integer;
				_num_piece_ = data_.numero_piece;	
				_echeance_ = data_.date_paiement;
				_libelle_ = 'Reglement Facture vente N° ';
				IF(data_.vente IS NULL OR data_.vente < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune facture';
				ELSE
					_libelle_ = _libelle_||data_.num_doc;
				END IF;
				
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune caisse';
				ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = 0;
				_debit_ = data_.montant;	
				_num_ref_ = _num_piece_ ||'-01';						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
				i_ = i_ + 1;

				_error_ = '';
				IF(data_.vente IS NOT NULL AND data_.vente > 0)THEN
					IF(data_.client IS NULL OR data_.client < 1)THEN
						_error_ = 'ce reglement est rattaché à une facture qui n''a pas de client';
					ELSE
						IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte_tiers;
						END IF;
						IF(data_.suivi_comptable)THEN
							IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
								_error_ = 'car ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte tiers';
							ELSE
								_compte_tiers_ = data_.tiers;
							END IF;
						ELSE
							IF(data_.tiers_interne IS NULL OR data_.tiers_interne < 1)THEN
								_error_ = 'car ce reglement est rattaché à une facture qui est liée à un vendeur qui n''a pas de compte tiers';
							ELSE
								_compte_tiers_ = data_.tiers_interne;
							END IF;
						END IF;
					END IF;
				END IF;
				_credit_ = data_.montant;
				_debit_ = 0;	
				_num_ref_ = _num_piece_ ||'-02';						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_ACHAT')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, p.caisse, p.achat, d.num_doc, d.fournisseur, t.compte AS compte_tiers, t.tiers, c.compte FROM yvs_compta_caisse_piece_achat p 
				LEFT JOIN yvs_base_caisse c ON p.caisse = c.id LEFT JOIN yvs_com_doc_achats d ON p.achat = d.id LEFT JOIN yvs_base_fournisseur t ON d.fournisseur = t.id WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN
				_jour_ = to_char(data_.date_paiement ,'MM')::integer;
				_num_piece_ = data_.numero_piece;	
				_echeance_ = data_.date_paiement;
				_libelle_ = 'Reglement Facture achat N° ';
				
				IF(data_.achat IS NULL OR data_.achat < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune facture';
				ELSE
					_libelle_ = _libelle_||data_.num_doc;
					IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
						_error_ = 'ce reglement est rattaché à une facture qui n''a pas de fournisseur';
					ELSE
						IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
							_error_ = 'car ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte tiers';
						ELSE
							_compte_tiers_ = data_.tiers;
						END IF;
						IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte_tiers;
						END IF;
					END IF;
				END IF;
				_credit_ = 0;
				_debit_ = data_.montant;
				_num_ref_ = _num_piece_ ||'-01';						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
				i_ = i_ + 1;
				
				_error_ = '';
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune caisse';
				ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_compte_tiers_ = null;				
				_credit_ = data_.montant;
				_debit_ = 0;		
				_num_ref_ = _num_piece_ ||'-02';						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_DIVERS')THEN
			SELECT INTO data_ p.id, p.num_piece, p.date_piece, p.montant, p.caisse, p.doc_divers, d.num_piece, CONCAT(d.type_doc, '') AS type_doc, CONCAT(d.mouvement, 'D') AS mouvement, d.tiers, d.compte_general, t.compte_collectif, c.compte FROM yvs_compta_caisse_piece_divers p 
				LEFT JOIN yvs_base_caisse c ON p.caisse = c.id LEFT JOIN yvs_compta_caisse_doc_divers d ON p.doc_divers = d.id LEFT JOIN yvs_base_tiers t ON d.tiers = t.id WHERE p.id = element_;
			IF(data_.num_piece IS NOT NULL AND data_.num_piece NOT IN ('', ' '))THEN
				_jour_ = to_char(data_.date_piece ,'MM')::integer;
				_num_piece_ = data_.num_piece;	
				_echeance_ = data_.date_piece;
				_libelle_ = 'Reglement Opération diverse N° ';
				
				IF(data_.doc_divers IS NULL OR data_.doc_divers < 1)THEN
					_error_ = 'ce reglement n''est associé à aucun document';
				ELSE
					_libelle_ = _libelle_||data_.num_piece;
				END IF;
				
				_error_ = '';
				_compte_general_ = null;
				_compte_tiers_ = null;
				_debit_ = 0;
				_credit_ = 0;
				IF(data_.type_doc = 'CTD' OR data_.type_doc = 'RTD')THEN
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'car ce reglement est rattaché à un document qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
						IF(data_.compte_collectif IS NULL OR data_.compte_collectif < 1)THEN
							_error_ = 'car ce reglement est rattaché à un document de tiers qui n''a pas de compte collectif';
						ELSE
							_compte_general_ = data_.compte_collectif;
						END IF;
					END IF;					
				ELSE
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN							
						IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
							_error_ = 'car ce reglement est rattaché à un document de tiers qui n''a pas de compte collectif';
						ELSE
							_compte_general_ = data_.compte_general;
						END IF;	
					ELSE
						_compte_tiers_ = data_.tiers;
						IF(data_.compte_collectif IS NULL OR data_.compte_collectif < 1)THEN
							_error_ = 'car ce reglement est rattaché à un document de tiers qui n''a pas de compte collectif';
						ELSE
							_compte_general_ = data_.compte_collectif;
						END IF;
					END IF;				
				END IF;			
				IF(data_.mouvement = 'D')THEN
					_credit_ = data_.montant;
				ELSE	
					_debit_ = data_.montant;					
				END IF;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
				i_ = i_ + 1;
				
				_error_ = '';
				_compte_general_ = null;
				_compte_tiers_ = null;
				_credit_ = 0;
				_debit_ = 0;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune caisse';
				ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				IF(data_.mouvement = 'D')THEN
					_debit_ = data_.montant;
				ELSE
					_credit_ = data_.montant;					
				END IF;	
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'DOC_VIREMENT')THEN
			SELECT INTO data_ p.numero_piece, p.date_paiement, p.montant, p.cible, p.source, c.compte AS compte_cible, s.compte AS compte_source FROM yvs_compta_caisse_piece_virement p LEFT JOIN yvs_base_caisse s ON p.source = s.id LEFT JOIN yvs_base_caisse c ON p.cible = c.id WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN
				_jour_ = to_char(data_.date_paiement ,'MM')::integer;
				_num_piece_ = data_.numero_piece;	
				_echeance_ = data_.date_paiement;
				_libelle_ = 'Virement N° '||_num_piece_;

				_error_ = '';
				_debit_ = 0;
				_compte_general_ = null;
				IF(data_.source IS NULL OR data_.source < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune emetteur';
				ELSIF(data_.compte_source IS NULL OR data_.compte_source < 1)THEN
					_error_ = 'ce reglement est rattaché à un emetteur qui n''a pas de compte général';
				ELSE
					_compte_general_ = data_.compte_source;
				END IF;
				_credit_ = data_.montant;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				_credit_ = 0;
				IF(data_.cible IS NULL OR data_.cible < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune recepteur';
				ELSIF(data_.compte_cible IS NULL OR data_.compte_cible < 1)THEN
					_error_ = 'ce reglement est rattaché à un recepteur qui n''a pas de compte général';
				ELSE
					_compte_general_ = data_.compte_cible;
				END IF;
				_debit_ = data_.montant;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'ORDRE_SALAIRE')THEN
			SELECT INTO data_ o.* FROM yvs_grh_ordre_calcul_salaire o WHERE o.id = element_;
			IF(data_.reference IS NOT NULL AND data_.reference NOT IN ('', ' '))THEN
				_jour_ = to_char(data_.date_execution ,'MM')::integer;
				_num_piece_ = data_.reference;	
				_echeance_ = data_.date_execution;
				-- Recupere gains
				-- récupère les éléments de gains en groupant par compte charge du gains
				FOR ligne_ IN SELECT ag.designation, el.nom, el.compte_charge, COALESCE(SUM(db.montant_payer),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition FROM yvs_grh_detail_bulletin db 
					INNER JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire INNER JOIN yvs_grh_contrat_emps c ON c.id=b.contrat 
					INNER JOIN yvs_grh_employes e ON e.id=c.employe INNER JOIN yvs_agences ag ON e.agence=ag.id LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id 
					WHERE el.visible_bulletin IS TRUE AND NOT (COALESCE(db.montant_payer,0)=0 AND COALESCE(db.retenu_salariale,0)=0 AND COALESCE(db.montant_employeur,0)=0) 
					AND el.actif IS TRUE AND el.retenue IS FALSE AND b.entete = element_ AND ag.id = agence_
					GROUP BY ag.id, el.id, co.id ORDER BY ag.designation, el.nom
				LOOP
					_libelle_ = ligne_.nom;

					_error_ = '';
					_credit_ = 0;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.id IS NULL OR ligne_.id < 1)THEN
						_error_ = 'certains bulletins ne sont pas rattachés aux elements de salaire';
					ELSIF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'le gain '||ligne_.nom||' n''est rattaché à aucun compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
					_debit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT yvs_compta_content_analytique(agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, ligne_.saisie_analytique, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- récupère les éléments de gains en groupant par compte collectif employé
				FOR ligne_ IN SELECT ag.designation, co.intitule, e.compte_collectif, COALESCE(SUM(db.montant_payer),0) AS montant, co.saisie_analytique 
					FROM yvs_grh_detail_bulletin db INNER JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					INNER JOIN yvs_grh_contrat_emps c ON c.id=b.contrat INNER JOIN yvs_grh_employes e ON e.id=c.employe INNER JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON e.compte_collectif = co.id 
					WHERE el.visible_bulletin IS TRUE AND NOT (COALESCE(db.montant_payer,0)=0 AND COALESCE(db.retenu_salariale,0)=0 AND COALESCE(db.montant_employeur,0)=0) 
					AND el.actif IS TRUE AND el.retenue IS FALSE AND b.entete = element_ AND ag.id = agence_
					GROUP BY ag.id, co.id, e.compte_collectif ORDER BY ag.designation, e.compte_collectif, co.intitule
				LOOP
					_libelle_ = 'Gains liés à '||ligne_.intitule;

					_error_ = '';
					_debit_ = 0;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_collectif IS NULL OR ligne_.compte_collectif < 1)THEN
						_error_ = 'le gain '||ligne_.nom||' est rattaché à des employes qui n''ont pas de compte collectif';
					ELSE
						_compte_general_ = ligne_.compte_collectif;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT yvs_compta_content_analytique(agence_, element_, 0, _compte_general_, _compte_tiers_, _credit_, '', ligne_.saisie_analytique, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- Recupere retenues
				-- récupère les retenues patronales
				FOR ligne_ IN SELECT ag.designation, el.nom, el.compte_charge, el.compte_cotisation, COALESCE(SUM(db.montant_employeur),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition 
					FROM yvs_grh_detail_bulletin db INNER JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					INNER JOIN yvs_grh_contrat_emps c ON c.id=b.contrat INNER JOIN yvs_grh_employes e ON e.id=c.employe INNER JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.montant_employeur,0)!=0) AND el.actif IS TRUE AND el.retenue IS TRUE AND b.entete = element_ AND ag.id = agence_
					GROUP BY ag.id, el.id, el.compte_charge, el.compte_cotisation, co.id ORDER BY ag.designation , el.compte_charge
				LOOP
					_libelle_ = ligne_.nom;

					_error_ = '';
					_debit_ = 0;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_cotisation IS NULL OR ligne_.compte_cotisation < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de cotisation';
					ELSE
						_compte_general_ = ligne_.compte_cotisation;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT yvs_compta_content_analytique(agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _credit_, ligne_.mode_repartition, ligne_.saisie_analytique, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_);
						END LOOP;
					END IF;
					i_ = i_ + 1;	

					_error_ = '';
					_credit_ = 0;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
					_debit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT yvs_compta_content_analytique(agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, ligne_.saisie_analytique, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- récupère les retenues salariales sans saisie tiers en groupant par compte de cotisation des retenues
				FOR ligne_ IN SELECT ag.designation, el.nom, el.compte_cotisation, COALESCE(SUM(db.retenu_salariale),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition 
					FROM yvs_grh_detail_bulletin db INNER JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					INNER JOIN yvs_grh_contrat_emps c ON c.id=b.contrat INNER JOIN yvs_grh_employes e ON e.id=c.employe INNER JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON el.compte_cotisation = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND (el.saisi_compte_tiers IS null or el.saisi_compte_tiers IS FALSE) 
					AND el.retenue IS TRUE AND b.entete = element_ AND ag.id = agence_
					GROUP BY ag.id, el.id, el.compte_cotisation, co.id ORDER BY ag.designation , el.nom
				LOOP
					_libelle_ = ligne_.nom;

					_error_ = '';
					_debit_ = 0;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_cotisation IS NULL OR ligne_.compte_cotisation < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de cotisation';
					ELSE
						_compte_general_ = ligne_.compte_cotisation;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT yvs_compta_content_analytique(agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _credit_, ligne_.mode_repartition, ligne_.saisie_analytique, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;
				-- récupère les retenues salariales sans saisie tiers en groupant par compte collectif des employes
				FOR ligne_ IN SELECT ag.designation, co.intitule, e.compte_collectif, COALESCE(SUM(db.retenu_salariale),0) AS montant, co.saisie_analytique 
                                        FROM yvs_grh_detail_bulletin db INNER JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
                                        INNER JOIN yvs_grh_contrat_emps c ON c.id=b.contrat INNER JOIN yvs_grh_employes e ON e.id=c.employe INNER JOIN yvs_agences ag ON e.agence=ag.id 
                                        LEFT JOIN yvs_base_plan_comptable co ON e.compte_collectif = co.id 
                                        WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND (el.saisi_compte_tiers IS null or el.saisi_compte_tiers IS FALSE) 
                                        AND el.retenue IS TRUE AND b.entete = element_ AND ag.id = agence_
                                        GROUP BY ag.id,  co.id, e.compte_collectif ORDER BY ag.designation , e.compte_collectif, co.intitule
				LOOP
					_libelle_ = ligne_.intitule;

					_error_ = '';
					_credit_ = 0;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_collectif IS NULL OR ligne_.compte_collectif < 1)THEN
						_error_ = 'certaines retenues sont rattachées à des employés sans compte collectif';
					ELSE
						_compte_general_ = ligne_.compte_collectif;
					END IF;
					_debit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT yvs_compta_content_analytique(agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, ligne_.saisie_analytique, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;	
				-- récupère les retenues salariales avec saisie tiers en groupant par compte de charge des retenues
				FOR ligne_ IN SELECT ag.designation, el.nom, el.compte_charge, COALESCE(SUM(db.retenu_salariale),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition 
                                        FROM yvs_grh_detail_bulletin db INNER JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
                                        INNER JOIN yvs_grh_contrat_emps c ON c.id=b.contrat INNER JOIN yvs_grh_employes e ON e.id=c.employe INNER JOIN yvs_agences ag ON e.agence=ag.id 
                                        LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id INNER JOIN yvs_base_tiers t ON e.compte_tiers = t.id 
                                        WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND el.saisi_compte_tiers IS TRUE 
                                        AND el.retenue IS TRUE AND b.entete = element_ AND ag.id = agence_
                                        GROUP BY ag.id, el.id, el.compte_charge, co.id ORDER BY ag.designation, el.nom
				LOOP
					_libelle_ = ligne_.nom;

					_error_ = '';
					_credit_ = 0;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' rattachée à des employés sans compte collectif';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
					_debit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT yvs_compta_content_analytique(agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, ligne_.saisie_analytique, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;	
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;	
				-- récupère les retenues salariales avec saisie tiers par compte collectif employé
				FOR ligne_ IN SELECT ag.designation, el.nom, e.compte_collectif, COALESCE(SUM(db.retenu_salariale),0) AS montant, concat(e.nom, ' ', e.prenom) AS nom_prenom, t.id, co.saisie_analytique, el.id, el.mode_repartition 
                                        FROM yvs_grh_detail_bulletin db INNER JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
                                        INNER JOIN yvs_grh_contrat_emps c ON c.id=b.contrat INNER JOIN yvs_grh_employes e ON e.id=c.employe INNER JOIN yvs_agences ag ON e.agence=ag.id 
                                        LEFT JOIN yvs_base_plan_comptable co ON e.compte_collectif = co.id LEFT JOIN yvs_base_tiers t ON e.compte_tiers = t.id 
                                        WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND el.saisi_compte_tiers IS TRUE 
                                        AND el.retenue IS TRUE AND b.entete = element_ AND ag.id = agence_
                                        GROUP BY ag.id, el.id, e.id, t.id, co.id ORDER BY ag.designation , e.compte_collectif, el.nom
				LOOP
					_libelle_ = ligne_.nom;

					_error_ = '';
					_debit_ = 0;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.compte_collectif IS NULL OR ligne_.compte_collectif < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||'n''a pas de compte de cotisation';
					ELSE
						_compte_general_ = ligne_.compte_collectif;
					END IF;
					IF(ligne_.id IS NULL OR ligne_.id < 1)THEN
						_error_ = 'l''employé '||ligne_.nom_prenom||' n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = ligne_.id;
						_libelle_ = _libelle_|| ' de '||ligne_.nom_prenom;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT yvs_compta_content_analytique(agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _credit_, ligne_.mode_repartition, ligne_.saisie_analytique, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;	
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, _libelle_, _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;
			END IF;
		END IF;
	END IF;
	RETURN QUERY SELECT * FROM table_compta_content_journal ORDER BY _debit DESC, _id DESC, _jour, _num_piece;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION yvs_compta_content_journal(bigint, bigint, character varying)
  OWNER TO postgres;
