/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.mrp;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class Mrp implements Serializable{

    private long id;
    private String reference;
    private List<ComposantsMrp> listCOmposantMrp;
    private Date dateRef = new Date();

    public Mrp() {
        listCOmposantMrp = new ArrayList<>();
    }

    public Mrp(long id, String reference) {
        this.id = id;
        this.reference = reference;
    }

    public List<ComposantsMrp> getListCOmposantMrp() {
        return listCOmposantMrp;
    }

    public void setListCOmposantMrp(List<ComposantsMrp> listCOmposantMrp) {
        this.listCOmposantMrp = listCOmposantMrp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDateRef() {
        return dateRef;
    }

    public void setDateRef(Date dateRef) {
        this.dateRef = dateRef;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Mrp other = (Mrp) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
