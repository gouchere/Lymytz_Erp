/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.recrutement.entretien;

import java.io.Serializable;import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.parametrage.poste.PosteDeTravail;
import yvs.grh.recrutement.SpecialitesDiplomeCandidat;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class Candidats implements Serializable {

    private String code;
    private int anciennete; //ancienneté en mois
    private long id;
    private String civilite;
    private PosteDeTravail poste = new PosteDeTravail();
    private String nom, prenom;
    private String codePostal;
    private String telephone, email;
    private String apropos;
    private int age;
    private Date dateNaiss = new Date();
    private Dictionnaire lieuNaissance = new Dictionnaire(), ville = new Dictionnaire();//ville d'habitation
    private Dictionnaire paysOrigine = new Dictionnaire();
    private List<String> telephones;
    private List<String> emails;
    private List<String> loisirs;
    private List<SpecialitesDiplomeCandidat> formations;
    private List<DomainesQualificationCandidat> competences;
    private List<LangueCandidat> langues;
    private boolean actif = true;

    public Candidats() {
        telephones = new ArrayList<>();
        emails = new ArrayList<>();
        formations = new ArrayList<>();
        loisirs = new ArrayList<>();
        langues = new ArrayList<>();
        competences = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAnciennete() {
        return anciennete;
    }

    public void setAnciennete(int anciennete) {
        this.anciennete = anciennete;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public PosteDeTravail getPoste() {
        return poste;
    }

    public void setPoste(PosteDeTravail poste) {
        this.poste = poste;
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

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApropos() {
        return apropos;
    }

    public void setApropos(String apropos) {
        this.apropos = apropos;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(Date dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public Dictionnaire getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(Dictionnaire lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public Dictionnaire getVille() {
        return ville;
    }

    public void setVille(Dictionnaire ville) {
        this.ville = ville;
    }

    public Dictionnaire getPaysOrigine() {
        return paysOrigine;
    }

    public void setPaysOrigine(Dictionnaire paysOrigine) {
        this.paysOrigine = paysOrigine;
    }

    public List<LangueCandidat> getLangues() {
        return langues;
    }

    public void setLangues(List<LangueCandidat> langues) {
        this.langues = langues;
    }

    public List<String> getTelephones() {
        return telephones;
    }

    public void setTelephones(List<String> telephones) {
        this.telephones = telephones;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getLoisirs() {
        return loisirs;
    }

    public void setLoisirs(List<String> loisirs) {
        this.loisirs = loisirs;
    }

    public List<SpecialitesDiplomeCandidat> getFormations() {
        return formations;
    }

    public void setFormations(List<SpecialitesDiplomeCandidat> formations) {
        this.formations = formations;
    }

    public List<DomainesQualificationCandidat> getCompetences() {
        return competences;
    }

    public void setCompetences(List<DomainesQualificationCandidat> competences) {
        this.competences = competences;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Candidats other = (Candidats) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @PostConstruct
    public void init() {
    }
}
