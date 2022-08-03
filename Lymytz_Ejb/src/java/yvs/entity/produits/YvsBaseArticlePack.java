/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.produits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_article_pack")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticlePack.findAll", query = "SELECT y FROM YvsBaseArticlePack y ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseArticlePack.findById", query = "SELECT y FROM YvsBaseArticlePack y WHERE y.id = :id ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseArticlePack.findByTitre", query = "SELECT y FROM YvsBaseArticlePack y WHERE y.designation = :designation ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseArticlePack.findByArticle", query = "SELECT y FROM YvsBaseArticlePack y WHERE y.article.article = :article ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseArticlePack.findByUnite", query = "SELECT y FROM YvsBaseArticlePack y WHERE y.article = :article ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseArticlePack.findByDateSave", query = "SELECT y FROM YvsBaseArticlePack y WHERE y.dateSave = :dateSave ORDER BY y.designation"),
    @NamedQuery(name = "YvsBaseArticlePack.findByDateUpdate", query = "SELECT y FROM YvsBaseArticlePack y WHERE y.dateUpdate = :dateUpdate ORDER BY y.designation")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseArticlePack extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_article_pack_id_seq", name = "yvs_base_article_pack_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_article_pack_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "photo")
    private String photo;
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement article;

    @OneToMany(mappedBy = "pack")
    private List<YvsBaseArticleContenuPack> contenus;

    @Transient
    private boolean new_;

    public YvsBaseArticlePack() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();

        contenus = new ArrayList<>();
    }

    public YvsBaseArticlePack(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
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

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public YvsBaseConditionnement getArticle() {
        return article;
    }

    public void setArticle(YvsBaseConditionnement article) {
        this.article = article;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseArticleContenuPack> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsBaseArticleContenuPack> contenus) {
        this.contenus = contenus;
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
        if (!(object instanceof YvsBaseArticlePack)) {
            return false;
        }
        YvsBaseArticlePack other = (YvsBaseArticlePack) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.produits.YvsBaseArticlePack[ id=" + id + " ]";
    }

}
