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
import javax.xml.bind.annotation.XmlRootElement;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_mut_vote_validation_credit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsMutVoteValidationCredit.findAll", query = "SELECT y FROM YvsMutVoteValidationCredit y"),
    @NamedQuery(name = "YvsMutVoteValidationCredit.findById", query = "SELECT y FROM YvsMutVoteValidationCredit y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutVoteValidationCredit.findByDateValidation", query = "SELECT y FROM YvsMutVoteValidationCredit y WHERE y.dateValidation = :dateValidation"),
    @NamedQuery(name = "YvsMutVoteValidationCredit.findByAccepte", query = "SELECT y FROM YvsMutVoteValidationCredit y WHERE y.accepte = :accepte"),
    @NamedQuery(name = "YvsMutVoteValidationCredit.counVoteValide", query = "SELECT COUNT(y) FROM YvsMutVoteValidationCredit y WHERE y.credit = :credit AND y.accepte=true"),
    @NamedQuery(name = "YvsMutVoteValidationCredit.findByCredit", query = "SELECT y FROM YvsMutVoteValidationCredit y WHERE y.credit = :credit"),
    @NamedQuery(name = "YvsMutVoteValidationCredit.findByCreditMutualise", query = "SELECT y FROM YvsMutVoteValidationCredit y WHERE y.credit = :credit AND y.mutualiste = :mutualiste")})
public class YvsMutVoteValidationCredit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_vote_validation_credit_id_seq", name = "yvs_mut_vote_validation_credit_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_vote_validation_credit_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_validation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidation;
    @Column(name = "accepte")
    private Boolean accepte;
    @JoinColumn(name = "credit", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCredit credit;
    @JoinColumn(name = "mutualiste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutualiste mutualiste;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsMutVoteValidationCredit() {
    }

    public YvsMutVoteValidationCredit(Long id) {
        this.id = id;
    }

    public YvsMutVoteValidationCredit(long id, YvsMutCredit credit, YvsMutMutualiste mutualiste) {
        this.id = id;
        this.credit = credit;
        this.mutualiste = mutualiste;
        this.dateValidation = new Date();
    }

    public YvsMutVoteValidationCredit(long id, YvsMutCredit credit, YvsMutMutualiste mutualiste, boolean approuve) {
        this.id = id;
        this.credit = credit;
        this.mutualiste = mutualiste;
        this.accepte = approuve;
        this.dateValidation = new Date();
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

    public Date getDateValidation() {
        return dateValidation != null ? dateValidation : new Date();
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    public Boolean getAccepte() {
        return accepte != null ? accepte : false;
    }

    public void setAccepte(Boolean accepte) {
        this.accepte = accepte;
    }

    public YvsMutCredit getCredit() {
        return credit;
    }

    public void setCredit(YvsMutCredit credit) {
        this.credit = credit;
    }

    public YvsMutMutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(YvsMutMutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsMutVoteValidationCredit)) {
            return false;
        }
        YvsMutVoteValidationCredit other = (YvsMutVoteValidationCredit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.credit.YvsMutVoteValidationCredit[ id=" + id + " ]";
    }

}
