/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_module")
@NamedQueries({
    @NamedQuery(name = "YvsModule.findAll", query = "SELECT y FROM YvsModule y ORDER BY y.libelle"),
    @NamedQuery(name = "YvsModule.findById", query = "SELECT y FROM YvsModule y WHERE y.id = :id"),
    @NamedQuery(name = "YvsModule.countModule", query = "SELECT COUNT(y) FROM YvsModule y"),
    @NamedQuery(name = "YvsModule.findByLibelle", query = "SELECT y FROM YvsModule y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsModule.findByDescription", query = "SELECT y FROM YvsModule y WHERE y.description = :description"),
    @NamedQuery(name = "YvsModule.findByReference", query = "SELECT y FROM YvsModule y WHERE y.reference = :reference")})
public class YvsModule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_module_id_seq", name = "yvs_module_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_module_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @OneToMany(mappedBy = "module")
    private List<YvsPageModule> yvsPageModuleList;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsModule() {
    }

    public YvsModule(Integer id) {
        this.id = id;
    }

    public YvsModule(Integer id, String libelle) {
        this(id);
        this.libelle = libelle;
    }

    public YvsModule(Integer id, String reference, String libelle, String description) {
        this(id, libelle);
        this.description = description;
        this.reference = reference;
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsPageModule> getYvsPageModuleList() {
        return yvsPageModuleList;
    }

    public void setYvsPageModuleList(List<YvsPageModule> yvsPageModuleList) {
        this.yvsPageModuleList = yvsPageModuleList;
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
        if (!(object instanceof YvsModule)) {
            return false;
        }
        YvsModule other = (YvsModule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsModule[ id=" + id + " ]";
    }
}
