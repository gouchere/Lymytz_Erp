/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.produits;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_base_article_code_barre")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findAll", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.conditionnement.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findById", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByCode", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.codeBarre = :codeBarre AND y.conditionnement.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByCodeBarre", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.codeBarre = :codeBarre AND y.conditionnement.article = :article"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findArticleByCodeBarre", query = "SELECT y.conditionnement FROM YvsBaseArticleCodeBarre y WHERE y.codeBarre = :codeBarre AND y.conditionnement.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByConditionnement", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.conditionnement = :conditionnement"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByCodeConditionnement", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.conditionnement = :conditionnement AND y.codeBarre  = :codeBarre"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findIdArticleByCodeBarre", query = "SELECT DISTINCT y.conditionnement.id FROM YvsBaseArticleCodeBarre y WHERE y.codeBarre LIKE :codeBarre"),
    @NamedQuery(name = "YvsBaseArticleCodeBarre.findByArticle", query = "SELECT y FROM YvsBaseArticleCodeBarre y WHERE y.conditionnement.article = :article")})
public class YvsBaseArticleCodeBarre extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_article_code_barre_id_seq", name = "yvs_base_article_code_barre_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_article_code_barre_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "code_barre")
    private String codeBarre;
    @Column(name = "description")
    private String description;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;

    public YvsBaseArticleCodeBarre() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @XmlTransient
    @JsonIgnore
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

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsBaseArticleCodeBarre other = (YvsBaseArticleCodeBarre) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsBaseArticleCodeBarre{" + "id=" + id + '}';
    }

}
