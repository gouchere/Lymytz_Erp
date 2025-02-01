/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.salaire;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_phase_piece_salaire")
@NamedQueries({
    @NamedQuery(name = "YvsComptaPhasePieceSalaire.findAll", query = "SELECT y FROM YvsComptaPhasePieceSalaire y"),
    @NamedQuery(name = "YvsComptaPhasePieceSalaire.findById", query = "SELECT y FROM YvsComptaPhasePieceSalaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaPhasePieceSalaire.findByPhaseOk", query = "SELECT y FROM YvsComptaPhasePieceSalaire y WHERE y.phaseOk = :phaseOk"),
    @NamedQuery(name = "YvsComptaPhasePieceSalaire.findByDateUpdate", query = "SELECT y FROM YvsComptaPhasePieceSalaire y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaPhasePieceSalaire.findByDateSave", query = "SELECT y FROM YvsComptaPhasePieceSalaire y WHERE y.dateSave = :dateSave")})
public class YvsComptaPhasePieceSalaire implements Serializable, Comparator<YvsComptaPhasePieceSalaire> {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_phase_piece_salaire_id_seq", name = "yvs_compta_phase_piece_salaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_phase_piece_salaire_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "phase_ok")
    private Boolean phaseOk;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_valider")
    @Temporal(TemporalType.DATE)
    private Date dateValider;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "phase_reg", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaPhaseReglement phaseReg;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceSalaire piece;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @Transient
    private boolean etapeActive;

    public YvsComptaPhasePieceSalaire() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaPhasePieceSalaire(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateValider() {
        return dateValider;
    }

    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    public Boolean getPhaseOk() {
        return phaseOk;
    }

    public void setPhaseOk(Boolean phaseOk) {
        this.phaseOk = phaseOk;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComptaPhaseReglement getPhaseReg() {
        return phaseReg;
    }

    public void setPhaseReg(YvsComptaPhaseReglement phaseReg) {
        this.phaseReg = phaseReg;
    }

    public YvsComptaCaissePieceSalaire getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaCaissePieceSalaire piece) {
        this.piece = piece;
    }

    public boolean isEtapeActive() {
        return etapeActive;
    }

    public void setEtapeActive(boolean etapeActive) {
        this.etapeActive = etapeActive;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComptaPhasePieceSalaire)) {
            return false;
        }
        YvsComptaPhasePieceSalaire other = (YvsComptaPhasePieceSalaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.vente.YvsComptaPhasePieceSalaire[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsComptaPhasePieceSalaire o1, YvsComptaPhasePieceSalaire o2) {
        if (o1 == null && o2 != null) {
            if (o2.getPhaseReg() != null) {
                return -1;
            }
        }
        if (o2 == null && o1 != null) {
            if (o1.getPhaseReg() != null) {
                return 1;
            }
        }
        return (o1.getPhaseReg().getNumeroPhase().compareTo(o2.getPhaseReg().getNumeroPhase()));

    }

}
