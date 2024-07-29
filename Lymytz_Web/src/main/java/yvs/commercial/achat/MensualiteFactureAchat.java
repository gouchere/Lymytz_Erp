/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class MensualiteFactureAchat implements Serializable, Comparable {

    private long id;
    private Date dateMensualite = new Date();
    private double montant, montantRest;
    private String nameMens;
    private DocAchat facture = new DocAchat();
    private String etat = Constantes.ETAT_EDITABLE;
    private List<PieceTresorerie> reglements;
    private boolean selectActif, new_, update, outDelai;

    public MensualiteFactureAchat() {
        reglements = new ArrayList<>();
    }

    public MensualiteFactureAchat(long id) {
        this.id = id;
        reglements = new ArrayList<>();
    }

    public boolean isOutDelai() {
        return outDelai;
    }

    public void setOutDelai(boolean outDelai) {
        this.outDelai = outDelai;
    }

    public String getNameMens() {
        return nameMens;
    }

    public void setNameMens(String nameMens) {
        this.nameMens = nameMens;
    }

    public double getMontantRest() {
        return montantRest;
    }

    public void setMontantRest(double montantRest) {
        this.montantRest = montantRest;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateMensualite() {
        return dateMensualite;
    }

    public void setDateMensualite(Date dateMensualite) {
        this.dateMensualite = dateMensualite;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public DocAchat getFacture() {
        return facture;
    }

    public void setFacture(DocAchat facture) {
        this.facture = facture;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public List<PieceTresorerie> getReglements() {
        return reglements;
    }

    public void setReglements(List<PieceTresorerie> reglements) {
        this.reglements = reglements;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final MensualiteFactureAchat other = (MensualiteFactureAchat) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        MensualiteFactureAchat m = (MensualiteFactureAchat) o;
        if (dateMensualite.equals(m.dateMensualite)) {
            return Long.valueOf(id).compareTo(m.id);
        }
        return dateMensualite.compareTo(m.dateMensualite);
    }

}
