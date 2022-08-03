/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.depot;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.parametrage.entrepot.Depots;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PointVenteDepot implements Serializable {

    private long id;
    private boolean actif, principal;
    private PointVente pointVente = new PointVente();
    private Depots depot = new Depots();
    private Date dateSave = new Date();
    private boolean select, update, new_;

    public PointVenteDepot() {
    }

    public PointVenteDepot(long id) {
        this.id = id;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
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

    public PointVente getPointVente() {
        return pointVente;
    }

    public void setPointVente(PointVente pointVente) {
        this.pointVente = pointVente;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PointVenteDepot other = (PointVenteDepot) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
