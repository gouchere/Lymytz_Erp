/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.parametre;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.personnel.YvsGrhDetailsChomageTechnique;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class SerieChomageTech implements Serializable {

    private long id;
    private String titre;
    private Date dateDebut;
    private Date dateFin;
    private ParamsTauxChomageTechnique typeCongeTech;
    private char statut;

    private List<YvsGrhDetailsChomageTechnique> detailsChamageEmp;

    public SerieChomageTech() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public ParamsTauxChomageTechnique getTypeCongeTech() {
        return typeCongeTech;
    }

    public void setTypeCongeTech(ParamsTauxChomageTechnique typeCongeTech) {
        this.typeCongeTech = typeCongeTech;
    }

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public List<YvsGrhDetailsChomageTechnique> getDetailsChamageEmp() {
        return detailsChamageEmp;
    }

    public void setDetailsChamageEmp(List<YvsGrhDetailsChomageTechnique> detailsChamageEmp) {
        this.detailsChamageEmp = detailsChamageEmp;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final SerieChomageTech other = (SerieChomageTech) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
