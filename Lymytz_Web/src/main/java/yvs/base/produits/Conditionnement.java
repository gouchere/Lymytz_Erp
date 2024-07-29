/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.base.YvsBaseConditionnementPoint;
import yvs.entity.produits.YvsBaseArticleCodeBarre;
import yvs.entity.produits.YvsBaseArticlePack;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Conditionnement implements Serializable {

    private long id;
    private Articles article = new Articles();
    private UniteMesure unite = new UniteMesure();
    private String codeBarre;
    private double prixMin;
    private double prixAchat;
    private double prixProd;
    private double remise;
    private double prix;
    private double margeMin, tauxPua;
    private boolean byVente, byAchat, byProd, defaut, proportionPua;
    private String naturePrixMin = Constantes.NATURE_MTANT;
    private String photo;
    private boolean actif = true;
    private Date dateSave = new Date();
    private List<YvsBaseArticleCodeBarre> codesBarres;
    private List<YvsBaseConditionnementPoint> tarifaires;
    private List<YvsBaseArticlePack> packs;

    public Conditionnement() {
        codesBarres = new ArrayList<>();
        tarifaires = new ArrayList<>();
        packs = new ArrayList<>();
    }

    public Conditionnement(long id) {
        this();
        this.id = id;
    }

    public Conditionnement(long id, UniteMesure unite) {
        this(id);
        this.unite = unite;
    }

    public double getTauxPua() {
        return tauxPua;
    }

    public void setTauxPua(double tauxPua) {
        this.tauxPua = tauxPua;
    }

    public boolean isProportionPua() {
        return proportionPua;
    }

    public void setProportionPua(boolean proportionPua) {
        this.proportionPua = proportionPua;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public double getPrixProd() {
        return prixProd;
    }

    public void setPrixProd(double prixProd) {
        this.prixProd = prixProd;
    }

    public UniteMesure getUnite() {
        return unite;
    }

    public void setUnite(UniteMesure unite) {
        this.unite = unite;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public double getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(double prixMin) {
        this.prixMin = prixMin;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getNaturePrixMin() {
        return naturePrixMin;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
    }

    public boolean isByVente() {
        return byVente;
    }

    public void setByVente(boolean byVente) {
        this.byVente = byVente;
    }

    public boolean isByAchat() {
        return byAchat;
    }

    public void setByAchat(boolean byAchat) {
        this.byAchat = byAchat;
    }

    public boolean isByProd() {
        return byProd;
    }

    public void setByProd(boolean byProd) {
        this.byProd = byProd;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsBaseArticleCodeBarre> getCodesBarres() {
        return codesBarres;
    }

    public void setCodesBarres(List<YvsBaseArticleCodeBarre> codesBarres) {
        this.codesBarres = codesBarres;
    }

    public List<YvsBaseConditionnementPoint> getTarifaires() {
        return tarifaires;
    }

    public void setTarifaires(List<YvsBaseConditionnementPoint> tarifaires) {
        this.tarifaires = tarifaires;
    }

    public List<YvsBaseArticlePack> getPacks() {
        return packs;
    }

    public void setPacks(List<YvsBaseArticlePack> packs) {
        this.packs = packs;
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
        final Conditionnement other = (Conditionnement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public double getMargeMin() {
        return margeMin;
    }

    public void setMargeMin(double margeMin) {
        this.margeMin = margeMin;
    }

    public boolean getActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    

}
