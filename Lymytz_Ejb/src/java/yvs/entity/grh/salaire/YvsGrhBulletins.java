/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

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
import yvs.dao.YvsEntity;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_bulletins")
@NamedQueries({
    @NamedQuery(name = "YvsGrhBulletins.findAll", query = "SELECT y FROM YvsGrhBulletins y WHERE y.societe=:societe ORDER BY y.dateFin DESC,y.contrat.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhBulletins.findAllAccessile", query = "SELECT y FROM YvsGrhBulletins y WHERE y.societe=:societe AND y.contrat.id NOT IN :idsRestreint ORDER BY y.dateFin DESC,y.contrat.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhBulletins.findById", query = "SELECT y FROM YvsGrhBulletins y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhBulletins.findByIds", query = "SELECT y FROM YvsGrhBulletins y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsGrhBulletins.findByHead", query = "SELECT y FROM YvsGrhBulletins y WHERE y.entete= :header AND y.societe=:societe ORDER BY y.contrat.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhBulletins.findOneByHead", query = "SELECT y FROM YvsGrhBulletins y WHERE y.entete= :header AND y.contrat=:contrat AND ( y.statut=:statut OR y.statut=:statut1)"),
    @NamedQuery(name = "YvsGrhBulletins.findOneByHead_", query = "SELECT y FROM YvsGrhBulletins y WHERE y.entete= :header AND y.contrat=:contrat"),
    //récupère les bulletins validé ou confirmé ou payé d'un employé
    @NamedQuery(name = "YvsGrhBulletins.findBulEmploye", query = "SELECT y FROM YvsGrhBulletins y WHERE y.entete= :header AND y.contrat=:contrat AND (y.statut='V' OR y.statut='C' OR y.statut='P') ORDER BY y.contrat.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhBulletins.findByContratActuel", query = "SELECT y FROM YvsGrhBulletins y WHERE y.contrat = :contrat AND :dateJour BETWEEN y.dateDebut AND y.dateFin"),
    @NamedQuery(name = "YvsGrhBulletins.findByContratEmploye", query = "SELECT y FROM YvsGrhBulletins y WHERE y.contrat = :contrat ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsGrhBulletins.findByDateDebut", query = "SELECT y FROM YvsGrhBulletins y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsGrhBulletins.findByDateFin", query = "SELECT y FROM YvsGrhBulletins y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsGrhBulletins.findNextBulletin", query = "SELECT y FROM YvsGrhBulletins y WHERE y.entete = :header AND y.contrat.actif=true ORDER BY y.contrat.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhBulletins.findCountBullPeriode", query = "SELECT COUNT(y) FROM YvsGrhBulletins y WHERE y.entete = :header AND y.contrat.actif=true "),
    @NamedQuery(name = "YvsGrhBulletins.findByDateCalcul", query = "SELECT y FROM YvsGrhBulletins y WHERE y.dateCalcul = :dateCalcul"),
    @NamedQuery(name = "YvsGrhBulletins.countByPeriode", query = "SELECT COUNT(y) FROM YvsGrhBulletins y WHERE y.entete = :head AND y.contrat=:contrat"),
    @NamedQuery(name = "YvsGrhBulletins.findByPeriode", query = "SELECT y FROM YvsGrhBulletins y WHERE y.entete = :head AND y.contrat=:contrat"),
    @NamedQuery(name = "YvsGrhBulletins.findByPeriode_", query = "SELECT y FROM YvsGrhBulletins y WHERE y.entete = :head AND y.contrat.employe=:contrat"),
    @NamedQuery(name = "YvsGrhBulletins.findLastBp", query = "SELECT y FROM YvsGrhBulletins y WHERE y.numMois=:mois AND y.contrat=:contrat ORDER BY y.id DESC"),

    @NamedQuery(name = "YvsGrhBulletins.countBySocieteDates", query = "SELECT COUNT(y) FROM YvsGrhBulletins y WHERE y.contrat.employe.agence.societe = :societe AND ((y.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.entete.finMois BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsGrhBulletins.countByAgenceDates", query = "SELECT COUNT(y) FROM YvsGrhBulletins y WHERE y.contrat.employe.agence = :agence AND ((y.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.entete.finMois BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsGrhBulletins.countByEmployeDates", query = "SELECT COUNT(y) FROM YvsGrhBulletins y WHERE y.contrat.employe = :employe AND ((y.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.entete.finMois BETWEEN :dateDebut AND :dateFin))"),
    @NamedQuery(name = "YvsGrhBulletins.countByContratDates", query = "SELECT COUNT(y) FROM YvsGrhBulletins y WHERE y.contrat=:contrat AND ((y.entete.debutMois BETWEEN :dateDebut AND :dateFin) AND (y.entete.finMois BETWEEN :dateDebut AND :dateFin))")})
