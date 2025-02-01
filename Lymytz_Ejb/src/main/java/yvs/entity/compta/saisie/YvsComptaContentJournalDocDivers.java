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
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_content_journal_doc_divers")
@NamedQueries({
    @NamedQuery(name = "YvsComptaContentJournalDocDivers.findAll", query = "SELECT y FROM YvsComptaContentJournalDocDivers y"),
    @NamedQuery(name = "YvsComptaContentJournalDocDivers.findById", query = "SELECT y FROM YvsComptaContentJournalDocDivers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaContentJournalDocDivers.findByIdByRefPiece", query = "SELECT y.piece.id FROM YvsComptaContentJournalDocDivers y WHERE UPPER(y.divers.numPiece) LIKE :numero AND y.piece.journal.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaContentJournalDocDivers.findByDateUpdate", query = "SELECT y FROM YvsComptaContentJournalDocDivers y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaContentJournalDocDivers.findByDateSave", query = "SELECT y FROM YvsComptaContentJournalDocDivers y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaContentJournalDocDivers.findByDocDivers", query = "SELECT y FROM YvsComptaContentJournalDocDivers y WHERE y.divers = :divers"),
    @NamedQuery(name = "YvsComptaContentJournalDocDivers.findByDocDiverss", query = "SELECT y FROM YvsComptaContentJournalDocDivers y WHERE y.divers.id IN :ids"),
    @NamedQuery(name = "YvsComptaContentJournalDocDivers.findIdByDocDivers", query = "SELECT y.id FROM YvsComptaContentJournalDocDivers y WHERE y.divers = :divers"),
    @NamedQuery(name = "YvsComptaContentJournalDocDivers.findDocDivers", query = "SELECT DISTINCT y.divers.id FROM YvsComptaContentJournalDocDivers y WHERE y.piece.journal.agence.societe = :societe")})
public class YvsComptaContentJournalDocDivers implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_content_journal_doc_divers_id_seq", name = "yvs_compta_content_journal_doc_divers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_content_journal_doc_divers_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "divers", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaCaisseDocDivers divers;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaPiecesComptable piece;

    public YvsComptaContentJournalDocDivers() {
        dateSave = new Date();
        dateUpdate = new Date();
    }

    public YvsComptaContentJournalDocDivers(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaContentJournalDocDivers(YvsComptaCaisseDocDivers facture, YvsComptaPiecesComptable piece) {
        this();
        this.divers = facture;
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

    public YvsComptaCaisseDocDivers getDivers() {
        return divers;
    }

    public void setDivers(YvsComptaCaisseDocDivers divers) {
        this.divers = divers;
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
        if (!(object instanceof YvsComptaContentJournalDocDivers)) {
            return false;
        }
        YvsComptaContentJournalDocDivers other = (YvsComptaContentJournalDocDivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.saisie.YvsComptaContentJournalDocDivers[ id=" + id + " ]";
    }
    
}
