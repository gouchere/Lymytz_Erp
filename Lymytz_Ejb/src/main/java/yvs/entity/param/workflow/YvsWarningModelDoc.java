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
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_warning_model_doc")
@NamedQueries({
    @NamedQuery(name = "YvsWarningModelDoc.findAll", query = "SELECT y FROM YvsWarningModelDoc y"),
    @NamedQuery(name = "YvsWarningModelDoc.findById", query = "SELECT y FROM YvsWarningModelDoc y WHERE y.id = :id"),
    @NamedQuery(name = "YvsWarningModelDoc.findByEcart", query = "SELECT y FROM YvsWarningModelDoc y WHERE y.ecart = :ecart"),
    @NamedQuery(name = "YvsWarningModelDoc.findByDateUpdate", query = "SELECT y FROM YvsWarningModelDoc y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsWarningModelDoc.findByDateSave", query = "SELECT y FROM YvsWarningModelDoc y WHERE y.dateSave = :dateSave"),

    @NamedQuery(name = "YvsWarningModelDoc.findOne", query = "SELECT y FROM YvsWarningModelDoc y WHERE y.model = :model AND y.societe = :societe"),
    @NamedQuery(name = "YvsWarningModelDoc.findEcartModel", query = "SELECT y.ecart FROM YvsWarningModelDoc y WHERE y.model.titreDoc = :model AND y.societe = :societe")})
public class YvsWarningModelDoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_warning_model_doc_id_seq", name = "yvs_warning_model_doc_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_warning_model_doc_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ecart")
    private Integer ecart;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne
    private YvsWorkflowModelDoc model;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;

    public YvsWarningModelDoc() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsWarningModelDoc(Integer id) {
        this();
        this.id = id;
    }

    public Integer getId() {
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEcart() {
        return ecart != null ? ecart : 0;
    }

    public void setEcart(Integer ecart) {
        this.ecart = ecart;
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

    public YvsWorkflowModelDoc getModel() {
        return model;
    }

    public void setModel(YvsWorkflowModelDoc model) {
        this.model = model;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsWarningModelDoc)) {
            return false;
        }
        YvsWarningModelDoc other = (YvsWarningModelDoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsWarningModelDoc[ id=" + id + " ]";
    }

}
