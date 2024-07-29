/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_agences")
@NamedQueries({
    @NamedQuery(name = "YvsAgences.findDesignation", query = "SELECT y.designation FROM YvsAgences y WHERE y.actif=true and y.supp=false and (y.codeagence = :codeagence OR y.abbreviation=:codeagence)"),
    @NamedQuery(name = "YvsAgences.findAll", query = "SELECT y FROM YvsAgences y WHERE y.actif=true"),
    @NamedQuery(name = "YvsAgences.findById", query = "SELECT y FROM YvsAgences y LEFT JOIN FETCH y.secteurActivite WHERE y.id = :id"),
    @NamedQuery(name = "YvsAgences.countAll", query = "SELECT COUNT(y) FROM YvsAgences y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsAgences.findByAbbreviation", query = "SELECT y FROM YvsAgences y WHERE y.abbreviation = :abbreviation"),
    @NamedQuery(name = "YvsAgences.findByActif", query = "SELECT y FROM YvsAgences y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsAgences.findByAdresse", query = "SELECT y FROM YvsAgences y WHERE y.adresse = :adresse"),
    @NamedQuery(name = "YvsAgences.findByCodeagence", query = "SELECT y FROM YvsAgences y WHERE y.codeagence = :codeagence AND y.societe = :societe"),
    @NamedQuery(name = "YvsAgences.findByDesignation", query = "SELECT y FROM YvsAgences y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsAgences.findBySupp", query = "SELECT y FROM YvsAgences y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsAgences.findByVille", query = "SELECT y FROM YvsAgences y WHERE y.ville = :ville"),
    @NamedQuery(name = "YvsAgences.findByAuthor", query = "SELECT y FROM YvsAgences y WHERE y.author = :author"),
    @NamedQuery(name = "YvsAgences.findByLastAuthor", query = "SELECT y FROM YvsAgences y WHERE y.lastAuthor = :lastAuthor"),
    @NamedQuery(name = "YvsAgences.findByDateSave", query = "SELECT y FROM YvsAgences y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsAgences.findUsable", query = "SELECT y FROM YvsAgences y WHERE y.societe = :societe AND y.actif = true and y.supp=false"),
    @NamedQuery(name = "YvsAgences.findUsableByGroupe", query = "SELECT y FROM YvsAgences y WHERE y.societe.groupe = :groupe AND y.actif = true AND y.societe.actif = true and y.supp=false"),
    @NamedQuery(name = "YvsAgences.findBySocieteAll", query = "SELECT y FROM YvsAgences y WHERE y.actif=true AND y.societe = :societe ORDER BY y.codeagence ASC"),
    @NamedQuery(name = "YvsAgences.findBySociete", query = "SELECT y FROM YvsAgences y WHERE y.actif = true and COALESCE(y.supp, FALSE) = false AND y.societe = :societe ORDER BY y.codeagence ASC"),
    @NamedQuery(name = "YvsAgences.findByLastDateSave", query = "SELECT y FROM YvsAgences y WHERE y.lastDateSave = :lastDateSave"),

    @NamedQuery(name = "YvsAgences.findPrevByNom", query = "SELECT y FROM YvsAgences y WHERE y.societe = :societe AND y.designation < :designation ORDER BY y.designation DESC"),
    @NamedQuery(name = "YvsAgences.findNextByNom", query = "SELECT y FROM YvsAgences y WHERE y.societe = :societe AND y.designation > :designation ORDER BY y.designation")})
