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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_tranche_horaire")
@NamedQueries({
    @NamedQuery(name = "YvsGrhTrancheHoraire.findAll", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.societe=:societe ORDER BY y.heureDebut, y.typeJournee"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findAllActif", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.societe=:societe AND y.actif=true ORDER BY y.heureDebut, y.typeJournee"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findAllByType", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.societe=:societe AND y.typeJournee=:typeJ AND y.actif=true ORDER BY y.heureDebut ASC"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findById", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByTitre", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.titre = :titre"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByTitres", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.titre = :titre AND y.societe = :societe"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByHeureDebut", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.heureDebut = :heureDebut"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByHeureFin", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.heureFin = :heureFin"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByOnline", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.societe=:societe AND y.venteOnline = :venteOnline"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findTypeTranche", query = "SELECT DISTINCT y.typeJournee FROM YvsGrhTrancheHoraire y WHERE y.societe=:societe AND y.typeJournee IS NOT NULL"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByTypeJournee", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.societe=:societe AND UPPER(y.typeJournee) = UPPER(:typeJournee) ORDER BY y.heureDebut, y.typeJournee"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findChevaucheByTypeJournee", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.societe=:societe AND UPPER(y.typeJournee) = UPPER(:typeJournee) AND y.heureDebut > y.heureFin ORDER BY y.heureDebut, y.typeJournee"),
    @NamedQuery(name = "YvsGrhTrancheHoraire.findByCritere", query = "SELECT y FROM YvsGrhTrancheHoraire y WHERE y.typeJournee = :critere AND y.societe = :societe")})
public class YvsGrhTrancheHoraire extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_tranche_horaire_id_seq1", name = "yvs_tranche_horaire_id_seq1_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_tranche_horaire_id_seq1_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "titre")
    private String titre;
    @Column(name = "heure_debut")
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    @Size(max = 5)
    @Column(name = "type_journee")
    private String typeJournee;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "vente_online")
    private Boolean venteOnline;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean chevauche = false;
    @Transient
    private String reference;
    @Transient
    private Date dureePause;

    public YvsGrhTrancheHoraire() {
    }

    public YvsGrhTrancheHoraire(Long id) {
        this.id = id;
    }

    public YvsGrhTrancheHoraire(Long id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    public YvsGrhTrancheHoraire(Long id, String titre, Date hf) {
        this.id = id;
        this.titre = titre;
        this.heureFin = hf;
    }

    public YvsGrhTrancheHoraire(Long id, Date hd, Date hf) {
        this.id = id;
        this.heureDebut = hd;
        this.heureFin = hf;
    }

    public YvsGrhTrancheHoraire(Long id, String titre, Date hd, Date hf) {
        this.id = id;
        this.titre = titre;
        this.heureFin = hf;
        this.heureDebut = hd;
    }

    public YvsGrhTrancheHoraire(Long id, String titre, String typeJ, Date hd, Date hf) {
        this.id = id;
        this.titre = titre;
        this.heureFin = hf;
        this.typeJournee = typeJ;
        this.heureDebut = hd;
    }

    public Boolean getVenteOnline() {
        return venteOnline != null ? venteOnline : false;
    }

    public void setVenteOnline(Boolean venteOnline) {
        this.venteOnline = venteOnline;
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

    public Date getDureePause() {
        return dureePause;
    }

    public void setDureePause(Date dureePause) {
        this.dureePause = dureePause;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre != null ? titre : "";
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getHeureDebut() {
        return heureDebut != null ? heureDebut : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getHeureFin() {
        return heureFin != null ? heureFin : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public String getTypeJournee() {
        return typeJournee != null ? typeJournee : "";
    }

    public void setTypeJournee(String typeJournee) {
        this.typeJournee = typeJournee;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public boolean isChevauche() {
        return chevauche;
    }

    public void setChevauche(boolean chevauche) {
        this.chevauche = chevauche;
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
        if (!(object instanceof YvsGrhTrancheHoraire)) {
            return false;
        }
        YvsGrhTrancheHoraire other = (YvsGrhTrancheHoraire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.presence.YvsGrhTrancheHoraire[ id=" + id + " ]";
    }

}
