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
import yvs.dao.YvsEntity;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_content_journal_facture_achat")
@NamedQueries({
    @NamedQuery(name = "YvsComptaContentJournalFactureAchat.findAll", query = "SELECT y FROM YvsComptaContentJournalFactureAchat y"),
    @NamedQuery(name = "YvsComptaContentJournalFactureAchat.findById", query = "SELECT y FROM YvsComptaContentJournalFactureAchat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaContentJournalFactureAchat.findByIdByRefPiece", query = "SELECT y.piece.id FROM YvsComptaContentJournalFactureAchat y WHERE UPPER(y.facture.numDoc) LIKE :numero AND y.piece.journal.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaContentJournalFactureAchat.findByDateUpdate", query = "SELECT y FROM YvsComptaContentJournalFactureAchat y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaContentJournalFactureAchat.findByDateSave", query = "SELECT y FROM YvsComptaContentJournalFactureAchat y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaContentJournalFactureAchat.findByFacture", query = "SELECT y FROM YvsComptaContentJournalFactureAchat y WHERE y.facture = :facture"),
    @NamedQuery(name = "YvsComptaContentJournalFactureAchat.findByFactures", query = "SELECT y FROM YvsComptaContentJournalFactureAchat y WHERE y.facture.id IN :ids"),
    @NamedQuery(name = "YvsComptaContentJournalFactureAchat.findIdByFacture", query = "SELECT y.id FROM YvsComptaContentJournalFactureAchat y WHERE y.facture = :facture"),
    @NamedQuery(name = "YvsComptaContentJournalFactureAchat.findFacture", query = "SELECT DISTINCT y.facture.id FROM YvsComptaContentJournalFactureAchat y WHERE y.piece.journal.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaContentJournalFactureAchat.findFactureNotIds", query = "SELECT DISTINCT y.facture.id FROM YvsComptaContentJournalFactureAchat y WHERE y.piece.journal.agence.societe = :societe AND y.facture.id NOT IN :ids")})
public class YvsComptaContentJournalFactureAchat extends YvsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_content_journal_facture_achat_id_seq", name = "yvs_compta_content_journal_facture_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_content_journal_facture_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "facture", referencedColumnName = "id")
    @ManyToOne
    private YvsComDocAchats facture;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaPiecesComptable piece;

    public YvsComptaContentJournalFactureAchat() {
        dateSave = new Date();
        dateUpdate = new Date();
    }

    public YvsComptaContentJournalFactureAchat(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaContentJournalFactureAchat(YvsComDocAchats facture, YvsComptaPiecesComptable piece) {
        this();
        this.facture = facture;
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

    public YvsComDocAchats getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocAchats facture) {
        this.facture = facture;
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
        if (!(object instanceof YvsComptaContentJournalFactureAchat)) {
            return false;
        }
        YvsComptaContentJournalFactureAchat other = (YvsComptaContentJournalFactureAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.saisie.YvsComptaContentJournalFactureAchat[ id=" + id + " ]";
    }
    
}
