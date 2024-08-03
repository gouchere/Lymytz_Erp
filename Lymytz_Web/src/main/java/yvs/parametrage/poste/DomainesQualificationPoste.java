/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.poste;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class DomainesQualificationPoste implements Serializable {

    private long id;
    private String titreDomaine;
    private List<QualificationPoste> qualifications;

    public DomainesQualificationPoste() {
        qualifications = new ArrayList<>();
    }

    public DomainesQualificationPoste(long id) {
        this.id = id;
        qualifications = new ArrayList<>();
    }
    public DomainesQualificationPoste(long id, String titre) {
        this.id = id;
        this.titreDomaine = titre;
        qualifications = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitreDomaine() {
        return titreDomaine;
    }

    public void setTitreDomaine(String titreDomaine) {
        this.titreDomaine = titreDomaine;
    }

    public List<QualificationPoste> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<QualificationPoste> qualifications) {
        this.qualifications = qualifications;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final DomainesQualificationPoste other = (DomainesQualificationPoste) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
