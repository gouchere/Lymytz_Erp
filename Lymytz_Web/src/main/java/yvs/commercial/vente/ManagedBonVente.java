/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.ModelReglement;
import yvs.base.compta.UtilCompta;
import yvs.base.produits.Articles;
import yvs.production.UtilProd;
import yvs.commercial.UtilCom;
import yvs.commercial.client.Client;
import yvs.commercial.rrr.GrilleRabais;
import yvs.commercial.rrr.Remise;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.rrr.YvsComGrilleRemise;
import yvs.entity.commercial.rrr.YvsComGrilleRistourne;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComRemiseDocVente;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.base.tiers.Tiers;
import yvs.commercial.ManagedCatCompt;
import yvs.grh.presence.TrancheHoraire;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.ManagedModeReglement;
import static yvs.commercial.UtilCom.buildDocVente;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.depot.ManagedPointVente;
import yvs.commercial.depot.PointVente;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.comptabilite.caisse.Caisses;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.caisse.ManagedReglementVente;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.base.YvsBaseArticlePoint;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBasePointVenteDepot;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.rrr.YvsComRemise;
import yvs.entity.commercial.vente.YvsComTaxeContenuVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaPhasePiece;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.users.YvsUsers;
import yvs.grh.UtilGrh;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.parametrage.entrepot.Depots;
import yvs.users.Users;
import static yvs.util.Managed.ldf;
import static yvs.util.Managed.time;

import yvs.util.ParametreRequete;
import yvs.util.Utilitaire;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedBonVente extends ManagedCommercial<DocVente, YvsComDocVentes> implements Serializable {

    private YvsComEnteteDocVente selectEntete;
    private EnteteDocVente entete = new EnteteDocVente();
    private List<YvsUsers> vendeurs;

    private DocVente docVente = new DocVente();
    private List<YvsComDocVentes> documents;
    private YvsComDocVentes selectDoc;

    private YvsComContenuDocVente selectContenu, entityContenu;
    private ContenuDocVente contenu = new ContenuDocVente();
    private boolean on_rabais;
    private double montant_rabais, montant_rabais_total, montant_remise, taux_remise, avance;
    private String commentaire, numSerie;

    private List<YvsUsers> caissiers;
    private PieceTresorerie reglement = new PieceTresorerie();
    private YvsComptaCaissePieceVente selectReglement, selectedPiece;
    private Caisses caisseAnnule = new Caisses();
    private Date dateAnnule = new Date();

    private List<YvsGrhTrancheHoraire> tranches;
//    private List<YvsBasePointVente> points;
    private List<YvsBaseTaxes> taxes;

    private String tabIds, tabIds_contenu;
    private boolean correct = false, selectArt, listArt;

    //Parametre recherche
    private boolean date = false, _first = true;
    private Date dateDebut = new Date(), dateFin = new Date(), dateLivraison = new Date();
    private String statut = null, egaliteStatut = "!=", egaliteStatutL = "=", egaliteStatutR = "=";
    private Boolean autoLivreSearch;

    // Nombre d'element a afficher dans le selectOneMenu
    private int subLenght;

    private List<YvsBaseDepots> depotsLivraison;

    private boolean annuleCmde = true, annuleFact = true, annuleLivr = true, annuleRegl = true;
    private boolean visibleCmde = false, visibleFact = false, visibleLivr = false, visibleRegl = false;
    private boolean deleteFact = true, deleteLivr = true, deleteRegl = true;
    private boolean annuleCmdeAll = true;
    private boolean deleteFactAll = true, deleteLivrAll = true, deleteReglAll = true;

    String type = Constantes.TYPE_BCV;

    private boolean lotBL, lotPiece, lotFV, lotBC;
    protected String statutLotBL = Constantes.ETAT_VALIDE, statutLotBC = Constantes.ETAT_VALIDE, statutLotFV = Constantes.ETAT_VALIDE, statutLotPiece = Constantes.ETAT_REGLE;

    public ManagedBonVente() {
        taxes = new ArrayList<>();
        documents = new ArrayList<>();
        depotsLivraison = new ArrayList<>();
        tranches = new ArrayList<>();
        caissiers = new ArrayList<>();
        vendeurs = new ArrayList<>();
    }

    public boolean isAnnuleCmdeAll() {
        return annuleCmdeAll;
    }

    public void setAnnuleCmdeAll(boolean annuleCmdeAll) {
        this.annuleCmdeAll = annuleCmdeAll;
    }

    public boolean isDeleteFactAll() {
        return deleteFactAll;
    }

    public void setDeleteFactAll(boolean deleteFactAll) {
        this.deleteFactAll = deleteFactAll;
    }

    public boolean isDeleteLivrAll() {
        return deleteLivrAll;
    }

    public void setDeleteLivrAll(boolean deleteLivrAll) {
        this.deleteLivrAll = deleteLivrAll;
    }

    public boolean isDeleteReglAll() {
        return deleteReglAll;
    }

    public void setDeleteReglAll(boolean deleteReglAll) {
        this.deleteReglAll = deleteReglAll;
    }

    public boolean isLotBC() {
        return lotBC;
    }

    public void setLotBC(boolean lotBC) {
        this.lotBC = lotBC;
    }

    public String getStatutLotBC() {
        return statutLotBC;
    }

    public void setStatutLotBC(String statutLotBC) {
        this.statutLotBC = statutLotBC;
    }

    public boolean isLotBL() {
        return lotBL;
    }

    public void setLotBL(boolean lotBL) {
        this.lotBL = lotBL;
    }

    public boolean isLotPiece() {
        return lotPiece;
    }

    public void setLotPiece(boolean lotPiece) {
        this.lotPiece = lotPiece;
    }

    public boolean isLotFV() {
        return lotFV;
    }

    public void setLotFV(boolean lotFV) {
        this.lotFV = lotFV;
    }

    public String getStatutLotBL() {
        return statutLotBL;
    }

    public void setStatutLotBL(String statutLotBL) {
        this.statutLotBL = statutLotBL;
    }

    public String getStatutLotPiece() {
        return statutLotPiece;
    }

    public void setStatutLotPiece(String statutLotPiece) {
        this.statutLotPiece = statutLotPiece;
    }

    public String getStatutLotFV() {
        return statutLotFV;
    }

    public void setStatutLotFV(String statutLotFV) {
        this.statutLotFV = statutLotFV;
    }

    public boolean isDeleteFact() {
        return deleteFact;
    }

    public void setDeleteFact(boolean deleteFact) {
        this.deleteFact = deleteFact;
    }

    public boolean isDeleteLivr() {
        return deleteLivr;
    }

    public void setDeleteLivr(boolean deleteLivr) {
        this.deleteLivr = deleteLivr;
    }

    public boolean isDeleteRegl() {
        return deleteRegl;
    }

    public void setDeleteRegl(boolean deleteRegl) {
        this.deleteRegl = deleteRegl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFirst() {
        return _first;
    }

    public void setFirst(boolean _first) {
        this._first = _first;
    }

    public boolean isAnnuleCmde() {
        return annuleCmde;
    }

    public void setAnnuleCmde(boolean annuleCmde) {
        this.annuleCmde = annuleCmde;
    }

    public boolean isAnnuleFact() {
        return annuleFact;
    }

    public void setAnnuleFact(boolean annuleFact) {
        this.annuleFact = annuleFact;
    }

    public boolean isAnnuleLivr() {
        return annuleLivr;
    }

    public void setAnnuleLivr(boolean annuleLivr) {
        this.annuleLivr = annuleLivr;
    }

    public boolean isAnnuleRegl() {
        return annuleRegl;
    }

    public void setAnnuleRegl(boolean annuleRegl) {
        this.annuleRegl = annuleRegl;
    }

    public boolean isVisibleCmde() {
        return visibleCmde;
    }

    public void setVisibleCmde(boolean visibleCmde) {
        this.visibleCmde = visibleCmde;
    }

    public boolean isVisibleFact() {
        return visibleFact;
    }

    public void setVisibleFact(boolean visibleFact) {
        this.visibleFact = visibleFact;
    }

    public boolean isVisibleLivr() {
        return visibleLivr;
    }

    public void setVisibleLivr(boolean visibleLivr) {
        this.visibleLivr = visibleLivr;
    }

    public boolean isVisibleRegl() {
        return visibleRegl;
    }

    public void setVisibleRegl(boolean visibleRegl) {
        this.visibleRegl = visibleRegl;
    }

    public YvsComptaCaissePieceVente getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaCaissePieceVente piece) {
        this.piece = piece;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isEmission() {
        return emission;
    }

    public void setEmission(boolean emission) {
        this.emission = emission;
    }

    public List<YvsUsers> getCaissiers() {
        return caissiers;
    }

    public void setCaissiers(List<YvsUsers> caissiers) {
        this.caissiers = caissiers;
    }

    public Boolean getAutoLivreSearch() {
        return autoLivreSearch;
    }

    public void setAutoLivreSearch(Boolean autoLivreSearch) {
        this.autoLivreSearch = autoLivreSearch;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
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

    public Caisses getCaisseAnnule() {
        return caisseAnnule;
    }

    public void setCaisseAnnule(Caisses caisseAnnule) {
        this.caisseAnnule = caisseAnnule;
    }

    public Date getDateAnnule() {
        return dateAnnule;
    }

    public void setDateAnnule(Date dateAnnule) {
        this.dateAnnule = dateAnnule;
    }

    public List<YvsUsers> getVendeurs() {
        return vendeurs;
    }

    public void setVendeurs(List<YvsUsers> vendeurs) {
        this.vendeurs = vendeurs;
    }

    public YvsComptaCaissePieceVente getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(YvsComptaCaissePieceVente selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public PieceTresorerie getReglement() {
        return reglement;
    }

    public void setReglement(PieceTresorerie reglement) {
        this.reglement = reglement;
    }

    public YvsComptaCaissePieceVente getSelectReglement() {
        return selectReglement;
    }

    public void setSelectReglement(YvsComptaCaissePieceVente selectReglement) {
        this.selectReglement = selectReglement;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public double getMontant_rabais_total() {
        return montant_rabais_total;
    }

    public void setMontant_rabais_total(double montant_rabais_total) {
        this.montant_rabais_total = montant_rabais_total;
    }

    public double getAvance() {
        return avance;
    }

    public void setAvance(double avance) {
        this.avance = avance;
    }

    public double getTaux_remise() {
        return taux_remise;
    }

    public void setTaux_remise(double taux_remise) {
        this.taux_remise = taux_remise;
    }

    public List<YvsBaseDepots> getDepotsLivraison() {
        return depotsLivraison;
    }

    public void setDepotsLivraison(List<YvsBaseDepots> depotsLivraison) {
        this.depotsLivraison = depotsLivraison;
    }

    public int getSubLenght() {
        return subLenght;
    }

    public void setSubLenght(int subLenght) {
        this.subLenght = subLenght;
    }

    public List<YvsBaseTaxes> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsBaseTaxes> taxes) {
        this.taxes = taxes;
    }

    public double getMontant_remise() {
        return montant_remise;
    }

    public void setMontant_remise(double montant_remise) {
        this.montant_remise = montant_remise;
    }

    public double getMontant_rabais() {
        return montant_rabais;
    }

    public void setMontant_rabais(double montant_rabais) {
        this.montant_rabais = montant_rabais;
    }

    public boolean isOn_rabais() {
        return on_rabais;
    }

    public void setOn_rabais(boolean on_rabais) {
        this.on_rabais = on_rabais;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
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

    public List<YvsComDocVentes> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocVentes> documents) {
        this.documents = documents;
    }

    public YvsComContenuDocVente getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsComContenuDocVente selectContenu) {
        this.selectContenu = selectContenu;
    }

    public YvsComContenuDocVente getEntityContenu() {
        return entityContenu;
    }

    public void setEntityContenu(YvsComContenuDocVente entityContenu) {
        this.entityContenu = entityContenu;
    }

    public List<YvsGrhTrancheHoraire> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsGrhTrancheHoraire> tranches) {
        this.tranches = tranches;
    }

    public boolean isSelectArt() {
        return selectArt;
    }

    public void setSelectArt(boolean selectArt) {
        this.selectArt = selectArt;
    }

    public boolean isDate() {
        return date;
    }

    public void setDate(boolean date) {
        this.date = date;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public EnteteDocVente getEntete() {
        return entete;
    }

    public void setEntete(EnteteDocVente entete) {
        this.entete = entete;
    }

    public DocVente getDocVente() {
        return docVente;
    }

    public void setDocVente(DocVente docVente) {
        this.docVente = docVente;
    }

    public ContenuDocVente getContenu() {
        return contenu;
    }

    public void setContenu(ContenuDocVente contenu) {
        this.contenu = contenu;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getTabIds_contenu() {
        return tabIds_contenu;
    }

    public void setTabIds_contenu(String tabIds_contenu) {
        this.tabIds_contenu = tabIds_contenu;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public YvsComEnteteDocVente getSelectEntete() {
        return selectEntete;
    }

    public void setSelectEntete(YvsComEnteteDocVente selectEntete) {
        this.selectEntete = selectEntete;
    }

    public YvsComDocVentes getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComDocVentes selectDoc) {
        this.selectDoc = selectDoc;
    }

    @Override
    public void loadAll() {
        _load();
        initView();
        setClientDefaut();
        _first = true;
        this.type = Constantes.TYPE_BCV;
    }

    public void load(Boolean livraison) {

    }

    public void initView() {
        if (((docVente != null) ? (docVente.getClient().getId() < 1 && docVente.getCategorieComptable().getId() < 1) : true)) {
            docVente = new DocVente();
            docVente.setTypeDoc(Constantes.TYPE_BCV);
            if (docVente.getDocumentLie() == null) {
                docVente.setDocumentLie(new DocVente());
            }
            numSearch_ = "";
            docVente.setEnteteDoc(new EnteteDocVente());
        }
        if (entete.getPoint() != null ? entete.getPoint().getId() < 1 : true) {
            if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauPoint() != null : false) {
                entete.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauPoint().getTranche()));
            }
            entete.setPoint(new PointVente(currentPoint != null ? currentPoint.getId() : 0));
            choosePoint();
            if (docVente.getDepot() != null ? docVente.getDepot().getId() < 1 : true) {
                docVente.setDepot(UtilCom.buildBeanDepot(currentDepot));
            }
            chooseDepot();
        }
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service != null) {
            YvsBaseCaisse y = service.findByResponsable(currentUser.getUsers());
            reglement.setCaisse(UtilCompta.buildBeanCaisse(y));
            loadCaissiers(y);
        }
        loadAllNoLivre();
    }

    public void loadAllNoLivre() {
        egaliteStatutL = "!=";
        statutLivre_ = Constantes.ETAT_LIVRE;
        addParamStatutLivre();
    }

    public void loadAllBons(boolean avance, boolean init) {
        //choisir les documents à charger
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_BCV)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence", "agence", currentAgence, "=", "AND");
                paginator.addParam(p);
                break;
            case 3: //charge tous les document des points de vente où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers", new String[]{"users", "date", "hier"}, new Object[]{currentUser.getUsers(), (Utilitaire.getIniTializeDate(new Date()).getTime()), Constantes.getPreviewDate(new Date())});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
                paginator.addParam(p);
                break;
            default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                p = new ParametreRequete("y.enteteDoc.creneau.users ", "users", currentUser.getUsers(), "=", "AND");
                paginator.addParam(p);
                break;
        }
        p = new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_BCV, "=", "AND");
        paginator.addParam(p);
        documents = paginator.executeDynamicQuery("YvsComDocVentes", "y.enteteDoc.dateEntete DESC, y.numDoc DESC", avance, init, (int) imax, dao);
        subLenght = documents.size() > 10 ? 10 : documents.size();
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComDocVentes> re = paginator.parcoursDynamicData("YvsComDocVentes", "y", "y.enteteDoc.dateEntete DESC, y.numDoc DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadBonsNonFacturer(boolean avance, boolean init) {
        if (_first) {
            clearParams();
        }
        _first = false;
        statutLivre_ = Constantes.ETAT_ATTENTE;
        paginator.addParam(new ParametreRequete("y.statutLivre", "statutLivre", Constantes.ETAT_LIVRE, "!=", "AND"));
        loadAllBons(avance, init);
    }

    public void clearParams() {
        codeClient_ = null;
        entete_ = 0;
        tranche_ = 0;
        point_ = 0;
        numSearch_ = null;
        statut = null;
        codeVendeur_ = null;
        statutLivre_ = null;
        cloturer_ = null;
        date = false;
        dateDebut = new Date();
        dateFin = new Date();
        date_ = false;
        dateDebut_ = new Date();
        dateFin_ = new Date();
        idsSearch = "";
        agence_ = 0;
        paginator.getParams().clear();
        _first = true;
        loadAllBons(true, true);
        update("blog_entete_commande_vente");
    }

    private void loadOthersDetailDoc(YvsComDocVentes y) {
        loadTaxesVente(y);
        update("tabview_commande_vente");
        update("data_contenu_commande_vente");
    }

    private void loadTaxesVente(YvsComDocVentes y) {
        taxes.clear();
        List<Object[]> l = dao.getTaxeVente(y.getId());
        for (Object[] o : l) {
            YvsBaseTaxes t = (YvsBaseTaxes) dao.loadOneByNameQueries("YvsBaseTaxes.findById", new String[]{"id"}, new Object[]{o[0]});
            if (t != null ? t.getId() > 0 : false) {
                t.setTaux((double) o[1]);
                taxes.add(t);
            }
        }
        update("data_taxes_commande_vente");
    }

    public void loadTaxesVente(YvsComContenuDocVente y) {
        contenu.setName(y.getArticle().getDesignation());
        contenu.setTaxes(new ArrayList<YvsComTaxeContenuVente>());
        contenu.getTaxes().addAll(y.getTaxes());
        update("data_taxes_contenu_commande_vente");
    }

    public void loadDepotByPoint(YvsBasePointVente y) {
        champ = new String[]{"pointVente"};
        val = new Object[]{y};
        nameQueri = "YvsBasePointVenteDepot.findDepotByPoint";
        depotsLivraison = dao.loadNameQueries(nameQueri, champ, val);

        nameQueri = "YvsBasePointVenteDepot.findDepotByPointPrincipal";
        champ = new String[]{"point"};
        List<YvsBaseDepots> lp = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        if (lp != null ? !lp.isEmpty() : false) {
            if (docVente.getDepot() != null ? docVente.getDepot().getId() < 1 : false) {
                docVente.setDepot(UtilCom.buildBeanDepot(lp.get(0)));
                loadAllTranche(lp.get(0), docVente.getDateLivraisonPrevu());
            }
        }
        update("select_depot_liv_prevu_bcv");
        update("select_tranche_livraison_bcv");
    }
