/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.commercial.depot.PointVente;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.YvsBasePublicites;
import yvs.init.Initialisation;
import yvs.parametrage.societe.UtilSte;
import yvs.production.UtilProd;
import yvs.service.base.produit.IYvsBasePublicites;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedPublicites extends Managed<Publicites, YvsBasePublicites> implements Serializable {

    private Publicites publicite = new Publicites();
    private YvsBasePublicites selectPublicite = new YvsBasePublicites();
    private List<YvsBasePublicites> publicites;

    private String tabIds;

    private String descriptionSearch, articleSearch;
    private Boolean permanentSearch;
    private boolean addDateSearch;
    private Date dateDebutSearch = new Date(), dateFinSearch;

    private String fusionneTo;
    private List<String> fusionnesBy;

    IYvsBasePublicites service;

    public ManagedPublicites() {
        publicites = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
    }

    public Publicites getPublicite() {
        return publicite;
    }

    public void setPublicite(Publicites publicite) {
        this.publicite = publicite;
    }

    public YvsBasePublicites getSelectPublicite() {
        return selectPublicite;
    }

    public void setSelectPublicite(YvsBasePublicites selectPublicite) {
        this.selectPublicite = selectPublicite;
    }

    public List<YvsBasePublicites> getPublicites() {
        return publicites;
    }

    public void setPublicites(List<YvsBasePublicites> publicites) {
        this.publicites = publicites;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getDescriptionSearch() {
        return descriptionSearch;
    }

    public void setDescriptionSearch(String descriptionSearch) {
        this.descriptionSearch = descriptionSearch;
    }

    public String getArticleSearch() {
        return articleSearch;
    }

    public void setArticleSearch(String articleSearch) {
        this.articleSearch = articleSearch;
    }

    public Boolean getPermanentSearch() {
        return permanentSearch;
    }

    public void setPermanentSearch(Boolean permanentSearch) {
        this.permanentSearch = permanentSearch;
    }

    public boolean isAddDateSearch() {
        return addDateSearch;
    }

    public void setAddDateSearch(boolean addDateSearch) {
        this.addDateSearch = addDateSearch;
    }

    public Date getDateDebutSearch() {
        return dateDebutSearch;
    }

    public void setDateDebutSearch(Date dateDebutSearch) {
        this.dateDebutSearch = dateDebutSearch;
    }

    public Date getDateFinSearch() {
        return dateFinSearch;
    }

    public void setDateFinSearch(Date dateFinSearch) {
        this.dateFinSearch = dateFinSearch;
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

    @Override
    public void loadAll() {
        service = (IYvsBasePublicites) IEntitiSax.createInstance("IYvsBasePublicites", dao);
        loadAll(true, true); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        publicites = paginator.executeDynamicQuery("YvsBasePublicites", "y.priorite DESC", avance, init, (int) imax, dao);
        update("data-publicites");
    }

    @Override
    public boolean controleFiche(Publicites bean) {
        if (publicite == null) {
            return false;
        }
        return true;
    }

    @Override
    public void resetFiche() {
        publicite = new Publicites();
        selectPublicite = new YvsBasePublicites();
        update("form-publicites");
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(publicite)) {
                selectPublicite = UtilSte.buildPublicites(publicite, currentAgence.getSociete(), currentUser);
                ResultatAction<YvsBasePublicites> result;
                if (publicite.getId() < 1) {
                    result = service.save(selectPublicite);
                    if (result != null ? result.isResult() : false) {
                        publicite.setId(selectPublicite.getId());
                    }
                } else {
                    result = service.update(selectPublicite);
                }
                if (result == null) {
                    getErrorMessage("Action Impossible!!!");
                    return false;
                } else if (!result.isResult()) {
                    getErrorMessage(result.getMessage());
                    return false;
                }
                int index = publicites.indexOf(selectPublicite);
                if (index > -1) {
                    publicites.set(index, selectPublicite);
                } else {
                    publicites.add(0, selectPublicite);
                }
                update("data-publicites");
                succes();
                return true;
            }
        } catch (Exception ex) {
            getException("ManagedPublicites (saveNew)", ex);
        }
        return false;
    }

    @Override
    public void deleteBean() {
        try {
            List<Integer> indexs = decomposeSelection(tabIds);
            if (indexs != null ? !indexs.isEmpty() : false) {
                YvsBasePublicites y;
                ResultatAction<YvsBasePublicites> result;
                boolean succes = true;
                for (Integer index : indexs) {
                    try {
                        y = publicites.get(index);
                        result = service.delete(y);
                        if (result != null ? !result.isResult() : true) {
                            succes = false;
                        } else if (result.isResult()) {
                            if (publicite.getId() == y.getId()) {
                                resetFiche();
                            }
                            publicites.remove(y);
                        }
                    } catch (Exception ex) {
                        getException("ManagedPublicites (deleteBean)", ex);
                        succes = false;
                    }
                }
                if (succes) {
                    succes();
                }
                update("data-publicites");
            }
        } catch (Exception ex) {
            getException("ManagedPublicites (deleteBean)", ex);
        }
    }

    public void deleteOne() {
        try {
            if (selectPublicite != null ? selectPublicite.getId() > 0 : false) {
                ResultatAction<YvsBasePublicites> result = service.delete(selectPublicite);
                if (result != null ? result.isResult() : false) {
                    if (publicite.getId() == selectPublicite.getId()) {
                        resetFiche();
                    }
                    publicites.remove(selectPublicite);
                    update("data-publicites");
                    succes();
                } else if (result == null) {
                    getErrorMessage("Action Impossible!!!");
                } else {
                    getErrorMessage(result.getMessage());
                }
            }
        } catch (Exception ex) {
            getException("ManagedPublicites (deleteBean)", ex);
        }
    }

    @Override
    public void onSelectObject(YvsBasePublicites y) {
        selectPublicite = y;
        publicite = UtilSte.buildBeanPublicites(y);
        update("form-publicites");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsBasePublicites) ev.getObject());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            chooseArticle(UtilProd.buildBeanArticles((YvsBaseArticles) ev.getObject()));
        }
    }

    public void clearParam() {
        addDateSearch = false;
        descriptionSearch = "";
        articleSearch = "";
        permanentSearch = null;
        paginator.getParams().clear();
        loadAll(true, true);
    }

    public void addParamPermanent() {
        paginator.addParam(new ParametreRequete("y.permanent", "permanent", permanentSearch, "=", "AND"));
        loadAll(true, true);
    }

    public void addParamDescription() {
        ParametreRequete p = new ParametreRequete("y.description", "commentaire", null, "=", "AND");
        if (descriptionSearch != null ? descriptionSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "commentaire", descriptionSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.description)", "description", descriptionSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.url)", "url", descriptionSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamArticle() {
        ParametreRequete p = new ParametreRequete("y.article.article.refArt", "reference", null, "=", "AND");
        if (articleSearch != null ? articleSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", articleSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.article.refArt)", "refArt", articleSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.article.designation)", "designation", articleSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateDebut", "dates", null, "=", "AND");
        if (addDateSearch) {
            p = new ParametreRequete(null, "dates", "current_date", "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.dateDebut", "dateDebut", dateDebutSearch, dateFinSearch, "BETWEEN", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.dateFin", "dateFin", dateDebutSearch, dateFinSearch, "BETWEEN", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void fusionner(boolean fusionne) {
        try {
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                long newValue = publicites.get(ids.get(0)).getId();
                if (!fusionne) {
                    if (!autoriser("base_user_fusion")) {
                        openNotAcces();
                        return;
                    }
                    String oldValue = "0";
                    for (int i : ids) {
                        if (publicites.get(i).getId() != newValue) {
                            oldValue += "," + publicites.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_base_publicites", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                publicites.remove(new YvsBasePublicites(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                    update("data-publicites");
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = resumeText(publicites.get(idx).getDescription(), 10);
                    } else {
                        YvsBasePublicites c = (YvsBasePublicites) dao.loadOneByNameQueries("YvsBasePublicites.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? c.getId() > 0 : false) {
                            fusionneTo = resumeText(c.getDescription(), 10);
                        }
                    }
                    YvsBasePublicites c;
                    for (int i : ids) {
                        long oldValue = publicites.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(resumeText(publicites.get(i).getDescription(), 10));
                            }
                        } else {
                            c = (YvsBasePublicites) dao.loadOneByNameQueries("YvsBasePublicites.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? c.getId() > 0 : false) {
                                fusionnesBy.add(resumeText(c.getDescription(), 10));
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 publicites");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public String imageBean(Publicites u) {
        if (u != null ? Util.asString(u.getImage()) : false) {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String path = ext.getRealPath("resources/lymytz/documents/docEnterprise/") + Initialisation.FILE_SEPARATOR + u.getImage();
            if (new File(path).exists()) {
                return u.getImage();
            }
        }
        return "publicites_1.jpeg";
    }

    public String imageEntity(YvsBasePublicites u) {
        if (u != null ? Util.asString(u.getImage()) : false) {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String path = ext.getRealPath("resources/lymytz/documents/docEnterprise/") + Initialisation.FILE_SEPARATOR + u.getImage();
            if (new File(path).exists()) {
                return u.getImage();
            }
        }
        return "publicites_1.jpeg";
    }

    public void handleFileUpload(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminDocEnterprise().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminDocEnterprise().length());
            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminDocEnterprise();
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
                publicite.setImage(file);
                getInfoMessage("Charger !");
                update("image-publicites");

            } catch (IOException ex) {
                Logger.getLogger(ManagedPublicites.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void chooseArticle(Articles y) {
        if (y.getConditionnements() != null ? !y.getConditionnements().isEmpty() : false) {
            publicite.setArticle(UtilProd.buildSimpleBeanConditionnement(y.getConditionnements().get(0)));
        }
        publicite.getArticle().setArticle(y);
        update("blog-choose_article_publicites");
    }

    public void searchArticle() {
        String num = publicite.getArticle().getArticle().getRefArt();
        publicite.getArticle().getArticle().setDesignation("");
        publicite.getArticle().getArticle().setError(true);
        publicite.getArticle().setId(0);
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            Articles y = m.searchArticleActif("V", num, true);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update("data-article_publicictes");
                } else {
                    chooseArticle(y);
                }
                publicite.getArticle().getArticle().setError(false);
            }
        }
    }

}
