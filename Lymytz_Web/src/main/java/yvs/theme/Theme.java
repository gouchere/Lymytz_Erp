/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.theme;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;

/**
 *
 * @author LYMYTZ
 */
public class Theme implements Serializable {

    private int id;
    private String displayName;
    private String name;

    public Theme() {
    }

    public Theme(int id) {
        this.id = id;
    }

    public Theme(int id, String displayName, String name) {
        this.id = id;
        this.displayName = displayName;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.id;
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
        final Theme other = (Theme) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
