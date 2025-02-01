/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz-pc
 */
@Entity
@Table(name = "yvs_compta_notif_reglement_doc_divers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findAll", query = "SELECT y FROM YvsComptaNotifReglementDocDivers y"),
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findById", query = "SELECT y FROM YvsComptaNotifReglementDocDivers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findByDateSave", query = "SELECT y FROM YvsComptaNotifReglementDocDivers y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findOne", query = "SELECT y FROM YvsComptaNotifReglementDocDivers y WHERE y.pieceDocDivers = :piece AND y.acompteClient = :acompte"),
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findByAcompte", query = "SELECT y FROM YvsComptaNotifReglementDocDivers y JOIN FETCH y.pieceDocDivers JOIN FETCH y.pieceDocDivers.docDivers WHERE y.acompteClient = :acompte"),
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findOne_fa", query = "SELECT y FROM YvsComptaNotifReglementDocDivers y WHERE y.pieceDocDivers = :piece AND y.acompteFournisseur = :acompte"),

    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findByDateUpdate", query = "SELECT y FROM YvsComptaNotifReglementDocDivers y WHERE y.dateUpdate = :dateUpdate"),
    
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findPieceComptabiliseByAcompteAchat", query = "SELECT DISTINCT y.pieceDocDivers FROM YvsComptaNotifReglementDocDivers y WHERE y.acompteFournisseur = :acompte AND y.pieceDocDivers.comptabilise = TRUE"),
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findPieceComptabiliseByAcompteVente", query = "SELECT DISTINCT y.pieceDocDivers FROM YvsComptaNotifReglementDocDivers y WHERE y.acompteClient = :acompte AND y.pieceDocDivers.comptabilise = TRUE"),
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findPieceNotComptabiliseByAcompteAchat", query = "SELECT DISTINCT y.pieceDocDivers FROM YvsComptaNotifReglementDocDivers y WHERE y.acompteFournisseur = :acompte AND (y.pieceDocDivers.comptabilise = FALSE OR y.pieceDocDivers.comptabilise = NULL)"),
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findPieceNotComptabiliseByAcompteVente", query = "SELECT DISTINCT y.pieceDocDivers FROM YvsComptaNotifReglementDocDivers y WHERE y.acompteClient = :acompte AND (y.pieceDocDivers.comptabilise = FALSE OR y.pieceDocDivers.comptabilise = NULL)"),
    
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findPieceByStatutAcompteAchat", query = "SELECT DISTINCT y.pieceDocDivers FROM YvsComptaNotifReglementDocDivers y WHERE y.acompteFournisseur = :acompte AND y.pieceDocDivers.statutPiece = :statut"),
    @NamedQuery(name = "YvsComptaNotifReglementDocDivers.findPieceByStatutAcompteVente", query = "SELECT DISTINCT y.pieceDocDivers FROM YvsComptaNotifReglementDocDivers y WHERE y.acompteClient = :acompte AND y.pieceDocDivers.statutPiece = :statut")})
public class YvsComptaNotifReglementDocDivers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.DATE)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.DATE)
    private Date dateUpdate;
    @JoinColumn(name = "acompte_client", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaAcompteClient acompteClient;
    @JoinColumn(name = "acompte_fournisseur", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaAcompteFournisseur acompteFournisseur;
    @JoinColumn(name = "piece_doc_divers", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaCaissePieceDivers pieceDocDivers;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsComptaNotifReglementDocDivers() {
    }

    public YvsComptaNotifReglementDocDivers(Long id) {
        this.id = id;
    }
    
    public YvsComptaNotifReglementDocDivers(YvsComptaCaissePieceDivers pieceDocDivers, YvsComptaAcompteClient acompte, YvsUsersAgence author) {
        this.author = author;
        this.pieceDocDivers = pieceDocDivers;
        this.acompteClient = acompte;
    }
     public YvsComptaNotifReglementDocDivers(YvsComptaCaissePieceDivers pieceDocDivers, YvsComptaAcompteFournisseur acompte, YvsUsersAgence author) {
        this.author = author;
        this.pieceDocDivers = pieceDocDivers;
        this.acompteFournisseur = acompte;
    }

    public Long getId() {
        return id != null ? id:0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public YvsComptaAcompteClient getAcompteClient() {
        return acompteClient;
    }

    public void setAcompteClient(YvsComptaAcompteClient acompteClient) {
        this.acompteClient = acompteClient;
    }

    public YvsComptaAcompteFournisseur getAcompteFournisseur() {
        return acompteFournisseur;
    }

    public void setAcompteFournisseur(YvsComptaAcompteFournisseur acompteFournisseur) {
        this.acompteFournisseur = acompteFournisseur;
    }

    public YvsComptaCaissePieceDivers getPieceDocDivers() {
        return pieceDocDivers;
    }

    public void setPieceDocDivers(YvsComptaCaissePieceDivers pieceDocDivers) {
        this.pieceDocDivers = pieceDocDivers;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsComptaNotifReglementDocDivers)) {
            return false;
        }
        YvsComptaNotifReglementDocDivers other = (YvsComptaNotifReglementDocDivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaNotifReglementDocDivers[ id=" + id + " ]";
    }

}
