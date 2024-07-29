/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.contrat;

import yvs.grh.paie.RubriqueBulletin;

/**
 *
 * @author hp Elite 8300 modélise les lignes d'indemnité enregistrée lors de la
 * suspension d'un contrat
 */
public class ElementIndemnite {

    private long id;
    private String libelle;
    private boolean retenue;
    private RubriqueIndemnite rubrique = new RubriqueIndemnite();
    private double base = 1, taux = 1, quantite = 1;
    private LibelleDroitFinContrat lib = new LibelleDroitFinContrat();

    public ElementIndemnite() {
    }

    public ElementIndemnite(long id, String libelle, RubriqueIndemnite rubrique) {
        this.id = id;
        this.libelle = libelle;
        this.rubrique = rubrique;
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

    public RubriqueIndemnite getRubrique() {
        return rubrique;
    }

    public void setRubrique(RubriqueIndemnite rubrique) {
        this.rubrique = rubrique;
    }

    public double getBase() {
        return base;
    }

    public void setBase(double base) {
        this.base = base;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public LibelleDroitFinContrat getLib() {
        return lib;
    }

    public void setLib(LibelleDroitFinContrat lib) {
        this.lib = lib;
    }

    public boolean isRetenue() {
        return retenue;
    }

    public void setRetenue(boolean retenue) {
        this.retenue = retenue;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ElementIndemnite other = (ElementIndemnite) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
