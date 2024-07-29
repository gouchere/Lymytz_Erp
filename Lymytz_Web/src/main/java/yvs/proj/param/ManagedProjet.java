/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.proj.param;

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
import yvs.dao.Options;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepartement;
import yvs.entity.proj.YvsProjDepartement;
import yvs.entity.proj.projet.YvsProjProjet;
import yvs.entity.proj.projet.YvsProjProjetService;
import yvs.proj.UtilProj;
import yvs.service.proj.IYvsProjProjet;
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
public class ManagedProjet extends Managed<Projet, YvsProjProjet> implements Serializable {

    private Projet projet = new Projet();
    private YvsProjProjet entity = new YvsProjProjet();
    private List<YvsProjProjet> projets, parents;

    private String tabIds;

    private String codeSearch;
    private Long parentSearch;
    private Boolean actifSearch;

    IYvsProjProjet service;

    /**
     * Afficher l'arboresence
     */
    private TreeNode root;

    public ManagedProjet() {
        projets = new ArrayList<>();
        parents = new ArrayList<>();
    }

    public List<YvsProjProjet> getParents() {
        return parents;
    }

    public void setParents(List<YvsProjProjet> parents) {
        this.parents = parents;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public YvsProjProjet getEntity() {
        return entity;
    }

    public void setEntity(YvsProjProjet entity) {
        this.entity = entity;
    }

    public String getCodeSearch() {
        return codeSearch;
    }

    public void setCodeSearch(String codeSearch) {
        this.codeSearch = codeSearch;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public List<YvsProjProjet> getProjets() {
        return projets;
    }

    public void setProjets(List<YvsProjProjet> projets) {
        this.projets = projets;
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
        service = (IYvsProjProjet) IEntitiSax.createInstance("IYvsProjProjet", dao);
    }

    @Override
    public void loadAll() {
        try {
            doNothing();
            loadAllParent();
            loadAll(true, true);
            if (projet.getParent() == null) {
                projet.setParent(new Projet());
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadAllParent() {
        parents = dao.loadNameQueries("YvsProjProjet.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAll(boolean avance, boolean init) {
        try {
            paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
            if (!autoriser("proj_projet_view")) {
                String query = "SELECT DISTINCT y.id FROM yvs_proj_projet y INNER JOIN yvs_base_users_acces a On y.code_acces = a.code WHERE a.users = ?";
                List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentUser.getUsers().getId(), 1)});
                if (ids != null ? ids.isEmpty() : true) {
                    ids = new ArrayList<Long>() {
                        {
                            add(-1L);
                        }
                    };
                }
                paginator.addParam(new ParametreRequete("y.id", "ids", ids, "IN", "AND"));
            }
            projets = paginator.executeDynamicQuery("YvsProjProjet", "y.libelle", avance, init, dao);
            if (projets != null) {
                createTree(buildNode(projets));
            }
            update("data-projet");
            update("tree-projet");
            update("select-projet_parent");
            update("select-projet_parent_search");
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

    private List<Nodes> buildNode(List<YvsProjProjet> l) {
        List<Nodes> r = new ArrayList<>();
        for (YvsProjProjet d : l) {
            Nodes n = new Nodes(d.getId(), new Projet(d.getId(), d.getLibelle()));
            n.setIdParent(d.getParent() != null ? d.getParent().getId() : 0);
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
            List<YvsProjProjet> re = paginator.parcoursDynamicData("YvsProjProjet", "y", "y.libelle", getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectObject(re.get(0));
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<YvsProjProjetService> loadService(YvsProjProjet y) {
        List<YvsProjProjetService> list = dao.loadNameQueries("YvsProjProjetService.findByProjet", new String[]{"projet"}, new Object[]{y});
        String query = "SELECT y.id, y.code_departement, y.intitule, y.description, y.actif, s.id FROM yvs_base_departement y INNER JOIN yvs_proj_departement s ON s.service = y.id LEFT JOIN yvs_proj_projet_service d On s.id = d.service WHERE y.societe = ? AND d.id IS NULL";
        List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
        if (result != null ? !result.isEmpty() : false) {
            YvsBaseDepartement d;
            YvsProjDepartement s;
            YvsProjProjetService b;
            for (Object[] data : result) {
                d = new YvsBaseDepartement((Long) data[0]);
                d.setCodeDepartement((String) data[1]);
                d.setIntitule((String) data[2]);
                d.setDescription((String) data[3]);
                d.setActif((Boolean) data[4]);

                s = new YvsProjDepartement((Long) data[8]);
                s.setService(d);
                s.setActif(d.getActif());
                s.setAuthor(currentUser);

                b = new YvsProjProjetService(YvsProjProjetService.ids--);
                b.setService(s);
                b.setProjet(y);
                b.setAuthor(currentUser);

                list.add(b);
            }
        }
        return list;
    }

    @Override
    public boolean controleFiche(Projet bean) {
        try {
            if (bean == null) {
                getErrorMessage("Action impossible!!!");
                return false;
            }
            if (!Util.asString(bean.getCode())) {
                getErrorMessage("Vous devez precisez le code du projet");
                return false;
            }
            if (!Util.asString(bean.getLibelle())) {
                getErrorMessage("Vous devez precisez le libelle du projet");
                return false;
            }
            if (bean.getParent() != null ? bean.getParent().getId() > 0 ? bean.getParent().getId() == bean.getId() : false : false) {
                getErrorMessage("Le projet ne peut pas etre sont propre parent");
                return false;
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Projet recopieView() {
        return projet;
    }

    @Override
    public void populateView(Projet bean) {
        try {
            cloneObject(projet, bean);
            update("form-projet");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(projet)) {
                entity = UtilProj.buildProjet(projet, currentUser, currentAgence.getSociete());
                if (projet.getCodeAcces() != null ? projet.getCodeAcces().trim().length() > 0 : false) {
                    entity.setCodeAcces(returnCodeAcces(projet.getCodeAcces()));
                } else {
                    entity.setCodeAcces(null);
                }
                ResultatAction<YvsProjProjet> result;
                boolean save = false;
                if (entity.getId() < 1) {
                    result = service.save(entity);
                    save = true;
                } else {
                    result = service.update(entity);
                }
                if (result != null ? result.isResult() : false) {
                    entity = (YvsProjProjet) result.getData();
                    projet.setId(entity.getId());
                    int index = projets.indexOf(entity);
                    if (index > -1) {
                        projets.set(index, entity);
                    } else {
                        projets.add(entity);
                    }
                    index = parents.indexOf(entity);
                    if (index > -1) {
                        parents.set(index, entity);
                    } else {
                        parents.add(entity);
                    }
                    succes();
                    if (save) {
                        entity.setServices(loadService(entity));
                        projet.setServices(entity.getServices());
                        update("data-projet_services");
                    } else {
                        actionOpenOrResetAfter(this);
                    }
                    update("data-projet");
                    update("select-projet_parent");
                    update("select-projet_parent_search");
                    return true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void addOrRemoveService(YvsProjProjetService y) {
        try {
            if (y != null) {
                int index = projet.getServices().indexOf(y);
                if (y.getId() < 1) {
                    y.setId(null);
                    y = (YvsProjProjetService) dao.save1(y);
                } else {
                    dao.delete(y);
                    y.setId(YvsProjProjetService.ids--);
                }
                if (index > -1) {
                    projet.getServices().set(index, y);
                }
                succes();
                update("data-projet_services");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void resetFiche() {
        try {
            projet = new Projet();
            projet.setParent(new Projet());
            entity = new YvsProjProjet();
            update("form-projet");
            execute("collapseForm('projet')");
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
                        YvsProjProjet y = projets.get(index);
                        dao.delete(y);
                        projets.remove(y);
                        parents.remove(y);
                        if (projet.getId() == y.getId()) {
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
                    projets.remove(entity);
                    parents.remove(entity);
                    if (projet.getId() == entity.getId()) {
                        resetFiche();
                    }
                    succes();
                }
            }
            update("data-projet");
            update("select-projet_parent");
            update("select-projet_parent_search");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onSelectObject(YvsProjProjet y) {
        try {
            entity = y;
            entity.setServices(loadService(y));
            populateView(UtilProj.buildBeanProjet(y));
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            onSelectObject((YvsProjProjet) ev.getObject());
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
            if (projet.getParent() != null ? projet.getParent().getId() > 0 : false) {
                int index = parents.indexOf(new YvsProjProjet(projet.getParent().getId()));
                if (index > -1) {
                    projet.setParent(UtilProj.buildBeanProjet(parents.get(index)));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedProjet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void active(YvsProjProjet y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setActif(!y.getActif());
                dao.update(y);
                int index = projets.indexOf(y);
                if (index > -1) {
                    projets.set(index, y);
                    update("data-projet");
                    update("select-projet_parent");
                    update("select-projet_parent_search");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedProjet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addParamCode() {
        ParametreRequete p = new ParametreRequete("y.code", "code", null, "=", "AND");
        if (Util.asString(codeSearch)) {
            p = new ParametreRequete(null, "code", codeSearch, "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.code)", "code", codeSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.libelle)", "libelle", codeSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.description)", "description", codeSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParamFind(p);
        loadAll(true, true);
    }

    public void addParamParent() {
        ParametreRequete p = new ParametreRequete("y.parent", "parent", null, "=", "AND");
        if (parentSearch != null ? parentSearch > 0 : false) {
            p = new ParametreRequete("y.parent", "parent", new YvsProjProjet(parentSearch), "=", "AND");
        }
        paginator.addParamFind(p);
        loadAll(true, true);
    }

    public void addParamActif() {
        paginator.addParamFind(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAll(true, true);
    }
}
