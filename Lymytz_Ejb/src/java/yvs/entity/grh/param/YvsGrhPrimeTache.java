/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

import yvs.entity.grh.taches.YvsGrhMontantTache;
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
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_grh_prime_tache")
@NamedQueries({
    @NamedQuery(name = "YvsGrhPrimeTache.findAll", query = "SELECT y FROM YvsGrhPrimeTache y WHERE y.author.agence.societe = :societe AND y.actif = true"),
    @NamedQuery(name = "YvsGrhPrimeTache.findByDatePrime", query = "SELECT y FROM YvsGrhPrimeTache y WHERE y.datePrime = :datePrime"),
    @NamedQuery(name = "YvsGrhPrimeTache.findById", query = "SELECT y FROM YvsGrhPrimeTache y WHERE y.id = :id")})
public class YvsGrhPrimeTache implements Serializable {

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_prime")
    @Temporal(TemporalType.DATE)
    private Date datePrime;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_prime_tache_id_seq", name = "yvs_grh_prime_tache_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_prime_tache_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @OneToMany(mappedBy = "primeTache")
    private List<YvsGrhIntervalPrimeTache> tranchesPrime;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Column(name = "reference_prime")
    private String referencePrime;

    public YvsGrhPrimeTache() {
    }

    public YvsGrhPrimeTache(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<YvsGrhIntervalPrimeTache> getTranchesPrime() {
        return tranchesPrime;
    }

    public void setTranchesPrime(List<YvsGrhIntervalPrimeTache> tranchesPrime) {
        this.tranchesPrime = tranchesPrime;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
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

    public String getReferencePrime() {
        return referencePrime;
    }

    public void setReferencePrime(String referencePrime) {
        this.referencePrime = referencePrime;
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
        if (!(object instanceof YvsGrhPrimeTache)) {
            return false;
        }
        YvsGrhPrimeTache other = (YvsGrhPrimeTache) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhPrimeTache[ id=" + id + " ]";
    }

    public Date getDatePrime() {
        return datePrime;
    }

    public void setDatePrime(Date datePrime) {
        this.datePrime = datePrime;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

}
