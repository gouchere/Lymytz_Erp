/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.dico;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean()
@SessionScoped
public class TarifLieux implements Serializable {

    private long id;
    private Date dateSave = new Date();
    private double fraisLivraison;
    private int delaiForLivraison;
    private int delaiForRetrait;
    private int delaiRetour;
    private boolean livraisonDomicile;
    private Articles article = new Articles();
    private Dictionnaire lieux = new Dictionnaire();

    public TarifLieux() {
    }

    public TarifLieux(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public double getFraisLivraison() {
        return fraisLivraison;
    }

    public void setFraisLivraison(double fraisLivraison) {
        this.fraisLivraison = fraisLivraison;
    }

    public boolean isLivraisonDomicile() {
        return livraisonDomicile;
    }

    public void setLivraisonDomicile(boolean livraisonDomicile) {
        this.livraisonDomicile = livraisonDomicile;
    }

    public int getDelaiForLivraison() {
        return delaiForLivraison;
    }

    public void setDelaiForLivraison(int delaiForLivraison) {
        this.delaiForLivraison = delaiForLivraison;
    }

    public int getDelaiForRetrait() {
        return delaiForRetrait;
    }

    public void setDelaiForRetrait(int delaiForRetrait) {
        this.delaiForRetrait = delaiForRetrait;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public Dictionnaire getLieux() {
        return lieux;
    }

    public void setLieux(Dictionnaire lieux) {
        this.lieux = lieux;
    }

    public int getDelaiRetour() {
        return delaiRetour;
    }

    public void setDelaiRetour(int delaiRetour) {
        this.delaiRetour = delaiRetour;
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
        final TarifLieux other = (TarifLieux) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
