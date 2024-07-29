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
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_content_journal_piece_virement")
@NamedQueries({
    @NamedQuery(name = "YvsComptaContentJournalPieceVirement.findAll", query = "SELECT y FROM YvsComptaContentJournalPieceVirement y"),
    @NamedQuery(name = "YvsComptaContentJournalPieceVirement.findById", query = "SELECT y FROM YvsComptaContentJournalPieceVirement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaContentJournalPieceVirement.findByIdByRefPiece", query = "SELECT y.piece.id FROM YvsComptaContentJournalPieceVirement y WHERE UPPER(y.reglement.numeroPiece) LIKE :numero AND y.reglement.source.journal.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaContentJournalPieceVirement.findByDateUpdate", query = "SELECT y FROM YvsComptaContentJournalPieceVirement y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaContentJournalPieceVirement.findByDateSave", query = "SELECT y FROM YvsComptaContentJournalPieceVirement y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaContentJournalPieceVirement.findByFacture", query = "SELECT y FROM YvsComptaContentJournalPieceVirement y WHERE y.reglement = :reglement"),
    @NamedQuery(name = "YvsComptaContentJournalPieceVirement.findByFactures", query = "SELECT y FROM YvsComptaContentJournalPieceVirement y WHERE y.reglement.id IN :ids"),
    @NamedQuery(name = "YvsComptaContentJournalPieceVirement.findByFacturesSource", query = "SELECT y FROM YvsComptaContentJournalPieceVirement y WHERE y.reglement.id IN :ids AND (y.sensCompta=:source OR y.sensCompta='B')"),
    @NamedQuery(name = "YvsComptaContentJournalPieceVirement.findByFactureSource", query = "SELECT y FROM YvsComptaContentJournalPieceVirement y WHERE y.reglement = :reglement AND y.sensCompta=:source"),
    @NamedQuery(name = "YvsComptaContentJournalPieceVirement.findIdByFacture", query = "SELECT y.id FROM YvsComptaContentJournalPieceVirement y WHERE y.reglement = :reglement"),
    @NamedQuery(name = "YvsComptaContentJournalPieceVirement.findVirement", query = "SELECT DISTINCT y.reglement.id FROM YvsComptaContentJournalPieceVirement y WHERE y.piece.journal.agence.societe = :societe")})
public class YvsComptaContentJournalPieceVirement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_content_journal_piece_virement_id_seq", name = "yvs_compta_content_journal_piece_virement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_content_journal_piece_virement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "sens_compta")
    private Character sensCompta; // Précise le sens de comptabilisation D=depense, R=recette B=Les deux sens (veleur avant la mise à jour)
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
    private YvsComptaCaissePieceVirement reglement;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaPiecesComptable piece;

    public YvsComptaContentJournalPieceVirement() {
        dateSave = new Date();
        dateUpdate = new Date();
    }

    public YvsComptaContentJournalPieceVirement(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaContentJournalPieceVirement(YvsComptaCaissePieceVirement reglement, YvsComptaPiecesComptable piece) {
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

    public YvsComptaCaissePieceVirement getReglement() {
        return reglement;
    }

    public void setReglement(YvsComptaCaissePieceVirement reglement) {
        this.reglement = reglement;
    }

    public YvsComptaPiecesComptable getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaPiecesComptable piece) {
        this.piece = piece;
    }

    public Character getSensCompta() {
        return sensCompta;
    }

    public void setSensCompta(Character sensCompta) {
        this.sensCompta = sensCompta;
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
        if (!(object instanceof YvsComptaContentJournalPieceVirement)) {
            return false;
        }
        YvsComptaContentJournalPieceVirement other = (YvsComptaContentJournalPieceVirement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.saisie.YvsComptaContentJournalPieceVirement[ id=" + id + " ]";
    }

}
