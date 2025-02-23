/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rations;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ParamRationSuspension implements Serializable {

    private long id;
    private ParamRations personnel = new ParamRations();
    private Date debutSuspension = new Date();
    private Date finSuspension = new Date();
    private Date dateSave = new Date();

    public ParamRationSuspension() {

    }

    public ParamRationSuspension(long id) {
        this();
        this.id = id;
    }

    public ParamRationSuspension(Date debutSuspension, Date finSuspension) {
        this();
        this.debutSuspension = debutSuspension;
        this.finSuspension = finSuspension;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ParamRations getPersonnel() {
        return personnel;
    }

    public void setPersonnel(ParamRations personnel) {
        this.personnel = personnel;
    }

    public Date getDebutSuspension() {
        return debutSuspension;
    }

    public void setDebutSuspension(Date debutSuspension) {
        this.debutSuspension = debutSuspension;
    }

    public Date getFinSuspension() {
        return finSuspension;
    }

    public void setFinSuspension(Date finSuspension) {
        this.finSuspension = finSuspension;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ParamRationSuspension other = (ParamRationSuspension) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
