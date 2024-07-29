/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat;

import java.util.Objects;

/**
 *
 * @author hp Elite 8300
 */
public class SignatureRapport {

    private Long id;
    private String titre1;
    private String titre2;
    private String titre3;
    private String titre4;
    private String titre5;

    public SignatureRapport() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre1() {
        return titre1;
    }

    public void setTitre1(String titre1) {
        this.titre1 = titre1;
    }

    public String getTitre2() {
        return titre2;
    }

    public void setTitre2(String titre2) {
        this.titre2 = titre2;
    }

    public String getTitre3() {
        return titre3;
    }

    public void setTitre3(String titre3) {
        this.titre3 = titre3;
    }

    public String getTitre4() {
        return titre4;
    }

    public void setTitre4(String titre4) {
        this.titre4 = titre4;
    }

    public String getTitre5() {
        return titre5;
    }

    public void setTitre5(String titre5) {
        this.titre5 = titre5;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final SignatureRapport other = (SignatureRapport) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
