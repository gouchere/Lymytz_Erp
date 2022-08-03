/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_niveau_acces")
@NamedQueries({
    @NamedQuery(name = "YvsNiveauAcces.findAll", query = "SELECT y FROM YvsNiveauAcces y ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findSociete", query = "SELECT y FROM YvsNiveauAcces y WHERE y.societe = :societe AND y.superAdmin = false ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findByGroupeSociete", query = "SELECT y FROM YvsNiveauAcces y WHERE y.societe.groupe = :groupe ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findByGroupeSuper", query = "SELECT y FROM YvsNiveauAcces y WHERE y.societe.groupe = :groupe AND y.superAdmin = :superAdmin ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findLikeDesignation", query = "SELECT y FROM YvsNiveauAcces y WHERE y.societe = :societe AND UPPER(y.designation) LIKE UPPER(:designation) ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findLikeDesignationNotSuper", query = "SELECT y FROM YvsNiveauAcces y WHERE y.societe = :societe AND UPPER(y.designation) LIKE UPPER(:designation) AND y.superAdmin = false ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findBySociete", query = "SELECT y FROM YvsNiveauAcces y WHERE y.societe = :societe ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findBySuperAdmin", query = "SELECT y FROM YvsNiveauAcces y WHERE y.societe = :societe AND y.superAdmin = :superAdmin ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findSuperAdmin", query = "SELECT y FROM YvsNiveauAcces y WHERE y.superAdmin = true ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findById", query = "SELECT y FROM YvsNiveauAcces y WHERE y.id = :id ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findByDesignation", query = "SELECT y FROM YvsNiveauAcces y WHERE y.designation = :designation ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findByDescription", query = "SELECT y FROM YvsNiveauAcces y WHERE y.description = :description ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findBySupp", query = "SELECT y FROM YvsNiveauAcces y WHERE y.supp = :supp ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findByActif", query = "SELECT y FROM YvsNiveauAcces y WHERE y.actif = :actif ORDER BY y.designation"),
    @NamedQuery(name = "YvsNiveauAcces.findByGrade", query = "SELECT y FROM YvsNiveauAcces y WHERE y.grade = :grade ORDER BY y.designation")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsNiveauAcces extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_groupes_id_seq", name = "yvs_groupes_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_groupes_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Size(max = 1000)
    @Column(name = "description")
    private String description;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "super_admin")
    private Boolean superAdmin;

    @JoinColumn(name = "grade", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersGrade grade;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean exist;
    @Transient
    private long idNiveauUsers;

    public YvsNiveauAcces() {
    }

    public YvsNiveauAcces(Long id) {
        this.id = id;
    }

    public YvsNiveauAcces(Long id, String designation) {
        this.id = id;
        this.designation = designation;
    }

    public YvsNiveauAcces(YvsNiveauAcces n) {
        this.id = n.id;
        this.designation = n.designation;
        this.description = n.description;
        this.grade = n.grade;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id != null ? id : 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersGrade getGrade() {
        return grade;
    }

    public void setGrade(YvsUsersGrade grade) {
        this.grade = grade;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public Boolean getSuperAdmin() {
        return superAdmin != null ? superAdmin : false;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public long getIdNiveauUsers() {
        return idNiveauUsers;
    }

    public void setIdNiveauUsers(long idNiveauUsers) {
        this.idNiveauUsers = idNiveauUsers;
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
        if (!(object instanceof YvsNiveauAcces)) {
            return false;
        }
        YvsNiveauAcces other = (YvsNiveauAcces) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsNiveauAcces[ id=" + id + " ]";
    }

}
