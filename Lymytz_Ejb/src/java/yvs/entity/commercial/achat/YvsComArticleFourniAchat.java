/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.achat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
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
import javax.validation.constraints.Size;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_article_fourni_achat")
@NamedQueries({
    @NamedQuery(name = "YvsComArticleFourniAchat.findAll", query = "SELECT y FROM YvsComArticleFourniAchat y WHERE y.article.article.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsComArticleFourniAchat.findByArticle", query = "SELECT y FROM YvsComArticleFourniAchat y WHERE y.article = :article"),
    @NamedQuery(name = "YvsComArticleFourniAchat.findById", query = "SELECT y FROM YvsComArticleFourniAchat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComArticleFourniAchat.findByFournisseur", query = "SELECT y FROM YvsComArticleFourniAchat y WHERE y.fournisseur = :fournisseur"),
    @NamedQuery(name = "YvsComArticleFourniAchat.findByPua", query = "SELECT y FROM YvsComArticleFourniAchat y WHERE y.pua = :pua"),
    @NamedQuery(name = "YvsComArticleFourniAchat.findByRemise", query = "SELECT y FROM YvsComArticleFourniAchat y WHERE y.remise = :remise"),
    @NamedQuery(name = "YvsComArticleFourniAchat.findByDateLivraison", query = "SELECT y FROM YvsComArticleFourniAchat y WHERE y.dateLivraison = :dateLivraison"),
    @NamedQuery(name = "YvsComArticleFourniAchat.findByNoEtat", query = "SELECT y FROM YvsComArticleFourniAchat y WHERE  y.article.article.article.famille.societe = :societe AND y.etat != :etat"),
    @NamedQuery(name = "YvsComArticleFourniAchat.findByEtat", query = "SELECT y FROM YvsComArticleFourniAchat y WHERE  y.article.article.article.famille.societe = :societe AND y.etat = :etat")})
public class YvsComArticleFourniAchat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_article_fourni_achat_id_seq", name = "yvs_com_article_fourni_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_article_fourni_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "pua")
    private Double pua;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "date_livraison")
    @Temporal(TemporalType.DATE)
    private Date dateLivraison;
    @Size(max = 2147483647)
    @Column(name = "etat")
    private String etat;
    
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComArticleApprovisionnement article;
    @JoinColumn(name = "fournisseur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFournisseur fournisseur;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsComArticleFourniAchat() {
    }

    public YvsComArticleFourniAchat(Long id) {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPua() {
        return pua;
    }

    public void setPua(Double pua) {
        this.pua = pua;
    }

    public Double getRemise() {
        return remise;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public YvsComArticleApprovisionnement getArticle() {
        return article;
    }

    public void setArticle(YvsComArticleApprovisionnement article) {
        this.article = article;
    }

    public YvsBaseFournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(YvsBaseFournisseur fournisseur) {
        this.fournisseur = fournisseur;
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
        if (!(object instanceof YvsComArticleFourniAchat)) {
            return false;
        }
        YvsComArticleFourniAchat other = (YvsComArticleFourniAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.achat.YvsComArticleFourniAchat[ id=" + id + " ]";
    }

}
