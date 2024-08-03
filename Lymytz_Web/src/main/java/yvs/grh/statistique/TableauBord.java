/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.statistique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.CartesianChartModel;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.param.poste.YvsGrhPosteEmployes;
import yvs.entity.grh.personnel.YvsGrhConventionEmploye;
import yvs.grh.bean.Employe;
import yvs.mutuelle.Exercice;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.poste.Departements;
import yvs.parametrage.societe.Societe;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class TableauBord implements Serializable {

    private Date datePrec, dateSuiv;
    private Societe societe = new Societe();
    private Exercice exercice = new Exercice();
    private Agence agence = new Agence();
    private Departements service = new Departements();
    private Employe employe = new Employe();

    private String eltTriGestEmps;
    private String periode = "M";
    private String type = "sal";
    private CartesianChartModel barModel;

    private double chargeMission;
    private double chargeCotisation;
    private double chargeFormation;
    private double masseSalariale, allMasseSalarial;
    //Gestion Retenue
    private double retenuEncours, retenuRegle;

    private long totalPoste, totalPosteBuzz;
    private long nbreBulletin;
    private long contratSuspendu, contratNouveau;

    private List<ContentDuree> contents;
    private List<ContentDuree> contentsCumul;
    private List<YvsGrhElementAdditionel> retenues;
    private List<YvsGrhConventionEmploye> conventions;
    private List<YvsGrhPosteEmployes> affectations;

    public TableauBord() {
        barModel = new CartesianChartModel();
        datePrec = new Date();
        dateSuiv = new Date();
        retenues = new ArrayList<>();
        contents = new ArrayList<>();
        contentsCumul = new ArrayList<>();
        conventions = new ArrayList<>();
        affectations = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CartesianChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(CartesianChartModel barModel) {
        this.barModel = barModel;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public List<YvsGrhConventionEmploye> getConventions() {
        return conventions;
    }

    public void setConventions(List<YvsGrhConventionEmploye> conventions) {
        this.conventions = conventions;
    }

    public List<YvsGrhPosteEmployes> getAffectations() {
        return affectations;
    }

    public void setAffectations(List<YvsGrhPosteEmployes> affectations) {
        this.affectations = affectations;
    }

    public double getChargeMission() {
        return chargeMission;
    }

    public void setChargeMission(double chargeMission) {
        this.chargeMission = chargeMission;
    }

    public double getChargeFormation() {
        return chargeFormation;
    }

    public void setChargeFormation(double chargeFormation) {
        this.chargeFormation = chargeFormation;
    }

    public long getTotalPoste() {
        return totalPoste;
    }

    public void setTotalPoste(long totalPoste) {
        this.totalPoste = totalPoste;
    }

    public long getTotalPosteBuzz() {
        return totalPosteBuzz;
    }

    public void setTotalPosteBuzz(long totalPosteBuzz) {
        this.totalPosteBuzz = totalPosteBuzz;
    }

    public long getContratSuspendu() {
        return contratSuspendu;
    }

    public void setContratSuspendu(long contratSuspendu) {
        this.contratSuspendu = contratSuspendu;
    }

    public long getContratNouveau() {
        return contratNouveau;
    }

    public void setContratNouveau(long contratNouveau) {
        this.contratNouveau = contratNouveau;
    }

    public double getAllMasseSalarial() {
        return allMasseSalarial;
    }

    public void setAllMasseSalarial(double allMasseSalarial) {
        this.allMasseSalarial = allMasseSalarial;
    }

    public long getNbreBulletin() {
        return nbreBulletin;
    }

    public void setNbreBulletin(long nbreBulletin) {
        this.nbreBulletin = nbreBulletin;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public List<ContentDuree> getContentsCumul() {
        return contentsCumul;
    }

    public void setContentsCumul(List<ContentDuree> contentsCumul) {
        this.contentsCumul = contentsCumul;
    }

    public List<ContentDuree> contentsCumulByList() {
        contentsCumul.clear();
        ContentDuree y;
        for (ContentDuree c : contents) {
            y = null;
            for (ContentDuree c_ : contentsCumul) {
                if (c_.getAgence() == c.getAgence() && c_.getElement().equals(c.getElement())) {
                    y = c_;
                    break;
                }
            }
            if (y != null ? y.getAgence() > 0 : false) {
                int idx = contentsCumul.indexOf(new ContentDuree(y.getElement()));
                if (idx > -1) {
                    y.setValeur(y.getValeur() + c.getValeur());
                    contentsCumul.set(idx, y);
                } else {
                    c.setEmploye(0);
                    contentsCumul.add(c);
                }
            } else {
                c.setEmploye(0);
                contentsCumul.add(c);
            }
        }
        return contentsCumul;
    }

    public List<ContentDuree> getContents() {
        return contents;
    }

    public void setContents(List<ContentDuree> contents) {
        this.contents = contents;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public Departements getService() {
        return service;
    }

    public void setService(Departements service) {
        this.service = service;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public double getRetenuEncours() {
        return retenuEncours;
    }

    public void setRetenuEncours(double retenuEncours) {
        this.retenuEncours = retenuEncours;
    }

    public double getRetenuRegle() {
        return retenuRegle;
    }

    public void setRetenuRegle(double retenuRegle) {
        this.retenuRegle = retenuRegle;
    }

    public List<YvsGrhElementAdditionel> getRetenues() {
        return retenues;
    }

    public void setRetenues(List<YvsGrhElementAdditionel> retenues) {
        this.retenues = retenues;
    }

    public String getEltTriGestEmps() {
        return eltTriGestEmps;
    }

    public void setEltTriGestEmps(String eltTriGestEmps) {
        this.eltTriGestEmps = eltTriGestEmps;
    }

    public Societe getSociete() {
        return societe;
    }

    public double getChargeCotisation() {
        return chargeCotisation;
    }

    public void setChargeCotisation(double chargeCotisation) {
        this.chargeCotisation = chargeCotisation;
    }

    public double getMasseSalariale() {
        return masseSalariale;
    }

    public void setMasseSalariale(double masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public Date getDatePrec() {
        return datePrec;
    }

    public void setDatePrec(Date datePrec) {
        this.datePrec = datePrec;
    }

    public Date getDateSuiv() {
        return dateSuiv;
    }

    public void setDateSuiv(Date dateSuiv) {
        this.dateSuiv = dateSuiv;
    }

    public String nameCharacter(String valeur) {
        if (valeur != null) {
            switch(valeur){
                case "sal":
                    return "Salaires";
                case "pre":
                    return "Présences";
                case "con":
                    return "Congés";
                case "per":
                    return "Permissions";
               default:
                    return valeur;
            }
        }
        return valeur;
    }
}
