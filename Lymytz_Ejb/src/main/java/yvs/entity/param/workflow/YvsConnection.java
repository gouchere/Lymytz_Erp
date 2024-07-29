/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_connection")
@NamedQueries({
    @NamedQuery(name = "YvsConnection.findAll", query = "SELECT y FROM YvsConnection y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsConnection.findOne", query = "SELECT y FROM YvsConnection y WHERE y.users=:users AND y.adresseIp=:adresse"),
    @NamedQuery(name = "YvsConnection.findByAuthorAdresse", query = "SELECT y FROM YvsConnection y WHERE y.author =:author AND y.adresseIp=:adresse"),
    @NamedQuery(name = "YvsConnection.findByUser", query = "SELECT y FROM YvsConnection y WHERE y.users = :users ORDER BY y.dateConnexion DESC"),
    @NamedQuery(name = "YvsConnection.findNBUser", query = "SELECT COUNT(y) FROM YvsConnection y WHERE y.idSession IS NOT NULL"),
    @NamedQuery(name = "YvsConnection.findByUserDates", query = "SELECT y FROM YvsConnection y WHERE y.users = :users AND y.dateConnexion BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsConnection.findByUserConnect", query = "SELECT y FROM YvsConnection y WHERE y.users = :users AND y.users.connecte = :connecte"),
    @NamedQuery(name = "YvsConnection.findByAgenceUser", query = "SELECT y FROM YvsConnection y WHERE y.agence.societe = :agence AND y.users = :users"),
    @NamedQuery(name = "YvsConnection.findById", query = "SELECT y FROM YvsConnection y WHERE y.id = :id"),
    @NamedQuery(name = "YvsConnection.findByAdresseIp", query = "SELECT y FROM YvsConnection y WHERE y.adresseIp = :adresseIp"),
    @NamedQuery(name = "YvsConnection.findByAdresseMac", query = "SELECT y FROM YvsConnection y WHERE y.adresseMac = :adresseMac"),
    @NamedQuery(name = "YvsConnection.findByDateConnexion", query = "SELECT y FROM YvsConnection y WHERE y.dateConnexion = :dateConnexion"),

    @NamedQuery(name = "YvsConnection.findUsers", query = "SELECT DISTINCT(y.users) FROM YvsConnection y WHERE y.agence.societe = :societe ORDER BY y.dateConnexion"),
    @NamedQuery(name = "YvsConnection.findUsersByAgence", query = "SELECT DISTINCT(y.users) FROM YvsConnection y WHERE y.agence = :agence ORDER BY y.dateConnexion"),
    @NamedQuery(name = "YvsConnection.findIdUsers", query = "SELECT DISTINCT(y.users.id) FROM YvsConnection y WHERE y.agence.societe = :societe AND y.idSession IS NOT NULL ORDER BY y.dateConnexion"),
    @NamedQuery(name = "YvsConnection.findIdUsersByAgence", query = "SELECT DISTINCT(y.users.id) FROM YvsConnection y WHERE y.agence = :agence AND y.idSession IS NOT NULL ORDER BY y.dateConnexion")})
public class YvsConnection implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_connection_id_seq", name = "yvs_connection_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_connection_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "adresse_ip")
    private String adresseIp;
    @Size(max = 2147483647)
    @Column(name = "adresse_mac")
    private String adresseMac;
    @Column(name = "date_connexion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateConnexion;
    @Column(name = "debut_navigation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date debutNavigation;
    @Size(max = 2147483647)
    @Column(name = "id_session")
    private String idSession;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;
    @OneToMany(mappedBy = "auteurPage")
    private List<YvsConnectionPages> pages;
    @Transient
    private String ip;

    public YvsConnection() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsConnection(Long id) {
        this();
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        ip = getAdresseIp() != null ? (getAdresseIp().trim().length() > 0 ? !getAdresseIp().equals("0:0:0:0:0:0:0:1") ? getAdresseIp() : "127.0.0.1" : "127.0.0.1") : "127.0.0.1";
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public String getAdresseMac() {
        return adresseMac;
    }

    public void setAdresseMac(String adresseMac) {
        this.adresseMac = adresseMac;
    }

    public Date getDateConnexion() {
        return dateConnexion;
    }

    public void setDateConnexion(Date dateConnexion) {
        this.dateConnexion = dateConnexion;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public Date getDebutNavigation() {
        return debutNavigation;
    }

    public void setDebutNavigation(Date debutNavigation) {
        this.debutNavigation = debutNavigation;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

    public List<YvsConnectionPages> getPages() {
        return pages;
    }

    public void setPages(List<YvsConnectionPages> pages) {
        this.pages = pages;
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
        if (!(object instanceof YvsConnection)) {
            return false;
        }
        YvsConnection other = (YvsConnection) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsConnection[ id=" + id + " ]";
    }

}
