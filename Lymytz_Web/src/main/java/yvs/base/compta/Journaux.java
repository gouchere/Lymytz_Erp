/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.parametrage.agence.Agence;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz
 */
@ManagedBean
@SessionScoped
public class Journaux implements Serializable {

    private long id;
    private String codejournal;
    private boolean actif;
    private String intitule;
    private String typeJournal;
    private String codeAcces;
    private boolean defaultFor; //indique que le journal est le journal par defaut dlors de la comptabilisation des op d'une fonction donée
    private double soldeJournal;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private Agence agence = new Agence();

    public Journaux() {
    }

    public Journaux(long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodejournal() {
        return codejournal;
    }

    public void setCodejournal(String codejournal) {
        this.codejournal = codejournal;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getTypeJournal() {
        return typeJournal != null ? typeJournal.trim().length() > 0 ? typeJournal : Constantes.ACHAT : Constantes.ACHAT;
    }

    public void setTypeJournal(String typeJournal) {
        this.typeJournal = typeJournal;
    }

    public void setSoldeJournal(double soldeJournal) {
        this.soldeJournal = soldeJournal;
    }

    public double getSoldeJournal() {
        return soldeJournal;
    }

    public void setDefaultFor(boolean defaultFor) {
        this.defaultFor = defaultFor;
    }

    public boolean isDefaultFor() {
        return defaultFor;
    }

    public String getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(String codeAcces) {
        this.codeAcces = codeAcces;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Journaux other = (Journaux) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
