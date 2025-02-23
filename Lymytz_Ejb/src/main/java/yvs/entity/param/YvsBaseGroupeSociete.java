/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_groupe_societe")
@NamedQueries({
    @NamedQuery(name = "YvsBaseGroupeSociete.findAll", query = "SELECT y FROM YvsBaseGroupeSociete y"),
    @NamedQuery(name = "YvsBaseGroupeSociete.findById", query = "SELECT y FROM YvsBaseGroupeSociete y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseGroupeSociete.findByLibelle", query = "SELECT y FROM YvsBaseGroupeSociete y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsBaseGroupeSociete.findByDateSave", query = "SELECT y FROM YvsBaseGroupeSociete y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseGroupeSociete.findByDateUpdate", query = "SELECT y FROM YvsBaseGroupeSociete y WHERE y.dateUpdate = :dateUpdate")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseGroupeSociete implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_groupe_societe_id_seq", name = "yvs_base_groupe_societe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_groupe_societe_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @OneToMany(mappedBy = "groupe")
    private List<YvsSocietes> yvsSocietesList;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsBaseGroupeSociete() {
    }

    public YvsBaseGroupeSociete(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsSocietes> getYvsSocietesList() {
        return yvsSocietesList;
    }

    public void setYvsSocietesList(List<YvsSocietes> yvsSocietesList) {
        this.yvsSocietesList = yvsSocietesList;
    }

    @XmlTransient
    @JsonIgnore
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
        if (!(object instanceof YvsBaseGroupeSociete)) {
            return false;
        }
        YvsBaseGroupeSociete other = (YvsBaseGroupeSociete) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.YvsBaseGroupeSociete[ id=" + id + " ]";
    }

}
