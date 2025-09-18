/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.ManagedLotReception;
import yvs.commercial.creneau.ManagedTypeCreneau;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.stock.ContenuDocStock;
import yvs.commercial.stock.DocStock;
import yvs.commercial.stock.ManagedReconditionnement;
import yvs.commercial.stock.ManagedTransfertStock;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseTableConversion;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.achat.YvsComArticleApprovisionnement;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidApprovissionnement;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.entity.production.YvsProdParametre;
import yvs.entity.production.base.YvsProdComposantNomenclature;
import yvs.entity.production.base.YvsProdDefaultDepotComposants;
import yvs.entity.production.base.YvsProdGammeArticle;
import yvs.entity.production.base.YvsProdIndicateurOp;
import yvs.entity.production.base.YvsProdNomenclature;
import yvs.entity.production.base.YvsProdOperationsGamme;
import yvs.entity.production.base.YvsProdPosteCharge;
import yvs.entity.production.base.YvsProdPosteOperation;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.entity.production.base.YvsProdValeursQualitative;
import yvs.entity.production.equipe.YvsProdEquipeProduction;
import yvs.entity.production.pilotage.YvsProdComposantOF;
import yvs.entity.production.pilotage.YvsProdComposantOp;
import yvs.entity.production.pilotage.YvsProdConditionnementDeclaration;
import yvs.entity.production.pilotage.YvsProdDeclarationProduction;
import yvs.entity.production.pilotage.YvsProdFluxComposant;
import yvs.entity.production.pilotage.YvsProdOfIndicateurSuivi;
import yvs.entity.production.pilotage.YvsProdOfSuiviFlux;
import yvs.entity.production.pilotage.YvsProdOperationsOF;
import yvs.entity.production.pilotage.YvsProdOrdreFabrication;
import yvs.entity.production.pilotage.YvsProdSessionOf;
import yvs.entity.production.pilotage.YvsProdSessionProd;
import yvs.entity.production.pilotage.YvsProdSuiviOperations;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.UtilGrh;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.entrepot.Depots;
import yvs.production.technique.GammeArticle;
import yvs.production.technique.PosteCharge;
import yvs.production.technique.Nomenclature;
import yvs.production.base.EquipeProduction;
import yvs.production.base.ManagedEquipeProduction;
import yvs.production.base.SiteProduction;
import yvs.production.technique.FicheConditionnement;
import yvs.production.technique.ManagedConditionnement;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public final class ManagedOrdresF extends Managed<OrdreFabrication, YvsProdOrdreFabrication> implements Serializable {

    @ManagedProperty(value = "#{ordreFabrication}")
    private OrdreFabrication ordre;
    private OrdreFabrication sous = new OrdreFabrication();
    private SuiviFlux suiviFlux = new SuiviFlux();
    @ManagedProperty(value = "#{sessionProd}")
    private SessionProd sessionProd;
    private SuiviOperations suiviOperation = new SuiviOperations();
    private OperationsOF operation = new OperationsOF();
    private ComposantsOf composant = new ComposantsOf();
    private DeclarationProduction declaration = new DeclarationProduction();
    private OrdreFabrication program = new OrdreFabrication();

    private List<OrdreFabrication> programs;

    private Date dateLancement = new Date();
    long ids = -10000;

    private boolean suggestionOF; //si on met cet attribut à true, une suggestion d'of est crée pour les produits intermédiares compris dans la nomenclature choisie   
    private boolean delCycle;

    private YvsProdSessionProd currentSession;
    private YvsProdFluxComposant selectedFlux = new YvsProdFluxComposant();
    private YvsBaseDepots current_cible;
    private YvsBaseDepots current_source;
    private YvsProdDeclarationProduction selectDeclaration;
    private YvsProdOperationsOF selectedOp;
    private YvsProdComposantOF selectedComposant;
    private YvsProdOrdreFabrication selectedOf;
    private YvsProdOfSuiviFlux selectedSuiviFlux;

    private String sourcePC;
    boolean initForm = true;
    private boolean source;

    private List<YvsProdOrdreFabrication> listeOrdreF;
    private List<YvsProdDeclarationProduction> declarations;
    private List<YvsProdOrdreFabrication> listeOrdreSelect;
    private List<YvsProdEquipeProduction> equipes;
    private List<YvsProdComposantOF> composantsOF, selectionComposantsOF;

    private Depots depotDest = new Depots();
    private TrancheHoraire trancheHoraire = new TrancheHoraire();

    private String sensConso = "S";
    private double margeNew = 0;

    private List<YvsBaseArticleFournisseur> fournisseurs;
    private List<YvsBaseFournisseur> fournis;
    private List<YvsProdComposantOF> listeMatieres;
    private List<YvsBaseDepots> depots;
    private List<YvsBaseDepots> depotsCible;
    private List<YvsGrhTrancheHoraire> tranchesCible, tranchesSearch;

    //Variable de recherche
    private long depotSessionSearch, equipeSessionSearch, userSessionSearch, equipeDSearch, depotDSearch, trancheDSearch;
    private int siteDSearch;
    private boolean addDateDSearch, groupProd = true;
    private Date dateDebutDSearch = new Date(), dateFinDSearch = new Date(), dateSessionSearch = new Date();
    private String articleDSearch, numeroDSearch, idsDeclaration;
    private String filterState;
    private Date dateFilter1 = new Date(), dateFilter2 = new Date();

    private boolean loadNext = true;
    private boolean displayAll = true;  //Afficher toutes les parties du formulaire
    private boolean displayOpAtTable = false;   //afficher les opération de l'OF sous forme de tableau
    private boolean transfertOf;
    private boolean ordre_achat = false;
    private boolean demande_appro = false;
    private boolean demande_transfert = false;

    private boolean controleTransferOk = true, controleInventaireOk = true, displayColStock, displayColCout, displayColFlux;
    private List<String> allStatutsOf = new ArrayList<>();

    private String typePage;
    private boolean notRemindDeleteDecl;
    private YvsProdParametre paramG;

    private PaginatorResult<YvsProdDeclarationProduction> p_declaration = new PaginatorResult<>();
    private PaginatorResult<YvsProdComposantOF> p_composant = new PaginatorResult<>();
    private PaginatorResult<YvsProdSessionProd> p_sessions = new PaginatorResult<>();

    public ManagedOrdresF() {
        programs = new ArrayList<>();
        listeOrdreF = new ArrayList<>();
        listeMatieres = new ArrayList<>();
        listeOrdreSelect = new ArrayList<>();
        equipes = new ArrayList<>();
        depotsCible = new ArrayList<>();
        tranchesCible = new ArrayList<>();
        declarations = new ArrayList<>();
        tranchesSearch = new ArrayList<>();
        sitesSeach = new ArrayList<>();
        producteurs = new ArrayList<>();
        tranches = new ArrayList<>();
        sessionsProd = new ArrayList<>();
        mesCycles = new ArrayList<>();
        listeFluxComposant = new ArrayList<>();
        depots = new ArrayList<>();
        fournisseurs = new ArrayList<>();
        fournis = new ArrayList<>();
        composantsOF = new ArrayList<>();
        selectionComposantsOF = new ArrayList<>();

        allStatutsOf.add(Constantes.ETAT_PROD_LANCE);
        allStatutsOf.add(Constantes.ETAT_ATTENTE);
        allStatutsOf.add(Constantes.ETAT_ENCOURS);
        allStatutsOf.add(Constantes.ETAT_TERMINE);
        allStatutsOf.add(Constantes.ETAT_CLOTURE);
        allStatutsOf.add(Constantes.ETAT_SUSPENDU);
    }

    public boolean isGroupProd() {
        return groupProd;
    }

    public void setGroupProd(boolean groupProd) {
        this.groupProd = groupProd;
    }

    public PaginatorResult<YvsProdComposantOF> getP_composant() {
        return p_composant;
    }

    public void setP_composant(PaginatorResult<YvsProdComposantOF> p_composant) {
        this.p_composant = p_composant;
    }

    public List<YvsProdComposantOF> getSelectionComposantsOF() {
        return selectionComposantsOF;
    }

    public void setSelectionComposantsOF(List<YvsProdComposantOF> selectionComposantsOF) {
        this.selectionComposantsOF = selectionComposantsOF;
    }

    public List<YvsProdComposantOF> getComposantsOF() {
        return composantsOF;
    }

    public void setComposantsOF(List<YvsProdComposantOF> composantsOF) {
        this.composantsOF = composantsOF;
    }

    public String getIdsDeclaration() {
        return idsDeclaration;
    }

    public void setIdsDeclaration(String idsDeclaration) {
        this.idsDeclaration = idsDeclaration;
    }

    public List<YvsBaseFournisseur> getFournis() {
        return fournis;
    }

    public void setFournis(List<YvsBaseFournisseur> fournis) {
        this.fournis = fournis;
    }

    public List<YvsBaseArticleFournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<YvsBaseArticleFournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    public boolean isDelCycle() {
        return delCycle;
    }

    public void setDelCycle(boolean delCycle) {
        this.delCycle = delCycle;
    }

    public boolean isDisplayOpAtTable() {
        return displayOpAtTable;
    }

    public void setDisplayOpAtTable(boolean displayOpAtTable) {
        this.displayOpAtTable = displayOpAtTable;
    }

    public SessionProd getSessionProd() {
        return sessionProd;
    }

    public void setSessionProd(SessionProd sessionProd) {
        this.sessionProd = sessionProd;
    }

    public List<YvsProdSuiviOperations> getMesCycles() {
        return mesCycles;
    }

    public void setMesCycles(List<YvsProdSuiviOperations> mesCycles) {
        this.mesCycles = mesCycles;
    }

    public PaginatorResult<YvsProdSessionProd> getP_sessions() {
        return p_sessions;
    }

    public void setP_sessions(PaginatorResult<YvsProdSessionProd> p_sessions) {
        this.p_sessions = p_sessions;
    }

    public String getSensConso() {
        return sensConso;
    }

    public long getDepotSessionSearch() {
        return depotSessionSearch;
    }

    public void setDepotSessionSearch(long depotSessionSearch) {
        this.depotSessionSearch = depotSessionSearch;
    }

    public long getEquipeSessionSearch() {
        return equipeSessionSearch;
    }

    public void setEquipeSessionSearch(long equipeSessionSearch) {
        this.equipeSessionSearch = equipeSessionSearch;
    }

    public long getUserSessionSearch() {
        return userSessionSearch;
    }

    public void setUserSessionSearch(long userSessionSearch) {
        this.userSessionSearch = userSessionSearch;
    }

    public Date getDateSessionSearch() {
        return dateSessionSearch;
    }

    public void setDateSessionSearch(Date dateSessionSearch) {
        this.dateSessionSearch = dateSessionSearch;
    }

    public void setSensConso(String sensConso) {
        this.sensConso = sensConso;
    }

    public double getMargeNew() {
        return margeNew;
    }

    public void setMargeNew(double margeNew) {
        this.margeNew = margeNew;
    }

    public List<YvsGrhTrancheHoraire> getTranchesSearch() {
        return tranchesSearch;
    }

    public void setTranchesSearch(List<YvsGrhTrancheHoraire> tranchesSearch) {
        this.tranchesSearch = tranchesSearch;
    }

    public long getIds() {
        return ids;
    }

    public void setIds(long ids) {
        this.ids = ids;
    }

    public String getSourcePC() {
        return sourcePC;
    }

    public void setSourcePC(String sourcePC) {
        this.sourcePC = sourcePC;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public long getDepotDSearch() {
        return depotDSearch;
    }

    public void setDepotDSearch(long depotDSearch) {
        this.depotDSearch = depotDSearch;
    }

    public boolean isAddDateDSearch() {
        return addDateDSearch;
    }

    public void setAddDateDSearch(boolean addDateDSearch) {
        this.addDateDSearch = addDateDSearch;
    }

    public List<Long> getIdsIndicateurs() {
        return idsIndicateurs;
    }

    public void setIdsIndicateurs(List<Long> idsIndicateurs) {
        this.idsIndicateurs = idsIndicateurs;
    }

    public String getSourceActionDepot() {
        return sourceActionDepot;
    }

    public void setSourceActionDepot(String sourceActionDepot) {
        this.sourceActionDepot = sourceActionDepot;
    }

    public List<PosteCharge> getTempPoste() {
        return tempPoste;
    }

    public void setTempPoste(List<PosteCharge> tempPoste) {
        this.tempPoste = tempPoste;
    }

    public int getNumLinePhase() {
        return numLinePhase;
    }

    public void setNumLinePhase(int numLinePhase) {
        this.numLinePhase = numLinePhase;
    }

    public int getNumLineComposant() {
        return numLineComposant;
    }

    public void setNumLineComposant(int numLineComposant) {
        this.numLineComposant = numLineComposant;
    }

    public String getEgaliteSite() {
        return egaliteSite;
    }

    public void setEgaliteSite(String egaliteSite) {
        this.egaliteSite = egaliteSite;
    }

    public List<Integer> getSitesSeach() {
        return sitesSeach;
    }

    public void setSitesSeach(List<Integer> sitesSeach) {
        this.sitesSeach = sitesSeach;
    }

    public List<YvsProdOfSuiviFlux> getListeFlux() {
        return listeFlux;
    }

    public void setListeFlux(List<YvsProdOfSuiviFlux> listeFlux) {
        this.listeFlux = listeFlux;
    }

    public int getSiteDSearch() {
        return siteDSearch;
    }

    public void setSiteDSearch(int siteDSearch) {
        this.siteDSearch = siteDSearch;
    }

    public long getEquipeDSearch() {
        return equipeDSearch;
    }

    public void setEquipeDSearch(long equipeDSearch) {
        this.equipeDSearch = equipeDSearch;
    }

    public long getTrancheDSearch() {
        return trancheDSearch;
    }

    public void setTrancheDSearch(long trancheDSearch) {
        this.trancheDSearch = trancheDSearch;
    }

    public Date getDateDebutDSearch() {
        return dateDebutDSearch;
    }

    public void setDateDebutDSearch(Date dateDebutDSearch) {
        this.dateDebutDSearch = dateDebutDSearch;
    }

    public Date getDateFinDSearch() {
        return dateFinDSearch;
    }

    public void setDateFinDSearch(Date dateFinDSearch) {
        this.dateFinDSearch = dateFinDSearch;
    }

    public String getArticleDSearch() {
        return articleDSearch;
    }

    public void setArticleDSearch(String articleDSearch) {
        this.articleDSearch = articleDSearch;
    }

    public String getNumeroDSearch() {
        return numeroDSearch;
    }

    public void setNumeroDSearch(String numeroDSearch) {
        this.numeroDSearch = numeroDSearch;
    }

    public List<YvsProdDeclarationProduction> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<YvsProdDeclarationProduction> declarations) {
        this.declarations = declarations;
    }

    public PaginatorResult<YvsProdDeclarationProduction> getP_declaration() {
        return p_declaration;
    }

    public void setP_declaration(PaginatorResult<YvsProdDeclarationProduction> p_declaration) {
        this.p_declaration = p_declaration;
    }

    public Date getDateLancement() {
        return dateLancement;
    }

    public void setDateLancement(Date dateLancement) {
        this.dateLancement = dateLancement;
    }

    public OrdreFabrication getProgram() {
        return program;
    }

    public void setProgram(OrdreFabrication program) {
        this.program = program;
    }

    public List<OrdreFabrication> getPrograms() {
        return programs;
    }

    public void setPrograms(List<OrdreFabrication> programs) {
        this.programs = programs;
    }

    public OrdreFabrication getSous() {
        return sous;
    }

    public void setSous(OrdreFabrication sous) {
        this.sous = sous;
    }

    public DeclarationProduction getDeclaration() {
        return declaration;
    }

    public void setDeclaration(DeclarationProduction declaration) {
        this.declaration = declaration;
    }

    public YvsProdDeclarationProduction getSelectDeclaration() {
        return selectDeclaration;
    }

    public void setSelectDeclaration(YvsProdDeclarationProduction selectDeclaration) {
        this.selectDeclaration = selectDeclaration;
    }

    public String getFilterState() {
        return filterState;
    }

    public void setFilterState(String filterState) {
        this.filterState = filterState;
    }

    public Date getDateFilter1() {
        return dateFilter1;
    }

    public void setDateFilter1(Date dateFilter1) {
        this.dateFilter1 = dateFilter1;
    }

    public Date getDateFilter2() {
        return dateFilter2;
    }

    public void setDateFilter2(Date dateFilter2) {
        this.dateFilter2 = dateFilter2;
    }

    public OperationsOF getOperation() {
        return operation;
    }

    public void setOperation(OperationsOF operation) {
        this.operation = operation;
    }

    public ComposantsOf getComposant() {
        return composant;
    }

    public void setComposant(ComposantsOf composant) {
        this.composant = composant;
    }

    public boolean isSuggestionOF() {
        return suggestionOF;
    }

    public void setSuggestionOF(boolean suggestionOF) {
        this.suggestionOF = suggestionOF;
    }

    public List<YvsProdEquipeProduction> getEquipes() {
        return equipes;
    }

    public void setEquipes(List<YvsProdEquipeProduction> equipes) {
        this.equipes = equipes;
    }

    public List<YvsProdOrdreFabrication> getListeOrdreF() {
        return listeOrdreF;
    }

    public void setListeOrdreF(List<YvsProdOrdreFabrication> listeOrdreF) {
        this.listeOrdreF = listeOrdreF;
    }

    public OrdreFabrication getOrdre() {
        return ordre;
    }

    public void setOrdre(OrdreFabrication ordre) {
        this.ordre = ordre;
    }

    public YvsProdComposantOF getSelectedComposant() {
        return selectedComposant;
    }

    public void setSelectedComposant(YvsProdComposantOF selectedComposant) {
        this.selectedComposant = selectedComposant;
    }

    public YvsProdOperationsOF getSelectedOp() {
        return selectedOp;
    }

    public void setSelectedOp(YvsProdOperationsOF selectedOp) {
        this.selectedOp = selectedOp;
    }

    public boolean isIsComposant() {
        return isComposant;
    }

    public void setIsComposant(boolean isComposant) {
        this.isComposant = isComposant;
    }

    public YvsProdOrdreFabrication getSelectedOf() {
        return selectedOf;
    }

    public void setSelectedOf(YvsProdOrdreFabrication selectedOf) {
        this.selectedOf = selectedOf;
    }

    public YvsProdParametre getParamG() {
        if (paramG == null) {
            paramG = new YvsProdParametre(0);
        }
        return paramG;
    }

    public void setParamG(YvsProdParametre paramG) {
        this.paramG = paramG;
    }

    public YvsProdFluxComposant getSelectedFlux() {
        return selectedFlux;
    }

    public void setSelectedFlux(YvsProdFluxComposant selectedFlux) {
        this.selectedFlux = selectedFlux;
    }

    public SuiviFlux getSuiviFlux() {
        return suiviFlux;
    }

    public void setSuiviFlux(SuiviFlux suiviFlux) {
        this.suiviFlux = suiviFlux;
    }

    public boolean isTransfertOf() {
        return transfertOf;
    }

    public void setTransfertOf(boolean transfertOf) {
        this.transfertOf = transfertOf;
    }

    public Depots getDepotDest() {
        return depotDest;
    }

    public void setDepotDest(Depots depotDest) {
        this.depotDest = depotDest;
    }

    public List<YvsBaseDepots> getDepotsCible() {
        return depotsCible;
    }

    public void setDepotsCible(List<YvsBaseDepots> depotsCible) {
        this.depotsCible = depotsCible;
    }

    public List<YvsGrhTrancheHoraire> getTranchesCible() {
        return tranchesCible;
    }

    public void setTranchesCible(List<YvsGrhTrancheHoraire> tranchesCible) {
        this.tranchesCible = tranchesCible;
    }

    public TrancheHoraire getTrancheHoraire() {
        return trancheHoraire;
    }

    public void setTrancheHoraire(TrancheHoraire trancheHoraire) {
        this.trancheHoraire = trancheHoraire;
    }

    public boolean isLoadNext() {
        return loadNext;
    }

    public void setLoadNext(boolean loadNext) {
        this.loadNext = loadNext;
    }

    public boolean isDisplayAll() {
        return displayAll;
    }

    public void setDisplayAll(boolean displayAll) {
        this.displayAll = displayAll;
    }

    public List<YvsUsers> getProducteurs() {
        return producteurs;
    }

    public void setProducteurs(List<YvsUsers> producteurs) {
        this.producteurs = producteurs;
    }

    public List<YvsGrhTrancheHoraire> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsGrhTrancheHoraire> tranches) {
        this.tranches = tranches;
    }

    public YvsProdSessionProd getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(YvsProdSessionProd currentSession) {
        this.currentSession = currentSession;
    }

    public List<YvsProdSessionProd> getSessionsProd() {
        return sessionsProd;
    }

    public void setSessionsProd(List<YvsProdSessionProd> sessionsProd) {
        this.sessionsProd = sessionsProd;
    }

    public SuiviOperations getSuiviOperation() {
        return suiviOperation;
    }

    public void setSuiviOperation(SuiviOperations suiviOperation) {
        this.suiviOperation = suiviOperation;
    }

    public YvsProdSuiviOperations getSelectedCycle() {
        return selectedCycle;
    }

    public void setSelectedCycle(YvsProdSuiviOperations selectedCycle) {
        this.selectedCycle = selectedCycle;
    }

    public boolean isNavNextCycle() {
        return navNextCycle;
    }

    public void setNavNextCycle(boolean navNextCycle) {
        this.navNextCycle = navNextCycle;
    }

    public boolean isNavPrevCycle() {
        return navPrevCycle;
    }

    public void setNavPrevCycle(boolean navPrevCycle) {
        this.navPrevCycle = navPrevCycle;
    }

    public boolean isNotRemindDeleteDecl() {
        return notRemindDeleteDecl;
    }

    public void setNotRemindDeleteDecl(boolean notRemindDeleteDecl) {
        this.notRemindDeleteDecl = notRemindDeleteDecl;
    }

    public String getTypePage() {
        return typePage;
    }

    public void setTypePage(String typePage) {
        this.typePage = typePage;
    }

    public boolean isDisplayColStock() {
        return displayColStock;
    }

    public void setDisplayColStock(boolean displayColStock) {
        this.displayColStock = displayColStock;
    }

    public boolean isDisplayColCout() {
        return displayColCout;
    }

    public void setDisplayColCout(boolean displayColCout) {
        this.displayColCout = displayColCout;
    }

    public boolean isDisplayColFlux() {
        return displayColFlux;
    }

    public void setDisplayColFlux(boolean displayColFlux) {
        this.displayColFlux = displayColFlux;
    }

    public YvsProdParametre getParamProduction() {
        return paramProduction;
    }

    public void setParamProduction(YvsProdParametre paramProduction) {
        this.paramProduction = paramProduction;
    }

    @Override
    public void loadAll() {

    }

    public void searchArticle(OrdreFabrication ordre) {
        if (ordre != null) {
            ordre.getArticles().setDesignation("");
            ordre.getArticles().setError(true);
            ordre.getArticles().setId(0);

            String code = ordre.getArticles().getRefArt();
            if (code != null ? code.trim().length() > 0 : false) {
                ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
                if (m != null) {
                    Articles y = m.searchArticleActif("P", code, false);
                    if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                        if (m.getArticlesResult().size() == 1) {
                            chooseArticle(ordre, y, false);
                            ordre.getArticles().setError(false);
                        }
                    }
                }
            }
        }
    }

    public void searchArticleProgram() {
        if (program != null) {
            program.setNomenclature(new Nomenclature());
            program.setGamme(new GammeArticle());
            program.getArticles().setDesignation("");
            program.getArticles().setError(true);
            program.getArticles().setId(0);
            program.getArticles().getNomenclatures().clear();
            program.getArticles().getGammes().clear();
            String code = program.getArticles().getRefArt();
            if (code != null ? code.trim().length() > 0 : false) {
                ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
                if (m != null) {
                    Articles y = m.searchArticleActif("P", code, true);
                    if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                        if (m.getArticlesResult().size() == 1) {
                            chooseArticle(program, y, false);
                            program.getArticles().setError(false);
                        } else {
                            update("data_article_gammes_of");
                        }
                    }
                }
            }
        }
    }

    public void searchArticle(String code, boolean isComposant) {
        if (ordre.getStatusOrdre().equals(Constantes.ETAT_TERMINE)) {
            getErrorMessage("Vous ne pouvez pas modifier cet ordre..il est déjà terminé");
            return;
        }
        this.isComposant = isComposant;
        if (!isComposant) {
            ordre.getArticles().setDesignation("");
            ordre.getArticles().setError(true);
            ordre.getArticles().setId(0);
        } else {
            composant.getComposant().setDesignation("");
            composant.getComposant().setError(true);
            composant.getComposant().setId((long) 0);
        }
        if (code != null ? code.trim().length() > 0 : false) {
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                Articles y = m.searchArticleActif(!isComposant ? "P" : "A", code, true);
                if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                    if (m.getArticlesResult().size() > 1) {
                        update("data_article_nomenclature");
                    } else {
                        chooseArticle(y);
                    }
                    if (!isComposant) {
                        ordre.getArticles().setError(false);
                    } else {
                        composant.getComposant().setError(false);
                    }
                }
            }
        }
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildSimpleBeanArticles(bean));
        }
    }

    public void loadOnViewArticleProgram(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            Articles y = UtilProd.buildSimpleBeanArticles(bean);
            chooseArticle(program, y, false);
            program.getArticles().setError(false);
            update("form-programmation_of");
        }
    }

    private void chooseArticle(Articles y) {
        chooseArticle(ordre, y, isComposant);
    }

    private void chooseArticle(OrdreFabrication ordre, Articles y, boolean isComposant) {
        if (ordre.getStatusOrdre().equals(Constantes.ETAT_TERMINE)) {
            getErrorMessage("Vous ne pouvez pas modifier cet ordre..il est déjà terminé");
            return;
        }
        if (!isComposant) {
            ordre.setArticles(y);
            //charge les gammes et les nomenclatures   
            if (autoriser("prod_gamme_load_all")) {
                ordre.getArticles().setGammes(dao.loadNameQueries("YvsProdGammeArticle.findByArticleActif", new String[]{"article"}, new Object[]{new YvsBaseArticles(y.getId())}));
            } else {
                ordre.getArticles().setGammes(dao.loadNameQueries("YvsProdGammeSite.findOneGammeBySite", new String[]{"article", "site"}, new Object[]{new YvsBaseArticles(y.getId()), new YvsProdSiteProduction(ordre.getSite().getId())}));
            }
            if (autoriser("prod_nomenc_load_all")) {
                ordre.getArticles().setNomenclatures(dao.loadNameQueries("YvsProdNomenclature.findByArticleFor", new String[]{"article", "for"}, new Object[]{new YvsBaseArticles(y.getId()), false}));
            } else {
                ordre.getArticles().setNomenclatures(dao.loadNameQueries("YvsProdNomenclatureSite.findOneNomenclatureBySite", new String[]{"article", "for", "site"}, new Object[]{new YvsBaseArticles(y.getId()), false, new YvsProdSiteProduction(ordre.getSite().getId())}));
            }
            if (!ordre.getArticles().getGammes().isEmpty()) {
                ordre.setGamme(UtilProd.buildSimpleBeanGammeArticleWA(ordre.getArticles().getGammes().get(0)));
            }
            if (!ordre.getArticles().getNomenclatures().isEmpty()) {
                ordre.setNomenclature(UtilProd.buildSimpleBeanNomenclatureAndUnite(ordre.getArticles().getNomenclatures().get(0)));
            }
            update("txt_article_des_gammes");
            update("chp_nomenclature");
            update("chp_gamme");
            update("txt_article_gammes_of");
        } else {
            composant.setComposant(y);
            update("txt_article_composant");
            update("txt_unite_composant");
            update("headPPtCc");
        }
    }

    private YvsProdNomenclature findNomenclature(YvsBaseArticles article) {
        if (autoriser("prod_nomenc_load_all")) {
            return (YvsProdNomenclature) dao.loadOneByNameQueries("YvsProdNomenclature.findByArticleFor", new String[]{"article", "for"}, new Object[]{article, false});
        } else {
            return (YvsProdNomenclature) dao.loadOneByNameQueries("YvsProdNomenclatureSite.findOneNomenclatureBySite", new String[]{"article", "for", "site"}, new Object[]{article, false, new YvsProdSiteProduction(ordre.getSite().getId())});
        }
    }

    private YvsProdGammeArticle findGamme(YvsBaseArticles article) {
        if (autoriser("prod_gamme_load_all")) {
            return (YvsProdGammeArticle) dao.loadOneByNameQueries("YvsProdGammeArticle.findByArticleActif", new String[]{"article"}, new Object[]{article});
        } else {
            return (YvsProdGammeArticle) dao.loadOneByNameQueries("YvsProdGammeSite.findOneGammeBySite", new String[]{"article", "site"}, new Object[]{article, new YvsProdSiteProduction(ordre.getSite().getId())});
        }
    }

    public void initArticles(boolean isComposant) {
        ManagedArticles a = (ManagedArticles) giveManagedBean("Marticle");
        if (a != null) {
            a.initArticles(!isComposant ? "P" : "A", ordre.getArticles());
        }
        this.isComposant = isComposant;
        update("data_article_gammes_of");
    }

    public void selectGamme(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            long OldId = (long) ev.getOldValue();
            if (OldId != id) {
                ordre.getListOperationsOf().clear();
            }
            onselectGamme(new YvsProdGammeArticle(id));
        }
    }

    public void onselectGamme(YvsProdGammeArticle gam) {
        //construire les op de la gamme en faisant le lien avec la consommation des composants
        if (gam != null) {
            List<YvsProdOperationsGamme> operations = dao.loadNameQueries("YvsProdOperationsGamme.findByGamme", new String[]{"gamme"}, new Object[]{gam});
            if (!operations.isEmpty()) {
                ordre.setGamme(UtilProd.buildSimpleBeanGammeArticle(operations.get(0).getGammeArticle()));
            }
            //Construit les opérations OF + les composants à l'opération            
            loadListOperationsOf(ordre, operations);
        }
    }

    private List<YvsProdOperationsOF> loadListOperationsOf(OrdreFabrication ordre, List<YvsProdOperationsGamme> lop) {
        //Construit
        YvsProdOperationsOF op;
        YvsProdPosteOperation postOp;
        for (YvsProdOperationsGamme opGamme : lop) {
            if (opGamme.getActif()) {
                //Vérifie si on est en modification ou en création
                op = findOperationOF(opGamme, ordre);
                if (op == null) {
                    op = new YvsProdOperationsOF(-opGamme.getId().longValue());
                    op.setCodeRef(opGamme.getCodeRef());
                    op.setNumero(opGamme.getNumero());
                    op.setTypeCout(opGamme.getTypeCout());
                }
                postOp = opGamme.givePosteMachine();
                if (postOp != null) {
                    op.setMachine(postOp.getPosteCharge());
                }
                postOp = opGamme.givePosteMo();
                if (postOp != null) {
                    op.setMainOeuvre(postOp.getPosteCharge());
                }
                op.setTempsReglage(opGamme.getTempsReglage());
                op.setTempsOperation(giveTimeOperation(opGamme));
                //charge les flux de composant
                opGamme.setComposants(dao.loadNameQueries("YvsProdComposantOp.findByOperation", new String[]{"operation"}, new Object[]{opGamme}));
                op.setComposants(buildCompUsedInoperation(opGamme, op, ordre));
                int idx = ordre.getListOperationsOf().indexOf(op);
                if (idx < 0) {
                    ordre.getListOperationsOf().add(op);
                } else {
                    ordre.getListOperationsOf().set(idx, op);
                }
            }
        }
        return ordre.getListOperationsOf();
    }

    private YvsProdOperationsOF findOperationOF(YvsProdOperationsGamme op, OrdreFabrication ordre) {
        for (YvsProdOperationsOF o : ordre.getListOperationsOf()) {
            if (op.getNumero().equals(o.getNumero()) && op.getCodeRef().equals(o.getCodeRef())) {
                return o;
            }
        }
        return null;
    }

    //obtient la durée de l'opération en fonction du paramétrage de la gamme
    private double giveTimeOperation(YvsProdOperationsGamme op) {
        double re = 0;
        if (op != null) {
            switch (op.getTypeTemps()) {
                case Constantes.PROD_OP_TYPE_TEMPS_PROPORTIONNEL:
                    if (op.getQuantiteBase() != 0) {
                        re = (ordre.getQuantitePrevu() * op.getTempsOperation()) / op.getQuantiteBase();
                    }
                    break;
                case Constantes.PROD_OP_TYPE_TEMPS_FIXE:
                    re = op.getTempsOperation();
                    break;
                case Constantes.PROD_OP_TYPE_TEMPS_CADENCE:
                    if (op.getCadence() != 0) {
                        re = (ordre.getQuantite() / op.getCadence()) * op.getTempsOperation();
                    }
                    break;
            }
        }
        return re;
    }

    private List<YvsProdFluxComposant> buildCompUsedInoperation(YvsProdOperationsGamme opGamme, YvsProdOperationsOF opOf, OrdreFabrication ordre) {
        YvsProdFluxComposant item;
        List<YvsProdFluxComposant> re = new ArrayList<>();
        YvsProdComposantOF comp;
        for (YvsProdComposantOp composantGamme : opGamme.getComposants()) {
            if (composantGamme.getComposant().getActif() && !composantGamme.getComposant().getAlternatif()) {
                comp = giveComposantOF(composantGamme.getComposant().getArticle(), composantGamme.getComposant().getUnite(), ordre);
                item = findOnFluxComposant(opOf, composantGamme, comp);
                if (item == null) {
                    item = new YvsProdFluxComposant(-composantGamme.getId().longValue());
                }
                item.setComposant(comp);
                item.setSens(composantGamme.getSens());
                item.setInListByDefault(true);
                item.setTauxComposant(composantGamme.getQuantite());
                item.setMargeQte(composantGamme.getMargeQte());
                item.setMargeSuperieure(composantGamme.getMargeSup());
                item.setCoeficientValeur(composantGamme.getCoeficientValeur());
                item.setOrdre(composantGamme.getComposant().getOrdre());
                item.setTypeCout(composantGamme.getTypeCout());
                if (composantGamme.getQuantite() > 0) {
                    double d = item.getComposant().getQuantitePrevu();
                    item.setQuantite((item.getComposant().getQuantitePrevu() * composantGamme.getQuantite()) / 100);
                }
                if (composantGamme.getTauxPerte() > 0) {
                    double d = item.getQuantite();
                    item.setQuantitePerdue((d != 0) ? (d * composantGamme.getTauxPerte()) / 100 : 0);
                } else {
                    item.setQuantitePerdue(0d);
                }
                if (composantGamme.getIndicateurs() != null) {
                    item.setSuivi(true);
                    item.setTypeSuivi(composantGamme.getIndicateurs().getTypeIndicateur());
                    item.setIndicateursSuivis(buildIndicateurSuivi(composantGamme.getIndicateurs(), item.getQuantite(), item));
                } else {
                    item.setSuivi(false);
                }
                re.add(item);
            }
        }
        return re;
    }

    private YvsProdFluxComposant findOnFluxComposant(YvsProdOperationsOF oof, YvsProdComposantOp composantOp, YvsProdComposantOF comp) {
        if (oof != null ? (oof.getComposants() != null && composantOp != null) : false) {
            YvsProdFluxComposant flux = null;
            if ((comp != null ? comp.getId() != null ? comp.getId() > 0 : false : false) && (oof.getId() != null ? oof.getId() > 0 : false)) {
                flux = (YvsProdFluxComposant) dao.loadOneByNameQueries("YvsProdFluxComposant.findOne", new String[]{"composant", "operation"}, new Object[]{comp, oof});
            }
            if (flux == null) {
                for (YvsProdFluxComposant fc : oof.getComposants()) {
                    if (fc.getComposant().getArticle().equals(composantOp.getComposant().getArticle())) {
                        return fc;
                    }
                }
            } else {
                return flux;
            }
        }
        return null;
    }
    List<Long> idsIndicateurs;

    private List<YvsProdOfIndicateurSuivi> buildIndicateurSuivi(YvsProdIndicateurOp ind, double qte, YvsProdFluxComposant item) {
        List<YvsProdOfIndicateurSuivi> re = new ArrayList<>();
        YvsProdOfIndicateurSuivi bean;
        if (ind != null) {
            idsIndicateurs = new ArrayList<>();
            Long id;
            for (YvsProdValeursQualitative val : ind.getValeurs()) {
                id = findIdIndicateur(item, val.getCodeCouleur());
                bean = new YvsProdOfIndicateurSuivi(id != null ? id : -val.getId());
                bean.setAuthor(currentUser);
                bean.setCodeCouleur(val.getCodeCouleur());
                bean.setDateSave(new Date());
                bean.setDateUpdate(new Date());
                bean.setQuantiteBorne(qte * val.getValeurQuantitative() / 100);
                bean.setValeurText(val.getValeurText());
                re.add(bean);
            }
            deleteOldIndicateur(item);
        }
        return re;
    }

    private Long findIdIndicateur(YvsProdFluxComposant fc, String couleur) {
        if (fc != null ? fc.getIndicateursSuivis() != null : false) {
            YvsProdOfIndicateurSuivi ind = (YvsProdOfIndicateurSuivi) dao.loadOneByNameQueries("YvsProdOfIndicateurSuivi.findOne", new String[]{"composant", "couleur"}, new Object[]{fc, couleur});
            if (ind == null) {
                for (YvsProdOfIndicateurSuivi i : fc.getIndicateursSuivis()) {
                    if (!idsIndicateurs.contains(i.getId())) {
                        idsIndicateurs.add(i.getId());
                        return i.getId();
                    }
                }
            } else {
                idsIndicateurs.add(ind.getId());
                return ind.getId();
            }
        }
        return null;
    }

    private void deleteOldIndicateur(YvsProdFluxComposant fc) {
        try {
            if (fc != null ? fc.getIndicateursSuivis() != null : false) {
                for (YvsProdOfIndicateurSuivi i : fc.getIndicateursSuivis()) {
                    if (!idsIndicateurs.contains(i.getId())) {
                        dao.delete(i);
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Erreur...");
        }
    }

    public void selectNomenclature(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            champ = new String[]{"nomenclature"};
            val = new Object[]{new YvsProdNomenclature(id)};
            nameQueri = "YvsProdComposantNomenclature.findByNomenclature";
            List<YvsProdComposantNomenclature> l = dao.loadListTableByNameQueries(nameQueri, champ, val);
            //récupère la nomenclature
            if (!l.isEmpty()) {
                ordre.setNomenclature(UtilProd.buildBeanNomenclature(l.get(0).getNomenclature()));
                ordre.getNomenclature().setComposants(l);
                if (ordre.getQuantitePrevu() <= 0) {
                    ordre.setQuantitePrevu(ordre.getNomenclature().getQuantite());
                }
//                ordre.setListComposantOf(calculBesoins(l, l.get(0).getNomenclature(), ordre));
            }
        } else {
            getErrorMessage("!!!");
        }
    }

    public void listenChangeValueQtePrevu() {
        if (ordre.canEditable(autoriser("prod_update_of_encours"))) {
            champ = new String[]{"nomenclature"};
            val = new Object[]{new YvsProdNomenclature(ordre.getNomenclature().getId())};
            nameQueri = "YvsProdComposantNomenclature.findByNomenclature";
            List<YvsProdComposantNomenclature> l = dao.loadListTableByNameQueries(nameQueri, champ, val);
            ordre.setListComposantOf(calculBesoins(l, l.get(0).getNomenclature(), ordre, false));
            if (ordre.getGamme() != null ? ordre.getGamme().getId() > 0 : false) {
                onselectGamme(new YvsProdGammeArticle(ordre.getGamme().getId()));
            }
        } else {
            getErrorMessage("Vous ne pouvez modifier un Ordre déjà lancé !");
        }
    }

//    public void listenChangeValueQte() {
//        if (ordre.canEditable(autoriser("prod_update_of_encours"))) {
//            champ = new String[]{"nomenclature"};
//            val = new Object[]{new YvsProdNomenclature(ordre.getNomenclature().getId())};
//            nameQueri = "YvsProdComposantNomenclature.findByNomenclature";
//            List<YvsProdComposantNomenclature> l = dao.loadListTableByNameQueries(nameQueri, champ, val);
//            ordre.setQuantitePrevu(ordre.getQuantitePrevu());
//            ordre.setListComposantOf(calculBesoins(l, l.get(0).getNomenclature()));
//            if (ordre.getGamme() != null ? ordre.getGamme().getId() > 0 : false) {
//                ordre.getListOperationsOf().clear();
//                onselectGamme(new YvsProdGammeArticle(ordre.getGamme().getId()));
//            }
//        }
//    }
    private String sourceActionDepot;
    private boolean isComposant;

    public void saisirDepot(String source, String code, boolean isComposant) {
        if (ordre.getStatusOrdre().equals(Constantes.ETAT_TERMINE)) {
            getErrorMessage("Vous ne pouvez pas modifier cet ordre..il est déjà terminé");
            return;
        }
        sourceActionDepot = source;
        this.isComposant = isComposant;
        if ((code != null) ? code.trim().length() > 0 : false) {
            ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (service != null) {
                Depots re = service.searchDepotByCode(code, true);
                if (re.getId() > 0) {
                    if (!isComposant) {
                        if (source.equals("PF")) {
                            ordre.setDepotPf(re);
                        } else {
                            ordre.setDepotMp(re);
                        }
                    } else {
                        composant.setDepotConso(re);
                    }
                } else {
                    if (!isComposant) {
                        if (source.equals("PF")) {
                            ordre.getDepotPf().setError(true);
                        } else {
                            ordre.getDepotMp().setError(true);
                        }
                    } else {
                        composant.getDepotConso().setError(true);
                    }
                }
                update("data_of_depot");
            }
        } else {
            if (!isComposant) {
                if (source.equals("PF")) {
                    ordre.getDepotPf().setDesignation("");
                } else {
                    ordre.getDepotMp().setDesignation("");
                }
            } else {
                composant.getDepotConso().setDesignation("");
            }
        }
        if (!isComposant) {
            if (source.equals("PF")) {
                update("txt_depot_pf");
            } else {
                update("txt_depot_mp");
            }
        } else {
            update("txt_depot_composant");
        }
    }

    public void openListDepots(String source, boolean isComposant) {
        if (ordre.getStatusOrdre().equals(Constantes.ETAT_TERMINE)) {
            getErrorMessage("Vous ne pouvez pas modifier cet ordre..il est déjà terminé");
            return;
        }
        sourceActionDepot = source;
        this.isComposant = isComposant;
        ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (service != null) {
            service.loadAllDepotActif(true);
            openDialog("dlgDepot");
            update("data_of_depot");
        }
    }

    public void selectLineDepot(SelectEvent ev) {
        if (ev.getObject() != null) {
            YvsBaseDepots y = (YvsBaseDepots) ev.getObject();
            Depots d = (UtilProd.buildBeanDepot(y));
            if (!isComposant) {
                if (sourceActionDepot.equals("PF")) {
                    ordre.setDepotPf(d);
                    update("txt_depot_pf");
                } else {
                    ordre.setDepotMp(d);
                    update("txt_depot_mp");
                }
            } else {
                composant.setDepotConso(d);
                update("txt_depot_composant");
            }
        }
    }

    public void chooseDepotConso() {
        if (composant.getDepotConso() != null ? composant.getDepotConso().getId() > 0 : false) {
            if (selectedComposant != null ? !selectedComposant.getDepotConso().getId().equals(composant.getDepotConso().getId()) : false) {
                openDialog("dlgConfirmChangeDepot");
            }
        }
    }

    public void confirmChangeDepotConso() {
        if (selectedComposant != null ? selectedComposant.getId() > 0 : false) {
            selectedComposant.setDepotConso(new YvsBaseDepots(composant.getDepotConso().getId(), composant.getDepotConso().getDesignation()));
            selectedComposant.setDateUpdate(new Date());
            selectedComposant.setAuthor(currentUser);
            dao.update(selectedComposant);
            succes();
        }
    }

    public void loadDataBase() {
        loadAllOf(true, true);
    }

    public void onEditQuantiteValide(CellEditEvent event) {
        if (event.getRowIndex() >= 0) {
            Double newValue;
            try {
                newValue = (Double) event.getNewValue();
            } catch (Exception ex) {
                List<Double> val = (ArrayList<Double>) event.getNewValue();
                newValue = val.get(0);
            }
            editQteValide(event.getRowIndex(), newValue, true);
        }
    }

    private void editQteValide(int row, double value, boolean repartir) {
        YvsProdComposantOF y = ordre.getListComposantOf().get(row);
        if (ordre.canEditable(autoriser("prod_update_of_encours"))) {
            if (y.getId() > 0) {
                y.setQuantiteValide(value);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
                if (repartir) {
                    miseAjourFluxComposant(y);
                }
            }
            //Modifier la quantité prévu
            if (ordre.getNomenclature().isQteLieAuxComposant()) {
                ordre.setQuantitePrevu(getQtePrevu(ordre.getListComposantOf()));
                if (selectedOf != null) {
                    selectedOf.setQuantite(ordre.getQuantitePrevu());
                    selectedOf.setAuthor(currentUser);
                    selectedOf.setDateUpdate(new Date());
                    dao.update(selectedOf);
                }
            }
            update("input_qte");
            succes();
        } else {
            getErrorMessage("Vous ne pouvez pas modifier cet ordre...il n'est plus éditable");
        }

    }

    private double getQtePrevu(List<YvsProdComposantOF> l) {
        double re = 0;
        for (YvsProdComposantOF c : l) {
            re += c.getQuantiteValide() * c.getCoefficient();
        }
        return re;
    }
    /**
     *
     */
    /**
     * DEBUT TRAITEMENTD DES ORDRES DE PRODUCTION
     *
     * @param bean
     * @return N*
     */
    private int firstResultOf, totalPageOf, currentPageOf;
    private boolean disNextOf, disPrevOf;
    private String chaineSelectOf;

    public int getFirstResultOf() {
        return firstResultOf;
    }

    public void setFirstResultOf(int firstResultOf) {
        this.firstResultOf = firstResultOf;
    }

    public int getTotalPageOf() {
        return totalPageOf;
    }

    public void setTotalPageOf(int totalPageOf) {
        this.totalPageOf = totalPageOf;
    }

    public void setDisNextOf(boolean disNextOf) {
        this.disNextOf = disNextOf;
    }

    public boolean isDisNextOf() {
        return disNextOf;
    }

    public boolean isDisPrevOf() {
        return disPrevOf;
    }

    public void setDisPrevOf(boolean disPrevOf) {
        this.disPrevOf = disPrevOf;
    }

    public void setCurrentPageOf(int currentPageOf) {
        this.currentPageOf = currentPageOf;
    }

    public int getCurrentPageOf() {
        return currentPageOf;
    }

    public String getChaineSelectOf() {
        return chaineSelectOf;
    }

    public void setChaineSelectOf(String chaineSelectOf) {
        this.chaineSelectOf = chaineSelectOf;
    }

    public boolean isDemande_transfert() {
        return demande_transfert;
    }

    public void setDemande_transfert(boolean demande_transfert) {
        this.demande_transfert = demande_transfert;
    }

    public List<YvsBaseDepots> getDepots() {
        return depots;
    }

    public void setDepots(List<YvsBaseDepots> depots) {
        this.depots = depots;
    }

    @Override
    public void resetFiche() {
        Depots dmp = ordre.getDepotMp();
        Depots dpf = ordre.getDepotPf();
        Date d = ordre.getDateDebutLancement();
        Date f = ordre.getDateFinFabrication();
        SiteProduction s = ordre.getSite();
        resetFiche(ordre);
        ordre.setArticles(new Articles());
        ordre.setEquipe(new EquipeProduction());
        ordre.setGamme(new GammeArticle());
        ordre.setNomenclature(new Nomenclature());
        ordre.getListOperationsOf().clear();
        ordre.getListComposantOf().clear();
        ordre.setDepotMp(dmp != null ? dmp : new Depots());
        ordre.setDepotPf(dpf != null ? dpf : new Depots());
        ordre.setSite(s != null ? s : new SiteProduction());
        ordre.setDateDebutLancement(d);
        ordre.setDateFinFabrication(f);
        ordre.setStatusOrdre(Constantes.ETAT_ATTENTE);
        ordre.setGenerateOfSousProduit(false);
        ordre.getDeclarations().clear();
        listeComposants.clear();
        suggestionOF = true;
        update("panel_state");
        update("formOF");
        update("tablePhase_");
        update("of_depot");
    }

    @Override
    public boolean controleFiche(OrdreFabrication bean) {
        return controleFiche(bean, true, true, true);
    }

    public boolean controleFiche(OrdreFabrication bean, boolean principal, boolean stockage, boolean generateNum) {
        if (principal) {
            if (bean.getSite().getId() <= 0) {
                getErrorMessage("Formulaire incorrecte !", "veuillez choisir un site de fabrication");
                return false;
            }
        } else {
            if (bean.getQuantitePrevu() <= 0) {
                getErrorMessage("Formulaire incorrecte !", "veuillez precisez la quantité prévue");
                return false;
            }
        }
        if (bean.getArticles().getId() <= 0) {
            getErrorMessage("Formulaire incorrecte !", "veuillez renseigner l'article");
            return false;
        }
        if (bean.getNomenclature().getId() <= -1) {
            getErrorMessage("Formulaire incorrecte !", "veuillez indiquer la nomenclature à utiliser");
            return false;
        }
        if (bean.isSuiviOperations()) {
            if (bean.getGamme().getId() <= -1) {
                getErrorMessage("Formulaire incorrecte !", "veuillez indiquer la gamme à utiliser");
                return false;
            }
        }
        //si l'ordre est clôturé, on ne modifie plus
        if (bean.getStatusOrdre() != null) {
            if (bean.getStatusOrdre().equals(Constantes.ETAT_CLOTURE) || bean.getStatusOrdre().equals(Constantes.ETAT_TERMINE) || (bean.getStatusOrdre().equals(Constantes.ETAT_ENCOURS) && !autoriser("prod_update_of_encours"))) {
                getErrorMessage("Impossible de continuer ce traitement", "L'ordre de fabrication pour l'article " + bean.getArticles().getDesignation() + " n'est plus éditable");
                return false;
            }
        }
        if (stockage) {
            if (bean.getDepotPf().getId() <= 0) {
                getWarningMessage("Vous n'avez pas renseigné le lieux de stockage des produits finis");
            }
        }
        //contrôle la date
        if (generateNum) {
            if ((selectedOf != null ? (selectedOf.getId() > 0 ? !selectedOf.getDateDebut().equals(bean.getDateDebutLancement()) : false) : false)
                    || (bean.getReference() == null || bean.getReference().trim().length() < 1)) {
                String ref = genererReference(Constantes.PROD_TYPE_PROD_NAME, bean.getDateDebutLancement());
                if ((ref != null) ? ref.trim().equals("") : true) {
                    return false;
                }
                bean.setReference(ref);
                bean.setNumIdentification(bean.getReference() + "_" + bean.getArticles().getRefArt());
            }
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        YvsProdOrdreFabrication y = saveNewOF(ordre);
        if (y != null ? y.getId() > 0 : false) {
            succes();
            actionOpenOrResetAfter(this);
            return true;
        }
        return false;
    }

    public YvsProdOrdreFabrication saveNewOF(OrdreFabrication ordre) {
        return saveNewOF(ordre, true);
    }

    public YvsProdOrdreFabrication saveNewOF(OrdreFabrication ordre, boolean oneProduct) {
        if (controleFiche(ordre)) {
            //récupère l'entête de la production
            YvsProdOrdreFabrication y = saveProduction(ordre);
            ordre.setId(y.getId());
            if (ordre.isGenerateOfSousProduit()) {
                //récupère les sous produits de la liste des composants
                YvsProdNomenclature subNom;
                YvsProdGammeArticle subGame;
                OrdreFabrication subOrder;
                List<YvsProdOperationsGamme> lop;
                for (YvsProdComposantOF c : ordre.getListComposantOf()) {
                    if (c.getType().equals(Constantes.PROD_OP_TYPE_COMPOSANT_NORMAL)) {
                        subNom = findNomenclature(c.getArticle());
                        subGame = findGamme(c.getArticle());
                        if (subGame != null && subNom != null) {
                            subOrder = new OrdreFabrication();
                            cloneObject(subOrder, ordre);
                            subOrder.setId(0L);
                            subOrder.setArticles(UtilProd.buildBeanArticles(c.getArticle()));
                            subOrder.setGamme(UtilProd.buildSimpleBeanGammeArticle(subGame));
                            subOrder.setNomenclature(UtilProd.buildBeanNomenclature(subNom));
                            subOrder.setQuantite(c.getQuantitePrevu());
                            subOrder.setQuantitePrevu(c.getQuantitePrevu());
                            subOrder.setReference(genererReference(Constantes.PROD_TYPE_PROD_NAME, ordre.getDateDebutLancement()));
                            subOrder.setListComposantOf(calculBesoins(dao.loadListTableByNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"}, new Object[]{subNom}), subNom, subOrder, true));
                            lop = dao.loadNameQueries("YvsProdOperationsGamme.findByGamme", new String[]{"gamme"}, new Object[]{subGame});
                            subOrder.setListOperationsOf(new ArrayList<YvsProdOperationsOF>());
                            loadListOperationsOf(subOrder, lop);
                            saveProduction(UtilProd.buildEntityOf(subOrder), subOrder, false);
                        }
                    }
                }
            }
            if (ordre.getStatusOrdre() == null) {
                ordre.setStatusOrdre(Constantes.ETAT_ATTENTE);
            }
            return y;
        }
        return null;
    }

    private YvsProdOrdreFabrication saveProduction(OrdreFabrication of) {
        selectedOf = UtilProd.buildEntityOf(of);
        return saveProduction(selectedOf, of, true);
    }

    private YvsProdOrdreFabrication saveProduction(YvsProdOrdreFabrication y, OrdreFabrication ordre, boolean msg) {
        y.setAuthor(currentUser);
        y.setSociete(currentAgence.getSociete());
        y.setDateUpdate(new Date());
        if (ordre.getId() <= 0) {
            y.setId(null);
            y = (YvsProdOrdreFabrication) dao.save1(y);
            listeOrdreF.add(0, y);
        } else {
            dao.update(y);
            int idx = listeOrdreF.indexOf(y);
            if (idx >= 0) {
                listeOrdreF.set(idx, y);
            }
        }
        if (!saveComposantOF(y, ordre) ? msg : false) {
            getWarningMessage("Les composants de l'ordre de fabrication n'ont pas été enregistré !");
        } else {
            this.ordre.getNomenclature().setComposants(dao.loadNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"}, new Object[]{new YvsProdNomenclature(ordre.getNomenclature().getId())}));
        }
        if (ordre.isSuiviOperations()) {
            if (!saveOperationsOF(y, ordre) ? msg : false) {
                getWarningMessage("Les opérations de l'ordre de fabrication n'ont pas été enregistré !");
            }
        }
        update("tableOF");
        return y;
    }

    private boolean saveComposantOF(YvsProdOrdreFabrication prod, OrdreFabrication ordre) {
        try {
            if (ordre != null ? !ordre.getListComposantOf().isEmpty() : false) {
                Double cout = 0d;
                for (YvsProdComposantOF cof : ordre.getListComposantOf()) {
                    if (cof.getId() <= 0) {
                        //appliquer le dépôt par defaut de conso d'un composant
                        cof.setDepotConso((YvsBaseDepots) dao.loadOneByNameQueries("YvsProdDefaultDepotComposants.findOneDepot", new String[]{"composant", "site"}, new Object[]{cof.getArticle(), prod.getSiteProduction()}));
                        cof.setAuthor(currentUser);
                        cof.setDateUpdate(new Date());
                        cof.setDateSave(new Date());
                        cof.setOrdreFabrication(prod);
                        if (cof.getDepotConso() != null) {
                            cout = dao.getPr(cof.getArticle().getId(), cof.getDepotConso().getId(), 0, ordre.getDateDebutLancement(), cof.getUnite().getId());
                        } else {
                            cout = dao.getPr(cof.getArticle().getId(), ordre.getDepotMp().getId(), 0, ordre.getDateDebutLancement(), cof.getUnite().getId());
                        }
                        cof.setId(null);
                        cof = (YvsProdComposantOF) dao.save1(cof);
                    } else {
                        cof.setDateUpdate(new Date());
                        cof.setAuthor(currentUser);
                        dao.update(cof);
                    }
                }
            }
        } catch (Exception ex) {
            getException("Production Error !", ex);
            return false;
        }
        return true;
    }

    private YvsProdOrdreFabrication createOF(OrdreFabrication ordre, YvsBaseArticles article, YvsProdGammeArticle gamme, YvsProdNomenclature nomenclature, YvsBaseDepots depot, Double quantite) {
        YvsProdOrdreFabrication entity = null;
        if (article != null ? article.getId() > 0 : false) {
            if (gamme != null ? gamme.getId() > 0 : false) {
                if (nomenclature != null ? nomenclature.getId() > 0 : false) {
                    String ref = genererReference(Constantes.PROD_TYPE_PROD_NAME, ordre.getDateDebutLancement());
                    if ((ref != null) ? ref.trim().equals("") : true) {
                        return null;
                    }
                    nomenclature.setComposants(dao.loadNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"}, new Object[]{nomenclature}));
                    entity = new YvsProdOrdreFabrication();
                    entity.setCodeRef(ref);
                    entity.setNumeroIdentification(entity.getCodeRef() + "_" + article.getRefArt());
                    entity.setArticle(article);
                    entity.setNomenclature(nomenclature);
                    entity.setGamme(gamme);
                    entity.setDateDebut(ordre.getDateDebutLancement());
                    entity.setHeureLancement(ordre.getHeureDeLancement());
                    entity.setPriorite(ordre.getPriorite());
                    entity.setQuantite(quantite);
                    entity.setStatutOrdre(Constantes.ETAT_ATTENTE);
                    entity.setStatutDeclaration(Constantes.ETAT_ATTENTE);
                    entity.setDateSave(new Date());
                    entity.setTypeOf(ordre.getTypeOf());
                    entity.setSuiviStockByOperation(ordre.isSuiviStockByOperation());
                    entity.setSuiviOperation(ordre.isSuiviOperations());
                    ordre.setListComposantOf(calculBesoins(nomenclature.getComposants(), nomenclature, ordre, true));
                    loadListOperationsOf(ordre, gamme.getOperations());
                }
            }
        }
        return entity;
    }

    public void saveOFSousProduit() {
        if (sous != null) {
            int idx = sous.getArticles().getNomenclatures().indexOf(new YvsProdNomenclature(sous.getNomenclature().getId()));
            if (idx > -1) {
                YvsProdNomenclature nomenclature = sous.getArticles().getNomenclatures().get(idx);
                idx = sous.getArticles().getGammes().indexOf(new YvsProdGammeArticle(sous.getGamme().getId()));
                if (idx > -1) {
                    YvsProdGammeArticle gamme = sous.getArticles().getGammes().get(idx);
                    sous.setSuiviOperations(ordre.isSuiviOperations());
                    YvsBaseArticles art = new YvsBaseArticles(sous.getArticles().getId(), sous.getArticles().getRefArt(), sous.getArticles().getDesignation());
                    YvsProdOrdreFabrication entity = createOF(sous, art, gamme, nomenclature, new YvsBaseDepots(sous.getDepotMp().getId()), sous.getQuantitePrevu());
                    if (entity != null) {
                        YvsProdOrdreFabrication y = saveProduction(entity, sous, true);
                        sous.setId(y.getId());
                        if (sous.getStatusOrdre() == null) {
                            sous.setStatusOrdre(Constantes.ETAT_ATTENTE);
                        }
                        succes();
                    }
                }
            }
        }
    }

    public void managedInsideCout(YvsProdComposantOF ra) {
        selectedComposant = ra;
        loadComposant(true, true);
    }

    public void loadComposant(boolean avance, boolean init) {
        p_composant.addParam(new ParametreRequete("y.ordreFabrication.nomenclature", "nomenclature", new YvsProdNomenclature(ordre.getNomenclature().getId()), "=", "AND"));
        p_composant.addParam(new ParametreRequete("y.unite", "unite", selectedComposant.getUnite(), "=", "AND"));
        p_composant.addParam(new ParametreRequete("y.id", "id", selectedComposant.getId(), "!=", "AND"));
        composantsOF = p_composant.executeDynamicQuery("YvsProdComposantOF", "y.ordreFabrication.codeRef DESC", avance, init, dao);
        update("tab_composant_lie");
    }

    public void tooglesInsideCout(boolean insideCout) {
        for (YvsProdComposantOF ra : composantsOF) {
            if (ra.getId() > 0) {
                ra.setInsideCout(insideCout);
                ra.setAuthor(currentUser);
                ra.setDateUpdate(new Date());
                dao.update(ra);
                recalculOrder(ra.getOrdreFabrication());
            }
        }
        succes();
    }

    public void toogleInsideCout(YvsProdComposantOF ra) {
        if (ra.getId() > 0) {
            ra.setInsideCout(!ra.getInsideCout());
            ra.setAuthor(currentUser);
            ra.setDateUpdate(new Date());
            dao.update(ra);
            recalculOrder(ra.getOrdreFabrication());
            succes();
        }
    }

    public void openInfosPFSousProduit(YvsProdComposantOF composant) {
        sous = new OrdreFabrication();
        sous.setArticles(UtilProd.buildBeanArticles(composant.getArticle()));
        sous.setDateDebutLancement(ordre.getDateDebutLancement());
        sous.setHeureDeLancement(ordre.getHeureDeLancement());
        sous.setDepotMp(UtilCom.buildSimpleBeanDepot(composant.getDepotConso()));
        sous.setDepotPf(ordre.getDepotPf());
        sous.setGenerateOfSousProduit(false);
        sous.setQuantitePrevu(composant.getQuantitePrevu());

        if (sous.getArticles().getNomenclatures() != null ? sous.getArticles().getNomenclatures().isEmpty() : true) {
            sous.getArticles().setNomenclatures(dao.loadNameQueries("YvsProdNomenclature.findByArticleActif", new String[]{"article"}, new Object[]{composant.getArticle()}));
        }
        if (sous.getArticles().getGammes() != null ? sous.getArticles().getGammes().isEmpty() : true) {
            sous.getArticles().setGammes(dao.loadNameQueries("YvsProdGammeArticle.findByArticleActif", new String[]{"article"}, new Object[]{composant.getArticle()}));
        }
        update("blog_of_sous_produit");
    }

    public void openDlgPoste(String source) {
        this.sourcePC = source;
        openDialog("dlgPostC");
    }

    public void addOrReplacePoste(SelectEvent ev) {
        if (operation != null) {
            PosteCharge po = UtilProd.buildBeanPosteCharge((YvsProdPosteCharge) ev.getObject());
            if ("MA".equals(sourcePC)) {
                operation.setMachine(po);
                update("op_poste_machine");
            } else {
                operation.setMainOeuvre(po);
                update("op_poste_mo");
            }
        } else {
            getErrorMessage("Modification non suporté", "Aucune opéraion n'a été trouvé");
        }
    }

    private boolean saveOperationsOF(YvsProdOrdreFabrication prod, OrdreFabrication ordre) {
        try {
            if (!ordre.getListOperationsOf().isEmpty()) {
                List<YvsProdFluxComposant> flux;
                for (YvsProdOperationsOF op : ordre.getListOperationsOf()) {
                    op.setAuthor(currentUser);
                    op.setDateUpdate(new Date());
                    op.setOrdreFabrication(prod);
                    flux = new ArrayList<>(op.getComposants());
                    op.getComposants().clear();
                    if (op.getId() != null ? op.getId() <= 0 : true) {
                        op.setStatutOp(Constantes.ETAT_ATTENTE);
                        op.setDateSave(new Date());
                        op.setId(null);
                        op = (YvsProdOperationsOF) dao.save1(op);
                    } else {
                        dao.update(op);
                    }
                    op.setComposants(flux);
                    saveFlux(op, ordre);
                    selectedOp = op;
                    operation = UtilProd.buildBeanPhaseOf(op);
                }
            }
        } catch (Exception ex) {
            getException("Production Error !", ex);
            return false;
        }
        return true;
    }

    public void applyPropertyPhase(boolean all) {
        if (operation != null) {
            if (operation.getStatutOp().equals(Constantes.ETAT_TERMINE)) {
                getErrorMessage("Vous ne pouvez pas modifier cette opération..elle est déjà terminé");
                return;
            }
            if (!ordre.canEditable(true)) {
                getErrorMessage("Vous ne pouvez pas modifier cet ordre..il est déjà terminé");
                return;
            }
            selectedOp = UtilProd.buildEntityPhaseOf(operation, currentUser);
            if (operation.getId() > 0) {
                dao.update(selectedOp);
                int idx = ordre.getListOperationsOf().indexOf(selectedOp);
                if (idx >= 0) {
                    ordre.getListOperationsOf().set(idx, selectedOp);
                }
            } else {
                if (selectedOf != null ? selectedOf.getId() < 1 : true) {
                    getErrorMessage("Vous devez selectionner un ordre de fabrication");
                    return;
                }
                selectedOp.setId(null);
                selectedOp.setOrdreFabrication(selectedOf);
                selectedOp = (YvsProdOperationsOF) dao.save1(selectedOp);
                operation.setId(selectedOp.getId());
                ordre.getListOperationsOf().add(selectedOp);
            }
            if (!all) {
                succes();
            }
            update("table_op_use_mp");
        } else {
            getErrorMessage("Aucune opération n'a été selectionné !");
        }
    }

    private void saveFlux(YvsProdOperationsOF o, OrdreFabrication ordre) {
        if (o.getComposants() != null) {
            List<YvsProdOfIndicateurSuivi> temp;
            for (YvsProdFluxComposant fc : o.getComposants()) {
                if (fc.getComposant() != null) {
                    temp = null;
                    fc.setOperation(o);
                    fc.setComposant(findComposantByArticle(fc.getComposant().getArticle(), ordre));
                    if (fc.getIndicateursSuivis() != null) {
                        temp = new ArrayList<>(fc.getIndicateursSuivis());
                        fc.getIndicateursSuivis().clear();
                    }
                    if (fc.getId() == null ? true : fc.getId() <= 0) {
                        fc.setId(null);
                        fc.setAuthor(currentUser);
                        fc.setDateSave(new Date());
                        fc.setDateUpdate(new Date());
                        fc = (YvsProdFluxComposant) dao.save1(fc);
                    } else {
                        dao.update(fc);
                    }
                    if (temp != null) {
                        for (YvsProdOfIndicateurSuivi in : temp) {
                            in.setComposant(fc);
                            if (in.getId() == null ? true : in.getId() <= 0) {
                                in.setId(null);
                                in = (YvsProdOfIndicateurSuivi) dao.save1(in);
                            } else {
                                dao.update(in);
                            }
                        }
                        fc.setIndicateursSuivis(temp);
                    }
                }
            }
        }
    }

    public void finishOrdre() {
        boolean finish = true;
        for (int i = 0; i < ordre.getListOperationsOf().size(); i++) {
            YvsProdOperationsOF ope = ordre.getListOperationsOf().get(i);
            if (!ope.getStatutOp().equals(Constantes.ETAT_TERMINE)) {
                selectedOp = ope;
                if (!finishOperation(true)) {
                    finish = false;
                    break;
                }
            }
        }
        if (finish) {
            if (changeStateOf(Constantes.ETAT_TERMINE)) {
                succes();
            }
        }
    }

    public void suspendOf() {
        if (ordre.getId() > 0) {
            if (ordre.getId() == selectedOf.getId()) {
                selectedOf.setSuspendu(true);
                ordre.setSuspendu(true);
                dao.update(selectedOf);
                update("panel_state");
                update("tableOF");
            }
        } else {
            getErrorMessage("Aucun ordre de fabrication n'a été trouvé !");
        }
    }

    public boolean changeStateOf(String state) {
        if (state.charAt(0) == Constantes.STATUT_DOC_SUSPENDU) {
            openDialog("dlgConfirmSuspendOf");
            return true;
        }
        return changeStateOf(ordre, selectedOf, state);
    }

    public boolean changeStateOfW(YvsProdOrdreFabrication of, String etat) {
        if (of != null) {
            return changeStateOf(UtilProd.buildBeanOf(of), of, etat);
        }
        return false;
    }

    public boolean changeStateOf(OrdreFabrication ordre, YvsProdOrdreFabrication selectedOf, String state) {
        if (state.equals(Constantes.ETAT_PROD_LANCE)) {
            if (!autoriser("prod_launch_of")) {
                openNotAccesByCode();
                return false;
            }
        } else if ((ordre.getStatusOrdre().equals(Constantes.ETAT_TERMINE) || ordre.getStatusOrdre().equals(Constantes.ETAT_RELANCE)) && ordre.getStatutDeclaration().equals(Constantes.ETAT_TERMINE) && ordre.getStatusOrdre().equals(Constantes.ETAT_CLOTURE)) {
            getErrorMessage("Impossible de modifier cet ordre de fabrication !");
            return false;
        }
        if (state.equals(Constantes.ETAT_CLOTURE)) {
            if (!autoriser("production_cloture_of")) {
                openNotAccesByCode();
                return false;
            }
            //vérifie le statut de déclaration            
            if (!ordre.getStatutDeclaration().equals(Constantes.ETAT_TERMINE)) {
                if (!controleQteDeclare(new DeclarationProduction(), true)) {
                    openDialog("dlgNotAllDeclare");
                    return false;
                }
            }
        }
        return changeStateOf(ordre, selectedOf, state, true);
    }

    public boolean changeStateOf(String state, boolean maj) {
        return changeStateOf(ordre, selectedOf, state, maj);
    }

    public boolean changeStateOf(OrdreFabrication ordre, YvsProdOrdreFabrication selectedOf, String state, boolean maj) {
        try {
            if (ordre.getId() > 0) {
                //si l'ordre n'est pas déjà clôturé
                if (!ordre.getStatusOrdre().equals(Constantes.ETAT_CLOTURE)) {
                    // Si l'on clôture l'OF, on marque aussi la déclaration terminé
                    if (state.equals(Constantes.ETAT_CLOTURE)) {
                        selectedOf.setStatutDeclaration(Constantes.ETAT_TERMINE);
                        ordre.setStatutDeclaration(Constantes.ETAT_TERMINE);
                    } else if (state.equals(Constantes.ETAT_ATTENTE)) {
                        for (YvsProdDeclarationProduction d : selectedOf.getDeclarations()) {
                            if (!controleChangeDeclaration(d)) {
                                return false;
                            }
                        }
                    }
                    selectedOf.setStatutOrdre(state);
                    selectedOf.setSuspendu(false);
                    selectedOf.setDateUpdate(new Date());
                    if (state.equals(Constantes.ETAT_ATTENTE)) {
                        selectedOf.setStatutDeclaration(Constantes.ETAT_ATTENTE);
                    }
                    dao.update(selectedOf);
                    ordre.setStatusOrdre(state);
                    update("panel_state");
                    int idx = listeOrdreF.indexOf(selectedOf);
                    if (idx >= 0) {
                        listeOrdreF.get(idx).setStatutOrdre(state);
                        if (state.equals(Constantes.ETAT_ATTENTE)) {
                            listeOrdreF.get(idx).setStatutDeclaration(Constantes.ETAT_ATTENTE);
                        }
                        update("tableOF");
                    }
                    if (maj) {
                        //met toutes les opérations non encore terminé en attente
                        if (state.equals(Constantes.ETAT_ATTENTE)) {
                            for (YvsProdOperationsOF op : ordre.getListOperationsOf()) {
                                op.setDateDebut(ordre.getDateDebutLancement());
                                op.setHeureDebut(ordre.getHeureDeLancement());
                                op.setStatutOp(Constantes.ETAT_ATTENTE);
                                dao.update(op);
                            }
                            for (YvsProdDeclarationProduction d : selectedOf.getDeclarations()) {
                                dao.delete(d);
                            }
                            for (YvsProdOperationsOF o : selectedOf.getOperations()) {
                                String rq = "DELETE FROM yvs_prod_suivi_operations WHERE operation_of = ?";
                                Options[] param = new Options[]{new Options(o.getId(), 1)};
                                dao.requeteLibre(rq, param);
                                o.getSuiviOperations().clear();
                            }
                            ordre.getDeclarations().clear();
                            selectedOf.getDeclarations().clear();
                            if (idx >= 0) {
                                listeOrdreF.get(idx).getDeclarations().clear();
                                listeOrdreF.get(idx).setOperations(selectedOf.getOperations());
                                update("tableOF");
                            }
                            update("zone_diplay_op");
                            update("tablePhase_:table_phase");
                        }
                    }
                    // si on lance les OP de l'OF
                    if (state.equals(Constantes.ETAT_PROD_LANCE)) {
                        if (!ordre.getListOperationsOf().isEmpty()) {
                            //Démarre la première opération (en supposant que c'est bien ordonné)
                            changeStatutOp(ordre, selectedOf, ordre.getListOperationsOf().get(0), Constantes.ETAT_ENCOURS, true);
                            update("zone_diplay_op");
                            return true;
                        } else {
                            getErrorMessage("Aucune opération n'a été trouvé !");
                            return false;
                        }
                    }
                    return true;
                } else {
                    getErrorMessage("Cet ordre de fabrication est cloturé!");
                }
            } else {
                getErrorMessage("Aucun ordre de fabrication n'est selectionné !");
            }
        } catch (Exception ex) {
            getException("Error (changeStateOf) ", ex);
        }
        return false;
    }
    /*Modifie les propriétés de l'opération */

    public void navigueInListOperation(boolean next) {
        if (!ordre.getListOperationsOf().isEmpty()) {
            if (selectedOp != null) {
                int idx = ordre.getListOperationsOf().indexOf(selectedOp);
                if (idx >= 0) {
                    idx = (next) ? idx + 1 : idx - 1;
                }
                idx = (idx < 0 || idx >= ordre.getListOperationsOf().size()) ? 0 : idx;
                selectedOp = ordre.getListOperationsOf().get(idx);
            } else {
                selectedOp = ordre.getListOperationsOf().get(0);
            }
            openPropertiesOperation(selectedOp);
            update("formPptePhase");
            update("tablePhase_:table_phase");
        } else {
            getErrorMessage("Aucune opération trouvé pour cette OF!");
        }
    }
    private List<YvsProdComposantOF> listeComposants = new ArrayList<>();
    private List<String> colonnes = new ArrayList<>();

    public List<YvsProdComposantOF> getListeComposants() {
        return listeComposants;
    }

    public void setListeComposants(List<YvsProdComposantOF> listeComposants) {
        this.listeComposants = listeComposants;
    }

    public List<String> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<String> colonnes) {
        this.colonnes = colonnes;
    }

    public void displayFluxComposant() {
        List<YvsProdFluxComposant> list = dao.loadNameQueries("YvsProdFluxComposant.findByIdOf", new String[]{"ordre"}, new Object[]{selectedOf});
        listeComposants.clear();
        YvsProdComposantOF item;
        YvsProdFluxComposant temp;
        long id = -1000;
        int nb = 0;
        colonnes.clear();
        for (YvsProdComposantOF co : ordre.getListComposantOf()) {
            item = co;
            item.setComposantsUsed(new ArrayList<YvsProdFluxComposant>());
            for (YvsProdOperationsOF op : ordre.getListOperationsOf()) {
                temp = getOneFlux(op, co, list);
                if (temp == null) {
                    temp = new YvsProdFluxComposant(id++);
                    temp.setOperation(op);
                    temp.setComposant(co);
                }
                item.getComposantsUsed().add(temp);
                if (nb == 0) {
                    colonnes.add(op.getCodeRef());
                }
            }
            nb++;
            listeComposants.add(item);
        }
    }

    private YvsProdFluxComposant getOneFlux(YvsProdOperationsOF op, YvsProdComposantOF oc, List<YvsProdFluxComposant> list) {
        for (YvsProdFluxComposant f : list) {
            if (f.getComposant().equals(oc) && f.getOperation().equals(op)) {
                return f;
            }
        }
        return null;
    }

//    private YvsProdFluxComposant getOneFluxBySens(YvsProdComposantOF oc, List<YvsProdFluxComposant> list, Character sens) {
//        for (YvsProdFluxComposant f : list) {
//            if (f.getComposant().equals(oc) && f.getSens().equals(sens)) {
//                return f;
//            }
//        }
//        return null;
//    }
    private YvsProdComposantOF findComposantByArticle(YvsBaseArticles art, OrdreFabrication ordre) {
        for (YvsProdComposantOF c : ordre.getListComposantOf()) {
            if (c.getArticle().equals(art)) {
                return c;
            }
        }
        return null;
    }

//    private Integer giveOrdreInNom(YvsProdNomenclature n, YvsBaseArticles art) {
//        Integer o = (Integer) dao.loadOneByNameQueries("YvsProdComposantNomenclature.findOrdreByNom", new String[]{"article", "nomenclature"}, new Object[]{art, n});
//        return (o != null) ? o : 0;
//    }
    private YvsProdComposantNomenclature findComposantInNomenByArticle(YvsBaseArticles art) {
        if (ordre.getNomenclature().getId() > 0) {
            int idx = ordre.getArticles().getNomenclatures().indexOf(new YvsProdNomenclature(ordre.getNomenclature().getId()));
            if (idx >= 0) {
                for (YvsProdComposantNomenclature c : ordre.getArticles().getNomenclatures().get(idx).getComposants()) {
                    if (c.getArticle().equals(art)) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    public Double giveStockMp(YvsProdComposantOF art) {
        if (art != null && currentSession != null) {
            long depot = (art.getDepotConso() != null) ? art.getDepotConso().getId() : currentSession.getDepot().getId();
            Double d = dao.stocksReel(art.getArticle().getId(), -1, depot, 0L, 0L, currentSession.getDateSession(), (art.getUnite() != null) ? art.getUnite().getId() : -1, (art.getLotSortie() != null) ? art.getLotSortie().getId() : -1);
            art.setStock(d != null ? d : 0);
            return d;
        }
        return 0.0;
    }

    public Double giveStockMp(YvsProdComposantOF art, long unite) {
        if (art != null && currentSession != null) {
            long depot = (art.getDepotConso() != null) ? art.getDepotConso().getId() : currentSession.getDepot().getId();
            Double d = dao.stocksReel(art.getArticle().getId(), -1, depot, 0L, 0L, currentSession.getDateSession(), unite, (art.getLotSortie() != null) ? art.getLotSortie().getId() : -1);
            art.setStock(d != null ? d : 0);
            return d;
        }
        return 0.0;
    }

    public void resetOperation() {
        selectedOp = new YvsProdOperationsOF();
        operation = new OperationsOF();
    }

    public void resetComposant() {
        selectedComposant = new YvsProdComposantOF();
        composant = new ComposantsOf();
    }

    public void openPropertiesOperation(YvsProdOperationsOF op) {
        selectedOp = op;
        operation = UtilProd.buildBeanPhaseOf(op);
        onselectOperation(op);
        operation.setComposants(new ArrayList<>(selectedOp.getComposants()));
        operation.setOperations(new ArrayList<>(selectedOp.getSuiviOperations()));
    }

    private YvsProdOperationsOF findNextOpOder(YvsProdOperationsOF op, boolean next) {
        for (YvsProdOperationsOF o : ordre.getListOperationsOf()) {
            if (next ? (o.getNumero() - op.getNumero() == 10) : (op.getNumero() - o.getNumero() == 10)) {
                return o;
            }
        }
        return null;
    }

    public boolean findAndSelectNextOperation(YvsProdOperationsOF op, boolean next) {
        if (op != null) {
            int numero = op.getNumero();
            YvsProdOperationsOF nextOp = findNextOpOder(op, next);
            if (nextOp != null) {
                if (!nextOp.getStatutOp().equals(Constantes.ETAT_ENCOURS)) {
                    changeStatutOp(nextOp, Constantes.ETAT_ENCOURS, false);
                }
                openFluxComposantOperation(nextOp);
                calculBesoinProduit();
                return true;
            } else {
                if (!paramProduction.getDeclareWhenFinishOf()) {
                    getInfoMessage("Plus d'opérations...");
                }
                //ouvre le formulaire de déclaration
                if (next && !ordre.getNomenclature().getTypeNomenclature().equals(Constantes.PROD_TYPE_NOMENCLATURE_TRANSFORMATION)) {
                    if (!ordre.getStatutDeclaration().equals(Constantes.ETAT_TERMINE)) {
                        resetDeclaration();
                        double quantite = this.qteADeclarer > this.qteAProduire ? this.qteADeclarer : this.qteAProduire;
                        if (quantite > 0) {
                            declaration.setQuantite(quantite);
                            update("form_declaration");
                        }
                        closeDialog("dlgEditFlux");
                        if (paramProduction.getDeclareWhenFinishOf()) {
                            if (declaration.getQuantite() > 0) {
                                saveDeclaration(false);
                            }
                        } else {
                            openDialog("dlgDeclaration");
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void openFluxComposantOperation(YvsProdOperationsOF op) {
        selectedOp = op;
        //charge les attributs de base de l'opération
        operation = UtilProd.buildBeanPhaseOf(op);
        //charge les autres attributs de l'opération
        onselectOperation(selectedOp);
        // charge le tableau de suivi des consommations
        buildFluxStockOp(op);
    }

    private void buildFluxStockOp(YvsProdOperationsOF op) {
        YvsProdOfSuiviFlux item;
        if (selectedFlux == null) {
            selectedFlux = new YvsProdFluxComposant();
        }
        if (selectedFlux.getListeSuiviFlux() != null) {
            selectedFlux.getListeSuiviFlux().clear();
        } else {
            selectedFlux.setListeSuiviFlux(new ArrayList<YvsProdOfSuiviFlux>());
        }
        for (YvsProdFluxComposant fc : op.getComposants()) {
            item = new YvsProdOfSuiviFlux(-fc.getId());
            item.setComposant(fc);
            item.setAuthor(currentUser);
            item.setQuantiteSave(giveQteFluxEncours(fc));
            if (!fc.getSens().equals(Constantes.STOCK_SENS_NEUTRE)) {
                item.setQuantite(0d); //Quantité à déclarer
            } else {
                item.setQuantite(fc.getQuantiteResteFlux());
                item.setQuantitePerdue(fc.getQuantitePerdue());
            }
            if (paramProduction.getDeclareWhenFinishOf()) {
                if (!fc.getSens().equals(Constantes.STOCK_SENS_NEUTRE)) {
                    item.setQuantite(fc.getQuantite() * qteAProduire / ordre.getQuantitePrevu());
                } else {
                    // récupère la quantité généré à l'étape précédente
                    YvsProdOperationsOF prevOp = findNextOpOder(fc.getOperation(), false);
                    if (prevOp != null) {
                        //récupère les qté
                        Double qteOpe = (Double) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.findByOp", new String[]{"operation"}, new Object[]{fc.getOperation()});
                        qteOpe = qteOpe != null ? qteOpe : 0;
                        item.setQuantite(qteOpe);
                    }
                }
            }
            selectedFlux.getListeSuiviFlux().add(item);
        }
        update("form_detail_edit_flux");
        update("table_suivi_edit_flux");
    }

    private void onselectOperation(YvsProdOperationsOF op) {
        if (op != null) {
            //Récupère la liste des composant utilisable à cette opération
            op.setComposants(dao.loadNameQueries("YvsProdFluxComposant.findByOpération", new String[]{"operation"}, new Object[]{op}));
            //Charge les cycles opératoire de l'opération (suivi_opérations)     
            op.setSuiviOperations(dao.loadNameQueries("YvsProdSuiviOperations.findByOperation", new String[]{"operation"}, new Object[]{op}));
            if (op.getSuiviOperations() != null ? !op.getSuiviOperations().isEmpty() : false) {
                YvsProdSuiviOperations s = op.getSuiviOperations().get(0);
                copyBeanSuiviOperation(s);
//                loadMyCycleSession(session, op);                
            } else {
                copyBeanSuiviOperation(null);
            }
        }
    }

    public void addOrRemoveFromOp(YvsProdFluxComposant cop) {
        if (ordre.getStatusOrdre().equals(Constantes.ETAT_TERMINE)) {
            getErrorMessage("Vous ne pouvez pas modifier cet ordre..il est déjà terminé");
            return;
        }
        if (cop.getId() > 0) {
            // vérifie s'il ya des flux généré pour cette ligne
            Long N = (Long) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.countByComposant", new String[]{"composant"}, new Object[]{cop});
            N = N != null ? N : 0;
            if (N > 0) {
                getErrorMessage("Impossible de modifier la ligne, des flux de stocks ont déja été enregistrés !");
                return;
            }
            dao.delete(cop);
            cop.setId(-cop.getId());
            cop.setInListByDefault(!cop.isInListByDefault());
        } else {
            cop.setInListByDefault(!cop.isInListByDefault());
            // récupère dans la liste des composants
            YvsProdComposantNomenclature co = findComposantInNomenByArticle(cop.getComposant().getArticle());
            if (co != null) {
                cop.setSens(co.getType().equals(Constantes.PROD_OP_TYPE_COMPOSANT_NORMAL) ? Constantes.STOCK_SENS_SORTIE : Constantes.STOCK_SENS_ENTREE);
                cop.setQuantite(co.getQuantite());
            }
            cop.setId(null);
            cop = (YvsProdFluxComposant) dao.save1(cop);
        }
        update("table_op_use_mp");
    }

    public void loadDetailFluxCOmposant(YvsProdFluxComposant flux) {
        selectedFlux = flux;
        selectedFlux.setListeSuiviFlux(dao.loadNameQueries("YvsProdOfSuiviFlux.findByComposant", new String[]{"composant"}, new Object[]{flux}));
//        if (currentCreneauEquipe != null ? currentCreneauEquipe.getId() > 0 : false) {
//            suiviFlux.setTranche(UtilGrh.buildTrancheHoraire(currentCreneauEquipe.getTranche()));
//            suiviFlux.setEquipe(UtilProd.buildBeanEquipeProduction(currentCreneauEquipe.getEquipe()));
//        }
        update("table_suivi_flux");
        update("table_op_use_mp");
        update("header_detail_flux");
        update("form_detail_flux");
    }

    public void loadAllIndicateurs(YvsProdFluxComposant flux) {
        selectedFlux = flux;
        openDialog("dlgAllIndc");
        update("table_indicateur_xx table_op_use_mp");
    }

    public void deleteOrdreFabrication(YvsProdOfIndicateurSuivi in) {
        dao.delete(in);
        selectedFlux.getIndicateursSuivis().remove(in);
        update("table_indicateur_xx");
    }

    public void openOneComposantToUpdate(YvsProdComposantOF bean) {
        //charche les flux par opérations
        if (bean.getId() > 0) {
            bean.setComposantsUsed(dao.loadNameQueries("YvsProdFluxComposant.findByComposant", new String[]{"composant"}, new Object[]{bean}));
        }
        selectedComposant = bean;
        this.composant = UtilProd.buildComposantOf(selectedComposant);
        if (composant.getDepotConso().getId() <= 0 && currentSession != null) {
            composant.setDepotConso(UtilCom.buildSimpleBeanDepot(currentSession.getDepot()));
        }
//        loadAllFluxComposantCo(bean);
        update("formPpteComposant");
        update("tbv_composant_of");
    }

    public boolean finishOperation(boolean controle) {
        if (!controle) {
            //si l'on demande de ne pas contrôler la conformité des consommation, il faut disposé de ce droit
            if (!autoriser("prod_force_declaration")) {
                getErrorMessage("Vous ne pouvez déclarer la fin de cet ordre de fabrication!", "les quantités ne sont pas conforme");
                return false;
            }

        }
        return finishOperation(selectedOp, controle);
    }

    public boolean finishAllOperation() {
        for (YvsProdOperationsOF op : ordre.getListOperationsOf()) {
            finishOperation(op, false);
        }
        return true;
    }

    public boolean finishOperation(YvsProdOperationsOF selectedOp, boolean controle) {
        this.selectedOp = selectedOp;
        if (selectedOp.getComposants() != null) {
            if (controle) {
                if (!controleQteFlux(selectedOp)) {
                    openDialog("dlgConfirmFinOp");
                    return false;
                }
            }
            if (changeStatutOp(selectedOp, Constantes.ETAT_TERMINE, true)) {
                YvsProdComposantOF co;
//                List<YvsProdComposantOF> list = new ArrayList<>();
//                for (YvsProdFluxComposant pflux : selectedOp.getComposants()) {
//                    if (pflux.getComposant().getArticle() != null) {
//                        co = findComposantByArticle(pflux.getComposant().getArticle(), ordre);
//                        if (!list.contains(co)) {
//                            list.add(co);
//                        }
//                    }
//                }
                applyCoutComposant(selectedOp);
                succes();
            }
            return true;

        } else {
            getErrorMessage("Impossible de terminer cet OF; les composant n'ont pas été paramétré ou sont mal paramétré !");
        }
        return false;
    }

    public void changePreviousStateOf() {
        if (ordre.getId() > 0) {
            switch (ordre.getStatusOrdre()) {
                case Constantes.ETAT_RELANCE:
                case Constantes.ETAT_TERMINE:
                    if (!ordre.getStatutDeclaration().equals(Constantes.ETAT_TERMINE)) {
                        changeStateOf(Constantes.ETAT_ENCOURS);
                    } else {
                        getErrorMessage("Impossible de ramener le statut", "l'OF est entièrement déclaré !");
                    }
                    break;
                case Constantes.ETAT_ENCOURS:
                    changeStateOf(Constantes.ETAT_PROD_LANCE);
                    break;
                case Constantes.ETAT_PROD_LANCE:
                    changeStateOf(Constantes.ETAT_ATTENTE);
                    break;
            }
        }
    }

    public boolean changeStatutOp(String statut) {
        boolean re = changeStatutOp(selectedOp, statut, true);
        update("table_op_use_mp");
        return re;
    }

    public boolean changeStatutOp(YvsProdOperationsOF selectedOp, String statut, boolean maj) {
        return changeStatutOp(ordre, selectedOf, selectedOp, statut, maj);
    }

    public boolean changeStatutOp(OrdreFabrication ordre, YvsProdOrdreFabrication selectedOf, YvsProdOperationsOF selectedOp, String statut, boolean maj) {
        try {
            if (ordre.canChangeStatutOperation()) {
                List<YvsProdFluxComposant> lf = new ArrayList<>(selectedOp.getComposants());
                selectedOp.setStatutOp(statut);
                if (operation != null) {
                    operation.setStatutOp(statut);
                }
                selectedOp.setDateUpdate(new Date());
                selectedOp.setAuthor(currentUser);
                selectedOp.getComposants().clear();
                dao.update(selectedOp);
                selectedOp.setComposants(lf);
                if (operation != null) {
                    operation.setComposants(lf);
                }
                int idx = ordre.getListOperationsOf().indexOf(selectedOp);
                if (idx > -1) {
                    ordre.getListOperationsOf().set(idx, selectedOp);
                }
                if (maj) {
                    miseAjourStatutOrdre(ordre, selectedOf);
                }
                update("zone_state_op");
                return true;
            } else {
                getErrorMessage("Vous ne pouvez modifier le statut de l'opération", "Vérifier la coherence avec le statut de l'ordre de fabrication");
                return false;
            }
        } catch (Exception ex) {
            getException("Error (changeStatutOp) ", ex);
            return false;
        }
    }

    public void changeLineOperation(CellEditEvent ev) {
        if (ev.getNewValue() != null) {
            YvsProdOperationsOF line = ordre.getListOperationsOf().get(ev.getRowIndex());
            if (ordre.canChangeStatutOperation()) {
                if (line != null) {
                    dao.update(line);
                    miseAjourStatutOrdre();
                    succes();
                }
            } else {
                getErrorMessage("L'ordre de fabrication n'est encore validé !");
                line.setStatutOp((String) ev.getOldValue());
            }
            update("table_phase");
        }
    }

    public void miseAjourStatutOrdre() {
        miseAjourStatutOrdre(ordre, selectedOf);
    }

    public void miseAjourStatutOrdre(OrdreFabrication ordre, YvsProdOrdreFabrication selectedOf) {
        int terminer = 0;
        boolean encours = false;
        for (YvsProdOperationsOF y : ordre.getListOperationsOf()) {
            if (!y.getStatutOp().equals(Constantes.ETAT_TERMINE)) {
                if (y.getStatutOp().equals(Constantes.ETAT_ENCOURS)) {
                    encours = true;
                }
            } else {
                terminer++;
            }
        }
        if (terminer >= ordre.getListOperationsOf().size()) {
            changeStateOf(ordre, selectedOf, Constantes.ETAT_TERMINE, false);
        } else if (terminer > 0 || encours) {
            changeStateOf(ordre, selectedOf, Constantes.ETAT_ENCOURS, false);
        } else {
            changeStateOf(ordre, selectedOf, Constantes.ETAT_PROD_LANCE, false);
        }
    }

    public void miseAjourComposant(YvsProdComposantOF y) {
        if (y != null ? y.getId() > 0 : false) {
            if (selectedOf.getSuiviStockByOperation()) {
                Double d = (Double) dao.loadObjectByNameQueries("YvsProdFluxComposant.findSumByComposantStatut", new String[]{"composant", "statut"}, new Object[]{y, Constantes.ETAT_TERMINE});
                y.setQuantiteValide(d != null ? d : 0);
                if (y.getComposantsUsed() != null) {
                    y.getComposantsUsed().clear();
                }
                dao.update(y);
            }
        }
    }

    public void miseAjourFluxComposant(YvsProdComposantOF y) {
        if (y != null ? y.getId() > 0 : false) {
            List<YvsProdFluxComposant> list = dao.loadNameQueries("YvsProdFluxComposant.findByComposant", new String[]{"composant"}, new Object[]{y});
            for (YvsProdFluxComposant c : list) {
                c.setQuantite((y.getQuantiteValide() * c.getTauxComposant()) / 100);
                dao.update(c);
            }
        }
    }

//    private void loadEquipes() {
//        champ = new String[]{"actif"};
//        val = new Object[]{true};
//        nameQueri = "YvsProdEquipeProduction.findByActif";
//        equipes = UtilProd.buildBeanListEquipeProduction(dao.loadNameQueries(nameQueri, champ, val));
//    }
//    private void loadProduits() {
//        champ = new String[]{"categorie", "categorie1", "societe"};
//        val = new Object[]{Constantes.CAT_PF, Constantes.CAT_PSF, currentAgence.getSociete()};
//        nameQueri = "YvsBaseArticles.findRefF";
////        articles = UtilProd.buildBeanListArticleProduction(dao.loadNameQueries(nameQueri, champ, val));
//    }
//    public void loadNomenclature(ValueChangeEvent ev) {
//        ordre.getListComposantOf().clear();
//        ordre.getListOperationsOf().clear();
//        if (ev.getNewValue() != null) {
//            long id = (long) ev.getNewValue();
////            loadDataArticle(id);
//        }
//    }
//    public void changeQuantite() {
//        if (ordre.getNomenclature().getId() > 0 && ordre.getGamme().getId() > 0 && ordre.getQuantite() > 0) {
//            champ = new String[]{"nomenclature"};
//            val = new Object[]{new YvsProdNomenclature(ordre.getNomenclature().getId())};
//            nameQueri = "YvsProdComposantNomenclature.findByNomenclature";
//            List<YvsProdComposantNomenclature> l = dao.loadListTableByNameQueries(nameQueri, champ, val);
//            //garde les composants qui ont une nomenclature afin de générer les ordres de fabrications correspondant au besoin
//            for (YvsProdComposantNomenclature c : l) {
////                if (!c.getArticle().getYvsProdComposantNomenclatureList().isEmpty()) {
////                    listArtIntermediaire.add(c.getArticle());
////                }
//            }
//            if (!l.isEmpty()) {
//                ordre.setListComposantOf(calculBesoins(l, l.get(0).getNomenclature()));
//            }
//        }
//    }
//    private List<YvsBaseArticles> listArtIntermediaire = new ArrayList<>();
    //appelé à la selection d'une nomenclature
    private List<YvsProdComposantOF> calculBesoins(List<YvsProdComposantNomenclature> list, YvsProdNomenclature nom_, OrdreFabrication ordre, boolean findPr) {
        List<YvsProdComposantOF> result = new ArrayList<>();
        for (YvsProdComposantNomenclature com_ : list) {
            if (com_.getActif() && !com_.getAlternatif()) {
                result.add(calculBesoins(com_, nom_, result, ordre, findPr));
            }
        }
        return result;
    }

    private YvsProdComposantOF calculBesoins(YvsProdComposantNomenclature composantNom_, YvsProdNomenclature nom_,
            List<YvsProdComposantOF> result, OrdreFabrication ordre, boolean findPr) {
        YvsProdComposantOF composant_ = null;
        double qte, cout = 0;
        if (composantNom_ != null) {
            //si on est en mode modification on vérifie si le composant courant a déjà été persisté
            if (ordre.getListComposantOf() != null) {
                composant_ = giveIdComposantPersiste(composantNom_, ordre);
            }
            if (composant_ == null) {
                //construit l'objet composantOF à partir de composantNomenclature
                composant_ = UtilProd.buildComposantOf(composantNom_);
            }
            qte = giveQuantite(composantNom_, nom_, result, ordre);
            if (findPr) {
                cout = dao.getPr(composantNom_.getArticle().getId(), ordre.getDepotMp().getId(), 0, ordre.getDateDebutLancement(), composant_.getUnite().getId());
            }
            composant_.setQuantiteValide(0d); //Important !
            composant_.setQuantitePrevu(Constantes.arrondi((qte), 3));
            composant_.setCoutComposant(cout);
            if (findPr) {
                composant_.getArticle().setSuiviEnStock(controleStock(composantNom_.getArticle(), new YvsBaseDepots(ordre.getDepotMp().getId())));
            }
            composant_.setOrdre(composantNom_.getOrdre());
            composant_.setType(composantNom_.getType());
            composant_.setInsideCout(composantNom_.getInsideCout());
        }
        return composant_;
    }

    private double giveQuantite(YvsProdComposantNomenclature com_, YvsProdNomenclature nom_, List<YvsProdComposantOF> list, OrdreFabrication ordre) {
        if (com_.getComposantLie() != null) {
            // récupère le composant 
            YvsProdComposantOF c = findLine(com_.getComposantLie(), list);
            if (c != null) {
                return com_.getQuantite() * c.getQuantitePrevu() / 100;
            } else {
                //Calcule d'abord la quantité de base
                YvsProdComposantOF cl = calculBesoins(com_.getComposantLie(), nom_, list, ordre, false);
                if (cl != null) {
                    return com_.getQuantite() * cl.getQuantitePrevu() / 100;
                }
            }
        } else {
            return (com_.getQuantite() * ordre.getQuantitePrevu() / nom_.getQuantite());
        }
        return 0;
    }

    private YvsProdComposantOF findLine(YvsProdComposantNomenclature com_, List<YvsProdComposantOF> list) {
        for (YvsProdComposantOF c : list) {
            if (c.getArticle().equals(com_.getArticle())) {
                return c;
            }
        }
        return null;
    }

    private YvsProdComposantOF giveIdComposantPersiste(YvsProdComposantNomenclature c, OrdreFabrication ordre) {
        for (YvsProdComposantOF co : ordre.getListComposantOf()) {
            if (co.getArticle().equals(c.getArticle())) {
                return co;
            }
        }
        return null;
    }

    private YvsProdComposantOF giveComposantOF(YvsBaseArticles art, YvsBaseConditionnement unite, OrdreFabrication ordre) {
        if (ordre.getListComposantOf() != null ? !ordre.getListComposantOf().isEmpty() : false) {
            for (YvsProdComposantOF comp : ordre.getListComposantOf()) {
                if (comp.getArticle().equals(art)) {
                    return comp;
                }
            }
        }
        return new YvsProdComposantOF(art, unite);
    }

    private List<PosteCharge> tempPoste;
    int numLinePhase, numLineComposant;

    public void openOnePhaseToUpdate(int numLine_) {
//        cloneObject(phase_, ordre.getListOperationsOf().get(numLine_));
        numLinePhase = numLine_;
    }

    public void chooseUniteComposant() {
        if (composant.getUnite() != null ? composant.getUnite().getId() > 0 : false) {
            int idx = composant.getComposant().getConditionnements().indexOf(new YvsBaseConditionnement(composant.getUnite().getId()));
            if (idx > -1) {
                composant.setUnite(UtilProd.buildBeanConditionnement(composant.getComposant().getConditionnements().get(idx)));
            }
        }
    }

    public void chooseLotComposant() {
        if (composant.getLotSortie() != null ? composant.getLotSortie().getId() > 0 : false) {
            ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
            if (w
                    != null) {
                int idx = w.getLots().indexOf(new YvsComLotReception(composant.getLotSortie().getId()));
                if (idx > -1) {
                    composant.setLotSortie(UtilCom.buildBeanLotReception(w.getLots().get(idx)));
                }
            }
        }
    }

    public void applyPropertyComposant() {
        try {
            if (ordre.canEditable(autoriser("prod_update_of_encours"))) {
                if (autoriser("prod_update_ppte_composant")) {
                    if (composant != null) {
                        selectedComposant = UtilProd.buildEntityComposantOf(composant, currentUser);
                        if (composant.getId() > 0) {
                            for (YvsProdFluxComposant fc : composant.getComposantsUsed()) {
                                if (fc.getId() > 0 && fc.getOperation() != null && fc.getComposant() != null) {
                                    dao.update(fc);
                                }
                            }
                            int idx = ordre.getListComposantOf().indexOf(selectedComposant);
                            if (idx >= 0) {
                                selectedComposant.setQuantitePrevu(majQteNomenclature(composant.getComposantsUsed(), idx));
                                ordre.getListComposantOf().set(idx, selectedComposant);
                            }
                            dao.update(selectedComposant);
                            if (selectedComposant.getDepotConso() != null) {
                                //Enregistre les préférence du dépôt
                                YvsProdDefaultDepotComposants defaul = (YvsProdDefaultDepotComposants) dao.loadOneByNameQueries("YvsProdDefaultDepotComposants.findOne", new String[]{"site", "composant"}, new Object[]{selectedOf.getSiteProduction(), selectedComposant.getArticle()});
                                if (defaul == null) {
                                    defaul = new YvsProdDefaultDepotComposants();
                                    defaul.setAuthor(currentUser);
                                    defaul.setComposant(selectedComposant.getArticle());
                                    defaul.setDepotConso(selectedComposant.getDepotConso());
                                    defaul.setSite(selectedOf.getSiteProduction());
                                    defaul.setId(null);
                                    dao.save1(defaul);
                                } else {
                                    defaul.setDepotConso(selectedComposant.getDepotConso());
                                    defaul.setAuthor(currentUser);
                                    dao.update(defaul);
                                }
                            }

                        } else {
                            if (selectedOf != null ? selectedOf.getId() < 1 : true) {
                                getErrorMessage("Vous devez selectionner un ordre de fabrication");
                                return;
                            }
                            nameQueri = "YvsProdComposantOF.findByOne";
                            champ = new String[]{"ordre", "article", "unite"};
                            val = new Object[]{selectedOf, selectedComposant.getArticle(), selectedComposant.getUnite()};
                            if (composant.getLotSortie() != null ? composant.getId() > 0 : false) {
                                nameQueri = "YvsProdComposantOF.findByOneLot";
                                champ = new String[]{"ordre", "article", "unite", "lot"};
                                val = new Object[]{selectedOf, selectedComposant.getArticle(), selectedComposant.getUnite(), selectedComposant.getLotSortie()};
                            }
                            YvsProdComposantOF y = (YvsProdComposantOF) dao.loadOneByNameQueries(nameQueri, champ, val);
                            if (y != null ? y.getId() > 0 : false) {
                                //modifier la quantité de la nomenclature
                            }
                            selectedComposant.setOrdreFabrication(selectedOf);
                            if (selectedComposant.getComposantsUsed() != null) {
                                selectedComposant.getComposantsUsed().clear();
                            }
                            selectedComposant.setId(null);
                            selectedComposant = (YvsProdComposantOF) dao.save1(selectedComposant);
                            composant.setId(selectedComposant.getId());
                            ordre.getListComposantOf().add(selectedComposant);
                        }
                        update("tablePhase_:table_composantOF");
                        succes();
                    }
                } else {
                    openNotAcces();
                }
            } else {
                getErrorMessage("Vous ne pouvez pas modifier cet ordre..il est déjà terminé");
            }
        } catch (Exception ex) {
            getException("Error (applyPropertyComposant) ", ex);
            getErrorMessage("Action impossible");
        }

    }

    public void loadParamInit() {
        if (currentAgence != null) {
            paramProduction = (YvsProdParametre) dao.loadOneByNameQueries("YvsProdParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramProd", paramProduction);
        }
    }

    public void deleteUseComposant(YvsProdOfSuiviFlux flux) {
        //si aucune consommation n'a été trouvé
        if (ordre.canEditable(autoriser("prod_update_of_encours"))) {
            if (autoriser("prod_update_ppte_composant")) {
                if (flux.getComposant().getListeSuiviFlux().isEmpty()) {
                    dao.delete(flux.getComposant());
                    selectedFlux.getListeSuiviFlux().remove(flux);
                    update("table_suivi_edit_flux");
                } else {
                    getErrorMessage("Impossible de supprimer ce composant! des flux de stocks ont été trouvés!");
                }
            } else {
                openNotAcces();
            }
        } else {
            openNotAcces();
        }
    }

    public void addUseComposantInOperation() {
        //voir si le composant exite déjà pour l'opération                
        if (selectedOp != null && selectedComposant != null && sensConso != null) {
            boolean continu = true;
            for (YvsProdOfSuiviFlux s : selectedFlux.getListeSuiviFlux()) {
                if (s.getComposant().getComposant().equals(selectedComposant)) {
                    continu = false;
                    break;
                }
            }
            if (continu) {
                YvsProdFluxComposant flux = new YvsProdFluxComposant();
                flux.setComposant(selectedComposant);
                flux.setCoeficientValeur(0d);
                flux.setMargeQte(margeNew);
                flux.setOperation(selectedOp);
                flux.setQuantite(selectedComposant.getQuantitePrevu());
                flux.setSens(sensConso.charAt(0));
                flux.setSuivi(false); //détermine si c'est sur ce composant qu'on étudie la progression de l'opération
                flux.setTauxComposant(100d);
                flux.setTypeSuivi(null);//
//                flux.setUnite(selectedComposant.getUnite());
                flux = (YvsProdFluxComposant) dao.save1(flux);
                //--
                YvsProdOfSuiviFlux c = new YvsProdOfSuiviFlux(-flux.getId());
                c.setAuthor(currentUser);
                c.setComposant(flux);
                selectedFlux.getListeSuiviFlux().add(c);
                update("table_suivi_edit_flux");

            } else {
                getErrorMessage("Le composant est déjà enregistré pour cette opération !");
            }
        } else {
            getErrorMessage("Formulaire incorrecte !");
        }
    }

    private double majQteNomenclature(List<YvsProdFluxComposant> fc, int lineC) {
        double re = 0;
        for (YvsProdFluxComposant c : fc) {
            re += c.getQuantite();
        }
        if (lineC >= 0) {
            ordre.getListComposantOf().get(lineC).setQuantitePrevu(re);
            editQteValide(lineC, re, false);
        }
        return re;
    }
    private List<Long> idsCurrentEquipes = new ArrayList<>();

    private void buildIdEquipe(List<YvsComCreneauHoraireUsers> l) {
        if (l != null ? !l.isEmpty() : false) {
            for (YvsComCreneauHoraireUsers c : l) {
                if (c.getEquipe() != null) {
                    idsCurrentEquipes.add(c.getEquipe().getId());
                }
            }
        } else {
            idsCurrentEquipes.add(-1L);
        }
    }

    private void initDisPlayByDroit() {
        Date date = new Date();
        List<YvsComCreneauHoraireUsers> l = dao.loadNameQueries("YvsComCreneauHoraireUsers.findActifByUser", new String[]{"users"}, new Object[]{currentUser.getUsers()}, 0, 10);
        if (!l.isEmpty()) {
            date = l.get(l.size() - 1).getDateTravail();
            buildIdEquipe(l);
        }
        if (!typePage.equals("SUIVI")) {
            dateDebut = date;
        }
        if (!autoriser("production_view_all_date")) {
            // je me rassure que ma date ne va pas plus loin que la limite fixé
            Date limit = yvs.dao.salaire.service.Constantes.givePrevOrNextDate(new Date(), -paramProduction.getLimiteVuOf());
            if (dateDebut.before(limit)) {
                dateDebut = limit;
            }
            addParametreDate(true);
        }
        if (!l.isEmpty() ? l.get(0).getEquipe() != null ? l.get(0).getEquipe().getSite() != null : false : false) {
            ordre.setSite(UtilProd.buildBeanSiteProduction(l.get(0).getEquipe().getSite()));
        }
        if (!autoriser("production_view_all_site")) {
            //trouve les sites où je suis planifier
            sitesSeach = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdSiteByUserPlanif", new String[]{"users", "debut", "fin"}, new Object[]{currentUser.getUsers(), date, new Date()});
            if (sitesSeach == null) {
                sitesSeach = new ArrayList<>();
            }
            sitesSeach.add(0);
            addParametreSite();
        }
        if (!autoriser("production_view_all_user")) {
            //trouve les sites où je suis planifier
//            date = yvs.dao.salaire.service.Constantes.givePrevOrNextDate(new Date(), -paramProduction.getLimiteVuOf());
//            List<Long> ids = dao.loadNameQueries("YvsProdSessionOf.findIdProdByUsersDates", new String[]{"producteur", "dateDebut", "dateFin"}, new Object[]{currentUser.getUsers(), date, new Date()});
//            if (ids == null) {
//                ids = new ArrayList<>();
//            }
//            List<Long> i = dao.loadNameQueries("YvsProdOrdreFabrication.findIdByAuthorDates", new String[]{"author", "dateDebut", "dateFin"}, new Object[]{currentUser, date, new Date()});
//            if (i != null) {
//                ids.addAll(i);
//            }
//            if (ids.isEmpty()) {
//                ids.add(0L);
//            }
//            paginator.addParam(new ParametreRequete("y.id", "idsP", ids, "IN", "AND"));
        }
        if (!autoriser("production_view_all_societe")) {
            paginator.addParam(new ParametreRequete("y.siteProduction.agence", "agence", currentAgence, "=", "AND"));
        }
        if (!autoriser("production_view_all_of")) {
            statutsOf.clear();
            egaliteStatutOf = "IN";
            statutsOf.add(Constantes.ETAT_ENCOURS);
            statutsOf.add(Constantes.ETAT_PROD_LANCE);
            addParametreStatutOF();
        }
    }

    public void loadAllSessionProd(boolean avance, boolean init) {
        if (!autoriser("prod_view_session_all_date")) {
            // je me rassure que ma date ne va pas plus loin que la limite fixé
            Date limit = yvs.dao.salaire.service.Constantes.givePrevOrNextDate(new Date(), -paramProduction.getLimiteVuOf());
            if (dateDebut.before(limit)) {
                dateDebut = limit;
            }
            addParamDateSession(true);
        }
        if (!autoriser("prod_view_all_equipe")) {
            addParamEquipeSession();
        }
        if (!autoriser("prod_view_session_all_user")) {
            addParamProducteurSession();
        }
        p_sessions.addParam(new ParametreRequete("y.equipe.site.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        sessionsProd = p_sessions.executeDynamicQuery("y", "y", "YvsProdSessionProd y JOIN FETCH y.equipe JOIN FETCH y.producteur JOIN FETCH y.depot JOIN FETCH y.tranche", "y.dateSession DESC", avance, init, (int) p_sessions.getRows(), dao);
    }

    public void clearParamSessions() {
        p_sessions.getParams().clear();
        loadAllSessionProd(true, true);
        update("table_sesion_prod");
    }

    public void addParamOrdreAtSession() {
        //trouve les ids des sessions liées à l'of en cours
        if (selectedOf != null) {
            List<Long> ids = dao.loadNameQueries("YvsProdSessionOf.findIdSessByOrdre", new String[]{"ordre"}, new Object[]{selectedOf});
            if (ids.isEmpty()) {
                ids.add(-1L);
            }
            p_sessions.addParam(new ParametreRequete("y.id", "ids", ids, "IN", "AND"));
            loadAllSessionProd(true, true);
        }
    }

    public void choosePaginatorSession(ValueChangeEvent ev) {
        long row;
        try {
            row = (long) ev.getNewValue();
        } catch (Exception ex) {
            row = (int) ev.getNewValue();
        }
        p_sessions.setRows((int) row);
        loadAllSessionProd(true, true);
    }

    public void addParamEquipeSessionProd(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.equipe", "equipe", null, "=", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            long id = (Long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsProdEquipeProduction((Long) ev.getNewValue()));
            }
        }
        p_sessions.addParam(p);
        loadAllSessionProd(true, true);
    }

    public void addParamDepotSessionProd(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.depot", "depot", null, "=", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            long id = (Long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsBaseDepots((Long) ev.getNewValue()));
            }
        }
        p_sessions.addParam(p);
        loadAllSessionProd(true, true);
    }

    public void addParamProducteurSessionProd(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.producteur", "producteur", null, "=", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            long id = (Long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsUsers((Long) ev.getNewValue()));
            }
        }
        p_sessions.addParam(p);
        loadAllSessionProd(true, true);
    }

    public void addParamDateSessionProd(SelectEvent ev) {
        if (ev != null) {
            Date date = (Date) ev.getObject();
            p_sessions.addParam(new ParametreRequete("y.dateSession", "date", date, "=", "AND"));
        } else {
            p_sessions.addParam(new ParametreRequete("y.dateSession", "date", null, "=", "AND"));
        }
        loadAllSessionProd(true, true);
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllOf(true, true);
    }

    public void addParamIdsDeclaration() {
        addParamIds(true);
        ParametreRequete p = new ParametreRequete("y.id", "ids_warning", null, "IN", "AND");
        if (Util.asString(idsDeclaration) ? !idsDeclaration.equals("0") : false) {
            List<Long> ids = new ArrayList<>();
            for (String id : idsDeclaration.split(",")) {
                ids.add(Util.isNumeric(id) ? Long.valueOf(id.trim()) : 0);
            }
            p = new ParametreRequete("y.id", "ids_warning", ids, "IN", "AND");
        }
        p_declaration.addParam(p);
        loadDeclaration(true, true);
    }

    public void loadAll(String page) {
        loadInfosWarning(false);
        if (isWarning != null ? isWarning : false) {
            if (modelWarning.equals("CP_UPPER_PR")) {
                loadByWarningDeclaration();
                openDialog("dlgDeclaration");
            } else {
                loadByWarning();
            }
        } else {
            loadInitProd(page);
        }
        initView();
    }

    private void loadByWarningDeclaration() {
        p_declaration.clear();
        loadInfosWarning(true);
        idsDeclaration = idsSearch;
        idsSearch = "";
        addParamIdsDeclaration();
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds();
    }

    public void loadInitProd(String page) {
        if (page.equals("SUIVI")) {
            if (paramProduction != null) {
                dateDebut = yvs.dao.salaire.service.Constantes.givePrevOrNextDate(new Date(), -paramProduction.getLimiteVuOf());
            } else {
                dateDebut = new Date();
            }
            dateFin = new Date();
            date_ = true;
            addParamDate_only(true);
            searchByStatutOF_only();
            statutDec = Constantes.ETAT_TERMINE;
            egaliteStatutDec = " !=";
            searchByStatutDec_only();
            loadAllOf(true, true);

        } else {
            loadAllOf(true, true);
        }
        initView();
    }

    private void initView() {
        //charge les paramètres généreau de la prod
        paramG = (YvsProdParametre) dao.loadOneByNameQueries("YvsProdParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        if (paramG != null) {
            if (ordre.getId() <= 0) {
                ordre.setSuiviOperations(paramG.getSuiviOpRequis());
            }
            loadNext = paramG.getDeclarationContinue();
        }
        //User ayant un planning actif ou permanent
        if (autoriser("prod_view_session_all_user")) {
            if (autoriser("prod_view_all_site")) {
                producteurs = dao.loadNameQueries("YvsComCreneauHoraireUsers.findUserEquipeActif", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            } else {
                producteurs = dao.loadNameQueries("YvsComCreneauHoraireUsers.findUserEquipeActifAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
        } else {
            if (currentUser != null) {
                producteurs.clear();
                producteurs.add(currentUser.getUsers());
            }
        }
        //charge la dernière session en date du user
        if (currentSession != null ? currentSession.getId() < 1 : true) {
            YvsProdSessionProd ss = (YvsProdSessionProd) dao.loadOneByNameQueries("YvsProdSessionProd.findCurrent", new String[]{"producteur", "date"}, new Object[]{currentUser.getUsers(), yvs.dao.salaire.service.Constantes.giveOnlyDate(new Date())});
            if (ss != null) {
                //copie session prod
                currentSession = ss;
            } else {
                initSessionProdWithParam();
            }
        }
        if (producteurs.contains(currentUser.getUsers())) {
            sessionProd.setProducteur(UtilUsers.buildBeanUsers(currentUser.getUsers()));
        }
        displayAll = autoriser("prod_launch_of");
    }

    public void avance(boolean avancer) {
        initForm = false;
        loadAllOf(avancer, false);
    }

    public void loadAllOf(boolean avancer, boolean init) {
        initDisPlayByDroit();
        initForm = init;
        paginator.addParam(new ParametreRequete("y.siteProduction.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        String query = "YvsProdOrdreFabrication y JOIN FETCH y.siteProduction "
                + "JOIN FETCH y.article "
                + "LEFT JOIN FETCH y.article.groupe "
                + "LEFT JOIN FETCH y.nomenclature "
                + "LEFT JOIN FETCH y.gamme JOIN FETCH y.author.users";
        String orderBy = "y.dateDebut DESC, y.article.groupe.id, y.article.categorie DESC";
        if (!groupProd) {
            orderBy = "y.dateDebut DESC, y.article.categorie DESC";
        }
        listeOrdreF = paginator.executeDynamicQuery("DISTINCT y", " DISTINCT y", query, orderBy, avancer, initForm, (int) imax, dao);
        update("tableOF");
    }

    public void loadDeclaration(boolean avance, boolean init) {
        List<YvsComCreneauHoraireUsers> l = dao.loadNameQueries("YvsComCreneauHoraireUsers.findActifByUser", new String[]{"users"}, new Object[]{currentUser.getUsers()}, 0, 1);
        Date debut = new Date();
        if (!l.isEmpty()) {
            debut = l.get(0).getDateTravail();
        }
        if (!autoriser("production_view_all_date")) {
            Date fin = new Date();
            p_declaration.addParam(new ParametreRequete("y.ordre.dateDebut", "date", debut, fin, "BETWEEN", "AND"));
        }
        if (!autoriser("production_view_all_site")) {
            List<Integer> sites = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdSiteByUserPlanif", new String[]{"users", "debut", "fin"}, new Object[]{currentUser.getUsers(), debut, new Date()});
            if (sites == null) {
                sites = new ArrayList<>();
            }
            sites.add(0);
            p_declaration.addParam(new ParametreRequete("y.ordre.siteProduction.id", "site", sites, "IN", "AND"));
        }
        if (!autoriser("production_view_all_societe")) {
            p_declaration.addParam(new ParametreRequete("y.ordre.siteProduction.agence", "agence", currentAgence, "=", "AND"));
        }
        if (!autoriser("production_view_all_of")) {
            List<Integer> equips = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdEquipeByUserPlanif", new String[]{"users", "debut", "fin"}, new Object[]{currentUser.getUsers(), debut, new Date()});
            if (equips == null) {
                equips = new ArrayList<>();
            }
            equips.add(0);
            p_declaration.addParam(new ParametreRequete("y.sessionOf.sessionProd.equipe.id", "equipes", equips, "IN", "AND"));
        }
        p_declaration.addParam(new ParametreRequete("y.sessionOf.sessionProd.depot.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        declarations = p_declaration.executeDynamicQuery("YvsProdDeclarationProduction", "y.sessionOf.sessionProd.dateSession DESC", avance, init, p_declaration.getRows(), dao);
        update("data-declaration_of");
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        imax = (long) ev.getNewValue();
        initForm = true;
        loadAllOf(true, initForm);
    }

    public void choosePaginatorComposant(ValueChangeEvent ev) {
        p_composant.setRows((int) ((int) ev.getNewValue()));
        loadComposant(true, true);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAllOf(true, initForm);
    }

    public void gotoPagePaginatorComposant() {
        p_composant.gotoPage((int) imax);
        loadComposant(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsProdOrdreFabrication> re = paginator.parcoursDynamicData("YvsProdOrdreFabrication", "y", "y.priorite DESC, y.dateDebut DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void openOfToupdate(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectedOf = (YvsProdOrdreFabrication) ev.getObject();
            onSelectObject(selectedOf);
            if (typePage.equals(Constantes.PROD_TYPE_PAGE_SUIVI)) {
                qteAProduire = selectedOf.getResteADeclarer();
                qteADeclarer = 0;
            }
        }
    }

    public void loadOnViewToUpdate(SelectEvent ev) {
        suiviOperation = new SuiviOperations();
        openOfToupdate(ev);
        for (YvsProdOperationsOF cp : ordre.getListOperationsOf()) {
            if (!cp.getStatutOp().equals(Constantes.ETAT_TERMINE)) {
                //prépare la première opération non encore terminé
                openFluxComposantOperation(cp);
                break;
            }
        }
    }

    @Override
    public void onSelectDistant(YvsProdOrdreFabrication y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Ordres de fabrication", "modProduction", "smenOfSuivi2", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsProdOrdreFabrication y) {
        selectedOf = y;
        if (!listeOrdreF.contains(selectedOf)) {
            listeOrdreF.add(selectedOf);
        }
        cloneObject(ordre, UtilProd.buildBeanOf(y));
        //garde les composants de la nomenclature
        selectedOf.getComposants().clear();
        selectedOf.getOperations().clear();
        //liste des composants rattaché à l'OF
        List<YvsProdFluxComposant> composants = dao.loadNameQueries("YvsProdFluxComposant.findByOrdre", new String[]{"ordre"}, new Object[]{y});
        //construit la liste des composants et des opérations de l'OF
        buildOpAndComposant(composants);
        selectedOf.setDeclarations(dao.loadNameQueries("YvsProdDeclarationProduction.findByOF", new String[]{"ordre"}, new Object[]{y}));
        ordre.setDeclarations(new ArrayList<>(selectedOf.getDeclarations()));
        //s'il y a pas opération et pas composant pour l'OF, on vide la liste des flux
        if (ordre.getListOperationsOf().isEmpty() || ordre.getListComposantOf().isEmpty()) {
            if (selectedFlux != null) {
                selectedFlux.getListeSuiviFlux().clear();
            }
        }
        update("panel-opt-session-prod");
    }

    private void buildOpAndComposant(List<YvsProdFluxComposant> l) {
        for (YvsProdFluxComposant fc : l) {
            if (!selectedOf.getComposants().contains(fc.getComposant())) {
                selectedOf.getComposants().add(fc.getComposant());
            }
            if (!selectedOf.getOperations().contains(fc.getOperation())) {
                selectedOf.getOperations().add(fc.getOperation());
            }
        }
        Collections.sort(selectedOf.getOperations(), new YvsProdOperationsOF());
        Collections.sort(selectedOf.getComposants(), new YvsProdComposantOF());
        ordre.setListComposantOf(new ArrayList<>(selectedOf.getComposants()));
        ordre.setListOperationsOf(new ArrayList<>(selectedOf.getOperations()));
    }

    public void actualiseOperation() {
        for (YvsProdOperationsOF o : ordre.getListOperationsOf()) {
            o.setIndicateur(o.getIndicateurOp());
            //construire les autres données

        }
    }

    public void deleteOrdres() {
        try {
            if (chaineSelectOf != null ? !chaineSelectOf.equals("") : false) {
                List<Long> l = decomposeIdSelection(chaineSelectOf);
                List<YvsProdOrdreFabrication> list = new ArrayList<>();
                YvsProdOrdreFabrication bean;
                for (Long ids : l) {
                    bean = listeOrdreF.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                }

                listeOrdreF.removeAll(list);
                succes();
                resetFiche();
                update("formOF");
                update("tableOF");
                update("panel_state");
                update("tablePhase_");
                update("of_depot");

            } else {
                getErrorMessage("Aucun élément sélectionner !");

            }

        } catch (Exception e) {
            getErrorMessage("Action impossible.");
            getException("Suppression", e);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (selectedOf != null ? selectedOf.getId() > 0 : false) {
                if (!selectedOf.canDelete()) {
                    getErrorMessage("Vous ne pouvez supprimer cet ordre de fabrication", "Son statut n'est pas éditable");
                    return;
                }
                dao.delete(selectedOf);
                listeOrdreF.remove(selectedOf);
                if (ordre.getId() == selectedOf.getId()) {
                    resetFiche();
                    update("formOF");
                    update("panel_state");
                    update("tablePhase_");
                    update("of_depot");
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible.");
            getException("Suppression", ex);
        }
    }

    public void deleteBeanOperation() {
        try {
            if (selectedOp != null ? selectedOp.getId() > 0 : false) {
                if (selectedOp.getStatutOp().equals(Constantes.ETAT_TERMINE)) {
                    getErrorMessage("Cette opération est déjà terminée");
                    return;
                }
                dao.delete(selectedOp);
                ordre.getListOperationsOf().remove(selectedOp);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible.");
            getException("Suppression", ex);
        }
    }

    public void deleteBeanComposant() {
        try {
            if (selectedComposant != null ? selectedComposant.getId() > 0 : false) {
                List<YvsProdFluxComposant> list = dao.loadNameQueries("YvsProdFluxComposant.findByComposant", new String[]{"composant"}, new Object[]{selectedComposant});
                for (YvsProdFluxComposant flx : list) {
                    if (flx.getOperation().getStatutOp().equals(Constantes.ETAT_TERMINE)) {
                        getErrorMessage("Ce composant est encours d'utilisation ou est déjà utilisé");
                        return;
                    }
                }
                dao.delete(selectedComposant);
                ordre.getListComposantOf().remove(selectedComposant);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible.");
            getException("Suppression", ex);
        }
    }

    public void listenChangeQuantite(CellEditEvent ev) {
        if (ev != null) {
            int row = ev.getRowIndex();
            YvsProdOfSuiviFlux line = selectedFlux.getListeSuiviFlux().get(row);
            //Trouve la ligne de nomenclature qui correspond
            YvsProdComposantNomenclature comp = findComposantNom(line.getComposant().getComposant().getArticle());
            if (comp != null) {
                // trouve les ligne qui dépendent de ce composant                
                YvsProdComposantNomenclature c;
                int r = 0;
                for (YvsProdOfSuiviFlux sf : selectedFlux.getListeSuiviFlux()) {
                    r = selectedFlux.getListeSuiviFlux().indexOf(sf);
                    if (!sf.equals(line)) {
                        c = findComposantNom(sf.getComposant().getComposant().getArticle());
                        if (c != null ? c.getComposantLie() != null : false) {
                            if (c.getComposantLie().equals(comp)) {
//                                sf.setQuantite(((Double) ev.getNewValue()) * c.getQuantite() / 100);
                                sf.setQuantite(line.getQuantite() * c.getQuantite() / 100);
//                                sf.setQuantitePerdue(line.getQuantitePerdue()* c.getQuantite() / 100);
                                DataTable s = (DataTable) ev.getSource();
                                update(s.getClientId(FacesContext.getCurrentInstance()).concat(":" + r).concat(":cell_qte1"));
                                update(s.getClientId(FacesContext.getCurrentInstance()).concat(":" + r).concat(":cell_qte2"));
                            }
                        }
                    }
                }
            }
        }
    }

    private double qteAProduire;
    private double qteADeclarer;

    public double getQteAProduire() {
        return qteAProduire;
    }

    public void setQteAProduire(double qteAProduire) {
        this.qteAProduire = qteAProduire;
    }

    public double getQteADeclarer() {
        return qteADeclarer;
    }

    public void setQteADeclarer(double qteADeclarer) {
        this.qteADeclarer = qteADeclarer;
    }

    public void calculBesoinProduit() {
        if (ordre.getId() > 0 && ordre.getNomenclature().getId() > 0 && selectedOp != null) {
            for (YvsProdOfSuiviFlux fc : selectedFlux.getListeSuiviFlux()) {
                if (!fc.getComposant().getSens().equals(Constantes.STOCK_SENS_NEUTRE)) {
                    fc.setQuantite(fc.getComposant().getQuantite() * qteAProduire / ordre.getQuantitePrevu());
                } else {
                    // récupère la quantité généré à l'étape précédente
                    YvsProdOperationsOF prevOp = findNextOpOder(fc.getComposant().getOperation(), false);
                    if (prevOp != null) {
                        //récupère les qté
                        Double qteOpe = (Double) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.findByOp", new String[]{"operation"}, new Object[]{fc.getComposant().getOperation()});
                        qteOpe = qteOpe != null ? qteOpe : 0;
                        fc.setQuantite(qteOpe);
                    }
                }
            }
        }
    }

    public void applyResteQte(boolean save) {
        for (YvsProdOfSuiviFlux s : selectedFlux.getListeSuiviFlux()) {
            s.setQuantite(s.getComposant().getQuantiteResteFlux());
        }
        if (save) {
            saveFluxStock(true);
        }
        update("table_suivi_edit_flux");
    }

    private YvsProdComposantNomenclature findComposantNom(YvsBaseArticles article) {
        for (YvsProdComposantNomenclature c : ordre.getNomenclature().getComposants()) {
            if (c.getArticle().equals(article)) {
                return c;
            }
        }
        return null;
    }

    public void saveFluxStock(boolean createNew) {
        boolean re = false;
        boolean continue_ = false;
        //Contôle la cohérence des inventaires        
        List<YvsProdOfSuiviFlux> l = new ArrayList<>(selectedFlux.getListeSuiviFlux());
        if (currentSession != null) {
            if (!controleInventaireOk) {
                getErrorMessage("Vous ne pouvez créer une fiche de stock à cette date car un ou plusieurs inventaires ont déjà été réalisés après dans ce dépot");
                return;
            }
            if (!controleTransferOk) {
                openDialog("dlgVerifyAppro");
                return;
            }
            //si au moins une ligne de consommation n'a pas une quantité sup. à 0, on n'enregistre aucun suivi opération
            for (YvsProdOfSuiviFlux s : l) {
                if (s.getQuantite() > 0) {
                    long depot = (s.getComposant().getComposant().getDepotConso() != null) ? s.getComposant().getComposant().getDepotConso().getId() : currentSession.getDepot().getId();
                    String result = controleStock(s.getComposant().getComposant().getArticle().getId(), s.getComposant().getComposant().getUnite().getId(), depot, 0, s.getQuantite(), 0, "INSERT", "S", currentSession.getDateSession(), 0);
                    if (result != null) {
                        getErrorMessage("L'article '" + s.getComposant().getComposant().getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou pourrait entrainer un stock négatif au " + result + " dans le dépôt " + currentSession.getDepot().getDesignation());
                        return;
                    }
                    continue_ = true;
                }
            }
            if (continue_) {
                //Enregistrer l'opération 
                YvsProdSuiviOperations operation;
                if ((suiviOperation.getId() > 0 && createNew) || suiviOperation.getId() <= 0) {
                    operation = saveOrGetOperationSession(selectedOp, false);
                } else {
                    operation = copyEntitySuiviOperation();
                }
                if (operation == null) {
                    getErrorMessage("Erreur lors de la lecture des propriétés de l'opération !");
                    return;
                }
                for (YvsProdOfSuiviFlux s : l) {
                    if (s.getQuantite() > 0) {
                        suiviFlux.setQuantite(s.getQuantite());
                        selectedFlux = s.getComposant();
                        re = saveNewSuiviFluxComposant(true, operation, false);
                    }
                    s.setQuantite(0d);
                }
                selectedFlux.setListeSuiviFlux(l);
                majTauxEvolutionOf();
                update("tableOF");
                if (loadNext) {
                    if (findAndSelectNextOperation(selectedOp, !paramProduction.getDeclareWhenFinishOf())) {
                        update("header_detail_edit_flux");
                    }
                }
                //Si l'opération en cours est la dernière, on ouvre l'interface des déclarations
                if (isLastOperation(selectedOp)) {
                    if (!ordre.getStatutDeclaration().equals(Constantes.ETAT_TERMINE)) {
                        resetDeclaration();
                        double quantite = this.qteADeclarer > this.qteAProduire ? this.qteADeclarer : this.qteAProduire;
                        if (quantite > 0) {
                            declaration.setQuantite(quantite);
                            update("form_declaration");
                        }
                        if (paramProduction.getDeclareWhenFinishOf()) {
                            if (declaration.getQuantite() > 0) {
                                saveDeclaration(false);
                            }
                        } else {
                            openDialog("dlgDeclaration");
                        }
                    }
                }

                if (!re) {
                    getWarningMessage("Votre opération n'a pas entièrement réussi ! veuillez contacter votre administrateur...");
                }
                succes();
            } else {
                getErrorMessage("Aucun flux de matière n'a été trouvé");
            }
        } else {
            getErrorMessage("Votre session n'est pas initialisé !");
        }
    }

    private String builNumeroSuiviOp(YvsProdOperationsOF op) {
        if (op != null) {
            String ref = (String) dao.loadObjectByNameQueries("YvsProdSuiviOperations.findLastRefOp", new String[]{"operation"}, new Object[]{op});
            if (ref != null) {
                String num = ref.substring(ref.length() - 2);
                try {
                    int n = Integer.valueOf(num);
                    if (n < 9) {
                        return op.getNumero() + "_0" + (n + 1);
                    } else {
                        return op.getNumero() + "_" + (n + 1);
                    }
                } catch (NumberFormatException ex) {
                    getErrorMessage("Erreur lors de la génération du numéro !");
                }
            } else {
                return op.getNumero() + "_01";
            }
        }
        return null;
    }

    private YvsProdSessionOf findSessionOf() {
        YvsProdSessionOf sess = (YvsProdSessionOf) dao.loadOneByNameQueries("YvsProdSessionOf.findOne", new String[]{"ordre", "session"}, new Object[]{selectedOf, currentSession});
        if (sess == null) {
            sess = new YvsProdSessionOf();
            sess.setAuthor(currentUser);
            sess.setDateSave(new Date());
            sess.setDateUpdate(new Date());
            sess.setId(null);
            sess.setOrdre(selectedOf);
            sess.setSessionProd(currentSession);
            sess = (YvsProdSessionOf) dao.save1(sess);
        }
        return sess;
    }

    private YvsProdSuiviOperations saveOrGetOperationSession(YvsProdOperationsOF operation, boolean saveHere) {
        if (ordre.getId() > 0 && selectedOf != null) {
            YvsProdSessionOf sess = findSessionOf();
            currentSession.setOrdreF(sess);
            //Save suivi opération
            YvsProdSuiviOperations sOp = new YvsProdSuiviOperations();
            sOp.setReferenceOp(builNumeroSuiviOp(operation));
            sOp.setStatut(Constantes.ETAT_ENCOURS);
            sOp.setAuthor(currentUser);
            sOp.setCout(0d);
            sOp.setDateDebut(currentSession.getDateSession());
            sOp.setDateFin(currentSession.getDateSession());
            sOp.setDateSave(new Date());
            sOp.setDateUpadate(new Date());
            sOp.setHeureDebut(suiviOperation.getHeureDebut());
            sOp.setHeureFin(suiviOperation.getHeureFin());
            sOp.setOperationOf(operation);
            sOp.setSessionOf(sess);
            if (saveHere) {
                sOp = (YvsProdSuiviOperations) dao.save1(sOp);
            } else {
                sOp.setId(-1L);
            }
            copyBeanSuiviOperation(sOp);
            return sOp;
        } else {
            getErrorMessage("Aucun ordre de fabrication selectionné !");
        }
        return null;
    }

    public boolean saveNewSuiviFluxComposant(boolean all) {
        if (suiviOperation.getId() > 0) {
            return saveNewSuiviFluxComposant(all, copyEntitySuiviOperation(), true);
        } else {
            getErrorMessage("Aucune opération en cour...");
        }
        return false;
    }

    private boolean verifieQuantite() {
        if (!selectedFlux.getComposant().getFreeUse()) {
            // la quantité qu'on consomme doit être inférieur à la quantité prévu augmenté de la marge
            double quantite = selectedFlux.getQuantite() + (selectedFlux.getQuantite() * selectedFlux.getMargeQte() / 100);
            if (Constantes.arrondi(suiviFlux.getQuantite(), paramG.getConverter()) > Constantes.arrondi(quantite, paramG.getConverter())) {
                getErrorMessage("la consommation de l'article '" + selectedFlux.getComposant().getArticle().getDesignation() + " est supérieur à la quantité prévue");
                return false;
            }
            // la quantité qu'on consomme doit être supérieur à la quantité prévu diminué de la marge
            quantite = selectedFlux.getQuantite() - (selectedFlux.getQuantite() * selectedFlux.getMargeQte() / 100);
            if (Constantes.arrondi(suiviFlux.getQuantite(), paramG.getConverter()) < Constantes.arrondi(quantite, paramG.getConverter())) {
                getErrorMessage("la consommation de l'article '" + selectedFlux.getComposant().getArticle().getDesignation() + " est inférieur à la quantité prévue");
                return false;
            }
        }
        return true;
    }

    public boolean saveNewSuiviFluxComposant(boolean all, YvsProdSuiviOperations operation) {
        return saveNewSuiviFluxComposant(all, operation, true);
    }

    public boolean saveNewSuiviFluxComposant(boolean all, YvsProdSuiviOperations operation, boolean controlStock) {
        if (currentSession != null ? Util.asLong(currentSession.getId()) : false) {
            if (selectedOp != null ? selectedOp.getStatutOp().equals(Constantes.ETAT_ENCOURS) : false) {
                if (operation != null) {
                    if (operation.getId() <= 0) {
                        operation.setId(null);
                        operation = (YvsProdSuiviOperations) dao.save1(operation);
                        suiviOperation.setId(operation.getId());
                    }
                    if (suiviFlux.getQuantite() > 0 && selectedFlux != null) {
                        if (!all) { // Vérification si l'on enregistre séparément les composants
                            if (!controleInventaireOk) {
                                getErrorMessage("Vous ne pouvez créer une fiche de stock à cette date car un ou plusieurs inventaires ont déjà été réalisés après dans ce dépot");
                                return false;
                            }
                            if (!controleTransferOk) {
                                openDialog("dlgVerifyAppro");
                                return false;
                            }
                        }
                        if (controlStock && selectedFlux.getId() > 0 ? selectedFlux.getSens().equals(Constantes.STOCK_SENS_SORTIE) : true) {
                            long depot = (selectedFlux.getComposant().getDepotConso() != null) ? selectedFlux.getComposant().getDepotConso().getId() : currentSession.getDepot().getId();
                            String result = controleStock(selectedFlux.getComposant().getArticle().getId(), selectedFlux.getComposant().getUnite().getId(), depot, 0, suiviFlux.getQuantite(), 0, "INSERT", "S", currentSession.getDateSession(), 0);
                            if (result != null) {
                                getErrorMessage("L'article '" + selectedFlux.getComposant().getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou pourrait entrainer un stock négatif au " + result + " dans le dépôt " + currentSession.getDepot().getDesignation());
                                return false;
                            }
                        }
                        if (verifieQuantite()) {
                            YvsProdOfSuiviFlux bean = (YvsProdOfSuiviFlux) dao.loadOneByNameQueries("YvsProdOfSuiviFlux.findOne", new String[]{"operation", "composant"}, new Object[]{operation, selectedFlux});
                            if (bean == null) {
                                bean = new YvsProdOfSuiviFlux(-1l);
                            }
                            bean.setAuthor(currentUser);
                            bean.setComposant(selectedFlux);
                            bean.setDateSave(suiviFlux.getDateSave());
                            bean.setDateUpdate(new Date());
                            bean.setCalculPr(true);
                            bean.setQuantite(arrondi(suiviFlux.getQuantite(), paramG.getConverter()));
                            bean.setQuantitePerdue(selectedFlux.getQuantitePerdue());
                            bean.setCout(findCoutComposant(selectedOp, bean));
                            bean.setIdOperation(operation);
                            // double quantite = suiviFlux.getQuantite();
                            if (bean.getId() != null ? bean.getId() <= 0 : true) {
                                bean.setId(null);
                                bean = (YvsProdOfSuiviFlux) dao.save1(bean);
                                selectedFlux.getListeSuiviFlux().add(0, bean);
                                suiviOperation.setCoutOperation(suiviOperation.getCoutOperation() + bean.getCout());
                            } else {
                                dao.update(bean);
                                int idx = selectedFlux.getListeSuiviFlux().indexOf(bean);
                                if (idx >= 0) {
                                    selectedFlux.getListeSuiviFlux().set(idx, bean);
                                }
                            }
                            // Met à jour le taux d'évolution de l'ordre de fabrication 
                            if (!all) {
                                majTauxEvolutionOf();
                                //met à jour le prix de l'opération
                                suiviOperation.setCoutOperation(suiviOperation.getCoutOperation() + bean.getCout());
                            }
                            update("table_op_use_mp");
                            update("tableOF");
                        }
                    } else {
                        getErrorMessage("Formulaire incorrecte !");
                        return false;
                    }
                } else {
                    getErrorMessage("Impossible de continuer, Les proprités de l'opération n'ont pas été trouvés !");
                    return false;
                }
            } else {
                getErrorMessage("Vous devez lancer l'opération au préalable");
                return false;
            }
        } else {
            getErrorMessage("Veuillez initialiser votre session de travail");
            return false;
        }
        return true;
    }

    private double findCoutComposant(YvsProdOperationsOF operation, YvsProdOfSuiviFlux composant) {
        if (operation != null && composant != null) {
            if (composant.getComposant().getSens().equals(Constantes.STOCK_SENS_SORTIE)) {
                return dao.getPr(composant.getComposant().getComposant().getArticle().getId(), currentSession.getDepot().getId(), 0, currentSession.getDateSession(), composant.getComposant().getComposant().getUnite().getId());
            } else {
                //Récupère la conso de tous les intrant (MP) dejà consommée au cour de l'opération et calcule leur coût

            }

        }
        return 0;
    }

    private double applyCoutComposant(YvsProdOperationsOF operation) {
        if (operation != null) {
            List<YvsProdOfSuiviFlux> intrants = dao.loadNameQueries("YvsProdOfSuiviFlux.findFluxByOpAndSens", new String[]{"operation", "sens"}, new Object[]{operation, Constantes.STOCK_SENS_SORTIE});
            List<YvsProdOfSuiviFlux> sortants = dao.loadNameQueries("YvsProdOfSuiviFlux.findFluxByOpAndSens", new String[]{"operation", "sens"}, new Object[]{operation, Constantes.STOCK_SENS_ENTREE});
            Double soeValeur = 0d;
            Double pr;
            long depot = 0;
            for (YvsProdOfSuiviFlux f : intrants) {
                if (f.getComposant().getComposant().getDepotConso() != null ? f.getComposant().getComposant().getDepotConso().getId() > 0 : false) {
                    depot = f.getComposant().getComposant().getDepotConso().getId();
                } else {
                    depot = f.getIdOperation().getSessionOf().getSessionProd().getDepot().getId();
                }
                pr = dao.getPr(f.getComposant().getComposant().getArticle().getId(), depot, 0, f.getIdOperation().getSessionOf().getSessionProd().getDateSession(), f.getComposant().getComposant().getUnite().getId());
                f.setCout(pr);
                soeValeur += (f.getQuantite() * pr);
                dao.update(f);
            }
            for (YvsProdOfSuiviFlux f : sortants) {
                //cout matière + cout de l'opération
                if (operation.getTypeCout().equals(Constantes.PROD_TYPE_COUT_TAUX)) {
                    f.setCout((f.getComposant().getCoeficientValeur() * soeValeur / 100) / f.getQuantite());
                    dao.update(f);
                } else if (operation.getTypeCout().equals(Constantes.PROD_TYPE_COUT_VALEUR)) {
                    f.setCout(f.getComposant().getCoeficientValeur() * f.getQuantite());
                    dao.update(f);
                } else {
                    // la valeur est proportionnelle
//                    List<Object[]> produits = dao.loadNameQueries("YvsProdOfSuiviFlux.findGroupFluxByOpAndSens", new String[]{"operation", "sens"}, new Object[]{operation, Constantes.STOCK_SENS_ENTREE});
//                    for (Object[] line : produits) {
////                        YvsProdComposantOF c = (YvsProdComposantOF) line[1];
//                    }
                }
            }

        }
        return 0;
    }

    public double getCoutComposant(YvsProdComposantOF composant) {
        Double re = (Double) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.findCoutOneComposant", new String[]{"composant"}, new Object[]{composant});
        return (re != null) ? re : 0;
    }

    private boolean controleQteFlux(YvsProdOperationsOF op) {
        boolean result = true;
        Double total, marge, perte;
        for (YvsProdFluxComposant fxc : op.getComposants()) {
            total = (Double) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.findFluxComposant", new String[]{"composant"}, new Object[]{fxc});
            total = (total != null) ? total : 0;
            //marge
            marge = fxc.getQuantite() * fxc.getMargeQte() / 100;
            perte = fxc.getQuantite() * fxc.getQuantitePerdue() / 100;
            if (Constantes.arrondi((total + marge - perte), 3) < Constantes.arrondi(fxc.getQuantite(), 3)) {
                getWarningMessage("Les flux du composant " + fxc.getComposant().getArticle().getDesignation() + " sont inférieure à la quantité prévue !");
                result = false;
                break;
            }
            if (Constantes.arrondi(total, 3) > Constantes.arrondi((fxc.getQuantite() + marge), 3)) {
                getWarningMessage("Les flux du composant " + fxc.getComposant().getArticle().getDesignation() + " sont supérieure à la quantité prévue !");
                result = false;
                break;
            }
        }
        return result;
    }

    private void majTauxEvolutionOf() {
        double taux = 0;
        ordre.setListOperationsOf(dao.loadNameQueries("YvsProdOperationsOF.findByOF", new String[]{"ordre"}, new Object[]{selectedOf}));
        actualiseOperation();
        for (YvsProdOperationsOF o : ordre.getListOperationsOf()) {
            if (o.getIndicateur() != null) {
                taux += 100 * (o.getIndicateur().getComposant().getQuantiteFlux() / o.getIndicateur().getComposant().getQuantite());
            }
        }
        if (taux > 0) {
            selectedOf.setTauxEvolution(taux / ordre.getListOperationsOf().size());
            dao.update(selectedOf);
        }
    }

    public void deleteSuiviFlux(YvsProdOfSuiviFlux y) {
        if (y != null) {
            dao.delete(y);
            selectedFlux.getListeSuiviFlux().remove(y);
            update("table_op_use_mp");
        }
    }

    public double findQteReelComposantOf(YvsProdComposantOF c, boolean entree) {
        Double d = 0d;
        if (c != null) {
            if (entree) {
                d = (Double) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.findFluxComposantBySens", new String[]{"composant", "sens"}, new Object[]{c, Constantes.STOCK_SENS_ENTREE});
            } else {
                d = (Double) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.findFluxComposantBySens", new String[]{"composant", "sens"}, new Object[]{c, Constantes.STOCK_SENS_SORTIE});
            }
        }
        return d != null ? d : 0d;
    }

    public void chooseEquipe() {
        if (suiviFlux != null ? suiviFlux.getEquipe() != null : false) {
            ManagedEquipeProduction w = (ManagedEquipeProduction) giveManagedBean(ManagedEquipeProduction.class);
            if (w != null) {
                int idx = w.getEquipes().indexOf(new YvsProdEquipeProduction(suiviFlux.getEquipe().getId()));
                if (idx > -1) {
                    YvsProdEquipeProduction y = w.getEquipes().get(idx);
                    suiviFlux.setEquipe(UtilProd.buildBeanEquipeProduction(y));
                }
            }
        }
    }

    public void chooseTranche() {
        if (suiviFlux != null ? suiviFlux.getTranche() != null : false) {
            ManagedTypeCreneau w = (ManagedTypeCreneau) giveManagedBean(ManagedTypeCreneau.class);
            if (w != null) {
                int idx = w.getTranches().indexOf(new YvsGrhTrancheHoraire(suiviFlux.getTranche().getId()));
                if (idx > -1) {
                    YvsGrhTrancheHoraire y = w.getTranches().get(idx);
                    suiviFlux.setTranche(UtilGrh.buildTrancheHoraire(y));
                }
            }
        }
    }

    public void resetDeclaration() {
        declaration = new DeclarationProduction();
        if (selectedOf != null ? selectedOf.getNomenclature() != null : false) {
            declaration.setConditionnement(UtilProd.buildBeanConditionnement(selectedOf.getNomenclature().getUniteMesure()));
            double quantite = selectedOf.getQuantite();
            for (YvsProdDeclarationProduction d : selectedOf.getDeclarations()) {
                quantite -= d.getQuantite();
            }
            if (currentSession != null ? currentSession.getOrdreF() != null ? currentSession.getOrdreF().getOrdre().equals(selectedOf) : false : false) {
                declaration.setSessionOf(UtilProd.copyBeanSessionProdOf(currentSession.getOrdreF()));
            }
            declaration.setQuantite(quantite > 0 ? quantite : 0);
            //cherche la margeNew
            double marge = 0;
            int idx = ordre.getArticles().getNomenclatures().indexOf(selectedOf.getNomenclature());
            if (idx >= 0) {
                YvsProdNomenclature nom = ordre.getArticles().getNomenclatures().get(idx);
                marge = nom.getMargeQte();
            }
            marge = (ordre.getQuantitePrevu() * (marge / 100));
            declaration.setTerminer(quantite <= marge);
        }
        selectDeclaration = new YvsProdDeclarationProduction();
        update("form_declaration");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectLineDeclaration((YvsProdDeclarationProduction) ev.getObject());
        }
    }

    public void loadOnViewDeclaration(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectLineDeclaration((YvsProdDeclarationProduction) ev.getObject());
            onSelectObject(((YvsProdDeclarationProduction) ev.getObject()).getOrdre());
        }
    }

    public void selectLineDeclaration(YvsProdDeclarationProduction line) {
        if (line != null) {
            selectDeclaration = line;
            declaration = UtilProd.buildBeanDeclarationProduction(selectDeclaration);
            declaration.setSessionOf(UtilProd.copyBeanSessionProdOf(selectDeclaration.getSessionOf()));
            update("blog_declaration");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetDeclaration();
    }

    public void loadDeclaration() {
        if (currentSession == null ? true : currentSession.getOrdreF() == null) {
            getWarningMessage("Aucune informations de session n'a été trouvé !");
        }
        resetDeclaration();
    }

    public void loadDeclaration(YvsProdOrdreFabrication y) {
        selectedOf = y;
        loadDeclaration();
    }

    private boolean controleQteDeclare(DeclarationProduction bean, boolean cloture) {
        // quantité déjà déclaré
        double totalDeclare = bean.getQuantite();
        double marge = 0;
        for (YvsProdDeclarationProduction d : ordre.getDeclarations()) {
            if (d.getId() != bean.getId()) {
                totalDeclare += d.getQuantite();
            }
        }
        //récupère la margeNew
        int idx = ordre.getArticles().getNomenclatures().indexOf(new YvsProdNomenclature(ordre.getNomenclature().getId()));
        if (idx >= 0) {
            YvsProdNomenclature nom = ordre.getArticles().getNomenclatures().get(idx);
            marge = nom.getMargeQte();
        }
        marge = (ordre.getQuantitePrevu() * (marge / 100));
        if (totalDeclare > (ordre.getQuantitePrevu() + marge)) {
            getErrorMessage("La quantité déclaré est supérieure à la marge prévue !");
            return false;
        }
        //à la clôture, on vérifie si on a tout déclaré
        if (cloture) {
            if (totalDeclare + marge < (ordre.getQuantitePrevu())) {
                getErrorMessage("La quantité déclaré est inférieure à la marge prévue !");
                return false;
            }
        }
        return true;
    }

    public void openDlgDeclaration() {
        cloneObject(declaration.getConditionnement(), ordre.getNomenclature().getUnite());
    }

    public void saveDeclaration(boolean continu) {
        try {
            if (selectedOf != null ? selectedOf.getId() > 0 : false) {
                if (currentSession == null) {
                    getErrorMessage("Aucune session n'a été touvé !");
                    return;
                }
                if (selectedOf.getStatutOrdre().equals(Constantes.ETAT_ATTENTE) || selectedOf.getStatutOrdre().equals(Constantes.ETAT_SUSPENDU)) {
                    getErrorMessage("Vous ne pouvez effectuer de déclaration pour un ordre " + (selectedOf.getStatutOrdre().equals(Constantes.ETAT_ATTENTE) ? "en attente de lancement" : "suspendu"));
                    return;
                }
                if (declaration.getQuantite() <= 0) {
                    getErrorMessage("Impossible de continuer la déclaration", "Vous devez entrer la quantité !");
                    return;
                }
                YvsProdSessionOf sess;
                if (declaration.getSessionOf() == null ? true : declaration.getSessionOf().getId() <= 0) {
                    sess = findSessionOf();
                    if (sess != null ? sess.getId() < 1 : true) {
                        getErrorMessage("Impossible de continuer la déclaration", "Aucune session de travail n'a été trouvé !");
                        return;
                    }
                    currentSession.setOrdreF(sess);
                } else {
                    sess = UtilProd.copyBeanSessionProdOf(declaration.getSessionOf());
                }
                //vérifie si l'article existe dans le dépôt
                Long nb = (Long) dao.loadObjectByNameQueries("YvsBaseArticleDepot.countByArticleDepot", new String[]{"article", "depot"}, new Object[]{selectedOf.getArticle(), currentSession.getDepot()});
                if (nb != null ? nb <= 0 : true) {
                    getErrorMessage("Impossible de continuer la déclaration", "Cet article n'est pas référencé dans le dépôt");
                    return;
                }
                Double totalDeclare = (Double) dao.loadObjectByNameQueries("YvsProdDeclarationProduction.findSumDeclare", new String[]{"ordre", "statut"}, new Object[]{selectedOf, Constantes.STATUT_DOC_VALIDE});
                totalDeclare = totalDeclare != null ? totalDeclare : 0;
                //contrôle la conformité des matières consommées si l'option de cohérence est activé
                if (!canDeclare(totalDeclare)) {
                    return;
                }
                int idx = 0;
                if (declaration.getConditionnement() != null ? declaration.getConditionnement().getId() < 1 : true) {
                    cloneObject(declaration.getConditionnement(), ordre.getNomenclature().getUnite());
                }
                if (declaration.getConditionnement() != null ? declaration.getConditionnement().getId() < 1 : true) {
                    getErrorMessage("Impossible de continuer la déclaration", "Aucun conditionnement n'a été trouvé !");
                    return;
                }
                declaration.setOrdre(new OrdreFabrication(selectedOf.getId(), selectedOf.getQuantite() - ordre.getQuantiteDeclare()));
                if (!continu) {
                    // on se rassure de ne pas déclarer plus que la marge donnée
                    if (!controleQteDeclare(declaration, selectedOf.getStatutOrdre().equals(Constantes.ETAT_CLOTURE))) {
                        if (autoriser("prod_force_declaration")) {
                            openDialog("dlgConfirmDeclare");
                        }
                        return;
                    }
                }
                declaration.setStatut(autoriser("prod_valid_declaration_prod") ? Constantes.STATUT_DOC_VALIDE : Constantes.STATUT_DOC_ATTENTE);
                if (declaration.getStatut() == Constantes.STATUT_DOC_VALIDE) {
                    if (!controleInventaire(sess.getSessionProd().getDepot().getId(), sess.getSessionProd().getDateSession(), sess.getSessionProd().getTranche().getId())) {
                        return;
                    }
                }
                double oldQte = (selectDeclaration != null && declaration.getId() > 0) ? selectDeclaration.getQuantite() : 0;
                selectDeclaration = UtilProd.buildDeclarationProduction(declaration, currentUser, findSessionOf());
                selectDeclaration.setSessionOf(sess);
                //evalue le cout de production (Récupère toutes les déclarations lié à cet OF)
                List<YvsProdDeclarationProduction> listeDeclarations = dao.loadNameQueries("YvsProdDeclarationProduction.findByOFStatut", new String[]{"ordre", "statut"}, new Object[]{selectedOf, Constantes.STATUT_DOC_VALIDE});
                if (declaration.getId() < 1) {
                    selectDeclaration.setId(null);
                    selectDeclaration = (YvsProdDeclarationProduction) dao.save1(selectDeclaration);
                    declaration.setId(selectDeclaration.getId());
                    listeDeclarations.add(selectDeclaration);
                    totalDeclare = totalDeclare + declaration.getQuantite();
                } else {
                    idx = listeDeclarations.indexOf(selectDeclaration);
                    dao.update(selectDeclaration);
                    totalDeclare = totalDeclare + declaration.getQuantite() - oldQte;
                    if (idx >= 0) {
                        listeDeclarations.set(idx, selectDeclaration);
                    }
                }
                if (totalDeclare > 0) {
                    //recalcul le coût de l'OF
                    if (declaration.isRecalculCoutOf()) {
                        selectedOf.setCoutOf(valoriseOf(selectedOf));
                        ordre.setCoutDeProduction(selectedOf.getCoutOf());
                        dao.update(selectedOf);
                    }
                    for (YvsProdDeclarationProduction d : listeDeclarations) {
                        d.setCoutProduction(selectedOf.getCoutOf() / totalDeclare);
                        dao.update(d);
                        if (d.getId() == declaration.getId()) {
                            declaration.setCout(d.getCoutProduction());
                        }
                    }
                }
                idx = ordre.getDeclarations().indexOf(selectDeclaration);
                if (idx < 0) {
                    ordre.getDeclarations().add(0, selectDeclaration);
                } else {
                    ordre.getDeclarations().set(idx, selectDeclaration);
                }
                idx = selectedOf.getDeclarations().indexOf(selectDeclaration);
                if (idx < 0) {
                    selectedOf.getDeclarations().add(0, selectDeclaration);
                } else {
                    selectedOf.getDeclarations().set(idx, selectDeclaration);
                }
                afterSaveDeclaration();
                loadDeclaration(selectedOf);
                if (!ordre.getDeclarations().isEmpty()) {
                    update("tablePhase_"); //pour afficher l'onglet caché
                }
                update("txt_article_gammes_of");
                succes();
            } else {
                getErrorMessage("Vous devez préciser l'ordre de fabrication");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("Error (saveDeclaration)", ex);
        }
    }

    private Double getConsoReel(YvsProdComposantOF co) {
        Double value = (Double) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.findFluxComposantBySens", new String[]{"composant", "sens"}, new Object[]{co, Constantes.STOCK_SENS_SORTIE});
        return (value != null) ? arrondi(value, paramG.getConverter()) : 0;
    }

    private Double getConsoMaximale(YvsProdComposantOF co, double quantitePrevu) {
        Double value = getSommeMargeSup(co);
        double margeSup = quantitePrevu * value / 100;
        return arrondi(quantitePrevu + margeSup, paramG.getConverter());
    }

    private Double getConsoMinimale(YvsProdComposantOF co, double quantitePrevu) {
        Double value = getSommeMargeInf(co);
        double margeMin = quantitePrevu * value / 100;
        return arrondi(quantitePrevu - margeMin, paramG.getConverter());
    }

    private boolean canDeclare(double totalDeclare) {
        if (paramProduction != null ? paramProduction.getDeclarationProportionnel() : false) {
            double oldQte = (declaration.getId() > 0 && selectDeclaration != null) ? selectDeclaration.getQuantite() : 0;
            totalDeclare = totalDeclare - oldQte + declaration.getQuantite();
            Double consoReel;
            Double consoPrevu;
            double consoMinimale;
            double consoMaximale;
            for (YvsProdComposantOF co : ordre.getListComposantOf()) {
                consoReel = getConsoReel(co);
                consoPrevu = totalDeclare * co.getQuantitePrevu() / ordre.getQuantite();
                consoMinimale = getConsoMinimale(co, consoPrevu);
                consoMaximale = getConsoMaximale(co, consoPrevu);
                if (consoReel > consoMaximale || consoReel < consoMinimale) {
                    getErrorMessage("Impossible de continuer cette déclaration", "La consommation de " + co.getArticle().getDesignation() + " n'est pas cohérente avec les quantité planifiées");
                    return false;
                }
            }
        }
        return true;
    }

    private double getSommeMargeInf(YvsProdComposantOF co) {
        Double re = (Double) dao.loadOneByNameQueries("YvsProdFluxComposant.findSomMargeByComposant", new String[]{"composant", "sens"}, new Object[]{co, Constantes.STOCK_SENS_SORTIE});
        return re != null ? re : 0;
    }

    private double getSommeMargeSup(YvsProdComposantOF co) {
        Double re = (Double) dao.loadOneByNameQueries("YvsProdFluxComposant.findSomMargeByComposantSup", new String[]{"composant", "sens"}, new Object[]{co, Constantes.STOCK_SENS_SORTIE});
        return re != null ? re : 0;
    }

    public void valoriseOperationOF(YvsProdOperationsOF op) {
        Double re = 0d;
        for (YvsProdSuiviOperations o : op.getSuiviOperations()) {
            re += valoriseOperation(o);
        }
        getInfoMessage("Cout de l'opération: " + re);
    }

    public double valoriseOperation(YvsProdSuiviOperations op) {
        Double pr = 0d;
        Double coutOp = 0d;
        Double re = 0d;
        Double qteSP = 0d;
        YvsBaseUniteMesure unite = null;
        List<Options> tempList = new ArrayList<>(); // permet de garder les Mp et leur pr
        if (op != null) {
            //valaorise la matière
            List<YvsProdOfSuiviFlux> list = dao.loadNameQueries("YvsProdOfSuiviFlux.findByOneOp", new String[]{"operation"}, new Object[]{op});
            for (YvsProdOfSuiviFlux fc : list) {
                if (fc.getComposant().getSens().equals(Constantes.STOCK_SENS_SORTIE) && fc.getComposant().getComposant().getInsideCout()) {
                    pr = findLastPr(fc.getComposant().getComposant().getArticle().getId(),
                            op.getSessionOf().getSessionProd().getDepot().getId(), op.getSessionOf().getSessionProd().getDateSession(), fc.getComposant().getComposant().getUnite().getId());
                    re = (pr * (fc.getQuantite() + fc.getQuantitePerdue()));
                    fc.setCout(pr);
                    dao.update(fc);
                    coutOp += re;
                    if (tempList.contains(new Options(null, fc.getComposant().getComposant().getId().intValue()))) {
                        tempList.add(new Options(pr, fc.getComposant().getComposant().getId().intValue()));
                    }
                } else if (fc.getComposant().getSens().equals(Constantes.STOCK_SENS_ENTREE)) {
                    if (unite == null) {
                        unite = fc.getComposant().getComposant().getUnite().getUnite();
                    }
                    if (!fc.getComposant().getComposant().getUnite().getUnite().equals(unite)) {
                        // trouve le lien de conversion entre ces unité
                        double q = convertirUnites(fc.getComposant().getComposant().getUnite().getUnite(), unite, fc.getQuantite());
                        qteSP += (q > 0) ? q : fc.getQuantite(); // s'il exite un lien de conversion entre les deux unité
                    } else {
                        qteSP += fc.getQuantite();
                    }
                }
            }
            System.err.println(" ---- cout de l'opération " + coutOp);
            //evaluation du cout des sous produits (on va évaluer ici le coût en référence à la norme de fabrication)
            Double qteNorme = 0d, qtePrevu = 0d;
            Double coutMp = 0d;
            int idx;

            for (YvsProdOfSuiviFlux fc : list) {
                if (fc.getComposant().getSens().equals(Constantes.STOCK_SENS_ENTREE)) {
                    System.err.println(".... Article :" + fc.getComposant().getComposant().getArticle().getDesignation());
                    //évaluons d'après la nomenclature les qtes de Mp qui aurait produit cette qte de sous produit
                    idx = ordre.getListComposantOf().indexOf(fc.getComposant().getComposant());
                    if (idx >= 0) {
                        qtePrevu = ordre.getListComposantOf().get(idx).getQuantitePrevu();
                        if (qtePrevu != 0) {
                            qteNorme = (ordre.getNomenclature().getQuantite() * fc.getQuantite()) / qtePrevu;
                        }
                    }
                    if (qteNorme > 0) {
                        // liste des Mp de cette opération
                        coutMp = evaluateCoutMP(fc.getIdOperation().getSessionOf().getSessionProd().getDepot().getId(), fc.getIdOperation().getSessionOf().getSessionProd().getDateSession(), tempList, fc.getIdOperation().getOperationOf(), ordre.getNomenclature().getQuantite(), qteNorme);
                        System.err.println(" ---- cout de Matière P " + coutMp);
                        if (coutMp > 0) {
                            if (fc.getComposant().getTypeCout().equals(Constantes.PROD_OP_TYPE_COUT_PROPORTIONNEL)) {
                                if (qteSP != 0) {
                                    fc.setCout((fc.getQuantite() * coutMp / (qteSP)) / (fc.getQuantite() > 0 ? fc.getQuantite() : 1));
                                } else {
                                    fc.setCout(coutMp / fc.getQuantite());
                                }
                            } else {
                                fc.setCout(coutMp / fc.getQuantite());
                            }
                        }
                    }
                    dao.update(fc);
                }
            }
            //Valorise l'opération= Cout de l'op / Temps passé à cette opération
            op.setCout(coutOp);
            dao.update(op);
        }
        return coutOp;
    }

    private double evaluateCoutMP(long depot, Date date, List<Options> tempsList, YvsProdOperationsOF op, double QteNorme, double qteNorme) {
        int idx;
        double qte = 0d, qtePrevu = 0d;
        double valeur = 0d, pr;
        for (YvsProdFluxComposant fc : op.getComposants()) {
            if (fc.getSens().equals(Constantes.STOCK_SENS_SORTIE)) {
                System.err.println(QteNorme + " Composant .. " + fc.getComposant().getArticle().getDesignation());
                idx = ordre.getListComposantOf().indexOf(fc.getComposant());
                if (idx >= 0) {
                    qtePrevu = ordre.getListComposantOf().get(idx).getQuantitePrevu();
                    if (qtePrevu != 0) {
                        qte = (qtePrevu * qteNorme) / QteNorme;
                        idx = tempsList.indexOf(new Options(null, fc.getComposant().getId().intValue()));
                        if (idx >= 0) {
                            pr = (Double) tempsList.get(idx).getValeur();
                        } else {
                            pr = findLastPr(fc.getComposant().getArticle().getId(),
                                    depot, date, fc.getComposant().getUnite().getId());
                        }
                        valeur += (pr * qte);
                    }
                }
            } else {

            }
        }
        return valeur;
    }

    public void recalculCurrentOrder() {
        recalculOrder(selectedOf);
    }

    public void recalculOrder(YvsProdOrdreFabrication selectedOf) {
        selectedOf.setCoutOf(valoriseOf(selectedOf));
        dao.update(selectedOf);
        Double totalDeclare = (Double) dao.loadObjectByNameQueries("YvsProdDeclarationProduction.findSumDeclare", new String[]{"ordre", "statut"}, new Object[]{selectedOf, Constantes.STATUT_DOC_VALIDE});
        totalDeclare = totalDeclare != null ? totalDeclare : 0;
        if (totalDeclare > 0) {
            selectedOf.setDeclarations(dao.loadNameQueries("YvsProdDeclarationProduction.findByOF", new String[]{"ordre"}, new Object[]{selectedOf}));
            for (YvsProdDeclarationProduction d : selectedOf.getDeclarations()) {
                d.setCoutProduction(selectedOf.getCoutOf() / totalDeclare);
                if (ordre != null ? selectedOf.getId().equals(ordre.getId()) : false) {
                    int idx = ordre.getDeclarations().indexOf(d);
                    if (idx > -1) {
                        ordre.getDeclarations().set(idx, d);
                    }
                }
                dao.update(d);
            }
        }
        if (ordre != null ? selectedOf.getId().equals(ordre.getId()) : false) {
            ordre.setCoutDeProduction(selectedOf.getCoutOf());
        }
        if (listeOrdreF != null ? !listeOrdreF.isEmpty() : false) {
            int idx = listeOrdreF.indexOf(selectedOf);
            if (idx > -1) {
                listeOrdreF.set(idx, selectedOf);
            }
        }
    }

    public void recalculOrder() {
        List<Integer> indexs = decomposeSelection(chaineSelectOf);
        if (indexs != null ? !indexs.isEmpty() : false) {
            YvsProdOrdreFabrication of;
            for (Integer index : indexs) {
                of = listeOrdreF.get(index);
                recalculOrder(of);
            }
            succes();
        }
    }

    public double valoriseOf(YvsProdOrdreFabrication of) {
        Double re = 0d;
        if (of != null) {
            List<YvsProdSuiviOperations> list = dao.loadNameQueries("YvsProdSuiviOperations.findByOF", new String[]{"ordre"}, new Object[]{of});
            //valaorise la matière
            for (YvsProdSuiviOperations op : list) {
                re += valoriseOperation(op);
            }
            of.setCoutOf(re);
            //Valorise l'opération= Cout de l'op/Temps passé à cette opération
        }
        return re;
    }

    public void openToConfirmDelete(YvsProdDeclarationProduction y) {
        selectDeclaration = y;
        selectedOf = y.getOrdre();
        if (!notRemindDeleteDecl) {
            openDialog("dlgConfirmDeleteDec");
        } else {
            deleteDeclaration();
        }
    }

    public void deleteDeclaration() {
        try {
            if (selectedOf != null ? selectedOf.getId() > 0 : false) {
                if (selectDeclaration.getStatut().equals(Constantes.STATUT_DOC_TERMINE)) {
                    openDialog("dlgConfirmDeleteDeclaration");
                    return;
                }
                if (selectDeclaration.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                    getErrorMessage("Vous ne pouvez supprimer cette déclaration. Elle est déjà validé !");
                    return;
                }
                List<YvsProdConditionnementDeclaration> list = dao.loadNameQueries("YvsProdConditionnementDeclaration.findyDeclaration", new String[]{"declaration"}, new Object[]{selectDeclaration});
                dao.delete(selectDeclaration);
                for (YvsProdConditionnementDeclaration d : list) {
                    dao.delete(d.getConditionnement());
                }
                selectedOf.getDeclarations().remove(selectDeclaration);
                ordre.getDeclarations().remove(selectDeclaration);
                if (declaration.getId() == selectDeclaration.getId()) {
                    resetDeclaration();
                }
                declarations.remove(selectDeclaration);
                afterSaveDeclaration();
                succes();
                update("data-declaration_of");
                update("data_declaration");
            } else {
                getErrorMessage("Vous devez perciser l'ordre de fabrication");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("Error (saveDeclaration)", ex);
        }
    }

    private void afterSaveDeclaration() {
        //change le statut de l'of si la soe des quantité déclaré est égale à la somme produite
        Double sum = (Double) dao.loadObjectByNameQueries("YvsProdDeclarationProduction.findSumDeclare", new String[]{"ordre", "statut"}, new Object[]{selectedOf, Constantes.STATUT_DOC_VALIDE});
        sum = (sum != null) ? sum : 0d;
        if (sum >= selectedOf.getQuantite()) {
            if (paramProduction.getCloseDeclAuto()) {
                if (ordre.isEndAll()) {
                    selectedOf.setStatutOrdre(Constantes.ETAT_TERMINE);
                    selectedOf.setStatutDeclaration(Constantes.ETAT_TERMINE);
                    ordre.setStatusOrdre(Constantes.ETAT_TERMINE);
                    ordre.setStatutDeclaration(Constantes.ETAT_TERMINE);
                    selectedOf.setQuantite(ordre.getQuantitePrevu());
                } else {
                    if (paramProduction.getDeclareWhenFinishOf()) {
                        finishOperation(true);
                    } else {
                        openDialog("dlgConfirmFinAllOp");
                    }
                    if (transfertOf) {
                        transfertAllProd();
                    }
                    return;
                }
            } else {
                selectedOf.setStatutDeclaration(Constantes.ETAT_ENCOURS);
                ordre.setStatutDeclaration(Constantes.ETAT_ENCOURS);
            }
        } else if (sum > 0) {
            selectedOf.setStatutDeclaration(Constantes.ETAT_ENCOURS);
            ordre.setStatutDeclaration(Constantes.ETAT_ENCOURS);
        } else {
            selectedOf.setStatutDeclaration(Constantes.ETAT_ATTENTE);
            ordre.setStatutDeclaration(Constantes.ETAT_ATTENTE);
        }
        if (declaration.isTerminer()) {
            if (ordre.isEndAll()) {
                selectedOf.setStatutOrdre(Constantes.ETAT_TERMINE);
                selectedOf.setStatutDeclaration(Constantes.ETAT_TERMINE);
                ordre.setStatusOrdre(Constantes.ETAT_TERMINE);
                ordre.setStatutDeclaration(Constantes.ETAT_TERMINE);
                selectedOf.setQuantite(ordre.getQuantitePrevu());
            } else {
                if (paramProduction.getDeclareWhenFinishOf()) {
                    finishOperation(true);
                } else {
                    openDialog("dlgConfirmFinAllOp");
                }
                if (transfertOf) {
                    transfertAllProd();
                }
                return;
            }
        }
        dao.update(selectedOf);
        if (transfertOf) {
            transfertAllProd();
        }
        int idx = listeOrdreF.indexOf(selectedOf);
        if (idx >= 0) {
            listeOrdreF.set(idx, selectedOf);
        }
    }

    public void changestatutDec(YvsProdOrdreFabrication of) {
        for (YvsProdDeclarationProduction d : of.getDeclarations()) {
            changeStatutDeclaration(d);
        }
    }

    private boolean controleChangeDeclaration(YvsProdDeclarationProduction doc) {
        char statut;
        if (doc != null) {
            if (doc.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                statut = Constantes.STATUT_DOC_ATTENTE;
            } else {
                statut = Constantes.STATUT_DOC_VALIDE;
            }
            if (statut != Constantes.STATUT_DOC_VALIDE && doc.getStatut() == Constantes.STATUT_DOC_VALIDE) {
                if (!controleInventaire(doc.getSessionOf().getSessionProd().getDepot().getId(), doc.getSessionOf().getSessionProd().getDateSession(), doc.getSessionOf().getSessionProd().getTranche().getId())) {
                    return false;
                }
                //vérifie qu'on n'ai pas de conditionnement
                if (!doc.getConditionnements().isEmpty()) {
                    getErrorMessage("Impossible de modifier le statut", " des ordres de conditionnements ont été trouvés pour cette déclaration !");
                    return false;
                }
                double stock = dao.stocks(selectedOf.getArticle().getId(), 0, doc.getSessionOf().getSessionProd().getDepot().getId(), 0, 0, doc.getSessionOf().getSessionProd().getDateSession(), (doc.getConditionnement() != null) ? doc.getConditionnement().getId() : -1, 0);
                if (stock < doc.getQuantite()) {
                    getErrorMessage("L'article '" + selectedOf.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
                    return false;
                }
                stock = dao.stocks(selectedOf.getArticle().getId(), 0, doc.getSessionOf().getSessionProd().getDepot().getId(), 0, 0, new Date(), (doc.getConditionnement() != null) ? doc.getConditionnement().getId() : -1, 0);
                if (stock < doc.getQuantite()) {
                    getErrorMessage("L'article '" + selectedOf.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean changeStatutDeclaration(YvsProdDeclarationProduction doc) {
        char statut;
        if (doc != null) {
            if (doc.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                statut = Constantes.STATUT_DOC_ATTENTE;
            } else {
                statut = Constantes.STATUT_DOC_VALIDE;
            }
            if (!controleChangeDeclaration(doc)) {
                return false;
            }
            String rq = "UPDATE yvs_prod_declaration_production SET statut = '" + statut + "' WHERE id=?";
            Options[] param = new Options[]{new Options(doc.getId(), 1)};
            dao.requeteLibre(rq, param);
            doc.setStatut(statut);
            int idx = ordre.getDeclarations().indexOf(doc);
            if (idx > -1) {
                ordre.getDeclarations().set(idx, doc);
            }
            idx = selectedOf.getDeclarations().indexOf(doc);
            if (idx > -1) {
                selectedOf.getDeclarations().set(idx, doc);
            }
            afterSaveDeclaration();
            succes();
            return true;
        } else {
            getErrorMessage("Vous devez selectionner une doc");
        }
        return false;
    }

    public boolean canDeclared(YvsProdOrdreFabrication y) {
        return canDeclared(dao.loadNameQueries("YvsProdOperationsOF.findByOF", new String[]{"ordre"}, new Object[]{y}));
    }

    public boolean canDeclared() {
        return canDeclared(ordre.getListOperationsOf());
    }

    private boolean canDeclared(List<YvsProdOperationsOF> list) {
        if (ordre.isSuiviOperations()) {
            if (list != null ? !list.isEmpty() : false) {
                for (YvsProdOperationsOF o : list) {
                    if (o.getStatutOp().equals(Constantes.ETAT_ATTENTE) || o.getStatutOp().equals(Constantes.ETAT_EDITABLE) || o.getStatutOp().equals(Constantes.ETAT_ANNULE)) {
                        return false;
                    }
                    if (!Constantes.TYPE_OF_REPETITIF.equals(ordre.getTypeOf()) && !Constantes.ETAT_TERMINE.equals(o.getStatutOp())) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return ordre.getStatusOrdre().equals(Constantes.ETAT_TERMINE) || ordre.getStatusOrdre().equals(Constantes.ETAT_CLOTURE);
        }
        return false;
    }

    public void resetConditionner() {
        ManagedConditionnement w = (ManagedConditionnement) giveManagedBean(ManagedConditionnement.class
        );
        if (w
                != null) {
            YvsProdDeclarationProduction y = new YvsProdDeclarationProduction(w.getSelectDeclaration());
            w.resetFiche();
            w.setFiche(new FicheConditionnement());
            w.getFiche().setQuantite(y.getReste());
            w.chooseArticle(UtilProd.buildSimpleBeanArticles(selectedOf.getArticle()), true);
            w.setSelectDeclaration(y);
        }
    }

    public void beginConditionner(YvsProdDeclarationProduction y) {
        if (y != null ? y.getId() > 0 : false) {
            if (y.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                ManagedConditionnement w = (ManagedConditionnement) giveManagedBean(ManagedConditionnement.class
                );
                if (w
                        != null) {
                    w.clearParams(false);
                    w.setFiche(new FicheConditionnement());
                    w.getFiche().setDepot(UtilCom.buildBeanDepot(y.getSessionOf().getSessionProd().getDepot()));
                    w.getFiche().setDateConditionnement(y.getSessionOf().getSessionProd().getDateSession());
                    w.getFiche().setQuantite(y.getReste());
                    w.chooseArticle(UtilProd.buildSimpleBeanArticles(selectedOf.getArticle()), true);

                    w.setSelectDeclaration(y);
                    w.loadAll(true, true);
                    openDialog("dlgConditionnement");
                }
            } else {
                getErrorMessage("La declaration doit etre validée");
            }
        }
    }
    public boolean date_, displayQteDeclarer;
    private String egaliteStatutOf = "IN", egaliteStatutDec = " =", egaliteSite = " IN ";
    private List<String> statutsOf = new ArrayList<>();
    private String statutDec, statutOf, etatDec = "";
    private Date dateDebut = new Date(), dateFin = new Date();
    private int siteSearch;
    public String article_, numSearch_;
    List<Integer> sitesSeach = new ArrayList<>();

    public boolean isDisplayQteDeclarer() {
        return displayQteDeclarer;
    }

    public void setDisplayQteDeclarer(boolean displayQteDeclarer) {
        this.displayQteDeclarer = displayQteDeclarer;
    }

    public String getStatutOf() {
        return statutOf;
    }

    public void setStatutOf(String statutOf) {
        this.statutOf = statutOf;
    }

    public String getEtatDec() {
        return etatDec;
    }

    public void setEtatDec(String etatDec) {
        this.etatDec = etatDec;
    }

    public boolean isDate_() {
        return date_;
    }

    public void setDate_(boolean date_) {
        this.date_ = date_;
    }

    public String getEgaliteStatutOf() {
        return egaliteStatutOf;
    }

    public void setEgaliteStatutOf(String egaliteStatutOf) {
        this.egaliteStatutOf = egaliteStatutOf;
    }

    public String getEgaliteStatutDec() {
        return egaliteStatutDec;
    }

    public void setEgaliteStatutDec(String egaliteStatutDec) {
        this.egaliteStatutDec = egaliteStatutDec;
    }

    public List<String> getStatutsOf() {
        return statutsOf;
    }

    public void setStatutsOf(List<String> statutsOf) {
        this.statutsOf = statutsOf;
    }

    public String getStatutDec() {
        return statutDec;
    }

    public void setStatutDec(String statutDec) {
        this.statutDec = statutDec;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getArticle_() {
        return article_;
    }

    public void setArticle_(String article_) {
        this.article_ = article_;
    }

    public List<YvsProdOfSuiviFlux> getListeFluxComposant() {
        return listeFluxComposant;
    }

    public void setListeFluxComposant(List<YvsProdOfSuiviFlux> listeFluxComposant) {
        this.listeFluxComposant = listeFluxComposant;
    }

    public List<String> getAllStatutsOf() {
        return allStatutsOf;
    }

    public void setAllStatutsOf(List<String> allStatutsOf) {
        this.allStatutsOf = allStatutsOf;
    }

    public double getQteDeclarer(YvsProdOrdreFabrication y) {
        Double qte = (Double) dao.loadObjectByNameQueries("YvsProdDeclarationProduction.findSumDeclare", new String[]{"ordre", "statut"}, new Object[]{y, Constantes.STATUT_DOC_VALIDE});
        return qte != null ? qte : 0;
    }

    public void clearParams() {
        if (!typePage.equals(Constantes.PROD_TYPE_PAGE_SUIVI)) {
            statutsOf.clear();
        }
        initForm = true;
        paginator.clear();
        //init variable
        date_ = false;
        idsSearch = "";
        siteDSearch = 0;
        article_ = null;
        statutOf = null;
        statutDec = null;
        numSearch_ = null;
        groupProd = true;
        loadAllOf(true, initForm);
    }

    public void _chooseDateSearch(ValueChangeEvent ev) {
        date_ = (Boolean) ev.getNewValue();
        addParaDate(date_);
    }

    public void addParamDate1(SelectEvent ev) {
        addParaDate(date_);
    }

    public void addParamDate2() {
        addParaDate(date_);
    }

    public int getSiteSearch() {
        return siteSearch;
    }

    public void setSiteSearch(int siteSearch) {
        this.siteSearch = siteSearch;
    }

    public String getNumSearch_() {
        return numSearch_;
    }

    public void setNumSearch_(String numSearch_) {
        this.numSearch_ = numSearch_;
    }

    private void addParametreSite() {
        ParametreRequete p = new ParametreRequete("y.siteProduction.id", "sites", null, "IN", "AND");
        if (!sitesSeach.isEmpty()) {
            p.setObjet(sitesSeach);
        }
        paginator.addParam(p);
        initForm = true;
    }

    private void addParametreDate(boolean b) {
        ParametreRequete p = new ParametreRequete("y.dateDebut", "date", null, " BETWEEN ", "AND");
        if (b) {
            if (dateDebut != null && dateFin != null) {
                p.setObjet(dateDebut);
                p.setOtherObjet(dateFin);
            }
        }
        paginator.addParam(p);
        initForm = true;
    }

    private void addParamDate_only(boolean b) {
        addParametreDate(b);
    }

    private void addParaDate(boolean b) {
        addParametreDate(b);
        loadAllOf(true, true);
    }

    public void addParamStatuOf(ValueChangeEvent ev) {
        statutOf = (String) ev.getNewValue();
        if (statutOf != null) {
            String[] mots = statutOf.split("-");
            if (mots[0].equals("E")) {
                egaliteStatutOf = "IN";
            } else {
                egaliteStatutOf = "NOT IN";
            }
            this.statutOf = mots[1];
        } else {
            this.statutOf = (String) ev.getNewValue();
        }
        if (statutsOf == null) {
            statutsOf = new ArrayList<>();
        }
        statutsOf.add((statutOf == null) ? "" : statutOf);

        searchByStatutOF();
    }

    public void addParamStatut(ValueChangeEvent ev) {
        String statut = ((String) ev.getNewValue());
        if (statut != null ? statut.trim().equals("Z") : false) {
            openDialog("dlgMoreStatuts");
        } else {
            addParamStatuOf(ev);
        }
    }

    public void addParamStatuDeclaration(ValueChangeEvent ev) {
        statutDec = (String) ev.getNewValue();
        searchByStatutDec();
    }

    public void addParamEtatDeclarer() {
        ParametreRequete p = new ParametreRequete("y.id", "ids_from_declare", null, "IN", "AND");
        if (Util.asString(etatDec)) {
            String query = "SELECT p.id FROM yvs_prod_ordre_fabrication p LEFT JOIN yvs_prod_declaration_production d ON (p.id = d.ordre AND d.statut = 'V') WHERE p.societe = ?";
            switch (etatDec) {
                case "EQUAL_DECLARE": {
                    query += " GROUP BY p.id HAVING p.quantite_fabrique::int = SUM(d.quantite)::int";
                    break;
                }
                case "PLUS_DECLARE": {
                    query += " GROUP BY p.id HAVING p.quantite_fabrique::int < SUM(d.quantite)::int";
                    break;
                }
                case "MOINS_DECLARE": {
                    query += " GROUP BY p.id HAVING p.quantite_fabrique::int > SUM(d.quantite)::int";
                    break;
                }
                //NON_DECLARE
                default: {
                    query += " AND d.id IS NULL";
                    break;
                }
            }
            query += " ORDER BY p.date_save LIMIT 1000";
            List<Long> ids_from_declare = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
            if (ids_from_declare == null || ids_from_declare.isEmpty()) {
                ids_from_declare = new ArrayList<Long>() {
                    {
                        add(0L);
                    }
                };
            }
            p = new ParametreRequete("y.id", "ids_from_declare", ids_from_declare, "IN", "AND");
        }
        paginator.addParam(p);
        initForm = true;
    }

    public void searchByStatutDec_only() {
        ParametreRequete p = new ParametreRequete("y.statutDeclaration", "statutDeclaration", statutDec, egaliteStatutDec, "AND");
        paginator.addParam(p);
        initForm = true;
    }

    public void searchByStatutDec() {
        searchByStatutDec_only();
        loadAllOf(true, initForm);
    }

    public void addParamSite() {
        ParametreRequete p = new ParametreRequete("y.siteProduction", "site", null, "=", "AND");
        if (siteSearch > 0) {
            p = new ParametreRequete("y.siteProduction", "site", new YvsProdSiteProduction(siteSearch), "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllOf(true, initForm);
    }

    public void addParametreStatutOF() {
        ParametreRequete p = new ParametreRequete("y.statutOrdre", "statutOrdre", statutsOf.isEmpty() ? null : statutsOf, egaliteStatutOf, "AND");
        paginator.addParam(p);
        initForm = true;
    }

    public void searchByStatutOF_only() {
        statutsOf.clear();
        statutsOf.add(Constantes.ETAT_ATTENTE);
        statutsOf.add(Constantes.ETAT_ENCOURS);
        egaliteStatutOf = "IN";
        addParametreStatutOF();
    }

    public void searchByStatutOF() {
        addParametreStatutOF();
        loadAllOf(true, initForm);
    }

    public void applyMostSearch() {
        loadAllOf(true, initForm);
    }

    public void addParamArticle() {
        article_ = (article_ != null) ? (article_.trim().isEmpty() ? null : article_) : article_;
        ParametreRequete p0 = new ParametreRequete("y.article.refArt", "article", article_, " LIKE ", "AND");
        if (article_ != null) {
            p0.setAttribut(null);
            p0.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "refArt", article_.trim().toUpperCase() + "%", " LIKE ", " OR "));
            p0.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "designation", article_.trim().toUpperCase() + "%", " LIKE ", "AND"));
        }
        paginator.addParam(p0);
        initForm = true;
        loadAllOf(true, initForm);
    }

    public void addParamNumRef() {
        numSearch_ = (numSearch_ != null) ? (numSearch_.trim().isEmpty() ? null : numSearch_.trim().toUpperCase()) : numSearch_;
        ParametreRequete p0 = new ParametreRequete("UPPER(y.codeRef)", "codeRef", numSearch_, "LIKE", "AND");
        paginator.addParam(p0);
        initForm = true;
        loadAllOf(true, initForm);
    }

    /**
     * Filtrage des données de sessions*
     */
    public void addParamDateSession(boolean b) {
        ParametreRequete p = new ParametreRequete("y.dateSession", "date", null, " BETWEEN ", "AND");
        if (b) {
            if (dateDebut != null && dateFin != null) {
                if (dateDebut.before(dateFin) || dateDebut.equals(dateFin)) {
                    p.setObjet(dateDebut);
                    p.setOtherObjet(dateFin);
                }
            }
        }
        p_sessions.addParam(p);
        initForm = true;
    }

    public void addParamProducteurSession() {
        if (currentUser != null) {
            p_sessions.addParam(new ParametreRequete("y.producteur", "producteur", currentUser.getUsers(), "=", "AND"));
        }
    }

    public void addParamEquipeSession() {
        if (idsCurrentEquipes.isEmpty()) {
            idsCurrentEquipes.add(-1L);
        }
        p_sessions.addParam(new ParametreRequete("y.equipe.id", "ids", idsCurrentEquipes, "IN", "AND"));
    }

    /*build reap flux de composants*/
    private List<Object[]> listeDate = new ArrayList<>();
    private List<YvsProdEquipeProduction> listeEquipe = new ArrayList<>();
    private List<YvsProdEquipeProduction> listeEquipeFilter = new ArrayList<>();
    private List<YvsProdOfSuiviFlux> listeFlux, listeFluxComposant;
    private EquipeProduction filterEquipeResume = new EquipeProduction();
    private boolean filterDate = false;
    private Date filterDateResume = new Date();
    private Date filterDateResume2 = new Date();

    public List<Object[]> getListeDate() {
        return listeDate;
    }

    public void setListeDate(List<Object[]> listeDate) {
        this.listeDate = listeDate;
    }

    public List<YvsProdEquipeProduction> getListeEquipe() {
        return listeEquipe;
    }

    public void setListeEquipe(List<YvsProdEquipeProduction> listeEquipe) {
        this.listeEquipe = listeEquipe;
    }

    public Date getFilterDateResume() {
        return filterDateResume;
    }

    public void setFilterDateResume(Date filterDateResume) {
        this.filterDateResume = filterDateResume;
    }

    public Date getFilterDateResume2() {
        return filterDateResume2;
    }

    public void setFilterDateResume2(Date filterDateResume2) {
        this.filterDateResume2 = filterDateResume2;
    }

    public EquipeProduction getFilterEquipeResume() {
        return filterEquipeResume;
    }

    public void setFilterEquipeResume(EquipeProduction filterEquipeResume) {
        this.filterEquipeResume = filterEquipeResume;
    }

    public List<YvsProdEquipeProduction> getListeEquipeFilter() {
        return listeEquipeFilter;
    }

    public void setListeEquipeFilter(List<YvsProdEquipeProduction> listeEquipeFilter) {
        this.listeEquipeFilter = listeEquipeFilter;
    }

    public boolean isFilterDate() {
        return filterDate;
    }

    public void setFilterDate(boolean filterDate) {
        this.filterDate = filterDate;
    }

    /**
     * *
     * @param all : Le paramètre all détermine si l'on charge le résumé de l'OF
     * ou Juste le résumé d'une equipe
     */
    private boolean allFlux;

    public void loadAllFluxComposant(boolean all) {
        loadAllFluxComposant(selectedOf, all);
    }

    public void loadAllFluxComposant(YvsProdOrdreFabrication selectedOf, boolean all) {
        this.allFlux = all;
        if (selectedOf != null) {
            listeEquipeFilter.clear();
            if (all) {
                listeFlux = dao.loadNameQueries("YvsProdOfSuiviFlux.findByOf", new String[]{"ordre"}, new Object[]{selectedOf});
                //1. Liste des équipes ayant participé
                listeDate = dao.loadNameQueries("YvsProdSessionOf.findDateByOrdre_", new String[]{"ordre"}, new Object[]{selectedOf});
                //2. Liste des date et tranche           
                listeEquipe = dao.loadNameQueries("YvsProdSessionOf.findEquipeByOrdre", new String[]{"ordre"}, new Object[]{selectedOf});
                listeEquipeFilter.addAll(listeEquipe);
            } else {
                if (currentSession != null) {
                    listeFlux = dao.loadNameQueries("YvsProdOfSuiviFlux.findByOfProducteur", new String[]{"ordre", "producteur"}, new Object[]{selectedOf, currentSession.getProducteur()});
                    listeDate = dao.loadNameQueries("YvsProdSessionOf.findDateByProducteurOrdre_", new String[]{"ordre", "producteur"}, new Object[]{selectedOf, currentSession.getProducteur()});
                    listeEquipe = dao.loadNameQueries("YvsProdSessionOf.findEquipeProducteurOrdre", new String[]{"ordre", "producteur"}, new Object[]{selectedOf, currentSession.getProducteur()});
                    listeEquipeFilter.addAll(listeEquipe);
                }
            }
        }
        openDialog("dlgResumeFlux");
        update("table_resume_flux_equipe");
    }

    public void loadCustumDataFlux() {
        if ((filterDate && filterDateResume != null && filterDateResume2 != null)) {
            if (this.allFlux) {
                listeDate = dao.loadNameQueries("YvsProdSessionOf.findDateByOrdre", new String[]{"ordre", "date1", "date2"}, new Object[]{selectedOf, filterDateResume, filterDateResume2});
            } else {
                listeDate = dao.loadNameQueries("YvsProdSessionOf.findDateByProducteurOrdre", new String[]{"ordre", "producteur", "date1", "date2"}, new Object[]{selectedOf, currentSession.getProducteur(), filterDateResume, filterDateResume2});
            }
        } else {
            loadAllFluxComposant(this.allFlux);
        }
        if (filterEquipeResume != null ? filterEquipeResume.getId() > 0 : false) {
            int idx = listeEquipeFilter.indexOf(new YvsProdEquipeProduction(filterEquipeResume.getId()));
            if (idx >= 0) {
                listeEquipe.clear();
                listeEquipe.add(listeEquipeFilter.get(idx));
            }
        } else {
            listeEquipe.clear();
            listeEquipe.addAll(listeEquipeFilter);
        }
    }

    public double giveValueFlux(YvsProdEquipeProduction eq, Date date, YvsGrhTrancheHoraire tranche, YvsProdComposantOF composant, boolean conso) {
        if (listeFlux != null) {
            for (YvsProdOfSuiviFlux f : listeFlux) {
                if (conso) {
                    if (f.getIdOperation().getSessionOf().getSessionProd().getEquipe().equals(eq) && f.getIdOperation().getSessionOf().getSessionProd().getDateSession().equals(date) && f.getIdOperation().getSessionOf().getSessionProd().getTranche().equals(tranche) && f.getComposant().getComposant().equals(composant) && f.getComposant().getSens().equals(Constantes.STOCK_SENS_SORTIE)) {
                        return f.getQuantite();
                    }
                } else {
                    if (f.getIdOperation().getSessionOf().getSessionProd().getEquipe().equals(eq) && f.getIdOperation().getSessionOf().getSessionProd().getDateSession().equals(date) && f.getIdOperation().getSessionOf().getSessionProd().getTranche().equals(tranche) && f.getComposant().getComposant().equals(composant) && f.getComposant().getSens().equals(Constantes.STOCK_SENS_ENTREE)) {
                        return f.getQuantite();
                    }
                }
            }
        }
        return 0d;
    }

    public double giveValueLine(YvsProdEquipeProduction eq, Date date, YvsGrhTrancheHoraire tranche) {
        double soe = 0;
        if (listeFlux != null) {
            for (YvsProdOfSuiviFlux f : listeFlux) {
                if (f.getIdOperation().getSessionOf().getSessionProd().getEquipe().equals(eq) && f.getIdOperation().getSessionOf().getSessionProd().getDateSession().equals(date) && f.getIdOperation().getSessionOf().getSessionProd().getTranche().equals(tranche)) {
                    soe += f.getQuantite();
                }
            }
        }
        return soe;
    }

    public void initTransfert(ValueChangeEvent ev) {
        Boolean re = (Boolean) ev.getNewValue();
        initTransfert_(re);
    }

    public void initTransfert_(Boolean re) {
        if (re) {
            //charge les dépôts lié
            if (currentSession != null ? currentSession.getDepot() != null : false) {
                depotsCible = dao.loadNameQueries("YvsComLiaisonDepot.findDepotLierByDepot_", new String[]{"depot"}, new Object[]{currentSession.getDepot()});
                if (!depotsCible.isEmpty()) {
                    depotDest = UtilProd.buildBeanDepot(depotsCible.get(0));
                    //charge ses tranches
                    loadTranchesCible(depotsCible.get(0));
                    update("chp_depot_tr_decl_prod");
                } else {
                    getWarningMessage("Aucun dépôt de liaison n'a été trouvé avec le dépôt de déclaration!");
                }
            } else {
                getErrorMessage("Vous devez choisir le dépôt de déclarations !");
            }
        }
    }

    public void chooseDepotCible(ValueChangeEvent ev) {
        Long re = (Long) ev.getNewValue();
        if (re != null ? re > 0 : false) {
            int idx = depotsCible.indexOf(new YvsBaseDepots(re));
            if (idx >= 0) {
                depotDest = UtilProd.buildBeanDepot(depotsCible.get(idx));
                loadTranchesCible(depotsCible.get(idx));
            }
        }
    }

    public void loadTranchesCible(YvsBaseDepots depot) {
        tranchesCible = loadTranche(depot, declaration.getSessionOf().getDateSession());
        update("chp_tranche_tr_decl_prod");
    }

    public void transfertAllProd() {
        ManagedTransfertStock service = (ManagedTransfertStock) giveManagedBean(ManagedTransfertStock.class);
        if (service != null && declaration.getSessionOf() != null ? declaration.getSessionOf().getId() > 0 : false) {
            //vérifie si l'article existe dans le dépôt
            Long nb = (Long) dao.loadObjectByNameQueries("YvsBaseArticleDepot.countByArticleDepot", new String[]{"article", "depot"}, new Object[]{selectedOf.getArticle(), new YvsBaseDepots(depotDest.getId())});
            if (nb != null ? nb <= 0 : true) {
                getErrorMessage("Impossible de continuer ce transfert", "Cet article n'est pas référencé dans le dépôt cible");
                return;
            }
            //Cherche un doc de transfert entre les deux dépôt à cette date et au créneau
            YvsComCreneauDepot crSrc = (YvsComCreneauDepot) dao.loadOneByNameQueries("YvsComCreneauDepot.findByTrancheDepot", new String[]{"tranche", "depot"}, new Object[]{new YvsGrhTrancheHoraire(declaration.getSessionOf().getTranche().getId()), new YvsBaseDepots(declaration.getSessionOf().getDepot().getId())});
            if ((crSrc != null) ? crSrc.getId() < 1 : true) {
                getErrorMessage("Vous devez specifier le créneau source");
                return;
            }
            YvsComCreneauDepot crDest = (YvsComCreneauDepot) dao.loadOneByNameQueries("YvsComCreneauDepot.findByTrancheDepot", new String[]{"tranche", "depot"}, new Object[]{new YvsGrhTrancheHoraire(trancheHoraire.getId()), new YvsBaseDepots(depotDest.getId())});
            if ((crDest != null) ? crDest.getId() < 1 : true) {
                getErrorMessage("Vous devez specifier le créneau de reception");
                return;
            }
            YvsComDocStocks doc = null;
            if (crDest != null && crSrc != null) {
                doc = (YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findOneDocStockToEdit", new String[]{"statut", "statut1", "statut2", "date", "crSource", "crDest"},
                        new Object[]{Constantes.ETAT_EDITABLE, Constantes.ETAT_ENCOURS, Constantes.ETAT_SOUMIS, declaration.getSessionOf().getDateSession(), crSrc, crDest});
            }
            if (doc == null) {
                service.setDocStock(new DocStock());
                service.getDocStock().setActif(true);
                service.getDocStock().setCreneauDestinataire(UtilCom.buildBeanCreneau(crDest));
                service.getDocStock().setCreneauSource(UtilCom.buildBeanCreneau(crSrc));
                service.getDocStock().setDateDoc(declaration.getSessionOf().getDateSession());
                service.getDocStock().setDateReception(declaration.getSessionOf().getDateSession());
                service.getDocStock().setDescription("Transfert de production");
                service.getDocStock().setDestination(depotDest);
                service.getDocStock().setSource(declaration.getSessionOf().getDepot());
                service.getDocStock().setStatut(Constantes.ETAT_EDITABLE);
                service.getDocStock().setTypeDoc(Constantes.TYPE_FT);
                service.getDocStock().setNature(Constantes.TRANSFERT);
            } else {
                //charge le contenu
                doc.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{doc}));
                service.setDocStock(UtilCom.buildBeanDocStock(doc));
            }
            //contrôle le stock

            if (service.getDocStock().getStatut().equals(Constantes.ETAT_SOUMIS) || service.getDocStock().getStatut().equals(Constantes.ETAT_ENCOURS)) {
                String result = controleStock(ordre.getArticles().getId(), declaration.getConditionnement().getId(), declaration.getSessionOf().getDepot().getId(), 0L, declaration.getQuantite(), 0, "INSERT", "S", declaration.getSessionOf().getDateSession(), 0);
                if (result != null) {
                    getErrorMessage("Impossible de valider cette fiche d'inventaire car la ligne d'article " + ordre.getArticles().getDesignation() + " engendrera une incohérence dans le stock au " + result);
                    return;
                }
            }
            ContenuDocStock c = new ContenuDocStock();
            c.setActif(true);
            c.setArticle(ordre.getArticles());
            c.setConditionnement(declaration.getConditionnement());
            c.setDateContenu(declaration.getSessionOf().getDateSession());
            c.setPrixEntree(declaration.getCout());
            c.setPrix(declaration.getCout());
            c.setQteAttente(declaration.getQuantite());
            c.setQuantite(declaration.getQuantite());
            c.setResultante(declaration.getQuantite());
            c.setQteAttente(declaration.getQuantite());
            c.setStatut(Constantes.ETAT_EDITABLE);
            c.setUniteDestination(declaration.getConditionnement());
            c.setDocStock(service.getDocStock());
            service.setContenu(c);
            YvsComContenuDocStock re = service.saveEntityContenu();
            //update déclaration 
            if (re != null ? re.getId() > 0 : false) {
                selectDeclaration.setDocStock(re);
                dao.update(selectDeclaration);
                if (!service.getDocStock().getStatut().equals(Constantes.ETAT_SOUMIS) && !service.getDocStock().getStatut().equals(Constantes.ETAT_ENCOURS)) {
                    if (service.transmis()) {
                        getInfoMessage("Transmis !");
                    } else {
                        getWarningMessage("Production non transféré !");
                    }
                }
            } else {
                getWarningMessage("Transmission impossible !");
            }
        }
    }

    public void openViewToConfirmTransfert(YvsProdDeclarationProduction dec) {
        selectLineDeclaration(dec);
        initTransfert_(true);
        update("chp_depot_tr_decl_prod_1");
        update("chp_tranche_tr_decl_prod_1");
        openDialog("dlgTransfert");
    }

    public void choosePaginatorDeclaration(ValueChangeEvent ev) {
        long row;
        try {
            row = (long) ev.getNewValue();
        } catch (Exception ex) {
            row = (int) ev.getNewValue();
        }
        p_declaration.setRows((int) row);
        loadDeclaration(true, true);
    }

    public void gotoPagePaginatorDeclaration() {
        p_declaration.gotoPage(p_declaration.getRows());
        loadDeclaration(true, true);
    }

    public void addParamDateDeclaration() {
        ParametreRequete p = new ParametreRequete("y.sessionOf.sessionProd.dateSession", "date", null, " BETWEEN ", "AND");
        if (addDateDSearch ? !dateDebutDSearch.after(dateFinDSearch) : false) {
            p.setObjet(dateDebutDSearch);
            p.setOtherObjet(dateFinDSearch);
        }
        p_declaration.addParam(p);
        loadDeclaration(true, true);
    }

    public void addParamReferenceDeclaration() {
        ParametreRequete p = new ParametreRequete("y.ordre", "reference", null, "=", "AND");
        if (Util.asString(numeroDSearch)) {
            p = new ParametreRequete(null, "reference", numeroDSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.ordre.codeRef)", "reference", numeroDSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.ordre.numeroIdentification)", "reference", numeroDSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_declaration.addParam(p);
        loadDeclaration(true, true);
    }

    public void addParamArticleDeclaration() {
        ParametreRequete p = new ParametreRequete("y.conditionnement", "article", null, "=", "AND");
        if (Util.asString(articleDSearch)) {
            p = new ParametreRequete(null, "article", articleDSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.conditionnement.article.refArt)", "article", articleDSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.conditionnement.article.designation)", "article", articleDSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_declaration.addParam(p);
        loadDeclaration(true, true);
    }

    public void addParamDepotDeclaration() {
        ParametreRequete p = new ParametreRequete("y.sessionOf.sessionProd.depot", "depot", null, "=", "AND");
        if (depotDSearch > 0) {
            p.setObjet(new YvsBaseDepots(depotDSearch));
        }
        p_declaration.addParam(p);
        p = new ParametreRequete("y.sessionOf.sessionProd.tranche", "tranche", null, "=", "AND");
        trancheDSearch = 0;
        tranchesSearch = loadTranche(new YvsBaseDepots(depotDSearch), dateDebutDSearch);
        p_declaration.addParam(p);
        loadDeclaration(true, true);
    }

    public void addParamTrancheDeclaration() {
        ParametreRequete p = new ParametreRequete("y.sessionOf.sessionProd.tranche", "tranche", null, "=", "AND");
        if (trancheDSearch > 0) {
            p.setObjet(new YvsGrhTrancheHoraire(trancheDSearch));
        }
        p_declaration.addParam(p);
        loadDeclaration(true, true);
    }

    public void addParamEquipeDeclaration() {
        ParametreRequete p = new ParametreRequete("y.sessionOf.sessionProd.equipe", "equipe", null, "=", "AND");
        if (equipeDSearch > 0) {
            p.setObjet(new YvsProdEquipeProduction(equipeDSearch));
        }
        p_declaration.addParam(p);
        loadDeclaration(true, true);
    }

    public void clearParamDeclaration() {
        p_declaration.clear();
        idsDeclaration = "";
        loadDeclaration(true, true);
    }

    public void chooseGamme() {
        if (program.getGamme() != null ? program.getGamme().getId() > 0 : false) {
            int idx = program.getArticles().getGammes().indexOf(new YvsProdGammeArticle(program.getGamme().getId()));
            if (idx > -1) {
                program.setGamme(UtilProd.buildSimpleBeanGammeArticle(program.getArticles().getGammes().get(idx)));
            }
        }
    }

    public void chooseNomenclature() {
        if (program.getNomenclature() != null ? program.getNomenclature().getId() > 0 : false) {
            int idx = program.getArticles().getNomenclatures().indexOf(new YvsProdNomenclature(program.getNomenclature().getId()));
            if (idx > -1) {
                program.setNomenclature(UtilProd.buildBeanNomenclature(program.getArticles().getNomenclatures().get(idx)));
            }
        }
    }

    public void loadComposantsNomenclature() {
        if (program.getNomenclature() != null ? program.getNomenclature().getId() > 0 : false) {
            program.getNomenclature().setComposants(dao.loadNameQueries("YvsProdComposantNomenclature.findByNomenclature_1", new String[]{"nomenclature"}, new Object[]{new YvsProdNomenclature(program.getNomenclature().getId())}));
            for (YvsProdComposantNomenclature c : program.getNomenclature().getComposants()) {
                c.setValeur(c.getQuantite());
                c.setQuantite(0d);
            }
            openDialog("dlgComposantNom");
            update("data_composant_nom_of");
        } else {
            getErrorMessage("Veuillez choisir une nomenclature !");
        }
    }

    public void changeQuantiteMp(CellEditEvent event) {
        if (event.getRowIndex() >= 0) {
            YvsProdComposantNomenclature co = program.getNomenclature().getComposants().get(event.getRowIndex());
            Double newValue = co.getQuantite();
            double Q = (program.getNomenclature().getQuantite() * newValue) / co.getValeur();
            program.setQuantitePrevu(Constantes.arrondi(Q, 3));
            closeDialog("dlgComposantNom");
            update("form-programmation_of_f");
        }
    }

    public void saisirDepotProgram(Depots depot) {
        if (depot != null ? depot.getId() > 0 : false) {
            String code = depot.getCode();
            if ((code != null) ? code.trim().length() > 0 : false) {
                depot.setDesignation("");
                depot.setError(true);
                ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class
                );
                if (service
                        != null) {
                    Depots re = service.searchDepotByCode(code, false);
                    if (re.getId() > 0) {
                        cloneObject(depot, re);
                        System.err.println("re : " + re);
                        depot.setError(false);
                    }
                }
            }
        }
    }

    public void resetProgram() {
        program = new OrdreFabrication();
        program.setSuiviOperations(true);
        program.setId(0);
        program.setGenerateOfSousProduit(true);
        cloneObject(program.getDepotMp(), ordre.getDepotMp());
        cloneObject(program.getDepotPf(), ordre.getDepotPf());
        cloneObject(program.getSite(), ordre.getSite());
    }

    public void addProgram() {
        if (controleFiche(program, false, false, false)) {
            if (program.getId() == 0) {
                program.setId(ids--);
            }
            OrdreFabrication y = new OrdreFabrication();
            cloneObject(y, program);
            addOfToPlan(y);
            resetProgram();

        }
    }

    public void addOfToPlan(OrdreFabrication of) {
        of.setTypeOf(Constantes.TYPE_OF_PRODUCTION);
        of.setPriorite(1);
        of.setDateDebutLancement(dateLancement);
        of.setHeureDeLancement(new Date());
        of.setDateFinFabrication(dateLancement);
        of.setHeureFinFabrication(new Date());
        OrdreFabrication y = contains(of.getArticles());
        if (y == null) {
            programs.add(of);
        } else {
//            programs.get(programs.indexOf(y)).setQuantitePrevu(y.getQuantitePrevu() + of.getQuantitePrevu());
            Double qte = of.getQuantitePrevu();
            of = y;
            of.setQuantitePrevu(y.getQuantitePrevu() + qte);
        }
        if (of.getQuantitePrevu() <= 0) {
            programs.remove(of);
        }
        //parcours la nomenclature du produit et ajoute le sous composant...
        List<YvsProdComposantNomenclature> lc = dao.loadListTableByNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"}, new Object[]{new YvsProdNomenclature(of.getNomenclature().getId())});
        List<YvsProdComposantOF> l = calculBesoins(lc, UtilProd.buildBeanNomenclature(of.getNomenclature(), currentUser), of, true);
        of.setListComposantOf(l);
        int index = programs.indexOf(of);
        if (index > -1) {
            programs.set(index, of);
        }
        if (of.isGenerateOfSousProduit()) {
            Long id;
            YvsProdComposantNomenclature cn;
            boolean sousProduit;
            for (YvsProdComposantOF cof : l) {
                if (cof.getArticle().getId() != of.getArticles().getId()) { //On évite un bouclage infini
                    id = (Long) dao.loadObjectByNameQueries("YvsProdNomenclature.findIdByArticle", new String[]{"article"}, new Object[]{cof.getArticle()});
                    if (id != null ? id > 0 : false) {
                        cn = findComposantNom(cof.getArticle(), new YvsProdNomenclature(of.getNomenclature().getId()));
                        sousProduit = (cn != null) ? (cn.getType().equals(Constantes.PROD_OP_TYPE_COMPOSANT_SOUS_PRODUIT)) : false;
                        if (sousProduit) {
                            y = new OrdreFabrication(ids--);
                            y.setArticles(UtilProd.buildSimpleBeanArticles(cof.getArticle()));
                            y.setGamme(UtilProd.buildSimpleBeanGammeArticle(findGamme(cof.getArticle())));
                            y.setNomenclature(UtilProd.buildSimpleBeanNomenclatureAndUnite(findNomenclature(cof.getArticle())));
                            y.setQuantitePrevu(cof.getQuantitePrevu());
                            y.setGenerateOfSousProduit(of.isGenerateOfSousProduit());
                            y.setSuiviOperations(of.isSuiviOperations());
                            addOfToPlan(y);
                        }
                    }
                }
            }
        }
    }

    private OrdreFabrication contains(Articles a) {
        for (OrdreFabrication o : programs) {
            if (o.getArticles().equals(a)) {
                return o;
            }
        }
        return null;
    }

    public void removeProgram(OrdreFabrication y) {
        y.setQuantitePrevu(-y.getQuantitePrevu());
        y.setGenerateOfSousProduit(true);
        addOfToPlan(y);
    }

    public void loadToUpdateProgram(OrdreFabrication y) {
        cloneObject(program, y);
        program.setGenerateOfSousProduit(true);
        update("form-programmation_of");
    }

    public void displayComposantOf(OrdreFabrication y) {
        List<YvsProdComposantNomenclature> lc = dao.loadListTableByNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"}, new Object[]{new YvsProdNomenclature(y.getNomenclature().getId())});
        y.setListComposantOf(calculBesoins(lc, UtilProd.buildBeanNomenclature(y.getNomenclature(), currentUser), y, false));
        update("data-programmation_of");
    }

    public void resetFicheProgram() {
        programs.clear();
        resetProgram();
    }

    public void terminerProgram() {
        terminerProgram(true);
    }

    public void terminerProgram(boolean close) {
        try {
            int count = 0;
            List<YvsProdOperationsGamme> lop;
            for (OrdreFabrication bean : programs) {
                if (bean.getId() > 0) {
                    continue;
                }
                if (bean.getGamme() != null ? bean.getGamme().getId() > 0 : false) {
                    champ = new String[]{"gamme"};
                    val = new Object[]{new YvsProdGammeArticle(bean.getGamme().getId())};
                    nameQueri = "YvsProdOperationsGamme.findByGamme";
                    lop = dao.loadNameQueries(nameQueri, champ, val);
                    bean.setListOperationsOf(loadListOperationsOf(bean, lop));
                }

                bean.setSite(program.getSite());
                if (currentSession != null ? currentSession.getId() > 0 : false) {
                    bean.setDepotPf(new Depots(currentSession.getDepot().getId()));
                }
                YvsProdOrdreFabrication entity = saveNewOF(bean);
                if (entity != null ? entity.getId() > 0 : false) {
                    bean.setId(entity.getId());
                    count++;
                    if (autoriser("prod_launch_of")) {
                        changeStateOf(bean, entity, Constantes.STATUT_PROD_LANCE + "", false);
                    }
                }
            }
            if (count == programs.size()) {
                succes();
            }
            ids = -10000;
            if (close) {
                resetFicheProgram();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible!!!");
            getException("terminerProgram", ex);
        }
    }

    public void printProgram(String categorie) {
        String ids = null;
        if (programs != null) {
            for (OrdreFabrication program : programs) {
                ids = (ids != null) ? (ids + "," + program.getId()) : program.getId() + "";
            }
        }
        if (!asString(ids)) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("TITLE_RAPPORT", "PRODUCTION" + (asString(categorie) ? " DES " + giveNameCategorie(categorie) : ""));
        param.put("CATEGORIE", asString(categorie) ? categorie : null);
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR(true));
        param.put("IDS", ids);
        executeReport("program_production", param);
    }

    public void addComposantsOF(OrdreFabrication bean, List<YvsProdComposantOF> newList) {
        YvsProdComposantOF current;
        int idx;
        for (YvsProdComposantOF cof : newList) {
            idx = bean.getListComposantOf().indexOf(cof);
            if (idx >= 0) {
                bean.getListComposantOf().get(idx).setQuantitePrevu(cof.getQuantitePrevu());
            } else {
                bean.getListComposantOf().add(cof);
            }
        }
    }

    /**
     * Revision OF*
     */
    private List<YvsUsers> producteurs;
    private List<YvsGrhTrancheHoraire> tranches;
    private List<YvsProdSessionProd> sessionsProd;
    private List<YvsProdSuiviOperations> mesCycles;
    private YvsProdSuiviOperations selectedCycle;
    private boolean navNextCycle, navPrevCycle;

    public void chooseDepotSession(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                int idx = m.getDepots().indexOf(new YvsBaseDepots((long) ev.getNewValue()));
                if (idx > -1) {
                    YvsBaseDepots y = m.getDepots().get(idx);
                    sessionProd.setDepot(UtilCom.buildBeanDepot(y));
                    //Charge les tranches
                    tranches = dao.loadNameQueries("YvsComCreneauHoraireUsers.findTrancheByDepotActif", new String[]{"depot"}, new Object[]{y});
                } else {
                    sessionProd.setDepot(new Depots());
                }
            }
        }
    }

    public void choixEquipes(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            ManagedEquipeProduction w = (ManagedEquipeProduction) giveManagedBean(ManagedEquipeProduction.class
            );
            if (w
                    != null) {
                int idx = w.getEquipes().indexOf(new YvsProdEquipeProduction((long) ev.getNewValue()));
                if (idx > -1) {
                    YvsProdEquipeProduction y = w.getEquipes().get(idx);
                    sessionProd.setEquipe(UtilProd.buildBeanEquipeProduction(y));
                }
            }
        }
    }

    public void choixUsers(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            if (producteurs != null) {
                int idx = producteurs.indexOf(new YvsUsers((long) ev.getNewValue()));
                if (idx > -1) {
                    sessionProd.setProducteur(UtilUsers.buildBeanUsers(producteurs.get(idx)));
                }
            }
        }
    }

    public void choixTranche(ValueChangeEvent ev) {
        //charge les membre d'une equipes
        if (ev != null ? ev.getNewValue() != null : false) {
            long id = (long) ev.getNewValue();
            if (!tranches.isEmpty() && id > 0) {
                sessionProd.setTranche(UtilCom.buildBeanTrancheHoraire(tranches.get(tranches.indexOf(new YvsGrhTrancheHoraire(id)))));
            }
        }
    }

    public void initSessionProdWithParam() {
        YvsComCreneauHoraireUsers creno = (YvsComCreneauHoraireUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findByUsersPlan", new String[]{"users", "date"}, new Object[]{currentUser.getUsers(), new Date()});
        if (creno != null) {
            YvsProdSessionProd sess = null;
            if (creno.getEquipe() != null && creno.getCreneauDepot() != null) {
                //Vérifie la date du planing : 
                if (creno.getPermanent()) {
                    //Créée une session à la date du jour
                    sess = saveNewSessionProduction(null, new Date(), creno.getUsers(), creno.getCreneauDepot().getTranche(), creno.getCreneauDepot().getDepot(), creno.getEquipe());
                } else {
                    if (creno.getDateTravail().equals(yvs.dao.salaire.service.Constantes.giveOnlyDate(new Date()))) {
                        //créée une session à la date
                        sess = saveNewSessionProduction(null, new Date(), creno.getUsers(), creno.getCreneauDepot().getTranche(), creno.getCreneauDepot().getDepot(), creno.getEquipe());
                    } else {
                        if (creno.getDateTravail().equals(yvs.dao.salaire.service.Constantes.givePrevOrNextDate(new Date(), -1))
                                && creno.getCreneauDepot().getTranche().getHeureDebut().after(creno.getCreneauDepot().getTranche().getHeureFin())) {
                            //créée une session de production hier
                            sess = saveNewSessionProduction(null, creno.getDateTravail(), creno.getUsers(), creno.getCreneauDepot().getTranche(), creno.getCreneauDepot().getDepot(), creno.getEquipe());
                        }
                    }
                }
            }
            if (sess != null) {
                currentSession = sess;
            }
        }

    }

    public YvsProdSessionProd saveNewSessionProduction(Long id, Date date, YvsUsers producteur, YvsGrhTrancheHoraire tranche, YvsBaseDepots depot, YvsProdEquipeProduction equipe) {
        YvsProdSessionProd s = (YvsProdSessionProd) dao.loadOneByNameQueries("YvsProdSessionProd.findOne", new String[]{"producteur", "tranche", "depot", "date"}, new Object[]{producteur, tranche, depot, date});
        if (s == null) {
            if (id != null ? id > 0 : false) {
                s = new YvsProdSessionProd(id);
                s.setDateSave(sessionProd.getDateSave());
            } else {
                s = new YvsProdSessionProd();
                s.setId(null);
                s.setDateSave(new Date());
            }
            //controle
            if (tranche == null ? true : tranche.getId() <= 0) {
                getErrorMessage("Vous devez choisir une tranche...");
                return null;
            }
            if (producteur == null ? true : producteur.getId() <= 0) {
                getErrorMessage("Vous devez choisir un producteur...");
                return null;
            }
            if (depot == null ? true : depot.getId() <= 0) {
                getErrorMessage("Vous devez choisir un depot...");
                return null;
            }
            // Contrôle l'existence d'un planning...
            if (!autoriser("prod_create_other_session")) {
                Long c = (Long) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.countMyPlanning", new String[]{"users", "date", "tranche", "depot"}, new Object[]{producteur, date, tranche, depot});
                System.err.println(" --- Créneau existe true");
                if (c != null ? c <= 0 : true) {
                    getErrorMessage("Impossible de créer cette session ", "Aucun planning n'a été trouvé");
                    return null;
                }
            }
            s.setAuthor(currentUser);
            s.setDateUpdate(new Date());
            s.setDateSession(date);
            s.setDepot(depot);
            s.setEquipe(equipe);
            s.setProducteur(producteur);
            s.setTranche(tranche);
            s.setActif(true);
            if (id != null ? id > 0 : false) {

                dao.update(s);
                int idx = sessionsProd.indexOf(s);
                if (idx >= 0) {
                    sessionsProd.set(idx, s);
                }
            } else {

                s = (YvsProdSessionProd) dao.save1(s);
                sessionsProd.add(0, s);
            }
            cloneObject(sessionProd, UtilProd.copyBeanSessionProd(s));
            succes();
            return s;
        }
        return s;
    }

    public void resetViewSession(boolean reset) {
        resetFiche(sessionProd);
        sessionProd.setDateSession(new Date());
        sessionProd.setDepot(new Depots());
        sessionProd.setEquipe(new EquipeProduction());
        sessionProd.setProducteur(new Users());
        sessionProd.setTranche(new TrancheHoraire());
        //initialise les paramètres
        if (!reset) {
            YvsComCreneauHoraireUsers creno = (YvsComCreneauHoraireUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findByUsersPlan", new String[]{"users", "date"}, new Object[]{currentUser.getUsers(), new Date()});
            if (creno != null) {
                sessionProd.setEquipe(UtilProd.buildBeanEquipeProduction(creno.getEquipe()));
                sessionProd.setProducteur(UtilUsers.buildBeanUsers(creno.getUsers()));
                sessionProd.setDepot(UtilCom.buildSimpleBeanDepot(creno.getCreneauDepot().getDepot()));
                sessionProd.setDateSession(creno.getDateTravail());
            }
        }
        update("form_session_prod");
    }

    public void saveNewSessionProduction() {
        //Vérifie qu'une session n'existe pas avec ces donnée
        currentSession = saveNewSessionProduction(sessionProd.getId(), sessionProd.getDateSession(), UtilUsers.buildSimpleBeanUsers(sessionProd.getProducteur()), UtilCom.buildTrancheHoraire(sessionProd.getTranche(), currentUser), UtilProd.buildBeanDepot(sessionProd.getDepot()), UtilProd.buildEquipeProduction(sessionProd.getEquipe(), currentUser));
        if (currentSession != null) {
            initControleTransfertAndInventaire();
//            if (!ordre.getListOperationsOf().isEmpty() && ordre.getId() > 0) {
            currentSession.setOrdreF((YvsProdSessionOf) dao.loadOneByNameQueries("YvsProdSessionOf.findOne", new String[]{"ordre", "session"}, new Object[]{selectedOf, currentSession}));
//                Collections.sort(ordre.getListOperationsOf(), new YvsProdOperationsOF());
//                openFluxComposantOperation(ordre.getListOperationsOf().get(0));
//                openDialog("dlgEditFlux");
            update(":formulaire_update_edit_flux:header_detail_edit_flux");
            update(":formulaire_update_edit_flux:zone_gear_operation");
            update("panel-opt-session-prod");
            closeDialog("dlgInfoSession_");
//            }
        }
    }

    public void loadSessionProdOnView(SelectEvent ev) {
        if (ev != null) {
            currentSession = (YvsProdSessionProd) ev.getObject();
            initControleTransfertAndInventaire();
            if (selectedOf != null) {
                currentSession.setOrdreF((YvsProdSessionOf) dao.loadOneByNameQueries("YvsProdSessionOf.findOne", new String[]{"ordre", "session"}, new Object[]{selectedOf, currentSession}));
                declaration.setSessionOf(UtilProd.copyBeanSessionProdOf(currentSession.getOrdreF()));
            }
            update("blog_declaration");
            update("zone_gear_operation");
            update("panel-opt-session-prod");
            update("form_detail_edit_flux");
            update("table_suivi_edit_flux");
        }
    }

    private void initControleTransfertAndInventaire() {
        if (currentSession != null) {
            if (!controleInventaire(currentSession.getDepot().getId(), currentSession.getDateSession(), currentSession.getTranche().getId())) {
                controleInventaireOk = false;
            } else {
                controleInventaireOk = true;
            }
            //vérifie la validation des transferts
            if (currentSession.getDepot().getVerifyAppro()) {
                if (!verifyTransfer(currentSession.getDepot().getId(), currentSession.getDateSession())) {
                    controleTransferOk = false;
                } else {
                    controleTransferOk = true;
                }
            } else {
                controleTransferOk = true;
            }
        }
    }

    public void openSessionToUpdate(YvsProdSessionProd s) {
        if (s != null) {
            cloneObject(sessionProd, UtilProd.copyBeanSessionProd(s));
            if (s.getTranche() != null) {
                if (!tranches.contains(s.getTranche())) {
                    tranches.add(s.getTranche());
                }
            }
            ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (service != null) {
                if (!service.getDepots().contains(s.getDepot())) {
                    service.getDepots().add(s.getDepot());
                }
            }
            ManagedEquipeProduction service_ = (ManagedEquipeProduction) giveManagedBean(ManagedEquipeProduction.class);
            if (service_ != null) {
                if (!service_.getEquipes().contains(s.getEquipe())) {
                    service_.getEquipes().add(s.getEquipe());
                }
            }
            update("form_session_prod");
            update("btn_save_session_prod");
        }
    }

    public void loadCurrentOperation(YvsProdOperationsOF op, YvsProdSessionOf session) {
        if (session != null) {
            //Charge les cycles opératoire de l'opération (suivi_opérations)
            selectedOp.setSuiviOperations(dao.loadNameQueries("YvsProdSuiviOperations.findOne", new String[]{"operation", "session"}, new Object[]{op, session}));
        } else {
            selectedOp.setSuiviOperations(dao.loadNameQueries("YvsProdSuiviOperations.findByOperation", new String[]{"operation"}, new Object[]{op}));
        }
        if (selectedOp.getSuiviOperations() != null ? !selectedOp.getSuiviOperations().isEmpty() : false) {
            YvsProdSuiviOperations s = selectedOp.getSuiviOperations().get(0);
            copyBeanSuiviOperation(s);
//                loadMyCycleSession(session, op);                
        } else {
            copyBeanSuiviOperation(null);
        }
    }

    public void deleteSessionProd(YvsProdSessionProd s) {
        try {
            dao.delete(s);
            sessionsProd.remove(s);
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible !");
        }
    }

    public void toogleActivate(YvsProdSessionProd s) {
        try {
            s.setActif(!s.getActif());
            dao.update(s);
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible !");
        }
    }

    private void copyBeanSuiviOperation(YvsProdSuiviOperations s) {
        if (suiviOperation == null || s == null) {
            suiviOperation = new SuiviOperations();
        }
        if (s != null) {
            suiviOperation.setId(s.getId());
            suiviOperation.setCoutOperation(s.getCout());
            suiviOperation.setDate(s.getDateDebut());
            suiviOperation.setHeureDebut(s.getHeureDebut());
            suiviOperation.setHeureFin(s.getHeureFin());
            suiviOperation.setReference(s.getReferenceOp());
            suiviOperation.setStatut(s.getStatut());
        }
        update("panel_detail_ppte_cycle_op");
    }

    private YvsProdSuiviOperations copyEntitySuiviOperation() {
        YvsProdSuiviOperations s = new YvsProdSuiviOperations();
        if (suiviOperation != null ? Util.asLong(suiviOperation.getId()) : false) {
            s.setId(suiviOperation.getId());
            s.setDateDebut(suiviOperation.getDate());
            s.setHeureDebut(suiviOperation.getHeureDebut());
            s.setHeureFin(suiviOperation.getHeureFin());
            s.setReferenceOp(suiviOperation.getReference());
            s.setStatut(suiviOperation.getStatut());
        }
        return s;
    }

    public void openSuiviFluxOperation(YvsProdSuiviOperations op) {
        copyBeanSuiviOperation(op);
        openDialog("dlgConfirmDelSuivi");
    }

    public void displayFluxOneOperation(YvsProdSuiviOperations op) {
        if (op != null) {
            buildFluxStockOp(op.getOperationOf());
            copyBeanSuiviOperation(op);
//            suiviOperation.setComposants(dao.loadNameQueries("YvsProdOfSuiviFlux.findByOneOp", new String[]{"operation"}, new Object[]{op}));            
            openDialog("dlgEditFlux");
            update("zone_gear_operation form_detail_edit_flux table_suivi_edit_flux");
        }
    }

    public void loadMyCycleSession(YvsProdSessionOf session, YvsProdOperationsOF op) {
        if (!autoriser("prod_update_all_of")) {
            mesCycles = dao.loadNameQueries("YvsProdSuiviOperations.findByOperationSessionProducteur", new String[]{"session", "operation"}, new Object[]{session, op});
        } else {
            mesCycles = dao.loadNameQueries("YvsProdSuiviOperations.findByOperation", new String[]{"operation"}, new Object[]{op});
        }
        navigueInCycle(true);
        update("zone_gear_operation");
    }

    public void navigueInCycle(boolean next) {
        if (!mesCycles.isEmpty()) {
            navNextCycle = navPrevCycle = false;
            if (selectedCycle == null) {
                selectedCycle = mesCycles.get(0);
                navPrevCycle = true;
                navNextCycle = !(mesCycles.size() > 1);
                return;
            }
            int idx = mesCycles.indexOf(selectedCycle);
            idx = (next) ? idx + 1 : idx - 1;
            if (idx > 0) {
                navPrevCycle = false;
                navNextCycle = !(mesCycles.size() > idx);
                if (idx < mesCycles.size()) {
                    selectedCycle = mesCycles.get(idx);
                }
            } else {
                if (idx == 0) {
                    selectedCycle = mesCycles.get(0);
                }
                navPrevCycle = true;
                navNextCycle = !(mesCycles.size() > 1);
            }
        } else {
            navNextCycle = navPrevCycle = true;
        }
        if (selectedCycle != null) {
            copyBeanSuiviOperation(selectedCycle);
        }
    }

    public void loadMyCycleProducteur(YvsUsers users, YvsProdOperationsOF op) {
        mesCycles = dao.loadNameQueries("YvsProdSuiviOperations.findByOperationProducteur", new String[]{"producteur", "operation"}, new Object[]{users, op});
        System.err.println(" Cycles Users " + users);
    }

    public double giveQteCycleOp(YvsProdFluxComposant flux, long op) {
        Double re = (Double) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.findQteCycleOp", new String[]{"operation", "composant"}, new Object[]{new YvsProdSuiviOperations(op), flux});
        return (re != null ? re : 0);
    }

    public double giveQteFluxEncours(YvsProdFluxComposant flux) {
        Double re = (Double) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.findFluxComposant", new String[]{"composant"}, new Object[]{flux});
        return (re != null ? re : 0);
    }

    public void deleteSuiviOperation() {
        if (suiviOperation.getId() > 0) {
            YvsProdSuiviOperations op = copyEntitySuiviOperation();
            op.setDateUpadate(new Date());
            op.setAuthor(currentUser);
            dao.delete(op);
            if (operation.getOperations() != null) {
                operation.getOperations().remove(op);
                update("tbv_ppte_op:table_op_suivi_op");
            }
            suiviOperation = new SuiviOperations();
            majTauxEvolutionOf();
            update("form_detail_edit_flux");
            update("table_suivi_edit_flux");
        }
    }

    public void loadAllFluxComposantCo(YvsProdComposantOF co) {
        listeFluxComposant = dao.loadNameQueries("YvsProdOfSuiviFlux.findByComposantOF", new String[]{"composant"}, new Object[]{co});
    }

    public void openToDeleteOneLineFlux(YvsProdOfSuiviFlux co) {
        selectedSuiviFlux = co;
        openDialog("confirmDelSF");
    }

    public void deleteOneLineFlux(boolean delOp) {
        if (selectedSuiviFlux != null) {
            if (!delOp) {
                dao.delete(selectedSuiviFlux);
            } else {
                dao.delete(selectedSuiviFlux.getIdOperation());
            }
            listeFluxComposant.remove(selectedSuiviFlux);
            update("tbv_composant_of:table_flux_reel_composants");
        }
    }

    public void maintenanceOF(YvsProdOrdreFabrication oder) {
//        List<YvsProdDeclarationProduction> declarations = dao.loadNameQueries("YvsProdDeclarationProduction.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()}, 0, 10);
        String query = "DELETE FROM yvs_prod_suivi_operations WHERE id IN(SELECT s.id FROM yvs_prod_suivi_operations s INNER JOIN yvs_prod_operations_of op "
                + "ON op.id=s.operation_of WHERE op.ordre_fabrication=? )";
        if (oder != null) {
            dao.requeteLibre(query, new Options[]{new Options(oder.getId(), 1)});
        }
        oder.setDeclarations(dao.loadNameQueries("YvsProdDeclarationProduction.findByOF", new String[]{"ordre"}, new Object[]{oder}));
        int i = 0;
        Double totalDeclare = 0d;
        for (YvsProdDeclarationProduction d : oder.getDeclarations()) {
            buildAndSaveEntityFlux(dao.loadNameQueries("YvsProdOperationsOF.findByOF", new String[]{"ordre"}, new Object[]{oder}), d.getSessionOf(), d.getQuantite());

//            if (i == 0) {
//                //Modifie le coût de l'of 
            totalDeclare = totalDeclare + d.getQuantite();
//                oder.setCoutOf(valoriseOf(oder));
//                dao.update(oder);
//            }
//            if (totalDeclare > 0) {
//                d.setCoutProduction(oder.getCoutOf() / totalDeclare);
//                dao.update(d);
//            }
//            i++;
        }
        for (YvsProdDeclarationProduction d : oder.getDeclarations()) {
            if (i == 0) {
                //Modifie le coût de l'of                 
                oder.setCoutOf(valoriseOf(oder));
                dao.update(oder);
            }
            if (totalDeclare > 0) {
                d.setCoutProduction(oder.getCoutOf() / totalDeclare);
                dao.update(d);
            }
            i++;
        }
        succes();
    }

    private List<YvsProdOfSuiviFlux> buildAndSaveEntityFlux(List<YvsProdOperationsOF> listOp, YvsProdSessionOf session, double qte) {
        YvsProdOfSuiviFlux flux;
        YvsProdSuiviOperations soperation;
        for (YvsProdOperationsOF op : listOp) {
            soperation = new YvsProdSuiviOperations();
            soperation.setAuthor(session.getAuthor());
            soperation.setDateDebut(session.getSessionProd().getDateSession());
            soperation.setDateFin(session.getSessionProd().getDateSession());
            soperation.setDateSave(session.getDateSave());
            soperation.setDateUpadate(new Date());
            soperation.setHeureDebut(new Date());
            soperation.setHeureFin(new Date());
            soperation.setOperationOf(op);
            soperation.setReferenceOp(builNumeroSuiviOp(op));
            soperation.setSessionOf(session);
            soperation.setStatut(Constantes.ETAT_TERMINE);
            soperation = (YvsProdSuiviOperations) dao.save1(soperation);
            for (YvsProdFluxComposant fc : op.getComposants()) {
                flux = new YvsProdOfSuiviFlux();
                flux.setAuthor(session.getAuthor());
                flux.setCalculPr(true);
                flux.setComposant(fc);
                flux.setCout(findLastPr(fc.getComposant().getArticle().getId(), session.getSessionProd().getDepot().getId(), session.getSessionProd().getDateSession(), fc.getComposant().getUnite().getId()));
                flux.setDateSave(session.getDateSave());
                flux.setDateUpdate(new Date());
                flux.setIdOperation(soperation);
                flux.setQuantite(giveQuantite(findComposantNom(fc.getComposant().getArticle(), session.getOrdre().getNomenclature()), session.getOrdre().getNomenclature(), qte));
                flux.setQuantitePerdue(0d);
                dao.save(flux);
            }
        }
        return null;
    }

    private double giveQuantite(YvsProdComposantNomenclature com_, YvsProdNomenclature nom_, double quantiteOF) {
        if (com_ != null ? com_.getComposantLie() != null : false) {
            // récupère le composant             
            YvsProdComposantNomenclature c = (YvsProdComposantNomenclature) dao.loadOneByNameQueries("YvsProdComposantNomenclature.findById", new String[]{"id"}, new Object[]{com_.getComposantLie().getId()});
            if (c != null) {
                double d = giveQuantite(c, nom_, quantiteOF);
                return com_.getQuantite() * d / 100;
            }
        } else {
            if (com_ != null) {
                return (com_.getQuantite() * quantiteOF / nom_.getQuantite());
            }
        }
        return 0;
    }

    private YvsProdComposantNomenclature findComposantNom(YvsBaseArticles article, YvsProdNomenclature nom) {
        nom.setComposants(dao.loadNameQueries("YvsProdComposantNomenclature.findByNomenclature", new String[]{"nomenclature"},
                new Object[]{nom}));
        for (YvsProdComposantNomenclature c : nom.getComposants()) {
            if (c.getArticle().equals(article)) {
                return c;
            }
        }
        return null;
    }

    public void openDlgMaintenance(String wdgv) {
        openDialog(wdgv);
    }

    public void maintenanceAll() {
        List<Long> ids = decomposeIdSelection(chaineSelectOf);
        for (Long idx : ids) {
            if (idx >= 0) {
                selectedOf = listeOrdreF.get(idx.intValue());
                maintenanceOF(selectedOf);
            }
        }
//        for (YvsProdOrdreFabrication of : listeOrdreF) {
//            selectedOf = of;
//            maintenanceOF(of);
//        }
        succes();
    }

    public void recalculCoutOF() {
        List<Long> ids = decomposeIdSelection(chaineSelectOf);
        for (Long idx : ids) {
            if (idx >= 0) {
                recalculOrder(listeOrdreF.get(idx.intValue()));
            }
        }
        succes();
    }

    public void maintenanceOF() {
        List<Integer> indexs = decomposeSelection(chaineSelectOf);
        if (indexs != null ? !indexs.isEmpty() : false) {
            YvsProdOrdreFabrication of;
            for (Integer index : indexs) {
                of = listeOrdreF.get(index);
                selectedOf = of;
                maintenanceOF(of);
            }
            succes();
        }
    }

    public void _onloadDIstantDocStock() {
        ManagedTransfertStock service = (ManagedTransfertStock) giveManagedBean(ManagedTransfertStock.class);
        if (service != null) {
            service.setDestSearch(currentSession.getDepot().getId());
            service.setStatut_(Constantes.ETAT_VALIDE);
            service.chooseDestSearch(false);
            service.addParamStatut(true);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Transfert Stock", "modGescom", "smenTransfert", true);
            }
        }
    }

    public List<YvsProdComposantOF> getListeMatieres() {
        return listeMatieres;
    }

    public void setListeMatieres(List<YvsProdComposantOF> listeOrderSelect) {
        this.listeMatieres = listeMatieres;
    }

    public List<YvsProdOrdreFabrication> getListeOrdreSelect() {
        return listeOrdreSelect;
    }

    public void setListeOrdreSelect(List<YvsProdOrdreFabrication> listeOrdreSelect) {
        this.listeOrdreSelect = listeOrdreSelect;
    }

    public void getOrdres() {
        List<Integer> indexs = decomposeSelection(chaineSelectOf);
        if (indexs != null ? !indexs.isEmpty() : false) {
            YvsProdOrdreFabrication of;
            depots = dao.loadNameQueries("YvsBaseDepots.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            for (Integer index : indexs) {
                of = listeOrdreF.get(index);
                of.setComposants(dao.loadNameQueries("YvsProdComposantOF.findByOF", new String[]{"ordre"}, new Object[]{of}));
                listeOrdreSelect.add(of);
            }
            getMatiere();
            listeOrdreSelect.clear();

        }
    }

    public void getStock(YvsProdComposantOF com) {
        try {
            Double s = dao.stocks(com.getArticle().getId(), com.getIdDepot(), new Date());
            s = s != null ? s : 0.0;
            com.setStock(s);
            com.setQuantiteValide(com.getQuantitePrevu() - s);

        } catch (Exception e) {
            return;
        }
    }

    public boolean isOrdre_achat() {
        return ordre_achat;
    }

    public void setOrdre_achat(boolean ordre_achat) {
        this.ordre_achat = ordre_achat;
    }

    public void getExiste(YvsBaseArticles art, YvsBaseDepots dep) {
        try {
            YvsBaseArticleDepot articleDepot = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", new String[]{"article", "depot"}, new Object[]{art, dep});
            if (articleDepot != null ? articleDepot.getId() > 0 : false) {

            } else {
                getErrorMessage("L'article " + art.getDesignation() + " n'appartient pas au dépot " + dep.getDesignation());
            }
        } catch (Exception e) {
        }
    }

    public void getDepot(YvsProdComposantOF composant) {
        try {
            if (composant.getIdDepot() > 0) {
                YvsBaseDepots y = new YvsBaseDepots(composant.getIdDepot());
                int index = depots.indexOf(y);

                if (index > -1) {
                    composant.setDepotConso(depots.get(index));
                    int i = listeMatieres.indexOf(composant);
                    current_source = composant.getDepotConso();
                    source = true;
                    listeMatieres.set(i, composant);
                    getStock(composant);
                    getExiste(composant.getArticle(), composant.getDepotConso());
                }
            }

        } catch (Exception e) {
        }

    }

    public void getDepotCible(YvsProdComposantOF composant) {
        try {
            if (composant.getIdDepot_cible() > 0) {
                YvsBaseDepots y = new YvsBaseDepots(composant.getIdDepot_cible());
                int index = depots.indexOf(y);

                if (index > -1) {
                    composant.setDepot_cible(depots.get(index));
                    int i = listeMatieres.indexOf(composant);
                    current_cible = composant.getDepot_cible();
                    source = false;
                    listeMatieres.set(i, composant);
                    getStock(composant);
                    getExiste(composant.getArticle(), composant.getDepot_cible());
                }
            }

        } catch (Exception e) {
        }

    }

    public void getDepotAll() {
        try {
            for (YvsProdComposantOF o : listeMatieres) {
                if (source) {
                    o.setIdDepot(current_source.getId());
                    o.setDepotConso(current_source);
                    getStock(o);
                    getExiste(o.getArticle(), current_source);
                } else {
                    o.setIdDepot_cible(current_cible.getId());
                    o.setDepot_cible(current_cible);
                    getExiste(o.getArticle(), current_cible);
                }

            }
        } catch (Exception e) {
        }
    }

    public boolean isDemande_appro() {
        return demande_appro;
    }

    public void setDemande_appro(boolean demande_appro) {
        this.demande_appro = demande_appro;
    }

    public String getFournisseur(YvsProdComposantOF composant) {
        try {
            YvsBaseArticleFournisseur artFourn = (YvsBaseArticleFournisseur) dao.loadOneByNameQueries("YvsBaseArticleFournisseur.findByFournPrinc", new String[]{"article"}, new Object[]{composant.getArticle()});
            composant.setPrincipal(artFourn.getFournisseur());
            System.err.println("fournisseur = " + composant.getPrincipal().getNom_prenom());
            return artFourn.getFournisseur().getNom_prenom();
        } catch (Exception e) {
            return null;
        }
    }

    public void afficherFournisseur() {
        ordre_achat = true;
        demande_appro = false;
        demande_transfert = false;
        update("table_matiere");
    }

    public void afficherAppro() {
        demande_appro = true;
        demande_transfert = false;
        ordre_achat = false;
        update("table_matiere");
    }

    public void afficherTransfert() {
        demande_appro = false;
        demande_transfert = true;
        ordre_achat = false;
        update("table_matiere");
    }

    public void getMatiere() {
        YvsProdComposantOF exit = null;
        List<YvsProdComposantOF> list_composant = new ArrayList<>();
        for (YvsProdOrdreFabrication of : listeOrdreSelect) {
            list_composant.addAll(of.getComposants());
        }
        for (YvsProdComposantOF com : list_composant) {
            if (com.getDepotConso() != null ? com.getDepotConso().getId() < 1 : true) {
//                com.setDepotConso(depots.get(0));
            }
            exit = null;

            for (int i = 0; i < listeMatieres.size(); i++) {
                fournisseurs = dao.loadNameQueries("YvsBaseArticleFournisseur.findByArticle", new String[]{"article"}, new Object[]{listeMatieres.get(i).getArticle()});
                if (fournisseurs != null ? fournisseurs.size() == 1 : false) {
                    listeMatieres.get(i).setPrincipal(fournisseurs.get(0).getFournisseur());
                }
                if (!fournisseurs.isEmpty()) {
                    for (YvsBaseArticleFournisseur a : fournisseurs) {
                        if (a.getPrincipal()) {
                            listeMatieres.get(i).setPrincipal(a.getFournisseur());
                        }
                        System.err.println("fournisseur de  " + i + " =" + a.getFournisseur().getNom_prenom());
                        if (!listeMatieres.get(i).getFournis().contains(a.getFournisseur())) {
                            listeMatieres.get(i).getFournis().add(a.getFournisseur());
                        }

                    }
                }

                if (com.getUnite().equals(listeMatieres.get(i).getUnite())) {
                    exit = listeMatieres.get(i);
                    break;
                }

            }
            if (exit != null) {
                exit.setQuantitePrevu(com.getQuantitePrevu() + exit.getQuantitePrevu());
                int index = listeMatieres.indexOf(exit);
                if (index > -1) {

                    listeMatieres.set(index, exit);
                }
            } else {
                listeMatieres.add(com);
            }

        }

        update("table_matiere");

    }

    public void getFournPrincipal(YvsProdComposantOF composant) {
        try {
            System.err.println("selection fournisseur ");
            if (composant.getIdFournisseur() > 0) {
                YvsBaseFournisseur y = new YvsBaseFournisseur(composant.getIdFournisseur());
                int index = composant.getFournis().indexOf(y);
                System.err.println("index =" + index);
                if (index > -1) {
                    System.err.println("index ok");
                    composant.setPrincipal(composant.getFournis().get(index));
                    int i = listeMatieres.indexOf(composant);
                    listeMatieres.set(i, composant);

                }
            }

        } catch (Exception e) {
        }
    }

    public void enregistrerOrdre() {
        try {
            if (ordre_achat) {
                List<YvsComDocAchats> achats = new ArrayList<>();
                YvsComDocAchats facture = null;
                boolean succes;
                // enregistrement d'ordre d'achat
                for (YvsProdComposantOF com : listeMatieres) {
                    if (com.getPrincipal() != null ? com.getPrincipal().getId() > 0 : false) {
                        if (com.getQuantiteValide() > 0) {
                            if (com.getDepotConso() != null ? com.getDepotConso().getId() > 0 : false) {
                                facture = null;
                                for (int i = 0; i < achats.size(); i++) {
                                    if (achats.get(i).getFournisseur() != null ? achats.get(i).getFournisseur().getId() > 0 : false) {
                                        if (com.getPrincipal().getId() > 0 ? achats.get(i).getFournisseur().equals(com.getPrincipal()) : false) {
                                            facture = achats.get(i);
                                            break;
                                        }
                                    }

                                }
                                if (facture != null) {
                                    YvsComContenuDocAchat content = new YvsComContenuDocAchat();
                                    content.setArticle(com.getArticle());
                                    content.setQuantiteCommande(com.getQuantiteValide());
                                    content.setConditionnement(com.getUnite());
                                    content.setPrixAchat(com.getUnite().getPrixAchat());
                                    content.setPrixTotal(content.getQuantiteCommande() * com.getUnite().getPrixAchat());
                                    content.setStatut(Constantes.ETAT_EDITABLE);
                                    content.setQuantiteBonus(0d);
                                    content.setCalculPr(false);
                                    content.setActif(true);
                                    content.setAuthor(currentUser);
                                    content.setDateSave(new Date());
                                    content.setDateUpdate(new Date());
                                    facture.getContenus().add(content);
                                    int index = achats.indexOf(facture);
                                    achats.set(index, facture);

                                } else {
                                    facture = new YvsComDocAchats();
                                    YvsComContenuDocAchat content = new YvsComContenuDocAchat();
                                    content.setDateSave(new Date());
                                    content.setDateUpdate(new Date());
                                    content.setArticle(com.getArticle());
                                    content.setQuantiteCommande(com.getQuantiteValide());
                                    content.setConditionnement(com.getUnite());
                                    content.setPrixAchat(com.getUnite().getPrixAchat());
                                    content.setPrixTotal(content.getQuantiteCommande() * com.getUnite().getPrixAchat());
                                    content.setStatut(Constantes.ETAT_EDITABLE);
                                    content.setQuantiteBonus(0d);
                                    content.setCalculPr(false);
                                    content.setActif(true);
                                    content.setAuthor(currentUser);
                                    facture.getContenus().add(content);
                                    facture.setFournisseur(com.getPrincipal());
                                    facture.setDepotReception(com.getDepotConso());
                                    facture.setTypeDoc(Constantes.TYPE_FA);
                                    facture.setAgence(currentAgence);
                                    facture.setStatut(Constantes.ETAT_EDITABLE);
                                    facture.setStatutLivre(Constantes.ETAT_ATTENTE);
                                    facture.setStatutRegle(Constantes.ETAT_ATTENTE);
                                    facture.setDateDoc(new Date());
                                    facture.setAuthor(currentUser);
                                    facture.setCategorieComptable(com.getPrincipal().getCategorieComptable());
                                    facture.setDateSave(new Date());
                                    facture.setDateUpdate(new Date());
                                    facture.setActif(true);
                                    facture.setAutomatique(false);
                                    facture.setGenererFactureAuto(false);
                                    facture.setComptabilise(false);
                                    achats.add(facture);
                                }
                            }

                        }
                    }

                }
                for (YvsComDocAchats doc : achats) {
                    List<YvsComContenuDocAchat> conten = new ArrayList<>(doc.getContenus());
                    List<YvsWorkflowEtapeValidation> etapes = new ArrayList<>();
                    etapes = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", new String[]{"titre", "societe"}, new Object[]{Constantes.DOCUMENT_FACTURE_ACHAT, currentAgence.getSociete()});
                    doc.setEtapeTotal(etapes.size());

                    doc.setNumDoc(dao.genererReference(Constantes.TYPE_FA_NAME, new Date(), doc.getDepotReception().getId(), currentAgence.getSociete(), currentAgence));
                    doc.getContenus().clear();
                    doc = (YvsComDocAchats) dao.save1(doc);
                    for (YvsComContenuDocAchat con : conten) {
                        con.setDocAchat(doc);
                        con = (YvsComContenuDocAchat) dao.save1(con);

                    }

                    if (etapes != null ? !etapes.isEmpty() : false) {
                        doc.setEtapesValidations(saveEtapesValidation(doc, etapes));
                        for (YvsWorkflowValidFactureAchat etap : doc.getEtapesValidations()) {
                            etap.setEtape(new YvsWorkflowEtapeValidation(etap.getEtape().getId()));
                        }
                    }
                }

                getInfoMessage("Succès");

            } else if (demande_appro) {
                // enregistrement de la demande d'appro
                List<YvsComFicheApprovisionnement> appros = new ArrayList<>();
                YvsComFicheApprovisionnement bean = null;
                for (YvsProdComposantOF com : listeMatieres) {
                    if (com.getDepotConso() != null ? com.getDepotConso().getId() > 0 : false) {
                        if (com.getQuantiteValide() > 0) {
                            bean = null;
                            for (int i = 0; i < appros.size(); i++) {
                                if (appros.get(i).getDepot() != null ? appros.get(i).getDepot().getId() > 0 : false) {
                                    if (com.getDepotConso() != null ? com.getDepotConso().equals(appros.get(i).getDepot()) : false) {
                                        bean = appros.get(i);
                                        break;
                                    }
                                }
                            }
                            if (bean != null) {
                                YvsComArticleApprovisionnement content = new YvsComArticleApprovisionnement();
                                YvsBaseArticleDepot articleDepot = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticle", new String[]{"article"}, new Object[]{com.getArticle()});
                                articleDepot = articleDepot != null ? articleDepot : new YvsBaseArticleDepot();
                                content.setArticle(articleDepot);
                                content.setConditionnement(com.getUnite());
                                content.setDateSave(new Date());
                                content.setDateUpdate(new Date());
                                content.setQuantite(com.getQuantiteValide());
                                content.setAuthor(currentUser);
                                bean.setDepot(com.getDepotConso());
                                bean.getArticles().add(content);
                                int index = appros.indexOf(bean);
                                appros.set(index, bean);
                            } else {
                                bean = new YvsComFicheApprovisionnement();
                                YvsComArticleApprovisionnement content = new YvsComArticleApprovisionnement();
                                YvsBaseArticleDepot articleDepot = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticle", new String[]{"article"}, new Object[]{com.getArticle()});
                                articleDepot = articleDepot != null ? articleDepot : new YvsBaseArticleDepot();
                                content.setArticle(articleDepot);
                                content.setConditionnement(com.getUnite());
                                content.setDateSave(new Date());
                                content.setQuantite(com.getQuantiteValide());
                                content.setAuthor(currentUser);
                                bean.getArticles().add(content);
                                bean.setDepot(com.getDepotConso());
                                bean.setAuthor(currentUser);
                                bean.setDateSave(new Date());
                                bean.setDateUpdate(new Date());
                                bean.setHeureApprovisionnement(new Date());
                                bean.setEtat(Constantes.ETAT_EDITABLE);
                                bean.setStatutTerminer(Constantes.ETAT_ATTENTE);

                                appros.add(bean);

                            }
                        }
                    }
                }

                for (YvsComFicheApprovisionnement fiche : appros) {
                    List<YvsComArticleApprovisionnement> content = new ArrayList<>(fiche.getArticles());
                    List<YvsWorkflowEtapeValidation> etapes = new ArrayList<>();
                    etapes = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", new String[]{"titre", "societe"}, new Object[]{Constantes.DOCUMENT_APPROVISIONNEMENT, currentAgence.getSociete()});
                    fiche.setEtapeTotal(etapes.size());

                    fiche.getArticles().clear();
                    fiche.setDateApprovisionnement(new Date());
                    fiche.setReference(dao.genererReference(Constantes.TYPE_FiA_NAME, new Date(), fiche.getDepot().getId(), currentAgence.getSociete(), currentAgence));

                    fiche = (YvsComFicheApprovisionnement) dao.save1(fiche);
                    for (YvsComArticleApprovisionnement c : content) {
                        c.setFiche(fiche);
                        dao.save(c);
                    }

                    if (etapes != null ? !etapes.isEmpty() : false) {
                        fiche.setEtapesValidations(saveEtapesValidation(fiche, etapes));
                        for (YvsWorkflowValidApprovissionnement etap : fiche.getEtapesValidations()) {
                            etap.setEtape(new YvsWorkflowEtapeValidation(etap.getEtape().getId()));
                        }
                    }
                }

                getInfoMessage("Succès");

            } else if (demande_transfert) {
                // ordre de transfert
                List<YvsComDocStocks> list = new ArrayList<>();
                YvsComDocStocks bean = null;
                for (YvsProdComposantOF com : listeMatieres) {
                    if (com.getDepotConso() != null ? com.getDepotConso().getId() > 0 : false) {
                        if (com.getDepot_cible() != null ? com.getDepot_cible().getId() > 0 : false) {
                            if (com.getQuantiteValide() > 0) {
                                bean = null;
                                for (int i = 0; i < list.size(); i++) {
                                    if (com.getDepot_cible() != null ? com.getDepot_cible().equals(list.get(i).getSource()) : false) {
                                        bean = list.get(i);
                                        break;
                                    }
                                }

                                if (bean != null) {
                                    YvsComContenuDocStock content = new YvsComContenuDocStock();
                                    content.setArticle(com.getArticle());
                                    content.setActif(true);
                                    content.setAuthor(currentUser);
                                    content.setQuantite(com.getQuantiteValide());
                                    content.setPrixEntree(dao.getPr(com.getArticle().getId(), com.getDepotConso().getId(), 0L, new Date(), com.getUnite().getId()));
                                    content.setPrix(dao.getPr(com.getArticle().getId(), com.getDepot_cible().getId(), 0L, new Date(), com.getUnite().getId()));
                                    content.setDateContenu(new Date());
                                    content.setDateSave(new Date());
                                    content.setDateUpdate(new Date());
                                    content.setStatut(Constantes.ETAT_EDITABLE);
                                    content.setConditionnement(com.getUnite());
                                    content.setConditionnementEntree(com.getUnite());
                                    content.setQuantiteEntree(com.getQuantiteValide());
                                    content.setCalculPr(true);
                                    content.setDateReception(new Date());

                                    bean.getContenus().add(content);
                                    int index = list.indexOf(bean);
                                    list.set(index, bean);
                                } else {
                                    bean = new YvsComDocStocks();
                                    bean.setDateDoc(new Date());
                                    bean.setStatut(Constantes.ETAT_EDITABLE);
                                    bean.setTypeDoc(Constantes.TYPE_FT);
                                    bean.setDestination(com.getDepotConso());
                                    bean.setSource(com.getDepot_cible());
                                    bean.setActif(true);
                                    bean.setDateSave(new Date());
                                    bean.setHeureDoc(new Date());
                                    bean.setSociete(currentAgence.getSociete());
                                    bean.setAuthor(currentUser);
                                    bean.setNature(Constantes.TYPE_FT_NAME);
                                    bean.setEtapeTotal(0);
                                    bean.setEtapeValide(0);
                                    bean.setEditeur(currentUser.getUsers());
                                    YvsComContenuDocStock content = new YvsComContenuDocStock();
                                    content.setArticle(com.getArticle());
                                    content.setActif(true);
                                    content.setAuthor(currentUser);
                                    content.setQuantite(com.getQuantiteValide());
                                    content.setPrixEntree(dao.getPr(com.getArticle().getId(), com.getDepotConso().getId(), 0L, new Date(), com.getUnite().getId()));
                                    content.setPrix(dao.getPr(com.getArticle().getId(), com.getDepot_cible().getId(), 0L, new Date(), com.getUnite().getId()));
                                    content.setDateContenu(new Date());
                                    content.setDateSave(new Date());
                                    content.setDateUpdate(new Date());
                                    content.setStatut(Constantes.ETAT_EDITABLE);
                                    content.setConditionnement(com.getUnite());
                                    content.setConditionnementEntree(com.getUnite());
                                    content.setQuantiteEntree(com.getQuantiteValide());
                                    content.setCalculPr(true);
                                    content.setDateReception(new Date());

                                    bean.getContenus().add(content);
                                    list.add(bean);

                                }
                            }
                        }
                    }
                }

                for (YvsComDocStocks doc : list) {
                    List<YvsComContenuDocStock> content = new ArrayList<>(doc.getContenus());

                    doc.getContenus().clear();
                    doc.setNumDoc(dao.genererReference(Constantes.TYPE_FT_NAME, new Date(), doc.getDestination().getId(), currentAgence.getSociete(), currentAgence));

                    doc = (YvsComDocStocks) dao.save1(doc);
                    for (YvsComContenuDocStock c : content) {
                        c.setDocStock(doc);
                        dao.save(c);
                    }

                }
                getInfoMessage("Succès");

            } else {
                getErrorMessage("Veuillez selectionner une action !");
            }
        } catch (Exception e) {
            System.err.println("erreur = " + e.getMessage());
            e.printStackTrace();
        }
    }

    public YvsBaseDepots getCurrent_cible() {
        return current_cible;
    }

    public void setCurrent_cible(YvsBaseDepots current_cible) {
        this.current_cible = current_cible;
    }

    public YvsBaseDepots getCurrent_source() {
        return current_source;
    }

    public void setCurrent_source(YvsBaseDepots current_source) {
        this.current_source = current_source;
    }

    public boolean isSource() {
        return source;
    }

    public void setSource(boolean source) {
        this.source = source;
    }

    public List<YvsWorkflowValidFactureAchat> saveEtapesValidation(YvsComDocAchats m, List<YvsWorkflowEtapeValidation> model) {
        //charge les étape de vailidation
        List<YvsWorkflowValidFactureAchat> re = new ArrayList<>();
        if (!model.isEmpty()) {
            YvsWorkflowValidFactureAchat vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    String[] champ = new String[]{"facture", "etape"};
                    Object[] val = new Object[]{m, et};
                    YvsWorkflowValidFactureAchat w = (YvsWorkflowValidFactureAchat) dao.loadOneByNameQueries("YvsWorkflowValidFactureAchat.findByEtapeFacture", champ, val);
                    if (w != null ? w.getId() < 1 : true) {
                        vm = new YvsWorkflowValidFactureAchat();
                        vm.setAuthor(new YvsUsersAgence(m.getAuthor().getId()));
                        vm.setEtape(et);
                        vm.setEtapeValid(false);
                        vm.setFactureAchat(new YvsComDocAchats(m.getId()));
                        vm.setDateSave(new Date());
                        vm.setId(null);
                        vm = (YvsWorkflowValidFactureAchat) dao.save1(vm);
                        re.add(vm);
                    }
                }
            }
        }
        return ordonneEtapes(re);
    }

    public List<YvsWorkflowValidFactureAchat> ordonneEtapes(List<YvsWorkflowValidFactureAchat> l) {
        return YvsWorkflowValidFactureAchat.ordonneEtapes(l);
    }

    private List<YvsWorkflowValidApprovissionnement> saveEtapesValidation(YvsComFicheApprovisionnement m, List<YvsWorkflowEtapeValidation> model) {
        //charge les étape de vailidation
        List<YvsWorkflowValidApprovissionnement> re = new ArrayList<>();
        if (!model.isEmpty()) {
            YvsWorkflowValidApprovissionnement vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    champ = new String[]{"facture", "etape"};
                    val = new Object[]{m, et};
                    YvsWorkflowValidApprovissionnement w = (YvsWorkflowValidApprovissionnement) dao.loadOneByNameQueries("YvsWorkflowValidApprovissionnement.findByEtapeFacture", champ, val);
                    if (w != null ? w.getId() < 1 : true) {
                        vm = new YvsWorkflowValidApprovissionnement();
                        vm.setAuthor(currentUser);
                        vm.setEtape(et);
                        vm.setEtapeValid(false);
                        vm.setDocument(m);
                        vm.setOrdreEtape(et.getOrdreEtape());
                        vm = (YvsWorkflowValidApprovissionnement) dao.save1(vm);
                        re.add(vm);
                    }
                }
            }
        }
        return ordonneEtapes_appro(re);
    }

    private List<YvsWorkflowValidApprovissionnement> ordonneEtapes_appro(List<YvsWorkflowValidApprovissionnement> l) {
        return YvsWorkflowValidApprovissionnement.ordonneEtapes(l);
    }

    private List<YvsProdFluxComposant> composants = new ArrayList<YvsProdFluxComposant>();

    public List<YvsProdFluxComposant> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsProdFluxComposant> composants) {
        this.composants = composants;
    }

    //Charge les composant d'une nomenclature lié à une opération.
    public void loadAllComposantNom(YvsProdOperationsOF op, YvsProdOrdreFabrication ordre) {
        //récupère à partir de la référence de l(opération
        List<YvsProdComposantOp> l = dao.loadNameQueries("YvsProdComposantOp.findByRefOperation", new String[]{"gamme", "operation"}, new Object[]{ordre.getGamme(), op.getCodeRef()});
        YvsProdFluxComposant flux;
        YvsProdComposantOF co;
        composants.clear();
        long i = -100;
        for (YvsProdComposantOp c : l) {
            flux = new YvsProdFluxComposant(i++);
            co = new YvsProdComposantOF(c.getComposant().getArticle(), c.getComposant().getUnite());
            flux.setComposant(co);
            flux.setOperation(op);
            flux.setQuantite(c.getQuantite());
            flux.setInListByDefault(incomposant(c.getComposant().getArticle()));
            flux.setSens(c.getSens());
            composants.add(flux);
        }
        update("tab_composant_alternatif");
    }

    public void openToAddNewComposant(boolean replace) {
        replaceComposant = replace;
        loadAllComposantNom(selectedOp, selectedOf);
        openDialog("dlgAlterComp");
    }

    private boolean incomposant(YvsBaseArticles a) {
        for (YvsProdComposantOF c : ordre.getListComposantOf()) {
            if (c.getArticle().equals(a)) {
                return true;
            }
        }
        return false;
    }

    public void addNewComposantInOF(SelectEvent ev) {
        if (ev != null) {
            YvsProdFluxComposant y = ((YvsProdFluxComposant) ev.getObject());
            if (!y.getOperation().getStatutOp().equals(Constantes.ETAT_TERMINE)) {
                if (replaceComposant && Oldflux != null) {
                    dao.delete(Oldflux);
                    if (operation != null) {
                        operation.getComposants().remove(Oldflux);
                        update("tbv_ppte_op:table_op_use_mp");
                        update("tablePhase_:table_composantOF");
                    }
                    ordre.getListComposantOf().remove(Oldflux.getComposant());
                }
                if (!containComposant(operation.getComposants(), y)) {
                    saveNewComposant(y, selectedOf);
                    update("tbv_ppte_op:table_op_use_mp");
                    update("tablePhase_:table_composantOF");
                    closeDialog("dlgAlterComp");
                    succes();
                } else {
                    getErrorMessage("Le composant est déjà utilisé à cette phase !");
                }
            } else {
                getErrorMessage("L'opération est déjà terminé !");
            }
        }
    }

    private boolean containComposant(List<YvsProdFluxComposant> composants, YvsProdFluxComposant composant) {
        for (YvsProdFluxComposant f : composants) {
            if (f.getComposant().getArticle().equals(composant.getComposant().getArticle()) && f.getSens().equals(composant.getSens())) {
                return true;
            }
        }
        return false;
    }

    public void saveNewComposant(YvsProdFluxComposant flux, YvsProdOrdreFabrication of) {
        //l'ajout du nouveau composant se fait par création 
        if (flux != null) {
            //trouve la quantité prévu
            if (ordre.canEditable(autoriser("prod_update_of_encours"))) {
                //récupère le composant nomenclature
                YvsProdComposantNomenclature c = (YvsProdComposantNomenclature) dao.loadOneByNameQueries("YvsProdComposantNomenclature.findComposantArticleByNom", new String[]{"article", "nomenclature"}, new Object[]{flux.getComposant().getArticle(), of.getNomenclature()});
                YvsProdComposantOp cop = (YvsProdComposantOp) dao.loadOneByNameQueries("YvsProdComposantOp.findByRefOperationAndComp",
                        new String[]{"operation", "composant"}, new Object[]{flux.getOperation().getCodeRef(), flux.getComposant().getArticle()});
                if (c != null) {
                    YvsProdComposantOF cof = calculBesoins(c, of.getNomenclature(), ordre.getListComposantOf(), ordre, true);
                    if (cof != null) {
                        cof.setAuthor(currentUser);
                        cof.setCoefficient(0d);
                        cof.setDateSave(new Date());
                        cof.setDateUpdate(new Date());
                        cof.setModeArrondi(c.getModeArrondi());
                        cof.setOrdreFabrication(of);
                        cof.setOrdre(c.getOrdre());
                        cof.setType(c.getType());
                        cof.setUnite(c.getUnite());
                        cof.setInsideCout(c.getInsideCout());
                        cof.setId(null);
                        cof = (YvsProdComposantOF) dao.save1(cof);
                        flux.setQuantite(cof.getQuantitePrevu());
                        flux.setComposant(cof);
                        ordre.getListComposantOf().add(cof);
                    } else {
                    }

                }
                flux.setAuthor(currentUser);
                flux.setCoeficientValeur(0d);
                flux.setDateSave(new Date());
                flux.setDateUpdate(new Date());
                if (cop != null) {
                    flux.setTypeCout(cop.getTypeCout());
                    flux.setSens(cop.getSens());
                    flux.setMargeQte(cop.getMargeQte());
                    flux.setQuantitePerdue(flux.getQuantite() * cop.getTauxPerte() / 100);
                }
                flux.setId(null);
                flux = (YvsProdFluxComposant) dao.save1(flux);
                operation.getComposants().add(flux);
            } else {
                getErrorMessage("Vous ne pouvez modifier un Ordre déjà lancé !");
            }
        }
    }

    public void removeComposant(YvsProdFluxComposant flux) {
        //s'assurer que le composant n'est pas encore utilisé
        Double qte = (Double) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.findFluxOneComposant", new String[]{"composant"}, new Object[]{flux});
        if (qte != null ? qte > 0 : false) {
            getErrorMessage("Ce composant a dejà été utilisé, vous ne pouvez le supprimer de cet ordre");
            return;
        }
        dao.delete(flux);
        dao.delete(flux.getComposant());
        if (operation != null) {
            operation.getComposants().remove(flux);
            update("tbv_ppte_op:table_op_use_mp");
            update("tablePhase_:table_composantOF");
        }
        succes();
        ordre.getListComposantOf().remove(flux.getComposant());
    }

    private boolean replaceComposant;
    private YvsProdFluxComposant Oldflux;

    public void replaceComposant(YvsProdFluxComposant flux) {
        //s'assurer que le composant n'est pas encore utilisé
        Double qte = (Double) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.findFluxOneComposant", new String[]{"composant"}, new Object[]{flux});
        if (qte != null ? qte > 0 : false) {
            getErrorMessage("Ce composant a dejà fait l'objet d'une utilisation Vous ne pouvez le supprimer de cet OF");
            return;
        }
        Oldflux = flux;
        openToAddNewComposant(true);
    }

    //
    public void displayComposition(YvsProdOrdreFabrication of) {
        if (of != null) {
            of.setComposants(dao.loadNameQueries("YvsProdComposantOF.findByOF", new String[]{"ordre"}, new Object[]{of}));
            update("tableOF");
        }
    }

    public boolean isLastOperation(YvsProdOperationsOF y) {
        int index = ordre.getListOperationsOf().indexOf(y);
        if (index > -1) {
            return index == ordre.getListOperationsOf().size() - 1;
        }
        return false;
    }

    public void openToRecond(ComposantsOf composant, YvsBaseConditionnement uniteScr) {
        // depot
        ManagedReconditionnement service = (ManagedReconditionnement) giveManagedBean(ManagedReconditionnement.class);
        if (service != null) {
            service.resetFiche();
            service.getContenu().setArticle(composant.getComposant());
            service.getContenu().setCalculPr(true);
            service.getContenu().setConditionnement(UtilProd.buildBeanConditionnement(uniteScr));
            service.getContenu().setUniteDestination(composant.getUnite());
            service.getContenu().setResultante(composant.getQuantitePrevu());
            YvsBaseTableConversion tc = (YvsBaseTableConversion) dao.loadOneByNameQueries("YvsBaseTableConversion.findUniteCorrespondance", new String[]{"unite", "uniteE"},
                    new Object[]{uniteScr.getUnite(), new YvsBaseUniteMesure(service.getContenu().getConditionnement().getUnite().getId())});
            double taux = (tc != null) ? tc.getTauxChange() : 0;
            service.getContenu().setQuantite(composant.getQuantitePrevu() * taux);
            openDialog("dlgRecondQte");
            update("form-recond-qte");
        }
    }

    public void applyReconditionnement() {
        ManagedReconditionnement service = (ManagedReconditionnement) giveManagedBean(ManagedReconditionnement.class);
        if (service != null) {
            //construire le doc de recond
            service.getDocStock().setDateDoc(currentSession.getDateSession());
            service.getDocStock().setCreneauSource(UtilCom.buildBeanCreneau((YvsComCreneauDepot) dao.loadOneByNameQueries("YvsComCreneauDepot.findByTrancheDepot", new String[]{"tranche", "depot"}, new Object[]{currentSession.getTranche(), currentSession.getDepot()})));
            service.getDocStock().setSource(UtilCom.buildSimpleBeanDepot(currentSession.getDepot()));
            service.saveNewContenu();
        }
    }

    public void downloadProdDeclare() {
        if (dateDebut != null && dateFin != null) {
            try {
                Map<String, Object> param = new HashMap<>();
                param.put("AGENCE", currentAgence.getId().intValue());
                param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("DATE_DEBUT", dateDebut);
                param.put("DATE_FIN", dateFin);
                param.put("CATEGORIE", null);
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", SUBREPORT_DIR());
                executeReport("declaration", param);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ManagedTransfertStock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
