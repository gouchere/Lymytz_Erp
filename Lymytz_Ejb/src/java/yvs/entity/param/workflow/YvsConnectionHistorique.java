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
import javax.persistence.Entity; import javax.persistence.FetchType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_connection_historique")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsConnectionHistorique.findAll", query = "SELECT y FROM YvsConnectionHistorique y"),
    @NamedQuery(name = "YvsConnectionHistorique.findById", query = "SELECT y FROM YvsConnectionHistorique y WHERE y.id = :id"),
    @NamedQuery(name = "YvsConnectionHistorique.findByAdresseIp", query = "SELECT y FROM YvsConnectionHistorique y WHERE y.adresseIp = :adresseIp"),
    @NamedQuery(name = "YvsConnectionHistorique.findByAdresseMac", query = "SELECT y FROM YvsConnectionHistorique y WHERE y.adresseMac = :adresseMac"),
    @NamedQuery(name = "YvsConnectionHistorique.findByDateConnexion", query = "SELECT y FROM YvsConnectionHistorique y WHERE y.dateConnexion = :dateConnexion"),
    @NamedQuery(name = "YvsConnectionHistorique.findByIdSession", query = "SELECT y FROM YvsConnectionHistorique y WHERE y.idSession = :idSession"),
    @NamedQuery(name = "YvsConnectionHistorique.findByDebutNavigation", query = "SELECT y FROM YvsConnectionHistorique y WHERE y.debutNavigation = :debutNavigation")})
public class YvsConnectionHistorique implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
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
    @Size(max = 2147483647)
    @Column(name = "id_session")
    private String idSession;
    @Column(name = "debut_navigation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date debutNavigation;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @OneToMany(mappedBy = "auteurPage")
    private List<YvsConnectionPagesHistorique> pages;
    @Transient
    private String ip;

    public YvsConnectionHistorique() {
    }

    public YvsConnectionHistorique(Long id) {
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
        return id;
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

    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
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

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsConnectionPagesHistorique> getPages() {
        return pages;
    }

    public void setPages(List<YvsConnectionPagesHistorique> pages) {
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
        if (!(object instanceof YvsConnectionHistorique)) {
            return false;
        }
        YvsConnectionHistorique other = (YvsConnectionHistorique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsConnectionHistorique[ id=" + id + " ]";
    }

}
