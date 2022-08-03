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
public class AccesPage implements Serializable {

    //Module Parametrage param_
    private boolean param_agenc_, param_societ_, param_model_form, param_import_export, param_workflow;

    //Module Ressource Humaine grh_
    //Bloc Parametrage grh_bloc_param_
    private boolean grh_param_,
            grh_cat_, //page catégorie echélon           
            grh_conv_,
            grh_jr_ferier_,
            grh_param_tache_,
            grh_param_sanct_,
            grh_departm_,
            grh_post_,
            grh_planning_,
            grh_tache_emp_,//Affectation des tâches aux employé
            grh_param_assuranc_,
            grh_param_entretien_,
            grh_empl_,
            grh_contrat_,
            grh_regl_sal_,
            grh_struct_sal,
            grh_type_chomag_;
    //Bloc Fonction grh_bloc_recrutement
    private boolean grh_recrut_entretien,
            grh_recrut_doss_candidat;
    //Bloc Gestion administrative
    private boolean grh_format_, grh_mission_, grh_affect_, grh_sanct_, grh_assurance_;
    //Bloc Rémunération
    private boolean grh_bull_, grh_typ_dep_, grh_bloc_not_, grh_ges_retenu, grh_prelvm_ret_;
    //Bloc Gestion des présences
    private boolean grh_conge_, grh_presenc_, grh_chomag_;

    //Bloc Note de frais grh_bloc_not_
    private boolean grh_not_frai_;
    //Bloc Statistique 
    private boolean grh_tab_bord_, grh_rapports_paie, grh_param_stat;

    //Module Donnees de base base_
    private boolean base_user_, base_niv_, gen_view_parametre,
            gen_view_dictionnaire, base_template_article, base_famille_article, base_groupe_article,
            gen_view_model_ref,
            base_view_article,
            base_view_template_tiers,
            base_view_tiers,
            base_view_client,
            base_view_fournisseur,
            base_view_depots,
            base_view_emplacement,
            base_view_exercices,
            base_view_plan_comptable,
            base_view_caisses,
            base_view_journaux,
            base_view_plan_anal,
            base_code_barre;

    //Module Gestion commerciale com_
    private boolean gescom_paramG, gescom_paramC, gescom_personnel, gescom_pv, gescom_creno_p, gescom_commission, gescom_rist, gescom_cat_tarif, gescom_remise,
            gescom_fiche_appro, gescom_bca, gescom_fa, gescom_avoira, gescom_bla, gescom_retoura, gescom_entree, gescom_sortie, gescom_transfert,
            gescom_inventaire, gescom_stock_dep, gescom_valorise, gescom_ration, gescom_param_ration, gescom_reserv, gescom_recond, gescom_param_obj_vente,
            gescom_bcv, gescom_avoirv, gescom_blv, gescom_retourv, gescom_tbG, gescom_fv, gescom_pfv, gescom_obj_vente, gescom_plan_remise, gescom_calcul_com,
            gescom_dashboard_client, gescom_jorunal_vente, gescom_age_client, gescom_reconditionnment, gescom_tbg_stock, gescom_tbg_vente,
            gescom_dashboard_fournisseur, gescom_dashboard_vendeur, gescom_fv_v2, gescom_fv_service;
    //Module Production prod_
    //Module Comptabilite et Finance compta_
    private boolean compta_view_reg_vente, compta_cheq_reg_vente, compta_param_g,
            compta_view_reg_achat, compta_ope_client, compta_ope_fsseur, compta_rapp_compta,
            compta_view_reg_missions,
            compta_view_reg_virement,
            compta_view_schema_s,
            compta_view_saisie_libre,
            compta_view_op_div,
            compta_view_reg_pieces,
            compta_view_maj_comptable,
            compta_view_report_nouveau,
            compta_view_mvt_caiss,
            compta_view_centre_anal,
            compta_view_mens_vente,
            compta_view_mens_achat;

    //Module Mutuelle mutuel_
    private boolean mutuel_param_g, mutuel_mutuelle, mutuel_mutualiste, mutuel_type_credit,
            mutuel_caisse, mutuel_eparg_assuranc, mutuel_credit, mutuel_periode, mutuel_evenement,
            mutuel_echeancier, mutuel_avance, mutuel_prime, mutuel_paie, mutuel_interet;
    //Module Production prod_
    private boolean ges_prod_param_g, ges_prod_nomenclature, ges_prod_poste_charge, ges_prod_gammes, ges_prod_unites, ges_prod_commande,
            ges_prod_of, ges_prod_conditionnement, ges_prod_equipe, ges_prod_creno_equipe, ges_prod_pic, ges_prod_pdp, ges_prod_pdc, ges_prod_mrp;

    //Module Statistique stat_
    private boolean stat_general_com, stat_ca, stat_classement, stat_listing_vente, stat_journal_vente_vendeur, stat_ecart_vendeur, stat_classement_vendeur,
            stat_creance_vendeur, stat_ca_client, stat_ristourne_client, stat_listing_vente_client, stat_classement_client, stat_journal_vente_client,
            stat_progression_client, stat_valorisation_stock, stat_distribution_stock, stat_resume, stat_journal_production, stat_synthese_production,
            stat_ecart_production, stat_production_vente, stat_bon_provisoire_encours, stat_solde_caisse, stat_brouillard_caisse, stat_facture_achat_impaye,
            stat_ca_fournisseur, stat_classement_fournisseur, stat_ca_article, stat_classement_article, stat_marge_article, stat_listing_vente_article;

