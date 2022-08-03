-- Function: com_et_ecart_vente(bigint, bigint, bigint, date, date, character varying, boolean)
DROP FUNCTION com_et_ecart_vente(bigint, bigint, bigint, date, date, character varying);
CREATE OR REPLACE FUNCTION com_et_ecart_vente(IN societe_ bigint, IN agence_ bigint, IN element_ character varying, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN by_point_ boolean)
  RETURNS TABLE(agence bigint, element bigint, code character varying, nom character varying, entete character varying, versement_attendu double precision, versement_reel double precision, ecart double precision, solde double precision, rang integer) AS
$BODY$
declare 

   vendeur_ RECORD;
   dates_ RECORD;
   
   jour_ character varying;
   ids_ character varying default '0';
   
   versement_attendu_ double precision default 0;
   versement_reel_ double precision default 0;
   ecart_ double precision default 0;
   solde_ double precision default 0;
   
   query_ CHARACTER VARYING DEFAULT '';
   query_montant_ CHARACTER VARYING DEFAULT '';

   i integer default 0;
   
begin 	
	--DROP TABLE IF EXISTS table_et_ecart_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ecart_vente(_agence bigint, _element bigint, _code character varying, _nom character varying, _entete character varying, _versement_attendu double precision, _versement_reel double precision, _ecart double precision, _solde double precision, _rang integer);
	DELETE FROM table_et_ecart_vente;
	IF(by_point_)THEN
		query_ = 'SELECT DISTINCT y.id::bigint as id, y.code as code, y.libelle as nom, y.agence::bigint as agence FROM yvs_base_point_vente y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.code IS NOT NULL';
	ELSE
		query_ = 'SELECT DISTINCT y.id::bigint as id, y.code_users as code, y.nom_users as nom, y.agence::bigint as agence FROM yvs_com_ecart_entete_vente e INNER JOIN yvs_users y ON e.users = y.id INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.code_users IS NOT NULL';
	END IF;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND y.agence = '||agence_;
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_;
	END IF;
	IF(COALESCE(element_, '') NOT IN ('', ' ', '0'))THEN
		FOR jour_ IN SELECT head FROM regexp_split_to_table(element_,',') head
		LOOP
			ids_ = ids_ ||','||jour_;
		END LOOP;
		query_ = query_ || ' AND y.id IN ('||ids_||')';
	END IF;
	RAISE NOTICE 'query_ %',query_;
	FOR vendeur_ IN EXECUTE query_
	LOOP
		i = 0;
		solde_ = 0;
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_, true)
		LOOP
			i = i + 1;
			jour_ = dates_.intitule;
			IF(by_point_)THEN
				query_montant_ = 'SELECT SUM(com_get_versement_attendu(y.id::character varying)) FROM yvs_com_entete_doc_vente y INNER JOIN yvs_com_creneau_horaire_users h ON y.creneau = h.id INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE c.point = '||vendeur_.id;
			ELSE
				query_montant_ = 'SELECT SUM(com_get_versement_attendu(y.id::character varying)) FROM yvs_com_entete_doc_vente y INNER JOIN yvs_com_creneau_horaire_users h ON y.creneau = h.id WHERE h.users = '||vendeur_.id;
			END IF;
			query_montant_ = query_montant_ || ' AND y.date_entete BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			EXECUTE query_montant_ INTO versement_attendu_;
			versement_attendu_ = COALESCE(versement_attendu_, 0);
			
			IF(by_point_)THEN
				query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source IN (SELECT DISTINCT h.users FROM yvs_com_creneau_horaire_users h INNER JOIN yvs_com_creneau_point c ON h.creneau_point = c.id WHERE c.point = '||vendeur_.id||')';
			ELSE
				query_montant_ = 'SELECT SUM(y.montant) FROM yvs_compta_caisse_piece_virement y WHERE y.caissier_source = '||vendeur_.id;
			END IF;
			query_montant_ = query_montant_ || ' AND y.statut_piece = ''P'' AND y.date_paiement BETWEEN '||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin);
			EXECUTE query_montant_ INTO versement_reel_;
			versement_reel_ = COALESCE(versement_reel_, 0);
			
			ecart_ = versement_attendu_ - versement_reel_;
			solde_ = solde_ + ecart_;
			IF(ecart_ != 0)THEN
				INSERT INTO table_et_ecart_vente VALUES(vendeur_.agence, vendeur_.id::bigint, vendeur_.code, vendeur_.nom, jour_, versement_attendu_, versement_reel_, ecart_, solde_, i);
			END IF;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_ecart_vente ORDER BY _agence, _code, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ecart_vente(bigint, bigint, character varying, date, date, character varying, boolean)
  OWNER TO postgres;
  
  
-- Function: com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean)
-- DROP FUNCTION com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION com_et_ristourne_vente(IN societe_ bigint, IN users_ bigint, IN client_ bigint, IN date_debut_ date, IN date_fin_ date, IN period_ character varying, IN cumul_ boolean)
  RETURNS TABLE(client bigint, code character varying, nom character varying, users bigint, code_users character varying, nom_users character varying, unite bigint, entete character varying, quantite double precision, prix_total double precision, ristourne double precision, rang integer) AS
$BODY$
declare 

   clients_ RECORD;
   data_ RECORD;
   dates_ RECORD;
   
   jour_ character varying;
   
   ristourne_ CHARACTER VARYING DEFAULT 'SELECT u.id, u.code_users, u.nom_users, c.ristourne, c.quantite, c.prix_total, c.conditionnement';
   colonne_ CHARACTER VARYING DEFAULT 'SELECT DISTINCT y.id, y.code_client, CONCAT(y.nom, '' '', y.prenom) AS nom';
   save_ CHARACTER VARYING DEFAULT ' FROM yvs_com_client y INNER JOIN yvs_com_doc_ventes d ON d.client = y.id INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
						INNER JOIN yvs_com_contenu_doc_vente c ON c.doc_vente = d.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id
						WHERE e.date_entete BETWEEN'||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND COALESCE(c.ristourne, 0) > 0 AND a.societe = '||societe_;
   where_ CHARACTER VARYING DEFAULT '';
   query_ CHARACTER VARYING DEFAULT '';

   i integer default 0;
   
begin 	
	DROP TABLE IF EXISTS table_et_ristourne_vente;
	CREATE TEMP TABLE IF NOT EXISTS table_et_ristourne_vente(_client bigint, _code character varying, _nom character varying, _users bigint, _code_users character varying, _nom_users character varying, _unite bigint, _entete character varying, _quantite double precision, _prix_total double precision, _ristourne double precision, _rang integer);
	DELETE FROM table_et_ristourne_vente;
	IF(users_ IS NOT NULL AND users_ > 0)THEN
		save_ = save_ || ' AND u.id = '||users_;
	END IF;
	IF(client_ IS NOT NULL AND client_ > 0)THEN
		where_ = ' AND y.id = '||client_;
	END IF;
-- 	RAISE NOTICE 'query_ %',query_;
	IF(cumul_)THEN
		ristourne_ = 'SELECT u.id, u.code_users, u.nom_users, SUM(COALESCE(c.ristourne, 0)) as ristourne, SUM(COALESCE(c.quantite, 0)) as quantite, SUM(COALESCE(c.prix_total, 0)) as prix_total, c.conditionnement';
	END IF;
	FOR clients_ IN EXECUTE colonne_ || save_ || where_
	LOOP
		i = 0;
		FOR dates_ IN SELECT * FROM decoupage_interval_date(date_debut_, date_fin_, period_, true)
		LOOP
			i = i + 1;
			jour_ = dates_.intitule;
			query_ = ristourne_ || save_ || ' AND e.date_entete BETWEEN'||QUOTE_LITERAL(dates_.date_debut)||' AND '||QUOTE_LITERAL(dates_.date_fin)||' AND y.id = '||clients_.id;
			IF(cumul_)THEN
				query_ = query_ || ' GROUP BY u.id, c.conditionnement';
			END IF;
			FOR data_ IN EXECUTE query_
			LOOP
				IF(COALESCE(data_.ristourne, 0) != 0)THEN
					INSERT INTO table_et_ristourne_vente VALUES(clients_.id, clients_.code_client, clients_.nom, data_.id, data_.code_users, data_.nom_users, data_.conditionnement, jour_, data_.quantite, data_.prix_total, data_.ristourne, i);
				END IF;
			END LOOP;
		END LOOP;
	END LOOP;
	RETURN QUERY SELECT * FROM table_et_ristourne_vente ORDER BY _client, _code, _rang;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION com_et_ristourne_vente(bigint, bigint, bigint, date, date, character varying, boolean)
  OWNER TO postgres;
  
  
