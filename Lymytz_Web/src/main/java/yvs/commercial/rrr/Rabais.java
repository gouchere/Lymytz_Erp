/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rrr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Conditionnement;
import yvs.commercial.depot.ArticleDepot;
import yvs.commercial.depot.PointVente;
import yvs.entity.commercial.rrr.YvsComGrilleRabais;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Rabais implements Serializable {

    private long id; 
    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private boolean actif = true;
    private boolean permanent;
    private double montant;
    private ArticleDepot article = new ArticleDepot();
    private PointVente point = new PointVente();
    private Conditionnement conditionnement = new Conditionnement();
    private Date dateSave = new Date();
    private List<YvsComGrilleRabais> tranches;
    private boolean selectActif, new_, unique, update;

    public Rabais() {
        tranches = new ArrayList<>();
    }

    public Rabais(long id) {
        this();
        this.id = id;
    }

    public PointVente getPoint() {
        return point;
    }

    public void setPoint(PointVente point) {
        this.point = point;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public List<YvsComGrilleRabais> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsComGrilleRabais> tranches) {
        this.tranches = tranches;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Date getDateDebut() {
        return dateDebut != null ? dateDebut : new Date();
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin != null ? dateFin : new Date();
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArticleDepot getArticle() {
        return article;
    }

    public void setArticle(ArticleDepot article) {
        this.article = article;
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

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Rabais other = (Rabais) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
