/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_base_caisse_user")
@NamedQueries({
    @NamedQuery(name = "YvsBaseCaisseUser.findAll", query = "SELECT y FROM YvsBaseCaisseUser y"),
    @NamedQuery(name = "YvsBaseCaisseUser.findById", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseCaisseUser.findExist", query = "SELECT y.actif FROM YvsBaseCaisseUser y WHERE y.idUser=:user AND y.idCaisse=:caisse"),
    @NamedQuery(name = "YvsBaseCaisseUser.findOne", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.idUser=:user AND y.idCaisse=:caisse"),
    @NamedQuery(name = "YvsBaseCaisseUser.findIdCaisseByUser", query = "SELECT y.idCaisse.id FROM YvsBaseCaisseUser y WHERE y.idUser=:user AND y.actif=true"),
    @NamedQuery(name = "YvsBaseCaisseUser.findCaisseByUser", query = "SELECT y.idCaisse FROM YvsBaseCaisseUser y WHERE y.idUser=:user AND y.actif=true"),
    @NamedQuery(name = "YvsBaseCaisseUser.findByDateSave", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseCaisseUser.findByDateUpdate", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseCaisseUser.findByActif", query = "SELECT y FROM YvsBaseCaisseUser y WHERE y.actif = :actif"),

    @NamedQuery(name = "YvsBaseCaisseUser.findCaisseByUsers", query = "SELECT DISTINCT y.idCaisse FROM YvsBaseCaisseUser y WHERE y.idUser = :users"),
    @NamedQuery(name = "YvsBaseCaisseUser.findCaisseByUsersAgence", query = "SELECT DISTINCT y.idCaisse FROM YvsBaseCaisseUser y WHERE y.idCaisse.journal.agence = :agence AND y.idUser = :users AND y.actif=true"),
})
public class YvsBaseCaisseUser extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_caisse_user_id_seq", name = "yvs_base_caisse_user_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_caisse_user_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @ManyToOne
    private YvsUsers idUser;
    @JoinColumn(name = "id_caisse", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseCaisse idCaisse;

    public YvsBaseCaisseUser() {
    }

    public YvsBaseCaisseUser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsers getIdUser() {
        return idUser;
    }

    public void setIdUser(YvsUsers idUser) {
        this.idUser = idUser;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseCaisse getIdCaisse() {
        return idCaisse;
    }

    public void setIdCaisse(YvsBaseCaisse idCaisse) {
        this.idCaisse = idCaisse;
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
        if (!(object instanceof YvsBaseCaisseUser)) {
            return false;
        }
        YvsBaseCaisseUser other = (YvsBaseCaisseUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseCaisseUser[ id=" + id + " ]";
    }

}
