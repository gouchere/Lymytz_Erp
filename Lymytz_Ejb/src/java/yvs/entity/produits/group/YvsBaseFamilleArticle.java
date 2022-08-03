/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.produits.group;

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
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsSocietes;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.production.planification.YvsProdDetailPic;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_famille_article")
@NamedQueries({
    @NamedQuery(name = "YvsBaseFamilleArticle.findCountAll", query = "SELECT COUNT(y) FROM YvsBaseFamilleArticle y WHERE y.societe =:societe"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findAll", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.societe =:societe ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findActif", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.societe =:societe AND y.actif = TRUE ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findById", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByNotId", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.id != :id AND y.societe =:societe ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByReferenceL", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE (y.referenceFamille LIKE :code OR y.designation LIKE :code) AND y.societe=:societe "),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByReference", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.referenceFamille = :code AND y.societe=:societe "),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByDesignation", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByDescription", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findParent", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.familleParent is null AND y.societe = :societe ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseFamilleArticle.findByParent", query = "SELECT y FROM YvsBaseFamilleArticle y WHERE y.familleParent = :parent")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseFamilleArticle extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_famille_article_id_seq", name = "yvs_prod_famille_article_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_famille_article_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "reference_famille")
    private String referenceFamille;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "prefixe")
    private String prefixe;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "famille_parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFamilleArticle familleParent;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "famille")
    private List<YvsBaseArticles> yvsArticlesList;
    @OneToMany(mappedBy = "familleParent")
    private List<YvsBaseFamilleArticle> yvsProdFamilleArticleList;
    @OneToMany(mappedBy = "famille")
    private List<YvsProdDetailPic> yvsProdDetailPicList;
    @OneToMany(mappedBy = "famille")
    private List<YvsBaseArticlesTemplate> yvsArticlesTemplateList;

    @Transient
    private boolean select;
    @Transient
    private boolean update;
    @Transient
    private boolean new_;
    @Transient
    private String message;

    public YvsBaseFamilleArticle() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseFamilleArticle(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseFamilleArticle(String ref) {
        this();
        this.referenceFamille = ref;
    }

    public YvsBaseFamilleArticle(Long id, String designation) {
        this(id);
        this.designation = designation;
    }

    public YvsBaseFamilleArticle(Long id, String referenceFamille, String designation) {
        this(id, designation);
        this.referenceFamille = referenceFamille;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public String getPrefixe() {
        return prefixe;
    }

    public void setPrefixe(String prefixe) {
        this.prefixe = prefixe;
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

    public String getReferenceFamille() {
        return referenceFamille;
    }

    public void setReferenceFamille(String referenceFamille) {
        this.referenceFamille = referenceFamille;
    }

    public String getDesignation() {
        return designation != null ? designation : "";
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticles> getYvsArticlesList() {
        return yvsArticlesList;
    }

    public void setYvsArticlesList(List<YvsBaseArticles> yvsArticlesList) {
        this.yvsArticlesList = yvsArticlesList;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseFamilleArticle> getYvsBaseFamilleArticleList() {
        return yvsProdFamilleArticleList;
    }

    public void setYvsBaseFamilleArticleList(List<YvsBaseFamilleArticle> yvsProdFamilleArticleList) {
        this.yvsProdFamilleArticleList = yvsProdFamilleArticleList;
    }

    public YvsBaseFamilleArticle getFamilleParent() {
        return familleParent;
    }

    public void setFamilleParent(YvsBaseFamilleArticle familleParent) {
        this.familleParent = familleParent;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseFamilleArticle> getYvsProdFamilleArticleList() {
        return yvsProdFamilleArticleList;
    }

    public void setYvsProdFamilleArticleList(List<YvsBaseFamilleArticle> yvsProdFamilleArticleList) {
        this.yvsProdFamilleArticleList = yvsProdFamilleArticleList;
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
        if (!(object instanceof YvsBaseFamilleArticle)) {
            return false;
        }
        YvsBaseFamilleArticle other = (YvsBaseFamilleArticle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.produits.YvsBaseFamilleArticle[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticlesTemplate> getYvsArticlesTemplateList() {
        return yvsArticlesTemplateList;
    }

    public void setYvsArticlesTemplateList(List<YvsBaseArticlesTemplate> yvsArticlesTemplateList) {
        this.yvsArticlesTemplateList = yvsArticlesTemplateList;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsProdDetailPic> getYvsProdDetailPicList() {
        return yvsProdDetailPicList;
    }

    public void setYvsProdDetailPicList(List<YvsProdDetailPic> yvsProdDetailPicList) {
        this.yvsProdDetailPicList = yvsProdDetailPicList;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

}
