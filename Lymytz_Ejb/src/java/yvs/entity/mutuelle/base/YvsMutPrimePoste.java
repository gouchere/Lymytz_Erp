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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_prime_poste")
@NamedQueries({
    @NamedQuery(name = "YvsMutPrimePoste.findAll", query = "SELECT y FROM YvsMutPrimePoste y WHERE y.type.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutPrimePoste.findById", query = "SELECT y FROM YvsMutPrimePoste y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutPrimePoste.findByPoste", query = "SELECT y FROM YvsMutPrimePoste y WHERE y.poste = :poste"),
    @NamedQuery(name = "YvsMutPrimePoste.findByMutuelle", query = "SELECT y FROM YvsMutPrimePoste y WHERE y.type.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutPrimePoste.findByMontant", query = "SELECT y FROM YvsMutPrimePoste y WHERE y.montant = :montant")})
public class YvsMutPrimePoste implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_prime_poste_id_seq", name = "yvs_mut_prime_poste_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_prime_poste_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "type", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutTypePrime type;
    @JoinColumn(name = "poste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutPoste poste;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private Date datePaiement = new Date();
    @Transient
    private boolean payePasse;
    @Transient
    private boolean paye;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutPrimePoste() {
    }

    public YvsMutPrimePoste(Long id) {
        this.id = id;
    }

    public YvsMutPrimePoste(Long id, YvsMutPoste poste) {
        this.id = id;
        this.poste = poste;
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

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isPaye() {
        return paye;
    }

    public void setPaye(boolean paye) {
        this.paye = paye;
    }

    public boolean isPayePasse() {
        return payePasse;
    }

    public void setPayePasse(boolean payePasse) {
        this.payePasse = payePasse;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant!=null?montant:0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public YvsMutTypePrime getType() {
        return type;
    }

    public void setType(YvsMutTypePrime type) {
        this.type = type;
    }

    public YvsMutPoste getPoste() {
        return poste;
    }

    public void setPoste(YvsMutPoste poste) {
        this.poste = poste;
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
        if (!(object instanceof YvsMutPrimePoste)) {
            return false;
        }
        YvsMutPrimePoste other = (YvsMutPrimePoste) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.base.YvsMutPrimePoste[ id=" + id + " ]";
    }

}
