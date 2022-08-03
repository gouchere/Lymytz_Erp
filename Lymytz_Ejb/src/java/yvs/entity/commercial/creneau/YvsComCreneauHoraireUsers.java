/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.creneau;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.entity.users.YvsUsersAgence;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.production.equipe.YvsProdEquipeProduction;
import yvs.entity.users.YvsUsers;

/**
 *
 * @author lymytzfindByUsersDateTimeActif
 */
@Entity
@Table(name = "yvs_com_creneau_horaire_users")
@NamedQueries({
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findAll", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users.agence.societe = :societe ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findAllCrenauPoint", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users.agence.societe = :societe AND y.creneauPoint IS NOT NULL ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByAgence", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users.agence = :agence ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findById", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsers", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersPlan", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.dateTravail <= :date AND y.actif=true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersPermanent", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.permanent = :permanent ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersPermActif", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.permanent = true AND y.actif = true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersPermActifVente", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.permanent = true AND y.actif = true AND y.creneauPoint IS NOT NULL ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findCByUsersOnPV", query = "SELECT COUNT(y) FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauPoint IS NOT NULL AND y.creneauDepot IS NOT NULL AND y.actif=true"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findPointPermanentByUser", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauPoint IS NOT NULL AND y.permanent=TRUE AND y.actif=TRUE AND y.creneauPoint.actif=TRUE AND y.creneauPoint.point.actif=TRUE"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDate", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.dateTravail = :dateTravail"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDates", query = "SELECT y FROM YvsComCreneauHoraireUsers y LEFT JOIN FETCH y.creneauPoint LEFT JOIN FETCH y.creneauPoint.point LEFT JOIN FETCH y.creneauPoint.tranche "
            + "LEFT JOIN FETCH y.creneauDepot LEFT JOIN FETCH y.creneauDepot.tranche LEFT JOIN FETCH y.creneauDepot.depot "
            + "WHERE y.actif=true AND y.users = :users AND (y.permanent=TRUE OR y.dateTravail BETWEEN :dateDebut AND :dateFin) ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDatesVente", query = "SELECT y FROM YvsComCreneauHoraireUsers y JOIN FETCH y.creneauPoint JOIN FETCH y.creneauPoint.point JOIN FETCH y.creneauPoint.tranche "
            + "LEFT JOIN FETCH y.creneauDepot LEFT JOIN FETCH y.creneauDepot.tranche LEFT JOIN FETCH y.creneauDepot.depot "
            + "WHERE y.actif=true AND y.users = :users AND (y.permanent=TRUE OR y.dateTravail BETWEEN :dateDebut AND :dateFin) ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findEquipeByUsersDates", query = "SELECT DISTINCT y.equipe FROM YvsComCreneauHoraireUsers y WHERE y.equipe IS NOT NULL AND y.equipe.actif=true AND y.actif=true AND y.users = :users AND y.dateTravail<=:date ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findUserEquipeActif", query = "SELECT DISTINCT y.users FROM YvsComCreneauHoraireUsers y WHERE y.actif=true AND y.equipe IS NOT NULL AND y.equipe.actif=true AND y.equipe.site.agence.societe=:societe"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findUserEquipeActifAgence", query = "SELECT DISTINCT y.users FROM YvsComCreneauHoraireUsers y WHERE y.actif=true AND y.equipe IS NOT NULL AND y.equipe.actif=true AND y.equipe.site.agence=:agence"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDateTime", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.dateTravail = :dateTravail AND :heureTravail BETWEEN y.creneauDepot.tranche.heureDebut AND y.creneauDepot.tranche.heureFin ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDateTimeActif", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.dateTravail = :dateTravail AND :heureTravail BETWEEN y.creneauDepot.tranche.heureDebut AND y.creneauDepot.tranche.heureFin AND y.actif = true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDateCreneau", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.dateTravail = :dateTravail AND y.creneauDepot = :creneau ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDateCreneau_", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.dateTravail = :dateTravail AND y.creneauPoint = :creneau AND y.type = 'V' ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersPermanentCreneau", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.permanent = :permanent AND y.creneauDepot = :creneau AND y.type = 'S' ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersPermanentCreneau_", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.permanent = :permanent AND y.creneauPoint = :creneau AND y.type = 'V' ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByDateCreneau", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.dateTravail = :dateTravail AND y.creneauDepot = :creneau ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByDateTravail", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.dateTravail = :dateTravail ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByActif", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND  y.actif = :actif ORDER BY y.dateTravail DESC"),

    @NamedQuery(name = "YvsComCreneauHoraireUsers.findTrancheByUsersDates", query = "SELECT DISTINCT y.creneauDepot.tranche FROM YvsComCreneauHoraireUsers y WHERE y.actif=true AND y.users = :users AND (y.dateTravail BETWEEN :debut AND :fin OR y.permanent = TRUE) ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findTrancheByDepotDates", query = "SELECT DISTINCT y.creneauDepot.tranche FROM YvsComCreneauHoraireUsers y WHERE y.actif=true AND y.creneauDepot.depot = :depot AND (y.dateTravail BETWEEN :debut AND :fin OR y.permanent = TRUE) ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findTrancheByDepot", query = "SELECT DISTINCT y.creneauDepot.tranche FROM YvsComCreneauHoraireUsers y WHERE y.actif=true AND y.creneauDepot.depot = :depot ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findTrancheByUsersDepotDates", query = "SELECT DISTINCT y.creneauDepot.tranche FROM YvsComCreneauHoraireUsers y WHERE y.actif=true AND y.creneauDepot.depot = :depot AND y.users = :users AND (y.dateTravail BETWEEN :debut AND :fin OR y.permanent = TRUE) ORDER BY y.dateTravail DESC"),

    @NamedQuery(name = "YvsComCreneauHoraireUsers.findIdPointByUsers", query = "SELECT y.creneauPoint.point.id FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauPoint IS NOT NULL AND (((y.dateTravail=:date AND y.creneauPoint.tranche.heureDebut<y.creneauPoint.tranche.heureFin) OR (y.dateTravail BETWEEN :hier AND :date AND y.creneauPoint.tranche.heureDebut>y.creneauPoint.tranche.heureFin)) OR y.permanent=TRUE) AND y.actif = true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findIdPointByUsers_", query = "SELECT y.creneauPoint.point.id FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauPoint IS NOT NULL AND y.actif = true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findIdPointByUsersAgence_", query = "SELECT y.creneauPoint.point.id FROM YvsComCreneauHoraireUsers y WHERE y.creneauPoint.point.agence = :agence AND y.users = :users AND y.creneauPoint IS NOT NULL AND y.actif = true AND y.creneauPoint.point.actif = true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findPointByUsersAgence", query = "SELECT DISTINCT y.creneauPoint.point FROM YvsComCreneauHoraireUsers y WHERE y.creneauDepot.depot.agence = :agence AND ((y.users = :users AND (((y.dateTravail=:date AND y.creneauDepot.tranche.heureDebut<y.creneauDepot.tranche.heureFin) OR (y.dateTravail BETWEEN :hier AND :date AND y.creneauDepot.tranche.heureDebut>y.creneauDepot.tranche.heureFin)) OR y.permanent=TRUE)) OR (y.creneauDepot.depot.responsable=:responsable)) AND y.actif = true AND y.creneauDepot.depot.actif = true ORDER BY y.dateTravail DESC"),
    
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findIdDepotByUsers", query = "SELECT y.creneauDepot.depot.id FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND (y.dateTravail=:date OR y.permanent=true) AND y.actif = true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findIdsDepotByUsers", query = "SELECT y.creneauDepot.depot.id FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.actif = true AND y.creneauDepot!=null ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findDepotByUsersAgence", query = "SELECT DISTINCT y.creneauDepot.depot FROM YvsComCreneauHoraireUsers y WHERE y.creneauDepot.depot.agence = :agence AND ((y.users = :users AND (((y.dateTravail=:date AND y.creneauDepot.tranche.heureDebut<y.creneauDepot.tranche.heureFin) OR (y.dateTravail BETWEEN :hier AND :date AND y.creneauDepot.tranche.heureDebut>y.creneauDepot.tranche.heureFin)) OR y.permanent=TRUE)) OR (y.creneauDepot.depot.responsable=:responsable)) AND y.actif = true AND y.creneauDepot.depot.actif = true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findDepotByUsers", query = "SELECT DISTINCT y.creneauDepot.depot FROM YvsComCreneauHoraireUsers y WHERE ((y.users = :users AND (((y.dateTravail=:date AND y.creneauDepot.tranche.heureDebut<y.creneauDepot.tranche.heureFin) OR (y.dateTravail BETWEEN :hier AND :date AND y.creneauDepot.tranche.heureDebut>y.creneauDepot.tranche.heureFin)) OR y.permanent=TRUE)) OR (y.creneauDepot.depot.responsable=:responsable)) AND y.actif = true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findDepotByUsersAndPlanActif", query = "SELECT DISTINCT y.creneauDepot.depot FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.actif = true AND y.creneauDepot.depot.actif=true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findIdDepotByUsersAndPlanActif", query = "SELECT DISTINCT y.creneauDepot.depot.id FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.actif = true AND y.creneauDepot.depot.actif=true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findIdCrenoDepotByUsersAndPlanActif", query = "SELECT DISTINCT y.creneauDepot.id FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.actif = true AND y.creneauDepot.depot.actif=true ORDER BY y.dateTravail DESC"),

    @NamedQuery(name = "YvsComCreneauHoraireUsers.findSitesByUsersActif", query = "SELECT DISTINCT y.equipe.site FROM YvsComCreneauHoraireUsers y WHERE y.equipe IS NOT NULL AND y.actif=true AND y.users = :users ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findIdSiteByUserPlanif", query = "SELECT DISTINCT y.equipe.site.id FROM YvsComCreneauHoraireUsers y WHERE y.equipe.site IS NOT NULL AND y.users = :users AND y.actif=true AND (y.dateTravail BETWEEN :debut AND :fin OR y.permanent=true) ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findIdEquipeByUserPlanif", query = "SELECT DISTINCT y.equipe.id FROM YvsComCreneauHoraireUsers y WHERE y.equipe IS NOT NULL AND y.users = :users AND y.actif=true AND (y.dateTravail BETWEEN :debut AND :fin OR y.permanent=true) ORDER BY y.dateTravail DESC"),

    @NamedQuery(name = "YvsComCreneauHoraireUsers.findSiteByEquipeActifByUser", query = "SELECT y.equipe.site FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.actif=true"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findActifByUser", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.actif=true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findActifByUserI", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.actif=true ORDER BY y.dateTravail ASC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUserPlanif", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.actif=true AND (y.dateTravail BETWEEN :debut AND :fin OR y.permanent=true) ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findUsersByCreneauDepotDate", query = "SELECT DISTINCT y.users FROM YvsComCreneauHoraireUsers y WHERE y.creneauDepot = :creneau AND y.actif=true AND y.dateTravail = :date"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findUsersByCreneauDepot", query = "SELECT DISTINCT y.users FROM YvsComCreneauHoraireUsers y WHERE y.creneauDepot = :creneau AND y.actif=true AND (y.dateTravail = :date OR y.permanent = TRUE) AND y.users.actif = TRUE"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findUsersByCreneauDepotPerm", query = "SELECT DISTINCT y.users FROM YvsComCreneauHoraireUsers y WHERE y.creneauDepot = :creneau AND y.actif=true AND y.permanent=TRUE"),

    @NamedQuery(name = "YvsComCreneauHoraireUsers.findUsersByPoint", query = "SELECT DISTINCT y.users FROM YvsComCreneauHoraireUsers y WHERE y.creneauPoint IS NOT NULL AND y.creneauPoint.point=:point AND y.actif=true"),

    @NamedQuery(name = "YvsComCreneauHoraireUsers.findCreneauPointByUsers", query = "SELECT DISTINCT y.creneauPoint FROM YvsComCreneauHoraireUsers y JOIN FETCH y.creneauPoint JOIN FETCH y.creneauPoint.point JOIN FETCH y.creneauPoint.tranche WHERE y.users = :users AND y.creneauPoint.actif=true AND (y.dateTravail=:date OR y.permanent=true) AND y.actif=true"),

    @NamedQuery(name = "YvsComCreneauHoraireUsers.findTrancheDepotByUsersDateDepot", query = "SELECT DISTINCT(y.creneauDepot.tranche) FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauDepot.depot = :depot AND (y.dateTravail = :date OR y.permanent=true)"),

    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersPermActifAgence", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauDepot.depot.agence = :agence AND y.permanent = true AND y.actif = true ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersPermActifDepot", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauDepot.depot = :depot AND y.permanent = true AND y.actif = true AND y.type = 'S' ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersPermActifPoint", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauPoint.point = :point AND y.permanent = true AND y.actif = true AND y.type = 'V' ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDepotPointDateTranche", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauPoint.point = :point AND y.creneauDepot.depot = :depot AND y.creneauDepot.tranche = :tranche AND y.dateTravail = :date ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findOneCompletCreno", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND "
            + "y.creneauPoint = :crenoP AND y.creneauPoint.actif=true AND (y.dateTravail=:date OR y.permanent=true) AND y.actif=true"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findOneCompletCreno2", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND "
            + "y.creneauPoint.point = :point AND y.creneauPoint.actif=true AND (y.dateTravail=:date OR y.permanent=true) AND y.actif=true ORDER BY y.dateTravail DESC, y.id DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDepotDateTranche", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauDepot.depot = :depot AND y.creneauDepot.tranche = :tranche AND y.dateTravail = :date ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDepotPointTrancheActif", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauPoint.point = :point AND y.creneauDepot.depot = :depot AND y.creneauDepot.tranche = :tranche AND y.actif =:actif ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDepotTrancheActif", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauDepot.depot = :depot AND y.creneauDepot.tranche = :tranche AND y.actif =:actif ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDepotPointTranche", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauPoint.point = :point AND y.creneauDepot.depot = :depot AND y.creneauDepot.tranche = :tranche ORDER BY y.dateTravail DESC"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.ifUserHasPlaning", query = "SELECT COUNT(y) FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauDepot= :crenoD AND (((y.dateTravail=:date AND y.creneauDepot.tranche.heureDebut<y.creneauDepot.tranche.heureFin) OR (y.dateTravail BETWEEN :hier AND :date AND y.creneauDepot.tranche.heureDebut>y.creneauDepot.tranche.heureFin)) OR y.permanent=TRUE) AND y.actif=true"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.countMyPlanning", query = "SELECT COUNT(y) FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauDepot.depot=:depot AND y.creneauDepot.tranche=:tranche AND (y.dateTravail=:date OR y.permanent=TRUE) AND y.actif=true"),
    @NamedQuery(name = "YvsComCreneauHoraireUsers.findByUsersDepotTranche", query = "SELECT y FROM YvsComCreneauHoraireUsers y WHERE y.users = :users AND y.creneauDepot.depot = :depot AND y.creneauDepot.tranche = :tranche ORDER BY y.dateTravail DESC")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComCreneauHoraireUsers extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_creneau_horaire_employe_id_seq", name = "yvs_com_creneau_horaire_employe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_creneau_horaire_employe_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_travail")
    @Temporal(TemporalType.DATE)
    private Date dateTravail;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "type")
    private String type;

    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;
    @JoinColumn(name = "creneau_depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComCreneauDepot creneauDepot;
    @JoinColumn(name = "creneau_point", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComCreneauPoint creneauPoint;
    @JoinColumn(name = "equipe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdEquipeProduction equipe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Size(max = 2147483647)
    @Column(name = "execute_trigger")
    private String executeTrigger;

    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private String jour;

    public YvsComCreneauHoraireUsers() {
    }

    public YvsComCreneauHoraireUsers(Long id) {
        this.id = id;
    }

    public YvsComCreneauHoraireUsers(Long id, YvsComCreneauPoint point, YvsComCreneauDepot creneau) {
        this.id = id;
        this.creneauDepot = creneau;
        this.creneauPoint = point;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : "D" : "D";
    }

    public void setType(String type) {
        this.type = type;
    }

    public YvsComCreneauPoint getCreneauPoint() {
        return creneauPoint;
    }

    public void setCreneauPoint(YvsComCreneauPoint creneauPoint) {
        this.creneauPoint = creneauPoint;
    }

    public Boolean getPermanent() {
        return permanent != null ? permanent : false;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    @Override
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateTravail() {
        return dateTravail != null ? dateTravail : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateTravail(Date dateDebut) {
        this.dateTravail = dateDebut;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public YvsComCreneauDepot getCreneauDepot() {
        return creneauDepot;
    }

    public void setCreneauDepot(YvsComCreneauDepot creneauDepot) {
        this.creneauDepot = creneauDepot;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public YvsProdEquipeProduction getEquipe() {
        return equipe;
    }

    public void setEquipe(YvsProdEquipeProduction equipe) {
        this.equipe = equipe;
    }

    public String getJour() {
        Calendar d_ = Calendar.getInstance();
        if (getCreneauPoint() != null ? getCreneauPoint().getTranche() != null : false) {
            dateToCalendar(getTimeStamp(getDateTravail(), getCreneauPoint().getTranche().getHeureDebut()));
        } else {
            if (getCreneauDepot() != null ? getCreneauDepot().getTranche() != null : false) {
                dateToCalendar(getTimeStamp(getDateTravail(), getCreneauDepot().getTranche().getHeureDebut()));
            }
        }
        jour = getDateToStringComplet(d_.getTime());
        return jour;
    }

    public String getTitle() {
        String titre = "";
        if (getUsers()!= null ? getUsers() != null : false) {
            if (getCreneauDepot() != null ? getCreneauDepot().getTranche() != null : false) {
                titre = " " + getUsers().getNomUsers() + " : De " + getTimeToString(getCreneauDepot().getTranche().getHeureDebut()) + " à " + getTimeToString(getCreneauDepot().getTranche().getHeureFin());
            } else {
                if (getCreneauPoint() != null ? getCreneauPoint().getTranche() != null : false) {
                    titre = " " + getUsers().getNomUsers() + " : De " + getTimeToString(getCreneauPoint().getTranche().getHeureDebut()) + " à " + getTimeToString(getCreneauPoint().getTranche().getHeureFin());
                }
            }
        }
        return titre != null ? titre : "";
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
        if (!(object instanceof YvsComCreneauHoraireUsers)) {
            return false;
        }
        YvsComCreneauHoraireUsers other = (YvsComCreneauHoraireUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers[ id=" + id + " ]";
    }

    public static String getDateToStringComplet(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.FRANCE);
        return formater.format(date);
    }

    public static String getTimeToString(Date time) {
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        return formater.format(time);
    }

    public static int ecartOnDate(Date dateDebut, Date dateFin) {
        if (dateDebut != null && dateFin != null) {
            Calendar cal1 = dateToCalendar(dateDebut);
            Calendar cal2 = dateToCalendar(dateFin);
            Calendar cal;

            int heure = 0;
            cal = cal1;
            if (cal.after(cal2)) {
                cal2.add(Calendar.DATE, 1);
            }
            while (cal.before(cal2)) {
                heure++;
                cal.add(Calendar.HOUR, 1);
            }
            if (heure > 24) {
                return 24;
            }

            return heure;
        }
        return 0;
    }

    public static Calendar dateToCalendar(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            return cal;
        }
        return Calendar.getInstance();
    }

    public static Date getTimeStamp(Date date, Date heure) {
        if (date != null && heure != null) {
            Calendar d = dateToCalendar(date);
            Calendar h = dateToCalendar(heure);
            Calendar r = Calendar.getInstance();
            r.clear();
//            r.set(year, month, date, hourOfDay, minute, second);
            r.set(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DATE), h.get(Calendar.HOUR_OF_DAY), h.get(Calendar.MINUTE), h.get(Calendar.SECOND));
            return r.getTime();
        }
        return new Date();
    }
}
