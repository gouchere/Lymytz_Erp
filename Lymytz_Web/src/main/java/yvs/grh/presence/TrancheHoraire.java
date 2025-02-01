/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.presence;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class TrancheHoraire implements Serializable {

    private long id;
    private String titre;
    private Date heureDebut = new Date();
    private Date heureFin = new Date();
    private Date dureePause = new Date();
    private Date dateSave = new Date();
    private String typeJournee;
    private boolean venteOnline;
    private boolean actif, new_;

    public TrancheHoraire() {
    }

    public TrancheHoraire(long id) {
        this.id = id;
    }

    public Date getDureePause() {
        return dureePause;
    }

    public void setDureePause(Date dureePause) {
        this.dureePause = dureePause;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isVenteOnline() {
        return venteOnline;
    }

    public void setVenteOnline(boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getHeureDebut() {
        return heureDebut;
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

    public String getTypeJournee() {
        return typeJournee;
    }

    public void setTypeJournee(String typeJournee) {
        this.typeJournee = typeJournee;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final TrancheHoraire other = (TrancheHoraire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
