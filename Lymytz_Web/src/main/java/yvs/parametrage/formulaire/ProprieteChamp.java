/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.formulaire;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ProprieteChamp implements Serializable {

    private long id;
    private Date dateUpdate = new Date();
    private Date dateSave = new Date();
    private boolean visible;
    private boolean obligatoire;
    private boolean editable;
    private ChampFormulaire champ = new ChampFormulaire();
    private ModelFormulaire modele = new ModelFormulaire();

    public ProprieteChamp() {
    }

    public ProprieteChamp(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isObligatoire() {
        return obligatoire;
    }

    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public ChampFormulaire getChamp() {
        return champ;
    }

    public void setChamp(ChampFormulaire champ) {
        this.champ = champ;
    }

    public ModelFormulaire getModele() {
        return modele;
    }

    public void setModele(ModelFormulaire modele) {
        this.modele = modele;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ProprieteChamp other = (ProprieteChamp) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
