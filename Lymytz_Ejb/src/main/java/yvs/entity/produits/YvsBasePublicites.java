/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.produits;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_publicites")
@NamedQueries({
    @NamedQuery(name = "YvsBasePublicites.findAll", query = "SELECT y FROM YvsBasePublicites y"),
    @NamedQuery(name = "YvsBasePublicites.findById", query = "SELECT y FROM YvsBasePublicites y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePublicites.findByPriorite", query = "SELECT y FROM YvsBasePublicites y WHERE y.priorite = :priorite"),
    @NamedQuery(name = "YvsBasePublicites.findByDescription", query = "SELECT y FROM YvsBasePublicites y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBasePublicites.findByUrl", query = "SELECT y FROM YvsBasePublicites y WHERE y.url = :url"),
    @NamedQuery(name = "YvsBasePublicites.findByDateSave", query = "SELECT y FROM YvsBasePublicites y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBasePublicites.findByDateUpdate", query = "SELECT y FROM YvsBasePublicites y WHERE y.dateUpdate = :dateUpdate")})
public class YvsBasePublicites extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "priorite")
    private Integer priorite;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "image")
    private String image;
    @Size(max = 2147483647)
    @Column(name = "url")
    private String url;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement article;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBasePublicites() {
    }

    public YvsBasePublicites(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPriorite() {
        return priorite != null ? priorite : 1;
    }

    public void setPriorite(Integer priorite) {
        this.priorite = priorite;
    }

    public String getDescription() {
        return description != null ? description : "---";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image != null ? image : "";
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url != null ? url : "";
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getPermanent() {
        return permanent != null ? permanent : true;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateDebut() {
        return dateDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateFin() {
        return dateFin;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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
        if (!(object instanceof YvsBasePublicites)) {
            return false;
        }
        YvsBasePublicites other = (YvsBasePublicites) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.produits.YvsBasePublicites[ id=" + id + " ]";
    }

}
