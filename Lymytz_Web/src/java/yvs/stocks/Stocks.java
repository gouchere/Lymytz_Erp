/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stocks;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import yvs.base.produits.Articles;

/**
 *
 * @author GOUCHERE YVES
 */
public class Stocks implements Serializable {

    private int numero;
    private Articles produit;
    private double stockMin, stockMax, stock;

    public Stocks() {
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Articles getProduit() {
        return produit;
    }

    public void setProduit(Articles produit) {
        this.produit = produit;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getStockMax() {
        return stockMax;
    }

    public void setStockMax(double stockMax) {
        this.stockMax = stockMax;
    }

    public double getStockMin() {
        return stockMin;
    }

    public void setStockMin(double stockMin) {
        this.stockMin = stockMin;
    }
}
