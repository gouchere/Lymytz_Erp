/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_tarif_point_livraison")
@NamedQueries({
    @NamedQuery(name = "YvsBaseTarifPointLivraison.findAll", query = "SELECT y FROM YvsBaseTarifPointLivraison y ORDER BY y.lieuxLiv.lieux.libele"),
    @NamedQuery(name = "YvsBaseTarifPointLivraison.findById", query = "SELECT y FROM YvsBaseTarifPointLivraison y WHERE y.id = :id ORDER BY y.lieuxLiv.lieux.libele"),
    @NamedQuery(name = "YvsBaseTarifPointLivraison.findByArticle", query = "SELECT y FROM YvsBaseTarifPointLivraison y JOIN FETCH y.lieuxLiv WHERE y.article = :article ORDER BY y.lieuxLiv.lieux.libele"),
    @NamedQuery(name = "YvsBaseTarifPointLivraison.findOne", query = "SELECT y FROM YvsBaseTarifPointLivraison y WHERE y.article=:article AND y.lieuxLiv=:lieux"),
    @NamedQuery(name = "YvsBaseTarifPointLivraison.findOneByVille", query = "SELECT y FROM YvsBaseTarifPointLivraison y WHERE y.article=:article AND y.lieuxLiv.lieux=:ville"),
    @NamedQuery(name = "YvsBaseTarifPointLivraison.findByLieuxSociete", query = "SELECT y FROM YvsBaseTarifPointLivraison y WHERE y.lieuxLiv = :lieux AND y.lieuxLiv.societe = :societe ORDER BY y.lieuxLiv.lieux.libele"),
    @NamedQuery(name = "YvsBaseTarifPointLivraison.findByParentArticle", query = "SELECT y FROM YvsBaseTarifPointLivraison y WHERE y.lieuxLiv.lieux = :lieux AND y.article = :article ORDER BY y.lieuxLiv.lieux.libele"),
    @NamedQuery(name = "YvsBaseTarifPointLivraison.findByLieux", query = "SELECT y FROM YvsBaseTarifPointLivraison y WHERE y.lieuxLiv.lieux = :lieux ORDER BY y.lieuxLiv.lieux.libele")})
public class YvsBaseTarifPointLivraison implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_tarif_point_livraison_id_seq", name = "yvs_base_tarif_point_livraison_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_tarif_point_livraison_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "frais_livraison")
    private Double fraisLivraison;
    @Column(name = "delai_retour")
    private Integer delaiRetour;
    @Column(name = "delai_for_livraison")
    private Integer delaiForLivraison;
    @Column(name = "delai_for_retrait")
    private Integer delaiForRetrait;
    @Column(name = "livraison_domicile")
    private Boolean livraisonDomicile;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "lieux_liv", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaireInformations lieuxLiv;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBaseTarifPointLivraison() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseTarifPointLivraison(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getFraisLivraison() {
        return fraisLivraison != null ? fraisLivraison : 0;
    }

    public void setFraisLivraison(Double fraisLivraison) {
        this.fraisLivraison = fraisLivraison;
    }

    public Boolean getLivraisonDomicile() {
        return livraisonDomicile != null ? livraisonDomicile : false;
    }

    public void setLivraisonDomicile(Boolean livraisonDomicile) {
        this.livraisonDomicile = livraisonDomicile;
    }

    public Integer getDelaiForLivraison() {
        return delaiForLivraison != null ? delaiForLivraison : 0;
    }

    public void setDelaiForLivraison(Integer delaiForLivraison) {
        this.delaiForLivraison = delaiForLivraison;
    }

    public Integer getDelaiForRetrait() {
        return delaiForRetrait != null ? delaiForRetrait : 0;
    }

    public void setDelaiForRetrait(Integer delaiForRetrait) {
        this.delaiForRetrait = delaiForRetrait;
    }

    public Integer getDelaiRetour() {
        return delaiRetour != null ? delaiRetour : 0;
    }

    public void setDelaiRetour(Integer delaiRetour) {
        this.delaiRetour = delaiRetour;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsDictionnaireInformations getLieuxLiv() {
        return lieuxLiv;
    }

    public void setLieuxLiv(YvsDictionnaireInformations lieuxLiv) {
        this.lieuxLiv = lieuxLiv;
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
        if (!(object instanceof YvsBaseTarifPointLivraison)) {
            return false;
        }
        YvsBaseTarifPointLivraison other = (YvsBaseTarifPointLivraison) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseTarifPointLivraison[ id=" + id + " ]";
    }

}
