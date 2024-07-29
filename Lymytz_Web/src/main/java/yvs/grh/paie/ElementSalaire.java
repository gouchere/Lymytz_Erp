/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Comptes;
import yvs.grh.bean.OptionEltSalaire;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class ElementSalaire implements Serializable, Comparator<ElementSalaire> {

    private long id;
    private String nom, code, description;
    private String newCode;
    private CategorieRegleSalaire categorie = new CategorieRegleSalaire();
    /**
     * **
     */
    private int typeMontant; //expression Système (0), montant fixe (1), pourcentage (2), expression Algébrique (3) quantité (4)
    private OptionEltSalaire basePourcentage = new OptionEltSalaire(); //code de la règle sur laquelle sera appliqué le pourcentage
    private OptionEltSalaire expQuantite = new OptionEltSalaire(); //code de la règle qui constitue la quantité
    private double montant; // si type montant est pourcentage ou montant
    private String expressionRegle; //si type montant est exxpression algébrique
    private boolean sup = false, actif = true, visibleSurBulletin = true;
    private boolean visibleOnLivrePaie;
    private boolean retenue;
    private double tauxPatronale, tauxSalarial;

    private boolean attribuer;  //permet de distinguer sur la vue les eléments déjà attribuer à une structure
//    private boolean selectActif;
    private RubriqueBulletin rubrique = new RubriqueBulletin();
    private int numSequence;    //nécessaire pour les éléments visible sur le bulletin: sert à indiquer la position (ordre)
    private Date dateSave = new Date();
    private boolean regleConge, excludeInConge;
    private boolean regleArondi;
    private Comptes compteCharge = new Comptes();
    private Comptes compteCotisation = new Comptes();
    private boolean saisiCompteTiers;

    public ElementSalaire() {

    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

    public ElementSalaire(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CategorieRegleSalaire getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieRegleSalaire categorie) {
        this.categorie = categorie;
    }

    public String getExpressionRegle() {
        return expressionRegle;
    }

    public void setExpressionRegle(String expressionRegle) {
        this.expressionRegle = expressionRegle;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isVisibleSurBulletin() {
        return visibleSurBulletin;
    }

    public void setVisibleSurBulletin(boolean visibleSurBulletin) {
        this.visibleSurBulletin = visibleSurBulletin;
    }

    public int getTypeMontant() {
        return typeMontant;
    }

    public void setTypeMontant(int typeMontant) {
        this.typeMontant = typeMontant;
    }

    public OptionEltSalaire getBasePourcentage() {
        return basePourcentage;
    }

    public void setBasePourcentage(OptionEltSalaire basePourcentage) {
        this.basePourcentage = basePourcentage;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public OptionEltSalaire getExpQuantite() {
        return expQuantite;
    }

    public void setExpQuantite(OptionEltSalaire expQuantite) {
        this.expQuantite = expQuantite;
    }

    public double getTauxPatronale() {
        return tauxPatronale;
    }

    public void setTauxPatronale(double tauxPatronale) {
        this.tauxPatronale = tauxPatronale;
    }

    public double getTauxSalarial() {
        return tauxSalarial;
    }

    public void setTauxSalarial(double tauxSalarial) {
        this.tauxSalarial = tauxSalarial;
    }

    public boolean isSup() {
        return sup;
    }

    public void setSup(boolean sup) {
        this.sup = sup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumSequence() {
        return numSequence;
    }

    public void setNumSequence(int numSequence) {
        this.numSequence = numSequence;
    }

    public boolean isExcludeInConge() {
        return excludeInConge;
    }

    public void setExcludeInConge(boolean excludeInConge) {
        this.excludeInConge = excludeInConge;
    }

    public boolean isVisibleOnLivrePaie() {
        return visibleOnLivrePaie;
    }

    public void setVisibleOnLivrePaie(boolean visibleOnLivrePaie) {
        this.visibleOnLivrePaie = visibleOnLivrePaie;
    }

    public Comptes getCompteCharge() {
        return compteCharge;
    }

    public void setCompteCharge(Comptes compteCharge) {
        this.compteCharge = compteCharge;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

//    public boolean isSelectActif() {
//        return selectActif;
//    }
//
//    public void setSelectActif(boolean selectActif) {
//        this.selectActif = selectActif;
//    }
    public boolean isRetenue() {
        return retenue;
    }

    public void setRetenue(boolean retenue) {
        this.retenue = retenue;
    }

    public boolean isAttribuer() {
        return attribuer;
    }

    public void setAttribuer(boolean attribuer) {
        this.attribuer = attribuer;
    }

    public RubriqueBulletin getRubrique() {
        return rubrique;
    }

    public void setRubrique(RubriqueBulletin rubrique) {
        this.rubrique = rubrique;
    }

    public boolean isRegleConge() {
        return regleConge;
    }

    public void setRegleConge(boolean regleConge) {
        this.regleConge = regleConge;
    }

    public boolean isRegleArondi() {
        return regleArondi;
    }

    public void setRegleArondi(boolean regleArondi) {
        this.regleArondi = regleArondi;
    }

    public Comptes getCompteCotisation() {
        return compteCotisation;
    }

    public void setCompteCotisation(Comptes compteCotisation) {
        this.compteCotisation = compteCotisation;
    }

    public boolean isSaisiCompteTiers() {
        return saisiCompteTiers;
    }

    public void setSaisiCompteTiers(boolean saisiCompteTiers) {
        this.saisiCompteTiers = saisiCompteTiers;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ElementSalaire other = (ElementSalaire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compare(ElementSalaire o1, ElementSalaire o2) {
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (o1.getCode().compareTo(o2.getCode()) > 0) {
            return 1;
        } else if (o1.getCode().compareTo(o2.getCode()) < 0) {
            return -1;
        } else {
            return 0;
        }
    }

}
