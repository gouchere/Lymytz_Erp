/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.tresorerie;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.tiers.YvsBaseTiers;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class JustifierBon implements Serializable {

    private long id;
    private double montant;
    private Date dateJustifier = new Date();
    private Date datePiece;
    private Date dateSave = new Date();
    private PieceTresorerie piece;
    private BonProvisoire bon = new BonProvisoire();
    private YvsComptaCaissePieceDivers piceDivers;
    private YvsComptaCaissePieceAchat pieceAchat;
    private YvsComptaCaissePieceMission pieceMission;
    private YvsBaseCaisse caisse;
    private YvsBaseTiers tiers;
    private String numeroPiece;
    private String refExterne;
    private char statutPiece = 'W';
    private String type;

    public JustifierBon() {
        piceDivers = new YvsComptaCaissePieceDivers();
        pieceAchat = new YvsComptaCaissePieceAchat();
        pieceMission = new YvsComptaCaissePieceMission();
    }

    public JustifierBon(long id) {
        this();
        this.id = id;
    }

    public JustifierBon(long id, String type) {
        this(id);
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDateJustifier() {
        return dateJustifier;
    }

    public void setDateJustifier(Date dateJustifier) {
        this.dateJustifier = dateJustifier;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }
    
    public BonProvisoire getBon() {
        return bon;
    }

    public void setBon(BonProvisoire bon) {
        this.bon = bon;
    }

    public YvsComptaCaissePieceDivers getPiceDivers() {
        return piceDivers;
    }

    public void setPiceDivers(YvsComptaCaissePieceDivers piceDivers) {
        this.piceDivers = piceDivers;
    }

    public YvsComptaCaissePieceAchat getPieceAchat() {
        return pieceAchat;
    }

    public void setPieceAchat(YvsComptaCaissePieceAchat pieceAchat) {
        this.pieceAchat = pieceAchat;
    }

    public YvsComptaCaissePieceMission getPieceMission() {
        return pieceMission;
    }

    public void setPieceMission(YvsComptaCaissePieceMission pieceMission) {
        this.pieceMission = pieceMission;
    }

    public char getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(char statutPiece) {
        this.statutPiece = statutPiece;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public YvsBaseTiers getTiers() {
        return tiers;
    }

    public void setTiers(YvsBaseTiers tiers) {
        this.tiers = tiers;
    }

    public String getNumeroPiece() {
        return numeroPiece;
    }

    public void setNumeroPiece(String numeroPiece) {
        this.numeroPiece = numeroPiece;
    }

    public String getRefExterne() {
        return refExterne;
    }

    public void setRefExterne(String refExterne) {
        this.refExterne = refExterne;
    }

    public Date getDatePiece() {
        return datePiece;
    }

    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
       

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 13 * hash + Objects.hashCode(this.type);
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
        final JustifierBon other = (JustifierBon) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

}
