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
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.FamilleArticle;
import yvs.entity.commercial.rrr.YvsComGrilleRistourne;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Ristourne implements Serializable {

    private long id;
    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private boolean actif = true;
    private boolean permanent;
    private Articles article = new Articles();
    private FamilleArticle famille = new FamilleArticle();
    private Conditionnement conditionnement = new Conditionnement();
    private PlanRistourne plan = new PlanRistourne();
    private double montant;
    private String natureMontant = Constantes.NATURE_TAUX;
    private String base = Constantes.BASE_CATTC;
    private char nature = 'R';    //ristourne ou bonus R=ristourne, B=Bonus
    private Date dateSave = new Date();
    private List<YvsComGrilleRistourne> tranches;
    private boolean selectActif, new_, unique, update, forArticle = true, applyOnArticle = true;

    public Ristourne() {
        tranches = new ArrayList<>();
    }

    public Ristourne(long id) {
        this();
        this.id = id;
    }

    public Ristourne(long id, Ristourne y) {
        this.id = id;
        this.permanent = y.permanent;
        this.montant = y.montant;
        this.tranches = y.tranches;
        this.selectActif = y.selectActif;
        this.new_ = y.new_;
        this.unique = y.unique;
        this.update = y.update;
        this.applyOnArticle = y.applyOnArticle;
    }

    public String getNatureMontant() {
        return natureMontant;
    }

    public void setNatureMontant(String natureMontant) {
        this.natureMontant = natureMontant;
    }

    public String getBase() {
        return base != null ? base.trim().length() > 0 ? base : Constantes.BASE_CATTC : Constantes.BASE_CATTC;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public PlanRistourne getPlan() {
        return plan;
    }

    public void setPlan(PlanRistourne plan) {
        this.plan = plan;
    }

    public List<YvsComGrilleRistourne> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsComGrilleRistourne> tranches) {
        this.tranches = tranches;
    }

    public FamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(FamilleArticle famille) {
        this.famille = famille;
    }

    public boolean isForArticle() {
        return forArticle;
    }

    public void setForArticle(boolean forArticle) {
        this.forArticle = forArticle;
    }

    public boolean isApplyOnArticle() {
        return applyOnArticle;
    }

    public void setApplyOnArticle(boolean applyOnArticle) {
        this.applyOnArticle = applyOnArticle;
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

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
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

    public char getNature() {
        return nature;
    }

    public void setNature(char nature) {
        this.nature = nature;
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
        final Ristourne other = (Ristourne) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
