/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

import java.io.Serializable;
import java.util.Calendar;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_users_favoris")
@NamedQueries({
    @NamedQuery(name = "YvsUsersFavoris.findAll", query = "SELECT y FROM YvsUsersFavoris y"),
    @NamedQuery(name = "YvsUsersFavoris.findById", query = "SELECT y FROM YvsUsersFavoris y WHERE y.id = :id"),
    @NamedQuery(name = "YvsUsersFavoris.findByTitre", query = "SELECT y FROM YvsUsersFavoris y WHERE y.titre = :titre"),
    @NamedQuery(name = "YvsUsersFavoris.findByUsers", query = "SELECT y FROM YvsUsersFavoris y WHERE y.users = :users ORDER BY y.page"),
    @NamedQuery(name = "YvsUsersFavoris.findByPageUsers", query = "SELECT y FROM YvsUsersFavoris y WHERE y.page = :page AND y.users = :users")})
public class YvsUsersFavoris implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_users_favoris_id_seq", name = "yvs_users_favoris_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_users_favoris_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "titre")
    private String titre;
    @Size(max = 2147483647)
    @Column(name = "module")
    private String module;
    @Size(max = 2147483647)
    @Column(name = "page")
    private String page;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;
    
    @Transient
    private boolean select;

    public YvsUsersFavoris() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsUsersFavoris(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
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

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getTitre() {
        return titre != null ? titre : "";
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getModule() {
        return module != null ? module : "";
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPage() {
        return page != null ? page : "";
    }

    public void setPage(String page) {
        this.page = page;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
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
        if (!(object instanceof YvsUsersFavoris)) {
            return false;
        }
        YvsUsersFavoris other = (YvsUsersFavoris) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsMemoUsers[ id=" + id + " ]";
    }

}