    public boolean stat_home_classement_client, stat_home_classement_vendeur, stat_home_classement_point, stat_home_classement_article,
            stat_home_classement_fournisseur, stat_home_classement_caisse, stat_home_classement_bon_provisoire, stat_home_resume_salarial;

    //Module Projet proj_
    private boolean proj_departement, proj_projet;

    public boolean isStat_home_classement_client() {
        return stat_home_classement_client;
    }

    public void setStat_home_classement_client(boolean stat_home_classement_client) {
        this.stat_home_classement_client = stat_home_classement_client;
    }

    public boolean isStat_home_classement_vendeur() {
        return stat_home_classement_vendeur;
    }

    public void setStat_home_classement_vendeur(boolean stat_home_classement_vendeur) {
        this.stat_home_classement_vendeur = stat_home_classement_vendeur;
    }

    public boolean isStat_home_classement_point() {
        return stat_home_classement_point;
    }

    public void setStat_home_classement_point(boolean stat_home_classement_point) {
        this.stat_home_classement_point = stat_home_classement_point;
    }

    public boolean isStat_home_classement_article() {
        return stat_home_classement_article;
    }

    public void setStat_home_classement_article(boolean stat_home_classement_article) {
        this.stat_home_classement_article = stat_home_classement_article;
    }

    public boolean isStat_home_classement_fournisseur() {
        return stat_home_classement_fournisseur;
    }

    public void setStat_home_classement_fournisseur(boolean stat_home_classement_fournisseur) {
        this.stat_home_classement_fournisseur = stat_home_classement_fournisseur;
    }

    public boolean isStat_home_classement_caisse() {
        return stat_home_classement_caisse;
    }

    public void setStat_home_classement_caisse(boolean stat_home_classement_caisse) {
        this.stat_home_classement_caisse = stat_home_classement_caisse;
    }

    public boolean isStat_home_classement_bon_provisoire() {
        return stat_home_classement_bon_provisoire;
    }

    public void setStat_home_classement_bon_provisoire(boolean stat_home_classement_bon_provisoire) {
        this.stat_home_classement_bon_provisoire = stat_home_classement_bon_provisoire;
    }

    public boolean isStat_home_resume_salarial() {
        return stat_home_resume_salarial;
    }

    public void setStat_home_resume_salarial(boolean stat_home_resume_salarial) {
        this.stat_home_resume_salarial = stat_home_resume_salarial;
    }

    public boolean isCompta_view_centre_anal() {
        return compta_view_centre_anal;
    }

    public void setCompta_view_centre_anal(boolean compta_view_centre_anal) {
        this.compta_view_centre_anal = compta_view_centre_anal;
    }

    public boolean isCompta_view_mens_vente() {
        return compta_view_mens_vente;
    }

    public void setCompta_view_mens_vente(boolean compta_view_mens_vente) {
        this.compta_view_mens_vente = compta_view_mens_vente;
    }

    public boolean isCompta_view_mens_achat() {
        return compta_view_mens_achat;
    }

    public void setCompta_view_mens_achat(boolean compta_view_mens_achat) {
        this.compta_view_mens_achat = compta_view_mens_achat;
    }

    public boolean isGescom_fv_v2() {
        return gescom_fv_v2;
    }

    public void setGescom_fv_v2(boolean gescom_fv_v2) {
        this.gescom_fv_v2 = gescom_fv_v2;
    }

    public boolean isGescom_fv_service() {
        return gescom_fv_service;
    }

    public void setGescom_fv_service(boolean gescom_fv_service) {
        this.gescom_fv_service = gescom_fv_service;
    }

    public boolean isGes_prod_commande() {
        return ges_prod_commande;
    }

    public void setGes_prod_commande(boolean ges_prod_commande) {
        this.ges_prod_commande = ges_prod_commande;
    }

    public boolean isGescom_tbg_stock() {
        return gescom_tbg_stock;
    }

    public void setGescom_tbg_stock(boolean gescom_tbg_stock) {
        this.gescom_tbg_stock = gescom_tbg_stock;
    }

    public boolean isGescom_tbg_vente() {
        return gescom_tbg_vente;
    }

    public void setGescom_tbg_vente(boolean gescom_tbg_vente) {
        this.gescom_tbg_vente = gescom_tbg_vente;
    }

    public boolean isGescom_dashboard_fournisseur() {
        return gescom_dashboard_fournisseur;
    }

    public void setGescom_dashboard_fournisseur(boolean gescom_dashboard_fournisseur) {
        this.gescom_dashboard_fournisseur = gescom_dashboard_fournisseur;
    }

    public boolean isGescom_dashboard_vendeur() {
        return gescom_dashboard_vendeur;
    }

    public void setGescom_dashboard_vendeur(boolean gescom_dashboard_vendeur) {
        this.gescom_dashboard_vendeur = gescom_dashboard_vendeur;
    }

    public boolean isStat_general_com() {
        return stat_general_com;
    }

    public void setStat_general_com(boolean stat_general_com) {
        this.stat_general_com = stat_general_com;
    }

    public boolean isStat_ca() {
        return stat_ca;
    }

    public void setStat_ca(boolean stat_ca) {
        this.stat_ca = stat_ca;
    }

    public boolean isStat_classement() {
        return stat_classement;
    }

    public void setStat_classement(boolean stat_classement) {
        this.stat_classement = stat_classement;
    }

    public boolean isStat_listing_vente() {
        return stat_listing_vente;
    }

