/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.rrr;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_grille_ristourne")
@NamedQueries({
    @NamedQuery(name = "YvsComGrilleRistourne.findAll", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.ristourne.plan.societe = :societe"),
    @NamedQuery(name = "YvsComGrilleRistourne.findById", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByRistourne", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.ristourne = :ristourne"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByRistourneValeur", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.ristourne = :ristourne AND y.montantRistourne = :montant"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByPlan", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.ristourne.actif=true AND y.ristourne.plan=:plan"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByBonus", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.ristourne.actif=true AND y.ristourne.article= :article AND y.ristourne.conditionnement= :unite AND y.ristourne.nature=:nature AND (y.ristourne.permanent=true OR (y.ristourne.permanent=false AND (:date BETWEEN y.ristourne.dateDebut AND y.ristourne.dateFin))) AND y.ristourne.plan=:plan AND (:quantite BETWEEN y.montantMinimal AND y.montantMaximal)"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByRistourneMontant", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.ristourne = :ristourne AND :montant BETWEEN y.montantMinimal AND y.montantMaximal"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByRistourneMontants", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.ristourne = :ristourne AND y.montantMinimal <= :montantMinimal AND y.montantMaximal = :montantMaximal"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByMontantMinimal", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.montantMinimal = :montantMinimal"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByMontantMaximal", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.montantMaximal = :montantMaximal"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByMontantRistourne", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.montantRistourne = :montantRistourne"),
    @NamedQuery(name = "YvsComGrilleRistourne.findByNatureMontant", query = "SELECT y FROM YvsComGrilleRistourne y WHERE y.natureMontant = :natureMontant")})
public class YvsComGrilleRistourne implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_grille_ristourne_id_seq", name = "yvs_com_grille_ristourne_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_grille_ristourne_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "montant_minimal")
    private Double montantMinimal;
    @Column(name = "montant_maximal")
    private Double montantMaximal;
    @Column(name = "montant_ristourne")
    private Double montantRistourne;
    @Size(max = 2147483647)
    @Column(name = "nature_montant")
    private String natureMontant;
    @Size(max = 2147483647)
    @Column(name = "base")
    private String base;
    @JoinColumn(name = "ristourne", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComRistourne ristourne;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean update;
    @Transient
    private boolean unique;

    public YvsComGrilleRistourne() {
    }

    public YvsComGrilleRistourne(Long id) {
        this.id = id;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isUnique() {
        unique = ((getMontantMinimal() <= 1) && (getMontantMaximal() >= Double.MAX_VALUE || getMontantMaximal() <= 0));
        return !unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getBase() {
        return base != null ? (base.trim().length() > 0 ? base : Constantes.BASE_QTE) : Constantes.BASE_QTE;
    }

    public void setBase(String base) {
        this.base = base;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontantMinimal() {
        return montantMinimal != null ? montantMinimal : 0;
    }

    public void setMontantMinimal(Double montantMinimal) {
        this.montantMinimal = montantMinimal;
    }

    public Double getMontantMaximal() {
        return montantMaximal != null ? montantMaximal : 0;
    }

    public void setMontantMaximal(Double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public Double getMontantRistourne() {
        return montantRistourne != null ? montantRistourne : 0;
    }

    public void setMontantRistourne(Double montantRistourne) {
        this.montantRistourne = montantRistourne;
    }

    public String getNatureMontant() {
        return natureMontant != null ? (natureMontant.trim().length() > 0 ? natureMontant : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureMontant(String natureMontant) {
        this.natureMontant = natureMontant;
    }

    @XmlTransient  @JsonIgnore
    public YvsComRistourne getRistourne() {
        return ristourne;
    }

    @XmlTransient  @JsonIgnore
    public void setRistourne(YvsComRistourne ristourne) {
        this.ristourne = ristourne;
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
        if (!(object instanceof YvsComGrilleRistourne)) {
            return false;
        }
        YvsComGrilleRistourne other = (YvsComGrilleRistourne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.rrr.YvsComGrilleRistourne[ id=" + id + " ]";
    }

}
