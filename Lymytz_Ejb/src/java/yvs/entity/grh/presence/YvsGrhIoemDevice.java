/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.presence;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_grh_ioem_device")
@NamedQueries({
    @NamedQuery(name = "YvsGrhIoemDevice.findAll", query = "SELECT y FROM YvsGrhIoemDevice y"),
    @NamedQuery(name = "YvsGrhIoemDevice.findById", query = "SELECT y FROM YvsGrhIoemDevice y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhIoemDevice.findByDateUpdate", query = "SELECT y FROM YvsGrhIoemDevice y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsGrhIoemDevice.findByDateSave", query = "SELECT y FROM YvsGrhIoemDevice y WHERE y.dateSave = :dateSave"),

    @NamedQuery(name = "YvsGrhIoemDevice.countByEmployeDateTime", query = "SELECT COUNT(y.id) FROM YvsGrhIoemDevice y WHERE y.employe = :employe AND y.dateTimeAction = :dateTime"),

    @NamedQuery(name = "YvsGrhIoemDevice.findByPointeuse", query = "SELECT y FROM YvsGrhIoemDevice y WHERE y.pointeuse = :pointeuse ORDER BY y.dateTimeAction"),
    @NamedQuery(name = "YvsGrhIoemDevice.findByPointeuseEmploye", query = "SELECT y FROM YvsGrhIoemDevice y WHERE y.pointeuse = :pointeuse AND y.employe = :employe ORDER BY y.dateTimeAction"),
    @NamedQuery(name = "YvsGrhIoemDevice.findByPointeuseDate", query = "SELECT y FROM YvsGrhIoemDevice y WHERE y.pointeuse = :pointeuse AND y.dateAction BETWEEN :dateDebut AND :dateFin ORDER BY y.dateTimeAction"),
    @NamedQuery(name = "YvsGrhIoemDevice.findByPointeuseEmployeDate", query = "SELECT y FROM YvsGrhIoemDevice y WHERE y.pointeuse = :pointeuse AND y.employe = :employe AND y.dateAction BETWEEN :dateDebut AND :dateFin ORDER BY y.dateTimeAction")})
