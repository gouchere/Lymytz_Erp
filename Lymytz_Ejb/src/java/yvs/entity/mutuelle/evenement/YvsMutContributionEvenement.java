/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.evenement;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_contribution_evenement")
@NamedQueries({
    @NamedQuery(name = "YvsMutContributionEvenement.findAll", query = "SELECT y FROM YvsMutContributionEvenement y WHERE y.compte.mutualiste.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutContributionEvenement.findById", query = "SELECT y FROM YvsMutContributionEvenement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutContributionEvenement.findByMutualiste", query = "SELECT y FROM YvsMutContributionEvenement y WHERE y.compte.mutualiste = :mutualiste"),
    @NamedQuery(name = "YvsMutContributionEvenement.findByEvenement", query = "SELECT y FROM YvsMutContributionEvenement y WHERE y.evenement = :evenement"),
    @NamedQuery(name = "YvsMutContributionEvenement.findByDateContribution", query = "SELECT y FROM YvsMutContributionEvenement y WHERE y.dateContribution = :dateContribution"),
    @NamedQuery(name = "YvsMutContributionEvenement.findByMontant", query = "SELECT y FROM YvsMutContributionEvenement y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsMutContributionEvenement.findSumByEvent", query = "SELECT SUM(y.montant) FROM YvsMutContributionEvenement y WHERE y.evenement= :evenement"),
    @NamedQuery(name = "YvsMutContributionEvenement.findContribution", query = "SELECT SUM(y.montant) FROM YvsMutContributionEvenement y WHERE y.evenement = :evenement AND y.compte = :compte")})
public class YvsMutContributionEvenement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_contribution_evenement_id_seq", name = "yvs_mut_contribution_evenement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_contribution_evenement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_contribution")
    @Temporal(TemporalType.DATE)
    private Date dateContribution;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCompte compte;
    @JoinColumn(name = "evenement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutEvenement evenement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean regle;
    @Transient
    private String etat;
    @Transient
    private double montantVerse;

    public YvsMutContributionEvenement() {
    }

    public YvsMutContributionEvenement(Long id) {
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

    public double getMontantVerse() {
        return montantVerse;
    }

    public void setMontantVerse(double montantVerse) {
        this.montantVerse = montantVerse;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public boolean isRegle() {
        if (getMontant() >= getEvenement().getMontantObligatoire()) {
            setRegle(true);
        }
        return regle;
    }

    public void setRegle(boolean regle) {
        this.regle = regle;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateContribution() {
        return dateContribution != null ? dateContribution : new Date();
    }

    public void setDateContribution(Date dateContribution) {
        this.dateContribution = dateContribution;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public YvsMutCompte getCompte() {
        return compte;
    }

    public void setCompte(YvsMutCompte compte) {
        this.compte = compte;
    }

    public YvsMutEvenement getEvenement() {
        return evenement;
    }

    public void setEvenement(YvsMutEvenement evenement) {
        this.evenement = evenement;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsMutContributionEvenement)) {
            return false;
        }
        YvsMutContributionEvenement other = (YvsMutContributionEvenement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.evenement.YvsMutContributionEvenement[ id=" + id + " ]";
    }

}
