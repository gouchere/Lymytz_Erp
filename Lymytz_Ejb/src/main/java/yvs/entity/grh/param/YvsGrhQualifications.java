/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_grh_qualifications")
@NamedQueries({
    @NamedQuery(name = "YvsQualifications.findAll", query = "SELECT y FROM YvsGrhQualifications y WHERE y.domaine.societe=:societe ORDER BY y.domaine.titreDomaine ASC, y.designation"),
    @NamedQuery(name = "YvsQualifications.findByCode", query = "SELECT y FROM YvsGrhQualifications y WHERE y.domaine.societe=:societe AND ( y.codeInterne LIKE :code OR y.designation LIKE :code) ORDER BY y.domaine.titreDomaine ASC, y.designation"),
    @NamedQuery(name = "YvsQualifications.findById", query = "SELECT y FROM YvsGrhQualifications y WHERE y.id = :id"),
    @NamedQuery(name = "YvsQualifications.findByDesignation", query = "SELECT y FROM YvsGrhQualifications y WHERE y.designation = :designation AND y.domaine.societe=:societe"),
    @NamedQuery(name = "YvsQualifications.findByDescription", query = "SELECT y FROM YvsGrhQualifications y WHERE y.description = :description"),
    @NamedQuery(name = "YvsQualifications.findByFile", query = "SELECT y FROM YvsGrhQualifications y WHERE y.file = :file")})
public class YvsGrhQualifications implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_qualifications_id_seq", name = "yvs_qualifications_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_qualifications_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "supp")
    private Boolean supp;
    @Size(max = 2147483647)
    @Column(name = "code_interne")
    private String codeInterne;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "file")
    private String file;

    @JoinColumn(name = "domaine", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhDomainesQualifications domaine;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhQualifications() {
    }

    public YvsGrhQualifications(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getCodeInterne() {
        return codeInterne;
    }

    public void setCodeInterne(String codeInterne) {
        this.codeInterne = codeInterne;
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
        if (!(object instanceof YvsGrhQualifications)) {
            return false;
        }
        YvsGrhQualifications other = (YvsGrhQualifications) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsQualifications[ id=" + id + " ]";
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return (actif != null) ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhDomainesQualifications getDomaine() {
        return domaine;
    }

    public void setDomaine(YvsGrhDomainesQualifications domaine) {
        this.domaine = domaine;
    }

}
