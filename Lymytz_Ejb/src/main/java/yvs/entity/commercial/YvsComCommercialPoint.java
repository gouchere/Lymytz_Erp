/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial;

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
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_commercial_point", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsComCommercialPoint.findAll", query = "SELECT y FROM YvsComCommercialPoint y"),
    @NamedQuery(name = "YvsComCommercialPoint.findById", query = "SELECT y FROM YvsComCommercialPoint y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCommercialPoint.findByDateSave", query = "SELECT y FROM YvsComCommercialPoint y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComCommercialPoint.findByDateUpdate", query = "SELECT y FROM YvsComCommercialPoint y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComCommercialPoint.findByAuthor", query = "SELECT y FROM YvsComCommercialPoint y WHERE y.author = :author"),
    @NamedQuery(name = "YvsComCommercialPoint.findOne", query = "SELECT y FROM YvsComCommercialPoint y WHERE y.point = :point AND y.commercial = :commercial"),
    @NamedQuery(name = "YvsComCommercialPoint.findByPoint", query = "SELECT y FROM YvsComCommercialPoint y WHERE y.point = :point")})
public class YvsComCommercialPoint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_commercial_point_id_seq", name = "yvs_com_commercial_point_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_commercial_point_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "point", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePointVente point;
    @JoinColumn(name = "commercial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComComerciale commercial;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComCommercialPoint() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComCommercialPoint(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public YvsBasePointVente getPoint() {
        return point;
    }

    public void setPoint(YvsBasePointVente point) {
        this.point = point;
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
        if (!(object instanceof YvsComCommercialPoint)) {
            return false;
        }
        YvsComCommercialPoint other = (YvsComCommercialPoint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComCommercialPoint[ id=" + id + " ]";
    }

}
