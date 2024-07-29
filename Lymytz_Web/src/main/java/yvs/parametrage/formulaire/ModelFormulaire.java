/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.formulaire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.param.formulaire.YvsParamProprieteChamp;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ModelFormulaire implements Serializable {

    private long id;
    private Date dateUpdate = new Date();
    private Date dateSave = new Date();
    private String intitule;
    private Formulaire formulaire = new Formulaire();
    private List<YvsParamProprieteChamp> composants;

    public ModelFormulaire() {
        composants = new ArrayList<>();
    }

    public ModelFormulaire(long id) {
        this();
        this.id = id;
    }

    public Formulaire getFormulaire() {
        return formulaire;
    }

    public void setFormulaire(Formulaire formulaire) {
        this.formulaire = formulaire;
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

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public List<YvsParamProprieteChamp> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsParamProprieteChamp> composants) {
        this.composants = composants;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ModelFormulaire other = (ModelFormulaire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
