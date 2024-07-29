/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_article_fournisseur")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleFournisseur.findAll", query = "SELECT y FROM YvsBaseArticleFournisseur y"),
    @NamedQuery(name = "YvsBaseArticleFournisseur.findById", query = "SELECT y FROM YvsBaseArticleFournisseur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleFournisseur.findByArticle", query = "SELECT DISTINCT y FROM YvsBaseArticleFournisseur y JOIN FETCH y.article  JOIN FETCH y.fournisseur WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleFournisseur.findByArticle1", query = "SELECT y FROM YvsBaseArticleFournisseur y JOIN FETCH y.fournisseur WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleFournisseur.findByFournisseur", query = "SELECT y FROM YvsBaseArticleFournisseur y WHERE y.fournisseur = :fournisseur"),
    @NamedQuery(name = "YvsBaseArticleFournisseur.findByFsseurArt", query = "SELECT y FROM YvsBaseArticleFournisseur y WHERE y.fournisseur = :fournisseur AND y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleFournisseur.findByDelaiLivraison", query = "SELECT y FROM YvsBaseArticleFournisseur y WHERE y.delaiLivraison = :delaiLivraison"),
    @NamedQuery(name = "YvsBaseArticleFournisseur.findByDureeGarantie", query = "SELECT y FROM YvsBaseArticleFournisseur y WHERE y.dureeGarantie = :dureeGarantie"),
    @NamedQuery(name = "YvsBaseArticleFournisseur.findByFournPrinc", query = "SELECT y FROM YvsBaseArticleFournisseur y WHERE y.article = :article AND y.principal = true"),

    @NamedQuery(name = "YvsBaseArticleFournisseur.findArticleByFournisseur", query = "SELECT y.article FROM YvsBaseArticleFournisseur y WHERE y.fournisseur = :fournisseur"),
    @NamedQuery(name = "YvsBaseArticleFournisseur.findIdFsseurByArticle", query = "SELECT y.fournisseur.id FROM YvsBaseArticleFournisseur y WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleFournisseur.findFsseurByArticle", query = "SELECT DISTINCT(y.fournisseur) FROM YvsBaseArticleFournisseur y WHERE y.article = :article ORDER By y.puv")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseArticleFournisseur extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_article_fournisseur_id_seq", name = "yvs_prod_article_fournisseur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_article_fournisseur_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "delai_livraison")
    private Double delaiLivraison;
    @Column(name = "duree_garantie")
    private Double dureeGarantie;
    @Column(name = "duree_vie")
    private Double dureeVie;
    @Size(max = 2147483647)
    @Column(name = "unite_delai")
    private String uniteDelai;
    @Size(max = 2147483647)
    @Column(name = "unite_garantie")
    private String uniteGarantie;
    @Size(max = 2147483647)
    @Column(name = "unite_vie")
    private String uniteVie;
    @Column(name = "pua_ttc")
    private Boolean puaTtc;
    @Column(name = "principal")
    private Boolean principal;
    @Column(name = "puv")
    private Double puv;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "nature_remise")
    private String natureRemise;
    @Column(name = "ref_art_externe")
    private String refArtExterne;
    @Column(name = "des_art_externe")
    private String desArtExterne;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "fournisseur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFournisseur fournisseur;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;

    @OneToMany(mappedBy = "article")
    private List<YvsBaseConditionnementFournisseur> conditionnements;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private Date dateLivraison;
    @Transient
    private YvsBaseConditionnementFournisseur conditionnement;
    @Transient
    private String message_service;

    public YvsBaseArticleFournisseur() {
        conditionnements = new ArrayList<>();
    }

    public String getMessage_service() {
        return message_service;
    }

    public void setMessage_service(String message_service) {
        this.message_service = message_service;
    }

    public YvsBaseArticleFournisseur(Long id) {
        this();
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
    public Date getDateLivraison() {
        Calendar cal = Calendar.getInstance();
        switch (getUniteDelai()) {
            case "H":
                cal.add(Calendar.HOUR_OF_DAY, getDelaiLivraison().intValue());
                break;
            case "S":
                cal.add(Calendar.WEEK_OF_YEAR, getDelaiLivraison().intValue());
                break;
            case "M":
                cal.add(Calendar.MONTH, getDelaiLivraison().intValue());
                break;
            case "A":
                cal.add(Calendar.YEAR, getDelaiLivraison().intValue());
                break;
            default:
                cal.add(Calendar.DAY_OF_YEAR, getDelaiLivraison().intValue());
                break;
        }
        dateLivraison = cal.getTime();
        return dateLivraison;
    }

    public Boolean getPuaTtc() {
        return puaTtc != null ? puaTtc : false;
    }

    public void setPuaTtc(Boolean puaTtc) {
        this.puaTtc = puaTtc;
    }

    public String getNatureRemise() {
        return natureRemise != null ? (natureRemise.trim().length() > 0 ? natureRemise : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureRemise(String natureRemise) {
        this.natureRemise = natureRemise;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getRefArtExterne() {
        return refArtExterne != null ? refArtExterne.trim().length() > 0 ? refArtExterne : getArticle() != null ? getArticle().getRefArt() : "" : "";
    }

    public void setRefArtExterne(String refArtExterne) {
        this.refArtExterne = refArtExterne;
    }

    public String getDesArtExterne() {
        return desArtExterne != null ? desArtExterne.trim().length() > 0 ? desArtExterne : getArticle() != null ? getArticle().getDesignation() : "" : "";
    }

    public void setDesArtExterne(String desArtExterne) {
        this.desArtExterne = desArtExterne;
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

    public Double getDelaiLivraison() {
        return delaiLivraison != null ? delaiLivraison : 0;
    }

    public Double getDureeVie() {
        return dureeVie != null ? dureeVie : 0;
    }

    public void setDureeVie(Double dureeVie) {
        this.dureeVie = dureeVie;
    }

    public void setDelaiLivraison(Double delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public Double getDureeGarantie() {
        return dureeGarantie != null ? dureeGarantie : 0;
    }

    public void setDureeGarantie(Double dureeGarantie) {
        this.dureeGarantie = dureeGarantie;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public Boolean getPrincipal() {
        return principal != null ? principal : false;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseFournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(YvsBaseFournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getUniteDelai() {
        return uniteDelai != null ? uniteDelai : "H";
    }

    public void setUniteDelai(String uniteDelai) {
        this.uniteDelai = uniteDelai;
    }

    public String getUniteGarantie() {
        return uniteGarantie != null ? uniteGarantie.trim().length() > 0 ? uniteGarantie : "H" : "H";
    }

    public void setUniteGarantie(String uniteGarantie) {
        this.uniteGarantie = uniteGarantie;
    }

    public String getUniteVie() {
        return uniteVie != null ? uniteVie.trim().length() > 0 ? uniteVie : "H" : "H";
    }

    public void setUniteVie(String uniteVie) {
        this.uniteVie = uniteVie;
    }

    public List<YvsBaseConditionnementFournisseur> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsBaseConditionnementFournisseur> conditionnements) {
        this.conditionnements = conditionnements;
    }

    public YvsBaseConditionnementFournisseur getConditionnement() {
        if (conditionnements != null) {
            if (conditionnements.size() == 1) {
                conditionnement = conditionnements.get(0);
            } else {
                for (YvsBaseConditionnementFournisseur y : conditionnements) {
                    if (y.getPrincipal()) {
                        conditionnement = y;
                        break;
                    }
                }
            }
        }
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnementFournisseur conditionnement) {
        this.conditionnement = conditionnement;
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
        if (!(object instanceof YvsBaseArticleFournisseur)) {
            return false;
        }
        YvsBaseArticleFournisseur other = (YvsBaseArticleFournisseur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.prod.YvsBaseArticleFournisseur[ id=" + id + " ]";
    }

}
