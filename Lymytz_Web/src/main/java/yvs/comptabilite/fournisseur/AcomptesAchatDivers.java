/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.fournisseur;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.compta.YvsComptaNotifReglementDocDivers;
import yvs.entity.compta.achat.YvsComptaNotifReglementAchat;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;

/**
 *
 * @author Lymytz-pc
 */
public class AcomptesAchatDivers implements Serializable {

    private long id;
    private double montant;
    private Date dateReglement;
    private Date datePiece;
    private String numero;
    private String fournisseur;
    private String statutPiece;
    private boolean comptabilise;
    private YvsComDocAchats achats;
    private YvsComptaCaisseDocDivers divers;
    private YvsBaseModeReglement mode;
    private YvsComptaNotifReglementAchat notifs;
    private YvsComptaNotifReglementDocDivers notif_divers;
    private String type = "ACHAT";
    private String piece;

    public AcomptesAchatDivers() {
        this.notifs = new YvsComptaNotifReglementAchat();
        this.notif_divers = new YvsComptaNotifReglementDocDivers();
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

    public Date getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(Date dateReglement) {
        this.dateReglement = dateReglement;
    }

    public Date getDatePiece() {
        return datePiece;
    }

    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    public YvsComDocAchats getAchats() {
        return achats;
    }

    public void setAchats(YvsComDocAchats achats) {
        this.achats = achats;
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public YvsComptaCaisseDocDivers getDivers() {
        return divers;
    }

    public void setDivers(YvsComptaCaisseDocDivers divers) {
        this.divers = divers;
    }

    public String getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(String statutPiece) {
        this.statutPiece = statutPiece;
    }

    public YvsBaseModeReglement getMode() {
        return mode;
    }

    public void setMode(YvsBaseModeReglement mode) {
        this.mode = mode;
    }

    public YvsComptaNotifReglementAchat getNotifs() {
        return notifs;
    }

    public void setNotifs(YvsComptaNotifReglementAchat notifs) {
        this.notifs = notifs;
    }

    public YvsComptaNotifReglementDocDivers getNotif_divers() {
        return notif_divers;
    }

    public void setNotif_divers(YvsComptaNotifReglementDocDivers notif_divers) {
        this.notif_divers = notif_divers;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 41 * hash + Objects.hashCode(this.type);
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
        final AcomptesAchatDivers other = (AcomptesAchatDivers) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

    public String getType() {
        return type != null ? type : "ACHAT";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

}
