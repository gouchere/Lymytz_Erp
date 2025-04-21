/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.poste;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.Qualification;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class QualificationPoste implements Serializable {

    private long id;
    private Qualification qualification = new Qualification();
    private boolean rattache;

    public QualificationPoste() {
    }

    public QualificationPoste(long id) {
        this.id = id;
    }

    public QualificationPoste(long id, Qualification qualification) {
        this.id = id;
        this.qualification = qualification;
    }
    public QualificationPoste(long id, Qualification qualification, boolean rattache) {
        this.id = id;
        this.qualification = qualification;
        this.rattache=rattache;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public boolean isRattache() {
        return rattache;
    }

    public void setRattache(boolean rattache) {
        this.rattache = rattache;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final QualificationPoste other = (QualificationPoste) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QualificationPoste{" + "id=" + id + '}';
    }

}
