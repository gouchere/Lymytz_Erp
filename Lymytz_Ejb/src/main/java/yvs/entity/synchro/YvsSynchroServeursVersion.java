/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.synchro;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_synchro_serveurs_version")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsSynchroServeursVersion.findAll", query = "SELECT y FROM YvsSynchroServeursVersion y"),
    @NamedQuery(name = "YvsSynchroServeursVersion.findById", query = "SELECT y FROM YvsSynchroServeursVersion y WHERE y.id = :id"),
    @NamedQuery(name = "YvsSynchroServeursVersion.findByAdresse", query = "SELECT y FROM YvsSynchroServeursVersion y WHERE y.adresse = :adresse"),
    @NamedQuery(name = "YvsSynchroServeursVersion.findByVersion", query = "SELECT y FROM YvsSynchroServeursVersion y WHERE y.version = :version")
})
public class YvsSynchroServeursVersion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_synchro_serveurs_version_id_seq", name = "yvs_synchro_serveurs_version_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_synchro_serveurs_version_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "adresse")
    private String adresse;
    @Size(max = 2147483647)
    @Column(name = "version")
    private String version;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    public YvsSynchroServeursVersion() {
    }

    public YvsSynchroServeursVersion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVersion() {
        return version != null ? !version.trim().isEmpty() ? version : "00.01" : "00.01";
    }

    public void setVersion(String version) {
        this.version = version;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsSynchroServeursVersion)) {
            return false;
        }
        YvsSynchroServeursVersion other = (YvsSynchroServeursVersion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.synchro.YvsSynchroServeursVersion[ id=" + id + " ]";
    }

}
