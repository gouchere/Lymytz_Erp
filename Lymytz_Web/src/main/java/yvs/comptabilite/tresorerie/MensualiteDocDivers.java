/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.tresorerie;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class MensualiteDocDivers implements Serializable {

    private long id;
    private Date dateMensualite = new Date();
    private double montant, montantRest;
    private String etat, nameMens;
    private DocCaissesDivers docDivers = new DocCaissesDivers();
    private List<PieceTresorerie> pieces;
    private boolean selectActif, new_, update, outDelai;

    public MensualiteDocDivers() {
        pieces = new ArrayList<>();
    }

    public MensualiteDocDivers(long id) {
        this.id = id;
        pieces = new ArrayList<>();
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

    public List<PieceTresorerie> getPieces() {
        return pieces;
    }

    public void setPieces(List<PieceTresorerie> pieces) {
        this.pieces = pieces;
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

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDateMensualite() {
        return dateMensualite;
    }

    public void setDateMensualite(Date dateMensualite) {
        this.dateMensualite = dateMensualite;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public DocCaissesDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(DocCaissesDivers docDivers) {
        this.docDivers = docDivers;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final MensualiteDocDivers other = (MensualiteDocDivers) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
