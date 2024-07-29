/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.commercial.UtilCom;
import yvs.commercial.depot.ManagedDepot;
import yvs.production.UtilProd;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.entity.production.equipe.YvsProdMembresEquipe;
import yvs.entity.production.equipe.YvsProdEquipeProduction;
import yvs.entity.users.YvsUsers;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedEquipeProduction extends Managed<EquipeProduction, YvsProdEquipeProduction> implements Serializable {

    @ManagedProperty(value = "#{equipeProduction}")
    private EquipeProduction equipeProduction;
    private List<YvsProdEquipeProduction> equipes;
    private EmployeEquipe employeEquipe = new EmployeEquipe();

    private List<YvsUsers> users, usersSelect;

    private boolean updateEmployeEquipe;
    private String tabIds = "", input_reset = "", tabIds_employe = "", input_reset_employe = "", tabIds_employe_equipe = "", input_reset_employe_equipe = "";

    YvsProdEquipeProduction entityEquipe;

    private String reference, chefSearch;
    private Boolean principalSearch, actifSearch;
    private long depotSearch;
    private int siteSearch;

    public ManagedEquipeProduction() {
        users = new ArrayList<>();
        usersSelect = new ArrayList<>();
        equipes = new ArrayList<>();
    }

    public List<YvsUsers> getUsersSelect() {
        return usersSelect;
    }

    public void setUsersSelect(List<YvsUsers> usersSelect) {
        this.usersSelect = usersSelect;
    }

    public String getChefSearch() {
        return chefSearch;
    }

    public void setChefSearch(String chefSearch) {
        this.chefSearch = chefSearch;
    }

    public YvsProdEquipeProduction getEntityEquipe() {
        return entityEquipe;
    }

    public void setEntityEquipe(YvsProdEquipeProduction entityEquipe) {
        this.entityEquipe = entityEquipe;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getPrincipalSearch() {
        return principalSearch;
    }

    public void setPrincipalSearch(Boolean principalSearch) {
        this.principalSearch = principalSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public int getSiteSearch() {
        return siteSearch;
    }

    public void setSiteSearch(int siteSearch) {
        this.siteSearch = siteSearch;
    }

    public EquipeProduction getEquipeProduction() {
        return equipeProduction;
    }

    public void setEquipeProduction(EquipeProduction equipeProduction) {
        this.equipeProduction = equipeProduction;
    }

    public List<YvsProdEquipeProduction> getEquipes() {
        return equipes;
    }

    public void setEquipes(List<YvsProdEquipeProduction> equipes) {
        this.equipes = equipes;
    }

    public String getTabIds_employe_equipe() {
        return tabIds_employe_equipe;
    }

    public void setTabIds_employe_equipe(String tabIds_employe_equipe) {
        this.tabIds_employe_equipe = tabIds_employe_equipe;
    }

    public String getInput_reset_employe_equipe() {
        return input_reset_employe_equipe;
    }

    public void setInput_reset_employe_equipe(String input_reset_employe_equipe) {
        this.input_reset_employe_equipe = input_reset_employe_equipe;
    }

    public EmployeEquipe getEmployeEquipe() {
        return employeEquipe;
    }

    public void setEmployeEquipe(EmployeEquipe employeEquipe) {
        this.employeEquipe = employeEquipe;
    }

    public List<YvsUsers> getUsers() {
        return users;
    }

    public void setUsers(List<YvsUsers> users) {
        this.users = users;
    }

    public boolean isUpdateEmployeEquipe() {
        return updateEmployeEquipe;
    }

    public void setUpdateEmployeEquipe(boolean updateEmployeEquipe) {
        this.updateEmployeEquipe = updateEmployeEquipe;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getInput_reset() {
        return input_reset;
    }

    public void setInput_reset(String input_reset) {
        this.input_reset = input_reset;
    }

    public String getTabIds_employe() {
        return tabIds_employe;
    }

    public void setTabIds_employe(String tabIds_employe) {
        this.tabIds_employe = tabIds_employe;
    }

    public String getInput_reset_employe() {
        return input_reset_employe;
    }

    public void setInput_reset_employe(String input_reset_employe) {
        this.input_reset_employe = input_reset_employe;
    }

    @Override
    public void loadAll() {
        loadAll(true, true);
        loadAllEmploye();
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.site.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        equipes = paginator.executeDynamicQuery("YvsProdEquipeProduction", "y.reference", avance, init, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsProdEquipeProduction> re = paginator.parcoursDynamicData("YvsProdEquipeProduction", "y", "y.reference", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void init(boolean init) {
        loadAll(init, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAll(true, true);
    }

    public void loadAllEmploye() {
        users = dao.loadNameQueries("YvsGrhEmployes.findAll", new String[]{"agence"}, new Object[]{currentAgence});
    }

    public void loadAllEqFromPlanif() {
        equipes = dao.loadNameQueries("YvsProdEquipeProduction.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllEquipeProduction() {
        if (autoriser("prod_view_all_equipe")) {
            if (autoriser("prod_view_all_site")) {
                equipes = dao.loadNameQueries("YvsProdEquipeProduction.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            } else {
                //Trouve mon site. et charge selement les equipes de ce site
                YvsProdSiteProduction s = (YvsProdSiteProduction) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findSiteByEquipeActifByUser", new String[]{"user"}, new Object[]{currentUser.getUsers()});
                if (s != null) {
                    equipes = dao.loadNameQueries("YvsProdEquipeProduction.findBySite", new String[]{"site"}, new Object[]{s});
                } else {
                    equipes = dao.loadNameQueries("YvsProdEquipeProduction.findAll", new String[]{"agence"}, new Object[]{currentAgence.getSociete()});
                }
            }
        } else {
            equipes = dao.loadNameQueries("YvsComCreneauHoraireUsers.findEquipeByUsersDates", new String[]{"date", "users"}, new Object[]{new Date(), currentUser.getUsers()});
        }
    }

    @Override
    public EquipeProduction recopieView() {
        return equipeProduction;
    }

    @Override
    public boolean controleFiche(EquipeProduction bean) {
        if (bean.getNom() == null || bean.getNom().equals("")) {
            getErrorMessage("Vous devez entrer le nom");
            return false;
        }
        if (bean.getSite().getId() <= 0) {
            getErrorMessage("Vous devez indiquer le site de rattachement !");
            return false;
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        String action = equipeProduction.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(equipeProduction)) {
                entityEquipe = UtilProd.buildEquipeProduction(equipeProduction, currentUser);
                if (equipeProduction.getId() <= 0) {
                    entityEquipe.setId(null);
                    entityEquipe = (YvsProdEquipeProduction) dao.save1(entityEquipe);
                    equipeProduction.setId(entityEquipe.getId());
                } else {
                    dao.update(entityEquipe);
                }
                int idx = equipes.indexOf(entityEquipe);
                if (idx > -1) {
                    equipes.set(idx, entityEquipe);
                } else {
                    equipes.add(0, entityEquipe);
                }
                succes();
                update("data_equipe_production");
                actionOpenOrResetAfter(this);
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Erreur " + action, ex);
            return false;
        }
        return false;
    }

    @Override
    public void resetFiche() {
        resetFiche(equipeProduction);
        equipeProduction.setSite(new SiteProduction());
        equipeProduction.setChefEquipe(new Employe());
        equipeProduction.setDepot(new Depots());
        equipeProduction.setEmployeEquipeList(new ArrayList<YvsProdMembresEquipe>());
        input_reset = "";
        tabIds = "";
    }

    public void clearData() {
        equipeProduction.setEmployeEquipeList(new ArrayList<YvsProdMembresEquipe>());
        update("data_employe_equipe");
    }

    @Override
    public void onSelectObject(YvsProdEquipeProduction y) {
        entityEquipe = y;
        populateView(UtilProd.buildBeanEquipeProduction(y));
    }

    @Override
    public void populateView(EquipeProduction bean) {
        cloneObject(equipeProduction, bean);
        update("blog_form_equipe_production");
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsProdEquipeProduction> list = new ArrayList<>();
                YvsProdEquipeProduction bean;
                for (Long ids : l) {
                    bean = equipes.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                    if (bean.getId() == equipeProduction.getId()) {
                        resetFiche();
                    }
                }
                equipes.removeAll(list);
                succes();
                update("data_equipe_production");
                tabIds = "";

            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void deleteBean_() {
        try {
            if (entityEquipe != null ? entityEquipe.getId() > 0 : false) {
                dao.delete(entityEquipe);
                equipes.remove(entityEquipe);
                if (entityEquipe.getId().equals(equipeProduction.getId())) {
                    resetFiche();
                }
                update("data_equipe_production");
                succes();
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void updateBean() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            if (equipeProduction.getId() > 0) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                entityEquipe = equipes.get(equipes.indexOf(new YvsProdEquipeProduction(id)));
                populateView(UtilProd.buildBeanEquipeProduction(entityEquipe));
                update("blog_form_equipe_production");
            }
        }
    }

    public void deleteBeanEmployeEquipe() {
        try {
            if ((tabIds_employe_equipe != null) ? tabIds_employe_equipe.length() > 0 : false) {
                String[] ids = tabIds_employe_equipe.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        dao.delete(new YvsProdMembresEquipe(id));
                        equipeProduction.getEmployeEquipeList().remove(new YvsProdMembresEquipe(id));
                    }
                    succes();
                    tabIds_employe_equipe = "";
                    update("data_employe_equipe");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void loadOnViewChefEquipe(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsGrhEmployes bean = (YvsGrhEmployes) ev.getObject();
            chooseChefEquipe(UtilGrh.buildBeanSimpleEmploye(bean));
        }
    }

    public void findOneEmploye(String matricule) {
        if (matricule != null) {
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.findEmploye(matricule);
                if (service.getListEmployes().size() == 1) {
                    chooseChefEquipe(UtilGrh.buildBeanSimpleEmploye(service.getListEmployes().get(0)));
                    equipeProduction.getChefEquipe().setError(false);
                } else if (!service.getListEmployes().isEmpty()) {
                    openDialog("dlgChefEquipe");
                    update("data_chef_equipe");
                    equipeProduction.getChefEquipe().setError(false);
                } else {
                    equipeProduction.getChefEquipe().setError(true);
                }
            }
        }
    }

    public void chooseChefEquipe(Employe e) {
        if (e != null) {
            equipeProduction.setChefEquipe(e);
            update("txt_chef_equipe_production");
        }
    }

    public void chooseEmploye() {
        System.err.println("employesSelect : " + usersSelect.size());
        System.err.println("equipeProduction.getId() : " + equipeProduction.getId());
        if (equipeProduction.getId() > 0) {
            if ((usersSelect != null) ? usersSelect.size() > 0 : false) {
                YvsProdMembresEquipe y;
                for (YvsUsers e : usersSelect) {
                    y = new YvsProdMembresEquipe();
                    y.setProducteur(e);
                    y.setDateUpdate(new Date());
                    y.setDateSave(new Date());
                    y.setEquipeProduction(entityEquipe);
                    y.setActif(true);
                    y.setAuthor(currentUser);
                    y.setId(null);
                    y = (YvsProdMembresEquipe) dao.save1(y);
                    equipeProduction.getEmployeEquipeList().add(y);
                }
                succes();
                tabIds_employe = "";
                usersSelect.clear();
                update("data_employe_equipe");
            }
        } else {
            getInfoMessage("Vous devez dabor enregistrer l'equipe !");
        }
    }

    public void deleteMembreEquipe(YvsProdMembresEquipe membre) {
        if (membre.getId() != null ? membre.getId() > 0 : false) {
            dao.delete(membre);
            equipeProduction.getEmployeEquipeList().remove(membre);
        }
    }

    public void chooseSite() {
        if (equipeProduction.getSite() != null ? equipeProduction.getSite().getId() > 0 : false) {
            ManagedSiteProduction w = (ManagedSiteProduction) giveManagedBean(ManagedSiteProduction.class);
            if (w != null) {
                int idx = w.getSites().indexOf(new YvsProdSiteProduction(equipeProduction.getSite().getId()));
                if (idx > -1) {
                    YvsProdSiteProduction y = w.getSites().get(idx);
                    equipeProduction.setSite(UtilProd.buildBeanSiteProduction(y));
                }
            }
        }
    }

    public void chooseDepot() {
        if (equipeProduction.getDepot() != null ? equipeProduction.getDepot().getId() > 0 : false) {
            ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (w != null) {
                int idx = w.getDepots_all().indexOf(new YvsBaseDepots(equipeProduction.getDepot().getId()));
                if (idx > -1) {
                    YvsBaseDepots y = w.getDepots_all().get(idx);
                    equipeProduction.setDepot(UtilCom.buildBeanDepot(y));
                }
            }
        }
    }

    public void activeEmployeEquipe(EmployeEquipe bean) {
        if (bean != null) {
            bean.setActif(!bean.isActif());
            equipeProduction.getEmployeEquipeList().get(equipeProduction.getEmployeEquipeList().indexOf(bean)).setActif(bean.isActif());
            String rq = "UPDATE yvs_prod_employe_equipe SET actif=" + bean.isActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            update("data_employe_equipe");
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsProdEquipeProduction) ev.getObject());
            tabIds = equipes.indexOf((YvsProdEquipeProduction) ev.getObject()) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void clearParams() {
        reference = null;
        chefSearch = null;
        actifSearch = null;
        principalSearch = null;
        depotSearch = 0;
        siteSearch = 0;
        paginator.getParams().clear();
        loadAll(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.reference", "reference", null);
        if (reference != null ? reference.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", reference.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.reference)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nom)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamChef() {
        ParametreRequete p = new ParametreRequete("y.chef_equipe", "chef", null);
        if (chefSearch != null ? reference.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "chef", chefSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.chef_equipe.nomUsers)", "chef", chefSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.chef_equipe.codeUsers)", "chef", chefSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND");
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamPrincipal() {
        ParametreRequete p = new ParametreRequete("y.principal", "principal", principalSearch, "=", "AND");
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamSite() {
        ParametreRequete p = new ParametreRequete("y.site", "site", null);
        if (siteSearch > 0) {
            p = new ParametreRequete("y.site", "site", new YvsProdSiteProduction(siteSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDepot() {
        ParametreRequete p = new ParametreRequete("y.depot", "depot", null);
        if (siteSearch > 0) {
            p = new ParametreRequete("y.depot", "depot", new YvsBaseDepots(depotSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

}
