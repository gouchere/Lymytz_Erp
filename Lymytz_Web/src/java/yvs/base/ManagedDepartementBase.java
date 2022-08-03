/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base;

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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepartement;
import yvs.service.base.param.IYvsBaseDepartement;
import yvs.util.Managed;
import yvs.util.Nodes;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class ManagedDepartementBase extends Managed<Departement, YvsBaseDepartement> implements Serializable {
    
    private Departement departement = new Departement();
    private YvsBaseDepartement entity = new YvsBaseDepartement();
    private List<YvsBaseDepartement> departements, parents;
    
    private String tabIds;
    
    private String codeSearch;
    private Long parentSearch;
    private Boolean actifSearch;
    
    IYvsBaseDepartement service;

    /**
     * Afficher l'arboresence
     */
    private TreeNode root;
    
    public ManagedDepartementBase() {
        departements = new ArrayList<>();
        parents = new ArrayList<>();
    }
    
    public List<YvsBaseDepartement> getParents() {
        return parents;
    }
    
    public void setParents(List<YvsBaseDepartement> parents) {
        this.parents = parents;
    }
    
    public TreeNode getRoot() {
        return root;
    }
    
    public void setRoot(TreeNode root) {
        this.root = root;
    }
    
    public YvsBaseDepartement getEntity() {
        return entity;
    }
    
    public void setEntity(YvsBaseDepartement entity) {
        this.entity = entity;
    }
    
    public String getCodeSearch() {
        return codeSearch;
    }
    
    public void setCodeSearch(String codeSearch) {
        this.codeSearch = codeSearch;
    }
    
    public Departement getDepartement() {
        return departement;
    }
    
    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
    
    public List<YvsBaseDepartement> getDepartements() {
        return departements;
    }
    
    public void setDepartements(List<YvsBaseDepartement> departements) {
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
    public void doNothing() {
        service = (IYvsBaseDepartement) IEntitiSax.createInstance("IYvsBaseDepartement", dao);
    }
    
    @Override
    public void loadAll() {
        try {
            doNothing();
            parents = dao.loadNameQueries("YvsBaseDepartement.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            loadAll(true, true);
            if (departement.getParent() == null) {
                departement.setParent(new Departement());
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadAll(boolean avance, boolean init) {
        try {
            paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
            departements = paginator.executeDynamicQuery("YvsBaseDepartement", "y.intitule", avance, init, dao);
            if (departements != null) {
                createTree(buildNode(departements));
            }
            update("data-departement");
            update("tree-departement");
            update("select-departement_parent");
            update("select-departement_parent_search");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createTree(List<Nodes> l) {
        root = new DefaultTreeNode("--", null);
        for (Nodes n : l) {
            if (n.getIdParent() == 0) {
                root.getChildren().add(n);
            } else {
                //cherche le noeud parent de n
                if (l.contains(new Nodes(n.getIdParent(), n.getIdParent()))) {
                    l.get(l.indexOf(new Nodes(n.getIdParent(), null))).getChildren().add(n);
                }
            }
        }
    }
    
    private List<Nodes> buildNode(List<YvsBaseDepartement> l) {
        List<Nodes> r = new ArrayList<>();
        for (YvsBaseDepartement d : l) {
            Nodes n = new Nodes(d.getId(), new Departement(d.getId(), d.getIntitule()));
            n.setIdParent(d.getDepartementParent() != null ? d.getDepartementParent().getId() : 0);
            r.add(n);
        }
        return r;
    }
    
    public void parcoursInAllResult(boolean avancer) {
        try {
            setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
            if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
                setOffset(0);
            }
            List<YvsBaseDepartement> re = paginator.parcoursDynamicData("YvsBaseDepartement", "y", "y.intitule", getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectObject(re.get(0));
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean controleFiche(Departement bean) {
        try {
            if (bean == null) {
                getErrorMessage("Action impossible!!!");
                return false;
            }
            if (!Util.asString(bean.getCodeDepartement())) {
                getErrorMessage("Vous devez precisez le code du département");
                return false;
            }
            if (!Util.asString(bean.getIntitule())) {
                getErrorMessage("Vous devez precisez le libelle du département");
                return false;
            }
            if (bean.getParent() != null ? bean.getParent().getId() > 0 ? bean.getParent().getId() == bean.getId() : false : false) {
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
    public Departement recopieView() {
        return departement;
    }
    
    @Override
    public void populateView(Departement bean) {
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
            YvsBaseDepartement result = saveNew(departement);
            if (result != null ? result.getId() > 0 : false) {
                entity = result;
                departement.setId(entity.getId());
                int index = departements.indexOf(entity);
                if (index > -1) {
                    departements.set(index, entity);
                } else {
                    departements.add(entity);
                }
                index = parents.indexOf(entity);
                if (index > -1) {
                    parents.set(index, entity);
                } else {
                    parents.add(entity);
                }
                succes();
                actionOpenOrResetAfter(this);
                
                update("data-departement");
                update("select-departement_parent");
                update("select-departement_parent_search");
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public YvsBaseDepartement saveNew(Departement departement) {
        YvsBaseDepartement entity = null;
        try {
            if (controleFiche(departement)) {
                entity = UtilBase.buildDepartement(departement, currentUser, currentAgence.getSociete());
                ResultatAction<YvsBaseDepartement> result;
                if (entity.getId() < 1) {
                    result = service.save(entity);
                } else {
                    result = service.update(entity);
                }
                if (result != null ? result.isResult() : false) {
                    entity = (YvsBaseDepartement) result.getData();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return entity;
    }
    
    @Override
    public void resetFiche() {
        try {
            departement = new Departement();
            departement.setParent(new Departement());
            entity = new YvsBaseDepartement();
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
                        YvsBaseDepartement y = departements.get(index);
                        dao.delete(y);
                        departements.remove(y);
                        parents.remove(y);
                        if (departement.getId() == y.getId()) {
                            resetFiche();
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
                    parents.remove(entity);
                    if (departement.getId() == entity.getId()) {
                        resetFiche();
                    }
                    succes();
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
    public void onSelectObject(YvsBaseDepartement y) {
        try {
            entity = y;
            populateView(UtilBase.buildBeanDepartement(y));
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            onSelectObject((YvsBaseDepartement) ev.getObject());
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
            if (departement.getParent() != null ? departement.getParent().getId() > 0 : false) {
                int index = parents.indexOf(new YvsBaseDepartement(departement.getParent().getId()));
                if (index > -1) {
                    departement.setParent(UtilBase.buildSimpleBeanDepartement(parents.get(index)));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedDepartementBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void active(YvsBaseDepartement y) {
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
            Logger.getLogger(ManagedDepartementBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addParamCode() {
        ParametreRequete p = new ParametreRequete("y.codeDepartement", "code", null, "=", "AND");
        if (Util.asString(codeSearch)) {
            p = new ParametreRequete(null, "code", codeSearch, "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeDepartement)", "code", codeSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.intitule)", "intitule", codeSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.abreviation)", "abreviation", codeSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParamFind(p);
        loadAll(true, true);
    }
    
    public void addParamParent() {
        ParametreRequete p = new ParametreRequete("y.departementParent", "parent", null, "=", "AND");
        if (parentSearch != null ? parentSearch > 0 : false) {
            p = new ParametreRequete("y.departementParent", "parent", new YvsBaseDepartement(parentSearch), "=", "AND");
        }
        paginator.addParamFind(p);
        loadAll(true, true);
    }
    
    public void addParamActif() {
        paginator.addParamFind(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAll(true, true);
    }
}
