/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ParametreBase implements Serializable {

    private long id;
    private Date dateSave;
    private boolean genererPassword, genererReferenceArticle, monitorPr;
    private String defautPassword, backColorEtiquette = "FFAF02", foreColorEtiquette = "C78801";
    private double tauxEcartPr;
    private int taillePassword, dureeInactivArticle, tailleLettreReferenceArticle, tailleNumeroReferenceArticle, nombreEltAccueil = 5, nombrePageMin = 10, dureeDefaultPassword= 14;

    public ParametreBase() {
    }

    public ParametreBase(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isGenererPassword() {
        return genererPassword;
    }

    public void setGenererPassword(boolean genererPassword) {
        this.genererPassword = genererPassword;
    }

    public String getDefautPassword() {
        return defautPassword;
    }

    public void setDefautPassword(String defautPassword) {
        this.defautPassword = defautPassword;
    }

    public String getBackColorEtiquette() {
        return backColorEtiquette != null ? backColorEtiquette.trim().length() > 0 ? backColorEtiquette : "FFAF02" : "FFAF02";
    }

    public void setBackColorEtiquette(String backColorEtiquette) {
        this.backColorEtiquette = backColorEtiquette;
    }

    public String getForeColorEtiquette() {
        return foreColorEtiquette != null ? foreColorEtiquette.trim().length() > 0 ? foreColorEtiquette : "C78801" : "C78801";
    }

    public void setForeColorEtiquette(String foreColorEtiquette) {
        this.foreColorEtiquette = foreColorEtiquette;
    }

    public int getTaillePassword() {
        return taillePassword;
    }

    public void setTaillePassword(int taillePassword) {
        this.taillePassword = taillePassword;
    }

    public int getDureeInactivArticle() {
        return dureeInactivArticle;
    }

    public void setDureeInactivArticle(int dureeInactivArticle) {
        this.dureeInactivArticle = dureeInactivArticle;
    }

    public boolean isGenererReferenceArticle() {
        return genererReferenceArticle;
    }

    public void setGenererReferenceArticle(boolean genererReferenceArticle) {
        this.genererReferenceArticle = genererReferenceArticle;
    }

    public int getTailleLettreReferenceArticle() {
        return tailleLettreReferenceArticle;
    }

    public void setTailleLettreReferenceArticle(int tailleLettreReferenceArticle) {
        this.tailleLettreReferenceArticle = tailleLettreReferenceArticle;
    }

    public int getTailleNumeroReferenceArticle() {
        return tailleNumeroReferenceArticle;
    }

    public void setTailleNumeroReferenceArticle(int tailleNumeroReferenceArticle) {
        this.tailleNumeroReferenceArticle = tailleNumeroReferenceArticle;
    }

    public int getDureeDefaultPassword() {
        return dureeDefaultPassword;
    }

    public void setDureeDefaultPassword(int dureeDefaultPassword) {
        this.dureeDefaultPassword = dureeDefaultPassword;
    }

    public boolean isMonitorPr() {
        return monitorPr;
    }

    public void setMonitorPr(boolean monitorPr) {
        this.monitorPr = monitorPr;
    }

    public double getTauxEcartPr() {
        return tauxEcartPr;
    }

    public void setTauxEcartPr(double tauxEcartPr) {
        this.tauxEcartPr = tauxEcartPr;
    }

    public int getNombreEltAccueil() {
        return nombreEltAccueil;
    }

    public void setNombreEltAccueil(int nombreEltAccueil) {
        this.nombreEltAccueil = nombreEltAccueil;
    }

    public int getNombrePageMin() {
        return nombrePageMin;
    }

    public void setNombrePageMin(int nombrePageMin) {
        this.nombrePageMin = nombrePageMin;
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
        final ParametreBase other = (ParametreBase) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
