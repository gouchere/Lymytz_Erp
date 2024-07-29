/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

import yvs.entity.grh.param.YvsGrhSanction;
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
import yvs.entity.grh.personnel.YvsGrhEmployes;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_sanction_emps")
@NamedQueries({
    @NamedQuery(name = "YvsGrhSanctionEmps.findAll", query = "SELECT y FROM YvsGrhSanctionEmps y WHERE y.actif = true"),
    @NamedQuery(name = "YvsGrhSanctionEmps.findById", query = "SELECT y FROM YvsGrhSanctionEmps y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhSanctionEmps.findBySupp", query = "SELECT y FROM YvsGrhSanctionEmps y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsGrhSanctionEmps.findByActif", query = "SELECT y FROM YvsGrhSanctionEmps y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsGrhSanctionEmps.findByEmploye", query = "SELECT y FROM YvsGrhSanctionEmps y WHERE y.employe = :employe"),
    @NamedQuery(name = "YvsGrhSanctionEmps.findByDateSanction", query = "SELECT y FROM YvsGrhSanctionEmps y WHERE y.dateSanction = :dateSanction"),
    @NamedQuery(name = "YvsGrhSanctionEmps.findByDateDebut", query = "SELECT y FROM YvsGrhSanctionEmps y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsGrhSanctionEmps.findByEnCours", query = "SELECT y FROM YvsGrhSanctionEmps y WHERE y.dateFin > :dateFin"),
    @NamedQuery(name = "YvsGrhSanctionEmps.findByDateFin", query = "SELECT y FROM YvsGrhSanctionEmps y WHERE y.dateFin = :dateFin")})
public class YvsGrhSanctionEmps implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_sanction_emps_id_seq", name = "yvs_grh_sanction_emps_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_sanction_emps_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_sanction")
    @Temporal(TemporalType.DATE)
    private Date dateSanction;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @JoinColumn(name = "sanction", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhSanction sanction;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;

    public YvsGrhSanctionEmps() {
    }

    public YvsGrhSanctionEmps(Long id) {
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

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Date getDateSanction() {
        return dateSanction;
    }

    public void setDateSanction(Date dateSanction) {
        this.dateSanction = dateSanction;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public YvsGrhSanction getSanction() {
        return sanction;
    }

    public void setSanction(YvsGrhSanction sanction) {
        this.sanction = sanction;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
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
        if (!(object instanceof YvsGrhSanctionEmps)) {
            return false;
        }
        YvsGrhSanctionEmps other = (YvsGrhSanctionEmps) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsGrhSanctionEmps[ id=" + id + " ]";
    }

}
