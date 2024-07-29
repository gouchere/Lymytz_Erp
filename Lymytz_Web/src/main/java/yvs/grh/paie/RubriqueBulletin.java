/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;

/**
 *
 * @author LYMYTZ-PC
 */
public class RubriqueBulletin implements Serializable {

    private long id;
    private String code, designation;    

    public RubriqueBulletin() {
    }

    public RubriqueBulletin(long id, String code, String designation) {
        this.id = id;
        this.code = code;
        this.designation = designation;
    }

    public RubriqueBulletin(String code, String designation) {
        this.code = code;
        this.designation = designation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final RubriqueBulletin other = (RubriqueBulletin) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RubriqueBulletin{" + "id=" + id + '}';
    }

}
