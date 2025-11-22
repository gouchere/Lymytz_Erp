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
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_justif_bon_achat")
@NamedQueries({
    @NamedQuery(name = "YvsComptaJustifBonAchat.findAll", query = "SELECT y FROM YvsComptaJustifBonAchat y"),
    @NamedQuery(name = "YvsComptaJustifBonAchat.findById", query = "SELECT y FROM YvsComptaJustifBonAchat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaJustifBonAchat.findOne", query = "SELECT y FROM YvsComptaJustifBonAchat y WHERE y.bon = :bon AND y.piece = :piece"),
    @NamedQuery(name = "YvsComptaJustifBonAchat.findByDateSave", query = "SELECT y FROM YvsComptaJustifBonAchat y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaJustifBonAchat.findByDateUpdate", query = "SELECT y FROM YvsComptaJustifBonAchat y WHERE y.dateUpdate = :dateUpdate")})
public class YvsComptaJustifBonAchat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_justif_bon_achat_id_seq", name = "yvs_compta_justif_bon_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_justif_bon_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaCaissePieceAchat piece;
    @JoinColumn(name = "bon", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaBonProvisoire bon;

    public YvsComptaJustifBonAchat() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaJustifBonAchat(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaJustifBonAchat(YvsComptaCaissePieceAchat piece, YvsComptaBonProvisoire bon) {
        this();
        this.piece = piece;
        this.bon = bon;
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
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
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

    public YvsComptaCaissePieceAchat getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaCaissePieceAchat piece) {
        this.piece = piece;
    }

    public YvsComptaBonProvisoire getBon() {
        return bon;
    }

    public void setBon(YvsComptaBonProvisoire bon) {
        this.bon = bon;
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
        if (!(object instanceof YvsComptaJustifBonAchat)) {
            return false;
        }
        YvsComptaJustifBonAchat other = (YvsComptaJustifBonAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.divers.YvsComptaJustifBonAchat[ id=" + id + " ]";
    }

}
