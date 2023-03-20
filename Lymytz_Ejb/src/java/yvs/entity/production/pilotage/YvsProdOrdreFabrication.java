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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.param.YvsSocietes;
import yvs.entity.production.base.YvsProdGammeArticle;
import yvs.entity.production.base.YvsProdNomenclature;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_ordre_fabrication")
@NamedQueries({
    @NamedQuery(name = "YvsProdOrdreFabrication.findAllC", query = "SELECT COUNT(y) FROM YvsProdOrdreFabrication y WHERE y.siteProduction.agence.societe=:societe"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findAll", query = "SELECT DISTINCT y FROM YvsProdOrdreFabrication y INNER JOIN y.nomenclature N INNER JOIN y.gamme G  WHERE y.siteProduction.agence.societe=:societe"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findById", query = "SELECT y FROM YvsProdOrdreFabrication y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findByReference", query = "SELECT y FROM YvsProdOrdreFabrication y WHERE y.siteProduction.agence.societe= :societe AND y.codeRef LIKE :codeRef ORDER BY y.codeRef DESC"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findReferenceByReference", query = "SELECT y.codeRef FROM YvsProdOrdreFabrication y WHERE y.siteProduction.agence.societe= :societe AND y.codeRef>:codeRef AND y.codeRef<:codeRef1 ORDER BY y.codeRef DESC"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findByNumeroIdentification", query = "SELECT y FROM YvsProdOrdreFabrication y WHERE y.numeroIdentification = :numeroIdentification"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findByTypeOrdre", query = "SELECT y FROM YvsProdOrdreFabrication y WHERE y.typeOrdre = :typeOrdre"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findByPriorite", query = "SELECT y FROM YvsProdOrdreFabrication y WHERE y.priorite = :priorite"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findByDateDebut", query = "SELECT y FROM YvsProdOrdreFabrication y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findByDate", query = "SELECT y FROM YvsProdOrdreFabrication y WHERE (y.dateDebut BETWEEN :date1 AND :date2) AND y.siteProduction.agence.societe=:societe"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findByDateC", query = "SELECT COUNT(y) FROM YvsProdOrdreFabrication y WHERE (y.dateDebut BETWEEN :date1 AND :date2) AND y.siteProduction.agence.societe=:societe"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findIdByAuthorDates", query = "SELECT y.id FROM YvsProdOrdreFabrication y WHERE (y.dateDebut BETWEEN :dateDebut AND :dateFin) AND y.author =:author"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findByStatutOrdre", query = "SELECT y FROM YvsProdOrdreFabrication y WHERE y.statutOrdre = :statutOrdre AND y.siteProduction.agence.societe=:societe"),
    @NamedQuery(name = "YvsProdOrdreFabrication.findByStatutOrdreC", query = "SELECT COUNT(y) FROM YvsProdOrdreFabrication y WHERE y.statutOrdre = :statutOrdre AND y.siteProduction.agence.societe=:societe")})
public class YvsProdOrdreFabrication implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_ordre_fabrication_id_seq", name = "yvs_prod_ordre_fabrication_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_ordre_fabrication_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code_ref")
    private String codeRef;
    @Size(max = 2147483647)
    @Column(name = "numero_identification")
    private String numeroIdentification;
    @Size(max = 2147483647)
    @Column(name = "type_ordre")
    private String typeOrdre;
    @Column(name = "priorite")
    private Integer priorite;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "quantite_fabrique")
    private Double quantite;
    @Size(max = 2147483647)
    @Column(name = "statut_ordre")
    private String statutOrdre;
    @Column(name = "statut_declaration")
    private String statutDeclaration;
    @Column(name = "heure_lancement")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date heureLancement;
    @Column(name = "rebut")
    private Double rebut;
    @Column(name = "debut_validite")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date debutValidite;
    @Column(name = "fin_validite")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date finValidite;
