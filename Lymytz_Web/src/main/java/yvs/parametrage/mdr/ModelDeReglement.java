/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.mdr;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.BeanDeBase;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "mdr")
@SessionScoped
public class ModelDeReglement extends BeanDeBase implements Serializable {

    private int id;
    private String codeMdr;
    private String designation;
    private String description;
    private List<TraiteMdr> listTraite; 

    public ModelDeReglement() {
        listTraite = new ArrayList<>();
    }

    public ModelDeReglement(int id) {
        this.id = id;
        listTraite = new ArrayList<>();
    }

    public ModelDeReglement(int id, String designation) {
        this.id = id;
        this.designation = designation;
        listTraite = new ArrayList<>();
    }

    public ModelDeReglement(String codeMdr, String designation) {
        this.codeMdr = codeMdr;
        this.designation = designation;
        listTraite = new ArrayList<>();
    }

    public ModelDeReglement(int id, String codeMdr, String designation) {
        this.id = id;
        this.codeMdr = codeMdr;
        this.designation = designation;
        listTraite = new ArrayList<>();
    }

    public String getCodeMdr() {
        return codeMdr;
    }

    public void setCodeMdr(String codeMdr) {
        this.codeMdr = codeMdr;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TraiteMdr> getListTraite() {
        return listTraite;
    }

    public void setListTraite(List<TraiteMdr> listTraite) {
        this.listTraite = listTraite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModelDeReglement other = (ModelDeReglement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
