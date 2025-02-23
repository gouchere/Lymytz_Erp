/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.UniteMesure;
import yvs.entity.compta.analytique.YvsComptaRepartitionAnalytique;
import yvs.util.Constantes;

/**
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class CentreAnalytique implements Serializable {

    private long id;
    private String codeRef;
    private String intitule;
    private String description;
    private String niveau;
    private String type = Constantes.PRINCIPAL;//Principal, Auxiliaire
    private UniteMesure uniteOeuvre = new UniteMesure();
    private PlanAnalytique plan = new PlanAnalytique();
    private List<YvsComptaRepartitionAnalytique> repartitions;
    private boolean actif = true;
    private boolean error = true;
    private Date dateSave = new Date();

    public CentreAnalytique() {
        repartitions = new ArrayList<>();
    }

    public CentreAnalytique(long id) {
        this();
        this.id = id;
    }

    public CentreAnalytique(long id, String intitule, String description) {
        this(id);
        this.intitule = intitule;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<YvsComptaRepartitionAnalytique> getRepartitions() {
        return repartitions;
    }

    public void setRepartitions(List<YvsComptaRepartitionAnalytique> repartitions) {
        this.repartitions = repartitions;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : Constantes.PRINCIPAL : Constantes.PRINCIPAL;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UniteMesure getUniteOeuvre() {
        return uniteOeuvre;
    }

    public void setUniteOeuvre(UniteMesure uniteOeuvre) {
        this.uniteOeuvre = uniteOeuvre;
    }

    public PlanAnalytique getPlan() {
        return plan;
    }

    public void setPlan(PlanAnalytique plan) {
        this.plan = plan;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final CentreAnalytique other = (CentreAnalytique) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
