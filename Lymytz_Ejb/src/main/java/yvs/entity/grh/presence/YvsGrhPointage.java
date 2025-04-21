/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.presence;

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
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_pointage")
@NamedQueries({
    @NamedQuery(name = "YvsGrhPointage.findAll", query = "SELECT y FROM YvsGrhPointage y"),
    @NamedQuery(name = "YvsGrhPointage.findById", query = "SELECT y FROM YvsGrhPointage y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhPointage.findByFiche", query = "SELECT y FROM YvsGrhPointage y WHERE y.presence = :fiche ORDER BY y.heureEntree ASC"),
    @NamedQuery(name = "YvsGrhPointage.findByHeureEntree", query = "SELECT y FROM YvsGrhPointage y WHERE y.heureEntree = :heureEntree"),
    @NamedQuery(name = "YvsGrhPointage.findByMotif", query = "SELECT y FROM YvsGrhPointage y WHERE y.motifSortie = :motif AND y.presence=:fiche"),
    @NamedQuery(name = "YvsGrhPointage.findByDescriptionCommission", query = "SELECT y FROM YvsGrhPointage y WHERE y.descriptionCommission = :descriptionCommission"),
    @NamedQuery(name = "YvsGrhPointage.findByValider", query = "SELECT y FROM YvsGrhPointage y WHERE y.valider = :valider"),
    @NamedQuery(name = "YvsGrhPointage.findByCompensationHeure", query = "SELECT y FROM YvsGrhPointage y WHERE y.compensationHeure = :compensationHeure"),
    @NamedQuery(name = "YvsGrhPointage.findByHeureSupplementaire", query = "SELECT y FROM YvsGrhPointage y WHERE y.heureSupplementaire = :heureSupplementaire"),
    @NamedQuery(name = "YvsGrhPointage.findPointageEmps", query = "SELECT y FROM YvsGrhPointage y WHERE y.presence.employe=:employe AND (y.presence.dateDebut=:date) ORDER BY y.heureEntree ASC"),
    @NamedQuery(name = "YvsGrhPointage.findTrue", query = "SELECT y FROM YvsGrhPointage y WHERE y.presence=:fiche AND (:date BETWEEN y.heureEntree AND y.heureSortie)"),
    @NamedQuery(name = "YvsGrhPointage.findLastPointage", query = "SELECT y FROM YvsGrhPointage y WHERE y.presence=:fiche ORDER BY y.heureEntree DESC"),
    @NamedQuery(name = "YvsGrhPointage.findLastPointageAGSO", query = "SELECT DISTINCT y.presence FROM YvsGrhPointage y WHERE (y.presence.dateDebut=:date OR y.presence.dateFin=:date) AND y.heureEntree IS NOT NULL AND y.heureSortie IS NULL AND y.presence.employe.agence=:agence ORDER BY y.heurePointage DESC"),
    @NamedQuery(name = "YvsGrhPointage.findLastPointageAGEN", query = "SELECT DISTINCT y.presence FROM YvsGrhPointage y WHERE (y.presence.dateDebut=:date OR y.presence.dateFin=:date) AND (y.heureSortie IS NOT NULL) AND y.presence.employe.agence=:agence ORDER BY y.heurePointage DESC"),
    @NamedQuery(name = "YvsGrhPointage.findLastPointageSCTSO", query = "SELECT DISTINCT y.presence FROM YvsGrhPointage y WHERE (y.presence.dateDebut=:date OR y.presence.dateFin=:date) AND y.heureEntree IS NOT NULL AND y.heureSortie IS NULL AND y.presence.employe.agence.societe=:agence ORDER BY y.heurePointage DESC"),
    @NamedQuery(name = "YvsGrhPointage.findLastPointageSCTEN", query = "SELECT DISTINCT y.presence FROM YvsGrhPointage y WHERE (y.presence.dateDebut=:date OR y.presence.dateFin=:date) AND (y.heureSortie IS NOT NULL) AND y.presence.employe.agence.societe=:agence ORDER BY y.heurePointage DESC"),
    @NamedQuery(name = "YvsGrhPointage.findByHoraireNormale", query = "SELECT y FROM YvsGrhPointage y WHERE y.horaireNormale = :horaireNormale"),
    @NamedQuery(name = "YvsGrhPointage.findByDureeCommission", query = "SELECT y FROM YvsGrhPointage y WHERE y.dureeCommission = :dureeCommission"),
    @NamedQuery(name = "YvsGrhPointage.findByPointageAutomatique", query = "SELECT y FROM YvsGrhPointage y WHERE y.pointageAutomatique = :pointageAutomatique"),
    @NamedQuery(name = "YvsGrhPointage.findByHeureSortie", query = "SELECT y FROM YvsGrhPointage y WHERE y.heureSortie = :heureSortie"),

    @NamedQuery(name = "YvsGrhPointage.findLastEntreeByPresence", query = "SELECT y FROM YvsGrhPointage y WHERE y.presence = :presence AND y.heureEntree IS NOT NULL ORDER BY y.heureEntree DESC"),
    @NamedQuery(name = "YvsGrhPointage.findByHeureEmploye", query = "SELECT y FROM YvsGrhPointage y WHERE y.presence.employe = :employe AND ((y.heureEntree IS NOT NULL AND y.heureEntree = :heure) OR (y.heureSortie IS NOT NULL AND y.heureSortie = :heure))")})
