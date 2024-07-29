/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.presence;

import java.io.Serializable;
import java.util.Date;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsPointeuse;

/**
 *
 * @author Lymytz Dowes
 */
public class IOEMDevice implements Serializable, Comparable {

    public int id = 0;
    public int iMachineNumber = 0;
    public int idwTMachineNumber = 0;
    public int idwSEnrollNumber = 0;
    public int idwManipulation = 0;//Action --6 Register a fingerprint --7 Register a password --8 Register a ID Card
    public int iParams1 = 0; // ID Users
    public int iParams2 = 0; //Resutat od operation --0 Succes --#0 Echec
    public int iParams3 = 0; //Index fingerprint register
    public int iParams4 = 0; //Length of the fingerprint template
    public int idwYear = 0;
    public int idwMonth = 0;
    public int idwDay = 0;
    public int idwHour = 0;
    public int idwMinute = 0;
    public int idwSecond = 0;
    public int idwInOutMode = 0;//0—Check-In (default value) 1—Check-Out 2—Break-Out 3—Break-In 4—OT-In 5—OT-Out
    public boolean inclure = true;
    public boolean icorrect = false;
    public YvsPointeuse pointeuse = new YvsPointeuse();
    public YvsGrhEmployes employe = new YvsGrhEmployes();
    public Date date = new Date();
    public Date heure = new Date();

    public IOEMDevice() {
    }

    public IOEMDevice(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getiMachineNumber() {
        return iMachineNumber;
    }

    public void setiMachineNumber(int iMachineNumber) {
        this.iMachineNumber = iMachineNumber;
    }

    public int getIdwTMachineNumber() {
        return idwTMachineNumber;
    }

    public void setIdwTMachineNumber(int idwTMachineNumber) {
        this.idwTMachineNumber = idwTMachineNumber;
    }

    public int getIdwSEnrollNumber() {
        return idwSEnrollNumber;
    }

    public void setIdwSEnrollNumber(int idwSEnrollNumber) {
        this.idwSEnrollNumber = idwSEnrollNumber;
    }

    public int getIdwManipulation() {
        return idwManipulation;
    }

    public void setIdwManipulation(int idwManipulation) {
        this.idwManipulation = idwManipulation;
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

    public int getIdwInOutMode() {
        return idwInOutMode;
    }

    public void setIdwInOutMode(int idwInOutMode) {
        this.idwInOutMode = idwInOutMode;
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

    public YvsPointeuse getPointeuse() {
        return pointeuse;
    }

    public void setPointeuse(YvsPointeuse pointeuse) {
        this.pointeuse = pointeuse;
    }

    public YvsGrhEmployes getYvsGrhEmployes() {
        return employe;
    }

    public void setYvsGrhEmployes(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getHeure() {
        return heure;
    }

    public void setHeure(Date heure) {
        this.heure = heure;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public String action() {
        switch (idwInOutMode) {
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.id;
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
        final IOEMDevice other = (IOEMDevice) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        IOEMDevice m = (IOEMDevice) o;
        if (date.equals(m.date)) {
            return Integer.valueOf(id).compareTo(m.id);
        }
        return date.compareTo(m.date);
    }
}
