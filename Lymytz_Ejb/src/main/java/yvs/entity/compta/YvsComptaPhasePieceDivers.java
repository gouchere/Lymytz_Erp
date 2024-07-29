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
import javax.xml.bind.annotation.XmlTransient;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceDivers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_phase_piece_divers")
@NamedQueries({
    @NamedQuery(name = "YvsComptaPhasePieceDivers.findAll", query = "SELECT y FROM YvsComptaPhasePieceDivers y"),
    @NamedQuery(name = "YvsComptaPhasePieceDivers.findById", query = "SELECT y FROM YvsComptaPhasePieceDivers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaPhasePieceDivers.findByIdAll", query = "SELECT y FROM YvsComptaPhasePieceDivers y JOIN FETCH y.caisse JOIN FETCH y.phaseReg JOIN FETCH y.pieceDivers "
            + " WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaPhasePieceDivers.findByPhaseOk", query = "SELECT y FROM YvsComptaPhasePieceDivers y WHERE y.phaseOk = :phaseOk"),
    @NamedQuery(name = "YvsComptaPhasePieceDivers.findByDateUpdate", query = "SELECT y FROM YvsComptaPhasePieceDivers y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaPhasePieceDivers.findByDateSave", query = "SELECT y FROM YvsComptaPhasePieceDivers y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaPhasePieceDivers.findByPiece", query = "SELECT y FROM YvsComptaPhasePieceDivers y WHERE y.pieceDivers=:piece ORDER BY y.phaseReg.numeroPhase "),

    @NamedQuery(name = "YvsComptaPhasePieceDivers.findPrecByPiece", query = "SELECT y FROM YvsComptaPhasePieceDivers y WHERE y.pieceDivers = :piece AND y.phaseReg.numeroPhase < :numero ORDER BY y.phaseReg.numeroPhase DESC"),
    @NamedQuery(name = "YvsComptaPhasePieceDivers.findNextByPiece", query = "SELECT y FROM YvsComptaPhasePieceDivers y WHERE y.pieceDivers = :piece AND y.phaseReg.numeroPhase > :numero ORDER BY y.phaseReg.numeroPhase")})
public class YvsComptaPhasePieceDivers implements Serializable, Comparator<YvsComptaPhasePieceDivers> {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_phase_piece_divers_id_seq", name = "yvs_compta_phase_piece_divers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_phase_piece_divers_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "phase_ok")
    private Boolean phaseOk;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_valider")
    @Temporal(TemporalType.DATE)
    private Date dateValider;
    @Column(name = "statut")
    private Character statut;

    @OneToOne(mappedBy = "etape")
    private YvsComptaContentJournalEtapePieceDivers pieceContenu;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "phase_reg", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaPhaseReglement phaseReg;
    @JoinColumn(name = "piece_divers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceDivers pieceDivers;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @Transient
    private boolean etapeActive;
    @Transient
    private boolean comptabilised;

    public YvsComptaPhasePieceDivers() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaPhasePieceDivers(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaPhasePieceDivers(YvsComptaPhasePieceDivers p) {
        this(p.getId());
        this.author = p.getAuthor();
        this.caisse = p.getCaisse();
        this.pieceDivers = p.pieceDivers;
        this.phaseReg = p.phaseReg;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public Character getStatut() {
        return statut != null ? String.valueOf(statut).trim().length() > 0 ? statut : 'W' : 'W';
    }

    public void setStatut(Character statut) {
        this.statut = statut;
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

    public YvsComptaCaissePieceDivers getPieceDivers() {
        return pieceDivers;
    }

    public void setPieceDivers(YvsComptaCaissePieceDivers pieceDivers) {
        this.pieceDivers = pieceDivers;
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

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
    }

    @XmlTransient
    public YvsComptaContentJournalEtapePieceDivers getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalEtapePieceDivers pieceContenu) {
        this.pieceContenu = pieceContenu;
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
        if (!(object instanceof YvsComptaPhasePieceDivers)) {
            return false;
        }
        YvsComptaPhasePieceDivers other = (YvsComptaPhasePieceDivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.vente.YvsComptaPhasePieceDivers[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsComptaPhasePieceDivers o1, YvsComptaPhasePieceDivers o2) {
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
