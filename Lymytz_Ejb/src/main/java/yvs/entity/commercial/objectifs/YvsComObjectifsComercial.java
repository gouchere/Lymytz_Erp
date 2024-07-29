/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.objectifs;

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
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
  @Entity
@Table(name = "yvs_com_objectifs_comercial")
@NamedQueries({
    @NamedQuery(name = "YvsComObjectifsComercial.findAll", query = "SELECT y FROM YvsComObjectifsComercial y"),
    @NamedQuery(name = "YvsComObjectifsComercial.findById", query = "SELECT y FROM YvsComObjectifsComercial y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComObjectifsComercial.findByPeriode", query = "SELECT y FROM YvsComObjectifsComercial y WHERE y.periode = :periode"),
    @NamedQuery(name = "YvsComObjectifsComercial.findByPeriodeCom", query = "SELECT y FROM YvsComObjectifsComercial y WHERE y.periode = :periode AND y.commercial=:commercial AND y.objectif=:objectif"),
    @NamedQuery(name = "YvsComObjectifsComercial.findByObjectifs", query = "SELECT y FROM YvsComObjectifsComercial y WHERE y.periode.id IN (:periodes) AND y.objectif = :objectif"),
    @NamedQuery(name = "YvsComObjectifsComercial.findByCommercial", query = "SELECT y FROM YvsComObjectifsComercial y WHERE y.periode = :periode AND y.commercial = :commercial"),
    @NamedQuery(name = "YvsComObjectifsComercial.findByDateSave", query = "SELECT y FROM YvsComObjectifsComercial y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComObjectifsComercial.findByDateUpdate", query = "SELECT y FROM YvsComObjectifsComercial y WHERE y.dateUpdate = :dateUpdate")})
public class YvsComObjectifsComercial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_objectifs_comercial_id_seq", name = "yvs_com_objectifs_comercial_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_objectifs_comercial_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur")
    private Double valeur;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "periode", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComPeriodeObjectif periode;
    @JoinColumn(name = "objectif", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComModeleObjectif objectif;
    @JoinColumn(name = "commercial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComComerciale commercial;

    @Transient
    private boolean new_;
    @Transient
    private double realise;

    public YvsComObjectifsComercial() {
    }

    public YvsComObjectifsComercial(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComPeriodeObjectif getPeriode() {
        return periode;
    }

    public void setPeriode(YvsComPeriodeObjectif periode) {
        this.periode = periode;
    }

    public YvsComModeleObjectif getObjectif() {
        return objectif;
    }

    public void setObjectif(YvsComModeleObjectif objectif) {
        this.objectif = objectif;
    }

    public YvsComComerciale getCommercial() {
        return commercial;
    }

    public void setCommercial(YvsComComerciale commercial) {
        this.commercial = commercial;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public double getRealise() {
        return realise;
    }

    public void setRealise(double realise) {
        this.realise = realise;
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
        if (!(object instanceof YvsComObjectifsComercial)) {
            return false;
        }
        YvsComObjectifsComercial other = (YvsComObjectifsComercial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.objectifs.YvsComObjectifsComercial[ id=" + id + " ]";
    }

}
