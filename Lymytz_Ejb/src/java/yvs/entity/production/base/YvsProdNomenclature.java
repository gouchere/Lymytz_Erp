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
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseTableConversion;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.production.pilotage.YvsProdFicheConditionnement;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_nomenclature")
@NamedQueries({
    @NamedQuery(name = "YvsProdNomenclature.findCountAll", query = "SELECT COUNT(y) FROM YvsProdNomenclature y WHERE y.article.famille.societe=:societe AND y.actif=true"),
    @NamedQuery(name = "YvsProdNomenclature.findAll", query = "SELECT y FROM YvsProdNomenclature y WHERE y.article.famille.societe=:societe AND y.actif=true AND y.masquer=false ORDER BY y.article.refArt"),
    @NamedQuery(name = "YvsProdNomenclature.findById", query = "SELECT y FROM YvsProdNomenclature y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdNomenclature.findIdByArticle", query = "SELECT y.id FROM YvsProdNomenclature y WHERE y.article=:article AND y.actif=true AND y.masquer=false"),
    @NamedQuery(name = "YvsProdNomenclature.countByArticle", query = "SELECT COUNT(y) FROM YvsProdNomenclature y WHERE y.article=:article AND y.masquer=false"),
    @NamedQuery(name = "YvsProdNomenclature.countByArticleActif", query = "SELECT COUNT(y) FROM YvsProdNomenclature y WHERE y.article=:article AND y.actif=true AND y.masquer=false"),
    @NamedQuery(name = "YvsProdNomenclature.findByReference", query = "SELECT y FROM YvsProdNomenclature y WHERE y.reference = :reference AND y.article.famille.societe=:societe "),
    @NamedQuery(name = "YvsProdNomenclature.findByNiveau", query = "SELECT y FROM YvsProdNomenclature y WHERE y.niveau = :niveau"),
    @NamedQuery(name = "YvsProdNomenclature.findByArticleNiveau", query = "SELECT y FROM YvsProdNomenclature y WHERE y.niveau = :niveau AND y.article = :article"),
    @NamedQuery(name = "YvsProdNomenclature.findByArticleActif", query = "SELECT y FROM YvsProdNomenclature y WHERE y.actif = true AND y.masquer=false AND y.article = :article"),
    @NamedQuery(name = "YvsProdNomenclature.findByArticleFor", query = "SELECT y FROM YvsProdNomenclature y JOIN FETCH y.article JOIN FETCH y.uniteMesure JOIN FETCH y.uniteMesure.unite WHERE y.forConditionnement = :for AND y.article = :article AND y.actif=true AND y.masquer=false ORDER BY y.principal DESC"),
    @NamedQuery(name = "YvsProdNomenclature.findByDebutValidite", query = "SELECT y FROM YvsProdNomenclature y WHERE y.debutValidite = :debutValidite"),
    @NamedQuery(name = "YvsProdNomenclature.findByFinValidite", query = "SELECT y FROM YvsProdNomenclature y WHERE y.finValidite = :finValidite"),
    @NamedQuery(name = "YvsProdNomenclature.findByQuantite", query = "SELECT y FROM YvsProdNomenclature y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsProdNomenclature.findByCompose", query = "SELECT y FROM YvsProdNomenclature y WHERE y.article = :article"),
    @NamedQuery(name = "YvsProdNomenclature.findByArticlePrincipal", query = "SELECT y FROM YvsProdNomenclature y WHERE y.article = :article AND y.actif = true AND y.masquer=false"),
    @NamedQuery(name = "YvsProdNomenclature.findByArticlePrincipalJF", query = "SELECT y FROM YvsProdNomenclature y JOIN FETCH y.composants WHERE y.article = :article AND y.actif = true AND y.masquer=false"),
    @NamedQuery(name = "YvsProdNomenclature.findByPrincipal", query = "SELECT y FROM YvsProdNomenclature y WHERE y.article = :article AND y.actif = true AND y.principal=true AND y.masquer=false"),
    @NamedQuery(name = "YvsProdNomenclature.findByComposes", query = "SELECT y FROM YvsProdNomenclature y WHERE y.article.id IN :articles"),
    @NamedQuery(name = "YvsProdNomenclature.findByActif", query = "SELECT y FROM YvsProdNomenclature y WHERE y.actif = :actif AND y.masquer=false")})
public class YvsProdNomenclature implements Serializable {

