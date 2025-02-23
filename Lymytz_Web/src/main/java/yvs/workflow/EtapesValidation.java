/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.util.Constantes;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class EtapesValidation implements Serializable {

    private long id;
    private String labelStatu;
    private String nature;
    private boolean valide;
    private int ordre;
    private ModelDocuments document;
    private List<AutorisationValidation> listUsersAutorise;
    private long idEtapeSuivante;
    private EtapesValidation etapeSuivante;
    private boolean firstEtape;
    private boolean etapeActive;
    private boolean livraisonHere;
    private boolean canUpdateHere;
    private boolean printHere;
    private boolean reglementHere;
    private String titreEtape;
    private boolean actif = true;
    private Date dateSave = new Date();

    public EtapesValidation() {
        listUsersAutorise = new ArrayList<>();
    }

    public EtapesValidation(long id, String labelStatu, boolean valide, ModelDocuments document) {
        this.id = id;
        this.labelStatu = labelStatu;
        this.valide = valide;
        this.document = document;
        listUsersAutorise = new ArrayList<>();
    }

    public boolean isLivraisonHere() {
        return livraisonHere;
    }

    public void setLivraisonHere(boolean livraisonHere) {
        this.livraisonHere = livraisonHere;
    }

    public String getNature() {
        return nature != null ? nature.trim().length() > 0 ? nature : Constantes.OP_DONS : Constantes.OP_DONS;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabelStatu() {
        return labelStatu;
    }

    public void setLabelStatu(String labelStatu) {
        this.labelStatu = labelStatu;
    }

    public boolean isValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public ModelDocuments getDocument() {
        return document;
    }

    public void setDocument(ModelDocuments document) {
        this.document = document;
    }

    public List<AutorisationValidation> getListUsersAutorise() {
        return listUsersAutorise;
    }

    public void setListUsersAutorise(List<AutorisationValidation> listUsersAutorise) {
        this.listUsersAutorise = listUsersAutorise;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public boolean isPrintHere() {
        return printHere;
    }

    public void setPrintHere(boolean printHere) {
        this.printHere = printHere;
    }

    public long getIdEtapeSuivante() {
        return idEtapeSuivante;
    }

    public void setIdEtapeSuivante(long idEtapeSuivante) {
        this.idEtapeSuivante = idEtapeSuivante;
    }

    public EtapesValidation getEtapeSuivante() {
        return etapeSuivante;
    }

    public void setEtapeSuivante(EtapesValidation etapeSuivante) {
        this.etapeSuivante = etapeSuivante;
    }

    public boolean isFirstEtape() {
        return firstEtape;
    }

    public void setFirstEtape(boolean firstEtape) {
        this.firstEtape = firstEtape;
    }

    public boolean isEtapeActive() {
        return etapeActive;
    }

    public void setEtapeActive(boolean etapeActive) {
        this.etapeActive = etapeActive;
    }

    public String getTitreEtape() {
        return titreEtape;
    }

    public void setTitreEtape(String titreEtape) {
        this.titreEtape = titreEtape;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isCanUpdateHere() {
        return canUpdateHere;
    }

    public void setCanUpdateHere(boolean canUpdateHere) {
        this.canUpdateHere = canUpdateHere;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isReglementHere() {
        return reglementHere;
    }

    public void setReglementHere(boolean reglementHere) {
        this.reglementHere = reglementHere;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final EtapesValidation other = (EtapesValidation) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
