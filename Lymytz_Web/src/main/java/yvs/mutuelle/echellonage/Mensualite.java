/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.echellonage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.echellonage.YvsMutReglementMensualite;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Mensualite implements Serializable, Comparator<Mensualite> {

    private long id;
    private Date dateMensualite = new Date();
    private double montant, montantReste, montantVerse;
    private double interet, amortissement, montantPenalite, montantReel;
    private String etat;
    private String commentaire;
    private boolean selectActif, new_, marquer;
    private Echellonage echeancier = new Echellonage();
    private List<YvsMutReglementMensualite> reglements;

    public Mensualite() {
        reglements = new ArrayList<>();
    }

    public Mensualite(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontantPenalite() {
        return montantPenalite;
    }

    public void setMontantPenalite(double montantPenalite) {
        this.montantPenalite = montantPenalite;
    }

    public double getMontantReel() {
        return montantReel;
    }

    public void setMontantReel(double montantReel) {
        this.montantReel = montantReel;
    }

    public boolean isMarquer() {
        return marquer;
    }

    public void setMarquer(boolean marquer) {
        this.marquer = marquer;
    }

    public double getMontantVerse() {
        return montantVerse;
    }

    public void setMontantVerse(double montantVerse) {
        this.montantVerse = montantVerse;
    }

    public double getMontantReste() {
        return montantReste;
    }

    public void setMontantReste(double montantReste) {
        this.montantReste = montantReste;
    }

    public List<YvsMutReglementMensualite> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsMutReglementMensualite> reglements) {
        this.reglements = reglements;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateMensualite() {
        return dateMensualite;
    }

    public void setDateMensualite(Date dateMensualite) {
        this.dateMensualite = dateMensualite;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public double getAmortissement() {
        return amortissement;
    }

    public void setAmortissement(double amortissement) {
        this.amortissement = amortissement;
    }

    public double getInteret() {
        return interet;
    }

    public void setInteret(double interet) {
        this.interet = interet;
    }

    public Echellonage getEcheancier() {
        return echeancier;
    }

    public void setEcheancier(Echellonage echeancier) {
        this.echeancier = echeancier;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Mensualite other = (Mensualite) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compare(Mensualite o1, Mensualite o2) {
        if (o1.getDateMensualite() == null) {
            return -1;
        } else if (o2.getDateMensualite() == null) {
            return 1;
        } else if (o1.getDateMensualite().after(o2.getDateMensualite())) {
            return 1;
        } else if (o1.getDateMensualite().before(o2.getDateMensualite())) {
            return -1;
        } else {
            return 0;
        }
    }

}
