CREATE OR REPLACE FUNCTION action_on_workflow_approvissionnement()
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
		SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_approvissionnement WHERE "document"=NEW."document";
		SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_approvissionnement WHERE "document"=NEW."document" AND etape_valid IS TRUE;
		UPDATE yvs_com_fiche_approvisionnement SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=NEW."document";
	END IF;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_approvissionnement()
  OWNER TO postgres;


CREATE TRIGGER action_on_workflow_approvissionnement
  AFTER INSERT OR UPDATE OR DELETE
  ON yvs_workflow_valid_approvissionnement
  FOR EACH ROW
  EXECUTE PROCEDURE action_on_workflow_approvissionnement();
  
  
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

	i integer default 0;

BEGIN
	DROP TABLE IF EXISTS table_workflow;
	CREATE TEMP TABLE IF NOT EXISTS table_workflow(elt character varying, nbr integer, mod character varying);
	if(agence_ is not null and agence_ > 0)then
		for model_ in select titre_doc from yvs_workflow_model_doc
		loop
			-- Initialisation des valeurs
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
					for lect_ in select y.id from yvs_compta_caisse_doc_divers y inner join yvs_users_agence e on y.author = e.id where y.statut_doc != 'V' and e.agence = agence_
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
				ELSIF(model_ = 'APPROVISIONNEMENT')THEN
					titre_ = 'Approvisionnment en attente';
					for lect_ in select y.id from yvs_com_fiche_approvisionnement y inner join yvs_users_agence e on y.author = e.id where y.etat != 'V' and e.agence = agence_
					loop
						index_ = taille_;
						while(index_ > -1)
						loop
							-- Recherche de l'etape active
							etape_ = etapes_[index_];
							select into continu_ w.etape_valid from yvs_workflow_valid_approvissionnement w where w.document = lect_.id and w.etape = etape_;
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

  
  -- Function: action_on_workflow_approvissionnement()

-- DROP FUNCTION action_on_workflow_approvissionnement();

CREATE OR REPLACE FUNCTION action_on_workflow_approvissionnement()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
	id_ BIGINT DEFAULT 0;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT' OR action_='UPDATE' ) THEN
		id_ = NEW."document";
	ELSIF (action_='TRUNCATE' OR action_='DELETE') THEN 
		id_ = OLD."document";
	END IF;
	SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_approvissionnement WHERE "document"=id_;
	SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_approvissionnement WHERE "document"=id_ AND etape_valid IS TRUE;
	UPDATE yvs_com_fiche_approvisionnement SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=id_;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_approvissionnement()
  OWNER TO postgres;
  
  -- Function: action_on_workflow_doc_caisse()

-- DROP FUNCTION action_on_workflow_doc_caisse();

CREATE OR REPLACE FUNCTION action_on_workflow_doc_caisse()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
	id_ BIGINT DEFAULT 0;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT' OR action_='UPDATE' ) THEN
		id_ = NEW.doc_caisse;
	ELSIF (action_='TRUNCATE' OR action_='DELETE') THEN 
		id_ = OLD.doc_caisse;
	END IF;
	SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_doc_caisse WHERE doc_caisse=id_;
	SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_doc_caisse WHERE doc_caisse=id_ AND etape_valid IS TRUE;
	UPDATE yvs_compta_caisse_doc_divers SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=id_;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_doc_caisse()
  OWNER TO postgres;

  
  -- Function: action_on_workflow_doc_stock()

-- DROP FUNCTION action_on_workflow_doc_stock();

CREATE OR REPLACE FUNCTION action_on_workflow_doc_stock()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
	id_ BIGINT DEFAULT 0;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT' OR action_='UPDATE' ) THEN
		id_ = NEW.doc_stock;
	ELSIF (action_='TRUNCATE' OR action_='DELETE') THEN 
		id_ = OLD.doc_stock;
	END IF;
	SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_doc_stock WHERE doc_stock=id_;
	SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_doc_stock WHERE doc_stock=id_ AND etape_valid IS TRUE;
	UPDATE yvs_com_doc_stocks SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=id_;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_doc_stock()
  OWNER TO postgres;

  
  -- Function: action_on_workflow_facture_achat()

-- DROP FUNCTION action_on_workflow_facture_achat();

CREATE OR REPLACE FUNCTION action_on_workflow_facture_achat()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
	id_ BIGINT DEFAULT 0;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT' OR action_='UPDATE' ) THEN
		id_ = NEW.facture_achat;
	ELSIF (action_='TRUNCATE' OR action_='DELETE') THEN 
		id_ = OLD.facture_achat;
	END IF;
	SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_facture_achat WHERE facture_achat=id_;
	SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_facture_achat WHERE facture_achat=id_ AND etape_valid IS TRUE;
	UPDATE yvs_com_doc_achats SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=id_;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_facture_achat()
  OWNER TO postgres;

  
  -- Function: action_on_workflow_facture_vente()

