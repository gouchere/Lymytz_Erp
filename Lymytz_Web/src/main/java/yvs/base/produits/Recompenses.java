/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.BeanDeBase;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "recompense")
@SessionScoped
public class Recompenses extends BeanDeBase implements Serializable {

    private long id;
    private String designation;
    private String nature="Comission";//Comission ou Ristourne    
    private String duree;
    private Date debut;
    private Date fin;
    private String objectif;
    private String baseCalcul;
    private List<ArticleRecompense> listArticle;  

    public Recompenses() {
        listArticle = new ArrayList<>();
    }

    public Recompenses(long id, String designation, String nature) {
        this.id = id;
        this.designation = designation;
        this.nature = nature;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public List<ArticleRecompense> getListArticle() {
        return listArticle;
    }

    public void setListArticle(List<ArticleRecompense> listArticle) {
        this.listArticle = listArticle;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getBaseCalcul() {
        return baseCalcul;
    }

    public void setBaseCalcul(String baseCalcul) {
        this.baseCalcul = baseCalcul;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Recompenses other = (Recompenses) obj;
        if (!Objects.equals(this.designation, other.designation)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.designation);
        return hash;
    }

//    public List<Recompenses> getListArticle1() {
//        return listArticle1;
//    }
//
//    public void setListArticle1(List<Recompenses> listArticle1) {
//        this.listArticle1 = listArticle1;
//    }
    
    
}
