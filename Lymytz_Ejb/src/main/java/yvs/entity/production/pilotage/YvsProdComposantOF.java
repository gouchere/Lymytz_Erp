/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_composant_of")
@NamedQueries({
    @NamedQuery(name = "YvsProdComposantOF.findAll", query = "SELECT y FROM YvsProdComposantOF y ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantOF.findById", query = "SELECT y FROM YvsProdComposantOF y WHERE y.id = :id ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantOF.findByOne", query = "SELECT y FROM YvsProdComposantOF y WHERE y.ordreFabrication = :ordre AND y.article = :article AND y.unite = :unite ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantOF.findByOneLot", query = "SELECT y FROM YvsProdComposantOF y WHERE y.ordreFabrication = :ordre AND y.article = :article AND y.unite = :unite AND y.lotSortie = :lot ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantOF.findByOF", query = "SELECT y FROM YvsProdComposantOF y JOIN FETCH y.ordreFabrication JOIN FETCH y.article JOIN FETCH y.unite.unite "
            + "WHERE y.ordreFabrication = :ordre ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantOF.findByQuantitePrevu", query = "SELECT y FROM YvsProdComposantOF y WHERE y.quantitePrevu = :quantitePrevu ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantOF.findByQuantiteValide", query = "SELECT y FROM YvsProdComposantOF y WHERE y.quantiteValide = :quantiteValide ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantOF.findByModeArrondi", query = "SELECT y FROM YvsProdComposantOF y WHERE y.modeArrondi = :modeArrondi ORDER BY y.ordre"),
    @NamedQuery(name = "YvsProdComposantOF.findByUnite", query = "SELECT y FROM YvsProdComposantOF y WHERE y.unite = :unite ORDER BY y.ordreFabrication.codeRef DESC"),
    @NamedQuery(name = "YvsProdComposantOF.findByUniteNoId", query = "SELECT y FROM YvsProdComposantOF y WHERE y.unite = :unite AND y.id != :id ORDER BY y.ordreFabrication.codeRef DESC"),
    @NamedQuery(name = "YvsProdComposantOF.findByCommentaire", query = "SELECT y FROM YvsProdComposantOF y WHERE y.commentaire = :commentaire ORDER BY y.ordre")})
