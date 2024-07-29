/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rrr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.ManagedArticles;
import yvs.commercial.UtilCom;
import yvs.commercial.depot.ManagedPointVente;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseConditionnementPoint;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.rrr.YvsComRabais;
import yvs.entity.produits.YvsBaseArticles;
import yvs.production.UtilProd;
import yvs.service.com.rrr.IYvsComRabais;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz-pc
 */
@ManagedBean
@SessionScoped
public class ManagedRabais extends Managed<Rabais, YvsComRabais> implements Serializable {

    private Rabais rabais = new Rabais();
    private YvsComRabais entity = new YvsComRabais();
    private List<YvsComRabais> listRabais;

    private String tabIds;
    private IYvsComRabais service;

    private Boolean permanentSearch = null, actifSearch = null;
    private boolean addDate = false;
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();
    private String articleSearch;
    private long pointSearch;

    public ManagedRabais() {
        this.listRabais = new ArrayList<>();
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public Boolean getPermanentSearch() {
        return permanentSearch;
    }

    public void setPermanentSearch(Boolean permanentSearch) {
        this.permanentSearch = permanentSearch;
    }

    public boolean isAddDate() {
        return addDate;
    }

    public void setAddDate(boolean addDate) {
        this.addDate = addDate;
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

    public long getPointSearch() {
        return pointSearch;
    }

    public void setPointSearch(long pointSearch) {
        this.pointSearch = pointSearch;
    }

    public Rabais getRabais() {
        return rabais;
    }

    public void setRabais(Rabais rabais) {
        this.rabais = rabais;
    }

    public YvsComRabais getEntity() {
        return entity;
    }

    public void setEntity(YvsComRabais entity) {
        this.entity = entity;
    }

    public List<YvsComRabais> getListRabais() {
        return listRabais;
    }

    public void setListRabais(List<YvsComRabais> listRabais) {
        this.listRabais = listRabais;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public IYvsComRabais getService() {
        return service;
    }

    public void setService(IYvsComRabais service) {
        this.service = service;
    }

    public String getArticleSearch() {
        return articleSearch;
    }

    public void setArticleSearch(String articleSearch) {
        this.articleSearch = articleSearch;
    }

    @Override
    public void loadAll() {
        try {
            service = (IYvsComRabais) IEntitiSax.createInstance("IYvsComRabais", dao);
            loadAll(true, true);
        } catch (Exception ex) {
            getErrorMessage("loadAll Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void loadAll(boolean avance, boolean init) {
        try {
            paginator.addParam(new ParametreRequete("y.article.article.article.famille.societe", "societe", currentAgence.getSociete(), "=", "AND"));
            listRabais = paginator.executeDynamicQuery("y", "y", "YvsComRabais y JOIN FETCH y.article", "y.article.article.article.refArt", avance, init, (int) imax, dao);
        } catch (Exception ex) {
            getErrorMessage("loadAll Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
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

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
            setOffset(0);
        }
        List<YvsComRabais> re = paginator.parcoursDynamicData("YvsComRabais", "y", "y.article.article.article.refArt", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    @Override
    public boolean controleFiche(Rabais bean) {
        try {
            if (bean.getArticle() == null) {
                getErrorMessage("Vous devez selectionner un article");
                return false;
            }
            if (bean.getArticle().getArticle() != null ? bean.getArticle().getArticle().getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner un article");
                return false;
            }
            if (bean.getConditionnement() != null ? bean.getConditionnement().getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner un conditionnement");
                return false;
            }
            if (bean.getPoint() != null ? bean.getPoint().getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner un point de vente");
                return false;
            }
            //vérifie l'existence d'un plan de rabais dans une période qui se chevauche
            champ = new String[]{"article", "unite", "point", "debut", "fin"};
            val = new Object[]{new YvsBaseArticles(bean.getArticle().getArticle().getId()), new YvsBaseConditionnement(bean.getConditionnement().getId()), new YvsBasePointVente(rabais.getPoint().getId()), bean.getDateDebut(), bean.getDateFin()};
            YvsComRabais r = (YvsComRabais) dao.loadOneByNameQueries("YvsComRabais.findOtherByPointArticleUnit", champ, val);
            if (r != null) {
                if (r.getId() != bean.getId()) {
                    getErrorMessage("Vous avez déjà planifié un rabais pour cet article dans une période semblable !");
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage("controleFiche Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public void resetFiche() {
        try {
            rabais = new Rabais();
            entity = new YvsComRabais();
            update("form-rabais");
        } catch (Exception ex) {
            getErrorMessage("resetFiche Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean saveNew() {
        try {
            YvsComRabais y = _saveNew(true);
            if (y != null) {
                entity = y;
                int index = listRabais.indexOf(entity);
                if (index > -1) {
                    listRabais.set(index, entity);
                } else {
                    listRabais.add(0, y);
                }
                if (entity.getId() > 0) {
                    succes();
                }
                actionOpenOrResetAfter(this);
                update("data-rabais");
            }
        } catch (Exception ex) {
            getErrorMessage("saveNew Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public YvsComRabais _saveNew(boolean message) {
        try {
            System.err.println("service : " + service);
            if (service != null && controleFiche(rabais)) {
                YvsComRabais y = UtilCom.buildRabais(rabais, currentUser);
                if (y.getArticle() != null ? y.getArticle().getId() < 1 : true) {
                    //Vérifie si l'article est déjà rattaché au point de vente
                    champ = new String[]{"article", "unite", "point"};
                    val = new Object[]{new YvsBaseArticles(rabais.getArticle().getArticle().getId()), new YvsBaseConditionnement(rabais.getConditionnement().getId()), new YvsBasePointVente(rabais.getPoint().getId())};
                    YvsBaseConditionnementPoint cp = (YvsBaseConditionnementPoint) dao.loadOneByNameQueries("YvsBaseConditionnementPoint.findArticlePointUnite", champ, val);
                    if (cp != null ? cp.getId() < 1 : true) {
                        ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                        //Save le conditionnement dans le point de vente                
                        if (service != null) {
                            service.getArticle().setArticle(rabais.getArticle().getArticle());
                            service.getArticle().setConditionnement(rabais.getConditionnement());
                            service.getArticle().setActif(true);
                            service.setPointVente(rabais.getPoint());
                            service.getArticle().setPuv(rabais.getConditionnement().getPrix());
                            service.getArticle().setRemise(rabais.getConditionnement().getRemise());
                            service.setSelectPoint(UtilCom.buildPointVente(rabais.getPoint(), currentUser, currentAgence));
                            cp = service.saveNewArticle(false);
                        }
                    }
                    y.setArticle(cp);
                }
                ResultatAction<YvsComRabais> result;
                if (y.getId() < 1) {
                    result = service.save(y);
                } else {
                    result = service.update(y);
                }
                if (result != null ? result.isResult() : false) {
                    return (YvsComRabais) result.getData();
                } else {
                    if (message) {
                        getErrorMessage(result != null ? result.getMessage() : "Action impossible");
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("_saveNew Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void deleteBean() {
        try {
            if (service != null && entity != null ? entity.getId() > 0 : false) {
                ResultatAction<YvsComRabais> result = service.delete(entity);
                if (result != null ? result.isResult() : false) {
                    listRabais.remove(entity);
                    update("data-rabais");
                    succes();
                } else {
                    getErrorMessage(result != null ? result.getMessage() : "Action Impossible!!!");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("deleteBean Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteAll() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComRabais> list = new ArrayList<>();
                YvsComRabais bean;
                for (Long ids : l) {
                    bean = listRabais.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);

                    if (bean.getId() == rabais.getId()) {
                        resetFiche();
                    }
                }
                listRabais.removeAll(list);
                update("data-rabais");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("deleteAll Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onSelectObject(YvsComRabais y) {
        try {
            entity = y;
            rabais = UtilCom.buildBeanRabais(y);
            rabais.getArticle().getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{y.getArticle().getArticle().getArticle()}));
            update("form-rabais");
        } catch (Exception ex) {
            getErrorMessage("onSelectObject Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                onSelectObject((YvsComRabais) ev.getObject());
            }
        } catch (Exception ex) {
            getErrorMessage("unLoadOnView Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        try {
            resetFiche();
        } catch (Exception ex) {
            getErrorMessage("unLoadOnView Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void loadOnViewArticle(SelectEvent ev) {
        try {
            if ((ev != null) ? ev.getObject() != null : false) {
                chooseArticle(UtilProd.buildSimpleBeanArticles((YvsBaseArticles) ev.getObject()));
            }
        } catch (Exception ex) {
            getErrorMessage("loadOnViewArticle Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    private void chooseArticle(Articles article) {
        try {
            if (article != null ? article.getId() > 0 : false) {
                article.setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(article.getId())}));
                rabais.getArticle().setArticle(article);
                if (article.getConditionnements() != null ? !article.getConditionnements().isEmpty() : false) {
                    rabais.setConditionnement(UtilProd.buildBeanConditionnement(article.getConditionnements().get(0)));
                }
                update("blog-article_rabais");
                update("select-conditionnement_rabais");
            }
        } catch (Exception ex) {
            getErrorMessage("chooseConditionnement Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void chooseConditionnement() {
        try {
            if (rabais.getConditionnement() != null ? rabais.getConditionnement().getId() > 0 : false) {
                int index = rabais.getArticle().getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(rabais.getConditionnement().getId()));
                if (index > -1) {
                    YvsBaseConditionnement y = rabais.getArticle().getArticle().getConditionnements().get(index);
                    rabais.setConditionnement(UtilProd.buildBeanConditionnement(y));
                }
            }
        } catch (Exception ex) {
            getErrorMessage("chooseConditionnement Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void choosePointVente() {
        try {
            if (rabais.getPoint() != null ? rabais.getPoint().getId() > 0 : false) {
                ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                if (service != null) {
                    int index = service.getPointsvente().indexOf(new YvsBasePointVente(rabais.getPoint().getId()));
                    if (index > -1) {
                        YvsBasePointVente y = service.getPointsvente().get(index);
                        rabais.setPoint(UtilCom.buildBeanPointVente(y));
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("choosePointVente Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void searchArticle() {
        try {
            String num = rabais.getArticle().getArticle().getRefArt();
            rabais.getArticle().getArticle().setDesignation("");
            rabais.getArticle().getArticle().setError(true);
            rabais.getArticle().getArticle().setId(0);
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                Articles y = m.searchArticleActif("V", num, true);
                if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                    if (m.getArticlesResult().size() > 1) {
                        update("data-articles_rabais");
                    } else {
                        chooseArticle(y);
                    }
                    rabais.getArticle().getArticle().setError(false);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("searchArticle Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void clearParams() {
        try {
            articleSearch = "";
            pointSearch = 0;
            addDate = false;
            dateDebutSearch = new Date();
            dateFinSearch = new Date();
            permanentSearch = null;
            actifSearch = null;
            paginator.clear();
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("clearParams Impossible !");
            log.log(Level.SEVERE, null, ex);
        }

    }

    public void addParamArticle() {
        try {
            ParametreRequete p = new ParametreRequete("y.article.article.article.refArt", "article", null);
            if (articleSearch != null ? articleSearch.trim().length() > 0 : false) {
                p = new ParametreRequete(null, "article", null, "=", "AND");
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.article.article.refArt)", "article", articleSearch.toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.article.article.designation)", "article", articleSearch.toUpperCase() + "%", "LIKE", "OR"));
            }
            paginator.addParam(p);
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("addParamArticle Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void addParamPointVente() {
        try {
            ParametreRequete p = new ParametreRequete("y.article.article.point", "point", null);
            if (pointSearch > 0) {
                p = new ParametreRequete("y.article.article.point", "point", new YvsBasePointVente(pointSearch), "=", "AND");
            }
            paginator.addParam(p);
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("addParamPointVente Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void addParamDates() {
        try {
            ParametreRequete p = new ParametreRequete("y.dateDebut", "dateDebut", null);
            if (addDate) {
                p = new ParametreRequete("y.dateDebut", "dateDebut", dateDebutSearch, dateFinSearch, "BETWEEN", "AND");
            }
            paginator.addParam(p);
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("addParamDates Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void addParamPermanent() {
        try {
            paginator.addParam(new ParametreRequete("y.permanent", "permanent", permanentSearch, "=", "AND"));
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("addParamPermanent Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void addParamActif() {
        try {
            paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
            loadAll(false, true);
        } catch (Exception ex) {
            getErrorMessage("addParamPermanent Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

}
