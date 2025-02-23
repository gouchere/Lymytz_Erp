/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.stock;

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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.OneToOne;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.grh.contrat.retenue.YvsGrhRetenueEcartStock;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_reglement_ecart_stock", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsComReglementEcartStock.findAll", query = "SELECT y FROM YvsComReglementEcartStock y"),
    @NamedQuery(name = "YvsComReglementEcartStock.findById", query = "SELECT y FROM YvsComReglementEcartStock y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComReglementEcartStock.findByDateSave", query = "SELECT y FROM YvsComReglementEcartStock y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComReglementEcartStock.findByDateUpdate", query = "SELECT y FROM YvsComReglementEcartStock y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComReglementEcartStock.findByAuthor", query = "SELECT y FROM YvsComReglementEcartStock y WHERE y.author = :author"),
    @NamedQuery(name = "YvsComReglementEcartStock.findByPiece", query = "SELECT y FROM YvsComReglementEcartStock y WHERE y.piece = :piece"),
    
    @NamedQuery(name = "YvsComReglementEcartStock.countByPiece", query = "SELECT COUNT(y.id) FROM YvsComReglementEcartStock y WHERE y.piece = :piece"),
    
    @NamedQuery(name = "YvsComReglementEcartStock.sumByPiece", query = "SELECT SUM(y.montant) FROM YvsComReglementEcartStock y WHERE y.piece = :piece"),
    @NamedQuery(name = "YvsComReglementEcartStock.sumByPieceNotId", query = "SELECT SUM(y.montant) FROM YvsComReglementEcartStock y WHERE y.piece = :piece AND y.id != :id"),
    @NamedQuery(name = "YvsComReglementEcartStock.sumByPiecePaye", query = "SELECT SUM(y.montant) FROM YvsComReglementEcartStock y WHERE y.piece = :piece AND y.statut = 'P'")
})
public class YvsComReglementEcartStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_reglement_ecart_stock_id_seq", name = "yvs_com_reglement_ecart_stock_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_reglement_ecart_stock_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "numero")
    private String numero;
    @Column(name = "date_reglement")
    @Temporal(TemporalType.DATE)
    private Date dateReglement;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "statut")
    private Character statut;

    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocStocksEcart piece;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToOne(mappedBy = "ecart")
    private YvsGrhRetenueEcartStock retenue;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComReglementEcartStock() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComReglementEcartStock(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Date getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(Date dateReglement) {
        this.dateReglement = dateReglement;
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

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Character getStatut() {
        return statut != null ? statut : 'W';
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComDocStocksEcart getPiece() {
        return piece;
    }

    public void setPiece(YvsComDocStocksEcart piece) {
        this.piece = piece;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhRetenueEcartStock getRetenue() {
        return retenue;
    }

    public void setRetenue(YvsGrhRetenueEcartStock retenue) {
        this.retenue = retenue;
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
        if (!(object instanceof YvsComReglementEcartStock)) {
            return false;
        }
        YvsComReglementEcartStock other = (YvsComReglementEcartStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComReglementEcartStock[ id=" + id + " ]";
    }

}
