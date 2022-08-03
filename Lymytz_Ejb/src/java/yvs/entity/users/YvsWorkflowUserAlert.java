/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.xml.bind.annotation.XmlTransient;
import yvs.entity.param.workflow.YvsWorkflowModelDoc;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_workflow_user_alert")
@NamedQueries({
    @NamedQuery(name = "YvsWorkflowUserAlert.findAll", query = "SELECT y FROM YvsWorkflowUserAlert y"),
    @NamedQuery(name = "YvsWorkflowUserAlert.findAllModel", query = "SELECT y FROM YvsWorkflowUserAlert y LEFT JOIN FETCH y.documentType WHERE y.users=:users ORDER BY y.documentType.titreDoc"),
    @NamedQuery(name = "YvsWorkflowUserAlert.findById", query = "SELECT y FROM YvsWorkflowUserAlert y WHERE y.id = :id"),
    @NamedQuery(name = "YvsWorkflowUserAlert.findByDateSave", query = "SELECT y FROM YvsWorkflowUserAlert y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsWorkflowUserAlert.findByDateUpdate", query = "SELECT y FROM YvsWorkflowUserAlert y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsWorkflowUserAlert.findByActif", query = "SELECT y FROM YvsWorkflowUserAlert y WHERE y.actif = :actif")})
public class YvsWorkflowUserAlert implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_workflow_user_alert_id_seq", name = "yvs_workflow_user_alert_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_workflow_user_alert_id_seq_name", strategy = GenerationType.SEQUENCE)
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

    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;
    @JoinColumn(name = "document_type", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsWorkflowModelDoc documentType;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @Transient
    private long count;
    
    @Transient
    public static long ids = -1;

    public YvsWorkflowUserAlert() {
    }

    public YvsWorkflowUserAlert(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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
    public YvsWorkflowModelDoc getDocumentType() {
        return documentType;
    }

    public void setDocumentType(YvsWorkflowModelDoc documentType) {
        this.documentType = documentType;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
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
        if (!(object instanceof YvsWorkflowUserAlert)) {
            return false;
        }
        YvsWorkflowUserAlert other = (YvsWorkflowUserAlert) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsWorkflowUserAlert[ id=" + id + " ]";
    }

}
