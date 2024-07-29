/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.credit;

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
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.mutuelle.base.YvsMutGrilleTauxTypeCredit;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_type_credit")
@NamedQueries({
    @NamedQuery(name = "YvsMutTypeCredit.findAll", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.mutuelle.societe = :societe AND y.typeAvance = false AND y.byFusion = false ORDER BY y.designation ASC"),
    @NamedQuery(name = "YvsMutTypeCredit.findById", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutTypeCredit.findByMutuelle", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.mutuelle = :mutuelle AND y.impayeDette = false AND y.typeAvance = :typeAvance ORDER BY y.natureMontant , y.montantMaximal DESC"),
    @NamedQuery(name = "YvsMutTypeCredit.findByMutuelleType", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.mutuelle = :mutuelle AND y.typeAvance = :typeAvance AND y.byFusion = false ORDER BY y.natureMontant , y.montantMaximal DESC"),
    @NamedQuery(name = "YvsMutTypeCredit.findByMutuelleImpaye", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.mutuelle = :mutuelle AND y.impayeDette = :impayeDette AND y.typeAvance = false"),
    @NamedQuery(name = "YvsMutTypeCredit.findByMutuelleAssistance", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.mutuelle = :mutuelle AND y.assistance = :assistance"),
    @NamedQuery(name = "YvsMutTypeCredit.findByDesignation", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsMutTypeCredit.findByMontantMaximal", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.montantMaximal = :montantMaximal"),
    @NamedQuery(name = "YvsMutTypeCredit.findByTauxMaximal", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.tauxMaximal = :tauxMaximal"),
    @NamedQuery(name = "YvsMutTypeCredit.findByReport", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.impayeDette = :report AND y.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutTypeCredit.findByFusion", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.byFusion = :byFusion AND y.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutTypeCredit.findByPeriodeMaximal", query = "SELECT y FROM YvsMutTypeCredit y WHERE y.periodeMaximal = :periodeMaximal")})
public class YvsMutTypeCredit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_type_credit_id_seq", name = "yvs_mut_type_credit_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_type_credit_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "coefficient_remboursement")
    private Double coefficientRemboursement; // indique le coefficient max du salaire à prélever;
    @Column(name = "montant_maximal")
    private Double montantMaximal;
    @Column(name = "taux_maximal")
    private Double tauxMaximal;
    @Column(name = "periode_maximal")
    private Double periodeMaximal;
    @Column(name = "impaye_dette")
    private Boolean impayeDette;
    @Column(name = "type_avance")
    private Boolean typeAvance;
    @Column(name = "assistance")
    private Boolean assistance;
    @Column(name = "jour_debut_avance")
    private Integer jourDebutAvance;
    @Column(name = "jour_fin_avance")
    private Integer jourFinAvance;
    @Column(name = "nbre_avalise")
    private Double nbreAvalise;
    @Column(name = "periodicite")
    private Integer periodicite;
    @Column(name = "type_mensualite")
    private Character typeMensualite;
    @Column(name = "formule_interet")
    private Character formuleInteret;
    @Column(name = "model_remboursement")
    private Character modelRemboursement;
    @Column(name = "reechellonage_possible")
    private Boolean reechellonagePossible;
    @Column(name = "fusion_possible")
    private Boolean fusionPossible;
    @Column(name = "anticipation_possible")
    private Boolean anticipationPossible;
    @Column(name = "penalite_anticipation")
    private Boolean penaliteAnticipation;
    @Column(name = "taux_penalite_anticipation")
    private Double tauxPenaliteAnticipation;
    @Column(name = "base_penalite_anticipation")
    private Character basePenaliteAnticipation;
    @Column(name = "nature_penalite_anticipation")
    private Character naturePenaliteAnticipation;
    @Column(name = "suspension_possible")
    private Boolean suspensionPossible;
    @Column(name = "by_fusion")
    private Boolean byFusion;
    @Column(name = "penalite_suspension")
    private Boolean penaliteSuspension;
    @Column(name = "taux_penalite_suspension")
    private Double tauxPenaliteSuspension;
    @Column(name = "base_penalite_suspension")
    private Character basePenaliteSuspension;
    @Column(name = "nature_penalite_suspension")
    private Character naturePenaliteSuspension;
    @Column(name = "penalite_retard")
    private Boolean penaliteRetard;
    @Column(name = "taux_penalite_retard")
    private Double tauxPenaliteRetard;
    @Column(name = "base_penalite_retard")
    private Character basePenaliteRetard;
    @Column(name = "nature_penalite_retard")
    private Character naturePenaliteRetard;
    @Size(max = 2147483647)
    @Column(name = "nature_montant")
    private String natureMontant;
    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "type")
    private List<YvsMutCredit> credits;
    @OneToMany(mappedBy = "typeCredit")
    private List<YvsMutGrilleTauxTypeCredit> grilles;
    @OneToMany(mappedBy = "credit")
    private List<YvsMutFraisTypeCredit> frais;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private String suffixeMontant;
    @Transient
    private long totalPris;
    @Transient
    private long totalEncours;
    @Transient
    private long totalPaye;
    @Transient
    private double montantTotal;

    public YvsMutTypeCredit() {
        frais = new ArrayList<>();
        grilles = new ArrayList<>();
    }

    public YvsMutTypeCredit(Long id) {
        this();
        this.id = id;
    }

    public YvsMutTypeCredit(Long id, String designation) {
        this(id);
        this.designation = designation;
    }

    public YvsMutTypeCredit(Long id, String designation, String natureMontant) {
        this(id, designation);
        this.natureMontant = natureMontant;
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

    public String getCode() {
        return code != null ? code.trim().length() > 0 ? code : "CR" : "CR";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getByFusion() {
        return byFusion != null ? byFusion : false;
    }

    public void setByFusion(Boolean byFusion) {
        this.byFusion = byFusion;
    }

    public String getSuffixeMontant() {
        suffixeMontant = getNatureMontant().equals("Pourcentage") ? "% Salaire" : " Fcfa";
        return suffixeMontant != null ? suffixeMontant : "";
    }

    public void setSuffixeMontant(String suffixeMontant) {
        this.suffixeMontant = suffixeMontant;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Boolean getAssistance() {
        return assistance != null ? assistance : false;
    }

    public void setAssistance(Boolean assistance) {
        this.assistance = assistance;
    }

    public Boolean getTypeAvance() {
        return typeAvance != null ? typeAvance : false;
    }

    public void setTypeAvance(Boolean typeAvance) {
        this.typeAvance = typeAvance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getImpayeDette() {
        return impayeDette != null ? impayeDette : false;
    }

    public void setImpayeDette(Boolean impayeDette) {
        this.impayeDette = impayeDette;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getMontantMaximal() {
        return montantMaximal != null ? montantMaximal : 0;
    }

    public void setMontantMaximal(Double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public Double getTauxMaximal() {
        return tauxMaximal != null ? tauxMaximal : 0;
    }

    public void setTauxMaximal(Double tauxMaximal) {
        this.tauxMaximal = tauxMaximal;
    }

    public Double getPeriodeMaximal() {
        return periodeMaximal != null ? periodeMaximal : 0;
    }

    public void setPeriodeMaximal(Double periodeMaximal) {
        this.periodeMaximal = periodeMaximal;
    }

    public YvsMutMutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(YvsMutMutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public List<YvsMutCredit> getYvsMutCreditList() {
        return credits;
    }

    public void setYvsMutCreditList(List<YvsMutCredit> yvsMutCreditList) {
        this.credits = yvsMutCreditList;
    }

    public List<YvsMutGrilleTauxTypeCredit> getGrilles() {
        return grilles;
    }

    public void setGrilles(List<YvsMutGrilleTauxTypeCredit> grilles) {
        this.grilles = grilles;
    }

    public Integer getJourDebutAvance() {
        return jourDebutAvance != null ? jourDebutAvance : 1;
    }

    public void setJourDebutAvance(Integer jourDebutAvance) {
        this.jourDebutAvance = jourDebutAvance;
    }

    public Integer getJourFinAvance() {
        return jourFinAvance != null ? jourFinAvance : 0;
    }

    public void setJourFinAvance(Integer jourFinAvance) {
        this.jourFinAvance = jourFinAvance;
    }

    public String getNatureMontant() {
        return natureMontant != null ? natureMontant : "";
    }

    public void setNatureMontant(String natureMontant) {
        this.natureMontant = natureMontant;
    }

    public Double getNbreAvalise() {
        return nbreAvalise != null ? nbreAvalise : 0;
    }

    public void setNbreAvalise(Double nbreAvalise) {
        this.nbreAvalise = nbreAvalise;
    }

    public Integer getPeriodicite() {
        return periodicite != null ? periodicite : 1;
    }

    public void setPeriodicite(Integer periodicite) {
        this.periodicite = periodicite;
    }

    public Character getTypeMensualite() {
        return typeMensualite != null ? String.valueOf(typeMensualite).trim().length() > 0 ? typeMensualite : 'M' : 'M';
    }

    public void setTypeMensualite(Character typeMensualite) {
        this.typeMensualite = typeMensualite;
    }

    public Character getFormuleInteret() {
        return formuleInteret != null ? String.valueOf(formuleInteret).trim().length() > 0 ? formuleInteret : 'S' : 'S';
    }

    public void setFormuleInteret(Character formuleInteret) {
        this.formuleInteret = formuleInteret;
    }

    public Character getModelRemboursement() {
        return modelRemboursement != null ? String.valueOf(modelRemboursement).trim().length() > 0 ? modelRemboursement : 'M' : 'M';
    }

    public void setModelRemboursement(Character modelRemboursement) {
        this.modelRemboursement = modelRemboursement;
    }

    public Boolean getReechellonagePossible() {
        return reechellonagePossible != null ? reechellonagePossible : false;
    }

    public void setReechellonagePossible(Boolean reechellonagePossible) {
        this.reechellonagePossible = reechellonagePossible;
    }

    public Boolean getFusionPossible() {
        return fusionPossible != null ? fusionPossible : false;
    }

    public void setFusionPossible(Boolean fusionPossible) {
        this.fusionPossible = fusionPossible;
    }

    public Boolean getAnticipationPossible() {
        return anticipationPossible != null ? anticipationPossible : false;
    }

    public void setAnticipationPossible(Boolean anticipationPossible) {
        this.anticipationPossible = anticipationPossible;
    }

    public Boolean getPenaliteAnticipation() {
        return penaliteAnticipation != null ? penaliteAnticipation : false;
    }

    public void setPenaliteAnticipation(Boolean penaliteAnticipation) {
        this.penaliteAnticipation = penaliteAnticipation;
    }

    public Double getTauxPenaliteAnticipation() {
        return tauxPenaliteAnticipation != null ? tauxPenaliteAnticipation : 0;
    }

    public void setTauxPenaliteAnticipation(Double tauxPenaliteAnticipation) {
        this.tauxPenaliteAnticipation = tauxPenaliteAnticipation;
    }

    public Character getBasePenaliteAnticipation() {
        return basePenaliteAnticipation != null ? String.valueOf(basePenaliteAnticipation).trim().length() > 0 ? basePenaliteAnticipation : 'M' : 'M';
    }

    public void setBasePenaliteAnticipation(Character basePenaliteAnticipation) {
        this.basePenaliteAnticipation = basePenaliteAnticipation;
    }

    public Boolean getSuspensionPossible() {
        return suspensionPossible != null ? suspensionPossible : false;
    }

    public void setSuspensionPossible(Boolean suspensionPossible) {
        this.suspensionPossible = suspensionPossible;
    }

    public Boolean getPenaliteSuspension() {
        return penaliteSuspension != null ? penaliteSuspension : false;
    }

    public void setPenaliteSuspension(Boolean penaliteSuspension) {
        this.penaliteSuspension = penaliteSuspension;
    }

    public Double getTauxPenaliteSuspension() {
        return tauxPenaliteSuspension != null ? tauxPenaliteSuspension : 0;
    }

    public void setTauxPenaliteSuspension(Double tauxPenaliteSuspension) {
        this.tauxPenaliteSuspension = tauxPenaliteSuspension;
    }

    public Character getBasePenaliteSuspension() {
        return basePenaliteSuspension != null ? String.valueOf(basePenaliteSuspension).trim().length() > 0 ? basePenaliteSuspension : 'M' : 'M';
    }

    public void setBasePenaliteSuspension(Character basePenaliteSuspension) {
        this.basePenaliteSuspension = basePenaliteSuspension;
    }

    public Boolean getPenaliteRetard() {
        return penaliteRetard != null ? penaliteRetard : false;
    }

    public void setPenaliteRetard(Boolean penaliteRetard) {
        this.penaliteRetard = penaliteRetard;
    }

    public Double getTauxPenaliteRetard() {
        return tauxPenaliteRetard != null ? tauxPenaliteRetard : 0;
    }

    public void setTauxPenaliteRetard(Double tauxPenaliteRetard) {
        this.tauxPenaliteRetard = tauxPenaliteRetard;
    }

    public Character getBasePenaliteRetard() {
        return basePenaliteRetard != null ? String.valueOf(basePenaliteRetard).trim().length() > 0 ? basePenaliteRetard : 'M' : 'M';
    }

    public void setBasePenaliteRetard(Character basePenaliteRetard) {
        this.basePenaliteRetard = basePenaliteRetard;
    }

    public Character getNaturePenaliteAnticipation() {
        return naturePenaliteAnticipation != null ? String.valueOf(naturePenaliteAnticipation).trim().length() > 0 ? naturePenaliteAnticipation : 'T' : 'T';
    }

    public void setNaturePenaliteAnticipation(Character naturePenaliteAnticipation) {
        this.naturePenaliteAnticipation = naturePenaliteAnticipation;
    }

    public Character getNaturePenaliteSuspension() {
        return naturePenaliteSuspension != null ? String.valueOf(naturePenaliteSuspension).trim().length() > 0 ? naturePenaliteSuspension : 'T' : 'T';
    }

    public void setNaturePenaliteSuspension(Character naturePenaliteSuspension) {
        this.naturePenaliteSuspension = naturePenaliteSuspension;
    }

    public Character getNaturePenaliteRetard() {
        return naturePenaliteRetard != null ? String.valueOf(naturePenaliteRetard).trim().length() > 0 ? naturePenaliteRetard : 'T' : 'T';
    }

    public void setNaturePenaliteRetard(Character naturePenaliteRetard) {
        this.naturePenaliteRetard = naturePenaliteRetard;
    }

    public List<YvsMutCredit> getCredits() {
        return credits;
    }

    public void setCredits(List<YvsMutCredit> credits) {
        this.credits = credits;
    }

    public long getTotalPris() {
        return totalPris;
    }

    public void setTotalPris(long totalPris) {
        this.totalPris = totalPris;
    }

    public long getTotalEncours() {
        return totalEncours;
    }

    public void setTotalEncours(long totalEncours) {
        this.totalEncours = totalEncours;
    }

    public long getTotalPaye() {
        return totalPaye;
    }

    public void setTotalPaye(long totalPaye) {
        this.totalPaye = totalPaye;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsMutFraisTypeCredit> getFrais() {
        return frais;
    }

    public void setFrais(List<YvsMutFraisTypeCredit> frais) {
        this.frais = frais;
    }

    public double getTotalFrais() {
        double d = 0;
        if (frais != null) {
            for (YvsMutFraisTypeCredit f : frais) {
                d += f.getMontant();
            }
        }
        return d;
    }

    public Double getCoefficientRemboursement() {
        return coefficientRemboursement!=null?coefficientRemboursement:0;
    }

    public void setCoefficientRemboursement(Double coefficientRemboursement) {
        this.coefficientRemboursement = coefficientRemboursement;
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
        if (!(object instanceof YvsMutTypeCredit)) {
            return false;
        }
        YvsMutTypeCredit other = (YvsMutTypeCredit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.credit.YvsMutTypeCredit[ id=" + id + " ]";
    }

}
