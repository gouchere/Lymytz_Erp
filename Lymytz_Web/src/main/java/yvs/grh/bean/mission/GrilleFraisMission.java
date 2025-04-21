/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean.mission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Comptes;
import yvs.entity.grh.activite.YvsGrhDetailGrilleFraiMission;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class GrilleFraisMission implements Serializable {

    private long id;
    private boolean actif = true;
    private List<YvsGrhDetailGrilleFraiMission> detailsFrais;
    private String categorie;
    private String titre;
    private Date dateSave = new Date();
    private Comptes compteCharge = new Comptes();

    public GrilleFraisMission() {
        detailsFrais = new ArrayList<>();
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsGrhDetailGrilleFraiMission> getDetailsFrais() {
        return detailsFrais;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setDetailsFrais(List<YvsGrhDetailGrilleFraiMission> detailsFrais) {
        this.detailsFrais = detailsFrais;
    }

    public GrilleFraisMission(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Comptes getCompteCharge() {
        return compteCharge;
    }

    public void setCompteCharge(Comptes compteCharge) {
        this.compteCharge = compteCharge;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final GrilleFraisMission other = (GrilleFraisMission) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
