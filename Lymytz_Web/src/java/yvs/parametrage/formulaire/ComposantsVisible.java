/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.formulaire;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ComposantsVisible implements Serializable{

    public boolean fv_cat_compta = true, fv_model_reg = true, fv_adresse = true, fv_nom_client = true, fv_depot_liv = true, fv_date_liv = true, fv_tranche_liv = true, fv_depot_content = false, fv_conditionnement = true;

    public ComposantsVisible() {
    }

    public boolean isFv_cat_compta() {
        return fv_cat_compta;
    }

    public boolean isFv_model_reg() {
        return fv_model_reg;
    }

    public boolean isFv_adresse() {
        return fv_adresse;
    }

    public boolean isFv_nom_client() {
        return fv_nom_client;
    }

    public boolean isFv_depot_liv() {
        return fv_depot_liv;
    }

    public boolean isFv_date_liv() {
        return fv_date_liv;
    }

    public boolean isFv_tranche_liv() {
        return fv_tranche_liv;
    }

    public boolean isFv_depot_content() {
        return fv_depot_content;
    }

    public boolean isFv_conditionnement() {
        return fv_conditionnement;
    }

}
