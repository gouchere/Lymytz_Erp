/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.ration;

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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_param_ration_suspension")
@NamedQueries({
    @NamedQuery(name = "YvsComParamRationSuspension.findAll", query = "SELECT y FROM YvsComParamRationSuspension y WHERE y.personnel.personnel.societe = :societe"),
    @NamedQuery(name = "YvsComParamRationSuspension.findByPersonnel", query = "SELECT y FROM YvsComParamRationSuspension y WHERE y.personnel = :personnel"),
    @NamedQuery(name = "YvsComParamRationSuspension.findByPersonnelNotDate", query = "SELECT y FROM YvsComParamRationSuspension y WHERE y.personnel = :personnel AND :date NOT BETWEEN y.debutSuspension AND y.finSuspension"),
    @NamedQuery(name = "YvsComParamRationSuspension.findByPersonnelDate", query = "SELECT y FROM YvsComParamRationSuspension y WHERE y.personnel = :personnel AND :date BETWEEN y.debutSuspension AND y.finSuspension"),
    @NamedQuery(name = "YvsComParamRationSuspension.findCByPersonnelDate", query = "SELECT COUNT(y) FROM YvsComParamRationSuspension y WHERE y.personnel = :personnel AND :date BETWEEN y.debutSuspension AND y.finSuspension"),
    @NamedQuery(name = "YvsComParamRationSuspension.findByPersonnelDates", query = "SELECT y FROM YvsComParamRationSuspension y WHERE y.personnel = :personnel AND y.debutSuspension >= :debutSuspension AND y.finSuspension <= :finSuspension"),
    @NamedQuery(name = "YvsComParamRationSuspension.findIdPersonnelByDate", query = "SELECT DISTINCT y.personnel.id FROM YvsComParamRationSuspension y WHERE y.personnel.personnel.societe = :societe AND :date BETWEEN y.debutSuspension AND y.finSuspension"),
    @NamedQuery(name = "YvsComParamRationSuspension.findIdPersonnelByDates", query = "SELECT DISTINCT y.personnel.id FROM YvsComParamRationSuspension y WHERE y.personnel.personnel.societe = :societe AND :date BETWEEN y.debutSuspension AND y.finSuspension"),
    @NamedQuery(name = "YvsComParamRationSuspension.findById", query = "SELECT y FROM YvsComParamRationSuspension y WHERE y.id = :id")})
public class YvsComParamRationSuspension implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_param_ration_suspension_id_seq", name = "yvs_com_param_ration_suspension_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_param_ration_suspension_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "debut_suspension")
    @Temporal(TemporalType.DATE)
    private Date debutSuspension;
    @Column(name = "fin_suspension")
    @Temporal(TemporalType.DATE)
    private Date finSuspension;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "personnel", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComParamRation personnel;

    public YvsComParamRationSuspension() {
    }

    public YvsComParamRationSuspension(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDebutSuspension() {
        return debutSuspension != null ? debutSuspension : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDebutSuspension(Date debutSuspension) {
        this.debutSuspension = debutSuspension;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFinSuspension() {
        return finSuspension != null ? finSuspension : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setFinSuspension(Date finSuspension) {
        this.finSuspension = finSuspension;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComParamRation getPersonnel() {
        return personnel;
    }

    public void setPersonnel(YvsComParamRation personnel) {
        this.personnel = personnel;
    }

    public String etat() {
        return etat(new Date());
    }

    public String etat(Date date) {
        if (!getDebutSuspension().after(date) && !getFinSuspension().before(date)) {
            return "A";
        }
        return "E";
    }

    public String etat(Date debut, Date fin) {
        if ((!getDebutSuspension().after(debut) && !getFinSuspension().before(debut)) || (!getDebutSuspension().after(fin) && !getFinSuspension().before(fin))) {
            return "A";
        }
        return "E";
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
        if (!(object instanceof YvsComParamRationSuspension)) {
            return false;
        }
        YvsComParamRationSuspension other = (YvsComParamRationSuspension) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.ration.YvsComParamRationSuspension[ id=" + id + " ]";
    }

}
