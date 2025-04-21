/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.tiers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.PersonneEnCharge;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "emps")
@SessionScoped
public class Employe extends Tiers implements Serializable {

    private String matricule;
    private String prenom;
    private String secuSocial;
    private Date dateEmbauche = new Date();
    private Date dateDepart = new Date();
    private Date dateNaiss = new Date();
    private String etatCivil;
    private short nbeEnfant;
    private double salaireMensuelBrut;
    private double salaireHoraire;
    private String modeDePaiement;
    private String fonction;
    private String departement;
    private String categorie, comentaire, echelon;
    private String motifArret, motifEntree;
    private String grade; // Cadre moyen, cades sup, cadre special, Junior, Senior
    private char sexe;
    private boolean sexeM, sexeF;
    private String secteur;
    /**
     * Modules souscrit
     */
    private boolean heureSup, conges;
    private boolean absence, listEmploye;
    private boolean noteDeFrais;
    private List<ContactTel> listTels;
    private List<ContactTel> listEmail;
    private List<ContactTel> listAdresse;
    private List<PersonneEnCharge> listenfant;

    public Employe() {
        setPhotos("user1.jpg");
        listTels = new ArrayList<>();
        listenfant = new ArrayList<>();
        listEmail = new ArrayList<>();
        listAdresse = new ArrayList<>();
    }

    public Employe(long id, Tiers y) {
        super(id, y.getNom(), y.getPrenom());
        setPhotos("user1.jpg");
        listTels = new ArrayList<>();
        listenfant = new ArrayList<>();
        listEmail = new ArrayList<>();
        listAdresse = new ArrayList<>();
    }

    public boolean isListEmploye() {
        return listEmploye;
    }

    public void setListEmploye(boolean listEmploye) {
        this.listEmploye = listEmploye;
    }

    public List<ContactTel> getListAdresse() {
        return listAdresse;
    }

    public void setListAdresse(List<ContactTel> listAdresse) {
        this.listAdresse = listAdresse;
    }

    public List<ContactTel> getListEmail() {
        return listEmail;
    }

    public void setListEmail(List<ContactTel> listEmail) {
        this.listEmail = listEmail;
    }

    public String getComentaire() {
        return comentaire;
    }

    public void setComentaire(String comentaire) {
        this.comentaire = comentaire;
    }

    public List<ContactTel> getListTels() {
        return listTels;
    }

    public void setListTels(List<ContactTel> listTels) {
        this.listTels = listTels;
    }

    public boolean isAbsence() {
        return absence;
    }

    public void setAbsence(boolean absence) {
        this.absence = absence;
    }

    public boolean isConges() {
        return conges;
    }

    public void setConges(boolean conges) {
        this.conges = conges;
    }

    public boolean isHeureSup() {
        return heureSup;
    }

    public void setHeureSup(boolean heureSup) {
        this.heureSup = heureSup;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public boolean isNoteDeFrais() {
        return noteDeFrais;
    }

    public void setNoteDeFrais(boolean noteDeFrais) {
        this.noteDeFrais = noteDeFrais;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Date getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public String getModeDePaiement() {
        return modeDePaiement;
    }

    public void setModeDePaiement(String modeDePaiement) {
        this.modeDePaiement = modeDePaiement;
    }

    public short getNbeEnfant() {
        return nbeEnfant;
    }

    public void setNbeEnfant(short nbeEnfant) {
        this.nbeEnfant = nbeEnfant;
    }

    public double getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(double salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public double getSalaireMensuelBrut() {
        return salaireMensuelBrut;
    }

    public void setSalaireMensuelBrut(double salaireMensuelBrut) {
        this.salaireMensuelBrut = salaireMensuelBrut;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getEtatCivil() {
        return etatCivil;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getMotifArret() {
        return motifArret;
    }

    public void setMotifArret(String motifArret) {
        this.motifArret = motifArret;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSecuSocial() {
        return secuSocial;
    }

    public void setSecuSocial(String secuSocial) {
        this.secuSocial = secuSocial;
    }

    public List<PersonneEnCharge> getListenfant() {
        return listenfant;
    }

    public void setListenfant(List<PersonneEnCharge> listenfant) {
        this.listenfant = listenfant;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Date getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(Date dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public char getSexe() {
        return sexe;
    }

    public void setSexe(char sexe) {
        this.sexe = sexe;
    }

    public boolean isSexeF() {
        return sexeF;
    }

    public void setSexeF(boolean sexeF) {
        this.sexeF = sexeF;
    }

    public boolean isSexeM() {
        return sexeM;
    }

    public void setSexeM(boolean sexeM) {
        this.sexeM = sexeM;
    }

    public String getMotifEntree() {
        return motifEntree;
    }

    public void setMotifEntree(String motifEntree) {
        this.motifEntree = motifEntree;
    }

    public String getEchelon() {
        return echelon;
    }

    public void setEchelon(String echelon) {
        this.echelon = echelon;
    }

    /**
     *
     * @param secteur
     * @return
     */
//    @Override
//    public String getSecteur() {
//        return secteur;
//    }
    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

}
