/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.stock;

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
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_nature_doc")
@NamedQueries({
    @NamedQuery(name = "YvsComNatureDoc.findAlls", query = "SELECT y FROM YvsComNatureDoc y WHERE y.societe=:societe ORDER BY y.nature"),
    @NamedQuery(name = "YvsComNatureDoc.findAll", query = "SELECT y FROM YvsComNatureDoc y WHERE y.societe=:societe AND y.actif=:actif"),
    @NamedQuery(name = "YvsComNatureDoc.findById", query = "SELECT y FROM YvsComNatureDoc y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComNatureDoc.findByNature", query = "SELECT y FROM YvsComNatureDoc y WHERE y.nature = :nature"),
    @NamedQuery(name = "YvsComNatureDoc.findByDescription", query = "SELECT y FROM YvsComNatureDoc y WHERE y.description = :description"),
    @NamedQuery(name = "YvsComNatureDoc.findByDateSave", query = "SELECT y FROM YvsComNatureDoc y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComNatureDoc.findByDateUpdate", query = "SELECT y FROM YvsComNatureDoc y WHERE y.dateUpdate = :dateUpdate")})
public class YvsComNatureDoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_nature_doc_id_seq", name = "yvs_com_nature_doc_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_nature_doc_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "nature")
    private String nature;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;

    public YvsComNatureDoc() {
    }

    public YvsComNatureDoc(Long id) {
        this.id = id;
    }

    public YvsComNatureDoc(Long id, String nature) {
        this.id = id;
        this.nature = nature;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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
        if (!(object instanceof YvsComNatureDoc)) {
            return false;
        }
        YvsComNatureDoc other = (YvsComNatureDoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.stock.YvsComNatureDoc[ id=" + id + " ]";
    }

}
