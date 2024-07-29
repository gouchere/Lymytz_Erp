/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.proj;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
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
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseDepartement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_proj_departement", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsProjDepartement.findAll", query = "SELECT y FROM YvsProjDepartement y"),
    @NamedQuery(name = "YvsProjDepartement.findById", query = "SELECT y FROM YvsProjDepartement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProjDepartement.findBySupp", query = "SELECT y FROM YvsProjDepartement y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsProjDepartement.findByActif", query = "SELECT y FROM YvsProjDepartement y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsProjDepartement.findByDateUpdate", query = "SELECT y FROM YvsProjDepartement y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsProjDepartement.findByDateSave", query = "SELECT y FROM YvsProjDepartement y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProjDepartement.findByService", query = "SELECT y FROM YvsProjDepartement y WHERE y.service = :service")})
public class YvsProjDepartement extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_proj_departement_id_seq", name = "yvs_proj_departement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_proj_departement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "service", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepartement service;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @Transient
    private boolean insert;
    @Transient
    public static long ids = -1;

    public YvsProjDepartement() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsProjDepartement(Long id) {
        this();
        this.id = id;
    }

    @Override
    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSupp() {
        return supp != null ? supp : false;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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

    @Override
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseDepartement getService() {
        return service;
    }

    public void setService(YvsBaseDepartement service) {
        this.service = service;
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
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
        if (!(object instanceof YvsProjDepartement)) {
            return false;
        }
        YvsProjDepartement other = (YvsProjDepartement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.proj.YvsProjDepartement[ id=" + id + " ]";
    }

}
