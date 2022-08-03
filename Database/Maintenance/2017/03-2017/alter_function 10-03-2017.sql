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

	models_ character varying[] := array['MISSIONS','FORMATIONS','CONGES','FACTURE ACHAT','FACTURE VENTE','PERMISSION CD','SORTIE STOCK','OPERATION DIVERS'];
	i integer default 0;

BEGIN
	DROP TABLE IF EXISTS table_workflow;
	CREATE TEMP TABLE IF NOT EXISTS table_workflow(elt character varying, nbr integer, mod character varying);
	if(agence_ is not null and agence_ > 0)then
		for i in 1..8
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
				if(etape_ is not null)then
					-- Si la 1ere etatpe n'existe pas alors on recupere de la 1ere etape en fonction de l'ordre de création
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
				RAISE NOTICE 'etapes %',etapes_;
				IF(model_ = 'MISSIONS')THEN
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
					RAISE NOTICE 'INT %','THIS';
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
				ELSIF(model_ = 'FACTURE ACHAT')THEN
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
				ELSIF(model_ = 'FACTURE VENTE')THEN
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
				ELSIF(model_ = 'PERMISSION CD')THEN
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
				ELSIF(model_ = 'SORTIE STOCK')THEN
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
				ELSIF(model_ = 'OPERATION DIVERS')THEN
					titre_ = 'Opération Diverse en attente';
					for lect_ in select y.id from yvs_compta_caisse_doc_divers y inner join yvs_users_agence e on y.author = e.id where y.statut_doc = 'V' and e.agence = agence_
					loop
						index_ = taille_;
						while(index_ > -1)
						loop
							-- Recherche de l'etape active
							etape_ = etapes_[index_];
							select into continu_ w.etape_valid from yvs_workflow_valid_divers w where w.divers = lect_.id and w.etape = etape_;
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
