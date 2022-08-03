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
@Table(name = "yvs_autorisation_module")
@NamedQueries({
    @NamedQuery(name = "YvsAutorisationModule.findAll", query = "SELECT y FROM YvsAutorisationModule y"),
    @NamedQuery(name = "YvsAutorisationModule.findByNiveauAcces", query = "SELECT y FROM YvsAutorisationModule y JOIN FETCH y.module WHERE y.niveauAcces = :niveauAcces"),
    @NamedQuery(name = "YvsAutorisationModule.findByModule", query = "SELECT y FROM YvsAutorisationModule y WHERE y.module = :module"),
    @NamedQuery(name = "YvsAutorisationModule.findByModuleNiveau", query = "SELECT y FROM YvsAutorisationModule y WHERE y.module = :module AND y.niveauAcces=:niveau"),
    @NamedQuery(name = "YvsAutorisationModule.findByAcces", query = "SELECT y FROM YvsAutorisationModule y WHERE y.acces = :acces"),
    
    @NamedQuery(name = "YvsAutorisationModule.findIdModuleByAcces", query = "SELECT DISTINCT y.module.id FROM YvsAutorisationModule y WHERE y.acces = :acces AND y.niveauAcces=:niveau")})
public class YvsAutorisationModule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    @SequenceGenerator(sequenceName = "yvs_autorisation_module_id_seq", name = "yvs_autorisation_module_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_autorisation_module_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "acces")
    private Boolean acces;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "module", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsModule module;
    @JoinColumn(name = "niveau_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsNiveauAcces niveauAcces;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsAutorisationModule() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsAutorisationModule(Long id) {
        this();
        this.id = id;
    }

    public YvsAutorisationModule(YvsNiveauAcces niveauAcces, YvsModule module) {
        this();
        this.niveauAcces = niveauAcces;
        this.module = module;
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

    public YvsModule getModule() {
        return module;
    }

    public void setModule(YvsModule module) {
        this.module = module;
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
        final YvsAutorisationModule other = (YvsAutorisationModule) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsAutorisationModule[ id=" + id + " ]";
    }

}