-- Function: decoupage_interval_date(date, date, character varying, boolean)
-- DROP FUNCTION decoupage_interval_date(date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION decoupage_interval_date(IN date_debut_ date, IN date_fin_ date, IN periode_ character varying, IN view_date_ boolean)
  RETURNS TABLE(intitule character varying, date_debut date, date_fin date) AS
$BODY$
DECLARE

    date_ date;
    date_action_ date default date_debut_;

    intitule_ character varying;
    jour_ character varying;
    jour_0 character varying;
    mois_ character varying;
    mois_0 character varying;
    annee_ character varying;
    annee_0 character varying;

    pattern_ character varying default 'dd'; 

BEGIN    
-- 	DROP TABLE IF EXISTS table_decoupage_interval_date;
	CREATE TEMP TABLE IF NOT EXISTS table_decoupage_interval_date(_intitule character varying, _date_debut date, _date_fin date);
	DELETE FROM table_decoupage_interval_date;
	if(view_date_)then
		pattern_ = 'dd-MM-yyyy';
	else
		if(to_char(date_debut_ , 'MM') != to_char(date_fin_ , 'MM'))then
			pattern_ =  pattern_ || '-MM';
		end if;
		if(to_char(date_debut_ , 'yyyy') != to_char(date_fin_ , 'yyyy'))then
			pattern_ =  pattern_ || '-yyyy';
		end if;
	end if;
	while(date_action_ <= date_fin_)
	loop
		if(periode_ = 'A')then
			date_ = (date_action_ + interval '1 year' - interval '1 day');
			if(date_ > date_fin_)then
				date_ = date_ - (date_ - date_fin_);
			end if;		
			intitule_ = (select extract(year from date_action_));	
		elsif(periode_ = 'T')then
			date_ = (date_action_ + interval '3 month' - interval '1 day');
			if(date_ > date_fin_)then
				date_ = date_ - (date_ - date_fin_);
			end if;		
			jour_0 = (select extract(month from date_action_));
			if(char_length(jour_0)<2)then
				jour_0 = '0'||jour_0;
			end if;
			jour_ = '('||jour_0||'/';
			
			jour_0 = (select extract(month from date_));
			if(char_length(jour_0)<2)then
				jour_0 = '0'||jour_0;
			end if;
			intitule_ = jour_||jour_0||')';		
		elsif(periode_ = 'M')then
			date_ = (date_action_ + interval '1 month' - interval '1 day');
			if(date_ > date_fin_)then
				date_ = date_ - (date_ - date_fin_);
			end if;		
			jour_ = (select extract(month from date_action_));
			if(char_length(jour_)<2)then
				jour_ = '0'||jour_;
			end if;			
			annee_ = (select extract(year from date_action_));	
			intitule_ = jour_ ||'-'|| annee_;
		elsif(periode_ = 'S')then
			date_ = (date_action_ + interval '1 week' - interval '1 day');	
			if(date_ > date_fin_)then
				date_ = date_ - (date_ - date_fin_);
			end if;
			
			jour_0 = (select extract(day from date_action_));
			if(char_length(jour_0)<2)then
				jour_0 = '0'||jour_0;
			end if;
			jour_ = '('||jour_0||'/';
			jour_0 = (select extract(day from date_));
			if(char_length(jour_0)<2)then
				jour_0 = '0'||jour_0;
			end if;
			jour_ = jour_||jour_0||')-';
			
			mois_ = (select extract(month from date_));
			if(char_length(mois_)<2)then
				mois_ = '0'||mois_;
			end if;
			mois_0 = (select extract(month from date_action_));
			if(char_length(mois_0)<2)then
				mois_0 = '0'||mois_0;
			end if;
			if(mois_ != mois_0)then
				mois_ = '('|| mois_0 || '/'|| mois_ ||')';
			end if;
			intitule_ = jour_ || mois_;
		else
			date_ = (date_action_ + interval '0 day');
			if(date_ > date_fin_)then
				date_ = date_ - (date_ - date_fin_);
			end if;	
			intitule_ = to_char(date_action_ , pattern_);
		end if;
		
		INSERT INTO table_decoupage_interval_date VALUES(intitule_, date_action_, date_);
	
		if(periode_ = 'A')then
			date_action_ = date_action_ + interval '1 year';
		elsif(periode_ = 'T')then
			date_action_ = date_action_ + interval '3 month';
		elsif(periode_ = 'M')then
			date_action_ = date_action_ + interval '1 month';
		elsif(periode_ = 'S')then
			date_action_ = date_action_ + interval '1 week';
		else
			date_action_ = date_action_ + interval '1 day';
		end if;
	end loop;
	return QUERY select * from table_decoupage_interval_date;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION decoupage_interval_date(date, date, character varying, boolean)
  OWNER TO postgres;

  
-- Function: decoupage_interval_date(date, date, character varying)
-- DROP FUNCTION decoupage_interval_date(date, date, character varying);
CREATE OR REPLACE FUNCTION decoupage_interval_date(IN date_debut_ date, IN date_fin_ date, IN periode_ character varying)
  RETURNS TABLE(intitule character varying, date_debut date, date_fin date) AS
$BODY$
DECLARE

BEGIN    
	return QUERY select * from decoupage_interval_date(date_debut_, date_fin_, periode_, false);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION decoupage_interval_date(date, date, character varying)
  OWNER TO postgres;

  
  
-- Function: compta_et_grand_livre(bigint, bigint, character varying, character varying, date, date, character varying, boolean)
-- DROP FUNCTION compta_et_grand_livre(bigint, bigint, character varying, character varying, date, date, character varying, boolean);
CREATE OR REPLACE FUNCTION compta_et_grand_livre(IN societe_ bigint, IN agence_ bigint, IN compte_debut_ character varying, IN compte_fin_ character varying, IN date_debut_ date, IN date_fin_ date, IN type_ character varying, IN lettrer_ boolean)
  RETURNS TABLE(id bigint, code character varying, intitule character varying, compte character varying, designation character varying, numero character varying, date_piece date, jour integer, libelle character varying, lettrage character varying, journal character varying, debit double precision, credit double precision) AS
$BODY$
declare 
	comptes_ record;
	
	credit_si_ bigint default 0;
	debit_si_ bigint default 0;
	
	query_ character varying default '';
	last_code_ character varying default '';
begin 	
	DROP TABLE IF EXISTS table_et_grand_livre;
	CREATE TEMP TABLE IF NOT EXISTS table_et_grand_livre(_id bigint, _code character varying, _intitule character varying, _compte character varying, _designation character varying, _numero character varying, _date_piece date, _jour integer, _libelle character varying, _lettrage character varying, _journal character varying, _debit double precision, _credit double precision); 
	DELETE FROM table_et_grand_livre;
	IF(type_ = 'T')THEN
		query_ = 'SELECT p.num_piece, y.jour, y.num_piece, y.num_ref, y.libelle, y.lettrage, y.debit, y.credit, y.compte_tiers as id, c.num_compte as compte, c.intitule as designation, p.date_piece, j.code_journal as journal, name_tiers(y.compte_tiers, y.table_tiers, ''R'') as code, name_tiers(y.compte_tiers, y.table_tiers, ''R'') as intitule
				FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id
				WHERE p.date_piece BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND name_tiers(y.compte_tiers, y.table_tiers, ''R'') BETWEEN '||QUOTE_LITERAL(compte_debut_)||' AND '||QUOTE_LITERAL(compte_fin_);
	ELSIF(type_ = 'C') THEN
		query_ = 'SELECT p.num_piece, y.jour, y.num_piece, y.num_ref, y.libelle, y.lettrage, y.debit, y.credit, c.id, c.num_compte as code, c.intitule, p.date_piece, j.code_journal as journal, null as compte, null as designation
				FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id
				WHERE p.date_piece BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND c.num_compte BETWEEN '||QUOTE_LITERAL(compte_debut_)||' AND '||QUOTE_LITERAL(compte_fin_);
			
	ELSE
		query_ = 'SELECT p.num_piece, y.jour, y.num_piece, y.num_ref, y.libelle, y.lettrage, l.debit, l.credit, n.id, c.num_compte as compte, c.intitule as designation, p.date_piece, j.code_journal as journal, n.code_ref as code, n.designation as intitule
				FROM yvs_compta_pieces_comptable p INNER JOIN yvs_compta_content_journal y ON p.id = y.piece INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
				INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id
				INNER JOIN yvs_compta_content_analytique l ON y.id = l.contenu INNER JOIN yvs_compta_centre_analytique n ON n.id = l.centre
				WHERE p.date_piece BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_)||' AND n.code_ref BETWEEN '||QUOTE_LITERAL(compte_debut_)||' AND '||QUOTE_LITERAL(compte_fin_);
	END IF;
	IF(agence_ IS NOT NULL AND agence_ > 0)THEN
		query_ = query_ || ' AND a.id = '||agence_||'';
	END IF;
	IF(societe_ IS NOT NULL AND societe_ > 0)THEN
		query_ = query_ || ' AND a.societe = '||societe_||'';
	END IF;
	IF(lettrer_ IS NOT NULL)THEN
		query_ = query_ || ' AND COALESCE(LENGTH(y.lettrage), 0)';
		IF(lettrer_)THEN
			query_ = query_ || '>0';
		ELSE
			query_ = query_ || '<=0';
		END IF;
	END IF;
	IF(type_ = 'T')THEN
		query_ = query_ || ' ORDER BY name_tiers(y.compte_tiers, y.table_tiers, ''R''), p.date_piece, p.num_piece';
	ELSIF(type_ = 'C')THEN
		query_ = query_ || ' ORDER BY c.num_compte, p.date_piece, p.num_piece';
	ELSE
		query_ = query_ || ' ORDER BY n.code_ref, p.date_piece, p.num_piece';
	END IF;
	IF(query_ IS NOT NULL AND query_ != '')THEN
		FOR comptes_ IN EXECUTE query_
		LOOP
			IF(last_code_ != comptes_.code)THEN
				debit_si_ = (select compta_et_debit_initial(agence_, societe_, comptes_.id, comptes_.date_piece, type_, false));
				credit_si_ = (select compta_et_credit_initial(agence_, societe_, comptes_.id, comptes_.date_piece, type_, false));			
				last_code_ = comptes_.code;
				INSERT INTO table_et_grand_livre VALUES(comptes_.id, comptes_.code, comptes_.intitule, comptes_.compte, comptes_.designation, 'S.I', (comptes_.date_piece - interval '1 day'), to_char((comptes_.date_piece - interval '1 day'), 'dd')::integer, 'Solde Initial', '', comptes_.journal, debit_si_, credit_si_);
			END IF;
			INSERT INTO table_et_grand_livre VALUES(comptes_.id, comptes_.code, comptes_.intitule, comptes_.compte, comptes_.designation, comptes_.num_piece, comptes_.date_piece, comptes_.jour, comptes_.libelle, comptes_.lettrage, comptes_.journal, comptes_.debit, comptes_.credit);
		END LOOP;			
	END IF;
	RETURN QUERY SELECT * FROM table_et_grand_livre ORDER BY _code, _date_piece, _numero;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compta_et_grand_livre(bigint, bigint, character varying, character varying, date, date, character varying, boolean)
  OWNER TO postgres;

  
-- Function: com_get_versement_attendu(bigint, date, date)
-- DROP FUNCTION com_get_versement_attendu(bigint, date, date);
CREATE OR REPLACE FUNCTION com_get_versement_attendu(users_ bigint, date_debut_ date, date_fin_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;
BEGIN    
	SELECT INTO ca_ SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id WHERE d.type_doc = 'FV' AND d.document_lie IS NULL AND h.users = users_ AND e.date_entete BETWEEN date_debut_ AND date_fin_;
	SELECT INTO avance_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE d.type_doc = 'BCV' AND p.caissier = users_ AND p.date_paiement BETWEEN date_debut_ AND date_fin_;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avance : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu(bigint, date, date)
  OWNER TO postgres;

  
-- Function: com_get_versement_attendu(character varying)
-- DROP FUNCTION com_get_versement_attendu(character varying);
CREATE OR REPLACE FUNCTION com_get_versement_attendu(headers_ character varying)
  RETURNS double precision AS
$BODY$
DECLARE
	ca_ DOUBLE PRECISION DEFAULT 0;
	avance_ DOUBLE PRECISION DEFAULT 0;
	result_ DOUBLE PRECISION DEFAULT 0;

	head_ RECORD;
BEGIN    
	SELECT INTO ca_ SUM(get_ca_vente(d.id)) FROM yvs_com_doc_ventes d WHERE d.type_doc = 'FV' AND d.document_lie IS NULL AND d.entete_doc::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head);
	FOR head_ IN SELECT h.users, e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id WHERE e.id::CHARACTER VARYING IN (SELECT head FROM regexp_split_to_table(headers_,',') head)
	LOOP
		SELECT INTO result_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) AND p.caissier = head_.users AND p.date_paiement = head_.date_entete;
		RAISE NOTICE 'result_ : %',result_;
		avance_ = avance_ + COALESCE(result_, 0);
	END LOOP;
	RAISE NOTICE 'ca : %',ca_;
	RAISE NOTICE 'avances : %',avance_;
	result_ = COALESCE(ca_, 0) + COALESCE(avance_, 0);
	RETURN COALESCE(result_, 0);
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION com_get_versement_attendu(character varying)
  OWNER TO postgres;

  
  
-- Function: compta_action_on_piece_caisse_vente()
-- DROP FUNCTION compta_action_on_piece_caisse_vente();
CREATE OR REPLACE FUNCTION compta_action_on_piece_caisse_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	line_ RECORD;
	action_ character varying;
	id_el_ bigint;
	id_tiers_ bigint;
	nom_ character varying default '';
	prenom_ character varying default '';
	echeance_ RECORD;
	vente_ BIGINT;
	solde_ double precision default 0;
	payer_ double precision default 0;
	etape_total_ integer default 0;
	etape_valide_ integer default 0;
	
