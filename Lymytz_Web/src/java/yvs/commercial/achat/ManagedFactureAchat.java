/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.ManagedCentreAnalytique;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.UtilCompta;
import yvs.comptabilite.caisse.ManagedReglementAchat;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.ManagedArticles;
import yvs.production.UtilProd;
import yvs.base.tiers.Tiers;
import yvs.commercial.ManagedCatCompt;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.ManagedTaxes;
import yvs.commercial.UtilCom;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.commercial.param.ManagedTypeDocDivers;
import yvs.commercial.param.TypeDocDivers;
import yvs.commercial.stock.CoutSupDoc;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.analytique.CentreContenuAchat;
import yvs.comptabilite.caisse.Caisses;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleAnalytique;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleCategorieComptableTaxe;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepartement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseTypeDocCategorie;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComParametreAchat;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComCoutSupDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.achat.YvsComTaxeContenuAchat;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaPhasePiece;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.analytique.YvsComptaCentreContenuAchat;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaJustifBonAchat;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.proj.YvsProjDepartement;
import yvs.entity.proj.projet.YvsProjProjet;
import yvs.entity.proj.projet.YvsProjProjetContenuDocAchat;
import yvs.entity.proj.projet.YvsProjProjetService;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ManagedTypeCout;
import yvs.grh.bean.TypeCout;
import yvs.grh.presence.TrancheHoraire;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;
import yvs.util.enume.Nombre;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedFactureAchat extends ManagedCommercial<DocAchat, YvsComDocAchats> implements Serializable {

    private DocAchat docAchat = new DocAchat(), bon = new DocAchat();
    private List<YvsComDocAchats> documents;
    private YvsComDocAchats selectDoc, docLie;
//    public boolean date_up;

    private YvsComParametreAchat currentParamAchat;

    private List<YvsComContenuDocAchat> contenusBon, all_contenus, contenusRequireLot, selectContenus;
    private YvsComContenuDocAchat selectContenu;
    private ContenuDocAchat contenu = new ContenuDocAchat();
    private ContenuDocAchat bonus = new ContenuDocAchat();
    public PaginatorResult<YvsComContenuDocAchat> p_contenu = new PaginatorResult<>();
    private long projet;

    private CentreContenuAchat analytique = new CentreContenuAchat();
    private YvsComptaCentreContenuAchat selectAnalytique = new YvsComptaCentreContenuAchat();

    private CoutSupDoc cout = new CoutSupDoc();
    private YvsComCoutSupDocAchat selectCout;
    private YvsComTaxeContenuAchat selectedTaxe;
    private List<YvsBaseTaxes> taxes;
    private List<YvsGrhTrancheHoraire> tranches;
    private String notes;

    private YvsComptaCaissePieceAchat selectReglement;
    private PieceTresorerie reglement = new PieceTresorerie();

    private String tabIds, tabIds_article, tabIds_article_fa, tabIds_cout, tabIds_type, tabIds_mensualite;
    private boolean selectArt, isBon, listArt, newMensualite, afficheRecu, deleteAll = true;

    private double montant_remise, taux_remise, quantite_recu;
    private String commentaire, numSerie, statutLivraison, egaliteStatut = "!=", egaliteStatutL = "=", egaliteStatutR = "=";
    private Date dateLivraison = new Date();

    private String motifEtape;
    YvsWorkflowValidFactureAchat etape;
    private boolean lastEtape, memoriserDeleteContenu;

    // Nombre d'element a afficher dans le selectOneMenu
    private int subLenght;
    private boolean _first = true;
    private Boolean comptaSearch;
    private long nbrComptaSearch;

    //Parametre recherche contenu
    private long agenceContenu = -1;
    private boolean dateContenu = false, addPrix, toValideLoad = true;
    private Date dateDebutContenu = new Date(), dateFinContenu = new Date();
    private String statutContenu, reference, article, articleContenu, depot, fournisseurF, comparer = ">=";
    private Long condArticle, agenceSearch;
    private List<YvsBaseUniteMesure> lconditionnements;
    private double prixSearch, prix2Search;
    private List<String> statuts;
    private DataTable table;

    public ManagedFactureAchat() {
        documents = new ArrayList<>();
        tranches = new ArrayList<>();
        all_contenus = new ArrayList<>();
        selectContenus = new ArrayList<>();
        statuts = new ArrayList<>();
        taxes = new ArrayList<>();
        lconditionnements = new ArrayList<>();
        contenusRequireLot = new ArrayList<>();
    }

    public Long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(Long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public boolean isMemoriserDeleteContenu() {
        return memoriserDeleteContenu;
    }

    public void setMemoriserDeleteContenu(boolean memoriserDeleteContenu) {
        this.memoriserDeleteContenu = memoriserDeleteContenu;
    }

    public List<YvsComContenuDocAchat> getSelectContenus() {
        return selectContenus;
    }

    public void setSelectContenus(List<YvsComContenuDocAchat> selectContenus) {
        this.selectContenus = selectContenus;
    }

    public DataTable getTable() {
        return table;
    }

    public void setTable(DataTable table) {
        this.table = table;
    }

    public long getAgenceContenu() {
        return agenceContenu;
    }

    public void setAgenceContenu(long agenceContenu) {
        this.agenceContenu = agenceContenu;
    }

    public List<YvsComContenuDocAchat> getContenusRequireLot() {
        return contenusRequireLot;
    }

    public void setContenusRequireLot(List<YvsComContenuDocAchat> contenusRequireLot) {
        this.contenusRequireLot = contenusRequireLot;
    }

    public boolean isDeleteAll() {
        return deleteAll;
    }

    public void setDeleteAll(boolean deleteAll) {
        this.deleteAll = deleteAll;
    }

    public long getProjet() {
        return projet;
    }

    public void setProjet(long projet) {
        this.projet = projet;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<YvsBaseTaxes> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsBaseTaxes> taxes) {
        this.taxes = taxes;
    }

    public long getNbrComptaSearch() {
        return nbrComptaSearch;
    }

    public void setNbrComptaSearch(long nbrComptaSearch) {
        this.nbrComptaSearch = nbrComptaSearch;
    }

    public CentreContenuAchat getAnalytique() {
        return analytique;
    }

    public void setAnalytique(CentreContenuAchat analytique) {
        this.analytique = analytique;
    }

    public YvsComptaCentreContenuAchat getSelectAnalytique() {
        return selectAnalytique;
    }

    public void setSelectAnalytique(YvsComptaCentreContenuAchat selectAnalytique) {
        this.selectAnalytique = selectAnalytique;
    }

    public YvsComDocAchats getDocLie() {
        return docLie;
    }

    public void setDocLie(YvsComDocAchats docLie) {
        this.docLie = docLie;
    }

    public String getArticleContenu() {
        return articleContenu;
    }

    public void setArticleContenu(String articleContenu) {
        this.articleContenu = articleContenu;
    }

    public YvsComParametreAchat getCurrentParamAchat() {
        return currentParamAchat;
    }

    public void setCurrentParamAchat(YvsComParametreAchat currentParamAchat) {
        this.currentParamAchat = currentParamAchat;
    }

    public Boolean getComptaSearch() {
        return comptaSearch;
    }

    public void setComptaSearch(Boolean comptaSearch) {
        this.comptaSearch = comptaSearch;
    }

    public String getMotifEtape() {
        return motifEtape;
    }

    public void setMotifEtape(String motifEtape) {
        this.motifEtape = motifEtape;
    }

    public boolean isLastEtape() {
        return lastEtape;
    }

    public void setLastEtape(boolean lastEtape) {
        this.lastEtape = lastEtape;
    }

    public List<String> getStatuts() {
        return statuts;
    }

    public void setStatuts(List<String> statuts) {
        this.statuts = statuts;
    }

    public boolean isToValideLoad() {
        return toValideLoad;
    }

    public void setToValideLoad(boolean toValideLoad) {
        this.toValideLoad = toValideLoad;
    }

    public String getEgaliteStatutL() {
        return egaliteStatutL;
    }

    public void setEgaliteStatutL(String egaliteStatutL) {
        this.egaliteStatutL = egaliteStatutL;
    }

    public String getEgaliteStatutR() {
        return egaliteStatutR;
    }

    public void setEgaliteStatutR(String egaliteStatutR) {
        this.egaliteStatutR = egaliteStatutR;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public boolean isAddPrix() {
        return addPrix;
    }

    public void setAddPrix(boolean addPrix) {
        this.addPrix = addPrix;
    }

    public String getComparer() {
        return comparer != null ? comparer.trim().length() > 0 ? comparer : ">=" : ">=";
    }

    public void setComparer(String comparer) {
        this.comparer = comparer;
    }

    public double getPrixSearch() {
        return prixSearch;
    }

    public void setPrixSearch(double prixSearch) {
        this.prixSearch = prixSearch;
    }

    public double getPrix2Search() {
        return prix2Search;
    }

    public void setPrix2Search(double prix2Search) {
        this.prix2Search = prix2Search;
    }

    public boolean isAfficheRecu() {
        return afficheRecu;
    }

    public void setAfficheRecu(boolean afficheRecu) {
        this.afficheRecu = afficheRecu;
    }

    public List<YvsComContenuDocAchat> getAll_contenus() {
        return all_contenus;
    }

    public void setAll_contenus(List<YvsComContenuDocAchat> all_contenus) {
        this.all_contenus = all_contenus;
    }

    public PaginatorResult<YvsComContenuDocAchat> getP_contenu() {
        return p_contenu;
    }

    public void setP_contenu(PaginatorResult<YvsComContenuDocAchat> p_contenu) {
        this.p_contenu = p_contenu;
    }

    public boolean isFirst() {
        return _first;
    }

    public void setFirst(boolean _first) {
        this._first = _first;
    }

    public boolean isDateContenu() {
        return dateContenu;
    }

    public void setDateContenu(boolean dateContenu) {
        this.dateContenu = dateContenu;
    }

    public Date getDateDebutContenu() {
        return dateDebutContenu;
    }

    public void setDateDebutContenu(Date dateDebutContenu) {
        this.dateDebutContenu = dateDebutContenu;
    }

    public Date getDateFinContenu() {
        return dateFinContenu;
    }

    public void setDateFinContenu(Date dateFinContenu) {
        this.dateFinContenu = dateFinContenu;
    }

    public String getStatutContenu() {
        return statutContenu;
    }

    public void setStatutContenu(String statutContenu) {
        this.statutContenu = statutContenu;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Long getCondArticle() {
        return condArticle;
    }

    public void setCondArticle(Long condArticle) {
        this.condArticle = condArticle;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getFournisseurF() {
        return fournisseurF;
    }

    public void setFournisseurF(String fournisseurF) {
        this.fournisseurF = fournisseurF;
    }

    public boolean isIsBonus() {
        return isBonus;
    }

    public void setIsBonus(boolean isBonus) {
        this.isBonus = isBonus;
    }

    public String getStatutLivraison() {
        return statutLivraison;
    }

    public void setStatutLivraison(String statutLivraison) {
        this.statutLivraison = statutLivraison;
    }

    public double getQuantite_recu() {
        return quantite_recu;
    }

    public void setQuantite_recu(double quantite_recu) {
        this.quantite_recu = quantite_recu;
    }

    public ContenuDocAchat getBonus() {
        return bonus;
    }

    public void setBonus(ContenuDocAchat bonus) {
        this.bonus = bonus;
    }

    public YvsWorkflowValidFactureAchat getCurrentEtape() {
        return currentEtape;
    }

    public void setCurrentEtape(YvsWorkflowValidFactureAchat currentEtape) {
        this.currentEtape = currentEtape;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public List<YvsGrhTrancheHoraire> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsGrhTrancheHoraire> tranches) {
        this.tranches = tranches;
    }

    public boolean isNewMensualite() {
        return newMensualite;
    }

    public void setNewMensualite(boolean newMensualite) {
        this.newMensualite = newMensualite;
    }

    public String getTabIds_mensualite() {
        return tabIds_mensualite;
    }

    public void setTabIds_mensualite(String tabIds_mensualite) {
        this.tabIds_mensualite = tabIds_mensualite;
    }

    public String getTabIds_type() {
        return tabIds_type;
    }

    public void setTabIds_type(String tabIds_type) {
        this.tabIds_type = tabIds_type;
    }

    public double getMontant_remise() {
        return montant_remise;
    }

    public void setMontant_remise(double montant_remise) {
        this.montant_remise = montant_remise;
    }

    public double getTaux_remise() {
        return taux_remise;
    }

    public void setTaux_remise(double taux_remise) {
        this.taux_remise = taux_remise;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public int getSubLenght() {
        return subLenght;
    }

    public void setSubLenght(int subLenght) {
        this.subLenght = subLenght;
    }

    public CoutSupDoc getCout() {
        return cout;
    }

    public void setCout(CoutSupDoc cout) {
        this.cout = cout;
    }

    public YvsComCoutSupDocAchat getSelectCout() {
        return selectCout;
    }

    public void setSelectCout(YvsComCoutSupDocAchat selectCout) {
        this.selectCout = selectCout;
    }

    public DocAchat getBon() {
        return bon;
    }

    public void setBon(DocAchat bon) {
        this.bon = bon;
    }

    public List<YvsComContenuDocAchat> getContenusBon() {
        return contenusBon;
    }

    public void setContenusBon(List<YvsComContenuDocAchat> contenusBon) {
        this.contenusBon = contenusBon;
    }

    public boolean isIsBon() {
        return isBon;
    }

    public void setIsBon(boolean isBon) {
        this.isBon = isBon;
    }

    public YvsComptaCaissePieceAchat getSelectReglement() {
        return selectReglement;
    }

    public void setSelectReglement(YvsComptaCaissePieceAchat selectReglement) {
        this.selectReglement = selectReglement;
    }

    public boolean isSelectArt() {
        return selectArt;
    }

    public void setSelectArt(boolean selectArt) {
        this.selectArt = selectArt;
    }

    public YvsComContenuDocAchat getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsComContenuDocAchat selectContenu) {
        this.selectContenu = selectContenu;
    }

    public YvsComDocAchats getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComDocAchats selectDoc) {
        this.selectDoc = selectDoc;
    }

    public PieceTresorerie getReglement() {
        return reglement;
    }

    public void setReglement(PieceTresorerie reglement) {
        this.reglement = reglement;
    }

    public String getTabIds_cout() {
        return tabIds_cout;
    }

    public void setTabIds_cout(String tabIds_cout) {
        this.tabIds_cout = tabIds_cout;
    }

    public ContenuDocAchat getContenu() {
        return contenu;
    }

    public void setContenu(ContenuDocAchat contenu) {
        this.contenu = contenu;
    }

    public String getTabIds_article() {
        return tabIds_article;
    }

    public void setTabIds_article(String tabIds_article) {
        this.tabIds_article = tabIds_article;
    }

    public String getTabIds_article_fa() {
        return tabIds_article_fa;
    }

    public void setTabIds_article_fa(String tabIds_article_fa) {
        this.tabIds_article_fa = tabIds_article_fa;
    }

    public DocAchat getDocAchat() {
        return docAchat;
    }

    public void setDocAchat(DocAchat docAchat) {
        this.docAchat = docAchat;
    }

    public List<YvsComDocAchats> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocAchats> documents) {
        this.documents = documents;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public YvsComTaxeContenuAchat getSelectedTaxe() {
        return selectedTaxe;
    }

    public void setSelectedTaxe(YvsComTaxeContenuAchat selectedTaxe) {
        this.selectedTaxe = selectedTaxe;
    }

    public List<YvsBaseUniteMesure> getLconditionnements() {
        return lconditionnements;
    }

    public void setLconditionnements(List<YvsBaseUniteMesure> lconditionnements) {
        this.lconditionnements = lconditionnements;
    }

    @Override
    public void loadAll() {
        _load();
        loadInfosWarning(false);
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {
//            addParamToValide();
        }
//        loadContenus(true, true);
        initView();
        setFournisseurDefaut();
        _first = true;
        if (reglement.getMode() != null ? reglement.getMode().getId() < 1 : true) {
            reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        }
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllFacture(true, true);
    }

    public void load(String load) {
        _first = true;
    }

    public void initView() {
        indiceNumsearch_ = genererPrefixe(Constantes.TYPE_FA_NAME, docAchat.getDepotReception().getId());
        if (docAchat == null) {
            docAchat = new DocAchat();
            docAchat.setTypeDoc(Constantes.TYPE_FA);
            numSearch_ = "";
        }
        if (agence_ < 1) {
            agence_ = currentAgence.getId();
        }
        if (agenceContenu < 0) {
            agenceContenu = currentAgence.getId();
        }
        if ((docAchat.getFournisseur() != null) ? docAchat.getFournisseur().getId() < 1 : true) {
            docAchat.setTypeDoc(Constantes.TYPE_FA);
        }
        if (docAchat.getDocumentLie() == null) {
            docAchat.setDocumentLie(new DocAchat());
        }
        if ((docAchat.getDepotReception() != null) ? docAchat.getDepotReception().getId() < 1 : true) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null ? m.getDepots().contains(currentDepot) : false) {
                docAchat.setDepotReception(UtilCom.buildBeanDepot(currentDepot));
                loadAllTranche(currentDepot, docAchat.getDateLivraison());
                if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false) {
                    docAchat.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
                }
            }
        }
        if (currentParamAchat == null) {
            currentParamAchat = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
    }

    private void loadTaxesAchat(YvsComDocAchats y) {
        docAchat.getTaxes().clear();
        List<Object[]> l = dao.getTaxeAchat(y.getId());
        for (Object[] o : l) {
            YvsBaseTaxes t = (YvsBaseTaxes) dao.loadOneByNameQueries("YvsBaseTaxes.findById", new String[]{"id"}, new Object[]{o[0]});
            if (t != null ? t.getId() > 0 : false) {
                t.setTaux((double) o[1]);
                docAchat.getTaxes().add(t);
            }
        }
        update("data_taxes_facture_achat");
    }

    public void loadFicheByFsseur() {
        if (docAchat != null ? docAchat.getFournisseur() != null : false) {
            YvsBaseFournisseur y = new YvsBaseFournisseur(docAchat.getFournisseur().getId());
        }
    }

    public void loadAllFacture(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_FA)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND");
                paginator.addParam(p);
                break;
            case 3: //charge tous les document de l'agence du users
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (currentUser.getUsers() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                }
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.depotReception.id", "depot", ids, " IN ", "AND");
                paginator.addParam(p);
                break;
            default:    //charge les document dont il est l'auteur du users
                p = new ParametreRequete("y.author.users", "users", currentUser.getUsers(), "=", "AND");
                paginator.addParam(p);
                break;

        }
        paginator.addParam(new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_FA, "=", "AND"));
        documents = paginator.executeDynamicQuery("y", "y", "YvsComDocAchats y JOIN FETCH y.fournisseur JOIN FETCH y.fournisseur.tiers JOIN FETCH y.agence", "y.dateDoc DESC, y.numDoc DESC", avance, init, (int) imax, dao);
        if (documents != null ? documents.size() == 1 : false) {
            onSelectObject(documents.get(0));
            execute("collapseForm('facture_achat')");
            execute("_slideShow('zone_show_detail_produit')");
        } else {
            execute("collapseList('facture_achat')");
        }
        subLenght = documents.size() > 10 ? 10 : documents.size();
    }

    public void gotoPagePaginatorContenu() {
        p_contenu.gotoPage(p_contenu.getPage());
        loadContenus(true, true);
    }

    public void choosePaginatorContenu(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            long v;
            try {
                v = (long) ev.getNewValue();
            } catch (Exception ex) {
                v = (int) ev.getNewValue();
            }
            p_contenu.setRows((int) v);
            loadContenus(true, true);
        }
    }

    public void loadContenus(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_FA)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.docAchat.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                p_contenu.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.docAchat.agence", "agence", currentAgence, "=", "AND");
                p_contenu.addParam(p);
                break;
            case 3: { //charge tous les document des points de vente où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (currentUser.getUsers() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                }
                if (!ids.isEmpty()) {
                    p = new ParametreRequete("y.docAchat.depotReception.id", "depot", ids, " IN ", "AND");
                    p_contenu.addParam(p);
                } else {
                    p_contenu.getParams().clear();
                }
                break;
            }
            default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                p = new ParametreRequete("y.docAchat.author.users ", "users", currentUser.getUsers(), "=", "AND");
                p_contenu.addParam(p);
                break;

        }

        p_contenu.addParam(new ParametreRequete("y.docAchat.typeDoc", "typeDoc", Constantes.TYPE_FA, "=", "AND"));
        String orderBy = "y.docAchat.dateDoc DESC, y.docAchat.numDoc";
        all_contenus = p_contenu.executeDynamicQuery("YvsComContenuDocAchat", orderBy, avance, init, dao);
        update("data_contenu_fa");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComDocAchats> re = paginator.parcoursDynamicData("YvsComDocAchats", "y", "y.dateDoc DESC, y.numDoc DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadFactureNonLivre(boolean avance, boolean init) {
        if (_first) {
            clearParams();
        }
        _first = false;
        egaliteStatut = "=";
        egaliteStatutL = "!=";
        egaliteStatutR = "=";
        statutLivre_ = Constantes.ETAT_LIVRE;
        paginator.addParam(new ParametreRequete("y.statutLivre", "statutLivre", statutLivre_, egaliteStatutL, "AND"));
        loadAllFacture(avance, init);
    }

    public void loadFactureNonRegle(boolean avance, boolean init) {
        if (_first) {
            clearParams();
        }
        _first = false;
        statutRegle_ = Constantes.ETAT_REGLE;
        egaliteStatutR = "!=";
        paginator.addParam(new ParametreRequete("y.statutRegle", "statutRegle", Constantes.ETAT_REGLE, "!=", "AND"));
        loadAllFacture(avance, init);
    }

    public void loadEditableFacture(boolean avance, boolean init) {
        if (paginator.getParams().contains(new ParametreRequete("fournisseurs"))) {
            if (_first) {
                clearParams();
            }
            _first = false;
            statut_ = Constantes.ETAT_EDITABLE;
            cloturer_ = false;

            paginator.addParam(new ParametreRequete("y.statut", "statut", Constantes.ETAT_EDITABLE, "=", "AND"));
            paginator.addParam(new ParametreRequete("y.cloturer", "cloturer", false, "=", "AND"));
            loadAllFacture(avance, init);
        } else {
            documents.clear();
            subLenght = 0;
        }
    }

    public void loadFactureStatut(String statut) {
        if (_first) {
            clearParams();
        }
        _first = false;
        statut_ = statut;
        paginator.addParam(new ParametreRequete("y.statut", "statut", statut, "=", "AND"));
        loadAllFacture(true, true);
    }

    public void loadTarifaireUnite(Conditionnement unite) {
        if (unite != null ? unite.getId() > 0 : false) {
            unite.setTarifaires(dao.loadNameQueries("YvsBaseConditionnementPoint.findConditionnementAgence", new String[]{"agence", "unite"}, new Object[]{currentAgence, new YvsBaseConditionnement(unite.getId())}));
        }
    }

    public void clearParams() {
        entete_ = 0;
        tranche_ = 0;
        depot_ = 0;
        agence_ = 0;

        codeFsseur_ = null;
        numSearch_ = null;
        statut_ = null;
        codeVendeur_ = null;
        statutLivre_ = null;
        statutRegle_ = null;
        cloturer_ = null;

        date_ = false;
        date_up = false;
        toValideLoad = false;
        dateDebut_ = new Date();
        dateFin_ = new Date();
        paginator.getParams().clear();
        _first = true;
//        loadAllFacture(true, true);
        addParamAgenceDoc();
        update("blog_plus_option_facture_achat");
    }

    public void loadSectionsAchat(YvsComContenuDocAchat y) {
        selectContenu = y != null ? y : new YvsComContenuDocAchat();
        if (y != null) {
            selectContenu.setAnalytiques(dao.loadNameQueries("YvsComptaCentreContenuAchat.findByContenu", new String[]{"contenu"}, new Object[]{y}));
            update("blog-analytique_fa");
        } else {
            ManagedCentreAnalytique w = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (w != null) {
                for (YvsComptaCentreAnalytique e : w.getCentres()) {
                    selectContenu.getAnalytiques().add(new YvsComptaCentreContenuAchat(Long.valueOf(-(selectContenu.getAnalytiques().size())), e));
                }
                update("data-all_analytique_fa");
            }
        }
    }

