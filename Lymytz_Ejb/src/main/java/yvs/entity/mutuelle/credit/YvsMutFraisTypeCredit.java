/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.credit;

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
import javax.xml.bind.annotation.XmlRootElement;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_mut_frais_type_credit", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsMutFraisTypeCredit.findAll", query = "SELECT y FROM YvsMutFraisTypeCredit y"),
    @NamedQuery(name = "YvsMutFraisTypeCredit.findById", query = "SELECT y FROM YvsMutFraisTypeCredit y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutFraisTypeCredit.findByMontant", query = "SELECT y FROM YvsMutFraisTypeCredit y WHERE y.montant = :montant")})
public class YvsMutFraisTypeCredit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_frais_type_credit_id_seq", name = "yvs_mut_frais_type_credit_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_frais_type_credit_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "credit", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutTypeCredit credit;
    @JoinColumn(name = "type", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeCout type;
    @Transient
    private boolean select;
    @Transient
    private boolean new_;

    public YvsMutFraisTypeCredit() {
    }

    public YvsMutFraisTypeCredit(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsMutTypeCredit getCredit() {
        return credit;
    }

    public void setCredit(YvsMutTypeCredit credit) {
        this.credit = credit;
    }

    public YvsGrhTypeCout getType() {
        return type;
    }

    public void setType(YvsGrhTypeCout type) {
        this.type = type;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        if (!(object instanceof YvsMutFraisTypeCredit)) {
            return false;
        }
        YvsMutFraisTypeCredit other = (YvsMutFraisTypeCredit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.credit.YvsMutFraisTypeCredit[ id=" + id + " ]";
    }

}
