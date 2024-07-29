/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats.commercial;

/**
 *
 * @author Lymytz Dowes
 */
public class InventairePreparatoire {

    long depot;
    long article;
    String code;
    String designation;
    String numero;
    String famille;
    long unite;
    String reference;
    double prix;
    double puv;
    double pua;
    double pr;
    double stock;
    double reservation;
    double reste_a_livre;
    String categorie;
    String libelle;
    double valeur;

    public InventairePreparatoire(long depot, long article, String code, String designation, String famille, long unite, String reference, double puv, double pua) {
        this.depot = depot;
        this.article = article;
        this.code = code;
        this.designation = designation;
        this.famille = famille;
        this.unite = unite;
        this.reference = reference;
        this.puv = puv;
        this.pua = pua;
    }

    public InventairePreparatoire(long depot, long article, String code, String designation, String famille, long unite, String reference, double puv, double pua, double pr, double stock, double reservation, double reste_a_livre) {
        this(depot, article, code, designation, famille, unite, reference, puv, pua);
        this.pr = pr;
        this.stock = stock;
        this.reservation = reservation;
        this.reste_a_livre = reste_a_livre;
    }

    public InventairePreparatoire(long depot, long article, String code, String designation, String numero, String famille, long unite, String reference, double prix, double puv, double pua, double pr, double stock, double reservation, double reste_a_livre) {
        this(depot, article, code, designation, famille, unite, reference, puv, pua, pr, stock, reservation, reste_a_livre);
        this.numero = numero;
        this.prix = prix;
    }

    public InventairePreparatoire(long depot, long article, String code, String designation, String categorie, String numero, String famille, long unite, String reference, String libelle, double prix, double puv, double pua, double pr, double stock, double reservation, double reste_a_livre, double valeur) {
        this(depot, article, code, designation, numero, famille, unite, reference, prix, puv, pua, pr, stock, reservation, reste_a_livre);
        this.categorie = categorie;
        this.libelle = libelle;
        this.valeur = valeur;
    }

    public long getDepot() {
        return depot;
    }

    public void setDepot(long depot) {
        this.depot = depot;
    }

    public long getArticle() {
        return article;
    }

    public void setArticle(long article) {
        this.article = article;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFamille() {
        return famille;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public long getUnite() {
        return unite;
    }

    public void setUnite(long unite) {
        this.unite = unite;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public double getPuv() {
        return puv;
    }

    public void setPuv(double puv) {
        this.puv = puv;
    }

    public double getPua() {
        return pua;
    }

    public void setPua(double pua) {
        this.pua = pua;
    }

    public double getPr() {
        return pr;
    }

    public void setPr(double pr) {
        this.pr = pr;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getReservation() {
        return reservation;
    }

    public void setReservation(double reservation) {
        this.reservation = reservation;
    }

    public double getReste_a_livre() {
        return reste_a_livre;
    }

    public void setReste_a_livre(double reste_a_livre) {
        this.reste_a_livre = reste_a_livre;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

}
