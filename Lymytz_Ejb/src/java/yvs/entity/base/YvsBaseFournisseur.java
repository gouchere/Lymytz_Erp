/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import yvs.dao.YvsEntity;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.ext.YvsExtFournisseur;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_fournisseur")
@NamedQueries({
    @NamedQuery(name = "YvsBaseFournisseur.findAll", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.tiers.societe = :societe ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsBaseFournisseur.findTrue", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.tiers.societe = :societe AND y.actif = true ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsBaseFournisseur.findLikeNumActif", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.tiers.societe = :societe AND y.actif = true AND y.codeFsseur LIKE :codeFsseur ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsBaseFournisseur.findAllActif", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.tiers.societe = :societe AND y.tiers.actif=true ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsBaseFournisseur.findAllDiffArt", query = "SELECT y FROM YvsBaseFournisseur y JOIN FETCH y.categorieComptable LEFT OUTER JOIN y.articles a "
            + "WHERE y.tiers.societe = :societe AND y.tiers.actif=true AND a.id IS NULL ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsBaseFournisseur.findByIds", query = "SELECT y FROM YvsBaseFournisseur y JOIN FETCH y.categorieComptable WHERE y.id IN :ids ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsBaseFournisseur.findDefaut", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.tiers.societe = :societe AND y.defaut = true AND y.actif = true ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsBaseFournisseur.findDefautVille", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.tiers.societe = :societe AND y.tiers.ville = :ville AND y.defaut = true AND y.actif = true ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsBaseFournisseur.findDefautPays", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.tiers.societe = :societe AND y.tiers.pays = :pays AND y.tiers.ville IS NULL AND y.defaut = true AND y.actif = true ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsBaseFournisseur.findAllCount", query = "SELECT COUNT(y) FROM YvsBaseFournisseur y WHERE y.tiers.societe = :societe"),
    @NamedQuery(name = "YvsBaseFournisseur.findByAgence", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.author.agence = :agence ORDER BY y.tiers.nom, y.tiers.prenom"),
    @NamedQuery(name = "YvsBaseFournisseur.findByTiers", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.tiers = :tiers"),
    @NamedQuery(name = "YvsBaseFournisseur.findByCodeFsseur", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.codeFsseur = :codeFsseur AND  y.tiers.societe = :societe"),
    @NamedQuery(name = "YvsBaseFournisseur.findByLikeCode", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.codeFsseur LIKE :code AND y.tiers.societe = :societe ORDER BY y.tiers.nom, y.tiers.prenom"),

    @NamedQuery(name = "YvsBaseFournisseur.findIdByCode", query = "SELECT y.id FROM YvsBaseFournisseur y WHERE y.codeFsseur = :code AND y.tiers.societe = :societe"),
    @NamedQuery(name = "YvsBaseFournisseur.findByCodes", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.codeFsseur BETWEEN :compteDebut AND :compteFin AND y.tiers.societe = :societe"),
    @NamedQuery(name = "YvsBaseFournisseur.findTiers", query = "SELECT y.tiers FROM YvsBaseFournisseur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseFournisseur.findTiersId", query = "SELECT DISTINCT y.tiers.id FROM YvsBaseFournisseur y WHERE y.id IN :ids"),

    @NamedQuery(name = "YvsBaseFournisseur.findByCode", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.codeFsseur = :code AND y.tiers.societe = :societe"),
    @NamedQuery(name = "YvsBaseFournisseur.findTiersById", query = "SELECT y.tiers FROM YvsBaseFournisseur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseFournisseur.findById", query = "SELECT y FROM YvsBaseFournisseur y WHERE y.id = :id")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseFournisseur extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_fournisseur_id_seq", name = "yvs_base_fournisseur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_fournisseur_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "code_fsseur")
    private String codeFsseur;
    @Basic(optional = true)
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "seuil_solde")
    private Double seuilSolde;
    @Column(name = "defaut")
    private Boolean defaut;    
    @Column(name = "exclus_for_home")
    private Boolean exclusForHome;    

    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTiers tiers;
    @JoinColumn(name = "categorie_comptable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieComptable categorieComptable;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieFournisseur categorie;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compte;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModelReglement model;
    @OneToOne(mappedBy = "fournisseur", fetch = FetchType.LAZY)
    private YvsExtFournisseur codeExterne;

    @OneToMany(mappedBy = "fournisseur", fetch = FetchType.LAZY)
    private List<YvsBaseArticleFournisseur> articles;

    @Transient
    private List<YvsComDocAchats> factures;
    @Transient
    private double soldeInitial;
    @Transient
    private double solde;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private String nom_prenom;
    @Transient
    private double credit;
    @Transient
    private double debit;
    @Transient
    private double acompte;
    @Transient
    private double creance;

    public YvsBaseFournisseur() {
        factures = new ArrayList<>();
    }

    public YvsBaseFournisseur(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseFournisseur(Long id, YvsBaseTiers tiers) {
        this(id);
        this.tiers = tiers;
    }

    public YvsBaseFournisseur(Long id, String code, YvsBaseTiers tiers) {
        this(id, tiers);
        this.codeFsseur = code;
    }

    public YvsBaseFournisseur(Long id, String nom, String prenom) {
        this(id);
        this.nom = nom;
        this.prenom = prenom;
    }

    public YvsBaseFournisseur(Long id, String codeFsseur, String nom, String prenom) {
        this(id, nom, prenom);
        this.codeFsseur = codeFsseur;
    }

    @XmlTransient
    @JsonIgnore
    public YvsExtFournisseur getCodeExterne() {
        return codeExterne;
    }

    public void setCodeExterne(YvsExtFournisseur codeExterne) {
        this.codeExterne = codeExterne;
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

    public Double getSeuilSolde() {
        return seuilSolde != null ? seuilSolde : 0.0;
    }

    public void setSeuilSolde(Double seuilSolde) {
        this.seuilSolde = seuilSolde;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public YvsBaseModelReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModelReglement model) {
        this.model = model;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
    }

    public Boolean getExclusForHome() {
        return exclusForHome != null ? exclusForHome : false;
    }

    public void setExclusForHome(Boolean exclusForHome) {
        this.exclusForHome = exclusForHome;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSoldeInitial() {
        return soldeInitial;
    }

    public void setSoldeInitial(double soldeInitial) {
        this.soldeInitial = soldeInitial;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Boolean getDefaut() {
        return defaut != null ? defaut : false;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
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

    public String getNom_prenom() {
        nom_prenom = getNom();
        if (nom_prenom != null ? nom_prenom.trim().length() < 1 : true) {
            if (getTiers() != null ? getTiers().getId() != null ? getTiers().getId() > 0 : false : false) {
                nom_prenom = getTiers().getNom();
            }
        }
        String suite = getPrenom();
        if (suite != null ? suite.trim().length() < 1 : true) {
            if (getTiers() != null ? getTiers().getId() != null ? getTiers().getId() > 0 : false : false) {
                suite = getTiers().getPrenom();
            }
        }
        if (suite != null ? suite.trim().length() > 0 : false) {
            nom_prenom += " " + suite;
        }
        if (getTiers() != null ? getTiers().getId() > 0 : false) {
            if (getTiers().getStSociete()) {
                if (!(getTiers().getResponsable() == null || getTiers().getResponsable().trim().equals(""))) {
                    if (nom_prenom == null || nom_prenom.trim().equals("")) {
                        nom_prenom = getTiers().getResponsable();
                    } else {
                        nom_prenom += " / " + getTiers().getResponsable();
                    }
                }
            }
        }
        return nom_prenom != null ? nom_prenom.trim() : "";
    }

    public String getCodeFsseur() {
        return codeFsseur != null ? codeFsseur : "";
    }

    public void setCodeFsseur(String codeFsseur) {
        this.codeFsseur = codeFsseur;
    }

    public YvsBaseTiers getTiers() {
        return tiers;
    }

    public void setTiers(YvsBaseTiers tiers) {
        this.tiers = tiers;
    }

    public YvsBaseCategorieFournisseur getCategorie() {
        return categorie;
    }

    public void setCategorie(YvsBaseCategorieFournisseur categorie) {
        this.categorie = categorie;
    }

    public YvsBaseCategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(YvsBaseCategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
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

    @XmlTransient
    @JsonIgnore
    public List<YvsComDocAchats> getFactures() {
        return factures;
    }

    public void setFactures(List<YvsComDocAchats> factures) {
        this.factures = factures;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleFournisseur> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticleFournisseur> articles) {
        this.articles = articles;
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
        if (!(object instanceof YvsBaseFournisseur)) {
            return false;
        }
        YvsBaseFournisseur other = (YvsBaseFournisseur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseFournisseur[ id=" + id + " ]";
    }

}
