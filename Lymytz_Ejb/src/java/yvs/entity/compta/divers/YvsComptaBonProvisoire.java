/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.divers;

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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import yvs.dao.salaire.service.Constantes;
import static yvs.dao.salaire.service.Constantes.dfs;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.compta.YvsComptaJustifBonMission;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsWorkflowValidBonProvisoire;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_bon_provisoire")
@NamedQueries({
    @NamedQuery(name = "YvsComptaBonProvisoire.findAll", query = "SELECT y FROM YvsComptaBonProvisoire y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaBonProvisoire.countAll", query = "SELECT COUNT(y) FROM YvsComptaBonProvisoire y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findById", query = "SELECT y FROM YvsComptaBonProvisoire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findByIds", query = "SELECT y FROM YvsComptaBonProvisoire y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findByReference", query = "SELECT y FROM YvsComptaBonProvisoire y WHERE y.agence.societe = :societe AND y.numero LIKE :reference ORDER by y.numero DESC"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findByNumDoc", query = "SELECT y FROM YvsComptaBonProvisoire y WHERE y.agence.societe = :societe AND y.numero = :reference ORDER by y.numero DESC"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findByDateSave", query = "SELECT y FROM YvsComptaBonProvisoire y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findByDateUpdate", query = "SELECT y FROM YvsComptaBonProvisoire y WHERE y.dateUpdate = :dateUpdate"),

    @NamedQuery(name = "YvsComptaBonProvisoire.findNotJustiferByDatesAgence", query = "SELECT y FROM YvsComptaBonProvisoire y "
            + " LEFT JOIN FETCH y.justificatifs LEFT JOIN FETCH y.justificatifsAchats LEFT JOIN FETCH y.justificatifsMissions LEFT JOIN FETCH y.etapesValidations "
            + "WHERE y.agence = :agence AND y.statutJustify != 'J' AND y.dateBon BETWEEN :dateDebut AND :dateFin ORDER BY y.dateBon"),

    @NamedQuery(name = "YvsComptaBonProvisoire.findByNotJustiferDatesAgence", query = "SELECT y FROM YvsComptaBonProvisoire y WHERE y.beneficiaire = :beneficiaire AND y.agence = :agence AND y.statutJustify != 'J' AND y.dateBon BETWEEN :dateDebut AND :dateFin ORDER BY y.dateBon"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findByNotJustiferDates", query = "SELECT y FROM YvsComptaBonProvisoire y WHERE y.beneficiaire = :beneficiaire AND y.agence.societe = :societe AND y.statutJustify != 'J' AND y.dateBon BETWEEN :dateDebut AND :dateFin ORDER BY y.dateBon"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findByNotJustiferDateAgence", query = "SELECT y FROM YvsComptaBonProvisoire y WHERE y.dateBon = :dateBon AND y.agence = :agence AND y.statutJustify != 'J' ORDER BY y.beneficiaire"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findByNotJustiferDate", query = "SELECT y FROM YvsComptaBonProvisoire y WHERE y.dateBon = :dateBon AND y.agence.societe = :societe AND y.statutJustify != 'J' ORDER BY y.beneficiaire"),
    @NamedQuery(name = "YvsComptaBonProvisoire.sumNotJustifieInAgence", query = "SELECT SUM(y.montant) FROM YvsComptaBonProvisoire y WHERE y.statutPaiement = :statutPaiement AND y.agence = :agence AND y.statutJustify != 'J'"),
    @NamedQuery(name = "YvsComptaBonProvisoire.sumNotJustifieInSociete", query = "SELECT SUM(y.montant) FROM YvsComptaBonProvisoire y WHERE y.statutPaiement = :statutPaiement AND y.agence.societe = :societe AND y.statutJustify != 'J'"),
    @NamedQuery(name = "YvsComptaBonProvisoire.countNotJustifieInAgence", query = "SELECT count(y) FROM YvsComptaBonProvisoire y WHERE y.statutPaiement = :statutPaiement AND y.agence = :agence AND y.statutJustify != 'J'"),
    @NamedQuery(name = "YvsComptaBonProvisoire.countNotJustifieInSociete", query = "SELECT count(y) FROM YvsComptaBonProvisoire y WHERE y.statutPaiement = :statutPaiement AND y.agence.societe = :societe AND y.statutJustify != 'J'"),
    @NamedQuery(name = "YvsComptaBonProvisoire.sumNotJustifieForUser", query = "SELECT SUM(y.montant) FROM YvsComptaBonProvisoire y WHERE y.statutPaiement = :statutPaiement AND y.validerBy=:users  AND y.statutJustify != 'J'"),
    @NamedQuery(name = "YvsComptaBonProvisoire.countNotJustifieForUser", query = "SELECT COUNT(y) FROM YvsComptaBonProvisoire y WHERE y.statutPaiement = :statutPaiement AND y.validerBy=:users  AND y.statutJustify != 'J'"),

    @NamedQuery(name = "YvsComptaBonProvisoire.findDateByNotJustiferDatesAgence", query = "SELECT DISTINCT y.dateBon FROM YvsComptaBonProvisoire y WHERE y.agence = :agence AND y.statutJustify != 'J' AND y.dateBon BETWEEN :dateDebut AND :dateFin ORDER BY y.dateBon"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findDateByNotJustiferDates", query = "SELECT DISTINCT y.dateBon FROM YvsComptaBonProvisoire y WHERE y.agence.societe = :societe AND y.statutJustify != 'J' AND y.dateBon BETWEEN :dateDebut AND :dateFin ORDER BY y.dateBon"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findBeneficiaireByNotJustiferDatesAgence", query = "SELECT DISTINCT y.beneficiaire FROM YvsComptaBonProvisoire y WHERE y.agence = :agence AND y.statutJustify != 'J' AND y.dateBon BETWEEN :dateDebut AND :dateFin ORDER BY y.beneficiaire"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findBeneficiaireByNotJustiferDates", query = "SELECT DISTINCT y.beneficiaire FROM YvsComptaBonProvisoire y WHERE y.agence.societe = :societe AND y.statutJustify != 'J' AND y.dateBon BETWEEN :dateDebut AND :dateFin ORDER BY y.beneficiaire"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findTiersByNotJustiferDatesAgence", query = "SELECT DISTINCT y.tiers FROM YvsComptaBonProvisoire y WHERE y.agence = :agence AND y.statutJustify != 'J' AND y.dateBon BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaBonProvisoire.findTiersByNotJustiferDates", query = "SELECT DISTINCT y.tiers FROM YvsComptaBonProvisoire y WHERE y.agence.societe = :societe AND y.statutJustify != 'J' AND y.dateBon BETWEEN :dateDebut AND :dateFin")})
public class YvsComptaBonProvisoire implements Serializable {

    @Size(max = 1)
    @Column(name = "statut")
    private String statut;
    @Size(max = 1)
    @Column(name = "statut_paiement")
    private String statutPaiement;
    @Size(max = 1)
    @Column(name = "statut_justify")
    private String statutJustify;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_bon_provisoire_id_seq", name = "yvs_compta_bon_provisoire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_bon_provisoire_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero")
    private String numero;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_bon")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateBon;
    @Column(name = "date_valider")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValider;
    @Column(name = "date_payer")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePayer;
    @Column(name = "date_justofy")
    @Temporal(TemporalType.DATE)
    private Date dateJustify;
    @Column(name = "montant")
    private Double montant;
    @Column(name = "description")
    private String description;
    @Column(name = "etape_total")
    private Integer etapeTotal;
    @Column(name = "etape_valide")
    private Integer etapeValide;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "beneficiaire")
    private String beneficiaire;

    @JoinColumn(name = "type_doc", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTypeDocDivers typeDoc;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers caissier;
    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTiers tiers;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "ordonnateur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers ordonnateur;
    @JoinColumn(name = "valider_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers validerBy;
    @JoinColumn(name = "justify_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers justifyBy;

//    @OneToOne(mappedBy = "piece")
//    private YvsComptaJustifBonMission bonMission;
//    @OneToOne(mappedBy = "bon")
//    private YvsComptaJustifBonAchat bonAchat;
//    @OneToOne(mappedBy = "bon")
//    private YvsComptaJustificatifBon bonDivers;

    @OneToMany(mappedBy = "bon", fetch = FetchType.LAZY)
    private List<YvsComptaJustificatifBon> justificatifs;
    @OneToMany(mappedBy = "bon", fetch = FetchType.LAZY)
    private List<YvsComptaJustifBonAchat> justificatifsAchats;
    @OneToMany(mappedBy = "piece", fetch = FetchType.LAZY)
    private List<YvsComptaJustifBonMission> justificatifsMissions;
    
    @OneToMany(mappedBy = "docCaisse", fetch = FetchType.LAZY)
    private List<YvsWorkflowValidBonProvisoire> etapesValidations;

    @Transient
    private String libEtapes;
    @Transient
    private String maDateSave;
    @Transient
    private double montantJustifie;
    @Transient
    private double attente;
    @Transient
    private double reste;
    @Transient
    private boolean new_;
    @Transient
    private boolean error;

    public YvsComptaBonProvisoire() {
        justificatifs = new ArrayList<>();
        etapesValidations = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaBonProvisoire(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaBonProvisoire(Long id, String numero) {
        this(id);
        this.numero = numero;
    }

    public YvsComptaBonProvisoire(Long id, YvsUsers ordonnateur) {
        this(id);
        this.ordonnateur = ordonnateur;
    }

    public YvsComptaBonProvisoire(boolean error) {
        this();
        this.error = error;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateJustify() {
        return dateJustify;
    }

    public void setDateJustify(Date dateJustify) {
        this.dateJustify = dateJustify;
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

    public String getBeneficiaire() {
        return beneficiaire != null ? beneficiaire : "";
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public YvsUsers getOrdonnateur() {
        return ordonnateur;
    }

    public void setOrdonnateur(YvsUsers ordonnateur) {
        this.ordonnateur = ordonnateur;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaJustificatifBon> getJustificatifs() {
        return justificatifs;
    }

    public void setJustificatifs(List<YvsComptaJustificatifBon> justificatifs) {
        this.justificatifs = justificatifs;
    }

    public String getNumero() {
        return numero != null ? numero : "";
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDateBon() {
        return dateBon != null ? dateBon : new Date();
    }

    public void setDateBon(Date dateBon) {
        this.dateBon = dateBon;
    }
//
//
//    public Character getStatutPaiement() {
//        return statutPaiement != null ? String.valueOf(statutPaiement).trim().length() > 0 ? statutPaiement : Constantes.STATUT_ATTENTE : Constantes.STATUT_ATTENTE;
//    }
//
//    public void setStatutPaiement(Character statutPaiement) {
//        this.statutPaiement = statutPaiement;
//    }
//
//    public Character getStatutJustify() {
//        return statutJustify != null ? String.valueOf(statutJustify).trim().length() > 0 ? statutJustify : Constantes.STATUT_ATTENTE : Constantes.STATUT_ATTENTE;
//    }
//
//    public void setStatutJustify(Character statutJustify) {
//        this.statutJustify = statutJustify;
//    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEtapeTotal() {
        return etapeTotal != null ? etapeTotal : 0;
    }

    public void setEtapeTotal(Integer etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public Integer getEtapeValide() {
        return etapeValide != null ? etapeValide > getEtapeTotal() ? getEtapeTotal() : etapeValide : 0;
    }

    public void setEtapeValide(Integer etapeValide) {
        this.etapeValide = etapeValide;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public YvsUsers getCaissier() {
        return caissier;
    }

    public void setCaissier(YvsUsers caissier) {
        this.caissier = caissier;
    }

    public YvsBaseTiers getTiers() {
        return tiers;
    }

    public void setTiers(YvsBaseTiers tiers) {
        this.tiers = tiers;
    }

    public Date getDateValider() {
        return dateValider;
    }

    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    public YvsUsers getValiderBy() {
        return validerBy;
    }

    public void setValiderBy(YvsUsers validerBy) {
        this.validerBy = validerBy;
    }
    
    public String getLibEtapes() {
        libEtapes = "Etp. " + getEtapeValide() + " / " + getEtapeTotal();
        return libEtapes;
    }

    public void setLibEtapes(String libEtapes) {
        this.libEtapes = libEtapes;
    }

    public double getMontantJustifie() {
        montantJustifie = 0;
        attente = 0;
        if (justificatifs != null) {
            for (YvsComptaJustificatifBon j : justificatifs) {
                montantJustifie += j.getPiece().getMontant();
            }
        }
        if (justificatifsAchats != null) {
            for (YvsComptaJustifBonAchat j : justificatifsAchats) {
                montantJustifie += j.getPiece().getMontant();
            }
        }
        return montantJustifie;
    }

    public void setJustifier(double justifier) {
        this.montantJustifie = justifier;
    }

    public double getAttente() {
        return attente;
    }

    public void setAttente(double attente) {
        this.attente = attente;
    }

    public double getReste() {
        reste = getMontant() - getMontantJustifie();
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsWorkflowValidBonProvisoire> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidBonProvisoire> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Date getDatePayer() {
        return datePayer;
    }

    public void setDatePayer(Date datePayer) {
        this.datePayer = datePayer;
    }

    public String getMaDateSave() {
        return getDateSave() != null ? dfs.format(getDateSave()) : "";
    }

    public void setMaDateSave(String maDateSave) {
        this.maDateSave = maDateSave;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseTypeDocDivers getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(YvsBaseTypeDocDivers typeDoc) {
        this.typeDoc = typeDoc;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaJustifBonMission> getJustificatifsMissions() {
        return justificatifsMissions;
    }

    public void setJustificatifsMissions(List<YvsComptaJustifBonMission> justificatifsMissions) {
        this.justificatifsMissions = justificatifsMissions;
    }

    public String getStatut() {
        return statut != null ? statut : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getStatutPaiement() {
        return statutPaiement != null ? statutPaiement : Constantes.ETAT_ATTENTE;
    }

    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public String getStatutJustify() {
        return statutJustify != null ? statutJustify : Constantes.ETAT_INJUSTIFIE;
    }

    public void setStatutJustify(String statutJustify) {
        this.statutJustify = statutJustify;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaJustifBonAchat> getJustificatifsAchats() {
        return justificatifsAchats;
    }

    public void setJustificatifsAchats(List<YvsComptaJustifBonAchat> justificatifsAchats) {
        this.justificatifsAchats = justificatifsAchats;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsers getJustifyBy() {
        return justifyBy;
    }

    public void setJustifyBy(YvsUsers justifyBy) {
        this.justifyBy = justifyBy;
    }

//    @XmlTransient
//    @JsonIgnore
//    public YvsComptaJustifBonAchat getBonAchat() {
//        return bonAchat;
//    }
//
//    public void setBonAchat(YvsComptaJustifBonAchat bonAchat) {
//        this.bonAchat = bonAchat;
//    }
//
//    @XmlTransient
//    @JsonIgnore
//    public YvsComptaJustificatifBon getBonDivers() {
//        return bonDivers;
//    }
//
//    public void setBonDivers(YvsComptaJustificatifBon bonDivers) {
//        this.bonDivers = bonDivers;
//    }
//
//
//    @XmlTransient
//    @JsonIgnore
//    public YvsComptaJustifBonMission getBonMission() {
//        return bonMission;
//    }
//
//    public void setBonMission(YvsComptaJustifBonMission bonMission) {
//        this.bonMission = bonMission;
//    }

    public int countJustif() {
        int count=justificatifsAchats != null ? justificatifsAchats.size() : 0;
        count += justificatifs != null ? justificatifs.size() : 0;
        count += justificatifsMissions != null ? justificatifsMissions.size() : 0;
        return count;
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
        if (!(object instanceof YvsComptaBonProvisoire)) {
            return false;
        }
        YvsComptaBonProvisoire other = (YvsComptaBonProvisoire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.divers.YvsComptaBonProvisoire[ id=" + id + " ]";
    }

    public boolean canEditable() {
        return getStatut().equals(Constantes.ETAT_ATTENTE) || getStatut().equals(Constantes.ETAT_EDITABLE);
    }

    public boolean canDelete() {
        return getStatut().equals(Constantes.ETAT_ATTENTE)
                || getStatut().equals(Constantes.ETAT_EDITABLE)
                || getStatut().equals(Constantes.ETAT_SUSPENDU)
                || getStatut().equals(Constantes.ETAT_ANNULE);
    }

    //todo: cette méthode devrait plutôt vérifier (que le bon soit validé et payé et que les justif déjà associé ne soient pas sup. au montant payé)
    public boolean canJustify() {
        if (justificatifs != null ? !justificatifs.isEmpty() : false) {
            return true;
        }
        if (justificatifsAchats != null ? !justificatifsAchats.isEmpty() : false) {
            return true;
        }
        if (justificatifsMissions != null ? !justificatifsMissions.isEmpty() : false) {
            return true;
        }
        return false;
    }

}
