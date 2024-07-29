/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_parametre")
@NamedQueries({
    @NamedQuery(name = "YvsMutParametre.findAll", query = "SELECT y FROM YvsMutParametre y WHERE y.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutParametre.findByMutuelle", query = "SELECT y FROM YvsMutParametre y WHERE y.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutParametre.findById", query = "SELECT y FROM YvsMutParametre y WHERE y.id = :id")})
public class YvsMutParametre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_parametre_id_seq", name = "yvs_mut_parametre_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_parametre_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "duree_membre")
    private Integer dureeMembre;
    @Column(name = "valid_credit_by_vote")
    private Boolean validCreditByVote;
    @Column(name = "duree_etude_credit")
    private Integer dureeEtudeCredit;
    @Column(name = "taux_vote_valid_credit_correct")
    private Double tauxVoteValidCreditCorrect;
    @Column(name = "taux_vote_valid_credit_incorrect")
    private Double tauxVoteValidCreditIncorrect;
    @Column(name = "retains_epargne")
    private Boolean retainsEpargne;
    @Size(max = 2147483647)
    @Column(name = "monnaie")
    private String monnaie;
    @Column(name = "arrondi_qte")
    private Double arrondiQte;
    @Column(name = "souscription_general")
    private Boolean souscriptionGeneral;
    @Column(name = "capacite_endettement")
    private Double capaciteEndettement;
    @Column(name = "base_capacite_endettement")
    private String baseCapaciteEndettement;
    @Column(name = "quotite_cessible")
    private Double quotiteCessible;
    @Column(name = "periode_salaire_moyen")
    private Integer periodeSalaireMoyen;
    /**
     * Correspond au taux de couverture du crédit par l'Epargne et les Avalise.
     * Si taux=80%, ie Epargne+ Avilise doit être >= 80% du crédit
     */
    @Column(name = "taux_couverture_credit")
    private Double tauxCouvertureCredit;
    @Column(name = "arrondi_valeur")
    private Integer arrondiValeur;
    @Column(name = "debut_epargne")
    private Integer debutEpargne;   //Période autorisé pour l'enregistrement d'une épargne
    @Column(name = "fin_epargne")
    private Integer finEpargne;   //Période autorisé pour l'enregistrement d'une épargne
    @Column(name = "retard_saisie_epargne")
    private Integer retardSaisieEpargne;//Nombre de jour de retard autorisé lors de la saisie d'une épargne

    @Column(name = "paiement_par_compte_strict")
    private Boolean paiementParCompteStrict;
    @Column(name = "accept_retrait_epargne")
    private Boolean acceptRetraitEpargne;
    @Column(name = "credit_retains_interet")
    private Boolean creditRetainsInteret;

    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsMutParametre() {
    }

    public YvsMutParametre(Long id) {
        this.id = id;
    }

    public Double getQuotiteCessible() {
        return quotiteCessible != null ? quotiteCessible : 0;
    }

    public void setQuotiteCessible(Double quotiteCessible) {
        this.quotiteCessible = quotiteCessible;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Integer getPeriodeSalaireMoyen() {
        return periodeSalaireMoyen != null ? periodeSalaireMoyen : 0;
    }

    public void setPeriodeSalaireMoyen(Integer periodeSalaireMoyen) {
        this.periodeSalaireMoyen = periodeSalaireMoyen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDureeMembre() {
        return dureeMembre;
    }

    public void setDureeMembre(Integer dureeMembre) {
        this.dureeMembre = dureeMembre;
    }

    public YvsMutMutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(YvsMutMutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public Integer getArrondiValeur() {
        return arrondiValeur != null ? arrondiValeur : 2;
    }

    public void setArrondiValeur(Integer arrondiValeur) {
        this.arrondiValeur = arrondiValeur;
    }

    public String getMonnaie() {
        return monnaie;
    }

    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }

    public Double getArrondiQte() {
        return arrondiQte;
    }

    public void setArrondiQte(Double arrondiQte) {
        this.arrondiQte = arrondiQte;
    }

    public Boolean getSouscriptionGeneral() {
        return souscriptionGeneral != null ? souscriptionGeneral : false;
    }

    public void setSouscriptionGeneral(Boolean souscriptionGeneral) {
        this.souscriptionGeneral = souscriptionGeneral;
    }

    public Double getTauxCouvertureCredit() {
        return tauxCouvertureCredit != null ? tauxCouvertureCredit : 0;
    }

    public void setTauxCouvertureCredit(Double tauxCouvertureCredit) {
        this.tauxCouvertureCredit = tauxCouvertureCredit;
    }

    public Boolean getPaiementParCompteStrict() {
        return paiementParCompteStrict != null ? paiementParCompteStrict : false;
    }

    public void setPaiementParCompteStrict(Boolean paiementParCompteStrict) {
        this.paiementParCompteStrict = paiementParCompteStrict;
    }

    public Boolean getAcceptRetraitEpargne() {
        return acceptRetraitEpargne != null ? acceptRetraitEpargne : false;
    }

    public void setAcceptRetraitEpargne(Boolean acceptRetraitEpargne) {
        this.acceptRetraitEpargne = acceptRetraitEpargne;
    }

    public Integer getDebutEpargne() {
        return debutEpargne != null ? debutEpargne : 1;
    }

    public void setDebutEpargne(Integer debutEpargne) {
        this.debutEpargne = debutEpargne;
    }

    public Integer getFinEpargne() {
        return finEpargne != null ? finEpargne : 1;
    }

    public void setFinEpargne(Integer finEpargne) {
        this.finEpargne = finEpargne;
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
        if (!(object instanceof YvsMutParametre)) {
            return false;
        }
        YvsMutParametre other = (YvsMutParametre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.YvsMutParametre[ id=" + id + " ]";
    }

    public Boolean getRetainsEpargne() {
        return retainsEpargne!=null?retainsEpargne:true;
    }

    public void setRetainsEpargne(Boolean retainsEpargne) {
        this.retainsEpargne = retainsEpargne;
    }

    public Boolean getValidCreditByVote() {
        return validCreditByVote != null ? validCreditByVote : false;
    }

    public void setValidCreditByVote(Boolean validCreditByVote) {
        this.validCreditByVote = validCreditByVote;
    }

    public Integer getDureeEtudeCredit() {
        return dureeEtudeCredit != null ? dureeEtudeCredit : 0;
    }

    public void setDureeEtudeCredit(Integer dureeEtudeCredit) {
        this.dureeEtudeCredit = dureeEtudeCredit;
    }

    public Double getTauxVoteValidCreditCorrect() {
        return tauxVoteValidCreditCorrect != null ? tauxVoteValidCreditCorrect : 0;
    }

    public void setTauxVoteValidCreditCorrect(Double tauxVoteValidCreditCorrect) {
        this.tauxVoteValidCreditCorrect = tauxVoteValidCreditCorrect;
    }

    public Double getTauxVoteValidCreditIncorrect() {
        return tauxVoteValidCreditIncorrect != null ? tauxVoteValidCreditIncorrect : 0;
    }

    public void setTauxVoteValidCreditIncorrect(Double tauxVoteValidCreditIncorrect) {
        this.tauxVoteValidCreditIncorrect = tauxVoteValidCreditIncorrect;
    }

    public Integer getRetardSaisieEpargne() {
        return retardSaisieEpargne!=null?retardSaisieEpargne:0;
    }

    public void setRetardSaisieEpargne(Integer retardSaisieEpargne) {
        this.retardSaisieEpargne = retardSaisieEpargne;
    }

    public Boolean getCreditRetainsInteret() {
        return creditRetainsInteret != null ? creditRetainsInteret : false;
    }

    public void setCreditRetainsInteret(Boolean creditRetainsInteret) {
        this.creditRetainsInteret = creditRetainsInteret;
    }

    public Double getCapaciteEndettement() {
        return capaciteEndettement!=null?capaciteEndettement:0;
    }

    public void setCapaciteEndettement(Double capaciteEndettement) {
        this.capaciteEndettement = capaciteEndettement;
    }

    public String getBaseCapaciteEndettement() {
        return baseCapaciteEndettement;
    }

    public void setBaseCapaciteEndettement(String baseCapaciteEndettement) {
        this.baseCapaciteEndettement = baseCapaciteEndettement;
    }

}