public class YvsGrhIoemDevice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "machine")
    private String machine;
    @Column(name = "employe")
    private Long employe;
    @Column(name = "date_action")
    @Temporal(TemporalType.DATE)
    private Date dateAction;
    @Column(name = "time_action")
    @Temporal(TemporalType.TIME)
    private Date timeAction;
    @Column(name = "date_time_action")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeAction;
    @Column(name = "verify_mode")
    private Integer verifyMode = 0;
    @Column(name = "in_out_mode")
    private Integer inOutMode = 0;
    @Column(name = "work_code")
    private Integer workCode = 0;
    @Column(name = "reserved")
    private Integer reserved = 0;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "pointeuse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsPointeuse pointeuse;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private YvsGrhEmployes employes = new YvsGrhEmployes();
    @Transient
    private boolean inclure = true;
    @Transient
    private boolean icorrect = false;
    @Transient
    private int idwYear;
    @Transient
    private int idwMonth;
    @Transient
    private int idwDay;
    @Transient
    private int idwHour;
    @Transient
    private int idwMinute;
    @Transient
    private int idwSecond;
    @Transient
    private int iParams1;
    @Transient
    private int iParams2;
    @Transient
    private int iParams3;
    @Transient
    private int iParams4;
    @Transient
    private int idwTMachineNumber;
    @Transient
    private int idwManipulation;

    public YvsGrhIoemDevice() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhIoemDevice(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public Long getEmploye() {
        return employe;
    }

    public void setEmploye(Long employe) {
        this.employe = employe;
    }

    public Date getDateTimeAction() {
        return dateTimeAction;
    }

    public void setDateTimeAction(Date dateTimeAction) {
        this.dateTimeAction = dateTimeAction;
    }

    public Integer getReserved() {
        return reserved != null ? reserved : 0;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    public Date getDateAction() {
        return dateAction;
    }

    public void setDateAction(Date dateAction) {
        this.dateAction = dateAction;
    }

    public Date getTimeAction() {
        return timeAction;
    }

    public void setTimeAction(Date timeAction) {
        this.timeAction = timeAction;
    }

    public Integer getVerifyMode() {
        return verifyMode != null ? verifyMode : 0;
    }

    public void setVerifyMode(Integer verifyMode) {
        this.verifyMode = verifyMode;
    }

    public Integer getInOutMode() {
        return inOutMode != null ? inOutMode : 0;
    }

    public void setInOutMode(Integer inOutMode) {
        this.inOutMode = inOutMode;
    }

    public Integer getWorkCode() {
        return workCode != null ? workCode : 0;
    }

    public void setWorkCode(Integer workCode) {
        this.workCode = workCode;
    }

    public int getiParams1() {
        return iParams1;
    }

    public void setiParams1(int iParams1) {
        this.iParams1 = iParams1;
    }

    public int getiParams2() {
        return iParams2;
    }

    public void setiParams2(int iParams2) {
        this.iParams2 = iParams2;
    }

    public int getiParams3() {
        return iParams3;
    }

    public void setiParams3(int iParams3) {
        this.iParams3 = iParams3;
    }

    public int getiParams4() {
        return iParams4;
    }

    public void setiParams4(int iParams4) {
        this.iParams4 = iParams4;
    }

    public int getIdwTMachineNumber() {
        return idwTMachineNumber;
    }

    public void setIdwTMachineNumber(int idwTMachineNumber) {
        this.idwTMachineNumber = idwTMachineNumber;
    }

    public int getIdwManipulation() {
        return idwManipulation;
    }

    public void setIdwManipulation(int idwManipulation) {
        this.idwManipulation = idwManipulation;
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

    public YvsPointeuse getPointeuse() {
        return pointeuse;
    }

    public void setPointeuse(YvsPointeuse pointeuse) {
        this.pointeuse = pointeuse;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isInclure() {
        return inclure;
    }

    public void setInclure(boolean inclure) {
        this.inclure = inclure;
    }

    public boolean isIcorrect() {
        return icorrect;
    }

    public void setIcorrect(boolean icorrect) {
        this.icorrect = icorrect;
    }

    public int getIdwYear() {
        return idwYear;
    }

    public void setIdwYear(int idwYear) {
        this.idwYear = idwYear;
    }

    public int getIdwMonth() {
        return idwMonth;
    }

    public void setIdwMonth(int idwMonth) {
        this.idwMonth = idwMonth;
    }

    public int getIdwDay() {
        return idwDay;
    }

    public void setIdwDay(int idwDay) {
        this.idwDay = idwDay;
    }

    public int getIdwHour() {
        return idwHour;
    }

    public void setIdwHour(int idwHour) {
        this.idwHour = idwHour;
    }

    public int getIdwMinute() {
        return idwMinute;
    }

    public void setIdwMinute(int idwMinute) {
        this.idwMinute = idwMinute;
    }

    public int getIdwSecond() {
        return idwSecond;
    }

    public void setIdwSecond(int idwSecond) {
        this.idwSecond = idwSecond;
    }

    public YvsGrhEmployes getEmployes() {
        return employes;
    }

    public void setEmployes(YvsGrhEmployes employes) {
        this.employes = employes;
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
        if (!(object instanceof YvsGrhIoemDevice)) {
            return false;
        }
        YvsGrhIoemDevice other = (YvsGrhIoemDevice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.presence.YvsGrhIoemDevice[ id=" + id + " ]";
    }

    public String action() {
        switch (getInOutMode()) {
            case 1:
                return "check_out.png";
            case 2:
                return "break_out.png";
            case 3:
                return "break_in.png";
            case 4:
                return "override_in.png";
            case 5:
                return "override_out.png";
            default:
                return "check_in.png";
        }
    }

    public String action_title() {
        switch (getInOutMode()) {
            case 1:
                return "Sortie";
            case 2:
                return "Sortie en pause";
            case 3:
                return "Retour de pause";
            case 4:
                return "Annuler entrée";
            case 5:
                return "Annuler sortie";
            default:
                return "Entrée";
        }
    }
}
