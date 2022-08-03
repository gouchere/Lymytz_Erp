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
import javax.xml.bind.annotation.XmlTransient; import com.fasterxml.jackson.annotation.JsonIgnore;import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_base_depots_user")
@NamedQueries({
    @NamedQuery(name = "YvsBaseDepotsUser.findAll", query = "SELECT y FROM YvsBaseDepotsUser y"),
    @NamedQuery(name = "YvsBaseDepotsUser.findById", query = "SELECT y FROM YvsBaseDepotsUser y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseDepotsUser.findExist", query = "SELECT y.actif FROM YvsBaseDepotsUser y WHERE y.users=:user AND y.depot=:depot"),
    @NamedQuery(name = "YvsBaseDepotsUser.findOne", query = "SELECT y FROM YvsBaseDepotsUser y WHERE y.users=:user AND y.depot=:depot"),
    @NamedQuery(name = "YvsBaseDepotsUser.findOneActif", query = "SELECT y.actif FROM YvsBaseDepotsUser y WHERE y.users=:user AND y.depot=:depot"),
    @NamedQuery(name = "YvsBaseDepotsUser.findByDateSave", query = "SELECT y FROM YvsBaseDepotsUser y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseDepotsUser.findByDateUpdate", query = "SELECT y FROM YvsBaseDepotsUser y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseDepotsUser.findByActif", query = "SELECT y FROM YvsBaseDepotsUser y WHERE y.actif = :actif"),

    @NamedQuery(name = "YvsBaseDepotsUser.findIdDepotByUser", query = "SELECT DISTINCT y.depot.id FROM YvsBaseDepotsUser y WHERE y.users=:user AND y.actif=true"),
    @NamedQuery(name = "YvsBaseDepotsUser.findDepotByUsers", query = "SELECT DISTINCT y.depot FROM YvsBaseDepotsUser y WHERE y.users = :users AND y.depot.actif=true AND y.actif=true ORDER BY y.depot.agence.id "),
    @NamedQuery(name = "YvsBaseDepotsUser.findDepotByUsersAgence", query = "SELECT DISTINCT y.depot FROM YvsBaseDepotsUser y WHERE y.depot.agence = :agence AND y.users = :users AND y.depot.actif=true AND y.actif=true ORDER BY y.depot.agence.id "),
    @NamedQuery(name = "YvsBaseDepotsUser.findDepotActifByUsers", query = "SELECT DISTINCT y.depot FROM YvsBaseDepotsUser y WHERE y.users = :users AND y.depot.actif = TRUE AND y.actif = TRUE")})
public class YvsBaseDepotsUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_depots_user_id_seq", name = "yvs_base_depots_user_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_depots_user_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepots depot;

    public YvsBaseDepotsUser() {
    }

    public YvsBaseDepotsUser(Long id) {
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

    @XmlTransient  @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient  @JsonIgnore
    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    @XmlTransient  @JsonIgnore
    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
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
        if (!(object instanceof YvsBaseDepotsUser)) {
            return false;
        }
        YvsBaseDepotsUser other = (YvsBaseDepotsUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseDepotsUser[ id=" + id + " ]";
    }

}
