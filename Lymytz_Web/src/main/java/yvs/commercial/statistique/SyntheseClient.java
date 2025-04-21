/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.statistique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;
import yvs.commercial.client.Client;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCreditClient;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class SyntheseClient implements Serializable {

    private Client client = new Client();
    private String periode;
    private YvsBaseExercice exo = new YvsBaseExercice();
    private double solde, soldeInitial, soldeAvance, soldeImpaye, soldeImpayes, soldeAcompte, soldeCredit, soldeAvoir,ttc,montant_last;
    private Date dateDebut = new Date(), dateFin = new Date();
    private List<YvsComDocVentes> factures;
    private List<YvsComMensualiteFactureVente> mensualites;
    private List<YvsComptaAcompteClient> acomptes;
    private List<YvsComptaCreditClient> credits;
    private CartesianChartModel barModel;
    private long nombre_factures;
    private YvsComDocVentes last = new YvsComDocVentes();

    public SyntheseClient() {
        mensualites = new ArrayList<>();
        acomptes = new ArrayList<>();
        factures = new ArrayList<>();
        credits = new ArrayList<>();
        barModel = new CartesianChartModel();  
    }

    public double getSoldeAvoir() {
        return soldeAvoir;
    }

    public void setSoldeAvoir(double soldeAvoir) {
        this.soldeAvoir = soldeAvoir;
    }

    public double getSoldeImpayes() {
        return soldeImpayes;
    }

    public void setSoldeImpayes(double soldeImpayes) {
        this.soldeImpayes = soldeImpayes;
    }

    public String getPeriode() {
        return periode != null ? periode.trim().length() > 0 ? periode : "M" : "M";
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public double getSoldeInitial() {
        return soldeInitial;
    }

    public void setSoldeInitial(double soldeInitial) {
        this.soldeInitial = soldeInitial;
    }

    public double getSoldeAvance() {
        return soldeAvance;
    }

    public void setSoldeAvance(double soldeAvance) {
        this.soldeAvance = soldeAvance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<YvsComMensualiteFactureVente> getMensualites() {
        return mensualites;
    }

    public void setMensualites(List<YvsComMensualiteFactureVente> mensualites) {
        this.mensualites = mensualites;
    }

    public YvsBaseExercice getExo() {
        return exo;
    }

    public void setExo(YvsBaseExercice exo) {
        this.exo = exo;
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

    public CartesianChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(CartesianChartModel barModel) {
        this.barModel = barModel;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public double getSoldeAcompte() {
        return soldeAcompte;
    }

    public void setSoldeAcompte(double soldeAcompte) {
        this.soldeAcompte = soldeAcompte;
    }

    public double getSoldeCredit() {
        return soldeCredit;
    }

    public void setSoldeCredit(double soldeCredit) {
        this.soldeCredit = soldeCredit;
    }

    public List<YvsComptaAcompteClient> getAcomptes() {
        return acomptes;
    }

    public void setAcomptes(List<YvsComptaAcompteClient> acomptes) {
        this.acomptes = acomptes;
    }

    public List<YvsComptaCreditClient> getCredits() {
        return credits;
    }

    public void setCredits(List<YvsComptaCreditClient> credits) {
        this.credits = credits;
    }

    public double getSoldeImpaye() {
        return soldeImpaye;
    }

    public void setSoldeImpaye(double soldeImpaye) {
        this.soldeImpaye = soldeImpaye;
    }

    public List<YvsComDocVentes> getFactures() {
        return factures;
    }

    public void setFactures(List<YvsComDocVentes> factures) {
        this.factures = factures;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.client);
        hash = 73 * hash + Objects.hashCode(this.periode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SyntheseClient other = (SyntheseClient) obj;
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        if (!Objects.equals(this.periode, other.periode)) {
            return false;
        }
        return true;
    }

    public void createCategoryModel() {  
        barModel = new CartesianChartModel();  

        ChartSeries boys = new LineChartSeries();  
        boys.setLabel("Boys");  

        boys.set("2004", 120);  
        boys.set("2005", 100);  
        boys.set("2006", 44);  
        boys.set("2007", 150);  
        boys.set("2008", 25);  

        ChartSeries girls = new LineChartSeries();  
        girls.setLabel("Girls");  
        girls.set("2004", 52);  
        girls.set("2005", 60);  
        girls.set("2006", 110);  
        girls.set("2007", 135);  
        girls.set("2008", 120);  

        barModel.addSeries(boys);  
        barModel.addSeries(girls);  
    } 

    public double getTtc() {
        return ttc;
    }

    public void setTtc(double ttc) {
        this.ttc = ttc;
    }

    public long getNombre_factures() {
        return nombre_factures;
    }

    public void setNombre_factures(long nombre_factures) {
        this.nombre_factures = nombre_factures;
    }

    public YvsComDocVentes getLast() {
        return last;
    }

    public void setLast(YvsComDocVentes last) {
        this.last = last;
    }

    public double getMontant_last() {
        return montant_last;
    }

    public void setMontant_last(double montant_last) {
        this.montant_last = montant_last;
    }
    
    

}
