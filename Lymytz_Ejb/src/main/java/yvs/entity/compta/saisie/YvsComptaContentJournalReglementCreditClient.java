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
import yvs.entity.compta.YvsComptaReglementCreditClient;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_content_journal_reglement_credit_client")
@NamedQueries({
    @NamedQuery(name = "YvsComptaContentJournalReglementCreditClient.findAll", query = "SELECT y FROM YvsComptaContentJournalReglementCreditClient y"),
    @NamedQuery(name = "YvsComptaContentJournalReglementCreditClient.findById", query = "SELECT y FROM YvsComptaContentJournalReglementCreditClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaContentJournalReglementCreditClient.findByDateUpdate", query = "SELECT y FROM YvsComptaContentJournalReglementCreditClient y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaContentJournalReglementCreditClient.findByDateSave", query = "SELECT y FROM YvsComptaContentJournalReglementCreditClient y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaContentJournalReglementCreditClient.findByReglement", query = "SELECT y FROM YvsComptaContentJournalReglementCreditClient y WHERE y.reglement = :reglement"),
    @NamedQuery(name = "YvsComptaContentJournalReglementCreditClient.findByReglements", query = "SELECT y FROM YvsComptaContentJournalReglementCreditClient y WHERE y.reglement.id IN :ids")})
public class YvsComptaContentJournalReglementCreditClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_content_journal_reglement_credit_client_id_seq", name = "yvs_compta_content_journal_reglement_credit_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_content_journal_reglement_credit_client_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "reglement", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaReglementCreditClient reglement;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaPiecesComptable piece;

    public YvsComptaContentJournalReglementCreditClient() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaContentJournalReglementCreditClient(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaContentJournalReglementCreditClient(YvsComptaReglementCreditClient reglement, YvsComptaPiecesComptable piece) {
        this();
        this.reglement = reglement;
        this.piece = piece;
    }

    public Long getId() {
        return id != null ? id : 0;
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

    public YvsComptaReglementCreditClient getReglement() {
        return reglement;
    }

    public void setReglement(YvsComptaReglementCreditClient reglement) {
        this.reglement = reglement;
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
        if (!(object instanceof YvsComptaContentJournalReglementCreditClient)) {
            return false;
        }
        YvsComptaContentJournalReglementCreditClient other = (YvsComptaContentJournalReglementCreditClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.saisie.YvsComptaContentJournalReglementCreditClient[ id=" + id + " ]";
    }

}
