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
import yvs.base.produits.Conditionnement;

/**
 *
 * @author Lymytz-pc
 */
@ManagedBean
@SessionScoped
public class ProformaVenteContenu implements Serializable {

    private long id;
    private double quantite = 1;
    private double prix;
    private Date dateSave = new Date();

    private Conditionnement conditionnement = new Conditionnement();
    private ProformaVente proforma = new ProformaVente();

    public ProformaVenteContenu() {
    }

    public ProformaVenteContenu(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public ProformaVente getProforma() {
        return proforma;
    }

    public void setProforma(ProformaVente proforma) {
        this.proforma = proforma;
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
        final ProformaVenteContenu other = (ProformaVenteContenu) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProformatVenteContenu{" + "id=" + id + '}';
    }

}
