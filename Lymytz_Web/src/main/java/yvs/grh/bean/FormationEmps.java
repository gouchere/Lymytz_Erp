/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class FormationEmps implements Serializable {

    private long id;
    private Date dateDebut, dateFin, dateFormation = new Date();
    private Dictionnaire ville = new Dictionnaire();
    private boolean valider;
    private Employe employe = new Employe();
    private boolean diplomee;
    private double coutTotal = 0;
    private List<CoutFormation> coutFormation;

    public FormationEmps() {
        coutFormation = new ArrayList<>();
    }

    public FormationEmps(long id) {
        this.id = id;
        coutFormation = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(double coutTotal) {
        this.coutTotal = coutTotal;
    }

    public Dictionnaire getVille() {
        return ville;
    }

    public void setVille(Dictionnaire ville) {
        this.ville = ville;
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

    public Date getDateFormation() {
        return dateFormation;
    }

    public void setDateFormation(Date dateFormation) {
        this.dateFormation = dateFormation;
    }

    public boolean isValider() {
        return valider;
    }

    public void setValider(boolean valider) {
        this.valider = valider;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public boolean isDiplomee() {
        return diplomee;
    }

    public void setDiplomee(boolean diplomee) {
        this.diplomee = diplomee;
    }

    public List<CoutFormation> getCoutFormation() {
        return coutFormation;
    }

    public void setCoutFormation(List<CoutFormation> coutFormation) {
        this.coutFormation = coutFormation;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final FormationEmps other = (FormationEmps) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
