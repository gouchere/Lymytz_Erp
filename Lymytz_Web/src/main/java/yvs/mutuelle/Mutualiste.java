/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutPosteEmploye;
import yvs.entity.mutuelle.credit.YvsMutTypeCredit;
import yvs.grh.bean.Employe;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Mutualiste implements Serializable {

    private long id;
    private String matricule;
    private Date dateAdhesion = new Date();
    private Date dateSave = new Date();
    private List<YvsMutCompte> comptes;
    private Employe employe = new Employe();
    private List<YvsMutPosteEmploye> postes;
    private PosteEmploye posteEmploye = new PosteEmploye();
    private Mutuelle mutuelle = new Mutuelle();
    private double montantSalaire = 0, montantPaie = 0, montantEpargne = 0, montantRetrait = 0,
            montantResteEpargne = 0, montantCredit = 0, montantTotalEpargne = 0;
    private double soldeAssurance = 0;
    private boolean selectActif, actif;
    private double couvertureAvalise, currentCouvertureAvalise;   //Garde le montant des avalise du mutualiste
    private boolean assistance = true;
    private boolean error, list;
    
    private List<YvsMutPeriodeExercice> encours, retards;
    private List<YvsMutTypeCredit> credits;
    private List<Object[]> montants;

    public Mutualiste() {
        postes = new ArrayList<>();
        comptes = new ArrayList<>();
        encours = new ArrayList<>();
        retards = new ArrayList<>();
        montants = new ArrayList<>();
        credits = new ArrayList<>();
    }

    public Mutualiste(long id) {
        this();
        this.id = id;
    }

    public Mutualiste(String matricule) {
        this();
        this.matricule = matricule;
    }

    public Mutualiste(String matricule, boolean error) {
        this(matricule);
        this.error = error;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public double getCurrentCouvertureAvalise() {
        return currentCouvertureAvalise;
    }

    public void setCurrentCouvertureAvalise(double currentCouvertureAvalise) {
        this.currentCouvertureAvalise = currentCouvertureAvalise;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public boolean isAssistance() {
        return assistance;
    }

    public void setAssistance(boolean assistance) {
        this.assistance = assistance;
    }

    public double getSoldeAssurance() {
        return soldeAssurance;
    }

    public void setSoldeAssurance(double soldeAssurance) {
        this.soldeAssurance = soldeAssurance;
    }

    public double getMontantTotalEpargne() {
        return montantTotalEpargne;
    }

    public void setMontantTotalEpargne(double montantTotalEpargne) {
        this.montantTotalEpargne = montantTotalEpargne;
    }

    public double getMontantCredit() {
        return montantCredit;
    }

    public void setMontantCredit(double montantCredit) {
        this.montantCredit = montantCredit;
    }

    public double getMontantPaie() {
        return montantPaie;
    }

    public void setMontantPaie(double montantPaie) {
        this.montantPaie = montantPaie;
    }

    public double getMontantRetrait() {
        return montantRetrait;
    }

    public void setMontantRetrait(double montantRetrait) {
        this.montantRetrait = montantRetrait;
    }

    public double getMontantResteEpargne() {
        return montantResteEpargne;
    }

    public void setMontantResteEpargne(double montantResteEpargne) {
        this.montantResteEpargne = montantResteEpargne;
    }

    public void setMontantEpargne(double montantEpargne) {
        this.montantEpargne = montantEpargne;
    }

    public double getMontantEpargne() {
        return montantEpargne;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public double getMontantSalaire() {
        return montantSalaire;
    }

    public void setMontantSalaire(double montantSalaire) {
        this.montantSalaire = montantSalaire;
    }

    public PosteEmploye getPosteEmploye() {
        return posteEmploye;
    }

    public void setPosteEmploye(PosteEmploye posteEmploye) {
        this.posteEmploye = posteEmploye;
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

    public Date getDateAdhesion() {
        return dateAdhesion;
    }

    public void setDateAdhesion(Date dateAdhesion) {
        this.dateAdhesion = dateAdhesion;
    }

    public List<YvsMutCompte> getComptes() {
        return comptes;
    }

    public void setComptes(List<YvsMutCompte> comptes) {
        this.comptes = comptes;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public List<YvsMutPosteEmploye> getPostes() {
        return postes;
    }

    public void setPostes(List<YvsMutPosteEmploye> postes) {
        this.postes = postes;
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public double getCouvertureAvalise() {
        return couvertureAvalise;
    }

    public void setCouvertureAvalise(double couvertureAvalise) {
        this.couvertureAvalise = couvertureAvalise;
    }

    public List<YvsMutPeriodeExercice> getEncours() {
        return encours;
    }

    public void setEncours(List<YvsMutPeriodeExercice> encours) {
        this.encours = encours;
    }

    public List<YvsMutPeriodeExercice> getRetards() {
        return retards;
    }

    public void setRetards(List<YvsMutPeriodeExercice> retards) {
        this.retards = retards;
    }

    public List<Object[]> getMontants() {
        return montants;
    }

    public void setMontants(List<Object[]> montants) {
        this.montants = montants;
    }

    public List<YvsMutTypeCredit> getCredits() {
        return credits;
    }

    public void setCredits(List<YvsMutTypeCredit> credits) {
        this.credits = credits;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final Mutualiste other = (Mutualiste) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
