/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

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
public class ContenuPack implements Serializable {

    private long id;
    private double montant;
    private double quantite;
    private double quantiteMax;
    private Date dateSave = new Date();
    private Articles article = new Articles();
    private Conditionnement unite = new Conditionnement();
    private ArticlePack pack = new ArticlePack();

    public ContenuPack() {
    }

    public ContenuPack(long id) {
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

    public double getQuantite() {
        return quantite > 0 ? quantite : 1;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getQuantiteMax() {
        return quantiteMax > 0 ? quantite : 1;
    }

    public void setQuantiteMax(double quantiteMax) {
        this.quantiteMax = quantiteMax;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public Conditionnement getUnite() {
        return unite;
    }

    public void setUnite(Conditionnement unite) {
        this.unite = unite;
    }

    public ArticlePack getPack() {
        return pack;
    }

    public void setPack(ArticlePack pack) {
        this.pack = pack;
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
        final ContenuPack other = (ContenuPack) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
