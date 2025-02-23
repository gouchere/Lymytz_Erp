/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.salaire.YvsGrhElementStructure;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class StructureElementSalaire implements Serializable {

    private long id;
    private String nom, code, description;
    private Date dateSave = new Date();
    private List<YvsGrhElementStructure> listElement;

    public StructureElementSalaire() {
        listElement = new ArrayList<>();
    }

    public StructureElementSalaire(long id) {
        this.id = id;
    }

    public StructureElementSalaire(long id, String nom, String code) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        listElement = new ArrayList<>();
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<YvsGrhElementStructure> getListElement() {
        return listElement;
    }

    public void setListElement(List<YvsGrhElementStructure> listElement) {
        this.listElement = listElement;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final StructureElementSalaire other = (StructureElementSalaire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StructureElementSalaire{" + "id=" + id + '}';
    }

}
