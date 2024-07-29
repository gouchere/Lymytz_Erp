/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_langue_emps")
@NamedQueries({
    @NamedQuery(name = "YvsLangueEmps.findAll", query = "SELECT y FROM YvsLangueEmps y"),
    @NamedQuery(name = "YvsLangueEmps.findByLangue", query = "SELECT y FROM YvsLangueEmps y WHERE y.yvsLangueEmpsPK.langue = :langue"),
    @NamedQuery(name = "YvsLangueEmps.findByEmploye", query = "SELECT y FROM YvsLangueEmps y JOIN FETCH y.langue WHERE y.yvsLangueEmpsPK.employe = :employe"),
    @NamedQuery(name = "YvsLangueEmps.findByParle", query = "SELECT y FROM YvsLangueEmps y WHERE y.parle = :parle"),
    @NamedQuery(name = "YvsLangueEmps.findByEcrit", query = "SELECT y FROM YvsLangueEmps y WHERE y.ecrit = :ecrit"),
    @NamedQuery(name = "YvsLangueEmps.findByLu", query = "SELECT y FROM YvsLangueEmps y WHERE y.lu = :lu"),
    @NamedQuery(name = "YvsLangueEmps.findBySupp", query = "SELECT y FROM YvsLangueEmps y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsLangueEmps.findByActif", query = "SELECT y FROM YvsLangueEmps y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsLangueEmps.findByDateUpdate", query = "SELECT y FROM YvsLangueEmps y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsLangueEmps.findByDateSave", query = "SELECT y FROM YvsLangueEmps y WHERE y.dateSave = :dateSave")})
public class YvsLangueEmps implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected YvsLangueEmpsPK yvsLangueEmpsPK;
    @Column(name = "parle")
    private Boolean parle;
    @Column(name = "ecrit")
    private Boolean ecrit;
    @Column(name = "lu")
    private Boolean lu;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "employe", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "langue", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsLangues langue;

    public YvsLangueEmps() {
    }

    public YvsLangueEmps(YvsLangueEmpsPK yvsLangueEmpsPK) {
        this.yvsLangueEmpsPK = yvsLangueEmpsPK;
    }

    public YvsLangueEmps(int langue, int employe) {
        this.yvsLangueEmpsPK = new YvsLangueEmpsPK(langue, employe);
    }

    public YvsLangueEmpsPK getYvsLangueEmpsPK() {
        return yvsLangueEmpsPK;
    }

    public void setYvsLangueEmpsPK(YvsLangueEmpsPK yvsLangueEmpsPK) {
        this.yvsLangueEmpsPK = yvsLangueEmpsPK;
    }

    public Boolean getParle() {
        return parle != null ? parle : false;
    }

    public void setParle(Boolean parle) {
        this.parle = parle;
    }

    public Boolean getEcrit() {
        return ecrit != null ? ecrit : false;
    }

    public void setEcrit(Boolean ecrit) {
        this.ecrit = ecrit;
    }

    public Boolean getLu() {
        return lu != null ? lu : false;
    }

    public void setLu(Boolean lu) {
        this.lu = lu;
    }

    public Boolean getSupp() {
        return supp != null ? supp : false;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public YvsLangues getLangue() {
        return langue;
    }

    public void setLangue(YvsLangues langue) {
        this.langue = langue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (yvsLangueEmpsPK != null ? yvsLangueEmpsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsLangueEmps)) {
            return false;
        }
        YvsLangueEmps other = (YvsLangueEmps) object;
        if ((this.yvsLangueEmpsPK == null && other.yvsLangueEmpsPK != null) || (this.yvsLangueEmpsPK != null && !this.yvsLangueEmpsPK.equals(other.yvsLangueEmpsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsLangueEmps[ yvsLangueEmpsPK=" + yvsLangueEmpsPK + " ]";
    }

}