public class YvsGrhBulletins extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_bulletins_id_seq", name = "yvs_grh_bulletins_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_bulletins_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "statut")
    private Character statut; //B=Brouillon, C=Confirmé, P=Payé
    @Column(name = "numero")
    private Short numero;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "date_calcul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCalcul;
    @Column(name = "date_validation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidation;
    @Column(name = "num_mois")
    private String numMois;
    @Column(name = "ref_bulletin")
    private String refBulletin;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @OneToMany(mappedBy = "yvsGrhBulletins", fetch = FetchType.LAZY)
    private List<YvsGrhDetailBulletin> listDetails;
    @JoinColumn(name = "contrat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhContratEmps contrat;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "auteur_validation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers auteurValidation;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "entete", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhOrdreCalculSalaire entete;

    @Transient
    private List<YvsGrhDetailPrelevementEmps> listeRetenue;
    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private double netApayer;
    @Transient
    private boolean comptabilised;

    public YvsGrhBulletins() {
        listDetails = new ArrayList<>();
    }

    public YvsGrhBulletins(Long id) {
        this();
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public double getNetApayer() {
        netApayer = 0;
        for (YvsGrhDetailBulletin d : getListDetails()) {
            if (d.getElement() != null ? d.getElement().getId() > 0 : false) {
                if (d.getNowVisible() && d.getElement().getVisibleBulletin() && (d.getMontantEmployeur() != 0 || d.getMontantPayer() != 0 || d.getRetenuSalariale() != 0)) {
                    netApayer += d.getMontantPayer() - d.getRetenuSalariale();
                }
            }
        }
        return netApayer;
    }

    public void setNetApayer(double netApayer) {
        this.netApayer = netApayer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDateCalcul() {
        return dateCalcul;
    }

    public void setDateCalcul(Date dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    public List<YvsGrhDetailBulletin> getListDetails() {
        return listDetails;
    }

    public void setListDetails(List<YvsGrhDetailBulletin> listDetails) {
        this.listDetails = listDetails;
    }

    public YvsGrhContratEmps getContrat() {
        return contrat;
    }

    public void setContrat(YvsGrhContratEmps contrat) {
        this.contrat = contrat;
    }

    public String getRefBulletin() {
        return refBulletin;
    }

    public void setRefBulletin(String refBulletin) {
        this.refBulletin = refBulletin;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getNumMois() {
        return numMois;
    }

    public void setNumMois(String numMois) {
        this.numMois = numMois;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    public YvsUsers getAuteurValidation() {
        return auteurValidation;
    }

    public void setAuteurValidation(YvsUsers auteurValidation) {
        this.auteurValidation = auteurValidation;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhOrdreCalculSalaire getEntete() {
        return entete;
    }

    public void setEntete(YvsGrhOrdreCalculSalaire entete) {
        this.entete = entete;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Character getStatut() {
        return statut;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public Short getNumero() {
        return numero;
    }

    public void setNumero(Short numero) {
        this.numero = numero;
    }

    public List<YvsGrhDetailPrelevementEmps> getListeRetenue() {
        return listeRetenue;
    }

    public void setListeRetenue(List<YvsGrhDetailPrelevementEmps> listeRetenue) {
        this.listeRetenue = listeRetenue;
    }

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
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
        if (!(object instanceof YvsGrhBulletins)) {
            return false;
        }
        YvsGrhBulletins other = (YvsGrhBulletins) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhBulletins[ id=" + id + " ]";
    }

}
