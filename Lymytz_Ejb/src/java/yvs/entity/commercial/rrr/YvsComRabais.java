/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.rrr;

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
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseConditionnementPoint;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_rabais")
@NamedQueries({
    @NamedQuery(name = "YvsComRabais.findAll", query = "SELECT y FROM YvsComRabais y WHERE y.article.article.point.agence.societe = :societe"),
    @NamedQuery(name = "YvsComRabais.findById", query = "SELECT y FROM YvsComRabais y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComRabais.findByArticlePoint", query = "SELECT y FROM YvsComRabais y WHERE y.article.article = :article"),
    @NamedQuery(name = "YvsComRabais.findOne", query = "SELECT y FROM YvsComRabais y WHERE y.article.article = :article AND y.article.conditionnement = :conditionnement"),
    @NamedQuery(name = "YvsComRabais.findOneActif", query = "SELECT y FROM YvsComRabais y WHERE y.article.article.article = :article AND y.article.article.point = :point AND y.article.conditionnement = :conditionnement AND y.actif = TRUE"),
    @NamedQuery(name = "YvsComRabais.findOneActifDates", query = "SELECT y FROM YvsComRabais y WHERE y.article.article.article = :article AND y.article.article.point = :point AND y.article.conditionnement = :conditionnement AND ((:date BETWEEN y.dateDebut AND y.dateFin AND y.permanent = FALSE) OR (y.permanent = TRUE)) AND y.actif = TRUE"),
    @NamedQuery(name = "YvsComRabais.findByArticle", query = "SELECT y FROM YvsComRabais y WHERE y.article.article.article = :article"),
    @NamedQuery(name = "YvsComRabais.findByPoint", query = "SELECT y FROM YvsComRabais y WHERE y.article.article.point = :point"),
    @NamedQuery(name = "YvsComRabais.findByPointActif", query = "SELECT y FROM YvsComRabais y WHERE y.article.article.point = :point AND y.actif = true"),
    @NamedQuery(name = "YvsComRabais.findByPointArticle", query = "SELECT y FROM YvsComRabais y WHERE y.article.article.point = :point AND y.article.article.article = :article"),
    @NamedQuery(name = "YvsComRabais.findByPointArticleUnit", query = "SELECT y FROM YvsComRabais y WHERE y.article.article.point = :point AND y.article.article.article = :article AND y.article.conditionnement=:unite"),
    @NamedQuery(name = "YvsComRabais.findOtherByPointArticleUnit", query = "SELECT y FROM YvsComRabais y WHERE y.actif=TRUE AND y.article.article.point = :point AND y.article.article.article = :article AND y.article.conditionnement=:unite AND (y.permanent=TRUE OR (y.permanent=FALSE AND (:debut BETWEEN y.dateDebut AND y.dateFin OR :fin BETWEEN y.dateDebut AND y.dateFin)))"),
    @NamedQuery(name = "YvsComRabais.findByPointArticleActif", query = "SELECT y FROM YvsComRabais y WHERE y.article.article.point = :point AND y.article.article.article = :article AND y.actif = true")})
public class YvsComRabais extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_rabais_id_seq", name = "yvs_com_rabais_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_rabais_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "montant")
    private Double montant;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnementPoint article;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "rabais")
    private List<YvsComGrilleRabais> tranches;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean update;

    public YvsComRabais() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.tranches = new ArrayList<>();
    }

    public YvsComRabais(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }
    @XmlTransient
    @JsonIgnore
    public YvsBaseConditionnementPoint getArticle() {
        return article;
    }

    public void setArticle(YvsBaseConditionnementPoint article) {
        this.article = article;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getPermanent() {
        return permanent != null ? permanent : false;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComGrilleRabais> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsComGrilleRabais> tranches) {
        this.tranches = tranches;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsComRabais)) {
            return false;
        }
        YvsComRabais other = (YvsComRabais) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.rrr.YvsComRabais[ id=" + id + " ]";
    }

}
