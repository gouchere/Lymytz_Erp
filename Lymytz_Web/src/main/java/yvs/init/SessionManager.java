/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.init;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.primefaces.context.RequestContext;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.param.workflow.YvsConnectionHistorique;
import yvs.users.Sessions;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz
 */
@ManagedBean
@ApplicationScoped
public class SessionManager implements HttpSessionListener {

    public static List<Sessions> sessions = new ArrayList<>();

    @EJB
    public DaoInterfaceLocal dao;

    private long offset = 0;
    private List<YvsConnectionHistorique> connexions = new ArrayList<>();
    private PaginatorResult<YvsConnectionHistorique> paginator = new PaginatorResult();

    private boolean addDateSearch;
    private Boolean activeSearch = true;
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();

    private SelectItem[] paginations = {
        new SelectItem((int) 0, "@"),
        new SelectItem((int) 5, "5"),
        new SelectItem((int) 10, "10"),
        new SelectItem((int) 15, "15"),
        new SelectItem((int) 25, "25"),
        new SelectItem((int) 50, "50"),
        new SelectItem((int) 100, "100"),
        new SelectItem((int) 150, "150"),
        new SelectItem((int) 200, "200"),
        new SelectItem((int) 500, "500"),
        new SelectItem((int) 1000, "1000"),
        new SelectItem((int) 5000, "1000+")
    };

    public List<Sessions> getSessions() {
        return sessions;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public SelectItem[] getPaginations() {
        return paginations;
    }

    public void setPaginations(SelectItem[] paginations) {
        this.paginations = paginations;
    }

    public List<YvsConnectionHistorique> getConnexions() {
        return connexions;
    }

    public void setConnexions(List<YvsConnectionHistorique> connexions) {
        this.connexions = connexions;
    }

    public PaginatorResult<YvsConnectionHistorique> getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorResult<YvsConnectionHistorique> paginator) {
        this.paginator = paginator;
    }

    public boolean isAddDateSearch() {
        return addDateSearch;
    }

    public void setAddDateSearch(boolean addDateSearch) {
        this.addDateSearch = addDateSearch;
    }

    public Boolean getActiveSearch() {
        return activeSearch;
    }

    public void setActiveSearch(Boolean activeSearch) {
        this.activeSearch = activeSearch;
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

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        sessions.add(new Sessions(event.getSession().getId(), event.getSession()));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        sessions.remove(new Sessions(event.getSession().getId()));
    }

    public static boolean isActive(String sessionId) {
        return sessions.contains(new Sessions(sessionId));
    }

    public static boolean invalidate(String sessionId) {
        if (isActive(sessionId)) {
            HttpSession session = sessions.get(sessions.indexOf(new Sessions(sessionId))).getValeur();
            if (session != null) {
                session.invalidate();
                return true;
            }
        }
        return false;
    }

    public static boolean addSession(String sessionId, HttpSession session) {
        if (!isActive(sessionId)) {
            sessions.add(new Sessions(sessionId, session));
            return true;
        }
        return false;
    }

    public static boolean removeSession(String sessionId) {
        int index = sessions.indexOf(new Sessions(sessionId));
        if (index > -1) {
            sessions.remove(index);
            return true;
        }
        return false;
    }

    public void update(String id) {
        try {
            RequestContext.getCurrentInstance().update(id);
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadAll(boolean avancer, boolean init) {
        connexions = paginator.executeDynamicQuery("YvsConnectionHistorique", "y.dateConnexion DESC", avancer, init, dao);
        update("data-connexion");
    }

    public void gotoPagePaginator() {
        loadAll(true, false);
    }

    public void choosePaginator(ValueChangeEvent ev) {
        try {
            if ((ev != null) ? ev.getNewValue() != null : false) {
                long v;
                try {
                    v = (long) ev.getNewValue();
                } catch (Exception ex) {
                    v = (int) ev.getNewValue();
                }
                paginator.setRows((int) v);
            }
            loadAll(true, false);
        } catch (Exception ex) {
            Logger.getLogger(SessionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearParams() {
        addDateSearch = false;
        activeSearch = null;
        dateDebutSearch = new Date();
        dateFinSearch = new Date();
        paginator.clear();
        loadAll(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateConnexion", "dateConnexion", null, null, "BETWEEN", "AND");
        if (addDateSearch ? (dateDebutSearch != null && dateFinSearch != null) : false) {
            Calendar debut = Calendar.getInstance();
            debut.setTime(dateDebutSearch);
            debut.set(Calendar.HOUR, 0);
            debut.set(Calendar.MINUTE, 0);
            debut.set(Calendar.SECOND, 0);
            Calendar fin = Calendar.getInstance();
            fin.setTime(dateFinSearch);
            fin.set(Calendar.HOUR, 23);
            fin.set(Calendar.MINUTE, 59);
            fin.set(Calendar.SECOND, 59);
            p = new ParametreRequete("y.dateConnexion", "dateConnexion", debut.getTime(), fin.getTime(), "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamActive(Boolean active) {
        activeSearch = active;
        addParamActive();
    }

    public void addParamActive() {
        ParametreRequete p = new ParametreRequete("y.idSession", "idSession", null, "=", "AND");
        if (activeSearch != null) {
            List<String> ids = new ArrayList<>();
            for (Sessions s : sessions) {
                ids.add(s.getId());
            }
            p = new ParametreRequete("y.idSession", "idSession", ids, (activeSearch ? "IN" : "NOT IN"), "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

}
