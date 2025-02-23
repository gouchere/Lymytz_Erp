/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.analytique;

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
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_repartition_analytique")
@NamedQueries({
    @NamedQuery(name = "YvsComptaRepartitionAnalytique.findAll", query = "SELECT y FROM YvsComptaRepartitionAnalytique y"),
    @NamedQuery(name = "YvsComptaRepartitionAnalytique.findByCentreP", query = "SELECT y FROM YvsComptaRepartitionAnalytique y WHERE y.principal = :centre"),
    @NamedQuery(name = "YvsComptaRepartitionAnalytique.findOne", query = "SELECT y FROM YvsComptaRepartitionAnalytique y WHERE y.principal = :principal AND y.secondaire = :secondaire"),
    @NamedQuery(name = "YvsComptaRepartitionAnalytique.findByCoefficient", query = "SELECT y FROM YvsComptaRepartitionAnalytique y WHERE y.taux = :coefficient")})
public class YvsComptaRepartitionAnalytique implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_repartition_analytique_id_seq", name = "yvs_compta_repartition_analytique_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_repartition_analytique_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "taux")
    private Double taux;
    @JoinColumn(name = "secondaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCentreAnalytique secondaire;
    @JoinColumn(name = "principal", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCentreAnalytique principal;
    @JoinColumn(name = "unite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure unite;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;

    public YvsComptaRepartitionAnalytique() {
    }

    public YvsComptaRepartitionAnalytique(Long id) {
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
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTaux() {
        return taux != null ? taux : 0;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public YvsComptaCentreAnalytique getSecondaire() {
        return secondaire;
    }

    public void setSecondaire(YvsComptaCentreAnalytique secondaire) {
        this.secondaire = secondaire;
    }

    public YvsComptaCentreAnalytique getPrincipal() {
        return principal;
    }

    public void setPrincipal(YvsComptaCentreAnalytique principal) {
        this.principal = principal;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseUniteMesure getUnite() {
        return unite;
    }

    public void setUnite(YvsBaseUniteMesure unite) {
        this.unite = unite;
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
        if (!(object instanceof YvsComptaRepartitionAnalytique)) {
            return false;
        }
        YvsComptaRepartitionAnalytique other = (YvsComptaRepartitionAnalytique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.analytique.YvsComptaRepartitionAnalytique[ id=" + id + " ]";
    }

}
