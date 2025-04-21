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
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_content_journal_acompte_client")
@NamedQueries({
    @NamedQuery(name = "YvsComptaContentJournalAcompteClient.findAll", query = "SELECT y FROM YvsComptaContentJournalAcompteClient y"),
    @NamedQuery(name = "YvsComptaContentJournalAcompteClient.findById", query = "SELECT y FROM YvsComptaContentJournalAcompteClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaContentJournalAcompteClient.findByIdByRefPiece", query = "SELECT y.piece.id FROM YvsComptaContentJournalAcompteClient y WHERE UPPER(y.acompte.numRefrence) LIKE :numero AND y.piece.journal.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaContentJournalAcompteClient.findByDateUpdate", query = "SELECT y FROM YvsComptaContentJournalAcompteClient y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaContentJournalAcompteClient.findByDateSave", query = "SELECT y FROM YvsComptaContentJournalAcompteClient y WHERE y.dateSave = :dateSave"),
    
    @NamedQuery(name = "YvsComptaContentJournalAcompteClient.findByAcompte", query = "SELECT y FROM YvsComptaContentJournalAcompteClient y WHERE y.acompte = :acompte"),
    @NamedQuery(name = "YvsComptaContentJournalAcompteClient.findByAcomptes", query = "SELECT y FROM YvsComptaContentJournalAcompteClient y WHERE y.acompte.id IN :ids"),
    @NamedQuery(name = "YvsComptaContentJournalAcompteClient.findAcompte", query = "SELECT DISTINCT y.acompte.id FROM YvsComptaContentJournalAcompteClient y WHERE y.piece.journal.agence.societe = :societe")})
public class YvsComptaContentJournalAcompteClient implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_content_journal_acompte_client_id_seq", name = "yvs_compta_content_journal_acompte_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_content_journal_acompte_client_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaPiecesComptable piece;
    @JoinColumn(name = "acompte", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaAcompteClient acompte;

    public YvsComptaContentJournalAcompteClient() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaContentJournalAcompteClient(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaContentJournalAcompteClient(YvsComptaAcompteClient acompte, YvsComptaPiecesComptable piece) {
        this();
        this.piece = piece;
        this.acompte = acompte;
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

    public YvsComptaPiecesComptable getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaPiecesComptable piece) {
        this.piece = piece;
    }

    public YvsComptaAcompteClient getAcompte() {
        return acompte;
    }

    public void setAcompte(YvsComptaAcompteClient acompte) {
        this.acompte = acompte;
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
        if (!(object instanceof YvsComptaContentJournalAcompteClient)) {
            return false;
        }
        YvsComptaContentJournalAcompteClient other = (YvsComptaContentJournalAcompteClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.saisie.YvsComptaContentJournalAcompteClient[ id=" + id + " ]";
    }
    
}
