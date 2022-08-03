/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceVente;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_phase_piece")
@NamedQueries({
    @NamedQuery(name = "YvsComptaPhasePiece.findAll", query = "SELECT y FROM YvsComptaPhasePiece y"),
    @NamedQuery(name = "YvsComptaPhasePiece.findById", query = "SELECT y FROM YvsComptaPhasePiece y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaPhasePiece.findByPhaseOk", query = "SELECT y FROM YvsComptaPhasePiece y WHERE y.phaseOk = :phaseOk"),
    @NamedQuery(name = "YvsComptaPhasePiece.findByPhasePiece", query = "SELECT y FROM YvsComptaPhasePiece y WHERE y.pieceVente = :piece AND y.phaseReg = :phase"),

    @NamedQuery(name = "YvsComptaPhasePiece.findPrecByPiece", query = "SELECT y FROM YvsComptaPhasePiece y WHERE y.pieceVente = :piece AND y.phaseReg.numeroPhase < :numero ORDER BY y.phaseReg.numeroPhase DESC"),
    @NamedQuery(name = "YvsComptaPhasePiece.findNextByPiece", query = "SELECT y FROM YvsComptaPhasePiece y WHERE y.pieceVente = :piece AND y.phaseReg.numeroPhase > :numero ORDER BY y.phaseReg.numeroPhase"),
    @NamedQuery(name = "YvsComptaPhasePiece.findByBefore", query = "SELECT y FROM YvsComptaPhasePiece y WHERE y.pieceVente = :piece AND y.phaseOk = true AND y.phaseReg.numeroPhase < :numero ORDER BY y.phaseReg.numeroPhase DESC"),
    @NamedQuery(name = "YvsComptaPhasePiece.findActiveByPiece", query = "SELECT y FROM YvsComptaPhasePiece y WHERE y.pieceVente = :piece AND y.phaseOk = true ORDER BY y.phaseReg.numeroPhase"),
    @NamedQuery(name = "YvsComptaPhasePiece.findByPiece", query = "SELECT y FROM YvsComptaPhasePiece y JOIN FETCH y.phaseReg JOIN FETCH y.caisse JOIN FETCH y.pieceVente WHERE y.pieceVente = :piece ORDER BY y.phaseReg.numeroPhase"),
    @NamedQuery(name = "YvsComptaPhasePiece.findByActive", query = "SELECT y FROM YvsComptaPhasePiece y WHERE y.pieceVente = :piece AND y.phaseOk = true ORDER BY y.phaseReg.numeroPhase DESC")})
public class YvsComptaPhasePiece implements Serializable, Comparator<YvsComptaPhasePiece> {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_phase_piece_id_seq", name = "yvs_compta_phase_piece_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_phase_piece_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_valider")
    @Temporal(TemporalType.DATE)
    private Date dateValider;
    @Column(name = "phase_ok")
    private Boolean phaseOk;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "statut")
    private Character statut;

    @OneToOne(mappedBy = "etape", fetch = FetchType.LAZY)
    private YvsComptaContentJournalEtapePieceVente pieceContenu;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "phase_reg", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaPhaseReglement phaseReg;
    @JoinColumn(name = "piece_vente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceVente pieceVente;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;

    @Transient
    private boolean etapeActive;
    @Transient
    private boolean comptabilised;

    public YvsComptaPhasePiece() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaPhasePiece(Long id) {
        this();
        this.id = id;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
    }

    public Date getDateValider() {
        return dateValider;
    }

    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPhaseOk() {
        return phaseOk != null ? phaseOk : false;
    }

    public void setPhaseOk(Boolean phaseOk) {
        this.phaseOk = phaseOk;
    }

    public Character getStatut() {
        return statut != null ? String.valueOf(statut).trim().length() > 0 ? statut : 'W' : 'W';
    }

    public void setStatut(Character statut) {
        this.statut = statut;
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

    public boolean isEtapeActive() {
        return etapeActive;
    }

    public void setEtapeActive(boolean etapeActive) {
        this.etapeActive = etapeActive;
    }

    public YvsComptaCaissePieceVente getPieceVente() {
        return pieceVente;
    }

    public void setPieceVente(YvsComptaCaissePieceVente pieceVente) {
        this.pieceVente = pieceVente;
    }

    public YvsComptaContentJournalEtapePieceVente getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalEtapePieceVente pieceContenu) {
        this.pieceContenu = pieceContenu;
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
        if (!(object instanceof YvsComptaPhasePiece)) {
            return false;
        }
        YvsComptaPhasePiece other = (YvsComptaPhasePiece) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaPhasePiece[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsComptaPhasePiece o1, YvsComptaPhasePiece o2) {
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