//    public void loadTaxesAchat(YvsComContenuDocAchat y) {
//        contenu.setNameArticle(y.getArticle().getDesignation());
//        contenu.setTaxes(new ArrayList<YvsComTaxeContenuAchat>());
//        contenu.getTaxes().addAll(y.getTaxes());
//        update("data_taxes_contenu_facture_achat");
//    }   
    public void loadAllTaxesAchat(YvsComDocAchats y) {
        taxes.clear();
        List<Object[]> l = dao.getTaxeAchat(y.getId());
        for (Object[] o : l) {
            YvsBaseTaxes t = (YvsBaseTaxes) dao.loadOneByNameQueries("YvsBaseTaxes.findById", new String[]{"id"}, new Object[]{o[0]});
            if (t != null ? t.getId() > 0 : false) {
                t.setTaux((double) o[1]);
                taxes.add(t);
            }
        }
        update("data_taxes_facture_achat");
    }

    public void loadTaxesAchat(YvsComContenuDocAchat y) {
        contenu.setNameArticle(y.getArticle().getDesignation());
        contenu.setTaxes(new ArrayList<YvsComTaxeContenuAchat>());
        contenu.getTaxes().addAll(y.getTaxes());
        ManagedTaxes service = (ManagedTaxes) giveManagedBean(ManagedTaxes.class);
        if (service != null) {
            service.loadAllTaxes("A");
            List<YvsBaseTaxes> l = new ArrayList<>(service.getTaxesList());
            for (YvsComTaxeContenuAchat tc : y.getTaxes()) {
                l.remove(tc.getTaxe());
            }
            YvsComTaxeContenuAchat taxe;
            for (YvsBaseTaxes t : l) {
                if (t.getActif()) {
                    taxe = new YvsComTaxeContenuAchat();
                    taxe.setAuthor(currentUser);
                    taxe.setTaxe(t);
                    taxe.setContenu(y);
                    taxe.setMontant(0d);
                    contenu.getTaxes().add(taxe);
                }
            }
        }
        update("tbl_data_taxes");
    }

    private void loadOthers(YvsComDocAchats y) {

        update("blog_others");
    }

    private void loadContenusBon(YvsComDocAchats y) {
        contenusBon = loadContenusStay(y, Constantes.TYPE_FA);
        update("data_article_bon_achat");
    }

    private List<YvsComContenuDocAchat> loadContenusStay(YvsComDocAchats y, String type) {
        List<YvsComContenuDocAchat> list = new ArrayList<>();
        y.setInt_(false);
        nameQueri = "YvsComContenuDocAchat.findByDocAchat";
        champ = new String[]{"docAchat"};
        val = new Object[]{y};
        List<YvsComContenuDocAchat> contenus = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComContenuDocAchat c : contenus) {
            String[] ch = new String[]{"parent", "typeDoc", "statut"};
            Object[] v = new Object[]{c, type, Constantes.ETAT_VALIDE};
            Double qte = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByTypeStatutParent", ch, v);
            if (c.getQuantiteRecu() > (qte != null ? qte : 0)) {
                c.setQuantiteCommande(c.getQuantiteRecu());
                c.setQuantiteRecu(c.getQuantiteRecu() - (qte != null ? qte : 0));
                c.setPrixTotal(c.getPrixAchat() * c.getQuantiteRecu());
                c.setParent(new YvsComContenuDocAchat(c.getId()));
                int index = y.getContenus().indexOf(c);
                if (index > -1) {
                    c.setLot(y.getContenus().get(index).getLot());
                }
                list.add(c);
            }
        }
        return list;
    }

    public void loadAllTranche(YvsBaseDepots depot, Date date) {
        if (docAchat.getDepotReception() != null ? depot != null ? docAchat.getDepotReception().getId() != depot.getId() : true : true) {
            docAchat.setTranche(new TrancheHoraire());
        }
        tranches = loadTranche(depot, date);
        if (tranches != null ? tranches.size() == 1 : false) {
            if (docAchat.getTranche() != null ? docAchat.getTranche().getId() < 0 : false) {
                docAchat.setTranche(UtilCom.buildBeanTrancheHoraire(tranches.get(0)));
            } else {
                if (!tranches.contains(new YvsGrhTrancheHoraire(docAchat.getTranche().getId()))) {
                    docAchat.setTranche(UtilCom.buildBeanTrancheHoraire(tranches.get(0)));
                }
            }
        }
        update("data_tranche_fa");
    }

    public YvsComContenuDocAchat buildContenuDocAchat(ContenuDocAchat y, YvsComDocAchats en) {
        YvsComContenuDocAchat c = UtilCom.buildContenuDocAchat(y, currentUser);
        c.setDocAchat(en);
        return c;
    }

    public YvsComCoutSupDocAchat buildCoutSupDocAchat(CoutSupDoc y) {
        YvsComCoutSupDocAchat c = UtilCom.buildCoutSupDocAchat(y, currentUser);
        if (y != null) {
            if (y.getType() != null ? y.getType().getId() > 0 : false) {
                ManagedTypeCout s = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
                if (s != null) {
                    c.setTypeCout(s.getTypes().get(s.getTypes().indexOf(new YvsGrhTypeCout(y.getType().getId()))));
                } else {
                    c.setTypeCout(new YvsGrhTypeCout(y.getType().getId()));
                }
            }
        }
        return c;
    }

    @Override
    public DocAchat recopieView() {
        docAchat.setTypeDoc(Constantes.TYPE_FA);
        ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
        if (m != null) {
            int idx = m.getModels().indexOf(new YvsBaseModelReglement(docAchat.getModeReglement().getId()));
            if (idx > -1) {
                YvsBaseModelReglement y = m.getModels().get(idx);
                cloneObject(docAchat.getModeReglement(), UtilCom.buildBeanModelReglement(y));
            }
        }
//        return UtilCom.recopieAchat(docAchat, Constantes.TYPE_FA);
        return docAchat;
    }

    public CoutSupDoc recopieViewCoutSupDoc() {
        cout.setDoc(cout.isUpdate() ? cout.getDoc() : docAchat.getId());
        return cout;
    }

    public PieceTresorerie recopieViewPiece() {
        if (reglement != null) {
            if (reglement.getMode() != null ? reglement.getMode().getId() > 0 : false) {
                ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                if (m != null) {
                    int idx = m.getModes().indexOf(new YvsBaseModeReglement((long) reglement.getMode().getId()));
                    if (idx > -1) {
                        YvsBaseModeReglement o = m.getModes().get(idx);
                        reglement.setMode(new ModeDeReglement(o.getId().intValue(), o.getDesignation(), o.getTypeReglement()));
                    }
                }
            }
            if (reglement.getCaisse() != null ? reglement.getCaisse().getId() > 0 : false) {
                ManagedCaisses m = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                if (m != null) {
                    int idx = m.getCaisses().indexOf(new YvsBaseCaisse(reglement.getCaisse().getId()));
                    if (idx > -1) {
                        YvsBaseCaisse o = m.getCaisses().get(idx);
                        reglement.setCaisse(new Caisses(o.getId(), o.getCode(), o.getIntitule()));
                    }
                }
            }
            reglement.setMouvement(Constantes.MOUV_CREDIT);
            reglement.setUpdate(true);
            reglement.setDateUpdate(new Date());
            if (reglement.getId() < 1) {
                reglement.setDatePiece(reglement.getDatePaiement());
            }
            reglement.setDatePaiementPrevu(reglement.getDatePaiement());
        }
        return reglement;
    }

    @Override
    public boolean controleFiche(DocAchat bean) {
        return controleFiche(bean, true, true);
    }

    public boolean controleFiche(DocAchat bean, boolean livre, boolean regle) {
        if (!_controleFiche_(bean, true, livre, regle)) {
            return false;
        }
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            if (w.isComptabilise(bean.getId(), Constantes.SCR_ACHAT)) {
                getErrorMessage("Cette pièce est déjà comptabilisée");
                return false;
            }
        }
        if ((bean.getFournisseur() != null) ? bean.getFournisseur().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer le fournisseur");
            return false;
        }
        if ((bean.getCategorieComptable() != null) ? bean.getCategorieComptable().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer la catégorie comptable");
            return false;
        }
        //modifie le numéro de document si la date change   
        if ((selectDoc != null ? (((selectDoc.getId() != null) ? selectDoc.getId() > 0 : false && selectDoc.getDateDoc() != null) ? !bean.getDateDoc().equals(selectDoc.getDateDoc()) : false) : false)
                || (bean.getNumDoc() == null || bean.getNumDoc().trim().length() < 1)) {
            String ref = genererReference(Constantes.TYPE_FA_NAME, bean.getDateDoc(), (bean.getDepotReception() != null ? bean.getDepotReception().getId() : 0));
            if (ref == null || ref.trim().equals("")) {
                return false;
            }
            bean.setNumDoc(ref);
        }
        return true;
    }

    private boolean _controleFiche_(DocAchat bean, boolean controle, boolean livre, boolean regle) {
        if (bean == null) {
            getErrorMessage("Vous devez selectionner un document");
            return false;
        }
        if (new Date().before(bean.getDateDoc())) {
            getErrorMessage("Vous ne pouvez pas enregistrer une facture dans le future");
            return false;
        }
        if (bean.getId() < 1) {
            if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
                return false;
            }
        } else {
            if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                YvsWorkflowValidFactureAchat e;
                YvsWorkflowValidFactureAchat v = giveEtapeActuelle(bean.getEtapesValidations());
                for (int i = 0; i < bean.getEtapesValidations().size(); i++) {
                    e = bean.getEtapesValidations().get(i);
                    if (!e.getEtapeValid()) {
                        if (!asDroitValideEtape(v.getEtape().getAutorisations(), currentUser.getUsers().getNiveauAcces())) {
                            getErrorMessage("Vous ne pouvez modifier cette facture ! Elle requière un niveau suppérieur");
                            return false;
                        }
                    }
                }
            }
            if (controle) {
                if (bean.getStatut().equals(Constantes.ETAT_VALIDE) && !autoriser("fa_update_doc_valid")) {
                    getErrorMessage("Vous ne pouvez pas modifer cette facture ! Elle est déja validée");
                    return false;
                }
            }
            if (bean.getStatut().equals(Constantes.ETAT_ANNULE)) {
                getErrorMessage("Vous ne pouvez pas modifer cette facture ! Elle est déja annulée");
                return false;
            }
            if (regle) {
                if (bean.getStatutRegle().equals(Constantes.ETAT_REGLE) && !autoriser("fa_update_doc_valid")) {
                    getErrorMessage("Vous ne pouvez pas modifer cette facture ! Elle est déja réglée");
                    return false;
                }
            }
            if (livre) {
                if (bean.getStatutLivre().equals(Constantes.ETAT_LIVRE) && !autoriser("fa_update_doc_valid")) {
                    getErrorMessage("Vous ne pouvez pas modifer cette facture ! Elle est déja livrée");
                    return false;
                }
            }
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document est vérouillé");
            return false;
        }
        boolean comptabilise = dao.isComptabilise(bean.getId(), Constantes.SCR_ACHAT);
        if (comptabilise) {
            getErrorMessage("Cette facture est déja comptabilisé");
            return false;
        }
        return verifyDateAchat(bean.getDateDoc(), bean.getId() > 0);
    }

    private boolean _controleFiche_(YvsComDocAchats bean) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        for (YvsWorkflowValidFactureAchat e : bean.getEtapesValidations()) {
            if (e.getEtapeValid()) {
                if (!asDroitValideEtape(e.getEtape().getAutorisations(), currentUser.getUsers().getNiveauAcces())) {
                    getErrorMessage("Vous ne pouvez modifier cette facture ! Elle requière un niveau supérieur");
                    return false;
                }
            }
        }
        if (bean.getStatut().equals(Constantes.ETAT_VALIDE)) {
            getErrorMessage("Vous ne pouvez pas modifer cette facture ! Elle est déja validée");
            return false;
        }
        if (bean.getStatut().equals(Constantes.ETAT_ANNULE)) {
            getErrorMessage("Vous ne pouvez pas modifer cette facture ! Elle est déja annulée");
            return false;
        }
        if (bean.getCloturer()) {
            getErrorMessage("Ce document est vérouillé");
            return false;
        }
        boolean comptabilise = dao.isComptabilise(bean.getId(), Constantes.SCR_ACHAT);
        if (comptabilise) {
            getErrorMessage("Cette facture est déja comptabilisé");
            return false;
        }
        return true;
    }

    public boolean controleFicheArticle(ContenuDocAchat bean) {
        if (bean.getDocAchat() != null ? bean.getDocAchat().getId() < 1 : true) {
            if (!_saveNew(true, true, true)) {
                return false;
            }
            bean.setDocAchat(docAchat);
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner l' article");
            return false;
        }
        if ((bean.getConditionnement() != null) ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le conditionnement");
            return false;
        }
        if (!bean.getDocAchat().getStatut().equals(Constantes.ETAT_EDITABLE) && !bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Vous ne pouvez pas modifier cette ligne car le document est " + giveNameStatut(bean.getDocAchat().getStatut()).toLowerCase() + " et la ligne est valide");
            return false;
        }
        if (bean.getQuantiteCommende() <= 0) {
            getErrorMessage("Vous devez entrer une quantité valide");
            return false;
        }
        if (bean.getPrixAchat() <= 0) {
            getErrorMessage("Vous devez specifier un prix d'achat");
            return false;
        }
        if (!bean.getArticle().getCategorie().equals(Constantes.CAT_SERVICE) && selectDoc.getStatutLivre().equals(Constantes.ETAT_LIVRE) && bean.getId() > 0) {
            String result = controleStock(bean.getArticle().getId(), (bean.getConditionnement() != null ? bean.getConditionnement().getId() : 0), selectDoc.getDepotReception().getId(), 0L, bean.getQuantiteCommende(), selectContenu.getQuantiteRecu(), "UPDATE", "S", selectDoc.getDateDoc(), (bean.getLot() != null ? bean.getLot().getId() : 0));
            if (result != null) {
                getErrorMessage("L'article '" + bean.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                return false;
            }
        }
        bean.setQuantiteRecu(bean.getQuantiteCommende());
        return _controleFiche_(bean.getDocAchat(), false, !autoriser("fa_update_when_receive"), !autoriser("fa_update_when_paye"));
    }

    private boolean _controleFiche_(YvsComContenuDocAchat bean, boolean msg) {
        if (bean == null) {
            return false;
        }
        if (selectDoc.getStatutRegle().equals(Constantes.ETAT_REGLE) && !autoriser("fa_update_when_paye")) {
            if (msg) {
                getErrorMessage("Cette ligne ne peux être supprimé car la facture est déjà payée");
            }
            return false;
        }
        if (selectDoc.getStatutLivre().equals(Constantes.ETAT_LIVRE) && !autoriser("fa_update_when_receive")) {
            if (msg) {
                openNotAccesAction("Cette ligne ne peux être supprimé car la facture est déjà livrée");
            }
            return false;
        }
        if (dao.isComptabilise(selectDoc.getId(), Constantes.SCR_ACHAT)) {
            if (msg) {
                getErrorMessage("Cette ligne ne peux être supprimé car la facture est déjà comptabilisée");
            }
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            if (msg) {
                getErrorMessage("Cette ligne ne peux être supprimé car elle est validée");
            }
            return false;
        }
        return true;
    }

    public boolean controleFicheCout(CoutSupDoc bean) {
        if (bean.getDoc() < 1) {
            if (!_saveNew(true, true, true)) {
                return false;
            }
            bean.setDoc(docAchat.getId());
        }
        if ((bean.getType() != null) ? bean.getType().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le type de coût");
            return false;
        }
        if (bean.getMontant() < 1) {
            getErrorMessage("Vous devez entrer un montant");
            return false;
        }

        if (docAchat != null) {
            if (docAchat.getId() <= 0) {
                getErrorMessage("Vous devez selectionner un document");
                return false;
            }
            if (docAchat.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                getErrorMessage("Le document est déjà marqué réglé !");
                return false;
            }
        } else {
            getErrorMessage("Vous devez selectionner un document");
            return false;
        }
        return true;
    }

    public boolean controleFiche(CentreContenuAchat bean) {
        if (bean.getContenu() != null ? bean.getContenu().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner le contenu");
            return false;
        }
        if (bean.getCentre() != null ? bean.getCentre().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez selectionner le centre analytique");
            return false;
        }
        double taux = bean.getTaux();
        for (YvsComptaCentreContenuAchat a : selectContenu.getAnalytiques()) {
            if (!a.getId().equals(bean.getId()) ? a.getCentre().getId().equals(bean.getCentre().getId()) : false) {
                getErrorMessage("Formulaire invalide", "Vous avez déjà attribuer ce centre analytique");
                return false;
            }
            if (!a.getId().equals(bean.getId())) {
                taux += a.getCoefficient();
            }
        }
        if (taux > 100) {
            getErrorMessage("Formulaire invalide", "La répartition analytique ne peut pas etre supérieur à 100%");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(DocAchat bean) {
        cloneObject(docAchat, bean);
        docAchat.setEtapesValidations(ordonneEtapes(bean.getEtapesValidations()));
        for (YvsWorkflowValidFactureAchat etape : docAchat.getEtapesValidations()) {
            if (etape.getEtape().getLivraisonHere() && etape.getEtapeValid()) {
                docAchat.setLivraisonDo(true);
            }
        }
        isBon = (bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false);
        if (isBon) {
            cloneObject(bon, bean.getDocumentLie());
            loadContenusBon(new YvsComDocAchats(bon.getId()));
        }
        setMontantTotalDoc(docAchat);
        loadTaxesAchat(new YvsComDocAchats(docAchat.getId()));
    }

    public void populateViewArticle(ContenuDocAchat bean) {
        bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, docAchat.getDepotReception().getId(), 0, 0, docAchat.getDateLivraison(), bean.getConditionnement().getId(), bean.getLot().getId()));
        bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));
        selectArt = true;
        isBonus = false;
        cloneObject(contenu, bean);
        chooseArticle(bean.getArticle());
        update("desc_contenu_facture_achat");
    }

    @Override
    public void resetFiche() {
        resetFiche_();
        update("blog_form_facture_achat");
    }

    public void resetFiche_() {
        docAchat = new DocAchat();
        docAchat.setStatut(Constantes.ETAT_EDITABLE);
        docAchat.setCloturer(false);
        ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (m != null ? m.getDepots().contains(currentDepot) : false) {
            docAchat.setDepotReception(UtilCom.buildBeanDepot(currentDepot));
            loadAllTranche(currentDepot, docAchat.getDateLivraison());
            if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false) {
                docAchat.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
            }

            bon = new DocAchat();

            selectDoc = new YvsComDocAchats((long) 0);

            resetFicheArticle();
            resetFicheCout();

            tabIds = "";
            isBon = false;
            setFournisseurDefaut();
        }

    }

    public void resetSubFiche() {
        resetFicheArticle();
        resetFicheCout();
        resetFicheReglement(false);
        update("tabview_facture_achat");
    }

    public void resetFicheArticle() {
        contenu = new ContenuDocAchat();
        bonus = new ContenuDocAchat();

        selectContenu = new YvsComContenuDocAchat();
        tabIds_article = "";
        selectArt = false;
        listArt = false;
        resetFicheAnalytique();
        update("tabview_facture_achat:blog_form_contenu_facture_achat");
        update("desc_contenu_facture_achat");
    }

    public void resetFicheCout() {
        resetFiche(cout);
        cout.setType(new TypeCout());
        tabIds_cout = "";
        selectCout = null;
    }

    public void resetFicheReglement(boolean save) {
        reglement = new PieceTresorerie();
        reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        tabIds_mensualite = "";
        if (!save) {
            newMensualite = false;
        }
        if (docAchat != null) {
            if (docAchat.getMontantNetApayer() > 0) {
                double m = docAchat.getMontantNetApayer();
                for (YvsComptaCaissePieceAchat r : docAchat.getReglements()) {
                    m -= r.getMontant();
                }
                reglement.setMontant(m > 0 ? m : 0);
            }
        }
        selectReglement = new YvsComptaCaissePieceAchat();
    }

    public void resetView(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            int idx = getDocuments().indexOf(y);
            if (idx > -1) {
                getDocuments().set(idx, y);
                update("data_facture_achat");
            }
            if (docAchat.getId() == y.getId()) {
                onSelectObject(y);
            }
        }
    }

    public void resetFicheAnalytique() {
        analytique = new CentreContenuAchat();
        selectAnalytique = new YvsComptaCentreContenuAchat();
        update("form-analytique_fa");
    }

    @Override
    public boolean saveNew() {
        if (_saveNew(true, true, true)) {
            succes();
            actionOpenOrResetAfter(this);
            return true;
        }
        return false;
    }

    public boolean _saveNew(boolean update, boolean livre, boolean regle) {
        YvsComDocAchats selectDoc = _saveNew(recopieView(), update, livre, regle);
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            this.selectDoc = selectDoc;
            return true;
        }
        return false;
    }

    public YvsComDocAchats _saveNew(DocAchat bean, boolean update, boolean livre, boolean regle) {
        try {
            if (controleFiche(bean, livre, regle)) {
                YvsComDocAchats y = UtilCom.buildDocAchat(bean, currentUser, currentAgence);
                if (bean.getId() <= 0) {
                    List<YvsWorkflowEtapeValidation> etapes = saveEtapesValidation();
                    docAchat.setEtapeTotal(etapes != null ? etapes.size() : 0);
                    y.setEtapeTotal(docAchat.getEtapeTotal());
                    y.setId(null);
                    y = (YvsComDocAchats) dao.save1(y);
                    docAchat.setId(y.getId());
                    y.setEtapesValidations(saveEtapesValidation(y, etapes));
                    docAchat.setEtapesValidations(new ArrayList<>(y.getEtapesValidations()));
                    documents.add(0, y);
                    if (documents.size() > imax) {
                        documents.remove(documents.size() - 1);
                    }
                } else {
                    dao.update(y);
                    bean.setUpdate(true);
                    if (update) {
                        if (documents.contains(y)) {
                            documents.set(documents.indexOf(y), y);
                        }
                    }
                }

                docAchat.setNumDoc(bean.getNumDoc());
                docAchat.setUpdate(true);
                update("data_facture_achat");
                update("entete_form_facture_achat");
                return y;
            }
            return null;
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
            return null;
        }
    }

    public void addNewArticleFournisseur() {
        ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (m != null) {
            if (m.saveNewArticle()) {
                chooseArticle(contenu.getArticleSave());
            }
        }
    }

    public void saveNewArticle() {
        try {
            contenu.setDocAchat(docAchat);
            if (controleFicheArticle(contenu)) {
                findPrixArticle(contenu, selectContenu, selectDoc, false);
                contenu.setPrixTotalRecu(contenu.getPrixTotalRecu() > 0 ? contenu.getPrixTotalRecu() : 0);
                contenu.setPrixTotalAttendu(contenu.getPrixTotalRecu());

                boolean update = false;
                boolean save = false;
                if (selectContenu != null ? selectContenu.getId() > 0 : false) {
                    if (!selectContenu.getArticle().getId().equals(contenu.getArticle().getId())) {
                        update = true;
                    }
                }
                selectContenu = UtilCom.buildContenuDocAchat(contenu, currentUser);
                if (contenu.getId() < 1) {
                    selectContenu.setId(null);
                    selectContenu = (YvsComContenuDocAchat) dao.save1(selectContenu);
                    save = true;
                } else {
                    dao.update(selectContenu);
                }
                int idx = docAchat.getContenus().indexOf(selectContenu);
                if (idx > -1) {
                    docAchat.getContenus().set(idx, selectContenu);
                } else {
                    docAchat.getContenus().add(0, selectContenu);
                }
                idx = selectDoc.getContenus().indexOf(selectContenu);
                if (idx > -1) {
                    selectDoc.getContenus().set(idx, selectContenu);
                } else {
                    selectDoc.getContenus().add(0, selectContenu);
                }
                idx = docAchat.getContenusSave().indexOf(selectContenu);
                if (idx < 0) {
                    docAchat.getContenusSave().add(selectContenu);
                } else {
                    docAchat.getContenusSave().set(idx, selectContenu);
                }
                idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                    update("data_facture_achat");
                }
                saveAllCentre(selectContenu, save, update);
                saveAllTaxe(selectContenu);
                setMontantTotalDoc(docAchat);
                loadTaxesAchat(new YvsComDocAchats(docAchat.getId()));
                resetFicheArticle();
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    private boolean controleSoeCoef(List<YvsComptaCentreContenuAchat> l, YvsComContenuDocAchat c) {
        double coefficient = 0;
        YvsComptaCentreContenuAchat y;
        for (YvsComptaCentreContenuAchat a : l) {
            y = (YvsComptaCentreContenuAchat) dao.loadOneByNameQueries("YvsComptaCentreContenuAchat.findByContenuCentre", new String[]{"contenu", "centre"}, new Object[]{c, a.getCentre()});
            if (y != null ? y.getId() < 1 : true) {
                //la liaison n'existe pas encore
                coefficient = coefficient + a.getCoefficient();
            } else {
                if (a.getCoefficient() > 0) {
                    coefficient = coefficient + a.getCoefficient();
                } else {
                    coefficient = coefficient + y.getCoefficient();
                }
            }

        }
        return coefficient <= 100;
    }

    public void saveNewAnalytique() {
        //Contrôle si la facture est comptabilisé
        if (dao.isComptabilise(docAchat.getId(), Constantes.SCR_ACHAT)) {
            getErrorMessage("Cette facture est déjà comptabilisé !");
            return;
        }
        try {
            boolean save = selectContenu != null ? selectContenu.getId() > 0 : false;
            analytique.setContenu(new ContenuDocAchat(selectContenu != null ? selectContenu.getId() : 0));
            if (save ? controleFiche(analytique) : true) {
                selectAnalytique = UtilCompta.buildCentreContenuAchat(analytique, currentUser);
                if (save) {
                    if (analytique.getId() < 1) {
                        selectAnalytique = (YvsComptaCentreContenuAchat) dao.save1(selectAnalytique);
                    } else {
                        dao.update(selectAnalytique);
                    }
                } else {
                    selectAnalytique.setId(Long.valueOf(-(selectContenu.getAnalytiques().size() + 1)));
                }
                int idx = selectContenu.getAnalytiques().indexOf(selectAnalytique);
                if (idx < 0) {
                    selectContenu.getAnalytiques().add(0, selectAnalytique);
                } else {
                    selectContenu.getAnalytiques().set(idx, selectAnalytique);
                }
                resetFicheAnalytique();
                if (save) {
                    succes();
                }
                update("data-analytique_fa");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedArticles (saveNewAnalytique)", ex);
        }
    }

    public void saveAllAnalytique() {
        try {
            if (dao.isComptabilise(docAchat.getId(), Constantes.SCR_ACHAT)) {
                getErrorMessage("Cette facture est déjà comptabilisé !");
                return;
            }
            if (selectContenu.getAnalytiques() != null ? !selectContenu.getAnalytiques().isEmpty() : false) {
                YvsComptaCentreContenuAchat y;

                for (YvsComContenuDocAchat c : docAchat.getContenus()) {
                    if (!controleSoeCoef(selectContenu.getAnalytiques(), c)) {
                        getErrorMessage("Erreur de données:", "" + c.getArticle().getDesignation() + " est affecté à plus de 100%");
                        return;
                    }
                    for (YvsComptaCentreContenuAchat a : selectContenu.getAnalytiques()) {
                        y = (YvsComptaCentreContenuAchat) dao.loadOneByNameQueries("YvsComptaCentreContenuAchat.findByContenuCentre", new String[]{"contenu", "centre"}, new Object[]{c, a.getCentre()});
                        if (y != null ? y.getId() < 1 : true) {
                            if (a.getCoefficient() > 0) {
                                y = new YvsComptaCentreContenuAchat(c, a.getCentre());
                                y.setCoefficient(a.getCoefficient());
                                y.setAuthor(currentUser);
                                dao.save1(y);
                            }
                        } else {
                            if (a.getCoefficient() > 0) {
                                y.setCoefficient(a.getCoefficient());
                                y.setAuthor(currentUser);
                                dao.update(y);
                            }
                        }
                    }
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedArticles (saveAllAnalytique)", ex);
        }
    }

    public void saveAllCentre(YvsComContenuDocAchat y, boolean save, boolean update) {
        if (y != null ? y.getId() > 0 : false) {
            if (update) {
                String query = "DELETE FROM yvs_compta_centre_contenu_achat WHERE contenu = ?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.getAnalytiques().clear();
            }
            if (save || update) {
                List<YvsBaseArticleAnalytique> list = dao.loadNameQueries("YvsBaseArticleAnalytique.findByArticle", new String[]{"article"}, new Object[]{y.getArticle()});
                YvsComptaCentreContenuAchat c;
                for (YvsBaseArticleAnalytique a : list) {
                    c = (YvsComptaCentreContenuAchat) dao.loadOneByNameQueries("YvsComptaCentreContenuAchat.findByContenuCentre", new String[]{"contenu", "centre"}, new Object[]{y, a.getCentre()});
                    if (c != null ? c.getId() < 1 : true) {
                        c = new YvsComptaCentreContenuAchat(y, a.getCentre());
                        c.setCoefficient(a.getCoefficient());
                        c.setAuthor(currentUser);
                        dao.save(c);
                    }
                }
            }
        }
    }

    public void saveAllTaxe(YvsComContenuDocAchat y) {
        saveAllTaxe(y, docAchat, selectDoc);
    }

    public void saveAllTaxe(YvsComContenuDocAchat y, DocAchat docAchat, YvsComDocAchats selectDoc) {
        long categorie = returnCategorie(docAchat, selectDoc);
        saveAllTaxe(y, docAchat, selectDoc, categorie, true);
    }

    public double saveAllTaxe(YvsComContenuDocAchat y, DocAchat docAchat, YvsComDocAchats selectDoc, long categorie, boolean save) {
        double montant = 0;
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            double prix = y.getPrixAchat();
            double qte = y.getQuantiteRecu();
            double remise = y.getRemise();
            double taxe = 0;
            double valeur = 0;
            YvsBaseArticles article = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{y.getArticle().getId()});
            nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticle";
            champ = new String[]{"categorie", "article"};
            val = new Object[]{new YvsBaseCategorieComptable(categorie), article};
            YvsBaseArticleCategorieComptable acc = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (acc != null ? (acc.getId() != null ? acc.getId() > 0 : false) : false) {
                String query = "SELECT pua_ttc FROM yvs_base_article_fournisseur WHERE article = ? AND fournisseur = ?";
                Boolean pua_ttc = (Boolean) dao.loadObjectBySqlQuery(query, new yvs.dao.Options[]{new yvs.dao.Options(article.getId(), 1), new yvs.dao.Options(docAchat.getFournisseur().getId(), 2)});
                if (pua_ttc == null) {
                    pua_ttc = article.getPuaTtc();
                }
                if (pua_ttc) {
                    for (YvsBaseArticleCategorieComptableTaxe t : acc.getTaxes()) {
                        taxe += t.getTaxe().getTaux();
                    }
                    prix = prix / (1 + (taxe / 100));
                }
                valeur = qte * prix;
                for (YvsBaseArticleCategorieComptableTaxe t : acc.getTaxes()) {
                    taxe = 0;
                    if (t.getAppRemise()) {
                        taxe = (((valeur - remise) * t.getTaxe().getTaux()) / 100);
                    } else {
                        taxe = ((valeur * t.getTaxe().getTaux()) / 100);
                    }
                    montant += taxe;
                    if (save) {
                        YvsComTaxeContenuAchat ct = (YvsComTaxeContenuAchat) dao.loadOneByNameQueries("YvsComTaxeContenuAchat.findOne", new String[]{"contenu", "taxe"}, new Object[]{y, t.getTaxe()});
                        if (ct != null ? (ct.getId() != null ? ct.getId() > 0 : false) : false) {
                            ct.setMontant(taxe);
                            ct.setAuthor(currentUser);
                            dao.update(ct);
                        } else {
                            ct = new YvsComTaxeContenuAchat();
                            ct.setContenu(y);
                            ct.setMontant(taxe);
                            ct.setTaxe(t.getTaxe());
                            ct.setAuthor(currentUser);
                            dao.save1(ct);
                        }
                        int idx = y.getTaxes().indexOf(ct);
                        if (idx > -1) {
                            y.getTaxes().set(idx, ct);
                        } else {
                            y.getTaxes().add(0, ct);
                        }
                    }
                }
                if (save) {
                    int idx = docAchat.getContenus().indexOf(y);
                    if (idx > -1) {
                        docAchat.getContenus().set(idx, y);
                    }
                }
            }
        }
        return montant;
    }

    public void saveNewCout() {
        try {
            CoutSupDoc bean = recopieViewCoutSupDoc();
            if (controleFicheCout(bean) && hasDroitUpdateFacture(selectDoc)) {
                selectCout = buildCoutSupDocAchat(bean);
                if (!bean.isUpdate()) {
                    selectCout.setId(null);
                    selectCout = (YvsComCoutSupDocAchat) dao.save1(selectCout);
                    cout.setId(selectCout.getId());
                } else {
                    dao.update(selectCout);
                }
                int idx = docAchat.getCouts().indexOf(selectCout);
                double montant = (selectCout.getTypeCout().getAugmentation() ? selectCout.getMontant() : 0);
                if (idx > -1) {
                    YvsComCoutSupDocAchat c = docAchat.getCouts().get(docAchat.getCouts().indexOf(selectCout));
                    docAchat.getCouts().set(idx, selectCout);
                    docAchat.setMontantCS(docAchat.getMontantCS() + montant + (c.getTypeCout().getAugmentation() ? -c.getMontant() : 0));

                } else {
                    docAchat.getCouts().add(0, selectCout);
                    docAchat.setMontantCS(docAchat.getMontantCS() + montant);
                }
                resetFicheCout();
                succes();
                update("chp_fa_net_a_payer");
                update("blog_form_montant_doc_fa");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    private boolean controleBeforeSave(boolean deletePhase, PieceTresorerie bean) {

        if (bean.getId() > 0 ? (selectReglement != null ? selectReglement.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) : false) : false) {
            getErrorMessage("La pièce en cours est déjà payé !");
            return false;
        }
        if (!bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE) && !deletePhase) {
            for (YvsComptaPhasePiece pp : reglement.getPhases()) {
                if (pp.getPhaseOk()) {
                    openDialog("dlgConfirmChangeMode");
                    return false;
                }
            }
            if (!reglement.getPhases().isEmpty()) {
                openDialog("dlgConfirmDeletePhase");
                return false;
            }
        }
        return true;
    }

    public void saveNewReglement(boolean deletePhase) {
        boolean update = reglement.getId() > 0;
        try {
            ManagedReglementAchat service = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
            if (service != null) {
                PieceTresorerie bean = recopieViewPiece();
                if (controleBeforeSave(deletePhase, bean)) {
                    YvsComptaCaissePieceAchat piece = UtilCom.buildPieceAchat(bean, currentUser);
                    if (bean.getId() < 1 ? (selectReglement != null ? !selectReglement.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) : true) : true) {
                        piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    }
                    if (piece.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && !bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE)) {
                        piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                        getWarningMessage("Seuls les règlements en espèces peuvent être validé avec ce schéma !");
                    }
                    if (bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE) && bean.getCaisse().getId() <= 0) {
                        piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                        getWarningMessage("Aucune caisse n'a été indiqué!", "La pièce de caisse ne peut-être validé");
                    }
                    if (bean.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                        if (!currentParamAchat.getPaieWithoutValide() ? !docAchat.getStatut().equals(Constantes.ETAT_VALIDE) : false) {
                            piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                            getWarningMessage("La facture doit etre au préalable validée");
                        } else {
                            piece.setCaissier(currentUser.getUsers());
                        }
                    }
                    piece.setAchat(selectDoc);
                    piece = service.createNewPieceCaisse(docAchat, piece, deletePhase);
                    if (piece != null ? piece.getId() > 0 : false) {
                        int idx = docAchat.getReglements().indexOf(piece);
                        if (idx >= 0) {
                            docAchat.getReglements().set(idx, piece);
                        } else {
                            docAchat.getReglements().add(0, piece);
                        }
                        idx = selectDoc.getReglements().indexOf(piece);
                        if (idx >= 0) {
                            selectDoc.getReglements().set(idx, piece);
                        } else {
                            selectDoc.getReglements().add(0, piece);
                        }
                        if (bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE) && reglement.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                            service.reglerPieceTresorerie(docAchat, piece, "F", true);
                        } else {
                            succes();
                        }
                    }
                    selectDoc.setStatutRegle(docAchat.getStatutRegle());
                    selectDoc.setStatutLivre(docAchat.getStatutLivre());
                    resetFicheReglement(true);
                }
            }
        } catch (Exception ex) {
            getErrorMessage(update ? "Modification" : "Insertion" + " Impossible !");
            getException("Règlement Achat Error....", ex);
        }
    }

    public
            void saveNewAllMensualite() {
        try {
            ManagedReglementAchat m = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class
            );
            if (m
                    != null) {
                boolean correct = false;
                for (YvsComptaCaissePieceAchat r : docAchat.getReglements()) {
                    int pos = docAchat.getReglements().indexOf(r);
                    r.setId((long) 0);
                    r = m.createNewPieceCaisse(docAchat, r, false);
                    if (r != null ? r.getId() > 0 : false) {
                        docAchat.getReglements().set(pos, r);
                        int idx = documents.indexOf(new YvsComDocAchats(docAchat.getId()));
                        if (idx >= 0) {
                            int idx1 = documents.get(idx).getReglements().indexOf(r);
                            if (idx1 >= 0) {
                                documents.get(idx).getReglements().set(idx1, r);
                            } else {
                                documents.get(idx).getReglements().add(0, r);
                            }
                        }
                        correct = true;
                    }
                }
                newMensualite = false;
                if (correct) {
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void addBonusContenu(YvsComContenuDocAchat y) {
        bonus = UtilCom.buildBeanContenuDocAchat(y);
        if (bonus.getArticleBonus() != null ? bonus.getArticleBonus().getId() < 1 : true) {
            cloneObject(bonus.getArticleBonus(), bonus.getArticle());
        }
        if (bonus.getConditionnementBonus() != null ? bonus.getConditionnementBonus().getId() < 1 : true) {
            cloneObject(bonus.getConditionnementBonus(), bonus.getConditionnement());
        }
        bonus.getArticleBonus().setSelectArt(true);
        update("txt_bonus_contenu_facture_achat");
    }

    public void addBonusContenu() {
        if (!autoriser("fv_apply_remise")) {
            openNotAcces();
            return;
        }
        if (docAchat.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
            getErrorMessage("Impossible d'ajouter un bonus sur une facture déjà livré !");
            return;
        }
        if (bonus != null ? bonus.getId() > 0 : false) {
            if ((bonus.getArticleBonus() != null) ? bonus.getArticleBonus().getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner l' article");
                return;
            }
            if ((bonus.getConditionnementBonus() != null) ? bonus.getConditionnementBonus().getId() < 1 : true) {
                getErrorMessage("Vous devez specifier le conditionnement");
                return;
            }
            YvsComContenuDocAchat y = UtilCom.buildContenuDocAchat(bonus, currentUser);
            dao.update(y);
            y.getArticle().getConditionnements().addAll(bonus.getArticle().getConditionnements());
            int idx = docAchat.getContenus().indexOf(y);
            if (idx > -1) {
                docAchat.getContenus().set(idx, y);
            }
            update("tabview_facture_achat:data_contenu_facture_achat");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addDateLivraisonContenu(YvsComContenuDocAchat y) {
        selectContenu = y;
        dateLivraison = y.getDateLivraison();
        update("dd_livraison_contenu_facture_achat");
    }

    public void addDateLivraisonContenu() {
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setDateLivraison(dateLivraison);
            selectContenu.setAuthor(currentUser);
            dao.update(selectContenu);
            docAchat.getContenus().set(docAchat.getContenus().indexOf(selectContenu), selectContenu);
            update("tabview_facture_achat:data_contenu_facture_achat");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addCommentaireContenu(YvsComContenuDocAchat y) {
        selectContenu = y;
        commentaire = y.getCommentaire();
        update("txt_commentaire_contenu_facture_achat");
    }

    public void addCommentaireContenu() {
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setCommentaire(commentaire);
            selectContenu.setAuthor(currentUser);
            dao.update(selectContenu);
            docAchat.getContenus().set(docAchat.getContenus().indexOf(selectContenu), selectContenu);
            update("tabview_facture_achat:data_contenu_facture_achat");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addRecuContenu(YvsComContenuDocAchat y) {
        selectContenu = y;
        quantite_recu = y.getQuantiteRecu();
        update("txt_recu_contenu_facture_achat");
    }

    public void addRecuContenu() {
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
//            selectContenu.setQuantiteCommande(selectContenu.getQuantiteRecu());
            selectContenu.setQuantiteRecu(quantite_recu);
            selectContenu.setDateUpdate(new Date());
            selectContenu.setAuthor(currentUser);
            dao.update(selectContenu);
            docAchat.getContenus().set(docAchat.getContenus().indexOf(selectContenu), selectContenu);
            update("tabview_facture_achat:data_contenu_facture_achat");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addNumSerieContenu(YvsComContenuDocAchat y) {
        selectContenu = y;
        numSerie = y.getNumSerie();
        update("txt_num_serie_contenu_facture_achat");
    }

    public void addNumSerieContenu() {
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setNumSerie(numSerie);
            selectContenu.setAuthor(currentUser);
            dao.update(selectContenu);
            docAchat.getContenus().set(docAchat.getContenus().indexOf(selectContenu), selectContenu);
            update("tabview_facture_achat:data_contenu_facture_achat");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addRemiseContenu(YvsComContenuDocAchat y) {
        selectContenu = y;
        montant_remise = y.getRemise();
        taux_remise = y.getTauxRemise();
        update("txt_remise_contenu_facture_achat");
    }

    public void addRemiseContenu() {
        if (!autoriser("fa_apply_remise")) {
            openNotAcces();
            return;
        }
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setAuthor(currentUser);
            selectContenu.setDateUpdate(new Date());
            selectContenu.setPrixTotal(selectContenu.getPrixTotal() + (selectContenu.getRemise()) - montant_remise);
            selectContenu.setRemise(montant_remise);
            dao.update(selectContenu);
            docAchat.getContenus().set(docAchat.getContenus().indexOf(selectContenu), selectContenu);
            setMontantTotalDoc(docAchat);
            update("tabview_facture_achat:data_contenu_facture_achat");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    YvsComptaCaissePieceAchat piece;
    String source;

    public void openConfirmPaiement(YvsComptaCaissePieceAchat pc, String source) {
        this.piece = pc;
        this.source = source;
        if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_ACHAT)) {
            openDialog("dlgConfirmAnnulePiece");
            return;
        }
        ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
        if (w != null) {
            pc.setAchat(selectDoc);
            selectReglement = pc;
            pc = w.openConfirmPaiement(pc, source);
            int idx = docAchat.getReglements().indexOf(pc);
            if (idx > -1) {
                docAchat.getReglements().set(idx, pc);
            }
        }
    }

    public void openConfirmPaiement() {
        ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
        if (w != null) {
            piece.setAchat(selectDoc);
            selectReglement = piece;
            piece = w.openConfirmPaiement(piece, source);
            int idx = docAchat.getReglements().indexOf(piece);
            if (idx > -1) {
                docAchat.getReglements().set(idx, piece);
            }
        }
    }

    public void genereMensualite() {
        if ((docAchat != null) ? docAchat.getId() > 0 : false) {
            docAchat.getReglements().clear();

            if ((docAchat.getModeReglement() != null) ? docAchat.getModeReglement().getId() > 0 : false) {
                ManagedReglementAchat m = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                if (m != null) {
                    Caisses caisse = reglement.getCaisse();
                    if (caisse != null ? caisse.getId() < 1 : true) {
                        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                        if (service != null) {
                            reglement.setCaisse(UtilCompta.buildSimpleBeanCaisse(service.findByResponsable(currentUser.getUsers())));
                        }
                    }
                    docAchat.setReglements(m.generetedPiecesFromModel(new YvsBaseModelReglement(docAchat.getModeReglement().getId(), docAchat.getModeReglement().getDesignation()), docAchat, new YvsBaseCaisse(caisse.getId())));
                    newMensualite = true;
                    reglement.setMontant(0);
                    reglement.setDatePaiement(new Date());
                    update("form_mensualite_facture_achat");
                    succes();
                }
            } else {
                getErrorMessage("Vous devez preciser le model de reglement");
            }
        } else {
            getErrorMessage("Vous devez au préalable enregistrer la facture");
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComDocAchats> list = new ArrayList<>();
                YvsComDocAchats bean;
                for (Long ids : l) {
                    bean = documents.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    if (!_controleFiche_(bean)) {
                        continue;
                    }
                    dao.delete(bean);
                    if (deleteAll) {
                        for (YvsComDocAchats d : bean.getDocuments()) {
                            d.setAuthor(currentUser);
                            dao.delete(d);
                        }
                    }
                    if (bean.getId() == docAchat.getId()) {
                        resetFiche();
                    }
                }
                documents.removeAll(list);
                succes();
                update("data_facture_achat");
                tabIds = "";
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComDocAchats y) {
        selectDoc = y;
    }

    public void openDlgToConfirmSuspend(YvsComDocAchats y) {
        selectDoc = y;
        populateView(UtilCom.buildBeanDocAchat(y));
    }

    public void deleteBean_() {
        try {
            if (selectDoc != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                dao.delete(selectDoc);
                if (deleteAll) {
                    for (YvsComDocAchats d : selectDoc.getDocuments()) {
                        d.setAuthor(currentUser);
                        dao.delete(d);
                    }
                    selectDoc.getDocuments().clear();
                }
                documents.remove(selectDoc);
                if (docAchat.getId() == selectDoc.getId()) {
                    resetFiche();
                }
                succes();
                update("data_facture_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContenu() {
        try {
            if ((tabIds_article != null) ? !tabIds_article.equals("") : false) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                String[] tab = tabIds_article.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComContenuDocAchat bean = docAchat.getContenus().get(docAchat.getContenus().indexOf(new YvsComContenuDocAchat(id)));
                    if (!_controleFiche_(bean, false)) {
                        continue;
                    }
                    dao.delete(new YvsComContenuDocAchat(bean.getId()));
                    docAchat.getContenus().remove(bean);
                    docAchat.getContenusSave().remove(bean);
                    selectDoc.getContenus().remove(bean);
                    int idx = documents.indexOf(new YvsComDocAchats(docAchat.getId()));
                    if (idx > -1) {
                        documents.get(idx).getContenus().remove(bean);
                    }
                    if (contenu.getId() == id) {
                        resetFicheArticle();
                    }
                }
                setMontantTotalDoc(docAchat);
                loadTaxesAchat(new YvsComDocAchats(docAchat.getId()));
                succes();
                update("data_article_bon_achat");

            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanArticles(boolean execute) {
        if (execute || memoriserDeleteContenu) {
            if (selectContenus != null ? !selectContenus.isEmpty() : false) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                for (YvsComContenuDocAchat c : selectContenus) {
                    dao.delete(c);
                    docAchat.getContenus().remove(c);
                    if (contenu.getId() == c.getId()) {
                        resetFicheArticle();
                    }
                }
                succes();
            }
        } else {
            openDialog("dlgConfirmDeleteArticles");
        }
    }

    public void deleteBeanContenu(YvsComContenuDocAchat y) {
        selectContenu = y;
    }

    public void deleteSelectContenu(boolean force) {
        try {
            if (_controleFiche_(selectContenu, true)) {
                if (!force) {
                    if (selectContenu.getContenus() != null ? !selectContenu.getContenus().isEmpty() : false) {
                        openDialog("dlgConfirmDeleteArticleByForce");
                        return;
                    }
                } else {
                    YvsComDocAchats entity;
                    for (YvsComContenuDocAchat y : selectContenu.getContenus()) {
                        entity = y.getDocAchat();
                        if (entity != null ? entity.getStatut().equals(Constantes.ETAT_VALIDE) : true) {
                            String result = controleStock(y.getArticle().getId(), (y.getConditionnement() != null ? y.getConditionnement().getId() : 0), entity.getDepotReception().getId(), 0L, y.getQuantiteRecu(), 0, "DELETE", "E", entity.getDateDoc(), (y.getLot() != null ? y.getLot().getId() : 0));
                            if (result != null) {
                                getErrorMessage("L'article '" + y.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                                return;
                            }
                        }
                    }
                    for (YvsComContenuDocAchat y : selectContenu.getContenus()) {
                        dao.delete(y);
                    }
                    selectContenu.getContenus().clear();
                }
                dao.delete(selectContenu);
                docAchat.getContenus().remove(selectContenu);
                docAchat.getContenusSave().remove(selectContenu);
                selectDoc.getContenus().remove(selectContenu);
                int idx = documents.indexOf(new YvsComDocAchats(docAchat.getId()));
                if (idx > -1) {
                    documents.get(idx).getContenus().remove(selectContenu);
                }
                if (contenu.getId() == selectContenu.getId()) {
                    resetFicheArticle();
                }
                setMontantTotalDoc(docAchat);
                loadTaxesAchat(new YvsComDocAchats(docAchat.getId()));
                succes();
                update("data_article_bon_achat");
                update("data_facture_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanReglement() {
        try {
            if ((tabIds_mensualite != null) ? !tabIds_mensualite.equals("") : false) {
                String[] tab = tabIds_mensualite.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComptaCaissePieceAchat bean = docAchat.getReglements().get(docAchat.getReglements().indexOf(new YvsComptaCaissePieceAchat(id)));
                    boolean comptabilise = dao.isComptabilise(bean.getId(), Constantes.SCR_CAISSE_ACHAT);
                    if (comptabilise) {
                        getErrorMessage("Cette piece est déja comptabilisée");
                        return;
                    }
                    if (bean.getId() > 0) {
                        dao.delete(new YvsComptaCaissePieceAchat(bean.getId()));
                    }
                    docAchat.getReglements().remove(bean);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanReglement_(YvsComptaCaissePieceAchat y) {
        selectReglement = y;
    }

    public void deleteBeanReglement_() {
        try {
            if (selectReglement != null) {
                boolean comptabilise = dao.isComptabilise(selectReglement.getId(), Constantes.SCR_CAISSE_ACHAT);
                if (comptabilise) {
                    getErrorMessage("Cette piece est déja comptabilisée");
                    return;
                }
                if (selectReglement.getId() > 0) {
                    dao.delete(selectReglement);
                }
                docAchat.getReglements().remove(selectReglement);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanCout_(YvsComCoutSupDocAchat y) {
        selectCout = y;
    }

    public void deleteBeanCout_() {
        try {
            if (selectCout != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                docAchat.setMontantCS(docAchat.getMontantCS() - selectCout.getMontant());
                dao.delete(selectCout);
                docAchat.getCouts().remove(selectCout);
                if (cout.getId() == selectCout.getId()) {
                    resetFicheCout();
                }
                succes();
                update("blog_form_montant_doc_fa");
                update("tabview_livraison_achat:data_cout_facture_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteAnalytique(YvsComptaCentreContenuAchat y, boolean delete) {
        try {
            if (y != null) {
                if (!delete) {
                    selectAnalytique = y;
                    openDialog("dlgConfirmDeleteAnalytique");
                    return;
                }
                dao.delete(y);
                selectContenu.getAnalytiques().remove(y);
                if (analytique.getId() == y.getId()) {
                    resetFicheAnalytique();
                }
                update("data-analytique_fa");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedFactureAchat (deleteAnalytique)", ex);
        }
    }

    @Override
    public void onSelectDistant(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Factures Achat", "modGescom", "smenFactureAchat", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsComDocAchats y) {
        y.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidFactureAchat.findByFacture", new String[]{"facture"}, new Object[]{y}));
        selectDoc = y;
        populateView(UtilCom.buildBeanDocAchat(selectDoc));
        resetSubFiche();
        loadOthers(selectDoc);
        loadAllTranche(y.getDepotReception(), y.getDateLivraison());
        update("blog_form_facture_achat");
        update("grp_btn_etat_facture_achat");
        update("save_doc_achat");
        update("save_facture_achat");

    }

    public void onSelectDistantLivraison(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_BLA: {
                    ManagedLivraisonAchat s = (ManagedLivraisonAchat) giveManagedBean(ManagedLivraisonAchat.class);
                    if (s != null) {
                        s.onSelectObject(y);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Receptions Achat", "modGescom", "smenBonLivraisonAchat", true);
                        }
                    }
                    break;
                }

                case Constantes.TYPE_BRA: {
                    ManagedBonAvoirAchat s = (ManagedBonAvoirAchat) giveManagedBean(ManagedBonAvoirAchat.class);
                    if (s != null) {
                        s.onSelectObject(y, Constantes.TYPE_BRA);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Bon Retour Achat", "modGescom", "smenFactureRetourAchat", true);
                        }
                    }

                    break;
                }

                case Constantes.TYPE_FAA: {
                    ManagedBonAvoirAchat s = (ManagedBonAvoirAchat) giveManagedBean(ManagedBonAvoirAchat.class);
                    if (s != null) {
                        s.onSelectObject(y, Constantes.TYPE_FAA);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Factures Avoir Achat", "modCompta", "smenFactureAvoirAchat", true);
                        }
                    }

                    break;
                }
            }
        }
    }

    public void onPrintDistant(YvsComDocAchats y) {
        onPrintDistant(y, true);
    }

    public void onPrintDistant(YvsComDocAchats y, boolean withHeader) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_BLA: {
                    ManagedLivraisonAchat s = (ManagedLivraisonAchat) giveManagedBean(ManagedLivraisonAchat.class);
                    if (s != null) {
                        s.print(y, withHeader);
                    }
                    break;
                }
                case Constantes.TYPE_BRA:
                case Constantes.TYPE_FAA: {
                    ManagedBonAvoirAchat s = (ManagedBonAvoirAchat) giveManagedBean(ManagedBonAvoirAchat.class);
                    if (s != null) {
                        s.print(y, withHeader);
                    }
                    break;
                }
            }
        }
    }

    public void deleteBeanLie(YvsComDocAchats y) {
        docLie = y;
    }

    public void deleteBeanLie() {
        if (docLie != null ? docLie.getId() > 0 : false) {
            boolean correct = false;
            switch (docLie.getTypeDoc()) {
                case Constantes.TYPE_BLA: {
                    ManagedLivraisonAchat s = (ManagedLivraisonAchat) giveManagedBean(ManagedLivraisonAchat.class);
                    if (s != null) {
                        s.setSelectDoc(docLie);
                        correct = s.deleteBean_();
                    }
                    break;
                }
                case Constantes.TYPE_BRA: {
                    ManagedBonAvoirAchat s = (ManagedBonAvoirAchat) giveManagedBean(ManagedBonAvoirAchat.class);
                    if (s != null) {
                        s.setType(Constantes.TYPE_BRA);
                        s.setSelectDoc(docLie);
                        correct = s.deleteBean_();
                    }
                    break;
                }
                case Constantes.TYPE_FAA: {
                    ManagedBonAvoirAchat s = (ManagedBonAvoirAchat) giveManagedBean(ManagedBonAvoirAchat.class);
                    if (s != null) {
                        s.setType(Constantes.TYPE_FAA);
                        s.setSelectDoc(docLie);
                        correct = s.deleteBean_();
                    }
                    break;
                }
            }
            if (correct) {
                docAchat.getDocuments().remove(docLie);
                selectDoc.getDocuments().remove(docLie);
                int idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                }
                update("tabview_facture_achat:data_livraison_facture_achat");
            }
        }
    }

    public void onSelectDistantReglement(YvsComptaCaissePieceAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getModel().getTypeReglement()) {
                case Constantes.MODE_PAIEMENT_BANQUE: {
                    ManagedReglementAchat s = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                    if (s != null) {
                        s.onSelectObjectForCheque(y);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Suivi des chèques", "modCompta", "smenSuiviRegVente", true);
                        }
                    }

                    break;
                }

                case Constantes.MODE_PAIEMENT_COMPENSATION: {
                    ManagedReglementAchat s = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                    if (s != null) {
                        s.onSelectDistant(y);
                    }
                    break;
                }
            }
        }
    }

    public void onValideDistantLivraisonByForce() {
        if (distant != null ? distant.getId() > 0 : false) {
            switch (distant.getTypeDoc()) {
                case Constantes.TYPE_BLA: {
                    ManagedLivraisonAchat s = (ManagedLivraisonAchat) giveManagedBean(ManagedLivraisonAchat.class);
                    if (s != null) {
                        if (s.validerOrderByForce()) {
                            selectDoc = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docAchat.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_facture_achat");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_FAA:
                case Constantes.TYPE_BRA: {
                    ManagedBonAvoirAchat s = (ManagedBonAvoirAchat) giveManagedBean(ManagedBonAvoirAchat.class);
                    if (s != null) {

                    }
                    break;
                }
            }
        }
    }
    YvsComDocAchats distant;

    public void onValideDistantLivraison(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_BLA: {
                    ManagedLivraisonAchat s = (ManagedLivraisonAchat) giveManagedBean(ManagedLivraisonAchat.class);
                    if (s != null) {
                        if (s.validerOrder(y, false, false, true, null, false)) {
                            selectDoc = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docAchat.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_facture_achat");
                        }
                    }

                    break;
                }

                case Constantes.TYPE_FAA:
                case Constantes.TYPE_BRA: {
                    ManagedBonAvoirAchat s = (ManagedBonAvoirAchat) giveManagedBean(ManagedBonAvoirAchat.class);
                    if (s != null) {
                        if (s.validerOrder(y)) {
                            selectDoc = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            update("grp_btn_etat_facture_achat");
                        }
                    }

                    break;
                }
            }
        }
    }

    public void onRefuserDistantLivraison(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_BLA: {
                    ManagedLivraisonAchat s = (ManagedLivraisonAchat) giveManagedBean(ManagedLivraisonAchat.class);
                    if (s != null) {
                        if (s.refuserOrder(y, false, true, false, false)) {
                            selectDoc = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docAchat.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_facture_achat");
                        }
                    }

                    break;
                }

                case Constantes.TYPE_BRA:
                case Constantes.TYPE_FAA: {
                    ManagedBonAvoirAchat s = (ManagedBonAvoirAchat) giveManagedBean(ManagedBonAvoirAchat.class);
                    if (s != null) {
                        if (s.refuserOrder(y, true, false, false)) {
                            selectDoc = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                        }
                    }

                    break;
                }
            }
        }
    }

    public void onAnnulerDistantLivraisonByForce() {
        if (distant != null ? distant.getId() > 0 : false) {
            switch (distant.getTypeDoc()) {
                case Constantes.TYPE_BLA: {
                    ManagedLivraisonAchat s = (ManagedLivraisonAchat) giveManagedBean(ManagedLivraisonAchat.class);
                    if (s != null) {
                        if (s.annulerOrderByForce()) {
                            selectDoc = ((YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{selectDoc.getId()}));
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docAchat.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_facture_achat");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_BRA:
                case Constantes.TYPE_FAA: {
                    ManagedBonAvoirAchat s = (ManagedBonAvoirAchat) giveManagedBean(ManagedBonAvoirAchat.class);
                    if (s != null) {

                    }
                    break;
                }
            }
        }
    }

    public void onAnnulerDistantLivraison(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_BLA: {
                    ManagedLivraisonAchat s = (ManagedLivraisonAchat) giveManagedBean(ManagedLivraisonAchat.class);
                    if (s != null) {
                        if (s.annulerOrder(y, false, true, false, false)) {
                            selectDoc = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docAchat.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_facture_achat");
                        }
                    }

                    break;
                }

                case Constantes.TYPE_BRA:
                case Constantes.TYPE_FAA: {
                    ManagedBonAvoirAchat s = (ManagedBonAvoirAchat) giveManagedBean(ManagedBonAvoirAchat.class);
                    if (s != null) {
                        if (s.annulerOrder(y, true, false, false)) {
                            selectDoc = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                        }
                    }

                    break;
                }
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocAchats y = (YvsComDocAchats) ev.getObject();
            onSelectObject(y);
            tabIds = "" + documents.indexOf(y);
        }
    }

    public void displayContent(YvsComDocAchats y) {
        y.setContenus(dao.loadNameQueries("YvsComContenuDocAchat.findByDocAchat", new String[]{"docAchat"}, new Object[]{y}));
        for (YvsComContenuDocAchat c : y.getContenus()) {
            y.setMontantTaxe(y.getMontantTaxe() + c.getTaxe());
            y.setMontantTTC(y.getMontantTTC() + c.getPrixTotal());
        }
        update("dt_row_ex_" + y.getId());
    }

    public void loadOnViewContent(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocAchat y = (YvsComContenuDocAchat) ev.getObject();
            onSelectObject(y.getDocAchat());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche_();
        update("blog_form_facture_achat");
    }

    public void loadOnViewDoc(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocAchats bean = (YvsComDocAchats) ev.getObject();
            bon = UtilCom.buildBeanDocAchat(bean);
            cloneObject(docAchat, bon);
            docAchat.setDocumentLie(new DocAchat(bon.getId(), bon.getNumDoc(), bon.getStatut()));
            docAchat.setNumDoc(genererReference(Constantes.TYPE_FA_NAME, bon.getDateDoc()));
            docAchat.setDateDoc(new Date());
            docAchat.setStatut(Constantes.ETAT_EDITABLE);
            update("infos_bon");
        }
    }

    public void loadOnViewFsseur(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseFournisseur y = (YvsBaseFournisseur) ev.getObject();
            chooseFsseur(UtilCom.buildBeanFournisseur(y));
        }
    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocAchat y = (YvsComContenuDocAchat) ev.getObject();
            selectContenu = (YvsComContenuDocAchat) dao.loadOneByNameQueries("YvsComContenuDocAchat.findById", new String[]{"id"}, new Object[]{y.getId()});
            populateViewArticle(UtilCom.buildBeanContenuDocAchat(selectContenu));
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheArticle();
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildBeanArticles(bean));
            listArt = false;
        }
    }

    public void loadOnViewConditionnement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseConditionnement bean = (YvsBaseConditionnement) ev.getObject();
            chooseConditionnement(UtilProd.buildBeanConditionnement(bean));
            listArt = false;
        }
    }

    public void loadOnViewArticleBon(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectArt = true;
            YvsComContenuDocAchat bean = (YvsComContenuDocAchat) ev.getObject();
            ContenuDocAchat c = UtilCom.buildBeanContenuDocAchat(bean);
            chooseArticle(c.getArticle());
            update("form_contenu_facture_achat");
            update("desc_contenu_facture_achat");
        }
    }

    public void loadOnViewCout(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCoutSupDocAchat bean = (YvsComCoutSupDocAchat) ev.getObject();
            cloneObject(cout, UtilCom.buildBeanCoutSupDocAchat(bean));
            update("blog_form_cout_facture_achat");
        }
    }

    public void unLoadOnViewCout(UnselectEvent ev) {
        resetFicheCout();
        update("blog_form_cout_facture_achat");
    }

    public void loadOnViewMensualite(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectReglement = (YvsComptaCaissePieceAchat) ev.getObject();
            reglement = UtilCom.buildBeanPieceAchat(selectReglement);
        }
    }

    public void unLoadOnViewMensualite(UnselectEvent ev) {
        resetFicheReglement(false);
    }

    public void loadOnViewTranche(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsGrhTrancheHoraire t = (YvsGrhTrancheHoraire) ev.getObject();
            docAchat.setTranche(UtilGrh.buildTrancheHoraire(t));
            update("select_tranche_livraison_fa");
        }
    }

    public void loadOnViewAnalytique(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectAnalytique = (YvsComptaCentreContenuAchat) ev.getObject();
            analytique = UtilCompta.buildBeanCentreContenuAchat(selectAnalytique);
            update("form-analytique_fa");
        }
    }

    public void unLoadOnViewAnalytique(UnselectEvent ev) {
        resetFicheAnalytique();
    }

    public void init(boolean next) {
        loadAllFacture(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllFacture(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllFacture(true, true);
    }

    public void chooseBon() {
//        if (bon != null ? bon.getId() > 0 : false) {
//            YvsComDocAchats d_ = bons.get(bons.indexOf(new YvsComDocAchats(bon.getId())));
//            DocAchat d = UtilCom.buildBeanDocAchat(d_);
//            cloneObject(bon, d);
//            docAchat.setUpdate(false);
//            cloneObject(docAchat.getFournisseur(), bon.getFournisseur());
//            cloneObject(docAchat.getCategorieComptable(), bon.getFournisseur().getCategorieComptable());
//            loadContenusBon(d_);
//        } else {
//            if (bon != null ? bon.getId() < 0 : true) {
//                openDialog("dlgListBonCommande");
//            } else {
//                resetFiche();
//            }
//        }
        update("form_facture_achat");
        update("data_article_bon_achat");
    }

    @Override
    public void _chooseAgence() {
        super._chooseAgence();
        addParamAgenceDoc();
    }

    public void addParamAgenceDoc() {
        ParametreRequete p;
        if (agence_ > 0) {
            p = new ParametreRequete("y.agence", "agence", new YvsAgences(agence_));
            p.setOperation("=");
            p.setPredicat("AND");
            if (paginator.getParams().contains(new ParametreRequete("societe"))) {
                paginator.getParams().remove(new ParametreRequete("societe"));
            }
            if (paginator.getParams().contains(new ParametreRequete("depot"))) {
                paginator.getParams().remove(new ParametreRequete("depot"));
            }
        } else {
            p = new ParametreRequete("y.agence", "agence", null);
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    @Override
    public void _chooseDepot() {
        super._chooseDepot();
        ParametreRequete p;
        if (depot_ > 0) {
            p = new ParametreRequete("y.depotReception", "depot", new YvsBaseDepots(depot_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.depotReception", "depot", null);
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    public void _chooseTranche() {
        ParametreRequete p;
        if (tranche_ > 0) {
            p = new ParametreRequete("y.tranche", "tranche", new YvsGrhTrancheHoraire(tranche_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.tranche", "tranche", null);
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    public void chooseConditionnement(boolean isBonus) {
        if (!isBonus) {
            if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
                if (contenu.getArticle() != null) {
                    int idx = contenu.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(contenu.getConditionnement().getId()));
                    if (idx > -1) {
                        YvsBaseConditionnement y = contenu.getArticle().getConditionnements().get(idx);
                        contenu.setConditionnement(UtilProd.buildBeanConditionnement(y));
                    }
                }
                findInfosArticle(contenu.getConditionnement());
            }
        } else {
            if (bonus.getConditionnementBonus() != null ? bonus.getConditionnementBonus().getId() > 0 : false) {
                if (bonus.getArticle() != null) {
                    int idx = bonus.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(bonus.getConditionnementBonus().getId()));
                    if (idx > -1) {
                        YvsBaseConditionnement y = bonus.getArticle().getConditionnements().get(idx);
                        bonus.setConditionnementBonus(UtilProd.buildBeanConditionnement(y));
                    }
                }
            }
        }
    }

    public void chooseTrancheDepot() {
        if (docAchat.getTranche() != null ? docAchat.getTranche().getId() > 0 : false) {
            int idx = tranches.indexOf(new YvsGrhTrancheHoraire(docAchat.getTranche().getId()));
            if (idx > -1) {
                YvsGrhTrancheHoraire t = tranches.get(idx);
                docAchat.setTranche(UtilGrh.buildTrancheHoraire(t));
                update("select_tranche_livraison_fv");
            }
        }
    }

    private void findInfosArticle(Conditionnement c) {
        if (c != null) {
            contenu.getArticle().setPua(dao.getPua(contenu.getArticle().getId(), docAchat.getFournisseur().getId(), c.getId()));
            contenu.getArticle().setLastPua(contenu.getArticle().getPua());
            if (docAchat.getDepotReception() != null ? docAchat.getDepotReception().getId() > 0 : false) {
                contenu.getArticle().setPr(dao.getPr(contenu.getArticle().getId(), docAchat.getDepotReception().getId(), 0, docAchat.getDateDoc(), c.getId()));
                contenu.getArticle().setDateLastMvt((Date) dao.loadObjectByNameQueries("YvsBaseArticleDepot.findLastDateByArticleDepot", new String[]{"article", "depot"}, new Object[]{new YvsBaseArticles(contenu.getArticle().getId()), new YvsBaseDepots(docAchat.getDepotReception().getId())}));
            } else {
                contenu.getArticle().setDateLastMvt(null);
            }

            if (contenu != null ? contenu.getId() < 1 : false) {
                contenu.setPrixAchat(contenu.getArticle().getPua());
            }
            contenu.setConditionnement(c);
            String query = "SELECT MIN(y.puv) FROM yvs_base_conditionnement_point y INNER JOIN yvs_base_article_point a ON y.article = a.id INNER JOIN yvs_base_point_vente p ON a.point = p.id WHERE p.agence = ? AND y.conditionnement = ?";
            Double puv = (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(currentAgence.getId(), 1), new Options(c.getId(), 2)});
            if (puv != null ? puv > c.getPrix() : false) {
                contenu.getArticle().setPuv(puv);
            } else {
                contenu.getArticle().setPuv(c.getPrix());
            }
            if (!contenu.getArticle().getCategorie().equals(Constantes.CAT_SERVICE)) {
                if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
                    contenu.getArticle().setStock(dao.stocks(contenu.getArticle().getId(), 0, docAchat.getDepotReception().getId(), 0, 0, docAchat.getDateLivraison(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
                }
            }
            if (contenu.getQuantiteCommende() > 0) {
                onPrixBlur();
            }
        }
    }

    public void chooseArticle(Articles t) {
        if (((docAchat.getFournisseur() != null) ? docAchat.getFournisseur().getId() > 0 : false)) {
            if (!checkOperationArticle(t.getId(), docAchat.getDepotReception().getId(), Constantes.ACHAT)) {
                getWarningMessage("L'article '" + t.getDesignation() + "' ne fait pas d'achat dans le depot '" + docAchat.getDepotReception().getDesignation() + "'");
            }
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(t.getId())});
            t.setConditionnements(unites);
            t.setSelectArt(true);
            if (!isBonus) {
                cloneObject(contenu.getArticle(), t);
                if (unites != null ? !unites.isEmpty() : false) {
                    findInfosArticle(UtilProd.buildBeanConditionnement(unites.get(0)));
                } else {
                    findInfosArticle(null);
                }
            } else {
                cloneObject(bonus.getArticleBonus(), t);
            }
            if (!isBonus) {
                selectArt = t.isSelectArt();
                listArt = t.isListArt();
            }
        } else {
            getErrorMessage("Vous devez selectionner le fournisseur");
            resetFicheArticle();
        }
        update("form_contenu_facture_achat");
    }

    private void chooseConditionnement(Conditionnement c) {
        if (c != null ? c.getId() > 0 : false) {
            chooseArticle(c.getArticle());
            cloneObject(contenu.getConditionnement(), c);
            chooseConditionnement(false);
        }
    }

    public void choosTypeCout() {
        ManagedTypeCout w = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
        if (w != null) {
            if (cout.getType() != null) {
                if (cout.getType().getId() > 0) {
                    YvsGrhTypeCout d_ = w.getTypes().get(w.getTypes().indexOf(new YvsGrhTypeCout(cout.getType().getId())));
                    TypeCout d = UtilGrh.buildBeanTypeCout(d_);
                    cloneObject(cout.getType(), d);
                } else if (cout.getType().getId() == -1) {
                    w.setTypeCout(new TypeCout(Constantes.COUT_ACHAT));
                    openDialog("dlgAddTypeCout");
                }
            }
        }
    }

    public void chooseDepot() {
        if ((docAchat.getDepotReception() != null) ? docAchat.getDepotReception().getId() > 0 : false) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                YvsBaseDepots d = m.getDepots().get(m.getDepots().indexOf(new YvsBaseDepots(docAchat.getDepotReception().getId())));
                cloneObject(docAchat.getDepotReception(), UtilCom.buildBeanDepot(d));
                loadAllTranche(d, docAchat.getDateDoc());
            }
        }
    }

    public void chooseCentre() {
        if (analytique.getCentre() != null ? analytique.getCentre().getId() > 0 : false) {
            ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (service != null) {
                int idx = service.getCentres().indexOf(new YvsComptaCentreAnalytique(analytique.getCentre().getId()));
                if (idx > -1) {
                    YvsComptaCentreAnalytique y = service.getCentres().get(idx);
                    analytique.setCentre(UtilCompta.buildBeanCentreAnalytique(y));
                }
            }
        }
    }

    public void chooseDate() {
        dateLivraison = docAchat.getDateDoc();
        docAchat.setDateLivraison(docAchat.getDateDoc());
        update("chp_date_valid_bla");
        update("chp_date_a_valid_bla");
    }

    public void chooseCategorie() {
        if ((docAchat.getCategorieComptable() != null) ? docAchat.getCategorieComptable().getId() > 0 : false) {
            ManagedCatCompt m = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
            if (m != null) {
                int idx = m.getCategories().indexOf(new YvsBaseCategorieComptable(docAchat.getCategorieComptable().getId()));
                if (idx >= 0) {
                    YvsBaseCategorieComptable d = m.getCategories().get(idx);
                    cloneObject(docAchat.getCategorieComptable(), UtilCom.buildBeanCategorieComptable(d));
                }
            }
        } else {
            docAchat.setCategorieComptable(new CategorieComptable());
        }
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getCategorieComptable() != null ? !selectDoc.getCategorieComptable().getId().equals(docAchat.getCategorieComptable().getId()) : docAchat.getCategorieComptable().getId() > 0) {
                if (docAchat.getContenus() != null ? !docAchat.getContenus().isEmpty() : false) {
                    if (!autoriser("fa_change_categorie")) {
                        openNotAcces();
                        noChangeCategorie();
                        return;
                    }
                    openDialog("dlgConfirmChangeCategorie");
                }
            }
        }
    }

    public void chooseTypeAchat() {
        if (docAchat.getTypeAchat() != null ? docAchat.getTypeAchat().getId() > 0 : false) {
            ManagedTypeDocDivers w = (ManagedTypeDocDivers) giveManagedBean(ManagedTypeDocDivers.class);
            if (w != null) {
                int index = w.getTypesDocDivers().indexOf(new YvsBaseTypeDocDivers(docAchat.getTypeAchat().getId()));
                if (index > -1) {
                    YvsBaseTypeDocDivers y = w.getTypesDocDivers().get(index);
                    docAchat.setTypeAchat(UtilCompta.buildBeanTypeDoc(y));
                }
            }
        } else {
            docAchat.setTypeAchat(new TypeDocDivers());
        }
        update("infos_facture_achat");
    }

    public void setFournisseurDefaut() {
        if (docAchat.getFournisseur() != null ? docAchat.getFournisseur().getId() < 1 : true) {
            YvsBaseFournisseur c = currentFournisseurDefault();
            if (c != null ? c.getId() > 0 : false) {
                ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
                if (w != null) {
                    w.creance(c);
                }
                chooseFsseur(UtilCom.buildBeanFournisseur(c));
            }
            update("infos_facture_achat");
        }
    }

    public void noChangeCategorie() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            CategorieComptable d = UtilCom.buildBeanCategorieComptable(selectDoc.getCategorieComptable());
            cloneObject(docAchat.getCategorieComptable(), d);
            update("select_categorie_facture_achat");
        }
    }

    public void updatePrixArticle() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getCategorieComptable() != null ? !selectDoc.getCategorieComptable().getId().equals(docAchat.getCategorieComptable().getId()) : docAchat.getCategorieComptable().getId() > 0) {
                long newCategorie = docAchat.getCategorieComptable().getId();
                long oldCategorie = selectDoc.getCategorieComptable().getId();
                selectDoc.setCategorieComptable(UtilCom.buildCategorieComptable(docAchat.getCategorieComptable(), currentUser, currentAgence.getSociete()));
                String query = "UPDATE yvs_com_doc_achats SET categorie_comptable = ? WHERE id = ?";
                dao.requeteLibre(query, new Options[]{new Options(newCategorie, 1), new Options(docAchat.getId(), 2)});
                for (int i = 0; i < docAchat.getContenus().size(); i++) {
                    YvsComContenuDocAchat c = docAchat.getContenus().get(i);
                    for (YvsComTaxeContenuAchat t : c.getTaxes()) {
                        dao.delete(t);
                    }
                    c.getTaxes().clear();
                    double oldTaxe = saveAllTaxe(c, docAchat, selectDoc, oldCategorie, false);
                    double newTaxe = saveAllTaxe(c, docAchat, selectDoc, newCategorie, true);
                    c.setPrixTotal(c.getPrixTotal() - oldTaxe + newTaxe);
                    dao.update(c);
                    int idx = selectDoc.getContenus().indexOf(c);
                    if (idx > -1) {
                        selectDoc.getContenus().set(idx, c);
                    }
                }
                int idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                }
                update("tabview_facture_achat:data_contenu_facture_achat");
            }
        }
    }

    public void chooseCloturer(ValueChangeEvent ev) {
        cloturer_ = ((Boolean) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("y.cloturer", "cloturer", cloturer_);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    public void chooseStatut(ValueChangeEvent ev) {
        String statut = ((String) ev.getNewValue());
        if (statut != null ? statut.trim().equals("Z") : false) {
            openDialog("dlgMoreStatuts");
        } else {
            statut_ = statut;
            addParamStatut();
        }
    }

    public void addParamStatut() {
        addParamStatut(true);
    }

    public void addParamStatut(boolean load) {
        ParametreRequete p;
        if (statut_ != null ? statut_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statut", "statut", statut_, egaliteStatut, "AND");
        } else {
            p = new ParametreRequete("statut", "statut", null);
        }
        paginator.addParam(p);
        if (load) {
            if (searchAutomatique) {
                loadAllFacture(true, true);
            }
        }
    }

    public void chooseStatuts() {
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null);
        if (statuts != null ? !statuts.isEmpty() : false) {
            boolean add = true;
            for (String s : statuts) {
                if (s != null ? s.trim().length() < 1 : true) {
                    add = false;
                    break;
                }
            }
            if (add) {
                p = new ParametreRequete("y.statut", "statut", statuts, "IN", "AND");
            }
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllFacture(true, true);
    }

    public void addParamToValide() {
        ParametreRequete p = new ParametreRequete("(y.etapeValide+1)", "etape", null, "IN", "AND");
        if (toValideLoad) {
            List<Integer> lnum = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepe", new String[]{"document", "niveau"}, new Object[]{Constantes.DOCUMENT_FACTURE_ACHAT, currentUser.getUsers().getNiveauAcces()});
            if ((lnum != null) ? !lnum.isEmpty() : false) {
                p = new ParametreRequete("(y.etapeValide+1)", "etape", lnum, "IN", "AND");
            }
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    public void chooseStatutRegle(ValueChangeEvent ev) {
        statutRegle_ = ((String) ev.getNewValue());
        addParamStatutRegle();
    }

    public void addParamStatutRegle() {
        ParametreRequete p;
        if (statutRegle_ != null ? statutRegle_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutRegle", "statutRegle", statutRegle_, egaliteStatutR, "AND");
        } else {
            p = new ParametreRequete("y.statutRegle", "statutRegle", null);
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    public void chooseStatutLivre(ValueChangeEvent ev) {
        statutLivre_ = ((String) ev.getNewValue());
        addParamStatutLivre();
    }

    public void addParamStatutLivre() {
        ParametreRequete p;
        if (statutLivre_ != null ? statutLivre_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutLivre", "statutLivre", statutLivre_, egaliteStatutL, "AND");
        } else {
            p = new ParametreRequete("y.statutLivre", "statutLivre", null);
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    public void addParamAgences() {
        ParametreRequete p = new ParametreRequete("y.agence", "agence", null, "=", "AND");
        if (agenceSearch != null ? agenceSearch > 0 : false) {
            p = new ParametreRequete("y.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void chooseDateSearch() {
        ParametreRequete p;
        if (date_) {
            p = new ParametreRequete("y.dateDoc", "dateDoc", dateDebut_);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFin_);
        } else {
            p = new ParametreRequete("y.dateDoc", "dateDoc", null);
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    public DocAchat searchFacture(String num, Fournisseur c, boolean open) {
        DocAchat a = new DocAchat();
        a.setNumDoc(num);
        a.setError(true);
        if (num != null ? num.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete("UPPER(y.numDoc)", "numDoc", num.trim().toUpperCase() + "%", "LIKE", "AND");
            paginator.addParam(p);
        } else {
            paginator.addParam(new ParametreRequete("y.numDoc", "numDoc", null));
        }
        if (c != null ? c.getId() > 0 : false) {
            codeFsseur_ = c.getCodeFsseur();
            addParamFsseur();
        } else {
            loadAllFacture(true, true);
        }
        a = chechFactureResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setNumDoc(num);
            a.setError(true);
        }
        return a;
    }

    private DocAchat chechFactureResult(boolean open) {
        DocAchat a = new DocAchat();
        if (documents != null ? !documents.isEmpty() : false) {
            if (documents.size() > 1) {
                if (open) {
                    openDialog("dlgListFactures");
                }
                a.setList(true);
            } else {
                YvsComDocAchats c = documents.get(0);
                a = UtilCom.buildBeanDocAchat(c);
            }
            a.setError(false);
        }
        return a;
    }

    public void addParamFsseur() {
        ParametreRequete p = new ParametreRequete("y.fournisseur.codeFsseur", "fournisseur", null);
        if (codeFsseur_ != null ? codeFsseur_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "fournisseur", codeFsseur_.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.fournisseur.codeFsseur)", "fournisseur", codeFsseur_.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.fournisseur.nom)", "fournisseur", codeFsseur_.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    public void onPrixBlur() {
        findPrixArticle(contenu);
        contenu.setPrixTotalRecu(contenu.getPrixTotalRecu() > 0 ? contenu.getPrixTotalRecu() : 0);
        contenu.setPrixTotalAttendu(contenu.getPrixTotalRecu());
    }

    public void onRemiseBlur() {
        double total = contenu.getQuantiteCommende() * contenu.getPrixAchat();
        contenu.setPrixTotalRecu(total - contenu.getRemiseRecu());
        long categorie = returnCategorie(docAchat, selectDoc);
        if (categorie > 0) {
            contenu.setTaxe(dao.getTaxe(contenu.getArticle().getId(), categorie, 0, contenu.getRemiseRecu(), contenu.getQuantiteCommende(), contenu.getPrixAchat(), false, docAchat.getFournisseur().getId()));
            String query = "SELECT pua_ttc FROM yvs_base_article_fournisseur WHERE article = ? AND fournisseur = ?";
            Boolean pua_ttc = (Boolean) dao.loadObjectBySqlQuery(query, new yvs.dao.Options[]{new yvs.dao.Options(contenu.getArticle().getId(), 1), new yvs.dao.Options(docAchat.getFournisseur().getId(), 2)});
            if (pua_ttc == null) {
                pua_ttc = contenu.getArticle().isPuaTtc();
            }
            contenu.setPrixTotalRecu(contenu.getPrixTotalRecu() + (pua_ttc ? 0 : contenu.getTaxe()));
        } else {
            getWarningMessage("Selectionner la catégorie comptable!");
        }
        contenu.setPrixTotalRecu(contenu.getPrixTotalRecu() > 0 ? contenu.getPrixTotalRecu() : 0);
    }

    public void onQuantiteBlur() {
        findPrixArticle(contenu);
    }

    public void findPrixArticle(ContenuDocAchat c) {
        findPrixArticle(c, selectDoc);
    }

    public void findPrixArticle(ContenuDocAchat c, YvsComDocAchats doc) {
        findPrixArticle(c, selectContenu, doc, true);
    }

    public void findPrixArticle(ContenuDocAchat c, YvsComContenuDocAchat y, YvsComDocAchats doc, boolean msg) {
        double total = c.getQuantiteCommende() * c.getPrixAchat();
        double _remise = dao.getRemiseAchat(c.getArticle().getId(), c.getQuantiteCommende(), c.getPrixAchat(), docAchat.getFournisseur().getId());
        if (y != null ? y.getId() > 0 : false) {
            if (c.getId() > 0 && (y.getQuantiteRecu() == c.getQuantiteCommende())) {
                _remise = y.getRemise();
            }
        }

        c.setRemiseRecu(_remise);
        c.setPrixTotalRecu(total - c.getRemiseRecu());
        long categorie = returnCategorie(docAchat, doc);
        if (categorie > 0) {
            c.setTaxe(dao.getTaxe(c.getArticle().getId(), categorie, 0, c.getRemiseRecu(), c.getQuantiteCommende(), c.getPrixAchat(), false, docAchat.getFournisseur().getId()));
            String query = "SELECT pua_ttc FROM yvs_base_article_fournisseur WHERE article = ? AND fournisseur = ?";
            Boolean pua_ttc = (Boolean) dao.loadObjectBySqlQuery(query, new yvs.dao.Options[]{new yvs.dao.Options(c.getArticle().getId(), 1), new yvs.dao.Options(docAchat.getFournisseur().getId(), 2)});
            if (pua_ttc == null) {
                pua_ttc = c.getArticle().isPuaTtc();
            }
            c.setPrixTotalRecu(c.getPrixTotalRecu() + (pua_ttc ? 0 : c.getTaxe()));
        } else {
            if (msg) {
                getWarningMessage("Selectionner la catégorie comptable!");
            }
        }
    }

    private long returnCategorie(DocAchat docAchat, YvsComDocAchats selectDoc) {
        long categorie = 0;
        if (selectDoc != null ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
            if (selectDoc.getCategorieComptable() != null ? selectDoc.getCategorieComptable().getId() > 0 : false) {
                categorie = selectDoc.getCategorieComptable().getId();
            }
        } else {
            if (docAchat.getCategorieComptable() != null ? docAchat.getCategorieComptable().getId() > 0 : false) {
                categorie = docAchat.getCategorieComptable().getId();
            }
        }
        return categorie;
    }

    public void onRemiseBlur(boolean taux) {
        double total = selectContenu.getQuantiteRecu() * (selectContenu.getPrixAchat());
        if (taux) {
            if (taux_remise > 100) {
                getErrorMessage("Le taux ne peut pas excéder 100%");
                onRemiseBlur(false);
                return;
            }
            montant_remise = (total * taux_remise) / 100;
        } else {
            taux_remise = (montant_remise * 100) / total;
        }
    }

    public void findContenusByArticle() {
        docAchat.getContenus().clear();
        if (Util.asString(articleContenu)) {
            for (YvsComContenuDocAchat c : docAchat.getContenusSave()) {
                if (c.getArticle().getRefArt().toUpperCase().contains(articleContenu.toUpperCase()) || c.getArticle().getDesignation().toUpperCase().contains(articleContenu.toUpperCase())) {
                    docAchat.getContenus().add(c);
                }
            }
        } else {
            docAchat.getContenus().addAll(docAchat.getContenusSave());
        }
    }

    public void findContenusErrorComptabilite() {
        YvsBaseArticleCategorieComptable y;
        for (YvsComContenuDocAchat c : docAchat.getContenus()) {
            c.setErrorComptabilise(true);
            Long count = (Long) dao.loadObjectByNameQueries("YvsBaseArticleCategorieComptable.countByCategorieArticle", new String[]{"categorie", "article"}, new Object[]{selectDoc.getCategorieComptable(), c.getArticle()});
            if (count != null ? count > 0 : false) {
                if (count == 1) {
                    y = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries("YvsBaseArticleCategorieComptable.findByCategorieArticle", new String[]{"categorie", "article"}, new Object[]{selectDoc.getCategorieComptable(), c.getArticle()});
                    if (y != null ? (y.getId() > 0 ? (y.getCompte() != null ? y.getCompte().getId() > 0 : false) : false) : false) {
                        c.setErrorComptabilise(false);
                        c.setCompte(y.getCompte());
                    }
                } else {
                    c.setMessageError("Cet article est rattaché plusieurs fois la catégorie comptable de la facture");
                }
            } else {
                c.setMessageError("Cet article n'est pas bien paramètré avec cette catégorie comptable pour la comptabilité");
            }
        }
        update("tabview_facture_achat:data_contenu_facture_achat");
    }

    public void findAllContenusErrorComptabilite() {
        YvsBaseArticleCategorieComptable y;
        for (YvsComContenuDocAchat c : all_contenus) {
            c.setErrorComptabilise(true);
            Long count = (Long) dao.loadObjectByNameQueries("YvsBaseArticleCategorieComptable.countByCategorieArticle", new String[]{"categorie", "article"}, new Object[]{c.getDocAchat().getCategorieComptable(), c.getArticle()});
            if (count != null ? count > 0 : false) {
                if (count == 1) {
                    y = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries("YvsBaseArticleCategorieComptable.findByCategorieArticle", new String[]{"categorie", "article"}, new Object[]{c.getDocAchat().getCategorieComptable(), c.getArticle()});
                    if (y != null ? (y.getId() > 0 ? (y.getCompte() != null ? y.getCompte().getId() > 0 : false) : false) : false) {
                        c.setErrorComptabilise(false);
                        c.setCompte(y.getCompte());
                    }
                } else {
                    c.setMessageError("Cet article est rattaché plusieurs fois la catégorie comptable de la facture");
                }
            } else {
                c.setMessageError("Cet article n'est pas bien paramètré avec cette catégorie comptable pour la comptabilité");
            }
        }
        update("data_contenu_fa");
    }

    public void sendByMail() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            YvsBaseFournisseur fsseur = selectDoc.getFournisseur();
            String email = fsseur.getTiers().getEmail();
            if ((email != null) ? !email.equals("") : false) {
                if (Util.correctEmail(email)) {
                    changeStatut(Constantes.ETAT_SOUMIS, selectDoc);
                } else {
                    getErrorMessage("Impossible d'envoyer! Email Incorrect");
                }
            } else {
                getErrorMessage("Impossible d'envoyer! Le fournisseur n'a pas d'email");
            }
        }
    }

    public void annulerOrder(boolean force, boolean passe) {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                if (!passe) {
                    List<YvsComDocAchats> l = dao.loadNameQueries("YvsComDocAchats.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                    for (YvsComDocAchats d : l) {
                        dao.delete(d);
                    }
                }
                cancelEtapeFacture(force, false, passe);
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            System.err.println("Erreur : " + ex.getMessage());
        }
    }

    public void refuserOrder(boolean force, boolean passe) {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                if (!passe) {
                    List<YvsComDocAchats> l = dao.loadNameQueries("YvsComDocAchats.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                    for (YvsComDocAchats d : l) {
                        dao.delete(d);
                    }
                }
                cancelEtapeFacture(force, true, passe);
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("Doc Achat Error...", ex);
        }
    }

    List<Long> unitesMaj = new ArrayList<>();

    public boolean validerOrder() {
        return validerOrder(docAchat, selectDoc);
    }

    public boolean validerOrder(DocAchat docAchat, YvsComDocAchats selectDoc) {
        if (!autoriser("fa_valide_doc")) {
            openNotAcces();
            return false;
        }
        if (selectDoc == null) {
            return false;
        }
        if (!verifyOperation(docAchat.getDepotReception(), Constantes.ENTREE, Constantes.ACHAT, true)) {
            return false;
        }
        if (docAchat.getMontantNetApayer() < 0) {
            getErrorMessage("Vous ne pouvez pas valider cette facture...car la somme des reglements est superieure au montant de la facture");
            return false;
        }
        if (docAchat.getContenus().isEmpty()) {
            getErrorMessage("Vous ne pouvez valider un document vide !");
            return false;
        }
        for (YvsComContenuDocAchat c : docAchat.getContenus()) {
            if (c.getPrixAchat() <= 0) {
                getErrorMessage("Vous devez precisez le prix d'achat de l'article " + c.getArticle().getDesignation());
                return false;
            }
        }
        docAchat.setLivraisonDo(true);

        if (changeStatut_(Constantes.ETAT_VALIDE, docAchat, selectDoc)) {
            selectDoc.setCloturer(false);
            selectDoc.setAnnulerBy(null);
            selectDoc.setValiderBy(currentUser.getUsers());
            selectDoc.setDateAnnuler(null);
            selectDoc.setCloturerBy(null);
            selectDoc.setDateCloturer(null);
            selectDoc.setDateValider(new Date());
            selectDoc.setAuthor(currentUser);
            dao.update(selectDoc);
            for (YvsComContenuDocAchat c : docAchat.getContenus()) {
                c.setAuthor(currentUser);
                c.setStatut(Constantes.ETAT_VALIDE);
                if (selectDoc.getContenus().contains(c)) {
                    selectDoc.getContenus().set(selectDoc.getContenus().indexOf(c), c);
                }
                dao.update(c);
                unitesMaj.clear();
                majPrixVente(c.getConditionnement(), c.getPrixAchat(), docAchat, selectDoc);
            }
            if (documents.contains(selectDoc)) {
                documents.set(documents.indexOf(selectDoc), selectDoc);
            }

            if (currentParamAchat == null) {
                currentParamAchat = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (currentParamAchat != null) {
                if (currentParamAchat.getComptabilisationAuto()) {
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        w.comptabiliserAchat(selectDoc, false, false);
                    }
                }
            }
            update("tabview_facture_achat:data_contenu_facture_achat");
            return true;
        }
        return false;
    }

    private void majPrixVente(YvsBaseConditionnement c, double prixAchat) {
        majPrixVente(c, prixAchat, docAchat, selectDoc);
    }

    private void majPrixVente(YvsBaseConditionnement c, double prixAchat, DocAchat docAchat, YvsComDocAchats selectDoc) {
        try {
            if (c == null) {
                return;
            }
            if (unitesMaj.contains(c.getId())) {
                return;
            }
            unitesMaj.add(c.getId());
            List<Object[]> data;
            String query = "SELECT COUNT(y.id) FROM yvs_com_doc_achats y INNER JOIN yvs_com_contenu_doc_achat c ON c.doc_achat = y.id WHERE y.type_doc = 'FA' AND y.date_doc > ? AND y.id != ? AND c.conditionnement = ?";
            Long count = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options(docAchat.getDateDoc(), 1), new Options(docAchat.getId(), 2), new Options(c.getId(), 3)});
            if (count != null ? count < 1 : true) {
                query = "SELECT y.id, y.taux_pua FROM yvs_base_conditionnement_point y INNER JOIN yvs_base_article_point a ON y.article = a.id INNER JOIN yvs_base_point_vente p ON a.point = p.id WHERE y.proportion_pua IS TRUE AND COALESCE(y.taux_pua, 0) > 0 AND y.conditionnement = ? AND p.agence = ?";
                data = dao.loadListBySqlQuery(query, new Options[]{new Options(c.getId(), 1), new Options(selectDoc.getAgence().getId(), 2)});
                for (Object[] point : data) {
                    double prix = prixAchat + ((prixAchat * (Double) point[1]) / 100);
                    query = "UPDATE yvs_base_conditionnement_point SET puv = ? WHERE id = ?";
                    dao.requeteLibre(query, new Options[]{new Options(prix, 1), new Options((Long) point[0], 2)});
                }
                query = "SELECT y.id, y.taux_pua FROM yvs_base_conditionnement y WHERE y.proportion_pua IS TRUE AND COALESCE(y.taux_pua, 0) > 0 AND y.id = ?";
                data = dao.loadListBySqlQuery(query, new Options[]{new Options(c.getId(), 1)});
                for (Object[] point : data) {
                    double prix = prixAchat + ((prixAchat * (Double) point[1]) / 100);
                    query = "UPDATE yvs_base_conditionnement SET prix = ? WHERE id = ?";
                    dao.requeteLibre(query, new Options[]{new Options(prix, 1), new Options((Long) point[0], 2)});
                }
            }
            if (c.getUnite() != null && c.getArticle() != null) {
                query = "SELECT y.id, y.taux_pua, u.id, y.article, COALESCE(t.taux_change, 0) FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id INNER JOIN yvs_base_table_conversion t On u.id = t.unite_equivalent WHERE y.proportion_pua IS TRUE AND COALESCE(y.taux_pua, 0) > 0 AND t.unite != t.unite_equivalent AND t.unite = ? AND y.article = ?";
                data = dao.loadListBySqlQuery(query, new Options[]{new Options(c.getUnite().getId(), 1), new Options(c.getArticle().getId(), 2)});
                if (data != null ? !data.isEmpty() : false) {
                    for (Object[] point : data) {
                        double pua = prixAchat / (Double) point[4];
                        majPrixVente(new YvsBaseConditionnement((Long) point[0], new YvsBaseArticles((Long) point[3]), new YvsBaseUniteMesure((Long) point[2])), pua);
                    }
                }
                query = "SELECT y.id, y.taux_pua, u.id, y.article, COALESCE(t.taux_change, 0) FROM yvs_base_conditionnement y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id INNER JOIN yvs_base_table_conversion t On u.id = t.unite WHERE y.proportion_pua IS TRUE AND COALESCE(y.taux_pua, 0) > 0 AND t.unite != t.unite_equivalent AND t.unite_equivalent = ? AND y.article = ?";
                data = dao.loadListBySqlQuery(query, new Options[]{new Options(c.getUnite().getId(), 1), new Options(c.getArticle().getId(), 2)});
                if (data != null ? !data.isEmpty() : false) {
                    for (Object[] point : data) {
                        double pua = prixAchat * (Double) point[4];
                        majPrixVente(new YvsBaseConditionnement((Long) point[0], new YvsBaseArticles((Long) point[3]), new YvsBaseUniteMesure((Long) point[2])), pua);
                    }
                }
            }
        } catch (Exception ex) {
            getException("Error on majPrixVente", ex);
        }
    }

    public void validerOrderAll() {
        try {
            if (!autoriser("fa_valide_doc")) {
                openNotAcces();
                return;
            }
            if (currentParamAchat == null) {
                currentParamAchat = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                long succes = 0;
                for (Long ids : l) {
                    if (validerOrderOne(documents.get(ids.intValue()), false)) {
                        succes++;
                    }
                }
                if (succes > 0) {
                    succes();
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public boolean validerOrderOne(YvsComDocAchats entity, boolean succes) {
        try {
            if (!autoriser("fa_valide_doc")) {
                openNotAcces();
                return false;
            }
            if (currentParamAchat == null) {
                currentParamAchat = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (!entity.getStatut().equals(Constantes.ETAT_VALIDE)) {
                DocAchat bean = UtilCom.buildBeanDocAchat(entity);
                setMontantTotalDoc(bean, entity);
                entity.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidFactureAchat.findByFacture", new String[]{"facture"}, new Object[]{entity}));
                bean.setEtapesValidations(ordonneEtapes(entity.getEtapesValidations()));
                boolean response = false;
                if (bean.getEtapesValidations() != null ? bean.getEtapesValidations().isEmpty() : true) {
                    response = validerOrder(bean, entity, currentParamAchat);
                } else {
                    for (YvsWorkflowValidFactureAchat r : bean.getEtapesValidations()) {
                        if (!r.getEtapeValid()) {
                            etape = r;
                            break;
                        }
                    }
                    if (etape != null ? etape.getId() > 0 : false) {
                        response = validEtapeFacture(bean, entity, etape, false, false, dao);
                    } else {
                        response = validerOrder(bean, entity, currentParamAchat);
                    }
                }
                if (response && succes) {
                    succes();
                }
                return response;
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
        return false;
    }

    public boolean validerOrder(DocAchat docAchat, YvsComDocAchats selectDoc, YvsComParametreAchat currentParamAchat) {
        if (selectDoc == null) {
            return false;
        }
        if (selectDoc.getContenus().isEmpty()) {
            getErrorMessage("Vous ne pouvez valider un document vide !");
            return false;
        }
        for (YvsComContenuDocAchat c : selectDoc.getContenus()) {
            if (c.getPrixAchat() <= 0) {
                getErrorMessage("Vous devez precisez le prix d'achat de l'article " + c.getArticle().getDesignation());
                return false;
            }
        }
        selectDoc.setLivraisonDo(true);
        if (currentParamAchat == null) {
            currentParamAchat = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        if (changeStatut_(Constantes.ETAT_VALIDE, docAchat, selectDoc)) {
            selectDoc.setCloturer(false);
            selectDoc.setAnnulerBy(null);
            selectDoc.setValiderBy(currentUser.getUsers());
            selectDoc.setDateAnnuler(null);
            selectDoc.setCloturerBy(null);
            selectDoc.setDateCloturer(null);
            selectDoc.setDateValider(new Date());
            selectDoc.setAuthor(currentUser);
            dao.update(selectDoc);

            for (YvsComContenuDocAchat c : selectDoc.getContenus()) {
                c.setAuthor(currentUser);
                c.setStatut(Constantes.ETAT_VALIDE);
                if (selectDoc.getContenus().contains(c)) {
                    selectDoc.getContenus().set(selectDoc.getContenus().indexOf(c), c);
                }
                dao.update(c);
            }
            if (documents.contains(selectDoc)) {
                documents.set(documents.indexOf(selectDoc), selectDoc);
            }

            if (currentParamAchat != null) {
                if (currentParamAchat.getComptabilisationAuto()) {
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        w.comptabiliserAchat(selectDoc, false, false);
                    }
                }
            }
            update("tabview_facture_achat:data_contenu_facture_achat");
            return true;
        }
        return false;
    }

    private boolean controlContentForTransmis(YvsComContenuDocAchat c, YvsBaseDepots depot, boolean message) {
        champ = new String[]{"article", "depot"};
        val = new Object[]{c.getArticle(), depot};
        YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
        if (y != null ? y.getId() < 1 : true) {
            if (message) {
                getErrorMessage("Impossible d'effectuer cette action... Car le depot " + depot.getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
            }
            return false;
        }
        if (y.getRequiereLot() ? (c.getLot() != null ? c.getLot().getNumero() != null ? c.getLot().getNumero().trim().length() < 1 : true : true) : false) {
            if (message) {
                getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le depot " + depot.getDesignation());
            }
            return false;
        }
        c.setRequiereLot(y.getRequiereLot());
        if (!checkOperationArticle(c.getArticle().getId(), depot.getId(), Constantes.ACHAT)) {
            if (message) {
                getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' ne fait pas d'achat dans le depot '" + docAchat.getDepotReception().getDesignation() + "'");
            }
            return false;
        }
        return true;
    }

    public void transmisOrderByForce() {
        transmisOrder(true);
    }

    public void transmisOrder() {
        transmisOrder(false);
    }
    boolean exist_inventaire;

    public void transmisOrder(boolean force) {
        selectDoc.setDepotReception(UtilProd.buildBeanDepot(docAchat.getDepotReception()));
        selectDoc.setTranche(UtilGrh.buildTrancheHoraire(docAchat.getTranche(), currentUser));
        selectDoc.setDateLivraison(docAchat.getDateLivraison());
        transmisOrder(selectDoc, dateLivraison, statutLivraison, true, force);
        docAchat.setStatutLivre(selectDoc.getStatutLivre());
        docAchat.setDocuments(selectDoc.getDocuments());
        docAchat.setEtapesValidations(ordonneEtapes(selectDoc.getEtapesValidations()));
        update("infos_document_facture_achat");
        update("grp_btn_etat_facture_achat");
        update("tabview_facture_achat");
    }

    public void transmisOrder(YvsComDocAchats facture, Date dateLivraison, String statut, boolean message, boolean force) {
        if (!force) {
            if (facture == null) {
                if (message) {
                    getErrorMessage("Vous devez selectionner la facture");
                }
                return;
            }
            if (facture.getDepotReception() != null ? facture.getDepotReception().getId() < 1 : true) {
                if (message) {
                    getErrorMessage("Aucun dépôt de reception n'a été trouvé !");
                }
                return;
            }
            if (facture.getTranche() != null ? facture.getTranche().getId() < 1 : true) {
                List<YvsGrhTrancheHoraire> list = loadTranche(facture.getDepotReception(), dateLivraison);
                if (list != null ? list.size() == 1 : false) {
                    facture.setTranche(list.get(0));
                } else {
                    if (message) {
                        getErrorMessage("Aucune tranche de livraison n'a été trouvé !");
                    }
                    return;
                }
            } else if (!Util.asString(facture.getTranche().getTypeJournee())) {
                facture.setTranche((YvsGrhTrancheHoraire) dao.loadOneByNameQueries("YvsGrhTrancheHoraire.findById", new String[]{"id"}, new Object[]{facture.getTranche().getId()}));
            }
            if (dateLivraison != null ? dateLivraison.after(new Date()) : true) {
                if (message) {
                    getErrorMessage("La date de livraison est incorrecte !");
                }
                return;
            }
            if (!verifyDateAchat(dateLivraison, false)) {
                return;
            }
//            boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
//            exist_inventaire = !verifyInventaire(facture.getDepotReception(), facture.getTranche(), dateLivraison, (gescom_update_stock_after_valide ? false : message));
//            if (exist_inventaire) {
//                if (!gescom_update_stock_after_valide) {
//                    return;
//                } else if (!force) {
//                    openDialog("dlgConfirmChangeInventaire");
//                    return;
//                }
//            }
        }
        String num = genererReference(Constantes.TYPE_BLA_NAME, dateLivraison, facture.getDepotReception().getId());
        if (num != null ? num.trim().length() > 0 : false) {
            List<YvsComContenuDocAchat> contenus = new ArrayList<>(docAchat.getContenus());
            //charge les restes à livrer
            List<YvsComContenuDocAchat> l = loadContenusStay(facture, Constantes.TYPE_BLA);
            if (l != null ? !l.isEmpty() : false) {
                if (!verifyTranche(facture.getTranche(), facture.getDepotReception(), dateLivraison)) {
                    return;
                }
                List<YvsComContenuDocAchat> list = new ArrayList<>();
                for (YvsComContenuDocAchat c : l) {
                    if (!controlContentForTransmis(c, facture.getDepotReception(), message)) {
                        return;
                    }
                    list.add(c);
                    if (c.getQuantiteBonus() > 0) {
                        YvsComContenuDocAchat a = new YvsComContenuDocAchat(c);
                        a.setArticle(new YvsBaseArticles(c.getArticleBonus().getId(), c.getArticleBonus().getRefArt(), c.getArticleBonus().getDesignation()));
                        a.setConditionnement(new YvsBaseConditionnement(c.getConditionnementBonus().getId(), new YvsBaseUniteMesure(c.getConditionnementBonus().getUnite().getId(), c.getConditionnementBonus().getUnite().getReference(), c.getConditionnementBonus().getUnite().getLibelle())));
                        a.setQuantiteRecu(c.getQuantiteBonus());
                        a.setArticleBonus(null);
                        a.setConditionnementBonus(null);
                        a.setQuantiteBonus(0.0);
                        a.setPrixAchat(0.0);
                        a.setPrixTotal(0.0);
                        a.setTaxe(0.0);
                        if (!controlContentForTransmis(a, facture.getDepotReception(), message)) {
                            return;
                        }
                        list.add(a);
                    }
                }
                distant = new YvsComDocAchats(facture);
                distant.setDateSave(new Date());
                distant.setAuthor(currentUser);
                distant.setAgence(facture.getAgence());
                distant.setValiderBy(currentUser.getUsers());
                distant.setTypeDoc(Constantes.TYPE_BLA);
                distant.setNumDoc(num);
                distant.setNumPiece("BL N° " + facture.getNumDoc());
                distant.setDepotReception(new YvsBaseDepots(facture.getDepotReception().getId()));
                distant.setTranche(new YvsGrhTrancheHoraire(facture.getTranche().getId()));
                distant.setDateDoc(dateLivraison);
                distant.setDateLivraison(dateLivraison);
                distant.setDocumentLie(new YvsComDocAchats(facture.getId()));
                distant.setCloturer(false);
                distant.setStatut(statut);
                distant.setStatutLivre(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                distant.setStatutRegle(Constantes.ETAT_ATTENTE);
                distant.setDescription("Reception de la facture N° " + facture.getNumDoc() + " le " + ldf.format(dateLivraison) + " à " + time.format(dateLivraison));
                distant.getContenus().clear();
                distant.setId(null);
                distant = (YvsComDocAchats) dao.save1(distant);
                ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                for (YvsComContenuDocAchat c : list) {
                    long id = c.getId();
                    c.setExterne(null);
                    c.setDateLivraison(distant.getDateLivraison());
                    c.setDocAchat(distant);
                    c.setStatut(Constantes.ETAT_VALIDE);
                    c.setParent(new YvsComContenuDocAchat(c.getId()));
                    c.setAuthor(currentUser);
                    if (c.isRequiereLot() && m != null) {
                        if (c.getLot() != null ? c.getLot().getId() < 1 : false) {
                            c.setLot(m._saveNew(c.getLot().getNumero(), new Articles(c.getArticle().getId(), c.getArticle().getRefArt(), c.getArticle().getDesignation()), c.getLot().getDateFabrication(), c.getLot().getDateExpiration()));
                        }
                        if (c.getLot() != null ? c.getLot().getId() < 1 : true) {
                            dao.requeteLibre("DELETE FROM yvs_com_doc_achats WHERE id = ?", new Options[]{new Options(distant.getId(), 1)});
                            getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le depot");
                            return;
                        }
                    }
                    c.setId(null);
                    c = (YvsComContenuDocAchat) dao.save1(c);
                    distant.getContenus().add(c);
                    int idx = facture.getContenus().indexOf(new YvsComContenuDocAchat(id));
                    if (idx > -1) {
                        facture.getContenus().get(idx).getContenus().add(c);
                    }
                }
                distant.setDocumentLie(facture);
                if (statut.equals(Constantes.ETAT_VALIDE)) {
                    ManagedLivraisonAchat service = (ManagedLivraisonAchat) giveManagedBean(ManagedLivraisonAchat.class);
                    if (service != null) {
                        if (service.validerOrder(distant, false, false, true, exist_inventaire, force)) {
                            String rq = "UPDATE yvs_com_doc_achats SET statut_livre = '" + (statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE) + "' WHERE id=?";
                            Options[] param = new Options[]{new Options(facture.getId(), 1)};
                            dao.requeteLibre(rq, param);
                            facture.setStatutLivre(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                        }
                    }
                } else if (message) {
                    succes();
                }
                int idx = facture.getDocuments().indexOf(distant);
                if (idx < 0) {
                    facture.getDocuments().add(distant);
                }
                if (documents.contains(facture)) {
                    documents.set(documents.indexOf(facture), facture);
                    update("data_facture_achat");
                }
            } else {
                if (!facture.getContenus().isEmpty()) {
                    String rq = "UPDATE yvs_com_doc_achats SET statut_livre = 'L' WHERE id=?";
                    Options[] param = new Options[]{new Options(facture.getId(), 1)};
                    dao.requeteLibre(rq, param);
                    rq = "UPDATE yvs_com_doc_achats SET statut_livre = 'L' WHERE document_lie=?";
                    param = new Options[]{new Options(facture.getId(), 1)};
                    dao.requeteLibre(rq, param);

                    facture.setStatutLivre(Constantes.ETAT_LIVRE);
                    if (documents.contains(facture)) {
                        documents.set(documents.indexOf(facture), facture);
                        update("data_facture_achat");
                    }
                    if (message) {
                        getInfoMessage("Cette facture est deja livrée à sa totalité");
                    }
                } else {
                    if (message) {
                        getErrorMessage("Vous ne pouvez pas livrer cette facture car elle est vide");
                    }
                }
            }
            if (docAchat.getContenus().isEmpty()) {
                docAchat.getContenus().addAll(contenus);
            }
        }
    }

    public void cloturer(YvsComDocAchats y) {
        selectDoc = y;
        update("id_confirm_close_fa");
    }

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }
        selectDoc.setCloturer(!selectDoc.getCloturer());
        selectDoc.setDateCloturer(selectDoc.getCloturer() ? new Date() : null);
        selectDoc.setCloturerBy(selectDoc.getCloturer() ? currentUser.getUsers() : null);
        selectDoc.setAuthor(currentUser);
        dao.update(selectDoc);
        if (documents.contains(selectDoc)) {
            documents.set(documents.indexOf(selectDoc), selectDoc);
            update("data_facture_achat");
        }
    }

    public boolean changeStatut(String etat) {
        if (changeStatut_(etat)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat) {
        return changeStatut_(etat, docAchat, selectDoc);
    }

    public boolean changeStatut(String etat, YvsComDocAchats doc_) {
        if (changeStatut_(etat, doc_)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, YvsComDocAchats doc_) {
        return changeStatut_(etat, UtilCom.buildBeanDocAchat(doc_), doc_);
    }

    public boolean changeStatut(String etat, DocAchat doc, YvsComDocAchats doc_) {
        if (changeStatut_(etat, doc, doc_)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, DocAchat doc, YvsComDocAchats doc_) {
        if (!etat.equals("")) {
            if (doc.isCloturer()) {
                getErrorMessage("Ce document est vérouillé");
                return false;
            }
            String rq = "UPDATE yvs_com_doc_achats SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(doc.getId(), 1)};
            dao.requeteLibre(rq, param);
            doc.setStatut(etat);
            doc_.setStatut(etat);
            if (documents.contains(doc_)) {
                documents.set(documents.indexOf(doc_), doc_);
                update("data_facture_achat");
            }
            update("grp_btn_etat_facture_achat");
            return true;
        }
        return false;
    }

    public void changeStatutLine(YvsComContenuDocAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            if (!y.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                if (!autoriser("fa_editer_doc")) {
                    openNotAcces();
                    return;
                }
                y.setStatut(Constantes.ETAT_EDITABLE);
                y.setAuthor(currentUser);
                dao.update(y);
                int idx = documents.indexOf(new YvsComDocAchats(docAchat.getId()));
                if (idx > -1) {
                    int i = documents.get(idx).getContenus().indexOf(y);
                    if (i > -1) {
                        documents.get(idx).getContenus().set(i, y);
                        update("data_facture_achat");
                    }
                }
                succes();
            } else {
                if (docAchat.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    y.setStatut(Constantes.ETAT_VALIDE);
                    y.setAuthor(currentUser);
                    dao.update(y);
                    int idx = documents.indexOf(new YvsComDocAchats(docAchat.getId()));
                    if (idx > -1) {
                        int i = documents.get(idx).getContenus().indexOf(y);
                        if (i > -1) {
                            documents.get(idx).getContenus().set(i, y);
                            update("data_facture_achat");
                        }
                    }
                    succes();
                } else {
                    getErrorMessage("Impossible de valider cette ligne car le document n'est pas valide");
                }
            }
        }
    }

    public void searchByNum() {
        ParametreRequete p;
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "numDoc", "%" + numSearch_ + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numDoc)", "numDoc", "%" + numSearch_.trim().toUpperCase() + "%", "LIKE", "AND"));
        } else {
            p = new ParametreRequete("y.numDoc", "numDoc", null);
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    public void addParamComptabilised() {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_facture_achat c RIGHT JOIN yvs_com_doc_achats y ON c.facture = y.id "
                    + "INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.type_doc = 'FA' AND y.statut = 'V' AND a.societe = ? "
                    + "AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
            Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (date_) {
                query += " AND y.date_doc BETWEEN ? AND ?";
                param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebut_, 2), new Options(dateFin_, 3)};
            }
            Long count = (Long) dao.loadObjectBySqlQuery(query, param);
            nbrComptaSearch = count != null ? count : 0;
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    boolean isBonus;

    public void searchArticle(boolean isBonus) {
        this.isBonus = isBonus;
        List<String> categories = new ArrayList<>();
        if (docAchat.getTypeAchat() != null ? docAchat.getTypeAchat().getId() > 0 : false) {
            for (YvsBaseTypeDocCategorie c : docAchat.getTypeAchat().getCategories()) {
                categories.add(c.getCategorie());
            }
        }
        if (!isBonus) {
            String num = contenu.getArticle().getRefArt();
            contenu.getArticle().setDesignation("");
            contenu.getArticle().setError(true);
            contenu.getArticle().setId(0);
            listArt = false;
            selectArt = false;
            if (num != null ? num.trim().length() > 0 : false) {
                ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
                if (m != null) {
                    Articles y = m.searchArticleActif(categories, "A", num, true);
                    if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                        if (m.getArticlesResult().size() > 1) {
                            update("data_articles_facture_achat");
                        } else {
                            chooseArticle(y);
                        }
                        contenu.getArticle().setError(false);
                    } else {
                        Conditionnement c = m.searchArticleActifByCodeBarre(num);
                        if (m.getConditionnements() != null ? !m.getConditionnements().isEmpty() : false) {
                            if (m.getConditionnements().size() > 1) {
                                update("data_conditionnements_facture_achat");
                            } else {
                                chooseConditionnement(c);
                            }
                            listArt = true;
                            selectArt = true;
                        }
                    }
                }
            }
        } else {
            String num = bonus.getArticleBonus().getRefArt();
            bonus.getArticleBonus().setDesignation("");
            bonus.getArticleBonus().setError(true);
            bonus.getArticleBonus().setId(0);
            if (num != null ? num.trim().length() > 0 : false) {
                ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
                if (m != null) {
                    Articles y = m.searchArticleActif(categories, "A", num, true);
                    if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                        if (m.getArticlesResult().size() > 1) {
                            update("data_articles_facture_achat");
                        } else {
                            chooseArticle(y);
                        }
                        bonus.getArticleBonus().setError(false);
                    }
                }
            }
        }
    }

    public void initArticles(boolean isBonus) {
        this.isBonus = isBonus;
        listArt = false;
        ManagedArticles a = (ManagedArticles) giveManagedBean("Marticle");
        if (a != null) {
            if (!isBonus) {
                a.initArticles("A", contenu.getArticle());
                listArt = contenu.getArticle().isListArt();
            } else {
                a.initArticles("A", bonus.getArticleBonus());
            }
        }
        update("data_articles_facture_achat");
    }

    public void chooseFsseur(Fournisseur d) {
        if ((d != null) ? d.getId() > 0 : false) {
            cloneObject(docAchat.getFournisseur(), d);
            if (d.getCategorieComptable() != null) {
                cloneObject(docAchat.getCategorieComptable(), d.getCategorieComptable());
                update("select_categorie_comptable_facture_achat");
            }
            if (docAchat.getFournisseur() != null ? docAchat.getFournisseur().getModel() != null : false) {
                docAchat.setModeReglement(docAchat.getFournisseur().getModel());
            }
            update("select_model_facture_achat");
        }
        update("infos_facture_achat");
    }

    public void searchFsseur() {
        String num = docAchat.getFournisseur().getCodeFsseur();
        docAchat.getFournisseur().setId(0);
        docAchat.getFournisseur().setError(true);
        docAchat.getFournisseur().setTiers(new Tiers());
        if ((num != null) ? !num.trim().isEmpty() : false) {
            ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
            if (m != null) {
                Fournisseur y = m.searchFsseur(num, true);
                if (m.getFournisseurs() != null ? !m.getFournisseurs().isEmpty() : false) {
                    if (m.getFournisseurs().size() > 1) {
                        update("data_fournisseur_fa");
                    } else {
                        chooseFsseur(y);
                    }
                    docAchat.getFournisseur().setError(false);
                }
            }
        }
    }

    public void initFsseurs() {
        ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (m != null) {
            m.initFsseurs(docAchat.getFournisseur());
        }
        update("data_fournisseur_fa");
    }

    public void initFacture(DocAchat a, Fournisseur c) {
        if (a == null) {
            a = new DocAchat();
        }
        paginator.addParam(new ParametreRequete("y.numDoc", "numDoc", null));
        if (c != null ? c.getId() > 0 : false) {
            codeFsseur_ = c.getCodeFsseur();
            addParamFsseur();
        } else {
            loadAllFacture(true, true);
        }
        a.setList(true);
    }

    public void removeDoublon(YvsComDocAchats y) {
        selectDoc = y;
        removeDoublon();
    }

    public void removeDoublon() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            if (!selectDoc.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre editable pour pouvoir etre modifié");
                return;
            }
            removeDoublonAchat(selectDoc.getId());
            succes();
        }
    }

    @Override
    public void cleanAchat() {
        super.cleanAchat();
        loadAllFacture(true, true);
    }

    public void equilibreByDate() {
        dao.getEquilibreAchat(currentAgence.getSociete().getId(), dateDebut_, dateFin_);
    }

    public void equilibre(YvsComDocAchats y) {
        selectDoc = y;
        equilibre();
    }

    public void equilibre() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            Map<String, String> statuts = dao.getEquilibreAchat(selectDoc.getId());
            if (statuts != null) {
                selectDoc.setStatutLivre(statuts.get("statut_livre"));
                selectDoc.setStatutRegle(statuts.get("statut_regle"));
            }
            int idx = documents.indexOf(selectDoc);
            if (idx > -1) {
                documents.set(idx, selectDoc);
                update("data_facture_achat");
            }
            succes();
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void print(YvsComDocAchats y) {
        print(y, true);
    }

    public void print(YvsComDocAchats y, boolean withHeader) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Double ca = dao.loadCaAchat(y.getId());
                Double taxe = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findTaxeByFacture", new String[]{"docAchat"}, new Object[]{y});
                List<Object[]> taxes = dao.getTaxeAchat(y.getId());
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + "/icones/" + (y.getStatutRegle().equals(Constantes.ETAT_REGLE) ? "solde.png" : "empty.png"));
                param.put("IMG_LIVRE", path + "/icones/" + (y.getStatutLivre().equals(Constantes.ETAT_LIVRE) ? "livre.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(ca));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("TTC", ca);
                param.put("TAXE", taxe);
                param.put("AFFICHE_TAXE", taxes != null ? taxes.size() > 1 : false);
                param.put("LOGO", returnLogo());
                path = SUBREPORT_DIR(withHeader);
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(withHeader));
                System.err.println("Chemin... " + SUBREPORT_DIR(withHeader));
                if (y.getStatut().equals(Constantes.ETAT_VALIDE)) {
//                    param.put("TITRE_DOC", Constantes.DOCUMENT_FACTURE_ACHAT);
                    executeReport("facture_achat", param);
                } else {
                    param.put("TITRE_DOC", Constantes.DOCUMENT_COMMANDE_ACHAT);
                    executeReport("bon_de_commande", param);
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedFactureAchat.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     DEBUT WORKFLOW
     */
    private YvsWorkflowValidFactureAchat currentEtape;

    private YvsWorkflowValidFactureAchat giveEtapeActuelle(List<YvsWorkflowValidFactureAchat> etapes) {
        if (!etapes.isEmpty()) {
            List<YvsWorkflowValidFactureAchat> l = ordonneEtapes(etapes);
            for (YvsWorkflowValidFactureAchat e : l) {
                if (!e.getEtapeValid()) {
                    return e;
                }
            }
            return l.get(l.size() - 1);
        } else {
            return null;
        }
    }

    public List<YvsWorkflowEtapeValidation> saveEtapesValidation() {
        champ = new String[]{"titre", "societe"};
        val = new Object[]{Constantes.DOCUMENT_FACTURE_ACHAT, currentAgence.getSociete()};
        return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", champ, val);
    }

    public List<YvsWorkflowValidFactureAchat> saveEtapesValidation(YvsComDocAchats m, List<YvsWorkflowEtapeValidation> model) {
        //charge les étape de vailidation
        List<YvsWorkflowValidFactureAchat> re = new ArrayList<>();
        if (!model.isEmpty()) {
            YvsWorkflowValidFactureAchat vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    champ = new String[]{"facture", "etape"};
                    val = new Object[]{m, et};
                    YvsWorkflowValidFactureAchat w = (YvsWorkflowValidFactureAchat) dao.loadOneByNameQueries("YvsWorkflowValidFactureAchat.findByEtapeFacture", champ, val);
                    if (w != null ? w.getId() < 1 : true) {
                        vm = new YvsWorkflowValidFactureAchat();
                        vm.setAuthor(currentUser);
                        vm.setEtape(et);
                        vm.setEtapeValid(false);
                        vm.setFactureAchat(m);
                        vm.setDateSave(new Date());
                        vm.setOrdreEtape(et.getOrdreEtape());
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

    public void motifEtapeFacture(String motifEtape, boolean lastEtape) {
        this.motifEtape = motifEtape;
        this.lastEtape = lastEtape;
    }

    public void annulEtapeFacture(YvsWorkflowValidFactureAchat etape, boolean lastEtape) {
        this.etape = etape;
        this.lastEtape = lastEtape;
        this.motifEtape = null;
        if (etape.getEtapeSuivante() == null) {
            if (dao.isComptabilise(docAchat.getId(), Constantes.SCR_ACHAT)) {
                openDialog("dlgConfirmAnnuleDoc");
                return;
            }
        }
        openDialog("dglMotifCancelEtape");
    }

    public boolean annulEtapeFacture() {
        return annulEtapeFacture(selectDoc, docAchat, currentUser, etape, lastEtape, motifEtape);
    }

    public boolean annulEtapeFacture(YvsComDocAchats current, DocAchat achat, YvsUsersAgence users, YvsWorkflowValidFactureAchat etape, boolean lastEtape, String motif) {
        if (!asDroitValideEtape(etape.getEtape(), users.getUsers())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                getErrorMessage("Vous devez précisez le motif");
                return false;
            }
            if (current != null ? (current.getId() != null ? current.getId() < 1 : true) : true) {
                current = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{achat.getId()});
            }
            if (achat != null ? achat.getId() < 1 : true) {
                achat = UtilCom.buildBeanDocAchat(current);
            }
            if (dao.isComptabilise(current.getId(), Constantes.SCR_ACHAT)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return false;
                }
            }
            int idx = achat.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtapeSuivante().getEtape(), current};
                    YvsWorkflowValidFactureAchat y = (YvsWorkflowValidFactureAchat) dao.loadOneByNameQueries("YvsWorkflowValidFactureAchat.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        getErrorMessage("Vous devez au préalable annuler l'étape suivante");
                        return false;
                    }
                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    if (!w.unComptabiliserAchat(current, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return false;
                    }
                }
                etape.setAuthor(users);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                dao.update(etape);

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setStatut(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                dao.update(current);

                achat.setStatut(current.getStatut());
                achat.setEtapeValide(current.getEtapeValide());
                if (documents != null ? documents.contains(current) : false) {
                    int idx_ = documents.indexOf(current);
                    documents.get(idx_).setEtapeValide(current.getEtapeValide());
                    documents.get(idx_).setStatut(current.getStatut());
                }
                getInfoMessage("Annulation effectué avec succès !");
                update("tabview_facture_achat");
                return true;
            } else {
                getErrorMessage("Impossible de continuer !");
            }
        }
        return false;
    }

    public boolean validEtapeFacture(YvsWorkflowValidFactureAchat etape, boolean lastEtape) {
        return validEtapeFacture(docAchat, selectDoc, etape, lastEtape, true, dao);
    }

    public boolean validEtapeFacture(DocAchat docAchat, YvsComDocAchats selectDoc, YvsWorkflowValidFactureAchat etape, boolean lastEtape, DaoInterfaceLocal dao) {
        return validEtapeFacture(docAchat, selectDoc, etape, lastEtape, false, dao);
    }

    public boolean validEtapeFacture(DocAchat achat, YvsComDocAchats current, YvsWorkflowValidFactureAchat etape, boolean lastEtape, boolean save, DaoInterfaceLocal dao) {
        //vérifier que la personne qui valide l'étape a le droit 
        if (!asDroitValideEtape(etape.getEtape())) {
            openNotAcces();
            return false;
        } else {
            boolean continu = true;
            if (save) {
//                continu = _saveNew(false, false, false);
                YvsComDocAchats y = _saveNew(recopieView(), false, false, false);
                continu = y != null ? y.getId() > 0 : false;
            }
            if (continu) {
                //contrôle la cohérence des dates
                if (current != null ? (current.getId() != null ? current.getId() < 1 : true) : true) {
                    if (!save) {
                        current = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{achat.getId()});
                    }
                }
                int idx = achat.getEtapesValidations().indexOf(etape);
                if (idx >= 0) {
                    if (etape.getEtape().getLivraisonHere()) {
                        achat.setLivraisonDo(true);
                    }
                    //cas de la validation de la dernière étapes
                    etape.setFactureAchat(current);
                    if (etape.getEtapeSuivante() == null) {
                        if (validerOrder(achat, current)) {
                            if (currentUser != null) {
                                etape.setAuthor(currentUser);
                            }
                            etape.setEtapeValid(true);
                            etape.setEtapeActive(false);
                            etape.setMotif(null);
                            etape.setDateUpdate(new Date());
                            if (achat.getEtapesValidations().size() > (idx + 1)) {
                                achat.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                            }
                            dao.update(etape);
                            current.setEtapeValide(current.getEtapeValide() + 1);
                            if (documents != null ? documents.contains(current) : false) {
                                int idx_ = documents.indexOf(current);
                                documents.get(idx_).setEtapeValide(current.getEtapeValide());
                            }
                            update("tabview_facture_achat");
                            return true;
                        }
                    } else {
                        if (currentUser != null) {
                            etape.setAuthor(currentUser);
                        }
                        etape.setEtapeValid(true);
                        etape.setEtapeActive(false);
                        etape.setMotif(null);
                        etape.setDateUpdate(new Date());
                        if (achat.getEtapesValidations().size() > (idx + 1)) {
                            achat.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                        }
                        dao.update(etape);
                        current.setStatut(Constantes.ETAT_ENCOURS);
                        if (currentUser != null) {
                            current.setAuthor(currentUser);
                        }
                        current.setDateUpdate(new Date());
                        current.setEtapeValide(current.getEtapeValide() + 1);
                        dao.update(current);
                        achat.setStatut(Constantes.ETAT_ENCOURS);
                        achat.setEtapeValide(current.getEtapeValide());
                        if (documents != null ? documents.contains(current) : false) {
                            int idx_ = documents.indexOf(current);
                            documents.get(idx_).setEtapeValide(current.getEtapeValide());
                            documents.get(idx_).setStatut(current.getStatut());
                        }
                        getInfoMessage("Validation effectué avec succès !");
                        return true;
                    }
                } else {
                    getErrorMessage("Impossible de continuer !");
                }
            }
        }
        return false;
    }

    public void cancelEtapeFacture(boolean force, boolean suspend) {
        if (dao.isComptabilise(docAchat.getId(), Constantes.SCR_ACHAT)) {
            openDialog("dlgConfirmAnnulerDoc");
            return;
        }
        cancelEtapeFacture(force, suspend, false);
    }

    public void cancelEtapeFacture(boolean force, boolean suspend, boolean passe) {
        //vérifie le droit
        if (!autoriser("fa_editer_doc") && docAchat.getEtapesValidations().isEmpty()) {
            openNotAcces();
            return;
        }
        if (dao.isComptabilise(selectDoc.getId(), Constantes.SCR_ACHAT)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return;
            }
        }
        suspend = selectDoc.getStatut().equals(Constantes.ETAT_ANNULE) ? false : suspend;
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            List<YvsComDocAchats> l = dao.loadNameQueries("YvsComDocAchats.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
            if (l != null ? l.isEmpty() : true || passe) {
                //annule toute les validation acquise
                int i = 0;
                boolean update = force;
                if (!force) {
                    for (YvsWorkflowValidFactureAchat vm : docAchat.getEtapesValidations()) {
                        //si on trouve une étape non valide (on ne peut annuler un ordre de docAchat complètement valide)
                        if (!vm.getEtapeValid()) {
                            update = true;
                        } else {
                            //ais-je un droit de validation pour cet étape?
                            if (!asDroitValideEtape(vm.getEtape().getAutorisations(), currentUser.getUsers().getNiveauAcces())) {
                                getErrorMessage("Vous ne pouvez annuler cette facture ! Elle requière un niveau suppérieur");
                                return;
                            }
                        }
                    }
                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    if (!w.unComptabiliserAchat(selectDoc, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                }
                if (update) {
                    for (YvsWorkflowValidFactureAchat vm : docAchat.getEtapesValidations()) {
                        vm.setEtapeActive(false);
                        if (i == 0) {
                            vm.setEtapeActive(true);
                        }
                        vm.setAuthor(currentUser);
                        vm.setEtapeValid(false);
                        dao.update(vm);
                        i++;
                    }
                } else if (!docAchat.getEtapesValidations().isEmpty()) {
                    openDialog(suspend ? "dlgConfirmRefuserForcer" : "dlgConfirmAnnulerForcer");
                    return;
                }
                //désactive la docAchat
                docAchat.setLivraisonDo(false);
                if (changeStatut((suspend) ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE)) {
                    selectDoc.setCloturer(false);
                    selectDoc.setAnnulerBy(currentUser.getUsers());
                    selectDoc.setValiderBy(null);
                    selectDoc.setDateAnnuler(new Date());
                    selectDoc.setDateCloturer(null);
                    selectDoc.setDateValider(null);
                    selectDoc.setCloturerBy(null);
                    selectDoc.setAuthor(currentUser);
                    selectDoc.setEtapeValide(0);
                    docAchat.setEtapeValide(0);
                    dao.update(selectDoc);
                    for (YvsComContenuDocAchat c : docAchat.getContenus()) {
                        c.setAuthor(currentUser);
                        c.setStatut(Constantes.ETAT_EDITABLE);
                        if (selectDoc.getContenus().contains(c)) {
                            selectDoc.getContenus().set(selectDoc.getContenus().indexOf(c), c);
                        }
                        dao.update(c);
                    }
                }
            } else {
                for (YvsComDocAchats d : l) {
                    if (d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        getErrorMessage("Impossible d'annuler cet ordre car il possède une livraison déja valide");
                        return;
                    }
                }
                openDialog(suspend ? "dlgConfirmRefuser" : "dlgConfirmAnnuler");
            }
        }
    }

    /*
     FIN WORKFLOW
     */
    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.docAchat.numDoc", "reference", null);
        if (reference != null ? reference.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", reference + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.numDoc)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.codeFsseur)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.nom)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamStatuts() {
        ParametreRequete p = new ParametreRequete("y.docAchat.statut", "statut", null);
        if (statutContenu != null ? statutContenu.trim().length() > 0 : false) {
            p = new ParametreRequete("y.docAchat.statut", "statut", statutContenu, "=", "AND");
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamArticle() {
        ParametreRequete p = new ParametreRequete("y.article.refArt", "article", null);
        if (article != null ? article.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "article", article + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", article.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", article.toUpperCase() + "%", "LIKE", "OR"));
            lconditionnements = dao.loadNameQueries("YvsBaseConditionnement.findUniteByArticle", new String[]{"article", "societe"}, new Object[]{"%" + article + "%", currentAgence.getSociete()});
        }
        p_contenu.addParam(p);
        p_contenu.addParam(new ParametreRequete("y.conditionnement.unite", "unite", null, "=", "AND"));
        loadContenus(true, true);
    }

    public void addParamUnite() {
        ParametreRequete p = new ParametreRequete("y.conditionnement.unite", "unite", null, "=", "AND");
        if (condArticle != null ? condArticle > 0 : false) {
            p.setObjet(new YvsBaseUniteMesure(condArticle));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamAgence() {
        ParametreRequete p;
        if (agenceContenu > 0) {
            p = new ParametreRequete("y.docAchat.agence", "agence", new YvsAgences(agenceContenu));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.docAchat.agence", "agence", null);
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamDepot() {
        ParametreRequete p = new ParametreRequete("y.docAchat.depotReception.code", "depot", null);
        if (depot != null ? depot.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "depot", depot + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.depotReception.code)", "depot", depot.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.depotReception.designation)", "depot", depot.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamFournisseur() {
        ParametreRequete p = new ParametreRequete("y.docVente.client", "fournisseur", null);
        if (fournisseurF != null ? fournisseurF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "fournisseur", fournisseurF + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.codeFsseur)", "fournisseur", fournisseurF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.nom)", "fournisseur", fournisseurF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.prenom)", "fournisseur", fournisseurF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.tiers.codeTiers)", "fournisseur", fournisseurF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(CONCAT(y.docAchat.fournisseur.nom, ' ', y.docAchat.fournisseur.prenom))", "fournisseur", fournisseurF.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamDateContenu(SelectEvent ev) {
        findByDateContenu();
    }

    public void findByDateContenu() {
        ParametreRequete p = new ParametreRequete("y.docAchat.dateDoc", "date", null, "BETWEEN", "AND");
        if (dateContenu) {
            if (dateDebutContenu != null && dateFinContenu != null) {
                if (dateDebutContenu.before(dateFinContenu) || dateDebutContenu.equals(dateFinContenu)) {
                    p = new ParametreRequete(null, "date", dateDebutContenu, dateFinContenu, "BETWEEN", "AND");
                    p.getOtherExpression().add(new ParametreRequete("y.docAchat.dateDoc", "date", dateDebutContenu, dateFinContenu, "BETWEEN", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("y.docAchat.dateSave", "date", dateDebutContenu, dateFinContenu, "BETWEEN", "OR"));
                }
            }
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamPrixContenu() {
        ParametreRequete p = new ParametreRequete("y.prixAchat", "prix", null, "=", "AND");
        if (addPrix) {
            if (comparer.equals("BETWEEN")) {
                if (prixSearch <= prix2Search) {
                    p = new ParametreRequete("y.prixAchat", "prix", prixSearch, prix2Search, comparer, "AND");
                }
            } else {
                double prix = comparer.equals(Constantes.SYMBOLE_SUP_EGALE) ? prixSearch : prix2Search;
                p = new ParametreRequete("y.prixAchat", "prix", prix, comparer, "AND");
            }
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void verifyComptabilised(Boolean comptabilised) {
        loadAllFacture(true, true);
        if (comptabilised != null) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                List<YvsComDocAchats> list = new ArrayList<>();
                list.addAll(documents);
                for (YvsComDocAchats y : list) {
                    y.setComptabilised(w.isComptabilise(y.getId(), Constantes.SCR_ACHAT));
                    if (comptabilised ? !y.isComptabilised() : y.isComptabilised()) {
                        documents.remove(y);
                    }
                }
            }
        }
        update("data_facture_achat");
    }

    public void reBuildNumero() {
        if (tabIds != null ? tabIds.trim().length() > 0 : false) {
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComDocAchats current;
                for (Integer index : ids) {
                    current = reBuildNumero(documents.get(index), true, false);
                    documents.set(index, current);
                }
                succes();
            }
            tabIds = "";
        }
    }

    public void equilibreAll() {
        if (tabIds != null ? tabIds.trim().length() > 0 : false) {
            System.err.println("tabIds = " + tabIds);
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComDocAchats current;
                for (Integer index : ids) {
                    current = documents.get(index);
                    equilibre(current);
                }
                succes();
            }
            tabIds = "";
        }
    }

    public YvsComDocAchats reBuildNumero(YvsComDocAchats y, boolean save, boolean msg) {
        return reBuildNumero(y, y.getTypeDoc(), save, msg);
    }

    public YvsComDocAchats reBuildNumero(YvsComDocAchats y, String type, boolean save, boolean msg) {
        if (y != null ? (y.getId() > 0 ? (y.getDepotReception() != null ? y.getDepotReception().getId() > 0 : false) : false) : false) {
            String num = genererReference(giveNameType(type), y.getDateDoc(), (y.getDepotReception() != null ? y.getDepotReception().getId() : 0), Constantes.DEPOT);;
            if (num != null ? num.trim().length() < 1 : true) {
                return y;
            }
            y.setNumDoc(num);
            y.setAuthor(currentUser);
            if (save) {
                dao.update(y);
            }
            if (msg) {
                succes();
            }
        }
        return y;
    }

    public void forceOnViewAchat(YvsComptaBonProvisoire piece, YvsComptaCaissePieceAchat select) {
        if (piece != null ? piece.getId() > 0 : false) {
            if (piece.getBonAchat() != null) {
                piece.getBonAchat().setPiece(select);
            }
            if (select != null ? select.getId() > 0 : false) {
                addBonProvisoire(piece, select, true, true);
            }
        }
    }

    public YvsComptaJustifBonAchat addBonProvisoire(YvsComptaBonProvisoire bon, YvsComptaCaissePieceAchat piece, boolean msg, boolean succes) {
        if (piece != null ? piece.getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez selectionner une mission!!!");
            }
            return null;
        }
        if (bon != null ? bon.getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez enregistrer un bon provisoire!!!");
            }
            return null;
        }
        if (bon.getBonAchat() != null ? (bon.getBonAchat().getId() > 0 ? !bon.getBonAchat().getPiece().equals(piece) : false) : false) {
            openDialog("dlgConfirmBonProvAchat");
            update("cfm_bon_achat");
            return null;
        }
        if (bon.getMontant() <= 0) {
            if (msg) {
                getErrorMessage("Vous devez entrer le montant du bon");
            }
            return null;
        }
        if (piece.getMontant() > bon.getMontant()) {
            if (msg) {
                getErrorMessage("Impossible d'associer une piece de reglement supérieur à la valeur du bon");
            }
            return null;
        }
        YvsComptaJustifBonAchat y = new YvsComptaJustifBonAchat(piece, bon);
        y.setAuthor(currentUser);
        if (bon.getBonAchat() != null ? bon.getBonAchat().getId() < 1 : true) {
            champ = new String[]{"bon", "piece"};
            val = new Object[]{bon, piece};
            YvsComptaJustifBonAchat old = (YvsComptaJustifBonAchat) dao.loadOneByNameQueries("YvsComptaJustifBonAchat.findOne", champ, val);
            if (old != null ? old.getId() < 1 : true) {
                y.setId(null);
                y = (YvsComptaJustifBonAchat) dao.save1(y);
            } else {
                y = old;
            }
        } else {
            y.setId(bon.getBonAchat().getId());
            dao.update(y);
        }
        bon.setBonAchat(y);
        piece.setJustify(y);
        if (succes) {
            succes();
        }
        return y;
    }

    public void comptabiliseByDate(Date dateDebut, Date dateFin) {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            champ = new String[]{"societe", "statut", "dateDebut", "dateFin", "type"};
            val = new Object[]{currentAgence.getSociete(), Constantes.ETAT_VALIDE, dateDebut, dateFin, Constantes.TYPE_FA};
            List<YvsComDocAchats> list = dao.loadNameQueries("YvsComDocAchats.findByStatutDates", champ, val);
            if (list != null ? !list.isEmpty() : false) {
                if (list.size() > 1000) {
                    getErrorMessage("Veuillez entrer une période plus petite");
                    return;
                }
                for (YvsComDocAchats y : list) {
                    w.comptabiliserAchat(y, false, false);
                }
                succes();
            }
        }
    }

    public void lettrer(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                boolean comptabilise = w.isComptabilise(y.getId(), Constantes.SCR_ACHAT);
                if (!comptabilise) {
                    getInfoMessage("Cette facture n'est pas comptabilisée");
                    return;
                }
                w.setContenusLettrer(w.lettrerAchat(y));
                if (w.getContenusLettrer() != null ? !w.getContenusLettrer().isEmpty() : false) {
                    openDialog("dlgLettrage");
                    update("data_contenu_journal");
                }
            }
        }
    }

    public void lettrerCaisse(YvsComptaCaissePieceAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                boolean comptabilise = w.isComptabilise(y.getId(), Constantes.SCR_CAISSE_ACHAT);
                if (!comptabilise) {
                    getInfoMessage("Cette pièce n'est pas comptabilisée");
                    return;
                }
                w.setContenusLettrer(w.lettrerCaisseAchat(y));
                if (w.getContenusLettrer() != null ? !w.getContenusLettrer().isEmpty() : false) {
                    openDialog("dlgLettrage");
                    update("data_contenu_journal");
                }
            }
        }
    }

    private String numCompte(Long id) {
        if (id != null) {
            return (String) dao.loadObjectByNameQueries("YvsBasePlanComptable.findNumeroById", new String[]{"id"}, new Object[]{id});
        }
        return null;
    }

    public boolean isComptabiliseBean(DocAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_ACHAT));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabilise(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_ACHAT));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public void applyOrRemoveTaxe(YvsComTaxeContenuAchat taxe) {
        selectedTaxe = taxe;
        if (taxe != null) {
            if (docAchat.getId() > 0) {
                if (taxe.getId() != null ? taxe.getId() > 0 : false) {
                    openDialog("dlgConfirmdeleteTaxe");
                } else {
                    applyNewTaxe(taxe, taxe.getTaxe(), selectDoc);
                    //change la ligne de contenu                   
                    setMontantTotalDoc(docAchat);
                    loadTaxesAchat(taxe.getContenu());
                    succes();
                    update("chp_fa_net_a_payer");
                    update("data_contenu_facture_achat");
                    update("tabview_facture_achat:data_contenu_facture_achat");
                }
            } else {
                getErrorMessage("La facture séléctionné n'est pas enregistré !");
            }
        } else {
            getErrorMessage("Aucune taxe n'a été selectionné !");
        }
    }

    public double applyNewTaxe(YvsComTaxeContenuAchat c, YvsBaseTaxes t, YvsComDocAchats doc) {
        Double taxe = 0d;
        Double prixU = 0d;
        Double valeur = 0d;
        int cpt = 0;
        if (c != null && t != null) {
            if (hasDroitUpdateFacture(doc)) {
                if (c.getContenu().getArticle().getPuvTtc()) {
                    //récupère l'ensemble des taux de taxe
                    for (YvsComTaxeContenuAchat ct : c.getContenu().getTaxes()) {
                        if (ct.getId() != null ? ct.getId() > 0 : false) {
                            taxe += ct.getTaxe().getTaux();
                        }
                    }
                    taxe += t.getTaux();
                    //PU HT
                    prixU = c.getContenu().getPrixAchat() / (1 + (taxe / 100));

                } else {
                    prixU = c.getContenu().getPrixAchat();
                }
                //modifie les autre taxes   
                valeur = c.getContenu().getQuantiteCommande() * prixU;
                for (YvsComTaxeContenuAchat ct : c.getContenu().getTaxes()) {
                    if (ct.getId() != null ? (ct.getId() > 0 && ct.getTaxe().getTaux() > 0) : false) {
                        taxe = (((valeur - c.getContenu().getRemise()) * ct.getTaxe().getTaux()) / 100);
                        taxe = dao.arrondi(currentAgence.getSociete().getId(), taxe);
                        ct.setMontant(taxe);
                        ct.setDateUpdate(new Date());
                        ct.setAuthor(currentUser);
                        dao.update(ct);
                    }
                }
                taxe = (((valeur - c.getContenu().getRemise()) * t.getTaux()) / 100);
                taxe = dao.arrondi(currentAgence.getSociete().getId(), taxe);
                c.setMontant(taxe);
                c.setDateSave(new Date());
                c.setDateUpdate(new Date());
                c.setAuthor(currentUser);
                c = (YvsComTaxeContenuAchat) dao.save1(c);
                int idx = docAchat.getContenus().indexOf(c.getContenu());
                if (idx >= 0) {
                    docAchat.getContenus().get(idx).setTaxes(dao.loadNameQueries("YvsComTaxeContenuAchat.findByContenu", new String[]{"contenu"}, new Object[]{c.getContenu()}));
                    loadTaxesAchat(docAchat.getContenus().get(idx));
                }
            }
        }
        return taxe;
    }

    public void confirmDeleteTaxe() {
        if (selectedTaxe != null) {
            if (hasDroitUpdateFacture(selectDoc)) {
                dao.delete(selectedTaxe);
                selectedTaxe.setId(null);
                selectedTaxe.setMontant(0d);
                setMontantTotalDoc(docAchat);
                int idx = selectedTaxe.getContenu().getTaxes().indexOf(selectedTaxe);
                if (idx >= 0) {
                    selectedTaxe.getContenu().getTaxes().set(idx, selectedTaxe);
                }
                if (selectedTaxe.getContenu().getArticle().getPuvTtc()) {
                    Double taxe = 0d;
                    Double prixU = 0d;
                    Double valeur = 0d;
                    //récupère l'ensemble des taux de taxe
                    for (YvsComTaxeContenuAchat ct : contenu.getTaxes()) {
                        if (ct.getId() != null ? ct.getId() > 0 : false) {
                            taxe += ct.getTaxe().getTaux();
                        }
                    }
                    taxe += selectedTaxe.getTaxe().getTaux();
                    //PU HT
                    prixU = selectedTaxe.getContenu().getPrixAchat() / (1 + (taxe / 100));
                    //modifie les autre taxes
                    for (YvsComTaxeContenuAchat ct : selectedTaxe.getContenu().getTaxes()) {
                        if (ct.getId() != null ? ct.getId() > 0 : false) {
                            valeur = selectedTaxe.getContenu().getQuantiteCommande() * prixU;
                            taxe = (((valeur - selectedTaxe.getContenu().getRemise()) * ct.getTaxe().getTaux()) / 100);
                            taxe = dao.arrondi(currentAgence.getSociete().getId(), taxe);
                            ct.setMontant(taxe);
                            dao.update(ct);
                        }
                    }
                }
                idx = docAchat.getContenus().indexOf(selectedTaxe.getContenu());
                if (idx >= 0) {
                    docAchat.getContenus().get(idx).setTaxes(dao.loadNameQueries("YvsComTaxeContenuAchat.findByContenu", new String[]{"contenu"}, new Object[]{selectedTaxe.getContenu()}));
                    loadTaxesAchat(docAchat.getContenus().get(idx));
                }
                succes();
                update("chp_fa_net_a_payer");
                update("tabview_facture_achat:data_contenu_facture_achat");
            }
        }
    }

    public boolean hasDroitUpdateFacture(YvsComDocAchats doc) {
        if (doc == null) {
            getErrorMessage("Impossible de modifier, ", "La facture n'est pas enregistré");
            return false;
        }
        if (doc.getId() != null ? doc.getId() <= 0 : true) {
            getErrorMessage("Impossible de modifier, ", "La facture n'est pas enregistré");
            return false;
        }
        boolean etape = false;
        if (doc.getStatut().equals(Constantes.ETAT_VALIDE)) {
            if (dao.isComptabilise(doc.getId(), Constantes.SCR_VENTE)) {
                getErrorMessage("Impossible de modifier, ", "La facture est dejà comptabilisé");
                return false;
            }
            if (!autoriser("fa_update_doc")) {
                getErrorMessage("Impossible de modifier, ", "La facture est dejà validé");
                return false;
            }
        } else if (doc.getStatut().equals(Constantes.ETAT_ENCOURS)) {
            //teste le droit à une étape
            etape = (doc.getEtapesValidations().size() > 0) && !autoriser("fv_update_doc");
        }
        if (etape) {
            int prec = -1;
            for (YvsWorkflowValidFactureAchat e : doc.getEtapesValidations()) {
                if (e.isEtapeActive()) {
                    if (prec > -1 && prec < doc.getEtapesValidations().size()) {
                        if (!asDroitValideEtape(doc.getEtapesValidations().get(prec).getEtape().getAutorisations(), currentUser.getUsers().getNiveauAcces())) {
                            openNotAcces();
                            return false;
                        }
                    }
                }
                prec++;
            }
        }
        return true;
    }

    public void selectdocs(YvsComDocAchats doc) {
        if (doc != null ? doc.getId() > 0 : false) {
            System.out.println("doc = " + doc.getId());
            selectDoc = doc;
            notes = selectDoc.getNotes();

        }
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true, true);
        }
    }

    public void initInfosContenu(YvsComContenuDocAchat y) {
        selectContenu = y;
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            Double quantite = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findSumByUniteDocLieTypeStatut", new String[]{"facture", "conditionnement", "statut", "typeDoc"}, new Object[]{y.getDocAchat(), y.getConditionnement(), Constantes.ETAT_VALIDE, Constantes.TYPE_BLA});
            String statut = Constantes.ETAT_ATTENTE;
            if (quantite != null ? quantite > 0 : false) {
                if (quantite >= y.getQuantiteCommande()) {
                    statut = Constantes.ETAT_LIVRE;
                } else {
                    statut = Constantes.ETAT_ENCOURS;
                }
            }
            y.setQteLivree(quantite != null ? quantite : 0);
            if (!y.getStatutLivree().equals(statut)) {
                y.setStatutLivree(statut);
                selectContenu.setStatutLivree(statut);
                dao.update(y);
                if (docAchat.getContenus().contains(y)) {
                    update("tabview_facture_achat:data_contenu_facture_achat");
                }
                if (all_contenus.contains(y)) {
                    update("data_contenu_fa");
                }
            }
            update("blog_form_infos_contenu");
        }
    }

    public void addNote() {
        try {
            if (notes != null ? !notes.isEmpty() : false) {
                selectDoc.setNotes(notes);
                dao.update(selectDoc);
                succes();
                notes = "";
            } else {

            }

        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onLoadContenuProjet(YvsComContenuDocAchat y) {
        selectContenu = y;
        chooseProjet();
    }

    public void chooseProjet() {
        try {
            if (selectContenu != null ? selectContenu.getId() > 0 : false) {
                nameQueri = "YvsProjProjetContenuDocAchat.findByContenu";
                champ = new String[]{"contenu"};
                val = new Object[]{selectContenu};

                String query = "SELECT p.id, p.code, p.libelle, d.id, d.code_departement, d.intitule, s.id, y.id FROM yvs_proj_projet p INNER JOIN yvs_proj_projet_service y ON y.projet = p.id "
                        + " INNER JOIN yvs_proj_departement s ON y.service = s.id INNER JOIN yvs_base_departement d ON s.service = d.id"
                        + " LEFT JOIN yvs_proj_projet_contenu_doc_achat c ON (c.projet = y.id AND c.contenu = ?) WHERE c.id IS NULL AND p.societe = ?";

                if (projet > 0) {
                    nameQueri = "YvsProjProjetContenuDocAchat.findByProjetContenu";
                    champ = new String[]{"contenu", "projet"};
                    val = new Object[]{selectContenu, new YvsProjProjet(projet)};
                    query += " AND p.id = " + projet;
                }
                selectContenu.setProjets(dao.loadNameQueries(nameQueri, champ, val));

                List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(selectContenu.getId(), 1), new Options(currentAgence.getSociete().getId(), 2)});
                YvsProjProjet p;
                YvsBaseDepartement d;
                YvsProjDepartement s;
                YvsProjProjetService y;
                YvsProjProjetContenuDocAchat c;
                for (Object[] data : result) {
                    p = new YvsProjProjet((Long) data[0]);
                    p.setCode((String) data[1]);
                    p.setLibelle((String) data[2]);

                    d = new YvsBaseDepartement((Long) data[3]);
                    d.setCodeDepartement((String) data[4]);
                    d.setIntitule((String) data[5]);

                    s = new YvsProjDepartement((Long) data[6]);
                    s.setService(d);

                    y = new YvsProjProjetService((Long) data[7]);
                    y.setProjet(p);
                    y.setService(s);

                    c = new YvsProjProjetContenuDocAchat(YvsProjProjetContenuDocAchat.ids--);
                    c.setAuthor(currentUser);
                    c.setContenu(selectContenu);
                    c.setProjet(y);

                    selectContenu.getProjets().add(c);
                }
                if (projet > 0) {
                    query = "SELECT p.id, p.code, p.libelle, d.id, d.code_departement, d.intitule, s.id FROM yvs_proj_projet p, yvs_proj_departement s INNER JOIN yvs_base_departement d ON s.service = d.id"
                            + " LEFT JOIN yvs_proj_projet_service y ON (y.service = s.id AND y.projet = ?) WHERE y.id IS NULL AND p.id = ? AND d.societe = ?";
                    result = dao.loadListBySqlQuery(query, new Options[]{new Options(projet, 1), new Options(projet, 2), new Options(currentAgence.getSociete().getId(), 3)});
                    for (Object[] data : result) {
                        p = new YvsProjProjet((Long) data[0]);
                        p.setCode((String) data[1]);
                        p.setLibelle((String) data[2]);

                        d = new YvsBaseDepartement((Long) data[3]);
                        d.setCodeDepartement((String) data[4]);
                        d.setIntitule((String) data[5]);

                        s = new YvsProjDepartement((Long) data[6]);
                        s.setService(d);

                        y = new YvsProjProjetService(YvsProjProjetService.ids--);
                        y.setAuthor(currentUser);
                        y.setProjet(p);
                        y.setService(s);

                        c = new YvsProjProjetContenuDocAchat(YvsProjProjetContenuDocAchat.ids--);
                        c.setAuthor(currentUser);
                        c.setContenu(selectContenu);
                        c.setProjet(y);

                        selectContenu.getProjets().add(c);
                    }
                }
                update("data-contenu_projet");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cellEditProjet(CellEditEvent ev) {
        try {
            if (ev != null ? ev.getRowIndex() > -1 : false) {
                int index = ev.getRowIndex();
                YvsProjProjetContenuDocAchat y = selectContenu.getProjets().get(index);
                if ((Double) ev.getNewValue() <= 0) {
                    if (y.getId() > 0) {
                        dao.delete(y);
                        y.setId(YvsProjProjetContenuDocAchat.ids--);
                    }
                } else {
                    Double quantite = (Double) dao.loadObjectByNameQueries("YvsProjProjetContenuDocAchat.sumByContenuNotId", new String[]{"contenu", "id"}, new Object[]{selectContenu, y.getId()});
                    if (((quantite != null ? quantite : 0) + (Double) ev.getNewValue()) > selectContenu.getQuantiteCommande()) {
                        y.setQuantite((Double) ev.getOldValue());
                        if (index > -1) {
                            selectContenu.getProjets().set(index, y);
                        }
                        update("data-contenu_projet");
                        getErrorMessage("Vous ne pouvez pas exceder la quantitée de la ligne d'achat");
                        return;
                    }
                    if (y.getProjet().getId() < 1) {
                        y.getProjet().setId(null);
                        y.setProjet((YvsProjProjetService) dao.save1(y.getProjet()));
                    }
                    y.setQuantite((Double) ev.getNewValue());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    if (y.getId() > 0) {
                        dao.update(y);
                    } else {
                        y.setId(null);
                        y = (YvsProjProjetContenuDocAchat) dao.save1(y);
                    }
                }
                if (index > -1) {
                    selectContenu.getProjets().set(index, y);
                }
                succes();
                update("data-contenu_projet");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getPuv(DocAchat d, YvsComContenuDocAchat c) {
        return dao.getPuv(c.getArticle().getId(), 0, 0, 0, d.getDepotReception().getId(), 0, d.getDateDoc(), c.getConditionnement().getId());
    }

    public void buildToReceive() {
        try {
            contenusRequireLot.clear();
            if (docAchat.getDepotReception() != null ? docAchat.getDepotReception().getId() > 0 : false) {
                Boolean requiere_lot = false;
                String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
                for (YvsComContenuDocAchat c : docAchat.getContenus()) {
                    requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(docAchat.getDepotReception().getId(), 2)});
                    c.setRequiereLot(requiere_lot != null ? requiere_lot : false);
                    if (c.isRequiereLot()) {
                        if (c.getLot() == null) {
                            c.setLot(new YvsComLotReception());
                        }
                        contenusRequireLot.add(c);
                    }
                }
            }
            update("blog-contenu_fa_require_lot");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void findLotReception(YvsComContenuDocAchat c) {
        try {
            if (c != null ? c.getLot() != null : false) {
                if (c.getLot().getNumero() != null ? c.getLot().getNumero().trim().length() > 0 : false) {
                    YvsComLotReception l = (YvsComLotReception) dao.loadOneByNameQueries("YvsComLotReception.findByNumeroArticle", new String[]{"article", "numero"}, new Object[]{new YvsBaseArticles(c.getArticle().getId()), c.getLot().getNumero()});
                    if (l != null ? l.getId() < 1 : true) {
                        String query = "SELECT y.id FROM yvs_com_lot_reception y INNER JOIN yvs_base_mouvement_stock_lot l ON l.lot = y.id INNER JOIN yvs_base_mouvement_stock m ON l.mouvement = m.id "
                                + " WHERE m.article = ? AND y.numero = ? ORDER BY y.id DESC LIMIT 1";
                        Long lot = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(c.getLot().getNumero(), 2)});
                        l = new YvsComLotReception(lot, c.getLot().getNumero());
                    }
                    if (l != null ? l.getId() > 0 : false) {
                        c.setLot(l);
                        update("data-contenu_fa_require_lot");
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getMontantTotal(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            return setMontantTotalDoc(y, y.getContenus(), null, null);
        }
        return 0;
    }

    public void onLoadMontant(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            if (y.getContenus() != null ? y.getContenus().isEmpty() : true) {
                y.setContenus(dao.loadNameQueries("YvsComContenuDocAchat.findByDocAchat", new String[]{"docAchat"}, new Object[]{y}));
            }
            y.setCharger(true);
            setMontantTotalDoc(y, y.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
        }
    }
}
