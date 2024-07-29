/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.UniteMesure;
import yvs.entity.production.base.YvsProdGammeSite;
import yvs.entity.production.base.YvsProdOperationsGamme;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class GammeArticle implements Serializable {

    private long id;
    private String reference;
    private String designation;
    private String description;
    private boolean actif, selectActif, principal, gamme;
    private Articles article = new Articles();
    private Date debutValidite, finValidite;
    private boolean permanant;
    private UniteMesure uniteTemps = new UniteMesure();
    private String modeGestion; //Pas de modif sur l'OF, Modif matière autorisé, Modif opération, Modif opération et matière
    private Date dateSave = new Date();
    private List<YvsProdOperationsGamme> operations;
    private List<YvsProdGammeSite> sites;
    private boolean masquer;

    public GammeArticle() {
        operations = new ArrayList<>();
        sites = new ArrayList<>();
    }

    public GammeArticle(long id) {
        this();
        this.id = id;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public boolean isGamme() {
        return gamme;
    }

    public void setGamme(boolean gamme) {
        this.gamme = gamme;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public List<YvsProdOperationsGamme> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsProdOperationsGamme> operations) {
        this.operations = operations;
    }

    public Date getDebutValidite() {
        return debutValidite;
    }

    public void setDebutValidite(Date debutValidite) {
        this.debutValidite = debutValidite;
    }

    public Date getFinValidite() {
        return finValidite;
    }

    public void setFinValidite(Date finValidite) {
        this.finValidite = finValidite;
    }

    public boolean isPermanant() {
        return permanant;
    }

    public void setPermanant(boolean permanant) {
        this.permanant = permanant;
    }

    public UniteMesure getUniteTemps() {
        return uniteTemps;
    }

    public void setUniteTemps(UniteMesure uniteTemps) {
        this.uniteTemps = uniteTemps;
    }

    public String getModeGestion() {
        return modeGestion;
    }

    public void setModeGestion(String modeGestion) {
        this.modeGestion = modeGestion;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsProdGammeSite> getSites() {
        return sites;
    }

    public void setSites(List<YvsProdGammeSite> sites) {
        this.sites = sites;
    }

    public boolean isMasquer() {
        return masquer;
    }

    public void setMasquer(boolean masquer) {
        this.masquer = masquer;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final GammeArticle other = (GammeArticle) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
