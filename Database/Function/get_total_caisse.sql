-- Function: get_total_caisse(bigint, character varying, character varying)

-- DROP FUNCTION get_total_caisse(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION get_total_caisse(caisse_ bigint, table_ character varying, mouvement_ character varying)
  RETURNS double precision AS
$BODY$
DECLARE
	total_ double precision default 0;
	somme_mouv double precision default 0;

BEGIN
	if(caisse_ is not null and caisse_ > 0)then
		if(mouvement_ is not null)then
			if(table_ is null or table_ = '')then
				if(mouvement_ = 'D')then
					select into somme_mouv SUM(montant) from yvs_compta_mouvement_caisse where mouvement = 'D' and caisse = caisse_;
				elsif(mouvement_ = 'R')then
					select into somme_mouv SUM(montant) from yvs_compta_mouvement_caisse where mouvement = 'R' and caisse = caisse_;
				end if;
			else
				if(mouvement_ = 'D')then
					select into somme_mouv SUM(montant) from yvs_compta_mouvement_caisse where mouvement = 'D' and caisse = caisse_ and table_externe = table_;
				elsif(mouvement_ = 'R')then
					select into somme_mouv SUM(montant) from yvs_compta_mouvement_caisse where mouvement = 'R' and caisse = caisse_ and table_externe = table_;
				end if;
			end if;
			if(somme_mouv is null)then
				somme_mouv = 0;
			end if;
			total_ = somme_mouv;
		end if;
	end if;
	return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_total_caisse(bigint, character varying, character varying)
  OWNER TO postgres;
COMMENT ON FUNCTION get_total_caisse(bigint, character varying, character varying) IS 'retourne le total (dépense ou recette) d''une caisse ';
