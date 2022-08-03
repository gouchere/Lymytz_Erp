/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param.poste;

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
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_preavis")
@NamedQueries({
    @NamedQuery(name = "YvsGrhPreavis.findAll", query = "SELECT y FROM YvsGrhPreavis y"),
    @NamedQuery(name = "YvsGrhPreavis.findById", query = "SELECT y FROM YvsGrhPreavis y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhPreavis.findByDuree", query = "SELECT y FROM YvsGrhPreavis y WHERE y.duree = :duree")})
public class YvsGrhPreavis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_preavis_id_seq", name = "yvs_grh_preavis_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_preavis_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "duree")
    private Double duree;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "unite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure unite;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsGrhPreavis() {
    }

    public YvsGrhPreavis(Integer id) {
        this.id = id;
    }

    public YvsGrhPreavis(Integer id, Double duree, YvsBaseUniteMesure unite) {
        this.id = id;
        this.duree = duree;
        this.unite = unite;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getDuree() {
        return duree != null ? duree : 0;
    }

    public void setDuree(Double duree) {
        this.duree = duree;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsGrhPreavis)) {
            return false;
        }
        YvsGrhPreavis other = (YvsGrhPreavis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.poste.YvsGrhPreavis[ id=" + id + " ]";
    }

    }
