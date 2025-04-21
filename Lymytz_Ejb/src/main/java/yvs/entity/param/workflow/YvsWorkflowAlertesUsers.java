/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

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
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_workflow_alertes_users", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsWorkflowAlertesUsers.findAll", query = "SELECT y FROM YvsWorkflowAlertesUsers y"),
    @NamedQuery(name = "YvsWorkflowAlertesUsers.findById", query = "SELECT y FROM YvsWorkflowAlertesUsers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsWorkflowAlertesUsers.findByVoir", query = "SELECT y FROM YvsWorkflowAlertesUsers y WHERE y.voir = :voir"),
    @NamedQuery(name = "YvsWorkflowAlertesUsers.findByDateSave", query = "SELECT y FROM YvsWorkflowAlertesUsers y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsWorkflowAlertesUsers.findByDateUpdate", query = "SELECT y FROM YvsWorkflowAlertesUsers y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsWorkflowAlertesUsers.findByExecuteTrigger", query = "SELECT y FROM YvsWorkflowAlertesUsers y WHERE y.executeTrigger = :executeTrigger"),
    
    @NamedQuery(name = "YvsWorkflowAlertesUsers.findByAlerteUsers", query = "SELECT y FROM YvsWorkflowAlertesUsers y WHERE y.alerte = :alerte AND y.users = :users")})
public class YvsWorkflowAlertesUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @SequenceGenerator(sequenceName = "yvs_workflow_alertes_users_id_seq", name = "yvs_workflow_alertes_users_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_workflow_alertes_users_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "voir")
    private Boolean voir;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Size(max = 2147483647)
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @JoinColumn(name = "alerte", referencedColumnName = "id")
    @ManyToOne
    private YvsWorkflowAlertes alerte;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers users;

    @Transient
    public static long ids = -1;

    public YvsWorkflowAlertesUsers() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsWorkflowAlertesUsers(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getVoir() {
        return voir != null ? voir : true;
    }

    public void setVoir(Boolean voir) {
        this.voir = voir;
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

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    public YvsWorkflowAlertes getAlerte() {
        return alerte;
    }

    public void setAlerte(YvsWorkflowAlertes alerte) {
        this.alerte = alerte;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsWorkflowAlertesUsers)) {
            return false;
        }
        YvsWorkflowAlertesUsers other = (YvsWorkflowAlertesUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsWorkflowAlertesUsers[ id=" + id + " ]";
    }

}
