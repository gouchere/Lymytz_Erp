/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class JoursOuvres implements Serializable, Comparator<JoursOuvres> {

    private long id;
    private String jour;
    private Date heureDebutTravail = new Date();
    private Date heureFinTravail = new Date();
    private Date dureePause = new Date();
    private Date heureDebutPause = new Date();
    private Date heureFinPause = new Date();
    private boolean actif = true, ouvrable= true, vu = false;
    private String strDureePause;
    private Date dureeService = new Date();
    private Date dateSave = new Date();
    private short ordre;    //ipour le classement des journées
    private boolean jourDerepos;

    public JoursOuvres() {
    }

    public JoursOuvres(int id) {
        this.id = id;
    }

    public JoursOuvres(int id, String jour) {
        this.id = id;
        this.jour = jour;
    }

    public boolean isOuvrable() {
        return ouvrable;
    }

    public void setOuvrable(boolean ouvrable) {
        this.ouvrable = ouvrable;
    }

    public JoursOuvres(String jour) {
        this.jour = jour;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public Date getHeureDebutTravail() {
        return heureDebutTravail;
    }

    public void setHeureDebutTravail(Date heureDebutTravail) {
        this.heureDebutTravail = heureDebutTravail;
    }

    public Date getHeureFinTravail() {
        return heureFinTravail;
    }

    public void setHeureFinTravail(Date heureFinTravail) {
        this.heureFinTravail = heureFinTravail;
    }

    public Date getDureePause() {
        return dureePause;
    }

    public void setDureePause(Date dureePause) {
        this.dureePause = dureePause;
    }

    public Date getHeureDebutPause() {
        return heureDebutPause;
    }

    public void setHeureDebutPause(Date heureDebutPause) {
        this.heureDebutPause = heureDebutPause;
    }

    public Date getHeureFinPause() {
        return heureFinPause;
    }

    public void setHeureFinPause(Date heureFinPause) {
        this.heureFinPause = heureFinPause;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getStrDureePause() {
        return strDureePause;
    }

    public void setStrDureePause(String strDureePause) {
        this.strDureePause = strDureePause;
    }

    public short getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = (short) ordre;
    }

    public Date getDureeService() {
        return dureeService;
    }

    public void setDureeService(Date dureeService) {
        this.dureeService = dureeService;
    }

    public boolean isVu() {
        return vu;
    }

    public void setOrdre(short ordre) {
        this.ordre = ordre;
    }

    public void setVu(boolean vu) {
        this.vu = vu;
    }

    public boolean isJourDerepos() {
        return jourDerepos;
    }

    public void setJourDerepos(boolean jourDerepos) {
        this.jourDerepos = jourDerepos;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateSave() {
        return dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final JoursOuvres other = (JoursOuvres) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int compare(JoursOuvres o1, JoursOuvres o2) {
        if (o1 != null && o2 != null) {
            if (o1.getOrdre() > o2.getOrdre()) {
                return 1;
            } else if (o1.getOrdre() < o2.getOrdre()) {
                return -1;
            } else {
                return 0;
            }
        } else if (o1 != null) {
            return 1;
        } else {
            return -1;
        }
    }

}
