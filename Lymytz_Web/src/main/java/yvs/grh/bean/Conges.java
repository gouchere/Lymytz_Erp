/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultScheduleEvent;
import yvs.entity.param.workflow.YvsWorkflowValidConge;
import yvs.util.Constantes;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class Conges extends DefaultScheduleEvent implements Serializable {

    private long idConge;
    private String effet;   //A faire valoir sur le congé annuelle ou sur le salaire ou permission spéciale autorisé
    private boolean valider;
    private Employe employe = new Employe();
    private char statut = Constantes.STATUT_DOC_ATTENTE;  //T=transmi V=validé C=cloturé S=suspendre W= en attente E= en cours
    private char nature = 'C';    // P= permission    C=congé
    private Date heureDebut = new Date(), HeureFin = new Date();
    private char typeDureePermission = 'L';   //C=court L=long
    private String typeConge = "Autres";   //Anuelle, ou autres
    private String description;
    private double dureeAbsence;
    private Date dureeAbsenceEnHeure;
    private Date datePaiementAlloc;
    private Date dateRetourConge;
    private List<YvsWorkflowValidConge> etapesValidations;
    private int etapeValide;
    private Date dateFinPrevu;
    private Date dateUpdate = new Date();
    private Date dateSuspension;
    private String motifSuspension;
    private String referenceConge;
    private Date dateSave = new Date();
    private String author;
    private Date heureRetourEffectif;
    private int congePrincipalDu, congePrincipalPris;
    private int dureePermPrisSpeciale;      //durée des permissions longue durée à effet spéciale
    private double congeSupp;
    private double dureePermissionAutorisePris;    //durée de permission prise à faire valoir sur le quotas de permissions autorisé
    private double dureePermissionAutorise;
    private double dureePermCD;
    private double dureePermCD_AN;
    private double dureePermCD_SP;
    private double dureePermCD_AU;
    private double dureePermCD_SAL;
    private boolean compteJourRepos = true;

    public Conges() {
    }

    public Conges(Conges c) {
        this.idConge = c.idConge;
        this.description = c.description;
        this.effet = c.getEffet();
        this.statut = c.getStatut();
        this.valider = c.valider;
        this.setStartDate(c.getStartDate());
        this.setEndDate(c.getEndDate());
        this.setTitle(c.getTitle());
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDureeAbsenceEnHeure() {
        return dureeAbsenceEnHeure;
    }

    public void setDureeAbsenceEnHeure(Date dureeAbsenceEnHeure) {
        this.dureeAbsenceEnHeure = dureeAbsenceEnHeure;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReferenceConge() {
        return referenceConge;
    }

    public void setReferenceConge(String referenceConge) {
        this.referenceConge = referenceConge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public Date getDatePaiementAlloc() {
        return datePaiementAlloc;
    }

    public void setDatePaiementAlloc(Date datePaiementAlloc) {
        this.datePaiementAlloc = datePaiementAlloc;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return HeureFin;
    }

    public void setHeureFin(Date HeureFin) {
        this.HeureFin = HeureFin;
    }

    public char getNature() {
        return nature;
    }

    public void setNature(char nature) {
        this.nature = nature;
    }

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public long getIdConge() {
        return idConge;
    }

    public void setIdConge(long idConge) {
        this.idConge = idConge;
    }

    public double getDureeAbsence() {
        return dureeAbsence;
    }

    public void setDureeAbsence(double dureeAbsence) {
        this.dureeAbsence = dureeAbsence;
    }

    public String getEffet() {
        return effet;
    }

    public void setEffet(String effet) {
        this.effet = effet;
    }

    public boolean isValider() {
        return valider;
    }

    public void setValider(boolean valider) {
        this.valider = valider;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public char getTypeDureePermission() {
        return typeDureePermission;
    }

    public void setTypeDureePermission(char typeDureePermission) {
        this.typeDureePermission = typeDureePermission;
    }

    public String getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(String typeConge) {
        this.typeConge = typeConge;
    }

    public Date getDateRetourConge() {
        return dateRetourConge;
    }

    public void setDateRetourConge(Date dateRetourConge) {
        this.dateRetourConge = dateRetourConge;
    }

    public int getCongePrincipalDu() {
        return congePrincipalDu;
    }

    public void setCongePrincipalDu(int congePrincipalDu) {
        this.congePrincipalDu = congePrincipalDu;
    }

    public int getCongePrincipalPris() {
        return congePrincipalPris;
    }

    public void setCongePrincipalPris(int congePrincipalPris) {
        this.congePrincipalPris = congePrincipalPris;
    }

    public List<YvsWorkflowValidConge> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidConge> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public int getEtapeValide() {
        return etapeValide;
    }

    public void setEtapeValide(int etapeValide) {
        this.etapeValide = etapeValide;
    }

    public Date getDateFinPrevu() {
        return dateFinPrevu;
    }

    public void setDateFinPrevu(Date dateFinPrevu) {
        this.dateFinPrevu = dateFinPrevu;
    }

    public Date getDateSuspension() {
        return dateSuspension;
    }

    public void setDateSuspension(Date dateSuspension) {
        this.dateSuspension = dateSuspension;
    }

    public String getMotifSuspension() {
        return motifSuspension;
    }

    public void setMotifSuspension(String motifSuspension) {
        this.motifSuspension = motifSuspension;
    }

    public Date getHeureRetourEffectif() {
        return heureRetourEffectif;
    }

    public void setHeureRetourEffectif(Date heureRetourEffectif) {
        this.heureRetourEffectif = heureRetourEffectif;
    }

    public double getCongeSupp() {
        return congeSupp;
    }

    public void setCongeSupp(double congeSupp) {
        this.congeSupp = congeSupp;
    }

    public double getDureePermissionAutorisePris() {
        return dureePermissionAutorisePris;
    }

    public void setDureePermissionAutorisePris(double dureePermissionAutorisePris) {
        this.dureePermissionAutorisePris = dureePermissionAutorisePris;
    }

    public double getDureePermissionAutorise() {
        return dureePermissionAutorise;
    }

    public void setDureePermissionAutorise(double dureePermissionAutorise) {
        this.dureePermissionAutorise = dureePermissionAutorise;
    }

    public int getDureePermPrisSpeciale() {
        return dureePermPrisSpeciale;
    }

    public void setDureePermPrisSpeciale(int dureePermPrisSpeciale) {
        this.dureePermPrisSpeciale = dureePermPrisSpeciale;
    }

    public boolean isCompteJourRepos() {
        return compteJourRepos;
    }

    public void setCompteJourRepos(boolean compteJourRepos) {
        this.compteJourRepos = compteJourRepos;
    }

    public double getDureePermCD_AN() {
        return dureePermCD_AN;
    }

    public void setDureePermCD_AN(double dureePermCD_AN) {
        this.dureePermCD_AN = dureePermCD_AN;
    }

    public double getDureePermCD_SP() {
        return dureePermCD_SP;
    }

    public void setDureePermCD_SP(double dureePermCD_SP) {
        this.dureePermCD_SP = dureePermCD_SP;
    }

    public double getDureePermCD_AU() {
        return dureePermCD_AU;
    }

    public void setDureePermCD_AU(double dureePermCD_AU) {
        this.dureePermCD_AU = dureePermCD_AU;
    }

    public double getDureePermCD_SAL() {
        return dureePermCD_SAL;
    }

    public void setDureePermCD_SAL(double dureePermCD_SAL) {
        this.dureePermCD_SAL = dureePermCD_SAL;
    }

    public double getDureePermCD() {
        return dureePermCD;
    }

    public void setDureePermCD(double dureePermCD) {
        this.dureePermCD = dureePermCD;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (this.idConge ^ (this.idConge >>> 32));
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
        final Conges other = (Conges) obj;
        if (this.idConge != other.idConge) {
            return false;
        }
        return true;
    }

}
