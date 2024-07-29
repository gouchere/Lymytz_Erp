/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.prod;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.XmlTransient;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseClassesStat;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_articles_template")
@NamedQueries({
    @NamedQuery(name = "YvsArticlesTemplate.findAll", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.societe =:societe"),
    @NamedQuery(name = "YvsArticlesTemplate.findById", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.id = :id"),
    @NamedQuery(name = "YvsArticlesTemplate.findByCode", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.refArt =:code AND y.societe=:societe"),
    @NamedQuery(name = "YvsArticlesTemplate.findByCodeTemplate", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE (y.refArt =:code OR y.designation = :code) AND y.societe=:societe"),
    @NamedQuery(name = "YvsArticlesTemplate.findByCodeTemplateL", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE (y.refArt LIKE :code OR y.designation LIKE :code) AND y.societe=:societe"),
    @NamedQuery(name = "YvsArticlesTemplate.findByChangePrix", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.changePrix = :changePrix"),
    @NamedQuery(name = "YvsArticlesTemplate.findByDescription", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.description = :description"),
    @NamedQuery(name = "YvsArticlesTemplate.findByDefNorme", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.defNorme = :defNorme"),
    @NamedQuery(name = "YvsArticlesTemplate.findByDesignation", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsArticlesTemplate.findByModeConso", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.modeConso = :modeConso"),
    @NamedQuery(name = "YvsArticlesTemplate.findByNorme", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.norme = :norme"),
    @NamedQuery(name = "YvsArticlesTemplate.findBySuiviEnStock", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.suiviEnStock = :suiviEnStock"),
    @NamedQuery(name = "YvsArticlesTemplate.findByVisibleEnSynthese", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.visibleEnSynthese = :visibleEnSynthese"),
    @NamedQuery(name = "YvsArticlesTemplate.findByCoefficient", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.coefficient = :coefficient"),
    @NamedQuery(name = "YvsArticlesTemplate.findByService", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.service = :service"),
    @NamedQuery(name = "YvsArticlesTemplate.findByMethodeVal", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.methodeVal = :methodeVal"),
    @NamedQuery(name = "YvsArticlesTemplate.findByActif", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsArticlesTemplate.findByCategorie", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.categorie = :categorie"),
    @NamedQuery(name = "YvsArticlesTemplate.findByDureeVie", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.dureeVie = :dureeVie"),
    @NamedQuery(name = "YvsArticlesTemplate.findByDureeGarantie", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.dureeGarantie = :dureeGarantie"),
    @NamedQuery(name = "YvsArticlesTemplate.findByFichier", query = "SELECT y FROM YvsBaseArticlesTemplate y WHERE y.fichier = :fichier")})
public class YvsBaseArticlesTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_articles_template_id_seq", name = "yvs_articles_template_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_articles_template_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "change_prix")
    private Boolean changePrix;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "def_norme")
    private boolean defNorme;
    @Size(max = 255)
    @Column(name = "designation")
    private String designation;
    @Size(max = 255)
    @Column(name = "mode_conso")
    private String modeConso;
    @Basic(optional = false)
    @Column(name = "norme")
    private boolean norme;
    @Size(max = 255)
    @Column(name = "ref_art")
    private String refArt;
    @Basic(optional = false)
    @Column(name = "suivi_en_stock")
    private Boolean suiviEnStock;
    @Basic(optional = false)
    @Column(name = "visible_en_synthese")
    private boolean visibleEnSynthese;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "coefficient")
    private Double coefficient;
    @Column(name = "service")
    private Boolean service;
    @Size(max = 2147483647)
    @Column(name = "methode_val")
    private String methodeVal;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "categorie")
    private String categorie;
    @Column(name = "duree_vie")
    private Double dureeVie;
    @Column(name = "duree_garantie")
    private Double dureeGarantie;
    @Size(max = 2147483647)
    @Column(name = "fichier")
    private String fichier;
    @Size(max = 2147483647)
    @Column(name = "code_barre")
    private String codeBarre;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "classe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseClassesStat classe;
    @JoinColumn(name = "groupe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseGroupesArticle groupe;
    @JoinColumn(name = "famille", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFamilleArticle famille;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "template")
    private List<YvsBasePlanTarifaire> plans_tarifaires;
    @OneToMany(mappedBy = "template")
    private List<YvsBaseArticleCategorieComptable> comptes;
    @OneToMany(mappedBy = "template")
    private List<YvsBaseArticles> articles;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBaseArticlesTemplate() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        plans_tarifaires = new ArrayList<>();
        comptes = new ArrayList<>();
    }

    public YvsBaseArticlesTemplate(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseArticlesTemplate(String ref) {
        this();
        this.refArt = ref;
    }

    public YvsBaseArticlesTemplate(Long id, String ref, String des) {
        this();
        this.id = id;
        this.refArt = ref;
        this.designation = des;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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

    public String getCodeBarre() {
        return codeBarre != null ? codeBarre : "";
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getChangePrix() {
        return changePrix != null ? changePrix : false;
    }

    public void setChangePrix(Boolean changePrix) {
        this.changePrix = changePrix;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getDefNorme() {
        return defNorme;
    }

    public void setDefNorme(boolean defNorme) {
        this.defNorme = defNorme;
    }

    public String getDesignation() {
        return designation != null ? designation : "";
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getModeConso() {
        return modeConso != null ? modeConso : "";
    }

    public void setModeConso(String modeConso) {
        this.modeConso = modeConso;
    }

    public boolean getNorme() {
        return norme;
    }

    public void setNorme(boolean norme) {
        this.norme = norme;
    }

    public Boolean getSuiviEnStock() {
        return suiviEnStock != null ? suiviEnStock : false;
    }

    public void setSuiviEnStock(Boolean suiviEnStock) {
        this.suiviEnStock = suiviEnStock;
    }

    public boolean getVisibleEnSynthese() {
        return visibleEnSynthese;
    }

    public void setVisibleEnSynthese(boolean visibleEnSynthese) {
        this.visibleEnSynthese = visibleEnSynthese;
    }

    public Double getCoefficient() {
        return coefficient != null ? coefficient : 0.0;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Boolean getService() {
        return service != null ? service : false;
    }

    public void setService(Boolean service) {
        this.service = service;
    }

    public String getMethodeVal() {
        return methodeVal != null ? methodeVal : "FIFO";
    }

    public void setMethodeVal(String methodeVal) {
        this.methodeVal = methodeVal;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getCategorie() {
        return categorie != null ? categorie : "";
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Double getDureeVie() {
        return dureeVie != null ? dureeVie : 0.0;
    }

    public void setDureeVie(Double dureeVie) {
        this.dureeVie = dureeVie;
    }

    public Double getDureeGarantie() {
        return dureeGarantie != null ? dureeGarantie : 0.0;
    }

    public void setDureeGarantie(Double dureeGarantie) {
        this.dureeGarantie = dureeGarantie;
    }

    public String getFichier() {
        return fichier != null ? fichier : "";
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public YvsBaseGroupesArticle getGroupe() {
        return groupe;
    }

    public void setGroupe(YvsBaseGroupesArticle groupe) {
        this.groupe = groupe;
    }

    public YvsBaseFamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(YvsBaseFamilleArticle famille) {
        this.famille = famille;
    }

    public String getRefArt() {
        return refArt != null ? refArt : "";
    }

    public void setRefArt(String refArt) {
        this.refArt = refArt;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseClassesStat getClasse() {
        return classe;
    }

    public void setClasse(YvsBaseClassesStat classe) {
        this.classe = classe;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBasePlanTarifaire> getPlans_tarifaires() {
        return plans_tarifaires;
    }

    public void setPlans_tarifaires(List<YvsBasePlanTarifaire> plans_tarifaires) {
        this.plans_tarifaires = plans_tarifaires;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleCategorieComptable> getComptes() {
        return comptes;
    }

    public void setComptes(List<YvsBaseArticleCategorieComptable> comptes) {
        this.comptes = comptes;
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
        if (!(object instanceof YvsBaseArticlesTemplate)) {
            return false;
        }
        YvsBaseArticlesTemplate other = (YvsBaseArticlesTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.prod.YvsArticlesTemplate[ id=" + id + " ]";
    }

}
