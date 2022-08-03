-- Function: equilibre_vente(bigint)

-- DROP FUNCTION equilibre_vente(bigint);

CREATE OR REPLACE FUNCTION equilibre_vente(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	av_ double precision default 0;
	contenu_ record;
	qte_ double precision default 0;
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

BEGIN
	-- Equilibre de l'etat reglé
	ttc_ = (select get_ttc_vente(id_));
	select into av_ sum(montant) from yvs_compta_caisse_piece_vente where vente = id_ and statut_piece = 'P';
	if(av_ is null)then
		av_ = 0;
	end if;

	-- Equilibre de l'etat livré
	for contenu_ in select article, sum(quantite) as qte from yvs_com_contenu_doc_vente where doc_vente = id_ group by article
	loop
		in_ = true;
		select into qte_ sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id where d.document_lie = id_ and c.article = contenu_.article and d.statut = 'V';
		if(qte_ is null)then
			qte_ = 0;
		end if;
		if(qte_ < contenu_.qte)then
			correct = false;
			if(qte_ > 0)then
				encours = true;
				exit;
			end if;
		end if;
	end loop;
	if(in_)then
		if(av_>=ttc_)then
			update yvs_com_doc_ventes set statut_regle = 'P' where id = id_;
		elsif (av_ > 0) then
			update yvs_com_doc_ventes set statut_regle = 'R' where id = id_;
		else
			update yvs_com_doc_ventes set statut_regle = 'W' where id = id_;
		end if;
		
		if(correct)then
			update yvs_com_doc_ventes set statut_livre = 'L' where id = id_;
		else
			if(encours)then
				update yvs_com_doc_ventes set statut_livre = 'R' where id = id_;
			else
				update yvs_com_doc_ventes set statut_livre = 'W' where id = id_;
			end if;
		end if;	
	else
		update yvs_com_doc_ventes set statut_regle = 'W', statut_livre = 'W' where id = id_;
	end if;	
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de vente';


-- Function: get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock_consigne(article_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	  stock_ double precision default 0;
	 
BEGIN
	if(depot_ is not null and depot_ >0)then
		select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and c.depot = depot_;
	else
		if(agence_ is not null and agence_ >0)then
			select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c inner join yvs_base_depots d on c.depot = d.id
				where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and d.agence = agence_;
		else
			if(societe_ is not null and societe_ >0)then
				select into stock_ SUM(c.quantite) from yvs_com_reservation_stock c inner join yvs_base_depots d on c.depot = d.id
					inner join yvs_agences a on d.agence = a.id
					where c.statut = 'V' and date_ between c.date_reservation and c.date_echeance and a.societe = societe;
			else
				stock_ = 0;
			end if;
		end if;
	end if;	
	if(stock_ is null)then
		stock_ = 0;
	end if;
	RETURN stock_;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, date)
  OWNER TO postgres;

  
  -- Function: get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date)

-- DROP FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date);

CREATE OR REPLACE FUNCTION get_stock_consigne(article_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date)
  RETURNS double precision AS
$BODY$
DECLARE
	 
BEGIN
	return get_stock_consigne(article_, depot_ ,agence_ , societe_, date_);
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_stock_consigne(bigint, bigint, bigint, bigint, bigint, date)
  OWNER TO postgres;

