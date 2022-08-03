/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification.view;

import java.util.List;

/**
 *
 * @author hp Elite 8300
 */
public class DataLigne {

    private FabriqueColonne fabriqueColone=new FabriqueColonne();
    private List colones;

    public DataLigne(FabriqueColonne fabriqueColone) {
        this.fabriqueColone = fabriqueColone;
    }

    public FabriqueColonne getFabriqueColone() {
        return fabriqueColone;
    }

    public void setFabriqueColone(FabriqueColonne fabriqueColone) {
        this.fabriqueColone = fabriqueColone;
    }

    public void setColones(List colones) {
        this.colones = colones;
    }

    public List getColones() {
        return colones;
    }

//    public DataLigne() {
//        details = new ArrayList<>();
//    }
//
//    public List<DataColone> getDetails() {
//        return details;
//    }
//
//    public void setDetails(List<DataColone> details) {
//        this.details = details;
//    }
    public DataLigne() {
    }
}
