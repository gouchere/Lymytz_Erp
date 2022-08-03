/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.base;

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
import javax.validation.constraints.Size;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_type_prime")
@NamedQueries({
    @NamedQuery(name = "YvsMutTypePrime.findAll", query = "SELECT y FROM YvsMutTypePrime y WHERE y.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutTypePrime.findById", query = "SELECT y FROM YvsMutTypePrime y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutTypePrime.findByMutuelle", query = "SELECT y FROM YvsMutTypePrime y WHERE y.mutuelle = :mutuelle ORDER BY y.natureMontant ,y.montantMaximal DESC"),
    @NamedQuery(name = "YvsMutTypePrime.findByDesignation", query = "SELECT y FROM YvsMutTypePrime y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsMutTypePrime.findByMontantMaximal", query = "SELECT y FROM YvsMutTypePrime y WHERE y.montantMaximal = :montantMaximal"),
    @NamedQuery(name = "YvsMutTypePrime.findByNatureMontant", query = "SELECT y FROM YvsMutTypePrime y WHERE y.natureMontant = :natureMontant")})
public class YvsMutTypePrime implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_type_prime_id_seq", name = "yvs_mut_type_prime_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_type_prime_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_maximal")
    private Double montantMaximal;
    @Size(max = 2147483647)
    @Column(name = "nature_montant")
    private String natureMontant;
    @Column(name = "periode_remuneration")
    private String periodeRemuneration;
    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutTypePrime() {
    }

    public YvsMutTypePrime(Long id) {
        this.id = id;
    }

    public YvsMutTypePrime(Long id, String designation) {
        this.id = id;
        this.designation = designation;
    }

    public YvsUsersAgence getAuthor() {
        return author;
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

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getMontantMaximal() {
        return montantMaximal != null ? montantMaximal : 0;
    }

    public void setMontantMaximal(Double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public String getNatureMontant() {
        return natureMontant != null ? natureMontant.trim().length() > 0 ? natureMontant : "Fixe" : "Fixe";
    }

    public void setNatureMontant(String natureMontant) {
        this.natureMontant = natureMontant;
    }

    public YvsMutMutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(YvsMutMutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public String getPeriodeRemuneration() {
        return periodeRemuneration;
    }

    public void setPeriodeRemuneration(String periodeRemuneration) {
        this.periodeRemuneration = periodeRemuneration;
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
        if (!(object instanceof YvsMutTypePrime)) {
            return false;
        }
        YvsMutTypePrime other = (YvsMutTypePrime) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.base.YvsMutTypePrime[ id=" + id + " ]";
    }

}
