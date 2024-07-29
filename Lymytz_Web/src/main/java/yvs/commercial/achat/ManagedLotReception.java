/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.commercial.UtilCom;
import yvs.commercial.param.ManagedParametre;
import yvs.dao.Options;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.produits.YvsBaseArticles;
import yvs.production.UtilProd;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedLotReception extends Managed<LotReception, YvsComLotReception> implements Serializable {
    
    @ManagedProperty(value = "#{lotReception}")
    private LotReception lotReception;
    private List<YvsComLotReception> lots;
    private YvsComLotReception lotSelect;
    private String tabIds;
    
    private String referenceSearch, statutSearch, articleSearch;
    private Boolean actifSearch = null;
    private boolean dateSearch = false;
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();
    
    public ManagedLotReception() {
        lots = new ArrayList<>();
    }
    
    public String getArticleSearch() {
        return articleSearch;
    }
    
    public void setArticleSearch(String articleSearch) {
        this.articleSearch = articleSearch;
    }
    
    public String getReferenceSearch() {
        return referenceSearch;
    }
    
    public void setReferenceSearch(String referenceSearch) {
        this.referenceSearch = referenceSearch;
    }
    
    public String getStatutSearch() {
        return statutSearch;
    }
    
    public void setStatutSearch(String statutSearch) {
        this.statutSearch = statutSearch;
    }
    
    public Boolean getActifSearch() {
        return actifSearch;
    }
    
    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }
    
    public boolean isDateSearch() {
        return dateSearch;
    }
    
    public void setDateSearch(boolean dateSearch) {
        this.dateSearch = dateSearch;
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
    
    public YvsComLotReception getLotSelect() {
        return lotSelect;
    }
    
    public void setLotSelect(YvsComLotReception lotSelect) {
        this.lotSelect = lotSelect;
    }
    
    public LotReception getLotReception() {
        return lotReception;
    }
    
    public void setLotReception(LotReception lotReception) {
        this.lotReception = lotReception;
    }
    
    public List<YvsComLotReception> getLots() {
        return lots;
    }
    
    public void setLots(List<YvsComLotReception> lots) {
        this.lots = lots;
    }
    
    public String getTabIds() {
        return tabIds;
    }
    
    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }
    
    @Override
    public void loadAll() {
        loadAllLots();
    }
    
    public void loadAll(boolean avancer, boolean init) {
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        lots = paginator.executeDynamicQuery("YvsComLotReception", "y.dateExpiration DESC, y.numero", avancer, init, (int) imax, dao);
        update("tabview_param_com:data_lot_reception");
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
    
    public void loadAllLots() {
        lots = dao.loadNameQueries("YvsComLotReception.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }
    
    public List<YvsComLotReception> loadList(long depot, long conditionnement, Date date, double quantite, double stock) {
        return loadList(depot, 0, conditionnement, date, quantite, stock);
    }
    
    public List<YvsComLotReception> loadList(long depot, long article, long conditionnement, Date date, double quantite, double stock) {
        List<YvsComLotReception> list = new ArrayList<>();
        try {
            String query = "SELECT y.id, y.numero, y.date_fabrication, y.date_expiration, get_stock(m.article, 0, m.depot, 0, 0, ?::date, m.conditionnement, l.lot) "
                    + "FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_mouvement_stock_lot l ON m.id = l.mouvement INNER JOIN yvs_com_lot_reception y ON l.lot = y.id "
                    + "WHERE m.depot = ? ";
            if (article > 0) {
                query += "AND m.article = ? ";
            }
            if (conditionnement > 0) {
                query += "AND m.conditionnement = ? ";
            }
            query += "GROUP BY y.id, m.article, m.depot, m.conditionnement, l.lot "
                    + "HAVING get_stock(m.article, 0, m.depot, 0, 0, ?::date, m.conditionnement, l.lot) > 0 ORDER BY y.date_expiration, y.date_fabrication, y.numero";
            int i = 1;
            List<Options> params = new ArrayList<>();
            params.add(new Options(date, i++));
            params.add(new Options(depot, i++));
            if (article > 0) {
                params.add(new Options(article, i++));
            }
            if (conditionnement > 0) {
                params.add(new Options(conditionnement, i++));
            }
            params.add(new Options(date, i++));
            List<Object[]> result = dao.loadListBySqlQuery(query, params.toArray(new Options[params.size()]));
            YvsComLotReception y;
            for (Object[] lect : result) {
                y = new YvsComLotReception((Long) lect[0], (String) lect[1], new Date(((java.sql.Date) lect[2]).getTime()), new Date(((java.sql.Date) lect[3]).getTime()), (Double) lect[4]);
                if (quantite > 0) {
                    if (quantite > y.getStock()) {
                        y.setQuantitee(y.getStock());
                        quantite -= y.getStock();
                    } else {
                        y.setQuantitee(quantite);
                        quantite = 0;
                    }
                }
                if (stock > 0) {
                    stock -= y.getStock();
                }
                list.add(y);
            }
            if (stock > 0) {
                YvsComLotReception no = new YvsComLotReception(-1L, "SANS LOT", new Date(), new Date(), stock);
                list.add(0, no);
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public List<YvsComLotReception> buildQuantiteLot(List<YvsComLotReception> list, double quantite) {
        if (list != null ? !list.isEmpty() : false) {
            for (YvsComLotReception y : list) {
                if (y.getId() > 0 && quantite > 0) {
                    if (quantite > y.getStock()) {
                        y.setQuantitee(y.getStock());
                        quantite -= y.getStock();
                    } else {
                        y.setQuantitee(quantite);
                        quantite = 0;
                    }
                } else {
                    y.setQuantitee(0);
                }
            }
        }
        return list;
    }
    
    @Override
    public boolean controleFiche(LotReception bean) {
        if (bean.getNumero() == null || bean.getNumero().equals("")) {
            getErrorMessage("Vous devez entrer le numero");
            return false;
        }
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer l'article");
            return false;
        }
        YvsComLotReception y = (YvsComLotReception) dao.loadOneByNameQueries("YvsComLotReception.findByNumeroArticle", new String[]{"article", "numero"}, new Object[]{new YvsBaseArticles(bean.getArticle().getId()), bean.getNumero()});
        if (y != null ? (y.getId() > 0 ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja attribuer ce numero à cet article");
            return false;
        }
        return true;
    }
    
    @Override
    public void populateView(LotReception bean) {
        cloneObject(lotReception, bean);
    }
    
    @Override
    public void resetFiche() {
        resetFiche(lotReception);
        lotReception.setUpdateArticle(true);
        lotReception.setArticle(new Articles());
        tabIds = "";
        lotSelect = new YvsComLotReception();
        update("tabview_param_com:form_lot_reception");
    }
    
    @Override
    public boolean saveNew() {
        String action = lotReception.getId() > 0 ? "Modification" : "Insertion";
        try {
            YvsComLotReception y = _saveNew(lotReception);
            if (y != null) {
                lotSelect = y;
                succes();
                resetFiche();
                update("tabview_param_com:form_lot_reception");
                update("tabview_param_com:data_lot_reception");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }
    
    public YvsComLotReception _saveNew(String numero, Articles article, Date dateFabrication, Date dateExpiration) {
        YvsComLotReception y = (YvsComLotReception) dao.loadOneByNameQueries("YvsComLotReception.findByNumeroArticle", new String[]{"article", "numero"}, new Object[]{new YvsBaseArticles(article.getId()), numero});
        if (y != null ? y.getId() > 0 : false) {
            return y;
        }
        return _saveNew(new LotReception(numero, article, dateFabrication, dateExpiration));
    }
    
    public YvsComLotReception _saveNew(LotReception lotReception) {
        YvsComLotReception y = null;
        String action = lotReception.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(lotReception)) {
                y = UtilCom.buildLotReception(lotReception, currentAgence, currentUser);
                if (lotReception.getId() < 1) {
                    y.setId(null);
                    y = (YvsComLotReception) dao.save1(y);
                    lotReception.setId(y.getId());
                } else {
                    dao.update(y);
                }
                int index = lots.indexOf(y);
                if (index < 0) {
                    lots.add(0, y);
                } else {
                    lots.set(lots.indexOf(y), y);
                }
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
        }
        return y;
    }
    
    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComLotReception bean = lots.get(lots.indexOf(new YvsComLotReception(id)));
                    dao.delete(bean);
                    lots.remove(bean);
                }
                succes();
                update("data_lot_reception");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }
    
    public void deleteBean_(YvsComLotReception y) {
        lotSelect = y;
    }
    
    public void deleteBean_() {
        try {
            if (lotSelect != null) {
                dao.delete(lotSelect);
                lots.remove(lotSelect);
                succes();
                update("data_lot_reception");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            lotSelect = (YvsComLotReception) ev.getObject();
            populateView(UtilCom.buildBeanLotReception(lotSelect));
            update("blog_form_lot_reception");
        }
    }
    
    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_lot_reception");
    }
    
    public void activeLot(YvsComLotReception bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            dao.update(bean);
            lots.set(lots.indexOf(bean), bean);
        }
    }
    
    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void clearParams() {
        referenceSearch = "";
        articleSearch = "";
        statutSearch = "";
        actifSearch = null;
        dateSearch = false;
        dateDebutSearch = new Date();
        dateFinSearch = new Date();
        paginator.clear();
        loadAll(true, true);
    }
    
    public void addParamNumero() {
        ParametreRequete p = new ParametreRequete("y.numero", "numero", null);
        if (referenceSearch != null ? referenceSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "numero", referenceSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numero)", "numero", referenceSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }
    
    public void addParamArticle() {
        ParametreRequete p = new ParametreRequete("y.article.refArt", "article", null);
        if (articleSearch != null ? articleSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "article", articleSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "refArt", articleSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "designation", articleSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }
    
    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch));
        loadAll(true, true);
    }
    
    public void addParamStatut() {
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null);
        if (statutSearch != null ? statutSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statut", "statut", statutSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }
    
    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateFabrication", "dates", null, "BETWEEN", "AND");
        if (dateSearch ? (dateDebutSearch != null && dateFinSearch != null) : false) {
            p = new ParametreRequete(null, "dates", dateDebutSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.dateFabrication", "dateFabrication", dateDebutSearch, dateFinSearch, "BETWEEN", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.dateExpiration", "dateExpiration", dateDebutSearch, dateFinSearch, "BETWEEN", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }
    
    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            lotReception.setArticle(UtilProd.buildBeanArticles(bean));
            update("tabview_param_com:form_lot_reception");
        }
    }
    
    public void searchArticle() {
        String num = lotReception.getArticle().getRefArt();
        lotReception.getArticle().setDesignation("");
        lotReception.getArticle().setError(true);
        lotReception.getArticle().setId(0);
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            Articles y = m.findArticleActif(num, true);
            if (m.getListArticle() != null ? !m.getListArticle().isEmpty() : false) {
                if (m.getListArticle().size() > 1) {
                    ManagedParametre w = (ManagedParametre) giveManagedBean(ManagedParametre.class);
                    if (w != null) {
                        w.setManagedForArticle(this);
                    }
                    update("data_articles_param_com");
                } else {
                    lotReception.setArticle(y);
                }
                lotReception.getArticle().setError(false);
            }
        }
    }
    
}
