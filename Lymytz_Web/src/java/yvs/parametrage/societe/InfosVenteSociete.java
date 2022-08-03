/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.societe;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.TypeCout;

/**
 *
 * @author Lymytz Dowes
 */
@SessionScoped
@ManagedBean
public class InfosVenteSociete implements Serializable {

    private long id;
    private boolean displayCatalogueOnList;
    private TypeCout typeLivraison = new TypeCout();
    private Date dateSave = new Date();

    public InfosVenteSociete() {
    }

    public InfosVenteSociete(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDisplayCatalogueOnList() {
        return displayCatalogueOnList;
    }

    public void setDisplayCatalogueOnList(boolean displayCatalogueOnList) {
        this.displayCatalogueOnList = displayCatalogueOnList;
    }

    public TypeCout getTypeLivraison() {
        return typeLivraison;
    }

    public void setTypeLivraison(TypeCout typeLivraison) {
        this.typeLivraison = typeLivraison;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final InfosVenteSociete other = (InfosVenteSociete) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
