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
import yvs.entity.grh.param.YvsGrhEchelons;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_echelon_employe")
@NamedQueries({
    @NamedQuery(name = "YvsEchelonEmploye.findAll", query = "SELECT y FROM YvsEchelonEmploye y"),
    @NamedQuery(name = "YvsEchelonEmploye.findById", query = "SELECT y FROM YvsEchelonEmploye y WHERE y.id = :id"),
    @NamedQuery(name = "YvsEchelonEmploye.findByDateChange", query = "SELECT y FROM YvsEchelonEmploye y WHERE y.dateChange = :dateChange"),
    @NamedQuery(name = "YvsEchelonEmploye.findByEmploye", query = "SELECT y FROM YvsEchelonEmploye y WHERE y.employe = :employe AND y.actif=true"),
    @NamedQuery(name = "YvsEchelonEmploye.countE", query = "SELECT COUNT(y) FROM YvsEchelonEmploye y WHERE y.employe = :employe AND y.actif=true AND y.echelon.id=:echelon"),
    @NamedQuery(name = "YvsEchelonEmploye.findByActif", query = "SELECT y FROM YvsEchelonEmploye y WHERE y.actif = :actif")})
public class YvsEchelonEmploye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_echelon_employe_id_seq")
    @SequenceGenerator(sequenceName = "yvs_echelon_employe_id_seq", allocationSize = 1, name = "yvs_echelon_employe_id_seq")
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
    @Column(name = "actif")
    private boolean actif;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "echelon", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEchelons echelon;

    public YvsEchelonEmploye() {
    }

    public YvsEchelonEmploye(Long id) {
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

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public YvsGrhEchelons getEchelon() {
        return echelon;
    }

    public void setEchelon(YvsGrhEchelons echelon) {
        this.echelon = echelon;
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
        if (!(object instanceof YvsEchelonEmploye)) {
            return false;
        }
        YvsEchelonEmploye other = (YvsEchelonEmploye) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsEchelonEmploye[ id=" + id + " ]";
    }

}
