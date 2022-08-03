/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_planning_employe")
@NamedQueries({
    @NamedQuery(name = "YvsGrhPlanningEmploye.findAll", query = "SELECT y FROM YvsGrhPlanningEmploye y"),
    @NamedQuery(name = "YvsGrhPlanningEmploye.findByEmploye", query = "SELECT y FROM YvsGrhPlanningEmploye y WHERE y.employe = :employe"),
    @NamedQuery(name = "YvsGrhPlanningEmploye.findByOneEmploye", query = "SELECT y FROM YvsGrhPlanningEmploye y WHERE y.employe = :employe AND y.dateDebut=:date"),
    @NamedQuery(name = "YvsGrhPlanningEmploye.findOnePlan", query = "SELECT y FROM YvsGrhPlanningEmploye y WHERE y.employe = :employe AND y.tranche=:tranche AND y.dateDebut=:date"),
    @NamedQuery(name = "YvsGrhPlanningEmploye.findByEmployeDay", query = "SELECT y FROM YvsGrhPlanningEmploye y WHERE y.employe = :employe AND (:date BETWEEN y.dateDebut AND y.dateFin) ORDER BY y.dateFin ASC"),
    @NamedQuery(name = "YvsGrhPlanningEmploye.findByDate", query = "SELECT y FROM YvsGrhPlanningEmploye y WHERE y.dateDebut = :date AND y.employe.agence=:agence ORDER BY y.employe.nom"),
    @NamedQuery(name = "YvsGrhPlanningEmploye.findByDates", query = "SELECT y FROM YvsGrhPlanningEmploye y WHERE y.dateDebut BETWEEN :d1 AND :d2 ORDER BY y.employe.nom, y.dateDebut"),
    @NamedQuery(name = "YvsGrhPlanningEmploye.findEmployeByDates", query = "SELECT y FROM YvsGrhPlanningEmploye y JOIN FETCH y.employe WHERE y.employe.agence.societe=:societe AND :date BETWEEN y.dateDebut AND y.dateFin ORDER BY y.employe.nom, y.dateDebut"),
    @NamedQuery(name = "YvsGrhPlanningEmploye.findEmployeByDate", query = "SELECT y FROM YvsGrhPlanningEmploye y JOIN FETCH y.employe WHERE y.employe.agence.societe=:societe AND y.dateDebut=:date ORDER BY y.employe.nom, y.dateDebut"),
    @NamedQuery(name = "YvsGrhPlanningEmploye.findByDatesAgence", query = "SELECT y FROM YvsGrhPlanningEmploye y WHERE y.dateDebut BETWEEN :d1 AND :d2 AND y.employe.agence =:agence ORDER BY y.employe.nom, y.dateDebut"),
    @NamedQuery(name = "YvsGrhPlanningEmploye.findByDatesANDEmployes", query = "SELECT y FROM YvsGrhPlanningEmploye y WHERE y.employe.id IN :liste AND (y.dateDebut  BETWEEN :d1 AND :d2) ORDER BY y.dateDebut"),

    @NamedQuery(name = "YvsGrhPlanningEmploye.findByDateAndEmploye", query = "SELECT y FROM YvsGrhPlanningEmploye y WHERE y.employe = :employe AND :date BETWEEN y.dateDebut AND y.dateFin ORDER BY y.dateDebut, y.tranche.heureDebut")
})
public class YvsGrhPlanningEmploye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    @SequenceGenerator(sequenceName = "yvs_grh_planning_employe_id_seq", name = "yvs_grh_planning_employe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_planning_employe_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Temporal(javax.persistence.TemporalType.TIME)
    @Column(name = "duree_pause")
    private Date dureePause;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "repos")
    private Boolean repos;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tranche", referencedColumnName = "id")
    private YvsGrhTrancheHoraire tranche;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    
    @Transient
    private boolean supplementaire;
    @Transient
    private boolean chevauche;
    @Transient
    private boolean valide;

    public YvsGrhPlanningEmploye() {
        tranche = new YvsGrhTrancheHoraire();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhPlanningEmploye(Long id) {
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

    public void setTranche(YvsGrhTrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public YvsGrhTrancheHoraire getTranche() {
        return tranche;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDureePause() {
        return dureePause;
    }

    public void setDureePause(Date dureePause) {
        this.dureePause = dureePause;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public Date getHeureDebut() {
        return getTranche().getHeureDebut();
    }

    public void setHeureDebut(Date heureDebut) {
        this.getTranche().setHeureDebut(heureDebut);
    }

    public Date getHeureFin() {
        return getTranche().getHeureFin();
    }

    public void setHeureFin(Date heureFin) {
        this.getTranche().setHeureFin(heureFin);
    }

    public boolean isSupplementaire() {
        return supplementaire;
    }

    public void setSupplementaire(boolean supplementaire) {
        this.supplementaire = supplementaire;
    }

    public boolean isChevauche() {
        return chevauche;
    }

    public void setChevauche(boolean chevauche) {
        this.chevauche = chevauche;
    }

    public boolean isValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public Boolean getRepos() {
        return repos != null ? repos : false;
    }

    public void setRepos(Boolean repos) {
        this.repos = repos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final YvsGrhPlanningEmploye other = (YvsGrhPlanningEmploye) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsGrhPlanningEmploye{" + "id=" + id + '}';
    }

}
