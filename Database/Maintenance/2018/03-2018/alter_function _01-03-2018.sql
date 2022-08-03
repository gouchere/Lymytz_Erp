-- Function: equilibre_approvision(bigint)

-- DROP FUNCTION equilibre_approvision(bigint);

CREATE OR REPLACE FUNCTION equilibre_approvision(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	fiche_ record;
	contenu_ record;
	achat_ double precision default 0;
	stock_ double precision default 0;
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

BEGIN
	select into fiche_ etat as statut from yvs_com_fiche_approvisionnement where id = id_;
	-- Equilibre de l'etat livré
	for contenu_ in select id, article, conditionnement as unite, quantite as qte from yvs_com_article_approvisionnement where fiche = id_
	loop
		in_ = true;
		select into achat_ COALESCE(SUM(c.quantite_recu), 0) from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on c.doc_achat = d.id
			where c.externe = contenu_.id and c.quantite_recu is not null and d.type_doc = 'FA' and d.statut in ('E','V');
		if(achat_ is null)then
			achat_ = 0;
		end if;
		select into stock_ COALESCE(SUM(c.quantite), 0) from yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on c.doc_stock = d.id
			where c.externe = contenu_.id and c.quantite is not null and d.type_doc = 'FA' and d.statut in ('E','V');
		if(stock_ is null)then
			stock_ = 0;
		end if;
		if((stock_ + achat_) < contenu_.qte)then
			correct = false;
			if((stock_ + achat_) > 0)then
				encours = true;
				exit;
			end if;
		end if;
	end loop;
	if(fiche_.statut = 'V')then
		if(in_)then
			if(correct)then
				update yvs_com_fiche_approvisionnement set statut_terminer = 'U' where id = id_;
			elsif(encours)then
				update yvs_com_fiche_approvisionnement set statut_terminer = 'R' where id = id_;
			else
				update yvs_com_fiche_approvisionnement set statut_terminer = 'W' where id = id_;
			end if;	
		else
			update yvs_com_fiche_approvisionnement set statut_terminer = 'W' where id = id_;
		end if;	
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_approvision(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_approvision(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de approvision';


-- Function: action_on_workflow_facture_achat()

-- DROP FUNCTION action_on_workflow_facture_achat();

CREATE OR REPLACE FUNCTION action_on_workflow_facture_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT') THEN
		--compte les étapes				
	ELSIF (action_='UPDATE' OR action_='DELETE') THEN 
		SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_facture_achat WHERE facture_achat=NEW.facture_achat;
		SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_facture_achat WHERE facture_achat=NEW.facture_achat AND etape_valid IS TRUE;
		UPDATE yvs_com_doc_achats SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=NEW.facture_achat;
	END IF;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_facture_achat()
  OWNER TO postgres;

CREATE TRIGGER action_on_workflow_facture_achat
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_workflow_valid_facture_achat
  FOR EACH ROW
  EXECUTE PROCEDURE action_on_workflow_facture_achat();
  
  -- Function: action_on_workflow_facture_vente()

-- DROP FUNCTION action_on_workflow_facture_vente();

CREATE OR REPLACE FUNCTION action_on_workflow_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT') THEN
		--compte les étapes				
	ELSIF (action_='UPDATE' OR action_='DELETE') THEN 
		SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_facture_vente WHERE facture_vente=NEW.facture_vente;
		SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_facture_vente WHERE facture_vente=NEW.facture_vente AND etape_valid IS TRUE;
		UPDATE yvs_com_doc_ventes SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=NEW.facture_vente;
	END IF;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_facture_vente()
  OWNER TO postgres;

CREATE TRIGGER action_on_workflow_facture_vente
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_workflow_valid_facture_vente
  FOR EACH ROW
  EXECUTE PROCEDURE action_on_workflow_facture_vente();
  
  
  -- Function: action_on_workflow_doc_stock()

-- DROP FUNCTION action_on_workflow_doc_stock();

CREATE OR REPLACE FUNCTION action_on_workflow_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT') THEN
		--compte les étapes				
	ELSIF (action_='UPDATE' OR action_='DELETE') THEN 
		SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_doc_stock WHERE doc_stock=NEW.doc_stock;
		SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_doc_stock WHERE doc_stock=NEW.doc_stock AND etape_valid IS TRUE;
		UPDATE yvs_com_doc_stocks SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=NEW.doc_stock;
	END IF;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_doc_stock()
  OWNER TO postgres;

CREATE TRIGGER action_on_workflow_doc_stock
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_workflow_valid_doc_stock
  FOR EACH ROW
  EXECUTE PROCEDURE action_on_workflow_doc_stock();
  
  
  -- Function: action_on_workflow_divers()

-- DROP FUNCTION action_on_workflow_divers();

CREATE OR REPLACE FUNCTION action_on_workflow_divers()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT') THEN
		--compte les étapes				
	ELSIF (action_='UPDATE' OR action_='DELETE') THEN 
		SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_divers WHERE divers=NEW.divers;
		SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_divers WHERE divers=NEW.divers AND etape_valid IS TRUE;
		UPDATE yvs_compta_caisse_doc_divers SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=NEW.divers;
	END IF;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_divers()
  OWNER TO postgres;

CREATE TRIGGER action_on_workflow_divers
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_workflow_valid_divers
  FOR EACH ROW
  EXECUTE PROCEDURE action_on_workflow_divers();
  
  
  -- Function: action_on_workflow_doc_caisse()

-- DROP FUNCTION action_on_workflow_doc_caisse();

CREATE OR REPLACE FUNCTION action_on_workflow_doc_caisse()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT') THEN
		--compte les étapes				
	ELSIF (action_='UPDATE' OR action_='DELETE') THEN 
		SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_doc_caisse WHERE doc_caisse=NEW.doc_caisse;
		SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_doc_caisse WHERE doc_caisse=NEW.doc_caisse AND etape_valid IS TRUE;
		UPDATE yvs_compta_caisse_doc_divers SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=NEW.doc_caisse;
	END IF;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_doc_caisse()
  OWNER TO postgres;

CREATE TRIGGER action_on_workflow_doc_caisse
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_workflow_valid_doc_caisse
  FOR EACH ROW
  EXECUTE PROCEDURE action_on_workflow_doc_caisse();
  
  
  -- Function: workflow(bigint, bigint)

-- DROP FUNCTION workflow(bigint, bigint);

CREATE OR REPLACE FUNCTION workflow(IN agence_ bigint, IN niveau_ bigint)
  RETURNS TABLE(element character varying, valeur integer, model character varying) AS
$BODY$
DECLARE
	lect_ record;
	etape_ bigint default 0;
	etapes_ bigint[];
	index_ integer default 0;
	taille_ bigint default 0;
	compteur_ integer default 0;
	continu_ boolean default false;
	model_ character varying default '';
	titre_ character varying default 'Vide';

	models_ character varying[] := array['MISSIONS','FORMATIONS','CONGES','FACTURE_ACHAT','FACTURE_VENTE','PERMISSION_CD','SORTIE_STOCK','OPERATION_DIVERS','BON_OPERATION_DIVERS'];
	i integer default 0;

BEGIN
	DROP TABLE IF EXISTS table_workflow;
	CREATE TEMP TABLE IF NOT EXISTS table_workflow(elt character varying, nbr integer, mod character varying);
	if(agence_ is not null and agence_ > 0)then
		for i in 1..9
		loop
			-- Initialisation des valeurs
			model_ = models_[i];
			etapes_ = null;
			compteur_ = 0;
			taille_ = 0;
			etape_ = 0;
			continu_ = false;

			-- Recuperation des etapes
			select into taille_ count(et.id) from yvs_workflow_etape_validation et inner join yvs_workflow_model_doc md on md.id = et.document_model where md.titre_doc = model_ group by et.id;
			if(taille_ > 0)then
				taille_ = 0;
				-- Recuperation de la 1ere etape
				select into etape_ et.id from yvs_workflow_etape_validation et inner join yvs_workflow_model_doc md on md.id = et.document_model where md.titre_doc = model_ and et.first_etape is true;
				if(etape_ is null)then
					-- Si la 1ere etape n'existe pas alors on recupere de la 1ere etape en fonction de l'ordre de création
					select into etape_ et.id from yvs_workflow_etape_validation et inner join yvs_workflow_model_doc md on md.id = et.document_model where md.titre_doc = model_ order by et.id limit 1;
				end if;
				if(etape_ is not null)then
					-- Ordonne les etapes
					while(continu_ is false)
					loop
						etapes_[taille_] = etape_;
						select into etape_ et.etape_suivante from yvs_workflow_etape_validation et inner join yvs_workflow_model_doc md on md.id = et.document_model where md.titre_doc = model_ and et.id = etape_;
						if(etape_ is null or taille_ > 50)then
							continu_ = true;
						end if;
						taille_ = taille_ + 1;
					end loop;
				end if;
				IF(model_ = 'MISSIONS')THEN
					RAISE NOTICE 'INT %','MISSIONS';
					titre_ = 'Missions en attente';
					for lect_ in select y.id from yvs_grh_missions y inner join yvs_grh_employes e on y.employe = e.id where y.statut_mission != 'V' and e.agence = agence_
					loop
						index_ = taille_;
						while(index_ > -1)
						loop
							-- Recherche de l'etape active
							etape_ = etapes_[index_];
							select into continu_ w.etape_valid from yvs_workflow_valid_mission w where w.mission = lect_.id and w.etape = etape_;
							if(continu_ is true)then
								select into etape_ et.etape_suivante from yvs_workflow_etape_validation et where et.id = etape_;
								if(etape_ is not null)then
									select into continu_ wd.can_valide from yvs_workflow_autorisation_valid_doc wd where wd.etape_valide = etape_ and wd.niveau_acces = niveau_ and wd.can_notify IS TRUE;
									if(continu_ is true)then
										compteur_ = compteur_ + 1;
									end if;
								end if;
								exit;
							end if;
							index_ = index_ -1;
						end loop;
					end loop;
				ELSIF(model_ = 'FORMATIONS')THEN
					RAISE NOTICE 'INT %','FORMATIONS';
					titre_ = 'Formation en attente';
					for lect_ in select y.formation from yvs_grh_formation_emps y inner join yvs_grh_employes e on m.employe = e.id inner join yvs_grh_formation f on y.formation = f.id where f.statut_formation != 'V' and e.agence = agence_
					loop
						index_ = taille_;
						while(index_ > -1)
						loop
							-- Recherche de l'etape active
							etape_ = etapes_[index_];
							select into continu_ w.etape_valid from yvs_workflow_valid_formation w where w.formation = lect_.id and w.etape = etape_;
							if(continu_ is true)then
								select into etape_ et.etape_suivante from yvs_workflow_etape_validation et where et.id = etape_;
								if(etape_ is not null)then
									select into continu_ wd.can_valide from yvs_workflow_autorisation_valid_doc wd where wd.etape_valide = etape_ and wd.niveau_acces = niveau_ and wd.can_notify IS TRUE;
									if(continu_ is true)then
										compteur_ = compteur_ + 1;
									end if;
								end if;
								exit;
							end if;
							index_ = index_ -1;
						end loop;
					end loop;
				ELSIF(model_ = 'CONGES')THEN
					titre_ = 'Conges en attente';
					for lect_ in select y.id from yvs_grh_conge_emps y inner join yvs_grh_employes e on y.employe = e.id where y.statut != 'V' and (y.nature = 'C' or y.nature = 'P' and y.duree_permission = 'L') and e.agence = agence_
					loop
						index_ = taille_;
						while(index_ > -1)
						loop
							-- Recherche de l'etape active
							etape_ = etapes_[index_];
							select into continu_ w.etape_valid from yvs_workflow_valid_conge w where w.conge = lect_.id and w.etape = etape_;
							if(continu_ is true)then
								select into etape_ et.etape_suivante from yvs_workflow_etape_validation et where et.id = etape_;
								if(etape_ is not null)then
									select into continu_ wd.can_valide from yvs_workflow_autorisation_valid_doc wd where wd.etape_valide = etape_ and wd.niveau_acces = niveau_ and wd.can_notify IS TRUE;
									if(continu_ is true)then
										compteur_ = compteur_ + 1;
									end if;
								end if;
								exit;
							end if;
							index_ = index_ -1;
						end loop;
					end loop;
				ELSIF(model_ = 'FACTURE_ACHAT')THEN
					titre_ = 'Facture achat en attente';
					for lect_ in select y.id from yvs_com_doc_achats y where y.type_doc = 'FA' and y.statut != 'V' and y.agence = agence_
					loop
						index_ = taille_;
						while(index_ > -1)
						loop
							-- Recherche de l'etape active
							etape_ = etapes_[index_];
							select into continu_ w.etape_valid from yvs_workflow_valid_facture_achat w where w.facture_achat = lect_.id and w.etape = etape_;
							if(continu_ is true)then
								select into etape_ et.etape_suivante from yvs_workflow_etape_validation et where et.id = etape_;
								if(etape_ is not null)then
									select into continu_ wd.can_valide from yvs_workflow_autorisation_valid_doc wd where wd.etape_valide = etape_ and wd.niveau_acces = niveau_ and wd.can_notify IS TRUE;
									if(continu_ is true)then
										compteur_ = compteur_ + 1;
									end if;
								end if;
								exit;
							end if;
							index_ = index_ -1;
						end loop;
					end loop;
				ELSIF(model_ = 'FACTURE_VENTE')THEN
					titre_ = 'Facture vente en attente';
					for lect_ in select y.id from yvs_com_doc_ventes y inner join yvs_com_entete_doc_vente ev on ev.id = y.entete_doc where y.type_doc = 'FV' and y.statut != 'V' and ev.agence = agence_
					loop
						index_ = taille_;
						while(index_ > -1)
						loop
							-- Recherche de l'etape active
							etape_ = etapes_[index_];
							select into continu_ w.etape_valid from yvs_workflow_valid_facture_vente w where w.facture_vente = lect_.id and w.etape = etape_;
							if(continu_ is true)then
								select into etape_ et.etape_suivante from yvs_workflow_etape_validation et where et.id = etape_;
								if(etape_ is not null)then
									select into continu_ wd.can_valide from yvs_workflow_autorisation_valid_doc wd where wd.etape_valide = etape_ and wd.niveau_acces = niveau_ and wd.can_notify IS TRUE;
									if(continu_ is true)then
										compteur_ = compteur_ + 1;
									end if;
								end if;
								exit;
							end if;
							index_ = index_ -1;
						end loop;
					end loop;
				ELSIF(model_ = 'PERMISSION_CD')THEN
					titre_ = 'Permission en attente';
					for lect_ in select y.id from yvs_grh_conge_emps y inner join yvs_grh_employes e on y.employe = e.id where y.statut != 'V' and y.nature = 'P' and y.duree_permission = 'C' and e.agence = agence_
					loop
						index_ = taille_;
						while(index_ > -1)
						loop
							-- Recherche de l'etape active
							etape_ = etapes_[index_];
							select into continu_ w.etape_valid from yvs_workflow_valid_conge w where w.conge = lect_.id and w.etape = etape_;
							if(continu_ is true)then
								select into etape_ et.etape_suivante from yvs_workflow_etape_validation et where et.id = etape_;
								if(etape_ is not null)then
									select into continu_ wd.can_valide from yvs_workflow_autorisation_valid_doc wd where wd.etape_valide = etape_ and wd.niveau_acces = niveau_ and wd.can_notify IS TRUE;
									if(continu_ is true)then
										compteur_ = compteur_ + 1;
									end if;
								end if;
								exit;
							end if;
							index_ = index_ -1;
						end loop;
					end loop;
				ELSIF(model_ = 'SORTIE_STOCK')THEN
					titre_ = 'Sortie en attente';
					for lect_ in select y.id from yvs_com_doc_stocks y inner join yvs_base_depots e on y.source = e.id where y.statut != 'V' and e.agence = agence_
					loop
						index_ = taille_;
						while(index_ > -1)
						loop
							-- Recherche de l'etape active
							etape_ = etapes_[index_];
							select into continu_ w.etape_valid from yvs_workflow_valid_doc_stock w where w.doc_stock = lect_.id and w.etape = etape_;
							if(continu_ is true)then
								select into etape_ et.etape_suivante from yvs_workflow_etape_validation et where et.id = etape_;
								if(etape_ is not null)then
									select into continu_ wd.can_valide from yvs_workflow_autorisation_valid_doc wd where wd.etape_valide = etape_ and wd.niveau_acces = niveau_ and wd.can_notify IS TRUE;
									if(continu_ is true)then
										compteur_ = compteur_ + 1;
									end if;
								end if;
								exit;
							end if;
							index_ = index_ -1;
						end loop;
					end loop;
				ELSIF(model_ = 'OPERATION_DIVERS' or model_ = 'BON_OPERATION_DIVERS')THEN
					titre_ = 'Opération Diverse en attente';
					for lect_ in select y.id from yvs_compta_caisse_doc_divers y inner join yvs_users_agence e on y.author = e.id where y.statut_doc = 'V' and e.agence = agence_
					loop
						index_ = taille_;
						while(index_ > -1)
						loop
							-- Recherche de l'etape active
							etape_ = etapes_[index_];
							select into continu_ w.etape_valid from yvs_workflow_valid_doc_caisse w where w.doc_caisse = lect_.id and w.etape = etape_;
							if(continu_ is true)then
								select into etape_ et.etape_suivante from yvs_workflow_etape_validation et where et.id = etape_;
								if(etape_ is not null)then
									select into continu_ wd.can_valide from yvs_workflow_autorisation_valid_doc wd where wd.etape_valide = etape_ and wd.niveau_acces = niveau_ and wd.can_notify IS TRUE;
									if(continu_ is true)then
										compteur_ = compteur_ + 1;
									end if;
								end if;
								exit;
							end if;
							index_ = index_ -1;
						end loop;
					end loop;
				ELSE
					titre_ = 'Missions en attente';
				END IF;
			end if;
			if(compteur_ > 0)then
				insert into table_workflow values(titre_, compteur_, model_);
			end if;
		end loop;
	end if;
	return QUERY select * from table_workflow order by elt;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION workflow(bigint, bigint)
  OWNER TO postgres;
