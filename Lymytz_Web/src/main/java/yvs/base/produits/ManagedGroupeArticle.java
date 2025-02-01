/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.File;
import java.io.IOException;
import yvs.production.UtilProd;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.init.Initialisation;
import yvs.parametrage.societe.ManagedSociete;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedGroupeArticle extends Managed<GroupeArticle, YvsBaseGroupesArticle> implements Serializable {

    @ManagedProperty(value = "#{groupeArticle}")
    private GroupeArticle groupeArticle;
    private List<YvsBaseGroupesArticle> groupes, parents;
    private YvsBaseGroupesArticle selectGroupe = new YvsBaseGroupesArticle();

    private String tabIds;

    private TreeNode root;
    private TreeNode selectedNode;

    private long parent;
    private String numSearch;
    private long parentSearch;
    private Boolean actifSearch;

    public ManagedGroupeArticle() {
        groupes = new ArrayList<>();
        parents = new ArrayList<>();
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
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

    public YvsBaseGroupesArticle getSelectGroupe() {
        return selectGroupe;
    }

    public void setSelectGroupe(YvsBaseGroupesArticle selectGroupe) {
        this.selectGroupe = selectGroupe;
    }

    public List<YvsBaseGroupesArticle> getParents() {
        return parents;
    }

    public void setParents(List<YvsBaseGroupesArticle> parents) {
        this.parents = parents;
    }

    public GroupeArticle getGroupeArticle() {
        return groupeArticle;
    }

    public void setGroupeArticle(GroupeArticle groupeArticle) {
        this.groupeArticle = groupeArticle;
    }

    public List<YvsBaseGroupesArticle> getGroupes() {
        return groupes;
    }

    public void setGroupes(List<YvsBaseGroupesArticle> groupes) {
        this.groupes = groupes;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
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

    @Override
    public void loadAll() {
        loadAll(true);
    }

    public void loadAll(boolean load) {
        setImax(100);
        if (groupeArticle.getParent() == null) {
            groupeArticle.setParent(new GroupeArticle());
        }
        tabIds = "";
        loadGroupe(true, true);
        if (load) {
            root = buildTreeFamille();
        }
        loadParent(selectGroupe);
    }

    public void loadGroupe(boolean avance, boolean init) {
        loadGroupe(avance, init, true);
    }

    public void loadGroupe(boolean avance, boolean init, boolean count) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        groupes = paginator.executeDynamicQuery(count, "y", "y", "YvsBaseGroupesArticle y ", "y.refgroupe", avance, init, (int) imax, "id", dao);
        if (parents != null ? parents.isEmpty() : true) {
            parents = new ArrayList<>(groupes);
        }
    }

    public void loadGroupeByActif(Boolean actif) {
        imax = 0;
        actifSearch = actif;
        addParamActif();
    }

    public void loadParent(YvsBaseGroupesArticle y) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            champ = new String[]{"id"};
            val = new Object[]{y.getId()};
            nameQueri = "YvsBaseGroupesArticle.findByNotId";
        } else {
            champ = new String[]{};
            val = new Object[]{};
            nameQueri = "YvsBaseGroupesArticle.findAll";
        }
        parents = dao.loadNameQueries(nameQueri, champ, val);
        update("select_parent_groupe_article");
    }

    @Override
    public GroupeArticle recopieView() {
        if (parent > 0) {
            int idx = parents.indexOf(new YvsBaseGroupesArticle(parent));
            if (idx > -1) {
                YvsBaseGroupesArticle p = parents.get(idx);
                groupeArticle.setParent(new GroupeArticle(p.getId(), p.getRefgroupe(), p.getDesignation()));
            }
        }
        return groupeArticle;
    }

    @Override
    public void populateView(GroupeArticle bean) {
        cloneObject(groupeArticle, bean);
        parent = bean.getParent() != null ? bean.getParent().getId() : 0;
    }

    @Override
    public boolean controleFiche(GroupeArticle bean) {
        if (bean.getReference() == null || bean.getReference().trim().equals("")) {
            getErrorMessage("Vous devez entrer la reference");
            return false;
        }
        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(groupeArticle);
        groupeArticle.setParent(new GroupeArticle());
        selectGroupe = new YvsBaseGroupesArticle();
        parent = 0;
        tabIds = "";
        loadParent(selectGroupe);
        update("blog_form_groupe_article");
    }

    @Override
    public boolean saveNew() {
        try {
            GroupeArticle bean = recopieView();
            if (controleFiche(bean)) {
                selectGroupe = UtilProd.buildGroupeArticle(bean, currentAgence.getSociete(), currentUser);
                if (bean.getId() > 0) {
                    dao.update(selectGroupe);
                } else {
                    selectGroupe.setId(null);
                    selectGroupe = (YvsBaseGroupesArticle) dao.save1(selectGroupe);
                    bean.setId(selectGroupe.getId());
                }
                int idx = groupes.indexOf(selectGroupe);
                if (idx > -1) {
                    groupes.set(idx, selectGroupe);
                } else {
                    groupes.add(0, selectGroupe);
                }
                root = buildTreeFamille();
                actionOpenOrResetAfter(this);
                succes();
                update("tree_groupe_article");
                update("data_groupe_article");
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
            return false;
        }
    }

    @Override
    public void deleteBean() {
        try {
            System.err.println("tabIds = " + tabIds);
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBaseGroupesArticle> list = new ArrayList<>();
                YvsBaseGroupesArticle bean;
                for (Long ids : l) {
                    bean = groupes.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    boolean del = dao.delete(bean);
                    System.err.println("result = " + del);
                }
                groupes.removeAll(list);
                root = buildTreeFamille();
                resetFiche();
                succes();
                update("data_groupe_article");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
        }
        tabIds = "";
    }

    public void deleteBean_(YvsBaseGroupesArticle y, boolean open) {
        selectGroupe = y;
        if (!open) {
            deleteBean_();
        }
    }

    public void deleteBean_() {
        try {
            if (selectGroupe != null) {
                dao.delete(selectGroupe);
                groupes.remove(selectGroupe);
                root = buildTreeFamille();
                resetFiche();
                succes();
                update("data_groupe_article");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectObject(YvsBaseGroupesArticle y) {
        selectGroupe = y;
        loadParent(y);
        populateView(UtilProd.buildBeanGroupeArticle(y));
        update("blog_form_groupe_article");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseGroupesArticle f = (YvsBaseGroupesArticle) ev.getObject();
            onSelectObject(f);
            tabIds = groupes.indexOf(f) + "";
//            execute("onselectLine(" + tabIds + ",'groupe_article')");
            execute("oncollapsesForm('groupe_article')");

        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        tabIds = null;
    }

    public void onNodeSelect(NodeSelectEvent ev) {
        YvsBaseGroupesArticle f = (YvsBaseGroupesArticle) ev.getTreeNode().getData();
        onSelectObject(f);
    }

    public TreeNode buildTreeFamille() {
        TreeNode r = new DefaultTreeNode(new YvsBaseGroupesArticle((long) 0, "Groupes Articles", "Items"), null);
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        List<YvsBaseGroupesArticle> l = dao.loadNameQueries("YvsBaseGroupesArticle.findParent", champ, val);
        for (YvsBaseGroupesArticle f : l) {
            TreeNode fils = new DefaultTreeNode(new YvsBaseGroupesArticle(f.getId(), f.getRefgroupe(), f.getDesignation()), r);
            completeTreeFamille(f, fils);
        }
        return r;
    }

    private void completeTreeFamille(YvsBaseGroupesArticle parent, TreeNode root) {
        champ = new String[]{"parent"};
        val = new Object[]{parent};
        List<YvsBaseGroupesArticle> l = dao.loadNameQueries("YvsBaseGroupesArticle.findByParent", champ, val);
        if (l != null ? !l.isEmpty() : false) {
            for (YvsBaseGroupesArticle f : l) {
                if (!f.equals(parent)) {
                    TreeNode fils = new DefaultTreeNode(new YvsBaseGroupesArticle(f.getId(), f.getRefgroupe(), f.getDesignation()), root);
                    completeTreeFamille(f, fils);
                }
            }
        }
    }

    public void init(boolean next) {
        loadGroupe(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadGroupe(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadGroupe(true, true);
    }

    public void active(YvsBaseGroupesArticle art) {
        if (art != null) {
            art.setActif(!art.getActif());
            art.setAuthor(currentUser);
            dao.update(art);
            groupes.set(groupes.indexOf(art), art);
        }
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.refgroupe", "reference", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.refgroupe)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeAppel)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadGroupe(true, true);
    }

    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadGroupe(true, true);
    }

    public void addParamParent() {
        ParametreRequete p = new ParametreRequete("y.groupeParent", "parent", null, "=", "AND");
        if (parentSearch > 0) {
            p = new ParametreRequete("y.groupeParent", "parent", new YvsBaseGroupesArticle(parentSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadGroupe(true, true);
    }

    public void handleFileUpload(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminArticle().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminArticle().length());            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminArticle();
            String file = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
            try {
                //copie dans le dossier de l'application
                Util.copyFile(file, repDest + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                //copie dans le dossier de sauvegarde
                Util.copySVGFile(file, repDestSVG + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                File f = new File(new StringBuilder(repDest).append(file).toString());
                if (!f.exists()) {
                    File doc = new File(repDest);
                    if (!doc.exists()) {
                        doc.mkdirs();
                        f.createNewFile();
                    } else {
                        f.createNewFile();
                    }
                }
                groupeArticle.setPhoto(file);
                getInfoMessage("Charger !");
                update("photo_groupe_article");

            } catch (IOException ex) {
                Logger.getLogger(ManagedSociete.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static String photo(GroupeArticle u) {
        if (u != null) {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String path = ext.getRealPath("resources/lymytz/documents/docArticle/") + Initialisation.FILE_SEPARATOR + u.getPhoto();
            if (new File(path).exists()) {
                return u.getPhoto();
            }
        }
        return "produits.png";
    }
}
