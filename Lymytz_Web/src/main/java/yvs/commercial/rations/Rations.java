/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.tiers.Tiers;
import yvs.commercial.achat.LotReception;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.tiers.YvsBaseTiers;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Rations implements Serializable {

    private long id;
    private Articles article = new Articles();
    private Conditionnement conditionnement = new Conditionnement();
    private LotReception lot = new LotReception();
    private double quantite;
    private boolean calculPr;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private Date dateRation = new Date();
    private Tiers personnel = new Tiers();
    private String codeRation;
    private double stock;
    private List<YvsComLotReception> lots;

    public Rations() {
        lots = new ArrayList<>();
    }

    public Rations(long id) {
        this.id = id;
    }

    public String getCodeRation() {
        return codeRation != null ? codeRation.trim() : "";
    }

    public void setCodeRation(String codeRation) {
        this.codeRation = codeRation;
    }

    public boolean isCalculPr() {
        return calculPr;
    }

    public void setCalculPr(boolean calculPr) {
        this.calculPr = calculPr;
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

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public LotReception getLot() {
        return lot;
    }

    public void setLot(LotReception lot) {
        this.lot = lot;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateRation() {
        return dateRation;
    }

    public void setDateRation(Date dateRation) {
        this.dateRation = dateRation;
    }

    public Tiers getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Tiers personnel) {
        this.personnel = personnel;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public List<YvsComLotReception> getLots() {
        return lots;
    }

    public void setLots(List<YvsComLotReception> lots) {
        this.lots = lots;
    }

    public double getMaxPeriode(DaoInterfaceLocal dao) {
        if (getPersonnel() != null ? getPersonnel().getId() > 0 : false) {
            Double re = (Double) dao.loadObjectByNameQueries("YvsComParamRation.findQteByTiers", new String[]{"personnel"}, new Object[]{new YvsBaseTiers(getPersonnel().getId())});
            return (re != null) ? re : 0;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Rations other = (Rations) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
