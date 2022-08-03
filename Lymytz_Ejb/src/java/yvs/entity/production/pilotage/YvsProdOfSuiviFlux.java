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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_of_suivi_flux")
@NamedQueries({
    @NamedQuery(name = "YvsProdOfSuiviFlux.findAll", query = "SELECT y FROM YvsProdOfSuiviFlux y"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findById", query = "SELECT y FROM YvsProdOfSuiviFlux y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.countByComposant", query = "SELECT COUNT(y) FROM YvsProdOfSuiviFlux y WHERE y.composant=:composant"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findByComposant", query = "SELECT y FROM YvsProdOfSuiviFlux y WHERE y.composant=:composant"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findOne", query = "SELECT y FROM YvsProdOfSuiviFlux y WHERE y.idOperation=:operation AND y.composant=:composant"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findByOf", query = "SELECT y FROM YvsProdOfSuiviFlux y WHERE y.composant.operation.ordreFabrication=:ordre"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findByComposantOF", query = "SELECT y FROM YvsProdOfSuiviFlux y WHERE y.composant.composant=:composant ORDER BY y.idOperation.sessionOf.sessionProd.dateSession DESC"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findByOfProducteur", query = "SELECT y FROM YvsProdOfSuiviFlux y WHERE y.composant.operation.ordreFabrication=:ordre AND y.idOperation.sessionOf.sessionProd.producteur=:producteur"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findByOneOp", query = "SELECT y FROM YvsProdOfSuiviFlux y WHERE y.idOperation=:operation"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findByOp", query = "SELECT y.quantite FROM YvsProdOfSuiviFlux y WHERE y.idOperation.operationOf=:operation"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findByQuantite", query = "SELECT y FROM YvsProdOfSuiviFlux y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findByDateSave", query = "SELECT y FROM YvsProdOfSuiviFlux y WHERE y.dateSave = :dateSave"),
    
    @NamedQuery(name = "YvsProdOfSuiviFlux.countByArticle", query = "SELECT COUNT(y.id) FROM YvsProdOfSuiviFlux y WHERE y.composant.composant.article = :article"),

    @NamedQuery(name = "YvsProdOfSuiviFlux.findEquipes", query = "SELECT DISTINCT y.idOperation.sessionOf.sessionProd.equipe FROM YvsProdOfSuiviFlux y WHERE y.composant.operation.ordreFabrication=:ordre"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findEquipesProducteur", query = "SELECT DISTINCT y.idOperation.sessionOf.sessionProd.equipe FROM YvsProdOfSuiviFlux y WHERE y.composant.operation.ordreFabrication=:ordre AND y.idOperation.sessionOf.sessionProd.producteur=:producteur"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findDates", query = "SELECT DISTINCT y.idOperation.sessionOf.sessionProd.dateSession, y.idOperation.sessionOf.sessionProd.tranche FROM YvsProdOfSuiviFlux y WHERE y.composant.operation.ordreFabrication=:ordre ORDER BY y.idOperation.sessionOf.sessionProd.dateSession ASC, y.idOperation.sessionOf.sessionProd.tranche.heureDebut"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findDatesProducteur", query = "SELECT DISTINCT y.idOperation.sessionOf.sessionProd.dateSession, y.idOperation.sessionOf.sessionProd.tranche FROM YvsProdOfSuiviFlux y WHERE y.composant.operation.ordreFabrication=:ordre AND y.idOperation.sessionOf.sessionProd.producteur=:producteur ORDER BY y.idOperation.sessionOf.sessionProd.dateSession ASC, y.idOperation.sessionOf.sessionProd.tranche.heureDebut"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findFluxByOpAndSens", query = "SELECT y FROM YvsProdOfSuiviFlux y WHERE y.composant.operation=:operation AND y.composant.sens=:sens"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findFluxComposant", query = "SELECT SUM(y.quantite) FROM YvsProdOfSuiviFlux y WHERE y.composant=:composant"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findFluxComposantBySens", query = "SELECT SUM(y.quantite) FROM YvsProdOfSuiviFlux y WHERE y.composant.composant=:composant AND y.composant.sens=:sens"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findFluxOneComposant", query = "SELECT SUM(y.quantite) FROM YvsProdOfSuiviFlux y WHERE y.composant=:composant"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findQteCycleOp", query = "SELECT SUM(y.quantite) FROM YvsProdOfSuiviFlux y WHERE y.idOperation=:operation AND y.composant=:composant"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findGroupFluxByOpAndSens", query = "SELECT SUM(y.quantite), y.composant FROM YvsProdOfSuiviFlux y WHERE y.composant.operation=:operation AND y.composant.sens=:sens GROUP BY y.composant"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findCoutCOmposant", query = "SELECT SUM(y.quantite*y.cout)  FROM YvsProdOfSuiviFlux y WHERE y.composant.operation.ordreFabrication=:ordre AND y.composant.sens=:sens"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findCoutOneComposant", query = "SELECT SUM(y.quantite*y.cout)  FROM YvsProdOfSuiviFlux y WHERE y.composant.composant=:composant"),
    @NamedQuery(name = "YvsProdOfSuiviFlux.findByDateUpdate", query = "SELECT y FROM YvsProdOfSuiviFlux y WHERE y.dateUpdate = :dateUpdate")})
public class YvsProdOfSuiviFlux implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_of_suivi_flux_id_seq", name = "yvs_prod_of_suivi_flux_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_of_suivi_flux_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "quantite_perdue")
    private Double quantitePerdue;
    @Column(name = "cout")
    private Double cout;
    @Column(name = "calcul_pr")
    private Boolean calculPr;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "composant", referencedColumnName = "id")
    @ManyToOne
    private YvsProdFluxComposant composant;
    @JoinColumn(name = "id_operation", referencedColumnName = "id")
    @ManyToOne
    private YvsProdSuiviOperations idOperation;

    @Transient
    private Double quantiteSave;
    @Transient
    private Double stock;

    public YvsProdOfSuiviFlux() {
    }

    public YvsProdOfSuiviFlux(Long id) {
        this.id = id;
    }
    public YvsProdOfSuiviFlux(Long id, YvsUsersAgence author) {
        this(id);
        this.author = author;
    }

    public Long getId() {
        return id;
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

    public Double getQuantite() {
        return quantite != null ? quantite : 0d;
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

    public YvsProdFluxComposant getComposant() {
        return composant;
    }

    public void setComposant(YvsProdFluxComposant composant) {
        this.composant = composant;
    }
//
//    public YvsProdEquipeProduction getEquipe() {
//        return equipe;
//    }
//
//    public void setEquipe(YvsProdEquipeProduction equipe) {
//        this.equipe = equipe;
//    }
//
//    public YvsGrhTrancheHoraire getTranche() {
//        return tranche;
//    }
//
//    public void setTranche(YvsGrhTrancheHoraire tranche) {
//        this.tranche = tranche;
//    }
//
//    public Date getDateFlux() {
//        return dateFlux;
//    }
//
//    public void setDateFlux(Date dateFlux) {
//        this.dateFlux = dateFlux;
//    }

    public Double getQuantiteSave() {
        return quantiteSave != null ? quantiteSave : 0;
    }

    public void setQuantiteSave(Double quantiteSave) {
        this.quantiteSave = quantiteSave;
    }

    public Double getStock() {
        return stock != null ? stock : 0;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public Double getCout() {
        return cout != null ? cout : 0d;
    }

    public void setCout(Double cout) {
        this.cout = cout;
    }

    public Double getQuantitePerdue() {
        return quantitePerdue != null ? quantitePerdue : 0;
    }

    public void setQuantitePerdue(Double quantitePerdue) {
        this.quantitePerdue = quantitePerdue;
    }

    public YvsProdSuiviOperations getIdOperation() {
        return idOperation;
    }

    public void setIdOperation(YvsProdSuiviOperations idOperation) {
        this.idOperation = idOperation;
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
        if (!(object instanceof YvsProdOfSuiviFlux)) {
            return false;
        }
        YvsProdOfSuiviFlux other = (YvsProdOfSuiviFlux) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdOfSuiviFlux[ id=" + id + " ]";
    }

}