    public void setStat_listing_vente(boolean stat_listing_vente) {
        this.stat_listing_vente = stat_listing_vente;
    }

    public boolean isStat_journal_vente_vendeur() {
        return stat_journal_vente_vendeur;
    }

    public void setStat_journal_vente_vendeur(boolean stat_journal_vente_vendeur) {
        this.stat_journal_vente_vendeur = stat_journal_vente_vendeur;
    }

    public boolean isStat_ecart_vendeur() {
        return stat_ecart_vendeur;
    }

    public boolean isCompta_view_report_nouveau() {
        return compta_view_report_nouveau;
    }

    public void setCompta_view_report_nouveau(boolean compta_view_report_nouveau) {
        this.compta_view_report_nouveau = compta_view_report_nouveau;
    }

    public void setStat_ecart_vendeur(boolean stat_ecart_vendeur) {
        this.stat_ecart_vendeur = stat_ecart_vendeur;
    }

    public boolean isStat_classement_vendeur() {
        return stat_classement_vendeur;
    }

    public void setStat_classement_vendeur(boolean stat_classement_vendeur) {
        this.stat_classement_vendeur = stat_classement_vendeur;
    }

    public boolean isStat_creance_vendeur() {
        return stat_creance_vendeur;
    }

    public void setStat_creance_vendeur(boolean stat_creance_vendeur) {
        this.stat_creance_vendeur = stat_creance_vendeur;
    }

    public boolean isStat_ca_client() {
        return stat_ca_client;
    }

    public void setStat_ca_client(boolean stat_ca_client) {
        this.stat_ca_client = stat_ca_client;
    }

    public boolean isStat_ristourne_client() {
        return stat_ristourne_client;
    }

    public void setStat_ristourne_client(boolean stat_ristourne_client) {
        this.stat_ristourne_client = stat_ristourne_client;
    }

    public boolean isStat_listing_vente_client() {
        return stat_listing_vente_client;
    }

    public void setStat_listing_vente_client(boolean stat_listing_vente_client) {
        this.stat_listing_vente_client = stat_listing_vente_client;
    }

    public boolean isStat_classement_client() {
        return stat_classement_client;
    }

    public void setStat_classement_client(boolean stat_classement_client) {
        this.stat_classement_client = stat_classement_client;
    }

    public boolean isStat_journal_vente_client() {
        return stat_journal_vente_client;
    }

    public void setStat_journal_vente_client(boolean stat_journal_vente_client) {
        this.stat_journal_vente_client = stat_journal_vente_client;
    }

    public boolean isStat_progression_client() {
        return stat_progression_client;
    }

    public void setStat_progression_client(boolean stat_progression_client) {
        this.stat_progression_client = stat_progression_client;
    }

    public boolean isStat_valorisation_stock() {
        return stat_valorisation_stock;
    }

    public void setStat_valorisation_stock(boolean stat_valorisation_stock) {
        this.stat_valorisation_stock = stat_valorisation_stock;
    }

    public boolean isStat_distribution_stock() {
        return stat_distribution_stock;
    }

    public void setStat_distribution_stock(boolean stat_distribution_stock) {
        this.stat_distribution_stock = stat_distribution_stock;
    }

    public boolean isStat_resume() {
        return stat_resume;
    }

    public void setStat_resume(boolean stat_resume) {
        this.stat_resume = stat_resume;
    }

    public boolean isStat_journal_production() {
        return stat_journal_production;
    }

    public void setStat_journal_production(boolean stat_journal_production) {
        this.stat_journal_production = stat_journal_production;
    }

    public boolean isStat_synthese_production() {
        return stat_synthese_production;
    }

    public void setStat_synthese_production(boolean stat_synthese_production) {
        this.stat_synthese_production = stat_synthese_production;
    }

    public boolean isStat_ecart_production() {
        return stat_ecart_production;
    }

    public void setStat_ecart_production(boolean stat_ecart_production) {
        this.stat_ecart_production = stat_ecart_production;
    }

    public boolean isStat_production_vente() {
        return stat_production_vente;
    }

    public void setStat_production_vente(boolean stat_production_vente) {
        this.stat_production_vente = stat_production_vente;
    }

    public boolean isStat_bon_provisoire_encours() {
        return stat_bon_provisoire_encours;
    }

    public void setStat_bon_provisoire_encours(boolean stat_bon_provisoire_encours) {
        this.stat_bon_provisoire_encours = stat_bon_provisoire_encours;
    }

    public boolean isStat_solde_caisse() {
        return stat_solde_caisse;
    }

    public void setStat_solde_caisse(boolean stat_solde_caisse) {
        this.stat_solde_caisse = stat_solde_caisse;
    }

    public boolean isStat_brouillard_caisse() {
        return stat_brouillard_caisse;
    }

    public void setStat_brouillard_caisse(boolean stat_brouillard_caisse) {
        this.stat_brouillard_caisse = stat_brouillard_caisse;
    }

    public boolean isStat_facture_achat_impaye() {
        return stat_facture_achat_impaye;
    }

    public void setStat_facture_achat_impaye(boolean stat_facture_achat_impaye) {
        this.stat_facture_achat_impaye = stat_facture_achat_impaye;
    }

    public boolean isStat_ca_fournisseur() {
        return stat_ca_fournisseur;
    }

    public void setStat_ca_fournisseur(boolean stat_ca_fournisseur) {
        this.stat_ca_fournisseur = stat_ca_fournisseur;
    }

    public boolean isStat_classement_fournisseur() {
        return stat_classement_fournisseur;
    }

