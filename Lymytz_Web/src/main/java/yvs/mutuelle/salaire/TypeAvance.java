/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.salaire;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.credit.TypeCredit;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class TypeAvance extends TypeCredit implements Serializable {

    public TypeAvance() {
    }

    public TypeAvance(long id) {
        super(id);
    }

}
