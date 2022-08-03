/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.commission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.Commerciales;
import yvs.commercial.objectifs.PeriodesObjectifs;
import yvs.entity.compta.YvsComptaCaissePieceCommission;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class CommissionCommerciaux implements Serializable {

    private long id;
    private double montant;
    private String numero;
    private String statut;
    private Date dateUpdate = new Date();
    private Date dateSave = new Date();
    private PeriodesObjectifs periode;
    private Commerciales commerciaux;
    private List<YvsComptaCaissePieceCommission> reglements;

    public CommissionCommerciaux() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.reglements = new ArrayList<>();
    }

    public CommissionCommerciaux(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public PeriodesObjectifs getPeriode() {
        return periode;
    }

    public void setPeriode(PeriodesObjectifs periode) {
        this.periode = periode;
    }

    public Commerciales getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(Commerciales commerciaux) {
        this.commerciaux = commerciaux;
    }

    public List<YvsComptaCaissePieceCommission> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceCommission> reglements) {
        this.reglements = reglements;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final CommissionCommerciaux other = (CommissionCommerciaux) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