    public void setStat_classement_fournisseur(boolean stat_classement_fournisseur) {
        this.stat_classement_fournisseur = stat_classement_fournisseur;
    }

    public boolean isStat_ca_article() {
        return stat_ca_article;
    }

    public void setStat_ca_article(boolean stat_ca_article) {
        this.stat_ca_article = stat_ca_article;
    }

    public boolean isStat_classement_article() {
        return stat_classement_article;
    }

    public void setStat_classement_article(boolean stat_classement_article) {
        this.stat_classement_article = stat_classement_article;
    }

    public boolean isStat_marge_article() {
        return stat_marge_article;
    }

    public void setStat_marge_article(boolean stat_marge_article) {
        this.stat_marge_article = stat_marge_article;
    }

    public boolean isStat_listing_vente_article() {
        return stat_listing_vente_article;
    }

    public void setStat_listing_vente_article(boolean stat_listing_vente_article) {
        this.stat_listing_vente_article = stat_listing_vente_article;
    }

    public boolean isGescom_reconditionnment() {
        return gescom_reconditionnment;
    }

    public void setGescom_reconditionnment(boolean gescom_reconditionnment) {
        this.gescom_reconditionnment = gescom_reconditionnment;
    }

    public boolean isMutuel_param_g() {
        return mutuel_param_g;
    }

    public void setMutuel_param_g(boolean mutuel_param_g) {
        this.mutuel_param_g = mutuel_param_g;
    }

    public boolean isMutuel_mutuelle() {
        return mutuel_mutuelle;
    }

    public void setMutuel_mutuelle(boolean mutuel_mutuelle) {
        this.mutuel_mutuelle = mutuel_mutuelle;
    }

    public boolean isMutuel_mutualiste() {
        return mutuel_mutualiste;
    }

    public void setMutuel_mutualiste(boolean mutuel_mutualiste) {
        this.mutuel_mutualiste = mutuel_mutualiste;
    }

    public boolean isMutuel_type_credit() {
        return mutuel_type_credit;
    }

    public void setMutuel_type_credit(boolean mutuel_type_credit) {
        this.mutuel_type_credit = mutuel_type_credit;
    }

    public boolean isMutuel_caisse() {
        return mutuel_caisse;
    }

    public void setMutuel_caisse(boolean mutuel_caisse) {
        this.mutuel_caisse = mutuel_caisse;
    }

    public boolean isMutuel_eparg_assuranc() {
        return mutuel_eparg_assuranc;
    }

    public void setMutuel_eparg_assuranc(boolean mutuel_eparg_assuranc) {
        this.mutuel_eparg_assuranc = mutuel_eparg_assuranc;
    }

    public boolean isMutuel_credit() {
        return mutuel_credit;
    }

    public void setMutuel_credit(boolean mutuel_credit) {
        this.mutuel_credit = mutuel_credit;
    }

    public boolean isMutuel_periode() {
        return mutuel_periode;
    }

    public void setMutuel_periode(boolean mutuel_periode) {
        this.mutuel_periode = mutuel_periode;
    }

    public boolean isMutuel_evenement() {
        return mutuel_evenement;
    }

    public void setMutuel_evenement(boolean mutuel_evenement) {
        this.mutuel_evenement = mutuel_evenement;
    }

    public boolean isMutuel_echeancier() {
        return mutuel_echeancier;
    }

    public void setMutuel_echeancier(boolean mutuel_echeancier) {
        this.mutuel_echeancier = mutuel_echeancier;
    }

    public boolean isMutuel_avance() {
        return mutuel_avance;
    }

    public void setMutuel_avance(boolean mutuel_avance) {
        this.mutuel_avance = mutuel_avance;
    }

    public boolean isMutuel_prime() {
        return mutuel_prime;
    }

    public void setMutuel_prime(boolean mutuel_prime) {
        this.mutuel_prime = mutuel_prime;
    }

    public boolean isMutuel_paie() {
        return mutuel_paie;
    }

    public void setMutuel_paie(boolean mutuel_paie) {
        this.mutuel_paie = mutuel_paie;
    }

    public boolean isMutuel_interet() {
        return mutuel_interet;
    }

    public void setMutuel_interet(boolean mutuel_interet) {
        this.mutuel_interet = mutuel_interet;
    }

    public boolean isParam_model_form() {
        return param_model_form;
    }

    public void setParam_model_form(boolean param_model_form) {
        this.param_model_form = param_model_form;
    }

    public boolean isParam_import_export() {
        return param_import_export;
    }

    public void setParam_import_export(boolean param_import_export) {
        this.param_import_export = param_import_export;
    }

    public boolean isParam_workflow() {
        return param_workflow;
    }

    public void setParam_workflow(boolean param_workflow) {
        this.param_workflow = param_workflow;
    }

    public boolean isCompta_cheq_reg_vente() {
        return compta_cheq_reg_vente;
    }

    public void setCompta_cheq_reg_vente(boolean compta_cheq_reg_vente) {
        this.compta_cheq_reg_vente = compta_cheq_reg_vente;
    }

    public boolean isCompta_param_g() {
        return compta_param_g;
    }

    public void setCompta_param_g(boolean compta_param_g) {
        this.compta_param_g = compta_param_g;
    }

    public boolean isCompta_ope_client() {
        return compta_ope_client;
    }

    public void setCompta_ope_client(boolean compta_ope_client) {
        this.compta_ope_client = compta_ope_client;
    }

    public boolean isCompta_ope_fsseur() {
        return compta_ope_fsseur;
    }