BEGIN	
	action_=TG_OP;	--INSERT DELETE TRONCATE UPDATE
	IF(action_='INSERT' OR action_='UPDATE') THEN
		id_el_ = NEW.id;
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN
		id_el_ = OLD.id;
	END IF;
		
	--find societe concerné
	IF(action_='INSERT' OR action_='UPDATE') THEN
		SELECT INTO line_  ag.societe, dv.num_doc,cu.users, dv.client, dv.nom_client, cl.nom, cl.prenom, dv.entete_doc FROM yvs_compta_caisse_piece_vente pv INNER JOIN yvs_com_doc_ventes dv ON dv.id=pv.vente 
			INNER JOIN yvs_com_client cl ON cl.id=dv.client INNER JOIN yvs_com_entete_doc_vente en ON en.id=dv.entete_doc
			INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=en.creneau INNER JOIN yvs_com_creneau_point cr ON cr.id=cu.creneau_point
			INNER JOIN yvs_base_point_vente dp ON dp.id=cr.point INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE pv.id=NEW.id; 
									    
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		id_tiers_=line_.client;
		--récupère le code tièrs de ce clients
		SELECT INTO id_tiers_ t.tiers FROM yvs_com_client t WHERE t.id=id_tiers_;
		IF(line_.societe IS NULL) THEN
			SELECT INTO line_.societe  a.societe FROM yvs_users_agence ua INNER JOIN yvs_agences a ON a.id=ua.agence WHERE ua.id=NEW.author;
		END IF;
		IF(line_.nom IS NOT NULL) THEN
			nom_=line_.nom;
		END IF;
		IF(line_.prenom IS NOT NULL) THEN
			prenom_=line_.prenom;
		END IF;
		-- recherche les étapes de validation
		SELECT INTO etape_total_ COUNT(id) from yvs_compta_phase_piece WHERE piece_vente= NEW.id;
		SELECT INTO etape_valide_ COUNT(id) from yvs_compta_phase_piece WHERE piece_vente= NEW.id AND phase_ok IS TRUE;
	ELSE
		SELECT INTO line_  ag.societe, dv.num_doc,cu.users, dv.client, dv.nom_client, cl.nom, cl.prenom, dv.entete_doc FROM yvs_compta_caisse_piece_vente pv INNER JOIN yvs_com_doc_ventes dv ON dv.id=pv.vente 
			INNER JOIN yvs_com_client cl ON cl.id=dv.client INNER JOIN yvs_com_entete_doc_vente en ON en.id=dv.entete_doc
			INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=en.creneau INNER JOIN yvs_com_creneau_point cr ON cr.id=cu.creneau_point
			INNER JOIN yvs_base_point_vente dp ON dp.id=cr.point INNER JOIN yvs_agences ag ON ag.id=dp.agence WHERE pv.id=OLD.id; 
	END IF;         
	IF(action_='INSERT') THEN
		-- Récupère le document
		INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total,numero_externe)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, 
				line_.societe,NEW.author,NEW.caisse, NEW.caissier, null, id_tiers_, COALESCE(NEW.mouvement, 'R'), line_.nom_client, NEW.model, etape_valide_, etape_total_,NEW.reference_externe);
		vente_ = NEW.vente;
	ELSIF (action_='UPDATE') THEN 
		SELECT INTO id_el_ id FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		IF(id_el_ IS NOT NULL) THEN 	
			UPDATE yvs_compta_mouvement_caisse SET numero=NEW.numero_piece, note=NEW.note, montant=NEW.montant, tiers_externe=id_tiers_, model = NEW.model,
				reference_externe=line_.num_doc, date_mvt=NEW.date_piece, date_paiment_prevu=NEW.date_paiment_prevu, date_paye=NEW.date_paiement, 
				statut_piece=NEW.statut_piece, caisse=NEW.caisse, caissier=NEW.caissier, mouvement=COALESCE(NEW.mouvement, 'R'), societe=line_.societe, name_tiers=line_.nom_client,
				etape_total=etape_total_, etape_valide=etape_valide_, numero_externe=NEW.reference_externe
			WHERE table_externe='DOC_VENTE' AND id_externe=NEW.id;
		ELSE
			INSERT INTO yvs_compta_mouvement_caisse(numero, id_externe, note, table_externe, montant, reference_externe, date_mvt, date_paiment_prevu, date_paye, statut_piece, 
				societe, author, caisse,caissier, tiers_interne, tiers_externe,mouvement, name_tiers, model, etape_valide, etape_total,numero_externe)
			VALUES (NEW.numero_piece, NEW.id, NEW.note, 'DOC_VENTE', NEW.montant, line_.num_doc, NEW.date_piece, NEW.date_paiment_prevu, NEW.date_paiement,  NEW.statut_piece, line_.societe,NEW.author,
				NEW.caisse, NEW.caissier,null, id_tiers_, COALESCE(NEW.mouvement, 'R'), line_.nom_client, NEW.model, etape_valide_, etape_total_,NEW.reference_externe);
		END IF;	
		UPDATE yvs_compta_notif_reglement_vente SET date_update = current_date WHERE piece_vente = NEW.id;
		vente_ = NEW.vente;	           
	ELSIF(action_='DELETE' OR action_='TRONCATE') THEN 	
		DELETE FROM yvs_compta_mouvement_caisse WHERE table_externe='DOC_VENTE' AND id_externe=OLD.id;		
		vente_ = OLD.vente;		    	 
	END IF;

	-- Mise à jour de l'échéancier
	SELECT INTO solde_ coalesce(SUM(montant), 0) FROM yvs_com_mensualite_facture_vente WHERE facture = vente_;
	SELECT INTO payer_ coalesce(SUM(montant), 0) FROM yvs_compta_caisse_piece_vente WHERE vente = vente_ AND statut_piece = 'P';
	IF(payer_ <= 0 OR solde_ <= 0)THEN
		UPDATE yvs_com_mensualite_facture_vente SET etat = 'W', avance = 0 WHERE facture = vente_ AND etat != 'W';
	ELSIF(solde_ > payer_)THEN
		WHILE(payer_ > 0)
		LOOP
			FOR echeance_ IN SELECT * FROM yvs_com_mensualite_facture_vente WHERE facture = vente_ ORDER BY date_reglement ASC
			LOOP
				IF(payer_ >= echeance_.montant)THEN
					UPDATE yvs_com_mensualite_facture_vente SET etat = 'P', avance = montant WHERE id = echeance_.id;
				ELSE
					UPDATE yvs_com_mensualite_facture_vente SET etat = 'R', avance = (montant - payer_) WHERE id = echeance_.id;
				END IF;
				payer_ = payer_ - echeance_.montant;
			END LOOP;
		END LOOP;
	ELSE
		UPDATE yvs_com_mensualite_facture_vente SET etat = 'P', avance = montant WHERE facture = vente_ AND etat != 'P';
	END IF;
	PERFORM action_in_header_vente_or_piece_virement(line_.entete_doc);
	RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION compta_action_on_piece_caisse_vente()
  OWNER TO postgres;

  
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
				FOR niveaux_ IN SELECT n.id FROM yvs_niveau_acces n INNER JOIN yvs_users_grade g ON n.grade = g.id WHERE g.reference::character varying in (select val from regexp_split_to_table(grade_, ',') val)
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
				FOR niveaux_ IN SELECT n.id FROM yvs_niveau_acces n INNER JOIN yvs_users_grade g ON n.grade = g.id WHERE g.reference::character varying in (select val from regexp_split_to_table(grade_, ',') val)
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
				FOR niveaux_ IN SELECT n.id FROM yvs_niveau_acces n INNER JOIN yvs_users_grade g ON n.grade = g.id WHERE g.reference::character varying in (select val from regexp_split_to_table(grade_, ',') val)
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

  
  
-- Function: yvs_compta_content_analytique(bigint, bigint, bigint, bigint, bigint, double precision, character varying, character varying, boolean, boolean)
DROP FUNCTION yvs_compta_content_analytique(bigint, bigint, bigint, bigint, bigint, double precision, character varying, boolean, boolean, boolean);
CREATE OR REPLACE FUNCTION yvs_compta_content_analytique(IN agence_ bigint, IN header_ bigint, IN element_ bigint, IN compte_ bigint, IN tiers_ bigint, IN valeur_ double precision, IN mode_ character varying, IN table_ character varying, IN retenue_ boolean, IN salarial_ boolean)
  RETURNS TABLE(error character varying, centre bigint, valeur double precision, coefficient double precision) AS
$BODY$
DECLARE
	_error_ CHARACTER VARYING DEFAULT '';

	ligne_ RECORD;
	query_ CHARACTER VARYING DEFAULT '';
BEGIN   
	DROP TABLE IF EXISTS table_compta_content_analytique;
	CREATE TEMP TABLE IF NOT EXISTS table_compta_content_analytique(_error character varying, _centre bigint, _valeur double precision, _coefficient double precision);
	DELETE FROM table_compta_content_analytique;
	IF((valeur_ IS NOT NULL AND valeur_ > 0) AND (compte_ IS NOT NULL AND compte_ > 0))THEN
		IF(table_ = 'DOC_ACHAT')THEN
			query_ = 'SELECT DISTINCT ap.centre AS centre, (('||valeur_||') *(COALESCE(ap.coefficient, 1)/100)) AS montant, ap.coefficient AS coeficient FROM yvs_compta_centre_contenu_achat ap 
				INNER JOIN yvs_com_contenu_doc_achat co ON ap.contenu = co.id INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article
				WHERE (COALESCE(ap.coefficient,0)!=0) AND ca.compte = '||compte_||'';
		ELSE
			IF(element_ IS NULL OR element_ < 1 )THEN
				query_ = 'SELECT ap.centre_analytique AS centre, (('||valeur_||') *(COALESCE(ap.coefficient, 1)/100)) AS montant, ap.coefficient AS coeficient FROM yvs_compta_affectation_gen_anal ap 
					WHERE (COALESCE(ap.coefficient,0)!=0) AND ap.compte = '||compte_||'';
			ELSE
				query_ = 'SELECT ap.centre, SUM((SELECT COALESCE(SUM(_db.';
				IF(retenue_)THEN
					IF(salarial_)THEN
						query_ = query_ ||'retenu_salariale';
					ELSE
						query_ = query_ ||'montant_employeur';
					END IF;
				ELSE
					query_ = query_ ||'montant_payer';
				END IF;
				IF(mode_= 'E')THEN
					query_ = query_ ||'), 1) FROM yvs_grh_detail_bulletin _db INNER JOIN yvs_grh_bulletins _b ON _b.id=_db.bulletin INNER JOIN yvs_grh_contrat_emps _c ON _c.id=_b.contrat 
										WHERE _b.entete = b.entete  AND db.element_salaire = '||element_||' AND _c.employe= e.id) *(COALESCE(ap.coeficient, 1)/100)) AS montant, ap.coeficient 
						FROM yvs_compta_affec_anal_emp ap INNER JOIN yvs_grh_employes e ON e.id=ap.employe INNER JOIN yvs_agences ag ON e.agence=ag.id INNER JOIN yvs_grh_contrat_emps c ON c.employe=e.id INNER JOIN yvs_grh_bulletins b ON c.id=b.contrat 
						INNER JOIN yvs_compta_affectation_gen_anal ca ON ap.centre=ca.centre_analytique WHERE (COALESCE(ap.coeficient,0)!=0) AND ca.compte = '||compte_||' AND b.entete = '||header_||' AND ag.id = '||agence_||'';
				ELSE
					query_ = query_ ||'), 1) FROM yvs_grh_detail_bulletin _db INNER JOIN yvs_grh_bulletins _b ON _b.id=_db.bulletin INNER JOIN yvs_grh_contrat_emps _c ON _c.id=_b.contrat 
										INNER JOIN yvs_grh_employes _e ON _e.id=_c.employe INNER JOIN yvs_grh_poste_employes _pe ON _e.id=_pe.employe INNER JOIN yvs_grh_poste_de_travail _po ON _po.id = _pe.poste 
										WHERE _b.entete = b.entete AND _db.element_salaire = '||element_||' AND _po.departement in (select grh_get_sous_service(d.id, true))) *(COALESCE(ap.coeficient, 1)/100)) AS montant, ap.coeficient 
						FROM yvs_compta_affec_anal_departement ap INNER JOIN yvs_grh_departement d ON d.id=ap.departement INNER JOIN yvs_grh_poste_de_travail po ON po.departement = d.id 
						INNER JOIN yvs_grh_poste_employes pe ON pe.poste = po.id INNER JOIN yvs_grh_employes e ON e.id=pe.employe INNER JOIN yvs_grh_contrat_emps c ON c.employe=e.id INNER JOIN yvs_grh_bulletins b ON c.id=b.contrat 
						INNER JOIN yvs_compta_affectation_gen_anal ca ON ap.centre=ca.centre_analytique INNER JOIN yvs_agences ag ON e.agence=ag.id 
						WHERE (COALESCE(ap.coeficient,0)!=0) AND ca.compte = '||compte_||' AND b.entete = '||header_||' AND ag.id = '||agence_||'';
				END IF;
				IF(tiers_ IS NOT NULL AND tiers_ > 0)THEN
					query_ = query_ ||' AND e.compte_tiers = '||tiers_||'';
				END IF;
			END IF;
		END IF;
		query_ = query_ ||' GROUP BY ap.id';
		FOR ligne_ IN EXECUTE query_
		LOOP
			INSERT INTO table_compta_content_analytique VALUES(_error_, ligne_.centre, ligne_.montant, ligne_.coeficient);
		END LOOP;
	END IF;
	RETURN QUERY SELECT * FROM table_compta_content_analytique;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION yvs_compta_content_analytique(bigint, bigint, bigint, bigint, bigint, double precision, character varying, character varying, boolean, boolean)
  OWNER TO postgres;

  
  
-- Function: yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean)
-- DROP FUNCTION yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean);
CREATE OR REPLACE FUNCTION yvs_compta_content_journal(IN societe_ bigint, IN agences_ character varying, IN element_ bigint, IN table_ character varying, IN clear_ boolean)
  RETURNS TABLE(id bigint, jour integer, num_piece character varying, num_ref character varying, compte_general bigint, compte_tiers bigint, libelle character varying, debit double precision, credit double precision, echeance date, ref_externe bigint, table_externe character varying, statut character varying, error character varying, contenu bigint, centre bigint, coefficient double precision, numero integer, agence bigint, warning character varying, table_tiers character varying) AS
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
	_warning_ CHARACTER VARYING DEFAULT '';
	_contenu_ BIGINT;
	_centre_ BIGINT;
	_coefficient_ DOUBLE PRECISION DEFAULT 0;
	_numero_ INTEGER DEFAULT 0;
	_agence_ BIGINT DEFAULT 0;
	_table_tiers_ CHARACTER VARYING DEFAULT '';

	ligne_ RECORD;
	sous_ RECORD;
	data_ RECORD;
	
	taux_ DOUBLE PRECISION DEFAULT 100;
	total_ DOUBLE PRECISION DEFAULT 0; 
	somme_ DOUBLE PRECISION DEFAULT 0; 
	valeur_ DOUBLE PRECISION DEFAULT 0; 
	valeur_limite_arrondi_ DOUBLE PRECISION DEFAULT 0;
	
	i_ INTEGER DEFAULT 1;

	titre_ CHARACTER VARYING;