public class YvsProdComposantOF implements Serializable, Comparator<YvsProdComposantOF> {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_composant_ordre_fabrication_id_seq", name = "yvs_prod_composant_ordre_fabrication_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_composant_ordre_fabrication_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    @Min(value = 0)
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite_prevu")
    private Double quantitePrevu;
    @Column(name = "quantite_valide")
    private Double quantiteValide;
    @Size(max = 2147483647)
    @Column(name = "mode_arrondi")
    private String modeArrondi;
    @Column(name = "etat_composant")
    private String etatComposant;   //en attente, consommé
    @Size(max = 2147483647)
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "cout_composant")
    private Double coutComposant;
    @Column(name = "coefficient")
    private Double coefficient;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "ordre")
    private Integer ordre;
    @Size(max = 2147483647)
    @Column(name = "type")
    private String type;
    @Column(name = "inside_cout")
    private Boolean insideCout;
    @Column(name = "free_use")
    private Boolean freeUse;   // Indique que l'utilisation du composant dépend de l'utilisateur et donc n'est pas strictement lié à la nomenclature

    @JoinColumn(name = "ordre_fabrication", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdOrdreFabrication ordreFabrication;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "depot_conso", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depotConso;
    @JoinColumn(name = "unite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement unite;
    @JoinColumn(name = "lot_sortie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComLotReception lotSortie;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private YvsBaseFournisseur principal;
    @Transient
    private Long idDepot;
    @Transient
    private Long idDepot_cible;
    @Transient
    private YvsBaseDepots depot_cible;
    @Transient
    private List<YvsProdFluxComposant> composantsUsed;
    @Transient
    private Double stock;
    @Transient
    private Long idFournisseur;

    @Transient
    private List<YvsBaseArticleFournisseur> fournisseurs;
    @Transient
    private List<YvsBaseFournisseur> fournis;

    public YvsProdComposantOF() {
        fournisseurs = new ArrayList<>();
        fournis = new ArrayList<>();

    }

    public YvsProdComposantOF(Long id) {
        this();
        this.id = id;
    }

    public YvsProdComposantOF(YvsBaseArticles article, YvsBaseConditionnement unite) {
        this();
        this.article = article;
        this.unite = unite;
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

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : "N" : "N";
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInsideCout() {
        return insideCout != null ? insideCout : false;
    }

    public void setInsideCout(Boolean insideCout) {
        this.insideCout = insideCout;
    }

    public Double getQuantitePrevu() {
        return quantitePrevu != null ? quantitePrevu : 0;
    }

    public void setQuantitePrevu(Double quantitePrevu) {
        this.quantitePrevu = quantitePrevu;
    }

    public Double getQuantiteValide() {
        return quantiteValide != null ? quantiteValide : 0;
    }

    public void setQuantiteValide(Double quantiteValide) {
        this.quantiteValide = quantiteValide;
    }

    public String getModeArrondi() {
        return modeArrondi != null ? modeArrondi.trim().length() > 0 ? modeArrondi : "E" : "E";
    }

    public void setModeArrondi(String modeArrondi) {
        this.modeArrondi = modeArrondi;
    }

    public String getCommentaire() {
        return commentaire != null ? commentaire : "";
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public YvsProdOrdreFabrication getOrdreFabrication() {
        return ordreFabrication;
    }

    public void setOrdreFabrication(YvsProdOrdreFabrication ordreFabrication) {
        this.ordreFabrication = ordreFabrication;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public String getEtatComposant() {
        return etatComposant != null ? etatComposant.trim().length() > 0 ? etatComposant : "W" : "W";
    }

    public void setEtatComposant(String etatComposant) {
        this.etatComposant = etatComposant;
    }

    public Double getCoutComposant() {
        return coutComposant != null ? coutComposant : 0;
    }

    public void setCoutComposant(Double coutComposant) {
        this.coutComposant = coutComposant;
    }

    public Double getCoefficient() {
        return coefficient != null ? coefficient : 1;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public YvsBaseDepots getDepotConso() {
        return depotConso;
    }

    public void setDepotConso(YvsBaseDepots depotConso) {
        this.depotConso = depotConso;
    }

    public Double getStock() {
        return stock != null ? stock : 0;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public YvsBaseConditionnement getUnite() {
        return unite;
    }

    public void setUnite(YvsBaseConditionnement unite) {
        this.unite = unite;
    }

    public YvsComLotReception getLotSortie() {
        return lotSortie;
    }

    public void setLotSortie(YvsComLotReception lotSortie) {
        this.lotSortie = lotSortie;
    }

    public List<YvsProdFluxComposant> getComposantsUsed() {
        return composantsUsed;
    }

    public void setComposantsUsed(List<YvsProdFluxComposant> composantsUsed) {
        this.composantsUsed = composantsUsed;
    }

    public Integer getOrdre() {
        return ordre != null ? ordre : 0;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
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
        if (!(object instanceof YvsProdComposantOF)) {
            return false;
        }
        YvsProdComposantOF other = (YvsProdComposantOF) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdComposantOrdreFabrication[ id=" + id + " ]";
    }

    public YvsBaseFournisseur getPrincipal() {
        return principal != null ? principal : new YvsBaseFournisseur();
    }

    public void setPrincipal(YvsBaseFournisseur principal) {
        this.principal = principal;
    }

    public Long getIdDepot() {
        return idDepot;
    }

    public void setIdDepot(Long idDepot) {
        this.idDepot = idDepot;
    }

    public Long getIdDepot_cible() {
        return idDepot_cible;
    }

    public void setIdDepot_cible(Long idDepot_cible) {
        this.idDepot_cible = idDepot_cible;
    }

    public YvsBaseDepots getDepot_cible() {
        return depot_cible;
    }

    public void setDepot_cible(YvsBaseDepots depot_cible) {
        this.depot_cible = depot_cible;
    }

    public List<YvsBaseArticleFournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<YvsBaseArticleFournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    public Long getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(Long idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public List<YvsBaseFournisseur> getFournis() {
        return fournis;
    }

    public void setFournis(List<YvsBaseFournisseur> fournis) {
        this.fournis = fournis;
    }

    @Override
    public int compare(YvsProdComposantOF o1, YvsProdComposantOF o2) {
        if (o1 != null && o2 != null) {
            return o1.getOrdre().compareTo(o2.getOrdre());
        } else if (o1 != null) {
            return 1;
        } else {
            return -1;

        }
    }
}
