/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PosteOperation implements Serializable {

    private int id;
    private int nombre = 1;
    private String typeCharge;
    private PosteCharge poste = new PosteCharge();
    private boolean selectActif;
    private Date dateSave = new Date();

    public PosteOperation() {
        nombre = 1;
        typeCharge = Constantes.PROD_OP_TYPE_CHARGE_MACHINE;
    }

    public PosteOperation(int id) {
        this.id = id;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNombre() {
        return nombre;
    }

    public PosteCharge getPoste() {
        return poste;
    }

    public void setPoste(PosteCharge poste) {
        this.poste = poste;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public String getTypeCharge() {
        return typeCharge != null ? typeCharge.trim().length() > 0 ? typeCharge : Constantes.PROD_OP_TYPE_CHARGE_MACHINE : Constantes.PROD_OP_TYPE_CHARGE_MACHINE;
    }

    public void setTypeCharge(String typeCharge) {
        this.typeCharge = typeCharge;
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
        hash = 53 * hash + this.id;
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
        final PosteOperation other = (PosteOperation) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return poste.getDesignation() + " (" + nombre + ")";
    }

}
