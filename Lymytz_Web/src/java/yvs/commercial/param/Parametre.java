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
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean(name = "parametre_compta")
@SessionScoped
public class Parametre implements Serializable {

    private long id;
    private boolean reglementAuto, useLotReception;
    private int converter = 0;
    private int converterCs = 2; //pour evaluer le cout du stock
    private String documentMouvAchat = Constantes.DOC_MOUV_BL;
    private String documentMouvVente = Constantes.DOC_MOUV_F;
    private String modeInventaire = Constantes.MODE_INV_PERM;
    private String documentGenererFromEcart = Constantes.DOC_MANAGE_ECART_RE;
    private String tauxMargeSur = "V";
    private Date dateSave = new Date();
    private double seuilFsseur;
    private double seuilClient;
    private int dureeInactiv;
    private int jourUsine = 30;
    private boolean selectActif, update;
    private boolean displayPrixRevient;
    private boolean displayResteALivrer;
    private boolean displayAvgPuv;

    public Parametre() {
    }

    public Parametre(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDisplayPrixRevient() {
        return displayPrixRevient;
    }

    public void setDisplayPrixRevient(boolean displayPrixRevient) {
        this.displayPrixRevient = displayPrixRevient;
    }

    public boolean isDisplayResteALivrer() {
        return displayResteALivrer;
    }

    public void setDisplayResteALivrer(boolean displayResteALivrer) {
        this.displayResteALivrer = displayResteALivrer;
    }

    public boolean isDisplayAvgPuv() {
        return displayAvgPuv;
    }

    public void setDisplayAvgPuv(boolean displayAvgPuv) {
        this.displayAvgPuv = displayAvgPuv;
    }

    public int getConverter() {
        return converter;
    }

    public void setConverter(int converter) {
        this.converter = converter;
    }

    public int getConverterCs() {
        return converterCs;
    }

    public void setConverterCs(int converterCs) {
        this.converterCs = converterCs;
    }

    public int getDureeInactiv() {
        return dureeInactiv;
    }

    public void setDureeInactiv(int dureeInactiv) {
        this.dureeInactiv = dureeInactiv;
    }

    public double getSeuilFsseur() {
        return seuilFsseur;
    }

    public void setSeuilFsseur(double seuilFsseur) {
        this.seuilFsseur = seuilFsseur;
    }

    public double getSeuilClient() {
        return seuilClient;
    }

    public void setSeuilClient(double seuilClient) {
        this.seuilClient = seuilClient;
    }

    public String getDocumentMouvAchat() {
        return documentMouvAchat;
    }

    public void setDocumentMouvAchat(String documentMouvAchat) {
        this.documentMouvAchat = documentMouvAchat;
    }

    public String getDocumentMouvVente() {
        return documentMouvVente;
    }

    public void setDocumentMouvVente(String documentMouvVente) {
        this.documentMouvVente = documentMouvVente;
    }

    public String getModeInventaire() {
        return modeInventaire;
    }

    public void setModeInventaire(String modeInventaire) {
        this.modeInventaire = modeInventaire;
    }

    public String getTauxMargeSur() {
        return tauxMargeSur != null ? !tauxMargeSur.trim().isEmpty() ? tauxMargeSur : "V" : "V";
    }

    public void setTauxMargeSur(String tauxMargeSur) {
        this.tauxMargeSur = tauxMargeSur;
    }

    public boolean isReglementAuto() {
        return reglementAuto;
    }

    public void setReglementAuto(boolean reglementAuto) {
        this.reglementAuto = reglementAuto;
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

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public int getJourUsine() {
        return jourUsine;
    }

    public void setJourUsine(int jourUsine) {
        this.jourUsine = jourUsine;
    }

    public boolean isUseLotReception() {
        return useLotReception;
    }

    public void setUseLotReception(boolean useLotReception) {
        this.useLotReception = useLotReception;
    }

    public String getDocumentGenererFromEcart() {
        return documentGenererFromEcart;
    }

    public void setDocumentGenererFromEcart(String documentGenererFromEcart) {
        this.documentGenererFromEcart = documentGenererFromEcart;
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
        final Parametre other = (Parametre) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
