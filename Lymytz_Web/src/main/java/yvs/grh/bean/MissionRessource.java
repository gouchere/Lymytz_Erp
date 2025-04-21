/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import yvs.base.produits.Articles;
import yvs.entity.produits.YvsBaseArticles;

/**
 *
 * @author user1
 */
//@ManagedBean
//@SessionScoped
public class MissionRessource implements Serializable{
    private long id;
    private YvsBaseArticles  article = new YvsBaseArticles();
//    private Mission mission = new Mission();
    private int quantite;
    private boolean  supp;

    public MissionRessource() {
    }

    public MissionRessource(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

//    public Mission getMission() {
//        return mission;
//    }
//
//    public void setMission(Mission mission) {
//        this.mission = mission;
//    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
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
        final MissionRessource other = (MissionRessource) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
}
