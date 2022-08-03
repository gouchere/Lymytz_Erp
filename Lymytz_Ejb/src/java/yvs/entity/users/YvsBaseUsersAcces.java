/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseCodeAcces;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_users_acces")
@NamedQueries({
    @NamedQuery(name = "YvsBaseUsersAcces.findAll", query = "SELECT y FROM YvsBaseUsersAcces y"),
    @NamedQuery(name = "YvsBaseUsersAcces.findById", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseUsersAcces.findByDateSave", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseUsersAcces.findByDateUpdate", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseUsersAcces.findOne", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.users = :users AND y.code = :code"),
    @NamedQuery(name = "YvsBaseUsersAcces.findAcces", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.users = :users AND y.code.code = :code AND y.code.societe = :societe"),
    @NamedQuery(name = "YvsBaseUsersAcces.findIdAcces", query = "SELECT y.id FROM YvsBaseUsersAcces y WHERE y.users = :users AND y.code.code = :code AND y.code.societe = :societe"),
    @NamedQuery(name = "YvsBaseUsersAcces.findIdAccesByUsers", query = "SELECT DISTINCT y.code.id FROM YvsBaseUsersAcces y WHERE y.users = :users AND y.code.societe = :societe"),
    @NamedQuery(name = "YvsBaseUsersAcces.findIdUsersByCode", query = "SELECT DISTINCT y.users.id FROM YvsBaseUsersAcces y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseUsersAcces.findUsersByCode", query = "SELECT DISTINCT y.users FROM YvsBaseUsersAcces y WHERE y.code = :code"),
    @NamedQuery(name = "YvsBaseUsersAcces.findByUsers", query = "SELECT y FROM YvsBaseUsersAcces y WHERE y.users = :users AND y.code.societe = :societe"),

    @NamedQuery(name = "YvsBaseUsersAcces.findUsersByCodeNotUsers", query = "SELECT DISTINCT y.users FROM YvsBaseUsersAcces y JOIN FETCH y.users.agence JOIN FETCH y.users.categorie  WHERE y.users != :users AND y.code = :code")})
public class YvsBaseUsersAcces implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_users_acces_id_seq", name = "yvs_base_users_acces_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_users_acces_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;
    @JoinColumn(name = "code", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCodeAcces code;
    @Transient    
    private List<YvsUsers> utilisateurs;

    public YvsBaseUsersAcces() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.utilisateurs = new ArrayList<>();
    }

    public YvsBaseUsersAcces(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
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

    public YvsBaseCodeAcces getCode() {
        return code;
    }

    public void setCode(YvsBaseCodeAcces code) {
        this.code = code;
    }

    @XmlTransient
    public List<YvsUsers> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<YvsUsers> utilisateurs) {
        this.utilisateurs = utilisateurs;
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
        if (!(object instanceof YvsBaseUsersAcces)) {
            return false;
        }
        YvsBaseUsersAcces other = (YvsBaseUsersAcces) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsBaseUsersAcces[ id=" + id + " ]";
    }

}
