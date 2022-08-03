/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.echellonage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_mensualite")
@NamedQueries({
    @NamedQuery(name = "YvsMutMensualite.findAll", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste.mutuelle.societe = :societe ORDER BY y.dateMensualite"),
    @NamedQuery(name = "YvsMutMensualite.findById", query = "SELECT y FROM YvsMutMensualite y WHERE y.id = :id ORDER BY y.dateMensualite"),
    @NamedQuery(name = "YvsMutMensualite.findByMutualiste", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste= :mutualiste ORDER BY y.dateMensualite"),
    @NamedQuery(name = "YvsMutMensualite.findMensualiteEnCour", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste= :mutualiste AND y.echellonage.etat = :etat AND y.dateMensualite <=:dateFin AND y.etat!=:etatMens ORDER BY y.montant ASC"),
    @NamedQuery(name = "YvsMutMensualite.findMensualiteEnCour_", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste= :mutualiste AND y.echellonage.etat IN :etat AND y.dateMensualite <=:dateFin AND y.etat!=:etatMens ORDER BY y.montant ASC"),
    @NamedQuery(name = "YvsMutMensualite.findIdMensualiteEnCourPeriode_", query = "SELECT y.id FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste= :mutualiste AND y.echellonage.etat IN :etat AND y.dateMensualite <=:dateFin AND y.etat!=:etatMens"),
    @NamedQuery(name = "YvsMutMensualite.findSoeMensualiteFomIds", query = "SELECT SUM(y.montant + y.montantPenalite) FROM YvsMutMensualite y WHERE y.id IN :mensualites"),
    @NamedQuery(name = "YvsMutMensualite.findSoeMensualiteEnCourPeriode_", query = "SELECT SUM(y.montant + y.montantPenalite) FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste= :mutualiste AND y.echellonage.etat IN :etat AND y.dateMensualite <=:dateFin AND y.etat!=:etatMens"),
    @NamedQuery(name = "YvsMutMensualite.findSoeMensualiteEnCour", query = "SELECT SUM(y.montant + y.montantPenalite) FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste= :mutualiste AND y.echellonage.etat = :etat AND y.etat!=:etatMens"),
    @NamedQuery(name = "YvsMutMensualite.findByMutualisteDate", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste= :mutualiste AND y.echellonage.etat = 'R' AND y.dateMensualite BETWEEN  :dateDebut AND :dateFin "),
    @NamedQuery(name = "YvsMutMensualite.findBetweenDate", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste.mutuelle=:mutuelle AND y.etat!=:etat AND y.dateMensualite BETWEEN  :debut AND :fin "),
    @NamedQuery(name = "YvsMutMensualite.findByMutuelle", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste.mutuelle = :mutuelle ORDER BY y.dateMensualite"),
    @NamedQuery(name = "YvsMutMensualite.findByEchellonage", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage = :echellonage ORDER BY y.dateMensualite"),
    @NamedQuery(name = "YvsMutMensualite.findActifByEcheancier", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage = :echellonage AND y.etat != 'S' ORDER BY y.dateMensualite"),
    @NamedQuery(name = "YvsMutMensualite.findByEchellonageNotRegle", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage = :echellonage AND y.etat != 'P' AND y.etat != 'S' ORDER BY y.dateMensualite ASC"),
    @NamedQuery(name = "YvsMutMensualite.findLastByEchellonage", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage = :echellonage AND y.etat NOT IN ('P', 'S') ORDER BY y.dateMensualite DESC"),
    @NamedQuery(name = "YvsMutMensualite.findByDateMensualite", query = "SELECT y FROM YvsMutMensualite y WHERE y.dateMensualite = :dateMensualite ORDER BY y.dateMensualite"),
    @NamedQuery(name = "YvsMutMensualite.findMensualiteEnCours", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste = :mutualiste AND y.dateMensualite>=:date ORDER BY y.dateMensualite DESC"),
    @NamedQuery(name = "YvsMutMensualite.findDistinctMensualiteEnCours", query = "SELECT DISTINCT y.montant, y.echellonage FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste = :mutualiste AND y.dateMensualite>=:date "
            + " AND y.etat IN :etats"),
    @NamedQuery(name = "YvsMutMensualite.findEnCours", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste = :mutualiste AND y.dateMensualite>=:date AND y.etat NOT IN ('P', 'S') ORDER BY y.dateMensualite DESC"),
    @NamedQuery(name = "YvsMutMensualite.findMontantEnCours", query = "SELECT SUM(y.montant) FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste = :mutualiste AND y.dateMensualite>=:date AND y.etat NOT IN ('P', 'S')"),
    @NamedQuery(name = "YvsMutMensualite.findMontantDates", query = "SELECT SUM(y.montant) FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste = :mutualiste AND y.dateMensualite BETWEEN :dateDebut AND :dateFin AND y.etat NOT IN ('P', 'S')"),
    @NamedQuery(name = "YvsMutMensualite.findInteretDates", query = "SELECT SUM(y.interet) FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste = :mutualiste AND y.dateMensualite BETWEEN :dateDebut AND :dateFin AND y.etat NOT IN ('P', 'S')"),
    @NamedQuery(name = "YvsMutMensualite.findAmortissementDates", query = "SELECT SUM(y.amortissement) FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste = :mutualiste AND y.dateMensualite BETWEEN :dateDebut AND :dateFin AND y.etat NOT IN ('P', 'S')"),
    @NamedQuery(name = "YvsMutMensualite.findPenaliteDates", query = "SELECT SUM(y.amortissement) FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste = :mutualiste AND y.dateMensualite BETWEEN :dateDebut AND :dateFin AND y.etat NOT IN ('P', 'S')"),
    @NamedQuery(name = "YvsMutMensualite.findEnRetard", query = "SELECT y FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste = :mutualiste AND y.dateMensualite<:date AND y.etat NOT IN ('P', 'S') ORDER BY y.dateMensualite DESC"),
    @NamedQuery(name = "YvsMutMensualite.findMontantEnRetard", query = "SELECT SUM(y.montant) FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste = :mutualiste AND y.dateMensualite<:date AND y.etat NOT IN ('P', 'S')"),
    @NamedQuery(name = "YvsMutMensualite.findSoldeCredit", query = "SELECT SUM(y.montant) FROM YvsMutMensualite y WHERE y.echellonage.credit.compte.mutualiste = :mutualiste AND y.echellonage.credit.etat=:etat AND y.echellonage.etat=:etatEch"),
    @NamedQuery(name = "YvsMutMensualite.findSoldeInteret", query = "SELECT SUM(y.interet) FROM YvsMutMensualite y WHERE y.echellonage.credit = :credit AND y.echellonage.credit.etat=:etat AND y.echellonage.etat=:etatEch AND y.echellonage.dateEchellonage BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsMutMensualite.findSoldeInteret_", query = "SELECT SUM(y.interet) FROM YvsMutMensualite y WHERE y.echellonage.credit.etat=:etat AND y.echellonage.etat=:etatEch AND (y.echellonage.dateEchellonage BETWEEN :dateDebut AND :dateFin) AND y.montant = :montantVerse"),
    @NamedQuery(name = "YvsMutMensualite.findByMontant", query = "SELECT y FROM YvsMutMensualite y WHERE y.montant = :montant ORDER BY y.dateMensualite")})
public class YvsMutMensualite implements Serializable, Comparator<YvsMutMensualite> {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_mensualite_id_seq", name = "yvs_mut_mensualite_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_mensualite_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_mensualite")
    @Temporal(TemporalType.DATE)
    private Date dateMensualite;
    @Column(name = "date_reglement")
    @Temporal(TemporalType.DATE)
    private Date dateReglement;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private double montant;
    @Column(name = "montant_penalite")
    private double montantPenalite;
    @Size(max = 2147483647)
    @Column(name = "regle_par")
    private String reglePar;
    @Column(name = "interet")
    private Double interet;
    @Column(name = "amortissement")
    private Double amortissement;
    @Column(name = "etat")
    private String etat;
    @Column(name = "commentaire")
    private String commentaire;
    @JoinColumn(name = "echellonage", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutEchellonage echellonage;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "mensualite")
    private List<YvsMutReglementMensualite> reglements;

    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean marquer;
    @Transient
    private double montantReste;
    @Transient
    private double montantReel;
    @Transient
    private double montantVerse;

    public YvsMutMensualite() {
        reglements = new ArrayList<>();
    }

    public YvsMutMensualite(Long id) {
        this();
        this.id = id;
    }

    public YvsMutMensualite(Long id, YvsMutMensualite y) {
        this(id);
        this.dateMensualite = y.dateMensualite;
        this.montant = y.montant;
        this.montantVerse = y.montantVerse;
        this.montantPenalite = y.montantPenalite;
        this.reglePar = y.reglePar;
        this.echellonage = y.echellonage;
        this.interet = y.interet;
        this.amortissement = y.amortissement;
        this.etat = y.etat;
        this.selectActif = y.selectActif;
        this.new_ = y.new_;
        this.marquer = y.marquer;
        this.montantReste = y.montantReste;
        this.montantReel = y.montantReel;
        this.dateSave = y.dateSave;
        this.dateUpdate = y.dateUpdate;
        this.commentaire = y.commentaire;
        if (y.reglements != null) {
            this.reglements.addAll(y.reglements);
        }
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

    public double getMontantReel() {
        montantReel = getMontant() + getMontantPenalite();
        return montantReel;
    }

    public void setMontantReel(double montantReel) {
        this.montantReel = montantReel;
    }

    public double getMontantPenalite() {
        return montantPenalite;
    }

    public void setMontantPenalite(double montantPenalite) {
        this.montantPenalite = montantPenalite;
    }

    public boolean isMarquer() {
        return marquer;
    }

    public void setMarquer(boolean marquer) {
        this.marquer = marquer;
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

    public double getMontantReste() {
        montantReste = getMontantReel() - getMontantVerse();
        return montantReste > 0 ? montantReste : 0;
    }

    public void setMontantReste(double montantReste) {
        this.montantReste = montantReste;
    }

    public Date getDateMensualite() {
        return dateMensualite;
    }

    public void setDateMensualite(Date dateMensualite) {
        this.dateMensualite = dateMensualite;
    }

    public YvsMutEchellonage getEchellonage() {
        return echellonage;
    }

    public void setEchellonage(YvsMutEchellonage echellonage) {
        this.echellonage = echellonage;
    }

    public Double getAmortissement() {
        return amortissement != null ? amortissement : 0;
    }

    public void setAmortissement(Double amortissement) {
        this.amortissement = amortissement;
    }

    public Double getInteret() {
        return interet != null ? interet : 0;
    }

    public void setInteret(Double interet) {
        this.interet = interet;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public List<YvsMutReglementMensualite> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsMutReglementMensualite> reglements) {
        this.reglements = reglements;
    }

    public double getMontant() {
        return getAmortissement() + (echellonage != null ? (!echellonage.getCreditRetainsInteret() ? getInteret() : 0) : 0);
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getReglePar() {
        return reglePar;
    }

    public void setReglePar(String reglePar) {
        this.reglePar = reglePar;
    }

    public double getMontantVerse() {
        montantVerse = 0;
        for (YvsMutReglementMensualite r : reglements) {
            if (r.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                montantVerse += r.getMontant();
            }
        }
        return montantVerse;
    }

    public void setMontantVerse(double montantVerse) {
        this.montantVerse = montantVerse;
    }

    public String getCommentaire() {
        return commentaire != null ? commentaire : "";
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(Date dateReglement) {
        this.dateReglement = dateReglement;
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
        if (!(object instanceof YvsMutMensualite)) {
            return false;
        }
        YvsMutMensualite other = (YvsMutMensualite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.remboursement.YvsMutMensualite[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsMutMensualite o1, YvsMutMensualite o2) {
        if (o1.getDateMensualite() == null) {
            return -1;
        } else if (o2.getDateMensualite() == null) {
            return 1;
        } else if (o1.getDateMensualite().after(o2.getDateMensualite())) {
            return 1;
        } else if (o1.getDateMensualite().before(o2.getDateMensualite())) {
            return -1;
        } else {
            return 0;
        }
    }

}
