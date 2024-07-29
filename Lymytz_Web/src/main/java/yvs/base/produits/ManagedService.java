///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.base.produits;
//
//import yvs.entity.base.YvsBaseArticleDepot;
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
//import javax.faces.event.ValueChangeEvent;
//import org.primefaces.context.RequestContext;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.TabChangeEvent;
//import org.primefaces.event.UnselectEvent;
//import org.primefaces.model.chart.PieChartModel;
//import yvs.entity.produits.*;
//import yvs.entity.produits.group.*;
//import yvs.produits.groupe.PlanTarifGroup;
//import yvs.util.*;
//
///**
// *
// * @author GOUCHERE YVES
// */
//@SessionScoped
//@ManagedBean(name = "Mservice")
//public class ManagedService extends Managed<Service, YvsBaseCaisse> implements Serializable {
//
//    private Options[] categories;
//    private Options[] uniteMasses;
//    private Options[] modeConso;
//    private List<Service> listeService;
//    private List<Service> listFiltre;
//    private List<PlanTarifArticles> listTarif;
//    private List<BorneTranche> listBorneTranche;
//    private BorneTranche selectBorne;
//    private Service selectedService;
//    @ManagedProperty(value = "#{service}")
//    private Service service;
//    @ManagedProperty(value = "#{tarifArt}")
//    private PlanTarifArticles tarif;
//    private PlanTarifArticles selectTarifGroup;
//    private boolean tranche;
//    private boolean renderOnglet;
//    private String defaultButon = "A-groupP-save";
//    /*
//     * information sur les borne du plan tarifaire
//     */
//    private double borne;
//    private double remise;
//    private double prix;
//    private String modelTranche = "Valeurs"; //indique le model tarifaire(valeur ou quantité)
//    /**
//     * informations sur la liaison avec le dépot
//     */
//    @ManagedProperty(value = "#{artDepot}")
//    private ArticlesDepot groupeDepot;
//    private ArticlesDepot selectGroupeDepot;
//    private PieChartModel pieModel;
//    private List<String> listSource;
//    private List<String> listTarget;
//
//    /**
//     * Creates a new instance of ManagedGroupeProduit
//     */
//    public ManagedService() {
//        listeService = new ArrayList<>();
//        listTarif = new ArrayList<>();
//        listBorneTranche = new ArrayList<>();
//        categories = new Options[]{
//            new Options(Constantes.CAT_MARCHANDISE, "Marchandises(Négoce)"),
//            new Options(Constantes.CAT_PSF, "Produits semi fini"),
//            new Options(Constantes.CAT_PF, "Produits fini"),
//            new Options(Constantes.CAT_MP, "Matière première")
//        };
//        uniteMasses = new Options[]{
//            new Options(Constantes.KG, "Kilogramme"),
//            new Options(Constantes.GR, "Gramme"),
//            new Options(Constantes.MG, "Miligramme")
//        };
//        modeConso = new Options[]{
//            new Options(Constantes.MODE_CONSO_MASSIQUE, "Massique"),
//            new Options(Constantes.MODE_CONSO_UNITAIRE, "Unitaire")
//        };
//        pieModel = new PieChartModel();
//        listTarget = new ArrayList<>();
//        listSource = new ArrayList<>();
//    }
//
//    public List<Service> getListFiltre() {
//        return listFiltre;
//    }
//
//    public void setListFiltre(List<Service> listFiltre) {
//        this.listFiltre = listFiltre;
//    }
//
//    public List<Service> getListeService() {
//        return listeService;
//    }
//
//    public void setListeService(List<Service> listeService) {
//        this.listeService = listeService;
//    }
//
//    public List<PlanTarifArticles> getListTarif() {
//        return listTarif;
//    }
//
//    public void setListTarif(List<PlanTarifArticles> listTarif) {
//        this.listTarif = listTarif;
//    }
//
//    public Service getSelectedService() {
//        return selectedService;
//    }
//
//    public void setSelectedService(Service selectedService) {
//        this.selectedService = selectedService;
//    }
//
//    public Service getService() {
//        return service;
//    }
//
//    public void setService(Service service) {
//        this.service = service;
//    }
//
//    public void setUniteMasses(Options[] uniteMasses) {
//        this.uniteMasses = uniteMasses;
//    }
//
//    public Options[] getUniteMasses() {
//        return uniteMasses;
//    }
//
//    public void setModeConso(Options[] modeConso) {
//        this.modeConso = modeConso;
//    }
//
//    public Options[] getModeConso() {
//        return modeConso;
//    }
//
//    public void setCategories(Options[] categories) {
//        this.categories = categories;
//    }
//
//    public Options[] getCategories() {
//        return categories;
//    }
//
//    public boolean isTranche() {
//        return tranche;
//    }
//
//    public void setTranche(boolean tranche) {
//        this.tranche = tranche;
//    }
//
//    public List<BorneTranche> getListBorneTranche() {
//        return listBorneTranche;
//    }
//
//    public void setListBorneTranche(List<BorneTranche> listBorneTranche) {
//        this.listBorneTranche = listBorneTranche;
//    }
//
//    public PlanTarifArticles getTarif() {
//        return tarif;
//    }
//
//    public void setTarif(PlanTarifArticles tarif) {
//        this.tarif = tarif;
//    }
//
//    public double getBorne() {
//        return borne;
//    }
//
//    public void setBorne(double borne) {
//        this.borne = borne;
//    }
//
//    public double getRemise() {
//        return remise;
//    }
//
//    public void setRemise(double remise) {
//        this.remise = remise;
//    }
//
//    public String getModelTranche() {
//        return modelTranche;
//    }
//
//    public void setModelTranche(String modelTranche) {
//        this.modelTranche = modelTranche;
//    }
//
//    public boolean isRenderOnglet() {
//        return renderOnglet;
//    }
//
//    public void setRenderOnglet(boolean renderOnglet) {
//        this.renderOnglet = renderOnglet;
//    }
//
//    public PlanTarifArticles getSelectTarifGroup() {
//        return selectTarifGroup;
//    }
//
//    public void setSelectTarifGroup(PlanTarifArticles selectTarifGroup) {
//        this.selectTarifGroup = selectTarifGroup;
//    }
//
//    public ArticlesDepot getGroupeDepot() {
//        return groupeDepot;
//    }
//
//    public void setGroupeDepot(ArticlesDepot groupeDepot) {
//        this.groupeDepot = groupeDepot;
//    }
//
//    public ArticlesDepot getSelectGroupeDepot() {
//        return selectGroupeDepot;
//    }
//
//    public void setSelectGroupeDepot(ArticlesDepot selectGroupeDepot) {
//        this.selectGroupeDepot = selectGroupeDepot;
//    }
//
//    public String getDefaultButon() {
//        return defaultButon;
//    }
//
//    public void setDefaultButon(String defaultButon) {
//        this.defaultButon = defaultButon;
//    }
//
//    public PieChartModel getPieModel() {
//        return pieModel;
//    }
//
//    public void setPieModel(PieChartModel pieModel) {
//        this.pieModel = pieModel;
//    }
//
//    public BorneTranche getSelectBorne() {
//        return selectBorne;
//    }
//
//    public void setSelectBorne(BorneTranche selectBorne) {
//        this.selectBorne = selectBorne;
//    }
//
//    public double getPrix() {
//        return prix;
//    }
//
//    public void setPrix(double prix) {
//        this.prix = prix;
//    }
//
//    @Override
//    public boolean controleFiche(Service bean) {
//        if (bean.getRefService() == null) {
//            getMessage("Vous devez renseigner la Désignation", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        if (bean.getDesignation() == null) {
//            getMessage("Vous devez renseigner la Désignation", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        if (bean.getCategorie() == null) {
//            getMessage("Vous devez renseigner la catégorie de l'article", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean saveNew() {
//        Service ar = recopieView();
//        if (controleFiche(ar)) {
//            if (service.getRefGroupe() != null) {
//                dao.setEntityClass(YvsBaseGroupesArticle.class);
//                String[] ch = {"refgroupe"};
//                Object[] val = {service.getRefGroupe()};
//                groupe = (YvsBaseGroupesArticle) dao.getOne(ch, val);
//                YvsBaseArticles art = buildEntityFromBean(ar);
//                art.setService(true);
//                art = (YvsBaseArticles) dao.save1(art);
//                ar.setId(art.getId());
//                service.setId(ar.getId());
//                //sauvegarde le plan tarifaire du groupe
//                List<YvsBasePlanTarifaireArticle> listEntityPT = findPropertyPlanTarifGroupe();
//                List<PlanTarifArticles> listBeanPT = new ArrayList<>();
//                foYvsBsePlanTarifrif pt : listEntityPT) {
//                    PlanTarifArticles beanPt = buildTarif(pt);
//                    listBeanPT.add(beanPt);
//                }
//                ar.setListTarifArt(listBeanPT);
//                listeService.add(0, ar);
//                setRenderOnglet(true);
//                selectedService = ar;
//                update("S-groupP-table");
//                update("S-ref-groupP");
//                update("S-groupProd-main");
//                defaultButon = "S-groupP-update";
//                setDisableSave(true);
//                succes();
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void updateBean() {
//        Service ar = recopieView();
//        if (controleFiche(ar)) {
//            YvsBaseArticles a = buildEntityFromBean(ar);
//            ar.setLastAuteur(getUserOnLine());
//            ar.setLastDateUpdate(df.format(new Date()));
//            dao.update(a);
//            listeService.set(listeService.indexOf(ar), ar);
//            update("A-groupP-table");
//            succes();
//        }
//    }
//
//    @Override
//    public Service recopieView() {
//        Service ar = new Service();
//        ar.setId(service.getId());
//        ar.setActif(service.isActif());
//        ar.setRefGroupe(service.getRefGroupe());
//        ar.setAuteur(service.getAuteur());
//        ar.setLastAuteur(service.getLastAuteur());       
//        if (service.getLastDateUpdate() != null) {
//            ar.setLastDateUpdate(service.getLastDateUpdate());
//        } else {
//            ar.setLastDateUpdate(df.format(new Date()));
//        }
//        ar.setCategorie(service.getCategorie());
//        ar.setChangePrix(service.isChangePrix());
//        ar.setClasseStat(service.getClasseStat());
//        ar.setCodeBarre(service.getCodeBarre());
//        ar.setDescription(service.getDescription());
//        ar.setDesignation(service.getDesignation());
//        ar.setIdGroupe(service.getIdGroupe());
//        ar.setPua(service.getPua());
//        ar.setPuv(service.getPuv());
//        ar.setPuvMin(service.getPuvMin());
//        ar.setSuiviEnStock(service.isSuiviEnStock());
//        ar.setRemise(service.getRemise());
//        return ar;
//    }
//
//    @Override
//    public void populateView(Service bean) {
//        if (bean != null) {
//            service.setId(bean.getId());
//            service.setActif(bean.isActif());
//            service.setRefGroupe(bean.getRefGroupe());
//            service.setAuteur(bean.getAuteur());
//            service.setLastAuteur(bean.getLastAuteur());
//            if (bean.getLastDateUpdate() != null) {
//                service.setLastDateUpdate(bean.getLastDateUpdate());
//            } else {
//                service.setLastDateUpdate(df.format(new Date()));
//            }
//            service.setCategorie(bean.getCategorie());
//            service.setChangePrix(bean.isChangePrix());
//            service.setClasseStat(bean.getClasseStat());
//            service.setCodeBarre(bean.getCodeBarre());
//            service.setDescription(bean.getDescription());
//            service.setDesignation(bean.getDesignation());
//            service.setIdGroupe(bean.getIdGroupe());
//            service.setPua(bean.getPua());
//            service.setPuv(bean.getPuv());
//            service.setPuvMin(bean.getPuvMin());
//            service.setSuiviEnStock(bean.isSuiviEnStock());
//            service.setRemise(bean.getRemise());
//            service.setListTarifArt(bean.getListTarifArt());
//        }
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        Service ar = (Service) ev.getObject();
//        if (ar != null) {
//            populateView(ar);
//            setDisableSave(true);
//            setRenderOnglet(true);
//            update("A-vue-group");
////            defaultButon = "A-groupP-update";
//        }
//    }
//
//    @Override
//    public void loadAll() {
//        //charge les articles depuis  la base de donnée
//        listeService.clear();
//        String[] ch = {"societe"};
//        Object[] val = {currentScte};
//        List<YvsBaseArticles> la = dao.loadNameQueries("YvsArticles.findAll", ch, val);
//        for (YvsBaseArticles a : la) {
//            //récupère la liste des relations GroupeProduit-CategorieTarif contenu dans g
//            Service ar = buildBeanFromEntity(a);
//            listeService.add(0, ar);
//            createGraphe();
//        }
//    }
//
//    public void loadActif() {
//        //charge les articles depuis  la base de donnée
//        listeService.clear();
//        String[] ch = {};
//        Object[] val = {};
//        List<YvsBaseArticles> la = dao.loadNameQueries("YvsArticles.findByActif", ch, val);
//        for (YvsBaseArticles a : la) {
//            //récupère la liste des relations GroupeProduit-CategorieTarif contenu dans g
//            Service ar = new Service(a.getId());
//            ar.setRefService(a.getRefArt());
//            listeService.add(0, ar);
//        }
//    }
//
//    @Override
//    public void resetFiche() {
//        resetFiche(service);
//        setDisableSave(false);
//        setRenderOnglet(false);
//        update("A-vue-group");
//        update("A-groupP-table");
//        update("A-ref-groupP");
//        setDefaultButon("A-groupP-save");
//
//    }
//
//    private Service buildBeanFromEntity(YvsBaseArticles a) {
//        Service ar = new Service(a.getId());
//        ar.setActif(a.getActif());
//        ar.setRefGroupe(a.getGroupe().getRefgroupe());
////        ar.setCategorie(a.getCategorie());
////        ar.setChangePrix(a.isChangePrix());
//        ar.setClasseStat(a.getClassStat());
//        ar.setCodeBarre(a.getCodeBarre());
//        ar.setDescription(a.getDescription());
//        ar.setDesignation(a.getDesignation());
//        ar.setIdGroupe(a.getGroupe().getId());
//        ar.setPua(a.getPua());
//        ar.setPuv(a.getPuv());
//        ar.setPuvMin(a.getPrixMin());
////        ar.setSuiviEnStock(a.isSuiviEnStock());
//        ar.setRemise(a.getRemise());
//        //charge la liste des plans tarifaire s'il y en a
////        String[] ch = {"article"};
////        Object[] val = {a.getId()};
////        List<Object[]> r = dao.loadNameQueYvsBsePlanTarifnTarif.findByArticle", ch, val);
////        List<YvsPlanTarif> lpt = buildPlanTarif(r);
////        if (!lpt.isEmpty()) {
////            a.setYvsPlanTarifList(lpt);
////        }
////        if (a.getYvsPlanTarifList() != null) {
////            if (!a.getYvsPlanTarifList().isEmpty()) {
////                List<PlanTarifArticles> lp = new ArrayList<>();
////                ar.setListTarifArt(lp);
////            YvsBsePlanTarifPlanTarif pt : a.getYvsPlanTarifList()) {
////                    ar.getListTarifArt().add(buildTarif(pt));
////                }
////            }
////        }
//////        //charge les dépots s'il y en a
////        if (a.getYvsArticlesDepotList() != null) {
////            if (!a.getYvsArticlesDepotList().isEmpty()) {
////                List<ArticlesDepots> lgd = new ArrayList<>();
////                for (YvsBaseArticleDepot ad : a.getYvsArticlesDepotList()) {
//////                    ar.getListArtDepot().add(buildArticleDepot(ad));
////                }
////            }
////        }
//        return ar;
//
//    }   
//
//    private PlanTarifGroup buildTarif(YvsPlanTarifGroupe ta) {
//        PlanTarifGroup t = new PlanTarifGroup();
//        TrancheVal tranch = null;
//        //si le plan tarifaire est en tranche, on charge les bornes
//        if (ta.getIdTranche() != null) {
//            tranch = new TrancheVal();
//            tranch.setId(ta.getIdTranche().getId());
//            tranch.setModelTranche(ta.getIdTranche().getModelTranche());
//            //récupération des bornes
//            List<BorneTranche> lb = new ArrayList<>();
//            for (YvsBorneTranches b : ta.getIdTranche().getYvsBorneTranchesList()) {
//                lb.add(buildBorne(b));
//            }
//            tranch.setListBorne(lb);
//        }
//        t.setIdCategorie(ta.getYvsCatTarif().getId());
//        t.setRefAgence(ta.getYvsAgences().getCodeagence());
//        t.setIdAgence(ta.getYvsAgences().getId());
//        t.setIdTranche(tranch);
//        t.setPrix(ta.getPrix());
//        t.setRefCategorie(ta.getYvsCatTarif().getDesignation());
//        t.setRemise(ta.getRemise());
//        return t;
//    }
//
//    private BorneTranche buildBorne(YvsBorneTranches b) {
//        BorneTranche bo = new BorneTranche();
//        bo.setBorne(b.getBorne());
//        bo.setId(b.getId());
//        bo.setIdTranche(b.getTranche().getId());
//        bo.setPrix(b.getPrix());
//        bo.setRemise(b.getRemise());
//        return bo;
//    }
//
//    private YvsBaseArticles buildEntityFromBean(Service a) {
//        YvsBaseArticles art = new YvsBaseArticles(a.getId());
//        art.setActif(a.isActif());
//        art.setDescription(a.getDescription());
//        dao.setEntityClass(YvsBaseGroupesArticle.class);
//        String[] ch = {"refgroupe"};
//        Object[] val = {service.getRefGroupe()};
//        groupe = (YvsBaseGroupesArticle) dao.getOne(ch, val);
//        art.setGroupe(groupe);
////        art.setCategorie(a.getCategorie());
//        art.setChangePrix(a.isChangePrix());
//        art.setClassStat(a.getClasseStat());
//        art.setCodeBarre(a.getCodeBarre());
//        art.setDescription(a.getDescription());
//        art.setDesignation(a.getDesignation());
//        art.setPua(a.getPua());
//        art.setPuv(a.getPuv());
//        art.setPrixMin(a.getPuvMin());
//        art.setSuiviEnStock(a.isSuiviEnStock());
//        art.setRemise(a.getRemise());
//        return art;
//    }
//
//    public void disable() {
//    }
//
//    public void delete() {
//    }
//    /**
//     * *Tarif grouepe*
//     */
//    private int idBorne = 1;
//
//    public void openBorne(ValueChangeEvent ev) {
//        boolean b = (Boolean) ev.getNewValue();
//        if (b) {
//            tarif.setRemise(0);
//            borne = 1;
//            listBorneTranche.clear();
//            remise = service.getRemise();
//            prix = service.getPuv();
//            update("A-bo-tarif");
//            update("A-bo-grid");
//            RequestContext.getCurrentInstance().execute("tranche.show()");
//        }
//    }
//
//    public void addBorne() {
//        BorneTranche b = new BorneTranche();
//        b.setBorne(getBorne());
//        b.setRemise(getRemise());
//        b.setId(idBorne++);
//        b.setPrix(getPrix());
//        listBorneTranche.add(0, b);
//        update("A-bo-tarif");
//    }
//    PlanTarifArticles planTarif = new PlanTarifArtiYvsBsePlanTarifYvsPlanTarif yvsPYvsBsePlanTarifew YvsPlanTarif();
//    PlanTarifArticles toUpdate;
//
//    public void addTarifGroupe() {
////        Articles art = listArticle.get(listArticle.indexOf(new Articles(article.getId())));              
////        String refCatt = art.getListTarifArt().get(art.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie(), tarif.getRefAgence()))).getRefCategorie();
////        long idCatt = art.getListTarifArt().get(art.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie(), tarif.getRefAgence()))).getIdCategorie();
////        long idAgence = art.getListTarifArt().get(art.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie(), tarif.getRefAgence()))).getIdAgence();
////        String refAgence = art.getListTarifArt().get(art.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie(), tarif.getRefAgence()))).getRefAgence();
////        System.out.println("Ref Cat " + refCatt + " IdCat " + idCatt + " Id Agence " + idAgence + " Ref Agence " + refAgence);
//        if (tarif.getRefCategorie() != null) {
//            String[] ch = {"designation"};
//            Object[] val = {tarif.getRefCategorie()};
//            planTarif = new PlanTarifArticles();
//            yYvsBsePlanTarif= new YvsPlanTarif();
//            dao.setEntityClass(YvsCatTarif.class);
//            YvsCatTarif catT = (YvsCatTarif) dao.getOne(ch, val);
//            //charge l'agence           
//            yvsPlanTarif.setYvsPlanTarifPK(new YvsPlanTarifPK(catT.getId(), service.getId()));
//            planTarif.setRefCategorie(catT.getDesignation());
//            planTarif.setIdCategorie(catT.getId());
//            planTarif.setRemise(tarif.getRemise());
//            planTarif.setPrix(tarif.getPrix());
//            if (service.getListTarifArt() == null) {
//                service.setListTarifArt(listTarif);
//            }
//            if (!service.getListTarifArt().contains(planTarif)) {
//                if (tranche && !listBorneTranche.isEmpty()) {
//                    TrancheVal tr = new TrancheVal(0, modelTranche);
//                    //je persiste la tranche
//                    YvsTranches tranch = buildTranche();
//                    tranch = (YvsTranches) dao.save1(tranch);
//                    tr.setId(tranch.getId());
//                    //parcour la liste des borne
//                    int pos = 0;
//                    for (BorneTranche b : listBorneTranche) {
//                        YvsBorneTranches bt = buildBorne(b);
//                        bt.setId(null);
//                        bt.setTranche(tranch);
//                        bt = (YvsBorneTranches) dao.save1(bt);
//                        b.setIdTranche(bt.getId());
//                        b.setId(bt.getId());
//                        listBorneTranche.set(pos, b);
//                    }
//                    List<BorneTranche> l = new ArrayList<>();
//                    l.addAll(listBorneTranche);
//                    tr.setListBorne(l);
//
//                    planTarif.setIdTranche(tr);
//                    yvsPlanTarif.setIdTranche(tranch);
//                } else if (tranche && listBorneTranche.isEmpty()) {
//                    getMessage("Vous avez indiqué que ce plan était repartie en tranche, cependant vous n'avez mis à jour aucune tranches", FacesMessage.SEVERITY_WARN);
//                    return;
//                } else {
//                    yvsPlanTarif.setIdTranche(null);
//                    planTarif.setIdTranche(null);
//                }
//                yvsPlanTarif.setPrix(planTarif.getPrix());
//                yvsPlanTarif.setRemise(planTarif.getRemise());
//                dao.save(yvsPlanTarif);
//                service.getListTarifArt().add(0, planTarif);
//                if (selectedService.getListTarifArt() == null) {
//                    List<PlanTarifArticles> l = new ArrayList<>();
//                    selectedService.setListTarifArt(l);
//                }
////            selectArticle.getListTarifArt().add(planTarif);
////                listArticle.set(listArticle.indexOf(selectArticle), selectArticle);
//                listBorneTranche.clear();
//                borne = 1;
//                remise = 1;
//                tarif.setRemise(1);
//                tranche = false;
//                update("A-ta-tarif");
//                update("A-bo-tarif");
//            } else {
//                //modification du tarif-groupe
//                //on vérifie si la ligne tarif-categorie était lié à une tranche.
//                int index = service.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie()));
//                toUpdate = service.getListTarifArt().get(index);
//                if (toUpdate.getIdTranche() != null && !tranche) {
//                    //s'il y avait une tranche et qu'il y en a plus, on informe l'utilisateur                    
//                    getMessage("annullation de tranches", FacesMessage.SEVERITY_ERROR);
//                    openDialog("delTranche");
//                } else {
//                    //si la forma du plan tarifaire est inchangé,    
//                    if (toUpdate.getIdTranche() != null) {
//                        //on modifie les tranches s'il y en a   
//                        List<BorneTranche> lb = toUpdate.getIdTranche().getListBorne();
//                        int pos = 0;
//                        for (BorneTranche b : listBorneTranche) {
//                            YvsBorneTranches bb = buildBorne(b);
//                            bb.setTranche(new YvsTranches(toUpdate.getIdTranche().getId()));
//                            bb = (YvsBorneTranches) dao.update(bb);
//                            long id = b.getId();
//                            b.setId(bb.getId());
//                            //modifie sur la vue
//                            listBorneTranche.set(pos, b);
//                            //modifie l'objet toUpdate
//                            if (toUpdate.getIdTranche().getListBorne().contains(new BorneTranche(id))) {
//                                toUpdate.getIdTranche().getListBorne().set(toUpdate.getIdTranche().getListBorne().indexOf(new BorneTranche(id)), b);
//                            } else {
//                                toUpdate.getIdTranche().getListBorne().add(b);
//                            }
//                            pos++;
//                            if (lb.contains(b));
//                            lb.remove(b);
//                        }
//                        supprimeBorne(lb);
//                        service.getListTarifArt().set(service.getListTarifArt().indexOf(toUpdate), toUpdate);
////                        article.getListTarifArt().get(art.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie(), tarif.getRefAgence()))).getIdTranche().setListBorne(listBorneTranche);
//                    } else {
//                        //s'il y a des tranches
//                        if (tranche && !listBorneTranche.isEmpty()) {
//                            TrancheVal tr = new TrancheVal(0, modelTranche);
//                            YvsTranches tranch = buildTranche();
//                            tranch = (YvsTranches) dao.save1(tranch);
//                            tr.setId(tranch.getId());
//                            //parcour la liste des borne
//                            int pos = 0;
//                            for (BorneTranche b : listBorneTranche) {
//                                YvsBorneTranches bt = buildBorne(b);
//                                bt.setId(null);
//                                bt.setTranche(tranch);
//                                bt = (YvsBorneTranches) dao.save1(bt);
//                                b.setIdTranche(bt.getId());
//                                b.setId(bt.getId());
//                                listBorneTranche.set(pos, b);
//                            }
//                            List<BorneTranche> l = new ArrayList<>();
//                            l.addAll(listBorneTranche);
//                            tr.setListBorne(l);
//                            toUpdate.setIdTranche(tr);
//      YvsBasePlanTariff       YvsBsePlanTarifif pt = new YvsPlanTarif(new YvsPlanTarifPK(toUpdate.getIdCategorie(), service.getId()));
//                            pt.setIdTranche(tranch);
//                            pt.setPrix(0);
//                            pt.setRemise(0);
//                            dao.update(pt);
//                            service.getListTarifArt().set(service.getListTarifArt().indexOf(toUpdate), toUpdate);
//                            toUpdate = null;
//                        } else {
//                            //s'il n'y a pas de tranche,                             
//                            toUpdate.setRemise(tarif.getRemise());
//                            toUpdate.setPrix(tarif.getPrix());YvsBasePlanTarifif       YvsBsePlanTariflanTarif pt = new YvsPlanTarif(toUpdate.getIdCategorie(), service.getId());
//                            pt.setRemise(tarif.getRemise());
//                            pt.setPrix(tarif.getPrix());
//                            dao.update(pt);
//                            service.getListTarifArt().set(service.getListTarifArt().indexOf(toUpdate), toUpdate);
//                        }
//                    }
//                    succes();
//                    toUpdate = null;
//                }
//            }
//        } else {
//            getMessage(message.getMessage("choose_catTarif"), FacesMessage.SEVERITY_ERROR);
//        }
//    }
//
//    private void supprimeBorne(List<BorneTranche> l) {
//        for (BorneTranche bb : l) {
//            YvsBorneTranches b = new YvsBorneTranches(bb.getId());
//            dao.delete(b);
//        }
//    }
////support à la modification du plan tarifaire
//
//    public void updatePt() {
//        if (toUpdate.getIdTranche() != null) {
//            //supprime les tranches
//            for (BorneTranche b : toUpdate.getIdTranche().getListBorne()) {
//                YvsBorneTranches bb = buildBorne(b);
//                dao.delete(bb);
//            }
//            toUpdate.setRemise(tarif.getRemise());
//            toUpdate.setPrYvsBsePlanTarifPrixYvsBasePlanTarifrif  YvsPlanTarif pt = new YvsPlanTarif(toUpdate.getIdCategorie(), service.getId());
//            pt.setRemise(tarif.getRemise());
//            pt.setPrix(tarif.getPrix());
//            pt.setIdTranche(null);
//            dao.update(pt);
//            dao.delete(new YvsTranches(toUpdate.getIdTranche().getId()));
//            toUpdate.setIdTranche(null);
//            service.getListTarifArt().set(service.getListTarifArt().indexOf(toUpdate), toUpdate);
//            update("A-vue-group:A-ta-tarif");
//            update(" A-vue-group:A-choix_tranche");
//            toUpdate = null;
//        }
//    }
//
//    private YvsBorneTranches buildBorne(BorneTranche b) {
//        YvsBorneTranches bb = new YvsBorneTranches(b.getId());
//        bb.setBorne((float) b.getBorne());
//        bb.setPrix(b.getPrix());
//        bb.setRemise(b.getRemise());
//        bb.setTranche(new YvsTranches((int) b.getIdTranche()));
//        return bb;
//    }
//
//    private YvsTranches buildTranche() {
//        YvsTranches tranch = new YvsTranches();
//        tranch.setModelTranche(modelTranche);
////        tranch.setAuthor(getUserOnLine());   
//        return tranch;
//    }
//
//    /**
//     *
//     */
//    public void selectTarif(SelectEvent ev) {
//        PlanTarifArticles t = (PlanTarifArticles) ev.getObject();
//        if (t != null) {
//            tarif.setRefCategorie(t.getRefCategorie());
//            tarif.setIdCategorie(t.getIdCategorie());
//            listBorneTranche.clear();
//            if (t.getIdTranche() != null) {
//                tranche = true;
//                tarif.setRemise(0);
//                listBorneTranche.addAll(t.getIdTranche().getListBorne());
//                RequestContext.getCurrentInstance().execute("tranche.show()");
//            } else {
//                tranche = false;
//                tarif.setRemise(t.getRemise());
//                tarif.setPrix(t.getPrix());
//            }
//            Collections.sort(listBorneTranche, new BorneTranche());
//            update("A-bo-tarif");
//            update("A-vue-group:A-ch_catT");
//            update("A-tab_tarif");
//        }
//    }
//
//    public void changeVal(ValueChangeEvent ev) {
//        if (ev.getOldValue() != ev.getNewValue()) {
//            if (tranche) {
//                tranche = false;
//            } else {
//                tarif.setRemise(1);
//            }
//        }
//    }
//
//    /**
//     * Supprimer les tranches
//     */
//    pYvsBsePlanTarifeletTariYvsBsePlanTarif       YvsPlanTarif tar = new YvsPlanTarif();
//        tar.setYvsPlanTarifPK(new YvsPlanTarifPK(selectTarifGroup.getIdCategorie(), service.getId()));
//        dao.delete(tar);
//        //si le tarif est lié à une tranche, on la supprime aussi
//        if (selectTarifGroup.getIdTranche() != null) {
//            YvsTranches t = new YvsTranches(selectTarifGroup.getIdTranche().getId());
//            dao.delete(t);
//        }
//        PlanTarifArticles t = new PlanTarifArticles();
//        t.setRefCategorie(selectTarifGroup.getRefCategorie());
//        selectedService.getListTarifArt().remove(selectTarifGroup);
//        service.getListTarifArt().remove(selectTarifGroup);
//        update("A-groupP-table");
//        update("A-ta-tarif");
//    }
////supprime une borne
//
//    public void deleteBorne() {
//        try {
////            YvsBorneTranches b = new YvsBorneTranches(selectBorne.getId());
////            dao.delete(b);
//            listBorneTranche.remove(selectBorne);
//            update("A-bo-tarif");
//        } catch (Exception ex) {
//            listBorneTranche.remove(selectBorne);
//            update("A-bo-tarif");
//        }
//    }
//    /**
//     * stockage des groupes de produits*
//     */
//    private Options[] modesAppro = new Options[]{
//        new Options(Constantes.APPRO_ACHT_PROD_EN, "Achat + Productions+ Entrées"),
//        new Options(Constantes.APPRO_ACHTON, "Achat seulement"),
//        new Options(Constantes.APPRO_ENON, "Entré seulement"),
//        new Options(Constantes.APPRO_PRODON, "Production seulement"),
//        new Options(Constantes.APPRO_ACHT_EN, "Achat + Entrée"),
//        new Options(Constantes.APPRO_ACHT_PROD, "Achat + Production"),
//        new Options(Constantes.APPRO_PROD_EN, "Production + Entrée")
//    };
//    private Options[] modesReappro = new Options[]{
//        new Options(Constantes.REAPPRO_SEUIL, Constantes.REAPPRO_SEUIL),
//        new Options(Constantes.REAPPRO_PERIODE, Constantes.REAPPRO_PERIODE)
//    };
//    private boolean renderNj;
//
//    public Options[] getModesAppro() {
//        return modesAppro;
//    }
//
//    public void setModesAppro(Options[] modesAppro) {
//        this.modesAppro = modesAppro;
//    }
//
//    public boolean isRenderNj() {
//        return renderNj;
//    }
//
//    public void setRenderNj(boolean renderNj) {
//        this.renderNj = renderNj;
//    }
//
//    public Options[] getModesReappro() {
//        return modesReappro;
//    }
//
//    public void setModesReappro(Options[] modesReappro) {
//        this.modesReappro = modesReappro;
//    }
//
//    public void choixModeReappro(ValueChangeEvent ev) {
//        String str = (String) ev.getNewValue();
//        if (str.equals(Constantes.REAPPRO_PERIODE)) {
//            renderNj = true;
//        } else {
//            renderNj = false;
//        }
//    }
//
//    //récupérations des propriété du groupe pour les attribuer par défaut au produit
//    /**
//     * 1. les infos tarifaire 2.les infos comptables 3. les infos de stockage
//     *
//     */
//    private YvsBaseGrYvsBsePlanTarif groupe = null;
//
//    public List<YvsPlanTariYvsBsePlanTarifrtyPlanTarifGroupe() {
//        List<YvsPlanTarif> lpt = null;
//        if (groupe != null) {
//            lpt = new ArrayList<>();
//            //information tarifaire
//            for (YvsPlanTarifGroupe ptg : groYvsBsePlanTarifanTarifYvsBsePlanTarif) {
//                YvsPlanTarif pt = new YvsPlanTarif(new YvsPlanTarifPK(ptg.getYvsCatTarif().getId(), service.getId()));
//                pt.setPrix(service.getPuv());
//                pt.setRemise(ptg.getRemise());
//                pt.setYvsCatTarif(ptg.getYvsCatTarif());
//                //s'il y a des tranche
//                if (ptg.getIdTranche() != null) {
//                    YvsTranches tranch = new YvsTranches(ptg.getIdTranche());
//                    tranch.setId(null);
//                    tranch = (YvsTranches) dao.save1(tranch);
//                    if (!ptg.getIdTranche().getYvsBorneTranchesList().isEmpty()) {
//                        for (YvsBorneTranches b : ptg.getIdTranche().getYvsBorneTranchesList()) {
//                            YvsBorneTranches bo = new YvsBorneTranches(b);
//                            bo.setPrix(service.getPuv());
//                            bo.setTranche(tranch);
//                            dao.save(bo);
//                        }
//                    }
//                    pt.setIdTYvsBsePlanTarifh);
//                }
//
//                pt = (YvsPlanTarif) dao.save1(pt);
//                lpt.add(pt);
//            }
//        }
//        return lpt;
//    }
//
//    //converti un plan tarifaire persistant en plaYvsBsePlanTarifn bean
//    private PlanTarifArticles buildTarif(YvsPlanTarif ta) {
//        PlanTarifArticles t = new PlanTarifArticles();
//        TrancheVal tranch = null;
//        //si le plan tarifaire est en tranche, on charge les bornes
//        if (ta.getIdTranche() != null) {
//            tranch = new TrancheVal();
//            tranch.setId(ta.getIdTranche().getId());
//            tranch.setModelTranche(ta.getIdTranche().getModelTranche());
//            //récupération des bornes
//            String[] ch = {"tranche"};
//            Object[] val = {ta.getIdTranche()};
//            List<Object[]> r = dao.loadNameQueries("YvsBorneTranches.findByTranche", ch, val);
//            List<YvsBorneTranches> l = buildBorne(r);
//            ta.getIdTranche().setYvsBorneTranchesList(l);
//            List<BorneTranche> lb = new ArrayList<>();
//            for (YvsBorneTranches b : ta.getIdTranche().getYvsBorneTranchesList()) {
//                lb.add(buildBorne(b));
//            }
//            tranch.setListBorne(lb);
//        }
//        t.setIdCategorie(ta.getYvsCatTarif().getId());
//        t.setIdTranche(tranch);
//        t.setPrix(ta.getPrix());
//        t.setRefCategorie(ta.getYvsCatTarif().getDesignation());
//        t.setRemise(ta.getRemise());
//        return t;
//    }
//
//    public void changeDefaultButon(TabChangeEvent ev) {
//        String id = ev.getTab().getId();
//        switch (id) {
//            case "A-tab_gen":
//                setDefaultButon("A-groupP-update");
//                break;
//            case "A-tab_tarif":
//                setDefaultButon("A-vue-group:A-buton-addTarif");
//                break;
//            case "A-tab_stock":
//                setDefaultButon("A-vue-group:A-badd-stoc");
//                break;
//            default:
//                setDefaultButon("A-groupP-save");
//                break;
//        }
//    }
//
//    private void createGraphe() {
//        if (!listeService.isEmpty()) {
//            Collections.sort(listeService, new Service());
//            String art = listeService.get(0).getRefGroupe();
//            int count = 0;
//            int nbre = 1;
//            for (Service ag : listeService) {
//                if (ag.getRefGroupe().equals(art)) {
//                    count++;
//                } else {
//                    pieModel.set(art, count);
//                    count = 1;
//                    art = ag.getRefGroupe();
//                }
//                if (nbre == listeService.size()) {
//                    pieModel.set(art, count);
//                }
//                nbre++;
//            }
//        }
//    }
//
//    private List<YvsBorneTranches> buildBorne(List<Object[]> l) {
//        List<YvsBorneTranches> result = new ArrayList<>();
//        for (Object[] tuple : l) {
//            YvsBorneTranches b = new YvsBorneTranches((Long) tuple[0]);
//            b.setBorne((double) tuple[1]);
//            b.setRemise((double) tuple[2]);
//            b.setPrix((double) tuple[3]);
//            b.setTranche(new YvsTranches((Integer) tuple[4]));
//            result.add(b);
//        }
//        return result;
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
