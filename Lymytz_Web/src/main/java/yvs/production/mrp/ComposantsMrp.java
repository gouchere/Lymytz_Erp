/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.mrp;

import yvs.production.technique.ComposantNomenclature;
import yvs.production.planification.DetailPlan;


/**
 *
 * @author hp Elite 8300
 * cette classe reproduit le même rôle que la classe ComposantNomenclature pour le MRP.
 * ell Contient pour chaque composant représenté par des instances de la classe DetailPlan
 * la quantité du dit composant qui représente le besoin.
 */
public class ComposantsMrp {

    private long id;
    private DetailPlan pdp;
    private ComposantNomenclature nomenclature;
    private double quantite;

    public ComposantsMrp() {
    }

    public ComposantsMrp(DetailPlan pdp, ComposantNomenclature nomenclature, double quantite) {
        this.pdp = pdp;
        this.nomenclature = nomenclature;
        this.quantite = quantite;
    }

    public DetailPlan getPdp() {
        return pdp;
    }

    public void setPdp(DetailPlan pdp) {
        this.pdp = pdp;
    }

    public ComposantNomenclature getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(ComposantNomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

}
