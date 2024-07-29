/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsConnectionHistorique;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedHistorique extends Managed {

    private List<YvsConnectionHistorique> connections;
    private YvsConnectionHistorique connection = new YvsConnectionHistorique();
    private long agence;
    private String users, adresseIp;
    private Date dateDebut = new Date(), dateFin = new Date();
    private boolean date;

    public ManagedHistorique() {
        connections = new ArrayList<>();
    }

    public boolean isDate() {
        return date;
    }

    public void setDate(boolean date) {
        this.date = date;
    }

    public List<YvsConnectionHistorique> getConnections() {
        return connections;
    }

    public void setConnections(List<YvsConnectionHistorique> connections) {
        this.connections = connections;
    }

    public YvsConnectionHistorique getConnection() {
        return connection;
    }

    public void setConnection(YvsConnectionHistorique connection) {
        this.connection = connection;
    }

    public long getAgence() {
        return agence;
    }

    public void setAgence(long agence) {
        this.agence = agence;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void loadAll() {
        if (agence < 1) {
            agence = currentAgence.getId();
        }
        loadAll(true, true);
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.users.agence", "agence", new YvsAgences(agence), "=", "AND"));
        connections = paginator.executeDynamicQuery("YvsConnectionHistorique", "y.dateConnexion DESC, y.debutNavigation DESC", avance, init, (int) imax, dao);
        update("data_historique_user");
    }

    public void init(boolean next) {
        loadAll(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public void addParamUsers() {
        ParametreRequete p = new ParametreRequete("y.users", "users", null, "LIKE", "AND");
        if (users != null ? users.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "users", users.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.users.codeUsers)", "users", users.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.users.nomUsers)", "users", users.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamIp() {
        ParametreRequete p = new ParametreRequete("y.adresseIp", "adresse", null, "LIKE", "AND");
        if (adresseIp != null ? adresseIp.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.adresseIp)", "adresse", adresseIp.toUpperCase() + "%", "LIKE", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.agence", "agence", null, "=", "AND");
        if (agence > 0) {
            p = new ParametreRequete("y.agence", "agence", new YvsAgences(agence), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateConnexion", "dates", null, "BETWEEN", "AND");
        if (date) {
            p = new ParametreRequete("y.dateConnexion", "dates", dateDebut, dateFin, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Serializable recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            connection = (YvsConnectionHistorique) ev.getObject();
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        connection = new YvsConnectionHistorique();
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
