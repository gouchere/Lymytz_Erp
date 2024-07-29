/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rations;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.objectifs.PeriodesObjectifs;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.entrepot.Depots;
import yvs.users.Users;
import yvs.util.Constantes;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class DocRations implements Serializable {

    private long id;
    private int nbrJrUsine = 30;
    private Depots depot = new Depots();
    private String numDoc;
    private String numReference;
    private PeriodesObjectifs periode = new PeriodesObjectifs();
    private char statut = Constantes.STATUT_DOC_EDITABLE;
    private Users validBy = new Users();
    private Creneau creneauHoraire = new Creneau();
    private Date dateFiche = new Date();
    private Date dateSave = new Date();
    private boolean cloturer = false;

    public DocRations() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNbrJrUsine() {
        return nbrJrUsine;
    }

    public void setNbrJrUsine(int nbrJrUsine) {
        this.nbrJrUsine = nbrJrUsine;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNumReference() {
        return numReference;
    }

    public void setNumReference(String numReference) {
        this.numReference = numReference;
    }

    public PeriodesObjectifs getPeriode() {
        return periode;
    }

    public void setPeriode(PeriodesObjectifs periode) {
        this.periode = periode;
    }

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public Users getValidBy() {
        return validBy;
    }

    public void setValidBy(Users validBy) {
        this.validBy = validBy;
    }

    public Creneau getCreneauHoraire() {
        return creneauHoraire;
    }

    public void setCreneauHoraire(Creneau creneauHoraire) {
        this.creneauHoraire = creneauHoraire;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateFiche() {
        return dateFiche != null ? dateFiche : new Date();
    }

    public void setDateFiche(Date dateFiche) {
        this.dateFiche = dateFiche;
    }

    public boolean isCloturer() {
        return cloturer;
    }

    public void setCloturer(boolean cloturer) {
        this.cloturer = cloturer;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final DocRations other = (DocRations) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
