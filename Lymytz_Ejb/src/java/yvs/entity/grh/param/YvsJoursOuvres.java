/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.Util;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_jours_ouvres")
@NamedQueries({
    @NamedQuery(name = "YvsJoursOuvres.findAll", query = "SELECT y FROM YvsJoursOuvres y"),
    @NamedQuery(name = "YvsJoursOuvres.findDefaut", query = "SELECT y FROM YvsJoursOuvres y WHERE y.calendrier.societe = :societe AND y.calendrier.defaut = true"),
    @NamedQuery(name = "YvsJoursOuvres.findJourDefaut", query = "SELECT y FROM YvsJoursOuvres y WHERE y.calendrier.societe = :societe AND y.calendrier.defaut = true AND UPPER(y.jour) = UPPER(:jour)"),
    @NamedQuery(name = "YvsJoursOuvres.findById", query = "SELECT y FROM YvsJoursOuvres y WHERE y.id = :id"),
    @NamedQuery(name = "YvsJoursOuvres.findByJour", query = "SELECT y FROM YvsJoursOuvres y WHERE y.jour = :jour"),
    @NamedQuery(name = "YvsJoursOuvres.findByJourConnu", query = "SELECT y FROM YvsJoursOuvres y WHERE UPPER(y.jour) = UPPER(:jour) AND y.calendrier = :calendrier"),
    @NamedQuery(name = "YvsJoursOuvres.findByCalendrier", query = "SELECT y FROM YvsJoursOuvres y WHERE y.calendrier = :calendrier ORDER BY y.ordre"),
    @NamedQuery(name = "YvsJoursOuvres.findByHeureDebutTravail", query = "SELECT y FROM YvsJoursOuvres y WHERE y.heureDebutTravail = :heureDebutTravail"),
    @NamedQuery(name = "YvsJoursOuvres.findByHeureFinTravail", query = "SELECT y FROM YvsJoursOuvres y WHERE y.heureFinTravail = :heureFinTravail"),
    @NamedQuery(name = "YvsJoursOuvres.findByDureePause", query = "SELECT y FROM YvsJoursOuvres y WHERE y.dureePause = :dureePause"),
    @NamedQuery(name = "YvsJoursOuvres.findByHeureDebutPause", query = "SELECT y FROM YvsJoursOuvres y WHERE y.heureDebutPause = :heureDebutPause"),
    @NamedQuery(name = "YvsJoursOuvres.findByHeureFinPause", query = "SELECT y FROM YvsJoursOuvres y WHERE y.heureFinPause = :heureFinPause"),
    @NamedQuery(name = "YvsJoursOuvres.findByActif", query = "SELECT y FROM YvsJoursOuvres y WHERE y.actif = :actif")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsJoursOuvres implements Serializable , Comparator<YvsJoursOuvres> {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_jours_ouvree_id_seq", name = "yvs_jours_ouvree_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_jours_ouvree_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "jour")
    private String jour;
    @Column(name = "heure_debut_travail")
    @Temporal(TemporalType.TIME)
    private Date heureDebutTravail;
    @Column(name = "heure_fin_travail")
    @Temporal(TemporalType.TIME)
    private Date heureFinTravail;
    @Column(name = "duree_pause")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date dureePause;
    @Column(name = "heure_debut_pause")
    @Temporal(TemporalType.TIME)
    private Date heureDebutPause;
    @Column(name = "heure_fin_pause")
    @Temporal(TemporalType.TIME)
    private Date heureFinPause;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "ouvrable")
    private Boolean ouvrable;
    @Column(name = "jour_de_repos")
    private Boolean jourDeRepos;
    @Column(name = "ordre")
    private Integer ordre;
    @Column(name = "duree_travail")
    private Double dureeTravail;
    @Column(name = "duree_repos")
    private Double dureeRepos;
    
    @JoinColumn(name = "calendrier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsCalendrier calendrier;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private Date date_;
    @Transient
    private double dureeService;

    public YvsJoursOuvres() {
    }

    public YvsJoursOuvres(Long id) {
        this.id = id;
    }

    public YvsJoursOuvres(Long id, String jour) {
        this.id = id;
        this.jour = jour;
    }

    public YvsJoursOuvres(Long id, Date hd, Date hf) {
        this.id = id;
        this.heureDebutTravail = hd;
        this.heureFinTravail = hf;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public Date getHeureDebutTravail() {
        return heureDebutTravail;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public void setHeureDebutTravail(Date heureDebutTravail) {
        this.heureDebutTravail = heureDebutTravail;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public Date getHeureFinTravail() {
        return heureFinTravail;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public void setHeureFinTravail(Date heureFinTravail) {
        this.heureFinTravail = heureFinTravail;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public Date getDureePause() {
        return dureePause;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public void setDureePause(Date dureePause) {
        this.dureePause = dureePause;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public Date getHeureDebutPause() {
        return heureDebutPause;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public void setHeureDebutPause(Date heureDebutPause) {
        this.heureDebutPause = heureDebutPause;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public Date getHeureFinPause() {
        return heureFinPause;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public void setHeureFinPause(Date heureFinPause) {
        this.heureFinPause = heureFinPause;
    }

    public double getDureeService() {
        int total = Util.ecartOnDate(heureDebutTravail, heureFinTravail, "heure");
        int pause = Util.ecartOnDate(heureDebutPause, heureFinPause, "heure");
        dureeService = total - pause;
        return dureeService;
    }

    public void setDureeService(double dureeService) {
        this.dureeService = dureeService;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsCalendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(YvsCalendrier calendrier) {
        this.calendrier = calendrier;
    }

    public Boolean getOuvrable() {
        return ouvrable != null ? ouvrable : true;
    }

    public void setOuvrable(Boolean ouvrable) {
        this.ouvrable = ouvrable;
    }

    public Boolean getJourDeRepos() {
        return jourDeRepos != null ? jourDeRepos : false;
    }

    public void setJourDeRepos(Boolean jourDeRepos) {
        this.jourDeRepos = jourDeRepos;
    }

    public Integer getOrdre() {
        return ordre != null ? ordre : 0;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Double getDureeTravail() {
        return dureeTravail!=null?dureeTravail:0;
    }

    public void setDureeTravail(Double dureeTravail) {
        this.dureeTravail = dureeTravail;
    }

    public Double getDureeRepos() {
        return dureeRepos;
    }

    public void setDureeRepos(Double dureeRepos) {
        this.dureeRepos = dureeRepos;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public Date getDate_() {
        return date_;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public void setDate_(Date date_) {
        this.date_ = date_;
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
        if (!(object instanceof YvsJoursOuvres)) {
            return false;
        }
        YvsJoursOuvres other = (YvsJoursOuvres) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsJoursOuvres[ id=" + id + " ]";
    }
    
    
    @Override
    public int compare(YvsJoursOuvres o1, YvsJoursOuvres o2) {
        if (o1 != null && o2 != null) {
           return o1.getOrdre().compareTo(o2.getOrdre());
        } else if (o1 != null) {
            return 1;
        } else {
            return -1;
        }
    }

}
