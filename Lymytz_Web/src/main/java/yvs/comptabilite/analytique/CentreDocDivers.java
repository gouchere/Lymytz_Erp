/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.analytique;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.comptabilite.tresorerie.DocCaissesDivers;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class CentreDocDivers extends SectionAnalytique implements Serializable {

    private DocCaissesDivers docDivers;

    public CentreDocDivers() {
    }

    public CentreDocDivers(long id) {
        super(id);
    }

    public DocCaissesDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(DocCaissesDivers docDivers) {
        this.docDivers = docDivers;
    }

}
