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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_contenu_doc_stock_reception")
@NamedQueries({
    @NamedQuery(name = "YvsComContenuDocStockReception.findAll", query = "SELECT y FROM YvsComContenuDocStockReception y WHERE y.contenu.docStock.source.agence.societe = :societe"),
    @NamedQuery(name = "YvsComContenuDocStockReception.findById", query = "SELECT y FROM YvsComContenuDocStockReception y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComContenuDocStockReception.findByMontant", query = "SELECT y FROM YvsComContenuDocStockReception y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsComContenuDocStockReception.sunQuantiteByContenu", query = "SELECT SUM(y.quantite) FROM YvsComContenuDocStockReception y WHERE y.contenu = :contenu"),
    @NamedQuery(name = "YvsComContenuDocStockReception.findByContenu", query = "SELECT y FROM YvsComContenuDocStockReception y WHERE y.contenu = :contenu"),
    @NamedQuery(name = "YvsComContenuDocStockReception.findByDocStock", query = "SELECT y FROM YvsComContenuDocStockReception y WHERE y.contenu.docStock = :docStock")})
public class YvsComContenuDocStockReception implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_contenu_doc_stock_reception_id_seq", name = "yvs_com_contenu_doc_stock_reception_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_contenu_doc_stock_reception_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_reception")
    @Temporal(TemporalType.DATE)
    private Date dateReception;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "calcul_pr")
    private Boolean calculPr;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "contenu", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComContenuDocStock contenu;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    public YvsComContenuDocStockReception() {
    }

    public YvsComContenuDocStockReception(Long id) {
        this.id = id;
    }

    public YvsComContenuDocStockReception(Long id, YvsUsersAgence author) {
        this(id);
        this.author = author;
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

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Boolean getCalculPr() {
        return calculPr != null ? calculPr : false;
    }

    public void setCalculPr(Boolean calculPr) {
        this.calculPr = calculPr;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComContenuDocStock getContenu() {
        return contenu;
    }

    public void setContenu(YvsComContenuDocStock contenu) {
        this.contenu = contenu;
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
        if (!(object instanceof YvsComContenuDocStockReception)) {
            return false;
        }
        YvsComContenuDocStockReception other = (YvsComContenuDocStockReception) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.stock.YvsComContenuDocStockReception[ id=" + id + " ]";
    }

}
