/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.contrat;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hp Elite 8300
 */
public class RubriqueIndemnite {

    private long id;
    private String code, designation;
    private int ordre;
    private List<ElementIndemnite> indemnites;

    public RubriqueIndemnite() {
        indemnites = new ArrayList<>();
    }

    public RubriqueIndemnite(long id, String designation, int ordre) {
        this.id = id;
        this.designation = designation;
        this.ordre = ordre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public List<ElementIndemnite> getIndemnites() {
        return indemnites;
    }

    public void setIndemnites(List<ElementIndemnite> indemnites) {
        this.indemnites = indemnites;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final RubriqueIndemnite other = (RubriqueIndemnite) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
