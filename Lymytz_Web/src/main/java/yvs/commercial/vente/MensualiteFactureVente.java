/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.ModeDeReglement;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class MensualiteFactureVente implements Serializable, Comparable {

    private long id;
    private Date dateMensualite = new Date();
    private Date dateSave = new Date();
    private double montant, montantRest, avance;
    private String nameMens;
    private DocVente facture = new DocVente();
    private String etat = Constantes.ETAT_ATTENTE;
    private char statut = 'W';
    private List<YvsComptaCaissePieceVente> reglements;
    private boolean selectActif, new_, update, outDelai;
    private ModeDeReglement modeReglement = new ModeDeReglement();

    public MensualiteFactureVente() {
        reglements = new ArrayList<>();
    }

    public MensualiteFactureVente(long id) {
        this.id = id;
        reglements = new ArrayList<>();
    }

    public double getAvance() {
        return avance;
    }

    public void setAvance(double avance) {
        this.avance = avance;
    }

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public ModeDeReglement getModeReglement() {
        return modeReglement;
    }

    public void setModeReglement(ModeDeReglement modeReglement) {
        this.modeReglement = modeReglement;
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

    public DocVente getFacture() {
        return facture;
    }

    public void setFacture(DocVente facture) {
        this.facture = facture;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
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

    public List<YvsComptaCaissePieceVente> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceVente> reglements) {
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
        final MensualiteFactureVente other = (MensualiteFactureVente) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        MensualiteFactureVente m = (MensualiteFactureVente) o;
        if (dateMensualite.equals(m.dateMensualite)) {
            return Long.valueOf(id).compareTo(m.id);
        }
        return dateMensualite.compareTo(m.dateMensualite);
    }

}
