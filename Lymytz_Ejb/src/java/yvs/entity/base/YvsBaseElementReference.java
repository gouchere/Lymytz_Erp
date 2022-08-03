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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_element_reference")
@NamedQueries({
    @NamedQuery(name = "YvsBaseElementReference.findAll", query = "SELECT y FROM YvsBaseElementReference y ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseElementReference.findById", query = "SELECT y FROM YvsBaseElementReference y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseElementReference.findByDesignation", query = "SELECT y FROM YvsBaseElementReference y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseElementReference.findByModule", query = "SELECT y FROM YvsBaseElementReference y WHERE y.module = :module ORDER BY y.designation")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseElementReference  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_element_reference_id_seq", name = "yvs_base_element_reference_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_element_reference_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "module")
    private String module;
    @Column(name = "model_courant")
    private Boolean modelCourant;
    @Column(name = "default_prefix")
    private String defaultPrefix;

    public YvsBaseElementReference() {
    }

    public YvsBaseElementReference(Long id) {
        this.id = id;
    }

    public YvsBaseElementReference(Long id, String des) {
        this.id = id;
        this.designation = des;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getModule() {
        return module != null ? module.trim().length() > 0 ? module : "COM" : "COM";
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Boolean getModelCourant() {
        return modelCourant != null ? modelCourant : false;
    }

    public void setModelCourant(Boolean modelCourant) {
        this.modelCourant = modelCourant;
    }

    public String getDefaultPrefix() {
        return defaultPrefix;
    }

    public void setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsBaseElementReference)) {
            return false;
        }
        YvsBaseElementReference other = (YvsBaseElementReference) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseElementReference[ id=" + id + " ]";
    }

}
