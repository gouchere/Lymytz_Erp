///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.stocks;
//
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
//import javax.faces.event.ValueChangeEvent;
//import org.primefaces.component.api.UIColumn;
//import org.primefaces.event.CellEditEvent;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.UnselectEvent;
//import yvs.commerce.ContenuDoc;
//import yvs.commerce.achats.BaseTaxe;
//import yvs.entity.base.YvsBaseCategorieComptable;
//import yvs.entity.base.YvsBaseDepots;
//import yvs.entity.commercial.stock.YvsContenuDocStock;
//import yvs.entity.commercial.stock.YvsCoutSupDocStock;
//import yvs.entity.commercial.stock.YvsDocStocks;
//import yvs.entity.param.YvsCrenauxHoraire;
//import yvs.entity.produits.YvsArticles;
//import yvs.parametrage.catCompta.CategorieComptable;
//import yvs.parametrage.doc.ConfigDoc;
//import yvs.parametrage.doc.FactoryDoc;
//import yvs.parametrage.entrepot.Depots;
//import yvs.parametrage.th.CrenauxHoraire;
//import yvs.produits.Articles;
//import yvs.util.Constantes;
//import yvs.util.Managed;
//import yvs.util.enume.TypeDoc;
//
///**
// *
// * @author GOUCHERE YVES
// */
//@ManagedBean(name = "Mdstock")
//@SessionScoped
//public class ManagedDocStock extends Managed<DocStock, YvsBaseCaisse> implements Serializable {
//
//    @ManagedProperty(value = "#{dstock}")
//    private DocStock dStock;
//    private String numDocToUpdate;
//    private boolean displayCrenoS, displayCrenoD, displayDepoD;
//    private boolean disabledCrenoS, disabledCrenoD, disabledDepoD, disabledDate, disabledDepoS, disabledTypeDoc;
//    private boolean displayDes, restricToDep = true;
//    private boolean updateDocStock, docOk, displayPrix;
//    private YvsBaseDepots source;
//    private YvsBaseDepots destination;
//    private YvsBaseCategorieComptable catCompta;
//    private YvsArticles produit;
//    private YvsCrenauxHoraire crenoSource;
//    private YvsCrenauxHoraire crenoDest;
//    private List<Depots> listDepot;
//    private List<Depots> listFilterDepot;
//    private List<Articles> listArt;
//    private List<Articles> listFilterArt;
//    private List<CrenauxHoraire> listCrenaux;
//    private List<CrenauxHoraire> listCrenauxF;
//    private List<CategorieComptable> listCatC;
//    private List<CategorieComptable> listCatCFilter;
//    private List<DocStock> listDocStock;
//    private List<DocStock> listDocStockF;
//    private List<ContenuDoc> listContentDS;
//    private ContenuDoc selectedContent;
//    @ManagedProperty(value = "#{catC}")
//    private CategorieComptable catC;
//    @ManagedProperty(value = "#{depot}")
//    private Depots dep;
//    @ManagedProperty(value = "#{th}")
//    private CrenauxHoraire creno;
//    private List<BaseTaxe> listBaseTaxe;
//    private String sourceClick = "";
//    private List<String> ltypeArt;
//    private List<YvsDocStocks> yvsDocStocks;
//    private List<YvsCoutSupDocStock> yvsCoutSupp;
//
//    /*
//     *
//     */
//    public ManagedDocStock() {
//        listCatC = new ArrayList<>();
//        listDepot = new ArrayList<>();
//        listCrenaux = new ArrayList<>();
//        listArt = new ArrayList<>();
//        listDocStock = new ArrayList<>();
//        listContentDS = new ArrayList<>();
//        listBaseTaxe = new ArrayList<>();
//        ltypeArt = new ArrayList<>();
//        ltypeArt.add(Constantes.CAT_MP);
//        ltypeArt.add(Constantes.CAT_MARCHANDISE);
//        ltypeArt.add(Constantes.CAT_SERVICE);
//        ltypeArt.add(Constantes.CAT_PF);
//        ltypeArt.add(Constantes.CAT_PSF);
//        yvsCoutSupp = new ArrayList<>();
//    }
//
//    public List<BaseTaxe> getListBaseTaxe() {
//        return listBaseTaxe;
//    }
//
//    public void setListBaseTaxe(List<BaseTaxe> listBaseTaxe) {
//        this.listBaseTaxe = listBaseTaxe;
//    }
//
//    public ContenuDoc getSelectedContent() {
//        return selectedContent;
//    }
//
//    public void setSelectedContent(ContenuDoc selectedContent) {
//        this.selectedContent = selectedContent;
//    }
//
//    public List<ContenuDoc> getListContentDS() {
//        return listContentDS;
//    }
//
//    public void setListContentDS(List<ContenuDoc> listContentDS) {
//        this.listContentDS = listContentDS;
//    }
//
//    public DocStock getdStock() {
//        return dStock;
//    }
//
//    public void setdStock(DocStock dStock) {
//        this.dStock = dStock;
//    }
//
//    public boolean isDisabledCrenoD() {
//        return disabledCrenoD;
//    }
//
//    public void setDisabledCrenoD(boolean disabledCrenoD) {
//        this.disabledCrenoD = disabledCrenoD;
//    }
//
//    public boolean isRestricToDep() {
//        return restricToDep;
//    }
//
//    public void setRestricToDep(boolean restricToDep) {
//        this.restricToDep = restricToDep;
//    }
//
//    public boolean isDisabledCrenoS() {
//        return disabledCrenoS;
//    }
//
//    public void setDisabledCrenoS(boolean disabledCrenoS) {
//        this.disabledCrenoS = disabledCrenoS;
//    }
//
//    public boolean isDisabledDate() {
//        return disabledDate;
//    }
//
//    public void setDisabledDate(boolean disabledDate) {
//        this.disabledDate = disabledDate;
//    }
//
//    public boolean isDisabledDepoD() {
//        return disabledDepoD;
//    }
//
//    public void setDisabledDepoD(boolean disabledDepoD) {
//        this.disabledDepoD = disabledDepoD;
//    }
//
//    public boolean isDisabledDepoS() {
//        return disabledDepoS;
//    }
//
//    public void setDisabledDepoS(boolean disabledDepoS) {
//        this.disabledDepoS = disabledDepoS;
//    }
//
//    public boolean isDisabledTypeDoc() {
//        return disabledTypeDoc;
//    }
//
//    public void setDisabledTypeDoc(boolean disabledTypeDoc) {
//        this.disabledTypeDoc = disabledTypeDoc;
//    }
//
//    public boolean isDisplayCrenoD() {
//        return displayCrenoD;
//    }
//
//    public void setDisplayCrenoD(boolean displayCrenoD) {
//        this.displayCrenoD = displayCrenoD;
//    }
//
//    public boolean isDisplayCrenoS() {
//        return displayCrenoS;
//    }
//
//    public void setDisplayCrenoS(boolean displayCrenoS) {
//        this.displayCrenoS = displayCrenoS;
//    }
//
//    public boolean isDisplayDepoD() {
//        return displayDepoD;
//    }
//
//    public void setDisplayDepoD(boolean displayDepoD) {
//        this.displayDepoD = displayDepoD;
//    }
//
//    public List<CategorieComptable> getListCatC() {
//        return listCatC;
//    }
//
//    public void setListCatC(List<CategorieComptable> listCatC) {
//        this.listCatC = listCatC;
//    }
//
//    public List<CategorieComptable> getListCatCFilter() {
//        return listCatCFilter;
//    }
//
//    public void setListCatCFilter(List<CategorieComptable> listCatCFilter) {
//        this.listCatCFilter = listCatCFilter;
//    }
//
//    public List<CrenauxHoraire> getListCrenaux() {
//        return listCrenaux;
//    }
//
//    public void setListCrenaux(List<CrenauxHoraire> listCrenaux) {
//        this.listCrenaux = listCrenaux;
//    }
//
//    public List<CrenauxHoraire> getListCrenauxF() {
//        return listCrenauxF;
//    }
//
//    public void setListCrenauxF(List<CrenauxHoraire> listCrenauxF) {
//        this.listCrenauxF = listCrenauxF;
//    }
//
//    public List<Depots> getListDepot() {
//        return listDepot;
//    }
//
//    public void setListDepot(List<Depots> listDepot) {
//        this.listDepot = listDepot;
//    }
//
//    public List<Depots> getListFilterDepot() {
//        return listFilterDepot;
//    }
//
//    public void setListFilterDepot(List<Depots> listFilterDepot) {
//        this.listFilterDepot = listFilterDepot;
//    }
//
//    public CategorieComptable getCatC() {
//        return catC;
//    }
//
//    public void setCatC(CategorieComptable catC) {
//        this.catC = catC;
//    }
//
//    public CrenauxHoraire getCreno() {
//        return creno;
//    }
//
//    public void setCreno(CrenauxHoraire creno) {
//        this.creno = creno;
//    }
//
//    public Depots getDep() {
//        return dep;
//    }
//
//    public void setDep(Depots dep) {
//        this.dep = dep;
//    }
//
//    public String getNumDocToUpdate() {
//        return numDocToUpdate;
//    }
//
//    public void setNumDocToUpdate(String numDocToUpdate) {
//        this.numDocToUpdate = numDocToUpdate;
//    }
//
//    public List<Articles> getListArt() {
//        return listArt;
//    }
//
//    public void setListArt(List<Articles> listArt) {
//        this.listArt = listArt;
//    }
//
//    public List<Articles> getListFilterArt() {
//        return listFilterArt;
//    }
//
//    public void setListFilterArt(List<Articles> listFilterArt) {
//        this.listFilterArt = listFilterArt;
//    }
//
//    public List<DocStock> getListDocStock() {
//        return listDocStock;
//    }
//
//    public void setListDocStock(List<DocStock> listDocStock) {
//        this.listDocStock = listDocStock;
//    }
//
//    public List<DocStock> getListDocStockF() {
//        return listDocStockF;
//    }
//
//    public void setListDocStockF(List<DocStock> listDocStockF) {
//        this.listDocStockF = listDocStockF;
//    }
//
//    public boolean isDisplayDes() {
//        return displayDes;
//    }
//
//    public void setDisplayDes(boolean displayDes) {
//        this.displayDes = displayDes;
//    }
//
//    public boolean isDisplayPrix() {
//        return displayPrix;
//    }
//
//    public void setDisplayPrix(boolean displayPrix) {
//        this.displayPrix = displayPrix;
//    }
//
//    @Override
//    public boolean controleFiche(DocStock bean) {
//        if (bean.getTypeDoc() != null) {
//            switch (bean.getTypeDoc()) {
//                case TypeDoc.TR:
//                    if (source == null) {
//                        getMessage("Veillez choisir la source du transfert", FacesMessage.SEVERITY_ERROR);
//                        return false;
//                    } else {
//                        if (displayCrenoS && crenoSource == null) {
//                            getMessage("Vous devez indiquer le creno du depot source", FacesMessage.SEVERITY_ERROR);
//                            return false;
//                        }
//                    }
//                    if (destination == null) {
//                        getMessage("Veillez choisir la destination du transfert", FacesMessage.SEVERITY_ERROR);
//                        return false;
//                    } else {
//                        if (displayCrenoD && crenoDest == null) {
//                            getMessage("Vous devez indiquer le creno du depot destination", FacesMessage.SEVERITY_ERROR);
//                            return false;
//                        }
//                    }
//                    break;
//                case TypeDoc.SO:
//                case TypeDoc.EN:
//                    if (source == null) {
//                        getMessage("Veillez choisir le Depot", FacesMessage.SEVERITY_ERROR);
//                        return false;
//                    } else {
//                        if (displayCrenoS && crenoSource == null) {
//                            getMessage("Vous devez indiquer le creno du depot", FacesMessage.SEVERITY_ERROR);
//                            return false;
//                        }
//                    }
//                    break;
//            }
//            return true;
//        } else {
//            getMessage("Vous devez choisir le type de document", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//    }
//
//    @Override
//    public void updateBean() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public DocStock recopieView() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void populateView(DocStock bean) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void loadAll() {
//    }
//
//    public void chooseDepot(SelectEvent ev) {
//        Depots choix = (Depots) ev.getObject();
//        switch (sourceClick) {
//            case "D":
//                dStock.setDepDest(choix.getCodeDepot());
//                choixDepot(false);
//                break;
//            case "S":
//                dStock.setDepSource(choix.getCodeDepot());
//                choixDepot(true);
//                break;
//            default:
//                break;
//        }
//        closeDialog("listDep");
//        sourceClick = "";
//    }
//
//    public void openListDep() {
//        listDepot = dep.loadLisDepot(currentAgence.getId());
//        sourceClick = "S";
//        openDialog("listDep");
//        update(":ldep-form:ldep-S");
//    }
//
//    public void openListDepLie() {
//        sourceClick = "D";
//        listDepot.clear();
//        listDepot = dep.buildListDepoLie(source);
//        openDialog("listDep");
//        update(":ldep-form:ldep-S");
//    }
//    ConfigDoc cfg;
//
//    public void chooseTypedoc(ValueChangeEvent ev) {
//        String str = (String) ev.getNewValue();
//        FactoryDoc fdoc = new FactoryDoc();
//        switch (str) {
//            case "TR":
//                cfg = fdoc.creerDoc(TypeDoc.TR);
//                setDisplayDepoD(true);
//                setDisplayPrix(false);
//                break;
//            case "EN":
//                cfg = fdoc.creerDoc(TypeDoc.EN);
//                destination = null;
//                crenoDest = null;
//                setDisplayDepoD(false);
//                setDisplayCrenoD(false);
//                setDisplayPrix(true);
//                break;
//            case "SO":
//                cfg = fdoc.creerDoc(TypeDoc.SO);
//                destination = null;
//                crenoDest = null;
//                setDisplayDepoD(false);
//                setDisplayCrenoD(false);
//                setDisplayPrix(false);
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void choixDepot(boolean src) {
//        if (src) {
//            if (dStock.getDepSource() != null) {
//                source = chargement.loadDepot(dStock.getDepSource(), currentAgence.getId());
//                if (source != null) {
//                    listCrenaux = dep.buildListCrenaux(source);
//                    listDepot = dep.buildListDepoLie(source);
//                } else {
//                    dStock.setDepSource(null);
//                    listCrenaux.clear();
//                    listDepot.clear();
//                }
//                setDisplayCrenoS(!listCrenaux.isEmpty());
//                //puisque le choix du depot source détermine le depot dest et le creno dest, après chaque changement on réinitialise ces valeurs
//                destination = null;
//                crenoSource = null;
//                dStock.setDepDest(null);
//                dStock.setCrenoSource(null);
//            }
//        } else {
//            if (dStock.getDepDest() != null) {
//                //choisir le dépôt dans la liste des dépôt lié à source
//                if (source != null && dStock.getDepDest() != null) {
////                    if (source.getYvsDepotsList() != null) {
////                        for (YvsDepots d : source.getYvsDepotsList()) {
////                            if (d.getCode().equals(dStock.getDepDest())) {
////                                destination = d;
////                                break;
////                            }
////                        }
////                    }
//                }
//                if (destination != null) {
//                    listCrenaux = dep.buildListCrenaux(destination);
//                } else {
//                    destination = null;
//                    dStock.setDepDest(null);
//                    listCrenaux.clear();
//                }
//                setDisplayCrenoD(!listCrenaux.isEmpty());
//            }
//            crenoDest = null;
//            dStock.setCrenoDest(null);
//        }
//    }
//
//    public void choixDepot(String dest) {
//        if (dest.equals("D")) {
//            choixDepot(false);
//        } else {
//            choixDepot(true);
//        }
//    }
//
//    public void openListCreno(String choi) {
//        sourceClick = choi;
//        openDialog("listCrenaux");
//    }
//
//    public void chooseCrenaux(SelectEvent ev) {
//        CrenauxHoraire choix = (CrenauxHoraire) ev.getObject();
//        switch (sourceClick) {
//            case "CD":
//                dStock.setCrenoDest(choix.getCodeTranche());
//                choixCrenaux(false);
//                break;
//            case "CS":
//                dStock.setCrenoSource(choix.getCodeTranche());
//                choixCrenaux(true);
//                break;
//            default:
//                break;
//        }
//        closeDialog("listCrenaux");
//        sourceClick = "";
//    }
//
//    public void choixCrenaux(String choix) {
//        //on choisit le crénaux dans la liste des crénaux du dépot source
//        switch (choix) {
//            case "CD":
//                choixCrenaux(false);
//                break;
//            case "CS":
//                choixCrenaux(true);
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void choixCrenaux(boolean src) {
//        if (src) {
//            if (source != null && dStock.getCrenoSource() != null) {
////                if (source.getYvsCrenauxHoraireList() != null) {
////                    crenoSource = null;
////                    for (YvsDepotCrenaux d : source.getYvsCrenauxHoraireList()) {
////                        if (d.getIdCrenaux().getCodeTranche().equals(dStock.getCrenoSource())) {
////                            crenoSource = d.getIdCrenaux();
////                            break;
////                        }
////                    }
////                }
//            }
//            if (crenoSource == null) {
//                dStock.setCrenoSource(null);
//            }
//        } else {
//            if (destination != null && dStock.getCrenoSource() != null) {
////                if (destination.getYvsCrenauxHoraireList() != null) {
////                    crenoDest = null;
////                    for (YvsDepotCrenaux d : destination.getYvsCrenauxHoraireList()) {
////                        if (d.getIdCrenaux().getCodeTranche().equals(dStock.getCrenoDest())) {
////                            crenoDest = d.getIdCrenaux();
////                            break;
////                        }
////                    }
////                }
//            }
//            if (crenoDest == null) {
//                dStock.setCrenoDest(null);
//            }
//        }
//    }
//
//    public void chooseCatC(SelectEvent ev) {
//    }
//
//    public void remove() {
//        if (ds != null) {
//            //vérifie si le document est modifiable
//            if (ds.getStatut() == 'B') {
//                //document bloqué
//                getMessage("Impossible de modifier ce document. Il est bloqué !", FacesMessage.SEVERITY_ERROR);
//            } else if (ds.getStatut() == 'V') {
//                //document Validé
//                openDialog("confirmRem");
//            } else {
//                removeToList(false);
//            }
//        }
//    }
//
//    public void removeToList(boolean update) {
//        //controle le stock à la destination si SO ou TR
//        //controle le stock à la source si EN         
//        if (selectedContent != null) {
//            switch (dStock.getTypeDoc()) {
//                case "EN":
//                    if (controleStock(selectedContent.getProduit(), source)) {
//                        //contrôle stock
//                        if (((dao.getStocks(selectedContent.getProduit().getId(), source.getId(), new Date()) - selectedContent.getQuantite()) < 0)) {
//                            getMessage("Stock insuffisant ", FacesMessage.SEVERITY_ERROR);
//                            return;
//                        }
//                    }
//                    break;
//                case "TR":
//                    if (controleStock(selectedContent.getProduit(), destination)) {
//                        //contrôle stock
//                        if (((dao.getStocks(selectedContent.getProduit().getId(), destination.getId(), new Date()) - selectedContent.getQuantite()) < 0)) {
//                            getMessage("Stock insuffisant ", FacesMessage.SEVERITY_ERROR);
//                            return;
//                        }
//                    }
//                    break;
//            }
//            dao.delete(new YvsContenuDocStock(selectedContent.getId()));
//            System.err.println(selectedContent.getId());
//            listContentDS.remove(selectedContent);
//            succes();
//            if (listContentDS.isEmpty()) {
//                setDisabledTypeDoc(false);
//                setDisabledDepoS(false);
//                setDisabledDepoD(false);
//            }
//            calculTotal(listContentDS);
//            if (update) {
//                ds.setStatut('O');
//                ds.setDateUpdateDoc(new Date());
//                dao.update(ds);
//            }
//        }
//    }
//
//    public void resetView() {
//        resetFiche(dStock);
//        dStock.setNumPiece(null);
//        dStock.setNumRefPiece(null);
//        listContentDS.clear();
//        catCompta = null;
//        source = destination = null;
//        crenoDest = crenoSource = null;
//        updateDocStock = displayCrenoD = displayCrenoS = displayDepoD = false;
//        dStock.setTypeDoc(null);
//        setDisabledTypeDoc(false);
//        setDisabledDepoS(false);
//        setDisabledDepoD(false);
//        setDisabledDate(false);
//        //init taxe
//        int i = 0;
//        for (BaseTaxe b : listBaseTaxe) {
//            b.setBase(0);
//            b.setMontant(0);
//            listBaseTaxe.set(i, b);
//            i++;
//        }
//        update(":stock-head");
//        update(":AddS-form");
//        update(":contentS-form");
//        update(":coutS-add-form");
//    }
////vider le formule d'ajout d'un nouvel article
//    private void videForm() {
//        dStock.setRefProd(null);
//        dStock.setQuantite(1);
//        dStock.setPrix(0);
//    }
//
//    //choisir un document de stock à partir du dataTable
//    public void chooseDocStock(SelectEvent ev) {
//        YvsDocStocks doc = yvsDocStocks.get(listDocStock.indexOf((DocStock) ev.getObject()));
//        choixDocToUpdate(doc);
//        ds = doc;
//        closeDialog("listDS");
//    }
//
//    //récupère un document de stock à partir de son numéro de pièce
//    public void loadOneDocStock() {
//        String[] chmp = {"agence", "numPiece"};
//        Object[] val = {currentAgence.getId(), numDocToUpdate};
//        ds = (YvsDocStocks) dao.loadOneByNameQueries("YvsDocStocks.findByNumPiece", chmp, val);
//        choixDocToUpdate(ds);
//    }
//
//    private void choixDocToUpdate(YvsDocStocks d) {
//        if (d == null) {
//            numDocToUpdate = null;
//        } else {
//            dStock.setTypeDoc(d.getTypeDoc());
//            numDocToUpdate = d.getNumPiece();
//            buildContentDocStock(d.getYvsContenuDocStockList());
//            dStock.setId(d.getId());
//            dStock.setNumPiece(d.getNumPiece());
//            dStock.setNumRefPiece(d.getNumRef());
//            dStock.setDate(d.getDateDoc());
//            dStock.setDepSource(d.getSource().getCode());
//            dStock.setDepDest((d.getDestination() != null) ? d.getDestination().getCode() : null);
//            dStock.setCrenoSource((d.getCrenoSource() != null) ? d.getCrenoSource().getCodeTranche() : null);
//            dStock.setCrenoDest((d.getCrenoDest() != null) ? d.getCrenoDest().getCodeTranche() : null);
//            dStock.setNumDoc(d.getNumDoc());
//            dStock.setStatut(d.getStatut().toString());
//            dStock.setDateSaveDoc(d.getDateSaveDoc());
//            dStock.setPrefixe(d.getPrefixe());
//            source = d.getSource();
//            destination = d.getDestination();
//            setDisplayCrenoD(crenoDest != null);
//            setDisplayCrenoS(crenoSource != null);
//            setDisplayDepoD(destination != null);
//            setDisplayPrix(dStock.getTypeDoc().equals(TypeDoc.EN));
//            disableAll();
//            //charger les frais supplémentaire
//            yvsCoutSupp = d.getYvsCoutSupDocStocks();
//            for (YvsCoutSupDocStock c : yvsCoutSupp) {
//                switch (c.getLibelle()) {
//                    case Constantes.TRANSPORT:
//                        dStock.setFraisTransport(c.getMontant());
//                        break;
//                    case Constantes.MANUTENTION:
//                        dStock.setFraisManutention(c.getMontant());
//                        break;
//                    case Constantes.ASSURANCE:
//                        dStock.setFraisAssurance(c.getMontant());
//                        break;
//                    case Constantes.COMMISSION:
//                        dStock.setFraisComission(c.getMontant());
//                        break;
//                    case Constantes.AUTRES:
//                        dStock.setAutreFrais(c.getMontant());
//                        break;
//                }
//            }
//
//        }
//        docOk = true;
//    }
//
//    private void buildContentDocStock(List<YvsContenuDocStock> list) {
//        listContentDS.clear();
//        numero = 0;
//        for (YvsContenuDocStock cda : list) {
//            ContenuDoc cd = new ContenuDoc();
//            cd.setProduit(buildArt(cda.getArticles()));
//            cd.setId(cda.getId());
//            cd.setDesignation(cda.getArticles().getDesignation());
//            cd.setPrix(cda.getPrix());
//            cd.setQuantite(cda.getQuantite());
//            cd.setRefProd(cda.getArticles().getRefArt());
//            cd.setNumero(numero = numero + 1);
//            listContentDS.add(cd);
//        }
//        calculTotal(listContentDS);
//    }
//
//    public void loadListDocToUpdate() {
//        updateDocStock = true;
//        sourceClick = "BU";
//        String[] chmp = {"agence"};
//        Object[] val = {currentAgence.getId()};
//        yvsDocStocks = dao.loadListTableByNameQueries("YvsDocStocks.findAllInAgence", chmp, val);
//        listDocStock = dStock.buildListBean(yvsDocStocks);
//        openDialog("listDS");
//    }
//    private double oldValue;
//    private int line;
//    private int col;
//
//    public void edit(boolean update) {
//        switch (dStock.getTypeDoc()) {
//            case "EN":
//                //si old>new, on controle le stock
//                if (id.equals("listContentDS:" + line + ":colQteS")) {
//                    if (oldValue > newVal) {
//                        col = 1;
//                        if (controleStock(cdv.getProduit(), source)) {
//                            //contrôle stock
//                            if (((dao.getStocks(cdv.getProduit().getId(), source.getId(), new Date()) - newVal) < 0)) {
//                                getMessage("Stock insuffisant ", FacesMessage.SEVERITY_ERROR);
//                                cancelEdit();
//                                return;
//                            }
//                        }
//                        cdv.setQuantite(newVal);
//                    }
//                } else if (id.equals("listContentDS:" + line + ":colPuE")) {
//                    col = 2;
//                    cdv.setPrix(newVal);
//                }
//                break;
//            case "SO":
//                //si old<new, on controle le stock
//                if (id.equals("listContentDS:" + line + ":colQteS")) {
//                    if (oldValue > newVal) {
//                        col = 1;
//                        if (controleStock(cdv.getProduit(), source)) {
//                            //contrôle stock
//                            if (((dao.getStocks(cdv.getProduit().getId(), source.getId(), new Date()) - newVal) < 0)) {
//                                getMessage("Stock insuffisant ", FacesMessage.SEVERITY_ERROR);
//                                cancelEdit();
//                                return;
//                            }
//                        }
//                        cdv.setQuantite(newVal);
//                    }
//                }
//                break;
//            case "TR":
//                //si old>new, on controle le stock
//                if (id.equals("listContentDS:" + line + ":colQteS")) {
//                    col = 1;
//                    if (oldValue > newVal) {
//                        if (controleStock(cdv.getProduit(), source)) {
//                            //contrôle stock
//                            if (((dao.getStocks(cdv.getProduit().getId(), source.getId(), new Date()) - newVal) < 0)) {
//                                getMessage("Stock insuffisant ", FacesMessage.SEVERITY_ERROR);
//                                cancelEdit();
//                                return;
//                            }
//                        }
//                        cdv.setQuantite(newVal);
//                    }
//                } else {
//                    if (controleStock(cdv.getProduit(), destination)) {
//                        //contrôle stock
//                        if (((dao.getStocks(cdv.getProduit().getId(), destination.getId(), new Date()) - newVal) < 0)) {
//                            getMessage("Stock insuffisant ", FacesMessage.SEVERITY_ERROR);
//                            cancelEdit();
//                            return;
//                        }
//                    }
//                    cdv.setQuantite(newVal);
//                }
//                break;
//        }
//        if (newVal != 0) {
//            continueEdit();
//            if (update) {
//                ds.setStatut('O');
//                ds.setDateUpdateDoc(new Date());
//                dao.update(ds);
//            }
//        } else {
//            selectedContent = cdv;
//            remove();
//        }
//    }
//    String id;
//    double newVal;
//
//    public void editListContentArt(CellEditEvent ev) {
//        line = ev.getRowIndex();
//        UIColumn column = ev.getColumn();
//        id = column.getClientId();
//        oldValue = (double) ev.getOldValue();
//        newVal = (double) ev.getNewValue();
//        if (ev.getNewValue() != ev.getOldValue()) {
//            cdv = listContentDS.get(line);
//            if (ds != null) {
//                //vérifie si le document est modifiable
//                if (ds.getStatut() == 'B') {
//                    //document bloqué
//                    getMessage("Impossible de modifier ce document. Il est bloqué !", FacesMessage.SEVERITY_ERROR);
//                } else if (ds.getStatut() == 'V') {
//                    //document Validé
//                    openDialog("confirmEdit");
//                } else {
//                    edit(false);
//                }
//            }
//        }
//    }
//
//    public void continueEdit() {
//        produit = buildArt(cdv.getProduit());
//        YvsContenuDocStock c = buildContent(cdv);
//        c.setId(cdv.getId());
//        dao.update(c);
//        calculTotal(listContentDS);
//    }
//
//    public void cancelEdit() {
//        cdv = listContentDS.get(line);
//        if (col == 1) {
//            cdv.setQuantite(oldValue);
//        } else if (col == 2) {
//            cdv.setPrix(oldValue);
//        } else if (col == 3) {
//            cdv.setRemise1(oldValue);
//        } else if (col == 4) {
//            cdv.setRemise2(oldValue);
//        }
//        listContentDS.set(line, cdv);
//    }
//    ContenuDoc cdv;
//
//    public void addToList() {
//        //vérifie si le document est modifiable
//        if (produit != null && ds!=null) {
//            if (ds.getStatut() == 'B') {
//                //document bloqué
//                getMessage("Impossible de modifier ce document. Il est bloqué !", FacesMessage.SEVERITY_ERROR);
//            } else if (ds.getStatut() == 'V') {
//                //document Validé
//                openDialog("confirmAdd");
//            } else {
//                ajouter(false);
//            }
//        }
//    }
//
//    public void ajouter(boolean update) {
//        if (TypeDoc.TR.equals(dStock.getTypeDoc()) || TypeDoc.SO.equals(dStock.getTypeDoc())) {
//            if (controleStock(buildArt(produit), source)) {
//                if ((chargement.getStocks(produit.getId(), source.getId(), new Date()) - dStock.getQuantite()) < 0) {
//                    getMessage("Stock insuffisant ", FacesMessage.SEVERITY_ERROR);
//                    return;
//                }
//            }
//        }
//        cdv = new ContenuDoc();
//        cdv.setDesignation(dStock.getDesignationProduit());
//        cdv.setRefProd(dStock.getRefProd());
//        cdv.setProduit(buildArt(produit));
//        cdv.setPrix(dStock.getPrix());
//        cdv.setQuantite(dStock.getQuantite());
//        cdv.setNumero(numero++);
//        YvsContenuDocStock c = buildContent(cdv);
//        c = (YvsContenuDocStock) dao.save1(c);
//        cdv.setId(c.getId());
//        cdv.setPersit(true);
//        listContentDS.add(0, cdv);
//        setDisabledTypeDoc(true);
//        setDisabledDepoS(true);
//        setDisabledDepoD(true);
//        calculTotal(listContentDS);
//        if (update) {
//            ds.setStatut('O');
//            ds.setDateUpdateDoc(new Date());
//            dao.update(ds);
//        }
//        videForm();
//    }
//
//    private YvsContenuDocStock buildContent(ContenuDoc cd) {
//        YvsContenuDocStock c = new YvsContenuDocStock();
//        c.setArticles(produit);
//        c.setDocStock(ds);
//        c.setPrix(cd.getPrix());
//        c.setQuantite(cd.getQuantite());
//        return c;
//
//    }
//
//    private Integer getNumeroLastDoc(String prefixe) {
//        String[] chmp = {"agence", "prefixe"};
//        Object[] val = {currentAgence.getId(), prefixe};
//        Integer res = (Integer) dao.loadOneByNameQueries("YvsDocStocks.findByPrefixe", chmp, val);
//        if (res != null) {
//            return res;
//        } else {
//            return 0;
//        }
//    }
//
//    private void generatedNumDoc() {
//        int N;
//        //on génère la pièce uniquement à l'enregistrement et pas à la modification
//        if (!updateDocStock) {
//            String pref = dStock.getPrefixe(cfg.getPrefixe(), cfg.isMonth(), cfg.isYear());
//            N = (int) getNumeroLastDoc(pref);
//            String nump = dStock.generateNum(cfg.getPrefixe(), cfg.isMonth(), cfg.isYear(), (N + 1), cfg.getNbDigit());
//            dStock.setNumPiece(nump);
//            dStock.setNumDoc(N + 1);
//            dStock.setNumRefPiece(dStock.generateNum(nump, true, cfg.isFournisseur(), currentAgence.getCodeagence(), ""));
//            dStock.setStatut("O");
//        }
//    }
//    YvsDocStocks ds;
//
//    public void saveDocumentVente(String type) {
//        if (controleFiche(dStock)) {
//            //générer le numéro de document
//            generatedNumDoc();
//            ds = buildDocStock(type);
//            if (!updateDocStock) {
//                ds = (YvsDocStocks) dao.save1(ds);
//                dStock.setId(ds.getId());
//            } else {
//                dao.update(ds);
//            }
//            disableAll();
//            updateDocStock = docOk = true;
//            succes();
//        } else {
//            updateDocStock = docOk = false;
//        }
//
//    }
//
//    private YvsDocStocks buildDocStock(String type) {
//        YvsDocStocks doc = new YvsDocStocks();
//        doc.setId(dStock.getId());
//        doc.setDateDoc(dStock.getDate());
//        doc.setTypeDoc(type);
//        doc.setPrefixe(dStock.getPrefixe());
//        doc.setNumDoc(dStock.getNumDoc());
//        doc.setNumPiece(dStock.getNumPiece());
//        doc.setNumRef(dStock.getNumRefPiece());
//        doc.setStatut(dStock.getStatut().charAt(0));
//        doc.setSource(source);
//        doc.setDestination(destination);
////        doc.setCatCompta(catCompta);       
//        doc.setCrenoSource(crenoSource);
//        doc.setCrenoDest(crenoDest);
//        if (updateDocStock) {
//            doc.setDateUpdateDoc(new Date());
//            doc.setDateSaveDoc(dStock.getDateSaveDoc());
//            doc.setLastAuthor(currentUser);
//        } else {
//            doc.setDateSaveDoc(new Date());
//            doc.setDateUpdateDoc(new Date());
//            doc.setAuthor(currentUser);
//            doc.setLastAuthor(currentUser);
//        }
//        return doc;
//    }
//
//    public void disableAll() {
////        setDisabledCrenoD(true);
////        setDisabledCrenoS(true);
//        //s'il y a pas encore de contenu, on peut modifier la source
//        setDisabledDepoD(!listContentDS.isEmpty());
//        setDisabledDepoS(!listContentDS.isEmpty());
//        setDisabledTypeDoc(true);
//        setDisabledDate(true);
//    }
//
//    /*
//     * Saisi produit
//     */
//    private boolean controleStock(Articles art, YvsBaseDepots dep) {
//        if (art.isSuiviEnStock()) {
//            return dep.getControlStock();
//        } else {
//            return false;
//        }
//    }
//
//    public void saisieProduit() {
//        if (docOk) {
//            //le produit selectionné doit être un produit du dépot choisit
//            if (restricToDep) {
//                //choisi dans le dépôt
//                produit = chargement.loadOneArtNegoceInDepot((source != null) ? source.getId() : 0, dStock.getRefProd(), ltypeArt);
//            } else {
//                //choisi dans l'Agence
//                produit = chargement.loadOneArtNegoceInAgence(currentAgence.getId(), dStock.getRefProd(), ltypeArt);
//            }
//            if (produit == null) {
//                dStock.setRefProd(null);
//            } else {
//                dStock.setRefProd(produit.getRefArt());
//                dStock.setDesignationProduit(produit.getDesignation());
//                dStock.setPrix(produit.getPua());
//            }
//        }
//    }
//
//    public void openListArt() {
//        listArt.clear();
//        if (docOk) {
//            if (restricToDep) {
//                //choisi dans le dépôt
//                listArt = chargement.loadListArtNegoceInDepot(source.getId(), ltypeArt);
//            } else {
//                //choisi dans l'Agence
//                listArt = chargement.loadListArtNegoceInAgence(currentAgence.getId(), ltypeArt);
//            }
//            openDialog("listArt");
//        }
//    }
//
//    public void chooseArticle(SelectEvent ev) {
//        Articles art = (Articles) ev.getObject();
//        if (produit == null) {
//            produit = new YvsArticles();
//        }
//        if (art != null) {
//            produit.setId(art.getId());
//            dStock.setRefProd(art.getRefArt());
//            dStock.setDesignationProduit(art.getDesignation());
//            dStock.setPrix(art.getPua());
//            closeDialog("listArt");
//        }
//    }
//
//    private Articles buildArt(YvsArticles a) {
//        Articles ar = new Articles();
//        if (a != null) {
//            ar.setId(a.getId());
//            ar.setPua(a.getPua());
//            ar.setPuv(a.getPuv());
//            ar.setPuvMin(a.getPrixMin());
////            ar.setSuiviEnStock(a.isSuiviEnStock());
////            ar.setMethodeVal(a.getMathodeVal());
//            ar.setRefArt(a.getRefArt());
//        }
//        return ar;
//    }
//
//    private YvsArticles buildArt(Articles a) {
//        YvsArticles ar = new YvsArticles(a.getId());
//        ar.setPua(a.getPua());
//        ar.setPuv(a.getPuv());
//        ar.setPrixMin(a.getPuvMin());
//        ar.setRefArt(a.getRefArt());
//        return ar;
//    }
//
//    public void saveCoutSup() {
//        if (docOk && yvsCoutSupp.isEmpty()) {
//            YvsCoutSupDocStock ca = new YvsCoutSupDocStock();
//            //frais de transport
//            ca.setLibelle(Constantes.TRANSPORT);
//            ca.setMontant(dStock.getFraisTransport());
//            ca.setDocStock(ds);
//            dao.save(ca);
//            ca.setLibelle(Constantes.ASSURANCE);
//            ca.setMontant(dStock.getFraisAssurance());
//            ca.setDocStock(ds);
//            dao.save(ca);
//            ca.setLibelle(Constantes.MANUTENTION);
//            ca.setMontant(dStock.getFraisManutention());
//            ca.setDocStock(ds);
//            dao.save(ca);
//            ca.setLibelle(Constantes.COMMISSION);
//            ca.setMontant(dStock.getFraisComission());
//            ca.setDocStock(ds);
//            dao.save(ca);
//            ca.setLibelle(Constantes.AUTRES);
//            ca.setMontant(dStock.getAutreFrais());
//            ca.setDocStock(ds);
//            dao.save(ca);
//        } else if (!yvsCoutSupp.isEmpty()) {
//            for (YvsCoutSupDocStock c : yvsCoutSupp) {
//                switch (c.getLibelle()) {
//                    case Constantes.TRANSPORT:
//                        c.setMontant(dStock.getFraisTransport());
//                        dao.update(c);
//                        break;
//                    case Constantes.MANUTENTION:
//                        c.setMontant(dStock.getFraisManutention());
//                        dao.update(c);
//                        break;
//                    case Constantes.ASSURANCE:
//                        c.setMontant(dStock.getFraisAssurance());
//                        dao.update(c);
//                        break;
//                    case Constantes.COMMISSION:
//                        c.setMontant(dStock.getFraisComission());
//                        dao.update(c);
//                        break;
//                    case Constantes.AUTRES:
//                        c.setMontant(dStock.getAutreFrais());
//                        dao.update(c);
//                        break;
//                }
//            }
//        }
//        calculTotal(listContentDS);
//        succes();
//    }
//
//    private void calculTotal(List<ContenuDoc> l) {
//        dStock.setValTotalHT(0);
//        dStock.setValTotalTTC(0);
//        for (ContenuDoc c : l) {
//            dStock.setValTotalHT(dStock.getValTotalHT() + (c.getQuantite() * c.getPrix()));
//            dStock.setValTotalTTC(dStock.getValTotalTTC() + (c.getQuantite() * c.getPrix()));
//        }
//        dStock.setValTotalHT(dStock.getValTotalHT() + dStock.getFraisAssurance() + dStock.getFraisComission() + dStock.getFraisManutention() + dStock.getFraisTransport() + dStock.getAutreFrais());
//        dStock.setValTotalTTC(dStock.getValTotalTTC() + dStock.getFraisAssurance() + dStock.getFraisComission() + dStock.getFraisManutention() + dStock.getFraisTransport() + dStock.getAutreFrais());
//    }
//
//    @Override
//    public void deleteBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void unLoadOnView(UnselectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}
