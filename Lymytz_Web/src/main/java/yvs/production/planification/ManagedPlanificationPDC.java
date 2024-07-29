/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.production.UtilProd;
import yvs.entity.production.base.YvsProdGammeArticle;
import yvs.entity.production.base.YvsProdOperationsGamme;
import yvs.entity.production.planification.YvsProdDetailPdc;
import yvs.entity.production.planification.YvsProdDetailPdp;
import yvs.entity.production.planification.YvsProdPlanification;
import yvs.entity.produits.YvsBaseArticles;
import yvs.production.technique.GammeArticle;
import yvs.production.base.OperationsGamme;
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
public final class ManagedPlanificationPDC extends Managed<DetailPlanPDC, YvsBaseCaisse> implements Serializable {

    private List<PlanificationPDP> planificationsPDP;
    private PlanificationPDP planificationPDP = new PlanificationPDP();

    private List<Articles> articlesPDP, articlesPDC;
    private Articles articleSelectPDC = new Articles();

    private List<PeriodePlanificationPDP> periodesPDP, periodesPDC;
    private PeriodePlanificationPDP periodeSelectPDC = new PeriodePlanificationPDP();

    private List<TypeValeur> typesValeurPDP, typesValeurPDC;

    private List<List<List<DetailPlanPDP>>> dataPDP;
    private DetailPlanPDP detailSelectPDP = new DetailPlanPDP();

    private List<DetailPlanPDC> detailsPlanPdc;

    private List<List<List<DetailPlanPDC_>>> dataPDC;
    private DetailPlanPDC_ detailSelectPDC = new DetailPlanPDC_();

    private String tabId_PDP, tabId_PDC, idx_valeur_select,
            input_reset, type_plan = "PDP";

    private List<GammeArticle> gammes, listDetailPdc;

    private boolean viewGamme, updatePlanPDP, updatePeriodeDPDP, updateDetailDPD;
    Random alea = new Random();
    YvsProdPlanification entityPDP;

    public ManagedPlanificationPDC() {
        listDetailPdc = new ArrayList<>();
        gammes = new ArrayList<>();
        detailsPlanPdc = new ArrayList<>();
        planificationsPDP = new ArrayList<>();
        articlesPDP = new ArrayList<>();
        articlesPDC = new ArrayList<>();
        periodesPDP = new ArrayList<>();
        periodesPDC = new ArrayList<>();
        typesValeurPDP = new ArrayList<>();
        typesValeurPDC = new ArrayList<>();
        dataPDP = new ArrayList<>();
        dataPDC = new ArrayList<>();
    }

    public DetailPlanPDC_ getDetailSelectPDC() {
        return detailSelectPDC;
    }

    public void setDetailSelectPDC(DetailPlanPDC_ detailSelectPDC) {
        this.detailSelectPDC = detailSelectPDC;
    }

    public String getType_plan() {
        return type_plan;
    }

    public void setType_plan(String type_plan) {
        this.type_plan = type_plan;
    }

    public List<TypeValeur> getTypesValeurPDC() {
        return typesValeurPDC;
    }

    public void setTypesValeurPDC(List<TypeValeur> typesValeurPDC) {
        this.typesValeurPDC = typesValeurPDC;
    }

    public List<GammeArticle> getListDetailPdc() {
        return listDetailPdc;
    }

    public void setListDetailPdc(List<GammeArticle> listDetailPdc) {
        this.listDetailPdc = listDetailPdc;
    }

    public String getTabId_PDC() {
        return tabId_PDC;
    }

    public void setTabId_PDC(String tabId_PDC) {
        this.tabId_PDC = tabId_PDC;
    }

    public List<DetailPlanPDC> getDetailsPlanPdc() {
        return detailsPlanPdc;
    }

    public void setDetailsPlanPdc(List<DetailPlanPDC> detailsPlanPdc) {
        this.detailsPlanPdc = detailsPlanPdc;
    }

