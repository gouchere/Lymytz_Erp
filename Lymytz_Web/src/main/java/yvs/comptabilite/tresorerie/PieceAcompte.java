/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.tresorerie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.client.Client;
import yvs.entity.compta.YvsComptaNotifReglementVente;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class PieceAcompte implements Serializable {

    private long id;
    private String numRef;
    private double montant;
    private Date dateAcompte = new Date();
    private String comentaire;
    private Client client = new Client();
    private List<YvsComptaNotifReglementVente> reglements;

    public PieceAcompte() {
        reglements = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumRef() {
        return numRef;
    }

    public void setNumRef(String numRef) {
        this.numRef = numRef;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDateAcompte() {
        return dateAcompte;
    }

    public void setDateAcompte(Date dateAcompte) {
        this.dateAcompte = dateAcompte;
    }

    public String getComentaire() {
        return comentaire;
    }

    public void setComentaire(String comentaire) {
        this.comentaire = comentaire;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<YvsComptaNotifReglementVente> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaNotifReglementVente> reglements) {
        this.reglements = reglements;
    }

}
