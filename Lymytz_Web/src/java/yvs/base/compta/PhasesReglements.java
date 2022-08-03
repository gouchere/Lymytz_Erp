/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

/**
 *
 * @author LENOVO
 */
public class PhasesReglements {

    private long id;
    private String phase;
    private boolean reglementOk;
    private boolean phaseOk, forEmission, actionInBanque;
    private int numeroPhase;
    private Comptes compteGeneral = new Comptes();
    private String codeComptable;
    private String libellePhase;

    public PhasesReglements() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isForEmission() {
        return forEmission;
    }

    public void setForEmission(boolean forEmission) {
        this.forEmission = forEmission;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public boolean isReglementOk() {
        return reglementOk;
    }

    public void setReglementOk(boolean reglementOk) {
        this.reglementOk = reglementOk;
    }

    public int getNumeroPhase() {
        return numeroPhase;
    }

    public void setNumeroPhase(int numeroPhase) {
        this.numeroPhase = numeroPhase;
    }

    public Comptes getCompteGeneral() {
        return compteGeneral;
    }

    public void setCompteGeneral(Comptes compteGeneral) {
        this.compteGeneral = compteGeneral;
    }

    public boolean isActionInBanque() {
        return actionInBanque;
    }

    public void setActionInBanque(boolean actionInBanque) {
        this.actionInBanque = actionInBanque;
    }

    public boolean isPhaseOk() {
        return phaseOk;
    }

    public void setPhaseOk(boolean phaseOk) {
        this.phaseOk = phaseOk;
    }

    public String getLibellePhase() {
        return libellePhase;
    }

    public void setLibellePhase(String libellePhase) {
        this.libellePhase = libellePhase;
    }

    public String getCodeComptable() {
        return codeComptable;
    }

    public void setCodeComptable(String codeComptable) {
        this.codeComptable = codeComptable;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PhasesReglements other = (PhasesReglements) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
