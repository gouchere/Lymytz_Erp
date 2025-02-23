/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.commercial.achat.LotReception;
import yvs.entity.production.pilotage.YvsProdFluxComposant;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ComposantsOf implements Serializable {

    private long id;
    private Articles composant = new Articles();
    private double quantitePrevu, quantiteValide, coefficient = 1;
    private String commentaire, modeArrondi;
    private String etat = Constantes.ETAT_COMPOSANT_OF_0;    //en attente, consomé,
    private String nature = Constantes.PROD_OP_TYPE_COMPOSANT_NORMAL;
    private double cout;
    private int niveau;
    private Date dateSave = new Date();
    private OrdreFabrication ordre = new OrdreFabrication();
    private Depots depotConso = new Depots();
    private Conditionnement unite = new Conditionnement();
    private LotReception lotSortie = new LotReception();
    private List<YvsProdFluxComposant> composantsUsed;
    private Double stock;
    private boolean insideCout;
    private boolean freeUse;

    public ComposantsOf() {
        composantsUsed = new ArrayList<>();
    }

    public ComposantsOf(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Articles getComposant() {
        return composant;
    }

    public void setComposant(Articles composant) {
        this.composant = composant;
    }

    public double getQuantitePrevu() {
        return quantitePrevu;
    }

    public void setQuantitePrevu(double quantitePrevu) {
        this.quantitePrevu = quantitePrevu;
    }

    public double getQuantiteValide() {
        return quantiteValide;
    }

    public void setQuantiteValide(double quantiteValide) {
        this.quantiteValide = quantiteValide;
    }

    public String getCommentaire() {
        return commentaire != null ? commentaire : "";
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getModeArrondi() {
        return modeArrondi != null ? modeArrondi.trim().length() > 0 ? modeArrondi : "E" : "E";
    }

    public void setModeArrondi(String modeArrondi) {
        this.modeArrondi = modeArrondi;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public OrdreFabrication getOrdre() {
        return ordre;
    }

    public void setOrdre(OrdreFabrication ordre) {
        this.ordre = ordre;
    }

    public Depots getDepotConso() {
        return depotConso;
    }

    public void setDepotConso(Depots depotConso) {
        this.depotConso = depotConso;
    }

    public Conditionnement getUnite() {
        return unite;
    }

    public void setUnite(Conditionnement unite) {
        this.unite = unite;
    }

    public LotReception getLotSortie() {
        return lotSortie;
    }

    public void setLotSortie(LotReception lotSortie) {
        this.lotSortie = lotSortie;
    }

    public List<YvsProdFluxComposant> getComposantsUsed() {
        return composantsUsed;
    }

    public void setComposantsUsed(List<YvsProdFluxComposant> composantsUsed) {
        this.composantsUsed = composantsUsed;
    }

    public double getCoefficient() {
        return coefficient <= 0 ? 1 : coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public boolean isInsideCout() {
        return insideCout;
    }

    public void setInsideCout(boolean insideCout) {
        this.insideCout = insideCout;
    }

    public boolean isFreeUse() {
        return freeUse;
    }

    public void setFreeUse(boolean freeUse) {
        this.freeUse = freeUse;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ComposantsOf other = (ComposantsOf) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
