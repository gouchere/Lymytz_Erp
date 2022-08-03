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
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_composant_nomenclature")
@NamedQueries({
    @NamedQuery(name = "YvsProdComposantNomenclature.findAll", query = "SELECT y FROM YvsProdComposantNomenclature y ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findById", query = "SELECT y FROM YvsProdComposantNomenclature y WHERE y.id = :id ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findByNomenclature_1", query = "SELECT y FROM YvsProdComposantNomenclature y WHERE y.nomenclature= :nomenclature ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findByOne", query = "SELECT y FROM YvsProdComposantNomenclature y WHERE y.article = :article AND y.unite = :unite AND y.nomenclature = :nomenclature ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findByQuantite", query = "SELECT y FROM YvsProdComposantNomenclature y WHERE y.quantite = :quantite ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findByType", query = "SELECT y FROM YvsProdComposantNomenclature y WHERE y.type = :type ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findByModeArrondi", query = "SELECT y FROM YvsProdComposantNomenclature y WHERE y.modeArrondi = :modeArrondi ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findByNomenclatureOf", query = "SELECT y FROM YvsProdComposantNomenclature y JOIN FETCH y.article JOIN FETCH y.unite JOIN FETCH y.unite.unite "
            + "WHERE y.nomenclature = :nomenclature AND y.actif=true AND y.alternatif=false ORDER BY y.ordre, y.quantite ASC "),
    @NamedQuery(name = "YvsProdComposantNomenclature.findByNomenclature", query = "SELECT y FROM YvsProdComposantNomenclature y JOIN FETCH "
            + "y.nomenclature JOIN FETCH y.article JOIN FETCH y.unite WHERE y.nomenclature = :nomenclature AND y.actif=true AND y.alternatif=false ORDER BY y.ordre, y.quantite ASC"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findComposantArticle", query = "SELECT y FROM YvsProdComposantNomenclature y WHERE y.nomenclature.article=:article AND y.actif=true ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findComposantArticleByNom", query = "SELECT y FROM YvsProdComposantNomenclature y  JOIN FETCH "
            + "y.nomenclature JOIN FETCH y.article JOIN FETCH y.unite WHERE y.article=:article AND y.nomenclature=:nomenclature AND y.actif=true ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findComposantNomByArticleP", query = "SELECT y FROM YvsProdComposantNomenclature y  JOIN FETCH y.nomenclature "
            + "JOIN FETCH y.article JOIN FETCH y.unite "
            + "WHERE y.nomenclature.article=:article AND y.nomenclature.actif=true AND y.actif=true AND y.nomenclature.masquer=false AND y.nomenclature.principal=true ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findComposantNomByArticle", query = "SELECT y FROM YvsProdComposantNomenclature y  JOIN FETCH y.nomenclature "
            + "JOIN FETCH y.article JOIN FETCH y.unite "
            + "WHERE y.nomenclature.article=:article AND y.nomenclature.actif=true AND y.actif=true AND y.nomenclature.masquer=false ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findModeArrondi", query = "SELECT y.modeArrondi FROM YvsProdComposantNomenclature y WHERE y.article=:article AND y.nomenclature=:nomenclature"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findIdsNomByComposant", query = "SELECT DISTINCT y.nomenclature.id FROM YvsProdComposantNomenclature y WHERE (UPPER(y.article.refArt) LIKE UPPER(:article) OR UPPER(y.article.designation) LIKE UPPER(:article)) AND y.nomenclature.article.famille.societe=:societe"),
    @NamedQuery(name = "YvsProdComposantNomenclature.findOrdreByNom", query = "SELECT y.ordre FROM YvsProdComposantNomenclature y WHERE y.nomenclature=:nomenclature AND y.article=:article "),
    @NamedQuery(name = "YvsProdComposantNomenclature.findByActif", query = "SELECT y FROM YvsProdComposantNomenclature y WHERE y.actif = :actif ORDER BY y.ordre")})
public class YvsProdComposantNomenclature implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_composant_nomenclature_id_seq", name = "yvs_prod_composant_nomenclature_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_composant_nomenclature_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "coefficient")
    private Double coefficient;
    @Column(name = "ordre")
    private Integer ordre;
    @Size(max = 2147483647)
    @Column(name = "type")
    private String type;
    @Size(max = 2147483647)
    @Column(name = "mode_arrondi")
    private String modeArrondi;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "stockable")
    private Boolean stockable;
    @Column(name = "inside_cout")
    private Boolean insideCout;
    @Column(name = "alternatif")
    private Boolean alternatif;
    @Column(name = "free_use")
    private Boolean freeUse;   // Indique que l'utilisation du composant dépend de l'utilisateur et donc n'est pas strictement lié à la nomenclature

    @JoinColumn(name = "nomenclature", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdNomenclature nomenclature;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "unite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement unite;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "composant_lie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdComposantNomenclature composantLie;

    @OneToMany(mappedBy = "composantNomenclature", fetch = FetchType.LAZY)
    private List<YvsProdComposantMrp> composants;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private Double pr;
    @Transient
    private Double valeur;

    public YvsProdComposantNomenclature() {
        composants = new ArrayList<>();
    }

    public YvsProdComposantNomenclature(Long id) {
        this();
        this.id = id;
    }

    public YvsProdComposantNomenclature(YvsProdComposantNomenclature c) {
        this(c.id);
        this.actif = c.actif;
        this.article = c.article;
        this.author = c.author;
        this.coefficient = c.coefficient;
        this.composantLie = c.composantLie;
        this.dateSave = c.dateSave;
        this.dateUpdate = c.dateUpdate;
        this.modeArrondi = c.modeArrondi;
        this.ordre = c.ordre;
        this.quantite = c.quantite;
        this.nomenclature = c.nomenclature;
        this.stockable = c.stockable;
        this.type = c.type;
        this.unite = c.unite;
        this.alternatif = c.alternatif;
        this.insideCout = c.insideCout;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public YvsBaseConditionnement getUnite() {
        return unite;
    }

    public void setUnite(YvsBaseConditionnement unite) {
        this.unite = unite;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getInsideCout() {
        return insideCout != null ? insideCout : false;
    }

    public void setInsideCout(Boolean insideCout) {
        this.insideCout = insideCout;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Double getCoefficient() {
        return coefficient != null ? coefficient : 0;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : "N" : "N";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModeArrondi() {
        return modeArrondi != null ? modeArrondi.trim().length() > 0 ? modeArrondi : "E" : "E";
    }

    public void setModeArrondi(String modeArrondi) {
        this.modeArrondi = modeArrondi;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public List<YvsProdComposantMrp> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsProdComposantMrp> composants) {
        this.composants = composants;
    }

    public YvsProdNomenclature getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(YvsProdNomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public Boolean getStockable() {
        return stockable != null ? stockable : false;
    }

    public void setStockable(Boolean stockable) {
        this.stockable = stockable;
    }

    public Integer getOrdre() {
        return ordre != null ? ordre : 0;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public YvsProdComposantNomenclature getComposantLie() {
        return composantLie;
    }

    public void setComposantLie(YvsProdComposantNomenclature composantLie) {
        this.composantLie = composantLie;
    }

    public Boolean getAlternatif() {
        return alternatif != null ? alternatif : false;
    }

    public void setAlternatif(Boolean alternatif) {
        this.alternatif = alternatif;
    }

    public Double getPr() {
        return pr;
    }

    public void setPr(Double pr) {
        this.pr = pr;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Boolean getFreeUse() {
        return freeUse != null ? freeUse : false;
    }

    public void setFreeUse(Boolean freeUse) {
        this.freeUse = freeUse;
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
        if (!(object instanceof YvsProdComposantNomenclature)) {
            return false;
        }
        YvsProdComposantNomenclature other = (YvsProdComposantNomenclature) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdComposantNomenclature[ id=" + id + " ]";
    }

}
