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
import javax.persistence.Entity; import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.param.YvsBaseCentreValorisation;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_poste_charge")
@NamedQueries({
    @NamedQuery(name = "YvsProdPosteCharge.findAll", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.siteProduction.agence.societe = :societe"),
    @NamedQuery(name = "YvsProdPosteCharge.findById", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdPosteCharge.findByReference", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.reference = :reference AND y.siteProduction.agence.societe = :societe"),
    @NamedQuery(name = "YvsProdPosteCharge.findByDesignation", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsProdPosteCharge.findByDescription", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.description = :description"),
    @NamedQuery(name = "YvsProdPosteCharge.findByType", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.type = :type"),
    @NamedQuery(name = "YvsProdPosteCharge.findByTempsAttente", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.tempsAttente = :tempsAttente"),
    @NamedQuery(name = "YvsProdPosteCharge.findByTempsExecution", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.tempsExecution = :tempsExecution"),
    @NamedQuery(name = "YvsProdPosteCharge.findByTempsTransfert", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.tempsTransfert = :tempsTransfert"),
    @NamedQuery(name = "YvsProdPosteCharge.findByTempsReglage", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.tempsReglage = :tempsReglage"),
    @NamedQuery(name = "YvsProdPosteCharge.findByTempsRebus", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.tempsRebus = :tempsRebus"),
    @NamedQuery(name = "YvsProdPosteCharge.findByTauxRendement", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.tauxRendement = :tauxRendement"),
    @NamedQuery(name = "YvsProdPosteCharge.findByCapaciteQ", query = "SELECT y FROM YvsProdPosteCharge y WHERE y.capaciteQ = :capaciteQ")})
public class YvsProdPosteCharge implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_poste_charge_id_seq", name = "yvs_prod_poste_charge_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_poste_charge_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "type")
    private String type;
    @Column(name = "type_valeur")
    private Character typeValeur;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "temps_attente")
    private Double tempsAttente;
    @Column(name = "temps_execution")
    private Double tempsExecution;
    @Column(name = "temps_transfert")
    private Double tempsTransfert;
    @Column(name = "temps_reglage")
    private Double tempsReglage;
    @Column(name = "temps_rebus")
    private Double tempsRebus;
    @Column(name = "taux_rendement")
    private Double tauxRendement;
    @Column(name = "capacite_q")
    private Double capaciteQ;
    @JoinColumn(name = "centre_valorisation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCentreValorisation centreValorisation;
    @JoinColumn(name = "site_production", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdSiteProduction siteProduction;
    @JoinColumn(name = "poste_equivalent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdPosteCharge posteEquivalent;
    @JoinColumn(name = "centre_charge", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdCentreCharge centreCharge;
    @JoinColumn(name = "calendrier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsCalendrier calendrier;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "posteCharge")
    private List<YvsProdCentrePosteCharge> centres;
    @OneToMany(mappedBy = "posteCharge")
    private List<YvsProdCapacitePosteCharge> capacites;
    @OneToMany(mappedBy = "posteEquivalent")
    private List<YvsProdPosteCharge> equivalences;
    @OneToMany(mappedBy = "posteCharge")
    private List<YvsProdPosteOperation> phrases;
    @OneToOne(mappedBy = "ressource")
    private YvsProdPosteChargeEmploye employe;
    @OneToOne(mappedBy = "ressource")
    private YvsProdPosteChargeTiers tiers;
    @OneToOne(mappedBy = "ressource")
    private YvsProdPosteChargeMateriel materiel;
    @OneToMany(mappedBy = "ressourceProduction")
    private List<YvsProdEtatRessource> etats;
    @Transient
    private YvsProdEtatRessource etat;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private double capaciteH;
    @Transient
    private String typeName;

    public YvsProdPosteCharge() {
        etats = new ArrayList<>();
    }

    public YvsProdPosteCharge(Integer id) {
        this.id = id;
        etats = new ArrayList<>();
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

    public double getCapaciteH() {
        return capaciteH;
    }

    public void setCapaciteH(double capaciteH) {
        this.capaciteH = capaciteH;
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDesignation() {
        return designation;
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

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : "M" : "M";
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getTempsAttente() {
        return tempsAttente != null ? tempsAttente : 0.0;
    }

    public void setTempsAttente(Double tempsAttente) {
        this.tempsAttente = tempsAttente;
    }

    public Double getTempsExecution() {
        return tempsExecution != null ? tempsExecution : 0.0;
    }

    public void setTempsExecution(Double tempsExecution) {
        this.tempsExecution = tempsExecution;
    }

    public Double getTempsTransfert() {
        return tempsTransfert != null ? tempsTransfert : 0.0;
    }

    public void setTempsTransfert(Double tempsTransfert) {
        this.tempsTransfert = tempsTransfert;
    }

    public Double getTempsReglage() {
        return tempsReglage != null ? tempsReglage : 0.0;
    }

    public void setTempsReglage(Double tempsReglage) {
        this.tempsReglage = tempsReglage;
    }

    public Double getTempsRebus() {
        return tempsRebus != null ? tempsRebus : 0.0;
    }

    public void setTempsRebus(Double tempsRebus) {
        this.tempsRebus = tempsRebus;
    }

    public Double getTauxRendement() {
        return tauxRendement != null ? tauxRendement : 0.0;
    }

    public void setTauxRendement(Double tauxRendement) {
        this.tauxRendement = tauxRendement;
    }

    public Double getCapaciteQ() {
        return capaciteQ != null ? capaciteQ : 0.0;
    }

    public void setCapaciteQ(Double capaciteQ) {
        this.capaciteQ = capaciteQ;
    }

    public List<YvsProdCentrePosteCharge> getCentres() {
        return centres;
    }

    public void setCentres(List<YvsProdCentrePosteCharge> centres) {
        this.centres = centres;
    }

    public YvsProdSiteProduction getSiteProduction() {
        return siteProduction;
    }

    public void setSiteProduction(YvsProdSiteProduction siteProduction) {
        this.siteProduction = siteProduction;
    }

    public List<YvsProdPosteCharge> getEquivalences() {
        return equivalences;
    }

    public void setEquivalences(List<YvsProdPosteCharge> equivalences) {
        this.equivalences = equivalences;
    }

    public YvsProdPosteCharge getPosteEquivalent() {
        return posteEquivalent;
    }

    public void setPosteEquivalent(YvsProdPosteCharge posteEquivalent) {
        this.posteEquivalent = posteEquivalent;
    }

    public YvsProdCentreCharge getCentreCharge() {
        return centreCharge;
    }

    public void setCentreCharge(YvsProdCentreCharge centreCharge) {
        this.centreCharge = centreCharge;
    }

    public YvsCalendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(YvsCalendrier calendrier) {
        this.calendrier = calendrier;
    }

    public List<YvsProdPosteOperation> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<YvsProdPosteOperation> phrases) {
        this.phrases = phrases;
    }

    public List<YvsProdCapacitePosteCharge> getCapacites() {
        return capacites;
    }

    public void setCapacites(List<YvsProdCapacitePosteCharge> capacites) {
        this.capacites = capacites;
    }

    public YvsBaseCentreValorisation getCentreValorisation() {
        return centreValorisation;
    }

    public void setCentreValorisation(YvsBaseCentreValorisation centreValorisation) {
        this.centreValorisation = centreValorisation;
    }

    public String getTypeName() {
        switch (getType()) {
            case "M":
                typeName = "Machine";
                break;
            case "H":
                typeName = "Main-Oeuvre";
                break;
            case "S":
                typeName = "Sous-Traitant";
                break;
            case "MH":
                typeName = "Machine/Main-Oeuvre";
                break;
            case "MS":
                typeName = "Machine/Sous-Traitant";
                break;
            case "HS":
                typeName = "Main-Oeuvre/Sous-Traitant";
                break;
            default:
                typeName = "Machine";
                break;
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public YvsProdPosteChargeEmploye getEmploye() {
        return employe;
    }

    public void setEmploye(YvsProdPosteChargeEmploye employe) {
        this.employe = employe;
    }

    public YvsProdPosteChargeTiers getTiers() {
        return tiers;
    }

    public void setTiers(YvsProdPosteChargeTiers tiers) {
        this.tiers = tiers;
    }

    public YvsProdPosteChargeMateriel getMateriel() {
        return materiel;
    }

    public void setMateriel(YvsProdPosteChargeMateriel materiel) {
        this.materiel = materiel;
    }

    public List<YvsProdEtatRessource> getEtats() {
        return etats;
    }

    public void setEtats(List<YvsProdEtatRessource> etats) {
        this.etats = etats;
    }

    public YvsProdEtatRessource getEtat() {
        etat = new YvsProdEtatRessource();
        for (YvsProdEtatRessource e : etats) {
            if (e.getActif()) {
                etat = e;
                break;
            }
        }
        return etat;
    }

    public void setEtat(YvsProdEtatRessource etat) {
        this.etat = etat;
    }

    public Character getTypeValeur() {
        return typeValeur != null ? String.valueOf(typeValeur).trim().length() > 0 ? typeValeur : 'D' : 'D';
    }

    public void setTypeValeur(Character typeValeur) {
        this.typeValeur = typeValeur;
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
        if (!(object instanceof YvsProdPosteCharge)) {
            return false;
        }
        YvsProdPosteCharge other = (YvsProdPosteCharge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdPosteCharge[ id=" + id + " ]";
    }

}