-- DROP FUNCTION action_on_workflow_facture_vente();

CREATE OR REPLACE FUNCTION action_on_workflow_facture_vente()
  RETURNS trigger AS
$BODY$    
DECLARE
	etape_total_ INTEGER DEFAULT 0;
	etape_valide_ INTEGER DEFAULT 0;
	action_ CHARACTER VARYING;
	id_ BIGINT DEFAULT 0;
BEGIN	
	action_= TG_OP;	--INSERT DELETE TRONCATE UPDATE        
	IF(action_='INSERT' OR action_='UPDATE' ) THEN
		id_ = NEW.facture_vente;
	ELSIF (action_='TRUNCATE' OR action_='DELETE') THEN 
		id_ = OLD.facture_vente;
	END IF;
	SELECT INTO etape_total_ COUNT(*) FROM yvs_workflow_valid_facture_vente WHERE facture_vente=id_;
	SELECT INTO etape_valide_ COUNT(*) FROM yvs_workflow_valid_facture_vente WHERE facture_vente=id_ AND etape_valid IS TRUE;
	UPDATE yvs_com_doc_ventes SET etape_total=etape_total_, etape_valide=etape_valide_ WHERE id=id_;
	RETURN OLD;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action_on_workflow_facture_vente()
  OWNER TO postgres;


  
  -- Function: equilibre_achat(bigint)

-- DROP FUNCTION equilibre_achat(bigint);

