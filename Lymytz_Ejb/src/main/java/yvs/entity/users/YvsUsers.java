/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComCategoriePersonnel;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.commission.YvsComPlanCommission;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.formulaire.YvsParamUserFormulaire;
import yvs.entity.param.workflow.YvsConnection;
import yvs.entity.stat.dashboard.YvsStatDashboardUsers;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_users")
@NamedQueries({
    @NamedQuery(name = "YvsUsers.counAll", query = "SELECT COUNT(y.id) FROM YvsUsers y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsUsers.findAll", query = "SELECT y FROM YvsUsers y WHERE y.agence.societe = :societe ORDER BY y.nomUsers"),
    @NamedQuery(name = "YvsUsers.findAllUser", query = "SELECT y FROM YvsUsers y  ORDER BY y.nomUsers"),
    @NamedQuery(name = "YvsUsers.findAlls", query = "SELECT y FROM YvsUsers y WHERE y.agence.societe = :societe AND y.actif = :actif ORDER BY y.nomUsers"),
    @NamedQuery(name = "YvsUsers.findByAgence", query = "SELECT y FROM YvsUsers y  LEFT JOIN FETCH y.categorie WHERE y.agence = :agence AND y.actif = :actif ORDER BY y.nomUsers"),
    @NamedQuery(name = "YvsUsers.findBySociete", query = "SELECT y FROM YvsUsers y WHERE y.agence.societe = :societe AND y.actif = :actif ORDER BY y.nomUsers"),
    @NamedQuery(name = "YvsUsers.findByGroupeSociete", query = "SELECT y FROM YvsUsers y WHERE y.agence.societe.groupe = :groupe AND y.actif = :actif ORDER BY y.nomUsers"),
    @NamedQuery(name = "YvsUsers.findById", query = "SELECT y FROM YvsUsers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsUsers.findByIds", query = "SELECT y FROM YvsUsers y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsUsers.findCodeByIds", query = "SELECT y.codeUsers FROM YvsUsers y WHERE y.id IN :ids ORDER BY y.id"),
    @NamedQuery(name = "YvsUsers.findByCode", query = "SELECT y FROM YvsUsers y JOIN FETCH y.agence JOIN FETCH y.agence.societe WHERE y.codeUsers = :codeUsers"),
    @NamedQuery(name = "YvsUsers.findByCodeUsers", query = "SELECT y FROM YvsUsers y WHERE y.codeUsers = :codeUsers AND y.actif = true "),
    @NamedQuery(name = "YvsUsers.findByCodeSociete", query = "SELECT y FROM YvsUsers y WHERE y.codeUsers = :codeUsers AND y.agence.societe=:societe"),
    @NamedQuery(name = "YvsUsers.findByCodeUsersSociete", query = "SELECT y FROM YvsUsers y WHERE y.codeUsers = :codeUsers AND y.agence.societe=:societe AND y.actif = true "),
    @NamedQuery(name = "YvsUsers.findByCodeUsers_", query = "SELECT y FROM YvsUsers y JOIN FETCH y.niveauxAcces JOIN FETCH y.agence JOIN FETCH y.agence.societe WHERE y.codeUsers = :codeUsers AND y.actif = true "),
    @NamedQuery(name = "YvsUsers.findByCodeUsersSociete_", query = "SELECT y FROM YvsUsers y INNER JOIN y.niveauxAcces na INNER JOIN  y.agence a INNER JOIN y.agence.societe s WHERE y.codeUsers = :codeUsers AND y.actif = true "),
    @NamedQuery(name = "YvsUsers.findLikeCodeActif", query = "SELECT y FROM YvsUsers y WHERE y.agence.societe = :societe AND y.codeUsers LIKE :codeUsers AND y.actif = true ORDER BY y.nomUsers"),
    @NamedQuery(name = "YvsUsers.findByPasswordUser", query = "SELECT y FROM YvsUsers y WHERE y.passwordUser = :passwordUser"),
    @NamedQuery(name = "YvsUsers.findBySupp", query = "SELECT y FROM YvsUsers y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsUsers.findByActif", query = "SELECT y FROM YvsUsers y WHERE y.agence.societe = :societe AND y.actif = :actif ORDER BY y.nomUsers"),
    @NamedQuery(name = "YvsUsers.findByAccesMultiAgence", query = "SELECT y FROM YvsUsers y WHERE y.accesMultiAgence = :accesMultiAgence"),
    @NamedQuery(name = "YvsUsers.findByAleaMdp", query = "SELECT y FROM YvsUsers y WHERE y.aleaMdp = :aleaMdp"),
    @NamedQuery(name = "YvsUsers.findByConnecte", query = "SELECT y FROM YvsUsers y WHERE y.connecte = :connecte"),
    @NamedQuery(name = "YvsUsers.findByOnline", query = "SELECT y FROM YvsUsers y WHERE y.agence.societe = :societe AND y.venteOnline = :venteOnline"),
    @NamedQuery(name = "YvsUsers.findByNom", query = "SELECT y FROM YvsUsers y WHERE (LOWER(y.nomUsers) = LOWER(:nomUsers) OR LOWER(y.codeUsers) = LOWER(:nomUsers)) AND y.actif = true ORDER BY y.nomUsers"),
    @NamedQuery(name = "YvsUsers.findByNomUsers", query = "SELECT y FROM YvsUsers y WHERE (y.nomUsers = :nomUsers OR y.codeUsers = :nomUsers) AND y.actif = true ORDER BY y.nomUsers")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsUsers extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_users_id_seq", name = "yvs_users_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_users_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave = new Date();
    @Size(max = 255)
    @Column(name = "code_users")
    private String codeUsers;
    @Size(max = 255)
    @Column(name = "password_user")
    private String passwordUser;
    @Size(max = 255)
    @Column(name = "photo")
    private String photo;
    @Column(name = "civilite")
    private String civilite;
    @Column(name = "abbreviation")
    private String abbreviation;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "vente_online")
    private Boolean venteOnline = false;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "acces_multi_agence")
    private Boolean accesMultiAgence = true;
    @Column(name = "acces_multi_societe")
    private Boolean accesMultiSociete = false;
    @Column(name = "connect_online_planning")
    private Boolean connectOnlinePlanning = false;
    @Size(max = 2147483647)
    @Column(name = "alea_mdp")
    private String aleaMdp;
    @Column(name = "connecte")
    private Boolean connecte = false;
    @Size(max = 2147483647)
    @Column(name = "nom_users")
    private String nomUsers;
    @Column(name = "super_admin")
    private Boolean superAdmin = false;

    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComCategoriePersonnel categorie;
    @JoinColumn(name = "plan_commission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComPlanCommission planCommission;

    @OneToOne(mappedBy = "users", fetch = FetchType.LAZY)
    private YvsUsersValidite validite;

    @OneToOne(mappedBy = "codeUsers", fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @OneToOne(mappedBy = "utilisateur", fetch = FetchType.LAZY)
    private YvsComComerciale commercial;
    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY)
    private List<YvsParamUserFormulaire> mesFormulaires;
    @OneToMany(mappedBy = "idUser", fetch = FetchType.LAZY)
    private List<YvsNiveauUsers> niveauxAcces;
    @OneToMany(mappedBy = "users")
    private List<YvsStatDashboardUsers> dashboards;

    @Transient
    private YvsComClient client;
    @Transient
    private String message;
    @Transient
    private YvsNiveauAcces niveauAcces;
    @Transient
    private YvsBaseCaisse caisse;
    @Transient
    private YvsBaseDepots depot;
    @Transient
    private YvsBasePointVente point;
    @Transient
    private boolean new_;
    @Transient
    private boolean defaut;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean error;
    @Transient
    private String nom;
    @Transient
    private String idSession;
    @Transient
    private boolean connected;

    public YvsUsers() {
        dashboards = new ArrayList<>();
    }

    public YvsUsers(Long id) {
        this();
        this.id = id;
    }

    public YvsUsers(Long id, String nomUsers) {
        this(id);
        this.nomUsers = nomUsers;
    }

    public YvsUsers(Long id, String codeUsers, String nomUsers) {
        this(id, nomUsers);
        this.codeUsers = codeUsers;
    }

    public YvsUsers(Long id, String codeUsers, String nomUsers, YvsAgences agence, boolean actif) {
        this(id, nomUsers, codeUsers);
        this.actif = actif;
        this.agence = agence;
    }

    public YvsUsers(Long id, String codeUsers, String civilite, String nomUsers) {
        this(id, codeUsers, nomUsers);
        this.civilite = civilite;
    }

    public YvsUsers(Long id, String codeUsers, String civilite, String nomUsers, YvsNiveauAcces niveauAcces) {
        this(id, codeUsers, civilite, nomUsers);
        this.niveauAcces = niveauAcces;
    }

    public Boolean getVenteOnline() {
        return venteOnline != null ? venteOnline : false;
    }

    public void setVenteOnline(Boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Boolean getConnectOnlinePlanning() {
        return connectOnlinePlanning != null ? connectOnlinePlanning : false;
    }

    public void setConnectOnlinePlanning(Boolean connectOnlinePlanning) {
        this.connectOnlinePlanning = connectOnlinePlanning;
    }

    public boolean isConnected() {
//        connected = getConnection() != null ? getConnection().getId() > 0 : false;
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCodeUsers() {
        return codeUsers;
    }

    public void setCodeUsers(String codeUsers) {
        this.codeUsers = codeUsers;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public Boolean getAccesMultiAgence() {
        return accesMultiAgence;
    }

    public void setAccesMultiAgence(Boolean accesMultiAgence) {
        this.accesMultiAgence = accesMultiAgence;
    }

    public String getAleaMdp() {
        return aleaMdp;
    }

    public void setAleaMdp(String aleaMdp) {
        this.aleaMdp = aleaMdp;
    }

    public Boolean getConnecte() {
        return connecte;
    }

    public void setConnecte(Boolean connecte) {
        this.connecte = connecte;
    }

    public String getNom() {
        nom = getNomUsers().trim().length() < 16 ? getNomUsers() : getNomUsers().substring(0, 15);
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomUsers() {
        return nomUsers != null ? nomUsers : "";
    }

    public void setNomUsers(String nomUsers) {
        this.nomUsers = nomUsers;
    }

    public Boolean getAccesMultiSociete() {
        return accesMultiSociete;
    }

    public void setAccesMultiSociete(Boolean accesMultiSociete) {
        this.accesMultiSociete = accesMultiSociete;
    }

    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public String getCivilite() {
        return civilite != null ? civilite.trim().length() > 0 ? civilite : "M" : "M";
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public Boolean getSuperAdmin() {
        return superAdmin != null ? superAdmin : false;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public YvsComCategoriePersonnel getCategorie() {
        return categorie;
    }

    public void setCategorie(YvsComCategoriePersonnel categorie) {
        this.categorie = categorie;
    }

    public YvsComPlanCommission getPlanCommission() {
        return planCommission;
    }

    public void setPlanCommission(YvsComPlanCommission planCommission) {
        this.planCommission = planCommission;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersValidite getValidite() {
        return validite;
    }

    public void setValidite(YvsUsersValidite validite) {
        this.validite = validite;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public YvsNiveauAcces getNiveauAcces() {
        return niveauAcces;
    }

    public void setNiveauAcces(YvsNiveauAcces niveauAcces) {
        this.niveauAcces = niveauAcces;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBasePointVente getPoint() {
        return point;
    }

    public void setPoint(YvsBasePointVente point) {
        this.point = point;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComComerciale getCommercial() {
        return commercial;
    }

    public void setCommercial(YvsComComerciale commercial) {
        this.commercial = commercial;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsNiveauUsers> getNiveauxAcces() {
        return niveauxAcces;
    }

    public void setNiveauxAcces(List<YvsNiveauUsers> niveauxAcces) {
        this.niveauxAcces = niveauxAcces;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsParamUserFormulaire> getMesFormulaires() {
        return mesFormulaires;
    }

    public void setMesFormulaires(List<YvsParamUserFormulaire> mesFormulaires) {
        this.mesFormulaires = mesFormulaires;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsStatDashboardUsers> getDashboards() {
        return dashboards;
    }

    public void setDashboards(List<YvsStatDashboardUsers> dashboards) {
        this.dashboards = dashboards;
    }

    @XmlTransient
    @JsonIgnore
    public YvsConnection getConnection() {
//        connection = null;
//        setIdSession(null);
//        for (YvsConnection c : connections) {
//            if (c.getIdSession() != null ? c.getIdSession().trim().length() > 0 : false) {
//                connection = c;
//                setIdSession(c.getIdSession());
//                break;
//            }
//        }
//        return connection;
        return new YvsConnection();
    }

    public void setConnection(YvsConnection connection) {
//        this.connection = connection;
    }

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
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
        if (!(object instanceof YvsUsers)) {
            return false;
        }
        YvsUsers other = (YvsUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsUsers[ id=" + id + " ]";
    }
}
