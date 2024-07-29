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
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsSocietes;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_groupes_article")
@NamedQueries({
    @NamedQuery(name = "YvsBaseGroupesArticle.findCountAll", query = "SELECT COUNT(y) FROM YvsBaseGroupesArticle y WHERE y.societe =:societe ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findAll", query = "SELECT y FROM YvsBaseGroupesArticle y ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findNotIds", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.id NOT IN :ids ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findBySociete", query = "SELECT y FROM YvsBaseGroupesArticle y where y.societe = :societe"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findById", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.id = :id ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByNotId", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.id != :id ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByAuthor", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.author = :author ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByDescription", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.description = :description ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByRefgroupe", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.refgroupe = :refgroupe ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByRefgroupes", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.refgroupe = :refgroupe AND y.societe = :societe ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByCodeAppel", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.codeAppel = :codeAppel ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByCodeL", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.societe = :societe AND (y.codeAppel LIKE :code OR y.refgroupe LIKE :code OR y.designation LIKE :code) ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByActif", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.societe =:societe AND y.actif = :actif ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByVenteOnline", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.societe.venteOnline =:venteOnline AND y.actif = :actif ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByGroupeParent", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.groupeParent = :groupeParent ORDER BY y.designation"),

    @NamedQuery(name = "YvsBaseGroupesArticle.findParent", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.groupeParent IS NULL AND y.societe=:societe ORDER BY y.designation ASC"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findParentActif", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.societe =:societe AND y.groupeParent IS NULL AND y.actif = TRUE ORDER BY y.designation ASC"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findParentVenteOnline", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.societe.venteOnline =:venteOnline AND y.groupeParent IS NULL AND y.actif = TRUE ORDER BY y.designation ASC"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findSousActif", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.societe =:societe AND y.groupeParent IS NOT NULL AND y.actif = TRUE ORDER BY y.designation ASC"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findSousVenteOnline", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.societe.venteOnline =:venteOnline AND y.groupeParent IS NOT NULL AND y.actif = TRUE ORDER BY y.designation ASC"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findByParent", query = "SELECT y FROM YvsBaseGroupesArticle y WHERE y.groupeParent = :parent ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseGroupesArticle.findIdByParent", query = "SELECT y.id FROM YvsBaseGroupesArticle y WHERE y.groupeParent = :parent AND y.actif = TRUE ORDER BY y.designation ASC")})
public class YvsBaseGroupesArticle extends YvsEntity implements Serializable {

    @XmlTransient
    @JsonIgnore
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_groupesproduits_id_seq", name = "yvs_groupesproduits_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_groupesproduits_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "designation")
    private String designation;
    @Size(max = 255)
    @Column(name = "photo")
    private String photo;
    @Size(max = 255)
    @Column(name = "refgroupe")
    private String refgroupe;
    @Size(max = 2147483647)
    @Column(name = "code_appel")
    private String codeAppel;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "groupe_parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private YvsBaseGroupesArticle groupeParent;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "groupe")
    private List<YvsBaseArticles> yvsArticlesList;
    @OneToMany(mappedBy = "groupe")
    private List<YvsBaseArticlesTemplate> yvsArticlesTemplateList;
    @OneToMany(mappedBy = "groupeParent")
    private List<YvsBaseGroupesArticle> sous;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBaseGroupesArticle() {
    }

    public YvsBaseGroupesArticle(Long id) {
        this.id = id;
    }

    public YvsBaseGroupesArticle(Long id, String designation) {
        this(id);
        this.designation = designation;
    }

    public YvsBaseGroupesArticle(Long id, String refgroupe, String designation) {
        this(id, designation);
        this.refgroupe = refgroupe;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefgroupe() {
        return refgroupe;
    }

    public void setRefgroupe(String refgroupe) {
        this.refgroupe = refgroupe;
    }

    public String getCodeAppel() {
        return codeAppel;
    }

    public void setCodeAppel(String codeAppel) {
        this.codeAppel = codeAppel;
    }

    public String getDesignation() {
        return designation != null ? designation : "";
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseGroupesArticle getGroupeParent() {
        return groupeParent;
    }

    public void setGroupeParent(YvsBaseGroupesArticle groupeParent) {
        this.groupeParent = groupeParent;
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
    public List<YvsBaseArticlesTemplate> getYvsArticlesTemplateList() {
        return yvsArticlesTemplateList;
    }

    public void setYvsArticlesTemplateList(List<YvsBaseArticlesTemplate> yvsArticlesTemplateList) {
        this.yvsArticlesTemplateList = yvsArticlesTemplateList;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseGroupesArticle> getSous() {
        return sous;
    }

    public void setSous(List<YvsBaseGroupesArticle> sous) {
        this.sous = sous;
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
        if (!(object instanceof YvsBaseGroupesArticle)) {
            return false;
        }
        YvsBaseGroupesArticle other = (YvsBaseGroupesArticle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.produits.YvsBaseGroupesArticle[ id=" + id + " ]";
    }
}
