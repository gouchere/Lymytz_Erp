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
import yvs.entity.grh.param.YvsGrhQualifications;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_grh_qualification_employe")
@NamedQueries({
    @NamedQuery(name = "YvsQualificationEmploye.findAll", query = "SELECT y FROM YvsGrhQualificationEmploye y"),
    @NamedQuery(name = "YvsQualificationEmploye.findByEmploye", query = "SELECT y FROM YvsGrhQualificationEmploye y JOIN FETCH y.qualification WHERE y.employe = :employe"),
    @NamedQuery(name = "YvsGrhQualificationEmploye.findEmployeByEmploye", query = "SELECT y.employe FROM YvsGrhQualificationEmploye y WHERE y.qualification.domaine = :qualification AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsQualificationEmploye.findByQualification", query = "SELECT y FROM YvsGrhQualificationEmploye y WHERE y.qualification = :qualification"),
    @NamedQuery(name = "YvsQualificationEmploye.findByDateAcquisition", query = "SELECT y FROM YvsGrhQualificationEmploye y WHERE y.dateAcquisition = :dateAcquisition")})
public class YvsGrhQualificationEmploye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    @SequenceGenerator(sequenceName = "yvs_grh_qualification_employe_id_seq", name = "yvs_grh_qualification_employe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_qualification_employe_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_acquisition")
    @Temporal(TemporalType.DATE)
    private Date dateAcquisition;
    @JoinColumn(name = "qualification", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhQualifications qualification;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhQualificationEmploye() {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(Date dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public YvsGrhQualifications getQualification() {
        return qualification;
    }

    public void setQualification(YvsGrhQualifications qualification) {
        this.qualification = qualification;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final YvsGrhQualificationEmploye other = (YvsGrhQualificationEmploye) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsGrhQualificationEmploye{" + "id=" + id + '}';
    }

}
