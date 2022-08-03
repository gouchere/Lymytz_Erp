-- Function: get_cout_stock_article(bigint, bigint, double precision)

-- DROP FUNCTION get_cout_stock_article(bigint, bigint, double precision);

CREATE OR REPLACE FUNCTION get_cout_stock_article(article_ bigint, depot_ bigint, quantite_ double precision)
  RETURNS double precision AS
$BODY$
DECLARE 
	entree_ record;  
	meth_ character varying;
	cout_ double precision default 0;
	qte_ double precision default 0;
	reste_ double precision default 0;
	stock_ double precision default 0;
	record_ record;
	pua_ double precision default 0;
BEGIN
	-- Recherche de la methode de valorisation de l'article
	select into record_ methode_val, pua FROM yvs_articles WHERE id =article_;
	meth_=record_.methode_val;
	pua_=record_.pua;
	if(meth_ is null)then
		meth_ = 'CMPI';
	end if;
	if(pua_ is null)then
		pua_ = 0;
	end if;
	if(meth_ = 'FIFO')then
		-- Sauvegarde la valeur de la quantitée demandée pour ne pas la perdre	
		qte_ = quantite_;
		-- Recherche des entrees d'un article dans un dépot dont le stock est superieur à 0
		for entree_ in select m.id as id, m.quantite as quantite, m.cout_stock as cout from yvs_base_mouvement_stock m where m.mouvement = 'E' and 
				((m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) is null or
				(m.quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = m.id)) > 0)
				and m.article = article_ and m.depot = depot_ order by m.date_doc, id
		loop
			-- Controle pour l'arret de la boucle (la qte doit etre egale à 0 pour sortir)
			if(qte_ > 0)then
				-- Recherche du reste en stock du lot courant
				select into reste_ (quantite - (select sum(quantite) from yvs_base_mouvement_stock where mouvement = 'S' and parent = entree_.id))
					from yvs_base_mouvement_stock where id = entree_.id;
				if(reste_ is null)then
					reste_ = entree_.quantite;
				end if;
				-- Teste pour vérifier si le lot courant peut couvrir la demande
				if(reste_ > qte_)then
					stock_ = qte_;
					qte_ = 0;
				else
					stock_ = reste_;
					qte_  = qte_ - stock_;
				end if;
				-- Calcul du cout global
				cout_ = cout_ + (stock_ * entree_.cout);
			else
				exit;
			end if;		
		end loop;
	elsif (meth_ = 'CMPI')then
		-- Recherche du dernier cout de stockage de l'article dans le depot
		select into cout_ cout_stock from yvs_base_mouvement_stock where mouvement = 'E' and article = article_ and depot = depot_ order by date_doc, id desc limit 1;
		if(cout_ is null)then
			cout_ = 0;
		end if;
		-- Calcul du cout global
		cout_ = cout_ * quantite_;
	elsif (meth_ = 'CMPII')then
	
	end if;
	IF( cout_=0 )THEN
		cout_=quantite_ * pua_;
	END IF;
	return cout_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_cout_stock_article(bigint, bigint, double precision)
  OWNER TO postgres;
COMMENT ON FUNCTION get_cout_stock_article(bigint, bigint, double precision) IS 'Retourne le cout moyen d''un article dans un dépot';
