/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

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
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_prod_declaration_production")
@NamedQueries({
    @NamedQuery(name = "YvsProdDeclarationProduction.findAll", query = "SELECT y FROM YvsProdDeclarationProduction y WHERE y.sessionOf.ordre.societe=:societe"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findById", query = "SELECT y FROM YvsProdDeclarationProduction y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findByOF", query = "SELECT y FROM YvsProdDeclarationProduction y JOIN FETCH y.ordre JOIN FETCH y.ordre.article JOIN FETCH y.sessionOf JOIN FETCH y.sessionOf.sessionProd WHERE y.sessionOf.ordre=:ordre"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findByOFStatut", query = "SELECT y FROM YvsProdDeclarationProduction y WHERE y.sessionOf.ordre=:ordre AND y.statut=:statut"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findByDateDeclaration", query = "SELECT y FROM YvsProdDeclarationProduction y WHERE y.sessionOf.sessionProd.dateSession = :dateDeclaration"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findByQuantite", query = "SELECT y FROM YvsProdDeclarationProduction y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findSumDeclare", query = "SELECT SUM(y.quantite) FROM YvsProdDeclarationProduction y WHERE y.sessionOf.ordre=:ordre AND y.statut=:statut"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findByStatut", query = "SELECT y FROM YvsProdDeclarationProduction y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findByDateSave", query = "SELECT y FROM YvsProdDeclarationProduction y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findByDateUpdate", query = "SELECT y FROM YvsProdDeclarationProduction y WHERE y.dateUpdate = :dateUpdate"),
    
    @NamedQuery(name = "YvsProdDeclarationProduction.countByArticle", query = "SELECT COUNT(y.id) FROM YvsProdDeclarationProduction y WHERE y.conditionnement.article = :article"),

    @NamedQuery(name = "YvsProdDeclarationProduction.findQteProdByArticle", query = "SELECT SUM(y.quantite)  FROM YvsProdDeclarationProduction y WHERE y.conditionnement = :conditionnement"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findCAByArticle", query = "SELECT SUM(y.quantite * y.coutProduction)  FROM YvsProdDeclarationProduction y WHERE y.conditionnement.article = :article"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findLastByArticle", query = "SELECT y  FROM YvsProdDeclarationProduction y WHERE y.conditionnement.article = :article ORDER BY y.sessionOf.sessionProd.dateSession DESC"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findNbreDocByArticle", query = "SELECT COUNT(DISTINCT(y.ordre))  FROM YvsProdDeclarationProduction y WHERE y.conditionnement.article = :article"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findNbreDocByConditionnement", query = "SELECT COUNT(DISTINCT(y.ordre))  FROM YvsProdDeclarationProduction y WHERE y.conditionnement = :conditionnement"),
    
    @NamedQuery(name = "YvsProdDeclarationProduction.findIdProdByUsers", query = "SELECT DISTINCT(y.ordre.id)  FROM YvsProdDeclarationProduction y WHERE y.sessionOf.sessionProd.producteur = :producteur"),
    @NamedQuery(name = "YvsProdDeclarationProduction.findIdProdByUsersDates", query = "SELECT DISTINCT(y.ordre.id)  FROM YvsProdDeclarationProduction y WHERE y.sessionOf.sessionProd.producteur = :producteur AND y.ordre.dateDebut BETWEEN :dateDebut AND :dateFin"),

    @NamedQuery(name = "YvsProdDeclarationProduction.findByOrdre", query = "SELECT y FROM YvsProdDeclarationProduction y WHERE y.ordre = :ordre")})
public class YvsProdDeclarationProduction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_declaration_production_id_seq", name = "yvs_prod_declaration_production_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_declaration_production_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
//    @Column(name = "date_declaration")
//    @Temporal(TemporalType.DATE)
//    private Date dateDeclaration;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "cout_production")
    private Double coutProduction;
    @Column(name = "statut")
    private Character statut;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "calcul_pr")
    private Boolean calculPr;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "ordre", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdOrdreFabrication ordre;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "session_of", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdSessionOf sessionOf;
    @JoinColumn(name = "doc_stock", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComContenuDocStock docStock;

    @OneToMany(mappedBy = "declaration")
    private List<YvsProdConditionnementDeclaration> conditionnements;
    @Transient
    private boolean new_;

    public YvsProdDeclarationProduction() {
        conditionnements = new ArrayList<>();
    }

    public YvsProdDeclarationProduction(Long id) {
        this();
        this.id = id;
    }

    public YvsProdDeclarationProduction(Long id, YvsUsersAgence author) {
        this(id);
        this.author = author;
    }

    public YvsProdDeclarationProduction(YvsProdDeclarationProduction y) {
        this(y.getId(), y);
    }

    public YvsProdDeclarationProduction(Long id, YvsProdDeclarationProduction y) {
        this.id = id;
        this.quantite = y.quantite;
        this.statut = y.statut;
        this.dateSave = y.dateSave;
        this.dateUpdate = y.dateUpdate;
        this.author = y.author;
        this.ordre = y.ordre;
        this.calculPr = y.calculPr;
        this.conditionnement = y.conditionnement;
        this.conditionnements = y.conditionnements;
        this.new_ = y.new_;
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

    public double getReste() {
        double reste = getQuantite();
        for (YvsProdConditionnementDeclaration c : conditionnements) {
            if (!c.getConditionnement().getStatut().equals(Constantes.STATUT_DOC_ANNULE)) {
                reste -= c.getConditionnement().getQuantite();
            }
        }
        return reste;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Character getStatut() {
        return statut != null ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
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

    public YvsProdOrdreFabrication getOrdre() {
        return ordre;
    }

    public void setOrdre(YvsProdOrdreFabrication ordre) {
        this.ordre = ordre;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public List<YvsProdConditionnementDeclaration> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsProdConditionnementDeclaration> conditionnements) {
        this.conditionnements = conditionnements;
    }

    public Double getCoutProduction() {
        return coutProduction != null ? coutProduction : 0;
    }

    public void setCoutProduction(Double coutProduction) {
        this.coutProduction = coutProduction;
    }

    public YvsComContenuDocStock getDocStock() {
        return docStock;
    }

    public void setDocStock(YvsComContenuDocStock docStock) {
        this.docStock = docStock;
    }

    public YvsProdSessionOf getSessionOf() {
        return sessionOf;
    }

    public void setSessionOf(YvsProdSessionOf sessionOf) {
        this.sessionOf = sessionOf;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        if (!(object instanceof YvsProdDeclarationProduction)) {
            return false;
        }
        YvsProdDeclarationProduction other = (YvsProdDeclarationProduction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdDeclarationProduction[ id=" + id + " ]";
    }

}
