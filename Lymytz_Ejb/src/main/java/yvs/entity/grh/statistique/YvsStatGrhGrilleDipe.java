/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.statistique;

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
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_stat_grh_grille_dipe")
@NamedQueries({
    @NamedQuery(name = "YvsStatGrhGrilleDipe.findAll", query = "SELECT y FROM YvsStatGrhGrilleDipe y"),
    @NamedQuery(name = "YvsStatGrhGrilleDipe.findById", query = "SELECT y FROM YvsStatGrhGrilleDipe y WHERE y.id = :id"),
    @NamedQuery(name = "YvsStatGrhGrilleDipe.findByEtat", query = "SELECT y FROM YvsStatGrhGrilleDipe y WHERE y.etat = :etat ORDER BY y.trancheMax ASC"),
    @NamedQuery(name = "YvsStatGrhGrilleDipe.findByTrancheMax", query = "SELECT y FROM YvsStatGrhGrilleDipe y WHERE y.trancheMax = :trancheMax"),
    @NamedQuery(name = "YvsStatGrhGrilleDipe.findByMontant", query = "SELECT y FROM YvsStatGrhGrilleDipe y WHERE y.montant = :montant")})
public class YvsStatGrhGrilleDipe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_stat_grh_grille_dipe_id_seq", name = "yvs_stat_grh_grille_dipe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_stat_grh_grille_dipe_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "tranche_min")
    private Double trancheMin;
    @Column(name = "tranche_max")
    private Double trancheMax;
    @Column(name = "montant")
    private Double montant;
    @JoinColumn(name = "etat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsStatGrhEtat etat;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsStatGrhGrilleDipe() {
    }

    public YvsStatGrhGrilleDipe(Long id) {
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

    public Double getTrancheMin() {
        return trancheMin;
    }

    public void setTrancheMin(Double trancheMin) {
        this.trancheMin = trancheMin;
    }

    public Double getTrancheMax() {
        return trancheMax;
    }

    public void setTrancheMax(Double trancheMax) {
        this.trancheMax = trancheMax;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public YvsStatGrhEtat getEtat() {
        return etat;
    }

    public void setEtat(YvsStatGrhEtat etat) {
        this.etat = etat;
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
        if (!(object instanceof YvsStatGrhGrilleDipe)) {
            return false;
        }
        YvsStatGrhGrilleDipe other = (YvsStatGrhGrilleDipe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.statistique.YvsStatGrhGrilleDipe[ id=" + id + " ]";
    }

}
