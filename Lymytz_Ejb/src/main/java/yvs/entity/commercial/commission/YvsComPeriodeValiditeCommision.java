/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.commission;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_periode_validite_commision")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsComPeriodeValiditeCommision.findAll", query = "SELECT y FROM YvsComPeriodeValiditeCommision y"),
    @NamedQuery(name = "YvsComPeriodeValiditeCommision.findById", query = "SELECT y FROM YvsComPeriodeValiditeCommision y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComPeriodeValiditeCommision.findByDateDebut", query = "SELECT y FROM YvsComPeriodeValiditeCommision y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsComPeriodeValiditeCommision.findByDateFin", query = "SELECT y FROM YvsComPeriodeValiditeCommision y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsComPeriodeValiditeCommision.findByPeriodicite", query = "SELECT y FROM YvsComPeriodeValiditeCommision y WHERE y.periodicite = :periodicite")})
public class YvsComPeriodeValiditeCommision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_periode_validite_commision_id_seq", name = "yvs_com_periode_validite_commision_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_periode_validite_commision_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "periodicite")
    private Character periodicite;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "facteur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComFacteurTaux facteur;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComPeriodeValiditeCommision() {
    }

    public YvsComPeriodeValiditeCommision(Integer id) {
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateDebut() {
        return dateDebut != null ? dateDebut : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateFin() {
        return dateFin != null ? dateFin : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Character getPeriodicite() {
        return periodicite != null ? String.valueOf(periodicite).trim().length() > 0 ? periodicite : 'P' : 'P';
    }

    public void setPeriodicite(Character periodicite) {
        this.periodicite = periodicite;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComFacteurTaux getFacteur() {
        return facteur;
    }

    public void setFacteur(YvsComFacteurTaux facteur) {
        this.facteur = facteur;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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
        if (!(object instanceof YvsComPeriodeValiditeCommision)) {
            return false;
        }
        YvsComPeriodeValiditeCommision other = (YvsComPeriodeValiditeCommision) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.commission.YvsComPeriodeValiditeCommision[ id=" + id + " ]";
    }

}
