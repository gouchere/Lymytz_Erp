-- DROP FUNCTION public.compta_et_journal(int8, int8, varchar, date, date, varchar);

CREATE OR REPLACE FUNCTION public.compta_et_journal(
	societe_ bigint, 
	agence_ bigint, 
	journaux_ character varying, 
	date_debut_ date, 
	date_fin_ date, 
	type_ character varying
)
RETURNS TABLE(
	id bigint, 
	code character varying, 
	intitule character varying, 
	piece bigint, 
	reference character varying, 
	date_piece date, 
	compte bigint, 
	numero character varying, 
	designation character varying, 
	compte_tiers bigint, 
	nom_prenom character varying, 
	plan bigint, 
	code_plan character varying, 
	libelle character varying, 
	jour integer, 
	description character varying, 
	lettrage character varying, 
	debit double precision, 
	credit double precision, 
	table_tiers character varying, 
	reference_externe character varying
)
LANGUAGE plpgsql
AS $function$
DECLARE 
	journaux_array bigint[];
BEGIN 	
	-- Conversion de la liste de journaux en tableau
	IF COALESCE(journaux_, '') NOT IN ('', ' ', '0') THEN
		SELECT array_agg(TRIM(head)::bigint) 
		INTO journaux_array
		FROM regexp_split_to_table(journaux_, ',') head 
		WHERE CHAR_LENGTH(TRIM(head)) > 0;
	END IF;

	-- Requête selon le type
	IF type_ = 'A' THEN
		-- Type Analytique
		RETURN QUERY
		SELECT 
			j.id, 
			j.code_journal as code, 
			j.intitule, 
			p.id as piece, 
			p.num_piece as reference, 
			p.date_piece, 
			c.id as compte, 
			c.num_compte as numero, 
			c.intitule as designation, 
			y.compte_tiers, 
			name_tiers(y.compte_tiers, y.table_tiers, 'N') as nom_prenom, 
			n.id as plan, 
			n.code_ref as code_plan, 
			n.designation as libelle, 
			y.jour, 
			UPPER(y.libelle)::character varying as description, 
			y.lettrage,
			l.debit, 
			l.credit, 
			y.table_tiers,
			y.num_ref as reference_externe
		FROM yvs_compta_pieces_comptable p 
		INNER JOIN yvs_compta_content_journal y ON p.id = y.piece 
		INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
		INNER JOIN yvs_compta_journaux j ON p.journal = j.id 
		INNER JOIN yvs_agences a ON j.agence = a.id
		INNER JOIN yvs_compta_content_analytique l ON y.id = l.contenu 
		INNER JOIN yvs_compta_centre_analytique n ON n.id = l.centre
		WHERE p.date_piece BETWEEN date_debut_ AND date_fin_
		  AND (agence_ IS NULL OR agence_ <= 0 OR a.id = agence_)
		  AND (societe_ IS NULL OR societe_ <= 0 OR a.societe = societe_)
		  AND (journaux_array IS NULL OR j.id = ANY(journaux_array))
		ORDER BY j.code_journal, p.date_piece, p.num_piece;
	ELSE
		-- Type standard
		RETURN QUERY
		SELECT 
			j.id, 
			j.code_journal as code, 
			j.intitule, 
			p.id as piece, 
			p.num_piece as reference, 
			p.date_piece, 
			c.id as compte, 
			c.num_compte as numero, 
			c.intitule as designation, 
			y.compte_tiers, 
			name_tiers(y.compte_tiers, y.table_tiers, 'N') as nom_prenom, 
			0::bigint as plan, 
			null::character varying as code_plan, 
			null::character varying as libelle, 
			y.jour, 
			UPPER(y.libelle)::character varying as description, 
			y.lettrage,
			y.debit, 
			y.credit, 
			y.table_tiers,
			y.num_ref as reference_externe
		FROM yvs_compta_pieces_comptable p 
		INNER JOIN yvs_compta_content_journal y ON p.id = y.piece 
		INNER JOIN yvs_base_plan_comptable c ON y.compte_general = c.id
		INNER JOIN yvs_compta_journaux j ON p.journal = j.id 
		INNER JOIN yvs_agences a ON j.agence = a.id
		WHERE p.date_piece BETWEEN date_debut_ AND date_fin_
		  AND (agence_ IS NULL OR agence_ <= 0 OR a.id = agence_)
		  AND (societe_ IS NULL OR societe_ <= 0 OR a.societe = societe_)
		  AND (journaux_array IS NULL OR j.id = ANY(journaux_array))
		ORDER BY j.code_journal, p.date_piece, p.num_piece;
	END IF;
END;
$function$;

