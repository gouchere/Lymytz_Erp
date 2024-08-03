/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import yvs.dao.YvsEntity;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_detail_bulletin")
@NamedQueries({
    @NamedQuery(name = "YvsGrhMontanSalaire.findAll", query = "SELECT y FROM YvsGrhDetailBulletin y"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findNetAPayer", query = "SELECT SUM(y.montantPayer - y.retenuSalariale) FROM YvsGrhDetailBulletin y WHERE y.yvsGrhBulletins=:bulletin"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findByBase", query = "SELECT y FROM YvsGrhDetailBulletin y WHERE y.base = :base"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findOneElement", query = "SELECT y FROM YvsGrhDetailBulletin y WHERE y.yvsGrhBulletins.entete=:periode AND y.element=:element"),
    @NamedQuery(name = "YvsGrhDetailBulletin.findSalaire", query = "SELECT SUM(y.montantPayer-y.retenuSalariale) FROM YvsGrhDetailBulletin y "
            + "WHERE y.yvsGrhBulletins.contrat=:contrat AND y.yvsGrhBulletins.entete.debutMois<=:date AND y.yvsGrhBulletins.entete.finMois>:date "
            + "AND y.element.visibleBulletin=true"),
    @NamedQuery(name = "YvsGrhDetailBulletin.findElementSalaire", query = "SELECT y FROM YvsGrhDetailBulletin y "
            + "WHERE y.yvsGrhBulletins.contrat=:contrat AND y.yvsGrhBulletins.entete.debutMois<=:date AND y.yvsGrhBulletins.entete.finMois>:date "
            + "AND y.element.code=:regle"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findElementEvaluer", query = "SELECT y FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins=:bulletin AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0) ORDER BY y.element.retenue, y.element.rubrique.id, y.element.numSequence"),

    @NamedQuery(name = "YvsGrhMontanSalaire.findMasseBySociete", query = "SELECT SUM(y.montantPayer - y.retenuSalariale) FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins.contrat.employe.agence.societe =:societe AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0)"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findMasseByAgence", query = "SELECT SUM(y.montantPayer - y.retenuSalariale) FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins.contrat.employe.agence =:agence AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0)"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findMasseByContrat", query = "SELECT SUM(y.montantPayer - y.retenuSalariale) FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins.contrat =:contrat AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0)"),

    @NamedQuery(name = "YvsGrhMontanSalaire.findCotisationBySociete", query = "SELECT SUM(y.montantEmployeur) FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins.contrat.employe.agence.societe =:societe AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0) AND ((y.yvsGrhBulletins.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.yvsGrhBulletins.entete.finMois BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findCotisationByAgence", query = "SELECT SUM(y.montantEmployeur) FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins.contrat.employe.agence =:agence AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0) AND ((y.yvsGrhBulletins.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.yvsGrhBulletins.entete.finMois BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findCotisationByContrat", query = "SELECT SUM(y.montantEmployeur) FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins.contrat =:contrat AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0) AND ((y.yvsGrhBulletins.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.yvsGrhBulletins.entete.finMois BETWEEN :dateDebut AND :dateFin))"),

    @NamedQuery(name = "YvsGrhMontanSalaire.findNetAPayerBySociete", query = "SELECT SUM(y.montantPayer - y.retenuSalariale) FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins.contrat.employe.agence.societe =:societe AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0) AND ((y.yvsGrhBulletins.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.yvsGrhBulletins.entete.finMois BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findNetAPayerByAgence", query = "SELECT SUM(y.montantPayer - y.retenuSalariale) FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins.contrat.employe.agence =:agence AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0) AND ((y.yvsGrhBulletins.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.yvsGrhBulletins.entete.finMois BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findNetAPayerByEmploye", query = "SELECT SUM(y.montantPayer - y.retenuSalariale) FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins.contrat.employe =:employe AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0) AND ((y.yvsGrhBulletins.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.yvsGrhBulletins.entete.finMois BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findNetAPayerByContrat", query = "SELECT SUM(y.montantPayer - y.retenuSalariale) FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins.contrat =:contrat AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0) AND ((y.yvsGrhBulletins.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.yvsGrhBulletins.entete.finMois BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsGrhMontanSalaire.findNetAPayerByBulletin", query = "SELECT SUM(y.montantPayer - y.retenuSalariale) FROM YvsGrhDetailBulletin y WHERE y.element.visibleBulletin=true AND y.nowVisible=true AND y.yvsGrhBulletins=:bulletin AND "
            + "(y.montantEmployeur!=0 OR y.montantPayer!=0 OR y.retenuSalariale!=0) AND ((y.yvsGrhBulletins.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.yvsGrhBulletins.entete.finMois BETWEEN :dateDebut AND :dateFin))")})
