/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.theme;

/**
 *
 * @author LYMYTZ
 */
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.base.YvsBaseFournisseur;

@ManagedBean(name = "fournisseurService", eager = true)
@SessionScoped
public class FournisseurService implements Serializable{

    private List<YvsBaseFournisseur> fournisseurs;

    public FournisseurService() {
        fournisseurs = new ArrayList<>();
    }

    public FournisseurService(List<YvsBaseFournisseur> clients) {
        this.fournisseurs = clients;
    }

    public List<YvsBaseFournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<YvsBaseFournisseur> clients) {
        this.fournisseurs = clients;
    }
}
