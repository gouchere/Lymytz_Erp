/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhProfilEmps;
import yvs.entity.produits.YvsBasePublicites;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_societes")
@NamedQueries({
    @NamedQuery(name = "YvsSocietes.findAll", query = "SELECT y FROM YvsSocietes y WHERE y.actif=true ORDER BY y.name ASC"),
    @NamedQuery(name = "YvsSocietes.findById", query = "SELECT y FROM YvsSocietes y WHERE y.id = :id"),
    @NamedQuery(name = "YvsSocietes.findByIds", query = "SELECT y FROM YvsSocietes y WHERE y.id IN :ids ORDER BY y.codeAbreviation"),
    @NamedQuery(name = "YvsSocietes.findByNotIdsVenteOnline", query = "SELECT y FROM YvsSocietes y WHERE y.id NOT IN :ids AND y.venteOnline = :venteOnline ORDER BY y.codeAbreviation"),
    @NamedQuery(name = "YvsSocietes.findByAuthor", query = "SELECT y FROM YvsSocietes y WHERE y.author = :author"),
    @NamedQuery(name = "YvsSocietes.findByCapital", query = "SELECT y FROM YvsSocietes y WHERE y.capital = :capital"),
    @NamedQuery(name = "YvsSocietes.findByCodePostal", query = "SELECT y FROM YvsSocietes y WHERE y.codePostal = :codePostal"),
    @NamedQuery(name = "YvsSocietes.findByDevise", query = "SELECT y FROM YvsSocietes y WHERE y.devise = :devise"),
    @NamedQuery(name = "YvsSocietes.findByEmail", query = "SELECT y FROM YvsSocietes y WHERE y.email = :email"),
    @NamedQuery(name = "YvsSocietes.findByFormeJuridique", query = "SELECT y FROM YvsSocietes y WHERE y.formeJuridique = :formeJuridique"),
    @NamedQuery(name = "YvsSocietes.findByGestva", query = "SELECT y FROM YvsSocietes y WHERE y.gestva = :gestva"),
    @NamedQuery(name = "YvsSocietes.findBySiteWeb", query = "SELECT y FROM YvsSocietes y WHERE y.siteWeb = :siteWeb"),
    @NamedQuery(name = "YvsSocietes.findByTel", query = "SELECT y FROM YvsSocietes y WHERE y.tel = :tel"),
    @NamedQuery(name = "YvsSocietes.findByUmonaie", query = "SELECT y FROM YvsSocietes y WHERE y.umonaie = :umonaie"),
    @NamedQuery(name = "YvsSocietes.findByVille", query = "SELECT y FROM YvsSocietes y WHERE y.ville = :ville"),
    @NamedQuery(name = "YvsSocietes.findByAdressSiege", query = "SELECT y FROM YvsSocietes y WHERE y.adressSiege = :adressSiege"),
    @NamedQuery(name = "YvsSocietes.findByName", query = "SELECT y FROM YvsSocietes y WHERE y.name = :name"),
    @NamedQuery(name = "YvsSocietes.findBySiege", query = "SELECT y FROM YvsSocietes y WHERE y.siege = :siege"),
    @NamedQuery(name = "YvsSocietes.findByNumeroRegistreComerce", query = "SELECT y FROM YvsSocietes y WHERE y.numeroRegistreComerce = :numeroRegistreComerce"),
    @NamedQuery(name = "YvsSocietes.findByLastAuthor", query = "SELECT y FROM YvsSocietes y WHERE y.lastAuthor = :lastAuthor"),
    @NamedQuery(name = "YvsSocietes.findByDateSave", query = "SELECT y FROM YvsSocietes y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsSocietes.findBySameGroupe", query = "SELECT y FROM YvsSocietes y WHERE y.groupe = :groupe AND y.actif=true"),
    @NamedQuery(name = "YvsSocietes.findByLastDateSave", query = "SELECT y FROM YvsSocietes y WHERE y.lastDateSave = :lastDateSave"),
    @NamedQuery(name = "YvsSocietes.findByOnline", query = "SELECT y FROM YvsSocietes y WHERE y.venteOnline = :venteOnline ORDER BY y.codeAbreviation"),
    @NamedQuery(name = "YvsSocietes.findByGlp", query = "SELECT y FROM YvsSocietes y WHERE y.forGlp = :forGlp"),
    @NamedQuery(name = "YvsSocietes.findByCodeAbreviation", query = "SELECT y FROM YvsSocietes y WHERE y.codeAbreviation = :code")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsSocietes extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", columnDefinition = "SERIAL")
    @SequenceGenerator(sequenceName = "yvs_societes_id_seq", name = "yvs_societes_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_societes_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Size(max = 255)
    @Column(name = "code_postal")
    private String codePostal;
    @Size(max = 255)
    @Column(name = "devise")
    private String devise;
    @Size(max = 255)
    @Column(name = "fax")
    private String fax;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "forme_juridique")
    private String formeJuridique;
    @Column(name = "description")
    private String description;
    @Column(name = "a_propos")
    private String aPropos;
    @Size(max = 255)
    @Column(name = "site_web")
    private String siteWeb;
    @Size(max = 255)
    @Column(name = "tel")
    private String tel;
    @Size(max = 255)
    @Column(name = "umonaie")
    private String umonaie;
    @Size(max = 255)
    @Column(name = "adress_siege")
    private String adressSiege;
    @Column(name = "logo")
    private String logo;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "siege")
    private String siege;
    @Size(max = 2147483647)
    @Column(name = "numero_registre_comerce")
    private String numeroRegistreComerce;
    @Size(max = 2147483647)
    @Column(name = "numero_contribuable")
    private String numeroContribuable;
    @Size(max = 2147483647)
    @Column(name = "regime_cnps")
    private String regimeCnps;
    @Size(max = 2147483647)
    @Column(name = "last_author")
    private String lastAuthor;
    @Size(max = 2147483647)
    @Column(name = "code_abreviation")
    private String codeAbreviation;
    @Size(max = 2147483647)
    @Column(name = "adresse_ip")
    private String adresseIp;
    @Column(name = "actif")
    private Boolean actif = true;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "capital")
    private Double capital;
    @Column(name = "ecart_document")
    private Integer ecartDocument;
    @Column(name = "gestva")
    private Boolean gestva;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave = new Date();
    @Column(name = "last_date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastDateSave;
    @Size(max = 2147483647)
    @Column(name = "mode_chargement")
    private String modeChargement = "PRETTY";
    @Column(name = "cachet")
    private String cachet;
    @Column(name = "print_with_entete")
    private Boolean printWithEntete;
    @Column(name = "vente_online")
    private Boolean venteOnline;
    @Column(name = "for_glp")
    private Boolean forGlp;
    @JoinColumn(name = "pays", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire pays;
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire ville;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "groupe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseGroupeSociete groupe;

    @OneToOne(mappedBy = "societe", fetch = FetchType.LAZY)
    private YvsSocietesInfosVente infosVente;
    @OneToOne(mappedBy = "societe", fetch = FetchType.LAZY)
    private YvsSocietesConnexion connexion;

    @OneToMany(mappedBy = "societe")
    private List<YvsBasePublicites> publicites;
    @OneToMany(mappedBy = "societe")
    private List<YvsParametreGrh> yvsParametreGrh;
    @OneToMany(mappedBy = "societe")
    private List<YvsAgences> agences;
    @OneToMany(mappedBy = "societe")
    private List<YvsGrhDepartement> services;
    @OneToMany(mappedBy = "societe", fetch = FetchType.LAZY)
    private List<YvsGrhSecteurs> secteurs;
    @OneToMany(mappedBy = "societe")
    private List<YvsBaseExercice> exercices;
    @OneToMany(mappedBy = "societe")
    private List<YvsSocietesInfosSuppl> supplementaires;

    @Transient
    private long employeTemporaire;
    @Transient
    private long employePermament;
    @Transient
    private long employeTacheron;
    @Transient
    private long employeStagiaire;

    public YvsSocietes() {
        agences = new ArrayList<>();
        services = new ArrayList<>();
        exercices = new ArrayList<>();
    }

    public YvsSocietes(Long id) {
        this();
        this.id = id;
    }

    public YvsSocietes(Long id, String name) {
        this(id);
        this.name = name;
    }

    public YvsSocietes(Long id, double capital, boolean gestva) {
        this(id);
        this.capital = capital;
        this.gestva = gestva;
    }

    public YvsSocietes(Long id, YvsBaseGroupeSociete groupe) {
        this(id);
        this.groupe = groupe;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getVenteOnline() {
        return venteOnline != null ? venteOnline : false;
    }

    public void setVenteOnline(Boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getForGlp() {
        return forGlp != null ? forGlp : false;
    }

    public void setForGlp(Boolean forGlp) {
        this.forGlp = forGlp;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseExercice> getExercices() {
        return exercices;
    }

    public void setExercices(List<YvsBaseExercice> exercices) {
        this.exercices = exercices;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhDepartement> getServices() {
        return services;
    }

    public void setServices(List<YvsGrhDepartement> services) {
        this.services = services;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getDevise() {
        return devise != null ? devise.trim().length() > 0 ? devise : "Fcfa" : "Fcfa";
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFormeJuridique() {
        return formeJuridique;
    }

    public void setFormeJuridique(String formeJuridique) {
        this.formeJuridique = formeJuridique;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUmonaie() {
        return umonaie;
    }

    public void setUmonaie(String umonaie) {
        this.umonaie = umonaie;
    }

    public String getAdressSiege() {
        return adressSiege;
    }

    public void setAdressSiege(String adressSiege) {
        this.adressSiege = adressSiege;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiege() {
        return siege;
    }

    public void setSiege(String siege) {
        this.siege = siege;
    }

    public String getNumeroRegistreComerce() {
        return numeroRegistreComerce;
    }

    public void setNumeroRegistreComerce(String numeroRegistreComerce) {
        this.numeroRegistreComerce = numeroRegistreComerce;
    }

    public String getNumeroContribuable() {
        return numeroContribuable;
    }

    public void setNumeroContribuable(String numeroContribuable) {
        this.numeroContribuable = numeroContribuable;
    }

    public String getLastAuthor() {
        return lastAuthor;
    }

    public void setLastAuthor(String lastAuthor) {
        this.lastAuthor = lastAuthor;
    }

    public String getCodeAbreviation() {
        return codeAbreviation != null ? codeAbreviation : "";
    }

    public void setCodeAbreviation(String codeAbreviation) {
        this.codeAbreviation = codeAbreviation;
    }

    public String getCachet() {
        return cachet;
    }

    public void setCachet(String cachet) {
        this.cachet = cachet;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsAgences> getAgences() {
        return agences;
    }

    public void setAgences(List<YvsAgences> agences) {
        this.agences = agences;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Double getCapital() {
        return capital;
    }

    public void setCapital(Double capital) {
        this.capital = capital;
    }

    public Boolean getGestva() {
        return gestva;
    }

    public void setGestva(Boolean gestva) {
        this.gestva = gestva;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsDictionnaire getVille() {
        return ville;
    }

    public void setVille(YvsDictionnaire ville) {
        this.ville = ville;
    }

    public YvsDictionnaire getPays() {
        return pays;
    }

    public void setPays(YvsDictionnaire pays) {
        this.pays = pays;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhSecteurs> getSecteurs() {
        return secteurs;
    }

    public void setSecteurs(List<YvsGrhSecteurs> secteurs) {
        this.secteurs = secteurs;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsParametreGrh> getYvsParametreGrh() {
        return yvsParametreGrh;
    }

    public void setYvsParametreGrh(List<YvsParametreGrh> yvsParametreGrh) {
        this.yvsParametreGrh = yvsParametreGrh;
    }

    @XmlTransient
    @JsonIgnore
    public YvsSocietesInfosVente getInfosVente() {
        return infosVente;
    }

    public void setInfosVente(YvsSocietesInfosVente infosVente) {
        this.infosVente = infosVente;
    }

    @XmlTransient
    @JsonIgnore
    public YvsSocietesConnexion getConnexion() {
        return connexion;
    }

    public void setConnexion(YvsSocietesConnexion connexion) {
        this.connexion = connexion;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsSocietesInfosSuppl> getSupplementaires() {
        return supplementaires;
    }

    public void setSupplementaires(List<YvsSocietesInfosSuppl> supplementaires) {
        this.supplementaires = supplementaires;
    }

    public YvsBaseGroupeSociete getGroupe() {
        return groupe;
    }

    public void setGroupe(YvsBaseGroupeSociete groupe) {
        this.groupe = groupe;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
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

    public String getRegimeCnps() {
        return regimeCnps;
    }

    public void setRegimeCnps(String regimeCnps) {
        this.regimeCnps = regimeCnps;
    }

    public String getAPropos() {
        return aPropos;
    }

    public void setAPropos(String aPropos) {
        this.aPropos = aPropos;
    }

    public String getModeChargement() {
        return modeChargement != null ? modeChargement.trim().length() > 0 ? modeChargement : "PRETTY" : "PRETTY";
    }

    public void setModeChargement(String modeChargement) {
        this.modeChargement = modeChargement;
    }

    public Integer getEcartDocument() {
        return ecartDocument != null ? ecartDocument : 0;
    }

    public void setEcartDocument(Integer ecartDocument) {
        this.ecartDocument = ecartDocument;
    }

    public Boolean getPrintWithEntete() {
        return printWithEntete != null ? printWithEntete : true;
    }

    public void setPrintWithEntete(Boolean printWithEntete) {
        this.printWithEntete = printWithEntete;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBasePublicites> getPublicites() {
        return publicites;
    }

    public void setPublicites(List<YvsBasePublicites> publicites) {
        this.publicites = publicites;
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
        if (!(object instanceof YvsSocietes)) {
            return false;
        }
        YvsSocietes other = (YvsSocietes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsSocietes{" + "id=" + id + '}';
    }

    @XmlTransient
    @JsonIgnore
    public long employePermamentByList() {
        employePermament = 0;
        employeTemporaire = 0;
        employeTacheron = 0;
        employeStagiaire = 0;
//        for (YvsAgences a : agences) {
//            for (YvsGrhEmployes e : a.getEmployes()) {
//                for (YvsGrhProfilEmps p : e.getYvsProfilEmpsList()) {
//                    if (p.getActif()) {
//                        switch (p.getStatut()) {
//                            case "PE":
//                                employePermament++;
//                                break;
//                            case "TEP":
//                                employeTemporaire++;
//                                break;
//                            case "Tacheron":
//                                employeTacheron++;
//                                break;
//                            case "ST":
//                                employeStagiaire++;
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                }
//            }
//        }
        return employePermament;
    }
}
