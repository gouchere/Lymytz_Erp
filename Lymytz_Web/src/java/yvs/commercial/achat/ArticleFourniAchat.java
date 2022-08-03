/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ArticleFourniAchat implements Serializable {

    private long id;
    private double pua;
    private double remise;
    private double quantite;
    private Date dateLivraison = new Date();
    private String etat = Constantes.ETAT_EDITABLE;
    private String nameArticle;
    private Fournisseur fournisseur = new Fournisseur();
    private ArticleApprovisionnement article = new ArticleApprovisionnement();
    private boolean selectActif, new_;

    public ArticleFourniAchat() {
    }

    public ArticleFourniAchat(long id) {
        this.id = id;
    }

    public String getNameArticle() {
        return nameArticle;
    }

    public void setNameArticle(String nameArticle) {
        this.nameArticle = nameArticle;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public ArticleApprovisionnement getArticle() {
        return article;
    }

    public void setArticle(ArticleApprovisionnement article) {
        this.article = article;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPua() {
        return pua;
    }

    public void setPua(double pua) {
        this.pua = pua;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ArticleFourniAchat other = (ArticleFourniAchat) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
