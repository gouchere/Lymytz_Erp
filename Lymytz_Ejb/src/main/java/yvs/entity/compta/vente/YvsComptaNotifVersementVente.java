/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.vente;

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
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_notif_versement_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComptaNotifVersementVente.findAll", query = "SELECT y FROM YvsComptaNotifVersementVente y"),
    @NamedQuery(name = "YvsComptaNotifVersementVente.findById", query = "SELECT y FROM YvsComptaNotifVersementVente y WHERE y.id = :id"),    
    @NamedQuery(name = "YvsComptaNotifVersementVente.findOne", query = "SELECT y FROM YvsComptaNotifVersementVente y WHERE y.piece = :piece AND y.enteteDoc = :enteteDoc"),
    @NamedQuery(name = "YvsComptaNotifVersementVente.findByHeader", query = "SELECT y FROM YvsComptaNotifVersementVente y WHERE y.enteteDoc = :enteteDoc"),    
    @NamedQuery(name = "YvsComptaNotifVersementVente.findSumByHeader", query = "SELECT SUM(y.piece.montant) FROM YvsComptaNotifVersementVente y WHERE y.enteteDoc = :enteteDoc"),
    @NamedQuery(name = "YvsComptaNotifVersementVente.findSumByHeaderStatut", query = "SELECT SUM(y.piece.montant) FROM YvsComptaNotifVersementVente y WHERE y.enteteDoc = :enteteDoc AND y.piece.statutPiece = :statut"),
    
    @NamedQuery(name = "YvsComptaNotifVersementVente.findVirementByHeader", query = "SELECT DISTINCT y.piece FROM YvsComptaNotifVersementVente y WHERE y.enteteDoc = :enteteDoc"),
    @NamedQuery(name = "YvsComptaNotifVersementVente.findIdPiece", query = "SELECT DISTINCT y.piece.id FROM YvsComptaNotifVersementVente y WHERE y.enteteDoc.creneau.users.agence.societe = :societe")})
public class YvsComptaNotifVersementVente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_notif_versement_vente_id_seq", name = "yvs_compta_notif_versement_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_notif_versement_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "entete_doc", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComEnteteDocVente enteteDoc;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceVirement piece;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComptaNotifVersementVente() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaNotifVersementVente(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaNotifVersementVente(YvsComEnteteDocVente enteteDoc, YvsComptaCaissePieceVirement piece, YvsUsersAgence author) {
        this();
        this.author = author;
        this.enteteDoc = enteteDoc;
        this.piece = piece;
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComEnteteDocVente getEnteteDoc() {
        return enteteDoc;
    }

    public void setEnteteDoc(YvsComEnteteDocVente enteteDoc) {
        this.enteteDoc = enteteDoc;
    }

    public YvsComptaCaissePieceVirement getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaCaissePieceVirement piece) {
        this.piece = piece;
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
        if (!(object instanceof YvsComptaNotifVersementVente)) {
            return false;
        }
        YvsComptaNotifVersementVente other = (YvsComptaNotifVersementVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.vente.YvsComptaNotifVersementVente[ id=" + id + " ]";
    }

}
