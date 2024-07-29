/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.client;

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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore; 
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_plan_tarifaire")
@NamedQueries({
    @NamedQuery(name = "YvsBasePlanTarifaire.findAll", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.categorie.societe = :societe"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByCategorie", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.categorie = :categorie"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByArticle", query = "SELECT y FROM YvsBasePlanTarifaire y JOIN FETCH y.categorie WHERE y.article = :article"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByFamille", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.famille = :famille"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findById", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByPuv", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.puv = :puv"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByRemise", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.remise = :remise"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByArticleCategorie", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.categorie = :categorie AND y.article = :article AND y.actif = true "),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByCategorieArticle", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.categorie = :categorie AND y.article = :article"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByFamilleCategorie", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.categorie = :categorie AND y.famille = :famille AND y.actif = true "),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByCategorieFamille", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.categorie = :categorie AND y.famille = :famille"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByCategorieTemplate", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.categorie = :categorie AND y.template = :template"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByParentArticle", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.categorie.parent = :parent AND y.article = :article AND y.actif = true"),
    @NamedQuery(name = "YvsBasePlanTarifaire.findByParentFamille", query = "SELECT y FROM YvsBasePlanTarifaire y WHERE y.categorie.parent = :parent AND y.famille = :famille AND y.actif = true")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBasePlanTarifaire extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_categorie_tarifaire_client_id_seq", name = "yvs_com_categorie_tarifaire_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_categorie_tarifaire_client_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "puv")
    private Double puv;
    @Column(name = "puv_min")
    private Double puvMin;
    @Column(name = "nature_prix_min")
    private String naturePrixMin;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "ristourne")
    private Double ristourne;
    @Column(name = "coef_augmentation")
    private Double coefAugmentation;
    @Column(name = "nature_coef_augmentation")
    private String natureCoefAugmentation;
    @Column(name = "nature_remise")
    private String natureRemise;
    @Column(name = "nature_ristourne")
    private String natureRistourne;
    @Column(name = "actif")
    private Boolean actif;
    
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieClient categorie;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "template", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticlesTemplate template;
    @JoinColumn(name = "famille", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFamilleArticle famille;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @OneToMany(mappedBy = "plan")
    private List<YvsBasePlanTarifaireTranche> grilles;
    
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean forTemplate;
    @Transient
    private YvsBasePlanTarifaire parent;
    @Transient
    private String message_service;

    public YvsBasePlanTarifaire() {
        grilles = new ArrayList<>();
    }

    public YvsBasePlanTarifaire(Long id) {
        this.id = id;
        grilles = new ArrayList<>();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getMessage_service() {
        return message_service;
    }

    public void setMessage_service(String message_service) {
        this.message_service = message_service;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isForTemplate() {
        forTemplate = getTemplate() != null ? (getTemplate().getId() != null ? getTemplate().getId() > 0 : false) : false;
        return forTemplate;
    }

    public void setForTemplate(boolean forTemplate) {
        this.forTemplate = forTemplate;
    }

    public String getNaturePrixMin() {
        return naturePrixMin != null ? (naturePrixMin.trim().length() > 0 ? naturePrixMin : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
    }

    public YvsBasePlanTarifaire getParent() {
        return parent;
    }

    public void setParent(YvsBasePlanTarifaire parent) {
        this.parent = parent;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsBasePlanTarifaireTranche> getGrilles() {
        return grilles;
    }

    public void setGrilles(List<YvsBasePlanTarifaireTranche> grilles) {
        this.grilles = grilles;
    }

    public Double getPuvMin() {
        return puvMin != null ? puvMin : 0;
    }

    public void setPuvMin(Double puvMin) {
        this.puvMin = puvMin;
    }

    public YvsBaseFamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(YvsBaseFamilleArticle famille) {
        this.famille = famille;
    }

    public Double getRistourne() {
        return ristourne != null ? ristourne : 0;
    }

    public void setRistourne(Double ristourne) {
        this.ristourne = ristourne;
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

    public Double getCoefAugmentation() {
        return coefAugmentation != null ? coefAugmentation : 0;
    }

    public void setCoefAugmentation(Double coefAugmentation) {
        this.coefAugmentation = coefAugmentation;
    }

    public String getNatureRemise() {
        return natureRemise != null ? (natureRemise.trim().length() > 0 ? natureRemise : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureRemise(String natureRemise) {
        this.natureRemise = natureRemise;
    }

    public String getNatureRistourne() {
        return natureRistourne != null ? (natureRistourne.trim().length() > 0 ? natureRistourne : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureRistourne(String natureRistourne) {
        this.natureRistourne = natureRistourne;
    }

    public String getNatureCoefAugmentation() {
        return natureCoefAugmentation != null ? (natureCoefAugmentation.trim().length() > 0 ? natureCoefAugmentation : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureCoefAugmentation(String natureCoefAugmentation) {
        this.natureCoefAugmentation = natureCoefAugmentation;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Double getPuv() {
        return puv != null ? puv : 0;
    }

    public void setPuv(Double puv) {
        this.puv = puv;
    }

    public Double getRemise() {
        return remise != null ? remise : 0;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public YvsBaseCategorieClient getCategorie() {
        return categorie;
    }

    public void setCategorie(YvsBaseCategorieClient categorie) {
        this.categorie = categorie;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    @XmlTransient  @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient  @JsonIgnore
    public YvsBaseArticlesTemplate getTemplate() {
        return template;
    }

    public void setTemplate(YvsBaseArticlesTemplate template) {
        this.template = template;
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
        if (!(object instanceof YvsBasePlanTarifaire)) {
            return false;
        }
        YvsBasePlanTarifaire other = (YvsBasePlanTarifaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.client.YvsBasePlanTarifaire[ id=" + id + " ]";
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

}
