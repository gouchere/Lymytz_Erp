/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.entity.production.pilotage.YvsProdContenuConditionnement;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class FicheConditionnement implements Serializable {

    private long id;
    private Date dateConditionnement = new Date();
    private Date dateSave = new Date();
    private String numero;
    private char statut = Constantes.STATUT_DOC_EDITABLE;
    private Depots depot = new Depots();
    private Articles article = new Articles();
    private Conditionnement conditionnement = new Conditionnement();
    private Nomenclature nomenclature = new Nomenclature();
    private double quantite;
    private List<YvsProdContenuConditionnement> contenus;

    public FicheConditionnement() {
        contenus = new ArrayList<>();
    }

    public FicheConditionnement(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero != null ? numero : "";
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Character getStatut() {
        return statut != ' ' ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_EDITABLE : Constantes.STATUT_DOC_EDITABLE;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateConditionnement() {
        return dateConditionnement;
    }

    public void setDateConditionnement(Date dateConditionnement) {
        this.dateConditionnement = dateConditionnement;
    }

    public Nomenclature getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(Nomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public List<YvsProdContenuConditionnement> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsProdContenuConditionnement> contenus) {
        this.contenus = contenus;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final FicheConditionnement other = (FicheConditionnement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
