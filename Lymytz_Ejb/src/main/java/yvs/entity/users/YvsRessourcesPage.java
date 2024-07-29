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
import javax.validation.constraints.Size;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_ressources_page")
@NamedQueries({
    @NamedQuery(name = "YvsRessourcesPage.findAll", query = "SELECT y FROM YvsRessourcesPage y"),
    @NamedQuery(name = "YvsRessourcesPage.findById", query = "SELECT y FROM YvsRessourcesPage y WHERE y.id = :id"),
    @NamedQuery(name = "YvsRessourcesPage.findByReference", query = "SELECT y FROM YvsRessourcesPage y WHERE y.referenceRessource= :reference"),
    @NamedQuery(name = "YvsRessourcesPage.findByLibelle", query = "SELECT y FROM YvsRessourcesPage y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsRessourcesPage.findByPage", query = "SELECT y FROM YvsRessourcesPage y WHERE y.pageModule = :pageModule"),
    @NamedQuery(name = "YvsRessourcesPage.findByModules", query = "SELECT y FROM YvsRessourcesPage y WHERE y.pageModule.module.id IN :modules"),
    @NamedQuery(name = "YvsRessourcesPage.findByDescription", query = "SELECT y FROM YvsRessourcesPage y WHERE y.description = :description")})
public class YvsRessourcesPage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_ressources_page_id_seq", name = "yvs_ressources_page_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_ressources_page_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference_ressource")
    private String referenceRessource;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "page_module", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsPageModule pageModule;
    @JoinColumn(name = "groupe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsResourcePageGroup groupe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsRessourcesPage() {
    }

    public YvsRessourcesPage(Integer id) {
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

    public String getReferenceRessource() {
        return referenceRessource;
    }

    public void setReferenceRessource(String referenceRessource) {
        this.referenceRessource = referenceRessource;
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

    public YvsPageModule getPageModule() {
        return pageModule;
    }

    public void setPageModule(YvsPageModule pageModule) {
        this.pageModule = pageModule;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsResourcePageGroup getGroupe() {
        return groupe;
    }

    public void setGroupe(YvsResourcePageGroup groupe) {
        this.groupe = groupe;
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
        if (!(object instanceof YvsRessourcesPage)) {
            return false;
        }
        YvsRessourcesPage other = (YvsRessourcesPage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsRessourcesPage[ id=" + id + " ]";
    }

}
