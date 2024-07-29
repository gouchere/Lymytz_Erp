/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_article_categorie_comptable", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findAll", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findAllCount", query = "SELECT COUNT(y) FROM YvsBaseArticleCategorieComptable y WHERE y.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findSimpleByArticle", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByArticle", query = "SELECT y FROM YvsBaseArticleCategorieComptable y JOIN FETCH y.categorie JOIN FETCH y.compte WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByCategorie", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.categorie = :categorie"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByCompte", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.compte = :compte"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.countByCategorieArticle", query = "SELECT COUNT(y) FROM YvsBaseArticleCategorieComptable y WHERE y.categorie = :categorie AND y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByCategorieArticle", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.categorie = :categorie AND y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByCategorieTemplate", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.categorie = :categorie AND y.template = :template"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByCategorieCompte", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.categorie = :categorie AND y.compte = :compte"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByArticleCompte", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.article = :article AND y.compte = :compte"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findAllArticleByCompte", query = "SELECT y.article.refArt, y.article.designation FROM YvsBaseArticleCategorieComptable y WHERE y.compte = :compte"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByCategorieArticleNoCompte", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.categorie = :categorie AND y.article = :article AND y.compte IS NULL"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByCategorieArticleCompte", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.categorie = :categorie AND y.article = :article AND y.compte = :compte"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findById", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptable.findByActif", query = "SELECT y FROM YvsBaseArticleCategorieComptable y WHERE y.actif = :actif")})
public class YvsBaseArticleCategorieComptable extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_compte_article_id_seq", name = "yvs_base_compte_article_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_compte_article_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif;
    
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compte;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieComptable categorie;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "template", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticlesTemplate template;
    @OneToMany(mappedBy = "articleCategorie")
    private List<YvsBaseArticleCategorieComptableTaxe> taxes;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private String numCompte;
    @Transient
    private String numTaxe;
    @Transient
    private boolean error;
    @Transient
    private boolean forTemplate;
    @Transient
    private String message;

    public YvsBaseArticleCategorieComptable() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        taxes = new ArrayList<>();
    }

    public YvsBaseArticleCategorieComptable(Long id) {
        this();
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public boolean isForTemplate() {
        forTemplate = getTemplate() != null ? (getTemplate().getId() != null ? getTemplate().getId() > 0 : false) : false;
        return forTemplate;
    }

    public void setForTemplate(boolean forTemplate) {
        this.forTemplate = forTemplate;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getNumTaxe() {
        numTaxe = null;
        if (taxes != null ? !taxes.isEmpty() : false) {
            for (YvsBaseArticleCategorieComptableTaxe a : taxes) {
                if (numTaxe == null) {
                    numTaxe = a.getTaxe().getCodeTaxe();
                } else {
                    numTaxe += " / " + a.getTaxe().getCodeTaxe();
                }
            }
        }
        return numTaxe;
    }

    public String viewNumTaxe(boolean all) {
        String num = null;
        if (taxes != null ? !taxes.isEmpty() : false) {
            for (YvsBaseArticleCategorieComptableTaxe a : taxes) {
                if (!a.isNew_() || all && a.getTaxe() != null) {
                    if (num == null) {
                        num = a.getTaxe().getCodeTaxe();
                    } else {
                        num += " / " + a.getTaxe().getCodeTaxe();
                    }
                }
            }
        }
        return num;
    }

    public void setNumTaxe(String numTaxe) {
        this.numTaxe = numTaxe;
    }

    public String getNumCompte() {
        return numCompte != null ? numCompte : "";
    }

    public void setNumCompte(String numCompte) {
        this.numCompte = numCompte;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActif() {
        return actif;
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

    public YvsBaseCategorieComptable getCategorie() {
        return categorie;
    }

    public void setCategorie(YvsBaseCategorieComptable categorie) {
        this.categorie = categorie;
    }

    
    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public List<YvsBaseArticleCategorieComptableTaxe> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsBaseArticleCategorieComptableTaxe> taxes) {
        this.taxes = taxes;
    }

    @XmlTransient
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
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
        if (!(object instanceof YvsBaseArticleCategorieComptable)) {
            return false;
        }
        YvsBaseArticleCategorieComptable other = (YvsBaseArticleCategorieComptable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseArticleCategorieComptable[ id=" + id + " ]";
    }

}
