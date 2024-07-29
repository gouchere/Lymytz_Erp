/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.tresorerie;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.stock.CoutSupDoc;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class CoutSupDocDivers extends CoutSupDoc implements Serializable {

    private boolean lierTiers;
    private DocCaissesDivers docDivers = new DocCaissesDivers();

    public CoutSupDocDivers() {
    }

    public CoutSupDocDivers(int id) {
        super(id);
    }

    public boolean isLierTiers() {
        return lierTiers;
    }

    public void setLierTiers(boolean lierTiers) {
        this.lierTiers = lierTiers;
    }

    public DocCaissesDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(DocCaissesDivers docDivers) {
        this.docDivers = docDivers;
    }

}
