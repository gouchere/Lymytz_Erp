/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.navigue;

import com.ocpsoft.pretty.faces.config.DigesterPrettyConfigParser;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.PrettyConfigBuilder;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.xml.sax.SAXException;
import yvs.connexion.Loggin;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.navigation.HistoriquePages;
import yvs.util.Constantes;
import static yvs.util.Constantes.USE_PRETTY;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class Navigations implements Serializable {

    @EJB
    public DaoInterfaceLocal dao;
    private static final String CONFIG_PATH = "pretty-config.xml";
    private PrettyConfig config;

    private String chemin = "Accueil ";
    private int activeIndex = 0;
    /**
     * command module
     */
    private boolean modDonneBase;
    private boolean modGescom, modProduction, modCompta, modRh, modCrm, modMutuelle, modParam, modStat, modProj;
    /**
     * End command module
     */
    private boolean welcome;
    /**
     * module données de bases
     */
    private boolean smenUser, smenProduit, smenFamille, smenDepots, smenNiveauAcces, smenJournaux,
            smenMessagerie, smenGroupePublic, smenCompte, smenDico, smenModRef, smenCaisses,
            smenParamBase, smenTemplateProd, smenGroupe, smenCondit, smenHistUser, smencodeBarre;
    /**
     * End module données de bases
     */

    /**
     * command module GHR
     */
    private boolean smenEntretien, smenCandidature, smenQuestionnaire;
    private boolean smenEmploye, smenAffectation, smenClassement, smenPresence, smenCategorie, smenEchelon, smenConventionEmp,
            smenPlaninTravail, smenTypeChomage, smenChomageEmps, smenParamSanction, smenSanctionEmps,
            smenConvesionCollective, smenGrhDepartement, smenQualification, smenJourFerie, smenParamGeneral, smenDemandes,
            smenposteDeTravail, smenConge, smenFormation, smenMission, smenTache, smenContratEmps, smenDashBoardGrh,
            smenRegleSalaire, smenStructureSalaire, smenBulletinS, smenTacheEmps, smenStatistiqueGRH, smenParamNote, smenPlanPrelevement, smenNoteFrais, smengesRetenue,
            smenAssureur, smenAssurance, smenPrelevement, smenRapportsPaie, smenParamCompta;

    private boolean blocSMParam, blocSMPersonnel, blocSMSalaire, blocSMStat;
    /**
     * End command module GRH
     */

    /**
     * command module COM
     */
    private boolean smenParamCom, smenParamComSec, smenPersonnel, smenDepotCom, smenPointVente, smenCreneauPersonnel, smenDashboardObjectif,
            smentaxeArticle, smenRemise, smenRistourne, smenPlanRemise, smenPlanRistourne, smenCommission, smenUpdateVente, smenDashboardVente,
            smenTiersTemplateCom, smenTiersCom, smenClientCom, smenFournisseurCom, smenReservStock, smenBalAgeClient, smenReconditionnement,
            smenFicheAppro, smenBonCommandAchat, smenFactureAchat, smenFactureAvoirAchat, smenBonLivraisonAchat, smenFactureRetourAchat,
            smenBonCommandVente, smenFactureVente, smenEnteteVente, smenFactureAvoirVente, smenBonLivraisonVente, smenFactureRetourVente,
            smenEntree, smenSortie, smenOrdreTransfert, smenTransfert, smenInventaire, smenStockArticle, smenValorisationStock, smenCalculCommission,
            smenOperationDivers, smenPieceTresorerie, smenCategorieClient, smenDashBoardCom, smenDashBoardClient, smenEtatsCom, smenparamObjectif, smenObjectif,
            smenParamRation, smenAttribRation, smenDashBoardStock, smenBonProvisoire, smenDashBoardFsseur, smenClotureCom, smenPointLivraison,
            smenEcartVente;
    /**
     * End command module COM
     */

    /**
     * Command Production*
     */
    private boolean smenDtNomenclature, smenDtGamme, smenDtPosteCharge, smenDtEquipeProd,
            smenPlanifPic, smenPlanifPdp, smenPlanifPdc, smenDPlanifMrp, smenConditionnement,
            smenPlanifCal, smenOfSuivi, smenParamProd, smenUniteMesure, smenDasbBoardProd;
    /**
     * End Command Production*
     */
    /**
     * command module MUT
     */
    private boolean smenParamMut, smenMutuelle, smenExercice, smenMutualiste, smenOperation, smenEpargne, smenCreneauEquipe, smenSiteProd,
            smenCredit, smenCloturePer, smenEvenement, smenEcheancier, smenAvance, smenPrime, smenSituationCompte,
            smenPaie, smenInteret, smenSimulerCredit, smenTypeCredit, smenSalaire, smenRapportsMut, smenCloturePeriode;
    /**
     * End command module MUT
     *
     */
    /**
     * Finance et comptabilité
     */
    private boolean smenPieceCaiss, smenRegVente, smenMenVente, smenSuiviRegVente, smenRegAchat, smenRegVirement, smenAcompteFsseur, smenCreditFsseur,
            smenSaisieJournal, smenModelSaisie, smenLettrageCompta, smenRegMiss, smenRapportCompta, smenOperationClient, smenAcompteClient, smenCreditClient,
            smenOperationFsseur, smenPlanAnal, smenCentreAnal, smenSuiviRegVenteOld, smenMajComptable, smenMouvementCaisse, smenDasbBoardCaisse;
    /**
     * End Finance et comptabilité
     */
    /**
     * command module Données de base
     */
    private boolean categorie, smenProfilUser;
    /**
     * End command module Données de base
     */
    /**
     * Paramétrage génaral
     */
    private boolean smenAgence, smenSociete, smenImportExport, smenWorkflow, smenWarning;
    private boolean smenParamStat, smenTbEmps, smenModelForm;

    /**
     * command module Statistique
     */
    private boolean smenAudiStock, smenEtatExport;
    /**
     * End command module Statistique
     */

    private Stack<Menus> pileForward;
    private Stack<Menus> pileNext;
    private byte callNavigation = 0; //marqueur du nombre d'appel de la méthode de navigation
//    private boolean displayBlocMenu, displayBlocApps;
//    private String classSubMen = "subAllMenu";
    private String lastPage, lastModule, lastTitre;
    private int selectPage;

    public Navigations() {
        pileForward = new Stack<>();
        pileNext = new Stack<>();
    }

    private Managed managedSession;

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public boolean isSmenDasbBoardProd() {
        return smenDasbBoardProd;
    }

    public void setSmenDasbBoardProd(boolean smenDasbBoardProd) {
        this.smenDasbBoardProd = smenDasbBoardProd;
    }

    public boolean isSmenEcartVente() {
        return smenEcartVente;
    }

    public void setSmenEcartVente(boolean smenEcartVente) {
        this.smenEcartVente = smenEcartVente;
    }

    public boolean isSmenSiteProd() {
        return smenSiteProd;
    }

    public void setSmenSiteProd(boolean smenSiteProd) {
        this.smenSiteProd = smenSiteProd;
    }

    public boolean isSmenPointLivraison() {
        return smenPointLivraison;
    }

    public void setSmenPointLivraison(boolean smenPointLivraison) {
        this.smenPointLivraison = smenPointLivraison;
    }

    public boolean isSmenClotureCom() {
        return smenClotureCom;
    }

    public void setSmenClotureCom(boolean smenClotureCom) {
        this.smenClotureCom = smenClotureCom;
    }

    public boolean isSmenDasbBoardCaisse() {
        return smenDasbBoardCaisse;
    }

    public boolean isSmenDashboardVente() {
        return smenDashboardVente;
    }

    public void setSmenDashboardVente(boolean smenDashboardVente) {
        this.smenDashboardVente = smenDashboardVente;
    }

    public boolean isSmenLettrageCompta() {
        return smenLettrageCompta;
    }

    public void setSmenLettrageCompta(boolean smenLettrageCompta) {
        this.smenLettrageCompta = smenLettrageCompta;
    }

    public boolean isSmenAcompteFsseur() {
        return smenAcompteFsseur;
    }

    public void setSmenAcompteFsseur(boolean smenAcompteFsseur) {
        this.smenAcompteFsseur = smenAcompteFsseur;
    }

    public boolean isSmenCreditFsseur() {
        return smenCreditFsseur;
    }

    public void setSmenCreditFsseur(boolean smenCreditFsseur) {
        this.smenCreditFsseur = smenCreditFsseur;
    }

    public boolean isSmenDashBoardFsseur() {
        return smenDashBoardFsseur;
    }

    public void setSmenDashBoardFsseur(boolean smenDashBoardFsseur) {
        this.smenDashBoardFsseur = smenDashBoardFsseur;
    }

    public boolean isSmenMouvementCaisse() {
        return smenMouvementCaisse;
    }

    public void setSmenMouvementCaisse(boolean smenMouvementCaisse) {
        this.smenMouvementCaisse = smenMouvementCaisse;
    }

    public boolean isSmenAcompteClient() {
        return smenAcompteClient;
    }

    public void setSmenAcompteClient(boolean smenAcompteClient) {
        this.smenAcompteClient = smenAcompteClient;
    }

    public boolean isSmenCreditClient() {
        return smenCreditClient;
    }

    public void setSmenCreditClient(boolean smenCreditClient) {
        this.smenCreditClient = smenCreditClient;
    }

    public boolean isSmenMenVente() {
        return smenMenVente;
    }

    public void setSmenMenVente(boolean smenMenVente) {
        this.smenMenVente = smenMenVente;
    }

    public boolean isSmenCreneauEquipe() {
        return smenCreneauEquipe;
    }

    public void setSmenCreneauEquipe(boolean smenCreneauEquipe) {
        this.smenCreneauEquipe = smenCreneauEquipe;
    }

    public boolean isSmenCloturePeriode() {
        return smenCloturePeriode;
    }

    public void setSmenCloturePeriode(boolean smenCloturePeriode) {
        this.smenCloturePeriode = smenCloturePeriode;
    }

    public boolean isSmenCentreAnal() {
        return smenCentreAnal;
    }

    public void setSmenCentreAnal(boolean smenCentreAnal) {
        this.smenCentreAnal = smenCentreAnal;
    }

    public boolean isSmenEtatExport() {
        return smenEtatExport;
    }

    public void setSmenEtatExport(boolean smenEtatExport) {
        this.smenEtatExport = smenEtatExport;
    }

    public boolean isSmenAudiStock() {
        return smenAudiStock;
    }

    public boolean isSmenConventionEmp() {
        return smenConventionEmp;
    }

    public boolean isSmenUniteMesure() {
        return smenUniteMesure;
    }

    public boolean isSmenSimulerCredit() {
        return smenSimulerCredit;
    }

    public void setSmenSimulerCredit(boolean smenSimulerCredit) {
        this.smenSimulerCredit = smenSimulerCredit;
    }

    public boolean isSmenTypeCredit() {
        return smenTypeCredit;
    }

    public void setSmenTypeCredit(boolean smenTypeCredit) {
        this.smenTypeCredit = smenTypeCredit;
    }

    public boolean isSmenSituationCompte() {
        return smenSituationCompte;
    }

    public void setSmenSituationCompte(boolean smenSituationCompte) {
        this.smenSituationCompte = smenSituationCompte;
    }

    public void setSmenUniteMesure(boolean smenUniteMesure) {
        this.smenUniteMesure = smenUniteMesure;
    }

    public boolean isSmenParamProd() {
        return smenParamProd;
    }

    public void setSmenParamProd(boolean smenParamProd) {
        this.smenParamProd = smenParamProd;
    }

    public boolean isSmenParamBase() {
        return smenParamBase;
    }

    public void setSmenParamBase(boolean smenParamBase) {
        this.smenParamBase = smenParamBase;
    }

    public boolean isSmenEtatsCom() {
        return smenEtatsCom;
    }

    public void setSmenEtatsCom(boolean smenEtatsCom) {
        this.smenEtatsCom = smenEtatsCom;
    }

    public boolean isSmenRegMiss() {
        return smenRegMiss;
    }

    public void setSmenRegMiss(boolean smenRegMiss) {
        this.smenRegMiss = smenRegMiss;
    }

    public boolean isSmenDico() {
        return smenDico;
    }

    public void setSmenDico(boolean smenDico) {
        this.smenDico = smenDico;
    }

    public boolean isSmenUpdateVente() {
        return smenUpdateVente;
    }

    public boolean isSmenEnteteVente() {
        return smenEnteteVente;
    }

    public void setSmenEnteteVente(boolean smenEnteteVente) {
        this.smenEnteteVente = smenEnteteVente;
    }

    public boolean isSmenModRef() {
        return smenModRef;
    }

    public void setSmenModRef(boolean smenModRef) {
        this.smenModRef = smenModRef;
    }

    public boolean isSmenDashBoardCom() {
        return smenDashBoardCom;
    }

    public boolean isSmenDashBoardStock() {
        return smenDashBoardStock;
    }

    public void setSmenDashBoardCom(boolean smenDashBoardCom) {
        this.smenDashBoardCom = smenDashBoardCom;
    }

    public boolean isSmenBalAgeClient() {
        return smenBalAgeClient;
    }

    public boolean isSmenDashBoardClient() {
        return smenDashBoardClient;
    }

    public boolean isSmenCaisses() {
        return smenCaisses;
    }

    public boolean isSmenSortie() {
        return smenSortie;
    }

    public void setSmenSortie(boolean smenSortie) {
        this.smenSortie = smenSortie;
    }

    public boolean isSmenParamMut() {
        return smenParamMut;
    }

    public void setSmenParamMut(boolean smenParamMut) {
        this.smenParamMut = smenParamMut;
    }

    public boolean isSmenMutuelle() {
        return smenMutuelle;
    }

    public void setSmenMutuelle(boolean smenMutuelle) {
        this.smenMutuelle = smenMutuelle;
    }

    public boolean isSmenExercice() {
        return smenExercice;
    }

    public void setSmenExercice(boolean smenExercice) {
        this.smenExercice = smenExercice;
    }

    public boolean isSmenMutualiste() {
        return smenMutualiste;
    }

    public void setSmenMutualiste(boolean smenMutualiste) {
        this.smenMutualiste = smenMutualiste;
    }

    public boolean isSmenOperation() {
        return smenOperation;
    }

    public boolean isSmenCategorieClient() {
        return smenCategorieClient;
    }

    public void setSmenCategorieClient(boolean smenCategorieClient) {
        this.smenCategorieClient = smenCategorieClient;
    }

    public void setSmenOperation(boolean smenOperation) {
        this.smenOperation = smenOperation;
    }

    public boolean isSmenEpargne() {
        return smenEpargne;
    }

    public void setSmenEpargne(boolean smenEpargne) {
        this.smenEpargne = smenEpargne;
    }

    public boolean isSmenCredit() {
        return smenCredit;
    }

    public void setSmenCredit(boolean smenCredit) {
        this.smenCredit = smenCredit;
    }

    public boolean isSmenCloturePer() {
        return smenCloturePer;
    }

    public void setSmenCloturePer(boolean smenCloturePer) {
        this.smenCloturePer = smenCloturePer;
    }

    public boolean isSmenEvenement() {
        return smenEvenement;
    }

    public void setSmenEvenement(boolean smenEvenement) {
        this.smenEvenement = smenEvenement;
    }

    public boolean isSmenEcheancier() {
        return smenEcheancier;
    }

    public void setSmenEcheancier(boolean smenEcheancier) {
        this.smenEcheancier = smenEcheancier;
    }

    public boolean isSmenAvance() {
        return smenAvance;
    }

    public void setSmenAvance(boolean smenAvance) {
        this.smenAvance = smenAvance;
    }

    public boolean isSmenPrime() {
        return smenPrime;
    }

    public void setSmenPrime(boolean smenPrime) {
        this.smenPrime = smenPrime;
    }

    public boolean isSmenPaie() {
        return smenPaie;
    }

    public void setSmenPaie(boolean smenPaie) {
        this.smenPaie = smenPaie;
    }

    public boolean isSmenSalaire() {
        return smenSalaire;
    }

    public void setSmenSalaire(boolean smenSalaire) {
        this.smenSalaire = smenSalaire;
    }

    public boolean isSmenRapportsMut() {
        return smenRapportsMut;
    }

    public boolean isSmenInteret() {
        return smenInteret;
    }

    public void setSmenInteret(boolean smenInteret) {
        this.smenInteret = smenInteret;
    }

    public boolean isSmenModelForm() {
        return smenModelForm;
    }

    public void setSmenModelForm(boolean smenModelForm) {
        this.smenModelForm = smenModelForm;
    }

    public boolean isSmenWorkflow() {
        return smenWorkflow;
    }

    public boolean isSmenWarning() {
        return smenWarning;
    }

    public boolean isSmenAgence() {
        return smenAgence;
    }

    public boolean isSmenImportExport() {
        return smenImportExport;
    }

    public boolean isSmenMessagerie() {
        return smenMessagerie;
    }

    public boolean isSmenPrelevement() {
        return smenPrelevement;
    }

    public boolean isSmenGroupePublic() {
        return smenGroupePublic;
    }

    public boolean isSmenNiveauAcces() {
        return smenNiveauAcces;
    }

    public boolean isSmenJournaux() {
        return smenJournaux;
    }

    public boolean isSmenPlanAnal() {
        return smenPlanAnal;
    }

    public boolean isSmenSociete() {
        return smenSociete;
    }

    public boolean isSmenTypeChomage() {
        return smenTypeChomage;
    }

    public boolean isSmenSanctionEmps() {
        return smenSanctionEmps;
    }

    public boolean isSmenParamSanction() {
        return smenParamSanction;
    }

    public boolean isSmenChomageEmps() {
        return smenChomageEmps;
    }

    public void setSmenSociete(boolean smenSociete) {
        this.smenSociete = smenSociete;
    }

    public boolean isSmenDashBoardGrh() {
        return smenDashBoardGrh;
    }

    public void setSmenDashBoardGrh(boolean smenDashBoardGrh) {
        this.smenDashBoardGrh = smenDashBoardGrh;
    }

    public boolean isSmenStatistiqueGRH() {
        return smenStatistiqueGRH;
    }

    public boolean isSmenParamCompta() {
        return smenParamCompta;
    }

    public void setSmenStatistiqueGRH(boolean smenStatistiqueGRH) {
        this.smenStatistiqueGRH = smenStatistiqueGRH;
    }
//
//    public boolean isDisplayBlocApps() {
//        return displayBlocApps;
//    }
//
//    public boolean isDisplayBlocMenu() {
//        return displayBlocMenu;
//    }

    public boolean isModDonneBase() {
        return modDonneBase;
    }

    public boolean isModGescom() {
        return modGescom;
    }

    public boolean isModProduction() {
        return modProduction;
    }

    public boolean isModCompta() {
        return modCompta;
    }

    public boolean isModRh() {
        return modRh;
    }

    public boolean isModCrm() {
        return modCrm;
    }

    public boolean isModMutuelle() {
        return modMutuelle;
    }

    public boolean isModProj() {
        return modProj;
    }

    public boolean isModParam() {
        return modParam;
    }

    public boolean isModStat() {
        return modStat;
    }

    public boolean isSmenEmploye() {
        return smenEmploye;
    }

    public boolean isSmenAffectation() {
        return smenAffectation;
    }

    public boolean isSmenClassement() {
        return smenClassement;
    }

    public boolean isSmenPresence() {
        return smenPresence;
    }

    public boolean isSmenCategorie() {
        return smenCategorie;
    }

    public boolean isSmenEchelon() {
        return smenEchelon;
    }

    public boolean isSmenConvesionCollective() {
        return smenConvesionCollective;
    }

    public boolean isSmenGrhDepartement() {
        return smenGrhDepartement;
    }

    public boolean isSmenQualification() {
        return smenQualification;
    }

    public boolean isSmenJourFerie() {
        return smenJourFerie;
    }

    public boolean isSmenParamGeneral() {
        return smenParamGeneral;
    }

    public byte getCallNavigation() {
        return callNavigation;
    }

    public boolean isCategorie() {
        return categorie;
    }

    public boolean isSmenposteDeTravail() {
        return smenposteDeTravail;
    }

    public boolean isSmenConge() {
        return smenConge;
    }

    public boolean isSmenFormation() {
        return smenFormation;
    }

    public boolean isSmenMission() {
        return smenMission;
    }

    public boolean isSmenStructureSalaire() {
        return smenStructureSalaire;
    }

    public boolean isSmenRegleSalaire() {
        return smenRegleSalaire;
    }

    public boolean isSmenBulletinS() {
        return smenBulletinS;
    }

    public boolean isSmenTache() {
        return smenTache;
    }

    public boolean isSmenDemandes() {
        return smenDemandes;
    }

    public boolean isSmenGroupe() {
        return smenGroupe;
    }

    public boolean isSmenCondit() {
        return smenCondit;
    }

    public boolean isSmenFamille() {
        return smenFamille;
    }

    public boolean isSmenTemplateProd() {
        return smenTemplateProd;
    }

    public boolean isSmenProduit() {
        return smenProduit;
    }

    public boolean isSmenDepots() {
        return smenDepots;
    }

    public boolean isSmenPlaninTravail() {
        return smenPlaninTravail;
    }

    public boolean isSmenContratEmps() {
        return smenContratEmps;
    }

    public boolean isSmenTacheEmps() {
        return smenTacheEmps;
    }

    public boolean isSmenObjectif() {
        return smenObjectif;
    }

    public boolean isSmenDashboardObjectif() {
        return smenDashboardObjectif;
    }

    public void setSmenDashboardObjectif(boolean smenDashboardObjectif) {
        this.smenDashboardObjectif = smenDashboardObjectif;
    }

    public boolean isSmenparamObjectif() {
        return smenparamObjectif;
    }

    public boolean isSmenReconditionnement() {
        return smenReconditionnement;
    }

    public void setSmenReconditionnement(boolean smenReconditionnement) {
        this.smenReconditionnement = smenReconditionnement;
    }

    public boolean isBlocSMParam() {
        return blocSMParam;
    }

    public boolean isBlocSMPersonnel() {
        return blocSMPersonnel;
    }

    public boolean isBlocSMSalaire() {
        return blocSMSalaire;
    }

    public boolean isBlocSMStat() {
        return blocSMStat;
    }

    public boolean isSmenParamNote() {
        return smenParamNote;
    }

    public boolean isSmenPlanPrelevement() {
        return smenPlanPrelevement;
    }

    public boolean isSmenNoteFrais() {
        return smenNoteFrais;
    }

    public boolean isSmengesRetenue() {
        return smengesRetenue;
    }

    public boolean isSmenAssurance() {
        return smenAssurance;
    }

    public boolean isSmenAssureur() {
        return smenAssureur;
    }

    public boolean isSmenUser() {
        return smenUser;
    }

    public boolean isSmenHistUser() {
        return smenHistUser;
    }

    public boolean isSmencodeBarre() {
        return smencodeBarre;
    }

    public int getSelectPage() {
        return selectPage;
    }

    public void setSelectPage(int selectPage) {
        this.selectPage = selectPage;
    }

    public boolean isSmenProfilUser() {
        return smenProfilUser;
    }

    public boolean isSmenParamStat() {
        return smenParamStat;
    }

    public boolean isSmenTbEmps() {
        return smenTbEmps;
    }

    public boolean isSmenRapportsPaie() {
        return smenRapportsPaie;
    }

    public boolean isSmenEntretien() {
        return smenEntretien;
    }

    public boolean isSmenCandidature() {
        return smenCandidature;
    }

    public boolean isSmenQuestionnaire() {
        return smenQuestionnaire;
    }

    public boolean isSmenParamCom() {
        return smenParamCom;
    }

    public boolean isSmenParamComSec() {
        return smenParamComSec;
    }

    public boolean isSmenPersonnel() {
        return smenPersonnel;
    }

    public boolean isSmenDepotCom() {
        return smenDepotCom;
    }

    public boolean isSmenPointVente() {
        return smenPointVente;
    }

    public boolean isSmenCreneauPersonnel() {
        return smenCreneauPersonnel;
    }

    public boolean isSmentaxeArticle() {
        return smentaxeArticle;
    }

    public boolean isSmenRemise() {
        return smenRemise;
    }

    public boolean isSmenRistourne() {
        return smenRistourne;
    }

    public boolean isSmenPlanRemise() {
        return smenPlanRemise;
    }

    public boolean isSmenPlanRistourne() {
        return smenPlanRistourne;
    }

    public boolean isSmenCommission() {
        return smenCommission;
    }

    public boolean isSmenTiersTemplateCom() {
        return smenTiersTemplateCom;
    }

    public boolean isSmenTiersCom() {
        return smenTiersCom;
    }

    public boolean isSmenClientCom() {
        return smenClientCom;
    }

    public boolean isSmenFournisseurCom() {
        return smenFournisseurCom;
    }

    public boolean isSmenFicheAppro() {
        return smenFicheAppro;
    }

    public boolean isSmenBonCommandAchat() {
        return smenBonCommandAchat;
    }

    public boolean isSmenFactureAchat() {
        return smenFactureAchat;
    }

    public boolean isSmenFactureAvoirAchat() {
        return smenFactureAvoirAchat;
    }

    public boolean isSmenBonLivraisonAchat() {
        return smenBonLivraisonAchat;
    }

    public boolean isSmenFactureRetourAchat() {
        return smenFactureRetourAchat;
    }

    public boolean isSmenBonCommandVente() {
        return smenBonCommandVente;
    }

    public boolean isSmenFactureVente() {
        return smenFactureVente;
    }

    public boolean isSmenFactureAvoirVente() {
        return smenFactureAvoirVente;
    }

    public boolean isSmenBonLivraisonVente() {
        return smenBonLivraisonVente;
    }

    public boolean isSmenFactureRetourVente() {
        return smenFactureRetourVente;
    }

    public boolean isSmenEntree() {
        return smenEntree;
    }

    public boolean isSmenOrdreTransfert() {
        return smenOrdreTransfert;
    }

    public boolean isSmenTransfert() {
        return smenTransfert;
    }

    public boolean isSmenInventaire() {
        return smenInventaire;
    }

    public boolean isSmenStockArticle() {
        return smenStockArticle;
    }

    public boolean isSmenReservStock() {
        return smenReservStock;
    }

    public void setSmenReservStock(boolean smenReservStock) {
        this.smenReservStock = smenReservStock;
    }

    public boolean isSmenValorisationStock() {
        return smenValorisationStock;
    }

    public boolean isSmenOperationDivers() {
        return smenOperationDivers;
    }

    public boolean isSmenBonProvisoire() {
        return smenBonProvisoire;
    }

    public boolean isSmenCalculCommission() {
        return smenCalculCommission;
    }

    public void setSmenCalculCommission(boolean smenCalculCommission) {
        this.smenCalculCommission = smenCalculCommission;
    }

    public boolean isSmenParamRation() {
        return smenParamRation;
    }

    public boolean isSmenAttribRation() {
        return smenAttribRation;
    }

    public boolean isSmenPieceTresorerie() {
        return smenPieceTresorerie;
    }

    public String getLastModule() {
        return lastModule;
    }

    public void setLastModule(String lastModule) {
        this.lastModule = lastModule;
    }

    public String getLastTitre() {
        return lastTitre;
    }

    public void setLastTitre(String lastTitre) {
        this.lastTitre = lastTitre;
    }

    public String getLastPage() {
        return lastPage;
    }

    public boolean isSmenDtNomenclature() {
        return smenDtNomenclature;
    }

    public boolean isSmenDtGamme() {
        return smenDtGamme;
    }

    public boolean isSmenDtPosteCharge() {
        return smenDtPosteCharge;
    }

    public boolean isSmenDtEquipeProd() {
        return smenDtEquipeProd;
    }

    public boolean isSmenPlanifPic() {
        return smenPlanifPic;
    }

    public boolean isSmenPlanifPdp() {
        return smenPlanifPdp;
    }

    public boolean isSmenPlanifPdc() {
        return smenPlanifPdc;
    }

    public boolean isSmenDPlanifMrp() {
        return smenDPlanifMrp;
    }

    public boolean isSmenPlanifCal() {
        return smenPlanifCal;
    }

    public boolean isSmenOfSuivi() {
        return smenOfSuivi;
    }

    public boolean isSmenConditionnement() {
        return smenConditionnement;
    }

    public boolean isSmenCompte() {
        return smenCompte;
    }

    public boolean isSmenPieceCaiss() {
        return smenPieceCaiss;
    }

    public boolean isSmenOperationFsseur() {
        return smenOperationFsseur;
    }

    public void setSmenOperationFsseur(boolean smenOperationFsseur) {
        this.smenOperationFsseur = smenOperationFsseur;
    }

    public boolean isSmenOperationClient() {
        return smenOperationClient;
    }

    public boolean isSmenRegAchat() {
        return smenRegAchat;
    }

    public boolean isSmenRegVirement() {
        return smenRegVirement;
    }

    public boolean isSmenRegVente() {
        return smenRegVente;
    }

    public boolean isSmenSuiviRegVenteOld() {
        return smenSuiviRegVenteOld;
    }

    public boolean isSmenMajComptable() {
        return smenMajComptable;
    }

    public boolean isSmenSuiviRegVente() {
        return smenSuiviRegVente;
    }

    public boolean isSmenRapportCompta() {
        return smenRapportCompta;
    }

    public boolean isSmenSaisieJournal() {
        return smenSaisieJournal;
    }

    public boolean isSmenModelSaisie() {
        return smenModelSaisie;
    }

    public boolean isWelcome() {
        return welcome;
    }

    public Object giveObjectOnView(String id) {
        return (Object) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
    }

    public Object giveManagedBean(Class classe) {
        String st = classe.getSimpleName();
        String f = st.substring(0, 1);
        if (st != null) {
            return (Object) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(f.toLowerCase().concat(st.substring(1)));
        }
        return null;
    }

    //cas de navigation des module
    public void naviguationModule(String chemin, String module, boolean empile) {
        if (USE_PRETTY) {
            Loggin s = (Loggin) giveManagedBean(Loggin.class);
            if (s != null) {
                s.saveDataNavigation(getNameModule(module, 1));
            }
        } else {
            navigue("", module, "", empile);
        }
        this.chemin = chemin;

        lastPage = "";
        lastModule = module;
        lastTitre = "";

        addPageInList(this.chemin, module, "", module, "");
        update("formulaire_module_page");
    }

    public void naviguationApps(String namePage, String modules, String pages, boolean empile) {
        naviguationApps(namePage, modules, pages, empile, null);
    }

    public void naviguationApps(String namePage, String modules, String pages, boolean empile, Managed bean) {
        try {
            managedSession = bean;
        } catch (Exception e) {
            managedSession = null;
        }
        if (USE_PRETTY) {
            this.chemin = getNameModule(modules) + (namePage != null ? namePage.trim().length() > 0 ? " / " + namePage : "" : "");
            Loggin s = (Loggin) giveManagedBean(Loggin.class);
            if (s != null) {
                s.saveDataNavigation(getNameModule(modules, 0) + " : " + namePage);
            }
        } else {
            navigue(modules, pages, "", empile);
            this.chemin += " / " + namePage;
        }
        lastPage = pages;
        lastModule = modules;
        lastTitre = namePage;

        addPageInList(this.chemin, modules, pages, modules, pages);
    }

    public void naviguationView(String namePage, String modules, String pages, boolean empile) {
        naviguationView(namePage, modules, pages, empile, false, null, new Date());
    }

    public void naviguationView(String namePage, String modules, String pages, boolean empile, Boolean isWarning, String modelWarning, Date debutWarning) {
        naviguationView(namePage, modules, pages, empile, isWarning, modelWarning, debutWarning, null, null);
    }

    public void naviguationView(String namePage, String modules, String pages, boolean empile, Boolean isWarning, String modelWarning, Date debutWarning, Long idWorkflow, String natureWarning, boolean param_warning_view_all) {
        Object idsWarning = null;
        if (idWorkflow != null ? idWorkflow > 0 : false) {
            YvsAgences currentAgence = ((YvsAgences) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("agenc"));
            YvsUsersAgence currentUser = ((YvsUsersAgence) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
            List<Object> params = new ArrayList<>();
            params.add(currentUser.getUsers() != null ? currentUser.getUsers().getId() : 0);
            params.add(idWorkflow != null ? idWorkflow : 0);
            params.add(natureWarning);
            String query = "SELECT DISTINCT y.id_element FROM yvs_workflow_alertes y "
                    + "INNER JOIN yvs_workflow_model_doc m ON y.model_doc = m.id "
                    + "INNER JOIN yvs_agences a ON y.agence = a.id "
                    + "INNER JOIN yvs_warning_model_doc w ON w.societe = a.societe AND w.model = m.id "
                    + "INNER JOIN yvs_user_view_alertes u ON u.document_type = m.id "
                    + "WHERE (COALESCE((current_date - COALESCE(y.date_doc, current_date)), 0) > COALESCE(w.ecart, 0)) AND COALESCE(y.actif, TRUE) IS TRUE AND u.actif IS TRUE AND u.users = ? AND m.id = ? AND y.nature_alerte = ?";
            if (!param_warning_view_all) {
                params.add(currentAgence != null ? currentAgence.getId() : 0);
                query += " AND a.id = ?";
            } else {
                params.add(currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0);
                query += " AND a.societe = ?";
            }
            idsWarning = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
        }
        naviguationView(namePage, modules, pages, empile, isWarning, modelWarning, debutWarning, idsWarning, natureWarning);
    }

    public void naviguationView(String namePage, String modules, String pages, boolean empile, Boolean isWarning, String modelWarning, Date debutWarning, Object idsWarning, String natureWarning) {
        if (modules != null) {
            lastPage = pages;
            lastModule = modules;
            lastTitre = namePage;
            this.chemin = getNameModule(modules) + (namePage != null ? namePage.trim().length() > 0 ? " / " + namePage : "" : "");
            Loggin s = (Loggin) giveManagedBean(Loggin.class);
            if (s != null) {
                s.saveDataNavigation(getNameModule(modules, 0) + " : " + namePage);
            }
            addPageInList(this.chemin, modules, pages, modules, pages);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isWarning", isWarning);
            if (isWarning != null ? isWarning : false) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("modelWarning", modelWarning);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("debutWarning", debutWarning);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("natureWarning", natureWarning);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idsWarning", idsWarning);
            }
            if (USE_PRETTY) {
                navigue(urlMapping(pages));
            } else {
                navigue(modules, pages, "", empile);
            }
        }
    }

    public boolean inToFavoris() {
        Loggin w = (Loggin) giveManagedBean(Loggin.class);
        if (w != null) {
            return w.inToFavoris(lastPage);
        }
        return false;
    }

    public void actionFavoris() {
        Loggin w = (Loggin) giveManagedBean(Loggin.class);
        if (w != null) {
            if (!w.inToFavoris(lastPage)) {
                w.addFavoris(lastTitre, lastModule, lastPage);
            } else {
                w.removeFavoris(lastTitre, lastModule, lastPage);
            }
            update("action_favoris");
            update("data_favoris_users");
        }
    }

    public String getNameModule(String code) {
        return getNameModule(code, 1);
    }

    public String getNameModule(String code, int type) {
        switch (code) {
            case "modDonneBase":
                switch (type) {
                    case 1:
                        return "Données de bases";
                    default:
                        return "DB";
                }
            case "modGescom":
                switch (type) {
                    case 1:
                        return "Commercial";
                    default:
                        return "GESCOM";
                }
            case "modProduction":
                switch (type) {
                    case 1:
                        return "Productions";
                    default:
                        return "PROD";
                }
            case "modCompta":
                switch (type) {
                    case 1:
                        return "Comptabilité & Finance";
                    default:
                        return "COMPTA";
                }
            case "modRh":
                switch (type) {
                    case 1:
                        return "Ressource Humaine";
                    default:
                        return "RH";
                }
            case "modMutuelle":
                switch (type) {
                    case 1:
                        return "Mutuelle";
                    default:
                        return "MUT";
                }
            case "modProj":
                switch (type) {
                    case 1:
                        return "Gestion Projet";
                    default:
                        return "PROJ";
                }
            case "modParam":
                switch (type) {
                    case 1:
                        return "Paramétrage";
                    default:
                        return "SETTING";
                }
            case "modStat":
                switch (type) {
                    case 1:
                        return "Statistiques";
                    default:
                        return "STAT";
                }
            case "modCrm":
                switch (type) {
                    case 1:
                        return "Relation Client";
                    default:
                        return "REC";
                }
            default:
                return "Accueil";
        }
    }
//navigation sous modules (élément de menu) 

    private void naviguationSM(String chemin, String modules, String pages, boolean empile) {
        this.chemin = chemin;
        navigue(lastPage, modules, pages, empile);
//        displayBlocMenu = false;
//        displayBlocApps = true;
        update("contentApps");
//        update("contentSubMenuPanel");
//        classSubMen = "subAllMenu_actif";
    }

    public void navigue(String adresse) {
        try {
            if (adresse != null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + Constantes.ADRESSE + adresse);
            }
        } catch (IOException ex) {
            Logger.getLogger(Navigations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String navigue(String module, String SM1, String SM2, boolean empile) {
        Class<Navigations> cl = Navigations.class;
        Field[] listChamp = cl.getDeclaredFields();
        String name = null;
        Field welcomeF = null;
        boolean moduleTrue = false;
        for (Field f : listChamp) {
            if (f.getType().getName().toLowerCase().equals("boolean")) {
                f.setAccessible(true);
                try {
                    if ((f.getName().equals(SM1) || f.getName().equals(module) || f.getName().equals(SM2)) && !f.getName().equals("welcome")) {
                        f.setBoolean(this, true);
                        moduleTrue = true;
                    } else {
                        f.setBoolean(this, false);
                    }
                    if (f.getName().equals(module)) {
                        f.setBoolean(this, false);
                        name = f.getName();
                    }
                    if (f.getName().equals("welcome")) {
                        welcomeF = f;
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Navigations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try {
            if (!moduleTrue && welcomeF != null) {
                welcomeF.setBoolean(this, true);
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Navigations.class.getName()).log(Level.SEVERE, null, ex);
        }
        update("contentApps");
        return name;
    }

    public Stack<Menus> getPileForward() {
        return pileForward;
    }

    public Stack<Menus> getPileNext() {
        return pileNext;
    }

    public String forward() {
        Menus m = pileForward.pop();
        pileNext.push(m);
        return navigue(m.getPage(), m.getMenu(), "", false);  //le paramètre false signale qu'on ne doit pas empiler cette navigation
    }

    public String next() {
        Menus m = pileNext.pop();
        pileForward.push(m);
        return navigue(m.getPage(), m.getMenu(), "", false);  //le paramètre false signale qu'on ne doit pas empiler cette navigation
    }

    public void update(String id) {
        RequestContext.getCurrentInstance().update(id);
    }

    public void execute(String id) {
        RequestContext.getCurrentInstance().execute(id);
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    //gestion des vues dans un tabview
    List<HistoriquePages> model = new ArrayList<>();
    List<HistoriquePages> favoris = new ArrayList<>();

    public List<HistoriquePages> getFavoris() {
        return favoris;
    }

    public void setFavoris(List<HistoriquePages> favoris) {
        this.favoris = favoris;
    }

    public List<HistoriquePages> getModel() {
        return model;
    }

    public void setModel(List<HistoriquePages> model) {
        this.model = model;
    }

    public void addTab(String id, String content, boolean b1, boolean b2) {
//        model.add(new HistoriquePages(id, content, b1, b2));
//        update("AllApps");
    }

    public void addPageInList(String title, String module, String page, String codeModule, String codePage) {
        if (!findPage(module, page)) {
            HistoriquePages hp = new HistoriquePages();
            hp.setId(calculId());
            hp.setTitlePage(title);
            hp.setModule(module);
            hp.setPage(page);
            hp.setCodeModule(codeModule);
            hp.setCodePage(codePage);
            model.add(hp);
        }
        update("action_favoris");
    }

    private boolean findPage(String module, String page) {
        for (HistoriquePages h : model) {
            if (h.getModule().equals(module) && h.getPage().equals(page)) {
                return true;
            }
        }
        return false;
    }

    private int calculId() {
        Collections.sort(model, new HistoriquePages());
        if (model.isEmpty()) {
            return 1;
        } else {
            return model.get(model.size() - 1).getId() + 1;
        }
    }

    public String navigueInHitorique(int id) {
        int idx = model.indexOf(new HistoriquePages(id, ""));
        if (idx > -1) {
            HistoriquePages hp = model.get(idx);
            if (USE_PRETTY) {
                String adresse = urlMapping(hp.getCodeModule());
                if (hp.getCodePage() != null ? hp.getCodePage().trim().length() > 0 : false) {
                    adresse = urlMapping(hp.getCodePage());
                }
                navigue(adresse);
            } else {
                navigue(hp.getCodeModule(), hp.getCodePage(), "", false);
            }
            this.chemin = hp.getTitlePage();
        }
        return null + "?faces-redirect=true";
    }

    public void deleteInHistorique(int id) {
        int idx = model.indexOf(new HistoriquePages(id, ""));
        if (idx > -1) {
            model.remove(idx);
        }
    }

    public void testJS() {
//            JSONObject jo=new JSONObject();
//            jo.put("key1", "1");
        String jo = "{\"key1\":\"1\"}";
        RequestContext.getCurrentInstance().execute("test('" + jo + "')");
    }

    public void navigueToMutuelle() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/LYMYTZ_MUTUELLE/pages/yvs_accueil.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(Navigations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doNothing() {
    }

    @PostConstruct
    public void configure() {
        final PrettyConfigBuilder builder = new PrettyConfigBuilder();
        try {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF") + FILE_SEPARATOR + CONFIG_PATH;
            File file = new File(path);
            if (file.exists()) {
                InputStream stream = new FileInputStream(file);
                new DigesterPrettyConfigParser().parse(builder, stream);
                config = builder.build();
            }
        } catch (IOException | SAXException ex) {
            Logger.getLogger(Navigations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String urlMapping(String id) {
        // is there a @URLAction annotation on the class?
        try {
            if (config != null) {
                UrlMapping mapping = config.getMappingById(id);
                if (mapping != null) {
                    return mapping.getPattern();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Navigations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/Welcome";
    }

    public boolean pageIsActive(String id) {
        // is there a @URLAction annotation on the class?
        try {
            if (config != null) {
                UrlMapping mapping = config.getMappingById(id);
                if (mapping != null) {
                    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    if (request != null) {
                        String url = request.getRequestURL().toString();
                        return url.contains(mapping.getViewId());
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Navigations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void getModuleUrlIndex() {
        // is there a @URLAction annotation on the class?
        try {
            if (config != null) {
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                if (request != null) {
                    String url = request.getRequestURL().toString();
                    if (url.contains("/donnee_de_base/")) {
                        activeIndex = 0;
                    } else if (url.contains("/gescom/")) {
                        activeIndex = 1;
                    } else if (url.contains("/production/")) {
                        activeIndex = 2;
                    } else if (url.contains("/finance_compta/")) {
                        activeIndex = 3;
                    } else if (url.contains("/grh/")) {
                        activeIndex = 4;
                    } else if (url.contains("/gescrm/")) {
                        activeIndex = 5;
                    } else if (url.contains("/gesmut/")) {
                        activeIndex = 6;
                    } else if (url.contains("/gesproj/")) {
                        activeIndex = 7;
                    } else if (url.contains("/param_general/")) {
                        activeIndex = 8;
                    } else if (url.contains("/stat/")) {
                        activeIndex = 9;
                    }
                    if (activeIndex > 0) {
                        AccordionPanel navigation = (AccordionPanel) giveObjectOnView(":formulaire_module_page:tabNavigation");
                        if (navigation != null) {
                            navigation.setActiveIndex(activeIndex + "");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Navigations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Managed getManagedSession() {
        return managedSession;
    }

    public void setManagedSession(Managed managedSession) {
        this.managedSession = managedSession;
    }

    public void updateResetFiche() {
        if (managedSession != null) {
            managedSession.setMemoriseActionAfterSave(false);
            managedSession.setResetAfterSave(false);

        }
    }

    public void onTabChange(TabChangeEvent event) {
        activeIndex = event.getPhaseId().hashCode();
    }

    public void onTabChangeRemote(int index) {
        activeIndex = index;
    }

    public void desactiveAlerte(Long id, String source) {
        try {
            String query = "UPDATE yvs_workflow_alertes y SET actif = FALSE FROM yvs_workflow_model_doc w WHERE y.model_doc = w.id AND w.titre_doc = ? AND y.id_element = ?";
            dao.requeteLibre(query, new Options[]{new Options(source, 1), new Options(id, 2)});
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void activeAlerte(Long id, String source) {
        try {
            String query = "UPDATE yvs_workflow_alertes y SET actif = TRUE FROM yvs_workflow_model_doc w WHERE y.model_doc = w.id AND w.titre_doc = ? AND y.id_element = ?";
            dao.requeteLibre(query, new Options[]{new Options(source, 1), new Options(id, 2)});
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isAlerte(Long id, String source) {
        try {
            String query = "SELECT COUNT(y.id) FROM yvs_workflow_alertes y INNER JOIN yvs_workflow_model_doc w ON y.model_doc = w.id INNER JOIN yvs_warning_model_doc a ON a.model = w.id WHERE (COALESCE((current_date - COALESCE(y.date_doc, current_date)), 0) > COALESCE(a.ecart, 0)) AND w.titre_doc = ? AND y.id_element = ?";
            Long count = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options(source, 1), new Options(id, 2)});
            return count != null ? count > 0 : false;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
