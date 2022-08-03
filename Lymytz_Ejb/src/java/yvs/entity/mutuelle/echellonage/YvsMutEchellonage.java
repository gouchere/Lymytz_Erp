/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.echellonage;

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
import yvs.dao.salaire.service.Constantes;
import yvs.entity.mutuelle.credit.YvsMutCredit;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_echellonage")
@NamedQueries({
    @NamedQuery(name = "YvsMutEchellonage.findAll", query = "SELECT y FROM YvsMutEchellonage y WHERE y.credit.compte.mutualiste.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutEchellonage.findById", query = "SELECT y FROM YvsMutEchellonage y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutEchellonage.findByMutuelle", query = "SELECT y FROM YvsMutEchellonage y WHERE y.credit.compte.mutualiste.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutEchellonage.findEncours", query = "SELECT y FROM YvsMutEchellonage y WHERE y.credit.compte.mutualiste =:mutualiste AND y.credit.dateEffet>=:date AND y.etat='EnCours' AND y.credit.etat!='Reglé' AND y.credit.id!=:idCredit"),
    @NamedQuery(name = "YvsMutEchellonage.findByMutuelleDate", query = "SELECT y FROM YvsMutEchellonage y WHERE y.credit.compte.mutualiste.mutuelle = :mutuelle AND y.dateEchellonage = :dateEchellonage"),
    @NamedQuery(name = "YvsMutEchellonage.findByMutuelleDates", query = "SELECT y FROM YvsMutEchellonage y WHERE y.credit.compte.mutualiste.mutuelle = :mutuelle AND y.dateEchellonage BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutEchellonage.findByDateEchellonage", query = "SELECT y FROM YvsMutEchellonage y WHERE y.dateEchellonage = :dateEchellonage"),
    @NamedQuery(name = "YvsMutEchellonage.findByMontant", query = "SELECT y FROM YvsMutEchellonage y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsMutEchellonage.findByCredit", query = "SELECT y FROM YvsMutEchellonage y WHERE y.credit = :credit ORDER BY y.dateEchellonage DESC"),
    @NamedQuery(name = "YvsMutEchellonage.findCurrentByCredit", query = "SELECT y FROM YvsMutEchellonage y WHERE y.credit = :credit AND y.actif = true"),
    @NamedQuery(name = "YvsMutEchellonage.findCurrentByCredit_", query = "SELECT y FROM YvsMutEchellonage y WHERE y.credit = :credit AND y.actif = true AND y.etat=:etat"),
    @NamedQuery(name = "YvsMutEchellonage.findByMutuelleEtat", query = "SELECT y FROM YvsMutEchellonage y WHERE y.credit.compte.mutualiste.mutuelle = :mutuelle AND y.etat = :etat"),
    @NamedQuery(name = "YvsMutEchellonage.findByEtat", query = "SELECT y FROM YvsMutEchellonage y WHERE y.etat = :etat"),
    @NamedQuery(name = "YvsMutEchellonage.findNotByEtats", query = "SELECT y FROM YvsMutEchellonage y WHERE y.etat NOT IN (:etat) AND y.actif = true"),
    @NamedQuery(name = "YvsMutEchellonage.findByTaux", query = "SELECT y FROM YvsMutEchellonage y WHERE y.taux = :taux"),
    @NamedQuery(name = "YvsMutEchellonage.findByDureeEcheance", query = "SELECT y FROM YvsMutEchellonage y WHERE y.dureeEcheance = :dureeEcheance")})
public class YvsMutEchellonage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_echellonage_id_seq", name = "yvs_mut_echellonage_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_echellonage_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_echellonage")
    @Temporal(TemporalType.DATE)
    private Date dateEchellonage;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "ecart_mensualite")
    private Integer ecartMensualite;
    @Size(max = 2147483647)
    @Column(name = "etat")
    private String etat;
    @Column(name = "taux")
    private Double taux;
    @Column(name = "duree_echeance")
    private Double dureeEcheance;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "credit_retains_interet")
    private Boolean creditRetainsInteret;

    @JoinColumn(name = "credit", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCredit credit;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "echellonage")
    private List<YvsMutMensualite> mensualites;

    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean update;
    @Transient
    private double montantReste;
    @Transient
    private double montantInteret;
    @Transient
    private double montantVerse;

    public YvsMutEchellonage() {
        mensualites = new ArrayList<>();
    }

    public YvsMutEchellonage(Long id) {
        this.id = id;
        mensualites = new ArrayList<>();
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

    public double getMontantInteret() {
        montantInteret = 0;
        for (YvsMutMensualite m : mensualites) {
            if (!m.getEtat().equals(Constantes.ETAT_SUSPENDU)) {
                montantInteret += m.getInteret();
            }
        }
        return montantInteret;
    }

    public void setMontantInteret(double montantInteret) {
        this.montantInteret = montantInteret;
    }

    public double getMontantReste() {
        montantReste = getMontant() - getMontantVerse();
        return montantReste;
    }

    public double getMontantVerse() {
        montantVerse = 0;
        for (YvsMutMensualite m : mensualites) {
            montantVerse += m.getMontantVerse();
        }
        return montantVerse +(getCreditRetainsInteret()?getMontantInteret():0);
    }

    public void setMontantVerse(double montantVerse) {
        this.montantVerse = montantVerse;
    }

    public void setMontantReste(double montantReste) {
        this.montantReste = montantReste;
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

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Integer getEcartMensualite() {
        return ecartMensualite;
    }

    public void setEcartMensualite(Integer ecartMensualite) {
        this.ecartMensualite = ecartMensualite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateEchellonage() {
        return dateEchellonage;
    }

    public void setDateEchellonage(Date dateEchellonage) {
        this.dateEchellonage = dateEchellonage;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getEtat() {
        return etat != null ? etat.trim().length() > 0 ? etat : "W" : "W";
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Double getTaux() {
        return taux != null ? taux : 0;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Double getDureeEcheance() {
        return dureeEcheance != null ? dureeEcheance : 0;
    }

    public void setDureeEcheance(Double dureeEcheance) {
        this.dureeEcheance = dureeEcheance;
    }

    public List<YvsMutMensualite> getMensualites() {
        return mensualites;
    }

    public void setMensualites(List<YvsMutMensualite> mensualites) {
        this.mensualites = mensualites;
    }

    public YvsMutCredit getCredit() {
        return credit;
    }

    public void setCredit(YvsMutCredit credit) {
        this.credit = credit;
    }

    public Boolean getActif() {
        return this.actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getCreditRetainsInteret() {
        return creditRetainsInteret != null ? creditRetainsInteret : false;
    }

    public void setCreditRetainsInteret(Boolean creditRetainsInteret) {
        this.creditRetainsInteret = creditRetainsInteret;
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
        if (!(object instanceof YvsMutEchellonage)) {
            return false;
        }
        YvsMutEchellonage other = (YvsMutEchellonage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.remboursement.YvsMutEchellonage[ id=" + id + " ]";
    }

}
