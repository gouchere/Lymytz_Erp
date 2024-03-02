/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.client;

import yvs.entity.tiers.YvsBaseVisiteurPointLivraison;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBasePointLivraison;
import yvs.entity.commercial.rrr.YvsComPlanRistourne;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.ext.YvsExtClients;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_client")
@NamedQueries({
    @NamedQuery(name = "YvsComClient.findAll", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findIds", query = "SELECT y.id FROM YvsComClient y WHERE y.tiers.societe = :societe ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findIdByCode", query = "SELECT y.id FROM YvsComClient y WHERE y.tiers.societe = :societe AND y.codeClient = :code ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findAll_", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe ORDER BY y.codeClient ASC"),
    @NamedQuery(name = "YvsComClient.findByName", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe AND  ((UPPER(y.codeClient) LIKE UPPER(:code) OR UPPER(y.nom) LIKE UPPER(:code) OR UPPER(y.prenom) LIKE UPPER(:code))) ORDER BY y.tiers.nom"),
    @NamedQuery(name = "YvsComClient.findTrue", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe AND y.actif = true ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findDefaut", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe AND y.defaut = true AND y.actif = true ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findByDefautOrVille", query = "SELECT y FROM YvsComClient y JOIN FETCH y.categorieComptable JOIN FETCH y.tiers "
            + " JOIN FETCH y.model LEFT JOIN FETCH y.tiers.categorieComptable WHERE y.tiers.societe = :societe AND y.actif=true AND (y.defaut = true OR y.tiers.ville = :ville) ORDER BY y.tiers.ville.id, y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findDefautSecteur", query = "SELECT y FROM YvsComClient y JOIN FETCH y.categorieComptable JOIN FETCH y.tiers "
            + " JOIN FETCH y.model LEFT JOIN FETCH y.tiers.categorieComptable WHERE y.tiers.societe = :societe AND y.actif=true AND y.defaut = true AND y.tiers.secteur = :secteur ORDER BY y.tiers.ville.id, y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findDefautVille", query = "SELECT y FROM YvsComClient y JOIN FETCH y.categorieComptable JOIN FETCH y.tiers "
            + " JOIN FETCH y.model LEFT JOIN FETCH y.tiers.categorieComptable WHERE y.tiers.societe = :societe AND y.actif=true AND y.defaut = true AND y.tiers.ville = :ville ORDER BY y.tiers.ville.id, y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findDefautPays", query = "SELECT y FROM YvsComClient y JOIN FETCH y.categorieComptable JOIN FETCH y.tiers "
            + " JOIN FETCH y.model LEFT JOIN FETCH y.tiers.categorieComptable  WHERE y.tiers.societe = :societe AND y.tiers.pays = :pays AND y.tiers.ville IS NULL AND y.defaut = true AND y.actif = true ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findLikeNumActif", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe AND y.actif = true AND y.codeClient LIKE :codeClient ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findAllCount", query = "SELECT COUNT(y) FROM YvsComClient y WHERE y.tiers.societe = :societe AND y.actif=true"),
    @NamedQuery(name = "YvsComClient.findByAgence", query = "SELECT y FROM YvsComClient y WHERE y.author.agence = :agence ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findById", query = "SELECT y FROM YvsComClient y JOIN FETCH y.categorieComptable JOIN FETCH y.tiers "
            + " JOIN FETCH y.model LEFT JOIN FETCH y.tiers.categorieComptable WHERE y.id = :id"),
    @NamedQuery(name = "YvsComClient.findByIds", query = "SELECT y FROM YvsComClient y JOIN FETCH y.categorieComptable JOIN FETCH y.tiers "
            + " JOIN FETCH y.model LEFT JOIN FETCH y.tiers.categorieComptable WHERE y.id IN :ids ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findTiersById", query = "SELECT y.tiers FROM YvsComClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComClient.findByLikeCode", query = "SELECT y FROM YvsComClient y WHERE y.codeClient LIKE :code AND y.tiers.societe = :societe ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findByCode", query = "SELECT y FROM YvsComClient y WHERE y.codeClient = :code AND y.tiers.societe = :societe ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findByNomPrenom", query = "SELECT y FROM YvsComClient y WHERE y.nom = :nom AND y.prenom = :prenom AND y.tiers.societe = :societe"),
    @NamedQuery(name = "YvsComClient.findByTiers", query = "SELECT y FROM YvsComClient y WHERE y.tiers=:tiers"),

    @NamedQuery(name = "YvsComClient.findByCodes", query = "SELECT y FROM YvsComClient y WHERE y.codeClient BETWEEN :compteDebut AND :compteFin AND y.tiers.societe = :societe"),

    @NamedQuery(name = "YvsComClient.findAllNotIds", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe AND y.id NOT IN :ids ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsComClient.findTiers", query = "SELECT y.tiers FROM YvsComClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComClient.findTiersId", query = "SELECT DISTINCT y.tiers.id FROM YvsComClient y WHERE y.id IN :ids"),

    @NamedQuery(name = "YvsComClient.findByNomPhone", query = "SELECT y FROM YvsComClient y WHERE UPPER(y.nom) = UPPER(:nom) AND y.tiers.tel = :phone"),
    @NamedQuery(name = "YvsComClient.findByNomsPhone", query = "SELECT y FROM YvsComClient y WHERE UPPER(CONCAT(y.nom, ' ', y.prenom)) = UPPER(:nom) AND y.tiers.tel = :phone AND y.tiers.societe = :societe"),
    @NamedQuery(name = "YvsComClient.findPrevByNom", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe AND y.codeClient < :code ORDER BY y.codeClient DESC, y.nom DESC"),
    @NamedQuery(name = "YvsComClient.findNextByNom", query = "SELECT y FROM YvsComClient y WHERE y.tiers.societe = :societe AND y.codeClient > :code ORDER BY y.codeClient, y.nom"),})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComClient extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_client_id_seq", name = "yvs_com_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_client_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "suivi_comptable")
    private Boolean suiviComptable;
    @Column(name = "code_client")
    private String codeClient;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Basic(optional = true)
    @Column(name = "actif")
    private Boolean actif;
    @Basic(optional = true)
    @Column(name = "confirmer")
    private Boolean confirmer;
    @Column(name = "seuil_solde")
    private Double seuilSolde;
    @Column(name = "date_creation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Column(name = "vente_online")
    private Boolean venteOnline;
    @Column(name = "exclus_for_home")
    private Boolean exclusForHome;

    @OneToOne(mappedBy = "client", fetch = FetchType.LAZY)
    private YvsExtClients codeExterne;

    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTiers tiers;
    @JoinColumn(name = "representant", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers representant;
    @JoinColumn(name = "categorie_comptable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieComptable categorieComptable;
    @JoinColumn(name = "plan_ristourne", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComPlanRistourne planRistourne;
    @JoinColumn(name = "ligne", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePointLivraison ligne;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compte;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModelReglement model;
    @JoinColumn(name = "create_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers createBy;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<YvsComCategorieTarifaire> categories;
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<YvsComPlanReglementClient> models;
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<YvsComContratsClient> contrats;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean deleteTiers;
    @Transient
    private String nom_prenom;
    @Transient
    private String categorie;
    @Transient
    private double credit;
    @Transient
    private double solde;
    @Transient
    private double soldeInitial;
    @Transient
    private double debit;
    @Transient
    private double acompte;
    @Transient
    private double creance;
    @Transient
    private long nbrFactureImpayee;
    @Transient
    private List<YvsBaseVisiteurPointLivraison> pointsLivraison;

    public YvsComClient() {
        categories = new ArrayList<>();
        contrats = new ArrayList<>();
        models = new ArrayList<>();
        pointsLivraison = new ArrayList<>();
        this.dateCreation = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComClient(Long id) {
        this();
        this.id = id;
    }

    public YvsComClient(Long id, YvsBaseTiers tiers) {
        this(id);
        this.tiers = tiers;
    }

    public YvsComClient(Long id, String codeClient) {
        this(id);
        this.codeClient = codeClient;
    }

    public YvsComClient(Long id, String nom, String prenom) {
        this(id);
        this.nom = nom;
        this.prenom = prenom;
    }

    public YvsComClient(Long id, String codeClient, String nom, String prenom) {
        this(id, nom, prenom);
        this.codeClient = codeClient;
    }

    public YvsComClient(Long id, String codeClient, YvsBaseTiers tiers) {
        this(id, codeClient);
        this.tiers = tiers;
    }

    public YvsComClient(Long id, String codeClient, String nom, String prenom, YvsBaseTiers tiers) {
        this(id, codeClient, nom, prenom);
        this.tiers = tiers;
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

    public String getCategorie() {
        categorie = "";
        if (categories != null) {
            for (YvsComCategorieTarifaire c : categories) {
                categorie += c.getCategorie().getLibelle() + " - ";
            }
        }
        return categorie.trim().length() < 1 ? categorie : categorie.substring(0, categorie.length() - 2);
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
//        if (getTiers() != null ? getTiers().getId() > 0 : false) {
//            if (getTiers().getStSociete()) {
//                if (!(getTiers().getResponsable() == null || getTiers().getResponsable().trim().equals(""))) {
//                    if (nom_prenom == null || nom_prenom.trim().equals("")) {
//                        nom_prenom = getTiers().getResponsable();
//                    } else {
//                        nom_prenom += " / " + getTiers().getResponsable();
//                    }
//                }
//            }
//        }
        return nom_prenom;
    }

    public Boolean getConfirmer() {
        return confirmer != null ? confirmer : false;
    }

    public void setConfirmer(Boolean confirmer) {
        this.confirmer = confirmer;
    }

    public Boolean getExclusForHome() {
        return exclusForHome != null ? exclusForHome : false;
    }

    public void setExclusForHome(Boolean exclusForHome) {
        this.exclusForHome = exclusForHome;
    }

    public YvsUsers getRepresentant() {
        return representant;
    }

    public void setRepresentant(YvsUsers representant) {
        this.representant = representant;
    }

    public double getAcompte() {
        return acompte;
    }

    public void setAcompte(double acompte) {
        this.acompte = acompte;
    }

    public double getCreance() {
        return creance;
    }

    public void setCreance(double creance) {
        this.creance = creance;
    }

    public Double getSeuilSolde() {
        return seuilSolde != null ? seuilSolde : 0.0;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public double getSoldeInitial() {
        return soldeInitial;
    }

    public void setSoldeInitial(double soldeInitial) {
        this.soldeInitial = soldeInitial;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public void setSeuilSolde(Double seuilSolde) {
        this.seuilSolde = seuilSolde;
    }

    public Boolean getSuiviComptable() {
        return suiviComptable != null ? suiviComptable : false;
    }

    public void setSuiviComptable(Boolean suiviComptable) {
        this.suiviComptable = suiviComptable;
    }

    public YvsComPlanRistourne getPlanRistourne() {
        return planRistourne;
    }

    public void setPlanRistourne(YvsComPlanRistourne planRistourne) {
        this.planRistourne = planRistourne;
    }

    public String getNom() {
        return nom != null ? nom : "";
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom != null ? prenom : "";
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public boolean isDeleteTiers() {
        return deleteTiers;
    }

    public void setDeleteTiers(boolean deleteTiers) {
        this.deleteTiers = deleteTiers;
    }

    public YvsBaseModelReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModelReglement model) {
        this.model = model;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComPlanReglementClient> getModels() {
        return models;
    }

    public void setModels(List<YvsComPlanReglementClient> models) {
        this.models = models;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsBaseTiers getTiers() {
        return tiers;
    }

    public void setTiers(YvsBaseTiers tiers) {
        this.tiers = tiers;
    }

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
    }

    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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

    public Boolean getDefaut() {
        return defaut != null ? defaut : false;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }

    public YvsBaseCategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(YvsBaseCategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public Boolean getVenteOnline() {
        return venteOnline != null ? venteOnline : false;
    }

    public void setVenteOnline(Boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComCategorieTarifaire> getCategories() {
        if (categories != null) {
            Collections.sort(categories);
        }
        return categories;
    }

    public void setCategories(List<YvsComCategorieTarifaire> categories) {
        this.categories = categories;
    }

    public YvsUsers getCreateBy() {
        return createBy;
    }

    public void setCreateBy(YvsUsers createBy) {
        this.createBy = createBy;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateCreation() {
        return dateCreation;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    @XmlTransient
    @JsonIgnore
    public YvsExtClients getCodeExterne() {
        return codeExterne;
    }

    public void setCodeExterne(YvsExtClients codeExterne) {
        this.codeExterne = codeExterne;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseVisiteurPointLivraison> getPointsLivraison() {
        return pointsLivraison;
    }

    public void setPointsLivraison(List<YvsBaseVisiteurPointLivraison> pointsLivraison) {
        this.pointsLivraison = pointsLivraison;
    }

    public long getNbrFactureImpayee() {
        return nbrFactureImpayee;
    }

    public void setNbrFactureImpayee(long nbrFactureImpayee) {
        this.nbrFactureImpayee = nbrFactureImpayee;
    }

    public YvsBasePointLivraison getLigne() {
        return ligne;
    }

    public void setLigne(YvsBasePointLivraison ligne) {
        this.ligne = ligne;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComContratsClient> getContrats() {
        return contrats;
    }

    public void setContrats(List<YvsComContratsClient> contrats) {
        this.contrats = contrats;
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
        if (!(object instanceof YvsComClient)) {
            return false;
        }
        YvsComClient other = (YvsComClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsComClient{" + "id=" + id + '}';
    }

}
