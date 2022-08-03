/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.tiers.Tiers;
import yvs.entity.production.base.YvsProdCapacitePosteCharge;
import yvs.entity.production.base.YvsProdCentrePosteCharge;
import yvs.entity.production.base.YvsProdEtatRessource;
import yvs.grh.Calendrier;
import yvs.grh.bean.Employe;
import yvs.parametrage.SectionDeValorisation;
import yvs.production.base.CentreCharge;
import yvs.production.base.SiteProduction;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PosteCharge implements Serializable {

    private int id;
    private String designation;
    private String description;
    private String type, nameType;
    private String reference;
    private double tauxRendement;
    private double capaciteH;
    private double capaciteQ;
    private double chargeH;
    private double chargeQ;
    private double cadence;
    private double tauxChargeMax;
    private double modsH;
    private int modsQ;
    private double tempsAttente;
    private double tempsTransfert;
    private double tempsRebus;
    private double tempsReglage;
    private double tempsExecution;
    private int posteEquivalent;
    private char typeValeur = 'D';
    private SectionDeValorisation centreVal = new SectionDeValorisation();
    private Calendrier calendrier = new Calendrier();
    private SiteProduction site = new SiteProduction();
    private CentreCharge centre = new CentreCharge();
    private Tiers tiers = new Tiers();
    private Employe employe = new Employe();
    private Articles materiel = new Articles();
    private List<YvsProdEtatRessource> etats;
    private List<YvsProdCentrePosteCharge> centres;
    private List<YvsProdCapacitePosteCharge> capacites;
    private boolean selectActif;

    public PosteCharge() {
        capacites = new ArrayList<>();
        etats = new ArrayList<>();
        centres = new ArrayList<>();
    }

    public PosteCharge(int id) {
        this();
        this.id = id;
    }

    public int getPosteEquivalent() {
        return posteEquivalent;
    }

    public void setPosteEquivalent(int posteEquivalent) {
        this.posteEquivalent = posteEquivalent;
    }

    public List<YvsProdCapacitePosteCharge> getCapacites() {
        return capacites;
    }

    public void setCapacites(List<YvsProdCapacitePosteCharge> capacites) {
        this.capacites = capacites;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public List<YvsProdCentrePosteCharge> getCentres() {
        return centres;
    }

    public void setCentres(List<YvsProdCentrePosteCharge> centres) {
        this.centres = centres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SiteProduction getSite() {
        return site;
    }

    public void setSite(SiteProduction site) {
        this.site = site;
    }

    public CentreCharge getCentre() {
        return centre;
    }

    public void setCentre(CentreCharge centre) {
        this.centre = centre;
    }

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : "M" : "M";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getTauxRendement() {
        return tauxRendement;
    }

    public void setTauxRendement(double tauxRendement) {
        this.tauxRendement = tauxRendement;
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(Calendrier calendrier) {
        this.calendrier = calendrier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getTempsAttente() {
        return tempsAttente;
    }

    public void setTempsAttente(double tempsAttente) {
        this.tempsAttente = tempsAttente;
    }

    public double getTempsTransfert() {
        return tempsTransfert;
    }

    public void setTempsTransfert(double tempsTransfert) {
        this.tempsTransfert = tempsTransfert;
    }

    public double getTempsRebus() {
        return tempsRebus;
    }

    public void setTempsRebus(double tempsRebus) {
        this.tempsRebus = tempsRebus;
    }

    public double getTempsReglage() {
        return tempsReglage;
    }

    public void setTempsReglage(double tempsReglage) {
        this.tempsReglage = tempsReglage;
    }

    public double getTempsExecution() {
        return tempsExecution;
    }

    public void setTempsExecution(double tempsExecution) {
        this.tempsExecution = tempsExecution;
    }

    public double getCapaciteH() {
        return capaciteH;
    }

    public void setCapaciteH(double capaciteH) {
        this.capaciteH = capaciteH;
    }

    public double getCapaciteQ() {
        return capaciteQ;
    }

    public void setCapaciteQ(double capaciteQ) {
        this.capaciteQ = capaciteQ;
    }

    public double getChargeH() {
        return chargeH;
    }

    public void setChargeH(double chargeH) {
        this.chargeH = chargeH;
    }

    public double getChargeQ() {
        return chargeQ;
    }

    public void setChargeQ(double chargeQ) {
        this.chargeQ = chargeQ;
    }

    public double getCadence() {
        return cadence;
    }

    public void setCadence(double cadence) {
        this.cadence = cadence;
    }

    public double getTauxChargeMax() {
        return tauxChargeMax;
    }

    public void setTauxChargeMax(double tauxChargeMax) {
        this.tauxChargeMax = tauxChargeMax;
    }

    public double getModsH() {
        return modsH;
    }

    public void setModsH(double modsH) {
        this.modsH = modsH;
    }

    public int getModsQ() {
        return modsQ;
    }

    public void setModsQ(int modsQ) {
        this.modsQ = modsQ;
    }

    public SectionDeValorisation getCentreVal() {
        return centreVal;
    }

    public void setCentreVal(SectionDeValorisation centreVal) {
        this.centreVal = centreVal;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Articles getMateriel() {
        return materiel;
    }

    public void setMateriel(Articles materiel) {
        this.materiel = materiel;
    }

    public List<YvsProdEtatRessource> getEtats() {
        return etats;
    }

    public void setEtats(List<YvsProdEtatRessource> etats) {
        this.etats = etats;
    }

    public char getTypeValeur() {
        return typeValeur;
    }

    public void setTypeValeur(char typeValeur) {
        this.typeValeur = typeValeur;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.id;
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
        final PosteCharge other = (PosteCharge) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.designation;
    }

}
