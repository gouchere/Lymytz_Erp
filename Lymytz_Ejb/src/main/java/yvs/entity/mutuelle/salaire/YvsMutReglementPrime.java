/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.salaire;

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
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.base.YvsMutPoste;
import yvs.entity.mutuelle.base.YvsMutPrimePoste;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_reglement_prime")
@NamedQueries({
    @NamedQuery(name = "YvsMutReglementPrime.findAll", query = "SELECT y FROM YvsMutReglementPrime y WHERE y.mutualiste.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutReglementPrime.findById", query = "SELECT y FROM YvsMutReglementPrime y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutReglementPrime.findByMutuelle", query = "SELECT y FROM YvsMutReglementPrime y WHERE y.mutualiste.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutReglementPrime.findOne", query = "SELECT y FROM YvsMutReglementPrime y WHERE y.mutualiste = :mutualiste AND y.periode = :periode"),
    @NamedQuery(name = "YvsMutReglementPrime.findByMutuelleDates", query = "SELECT y FROM YvsMutReglementPrime y WHERE y.mutualiste.mutuelle = :mutuelle AND y.datePrime BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutReglementPrime.findByMutualiste", query = "SELECT y FROM YvsMutReglementPrime y WHERE y.mutualiste = :mutualiste"),
    @NamedQuery(name = "YvsMutReglementPrime.findByMutualisteDates", query = "SELECT y FROM YvsMutReglementPrime y WHERE y.mutualiste = :mutualiste AND y.datePrime BETWEEN :dateDebut AND :dateFin ORDER BY y.datePrime DESC"),
    @NamedQuery(name = "YvsMutReglementPrime.findSoePrime", query = "SELECT SUM(y.montant) FROM YvsMutReglementPrime y WHERE y.mutualiste.mutuelle = :mutuelle AND y.datePrime BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutReglementPrime.findByMutualisteDates_", query = "SELECT COUNT(y) FROM YvsMutReglementPrime y WHERE y.mutualiste = :mutualiste AND y.prime=:prime AND y.datePrime BETWEEN :dateDebut AND :dateFin GROUP BY y.datePrime ORDER BY y.datePrime DESC"),
    @NamedQuery(name = "YvsMutReglementPrime.findByDatePrime", query = "SELECT y FROM YvsMutReglementPrime y WHERE y.datePrime = :datePrime")})
public class YvsMutReglementPrime implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_reglement_prime_id_seq", name = "yvs_mut_reglement_prime_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_reglement_prime_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_prime")
    @Temporal(TemporalType.DATE)
    private Date datePrime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "statut_paiement")
    private String statutPaiement;

    @JoinColumn(name = "prime", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutPrimePoste prime;
    @JoinColumn(name = "mutualiste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutualiste mutualiste;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCompte compte;
    @JoinColumn(name = "periode", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutPeriodeExercice periode;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private List<YvsMutReglementPrime> reglements;
    @Transient
    private YvsMutPoste poste;

    public YvsMutReglementPrime() {
        reglements = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsMutReglementPrime(Long id) {
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

    public YvsMutPoste getPoste() {
        if (getPrime() != null) {
            poste = getPrime().getPoste();
        }
        return poste;
    }

    public void setPoste(YvsMutPoste poste) {
        this.poste = poste;
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

    public List<YvsMutReglementPrime> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsMutReglementPrime> reglements) {
        this.reglements = reglements;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatePrime() {
        return datePrime;
    }

    public void setDatePrime(Date datePrime) {
        this.datePrime = datePrime;
    }

    public YvsMutPrimePoste getPrime() {
        return prime;
    }

    public void setPrime(YvsMutPrimePoste prime) {
        this.prime = prime;
    }

    public YvsMutMutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(YvsMutMutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public YvsMutPeriodeExercice getPeriode() {
        return periode;
    }

    public void setPeriode(YvsMutPeriodeExercice periode) {
        this.periode = periode;
    }

    public String getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsMutCompte getCompte() {
        return compte;
    }

    public void setCompte(YvsMutCompte compte) {
        this.compte = compte;
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
        if (!(object instanceof YvsMutReglementPrime)) {
            return false;
        }
        YvsMutReglementPrime other = (YvsMutReglementPrime) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.salaire.YvsMutReglementPrime[ id=" + id + " ]";
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

}
