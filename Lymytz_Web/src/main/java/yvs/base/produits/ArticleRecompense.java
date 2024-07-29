/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.util.TrancheVal;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "artRecompense")
@SessionScoped
public class ArticleRecompense implements Serializable {

    private String refArt;
    private String groupe;
    private long idArticle;
    private double taux;
    private double valeur;
    private TrancheVal tranche;

    public ArticleRecompense() {
    }

    public ArticleRecompense(String refArt) {
        this.refArt = refArt;
    }

    public long getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(long idArticle) {
        this.idArticle = idArticle;
    }

    public String getRefArt() {
        return refArt;
    }

    public void setRefArt(String refArt) {
        this.refArt = refArt;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public TrancheVal getTranche() {
        return tranche;
    }

    public void setTranche(TrancheVal tranche) {
        this.tranche = tranche;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArticleRecompense other = (ArticleRecompense) obj;
        if (!Objects.equals(this.refArt, other.refArt)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.refArt);
        return hash;
    }
}
