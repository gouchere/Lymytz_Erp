/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rrr;

import yvs.commercial.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.FamilleArticle;
import yvs.base.produits.ManagedArticles;
import yvs.base.produits.ManagedFamilleArticle;
import yvs.base.produits.UniteMesure;
import yvs.production.UtilProd;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.commercial.rrr.YvsComGrilleRistourne;
import yvs.entity.commercial.rrr.YvsComPlanRistourne;
import yvs.entity.commercial.rrr.YvsComRistourne;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.util.Constantes;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedRistourne extends ManagedCommercial<PlanRistourne, YvsComRistourne> implements Serializable {

    private PlanRistourne plan = new PlanRistourne();
    private List<YvsComPlanRistourne> plans;
    private YvsComPlanRistourne selectPlan;

    private Ristourne ristourne = new Ristourne();
    private List<YvsComRistourne> ristournes;
    private YvsComRistourne selectRistourne = new YvsComRistourne();

    private GrilleRabais tranche = new GrilleRabais();
    private List<YvsComGrilleRistourne> tranches;
    private YvsComGrilleRistourne selectTranche;

    private String tabIds, tabIds_ristourne, tabIds_tranche;
    private boolean listArt;

    private String articleSearch, natureSearch;
    private boolean addDateSearch;
    private Boolean permanentSearch;
    private Date debutSearch = new Date(), finSearch = new Date();

    public ManagedRistourne() {
        plans = new ArrayList<>();
        tranches = new ArrayList<>();
        ristournes = new ArrayList<>();
    }

    public String getNatureSearch() {
        return natureSearch;
    }

    public void setNatureSearch(String natureSearch) {
        this.natureSearch = natureSearch;
    }

    public boolean isAddDateSearch() {
        return addDateSearch;
    }

    public void setAddDateSearch(boolean addDateSearch) {
        this.addDateSearch = addDateSearch;
    }

    public Boolean getPermanentSearch() {
        return permanentSearch;
    }

    public void setPermanentSearch(Boolean permanentSearch) {
        this.permanentSearch = permanentSearch;
    }

    public Date getDebutSearch() {
        return debutSearch;
    }

    public void setDebutSearch(Date debutSearch) {
        this.debutSearch = debutSearch;
    }

    public Date getFinSearch() {
        return finSearch;
    }

    public void setFinSearch(Date finSearch) {
        this.finSearch = finSearch;
    }

    public List<YvsComGrilleRistourne> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsComGrilleRistourne> tranches) {
        this.tranches = tranches;
    }

    public YvsComRistourne getSelectRistourne() {
        return selectRistourne;
    }

    public void setSelectRistourne(YvsComRistourne selectRistourne) {
        this.selectRistourne = selectRistourne;
    }

    public GrilleRabais getTranche() {
        return tranche;
    }

    public void setTranche(GrilleRabais tranche) {
        this.tranche = tranche;
    }

    public YvsComGrilleRistourne getSelectTranche() {
        return selectTranche;
    }

    public void setSelectTranche(YvsComGrilleRistourne selectTranche) {
        this.selectTranche = selectTranche;
    }

    public String getTabIds_tranche() {
        return tabIds_tranche;
    }

    public void setTabIds_tranche(String tabIds_tranche) {
        this.tabIds_tranche = tabIds_tranche;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public YvsComPlanRistourne getSelectPlan() {
        return selectPlan;
    }

    public void setSelectPlan(YvsComPlanRistourne selectPlan) {
        this.selectPlan = selectPlan;
    }

    public PlanRistourne getPlan() {
        return plan;
    }

    public void setPlan(PlanRistourne plan) {
        this.plan = plan;
    }

    public List<YvsComPlanRistourne> getPlans() {
        return plans;
    }

    public void setPlans(List<YvsComPlanRistourne> plans) {
        this.plans = plans;
    }

    public String getTabIds_ristourne() {
        return tabIds_ristourne;
    }

    public void setTabIds_ristourne(String tabIds_ristourne) {
        this.tabIds_ristourne = tabIds_ristourne;
    }

    public Ristourne getRistourne() {
        return ristourne;
    }

    public void setRistourne(Ristourne planRistourne) {
        this.ristourne = planRistourne;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public List<YvsComRistourne> getRistournes() {
        return ristournes;
    }

    public void setRistournes(List<YvsComRistourne> ristournes) {
        this.ristournes = ristournes;
    }

    public String getArticleSearch() {
        return articleSearch;
    }

    public void setArticleSearch(String articleSearch) {
        this.articleSearch = articleSearch;
    }

    @Override
    public void loadAll() {
        loadAllPlanRistourne();
    }

    public void loadAllPlanRistourne() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsComPlanRistourne.findAll";
        plans = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadPlanRistourne() {
        champ = new String[]{"societe", "actif"};
        val = new Object[]{currentAgence.getSociete(), true};
        nameQueri = "YvsComPlanRistourne.findActif";
        plans = dao.loadNameQueries(nameQueri, champ, val);
    }

//    public void loadAllRistourne(YvsComPlanRistourne y) {
//        champ = new String[]{"plan"};
//        val = new Object[]{y};
//        nameQueri = "YvsComRistourne.findByPlan";
//        ristournes = dao.loadNameQueries(nameQueri, champ, val);
//    }
    public void loadAllRistourne(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.plan", "plan", new YvsComPlanRistourne(ristourne.getPlan().getId()), "=", "AND"));
        ristournes = paginator.executeDynamicQuery("YvsComRistourne", "y.id", avance, init, (int) imax, dao);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllRistourne(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllRistourne(true, true);
    }

    public void loadAllTranches(YvsComRistourne y) {
        champ = new String[]{"ristourne"};
        val = new Object[]{y};
        nameQueri = "YvsComGrilleRistourne.findByRistourne";
        tranches = dao.loadNameQueries(nameQueri, champ, val);
    }

    @Override
    public PlanRistourne recopieView() {
        plan.setNew_(true);
        return plan;
    }

    public Ristourne recopieViewRistourne() {
        ristourne.setNew_(true);
        return ristourne;
    }

    public GrilleRabais recopieView(long ristourne) {
        if (!tranche.isUnique()) {
            tranche.setMontantMinimal(1);
            tranche.setMontantMaximal(Double.MAX_VALUE);
        }
        tranche.setParent(tranche.isUpdate() ? tranche.getParent() : ristourne);
        tranche.setNew_(true);
        return tranche;
    }

    @Override
    public boolean controleFiche(PlanRistourne bean) {
        if (bean.getReference() == null || bean.getReference().trim().equals("")) {
            getErrorMessage("Vous devez entrer la reference");
            return false;
        }
        return true;
    }

    public boolean controleFiche(Ristourne bean, boolean msg) {
        if (bean.isForArticle()) {
            if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
                if (msg) {
                    getErrorMessage("Vous devez specifier l'article");
                }
                return false;
            }
            if ((bean.getConditionnement() != null) ? bean.getConditionnement().getId() < 1 : true) {
                if (msg) {
                    getErrorMessage("Vous devez specifier le conditionnement");
                }
                return false;
            }
        } else {
            if ((bean.getFamille() != null) ? bean.getFamille().getId() < 1 : true) {
                if (msg) {
                    getErrorMessage("Vous devez specifier la famille");
                }
                return false;
            }
        }
        if (!bean.getPlan().isUpdate()) {
            if (msg) {
                getErrorMessage("Vous devez dabord enregistrer le plan dec ommission");
            }
            return false;
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() > 0 : false) {
            champ = new String[]{"plan", "cond", "nature"};
            val = new Object[]{new YvsComPlanRistourne(bean.getPlan().getId()), new YvsBaseConditionnement(bean.getConditionnement().getId()), bean.getNature()};
            nameQueri = "YvsComRistourne.findCountOneByPlan";
        } else {
            champ = new String[]{"plan", "famille", "nature"};
            val = new Object[]{new YvsComPlanRistourne(bean.getPlan().getId()), new YvsBaseFamilleArticle(bean.getFamille().getId()), bean.getNature()};
            nameQueri = "YvsComRistourne.findCountOneByPlan2";
        }
        Long nb = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
        if (nb != null ? nb > 0 : false) {
            if (msg) {
                getErrorMessage("Cette ristourne a déjà été défini !");
            }
            return false;
        }
        return true;
    }

    public boolean controleFiche(GrilleRabais bean) {
        if (bean.getParent() < 1 || (selectRistourne != null ? (selectRistourne.getId() != null ? selectRistourne.getId() < 1 : true) : true)) {
            getErrorMessage("Vous devez dabord enregistrer la ristourne");
            return false;
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if (selectRistourne.getNature().equals('B') && ((bean.getConditionnement() != null) ? bean.getConditionnement().getId() < 1 : true)) {
            getErrorMessage("Vous devez specifier le conditionnement");
            return false;
        }
        if (bean.getMontantRabais() <= 0) {
            getErrorMessage("Vous devez definir un montant à attribuer pour la ristourne");
            return false;
        }
        if (bean.getMontantMaximal() <= 0) {
            getErrorMessage("Vous devez definir une grille unique ou un interval de valeur");
            return false;
        }
        if (bean.getMontantMaximal() < bean.getMontantMinimal()) {
            getErrorMessage("Le montant minimal ne peut pas etre superieur au montant maximal");
            return false;
        }
        if ((bean.getMontantMinimal() <= 1 || bean.getMontantMinimal() == Double.MIN_VALUE) && bean.getMontantMaximal() == Double.MAX_VALUE) {
            if (tranches.size() == 1) {
                YvsComGrilleRistourne y = tranches.get(0);
                if (!y.getId().equals(bean.getId())) {
                    getErrorMessage("Vous ne pouvez pas ajouter une grille unique car il existe déja une autre grille");
                    return false;
                }
            } else if (tranches.size() > 1) {
                getErrorMessage("Vous ne pouvez pas ajouter une grille unique car il existe déja une autre grille");
                return false;
            }
        }
        champ = new String[]{"ristourne", "montantMinimal", "montantMaximal"};
        val = new Object[]{selectRistourne, 1, Double.MAX_VALUE};
        YvsComGrilleRistourne y = (YvsComGrilleRistourne) dao.loadOneByNameQueries("YvsComGrilleRistourne.findByRistourneMontants", champ, val);
        if (y != null ? !y.getId().equals(bean.getId()) : false) {
            getErrorMessage("Vous ne pouvez pas ajouter une autre grille car il existe déja une grille unique");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(PlanRistourne bean) {
        cloneObject(plan, bean);
        resetFicheRistourne();
    }

    public void populateView(Ristourne bean) {
        cloneObject(ristourne, bean);
    }

    public void populateView(GrilleRabais bean) {
        cloneObject(tranche, bean);
        if (tranche.getArticle() != null ? tranche.getArticle().getId() < 1 : true) {
            tranche.setArticle(UtilCom.buildBeanArticle(selectRistourne.getArticle()));
        }
        tranche.getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle1", new String[]{"article"}, new Object[]{new YvsBaseArticles(tranche.getArticle().getId())}));
    }

    @Override
    public void resetFiche() {
        resetFiche(plan);
        plan.setRistournes(new ArrayList<YvsComRistourne>());
        selectPlan = new YvsComPlanRistourne();
        tabIds = "";
        ristournes.clear();
        resetFicheRistourne();
        update("form_plan_ristourne");
    }

    public void resetFicheRistourne() {
        plan = ristourne.getPlan();
        ristourne = new Ristourne();
        ristourne.setPlan(plan);
        ristourne.setNature('R');
        selectRistourne = new YvsComRistourne();
        tabIds_ristourne = "";
        listArt = false;
        tranches.clear();
        resetFicheTranche();
        update("form_ristourne");
    }

    public void resetFicheTranche() {
        try {
            List<YvsBaseConditionnement> conditionnements = new ArrayList<>();
            try {
                conditionnements = new ArrayList<>(tranche.getArticle().getConditionnements());
            } catch (Exception ex) {
                getException("resetFicheTranche", ex);
            }
            String natureMontant = tranche.getNatureMontant();
            String base = tranche.getBase();
            tranche = new GrilleRabais();
            tranche.setArticle(UtilCom.buildBeanArticle(selectRistourne.getArticle()));
            tranche.setConditionnement(UtilProd.buildBeanConditionnement(selectRistourne.getConditionnement()));
            tranche.setBase(base);
            tranche.setNatureMontant(natureMontant);
            tranche.getArticle().setConditionnements(conditionnements);
            selectTranche = new YvsComGrilleRistourne();
            tabIds_tranche = "";
        } catch (Exception ex) {
            getException("resetFicheTranche", ex);
        } finally {
            update("form_grille_ristourne");
        }
    }

    @Override
    public boolean saveNew() {
        String action = plan.isUpdate() ? "Modification" : "Insertion";
        try {
            PlanRistourne bean = recopieView();
            if (controleFiche(bean)) {
                YvsComPlanRistourne y = UtilCom.buildPlanRistourne(bean, currentUser, currentAgence.getSociete());
                if (!bean.isUpdate()) {
                    y.setId(null);
                    y = (YvsComPlanRistourne) dao.save1(y);
                    plan.setId(y.getId());
                    plans.add(0, y);
                } else {
                    dao.update(y);
                    int idx = plans.indexOf(y);
                    if (idx >= 0) {
                        plans.set(idx, y);
                    }
                }
                succes();
                plan.setUpdate(true);
                update("data_plan_ristourne");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewRistourne() {
        try {
            if (!ristourne.isForArticle() && ristourne.isApplyOnArticle()) {
                if (ristourne.getFamille() != null ? ristourne.getFamille().getId() < 1 : true) {
                    getErrorMessage("Vous devez selectionner une famille");
                    return false;
                }
                List<YvsBaseArticles> articles = dao.loadNameQueries("YvsBaseArticles.findByFamilleActif", new String[]{"famille", "actif"}, new Object[]{new YvsBaseFamilleArticle(ristourne.getFamille().getId()), true});
                Ristourne bean;
                boolean succes = false;
                for (YvsBaseArticles a : articles) {
                    for (YvsBaseConditionnement c : a.getConditionnements()) {
                        if (c.getByVente()) {
                            bean = new Ristourne(0, ristourne);
                            bean.setFamille(null);
                            bean.setForArticle(true);
                            bean.setArticle(new Articles(a.getId(), a.getRefArt(), a.getDesignation()));
                            bean.setConditionnement(new Conditionnement(c.getId(), new UniteMesure(c.getUnite().getReference(), c.getUnite().getLibelle())));
                            bean.setPlan(plan);
                            if (saveNewRistourne(bean, true)) {
                                succes = true;
                            }
                        }
                    }
                }
                if (succes) {
                    succes();
                }
                resetFicheRistourne();
                update("data_ristourne");
                return true;
            } else {
                boolean succes = saveNewRistourne(recopieViewRistourne(), true);
                if (succes) {
                    succes();
                    resetFicheRistourne();
                    update("data_ristourne");
                    return true;
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible !");
            getException("Error (saveNewRistourne) : " + ex.getMessage(), ex);
        }
        return false;
    }

    public boolean saveNewRistourne(Ristourne bean, boolean msg) {
        String action = bean.isUpdate() ? "Modification" : "Insertion";
        try {
            if (controleFiche(bean, msg)) {
                YvsComRistourne y = UtilCom.buildRistourne(bean, currentUser);
                if (bean.getId() <= 0) {
                    y.setId(null);
                    y = (YvsComRistourne) dao.save1(y);
                    bean.setId(y.getId());
                    ristournes.add(0, y);
                } else {
                    dao.update(y);
                    ristournes.set(ristournes.indexOf(y), y);
                }
                saveOneTranche(bean, y);
                bean.setUpdate(true);
                update("data_ristourne");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public void saveOneTranche(Ristourne bean, YvsComRistourne entity) {
        if (bean.getMontant() > 0 ? (entity != null ? entity.getId() > 0 : false) : false) {
            YvsComGrilleRistourne y = (YvsComGrilleRistourne) dao.loadOneByNameQueries("YvsComGrilleRistourne.findByRistourneValeur", new String[]{"ristourne", "montant"}, new Object[]{entity, bean.getMontant()});
            if (y != null ? y.getId() < 1 : true) {
                y = new YvsComGrilleRistourne();
                y.setArticle(entity.getArticle());
                y.setBase(bean.getBase());
                y.setConditionnement(entity.getConditionnement());
                y.setDateSave(new Date());
                y.setDateUpdate(new Date());
                y.setMontantMaximal(Double.MAX_VALUE);
                y.setMontantMinimal(1D);
                y.setNatureMontant(bean.getNatureMontant());
                y.setMontantRistourne(bean.getMontant());
                y.setRistourne(entity);
                y.setAuthor(currentUser);

                y = (YvsComGrilleRistourne) dao.save1(y);
                tranches.add(y);
            }
        }
    }

    public boolean saveNewTranche() {
        return saveNewTranche(recopieView(selectRistourne.getId()));
    }

    public boolean saveNewTranche(GrilleRabais bean) {
        String action = bean.isUpdate() ? "Modification" : "Insertion";
        try {
            if (controleFiche(bean)) {
                YvsComGrilleRistourne y = UtilCom.buildGrilleRistourne(bean, currentUser);
                if (!bean.isUpdate()) {
                    y.setId(null);
                    y = (YvsComGrilleRistourne) dao.save1(y);
                    bean.setId(y.getId());
                } else {
                    dao.update(y);
                }
                int idx = tranches.indexOf(y);
                if (idx > -1) {
                    tranches.set(idx, y);
                } else {
                    tranches.add(0, y);
                }
                succes();
                resetFicheTranche();
                update("data_grille_ristourne");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComPlanRistourne bean = plans.get(plans.indexOf(new YvsComPlanRistourne(id)));
                    dao.delete(new YvsComPlanRistourne(bean.getId()));
                    plans.remove(bean);
                    if (id == plan.getId()) {
                        resetFiche();
                    }
                }
                succes();
                update("data_plan_ristourne");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBean_(YvsComPlanRistourne y) {
        selectPlan = y;
    }

    public void deleteBean_() {
        try {
            if (selectPlan != null) {
                dao.delete(selectPlan);
                plans.remove(selectPlan);
                if (selectPlan.getId() == plan.getId()) {
                    resetFiche();
                }
                succes();
                update("data_plan_ristourne");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanCom() {
        try {
            if ((tabIds_ristourne != null) ? !tabIds_ristourne.equals("") : false) {
                String[] tab = tabIds_ristourne.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComRistourne bean = ristournes.get(ristournes.indexOf(new YvsComRistourne(id)));
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    dao.delete(bean);
                    ristournes.remove(bean);
                    if (id == ristourne.getId()) {
                        resetFicheRistourne();
                    }
                }
                succes();
                update("data_ristourne");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanCom_(YvsComRistourne y) {
        selectRistourne = y;
    }

    public void deleteBeanCom_() {
        try {
            if (selectRistourne != null) {
                selectRistourne.setAuthor(currentUser);
                dao.delete(selectRistourne);
                ristournes.remove(selectRistourne);
                if (selectRistourne.getId() == ristourne.getId()) {
                    resetFicheRistourne();
                }
                succes();
                update("data_ristourne");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanTranche() {
        try {
            if ((tabIds_tranche != null) ? !tabIds_tranche.equals("") : false) {
                String[] tab = tabIds_tranche.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComGrilleRistourne bean = tranches.get(tranches.indexOf(new YvsComGrilleRistourne(id)));
                    dao.delete(new YvsComGrilleRistourne(bean.getId()));
                    tranches.remove(bean);
                    if (id == tranche.getId()) {
                        resetFicheTranche();
                    }
                }
                succes();
                update("data_grille_ristourne");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanTranche_(YvsComGrilleRistourne y) {
        selectTranche = y;
    }

    public void deleteBeanTranche_() {
        try {
            if (selectTranche != null) {
                dao.delete(selectTranche);
                tranches.remove(selectTranche);
                if (selectTranche.getId() == tranche.getId()) {
                    resetFicheTranche();
                }
                succes();
                update("data_grille_ristourne");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void toogleActiveRistourne(YvsComPlanRistourne p) {
        p.setActif(!p.getActif());
        p.setAuthor(currentUser);
        dao.update(p);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectPlan = (YvsComPlanRistourne) ev.getObject();
            populateView(UtilCom.buildBeanPlanRistourne(selectPlan));
            update("form_plan_ristourne");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("data_ristourne");
    }

    public void loadOnViewCom(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectRistourne = (YvsComRistourne) ev.getObject();
            selectRistourne.getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle1", new String[]{"article"}, new Object[]{selectRistourne.getArticle()}));
            populateView(UtilCom.buildBeanRistourne(selectRistourne));
            champ = new String[]{"ristourne"};
            val = new Object[]{selectRistourne};
            nameQueri = "YvsComGrilleRistourne.findByRistourne";
            tranches = dao.loadNameQueries(nameQueri, champ, val);
            if (tranches != null ? tranches.size() == 1 : false) {
                ristourne.setMontant(tranches.get(0).getMontantRistourne());
            } else {

            }
            update("form_ristourne");
        }
    }

    public void unLoadOnViewCom(UnselectEvent ev) {
        resetFicheRistourne();
        update("data_grille_ristourne");
    }

    public void loadOnViewTranche(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectTranche = (YvsComGrilleRistourne) ev.getObject();
            populateView(UtilCom.buildBeanGrilleRistourne(selectTranche));
            update("form_grille_ristourne");
        }
    }

    public void unLoadOnViewTranche(UnselectEvent ev) {
        resetFicheTranche();
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            listArt = false;
            if (ristourne.getPlan().isUpdate()) {
                ristourne.setArticle(UtilProd.buildBeanArticles(bean));
            } else {
                getErrorMessage("Vous devez au préalable créer un plan de ristourne");
            }
        }
    }

    public void loadOnViewArticleForTranche(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            tranche.setArticle(UtilProd.buildBeanArticles(bean));
            update("select_article_tranche_ristourne");
            update("select_unite_article_tranche_bonus");
        }
    }

    public void onSelectPlan(YvsComPlanRistourne y) {
        if (y != null ? y.getId() > 0 : false) {
            ristourne.setPlan(UtilCom.buildBeanPlanRistourne(y));
            loadAllRistourne(true, true);
            update("blog_form_ristourne");
        }
    }

    public void chooseConditionnement() {
        if (ristourne.getConditionnement() != null ? ristourne.getConditionnement().getId() > 0 : false) {
            if (ristourne.getArticle() != null) {
                int idx = ristourne.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(ristourne.getConditionnement().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = ristourne.getArticle().getConditionnements().get(idx);
                    ristourne.setConditionnement(UtilProd.buildBeanConditionnement(y));
                }
            }
        }
    }

    public void chooseConditionnementForTranche() {
        if (tranche.getConditionnement() != null ? tranche.getConditionnement().getId() > 0 : false) {
            if (tranche.getArticle() != null) {
                int idx = tranche.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(tranche.getConditionnement().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = tranche.getArticle().getConditionnements().get(idx);
                    tranche.setConditionnement(UtilProd.buildBeanConditionnement(y));
                }
            }
        }
    }

    public void searchArticle() {
        String num = ristourne.getArticle().getRefArt();
        ristourne.getArticle().setDesignation("");
        ristourne.getArticle().setError(true);
        ristourne.getArticle().setId(0);
        listArt = false;
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            Articles y = m.searchArticleActif("V", num, false);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update("data_articles_ristourne");
                } else {
                    ristourne.setArticle(y);
                }
                ristourne.getArticle().setError(false);
            }
            listArt = y.isListArt();
        }
    }

    public void searchArticleForTranche() {
        String num = tranche.getArticle().getRefArt();
        tranche.getArticle().setDesignation("");
        tranche.getArticle().setError(true);
        tranche.getArticle().setId(0);
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            Articles y = m.searchArticleActif("V", num, true);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update("data_articles_tranche_ristourne");
                } else {
                    tranche.setArticle(y);
                    update("select_unite_article_tranche_bonus");
                }
                tranche.getArticle().setError(false);
            }
        }
    }

    public void choosePlan() {
        if (ristourne.getPlan().getId() == -1) {
            openDialog("dlgListPlanRistourne");
        } else if (ristourne.getPlan().getId() > 0) {
            int index = plans.indexOf(new YvsComPlanRistourne(ristourne.getPlan().getId()));
            if (index > -1) {
                onSelectPlan(plans.get(index));
            }
        }
    }

    public void initArticles(boolean tranche) {
        listArt = false;
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            if (!tranche) {
                m.initArticles("V", ristourne.getArticle());
                listArt = ristourne.getArticle().isListArt();
                update("data_articles_ristourne");
            } else {
                m.initArticles("V", this.tranche.getArticle());
                update("data_articles_tranche_ristourne");
            }
        }
    }

    public void initGrilleRistourne(YvsComRistourne y) {
        if (y != null ? y.getId() > 0 : false) {
            selectRistourne = y;
            tranche.setArticle(UtilCom.buildBeanArticle(selectRistourne.getArticle()));
            tranche.setConditionnement(UtilProd.buildBeanConditionnement(selectRistourne.getConditionnement()));
            loadAllTranches(y);
        }
    }

    public void activeRistourne(YvsComRistourne bean) {
        if (bean != null) {
            if (!bean.getActif() && !plan.isActif()) {
                getErrorMessage("Vous ne pouvez pas activer cette ristourne car le plan est inactif");
                return;
            }
            bean.setActif(!bean.getActif());
            dao.update(bean);
            ristournes.get(ristournes.indexOf(bean)).setActif(bean.getActif());
        }
    }

    public void activePlanRistourne(YvsComPlanRistourne bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            dao.update(bean);
            plans.get(plans.indexOf(bean)).setActif(bean.getActif());
        }
    }

    public void activeUnique() {
        if (tranche.isUnique()) {
            tranche.setMontantMinimal(0);
            tranche.setMontantMaximal(Double.MAX_VALUE);
        }
    }

    public void onArticleSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles t_ = (YvsBaseArticles) ev.getObject();
            Articles t = UtilCom.buildBeanArticle(t_);
            cloneObject(ristourne.getArticle(), t);
        }
    }

    public void changePermanent() {
        ristourne.setUnique(!ristourne.isPermanent() ? ristourne.isUnique() : true);
    }

    public void changeByElement() {
        if (ristourne.isForArticle()) {
            ristourne.setFamille(new FamilleArticle());
        } else {
            ristourne.setArticle(new Articles());
        }
        ristourne.setApplyOnArticle(true);
    }

    public void chooseFamille() {
        if (ristourne.getFamille() != null ? ristourne.getFamille().getId() > 0 : false) {
            ManagedFamilleArticle w = (ManagedFamilleArticle) giveManagedBean(ManagedFamilleArticle.class);
            if (w != null) {
                int idx = w.getFamilles().indexOf(new YvsBaseFamilleArticle(ristourne.getFamille().getId()));
                if (idx > -1) {
                    ristourne.setFamille(UtilProd.buildBeanFamilleArticle(w.getFamilles().get(idx)));
                }
            }
        }
    }

    public void chooseNature() {
        if (ristourne.getNature() == 'B') {
            ristourne.setNatureMontant(Constantes.NATURE_MTANT);
        } else {
            ristourne.setNatureMontant(Constantes.NATURE_TAUX);
        }
    }

    public void tesPermanent() {
        System.err.println("tranche.getBase() " + tranche.getBase());
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addParamArticle() {
        ParametreRequete p = new ParametreRequete("y.article", "article", null, "LIKE", "AND");
        if (Util.asString(articleSearch)) {
            p = new ParametreRequete(null, "article", articleSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", articleSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", articleSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllRistourne(true, true);
    }

    public void addParamNature() {
        ParametreRequete p = new ParametreRequete("y.nature", "nature", null, "=", "AND");
        if (Util.asString(natureSearch)) {
            p = new ParametreRequete("y.nature", "nature", natureSearch.charAt(0), "=", "AND");
        }
        paginator.addParam(p);
        loadAllRistourne(true, true);
    }

    public void addParamPermanent() {
        ParametreRequete p = new ParametreRequete("y.permanent", "permanent", null, "=", "AND");
        if (permanentSearch != null) {
            p = new ParametreRequete("y.permanent", "permanent", permanentSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAllRistourne(true, true);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateDebut", "dates", null, "=", "AND");
        if (addDateSearch ? !debutSearch.after(finSearch) : false) {
            p = new ParametreRequete("y.dateDebut", "dateDebut", debutSearch, finSearch, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllRistourne(true, true);
    }
    
    public void setMaxValueMontantMax(){
        tranche.setMontantMaximal(Double.MAX_VALUE);
        update("txt_intervale_montant_ristourne_maximal");
    }
}
