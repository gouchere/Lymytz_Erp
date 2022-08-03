/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

/**
 *
 * @author GOUCHERE YVES
 */
public class Telephones {

    private int id;
    private String telephone;

    public Telephones() {
    }

    public Telephones(int id, String telephone) {
        this.id = id;
        this.telephone = telephone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.id;
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
        final Telephones other = (Telephones) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
