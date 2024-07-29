/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.creneau;

import java.io.Serializable;import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.depot.PointVente;
import yvs.grh.JoursOuvres;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.entrepot.Depots;
import yvs.users.Users;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Creneau implements Serializable, Comparable {

    private long id;
    private boolean actif = true;
    private JoursOuvres jour = new JoursOuvres();
    private TrancheHoraire tranche = new TrancheHoraire();
    private String critere, critere_;
    private Depots depot = new Depots();
    private PointVente point = new PointVente();
    private String reference, semiReference;
    private Date dateSave = new Date();
    private boolean selectActif, new_, permanent;
    private Users users = new Users();

    public Creneau() {
    }

    public Creneau(long id) {
        this.id = id;
    }

    public PointVente getPoint() {
        return point;
    }

    public void setPoint(PointVente point) {
        this.point = point;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public String getCritere_() {
        return critere_;
    }

    public void setCritere_(String critere_) {
        this.critere_ = critere_;
    }

    public String getCritere() {
        return critere != null ? critere : "";
    }

    public void setCritere(String critere) {
        this.critere = critere;
    }

    public String getSemiReference() {
        return semiReference;
    }

    public void setSemiReference(String semiReference) {
        this.semiReference = semiReference;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public JoursOuvres getJour() {
        return jour;
    }

    public void setJour(JoursOuvres jour) {
        this.jour = jour;
    }

    public TrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(TrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Creneau other = (Creneau) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        Creneau c = (Creneau) o;
        if (depot.equals(c.depot)) {
            if (jour.equals(c.jour)) {
                if (tranche.equals(c.tranche)) {
                    return Long.valueOf(id).compareTo(c.id);
                }
//                return tranche.compareTo(c.tranche);
            }
//            return jour.compareTo(c.jour);
        }
        return depot.compareTo(c.depot);
    }

}
