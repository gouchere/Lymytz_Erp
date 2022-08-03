-- Function: insert_droit_com()

-- DROP FUNCTION insert_droit_com();

CREATE OR REPLACE FUNCTION insert_droit_com()
  RETURNS boolean AS
$BODY$   
DECLARE 
	module_ bigint default 0;
	page_ bigint default 0;
	ressource_  bigint default 0;
BEGIN
	select into module_ id from yvs_module where reference = 'com_';
	if(module_ is not null)then
		select into page_ id from yvs_page_module where reference = 'gescom_fv';
		if(page_ is not null)then
			select into ressource_ id from yvs_ressources_page where reference = 'fv_save_doc';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_save_doc', 'Enregistrer une facture', 'Enregistrer une facture', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_valide_doc';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_valide_doc', 'Valider une facture', 'Valider une facture', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_cancel_doc_valid';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_cancel_doc_valid', 'Annuler une facture', 'Annuler une facture (quand la facture est sous etat ''validé'')', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_delete_doc';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_delete_doc', 'Supprimer une facture', 'Supprimer une facture', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_view_all_doc';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_view_all_doc', 'Voir toutes les factures de la societe', 'Voir toutes les factures de la societe', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_view_only_doc_agence';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_view_only_doc_agence', 'Voir toutes les factures de l''agence', 'Voir toutes les factures de l''agence', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_view_only_doc_pv';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_view_only_doc_pv', 'Voir toutes les factures d''un point de vente', 'Voir toutes les factures d''un point de vente', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_update_header';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_update_header', 'Modifier la date du header', 'Modifier la date du header', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_save_in_past';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_save_in_past', 'Enregistrer une facture au dela de la marge de retard de a date autorisée', 'Enregistrer une facture au dela de la marge de retard de a date autorisée', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_apply_rem_all';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_apply_rem_all', 'Appliquer une remise sur la facture', 'Appliquer une remise sur la facture', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_apply_rabais';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_apply_rabais', 'Appliquer un rabais sur une ligne de vente', 'Appliquer un rabais sur une ligne de vente', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_apply_remise';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_apply_remise', 'Appliquer une remise sur une ligne de vente', 'Appliquer une remise sur une ligne de vente', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_create_client';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_create_client', 'Créer un client', 'Créer un client', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_create_reglement';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_create_reglement', 'Créer des pieces reglements', 'Créer des pieces reglements', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_clean';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_clean', 'Nettoyer les facture sans contenus', 'Nettoyer les facture sans contenus', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_livrer';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_livrer', 'Livrer une facture', 'Livrer une facture', page_);
			end if;
			select into ressource_ id from yvs_ressources_page where reference = 'fv_clean_header';
			if(ressource_ is null)then
				insert into yvs_ressources_page(reference, libelle, description, page_module) values('fv_clean_header', 'Nettoyer Les header sans facture', 'Nettoyer Les header sans facture', page_);
			end if;
		end if;
	end if;
	return true;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_droit_com()
  OWNER TO postgres;