    public void setCompta_ope_fsseur(boolean compta_ope_fsseur) {
        this.compta_ope_fsseur = compta_ope_fsseur;
    }

    public boolean isCompta_rapp_compta() {
        return compta_rapp_compta;
    }

    public void setCompta_rapp_compta(boolean compta_rapp_compta) {
        this.compta_rapp_compta = compta_rapp_compta;
    }

    //Module Statistique stat_
    public boolean isBase_template_article() {
        return base_template_article;
    }

    public void setBase_template_article(boolean base_template_article) {
        this.base_template_article = base_template_article;
    }

    public boolean isBase_famille_article() {
        return base_famille_article;
    }

    public void setBase_famille_article(boolean base_famille_article) {
        this.base_famille_article = base_famille_article;
    }

    public boolean isBase_groupe_article() {
        return base_groupe_article;
    }

    public void setBase_groupe_article(boolean base_groupe_article) {
        this.base_groupe_article = base_groupe_article;
    }

    public boolean isGescom_reserv() {
        return gescom_reserv;
    }

    public void setGescom_reserv(boolean gescom_reserv) {
        this.gescom_reserv = gescom_reserv;
    }

    public boolean isGescom_recond() {
        return gescom_recond;
    }

    public void setGescom_recond(boolean gescom_recond) {
        this.gescom_recond = gescom_recond;
    }

    public boolean isGescom_param_obj_vente() {
        return gescom_param_obj_vente;
    }

    public void setGescom_param_obj_vente(boolean gescom_param_obj_vente) {
        this.gescom_param_obj_vente = gescom_param_obj_vente;
    }

    public boolean isGescom_obj_vente() {
        return gescom_obj_vente;
    }

    public void setGescom_obj_vente(boolean gescom_obj_vente) {
        this.gescom_obj_vente = gescom_obj_vente;
    }

    public boolean isGescom_plan_remise() {
        return gescom_plan_remise;
    }

    public void setGescom_plan_remise(boolean gescom_plan_remise) {
        this.gescom_plan_remise = gescom_plan_remise;
    }

    public boolean isGescom_calcul_com() {
        return gescom_calcul_com;
    }

    public void setGescom_calcul_com(boolean gescom_calcul_com) {
        this.gescom_calcul_com = gescom_calcul_com;
    }

    public boolean isGescom_dashboard_client() {
        return gescom_dashboard_client;
    }

    public void setGescom_dashboard_client(boolean gescom_dashboard_client) {
        this.gescom_dashboard_client = gescom_dashboard_client;
    }

    public boolean isGescom_jorunal_vente() {
        return gescom_jorunal_vente;
    }

    public void setGescom_jorunal_vente(boolean gescom_jorunal_vente) {
        this.gescom_jorunal_vente = gescom_jorunal_vente;
    }

    public boolean isGescom_age_client() {
        return gescom_age_client;
    }

    public void setGescom_age_client(boolean gescom_age_client) {
        this.gescom_age_client = gescom_age_client;
    }

    //Module Relation Clientelle client_
    public boolean isGescom_ration() {
        return gescom_ration;
    }

    public boolean isGescom_param_ration() {
        return gescom_param_ration;
    }

    public void setGescom_param_ration(boolean gescom_param_ration) {
        this.gescom_param_ration = gescom_param_ration;
    }

    public void setGescom_ration(boolean gescom_ration) {
        this.gescom_ration = gescom_ration;
    }

    public boolean isParam_agenc_() {
        return param_agenc_;
    }

    public void setParam_agenc_(boolean param_agenc_) {
        this.param_agenc_ = param_agenc_;
    }

    public boolean isGrh_type_chomag_() {
        return grh_type_chomag_;
    }

    public boolean isGrh_chomag_() {
        return grh_chomag_;
    }

    public void setGrh_type_chomag_(boolean grh_type_chomag_) {
        this.grh_type_chomag_ = grh_type_chomag_;
    }

    public void setGrh_chomag_(boolean grh_chomag_) {
        this.grh_chomag_ = grh_chomag_;
    }

    public boolean isParam_societ_() {
        return param_societ_;
    }

    public void setParam_societ_(boolean param_societ_) {
        this.param_societ_ = param_societ_;
    }

    public boolean isGrh_param_sanct_() {
        return grh_param_sanct_;
    }

    public void setGrh_param_sanct_(boolean grh_param_sanct_) {
        this.grh_param_sanct_ = grh_param_sanct_;
    }

    public boolean isGrh_sanct_() {
        return grh_sanct_;
    }

    public void setGrh_sanct_(boolean grh_sanct_) {
        this.grh_sanct_ = grh_sanct_;
    }

    public boolean isGrh_empl_() {
        return grh_empl_;
    }

    public void setGrh_empl_(boolean grh_empl_) {
        this.grh_empl_ = grh_empl_;
    }

    public boolean isGrh_departm_() {
        return grh_departm_;
    }

    public void setGrh_departm_(boolean grh_departm_) {
        this.grh_departm_ = grh_departm_;
    }

    public boolean isGrh_cat_() {
        return grh_cat_;
    }

    public void setGrh_cat_(boolean grh_cat_) {
        this.grh_cat_ = grh_cat_;
    }

    public boolean isBase_user_() {
        return base_user_;
    }

    public void setBase_user_(boolean base_user_) {
        this.base_user_ = base_user_;
    }

    public boolean isGrh_param_() {
        return grh_param_;
    }

    public void setGrh_param_(boolean grh_param_) {
        this.grh_param_ = grh_param_;
    }

