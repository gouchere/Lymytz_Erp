/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
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
public class FicheLivraisonFournisseur implements Serializable {

    private long id;
    private Fournisseur fournisseur = new Fournisseur();
    private FicheApprovisionnement ficheApprov = new FicheApprovisionnement();
    private String etat = Constantes.ETAT_EDITABLE;
    private List<ArticleFourniAchat> articles;
    private boolean selectActif, new_, int_;

    public FicheLivraisonFournisseur() {
        articles = new ArrayList<>();
    }

    public FicheLivraisonFournisseur(long id) {
        this.id = id;
        articles = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public FicheApprovisionnement getFicheApprov() {
        return ficheApprov;
    }

    public void setFicheApprov(FicheApprovisionnement ficheApprov) {
        this.ficheApprov = ficheApprov;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public List<ArticleFourniAchat> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleFourniAchat> articles) {
        this.articles = articles;
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

    public boolean isInt_() {
        return int_;
    }

    public void setInt_(boolean int_) {
        this.int_ = int_;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final FicheLivraisonFournisseur other = (FicheLivraisonFournisseur) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
