/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.base;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
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
@Table(name = "yvs_mut_poste")
@NamedQueries({
    @NamedQuery(name = "YvsMutPoste.findAll", query = "SELECT y FROM YvsMutPoste y WHERE y.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutPoste.findById", query = "SELECT y FROM YvsMutPoste y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutPoste.findByMutuelle", query = "SELECT y FROM YvsMutPoste y WHERE y.mutuelle = :mutuelle ORDER BY y.designation"),
    @NamedQuery(name = "YvsMutPoste.findByEmploye", query = "SELECT y FROM YvsMutPoste y WHERE y.mutuelle = :mutuelle ORDER BY y.designation"),
    @NamedQuery(name = "YvsMutPoste.findByDesignation", query = "SELECT y FROM YvsMutPoste y WHERE y.designation = :designation")})
public class YvsMutPoste implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_poste_id_seq", name = "yvs_mut_poste_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_poste_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "can_vote_credit")
    private Boolean canVoteCredit;
    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "poste")
    private List<YvsMutPrimePoste> primes;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private double montantPrime;

    public YvsMutPoste() {
    }

    public YvsMutPoste(Long id) {
        this.id = id;
    }

    public YvsMutPoste(Long id, String designation) {
        this.id = id;
        this.designation = designation;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave==null?new Date():dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public double getMontantPrime() {
        montantPrime = 0;
        if (getPrimes() != null) {
            for (YvsMutPrimePoste pp : getPrimes()) {
                if (pp.getType().getNatureMontant().equals("Fixe")) {
                    montantPrime += pp.getMontant();
                }
            }
        }
        return montantPrime;
    }

    public void setMontantPrime(double montantPrime) {
        this.montantPrime = montantPrime;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public YvsMutMutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(YvsMutMutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public List<YvsMutPrimePoste> getPrimes() {
        return primes;
    }

    public void setPrimes(List<YvsMutPrimePoste> primes) {
        this.primes = primes;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getCanVoteCredit() {
        return canVoteCredit != null ? canVoteCredit : false;
    }

    public void setCanVoteCredit(Boolean canVoteCredit) {
        this.canVoteCredit = canVoteCredit;
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
        if (!(object instanceof YvsMutPoste)) {
            return false;
        }
        YvsMutPoste other = (YvsMutPoste) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.base.YvsMutPoste[ id=" + id + " ]";
    }

}
