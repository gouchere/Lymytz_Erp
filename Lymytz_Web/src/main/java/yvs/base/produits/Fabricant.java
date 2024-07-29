/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author Lymytz_Serveur
 */
public class Fabricant implements Serializable {

    private int id;
    private String codeFabricant, nomFabricant;
    private Dictionnaire pays = new Dictionnaire();

    public Fabricant() {
    }

    public Fabricant(int id, String codeFabricant, String nomFabricant) {
        this.id = id;
        this.codeFabricant = codeFabricant;
        this.nomFabricant = nomFabricant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeFabricant() {
        return codeFabricant;
    }

    public void setCodeFabricant(String codeFabricant) {
        this.codeFabricant = codeFabricant;
    }

    public String getNomFabricant() {
        return nomFabricant;
    }

    public void setNomFabricant(String nomFabricant) {
        this.nomFabricant = nomFabricant;
    }

    public Dictionnaire getPays() {
        return pays;
    }

    public void setPays(Dictionnaire pays) {
        this.pays = pays;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.id;
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
        final Fabricant other = (Fabricant) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
