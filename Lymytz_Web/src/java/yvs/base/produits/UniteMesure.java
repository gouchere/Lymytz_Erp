/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.base.YvsBaseTableConversion;

/**
 *
 * @author lymytz
 */ 
@ManagedBean
@SessionScoped
public class UniteMesure implements Serializable {

    private long id;
    private String reference, type;
    private Date dateSave = new Date();
    private String libelle, description;
    private List<YvsBaseTableConversion> equivalences;
    private boolean select, list, error, defaut;

    public UniteMesure() {
        equivalences = new ArrayList<>();
    }

    public UniteMesure(long id) {
        this();
        this.id = id;
    }

    public UniteMesure(String reference) {
        this();
        this.reference = reference;
    }

    public UniteMesure(String reference, String libelle) {
        this(reference);
        this.libelle = libelle;
    }

    public UniteMesure(long id, String type) {
        this(id);
        this.type = type;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isSelect() {
        return select;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : "M" : "M";
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<YvsBaseTableConversion> getEquivalences() {
        return equivalences;
    }

    public void setEquivalences(List<YvsBaseTableConversion> equivalences) {
        this.equivalences = equivalences;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final UniteMesure other = (UniteMesure) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
