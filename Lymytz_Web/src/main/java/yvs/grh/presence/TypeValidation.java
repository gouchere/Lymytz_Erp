/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.presence;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.TypeContrat;

/**
 *
 * @author Lymytz
 */
@ManagedBean
@SessionScoped
public class TypeValidation extends TypeContrat implements Serializable {

    private long id;

    public TypeValidation() {
        super();
    }

    public TypeValidation(long id) {
        super(id);
    }

}
