/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.client;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.YvsComptaNotifReglementDocDivers;
import yvs.entity.compta.YvsComptaNotifReglementVente;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;

/**
 *
 * @author Lymytz-pc
 */
@ManagedBean
@SessionScoped
public class AcomptesVenteDivers implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private double montant;
    private Date dateReglement = new Date();
    private Date datePiece = new Date();
    private String numero;
    private String client;
    private String statutPiece;
    private String type = "VENTE";
    private String piece;
    private boolean comptabilise;
    private YvsComDocVentes ventes;
    private YvsComptaCaisseDocDivers divers;
    private YvsBaseModeReglement mode;
    private YvsComptaNotifReglementVente notifs;
    private YvsComptaNotifReglementDocDivers notif_divers;

    public AcomptesVenteDivers() {
        notifs = new YvsComptaNotifReglementVente();
        notif_divers = new YvsComptaNotifReglementDocDivers();
    }

    public AcomptesVenteDivers(long id) {
        this();
        this.id = id;
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

    public YvsComDocVentes getVentes() {
        return ventes;
    }

    public void setVentes(YvsComDocVentes ventes) {
        this.ventes = ventes;
    }

    public YvsComptaCaisseDocDivers getDivers() {
        return divers;
    }

    public void setDivers(YvsComptaCaisseDocDivers divers) {
        this.divers = divers;
    }

    public YvsComptaNotifReglementVente getNotifs() {
        return notifs;
    }

    public void setNotifs(YvsComptaNotifReglementVente notifs) {
        this.notifs = notifs;
    }

    public YvsComptaNotifReglementDocDivers getNotif_divers() {
        return notif_divers;
    }

    public void setNotif_divers(YvsComptaNotifReglementDocDivers notif_divers) {
        this.notif_divers = notif_divers;
    }

    public String getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(String statutPiece) {
        this.statutPiece = statutPiece;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public YvsBaseModeReglement getMode() {
        return mode;
    }

    public void setMode(YvsBaseModeReglement mode) {
        this.mode = mode;
    }

    public String getType() {
        return type != null ? type : "VENTE";
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

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final AcomptesVenteDivers other = (AcomptesVenteDivers) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
