/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.compta.analytique.YvsComptaPlanAnalytique;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@SessionScoped
@ManagedBean
public class ManagedAnalytique extends Managed<PlanAnalytique, YvsComptaPlanAnalytique> implements Serializable {

    private PlanAnalytique plan = new PlanAnalytique();
    private List<YvsComptaPlanAnalytique> plans;
    private YvsComptaPlanAnalytique selectPlan;

    private String numeroSearch;
    private Boolean actifSearch;

    public ManagedAnalytique() {
        plans = new ArrayList<>();
    }

    public String getNumeroSearch() {
        return numeroSearch;
    }

    public void setNumeroSearch(String numeroSearch) {
        this.numeroSearch = numeroSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public PlanAnalytique getPlan() {
        return plan;
    }

    public void setPlan(PlanAnalytique plan) {
        this.plan = plan;
    }

    public List<YvsComptaPlanAnalytique> getPlans() {
        return plans;
    }

    public void setPlans(List<YvsComptaPlanAnalytique> plans) {
        this.plans = plans;
    }

    public YvsComptaPlanAnalytique getSelectPlan() {
        return selectPlan;
    }

    public void setSelectPlan(YvsComptaPlanAnalytique selectPlan) {
        this.selectPlan = selectPlan;
    }

    @Override
    public void loadAll() {
        loadAll(true, true);
    }

    public void loadAll(boolean avancer, boolean init) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        plans = paginator.executeDynamicQuery("YvsComptaPlanAnalytique", "y.codePlan", avancer, init, dao);
        update("data_plan_analytique");
    }

    public void loadAllPlan() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsComptaPlanAnalytique.findAlls";
        plans = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadActif() {
        plans = dao.loadNameQueries("YvsComptaPlanAnalytique.findByActif", new String[]{"societe", "actif"}, new Object[]{currentAgence.getSociete(), true});
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
            setOffset(0);
        }
        List<YvsComptaPlanAnalytique> re = paginator.parcoursDynamicData("YvsComptaPlanAnalytique", "y", "y.codePlan", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
        update("blog_form_plan_analytique");
    }

    public void paginer(boolean avancer) {
        loadAll(avancer, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public void resetFiche() {
        plan = new PlanAnalytique();
        selectPlan = new YvsComptaPlanAnalytique();
    }

    @Override
    public boolean controleFiche(PlanAnalytique bean) {
        if (bean == null) {
            getErrorMessage("Action impossible");
            return false;
        }
        if (bean.getCodePlan() != null ? bean.getCodePlan().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez la reference");
            return false;
        }
        if (bean.getIntitule() != null ? bean.getIntitule().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez le libellé");
            return false;
        }
        YvsComptaPlanAnalytique y = (YvsComptaPlanAnalytique) dao.loadOneByNameQueries("YvsComptaPlanAnalytique.findByCodePlan", new String[]{"societe", "codePlan"}, new Object[]{currentAgence.getSociete(), bean.getCodePlan()});
        if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
            getErrorMessage("Vous avez deja crée ce plan analytique");
            return false;
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(plan)) {
                selectPlan = UtilCompta.buildPlanAnalytique(plan, currentUser, currentAgence.getSociete());
                if (plan.getId() < 1) {
                    selectPlan.setId(null);
                    selectPlan = (YvsComptaPlanAnalytique) dao.save1(selectPlan);
                    plan.setId(selectPlan.getId());
                } else {
                    dao.update(selectPlan);
                }
                int idx = plans.indexOf(selectPlan);
                if (idx > -1) {
                    plans.set(idx, selectPlan);
                } else {
                    plans.add(0, selectPlan);
                }
                actionOpenOrResetAfter(this);
                update("data_plan_analytique");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!");
            Logger.getLogger(ManagedAnalytique.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public void deleteBean() {
        try {
            if (selectPlan != null ? selectPlan.getId() > 0 : false) {
                dao.delete(selectPlan);
                plans.remove(selectPlan);
                if (selectPlan.getId().equals(plan.getId())) {
                    resetFiche();
                    update("form_plan_analytique");
                }
                update("data_plan_analytique");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!");
            Logger.getLogger(ManagedAnalytique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onSelectObject(YvsComptaPlanAnalytique y) {
        selectPlan = y;
        plan = UtilCompta.buildBeanPlanAnalytique(y);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsComptaPlanAnalytique) ev.getObject());
            update("form_plan_analytique");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("form_plan_analytique");
    }

    public void changeActif(YvsComptaPlanAnalytique y) {
        if (y != null ? y.getId() > 0 : false) {
            y.setActif(!y.getActif());
            dao.update(y);
            int idx = plans.indexOf(y);
            if (idx > -1) {
                plans.set(idx, y);
            }
            succes();
        }
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.codePlan", "codePlan", null, "LIKE", "AND");
        if ((numeroSearch != null) ? !numeroSearch.trim().isEmpty() : false) {
            p = new ParametreRequete(null, "codePlan", null, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codePlan)", "numero", numeroSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.intitule)", "numero", numeroSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAll(true, true);
    }

}
