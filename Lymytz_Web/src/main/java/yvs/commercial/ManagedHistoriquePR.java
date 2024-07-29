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
import yvs.commercial.depot.ManagedDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
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
            p = new ParametreRequete("y.dateEvaluation", "dateEvaluation", dateDebutSearch);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFinSearch);
        } else {
            p = new ParametreRequete("y.dateEvaluation", "dateEvaluation", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void searchArticle() {
        ParametreRequete p;
        if (articleSearch != null ? articleSearch.trim().length() > 0 : false) {
            List<Long> ids = dao.loadListByNameQueries("YvsBaseConditionnement.findIdLikeArticle", new String[]{"article", "societe"}, new Object[]{articleSearch + "%", currentAgence.getSociete()});
            p = new ParametreRequete("y.conditionnement", "conditionnement", ids);
            p.setOperation("IN");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.conditionnement", "conditionnement", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void chooseDepotSeach() {
        ParametreRequete p;
        if (depotSearch > 0) {
            p = new ParametreRequete("y.depot", "depot", depotSearch);
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.depot", "depot", null);
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
        } catch (Exception ex) {
            System.err.println("Error Suppresion : " + ex.getMessage());
            getErrorMessage("Suppresion Impossible !");
        }
    }

    public String getArticleName(YvsHistoriquePr item) {
        YvsBaseConditionnement bean = null;
        if (item != null ? item.getConditionnement() > 0 : false) {
            int index = conditionnements.indexOf(new YvsBaseConditionnement(item.getConditionnement()));
            if (index > -1) {
                bean = conditionnements.get(index);
            }
            if (bean == null) {
                bean = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{item.getConditionnement()});
                conditionnements.add(bean);
            }
        }
        if (bean != null ? bean.getId() > 0 : false) {
            return bean.getArticle().getDesignation() + " [" + bean.getUnite().getReference() + "]";
        }
        return "--Inexistant--";
    }

    public String getDepotName(YvsHistoriquePr item) {
        if (item != null ? item.getDepot() > 0 : false) {
            ManagedDepot service = (ManagedDepot) giveManagedBean("managedDepot");
            if (service != null) {
                int index = service.getDepots_all().indexOf(new YvsBaseDepots(item.getDepot()));
                if (index > -1) {
                    return service.getDepots_all().get(index).getDesignation();
                }
            }
        }
        return "--Inexistant--";
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
