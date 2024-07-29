/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.client.Client;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.commercial.vente.YvsComProformaVenteContenu;

/**
 *
 * @author Lymytz-pc
 */
@ManagedBean
@SessionScoped
public class ProformaVente implements Serializable {

    private long id;
    private String numero;
    private String statut;
    private String telephone;
    private String client;
    private String adresse;
    private String description;
    private Date dateDoc = new Date();
    private Date dateExpiration = new Date();
    private Date dateSave = new Date();
    private double montantTotal = 0;

    private List<YvsComProformaVenteContenu> contenus;

    public ProformaVente() {
        contenus = new ArrayList<>();
    }

    public ProformaVente(long id) {
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

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDateDoc() {
        return dateDoc;
    }

    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<YvsComProformaVenteContenu> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComProformaVenteContenu> contenus) {
        this.contenus = contenus;
    }

    public double getMontantTotal() {
        montantTotal = 0;
        if (contenus != null) {
            for (YvsComProformaVenteContenu contenu : contenus) {
                montantTotal += (contenu.getQuantite() * contenu.getPrix());
            }
        }
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final ProformaVente other = (ProformaVente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProformatVente{" + "id=" + id + ", numero=" + numero + '}';
    }

}
