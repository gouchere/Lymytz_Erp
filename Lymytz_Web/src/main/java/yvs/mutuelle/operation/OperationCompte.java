/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.operation;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.Compte;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class OperationCompte extends Operation implements Serializable {

    private Compte compte = new Compte();

    public OperationCompte() {
    }

    public OperationCompte(long id) {
        super(id);
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

}
