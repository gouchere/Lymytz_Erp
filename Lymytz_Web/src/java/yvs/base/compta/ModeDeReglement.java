/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.util.Constantes;
import yvs.util.Util;

/**
 *
 * @author LENOVO
 */
@ManagedBean
@SessionScoped
public class ModeDeReglement implements Serializable {

    private int id;
    private String designation;
    private String description;
    private String numeroMarchand;
    private String typeReglement;   //Reglement par caisse, par salaire, par banques, effet de commerce
    private String codePaiement;   //Reglement par Orange money, par Mobile money, par VISA, Autre
    private boolean actif = true, defaut, update;
    private boolean visibleOnPrintVente;
    private Date dateSave = new Date();
    private InfosModeReglement infos = new InfosModeReglement();
    private ModeReglementBanque banque = new ModeReglementBanque();
    private List<YvsComptaPhaseReglement> phases;

    public ModeDeReglement() {
        phases = new ArrayList<>();
    }

    public ModeDeReglement(int id) {
        this();
        this.id = id;
    }

    public ModeDeReglement(int id, String designation) {
        this(id);
        this.designation = designation;
    }

    public ModeDeReglement(int id, String designation, String typeReglement) {
        this(id, designation);
        this.typeReglement = typeReglement;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCodePaiement() {
        return codePaiement != null ? Util.asString(codePaiement) ? codePaiement : Constantes.CODE_PAIEMENT_AUTRE : Constantes.CODE_PAIEMENT_AUTRE;
    }

    public void setCodePaiement(String codePaiement) {
        this.codePaiement = codePaiement;
    }

    public String getTypeReglement() {
        return typeReglement != null ? typeReglement.trim().length() > 0 ? typeReglement : Constantes.MODE_PAIEMENT_ESPECE : Constantes.MODE_PAIEMENT_ESPECE;
    }

    public void setTypeReglement(String typeReglement) {
        this.typeReglement = typeReglement;
    }

    public String getNumeroMarchand() {
        return numeroMarchand != null ? numeroMarchand : "";
    }

    public void setNumeroMarchand(String numeroMarchand) {
        this.numeroMarchand = numeroMarchand;
    }

    public boolean isVisibleOnPrintVente() {
        return visibleOnPrintVente;
    }

    public void setVisibleOnPrintVente(boolean visibleOnPrintVente) {
        this.visibleOnPrintVente = visibleOnPrintVente;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public InfosModeReglement getInfos() {
        return infos;
    }

    public void setInfos(InfosModeReglement infos) {
        this.infos = infos;
    }

    public ModeReglementBanque getBanque() {
        return banque;
    }

    public void setBanque(ModeReglementBanque banque) {
        this.banque = banque;
    }

    public List<YvsComptaPhaseReglement> getPhases() {
        return phases;
    }

    public void setPhases(List<YvsComptaPhaseReglement> phases) {
        this.phases = phases;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ModeDeReglement other = (ModeDeReglement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
