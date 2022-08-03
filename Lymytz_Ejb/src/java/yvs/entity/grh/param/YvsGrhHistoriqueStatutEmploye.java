/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_historique_statut_employe")
@NamedQueries({
    @NamedQuery(name = "YvsGrhHistoriqueStatutEmploye.findAll", query = "SELECT y FROM YvsGrhHistoriqueStatutEmploye y"),
    @NamedQuery(name = "YvsGrhHistoriqueStatutEmploye.findById", query = "SELECT y FROM YvsGrhHistoriqueStatutEmploye y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhHistoriqueStatutEmploye.findByStatEmps", query = "SELECT y FROM YvsGrhHistoriqueStatutEmploye y WHERE y.employe=:employe AND y.statut=:statut"),
    @NamedQuery(name = "YvsGrhHistoriqueStatutEmploye.findByDateChange", query = "SELECT y FROM YvsGrhHistoriqueStatutEmploye y WHERE y.dateChange = :dateChange")})
public class YvsGrhHistoriqueStatutEmploye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_historique_statut_employe_id_seq", name = "yvs_grh_historique_statut_employe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_historique_statut_employe_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_change")
    @Temporal(TemporalType.DATE)
    private Date dateChange;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "statut", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhStatutEmploye statut;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;

    public YvsGrhHistoriqueStatutEmploye() {
    }

    public YvsGrhHistoriqueStatutEmploye(Long id) {
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

    public Date getDateChange() {
        return dateChange;
    }

    public void setDateChange(Date dateChange) {
        this.dateChange = dateChange;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhStatutEmploye getStatut() {
        return statut;
    }

    public void setStatut(YvsGrhStatutEmploye statut) {
        this.statut = statut;
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
        if (!(object instanceof YvsGrhHistoriqueStatutEmploye)) {
            return false;
        }
        YvsGrhHistoriqueStatutEmploye other = (YvsGrhHistoriqueStatutEmploye) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhHistoriqueStatutEmploye[ id=" + id + " ]";
    }

}
