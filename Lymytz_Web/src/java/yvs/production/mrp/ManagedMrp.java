/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.mrp;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.production.UtilProd;
import yvs.entity.production.planification.YvsProdDetailPdp;
import yvs.entity.production.planification.YvsProdPeriodePlan;
import yvs.entity.production.planification.YvsProdPlanification;
import yvs.production.technique.Nomenclature;
import yvs.production.planification.DetailPlanPDP;
import yvs.production.planification.PeriodePlanification;
import yvs.production.planification.Planification;
import yvs.production.planification.view.ObjectMrp;
import yvs.production.planification.view.PlanifArticles;
import yvs.production.planification.view.DataColone2D;
import yvs.production.planification.view.ObjectData;
import yvs.production.planification.view.DataLigne2D;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class ManagedMrp extends Managed<Mrp, YvsBaseCaisse> implements Serializable {

    private Planification choixPdp;
    @ManagedProperty(value = "#{mrp}")
    private Mrp mrp;
    private List<Planification> listPlanification, listPlanification_;
    private boolean displayPdp;
    /*----------------------*/
    private List<PlanifArticles> listDataArticles;
    private List<PlanifArticles> listDataArticlesMrp; //pour contenir le résultat du calcul mrp   

    private List<PeriodePlanification> headers;
    /*------------Liste Nomenclature-----------*/
    private List<Nomenclature> listNomenclature;
    private List<ObjectMrp> listDataAProduire;

    private List<Mrp> listMrp;
    private List<Articles> listAricles;   //contiendra la liste des article concerné par un PDP (composant des nomenclature)
    private Articles article = new Articles();

    public ManagedMrp() {
        listMrp = new ArrayList<>();
        listPlanification = new ArrayList<>();
        listPlanification_ = new ArrayList<>();
        headers = new ArrayList<>();
        listDataArticles = new ArrayList<>();
        listNomenclature = new ArrayList<>();
        listDataArticlesMrp = new ArrayList<>();
        listDataAProduire = new ArrayList<>();
        listAricles = new ArrayList<>();
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public void setListAricles(List<Articles> listAricles) {
        this.listAricles = listAricles;
    }

    public List<Articles> getListAricles() {
        return listAricles;
    }

    public List<ObjectMrp> getListDataAProduire() {
        return listDataAProduire;
    }

    public void setListDataAProduire(List<ObjectMrp> listDataAProduire) {
        this.listDataAProduire = listDataAProduire;
    }

    public List<Nomenclature> getListNomenclature() {
        return listNomenclature;
    }

    public void setListNomenclature(List<Nomenclature> listNomenclature) {
        this.listNomenclature = listNomenclature;
    }

    public List<PlanifArticles> getListDataArticlesMrp() {
        return listDataArticlesMrp;
    }

    public void setListDataArticlesMrp(List<PlanifArticles> listDataArticlesMrp) {
        this.listDataArticlesMrp = listDataArticlesMrp;
    }

    public List<Mrp> getListMrp() {
        return listMrp;
    }

    public void setListMrp(List<Mrp> listMrp) {
        this.listMrp = listMrp;
    }

    public List<Planification> getListPlanification() {
        return listPlanification;
    }

    public void setListPlanification(List<Planification> listPlanification) {
        this.listPlanification = listPlanification;
    }

    public Planification getChoixPdp() {
        return choixPdp;
    }

    public void setChoixPdp(Planification choixPdp) {
        this.choixPdp = choixPdp;
    }

    public Mrp getMrp() {
        return mrp;
    }

    public void setMrp(Mrp mrp) {
        this.mrp = mrp;
    }

    public boolean isDisplayPdp() {
        return displayPdp;
    }

    public void setDisplayPdp(boolean displayPdp) {
        this.displayPdp = displayPdp;
    }

    public List<PeriodePlanification> getHeaders() {
        return headers;
    }

    public void setHeaders(List<PeriodePlanification> headers) {
        this.headers = headers;
    }

    public List<PlanifArticles> getListDataArticles() {
        return listDataArticles;
    }

    public void setListDataArticles(List<PlanifArticles> listDataArticles) {
        this.listDataArticles = listDataArticles;
    }

    ObjectData data_;
    private DataLigne2D detailData;
    private DataLigne2D detailData1;
    private DataLigne2D detailData2;

    private List<YvsProdPlanification> listEntityPlanif;
    private boolean disNextP, disPrevP;

    public boolean isDisNextP() {
        return disNextP;
    }

    public void setDisNextP(boolean disNextP) {
        this.disNextP = disNextP;
    }

    public boolean isDisPrevP() {
        return disPrevP;
    }

    public void setDisPrevP(boolean disPrevP) {
        this.disPrevP = disPrevP;
    }

    /**
     * ***
     */
    /**
     * Début de la planification MRP*
     */
    /**
     * @param avancer
     *
     * @param init**
     */
    //charger les données du PDP
    public void loadDataPdp(boolean avancer, boolean init) {
        //charge les données PDP
        champ = new String[]{"societe", "typePlan"};
        val = new Object[]{currentAgence.getSociete(), "PDP"};
        listPlanification.clear();
        listPlanification_.clear();
        nameQueriCount = "YvsProdPlanification.findByTypeC";
        nameQueri = "YvsProdPlanification.findByType";
        listEntityPlanif = loadPlanification(avancer, init);
        listPlanification = UtilProd.buildBeanListPlanification(listEntityPlanif);
        listPlanification_.addAll(listPlanification);
    }

    private List<YvsProdPlanification> loadPlanification(boolean avancer, boolean init) {
        if (!init) {
            if (avancer) {
                setFirtResult(getFirtResult() + getNbMax());
            } else {
                setFirtResult(getFirtResult() - getNbMax());
            }
            if (getTotalPage() == getCurrentPage()) {
                setFirtResult(0);
            }
        } else {
            setFirtResult(0);
        }
        PaginatorResult<YvsProdPlanification> pa = new PaginatorResult<>();
        pa = pa.loadResult(nameQueriCount, nameQueri, champ, val, getFirtResult(), getNbMax(), dao);
        setDisNextP(pa.isDisNext());
        setDisPrevP(pa.isDisPrev());
        setTotalPage(pa.getNbPage());
        setCurrentPage(pa.getCurrentPage());
        return pa.getResult();
    }
    //fin de la méthode de chargement du pdp

    public DataLigne2D loadData() {
        detailData = new DataLigne2D("Vente");
//        for (int i = 0; i <= 11; i++) {
//            data_ = new ObjectData(i + new Random().nextInt(100));
//            data_.setId_("V_" + id_ + "_" + i);
//            detailData.getListData().add(data_);
//        }
//        detailData1 = new DataLigne2D("Production");
//        for (int i = 0; i <= 11; i++) {
//            data_ = new ObjectData(i + new Random().nextInt(100));
//            data_.setId_("P_" + id_ + "_" + i);
//            detailData1.getListData().add(data_);
//        }
//        detailData2 = new DataLigne2D("Stocks");
//        for (int i = 0; i <= 11; i++) {
//            data_ = new ObjectData(i + new Random().nextInt(100));
//            data_.setId_("S_" + id_ + "_" + i);
//            detailData2.getListData().add(data_);
//        }
        return detailData;
    }
    List<DataLigne2D> listDataPic;
    int id_;

    public void chooseOnePdp(int numLine) {
        //charge les détails du PDP
        YvsProdPlanification pdp = listEntityPlanif.get(listEntityPlanif.indexOf(new YvsProdPlanification(listPlanification.get(numLine).getId())));
        if (pdp != null) {
            displayPdpInBean(pdp);
            mrp.setReference("RQ-/" + pdp.getReference());
//            loadDataNomenclature();
//            calculBesoinBrut();
//            update("panelTab_");
//            update("panelTab_Mrp");
//            update("ref_pdp");
        } else {
            getWarningMessage("PDP incomplet !");
        }
    }

    //charge les donées du PDP dans la liste listDataAProduire.
    //chaque ligne de cette liste contient un article à produire
    private void displayPdpInBean(YvsProdPlanification pdp) {
        listDataAProduire.clear();
        listAricles.clear();
        ObjectMrp lineMrp;
        int index;
        DataLigne2D linePdp;
        headers.clear();
        for (YvsProdPeriodePlan pe : pdp.getYvsProdPeriodePlanList()) {
            headers.add(UtilProd.buildPeriodePlan(pe));
            for (YvsProdDetailPdp de : pe.getYvsProdDetailPdpList()) {
                if (de.getTypeVal().equals(Constantes.PDP_TYPE_OPD)) {
                    //charge l'article
                    lineMrp = new ObjectMrp();
                    lineMrp.setId(de.getArticle().getId());
                    if (!listDataAProduire.contains(lineMrp)) {
                        linePdp = new DataLigne2D(de.getArticle().getRefArt());
                        linePdp.getListData();
                        lineMrp.setLinePdp(linePdp);
                        lineMrp.setArticle(UtilProd.buildBeanArticles(de.getArticle()));
                        listDataAProduire.add(lineMrp);
                    } else {
                        index = listDataAProduire.indexOf(lineMrp);
                        lineMrp = listDataAProduire.get(index);
                    }

                    ObjectData nData_ = new ObjectData(de.getId(), de.getValeur(), 0, "OPF_" + de.getId());
                    lineMrp.getLinePdp().getListData().add(new DataColone2D(nData_));
                }
            }
        }
//        System.err.println("-Article- "+listDataAProduire.get(0).getArticle().getDesignation());
//        for(DataColone2D dc:listDataAProduire.get(0).getLinePdp().getListData()){
//            System.err.println("-Qte- "+dc.getData().getValue());    
//        }

        loadDataMrp();
    }

    //parcours les donées du PDP chargé afin de calculer pour chaque article ses besoins
    private void loadDataMrp() {
        List<ObjectMrp> listData_ = new ArrayList<>(listDataAProduire);
        //charge toutes les données de nomenclatures concernée
        loadDataNomenclature();
//        DataColone2D periode;
        Nomenclature nomenclature_;
        List<ComposantsMrp> contentMrp;
        int num_;
        for (ObjectMrp pa : listData_) {
            //récupère la nomenclature pour cet article            
            nomenclature_ = giveOneNomenclature(pa.getArticle());
            num_ = 0;
            if (nomenclature_ != null) {
                for (DataColone2D periode : pa.getLinePdp().getListData()) {
//                    periode = period;//cet objet contient pour un article et une période le besoins en production(la quantité à fabrique)                              
                    //calcul des besoins d'une nomenclature pour la période courante
                    PeriodePlanification p = headers.get(num_);
                    contentMrp = calculBesoinsBrut(nomenclature_, p, new DetailPlanPDP(periode.getData().getId(), periode.getData().getValue()));
                    //ajouter la liste de contenu MRP à la liste des données à Afficher (listDataAProduire)
                    addDataNomenclatureInList(pa.getArticle(), contentMrp, p);
                    num_++;
                }
            } else {
                getWarningMessage("Aucune nomenclature trouvé pour " + pa.getArticle().getDesignation());
            }
        }
    }

    //cette méthode récupère les nomenclatures concernée par les donnée du PDP contenu dans listDataAProduire
    private void loadDataNomenclature() {
        //charge les nomenclatures des articles concernés par le PDP
        List<Long> listeArt = new ArrayList<>();
        for (ObjectMrp p : listDataAProduire) {
            listeArt.add(p.getArticle().getId());
        }
        listNomenclature = loadNomenclature(listeArt);
    }

    private Nomenclature giveOneNomenclature(Articles ap) {
        //recherche séquentiel
        for (Nomenclature n : listNomenclature) {
            if (n.getCompose().equals(ap)) {
                return n;
            }
        }
        return null;
    }

    //la fonction de calcul des besoins est définie par une méthode qui prend en entrée:
    //Une nomenclature, Une période et retourne une List d'objet Composant MRP
    private List<ComposantsMrp> calculBesoinsBrut(Nomenclature nom_, PeriodePlanification p, DetailPlanPDP detailPdp) {
        List<ComposantsMrp> result = new ArrayList<>();
        ComposantsMrp composant;
        double qte, rebut;
//        for (ComposantNomenclature com_ : nom_.getComposants()) {
//            composant = new ComposantsMrp();
//            composant.setNomenclature(com_);
//            composant.setPdp(detailPdp);
//            //convertion de la quantité du composant en unité du composé
//            if (!com_.getUnite().equals(nom_.getUnite())) {
//                qte = convertirUnite(nom_.getUnite(), com_.getUnite(), com_.getQuantite());
//            } else {
//                qte = (com_.getQuantite() * detailPdp.getValeur() / nom_.getQuantite());
//            }
//            composant.setQuantite(qte);
//            result.add(composant);
//            if (!listAricles.contains(com_.getArticle())) {
//                listAricles.add(com_.getArticle());
//            }
//        }
        return result;
    }


    private void addDataNomenclatureInList(Articles art, List<ComposantsMrp> l, PeriodePlanification p) {
        ObjectMrp compose = new ObjectMrp(); //composé à produire: provenant du PDP
        compose.setArticle(art);
        compose.setId(art.getId());
        if (!listDataAProduire.contains(compose)) {
            compose = new ObjectMrp();
            compose.setArticle(art);
            compose.setId(art.getId());
            listDataAProduire.add(compose);
        }
        int idx = listDataAProduire.indexOf(compose);
        compose = listDataAProduire.get(idx);
        int numComposant = 0;
        DataLigne2D line;
        for (ComposantsMrp c : l) {
            if (compose.getLineNomenclature().size() > numComposant) {//l'attribut lineNomenclature de l'objet compose est une liste contenant la liste des composants d'une nomenclature
                line = compose.getLineNomenclature().get(numComposant);
            } else {
                line = new DataLigne2D(c.getNomenclature().getArticle().getRefArt());
                line.setId(c.getNomenclature().getId());
                compose.getLineNomenclature().add(line);
            }
            int pos = headers.indexOf(p);
            line.getListData().add(pos, new DataColone2D(new ObjectData(c.getQuantite(), "CC" + c.getPdp().getId())));
            numComposant++;
        }
    }

    //charge la liste des nomenclatures des produits
    private List<Nomenclature> loadNomenclature(List<Long> listArticle) {
        champ = new String[]{"articles"};
        val = new Object[]{listArticle};
        nameQueri = "YvsProdNomenclature.findByComposes";
        return UtilProd.buildBeanListNomenclature(dao.loadNameQueries(nameQueri, champ, val));
    }

    /**
     * *Chargement des besoins brut agégé******
     */
    public void loadDataAgrege() {
        List<ObjectMrp> listData_ = new ArrayList<>(listDataAProduire);
        List<DataLigne2D> result = new ArrayList<>();
        DataLigne2D composantNom;
        int numCol;
        for (ObjectMrp line : listData_) {
            for (DataLigne2D composant : line.getLineNomenclature()) {
                composantNom = new DataLigne2D(composant.getLabelDetail());
                composantNom.setId(composant.getId());
                numCol = 0;
                for (DataColone2D period : composant.getListData()) {
                    if (!result.contains(composantNom)) {
                        composantNom.getListData().add(period);
                        result.add(composantNom);
                    } else {
                        ObjectData o = new ObjectData();
                        if (result.get(result.indexOf(composantNom)).getListData().size() > numCol) {
                            o = result.get(result.indexOf(composantNom)).getListData().get(numCol).getData();
                            o.setValue(o.getValue() + period.getData().getValue());
                        } else {
                            result.get(result.indexOf(composantNom)).getListData().add(period);
                        }                        
                    }
                    numCol++;
                }
            }
        }
        ObjectMrp o = new ObjectMrp();
        o.setArticle(new Articles(0, "----"));
        o.setLineNomenclature(result);
        listDataAProduire.clear();
        listDataAProduire.add(o);
    }

    //détermination du mrp par période du prd
    public void calculBesoinBrut(Nomenclature nomenclature) {
        //parcours la liste listDataArticle pour récupérer le plan de production par article
//        ObjectData val;
//        int i = 0;
//        for (PlanifArticles pl : listDataArticles) {
//            //récupère la ligne correspondant au plan de production de la liste: c'est le dexième objet dan la liste
//            if (!pl.getDetails().isEmpty()) {
//                DataLigne2D pd = pl.getDetails().get(1);
//                //ensuite, chaque objetc de la liste ObjectData contenu dans ce plan représente la production prévu pour les périodes classées dans l'ordre chronologique
//                //construction des lignes par nomenclature
//                Nomenclature nom_ = findNomenclature(i, pl.getArticle());
//                PlanifArticles newCompose = new PlanifArticles();
//                newCompose.setArticle(pl.getArticle());
//                DataLigne2D detail = new DataLigne2D();
//        for (ComposantNomenclature cn : nom_.getListComposants()) { //parcours la nomenclature
//            detail = new DataLigne2D();
//            detail.setLabelDetail(cn.getArticle().getDesignation());
//            for (ObjectData value : pd.getListData()) { //parcours les périodes du pdp qui se trouve être aussi les périodes du mrp
//                val = new ObjectData(calculBesoin(value.getValue(), nom_.getQuantite(), cn.getQuantite()), "C_" + cn.getId() + "_" + nom_.getCompose().getId());
//                detail.getListData().add(val);
//            }
//            newCompose.getDetails().add(detail);
//        }
//                listDataArticlesMrp.add(newCompose);
//            }
//            i++;
//        }
    }
    //pour garder la liste des composants qu'on a déjà renconté avec le cumul de la qté. 
    //j'utilise cet objetpour la simple raison qu'il à la structure que je veux. id, et valeur. 
    private List<ObjectData> listComposant = new ArrayList<>();

    public void calculBesoinsNets() {
        DataLigne2D composant_ = null;
        ObjectData new_;
        int numCol = 0;
        double v = 0;
        for (PlanifArticles pl : listDataArticlesMrp) {
//            for (DataLigne2D pd : pl.getDetails()) {
//                if (!listComposant.contains(new ObjectData(0, pd.getLabelDetail()))) {
//                    composant_ = new DataLigne2D();
//                    composant_.setLabelDetail(pd.getLabelDetail());
//                    numCol = 0;
//                    for (ObjectData o : pd.getListData()) {
//                        v = o.getValue();
//                        new_ = new ObjectData(v, o.getId_());
//                        composant_.getListData().add(new_);
//                        numCol++;
//                    }
//                    listComposant.add(new ObjectData(v, pd.getLabelDetail()));
//                    listBesoinNets.add(composant_);
//                } else {
//                    composant_ = listBesoinNets.get(listBesoinNets.indexOf(pd));
//                    for (ObjectData o : pd.getListData()) {
//                        v = o.getValue();
//                        new_ = new ObjectData(v + (listComposant.get(numCol).getValue()), o.getId_());
//                        composant_.getListData().add(new_);
//                        listComposant.set(listComposant.indexOf(new_), new_);
//                        numCol++;
//                    }
//                    listBesoinNets.get(listBesoinNets.indexOf(pd)).getListData().get(id_);
//                }
//            }
        }
    }
//
//    private List<ObjectData> loadProportionNomenclature(ObjectData o, Articles art) {
//        //récupère la nomenclature de l'article passé en paramètre:art
    DataLigne2D data;
    List<ObjectData> listData = new ArrayList<>();
//    Nomenclature nom_ = findNomenclature(art);
    ObjectData valeur;
//        for (ComposantNomenclature cn : nom_.getListComposants()) {
//  {
//            data = new DataLigne2D(cn.getArticle().getRefArt());
//            valeur = new ObjectData(calculBesoin(o.getValue(), nom_.getQuantite(), cn.getQuantite()), "C_" + cn.getId() + "_" + nom_.getCompose().getId());
//            listData.add(valeur);
//        }
//        return listData;
//    }

    private double calculBesoin(double qteCompose, double qteComposeNorme, double qteComposant) {
        if (qteComposeNorme != 0) {
            BigDecimal bg = new BigDecimal(qteCompose * qteComposant / qteComposeNorme);
            return bg.setScale(2, BigDecimal.ROUND_UP).doubleValue();
        } else {
            return 0;
        }
    }

    private Nomenclature findNomenclature(int index, Articles art) {
        return listNomenclature.get(index);
    }

    @Override
    public boolean controleFiche(Mrp bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Mrp recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Mrp bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
