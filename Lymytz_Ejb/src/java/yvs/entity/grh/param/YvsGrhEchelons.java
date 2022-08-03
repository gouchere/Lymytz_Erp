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
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_grh_echelons")
@NamedQueries({
    @NamedQuery(name = "YvsEchelons.findAll", query = "SELECT y FROM YvsGrhEchelons y WHERE y.societe=:societe AND y.actif=true ORDER BY y.degre ASC"),
    @NamedQuery(name = "YvsEchelons.findAll1", query = "SELECT y.echelon FROM YvsGrhEchelons y WHERE y.societe=:societe ORDER BY y.echelon"),
    @NamedQuery(name = "YvsEchelons.findById", query = "SELECT y FROM YvsGrhEchelons y WHERE y.id = :id"),
    @NamedQuery(name = "YvsEchelons.findByEchelonSociete", query = "SELECT y FROM YvsGrhEchelons y WHERE y.echelon = :echelon AND y.societe = :societe"),
    @NamedQuery(name = "YvsEchelons.findByEchelon", query = "SELECT y FROM YvsGrhEchelons y WHERE y.echelon = :echelon"),
    @NamedQuery(name = "YvsEchelons.count", query = "SELECT count(y) FROM YvsGrhEchelons y WHERE y.echelon = :echelon"),
    @NamedQuery(name = "YvsEchelons.findByDescription", query = "SELECT y FROM YvsGrhEchelons y WHERE y.description = :description")})
public class YvsGrhEchelons implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_echelons_id_seq")
    @SequenceGenerator(sequenceName = "yvs_echelons_id_seq", allocationSize = 1, name = "yvs_echelons_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "echelon")
    private String echelon;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "degre")
    private Integer degre;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhEchelons() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhEchelons(Integer id) {
        this();
        this.id = id;
    }

    public YvsGrhEchelons(Integer id, String echelon) {
        this(id);
        this.echelon = echelon;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEchelon() {
        return echelon;
    }

    public void setEchelon(String echelon) {
        this.echelon = echelon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsGrhEchelons)) {
            return false;
        }
        YvsGrhEchelons other = (YvsGrhEchelons) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsEchelons[ id=" + id + " ]";
    }

    public Integer getDegre() {
        return degre != null ? degre : 0;
    }

    public void setDegre(Integer degre) {
        this.degre = degre;
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

}
