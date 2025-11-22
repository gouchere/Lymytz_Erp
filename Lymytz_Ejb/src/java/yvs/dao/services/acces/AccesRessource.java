/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services.acces;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class AccesRessource implements Serializable {

    static AccesRessource instance;

//Module donnees de base base_
    //Page Exercice base_view_exercices
    public boolean base_cloture_exo, base_uncloture_exo, base_uncloture_periode_exo, base_cloture_periode_exo;
    //Page Utilisateur base_user_
    public boolean base_user_vu_, base_user_up_, base_user_del_, base_user_add_, base_view_all_user, base_view_all_user_groupe, base_user_attrib_code_acces, base_user_active,
            caiss_create_piece, base_user_fusion, stock_clean_all_doc_societe, base_conditionnement_update, base_user_change_niveau, base_user_reinitialise_password;
    //Page Niveau Acces base_depot
    public boolean base_depots_save, view_all_depot, stock_view_all_depot, base_depots_update, base_depots_delete, base_depots_add_creneau, change_requiere_lot_article_depot,
            base_depots_lie_depot, base_depots_lie_point, view_all_depot_societe, base_depots_add_article_emplacement, base_depots_add_accessibilite, base_depot_send_all_stock;
    //Page Niveau Acces base_niv_
    public boolean base_niv_vu_, base_niv_up_, base_niv_del_, base_niv_add_;
    //Page Niveau Acces base_tiers
    public boolean base_tiers_save, base_tiers_update, base_tiers_delete, base_tiers_attrib_ration;
    //Page Niveau Acces base_fournisseur
    public boolean base_fournisseur_save, base_fournisseur_update, base_fournisseur_delete;
    //Page Niveau Acces base_client
    public boolean base_client_save, base_client_update, base_client_delete, base_client_print_actif, base_client_change_ristourne, base_client_associer_tarifaire;
    //Page Niveau Acces base_article
    public boolean base_article_save, base_article_update, base_article_delete, base_article_add_depot, base_article_save_tarifaire,
            base_article_defined_comptable, base_article_change_reference, base_view_article_puv, base_view_article_pua, base_view_article_pr;

//Module parametrage param_
    //Page Agence param_agenc_
    public boolean param_agenc_vu_, param_agenc_up_, param_agenc_del_, param_agenc_add_;
    //Page Societe param_societ_
    public boolean param_societ_vu_, param_societ_up_, param_societ_del_, param_societ_add_, param_allowed_adresse_;
    //Page Workflow param_workflow
    public boolean param_warning_view_all;
    //Page caisse
    public boolean caiss_view_all;
//Module ressource humaine grh_
    //Page Employe grh_empl_
    public boolean param_empl_vu_, param_empl_up_, param_empl_del_, param_empl_add_;
    public boolean employe_save_new, employe_active, employe_up_date_embauch, employe_up_convention;
    public boolean grh_view_all_employe;
    public boolean grh_update_contrat,grh_valid_bulletin,grh_cancel_valid_bulletin,grh_compile_bulletin,grh_delete_bulletin;
    //page présence
    public boolean point_viewListEmps, point_viewFormMode, point_changeDateForm, point_savePointageInPast, point_savePointageInLongPast, point_validPointage, point_valide_fiche, point_invalide_fiche, point_validFicheAllScte, point_validFicheAgence, point_validFicheDepartement, point_validFicheEquipe,
            point_validPointageHs, point_validPointageHc, point_modifPointage, point_delLinePointage, point_invalidPointage, point_pointageManuel, point_viewRecapPointage, point_update_fiche_presence, point_delete_fiche_presence,
            point_update_hs, point_update_marge, point_print_rappport_periodique, point_print_rapp_hebdo, point_cr_mode_validation, point_del_mode_validation, point_valideMe, point_valid_fiche_onlyHs;

//Page de gestion des missions
    public boolean mission_cancel, mission_cloturer, mission_cr_ordreM, mission_suspendre, mission_cancel_cloturer, mission_cancel_valide, grh_valid_mission,
            mission_set_time, mission_report_date, mission_add_frais_mission, mission_del_mission, mission_del_mission_new, mission_print_report, mission_update_date_mission, mission_param_avance;
//Page de gestion des congés
    //conge_cancel_cloturer, conge_cancel_valide permettent de sortir un ordre de congé de l'etat respectif cloturer et  valider
    public boolean conge_cancel, conge_cloturer, conge_cr_conge, conge_suspendre, conge_cancel_cloturer, conge_cancel_valide, conge_delete_conge_suspendu,
            grh_valid_conge, conge_valid_duree_supX; //droit de vaider les congé de durée supérieur à x(Jour)
//Page de gestion des planning de travails
    public boolean planing_save, planing_update_tranche, planing_delete_emp, planing_active;
//page gestion des bulletins
    public boolean bulletin_save_bul_negatif, grh_contrat_change_statut, grh_bull_add_regle;

    /*Droits de la gestion commerciale*/
    public boolean com_save_hors_limit, gescom_inventaire_print_with_pr, gescom_valide_sortie, gescom_valide_entree, gescom_editer_sortie, gescom_editer_entree,
            gescom_editer_transfert, gescom_inv_update, gescom_inv_editer, gescom_inv_valider, stock_update_qte_recu, gescom_inv_delete;
    public boolean gescom_delete_recond, gescom_valide_recond;
    //Gestion des rations
    public boolean ra_attribuer, ra_activer, ra_suspendre, ra_distribuer, ra_valider_fiche, ra_controle_fiche, ra_save_fiche, ra_annuler_fiche, ra_delete_fiche,
            ra_view_all_societe, ra_view_depot, ra_view_all_historique, ra_view_cloturer, ra_unluck_fiche, ra_change_jr_usine, ra_view_all;
//Gestion des approvisionnements
    public boolean appro_valid_fiche, appro_cancel_fiche, appro_cr_ba_by_fiche, appro_delete_fiche;
//Gestion des achats
    public boolean fa_view_all_doc, fa_view_only_doc_agence, fa_view_only_doc_depot, fa_valide_doc, fa_editer_doc,
            fa_apply_remise, fa_update_when_receive, fa_update_doc_valid, fa_change_categorie, fa_update_when_paye, fa_view_totaux;
    public boolean bla_view_all_doc, bla_view_only_doc_agence, bla_view_only_doc_depot, bla_valide_doc, bla_editer_doc, fa_clean_all_doc_societe, bla_delete_doc;
    //Gestion des ventes
    public boolean bcv_view_all_doc, bcv_view_only_doc_agence, bcv_view_only_doc_pv, bcv_view_only_doc_depot, bcv_delete_doc;
    public boolean fv_view_all_doc, fv_view_only_doc_pv, fv_view_only_doc_agence, fv_save_doc, fv_valide_doc, fv_cancel_doc_valid, fv_delete_doc,
            fv_apply_rem_all, fv_save_in_past, fv_update_header, fv_apply_rabais, fv_apply_remise, fv_create_client, fv_create_reglement,
            fv_clean, fv_livrer, fv_clean_header, fv_can_reduce_prix, fv_livrer_in_all_depot, fv_super_update_header, fv_view_only_doc_depot,
            fv_save_only_pv, fv_update_doc, fv_change_categorie, fv_clean_all_doc_societe, fv_generer_entree, fv_change_num_doc, fv_can_save_for_other,
            fv_can_save_outside_seuil;
    public boolean brv_delete_doc;
    public boolean fav_delete_doc;
    public boolean bra_delete_doc;
    public boolean faa_delete_doc;
    public boolean gescom_pfv_delete;
    //Gestion des ristourne
    public boolean rist_recaculer_ventes, rist_change_agence;
    public boolean gescom_change_creneau_use, gescom_update_stock_after_valide, gescom_inv_attrib_ecart, gescom_inv_update_valeur, gescom_stock_view_totaux;
    public boolean recalcul_pr, gescom_mouv_delete, gescom_stock_generer_entree, gescom_print_without_transfert;
    //Gestion des bon livraison
    public boolean blv_view_all_doc, blv_view_only_doc_agence, blv_view_only_doc_depot, blv_valide_doc, blv_delete_doc;
    //Gestion des transfert
    public boolean tr_view_all, tr_view_all_user, tr_view_historique, tr_valid_all, tr_change_statut_line, tr_add_content_after_valide, tr_delete_doc;
    //Gestion des Entrée sortie
    public boolean d_stock_view_all_date, d_stock_view_all_agence, d_stock_view_all_depot, d_stock_edit_when_doc_valid, gescom_entree_view_totaux, gescom_sortie_view_totaux, gescom_delete_entree, gescom_delete_sortie;
//Page des opération diverses de caisses
    public boolean d_div_update_doc, d_div_update_doc_valid,
            encais_piece_espece, encais_piece_salaire, encais_piece_cheque, encais_piece_comp;

//Page de gestion des pièces de caisses
    public boolean p_caiss_view_all, p_caiss_view_all_societe, p_caiss_payer_acompte,
            p_caiss_view_all_limit_time, compta_del_reg_cout_cheque, compta_annule_lettrage_not_equilib,compta_change_compte_content,
            p_caiss_view_all_user, p_caiss_payer, p_caiss_cancel_already_pay, compta_reg_fv_change_caissier,
            p_caiss_delete_payer, compta_del_reg_retenue, compta_od_save_all_type, compta_od_view_all_type;

    public boolean compta_virement_up_users_source, compta_virement_view_historique, compta_virement_view_all_users, compta_virement_view_all_caisse,
            compta_extourne_cheque, compta_virement_cancel_pass;

// Points de vente
    public boolean pv_view_all_societe, pv_download_article_with_pr, pv_load_pr_article, prod_gammes_update_num_ope;

    /*Comptabilité*/
    public boolean compta_cancel_piece_valide, compta_od_statut_valid_to_edit, compta_od_statut_encours_to_edit, compta_od_annul_comptabilite,
            compta_cheq_reg_vente_annul_comptabilite, compta_od_valid_max_seuil_montant, compta_od_change_nature,
            compta_od_view_all, compta_od_view_all_agence, compta_od_valide_recette, compta_od_valide_depense, //pour l'annulation du statut validé d'une pièce de caisse
            compta_justif_bp, compta_injustifier_bp, compta_valid_bp, compta_valid_all_bp, compta_valid_bp_under_max,
            compta_ope_client_change_agence_equilibre, compta_ope_fsseur_change_agence_equilibre;

    /**
     * Production
     */
    public boolean prod_launch_of, prod_update_of_encours, prod_valid_declaration_prod, prod_change_depot_declaration, prod_gamme_load_all, prod_nomenc_load_all,
            production_view_all_date, production_view_all_site, production_view_all_of, production_cloture_of, prod_view_all_site, prod_view_all_equipe, production_view_all_societe,
            prod_update_all_of, prod_force_declaration, prod_view_session_all_user, prod_view_session_all_date, prod_acces_raccourcis, prod_update_ppte_composant, prod_create_other_session,
            prod_commande_save, prod_commande_update, prod_commande_delete, production_view_all_user;

    /**
     * Mutuelle
     */
    public boolean mut_atidate_epargne, mut_antidate_epargne_out_periode;

    /**
     * Mutuelle
     */
    public boolean ret_annuler_when_payer, grh_regl_sal_fusion, ret_generer_by_od, ret_generer_by_ecart, ret_marque_regle_prelevement, ret_suspendre_prelevement, ret_delete_prelevement;

    public boolean journaux_view_all, journaux_view_only_agence;

    //Projet proj_
    public boolean proj_departement_save, proj_departement_update, proj_departement_delete, proj_projet_view, proj_projet_save, proj_projet_update, proj_projet_delete, proj_projet_add_service;

    public boolean isBase_uncloture_periode_exo() {
        return base_uncloture_periode_exo;
    }

    public void setBase_uncloture_periode_exo(boolean base_uncloture_periode_exo) {
        this.base_uncloture_periode_exo = base_uncloture_periode_exo;
    }

    public boolean isBase_cloture_periode_exo() {
        return base_cloture_periode_exo;
    }

    public void setBase_cloture_periode_exo(boolean base_cloture_periode_exo) {
        this.base_cloture_periode_exo = base_cloture_periode_exo;
    }

    public boolean isBase_user_change_niveau() {
        return base_user_change_niveau;
    }

    public void setBase_user_change_niveau(boolean base_user_change_niveau) {
        this.base_user_change_niveau = base_user_change_niveau;
    }

    public boolean isBase_user_reinitialise_password() {
        return base_user_reinitialise_password;
    }

    public void setBase_user_reinitialise_password(boolean base_user_reinitialise_password) {
        this.base_user_reinitialise_password = base_user_reinitialise_password;
    }

    public boolean isCompta_ope_client_change_agence_equilibre() {
        return compta_ope_client_change_agence_equilibre;
    }

    public void setCompta_ope_client_change_agence_equilibre(boolean compta_ope_client_change_agence_equilibre) {
        this.compta_ope_client_change_agence_equilibre = compta_ope_client_change_agence_equilibre;
    }

    public boolean isCompta_ope_fsseur_change_agence_equilibre() {
        return compta_ope_fsseur_change_agence_equilibre;
    }

    public void setCompta_ope_fsseur_change_agence_equilibre(boolean compta_ope_fsseur_change_agence_equilibre) {
        this.compta_ope_fsseur_change_agence_equilibre = compta_ope_fsseur_change_agence_equilibre;
    }

    public boolean isBase_user_attrib_code_acces() {
        return base_user_attrib_code_acces;
    }

    public void setBase_user_attrib_code_acces(boolean base_user_attrib_code_acces) {
        this.base_user_attrib_code_acces = base_user_attrib_code_acces;
    }

    public boolean isBase_user_active() {
        return base_user_active;
    }

    public void setBase_user_active(boolean base_user_active) {
        this.base_user_active = base_user_active;
    }

    public boolean isBase_tiers_attrib_ration() {
        return base_tiers_attrib_ration;
    }

    public void setBase_tiers_attrib_ration(boolean base_tiers_attrib_ration) {
        this.base_tiers_attrib_ration = base_tiers_attrib_ration;
    }

    public boolean isGrh_contrat_change_statut() {
        return grh_contrat_change_statut;
    }

    public void setGrh_contrat_change_statut(boolean grh_contrat_change_statut) {
        this.grh_contrat_change_statut = grh_contrat_change_statut;
    }

    public boolean isGrh_bull_add_regle() {
        return grh_bull_add_regle;
    }

    public void setGrh_bull_add_regle(boolean grh_bull_add_regle) {
        this.grh_bull_add_regle = grh_bull_add_regle;
    }

    public boolean isGescom_inv_delete() {
        return gescom_inv_delete;
    }

    public void setGescom_inv_delete(boolean gescom_inv_delete) {
        this.gescom_inv_delete = gescom_inv_delete;
    }

    public boolean isGescom_delete_recond() {
        return gescom_delete_recond;
    }

    public void setGescom_delete_recond(boolean gescom_delete_recond) {
        this.gescom_delete_recond = gescom_delete_recond;
    }

    public boolean isAppro_delete_fiche() {
        return appro_delete_fiche;
    }

    public void setAppro_delete_fiche(boolean appro_delete_fiche) {
        this.appro_delete_fiche = appro_delete_fiche;
    }

    public boolean isBla_delete_doc() {
        return bla_delete_doc;
    }

    public void setBla_delete_doc(boolean bla_delete_doc) {
        this.bla_delete_doc = bla_delete_doc;
    }

    public boolean isBcv_delete_doc() {
        return bcv_delete_doc;
    }

    public void setBcv_delete_doc(boolean bcv_delete_doc) {
        this.bcv_delete_doc = bcv_delete_doc;
    }

    public boolean isBrv_delete_doc() {
        return brv_delete_doc;
    }

    public void setBrv_delete_doc(boolean brv_delete_doc) {
        this.brv_delete_doc = brv_delete_doc;
    }

    public boolean isFav_delete_doc() {
        return fav_delete_doc;
    }

    public void setFav_delete_doc(boolean fav_delete_doc) {
        this.fav_delete_doc = fav_delete_doc;
    }

    public boolean isBra_delete_doc() {
        return bra_delete_doc;
    }

    public void setBra_delete_doc(boolean bra_delete_doc) {
        this.bra_delete_doc = bra_delete_doc;
    }

    public boolean isFaa_delete_doc() {
        return faa_delete_doc;
    }

    public void setFaa_delete_doc(boolean faa_delete_doc) {
        this.faa_delete_doc = faa_delete_doc;
    }

    public boolean isGescom_pfv_delete() {
        return gescom_pfv_delete;
    }

    public void setGescom_pfv_delete(boolean gescom_pfv_delete) {
        this.gescom_pfv_delete = gescom_pfv_delete;
    }

    public boolean isBlv_delete_doc() {
        return blv_delete_doc;
    }

    public void setBlv_delete_doc(boolean blv_delete_doc) {
        this.blv_delete_doc = blv_delete_doc;
    }

    public boolean isTr_delete_doc() {
        return tr_delete_doc;
    }

    public void setTr_delete_doc(boolean tr_delete_doc) {
        this.tr_delete_doc = tr_delete_doc;
    }

    public boolean isGescom_delete_entree() {
        return gescom_delete_entree;
    }

    public void setGescom_delete_entree(boolean gescom_delete_entree) {
        this.gescom_delete_entree = gescom_delete_entree;
    }

    public boolean isGescom_delete_sortie() {
        return gescom_delete_sortie;
    }

    public void setGescom_delete_sortie(boolean gescom_delete_sortie) {
        this.gescom_delete_sortie = gescom_delete_sortie;
    }

    public boolean isGescom_stock_view_totaux() {
        return gescom_stock_view_totaux;
    }

    public void setGescom_stock_view_totaux(boolean gescom_stock_view_totaux) {
        this.gescom_stock_view_totaux = gescom_stock_view_totaux;
    }

    public boolean isChange_requiere_lot_article_depot() {
        return change_requiere_lot_article_depot;
    }

    public void setChange_requiere_lot_article_depot(boolean change_requiere_lot_article_depot) {
        this.change_requiere_lot_article_depot = change_requiere_lot_article_depot;
    }

    public boolean isBase_client_print_actif() {
        return base_client_print_actif;
    }

    public void setBase_client_print_actif(boolean base_client_print_actif) {
        this.base_client_print_actif = base_client_print_actif;
    }

    public boolean isRa_view_all() {
        return ra_view_all;
    }

    public void setRa_view_all(boolean ra_view_all) {
        this.ra_view_all = ra_view_all;
    }

    public boolean isBase_view_article_puv() {
        return base_view_article_puv;
    }

    public void setBase_view_article_puv(boolean base_view_article_puv) {
        this.base_view_article_puv = base_view_article_puv;
    }

    public boolean isBase_view_article_pua() {
        return base_view_article_pua;
    }

    public void setBase_view_article_pua(boolean base_view_article_pua) {
        this.base_view_article_pua = base_view_article_pua;
    }

    public boolean isBase_view_article_pr() {
        return base_view_article_pr;
    }

    public void setBase_view_article_pr(boolean base_view_article_pr) {
        this.base_view_article_pr = base_view_article_pr;
    }

    public boolean isGescom_entree_view_totaux() {
        return gescom_entree_view_totaux;
    }

    public void setGescom_entree_view_totaux(boolean gescom_entree_view_totaux) {
        this.gescom_entree_view_totaux = gescom_entree_view_totaux;
    }

    public boolean isGescom_sortie_view_totaux() {
        return gescom_sortie_view_totaux;
    }

    public void setGescom_sortie_view_totaux(boolean gescom_sortie_view_totaux) {
        this.gescom_sortie_view_totaux = gescom_sortie_view_totaux;
    }

    public boolean isFa_view_totaux() {
        return fa_view_totaux;
    }

    public void setFa_view_totaux(boolean fa_view_totaux) {
        this.fa_view_totaux = fa_view_totaux;
    }

    public boolean isProd_gammes_update_num_ope() {
        return prod_gammes_update_num_ope;
    }

    public void setProd_gammes_update_num_ope(boolean prod_gammes_update_num_ope) {
        this.prod_gammes_update_num_ope = prod_gammes_update_num_ope;
    }

    public boolean isBase_conditionnement_update() {
        return base_conditionnement_update;
    }

    public void setBase_conditionnement_update(boolean base_conditionnement_update) {
        this.base_conditionnement_update = base_conditionnement_update;
    }

    public boolean isProd_commande_save() {
        return prod_commande_save;
    }

    public void setProd_commande_save(boolean prod_commande_save) {
        this.prod_commande_save = prod_commande_save;
    }

    public boolean isProd_commande_update() {
        return prod_commande_update;
    }

    public void setProd_commande_update(boolean prod_commande_update) {
        this.prod_commande_update = prod_commande_update;
    }

    public boolean isProd_commande_delete() {
        return prod_commande_delete;
    }

    public void setProd_commande_delete(boolean prod_commande_delete) {
        this.prod_commande_delete = prod_commande_delete;
    }

    public boolean isRist_change_agence() {
        return rist_change_agence;
    }

    public void setRist_change_agence(boolean rist_change_agence) {
        this.rist_change_agence = rist_change_agence;
    }

    public boolean isTr_add_content_after_valide() {
        return tr_add_content_after_valide;
    }

    public void setTr_add_content_after_valide(boolean tr_add_content_after_valide) {
        this.tr_add_content_after_valide = tr_add_content_after_valide;
    }

    public boolean isGescom_inv_update_valeur() {
        return gescom_inv_update_valeur;
    }

    public void setGescom_inv_update_valeur(boolean gescom_inv_update_valeur) {
        this.gescom_inv_update_valeur = gescom_inv_update_valeur;
    }

    public boolean isGescom_inv_editer() {
        return gescom_inv_editer;
    }

    public void setGescom_inv_editer(boolean gescom_inv_editer) {
        this.gescom_inv_editer = gescom_inv_editer;
    }

    public boolean isGescom_inv_valider() {
        return gescom_inv_valider;
    }

    public void setGescom_inv_valider(boolean gescom_inv_valider) {
        this.gescom_inv_valider = gescom_inv_valider;
    }

    public boolean isGescom_inv_attrib_ecart() {
        return gescom_inv_attrib_ecart;
    }

    public void setGescom_inv_attrib_ecart(boolean gescom_inv_attrib_ecart) {
        this.gescom_inv_attrib_ecart = gescom_inv_attrib_ecart;
    }

    public boolean isRet_generer_by_od() {
        return ret_generer_by_od;
    }

    public void setRet_generer_by_od(boolean ret_generer_by_od) {
        this.ret_generer_by_od = ret_generer_by_od;
    }

    public boolean isRet_marque_regle_prelevement() {
        return ret_marque_regle_prelevement;
    }

    public void setRet_marque_regle_prelevement(boolean ret_marque_regle_prelevement) {
        this.ret_marque_regle_prelevement = ret_marque_regle_prelevement;
    }

    public boolean isRet_suspendre_prelevement() {
        return ret_suspendre_prelevement;
    }

    public void setRet_suspendre_prelevement(boolean ret_suspendre_prelevement) {
        this.ret_suspendre_prelevement = ret_suspendre_prelevement;
    }

    public boolean isRet_delete_prelevement() {
        return ret_delete_prelevement;
    }

    public void setRet_delete_prelevement(boolean ret_delete_prelevement) {
        this.ret_delete_prelevement = ret_delete_prelevement;
    }

    public boolean isRet_generer_by_ecart() {
        return ret_generer_by_ecart;
    }

    public void setRet_generer_by_ecart(boolean ret_generer_by_ecart) {
        this.ret_generer_by_ecart = ret_generer_by_ecart;
    }

    public boolean isGescom_update_stock_after_valide() {
        return gescom_update_stock_after_valide;
    }

    public void setGescom_update_stock_after_valide(boolean gescom_update_stock_after_valide) {
        this.gescom_update_stock_after_valide = gescom_update_stock_after_valide;
    }

    public boolean isParam_warning_view_all() {
        return param_warning_view_all;
    }

    public void setParam_warning_view_all(boolean param_warning_view_all) {
        this.param_warning_view_all = param_warning_view_all;
    }

    public boolean isCompta_change_compte_content() {
        return compta_change_compte_content;
    }

    public void setCompta_change_compte_content(boolean compta_change_compte_content) {
        this.compta_change_compte_content = compta_change_compte_content;
    }

    public boolean isCompta_annule_lettrage_not_equilib() {
        return compta_annule_lettrage_not_equilib;
    }

    public void setCompta_annule_lettrage_not_equilib(boolean compta_annule_lettrage_not_equilib) {
        this.compta_annule_lettrage_not_equilib = compta_annule_lettrage_not_equilib;
    }

    public boolean isBcv_view_all_doc() {
        return bcv_view_all_doc;
    }

    public void setBcv_view_all_doc(boolean bcv_view_all_doc) {
        this.bcv_view_all_doc = bcv_view_all_doc;
    }

    public boolean isBcv_view_only_doc_agence() {
        return bcv_view_only_doc_agence;
    }

    public void setBcv_view_only_doc_agence(boolean bcv_view_only_doc_agence) {
        this.bcv_view_only_doc_agence = bcv_view_only_doc_agence;
    }

    public boolean isBcv_view_only_doc_pv() {
        return bcv_view_only_doc_pv;
    }

    public void setBcv_view_only_doc_pv(boolean bcv_view_only_doc_pv) {
        this.bcv_view_only_doc_pv = bcv_view_only_doc_pv;
    }

    public boolean isBcv_view_only_doc_depot() {
        return bcv_view_only_doc_depot;
    }

    public void setBcv_view_only_doc_depot(boolean bcv_view_only_doc_depot) {
        this.bcv_view_only_doc_depot = bcv_view_only_doc_depot;
    }

    public boolean isCompta_virement_cancel_pass() {
        return compta_virement_cancel_pass;
    }

    public void setCompta_virement_cancel_pass(boolean compta_virement_cancel_pass) {
        this.compta_virement_cancel_pass = compta_virement_cancel_pass;
    }

    public boolean isGescom_print_without_transfert() {
        return gescom_print_without_transfert;
    }

    public void setGescom_print_without_transfert(boolean gescom_print_without_transfert) {
        this.gescom_print_without_transfert = gescom_print_without_transfert;
    }

    public boolean isGrh_regl_sal_fusion() {
        return grh_regl_sal_fusion;
    }

    public void setGrh_regl_sal_fusion(boolean grh_regl_sal_fusion) {
        this.grh_regl_sal_fusion = grh_regl_sal_fusion;
    }

    public boolean isRet_annuler_when_payer() {
        return ret_annuler_when_payer;
    }

    public void setRet_annuler_when_payer(boolean ret_annuler_when_payer) {
        this.ret_annuler_when_payer = ret_annuler_when_payer;
    }

    public boolean isP_caiss_view_all_societe() {
        return p_caiss_view_all_societe;
    }

    public void setP_caiss_view_all_societe(boolean p_caiss_view_all_societe) {
        this.p_caiss_view_all_societe = p_caiss_view_all_societe;
    }

    public boolean isBase_depot_send_all_stock() {
        return base_depot_send_all_stock;
    }

    public void setBase_depot_send_all_stock(boolean base_depot_send_all_stock) {
        this.base_depot_send_all_stock = base_depot_send_all_stock;
    }

    public boolean isBase_depots_add_creneau() {
        return base_depots_add_creneau;
    }

    public void setBase_depots_add_creneau(boolean base_depots_add_creneau) {
        this.base_depots_add_creneau = base_depots_add_creneau;
    }

    public boolean isBase_depots_add_accessibilite() {
        return base_depots_add_accessibilite;
    }

    public void setBase_depots_add_accessibilite(boolean base_depots_add_accessibilite) {
        this.base_depots_add_accessibilite = base_depots_add_accessibilite;
    }

    public boolean isBase_depots_add_article_emplacement() {
        return base_depots_add_article_emplacement;
    }

    public void setBase_depots_add_article_emplacement(boolean base_depots_add_article_emplacement) {
        this.base_depots_add_article_emplacement = base_depots_add_article_emplacement;
    }

    public boolean isStock_clean_all_doc_societe() {
        return stock_clean_all_doc_societe;
    }

    public void setStock_clean_all_doc_societe(boolean stock_clean_all_doc_societe) {
        this.stock_clean_all_doc_societe = stock_clean_all_doc_societe;
    }

    public boolean isBase_user_fusion() {
        return base_user_fusion;
    }

    public void setBase_user_fusion(boolean base_user_fusion) {
        this.base_user_fusion = base_user_fusion;
    }

    public boolean isFa_update_when_paye() {
        return fa_update_when_paye;
    }

    public void setFa_update_when_paye(boolean fa_update_when_paye) {
        this.fa_update_when_paye = fa_update_when_paye;
    }

    public boolean isGescom_change_creneau_use() {
        return gescom_change_creneau_use;
    }

    public void setGescom_change_creneau_use(boolean gescom_change_creneau_use) {
        this.gescom_change_creneau_use = gescom_change_creneau_use;
    }

    public boolean isGescom_stock_generer_entree() {
        return gescom_stock_generer_entree;
    }

    public void setGescom_stock_generer_entree(boolean gescom_stock_generer_entree) {
        this.gescom_stock_generer_entree = gescom_stock_generer_entree;
    }

    public boolean isGescom_mouv_delete() {
        return gescom_mouv_delete;
    }

    public void setGescom_mouv_delete(boolean gescom_mouv_delete) {
        this.gescom_mouv_delete = gescom_mouv_delete;
    }

    public boolean isRecalcul_pr() {
        return recalcul_pr;
    }

    public void setRecalcul_pr(boolean recalcul_pr) {
        this.recalcul_pr = recalcul_pr;
    }

    public boolean isRist_recaculer_ventes() {
        return rist_recaculer_ventes;
    }

    public void setRist_recaculer_ventes(boolean rist_recaculer_ventes) {
        this.rist_recaculer_ventes = rist_recaculer_ventes;
    }

    public boolean isRa_change_jr_usine() {
        return ra_change_jr_usine;
    }

    public void setRa_change_jr_usine(boolean ra_change_jr_usine) {
        this.ra_change_jr_usine = ra_change_jr_usine;
    }

    public boolean isBla_editer_doc() {
        return bla_editer_doc;
    }

    public void setBla_editer_doc(boolean bla_editer_doc) {
        this.bla_editer_doc = bla_editer_doc;
    }

    public boolean isCompta_virement_view_all_users() {
        return compta_virement_view_all_users;
    }

    public void setCompta_virement_view_all_users(boolean compta_virement_view_all_users) {
        this.compta_virement_view_all_users = compta_virement_view_all_users;
    }

    public boolean isCompta_virement_view_all_caisse() {
        return compta_virement_view_all_caisse;
    }

    public void setCompta_virement_view_all_caisse(boolean compta_virement_view_all_caisse) {
        this.compta_virement_view_all_caisse = compta_virement_view_all_caisse;
    }

    public boolean isCompta_reg_fv_change_caissier() {
        return compta_reg_fv_change_caissier;
    }

    public void setCompta_reg_fv_change_caissier(boolean compta_reg_fv_change_caissier) {
        this.compta_reg_fv_change_caissier = compta_reg_fv_change_caissier;
    }

    public boolean isGescom_editer_transfert() {
        return gescom_editer_transfert;
    }

    public void setGescom_editer_transfert(boolean gescom_editer_transfert) {
        this.gescom_editer_transfert = gescom_editer_transfert;
    }

    public boolean isGescom_editer_sortie() {
        return gescom_editer_sortie;
    }

    public void setGescom_editer_sortie(boolean gescom_editer_sortie) {
        this.gescom_editer_sortie = gescom_editer_sortie;
    }

    public boolean isGescom_editer_entree() {
        return gescom_editer_entree;
    }

    public void setGescom_editer_entree(boolean gescom_editer_entree) {
        this.gescom_editer_entree = gescom_editer_entree;
    }

    public boolean isFa_editer_doc() {
        return fa_editer_doc;
    }

    public void setFa_editer_doc(boolean fa_editer_doc) {
        this.fa_editer_doc = fa_editer_doc;
    }

    public boolean isGescom_valide_sortie() {
        return gescom_valide_sortie;
    }

    public void setGescom_valide_sortie(boolean gescom_valide_sortie) {
        this.gescom_valide_sortie = gescom_valide_sortie;
    }

    public boolean isGescom_valide_entree() {
        return gescom_valide_entree;
    }

    public void setGescom_valide_entree(boolean gescom_valide_entree) {
        this.gescom_valide_entree = gescom_valide_entree;
    }

    public boolean isGescom_valide_recond() {
        return gescom_valide_recond;
    }

    public void setGescom_valide_recond(boolean gescom_valide_recond) {
        this.gescom_valide_recond = gescom_valide_recond;
    }

    public boolean isCompta_od_valide_recette() {
        return compta_od_valide_recette;
    }

    public void setCompta_od_valide_recette(boolean compta_od_valide_recette) {
        this.compta_od_valide_recette = compta_od_valide_recette;
    }

    public boolean isCompta_od_valide_depense() {
        return compta_od_valide_depense;
    }

    public void setCompta_od_valide_depense(boolean compta_od_valide_depense) {
        this.compta_od_valide_depense = compta_od_valide_depense;
    }

    public boolean isCompta_od_change_nature() {
        return compta_od_change_nature;
    }

    public void setCompta_od_change_nature(boolean compta_od_change_nature) {
        this.compta_od_change_nature = compta_od_change_nature;
    }

    public boolean isCompta_virement_view_historique() {
        return compta_virement_view_historique;
    }

    public void setCompta_virement_view_historique(boolean compta_virement_view_historique) {
        this.compta_virement_view_historique = compta_virement_view_historique;
    }

    public boolean isCompta_virement_up_users_source() {
        return compta_virement_up_users_source;
    }

    public void setCompta_virement_up_users_source(boolean compta_virement_up_users_source) {
        this.compta_virement_up_users_source = compta_virement_up_users_source;
    }

    public boolean isProduction_view_all_societe() {
        return production_view_all_societe;
    }

    public void setProduction_view_all_societe(boolean production_view_all_societe) {
        this.production_view_all_societe = production_view_all_societe;
    }

    public boolean isBla_valide_doc() {
        return bla_valide_doc;
    }

    public void setBla_valide_doc(boolean bla_valide_doc) {
        this.bla_valide_doc = bla_valide_doc;
    }

    public boolean isBlv_valide_doc() {
        return blv_valide_doc;
    }

    public void setBlv_valide_doc(boolean blv_valide_doc) {
        this.blv_valide_doc = blv_valide_doc;
    }

    public boolean isFa_valide_doc() {
        return fa_valide_doc;
    }

    public void setFa_valide_doc(boolean fa_valide_doc) {
        this.fa_valide_doc = fa_valide_doc;
    }

    public boolean isGescom_inv_update() {
        return gescom_inv_update;
    }

    public void setGescom_inv_update(boolean gescom_inv_update) {
        this.gescom_inv_update = gescom_inv_update;
    }

    public boolean isGescom_inventaire_print_with_pr() {
        return gescom_inventaire_print_with_pr;
    }

    public void setGescom_inventaire_print_with_pr(boolean gescom_inventaire_print_with_pr) {
        this.gescom_inventaire_print_with_pr = gescom_inventaire_print_with_pr;
    }

    public boolean isBase_tiers_save() {
        return base_tiers_save;
    }

    public void setBase_tiers_save(boolean base_tiers_save) {
        this.base_tiers_save = base_tiers_save;
    }

    public boolean isBase_tiers_update() {
        return base_tiers_update;
    }

    public void setBase_tiers_update(boolean base_tiers_update) {
        this.base_tiers_update = base_tiers_update;
    }

    public boolean isBase_tiers_delete() {
        return base_tiers_delete;
    }

    public void setBase_tiers_delete(boolean base_tiers_delete) {
        this.base_tiers_delete = base_tiers_delete;
    }

    public boolean isBase_fournisseur_save() {
        return base_fournisseur_save;
    }

    public void setBase_fournisseur_save(boolean base_fournisseur_save) {
        this.base_fournisseur_save = base_fournisseur_save;
    }

    public boolean isBase_fournisseur_update() {
        return base_fournisseur_update;
    }

    public void setBase_fournisseur_update(boolean base_fournisseur_update) {
        this.base_fournisseur_update = base_fournisseur_update;
    }

    public boolean isBase_fournisseur_delete() {
        return base_fournisseur_delete;
    }

    public void setBase_fournisseur_delete(boolean base_fournisseur_delete) {
        this.base_fournisseur_delete = base_fournisseur_delete;
    }

    public boolean isBase_client_save() {
        return base_client_save;
    }

    public void setBase_client_save(boolean base_client_save) {
        this.base_client_save = base_client_save;
    }

    public boolean isBase_client_update() {
        return base_client_update;
    }

    public void setBase_client_update(boolean base_client_update) {
        this.base_client_update = base_client_update;
    }

    public boolean isBase_client_delete() {
        return base_client_delete;
    }

    public void setBase_client_delete(boolean base_client_delete) {
        this.base_client_delete = base_client_delete;
    }

    public boolean isBase_client_change_ristourne() {
        return base_client_change_ristourne;
    }

    public void setBase_client_change_ristourne(boolean base_client_change_ristourne) {
        this.base_client_change_ristourne = base_client_change_ristourne;
    }

    public boolean isBase_client_associer_tarifaire() {
        return base_client_associer_tarifaire;
    }

    public void setBase_client_associer_tarifaire(boolean base_client_associer_tarifaire) {
        this.base_client_associer_tarifaire = base_client_associer_tarifaire;
    }

    public boolean isBase_depots_save() {
        return base_depots_save;
    }

    public void setBase_depots_save(boolean base_depots_save) {
        this.base_depots_save = base_depots_save;
    }

    public boolean isBase_depots_update() {
        return base_depots_update;
    }

    public void setBase_depots_update(boolean base_depots_update) {
        this.base_depots_update = base_depots_update;
    }

    public boolean isBase_depots_delete() {
        return base_depots_delete;
    }

    public void setBase_depots_delete(boolean base_depots_delete) {
        this.base_depots_delete = base_depots_delete;
    }

    public boolean isBase_depots_lie_depot() {
        return base_depots_lie_depot;
    }

    public void setBase_depots_lie_depot(boolean base_depots_lie_depot) {
        this.base_depots_lie_depot = base_depots_lie_depot;
    }

    public boolean isBase_depots_lie_point() {
        return base_depots_lie_point;
    }

    public void setBase_depots_lie_point(boolean base_depots_lie_point) {
        this.base_depots_lie_point = base_depots_lie_point;
    }

    public boolean isBase_article_change_reference() {
        return base_article_change_reference;
    }

    public void setBase_article_change_reference(boolean base_article_change_reference) {
        this.base_article_change_reference = base_article_change_reference;
    }

    public boolean isBase_article_save() {
        return base_article_save;
    }

    public void setBase_article_save(boolean base_article_save) {
        this.base_article_save = base_article_save;
    }

    public boolean isBase_article_update() {
        return base_article_update;
    }

    public void setBase_article_update(boolean base_article_update) {
        this.base_article_update = base_article_update;
    }

    public boolean isBase_article_delete() {
        return base_article_delete;
    }

    public void setBase_article_delete(boolean base_article_delete) {
        this.base_article_delete = base_article_delete;
    }

    public boolean isBase_article_add_depot() {
        return base_article_add_depot;
    }

    public void setBase_article_add_depot(boolean base_article_add_depot) {
        this.base_article_add_depot = base_article_add_depot;
    }

    public boolean isBase_article_save_tarifaire() {
        return base_article_save_tarifaire;
    }

    public void setBase_article_save_tarifaire(boolean base_article_save_tarifaire) {
        this.base_article_save_tarifaire = base_article_save_tarifaire;
    }

    public boolean isBase_article_defined_comptable() {
        return base_article_defined_comptable;
    }

    public void setBase_article_defined_comptable(boolean base_article_defined_comptable) {
        this.base_article_defined_comptable = base_article_defined_comptable;
    }

    public boolean isCompta_od_view_all() {
        return compta_od_view_all;
    }

    public void setCompta_od_view_all(boolean compta_od_view_all) {
        this.compta_od_view_all = compta_od_view_all;
    }

    public boolean isCaiss_create_piece() {
        return caiss_create_piece;
    }

    public boolean isBase_cloture_exo() {
        return base_cloture_exo;
    }

    public void setBase_cloture_exo(boolean base_cloture_exo) {
        this.base_cloture_exo = base_cloture_exo;
    }

    public boolean isBase_uncloture_exo() {
        return base_uncloture_exo;
    }

    public void setBase_uncloture_exo(boolean base_uncloture_exo) {
        this.base_uncloture_exo = base_uncloture_exo;
    }

    public boolean isCompta_del_reg_cout_cheque() {
        return compta_del_reg_cout_cheque;
    }

    public void setCompta_del_reg_cout_cheque(boolean compta_del_reg_cout_cheque) {
        this.compta_del_reg_cout_cheque = compta_del_reg_cout_cheque;
    }

    public boolean isCom_save_hors_limit() {
        return com_save_hors_limit;
    }

    public void setCom_save_hors_limit(boolean com_save_hors_limit) {
        this.com_save_hors_limit = com_save_hors_limit;
    }

    public boolean isP_caiss_payer_acompte() {
        return p_caiss_payer_acompte;
    }

    public void setP_caiss_payer_acompte(boolean p_caiss_payer_acompte) {
        this.p_caiss_payer_acompte = p_caiss_payer_acompte;
    }

    public boolean isFv_generer_entree() {
        return fv_generer_entree;
    }

    public void setFv_generer_entree(boolean fv_generer_entree) {
        this.fv_generer_entree = fv_generer_entree;
    }

    public boolean isFv_change_categorie() {
        return fv_change_categorie;
    }

    public void setFv_change_categorie(boolean fv_change_categorie) {
        this.fv_change_categorie = fv_change_categorie;
    }

    public boolean isCompta_od_valid_max_seuil_montant() {
        return compta_od_valid_max_seuil_montant;
    }

    public void setCompta_od_valid_max_seuil_montant(boolean compta_od_valid_max_seuil_montant) {
        this.compta_od_valid_max_seuil_montant = compta_od_valid_max_seuil_montant;
    }

    public boolean isFa_change_categorie() {
        return fa_change_categorie;
    }

    public void setFa_change_categorie(boolean fa_change_categorie) {
        this.fa_change_categorie = fa_change_categorie;
    }

    public boolean isCompta_cheq_reg_vente_annul_comptabilite() {
        return compta_cheq_reg_vente_annul_comptabilite;
    }

    public void setCompta_cheq_reg_vente_annul_comptabilite(boolean compta_cheq_reg_vente_annul_comptabilite) {
        this.compta_cheq_reg_vente_annul_comptabilite = compta_cheq_reg_vente_annul_comptabilite;
    }

    public boolean isFa_update_doc_valid() {
        return fa_update_doc_valid;
    }

    public void setFa_update_doc_valid(boolean fa_update_doc_valid) {
        this.fa_update_doc_valid = fa_update_doc_valid;
    }

    public boolean isCompta_od_annul_comptabilite() {
        return compta_od_annul_comptabilite;
    }

    public void setCompta_od_annul_comptabilite(boolean compta_od_annul_comptabilite) {
        this.compta_od_annul_comptabilite = compta_od_annul_comptabilite;
    }

    public boolean isCompta_od_statut_valid_to_edit() {
        return compta_od_statut_valid_to_edit;
    }

    public void setCompta_od_statut_valid_to_edit(boolean compta_od_statut_valid_to_edit) {
        this.compta_od_statut_valid_to_edit = compta_od_statut_valid_to_edit;
    }

    public boolean isCompta_od_statut_encours_to_edit() {
        return compta_od_statut_encours_to_edit;
    }

    public void setCompta_od_statut_encours_to_edit(boolean compta_od_statut_encours_to_edit) {
        this.compta_od_statut_encours_to_edit = compta_od_statut_encours_to_edit;
    }

    public boolean isEmploye_save_new() {
        return employe_save_new;
    }

    public void setEmploye_save_new(boolean employe_save_new) {
        this.employe_save_new = employe_save_new;
    }

    public boolean isEmploye_active() {
        return employe_active;
    }

    public void setEmploye_active(boolean employe_active) {
        this.employe_active = employe_active;
    }

    public boolean isEmploye_up_date_embauch() {
        return employe_up_date_embauch;
    }

    public void setEmploye_up_date_embauch(boolean employe_up_date_embauch) {
        this.employe_up_date_embauch = employe_up_date_embauch;
    }

    public boolean isEmploye_up_convention() {
        return employe_up_convention;
    }

    public void setEmploye_up_convention(boolean employe_up_convention) {
        this.employe_up_convention = employe_up_convention;
    }

    public boolean isGrh_view_all_employe() {
        return grh_view_all_employe;
    }

    public void setGrh_view_all_employe(boolean grh_view_all_employe) {
        this.grh_view_all_employe = grh_view_all_employe;
    }

    public boolean isProd_gamme_load_all() {
        return prod_gamme_load_all;
    }

    public void setProd_gamme_load_all(boolean prod_gamme_load_all) {
        this.prod_gamme_load_all = prod_gamme_load_all;
    }

    public boolean isProd_nomenc_load_all() {
        return prod_nomenc_load_all;
    }

    public void setProd_nomenc_load_all(boolean prod_nomenc_load_all) {
        this.prod_nomenc_load_all = prod_nomenc_load_all;
    }

    public boolean isRa_suspendre() {
        return ra_suspendre;
    }

    public boolean isRa_activer() {
        return ra_activer;
    }

    public void setRa_activer(boolean ra_activer) {
        this.ra_activer = ra_activer;
    }

    public boolean isRa_distribuer() {
        return ra_distribuer;
    }

    public void setRa_distribuer(boolean ra_distribuer) {
        this.ra_distribuer = ra_distribuer;
    }

    public boolean isRa_controle_fiche() {
        return ra_controle_fiche;
    }

    public void setRa_controle_fiche(boolean ra_controle_fiche) {
        this.ra_controle_fiche = ra_controle_fiche;
    }

    public boolean isFa_update_when_receive() {
        return fa_update_when_receive;
    }

    public void setFa_update_when_receive(boolean fa_update_when_receive) {
        this.fa_update_when_receive = fa_update_when_receive;
    }

    public boolean isTr_valid_all() {
        return tr_valid_all;
    }

    public void setTr_valid_all(boolean tr_valid_all) {
        this.tr_valid_all = tr_valid_all;
    }

    public boolean isEncais_piece_comp() {
        return encais_piece_comp;
    }

    public void setEncais_piece_comp(boolean encais_piece_comp) {
        this.encais_piece_comp = encais_piece_comp;
    }

    public boolean isAppro_cr_ba_by_fiche() {
        return appro_cr_ba_by_fiche;
    }

    public void setAppro_cr_ba_by_fiche(boolean appro_cr_ba_by_fiche) {
        this.appro_cr_ba_by_fiche = appro_cr_ba_by_fiche;
    }

    public boolean isEncais_piece_espece() {
        return encais_piece_espece;
    }

    public void setEncais_piece_espece(boolean encais_piece_espece) {
        this.encais_piece_espece = encais_piece_espece;
    }

    public boolean isEncais_piece_salaire() {
        return encais_piece_salaire;
    }

    public void setEncais_piece_salaire(boolean encais_piece_salaire) {
        this.encais_piece_salaire = encais_piece_salaire;
    }

    public boolean isEncais_piece_cheque() {
        return encais_piece_cheque;
    }

    public void setEncais_piece_cheque(boolean encais_piece_cheque) {
        this.encais_piece_cheque = encais_piece_cheque;
    }

    public boolean isRa_attribuer() {
        return ra_attribuer;
    }

    public void setRa_attribuer(boolean ra_attribuer) {
        this.ra_attribuer = ra_attribuer;
    }

    public boolean isRa_valider_fiche() {
        return ra_valider_fiche;
    }

    public void setRa_valider_fiche(boolean ra_valider_fiche) {
        this.ra_valider_fiche = ra_valider_fiche;
    }

    public boolean isRa_save_fiche() {
        return ra_save_fiche;
    }

    public void setRa_save_fiche(boolean ra_save_fiche) {
        this.ra_save_fiche = ra_save_fiche;
    }

    public boolean isRa_annuler_fiche() {
        return ra_annuler_fiche;
    }

    public void setRa_annuler_fiche(boolean ra_annuler_fiche) {
        this.ra_annuler_fiche = ra_annuler_fiche;
    }

    public boolean isRa_delete_fiche() {
        return ra_delete_fiche;
    }

    public void setRa_delete_fiche(boolean ra_delete_fiche) {
        this.ra_delete_fiche = ra_delete_fiche;
    }

    public boolean isAppro_valid_fiche() {
        return appro_valid_fiche;
    }

    public void setAppro_valid_fiche(boolean appro_valid_fiche) {
        this.appro_valid_fiche = appro_valid_fiche;
    }

    public boolean isAppro_cancel_fiche() {
        return appro_cancel_fiche;
    }

    public void setAppro_cancel_fiche(boolean appro_cancel_fiche) {
        this.appro_cancel_fiche = appro_cancel_fiche;
    }

    public boolean isFv_update_doc() {
        return fv_update_doc;
    }

    public void setFv_update_doc(boolean fv_update_doc) {
        this.fv_update_doc = fv_update_doc;
    }

    public boolean isFa_view_all_doc() {
        return fa_view_all_doc;
    }

    public void setFa_view_all_doc(boolean fa_view_all_doc) {
        this.fa_view_all_doc = fa_view_all_doc;
    }

    public boolean isFa_view_only_doc_agence() {
        return fa_view_only_doc_agence;
    }

    public void setFa_view_only_doc_agence(boolean fa_view_only_doc_agence) {
        this.fa_view_only_doc_agence = fa_view_only_doc_agence;
    }

    public boolean isFa_view_only_doc_depot() {
        return fa_view_only_doc_depot;
    }

    public void setFa_view_only_doc_depot(boolean fa_view_only_doc_depot) {
        this.fa_view_only_doc_depot = fa_view_only_doc_depot;
    }

    public boolean isFa_apply_remise() {
        return fa_apply_remise;
    }

    public boolean isBla_view_all_doc() {
        return bla_view_all_doc;
    }

    public boolean isBla_view_only_doc_agence() {
        return bla_view_only_doc_agence;
    }

    public boolean isBla_view_only_doc_depot() {
        return bla_view_only_doc_depot;
    }

    public boolean isFv_save_only_pv() {
        return fv_save_only_pv;
    }

    public void setFv_save_only_pv(boolean fv_save_only_pv) {
        this.fv_save_only_pv = fv_save_only_pv;
    }

    public boolean isFv_view_only_doc_depot() {
        return fv_view_only_doc_depot;
    }

    public void setFv_view_only_doc_depot(boolean fv_view_only_doc_depot) {
        this.fv_view_only_doc_depot = fv_view_only_doc_depot;
    }

    public boolean isBlv_view_all_doc() {
        return blv_view_all_doc;
    }

    public void setBlv_view_all_doc(boolean blv_view_all_doc) {
        this.blv_view_all_doc = blv_view_all_doc;
    }

    public boolean isBlv_view_only_doc_agence() {
        return blv_view_only_doc_agence;
    }

    public void setBlv_view_only_doc_agence(boolean blv_view_only_doc_agence) {
        this.blv_view_only_doc_agence = blv_view_only_doc_agence;
    }

    public boolean isBlv_view_only_doc_depot() {
        return blv_view_only_doc_depot;
    }

    public void setBlv_view_only_doc_depot(boolean blv_view_only_doc_depot) {
        this.blv_view_only_doc_depot = blv_view_only_doc_depot;
    }

    public boolean isFv_super_update_header() {
        return fv_super_update_header;
    }

    public void setFv_super_update_header(boolean fv_super_update_header) {
        this.fv_super_update_header = fv_super_update_header;
    }

    public boolean isFv_apply_rem_all() {
        return fv_apply_rem_all;
    }

    public void setFv_apply_rem_all(boolean fv_apply_rem_all) {
        this.fv_apply_rem_all = fv_apply_rem_all;
    }

    public boolean isFv_save_in_past() {
        return fv_save_in_past;
    }

    public void setFv_save_in_past(boolean fv_save_in_past) {
        this.fv_save_in_past = fv_save_in_past;
    }

    public boolean isFv_update_header() {
        return fv_update_header;
    }

    public void setFv_update_header(boolean fv_update_header) {
        this.fv_update_header = fv_update_header;
    }

    public boolean isFv_apply_rabais() {
        return fv_apply_rabais;
    }

    public void setFv_apply_rabais(boolean fv_apply_rabais) {
        this.fv_apply_rabais = fv_apply_rabais;
    }

    public boolean isFv_apply_remise() {
        return fv_apply_remise;
    }

    public void setFv_apply_remise(boolean fv_apply_remise) {
        this.fv_apply_remise = fv_apply_remise;
    }

    public boolean isFv_create_client() {
        return fv_create_client;
    }

    public void setFv_create_client(boolean fv_create_client) {
        this.fv_create_client = fv_create_client;
    }

    public boolean isFv_create_reglement() {
        return fv_create_reglement;
    }

    public void setFv_create_reglement(boolean fv_create_reglement) {
        this.fv_create_reglement = fv_create_reglement;
    }

    public boolean isFv_clean() {
        return fv_clean;
    }

    public void setFv_clean(boolean fv_clean) {
        this.fv_clean = fv_clean;
    }

    public boolean isFv_livrer() {
        return fv_livrer;
    }

    public void setFv_livrer(boolean fv_livrer) {
        this.fv_livrer = fv_livrer;
    }

    public boolean isFv_clean_header() {
        return fv_clean_header;
    }

    public void setFv_clean_header(boolean fv_clean_header) {
        this.fv_clean_header = fv_clean_header;
    }

    public boolean isFv_can_reduce_prix() {
        return fv_can_reduce_prix;
    }

    public void setFv_can_reduce_prix(boolean fv_can_reduce_prix) {
        this.fv_can_reduce_prix = fv_can_reduce_prix;
    }

    public boolean isFv_save_doc() {
        return fv_save_doc;
    }

    public void setFv_save_doc(boolean fv_save_doc) {
        this.fv_save_doc = fv_save_doc;
    }

    public boolean isFv_valide_doc() {
        return fv_valide_doc;
    }

    public void setFv_valide_doc(boolean fv_valide_doc) {
        this.fv_valide_doc = fv_valide_doc;
    }

    public boolean isFv_cancel_doc_valid() {
        return fv_cancel_doc_valid;
    }

    public void setFv_cancel_doc_valid(boolean fv_cancel_doc_valid) {
        this.fv_cancel_doc_valid = fv_cancel_doc_valid;
    }

    public boolean isFv_delete_doc() {
        return fv_delete_doc;
    }

    public void setFv_delete_doc(boolean fv_delete_doc) {
        this.fv_delete_doc = fv_delete_doc;
    }

    public boolean isBase_user_vu_() {
        return base_user_vu_;
    }

    public void setBase_user_vu_(boolean base_user_vu_) {
        this.base_user_vu_ = base_user_vu_;
    }

    public boolean isBase_user_up_() {
        return base_user_up_;
    }

    public void setBase_user_up_(boolean base_user_up_) {
        this.base_user_up_ = base_user_up_;
    }

    public boolean isBase_user_del_() {
        return base_user_del_;
    }

    public void setBase_user_del_(boolean base_user_del_) {
        this.base_user_del_ = base_user_del_;
    }

    public boolean isBase_user_add_() {
        return base_user_add_;
    }

    public void setBase_user_add_(boolean base_user_add_) {
        this.base_user_add_ = base_user_add_;
    }

    public boolean isBase_niv_vu_() {
        return base_niv_vu_;
    }

    public void setBase_niv_vu_(boolean base_niv_vu_) {
        this.base_niv_vu_ = base_niv_vu_;
    }

    public boolean isBase_niv_up_() {
        return base_niv_up_;
    }

    public void setBase_niv_up_(boolean base_niv_up_) {
        this.base_niv_up_ = base_niv_up_;
    }

    public boolean isBase_niv_del_() {
        return base_niv_del_;
    }

    public void setBase_niv_del_(boolean base_niv_del_) {
        this.base_niv_del_ = base_niv_del_;
    }

    public boolean isBase_niv_add_() {
        return base_niv_add_;
    }

    public void setBase_niv_add_(boolean base_niv_add_) {
        this.base_niv_add_ = base_niv_add_;
    }

    public boolean isParam_agenc_vu_() {
        return param_agenc_vu_;
    }

    public void setParam_agenc_vu_(boolean param_agenc_vu_) {
        this.param_agenc_vu_ = param_agenc_vu_;
    }

    public boolean isParam_agenc_up_() {
        return param_agenc_up_;
    }

    public void setParam_agenc_up_(boolean param_agenc_up_) {
        this.param_agenc_up_ = param_agenc_up_;
    }

    public boolean isParam_agenc_del_() {
        return param_agenc_del_;
    }

    public void setParam_agenc_del_(boolean param_agenc_del_) {
        this.param_agenc_del_ = param_agenc_del_;
    }

    public boolean isParam_agenc_add_() {
        return param_agenc_add_;
    }

    public void setParam_agenc_add_(boolean param_agenc_add_) {
        this.param_agenc_add_ = param_agenc_add_;
    }

    public boolean isParam_societ_vu_() {
        return param_societ_vu_;
    }

    public void setParam_societ_vu_(boolean param_societ_vu_) {
        this.param_societ_vu_ = param_societ_vu_;
    }

    public boolean isParam_societ_up_() {
        return param_societ_up_;
    }

    public void setParam_societ_up_(boolean param_societ_up_) {
        this.param_societ_up_ = param_societ_up_;
    }

    public boolean isParam_societ_del_() {
        return param_societ_del_;
    }

    public void setParam_societ_del_(boolean param_societ_del_) {
        this.param_societ_del_ = param_societ_del_;
    }

    public boolean isParam_societ_add_() {
        return param_societ_add_;
    }

    public void setParam_societ_add_(boolean param_societ_add_) {
        this.param_societ_add_ = param_societ_add_;
    }

    public boolean isParam_empl_vu_() {
        return param_empl_vu_;
    }

    public void setParam_empl_vu_(boolean param_empl_vu_) {
        this.param_empl_vu_ = param_empl_vu_;
    }

    public boolean isParam_empl_up_() {
        return param_empl_up_;
    }

    public void setParam_empl_up_(boolean param_empl_up_) {
        this.param_empl_up_ = param_empl_up_;
    }

    public boolean isParam_empl_del_() {
        return param_empl_del_;
    }

    public void setParam_empl_del_(boolean param_empl_del_) {
        this.param_empl_del_ = param_empl_del_;
    }

    public boolean isParam_empl_add_() {
        return param_empl_add_;
    }

    public void setParam_empl_add_(boolean param_empl_add_) {
        this.param_empl_add_ = param_empl_add_;
    }

    public boolean isPoint_viewListEmps() {
        return point_viewListEmps;
    }

    public boolean isPoint_viewFormMode() {
        return point_viewFormMode;
    }

    public boolean isPoint_changeDateForm() {
        return point_changeDateForm;
    }

    public boolean isPoint_savePointageInPast() {
        return point_savePointageInPast;
    }

    public boolean isPoint_validPointage() {
        return point_validPointage;
    }

    public boolean isPoint_validPointageHs() {
        return point_validPointageHs;
    }

    public boolean isPoint_validPointageHc() {
        return point_validPointageHc;
    }

    public boolean isPoint_modifPointage() {
        return point_modifPointage;
    }

    public boolean isPoint_delLinePointage() {
        return point_delLinePointage;
    }

    public boolean isPoint_invalidPointage() {
        return point_invalidPointage;
    }

    public boolean isPoint_pointageManuel() {
        return point_pointageManuel;
    }

    public boolean isPoint_viewRecapPointage() {
        return point_viewRecapPointage;
    }

    public boolean isPlaning_delete_emp() {
        return planing_delete_emp;
    }

    public boolean isPlaning_update_tranche() {
        return planing_update_tranche;
    }

    public boolean isPlaning_save() {
        return planing_save;
    }

    public boolean isPlaning_active() {
        return planing_active;
    }

    public boolean isPoint_valide_fiche() {
        return point_valide_fiche;
    }

    public boolean isPoint_valid_fiche_onlyHs() {
        return point_valid_fiche_onlyHs;
    }

    public boolean isPoint_invalide_fiche() {
        return point_invalide_fiche;
    }

    public boolean isPoint_validFicheAllScte() {
        return point_validFicheAllScte;
    }

    public boolean isPoint_validFicheAgence() {
        return point_validFicheAgence;
    }

    public boolean isPoint_validFicheDepartement() {
        return point_validFicheDepartement;
    }

    public boolean isPoint_update_fiche_presence() {
        return point_update_fiche_presence;
    }

    public boolean isPoint_delete_fiche_presence() {
        return point_delete_fiche_presence;
    }

    public boolean isPoint_validFicheEquipe() {
        return point_validFicheEquipe;
    }

    public boolean isPoint_update_hs() {
        return point_update_hs;
    }

    public boolean isPoint_update_marge() {
        return point_update_marge;
    }

    public boolean isPoint_print_rappport_periodique() {
        return point_print_rappport_periodique;
    }

    public boolean isPoint_print_rapp_hebdo() {
        return point_print_rapp_hebdo;
    }

    public boolean isPoint_cr_mode_validation() {
        return point_cr_mode_validation;
    }

    public boolean isPoint_del_mode_validation() {
        return point_del_mode_validation;
    }

    public boolean isPoint_savePointageInLongPast() {
        return point_savePointageInLongPast;
    }

    public boolean isPoint_valideMe() {
        return point_valideMe;
    }

    public boolean isConge_cancel() {
        return conge_cancel;
    }

    public boolean isConge_cloturer() {
        return conge_cloturer;
    }

    public boolean isConge_cr_conge() {
        return conge_cr_conge;
    }

    public boolean isConge_suspendre() {
        return conge_suspendre;
    }

    public boolean isConge_cancel_cloturer() {
        return conge_cancel_cloturer;
    }

    public boolean isConge_cancel_valide() {
        return conge_cancel_valide;
    }

    public boolean isConge_delete_conge_suspendu() {
        return conge_delete_conge_suspendu;
    }

    public boolean isConge_valid_duree_supX() {
        return conge_valid_duree_supX;
    }

    public boolean isMission_cancel() {
        return mission_cancel;
    }

    public boolean isMission_cloturer() {
        return mission_cloturer;
    }

    public boolean isMission_cr_ordreM() {
        return mission_cr_ordreM;
    }

    public boolean isMission_suspendre() {
        return mission_suspendre;
    }

    public boolean isMission_cancel_cloturer() {
        return mission_cancel_cloturer;
    }

    public boolean isMission_cancel_valide() {
        return mission_cancel_valide;
    }

    public boolean isMission_set_time() {
        return mission_set_time;
    }

    public boolean isMission_report_date() {
        return mission_report_date;
    }

    public boolean isMission_update_date_mission() {
        return mission_update_date_mission;
    }

    public boolean isMission_param_avance() {
        return mission_param_avance;
    }

    public boolean isMission_add_frais_mission() {
        return mission_add_frais_mission;
    }

    public boolean isBulletin_save_bul_negatif() {
        return bulletin_save_bul_negatif;
    }

    public boolean isMission_del_mission() {
        return mission_del_mission;
    }

    public boolean isMission_print_report() {
        return mission_print_report;
    }

    public boolean isMission_del_mission_new() {
        return mission_del_mission_new;
    }

    public boolean isFv_view_all_doc() {
        return fv_view_all_doc;
    }

    public boolean isFv_view_only_doc_pv() {
        return fv_view_only_doc_pv;
    }

    public boolean isFv_view_only_doc_agence() {
        return fv_view_only_doc_agence;
    }

    public boolean isP_caiss_view_all() {
        return p_caiss_view_all;
    }

    public boolean isP_caiss_view_all_limit_time() {
        return p_caiss_view_all_limit_time;
    }

    public boolean isP_caiss_view_all_user() {
        return p_caiss_view_all_user;
    }

    public boolean isP_caiss_payer() {
        return p_caiss_payer;
    }

    public boolean isP_caiss_cancel_already_pay() {
        return p_caiss_cancel_already_pay;
    }

    public boolean isP_caiss_delete_payer() {
        return p_caiss_delete_payer;
    }

    public boolean isCompta_del_reg_retenue() {
        return compta_del_reg_retenue;
    }

    public boolean isCompta_cancel_piece_valide() {
        return compta_cancel_piece_valide;
    }

    public boolean isCaiss_view_all() {
        return caiss_view_all;
    }

    public boolean isTr_view_all() {
        return tr_view_all;
    }

    public boolean isTr_view_all_user() {
        return tr_view_all_user;
    }

    public boolean isTr_view_historique() {
        return tr_view_historique;
    }

    public boolean isFv_livrer_in_all_depot() {
        return fv_livrer_in_all_depot;
    }

    public boolean isStock_view_all_depot() {
        return stock_view_all_depot;
    }

    public boolean isView_all_depot() {
        return view_all_depot;
    }

    public boolean isD_stock_view_all_date() {
        return d_stock_view_all_date;
    }

    public boolean isD_stock_view_all_agence() {
        return d_stock_view_all_agence;
    }

    public boolean isD_stock_view_all_depot() {
        return d_stock_view_all_depot;
    }

    public boolean isD_stock_edit_when_doc_valid() {
        return d_stock_edit_when_doc_valid;
    }

    public boolean isD_div_update_doc_valid() {
        return d_div_update_doc_valid;
    }

    public boolean isD_div_update_doc() {
        return d_div_update_doc;
    }

    public boolean isProd_update_of_encours() {
        return prod_update_of_encours;
    }

    public boolean isProd_valid_declaration_prod() {
        return prod_valid_declaration_prod;
    }

    public boolean isProd_launch_of() {
        return prod_launch_of;
    }

    public boolean isProd_change_depot_declaration() {
        return prod_change_depot_declaration;
    }

    public boolean isProduction_view_all_date() {
        return production_view_all_date;
    }

    public boolean isProduction_view_all_site() {
        return production_view_all_site;
    }

    public boolean isProduction_view_all_of() {
        return production_view_all_of;
    }

    public boolean isProduction_cloture_of() {
        return production_cloture_of;
    }

    public boolean isProd_view_all_site() {
        return prod_view_all_site;
    }

    public boolean isProd_view_all_equipe() {
        return prod_view_all_equipe;
    }

    public boolean isMut_atidate_epargne() {
        return mut_atidate_epargne;
    }

    public boolean isMut_antidate_epargne_out_periode() {
        return mut_antidate_epargne_out_periode;
    }

    public boolean isCompta_od_view_all_agence() {
        return compta_od_view_all_agence;
    }

    public boolean isPv_load_pr_article() {
        return pv_load_pr_article;
    }

    public boolean isPv_download_article_with_pr() {
        return pv_download_article_with_pr;
    }

    public boolean isPv_view_all_societe() {
        return pv_view_all_societe;
    }

    public boolean isView_all_depot_societe() {
        return view_all_depot_societe;
    }

    public boolean isRa_view_all_societe() {
        return ra_view_all_societe;
    }

    public boolean isRa_view_depot() {
        return ra_view_depot;
    }

    public boolean isRa_view_all_historique() {
        return ra_view_all_historique;
    }

    public boolean isRa_view_cloturer() {
        return ra_view_cloturer;
    }

    public boolean isRa_unluck_fiche() {
        return ra_unluck_fiche;
    }

    public void doNothing() {
    }

    public boolean isBase_view_all_user() {
        return base_view_all_user;
    }

    public boolean isBase_view_all_user_groupe() {
        return base_view_all_user_groupe;
    }

    public boolean isProd_update_all_of() {
        return prod_update_all_of;
    }

    public boolean isProd_force_declaration() {
        return prod_force_declaration;
    }

    public boolean isProd_view_session_all_user() {
        return prod_view_session_all_user;
    }

    public boolean isProduction_view_all_user() {
        return production_view_all_user;
    }

    public boolean isProd_view_session_all_date() {
        return prod_view_session_all_date;
    }

    public boolean isProd_acces_raccourcis() {
        return prod_acces_raccourcis;
    }

    public void setFa_clean_all_doc_societe(boolean fa_clean_all_doc_societe) {
        this.fa_clean_all_doc_societe = fa_clean_all_doc_societe;
    }

    public boolean isFa_clean_all_doc_societe() {
        return fa_clean_all_doc_societe;
    }

    public boolean isFv_clean_all_doc_societe() {
        return fv_clean_all_doc_societe;
    }

    public boolean isTr_change_statut_line() {
        return tr_change_statut_line;
    }

    public boolean isCompta_justif_bp() {
        return compta_justif_bp;
    }

    public boolean isCompta_extourne_cheque() {
        return compta_extourne_cheque;
    }

    public boolean isCompta_od_save_all_type() {
        return compta_od_save_all_type;
    }

    public boolean isCompta_od_view_all_type() {
        return compta_od_view_all_type;
    }

    public boolean isFv_change_num_doc() {
        return fv_change_num_doc;
    }

    public boolean isProd_update_ppte_composant() {
        return prod_update_ppte_composant;
    }

    public boolean isStock_update_qte_recu() {
        return stock_update_qte_recu;
    }

    public boolean isProd_create_other_session() {
        return prod_create_other_session;
    }

    public boolean isGrh_valid_conge() {
        return grh_valid_conge;
    }

    public boolean isGrh_valid_mission() {
        return grh_valid_mission;
    }

    public boolean isFv_can_save_for_other() {
        return fv_can_save_for_other;
    }

    public boolean isFv_can_save_outside_seuil() {
        return fv_can_save_outside_seuil;
    }

    public boolean isJournaux_view_all() {
        return journaux_view_all;
    }

    public void setJournaux_view_all(boolean journaux_view_all) {
        this.journaux_view_all = journaux_view_all;
    }

    public boolean isJournaux_view_only_agence() {
        return journaux_view_only_agence;
    }

    public void setJournaux_view_only_agence(boolean journaux_view_only_agence) {
        this.journaux_view_only_agence = journaux_view_only_agence;
    }

    public boolean isProj_departement_save() {
        return proj_departement_save;
    }

    public void setProj_departement_save(boolean proj_departement_save) {
        this.proj_departement_save = proj_departement_save;
    }

    public boolean isProj_departement_update() {
        return proj_departement_update;
    }

    public void setProj_departement_update(boolean proj_departement_update) {
        this.proj_departement_update = proj_departement_update;
    }

    public boolean isProj_departement_delete() {
        return proj_departement_delete;
    }

    public void setProj_departement_delete(boolean proj_departement_delete) {
        this.proj_departement_delete = proj_departement_delete;
    }

    public boolean isProj_projet_save() {
        return proj_projet_save;
    }

    public void setProj_projet_save(boolean proj_projet_save) {
        this.proj_projet_save = proj_projet_save;
    }

    public boolean isProj_projet_update() {
        return proj_projet_update;
    }

    public void setProj_projet_update(boolean proj_projet_update) {
        this.proj_projet_update = proj_projet_update;
    }

    public boolean isProj_projet_delete() {
        return proj_projet_delete;
    }

    public void setProj_projet_delete(boolean proj_projet_delete) {
        this.proj_projet_delete = proj_projet_delete;
    }

    public boolean isProj_projet_add_service() {
        return proj_projet_add_service;
    }

    public void setProj_projet_add_service(boolean proj_projet_add_service) {
        this.proj_projet_add_service = proj_projet_add_service;
    }

    public boolean isProj_projet_view() {
        return proj_projet_view;
    }

    public void setProj_projet_view(boolean proj_projet_view) {
        this.proj_projet_view = proj_projet_view;
    }

    public boolean isParam_allowed_adresse_() {
        return param_allowed_adresse_;
    }

    public boolean isCompta_valid_all_bp() {
        return compta_valid_all_bp;
    }

    public boolean isCompta_valid_bp() {
        return compta_valid_bp;
    }

    public boolean isCompta_valid_bp_under_max() {
        return compta_valid_bp_under_max;
    }

    public boolean isGrh_update_contrat() {
        return grh_update_contrat;
    }

    public void setGrh_update_contrat(boolean grh_update_contrat) {
        this.grh_update_contrat = grh_update_contrat;
    }

    public boolean isGrh_valid_bulletin() {
        return grh_valid_bulletin;
    }

    public void setGrh_valid_bulletin(boolean grh_valid_bulletin) {
        this.grh_valid_bulletin = grh_valid_bulletin;
    }

    public boolean isGrh_cancel_valid_bulletin() {
        return grh_cancel_valid_bulletin;
    }

    public void setGrh_cancel_valid_bulletin(boolean grh_cancel_valid_bulletin) {
        this.grh_cancel_valid_bulletin = grh_cancel_valid_bulletin;
    }

    public boolean isGrh_compile_bulletin() {
        return grh_compile_bulletin;
    }

    public void setGrh_compile_bulletin(boolean grh_compile_bulletin) {
        this.grh_compile_bulletin = grh_compile_bulletin;
    }

    public boolean isGrh_delete_bulletin() {
        return grh_delete_bulletin;
    }

    public void setGrh_delete_bulletin(boolean grh_delete_bulletin) {
        this.grh_delete_bulletin = grh_delete_bulletin;
    }

    public boolean isCompta_injustifier_bp() {
        return compta_injustifier_bp;
    }

    public void setCompta_injustifier_bp(boolean compta_injustifier_bp) {
        this.compta_injustifier_bp = compta_injustifier_bp;
    }
   
    
    
    

}
