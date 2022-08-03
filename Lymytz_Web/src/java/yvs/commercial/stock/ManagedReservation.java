/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.production.UtilProd;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.UtilCom;
import yvs.commercial.depot.ManagedDepot;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.stock.YvsComReservationStock;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedReservation extends ManagedCommercial<ReservationStock, YvsComReservationStock> implements Serializable {

    @ManagedProperty(value = "#{reservationStock}")
    private ReservationStock reservationStock;
    private ReservationStock select = new ReservationStock();
    private List<YvsComReservationStock> reservations, historiques;
    private YvsComReservationStock selectReservation;
    public PaginatorResult<YvsComReservationStock> p_historique = new PaginatorResult<>();

    private String tabIds;
    private int max = 5;

    public ManagedReservation() {
        reservations = new ArrayList<>();
        historiques = new ArrayList<>();
    }

    public ReservationStock getSelect() {
        return select;
    }

    public void setSelect(ReservationStock select) {
        this.select = select;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public PaginatorResult<YvsComReservationStock> getP_historique() {
        return p_historique;
    }

    public void setP_historique(PaginatorResult<YvsComReservationStock> p_historique) {
        this.p_historique = p_historique;
    }

    public ReservationStock getReservationStock() {
        return reservationStock;
    }

    public void setReservationStock(ReservationStock reservationStock) {
        this.reservationStock = reservationStock;
    }

    public List<YvsComReservationStock> getReservations() {
        return reservations;
    }

    public void setReservations(List<YvsComReservationStock> reservations) {
        this.reservations = reservations;
    }

    public List<YvsComReservationStock> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsComReservationStock> historiques) {
        this.historiques = historiques;
    }

    public YvsComReservationStock getSelectReservation() {
        return selectReservation;
    }

    public void setSelectReservation(YvsComReservationStock selectReservation) {
        this.selectReservation = selectReservation;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    private int buildDroit() {
        return 1;
    }

    @Override
    public void loadAll() {
        if (agence_ < 1) {
            agence_ = currentAgence.getId();
        }
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        loadAll(true, true);
    }

    public void loadAll(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDroit()) {
            case 1:
                p = new ParametreRequete("y.depot.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                break;
            case 2:
                p = new ParametreRequete("y.depot.agence", "agence", currentAgence, "=", "AND");
                break;
            case 3:
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (currentUser.getUsers() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                }
                p = new ParametreRequete("y.depot.id", "depots", ids, "IN", "AND");
                break;
            default:
                p = new ParametreRequete("y.depot.responsable", "users", currentUser.getUsers(), "=", "AND");
                break;
        }
        paginator.addParam(p);
        reservations = paginator.executeDynamicQuery("YvsComReservationStock", "y.dateEcheance DESC", avance, init, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComReservationStock> re = paginator.parcoursDynamicData("YvsComReservationStock", "y", "y.dateEcheance DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadHistorique(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDroit()) {
            case 1:
                p = new ParametreRequete("y.depot.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                break;
            case 2:
                p = new ParametreRequete("y.depot.agence", "agence", currentAgence, "=", "AND");
                break;
            case 3:
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (currentUser.getUsers() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                }
                p = new ParametreRequete("y.depot.id", "depots", ids, "IN", "AND");
                break;
            default:
                p = new ParametreRequete("y.depot.responsable", "users", currentUser.getUsers(), "=", "AND");
                break;
        }
        p_historique.addParam(p);
        p_historique.addParam(new ParametreRequete("y.statut", "statut", Constantes.ETAT_VALIDE, "!=", "AND"));
        historiques = p_historique.executeDynamicQuery("YvsComReservationStock", "y.dateEcheance DESC", avance, init, max, dao);
    }

    @Override
    public boolean controleFiche(ReservationStock bean) {
        if (bean == null) {
            getErrorMessage("La reservation ne peut pas etre null");
            return false;
        }
        if (!bean.canEditable()) {
            getErrorMessage("Vous ne pouvez pas modifier cette reservation");
            return false;
        }
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser l'article");
            return false;
        }
        if (bean.getConditionnement() != null ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le conditionnement");
            return false;
        }
        if (bean.getDepot() != null ? bean.getDepot().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le depot");
            return false;
        }
        if (bean.getQuantite() <= 0) {
            getErrorMessage("Vous devez preciser la quantitée");
            return false;
        }
        if (bean.getArticle().getStock() < bean.getQuantite()) {
            getWarningMessage("L'article '" + bean.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
        } else {
            double stock = dao.stocks(bean.getArticle().getId(), 0, bean.getDepot().getId(), 0, 0, new Date(), bean.getConditionnement().getId(), 0);
            if (stock < bean.getQuantite()) {
                getWarningMessage("L'article '" + bean.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
            }
        }
        if ((selectReservation != null ? (!selectReservation.getDateReservation().equals(bean.getDateReservation())) : false)
                || (bean.getNumReference() == null || bean.getNumReference().trim().length() < 1)) {
            String ref = genererReference(Constantes.TYPE_RS_NAME, bean.getDateReservation(), bean.getDepot().getId());
            bean.setNumReference(ref);
            if (ref == null || ref.trim().equals("")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ReservationStock recopieView() {
        ReservationStock r = new ReservationStock();
        r.setActif(reservationStock.isActif());
        r.setArticle(reservationStock.getArticle());
        r.setDateEcheance(reservationStock.getDateEcheance());
        r.setDateReservation(reservationStock.getDateReservation());
        r.setDepot(reservationStock.getDepot());
        r.setId(reservationStock.getId());
        r.setNumReference(reservationStock.getNumReference());
        r.setNumExterne(reservationStock.getNumExterne());
        r.setQuantite(reservationStock.getQuantite());
        r.setStatut(reservationStock.getStatut());
        r.setConditionnement(reservationStock.getConditionnement());
        r.setDateSave(reservationStock.getDateSave());
        r.setDateUpdate(reservationStock.getDateUpdate());
        r.setDescription(reservationStock.getDescription());
        return r;
    }

    @Override
    public void populateView(ReservationStock bean) {
        cloneObject(reservationStock, bean);
        loadInfosArticle(bean.getArticle());
    }

    @Override
    public void resetFiche() {
        resetFiche(reservationStock);
        reservationStock.setArticle(new Articles());
        reservationStock.setDepot(new Depots());
        reservationStock.setConditionnement(new Conditionnement());

        selectReservation = new YvsComReservationStock();
        tabIds = "";
        update("blog_form_reservation");
    }

    @Override
    public boolean saveNew() {
        ReservationStock bean = recopieView();
        selectReservation = saveNew(bean);
        if (selectReservation != null ? (selectReservation.getId() != null ? selectReservation.getId() > 0 : false) : false) {
            reservationStock.setId(bean.getId());
            reservationStock.setNumReference(bean.getNumReference());
            succes();
            actionOpenOrResetAfter(this);
            return true;
        }
        return false;
    }

    public YvsComReservationStock saveNew(ReservationStock bean) {
        String action = bean.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(bean)) {
                YvsComReservationStock y = UtilCom.buildReservation(bean, currentUser);
                if (bean.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setDateSave(new Date());
                    y.setId(null);
                    y = (YvsComReservationStock) dao.save1(y);
                    bean.setId(y.getId());
                }
                int idx = reservations.indexOf(y);
                if (idx > -1) {
                    reservations.set(idx, y);
                } else {
                    reservations.add(0, y);
                }
                return y;
            }
        } catch (Exception ex) {
            getException(action + " Impossible !", ex);
        }
        return null;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComReservationStock bean = reservations.get(reservations.indexOf(new YvsComReservationStock(id)));
                    if (bean.canDelete()) {
                        dao.delete(bean);
                        reservations.remove(bean);
                        if (historiques.contains(bean)) {
                            historiques.remove(bean);
                        }
                        if (reservationStock.getId() == id) {
                            resetFiche();
                        }
                    }
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getException("Suppression Impossible !", ex);
        }
    }

    public void deleteBean(YvsComReservationStock y) {
        try {
            if (y != null) {
                if (!y.canDelete()) {
                    getErrorMessage("Vous ne pouvez pas supprimer cette reservation");
                    return;
                }
                dao.delete(y);
                if (reservations.contains(y)) {
                    reservations.remove(y);
                }
                if (historiques.contains(y)) {
                    historiques.remove(y);
                }
                if (reservationStock.getId() == y.getId()) {
                    resetFiche();
                }
            }
        } catch (NumberFormatException ex) {
            getException("Suppression Impossible !", ex);
        }
    }

    public void deleteBean_(YvsComReservationStock y) {
        selectReservation = y;
    }

    public void deleteBean_() {
        try {
            if (selectReservation != null) {
                if (!selectReservation.canDelete()) {
                    getErrorMessage("Vous ne pouvez pas supprimer cette reservation");
                    return;
                }
                dao.delete(selectReservation);
                reservations.remove(selectReservation);
                if (historiques.contains(selectReservation)) {
                    historiques.remove(selectReservation);
                }
                if (reservationStock.getId() == selectReservation.getId()) {
                    resetFiche();
                }
                succes();
                update("data_reservation");
                resetFiche();
            }
        } catch (Exception ex) {
            getException("Suppression Impossible !", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectObject(YvsComReservationStock y) {
        selectReservation = y;
        selectReservation.getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{y.getArticle()}));
        populateView(UtilCom.buildBeanReservation(selectReservation));
        p_historique.addParam(new ParametreRequete("y.depot", "depot", selectReservation.getDepot(), "=", "AND"));
        p_historique.addParam(new ParametreRequete("y.article", "article", selectReservation.getArticle(), "=", "AND"));
        loadHistorique(true, true);
        update("blog_form_reservation");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComReservationStock y = (YvsComReservationStock) ev.getObject();
            onSelectObject((y));
        }
    }

    public void _loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectReservation = (YvsComReservationStock) ev.getObject();
            populateView(UtilCom.buildBeanReservation(selectReservation));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleDepot bean = (YvsBaseArticleDepot) ev.getObject();
            chooseArticle(UtilProd.buildBeanArticles(bean.getArticle()));
        }
    }

    public void init(boolean next) {
        loadAll(next, false);
    }

    public void initViewDetail(YvsComReservationStock y) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            select = UtilCom.buildBeanReservation(y);
            YvsComDocVentes v = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findByNumDoc", new String[]{"societe", "numDoc"}, new Object[]{currentAgence.getSociete(), y.getNumExterne()});
            if (v != null ? (v.getId() != null ? y.getId() > 0 : false) : false) {
                select.setVente(UtilCom.buildBeanDocVente(v));
                List<YvsComContenuDocVente> lc = dao.loadNameQueries("YvsComContenuDocVente.findByVenteArticle", new String[]{"docVente", "article"}, new Object[]{v, y.getArticle()}, 0, 1);
                if (lc != null ? !lc.isEmpty() : false) {
                    select.getVente().setContenu(UtilCom.buildBeanContenuDocVente(lc.get(0)));
                }
            }
            update("blog_detail_reservation");
        }
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public void _choosePaginator(ValueChangeEvent ev) {
        max = (int) ev.getNewValue();
        loadHistorique(true, true);
    }

    public void chooseConditionnement() {
        if (reservationStock.getConditionnement() != null ? reservationStock.getConditionnement().getId() > 0 : false) {
            if (reservationStock.getArticle() != null) {
                int idx = reservationStock.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(reservationStock.getConditionnement().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = reservationStock.getArticle().getConditionnements().get(idx);
                    reservationStock.setConditionnement(UtilProd.buildBeanConditionnement(y));
                }
            }
        }
        loadInfosArticle(reservationStock.getArticle());
    }

    public void loadInfosArticle(Articles y) {
        if (reservationStock.getDepot() != null ? reservationStock.getDepot().getId() > 0 : false) {
            y.setStock(dao.stocks(y.getId(), 0, reservationStock.getDepot().getId(), 0, 0, reservationStock.getDateReservation(), reservationStock.getConditionnement().getId(), 0));
        } else {
            y.setStock(dao.stocks(y.getId(), 0, 0, currentAgence.getId(), 0, reservationStock.getDateReservation(), reservationStock.getConditionnement().getId(), 0));
        }
        reservationStock.setArticle(y);
        p_historique.addParam(new ParametreRequete("y.article", "article", new YvsBaseArticles(y.getId()), "=", "AND"));
        loadHistorique(true, true);

    }

    public void chooseArticle(Articles art) {
        champ = new String[]{"article", "depot", "operation", "type"};
        val = new Object[]{new YvsBaseArticles(art.getId()), new YvsBaseDepots(reservationStock.getDepot().getId()), "SORTIE", "RESERVATION"};
        nameQueri = "YvsBaseConditionnementDepot.findConditionnement";
        List<YvsBaseConditionnement> la = dao.loadNameQueries(nameQueri, champ, val);
        if (la != null ? !la.isEmpty() : false) {
            reservationStock.setConditionnement(UtilProd.buildBeanConditionnement(la.get(0)));
        } else {
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(art.getId())});
            art.setConditionnements(unites);
            reservationStock.setConditionnement(art.getUniteVenteByDefault());
        }
        loadInfosArticle(art);
        update("select_conditionnement_reservation");
        update("select_article_reservation");
    }

    public void chooseDepot() {
        ParametreRequete p = new ParametreRequete("y.depot", "depot", null);
        ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (s != null) {
            ManagedStockArticle a = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if ((reservationStock.getDepot() != null) ? reservationStock.getDepot().getId() > 0 : false) {
                YvsBaseDepots y = s.getDepots().get(s.getDepots().indexOf(new YvsBaseDepots(reservationStock.getDepot().getId())));
                Depots d = UtilCom.buildBeanDepot(y);
                cloneObject(reservationStock.getDepot(), d);
                if (a != null) {
                    a.loadAllArticle(y);
                    a.setEntityDepot(y);
                    a.loadActifArticleByDepot(true, true);
                }
                p = new ParametreRequete("y.depot", "depot", y, "=", "AND");
            } else {
                if (a != null) {
                    a.getArticlesDebut().clear();
                }
            }
        }
        p_historique.addParam(p);
        if (reservationStock.getArticle() != null ? reservationStock.getArticle().getId() > 0 : false) {
            loadHistorique(true, true);
        }
    }

    public void searchArticle() {
        paginator.addParam(new ParametreRequete("y.article", "article", null, "=", "AND"));
        String num = reservationStock.getArticle().getRefArt();
        reservationStock.getArticle().setDesignation("");
        reservationStock.getArticle().setError(true);
        reservationStock.getArticle().setId(0);
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            if (m.getEntityDepot() == null) {
                m.setEntityDepot(new YvsBaseDepots(reservationStock.getDepot().getId()));
            }
            Articles y = m.searchArticleActifByDepot(num, true);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update("data_articles_reservation");
                } else {
                    chooseArticle(y);
                }
                reservationStock.getArticle().setError(false);
            }
        }
    }

    public void initArticles() {
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.initArticlesByDepot(reservationStock.getArticle());
        }
        update("data_articles_reservation");
    }

    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actif_, "=", "AND"));
        loadAll(true, true);
    }

    public void addParamArticle() {
        ParametreRequete p = new ParametreRequete("y.article.refArt", "article", null, "LIKE", "AND");
        if (otherSearch_ != null ? otherSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "article", otherSearch_.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", otherSearch_.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", otherSearch_.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamAgence() {
        ParametreRequete p;
        if (agence_ > 0) {
            p = new ParametreRequete("y.depot.agence", "agence", new YvsAgences(agence_), "=", "AND");
        } else {
            p = new ParametreRequete("y.depot.agence", "agence", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
        _loadDepot();
    }

    public void addParamDepot() {
        ParametreRequete p = new ParametreRequete("y.depot", "depot", null, "=", "AND");
        if (depot_ > 0) {
            p = new ParametreRequete("y.depot", "depot", new YvsBaseDepots(depot_), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.numReference", "reference", null, "LIKE", "AND");
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch_.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numReference)", "reference", numSearch_.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numExterne)", "reference", numSearch_.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateReservation", "dates", null, "=", "AND");
        if (date_) {
            p = new ParametreRequete(null, "dates", dateDebut_, "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.dateReservation", "dates", dateDebut_, dateFin_, "BETWEEN", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.dateEcheance", "dates", dateDebut_, dateFin_, "BETWEEN", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public boolean canAutorisation(int action) {
        //action : valide 0 -- Annule 1 -- Editer 2
        switch (action) {
            case 0:
//        if(autoriser("")){
                //            return true;
                //        }
                if (currentUser != null ? (currentUser.getUsers() != null) : false) {
                    if (selectReservation != null ? selectReservation.getDepot() != null : false) {
                        if (currentUser.getUsers().getEmploye() != null && selectReservation.getDepot().getResponsable() != null) {
                            // Controle si le employé courant est le responsable du depot destinataire
                            if (!selectReservation.getDepot().getResponsable().equals(currentUser.getUsers().getEmploye())) {
                                getErrorMessage("Vous ne pouvez pas modifier cette reservation...car vous n'êtes pas habilité à le faire");
                                return false;
                            }
                        } else {
                            getErrorMessage("Vous ne pouvez pas modifier cette reservation...car vous n'êtes pas habilité à le faire");
                            return false;
                        }
                    }
                } else {
                    getErrorMessage("Vous ne pouvez pas modifier cette reservation...car vous n'êtes pas habilité à le faire");
                    return false;
                }
                break;
            case 1:
            //        if(autoriser("")){
            //            return true;
            //        }
            case 2:
                //        if(autoriser("")){
                //            return true;
                //        }
                break;
        }
        return true;
    }

    public void terminer() {
        if (selectReservation == null) {
            return;
        }
        if (selectReservation.getStatut().equals(Constantes.ETAT_VALIDE)) {
            if (changeStatut(Constantes.ETAT_TERMINE)) {
                succes();
            }
        } else {
            getErrorMessage("Cette reservation doit etre validée");
        }
    }

    public void annuler(boolean suspend, boolean force) {
        if (selectReservation == null) {
            return;
        }
        if (selectReservation != null ? selectReservation.getId() > 0 : false) {
            if (!canAutorisation(suspend ? 1 : 2)) {
                return;
            }
            if (!force && selectReservation.getStatut().equals(Constantes.ETAT_VALIDE)) {
                openDialog(suspend ? "dlgConfirmAnnuler" : "dlgConfirmEditer");
            } else {
                if (changeStatut(suspend ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE)) {
                    succes();
                }
            }
        }
    }

    public void valider() {
        if (selectReservation == null) {
            return;
        }
        if (!verifyOperation(reservationStock.getDepot(), Constantes.SORTIE, Constantes.RESERVATION, true)) {
            return;
        }
        if (!canAutorisation(0)) {
            return;
        }

        double stock = dao.stocks(reservationStock.getArticle().getId(), 0, reservationStock.getDepot().getId(), 0, 0, reservationStock.getDateReservation(), reservationStock.getConditionnement().getId(), 0);
        if (stock < reservationStock.getQuantite()) {
            getErrorMessage("L'article '" + reservationStock.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
            return;
        }
        stock = dao.stocks(reservationStock.getArticle().getId(), 0, reservationStock.getDepot().getId(), 0, 0, new Date(), reservationStock.getConditionnement().getId(), 0);
        if (stock < reservationStock.getQuantite()) {
            getErrorMessage("L'article '" + reservationStock.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
            return;
        }
        if (changeStatut(Constantes.ETAT_VALIDE)) {
            succes();
        }
    }

    public boolean changeStatut(String etat) {
        if (!etat.equals("") && selectReservation != null) {
            selectReservation.setStatut(etat);
            selectReservation.setAuthor(currentUser);
            dao.update(selectReservation);
            if (reservations.contains(selectReservation)) {
                reservations.set(reservations.indexOf(selectReservation), selectReservation);
            }
            if (!etat.equals(Constantes.ETAT_EDITABLE)) {
                if (historiques.contains(selectReservation)) {
                    historiques.remove(historiques.indexOf(selectReservation));
                    update("data_reservation_hist");
                }
            } else {
                if (!historiques.contains(selectReservation)) {
                    historiques.add(0, selectReservation);
                    update("data_reservation_hist");
                }
            }
            reservationStock.setStatut(etat);
            update("data_reservation");
            update("form_reservation");
            update("grp_btn_etat_reservation");
            return true;
        }
        return false;
    }

}
