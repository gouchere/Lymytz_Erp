-- Function: add_categorie_compte_to_artiles(character varying, character varying, character varying)

-- DROP FUNCTION add_categorie_compte_to_artiles(character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION add_categorie_compte_to_artiles(groupe_ character varying, categorie_ character varying, compte_ character varying)
  RETURNS integer AS
$BODY$   
DECLARE 
	id_article_ bigint;
	line_ bigint;
	id_categorie_ bigint;
	id_compte_ bigint;
	nombre_ integer default 0;
BEGIN
	select into id_categorie_ id from yvs_base_categorie_comptable where code = categorie_;
	if(id_categorie_ is not null and id_categorie_ > 0)then	
		select into id_compte_ id from yvs_base_plan_comptable where num_compte = compte_;
		if(id_compte_ is not null and id_compte_ > 0)then			
			for id_article_ in select id from yvs_base_articles where categorie = groupe_
			loop
				select into line_ id from yvs_base_article_categorie_comptable where article = id_article_ and categorie = id_categorie_ and compte = id_compte_;
				if(line_ is null or line_ < 1)then	
					INSERT INTO yvs_base_article_categorie_comptable(article, categorie, compte, actif) VALUES (id_article_, id_categorie_, id_compte_, true);
					nombre_ = nombre_ + 1;
				end if;
			end loop;
		end if;
	end if;
	return nombre_;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION add_categorie_compte_to_artiles(character varying, character varying, character varying)
  OWNER TO postgres;
