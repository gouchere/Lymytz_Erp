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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_caisse_piece_compensation")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaissePieceCompensation.findAll", query = "SELECT y FROM YvsComptaCaissePieceCompensation y"),
    @NamedQuery(name = "YvsComptaCaissePieceCompensation.findById", query = "SELECT y FROM YvsComptaCaissePieceCompensation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaissePieceCompensation.findOne", query = "SELECT y FROM YvsComptaCaissePieceCompensation y WHERE y.vente = :vente AND y.achat = :achat"),
    @NamedQuery(name = "YvsComptaCaissePieceCompensation.findByDateSave", query = "SELECT y FROM YvsComptaCaissePieceCompensation y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaCaissePieceCompensation.findByDateUpdate", query = "SELECT y FROM YvsComptaCaissePieceCompensation y WHERE y.dateUpdate = :dateUpdate"),

    @NamedQuery(name = "YvsComptaCaissePieceCompensation.findByVente", query = "SELECT y FROM YvsComptaCaissePieceCompensation y WHERE y.vente = :vente"),
    @NamedQuery(name = "YvsComptaCaissePieceCompensation.findByVenteNotId", query = "SELECT y FROM YvsComptaCaissePieceCompensation y WHERE y.vente = :vente AND y.achat.id != :id"),
    @NamedQuery(name = "YvsComptaCaissePieceCompensation.findByAchat", query = "SELECT y FROM YvsComptaCaissePieceCompensation y WHERE y.achat = :achat"),
    @NamedQuery(name = "YvsComptaCaissePieceCompensation.findByAchatNotId", query = "SELECT y FROM YvsComptaCaissePieceCompensation y WHERE y.achat = :achat AND y.vente.id != :id")})
public class YvsComptaCaissePieceCompensation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_piece_compensation_id_seq", name = "yvs_compta_caisse_piece_compensation_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_piece_compensation_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "sens")
    private Character sens;
    
    @JoinColumn(name = "vente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceVente vente;
    @JoinColumn(name = "achat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceAchat achat;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsComptaCaissePieceCompensation() {
    }

    public YvsComptaCaissePieceCompensation(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public YvsComptaCaissePieceVente getVente() {
        return vente;
    }

    public void setVente(YvsComptaCaissePieceVente vente) {
        this.vente = vente;
    }

    public YvsComptaCaissePieceAchat getAchat() {
        return achat;
    }

    public void setAchat(YvsComptaCaissePieceAchat achat) {
        this.achat = achat;
    }

    public Character getSens() {
        return sens != null ? String.valueOf(sens).trim().length() > 0 ? sens : 'C' : 'C';
    }

    public void setSens(Character sens) {
        this.sens = sens;
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
        if (!(object instanceof YvsComptaCaissePieceCompensation)) {
            return false;
        }
        YvsComptaCaissePieceCompensation other = (YvsComptaCaissePieceCompensation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaCaissePieceCompensation[ id=" + id + " ]";
    }

}
