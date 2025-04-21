/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

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
import javax.validation.constraints.Size;
import yvs.entity.grh.param.YvsGrhGradeEmploye;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_grh_profil_emps")
@NamedQueries({
    @NamedQuery(name = "YvsProfilEmps.findAll", query = "SELECT y FROM YvsGrhProfilEmps y"),
    @NamedQuery(name = "YvsProfilEmps.findById", query = "SELECT y FROM YvsGrhProfilEmps y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProfilEmps.findByGrade", query = "SELECT y FROM YvsGrhProfilEmps y WHERE y.grade = :grade"),
    @NamedQuery(name = "YvsProfilEmps.findByStatut", query = "SELECT y FROM YvsGrhProfilEmps y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsProfilEmps.findEmployeByStatut", query = "SELECT DISTINCT y.employe FROM YvsGrhProfilEmps y WHERE y.statut = :statut AND y.actif=true AND y.employe.agence.societe=:societe ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsProfilEmps.findCEmployeByStatut", query = "SELECT DISTINCT COUNT(y) FROM YvsGrhProfilEmps y WHERE y.statut = :statut AND y.actif=true AND y.employe.agence.societe=:societe GROUP BY y.employe.nom ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsProfilEmps.findEmployeByStatutActif", query = "SELECT y.employe FROM YvsGrhProfilEmps y WHERE y.statut = :statut AND y.actif=true AND y.employe.actif=true AND y.employe.agence.societe=:societe ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsProfilEmps.findByDateChange", query = "SELECT y FROM YvsGrhProfilEmps y WHERE y.dateChange = :dateChange"),
    @NamedQuery(name = "YvsProfilEmps.findByEmploye", query = "SELECT y FROM YvsGrhProfilEmps y WHERE y.employe = :employe AND y.actif=true"),
    @NamedQuery(name = "YvsGrhProfilEmps.findByEmploye", query = "SELECT y FROM YvsGrhProfilEmps y WHERE y.employe = :employe AND y.actif=true AND y.profil=:profil"),
    @NamedQuery(name = "YvsProfilEmps.findByEmploye_", query = "SELECT y FROM YvsGrhProfilEmps y WHERE y.employe = :employe AND y.actif=true AND y.grade=:grade AND y.statut=:statut"),
    @NamedQuery(name = "YvsProfilEmps.countP", query = "SELECT COUNT(y) FROM YvsGrhProfilEmps y WHERE y.employe = :employe AND y.actif=true AND y.grade=:grade AND y.statut=:statut"),
    @NamedQuery(name = "YvsProfilEmps.findByActif", query = "SELECT y FROM YvsGrhProfilEmps y WHERE y.actif = :actif")})
public class YvsGrhProfilEmps implements Serializable {

    @Column(name = "supp")
    private Boolean supp;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_profil_emps_id_seq")
    @SequenceGenerator(sequenceName = "yvs_profil_emps_id_seq", allocationSize = 1, name = "yvs_profil_emps_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "grade", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhGradeEmploye grade;
    @Size(max = 2147483647)
    @Column(name = "statut")
    private String statut;
    @Column(name = "date_change")
    @Temporal(TemporalType.DATE)
    private Date dateChange;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "profil", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhProfil profil;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhProfilEmps() {
    }

    public YvsGrhProfilEmps(Long id) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsGrhGradeEmploye getGrade() {
        return grade;
    }

    public void setGrade(YvsGrhGradeEmploye grade) {
        this.grade = grade;
    }

    public String getStatut() {
        return statut != null ? statut : "PE";
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Date getDateChange() {
        return dateChange;
    }

    public void setDateChange(Date dateChange) {
        this.dateChange = dateChange;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhProfil getProfil() {
        return profil;
    }

    public void setProfil(YvsGrhProfil profil) {
        this.profil = profil;
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
        if (!(object instanceof YvsGrhProfilEmps)) {
            return false;
        }
        YvsGrhProfilEmps other = (YvsGrhProfilEmps) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsProfilEmps[ id=" + id + " ]";
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

}
