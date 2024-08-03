/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.presence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.personnel.YvsGrhEmployes;

/**
 *
 * @author LYMYTZ-PC représente le récapitulatif d'une journée de pointage
 */
@ManagedBean
@SessionScoped
public class PresenceEmploye implements Serializable {

    private long id;
    private YvsGrhEmployes employe = new YvsGrhEmployes();
    private double totalPresence, totalAbsence, tauxJournee, totalHs, totalHc, totalRetard;
    private Date margeApprouve = new Date(-60 * 60 * 1000);
    private List<PointageEmploye> pointages;
    private boolean selectedActif;
    private String tpresence, tabsence, ths, thc, tretards;
    private boolean valider, supplementaire;
    private TypeValidation typeValidation = new TypeValidation();
    private Date debut = new Date(), fin = new Date(), finPrevu = new Date();
    private Date pause;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private boolean requisit;   //indique si un mployé est absent pour missions, formations ou congé...
    private boolean requisit2;   //indique si un mployé est absent pour une permission longue durée un congé ou une mission
    private String motifAbsence;
    private Date heureDebut, heureFin, heureFinPrevu;
    private String auteur;

    public PresenceEmploye() {
        pointages = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFinPrevu() {
        return finPrevu != null ? finPrevu : new Date();
    }

    public void setFinPrevu(Date finPrevu) {
        this.finPrevu = finPrevu;
    }

    public Date getHeureFinPrevu() {
        return heureFinPrevu != null ? heureFinPrevu : new Date();
    }

    public void setHeureFinPrevu(Date heureFinPrevu) {
        this.heureFinPrevu = heureFinPrevu;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public boolean isSupplementaire() {
        return supplementaire;
    }

    public void setSupplementaire(boolean supplementaire) {
        this.supplementaire = supplementaire;
    }

    public TypeValidation getTypeValidation() {
        return typeValidation;
    }

    public void setTypeValidation(TypeValidation typeValidation) {
        this.typeValidation = typeValidation;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public double getTotalHc() {
        return totalHc;
    }

    public void setTotalHc(double totalHc) {
        this.totalHc = totalHc;
    }

    public double getTotalHs() {
        return totalHs;
    }

    public void setTotalHs(double totalHs) {
        this.totalHs = totalHs;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public double getTotalPresence() {
        return totalPresence;
    }

    public void setTotalPresence(double totalPresence) {
        this.totalPresence = totalPresence;
    }

    public double getTotalAbsence() {
        return totalAbsence;
    }

    public void setTotalAbsence(double totalAbsence) {
        this.totalAbsence = totalAbsence;
    }

    public double getTauxJournee() {
        return tauxJournee;
    }

    public void setTauxJournee(double tauxJournee) {
        this.tauxJournee = tauxJournee;
    }

    public List<PointageEmploye> getPointages() {
        return pointages;
    }

    public void setPointages(List<PointageEmploye> pointages) {
        this.pointages = pointages;
    }

    public boolean isSelectedActif() {
        return selectedActif;
    }

    public void setSelectedActif(boolean selectedActif) {
        this.selectedActif = selectedActif;
    }

    public String getTabsence() {
        return tabsence;
    }

    public void setTabsence(String tabsence) {
        this.tabsence = tabsence;
    }

    public String getTpresence() {
        return tpresence;
    }

    public void setTpresence(String tpresence) {
        this.tpresence = tpresence;
    }

    public Date getMargeApprouve() {
        return margeApprouve;
    }

    public void setMargeApprouve(Date margeApprouve) {
        this.margeApprouve = margeApprouve;
    }

    public boolean isValider() {
        return valider;
    }

    public void setValider(boolean valider) {
        this.valider = valider;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public Date getFin() {
        return fin;
    }

    public Date getPause() {
        return pause;
    }

    public void setPause(Date pause) {
        this.pause = pause;
    }

    public String getThc() {
        return thc;
    }

    public void setThc(String thc) {
        this.thc = thc;
    }

    public String getThs() {
        return ths;
    }

    public void setThs(String ths) {
        this.ths = ths;
    }

    public String getMotifAbsence() {
        return motifAbsence;
    }

    public void setMotifAbsence(String motifAbsence) {
        this.motifAbsence = motifAbsence;
    }

    public boolean isRequisit() {
        return requisit;
    }

    public void setRequisit(boolean requisit) {
        this.requisit = requisit;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public boolean isRequisit2() {
        return requisit2;
    }

    public void setRequisit2(boolean requisit2) {
        this.requisit2 = requisit2;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public double getTotalRetard() {
        return totalRetard;
    }

    public void setTotalRetard(double totalRetard) {
        this.totalRetard = totalRetard;
    }

    public String getTretards() {
        return tretards;
    }

    public void setTretards(String tretards) {
        this.tretards = tretards;
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
        final PresenceEmploye other = (PresenceEmploye) obj;
        return this.id == other.id;
    }

}
