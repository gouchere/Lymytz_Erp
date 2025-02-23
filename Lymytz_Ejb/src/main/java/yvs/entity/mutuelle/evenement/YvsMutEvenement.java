/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.evenement;

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
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_evenement")
@NamedQueries({
    @NamedQuery(name = "YvsMutEvenement.findAll", query = "SELECT y FROM YvsMutEvenement y WHERE y.type.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutEvenement.findById", query = "SELECT y FROM YvsMutEvenement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutEvenement.findByMutualiste", query = "SELECT y FROM YvsMutEvenement y WHERE y.mutualiste = :mutualiste"),
    @NamedQuery(name = "YvsMutEvenement.findByMutuelle", query = "SELECT y FROM YvsMutEvenement y WHERE y.type.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutEvenement.findByMutuelleDates", query = "SELECT y FROM YvsMutEvenement y WHERE y.type.mutuelle = :mutuelle AND y.dateDebut BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutEvenement.findByMutuelleDate", query = "SELECT y FROM YvsMutEvenement y WHERE y.type.mutuelle = :mutuelle AND :dateJour BETWEEN y.dateDebut AND y.dateFin"),
    @NamedQuery(name = "YvsMutEvenement.findByDateDebut", query = "SELECT y FROM YvsMutEvenement y WHERE y.type.mutuelle = :mutuelle AND y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsMutEvenement.findByDateFin", query = "SELECT y FROM YvsMutEvenement y WHERE y.type.mutuelle = :mutuelle AND y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsMutEvenement.findByDateEvenement", query = "SELECT y FROM YvsMutEvenement y WHERE y.type.mutuelle = :mutuelle AND y.dateEvenement = :dateEvenement"),
    @NamedQuery(name = "YvsMutEvenement.findByDescription", query = "SELECT y FROM YvsMutEvenement y WHERE y.type.mutuelle = :mutuelle AND y.description = :description")})
public class YvsMutEvenement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_evenement_id_seq", name = "yvs_mut_evenement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_evenement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_evenement")
    @Temporal(TemporalType.DATE)
    private Date dateEvenement;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "etat")
    private String etat;
    @Column(name = "montant_obligatoire")
    private Double montantObligatoire;
    @Column(name = "heure_evenement")
    @Temporal(TemporalType.DATE)
    private Date heureEvenement;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "heure_debut")
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    @Column(name = "date_ouverture_contribution")
    @Temporal(TemporalType.DATE)
    private Date dateOuvertureContribution;
    @Column(name = "date_cloture_contribution")
    @Temporal(TemporalType.DATE)
    private Date dateClotureContribution;

    @JoinColumn(name = "caisse_event", referencedColumnName = "id")
    @ManyToOne
    private YvsMutCaisse caisseEvent;
    @JoinColumn(name = "lieu", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire lieu;
    @JoinColumn(name = "type", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutTypeEvenement type;
    @JoinColumn(name = "mutualiste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutualiste mutualiste;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "evenement")
    private List<YvsMutActivite> activites;
    @OneToMany(mappedBy = "evenement")
    private List<YvsMutContributionEvenement> contributions;
    @OneToMany(mappedBy = "evenement")
    private List<YvsMutCoutEvenement> couts;

    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private double montantAttendu;
    @Transient
    private double coutTotal;
    @Transient
    private double montantRecu;
    @Transient
    private List<YvsMutParticipantEvenement> participants;
    @Transient
    private List<YvsMutFinancementActivite> financements;

    public YvsMutEvenement() {
        couts = new ArrayList<>();
        participants = new ArrayList<>();
        financements = new ArrayList<>();
        activites = new ArrayList<>();
        contributions = new ArrayList<>();
    }

    public YvsMutEvenement(Long id) {
        this();
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

    public double getCoutTotal() {
        if (getCouts() != null) {
            for (YvsMutCoutEvenement c : getCouts()) {
                coutTotal += c.getMontant();
            }
        }
        return coutTotal;
    }

    public void setCoutTotal(double coutTotal) {
        this.coutTotal = coutTotal;
    }

    public double getMontantAttendu() {
        montantAttendu = 0;
        if (getActivites() != null) {
            for (YvsMutActivite ce : getActivites()) {
                montantAttendu += ce.getMontantRequis();
            }
        }
        return montantAttendu;
    }

    public void setMontantAttendu(double montantAttendu) {
        this.montantAttendu = montantAttendu;
    }

    public double getMontantRecu() {
        montantRecu = 0;
        if (getContributions() != null) {
            for (YvsMutContributionEvenement ce : getContributions()) {
                montantRecu += ce.getMontant();
            }
        }
        return montantRecu;
    }

    public Double getMontantObligatoire() {
        return montantObligatoire != null ? montantObligatoire : 0;
    }

    public void setMontantObligatoire(Double montantObligatoire) {
        this.montantObligatoire = montantObligatoire;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public void setMontantRecu(double montantRecu) {
        this.montantRecu = montantRecu;
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

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(Date dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YvsMutMutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(YvsMutMutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public List<YvsMutFinancementActivite> getFinancements() {
        financements.clear();
        for (YvsMutActivite a : activites) {
            financements.addAll(a.getFinancements());
        }
        return financements;
    }

    public void setFinancements(List<YvsMutFinancementActivite> financements) {
        this.financements = financements;
    }

    public List<YvsMutParticipantEvenement> getParticipants() {
        participants.clear();
        for (YvsMutActivite a : activites) {
            participants.addAll(a.getParticipants());
        }
        return participants;
    }

    public void setParticipants(List<YvsMutParticipantEvenement> participants) {
        this.participants = participants;
    }

    public List<YvsMutActivite> getActivites() {
        return activites;
    }

    public void setActivites(List<YvsMutActivite> activites) {
        this.activites = activites;
    }

    public YvsMutCaisse getCaisseEvent() {
        return caisseEvent;
    }

    public void setCaisseEvent(YvsMutCaisse caisseEvent) {
        this.caisseEvent = caisseEvent;
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
        if (!(object instanceof YvsMutEvenement)) {
            return false;
        }
        YvsMutEvenement other = (YvsMutEvenement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.evenement.YvsMutEvenement[ id=" + id + " ]";
    }

    public YvsMutTypeEvenement getType() {
        return type;
    }

    public void setType(YvsMutTypeEvenement type) {
        this.type = type;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getDateOuvertureContribution() {
        return dateOuvertureContribution;
    }

    public void setDateOuvertureContribution(Date dateOuvertureContribution) {
        this.dateOuvertureContribution = dateOuvertureContribution;
    }

    public Date getDateClotureContribution() {
        return dateClotureContribution;
    }

    public void setDateClotureContribution(Date dateClotureContribution) {
        this.dateClotureContribution = dateClotureContribution;
    }

    public YvsDictionnaire getLieu() {
        return lieu;
    }

    public void setLieu(YvsDictionnaire lieu) {
        this.lieu = lieu;
    }

    public List<YvsMutContributionEvenement> getContributions() {
        return contributions;
    }

    public void setContributions(List<YvsMutContributionEvenement> contributions) {
        this.contributions = contributions;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getHeureEvenement() {
        return heureEvenement;
    }

    public void setHeureEvenement(Date heureEvenement) {
        this.heureEvenement = heureEvenement;
    }

    public List<YvsMutCoutEvenement> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsMutCoutEvenement> couts) {
        this.couts = couts;
    }

}
