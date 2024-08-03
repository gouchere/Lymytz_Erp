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
import yvs.entity.production.base.YvsBaseArticleSite;
import yvs.production.UtilProd;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.entity.produits.YvsBaseArticles;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedSiteProduction extends Managed<SiteProduction, YvsProdSiteProduction> implements Serializable {

    @ManagedProperty(value = "#{siteProduction}")
    private SiteProduction siteProduction;
    private List<YvsProdSiteProduction> sites;
    private YvsProdSiteProduction selectSite;

    List<YvsBaseArticles> articles;

    private String tabIds;
    private String fusionneTo;
    private List<String> fusionnesBy;

    private String numSearch, articleContenu;

    public ManagedSiteProduction() {
        sites = new ArrayList<>();
        articles = new ArrayList<>();
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public String getArticleContenu() {
        return articleContenu;
    }

    public void setArticleContenu(String articleContenu) {
        this.articleContenu = articleContenu;
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

    public SiteProduction getSiteProduction() {
        return siteProduction;
    }

    public void setSiteProduction(SiteProduction siteProduction) {
        this.siteProduction = siteProduction;
    }

    public List<YvsProdSiteProduction> getSites() {
        return sites;
    }

    public void setSites(List<YvsProdSiteProduction> sites) {
        this.sites = sites;
    }

    public YvsProdSiteProduction getSelectSite() {
        return selectSite;
    }

    public void setSelectSite(YvsProdSiteProduction selectSite) {
        this.selectSite = selectSite;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        if (autoriser("prod_view_all_site")) {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            sites = dao.loadNameQueries("YvsProdSiteProduction.findAll", champ, val);
        } else {
            sites = dao.loadNameQueries("YvsComCreneauHoraireUsers.findSitesByUsersActif", new String[]{"users"}, new Object[]{currentUser.getUsers()});
        }
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        sites = paginator.executeDynamicQuery("YvsProdSiteProduction", "y.reference", avance, init, (int) imax, dao);
        if (sites != null ? sites.size() == 1 : false) {
            onSelectObject(sites.get(0));
            execute("collapseForm('site_production')");
        } else {
            execute("collapseList('site_production')");
        }
    }

    public void init() {
        articles = dao.loadNameQueries("YvsBaseArticles.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        loadAll(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsProdSiteProduction> re = paginator.parcoursDynamicData("YvsProdSiteProduction", "y", "y.reference", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void init(boolean next) {
        loadAll(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAll(true, true);
    }

    @Override
    public boolean controleFiche(SiteProduction bean) {
        if (bean.getReference() == null || bean.getReference().trim().length() < 1) {
            getErrorMessage("Vous devez preciser la reference");
            return false;
        }
        champ = new String[]{"reference", "societe"};
        val = new Object[]{bean.getReference(), currentAgence.getSociete()};
        YvsProdSiteProduction y = (YvsProdSiteProduction) dao.loadOneByNameQueries("YvsProdSiteProduction.findByReference", champ, val);
        if (y != null ? (y.getId() != null ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja inseré ce site de production");
            return false;
        }
        return true;
    }

    @Override
    public SiteProduction recopieView() {
        return siteProduction;
    }

    @Override
    public void populateView(SiteProduction bean) {
        cloneObject(siteProduction, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(siteProduction);
        siteProduction.setArticles(new ArrayList<YvsBaseArticles>());
        tabIds = "";
        selectSite = new YvsProdSiteProduction();
    }

    @Override
    public boolean saveNew() {
        String action = siteProduction.getId() > 0 ? "Modification" : "Insertion";
        try {
            SiteProduction bean = recopieView();
            if (controleFiche(bean)) {
                selectSite = UtilProd.buildSiteProduction(bean, currentAgence, currentUser);
                if (selectSite.getId() != null ? selectSite.getId() > 0 : false) {
                    dao.update(selectSite);
                } else {
                    selectSite.setId(null);
                    selectSite = (YvsProdSiteProduction) dao.save1(selectSite);
                    siteProduction.setId(selectSite.getId());
                }
                int idx = sites.indexOf(selectSite);
                if (idx > -1) {
                    sites.set(idx, selectSite);
                } else {
                    sites.add(0, selectSite);
                    if (sites.size() > imax) {
                        sites.remove(sites.size() - 1);
                    }
                }
                succes();
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
            return false;
        }
    }

    public void saveArticleSite(YvsBaseArticles y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (y.getOther() > 0) {
                    dao.delete(new YvsBaseArticleSite(y.getOther()));
                } else {
                    YvsBaseArticleSite a = new YvsBaseArticleSite();
                    a.setArticle(y);
                    a.setAuthor(currentUser);
                    a.setSite(new YvsProdSiteProduction(siteProduction.getId()));

                    dao.save(a);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error (saveArticleSite) :", ex);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (tabIds != null ? tabIds.trim().length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsProdSiteProduction> list = new ArrayList<>();
                YvsProdSiteProduction bean;
                for (Long ids : l) {
                    bean = sites.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);

                    if (bean.getId() == siteProduction.getId()) {
                        resetFiche();
                    }
                }
                sites.removeAll(list);
                succes();
                tabIds = "";
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBean_(YvsProdSiteProduction y) {
        selectSite = y;
    }

    public void deleteBean_() {
        try {
            if (selectSite != null ? selectSite.getId() > 0 : false) {
                dao.delete(selectSite);
                int idx = sites.indexOf(selectSite);
                if (idx > -1) {
                    sites.remove(idx);
                }
                if (selectSite.getId() == siteProduction.getId()) {
                    resetFiche();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectObject(YvsProdSiteProduction y) {
        selectSite = y;
        populateView(UtilProd.buildBeanSiteProduction(y));
        siteProduction.setArticles(new ArrayList<>(articles));
        update("form_site_production");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsProdSiteProduction) ev.getObject());
            tabIds = sites.indexOf((YvsProdSiteProduction) ev.getObject()) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public boolean checkExist(YvsBaseArticles a) {
        try {
            if ((a != null ? a.getId() > 0 : false) && (siteProduction != null ? siteProduction.getId() > 0 : false)) {
                a.setOther(0);
                Integer id = (Integer) dao.loadObjectByNameQueries("YvsBaseArticleSite.findIdByArticleSite", new String[]{"article", "site"}, new Object[]{a, new YvsProdSiteProduction(siteProduction.getId())});
                if (id != null ? id > 0 : false) {
                    a.setOther(id);
                    return true;
                }
            }
        } catch (Exception ex) {
            getException("Error checkExist ", ex);
        }
        return false;
    }

    public void findContenusByArticle() {
        siteProduction.getArticles().clear();
        if (Util.asString(articleContenu)) {
            for (YvsBaseArticles c : articles) {
                if (c.getRefArt().startsWith(articleContenu) || c.getDesignation().startsWith(articleContenu)) {
                    siteProduction.getArticles().add(c);
                }
            }
        } else {
            siteProduction.getArticles().addAll(articles);
        }
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete(null, "reference", null, "=", "AND");
        ParametreRequete p0;
        ParametreRequete p1;
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p0 = new ParametreRequete("UPPER(y.reference)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR");
            p1 = new ParametreRequete("UPPER(y.designation)", "libelle", numSearch.toUpperCase() + "%", "LIKE", "OR");
            p.setObjet("XXX");
            p.getOtherExpression().add(p0);
            p.getOtherExpression().add(p1);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty() ? ids.size() > 1 : false) {
                int newValue = sites.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (sites.get(i).getId() != newValue) {
                            oldValue += "," + sites.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_prod_site_production", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Integer id = Integer.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                sites.remove(new YvsProdSiteProduction(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = sites.get(idx).getDesignation();
                    } else {
                        YvsProdSiteProduction c = (YvsProdSiteProduction) dao.loadOneByNameQueries("YvsProdSiteProduction.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getDesignation();
                        }
                    }
                    YvsProdSiteProduction c;
                    for (int i : ids) {
                        long oldValue = sites.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(sites.get(i).getDesignation());
                            }
                        } else {
                            c = (YvsProdSiteProduction) dao.loadOneByNameQueries("YvsProdSiteProduction.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getDesignation());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 unites");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Action impossible");
            getException("fusionner ", ex);
        }
    }
}
