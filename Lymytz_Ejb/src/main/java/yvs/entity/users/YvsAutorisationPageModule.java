/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_autorisation_page_module")
@NamedQueries({
    @NamedQuery(name = "YvsAutorisationPageModule.findAll", query = "SELECT y FROM YvsAutorisationPageModule y"),
    @NamedQuery(name = "YvsAutorisationPageModule.findByNiveauAcces", query = "SELECT y FROM YvsAutorisationPageModule y JOIN FETCH y.pageModule WHERE y.niveauAcces = :niveauAcces"),
    @NamedQuery(name = "YvsAutorisationPageModule.findAccesRessource", query = "SELECT y FROM YvsAutorisationPageModule y WHERE y.pageModule.reference=:reference AND y.niveauAcces=:niveau"),
    @NamedQuery(name = "YvsAutorisationPageModule.findById", query = "SELECT y FROM YvsAutorisationPageModule y WHERE y.id=:id"),
    @NamedQuery(name = "YvsAutorisationPageModule.findModule", query = "SELECT COUNT(y) FROM YvsAutorisationPageModule y WHERE y.pageModule.module = :module AND y.niveauAcces = :niveau AND y.acces = true"),
    @NamedQuery(name = "YvsAutorisationPageModule.findByPageModule", query = "SELECT y FROM YvsAutorisationPageModule y WHERE y.pageModule = :pageModule"),
    @NamedQuery(name = "YvsAutorisationPageModule.findByPageNiveau", query = "SELECT y FROM YvsAutorisationPageModule y WHERE y.pageModule = :page AND y.niveauAcces = :niveau"),
    @NamedQuery(name = "YvsAutorisationPageModule.findByAcces", query = "SELECT y FROM YvsAutorisationPageModule y WHERE y.acces = :acces"),
    
    @NamedQuery(name = "YvsAutorisationPageModule.findIdPageByAcces", query = "SELECT DISTINCT y.pageModule.id FROM YvsAutorisationPageModule y WHERE y.acces = :acces AND y.niveauAcces=:niveau")

})
public class YvsAutorisationPageModule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @SequenceGenerator(sequenceName = "yvs_autorisation_page_module_id_seq", name = "yvs_autorisation_page_module_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_autorisation_page_module_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "acces")
    private Boolean acces;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "page_module", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsPageModule pageModule;
    @JoinColumn(name = "niveau_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsNiveauAcces niveauAcces;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsAutorisationPageModule() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsAutorisationPageModule(Long id) {
        this();
        this.id = id;
    }

    public YvsAutorisationPageModule(YvsNiveauAcces niveauAcces, YvsPageModule pageModule) {
        this();
        this.niveauAcces = niveauAcces;
        this.pageModule = pageModule;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAcces() {
        return acces != null ? acces : false;
    }

    public void setAcces(Boolean acces) {
        this.acces = acces;
    }

    public YvsPageModule getPageModule() {
        return pageModule;
    }

    public void setPageModule(YvsPageModule pageModule) {
        this.pageModule = pageModule;
    }

    public YvsNiveauAcces getNiveauAcces() {
        return niveauAcces;
    }

    public void setNiveauAcces(YvsNiveauAcces niveauAcces) {
        this.niveauAcces = niveauAcces;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsAutorisationPageModule other = (YvsAutorisationPageModule) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsAutorisationPageModule[ id=" + id + " ]";
    }

}
