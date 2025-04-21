/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.ModelReglement;
import yvs.commercial.rrr.PlanRistourne;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class CategorieClient implements Serializable {

    private long id;
    private String code;
    private String libelle;
    private String description;
    private ModelReglement model = new ModelReglement();
    private List<CategorieClient> fils;
    private CategorieClient parent;
    private List<PlanTarifaireClient> CategoriesTarifaire;
    private List<PlanReglementClient> plansReglements;
    private List<PlanRistourne> ristournes;
    private Date dateSave = new Date();
    private boolean lierClient, defaut, venteOnline;
    private boolean actif=true;
    private boolean selectActif, new_, update;

    public CategorieClient() {
        fils = new ArrayList<>();
        CategoriesTarifaire = new ArrayList<>();
        plansReglements = new ArrayList<>();
        ristournes = new ArrayList<>();
    }

    public CategorieClient(long id) {
        this.id = id;
        fils = new ArrayList<>();
        CategoriesTarifaire = new ArrayList<>();
        plansReglements = new ArrayList<>();
        ristournes = new ArrayList<>();
    }

    public CategorieClient(long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
        fils = new ArrayList<>();
        CategoriesTarifaire = new ArrayList<>();
        plansReglements = new ArrayList<>();
        ristournes = new ArrayList<>();
    }

    public boolean isUpdate() {
        return id > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public ModelReglement getModel() {
        return model;
    }

    public void setModel(ModelReglement model) {
        this.model = model;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isActif() {
        return actif;
    }

    public boolean isVenteOnline() {
        return venteOnline;
    }

    public void setVenteOnline(boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public List<PlanRistourne> getRistournes() {
        return ristournes;
    }

    public void setRistournes(List<PlanRistourne> ristournes) {
        this.ristournes = ristournes;
    }

    public List<PlanReglementClient> getPlansReglements() {
        return plansReglements;
    }

    public void setPlansReglements(List<PlanReglementClient> plansReglements) {
        this.plansReglements = plansReglements;
    }

    public CategorieClient getParent() {
        return parent;
    }

    public void setParent(CategorieClient parent) {
        this.parent = parent;
    }

    public boolean isLierClient() {
        return lierClient;
    }

    public void setLierClient(boolean lierClient) {
        this.lierClient = lierClient;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CategorieClient> getFils() {
        return fils;
    }

    public void setFils(List<CategorieClient> fils) {
        this.fils = fils;
    }

    public List<PlanTarifaireClient> getCategoriesTarifaire() {
        return CategoriesTarifaire;
    }

    public void setCategoriesTarifaire(List<PlanTarifaireClient> CategoriesTarifaire) {
        this.CategoriesTarifaire = CategoriesTarifaire;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final CategorieClient other = (CategorieClient) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
