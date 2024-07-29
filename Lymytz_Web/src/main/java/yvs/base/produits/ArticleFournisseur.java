/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
public class ArticleFournisseur implements Serializable {

    private long id;
    private double delaiLivraison;
    private double dureeGarantie, dureeVie;
    private Fournisseur fournisseur = new Fournisseur();
    private Articles article = new Articles();
    private boolean selectActif, principal, puaTtc;
    private double prix, remise;
    private String referenceArticle;
    private String designation;
    private String uniteDelaisLiv;
    private String uniteDureeGaranti;
    private String uniteDureeVie;
    private String refArtExterne;
    private String desArtExterne;
    private Date dateLivraison = new Date();
    private Date dateSave = new Date();
    private String natureRemise = Constantes.NATURE_MTANT;
    private Conditionnement conditionnement = new Conditionnement();

    public ArticleFournisseur() {
    }

    public ArticleFournisseur(Integer id) {
        this.id = id;
    }

    public boolean isPuaTtc() {
        return puaTtc;
    }

    public void setPuaTtc(boolean puaTtc) {
        this.puaTtc = puaTtc;
    }

    public String getRefArtExterne() {
        return refArtExterne;
    }

    public void setRefArtExterne(String refArtExterne) {
        this.refArtExterne = refArtExterne;
    }

    public String getDesArtExterne() {
        return desArtExterne;
    }

    public void setDesArtExterne(String desArtExterne) {
        this.desArtExterne = desArtExterne;
    }

    public String getNatureRemise() {
        return natureRemise != null ? (natureRemise.trim().length() > 0 ? natureRemise : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureRemise(String natureRemise) {
        this.natureRemise = natureRemise;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public double getDureeVie() {
        return dureeVie;
    }

    public void setDureeVie(double dureeVie) {
        this.dureeVie = dureeVie;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getDelaiLivraison() {
        return delaiLivraison;
    }

    public void setDelaiLivraison(double delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public double getDureeGarantie() {
        return dureeGarantie;
    }

    public void setDureeGarantie(double dureeGarantie) {
        this.dureeGarantie = dureeGarantie;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public String getUniteDelaisLiv() {
        return uniteDelaisLiv;
    }

    public void setUniteDelaisLiv(String uniteDelaisLiv) {
        this.uniteDelaisLiv = uniteDelaisLiv;
    }

    public String getUniteDureeGaranti() {
        return uniteDureeGaranti;
    }

    public void setUniteDureeGaranti(String uniteDureeGaranti) {
        this.uniteDureeGaranti = uniteDureeGaranti;
    }

    public String getUniteDureeVie() {
        return uniteDureeVie;
    }

    public void setUniteDureeVie(String uniteDureeVie) {
        this.uniteDureeVie = uniteDureeVie;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getReferenceArticle() {
        return referenceArticle;
    }

    public void setReferenceArticle(String referenceArticle) {
        this.referenceArticle = referenceArticle;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
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
        final ArticleFournisseur other = (ArticleFournisseur) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
