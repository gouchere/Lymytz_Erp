/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.tiers;

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
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.commission.YvsComPlanCommission;
import yvs.entity.commercial.ration.YvsComParamRation;
import yvs.entity.commercial.rrr.YvsComPlanRistourne;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.ext.YvsExtTiers;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_tiers")
@NamedQueries({
    @NamedQuery(name = "YvsBaseTiers.findAll", query = "SELECT y FROM YvsBaseTiers y"),
    @NamedQuery(name = "YvsBaseTiers.findAllsCount", query = "SELECT COUNT(y) FROM YvsBaseTiers y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsBaseTiers.findNoms", query = "SELECT CONCAT(y.nom, ' ', y.prenom) FROM YvsBaseTiers y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsBaseTiers.findByNoms", query = "SELECT y FROM YvsBaseTiers y WHERE CONCAT(y.nom, ' ', y.prenom) = :noms AND y.societe = :societe"),
    @NamedQuery(name = "YvsBaseTiers.findAlls", query = "SELECT y FROM YvsBaseTiers y WHERE y.societe = :societe ORDER BY y.nom, y.prenom"),
    @NamedQuery(name = "YvsBaseTiers.findLikeCode", query = "SELECT y FROM YvsBaseTiers y WHERE y.societe = :societe AND UPPER(y.codeTiers) LIKE :code"),
    @NamedQuery(name = "YvsBaseTiers.findLikeCodeOrNoms", query = "SELECT y FROM YvsBaseTiers y WHERE y.societe = :societe AND (UPPER(y.codeTiers) LIKE :code OR UPPER(CONCAT(y.nom, ' ', y.prenom)) LIKE :code)"),
    @NamedQuery(name = "YvsBaseTiers.findById", query = "SELECT y FROM YvsBaseTiers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseTiers.findByIds", query = "SELECT y FROM YvsBaseTiers y WHERE y.id IN :ids ORDER BY y.nom, y.prenom"),
    @NamedQuery(name = "YvsBaseTiers.findByIdfindLikeCode", query = "SELECT y FROM YvsBaseTiers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseTiers.findByAdresse", query = "SELECT y FROM YvsBaseTiers y WHERE y.adresse = :adresse"),
    @NamedQuery(name = "YvsBaseTiers.findByBp", query = "SELECT y FROM YvsBaseTiers y WHERE y.bp = :bp"),
    @NamedQuery(name = "YvsBaseTiers.findByCivilite", query = "SELECT y FROM YvsBaseTiers y WHERE y.civilite = :civilite"),
    @NamedQuery(name = "YvsBaseTiers.findByClasse", query = "SELECT y FROM YvsBaseTiers y WHERE y.classe = :classe"),
    @NamedQuery(name = "YvsBaseTiers.findByClient", query = "SELECT y FROM YvsBaseTiers y WHERE y.client = :client AND y.societe =:societe"),
    @NamedQuery(name = "YvsBaseTiers.findByClients", query = "SELECT y FROM YvsBaseTiers y WHERE y.client = true AND y.societe =:societe"),
    @NamedQuery(name = "YvsBaseTiers.findByClientsCount", query = "SELECT COUNT(y) FROM YvsBaseTiers y WHERE y.client = true AND y.societe =:societe"),
    @NamedQuery(name = "YvsBaseTiers.findByCodeBarre", query = "SELECT y FROM YvsBaseTiers y WHERE y.codeBarre = :codeBarre"),
    @NamedQuery(name = "YvsBaseTiers.findByCode", query = "SELECT y FROM YvsBaseTiers y WHERE y.codeTiers = :code AND y.societe = :societe"),
    @NamedQuery(name = "YvsBaseTiers.findByCodeTiers", query = "SELECT y FROM YvsBaseTiers y WHERE y.codeTiers = :codeTiers"),
    @NamedQuery(name = "YvsBaseTiers.findByCompte", query = "SELECT y FROM YvsBaseTiers y WHERE y.compteCollectif = :compte"),
    @NamedQuery(name = "YvsBaseTiers.findByEmail", query = "SELECT y FROM YvsBaseTiers y WHERE y.email = :email"),
    @NamedQuery(name = "YvsBaseTiers.findByFournisseur", query = "SELECT y FROM YvsBaseTiers y WHERE y.fournisseur = :fournisseur AND y.societe =:societe"),
    @NamedQuery(name = "YvsBaseTiers.findByFournisseurs", query = "SELECT y FROM YvsBaseTiers y WHERE y.fournisseur = true AND y.societe =:societe"),
    @NamedQuery(name = "YvsBaseTiers.findByFournisseursCount", query = "SELECT COUNT(y) FROM YvsBaseTiers y WHERE y.fournisseur = true AND y.societe =:societe"),
    @NamedQuery(name = "YvsBaseTiers.findByLogo", query = "SELECT y FROM YvsBaseTiers y WHERE y.logo = :logo"),
    @NamedQuery(name = "YvsBaseTiers.findByNom", query = "SELECT y FROM YvsBaseTiers y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsBaseTiers.findByPrenom", query = "SELECT y FROM YvsBaseTiers y WHERE y.prenom = :prenom"),
    @NamedQuery(name = "YvsBaseTiers.findByNomPrenom", query = "SELECT y FROM YvsBaseTiers y WHERE y.societe = :societe AND y.nom = :nom AND y.prenom = :prenom"),
    @NamedQuery(name = "YvsBaseTiers.findByPays", query = "SELECT y FROM YvsBaseTiers y WHERE y.pays = :pays"),
    @NamedQuery(name = "YvsBaseTiers.findByPointDeVente", query = "SELECT y FROM YvsBaseTiers y WHERE y.pointDeVente = :pointDeVente"),
    @NamedQuery(name = "YvsBaseTiers.findByRepresentant", query = "SELECT y FROM YvsBaseTiers y WHERE y.representant = :representant AND y.societe =:societe"),
    @NamedQuery(name = "YvsBaseTiers.findBySociete", query = "SELECT y FROM YvsBaseTiers y WHERE y.stSociete = :stSociete"),
    @NamedQuery(name = "YvsBaseTiers.findByTel", query = "SELECT y FROM YvsBaseTiers y WHERE y.tel = :tel"),
    @NamedQuery(name = "YvsBaseTiers.findByVille", query = "SELECT y FROM YvsBaseTiers y WHERE y.ville = :ville"),
    @NamedQuery(name = "YvsBaseTiers.findByCodePostal", query = "SELECT y FROM YvsBaseTiers y WHERE y.codePostal = :codePostal"),
    @NamedQuery(name = "YvsBaseTiers.findByStatut", query = "SELECT y FROM YvsBaseTiers y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsBaseTiers.findByAlwaysVisible", query = "SELECT y FROM YvsBaseTiers y WHERE y.alwaysVisible = :alwaysVisible"),
    @NamedQuery(name = "YvsBaseTiers.findByActif", query = "SELECT y FROM YvsBaseTiers y WHERE y.actif = :actif"),

    @NamedQuery(name = "YvsBaseTiers.findIdByCode", query = "SELECT y FROM YvsBaseTiers y WHERE y.codeTiers BETWEEN :compteDebut AND :compteFin AND y.societe = :societe"),

    @NamedQuery(name = "YvsBaseTiers.findCountByFabriquant", query = "SELECT COUNT(y) FROM YvsBaseTiers y WHERE y.fabriquant = :fabriquant AND y.societe =:societe"),
    @NamedQuery(name = "YvsBaseTiers.findByFabriquant", query = "SELECT y FROM YvsBaseTiers y WHERE y.fabriquant = :fabriquant AND y.societe =:societe AND y.actif=true")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseTiers extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_tiers_id_seq", name = "yvs_tiers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_tiers_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 255)
    @Column(name = "adresse")
    private String adresse;
    @Size(max = 255)
    @Column(name = "bp")
    private String bp;
    @Size(max = 255)
    @Column(name = "civilite")
    private String civilite;
    @Size(max = 255)
    @Column(name = "classe")
    private String classe;
    @Size(max = 255)
    @Column(name = "code_barre")
    private String codeBarre;
    @Size(max = 255)
    @Column(name = "code_tiers")
    private String codeTiers;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "logo")
    private String logo;
    @Size(max = 255)
    @Column(name = "nom")
    private String nom;
    @Size(max = 255)
    @Column(name = "point_de_vente")
    private String pointDeVente;
    @Size(max = 255)
    @Column(name = "prenom")
    private String prenom;
    @Size(max = 255)
    @Column(name = "tel")
    private String tel;
    @Size(max = 2147483647)
    @Column(name = "code_postal")
    private String codePostal;
    @Size(max = 2147483647)
    @Column(name = "code_ration")
    private String codeRation;
    @Size(max = 2147483647)
    @Column(name = "responsable")
    private String responsable;
    @Size(max = 2147483647)
    @Column(name = "statut")
    private String statut;
    @Column(name = "site")
    private String site;
    @Column(name = "always_visible")
    private Boolean alwaysVisible;
    @Basic(optional = true)
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "fabriquant")
    private Boolean fabriquant;
    @Size(max = 255)
    @Column(name = "compte")
    private String compte;
    @Column(name = "employe")
    private Boolean employe;
    @Column(name = "client")
    private Boolean client;
    @Column(name = "fournisseur")
    private Boolean fournisseur;
    @Column(name = "representant")
    private Boolean representant;
    @Column(name = "st_societe")
    private Boolean stSociete;
    @Column(name = "personnel")
    private Boolean personnel;

    @JoinColumn(name = "compte_collectif", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compteCollectif;
    @JoinColumn(name = "pays", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire pays;
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire ville;
    @JoinColumn(name = "secteur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire secteur;
//    @JoinColumn(name = "ristourne", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsComPlanRistourne ristourne;
//    @JoinColumn(name = "comission", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsComPlanCommission comission;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "mdr", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement mdr;
    @JoinColumn(name = "categorie_comptable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieComptable categorieComptable;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    @OneToOne(mappedBy = "tiers", fetch = FetchType.LAZY)
    private YvsExtTiers codeExterne;

    @OneToMany(mappedBy = "tiers", fetch = FetchType.LAZY)
    private List<YvsBaseTiersTelephone> telephones;
    @OneToMany(mappedBy = "tiers")
    private List<YvsBaseFournisseur> fournisseurs;
    @OneToMany(mappedBy = "tiers")
    private List<YvsComClient> clients;
    @OneToMany(mappedBy = "tiers")
    private List<YvsComComerciale> commerciaux;
    @OneToMany(mappedBy = "compteTiers")
    private List<YvsGrhEmployes> employes;
    @OneToMany(mappedBy = "personnel")
    private List<YvsComParamRation> paramsRations;

    @Transient
    private YvsGrhEmployes currentEmploye;

    @Transient
    private List<YvsComptaBonProvisoire> bonsProvisoire;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean error;
    @Transient
    private double solde;
    @Transient
    private double credit;
    @Transient
    private double debit;
    @Transient
    private String type = "";
    @Transient
    private String nom_prenom;
    @Transient
    private String region;
    @Transient
    private YvsBaseTiersTelephone contact = new YvsBaseTiersTelephone();

    public YvsBaseTiers() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        bonsProvisoire = new ArrayList<>();
//        clients = new ArrayList<>();
//        employes = new ArrayList<>();
//        commerciaux = new ArrayList<>();
//        fournisseurs = new ArrayList<>();
    }

    public YvsBaseTiers(String codeTiers) {
        this();
        this.codeTiers = codeTiers;
    }

    public YvsBaseTiers(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseTiers(Long id, String codeTiers) {
        this(id);
        this.codeTiers = codeTiers;
    }

    public YvsBaseTiers(Long id, String nom, String prenom) {
        this(id);
        this.nom = nom;
        this.prenom = prenom;
    }

    public YvsBaseTiers(Long id, String codeTiers, String nom, String prenom) {
        this(id);
        this.codeTiers = codeTiers;
        this.nom = nom;
        this.prenom = prenom;
    }

    public YvsBaseTiers(Long id, boolean client, boolean fournisseur, boolean representant, boolean societe, boolean sup) {
        this(id);
        this.client = client;
        this.fournisseur = fournisseur;
        this.representant = representant;
        this.stSociete = societe;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public String getCodeRation() {
        return codeRation != null ? codeRation.trim() : "";
    }

    public void setCodeRation(String codeRation) {
        this.codeRation = codeRation;
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

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getType() {
        type = "";
        if (getStSociete()) {
            type = "(Societe)";
        }
        if (getClient()) {
            if ((type != null) ? type.trim().equals("") : true) {
                type = "Client -";
            } else {
                type = type + " Client -";
            }
        }
        if (getFournisseur()) {
            if ((type != null) ? type.trim().equals("") : true) {
                type = "Fournisseur -";
            } else {
                type = type + " Fournisseur -";
            }
        }
        if (getRepresentant()) {
            if ((type != null) ? type.trim().equals("") : true) {
                type = "Representant -";
            } else {
                type = type + " Representant -";
            }
        }
        if (getFabriquant()) {
            if ((type != null) ? type.trim().equals("") : true) {
                type = "Fabriquant -";
            } else {
                type = type + " Fabriquant -";
            }
        }
        if (type.length() > 0) {
            if (!type.equals("(Societe)") && type.length() > 3) {
                type = type.substring(0, type.length() - 2);
            }
        } else {
            type = "Autre";
        }
        return type;
    }

    public String getNom_prenom() {
        nom_prenom = "";
        if (!(getNom() == null || getNom().trim().equals(""))) {
            nom_prenom = getNom();
        }
        if (!(getPrenom() == null || getPrenom().trim().equals(""))) {
            if (nom_prenom == null || nom_prenom.trim().equals("")) {
                nom_prenom = getPrenom();
            } else {
                nom_prenom += " " + getPrenom();
            }
        }
        if (getStSociete()) {
            if (!(getResponsable() == null || getResponsable().trim().equals(""))) {
                if (nom_prenom == null || nom_prenom.trim().equals("")) {
                    nom_prenom = getResponsable();
                } else {
                    nom_prenom += " / " + getResponsable();
                }
            }
        }
        return nom_prenom;
    }

    public String getRegion() {
        region = "";
        if (getSecteur() != null ? getSecteur().getId() > 0 : false) {
            region = getSecteur().getLibele();
        }
        if (getVille() != null ? getVille().getId() > 0 : false) {
            if (region != null ? region.trim().length() > 0 : false) {
                region += " - " + (!getVille().getAbreviation().equals("") ? getVille().getAbreviation() : getVille().getLibele());
            } else {
                region = getVille().getLibele();
            }
        }
        if (getPays() != null ? getPays().getId() > 0 : false) {
            if (region != null ? region.trim().length() > 0 : false) {
                region += " - " + (!getPays().getAbreviation().equals("") ? getPays().getAbreviation() : getPays().getLibele());
            } else {
                region = getPays().getLibele();
            }
        }
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setNom_prenom(String nom_prenom) {
        this.nom_prenom = nom_prenom;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContact(YvsBaseTiersTelephone contact) {
        this.contact = contact;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public String getCodeTiers() {
        return codeTiers != null ? codeTiers : "";
    }

    public void setCodeTiers(String codeTiers) {
        this.codeTiers = codeTiers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNom() {
        return nom != null ? nom : "";
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPointDeVente() {
        return pointDeVente;
    }

    public void setPointDeVente(String pointDeVente) {
        this.pointDeVente = pointDeVente;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Boolean getAlwaysVisible() {
        return alwaysVisible != null ? alwaysVisible : false;
    }

    public void setAlwaysVisible(Boolean alwaysVisible) {
        this.alwaysVisible = alwaysVisible;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getFabriquant() {
        return fabriquant != null ? fabriquant : false;
    }

    public void setFabriquant(Boolean fabriquant) {
        this.fabriquant = fabriquant;
    }

    public String getCompte() {
        return compte;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public Boolean getEmploye() {
        return employe != null ? employe : false;
    }

    public void setEmploye(Boolean employe) {
        this.employe = employe;
    }

    public YvsDictionnaire getSecteur() {
        return secteur;
    }

    public void setSecteur(YvsDictionnaire secteur) {
        this.secteur = secteur;
    }

    @XmlTransient
    @JsonIgnore
    public YvsExtTiers getCodeExterne() {
        return codeExterne;
    }

    public void setCodeExterne(YvsExtTiers codeExterne) {
        this.codeExterne = codeExterne;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseTiersTelephone getContact() {
        if (telephones != null ? !telephones.isEmpty() : false) {
            boolean add = false;
            for (YvsBaseTiersTelephone t : telephones) {
                if (t.getPrincipal()) {
                    contact = t;
                    add = true;
                    break;
                }
            }
            if (!add) {
                contact = telephones.get(0);
            }
        }
        return contact;
    }

    public YvsBasePlanComptable getCompteCollectif() {
        return compteCollectif;
    }

    public void setCompteCollectif(YvsBasePlanComptable compteCollectif) {
        this.compteCollectif = compteCollectif;
    }

//    public YvsComPlanRistourne getRistourne() {
//        return ristourne;
//    }
//
//    public void setRistourne(YvsComPlanRistourne ristourne) {
//        this.ristourne = ristourne;
//    }
//
//    public YvsComPlanCommission getComission() {
//        return comission;
//    }
//
//    public void setComission(YvsComPlanCommission comission) {
//        this.comission = comission;
//    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsDictionnaire getPays() {
        return pays;
    }

    public void setPays(YvsDictionnaire pays) {
        this.pays = pays;
    }

    public YvsDictionnaire getVille() {
        return ville;
    }

    public void setVille(YvsDictionnaire ville) {
        this.ville = ville;
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

    public YvsBaseModeReglement getMdr() {
        return mdr;
    }

    public void setMdr(YvsBaseModeReglement mdr) {
        this.mdr = mdr;
    }

    public YvsBaseCategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(YvsBaseCategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhEmployes getCurrentEmploye() {
        return currentEmploye;
    }

    public void setCurrentEmploye(YvsGrhEmployes currentEmploye) {
        this.currentEmploye = currentEmploye;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaBonProvisoire> getBonsProvisoire() {
        return bonsProvisoire;
    }

    public void setBonsProvisoire(List<YvsComptaBonProvisoire> bonsProvisoire) {
        this.bonsProvisoire = bonsProvisoire;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseFournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<YvsBaseFournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComComerciale> getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(List<YvsComComerciale> commerciaux) {
        this.commerciaux = commerciaux;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsGrhEmployes> getEmployes() {
        return employes;
    }

    public void setEmployes(List<YvsGrhEmployes> employes) {
        this.employes = employes;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseTiersTelephone> getTelephones() {
        return telephones;
    }

    public void setTelephones(List<YvsBaseTiersTelephone> telephones) {
        this.telephones = telephones;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComClient> getClients() {
        return clients;
    }

    public void setClients(List<YvsComClient> clients) {
        this.clients = clients;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComParamRation> getParamsRations() {
        return paramsRations;
    }

    public void setParamsRations(List<YvsComParamRation> paramsRations) {
        this.paramsRations = paramsRations;
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
        if (!(object instanceof YvsBaseTiers)) {
            return false;
        }
        YvsBaseTiers other = (YvsBaseTiers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.tiers.YvsBaseTiers[ id=" + id + " ]";
    }

    public Boolean getClient() {
        return client != null ? client : false;
    }

    public void setClient(Boolean client) {
        this.client = client;
    }

    public Boolean getFournisseur() {
        return fournisseur != null ? fournisseur : false;
    }

    public void setFournisseur(Boolean fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Boolean getRepresentant() {
        return representant != null ? representant : false;
    }

    public void setRepresentant(Boolean representant) {
        this.representant = representant;
    }

    public Boolean getStSociete() {
        return stSociete != null ? stSociete : false;
    }

    public void setStSociete(Boolean stSociete) {
        this.stSociete = stSociete;
    }

    public Boolean getPersonnel() {
        return personnel != null ? personnel : false;
    }

    public void setPersonnel(Boolean personnel) {
        this.personnel = personnel;
    }

}