//

    public void loadAllTranche(YvsBaseDepots depot, Date date) {
        docVente.setTranche(new TrancheHoraire());
        tranches = loadTranche(depot, date);
        update("data_tranche_bcv");
    }

    public YvsBaseModeReglement buildModelReglement(ModelReglement y) {
        YvsBaseModeReglement m = new YvsBaseModeReglement();
        if (y != null) {
            m.setId(y.getId());
            m.setActif(true);
            m.setDesignation(y.getDesignation());
            m.setDescription(y.getDescription());
            m.setSociete(currentAgence.getSociete());
            m.setAuthor(currentUser);
        }
        return m;
    }

    public YvsComClient buildClient(Client y) {
        YvsComClient f = new YvsComClient();
        if (y != null) {
            f.setId(y.getId());
            if ((y.getTiers() != null) ? y.getTiers().getId() > 0 : false) {
                f.setTiers(new YvsBaseTiers(y.getTiers().getId()));
            }
            if ((y.getCategorieComptable() != null) ? y.getCategorieComptable().getId() > 0 : false) {
                f.setCategorieComptable(new YvsBaseCategorieComptable(y.getCategorieComptable().getId()));
            }
            f.setDefaut(y.isDefaut());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                f.setAuthor(currentUser);
            }
        }
        return f;
    }

    public YvsBaseTiers buildTiers(Tiers t) {
        YvsBaseTiers r = new YvsBaseTiers();
        if (t != null) {
            r.setId(t.getId());
            r.setCodeTiers(t.getCodeTiers());
            r.setSociete(currentAgence.getSociete());
            r.setNom(t.getNom());
            r.setPrenom(t.getPrenom());
            r.setCodePostal(t.getCodePostal());
            r.setCivilite(t.getCivilite());
            r.setAdresse(t.getAdresse());
            r.setEmail(t.getEmail());
            if ((t.getVille() != null) ? t.getVille().getId() > 0 : false) {
                r.setVille(new YvsDictionnaire(t.getVille().getId(), t.getVille().getLibelle()));
            }
            if ((t.getPays() != null) ? t.getPays().getId() > 0 : false) {
                r.setPays(new YvsDictionnaire(t.getPays().getId(), t.getPays().getLibelle()));
            }
            r.setClient(true);
            r.setFournisseur(t.isFournisseur());
            r.setRepresentant(t.isRepresentant());
            r.setStSociete(t.isSociete());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                r.setAuthor(currentUser);
            }
        }
        return r;
    }

    public YvsComptaCaissePieceDivers buildPieceTresorerie(PieceTresorerie y) {
        YvsComptaCaissePieceDivers p = new YvsComptaCaissePieceDivers();
        if (y != null) {
            p.setDatePiece((y.getDatePiece() != null) ? y.getDatePiece() : new Date());
            p.setId(y.getId());
            p.setMontant(y.getMontant());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                p.setAuthor(currentUser);
            }
        }
        return p;
    }

    public YvsComRemiseDocVente buildRemiseDocVente(RemiseDocVente y) {
        YvsComRemiseDocVente r = new YvsComRemiseDocVente();
        if (y != null) {
            r.setId(y.getId());
            if ((y.getRemise() != null) ? y.getRemise().getId() > 0 : false) {
                r.setRemise(new YvsComRemise(y.getRemise().getId(), y.getRemise().getReference()));
            }
            r.setDocVente(selectDoc);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                r.setAuthor(currentUser);
            }
        }
        return r;
    }

    public YvsComRemise buildRemise(Remise y) {
        YvsComRemise r = new YvsComRemise();
        if (y != null) {
            r.setId(y.getId());
            r.setRefRemise(y.getReference());
            r.setActif(y.isActif());
            r.setPermanent(y.isPermanent());
            r.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            r.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            r.setSociete(currentAgence.getSociete());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                r.setAuthor(currentUser);
            }
        }
        return r;
    }

    public YvsComGrilleRemise buildGrilleRemise(GrilleRabais y) {
        YvsComGrilleRemise r = new YvsComGrilleRemise();
        if (y != null) {
            r.setId(y.getId());
            r.setMontantMaximal(y.getMontantMaximal());
            r.setMontantMinimal(y.getMontantMinimal());
            r.setMontantRemise(y.getMontantRabais());
            r.setNatureMontant((y.getNatureMontant() != null) ? y.getNatureMontant() : Constantes.NATURE_MTANT);
            r.setBase((y.getBase() != null) ? y.getBase() : Constantes.BASE_QTE);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                r.setAuthor(currentUser);
            }
        }
        return r;
    }

    public YvsComGrilleRistourne buildGrilleRistourne(GrilleRabais y) {
        YvsComGrilleRistourne r = new YvsComGrilleRistourne();
        if (y != null) {
            r.setId(y.getId());
            r.setMontantMaximal(y.getMontantMaximal());
            r.setMontantMinimal(y.getMontantMinimal());
            r.setMontantRistourne(y.getMontantRabais());
            r.setNatureMontant((y.getNatureMontant() != null) ? y.getNatureMontant() : Constantes.NATURE_MTANT);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                r.setAuthor(currentUser);
            }
        }
        return r;
    }

    @Override
    public DocVente recopieView() {
        docVente.setTypeDoc(Constantes.TYPE_BCV);
        ManagedClient service = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (service != null) {
            int idx = service.getClients().indexOf(new YvsComClient(docVente.getClient().getId()));
            if (idx >= 0) {
                docVente.setClient(UtilCom.buildBeanClient(service.getClients().get(idx)));
            }
        }
        return docVente;
    }

    public ContenuDocVente recopieViewContenu(DocVente doc) {
        contenu.setDocVente(doc);
        contenu.setNew_(true);
        return contenu;
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
                    } else {
                        reglement.setMode(reglement.getMode());
                    }
                } else {
                    reglement.setMode(reglement.getMode());
                }
            }
            reglement.setMouvement(Constantes.MOUV_DEBIT);
            reglement.setUpdate(true);
            reglement.setDateUpdate(new Date());
            reglement.setDatePiece(reglement.getDatePaiementPrevu());
            reglement.setDatePaiement(reglement.getDatePaiementPrevu());
        }
        return reglement;
    }

    public YvsBaseCategorieClient getDefautCategorie() {
        YvsBaseCategorieClient c = new YvsBaseCategorieClient();
        c.setDescription("Catégorie Par Défaut");
        c.setCode("Defaut");
        c.setLibelle("Autres");
        c.setLierClient(false);
        c.setDefaut(true);
        return c;
    }

    @Override
    public boolean controleFiche(DocVente bean) {
        if (!_controleFiche_(bean)) {
            return false;
        }
        if (!saveNewEntete()) {
            return false;
        }
        if ((selectEntete != null) ? selectEntete.getId() < 1 : true) {
            getErrorMessage("Vous ne disposé pas d'une entête");
            return false;
        }
        if ((bean.getClient() != null) ? bean.getClient().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le client!");
            return false;
        }
        if ((bean.getCategorieComptable() != null) ? bean.getCategorieComptable().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier la catégorie comptable!");
            return false;
        }
        if (!bean.isUpdate()) {
            String ref = genererReference(Constantes.TYPE_BCV_NAME, selectEntete.getDateEntete(), entete.getPoint().getId(), Constantes.POINTVENTE);
            bean.setNumDoc(ref);
            if (ref == null || ref.trim().equals("")) {
                return false;
            }
        }
        return verifyDateVente(selectEntete.getDateEntete(), bean.isUpdate());
    }

    private boolean _controleFiche_(DocVente bean) {
        if (bean == null) {
            getErrorMessage("Le devez selectionner un document");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
            getErrorMessage("Le document est déja livré");
            return false;
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    private boolean _controleFiche_(YvsComDocVentes bean) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
            getErrorMessage("Le document est déja livré");
            return false;
        }
        if (bean.getCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
//        return writeInExercice(bean.getDateDoc()); 
        return true;
    }

    public boolean controleFicheEntete(EnteteDocVente bean) {
        if ((bean.getPoint() != null) ? bean.getPoint().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner le point de vente!");
            return false;
        }
        if ((bean.getTranche() != null) ? bean.getTranche().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner la tranche!");
            return false;
        }
        if (!bean.getEtat().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("La journée doit etre editable pour etre modifier");
            return false;
        }
        return verifyDateAchat(bean.getDateEntete(), bean.getId() > 0);
    }

    public boolean controleFicheContenu(ContenuDocVente bean, boolean continuSave) {
        if (bean.getDocVente() != null ? !bean.getDocVente().isUpdate() : true) {
            if (!_saveNew()) {
                return false;
            }
            bean.setDocVente(docVente);
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if (bean.getQuantite() < 1) {
            getErrorMessage("Vous devez entrer la quantitée");
            return false;
        }
        if (bean.getPrix() < 1) {
            getErrorMessage("Vous devez entrer le prix de vente");
            return false;
        }
        if (bean.getPrix() < bean.getPrixMin()) {
            openDialog("dlgConfirmAlertRabais");
            update("dlg_confirm_rabais_bcv");
            on_rabais = true;
            return false;
        }
        boolean controlAcces = false;
        if (!continuSave) {
            if (bean.getRabais() <= 0) {
                if (bean.getPrix() < bean.getPrixMin()) {
                    openDialog("dlgConfirmAlertRabais");
                    update("dlg_confirm_rabais_bcv");
                    on_rabais = true;
                    return false;
                }
            } else {
                controlAcces = true;
            }
        } else {
            controlAcces = true;
        }
        if (controlAcces) {
            if (bean.getPrix() < bean.getPrixMin()) {
                if (!autoriser("fv_can_reduce_prix")) {
                    getErrorMessage("Vous ne pouvez réduire le prix de vente de cet article au delà de prix minimale !");
                    openNotAcces();
                    return false;
                }
            }
        }
        return _controleFiche_(bean.getDocVente());
    }

    @Override
    public void populateView(DocVente bean) {
        cloneObject(docVente, bean);
        setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
    }

    public void populateViewEntete(EnteteDocVente bean) {
        cloneObject(entete, bean);
        entete.setTranche(bean.getTranchePoint());
    }

    public void populateViewContenu(ContenuDocVente bean) {
        if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
            bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, docVente.getDepot().getId(), 0, 0, entete.getDateEntete(), bean.getConditionnement().getId(), bean.getLot().getId()));
        } else {
            bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, 0, currentAgence.getId(), 0, entete.getDateEntete(), bean.getConditionnement().getId(), bean.getLot().getId()));
        }
        bean.getArticle().setPuv(dao.getPuv(bean.getArticle().getId(), bean.getQuantite(), bean.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), bean.getConditionnement().getId()));
        bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));
        selectArt = true;
        cloneObject(contenu, bean);

        YvsBaseArticles t = new YvsBaseArticles(bean.getArticle().getId());
        contenu.setPrixMin(dao.getPuvMin(t.getId(), bean.getQuantite(), bean.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), bean.getConditionnement().getId()));
        champ = new String[]{"article", "point"};
        val = new Object[]{t, new YvsBasePointVente(entete.getPoint().getId())};
        List<YvsBaseArticlePoint> la = dao.loadNameQueries("YvsBaseArticlePoint.findByArticlePoint", champ, val, 0, 1);
        if (la != null ? !la.isEmpty() : false) {
            contenu.getArticle().setChangePrix(la.get(0).getChangePrix());
        }
        if (contenu.getPrix() < 1) {
            contenu.getArticle().setChangePrix(true);
        }
        update("desc_article_commande_vente");
    }

    @Override
    public void resetFiche() {
        docVente = new DocVente();
        docVente.setDepot(UtilCom.buildBeanDepot(currentDepot));
        chooseDepot();
        selectDoc = new YvsComDocVentes();
        tabIds = "";
        taxes.clear();
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            m.resetFiche();
        }
        resetSubFiche();
        setClientDefaut();
        update("blog_form_commande_vente");
    }

    public void resetFicheEntete() {
        if (!autoriser("fv_update_header")) {
            openNotAcces();
            return;
        }
        entete = new EnteteDocVente();
        if (selectEntete != null) {
            ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (m != null) {
                m.loadPointVenteByDroit(true, true);
            }
        }
        selectEntete = new YvsComEnteteDocVente();
        entete.setDateEntete(new Date());
        entete.setUsers(UtilUsers.buildBeanUsers(currentUser.getUsers()));
        if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauPoint() != null : false) {
            entete.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauPoint().getTranche()));
        }
        entete.setPoint(new PointVente(currentPoint != null ? currentPoint.getId() : 0));
        choosePoint();
        entete.setDepot(new Depots(currentDepot != null ? currentDepot.getId() : 0));
        chooseDepot();
        resetFiche();
        update("save_entete_commande_vente");
        update("data_commande_vente");
        update("blog_form_commande_vente");
    }

    public void resetSubFiche() {
        resetFicheContenu();
        update("tabview_commande_vente");
    }

    public void resetFicheContenu() {
        contenu = new ContenuDocVente();
        selectContenu = new YvsComContenuDocVente();
        entityContenu = new YvsComContenuDocVente();
        selectArt = false;
        listArt = false;
        on_rabais = false;
        tabIds_contenu = "";
        update("blog_form_article_commande_vente");
        update("desc_article_commande_vente");
    }

    public void resetFicheReglement(boolean save) {
        reglement.setId(0);
        reglement.setMontant(0);
        reglement.setDatePaiement(new Date());
        reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        reglement.getPhases().clear();
        if (docVente != null) {
            if (docVente.getMontantResteApayer() > 0) {
                double m = docVente.getMontantResteApayer();
                for (YvsComptaCaissePieceVente r : docVente.getReglements()) {
                    m -= r.getMontant();
                }
                reglement.setMontant(m > 0 ? m : 0);
            }
        }
    }

    @Override
    public boolean saveNew() {
        if (_saveNew()) {
            succes();
            actionOpenOrResetAfter(this);
            return true;
        }
        return false;
    }

    public boolean _saveNew() {
        try {
//            if ((!autoriser("fv_save_doc")) || (!autoriser("fv_update_header") && (entete.getDateEntete().compareTo(new Date()) < 0))) {
//                openNotAcces();
//                return false;
//            }
            DocVente bean = recopieView();
            if (controleFiche(bean)) {
                selectDoc = buildDocVente(bean, selectEntete, currentUser);
                if (!bean.isUpdate()) {
                    selectDoc.setId(null);
                    selectDoc = (YvsComDocVentes) dao.save1(selectDoc);
                    bean.setId(selectDoc.getId());
                    docVente.setId(selectDoc.getId());
                    documents.add(0, selectDoc);
                    if (documents.size() > imax) {
                        documents.remove(documents.size() - 1);
                    }
                } else {
                    dao.update(selectDoc);
                    //si la facture a déjà un contenu, et que le client ou la catégorie comptable a changé, on recalcule les éléments du contenu
                    if (documents.contains(selectDoc)) {
                        YvsComDocVentes old = documents.get(documents.indexOf(selectDoc));
                        if (!old.getClient().equals(selectDoc.getClient()) || !old.getCategorieComptable().equals(selectDoc.getCategorieComptable())) {
                            for (YvsComContenuDocVente co : docVente.getContenus()) {
                                ContenuDocVente c = UtilCom.buildBeanContenuDocVente(co);
                                c.setUpdate(false);
                                findPrixArticle(c);
                                YvsComContenuDocVente cc = UtilCom.buildContenuDocVente(c, currentUser);
                                arrondiVal(cc);
                                dao.update(cc);
                                if (docVente.getContenus().contains(co)) {
                                    docVente.getContenus().set(docVente.getContenus().indexOf(co), cc);
                                }
                            }
                            if (!docVente.getContenus().isEmpty()) {
                                update("data_contenu_commande_vente");
//                                setMontantTotalDoc(docVente);
                                setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
                            }
                        }
                    }
                    if (documents.contains(selectDoc)) {
                        documents.set(documents.indexOf(selectDoc), selectDoc);
                    }
                }
                docVente.setNumDoc(bean.getNumDoc());
                docVente.setUpdate(true);
                setClientDefaut();
                update("form_entete_commande_vente");
                update("data_commande_vente");
                update("data_commande_vente_hist");
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void saveNewContenu(boolean continuSave) {
        try {
            on_rabais = false;
            ContenuDocVente bean = recopieViewContenu(docVente);
            if (controleFicheContenu(bean, continuSave)) {
                entityContenu = UtilCom.buildContenuDocVente(bean, currentUser);
                arrondiVal(entityContenu);
                if (!bean.isUpdate()) {
                    entityContenu.setId(null);
                    entityContenu = (YvsComContenuDocVente) dao.save1(entityContenu);
                    contenu.setId(entityContenu.getId());
                    docVente.getContenus().add(0, entityContenu);
                } else {
                    dao.update(entityContenu);
                    if (docVente.getContenus().contains(entityContenu)) {
                        docVente.getContenus().set(docVente.getContenus().indexOf(entityContenu), entityContenu);
                    }
                }
                int idx = documents.indexOf(new YvsComDocVentes(docVente.getId()));
                if (idx >= 0) {
                    int idx1 = documents.get(idx).getContenus().indexOf(entityContenu);
                    if (idx1 >= 0) {
                        documents.get(idx).getContenus().set(idx1, entityContenu);
                    } else {
                        documents.get(idx).getContenus().add(0, entityContenu);
                    }
                }
                setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
                loadTaxesVente(entityContenu.getDocVente());
                resetFicheContenu();
                succes();
                update("data_commande_vente");
                update("blog_form_montant_doc");
                update("data_contenu_commande_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public boolean saveNewEntete() {
        selectEntete = new YvsComEnteteDocVente();
        try {
            ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
            if (w != null) {
                if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                    selectEntete = selectDoc.getEnteteDoc();
                } else {
                    entete.getCrenauHoraire().setPersonnel(entete.getUsers());
                    selectEntete = w.saveNewEntete(entete, docVente, false);
                }
                if (selectEntete != null ? (selectEntete.getId() > 0 && entete.getId() < 1) : false) {
                    entete = UtilCom.buildBeanEnteteDocVente(selectEntete);
                    entete.setTranche(entete.getTranchePoint());
                }
                if (selectEntete != null ? selectEntete.getId() > 0 : false) {
                    entete.setId(selectEntete.getId());
                    entete.setUpdate(true);
                    entete.setNew_(true);
                    docVente.setEnteteDoc(entete);
                    //charge les dépôt de livraisons possible de cette entête
                    YvsBasePointVente pv = selectEntete.getCreneau().getCreneauPoint().getPoint();
                    if (pv != null) {
                        depotsLivraison.clear();
                        for (YvsBasePointVenteDepot pvd : pv.getDepots()) {
                            if (pvd.getDepot().getActif() && pvd.getActif()) {
                                depotsLivraison.add(pvd.getDepot());
                            }
                            if (pvd.getPrincipal()) {
                                docVente.setDepot(UtilCom.buildBeanDepot(pvd.getDepot()));
                            }
                        }
                    }
                    update("select_depot_liv_prevu");
                    update("save_entete_commande_vente");
                    return true;
                }
                entete.setUpdate(false);
                update("save_entete_commande_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void saveNewReglement(boolean deletePhase) {
        boolean update = reglement.getId() > 0;
        try {
            ManagedReglementVente m = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
            if (m != null) {
                PieceTresorerie bean = recopieViewPiece();
                if (!bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE) && !deletePhase) {
                    for (YvsComptaPhasePiece pp : reglement.getPhases()) {
                        if (pp.getPhaseOk()) {
                            openDialog("dlgConfirmChangeMode");
                            return;
                        }
                    }
                    if (!reglement.getPhases().isEmpty()) {
                        openDialog("dlgConfirmDeletePhase");
                        return;
                    }
                }
                YvsComptaCaissePieceVente piece = UtilCom.buildPieceVente(bean, currentUser);
                piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                if (piece.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && !bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE)) {
                    piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    getWarningMessage("Seules les règlements en espèces peuvent être validé avec ce schéma !");
                }
                if (bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE) && bean.getCaisse().getId() <= 0) {
                    piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    getWarningMessage("Aucune caisse n'a été indiqué!", "La pièce de caisse ne peut-être validé");
                }
                if (bean.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                    piece.setValideBy(currentUser.getUsers());
                    piece.setCaissier(currentUser.getUsers());
                    piece.setDatePaiement(piece.getDatePaimentPrevu());
                    piece.setDateValide(new Date());
                }
                piece.setVente(selectDoc);
                piece = m.createNewPieceCaisse(docVente, piece, deletePhase);
                if (piece != null ? piece.getId() > 0 : false) {
                    if (!update) {
                        docVente.getReglements().add(0, piece);
                    } else {
                        int idx = docVente.getReglements().indexOf(piece);
                        if (idx >= 0) {
                            docVente.getReglements().set(idx, piece);
                        } else {
                            docVente.getReglements().add(piece);
                        }
                    }
                    int idx = documents.indexOf(selectDoc);
                    if (idx >= 0) {
                        int idx1 = documents.get(idx).getReglements().indexOf(piece);
                        if (idx1 >= 0) {
                            documents.get(idx).getReglements().set(idx1, piece);
                        } else {
                            documents.get(idx).getReglements().add(0, piece);
                        }
                    }
                    if (bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE) && reglement.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                        m.reglerPieceTresorerie(piece, "F", true);
                    } else {
                        succes();
                    }
                }
                resetFicheReglement(true);
            }
        } catch (Exception ex) {
            getErrorMessage(update ? "Modification" : "Insertion" + " Impossible !");
            getException("Lymytz Error...", ex);
        }
    }

    public void saveDefautClient() {
        try {
            ManagedClient s = (ManagedClient) giveManagedBean(ManagedClient.class);
            if (s != null) {
                YvsComClient e = s.saveDefautClient();
                if (e != null ? e.getId() > 0 : false) {
                    cloneObject(docVente.getClient(), UtilCom.buildBeanClient(e));
                    update("select_client_commande_vente");
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
        }
    }

    public void saveZone() {
        ManagedDico a = (ManagedDico) giveManagedBean("Mdico");
        if (a != null) {
            YvsDictionnaire y = a.save();
            if (y != null) {
                docVente.setAdresse(UtilGrh.buildBeanDictionnaire(y));
            }
            a.resetFiche();
        }
    }

    public void addCommentaireContenu(YvsComContenuDocVente y) {
        on_rabais = false;
        selectContenu = y;
        commentaire = y.getCommentaire();
        update("txt_commentaire_contenu_commande_vente");
    }

    public void addCommentaireContenu() {
        if (!on_rabais) {
            if (selectContenu != null ? selectContenu.getId() > 0 : false) {
                if (selectContenu.getRabais() > 0 && (commentaire != null ? commentaire.trim().length() < 1 : true)) {
                    getErrorMessage("Impossible d'effacer ce commentaire");
                    return;
                }
                selectContenu.setCommentaire(commentaire);
                selectContenu.setAuthor(currentUser);
                dao.update(selectContenu);
                if (docVente.getContenus().contains(selectContenu)) {
                    docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
                }
                update("data_contenu_commande_vente");
                succes();
            } else {
                getErrorMessage("Vous devez selectionner un contenu");
            }
        } else {
            if (!autoriser("fv_apply_rabais")) {
                openNotAcces();
                return;
            }
            contenu.setRabais(contenu.getPrixMin() - contenu.getPrix());
            contenu.setPrix(contenu.getPrixMin());
            contenu.setCommentaire(commentaire);
            saveNewContenu(true);
        }
    }

    public void addNumSerieContenu(YvsComContenuDocVente y) {
        selectContenu = y;
        numSerie = y.getNumSerie();
        update("txt_num_serie_contenu_commande_vente");
    }

    public void addNumSerieContenu() {
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setNumSerie(numSerie);
            selectContenu.setAuthor(currentUser);
            dao.update(selectContenu);
            if (docVente.getContenus().contains(selectContenu)) {
                docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
            }
            update("data_contenu_commande_vente");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addRabaisContenu(YvsComContenuDocVente y) {
        selectContenu = y;
        montant_rabais = y.getRabais();
        commentaire = y.getCommentaire();
        update("txt_rabais_contenu_commande_vente");
    }

    public void addRabaisContenu() {
        if (!autoriser("fv_apply_rabais")) {
            openNotAcces();
            return;
        }
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            if (montant_rabais > 0 && (commentaire != null ? commentaire.trim().length() < 1 : true)) {
                getErrorMessage("Impossible d'effacer ce commentaire");
                return;
            }
            selectContenu.setRabais(montant_rabais);
            selectContenu.setCommentaire(montant_rabais > 0 ? commentaire : "");
            selectContenu.setAuthor(currentUser);
            dao.update(selectContenu);
            if (docVente.getContenus().contains(selectContenu)) {
                docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
            }
            setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
            update("data_contenu_commande_vente");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addRemiseContenu(YvsComContenuDocVente y) {
        selectContenu = y;
        montant_remise = y.getRemise();
        taux_remise = y.getTauxRemise();
        update("txt_remise_contenu_commande_vente");
    }

    public void addRemiseContenu() {
        if (!autoriser("fv_apply_remise")) {
            openNotAcces();
            return;
        }
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setRemise(montant_remise);
            selectContenu.setAuthor(currentUser);
            dao.update(selectContenu);
            if (docVente.getContenus().contains(selectContenu)) {
                docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
            }
            setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
            update("data_contenu_commande_vente");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("bcv_delete_doc")) {
                openNotAcces();
                return;
            }
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComDocVentes> list = new ArrayList<>();
                YvsComDocVentes bean;
                for (Long ids : l) {
                    bean = documents.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    if (!_controleFiche_(bean)) {
                        return;
                    }
                    dao.delete(bean);

                    if (bean.getId() == docVente.getId()) {
                        resetFiche();
                        update("blog_form_commande_vente");
                    }
                }
                documents.removeAll(list);
                succes();
                tabIds = "";
                update("data_commande_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComDocVentes y) {
        selectDoc = y;
    }

    public void deleteBean_() {
        try {
            if (!autoriser("bcv_delete_doc")) {
                openNotAcces();
                return;
            }
            if (selectDoc != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                dao.delete(selectDoc);
                documents.remove(selectDoc);
                if (selectDoc.getId() == docVente.getId()) {
                    resetFiche();
                    update("blog_form_commande_vente");
                }
                succes();
                update("data_commande_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContenu() {
        try {
            if ((tabIds_contenu != null) ? !tabIds_contenu.equals("") : false) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                String[] tab = tabIds_contenu.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComContenuDocVente bean = docVente.getContenus().get(docVente.getContenus().indexOf(new YvsComContenuDocVente(id)));
                    dao.delete(bean);
                    docVente.getContenus().remove(bean);
                    if (id == contenu.getId()) {
                        resetFicheContenu();
                        update("form_contenu_commande_vente");
                    }
                }
                setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
                succes();
                update("data_contenu_commande_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContenu_(YvsComContenuDocVente y) {
        selectContenu = y;
    }

    public void deleteBeanContenu_() {
        try {
            if (selectContenu != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                dao.delete(selectContenu);
                docVente.getContenus().remove(selectContenu);
                if (selectContenu.getId() == contenu.getId()) {
                    resetFicheContenu();
                    update("form_contenu_commande_vente");
                }
                setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
                succes();
                update("data_contenu_commande_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanReglement_(YvsComptaCaissePieceVente y) {
        selectReglement = y;
    }

    public void deleteBeanReglement_() {
        try {
            if (selectReglement != null) {
                if (selectReglement.getId() > 0) {
                    dao.delete(selectReglement);
                }
                docVente.getReglements().remove(selectReglement);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectDistant(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Commandes Vente", "modGescom", "smenBonCommandVente", true);
            }
        }
    }

    // Chargement de la vue.... meme a distance
    @Override
    public void onSelectObject(YvsComDocVentes y) {
        selectDoc = y;
        populateView(UtilCom.buildBeanDocVente(selectDoc));
        entete_ = selectDoc.getEnteteDoc().getId();
        chooseEntete(false);
        choosePoint();
        chooseDepot();
        loadOthersDetailDoc(selectDoc);
        resetFicheContenu();
        resetSubFiche();
        update("blog_form_commande_vente");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
            onSelectObject(y);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_commande_vente");
    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocVente bean = (YvsComContenuDocVente) ev.getObject();
            populateViewContenu(UtilCom.buildBeanContenuDocVente(bean));
            update("form_contenu_commande_vente");
            update("desc_article_commande_vente");
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheContenu();
        update("form_contenu_commande_vente");
    }

    public void loadOnViewMensualite(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectReglement = (YvsComptaCaissePieceVente) ev.getObject();
            reglement = UtilCom.buildBeanPieceVente(selectReglement);
            if (reglement.getMode() != null ? reglement.getMode().getId() < 1 : true) {
                reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
            }
            loadCaissiers(selectReglement.getCaisse());
            update("blog_form_mensualite_facture_vente");
        }
    }

    public void unLoadOnViewMensualite(UnselectEvent ev) {
        resetFicheReglement(false);
        update("blog_form_mensualite_facture_vente");
    }

    public void loadOnViewTranche(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            if (docVente.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                YvsGrhTrancheHoraire t = (YvsGrhTrancheHoraire) ev.getObject();
                docVente.setTranche(UtilGrh.buildTrancheHoraire(t));
                update("select_tranche_livraison_bcv");
            } else {
                getErrorMessage("Impossible de modifier ce document");
            }
        }
    }

    public void loadOnViewClient(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComClient y = (YvsComClient) ev.getObject();
            chooseClient(UtilCom.buildBeanClient(y));
            update("infos_commande_vente");
        }
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildBeanArticles(bean));
            listArt = false;
            update("form_contenu_commande_vente");
        }
    }

    public void chooseArticle(Articles t) {
        if ((t != null) ? t.getId() > 0 : false) {
            if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
                t.setStock(dao.stocks(t.getId(), 0, docVente.getDepot().getId(), 0, 0, entete.getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
            } else {
                t.setStock(dao.stocks(t.getId(), 0, 0, currentAgence.getId(), 0, entete.getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
            }
            selectArt = true;
            t.setPuv(dao.getPuv(t.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId()));
            t.setPua(dao.getPua(t.getId(), 0));
            if (!contenu.isUpdate()) {
                contenu.setPrix(t.getPuv());
            }
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(t.getId())});
            t.setConditionnements(unites);
            contenu.setPrixMin(dao.getPuvMin(t.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId()));
            contenu.setPr(dao.getPr(t.getId(), entete.getDepot().getId(), 0, entete.getDateEntete(), contenu.getConditionnement().getId()));
            cloneObject(contenu.getArticle(), t);

            champ = new String[]{"article", "point"};
            val = new Object[]{new YvsBaseArticles(t.getId()), new YvsBasePointVente(entete.getPoint().getId())};
            List<YvsBaseArticlePoint> la = dao.loadNameQueries("YvsBaseArticlePoint.findByArticlePoint", champ, val, 0, 1);
            if (la != null ? !la.isEmpty() : false) {
                contenu.getArticle().setChangePrix(la.get(0).getChangePrix());
            }
            if (contenu.getPrix() < 1) {
                contenu.getArticle().setChangePrix(true);
            }
        } else {
            resetFicheContenu();
        }
        update("desc_article_commande_vente");
    }

    public void chooseConditionnement() {
        if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
            if (contenu.getArticle() != null) {
                int idx = contenu.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(contenu.getConditionnement().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = contenu.getArticle().getConditionnements().get(idx);
                    contenu.setConditionnement(UtilProd.buildBeanConditionnement(y));
                }
            }
        }
        loadInfosArticle(contenu.getArticle());
    }

    public void searchArticle() {
        String num = contenu.getArticle().getRefArt();
        contenu.getArticle().setDesignation("");
        contenu.getArticle().setError(true);
        contenu.getArticle().setId(0);
        listArt = false;
        selectArt = false;
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            Articles y = m.searchArticleActifByPoint(num, true);
            if (m.getArticles() != null ? !m.getArticles().isEmpty() : false) {
                if (m.getArticles().size() > 1) {
                    update("data_articles_commande_vente");
                } else {
                    chooseArticle(y);
                }
                contenu.getArticle().setError(false);
            }
            listArt = y.isListArt();
            selectArt = y.isSelectArt();
        }
    }

    public void initArticles() {
        listArt = false;
        ManagedStockArticle a = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (a != null) {
            a.initArticlesByPoint(contenu.getArticle());
            listArt = contenu.getArticle().isListArt();
        }
        update("data_articles_commande_vente");
        update("img_article");
    }

    public void searchClient() {
        String num = docVente.getClient().getCodeClient();
        docVente.getClient().setId(0);
        docVente.getClient().setError(true);
        docVente.getClient().setTiers(new Tiers());
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            Client y = m.searchClient(num, true);
            if (m.getClients() != null ? !m.getClients().isEmpty() : false) {
                if (m.getClients().size() > 1) {
                    update("data_client_commande_vente");
                } else {
                    chooseClient(y);
                }
                docVente.getClient().setError(false);
            }
        }
    }

    public void initClients() {
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            m.initClients(docVente.getClient());
        }
        update("data_client_commande_vente");
    }

    public void init(boolean next) {
        loadAllBons(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllBons(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        loadAllBons(true, false);
    }

    public void chooseCloturer(ValueChangeEvent ev) {
        cloturer_ = ((Boolean) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("y.cloturer", "cloturer", cloturer_);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void addParamStatut() {
        ParametreRequete p;
        if (statut_ != null ? statut_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statut", "statut", statut_, egaliteStatut, "AND");
        } else {
            p = new ParametreRequete("y.statut", "statut", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void chooseStatut(ValueChangeEvent ev) {
        statut_ = ((String) ev.getNewValue());
        addParamStatut();
    }

    public void chooseFacturer(ValueChangeEvent ev) {
        statutLivre_ = ((String) ev.getNewValue());
        ParametreRequete p;
        if (statutLivre_ != null ? statutLivre_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutLivre", "statutLivre", statutLivre_);
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.statutLivre", "statutLivre", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void chooseDateSearch() {
        ParametreRequete p;
        if (date_) {
            p = new ParametreRequete("heureDoc", "heureDoc", dateDebut_);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFin_);
        } else {
            p = new ParametreRequete("heureDoc", "heureDoc", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    //méthode de recherche par header
    public void chooseEntete(boolean _new) {
        if (entete_ > 0) {
            ManagedVente v = (ManagedVente) giveManagedBean(ManagedVente.class);
            if (v != null) {
                if (v.getDocuments().contains(new YvsComEnteteDocVente(entete_))) {
                    selectEntete = v.getDocuments().get(v.getDocuments().indexOf(new YvsComEnteteDocVente(entete_)));
                } else {
                    selectEntete = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{entete_});
                }
                if (selectEntete != null ? selectEntete.getId() > 0 : false) {
                    populateViewEntete(UtilCom.buildBeanEnteteDocVente(selectEntete));
                    ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                    if (m != null && (selectEntete.getCreneau() != null ? selectEntete.getCreneau().getCreneauPoint() != null ? selectEntete.getCreneau().getCreneauPoint().getPoint() != null : false : false)) {
                        YvsBasePointVente y = selectEntete.getCreneau().getCreneauPoint().getPoint();
                        if (!m.getPointsvente().contains(y)) {
                            m.getPointsvente().add(0, y);
                            loadDepotByPoint(y);
                        }
                    }
                    ManagedStockArticle service = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                    if (service != null) {
                        if (selectEntete.getCreneau().getCreneauPoint() != null) {
                            service.setEntityPoint(selectEntete.getCreneau().getCreneauPoint().getPoint());
                            service.loadActifArticleByPoint(true, true);
                        } else {
                            getWarningMessage("Point de vente absent !");
                        }
                    }
                    if (_new) {
                        resetFiche();
                    }
                    entete.setNew_(true);
                }
                update("save_entete_commande_vente");
                update("data_commande_vente");
                update("blog_form_commande_vente");
                return;
            }
        }
        update("save_entete_commande_vente");
        update("data_commande_vente");
        update("blog_form_commande_vente");
    }

    @Override
    public void _chooseSociete() {
        super._chooseSociete();
    }

    @Override
    public void _chooseAgence() {
        super._chooseAgence();
        loadAllBons(true, true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamAgence(agence_);
        }
    }

    @Override
    public void _chooseDepot() {
        super._chooseDepot();
        ParametreRequete p;
        if (depot_ > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.depot", "depot", new YvsBaseDepots(depot_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.depot", "depot", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    @Override
    public void _choosePoint() {
        super._choosePoint();
        ParametreRequete p;
        if (point_ > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point", "point", new YvsBasePointVente(point_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point", "point", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamPoint(point_);
        }
    }

    public void _chooseTranche() {
        ParametreRequete p;
        if (tranche_ > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.tranche", "tranche", new YvsGrhTrancheHoraire(tranche_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.tranche", "tranche", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void chooseStatutRegle(ValueChangeEvent ev) {
        statutRegle_ = ((String) ev.getNewValue());
        addParamStatutRegle();
    }

    private void addParamStatutDoc() {
        ParametreRequete p;
        if (statutRegle_ != null ? statutRegle_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutRegle", "statutRegle", statutRegle_, egaliteStatutR, "AND");
        } else {
            p = new ParametreRequete("y.statutRegle", "statutRegle", null);
        }
        paginator.addParam(p);
    }

    public void addParamStatutRegle() {
        addParamStatutDoc();
        loadAllBons(true, true);
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
        loadAllBons(true, true);
    }

    public void addParamAutoLivre() {
        ParametreRequete p = new ParametreRequete("y.livraisonAuto", "livraisonAuto", null);
        if (autoLivreSearch != null) {
            p = new ParametreRequete("y.livraisonAuto", "livraisonAuto", autoLivreSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    // Recherche des factures en selectionnant le vendeur
    public void _chooseVendeur() {
        addParamVendeur(false);
        loadAllBons(true, true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamVendeur(codeVendeur_);
        }
    }

    private void loadCaissiers(YvsBaseCaisse y) {
        caissiers.clear();
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            caissiers = w.loadCaissiers(y);
        }
        if (y != null ? y.getId() > 0 : false) {
            if (reglement.getCaissier() != null ? reglement.getCaissier().getId() < 1 : true) {
                reglement.setCaissier(UtilUsers.buildSimpleBeanUsers(y.getCaissier()));
            }
            if ((reglement.getCaissier() != null ? reglement.getCaissier().getId() < 1 : true) && caissiers.contains(currentUser.getUsers())) {
                reglement.setCaissier(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
            }
        }
        update("tabview_commande_vente:chmp_caissier_reglement_bcv");
    }

    public void chooseCaisseAnnule() {
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service != null) {
            int idx = service.getCaisses().indexOf(new YvsBaseCaisse(caisseAnnule.getId()));
            if (idx > -1) {
                YvsBaseCaisse y = service.getCaisses().get(idx);
                caisseAnnule = UtilCompta.buildBeanCaisse(y);
            }
        }
    }

    public void chooseCaisses(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            // trouve les caisses parent d'une caisse données
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            long id = (long) ev.getNewValue();
            caissiers.clear();
            reglement.setCaissier(new Users());
            if (service != null && id > 0) {
                int idx = service.getCaisses().indexOf(new YvsBaseCaisse(id));
                if (idx > -1) {
                    YvsBaseCaisse y = service.getCaisses().get(idx);
                    reglement.setCaisse(UtilCompta.buildBeanCaisse(y));
                    loadCaissiers(y);
                }
            }
            update("tabview_commande_vente:chmp_caissier_reglement_bcv");
        }
    }
    YvsComptaCaissePieceVente piece;
    String source;
    boolean emission;

    public void openConfirmPaiement(YvsComptaCaissePieceVente pc, String source, boolean emission) {
        this.piece = pc;
        this.source = source;
        this.emission = emission;
        if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_VENTE)) {
            openDialog("dlgConfirmAnnulePiece");
            return;
        }
        ManagedReglementVente service = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
        if (service != null) {
            pc = service.openConfirmPaiement(pc, source, true, true, emission);
            int idx = docVente.getReglements().indexOf(pc);
            if (idx > -1) {
                docVente.getReglements().set(idx, pc);
            }
        }
    }

    public void openConfirmPaiement() {
        ManagedReglementVente service = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
        if (service != null) {
            piece = service.openConfirmPaiement(piece, source, true, true, emission);
            int idx = docVente.getReglements().indexOf(piece);
            if (idx > -1) {
                docVente.getReglements().set(idx, piece);
            }
        }
    }

    // Recherche des factures en ecrivant le nom du vendeur
    public void _searchVendeur() {
        addParamVendeur(true);
        loadAllBons(true, true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamVendeur(codeVendeur_);
        }
    }

    public void addParamVendeur(boolean code) {
        ParametreRequete p;
        if (!code) {
            if (users_ > 0) {
                p = new ParametreRequete("y.enteteDoc.creneau.users", "vendeur", new YvsUsers(users_));
                p.setOperation("=");
                p.setPredicat("AND");
            } else {
                p = new ParametreRequete("y.enteteDoc.creneau.users", "vendeur", null);
            }
        } else {
            if (codeVendeur_ != null ? codeVendeur_.trim().length() > 0 : false) {
                p = new ParametreRequete(null, "vendeur", codeVendeur_.toUpperCase() + "%", " LIKE ", "AND");
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.enteteDoc.creneau.users.codeUsers)", "vendeur", codeVendeur_.toUpperCase() + "%", " LIKE ", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.enteteDoc.creneau.users.nomUser)", "vendeur", codeVendeur_.toUpperCase() + "%", " LIKE ", "OR"));
            } else {
                p = new ParametreRequete("enteteDoc.creneau.users.codeUsers", "vendeur", null);
            }
        }
        paginator.addParam(p);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("enteteDoc.dateEntete", "dateEntete", dateDebut_);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void searchByClient() {
        ParametreRequete p;
        if (codeClient_ != null ? codeClient_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "client", codeClient_.toUpperCase() + "%", " LIKE ", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.codeClient)", "client", codeClient_.toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.nom)", "client", codeClient_.toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.prenom)", "client", codeClient_.toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p = new ParametreRequete("y.client.codeClient", "client", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void _chooseStatut(ValueChangeEvent ev) {
        statut = ((String) ev.getNewValue());
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamStatut(statut);
        }
    }

    public void _chooseDateSearch() {
        ParametreRequete p;
        if (date) {
            p = new ParametreRequete("y.enteteDoc.dateEntete", "dateEntete", dateDebut);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFin);
        } else {
            p = new ParametreRequete("y.enteteDoc.dateEntete", "dateEntete", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamDate(date, dateDebut, dateFin);
        }
        update("_select_entete_commande_vente");
    }

    public void chooseVille() {
        docVente.setAdresse(new Dictionnaire());
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.chooseVille(new Dictionnaire(1), docVente.getVille().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(docVente.getVille(), d);
            }
        }
    }

    public void chooseAdresse() {
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.chooseSecteur(docVente.getVille(), docVente.getAdresse().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(docVente.getAdresse(), d);
            }
        }
    }

    public void chooseCategorie() {
        if ((docVente.getCategorieComptable() != null) ? docVente.getCategorieComptable().getId() > 0 : false) {
            ManagedCatCompt m = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
            if (m != null) {
                YvsBaseCategorieComptable d_ = m.getCategories().get(m.getCategories().indexOf(new YvsBaseCategorieComptable(docVente.getCategorieComptable().getId())));
                CategorieComptable d = UtilCom.buildBeanCategorieComptable(d_);
                cloneObject(docVente.getCategorieComptable(), d);
            }
        } else {
            docVente.setCategorieComptable(new CategorieComptable());
        }
//        setMontantTotalDoc(docVente);
        setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
    }

    public void chooseClient(Client d) {
        if ((d != null) ? d.getId() > 0 : false) {
            cloneObject(docVente.getClient(), d);
            if (d.getCategorieComptable() != null) {
                cloneObject(docVente.getCategorieComptable(), d.getCategorieComptable());
                update("select_categorie_comptable_commande_vente");
            }
            if (docVente.getClient() != null ? docVente.getClient().getModel() != null : false) {
                docVente.setModeReglement(docVente.getClient().getModel());
            } else {
                YvsBaseCategorieClient c = currentCategorieClient(d);
                if (c != null ? c.getModel() != null : false) {
                    docVente.setModeReglement(UtilCom.buildBeanModelReglement(c.getModel()));
                } else {
                    getWarningMessage("Ce client n'a pas de catégorie client!");
                }
            }
            docVente.getClient().setNom(d.getNom_prenom());
            docVente.setNomClient(d.getNom_prenom());
            docVente.setTelephone(d.getTiers().getTelephone());
            docVente.setVille(new Dictionnaire());
            docVente.setAdresse(new Dictionnaire());
            if (d.getTiers() != null ? d.getTiers().getId() > 0 : false) {
                boolean correct = false;
                if (d.getTiers().getVille() != null ? d.getTiers().getVille().getId() > 0 : false) {
                    cloneObject(docVente.getVille(), d.getTiers().getVille());
                    chooseVille();
                    if (d.getTiers().getSecteur() != null ? d.getTiers().getSecteur().getId() > 0 : false) {
                        cloneObject(docVente.getAdresse(), d.getTiers().getSecteur());
                        chooseAdresse();
                        correct = true;
                    }
                }
                if (!correct) {
                    cloneObject(docVente.getAdresse(), d.getTiers().getSecteur_());
                }
            }
            update("select_model_commande_vente");
        }
    }

    public void chooseDate() {
//        if (!autoriser("fv_update_header")) {
//            entete.setDateEntete(new Date());
//            openNotAcces();
//            return;
//        }
        selectEntete = new YvsComEnteteDocVente();
    }

    public void chooseTranche() {
        selectEntete = new YvsComEnteteDocVente();
        if (entete.getTranche() != null ? entete.getTranche().getId() > 0 : false) {
            for (YvsComCreneauPoint cp : entete.getPoint().getListTranche()) {
                if (cp.getTranche().getId().equals(entete.getTranche().getId())) {
                    TrancheHoraire t = UtilCom.buildBeanTrancheHoraire(cp.getTranche());
                    cloneObject(entete.getTranche(), t);
                    break;
                }
            }
        }
    }

    public void choosePoint_(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                entete.getPoint().setId(id);
                choosePoint();
            } else {
                boolean pagine = false;
                boolean next = false;
                if (id == -1) {
                    pagine = true;
                    next = false;
                } else if (id == -2) {
                    pagine = true;
                    next = true;
                }
                if (pagine) {
                    ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                    if (m != null) {
                        m.loadPointVenteByDroit(next, false);
                    }
                }
                entete.getPoint().setId(0);
            }
        }
    }

    public void choosePoint() {
        selectEntete = new YvsComEnteteDocVente();
        if (entete.getPoint() != null ? entete.getPoint().getId() > 0 : false) {
            ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (service != null) {
                int idx = service.getPointsvente().indexOf(new YvsBasePointVente(entete.getPoint().getId()));
                if (idx >= 0) {
                    YvsBasePointVente y = service.getPointsvente().get(idx);
                    entete.setPoint(UtilCom.buildBeanPointVente(y));
                    ManagedStockArticle service_ = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                    if (service_ != null) {
                        service_.setEntityPoint(y);
                        service_.loadActifArticleByPoint(true, true);
                    }
                    loadDepotByPoint(y);
                    service.loadCurrentCreneaux(y, entete.getDateEntete());
                    //trouve le vendeur
                    vendeurs = dao.loadNameQueries("YvsComCreneauHoraireUsers.findUsersByPoint", new String[]{"point"}, new Object[]{y});
                    if (vendeurs.contains(currentUser.getUsers())) {
                        entete.setUsers(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
                    }
                }
            }
        }
    }

    public void chooseDepot() {
        docVente.setTranche(new TrancheHoraire());
        if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
            int idx = depotsLivraison.indexOf(new YvsBaseDepots(docVente.getDepot().getId()));
            if (idx > -1) {
                YvsBaseDepots y = depotsLivraison.get(idx);
                Depots d = UtilCom.buildBeanDepot(y);
                cloneObject(docVente.getDepot(), d);
                loadAllTranche(y, docVente.getDateLivraisonPrevu());
                if (!verifyOperation(docVente.getDepot(), Constantes.SORTIE, Constantes.VENTE, false)) {

                }
                if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false) {
                    docVente.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
                    if (selectDoc != null ? selectDoc.getId() > 0 ? !selectDoc.getStatut().equals(Constantes.ETAT_EDITABLE) : false : false) {
                        selectDoc.setTrancheLivrer(currentPlanning.get(0).getCreneauDepot().getTranche());
                        String query = "UPDATE yvs_com_doc_ventes SET tranche_livrer = ? WHERE id = ?";
                        dao.requeteLibre(query, new Options[]{new Options(docVente.getTranche().getId(), 1), new Options(selectDoc.getId(), 2)});
                        int index = documents.indexOf(selectDoc);
                        if (index > -1) {
                            documents.set(index, selectDoc);
                        }
                    }
                }
                update("select_tranche_livraison_bcv");
            }
        }
    }

    public void initInfosContenu() {
        if ((entityContenu != null) ? entityContenu.getId() > 0 : false) {
            update("blog_form_infos_contenu");
        }
    }

    public void initFactureVente() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            update("blog_form_infos_contenu");
        }
    }

    public void setTotalReste(double montant) {
        docVente.setMontantCS(docVente.getMontantCS() + montant);
        docVente.setMontantResteApayer(docVente.getMontantResteApayer() - montant);
        update("blog_form_montant_doc");
    }

    public void setClientDefaut() {
        if (docVente.getClient() != null ? docVente.getClient().getId() < 1 : true) {
            YvsComClient c = currentClientDefault();
            if (c != null ? c.getId() > 0 : false) {
                chooseClient(UtilCom.buildBeanClient(c));
            } else {
                openDialog("dlgConfirmAddClient");
                docVente.setClient(new Client());
            }
            update("select_client_commande_vente");
        }
    }

    public void loadInfosArticle(Articles art) {
        if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
            art.setStock(dao.stocks(art.getId(), 0, docVente.getDepot().getId(), 0, 0, entete.getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
        } else {
            art.setStock(dao.stocks(art.getId(), 0, 0, currentAgence.getId(), 0, entete.getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
        }
        art.setPuv(dao.getPuv(art.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId()));
        contenu.setPrixMin(dao.getPuvMin(art.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId()));
        selectArt = true;
        art.setPua(dao.getPua(art.getId(), 0));
        if (!contenu.isUpdate()) {
            contenu.setPrix(art.getPuv());
        }
        contenu.setPr(dao.getPr(art.getId(), entete.getDepot().getId(), 0, entete.getDateEntete(), contenu.getConditionnement().getId()));
        cloneObject(contenu.getArticle(), art);

        champ = new String[]{"article", "point"};
        val = new Object[]{new YvsBaseArticles(art.getId()), new YvsBasePointVente(entete.getPoint().getId())};
        YvsBaseArticlePoint la = (YvsBaseArticlePoint) dao.loadOneByNameQueries("YvsBaseArticlePoint.findByArticlePoint", champ, val);
        if (la != null) {
            contenu.getArticle().setChangePrix(la.getChangePrix());
            contenu.getArticle().setPuvTtc(la.getArticle().getPuvTtc());
            contenu.setConditionnement(UtilProd.buildBeanConditionnement(la.getConditionementVente()));
        }
        if ((contenu.getConditionnement() != null) ? contenu.getConditionnement().getId() <= 0 : true) {
            //récupère le conditionnement de vente au niveau de l'article
            for (YvsBaseConditionnement cd : art.getConditionnements()) {
                if (cd.getByVente()) {
                    contenu.setConditionnement(UtilProd.buildBeanConditionnement(cd));
                }
            }

        }
        if (contenu.getPrix() < 1) {
            contenu.getArticle().setChangePrix(true);
        }
        if (contenu.getQuantite() > 0) {
            onPrixBlur();
        }
    }

    public void onPrixBlur() {
        if ((docVente.getClient() != null) ? docVente.getClient().getId() > 0 : false) {
            double prix = contenu.getPrix() - contenu.getRabais();
            double total = contenu.getQuantite() * prix;
            double _remise = dao.getRemiseVente(contenu.getArticle().getId(), contenu.getQuantite(), prix, docVente.getClient().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId());
            if (entityContenu != null ? entityContenu.getId() > 0 : false) {
                if (contenu.isUpdate() && (entityContenu.getQuantite() == contenu.getQuantite()) && (entityContenu.getPrix() == contenu.getPrix())) {
                    _remise = entityContenu.getRemise();
                }
            }
            contenu.setRemise(_remise);
            contenu.setPrixTotal(total - contenu.getRemise());
            long categorie = 0;
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                if (selectDoc.getCategorieComptable() != null ? selectDoc.getCategorieComptable().getId() > 0 : false) {
                    categorie = selectDoc.getCategorieComptable().getId();
                }
            } else {
                if (docVente.getCategorieComptable() != null ? docVente.getCategorieComptable().getId() > 0 : false) {
                    categorie = docVente.getCategorieComptable().getId();
                }
            }
            if (categorie > 0) {
                contenu.setTaxe(dao.getTaxe(contenu.getArticle().getId(), categorie, 0, contenu.getRemise(), contenu.getQuantite(), prix, true, 0));
                contenu.setPrixTotal(contenu.getPrixTotal() + (contenu.getArticle().isPuvTtc() ? 0 : contenu.getTaxe()));
            } else {
                getWarningMessage("Selectionner la catégorie comptable!");
            }
            contenu.setPrixTotal(contenu.getPrixTotal() > 0 ? contenu.getPrixTotal() : 0);
        } else {
            getWarningMessage("Selectionner le client!");
        }
    }

    public void onQuantiteBlur() {
        findPrixArticle(contenu);
    }

    public ContenuDocVente findPrixArticle(ContenuDocVente c) {
        if ((docVente.getClient() != null) ? docVente.getClient().getId() > 0 : false) {
            if (!c.isUpdate()) {
                c.setPrix(dao.getPuv(c.getArticle().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId()));
            }
            double prix = c.getPrix() - c.getRabais();
            double total = c.getQuantite() * prix;
            double _remise = dao.getRemiseVente(c.getArticle().getId(), c.getQuantite(), prix, docVente.getClient().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId());
            if (selectContenu != null ? selectContenu.getId() > 0 : false) {
                if (c.isUpdate() && (selectContenu.getQuantite() == c.getQuantite() && selectContenu.getPrix() == c.getPrix())) {
                    _remise = selectContenu.getRemise();
                }
            }
            c.setRemise(_remise);
            c.setPrixTotal(total - c.getRemise());
            c.setPrixMin(dao.getPuvMin(c.getArticle().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId()));
            long categorie = 0;
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                if (selectDoc.getCategorieComptable() != null ? selectDoc.getCategorieComptable().getId() > 0 : false) {
                    categorie = selectDoc.getCategorieComptable().getId();
                }
            } else {
                if (docVente.getCategorieComptable() != null ? docVente.getCategorieComptable().getId() > 0 : false) {
                    categorie = docVente.getCategorieComptable().getId();
                }
            }
            if (categorie > 0) {
                c.setTaxe(dao.getTaxe(c.getArticle().getId(), categorie, 0, c.getRemise(), c.getQuantite(), prix, true, 0));
                c.setPrixTotal(c.getPrixTotal() + (c.getArticle().isPuvTtc() ? 0 : c.getTaxe()));
            } else {
                getWarningMessage("Selectionner la catégorie comptable!");
            }
        } else {
            getWarningMessage("Selectionner le client!");
        }
        return c;
    }

    public void onRemiseBlur(boolean taux) {
        double total = selectContenu.getQuantite() * (selectContenu.getPrix() - selectContenu.getRabais());
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

    public void onRabaisBlur(boolean total) {
        if (total) {
            montant_rabais = montant_rabais_total / selectContenu.getQuantite();
        } else {
            montant_rabais_total = montant_rabais * selectContenu.getQuantite();
        }
    }

    public void annulerOrder(YvsComDocVentes selectDoc, DocVente docVente, String type, boolean force) {
        this.selectDoc = selectDoc;
        this.docVente = docVente;
        this.type = type;
        annulerOrder(force);
    }

    public void annulerOrder(boolean force) {
        annulerOrder(docVente, selectDoc, force);
    }

    public void annulerOrder(DocVente docVente, YvsComDocVentes selectDoc, boolean force) {
        visibleFact = false;
        visibleLivr = false;
        visibleRegl = false;
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            visibleCmde = true;
            List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
            if (force ? true : ((l != null ? l.isEmpty() : true) && selectDoc.getReglements().isEmpty())) {
                if (changeStatut(Constantes.ETAT_EDITABLE, docVente, selectDoc)) {
                    int lenght = docVente.getReglements().size();
                    YvsComptaCaissePieceVente p;
                    for (int i = 0; i < lenght; i++) {
                        p = selectDoc.getReglements().get(i);
                        if (p.getMouvement().equals(Constantes.MOUV_CAISS_SORTIE.charAt(0))) {
                            dao.delete(p);
                            selectDoc.getReglements().remove(p);
                        }
                    }
                    if (type.equals(Constantes.TYPE_BCV)) {
                        update("tabview_commande_vente:data_mensualite_commande_vente");
                    } else {
                        update("tabview_facture_vente:data_mensualite_facture_vente");
                    }
                    selectDoc.setCloturer(false);
                    selectDoc.setAnnulerBy(null);
                    selectDoc.setValiderBy(null);
                    selectDoc.setCloturerBy(null);
                    selectDoc.setDateAnnuler(null);
                    selectDoc.setDateCloturer(null);
                    selectDoc.setDateValider(null);
                    if (currentUser != null ? currentUser.getId() > 0 : false) {
                        selectDoc.setAuthor(currentUser);
                    }
                    dao.update(selectDoc);
                }
            } else {
                for (YvsComDocVentes d : l) {
                    switch (d.getTypeDoc()) {
                        case Constantes.TYPE_FV:
                            visibleFact = true;
                            if (d.getDocuments() != null ? !d.getDocuments().isEmpty() : false) {
                                visibleLivr = true;
                            }
                            if (d.getReglements() != null ? !d.getReglements().isEmpty() : false) {
                                visibleRegl = true;
                            }
                            break;
                        case Constantes.TYPE_BLV:
                            visibleLivr = true;
                            break;
                    }
                }
                if (selectDoc.getReglements() != null ? !selectDoc.getReglements().isEmpty() : false) {
                    visibleRegl = true;
                }
                openDialog("dlgConfirmAnnulerCmde");
                if (type.equals(Constantes.TYPE_BCV)) {
                    update("blog-annule_bcv");
                } else {
                    update("blog-annule_bcv_fv");
                }
            }
        }
    }

    public void annulerOrderForceAll() {
        try {
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComDocVentes y;
                for (Integer id : ids) {
                    if (id != -1 ? documents.size() > id : false) {
                        y = documents.get(id);
                        visibleCmde = true;
                        visibleFact = true;
                        visibleLivr = true;
                        visibleRegl = true;
                        annulerOrderForce(UtilCom.buildBeanDocVente(y), y, annuleCmdeAll, true, deleteFactAll, true, deleteLivrAll, false, deleteReglAll);
                    }
                }
            } else {
                getErrorMessage("Vous n'avez selectionné aucunne facture");
            }
        } catch (NumberFormatException ex) {
            getException("Commande... traitementLot", ex);
        }
        update("data_facture_vente");
    }

    public void annulerOrderForce() {
        annulerOrderForce(docVente, selectDoc, annuleCmde, annuleFact, deleteFact, annuleLivr, deleteLivr, annuleRegl, deleteRegl);
    }

    public void annulerOrderForce(DocVente docVente, YvsComDocVentes selectDoc, boolean annuleCmde, boolean annuleFact, boolean deleteFact, boolean annuleLivr, boolean deleteLivr, boolean annuleRegl, boolean deleteRegl) {
        String statutLotBC = annuleCmde ? Constantes.ETAT_EDITABLE : Constantes.ETAT_VALIDE;
        String statutLotFV = deleteFact ? "D" : (annuleFact ? Constantes.ETAT_EDITABLE : Constantes.ETAT_VALIDE);
        String statutLotBL = deleteLivr ? "D" : (annuleLivr ? Constantes.ETAT_EDITABLE : Constantes.ETAT_VALIDE);
        String statutLotPiece = deleteRegl ? "D" : (annuleRegl ? Constantes.ETAT_EDITABLE : Constantes.ETAT_REGLE);
        traitementLot(selectDoc, docVente, (!annuleCmde ? false : visibleCmde), statutLotBC, visibleFact, statutLotFV, visibleLivr, statutLotBL, visibleRegl, statutLotPiece, true);
    }

    public void annulerOrderForceOLD(DocVente docVente, YvsComDocVentes selectDoc, boolean annuleCmde, boolean annuleFact, boolean deleteFact, boolean annuleLivr, boolean deleteLivr, boolean annuleRegl, boolean deleteRegl) {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                if (annuleRegl) {// Annulation des reglements
                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (w != null) {
                        int lenght = selectDoc.getReglements().size();
                        YvsComptaCaissePieceVente p;
                        for (int i = 0; i < lenght; i++) {
                            p = selectDoc.getReglements().get(i);
                            if (p.getNotifs() != null ? p.getNotifs().getId() > 0 : false) {
                                dao.delete(p.getNotifs().getAcompte());
                            }
                            p.setNotifs(null);
                            if (deleteRegl) {// Suppression des reglements
                                dao.delete(p);
                                selectDoc.getReglements().remove(p);
                            } else {
                                w.setNeedConfirmation(true);
                                p.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                p = w.openConfirmPaiement(p, "F", false, true, false, false);
                                int idx = docVente.getReglements().indexOf(p);
                                if (idx > -1) {
                                    docVente.getReglements().set(idx, p);
                                }
                            }
                        }
                        docVente.setStatutRegle(Constantes.ETAT_ATTENTE);
                        selectDoc.setStatutRegle(Constantes.ETAT_ATTENTE);
                    }
                }
                if (annuleFact) {// Annulation des factures
                    ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (w != null) {
                        List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParentTypeDoc", new String[]{"documentLie", "typeDoc"}, new Object[]{selectDoc, Constantes.TYPE_FV});
                        for (YvsComDocVentes d : l) {
                            if (deleteFact || annuleLivr) {// Suppression des factures or Annulation des livraisons
                                for (YvsComDocVentes b : d.getDocuments()) {
                                    if (deleteFact || deleteLivr) {// Suppression des factures ou livraisons cas des livraisons rattachées aux factures
                                        dao.delete(b);
                                    } else {
                                        w.onAnnulerDistantLivraison(b);
                                    }
                                }
                                d.getDocuments().clear();
                            }
                            if (deleteFact) {// Suppression des factures
                                if (!deleteRegl) {// Non suppression des reglements mais suppressions des factures
                                    for (YvsComptaCaissePieceVente p : d.getReglements()) {
                                        p.setVente(selectDoc);
                                        dao.update(p);
                                        selectDoc.getReglements().add(p);
                                    }
                                }
                                dao.delete(d);
                                int idx = docVente.getDocuments().indexOf(d);
                                if (idx > -1) {
                                    docVente.getDocuments().remove(d);
                                }
                            } else {
                                if (w.annulerOrder(d, null, true)) {
                                    int idx = docVente.getDocuments().indexOf(d);
                                    if (idx > -1) {
                                        docVente.getDocuments().set(idx, d);
                                    }
                                }
                            }
                        }
                    }
                    docVente.setStatutLivre(Constantes.ETAT_ATTENTE);
                    selectDoc.setStatutLivre(Constantes.ETAT_ATTENTE);
                }
                if (annuleLivr) {// Annulation des livraisons
                    ManagedLivraisonVente w = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (w != null) {
                        List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParentTypeDoc", new String[]{"documentLie", "typeDoc"}, new Object[]{selectDoc, Constantes.TYPE_BLV});
                        for (YvsComDocVentes d : l) {
                            if (deleteLivr) {// Suppression des livraisons
                                dao.delete(d);
                                int idx = docVente.getDocuments().indexOf(d);
                                if (idx > -1) {
                                    docVente.getDocuments().remove(d);
                                }
                            } else {
                                if (w.annulerOrder(d, false, true, false, false)) {
                                    int idx = docVente.getDocuments().indexOf(d);
                                    if (idx > -1) {
                                        docVente.getDocuments().set(idx, d);
                                    }
                                }
                            }
                        }
                    }
                    docVente.setStatutLivre(Constantes.ETAT_ATTENTE);
                    selectDoc.setStatutLivre(Constantes.ETAT_ATTENTE);
                }
                if (annuleCmde) {// Annulation de la commande
                    annulerOrder(docVente, selectDoc, true);
                } else {
                    dao.update(selectDoc);
                }
                int idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                }
                if (type.equals(Constantes.TYPE_BCV)) {
                    update("grp_btn_etat_commande_vente");
                    update("tabview_commande_vente");
                    update("data_commande_vente");
                } else {
                    update("grp_btn_etat_facture_vente");
                    update("tabview_facture_vente");
                    update("data_facture_vente");
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedBonVente (annulerOrderForce) : ", ex);
        }
    }

    public void refuserOrder(YvsComDocVentes y) {
        selectDoc = y;
        type = Constantes.TYPE_BCV;
    }

    public void refuserOrder(YvsComDocVentes selectDoc, DocVente docVente, String type) {
        this.selectDoc = selectDoc;
        this.docVente = docVente;
        this.type = type;
    }

    public void refuserOrder() {
        refuserOrder(docVente, selectDoc, caisseAnnule, dateAnnule);
    }

    public void refuserOrder(DocVente docVente, YvsComDocVentes selectDoc, Caisses caisseAnnule, Date dateAnnule) {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (!autoriser("fv_cancel_doc_valid") && selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
                openNotAcces();
                return;
            }
            if (selectDoc.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
                getErrorMessage("Cette commande est déja livré");
                return;
            }
            if (selectDoc.getReglements() != null ? !selectDoc.getReglements().isEmpty() : false) {
                if (caisseAnnule != null ? caisseAnnule.getId() < 1 : true) {
                    getErrorMessage("Vous devez selectionner la caisse");
                    return;
                }
                YvsComptaCaissePieceVente y;
                List<YvsComptaCaissePieceVente> list = new ArrayList<>();
                for (int i = 0; i < selectDoc.getReglements().size(); i++) {
                    y = new YvsComptaCaissePieceVente(null, selectDoc.getReglements().get(i));
                    y.setMouvement(Constantes.MOUV_CAISS_SORTIE.charAt(0));
                    y.setCaissier(currentUser.getUsers());
                    y.setCaisse(new YvsBaseCaisse(caisseAnnule.getId(), caisseAnnule.getIntitule()));
                    y.setDatePaimentPrevu(dateAnnule);
                    y.setDatePiece(dateAnnule);
                    y.setDateValide(dateAnnule);
                    y.setAuthor(currentUser);
                    y.setVente(selectDoc);
                    y.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    String numero = genererReference(Constantes.TYPE_PC_VENTE_NAME, dateAnnule, caisseAnnule.getId());
                    if (numero != null ? numero.trim().length() < 1 : true) {
                        return;
                    }
                    y.setNumeroPiece(numero);

                    y.setId(null);
                    y = (YvsComptaCaissePieceVente) dao.save1(y);
                    list.add(y);
                }
                selectDoc.getReglements().addAll(list);
                if (type.equals(Constantes.TYPE_BCV)) {
                    update("tabview_commande_vente:data_mensualite_commande_vente");
                } else {
                    update("tabview_facture_vente:data_mensualite_facture_vente");
                }
            }
            if (changeStatut(Constantes.ETAT_ANNULE, docVente, selectDoc)) {
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(currentUser.getUsers());
                selectDoc.setValiderBy(null);
                selectDoc.setCloturerBy(null);
                selectDoc.setDateAnnuler(new Date());
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(null);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    selectDoc.setAuthor(currentUser);
                }
                dao.update(selectDoc);
            }
        }
    }

    public void refuserOrderForce() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                for (YvsComDocVentes d : l) {
                    dao.delete(d);
                }
                refuserOrder();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedBonVente (refuserOrderForce) : ", ex);
        }
    }

    public YvsComDocVentes validerOrder(boolean msg) {
        return validerOrder(docVente, selectDoc, msg);
    }

    public void validerOrderAll() {
        try {
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComDocVentes y;
                for (Integer id : ids) {
                    if (id != -1 ? documents.size() > id : false) {
                        y = documents.get(id);
                        validerOrder(UtilCom.buildBeanDocVente(y), y, false);
                    }
                }
            } else {
                getErrorMessage("Vous n'avez selectionné aucunne facture");
            }
        } catch (NumberFormatException ex) {
            getException("Commande... validerOrderAll", ex);
        }
        update("data_facture_vente");
    }

    public YvsComDocVentes validerOrder(DocVente docVente, YvsComDocVentes selectDoc, boolean msg) {
        YvsComDocVentes y = null;
        try {
            if (currentParamVente == null) {
                currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            boolean continu = false;
            if (selectDoc == null) {
                if (msg) {
                    getErrorMessage("Vous devez selectionner la facture");
                }
                return null;
            }
            if (docVente.getClient() != null ? docVente.getClient().getId() < 1 : true) {
                if (msg) {
                    getErrorMessage("Aucun client n'a été trouvé !");
                }
                return null;
            }
            //vérifier la tranche
            if (docVente.getTranche().getId() <= 0) {
                if (msg) {
                    getErrorMessage("Vous devez selectionner la tranche de livraison !");
                }
                return null;
            }
            if (currentParamVente != null ? !currentParamVente.getLivreBcvWithoutPaye() : false) {
                if (!selectDoc.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                    if (msg) {
                        getErrorMessage("La commande doit être reglée pour être livrée");
                    }
                    return null;
                }
            }
            if (selectDoc.getOnfacture().equals(Constantes.ETAT_VALIDE)) {
                if (msg) {
                    getWarningMessage("Cette commande a déja été facturée");
                }
                for (YvsComDocVentes d : selectDoc.getDocuments()) {
                    if (d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        y = d;
                        continu = true;
                        break;
                    }
                }
            }
            if (y != null ? y.getId() < 1 : true) {
                if (selectDoc.getOnfacture().equals(Constantes.ETAT_ENCOURS)) {
                    if (msg) {
                        getWarningMessage("Cette commande est déja rattachée à une facture");
                    }
                    for (YvsComDocVentes d : selectDoc.getDocuments()) {
                        y = d;
                        continu = true;
                        break;
                    }
                }
            }
            if (y != null ? y.getId() < 1 : true) {
                List<YvsComContenuDocVente> contenus = new ArrayList<>(docVente.getContenus());
                if (contenus != null ? contenus.isEmpty() : true) {
                    if (msg) {
                        getErrorMessage("Cette commande est vide");
                    }
                    return null;
                }
                if (selectDoc.getEnteteDoc() == null) {
                    if (msg) {
                        getErrorMessage("Cette commande n'est pas rattachée a un journal de vente");
                    }
                    return null;
                }
                String num = genererReference(Constantes.TYPE_FV_NAME, selectDoc.getEnteteDoc().getDateEntete(), entete.getPoint().getId(), Constantes.POINTVENTE);
                if (num != null ? num.trim().length() < 1 : true) {
                    return null;
                }
                List<YvsWorkflowEtapeValidation> etapes = null;

                y = new YvsComDocVentes(null, selectDoc);
                y.setEnteteDoc(selectDoc.getEnteteDoc());
                y.setDateSave(new Date());
                y.setAuthor(currentUser);
                y.setTypeDoc(Constantes.TYPE_FV);
                y.setNumDoc(num);
                if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
                    y.setDepotLivrer(new YvsBaseDepots(docVente.getDepot().getId()));
                }
                if (docVente.getTranche() != null ? docVente.getTranche().getId() > 0 : false) {
                    y.setTrancheLivrer(new YvsGrhTrancheHoraire(docVente.getTranche().getId()));
                }
                y.setLivreur(currentUser.getUsers());
                y.setDateLivraison(new Date());
                y.setDocumentLie(new YvsComDocVentes(selectDoc.getId()));
                y.setHeureDoc(new Date());
                y.setStatut(Constantes.ETAT_VALIDE);
                y.setStatutLivre(Constantes.ETAT_ATTENTE);
                y.setStatutRegle(selectDoc.getStatutRegle());
                y.setValiderBy(currentUser.getUsers());
                y.setDateValider(new Date());
                y.setDescription("Facturation de la commande N° " + selectDoc.getNumDoc() + " le " + ldf.format(new Date()) + " à " + time.format(y.getHeureDoc()));
                ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                if (w != null) {
                    etapes = w.getAllEtapeValidation();
                    y.setEtapeTotal(etapes != null ? etapes.size() : 0);
                }
                y.getContenus().clear();
                y.setId(null);
                y = (YvsComDocVentes) dao.save1(y);
                if (y != null ? y.getId() > 0 : false) {
                    YvsComContenuDocVente c;
                    int lenght = contenus.size();
                    for (int i = 0; i < lenght; i++) {
                        c = new YvsComContenuDocVente(null, contenus.get(i));
                        c.setDocVente(y);
                        c.setStatut(Constantes.ETAT_VALIDE);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        c = (YvsComContenuDocVente) dao.save1(c);
                        if (!y.getContenus().contains(c)) {
                            y.getContenus().add(c);
                        }
                    }
                    if (w != null ? etapes != null : false) {
                        w.saveEtapesValidation(y, etapes);
                    }
                }
            } else if (!y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                y.setAuthor(currentUser);
                y.setStatut(Constantes.ETAT_VALIDE);
                y.setValiderBy(currentUser.getUsers());
                y.setDateValider(new Date());
                dao.update(y);
                if (docVente != null) {
                    int index = docVente.getDocuments().indexOf(y);
                    if (index > -1) {
                        docVente.getDocuments().set(index, y);
                    }
                }
            }
            if (y != null ? y.getId() > 0 : false) {
                YvsComptaCaissePieceVente p;
                int lenght = docVente.getReglements().size();
                for (int i = 0; i < lenght; i++) {
                    p = docVente.getReglements().get(i);
                    if (p.getMouvement().equals(Constantes.MOUV_CAISS_SORTIE.charAt(0))) {
                        dao.delete(p);
                        docVente.getReglements().remove(p);
                    } else {
                        p.setValideBy(currentUser.getUsers());
                        p.setVente(y);
                        p.setAuthor(currentUser);
                        dao.update(p);
                        if (!y.getReglements().contains(p)) {
                            y.getReglements().add(p);
                        }
                    }
                }
                int idx = docVente.getDocuments().indexOf(y);
                if (idx < 0) {
                    docVente.getDocuments().add(0, y);
                }
                idx = selectDoc.getDocuments().indexOf(y);
                if (idx < 0) {
                    selectDoc.getDocuments().add(0, y);
                }
                continu = true;
                if (getType().equals(Constantes.TYPE_BCV)) {
                    update("tabview_commande_vente");
                } else {
                    update("tabview_facture_vente");
                }
            }
            if (continu ? changeStatut(Constantes.ETAT_VALIDE, docVente, selectDoc) : false) {
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(null);
                selectDoc.setValiderBy(currentUser.getUsers());
                selectDoc.setCloturerBy(null);
                selectDoc.setDateAnnuler(null);
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(new Date());
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    selectDoc.setAuthor(currentUser);
                }
                dao.update(selectDoc);
                int idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                }
            }
        } catch (Exception ex) {
            getException("ManagedBonVente (validerOrder) : ", ex);
        }
        return y;
    }

    public void transmisOrder() {
        transmisOrder(docVente, selectDoc, dateLivraison, Constantes.ETAT_VALIDE);
    }

    public void transmisOrderAll() {
        try {
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComDocVentes y;
                for (Integer id : ids) {
                    if (id != -1 ? documents.size() > id : false) {
                        y = documents.get(id);
                        transmisOrder(UtilCom.buildBeanDocVente(y), y, y.getDateLivraisonPrevu(), Constantes.ETAT_VALIDE);
                    }
                }
            } else {
                getErrorMessage("Vous n'avez selectionné aucunne facture");
            }
        } catch (NumberFormatException ex) {
            getException("Commande... transmisOrderAll", ex);
        }
        update("data_facture_vente");
    }

    public void transmisOrder(DocVente docVente, YvsComDocVentes selectDoc, Date dateLivraison, String statutLivre) {
        try {
            if (currentParamVente == null) {
                currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (!currentParamVente.getLivreBcvWithoutPaye() && !docVente.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                getErrorMessage("Cette commande doit etre reglée avant d'etre livrée");
                return;
            }
            YvsComDocVentes facture = validerOrder(docVente, selectDoc, true);
            if (facture != null ? facture.getId() > 0 : false) {
                List<YvsComContenuDocVente> contenus = new ArrayList<>(docVente.getContenus());
                if (contenus != null ? !contenus.isEmpty() : false) {
                    boolean continu = false;
                    String num = genererReference(Constantes.TYPE_BLV_NAME, dateLivraison, facture.getDepotLivrer().getId(), Constantes.DEPOT);
                    if (num != null ? num.trim().length() < 1 : true) {
                        return;
                    }
                    YvsComDocVentes y = new YvsComDocVentes(null, facture);
                    if (y.getClient() == null) {
                        y.setClient(selectDoc.getClient());
                    }
                    if (y.getCategorieComptable() == null) {
                        y.setCategorieComptable(selectDoc.getCategorieComptable());
                    }
                    y.setDateSave(new Date());
                    y.setAuthor(currentUser);
                    y.setTypeDoc(Constantes.TYPE_BLV);
                    y.setNumDoc(num);
                    y.setNumPiece("BLV N° " + facture.getNumDoc());
                    y.setLivreur(currentUser.getUsers());
                    y.setDateLivraison(dateLivraison);
                    y.setDocumentLie(new YvsComDocVentes(facture.getId()));
                    y.setHeureDoc(new Date());
                    y.setStatut(statutLivre);
                    y.setStatutLivre(Constantes.ETAT_LIVRE);
                    y.setStatutRegle(Constantes.ETAT_ATTENTE);
                    y.setAnnulerBy(null);
                    y.setCloturerBy(null);
                    y.setDateAnnuler(null);
                    y.setDateCloturer(null);
                    y.setValiderBy(currentUser.getUsers());
                    y.setDateValider(new Date());
                    y.setEtapeTotal(0);
                    y.setDescription("Livraison Facture N° " + facture.getNumDoc() + " le " + ldf.format(dateLivraison) + " à " + time.format(y.getHeureDoc()));
                    y.setId(null);
                    y.getContenus().clear();
                    y = (YvsComDocVentes) dao.save1(y);
                    if (y != null ? y.getId() > 0 : false) {
                        YvsComContenuDocVente c;
                        int lenght = contenus.size();
                        for (int i = 0; i < lenght; i++) {
                            c = new YvsComContenuDocVente(null, contenus.get(i));
                            c.setDocVente(y);
                            c.setStatut(Constantes.ETAT_VALIDE);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            c = (YvsComContenuDocVente) dao.save1(c);
                            if (!y.getContenus().contains(c)) {
                                y.getContenus().add(c);
                            }
                        }
                        if (facture.getReglements() != null ? facture.getReglements().isEmpty() : true) {
                            YvsComptaCaissePieceVente p;
                            lenght = docVente.getReglements().size();
                            for (int i = 0; i < lenght; i++) {
                                p = docVente.getReglements().get(i);
                                if (p.getMouvement().equals(Constantes.MOUV_CAISS_SORTIE.charAt(0))) {
                                    dao.delete(p);
                                    docVente.getReglements().remove(p);
                                } else {
                                    p.setValideBy(currentUser.getUsers());
                                    p.setVente(facture);
                                    p.setAuthor(currentUser);
                                    dao.update(p);
                                    if (!facture.getReglements().contains(p)) {
                                        facture.getReglements().add(p);
                                    }
                                }
                            }
                            if (getType().equals(Constantes.TYPE_BCV)) {
                                update("tabview_commande_vente:data_mensualite_commande_vente");
                            } else {
                                update("tabview_facture_vente:data_mensualite_facture_vente");
                            }
                        }
                        continu = true;
                    }
                    if (continu) {
                        facture.setStatutLivre(Constantes.ETAT_LIVRE);
                        if (currentUser != null ? currentUser.getId() > 0 : false) {
                            facture.setAuthor(currentUser);
                        }
                        dao.update(facture);

                        docVente.setStatutLivre(Constantes.ETAT_LIVRE);
                        selectDoc.setStatutLivre(Constantes.ETAT_LIVRE);
                        dao.update(selectDoc);
                        int idx = documents.indexOf(selectDoc);
                        if (idx > -1) {
                            documents.set(idx, selectDoc);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getException("ERROR : ", ex);
        }
    }

    public void cloturer(YvsComDocVentes y) {
        selectDoc = y;
        update("id_confirm_close_bcv");
    }

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }
        selectDoc.setCloturer(!selectDoc.getCloturer());
        selectDoc.setDateCloturer(selectDoc.getCloturer() ? new Date() : null);
        selectDoc.setCloturerBy(selectDoc.getCloturer() ? currentUser.getUsers() : null);
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        if (documents.contains(selectDoc)) {
            int idx = documents.indexOf(selectDoc);
            if (idx > -1) {
                documents.set(idx, selectDoc);
            }
            update("data_commande_vente");
        }
    }

    public boolean changeStatut(String etat) {
        if (changeStatutWithOutSucces(etat)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatutWithOutSucces(String etat) {
        return changeStatutWithOutSucces(etat, docVente, selectDoc);
    }

    public boolean changeStatut(String etat, YvsComDocVentes entity) {
        if (changeStatutWithOutSucces(etat, entity)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatutWithOutSucces(String etat, YvsComDocVentes entity) {
        return changeStatutWithOutSucces(etat, UtilCom.buildBeanDocVente(entity), entity);
    }

    public boolean changeStatut(String etat, DocVente bean, YvsComDocVentes entity) {
        if (changeStatutWithOutSucces(etat, bean, entity)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatutWithOutSucces(String etat, DocVente bean, YvsComDocVentes entity) {
        if (!etat.equals("")) {
            if (bean.isCloturer()) {
                getErrorMessage("Ce document est vérouillé");
                return false;
            }
            String rq = "UPDATE yvs_com_doc_ventes SET statut = '" + etat + "'  WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            bean.setStatut(etat);
            entity.setStatut(etat);
            if (documents.contains(entity)) {
                documents.set(documents.indexOf(entity), entity);
            }
            Map<String, String> statuts = dao.getEquilibreVente(bean.getId());
            if (statuts != null) {
                bean.setStatutLivre(statuts.get("statut_livre"));
                bean.setStatutRegle(statuts.get("statut_regle"));
            }
            if (type.equals(Constantes.TYPE_BCV)) {
                update("data_commande_vente");
                update("infos_document_commande_vente");
                update("grp_btn_etat_commande_vente");
            } else {
                ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                if (w != null ? w.getDocuments().contains(entity) : false) {
                    w.getDocuments().set(w.getDocuments().indexOf(entity), entity);
                }
                update("data_facture_vente");
                update("infos_document_facture_vente");
                update("form_entete_facture_vente");
            }
            return true;
        }
        return false;
    }

    public void searchByNum() {
        ParametreRequete p;
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.numDoc", "numDoc", "%" + numSearch_ + "%");
            p.setOperation(" LIKE ");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.numDoc", "numDoc", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void removeDoublon(YvsComDocVentes y) {
        selectDoc = y;
        removeDoublon();
    }

    public void removeDoublon() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            if (!selectDoc.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre editable pour pouvoir etre modifié");
                return;
            }
            removeDoublonVente(selectDoc.getId());
            succes();
        }
    }

    @Override
    public void cleanEnteteVente() {
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.cleanEnteteVente();
        }
    }

    @Override
    public void cleanVente() {
//        if (!autoriser("fv_clean")) {
//            openNotAcces();
//            return;
//        }
        super.cleanVente();
        loadAllBons(true, true);
    }

    public void equilibre(YvsComDocVentes y) {
        selectDoc = y;
        equilibre();
        succes();
    }

    public void equilibre() {
        equilibre(selectDoc, true);
    }

    public void equilibre(YvsComDocVentes selectDoc, boolean load) {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            YvsComDocVentes facture = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findByParentTypeDoc", new String[]{"documentLie", "typeDoc"}, new Object[]{selectDoc, Constantes.TYPE_FV});
            if (facture != null ? facture.getId() < 1 : true) {
                facture = selectDoc;
            }
            if (facture != null ? facture.getId() > 0 : false) {
                Map<String, String> statuts = dao.getEquilibreVente(facture.getId());
                System.err.println("statuts : " + statuts);
                if (statuts != null) {
                    facture.setStatutLivre(statuts.get("statut_livre"));
                    facture.setStatutRegle(statuts.get("statut_regle"));
                }
                selectDoc.setStatutLivre(facture.getStatutLivre());
                selectDoc.setStatutRegle(facture.getStatutRegle());
                selectDoc.setAuthor(currentUser);
                selectDoc.setDateUpdate(new Date());
                dao.update(selectDoc);
                if (docVente != null ? selectDoc.getId().equals(docVente.getId()) : false) {
                    docVente.setStatutLivre(facture.getStatutLivre());
                    docVente.setStatutRegle(facture.getStatutRegle());
                }
                if (documents != null ? !documents.isEmpty() : false) {
                    int idx = documents.indexOf(selectDoc);
                    if (idx > -1) {
                        documents.set(idx, selectDoc);
                    }
                }
            }
        }
    }

    public void onValideDistantLivraison(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_FV: {
                    ManagedFactureVenteV2 s = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (s != null) {
                        if (s.validerOrder(y)) {
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docVente.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_commande_vente");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        if (s.validerOrder(y, false, false, true, null, false)) {
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docVente.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_commande_vente");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_FAV:
                case Constantes.TYPE_BRV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        if (s.validerOrder(y)) {
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            update("grp_btn_etat_commande_vente");
                        }
                    }
                    break;
                }
            }
        }
    }

    public void onAnnulerDistantLivraison(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_FV: {
                    ManagedFactureVenteV2 s = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (s != null) {
                        if (s.annulerOrder(y, UtilCom.buildSimpleBeanDocVente(y), true)) {
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docVente.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_commande_vente");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        if (s.annulerOrder(y, false, true, false, false)) {
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docVente.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_commande_vente");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_BRV:
                case Constantes.TYPE_FAV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        if (s.annulerOrder(y, true, false, false)) {
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
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

    public void onRefuserDistantLivraison(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_FV: {
                    ManagedFactureVenteV2 s = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (s != null) {
                        if (s.refuserOrder(y, UtilCom.buildSimpleBeanDocVente(y), true, true)) {
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docVente.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_commande_vente");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        if (s.refuserOrder(y, false, true, false, false)) {
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docVente.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_commande_vente");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_BRV:
                case Constantes.TYPE_FAV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        if (s.refuserOrder(y, true, false, false)) {
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
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

    public void onSelectDistantLivraison(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_FV: {
                    ManagedFactureVenteV2 s = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (s != null) {
                        s.onSelectObject(y);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Factures Vente", "modGescom", "smenFactureVente", true);
                        }
                    }
                    break;
                }
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        s.onSelectObject(y);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Livraisons Vente", "modGescom", "smenBonLivraisonVente", true);
                        }
                    }
                    break;
                }
                case Constantes.TYPE_BRV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        s.onSelectObject(y, Constantes.TYPE_BRV);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Bon Retour Vente", "modGescom", "smenFactureRetourVente", true);
                        }
                    }
                    break;
                }
                case Constantes.TYPE_FAV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        s.onSelectObject(y, Constantes.TYPE_FAV);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Factures Avoir Vente", "modCompta", "smenFactureAvoirVente", true);
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public double setMontantTotalDoc(DocVente doc, List<YvsComContenuDocVente> lc, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        YvsComDocVentes entity = new YvsComDocVentes(doc.getId());
        setMontantTotalDoc(entity, lc, societe, dateDebut, dateFin, dao);

        doc.setMontantRemise(entity.getMontantRemise());
        doc.setMontantTaxe(entity.getMontantTaxe());
        doc.setMontantRistourne(entity.getMontantRistourne());
        doc.setMontantCommission(entity.getMontantCommission());
        doc.setMontantHT(entity.getMontantHT());
        doc.setMontantTTC(entity.getMontantTTC());
        doc.setMontantRemises(entity.getMontantRemises());
        doc.setMontantCS(entity.getMontantCS());
        doc.setMontantAvance(entity.getMontantAvance());
        doc.setMontantAvoir(entity.getMontantAvoir());
        doc.setMontantAvanceAvoir(entity.getMontantAvanceAvoir());
        doc.setMontantTaxeR(entity.getMontantTaxeR());
        doc.setMontantResteApayer(entity.getMontantResteApayer());

        return doc.getMontantTotal();
    }

    @Override
    public double setMontantTotalDoc(YvsComDocVentes doc, List<YvsComContenuDocVente> lc, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        YvsComDocVentes facture = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findByParentTypeDoc", new String[]{"documentLie", "typeDoc"}, new Object[]{doc, Constantes.TYPE_FV});
        if (facture != null ? facture.getId() > 0 : false) {
            super.setMontantTotalDoc(facture, lc, societe, dateDebut, dateFin, dao);

            doc.setMontantRemise(facture.getMontantRemise());
            doc.setMontantTaxe(facture.getMontantTaxe());
            doc.setMontantRistourne(facture.getMontantRistourne());
            doc.setMontantCommission(facture.getMontantCommission());
            doc.setMontantHT(facture.getMontantHT());
            doc.setMontantTTC(facture.getMontantTTC());
            doc.setMontantRemises(facture.getMontantRemises());
            doc.setMontantCS(facture.getMontantCS());
            doc.setMontantAvance(facture.getMontantAvance());
            doc.setMontantAvoir(facture.getMontantAvoir());
            doc.setMontantAvanceAvoir(facture.getMontantAvanceAvoir());
            doc.setMontantTaxeR(facture.getMontantTaxeR());
            doc.setMontantResteApayer(facture.getMontantResteApayer());

            return doc.getMontantTotal();
        } else {
            return super.setMontantTotalDoc(doc, lc, societe, dateDebut, dateFin, dao);
        }
    }

    public void traitementLot() {
        try {
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComDocVentes y;
                if (statutLotBC.equals(Constantes.ETAT_EDITABLE)) {
                    if (statutLotFV.equals(Constantes.ETAT_VALIDE)) {
                        statutLotFV = Constantes.ETAT_EDITABLE;
                    }
                    if (statutLotFV.equals(Constantes.ETAT_VALIDE)) {
                        statutLotBL = Constantes.ETAT_EDITABLE;
                    }
                    lotFV = true;
                    lotBL = true;
                }
                for (Integer id : ids) {
                    if (id != -1 ? documents.size() > id : false) {
                        y = documents.get(id);
                        traitementLot(y, lotBC, statutLotBC, lotFV, statutLotFV, lotBL, statutLotBL, lotPiece, statutLotPiece, false);
                    }
                }
            } else {
                getErrorMessage("Vous n'avez selectionné aucunne facture");
            }
        } catch (NumberFormatException ex) {
            getException("Commande... traitementLot", ex);
        }
        update("data_facture_vente");
    }

    public void traitementLot(YvsComDocVentes selectDoc, boolean lotBC, String statutLotBC, boolean lotFV, String statutLotFV, boolean lotBL, String statutLotBL, boolean lotPiece, String statutLotPiece, boolean msg) {
        traitementLot(selectDoc, UtilCom.buildBeanDocVente(selectDoc), lotBC, statutLotBC, lotFV, statutLotFV, lotBL, statutLotBL, lotPiece, statutLotPiece, msg);
    }

    public void traitementLot(YvsComDocVentes selectDoc, DocVente docVente, boolean lotBC, String statutLotBC, boolean lotFV, String statutLotFV, boolean lotBL, String statutLotBL, boolean lotPiece, String statutLotPiece, boolean msg) {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                List<YvsComptaCaissePieceVente> reglements = dao.loadNameQueries("YvsComptaCaissePieceVente.findByVentes", new String[]{"vente1", "vente2"}, new Object[]{selectDoc, selectDoc});
                List<YvsComDocVentes> livraisons = null, factures = null;
                if (lotPiece) {
                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (w != null) {
                        for (YvsComptaCaissePieceVente p : reglements) {
                            if (!w.controlToPaiement(p, "F", false, msg, false, false)) {
                                return;
                            }
                        }
                    }
                }
                if (lotBL) {
                    ManagedLivraisonVente w = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (w != null) {
                        livraisons = dao.loadNameQueries("YvsComDocVentes.findByParentsTypeDoc", new String[]{"documentLie1", "documentLie2", "typeDoc"}, new Object[]{selectDoc, selectDoc, Constantes.TYPE_BLV});
                        for (YvsComDocVentes d : livraisons) {
                            if ((statutLotBL.equals(Constantes.ETAT_EDITABLE) || statutLotBL.equals("D"))) {//Annulation de la livraison
                                if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                                    if (!w.controlToAnnulerOrder(d, false, msg, false, false)) {
                                        closeDialog("dlgConfirmChangeInventaireByCancel");
                                        w.setExistInventaire(null);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
                if (lotFV && !lotBL) {
                    ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (w != null) {
                        factures = dao.loadNameQueries("YvsComDocVentes.findByParentTypeDoc", new String[]{"documentLie", "typeDoc"}, new Object[]{selectDoc, Constantes.TYPE_FV});
                        for (YvsComDocVentes d : factures) {
                            if ((statutLotFV.equals(Constantes.ETAT_EDITABLE) || statutLotFV.equals("D"))) {//Annulation de la facture
                                if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                                    if (!w.controlToAnnulerOrder(d, null, true)) {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
                if (lotPiece) {//Traitement des reglements
                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (w != null && reglements != null) {
                        for (YvsComptaCaissePieceVente p : reglements) {
                            if (statutLotPiece.equals(Constantes.ETAT_REGLE)) {//Validation du reglement
                                if (!p.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                    w.setNeedConfirmation(true);
                                    p = w.openConfirmPaiement(p, "F", false, msg, false, false);
                                    if (p != null ? !p.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) : true) {
                                        return;
                                    }
                                    int idx = selectDoc.getReglements().indexOf(p);
                                    if (idx > -1) {
                                        selectDoc.getReglements().set(idx, p);
                                    }
                                    if (docVente != null) {
                                        idx = docVente.getReglements().indexOf(p);
                                        if (idx > -1) {
                                            docVente.getReglements().set(idx, p);
                                        }
                                    }
                                }
                            }
                            if ((statutLotPiece.equals(Constantes.ETAT_EDITABLE) || statutLotPiece.equals("D"))) {//Annulation du reglement
                                if (!p.getStatutPiece().equals(Constantes.STATUT_DOC_ATTENTE)) {
                                    if (p.getNotifs() != null ? p.getNotifs().getId() > 0 : false) {
                                        dao.delete(p.getNotifs().getAcompte());
                                    }
                                    p.setNotifs(null);
                                    w.setNeedConfirmation(true);
                                    p.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                    p = w.openConfirmPaiement(p, "F", false, msg, false, false);
                                    if (p != null ? !p.getStatutPiece().equals(Constantes.STATUT_DOC_ATTENTE) : true) {
                                        return;
                                    }
                                    int idx = selectDoc.getReglements().indexOf(p);
                                    if (idx > -1) {
                                        selectDoc.getReglements().set(idx, p);
                                    }
                                    if (docVente != null) {
                                        idx = docVente.getReglements().indexOf(p);
                                        if (idx > -1) {
                                            docVente.getReglements().set(idx, p);
                                        }
                                    }
                                }
                            }
                            if (statutLotPiece.equals("D")) {//Suppression du reglement
                                if (p.getNotifs() != null ? p.getNotifs().getId() > 0 : false) {
                                    dao.delete(p.getNotifs().getAcompte());
                                }
                                p.setNotifs(null);
                                dao.delete(p);
                                selectDoc.getReglements().remove(p);
                                if (docVente != null) {
                                    docVente.getReglements().remove(p);
                                }
                            }
                        }
                        if (statutLotPiece.equals("D")) {
                            reglements.clear();
                        }
                        if (docVente != null) {
                            docVente.setStatutRegle(statutLotPiece.equals(Constantes.ETAT_REGLE) ? Constantes.ETAT_REGLE : Constantes.ETAT_ATTENTE);
                        }
                        selectDoc.setStatutRegle(statutLotPiece.equals(Constantes.ETAT_REGLE) ? Constantes.ETAT_REGLE : Constantes.ETAT_ATTENTE);
                    }
                }

                if (lotBL) {//Traitement des livraisons
                    ManagedLivraisonVente w = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (w != null && livraisons != null) {
                        for (YvsComDocVentes d : livraisons) {
                            if (statutLotBL.equals(Constantes.ETAT_VALIDE)) {//Validation de la livraison
                                if (!d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                                    if (!w.validerOrder(d, true, false, msg, null, false)) {
                                        closeDialog("dlgConfirmChangeInventaireByValid");
                                        w.setExistInventaire(null);
                                        return;
                                    }
                                    int idx = selectDoc.getDocuments().indexOf(d);
                                    if (idx > -1) {
                                        selectDoc.getDocuments().set(idx, d);
                                    }
                                    if (docVente != null) {
                                        idx = docVente.getDocuments().indexOf(d);
                                        if (idx > -1) {
                                            docVente.getDocuments().set(idx, d);
                                        }
                                    }
                                }
                            }
                            if ((statutLotBL.equals(Constantes.ETAT_EDITABLE) || statutLotBL.equals("D"))) {//Annulation de la livraison
                                if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                                    if (!w.annulerOrder(d, false, msg, false, false)) {
                                        closeDialog("dlgConfirmChangeInventaireByCancel");
                                        return;
                                    }
                                    int idx = selectDoc.getDocuments().indexOf(d);
                                    if (idx > -1) {
                                        selectDoc.getDocuments().set(idx, d);
                                    }
                                    if (docVente != null) {
                                        idx = docVente.getDocuments().indexOf(d);
                                        if (idx > -1) {
                                            docVente.getDocuments().set(idx, d);
                                        }
                                    }
                                    if (docVente != null) {
                                        idx = docVente.getDocuments().indexOf(d);
                                        if (idx > -1) {
                                            docVente.getDocuments().set(idx, d);
                                        }
                                    }
                                }
                            }
                            if (statutLotBL.equals("D")) {//Suppression de la livraison
                                dao.delete(d);
                                selectDoc.getDocuments().remove(d);
                                if (docVente != null) {
                                    docVente.getDocuments().remove(d);
                                }
                            }
                        }
                        if (docVente != null) {
                            docVente.setStatutLivre(statutLotBL.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                        }
                        selectDoc.setStatutLivre(statutLotBL.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                    }
                }
                if (lotFV) {//TRaitement des factures
                    ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (w != null) {
                        if ((statutLotFV.equals(Constantes.ETAT_EDITABLE) || statutLotFV.equals("D"))) {//Annulation ou suppression de la facture
                            for (YvsComptaCaissePieceVente r : reglements) { // On deplace les reglements de la facture pour le bon
                                r.setVente(selectDoc);
                                dao.update(r);
                            }
                        }
                        factures = dao.loadNameQueries("YvsComDocVentes.findByParentTypeDoc", new String[]{"documentLie", "typeDoc"}, new Object[]{selectDoc, Constantes.TYPE_FV});
                        for (YvsComDocVentes d : factures) {
                            if (statutLotFV.equals(Constantes.ETAT_VALIDE)) {//Validation de la facture
                                if (!d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                                    if (!w.validerOrder(UtilCom.buildBeanDocVente(d), d, msg, msg)) {
                                        return;
                                    }
                                    int idx = selectDoc.getDocuments().indexOf(d);
                                    if (idx > -1) {
                                        selectDoc.getDocuments().set(idx, d);
                                    }
                                    if (docVente != null) {
                                        idx = docVente.getDocuments().indexOf(d);
                                        if (idx > -1) {
                                            docVente.getDocuments().set(idx, d);
                                        }
                                    }
                                }
                            }
                            if ((statutLotFV.equals(Constantes.ETAT_EDITABLE) || statutLotFV.equals("D"))) {//Annulation de la facture
                                if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                                    if (!w.annulerOrder(d, null, true)) {
                                        return;
                                    }
                                    int idx = selectDoc.getDocuments().indexOf(d);
                                    if (idx > -1) {
                                        selectDoc.getDocuments().set(idx, d);
                                    }
                                    if (docVente != null) {
                                        idx = docVente.getDocuments().indexOf(d);
                                        if (idx > -1) {
                                            docVente.getDocuments().set(idx, d);
                                        }
                                    }
                                }
                            }
                            if (statutLotFV.equals("D")) {//Suppression de la facture
                                dao.delete(d);
                                selectDoc.getDocuments().remove(d);
                                if (docVente != null) {
                                    docVente.getDocuments().remove(d);
                                }
                            }
                        }
                        if (docVente != null) {
                            docVente.setStatutLivre(statutLotFV.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                        }
                        selectDoc.setStatutLivre(statutLotFV.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                    }
                }
                if (lotBC) {//Traitement du bon de commande
                    if (statutLotBC.equals(Constantes.ETAT_EDITABLE)) {
                        if (!selectDoc.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                            annulerOrder(docVente, selectDoc, true);
                        }
                    }
                    if (statutLotBC.equals(Constantes.ETAT_VALIDE)) {
                        if (!selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            validerOrder(docVente, selectDoc, msg);
                        }
                    }
                    if (statutLotBC.equals(Constantes.ETAT_LIVRE)) {
                        if (!selectDoc.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
                            transmisOrder(docVente, selectDoc, selectDoc.getDateLivraisonPrevu(), Constantes.ETAT_VALIDE);
                        }
                    }
                } else {
                    dao.update(selectDoc);
                }
                if (lotFV && (statutLotFV.equals(Constantes.ETAT_EDITABLE) || statutLotFV.equals("D"))) {
                    selectDoc.setReglements(reglements);
                    if (docVente != null) {
                        docVente.setReglements(reglements);
                    }
                }
                int idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                }
                if (type.equals(Constantes.TYPE_BCV)) {
                    update("grp_btn_etat_commande_vente");
                    update("tabview_commande_vente");
                    update("data_commande_vente");
                } else {
                    update("grp_btn_etat_facture_vente");
                    update("tabview_facture_vente");
                    update("data_facture_vente");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedBonVente (traitementLot) : ", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void maintenance() {
        try {
            if (!date) {
                getErrorMessage("Vous devez preciser une periode");
                return;
            }
            String query = "SELECT y.id FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id "
                    + "LEFT JOIN yvs_com_doc_ventes d ON (d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie = y.id) LEFT JOIN yvs_compta_caisse_piece_vente p ON (p.statut_piece = 'P' AND p.vente = y.id) "
                    + "INNER JOIN yvs_users u ON c.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id "
                    + "WHERE a.id = ? AND e.date_entete BETWEEN ? AND ? AND y.type_doc = 'BCV' AND y.statut = 'V' "
                    + "GROUP BY y.id HAVING COUNT(d.id) > 0 AND COUNT(p.id) > 0";
            List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)});
            if (ids != null ? !ids.isEmpty() : false) {
                YvsComDocVentes y;
                for (Long id : ids) {
                    y = new YvsComDocVentes(id);
                    if (y != null ? y.getId() > 0 : false) {
                        maintenance(y, false);
                    }
                }
                succes();
            }
            update("data_commande_vente");
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedBonVente (maintenance) : ", ex);
        }
    }

    public void maintenance(YvsComDocVentes y, boolean message) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                String query = "SELECT d.id FROM yvs_com_doc_ventes d WHERE d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie = ? LIMIT 1";
                Long facture = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options(y.getId(), 1)});
                if (facture != null ? facture > 0 : false) {
                    List<YvsComptaCaissePieceVente> reglements = dao.loadNameQueries("YvsComptaCaissePieceVente.findByMensualite", new String[]{"vente"}, new Object[]{y});
                    for (YvsComptaCaissePieceVente p : reglements) {
                        p.setVente(new YvsComDocVentes(facture));
                        dao.update(p);
                    }
                    if (message) {
                        succes();
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedBonVente (maintenance) : ", ex);
        }
    }

}
