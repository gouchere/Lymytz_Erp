/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.Commerciales;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class CommercialVente implements Serializable {

    private long id;
    private double taux, montant;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private boolean responsable, diminueCa = false;
    private DocVente facture = new DocVente();
    private Commerciales commercial = new Commerciales();

    public CommercialVente() {
    }

    public CommercialVente(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public boolean isResponsable() {
        return responsable;
    }

    public void setResponsable(boolean responsable) {
        this.responsable = responsable;
    }

    public DocVente getFacture() {
        return facture;
    }

    public void setFacture(DocVente facture) {
        this.facture = facture;
    }

    public Commerciales getCommercial() {
        return commercial;
    }

    public void setCommercial(Commerciales commercial) {
        this.commercial = commercial;
    }

    public boolean isDiminueCa() {
        return diminueCa;
    }

    public void setDiminueCa(boolean diminueCa) {
        this.diminueCa = diminueCa;
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
        final CommercialVente other = (CommercialVente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
