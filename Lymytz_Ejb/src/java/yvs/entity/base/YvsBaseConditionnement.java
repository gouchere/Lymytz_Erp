/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.produits.YvsBaseArticleCodeBarre;
import yvs.entity.produits.YvsBaseArticlePack;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.YvsBasePublicites;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_conditionnement")
@NamedQueries({
    @NamedQuery(name = "YvsBaseConditionnement.findCountAll", query = "SELECT COUNT(y) FROM YvsBaseConditionnement y WHERE y.article.famille.societe =:societe"),
    @NamedQuery(name = "YvsBaseConditionnement.findAll", query = "SELECT y FROM YvsBaseConditionnement y JOIN FETCH y.unite JOIN FETCH y.article WHERE y.article.famille.societe =:societe ORDER BY y.article.refArt, y.article.designation"),
    @NamedQuery(name = "YvsBaseConditionnement.findById", query = "SELECT y FROM YvsBaseConditionnement y JOIN FETCH y.unite JOIN FETCH y.article WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseConditionnement.findBySociete", query = "SELECT y FROM YvsBaseConditionnement y JOIN FETCH y.unite JOIN FETCH y.article WHERE y.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsBaseConditionnement.findCondVente", query = "SELECT y FROM YvsBaseConditionnement y JOIN FETCH y.unite JOIN FETCH y.article WHERE y.article = :article AND y.unite=:unite AND y.byVente=TRUE"),
    @NamedQuery(name = "YvsBaseConditionnement.findByArticle", query = "SELECT y FROM YvsBaseConditionnement y JOIN FETCH y.unite JOIN FETCH y.article WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseConditionnement.findByArticle1", query = "SELECT y FROM YvsBaseConditionnement y JOIN FETCH y.unite WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseConditionnement.findByActifArticle", query = "SELECT y FROM YvsBaseConditionnement y JOIN FETCH y.unite JOIN FETCH y.article WHERE y.article = :article AND y.actif = TRUE"),
    @NamedQuery(name = "YvsBaseConditionnement.findByArticleUnite", query = "SELECT y FROM YvsBaseConditionnement y JOIN FETCH y.unite JOIN FETCH y.article WHERE y.article = :article AND y.unite = :unite"),
    @NamedQuery(name = "YvsBaseConditionnement.findByArticleReferenceUnite", query = "SELECT y FROM YvsBaseConditionnement y JOIN FETCH y.unite JOIN FETCH y.article WHERE y.article = :article AND y.unite.reference = :unite"),
    @NamedQuery(name = "YvsBaseConditionnement.findIdByArticleUnite", query = "SELECT y.id FROM YvsBaseConditionnement y JOIN FETCH y.unite JOIN FETCH y.article WHERE y.article = :article AND y.unite = :unite"),
    @NamedQuery(name = "YvsBaseConditionnement.findIdUniteById", query = "SELECT y.unite.id FROM YvsBaseConditionnement y  WHERE y.id=:id"),
    @NamedQuery(name = "YvsBaseConditionnement.findVenteByArticle", query = "SELECT y FROM YvsBaseConditionnement y JOIN FETCH y.unite JOIN FETCH y.article WHERE y.article = :article AND y.byVente = TRUE"),
    @NamedQuery(name = "YvsBaseConditionnement.findUniteByArticle", query = "SELECT DISTINCT(y.unite) FROM YvsBaseConditionnement y JOIN FETCH y.unite WHERE (UPPER(y.article.refArt) LIKE :article OR UPPER(y.article.designation) LIKE :article) AND y.unite.societe = :societe")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseConditionnement extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_conditionnement_id_seq", name = "yvs_base_conditionnement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_conditionnement_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "prix_min")
    private Double prixMin;
    @Column(name = "prix_achat")
    private Double prixAchat;
    @Column(name = "prix_prod")
    private Double prixProd;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "prix")
    private Double prix;
    @Column(name = "taux_pua")
    private Double tauxPua;
    @Column(name = "cond_vente")
    private Boolean byVente;
    @Column(name = "by_prod")
    private Boolean byProd;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "by_achat")
    private Boolean byAchat;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "proportion_pua")
    private Boolean proportionPua;
    @Size(max = 2147483647)
    @Column(name = "nature_prix_min")
    private String naturePrixMin = Constantes.NATURE_MTANT;
    @Column(name = "photo")
    private String photo;
    @Column(name = "photo_extension")
    private String photoExtension;
    @Size(max = 255)
    @Column(name = "code_barre")
    private String codeBarre;
    @Column(name = "marge_min")
    private Double margeMin;

    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "unite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure unite;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBasePublicites> publicites;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBaseArticlePack> packs;
    @OneToMany(mappedBy = "conditionnement", fetch = FetchType.LAZY)
    private List<YvsBaseArticleCodeBarre> codesBarres;
    @Transient
    private List<YvsComLotReception> lots;

    @Transient
    private String message_service, bytePhoto;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private double coefficient;
    @Transient
    private double stock;

    public YvsBaseConditionnement() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();

        packs = new ArrayList<>();
        codesBarres = new ArrayList<>();
    }

    public YvsBaseConditionnement(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseConditionnement(Long id, YvsBaseUniteMesure unite) {
        this(id);
        this.unite = unite;
    }

    public YvsBaseConditionnement(Long id, YvsBaseArticles article, YvsBaseUniteMesure unite) {
        this(id, unite);
        this.article = article;
    }

    public String getMessage_service() {
        return message_service;
    }

    public void setMessage_service(String message_service) {
        this.message_service = message_service;
    }

    public YvsBaseConditionnement(YvsBaseConditionnement y) {
        this();
        this.id = y.id;
        this.prixMin = y.prixMin;
        this.remise = y.remise;
        this.prix = y.prix;
        this.byVente = y.byVente;
        this.photo = y.photo;
        this.article = new YvsBaseArticles(y.article.getId());
        this.unite = y.unite;
        this.author = y.author;
        this.codeBarre = y.codeBarre;

        this.new_ = y.new_;
        this.select = y.select;
    }

    public Double getMargeMin() {
        return margeMin != null ? margeMin : 0;
    }

    public void setMargeMin(Double margeMin) {
        this.margeMin = margeMin;
    }

    public Double getPrixProd() {
        return prixProd != null ? prixProd : 0;
    }

    public void setPrixProd(Double prixProd) {
        this.prixProd = prixProd;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
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

    public Double getTauxPua() {
        return tauxPua != null ? tauxPua : 0;
    }

    public void setTauxPua(Double tauxPua) {
        this.tauxPua = tauxPua;
    }

    public Boolean getProportionPua() {
        return proportionPua != null ? proportionPua : false;
    }

    public void setProportionPua(Boolean proportionPua) {
        this.proportionPua = proportionPua;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Double getPrixAchat() {
        return prixAchat != null ? prixAchat : 0;
    }

    public void setPrixAchat(Double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public Double getPrixMin() {
        return prixMin != null ? prixMin : 0;
    }

    public void setPrixMin(Double prixMin) {
        this.prixMin = prixMin;
    }

    public Double getRemise() {
        return remise != null ? remise : 0;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public Double getPrix() {
        return prix != null ? prix : 0;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getNaturePrixMin() {
        return naturePrixMin != null ? (naturePrixMin.trim().length() > 0 ? naturePrixMin : yvs.dao.salaire.service.Constantes.NATURE_MTANT) : yvs.dao.salaire.service.Constantes.NATURE_MTANT;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
    }

    public Boolean getDefaut() {
        return defaut != null ? defaut : false;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }

    public Boolean getByVente() {
        return byVente != null ? byVente : false;
    }

    public void setByVente(Boolean byVente) {
        this.byVente = byVente;
    }

    public Boolean getByProd() {
        return byProd != null ? byProd : false;
    }

    public void setByProd(Boolean byProd) {
        this.byProd = byProd;
    }

    public Boolean getByAchat() {
        return byAchat != null ? byAchat : false;
    }

    public void setByAchat(Boolean byAchat) {
        this.byAchat = byAchat;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getPhotoExtension() {
        return photoExtension != null ? photoExtension.trim().length() > 0 ? photoExtension : "png" : "png";
    }

    public void setPhotoExtension(String photoExtension) {
        this.photoExtension = photoExtension;
    }

    public String getBytePhoto() {
        return bytePhoto;
    }

    public void setBytePhoto(String bytePhoto) {
        this.bytePhoto = bytePhoto;
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
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public YvsBaseUniteMesure getUnite() {
        return unite;
    }

    public void setUnite(YvsBaseUniteMesure unite) {
        this.unite = unite;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public List<YvsComLotReception> getLots() {
        return lots;
    }

    public void setLots(List<YvsComLotReception> lots) {
        this.lots = lots;
    }

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
    public List<YvsBaseArticlePack> getPacks() {
        return packs;
    }

    public void setPacks(List<YvsBaseArticlePack> packs) {
        this.packs = packs;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBasePublicites> getPublicites() {
        return publicites;
    }

    public void setPublicites(List<YvsBasePublicites> publicites) {
        this.publicites = publicites;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleCodeBarre> getCodesBarres() {
        return codesBarres;
    }

    public void setCodesBarres(List<YvsBaseArticleCodeBarre> codesBarres) {
        this.codesBarres = codesBarres;
    }

    @XmlTransient
    @JsonIgnore
    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
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
        if (!(object instanceof YvsBaseConditionnement)) {
            return false;
        }
        YvsBaseConditionnement other = (YvsBaseConditionnement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.produits.YvsBaseConditionnement[ id=" + id + " ]";
    }

}
