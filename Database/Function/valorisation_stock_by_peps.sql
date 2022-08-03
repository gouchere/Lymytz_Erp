-- Function: valorisation_stock_by_peps(bigint, bigint, bigint, bigint, double precision, character varying, bigint, date)
-- DROP FUNCTION valorisation_stock_by_peps(bigint, bigint, bigint, bigint, double precision, character varying, bigint, date);
CREATE OR REPLACE FUNCTION valorisation_stock_by_peps(
    article_ bigint,
    unite_ bigint,
    depot_ bigint,
    tranche_ bigint,
    quantite_ double precision,
    tableexterne_ character varying,
    idexterne_ bigint,
    date_ date)
  RETURNS integer AS
$BODY$
DECLARE 
	i integer default 0;  
	entree_ record;  
	qte double precision;
	stock double precision default 0;
	reste double precision default 0;
	last_pr_ double precision default 0;
BEGIN
	-- Sauvegarde la valeur de la quantitée demandée pour ne pas la perdre	
	RAISE NOTICE 'quantite_ is %', quantite_;
	qte = quantite_;
	-- Recherche de la derniere entree de l'article dans le dépot
	last_pr_= COALESCE(get_pr(article_, depot_, 0, date_, unite_, 0), 0);
	-- Recherche des entrees d'un article dans un dépot dont le stock est superieur à 0
	if(tranche_ != null and tranche_ >0)then
		for entree_ in select m.id as id, m.quantite as quantite, m.cout_stock as cout from yvs_base_mouvement_stock m where m.mouvement = 'E' and 
				((m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) is null or
				(m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) > 0)
				and m.article = article_ and m.conditionnement = unite_ and m.depot = depot_ and tranche = tranche_ order by m.date_doc,id
		loop
			-- Controle pour l'arret de la boucle (la qte doit etre egale à 0 pour sortir)
			if(qte > 0)then
				-- Recherche du reste en stock du lot courant
				select into reste (quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = entree_.id))
					from yvs_base_mouvement_stock where id = entree_.id;
				if(reste is null)then
					reste = entree_.quantite;
				end if;
				-- Teste pour vérifier si le lot courant peut couvrir la demande
				if(reste > qte)then
					stock = qte;
					qte = 0;
				else
					stock = reste;
					qte  = qte - stock;
				end if;
				-- Insertion du mouvement de stock
				if (select insert_mouvement_stock(article_, depot_, tranche_, stock, entree_.cout, entree_.cout, entree_.id, tableexterne_, idexterne_, 'S', date_, last_pr_, 0))then
					i = i + 1;
				end if;
			else
				exit;
			end if;		
		end loop;
	else
		for entree_ in select m.id as id, m.quantite as quantite, m.cout_stock as cout from yvs_base_mouvement_stock m where m.mouvement = 'E' and 
			((m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) is null or
			(m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) > 0)
			and m.article = article_ and m.conditionnement = unite_ and m.depot = depot_ order by m.date_doc,id
		loop
			-- Controle pour l'arret de la boucle (la qte doit etre egale à 0 pour sortir)
			if(qte > 0)then
				-- Recherche du reste en stock du lot courant
				select into reste (quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = entree_.id))
					from yvs_base_mouvement_stock where id = entree_.id;
				if(reste is null)then
					reste = entree_.quantite;
				end if;
				-- Teste pour vérifier si le lot courant peut couvrir la demande
				if(reste > qte)then
					stock = qte;
					qte = 0;
				else
					stock = reste;
					qte  = qte - stock;
				end if;
				-- Insertion du mouvement de stock
				if (select insert_mouvement_stock(article_, depot_, tranche_, stock, entree_.cout, entree_.cout, entree_.id, tableexterne_, idexterne_, 'S', date_, last_pr_, 0))then
					i = i + 1;
				end if;
			else
				exit;
			end if;		
		end loop;
	end if;
	if(i<1)then
		select into reste c.pua_recu from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on d.id = c.doc_achat where c.article = article_ and c.conditionnement = unite_ order by d.date_doc desc limit 1;
		if(reste is null)then
			select into reste pua from yvs_base_articles where id = article_;
			if(reste is null)then
				reste = 0;
			end if;
		end if;
		if (select insert_mouvement_stock(article_, depot_, tranche_, quantite_, reste, reste, entree_.id, tableexterne_, idexterne_, 'S', date_, last_pr_, 0))then
			i = i + 1;
		end if;
	end if;
	RAISE NOTICE '-- i : %',i; 
	return i;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION valorisation_stock_by_peps(bigint, bigint, bigint, bigint, double precision, character varying, bigint, date)
  OWNER TO postgres;
COMMENT ON FUNCTION valorisation_stock_by_peps(bigint, bigint, bigint, bigint, double precision, character varying, bigint, date) IS 'Valorisation de stock pas la methode PEPS';
