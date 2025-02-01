/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.salaire;

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
import yvs.dao.salaire.service.Constantes;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_interet")
@NamedQueries({
    @NamedQuery(name = "YvsMutInteret.findAll", query = "SELECT y FROM YvsMutInteret y WHERE y.mutualiste.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutInteret.findById", query = "SELECT y FROM YvsMutInteret y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutInteret.findByMutuelle", query = "SELECT y FROM YvsMutInteret y WHERE y.mutualiste.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutInteret.findByMutualistePeriode", query = "SELECT y FROM YvsMutInteret y WHERE y.mutualiste = :mutualiste AND y.periode=:periode"),
    @NamedQuery(name = "YvsMutInteret.findOne", query = "SELECT y FROM YvsMutInteret y WHERE y.mutualiste = :mutualiste AND y.periode = :periode"),
    @NamedQuery(name = "YvsMutInteret.findByMutualisteDates", query = "SELECT y FROM YvsMutInteret y WHERE y.mutualiste = :mutualiste AND y.dateInteret BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutInteret.findByMutuelleDates", query = "SELECT y FROM YvsMutInteret y WHERE y.mutualiste.mutuelle = :mutuelle AND y.dateInteret BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutInteret.findByDateInteret", query = "SELECT y FROM YvsMutInteret y WHERE y.dateInteret = :dateInteret"),
    @NamedQuery(name = "YvsMutInteret.findByMontant", query = "SELECT y FROM YvsMutInteret y WHERE y.montant = :montant")})
public class YvsMutInteret implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_interet_id_seq", name = "yvs_mut_interet_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_interet_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_interet")
    @Temporal(TemporalType.DATE)
    private Date dateInteret;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "statut_paiement")
    private String statutPaiement;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCompte compte;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCaisse caisse;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @JoinColumn(name = "mutualiste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutualiste mutualiste;
    @JoinColumn(name = "periode", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutPeriodeExercice periode;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private String refCompte;

    public YvsMutInteret() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        if (compte != null) {
            refCompte = compte.getReference();
        }
    }

    public YvsMutInteret(Long id) {
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
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateInteret() {
        return dateInteret;
    }

    public void setDateInteret(Date dateInteret) {
        this.dateInteret = dateInteret;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public YvsMutMutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(YvsMutMutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public Double getTaux() {
        return taux != null ? taux : 0;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public YvsMutPeriodeExercice getPeriode() {
        return periode;
    }

    public void setPeriode(YvsMutPeriodeExercice periode) {
        this.periode = periode;
    }

    public String getStatutPaiement() {
        return statutPaiement != null ? statutPaiement : Constantes.ETAT_ATTENTE;
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

    public String getRefCompte() {
        return refCompte;
    }

    public void setRefCompte(String refCompte) {
        this.refCompte = refCompte;
    }

    public YvsMutCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsMutCaisse caisse) {
        this.caisse = caisse;
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
        if (!(object instanceof YvsMutInteret)) {
            return false;
        }
        YvsMutInteret other = (YvsMutInteret) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.salaire.YvsMutInteret[ id=" + id + " ]";
    }

}
