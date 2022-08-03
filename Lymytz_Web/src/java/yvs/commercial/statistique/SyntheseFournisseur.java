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
import yvs.commercial.fournisseur.Fournisseur;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComMensualiteFactureAchat;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.achat.YvsComptaCreditFournisseur;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class SyntheseFournisseur implements Serializable {

    private Fournisseur fournisseur = new Fournisseur();
    private String periode;
    private double solde, soldeImpayes, soldeInitial, soldeAvance, soldeImpaye, soldeAcompte, soldeCredit;
    private YvsBaseExercice exo = new YvsBaseExercice();
    private Date dateDebut = new Date(), dateFin = new Date();
    private List<YvsComMensualiteFactureAchat> mensualites;
    private List<YvsComDocAchats> factures;
    private List<YvsComptaAcompteFournisseur> acomptes;
    private List<YvsComptaCreditFournisseur> credits;
    private CartesianChartModel barModel;

    public SyntheseFournisseur() {
        mensualites = new ArrayList<>();
        factures = new ArrayList<>();
        acomptes = new ArrayList<>();
        credits = new ArrayList<>();
        barModel = new CartesianChartModel();  
    }

    public double getSoldeAvance() {
        return soldeAvance;
    }

    public void setSoldeAvance(double soldeAvance) {
        this.soldeAvance = soldeAvance;
    }

    public double getSoldeInitial() {
        return soldeInitial;
    }

    public void setSoldeInitial(double soldeInitial) {
        this.soldeInitial = soldeInitial;
    }

    public double getSoldeImpayes() {
        return soldeImpayes;
    }

    public void setSoldeImpayes(double soldeImpayes) {
        this.soldeImpayes = soldeImpayes;
    }

    public double getSoldeImpaye() {
        return soldeImpaye;
    }

    public void setSoldeImpaye(double soldeImpaye) {
        this.soldeImpaye = soldeImpaye;
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

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public List<YvsComDocAchats> getFactures() {
        return factures;
    }

    public void setFactures(List<YvsComDocAchats> factures) {
        this.factures = factures;
    }

    public String getPeriode() {
        return periode != null ? periode.trim().length() > 0 ? periode : "M" : "M";
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur client) {
        this.fournisseur = client;
    }

    public List<YvsComMensualiteFactureAchat> getMensualites() {
        return mensualites;
    }

    public void setMensualites(List<YvsComMensualiteFactureAchat> mensualites) {
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

    public List<YvsComptaAcompteFournisseur> getAcomptes() {
        return acomptes;
    }

    public void setAcomptes(List<YvsComptaAcompteFournisseur> acomptes) {
        this.acomptes = acomptes;
    }

    public List<YvsComptaCreditFournisseur> getCredits() {
        return credits;
    }

    public void setCredits(List<YvsComptaCreditFournisseur> credits) {
        this.credits = credits;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.fournisseur);
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
        final SyntheseFournisseur other = (SyntheseFournisseur) obj;
        if (!Objects.equals(this.fournisseur, other.fournisseur)) {
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

}
