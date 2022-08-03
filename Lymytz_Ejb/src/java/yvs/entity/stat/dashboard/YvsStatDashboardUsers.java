/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.stat.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_stat_dashboard_users")
@NamedQueries({
    @NamedQuery(name = "YvsStatDashboardUsers.findAll", query = "SELECT y FROM YvsStatDashboardUsers y"),
    @NamedQuery(name = "YvsStatDashboardUsers.findById", query = "SELECT y FROM YvsStatDashboardUsers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsStatDashboardUsers.findOne", query = "SELECT y FROM YvsStatDashboardUsers y WHERE y.users = :users AND y.dashboard = :dashboard"),
    @NamedQuery(name = "YvsStatDashboardUsers.findByUsers", query = "SELECT y FROM YvsStatDashboardUsers y WHERE y.users = :users"),
    @NamedQuery(name = "YvsStatDashboardUsers.findByDateSave", query = "SELECT y FROM YvsStatDashboardUsers y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsStatDashboardUsers.findByDateUpdate", query = "SELECT y FROM YvsStatDashboardUsers y WHERE y.dateUpdate = :dateUpdate")})
public class YvsStatDashboardUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @SequenceGenerator(sequenceName = "yvs_stat_dashboard_users_id_seq", name = "yvs_stat_dashboard_users_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_stat_dashboard_users_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers users;
    @JoinColumn(name = "dashboard", referencedColumnName = "id")
    @ManyToOne
    private YvsStatDashboard dashboard;
    @Transient
    private boolean acces = true;

    public YvsStatDashboardUsers() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsStatDashboardUsers(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active != null ? active : false;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public YvsStatDashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(YvsStatDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public boolean isAcces() {
        return acces;
    }

    public void setAcces(boolean acces) {
        this.acces = acces;
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
        if (!(object instanceof YvsStatDashboardUsers)) {
            return false;
        }
        YvsStatDashboardUsers other = (YvsStatDashboardUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.stat.dashboard.YvsStatDashboardUsers[ id=" + id + " ]";
    }

}
