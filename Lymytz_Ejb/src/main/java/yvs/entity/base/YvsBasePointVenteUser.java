/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_base_point_vente_user")
@NamedQueries({
    @NamedQuery(name = "YvsBasePointVenteUser.findAll", query = "SELECT y FROM YvsBasePointVenteUser y"),
    @NamedQuery(name = "YvsBasePointVenteUser.findById", query = "SELECT y FROM YvsBasePointVenteUser y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePointVenteUser.findExist", query = "SELECT y.actif FROM YvsBasePointVenteUser y WHERE y.users=:user AND y.point=:point"),
    @NamedQuery(name = "YvsBasePointVenteUser.findOne", query = "SELECT y FROM YvsBasePointVenteUser y WHERE y.users=:user AND y.point=:point"),
    @NamedQuery(name = "YvsBasePointVenteUser.findOneActif", query = "SELECT y.actif FROM YvsBasePointVenteUser y WHERE y.users=:user AND y.point=:point"),
    @NamedQuery(name = "YvsBasePointVenteUser.findByDateSave", query = "SELECT y FROM YvsBasePointVenteUser y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBasePointVenteUser.findByDateUpdate", query = "SELECT y FROM YvsBasePointVenteUser y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBasePointVenteUser.findByActif", query = "SELECT y FROM YvsBasePointVenteUser y WHERE y.actif = :actif"),

    @NamedQuery(name = "YvsBasePointVenteUser.findIdPointByUser", query = "SELECT DISTINCT y.point.id FROM YvsBasePointVenteUser y WHERE y.users=:user AND y.actif=true"),
    @NamedQuery(name = "YvsBasePointVenteUser.findPointByUsers", query = "SELECT DISTINCT y.point FROM YvsBasePointVenteUser y WHERE y.users = :users AND y.point.actif=true AND y.actif=true ORDER BY y.point.agence.id "),
    @NamedQuery(name = "YvsBasePointVenteUser.findPointByUsersAgence", query = "SELECT DISTINCT y.point FROM YvsBasePointVenteUser y WHERE y.point.agence = :agence AND y.users = :users AND y.point.actif=true AND y.actif=true ORDER BY y.point.agence.id "),
    @NamedQuery(name = "YvsBasePointVenteUser.findPointActifByUsers", query = "SELECT DISTINCT y.point FROM YvsBasePointVenteUser y WHERE y.users = :users AND y.point.actif = TRUE AND y.actif = TRUE")})
public class YvsBasePointVenteUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_point_vente_user_id_seq", name = "yvs_base_point_vente_user_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_point_vente_user_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers users;
    @JoinColumn(name = "point", referencedColumnName = "id")
    @ManyToOne
    private YvsBasePointVente point;

    public YvsBasePointVenteUser() {
    }

    public YvsBasePointVenteUser(Long id) {
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

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBasePointVente getPoint() {
        return point;
    }

    public void setPoint(YvsBasePointVente point) {
        this.point = point;
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
        if (!(object instanceof YvsBasePointVenteUser)) {
            return false;
        }
        YvsBasePointVenteUser other = (YvsBasePointVenteUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBasePointVenteUser[ id=" + id + " ]";
    }

}
