-- Function: get_cout_total_achat_contenu(bigint)

-- DROP FUNCTION get_cout_total_achat_contenu(bigint);

CREATE OR REPLACE FUNCTION get_cout_total_achat_contenu(id_ bigint)
  RETURNS double precision AS
$BODY$
DECLARE
	record_ record;
	pua double precision default 0;
	cout double precision default 0;
	total_ double precision;

BEGIN
	pua = (select get_pua_total(id_));
	if(pua is null)then
		pua = 0;
	end if;
	for record_ in select montant from yvs_com_cout_additionel where contenu = id_
	loop
		if(record_.montant is null)then
			record_.montant = 0;
		end if;
		cout = cout + record_.montant;
	end loop;
	if(cout is null)then
		cout = 0;
	end if;
	total_ = pua + cout;
	if(total_ is null)then
		total_ = 0;
	end if;
    return total_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_cout_total_achat_contenu(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION get_cout_total_achat_contenu(bigint) IS 'retourne le cout d''achat d'' article';
