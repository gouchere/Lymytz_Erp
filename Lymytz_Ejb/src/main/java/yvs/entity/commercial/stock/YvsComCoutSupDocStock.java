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
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_cout_sup_doc_stock")
@NamedQueries({
    @NamedQuery(name = "YvsComCoutSupDocStock.findAll", query = "SELECT y FROM YvsComCoutSupDocStock y WHERE y.docStock.source.agence.societe = :societe"),
    @NamedQuery(name = "YvsComCoutSupDocStock.findById", query = "SELECT y FROM YvsComCoutSupDocStock y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCoutSupDocStock.findByMontant", query = "SELECT y FROM YvsComCoutSupDocStock y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComCoutSupDocStock.findBySupp", query = "SELECT y FROM YvsComCoutSupDocStock y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsComCoutSupDocStock.findByActif", query = "SELECT y FROM YvsComCoutSupDocStock y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComCoutSupDocStock.findByDocStock", query = "SELECT y FROM YvsComCoutSupDocStock y WHERE y.docStock = :docStock")})
public class YvsComCoutSupDocStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_cout_sup_doc_achat_id_seq", name = "yvs_cout_sup_doc_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_cout_sup_doc_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "doc_stock", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocStocks docStock;
    @JoinColumn(name = "type_cout", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeCout typeCout;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    public YvsComCoutSupDocStock() {
    }

    public YvsComCoutSupDocStock(Long id) {
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

    public YvsGrhTypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(YvsGrhTypeCout typeCout) {
        this.typeCout = typeCout;
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

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComDocStocks getDocStock() {
        return docStock;
    }

    public void setDocStock(YvsComDocStocks docStock) {
        this.docStock = docStock;
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
        if (!(object instanceof YvsComCoutSupDocStock)) {
            return false;
        }
        YvsComCoutSupDocStock other = (YvsComCoutSupDocStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.stock.YvsComCoutSupDocStock[ id=" + id + " ]";
    }

}
