/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

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
import javax.validation.constraints.Size;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_gamme_article")
@NamedQueries({
    @NamedQuery(name = "YvsProdGammeArticle.findAll", query = "SELECT y FROM YvsProdGammeArticle y WHERE y.article.famille.societe =:societe ORDER BY y.codeRef, y.article.refArt ASC"),
    @NamedQuery(name = "YvsProdGammeArticle.findById", query = "SELECT y FROM YvsProdGammeArticle y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdGammeArticle.findByReference", query = "SELECT y FROM YvsProdGammeArticle y WHERE y.codeRef = :reference AND y.article.famille.societe =:societe AND y.masquer=false"),
    @NamedQuery(name = "YvsProdGammeArticle.findByArticleActif", query = "SELECT y FROM YvsProdGammeArticle y LEFT JOIN FETCH y.uniteTemps WHERE y.article = :article AND y.actif=true AND y.masquer=false ORDER BY y.principal DESC"),
    @NamedQuery(name = "YvsProdGammeArticle.findByPrincipalArticle", query = "SELECT y FROM YvsProdGammeArticle y WHERE y.article = :article AND y.principal = true AND y.masquer=false"),
    @NamedQuery(name = "YvsProdGammeArticle.CountByArticle", query = "SELECT COUNT(y) FROM YvsProdGammeArticle y WHERE y.article = :article AND y.actif= true AND y.masquer=false"),
    @NamedQuery(name = "YvsProdGammeArticle.findByArticle", query = "SELECT y FROM YvsProdGammeArticle y WHERE y.article = :article"),
    @NamedQuery(name = "YvsProdGammeArticle.CountByPrincipalArticle", query = "SELECT COUNT(y) FROM YvsProdGammeArticle y WHERE y.article = :article AND y.principal = true AND y.masquer=false"),
    @NamedQuery(name = "YvsProdGammeArticle.findByDesignation", query = "SELECT y FROM YvsProdGammeArticle y WHERE y.designation = :designation AND y.masquer=false"),
    @NamedQuery(name = "YvsProdGammeArticle.findByDescription", query = "SELECT y FROM YvsProdGammeArticle y WHERE y.description = :description AND y.masquer=false"),
    @NamedQuery(name = "YvsProdGammeArticle.findByActif", query = "SELECT y FROM YvsProdGammeArticle y WHERE y.actif = :actif AND y.masquer=false")})
public class YvsProdGammeArticle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_gamme_article_id_seq", name = "yvs_prod_gamme_article_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_gamme_article_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "code_ref")
    private String codeRef;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "principal")
    private Boolean principal;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "debut_validite")
    private Date debutValidite;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "fin_validite")
    private Date finValidite;
    @Column(name = "permanant")
    private Boolean permanant;
    @Column(name = "masquer")
    private Boolean masquer;

    @JoinColumn(name = "acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCodeAcces acces;
    @JoinColumn(name = "unite_temps", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure uniteTemps;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "gammeArticle")
    private List<YvsProdOperationsGamme> operations;
    @OneToMany(mappedBy = "gamme")
    private List<YvsProdGammeSite> sites;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsProdGammeArticle() {
        operations = new ArrayList<>();
        sites = new ArrayList<>();
    }

    public YvsProdGammeArticle(Long id) {
        this();
        this.id = id;
    }

    public YvsProdGammeArticle(Long id, String codeRef) {
        this(id);
        this.codeRef = codeRef;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeRef() {
        return codeRef != null ? codeRef : "";
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
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

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public List<YvsProdOperationsGamme> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsProdOperationsGamme> operations) {
        this.operations = operations;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getPrincipal() {
        return principal != null ? principal : false;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Date getDebutValidite() {
        return debutValidite != null ? debutValidite : new Date();
    }

    public void setDebutValidite(Date debutValidite) {
        this.debutValidite = debutValidite;
    }

    public Date getFinValidite() {
        return finValidite != null ? finValidite : new Date();
    }

    public void setFinValidite(Date finValidite) {
        this.finValidite = finValidite;
    }

    public Boolean getPermanant() {
        return permanant != null ? permanant : false;
    }

    public void setPermanant(Boolean permanant) {
        this.permanant = permanant;
    }

    public YvsBaseUniteMesure getUniteTemps() {
        return uniteTemps;
    }

    public void setUniteTemps(YvsBaseUniteMesure uniteTemps) {
        this.uniteTemps = uniteTemps;
    }

    public YvsBaseCodeAcces getAcces() {
        return acces;
    }

    public void setAcces(YvsBaseCodeAcces acces) {
        this.acces = acces;
    }

    public List<YvsProdGammeSite> getSites() {
        return sites;
    }

    public void setSites(List<YvsProdGammeSite> sites) {
        this.sites = sites;
    }

    public Boolean getMasquer() {
        return masquer != null ? masquer : false;
    }

    public void setMasquer(Boolean masquer) {
        this.masquer = masquer;
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
        if (!(object instanceof YvsProdGammeArticle)) {
            return false;
        }
        YvsProdGammeArticle other = (YvsProdGammeArticle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.YvsProdGammeArticle[ id=" + id + " ]";
    }

}
