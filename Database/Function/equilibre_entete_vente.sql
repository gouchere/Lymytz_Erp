-- Function: equilibre_entete_vente(bigint)

-- DROP FUNCTION equilibre_entete_vente(bigint);

CREATE OR REPLACE FUNCTION equilibre_entete_vente(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	vente_ bigint;
	total_fv integer ;
	count_fv integer ;
	

BEGIN
	select into total_fv count(id) from yvs_com_doc_ventes where entete_doc = id_ and type_doc = 'FV';
	if(total_fv is null)then
		total_fv = 0;
	end if;
	if(total_fv > 0)then
		for vente_ in select id from yvs_com_doc_ventes where entete_doc = id_ and type_doc = 'FV'
		loop
			PERFORM equilibre_vente(vente_);
		end loop;
		
		-- Equilibre de l'etat livre
		select into count_fv count(id) from yvs_com_doc_ventes where entete_doc = id_ and type_doc = 'FV' and statut_livre = 'L';
		if(count_fv is null)then
			count_fv = 0;
		end if;
		if(total_fv = count_fv)then
			update yvs_com_entete_doc_vente set statut_livre = 'L' where id = id_;
		else
			select into count_fv count(id) from yvs_com_doc_ventes where entete_doc = id_ and type_doc = 'FV' and statut_livre = 'R';
			if(count_fv is null)then
				count_fv = 0;
			end if;
			if(count_fv > 0)then
				update yvs_com_entete_doc_vente set statut_livre = 'R' where id = id_;
			else
				update yvs_com_entete_doc_vente set statut_livre = 'W' where id = id_;
			end if;
		end if;
		
		-- Equilibre de l'etat regle		
		select into count_fv count(id) from yvs_com_doc_ventes where entete_doc = id_ and type_doc = 'FV' and statut_regle = 'P';
		if(count_fv is null)then
			count_fv = 0;
		end if;
		if(total_fv = count_fv)then
			update yvs_com_entete_doc_vente set statut_regle = 'P' where id = id_;
		else
			select into count_fv count(id) from yvs_com_doc_ventes where entete_doc = id_ and type_doc = 'FV' and statut_regle = 'R';
			if(count_fv is null)then
				count_fv = 0;
			end if;
			if(count_fv > 0)then
				update yvs_com_entete_doc_vente set statut_regle = 'R' where id = id_;
			else
				update yvs_com_entete_doc_vente set statut_regle = 'W' where id = id_;
			end if;
		end if;
	else
		update yvs_com_entete_doc_vente set statut_livre = 'W', statut_regle = 'W' where id = id_;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_entete_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_entete_vente(bigint) IS 'equilibre l''etat reglé et l''etat livré des entetes de vente';
