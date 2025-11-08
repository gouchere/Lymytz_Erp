/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.divers;

import java.io.Serializable;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_justificatif_bon")
@NamedQueries({
    @NamedQuery(name = "YvsComptaJustificatifBon.findAll", query = "SELECT y FROM YvsComptaJustificatifBon y"),
    @NamedQuery(name = "YvsComptaJustificatifBon.findById", query = "SELECT y FROM YvsComptaJustificatifBon y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaJustificatifBon.findOne", query = "SELECT y FROM YvsComptaJustificatifBon y WHERE y.bon = :bon AND y.piece = :piece"),
    @NamedQuery(name = "YvsComptaJustificatifBon.findByBon", query = "SELECT y FROM YvsComptaJustificatifBon y WHERE y.bon = :bon"),
    @NamedQuery(name = "YvsComptaJustificatifBon.findByPiece", query = "SELECT y FROM YvsComptaJustificatifBon y WHERE y.piece = :piece"),
    @NamedQuery(name = "YvsComptaJustificatifBon.findByDateSave", query = "SELECT y FROM YvsComptaJustificatifBon y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaJustificatifBon.findByDateUpdate", query = "SELECT y FROM YvsComptaJustificatifBon y WHERE y.dateUpdate = :dateUpdate"),

    @NamedQuery(name = "YvsComptaJustificatifBon.findPieceBybon", query = "SELECT y.piece FROM YvsComptaJustificatifBon y WHERE y.bon = :bon")})
public class YvsComptaJustificatifBon implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_justificatif_bon_id_seq", name = "yvs_compta_justificatif_bon_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_justificatif_bon_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name="date_justification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateJustification;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    @JoinColumn(name = "bon", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaBonProvisoire bon;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceDivers piece;
 
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;

    public YvsComptaJustificatifBon() {
        dateUpdate = new Date();
        dateSave = new Date();
    }

    public YvsComptaJustificatifBon(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaJustificatifBon(YvsComptaBonProvisoire bon, YvsComptaCaissePieceDivers piece) {
        this();
        this.bon = bon;
        this.piece = piece;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateJustification() {
        return dateJustification;
    }

    public void setDateJustification(Date dateJustification) {
        this.dateJustification = dateJustification;
    }
    
    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComptaBonProvisoire getBon() {
        return bon;
    }

    public void setBon(YvsComptaBonProvisoire bon) {
        this.bon = bon;
    }

    public YvsComptaCaissePieceDivers getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaCaissePieceDivers piece) {
        this.piece = piece;
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
        if (!(object instanceof YvsComptaJustificatifBon)) {
            return false;
        }
        YvsComptaJustificatifBon other = (YvsComptaJustificatifBon) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.divers.YvsComptaJustificatifBon[ id=" + id + " ]";
    }

}
