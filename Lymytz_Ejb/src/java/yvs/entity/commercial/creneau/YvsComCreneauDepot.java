/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.creneau;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore; 
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_creneau_depot")
@NamedQueries({
    @NamedQuery(name = "YvsComCreneauDepot.findAll", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.depot.agence.societe = :societe"),
    @NamedQuery(name = "YvsComCreneauDepot.findPermanent", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.depot.agence.societe = :societe AND y.permanent = :permanent"),
    @NamedQuery(name = "YvsComCreneauDepot.findById", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCreneauDepot.findByIdsPrecedent", query = "SELECT y.id FROM YvsComCreneauDepot y WHERE y.tranche.heureDebut<:heure AND y.depot=:depot "),
    @NamedQuery(name = "YvsComCreneauDepot.findByIdsByDepot", query = "SELECT y.id FROM YvsComCreneauDepot y WHERE y.depot=:depot "),
    @NamedQuery(name = "YvsComCreneauDepot.findByJour", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauDepot.findByDepot", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.depot = :depot ORDER BY y.tranche.typeJournee, y.tranche.heureDebut, y.permanent"),
    @NamedQuery(name = "YvsComCreneauDepot.findCurrentByDepot", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.depot = :depot AND (y.permanent = true OR (y.jour IS NOT NULL AND y.jour.jour = :jour))"),
    @NamedQuery(name = "YvsComCreneauDepot.findByDepotPermanent", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.depot = :depot AND y.permanent = :permanent AND y.actif = TRUE ORDER BY y.tranche.typeJournee, y.tranche.heureDebut, y.tranche.heureFin"),
    @NamedQuery(name = "YvsComCreneauDepot.findByType", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.tranche = :type"),
    @NamedQuery(name = "YvsComCreneauDepot.findByTypePermanent", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.tranche = :type AND y.permanent = :permanent"),
    @NamedQuery(name = "YvsComCreneauDepot.findByTypeDepot", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.tranche = :type AND y.depot = :depot"),
    @NamedQuery(name = "YvsComCreneauDepot.findByTrancheDepot", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.tranche = :tranche AND y.depot = :depot AND y.actif=true"),
    @NamedQuery(name = "YvsComCreneauDepot.findByTypeDepotJour_", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.tranche = :type AND y.depot = :depot AND y.jour.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauDepot.findByTypeDepotPermanent", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.tranche = :type AND y.depot = :depot AND y.permanent = :permanent"),
    @NamedQuery(name = "YvsComCreneauDepot.findByJourDepot", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.depot = :depot AND y.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauDepot.findByJourDepot_", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.depot = :depot AND y.jour.jour = :jour AND y.actif = TRUE ORDER BY y.tranche.typeJournee, y.tranche.heureDebut, y.tranche.heureFin"),
    @NamedQuery(name = "YvsComCreneauDepot.findByTypeJour", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.tranche = :type AND y.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauDepot.findByTypeJourDepot", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.tranche = :type AND y.depot = :depot AND y.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauDepot.findByActif", query = "SELECT y FROM YvsComCreneauDepot y WHERE y.actif = :actif"),

    @NamedQuery(name = "YvsComCreneauDepot.findTrancheById", query = "SELECT DISTINCT(y.tranche) FROM YvsComCreneauDepot y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCreneauDepot.findIdTrancheById", query = "SELECT DISTINCT y.tranche.id FROM YvsComCreneauDepot y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCreneauDepot.findIdTrancheOrderByDepot", query = "SELECT DISTINCT y.tranche.id FROM YvsComCreneauDepot y WHERE y.depot= :depot AND y.actif =TRUE AND y.tranche.heureDebut>=:heure ORDER BY y.tranche.typeJournee, y.tranche.heureDebut"),
    @NamedQuery(name = "YvsComCreneauDepot.findIdTypeTrancheOrderByDepot", query = "SELECT DISTINCT y.tranche.id FROM YvsComCreneauDepot y WHERE y.depot= :depot AND y.tranche.typeJournee=:typeJ AND y.actif =TRUE AND y.tranche.heureDebut>=:heure ORDER BY y.tranche.typeJournee, y.tranche.heureDebut"),
    @NamedQuery(name = "YvsComCreneauDepot.findTrancheCurrentByDepot", query = "SELECT DISTINCT(y.tranche) FROM YvsComCreneauDepot y LEFT JOIN y.jour j WHERE y.depot = :depot AND (y.permanent = true OR (j.id IS NOT NULL AND j.jour = :jour))"),
    @NamedQuery(name = "YvsComCreneauDepot.findTrancheByJourDepot_", query = "SELECT DISTINCT(y.tranche) FROM YvsComCreneauDepot y WHERE y.depot = :depot AND y.jour.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauDepot.findTrancheByDepotPermanent", query = "SELECT DISTINCT(y.tranche) FROM YvsComCreneauDepot y WHERE y.depot = :depot AND y.permanent = :permanent"),
    @NamedQuery(name = "YvsComCreneauDepot.findTrancheByDepot", query = "SELECT DISTINCT(y.tranche) FROM YvsComCreneauDepot y WHERE y.depot = :depot AND y.actif = TRUE"),
    
    @NamedQuery(name = "YvsComCreneauDepot.countByDepot", query = "SELECT COUNT(y.id) FROM YvsComCreneauDepot y WHERE y.depot = :depot AND y.actif = TRUE")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComCreneauDepot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_creneau_horaire_id_seq", name = "yvs_com_creneau_horaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_creneau_horaire_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "permanent")
    private Boolean permanent = false;

    @JoinColumn(name = "jour", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsJoursOuvres jour;
    @JoinColumn(name = "tranche", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTrancheHoraire tranche;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depot;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private String critere;
    @Transient
    private String critere_;
    @Transient
    private String reference;
    @Transient
    private String semiReference;

    public YvsComCreneauDepot() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComCreneauDepot(Long id) {
        this();
        this.id = id;
    }

    public YvsComCreneauDepot(Long id, YvsJoursOuvres jour, YvsGrhTrancheHoraire tranche, YvsBaseDepots depot) {
        this(id, tranche, depot);
        this.jour = jour;
    }

    public YvsComCreneauDepot(Long id, YvsGrhTrancheHoraire tranche, YvsBaseDepots depot) {
        this(id);
        this.tranche = tranche;
        this.depot = depot;
    }

    public YvsComCreneauDepot(Long id, YvsJoursOuvres jour, YvsGrhTrancheHoraire type) {
        this(id);
        this.jour = jour;
        this.tranche = type;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Boolean getPermanent() {
        return permanent != null ? permanent : false;
    }

    public String getSemiReference() {
        if (getTranche() != null) {
            semiReference = " (Permanent) : " + getTimeToString(getTranche().getHeureDebut()) + " à " + getTimeToString(getTranche().getHeureFin());
            if (getJour() != null ? (getJour().getId() > 0 && !getPermanent()) : false) {
                semiReference = getJour().getJour() + " : " + getTimeToString(getTranche().getHeureDebut()) + " à " + getTimeToString(getTranche().getHeureFin());
            }
        }
        return semiReference != null ? semiReference : "";
    }

    public void setSemiReference(String semiReference) {
        this.semiReference = semiReference;
    }

    public String getReference() {
        if (getTranche() != null && getDepot() != null) {
            reference = getDepot().getDesignation() + " - (Permanent) : " + getTimeToString(getTranche().getHeureDebut()) + " à " + getTimeToString(getTranche().getHeureFin());
            if (!getPermanent() && (getJour() != null ? getJour().getId() > 0 : false)) {
                reference = getDepot().getDesignation() + " - " + getJour().getJour() + " : " + getTimeToString(getTranche().getHeureDebut()) + " à " + getTimeToString(getTranche().getHeureFin());
            }
        }
        return reference != null ? reference : "";
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    @XmlTransient  @JsonIgnore
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

    public String getCritere() {
        critere = tranche != null ? tranche.getTypeJournee() : "";
        return critere;
    }

    public void setCritere(String critere) {
        this.critere = critere;
    }

    public String getCritere_() {
        critere_ = tranche != null ? tranche.getTypeJournee() : "";
        return critere_;
    }

    public void setCritere_(String critere_) {
        this.critere_ = critere_;
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

    public YvsJoursOuvres getJour() {
        return jour;
    }

    public void setJour(YvsJoursOuvres jour) {
        this.jour = jour;
    }

    public YvsGrhTrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(YvsGrhTrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
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
        if (!(object instanceof YvsComCreneauDepot)) {
            return false;
        }
        YvsComCreneauDepot other = (YvsComCreneauDepot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.creneau.YvsComCreneauDepot[ id=" + id + " ]";
    }

    public static String getTimeToString(Date time) {
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        return formater.format(time != null ? time : new Date());
    }

}