BEGIN    
	IF(clear_)THEN
		DROP TABLE IF EXISTS table_compta_content_journal;
		CREATE TEMP TABLE IF NOT EXISTS table_compta_content_journal(_id BIGINT, _jour INTEGER, _num_piece CHARACTER VARYING, _num_ref CHARACTER VARYING, _compte_general BIGINT, _compte_tiers BIGINT, _libelle CHARACTER VARYING, _debit DOUBLE PRECISION, _credit DOUBLE PRECISION, _echeance DATE, _ref_externe BIGINT, _table_externe CHARACTER VARYING, _statut CHARACTER VARYING, _error character varying, _contenu bigint, _centre bigint, _coefficient double precision, _numero integer, _agence bigint, _warning character varying, _table_tiers character varying);
		DELETE FROM table_compta_content_journal;
	END IF;
	IF(table_ IS NOT NULL AND table_ NOT IN ('', ' '))THEN
		SELECT INTO valeur_limite_arrondi_ valeur_limite_arrondi FROM yvs_compta_parametre WHERE societe = societe_;
		valeur_limite_arrondi_ = COALESCE(valeur_limite_arrondi_, 0);
		IF(table_ = 'DOC_VENTE' OR table_ = 'AVOIR_VENTE')THEN	
			SELECT INTO data_ d.num_doc, d.nom_client, d.tiers, d.client, e.date_entete, c.compte, p.saisie_compte_tiers, u.agence FROM yvs_com_doc_ventes d LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_client c ON d.client = c.id 
				 LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_base_plan_comptable p ON c.compte = p.id WHERE d.id = element_;
			IF(data_.num_doc IS NOT NULL AND data_.num_doc NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				total_ = (SELECT get_ttc_vente(element_)); -- RECUPERATION DU TTC DE LA FACTURE
				IF(total_ IS NOT NULL AND total_ > 0)THEN	
					-- COMPTABILISATION DU TTC DE LA FACTURE
					IF(data_.client IS NULL OR data_.client < 1)THEN
						_error_ = 'cette facture de vente n''est pas rattachée à un client';
					ELSIF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette facture de vente n''est pas rattachée à un compte tiers';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette facture de vente est rattachée à un client qui n''a pas de compte collectif';
					END IF;
					_jour_ = to_char(data_.date_entete ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_entete;
					_compte_general_ = data_.compte;
					IF(data_.saisie_compte_tiers)THEN
						_compte_tiers_ = data_.tiers;
					ELSE
						_compte_tiers_ = null;
					END IF;
					_numero_ = 1;
					FOR ligne_ IN SELECT t.* FROM yvs_base_tranche_reglement t INNER JOIN yvs_base_model_reglement m ON t.model = m.id INNER JOIN yvs_com_doc_ventes d ON d.model_reglement = m.id WHERE d.id = element_
					LOOP
						_debit_ = 0;
						_credit_ = 0;
						IF(table_ = 'DOC_VENTE')THEN
							_debit_ = (total_ * ligne_.taux / 100);
							_libelle_ = UPPER('Echéancier '||ligne_.taux||'% pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);
						ELSE	
							_credit_ = (total_ * ligne_.taux / 100);
							_libelle_ = UPPER('Echéancier '||ligne_.taux||'% pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);
						END IF;
						somme_ = somme_ + _debit_;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;
					IF(total_ > somme_)THEN
						_debit_ = 0;
						_credit_ = 0;
						IF(somme_ > 0)THEN
							taux_ = (((total_ - somme_) / total_) * 100);
						END IF;
						IF(table_ = 'DOC_VENTE')THEN
							_debit_ = (total_ - somme_);
							_libelle_ = UPPER('Echéancier '||taux_||'% pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);
						ELSE	
							_credit_ = (total_ - somme_);
							_libelle_ = UPPER('Echéancier '||taux_||'% pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
					_error_ = null;
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
					FOR ligne_ IN SELECT SUM(_montant) AS total, ca._compte AS compte, pc.intitule, pc.saisie_compte_tiers FROM table_montant_compte_contenu ca LEFT JOIN yvs_base_plan_comptable pc ON ca._compte = pc.id GROUP BY ca._compte, pc.id
					LOOP
						_debit_ = 0;					
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _credit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_vente tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_vente tc INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente 
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE ca.compte = ligne_.compte AND dv.id = element_);
						IF(table_ = 'DOC_VENTE')THEN
							_credit_ = ligne_.total - COALESCE(_credit_, 0);
							_libelle_ = UPPER(ligne_.intitule||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client);	
						ELSE	
							_debit_ = ligne_.total - COALESCE(_credit_, 0);
							_libelle_ = UPPER(ligne_.intitule||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client);	
							_credit_ = 0;	
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						IF(ligne_.saisie_compte_tiers)THEN
							_compte_tiers_ = data_.tiers;
						ELSE
							_compte_tiers_ = null;
						END IF;					
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES TAXES
					_numero_ = 2;
					FOR ligne_ IN SELECT sum(tc.montant) AS total, tx.compte, tx.designation, pc.saisie_compte_tiers FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_vente tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_vente co ON tc.contenu = co.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						LEFT JOIN yvs_base_plan_comptable pc ON tx.compte = pc.id WHERE dv.id = element_ GROUP BY tx.compte, tx.designation, pc.id 
					LOOP
						IF(ligne_.total != 0)THEN
							_debit_ = 0;
							_credit_ = 0;
							IF(table_ = 'DOC_VENTE')THEN
								_credit_ = ligne_.total;
								_libelle_ = ligne_.designation||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;	
							ELSE	
								_credit_ = ligne_.total;
								_libelle_ = ligne_.designation||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client;	
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							IF(ligne_.saisie_compte_tiers)THEN
								_compte_tiers_ = data_.tiers;
							ELSE
								_compte_tiers_ = null;
							END IF;						
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation, pc.saisie_compte_tiers FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_vente co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						LEFT JOIN yvs_base_plan_comptable pc ON tx.compte = pc.id WHERE dv.id = element_ GROUP BY tx.id, tx.libelle, pc.id 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(table_ = 'DOC_VENTE')THEN
								IF(ligne_.augmentation)THEN
									_credit_ = ligne_.total;
								ELSE
									_debit_ = ligne_.total;
								END IF;
								_libelle_ = ligne_.libelle||' pour la facture de vente N° '||data_.num_doc||' de '||data_.nom_client;
							ELSE	
								IF(ligne_.augmentation)THEN
									_debit_ = ligne_.total;
								ELSE
									_credit_ = ligne_.total;
								END IF;
								_libelle_ = ligne_.libelle||' pour l''avoir sur vente N° '||data_.num_doc||' de '||data_.nom_client;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							IF(ligne_.saisie_compte_tiers)THEN
								_compte_tiers_ = data_.tiers;
							ELSE
								_compte_tiers_ = null;
							END IF;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'JOURNAL_VENTE')THEN
			SELECT INTO data_ u.code_users, d.code AS depot, e.date_entete, p.code AS point, u.agence FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_users u ON h.users = u.id 
				LEFT JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id LEFT JOIN yvs_base_point_vente p ON cp.point = p.id LEFT JOIN yvs_com_creneau_depot cd ON h.creneau_point = cd.id LEFT JOIN yvs_base_depots d ON cd.depot = d.id 
				WHERE e.id = element_;
			IF(data_.code_users IS NOT NULL AND data_.code_users NOT IN ('', ' '))THEN		
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;		
				total_ = (SELECT get_ttc_entete_vente(element_)); -- RECUPERATION DU TTC DE LA JOURNEE DE VENTE
				IF(total_ IS NOT NULL AND total_ > 0)THEN	
					-- COMPTABILISATION DU TTC DE LA JOURNEE DE VENTE
					_jour_ = to_char(data_.date_entete ,'dd')::integer;
					_num_piece_ = data_.code_users || '/';
					IF(data_.point IS NOT NULL AND data_.point NOT IN ('', ' '))THEN
						_num_piece_ = _num_piece_ || data_.point || '/';
					ELSE
						_num_piece_ = _num_piece_ || data_.depot || '/';
					END IF;
					_numero_ = 1;
					_num_piece_ = _num_piece_ || to_char(data_.date_entete ,'ddMMyy');	
					_echeance_ = data_.date_entete;
					FOR ligne_ IN SELECT sum(get_ttc_vente(dv.id)) as total , cl.compte, dv.tiers, CONCAT(ts.nom, ' ', ts.prenom) AS nom, pc.saisie_compte_tiers FROM yvs_com_doc_ventes dv INNER JOIN yvs_com_client cl ON dv.client = cl.id 
						INNER JOIN yvs_com_client ts ON dv.tiers = ts.id LEFT JOIN yvs_base_plan_comptable pc ON cl.compte = pc.id WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_
						GROUP BY dv.entete_doc, cl.compte, dv.tiers, ts.id, pc.id
					LOOP
						_debit_ = ligne_.total;
						_credit_ = 0;
						somme_ = somme_ + _debit_;
						_libelle_ = 'Echéancier 100% pour le journal de vente N° '||_num_piece_;
						IF(i_ < 10)THEN
							_num_ref_ = _num_piece_ ||'-0'||i_;
						ELSE
							_num_ref_ = _num_piece_ ||'-'||i_;
						END IF;
						_compte_general_ = ligne_.compte;
						IF(ligne_.saisie_compte_tiers)THEN
							_compte_tiers_ = ligne_.tiers;
						ELSE
							_compte_tiers_ = null;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;

					_error_ = null;
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
					_numero_ = 2;
					FOR ligne_ IN SELECT SUM(_montant) AS total, ca._compte AS compte, pc.intitule, pc.saisie_compte_tiers FROM table_montant_compte_contenu ca LEFT JOIN yvs_base_plan_comptable pc ON ca._compte = pc.id GROUP BY ca._compte, pc.id
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
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = null;
						_libelle_ = ligne_.intitule||' pour le journal de vente N° '||_num_piece_;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;

					_error_ = null;
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
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.designation||' pour le journal de vente N° '||_num_piece_;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;

					_error_ = null;
					-- COMPTABILISATION DES COUPS SUPPLEMENTAIRES
					FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_com_cout_sup_doc_vente co ON co.type_cout = tx.id INNER JOIN yvs_com_doc_ventes dv ON dv.id = co.doc_vente
						WHERE dv.type_doc = 'FV' AND dv.statut = 'V' AND dv.entete_doc = element_ GROUP BY tx.id, tx.libelle 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(ligne_.augmentation)THEN
								_credit_ = ligne_.total;
								_numero_ = 2;
							ELSE
								_debit_ = ligne_.total;
								_numero_ = 1;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = _num_piece_ ||'-0'||i_;
							ELSE
								_num_ref_ = _num_piece_ ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_libelle_ = ligne_.libelle||' pour le journal de vente N° '||_num_piece_;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'DOC_ACHAT' OR table_ = 'AVOIR_ACHAT')THEN
			SELECT INTO data_ d.num_doc, d.date_doc, d.fournisseur, CONCAT(c.nom, ' ', c.prenom) AS nom, c.compte, d.fournisseur as tiers, pc.saisie_compte_tiers, d.agence, COALESCE(d.description, '') AS description FROM yvs_com_doc_achats d LEFT JOIN yvs_base_fournisseur c ON d.fournisseur = c.id LEFT JOIN yvs_base_plan_comptable pc ON c.compte = pc.id WHERE d.id = element_;
			IF(data_.num_doc IS NOT NULL AND data_.num_doc NOT IN ('', ' '))THEN		
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;			
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
					_jour_ = to_char(data_.date_doc ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_doc;
					_compte_general_ = data_.compte;
					_libelle_ = data_.description;
					IF(data_.saisie_compte_tiers)THEN
						_compte_tiers_ = data_.tiers;
					ELSE
						_compte_tiers_ = null;
					END IF;
					_numero_ = 2;
					FOR ligne_ IN SELECT t.* FROM yvs_base_tranche_reglement t INNER JOIN yvs_base_model_reglement m ON t.model = m.id INNER JOIN yvs_com_doc_achats d ON d.model_reglement = m.id WHERE d.id = element_
					LOOP
						_debit_ = 0;
						_credit_ = 0;
						IF(table_ = 'DOC_ACHAT')THEN
							_credit_ = (SELECT arrondi(societe_, (total_ * ligne_.taux / 100)));
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||ligne_.taux||'% pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE	
							_debit_ = (SELECT arrondi(societe_, (total_ * ligne_.taux / 100)));
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||ligne_.taux||'% pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						END IF;
						somme_ = somme_ + _credit_;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END LOOP;
					IF(total_ > somme_)THEN
						_debit_ = 0;
						_credit_ = 0;
						IF(somme_ > 0)THEN
							taux_ = (((total_ - somme_) / total_) * 100);
						END IF;
						IF(table_ = 'DOC_ACHAT')THEN
							_credit_ = (SELECT arrondi(societe_, (total_ - somme_)));
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||taux_||'% pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE	
							_debit_ = (SELECT arrondi(societe_, (total_ - somme_)));
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = 'Echéancier '||taux_||'% pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
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
					_numero_ = 1;
					FOR ligne_ IN SELECT SUM(_montant) AS total, ca._compte AS compte, pc.intitule, pc.saisie_analytique FROM table_montant_compte_contenu ca INNER JOIN yvs_base_plan_comptable pc ON ca._compte = pc.id GROUP BY ca._compte, pc.id
					LOOP
						_credit_ = 0;
						-- RECHERCHE DE LA VALEUR DE LA TAXE POUR CHAQUE COMPTE QUI INTERVIENT SUR UNE LIGNE DE VENTE
						SELECT INTO _debit_ SUM(tc.montant) FROM  yvs_com_taxe_contenu_achat tc WHERE tc.id IN
							(SELECT DISTINCT tc.id FROM yvs_com_taxe_contenu_achat tc INNER JOIN yvs_com_contenu_doc_achat co ON tc.contenu = co.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
								INNER JOIN yvs_base_article_categorie_comptable ca ON co.article = ca.article WHERE ca.compte = ligne_.compte AND dv.id = element_);
						IF(table_ = 'DOC_ACHAT')THEN
							_debit_ = ligne_.total - COALESCE(_debit_, 0);
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = ligne_.intitule||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
						ELSE
							_credit_ = ligne_.total - COALESCE(_debit_, 0);
							IF(TRIM(data_.description) IN ('', ' '))THEN
								_libelle_ = ligne_.intitule||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
							END IF;
							_debit_ = 0;
						END IF;
						IF(i_ < 10)THEN
							_num_ref_ = data_.num_doc ||'-0'||i_;
						ELSE
							_num_ref_ = data_.num_doc ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'certains article ne sont pas rattaché a des comptes pour la catégorie de la facture';
						END IF;
						_compte_general_ = ligne_.compte;
						_compte_tiers_ = null;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
						IF(table_ = 'DOC_ACHAT')THEN
							valeur_ = _debit_;
						ELSE
							valeur_ = _credit_;
						END IF;
						IF(ligne_.saisie_analytique IS TRUE AND (valeur_ IS NOT NULL AND valeur_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
							_contenu_ = _id_;
							RAISE NOTICE 'valeur_ : %',valeur_;
							RAISE NOTICE '_compte_general_ : %',_compte_general_;
							FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, 0, 0, _compte_general_, null, valeur_, '', table_, FALSE, FALSE)
							LOOP
								_error_ = sous_.error;
								_centre_ = sous_.centre;
								_coefficient_ = sous_.coefficient;
								IF(table_ = 'DOC_ACHAT')THEN
									_debit_ = sous_.valeur;
								ELSE
									_credit_ = sous_.valeur;
								END IF;
								INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
							END LOOP;
						END IF;
					END LOOP;

					-- COMPTABILISATION DES TAXES
					FOR ligne_ IN SELECT sum(tc.montant) AS total, tx.compte, tx.designation FROM yvs_base_taxes tx INNER JOIN yvs_com_taxe_contenu_achat tc ON tc.taxe = tx.id INNER JOIN yvs_com_contenu_doc_achat co ON tc.contenu = co.id INNER JOIN yvs_com_doc_achats dv ON dv.id = co.doc_achat
						WHERE dv.id = element_ GROUP BY tx.compte, tx.designation 
					LOOP
						IF(ligne_.total != 0)THEN
							_credit_ = 0;
							_debit_ = 0;
							IF(table_ = 'DOC_ACHAT')THEN
								_debit_ = ligne_.total;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.designation||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							ELSE
								_credit_ = ligne_.total;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.designation||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'la taxe '''||ligne_.designation||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
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
							IF(table_ = 'DOC_ACHAT')THEN
								IF(ligne_.augmentation IS TRUE)THEN
									_debit_ = ligne_.total;
									_numero_ = 1;
								ELSE
									_credit_ = ligne_.total;
									_numero_ = 2;
								END IF;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.libelle||' pour la facture d''achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							ELSE
								IF(ligne_.augmentation IS TRUE)THEN
									_credit_ = ligne_.total;
									_numero_ = 2;
								ELSE
									_debit_ = ligne_.total;
									_numero_ = 1;
								END IF;
								IF(TRIM(data_.description) IN ('', ' '))THEN
									_libelle_ = ligne_.libelle||' pour l''avoir sur achat N° '||data_.num_doc||' de '||data_.nom;
								END IF;
							END IF;
							IF(i_ < 10)THEN
								_num_ref_ = data_.num_doc ||'-0'||i_;
							ELSE
								_num_ref_ = data_.num_doc ||'-'||i_;
							END IF;
							IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
								_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
							END IF;
							_compte_general_ = ligne_.compte;
							_compte_tiers_ = null;							
							_id_ = _id_ - 1;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
							i_ = i_ + 1;
						END IF;
					END LOOP;
				END IF;
			END IF;
		ELSIF(table_ = 'FRAIS_MISSION')THEN
			SELECT INTO data_ f.id, f.date_paiement, f.montant, c.journal, c.compte, o.compte_charge, m.ordre, m.numero_mission, e.agence FROM yvs_compta_caisse_piece_mission f LEFT JOIN yvs_grh_missions m ON f.mission = m.id LEFT JOIN yvs_grh_employes e ON m.employe = e.id
				LEFT JOIN yvs_grh_objets_mission o ON m.objet_mission = o.id LEFT JOIN yvs_base_caisse c ON f.caisse = c.id WHERE f.id = element_;	
			_table_tiers_ = 'EMPLOYE';	
			IF(data_.numero_mission IS NOT NULL AND data_.numero_mission NOT IN ('', ' '))THEN	
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero_mission;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.ordre;
				_compte_tiers_ = null;
				
				_credit_ = 0;
				_debit_ = data_.montant;
				_numero_ = 1;
				_compte_general_ = data_.compte_charge;	
				_num_ref_ = _num_piece_ ||'-01';						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
				
				_credit_ = data_.montant;
				_numero_ = 2;
				_debit_ = 0;
				_compte_general_ = data_.compte;	
				_num_ref_ = _num_piece_ ||'-02';						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, y.caisse, p.vente, d.num_doc, d.client, t.compte as compte_tiers, t.suivi_comptable, d.tiers, m.compte_tiers AS tiers_interne, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, u.agence,
				r.libelle, r.mode_reglement, r.code_comptable, c.intitule, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.numero_piece) AS reference_externe FROM yvs_compta_phase_piece y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_vente p ON y.piece_vente = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_doc_ventes d ON p.vente = d.id LEFT JOIN yvs_com_client t ON d.client = t.id LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
				LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_grh_employes m ON u.id = m.code_users WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.vente IS NULL OR data_.vente < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune facture';
				ELSE
					_libelle_ = _libelle_|| ' du client '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece p ON p.phase_reg = r.id WHERE p.piece_vente = data_.id;
				RAISE NOTICE '%',data_.numero_phase;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune caisse';
				ELSE
					IF(data_.numero_phase = ligne_.max)THEN
						IF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;					
					ELSE
						IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
							_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
						ELSE
							_compte_general_ = data_.compte_caisse;
						END IF;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.vente IS NOT NULL AND data_.vente > 0)THEN
						IF(data_.client IS NULL OR data_.client < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de client';
						ELSE
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_vente v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_vente p ON y.piece_vente = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece _y INNER JOIN yvs_compta_caisse_piece_vente _p ON _y.piece_vente = _p.id INNER JOIN yvs_compta_phase_piece _y0 ON _y0.piece_vente = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
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
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, y.caisse, p.achat, d.num_doc, d.fournisseur, t.compte as compte_tiers, d.fournisseur as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, d.agence,
				r.libelle, r.mode_reglement, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.numero_piece) AS reference_externe FROM yvs_compta_phase_piece_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_achat p ON y.piece_achat = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_doc_achats d ON p.achat = d.id LEFT JOIN yvs_base_fournisseur t ON d.fournisseur = t.id WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.achat IS NULL OR data_.achat < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune facture';
				ELSE
					_libelle_ = _libelle_||' du fournisseur '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece_achat p ON p.phase_reg = r.id WHERE p.piece_achat = data_.id;
				RAISE NOTICE 'data_.numero_phase %',data_.numero_phase;
				RAISE NOTICE 'ligne_.max %',ligne_.max;
				RAISE NOTICE 'ligne_.min %',ligne_.min;
				IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune caisse';
				ELSE
					IF(data_.numero_phase = ligne_.min)THEN
						IF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;					
					ELSE
						IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
							_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
						ELSE
							_compte_general_ = data_.compte_caisse;
						END IF;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 2;
					_credit_ = data_.montant;	
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.achat IS NOT NULL AND data_.achat > 0)THEN
						IF(data_.fournisseur IS NULL OR data_.fournisseur < 1)THEN
							_error_ = 'ce reglement est rattaché à une facture qui n''a pas de fournisseur';
						ELSE
							IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
								_error_ = 'ce reglement est rattaché à une facture qui est liée à un fournisseur qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte_tiers;
							END IF;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_achat v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_achat p ON y.piece_achat = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece_achat _y INNER JOIN yvs_compta_caisse_piece_achat _p ON _y.piece_achat = _p.id INNER JOIN yvs_compta_phase_piece_achat _y0 ON _y0.piece_achat = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'car ce reglement est rattaché à une facture qui est liée à un client qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;	
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE			
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN
			SELECT INTO data_ p.id, p.num_piece as numero_piece, p.date_valider as date_paiement, p.montant, y.caisse, p.doc_divers, d.num_piece as num_doc, d.id_tiers as tiers, name_tiers(d.id_tiers, d.table_tiers, 'C')::integer as compte_tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, d.table_tiers, d.agence,
				r.libelle, r.mode_reglement, y.statut, d.mouvement, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, name_tiers(d.id_tiers, d.table_tiers, 'N') AS nom_prenom,
				COALESCE(p.reference_externe, p.num_piece) AS reference_externe FROM yvs_compta_phase_piece_divers y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_caisse_piece_divers p ON y.piece_divers = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_compta_caisse_doc_divers d ON p.doc_divers = d.id WHERE y.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = data_.table_tiers;		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_doc;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.doc_divers IS NULL OR data_.doc_divers < 1)THEN
					_error_ = 'ce reglement n''est associé à aucune operation diverse';
				ELSIF(data_.nom_prenom NOT IN ('', ' '))THEN
					_libelle_ = _libelle_||' du tiers '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min, 0 as tmp FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_piece_divers p ON p.phase_reg = r.id WHERE p.piece_divers = data_.id;
				IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
					ligne_.tmp = ligne_.max;
					ligne_.max = ligne_.min;
					ligne_.min = ligne_.tmp;
				END IF;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune caisse';
					ELSIF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattahé à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 2;
						_credit_ = data_.montant;
					ELSE
						_numero_ = 1;
						_debit_ = data_.montant;
					END IF;
				ELSE		
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 1;
						_debit_ = data_.montant;
					ELSE
						_numero_ = 2;
						_credit_ = data_.montant;
					END IF;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.doc_divers IS NOT NULL AND data_.doc_divers > 0)THEN
						IF(data_.compte_tiers IS NULL OR data_.compte_tiers < 1)THEN
							_error_ = 'ce reglement est rattaché à une operation diverse d''un tiers qui n''a pas de compte collectif';
						ELSE
							_compte_general_ = data_.compte_tiers;
						END IF;
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_piece_divers y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_piece_divers v ON v.etape = y.id INNER JOIN yvs_compta_caisse_piece_divers p ON y.piece_divers = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_piece_divers _y INNER JOIN yvs_compta_caisse_piece_divers _p ON _y.piece_divers = _p.id INNER JOIN yvs_compta_phase_piece_divers _y0 ON _y0.piece_divers = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;		
				END IF;	
				IF(data_.reglement_ok)THEN					
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'ce reglement est rattaché à une operation diverse qui n''a pas de tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;	
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 1;
						_debit_ = data_.montant;
					ELSE
						_numero_ = 2;
						_credit_ = data_.montant;
					END IF;
				ELSE		
					IF(COALESCE(data_.mouvement, 'D') != 'R')THEN
						_numero_ = 2;
						_credit_ = data_.montant;
					ELSE
						_numero_ = 1;
						_debit_ = data_.montant;
					END IF;
				END IF;
				_num_ref_ = data_.numero_piece;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN
			SELECT INTO data_ p.id, p.num_refrence, p.date_paiement, p.montant, y.caisse, t.compte as compte_tiers, p.fournisseur as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, y.piece_achat, m.societe, j.agence, 
				r.libelle, r.mode_reglement, r.code_comptable, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.num_refrence) AS reference_externe FROM yvs_compta_phase_acompte_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_acompte_fournisseur p ON y.piece_achat = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_base_fournisseur t ON p.fournisseur = t.id LEFT JOIN yvs_base_mode_reglement m ON p.model = m.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE y.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_refrence;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.piece_achat IS NULL OR data_.piece_achat < 1)THEN
					_error_ = 'cette phase n''est rattachée a aucun acompte';
				ELSE
					_libelle_ = _libelle_||' du fournisseur '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_acompte_achat p ON p.phase_reg = r.id WHERE p.piece_achat = data_.id;
				IF(data_.numero_phase = ligne_.min)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE			
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.max)THEN
					SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
					IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
						_error_ = 'Le compte des acomptes fournisseur n''est pas paramètré';
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_acompte_achat y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_acompte_achat v ON v.etape = y.id INNER JOIN yvs_compta_acompte_fournisseur p ON y.piece_achat = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_acompte_achat _y INNER JOIN yvs_compta_acompte_fournisseur _p ON _y.piece_achat = _p.id INNER JOIN yvs_compta_phase_acompte_achat _y0 ON _y0.piece_achat = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;			
				END IF;	
				IF(data_.reglement_ok)THEN
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette phase est liée a un acompte fournisseur qui n'' pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;			
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN
			SELECT INTO data_ p.id, p.num_refrence, p.date_paiement, p.montant, y.caisse, t.compte as compte_tiers, p.client as tiers, c.compte, COALESCE(r.numero_phase, 0) as numero_phase, y.piece_vente, m.societe, j.agence,
				r.libelle, r.mode_reglement, r.code_comptable, y.statut, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse, r.reglement_ok, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom,
				COALESCE(p.reference_externe, p.num_refrence) AS reference_externe FROM yvs_compta_phase_acompte_vente y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id INNER JOIN yvs_compta_acompte_client p ON y.piece_vente = p.id LEFT JOIN yvs_base_caisse c ON y.caisse = c.id 
				LEFT JOIN yvs_com_client t ON p.client = t.id LEFT JOIN yvs_base_mode_reglement m ON p.model = m.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE y.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';		
				_agence_ = data_.agence;
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.num_refrence;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.libelle||' N° '||data_.reference_externe;
				IF(data_.piece_vente IS NULL OR data_.piece_vente < 1)THEN
					_error_ = 'cette phase n''est rattachée a aucun acompte';
				ELSE
					_libelle_ = _libelle_||' du client '||data_.nom_prenom;
				END IF;
				SELECT INTO ligne_ MAX(r.numero_phase) as max, MIN(r.numero_phase) as min FROM yvs_compta_phase_reglement r INNER JOIN yvs_compta_phase_acompte_vente p ON p.phase_reg = r.id WHERE p.piece_vente = data_.id;
				IF(data_.numero_phase = ligne_.max)THEN
					IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui n''est associé à aucune caisse';
					ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cette phase est liée a un acompte qui est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;					
				ELSE
					IF(data_.compte_caisse IS NULL OR data_.compte_caisse < 1)THEN
						_error_ = 'cette etape est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = data_.compte_caisse;
					END IF;
				END IF;
				_compte_tiers_ = null;			
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 1;
					_debit_ = data_.montant;
				ELSE		
					_numero_ = 2;
					_credit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				IF(data_.numero_phase = ligne_.min)THEN
					SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
					IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
						_error_ = 'Le compte des acomptes client n''est pas paramètré';
					END IF;
				ELSE
					SELECT INTO sous_ v.id as comptabilise, y.id, r.libelle, r.code_comptable, (SELECT o.compte_general FROM yvs_base_comptes_caisse o WHERE o.code_comptable = r.code_comptable AND o.caisse = y.caisse LIMIT 1) as compte_caisse 
						FROM yvs_compta_phase_acompte_vente y INNER JOIN yvs_compta_phase_reglement r ON y.phase_reg = r.id LEFT JOIN yvs_compta_content_journal_etape_acompte_vente v ON v.etape = y.id INNER JOIN yvs_compta_acompte_client p ON y.piece_vente = p.id
						WHERE y.id = (SELECT _y.id FROM yvs_compta_phase_acompte_vente _y INNER JOIN yvs_compta_acompte_client _p ON _y.piece_vente = _p.id INNER JOIN yvs_compta_phase_acompte_vente _y0 ON _y0.piece_vente = _p.id INNER JOIN yvs_compta_phase_reglement _r ON _y.phase_reg = _r.id WHERE _y0.id = element_ AND _r.numero_phase = (data_.numero_phase - 1));
					IF(sous_.comptabilise IS NULL OR sous_.comptabilise < 1)THEN
						_warning_ = 'l''etape précédente n''est pas encore comptabilisée';
					END IF;	
					IF(sous_.compte_caisse IS NULL OR sous_.compte_caisse < 1)THEN
						_error_ = 'l''etape précédente est rattaché à un code comptable qui n''est pas associé à compte général';
					ELSE
						_compte_general_ = sous_.compte_caisse;
					END IF;			
				END IF;	
				IF(data_.reglement_ok)THEN
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cette phase est liée a un acompte client qui n'' pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;
					END IF;			
				END IF;		
				_credit_ = 0;		
				_debit_ = 0;
				IF(COALESCE(data_.statut, 'V') != 'A')THEN	
					_numero_ = 2;
					_credit_ = data_.montant;
				ELSE		
					_numero_ = 1;
					_debit_ = data_.montant;
				END IF;
				_num_ref_ = data_.num_refrence;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CAISSE_VENTE')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, p.caisse, p.vente, d.num_doc, d.client, t.compte as compte_tiers, t.suivi_comptable, d.tiers, m.compte_tiers AS tiers_interne, c.compte, mo.type_reglement, mo.societe, u.agence
				FROM yvs_compta_caisse_piece_vente p LEFT JOIN yvs_base_caisse c ON p.caisse = c.id LEFT JOIN yvs_base_mode_reglement mo ON p.model = mo.id
				LEFT JOIN yvs_com_doc_ventes d ON p.vente = d.id LEFT JOIN yvs_com_client t ON d.client = t.id LEFT JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id LEFT JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id 
				LEFT JOIN yvs_users u ON h.users = u.id LEFT JOIN yvs_grh_employes m ON u.id = m.code_users WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' ') AND data_.type_reglement != 'SALAIRE')THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece p WHERE p.piece_vente = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_vente = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_VENTE', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_paiement ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
					_echeance_ = data_.date_paiement;
					_libelle_ = 'Reglement Facture vente N° ';		
					_compte_tiers_ = null;	
					IF(data_.vente IS NULL OR data_.vente < 1)THEN
						_error_ = 'ce reglement n''est associé à aucune facture';
					ELSE
						_libelle_ = _libelle_||data_.num_doc;
					END IF;
					IF(data_.type_reglement = 'COMPENSATION')THEN					
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'COMPENSATION';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des attentes pour compensation n''est pas paramètré';
						END IF;
					ELSE				
						SELECT INTO ligne_ COUNT(n.id) as count FROM yvs_compta_notif_reglement_vente n WHERE n.piece_vente = element_;
						IF(ligne_.count IS NOT NULL AND ligne_.count > 0)THEN		
							SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
							IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
								_error_ = 'Le compte des acomptes client n''est paramètré';
							END IF;		
							IF(data_.suivi_comptable)THEN	
								_compte_tiers_ = data_.tiers;
							ELSE
								_compte_tiers_ = data_.tiers_interne;	
							END IF;			
						ELSE
							IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
								_error_ = 'ce reglement n''est associé à aucune caisse';
							ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
								_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
							ELSE
								_compte_general_ = data_.compte;
							END IF;
						END IF;
					END IF;
					_numero_ = 1;			
					_credit_ = 0;
					_debit_ = data_.montant;	
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
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
									_error_ = 'car ce reglement est rattaché à une facture qui n''a pas de compte tiers';
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
					ELSE
						_error_ = 'ce reglement n''est associé à aucune facture';
					END IF;
					_ref_externe_ = data_.id;
					_table_externe_ = 'CAISSE_VENTE';
					_numero_ = 2;
					_credit_ = data_.montant;
					_debit_ = 0;	
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CAISSE_ACHAT')THEN
			SELECT INTO data_ p.id, p.numero_piece, p.date_paiement, p.montant, p.caisse, p.achat, d.num_doc, d.fournisseur, t.compte AS compte_tiers, d.fournisseur as tiers, c.compte, mo.type_reglement, mo.societe, d.agence FROM yvs_compta_caisse_piece_achat p 
				LEFT JOIN yvs_base_caisse c ON p.caisse = c.id  LEFT JOIN yvs_base_mode_reglement mo ON p.model = mo.id
				LEFT JOIN yvs_com_doc_achats d ON p.achat = d.id LEFT JOIN yvs_base_fournisseur t ON d.fournisseur = t.id WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';		
				_agence_ = data_.agence;
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece_achat p WHERE p.piece_achat = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece_achat p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_achat = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_ACHAT', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_paiement ,'dd')::integer;
					_num_piece_ = data_.num_doc;	
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
					_numero_ = 1;
					_credit_ = 0;
					_debit_ = data_.montant;
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
					
					_error_ = '';			
					_compte_tiers_ = null;	
					IF(data_.type_reglement = 'COMPENSATION')THEN					
						SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'COMPENSATION';
						IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
							_error_ = 'Le compte des attentes pour compensation n''est pas paramètré';
						END IF;
					ELSE
						SELECT INTO ligne_ COUNT(n.id) as count FROM yvs_compta_notif_reglement_achat n WHERE n.piece_achat = element_;
						IF(ligne_.count IS NOT NULL AND ligne_.count > 0)THEN		
							SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
							IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
								_error_ = 'Le compte des acomptes fournisseur n''est paramètré';
							END IF;		
							_compte_tiers_ = data_.tiers;				
						ELSE
							IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
								_error_ = 'ce reglement n''est associé à aucune caisse';
							ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
								_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
							ELSE	
								_compte_general_ = data_.compte;
							END IF;
						END IF;
					END IF;
					_numero_ = 2;			
					_credit_ = data_.montant;
					_debit_ = 0;		
					_num_ref_ = data_.numero_piece;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CAISSE_DIVERS')THEN
			SELECT INTO data_ p.id, p.num_piece, p.date_piece, p.montant, p.caisse, p.doc_divers, d.num_piece as num_doc, CONCAT(d.type_doc, '') AS type_doc, CONCAT(d.mouvement, '') AS mouvement, d.id_tiers as tiers, d.compte_general, name_tiers(d.id_tiers, d.table_tiers, 'C')::integer as compte_collectif, 
				c.compte, mo.type_reglement, COALESCE(d.libelle_comptable, d.num_piece) AS description, d.table_tiers, d.agence 
				FROM yvs_compta_caisse_piece_divers p LEFT JOIN yvs_base_caisse c ON p.caisse = c.id LEFT JOIN yvs_base_mode_reglement mo ON p.mode_paiement = mo.id 
				LEFT JOIN yvs_compta_caisse_doc_divers d ON p.doc_divers = d.id WHERE p.id = element_;
			IF(data_.num_piece IS NOT NULL AND data_.num_piece NOT IN ('', ' '))THEN		
				_agence_ = data_.agence;
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_piece_divers p WHERE p.piece_divers = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_piece_divers p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_divers = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_CAISSE_DIVERS', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					SELECT INTO ligne_ COUNT(p.id) FROM yvs_compta_caisse_piece_cout_divers p WHERE p.piece = data_.id;
					IF(COALESCE(ligne_.count, 0) > 0)THEN
						_error_ = 'ce reglement est rattaché à un cout';
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					ELSE
						_table_tiers_ = data_.table_tiers;	
						_jour_ = to_char(data_.date_piece ,'dd')::integer;
						_num_piece_ = data_.num_doc;	
						_echeance_ = data_.date_piece;
						_libelle_ = 'Reglement N° '||data_.num_piece||' sur ';	
						
						IF(data_.doc_divers IS NULL OR data_.doc_divers < 1)THEN
							_error_ = 'ce reglement n''est associé à aucun document';
						ELSE
							_libelle_ = _libelle_||data_.description;
						END IF;
						
						_error_ = '';
						_compte_general_ = null;
						_compte_tiers_ = null;
						_debit_ = 0;
						_credit_ = 0;		
						IF(data_.mouvement = 'D')THEN
							_debit_ = data_.montant;
							_numero_ = 1;
						ELSE	
							_credit_ = data_.montant;	
							_numero_ = 2;			
						END IF;
						IF(data_.tiers IS NULL OR data_.tiers < 1)THEN							
							_error_ = 'devez comptabiliser le documents en question';
						ELSE
							_compte_tiers_ = data_.tiers;
							IF(data_.compte_collectif IS NULL OR data_.compte_collectif < 1)THEN							
								_error_ = 'car ce reglement est rattaché à un document d''un tiers qui n''a pas de compte collectif';
							ELSE
								_compte_general_ = data_.compte_collectif;
							END IF;	
						END IF;	
						_num_ref_ = data_.num_piece;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
						
						_error_ = '';
						_compte_general_ = null;
						_compte_tiers_ = null;
						_credit_ = 0;
						_debit_ = 0;
						IF(data_.mouvement = 'D')THEN
							_credit_ = data_.montant;
							_numero_ = 2;
						ELSE
							_debit_ = data_.montant;
							_numero_ = 1;		
						END IF;	
						IF(data_.caisse IS NULL OR data_.caisse < 1)THEN
							_error_ = 'ce reglement n''est associé à aucune caisse';
						ELSIF(data_.compte IS NULL OR data_.compte < 1)THEN
							_error_ = 'ce reglement est rattaché à une caisse qui n''a pas de compte général';
						ELSE
							_compte_general_ = data_.compte;
						END IF;	
						_num_ref_ = data_.num_piece;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
				END IF;
			END IF;
		ELSIF(table_ = 'ACOMPTE_VENTE')THEN
			SELECT INTO data_ a.id, a.date_acompte, a.num_refrence, a.montant, m.societe, c.compte, a.client as tiers, m.type_reglement, j.agence FROM yvs_compta_acompte_client a  LEFT JOIN yvs_base_caisse c ON a.caisse = c.id
				INNER JOIN yvs_base_mode_reglement m ON a.model = m.id LEFT JOIN yvs_com_client f ON a.client = f.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE a.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_acompte_vente p WHERE p.piece_vente = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_acompte_vente p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_vente = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_ACOMPTE_VENTE', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_acompte ,'dd')::integer;
					_num_piece_ = data_.num_refrence;	
					_echeance_ = data_.date_acompte;
					_libelle_ = 'Acompte N° '||data_.num_refrence;				
					SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_CLIENT';
					IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
						_error_ = 'Le compte des acomptes client n''est pas paramètré';
					END IF;
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cet acompte est rattaché à un fournisseur qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;				
					END IF;				
					_credit_ = data_.montant;
					_debit_ = 0;	
					_numero_ = 2;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;

					_error_ = null;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cet acompte est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;
					_credit_ = 0;
					_debit_ = data_.montant;	
					_numero_ = 1;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'ACOMPTE_ACHAT')THEN
			SELECT INTO data_ a.id, a.date_acompte, a.num_refrence, a.montant, m.societe, c.compte, a.fournisseur as tiers, m.type_reglement, j.agence FROM yvs_compta_acompte_fournisseur a LEFT JOIN yvs_base_caisse c ON a.caisse = c.id 
				INNER JOIN yvs_base_mode_reglement m ON a.model = m.id INNER JOIN yvs_base_fournisseur f ON a.fournisseur = f.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE a.id = element_;
			IF(data_.num_refrence IS NOT NULL AND data_.num_refrence NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;	
				SELECT INTO ligne_ COUNT(p.id) AS count FROM yvs_compta_phase_acompte_achat p WHERE p.piece_achat = data_.id;
				IF(data_.type_reglement = 'BANQUE' AND COALESCE(ligne_.count, 0) > 0)THEN	
					_numero_ = 0;
					_id_ = 0;
					FOR sous_ IN SELECT p.id FROM yvs_compta_phase_acompte_achat p INNER JOIN yvs_compta_phase_reglement r ON p.phase_reg = r.id WHERE p.piece_achat = data_.id ORDER BY r.numero_phase
					LOOP
						_numero_ = _numero_ + 1;
						IF(_numero_ > 1)THEN					
							_id_ = _id_ -2;
						END IF;
						PERFORM yvs_compta_content_journal(societe_, agences_, sous_.id, 'PHASE_ACOMPTE_ACHAT', false);
						UPDATE table_compta_content_journal SET _numero = _numero_, _id = _id + _id_ WHERE _ref_externe = sous_.id;
					END LOOP;	
				ELSE
					_jour_ = to_char(data_.date_acompte ,'dd')::integer;
					_num_piece_ = data_.num_refrence;	
					_echeance_ = data_.date_acompte;
					_libelle_ = 'Acompte N° '||data_.num_refrence;				
					SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = data_.societe AND n.nature = 'ACOMPTE_FOURNISSEUR';
					IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
						_error_ = 'Le compte des acomptes fournisseur n''est pas paramètré';
					END IF;
					IF(data_.tiers IS NULL OR data_.tiers < 1)THEN
						_error_ = 'cet acompte est rattaché à un fournisseur qui n''a pas de compte tiers';
					ELSE
						_compte_tiers_ = data_.tiers;				
					END IF;			
					_credit_ = 0;
					_debit_ = data_.montant;	
					_numero_ = 1;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;

					_error_ = null;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(data_.compte IS NULL OR data_.compte < 1)THEN
						_error_ = 'cet acompte est rattaché à une caisse qui n''a pas de compte général';
					ELSE
						_compte_general_ = data_.compte;
					END IF;
					_credit_ = data_.montant;
					_debit_ = 0;	
					_numero_ = 2;
					_num_ref_ = data_.num_refrence;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
					i_ = i_ + 1;
				END IF;
			END IF;
		ELSIF(table_ = 'CREDIT_VENTE')THEN
			SELECT INTO data_ a.date_credit, a.num_reference, a.montant, m.societe, c.compte, a.client as tiers, t.compte as compte_general, j.agence FROM yvs_compta_credit_client a 
				INNER JOIN yvs_com_client c ON a.client = c.id LEFT JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				LEFT JOIN yvs_grh_type_cout t ON a.type_credit = t.id WHERE a.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'CLIENT';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_credit ,'dd')::integer;
				_num_piece_ = data_.num_reference;	
				_echeance_ = data_.date_credit;
				_libelle_ = 'Credit N° '||data_.num_reference;				
				IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
					_error_ = 'Le credit est rattaché a un type qui n''a pas de compte';
				ELSE
					_compte_general_ = data_.compte_general;
				END IF;
				_compte_tiers_ = data_.tiers;				
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le client n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 1;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'CREDIT_ACHAT')THEN
			SELECT INTO data_ a.date_credit, a.num_reference, a.montant, m.societe, c.compte, a.fournisseur as tiers, t.compte as compte_general, j.agence FROM yvs_compta_credit_fournisseur a 
				INNER JOIN yvs_base_fournisseur c ON a.fournisseur = c.id INNER JOIN yvs_base_tiers m ON c.tiers = m.id LEFT JOIN yvs_compta_journaux j ON a.journal = j.id
				LEFT JOIN yvs_grh_type_cout t ON a.type_credit = t.id WHERE a.id = element_;
			IF(data_.num_reference IS NOT NULL AND data_.num_reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_credit ,'dd')::integer;
				_num_piece_ = data_.num_reference;	
				_echeance_ = data_.date_credit;
				_libelle_ = 'Credit N° '||data_.num_reference;					
				IF(data_.compte_general IS NULL OR data_.compte_general < 1)THEN
					_error_ = 'Le credit est rattaché a un type qui n''a pas de compte';
				ELSE
					_compte_general_ = data_.compte_general;
				END IF;
				_compte_tiers_ = data_.tiers;				
				_credit_ = 0;
				_debit_ = data_.montant;	
				_numero_ = 1;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = null;
				_compte_general_ = null;
				IF(data_.compte IS NULL OR data_.compte < 1)THEN
					_error_ = 'Le fournisseur n''a pas de compte collectif';
				ELSE
					_compte_general_ = data_.compte;
				END IF;
				_credit_ = data_.montant;
				_debit_ = 0;	
				_numero_ = 2;
				_num_ref_ = data_.num_reference;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;
			END IF;
		ELSIF(table_ = 'DOC_VIREMENT')THEN
			SELECT INTO data_ p.numero_piece, p.date_paiement, p.montant, p.cible, p.source, COALESCE(p.note, 'Virement N° '||p.numero_piece) AS note, c.compte AS compte_cible, s.compte AS compte_source, (SELECT l.compte FROM yvs_base_liaison_caisse l WHERE l.caisse_cible = p.cible AND l.caisse_source = p.source LIMIT 1) AS compte_interne, j.agence
				FROM yvs_compta_caisse_piece_virement p LEFT JOIN yvs_base_caisse s ON p.source = s.id LEFT JOIN yvs_base_caisse c ON p.cible = c.id LEFT JOIN yvs_compta_journaux j ON c.journal = j.id WHERE p.id = element_;
			IF(data_.numero_piece IS NOT NULL AND data_.numero_piece NOT IN ('', ' '))THEN	
				_table_tiers_ = 'EMPLOYE';	
				_agence_ = data_.agence;	
				_jour_ = to_char(data_.date_paiement ,'dd')::integer;
				_num_piece_ = data_.numero_piece;	
				_echeance_ = data_.date_paiement;
				_libelle_ = data_.note;

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
				_numero_ = 2;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_error_ = '';
				_compte_general_ = null;
				_credit_ = 0;
				IF(data_.compte_interne IS NULL OR data_.compte_interne < 1)THEN
					_error_ = 'ces comptes n''ont pas de compte de liaison';
				ELSE
					_compte_general_ = data_.compte_interne;
				END IF;
				_debit_ = data_.montant;
				_numero_ = 2;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				
				_debit_ = 0;
				_credit_ = data_.montant;
				_numero_ = 1;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
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
				_numero_ = 1;
				_num_ref_ = _num_piece_ ||'-0'||i_;						
				_id_ = _id_ - 1;
				INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
				i_ = i_ + 1;

				_numero_ = 2;
				FOR ligne_ IN SELECT sum(co.montant) AS total, tx.compte, tx.libelle, tx.augmentation FROM yvs_grh_type_cout tx INNER JOIN yvs_compta_cout_sup_piece_virement co ON co.type_cout = tx.id INNER JOIN yvs_compta_caisse_piece_virement dv ON dv.id = co.virement
					WHERE dv.id = element_ GROUP BY tx.id, tx.libelle 
				LOOP
					_compte_tiers_ = null;	
					IF(ligne_.total != 0)THEN
						_libelle_ = ligne_.libelle||' pour le virement N° '||data_.numero_piece;
						
						_credit_ = 0;
						_debit_ = ligne_.total;
						IF(i_ < 10)THEN
							_num_ref_ = data_.numero_piece ||'-0'||i_;
						ELSE
							_num_ref_ = data_.numero_piece ||'-'||i_;
						END IF;
						IF(ligne_.compte IS NULL OR ligne_.compte < 1)THEN
							_error_ = 'le cout supplementaire '''||ligne_.libelle||''' n''est pas rattaché a un compte general';
						END IF;
						_compte_general_ = ligne_.compte;						
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;

						
						_credit_ = ligne_.total;
						_debit_ = 0;
						IF(i_ < 10)THEN
							_num_ref_ = data_.numero_piece ||'-0'||i_;
						ELSE
							_num_ref_ = data_.numero_piece ||'-'||i_;
						END IF;
						IF(data_.source IS NULL OR data_.source < 1)THEN
							_error_ = 'ce reglement n''est associé à aucune emetteur';
						ELSIF(data_.compte_source IS NULL OR data_.compte_source < 1)THEN
							_error_ = 'ce reglement est rattaché à un emetteur qui n''a pas de compte général';
						END IF;
						_compte_general_ = data_.compte_source;
						_id_ = _id_ - 1;
						INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), round(_debit_::decimal, 0), round(_credit_::decimal, 0), _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
						i_ = i_ + 1;
					END IF;
				END LOOP;
			END IF;
		ELSIF(table_ = 'ORDRE_SALAIRE')THEN
			SELECT INTO data_ o.* FROM yvs_grh_ordre_calcul_salaire o WHERE o.id = element_;
			IF(data_.reference IS NOT NULL AND data_.reference NOT IN ('', ' '))THEN	
				_table_tiers_ = 'TIERS';	
				_jour_ = to_char(data_.date_execution ,'dd')::integer;
				_num_piece_ = data_.reference;	
				_echeance_ = data_.date_execution;
				-- Recupere gains
				-- récupère les éléments de gains en groupant par compte charge du gains
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, COALESCE(SUM(db.montant_payer),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition FROM yvs_grh_detail_bulletin db 
					LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat 
					LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id 
					WHERE el.visible_bulletin IS TRUE AND NOT (COALESCE(db.montant_payer,0)=0) AND el.actif IS TRUE AND el.retenue IS FALSE AND b.entete = element_ 
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, co.id ORDER BY ag.designation, el.nom
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;
					
					_error_ = '';
					_credit_ = 0;
					_numero_ = 1;
					_compte_general_ = null;
					_compte_tiers_ = null;
					IF(ligne_.id IS NULL OR ligne_.id < 1)THEN
						_error_ = 'certains bulletins ne sont pas rattachés aux elements de salaire';
					ELSIF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'le gain '||ligne_.nom||' n''est rattaché à aucun compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
-- 					RAISE NOTICE '- %',_compte_general_;
					_debit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- récupère du net a payer en groupant par compte collectif employé
				FOR ligne_ IN SELECT ag.id AS agence,  ag.designation, co.intitule, e.compte_collectif, (COALESCE(SUM(db.montant_payer),0) - COALESCE(SUM(db.retenu_salariale),0)) AS montant, co.saisie_analytique 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON e.compte_collectif = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.montant_payer,0)!=0 OR COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND b.entete = element_
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, co.id, e.compte_collectif ORDER BY ag.designation, e.compte_collectif, co.intitule
				LOOP
					_libelle_ = 'Gains liés à '||ligne_.intitule ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 1;
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
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, 0, _compte_general_, _compte_tiers_, _credit_, '', table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- Recupere retenues
				-- récupère les retenues patronales
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, el.compte_cotisation, COALESCE(SUM(db.montant_employeur),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.montant_employeur,0)!=0) AND el.actif IS TRUE AND el.retenue IS TRUE AND b.entete = element_
					AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, el.compte_charge, el.compte_cotisation, co.id ORDER BY ag.designation , el.compte_charge
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 5;
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
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _credit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
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
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;					
				END LOOP;
				-- récupère les retenues salariales sans saisie tiers en groupant par compte de cotisation des retenues
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_cotisation, COALESCE(SUM(db.retenu_salariale),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition 
					FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
					LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
					LEFT JOIN yvs_base_plan_comptable co ON el.compte_cotisation = co.id 
					WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND (COALESCE(el.saisi_compte_tiers, false) IS FALSE) 
					AND el.retenue IS TRUE AND b.entete = element_ AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val)))
					GROUP BY ag.id, el.id, el.compte_cotisation, co.id ORDER BY ag.designation , el.nom
				LOOP
					_libelle_ = ligne_.nom ||' ['|| ligne_.designation||']';
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 2;
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
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_credit_ IS NOT NULL AND _credit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _credit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;
							_coefficient_ = sous_.coefficient;
							_credit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;	
				-- récupère les retenues salariales avec saisie tiers en groupant par compte de charge des retenues
				FOR ligne_ IN SELECT ag.id AS agence, ag.designation, el.nom, el.compte_charge, COALESCE(SUM(db.retenu_salariale),0) AS montant, co.saisie_analytique, el.id, el.mode_repartition, t.id AS tiers, CONCAT(t.nom, ' ', t.prenom) AS nom_prenom
                                        FROM yvs_grh_detail_bulletin db LEFT JOIN yvs_grh_bulletins b ON b.id=db.bulletin LEFT JOIN yvs_grh_element_salaire el ON el.id=db.element_salaire 
                                        LEFT JOIN yvs_grh_contrat_emps c ON c.id=b.contrat LEFT JOIN yvs_grh_employes e ON e.id=c.employe LEFT JOIN yvs_agences ag ON e.agence=ag.id 
                                        LEFT JOIN yvs_base_plan_comptable co ON el.compte_charge = co.id LEFT JOIN yvs_base_tiers t ON e.compte_tiers = t.id 
                                        WHERE el.visible_bulletin IS TRUE AND (COALESCE(db.retenu_salariale,0)!=0) AND el.actif IS TRUE AND COALESCE(el.saisi_compte_tiers, false) IS TRUE 
                                        AND el.retenue IS TRUE AND b.entete = element_ AND ((agences_ IS NULL OR agences_ IN ('',' ')) OR ((agences_ IS NULL OR agences_ IN ('',' ')) OR (agences_ IS NOT NULL AND agences_ NOT IN ('',' ') AND e.agence::character varying in (select val from regexp_split_to_table(agences_,',') val))))
                                        GROUP BY ag.id, el.id, el.compte_charge, co.id, t.id ORDER BY ag.designation, el.nom
				LOOP
					_libelle_ = ligne_.nom ||' liée '|| ligne_.nom_prenom;
					_agence_ = ligne_.agence;

					_error_ = '';
					_debit_ = 0;
					_numero_ = 3;
					_compte_general_ = null;
					IF(ligne_.tiers IS NULL OR ligne_.tiers < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' est rattachée à des employés qui n''ont pas de compte tiers';
					ELSE
						_compte_tiers_ = ligne_.tiers;
					END IF;
					IF(ligne_.compte_charge IS NULL OR ligne_.compte_charge < 1)THEN
						_error_ = 'la retenue '||ligne_.nom||' n''a pas de compte de charge';
					ELSE
						_compte_general_ = ligne_.compte_charge;
					END IF;
					_credit_ = ligne_.montant;
					_num_ref_ = _num_piece_ ||'-0'||i_;						
					_id_ = _id_ - 1;
					INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);					
					IF(ligne_.saisie_analytique IS TRUE AND (_debit_ IS NOT NULL AND _debit_ > 0) AND (_compte_general_ IS NOT NULL AND _compte_general_ > 0))THEN
						_contenu_ = _id_;
						FOR sous_ IN SELECT * FROM yvs_compta_content_analytique(_agence_, element_, ligne_.id, _compte_general_, _compte_tiers_, _debit_, ligne_.mode_repartition, table_, FALSE, FALSE)
						LOOP
							_error_ = sous_.error;
							_centre_ = sous_.centre;	
							_coefficient_ = sous_.coefficient;
							_debit_ = sous_.valeur;
							INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, _contenu_, _centre_, _coefficient_, _numero_, _agence_, _warning_, _table_tiers_);
						END LOOP;
					END IF;
					i_ = i_ + 1;				
				END LOOP;	
			END IF;
		END IF;
	END IF;
	-- DETERMINATION DE L'ARRONDI
	SELECT INTO _debit_ SUM(_debit) FROM table_compta_content_journal;
	SELECT INTO _credit_ SUM(_credit) FROM table_compta_content_journal;
	somme_ = COALESCE(_debit_, 0) - COALESCE(_credit_, 0);
	IF(somme_ != 0 AND abs(somme_) <= valeur_limite_arrondi_)THEN
		_compte_tiers_ = null;
		IF(somme_ > 0)THEN
			SELECT INTO data_ * FROM table_compta_content_journal WHERE _credit > 0 ORDER BY _numero LIMIT 1;
			_libelle_ = 'arrondis gain';
			titre_ = 'ARRONDI_GAIN';
			IF(table_ = 'DOC_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'JOURNAL_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'DOC_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _credit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'FRAIS_MISSION')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _credit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CREDIT_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'CREDIT_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'DOC_VIREMENT')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'ORDRE_SALAIRE')THEN	
				_table_tiers_ = 'EMPLOYE';	
			END IF;				
			SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = societe_ AND n.nature = titre_;
			IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
				_error_ = 'Le compte des '||_libelle_||' n''est pas paramètré';
			END IF;
			_credit_ = somme_;
			_debit_ = 0;
		ELSE
			SELECT INTO data_ * FROM table_compta_content_journal WHERE _debit > 0 ORDER BY _numero LIMIT 1;
			_libelle_ = 'arrondis perte';
			titre_ = 'ARRONDI_PERTE';
			IF(table_ = 'DOC_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _debit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'JOURNAL_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'DOC_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'FRAIS_MISSION')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'PHASE_CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'PHASE_ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'PHASE_ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CAISSE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
				_libelle_ = 'arrondis gain';
				titre_ = 'ARRONDI_GAIN';
			ELSIF(table_ = 'CAISSE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
				_libelle_ = 'arrondis perte';
				titre_ = 'ARRONDI_PERTE';
				SELECT INTO _compte_tiers_ _compte_tiers FROM table_compta_content_journal WHERE _debit > 0 AND _compte_tiers IS NOT NULL LIMIT 1;
			ELSIF(table_ = 'CAISSE_DIVERS')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'ACOMPTE_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'ACOMPTE_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'CREDIT_VENTE')THEN	
				_table_tiers_ = 'CLIENT';	
			ELSIF(table_ = 'CREDIT_ACHAT')THEN	
				_table_tiers_ = 'FOURNISSEUR';	
			ELSIF(table_ = 'DOC_VIREMENT')THEN	
				_table_tiers_ = 'EMPLOYE';	
			ELSIF(table_ = 'ORDRE_SALAIRE')THEN	
				_table_tiers_ = 'EMPLOYE';	
			END IF;
			_libelle_ = 'Arrondi Perte';				
			SELECT INTO _compte_general_ c.id FROM yvs_base_plan_comptable c INNER JOIN yvs_base_nature_compte n ON c.nature_compte = n.id WHERE n.societe = societe_ AND n.nature = titre_;
			IF(_compte_general_ IS NULL OR _compte_general_ < 1)THEN
				_error_ = 'Le compte des '||_libelle_||' n''est pas paramètré';
			END IF;
			_credit_ = 0;
			_debit_ = -somme_;
		END IF;	
		_jour_ = data_._jour;
		_num_piece_ = data_._num_piece;
		_echeance_ = data_._echeance;
		_numero_ = data_._numero;	
		IF(i_ < 10)THEN
			_num_ref_ = _num_piece_ ||'-0'||i_;
		ELSE
			_num_ref_ = _num_piece_ ||'-'||i_;
		END IF;		
		_numero_ = COALESCE(_numero_, 0) + 1;
		_id_ = _id_ - 1;
		INSERT INTO table_compta_content_journal VALUES(_id_, _jour_, _num_piece_, _num_ref_, _compte_general_, _compte_tiers_, UPPER(_libelle_), _debit_, _credit_, _echeance_, _ref_externe_, _table_externe_, _statut_, _error_, 0, 0, 0, _numero_, _agence_, _warning_, _table_tiers_);
	END IF;
	RETURN QUERY SELECT * FROM table_compta_content_journal ORDER BY _numero, _debit DESC, _credit DESC, _id DESC, _jour, _num_piece;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION yvs_compta_content_journal(bigint, character varying, bigint, character varying, boolean)
  OWNER TO postgres;
