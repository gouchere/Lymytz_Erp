/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.commercial.YvsHistoriquePr;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedHistoriquePR extends Managed<YvsHistoriquePr, YvsHistoriquePr> implements Serializable {

    private List<YvsHistoriquePr> historiques;
    private String tabIds;

    private List<YvsBaseConditionnement> conditionnements;

    private boolean dateSearch;
    private String articleSearch;
    private long depotSearch;
    private Double prSearch = 0D;
    private String operateurPrSearch = ">";
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();

    public ManagedHistoriquePR() {
        historiques = new ArrayList<>();
        conditionnements = new ArrayList<>();
    }

    public List<YvsHistoriquePr> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsHistoriquePr> historiques) {
        this.historiques = historiques;
    }

    public boolean isDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(boolean dateSearch) {
        this.dateSearch = dateSearch;
    }

    public String getArticleSearch() {
        return articleSearch;
    }

    public void setArticleSearch(String articleSearch) {
        this.articleSearch = articleSearch;
    }

    public String getOperateurPrSearch() {
        return operateurPrSearch;
    }

    public void setOperateurPrSearch(String operateurPrSearch) {
        this.operateurPrSearch = operateurPrSearch;
    }

    public Double getPrSearch() {
        return prSearch;
    }

    public void setPrSearch(Double prSearch) {
        this.prSearch = prSearch;
    }

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
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

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public List<YvsBaseConditionnement> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsBaseConditionnement> conditionnements) {
        this.conditionnements = conditionnements;
    }

    public void loadAll(boolean avance, boolean init) {
        historiques.clear();
        if ((articleSearch != null ? articleSearch.trim().length() > 0 : false) || (depotSearch > 0)) {
            historiques = paginator.executeDynamicQuery("YvsHistoriquePr", "y.dateEvaluation DESC", avance, init, (int) imax, dao);
        }
    }

    public void avancer(boolean avance) {
        loadAll(avance, false);
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

    public void chooseDateSearch() {
        ParametreRequete p;
        if (dateSearch) {
            p = new ParametreRequete("y.dateEvaluation", "dateEvaluation", dateDebutSearch, dateFinSearch, "BETWEEN", "AND");
        } else {
            p = new ParametreRequete("y.dateEvaluation", "dateEvaluation", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void searchArticle() {
        ParametreRequete p;
        if (articleSearch != null ? articleSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "refArt", articleSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.conditionnement.article.refArt", "refArt", articleSearch + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.conditionnement.article.designation", "refArt", articleSearch + "%", "LIKE", "OR"));
        } else {
            p = new ParametreRequete("y.conditionnement.id", "conditionnement", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void chooseDepotSearch() {
        ParametreRequete p;
        if (depotSearch > 0) {
            p = new ParametreRequete("y.depot.id", "depot", depotSearch, "=", "AND");
        } else {
            p = new ParametreRequete("y.depot.id", "depot", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void choosePrSearch() {
        ParametreRequete p;
        if (prSearch != null && prSearch > -1) {
            p = new ParametreRequete("y.pr", "pr", prSearch, operateurPrSearch, "AND");
        } else {
            p = new ParametreRequete("y.pr", "pr", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    if (historiques.contains(new YvsHistoriquePr(id))) {
                        YvsHistoriquePr bean = historiques.get(historiques.indexOf(new YvsHistoriquePr(id)));
                        dao.delete(bean);
                        historiques.remove(bean);
                    }
                }
                tabIds = "";
                succes();
            }
        } catch (NumberFormatException ex) {
            System.err.println("Error Suppresion : " + ex.getMessage());
            getErrorMessage("Suppresion Impossible !");
        }
    }

    @Override
    public void loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public YvsHistoriquePr recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean controleFiche(YvsHistoriquePr bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(YvsHistoriquePr bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        tabIds = "";
    }

    @Override
    public boolean saveNew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
