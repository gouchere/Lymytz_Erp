/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.recrutement;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import yvs.grh.bean.Employe;
import yvs.grh.recrutement.entretien.Candidats;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class EntretienCandidat extends DynaFormModel implements Serializable {

    private long id;
    private Date date = new Date();
    private String lieu;
    private Employe examinateur = new Employe();
    private Candidats candidat = new Candidats();
    private boolean save;   //marque les entretien déjà enregistré

    public EntretienCandidat() {
    }

    public EntretienCandidat(long id, String lieu) {
        this.id = id;
        this.lieu = lieu;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Employe getExaminateur() {
        return examinateur;
    }

    public void setExaminateur(Employe examinateur) {
        this.examinateur = examinateur;
    }

    public Candidats getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidats candidat) {
        this.candidat = candidat;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final EntretienCandidat other = (EntretienCandidat) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
