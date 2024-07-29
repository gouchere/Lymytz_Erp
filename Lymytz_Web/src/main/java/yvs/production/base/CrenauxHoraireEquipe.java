/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.base;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.presence.TrancheHoraire;
import yvs.users.Users;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class CrenauxHoraireEquipe implements Serializable {

    private long id;
    private EquipeProduction equipe = new EquipeProduction();
    private TrancheHoraire tranche = new TrancheHoraire();
    private Users users = new Users();
    private SiteProduction site = new SiteProduction();
    private boolean actif = true, permanent;
    private Date dateTravail = new Date();
    private Date dateSave = new Date();

    public CrenauxHoraireEquipe() {
    }

    public CrenauxHoraireEquipe(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EquipeProduction getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeProduction equipe) {
        this.equipe = equipe;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public Date getDateTravail() {
        return dateTravail;
    }

    public void setDateTravail(Date dateTravail) {
        this.dateTravail = dateTravail;
    }

    public TrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(TrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public SiteProduction getSite() {
        return site;
    }

    public void setSite(SiteProduction site) {
        this.site = site;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final CrenauxHoraireEquipe other = (CrenauxHoraireEquipe) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
