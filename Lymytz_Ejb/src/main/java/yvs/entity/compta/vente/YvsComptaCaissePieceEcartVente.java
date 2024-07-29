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
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.vente.YvsComEcartEnteteVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_caisse_piece_ecart_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaissePieceEcartVente.findAll", query = "SELECT y FROM YvsComptaCaissePieceEcartVente y"),
    @NamedQuery(name = "YvsComptaCaissePieceEcartVente.findById", query = "SELECT y FROM YvsComptaCaissePieceEcartVente y WHERE y.id = :id"),    
    @NamedQuery(name = "YvsComptaCaissePieceEcartVente.findOne", query = "SELECT y FROM YvsComptaCaissePieceEcartVente y WHERE y.piece = :piece AND y.caisse = :caisse"),
    
    @NamedQuery(name = "YvsComptaCaissePieceEcartVente.findIdPieceByCaisse", query = "SELECT DISTINCT y.piece.id FROM YvsComptaCaissePieceEcartVente y WHERE y.caisse = :caisse")})
public class YvsComptaCaissePieceEcartVente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_piece_ecart_vente_id_seq", name = "yvs_compta_caisse_piece_ecart_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_piece_ecart_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement model;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComEcartEnteteVente piece;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComptaCaissePieceEcartVente() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaCaissePieceEcartVente(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaCaissePieceEcartVente(YvsBaseCaisse caisse, YvsBaseModeReglement model, YvsComEcartEnteteVente piece, YvsUsersAgence author) {
        this();
        this.author = author;
        this.caisse = caisse;
        this.piece = piece;
        this.model = model;
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

    @XmlTransient  @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient  @JsonIgnore
    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    @XmlTransient  @JsonIgnore
    public YvsComEcartEnteteVente getPiece() {
        return piece;
    }

    public void setPiece(YvsComEcartEnteteVente piece) {
        this.piece = piece;
    }

    @XmlTransient  @JsonIgnore
    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
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
        if (!(object instanceof YvsComptaCaissePieceEcartVente)) {
            return false;
        }
        YvsComptaCaissePieceEcartVente other = (YvsComptaCaissePieceEcartVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.vente.YvsComptaCaissePieceEcartVente[ id=" + id + " ]";
    }

}
