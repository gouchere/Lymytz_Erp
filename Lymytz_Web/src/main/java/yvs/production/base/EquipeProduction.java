/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.production.equipe.YvsProdMembresEquipe;
import yvs.grh.bean.Employe;
import yvs.parametrage.entrepot.Depots;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class EquipeProduction implements Serializable {

    private long id;
    private String reference;
    private String nom;
    private boolean principal;
    private boolean actif;
    private SiteProduction site = new SiteProduction();
    private Employe chefEquipe = new Employe();
    private Depots depot = new Depots();
    private List<YvsProdMembresEquipe> EmployeEquipeList;
    private boolean selectActif;
    private long idCrenoEquipe;  //Cette attribut à été ajouté pour garder l'id de la relation entre Crenau et Equipe pour eviter de créer la classe de cette relation qui n'est pas très utilisé
    private Date dateSave = new Date();

    public EquipeProduction() {
        EmployeEquipeList = new ArrayList<>();
    }

    public EquipeProduction(long id) {
        this.id = id;
        EmployeEquipeList = new ArrayList<>();
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public SiteProduction getSite() {
        return site;
    }

    public void setSite(SiteProduction site) {
        this.site = site;
    }

    public Employe getChefEquipe() {
        return chefEquipe;
    }

    public void setChefEquipe(Employe chefEquipe) {
        this.chefEquipe = chefEquipe;
    }

    public List<YvsProdMembresEquipe> getEmployeEquipeList() {
        return EmployeEquipeList;
    }

    public void setEmployeEquipeList(List<YvsProdMembresEquipe> EmployeEquipeList) {
        this.EmployeEquipeList = EmployeEquipeList;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public long getIdCrenoEquipe() {
        return idCrenoEquipe;
    }

    public void setIdCrenoEquipe(long idCrenoEquipe) {
        this.idCrenoEquipe = idCrenoEquipe;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final EquipeProduction other = (EquipeProduction) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nom;
    }

}
