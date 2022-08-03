/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.grh.statistique;

/**
 *
 * @author hp Elite 8300
 */
public class TauxContribution {
    private long id;
    private String libelle;
    private double taux;
    private Etats etat;

    public TauxContribution() {
    }

    public TauxContribution(long id, String libelle, double taux, Etats etat) {
        this.id = id;
        this.libelle = libelle;
        this.taux = taux;
        this.etat = etat;
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

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public Etats getEtat() {
        return etat;
    }

    public void setEtat(Etats etat) {
        this.etat = etat;
    }
    
    
}
