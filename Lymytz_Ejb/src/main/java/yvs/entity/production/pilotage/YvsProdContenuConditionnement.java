/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_prod_contenu_conditionnement")
@NamedQueries({
    @NamedQuery(name = "YvsProdContenuConditionnement.findAll", query = "SELECT y FROM YvsProdContenuConditionnement y"),
    @NamedQuery(name = "YvsProdContenuConditionnement.findById", query = "SELECT y FROM YvsProdContenuConditionnement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdContenuConditionnement.findByQuantite", query = "SELECT y FROM YvsProdContenuConditionnement y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsProdContenuConditionnement.findByDateSave", query = "SELECT y FROM YvsProdContenuConditionnement y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdContenuConditionnement.findByDateUpdate", query = "SELECT y FROM YvsProdContenuConditionnement y WHERE y.dateUpdate = :dateUpdate"),

    @NamedQuery(name = "YvsProdContenuConditionnement.findOne", query = "SELECT y FROM YvsProdContenuConditionnement y WHERE y.article = :article AND y.conditionnement = :unite AND y.fiche = :fiche")})
public class YvsProdContenuConditionnement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_contenu_conditionnement_id_seq", name = "yvs_prod_contenu_conditionnement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_contenu_conditionnement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "consommable")
    private Boolean consommable;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "calcul_pr")
    private Boolean calculPr;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "fiche", referencedColumnName = "id")
    @ManyToOne
    private YvsProdFicheConditionnement fiche;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticles article;

    @Transient
    private String modeArrondi;

    @Transient
    private boolean new_;

    public YvsProdContenuConditionnement() {
    }

    public YvsProdContenuConditionnement(Long id) {
        this.id = id;
    }

    public YvsProdContenuConditionnement(Long id, YvsUsersAgence author) {
        this(id);
        this.author = author;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCalculPr() {
        return calculPr != null ? calculPr : true;
    }

    public void setCalculPr(Boolean calculPr) {
        this.calculPr = calculPr;
    }

    public Boolean getConsommable() {
        return consommable != null ? consommable : false;
    }

    public void setConsommable(Boolean consommable) {
        this.consommable = consommable;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsProdFicheConditionnement getFiche() {
        return fiche;
    }

    public void setFiche(YvsProdFicheConditionnement fiche) {
        this.fiche = fiche;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public String getModeArrondi() {
        return modeArrondi;
    }

    public void setModeArrondi(String modeArrondi) {
        this.modeArrondi = modeArrondi;
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
        if (!(object instanceof YvsProdContenuConditionnement)) {
            return false;
        }
        YvsProdContenuConditionnement other = (YvsProdContenuConditionnement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdContenuConditionnement[ id=" + id + " ]";
    }

}
