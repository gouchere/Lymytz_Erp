/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import yvs.production.UtilProd;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import static yvs.init.Initialisation.USER_DOWNLOAD;
import static yvs.init.Initialisation.USER_DOWNLOAD_LINUX;
import yvs.util.Managed;
import static yvs.util.Managed.isWindows;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedFamilleArticle extends Managed<FamilleArticle, YvsBaseFamilleArticle> implements Serializable {

    @ManagedProperty(value = "#{familleArticle}")
    private FamilleArticle familleArticle;
    private List<YvsBaseFamilleArticle> familles, parents, famillesLoad;
    private YvsBaseFamilleArticle selectFamille = new YvsBaseFamilleArticle();

    private String tabIds;
    private UploadedFile file;

    private TreeNode root;
    private TreeNode selectedNode;

    private String numSearch;
    private long parentSearch;
    private Boolean actifSearch;

    private String fusionneTo;
    private List<String> fusionnesBy;

    public ManagedFamilleArticle() {
        familles = new ArrayList<>();
        parents = new ArrayList<>();
        famillesLoad = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
    }

    public String getFusionneTo() {
        return fusionneTo;
    }

    public void setFusionneTo(String fusionneTo) {
        this.fusionneTo = fusionneTo;
    }

    public List<String> getFusionnesBy() {
        return fusionnesBy;
    }

    public void setFusionnesBy(List<String> fusionnesBy) {
        this.fusionnesBy = fusionnesBy;
    }

    public FamilleArticle getFamilleArticle() {
        return familleArticle;
    }

    public void setFamilleArticle(FamilleArticle familleArticle) {
        this.familleArticle = familleArticle;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public long getParentSearch() {
        return parentSearch;
    }

    public void setParentSearch(long parentSearch) {
        this.parentSearch = parentSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public YvsBaseFamilleArticle getSelectFamille() {
        return selectFamille;
    }

    public void setSelectFamille(YvsBaseFamilleArticle selectFamille) {
        this.selectFamille = selectFamille;
    }

    public List<YvsBaseFamilleArticle> getFamillesLoad() {
        return famillesLoad;
    }

    public void setFamillesLoad(List<YvsBaseFamilleArticle> famillesLoad) {
        this.famillesLoad = famillesLoad;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public List<YvsBaseFamilleArticle> getParents() {
        return parents;
    }

    public void setParents(List<YvsBaseFamilleArticle> parents) {
        this.parents = parents;
    }

    public List<YvsBaseFamilleArticle> getFamilles() {
        return familles;
    }

    public void setFamilles(List<YvsBaseFamilleArticle> familles) {
        this.familles = familles;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    @Override
    public void loadAll() {
        loadAll(true);
    }

    public void loadAll(boolean load) {
        tabIds = "";
        loadFamille(true, true);
        if (load) {
            root = buildTreeFamille();
        }
        loadParent(selectFamille);
    }

    public void loadFamille(boolean avance, boolean init) {
        if (familleArticle.getParentFamille() == null) {
            familleArticle.setParentFamille(new FamilleArticle());
        }
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        familles = paginator.executeDynamicQuery("YvsBaseFamilleArticle", "y.referenceFamille", avance, init, (int) imax, dao);
    }

    public void loadFamilleByActif(Boolean actif) {
        imax = 0;
        actifSearch = actif;
        addParamActif();
    }

    public void loadAllActif() {
        familles = dao.loadNameQueries("YvsBaseFamilleArticle.findActif", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAlls() {
        familles = dao.loadNameQueries("YvsBaseFamilleArticle.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadParent(YvsBaseFamilleArticle y) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            champ = new String[]{"societe", "id"};
            val = new Object[]{currentAgence.getSociete(), y.getId()};
            nameQueri = "YvsBaseFamilleArticle.findByNotId";
        } else {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            nameQueri = "YvsBaseFamilleArticle.findAll";
        }
        parents = dao.loadNameQueries(nameQueri, champ, val);
        update("select_parent_famille_article");
    }

    @Override
    public FamilleArticle recopieView() {
        if (familleArticle.getParentFamille() != null ? familleArticle.getParentFamille().getId() > 0 : false) {
            int idx = parents.indexOf(new YvsBaseFamilleArticle(familleArticle.getParentFamille().getId()));
            if (idx > -1) {
                familleArticle.setParentFamille(UtilProd.buildBeanFamilleArticle(parents.get(idx)));
            }
        }
        return familleArticle;
    }

    @Override
    public void populateView(FamilleArticle bean) {
        cloneObject(familleArticle, bean);
    }

    @Override
    public boolean controleFiche(FamilleArticle bean) {
        if (bean.getReference() == null || bean.getReference().trim().equals("")) {
            getErrorMessage("Vous devez entrer la reference");
            return false;
        }
        if (!Util.asString(bean.getPrefixe())) {
            bean.setPrefixe(bean.getDesignation().length() > 3 ? bean.getDesignation().substring(0, 3) : bean.getDesignation());
        }
        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(familleArticle);
        familleArticle.setParentFamille(new FamilleArticle());
        selectFamille = new YvsBaseFamilleArticle();
        loadParent(selectFamille);
        tabIds = "";
        update("blog_form_famille_article");
    }

    @Override
    public boolean saveNew() {
        try {
            FamilleArticle bean = recopieView();
            if (controleFiche(bean)) {
                YvsBaseFamilleArticle y = UtilProd.buildFamilleArticle(bean, currentUser, currentAgence.getSociete());
                if (!bean.isUpdate()) {
                    y.setId(null);
                    y = (YvsBaseFamilleArticle) dao.save1(y);
                    familles.add(0, y);
                } else {
                    dao.update(y);
                    familles.set(familles.indexOf(y), y);
                }
                root = buildTreeFamille();
                actionOpenOrResetAfter(this);
                succes();
                update("list_famille_article");
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            getException("Lymytz Error... ", ex);
            return false;
        }
    }

    @Override
    public void deleteBean() {
        try {
            System.err.println("tabIds " + tabIds);

            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBaseFamilleArticle> list = new ArrayList<>();
                YvsBaseFamilleArticle bean;
                for (Long ids : l) {
                    bean = familles.get(ids.intValue());
                    System.err.println("bean " + bean.getDesignation());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);

                }
                familles.removeAll(list);
                root = buildTreeFamille();
                resetFiche();
                succes();
                update("list_famille_article");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
        tabIds = "";
    }

    public void deleteBean_(YvsBaseFamilleArticle y, boolean open) {
        selectFamille = y;
        if (!open) {
            deleteBean_();
        }
    }

    public void deleteBean_() {
        try {
            if (selectFamille != null) {
                dao.delete(selectFamille);
                familles.remove(selectFamille);
                root = buildTreeFamille();
                resetFiche();
                succes();
                update("data_famille_article");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void selectOnView(YvsBaseFamilleArticle y) {
        selectFamille = y;
        loadParent(y);
        populateView(UtilProd.buildBeanFamilleArticle(y));
        update("blog_form_famille_article");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseFamilleArticle f = (YvsBaseFamilleArticle) ev.getObject();
            selectOnView(f);
            execute("onselectLine('famille_article')");
            tabIds = familles.indexOf(f) + "";
            execute("oncollapsesForm('famille_article')");
            System.err.println("tabIds " + tabIds);

        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void onNodeSelect(NodeSelectEvent ev) {
        YvsBaseFamilleArticle f = (YvsBaseFamilleArticle) ev.getTreeNode().getData();
        selectOnView(f);
    }

    public TreeNode buildTreeFamille() {
        TreeNode r = new DefaultTreeNode(new YvsBaseFamilleArticle((long) 0, "Familles Articles", "Items"), null);
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        List<YvsBaseFamilleArticle> l = dao.loadNameQueries("YvsBaseFamilleArticle.findParent", champ, val);
        for (YvsBaseFamilleArticle f : l) {
            TreeNode fils = new DefaultTreeNode(new YvsBaseFamilleArticle(f.getId(), f.getReferenceFamille(), f.getDesignation()), r);
            completeTreeFamille(f, fils);
        }
        return r;
    }

    private void completeTreeFamille(YvsBaseFamilleArticle parent, TreeNode root) {
        champ = new String[]{"parent"};
        val = new Object[]{parent};
        List<YvsBaseFamilleArticle> l = dao.loadNameQueries("YvsBaseFamilleArticle.findByParent", champ, val);
        if (l != null ? !l.isEmpty() : false) {
            for (YvsBaseFamilleArticle f : l) {
                if (!f.equals(parent)) {
                    TreeNode fils = new DefaultTreeNode(new YvsBaseFamilleArticle(f.getId(), f.getReferenceFamille(), f.getDesignation()), root);
                    completeTreeFamille(f, fils);
                }
            }
        }
    }

    public void init(boolean next) {
        loadFamille(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadFamille(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadFamille(true, true);
    }

    public void active(YvsBaseFamilleArticle art) {
        if (art != null) {
            art.setActif(!art.getActif());
            art.setAuthor(currentUser);
            dao.update(art);
            familles.set(familles.indexOf(art), art);
        }
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.referenceFamille", "reference", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.referenceFamille)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.prefixe)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadFamille(true, true);
    }

    public void addParamActif_(Boolean actif) {
        this.actifSearch=actif;
        addParamActif();
    }
    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadFamille(true, true);
    }

    public void addParamParent() {
        ParametreRequete p = new ParametreRequete("y.familleParent", "parent", null, "=", "AND");
        if (parentSearch > 0) {
            p = new ParametreRequete("y.familleParent", "parent", new YvsBaseFamilleArticle(parentSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadFamille(true, true);
    }

    public void uploadFile(FileUploadEvent event) {
        if (event != null) {
            String path = (isWindows() ? USER_DOWNLOAD : USER_DOWNLOAD_LINUX);
            try {
                file = event.getFile();
                File f = Util.createRessource(file);
                if (f != null) {
                    try {
                        buildData(f.getAbsolutePath());
                    } finally {
                        f.delete();
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(ManagedFournisseur.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        update("txt_name_file");
    }

    private void buildData(String path) {
        famillesLoad.clear();
        if (new File(path).exists()) {
            List<List<Object>> data = readFileXLS(path);
            if (data != null) {
                if (!data.isEmpty()) {
                    List<Object> head = data.get(0);
                    if (head != null) {
                        if (!head.isEmpty()) {
                            for (int i = 1; i < data.size(); i++) {
                                YvsBaseFamilleArticle c = new YvsBaseFamilleArticle((long) famillesLoad.size());
                                if (data.get(i).size() > 0) {
                                    c.setReferenceFamille((String) data.get(i).get(0));
                                    if (data.get(i).size() > 1) {
                                        c.setDesignation((String) data.get(i).get(1));
                                    }
                                    champ = new String[]{"societe", "code"};
                                    val = new Object[]{currentAgence.getSociete(), c.getReferenceFamille()};
                                    nameQueri = "YvsBaseFamilleArticle.findByReference";
                                    List<YvsBaseFamilleArticle> l = dao.loadNameQueries(nameQueri, champ, val);
                                    c.setNew_(l != null ? !l.isEmpty() : false);
                                    famillesLoad.add(c);
                                }
                            }
                            openDialog("dlgLoadFamilles");
                            update("data_familles_article_load_");
                        } else {
                            getErrorMessage("Fichier Vide");
                        }
                    } else {
                        getErrorMessage("Fichier Incorrect");
                    }
                } else {
                    getErrorMessage("Fichier Vide");
                }
            } else {
                getErrorMessage("Fichier Incorrect");
            }
        } else {
            getErrorMessage("Le fichier n'existe pas");
        }
    }

    public void fullFamilles() {
        if (!famillesLoad.isEmpty()) {
            for (YvsBaseFamilleArticle t : famillesLoad) {
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    t.setAuthor(currentUser);
                }
                champ = new String[]{"societe", "code"};
                val = new Object[]{currentAgence.getSociete(), t.getReferenceFamille()};
                nameQueri = "YvsBaseFamilleArticle.findByReference";
                List<YvsBaseFamilleArticle> l = dao.loadNameQueries(nameQueri, champ, val);
                if (l != null ? l.isEmpty() : true) {
                    t.setSociete(currentAgence.getSociete());
                    t.setAuthor(currentUser);
                    t.setId(null);
                    t = (YvsBaseFamilleArticle) dao.save1(t);
                    familles.add(t);
                } else {
                    YvsBaseFamilleArticle t_ = l.get(0);
                    if (t_.getDesignation() != null ? t_.getDesignation().trim().equals("") : true) {
                        t_.setDesignation(t.getDesignation());
                        t_.setAuthor(currentUser);
                        dao.update(t_);
                        if (familles.contains(t_)) {
                            familles.set(familles.indexOf(t_), t_);
                        }
                    }
                }
            }
            root = buildTreeFamille();
            update("list_famille_article");
            succes();
        }
        famillesLoad.clear();
    }

    public void fusionner(boolean fusionne) {
        try {
            if (!autoriser("base_user_fusion")) {
                openNotAcces();
                return;
            }
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty() ? ids.size() > 1 : false) {
                long newValue = familles.get(ids.get(0)).getId();
                if (!fusionne) {
                    String oldValue = "0";
                    for (int i : ids) {
                        if (familles.get(i).getId() != newValue) {
                            oldValue += "," + familles.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_base_famille_article", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                familles.remove(new YvsBaseFamilleArticle(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = familles.get(idx).getDesignation();
                    } else {
                        YvsBaseFamilleArticle c = (YvsBaseFamilleArticle) dao.loadOneByNameQueries("YvsBaseFamilleArticle.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getDesignation();
                        }
                    }
                    YvsBaseFamilleArticle c;
                    for (int i : ids) {
                        long oldValue = familles.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(familles.get(i).getDesignation());
                            }
                        } else {
                            c = (YvsBaseFamilleArticle) dao.loadOneByNameQueries("YvsBaseFamilleArticle.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getDesignation());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 familles");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Action impossible");
            getException("fusionner ", ex);
        }
    }
}
