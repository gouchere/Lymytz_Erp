/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.poste;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;

/**
 *
 * @author LYMYTZ-PC modélise les missions à remplir et les activité à effectuer
 * à un poste
 */
public class MissionPoste implements Serializable {

    private long id;
    private String libelle;

    public MissionPoste() {
    }

    public MissionPoste(long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

}
