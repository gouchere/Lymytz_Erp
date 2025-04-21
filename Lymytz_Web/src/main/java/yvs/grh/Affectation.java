/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.param.poste.YvsGrhPosteEmployes;
import yvs.grh.bean.Employe;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.poste.PosteDeTravail;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Affectation implements Serializable {

//    private String prevAgence, prevDep, prevService, prevPoste;
    private Date prevDate;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private long id;
    private boolean valider = false, posteActif;
    private boolean activCheckBox = false;
    private String typeAffectation; //AF(Affectation) MU (Mutation au sein du groupe)
    private PosteDeTravail poste = new PosteDeTravail();
    private Employe employe = new Employe();
    private Date date = new Date();
    private char statut = 'E';
    private Date dateDebut;
    private Date dateFinInterim;
    private boolean indemnisable;
    private boolean changeAgence;
    private String motifAffectation;
    private Agence agence = new Agence();
    private Date dateConfirmation;
    private PosteDeTravail postePrecedent = new PosteDeTravail();
    private List<YvsGrhPosteEmployes> historiques;

    public Affectation() {
        historiques = new ArrayList<>();
    }

    public Affectation(long idAffectation) {
        historiques = new ArrayList<>();
        this.id = idAffectation;
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

    public boolean isChangeAgence() {
        return changeAgence;
    }

    public void setChangeAgence(boolean changeAgence) {
        this.changeAgence = changeAgence;
    }

    public String getMotifAffectation() {
        return motifAffectation;
    }

    public void setMotifAffectation(String motifAffectation) {
        this.motifAffectation = motifAffectation;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isActivCheckBox() {
        return activCheckBox;
    }

    public PosteDeTravail getPoste() {
        return poste;
    }

    public void setPoste(PosteDeTravail poste) {
        this.poste = poste;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public void setActivCheckBox(boolean activCheckBox) {
        this.activCheckBox = activCheckBox;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isValider() {
        return valider;
    }

    public void setValider(boolean valider) {
        this.valider = valider;
    }

    public boolean isPosteActif() {
        return posteActif;
    }

    public void setIndemnisable(boolean indemnisable) {
        this.indemnisable = indemnisable;
    }

    public boolean isIndemnisable() {
        return indemnisable;
    }

    public void setPosteActif(boolean posteActif) {
        this.posteActif = posteActif;
    }

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public Date getDateDebut() {
        return dateDebut != null ? dateDebut : new Date();
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFinInterim() {
        return dateFinInterim;
    }

    public void setDateFinInterim(Date dateFinInterim) {
        this.dateFinInterim = dateFinInterim;
    }

    public String getTypeAffectation() {
        return typeAffectation;
    }

    public void setTypeAffectation(String typeAffectation) {
        this.typeAffectation = typeAffectation;
    }

    public Date getPrevDate() {
        return prevDate;
    }

    public void setPrevDate(Date prevDate) {
        this.prevDate = prevDate;
    }

    public Date getDateConfirmation() {
        return dateConfirmation;
    }

    public void setDateConfirmation(Date dateConfirmation) {
        this.dateConfirmation = dateConfirmation;
    }

    public PosteDeTravail getPostePrecedent() {
        return postePrecedent;
    }

    public void setPostePrecedent(PosteDeTravail postePrecedent) {
        this.postePrecedent = postePrecedent;
    }

    public List<YvsGrhPosteEmployes> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsGrhPosteEmployes> historiques) {
        this.historiques = historiques;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Affectation other = (Affectation) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
