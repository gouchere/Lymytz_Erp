/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_affec_anal_emp")
@NamedQueries({
    @NamedQuery(name = "YvsComptaAffecAnalEmp.findAll", query = "SELECT y FROM YvsComptaAffecAnalEmp y"),
    @NamedQuery(name = "YvsComptaAffecAnalEmp.findById", query = "SELECT y FROM YvsComptaAffecAnalEmp y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaAffecAnalEmp.findByEmploye", query = "SELECT y FROM YvsComptaAffecAnalEmp y JOIN FETCH y.centre WHERE y.employe = :employe"),
    @NamedQuery(name = "YvsComptaAffecAnalEmp.findByCoeficient", query = "SELECT y FROM YvsComptaAffecAnalEmp y WHERE y.coeficient = :coeficient"),
    @NamedQuery(name = "YvsComptaAffecAnalEmp.findByDateSave", query = "SELECT y FROM YvsComptaAffecAnalEmp y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaAffecAnalEmp.findByDateUpdate", query = "SELECT y FROM YvsComptaAffecAnalEmp y WHERE y.dateUpdate = :dateUpdate")})
public class YvsComptaAffecAnalEmp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_affec_anal_emp_id_seq", name = "yvs_compta_affec_anal_emp_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_affec_anal_emp_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "coeficient")
    private Double coeficient;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe",referencedColumnName = "id")
    private YvsGrhEmployes employe;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centre",referencedColumnName = "id")
    private YvsComptaCentreAnalytique centre;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author",referencedColumnName = "id")
    private YvsUsersAgence author;

    public YvsComptaAffecAnalEmp() {
    }

    public YvsComptaAffecAnalEmp(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCoeficient() {
        return coeficient;
    }

    public void setCoeficient(Double coeficient) {
        this.coeficient = coeficient;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public YvsComptaCentreAnalytique getCentre() {
        return centre;
    }

    public void setCentre(YvsComptaCentreAnalytique centre) {
        this.centre = centre;
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
        if (!(object instanceof YvsComptaAffecAnalEmp)) {
            return false;
        }
        YvsComptaAffecAnalEmp other = (YvsComptaAffecAnalEmp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaAffecAnalEmp[ id=" + id + " ]";
    }

}
