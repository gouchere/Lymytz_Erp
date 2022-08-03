/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.grh.presence;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_pointeuse")
@NamedQueries({
    @NamedQuery(name = "YvsPointeuse.findAll", query = "SELECT y FROM YvsPointeuse y WHERE y.societe = :societe ORDER bY y.adresseIp"),
    @NamedQuery(name = "YvsPointeuse.findAgence", query = "SELECT y FROM YvsPointeuse y WHERE (y.societe = :societe AND y.multiSociete = TRUE) OR y.agence = :agence ORDER bY y.adresseIp"),
    @NamedQuery(name = "YvsPointeuse.findById", query = "SELECT y FROM YvsPointeuse y WHERE y.id = :id"),
    @NamedQuery(name = "YvsPointeuse.findByAdresseIp", query = "SELECT y FROM YvsPointeuse y WHERE y.adresseIp = :adresseIp"),
    @NamedQuery(name = "YvsPointeuse.findByPort", query = "SELECT y FROM YvsPointeuse y WHERE y.port = :port"),
    @NamedQuery(name = "YvsPointeuse.findByDescription", query = "SELECT y FROM YvsPointeuse y WHERE y.description = :description"),
    @NamedQuery(name = "YvsPointeuse.findByEmplacement", query = "SELECT y FROM YvsPointeuse y WHERE y.emplacement = :emplacement"),
    @NamedQuery(name = "YvsPointeuse.findByConnecter", query = "SELECT y FROM YvsPointeuse y WHERE y.connecter = :connecter"),
    @NamedQuery(name = "YvsPointeuse.findByActif", query = "SELECT y FROM YvsPointeuse y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsPointeuse.findByIMachine", query = "SELECT y FROM YvsPointeuse y WHERE y.iMachine = :iMachine"),
    @NamedQuery(name = "YvsPointeuse.findByMultiSociete", query = "SELECT y FROM YvsPointeuse y WHERE y.multiSociete = :multiSociete"),
    @NamedQuery(name = "YvsPointeuse.findByType", query = "SELECT y FROM YvsPointeuse y WHERE y.type = :type"),
    @NamedQuery(name = "YvsPointeuse.findByDateUpdate", query = "SELECT y FROM YvsPointeuse y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsPointeuse.findByDateSave", query = "SELECT y FROM YvsPointeuse y WHERE y.dateSave = :dateSave")})
public class YvsPointeuse implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "adresse_ip")
    private String adresseIp;
    @Column(name = "port")
    private Integer port;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "emplacement")
    private String emplacement;
    @Column(name = "connecter")
    private Boolean connecter;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "i_machine")
    private Integer iMachine;
    @Column(name = "multi_societe")
    private Boolean multiSociete;
    @Size(max = 2147483647)
    @Column(name = "type")
    private String type;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne
    private YvsAgences agence;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @Transient
    private boolean select;

    public YvsPointeuse() {
    }

    public YvsPointeuse(Integer id) {
        this.id = id;
    }

    public YvsPointeuse(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public Boolean getConnecter() {
        return connecter;
    }

    public void setConnecter(Boolean connecter) {
        this.connecter = connecter;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Integer getIMachine() {
        return iMachine;
    }

    public void setIMachine(Integer iMachine) {
        this.iMachine = iMachine;
    }

    public Boolean getMultiSociete() {
        return multiSociete;
    }

    public void setMultiSociete(Boolean multiSociete) {
        this.multiSociete = multiSociete;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Integer getiMachine() {
        return iMachine;
    }

    public void setiMachine(Integer iMachine) {
        this.iMachine = iMachine;
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
        if (!(object instanceof YvsPointeuse)) {
            return false;
        }
        YvsPointeuse other = (YvsPointeuse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.presence.YvsPointeuse[ id=" + id + " ]";
    }
    
}
