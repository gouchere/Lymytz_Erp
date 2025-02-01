/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.credit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.base.YvsMutGrilleTauxTypeCredit;
import yvs.entity.mutuelle.credit.YvsMutFraisTypeCredit;
import yvs.mutuelle.Mutuelle;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class TypeCredit implements Serializable {

    private long id;
    private String designation;
    private String code;
    private String natureMontant = "Fixe", suffixeMontant = "Fcfa";
    private double nbreAvalise;
    private int periodicite;
    private char typeMensualite = Constantes.TYPE_MENSUALITE_AMORTISSEMENT_FIXE;
    private char formuleInteret = Constantes.FORMULE_INTERET_SIMPLE;
    private char modelRemboursement = Constantes.MODEL_REMBOURSEMENT_MODULABLE;
    private boolean reechellonagePossible;
    private boolean fusionPossible;
    private boolean byFusion;
    private boolean anticipationPossible;
    private boolean penaliteAnticipation;
    private double tauxPenaliteAnticipation;
    private char basePenaliteAnticipation = Constantes.PENALITE_BASE_MENSUALITE;
    private char naturePenaliteAnticipation = Constantes.NATURE_POURCENTAGE;
    private boolean suspensionPossible;
    private boolean penaliteSuspension;
    private double tauxPenaliteSuspension;
    private char basePenaliteSuspension = Constantes.PENALITE_BASE_MENSUALITE;
    private char naturePenaliteSuspension = Constantes.NATURE_POURCENTAGE;
    private boolean penaliteRetard;
    private double tauxPenaliteRetard;
    private char basePenaliteRetard = Constantes.PENALITE_BASE_MENSUALITE;
    private char naturePenaliteRetard = Constantes.NATURE_POURCENTAGE;
    private double montantMaximal;
    private double tauxMaximal;
    private double periodeMaximal = 1;
    private double coefficientRemboursement = 0.33; // indique le coefficient max du salaire à prélever
    private boolean typeAvance, assistance;
    private Date dateSave = new Date();
    private Mutuelle mutuelle = new Mutuelle();
    private List<YvsMutGrilleTauxTypeCredit> grilles;
    private List<YvsMutFraisTypeCredit> frais;
    private boolean selectActif, new_, impayeDette;
    private int jourDebutAvance = 15, jourFinAvance = 17;

    public TypeCredit() {
        grilles = new ArrayList<>();
        frais = new ArrayList<>();
    }

    public TypeCredit(long id) {
        this.id = id;
        grilles = new ArrayList<>();
        frais = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isByFusion() {
        return byFusion;
    }

    public void setByFusion(boolean byFusion) {
        this.byFusion = byFusion;
    }

    public List<YvsMutFraisTypeCredit> getFrais() {
        return frais;
    }

    public void setFrais(List<YvsMutFraisTypeCredit> frais) {
        this.frais = frais;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isAssistance() {
        return assistance;
    }

    public void setAssistance(boolean assistance) {
        this.assistance = assistance;
    }

    public boolean isTypeAvance() {
        return typeAvance;
    }

    public void setTypeAvance(boolean typeAvance) {
        this.typeAvance = typeAvance;
    }

    public String getSuffixeMontant() {
        return suffixeMontant;
    }

    public void setSuffixeMontant(String suffixeMontant) {
        this.suffixeMontant = suffixeMontant;
    }

    public boolean isImpayeDette() {
        return impayeDette;
    }

    public void setImpayeDette(boolean impayeDette) {
        this.impayeDette = impayeDette;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public String getNatureMontant() {
        return natureMontant != null ? natureMontant : "";
    }

    public void setNatureMontant(String natureMontant) {
        this.natureMontant = natureMontant;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getMontantMaximal() {
        return montantMaximal;
    }

    public void setMontantMaximal(double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public double getTauxMaximal() {
        return tauxMaximal;
    }

    public void setTauxMaximal(double tauxMaximal) {
        this.tauxMaximal = tauxMaximal;
    }

    public double getPeriodeMaximal() {
        return periodeMaximal;
    }

    public void setPeriodeMaximal(double periodeMaximal) {
        this.periodeMaximal = periodeMaximal;
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public List<YvsMutGrilleTauxTypeCredit> getGrilles() {
        return grilles;
    }

    public void setGrilles(List<YvsMutGrilleTauxTypeCredit> grilles) {
        this.grilles = grilles;
    }

    public int getJourDebutAvance() {
        return jourDebutAvance;
    }

    public void setJourDebutAvance(int jourDebutAvance) {
        this.jourDebutAvance = jourDebutAvance;
    }

    public int getJourFinAvance() {
        return jourFinAvance;
    }

    public void setJourFinAvance(int jourFinAvance) {
        this.jourFinAvance = jourFinAvance;
    }

    public double getNbreAvalise() {
        return nbreAvalise;
    }

    public void setNbreAvalise(double nbreAvalise) {
        this.nbreAvalise = nbreAvalise;
    }

    public int getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(int periodicite) {
        this.periodicite = periodicite;
    }

    public char getTypeMensualite() {
        return typeMensualite;
    }

    public void setTypeMensualite(char typeMensualite) {
        this.typeMensualite = typeMensualite;
    }

    public char getFormuleInteret() {
        return formuleInteret;
    }

    public void setFormuleInteret(char formuleInteret) {
        this.formuleInteret = formuleInteret;
    }

    public char getModelRemboursement() {
        return modelRemboursement;
    }

    public void setModelRemboursement(char modelRemboursement) {
        this.modelRemboursement = modelRemboursement;
    }

    public boolean isReechellonagePossible() {
        return reechellonagePossible;
    }

    public void setReechellonagePossible(boolean reechellonagePossible) {
        this.reechellonagePossible = reechellonagePossible;
    }

    public boolean isFusionPossible() {
        return fusionPossible;
    }

    public void setFusionPossible(boolean fusionPossible) {
        this.fusionPossible = fusionPossible;
    }

    public boolean isAnticipationPossible() {
        return anticipationPossible;
    }

    public void setAnticipationPossible(boolean anticipationPossible) {
        this.anticipationPossible = anticipationPossible;
    }

    public boolean isPenaliteAnticipation() {
        return penaliteAnticipation;
    }

    public void setPenaliteAnticipation(boolean penaliteAnticipation) {
        this.penaliteAnticipation = penaliteAnticipation;
    }

    public double getTauxPenaliteAnticipation() {
        return tauxPenaliteAnticipation;
    }

    public void setTauxPenaliteAnticipation(double tauxPenaliteAnticipation) {
        this.tauxPenaliteAnticipation = tauxPenaliteAnticipation;
    }

    public char getBasePenaliteAnticipation() {
        return basePenaliteAnticipation;
    }

    public void setBasePenaliteAnticipation(char basePenaliteAnticipation) {
        this.basePenaliteAnticipation = basePenaliteAnticipation;
    }

    public boolean isSuspensionPossible() {
        return suspensionPossible;
    }

    public void setSuspensionPossible(boolean suspensionPossible) {
        this.suspensionPossible = suspensionPossible;
    }

    public boolean isPenaliteSuspension() {
        return penaliteSuspension;
    }

    public void setPenaliteSuspension(boolean penaliteSuspension) {
        this.penaliteSuspension = penaliteSuspension;
    }

    public double getTauxPenaliteSuspension() {
        return tauxPenaliteSuspension;
    }

    public void setTauxPenaliteSuspension(double tauxPenaliteSuspension) {
        this.tauxPenaliteSuspension = tauxPenaliteSuspension;
    }

    public char getBasePenaliteSuspension() {
        return basePenaliteSuspension;
    }

    public void setBasePenaliteSuspension(char basePenaliteSuspension) {
        this.basePenaliteSuspension = basePenaliteSuspension;
    }

    public boolean isPenaliteRetard() {
        return penaliteRetard;
    }

    public void setPenaliteRetard(boolean penaliteRetard) {
        this.penaliteRetard = penaliteRetard;
    }

    public double getTauxPenaliteRetard() {
        return tauxPenaliteRetard;
    }

    public void setTauxPenaliteRetard(double tauxPenaliteRetard) {
        this.tauxPenaliteRetard = tauxPenaliteRetard;
    }

    public char getBasePenaliteRetard() {
        return basePenaliteRetard;
    }

    public void setBasePenaliteRetard(char basePenaliteRetard) {
        this.basePenaliteRetard = basePenaliteRetard;
    }

    public char getNaturePenaliteAnticipation() {
        return naturePenaliteAnticipation;
    }

    public void setNaturePenaliteAnticipation(char naturePenaliteAnticipation) {
        this.naturePenaliteAnticipation = naturePenaliteAnticipation;
    }

    public char getNaturePenaliteSuspension() {
        return naturePenaliteSuspension;
    }

    public void setNaturePenaliteSuspension(char naturePenaliteSuspension) {
        this.naturePenaliteSuspension = naturePenaliteSuspension;
    }

    public char getNaturePenaliteRetard() {
        return naturePenaliteRetard;
    }

    public void setNaturePenaliteRetard(char naturePenaliteRetard) {
        this.naturePenaliteRetard = naturePenaliteRetard;
    }

    public double getCoefficientRemboursement() {
        return coefficientRemboursement;
    }

    public void setCoefficientRemboursement(double coefficientRemboursement) {
        this.coefficientRemboursement = coefficientRemboursement;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final TypeCredit other = (TypeCredit) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