public class YvsGrhPointage implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_presence_id_seq", name = "yvs_presence_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_presence_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "heure_entree")
    @Temporal(TemporalType.TIMESTAMP)
    private Date heureEntree;
    @Size(max = 50)
    @Column(name = "motif_sortie")
    private String motifSortie = "ABSENT";
    @Size(max = 255)
    @Column(name = "description_commission")
    private String descriptionCommission;
    @Column(name = "valider")
    private Boolean valider;
    @Column(name = "compensation_heure")
    private Boolean compensationHeure;
    @Column(name = "heure_supplementaire")
    private Boolean heureSupplementaire;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "horaire_normale")
    private Boolean horaireNormale;
    @Column(name = "duree_commission")
    @Temporal(TemporalType.TIME)
    private Date dureeCommission;
    @Column(name = "pointage_automatique")
    private Boolean pointageAutomatique;
    @Column(name = "system_in")
    private Boolean systemIn;
    @Column(name = "system_out")
    private Boolean systemOut;
    @Column(name = "heure_sortie")
    @Temporal(TemporalType.TIMESTAMP)
    private Date heureSortie;
    @Column(name = "commission")
    private Boolean commission;
    @Column(name = "heure_pointage")
    @Temporal(TemporalType.TIMESTAMP)
    private Date heurePointage;
    @Column(name = "date_save_entree")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateSaveEntree;
    @Column(name = "date_save_sortie")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateSaveSortie;
    @JoinColumn(name = "operateur_sortie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers operateurSortie;
    @JoinColumn(name = "operateur_entree", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers operateurEntree;
    @JoinColumn(name = "pointeuse_in", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsPointeuse pointeuseIn;
    @JoinColumn(name = "pointeuse_out", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsPointeuse pointeuseOut;
    @JoinColumn(name = "presence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPresence presence;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private int action;
    @Transient
    private boolean upload;

    public YvsGrhPointage() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhPointage(Long id) {
        this();
        this.id = id;
    }

    public YvsGrhPointage(YvsGrhPresence presence, Date heureEntree, int action) {
        this.heureEntree = heureEntree;
        this.presence = presence;
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getHeureEntree() {
        return heureEntree;
    }

    public void setHeureEntree(Date heureEntree) {
        this.heureEntree = heureEntree;
    }

    public String getMotifSortie() {
        return motifSortie;
    }

    public void setMotifSortie(String motifSortie) {
        this.motifSortie = motifSortie;
    }

    public String getDescriptionCommission() {
        return descriptionCommission;
    }

    public void setDescriptionCommission(String descriptionCommission) {
        this.descriptionCommission = descriptionCommission;
    }

    public Boolean getValider() {
        return valider;
    }

    public void setValider(Boolean valider) {
        this.valider = valider;
    }

    public Boolean getCompensationHeure() {
        return compensationHeure;
    }

    public void setCompensationHeure(Boolean compensationHeure) {
        this.compensationHeure = compensationHeure;
    }

    public Boolean getHeureSupplementaire() {
        return heureSupplementaire != null ? heureSupplementaire : false;
    }

    public void setHeureSupplementaire(Boolean heureSupplementaire) {
        this.heureSupplementaire = heureSupplementaire;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getHoraireNormale() {
        return horaireNormale != null ? horaireNormale : false;
    }

    public void setHoraireNormale(Boolean horaireNormale) {
        this.horaireNormale = horaireNormale;
    }

    public Date getDureeCommission() {
        return dureeCommission;
    }

    public void setDureeCommission(Date dureeCommission) {
        this.dureeCommission = dureeCommission;
    }

    public Boolean getPointageAutomatique() {
        return pointageAutomatique != null ? pointageAutomatique : false;
    }

    public void setPointageAutomatique(Boolean pointageAutomatique) {
        this.pointageAutomatique = pointageAutomatique;
    }

    public Date getHeureSortie() {
        return heureSortie;
    }

    public void setHeureSortie(Date heureSortie) {
        this.heureSortie = heureSortie;
    }

    public YvsUsers getOperateurSortie() {
        return operateurSortie;
    }

    public void setOperateurSortie(YvsUsers operateurSortie) {
        this.operateurSortie = operateurSortie;
    }

    public YvsUsers getOperateurEntree() {
        return operateurEntree;
    }

    public void setOperateurEntree(YvsUsers operateurEntree) {
        this.operateurEntree = operateurEntree;
    }

    public YvsPointeuse getPointeuseIn() {
        return pointeuseIn;
    }

    public void setPointeuseIn(YvsPointeuse pointeuseIn) {
        this.pointeuseIn = pointeuseIn;
    }

    public YvsPointeuse getPointeuseOut() {
        return pointeuseOut;
    }

    public void setPointeuseOut(YvsPointeuse pointeuseOut) {
        this.pointeuseOut = pointeuseOut;
    }

    public YvsGrhPresence getPresence() {
        return presence;
    }

    public void setPresence(YvsGrhPresence presence) {
        this.presence = presence;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Date getDateSaveEntree() {
        return dateSaveEntree;
    }

    public void setDateSaveEntree(Date dateSaveEntree) {
        this.dateSaveEntree = dateSaveEntree;
    }

    public Date getDateSaveSortie() {
        return dateSaveSortie;
    }

    public void setDateSaveSortie(Date dateSaveSortie) {
        this.dateSaveSortie = dateSaveSortie;
    }

    public Boolean getSystemIn() {
        return systemIn != null ? systemIn : false;
    }

    public void setSystemIn(Boolean systemIn) {
        this.systemIn = systemIn;
    }

    public Boolean getSystemOut() {
        return systemOut != null ? systemOut : false;
    }

    public void setSystemOut(Boolean systemOut) {
        this.systemOut = systemOut;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public Date getHeurePointage() {
        return heurePointage;
    }

    public void setHeurePointage(Date heurePointage) {
        this.heurePointage = heurePointage;
    }

    public Boolean getCommission() {
        return commission;
    }

    public void setCommission(Boolean commission) {
        this.commission = commission;
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
        if (!(object instanceof YvsGrhPointage)) {
            return false;
        }
        YvsGrhPointage other = (YvsGrhPointage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.presence.YvsGrhPointage[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        YvsGrhPointage y = (YvsGrhPointage) o;
        if (presence != null ? y.presence != null : false) {
            if (y.presence.getDateDebut().equals(presence.getDateDebut())) {
                if (y.heureEntree.equals(heureEntree)) {
                    return y.id.compareTo(id);
                } else {
                    return y.heureEntree.compareTo(heureEntree);
                }
            } else {
                return y.presence.getDateDebut().compareTo(presence.getDateDebut());
            }
        } else {
            if (y.heureEntree.equals(heureEntree)) {
                return y.id.compareTo(id);
            } else {
                return y.heureEntree.compareTo(heureEntree);
            }
        }
    }

}
