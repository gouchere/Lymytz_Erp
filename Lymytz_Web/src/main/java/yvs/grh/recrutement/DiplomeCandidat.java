/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.recrutement;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import yvs.grh.bean.Diplomes;

/**
 *
 * @author GOUCHERE YVES
 */
public class DiplomeCandidat implements Serializable {

    private long idDiplomeCandidat;
    private Diplomes diplome = new Diplomes();
    private Date dateObtention = new Date();
    private String mention = "Passable";
    private String ecole;
    private boolean newSave;    //marque les données qu'on peut modifier dans une session
    private boolean actif;
    private SpecialitesDiplomeCandidat specialite = new SpecialitesDiplomeCandidat();

    public DiplomeCandidat() {
    }

    public DiplomeCandidat(long id) {
        this.idDiplomeCandidat = id;
    }

    public Date getDateObtention() {
        return dateObtention;
    }

    public void setDateObtention(Date dateObtention) {
        this.dateObtention = dateObtention;
    }

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    public String getEcole() {
        return ecole;
    }

    public void setEcole(String ecole) {
        this.ecole = ecole;
    }

    public void setNewSave(boolean newSave) {
        this.newSave = newSave;
    }

    public boolean isNewSave() {
        return newSave;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public SpecialitesDiplomeCandidat getSpecialite() {
        return specialite;
    }

    public void setSpecialite(SpecialitesDiplomeCandidat specialite) {
        this.specialite = specialite;
    }

    public void setIdDiplomeCandidat(long idDiplomeCandidat) {
        this.idDiplomeCandidat = idDiplomeCandidat;
    }

    public long getIdDiplomeCandidat() {
        return idDiplomeCandidat;
    }

    public Diplomes getDiplome() {
        return diplome;
    }

    public void setDiplome(Diplomes diplome) {
        this.diplome = diplome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.idDiplomeCandidat ^ (this.idDiplomeCandidat >>> 32));
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
        final DiplomeCandidat other = (DiplomeCandidat) obj;
        if (this.idDiplomeCandidat != other.idDiplomeCandidat) {
            return false;
        }
        return true;
    }

}