//    @Column(name = "always_valid")
//    private Boolean alwaysValid;
    @Column(name = "suivi_stock_by_operation")
    private Boolean suiviStockByOperation;
    @Column(name = "suivi_operation")
    private Boolean suiviOperation;
    @Column(name = "suspendu")
    private Boolean suspendu;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "type_of")
    private String typeOf;  //REPETITIF, SEQUENTIEL, CONTINUE
    @Column(name = "taux_evolution")
    private Double tauxEvolution;
    @Column(name = "cout_of")
    private Double coutOf;

    @JoinColumn(name = "site_production", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdSiteProduction siteProduction;
    @JoinColumn(name = "depot_mp", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depotMp;
    @JoinColumn(name = "depot_pf", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depotPf;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "nomenclature", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdNomenclature nomenclature;
    @JoinColumn(name = "gamme", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdGammeArticle gamme;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    @Transient
    private List<YvsProdDeclarationProduction> declarations;
    @Transient
    private List<YvsProdOperationsOF> operations;
    @Transient
    private List<YvsProdComposantOF> composants;
    @Transient
    private double quantiteDeclare;
    @Transient
    private double resteADeclarer;

    public YvsProdOrdreFabrication() {
        operations = new ArrayList<>();
        composants = new ArrayList<>();
        declarations = new ArrayList<>();
    }

    public YvsProdOrdreFabrication(Long id) {
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

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getCodeRef() {
        return codeRef != null ? codeRef : "";
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public String getNumeroIdentification() {
        return numeroIdentification != null ? numeroIdentification : "";
    }

    public void setNumeroIdentification(String numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }

    public String getTypeOrdre() {
        return typeOrdre != null ? typeOrdre : "";
    }

    public void setTypeOrdre(String typeOrdre) {
        this.typeOrdre = typeOrdre;
    }

    public Integer getPriorite() {
        return priorite != null ? priorite : 0;
    }

    public void setPriorite(Integer priorite) {
        this.priorite = priorite;
    }

    public Date getDateDebut() {
        return dateDebut != null ? dateDebut : new Date();
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public String getStatutOrdre() {
        return statutOrdre != null ? statutOrdre.trim().length() > 0 ? statutOrdre : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatutOrdre(String statutOrdre) {
        this.statutOrdre = statutOrdre;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsProdGammeArticle getGamme() {
        return gamme;
    }

    public void setGamme(YvsProdGammeArticle gamme) {
        this.gamme = gamme;
    }

    public YvsProdNomenclature getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(YvsProdNomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Date getHeureLancement() {
        return heureLancement != null ? heureLancement : new Date();
    }

    public void setHeureLancement(Date heureLancement) {
        this.heureLancement = heureLancement;
    }

    public Double getRebut() {
        return rebut != null ? rebut : 0;
    }

    public void setRebut(Double rebut) {
        this.rebut = rebut;
    }

    public Date getDebutValidite() {
        return debutValidite != null ? debutValidite : new Date();
    }

    public void setDebutValidite(Date debutValidite) {
        this.debutValidite = debutValidite;
    }

    public Date getFinValidite() {
        return finValidite != null ? finValidite : getDebutValidite();
    }

    public void setFinValidite(Date finValidite) {
        this.finValidite = finValidite;
    }
//
//    public Boolean getAlwaysValid() {
//        return alwaysValid != null ? alwaysValid : false;
//    }
//
//    public void setAlwaysValid(Boolean alwaysValid) {
//        this.alwaysValid = alwaysValid;
//    }

    public double getQuantiteDeclare() {
        quantiteDeclare = 0;
        for (YvsProdDeclarationProduction d : declarations) {
            if (d.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                quantiteDeclare += d.getQuantite();
            }
        }
        return quantiteDeclare;
    }

    public void setQuantiteDeclare(double quantiteDeclare) {
        this.quantiteDeclare = quantiteDeclare;
    }

    public double getResteADeclarer() {
        resteADeclarer = getQuantite() - getQuantiteDeclare();
        return resteADeclarer > 0 ? resteADeclarer : 0;
    }

    public void setResteADeclarer(double resteADeclarer) {
        this.resteADeclarer = resteADeclarer;
    }

    public YvsBaseDepots getDepotMp() {
        return depotMp;
    }

    public void setDepotMp(YvsBaseDepots depotMp) {
        this.depotMp = depotMp;
    }

    public YvsBaseDepots getDepotPf() {
        return depotPf;
    }

    public void setDepotPf(YvsBaseDepots depotPf) {
        this.depotPf = depotPf;
    }

    public Boolean getSuiviStockByOperation() {
        return suiviStockByOperation != null ? suiviStockByOperation : false;
    }

    public void setSuiviStockByOperation(Boolean suiviStockByOperation) {
        this.suiviStockByOperation = suiviStockByOperation;
    }

    public Boolean getSuiviOperation() {
        return suiviOperation != null ? suiviOperation : false;
    }

    public void setSuiviOperation(Boolean suiviOperation) {
        this.suiviOperation = suiviOperation;
    }

    public List<YvsProdOperationsOF> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsProdOperationsOF> operations) {
        this.operations = operations;
    }

    public List<YvsProdComposantOF> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsProdComposantOF> composants) {
        this.composants = composants;
    }

    public String getStatutDeclaration() {
        return statutDeclaration == null ? Constantes.ETAT_ATTENTE : statutDeclaration;
    }

    public void setStatutDeclaration(String statutDeclaration) {
        this.statutDeclaration = statutDeclaration;
    }

    public String getTypeOf() {
        return typeOf;
    }

    public void setTypeOf(String typeOf) {
        this.typeOf = typeOf;
    }

    public Double getTauxEvolution() {
        return tauxEvolution != null ? tauxEvolution : 0d;
    }

    public void setTauxEvolution(Double tauxEvolution) {
        this.tauxEvolution = tauxEvolution;
    }

    public Double getCoutOf() {
        return coutOf != null ? coutOf : 0;
    }

    public void setCoutOf(Double coutOf) {
        this.coutOf = coutOf;
    }

    public Boolean getSuspendu() {
        return suspendu != null ? suspendu : false;
    }

    public void setSuspendu(Boolean suspendu) {
        this.suspendu = suspendu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public YvsProdSiteProduction getSiteProduction() {
        return siteProduction;
    }

    public void setSiteProduction(YvsProdSiteProduction siteProduction) {
        this.siteProduction = siteProduction;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsProdOrdreFabrication)) {
            return false;
        }
        YvsProdOrdreFabrication other = (YvsProdOrdreFabrication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdOrdreFabrication[ id=" + id + " ]";
    }

    public boolean canDelete() {
        return !(getStatutOrdre().equals(Constantes.ETAT_VALIDE) || getStatutOrdre().equals(Constantes.ETAT_ENCOURS) || getStatutOrdre().equals(Constantes.ETAT_CLOTURE) || getStatutOrdre().equals(Constantes.ETAT_TERMINE));
    }

    public boolean canEditable() {
        return (getStatutOrdre().equals(Constantes.ETAT_VALIDE) || getStatutOrdre().equals(Constantes.ETAT_ENCOURS) || getStatutOrdre().equals(Constantes.ETAT_CLOTURE) || getStatutOrdre().equals(Constantes.ETAT_TERMINE) || getStatutOrdre().equals(Constantes.ETAT_SUSPENDU));
    }

    public List<YvsProdDeclarationProduction> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<YvsProdDeclarationProduction> declarations) {
        this.declarations = declarations;
    }

}
