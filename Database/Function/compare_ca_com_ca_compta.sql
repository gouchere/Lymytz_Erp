-- Function: compare_ca_com_ca_compta(bigint, bigint, date, date)
DROP FUNCTION compare_ca_com_ca_compta(bigint, bigint, date, date);
CREATE OR REPLACE FUNCTION compare_ca_com_ca_compta(IN societe_ bigint, IN agence_ bigint, IN date_debut_ date, IN date_fin_ date)
  RETURNS TABLE(id bigint, numero character varying, debit double precision, credit double precision) AS
$BODY$
DECLARE
	result_ RECORD;

	query_ CHARACTER VARYING default 'SELECT d.id, d.num_doc FROM yvs_com_doc_ventes d 
						INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_agences a ON e.agence = a.id 
						WHERE d.type_doc = ''FV'' AND d.statut = ''V'' AND a.societe = '||societe_;

	ca_ DOUBLE PRECISION default 0;
	credit_ DOUBLE PRECISION default 0;

	count_ integer default 0;
	index_ integer default 0;

BEGIN
	CREATE TEMP TABLE IF NOT EXISTS table_ca_com_ca_compta(_id_ bigint, _numero_ character varying, _debit_ double precision, _credit_ double precision); 
	DELETE FROM table_ca_com_ca_compta;
	IF(COALESCE(agence_, 0) >0)THEN
		query_ = query_ || ' AND e.agence = '||agence_;
	END IF;
	query_ = query_ || ' AND e.date_entete BETWEEN '||QUOTE_LITERAL(date_debut_)||' AND '||QUOTE_LITERAL(date_fin_);
	EXECUTE REPLACE(query_,'d.id, d.num_doc', 'COUNT(d.id)') INTO count_;
	query_ = query_ || ' ORDER BY d.num_doc';
-- 	RAISE NOTICE 'query_ : %',query_;
	FOR result_ IN EXECUTE query_
	LOOP
		index_ = index_ + 1;
		ca_ = get_ca_vente(result_.id);
		credit_ := (SELECT SUM(cj.credit) FROM yvs_compta_content_journal cj INNER JOIN yvs_base_plan_comptable co ON cj.compte_general = co.id
				WHERE cj.ref_externe = result_.id AND cj.table_externe = 'DOC_VENTE' AND co.num_compte LIKE '70%');
		IF(ABS(COALESCE(ca_, 0) - COALESCE(credit_, 0)) > 2)THEN
			INSERT INTO table_ca_com_ca_compta VALUES(result_.id, result_.num_doc, COALESCE(ca_, 0), COALESCE(credit_, 0));
			RAISE NOTICE '%/% : %', index_, count_, result_.num_doc;
		ELSE		
			RAISE NOTICE '%/%', index_, count_;
		END IF;
	END LOOP;
	return QUERY SELECT * FROM table_ca_com_ca_compta;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION compare_ca_com_ca_compta(bigint, bigint, date, date)
  OWNER TO postgres;
COMMENT ON FUNCTION compare_ca_com_ca_compta(bigint, bigint, date, date) IS 'compare le chiffre d''affaire commercial a celui comptable';
