/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.caisse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.Comptes;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.UtilCompta;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaPlanAbonnement;
import yvs.util.Managed;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedPlanAbonnement extends Managed<PlanDecoupage, YvsComptaPlanAbonnement> implements Serializable {

    @ManagedProperty(value = "#{planDecoupage}")
    private PlanDecoupage planDecoupage;
    private List<YvsComptaPlanAbonnement> plans;
    private YvsComptaPlanAbonnement selectPlan;

    private String tabIds;

    public ManagedPlanAbonnement() {
        plans = new ArrayList<>();
    }

    public PlanDecoupage getPlanDecoupage() {
        return planDecoupage;
    }

    public void setPlanDecoupage(PlanDecoupage planDecoupage) {
        this.planDecoupage = planDecoupage;
    }

    public List<YvsComptaPlanAbonnement> getPlans() {
        return plans;
    }

    public void setPlans(List<YvsComptaPlanAbonnement> plans) {
        this.plans = plans;
    }

    public YvsComptaPlanAbonnement getSelectPlan() {
        return selectPlan;
    }

    public void setSelectPlan(YvsComptaPlanAbonnement selectPlan) {
        this.selectPlan = selectPlan;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        plans = dao.loadNameQueries("YvsComptaPlanAbonnement.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    @Override
    public boolean controleFiche(PlanDecoupage bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getReference() != null ? bean.getReference().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez une reference");
            return false;
        }
        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(planDecoupage);
        planDecoupage.setCompte(new Comptes());
        planDecoupage.setBasePlan('T');
        planDecoupage.setPeriodicite('M');

        selectPlan = new YvsComptaPlanAbonnement();
        tabIds = "";

        update("form-plan_abonnement");
    }

    @Override
    public boolean saveNew() {
        try {
            YvsComptaPlanAbonnement y = saveNew(planDecoupage);
            if (y != null ? y.getId() > 0 : false) {
                selectPlan = y;
                succes();
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible!!!");
            getException("saveNew", ex);
        }
        return false;
    }

    public YvsComptaPlanAbonnement saveNew(PlanDecoupage planDecoupage) {
        try {
            if (controleFiche(planDecoupage)) {
                YvsComptaPlanAbonnement y = UtilCompta.buildBeanPlanAbonnement(planDecoupage, currentUser, currentAgence.getSociete());
                if (planDecoupage.getId() < 1) {
                    y.setId(null);
                    y = (YvsComptaPlanAbonnement) dao.save1(y);
                    planDecoupage.setId(y.getId());
                } else {
                    dao.update(y);
                }
                int idx = plans.indexOf(y);
                if (idx > -1) {
                    plans.set(idx, y);
                } else {
                    plans.add(y);
                }
                update("data-plan_abonnement");
                return y;
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible!!!");
            getException("saveNew", ex);
        }
        return null;
    }

    @Override
    public void onSelectObject(YvsComptaPlanAbonnement y) {
        selectPlan = y;
        PlanDecoupage bean = UtilCompta.buildBeanPlanAbonnement(y);
        cloneObject(planDecoupage, bean);
        update("form-plan_abonnement");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsComptaPlanAbonnement) ev.getObject());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    @Override
    public void deleteBean() {
        try {
            List<Integer> ids = decomposeSelection(tabIds);
            if (ids != null ? !ids.isEmpty() : false) {
                for (Integer index : ids) {
                    YvsComptaPlanAbonnement y = plans.get(index);
                    dao.delete(y);
                }
                for (Integer index : ids) {
                    YvsComptaPlanAbonnement y = plans.get(index);
                    if (planDecoupage.getId() == y.getId()) {
                        resetFiche();
                    }
                    plans.remove(y);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible!!!");
            getException("deleteBean", ex);
        }
    }

    public void deleteBean(YvsComptaPlanAbonnement y) {
        try {
            if (y != null) {
                dao.delete(y);
                if (planDecoupage.getId() == y.getId()) {
                    resetFiche();
                }
                plans.remove(y);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible!!!");
            getException("deleteBean", ex);
        }
    }

    public void deleteBean_(YvsComptaPlanAbonnement y) {
        selectPlan = y;
    }

    public void deleteBean_() {
        deleteBean(selectPlan);
    }

    public void searchCompte() {
        String numCompte = planDecoupage.getCompte().getNumCompte();
        planDecoupage.getCompte().setId(0);
        planDecoupage.getCompte().setIntitule("");
        if (numCompte != null ? numCompte.trim().length() > 0 : false) {
            planDecoupage.getCompte().setError(true);
            ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (service != null) {
                service.findCompteByNum(numCompte);
                if (service.getListComptes() != null ? !service.getListComptes().isEmpty() : false) {
                    if (service.getListComptes().size() == 1) {
                        planDecoupage.getCompte().setError(false);
                        cloneObject(planDecoupage.getCompte(), UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                    } else {
                        openDialog("dlgListCompteA");
                        update("data_comptes_plan_abonnement");
                    }
                }
            }
        }
    }

    public void loadOnViewCompte(SelectEvent ev) {
        if (ev != null) {
            cloneObject(planDecoupage.getCompte(), UtilCompta.buildSimpleBeanCompte(((YvsBasePlanComptable) ev.getObject())));
        }
    }
}
