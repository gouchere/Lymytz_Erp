

ALTER TABLE yvs_base_article_depot ADD COLUMN quantite_vendu double precision;
ALTER TABLE yvs_base_article_depot ADD COLUMN quantite_achat double precision;
ALTER TABLE yvs_base_article_depot ADD COLUMN quantite_produit double precision;

ALTER TABLE yvs_grh_header_bulletin ADD COLUMN agence bigint;
ALTER TABLE yvs_grh_header_bulletin
  ADD CONSTRAINT yvs_grh_header_bulletin_agence_fkey FOREIGN KEY (agence)
      REFERENCES yvs_agences (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


-- Column: id_poste

-- ALTER TABLE yvs_grh_header_bulletin DROP COLUMN id_poste;

ALTER TABLE yvs_grh_header_bulletin ADD COLUMN id_poste bigint;

ALTER TABLE yvs_grh_header_bulletin ADD COLUMN id_service bigint;
ALTER TABLE yvs_grh_header_bulletin
  ADD CONSTRAINT yvs_grh_header_bulletin_id_poste_fkey FOREIGN KEY (id_poste)
      REFERENCES yvs_grh_poste_de_travail (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE yvs_grh_header_bulletin
  ADD CONSTRAINT yvs_grh_header_bulletin_id_service_fkey FOREIGN KEY (id_service)
      REFERENCES yvs_grh_departement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- répare la table
UPDATE yvs_grh_header_bulletin h SET agence=(SELECT a.id FROM yvs_agences a WHERE a.codeagence=h.code_agence limit 1);
UPDATE yvs_grh_header_bulletin h SET id_service=(SELECT d.id FROM yvs_grh_departement d WHERE d.intitule=h.departement limit 1);
UPDATE yvs_grh_header_bulletin h SET id_poste=(SELECT d.id FROM yvs_grh_poste_de_travail d WHERE d.intitule=h.poste limit 1);


-- Function: update_doc_achats()

-- DROP FUNCTION update_doc_achats();


CREATE OR REPLACE FUNCTION update_doc_achats()
  RETURNS trigger AS
$BODY$    
DECLARE
	cont_ record;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
	depot_ bigint;
	tranche_ bigint;
	date_ date;
BEGIN
	if(NEW.type_doc = 'BRA' or NEW.type_doc = 'BLA')then	
		if(NEW.statut = 'V')then
			IF(NEW.date_livraison IS NULL) THEN 
			  date_=NEW.date_doc;
			ELSE
			  date_=NEW.date_livraison;
			END IF;
			RAISE NOTICE 'Update content facture %',date_;
			for cont_ in select id, article , quantite_recu as qte, pua_recu as prix, conditionnement, calcul_pr 
				FROM yvs_com_contenu_doc_achat where doc_achat = NEW.id
			loop
				select into arts_ a.id, a.methode_val from yvs_base_articles a where a.id = cont_.article;					
				--Insertion mouvement stock
				select into mouv_ id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' ;
				RAISE NOTICE ' parcours content facture A VALIDE %',mouv_;				
				if(mouv_ is not null)then
					if(arts_.methode_val = 'FIFO')then
						delete from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat' and article = cont_.article;
						if(NEW.type_doc = 'BLA')then
							result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'E', date_));
						else
							result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'S', date_));
						end if;
					else
						UPDATE yvs_base_mouvement_stock SET quantite = cont_.qte, cout_entree = cont_.prix , conditionnement=cont_.conditionnement , calcul_pr=cont_.calcul_pr, tranche = NEW.tranche,
															date_doc=date_
						WHERE id = mouv_;
					end if;
				else
					if(NEW.type_doc = 'BLA')then
						result = (select valorisation_stock(cont_.article,cont_.conditionnement, NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'E', date_));
					else
						RAISE NOTICE ' INSERT MOUVEMENT';
						result = (select valorisation_stock(cont_.article, cont_.conditionnement,NEW.depot_reception, NEW.tranche, cont_.qte, cont_.prix, 'yvs_com_contenu_doc_achat', cont_.id, 'S', OLD.date_livraison));
					end if;
				end if;	
			end loop;
		elsif(NEW.statut != 'V')then
			for cont_ in select id from yvs_com_contenu_doc_achat WHERE doc_achat = NEW.id
			loop		
				RAISE NOTICE ' parcours content dr ';
				--Recherche mouvement stock
				for mouv_ in select id from yvs_base_mouvement_stock where id_externe = cont_.id and table_externe = 'yvs_com_contenu_doc_achat'
				loop
					RAISE NOTICE ' parcours content facture A DELETE %',mouv_;
					delete from yvs_base_mouvement_stock where id = mouv_;
				end loop;
			end loop;
		end if;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_doc_achats()
  OWNER TO postgres;

UPDATE yvs_com_doc_achats d SET agence=(SELECT de.agence FROM yvs_base_depots de WHERE de.id=d.depot_reception)
WHERE agence is null;