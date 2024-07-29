/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.client;

import java.io.Serializable;
import java.util.Date;
import yvs.base.produits.Conditionnement;

/**
 *
 * @author Lymytz Dowes
 */
public class ElementContratsClient implements Serializable {

    private long id;
    private double quantite = 1;
    private double prix;
    private Date dateSave = new Date();
    private ContratsClient contrat = new ContratsClient();
    private Conditionnement article = new Conditionnement();

    public ElementContratsClient() {
    }

    public ElementContratsClient(long id) {
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
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public ContratsClient getContrat() {
        return contrat;
    }

    public void setContrat(ContratsClient contrat) {
        this.contrat = contrat;
    }

    public Conditionnement getArticle() {
        return article;
    }

    public void setArticle(Conditionnement article) {
        this.article = article;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ElementContratsClient other = (ElementContratsClient) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
