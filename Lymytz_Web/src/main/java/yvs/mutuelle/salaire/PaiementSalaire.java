/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.salaire;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.Mutualiste;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PaiementSalaire implements Serializable {

    private long id;
    private Date datePaiement = new Date();
    private double montantPaye;     //montant éffectivement payé
    private double montantAPayer;   //montant à payer: celui qui vient des RH
    private String commentaire;
    private Mutualiste mutualiste = new Mutualiste();
    private boolean selectActif, new_;
    private double soeCredit, soeAcompte, epargneDuMois;
    private boolean payer;
    private String chainePeriode;
    private List<AvanceSalaire> avances;
    private PeriodeSalaireMut periode = new PeriodeSalaireMut();
    private double soldeCompteSalaire;

    public PaiementSalaire() {
        avances = new ArrayList<>();
    }

    public double getSoldeCompteSalaire() {
        return soldeCompteSalaire;
    }

    public void setSoldeCompteSalaire(double soldeCompteSalaire) {
        this.soldeCompteSalaire = soldeCompteSalaire;
    }

    public PaiementSalaire(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(double montantPaye) {
        this.montantPaye = montantPaye;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
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

    public double getSoeCredit() {
        return soeCredit;
    }

    public void setSoeCredit(double soeCredit) {
        this.soeCredit = soeCredit;
    }

    public double getSoeAcompte() {
        return soeAcompte;
    }

    public void setSoeAcompte(double soeAcompte) {
        this.soeAcompte = soeAcompte;
    }

    public double getMontantAPayer() {
        return montantAPayer;
    }

    public void setMontantAPayer(double montantAPayer) {
        this.montantAPayer = montantAPayer;
    }

    public double getEpargneDuMois() {
        return epargneDuMois;
    }

    public void setEpargneDuMois(double epargneDuMois) {
        this.epargneDuMois = epargneDuMois;
    }

    public boolean isPayer() {
        return payer;
    }

    public void setPayer(boolean payer) {
        this.payer = payer;
    }

    public String getChainePeriode() {
        return chainePeriode;
    }

    public void setChainePeriode(String chainePeriode) {
        this.chainePeriode = chainePeriode;
    }

    public List<AvanceSalaire> getAvances() {
        return avances;
    }

    public void setAvances(List<AvanceSalaire> avances) {
        this.avances = avances;
    }

    public PeriodeSalaireMut getPeriode() {
        return periode;
    }

    public void setPeriode(PeriodeSalaireMut periode) {
        this.periode = periode;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final PaiementSalaire other = (PaiementSalaire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
