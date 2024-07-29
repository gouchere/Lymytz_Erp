/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author hp Elite 8300
 */
public class SeuilStocks {

    private Long id;
    private String refARt;
    private String designation;
    private String categorie;

    private Double delaiMoy;
    private Double delaiMax;
    private Double besoinMoy;
    private Double besoinMax;
    private Double stockSecurite;
    private Double pointCmde;
    private Double coefficient;
    private Double ecartType;

    private Long conditionnement;
    private String refUnite;
    private Date date;

    public SeuilStocks() {
    }

    public SeuilStocks(Long id, String refARt, String designation) {
        this.id = id;
        this.refARt = refARt;
        this.designation = designation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefARt() {
        return refARt;
    }

    public void setRefARt(String refARt) {
        this.refARt = refARt;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getDelaiMoy() {
        return delaiMoy;
    }

    public void setDelaiMoy(Double delaiMoy) {
        this.delaiMoy = delaiMoy;
    }

    public Double getDelaiMax() {
        return delaiMax;
    }

    public void setDelaiMax(Double delaiMax) {
        this.delaiMax = delaiMax;
    }

    public Double getBesoinMoy() {
        return besoinMoy;
    }

    public void setBesoinMoy(Double besoinMoy) {
        this.besoinMoy = besoinMoy;
    }

    public Double getBesoinMax() {
        return besoinMax;
    }

    public void setBesoinMax(Double besoinMax) {
        this.besoinMax = besoinMax;
    }

    public Double getStockSecurite() {
        return stockSecurite;
    }

    public void setStockSecurite(Double stockSecurite) {
        this.stockSecurite = stockSecurite;
    }

    public Double getPointCmde() {
        return pointCmde;
    }

    public void setPointCmde(Double pointCmde) {
        this.pointCmde = pointCmde;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Double getEcartType() {
        return ecartType;
    }

    public void setEcartType(Double ecartType) {
        this.ecartType = ecartType;
    }

    public Long getConditionnement() {
        return conditionnement!=null?conditionnement:0;
    }

    public void setConditionnement(Long conditionnement) {
        this.conditionnement = conditionnement;
    }

    public String getRefUnite() {
        return refUnite;
    }

    public void setRefUnite(String refUnite) {
        this.refUnite = refUnite;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final SeuilStocks other = (SeuilStocks) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
