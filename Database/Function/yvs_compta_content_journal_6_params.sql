-- Function: yvs_compta_content_journal(bigint, character varying, character varying, character varying, boolean)
-- DROP FUNCTION yvs_compta_content_journal(bigint, character varying, character varying, character varying, boolean);
CREATE OR REPLACE FUNCTION yvs_compta_content_journal(IN societe_ bigint, IN agences_ character varying, IN element_ character varying, IN table_ character varying, IN clear_ boolean)
  RETURNS TABLE(id bigint, jour integer, num_piece character varying, num_ref character varying, compte_general bigint, compte_tiers bigint, libelle character varying, debit double precision, credit double precision, echeance date, ref_externe bigint, table_externe character varying, statut character varying, error character varying, contenu bigint, centre bigint, coefficient double precision, numero integer, agence bigint, warning character varying, table_tiers character varying) AS
$BODY$
DECLARE
	id_ CHARACTER VARYING DEFAULT '0';
BEGIN    
	CREATE TEMP TABLE IF NOT EXISTS temp_table_compta_content_journal(_id BIGINT, _jour INTEGER, _num_piece CHARACTER VARYING, _num_ref CHARACTER VARYING, _compte_general BIGINT, _compte_tiers BIGINT, _libelle CHARACTER VARYING, _debit DOUBLE PRECISION, _credit DOUBLE PRECISION, _echeance DATE, _ref_externe BIGINT, _table_externe CHARACTER VARYING, _statut CHARACTER VARYING, _error character varying, _contenu bigint, _centre bigint, _coefficient double precision, _numero integer, _agence bigint, _warning character varying, _table_tiers character varying);
	DELETE FROM temp_table_compta_content_journal;
	FOR id_ IN SELECT head FROM regexp_split_to_table(element_,'-') head
	LOOP
		IF(TRIM(COALESCE(id_, '')) NOT IN ('', '0'))THEN
			INSERT INTO temp_table_compta_content_journal SELECT * FROM yvs_compta_content_journal(societe_, agences_, id_::bigint, table_, clear_);
		END IF;
	END LOOP;
	RETURN QUERY SELECT * FROM temp_table_compta_content_journal ORDER BY _ref_externe, _numero, _debit DESC, _credit DESC, _id DESC, _jour, _num_piece;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION yvs_compta_content_journal(bigint, character varying, character varying, character varying, boolean)
  OWNER TO postgres;
