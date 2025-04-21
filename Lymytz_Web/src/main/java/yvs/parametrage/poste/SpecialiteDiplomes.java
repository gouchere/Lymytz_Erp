/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.poste;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.Diplomes;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class SpecialiteDiplomes implements Serializable{

    private long id;
    private String codeInterne, designation;
    private List<Diplomes> diplomes;

    public SpecialiteDiplomes() {
        diplomes=new ArrayList<>();
    }

    public SpecialiteDiplomes(long id, String codeInterne, String designation) {
        this.id = id;
        this.codeInterne = codeInterne;
        this.designation = designation;
        diplomes=new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodeInterne() {
        return codeInterne;
    }

    public void setCodeInterne(String codeInterne) {
        this.codeInterne = codeInterne;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public List<Diplomes> getDiplomes() {
        return diplomes;
    }

    public void setDiplomes(List<Diplomes> diplomes) {
        this.diplomes = diplomes;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final SpecialiteDiplomes other = (SpecialiteDiplomes) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