    public boolean isGrh_conv_() {
        return grh_conv_;
    }

    public void setGrh_conv_(boolean grh_conv_) {
        this.grh_conv_ = grh_conv_;
    }

    public boolean isGrh_jr_ferier_() {
        return grh_jr_ferier_;
    }

    public void setGrh_jr_ferier_(boolean grh_jr_ferier_) {
        this.grh_jr_ferier_ = grh_jr_ferier_;
    }

    public boolean isGrh_param_tache_() {
        return grh_param_tache_;
    }

    public void setGrh_param_tache_(boolean grh_param_tache_) {
        this.grh_param_tache_ = grh_param_tache_;
    }

    public boolean isGrh_post_() {
        return grh_post_;
    }

    public void setGrh_post_(boolean grh_post_) {
        this.grh_post_ = grh_post_;
    }

    public boolean isGrh_planning_() {
        return grh_planning_;
    }

    public void setGrh_planning_(boolean grh_planning_) {
        this.grh_planning_ = grh_planning_;
    }

    public boolean isGrh_tache_emp_() {
        return grh_tache_emp_;
    }

    public void setGrh_tache_emp_(boolean grh_tache_emp_) {
        this.grh_tache_emp_ = grh_tache_emp_;
    }

    public boolean isGrh_conge_() {
        return grh_conge_;
    }

    public void setGrh_conge_(boolean grh_conge_) {
        this.grh_conge_ = grh_conge_;
    }

    public boolean isGrh_presenc_() {
        return grh_presenc_;
    }

    public void setGrh_presenc_(boolean grh_presenc_) {
        this.grh_presenc_ = grh_presenc_;
    }

    public boolean isGrh_contrat_() {
        return grh_contrat_;
    }

    public void setGrh_contrat_(boolean grh_contrat_) {
        this.grh_contrat_ = grh_contrat_;
    }

    public boolean isGrh_format_() {
        return grh_format_;
    }

    public void setGrh_format_(boolean grh_format_) {
        this.grh_format_ = grh_format_;
    }

    public boolean isGrh_mission_() {
        return grh_mission_;
    }

    public void setGrh_mission_(boolean grh_mission_) {
        this.grh_mission_ = grh_mission_;
    }

    public boolean isGrh_affect_() {
        return grh_affect_;
    }

    public void setGrh_affect_(boolean grh_affect_) {
        this.grh_affect_ = grh_affect_;
    }

    public boolean isGrh_param_assuranc_() {
        return grh_param_assuranc_;
    }

    public void setGrh_param_assuranc_(boolean grh_param_assuranc_) {
        this.grh_param_assuranc_ = grh_param_assuranc_;
    }

    public boolean isGrh_tab_bord_() {
        return grh_tab_bord_;
    }

    public void setGrh_tab_bord_(boolean grh_tab_bord_) {
        this.grh_tab_bord_ = grh_tab_bord_;
    }

    public boolean isGrh_regl_sal_() {
        return grh_regl_sal_;
    }

    public void setGrh_regl_sal_(boolean grh_regl_sal_) {
        this.grh_regl_sal_ = grh_regl_sal_;
    }

    public boolean isGrh_struct_sal() {
        return grh_struct_sal;
    }

    public void setGrh_struct_sal(boolean grh_struct_sal) {
        this.grh_struct_sal = grh_struct_sal;
    }

    public void setGrh_param_entretien_(boolean grh_param_entretien_) {
        this.grh_param_entretien_ = grh_param_entretien_;
    }

    public boolean isGrh_param_entretien_() {
        return grh_param_entretien_;
    }

    public boolean isGrh_bull_() {
        return grh_bull_;
    }

    public void setGrh_bull_(boolean grh_bull_) {
        this.grh_bull_ = grh_bull_;
    }

    public boolean isGrh_typ_dep_() {
        return grh_typ_dep_;
    }

    public void setGrh_typ_dep_(boolean grh_typ_dep_) {
        this.grh_typ_dep_ = grh_typ_dep_;
    }

    public boolean isGrh_not_frai_() {
        return grh_not_frai_;
    }

    public void setGrh_not_frai_(boolean grh_not_frai_) {
        this.grh_not_frai_ = grh_not_frai_;
    }

    public boolean isGrh_ges_retenu() {
        return grh_ges_retenu;
    }

    public void setGrh_ges_retenu(boolean grh_ges_retenu) {
        this.grh_ges_retenu = grh_ges_retenu;
    }

    public boolean isGrh_prelvm_ret_() {
        return grh_prelvm_ret_;
    }

    public void setGrh_prelvm_ret_(boolean grh_prelvm_ret_) {
        this.grh_prelvm_ret_ = grh_prelvm_ret_;
    }

    public boolean isBase_niv_() {
        return base_niv_;
    }

    public void setBase_niv_(boolean base_niv_) {
        this.base_niv_ = base_niv_;
    }

    public boolean isGrh_recrut_doss_candidat() {
        return grh_recrut_doss_candidat;
    }

    public boolean isGrh_recrut_entretien() {
        return grh_recrut_entretien;
    }

    public boolean isGrh_assurance_() {
        return grh_assurance_;
    }

    public boolean isGrh_bloc_not_() {
        return grh_bloc_not_;
    }

    public void setGrh_bloc_not_(boolean grh_bloc_not_) {
        this.grh_bloc_not_ = grh_bloc_not_;
    }

    public boolean isGrh_param_stat() {
        return grh_param_stat;
    }

