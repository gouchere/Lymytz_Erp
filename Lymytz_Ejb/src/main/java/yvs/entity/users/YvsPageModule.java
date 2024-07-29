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
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_page_module")
@NamedQueries({
    @NamedQuery(name = "YvsPageModule.findAll", query = "SELECT DISTINCT y FROM YvsPageModule y"),
    @NamedQuery(name = "YvsPageModule.findById", query = "SELECT y FROM YvsPageModule y WHERE y.id = :id"),
    @NamedQuery(name = "YvsPageModule.findByLibelle", query = "SELECT y FROM YvsPageModule y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsPageModule.findByReference", query = "SELECT y FROM YvsPageModule y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsPageModule.findByModule", query = "SELECT y FROM YvsPageModule y WHERE y.module = :module ORDER BY y.libelle"),
    @NamedQuery(name = "YvsPageModule.findByModules", query = "SELECT y FROM YvsPageModule y WHERE y.module.id IN :modules ORDER BY y.libelle"),
    @NamedQuery(name = "YvsPageModule.findByDescription", query = "SELECT y FROM YvsPageModule y WHERE y.description = :description")})
public class YvsPageModule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_page_module_id_seq", name = "yvs_page_module_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_page_module_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "pageModule")
    private List<YvsRessourcesPage> yvsRessourcesPageList;
    @JoinColumn(name = "module", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsModule module;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsPageModule() {
    }

    public YvsPageModule(Integer id) {
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsRessourcesPage> getYvsRessourcesPageList() {
        return yvsRessourcesPageList;
    }

    public void setYvsRessourcesPageList(List<YvsRessourcesPage> yvsRessourcesPageList) {
        this.yvsRessourcesPageList = yvsRessourcesPageList;
    }

    public YvsModule getModule() {
        return module;
    }

    public void setModule(YvsModule module) {
        this.module = module;
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
        if (!(object instanceof YvsPageModule)) {
            return false;
        }
        YvsPageModule other = (YvsPageModule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsPageModule[ id=" + id + " ]";
    }

}