public class YvsGrhDetailBulletin extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_detail_bulletin_id_seq", name = "yvs_grh_detail_bulletin_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_detail_bulletin_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "taux_salarial")
    private Double tauxSalarial;
    @Column(name = "taux_patronal")
    private Double tauxPatronal;
    @Column(name = "retenu_salariale")
    private double retenuSalariale;
    @Column(name = "montant_payer")
    private double montantPayer;
    @Column(name = "montant_employeur")
    private double montantEmployeur;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "base")
    private Double base;
    @Column(name = "now_visible")
    private Boolean nowVisible;
    @Column(name = "tranche_min")
    private Double trancheMin;
    @Column(name = "tranche_max")
    private Double trancheMax;
    
    @JoinColumn(name = "bulletin", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhBulletins yvsGrhBulletins;
    @JoinColumn(name = "element_salaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhElementSalaire element;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @Transient
    private boolean lineRecap;

    public YvsGrhDetailBulletin(Long id) {
        this.id = id;
    }

    public YvsGrhDetailBulletin() {
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

    public Double getBase() {
        return (base == null) ? 0 : base;
    }

    public void setBase(Double base) {
        this.base = base;
    }

    public YvsGrhBulletins getYvsGrhBulletins() {
        return yvsGrhBulletins;
    }

    public void setYvsGrhBulletins(YvsGrhBulletins yvsGrhBulletins) {
        this.yvsGrhBulletins = yvsGrhBulletins;
    }

    public YvsGrhElementSalaire getElement() {
        return element;
    }

    public void setElement(YvsGrhElementSalaire element) {
        this.element = element;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantite() {
        return (quantite == null) ? 0 : quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Double getTauxSalarial() {
        return (tauxSalarial == null) ? 0 : tauxSalarial;
    }

    public void setTauxSalarial(Double tauxSalarial) {
        this.tauxSalarial = tauxSalarial;
    }

    public Double getTauxPatronal() {
        return (tauxPatronal == null) ? 0 : tauxPatronal;
    }

    public void setTauxPatronal(Double tauxPatronal) {
        this.tauxPatronal = tauxPatronal;
    }

    public double getRetenuSalariale() {
        return retenuSalariale;
    }

    public void setRetenuSalariale(double retenuSalariale) {
        this.retenuSalariale = retenuSalariale;
    }

    public double getMontantPayer() {
        return montantPayer;
    }

    public void setMontantPayer(double montantPayer) {
        this.montantPayer = montantPayer;
    }

    public double getMontantEmployeur() {
        return montantEmployeur;
    }

    public void setMontantEmployeur(Double montantEmployeur) {
        this.montantEmployeur = montantEmployeur;
    }

    public Boolean getNowVisible() {
        return nowVisible != null ? nowVisible : false;
    }

    public void setNowVisible(Boolean nowVisible) {
        this.nowVisible = nowVisible;
    }

    public Double getTrancheMax() {
        return (trancheMax == null) ? 0 : trancheMax;
    }

    public void setTrancheMax(Double trancheMax) {
        this.trancheMax = trancheMax;
    }

    public Double getTrancheMin() {
        return (trancheMin == null) ? 0 : trancheMin;
    }

    public void setTrancheMin(Double trancheMin) {
        this.trancheMin = trancheMin;
    }

    public boolean isLineRecap() {
        return lineRecap;
    }

    public void setLineRecap(boolean lineRecap) {
        this.lineRecap = lineRecap;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsGrhDetailBulletin other = (YvsGrhDetailBulletin) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsGrhDetailBulletin{" + "id=" + id + '}';
    }

}