    public List<Articles> getArticlesPDC() {
        return articlesPDC;
    }

    public void setArticlesPDC(List<Articles> articlesPDC) {
        this.articlesPDC = articlesPDC;
    }

    public List<PlanificationPDP> getPlanificationsPDP() {
        return planificationsPDP;
    }

    public void setPlanificationsPDP(List<PlanificationPDP> planificationsPDP) {
        this.planificationsPDP = planificationsPDP;
    }

    public PlanificationPDP getPlanificationPDP() {
        return planificationPDP;
    }

    public void setPlanificationPDP(PlanificationPDP planificationPDP) {
        this.planificationPDP = planificationPDP;
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

    public boolean isUpdatePeriodeDPDP() {
        return updatePeriodeDPDP;
    }

    public void setUpdatePeriodeDPDP(boolean updatePeriodeDPDP) {
        this.updatePeriodeDPDP = updatePeriodeDPDP;
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

    public boolean isViewGamme() {
        return viewGamme;
    }

    public void setViewGamme(boolean viewGamme) {
        this.viewGamme = viewGamme;
    }

    public List<Articles> getArticlesPDP() {
        return articlesPDP;
    }

    public void setArticlesPDP(List<Articles> articlesPDP) {
        this.articlesPDP = articlesPDP;
    }

    public DetailPlanPDP getDetailSelectPDP() {
        return detailSelectPDP;
    }

    public void setDetailSelectPDP(DetailPlanPDP detailSelectPDP) {
        this.detailSelectPDP = detailSelectPDP;
    }

    public PeriodePlanificationPDP getPeriodeSelectPDC() {
        return periodeSelectPDC;
    }

    public void setPeriodeSelectPDC(PeriodePlanificationPDP periodeSelectPDC) {
        this.periodeSelectPDC = periodeSelectPDC;
    }

    public Articles getArticleSelectPDC() {
        return articleSelectPDC;
    }

    public void setArticleSelectPDC(Articles articleSelectPDC) {
        this.articleSelectPDC = articleSelectPDC;
    }

    public List<PeriodePlanificationPDP> getPeriodesPDC() {
        return periodesPDC;
    }

    public void setPeriodesPDC(List<PeriodePlanificationPDP> periodesPDC) {
        this.periodesPDC = periodesPDC;
    }

    public List<List<List<DetailPlanPDC_>>> getDataPDC() {
        return dataPDC;
    }

    public void setDataPDC(List<List<List<DetailPlanPDC_>>> dataPDC) {
        this.dataPDC = dataPDC;
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

    @Override
    public void loadAll() {
//        checkCurrentSte();
        loadAllPlanification();
        loadAllDetailPlanPDC();
    }

    public void loadAllPlanification() {
        planificationsPDP.clear();
        List<YvsProdPlanification> l;
        if ((type_plan != null) ? !type_plan.equals("") : false) {
            l = dao.loadNameQueries("YvsProdPlanification.findByType", new String[]{"typePlan", "societe"}, new Object[]{type_plan, currentAgence.getSociete()});
        } else {
            l = dao.loadNameQueries("YvsProdPlanification.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        planificationsPDP = UtilProd.buildBeanListPlanificationPDP(l);
        dataPDC.clear();
        gammes.clear();
        periodesPDC.clear();
    }

    public void loadAllDetailPlanPDC() {
        detailsPlanPdc.clear();
        listDetailPdc.clear();
        List<YvsProdDetailPdc> l = dao.loadNameQueries("YvsProdDetailPdc.findSociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        detailsPlanPdc = UtilProd.buildBeanListDetailPlanPDC(l);
        GammeArticle gam;
        OperationsGamme phas;
        for (DetailPlanPDC d : detailsPlanPdc) {
            phas = recopiewDetailPdcToPhase(d);
            gam = d.getGammeArticle();
            if (!listDetailPdc.contains(gam)) {
//                gam.getPhases().add(phas);
                listDetailPdc.add(gam);
            } else {
//                listDetailPdc.get(listDetailPdc.indexOf(gam)).getPhases().add(phas);
            }
        }
    }

    public final void fullTypeValeurPDC() {
        typesValeurPDC.clear();
        typesValeurPDC.add(new TypeValeur("Charge(h)", "Charge en heure"));
        typesValeurPDC.add(new TypeValeur("FMT", "Flux Moyen Theorique")); //désigne le nombre d'article à sortir par unité de temps (/h)
        typesValeurPDC.add(new TypeValeur("Taux de Charge", "Taux de charge")); 
        typesValeurPDC.add(new TypeValeur("MOD", "Main Oeuvre Necessaire"));
    }

    private GammeArticle getGammeArticlePrincipale(Articles article, PeriodePlanificationPDP periode) {
        GammeArticle g = null;
        YvsProdGammeArticle l = (YvsProdGammeArticle) dao.loadOneByNameQueries("YvsProdGammeArticle.findByPrincipalArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(article.getId())});
        if ((l != null) ? l.getId() != 0 : false) {
            g = UtilProd.buildBeanGammeArticle(l, periode, article);
        }
        return g;
    }

    public OperationsGamme recopiewDetailPdcToPhase(DetailPlanPDC d) {
        OperationsGamme p = new OperationsGamme();
        if (d != null) {
            p.setId((int) d.getId());
//            p.setCapacite_h(d.getCapacite_h());
//            p.setCapacite_q(d.getCapacite_q());
//            p.setCharge_h(d.getCharge_h());
//            p.setCharge_q(d.getCharge_q());
//            p.setMods_h(d.getMods_h());
//            p.setMods_q(d.getMods_q());
//            p.setTaux_charge(d.getTaux_charge());
//            p.setDesignation(d.getPhase().getDesignation());
            p.setNumero(d.getPhase().getNumero());
        }
        return p;
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

    public int getIndexOfPhase(OperationsGamme p, List<OperationsGamme> list) {
        return list.indexOf(p);
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
            if (currentAgence.getSociete() != null) {
                p.setSociete(currentAgence.getSociete());
            }
        }
        return p;
    }

    @Override
    public boolean controleFiche(DetailPlanPDC bean) {
        if (bean.getReference() == null || bean.getReference().equals("")) {
            getErrorMessage("Vous devez entrer la reference");
            return false;
        }
        return true;
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            gammes.clear();
            dataPDC.clear();
            PlanificationPDP bean = (PlanificationPDP) ev.getObject();
            recopieViewPDP(bean);
            if ((planificationPDP != null) ? planificationPDP.getId() != 0 : false) {
                buildDataPDP_(planificationPDP);
                fullDataPDC();
            }
            setViewGamme(true);
            update("donnee_entree_PDC");
        }
    }

    //construire le plan de charge à partir des données du PDP. 
    /*en effet, le PDP détermine pour une période donnée la quantité de produits à fabriquer*/
    private void fullDataPDC() {
        fullTypeValeurPDC();
        DetailPlanPDP d;
        GammeArticle g;
        Articles ar;
        PeriodePlanificationPDP pe;
        TypeValeur ty;
        List<List<DetailPlanPDC_>> l1;
        List<DetailPlanPDC_> l2;
        DetailPlanPDC_ c;
        for (int a = 0; a < articlesPDP.size(); a++) { //pour chaque article du pdp
            ar = recopieViewArticlePDP_(articlesPDP.get(a));
            g = getGammeArticlePrincipale(ar, null);
            if (g != null) {
                gammes.add(g);
                l1 = new ArrayList<>();
                for (int t = 0; t < typesValeurPDC.size(); t++) {
                    ty = typesValeurPDC.get(t);
                    l2 = new ArrayList<>();
                    for (int p = 0; p < periodesPDP.size(); p++) {
                        pe = recopieViewPeriodePDP_(periodesPDP.get(p));
                        d = recopieViewDetailPlanPDP_(dataPDP.get(a).get(5).get(p));    //récupère la ligne de type correspondant à OPD (Ordre Prévisionnel début)
                        c = new DetailPlanPDC_();
                        c.setPdp(d);
                        c.setTypeVal(ty.getLibelle());
                        c.setGamme(g);
                        OperationsGamme ph = calculChargeGamme_(g, d.getValeur());
                        c.setPhase(ph);
                        l2.add(c);
                    }
                    l1.add(l2);
                }
                dataPDC.add(l1);
            }
        }
        setViewGamme(true);
    }

    public void viewDetailGammePDC() {
        String key = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idGamme");
        if (key != null) {
            Long id = Long.valueOf(key);
            viewDetailGammePlanPDC(new GammeArticle(id));
        }
    }

    public void viewDetailGammePlanPDC(GammeArticle g) {
        dataPDC.clear();
        int idx = gammes.indexOf(g);
        gammes.clear();
        if (g != null) {
            GammeArticle g1 = null;
            List<List<DetailPlanPDC_>> l1;
            List<DetailPlanPDC_> l2;
            DetailPlanPDC_ c;
            g = UtilProd.buildBeanGammeArticle((YvsProdGammeArticle) dao.loadOneByNameQueries("YvsProdGammeArticle.findById", new String[]{"id"}, new Object[]{g.getId()}), periodesPDP.get(0), g.getArticle());
            for (YvsProdOperationsGamme ph : g.getOperations()) {
//                g1 = buildGammeByPhase(ph);
                gammes.add(g1);
                l1 = new ArrayList<>();
                for (TypeValeur ty : typesValeurPDC) {
                    l2 = new ArrayList<>();
                    for (int p = 0; p < periodesPDP.size(); p++) {
                        PeriodePlanificationPDP pe = periodesPDP.get(p);
                        c = new DetailPlanPDC_();
                        c.setTypeVal(ty.getLibelle());
//                        c.setPhase(ph);
                        c.setPeriode(pe);
                        double v = dataPDP.get(idx).get(5).get(p).getValeur();
                        if (v > 0) {
//                            calculChargePhase(ph, v);
                            if (ty.equals(typesValeurPDC.get(0))) {
//                                c.setValeur(ph.getCharge_h_());
                            } else if (ty.equals(typesValeurPDC.get(1))) {
//                                c.setValeur(ph.getCapacite_q_());
                            } else if (ty.equals(typesValeurPDC.get(2))) {
//                                c.setValeur(ph.getTaux_charge_());
                            } else if (ty.equals(typesValeurPDC.get(3))) {
//                                c.setValeur(ph.getMods_h_());
                            }
                        }
                        l2.add(c);
                    }
                    l1.add(l2);
                }
                dataPDC.add(l1);
            }
        }
        setViewGamme(false);
        update("donnee_entree_PDC");
    }

    public void viewDetailGammePlanPDC() {
        dataPDC.clear();
        gammes.clear();
        if ((planificationPDP != null) ? planificationPDP.getId() != 0 : false) {
            buildDataPDP_(planificationPDP);
            fullDataPDC();
        }
        setViewGamme(true);
        update("donnee_entree_PDC");
    }

    private GammeArticle buildGammeByPhase(OperationsGamme p) {
        GammeArticle g = new GammeArticle();
        if (p != null) {
            g.setId(p.getId());
//            g.setReference((p.getReference() != null) ? (!p.getReference().equals("")) ? p.getReference() : p.getDesignation() : p.getDesignation());
            g.setDescription(p.getDescription());
            g.setGamme(false);
        }
        return g;
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

    public void resetFichePeriode() {
        periodeSelectPDC = new PeriodePlanificationPDP();
    }

    public void resetFicheArticle() {
        articleSelectPDC = new Articles();
    }

    public void recopieViewPeriodePDP(PeriodePlanificationPDP p) {
        periodeSelectPDC.setId(p.getId());
        periodeSelectPDC.setDateDebut(p.getDateDebut());
        periodeSelectPDC.setDateFin(p.getDateFin());
        periodeSelectPDC.setDetails(p.getDetails());
        periodeSelectPDC.setIndicatif(p.getIndicatif());
        periodeSelectPDC.setPlan(p.getPlan());
        periodeSelectPDC.setReference(p.getReference());
    }

    public void recopieViewArticlePDP(Articles a) {
        articleSelectPDC.setId(a.getId());
        articleSelectPDC.setId(a.getId());
        articleSelectPDC.setDesignation(a.getDesignation());
    }

    public Articles recopieViewArticlePDP_(Articles a) {
        Articles art = new Articles();
        if (a != null) {
            art.setId(a.getId());
            art.setId(a.getId());
            art.setDesignation(a.getDesignation());
        }
        return art;
    }

    public PeriodePlanificationPDP recopieViewPeriodePDP_(PeriodePlanificationPDP p) {
        PeriodePlanificationPDP pe = new PeriodePlanificationPDP();
        if (p != null) {
            pe.setId(p.getId());
            pe.setDateDebut(p.getDateDebut());
            pe.setDateFin(p.getDateFin());
            pe.setDetails(p.getDetails());
            pe.setIndicatif(p.getIndicatif());
            pe.setPlan(p.getPlan());
            pe.setReference(p.getReference());
        }
        return pe;
    }

    public DetailPlanPDP recopieViewDetailPlanPDP_(DetailPlanPDP y) {
        DetailPlanPDP d = new DetailPlanPDP();
        if (y != null) {
            d.setId(y.getId());
            d.setValeur(y.getValeur());
            d.setPeriode(y.getPeriode());
            d.setArticle(y.getArticle());
        }
        return d;
    }

    public YvsProdDetailPdc buildDetailPlanPDC(DetailPlanPDC y) {
        YvsProdDetailPdc d = new YvsProdDetailPdc();
        if (y != null) {
            d.setId(y.getId_());
//            d.setCapaciteH(y.getCapacite_h());
//            d.setCapaciteQ(y.getCapacite_q());
//            d.setChargeH(y.getCharge_h());
//            d.setChargeQ(y.getCharge_q());
//            d.setModsH(y.getMods_h());
//            d.setModsQ(y.getMods_q());
            if ((y.getPhase() != null) ? y.getPhase().getId() != 0 : false) {
                d.setPhase(new YvsProdOperationsGamme(y.getPhase().getId()));
            }
            if ((y.getPdp() != null) ? y.getPdp().getId() != 0 : false) {
                d.setPdp(new YvsProdDetailPdp(y.getPdp().getId()));
            }
        }
        return d;
    }

    @Override
    public DetailPlanPDC recopieView() {
        DetailPlanPDC p = new DetailPlanPDC();

        return p;
    }

    public DetailPlanPDC recopiewDetailPdcInPhase(OperationsGamme d, DetailPlanPDP pdp) {
        DetailPlanPDC p = new DetailPlanPDC();
        if (d != null) {
            p.setId((int) d.getId());
//            p.setCapacite_h(d.getCapacite_h());
//            p.setCapacite_q(d.getCapacite_q());
//            p.setCharge_h(d.getCharge_h());
//            p.setCharge_q(d.getCharge_q());
//            p.setMods_h(d.getMods_h());
//            p.setMods_q(d.getMods_q());
//            p.setTaux_charge(d.getTaux_charge());
            p.setPhase(d);
            p.setPdp(pdp);
        }
        return p;
    }

    @Override
    public void resetFiche() {

    }

    @Override
    public boolean saveNew() {
        try {
            gammes = buildDataPDC(gammes);
            if ((gammes != null) ? !gammes.isEmpty() : false) {
                if (!gammes.get(0).isGamme()) {
                    for (GammeArticle g : gammes) {
                        if (g.getOperations() != null) {
//                            for (OperationsGamme p : g.getPhases()) {
//                                YvsProdDetailPdc entite = buildDetailPlanPDC(recopiewDetailPdcInPhase(p, p.getPdp()));
//                                entite = (YvsProdDetailPdc) dao.save1(entite);
//                                p.setId(entite.getId().intValue());
//                            }
                        }
                        listDetailPdc.add(g);
                    }
                    succes();
                } else {
                    getInfoMessage("Vous devez passer en mode detail des gammes");
                }
            }
            return false;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            getErrorMessage("Insertion Impossible!");
            return false;
        }
    }

    public void saveNew_() {
        try {
            if (gammes != null) {
                for (GammeArticle g : gammes) {
                    if (g.getOperations() != null) {
//                        for (OperationsGamme p : g.getPhases()) {
//                            YvsProdDetailPdc entite = buildDetailPlanPDC(recopiewDetailPdcInPhase(p, detailSelectPDP));
//                            entite = (YvsProdDetailPdc) dao.save1(entite);
//                            p.setId(entite.getId().intValue());
//                        }
                    }
                    listDetailPdc.add(g);
                }
                succes();
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            getErrorMessage("Insertion Impossible!");
        }
    }

    public List<GammeArticle> buildDataPDC(List<GammeArticle> i) {
        List<GammeArticle> r = new ArrayList<>();
        if (dataPDC != null) {
            GammeArticle ga;
            OperationsGamme ph;
            for (int g = 0; g < i.size(); g++) {
                ga = new GammeArticle(i.get(g).getId());
                ga.setGamme(i.get(g).isGamme());
                for (int p = 0; p < periodesPDP.size(); p++) {
                    ph = new OperationsGamme();
                    ph.setId(dataPDC.get(g).get(3).get(p).getPhase().getId());
                    if (ph.getId() != 0) {
//                        ph.setCharge_h(dataPDC.get(g).get(0).get(p).getValeur());
//                        ph.setCharge_q(dataPDC.get(g).get(1).get(p).getValeur());
//                        ph.setTaux_charge(dataPDC.get(g).get(2).get(p).getValeur());
//                        ph.setMods_h(dataPDC.get(g).get(3).get(p).getValeur());
                        ph.setPdp(dataPDC.get(g).get(3).get(p).getPdp());
//                        ga.getPhases().add(ph);
                    }
                }
                r.add(ga);
            }
        }
        return r;
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
//                populateView(bean);
            }
        }
    }

    @Override
    public void populateView(DetailPlanPDC bean) {

        setUpdatePlanPDP(true);
    }

    public void fullPhaseGamme(Articles article, PeriodePlanificationPDP periode) {
        gammes.clear();
        List<YvsProdGammeArticle> l = dao.loadNameQueries("YvsProdGammeArticle.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(article.getId())});
        gammes = UtilProd.buildBeanListGammeArticle(l, periode, article);
    }

    public void fullDetailPDC(Articles article, PeriodePlanificationPDP periode) {
        fullPhaseGamme(article, periode);
    }

    public void fullAllPhaseGamme(Articles article, PeriodePlanificationPDP periode) {
        GammeArticle g = getGammeArticlePrincipale(article, periode);
        if (g != null) {
            gammes.add(g);
        }
    }

    public void fullAllDetailPDC(Articles article, PeriodePlanificationPDP periode) {
        fullAllPhaseGamme(article, periode);
    }

    public void choosePeriodePDC() {
        if (periodeSelectPDC.getId() < 0) {
            getErrorMessage("Vous devez selectionner une periode !");
        } else {
            recopieViewPeriodePDP(periodesPDC.get(periodesPDC.indexOf(periodeSelectPDC)));
        }
        resetFicheArticle();
        update("select_article_pdc");
    }

    public void chooseArticlePDC() {
        detailSelectPDP = new DetailPlanPDP();
        if (articleSelectPDC.getId() > 0) {
            if (periodeSelectPDC.getId() > -1) {
                for (int a = 0; a < articlesPDP.size(); a++) {
                    if (articlesPDP.get(a).equals(articleSelectPDC)) {
                        for (int p = 0; p < periodesPDP.size(); p++) {
                            if (periodesPDP.get(p).equals(periodeSelectPDC)) {
                                boolean pdp = dataPDP.get(a).get(5).get(p).getValeur() > 0;
                                if (!pdp) {
                                    getInfoMessage("L'article n'est pas programmé pour cette periode !");
                                    resetFiche(articleSelectPDC);
                                    update("select_article_pdc");
                                } else {
                                    detailSelectPDP = dataPDP.get(a).get(5).get(p);
                                    fullDetailPDC(articleSelectPDC, periodeSelectPDC);
                                }
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner une periode !");
            }
        }
    }

    public void choosePlanPDP() {
        resetFichePeriode();
        resetFicheArticle();
        if (planificationPDP.getId() > 0) {
            if (planificationsPDP.contains(planificationPDP)) {
                recopieViewPDP(planificationsPDP.get(planificationsPDP.indexOf(planificationPDP)));
                if ((planificationPDP != null) ? planificationPDP.getId() != 0 : false) {
                    buildDataPDP(planificationPDP);
                }
            }
        } else {
            update("data_pdp");
        }
        update("select_periode_pdc");
        update("select_article_pdc");
    }

    //charge les données du PDP
    public void buildDataPDP_(PlanificationPDP pl) {
        List<DetailPlanPDP> list = new ArrayList<>();
        if (pl != null) {
            if (pl.getPeriodes() != null) {
                articlesPDP.clear();
                periodesPDP.clear();
                typesValeurPDP.clear();
                TypeValeur type;
                for (PeriodePlanificationPDP p : pl.getPeriodes()) {
                    if (!periodesPDP.contains(p)) {
                        periodesPDP.add(p);
                    }
                    if (p.getDetails() != null) {
                        for (DetailPlanPDP d : p.getDetails()) {
                            if (!articlesPDP.contains(d.getArticle())) {
                                articlesPDP.add(d.getArticle());
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
                                    type = new TypeValeur(Constantes.PDP_TYPE_OPF, "Ordre Previsionnel Fin");
                                    break;
                                case Constantes.PDP_TYPE_OPD:
                                    type = new TypeValeur(Constantes.PDP_TYPE_OPD, "Ordre Previsionnel Debut");
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
                List<List<DetailPlanPDP>> ls;
                List<DetailPlanPDP> l;
                for (Articles a : articlesPDP) {
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
            }
        }
    }

    public void buildDataPDP(PlanificationPDP pl) {
        List<DetailPlanPDP> list = new ArrayList<>();
        if (pl != null) {
            if (pl.getPeriodes() != null) {
                articlesPDP.clear();
                periodesPDP.clear();
                typesValeurPDP.clear();
                TypeValeur type;
                for (PeriodePlanificationPDP p : pl.getPeriodes()) {
                    if (!periodesPDP.contains(p)) {
                        periodesPDP.add(p);
                    }
                    if (p.getDetails() != null) {
                        for (DetailPlanPDP d : p.getDetails()) {
                            if (!articlesPDP.contains(d.getArticle())) {
                                articlesPDP.add(d.getArticle());
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
                                    type = new TypeValeur(Constantes.PDP_TYPE_OPF, "Ordre Previsionnel Fin");
                                    break;
                                case Constantes.PDP_TYPE_OPD:
                                    type = new TypeValeur(Constantes.PDP_TYPE_OPD, "Ordre Previsionnel Debut");
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
                List<List<DetailPlanPDP>> ls;
                List<DetailPlanPDP> l;
                for (Articles a : articlesPDP) {
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
                fullPeriodePDC();
            }
        }
    }

    public void fullPeriodePDC() {
        articlesPDC.clear();
        periodesPDC.clear();
        for (int i = 0; i < articlesPDP.size(); i++) {
            Articles a = articlesPDP.get(i);
            for (int j = 0; j < periodesPDP.size(); j++) {
                boolean dpd = dataPDP.get(i).get(5).get(j).getValeur() > 0;
                if (dpd) {
                    if (!periodesPDC.contains(periodesPDP.get(j))) {
                        periodesPDC.add(periodesPDP.get(j));
                    }
                    if (!articlesPDC.contains(a)) {
                        articlesPDC.add(a);
                    }
                }
            }
        }
        if (!periodesPDC.isEmpty()) {
            int i = periodesPDC.get(0).getIndicatif();
            if (i == 2) {
                List<Calendar> lCal = new ArrayList<>();
                Calendar c;
                for (PeriodePlanificationPDP p : periodesPDC) {
                    String m = p.getReference().substring(0, p.getReference().length() - 4).trim();
                    int month = Util.getMonth(m);
                    int year = Integer.valueOf(p.getReference().substring(p.getReference().length() - 4, p.getReference().length()).trim());
                    int day = 1;
                    c = Calendar.getInstance();
                    c.clear();
                    c.set(year, month, day);
                    lCal.add(c);
                }

                Collections.sort(lCal);
                List<PeriodePlanificationPDP> list = new ArrayList<>();
                list.addAll(periodesPDC);
                periodesPDC.clear();
                for (Calendar s : lCal) {
                    if (s != null) {
                        for (PeriodePlanificationPDP p : list) {
                            String m = p.getReference().substring(0, p.getReference().length() - 4).trim();
                            int month = Util.getMonth(m);
                            int year = Integer.valueOf(p.getReference().substring(p.getReference().length() - 4, p.getReference().length()).trim());

                            int month_ = s.get(Calendar.MONTH);
                            int year_ = s.get(Calendar.YEAR);
                            if (month == month_ && year == year_) {
                                periodesPDC.add(p);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void genererPDC() {
        double lot = detailSelectPDP.getArticle().getLotFabrication();
        if (lot > 0) {
            if ((detailSelectPDP != null) ? detailSelectPDP.getId() > 0 : false) {
                calculCharge(detailSelectPDP);
            } else {
                if (periodeSelectPDC.getId() > 0) {
                    gammes.clear();
                    Articles art;
                    for (int a = 0; a < articlesPDP.size(); a++) {
                        art = recopieViewArticlePDP_(articlesPDP.get(a));
                        for (int p = 0; p < periodesPDP.size(); p++) {
                            if (periodesPDP.get(p).equals(periodeSelectPDC)) {
                                boolean pdp = dataPDP.get(a).get(5).get(p).getValeur() > 0;
                                if (pdp) {
                                    fullAllDetailPDC(art, periodeSelectPDC);
                                    calculCharge(detailSelectPDP);
                                }
                            }
                        }
                    }
                } else {
                    getErrorMessage("Vous devez selectionner une periode !");
                }
            }
        } else {
            getErrorMessage("Le lot de lancement est vide !");
        }
    }

    public void calculCharge(DetailPlanPDP pdp) {
        double lot = pdp.getArticle().getLotFabrication();
        for (GammeArticle g : gammes) {
            calculChargeGamme(g, lot);
        }
    }

    public void calculChargeGamme(final GammeArticle g, double lot) {
//        for (OperationsGamme p : g.getPhases()) {
//            calculChargePhase(p, lot);
//        }
    }

    public double calculChargeGamm(final GammeArticle g, double lot) {
        double max = 0;
//        for (OperationsGamme p : g.getPhases()) {
//            double v = calculChargePhase_(p, lot);
//            if (v > max) {
//                max = v;
//            }
//        }
        return max;
    }

    public OperationsGamme calculChargeGamme_(final GammeArticle g, double lot) {
        OperationsGamme max = new OperationsGamme();
//        for (OperationsGamme p : g.getPhases()) {
//            double v = calculChargePhase_(p, lot);
//            if (v > max.getTaux_charge()) {
//                max = p;
//            }
//        }
        return max;
    } 

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
