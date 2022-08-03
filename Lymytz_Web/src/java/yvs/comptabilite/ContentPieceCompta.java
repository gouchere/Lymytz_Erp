/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Comptes;
import yvs.base.tiers.Tiers;
import yvs.entity.compta.saisie.YvsComptaContentAnalytique;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ContentPieceCompta implements Serializable {

    private long id;
    private int jour;
    private String libelle;
    private String numRef;
    private String numPiece;
    private String lettrage;
    private Date date;
    private double debit;
    private double credit;
    private Comptes compteG = new Comptes();
    private Tiers compteT = new Tiers();
    private long compteTiers;
    private String tableTiers;
    private Date echeance = new Date();
    private PiecesCompta piece = new PiecesCompta();
    private long refExterne;
    private String tableExterne;
    private double solde;
    private boolean report;
    private long idX = 100;
    private List<YvsComptaContentAnalytique> analytiques;

    public ContentPieceCompta() {
        analytiques = new ArrayList<>();
    }

    public ContentPieceCompta(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public String getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public long getRefExterne() {
        return refExterne;
    }

    public void setRefExterne(long refExterne) {
        this.refExterne = refExterne;
    }

    public String getTableExterne() {
        return tableExterne;
    }

    public void setTableExterne(String tableExterne) {
        this.tableExterne = tableExterne;
    }

    public String getLettrage() {
        return lettrage;
    }

    public void setLettrage(String lettrage) {
        this.lettrage = lettrage;
    }

    public int getJour() {
        return jour;
    }

    public void setJour(int jour) {
        this.jour = jour;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public Comptes getCompteG() {
        return compteG;
    }

    public void setCompteG(Comptes compteG) {
        this.compteG = compteG;
    }

    public Tiers getCompteT() {
        return compteT;
    }

    public void setCompteT(Tiers compteT) {
        this.compteT = compteT;
    }

//    public long getCompteTiers() {
//        return compteTiers;
//    }
//
//    public void setCompteTiers(long compteTiers) {
//        this.compteTiers = compteTiers;
//    }

    public Date getEcheance() {
        return echeance;
    }

    public void setEcheance(Date echeance) {
        this.echeance = echeance;
    }

    public PiecesCompta getPiece() {
        return piece;
    }

    public void setPiece(PiecesCompta piece) {
        this.piece = piece;
    }

    public void setNumRef(String numRef) {
        this.numRef = numRef;
    }

    public String getNumRef() {
        return numRef;
    }

    public List<YvsComptaContentAnalytique> getAnalytiques() {
        return analytiques;
    }

    public void setAnalytiques(List<YvsComptaContentAnalytique> analytiques) {
        this.analytiques = analytiques;
    }

    public long getIdX() {
        return idX;
    }

    public void setIdX(long idX) {
        this.idX = idX;
    }

    public long getCompteTiers() {
        return compteTiers;
    }

    public void setCompteTiers(long compteTiers) {
        this.compteTiers = compteTiers;
    }

    public String getTableTiers() {
        return tableTiers;
    }

    public void setTableTiers(String tableTiers) {
        this.tableTiers = tableTiers;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final ContentPieceCompta other = (ContentPieceCompta) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
