/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.proj.param;

import yvs.base.*;
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
import yvs.dao.Options;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepartement;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.proj.YvsProjDepartement;
import yvs.proj.UtilProj;
import yvs.service.proj.IYvsProjDepartement;
import yvs.users.acces.AccesModule;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class ManagedDepartementProj extends Managed<DepartementProj, YvsProjDepartement> implements Serializable {

    private DepartementProj departement = new DepartementProj();
    private YvsProjDepartement entity = new YvsProjDepartement();
    private List<YvsProjDepartement> departements;
    private List<YvsBaseDepartement> parents;

    private String tabIds;

    private String codeSearch;
    private Long parentSearch;
    private Boolean actifSearch;

    IYvsProjDepartement service;
    List<Object[]> result;

    public ManagedDepartementProj() {
        departements = new ArrayList<>();
        parents = new ArrayList<>();
    }

    public List<YvsBaseDepartement> getParents() {
        return parents;
    }

    public void setParents(List<YvsBaseDepartement> parents) {
        this.parents = parents;
    }

    public YvsProjDepartement getEntity() {
        return entity;
    }

    public void setEntity(YvsProjDepartement entity) {
        this.entity = entity;
    }

    public String getCodeSearch() {
        return codeSearch;
    }

    public void setCodeSearch(String codeSearch) {
        this.codeSearch = codeSearch;
    }

    public DepartementProj getDepartement() {
        return departement;
    }

    public void setDepartement(DepartementProj departement) {
        this.departement = departement;
    }

    public List<YvsProjDepartement> getDepartements() {
        return departements;
    }

    public void setDepartements(List<YvsProjDepartement> departements) {
        this.departements = departements;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public Long getParentSearch() {
        return parentSearch;
    }

    public void setParentSearch(Long parentSearch) {
        this.parentSearch = parentSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    @Override
    public void loadAll() {
        try {
            service = (IYvsProjDepartement) IEntitiSax.createInstance("IYvsProjDepartement", dao);

            parents = dao.loadNameQueries("YvsBaseDepartement.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            String query = "SELECT y.id, y.code_departement, y.intitule, y.description, y.actif, p.id, p.code_departement, p.intitule FROM yvs_base_departement y LEFT JOIN yvs_base_departement p ON y.departement_parent = p.id LEFT JOIN yvs_proj_departement d On y.id = d.service WHERE y.societe = ? AND d.id IS NULL";
            result = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
            if (result != null ? !result.isEmpty() : false) {
                openDialog("dlgConfirmAdd");
            }
            loadAll(true, true);
            if (departement.getService().getParent() == null) {
                departement.getService().setParent(new yvs.base.Departement());
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadAll(boolean avance, boolean init) {
        try {
            paginator.addParam(new ParametreRequete("y.service.societe", "societe", currentAgence.getSociete(), "=", "AND"));
            departements = paginator.executeDynamicQuery("YvsProjDepartement", "y.service.intitule", avance, init, 0, dao);
            if (paginator.getParams().size() <= 1) {
                requestAddNotExist(false);
            }
            update("data-departement");
            update("tree-departement");
            update("select-departement_parent");
            update("select-departement_parent_search");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void requestAddNotExist(boolean add) {
        try {
            if (result != null ? !result.isEmpty() : false) {
                YvsBaseDepartement d;
                YvsProjDepartement s;
                for (int i = result.size() - 1; i > -1; i--) {
                    Object[] data = result.get(i);
                    d = new YvsBaseDepartement((Long) data[0]);
                    d.setCodeDepartement((String) data[1]);
                    d.setIntitule((String) data[2]);
                    d.setDescription((String) data[3]);
                    d.setActif((Boolean) data[4]);
                    d.setDepartementParent(new YvsBaseDepartement((Long) data[5], (String) data[6], (String) data[7]));

                    s = new YvsProjDepartement(YvsProjDepartement.ids--);
                    s.setService(d);
                    s.setActif(d.getActif());
                    s.setAuthor(currentUser);
                    if (add) {
                        ResultatAction result = service.save(s);
                        if (result != null ? result.isResult() : false) {
                            s = (YvsProjDepartement) result.getData();
                        }
                        this.result.remove(i);
                    }
                    departements.add(s);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        try {
            setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
            if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
                setOffset(0);
            }
            List<YvsProjDepartement> re = paginator.parcoursDynamicData("YvsProjDepartement", "y", "y.intitule", getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectObject(re.get(0));
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean controleFiche(DepartementProj bean) {
        try {
            if (bean != null ? (bean.getService() != null ? bean.getService().getId() < 1 : true) : true) {
                getErrorMessage("Action impossible!!!");
                return false;
            }
            if (!Util.asString(bean.getService().getCodeDepartement())) {
                getErrorMessage("Vous devez precisez le code du département");
                return false;
            }
            if (!Util.asString(bean.getService().getIntitule())) {
                getErrorMessage("Vous devez precisez le libelle du département");
                return false;
            }
            if (bean.getService().getParent() != null ? bean.getService().getParent().getId() > 0 ? bean.getService().getParent().getId() == bean.getId() : false : false) {
                getErrorMessage("Le département ne peut pas etre sont propre parent");
                return false;
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public DepartementProj recopieView() {
        return departement;
    }

    @Override
    public void populateView(DepartementProj bean) {
        try {
            cloneObject(departement, bean);
            update("form-departement");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean saveNew() {
        try {
            ManagedDepartementBase w = (ManagedDepartementBase) giveManagedBean(ManagedDepartementBase.class);
            if (w != null) {
                YvsBaseDepartement service = w.saveNew(departement.getService());
                if (service != null ? service.getId() < 1 : true) {
                    return false;
                }
                departement.getService().setId(service.getId());
            }
            if (controleFiche(departement)) {
                entity = UtilProj.buildDepartement(departement, currentUser, currentAgence.getSociete());
                ResultatAction<YvsProjDepartement> result;
                if (entity.getId() < 1) {
                    result = service.save(entity);
                } else {
                    result = service.update(entity);
                }
                if (result != null ? result.isResult() : false) {
                    entity = (YvsProjDepartement) result.getData();
                    departement.setId(entity.getId());

                    int index = departements.indexOf(entity);
                    if (index > -1) {
                        departements.set(index, entity);
                    } else {
                        departements.add(entity);
                    }
                    index = parents.indexOf(entity.getService());
                    if (index > -1) {
                        parents.set(index, entity.getService());
                    } else {
                        parents.add(entity.getService());
                    }
                    succes();
                    actionOpenOrResetAfter(this);

                    update("data-departement");
                    update("select-departement_parent");
                    update("select-departement_parent_search");
                    AccesModule acces = (AccesModule) giveManagedBean(AccesModule.class);
                    if (acces != null) {
                        if (acces.isGrh_()) {
                            YvsGrhDepartement e = (YvsGrhDepartement) dao.loadOneByNameQueries("YvsGrhDepartement.findByService", new String[]{"service"}, new Object[]{entity.getService()});
                            if (e != null ? e.getId() < 1 : true) {
                                e = new YvsGrhDepartement();
                                e.setAbreviation(departement.getService().getAbreviation());
                                e.setCodeDepartement(departement.getService().getCodeDepartement());
                                e.setIntitule(departement.getService().getIntitule());
                                e.setDescription(departement.getService().getDescription());
                                e.setActif(departement.getService().isActif());
                                e.setService(entity.getService());
                                e.setAuthor(currentUser);
                                e.setSociete(currentAgence.getSociete());
                                dao.save(e);
                            }
                        }
                    }
                    return true;
                }
                getErrorMessage(result != null ? result.getMessage() : "Action Impossible!!!");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean saveNew(YvsProjDepartement entity) {
        try {
            if (entity != null) {
                int index = departements.indexOf(entity);
                ResultatAction<YvsProjDepartement> result;
                if (entity.getId() < 1) {
                    result = service.save(entity);
                } else {
                    result = service.update(entity);
                }
                if (result != null ? result.isResult() : false) {
                    entity = (YvsProjDepartement) result.getData();
                    if (index > -1) {
                        departements.set(index, entity);
                    } else {
                        departements.add(entity);
                    }
                    for (int i = 0; i < this.result.size(); i++) {
                        Object[] data = this.result.get(i);
                        if (entity.getService().getId().equals((Long) data[0])) {
                            this.result.remove(i);
                            break;
                        }
                    }
                    succes();
                    update("data-departement");
                    return true;
                }
                getErrorMessage(result != null ? result.getMessage() : "Action Impossible!!!");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public void resetFiche() {
        try {
            departement = new DepartementProj();
            entity = new YvsProjDepartement();
            update("form-departement");
            execute("collapseForm('departement')");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (Util.asString(tabIds)) {
                List<Integer> ids = decomposeSelection(tabIds);
                for (Integer index : ids) {
                    try {
                        YvsProjDepartement y = departements.get(index);
                        if (y.getId() > 0) {
                            dao.delete(y);
                            departements.remove(y);
                            if (departement.getId() == y.getId()) {
                                resetFiche();
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
                succes();
            } else {
                if (entity != null ? entity.getId() > 0 : false) {
                    dao.delete(entity);
                    departements.remove(entity);
                    parents.remove(entity.getService());
                    if (departement.getId() == entity.getId()) {
                        resetFiche();
                    }
                    succes();
                } else {
                    getErrorMessage("Ce département n'a pas été ajouté");
                }
            }
            update("data-departement");
            update("select-departement_parent");
            update("select-departement_parent_search");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onSelectObject(YvsProjDepartement y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                entity = y;
                populateView(UtilProj.buildBeanDepartement(y));
            } else {
                getErrorMessage("Ce département n'a pas été ajouté");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            onSelectObject((YvsProjDepartement) ev.getObject());
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public void chooseParent() {
        try {
            if (departement.getService().getParent() != null ? departement.getService().getParent().getId() > 0 : false) {
                int index = parents.indexOf(new YvsBaseDepartement(departement.getService().getParent().getId()));
                if (index > -1) {
                    departement.getService().setParent(UtilBase.buildSimpleBeanDepartement(parents.get(index)));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedDepartementProj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void active(YvsProjDepartement y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setActif(!y.getActif());
                dao.update(y);
                int index = departements.indexOf(y);
                if (index > -1) {
                    departements.set(index, y);
                    update("data-departement");
                    update("select-departement_parent");
                    update("select-departement_parent_search");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedDepartementProj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addParamCode() {
        ParametreRequete p = new ParametreRequete("y.service.codeDepartement", "code", null, "=", "AND");
        if (Util.asString(codeSearch)) {
            p = new ParametreRequete(null, "code", codeSearch, "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.service.codeDepartement)", "code", codeSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.service.intitule)", "intitule", codeSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.service.abreviation)", "abreviation", codeSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParamFind(p);
        loadAll(true, true);
    }

    public void addParamParent() {
        ParametreRequete p = new ParametreRequete("y.service.departementParent", "parent", null, "=", "AND");
        if (parentSearch != null ? parentSearch > 0 : false) {
            p = new ParametreRequete("y.service.departementParent", "parent", new YvsProjDepartement(parentSearch), "=", "AND");
        }
        paginator.addParamFind(p);
        loadAll(true, true);
    }

    public void addParamActif() {
        paginator.addParamFind(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAll(true, true);
    }
}
