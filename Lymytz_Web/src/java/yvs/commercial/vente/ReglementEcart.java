/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.comptabilite.caisse.Caisses;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ReglementEcart implements Serializable {

    private long id;
    private String numero;
    private double montant;
    private Date dateReglement = new Date();
    private Date dateSave = new Date();
    private char statut = Constantes.STATUT_DOC_ATTENTE;
    private EcartVenteOrStock piece = new EcartVenteOrStock();
    private Caisses caisse = new Caisses();

    public ReglementEcart() {
    }

    public ReglementEcart(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public EcartVenteOrStock getPiece() {
        return piece;
    }

    public void setPiece(EcartVenteOrStock piece) {
        this.piece = piece;
    }

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ReglementEcart other = (ReglementEcart) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
