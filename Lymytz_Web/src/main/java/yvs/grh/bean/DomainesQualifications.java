/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class DomainesQualifications implements Serializable {

    private long id;
    private String titreDomaine;
    private List<Qualification> qualification;

    public DomainesQualifications() {
        qualification = new ArrayList<>();
    }

    public DomainesQualifications(long id) {
        this.id = id;
        qualification = new ArrayList<>();
    }

    public DomainesQualifications(long id, String titre) {
        this.id = id;
        this.titreDomaine = titre;
        qualification = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitreDomaine() {
        return titreDomaine;
    }

    public void setTitreDomaine(String titreDomaine) {
        this.titreDomaine = titreDomaine;
    }

    public List<Qualification> getQualification() {
        return qualification;
    }

    public void setQualification(List<Qualification> qualification) {
        this.qualification = qualification;
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
        final DomainesQualifications other = (DomainesQualifications) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
