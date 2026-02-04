/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import yvs.adapter.users.CYvsUsersAgence;
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsAgences;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_users_agence")
@NamedQueries({
    @NamedQuery(name = "YvsUsersAgence.findAll", query = "SELECT y FROM YvsUsersAgence y"),
    @NamedQuery(name = "YvsUsersAgence.findByAgence", query = "SELECT y FROM YvsUsersAgence y WHERE y.agence=:agence"),
    @NamedQuery(name = "YvsUsersAgence.findByUsersSysteme", query = "SELECT y FROM YvsUsersAgence y JOIN FETCH y.agence JOIN FETCH y.users JOIN FETCH y.agence.societe WHERE y.agence.societe=:societe AND y.users=:users"),
    @NamedQuery(name = "YvsUsersAgence.findByUser", query = "SELECT y FROM YvsUsersAgence y WHERE y.users=:users"),
    @NamedQuery(name = "YvsUsersAgence.findByUsersAgence", query = "SELECT y FROM YvsUsersAgence y JOIN FETCH y.agence JOIN FETCH y.users JOIN FETCH y.agence.societe WHERE y.agence=:agence AND y.users=:user"),
    @NamedQuery(name = "YvsUsersAgence.findById", query = "SELECT y FROM YvsUsersAgence y WHERE y.id = :id"),
    @NamedQuery(name = "YvsUsersAgence.findUsageByUsersSociete", query = "SELECT y FROM YvsUsersAgence y JOIN FETCH y.agence WHERE y.actif = TRUE AND y.users = :users AND y.agence.societe = :societe ORDER BY y.agence.designation"),
    @NamedQuery(name = "YvsUsersAgence.findIdUsageByUsersSociete", query = "SELECT y.id FROM YvsUsersAgence y WHERE y.actif = TRUE AND y.users = :users AND y.agence.societe = :societe ORDER BY y.agence.designation"),

    @NamedQuery(name = "YvsUsersAgence.findAgenceByUsers", query = "SELECT DISTINCT y.agence FROM YvsUsersAgence y WHERE y.actif = TRUE AND y.users = :users ORDER BY y.agence.designation"),
    @NamedQuery(name = "YvsUsersAgence.findAgenceByUsersSociete", query = "SELECT DISTINCT y.agence FROM YvsUsersAgence y WHERE y.actif = TRUE AND y.users = :users AND y.agence.societe = :societe ORDER BY y.agence.designation"),
    @NamedQuery(name = "YvsUsersAgence.findAgenceActionByUsersSociete", query = "SELECT DISTINCT y.agence FROM YvsUsersAgence y WHERE y.actif = TRUE AND y.canAction = TRUE AND y.users = :users AND y.agence.societe = :societe ORDER BY y.agence.designation")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsUsersAgence extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @SequenceGenerator(sequenceName = "yvs_users_agence_id_seq", name = "yvs_users_agence_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_users_agence_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "can_action")
    private Boolean canAction;
    @Column(name = "connecte")
    private Boolean connecte;
    @Column(name = "user_systeme")
    private Boolean userSysteme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users", referencedColumnName = "id")
    private YvsUsers users;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence", referencedColumnName = "id")
    private YvsAgences agence;
    @JsonSerialize(converter = CYvsUsersAgence.class)
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private Date lastConnexion;
    @Transient
    private Date firstConnexion;
    @Transient
    private boolean mustChangePassword;
    @Transient
    private boolean smallNavigation;
    @Transient
    public static long ids = -1;
    @Transient
    private int nombreJourRestantDefaultPassWord = 30;

    public YvsUsersAgence() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsUsersAgence(Long id) {
        this();
        this.id = id;
    }

    public YvsUsersAgence(Long id, YvsUsers users) {
        this(id);
        this.users = users;
    }

    public YvsUsersAgence(YvsUsers users, YvsAgences agence) {
        this();
        this.users = users;
        this.agence = agence;
    }

    @Override
    public Long getId() {
        return (id == null) ? 0 : id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getLastConnexion() {
        return lastConnexion;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setLastConnexion(Date lastConnexion) {
        this.lastConnexion = lastConnexion;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFirstConnexion() {
        return firstConnexion;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setFirstConnexion(Date firstConnexion) {
        this.firstConnexion = firstConnexion;
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

    public Boolean getConnecte() {
        return connecte != null ? connecte : false;
    }

    public void setConnecte(Boolean connecte) {
        this.connecte = connecte;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getCanAction() {
        return canAction != null ? canAction : false;
    }

    public void setCanAction(Boolean canAction) {
        this.canAction = canAction;
    }

    @XmlTransient
    @JsonIgnore
    public int getNombreJourRestantDefaultPassWord() {
        return nombreJourRestantDefaultPassWord;
    }

    @XmlTransient
    @JsonIgnore
    public void setNombreJourRestantDefaultPassWord(int nombreJourRestantDefaultPassWord) {
        this.nombreJourRestantDefaultPassWord = nombreJourRestantDefaultPassWord;
    }

    @XmlTransient
    @JsonIgnore
    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    @XmlTransient
    @JsonIgnore
    public boolean isSmallNavigation() {
        return smallNavigation;
    }

    public void setSmallNavigation(boolean smallNavigation) {
        this.smallNavigation = smallNavigation;
    }

    @JsonSerialize(converter = CYvsUsersAgence.class)
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
    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public Boolean getUserSysteme() {
        return userSysteme;
    }

    public void setUserSysteme(Boolean userSysteme) {
        this.userSysteme = userSysteme;
    }

//    @XmlTransient  @JsonIgnore  @JsonIgnore
//    public YvsBaseClassesStat getYvsBaseClassesStat() {
//        return yvsBaseClassesStat;
//    }
//
//    public void setYvsBaseClassesStat(YvsBaseClassesStat yvsBaseClassesStat) {
//        this.yvsBaseClassesStat = yvsBaseClassesStat;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsUsersAgence)) {
            return false;
        }
        YvsUsersAgence other = (YvsUsersAgence) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsUsersAgence[ id=" + id + " ]";
    }
}
