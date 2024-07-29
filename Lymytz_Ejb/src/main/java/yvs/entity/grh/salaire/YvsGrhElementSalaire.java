/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_element_salaire")
@NamedQueries({
    @NamedQuery(name = "YvsGrhElementSalaire.findAll", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.categorie.societe=:societe AND y.actif=true ORDER BY y.categorie.codeCategorie ASC, y.code ASC"),
    @NamedQuery(name = "YvsGrhElementSalaire.findAll_", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.categorie.societe=:societe AND y.actif=true ORDER BY y.code ASC"),
    @NamedQuery(name = "YvsGrhElementSalaire.findById", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhElementSalaire.findAllFormule", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.expressionRegle IS NOT NULL AND y.expressionRegle!='' AND y.categorie.societe=:societe"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByNom", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByCode0", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.code = :code AND y.categorie.societe=:societe"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByCode", query = "SELECT y FROM YvsGrhElementSalaire y WHERE upper(y.code) = upper(:code) AND y.categorie.societe=:societe AND y.actif=true"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByCodeAll", query = "SELECT y FROM YvsGrhElementSalaire y WHERE upper(y.code) = upper(:code) AND y.categorie.societe=:societe"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByCode1", query = "SELECT y FROM YvsGrhElementSalaire y WHERE (y.code LIKE :code OR y.nom LIKE :code) AND y.categorie.societe=:societe AND y.actif=true"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByTypeMontant", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.typeMontant = :typeMontant"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByMontant", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByExpressionRegle", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.expressionRegle = :expressionRegle"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByVisibleBulletin", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.visibleBulletin = true AND y.categorie.societe=:societe"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByDescriptionElement", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.descriptionElement = :descriptionElement"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByActif", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByRetenue", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.retenue = :retenue"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByPoucentagePatronale", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.poucentagePatronale = :poucentagePatronale"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByCategorie", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.categorie.codeCategorie=:categorie AND y.actif=true AND y.categorie.societe=:societe AND y.categorie.actif=true ORDER BY y.categorie.codeCategorie ASC, y.code ASC"),
    @NamedQuery(name = "YvsGrhElementSalaire.findRegleExclus", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.categorie.societe=:societe AND y.actif=true AND y.excludeInConge=true ORDER BY y.categorie.codeCategorie ASC, y.code ASC"),
    @NamedQuery(name = "YvsGrhElementSalaire.findRegleArrondi", query = "SELECT COUNT(y) FROM YvsGrhElementSalaire y WHERE y.categorie.societe=:societe AND y.regleArrondi=true"),
    @NamedQuery(name = "YvsGrhElementSalaire.findRegleArrondi_", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.categorie.societe=:societe AND y.regleArrondi=true"),
    @NamedQuery(name = "YvsGrhElementSalaire.countRegleAllocConge", query = "SELECT COUNT(y) FROM YvsGrhElementSalaire y WHERE y.categorie.societe=:societe AND y.allocationConge=true"),
    @NamedQuery(name = "YvsGrhElementSalaire.findByPoucentageSalarial", query = "SELECT y FROM YvsGrhElementSalaire y WHERE y.poucentageSalarial = :poucentageSalarial")})
public class YvsGrhElementSalaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_element_salaire_id_seq", name = "yvs_element_salaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_element_salaire_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "nom")
    private String nom;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @Column(name = "type_montant")
    private Integer typeMontant;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Size(max = 2147483647)
    @Column(name = "expression_regle")
    private String expressionRegle;
    @Column(name = "visible_bulletin")
    private Boolean visibleBulletin;
    @Size(max = 2147483647)
    @Column(name = "description_element")
    private String descriptionElement;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "retenue")
    private Boolean retenue;
    @Column(name = "poucentage_patronale")
    private Double poucentagePatronale;
    @Column(name = "poucentage_salarial")
    private Double poucentageSalarial;
    @Column(name = "num_sequence")
    private Integer numSequence;    //nécessaire pour les éléments visible sur le bulletin: sert à indiquer la position (ordre)
    @Column(name = "allocation_conge")
    private Boolean allocationConge;    //précise que la règle détermine l'allocation congé
    @Column(name = "exclude_in_conge")
    private Boolean excludeInConge;
    @Column(name = "regle_arrondi")
    private Boolean regleArrondi;
    @Column(name = "regle_conge")
    private Boolean regleConge;
    @Column(name = "visible_on_livre_paie")
    private Boolean visibleOnLivrePaie;
    @Column(name = "saisi_compte_tiers")
    private Boolean saisiCompteTiers;
    @JoinColumn(name = "quantite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhElementSalaire quantite; 
    @JoinColumn(name = "base_pourcentage", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhElementSalaire basePourcentage;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhCategorieElement categorie;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "compte_charge", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compteCharge;
    @JoinColumn(name = "compte_cotisation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compteCotisation;
    @JoinColumn(name = "rubrique", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhRubriqueBulletin rubrique;
    @Transient
    private boolean attribuer = false;
    @Transient
    private long idStructure = -1;

    public YvsGrhElementSalaire() {
    }

    public YvsGrhElementSalaire(Long id) {
        this.id = id;
    }

    public YvsGrhElementSalaire(String nom) {
        this.nom = nom;
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

    public Boolean getRegleConge() {
        return regleConge != null ? regleConge : false;
    }

    public void setRegleConge(Boolean regleConge) {
        this.regleConge = regleConge;
    }

    public Boolean getAllocationConge() {
        return this.allocationConge;
    }

    public void setAllocationConge(Boolean allocationConge) {
        this.allocationConge = allocationConge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getTypeMontant() {
        return typeMontant != null ? typeMontant : 0;
    }

    public void setTypeMontant(Integer typeMontant) {
        this.typeMontant = typeMontant;
    }

    public Double getMontant() {
        return (montant == null) ? 0 : montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getExpressionRegle() {
        return expressionRegle;
    }

    public void setExpressionRegle(String expressionRegle) {
        this.expressionRegle = expressionRegle;
    }

    public Boolean getVisibleBulletin() {
        return visibleBulletin != null ? visibleBulletin : false;
    }

    public void setVisibleBulletin(Boolean visibleBulletin) {
        this.visibleBulletin = visibleBulletin;
    }

    public String getDescriptionElement() {
        return descriptionElement;
    }

    public void setDescriptionElement(String descriptionElement) {
        this.descriptionElement = descriptionElement;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getRetenue() {
        return (retenue == null) ? false : retenue;
    }

    public void setRetenue(Boolean retenue) {
        this.retenue = retenue;
    }

    public Double getPoucentagePatronale() {
        return (poucentagePatronale == null) ? 0 : poucentagePatronale;
    }

    public void setPoucentagePatronale(Double poucentagePatronale) {
        this.poucentagePatronale = poucentagePatronale;
    }

    public Double getPoucentageSalarial() {
        return (poucentageSalarial == null) ? 0 : poucentageSalarial;
    }

    public void setPoucentageSalarial(Double poucentageSalarial) {
        this.poucentageSalarial = poucentageSalarial;
    }

    public YvsGrhElementSalaire getQuantite() {
        return quantite;
    }

    public void setQuantite(YvsGrhElementSalaire quantite) {
        this.quantite = quantite;
    }

    public YvsGrhElementSalaire getBasePourcentage() {
        return basePourcentage;
    }

    public void setBasePourcentage(YvsGrhElementSalaire basePourcentage) {
        this.basePourcentage = basePourcentage;
    }

    public YvsGrhCategorieElement getCategorie() {
        return categorie;
    }

    public void setCategorie(YvsGrhCategorieElement categorie) {
        this.categorie = categorie;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public void setNumSequence(Integer numSequence) {
        this.numSequence = numSequence;
    }

    public Integer getNumSequence() {
        return numSequence;
    }

    public void setExcludeInConge(Boolean excludeInConge) {
        this.excludeInConge = excludeInConge;
    }

    public Boolean getExcludeInConge() {
        return this.excludeInConge;
    }

    public Boolean getRegleArrondi() {
        return this.regleArrondi != null ? regleArrondi : false;
    }

    public void setRegleArrondi(Boolean regleArrondi) {
        this.regleArrondi = regleArrondi;
    }

    public void setVisibleOnLivrePaie(Boolean visibleOnLivrePaie) {
        this.visibleOnLivrePaie = visibleOnLivrePaie;
    }

    public Boolean getVisibleOnLivrePaie() {
        return visibleOnLivrePaie != null ? visibleOnLivrePaie : false;
    }

    public boolean isAttribuer() {
        return attribuer;
    }

    public void setAttribuer(boolean attribuer) {
        this.attribuer = attribuer;
    }

    public long getIdStructure() {
        return idStructure;
    }

    public void setIdStructure(long idStructure) {
        this.idStructure = idStructure;
    }

    public YvsBasePlanComptable getCompteCharge() {
        return compteCharge;
    }

    public void setCompteCharge(YvsBasePlanComptable compteCharge) {
        this.compteCharge = compteCharge;
    }

    public YvsBasePlanComptable getCompteCotisation() {
        return compteCotisation;
    }

    public void setCompteCotisation(YvsBasePlanComptable compteCotisation) {
        this.compteCotisation = compteCotisation;
    }

    public Boolean getSaisiCompteTiers() {
        return saisiCompteTiers != null ? saisiCompteTiers : false;
    }

    public void setSaisiCompteTiers(Boolean saisiCompteTiers) {
        this.saisiCompteTiers = saisiCompteTiers;
    }

    public YvsGrhRubriqueBulletin getRubrique() {
        return rubrique;
    }

    public void setRubrique(YvsGrhRubriqueBulletin rubrique) {
        this.rubrique = rubrique;
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
        if (!(object instanceof YvsGrhElementSalaire)) {
            return false;
        }
        YvsGrhElementSalaire other = (YvsGrhElementSalaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsElementSalaire[ id=" + id + " ]";
    }

}
