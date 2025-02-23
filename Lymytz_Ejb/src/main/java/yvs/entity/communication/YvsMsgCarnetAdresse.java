/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.communication;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
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
import yvs.entity.users.YvsUsers;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_msg_carnet_adresse")
@NamedQueries({
    @NamedQuery(name = "YvsMsgCarnetAdresse.findAll", query = "SELECT y FROM YvsMsgCarnetAdresse y"),
    @NamedQuery(name = "YvsMsgCarnetAdresse.findById", query = "SELECT y FROM YvsMsgCarnetAdresse y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMsgCarnetAdresse.findByNom", query = "SELECT y FROM YvsMsgCarnetAdresse y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsMsgCarnetAdresse.findByAdresse", query = "SELECT y FROM YvsMsgCarnetAdresse y WHERE y.adresse = :adresse")})
public class YvsMsgCarnetAdresse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_msg_carnet_adresse_id_seq", name = "yvs_msg_carnet_adresse_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_msg_carnet_adresse_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "nom")
    private String nom;
    @Size(max = 2147483647)
    @Column(name = "adresse")
    private String adresse;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;

    public YvsMsgCarnetAdresse() {
    }

    public YvsMsgCarnetAdresse(Integer id) {
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
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
        if (!(object instanceof YvsMsgCarnetAdresse)) {
            return false;
        }
        YvsMsgCarnetAdresse other = (YvsMsgCarnetAdresse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.communication.YvsMsgCarnetAdresse[ id=" + id + " ]";
    }

}
