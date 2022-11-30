/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.param;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Journaux;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ParametreCom implements Serializable {

    private long id;
    private boolean comptabilisationAuto;
    private boolean livraisonAuto;
    private boolean printDocumentWhenValide;
    private String comptabilisationMode;
    private String modelFactureVente;
    private String giveBonusInStatus;
    private boolean paieWithoutValide;
    private boolean genererFactureAuto;
    private boolean activeRation = true;
    private boolean calculPr = true;
    private boolean livreBcvWithoutPaye = true;
    private int jourAnterieur;
    private int nbFicheMax;
    private int tailleCodeRation;
    private Date dateSave = new Date();
    private boolean selectActif, update;
    private Journaux journal = new Journaux();
    private boolean sellLowerPr = true;

    public ParametreCom() {
    }

    public ParametreCom(int id) {
        this.id = id;
    }

    public boolean isSellLowerPr() {
        return sellLowerPr;
    }

    public void setSellLowerPr(boolean sellLowerPr) {
        this.sellLowerPr = sellLowerPr;
    }

    public boolean isLivraisonAuto() {
        return livraisonAuto;
    }

    public void setLivraisonAuto(boolean livraisonAuto) {
        this.livraisonAuto = livraisonAuto;
    }

    public boolean isPrintDocumentWhenValide() {
        return printDocumentWhenValide;
    }

    public void setPrintDocumentWhenValide(boolean printDocumentWhenValide) {
        this.printDocumentWhenValide = printDocumentWhenValide;
    }

    public boolean isComptabilisationAuto() {
        return comptabilisationAuto;
    }

    public void setComptabilisationAuto(boolean comptabilisationAuto) {
        this.comptabilisationAuto = comptabilisationAuto;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getModelFactureVente() {
        return modelFactureVente != null ? !modelFactureVente.isEmpty() ? modelFactureVente : "facture_vente" : "facture_vente";
    }

    public void setModelFactureVente(String modelFactureVente) {
        this.modelFactureVente = modelFactureVente;
    }

    public String getComptabilisationMode() {
        return comptabilisationMode != null ? comptabilisationMode.trim().length() > 0 ? comptabilisationMode : "D" : "D";
    }

    public void setComptabilisationMode(String comptabilisationMode) {
        this.comptabilisationMode = comptabilisationMode;
    }

    public String getGiveBonusInStatus() {
        return giveBonusInStatus != null ? giveBonusInStatus.trim().length() > 0 ? giveBonusInStatus : "R" : "R";
    }

    public void setGiveBonusInStatus(String giveBonusInStatus) {
        this.giveBonusInStatus = giveBonusInStatus;
    }

    public int getJourAnterieur() {
        return jourAnterieur;
    }

    public void setJourAnterieur(int jourAnterieur) {
        this.jourAnterieur = jourAnterieur;
    }

    public int getTailleCodeRation() {
        return tailleCodeRation;
    }

    public void setTailleCodeRation(int tailleCodeRation) {
        this.tailleCodeRation = tailleCodeRation;
    }

    public boolean isPaieWithoutValide() {
        return paieWithoutValide;
    }

    public void setPaieWithoutValide(boolean paieWithoutValide) {
        this.paieWithoutValide = paieWithoutValide;
    }

    public boolean isActiveRation() {
        return activeRation;
    }

    public void setActiveRation(boolean activeRation) {
        this.activeRation = activeRation;
    }

    public boolean isCalculPr() {
        return calculPr;
    }

    public void setCalculPr(boolean calculPr) {
        this.calculPr = calculPr;
    }

    public boolean isGenererFactureAuto() {
        return genererFactureAuto;
    }

    public void setGenererFactureAuto(boolean genererFactureAuto) {
        this.genererFactureAuto = genererFactureAuto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNbFicheMax() {
        return nbFicheMax;
    }

    public void setNbFicheMax(int nbFicheMax) {
        this.nbFicheMax = nbFicheMax;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Journaux getJournal() {
        return journal;
    }

    public void setJournal(Journaux journal) {
        this.journal = journal;
    }

    public boolean isLivreBcvWithoutPaye() {
        return livreBcvWithoutPaye;
    }

    public void setLivreBcvWithoutPaye(boolean livreBcvWithoutPaye) {
        this.livreBcvWithoutPaye = livreBcvWithoutPaye;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ParametreCom other = (ParametreCom) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
