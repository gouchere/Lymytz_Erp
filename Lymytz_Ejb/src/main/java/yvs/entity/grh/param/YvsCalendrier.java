/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_calendrier")
@NamedQueries({
    @NamedQuery(name = "YvsCalendrier.findAll", query = "SELECT y FROM YvsCalendrier y"),
    @NamedQuery(name = "YvsCalendrier.findById", query = "SELECT y FROM YvsCalendrier y WHERE y.id = :id"),
    @NamedQuery(name = "YvsCalendrier.findByDefaut", query = "SELECT y FROM YvsCalendrier y WHERE y.defaut = :defaut"),
    @NamedQuery(name = "YvsCalendrier.findBySociete", query = "SELECT y FROM YvsCalendrier y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsCalendrier.findByModule", query = "SELECT y FROM YvsCalendrier y WHERE y.societe = :societe AND y.module = :module"),
    @NamedQuery(name = "YvsCalendrier.findByDefautSociete", query = "SELECT y FROM YvsCalendrier y WHERE y.societe = :societe AND y.defaut =true"),
    @NamedQuery(name = "YvsCalendrier.findByReference", query = "SELECT y FROM YvsCalendrier y WHERE y.reference = :reference AND y.societe=:societe")})
public class YvsCalendrier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_calendrier_id_seq", name = "yvs_calendrier_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_calendrier_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @Column(name = "module")
    private String module;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "temps_marge")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date tempsMarge;
    @Column(name = "actif")
    private Boolean actif;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    @OneToMany(mappedBy = "calendrier")
    private List<YvsJoursOuvres> joursOuvres;

    public YvsCalendrier() {
        joursOuvres = new ArrayList<>();
    }

    public YvsCalendrier(Integer id) {
        this();
        this.id = id;
    }

    public YvsCalendrier(Integer id, String reference) {
        this(id);
        this.reference = reference;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModule() {
        return module != null ? (module.trim().length() > 0 ? module : Constantes.MOD_GRH) : Constantes.MOD_GRH;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Boolean isDefaut() {
        return defaut != null ? defaut : false;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsJoursOuvres> getJoursOuvres() {
        return joursOuvres;
    }

    public void setJoursOuvres(List<YvsJoursOuvres> joursOuvres) {
        this.joursOuvres = joursOuvres;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getTempsMarge() {
        return tempsMarge;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setTempsMarge(Date tempsMarge) {
        this.tempsMarge = tempsMarge;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsCalendrier)) {
            return false;
        }
        YvsCalendrier other = (YvsCalendrier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsCalendrier[ id=" + id + " ]";
    }

    }
