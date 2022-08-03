/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.compta.saisie;

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
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_content_journal_bulletin")
@NamedQueries({
    @NamedQuery(name = "YvsComptaContentJournalBulletin.findAll", query = "SELECT y FROM YvsComptaContentJournalBulletin y"),
    @NamedQuery(name = "YvsComptaContentJournalBulletin.findById", query = "SELECT y FROM YvsComptaContentJournalBulletin y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaContentJournalBulletin.findByDateUpdate", query = "SELECT y FROM YvsComptaContentJournalBulletin y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaContentJournalBulletin.findByDateSave", query = "SELECT y FROM YvsComptaContentJournalBulletin y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaContentJournalBulletin.findIdByBulletin", query = "SELECT y.id FROM YvsComptaContentJournalBulletin y WHERE y.bulletin = :bulletin"),
    
    @NamedQuery(name = "YvsComptaContentJournalBulletin.findByBulletin", query = "SELECT y FROM YvsComptaContentJournalBulletin y WHERE y.bulletin = :bulletin"),
    @NamedQuery(name = "YvsComptaContentJournalBulletin.findByBulletins", query = "SELECT y FROM YvsComptaContentJournalBulletin y WHERE y.bulletin.id IN :ids")})
public class YvsComptaContentJournalBulletin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_content_journal_bulletin_id_seq", name = "yvs_compta_content_journal_bulletin_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_content_journal_bulletin_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "bulletin", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhBulletins bulletin;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaPiecesComptable piece;

    public YvsComptaContentJournalBulletin() {
        dateSave = new Date();
        dateUpdate = new Date();
    }

    public YvsComptaContentJournalBulletin(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaContentJournalBulletin(YvsGrhBulletins bulletin, YvsComptaPiecesComptable piece) {
        this();
        this.bulletin = bulletin;
        this.piece = piece;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public YvsGrhBulletins getBulletin() {
        return bulletin;
    }

    public void setBulletin(YvsGrhBulletins bulletin) {
        this.bulletin = bulletin;
    }

    public YvsComptaPiecesComptable getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaPiecesComptable piece) {
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
        if (!(object instanceof YvsComptaContentJournalBulletin)) {
            return false;
        }
        YvsComptaContentJournalBulletin other = (YvsComptaContentJournalBulletin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.saisie.YvsComptaContentJournalBulletin[ id=" + id + " ]";
    }
    
}
