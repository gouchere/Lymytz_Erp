/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@SessionScoped
@ManagedBean
public class GroupeCatC {

    private String categorie;
    private String compte;
    private String taxe1;
    private String taxe2;
    private String taxe3;
    private long idCategorie;
    private long idCompte;
    private long idTaxe1;
    private long idTaxe2;
    private long idTaxe3;

    public GroupeCatC() {
    }

    public GroupeCatC(String categorie) {
        this.categorie = categorie;
    }

    public GroupeCatC(String categorie, String compte, String taxe1, String taxe2, String taxe3) {
        this.categorie = categorie;
        this.compte = compte;
        this.taxe1 = taxe1;
        this.taxe2 = taxe2;
        this.taxe3 = taxe3;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getCompte() {
        return compte;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public String getTaxe1() {
        return taxe1;
    }

    public void setTaxe1(String taxe1) {
        this.taxe1 = taxe1;
    }

    public String getTaxe2() {
        return taxe2;
    }

    public void setTaxe2(String taxe2) {
        this.taxe2 = taxe2;
    }

    public String getTaxe3() {
        return taxe3;
    }

    public void setTaxe3(String taxe3) {
        this.taxe3 = taxe3;
    }

    public long getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(long idCategorie) {
        this.idCategorie = idCategorie;
    }

    public long getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(long idCompte) {
        this.idCompte = idCompte;
    }

    public long getIdTaxe1() {
        return idTaxe1;
    }

    public void setIdTaxe1(long idTaxe1) {
        this.idTaxe1 = idTaxe1;
    }

    public long getIdTaxe2() {
        return idTaxe2;
    }

    public void setIdTaxe2(long idTaxe2) {
        this.idTaxe2 = idTaxe2;
    }

    public long getIdTaxe3() {
        return idTaxe3;
    }

    public void setIdTaxe3(long idTaxe3) {
        this.idTaxe3 = idTaxe3;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GroupeCatC other = (GroupeCatC) obj;
        if (!Objects.equals(this.categorie, other.categorie)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.categorie);
        return hash;
    }
}
