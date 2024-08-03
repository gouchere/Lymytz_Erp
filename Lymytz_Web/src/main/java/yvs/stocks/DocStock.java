///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.stocks;
//
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import yvs.commerce.ContenuDoc;
//import yvs.commerce.Documents;
//import yvs.commerce.achats.BaseTaxe;
//import yvs.entity.commercial.stock.YvsContenuDocStock;
//import yvs.entity.commercial.stock.YvsDocStocks;
//import yvs.produits.Articles;
//
///**
// *
// * @author GOUCHERE YVES
// */
//@ManagedBean(name = "dstock")
//@SessionScoped
//public class DocStock extends Documents implements Serializable {
//
//    private String depSource;
//    private String depDest;
//    private String crenoSource;
//    private String crenoDest;
//    private String categorieC; //catégorie comptable
//    private List<BaseTaxe> listTaxes;
//    private double fraisTransport;
//    private double fraisAssurance;
//    private double fraisManutention;
//    private double fraisComission;
//    private double autreFrais;
//
//    public DocStock() {
//        listTaxes = new ArrayList<>();
//    }
//
//    public String getCrenoDest() {
//        return crenoDest;
//    }
//
//    public void setCrenoDest(String crenoDest) {
//        this.crenoDest = crenoDest;
//    }
//
//    public String getCrenoSource() {
//        return crenoSource;
//    }
//
//    public void setCrenoSource(String crenoSource) {
//        this.crenoSource = crenoSource;
//    }
//
//    public String getDepDest() {
//        return depDest;
//    }
//
//    public void setDepDest(String depDest) {
//        this.depDest = depDest;
//    }
//
//    public String getDepSource() {
//        return depSource;
//    }
//
//    public void setDepSource(String depSource) {
//        this.depSource = depSource;
//    }
//
//    public String getCategorieC() {
//        return categorieC;
//    }
//
//    public void setCategorieC(String categorieC) {
//        this.categorieC = categorieC;
//    }
//
//    public double getAutreFrais() {
//        return autreFrais;
//    }
//
//    public void setAutreFrais(double autreFrais) {
//        this.autreFrais = autreFrais;
//    }
//
//    public double getFraisAssurance() {
//        return fraisAssurance;
//    }
//
//    public void setFraisAssurance(double fraisAssurance) {
//        this.fraisAssurance = fraisAssurance;
//    }
//
//    public double getFraisComission() {
//        return fraisComission;
//    }
//
//    public void setFraisComission(double fraisComission) {
//        this.fraisComission = fraisComission;
//    }
//
//    public double getFraisManutention() {
//        return fraisManutention;
//    }
//
//    public void setFraisManutention(double fraisManutention) {
//        this.fraisManutention = fraisManutention;
//    }
//
//    public double getFraisTransport() {
//        return fraisTransport;
//    }
//
//    public void setFraisTransport(double fraisTransport) {
//        this.fraisTransport = fraisTransport;
//    }
//
//    public DocStock buildBean(YvsDocStocks doc) {
//        DocStock d = new DocStock();
//        if (doc != null) {
//            d.setNumPiece(doc.getNumPiece());
//            d.setNumRefPiece(doc.getNumRef());
//            d.setDate(doc.getDateDoc());
//            d.setDateSaveDoc(doc.getDateSaveDoc());
//            d.setLastDateSaveDoc(doc.getDateUpdateDoc());
//            d.setPrefixe(doc.getPrefixe());
//            d.setTypeDoc(doc.getTypeDoc());
//            d.setId(doc.getId());
//            d.setCrenoDest((doc.getCrenoDest() != null) ? doc.getCrenoDest().getCodeTranche() : null);
//            d.setCrenoSource((doc.getCrenoSource() != null) ? doc.getCrenoSource().getCodeTranche() : null);
//            d.setDepSource((doc.getSource() != null) ? doc.getSource().getCode() : null);
//            d.setDepDest((doc.getDestination() != null) ? doc.getDestination().getCode() : null);
//        }
//        return d;
//    }
//
//    public List<DocStock> buildListBean(List<YvsDocStocks> l) {
//        List<DocStock> ls = new ArrayList<>();
//        for (YvsDocStocks d : l) {
//            DocStock ds = buildBean(d);
//            ds.setListContent(buildContent(d));
//            ls.add(ds);
//        }
//        return ls;
//    }
//
//    List<ContenuDoc> buildContent(YvsDocStocks doc) {
//        List<ContenuDoc> l = new ArrayList<>();
//        if (doc != null) {
//            if (doc.getYvsContenuDocStockList() != null) {
//                for (YvsContenuDocStock cd : doc.getYvsContenuDocStockList()) {
//                    ContenuDoc c = new ContenuDoc();
//                    Articles a = new Articles(cd.getArticles().getId());
//                    a.setPua(cd.getArticles().getPua());
//                    a.setPuv(cd.getArticles().getPuv());
//                    a.setPuvMin(cd.getArticles().getPrixMin());
//                    c.setDesignation(cd.getArticles().getDesignation());
//                    c.setId(cd.getId());
//                    c.setPrix(cd.getPrix());
//                    c.setQuantite(cd.getQuantite());
//                    c.setProduit(a);
//                    l.add(c);
//                }
//            }
//        }
//        return l;
//    }
//}
