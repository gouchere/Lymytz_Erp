///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.commercial;
//
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import yvs.base.produits.Articles;
//
///**
// *
// * @author lymytz
// */
//@ManagedBean
//@SessionScoped
//public class Articles implements Serializable {
//
//    private long id;
//    private Articles article_ = new Articles();
//    private double stock = 0, stock_ = 0;
//    private double pua, puv;
//    private boolean selectACtif, new_;
//
//    public Articles() {
//    }
//
//    public Articles(long id) {
//        this.id = id;
//    }
//
//    public Articles(long id, Articles article_) {
//        this.id = id;
//        this.article_ = article_;
//    }
//
//    public Articles(long id, String designation) {
//        this.id = id;
//        this.article_ = new Articles(designation);
//    }
//
//    public double getStock_() {
//        return stock_;
//    }
//
//    public void setStock_(double stock_) {
//        this.stock_ = stock_;
//    }
//
//    public double getPua() {
//        return pua;
//    }
//
//    public void setPua(double pua) {
//        this.pua = pua;
//    }
//
//    public double getPuv() {
//        return puv;
//    }
//
//    public void setPuv(double puv) {
//        this.puv = puv;
//    }
//
//    public double getStock() {
//        return stock;
//    }
//
//    public void setStock(double stock) {
//        this.stock = stock;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public boolean isSelectACtif() {
//        return selectACtif;
//    }
//
//    public void setSelectACtif(boolean selectACtif) {
//        this.selectACtif = selectACtif;
//    }
//
//    public boolean isNew_() {
//        return new_;
//    }
//
//    public void setNew_(boolean new_) {
//        this.new_ = new_;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public Articles getArticle_() {
//        return article_;
//    }
//
//    public void setArticle_(Articles article_) {
//        this.article_ = article_;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final Articles other = (Articles) obj;
//        if (this.id != other.id) {
//            return false;
//        }
//        return true;
//    }
//
//}
