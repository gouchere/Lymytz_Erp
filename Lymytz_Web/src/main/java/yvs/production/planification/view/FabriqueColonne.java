/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification.view;

import java.util.ArrayList;
import java.util.List;
import yvs.production.planification.view.DataColone2D;
import yvs.production.planification.view.DataLigne2D;

/**
 *
 * @author hp Elite 8300
 */
public class FabriqueColonne {

    public FabriqueColonne() {
    }

    public List getColone(String type) {
        if ("2D".equals(type)) {
            List<DataColone2D> l = new ArrayList<>();
            return l;
        }
        if ("L2D".equals(type)) {
            List<DataLigne2D> l = new ArrayList<>();
            return l;
        }
        if ("L3D".equals(type)) {
            List<ObjectMrp> l = new ArrayList<>();
            return l;
        }
        return null;
    }

}
