/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_notif_reglement_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComptaNotifReglementVente.findAll", query = "SELECT y FROM YvsComptaNotifReglementVente y"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findById", query = "SELECT y FROM YvsComptaNotifReglementVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findIdPiece", query = "SELECT DISTINCT y.pieceVente.id FROM YvsComptaNotifReglementVente y WHERE y.acompte.model.typeReglement = :typeReglement AND y.acompte.caisse.journal.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findOne", query = "SELECT y FROM YvsComptaNotifReglementVente y WHERE y.pieceVente = :piece AND y.acompte = :acompte"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findAcompte", query = "SELECT y FROM YvsComptaNotifReglementVente y WHERE y.pieceVente = :piece"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findSumByAcompte", query = "SELECT SUM(y.pieceVente.montant) FROM YvsComptaNotifReglementVente y WHERE y.acompte = :acompte AND y.pieceVente.statutPiece = :statut"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findSumByClient", query = "SELECT SUM(y.pieceVente.montant) FROM YvsComptaNotifReglementVente y WHERE y.acompte.client = :client AND y.pieceVente.statutPiece = :statut"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findSumByClientDates", query = "SELECT SUM(y.pieceVente.montant) FROM YvsComptaNotifReglementVente y WHERE y.acompte.client = :client AND y.pieceVente.statutPiece = 'P' AND y.pieceVente.datePaiement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findSumByClientDatesAgence", query = "SELECT SUM(y.pieceVente.montant) FROM YvsComptaNotifReglementVente y WHERE y.acompte.caisse.journal.agence = :agence AND y.acompte.client = :client AND y.pieceVente.statutPiece = 'P' AND y.pieceVente.datePaiement BETWEEN :dateDebut AND :dateFin"),
    
    @NamedQuery(name = "YvsComptaNotifReglementVente.findResteForAcompte", query = "SELECT y.acompte.montant - SUM(y.pieceVente.montant) FROM YvsComptaNotifReglementVente y WHERE y.acompte = :acompte AND y.pieceVente.statutPiece = 'P' GROUP BY y.acompte.id"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findResteUnBindForAcompte", query = "SELECT y.acompte.montant - SUM(y.pieceVente.montant) FROM YvsComptaNotifReglementVente y WHERE y.acompte = :acompte GROUP BY y.acompte.id"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findResteUnBindForAcompteAndNotPiece", query = "SELECT y.acompte.montant - SUM(y.pieceVente.montant) FROM YvsComptaNotifReglementVente y WHERE y.acompte = :acompte AND y.pieceVente.id != :piece GROUP BY y.acompte.id"),

    @NamedQuery(name = "YvsComptaNotifReglementVente.findPieceByAcompte", query = "SELECT DISTINCT y.pieceVente FROM YvsComptaNotifReglementVente y WHERE y.acompte = :acompte"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findByAcompte", query = "SELECT y FROM YvsComptaNotifReglementVente y JOIN FETCH y.pieceVente JOIN FETCH y.pieceVente.vente WHERE y.acompte = :acompte"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findPieceNotComptabiliseByAcompte", query = "SELECT DISTINCT y.pieceVente FROM YvsComptaNotifReglementVente y WHERE y.acompte = :acompte AND (y.pieceVente.comptabilise = FALSE OR y.pieceVente.comptabilise = NULL)"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findPieceComptabiliseByAcompte", query = "SELECT DISTINCT y.pieceVente FROM YvsComptaNotifReglementVente y WHERE y.acompte = :acompte AND y.pieceVente.comptabilise = TRUE"),
    
    @NamedQuery(name = "YvsComptaNotifReglementVente.findIdsPieceByDates", query = "SELECT DISTINCT y.pieceVente.id FROM YvsComptaNotifReglementVente y WHERE y.acompte.client.tiers.societe = :societe AND ((y.pieceVente.datePaiement BETWEEN :dateDebut AND :dateFin) OR (y.pieceVente.datePiece BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findIdsPiece", query = "SELECT DISTINCT y.pieceVente.id FROM YvsComptaNotifReglementVente y WHERE y.acompte.client.tiers.societe = :societe"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findIdPieceByAcompte", query = "SELECT DISTINCT y.pieceVente.id FROM YvsComptaNotifReglementVente y WHERE y.acompte = :acompte"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findIdFactureByAcompte", query = "SELECT DISTINCT y.pieceVente.vente.id FROM YvsComptaNotifReglementVente y WHERE y.acompte = :acompte"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.findAcompteByFactureNature", query = "SELECT DISTINCT y.acompte FROM YvsComptaNotifReglementVente y WHERE y.pieceVente.vente = :facture AND y.acompte.nature = :nature"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.countByFactureNature", query = "SELECT COUNT(y) FROM YvsComptaNotifReglementVente y WHERE y.pieceVente.vente = :facture AND y.acompte.nature = :nature"),
    @NamedQuery(name = "YvsComptaNotifReglementVente.countByFacture", query = "SELECT COUNT(y) FROM YvsComptaNotifReglementVente y WHERE y.pieceVente.vente = :facture")})
public class YvsComptaNotifReglementVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_notif_reglement_vente_id_seq", name = "yvs_compta_notif_reglement_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_notif_reglement_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    
    @JoinColumn(name = "piece_vente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceVente pieceVente;
    @JoinColumn(name = "acompte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaAcompteClient acompte;
        
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComptaNotifReglementVente() {
    }

    public YvsComptaNotifReglementVente(Long id) {
        this.id = id;
    }

    public YvsComptaNotifReglementVente(YvsComptaCaissePieceVente pieceVente, YvsComptaAcompteClient acompte, YvsUsersAgence author) {
        this.author = author;
        this.pieceVente = pieceVente;
        this.acompte = acompte;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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

    public YvsComptaCaissePieceVente getPieceVente() {
        return pieceVente;
    }

    public void setPieceVente(YvsComptaCaissePieceVente pieceVente) {
        this.pieceVente = pieceVente;
    }

    public YvsComptaAcompteClient getAcompte() {
        return acompte;
    }

    public void setAcompte(YvsComptaAcompteClient acompte) {
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
        if (!(object instanceof YvsComptaNotifReglementVente)) {
            return false;
        }
        YvsComptaNotifReglementVente other = (YvsComptaNotifReglementVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaNotifReglementVente[ id=" + id + " ]";
    }

}
