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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.echellonage.YvsMutEchellonage;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_credit")
@NamedQueries({
    @NamedQuery(name = "YvsMutCredit.findAll", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutCredit.countAll", query = "SELECT COUNT(y) FROM YvsMutCredit y WHERE y.compte.mutualiste.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutCredit.findById", query = "SELECT y FROM YvsMutCredit y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutCredit.findByMutuelle", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste.mutuelle = :mutuelle ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsMutCredit.findByMutuelleDates", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste.mutuelle = :mutuelle AND y.dateCredit BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutCredit.findByMutuelleDatesEtat", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste.mutuelle = :mutuelle AND y.etat = :etat AND y.dateCredit BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutCredit.findByMutualiste", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste = :mutualiste"),
    @NamedQuery(name = "YvsMutCredit.findByMutualisteDates", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste = :mutualiste AND y.dateEffet BETWEEN :dateDebut AND :dateFin AND y.etat=:etat"),
    @NamedQuery(name = "YvsMutCredit.findByMutualisteEtat", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste = :mutualiste AND y.etat = :etat"),
    @NamedQuery(name = "YvsMutCredit.findByMutualisteEtatDates", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste = :mutualiste AND y.etat = :etat AND y.dateCredit BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutCredit.findByCompte", query = "SELECT y FROM YvsMutCredit y WHERE y.compte = :compte"),
    @NamedQuery(name = "YvsMutCredit.findByReference", query = "SELECT y FROM YvsMutCredit y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsMutCredit.findByNumOp", query = "SELECT y FROM YvsMutCredit y WHERE y.reference LIKE :numeroPiece AND y.compte.mutualiste.mutuelle=:mutuelle ORDER BY y.reference DESC"),
    @NamedQuery(name = "YvsMutCredit.findByDateCredit", query = "SELECT y FROM YvsMutCredit y WHERE y.dateCredit = :dateCredit"),
    @NamedQuery(name = "YvsMutCredit.findByMontant", query = "SELECT y FROM YvsMutCredit y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsMutCredit.findByCompteEtat", query = "SELECT y FROM YvsMutCredit y WHERE y.compte = :compte AND y.etat = :etat"),
    @NamedQuery(name = "YvsMutCredit.findByCompteEtats", query = "SELECT y FROM YvsMutCredit y WHERE y.compte = :compte AND y.etat IN :etat"),
    @NamedQuery(name = "YvsMutCredit.findByMutualisteEtat", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste = :mutualiste AND y.etat = :etat"),
    @NamedQuery(name = "YvsMutCredit.findByMutualisteEtats", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste = :mutualiste AND y.etat IN :etat"),
    @NamedQuery(name = "YvsMutCredit.findByMutuelleEtat", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste.mutuelle = :mutuelle AND y.etat = :etat"),
    @NamedQuery(name = "YvsMutCredit.findByMutuelleEtats", query = "SELECT y FROM YvsMutCredit y WHERE y.compte.mutualiste.mutuelle = :mutuelle AND y.etat IN :etat"),
    @NamedQuery(name = "YvsMutCredit.findByEtat", query = "SELECT y FROM YvsMutCredit y WHERE y.etat = :etat"),

    @NamedQuery(name = "YvsMutCredit.sumByMutualisteType", query = "SELECT SUM(y.montant) FROM YvsMutCredit y WHERE y.compte.mutualiste = :mutualiste AND y.type = :type"),
    @NamedQuery(name = "YvsMutCredit.countByMutualisteType", query = "SELECT COUNT(y) FROM YvsMutCredit y WHERE y.compte.mutualiste = :mutualiste AND y.type = :type"),
    @NamedQuery(name = "YvsMutCredit.CountByMutualisteTypeStatut", query = "SELECT COUNT(y) FROM YvsMutCredit y WHERE y.compte.mutualiste = :mutualiste AND y.statutCredit = :statut AND y.type = :type")})
public class YvsMutCredit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_credit_id_seq", name = "yvs_mut_credit_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_credit_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Size(max = 2147483647)
    @Column(name = "motif_refus")
    private String motifRefus;
    @Column(name = "date_credit")
    @Temporal(TemporalType.DATE)
    private Date dateCredit;
    @Column(name = "date_effet")
    @Temporal(TemporalType.DATE)
    private Date dateEffet;
    @Column(name = "date_soumission")
    @Temporal(TemporalType.DATE)
    private Date dateSoumission;
    @Column(name = "montant")
    private Double montant;
    @Column(name = "frais_additionnel")
    private Double fraisAdditionnel;
    @Size(max = 2147483647)
    @Column(name = "etat")
    private String etat;
    @Column(name = "statut_credit")
    private Character statutCredit;
    @Column(name = "statut_paiement")
    private Character statutPaiement;
    @Column(name = "automatique")
    private Boolean automatique;
    @Column(name = "duree")
    private Integer duree;
    @Column(name = "heure_credit")
    @Temporal(TemporalType.TIME)
    private Date heureCredit;

    @JoinColumn(name = "type", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutTypeCredit type;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCompte compte;
    @JoinColumn(name = "credit_tansfere", referencedColumnName = "id")
    @OneToOne
    private YvsMutCredit creditTransfere;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "credit")
    private List<YvsMutConditionCredit> conditions;
    @OneToMany(mappedBy = "credit")
    private List<YvsMutEchellonage> remboursements;
    @OneToMany(mappedBy = "credit")
    private List<YvsMutAvaliseCredit> avalises;
    @OneToMany(mappedBy = "credit")
    private List<YvsMutVoteValidationCredit> votes;
    @OneToMany(mappedBy = "credit")
    private List<YvsMutReglementCredit> reglements;

    @Transient
    private boolean selectActif;
    @Transient
    private List<YvsMutVoteValidationCredit> votesApprouve;
    @Transient
    private List<YvsMutVoteValidationCredit> votesDeapprouve;
    @Transient
    private YvsMutEchellonage echeancier = new YvsMutEchellonage();
    @Transient
    private String nameApprouve;
    @Transient
    private String nameDeapprouve;
    @Transient
    private boolean new_;
    @Transient
    private boolean update;
    @Transient
    private double montantDispo;
    @Transient
    private double montantReste;
    @Transient
    private double montantVerse;
    @Transient
    private double montantEncaisse;
    @Transient
    private double montantRestant;

    public YvsMutCredit() {
        conditions = new ArrayList<>();
        votes = new ArrayList<>();
        votesApprouve = new ArrayList<>();
        votesDeapprouve = new ArrayList<>();
        avalises = new ArrayList<>();
        remboursements = new ArrayList<>();
        reglements = new ArrayList<>();
    }

    public YvsMutCredit(Long id) {
        this();
        this.id = id;
    }

    public YvsMutCredit(Long id, YvsMutCredit y) {
        this(id);
        this.reference = y.reference;
        this.motifRefus = y.motifRefus;
        this.dateCredit = y.dateCredit;
        this.dateEffet = y.dateEffet;
        this.dateSoumission = y.dateSoumission;
        this.montant = y.montant;
        this.fraisAdditionnel = y.fraisAdditionnel;
        this.etat = y.etat;
        this.statutCredit = y.statutCredit;
        this.automatique = y.automatique;
        this.duree = y.duree;
        this.montantVerse = y.montantVerse;
        this.heureCredit = y.heureCredit;
        this.type = y.type;
        this.compte = y.compte;
        this.creditTransfere = y.creditTransfere;
        this.author = y.author;
        this.conditions = y.conditions;
        this.remboursements = y.remboursements;
        this.avalises = y.avalises;
        this.votes = y.votes;
        this.reglements = y.reglements;
        this.selectActif = y.selectActif;
        this.votesApprouve = y.votesApprouve;
        this.votesDeapprouve = y.votesDeapprouve;
        this.nameApprouve = y.nameApprouve;
        this.nameDeapprouve = y.nameDeapprouve;
        this.new_ = y.new_;
        this.update = y.update;
        this.montantDispo = y.montantDispo;
        this.montantReste = y.montantReste;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    /**
     * Reste à verser à l'emprunteur*
     */
    public double getMontantReste() {
        montantReste = getMontant() - getMontantVerse();
        return montantReste;
    }

    public void setMontantReste(double montantreste) {
        this.montantReste = montantreste;
    }

    public List<YvsMutReglementCredit> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsMutReglementCredit> reglements) {
        this.reglements = reglements;
    }

    public Double getFraisAdditionnel() {
        return fraisAdditionnel != null ? fraisAdditionnel : 0;
    }

    public void setFraisAdditionnel(Double fraisAdditionnel) {
        this.fraisAdditionnel = fraisAdditionnel;
    }

    public Character getStatutPaiement() {
        return statutPaiement != null ? String.valueOf(statutPaiement).trim().length() > 0 ? statutPaiement : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutPaiement(Character statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public Character getStatutCredit() {
        return statutCredit != null ? String.valueOf(statutCredit).trim().length() > 0 ? statutCredit : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutCredit(Character statutCredit) {
        this.statutCredit = statutCredit;
    }

    public Date getDateSoumission() {
        return dateSoumission != null ? dateSoumission : new Date();
    }

    public void setDateSoumission(Date dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public String getNameApprouve() {
        return nameApprouve;
    }

    public void setNameApprouve(String nameApprouve) {
        this.nameApprouve = nameApprouve;
    }

    public String getNameDeapprouve() {
        return nameDeapprouve;
    }

    public void setNameDeapprouve(String nameDeapprouve) {
        this.nameDeapprouve = nameDeapprouve;
    }

    public List<YvsMutVoteValidationCredit> getVotes() {
        return votes;
    }

    public void setVotes(List<YvsMutVoteValidationCredit> votes) {
        this.votes = votes;
    }

    public List<YvsMutVoteValidationCredit> getVotesApprouve() {
        votesApprouve.clear();
        if (getVotes() != null) {
            for (YvsMutVoteValidationCredit v : getVotes()) {
                if (v.getAccepte()) {
                    votesApprouve.add(v);
                }
            }
        }
        return votesApprouve;
    }

    public void setVotesApprouve(List<YvsMutVoteValidationCredit> votesApprouve) {
        this.votesApprouve = votesApprouve;
    }

    public List<YvsMutVoteValidationCredit> getVotesDeapprouve() {
        votesDeapprouve.clear();
        if (getVotes() != null) {
            for (YvsMutVoteValidationCredit v : getVotes()) {
                if (!v.getAccepte()) {
                    votesDeapprouve.add(v);
                }
            }
        }
        return votesDeapprouve;
    }

    public void setVotesDeapprouve(List<YvsMutVoteValidationCredit> votesDeapprouve) {
        this.votesDeapprouve = votesDeapprouve;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
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

    public double getMontantDispo() {
        if (getAvalises() != null) {
            for (YvsMutAvaliseCredit a : getAvalises()) {
                montantDispo += a.getMontant();
            }
        }
        if (getCompte() != null ? getCompte().getMutualiste() != null : false) {
            montantDispo += getCompte().getMutualiste().getMontantEpargne();
        }
        return montantDispo;
    }

    public void setMontantDispo(double montantDispo) {
        this.montantDispo = montantDispo;
    }

    public Boolean isAutomatique() {
        return (automatique == null) ? false : automatique;
    }

    public void setAutomatique(Boolean automatique) {
        this.automatique = automatique;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getMotifRefus() {
        return motifRefus;
    }

    public void setMotifRefus(String motifRefus) {
        this.motifRefus = motifRefus;
    }

    public String getReference() {
        return reference != null ? reference : "";
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDateCredit() {
        return dateCredit;
    }

    public void setDateCredit(Date dateCredit) {
        this.dateCredit = dateCredit;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public YvsMutTypeCredit getType() {
        return type;
    }

    public void setType(YvsMutTypeCredit type) {
        this.type = type;
    }

    public YvsMutCompte getCompte() {
        return compte;
    }

    public void setCompte(YvsMutCompte compte) {
        this.compte = compte;
    }

    public List<YvsMutAvaliseCredit> getAvalises() {
        return avalises;
    }

    public void setAvalises(List<YvsMutAvaliseCredit> avalises) {
        this.avalises = avalises;
    }

    public Date getDateEffet() {
        return dateEffet;
    }

    public void setDateEffet(Date dateEffet) {
        this.dateEffet = dateEffet;
    }

    public YvsMutCredit getCreditTransfere() {
        return creditTransfere;
    }

    public void setCreditTransfere(YvsMutCredit creditTransfere) {
        this.creditTransfere = creditTransfere;
    }

    public Date getHeureCredit() {
        return heureCredit;
    }

    public void setHeureCredit(Date heureCredit) {
        this.heureCredit = heureCredit;
    }

    public YvsMutEchellonage getEcheancier() {
        if (getRemboursements() != null ? !getRemboursements().isEmpty() : false) {
            for (YvsMutEchellonage y : getRemboursements()) {
                if (y.getActif()) {
                    echeancier = y;
                    break;
                }
            }
        }
        return echeancier;
    }

    public void setEcheancier(YvsMutEchellonage echeancier) {
        this.echeancier = echeancier;
    }

    public List<YvsMutEchellonage> getRemboursements() {
        return remboursements != null ? remboursements : new ArrayList<YvsMutEchellonage>();
    }

    public void setRemboursements(List<YvsMutEchellonage> remboursements) {
        this.remboursements = remboursements;
    }

    /**Reste à rembourser**/
    public double getMontantRestant() {
        montantRestant = getMontant() - getMontantEncaisse();
        return montantRestant;
    }

    public void setMontantRestant(double montantRestant) {
        this.montantRestant = montantRestant;
    }

    public double getMontantEncaisse() {
        montantEncaisse = 0;
        if (remboursements != null) {
            for (YvsMutEchellonage p : remboursements) {
                montantEncaisse += p.getMontantVerse();
            }
        }
        return montantEncaisse;
    }

    public void setMontantEncaisse(double montantEncaisse) {
        this.montantEncaisse = montantEncaisse;
    }

    /**Montant déjà remis à l'emprunteur**/
    public double getMontantVerse() {
        montantVerse = 0;
        if (reglements != null) {
            for (YvsMutReglementCredit p : reglements) {
                if (p.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    montantVerse += p.getMontant();
                }
            }
        }
        return montantVerse;
    }

    public void setMontantVerse(double montantVerse) {
        this.montantVerse = montantVerse;
    }

    public List<YvsMutConditionCredit> getConditions() {
        return conditions;
    }

    public void setConditions(List<YvsMutConditionCredit> conditions) {
        this.conditions = conditions;
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
        if (!(object instanceof YvsMutCredit)) {
            return false;
        }
        YvsMutCredit other = (YvsMutCredit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.credit.YvsMutCredit[ id=" + id + " ]";
    }

}
