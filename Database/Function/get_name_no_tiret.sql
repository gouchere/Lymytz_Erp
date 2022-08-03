-- Function: get_name_no_tiret(character varying)

-- DROP FUNCTION get_name_no_tiret(character varying);

CREATE OR REPLACE FUNCTION get_name_no_tiret(entree_ character varying)
  RETURNS character varying AS
$BODY$
DECLARE
    sortie_ character varying;
    tail_ integer default 0;
    table_ text[];

BEGIN
    if(entree_ is not null)then
	tail_ = char_length(entree_);
	table_ := string_to_array($1, '_');
	for i in array_lower(table_, 1) .. array_upper(table_, 1) loop
		if(sortie_ is null)then
			sortie_ := upper(substring(table_[i] from 1 for 1));
		else
			sortie_ := sortie_ || upper(substring(table_[i] from 1 for 1));
		end if;
		sortie_ := sortie_ || substring(table_[i] from 2 for char_length(table_[i]));
		RAISE NOTICE ' i %',sortie_;
	end loop;
	return sortie_;
    end if;
    return null;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_name_no_tiret(character varying)
  OWNER TO postgres;
COMMENT ON FUNCTION get_name_no_tiret(character varying) IS 'retourne le nom sans tirets de 8 (_) en mettant les lettres qui suivrent les tirets en majuscule';
