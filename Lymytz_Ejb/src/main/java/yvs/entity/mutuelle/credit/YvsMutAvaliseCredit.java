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
import yvs.entity.mutuelle.base.YvsMutMutualiste;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_avalise_credit")
@NamedQueries({
    @NamedQuery(name = "YvsMutAvaliseCredit.findAll", query = "SELECT y FROM YvsMutAvaliseCredit y"),
    @NamedQuery(name = "YvsMutAvaliseCredit.findById", query = "SELECT y FROM YvsMutAvaliseCredit y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutAvaliseCredit.findByCredit", query = "SELECT y FROM YvsMutAvaliseCredit y WHERE y.credit = :credit"),
    @NamedQuery(name = "YvsMutAvaliseCredit.findByMutualiste", query = "SELECT y FROM YvsMutAvaliseCredit y WHERE y.mutualiste = :mutualiste"),
    @NamedQuery(name = "YvsMutAvaliseCredit.findByMutualisteDates", query = "SELECT y FROM YvsMutAvaliseCredit y WHERE y.mutualiste = :mutualiste AND y.credit.dateCredit BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutAvaliseCredit.findMontantAvalise", query = "SELECT SUM(y.montant - y.montantLibere) FROM YvsMutAvaliseCredit y WHERE y.mutualiste = :mutualiste AND y.credit.etat=:etat"),
    @NamedQuery(name = "YvsMutAvaliseCredit.findMontantAvalise_", query = "SELECT SUM(y.montant - y.montantLibere) FROM YvsMutAvaliseCredit y WHERE y.mutualiste = :mutualiste "),
    @NamedQuery(name = "YvsMutAvaliseCredit.findByMontant", query = "SELECT y FROM YvsMutAvaliseCredit y WHERE y.montant = :montant")})
public class YvsMutAvaliseCredit implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "montant_libere")
    private double montantLibere;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_avalise_credit_id_seq", name = "yvs_mut_avalise_credit_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_avalise_credit_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "mutualiste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutualiste mutualiste;
    @JoinColumn(name = "credit", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCredit credit;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutAvaliseCredit() {
    }

    public YvsMutAvaliseCredit(Long id) {
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

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsMutMutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(YvsMutMutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public YvsMutCredit getCredit() {
        return credit;
    }

    public void setCredit(YvsMutCredit credit) {
        this.credit = credit;
    }

    public Double getMontantLibere() {
        return montantLibere;
    }

    public void setMontantLibere(Double montantLibere) {
        this.montantLibere = montantLibere;
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
        if (!(object instanceof YvsMutAvaliseCredit)) {
            return false;
        }
        YvsMutAvaliseCredit other = (YvsMutAvaliseCredit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.credit.YvsMutAvaliseCredit[ id=" + id + " ]";
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

}
