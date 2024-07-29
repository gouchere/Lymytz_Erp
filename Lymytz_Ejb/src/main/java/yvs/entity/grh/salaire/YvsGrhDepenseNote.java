/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.grh.salaire;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity; import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_depense_note")
@NamedQueries({
    @NamedQuery(name = "YvsGrhDepenseNote.findAll", query = "SELECT y FROM YvsGrhDepenseNote y"),
    @NamedQuery(name = "YvsGrhDepenseNote.findByNoteFrais", query = "SELECT y FROM YvsGrhDepenseNote y WHERE y.yvsGrhDepenseNotePK.noteFrais = :noteFrais"),
    @NamedQuery(name = "YvsGrhDepenseNote.findByTypeDepense", query = "SELECT y FROM YvsGrhDepenseNote y WHERE y.yvsGrhDepenseNotePK.typeDepense = :typeDepense"),
    @NamedQuery(name = "YvsGrhDepenseNote.findByMontant", query = "SELECT y FROM YvsGrhDepenseNote y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsGrhDepenseNote.findNoteNonPaye", query = "SELECT SUM (y.montantApprouve) FROM YvsGrhDepenseNote y WHERE y.yvsGrhNotesFrais.employe = :employe AND (y.yvsGrhNotesFrais.dateNote<=:fin AND y.yvsGrhNotesFrais.dateNote>=:debut) AND y.yvsGrhNotesFrais.statut='V'"),
    @NamedQuery(name = "YvsGrhDepenseNote.findByMontantApprouve", query = "SELECT y FROM YvsGrhDepenseNote y WHERE y.montantApprouve = :montantApprouve")})
public class YvsGrhDepenseNote implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected YvsGrhDepenseNotePK yvsGrhDepenseNotePK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "montant_approuve")
    private double montantApprouve;
    @JoinColumn(name = "type_depense", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeDepense yvsGrhTypeDepense;
    @JoinColumn(name = "note_frais", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhNotesFrais yvsGrhNotesFrais;

    public YvsGrhDepenseNote() {
    }

    public YvsGrhDepenseNote(YvsGrhDepenseNotePK yvsGrhDepenseNotePK) {
        this.yvsGrhDepenseNotePK = yvsGrhDepenseNotePK;
    }

    public YvsGrhDepenseNote(long noteFrais, int typeDepense) {
        this.yvsGrhDepenseNotePK = new YvsGrhDepenseNotePK(noteFrais, typeDepense);
    }

    public YvsGrhDepenseNotePK getYvsGrhDepenseNotePK() {
        return yvsGrhDepenseNotePK;
    }

    public void setYvsGrhDepenseNotePK(YvsGrhDepenseNotePK yvsGrhDepenseNotePK) {
        this.yvsGrhDepenseNotePK = yvsGrhDepenseNotePK;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Double getMontantApprouve() {
        return montantApprouve;
    }

    public void setMontantApprouve(Double montantApprouve) {
        this.montantApprouve = montantApprouve;
    }

    public YvsGrhTypeDepense getYvsGrhTypeDepense() {
        return yvsGrhTypeDepense;
    }

    public void setYvsGrhTypeDepense(YvsGrhTypeDepense yvsGrhTypeDepense) {
        this.yvsGrhTypeDepense = yvsGrhTypeDepense;
    }

    public YvsGrhNotesFrais getYvsGrhNotesFrais() {
        return yvsGrhNotesFrais;
    }

    public void setYvsGrhNotesFrais(YvsGrhNotesFrais yvsGrhNotesFrais) {
        this.yvsGrhNotesFrais = yvsGrhNotesFrais;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (yvsGrhDepenseNotePK != null ? yvsGrhDepenseNotePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsGrhDepenseNote)) {
            return false;
        }
        YvsGrhDepenseNote other = (YvsGrhDepenseNote) object;
        if ((this.yvsGrhDepenseNotePK == null && other.yvsGrhDepenseNotePK != null) || (this.yvsGrhDepenseNotePK != null && !this.yvsGrhDepenseNotePK.equals(other.yvsGrhDepenseNotePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhDepenseNote[ yvsGrhDepenseNotePK=" + yvsGrhDepenseNotePK + " ]";
    }
    
}