    @OneToMany(mappedBy = "nomenclature")
    private List<YvsProdFicheConditionnement> yvsProdFicheConditionnementList;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_nomenclature_id_seq", name = "yvs_prod_nomenclature_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_nomenclature_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference")
    private String reference;
    @Column(name = "niveau")
    private Integer niveau;
    @Column(name = "debut_validite")
    @Temporal(TemporalType.DATE)
    private Date debutValidite;
    @Column(name = "fin_validite")
    @Temporal(TemporalType.DATE)
    private Date finValidite;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "marge_qte")
    private Double margeQte;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "quantite_lie_aux_composants")
    private Boolean quantiteLieAuxComposants;
    @Column(name = "principal")
    private Boolean principal;
    @Column(name = "alway_valide")
    private Boolean alwayValide;
    @Column(name = "for_conditionnement")
    private Boolean forConditionnement;
    @Column(name = "type_nomenclature")
    private String typeNomenclature;
    @Column(name = "masquer")
    private Boolean masquer;

    @JoinColumn(name = "acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCodeAcces acces;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "unite_mesure", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement uniteMesure;
    @OneToMany(mappedBy = "nomenclature")
    private List<YvsProdNomenclatureSite> sites;
    @OneToMany(mappedBy = "nomenclature")
    private List<YvsProdComposantNomenclature> composants;
    @Transient
    private String unite;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private Double valeur_total;

    public YvsProdNomenclature() {
        composants = new ArrayList<>();
        sites = new ArrayList<>();
    }

    public YvsProdNomenclature(Long id) {
        this();
        this.id = id;
    }

    public YvsProdNomenclature(Long id, String reference, YvsBaseArticles article) {
        this(id);
        this.reference = reference;
        this.article = article;
    }

    public YvsProdNomenclature(Long id, String reference, YvsBaseArticles article, YvsBaseConditionnement unite) {
        this(id, reference, article);
        this.uniteMesure = unite;
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

    public Boolean getForConditionnement() {
        return forConditionnement != null ? forConditionnement : false;
    }

    public void setForConditionnement(Boolean forConditionnement) {
        this.forConditionnement = forConditionnement;
    }    

    public YvsBaseConditionnement getUniteMesure() {
        return uniteMesure;
    }

    public void setUniteMesure(YvsBaseConditionnement uniteMesure) {
        this.uniteMesure = uniteMesure;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getTypeNomenclature() {
        return typeNomenclature!=null?typeNomenclature:Constantes.PROD_TYPE_NOMENCLATURE_PRODUCTION;
    }

    public void setTypeNomenclature(String typeNomenclature) {
        this.typeNomenclature = typeNomenclature;
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

    public String getReference() {
        return reference != null ? reference : "";
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getNiveau() {
        return niveau != null ? niveau : 0;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
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

    public YvsBaseCodeAcces getAcces() {
        return acces;
    }

    public void setAcces(YvsBaseCodeAcces acces) {
        this.acces = acces;
    }

    public Boolean getQuantiteLieAuxComposants() {
        return quantiteLieAuxComposants != null ? quantiteLieAuxComposants : false;
    }

    public void setQuantiteLieAuxComposants(Boolean quantiteLieAuxComposants) {
        this.quantiteLieAuxComposants = quantiteLieAuxComposants;
    }

    public Double getQuantite() {
//        if (getQuantiteLieAuxComposants()) {
//            quantite = 0.0;
//            for (YvsProdComposantNomenclature c : composants) {
//                if (c.getType().equals("N")) {
//                    if (c.getUnite() != null) {
//                        quantite += convertirUnite(c.getUnite().getUnite(), getUniteMesure().getUnite(), c.getQuantite());
//                    }
//                }
//            }
//        }
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
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

    public List<YvsProdComposantNomenclature> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsProdComposantNomenclature> composants) {
        this.composants = composants;
    }

    public Boolean getPrincipal() {
        return principal != null ? principal : false;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Boolean getAlwayValide() {
        return alwayValide != null ? alwayValide : false;
    }

    public void setAlwayValide(Boolean alwayValide) {
        this.alwayValide = alwayValide;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Double getMargeQte() {
        return margeQte != null ? margeQte : 0;
    }

    public void setMargeQte(Double margeQte) {
        this.margeQte = margeQte;
    }

    public List<YvsProdNomenclatureSite> getSites() {
        return sites;
    }

    public void setSites(List<YvsProdNomenclatureSite> sites) {
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
        if (!(object instanceof YvsProdNomenclature)) {
            return false;
        }
        YvsProdNomenclature other = (YvsProdNomenclature) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdNomenclature[ id=" + id + " ]";
    }

    public double convertirUnite(YvsBaseUniteMesure entree, YvsBaseUniteMesure sortie, double qte) {
        if ((sortie != null ? (sortie.getId() != null ? sortie.getId() > 0 : false) : false) && (entree != null ? (entree.getId() != null ? entree.getId() > 0 : false) : false)) {
            for (YvsBaseTableConversion tc : entree.getEquivalences()) {
                if (tc.getUniteEquivalent().getId().equals(sortie.getId())) {
                    return qte * tc.getTauxChange();
                }
            }
        }
        return 0;
    }

    public List<YvsProdFicheConditionnement> getYvsProdFicheConditionnementList() {
        return yvsProdFicheConditionnementList;
    }

    public void setYvsProdFicheConditionnementList(List<YvsProdFicheConditionnement> yvsProdFicheConditionnementList) {
        this.yvsProdFicheConditionnementList = yvsProdFicheConditionnementList;
    }

    public Double getValeur_total() {
        return valeur_total!=null?valeur_total:0;
    }

    public void setValeur_total(Double valeur_total) {
        this.valeur_total = valeur_total;
    }

}
