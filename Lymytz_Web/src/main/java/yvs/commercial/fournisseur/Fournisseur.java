/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.fournisseur;

import java.io.Serializable;import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import yvs.base.tiers.Tiers;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Comptes;
import yvs.base.compta.ModelReglement;
import yvs.base.produits.ArticleFournisseur;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean()
@SessionScoped
public class Fournisseur implements Serializable {

    private long id;
    private Tiers tiers = new Tiers();
    private String codeFsseur, nom, prenom, nom_prenom;
    private List<ArticleFournisseur> articles;
    private CategorieFournisseur categorie = new CategorieFournisseur();
    private List<PlanReglementFournisseur> plansReglements;
    private Comptes compteCollectif = new Comptes();
    private CategorieComptable categorieComptable = new CategorieComptable();
    private List<OpeCptFsseur> operations;
    private ModelReglement model = new ModelReglement();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();    
    private double solde = 0, versement = 0, reglement = 0, seuilSolde, acompte;
    private boolean selectActif, new_, actif = true, error, viewList, defaut, exclusForHome;

    public Fournisseur() {
        articles = new ArrayList<>();
        plansReglements = new ArrayList<>();
        operations = new ArrayList<>();
    }

    public Fournisseur(long id) {
        this();
        this.id = id;
    }

    public Fournisseur(long id, String codeFsseur) {
        this(id);
        this.codeFsseur = codeFsseur;
    }

    public Fournisseur(long id, Tiers tiers) {
        this(id);
        this.tiers = tiers;
        this.codeFsseur = tiers.getCodeTiers();
        this.nom = tiers.getNom();
        this.prenom = tiers.getPrenom();
        this.selectActif = tiers.isSelectActif();
        this.new_ = tiers.isNew_();
        this.error = tiers.isError();
    }

    public double getAcompte() {
        return acompte;
    }

    public void setAcompte(double acompte) {
        this.acompte = acompte;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getNom_prenom() {
        nom_prenom = "";
        if (!(getNom() == null || getNom().trim().equals(""))) {
            nom_prenom = getNom();
        }
        if (!(getPrenom() == null || getPrenom().trim().equals(""))) {
            if (nom_prenom == null || nom_prenom.trim().equals("")) {
                nom_prenom = getPrenom();
            } else {
                nom_prenom += " " + getPrenom();
            }
        }
        if (getTiers() != null) {
            if (getTiers().isSociete()) {
                if (!(getTiers().getResponsable() == null || getTiers().getResponsable().trim().equals(""))) {
                    if (nom_prenom == null || nom_prenom.trim().equals("")) {
                        nom_prenom = getTiers().getResponsable();
                    } else {
                        nom_prenom += " / " + getTiers().getResponsable();
                    }
                }
            }
        }
        return nom_prenom;
    }

    public double getSeuilSolde() {
        return seuilSolde;
    }

    public void setSeuilSolde(double seuilSolde) {
        this.seuilSolde = seuilSolde;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public boolean isExclusForHome() {
        return exclusForHome;
    }

    public void setExclusForHome(boolean exclusForHome) {
        this.exclusForHome = exclusForHome;
    }

    public void setNom_prenom(String nom_prenom) {
        this.nom_prenom = nom_prenom;
    }

    public boolean isViewList() {
        return viewList;
    }

    public void setViewList(boolean viewList) {
        this.viewList = viewList;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public ModelReglement getModel() {
        return model;
    }

    public void setModel(ModelReglement model) {
        this.model = model;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Comptes getCompteCollectif() {
        return compteCollectif;
    }

    public void setCompteCollectif(Comptes compteCollectif) {
        this.compteCollectif = compteCollectif;
    }

    public String getCodeFsseur() {
        return codeFsseur;
    }

    public void setCodeFsseur(String codeFsseur) {
        this.codeFsseur = codeFsseur;
    }

    public CategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(CategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public double getVersement() {
        return versement;
    }

    public void setVersement(double versement) {
        this.versement = versement;
    }

    public double getReglement() {
        return reglement;
    }

    public void setReglement(double reglement) {
        this.reglement = reglement;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public List<OpeCptFsseur> getOperations() {
        return operations;
    }

    public void setOperations(List<OpeCptFsseur> operations) {
        this.operations = operations;
    }

    public List<PlanReglementFournisseur> getPlansReglements() {
        return plansReglements;
    }

    public void setPlansReglements(List<PlanReglementFournisseur> plansReglements) {
        this.plansReglements = plansReglements;
    }

    public CategorieFournisseur getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieFournisseur categorie) {
        this.categorie = categorie;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    public List<ArticleFournisseur> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleFournisseur> articles) {
        this.articles = articles;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Fournisseur other = (Fournisseur) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
