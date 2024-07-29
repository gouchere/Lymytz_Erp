/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.UtilBase;
import yvs.base.produits.ArticleFournisseur;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.production.UtilProd;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.production.planification.YvsProdDetailPdp;
import yvs.entity.production.base.YvsProdGammeArticle;
import yvs.entity.production.planification.YvsProdPeriodePlan;
import yvs.entity.production.planification.YvsProdPlanification;
import yvs.entity.produits.YvsBaseArticles;
import yvs.production.technique.GammeArticle;
import yvs.production.base.OperationsGamme;
import yvs.production.technique.PosteCharge;
import yvs.production.base.TypeValeur;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedPlanificationPDP extends Managed<PlanificationPDP, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{planificationPDP}")
    private PlanificationPDP planificationPDP;
    private List<PlanificationPDP> planificationsPDP;
    private List<YvsBaseArticles> articles, articlesView, articlesViewUpdate;
    private ArticleFournisseur fournisseur = new ArticleFournisseur();
    private List<PeriodePlanificationPDP> periodesPDP;
    private List<TypeValeur> typesValeurPDP;
    private List<List<List<DetailPlanPDP>>> dataPDP;
    List<DetailPlanPDP> dataPDPUpdate;
    private DetailPlanPDP valeurSelect = new DetailPlanPDP(), detail = new DetailPlanPDP();
    private int[] viewRowSubTable = new int[1];
    private String ajust, tabIdArticle_PDP, tabId_PDP, idx_valeur_select,
            input_reset;
    private List<PosteCharge> postes;
    private List<OperationsGamme> phases;
    private List<GammeArticle> gammes;
    private GammeArticle gamme = new GammeArticle();
    private PeriodePlanificationPDP periode = new PeriodePlanificationPDP();
    private boolean init, updatePlanPDP, updatePeriodeDPDP, updateDetailDPD;
    Random alea = new Random();
    YvsProdPlanification entityPDP;

    public ManagedPlanificationPDP() {
        gammes = new ArrayList<>();
        planificationsPDP = new ArrayList<>();
        postes = new ArrayList<>();
        phases = new ArrayList<>();
        articlesView = new ArrayList<>();
        articlesViewUpdate = new ArrayList<>();
        articles = new ArrayList<>();
        periodesPDP = new ArrayList<>();
        typesValeurPDP = new ArrayList<>();
        dataPDP = new ArrayList<>();
        dataPDPUpdate = new ArrayList<>();
        fullTypeValeurPDP();
    }

    public GammeArticle getGamme() {
        return gamme;
    }

    public void setGamme(GammeArticle gamme) {
        this.gamme = gamme;
    }

    public List<YvsBaseArticles> getArticlesViewUpdate() {
        return articlesViewUpdate;
    }

    public void setArticlesViewUpdate(List<YvsBaseArticles> articlesViewUpdate) {
        this.articlesViewUpdate = articlesViewUpdate;
    }

    public String getInput_reset() {
        return input_reset;
    }

    public void setInput_reset(String input_reset) {
        this.input_reset = input_reset;
    }

    public String getTabId_PDP() {
        return tabId_PDP;
    }

    public void setTabId_PDP(String tabId_PDP) {
        this.tabId_PDP = tabId_PDP;
    }

    public ArticleFournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(ArticleFournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<GammeArticle> getGammes() {
        return gammes;
    }

    public void setGammes(List<GammeArticle> gammes) {
        this.gammes = gammes;
    }

    public String getIdx_valeur_select() {
        return idx_valeur_select;
    }

    public void setIdx_valeur_select(String idx_valeur_select) {
        this.idx_valeur_select = idx_valeur_select;
    }

    public boolean isUpdateDetailDPD() {
        return updateDetailDPD;
    }

    public void setUpdateDetailDPD(boolean updateDetailDPD) {
        this.updateDetailDPD = updateDetailDPD;
    }

    public DetailPlanPDP getDetail() {
        return detail;
    }

    public void setDetail(DetailPlanPDP detail) {
        this.detail = detail;
    }

    public boolean isUpdatePeriodeDPDP() {
        return updatePeriodeDPDP;
    }

    public void setUpdatePeriodeDPDP(boolean updatePeriodeDPDP) {
        this.updatePeriodeDPDP = updatePeriodeDPDP;
    }

    public PeriodePlanificationPDP getPeriode() {
        return periode;
    }

    public void setPeriode(PeriodePlanificationPDP periode) {
        this.periode = periode;
    }

    public List<PlanificationPDP> getPlanifications() {
        return planificationsPDP;
    }

    public void setPlanifications(List<PlanificationPDP> planifications) {
        this.planificationsPDP = planifications;
    }

    public boolean isUpdatePlanPDP() {
        return updatePlanPDP;
    }

    public void setUpdatePlanPDP(boolean updatePlanPDP) {
        this.updatePlanPDP = updatePlanPDP;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public String getTabIdArticle_PDP() {
        return tabIdArticle_PDP;
    }

    public void setTabIdArticle_PDP(String tabIdArticle_PDP) {
        this.tabIdArticle_PDP = tabIdArticle_PDP;
    }

    public int[] getViewRowSubTable() {
        return viewRowSubTable;
    }

    public void setViewRowSubTable(int[] viewRowSubTable) {
        this.viewRowSubTable = viewRowSubTable;
    }

    public List<YvsBaseArticles> getArticlesView() {
        return articlesView;
    }

    public void setArticlesView(List<YvsBaseArticles> articlesView) {
        this.articlesView = articlesView;
    }

    public List<OperationsGamme> getPhases() {
        return phases;
    }

    public void setPhases(List<OperationsGamme> phases) {
        this.phases = phases;
    }

    public List<PosteCharge> getPostes() {
        return postes;
    }

    public void setPostes(List<PosteCharge> postes) {
        this.postes = postes;
    }

    public String getAjust() {
        return ajust;
    }

    public void setAjust(String ajust) {
        this.ajust = ajust;
    }

    public List<List<List<DetailPlanPDP>>> getDataPDP() {
        return dataPDP;
    }

    public void setDataPDP(List<List<List<DetailPlanPDP>>> dataPDP) {
        this.dataPDP = dataPDP;
    }

    public List<TypeValeur> getTypesValeurPDP() {
        return typesValeurPDP;
    }

    public void setTypesValeurPDP(List<TypeValeur> typesValeurPDP) {
        this.typesValeurPDP = typesValeurPDP;
    }

    public List<PeriodePlanificationPDP> getPeriodesPDP() {
        return periodesPDP;
    }

    public void setPeriodesPDP(List<PeriodePlanificationPDP> periodesPDP) {
        this.periodesPDP = periodesPDP;
    }

    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    public DetailPlanPDP getValeurSelect() {
        return valeurSelect;
    }

    public void setValeurSelect(DetailPlanPDP valeurSelect) {
        this.valeurSelect = valeurSelect;
    }

    public void setPlanificationPDP(PlanificationPDP planificationPDP) {
        this.planificationPDP = planificationPDP;
    }

    public PlanificationPDP getPlanificationPDP() {
        return planificationPDP;
    }

    public void initialiser() {
        init = true;
    }

//    public void fullAll() {
//        fullTypeValeurPDP();
//        //fullDetailPIC();
//        //fullDetailPDP();
//        //fullDetailPDC_();
//    }
    @Override
    public void loadAll() {
//        checkCurrentSte();
        loadAllPlanification();
        loadAllArticle();
    }

    public void loadAllPlanification() {
        planificationsPDP.clear();
        List<YvsProdPlanification> l = dao.loadNameQueries("YvsProdPlanification.findByType", new String[]{"typePlan", "societe"}, new Object[]{"PDP", currentAgence.getSociete()});
        planificationsPDP = UtilProd.buildBeanListPlanificationPDP(l);
    }

    public void loadAllArticle() {
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (service != null) {
            service.loadActifArticle("PP");
            articles = service.getArticlesResult();
        }
    }

    public void fullPhaseAllGamme(Articles article, PeriodePlanificationPDP periode) {
        gammes.clear();
        List<YvsProdGammeArticle> l = dao.loadNameQueries("YvsProdGammeArticle.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(article.getId())});
        gammes = UtilProd.buildBeanListGammeArticle(l, periode, article);
    }

    public void fullPhaseGamme(YvsBaseArticles article, PeriodePlanificationPDP periode) {
        YvsProdGammeArticle l = (YvsProdGammeArticle) dao.loadOneByNameQueries("YvsProdGammeArticle.findByPrincipalArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(article.getId())});
        if ((l != null) ? l.getId() != 0 : false) {
//            gamme = UtilProd.buildBeanGammeArticle(l, periode, article);
        }
    }

    public double calculCharge(YvsBaseArticles article, PeriodePlanificationPDP periode) {
        fullPhaseGamme(article, periode);
        double lot = article.getLotFabrication();
        double max_ = 0;
        if (gamme != null) {
//            for (OperationsGamme p : gamme.getPhases()) {
////                if (p.getMods_q() != 0 && p.getCapacite_q() != 0) {
////                    p.setCharge_h((lot / p.getMods_q()) / p.getCapacite_q_());
////                } else {
////                    p.setCharge_h(0);
////                }
////                if (p.getCapacite_h() != 0) {
////                    p.setCharge_q(lot / p.getCapacite_h_());
////                } else {
////                    p.setCharge_q(0);
////                }
////                if (p.getCharge_h() != 0 && p.getCapacite_h() != 0) {
////                    p.setTaux_charge((p.getCharge_h_() / p.getCapacite_h_()) * 100);
////                } else {
////                    p.setTaux_charge(0);
////                }
////                p.setMods_h(p.getCharge_h_() * p.getMods_q());
////                if (max_ < p.getTaux_charge()) {
////                    max_ = p.getTaux_charge();
////                }
//            }
            return max_;
        }
        return 0;
    }

    public final void fullTypeValeurPDP() {
        typesValeurPDP.clear();
        typesValeurPDP.add(new TypeValeur(Constantes.PDP_TYPE_BB, "Besoins Bruts"));
        typesValeurPDP.add(new TypeValeur(Constantes.PDP_TYPE_SD, "Stock Début"));//SD.i-1 + OPD.i + RA.i - BB.i
        typesValeurPDP.add(new TypeValeur(Constantes.PDP_TYPE_RA, "Reception Attendue"));
        typesValeurPDP.add(new TypeValeur(Constantes.PDP_TYPE_BN, "Besoins Nets"));//BB - SD.i + RA.i + SC(Stock de Sécurité)
        typesValeurPDP.add(new TypeValeur(Constantes.PDP_TYPE_OPF, "Ordre Previsionnel Fin"));
        typesValeurPDP.add(new TypeValeur(Constantes.PDP_TYPE_OPD, "Ordre Previsionnel Debut"));
        typesValeurPDP.add(new TypeValeur(Constantes.PDP_TYPE_TM, "Taux de charge"));
    }

    private List<YvsBaseArticles> loadArticle(boolean avancer, boolean init) {
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
        PaginatorResult<YvsBaseArticles> pa = new PaginatorResult<>();
        pa = pa.loadResult(nameQueriCount, nameQueri, champ, val, getFirtResult(), getNbMax(), dao);
        setTotalPage(pa.getNbPage());
        setCurrentPage(pa.getCurrentPage());
        return pa.getResult();
    }

    @Override
    public boolean saveNew() {
        if (saveNewPlanPDP()) {
            if (!isUpdatePlanPDP()) {
                saveNewPDP();
            } else {
                updatePDP();
            }
            articlesViewUpdate.clear();
            articlesViewUpdate.addAll(articlesView);
            return true;
        }
        return false;
    }

    public void saveNewPDP() {
        List<DetailPlanPDP> list = getValuePdp();
        for (PeriodePlanificationPDP p : periodesPDP) {
            PeriodePlanificationPDP period = saveNewPeriodePDP(p);
            if ((period != null) ? period.getId() != 0 : false) {
                for (DetailPlanPDP det : list) {
                    if (det.getPeriode().equals(p)) {
                        det.setPeriode(period);
                        saveNewDetailPDP(det);
                    }
                }
            } else {
                getErrorMessage("Sauvegarde total impossible !");
            }
        }
        setUpdatePlanPDP(true);
        succes();
    }

    public void updatePDP() {
        try {
            List<DetailPlanPDP> list = getValuePdp();
            for (YvsBaseArticles a : articlesView) {
                if (!articlesViewUpdate.contains(a)) {
                    if ((list != null)) {
                        for (DetailPlanPDP d : list) {
                            if (d.getArticle().equals(a)) {
                                saveNewDetailPDP(d);
                                dataPDPUpdate.add(d);
                            }
                        }
                    }
                }
            }
            for (YvsBaseArticles a : articlesViewUpdate) {
                if (!articlesView.contains(a)) {
                    deleteBeanDetailPDP(a);
                }
            }
            succes();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            getErrorMessage("Modification Impossible !");
        }
    }

    public void deleteBeanDetailPDP(YvsBaseArticles a) {
        if ((a != null) ? a.getId() != 0 : false) {
            if ((dataPDPUpdate != null)) {
                for (DetailPlanPDP d : dataPDPUpdate) {
                    if (d.getArticle().equals(a)) {
                        dao.delete(new YvsProdDetailPdp(d.getId()));
                        dataPDPUpdate.remove(d);
                    }
                }
            }
        }
    }

    public List<DetailPlanPDP> getValuePdp() {
        List<DetailPlanPDP> list = new ArrayList<>();
        DetailPlanPDP d;
        YvsBaseArticles a;
        PeriodePlanificationPDP p;
        for (int i = 0; i < articlesView.size(); i++) {
            a = articlesView.get(i);
            for (int s = 0; s < typesValeurPDP.size(); s++) {
                for (int j = 0; j < periodesPDP.size(); j++) {
                    p = periodesPDP.get(j);
                    d = new DetailPlanPDP();
                    d.setArticle(UtilProd.buildSimpleBeanArticles(a));
                    d.setTypeVal(typesValeurPDP.get(s).getLibelle());
                    d.setPeriode(p);
                    d.setValeur(dataPDP.get(i).get(s).get(j).getValeur());
                    list.add(d);
                }
            }
        }
        return list;
    }

    public int getIndexOfSite(TypeValeur site, List<TypeValeur> sites) {
        int i = 0;
        while (i < sites.size()) {
            if (sites.get(i).equals(site)) {
                break;
            }
            i++;
        }
        return i;
    }

    public YvsProdPlanification buildPlanification(Planification y) {
        YvsProdPlanification p = new YvsProdPlanification();
        if (y != null) {
            p.setId(y.getId());
            p.setAmplitude((int) y.getAmplitude());
            p.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            p.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            p.setHorizon(y.getHorizon());
            p.setDatePlanification((y.getDatePlan() != null) ? y.getDatePlan() : new Date());
            p.setPeriode(y.getPeriode());
            p.setPeriodicite(y.getPeriodicite());
            p.setReference(y.getReference());
            p.setTypePlan(y.getType());
            p.setAuthor(currentUser);
            if (currentAgence.getSociete() != null) {
                p.setSociete(currentAgence.getSociete());
            }
        }
        return p;
    }

    @Override
    public boolean controleFiche(PlanificationPDP bean) {
        if (bean.getReference() == null || bean.getReference().equals("")) {
            getErrorMessage("Vous devez entrer la reference");
            return false;
        }
        return true;
    }

    @Override
    public PlanificationPDP recopieView() {
        PlanificationPDP p = new PlanificationPDP();
        p.setId(planificationPDP.getId());
        p.setAmplitude(planificationPDP.getAmplitude());
        p.setDateDebut(planificationPDP.getDateDebut());
        p.setDateFin(planificationPDP.getDateFin());
        p.setHorizon(planificationPDP.getHorizon());
        p.setPeriode(planificationPDP.getPeriode());
        p.setDatePlan(planificationPDP.getDatePlan());
        p.setReference(planificationPDP.getReference());
        p.setPeriodicite(planificationPDP.getPeriodicite());
        p.setType(planificationPDP.getType());
        return p;
    }

    public void recopieViewPDP(PlanificationPDP p) {
        planificationPDP.setId(p.getId());
        planificationPDP.setAmplitude(p.getAmplitude());
        planificationPDP.setDateDebut(p.getDateDebut());
        planificationPDP.setDateFin(p.getDateFin());
        planificationPDP.setHorizon(p.getHorizon());
        planificationPDP.setPeriode(p.getPeriode());
        planificationPDP.setPeriodicite(p.getPeriodicite());
        planificationPDP.setDatePlan(p.getDatePlan());
        planificationPDP.setReference(p.getReference());
        planificationPDP.setType(p.getType());
        planificationPDP.setPeriodes(p.getPeriodes());
    }

    @Override
    public void resetFiche() {
        resetFiche(planificationPDP);
        planificationPDP.setPeriodes(new ArrayList<PeriodePlanificationPDP>());
        setUpdatePlanPDP(false);
        resetPage();
    }

    @Override
    public void resetPage() {
        for (PlanificationPDP p : planificationsPDP) {
            p.setSelectActif(false);
        }
    }

    public void resetPageArticle() {
        for (YvsBaseArticles a : articles) {
//            a.setn(false);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabId_PDP != null) ? tabId_PDP.length() > 0 : false) {
                String[] ids = tabId_PDP.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Long.valueOf(s);
                        PlanificationPDP bean = planificationsPDP.get(planificationsPDP.indexOf(new PlanificationPDP(id)));
                        dao.delete(new YvsProdPlanification(bean.getId()));
                        planificationsPDP.remove(bean);
                    }
                    resetFiche();
                    succes();
                    update("tab_data_pdp");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("Error Suppression poste charge : " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
        if ((tabId_PDP != null) ? tabId_PDP.length() > 0 : false) {
            String[] ids = tabId_PDP.split("-");
            setUpdatePlanPDP((ids != null) ? ids.length > 0 : false);
            if (isUpdatePlanPDP()) {
                long id = Long.valueOf(ids[ids.length - 1]);
                PlanificationPDP bean = planificationsPDP.get(planificationsPDP.indexOf(new PlanificationPDP(id)));
                populateView(bean);
                if ((dataPDP != null) ? !dataPDP.isEmpty() : false) {
                    update("blog_form_pdp");
                }
            }
        }
    }

    @Override
    public void populateView(PlanificationPDP bean) {
        recopieViewPDP(bean);
        buildDataPDP(bean);
    }

    public void buildDataPDP(PlanificationPDP pl) {
        List<DetailPlanPDP> list = new ArrayList<>();
        if (pl != null) {
            if (pl.getPeriodes() != null) {
                articlesViewUpdate.clear();
                typesValeurPDP.clear();
                periodesPDP.clear();
                dataPDP.clear();
                articlesView.clear();
                tabIdArticle_PDP = "";
                resetPageArticle();
                TypeValeur type;
                for (PeriodePlanificationPDP p : pl.getPeriodes()) {
                    if (!periodesPDP.contains(p)) {
                        periodesPDP.add(p);
                    }
                    if (p.getDetails() != null) {
                        for (DetailPlanPDP d : p.getDetails()) {
                            if (!articlesViewUpdate.contains(d.getArticle())) {
                                articlesViewUpdate.add(UtilProd.buildEntityArticle(d.getArticle()));
//                                articles.get(articles.indexOf(d.getArticle())).setSelectActif(true);
                                if (tabIdArticle_PDP != null ? tabIdArticle_PDP.equals("") : false) {
                                    tabIdArticle_PDP = "" + d.getArticle().getId() + "-";
                                } else {
                                    tabIdArticle_PDP += d.getArticle().getId() + "-";
                                }
                            }
                            if (d.getTypeVal() == null) {
                                d.setTypeVal(Constantes.PDP_TYPE_BB);
                            }
                            switch (d.getTypeVal()) {
                                case Constantes.PDP_TYPE_BB:
                                    type = new TypeValeur(Constantes.PDP_TYPE_BB, "Besoins Bruts");
                                    break;
                                case Constantes.PDP_TYPE_SD:
                                    type = new TypeValeur(Constantes.PDP_TYPE_SD, "Stock Début");//SD.i-1 + OPD.i + RA.i - BB.i
                                    break;
                                case Constantes.PDP_TYPE_RA:
                                    type = new TypeValeur(Constantes.PDP_TYPE_RA, "Reception Attendue");
                                    break;
                                case Constantes.PDP_TYPE_BN:
                                    type = new TypeValeur(Constantes.PDP_TYPE_BN, "Besoins Nets");//BB - SD.i + RA.i + SC(Stock de Sécurité)
                                    break;
                                case Constantes.PDP_TYPE_OPF:
                                    type = new TypeValeur(Constantes.PDP_TYPE_OPF, "Ordre Previsionnel Fin");//BN:lot
                                    break;
                                case Constantes.PDP_TYPE_OPD:
                                    type = new TypeValeur(Constantes.PDP_TYPE_OPD, "Ordre Previsionnel Debut");
                                    break;
                                case Constantes.PDP_TYPE_TM:
                                    type = new TypeValeur(Constantes.PDP_TYPE_TM, "Taux Maximal");
                                    break;
                                default:
                                    type = new TypeValeur();
                                    break;
                            }
                            if (!typesValeurPDP.contains(type)) {
                                typesValeurPDP.add(type);
                            }
                            d.setPeriode(p);
                            list.add(d);
                        }
                    }
                }
                articlesView.addAll(articlesViewUpdate);

                List<List<DetailPlanPDP>> ls;
                List<DetailPlanPDP> l;
                for (YvsBaseArticles a : articlesViewUpdate) {
                    ls = new ArrayList<>();
                    for (TypeValeur t : typesValeurPDP) {
                        l = new ArrayList<>();
                        for (PeriodePlanificationPDP p : periodesPDP) {
                            for (DetailPlanPDP d : list) {
                                if (d.getArticle().equals(a) && d.getTypeVal().equals(t.getLibelle()) && d.getPeriode().equals(p)) {
                                    l.add(d);
                                    break;
                                }
                            }
                        }
                        ls.add(l);
                    }
                    dataPDP.add(ls);
                }
                dataPDPUpdate.addAll(list);
            }
        }
    }

    public boolean saveNewPlanPDP() {
        String action = isUpdatePlanPDP() ? "Modification" : "Insertion";
        try {
            planificationPDP.setType("PDP");
            PlanificationPDP bean = recopieView();
            if (controleFiche(bean)) {
                entityPDP = buildPlanification(bean);
                if (!isUpdatePlanPDP()) {
                    entityPDP.setId(null);
                    entityPDP = (YvsProdPlanification) dao.save1(entityPDP);
                    bean.setId(entityPDP.getId());
                    planificationPDP.setId(entityPDP.getId());
                    planificationsPDP.add(bean);
                } else {
                    dao.update(entityPDP);
                    planificationsPDP.set(planificationsPDP.indexOf(planificationPDP), bean);
                }
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible !");
            Logger.getLogger(ManagedPlanificationPDP.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public YvsProdPeriodePlan buildPeriodePlanificationPDP(PeriodePlanification y) {
        YvsProdPeriodePlan p = new YvsProdPeriodePlan();
        if (y != null) {
            p.setDebutPeriode(y.getDateDebut());
            p.setFinPeriode(y.getDateFin());
            p.setId(y.getId());
            p.setIndicatif((int) y.getIndicatif());
            p.setReference(y.getReference());
            p.setPlan(entityPDP);
        }
        return p;
    }

    public PeriodePlanificationPDP recopieViewPeriodePDP() {
        PeriodePlanificationPDP p = new PeriodePlanificationPDP();
        p.setDateDebut(periode.getDateDebut());
        p.setDateFin(periode.getDateFin());
        p.setId(periode.getId());
        p.setIndicatif(periode.getIndicatif());
        p.setReference(periode.getReference());
        return p;
    }

    public boolean controleFichePeriodePDP(PeriodePlanification bean) {
        if (bean.getReference() == null || bean.getReference().equals("")) {
            getErrorMessage("Vous devez renseigner la reference");
            return false;
        }
        return true;
    }

    public void resetFichePeriodePDP() {
        resetFiche(periode);
        periode.setPlan(new Planification());
        periode.setDetails(new ArrayList<DetailPlanPDP>());
        setUpdatePeriodeDPDP(false);
//        resetPagePeriode();
    }

//    public void resetPagePeriode() {
//        for (PeriodePlanification p : planificationPDP.getPeriodes()) {
//            p.setSelectActif(false);
//        }
//    }

    public void deleteBeanPeriode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateBeanPeriode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void populateViewPeriode(PeriodePlanification bean) {
        cloneObject(periode, bean);
        setUpdatePeriodeDPDP(true);
    }

    public PeriodePlanificationPDP saveNewPeriodePDP(PeriodePlanificationPDP bean) {
        if (controleFichePeriodePDP(bean)) {
            YvsProdPeriodePlan entite = buildPeriodePlanificationPDP(bean);
            if (!isUpdatePeriodeDPDP()) {
                entite.setId(null);
                entite = (YvsProdPeriodePlan) dao.save1(entite);
                bean.setId(entite.getId());
                periode.setId(entite.getId());
                planificationPDP.getPeriodes().add(bean);
            } else {
                dao.update(entite);
                planificationPDP.getPeriodes().set(planificationPDP.getPeriodes().indexOf(periode), bean);
            }
            return bean;
        }
        return null;
    }

    public YvsProdDetailPdp buildDetailPlanPDP(DetailPlanPDP y) {
        YvsProdDetailPdp d = new YvsProdDetailPdp();
        if (y != null) {
            d.setId(y.getId());
            d.setValeur(y.getValeur());
            d.setTypeVal(y.getTypeVal());
            d.setAuthor(currentUser);
            if ((y.getFournisseur() != null) ? y.getFournisseur().getId() != 0 : false) {
                d.setFournisseur(new YvsBaseArticleFournisseur(y.getFournisseur().getId()));
            }
            if ((y.getPeriode() != null) ? (y.getPeriode().getId() != 0) : false) {
                d.setPeriode(new YvsProdPeriodePlan(y.getPeriode().getId()));
            }
            if ((y.getArticle() != null) ? (y.getArticle().getId() != 0) : false) {
                d.setArticle(new YvsBaseArticles(y.getArticle().getId()));
            }
        }
        return d;
    }

    public DetailPlanPDP recopieViewDetailPlanPDP() {
        DetailPlanPDP d = new DetailPlanPDP();
        d.setId(detail.getId());
        d.setValeur(detail.getValeur());
        d.setPdp(detail.isPdp());
        d.setPeriode(detail.getPeriode());
        d.setArticle(detail.getArticle());
        d.setFournisseur(detail.getFournisseur());
        return d;
    }

    public boolean controleFicheDetailPlanPDP(DetailPlanPDP bean) {
        if ((bean.getPeriode() != null) ? bean.getPeriode().getId() == 0 : true) {
            getErrorMessage("Error creation detail");
            return false;
        }
        return true;
    }

    public void resetFicheDetailPDP() {
        resetFiche(detail);
        detail.setArticle(new Articles());
        detail.setPeriode(new PeriodePlanificationPDP());
        detail.setFournisseur(new ArticleFournisseur());
        setUpdateDetailDPD(false);
        resetPageDetailPDP();
    }

    public void resetPageDetailPDP() {
        for (DetailPlan d : periode.getDetails()) {
            d.setSelectActif(false);
        }
    }

    public void populateViewDetailPDP(DetailPlan bean) {
        cloneObject(detail, bean);
        setUpdateDetailDPD(true);
    }

//    public ArticleFournisseur getForunisseurPrincipal(Articles a) {
//        if (a != null) {
//            if (a.getFournisseurs() != null) {
//                for (YvsBaseArticleFournisseur f : a.getFournisseurs()) {
//                    if (f.isPrincipal()) {
//                        return f;
//                    }
//                }
//            }
//        }
//        return null;
//    }
    public void saveNewDetailPDP(DetailPlanPDP bean) {
//        bean.setFournisseur(getForunisseurPrincipal(bean.getArticle()));
        if (controleFicheDetailPlanPDP(bean)) {
            YvsProdDetailPdp entite = buildDetailPlanPDP(bean);
            entite.setId(null);
            entite = (YvsProdDetailPdp) dao.save1(entite);
            bean.setId(entite.getId());
            bean.getPeriode().getDetails().add(bean);
        }
    }

    public void genererPDP() {
        calculPeriode();
        if ((tabIdArticle_PDP != null) ? !tabIdArticle_PDP.equals("") : false) {
            articlesView.clear();
            List<Integer> ids=decomposeSelection(tabIdArticle_PDP);
            for (Integer idx : ids) {
                articlesView.add(articles.get(idx));
            }
            if ((articlesView != null) ? !articlesView.isEmpty() : false) {
                initDetailPDP();
            } else {
                getErrorMessage("Veuillez selectionner les produire à planifier !");
            }
        } else {
            update("form_data_article");
        }
    }

    private double getValueVente(YvsBaseArticles article, PeriodePlanification periode) {
        return alea.nextInt(200);
    }

    private double getValueLotLancement(YvsBaseArticles a) {
        return a.getLotFabrication();
    }

    private double getValueStockInit() {

        return 150;
    }

    private double getValueStockSecu() {

        return 50;
    }

    private double getValueReceptionAttendu() {

        return 0;
    }

    public final void initDetailPDP() {
        fullTypeValeurPDP();//initialise les rubriques de valeurs du PDP
        dataPDP.clear();
        List<List<DetailPlanPDP>> ls;
        List<DetailPlanPDP> l;
        for (YvsBaseArticles a : articlesView) {
            ls = new ArrayList<>();
            for (TypeValeur s : typesValeurPDP) {
                l = new ArrayList<>();
                int i = 0;
                for (PeriodePlanificationPDP p : periodesPDP) {
                    DetailPlanPDP d = new DetailPlanPDP();
                    d.setArticle(UtilProd.buildSimpleBeanArticles(a));
                    d.setTypeVal(s.getLibelle());
                    d.setPeriode(p);
                    if (s.equals(typesValeurPDP.get(0))) {
                        d.setValeur(getValueVente(a, p));   //Prévisionnel de vente de l'Article a à la période p
                    } else if (s.equals(typesValeurPDP.get(1))) {
                        d.setValeur(0);//stock début de l'article a à la période p
                    } else if (s.equals(typesValeurPDP.get(2))) {
                        d.setValeur(getValueReceptionAttendu());    //reception attendu
                    } else if (s.equals(typesValeurPDP.get(3))) {
                        d.setValeur(0);
                    } else if (s.equals(typesValeurPDP.get(4))) {
                        d.setValeur(0);
                    } else if (s.equals(typesValeurPDP.get(5))) {
                        if (ls.get(0).get(i).getValeur() != 0 && i < periodesPDP.size() - 1) {//ls.get(0).get(i).getValeur() désigne le besoin bruit de l'aticle i à la période p
                            d.setPdp((i == 0) ? getValueStockInit() < ls.get(0).get(i).getValeur() + getValueStockSecu()
                                    : l.get(i - 1).getValeur() < ls.get(0).get(i).getValeur() + getValueStockSecu());
                            d.setValeur((d.isPdp() ? getValueLotLancement(a) : 0));
                        }
                    } else if (s.equals(typesValeurPDP.get(6))) {
                        d.setValeur(Util.arrondi(calculCharge(a, p), 2));
                    }
                    l.add(d);
                    i++;
                }
                ls.add(l);
            }
            dataPDP.add(ls);
        }
        completeDetailPDP();
    }

    private void completeDetailPDP() {
        for (int a = 0; a < articlesView.size(); a++) {
            YvsBaseArticles ar = articlesView.get(a);
            for (int p = 0; p < periodesPDP.size(); p++) {
                //Calcul du stock de debut  
                double st;
                if (p > 0) {
                    st = dataPDP.get(a).get(1).get(p - 1).getValeur();
                } else {
                    st = getValueStockInit();
                }
                double bb = dataPDP.get(a).get(0).get(p).getValeur();
                double ra = dataPDP.get(a).get(2).get(p).getValeur();
                double opf = dataPDP.get(a).get(4).get(p).getValeur();
                double sd = st + opf + ra - bb;
                sd = (sd > 0) ? sd : 0;
                dataPDP.get(a).get(1).get(p).setValeur(sd);

                //Calcul du besoin net
                if (p > 0) {
                    sd = dataPDP.get(a).get(1).get(p - 1).getValeur();
                } else {
                    sd = getValueStockInit();
                }
                double bn = bb - sd - ra + getValueStockSecu();
                bn = (bn > 0) ? bn : 0;
                dataPDP.get(a).get(3).get(p).setValeur(bn);

                //Calcul de l'ordre previsionnel de fin
                if (p > 0) {
                    double opd = dataPDP.get(a).get(5).get(p - 1).getValeur();
                    if (opd > 0 && bn > 0) {
                        opf = bn / ar.getLotFabrication();
                        opf = Util.arrondi(opf, 2);
                        //Debut A retiré plutard
                        if (opf < 1) {
                            opf = ar.getLotFabrication();
                        }
                        //Fin A retiré plutard
                        dataPDP.get(a).get(4).get(p).setValeur(opf);
                    }
                }
            }
        }
        update("donnee_entree_PDP");
    }

    public void getHorizonPdp() {
        getDateFinPeriode();
//        calculPeriode();
    }

    public void getDateFinPeriode() {
        int horizon = planificationPDP.getHorizon();
        Date dateDebut = planificationPDP.getDateDebut();
        if (dateDebut != null) {
            Calendar cal = Util.dateToCalendar(dateDebut);
            switch (horizon) {
                case 1:
                    cal.add(Calendar.YEAR, planificationPDP.getAmplitude());
                    break;
                case 2:
                    cal.add(Calendar.MONTH, planificationPDP.getAmplitude());
                    break;
                case 3:
                    cal.add(Calendar.WEEK_OF_YEAR, planificationPDP.getAmplitude());
                    break;
                default:
                    break;
            }
            planificationPDP.setDateFin(cal.getTime());
            update("date_fin_periode_pdp");
        }
    }

    public void getDateDebutPeriode() {
        int horizon = planificationPDP.getHorizon();
        Date dateFin = planificationPDP.getDateFin();
        if (dateFin != null) {
            Calendar cal = Util.dateToCalendar(dateFin);
            switch (horizon) {
                case 1:
                    cal.add(Calendar.YEAR, -planificationPDP.getAmplitude());
                    break;
                case 2:
                    cal.add(Calendar.MONTH, -planificationPDP.getAmplitude());
                    break;
                case 3:
                    cal.add(Calendar.WEEK_OF_YEAR, -planificationPDP.getAmplitude());
                    break;
                default:
                    break;
            }
            planificationPDP.setDateDebut(cal.getTime());
            update("date_debut_periode_pdp");
        }
    }

    private boolean controleForm() {
        if (planificationPDP.getHorizon() <= 0) {
            getErrorMessage("Formulaire incorrecte", "vous devez indiquer l'horizon");
            return false;
        }
        if (planificationPDP.getAmplitude() <= 0) {
            getErrorMessage("Formulaire incorrecte", "vous devez indiquer l'amplitude");
            return false;
        }
        if (planificationPDP.getPeriode() == null) {
            getErrorMessage("Formulaire incorrecte", "vous devez indiquer la période");
            return false;
        }
        if (planificationPDP.getDateDebut() == null) {
            getErrorMessage("Formulaire incorrecte", "vous devez indiquer la date de début de la planification");
            return false;
        }
        return true;
    }

    private void calculPeriode() {
        if (controleForm()) {
            PeriodePlanificationPDP p;
            Calendar cal = Calendar.getInstance();
            cal.setTime(planificationPDP.getDateDebut());
            int periode = 0, coef = 0;
            switch (planificationPDP.getPeriode()) {
                case "Trimestre":
                    //le nombre de moi de l'horizon / 
                    cal.add(Calendar.MONTH, planificationPDP.getHorizon() * 3);
                    planificationPDP.setDateFin(cal.getTime());
                    periode = Calendar.MONTH;
                    coef = 3;
                    break;
                case "Mois":
                    cal.add(Calendar.MONTH, planificationPDP.getHorizon());
                    planificationPDP.setDateFin(cal.getTime());
                    periode = Calendar.MONTH;
                    coef = 1;
                    break;
                case "Semaine":
                    cal.add(Calendar.WEEK_OF_YEAR, planificationPDP.getHorizon());
                    planificationPDP.setDateFin(cal.getTime());
                    periode = Calendar.WEEK_OF_YEAR;
                    coef = 1;
                    break;
                case "Jour":
                    cal.add(Calendar.DAY_OF_MONTH, planificationPDP.getHorizon());
                    planificationPDP.setDateFin(cal.getTime());
                    periode = Calendar.DAY_OF_MONTH;
                    coef = 1;
                    break;
                default:
                    break;
            }
            periodesPDP.clear();
            cal.setTime(planificationPDP.getDateDebut());
            for (int i = 1; i <= planificationPDP.getHorizon(); i = i + planificationPDP.getAmplitude()) {
                p = new PeriodePlanificationPDP();
                p.setDateDebut(cal.getTime());
                switch (planificationPDP.getPeriode()) {
                    case "Trimestre":
                        //le nombre de moi de l'horizon / 
                        p.setReference("Trim. " + i);
                        coef = 3;
                        break;
                    case "Mois":
                        p.setReference(formatMonth_.format(p.getDateDebut()));
                        break;
                    case "Semaine":
                        p.setReference("Sem " + i);
                        coef = 1;
                        break;
                    case "Jour":
                        p.setReference(formatDayM.format(p.getDateDebut()));
                        break;
                    default:
                        break;
                }
                cal.add(periode, coef * planificationPDP.getAmplitude());
                p.setDateFin(cal.getTime());
                p.setId(i);
                periodesPDP.add(p);
            }

        }
    }

//    public void getPeriodePDP() {
//        String period = planificationPDP.getPeriode();
//        if ((period != null) ? !period.equals("") : false) {
//            Calendar dateDebut = Util.dateToCalendar(planificationPDP.getDateDebut());
//            Calendar dateFin = Util.dateToCalendar(planificationPDP.getDateFin());
//            Calendar date = dateDebut;
//            int periodicie = planificationPDP.getPeriodicite();
//            int i;
//            switch (period) {
//                case "M":
//                    periodesPDP.clear();
//                    i = 0;
//                    while (date.before(dateFin)) {
//                        PeriodePlanificationPDP p = new PeriodePlanificationPDP(i);
//                        int m = date.get(Calendar.MONTH);
//                        int y = date.get(Calendar.YEAR);
//                        p.setReference(Util.tabMois_min[m] + " " + y);
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
//                        p.setReference("Sem N°" + s + " de " + Util.tabMois_min[m] + " " + y);
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
//                        p.setReference(j + "  " + Util.tabMois_min[m] + " " + y);
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
    public void updateValuerPDP() {
        Articles a = valeurSelect.getArticle();
        PeriodePlanification p = valeurSelect.getPeriode();
        TypeValeur s = new TypeValeur(valeurSelect.getTypeVal());
        dataPDP.get(articlesView.indexOf(a)).get(getIndexOfSite(s, typesValeurPDP)).get(periodesPDP.indexOf(p)).setValeur(valeurSelect.getValeur());
        update("donnee_entree_PDP");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onCellEdit(CellEditEvent event) {
        if (event != null) {
            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();
            if (newValue != null && !newValue.equals(oldValue)) {
                getInfoMessage("Old : " + oldValue + ", New : " + newValue);
            }
        }
    }

}
