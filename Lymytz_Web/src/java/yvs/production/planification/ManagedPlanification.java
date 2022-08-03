package yvs.production.planification;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.produits.production;
//
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.ViewScoped;
//import org.primefaces.event.CellEditEvent;
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.UnselectEvent;
//import yvs.entity.production.base.YvsBaseArticles;
//import yvs.entity.production.base.YvsProdGammeArticle;
//import yvs.entity.production.planification.YvsProdDetailPdp;
//import yvs.entity.production.planification.YvsProdPeriodePlan;
//import yvs.entity.production.planification.YvsProdPlanification;
//import yvs.prod.FamilleArticle;
//import yvs.prod.UtilProd;
//import yvs.produits.base.PhaseGamme;
//import yvs.produits.base.PosteCharge;
//import yvs.produits.base.PostePhase;
//import yvs.produits.base.GammeArticle;
//import yvs.util.Constantes;
//import yvs.util.Managed;
//import yvs.util.PaginatorResult;
//import yvs.util.Util;
//
///**
// *
// * @author lymytz
// */
//@ManagedBean
//@ViewScoped
//public final class ManagedPlanification extends Managed<Planification, YvsBaseCaisse> implements Serializable {
//
//    @ManagedProperty(value = "#{planificationPDP}")
//    private PlanificationPDP planificationPDP;
//    @ManagedProperty(value = "#{planificationPIC}")
//    private PlanificationPIC planificationPIC;
//    private List<Planification> planifications;
//    private List<FamilleArticle> famillesArt;
//    private List<Articles> articles, articlesView;
//    private List<PeriodePlanificationPDP> periodesPDP, periodesPDC;
//    private List<String> sitePIC, sitePDP;
//    private List<List<List<DetailPlanPDP>>> dataPDP, dataPDC;
//    private DetailPlanPDP valeurSelect = new DetailPlanPDP();
//    private DetailPlanPDP detail = new DetailPlanPDP();
//    private int[] viewRowSubTable = new int[1];
//    private String ajust, tabId_PDP, typeMeth, idx_valeur_select, onglet_tab = "pic";
//    private List<PosteCharge> postes;
//    private List<PhaseGamme> phases;
//    private List<GammeArticle> gammes;
//    private Articles articleSelectPDC = new Articles();
//    private PeriodePlanificationPDP periodeSelectPDC = new PeriodePlanificationPDP(), periode = new PeriodePlanificationPDP();
//    private DetailPlanPDP detailSelectPDC = new DetailPlanPDP();
//    private boolean init, updatePlanPDP, updatePeriodeDPDP, updateDetailDPD;
//    Random alea = new Random();
//    YvsProdPlanification entityPDP;
//
//    public ManagedPlanification() {
//        gammes = new ArrayList<>();
//        planifications = new ArrayList<>();
//        postes = new ArrayList<>();
//        phases = new ArrayList<>();
//        articlesView = new ArrayList<>();
//        articles = new ArrayList<>();
//        famillesArt = new ArrayList<>();
//        periodesPDP = new ArrayList<>();
//        periodesPDC = new ArrayList<>();
//        sitePIC = new ArrayList<>();
//        sitePDP = new ArrayList<>();
//        dataPDP = new ArrayList<>();
//        dataPDC = new ArrayList<>();
//        fullAll();
//    }
//
//    public PlanificationPIC getPlanificationPIC() {
//        return planificationPIC;
//    }
//
//    public void setPlanificationPIC(PlanificationPIC planificationPIC) {
//        this.planificationPIC = planificationPIC;
//    }
//
//    public String getOnglet_tab() {
//        return onglet_tab;
//    }
//
//    public void setOnglet_tab(String onglet_tab) {
//        this.onglet_tab = onglet_tab;
//    }
//
//    public List<GammeArticle> getGammes() {
//        return gammes;
//    }
//
//    public void setGammes(List<GammeArticle> gammes) {
//        this.gammes = gammes;
//    }
//
//    public String getIdx_valeur_select() {
//        return idx_valeur_select;
//    }
//
//    public void setIdx_valeur_select(String idx_valeur_select) {
//        this.idx_valeur_select = idx_valeur_select;
//    }
//
//    public boolean isUpdateDetailDPD() {
//        return updateDetailDPD;
//    }
//
//    public void setUpdateDetailDPD(boolean updateDetailDPD) {
//        this.updateDetailDPD = updateDetailDPD;
//    }
//
//    public DetailPlanPDP getDetail() {
//        return detail;
//    }
//
//    public void setDetail(DetailPlanPDP detail) {
//        this.detail = detail;
//    }
//
//    public boolean isUpdatePeriodeDPDP() {
//        return updatePeriodeDPDP;
//    }
//
//    public void setUpdatePeriodeDPDP(boolean updatePeriodeDPDP) {
//        this.updatePeriodeDPDP = updatePeriodeDPDP;
//    }
//
//    public PeriodePlanificationPDP getPeriode() {
//        return periode;
//    }
//
//    public void setPeriode(PeriodePlanificationPDP periode) {
//        this.periode = periode;
//    }
//
//    public List<Planification> getPlanifications() {
//        return planifications;
//    }
//
//    public void setPlanifications(List<Planification> planifications) {
//        this.planifications = planifications;
//    }
//
//    public boolean isUpdatePlanPDP() {
//        return updatePlanPDP;
//    }
//
//    public void setUpdatePlanPDP(boolean updatePlanPDP) {
//        this.updatePlanPDP = updatePlanPDP;
//    }
//
//    public boolean isInit() {
//        return init;
//    }
//
//    public void setInit(boolean init) {
//        this.init = init;
//    }
//
//    public String getTabId_PDP() {
//        return tabId_PDP;
//    }
//
//    public void setTabId_PDP(String tabId_PDP) {
//        this.tabId_PDP = tabId_PDP;
//    }
//
//    public int[] getViewRowSubTable() {
//        return viewRowSubTable;
//    }
//
//    public void setViewRowSubTable(int[] viewRowSubTable) {
//        this.viewRowSubTable = viewRowSubTable;
//    }
//
//    public List<Articles> getArticlesView() {
//        return articlesView;
//    }
//
//    public void setArticlesView(List<Articles> articlesView) {
//        this.articlesView = articlesView;
//    }
//
//    public DetailPlanPDP getDetailSelectPDC() {
//        return detailSelectPDC;
//    }
//
//    public void setDetailSelectPDC(DetailPlanPDP detailSelectPDC) {
//        this.detailSelectPDC = detailSelectPDC;
//    }
//
//    public PeriodePlanificationPDP getPeriodeSelectPDC() {
//        return periodeSelectPDC;
//    }
//
//    public void setPeriodeSelectPDC(PeriodePlanificationPDP periodeSelectPDC) {
//        this.periodeSelectPDC = periodeSelectPDC;
//    }
//
//    public Articles getArticleSelectPDC() {
//        return articleSelectPDC;
//    }
//
//    public void setArticleSelectPDC(Articles articleSelectPDC) {
//        this.articleSelectPDC = articleSelectPDC;
//    }
//
//    public List<PeriodePlanificationPDP> getPeriodesPDC() {
//        return periodesPDC;
//    }
//
//    public void setPeriodesPDC(List<PeriodePlanificationPDP> periodesPDC) {
//        this.periodesPDC = periodesPDC;
//    }
//
//    public List<List<List<DetailPlanPDP>>> getDataPDC() {
//        return dataPDC;
//    }
//
//    public void setDataPDC(List<List<List<DetailPlanPDP>>> dataPDC) {
//        this.dataPDC = dataPDC;
//    }
//
//    public List<PhaseGamme> getPhases() {
//        return phases;
//    }
//
//    public void setPhases(List<PhaseGamme> phases) {
//        this.phases = phases;
//    }
//
//    public List<PosteCharge> getPostes() {
//        return postes;
//    }
//
//    public void setPostes(List<PosteCharge> postes) {
//        this.postes = postes;
//    }
//
//    public String getTypeMeth() {
//        return typeMeth;
//    }
//
//    public void setTypeMeth(String typeMeth) {
//        this.typeMeth = typeMeth;
//    }
//
//    public String getAjust() {
//        return ajust;
//    }
//
//    public void setAjust(String ajust) {
//        this.ajust = ajust;
//    }
//
//    public List<List<List<DetailPlanPDP>>> getDataPDP() {
//        return dataPDP;
//    }
//
//    public void setDataPDP(List<List<List<DetailPlanPDP>>> dataPDP) {
//        this.dataPDP = dataPDP;
//    }
//
//    public List<String> getSitePDP() {
//        return sitePDP;
//    }
//
//    public void setSitePDP(List<String> sitePDP) {
//        this.sitePDP = sitePDP;
//    }
//
//    public List<PeriodePlanificationPDP> getPeriodesPDP() {
//        return periodesPDP;
//    }
//
//    public void setPeriodesPDP(List<PeriodePlanificationPDP> periodesPDP) {
//        this.periodesPDP = periodesPDP;
//    }
//
//    public List<Articles> getArticles() {
//        return articles;
//    }
//
//    public void setArticles(List<Articles> articles) {
//        this.articles = articles;
//    }
//
//    public void setFamillesArt(List<FamilleArticle> famillesArt) {
//        this.famillesArt = famillesArt;
//    }
//
//    public List<FamilleArticle> getFamillesArt() {
//        return famillesArt;
//    }
//
//    public DetailPlanPDP getValeurSelect() {
//        return valeurSelect;
//    }
//
//    public void setValeurSelect(DetailPlanPDP valeurSelect) {
//        this.valeurSelect = valeurSelect;
//    }
//
//    public List<String> getSitePIC() {
//        return sitePIC;
//    }
//
//    public void setSitePIC(List<String> sitePIC) {
//        this.sitePIC = sitePIC;
//    }
//
//    public void setPlanificationPDP(PlanificationPDP planificationPDP) {
//        this.planificationPDP = planificationPDP;
//    }
//
//    public PlanificationPDP getPlanificationPDP() {
//        return planificationPDP;
//    }
//
//    public void initialiser() {
//        init = true;
//    }
//
//    public void fullAll() {
//        fullSitePDP();
//        //fullDetailPIC();
//        //fullDetailPDP();
//        //fullDetailPDC_();
//    }
//
//    @Override
//    public void loadAll() {
//        loadAllPlanification();
//    }
//
//    public void loadAllPlanification() {
//        planifications.clear();
//        List<YvsProdPlanification> l = dao.loadNameQueries("YvsProdPlanification.findAll", new String[]{}, new Object[]{});
//        planifications = UtilProd.buildBeanListPlanification(l);
//    }
//
//    public void loadAllPDP() {
//        loadAllArticle();
////        update("data_pdp_article");
//    }
//
//    public void loadAllArticle() {
//        articles.clear();
//        List<YvsBaseArticles> l;
//        if (currentScte != null) {
//            l = dao.loadNameQueries("YvsBaseArticles.findAll", new String[]{"societe"}, new Object[]{currentScte});
//        } else {
//            l = dao.loadNameQueries("YvsBaseArticles.find", new String[]{}, new Object[]{});
//        }
//        articles = UtilProd.buildBeanListArticleProduction(l);
//    }
//
//    public void loadAllArticle(boolean avancer, boolean init) {
//        articles.clear();
//        nameQueriCount = "YvsBaseArticles.findCountAll";
//        nameQueri = "YvsBaseArticles.findAll";
//        champ = new String[]{"societe"};
//        val = new Object[]{currentScte};
//        articles = UtilProd.buildBeanListArticleProduction(loadArticle(avancer, init));
//    }
//
//    private List<YvsBaseArticles> loadArticle(boolean avancer, boolean init) {
//        if (!init) {
//            if (avancer) {
//                setFirtResult(getFirtResult() + getNbMax());
//            } else {
//                setFirtResult(getFirtResult() - getNbMax());
//            }
//            if (getTotalPage() == getCurrentPage()) {
//                setFirtResult(0);
//            }
//        } else {
//            setFirtResult(0);
//        }
//        PaginatorResult<YvsBaseArticles> pa = new PaginatorResult<>();
//        pa = pa.loadResult(nameQueriCount, nameQueri, champ, val, getFirtResult(), getNbMax(), dao);
//        setDisNext(pa.isDisNext());
//        setDisPrev(pa.isDisPrev());
//        setTotalPage(pa.getNbPage());
//        setCurrentPage(pa.getCurrentPage());
//        return pa.getResult();
//    }
//
//    public int getIndexOfSite(String site, List<String> sites) {
//        int i = 0;
//        while (i < sites.size()) {
//            if (sites.get(i).equals(site)) {
//                break;
//            }
//            i++;
//        }
//        return i;
//    }
//
//    public YvsProdPlanification buildPlanification(Planification y) {
//        YvsProdPlanification p = new YvsProdPlanification();
//        if (y != null) {
//            p.setId(y.getId());
//            p.setAmplitude((int) y.getAmplitude());
//            p.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
//            p.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
//            p.setHorizon(y.getHorizon());
//            p.setPeriode(y.getPeriode());
//            p.setPeriodicite(y.getPeriodicite());
//            p.setReference(y.getReference());
//            p.setTypePlan(y.getType());
//            if (currentScte != null) {
//                p.setSociete(currentScte);
//            }
//        }
//        return p;
//    }
//
//    @Override
//    public boolean controleFiche(Planification bean) {
//        if (bean.getReference() == null || bean.getReference().equals("")) {
//            getErrorMessage("Vous devez entrer la reference");
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public Planification recopieView() {
//        Planification p = new Planification();
//        p.setId(planificationPDP.getId());
//        p.setAmplitude(planificationPDP.getAmplitude());
//        p.setDateDebut(planificationPDP.getDateDebut());
//        p.setDateFin(planificationPDP.getDateFin());
//        p.setHorizon(planificationPDP.getHorizon());
//        p.setPeriode(planificationPDP.getPeriode());
//        p.setReference(planificationPDP.getReference());
//        p.setType(planificationPDP.getType());
//        return p;
//    }
//
//    @Override
//    public void resetFiche() {
//        resetFiche(planificationPDP);
//        planificationPDP.setPeriodes(new ArrayList<PeriodePlanificationPDP>());
//        setUpdatePlanPDP(false);
//        resetPage();
//    }
//
//    @Override
//    public void resetPage() {
//        for (Planification p : planifications) {
//            p.setSelectActif(false);
//        }
//    }
//
//    @Override
//    public void deleteBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void updateBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void populateView(Planification bean) {
//        cloneObject(planificationPDP, bean);
//        setUpdatePlanPDP(true);
//    }
//
//    @Override
//    public boolean saveNew() {
//        switch (onglet_tab) {
//            case "pic":
//                break;
//            case "pdp":
//                if (saveNewPlanPDP()) {
//                    saveNewPDP();
//                    return true;
//                }
//                break;
//            default:
//                break;
//        }
//        return false;
//    }
//
//    public boolean saveNewPlanPDP() {
//        String action = isUpdatePlanPDP() ? "Modification" : "Insertion";
//        try {
//            planificationPDP.setType("PDP");
//            Planification bean = recopieView();
//            if (controleFiche(bean)) {
//                entityPDP = buildPlanification(bean);
//                if (!isUpdatePlanPDP()) {
//                    entityPDP = (YvsProdPlanification) dao.save1(entityPDP);
//                    bean.setId(entityPDP.getId());
//                    planificationPDP.setId(entityPDP.getId());
//                    planifications.add(bean);
//                } else {
//                    dao.update(entityPDP);
//                    planifications.set(planifications.indexOf(planificationPDP), bean);
//                }
//                setUpdatePlanPDP(true);
//                return true;
//            }
//            return false;
//        } catch (Exception ex) {
//            getException("Error " + action + " : " + ex.getMessage(), ex);
//            getErrorMessage(action + " impossible !");
//            return false;
//        }
//    }
//
//    public YvsProdPeriodePlan buildPeriodePlanificationPDP(PeriodePlanification y) {
//        YvsProdPeriodePlan p = new YvsProdPeriodePlan();
//        if (y != null) {
//            p.setDebutPeriode(y.getDateDebut());
//            p.setFinPeriode(y.getDateFin());
//            p.setId(y.getId());
//            p.setIndicatif((int) y.getIndicatif());
//            p.setReference(y.getReference());
//            p.setPlan(entityPDP);
//        }
//        return p;
//    }
//
//    public PeriodePlanificationPDP recopieViewPeriodePDP() {
//        PeriodePlanificationPDP p = new PeriodePlanificationPDP();
//        p.setDateDebut(periode.getDateDebut());
//        p.setDateFin(periode.getDateFin());
//        p.setId(periode.getId());
//        p.setIndicatif(periode.getIndicatif());
//        p.setReference(periode.getReference());
//        return p;
//    }
//
//    public boolean controleFichePeriodePDP(PeriodePlanification bean) {
//        if (bean.getReference() == null || bean.getReference().equals("")) {
//            getErrorMessage("Vous devez renseigner la reference");
//            return false;
//        }
//        return true;
//    }
//
//    public void resetFichePeriodePDP() {
//        resetFiche(periode);
//        periode.setPlan(new Planification());
//        periode.setDetails(new ArrayList<DetailPlanPDP>());
//        setUpdatePeriodeDPDP(false);
//        resetPagePeriode();
//    }
//
//    public void resetPagePeriode() {
//        for (PeriodePlanification p : planificationPDP.getPeriodes()) {
//            p.setSelectActif(false);
//        }
//    }
//
//    public void deleteBeanPeriode() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void updateBeanPeriode() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void populateViewPeriode(PeriodePlanification bean) {
//        cloneObject(periode, bean);
//        setUpdatePeriodeDPDP(true);
//    }
//
//    public PeriodePlanificationPDP saveNewPeriodePDP(PeriodePlanificationPDP bean) {
//        if (controleFichePeriodePDP(bean)) {
//            YvsProdPeriodePlan entite = buildPeriodePlanificationPDP(bean);
//            if (!isUpdatePeriodeDPDP()) {
//                entite = (YvsProdPeriodePlan) dao.save1(entite);
//                bean.setId(entite.getId());
//                periode.setId(entite.getId());
//                planificationPDP.getPeriodes().add(bean);
//            } else {
//                dao.update(entite);
//                planificationPDP.getPeriodes().set(planificationPDP.getPeriodes().indexOf(periode), bean);
//            }
//            return bean;
//        }
//        return null;
//    }
//
//    public YvsProdDetailPdp buildDetailPlanPDP(DetailPlanPDP y) {
//        YvsProdDetailPdp d = new YvsProdDetailPdp();
//        if (y != null) {
//            d.setId(y.getId());
//            d.setValeur(y.getValeur());
//            d.setTypeVal(y.getTypeVal());
//            if ((y.getPeriode() != null) ? (y.getPeriode().getId() != 0) : false) {
//                d.setPeriode(new YvsProdPeriodePlan(y.getPeriode().getId()));
//            }
//            if ((y.getArticle() != null) ? (y.getArticle().getId() != 0) : false) {
//                d.setArticle(new YvsBaseArticles(y.getArticle().getId_()));
//            }
//        }
//        return d;
//    }
//
//    public DetailPlanPDP recopieViewDetailPlanPDP() {
//        DetailPlanPDP d = new DetailPlanPDP();
//        d.setId(detail.getId());
//        d.setValeur(detail.getValeur());
//        d.setPdp(detail.isPdp());
//        d.setPeriode(detail.getPeriode());
//        d.setArticle(detail.getArticle());
//        return d;
//    }
//
//    public boolean controleFicheDetailPlanPDP(DetailPlanPDP bean) {
//        if ((bean.getPeriode() != null) ? bean.getPeriode().getId() == 0 : true) {
//            getErrorMessage("Error creation detail");
//            return false;
//        }
//        return true;
//    }
//
//    public void resetFicheDetailPDP() {
//        resetFiche(detail);
//        detail.setArticle(new Articles());
//        detail.setPeriode(new PeriodePlanification());
//        setUpdateDetailDPD(false);
//        resetPageDetailPDP();
//    }
//
//    public void resetPageDetailPDP() {
//        for (DetailPlan d : periode.getDetails()) {
//            d.setSelectActif(false);
//        }
//    }
//
//    public void deleteBeanDetailPDP() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void updateBeanDetailPDP() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void populateViewDetailPDP(DetailPlan bean) {
//        cloneObject(detail, bean);
//        setUpdateDetailDPD(true);
//    }
//
//    public void saveNewDetailPDP(PeriodePlanificationPDP periode, DetailPlanPDP bean) {
//        bean.setPeriode(periode);
//        if (controleFicheDetailPlanPDP(bean)) {
//            YvsProdDetailPdp entite = buildDetailPlanPDP(bean);
//            if (!isUpdateDetailDPD()) {
//                entite = (YvsProdDetailPdp) dao.save1(entite);
//                bean.setId(entite.getId());
//                periode.getDetails().add(bean);
//            } else {
//                dao.update(entite);
//                periode.getDetails().set(periode.getDetails().indexOf(bean), bean);
//            }
//        }
//    }
//
//    public void saveNewPDP() {
//        List<DetailPlanPDP> list = getValuePdp();
//        System.err.println("list " + list.size());
//        for (PeriodePlanificationPDP p : periodesPDP) {
//            PeriodePlanificationPDP period = saveNewPeriodePDP(p);
//            if ((period != null) ? period.getId() != 0 : false) {
//                for (DetailPlanPDP det : list) {
//                    if (det.getPeriode().equals(p)) {
//                        saveNewDetailPDP(period, det);
//                    }
//                }
//            } else {
//                getErrorMessage("Sauvegarde total impossible !");
//            }
//        }
//        succes();
//    }
//
//    public List<DetailPlanPDP> getValuePdp() {
//        List<DetailPlanPDP> list = new ArrayList<>();
//        DetailPlanPDP d;
//        Articles a;
//        PeriodePlanificationPDP p;
//        for (int i = 0; i < articlesView.size(); i++) {
//            a = articlesView.get(i);
//            for (int s = 0; s < sitePDP.size(); s++) {
//                for (int j = 0; j < periodesPDP.size(); j++) {
//                    p = periodesPDP.get(j);
//                    d = new DetailPlanPDP();
//                    d.setArticle(a);
//                    d.setTypeVal(sitePDP.get(s));
//                    d.setPeriode(p);
//                    d.setValeur(dataPDP.get(i).get(s).get(j).getValeur());
//                    list.add(d);
//                }
//            }
//        }
//        return list;
//    }
//
//    public void genererPDP() {
//        if ((tabId_PDP != null) ? !tabId_PDP.equals("") : false) {
//            if (saveNewPlanPDP()) {
//                if (isUpdatePlanPDP()) {
//                    articlesView.clear();
//                    String[] tab = tabId_PDP.split("-");
//                    for (String s : tab) {
//                        int id = Integer.valueOf(s);
//                        Articles art = articles.get(id);
//                        articlesView.add(art);
//                    }
//                    if ((articlesView != null) ? !articlesView.isEmpty() : false) {
//                        initDetailPDP();
//                    }
//                } else {
//                    getErrorMessage("Vous devez dabor enregistrer le PDP");
//                }
//            }
//        } else {
//            update("form_data_article");
//        }
//    }
//
//    public void genererPDP_() {
//        if ((tabId_PDP != null) ? !tabId_PDP.equals("") : false) {
//            articlesView.clear();
//            String[] tab = tabId_PDP.split("-");
//            for (String s : tab) {
//                int id = Integer.valueOf(s);
//                Articles art = articles.get(articles.indexOf(new Articles(id)));
//                articlesView.add(art);
//            }
//            if ((articlesView != null) ? !articlesView.isEmpty() : false) {
//                initDetailPDP();
//            }
//        } else {
//            update("form_data_article");
//        }
//    }
//
//    private double getValueVente(Articles article, PeriodePlanification periode) {
//        return alea.nextInt(200);
//    }
//
//    private double getValueLotLancement(Articles a) {
//        return a.getLotFabrication();
//    }
//
//    private double getValueStockInit() {
//
//        return 150;
//    }
//
//    private double getValueStockSecu() {
//
//        return 50;
//    }
//
//    private double getValueReceptionAttendu() {
//
//        return 0;
//    }
//
//    public final void initDetailPDP() {
//        fullSitePDP();
//        dataPDP.clear();
//        for (Articles a : articlesView) {
//            List<List<DetailPlanPDP>> ls = new ArrayList<>();
//            for (String s : sitePDP) {
//                List<DetailPlanPDP> l = new ArrayList<>();
//                int i = 0;
//                for (PeriodePlanificationPDP p : periodesPDP) {
//                    DetailPlanPDP d = new DetailPlanPDP();
//                    d.setArticle(a);
//                    d.setTypeVal(s);
//                    d.setPeriode(p);
//                    if (s == null ? sitePDP.get(0) == null : s.equals(sitePDP.get(0))) {
//                        d.setValeur(getValueVente(a, p));
//                    } else if (s.equals(sitePDP.get(1))) {
//                        d.setValeur(0);
//                    } else if (s.equals(sitePDP.get(2))) {
//                        d.setValeur(getValueReceptionAttendu());
//                    } else if (s.equals(sitePDP.get(3))) {
//                        d.setValeur(0);
//                    } else if (s.equals(sitePDP.get(4))) {
//                        d.setValeur(0);
//                    } else if (s.equals(sitePDP.get(5))) {
//                        if (ls.get(0).get(i).getValeur() != 0 && i < periodesPDP.size() - 1) {
//                            d.setPdp((i == 0) ? getValueStockInit() < ls.get(0).get(i).getValeur() + getValueStockSecu()
//                                    : l.get(i - 1).getValeur() < ls.get(0).get(i).getValeur() + getValueStockSecu());
//                            d.setValeur((d.isPdp() ? getValueLotLancement(a) : 0));
//                        }
//                    }
//                    l.add(d);
//                    i++;
//                }
//                ls.add(l);
//            }
//            dataPDP.add(ls);
//        }
//        completeDetailPDP();
//    }
//
//    private void completeDetailPDP() {
//        for (int a = 0; a < articlesView.size(); a++) {
//            Articles ar = articlesView.get(a);
//            for (int p = 0; p < periodesPDP.size(); p++) {
//                //Calcul du stock de debut  
//                double st;
//                if (p > 0) {
//                    st = dataPDP.get(a).get(1).get(p - 1).getValeur();
//                } else {
//                    st = getValueStockInit();
//                }
//                double bb = dataPDP.get(a).get(0).get(p).getValeur();
//                double ra = dataPDP.get(a).get(2).get(p).getValeur();
//                double opf = dataPDP.get(a).get(4).get(p).getValeur();
//                double sd = st + opf + ra - bb;
//                sd = (sd > 0) ? sd : 0;
//                dataPDP.get(a).get(1).get(p).setValeur(sd);
//
//                //Calcul du besoin net
//                if (p > 0) {
//                    sd = dataPDP.get(a).get(1).get(p - 1).getValeur();
//                } else {
//                    sd = getValueStockInit();
//                }
//                double bn = bb - sd - ra + getValueStockSecu();
//                bn = (bn > 0) ? bn : 0;
//                dataPDP.get(a).get(3).get(p).setValeur(bn);
//
//                //Calcul de l'ordre previsionnel de fin
//                if (p < periodesPDP.size() - 1) {
//                    double opd = dataPDP.get(a).get(5).get(p).getValeur();
//                    if (opd > 0 && bn > 0) {
//                        opf = bn / ar.getLotFabrication();
//                        opf = Util.arrondi(opf, 2);
//                        dataPDP.get(a).get(4).get(p + 1).setValeur(opf);
//                    }
//                }
//            }
//        }
//        fullPeriodePDC();
//        update("donnee_entree_PDP");
//    }
//
//    public void fullPeriodePDC() {
//        dataPDC.clear();
//        for (int i = 0; i < articlesView.size(); i++) {
//            Articles a = articlesView.get(i);
//            dataPDC.add(null);
//            for (int j = 0; j < periodesPDP.size(); j++) {
//                boolean dpd = false;
//                if (j > 0) {
//                    dpd = dataPDP.get(i).get(3).get(j - 1).isPdp();
//                }
//                dataPDP.get(i).get(2).get(j).setValeur((dpd) ? a.getLotFabrication() : 0);
//                if (dpd) {
//                    if (!periodesPDC.contains(periodesPDP.get(j - 1))) {
//                        periodesPDC.add(periodesPDP.get(j - 1));
//                    }
//                }
//            }
//        }
//        if (!periodesPDC.isEmpty()) {
//            int i = periodesPDC.get(0).getIndicatif();
//            if (i == 2) {
//                String[] mois = new String[12];
//                Calendar[] tabCal = new Calendar[periodesPDC.size()];
//                Calendar c = Calendar.getInstance();
//                for (PeriodePlanificationPDP p : periodesPDC) {
//                    String m = p.getReference().substring(0, p.getReference().length() - 4).trim();
//                    int year = Integer.valueOf(p.getReference().substring(p.getReference().length() - 4, p.getReference().length()).trim());
//                    int month = Util.getMonth(m);
//                    int day = 1;
//                    for (int j = 0; j < tabCal.length; j++) {
//                        c.clear();
//                        c.set(year, month, day);
//                        tabCal[j] = c;
//                    }
//                }
//
//                for (int j = 0; j < tabCal.length - 2; j++) {
//                    for (int k = 1; k < tabCal.length - 1; k++) {
//                        if (tabCal[j].after(tabCal[k])) {
//                            c = tabCal[j];
//                            tabCal[j] = tabCal[k];
//                            tabCal[k] = c;
//                        }
//                    }
//                }
//
//                for (PeriodePlanificationPDP p : periodesPDC) {
//                    String m = p.getReference().substring(0, p.getReference().length() - 4).trim();
//                    for (int j = 0; j < 12; j++) {
//                        if (Util.tabMois[j].equals(m)) {
//                            mois[j] = p.getReference();
//                            break;
//                        }
//                    }
//                }
//                List<PeriodePlanificationPDP> list = new ArrayList<>();
//                list.addAll(periodesPDC);
//                periodesPDC.clear();
//                for (String s : mois) {
//                    if ((s != null) ? !s.equals("") : false) {
//                        for (PeriodePlanificationPDP p : list) {
//                            if (p.getReference().equals(s)) {
//                                periodesPDC.add(p);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        update("select_periode_pdc");
//        update("select_article_pdc");
//    }
//
//    public void getHorizonPdp() {
//        getDateFinPeriode();
//        getPeriodePDP();
//    }
//
//    public void getDateFinPeriode() {
//        int horizon = planificationPDP.getHorizon();
//        Date dateDebut = planificationPDP.getDateDebut();
//        if (dateDebut != null) {
//            Calendar cal = Util.dateToCalendar(dateDebut);
//            int nombre = 0;
//            switch (horizon) {
//                case 1:
//                    Calendar c = Util.dateToCalendar(dateDebut);
//                    for (int i = 0; i < planificationPDP.getAmplitude(); i++) {
//                        nombre += Util.getNumberOfWeeksInYear(c.get(Calendar.YEAR));
//                        c.add(Calendar.WEEK_OF_YEAR, nombre);
//                    }
//                    break;
//                case 2:
//                    nombre = 4;
//                    nombre = nombre * planificationPDP.getAmplitude();
//                    break;
//                case 3:
//                    nombre = 1;
//                    nombre = nombre * planificationPDP.getAmplitude();
//                    break;
//                default:
//                    nombre = 0;
//                    break;
//            }
//            cal.add(Calendar.WEEK_OF_YEAR, nombre);
//            planificationPDP.setDateFin(cal.getTime());
//            update("date_fin_periode_pdp");
//        }
//    }
//
//    public void getDateDebutPeriode() {
//        int horizon = planificationPDP.getHorizon();
//        Date dateFin = planificationPDP.getDateFin();
//        if (dateFin != null) {
//            Calendar cal = Util.dateToCalendar(dateFin);
//            int nombre = 0;
//            switch (horizon) {
//                case 1:
//                    Calendar c = Util.dateToCalendar(dateFin);
//                    for (int i = 0; i < planificationPDP.getAmplitude(); i++) {
//                        nombre += Util.getNumberOfWeeksInYear(c.get(Calendar.YEAR));
//                        c.add(Calendar.WEEK_OF_YEAR, nombre);
//                    }
//                    break;
//                case 2:
//                    nombre = 4;
//                    nombre = nombre * planificationPDP.getAmplitude();
//                    break;
//                case 3:
//                    nombre = 1;
//                    nombre = nombre * planificationPDP.getAmplitude();
//                    break;
//                default:
//                    nombre = 0;
//                    break;
//            }
//            cal.add(Calendar.WEEK_OF_YEAR, -nombre);
//            planificationPDP.setDateDebut(cal.getTime());
//            update("date_debut_periode_pdp");
//        }
//    }
//
//    public void getPeriodePDP() {
//        String period = planificationPDP.getPeriode();
//        if ((period != null) ? !period.equals("") : false) {
//            Calendar dateDebut = Util.dateToCalendar(planificationPDP.getDateDebut());
//            Calendar dateFin = Util.dateToCalendar(planificationPDP.getDateFin());
//            Calendar date = dateDebut;
//            int periodicie = planificationPDP.getPeriodicite();
//            int i = 0;
//            switch (period) {
//                case "M":
//                    periodesPDP.clear();
//                    i = 0;
//                    while (date.before(dateFin)) {
//                        PeriodePlanificationPDP p = new PeriodePlanificationPDP(i);
//                        int m = date.get(Calendar.MONTH);
//                        int y = date.get(Calendar.YEAR);
//                        p.setReference(Util.tabMois[m] + " " + y);
//                        p.setIndicatif(2);
//                        p.setDateDebut(date.getTime());
//                        date.add(Calendar.MONTH, periodicie);
//                        p.setDateFin(date.getTime());
//                        periodesPDP.add(p);
//                        i++;
//                    }
//                    break;
//                case "S":
//                    periodesPDP.clear();
//                    i = 0;
//                    while (date.before(dateFin)) {
//                        PeriodePlanificationPDP p = new PeriodePlanificationPDP(i);
//                        int s = date.get(Calendar.WEEK_OF_MONTH);
//                        int m = date.get(Calendar.MONTH);
//                        int y = date.get(Calendar.YEAR);
//                        p.setReference("Sem N°" + s + " de " + Util.tabMois[m] + " " + y);
//                        p.setIndicatif(3);
//                        p.setDateDebut(date.getTime());
//                        date.add(Calendar.WEEK_OF_MONTH, periodicie);
//                        p.setDateFin(date.getTime());
//                        periodesPDP.add(p);
//                        i++;
//                    }
//                    break;
//                case "J":
//                    periodesPDP.clear();
//                    i = 0;
//                    while (date.before(dateFin)) {
//                        PeriodePlanificationPDP p = new PeriodePlanificationPDP(i);
//                        int j = date.get(Calendar.DATE);
//                        int m = date.get(Calendar.MONTH);
//                        int y = date.get(Calendar.YEAR);
//                        p.setReference(j + "  " + Util.tabMois[m] + " " + y);
//                        p.setIndicatif(4);
//                        p.setDateDebut(date.getTime());
//                        date.add(Calendar.DATE, periodicie);
//                        p.setDateFin(date.getTime());
//                        periodesPDP.add(p);
//                        i++;
//                    }
//                    break;
//                case "H":
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    public void updateValuerPDP() {
//        Articles a = valeurSelect.getArticle();
//        PeriodePlanification p = valeurSelect.getPeriode();
//        String s = valeurSelect.getTypeVal();
//        dataPDP.get(articlesView.indexOf(a)).get(getIndexOfSite(s, sitePDP)).get(periodesPDP.indexOf(p)).setValeur(valeurSelect.getValeur());
//        update("donnee_entree_PDP");
//    }
//
//    public void fullPhaseGamme(Articles article, PeriodePlanificationPDP periode) {
//        gammes.clear();
//        List<YvsProdGammeArticle> l = dao.loadNameQueries("YvsProdGammeArticle.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(article.getId_())});
//        gammes = UtilProd.buildBeanListGammeArticle(l, periode, article);
//    }
//
//    public void fullDetailPDC(Articles article, PeriodePlanificationPDP periode) {
//        fullPhaseGamme(article, periode);
//    }
//
//    public void chooseArticlePDC() {
//        if (articleSelectPDC.getId_() > 0) {
//            if (periodeSelectPDC.getId() > -1) {
//                for (int a = 0; a < articlesView.size(); a++) {
//                    if (articlesView.get(a).equals(articleSelectPDC)) {
//                        for (int p = 0; p < periodesPDP.size(); p++) {
//                            if (periodesPDP.get(p).equals(periodeSelectPDC)) {
//                                detailSelectPDC = new DetailPlanPDP();
//                                boolean pdp = dataPDP.get(a).get(3).get(p).getValeur() > 0;
//                                if (!pdp) {
//                                    getInfoMessage("L'article n'est pas programmé pour cette periode !");
//                                    resetFiche(articleSelectPDC);
//                                    update("select_article_pdc");
//                                } else {
//                                    detailSelectPDC = dataPDP.get(a).get(3).get(p);
//                                    fullDetailPDC(articleSelectPDC, periodeSelectPDC);
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                getErrorMessage("Vous devez selectionner une periode !");
//            }
//        }
//    }
//
//    public void choosePeriodePDC() {
//        if (periodeSelectPDC.getId() < 0) {
//            getErrorMessage("Vous devez selectionner une periode !");
//        } else {
//            periodeSelectPDC = periodesPDC.get(periodesPDC.indexOf(periodeSelectPDC));
//        }
//        resetFiche(articleSelectPDC);
//        update("select_article_pdc");
//    }
//
//    public void calculCharge() {
//        double lot = detailSelectPDC.getArticle().getLot_lancement();
//        if (lot > 0) {
//            for (GammeArticle g : gammes) {
//                for (PhaseGamme p : g.getPhases()) {
//                    if (p.getMods_q() != 0 && p.getCapacite_q() != 0) {
//                        p.setCharge_h((lot / p.getMods_q()) / p.getCapacite_q_());
//                    } else {
//                        p.setCharge_h(0);
//                    }
//                    if (p.getCapacite_h() != 0) {
//                        p.setCharge_q(lot / p.getCapacite_h_());
//                    } else {
//                        p.setCharge_q(0);
//                    }
//                    if (p.getCharge_h() != 0 && p.getCapacite_h() != 0) {
//                        p.setTaux_charge((p.getCharge_h_() / p.getCapacite_h_()) * 100);
//                    } else {
//                        p.setTaux_charge(0);
//                    }
//                    p.setMods_h(p.getCharge_h_() * p.getMods_q());
//                }
//            }
//        } else {
//            getErrorMessage("Le lot de lancement est vide !");
//        }
//    }
//
//    public final void fullFamilleArt() {
//        famillesArt.clear();
//        FamilleArticle f = new FamilleArticle(1);
//        f.setDesignation("Pain Simple");
//        famillesArt.add(f);
//        f = new FamilleArticle(2);
//        f.setDesignation("Pain au Lait");
//        famillesArt.add(f);
//        f = new FamilleArticle(3);
//        f.setDesignation("Cake");
//        famillesArt.add(f);
//    }
//
//    public final void fullSitePIC() {
//        sitePIC.clear();
//        sitePIC.add("Ventes");
//        sitePIC.add("Productions");
//        sitePIC.add("Stocks");
//    }
//
//    public final void fullSitePDP() {
//        sitePDP.clear();
//        sitePDP.add(Constantes.PDP_TYPE_BB);
//        sitePDP.add(Constantes.PDP_TYPE_SD);//SD.i-1 + OPD.i + RA.i - BB.i
//        sitePDP.add(Constantes.PDP_TYPE_RA);
//        sitePDP.add(Constantes.PDP_TYPE_BN);//BB - SD.i + RA.i + SC(Stock de Sécurité)
//        sitePDP.add(Constantes.PDP_TYPE_OPF);
//        sitePDP.add(Constantes.PDP_TYPE_OPD);
//    }
//
//    public void fullDetailPDC_() {
//        fullPhaseGamme_();
//        for (PhaseGamme p : phases) {
//            for (PostePhase pp : p.getPostes()) {
//                p.setCapacite_q(p.getCapacite_q_() + (pp.getNombre() * pp.getCapacite_q()));
//            }
//            for (PostePhase pp : p.getPostes()) {
//                p.setMods_q(p.getMods_q() + (pp.getNombre() * pp.getMods_q()));
//            }
//            for (PostePhase pp : p.getPostes()) {
//                p.setCapacite_h(p.getCapacite_h_() + (pp.getNombre() * pp.getCapacite_h()));
//            }
//            for (PostePhase pp : p.getPostes()) {
//                p.setCadence(p.getCadence_() + (pp.getNombre() * pp.getCadence() * pp.getMods_q()));
//            }
//        }
//    }
//
//    public void fullPosteCharge_() {
//        postes.clear();
//        PosteCharge p = new PosteCharge(1);
//        p.setDesignation("Decoupeur");
//        p.setCapaciteH(160);
//        p.setCapaciteQ(100);
//        p.setModsQ(1);
//        postes.add(p);
//        p = new PosteCharge(2);
//        p.setDesignation("Percoire");
//        p.setCapaciteH(160);
//        p.setCapaciteQ(17);
//        p.setModsQ(1);
//        postes.add(p);
//        p = new PosteCharge(3);
//        p.setDesignation("Soudeur");
//        p.setCapaciteH(160);
//        p.setCapaciteQ(6);
//        p.setModsQ(1);
//        postes.add(p);
//        p = new PosteCharge(4);
//        p.setDesignation("Peintre");
//        p.setCapaciteH(160);
//        p.setCapaciteQ(60);
//        p.setModsQ(1);
//        postes.add(p);
//        p = new PosteCharge(5);
//        p.setDesignation("Combineur");
//        p.setCapaciteH(160);
//        p.setCapaciteQ(6);
//        p.setModsQ(1);
//        postes.add(p);
//        p = new PosteCharge(6);
//        p.setDesignation("Percoire");
//        p.setCapaciteH(160);
//        p.setCapaciteQ(12);
//        p.setModsQ(2);
//        postes.add(p);
//    }
//
//    public void fullPhaseGamme_() {
//        fullPosteCharge_();
//        phases.clear();
//        PhaseGamme p = new PhaseGamme(1, 10, "Decoupage");
//        PostePhase pp = new PostePhase(1);
//        pp.setCapacite_h(160);
//        pp.setCapacite_q(100);
//        pp.setMods_q(1);
//        pp.setNombre(1);
//        pp.setPoste(postes.get(0));
//        p.getPostes().add(pp);
//        phases.add(p);
//        p = new PhaseGamme(2, 20, "Usinage");
//        pp = new PostePhase(2);
//        pp.setCapacite_h(160);
//        pp.setCapacite_q(17);
//        pp.setMods_q(1);
//        pp.setNombre(4);
//        pp.setPoste(postes.get(1));
//        p.getPostes().add(pp);
//        phases.add(p);
//        p = new PhaseGamme(3, 30, "Soudure");
//        pp = new PostePhase(3);
//        pp.setCapacite_h(160);
//        pp.setCapacite_q(6);
//        pp.setMods_q(1);
//        pp.setNombre(10);
//        pp.setPoste(postes.get(2));
//        p.getPostes().add(pp);
//        phases.add(p);
//        p = new PhaseGamme(4, 40, "Peinture");
//        pp = new PostePhase(4);
//        pp.setCapacite_h(160);
//        pp.setCapacite_q(60);
//        pp.setMods_q(1);
//        pp.setNombre(2);
//        pp.setPoste(postes.get(3));
//        p.getPostes().add(pp);
//        phases.add(p);
//        p = new PhaseGamme(5, 50, "Assemblage");
//        pp = new PostePhase(5);
//        pp.setCapacite_h(160);
//        pp.setCapacite_q(6);
//        pp.setMods_q(1);
//        pp.setNombre(10);
//        pp.setPoste(postes.get(4));
//        p.getPostes().add(pp);
//        phases.add(p);
//        p = new PhaseGamme(6, 60, "Emballage");
//        pp = new PostePhase(6);
//        pp.setCapacite_h(160);
//        pp.setCapacite_q(12);
//        pp.setMods_q(2);
//        pp.setNombre(6);
//        pp.setPoste(postes.get(5));
//        p.getPostes().add(pp);
//        phases.add(p);
//
//    }
//
//    @Override
//    public void loadOnView(SelectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void unLoadOnView(UnselectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void onCellEdit(CellEditEvent event) {
//        if (event != null) {
//            Object oldValue = event.getOldValue();
//            Object newValue = event.getNewValue();
//            if (newValue != null && !newValue.equals(oldValue)) {
//                getInfoMessage("Old : " + oldValue + ", New : " + newValue);
//            }
//        }
//    }
//}
