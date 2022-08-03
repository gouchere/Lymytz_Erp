/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author hp Elite 8300
 */
public class ParamDate {

    private Date debutPrec;
    private Date finPrec;
    private Date debut = new Date();
    private Date fin = new Date();
    private String periode;

    public ParamDate() {
    }

    public ParamDate(Date debut, Date fin, String periode) {
        this.debut = debut;
        this.fin = fin;
        this.periode = periode;
    }

    public Date getDebutPrec() {
        int ecart = Util.ecartOnDate(debut, fin, "jour") - 1;
        Calendar c = Calendar.getInstance();
        c.setTime(getFinPrec());
        c.add(Calendar.DAY_OF_MONTH, -ecart);
        debutPrec = c.getTime();
        return debutPrec;
    }

    public void setDebutPrec(Date debutPrec) {
        this.debutPrec = debutPrec;
    }

    public Date getFinPrec() {
        Calendar c = Calendar.getInstance();
        c.setTime(debut);
        c.add(Calendar.DAY_OF_MONTH, -1);
        finPrec = c.getTime();
        return finPrec;
    }

    public void setFinPrec(Date finPrec) {
        this.finPrec = finPrec;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

}
