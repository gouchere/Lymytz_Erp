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
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_creneau_point")
@NamedQueries({
    @NamedQuery(name = "YvsComCreneauPoint.findAll", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.point.agence.societe = :societe"),
    @NamedQuery(name = "YvsComCreneauPoint.findPermanent", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.point.agence.societe = :societe AND y.permanent = :permanent"),
    @NamedQuery(name = "YvsComCreneauPoint.findById", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCreneauPoint.findByJour", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauPoint.findByDepot", query = "SELECT y FROM YvsComCreneauPoint y JOIN FETCH y.tranche WHERE y.point = :point ORDER BY y.tranche.typeJournee, y.tranche.heureDebut, y.permanent"),
    @NamedQuery(name = "YvsComCreneauPoint.findCreno", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.point = :point AND y.tranche=:tranche AND y.actif=true AND (y.jour=:jour OR y.permanent=true)"),
    @NamedQuery(name = "YvsComCreneauPoint.findCrenoJour", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.point = :point AND y.actif=true AND (y.jour.jour =:jour OR y.permanent=true)"),
    @NamedQuery(name = "YvsComCreneauPoint.findCurrentByDepot", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.point = :point AND (y.permanent = true OR y.jour.jour = :jour)"),
    @NamedQuery(name = "YvsComCreneauPoint.findByDepotPermanent", query = "SELECT y FROM YvsComCreneauPoint y JOIN FETCH y.tranche WHERE y.point = :point AND y.permanent = :permanent AND y.actif = TRUE ORDER BY y.tranche.typeJournee, y.tranche.heureDebut"),
    @NamedQuery(name = "YvsComCreneauPoint.findByType", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.tranche = :type"),
    @NamedQuery(name = "YvsComCreneauPoint.findByTypePermanent", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.tranche = :type AND y.permanent = :permanent"),
    @NamedQuery(name = "YvsComCreneauPoint.findByTypeDepot", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.tranche = :type AND y.point = :point"),
    @NamedQuery(name = "YvsComCreneauPoint.findByTypeDepotJour_", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.tranche = :type AND y.point = :point AND y.jour.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauPoint.findByTypeDepotPermanent", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.tranche = :type AND y.point = :point AND y.permanent = :permanent"),
    @NamedQuery(name = "YvsComCreneauPoint.findByJourDepot", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.point = :point AND y.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauPoint.findByJourDepot_", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.point = :point AND y.jour.jour = :jour AND y.actif = TRUE"),
    @NamedQuery(name = "YvsComCreneauPoint.findByTypeJour", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.tranche = :type AND y.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauPoint.findByTypeJourDepot", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.tranche = :type AND y.point = :point AND y.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauPoint.findByActif", query = "SELECT y FROM YvsComCreneauPoint y WHERE y.actif = :actif"),

    @NamedQuery(name = "YvsComCreneauPoint.findTrancheCurrentByDepot", query = "SELECT DISTINCT(y.tranche) FROM YvsComCreneauPoint y WHERE y.point = :point AND (y.permanent = true OR y.jour.jour = :jour)"),
    @NamedQuery(name = "YvsComCreneauPoint.findTrancheByJourDepot_", query = "SELECT DISTINCT(y.tranche) FROM YvsComCreneauPoint y WHERE y.point = :point AND y.jour.jour = :jour"),
    @NamedQuery(name = "YvsComCreneauPoint.findTrancheByDepotPermanent", query = "SELECT DISTINCT(y.tranche) FROM YvsComCreneauPoint y WHERE y.point = :point AND y.permanent = :permanent"),
    @NamedQuery(name = "YvsComCreneauPoint.findTrancheByDepot", query = "SELECT DISTINCT(y.tranche) FROM YvsComCreneauPoint y WHERE y.point = :point")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComCreneauPoint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_creneau_point_id_seq", name = "yvs_com_creneau_point_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_creneau_point_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    private Boolean permanent = true;
    @JoinColumn(name = "jour", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsJoursOuvres jour;
    @JoinColumn(name = "tranche", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTrancheHoraire tranche;
    @JoinColumn(name = "point", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private YvsBasePointVente point;
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

    public YvsComCreneauPoint() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComCreneauPoint(Long id) {
        this();
        this.id = id;
    }

    public YvsComCreneauPoint(Long id, YvsJoursOuvres jour, YvsGrhTrancheHoraire tranche, YvsBasePointVente point) {
        this(id, tranche, point);
        this.jour = jour;
    }

    public YvsComCreneauPoint(Long id, YvsGrhTrancheHoraire tranche, YvsBasePointVente point) {
        this(id);
        this.tranche = tranche;
        this.point = point;
    }

    public YvsComCreneauPoint(Long id, YvsJoursOuvres jour, YvsGrhTrancheHoraire type) {
        this(id);
        this.jour = jour;
        this.tranche = type;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Boolean getPermanent() {
        return permanent != null ? permanent : false;
    }

    public String getSemiReference() {
        if (getTranche() != null && getPoint() != null) {
            semiReference = " (Permanent) : " + getTimeToString(getTranche().getHeureDebut()) + " à " + getTimeToString(getTranche().getHeureFin());
            if (getJour() != null ? getJour().getId() > 0 : false) {
                semiReference = getJour().getJour() + " : " + getTimeToString(getTranche().getHeureDebut()) + " à " + getTimeToString(getTranche().getHeureFin());
            }
        }
        return semiReference != null ? semiReference : "";
    }

    public void setSemiReference(String semiReference) {
        this.semiReference = semiReference;
    }

    public String getReference() {
        if (getTranche() != null && getPoint() != null) {
            reference = getPoint().getLibelle() + " - (Permanent) : " + getTimeToString(getTranche().getHeureDebut()) + " à " + getTimeToString(getTranche().getHeureFin());
            if (!getPermanent() && (getJour() != null ? getJour().getId() > 0 : false)) {
                reference = getPoint().getLibelle() + " - " + getJour().getJour() + " : " + getTimeToString(getTranche().getHeureDebut()) + " à " + getTimeToString(getTranche().getHeureFin());
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

    @XmlTransient
    @JsonIgnore
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

    @XmlTransient
    @JsonIgnore
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

    public YvsBasePointVente getPoint() {
        return point;
    }

    public void setPoint(YvsBasePointVente point) {
        this.point = point;
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
        if (!(object instanceof YvsComCreneauPoint)) {
            return false;
        }
        YvsComCreneauPoint other = (YvsComCreneauPoint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.creneau.YvsComCreneauPoint[ id=" + id + " ]";
    }

    public static String getTimeToString(Date time) {
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        return formater.format(time != null ? time : new Date());
    }

}