    public boolean isGrh_rapports_paie() {
        return grh_rapports_paie;
    }

    public boolean isGescom_paramG() {
        return gescom_paramG;
    }

    public void setGescom_paramG(boolean gescom_paramG) {
        this.gescom_paramG = gescom_paramG;
    }

    public boolean isGescom_paramC() {
        return gescom_paramC;
    }

    public void setGescom_paramC(boolean gescom_paramC) {
        this.gescom_paramC = gescom_paramC;
    }

    public boolean isGescom_personnel() {
        return gescom_personnel;
    }

    public void setGescom_personnel(boolean gescom_personnel) {
        this.gescom_personnel = gescom_personnel;
    }

    public boolean isGescom_pv() {
        return gescom_pv;
    }

    public void setGescom_pv(boolean gescom_pv) {
        this.gescom_pv = gescom_pv;
    }

    public boolean isGescom_creno_p() {
        return gescom_creno_p;
    }

    public void setGescom_creno_p(boolean gescom_creno_p) {
        this.gescom_creno_p = gescom_creno_p;
    }

    public boolean isGescom_commission() {
        return gescom_commission;
    }

    public void setGescom_commission(boolean gescom_commission) {
        this.gescom_commission = gescom_commission;
    }

    public boolean isGescom_rist() {
        return gescom_rist;
    }

    public void setGescom_rist(boolean gescom_rist) {
        this.gescom_rist = gescom_rist;
    }

    public boolean isGescom_cat_tarif() {
        return gescom_cat_tarif;
    }

    public void setGescom_cat_tarif(boolean gescom_cat_tarif) {
        this.gescom_cat_tarif = gescom_cat_tarif;
    }

    public boolean isGescom_fiche_appro() {
        return gescom_fiche_appro;
    }

    public void setGescom_fiche_appro(boolean gescom_fiche_appro) {
        this.gescom_fiche_appro = gescom_fiche_appro;
    }

    public boolean isGescom_bca() {
        return gescom_bca;
    }

    public void setGescom_bca(boolean gescom_bca) {
        this.gescom_bca = gescom_bca;
    }

    public boolean isGescom_fa() {
        return gescom_fa;
    }

    public void setGescom_fa(boolean gescom_fa) {
        this.gescom_fa = gescom_fa;
    }

    public boolean isGescom_avoira() {
        return gescom_avoira;
    }

    public void setGescom_avoira(boolean gescom_avoira) {
        this.gescom_avoira = gescom_avoira;
    }

    public boolean isGescom_bla() {
        return gescom_bla;
    }

    public void setGescom_bla(boolean gescom_bla) {
        this.gescom_bla = gescom_bla;
    }

    public boolean isGescom_retoura() {
        return gescom_retoura;
    }

    public void setGescom_retoura(boolean gescom_retoura) {
        this.gescom_retoura = gescom_retoura;
    }

    public boolean isGescom_entree() {
        return gescom_entree;
    }

    public void setGescom_entree(boolean gescom_entree) {
        this.gescom_entree = gescom_entree;
    }

    public boolean isGescom_sortie() {
        return gescom_sortie;
    }

    public void setGescom_sortie(boolean gescom_sortie) {
        this.gescom_sortie = gescom_sortie;
    }

    public boolean isGescom_transfert() {
        return gescom_transfert;
    }

    public void setGescom_transfert(boolean gescom_transfert) {
        this.gescom_transfert = gescom_transfert;
    }

    public boolean isGescom_inventaire() {
        return gescom_inventaire;
    }

    public void setGescom_inventaire(boolean gescom_inventaire) {
        this.gescom_inventaire = gescom_inventaire;
    }

    public boolean isGescom_stock_dep() {
        return gescom_stock_dep;
    }

    public void setGescom_stock_dep(boolean gescom_stock_dep) {
        this.gescom_stock_dep = gescom_stock_dep;
    }

    public boolean isGescom_valorise() {
        return gescom_valorise;
    }

    public void setGescom_valorise(boolean gescom_valorise) {
        this.gescom_valorise = gescom_valorise;
    }

    public boolean isGescom_pfv() {
        return gescom_pfv;
    }

    public void setGescom_pfv(boolean gescom_pfv) {
        this.gescom_pfv = gescom_pfv;
    }

    public boolean isGescom_bcv() {
        return gescom_bcv;
    }

    public void setGescom_bcv(boolean gescom_bcv) {
        this.gescom_bcv = gescom_bcv;
    }

    public boolean isGescom_avoirv() {
        return gescom_avoirv;
    }

    public void setGescom_avoirv(boolean gescom_avoirv) {
        this.gescom_avoirv = gescom_avoirv;
    }

    public boolean isGescom_blv() {
        return gescom_blv;
    }

    public void setGescom_blv(boolean gescom_blv) {
        this.gescom_blv = gescom_blv;
    }

    public boolean isGescom_retourv() {
        return gescom_retourv;
    }

    public void setGescom_retourv(boolean gescom_retourv) {
        this.gescom_retourv = gescom_retourv;
    }

    public boolean isGescom_tbG() {
        return gescom_tbG;
    }

    public void setGescom_tbG(boolean gescom_tbG) {
        this.gescom_tbG = gescom_tbG;
    }

    public boolean isGescom_remise() {
        return gescom_remise;
    }

    public void setGescom_fv(boolean gescom_fv) {
        this.gescom_fv = gescom_fv;
    }

    public boolean isGescom_fv() {
        return gescom_fv;
    }

