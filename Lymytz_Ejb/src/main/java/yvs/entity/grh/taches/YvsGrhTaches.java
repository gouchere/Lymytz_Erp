/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.taches;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_grh_taches")
@NamedQueries({
    @NamedQuery(name = "YvsTaches.findAll", query = "SELECT y FROM YvsGrhTaches y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsTaches.findById", query = "SELECT y FROM YvsGrhTaches y WHERE y.id = :id"),
    @NamedQuery(name = "YvsTaches.findByDescription", query = "SELECT y FROM YvsGrhTaches y WHERE y.description = :description"),
    @NamedQuery(name = "YvsTaches.findByModuleTache", query = "SELECT y FROM YvsGrhTaches y WHERE y.moduleTache = :moduleTache"),
    @NamedQuery(name = "YvsTaches.findByCodeModule", query = "SELECT y FROM YvsGrhTaches y WHERE y.codeTache like :codeTache OR y.moduleTache like :codeTache"),
    @NamedQuery(name = "YvsTaches.findByCodeTache", query = "SELECT y FROM YvsGrhTaches y WHERE y.codeTache = :codeTache"),
    @NamedQuery(name = "YvsTaches.findBySupp", query = "SELECT y FROM YvsGrhTaches y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsTaches.findByActif", query = "SELECT y FROM YvsGrhTaches y WHERE y.actif = :actif")})
public class YvsGrhTaches implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_taches_id_seq", name = "yvs_taches_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_taches_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "module_tache")
    private String moduleTache;
    @Size(max = 2147483647)
    @Column(name = "code_tache")
    private String codeTache;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhTaches() {
    }

    public YvsGrhTaches(Long id) {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModuleTache() {
        return moduleTache;
    }

    public void setModuleTache(String moduleTache) {
        this.moduleTache = moduleTache;
    }

    public String getCodeTache() {
        return codeTache;
    }

    public void setCodeTache(String codeTache) {
        this.codeTache = codeTache;
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

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
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
        if (!(object instanceof YvsGrhTaches)) {
            return false;
        }
        YvsGrhTaches other = (YvsGrhTaches) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsTaches[ id=" + id + " ]";
    }

}
