/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rrr;

import java.io.Serializable;import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.commercial.rrr.YvsComRistourne;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PlanRistourne implements Serializable {

    private long id;
    private String reference;
    private boolean actif = true, selectActif, new_, update;
    private Date dateSave = new Date();
    private List<YvsComRistourne> ristournes;

    public PlanRistourne() {
        ristournes = new ArrayList<>();
    }

    public PlanRistourne(long id) {
        this.id = id;
        ristournes = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
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

    public boolean isUpdate() {
        return id > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<YvsComRistourne> getRistournes() {
        return ristournes;
    }

    public void setRistournes(List<YvsComRistourne> ristournes) {
        this.ristournes = ristournes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PlanRistourne other = (PlanRistourne) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
}
