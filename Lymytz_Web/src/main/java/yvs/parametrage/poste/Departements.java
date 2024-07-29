/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.poste;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.Departement;
import yvs.entity.compta.YvsComptaAffecAnalDepartement;
import yvs.grh.bean.EmployePartial;
import yvs.parametrage.agence.Agence;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Departements implements Serializable {

    private int id;
    private String intitule, nomResponsable;
    private String description, cheminParent, codeDepartement;
    private int idParent, nivau;
    private List<PosteDeTravail> listPosteTravail;
    private double etatEndettement = 0;
    private double masseSalariale = 0;
    private Integer totalEmploye = 0;
    private EmployePartial responsable = new EmployePartial();
    private boolean actif;
    private boolean visibleOnLp;
    private String abreviation;
    private Agence agence = new Agence();
    private yvs.base.Departement service = new yvs.base.Departement();

    private Date dateSave = new Date();

    private List<YvsComptaAffecAnalDepartement> sectionsAnalytiques;

    public Departements() {
        listPosteTravail = new ArrayList<>();
        sectionsAnalytiques = new ArrayList<>();
    }

    public Departements(int id, String intitule) {
        this();
        this.id = id;
        this.intitule = intitule;
    }

    public Departements(int id, String intitule, String abreviation, String description) {
        this(id, intitule);
        this.abreviation = abreviation;
        this.description = description;
    }

    public Departements(int id, String intitule, String description) {
        this(id, intitule);
        this.id = id;
        this.intitule = intitule;
        this.description = description;
    }

    public List<YvsComptaAffecAnalDepartement> getSectionsAnalytiques() {
        return sectionsAnalytiques;
    }

    public void setSectionsAnalytiques(List<YvsComptaAffecAnalDepartement> sectionsAnalytiques) {
        this.sectionsAnalytiques = sectionsAnalytiques;
    }

    
    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public Integer getTotalEmploye() {
        return totalEmploye;
    }

    public void setTotalEmploye(Integer totalEmploye) {
        this.totalEmploye = totalEmploye;
    }

    public double getEtatEndettement() {
        return etatEndettement;
    }

    public void setEtatEndettement(double etatEndettement) {
        this.etatEndettement = etatEndettement;
    }

    public double getMasseSalariale() {
        return masseSalariale;
    }

    public void setMasseSalariale(double masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public List<PosteDeTravail> getListPosteTravail() {
        return listPosteTravail;
    }

    public void setListPosteTravail(List<PosteDeTravail> listPosteTravail) {
        this.listPosteTravail = listPosteTravail;
    }

    public String getCheminParent() {
        return cheminParent;
    }

    public void setCheminParent(String cheminParent) {
        this.cheminParent = cheminParent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public int getNivau() {
        return nivau;
    }

    public void setNivau(int nivau) {
        this.nivau = nivau;
    }

    public EmployePartial getResponsable() {
        return responsable;
    }

    public void setResponsable(EmployePartial responsable) {
        this.responsable = responsable;
    }

    public boolean isActif() {
        return actif;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public boolean isVisibleOnLp() {
        return visibleOnLp;
    }

    public void setVisibleOnLp(boolean visibleOnLp) {
        this.visibleOnLp = visibleOnLp;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Departement getService() {
        return service;
    }

    public void setService(Departement service) {
        this.service = service;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + this.id;
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
        final Departements other = (Departements) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return intitule;
    }

}
