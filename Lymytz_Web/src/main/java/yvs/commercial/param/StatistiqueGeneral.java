/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.param;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class StatistiqueGeneral implements Serializable {

    // Données des vente
    public long nbreVenteAnnule, nbreVenteAttence, nbreVenteEnCours, nbreVenteValide,
            nbreVenteLivre, nbreVenteNotLivre, nbreVenteEnCoursLivre, nbreVenteRetardLivr,
            nbreVenteRegle, nbreVenteEnCoursRegle, nbreVenteNotRegle, nbreVente, nbreVenteAvoir;
    public double caVenteAnnule, caVenteAttence, caVenteEnCours, caVenteValide, caVenteValideCS, caVenteValideSS,
            caVenteLivre, caVenteEnCoursLivre, caVenteNotLivre, caVenteRetardLivr,
            caVenteRegle, caVenteEnCoursRegle, caVenteNotRegle, caVente, caVenteAvoir;

    public StatistiqueGeneral() {
    }

    public long getNbreVenteAnnule() {
        return nbreVenteAnnule;
    }

    public void setNbreVenteAnnule(long nbreVenteAnnule) {
        this.nbreVenteAnnule = nbreVenteAnnule;
    }

    public long getNbreVenteAttence() {
        return nbreVenteAttence;
    }

    public void setNbreVenteAttence(long nbreVenteAttence) {
        this.nbreVenteAttence = nbreVenteAttence;
    }

    public long getNbreVenteEnCours() {
        return nbreVenteEnCours;
    }

    public void setNbreVenteEnCours(long nbreVenteEnCours) {
        this.nbreVenteEnCours = nbreVenteEnCours;
    }

    public long getNbreVenteValide() {
        return nbreVenteValide;
    }

    public void setNbreVenteValide(long nbreVenteValide) {
        this.nbreVenteValide = nbreVenteValide;
    }

    public long getNbreVenteLivre() {
        return nbreVenteLivre;
    }

    public void setNbreVenteLivre(long nbreVenteLivre) {
        this.nbreVenteLivre = nbreVenteLivre;
    }

    public long getNbreVenteNotLivre() {
        return nbreVenteNotLivre;
    }

    public void setNbreVenteNotLivre(long nbreVenteNotLivre) {
        this.nbreVenteNotLivre = nbreVenteNotLivre;
    }

    public long getNbreVenteEnCoursLivre() {
        return nbreVenteEnCoursLivre;
    }

    public void setNbreVenteEnCoursLivre(long nbreVenteEnCoursLivre) {
        this.nbreVenteEnCoursLivre = nbreVenteEnCoursLivre;
    }

    public long getNbreVenteRetardLivr() {
        return nbreVenteRetardLivr;
    }

    public void setNbreVenteRetardLivr(long nbreVenteRetardLivr) {
        this.nbreVenteRetardLivr = nbreVenteRetardLivr;
    }

    public long getNbreVenteRegle() {
        return nbreVenteRegle;
    }

    public void setNbreVenteRegle(long nbreVenteRegle) {
        this.nbreVenteRegle = nbreVenteRegle;
    }

    public long getNbreVenteEnCoursRegle() {
        return nbreVenteEnCoursRegle;
    }

    public void setNbreVenteEnCoursRegle(long nbreVenteEnCoursRegle) {
        this.nbreVenteEnCoursRegle = nbreVenteEnCoursRegle;
    }

    public long getNbreVenteNotRegle() {
        return nbreVenteNotRegle;
    }

    public void setNbreVenteNotRegle(long nbreVenteNotRegle) {
        this.nbreVenteNotRegle = nbreVenteNotRegle;
    }

    public long getNbreVente() {
        return nbreVente;
    }

    public void setNbreVente(long nbreVente) {
        this.nbreVente = nbreVente;
    }

    public long getNbreVenteAvoir() {
        return nbreVenteAvoir;
    }

    public void setNbreVenteAvoir(long nbreVenteAvoir) {
        this.nbreVenteAvoir = nbreVenteAvoir;
    }

    public double getCaVenteAnnule() {
        return caVenteAnnule;
    }

    public void setCaVenteAnnule(double caVenteAnnule) {
        this.caVenteAnnule = caVenteAnnule;
    }

    public double getCaVenteAttence() {
        return caVenteAttence;
    }

    public void setCaVenteAttence(double caVenteAttence) {
        this.caVenteAttence = caVenteAttence;
    }

    public double getCaVenteEnCours() {
        return caVenteEnCours;
    }

    public void setCaVenteEnCours(double caVenteEnCours) {
        this.caVenteEnCours = caVenteEnCours;
    }

    public double getCaVenteValide() {
        return caVenteValide;
    }

    public void setCaVenteValide(double caVenteValide) {
        this.caVenteValide = caVenteValide;
    }

    public double getCaVenteValideCS() {
        return caVenteValideCS;
    }

    public void setCaVenteValideCS(double caVenteValideCS) {
        this.caVenteValideCS = caVenteValideCS;
    }

    public double getCaVenteValideSS() {
        return caVenteValideSS;
    }

    public void setCaVenteValideSS(double caVenteValideSS) {
        this.caVenteValideSS = caVenteValideSS;
    }

    public double getCaVenteLivre() {
        return caVenteLivre;
    }

    public void setCaVenteLivre(double caVenteLivre) {
        this.caVenteLivre = caVenteLivre;
    }

    public double getCaVenteEnCoursLivre() {
        return caVenteEnCoursLivre;
    }

    public void setCaVenteEnCoursLivre(double caVenteEnCoursLivre) {
        this.caVenteEnCoursLivre = caVenteEnCoursLivre;
    }

    public double getCaVenteNotLivre() {
        return caVenteNotLivre;
    }

    public void setCaVenteNotLivre(double caVenteNotLivre) {
        this.caVenteNotLivre = caVenteNotLivre;
    }

    public double getCaVenteRetardLivr() {
        return caVenteRetardLivr;
    }

    public void setCaVenteRetardLivr(double caVenteRetardLivr) {
        this.caVenteRetardLivr = caVenteRetardLivr;
    }

    public double getCaVenteRegle() {
        return caVenteRegle;
    }

    public void setCaVenteRegle(double caVenteRegle) {
        this.caVenteRegle = caVenteRegle;
    }

    public double getCaVenteEnCoursRegle() {
        return caVenteEnCoursRegle;
    }

    public void setCaVenteEnCoursRegle(double caVenteEnCoursRegle) {
        this.caVenteEnCoursRegle = caVenteEnCoursRegle;
    }

    public double getCaVenteNotRegle() {
        return caVenteNotRegle;
    }

    public void setCaVenteNotRegle(double caVenteNotRegle) {
        this.caVenteNotRegle = caVenteNotRegle;
    }

    public double getCaVente() {
        return caVente;
    }

    public void setCaVente(double caVente) {
        this.caVente = caVente;
    }

    public double getCaVenteAvoir() {
        return caVenteAvoir;
    }

    public void setCaVenteAvoir(double caVenteAvoir) {
        this.caVenteAvoir = caVenteAvoir;
    }

}
