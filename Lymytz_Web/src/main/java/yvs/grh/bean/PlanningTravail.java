/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class PlanningTravail implements Serializable {

    private long id;

    private Date heureDebut = new Date(), heureFin = new Date(), jour, dateFin = new Date(), dateDebut = new Date(), pause = new Date();
    private String strDay;
    private Employe employe = new Employe();
//    private List<Employe> employes;
    private boolean valider, supplementaire;
    private boolean selectActif;
    private int nbreEmps;

    public PlanningTravail() {
//        employes = new ArrayList<>();
    }

//    public List<Employe> getEmployes() {
//        return employes;
//    }
//
//    public void setEmployes(List<Employe> employes) {
//        this.employes = employes;
//    }
    public Date getPause() {
        return pause;
    }

    public void setPause(Date pause) {
        this.pause = pause;
    }

    public boolean isValider() {
        return valider;
    }

    public void setValider(boolean valider) {
        this.valider = valider;
    }

    public boolean isSupplementaire() {
        return supplementaire;
    }

    public void setSupplementaire(boolean supplementaire) {
        this.supplementaire = supplementaire;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public PlanningTravail(long id) {
        this.id = id;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public Date getJour() {
        return jour;
    }

    public void setJour(Date jour) {
        this.jour = jour;
    }

    public String getStrDay() {
        return strDay;
    }

    public void setStrDay(String strDay) {
        this.strDay = strDay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public int getNbreEmps() {
        return nbreEmps;
    }

    public void setNbreEmps(int nbreEmps) {
        this.nbreEmps = nbreEmps;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PlanningTravail other = (PlanningTravail) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