    public boolean isCompta_view_reg_vente() {
        return compta_view_reg_vente;
    }

    public boolean isCompta_view_reg_achat() {
        return compta_view_reg_achat;
    }

    public boolean isCompta_view_reg_missions() {
        return compta_view_reg_missions;
    }

    public boolean isCompta_view_reg_virement() {
        return compta_view_reg_virement;
    }

    public boolean isCompta_view_schema_s() {
        return compta_view_schema_s;
    }

    public boolean isCompta_view_saisie_libre() {
        return compta_view_saisie_libre;
    }

    public boolean isCompta_view_op_div() {
        return compta_view_op_div;
    }

    public boolean isCompta_view_reg_pieces() {
        return compta_view_reg_pieces;
    }

    public boolean isCompta_view_maj_comptable() {
        return compta_view_maj_comptable;
    }

    public boolean isGen_view_parametre() {
        return gen_view_parametre;
    }

    public void setGen_view_parametre(boolean gen_view_parametre) {
        this.gen_view_parametre = gen_view_parametre;
    }

    public boolean isGen_view_dictionnaire() {
        return gen_view_dictionnaire;
    }

    public void setGen_view_dictionnaire(boolean gen_view_dictionnaire) {
        this.gen_view_dictionnaire = gen_view_dictionnaire;
    }

    public boolean isGen_view_model_ref() {
        return gen_view_model_ref;
    }

    public void setGen_view_model_ref(boolean gen_view_model_ref) {
        this.gen_view_model_ref = gen_view_model_ref;
    }

    public boolean isBase_view_article() {
        return base_view_article;
    }

    public void setBase_view_article(boolean base_view_article) {
        this.base_view_article = base_view_article;
    }

    public boolean isBase_view_template_tiers() {
        return base_view_template_tiers;
    }

    public void setBase_view_template_tiers(boolean base_view_template_tiers) {
        this.base_view_template_tiers = base_view_template_tiers;
    }

    public boolean isBase_view_tiers() {
        return base_view_tiers;
    }

    public void setBase_view_tiers(boolean base_view_tiers) {
        this.base_view_tiers = base_view_tiers;
    }

    public boolean isBase_view_client() {
        return base_view_client;
    }

    public void setBase_view_client(boolean base_view_client) {
        this.base_view_client = base_view_client;
    }

    public boolean isBase_view_fournisseur() {
        return base_view_fournisseur;
    }

    public void setBase_view_fournisseur(boolean base_view_fournisseur) {
        this.base_view_fournisseur = base_view_fournisseur;
    }

    public boolean isBase_view_depots() {
        return base_view_depots;
    }

    public void setBase_view_depots(boolean base_view_depots) {
        this.base_view_depots = base_view_depots;
    }

    public boolean isBase_view_exercices() {
        return base_view_exercices;
    }

    public void setBase_view_exercices(boolean base_view_exercices) {
        this.base_view_exercices = base_view_exercices;
    }

    public boolean isBase_view_plan_comptable() {
        return base_view_plan_comptable;
    }

    public void setBase_view_plan_comptable(boolean base_view_plan_comptable) {
        this.base_view_plan_comptable = base_view_plan_comptable;
    }

    public boolean isBase_view_caisses() {
        return base_view_caisses;
    }

    public void setBase_view_caisses(boolean base_view_caisses) {
        this.base_view_caisses = base_view_caisses;
    }

    public boolean isBase_view_journaux() {
        return base_view_journaux;
    }

    public void setBase_view_journaux(boolean base_view_journaux) {
        this.base_view_journaux = base_view_journaux;
    }

    public boolean isBase_view_plan_anal() {
        return base_view_plan_anal;
    }

    public void setBase_view_plan_anal(boolean base_view_plan_anal) {
        this.base_view_plan_anal = base_view_plan_anal;
    }

    public boolean isBase_code_barre() {
        return base_code_barre;
    }

    public boolean isGes_prod_param_g() {
        return ges_prod_param_g;
    }

    public boolean isGes_prod_nomenclature() {
        return ges_prod_nomenclature;
    }

    public boolean isGes_prod_poste_charge() {
        return ges_prod_poste_charge;
    }

    public boolean isGes_prod_gammes() {
        return ges_prod_gammes;
    }

    public boolean isGes_prod_of() {
        return ges_prod_of;
    }

    public boolean isGes_prod_conditionnement() {
        return ges_prod_conditionnement;
    }

    public boolean isGes_prod_equipe() {
        return ges_prod_equipe;
    }

    public boolean isGes_prod_creno_equipe() {
        return ges_prod_creno_equipe;
    }

    public boolean isGes_prod_pic() {
        return ges_prod_pic;
    }

    public boolean isGes_prod_pdp() {
        return ges_prod_pdp;
    }

    public boolean isGes_prod_pdc() {
        return ges_prod_pdc;
    }

    public boolean isGes_prod_mrp() {
        return ges_prod_mrp;
    }

    public boolean isGes_prod_unites() {
        return ges_prod_unites;
    }

    public boolean isCompta_view_mvt_caiss() {
        return compta_view_mvt_caiss;
    }

    public boolean isBase_view_emplacement() {
        return base_view_emplacement;
    }

    public boolean isProj_departement() {
        return proj_departement;
    }

    public void setProj_departement(boolean proj_departement) {
        this.proj_departement = proj_departement;
    }

    public boolean isProj_projet() {
        return proj_projet;
    }

    public void setProj_projet(boolean proj_projet) {
        this.proj_projet = proj_projet;
    }

}
