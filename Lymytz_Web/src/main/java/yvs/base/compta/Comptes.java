/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Comptes implements Serializable {

    private long id;
    private String numCompte;
    private String intitule;
    private String abbreviation;
    private boolean actif = true;
    private boolean saisieAnalytique;
    private boolean saisieCompteTiers;
    private boolean saisieEcheance;
    private boolean lettrable;
    private boolean venteOnline;
    private String typeCompte; // Auxilliaire, collectif
    private String typeRepport;    //details, solde, aucun    
    private List<Comptes> fils;
    private Comptes compteGeneral;
    private NatureCompte nature = new NatureCompte();
    private boolean error;
    private String auteur;
    private String sensCompte;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();

    /**/
    private String codeComptable;
    private long idCompteCaisse;

    public Comptes() {
        fils = new ArrayList<>();
    }

    public Comptes(long id) {
        this();
        this.id = id;
    }

    public Comptes(long id, String numCompte, String intitule) {
        this(id);
        this.numCompte = numCompte;
        this.intitule = intitule;
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

    public boolean isVenteOnline() {
        return venteOnline;
    }

    public void setVenteOnline(boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getSensCompte() {
        return sensCompte;
    }

    public void setSensCompte(String sensCompte) {
        this.sensCompte = sensCompte;
    }

    public String getTypeCompte() {
//        typeCompte = getIntitule() + "[" + getNumCompte() + "]";
        return typeCompte;
    }

    public void setTypeCompte(String typeCompte) {
        this.typeCompte = typeCompte;
    }

    public String getTypeRepport() {
//        typeRepport = getNumCompte() + "[" + getIntitule() + "]";
        return typeRepport;
    }

    public void setTypeRepport(String typeRepport) {
        this.typeRepport = typeRepport;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(String numCompte) {
        this.numCompte = numCompte;
    }

    public boolean isSaisieAnalytique() {
        return saisieAnalytique;
    }

    public void setSaisieAnalytique(boolean saisieAnalytique) {
        this.saisieAnalytique = saisieAnalytique;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isSaisieCompteTiers() {
        return saisieCompteTiers;
    }

    public void setSaisieCompteTiers(boolean saisieCompteTiers) {
        this.saisieCompteTiers = saisieCompteTiers;
    }

    public boolean isLettrable() {
        return lettrable;
    }

    public void setLettrable(boolean lettrable) {
        this.lettrable = lettrable;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public boolean isSaisieEcheance() {
        return saisieEcheance;
    }

    public void setSaisieEcheance(boolean saisieEcheance) {
        this.saisieEcheance = saisieEcheance;
    }

    public List<Comptes> getFils() {
        return fils;
    }

    public void setFils(List<Comptes> fils) {
        this.fils = fils;
    }

    public Comptes getCompteGeneral() {
        return compteGeneral;
    }

    public void setCompteGeneral(Comptes compteGeneral) {
        this.compteGeneral = compteGeneral;
    }

    public NatureCompte getNature() {
        return nature;
    }

    public void setNature(NatureCompte nature) {
        this.nature = nature;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getCodeComptable() {
        return codeComptable;
    }

    public void setCodeComptable(String codeComptable) {
        this.codeComptable = codeComptable;
    }

    public long getIdCompteCaisse() {
        return idCompteCaisse;
    }

    public void setIdCompteCaisse(long idCompteCaisse) {
        this.idCompteCaisse = idCompteCaisse;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Comptes other = (Comptes) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