CREATE OR REPLACE FUNCTION equilibre_achat(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	av_ double precision default 0;
	ch_ bigint default 0;
	contenu_ record;
	qte_ double precision default 0;
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

BEGIN
	-- Equilibre de l'etat reglé
	ttc_ = (select get_ttc_achat(id_));
	select into av_ sum(montant) from yvs_compta_caisse_piece_achat where achat = id_ and statut_piece = 'P';
	if(av_ is null)then
		av_ = 0;
	end if;

	-- Equilibre de l'etat livré
	for contenu_ in select id, article, conditionnement as unite, quantite_recu as qte from yvs_com_contenu_doc_achat where doc_achat = id_
	loop
		in_ = true;
		select into qte_ sum(c.quantite_recu) from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on c.doc_achat = d.id 
		where c.parent = contenu_.id and c.article = contenu_.article and c.conditionnement = contenu_.unite and c.article = contenu_.article and d.type_doc = 'BLA' and d.statut = 'V';
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
	-- Bonus
	for contenu_ in select id, article_bonus as article, conditionnement_bonus as unite, coalesce(quantite_bonus, 0) as qte from yvs_com_contenu_doc_achat where doc_achat = id_ and coalesce(quantite_bonus, 0) > 0
	loop
		in_ = true;
		select into qte_ sum(c.quantite_recu) from yvs_com_contenu_doc_achat c inner join yvs_com_doc_achats d on c.doc_achat = d.id 
		where c.parent = contenu_.id and c.article = contenu_.article and c.conditionnement = contenu_.unite and c.article = contenu_.article and d.type_doc = 'BLA' and d.statut = 'V';
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
	RAISE NOTICE 'in_ %',in_;
	if(in_)then
		select into ch_ count(y.id) from yvs_compta_caisse_piece_achat y inner join yvs_base_mode_reglement m on y.model = m.id where y.achat = id_ and m.type_reglement = 'BANQUE';
		if(av_>=ttc_)then
			update yvs_com_doc_achats set statut_regle = 'P' where id = id_;
		elsif (av_ > 0 or ch_ > 0) then
			update yvs_com_doc_achats set statut_regle = 'R' where id = id_;
		else
			update yvs_com_doc_achats set statut_regle = 'W' where id = id_;
		end if;
		
		RAISE NOTICE 'correct % ',correct;
		if(correct)then
			update yvs_com_doc_achats set statut_livre = 'L' where id = id_;
		else
			RAISE NOTICE 'encours % ',encours;
			if(encours)then
				update yvs_com_doc_achats set statut_livre = 'R' where id = id_;
			else
				update yvs_com_doc_achats set statut_livre = 'W' where id = id_;
			end if;
		end if;	
	else
		update yvs_com_doc_achats set statut_regle = 'W', statut_livre = 'W' where id = id_;
	end if;	
	update yvs_workflow_valid_facture_achat set date_update = date_update;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_achat(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_achat(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de achat';


-- Function: equilibre_vente(bigint)

-- DROP FUNCTION equilibre_vente(bigint);

CREATE OR REPLACE FUNCTION equilibre_vente(id_ bigint)
  RETURNS boolean AS
$BODY$
DECLARE
	ttc_ double precision default 0;
	av_ double precision default 0;
	ch_ bigint default 0;
	contenu_ record;
	qte_ double precision default 0;
	correct boolean default true;
	encours boolean default false;
	in_ boolean default false;

BEGIN
	-- Equilibre de l'etat reglé
	ttc_ = (select get_ttc_vente(id_));
	select into av_ sum(coalesce(montant,0)) from yvs_compta_caisse_piece_vente where vente = id_ and statut_piece = 'P';
	if(av_ is null)then
		av_ = 0;
	end if;

	-- Equilibre de l'etat livré
	for contenu_ in select id, article, conditionnement as unite, quantite as qte from yvs_com_contenu_doc_vente where doc_vente = id_
	loop
		in_ = true;
		select into qte_ sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id 
		where c.parent = contenu_.id and c.article = contenu_.article and c.conditionnement = contenu_.unite and c.article = contenu_.article and d.type_doc = 'BLV' and d.statut = 'V';
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
	-- Bonus
	for contenu_ in select id, article_bonus as article, conditionnement_bonus as unite, coalesce(quantite_bonus, 0) as qte from yvs_com_contenu_doc_vente where doc_vente = id_ and coalesce(quantite_bonus, 0) > 0
	loop
		in_ = true;
		select into qte_ sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id 
		where c.parent = contenu_.id and c.article = contenu_.article and c.conditionnement = contenu_.unite and c.article = contenu_.article and d.type_doc = 'BLV' and d.statut = 'V';
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
		select into ch_ count(y.id) from yvs_compta_caisse_piece_vente y inner join yvs_base_mode_reglement m on y.model = m.id where y.vente = id_ and m.type_reglement = 'BANQUE';
		if(av_>=ttc_)then
			update yvs_com_doc_ventes set statut_regle = 'P' where id = id_;
		elsif (av_ > 0 or ch_ > 0) then
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
	update yvs_workflow_valid_facture_vente set date_update = date_update;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_vente(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_vente(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de vente';


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
	update yvs_workflow_valid_approvisionnement set date_update = date_update;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION equilibre_approvision(bigint)
  OWNER TO postgres;
COMMENT ON FUNCTION equilibre_approvision(bigint) IS 'equilibre l''etat reglé et l''etat livré des documents de approvision';


CREATE OR REPLACE FUNCTION delete_declaration_production()
  RETURNS trigger AS
$BODY$    
DECLARE

BEGIN
	delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	return old;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION delete_declaration_production()
  OWNER TO postgres;
  
CREATE TRIGGER delete_
  AFTER DELETE
  ON yvs_prod_declaration_production
  FOR EACH ROW
  EXECUTE PROCEDURE delete_declaration_production();
  
  -- Function: insert_declaration_production()

-- DROP FUNCTION insert_declaration_production();

CREATE OR REPLACE FUNCTION insert_declaration_production()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	prix_ double precision default 0;
	result boolean default false;
BEGIN
	if(NEW.statut = 'V') then
		select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre;
		--Insertion mouvement stock
		result = (select valorisation_stock(ordre_.article, NEW.depot, 0, NEW.quantite, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_declaration));
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_declaration_production()
  OWNER TO postgres;


CREATE TRIGGER insert_
  AFTER INSERT
  ON yvs_prod_declaration_production
  FOR EACH ROW
  EXECUTE PROCEDURE insert_declaration_production();
  
  -- Function: update_declaration_production()

-- DROP FUNCTION update_declaration_production();

CREATE OR REPLACE FUNCTION update_declaration_production()
  RETURNS trigger AS
$BODY$    
DECLARE
	ordre_ record;
	prix_ double precision default 0;
	mouv_ bigint;
	arts_ record;
	result boolean default false;
BEGIN
	if(NEW.statut = 'V' and OLD.statut != 'V') then
		select into ordre_ * from yvs_prod_ordre_fabrication where id = NEW.ordre;
		--Insertion mouvement stock
		select into mouv_ id from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
		if(mouv_ is not null)then
			select into arts_ * from yvs_base_articles a where a.id = composant_.article;
			if(arts_.methode_val = 'FIFO')then
				delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
				result = (select valorisation_stock(ordre_.article, NEW.depot, 0, NEW.quantite, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_declaration));
			else
				update yvs_base_mouvement_stock set quantite = NEW.quantite, cout_entree = prix_, conditionnement = NEW.conditionnement where id = mouv_;
			end if;
		else
			result = (select valorisation_stock(ordre_.article, NEW.depot, 0, NEW.quantite, prix_, ''||TG_TABLE_NAME||'', NEW.id, 'E', NEW.date_declaration));
		end if;	
	elsif(NEW.statut != 'V')then
		delete from yvs_base_mouvement_stock where id_externe = OLD.id and table_externe = TG_TABLE_NAME;
	end if;
	return new;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_declaration_production()
  OWNER TO postgres;


CREATE TRIGGER update_
  AFTER UPDATE
  ON yvs_prod_declaration_production
  FOR EACH ROW
  EXECUTE PROCEDURE update_declaration_production();
  
  -- Function: insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)

-- DROP FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date);

CREATE OR REPLACE FUNCTION insert_mouvement_stock(article_ bigint, depot_ bigint, tranche_ bigint, quantite_ double precision, coutentree_ double precision, cout_ double precision, parent_ bigint, tableexterne_ character varying, idexterne_ bigint, mouvement_ character varying, date_ date)
  RETURNS boolean AS
$BODY$
DECLARE
       operation_ character varying default '';
       ligne_ record;
BEGIN
	CASE tableexterne_
		WHEN 'yvs_com_contenu_doc_vente' THEN  
			select into ligne_ qualite, conditionnement, lot from yvs_com_contenu_doc_vente where id = idexterne_;
			operation_='Vente';
		WHEN 'yvs_com_contenu_doc_achat' THEN	
			select into ligne_ qualite, conditionnement, lot from yvs_com_contenu_doc_achat where id = idexterne_;
			operation_='Achat';
		WHEN 'yvs_com_ration' THEN	
			select into ligne_ qualite, conditionnement, lot from yvs_com_ration where id = idexterne_;
			operation_='Ration';
		WHEN 'yvs_com_contenu_doc_stock' THEN	
			select into ligne_ d.type_doc, c.qualite, c.conditionnement, c.lot_sortie as lot FROM yvs_com_contenu_doc_stock c inner join yvs_com_doc_stocks d on d.id=c.doc_stock WHERE c.id=idexterne_ limit 1;
			if(ligne_.type_doc='FT') then 
				operation_='Transfert';
			elsif(ligne_.type_doc='TR')then
				if(mouvement_='S') then
					operation_='Reconditionnement';
				else
					select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot from yvs_com_contenu_doc_stock where id = idexterne_;
					operation_='Reconditionnement';
				end if;
			else
				if(mouvement_='S') then
					operation_='Sortie';
				else
					operation_='Entrée';
				end if;
			end if;	
		WHEN 'yvs_com_reconditionnement' THEN	
			if(mouvement_='S') then
				select into ligne_ qualite as qualite, conditionnement as conditionnement, lot as lot from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Reconditionnement';
			else
				select into ligne_ qualite_entree as qualite, conditionnement_entree as conditionnement, lot_entree as lot from yvs_com_contenu_doc_stock where id = idexterne_;
				operation_='Reconditionnement';
			end if;	
		WHEN 'yvs_prod_flux_composant' THEN	
			select into ligne_ c.unite as conditionnement, null::integer as qualite, null::integer as lot  from yvs_prod_flux_composant y inner join yvs_prod_composant_of c on y.composant = c.id where y.id = idexterne_;
			if(mouvement_='S') then
				operation_='Consommation';
			else
				operation_='Production';
			end if;
		WHEN 'yvs_prod_declaration_production' THEN	
			select into ligne_ conditionnement, null::integer as qualite, null::integer as lot from yvs_prod_declaration_production where id = idexterne_;
			operation_='Production';
		ELSE
			RETURN -1;
	END CASE;
	if(parent_ is not null)then
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, parent_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, parent, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, parent_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		end if;
	else
		if(tranche_ is not null and tranche_ > 0)then
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, tranche, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, tranche_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		else
			INSERT INTO yvs_base_mouvement_stock(quantite, date_doc, mouvement, article, supp, actif, id_externe, table_externe, description, depot, cout_entree, cout_stock, date_mouvement, qualite, conditionnement, lot)
					VALUES (quantite_, date_, mouvement_, article_, false, true, idexterne_, tableexterne_, operation_, depot_, coutentree_, cout_, current_timestamp, ligne_.qualite, ligne_.conditionnement, ligne_.lot);
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date)
  OWNER TO postgres;
COMMENT ON FUNCTION insert_mouvement_stock(bigint, bigint, bigint, double precision, double precision, double precision, bigint, character varying, bigint, character varying, date) IS 'Insert une ligne de sortie de mouvement de stock';
