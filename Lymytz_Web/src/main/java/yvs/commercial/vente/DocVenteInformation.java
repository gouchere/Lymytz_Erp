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
import yvs.commercial.depot.PointLivraison;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class DocVenteInformation implements Serializable {

    private long id;
    private Date dateUpdate = new Date();
    private Date heureDebut = new Date();
    private Date dateFin = new Date();
    private Date heureFin = new Date();
    private String numCni;
    private int nombrePersonne;
    private String nomPersonneSupplementaire;
    private String modeleVehicule;
    private String numImmatriculation;
    private PointLivraison adresseLivraison = new PointLivraison();
    private DocVente facture;

    public DocVenteInformation() {
    }

    public DocVenteInformation(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public String getNumCni() {
        return numCni;
    }

    public void setNumCni(String numCni) {
        this.numCni = numCni;
    }

    public int getNombrePersonne() {
        return nombrePersonne;
    }

    public void setNombrePersonne(int nombrePersonne) {
        this.nombrePersonne = nombrePersonne;
    }

    public String getNomPersonneSupplementaire() {
        return nomPersonneSupplementaire;
    }

    public void setNomPersonneSupplementaire(String nomPersonneSupplementaire) {
        this.nomPersonneSupplementaire = nomPersonneSupplementaire;
    }

    public String getModeleVehicule() {
        return modeleVehicule;
    }

    public void setModeleVehicule(String modeleVehicule) {
        this.modeleVehicule = modeleVehicule;
    }

    public String getNumImmatriculation() {
        return numImmatriculation;
    }

    public void setNumImmatriculation(String numImmatriculation) {
        this.numImmatriculation = numImmatriculation;
    }

    public PointLivraison getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(PointLivraison adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public DocVente getFacture() {
        return facture;
    }

    public void setFacture(DocVente facture) {
        this.facture = facture;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final DocVenteInformation other = (DocVenteInformation) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
