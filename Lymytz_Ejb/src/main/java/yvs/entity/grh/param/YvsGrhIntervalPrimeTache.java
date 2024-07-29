/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_grh_interval_prime_tache")
@NamedQueries({
    @NamedQuery(name = "YvsGrhIntervalPrimeTache.findAll", query = "SELECT y FROM YvsGrhIntervalPrimeTache y"),
    @NamedQuery(name = "YvsGrhIntervalPrimeTache.findById", query = "SELECT y FROM YvsGrhIntervalPrimeTache y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhIntervalPrimeTache.findByQuantite", query = "SELECT y FROM YvsGrhIntervalPrimeTache y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsGrhIntervalPrimeTache.findByPrimeTache", query = "SELECT y FROM YvsGrhIntervalPrimeTache y WHERE y.primeTache = :primeTache"),
    @NamedQuery(name = "YvsGrhIntervalPrimeTache.findByMontant", query = "SELECT y FROM YvsGrhIntervalPrimeTache y WHERE y.montant = :montant")})
public class YvsGrhIntervalPrimeTache implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_interval_prime_tache_id_seq", name = "yvs_grh_interval_prime_tache_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_interval_prime_tache_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "montant")
    private Double montant;
    @Column(name = "taux")
    private Boolean taux;
    @JoinColumn(name = "prime_tache", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPrimeTache primeTache;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhIntervalPrimeTache() {
    }

    public YvsGrhIntervalPrimeTache(Long id) {
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

    public Boolean getTaux() {
        return taux != null ? taux : false;
    }

    public void setTaux(Boolean taux) {
        this.taux = taux;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public YvsGrhPrimeTache getPrimeTache() {
        return primeTache;
    }

    public void setPrimeTache(YvsGrhPrimeTache primeTache) {
        this.primeTache = primeTache;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsersAgence getAuthor() {
        return author;
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
        if (!(object instanceof YvsGrhIntervalPrimeTache)) {
            return false;
        }
        YvsGrhIntervalPrimeTache other = (YvsGrhIntervalPrimeTache) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhIntervalPrimeTache[ id=" + id + " ]";
    }

}