public class YvsAgences extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    @SequenceGenerator(sequenceName = "yvs_agences_id_seq", name = "yvs_agences_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_agences_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Size(max = 255)
    @Column(name = "abbreviation")
    private String abbreviation;
    @Size(max = 255)
    @Column(name = "adresse")
    private String adresse;
    @Size(max = 255)
    @Column(name = "code_postal")
    private String codePostal;
    @Size(max = 255)
    @Column(name = "codeagence")
    private String codeagence;
    @Size(max = 255)
    @Column(name = "designation")
    private String designation;
    @Size(max = 50)
    @Column(name = "last_author")
    private String lastAuthor;
    @Column(name = "region")
    private String region;
    @Size(max = 2147483647)
    @Column(name = "adresse_ip")
    private String adresseIp;
    @Size(max = 2147483647)
    @Column(name = "telephone")
    private String telephone;
    @Size(max = 2147483647)
    @Column(name = "email")
    private String email;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave = new Date();
    @Column(name = "last_date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastDateSave;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "secteur_activite", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhSecteurs secteurActivite;
    @JoinColumn(name = "chef_agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes chefAgence;
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire ville;
    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<YvsGrhEmployes> employes;

    @Transient
    private boolean select;
    @Transient
    private long employeTemporaire;
    @Transient
    private long employePermament;
    @Transient
    private long employeTacheron;
    @Transient
    private long employeStagiaire;

    public YvsAgences() {
        employes = new ArrayList<>();
    }

    public YvsAgences(Long id) {
        this();
        this.id = id;
    }

    public YvsAgences(Long id, String abbreviation, String codeagence, YvsSocietes societe) {
        this(id);
        this.abbreviation = abbreviation;
        this.societe = societe;
    }

    public YvsAgences(Long id, String designation) {
        this(id);
        this.designation = designation;
    }

    public YvsAgences(Long id, String codeagence, String designation) {
        this(id, designation);
        this.codeagence = codeagence;
    }

    public YvsAgences(Long id, boolean actif, boolean supp) {
        this(id);
        this.actif = actif;
        this.supp = supp;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    @XmlTransient
    @JsonIgnore
    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public long getEmployeTemporaire() {
        return employeTemporaire;
    }

    public void setEmployeTemporaire(long employeTemporaire) {
        this.employeTemporaire = employeTemporaire;
    }

    public long getEmployePermament() {
        return employePermament;
    }

    public void setEmployePermament(long employePermament) {
        this.employePermament = employePermament;
    }

    public long getEmployeTacheron() {
        return employeTacheron;
    }

    public void setEmployeTacheron(long employeTacheron) {
        this.employeTacheron = employeTacheron;
    }

    public long getEmployeStagiaire() {
        return employeStagiaire;
    }

    public void setEmployeStagiaire(long employeStagiaire) {
        this.employeStagiaire = employeStagiaire;
    }

    public String getAbbreviation() {
        return abbreviation != null ? abbreviation : "";
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodeagence() {
        return codeagence != null ? codeagence : "";
    }

    public void setCodeagence(String codeagence) {
        this.codeagence = codeagence;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLastAuthor() {
        return lastAuthor;
    }

    public void setLastAuthor(String lastAuthor) {
        this.lastAuthor = lastAuthor;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    public Date getLastDateSave() {
        return lastDateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setLastDateSave(Date lastDateSave) {
        this.lastDateSave = lastDateSave;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsGrhSecteurs getSecteurActivite() {
        return secteurActivite;
    }

    public void setSecteurActivite(YvsGrhSecteurs secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public YvsGrhEmployes getChefAgence() {
        return chefAgence;
    }

    public void setChefAgence(YvsGrhEmployes chefAgence) {
        this.chefAgence = chefAgence;
    }

    public YvsDictionnaire getVille() {
        return ville;
    }

    public void setVille(YvsDictionnaire ville) {
        this.ville = ville;
    }

    @XmlTransient
    public YvsMutMutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(YvsMutMutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhEmployes> getEmployes() {
        return employes;
    }

    public void setEmployes(List<YvsGrhEmployes> employes) {
        this.employes = employes;
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
        if (!(object instanceof YvsAgences)) {
            return false;
        }
        YvsAgences other = (YvsAgences) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

//    public long employePermamentByList() {
//        employePermament = 0;
//        employeTemporaire = 0;
//        employeTacheron = 0;
//        employeStagiaire = 0;
//        for (YvsGrhEmployes e : employes) {
//            for (YvsGrhProfilEmps p : e.getYvsProfilEmpsList()) {
//                if (p.getActif()) {
//                    switch (p.getStatut()) {
//                        case "PE":
//                            employePermament++;
//                            break;
//                        case "TEP":
//                            employeTemporaire++;
//                            break;
//                        case "Tacheron":
//                            employeTacheron++;
//                            break;
//                        case "ST":
//                            employeStagiaire++;
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }
//        }
//        return employePermament;
//    }
    @Override
    public String toString() {
        return "YvsAgences{" + "id=" + id + '}';
    }
}
