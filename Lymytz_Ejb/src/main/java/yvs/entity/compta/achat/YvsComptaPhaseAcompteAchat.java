/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.achat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeAcompteAchat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_phase_acompte_achat")
@NamedQueries({
    @NamedQuery(name = "YvsComptaPhaseAcompteAchat.findAll", query = "SELECT y FROM YvsComptaPhaseAcompteAchat y"),
    @NamedQuery(name = "YvsComptaPhaseAcompteAchat.findById", query = "SELECT y FROM YvsComptaPhaseAcompteAchat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaPhaseAcompteAchat.findByPhaseOk", query = "SELECT y FROM YvsComptaPhaseAcompteAchat y WHERE y.phaseOk = :phaseOk"),
    @NamedQuery(name = "YvsComptaPhaseAcompteAchat.findByDateUpdate", query = "SELECT y FROM YvsComptaPhaseAcompteAchat y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaPhaseAcompteAchat.findByDateSave", query = "SELECT y FROM YvsComptaPhaseAcompteAchat y WHERE y.dateSave = :dateSave"),

    @NamedQuery(name = "YvsComptaPhaseAcompteAchat.findPrecByPiece", query = "SELECT y FROM YvsComptaPhaseAcompteAchat y WHERE y.pieceAchat = :piece AND y.phaseReg.numeroPhase < :numero ORDER BY y.phaseReg.numeroPhase DESC"),
    @NamedQuery(name = "YvsComptaPhaseAcompteAchat.findNextByPiece", query = "SELECT y FROM YvsComptaPhaseAcompteAchat y WHERE y.pieceAchat = :piece AND y.phaseReg.numeroPhase > :numero ORDER BY y.phaseReg.numeroPhase"),
    @NamedQuery(name = "YvsComptaPhaseAcompteAchat.findByPiece", query = "SELECT y FROM YvsComptaPhaseAcompteAchat y JOIN FETCH y.caisse JOIN FETCH y.phaseReg JOIN FETCH y.pieceAchat WHERE y.pieceAchat = :piece ORDER BY y.phaseReg.numeroPhase"),
    @NamedQuery(name = "YvsComptaPhaseAcompteAchat.findActiveByPiece", query = "SELECT y FROM YvsComptaPhaseAcompteAchat y WHERE y.pieceAchat = :piece AND y.phaseOk = true ORDER BY y.phaseReg.numeroPhase")})
public class YvsComptaPhaseAcompteAchat implements Serializable, Comparator<YvsComptaPhaseAcompteAchat> {

    @Column(name = "statut")
    private Character statut;
    @OneToMany(mappedBy = "etape")
    private List<YvsComptaContentJournalEtapeAcompteAchat> yvsComptaContentJournalEtapeAcompteAchatList;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_phase_acompte_achat_id_seq", name = "yvs_compta_phase_acompte_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_phase_acompte_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "phase_reg", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaPhaseReglement phaseReg;
    @JoinColumn(name = "piece_achat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaAcompteFournisseur pieceAchat;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;

    @OneToOne(mappedBy = "etape")
    private YvsComptaContentJournalEtapeAcompteAchat pieceContenu;

    @Transient
    private boolean etapeActive;
    @Transient
    private boolean comptabilised;

    public YvsComptaPhaseAcompteAchat() {
    }

    public YvsComptaPhaseAcompteAchat(Long id) {
        this.id = id;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateValider() {
        return dateValider;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    public Boolean getPhaseOk() {
        return phaseOk;
    }

    public void setPhaseOk(Boolean phaseOk) {
        this.phaseOk = phaseOk;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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

    public YvsComptaAcompteFournisseur getPieceAchat() {
        return pieceAchat;
    }

    public void setPieceAchat(YvsComptaAcompteFournisseur pieceAchat) {
        this.pieceAchat = pieceAchat;
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
    public YvsComptaContentJournalEtapeAcompteAchat getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalEtapeAcompteAchat pieceContenu) {
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
        if (!(object instanceof YvsComptaPhaseAcompteAchat)) {
            return false;
        }
        YvsComptaPhaseAcompteAchat other = (YvsComptaPhaseAcompteAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.achat.YvsComptaPhaseAcompteAchat[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsComptaPhaseAcompteAchat o1, YvsComptaPhaseAcompteAchat o2) {
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

    public Character getStatut() {
        return statut;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public List<YvsComptaContentJournalEtapeAcompteAchat> getYvsComptaContentJournalEtapeAcompteAchatList() {
        return yvsComptaContentJournalEtapeAcompteAchatList;
    }

    public void setYvsComptaContentJournalEtapeAcompteAchatList(List<YvsComptaContentJournalEtapeAcompteAchat> yvsComptaContentJournalEtapeAcompteAchatList) {
        this.yvsComptaContentJournalEtapeAcompteAchatList = yvsComptaContentJournalEtapeAcompteAchatList;
    }

}
