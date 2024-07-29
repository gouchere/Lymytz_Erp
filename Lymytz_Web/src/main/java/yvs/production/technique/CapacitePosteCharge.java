/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;

/**
 *
 * @author Lymytz
 */
@ManagedBean
@SessionScoped
public class CapacitePosteCharge implements Serializable {

    private int id;
    private double capaciteQ;
    private Articles article = new Articles();
    private PosteCharge poste = new PosteCharge();
    private boolean selectActif;

    public CapacitePosteCharge() {
    }

    public CapacitePosteCharge(int id) {
        this.id = id;
    }

    public PosteCharge getPoste() {
        return poste;
    }

    public void setPoste(PosteCharge poste) {
        this.poste = poste;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCapaciteQ() {
        return capaciteQ;
    }

    public void setCapaciteQ(double capaciteQ) {
        this.capaciteQ = capaciteQ;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.id;
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
        final CapacitePosteCharge other = (CapacitePosteCharge) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
