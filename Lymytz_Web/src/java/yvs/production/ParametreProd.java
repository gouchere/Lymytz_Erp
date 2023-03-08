/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.dao.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean()
@SessionScoped
public class ParametreProd implements Serializable {

    private int id;
    private int converter = 0;
    private int converterPF = 0;
    private boolean suiviOprequis;
    private boolean closeDeclAuto;
    private boolean numCmdeRequis;
    private boolean declarationProportionnel;
    private boolean declareWhenFinishOf;
    private int limiteVuOf = 1;
    private int limiteCreateOf = 1;
    private String valoriserBy = "V";

    private Date dateSave = new Date();

    public ParametreProd() {
    }

    public ParametreProd(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValoriserBy() {
        return Util.asString(valoriserBy) ? valoriserBy : "V";
    }

    public void setValoriserBy(String valoriserBy) {
        this.valoriserBy = valoriserBy;
    }

    public int getConverter() {
        return converter;
    }

    public void setConverter(int converter) {
        this.converter = converter;
    }

    public boolean isSuiviOprequis() {
        return suiviOprequis;
    }

    public void setSuiviOprequis(boolean suiviOprequis) {
        this.suiviOprequis = suiviOprequis;
    }

    public void setNumCmdeRequis(boolean numCmdeRequis) {
        this.numCmdeRequis = numCmdeRequis;
    }

    public boolean isNumCmdeRequis() {
        return numCmdeRequis;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public int getLimiteVuOf() {
        return limiteVuOf;
    }

    public void setLimiteVuOf(int limiteVuOf) {
        this.limiteVuOf = limiteVuOf;
    }

    public int getLimiteCreateOf() {
        return limiteCreateOf;
    }

    public void setLimiteCreateOf(int limiteCreateOf) {
        this.limiteCreateOf = limiteCreateOf;
    }

    public boolean isCloseDeclAuto() {
        return closeDeclAuto;
    }

    public void setCloseDeclAuto(boolean closeDeclAuto) {
        this.closeDeclAuto = closeDeclAuto;
    }

    public boolean isDeclarationProportionnel() {
        return declarationProportionnel;
    }

    public void setDeclarationProportionnel(boolean declarationProportionnel) {
        this.declarationProportionnel = declarationProportionnel;
    }

    public int getConverterPF() {
        return converterPF;
    }

    public void setConverterPF(int converterPF) {
        this.converterPF = converterPF;
    }

    public boolean isDeclareWhenFinishOf() {
        return declareWhenFinishOf;
    }

    public void setDeclareWhenFinishOf(boolean declareWhenFinishOf) {
        this.declareWhenFinishOf = declareWhenFinishOf;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParametreProd other = (ParametreProd) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
