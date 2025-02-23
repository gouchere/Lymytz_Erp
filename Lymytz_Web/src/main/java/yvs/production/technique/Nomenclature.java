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
import yvs.entity.production.base.YvsProdComposantNomenclature;
import yvs.entity.production.base.YvsProdNomenclatureSite;
import yvs.util.Constantes;

/**
 *
 * @author yves
 */
@ManagedBean
@SessionScoped
public class Nomenclature implements Serializable {

    private long id;
    private String reference, unitePreference;
    private String typeNomenclature=Constantes.PROD_TYPE_NOMENCLATURE_PRODUCTION;
    private Date dateReference = new Date();
    private Date debut = new Date(), fin = new Date();
    private int niveau;
    private double quantite = 1;
    private boolean qteLieAuxComposant = false;    //indique que la quantité du composant est donnée ou non par la somme des composants mis en jeu.
    private boolean forConditionnement = false;    //indique si la nomenclature est pour l'operation de conditionnement ou de production.
    private boolean actif, selectActif, alwayValid, principal;
    private Articles compose = new Articles();
    private List<YvsProdComposantNomenclature> composants;
    private Conditionnement unite = new Conditionnement();
    private double margeQte;
    private List<YvsProdNomenclatureSite> sites;
    private double val_total;
    private boolean masquer;

    private Date dateSave = new Date();

    public Nomenclature() {
        composants = new ArrayList<>();
        sites = new ArrayList<>();
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public String getUnitePreference() {
        return unitePreference != null ? (unitePreference.trim().length() > 0 ? unitePreference : Constantes.UNITE_QUANTITE) : Constantes.UNITE_QUANTITE;
    }

    public void setUnitePreference(String unitePreference) {
        this.unitePreference = unitePreference;
    }

    public boolean isAlwayValid() {
        return alwayValid;
    }

    public void setAlwayValid(boolean alwayValid) {
        this.alwayValid = alwayValid;
    }

    public boolean isForConditionnement() {
        return forConditionnement;
    }

    public void setForConditionnement(boolean forConditionnement) {
        this.forConditionnement = forConditionnement;
    }

    public Date getDateReference() {
        return dateReference;
    }

    public void setDateReference(Date dateReference) {
        this.dateReference = dateReference;
    }

    public Articles getCompose() {
        return compose;
    }

    public void setCompose(Articles compose) {
        this.compose = compose;
    }

    public List<YvsProdComposantNomenclature> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsProdComposantNomenclature> composants) {
        this.composants = composants;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference != null ? reference : "";
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDebut() {
        return debut != null ? debut : new Date();
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin != null ? fin : new Date();
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Conditionnement getUnite() {
        return unite;
    }

    public void setUnite(Conditionnement unite) {
        this.unite = unite;
    }

    public boolean isQteLieAuxComposant() {
        return qteLieAuxComposant;
    }

    public void setQteLieAuxComposant(boolean qteLieAuxComposant) {
        this.qteLieAuxComposant = qteLieAuxComposant;
    }

    public double getMargeQte() {
        return margeQte;
    }

    public void setMargeQte(double margeQte) {
        this.margeQte = margeQte;
    }

    public List<YvsProdNomenclatureSite> getSites() {
        return sites;
    }

    public void setSites(List<YvsProdNomenclatureSite> sites) {
        this.sites = sites;
    }

    public boolean isMasquer() {
        return masquer;
    }

    public void setMasquer(boolean masquer) {
        this.masquer = masquer;
    }

    public String getTypeNomenclature() {
        return typeNomenclature;
    }

    public void setTypeNomenclature(String typeNomenclature) {
        this.typeNomenclature = typeNomenclature;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Nomenclature other = (Nomenclature) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public double getVal_total() {
        return val_total;
    }

    public void setVal_total(double val_total) {
        this.val_total = val_total;
    }

}
