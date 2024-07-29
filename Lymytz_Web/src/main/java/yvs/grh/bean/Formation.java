/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.param.workflow.YvsWorkflowValidFormation;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Formation implements Serializable {

    private long id;
    private String titreFormation;
    private Formateur formateur = new Formateur();
    private int dureeFormation;
    private Date dateFormation, dateDebut = new Date(), dateFin = new Date();
    private String niveau;
    private Diplomes diplome = new Diplomes();
    private List<FormationEmps> employes;
    private String reference, description;
    private double cout;
    private List<DomainesQualifications> qualifications;
    private Dictionnaire lieuParDefaut = new Dictionnaire();
    private boolean selectActif;
    private char statutFormation = 'E';
    private List<YvsWorkflowValidFormation> etapesValidations;

    public Formation() {
        employes = new ArrayList<>();
        qualifications = new ArrayList<>();
    }

    public Formation(long id) {
        this.id = id;
        employes = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitreFormation() {
        return titreFormation;
    }

    public Formateur getFormateur() {
        return formateur;
    }

    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }

    public void setTitreFormation(String titreFormation) {
        this.titreFormation = titreFormation;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Diplomes getDiplome() {
        return diplome;
    }

    public void setDiplome(Diplomes diplome) {
        this.diplome = diplome;
    }

    public List<FormationEmps> getEmployes() {
        return employes;
    }

    public void setEmployes(List<FormationEmps> employes) {
        this.employes = employes;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public List<DomainesQualifications> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<DomainesQualifications> qualifications) {
        this.qualifications = qualifications;
    }

    public int getDureeFormation() {
        return dureeFormation;
    }

    public void setDureeFormation(int dureeFormation) {
        this.dureeFormation = dureeFormation;
    }

    public Date getDateFormation() {
        return dateFormation;
    }

    public void setDateFormation(Date dateFormation) {
        this.dateFormation = dateFormation;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Dictionnaire getLieuParDefaut() {
        return lieuParDefaut;
    }

    public void setLieuParDefaut(Dictionnaire lieuParDefaut) {
        this.lieuParDefaut = lieuParDefaut;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public char getStatutFormation() {
        return statutFormation;
    }

    public void setStatutFormation(char statutFormation) {
        this.statutFormation = statutFormation;
    }

    public List<YvsWorkflowValidFormation> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidFormation> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Formation other = (Formation) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
