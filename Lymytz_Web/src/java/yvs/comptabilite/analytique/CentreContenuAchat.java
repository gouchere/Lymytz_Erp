/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.analytique;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.achat.ContenuDocAchat;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class CentreContenuAchat extends SectionAnalytique implements Serializable {

    private ContenuDocAchat contenu = new ContenuDocAchat();

    public CentreContenuAchat() {
    }

    public CentreContenuAchat(long id) {
        super(id);
    }

    public ContenuDocAchat getContenu() {
        return contenu;
    }

    public void setContenu(ContenuDocAchat contenu) {
        this.contenu = contenu;
    }

}
