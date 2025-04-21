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
import yvs.base.compta.ModelReglement;
import yvs.base.tiers.Tiers;
import yvs.commercial.stock.DocStock;
import yvs.comptabilite.caisse.Caisses;
import yvs.entity.commercial.stock.YvsComReglementEcartStock;
import yvs.entity.commercial.vente.YvsComReglementEcartVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.parametrage.agence.Agence;
import yvs.users.Users;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class EcartVenteOrStock implements Serializable {

    private long id;
    private double montant, reste;
    private String numero;
    private Date dateDebut = new Date();
    private Date dateEcart = new Date();
    private Date dateSave = new Date();
    private char statut = Constantes.STATUT_DOC_EDITABLE;
    private char statutRegle = Constantes.STATUT_DOC_ATTENTE;
    private Tiers tiers = new Tiers();
    private Users users = new Users();
    private Agence agence = new Agence();
    private Caisses caisse = new Caisses();
    private ModelReglement model = new ModelReglement();
    private DocStock inventaire = new DocStock();
    private List<YvsComReglementEcartVente> reglementsVente;
    private List<YvsComReglementEcartStock> reglementsStock;
    private List<YvsBaseCaisse> caisses;

    public EcartVenteOrStock() {
        caisses = new ArrayList<>();
        reglementsVente = new ArrayList<>();
        reglementsStock = new ArrayList<>();
    }

    public EcartVenteOrStock(long id) {
        this();
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

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDateEcart() {
        return dateEcart;
    }

    public void setDateEcart(Date dateEcart) {
        this.dateEcart = dateEcart;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public char getStatutRegle() {
        return statutRegle;
    }

    public void setStatutRegle(char statutRegle) {
        this.statutRegle = statutRegle;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
    }

    public ModelReglement getModel() {
        return model;
    }

    public void setModel(ModelReglement model) {
        this.model = model;
    }

    public DocStock getInventaire() {
        return inventaire;
    }

    public void setInventaire(DocStock inventaire) {
        this.inventaire = inventaire;
    }

    public List<YvsBaseCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsBaseCaisse> caisses) {
        this.caisses = caisses;
    }

    public List<YvsComReglementEcartVente> getReglementsVente() {
        return reglementsVente;
    }

    public void setReglementsVente(List<YvsComReglementEcartVente> reglementsVente) {
        this.reglementsVente = reglementsVente;
    }

    public List<YvsComReglementEcartStock> getReglementsStock() {
        return reglementsStock;
    }

    public void setReglementsStock(List<YvsComReglementEcartStock> reglementsStock) {
        this.reglementsStock = reglementsStock;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final EcartVenteOrStock other = (EcartVenteOrStock) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
