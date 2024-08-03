/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.achat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
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
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_notif_reglement_achat")
@NamedQueries({
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findAll", query = "SELECT y FROM YvsComptaNotifReglementAchat y"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findById", query = "SELECT y FROM YvsComptaNotifReglementAchat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findByDateUpdate", query = "SELECT y FROM YvsComptaNotifReglementAchat y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findByDateSave", query = "SELECT y FROM YvsComptaNotifReglementAchat y WHERE y.dateSave = :dateSave"),    
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findIdPiece", query = "SELECT DISTINCT y.pieceAchat.id FROM YvsComptaNotifReglementAchat y WHERE y.acompte.model.typeReglement = :typeReglement AND y.acompte.caisse.journal.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findOne", query = "SELECT y FROM YvsComptaNotifReglementAchat y WHERE y.pieceAchat = :piece AND y.acompte = :acompte"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findAcompte", query = "SELECT y FROM YvsComptaNotifReglementAchat y WHERE y.pieceAchat = :piece"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findByAcompte", query = "SELECT y FROM YvsComptaNotifReglementAchat y WHERE y.acompte = :acompte"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findSumByAcompte", query = "SELECT SUM(y.pieceAchat.montant) FROM YvsComptaNotifReglementAchat y WHERE y.acompte = :acompte AND y.pieceAchat.statutPiece = :statut"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findSumByFournisseur", query = "SELECT SUM(y.pieceAchat.montant) FROM YvsComptaNotifReglementAchat y WHERE y.acompte.fournisseur = :fournisseur AND y.pieceAchat.statutPiece = 'P'"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findSumByFournisseurDates", query = "SELECT SUM(y.pieceAchat.montant) FROM YvsComptaNotifReglementAchat y WHERE y.acompte.fournisseur = :fournisseur AND y.pieceAchat.statutPiece = 'P' AND y.pieceAchat.datePaiement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findSumByFournisseurDatesAgence", query = "SELECT SUM(y.pieceAchat.montant) FROM YvsComptaNotifReglementAchat y WHERE y.acompte.caisse.journal.agence = :agence AND y.acompte.fournisseur = :fournisseur AND y.pieceAchat.statutPiece = 'P' AND y.pieceAchat.datePaiement BETWEEN :dateDebut AND :dateFin"),
    
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findResteForAcompte", query = "SELECT y.acompte.montant - SUM(y.pieceAchat.montant) FROM YvsComptaNotifReglementAchat y WHERE y.acompte = :acompte AND y.pieceAchat.statutPiece = 'P' GROUP BY y.acompte.id"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findResteUnBindForAcompte", query = "SELECT y.acompte.montant - SUM(y.pieceAchat.montant) FROM YvsComptaNotifReglementAchat y WHERE y.acompte = :acompte GROUP BY y.acompte.id"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findResteUnBindForAcompteAndNotPiece", query = "SELECT y.acompte.montant - SUM(y.pieceAchat.montant) FROM YvsComptaNotifReglementAchat y WHERE y.acompte = :acompte AND y.pieceAchat.id != :piece GROUP BY y.acompte.id"),

    @NamedQuery(name = "YvsComptaNotifReglementAchat.findIdsPieceByDates", query = "SELECT DISTINCT y.pieceAchat.id FROM YvsComptaNotifReglementAchat y WHERE y.acompte.fournisseur.tiers.societe = :societe AND ((y.pieceAchat.datePaiement BETWEEN :dateDebut AND :dateFin) OR (y.pieceAchat.datePiece BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findIdsPiece", query = "SELECT DISTINCT y.pieceAchat.id FROM YvsComptaNotifReglementAchat y WHERE y.acompte.fournisseur.tiers.societe = :societe"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findIdFactureByAcompte", query = "SELECT DISTINCT y.pieceAchat.achat.id FROM YvsComptaNotifReglementAchat y WHERE y.acompte = :acompte"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findAcompteByFactureNature", query = "SELECT DISTINCT y.acompte FROM YvsComptaNotifReglementAchat y WHERE y.pieceAchat.achat = :facture AND y.acompte.nature = :nature"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.countByFactureNature", query = "SELECT COUNT(y) FROM YvsComptaNotifReglementAchat y WHERE y.pieceAchat.achat = :facture AND y.acompte.nature = :nature"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.countByFacture", query = "SELECT COUNT(y) FROM YvsComptaNotifReglementAchat y WHERE y.pieceAchat.achat = :facture"),
    
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findPieceComptabiliseByAcompte", query = "SELECT DISTINCT y.pieceAchat FROM YvsComptaNotifReglementAchat y WHERE y.acompte = :acompte AND y.pieceAchat.comptabilise = TRUE"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findPieceNotComptabiliseByAcompte", query = "SELECT DISTINCT y.pieceAchat FROM YvsComptaNotifReglementAchat y WHERE y.acompte = :acompte AND (y.pieceAchat.comptabilise = FALSE OR y.pieceAchat.comptabilise = NULL)"),
    @NamedQuery(name = "YvsComptaNotifReglementAchat.findPieceByStatutAcompte", query = "SELECT DISTINCT y.pieceAchat FROM YvsComptaNotifReglementAchat y WHERE y.acompte = :acompte AND y.pieceAchat.statutPiece = :statut")})
public class YvsComptaNotifReglementAchat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_notif_reglement_achat_id_seq", name = "yvs_compta_notif_reglement_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_notif_reglement_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "piece_achat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceAchat pieceAchat;
    @JoinColumn(name = "acompte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaAcompteFournisseur acompte;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComptaNotifReglementAchat() {
    }

    public YvsComptaNotifReglementAchat(Long id) {
        this.id = id;
    }

    public YvsComptaNotifReglementAchat(YvsComptaCaissePieceAchat pieceAchat, YvsComptaAcompteFournisseur acompte, YvsUsersAgence author) {
        this.author = author;
        this.pieceAchat = pieceAchat;
        this.acompte = acompte;
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

    public YvsComptaCaissePieceAchat getPieceAchat() {
        return pieceAchat;
    }

    public void setPieceAchat(YvsComptaCaissePieceAchat pieceAchat) {
        this.pieceAchat = pieceAchat;
    }

    public YvsComptaAcompteFournisseur getAcompte() {
        return acompte;
    }

    public void setAcompte(YvsComptaAcompteFournisseur acompte) {
        this.acompte = acompte;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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
        if (!(object instanceof YvsComptaNotifReglementAchat)) {
            return false;
        }
        YvsComptaNotifReglementAchat other = (YvsComptaNotifReglementAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.achat.YvsComptaNotifReglementAchat[ id=" + id + " ]";
    }

}
